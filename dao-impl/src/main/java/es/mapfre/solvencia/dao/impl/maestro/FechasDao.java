package es.mapfre.solvencia.dao.impl.maestro;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.dao.DaoBase;

public class FechasDao<UmicKey, Fechas> extends DaoBase implements Map<UmicKey, Fechas> {
	
	private static final String CACHE_NAME = "fechas"; 
	
	public FechasDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<UmicKey, Fechas>> entrySet() {
		return (Set<Entry<UmicKey, Fechas>>) this.getCache().entrySet();
	}

	@Override
	public Fechas get(Object key) {
		return (Fechas) this.getCache().get(key);
	}

	@Override
	public Set<UmicKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Fechas put(UmicKey key, Fechas fechas) {
		 this.getCache().put(key, fechas);
		 return fechas;
	}

	@Override
	public Fechas remove(Object key) {
		return (Fechas) this.getCache().remove(key);
	}

	@Override
	public Collection<Fechas> values() {
		return (Collection<Fechas>) this.getCache().values();
	}

}