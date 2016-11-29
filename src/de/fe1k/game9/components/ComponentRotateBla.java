package de.fe1k.game9.components;

public class ComponentRotateBla extends Component {

	@Override
	public void update() {
		getOwner().getRotation().addZ(0.01f);
	}
}
