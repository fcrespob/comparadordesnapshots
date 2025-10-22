package es.mapfre.solvencia.dao.impl.maestro;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.maestro.DatosAdicionalesCoaseguroKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.maestro.DatosAdicionalesCoaseguro;


public class DatosAdicionalesCoaseguroDao extends DaoBase implements Map<DatosAdicionalesCoaseguroKey, DatosAdicionalesCoaseguro> {
	
	private static final String CACHE_NAME = "X880JI03"; 
	
	public DatosAdicionalesCoaseguroDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}


	@Override
	public Set<Entry<DatosAdicionalesCoaseguroKey, DatosAdicionalesCoaseguro>> entrySet() {
		return (Set<Entry<DatosAdicionalesCoaseguroKey, DatosAdicionalesCoaseguro>>) this.getCache().entrySet();
	}

	@Override
	public DatosAdicionalesCoaseguro get(Object key) {
		if (!(key instanceof DatosAdicionalesCoaseguroKey)) {
			return null;
		} else {
			return (DatosAdicionalesCoaseguro) this.getCache().get(key);
		}
	}

	@Override
	public Set<DatosAdicionalesCoaseguroKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public DatosAdicionalesCoaseguro put(DatosAdicionalesCoaseguroKey key, DatosAdicionalesCoaseguro datosAdicionalesCoaseguro) {
		 this.getCache().put(key, datosAdicionalesCoaseguro);
		 return datosAdicionalesCoaseguro;
	}

	@Override
	public DatosAdicionalesCoaseguro remove(Object key) {
		return (DatosAdicionalesCoaseguro) this.getCache().remove(key);
	}

	@Override
	public Collection<DatosAdicionalesCoaseguro> values() {
		return (Collection<DatosAdicionalesCoaseguro>) this.getCache().values();
	}
}