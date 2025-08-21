package de.ambertation.wunderlib.math.sdf;

import com.mojang.serialization.Codec;
import de.ambertation.wunderlib.WunderLib;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = WunderLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class SdfRegistries {
    private SdfRegistries() {}

    public static final ResourceKey<Registry<Codec<? extends SDF>>> SDF_REGISTRY_KEY =
            ResourceKey.createRegistryKey(new ResourceLocation(WunderLib.MOD_ID, "sdf"));

    public static Supplier<IForgeRegistry<Codec<? extends SDF>>> SDF_REGISTRY;

    @SubscribeEvent
    public static void onNewRegistry(NewRegistryEvent event) {
        SDF_REGISTRY = event.create(new RegistryBuilder<Codec<? extends SDF>>()
                .setName(SDF_REGISTRY_KEY.location())
                .disableSaving()
                .disableSync());
    }

}
