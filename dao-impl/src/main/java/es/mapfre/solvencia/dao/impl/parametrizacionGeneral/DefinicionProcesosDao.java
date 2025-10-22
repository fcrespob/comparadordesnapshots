package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionProcesos;

public class DefinicionProcesosDao extends DaoBase implements Map<String, DefinicionProcesos>{

private static final String CACHE_NAME = "DPR0"; 
	
	public DefinicionProcesosDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<String, DefinicionProcesos>> entrySet() {
		return (Set<Entry<String, DefinicionProcesos>>) this.getCache().entrySet();
	}

	@Override
	public DefinicionProcesos get(Object key) {
		if (!(key instanceof String)) {
			return null;
		} else {
			return (DefinicionProcesos) this.getCache().get(key);
		}
	}

	@Override
	public Set<String> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public DefinicionProcesos put(String key, DefinicionProcesos definicionProcesos) {
		 this.getCache().put(key, definicionProcesos);
		 return definicionProcesos;
	}

	@Override
	public DefinicionProcesos remove(Object key) {
		return (DefinicionProcesos) this.getCache().remove(key);
	}

	@Override
	public Collection<DefinicionProcesos> values() {
		return (Collection<DefinicionProcesos>) this.getCache().values();
	}
}