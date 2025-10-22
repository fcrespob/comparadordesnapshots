package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.DefinicionesSolvencia2Key;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesSolvencia2;

public class DefinicionesSolvencia2Dao extends DaoBase implements Map<DefinicionesSolvencia2Key, DefinicionesSolvencia2>{

private static final String CACHE_NAME = "SOL0"; 
	
	public DefinicionesSolvencia2Dao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<DefinicionesSolvencia2Key, DefinicionesSolvencia2>> entrySet() {
		return (Set<Entry<DefinicionesSolvencia2Key, DefinicionesSolvencia2>>) this.getCache().entrySet();
	}

	@Override
	public DefinicionesSolvencia2 get(Object key) {
		if (!(key instanceof DefinicionesSolvencia2Key)) {
			return null;
		} else {
			return (DefinicionesSolvencia2) this.getCache().get(key);
		}
	}

	@Override
	public Set<DefinicionesSolvencia2Key> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public DefinicionesSolvencia2 put(DefinicionesSolvencia2Key key, DefinicionesSolvencia2 definicionesSolvencia2) {
		 this.getCache().put(key, definicionesSolvencia2);
		 return definicionesSolvencia2;
	}

	@Override
	public DefinicionesSolvencia2 remove(Object key) {
		return (DefinicionesSolvencia2) this.getCache().remove(key);
	}

	@Override
	public Collection<DefinicionesSolvencia2> values() {
		return (Collection<DefinicionesSolvencia2>) this.getCache().values();
	}
}