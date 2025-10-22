package es.mapfre.solvencia.dao.impl.maestro;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.dao.DaoBase;


public class CapitalesDao<K, Capitales> extends DaoBase implements Map<K, Capitales> {
	
	private static final String CACHE_NAME = "capitales"; 
	
	public CapitalesDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}


	@Override
	public Set<Entry<K, Capitales>> entrySet() {
		return (Set<Entry<K, Capitales>>) this.getCache().entrySet();
	}

	@Override
	public Capitales get(Object key) {
		return (Capitales) this.getCache().get(key);
	}

	@Override
	public Set<K> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Capitales put(K key, Capitales capitales) {
		 this.getCache().put(key, capitales);
		 return capitales;
	}

	@Override
	public Capitales remove(Object key) {
		return (Capitales) this.getCache().remove(key);
	}

	@Override
	public Collection<Capitales> values() {
		return (Collection<Capitales>) this.getCache().values();
	}

}
