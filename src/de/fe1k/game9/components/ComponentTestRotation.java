package de.fe1k.game9.components;

import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventListener;
import de.fe1k.game9.events.EventUpdate;

public class ComponentTestRotation extends Component {

	public ComponentTestRotation() {
		Event.register(EventUpdate.class, this::update);
	}

	private void update(EventUpdate event) {
		getOwner().ifPresent(
				owner -> owner.getRotation().addZ(event.deltaTime)
		);
	}

	@Override
	public void destroy() {
		Event.unregister((EventListener<EventUpdate>) this::update);
	}
}
