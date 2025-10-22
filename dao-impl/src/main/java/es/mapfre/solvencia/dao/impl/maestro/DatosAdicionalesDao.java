package es.mapfre.solvencia.dao.impl.maestro; 

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.dao.DaoBase;

public class DatosAdicionalesDao<UmicKey, DatosAdicionales> extends DaoBase implements Map<UmicKey, DatosAdicionales> {
	
	private static final String CACHE_NAME = "datosAdicionales";
	
	public DatosAdicionalesDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}
	
	@Override
	public Set<Entry<UmicKey, DatosAdicionales>> entrySet() {
		return (Set<Entry<UmicKey, DatosAdicionales>>) this.getCache().entrySet();
	}

	@Override
	public DatosAdicionales get(Object key) {
		return (DatosAdicionales) this.getCache().get(key);
	}

	@Override
	public Set<UmicKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public DatosAdicionales put(UmicKey key, DatosAdicionales datosAdicionales) {
		 this.getCache().put(key, datosAdicionales);
		 return datosAdicionales;
	}

	@Override
	public DatosAdicionales remove(Object key) {
		return (DatosAdicionales) this.getCache().remove(key);
	}

	@Override
	public Collection<DatosAdicionales> values() {
		return (Collection<DatosAdicionales>) this.getCache().values();
	}
}