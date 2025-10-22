package es.mapfre.solvencia.dao.impl.entregables;


import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.ContabilidadCertificadoKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.ContabilidadCertificado;

public class ContabilidadCertificadoDao extends DaoBaseSalidaCalculo implements Map<ContabilidadCertificadoKey, ContabilidadCertificado> {
	
	private static final String CACHE_NAME = "contabc";

	public ContabilidadCertificadoDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public ContabilidadCertificado get(Object key) {
		if (key instanceof ContabilidadCertificadoKey) {
			return (ContabilidadCertificado) getCache().get(key);
		}
		return null;
	}

	@Override
	public ContabilidadCertificado put(ContabilidadCertificadoKey key, ContabilidadCertificado value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public ContabilidadCertificado remove(Object key) {
		return (ContabilidadCertificado) this.getCache().remove(key);
	}

	@Override
	public Collection<ContabilidadCertificado> values() {
		return (Collection<ContabilidadCertificado>) this.getCache().values();
	}

	@Override
	public Set<ContabilidadCertificadoKey> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<ContabilidadCertificadoKey, ContabilidadCertificado>> entrySet() {
		return (Set<Entry<ContabilidadCertificadoKey, ContabilidadCertificado>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<ContabilidadCertificadoKey>() {

			@Override
			public int compare(ContabilidadCertificadoKey dc1, ContabilidadCertificadoKey dc2) {
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

}