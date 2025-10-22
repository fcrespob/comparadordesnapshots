package es.mapfre.solvencia.dao.impl.salidaCalculo;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import com.tangosol.util.Filter;
import com.tangosol.util.QueryHelper;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.aggregator.Count;
import com.tangosol.util.aggregator.DistinctValues;
import com.tangosol.util.aggregator.GroupAggregator;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.AlwaysFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.NotFilter;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.IncidenciasMaestroKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.salidaCalculo.IncidenciasMaestro;

public class IncidenciasMaestroDao extends DaoBaseSalidaCalculo implements Map<IncidenciasMaestroKey, IncidenciasMaestro> {

	private static final String CACHE_NAME = "incidenciasmaestro";

	public IncidenciasMaestroDao() {
		super();
		super.setCacheName(CACHE_NAME);

	}

	@Override
	public Set<Entry<IncidenciasMaestroKey, IncidenciasMaestro>> entrySet() {
		return (Set<Entry<IncidenciasMaestroKey, IncidenciasMaestro>>) this.getCache().entrySet();
	}

	@Override
	public IncidenciasMaestro get(Object key) {
		if (!(key instanceof IncidenciasMaestroKey)) {
			return null;
		} else {
			return (IncidenciasMaestro) this.getCache().get(key);
		}
	}

	@Override
	public Set<IncidenciasMaestroKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public IncidenciasMaestro put(IncidenciasMaestroKey key, IncidenciasMaestro value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public IncidenciasMaestro remove(Object key) {
		return (IncidenciasMaestro) this.getCache().remove(key);
	}

	@Override
	public Collection<IncidenciasMaestro> values() {
		return (Collection<IncidenciasMaestro>) this.getCache().values();
	}

	@Override
	protected Comparator exportOrdered() {
		return new Comparator<IncidenciasMaestroKey>() {
			@Override
			public int compare(IncidenciasMaestroKey dc1, IncidenciasMaestroKey dc2) {
				if (dc1 == null) {
					if (dc2 != null) {
						return 1;
					}
					return 0;
				}
				return dc1.compareTo(dc2);
			}
		};
	}
}