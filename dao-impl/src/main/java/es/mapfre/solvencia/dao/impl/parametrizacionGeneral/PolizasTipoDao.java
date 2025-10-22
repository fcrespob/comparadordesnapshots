package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;

import es.mapfre.solvencia.coherence.keys.entregables.BasetecKey;
import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.PolizasTipoKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.PolizasTipo;

public class PolizasTipoDao extends DaoBaseSalidaCalculo implements Map<PolizasTipoKey, PolizasTipo> {

	private static final String CACHE_NAME = "PTIPO";

	private final ValueExtractor origenExtractor = new PofExtractor(String.class, PolizasTipo.IND_ORIGEN);
	private final ValueExtractor negocioExtractor = new PofExtractor(String.class, PolizasTipo.IND_CNEGOCIO);
	private final ValueExtractor canalExtractor = new PofExtractor(Integer.class, PolizasTipo.IND_CCANAL);

	public PolizasTipoDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<PolizasTipoKey, PolizasTipo>> entrySet() {
		return (Set<Entry<PolizasTipoKey, PolizasTipo>>) this.getCache().entrySet();
	}

	@Override
	public PolizasTipo get(Object key) {
		if (!(key instanceof PolizasTipoKey)) {
			return null;
		} else {
			return (PolizasTipo) this.getCache().get(key);
		}
	}

	@Override
	public Set<PolizasTipoKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public PolizasTipo put(PolizasTipoKey key, PolizasTipo polizasTipo) {
		this.getCache().put(key, polizasTipo);
		return polizasTipo;
	}

	@Override
	public PolizasTipo remove(Object key) {
		return (PolizasTipo) this.getCache().remove(key);
	}

	@Override
	public Collection<PolizasTipo> values() {
		return (Collection<PolizasTipo>) this.getCache().values();
	}

	public boolean containsUmic(UmicKey umicKey) {
		return this.containsKey(new PolizasTipoKey(umicKey));
	}

	protected Filter exportFiltered(FichaProceso ficha) {

		Filter[] arrayFilter = { new EqualsFilter(origenExtractor, ConstantesSolvencia.ORIGEN_AUTOMATICO),
				new EqualsFilter(negocioExtractor, ficha.getCnegocio()),
				new EqualsFilter(canalExtractor, ficha.getCcanal()) };

		Filter filter = new AllFilter(arrayFilter);
		
//		Filter filter = new EqualsFilter(origenExtractor, ConstantesSolvencia.ORIGEN_AUTOMATICO);
		// Introducir filtros de canal y negocio
		return filter;
	}

	@Override
	public boolean clearAfterExport() {
		return false;
	}

	@Override
	protected Comparator exportOrdered() {

		return new Comparator<PolizasTipoKey>() {

			@Override
			public int compare(PolizasTipoKey dc1, PolizasTipoKey dc2) {
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