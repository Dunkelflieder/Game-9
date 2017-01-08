package de.fe1k.game9.events;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EventLoadMap implements EventToClients {
	public String mapName;

	public EventLoadMap() {
	}

	public EventLoadMap(String mapName) {
		this.mapName = mapName;
	}

	@Override
	public void fromStream(DataInputStream in) throws IOException {
		int length = in.readInt();
		char[] chars = new char[length];
		for (int i = 0; i < length; i++) {
			chars[i] = in.readChar();
		}
		mapName = new String(chars);
	}

	@Override
	public void toStream(DataOutputStream out) throws IOException {
		out.writeInt(mapName.length());
		for (char c : mapName.toCharArray()) {
			out.writeChar(c);
		}
	}
}
