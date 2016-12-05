package de.fe1k.game9.entities;

import de.fe1k.game9.components.Component;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventEntityDestroyed;
import de.fe1k.game9.events.EventEntityMoved;
import de.fe1k.game9.events.EventEntitySpawned;
import de.fe1k.game9.exceptions.ComponentAlreadyExistsException;
import de.fe1k.game9.exceptions.InvalidComponentException;
import de.fe1k.game9.exceptions.MissingComponentDependenciesException;
import de.nerogar.noise.util.Vector2f;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

public class Entity {
	private final long id;

	private Vector2f position;
	private float rotation;
	private Vector2f scale;
	private HashMap<Class<? extends Component>, Component> components;
	private Entity(long id) {
		this.id = id;
		this.position = new Vector2f();
		this.rotation = 0;
		this.scale = new Vector2f(1);
		components = new HashMap<>();
	}

	private void throwOnMissingDependencies() {
		if (!components.values().stream().allMatch(Component::dependenciesSatisfied)) {
			throw new MissingComponentDependenciesException();
		}
	}

	/**
	 * Looks up if the entiry has a specific component
	 * @param component the component to check for.
	 * @return true if the entity has that component, false otherwise.
	 *     Note: uses the component's equals() and hashCode().
	 */
	public boolean hasComponent(Component component) {
		return components.containsValue(component);
	}

	/**
	 * Looks up if the entity has a component by class.
	 * @param clazz the component's class
	 * @return true if the entity has a component of that class, false otherwise
	 */
	public <T extends Component> boolean hasComponent(Class<T> clazz) {
		return components.containsKey(clazz);
	}

	/**
	 * Adds a component to the entity by the component's class.
	 * @param componentClass class of the component to add.
	 * @throws ComponentAlreadyExistsException if this entity already has a component of that class.
	 * @throws InvalidComponentException if the component is invalid (e.g. can't be constructed)
	 */
	public <T extends Component> void addComponent(Class<T> componentClass) {
		if (hasComponent(componentClass)) {
			throw new ComponentAlreadyExistsException();
		}
		try {
			Component component = componentClass.newInstance();
			component.setOwner(this);
			components.put(componentClass, component);
			throwOnMissingDependencies();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new InvalidComponentException();
		}
	}

	/**
	 * Adds a component to the entity. Removes it from the previous owner first, if it had one.
	 * @param component component to add
	 * @throws ComponentAlreadyExistsException if this entity already has a component of that class.
	 */
	public void addComponent(Component component) {
		if (hasComponent(component.getClass())) {
			throw new ComponentAlreadyExistsException();
		}
		Entity previousOwner = component.getOwner();
		if (previousOwner != null) {
			previousOwner.removeComponent(component);
		}
		component.setOwner(this);
		components.put(component.getClass(), component);
		throwOnMissingDependencies();
	}

	/**
	 * Removes a component from the entity by component class.
	 * @param componentClass class of the components to remove.
	 * @return the component removed, or null if no component got removed.
	 */
	public <T extends Component> Component removeComponent(Class<T> componentClass) {
		Component removed = components.remove(componentClass);
		if (removed != null) {
			removed.setOwner(null);
		}
		throwOnMissingDependencies();
		return removed;
	}

	/**
	 * Removes a component from the entity.
	 * @param component the component to remove
	 * @return true if the component was removed, false otherwise.
	 */
	public boolean removeComponent(Component component) {
		boolean removed = components.remove(component.getClass(), component);
		if (removed) {
			component.setOwner(null);
		}
		throwOnMissingDependencies();
		return removed;
	}

	/**
	 * Looks up a component by class.
	 * @param clazz the component's class
	 * @return the component object for that class, or null if the entity doesn't have that component.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(Class<T> clazz) {
		return (T) components.get(clazz);
	}

	public long getId() {
		return id;
	}

	public void teleport(Vector2f to) {
		to = to.clone();
		Vector2f from = position.clone();
		position = to;
		Event.trigger(new EventEntityMoved(this, from, to));
	}

	public void move(Vector2f delta) {
		Vector2f from = position.clone();
		Vector2f to = position.added(delta);
		position = to;
		Event.trigger(new EventEntityMoved(this, from, to));
	}

	public Vector2f getPosition() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	public Vector2f getScale() {
		return scale;
	}

	public void destroy() {
		components.values().forEach(Component::destroy);
		components.clear();
	}

	@Override
	public boolean equals(Object o) {
		// generated by IntelliJ IDEA
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Entity entity = (Entity) o;

		return id == entity.id;
	}

	@Override
	public int hashCode() {
		// generated by IntelliJ IDEA
		return (int) (id ^ (id >>> 32));
	}

	@Override
	public String toString() {
		return "Entity{" +
				"id=" + id +
				'}';
	}

	////////////////// STATIC STUFF //////////////////

	private static Map<Long, Entity> entities = new HashMap<>();
	private static Random uniqueIdRandom = new Random();

	public Entity getById(long id) {
		return entities.get(id);
	}

	public static Stream<Entity> getAll() {
		return entities.values().stream();
	}

	/**
	 * Returns all entities that have a given component specified by class.
	 * @param componentClass the component class to check for
	 * @return stream of entities that have the component
	 */
	public static <T extends Component> Stream<Entity> getAllWithComponent(Class<T> componentClass) {
		return entities.values().stream().filter(
				entity -> entity.hasComponent(componentClass)
		);
	}

	/**
	 * Returns all entities that have all the given components specified by classes.
	 * @param componentClasses list of component classes to check for
	 * @return stream of entities that have all the components
	 */
	public static Stream<Entity> getAllWithComponents(Class<? extends Component>... componentClasses) {
		return entities.values().stream().filter(
				entity -> Arrays.stream(componentClasses).allMatch(entity::hasComponent)
		);
	}

	public static <T extends Component> Stream<T> getComponents(Class<T> componentClass) {
		return getAllWithComponent(componentClass).map(entity -> entity.getComponent(componentClass));
	}

	public static Entity spawn() {
		Entity entity = new Entity(getUniqueId());
		entities.put(entity.getId(), entity);
		Event.trigger(new EventEntitySpawned(entity));
		return entity;
	}

	public static void despawn(long entityId) {
		Entity removedEntity = entities.remove(entityId);
		if (removedEntity != null) {
			Event.trigger(new EventEntityDestroyed(removedEntity));
		}

	}

	private static long getUniqueId() {
		return uniqueIdRandom.nextLong();
	}

}
