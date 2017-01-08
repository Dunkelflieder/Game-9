package de.fe1k.game9.states;

public abstract class GameState {
	public abstract void enter();
	public abstract void leave();

	////////////////// STATIC STUFF //////////////////

	private static GameState currentState;

	public static void transition(GameState newState) {
		if (currentState != null) {
			currentState.leave();
		}
		newState.enter();
		currentState = newState;
	}

	public static GameState getCurrent() {
		return currentState;
	}

}
