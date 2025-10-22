package es.mapfre.solvencia.filtros.impl;

import java.util.ArrayList;
import java.util.List;

import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.NotEqualsFilter;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
// JBMARTA - PYAM0025 - INI
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FiltroFichaProcesoAdicional;
// JBMARTA - PYAM0025 - FIN
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;

public abstract class FiltroString extends FiltroAbstract {

	public Filter crearFiltroString(FichaProceso fichaProceso, ValueExtractor stringExtractor)
			throws Solvencia2Excepcion {

		// El valor del campo incluyente/excluyente ha de ser el mismo para
		// todos los filtros de un mismo tipo (tipo2(ámbito) o
		// tipo3(adicionales))

		// En este ámbito, el tipo de dato sobre el que se realiza el filtro es
		// String

		String inclusivo = null;

		Boolean incluyente = false;

		List<Filter> filtros = new ArrayList<Filter>();

		// JBMARTA - PYAM0025 - INI
		for (FiltroFichaProcesoAdicional filtro : fichaProceso.getFiltrosAdicionales()) {

			if (inclusivo == null) {
				inclusivo = filtro.getGclasefil();
				incluyente = inclusivo.equalsIgnoreCase(ConstantesSolvencia.INCLUSIVO_ADICIONAL);
			} else {
				if (!inclusivo.equalsIgnoreCase(filtro.getGclasefil())) {
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_VALOR_INC_EXC_NO_HOMOGENEO, new Object[]{inclusivo, filtro.getGclasefil()});
				}
			}
			filtros.add(getFiltroString(filtro, stringExtractor, incluyente));
			// JBMARTA - PYAM0025 - FIN
		}

		Filter allFilter = null;

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

	// JBMARTA - PYAM0025 - INI
	public Filter getFiltroString(FiltroFichaProcesoAdicional filtro, ValueExtractor stringExtractor, Boolean inclusivo)
			throws Solvencia2Excepcion {

		String valorDesde = "";

		if (Operadores.validarOperadoresString(filtro)) {
			if (filtro.getValordesde() == null) {
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_ERROR_VALOR_OPERADORES, new Object[]{null});
			} else {
				valorDesde = filtro.getValordesde().trim();
			}
		}

		// Obviamos la comprobación del valorHasta, ya que no hacemos uso del
		// valor
		Filter filter = null;

		if (inclusivo) {
			filter = new EqualsFilter(stringExtractor, valorDesde);
		} else {
			filter = new NotEqualsFilter(stringExtractor, valorDesde);
		}
		return filter;
	}
	// JBMARTA - PYAM0025 - FIN
}