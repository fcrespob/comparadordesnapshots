package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.SwCobroComisionesKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.SwCobroComisiones;

public class SwCobroComisionesDao extends DaoBase implements Map<SwCobroComisionesKey, SwCobroComisiones>{

private static final String CACHE_NAME = "SWCOBROCOMISIONES"; 
	
	public SwCobroComisionesDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<SwCobroComisionesKey, SwCobroComisiones>> entrySet() {
		return (Set<Entry<SwCobroComisionesKey, SwCobroComisiones>>) this.getCache().entrySet();
	}

	@Override
	public SwCobroComisiones get(Object key) {
		if (!(key instanceof SwCobroComisionesKey)) {
			return null;
		} else {
			return (SwCobroComisiones) this.getCache().get(key);
		}
	}

	@Override
	public Set<SwCobroComisionesKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public SwCobroComisiones put(SwCobroComisionesKey key, SwCobroComisiones swCobroComisiones) {
		 this.getCache().put(key, swCobroComisiones);
		 return swCobroComisiones;
	}

	@Override
	public SwCobroComisiones remove(Object key) {
		return (SwCobroComisiones) this.getCache().remove(key);
	}

	@Override
	public Collection<SwCobroComisiones> values() {
		return (Collection<SwCobroComisiones>) this.getCache().values();
	}


}