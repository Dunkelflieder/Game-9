package de.fe1k.game9.commands;

import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventListener;
import de.fe1k.game9.events.EventUpdate;
import de.nerogar.noise.input.KeyboardKeyEvent;
import de.nerogar.noise.render.GLWindow;
import de.nerogar.noise.render.fontRenderer.Font;
import de.nerogar.noise.render.fontRenderer.FontRenderableString;
import de.nerogar.noise.util.Color;
import de.nerogar.noise.util.Logger;
import de.nerogar.noise.util.Matrix4f;
import de.nerogar.noise.util.Matrix4fUtils;

import java.util.ArrayDeque;
import java.util.Queue;

import static org.lwjgl.glfw.GLFW.*;

public class Console {
	private GLWindow window;
	private String   input;
	private Matrix4f projectionMatrix;
	private Color    inputColor;
	private Color    linesColor;
	private Font     font;

	private boolean                     enabled;
	private Queue<FontRenderableString> lines;
	private EventListener<EventUpdate>  eventUpdate = this::update;

	private static final int numLines = 10;
	private static final int fontSize = 15;

	public Console(GLWindow window) {
		this.window = window;
		input = "";
		projectionMatrix = new Matrix4f();
		inputColor = new Color(0.0f, 1.0f, 0.0f, 1.0f);
		linesColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
		font = new Font("consolas", fontSize);
		enabled = false;
		lines = new ArrayDeque<>();

		updateProjectionMatrix(window.getWidth(), window.getHeight());
		Event.register(EventUpdate.class, eventUpdate);

		Logger.addListener(Logger.WARNING, this::addLine);
	}

	private void addLine(String text) {
		FontRenderableString string = new FontRenderableString(font, text, linesColor, projectionMatrix, 1f, 1f);
		lines.add(string);
	}

	private void update(EventUpdate updateEvent) {
		for (KeyboardKeyEvent keyEvent : window.getInputHandler().getKeyboardKeyEvents()) {
			boolean isTriggered = keyEvent.action == GLFW_PRESS || keyEvent.action == GLFW_REPEAT;
			if (!isTriggered) continue;
			if (keyEvent.key == GLFW_KEY_TAB) {
				enabled = !enabled;
			}
			if (!enabled) return;
			if (input.isEmpty()) continue;
			if (keyEvent.key == GLFW_KEY_BACKSPACE) {
				input = input.substring(0, input.length() - 1);
			} else if (keyEvent.key == GLFW_KEY_ENTER || keyEvent.key == GLFW_KEY_KP_ENTER) {
				addLine("> " + input);
				parse(input.toLowerCase());
				input = "";
			}
		}
		String newText = window.getInputHandler().getInputText();
		if (!enabled) return;
		if (newText.matches("\\A\\p{ASCII}*\\z")) {
			// only ASCII support
			input += newText;
		}
	}

	private void parse(String text) {
		String[] args = text.split(" ", 2);
		ConsoleCommands command = ConsoleCommands.getByName(args[0]);
		if (command == null) {
			Logger.getErrorStream().printf("Unknown command: %s\n", args[0]);
		} else {
			command.getCommandConsumer().accept(args);
		}
	}

	public void render() {
		while (lines.size() > numLines) lines.poll().cleanup();
		if (!enabled) return;
		int margin = 8;
		int spacer = fontSize + 3;
		int i = 1;
		for (FontRenderableString line : lines) {
			line.render(margin, window.getHeight() - i*spacer - margin);
			i++;
		}
		FontRenderableString string = new FontRenderableString(font, "> " + input, inputColor, projectionMatrix, 1f, 1f);
		string.render(margin, window.getHeight() - i*spacer - margin);
		string.cleanup();
	}

	public void updateProjectionMatrix(int width, int height) {
		Matrix4fUtils.setOrthographicProjection(projectionMatrix, 0f, width, height, 0, 1, -1);
	}
}
