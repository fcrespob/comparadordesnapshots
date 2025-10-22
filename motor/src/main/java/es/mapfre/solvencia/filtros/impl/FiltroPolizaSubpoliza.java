package es.mapfre.solvencia.filtros.impl;

import java.util.ArrayList;
import java.util.List;

import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.NotFilter;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FiltroFichaProcesoAdicional;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;

/**
 * 
 * @author indra
 * 
 *         Todos los campos son de obligado cumplimiento
 *
 */
// JBMARTA - PYAM0025 - INI
public abstract class FiltroPolizaSubpoliza extends FiltroInteger {

	protected Filter crearFiltro(final FichaProceso fichaProceso, final ValueExtractor polizaExtractor,
			final ValueExtractor subPolizaExtractor, final ValueExtractor campo, final ValueExtractor casadosExtractor)
			throws Solvencia2Excepcion {

		String inclusivo = null;

		final List<Filter> filtros = new ArrayList<Filter>();

		Boolean incluyente = false;

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
			// para cada extractor se crea un nuevo filtro. Todos los campos son
			// de obligado cumplimiento

			// en caso de que casados venga a TODOS, no aplico el filtro por
			// casados/no casados
			if (filtro.getScasados() != null && !filtro.getScasados().equals("T")) {
				allFilterFicha = createAndFilter(
						createAndFilter(getFiltroEqualsLong(filtro.getCpoliza(), polizaExtractor, true),
								getFiltroEqualsInteger(Integer.parseInt(filtro.getNsubpoliza()), subPolizaExtractor,
										true)),
						createAndFilter(getFiltroInteger(filtro, campo, true),
								getFiltroEqualsString(filtro.getScasados().equals("C")?"S":filtro.getScasados(), casadosExtractor, true)));
			} else {
				allFilterFicha = createAndFilter(createAndFilter(getFiltroEqualsLong(filtro.getCpoliza(), polizaExtractor, true),
						getFiltroEqualsInteger(Integer.parseInt(filtro.getNsubpoliza()), subPolizaExtractor, true)), 
							getFiltroInteger(filtro, campo, true));
			}
			if (incluyente) {
				filtros.add(allFilterFicha);
			} else {
				filtros.add(new NotFilter(allFilterFicha));
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

	/*
	 * Chequeamos que los cuatro valores por los que se filtra vengan rellenos
	 */
	protected void validarParametros(final FichaProceso fichaProceso) {

		for (FiltroFichaProcesoAdicional filtroAdicional : fichaProceso.getFiltrosAdicionales()) {
			if (FiltroPolizaSubpolizaCertificado.ID_FILTRO
					.equals(fichaProceso.getFiltrosAdicionales().get(0).getCtipofiltro())) {
				if (filtroAdicional.getGoperdesde() == null || filtroAdicional.getCpoliza() == null
						|| filtroAdicional.getNsubpoliza() == null || filtroAdicional.getScasados() == null) {
					throw Solvencia2ExcepcionHelper.crearExcepcion(
							ConstantesSolvencia.CTE_FILTRO_POL_SUBPOL_CERT_ALGUN_PARAMETRO_NO_INFORMADO);
				}
			} else {
				if (filtroAdicional.getGoperdesde() == null || filtroAdicional.getCpoliza() == null
						|| filtroAdicional.getNsubpoliza() == null) {
					throw Solvencia2ExcepcionHelper.crearExcepcion(
							ConstantesSolvencia.CTE_FILTRO_POL_SUBPOL_SUSC_ALGUN_PARAMETRO_NO_INFORMADO);
				}
			}
		}
	}
}
// JBMARTA - PYAM0025 - FIN