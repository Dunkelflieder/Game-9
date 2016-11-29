package de.fe1k.game9.utils;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.UnaryOperator;

public class IDList<T extends IDList.UniqueId> extends ArrayList<T> {

	public interface UniqueId {
		long getId();
	}

	public IDList() {
		super();
	}

	public IDList(Collection<T> c) {
		super(c);
		sortById();
	}

	private void sortById() {
		// TODO directly insert at correct place instead
		sort((a, b) -> (int) (a.getId() - b.getId()));
	}

	@Override
	public boolean add(T entity) {
		super.add(entity);
		sortById();
		return true;
	}

	@Override
	public void add(int index, T entity) {
		throw new NotImplementedException();
	}

	@Override
	public T set(int index, T element) {
		throw new NotImplementedException();
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean result = super.addAll(c);
		sortById();
		return result;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		throw new NotImplementedException();
	}

	@Override
	public void replaceAll(UnaryOperator<T> operator) {
		super.replaceAll(operator);
		sortById();
	}

	private int getIndex(long id) {
		// binary serch
		int l = 0;
		int r = size() - 1;
		int p;
		while (l <= r) {
			p = (l + r) / 2;
			if (get(p).getId() == id) {
				return p;
			}
			if (get(p).getId() < id) {
				l = p + 1;
			} else {
				r = p - 1;
			}
		}
		return -1;
	}

	public T getById(long id) {
		int index = getIndex(id);
		if (index < 0) {
			return null;
		}
		return get(index);

	}

	public boolean replace(long id, T newItem) {
		int index = getIndex(id);
		if (index < 0) {
			return false;
		}
		super.set(index, newItem);
		return true;
	}

}
