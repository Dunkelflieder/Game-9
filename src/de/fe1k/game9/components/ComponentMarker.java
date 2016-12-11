package de.fe1k.game9.components;

public class ComponentMarker extends Component {
	public static final int MARKER_START = 0x00FF00;
	public static final int MARKER_PATH = 0x00FFFF;

	private final int marker;

	public ComponentMarker(int marker) {
		this.marker = marker;
	}

	public int getMarker() {
		return marker;
	}
}
