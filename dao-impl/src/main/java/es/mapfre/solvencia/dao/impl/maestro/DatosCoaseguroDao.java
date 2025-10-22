package es.mapfre.solvencia.dao.impl.maestro;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.dao.DaoBase;

public class DatosCoaseguroDao<UmicKey, DatosCoaseguro> extends DaoBase implements Map<UmicKey, DatosCoaseguro> {
	
	private static final String CACHE_NAME = "datos-coaseguro"; 
	
	public DatosCoaseguroDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}
	
	@Override
	public Set<Entry<UmicKey, DatosCoaseguro>> entrySet() {
		return (Set<Entry<UmicKey, DatosCoaseguro>>) this.getCache().entrySet();
	}

	@Override
	public DatosCoaseguro get(Object key) {
		return (DatosCoaseguro) this.getCache().get(key);
	}

	@Override
	public Set<UmicKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public DatosCoaseguro put(UmicKey key, DatosCoaseguro datosCoaseguro) {
		 this.getCache().put(key, datosCoaseguro);
		 return datosCoaseguro;
	}

	@Override
	public DatosCoaseguro remove(Object key) {
		return (DatosCoaseguro) this.getCache().remove(key);
	}

	@Override
	public Collection<DatosCoaseguro> values() {
		return (Collection<DatosCoaseguro>) this.getCache().values();
	}
}