package es.mapfre.solvencia.dao.impl.maestro;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.dao.DaoBase;

public class PrimasDao<UmicKey, Primas> extends DaoBase implements Map<UmicKey, Primas> {
	
	private static final String CACHE_NAME = "primas"; 
	
	public PrimasDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}


	@Override
	public Set<Entry<UmicKey, Primas>> entrySet() {
		return (Set<Entry<UmicKey, Primas>>) this.getCache().entrySet();
	}

	@Override
	public Primas get(Object key) {
		return (Primas) this.getCache().get(key);
	}

	@Override
	public Set<UmicKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Primas put(UmicKey key, Primas primas) {
		 this.getCache().put(key, primas);
		 return primas;
	}

	@Override
	public Primas remove(Object key) {
		return (Primas) this.getCache().remove(key);
	}

	@Override
	public Collection<Primas> values() {
		return (Collection<Primas>) this.getCache().values();
	}

}
