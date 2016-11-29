package de.fe1k.game9.entities;

import de.fe1k.game9.components.Component;
import de.fe1k.game9.exceptions.ComponentAlreadyExistsException;
import de.fe1k.game9.exceptions.InvalidComponentException;
import de.fe1k.game9.utils.IDList;
import de.nerogar.noise.util.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class Entity implements IDList.UniqueId {
	private long id;
	private Vector3f position;
	private Vector3f rotation;
	private Vector3f scale;
	private HashMap<Class<? extends Component>, Component> components;

	private Entity(long id) {
		this.id = id;
		this.position = new Vector3f();
		this.rotation = new Vector3f();
		this.scale = new Vector3f(1);
		components = new HashMap<>();
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
		return removed;
	}

	public void update() {
		components.values().forEach(Component::update);
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

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public Vector3f getScale() {
		return scale;
	}

	public void destroy() {
		components.values().forEach(Component::destroy);
		components.clear();
	}

	////////////////// STATIC STUFF //////////////////

	private static IDList<Entity> entities = new IDList<>();
	private static Random uniqueIdRandom = new Random();

	public Entity getById(long id) {
		return entities.getById(id);
	}

	/**
	 * Returns all entities that have a given component specified by class.
	 * @param componentClass the component class to check for
	 * @return stream of entities that have the component
	 */
	public static <T extends Component> Stream<Entity> getAllWithComponent(Class<T> componentClass) {
		return entities.stream().filter(
				entity -> entity.hasComponent(componentClass)
		);
	}

	/**
	 * Returns all entities that have all the given components specified by classes.
	 * @param componentClasses list of component classes to check for
	 * @return stream of entities that have all the components
	 */
	public static <T extends Component> Stream<Entity> getAllWithComponents(List<Class<T>> componentClasses) {
		return entities.stream().filter(
				entity -> componentClasses.stream().allMatch(entity::hasComponent)
		);
	}

	public static <T extends Component> Stream<T> getComponents(Class<T> componentClass) {
		return getAllWithComponent(componentClass).map(entity -> entity.getComponent(componentClass));
	}

	public static Stream<Entity> getAll() {
		return entities.stream();
	}

	public static Entity spawn() {
		Entity entity = new Entity(getUniqueId());
		entities.add(entity);
		return entity;
	}

	public static Entity spawn(List<Component> components) {
		Entity entity = spawn();
		components.forEach(entity::addComponent);
		return entity;
	}

	private static long getUniqueId() {
		return uniqueIdRandom.nextLong();
	}

}
