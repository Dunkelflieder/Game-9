package de.fe1k.game9.states;

import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventAfterRender;
import de.fe1k.game9.events.EventListener;
import de.fe1k.game9.events.EventUpdate;
import de.fe1k.game9.network.Network;
import de.nerogar.noise.render.fontRenderer.Font;
import de.nerogar.noise.render.fontRenderer.FontRenderableString;
import de.nerogar.noise.util.Color;
import de.nerogar.noise.util.Matrix4f;
import de.nerogar.noise.util.Matrix4fUtils;

public class StateMainMenu extends GameState {

	private EventListener<EventAfterRender> eventRender = this::render;
	private EventListener<EventUpdate> eventUpdate = this::update;

	private Font font = new Font("sans-serif", 20);
	private Color color = new Color(1.0f, 1.0f, 1.0f, 1.0f);

	@Override
	public void enter() {
		Event.register(EventAfterRender.class, eventRender);
		Event.register(EventUpdate.class, eventUpdate);
	}

	@Override
	public void leave() {
		Event.unregister(EventAfterRender.class, eventRender);
		Event.unregister(EventUpdate.class, eventUpdate);
	}

	private void update(EventUpdate event) {
		if (Network.isStarted()) {
			GameState.transition(new StateWaitingForPlayers());
		}
	}

	private void render(EventAfterRender event) {
		// TODO puke
		int width = event.window.getWidth();
		int height = event.window.getHeight();
		Matrix4f projectionMatrix = new Matrix4f();
		Matrix4fUtils.setOrthographicProjection(projectionMatrix, 0f, width, height, 0, 1, -1);
		String text = "WOAH, KEINE GUI! Naja, muss ja nur ein Video abgegeben werden.\n\n"
				+ "Du kannst das Spiel starten, indem du mit TAB die Konsole aufmachst\n"
				+ "und einen Server mit 'startserver <port>' startest. Jemand zweites\n"
				+ "muss sich dann mit 'connect <ip> <port>' verbinden.";
		FontRenderableString string = new FontRenderableString(font, text, color, projectionMatrix, 1f, 1f);
		string.render(20, Math.round(0.6f * height));
		string.cleanup();
	}

}
