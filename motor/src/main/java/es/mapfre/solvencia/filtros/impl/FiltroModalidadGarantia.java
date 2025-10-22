package es.mapfre.solvencia.filtros.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.tangosol.net.partition.PartitionSet;
import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.filter.AllFilter;
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
 * @author indra
 * 
 *         Se permite realizar filtros exclusivamente por uno de los dos campos.
 *         Cualquiera de ellos En caso de realizar filtro por ambos, será
 *         obligatorio que la modalidad sea la misma en todos los posibles
 *         filtros que se definan
 *
 */
// JBMARTA - PYAM0025 - INI
public class FiltroModalidadGarantia extends FiltroInteger {

	public static final String ID_FILTRO = "03";
	private DatosGeneralesDao datosGeneralesDao = new DatosGeneralesDao();

	@Override
	public String getNombreServicio() {
		return FiltroModalidadGarantia.ID_FILTRO;
	}

	private Filter crearFiltro(final FichaProceso fichaProceso, final ValueExtractor modalidadExtractor,
			final ValueExtractor garantiaExtractor) throws Solvencia2Excepcion {

		validarParametros(fichaProceso);

		String inclusivo = null;

		final List<Filter> filtros = new ArrayList<Filter>();

		Boolean incluyente = false;
		Integer modalidad = null;

		for (FiltroFichaProcesoAdicional filtro : fichaProceso.getFiltrosAdicionales()) {

			Filter allFilterFicha = null;

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
			if (filtro.getGoperdesde() != null && filtro.getGopdesdegar() != null) {
				// Filtro modalidad - garantía
				// será obligatorio que la modalidad sea la misma en todos los
				// posibles filtros que se definan
				if (modalidad == null) {
					modalidad = Integer.parseInt(filtro.getValordesde());
				} else {
					if (modalidad != Integer.parseInt(filtro.getValordesde())) {
						throw Solvencia2ExcepcionHelper.crearExcepcion(
								ConstantesSolvencia.CTE_FILTRO_MOD_GARANTIA_MODALIDAD_NO_HOMOGENEA,
								new Object[] { modalidad, filtro.getValordesde() });
					}
				}
				final Filter fitroModalidadGarantia = createAndFilter(
						getFiltroInteger(filtro, modalidadExtractor, true),
						getFiltroIntegerGar(filtro, garantiaExtractor, true));
				if (incluyente) {
					allFilterFicha = fitroModalidadGarantia;
				} else {
					allFilterFicha = new NotFilter(fitroModalidadGarantia);
				}
			} else if (filtro.getGoperdesde() != null) {
				// Filtro sólo modalidad
				allFilterFicha = getFiltroInteger(filtro, modalidadExtractor, incluyente);
			} else {
				// Filtro sólo garantía
				allFilterFicha = getFiltroIntegerGar(filtro, garantiaExtractor, incluyente);
			}

			filtros.add(allFilterFicha);
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

	/*
	 * Chequeamos que los dos valores por los que se filtra vengan rellenos
	 */
	private void validarParametros(final FichaProceso fichaProceso) {

		for (FiltroFichaProcesoAdicional filtroAdicional : fichaProceso.getFiltrosAdicionales()) {
			if (filtroAdicional.getGoperdesde() == null && filtroAdicional.getGopdesdegar() == null) {
				// TODO hay que dar de alta este error en bbdd
				throw Solvencia2ExcepcionHelper
						.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_MOD_GARANTIA_PARAM_NO_INFORMADO);
			}
		}
	}

	@Override
	public Set<UmicKey> filtrar(final Set<UmicKey> umicSet, final PartitionSet partitionSet,
			final FichaProceso fichaProceso) {

		final ValueExtractor modalidadExtractor = new PofExtractor(Integer.class, DatosGenerales.IND_EKMODALIDAD);
		final ValueExtractor garantiaExtractor = new PofExtractor(Integer.class, DatosGenerales.IND_EKGARANTIA);

		final Filter filtroModalidadGarantia = this.crearFiltro(fichaProceso, modalidadExtractor, garantiaExtractor);

		Filter filtroAcumulado = null;
		if (umicSet == null) {
			filtroAcumulado = filtroModalidadGarantia;
		} else {
			filtroAcumulado = new InKeySetFilter(filtroModalidadGarantia, umicSet);
		}

		return this.datosGeneralesDao.keySet(filtroAcumulado);
	}
}
// JBMARTA - PYAM0025 - FIN