package es.mapfre.solvencia.dao.impl.maestro;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.maestro.DatosDescuentos;

public class DatosDescuentosDao extends DaoBase implements Map<UmicKey, DatosDescuentos> {
	
	private static final String CACHE_NAME = "datosDescuentos"; 
	
	public DatosDescuentosDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<UmicKey, DatosDescuentos>> entrySet() {
		return (Set<Entry<UmicKey, DatosDescuentos>>) this.getCache().entrySet();
	}

	@Override
	public DatosDescuentos get(Object key) {
		if (!(key instanceof UmicKey)) {
			return null;
		} else {
			return (DatosDescuentos) this.getCache().get(key);
		}
	}

	@Override
	public Set<UmicKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public DatosDescuentos put(UmicKey key, DatosDescuentos descuentos) {
		 this.getCache().put(key, descuentos);
		 return descuentos;
	}

	@Override
	public DatosDescuentos remove(Object key) {
		return (DatosDescuentos) this.getCache().remove(key);
	}

	@Override
	public Collection<DatosDescuentos> values() {
		return (Collection<DatosDescuentos>) this.getCache().values();
	}

}