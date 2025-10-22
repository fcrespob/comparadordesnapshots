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
import es.mapfre.solvencia.coherence.keys.salidaCalculo.IncidenciaKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;

public class IncidenciaDao extends DaoBaseSalidaCalculo implements Map<IncidenciaKey, Incidencia> {

	private static final String CACHE_NAME = "incidencias";

	private final ValueExtractor umicKeyExtractor;
	private final ValueExtractor feccierreExtractor;
	private final ValueExtractor btExtractor;
	private final ValueExtractor tipoErrorExtractor;
	private final ValueExtractor codigoRetornoExtractor;

	public IncidenciaDao() {
		super();
		super.setCacheName(CACHE_NAME);

		umicKeyExtractor = createExtractor("getClaveUmic", UmicKey.class, Incidencia.IND_CLAVEUMIC);
		feccierreExtractor = createExtractor("getFecCierre", Timestamp.class, Incidencia.IND_FECCIERRE);
		btExtractor = createExtractor("getBt", String.class, Incidencia.IND_BT);
		tipoErrorExtractor = createExtractor("getTipoError", String.class, Incidencia.IND_TIPOERROR);
		codigoRetornoExtractor = createExtractor("getCodigoRetorno", String.class, Incidencia.IND_CODIGORETORNO);

		getCache().addIndex(umicKeyExtractor, false, null);
		getCache().addIndex(btExtractor, false, null);
		getCache().addIndex(feccierreExtractor, false, null);
		getCache().addIndex(tipoErrorExtractor, false, null);
	}

	@Override
	public Set<Entry<IncidenciaKey, Incidencia>> entrySet() {
		return (Set<Entry<IncidenciaKey, Incidencia>>) this.getCache().entrySet();
	}

	@Override
	public Incidencia get(Object key) {
		if (!(key instanceof IncidenciaKey)) {
			return null;
		} else {
			return (Incidencia) this.getCache().get(key);
		}
	}

	@Override
	public Set<IncidenciaKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Incidencia put(IncidenciaKey key, Incidencia value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public Incidencia remove(Object key) {
		return (Incidencia) this.getCache().remove(key);
	}

	@Override
	public Collection<Incidencia> values() {
		return (Collection<Incidencia>) this.getCache().values();
	}

	/**
	 * Comprueba si una UMIC es errónea en una determinada fecha de cierre y
	 * para una base técnica.
	 * 
	 * @param claveUmic
	 * @param fecCierre
	 * @param baseTec
	 * @return true si la UMIC ha tenido algún error, false si no lo ha tenido
	 */
	public Boolean isUmicErronea(UmicKey claveUmic, Timestamp fecCierre, String baseTec) {

		Filter claveFilter = new EqualsFilter(umicKeyExtractor, claveUmic);
		Filter btFilter = new EqualsFilter(btExtractor, baseTec);
		Filter fecCierreFilter = new EqualsFilter(feccierreExtractor, fecCierre.getTime());
		Filter tipoErrorFilter = new EqualsFilter(tipoErrorExtractor, ConstantesSolvencia.CTE_ERROR);
		Filter allFilter = new AllFilter(new Filter[] { claveFilter, btFilter, fecCierreFilter, tipoErrorFilter });

		return ((Integer) getCache().aggregate(allFilter, new Count())) > 0;
	}

	/**
	 * Calcula el número de UMIC distintas que han generado error. Si una UMIC
	 * ha generado dos errores, sólo se cuenta como una.
	 * 
	 * @return el número de UMIC distintas que han generado error
	 */
	public int calcularUmicsError() {

		Filter tipoErrorFilter = new NotFilter(new EqualsFilter(tipoErrorExtractor, ConstantesSolvencia.CTE_AVISO));

		return ((Set) this.getCache().aggregate(tipoErrorFilter, new DistinctValues(umicKeyExtractor))).size();
	}

	/**
	 * Calcula el número de UMIC distintas que han generado aviso. Si una UMIC
	 * ha generado dos avisos, sólo se cuenta como una.
	 * 
	 * @return el número de UMIC distintas que han generado aviso
	 */
	public int calcularUmicsAviso() {

		Filter tipoAvisoFilter = new EqualsFilter(tipoErrorExtractor, ConstantesSolvencia.CTE_AVISO);

		return ((Set) this.getCache().aggregate(tipoAvisoFilter, new DistinctValues(umicKeyExtractor))).size();
	}

	/**
	 * Calcula las incidencias de cada tipo generadas por las distintas UMIC. Si
	 * una UMIC ha generado dos incidencias distintas, sumará uno por cada
	 * incidencia.
	 * 
	 * @return Un mapa en el que la clave es el tipo de incidencia y el valor,
	 *         el número de ocasiones en el que se ha producido
	 */
	public Map<String, Integer> obtenerSubErrores() {
		return (Map<String, Integer>) this.getCache().aggregate(AlwaysFilter.INSTANCE,
				GroupAggregator.createInstance(codigoRetornoExtractor, new Count()));
	}

	@Override
	protected Comparator exportOrdered() {
		return new Comparator<IncidenciaKey>() {
			@Override
			public int compare(IncidenciaKey dc1, IncidenciaKey dc2) {
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