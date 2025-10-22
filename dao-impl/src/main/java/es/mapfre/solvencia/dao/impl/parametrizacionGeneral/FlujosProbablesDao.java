package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.FlujosProbablesKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;

public class FlujosProbablesDao extends DaoBase implements Map<FlujosProbablesKey, FlujosProbables>{

private static final String CACHE_NAME = "FLP0"; 
	
	public FlujosProbablesDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<FlujosProbablesKey, FlujosProbables>> entrySet() {
		return (Set<Entry<FlujosProbablesKey, FlujosProbables>>) this.getCache().entrySet();
	}

	@Override
	public FlujosProbables get(Object key) {
		if (!(key instanceof FlujosProbablesKey)) {
			return null;
		} else {
			return (FlujosProbables) this.getCache().get(key);
		}
	}

	@Override
	public Set<FlujosProbablesKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public FlujosProbables put(FlujosProbablesKey key, FlujosProbables flujosProbables) {
		 this.getCache().put(key, flujosProbables);
		 return flujosProbables;
	}

	@Override
	public FlujosProbables remove(Object key) {
		return (FlujosProbables) this.getCache().remove(key);
	}

	@Override
	public Collection<FlujosProbables> values() {
		return (Collection<FlujosProbables>) this.getCache().values();
	}
}