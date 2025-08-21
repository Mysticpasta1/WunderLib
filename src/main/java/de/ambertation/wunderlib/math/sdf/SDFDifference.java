package de.ambertation.wunderlib.math.sdf;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.KeyDispatchDataCodec;

import de.ambertation.wunderlib.math.Float3;
import de.ambertation.wunderlib.math.Transform;

public class SDFDifference extends SDFBinaryOperation {
    private static final Codec<SDF> SDF_DISPATCH = ExtraCodecs.lazyInitializedCodec(SDF::codec2);

    public static final Codec<SDFDifference> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    Transform.CODEC.fieldOf("transform").orElse(Transform.IDENTITY).forGetter(o -> o.transform),
                    SDF_DISPATCH.fieldOf("sdf_a").forGetter(SDFOperation::getFirst),
                    SDF_DISPATCH.fieldOf("sdf_b").forGetter(SDFBinaryOperation::getSecond)
            )
            .apply(instance, SDFDifference::new)
    );

    public static final KeyDispatchDataCodec<SDFDifference> CODEC = KeyDispatchDataCodec.of(DIRECT_CODEC);

    @Override
    public KeyDispatchDataCodec<? extends SDF> codec() {
        return CODEC;
    }


    //-------------------------------------------------------------------------------
    public SDFDifference(Transform t, SDF a, SDF b) {
        super(t, a, b);
    }

    public SDFDifference(SDF a, SDF b) {
        this(Transform.IDENTITY, a, b);
    }

    @Override
    public double dist(Float3 pos) {
        return Math.max(-getFirst().dist(pos), getSecond().dist(pos));
    }

    @Override
    public void dist(EvaluationData d, Float3 pos) {
        getFirst().dist(d, pos);
        final double d0 = -d.dist;
        final SDF s0 = d.source();

        getSecond().dist(d, pos);
        if (d0 > d.dist) {
            d.dist = d0;
            d.source = s0;
        }
    }

    @Override
    public String toString() {
        return "(" + getFirst() + " - " + getSecond() + ")" + " [" + graphIndex + "]";
    }
}
