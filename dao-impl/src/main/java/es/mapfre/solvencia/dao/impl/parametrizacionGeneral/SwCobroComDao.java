package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.SwCobroComKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.SwCobroCom;

public class SwCobroComDao extends DaoBase implements Map<SwCobroComKey, SwCobroCom>{

private static final String CACHE_NAME = "SWCOBROCOM"; 
	
	public SwCobroComDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<SwCobroComKey, SwCobroCom>> entrySet() {
		return (Set<Entry<SwCobroComKey, SwCobroCom>>) this.getCache().entrySet();
	}

	@Override
	public SwCobroCom get(Object key) {
		if (!(key instanceof SwCobroComKey)) {
			return null;
		} else {
			return (SwCobroCom) this.getCache().get(key);
		}
	}

	@Override
	public Set<SwCobroComKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public SwCobroCom put(SwCobroComKey key, SwCobroCom swCobroCom) {
		 this.getCache().put(key, swCobroCom);
		 return swCobroCom;
	}

	@Override
	public SwCobroCom remove(Object key) {
		return (SwCobroCom) this.getCache().remove(key);
	}

	@Override
	public Collection<SwCobroCom> values() {
		return (Collection<SwCobroCom>) this.getCache().values();
	}


}