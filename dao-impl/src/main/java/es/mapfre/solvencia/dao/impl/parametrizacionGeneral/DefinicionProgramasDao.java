package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.DefinicionProgramasKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionProgramas;

public class DefinicionProgramasDao extends DaoBase implements Map<DefinicionProgramasKey, DefinicionProgramas>{

private static final String CACHE_NAME = "DPG"; 
	
	public DefinicionProgramasDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<DefinicionProgramasKey, DefinicionProgramas>> entrySet() {
		return (Set<Entry<DefinicionProgramasKey, DefinicionProgramas>>) this.getCache().entrySet();
	}

	@Override
	public DefinicionProgramas get(Object key) {
		if (!(key instanceof DefinicionProgramasKey)) {
			return null;
		} else {
			return (DefinicionProgramas) this.getCache().get(key);
		}
	}

	@Override
	public Set<DefinicionProgramasKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public DefinicionProgramas put(DefinicionProgramasKey key, DefinicionProgramas definicionProgramas) {
		 this.getCache().put(key, definicionProgramas);
		 return definicionProgramas;
	}

	@Override
	public DefinicionProgramas remove(Object key) {
		return (DefinicionProgramas) this.getCache().remove(key);
	}

	@Override
	public Collection<DefinicionProgramas> values() {
		return (Collection<DefinicionProgramas>) this.getCache().values();
	}


}