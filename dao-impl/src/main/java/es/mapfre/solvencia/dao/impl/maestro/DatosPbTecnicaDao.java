package es.mapfre.solvencia.dao.impl.maestro;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.maestro.DatosPbTecnicaKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.maestro.DatosPbTecnica;

public class DatosPbTecnicaDao extends DaoBase implements Map<DatosPbTecnicaKey, DatosPbTecnica> {
	
	private static final String CACHE_NAME = "X880JI04"; 
	
	public DatosPbTecnicaDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<DatosPbTecnicaKey, DatosPbTecnica>> entrySet() {
		return (Set<Entry<DatosPbTecnicaKey, DatosPbTecnica>>) this.getCache().entrySet();
	}

	@Override
	public DatosPbTecnica get(Object key) {
		if (!(key instanceof DatosPbTecnicaKey)) {
			return null;
		} else {
			return (DatosPbTecnica) this.getCache().get(key);
		}
	}

	@Override
	public Set<DatosPbTecnicaKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public DatosPbTecnica put(DatosPbTecnicaKey key, DatosPbTecnica datosPbTecnica) {
		 this.getCache().put(key, datosPbTecnica);
		 return datosPbTecnica;
	}

	@Override
	public DatosPbTecnica remove(Object key) {
		return (DatosPbTecnica) this.getCache().remove(key);
	}

	@Override
	public Collection<DatosPbTecnica> values() {
		return (Collection<DatosPbTecnica>) this.getCache().values();
	}

}
