package es.mapfre.solvencia.dao.impl.maestro;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.dao.DaoBase;

public class DuracionesDao<UmicKey, Duraciones> extends DaoBase implements Map<UmicKey, Duraciones> {
	
	private static final String CACHE_NAME = "duraciones"; 
	
	public DuracionesDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}


	@Override
	public Set<Entry<UmicKey, Duraciones>> entrySet() {
		return (Set<Entry<UmicKey, Duraciones>>) this.getCache().entrySet();
	}

	@Override
	public Duraciones get(Object key) {
		return (Duraciones) this.getCache().get(key);
	}

	@Override
	public Set<UmicKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Duraciones put(UmicKey key, Duraciones duraciones) {
		 this.getCache().put(key, duraciones);
		 return duraciones;
	}

	@Override
	public Duraciones remove(Object key) {
		return (Duraciones) this.getCache().remove(key);
	}

	@Override
	public Collection<Duraciones> values() {
		return (Collection<Duraciones>) this.getCache().values();
	}

}
