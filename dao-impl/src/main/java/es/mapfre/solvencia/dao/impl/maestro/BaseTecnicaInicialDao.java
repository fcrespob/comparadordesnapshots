package es.mapfre.solvencia.dao.impl.maestro; 

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.dao.DaoBase;

public class BaseTecnicaInicialDao<K, BaseTecnicaInicial> extends DaoBase implements Map<K, BaseTecnicaInicial> {
	
	private static final String CACHE_NAME = "baseTecnicaInicial"; 
	
	public BaseTecnicaInicialDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<K, BaseTecnicaInicial>> entrySet() {
		return (Set<Entry<K, BaseTecnicaInicial>>) this.getCache().entrySet();
	}

	@Override
	public BaseTecnicaInicial get(Object key) {
		return (BaseTecnicaInicial) this.getCache().get(key);
	}

	@Override
	public Set<K> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public BaseTecnicaInicial put(K key, BaseTecnicaInicial baseTecnicaInicial) {
		 return (BaseTecnicaInicial) this.getCache().put(key, baseTecnicaInicial);
	}

	@Override
	public BaseTecnicaInicial remove(Object key) {
		return (BaseTecnicaInicial) this.getCache().remove(key);
	}

	@Override
	public Collection<BaseTecnicaInicial> values() {
		return (Collection<BaseTecnicaInicial>) this.getCache().values();
	}

}
