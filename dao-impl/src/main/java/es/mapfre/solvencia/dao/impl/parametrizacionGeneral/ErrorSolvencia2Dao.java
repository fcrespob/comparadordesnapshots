package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.maestro.ErrorSolvencia2Key;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.maestro.ErrorSolvencia2;

public class ErrorSolvencia2Dao extends DaoBase implements Map<ErrorSolvencia2Key, ErrorSolvencia2> {
	
	private static final String CACHE_NAME = "ERR0"; 
	
	public ErrorSolvencia2Dao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<ErrorSolvencia2Key, ErrorSolvencia2>> entrySet() {
		return (Set<Entry<ErrorSolvencia2Key, ErrorSolvencia2>>) this.getCache().entrySet();
	}

	@Override
	public ErrorSolvencia2 get(Object key) {
		if (!(key instanceof ErrorSolvencia2Key)) {
			return null;
		} else {
			return (ErrorSolvencia2) this.getCache().get(key);
		}
	}

	@Override
	public Set<ErrorSolvencia2Key> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public ErrorSolvencia2 put(ErrorSolvencia2Key key, ErrorSolvencia2 errorSolvencia2) {
		 this.getCache().put(key, errorSolvencia2);
		 return errorSolvencia2;
	}

	@Override
	public ErrorSolvencia2 remove(Object key) {
		return (ErrorSolvencia2) this.getCache().remove(key);
	}

	@Override
	public Collection<ErrorSolvencia2> values() {
		return (Collection<ErrorSolvencia2>) this.getCache().values();
	}

}