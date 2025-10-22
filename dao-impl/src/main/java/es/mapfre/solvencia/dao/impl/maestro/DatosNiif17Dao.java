package es.mapfre.solvencia.dao.impl.maestro;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.maestro.DatosNiif17;

public class DatosNiif17Dao extends DaoBase implements Map<UmicKey, DatosNiif17> {
	private static final String CACHE_NAME = "datosNiif17";

	public DatosNiif17Dao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<UmicKey, DatosNiif17>> entrySet() {
		return (Set<Entry<UmicKey, DatosNiif17>>) this.getCache().entrySet();
	}

	@Override
	public DatosNiif17 get(Object key) {
		if (!(key instanceof UmicKey)) {
			return null;
		} else {
			return (DatosNiif17) this.getCache().get(key);
		}
	}

	@Override
	public Set<UmicKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public DatosNiif17 put(UmicKey key, DatosNiif17 datosNiif17) {
		this.getCache().put(key, datosNiif17);
		return datosNiif17;
	}

	public DatosNiif17 remove(Object key) {
		return (DatosNiif17) this.getCache().remove(key);
	}

	@Override
	public Collection<DatosNiif17> values() {
		return (Collection<DatosNiif17>) this.getCache().values();
	}
}
