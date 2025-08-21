package de.ambertation.wunderlib.ui.layout.components.render;

import de.ambertation.wunderlib.ui.layout.components.AbstractVanillaComponentRenderer;
import de.ambertation.wunderlib.ui.layout.components.Range;
import de.ambertation.wunderlib.ui.vanilla.Slider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RangeRenderer<N extends Number> extends AbstractVanillaComponentRenderer<Slider<N>, Range<N>> {

}
