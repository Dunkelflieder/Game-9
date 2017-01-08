package de.fe1k.game9.network;

import de.nerogar.noise.network.packets.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketSetClientId implements Packet {
	public int clientId;

	public PacketSetClientId() {
	}

	public PacketSetClientId(int clientId) {
		this.clientId = clientId;
	}

	@Override
	public void fromStream(DataInputStream in) throws IOException {
		clientId = in.readInt();
	}

	@Override
	public void toStream(DataOutputStream out) throws IOException {
		out.writeInt(clientId);
	}
}
