package de.fe1k.game9.components;

import de.fe1k.game9.utils.Bounding;

public class ComponentBounding extends Component {
	public static final int LAYER_NONE = 0;
	public static final int LAYER_MAP  = 1;
	public static final int LAYER_PLAYER = 1 << 1;
	public static final int LAYER_PARTICLES = 1 << 2;
	public static final int LAYER_ALL = ~0;

	public Bounding bounding;
	public int layerSelf;
	public int layerCollides;
	public ComponentBounding(Bounding bounding, int layerSelf, int layerCollides) {
		this.bounding = bounding;
		this.layerSelf = layerSelf;
		this.layerCollides = layerCollides;
	}
}
