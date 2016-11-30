package de.fe1k.game9.components;

import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.exceptions.ComponentNotFoundException;

import java.util.Optional;

public abstract class Component {
	private Optional<Entity> owner = Optional.empty();

	/**
	 * @return the entity that owns this component.
	 */
	public Optional<Entity> getOwner() {
		return owner;
	}

	/**
	 * Sets the entity that owns this component.
	 * Not part of constructor for all components to have a default constructor.
	 * @param owner the entity's owner
	 */
	public void setOwner(Entity owner) {
		this.owner = Optional.ofNullable(owner);
	}

	public boolean dependenciesSatisfied() {
		if (!getOwner().isPresent()) {
			return false;
		}
		Depends depends = getClass().getAnnotation(Depends.class);
		if (depends == null) {
			return true;
		}
		for (Class<? extends Component> componentClass : depends.components()) {
			if (!getOwner().get().hasComponent(componentClass)) {
				return false;
			}
		}
		return true;
	}

	public void destroy() {}

	////////////////// STATIC STUFF //////////////////

	/**
	 * Looks up a component class by name.
	 *
	 * @param name name of the component
	 * @return class of the component, or null if not found
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Component> Class<T> getByName(String name) {
		Class clazz;
		try {
			clazz = Class.forName(Component.class.getPackage().getName() + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new ComponentNotFoundException();
		}
		if (!Component.class.isAssignableFrom(clazz)) {
			throw new ComponentNotFoundException();
		}
		return (Class<T>) clazz;
	}

}
