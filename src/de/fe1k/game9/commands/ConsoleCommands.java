package de.fe1k.game9.commands;

import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventToggleCollisions;
import de.fe1k.game9.events.EventToggleFlymode;
import de.fe1k.game9.events.EventTogglePhysics;
import de.nerogar.noise.util.Logger;

import java.util.function.Consumer;

public enum ConsoleCommands {
	PHYSICS   (new OnOffHandler(enabled -> Event.trigger(new EventTogglePhysics(enabled)))),
	COLLISIONS(new OnOffHandler(enabled -> Event.trigger(new EventToggleCollisions(enabled)))),
	FLYMODE   (new OnOffHandler(enabled -> {
		Event.trigger(new EventToggleFlymode(enabled));
		Event.trigger(new EventTogglePhysics(!enabled));
		Event.trigger(new EventToggleCollisions(!enabled));
	}));

	private static class OnOffHandler implements Consumer<String[]> {

		private Consumer<Boolean> consumer;

		public OnOffHandler(Consumer<Boolean> consumer) {
			this.consumer = consumer;
		}

		@Override
		public void accept(String[] args) {
			if (args.length != 2 || (!args[1].equals("on") && !args[1].equals("off"))) {
				Logger.getErrorStream().printf("Command argument for command %s can only be on/off.", args[0]);
				return;
			}
			consumer.accept(args[1].equals("on"));
		}
	}

	private Consumer<String[]> commandConsumer;

	ConsoleCommands(Consumer<String[]> commandConsumer) {
		this.commandConsumer = commandConsumer;
	}

	public Consumer<String[]> getCommandConsumer() {
		return commandConsumer;
	}

	public static ConsoleCommands getByName(String name) {
		for (ConsoleCommands consoleCommand : values()) {
			if (consoleCommand.name().equalsIgnoreCase(name)) {
				return consoleCommand;
			}
		}
		return null;
	}
}
