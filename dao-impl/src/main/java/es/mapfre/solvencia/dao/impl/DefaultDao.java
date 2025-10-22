package es.mapfre.solvencia.dao.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.dao.DaoBase;

public abstract class DefaultDao<K, V> extends DaoBase implements Map<Object, Object> {

	public DefaultDao(String cacheName) {
		super();
		super.setCacheName(cacheName);
	}
	
	@Override
	public Object get(Object key) {
		return getCache().get(key);
	}

	@Override
	public Object put(Object key, Object value) {
		return getCache().put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return getCache().remove(key);
	}

	@Override
	public Set<Object> keySet() {
		return getCache().keySet();
	}

	@Override
	public Collection<Object> values() {
		return getCache().values();
	}

	@Override
	public Set<Entry<Object, Object>> entrySet() {
		return getCache().entrySet();
	}
}
