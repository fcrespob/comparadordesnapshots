package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.DefinicionesConstantesRescatesKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesConstantesRescates;

public class DefinicionesConstantesRescatesDao extends DaoBase implements Map<DefinicionesConstantesRescatesKey, DefinicionesConstantesRescates> {

private static final String CACHE_NAME = "CPR0"; 
	
	public DefinicionesConstantesRescatesDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<DefinicionesConstantesRescatesKey, DefinicionesConstantesRescates>> entrySet() {
		return (Set<Entry<DefinicionesConstantesRescatesKey, DefinicionesConstantesRescates>>) this.getCache().entrySet();
	}

	@Override
	public DefinicionesConstantesRescates get(Object key) {
		if (!(key instanceof DefinicionesConstantesRescatesKey)) {
			return null;
		} else {
			return (DefinicionesConstantesRescates) this.getCache().get(key);
		}
	}

	@Override
	public Set<DefinicionesConstantesRescatesKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public DefinicionesConstantesRescates put(DefinicionesConstantesRescatesKey key, DefinicionesConstantesRescates definicionesConstantesRescates) {
		 this.getCache().put(key, definicionesConstantesRescates);
		 return definicionesConstantesRescates;
	}

	@Override
	public DefinicionesConstantesRescates remove(Object key) {
		return (DefinicionesConstantesRescates) this.getCache().remove(key);
	}

	@Override
	public Collection<DefinicionesConstantesRescates> values() {
		return (Collection<DefinicionesConstantesRescates>) this.getCache().values();
	}

}