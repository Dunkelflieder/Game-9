package de.fe1k.game9.components;

import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventListener;
import de.fe1k.game9.events.EventUpdate;

import java.util.List;

public class ComponentFallingBlock extends Component {

	private EventListener<EventUpdate> eventUpdate;

	public ComponentFallingBlock() {
		eventUpdate = this::update;
		Event.register(EventUpdate.class, eventUpdate);
	}

	private void update(EventUpdate event) {
		List<ComponentPlayer> components = Entity.getComponents(ComponentPlayer.class);

		if (components.isEmpty()) return;

		Entity player = components.get(0).getOwner();

		if (player.getPosition().getX() > getOwner().getPosition().getX()) {
			Event.unregister(EventUpdate.class, eventUpdate);

			getOwner().addComponent(new ComponentMoving());
		}

	}

	@Override
	public void destroy() {
		Event.unregister(EventUpdate.class, eventUpdate);
	}
}
