package de.ambertation.wunderlib.math.sdf;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.KeyDispatchDataCodec;

import de.ambertation.wunderlib.math.Bounds;
import de.ambertation.wunderlib.math.Float3;
import de.ambertation.wunderlib.math.Transform;

public class SDFInvert extends SDFOperation {
    private static final Codec<SDF> SDF_DISPATCH = ExtraCodecs.lazyInitializedCodec(SDF::codec2);

    public static final Codec<SDFInvert> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    Transform.CODEC.fieldOf("transform").orElse(Transform.IDENTITY).forGetter(o -> o.transform),
                    SDF_DISPATCH.fieldOf("sdf").forGetter(SDFOperation::getFirst)
            )
            .apply(instance, SDFInvert::new)
    );


    public static final KeyDispatchDataCodec<SDFInvert> CODEC = KeyDispatchDataCodec.of(DIRECT_CODEC);

    @Override
    public KeyDispatchDataCodec<? extends SDF> codec() {
        return CODEC;
    }


    //-------------------------------------------------------------------------------
    protected SDFInvert(Transform t, SDF sdf) {
        super(t, sdf);
    }

    protected SDFInvert(SDF sdf) {
        this(Transform.IDENTITY, sdf);
    }

    @Override
    public double dist(Float3 pos) {
        return -getFirst().dist(pos);
    }

    @Override
    public void dist(EvaluationData d, Float3 pos) {
        getFirst().dist(d, pos);
        d.dist *= -1;
    }

    @Override
    public String toString() {
        return "!" + getFirst() + " [" + graphIndex + "]";
    }

    @Override
    public Bounds getBoundingBox() {
        return getFirst().getBoundingBox();
    }
}
