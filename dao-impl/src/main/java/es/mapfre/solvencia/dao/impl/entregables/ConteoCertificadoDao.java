package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import es.mapfre.solvencia.coherence.keys.entregables.ConteoCertificadoKey;
import es.mapfre.solvencia.dominio.entregables.ConteoCertificado;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.ProvCoaSeg;

public class ConteoCertificadoDao extends DaoBaseSalidaCalculo implements Map<ConteoCertificadoKey, ConteoCertificado> {
	
	private static final String CACHE_NAME = "contcerti";

	public ConteoCertificadoDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public ConteoCertificado get(Object key) {
		if (key instanceof ConteoCertificadoKey) {
			return (ConteoCertificado) getCache().get(key);
		}
		return null;
	}



	@Override
	public Collection<ConteoCertificado> values() {
		return (Collection<ConteoCertificado>) this.getCache().values();
	}

	@Override
	public Set<ConteoCertificadoKey> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<ConteoCertificadoKey, ConteoCertificado>> entrySet() {
		return (Set<Entry<ConteoCertificadoKey, ConteoCertificado>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<ConteoCertificadoKey>() {

			@Override
			public int compare(ConteoCertificadoKey dc1, ConteoCertificadoKey dc2) {
				if (dc1 == null && dc2 != null) {
					return 1;
				} else if (dc1 != null) {
					return dc1.compareTo(dc2);
				} else if (dc1 == null) {
					return -1;
				}
				return 0;
			}
			
		};
	}

	@Override
	public ConteoCertificado put(ConteoCertificadoKey key, ConteoCertificado value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
			return value;
		}

	@Override
	public ConteoCertificado remove(Object key) {
		return (ConteoCertificado) this.getCache().remove(key);
	}

}
