package de.fe1k.game9.network;

import de.fe1k.game9.events.EventNetworked;
import de.fe1k.game9.exceptions.EventNotFoundException;
import de.fe1k.game9.exceptions.InvalidEventException;
import de.nerogar.noise.network.packets.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketNetworkedEvent implements Packet {

	public EventNetworked event;

	public PacketNetworkedEvent() {
	}

	public PacketNetworkedEvent(EventNetworked event) {
		this.event = event;
	}

	@Override
	public void fromStream(DataInputStream in) throws IOException {
		int nameLength = in.readInt();
		char[] chars = new char[nameLength];
		for (int i = 0; i < nameLength; i++) {
			chars[i] = in.readChar();
		}
		String eventName = new String(chars);
		Class<? extends EventNetworked> eventClass = getByName(eventName);
		try {
			event = eventClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new InvalidEventException();
		}
		event.fromStream(in);
	}

	@Override
	public void toStream(DataOutputStream out) throws IOException {
		String eventName = event.getClass().getSimpleName();
		out.writeInt(eventName.length());
		for (char c : eventName.toCharArray()) {
			out.writeChar(c);
		}
		event.toStream(out);
	}

	@SuppressWarnings("unchecked")
	private static <T extends EventNetworked> Class<T> getByName(String name) {
		Class clazz;
		try {
			clazz = Class.forName(EventNetworked.class.getPackage().getName() + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new EventNotFoundException();
		}
		if (!EventNetworked.class.isAssignableFrom(clazz)) {
			throw new EventNotFoundException();
		}
		return (Class<T>) clazz;
	}
}
