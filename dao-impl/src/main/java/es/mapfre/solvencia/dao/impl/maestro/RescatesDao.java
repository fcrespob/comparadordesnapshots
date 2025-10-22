package es.mapfre.solvencia.dao.impl.maestro;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.dao.DaoBase;

public class RescatesDao<UmicKey, Rescates> extends DaoBase implements Map<UmicKey, Rescates> {
	
	private static final String CACHE_NAME = "rescates"; 
	
	public RescatesDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}


	@Override
	public Set<Entry<UmicKey, Rescates>> entrySet() {
		return (Set<Entry<UmicKey, Rescates>>) this.getCache().entrySet();
	}

	@Override
	public Rescates get(Object key) {
		return (Rescates) this.getCache().get(key);
	}

	@Override
	public Set<UmicKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Rescates put(UmicKey key, Rescates rescates) {
		 this.getCache().put(key, rescates);
		 return rescates;
	}

	@Override
	public Rescates remove(Object key) {
		return (Rescates) this.getCache().remove(key);
	}

	@Override
	public Collection<Rescates> values() {
		return (Collection<Rescates>) this.getCache().values();
	}

}
