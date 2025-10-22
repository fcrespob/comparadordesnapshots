package es.mapfre.solvencia.dao.impl.maestro;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.maestro.OtrosDatos;

public class OtrosDatosDao<UmicKey, Rescates> extends DaoBase implements Map<UmicKey, OtrosDatos>{

	private static final String CACHE_NAME = "otrosDatos";
	
	public OtrosDatosDao(){
		super();
		super.setCacheName(CACHE_NAME);
	}
	
	@Override
	public Set<java.util.Map.Entry<UmicKey, OtrosDatos>> entrySet() {
		return (Set<Entry<UmicKey, OtrosDatos>>) this.getCache().entrySet();
	}

	@Override
	public OtrosDatos get(Object key) {
		return (OtrosDatos) this.getCache().get(key);
	}

	@Override
	public Set<UmicKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public OtrosDatos put(UmicKey key, OtrosDatos otrosDatos) {
		this.getCache().put(key, otrosDatos);
		return otrosDatos;
	}

	@Override
	public OtrosDatos remove(Object key) {
		return (OtrosDatos) this.getCache().remove(key);
	}

	@Override
	public Collection<OtrosDatos> values() {
		return (Collection<OtrosDatos>) this.getCache().values();
	}

}
