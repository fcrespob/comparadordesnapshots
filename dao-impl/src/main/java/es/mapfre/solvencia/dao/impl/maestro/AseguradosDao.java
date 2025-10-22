package es.mapfre.solvencia.dao.impl.maestro; 

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.dao.DaoBase;

public class AseguradosDao<UmicKey, Asegurados> extends DaoBase implements Map<UmicKey, Asegurados> {
	
	private static final String CACHE_NAME = "asegurados"; 
	
	public AseguradosDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<UmicKey, Asegurados>> entrySet() {
		return (Set<Entry<UmicKey, Asegurados>>) this.getCache().entrySet();
	}

	@Override
	public Asegurados get(Object key) {
		return (Asegurados) this.getCache().get(key);
	}

	@Override
	public Set<UmicKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Asegurados put(UmicKey key, Asegurados asegurados) {
		 return (Asegurados) this.getCache().put(key, asegurados);
	}

	@Override
	public Asegurados remove(Object key) {
		return (Asegurados) this.getCache().remove(key);
	}

	@Override
	public Collection<Asegurados> values() {
		return (Collection<Asegurados>) this.getCache().values();
	}

}
