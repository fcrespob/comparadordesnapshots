package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.OpcionesGeneracionKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.OpcionesGeneracion;

public class OpcionesGeneracionDao extends DaoBase implements Map<OpcionesGeneracionKey, OpcionesGeneracion>{

	private static final String CACHE_NAME = "OPG0"; 

	public OpcionesGeneracionDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<OpcionesGeneracionKey, OpcionesGeneracion>> entrySet() {
		return (Set<Entry<OpcionesGeneracionKey, OpcionesGeneracion>>) this.getCache().entrySet();
	}

	@Override
	public OpcionesGeneracion get(Object key) {
		if (!(key instanceof OpcionesGeneracionKey)) {
			return null;
		} else {
			return (OpcionesGeneracion) this.getCache().get(key);
		}
	}

	@Override
	public Set<OpcionesGeneracionKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public OpcionesGeneracion put(OpcionesGeneracionKey key, OpcionesGeneracion opcionesGeneracion) {
		 this.getCache().put(key, opcionesGeneracion);
		 return opcionesGeneracion;
	}

	@Override
	public OpcionesGeneracion remove(Object key) {
		return (OpcionesGeneracion) this.getCache().remove(key);
	}

	@Override
	public Collection<OpcionesGeneracion> values() {
		return (Collection<OpcionesGeneracion>) this.getCache().values();
	}
}