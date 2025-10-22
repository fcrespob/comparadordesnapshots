package es.mapfre.solvencia.dao.impl.maestro;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.dao.DaoBase;

public class RentasDao<UmicKey, Rentas> extends DaoBase implements Map<UmicKey, Rentas> {
	
	private static final String CACHE_NAME = "rentas"; 
	
	public RentasDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}


	@Override
	public Set<Entry<UmicKey, Rentas>> entrySet() {
		return (Set<Entry<UmicKey, Rentas>>) this.getCache().entrySet();
	}

	@Override
	public Rentas get(Object key) {
		return (Rentas) this.getCache().get(key);
	}

	@Override
	public Set<UmicKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Rentas put(UmicKey key, Rentas rentas) {
		 this.getCache().put(key, rentas);
		 return rentas;
	}

	@Override
	public Rentas remove(Object key) {
		return (Rentas) this.getCache().remove(key);
	}

	@Override
	public Collection<Rentas> values() {
		return (Collection<Rentas>) this.getCache().values();
	}

}