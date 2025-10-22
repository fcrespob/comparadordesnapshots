package es.mapfre.solvencia.filtros.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.tangosol.net.partition.PartitionSet;
import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.AndFilter;
import com.tangosol.util.filter.InKeySetFilter;
import com.tangosol.util.filter.NotFilter;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.impl.maestro.DatosGeneralesDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FiltroFichaProcesoAdicional;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;

/**
 * 
 * @author indra Será obligatorio rellenar al menos uno de los dos datos
 *
 */
// JBMARTA - PYAM0025 - INI
public class FiltroSegmentoRiesgoActuarial extends FiltroAbstract {

	public static final String ID_FILTRO = "06";
	private DatosGeneralesDao datosGeneralesDao = new DatosGeneralesDao();

	@Override
	public String getNombreServicio() {
		return FiltroSegmentoRiesgoActuarial.ID_FILTRO;
	}

	private Filter crearFiltro(final FichaProceso fichaProceso, final ValueExtractor segmento1Extractor,
			final ValueExtractor riesgoActuarialExtractor) throws Solvencia2Excepcion {

		String inclusivo = null;

		List<Filter> filtros = new ArrayList<Filter>();

		Boolean incluyente = false;

		for (FiltroFichaProcesoAdicional filtro : fichaProceso.getFiltrosAdicionales()) {

			// El valor del campo incluyente/excluyente ha de ser el mismo para
			// todos los filtros de un mismo tipo (ámbito/tipo2 o
			// adicionales/tipo3)
			if (inclusivo == null) {
				inclusivo = filtro.getGclasefil();
				incluyente = inclusivo.equalsIgnoreCase(ConstantesSolvencia.INCLUSIVO_ADICIONAL);
			} else {
				if (!inclusivo.equalsIgnoreCase(filtro.getGclasefil())) {
					throw Solvencia2ExcepcionHelper.crearExcepcion(
							ConstantesSolvencia.CTE_FILTRO_VALOR_INC_EXC_NO_HOMOGENEO,
							new Object[] { inclusivo, filtro.getGclasefil() });
				}
			}
			// para cada extractor se crea un nuevo filtro
			if (filtro.getCsegmento() != null && filtro.getCriesgoact() != null) {
				// están los dos, hay que crear el filtro con ambos
				final Filter filtroSegmentoRiesgo = new AndFilter(
						getFiltroEqualsString(filtro.getCsegmento(), segmento1Extractor, true),
						getFiltroEqualsString(filtro.getCriesgoact(), riesgoActuarialExtractor, true));
				if (incluyente) {
					filtros.add(filtroSegmentoRiesgo);
				} else {
					filtros.add(new NotFilter(filtroSegmentoRiesgo));
				}
			} else if (filtro.getCsegmento() != null) {
				filtros.add(getFiltroEqualsString(filtro.getCsegmento(), segmento1Extractor, incluyente));
			} else if (filtro.getCriesgoact() != null) {
				filtros.add(getFiltroEqualsString(filtro.getCriesgoact(), riesgoActuarialExtractor, incluyente));
			} else {
				// Error, al menos uno de los dos campos ha de ir relleno
				throw Solvencia2ExcepcionHelper
						.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_SEGM_RIESGO_NINGUN_PARAMETRO_INFORMADO);
			}
		}

		Filter allFilter = null;
		// Se tiene en cuenta si los valores a filtrar son excluyentes o
		// incluyentes (un sólo tipo) a la hora de crear el filtro conjunto con
		// los filtros de cada registro.
		if (incluyente) {
			for (Filter filtro : filtros) {
				allFilter = createOrFilter(allFilter, filtro);
			}
		} else {
			Filter[] arrayFiltros = (Filter[]) filtros.toArray(new Filter[0]);
			allFilter = new AllFilter(arrayFiltros);
		}
		return allFilter;
	}

	@Override
	public Set<UmicKey> filtrar(Set<UmicKey> umicSet, PartitionSet partitionSet, FichaProceso fichaProceso) {

		/**
		 * Extractores utilizados para construir los filtros de coherence para
		 * este tipo de filtro.
		 */
		final ValueExtractor segmento1Extractor = new PofExtractor(String.class, DatosGenerales.IND_SEGMENTO1);
		final ValueExtractor riesgoActuarialExtractor = new PofExtractor(String.class,
				DatosGenerales.IND_TIPOSUBRIESGO);

		final Filter filtroSegmentoRiesgoActuarial = this.crearFiltro(fichaProceso, segmento1Extractor,
				riesgoActuarialExtractor);

		Filter filtroAcumulado = null;
		if (umicSet == null) {
			filtroAcumulado = filtroSegmentoRiesgoActuarial;
		} else {
			filtroAcumulado = new InKeySetFilter(filtroSegmentoRiesgoActuarial, umicSet);
		}

		return this.datosGeneralesDao.keySet(filtroAcumulado);
	}
}
// JBMARTA - PYAM0025 - FIN