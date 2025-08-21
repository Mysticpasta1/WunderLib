package de.ambertation.wunderlib.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public abstract class ServerBoundPacketHandler<D> {
    protected ResourceLocation CHANNEL_ID;
    protected SimpleChannel CHANNEL;

    private static final String PROTOCOL_VERSION = "1";
    private static int NEXT_ID = 0;

    public static <D, T extends ServerBoundPacketHandler<D>> T register(ResourceLocation channelId, T handler) {
        handler.CHANNEL_ID = channelId;
        handler.CHANNEL = NetworkRegistry.newSimpleChannel(
                channelId,
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );

        handler.CHANNEL
                .messageBuilder(ServerBoundPacketHandler.ContentMessage.class, nextId(), NetworkDirection.PLAY_TO_SERVER)
                .encoder((msg, buf) -> {
                    @SuppressWarnings("unchecked")
                    D payload = (D) msg.content;        // encode the payload you put in when sending
                    handler.serializeOnClient(buf, payload);
                })
                .decoder(buf -> new ServerBoundPacketHandler.ContentMessage(handler.decodeOnServer(buf)))
                .consumerMainThread((msg, ctxSup) -> {
                    var ctx = ctxSup.get();             // <-- get the real context
                    ServerPlayer player = ctx.getSender();
                    if (player != null) {
                        MinecraftServer server = player.getServer(); // or player.serverLevel().getServer()
                        @SuppressWarnings("unchecked")
                        D payload = (D) msg.content;
                        handler.processOnGameThread(server, player, payload);
                    }
                    ctx.setPacketHandled(true);         // <-- mark handled on the context
                })
                .add();

        handler.onRegister();
        return handler;
    }

    /** Client-side send helper. Safe no-op on dedicated servers. */
    public void sendToServer(D content) {
        if (FMLEnvironment.dist == Dist.CLIENT && CHANNEL != null) {
            CHANNEL.sendToServer(new ContentMessage(content));
        }
    }

    /** Write the client payload into the buffer (client thread). */
    protected abstract void serializeOnClient(FriendlyByteBuf buf, D content);

    /** Read the payload from the buffer (netty decode thread). Keep it pure—no world access here. */
    protected abstract D decodeOnServer(FriendlyByteBuf buf);

    /** Run on the logical server thread. Do your gameplay work here. */
    protected abstract void processOnGameThread(MinecraftServer server, ServerPlayer player, D content);

    /** Optional hook after registration. */
    protected void onRegister() {}

    /** Simple wrapper message carrying your generic content. */
    protected static final class ContentMessage {
        final Object content;
        ContentMessage(Object content) { this.content = content; }
    }

    private static synchronized int nextId() { return NEXT_ID++; }
}
