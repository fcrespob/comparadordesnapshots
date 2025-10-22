package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.CurvasTipo656Key;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.CurvasTipo656;

public class CurvasTipo656Dao extends DaoBase implements Map<CurvasTipo656Key, CurvasTipo656>{

private static final String CACHE_NAME = "X880JI08"; 
	
	public CurvasTipo656Dao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<CurvasTipo656Key, CurvasTipo656>> entrySet() {
		return (Set<Entry<CurvasTipo656Key, CurvasTipo656>>) this.getCache().entrySet();
	}

	@Override
	public CurvasTipo656 get(Object key) {
		if (!(key instanceof CurvasTipo656Key)) {
			return null;
		} else {
			return (CurvasTipo656) this.getCache().get(key);
		}
	}

	@Override
	public Set<CurvasTipo656Key> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public CurvasTipo656 put(CurvasTipo656Key key, CurvasTipo656 curvasTipo656) {
		 this.getCache().put(key, curvasTipo656);
		 return curvasTipo656;
	}

	@Override
	public CurvasTipo656 remove(Object key) {
		return (CurvasTipo656) this.getCache().remove(key);
	}

	@Override
	public Collection<CurvasTipo656> values() {
		return (Collection<CurvasTipo656>) this.getCache().values();
	}
}