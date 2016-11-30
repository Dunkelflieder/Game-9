package de.fe1k.game9.components;

import de.fe1k.game9.utils.Bounding;

public class ComponentBounding extends Component {
	private Bounding bounding;

	public ComponentBounding(Bounding bounding) {
		this.bounding = bounding;
	}

	public void setBounding(Bounding bounding) {
		this.bounding = bounding;
	}

	public Bounding getBounding() {
		return bounding;
	}

}
