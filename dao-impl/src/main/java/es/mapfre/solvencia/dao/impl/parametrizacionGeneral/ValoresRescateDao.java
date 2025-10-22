package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.ValoresRescateKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ValoresRescate;

public class ValoresRescateDao extends DaoBase implements Map<ValoresRescateKey, ValoresRescate>{

private static final String CACHE_NAME = "INV0"; 
	
	public ValoresRescateDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<ValoresRescateKey, ValoresRescate>> entrySet() {
		return (Set<Entry<ValoresRescateKey, ValoresRescate>>) this.getCache().entrySet();
	}

	@Override
	public ValoresRescate get(Object key) {
		if (!(key instanceof ValoresRescateKey)) {
			return null;
		} else {
			return (ValoresRescate) this.getCache().get(key);
		}
	}

	@Override
	public Set<ValoresRescateKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public ValoresRescate put(ValoresRescateKey key, ValoresRescate valoresRescate) {
		 this.getCache().put(key, valoresRescate);
		 return valoresRescate;
	}

	@Override
	public ValoresRescate remove(Object key) {
		return (ValoresRescate) this.getCache().remove(key);
	}

	@Override
	public Collection<ValoresRescate> values() {
		return (Collection<ValoresRescate>) this.getCache().values();
	}


}