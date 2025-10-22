package es.mapfre.solvencia.dao.impl.maestro;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.dao.DaoBase;

public class ComisionesDao<UmicKey, Comisiones> extends DaoBase implements Map<UmicKey, Comisiones> {
	
	private static final String CACHE_NAME = "comisiones"; 
	
	public ComisionesDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}


	@Override
	public Set<Entry<UmicKey, Comisiones>> entrySet() {
		return (Set<Entry<UmicKey, Comisiones>>) this.getCache().entrySet();
	}

	@Override
	public Comisiones get(Object key) {
		return (Comisiones) this.getCache().get(key);
	}

	@Override
	public Set<UmicKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Comisiones put(UmicKey key, Comisiones comisiones) {
		 this.getCache().put(key, comisiones);
		 return comisiones;
	}

	@Override
	public Comisiones remove(Object key) {
		return (Comisiones) this.getCache().remove(key);
	}

	@Override
	public Collection<Comisiones> values() {
		return (Collection<Comisiones>) this.getCache().values();
	}

}
