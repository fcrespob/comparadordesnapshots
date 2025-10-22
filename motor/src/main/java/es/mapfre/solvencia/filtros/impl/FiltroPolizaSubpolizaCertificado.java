package es.mapfre.solvencia.filtros.impl;

import java.util.Set;

import com.tangosol.net.partition.PartitionSet;
import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.filter.InKeySetFilter;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.impl.maestro.DatosGeneralesDao;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;

/**
 * 
 * @author indra
 * 
 *         Todos los campos son de obligado cumplimiento
 *
 */
 // JBMARTA - PYAM0025 - INI
public class FiltroPolizaSubpolizaCertificado extends FiltroPolizaSubpoliza {

	public static final String ID_FILTRO = "04";
	private DatosGeneralesDao datosGeneralesDao = new DatosGeneralesDao();

	@Override
	public String getNombreServicio() {
		return FiltroPolizaSubpolizaCertificado.ID_FILTRO;
	}

	@Override
	public Set<UmicKey> filtrar(Set<UmicKey> umicSet, PartitionSet partitionSet, FichaProceso fichaProceso) {

		validarParametros(fichaProceso);

		/**
		 * Extractores utilizados para construir los filtros de coherence para
		 * este tipo de filtro.
		 */
		final ValueExtractor polizaExtractor = new PofExtractor(Long.class, DatosGenerales.IND_KPOLIZA);
		final ValueExtractor subPolizaExtractor = new PofExtractor(Integer.class, DatosGenerales.IND_EKSUBPOLIZA);
		final ValueExtractor certificadoExtractor = new PofExtractor(Integer.class, DatosGenerales.IND_KCERTIFICADO);
		final ValueExtractor casadosExtractor = new PofExtractor(String.class, DatosGenerales.IND_SWCASADO);

		final Filter filtroPolizaSubPolizaCertificado = crearFiltro(fichaProceso, polizaExtractor, subPolizaExtractor,
				certificadoExtractor, casadosExtractor);

		Filter filtroAcumulado = null;
		if (umicSet == null) {
			filtroAcumulado = filtroPolizaSubPolizaCertificado;
		} else {
			filtroAcumulado = new InKeySetFilter(filtroPolizaSubPolizaCertificado, umicSet);
		}

		return this.datosGeneralesDao.keySet(filtroAcumulado);
	}
}
// JBMARTA - PYAM0025 - FIN