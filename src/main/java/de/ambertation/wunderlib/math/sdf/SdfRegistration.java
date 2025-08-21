package de.ambertation.wunderlib.math.sdf;

import de.ambertation.wunderlib.WunderLib;
import de.ambertation.wunderlib.math.sdf.shapes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = WunderLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class SdfRegistration {
    private SdfRegistration() {}

    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        event.register(SdfRegistries.SDF_REGISTRY_KEY, helper -> {
            helper.register(id("union"),       SDFUnion.CODEC.codec());
            helper.register(id("intersect"),   SDFIntersection.CODEC.codec());
            helper.register(id("dif"),         SDFDifference.CODEC.codec());
            helper.register(id("invert"),      SDFInvert.CODEC.codec());

            helper.register(id("empty"),       Empty.CODEC.codec());
            helper.register(id("sphere"),      Sphere.CODEC.codec());
            helper.register(id("box"),         Box.CODEC.codec());
            helper.register(id("cylinder"),    Cylinder.CODEC.codec());
            helper.register(id("ellipsoid"),   Ellipsoid.CODEC.codec());
        });
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(WunderLib.MOD_ID, path);
    }
}
