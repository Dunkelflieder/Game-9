package de.fe1k.game9.components;

import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventListener;
import de.fe1k.game9.events.EventUpdate;

public class ComponentFallingBlock extends Component {

	private EventListener<EventUpdate> eventUpdate;

	public ComponentFallingBlock() {
		eventUpdate = this::update;
		Event.register(EventUpdate.class, eventUpdate);
	}

	private void update(EventUpdate event) {
		ComponentPlayer player = Entity.getFirstComponent(ComponentPlayer.class);

		if (player == null) {
			return;
		}

		if (player.getOwner().getPosition().getX() > getOwner().getPosition().getX()) {
			Event.unregister(EventUpdate.class, eventUpdate);

			getOwner().addComponent(new ComponentMoving());
		}

	}

	@Override
	public void destroy() {
		Event.unregister(EventUpdate.class, eventUpdate);
	}
}
