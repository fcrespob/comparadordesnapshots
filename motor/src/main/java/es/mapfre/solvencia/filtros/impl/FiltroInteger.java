package es.mapfre.solvencia.filtros.impl;

import java.util.ArrayList;
import java.util.List;

import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.GreaterEqualsFilter;
import com.tangosol.util.filter.GreaterFilter;
import com.tangosol.util.filter.LessEqualsFilter;
import com.tangosol.util.filter.LessFilter;
import com.tangosol.util.filter.NotEqualsFilter;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FiltroFichaProceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FiltroFichaProcesoAdicional;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;

/**
 * 
 * Clase que recoge la implementación del filtro de ficha de proceso que actúa
 * sobre el campo kmodalidad de datos generales. Los registros de filtros de
 * tipo 2 de una ficha de proceso, de existir, siempre serán de este tipo.
 * 
 * @author indra
 *
 */
public abstract class FiltroInteger extends FiltroAbstract {

	public Filter crearFiltroInteger(FichaProceso fichaProceso, ValueExtractor integerExtractor)
			throws Solvencia2Excepcion {

		// En este ámbito, el tipo de dato sobre el que se realiza el filtro es:
		// Integer

		String inclusivo = null;

		List<Filter> filtros = new ArrayList<Filter>();

		Boolean incluyente = false;

		for (FiltroFichaProceso filtro : fichaProceso.getFiltrosAmbito()) {

			// El valor del campo incluyente/excluyente ha de ser el mismo para
			// todos los filtros de un mismo tipo (ámbito/tipo2 o
			// adicionales/tipo3)
			if (inclusivo == null) {
				inclusivo = filtro.getCclaseamb();
				incluyente = inclusivo.equalsIgnoreCase(ConstantesSolvencia.INCLUSIVO);
			} else {
				if (!inclusivo.equalsIgnoreCase(filtro.getCclaseamb())) {
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_VALOR_INC_EXC_NO_HOMOGENEO, new Object[]{inclusivo, filtro.getCclaseamb()});
				}
			}
			// Se añade un filtro para cada registros de tipo 2 existente
			filtros.add(getFiltroInteger(filtro, integerExtractor, incluyente));
		}

		Filter allFilter = null;
		// Se tiene en cuenta si los valores a filtrar son excluyentes o
		// incluyentes (un sólo tipo)
		// a la hora de crear el filtro conjunto con los filtros de cada
		// registro.
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

	// /**
	// * Método que crea un filtro conjunto de los filtros incluyentes recibidos
	// por parámetro,
	// * aplicando la OR lógica.
	// *
	// * @param filterAc
	// * @param filterAd
	// * @return Or de los filtros recibidos por parámetro
	// */
	// public Filter createOrFilter(Filter filterAc, Filter filterAd) {
	//
	// Filter returnedFilter = null;
	//
	// if (filterAc == null) {
	// returnedFilter = filterAd;
	// } else {
	// returnedFilter = new OrFilter(filterAc, filterAd);
	// }
	// return returnedFilter;
	// }

	/**
	 * Construye el filtro de ámbito kmodalidad, en función de la información
	 * pasada por parámetro.
	 * 
	 * @param filtro
	 *            Registro de tipo filtro de proceso
	 * @param extractor
	 *            Extractor preparado para el campo sobre el que se aplica el
	 *            filtro
	 * @param inclusivo
	 *            Si el filtro es inclusivo o exclusivo
	 * @return
	 * @throws Exception
	 */
	public Filter getFiltroInteger(FiltroFichaProceso filtro, ValueExtractor extractor, Boolean inclusivo)
			throws Solvencia2Excepcion {

		// El tipo de dato del campo al que hace referencia el ambito es Integer

		Integer valorDesde = 0;

		Integer valorHasta = 0;

		String operadorDesde = "";

		String operadorHasta = "";

		// Comprobamos que los operadores del registro de tipo filtro de proceso
		// son válidos

		if (Operadores.validarOperadoresInteger(filtro)) {

			operadorDesde = filtro.getCoperadord();

			if (filtro.getCoperadorh() != null) {
				operadorHasta = filtro.getCoperadorh();
			}

			if (filtro.getGambitod() == null) {
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_ERROR_VALOR_FILTRO_MODALIDAD, new Object[]{null});
			} else {
				try {
					valorDesde = Integer.parseInt(filtro.getGambitod());
					if (!operadorHasta.isEmpty()) {
						valorHasta = Integer.parseInt(filtro.getGambitoh());
					}
				} catch (Exception e) {
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_ERROR_VALOR_FILTRO_MODALIDAD, null, e.getCause());
				}
			}
		}

		List<Filter> listaFiltro = new ArrayList<Filter>();

		Filter filterDesde = construirFiltro(extractor, operadorDesde, valorDesde, inclusivo);

		listaFiltro.add(filterDesde);

		if (!operadorHasta.isEmpty()) {
			Filter filterHasta = construirFiltro(extractor, operadorHasta, valorHasta, inclusivo);
			listaFiltro.add(filterHasta);
		}

		Filter allFilter = null;
		// Se tiene en cuenta si los valores a filtrar son excluyentes o
		// incluyentes (un sólo tipo)
		// a la hora de crear el filtro (que se divide en dos subfiltros si
		// existieran operador y valor hasta).
		if (inclusivo) {
			Filter[] filterArray = (Filter[]) listaFiltro.toArray(new Filter[0]);
			allFilter = new AllFilter(filterArray);
		} else {
			for (Filter filter : listaFiltro) {
				allFilter = createOrFilter(allFilter, filter);
			}
		}
		return allFilter;
	}

	/**
	 * Construye el filtro de ámbito de modalidad.
	 * 
	 * @param filtro
	 *            Registro de tipo filtro de proceso
	 * @param extractor
	 *            Extractor preparado para el campo sobre el que se aplica el
	 *            filtro
	 * @param inclusivo
	 *            Si el filtro es inclusivo o exclusivo
	 * @return
	 * @throws Exception
	 */
	protected Filter getFiltroInteger(final FiltroFichaProcesoAdicional filtro, final ValueExtractor extractor,
			final Boolean inclusivo) throws Solvencia2Excepcion {

		Integer valorDesde = 0;

		Integer valorHasta = 0;

		String operadorDesde = "";

		String operadorHasta = "";

		// Comprobamos que los operadores del registro de tipo filtro de proceso
		// son válidos

		if (Operadores.validarOperadoresIntegerAdicional(filtro)) {

			operadorDesde = filtro.getGoperdesde();

			if (filtro.getGoperhasta() != null) {
				operadorHasta = filtro.getGoperhasta();
			}

			if (filtro.getValordesde() == null) {
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_ERROR_VALOR_OPERADORES, new Object[]{null});
			} else {
				try {
					valorDesde = Integer.parseInt(filtro.getValordesde());
					if (!operadorHasta.isEmpty()) {
						valorHasta = Integer.parseInt(filtro.getValhasta());
					}
				} catch (Exception e) {
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_ERROR_VALOR_OPERADORES, null, e.getCause());
				}
			}
		}

		List<Filter> listaFiltro = new ArrayList<Filter>();

		Filter filterDesde = construirFiltro(extractor, operadorDesde, valorDesde, inclusivo);

		listaFiltro.add(filterDesde);

		if (!operadorHasta.isEmpty()) {
			Filter filterHasta = construirFiltro(extractor, operadorHasta, valorHasta, inclusivo);
			listaFiltro.add(filterHasta);
		}

		Filter allFilter = null;
		// Se tiene en cuenta si los valores a filtrar son excluyentes o
		// incluyentes (un sólo tipo)
		// a la hora de crear el filtro (que se divide en dos subfiltros si
		// existieran operador y valor hasta).
		if (inclusivo) {
			Filter[] filterArray = (Filter[]) listaFiltro.toArray(new Filter[0]);
			allFilter = new AllFilter(filterArray);
		} else {
			for (Filter filter : listaFiltro) {
				allFilter = createOrFilter(allFilter, filter);
			}
		}
		return allFilter;
	}
	
	protected Filter getFiltroStringCI(final FiltroFichaProcesoAdicional filtro, final ValueExtractor extractor,
			final Boolean inclusivo) throws Solvencia2Excepcion {

		String valorDesde = null;

		String valorHasta = null;

		String operadorDesde = "";

		String operadorHasta = "";

		// Comprobamos que los operadores del registro de tipo filtro de proceso
		// son válidos

		if (Operadores.validarOperadoresString(filtro)) {

			operadorDesde = filtro.getGoperdesde();

			if (filtro.getGoperhasta() != null) {
				operadorHasta = filtro.getGoperhasta();
			}

			if (filtro.getValordesde() == null) {
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_ERROR_VALOR_OPERADORES, new Object[]{null});
			} else {
				try {
					valorDesde = filtro.getValordesde();
					if (!operadorHasta.isEmpty()) {
						valorHasta = filtro.getValhasta();
					}
				} catch (Exception e) {
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_ERROR_VALOR_OPERADORES, null, e.getCause());
				}
			}
		}

		List<Filter> listaFiltro = new ArrayList<Filter>();

		Filter filterDesde = construirFiltroString(extractor, operadorDesde, valorDesde, inclusivo);

		listaFiltro.add(filterDesde);

		if (!operadorHasta.isEmpty()) {
			Filter filterHasta = construirFiltroString(extractor, operadorHasta, valorHasta, inclusivo);
			listaFiltro.add(filterHasta);
		}

		Filter allFilter = null;
		// Se tiene en cuenta si los valores a filtrar son excluyentes o
		// incluyentes (un sólo tipo)
		// a la hora de crear el filtro (que se divide en dos subfiltros si
		// existieran operador y valor hasta).
		if (inclusivo) {
			Filter[] filterArray = (Filter[]) listaFiltro.toArray(new Filter[0]);
			allFilter = new AllFilter(filterArray);
		} else {
			for (Filter filter : listaFiltro) {
				allFilter = createOrFilter(allFilter, filter);
			}
		}
		return allFilter;
	}
	
	protected Filter getFiltroStringGAP(final FiltroFichaProcesoAdicional filtro, final ValueExtractor extractor,
			final Boolean inclusivo) throws Solvencia2Excepcion {

		String valorDesde = null;

		String valorHasta = null;

		String operadorDesde = "";

		String operadorHasta = "";

		// Comprobamos que los operadores del registro de tipo filtro de proceso
		// son válidos

		if (Operadores.validarOperadoresIntegerAdicionalGarantia(filtro)) {

			operadorDesde = filtro.getGopdesdegar();

			if (filtro.getGophastagar() != null) {
				operadorHasta = filtro.getGophastagar();
			}

			if (filtro.getValdesdegar() == null) {
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_ERROR_VALOR_OPERADORES, new Object[]{null});
			} else {
				try {
					valorDesde = filtro.getValdesdegar();
					if (!operadorHasta.isEmpty()) {
						valorHasta = filtro.getValhastagar();
					}
				} catch (Exception e) {
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_ERROR_VALOR_OPERADORES, null, e.getCause());
				}
			}
		}

		List<Filter> listaFiltro = new ArrayList<Filter>();

		Filter filterDesde = construirFiltroString(extractor, operadorDesde, valorDesde, inclusivo);

		listaFiltro.add(filterDesde);

		if (!operadorHasta.isEmpty()) {
			Filter filterHasta = construirFiltroString(extractor, operadorHasta, valorHasta, inclusivo);
			listaFiltro.add(filterHasta);
		}

		Filter allFilter = null;
		// Se tiene en cuenta si los valores a filtrar son excluyentes o
		// incluyentes (un sólo tipo)
		// a la hora de crear el filtro (que se divide en dos subfiltros si
		// existieran operador y valor hasta).
		if (inclusivo) {
			Filter[] filterArray = (Filter[]) listaFiltro.toArray(new Filter[0]);
			allFilter = new AllFilter(filterArray);
		} else {
			for (Filter filter : listaFiltro) {
				allFilter = createOrFilter(allFilter, filter);
			}
		}
		return allFilter;
	}
	/**
	 * Construye el filtro de garantía.
	 * 
	 * @param filtro
	 *            Registro de tipo filtro de proceso
	 * @param extractor
	 *            Extractor preparado para el campo sobre el que se aplica el
	 *            filtro
	 * @param inclusivo
	 *            Si el filtro es inclusivo o exclusivo
	 * @return
	 * @throws Exception
	 */
	protected Filter getFiltroIntegerGar(FiltroFichaProcesoAdicional filtro, ValueExtractor extractor,
			Boolean inclusivo) throws Solvencia2Excepcion {

		Integer valorDesde = 0;

		Integer valorHasta = 0;

		String operadorDesde = "";

		String operadorHasta = "";

		// Comprobamos que los operadores del registro de tipo filtro de proceso
		// son válidos

		if (Operadores.validarOperadoresIntegerAdicionalGarantia(filtro)) {

			operadorDesde = filtro.getGopdesdegar();

			if (filtro.getGophastagar() != null) {
				operadorHasta = filtro.getGophastagar();
			}

			if (filtro.getValdesdegar() == null) {
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_ERROR_VALOR_OPERADORES, new Object[]{null});
			} else {
				try {
					valorDesde = Integer.parseInt(filtro.getValdesdegar());
					if (!operadorHasta.isEmpty()) {
						valorHasta = Integer.parseInt(filtro.getValhastagar());
					}
				} catch (Exception e) {
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_ERROR_VALOR_OPERADORES, null, e.getCause());
				}
			}
		}

		List<Filter> listaFiltro = new ArrayList<Filter>();

		Filter filterDesde = construirFiltro(extractor, operadorDesde, valorDesde, inclusivo);

		listaFiltro.add(filterDesde);

		if (!operadorHasta.isEmpty()) {
			Filter filterHasta = construirFiltro(extractor, operadorHasta, valorHasta, inclusivo);
			listaFiltro.add(filterHasta);
		}

		Filter allFilter = null;
		// Se tiene en cuenta si los valores a filtrar son excluyentes o
		// incluyentes (un sólo tipo)
		// a la hora de crear el filtro (que se divide en dos subfiltros si
		// existieran operador y valor hasta).
		if (inclusivo) {
			Filter[] filterArray = (Filter[]) listaFiltro.toArray(new Filter[0]);
			allFilter = new AllFilter(filterArray);
		} else {
			for (Filter filter : listaFiltro) {
				allFilter = createOrFilter(allFilter, filter);
			}
		}
		return allFilter;
	}

	/**
	 * En función del operador y si el filtro es inclusivo, se crea el filtro
	 * que se necesite.
	 * 
	 * @param extractor
	 * @param operador
	 * @param valor
	 * @param inclusivo
	 * @return
	 */
	public Filter construirFiltro(ValueExtractor extractor, String operador, Integer valor, Boolean inclusivo) { // NOSONAR

		Filter filter = null;
		switch (operador) {
		case Operadores.OPERADOR_IGUAL:
			if (inclusivo) {
				filter = new EqualsFilter(extractor, valor);
			} else {
				filter = new NotEqualsFilter(extractor, valor);
			}
			break;
		case Operadores.OPERADOR_MAYOR:
			if (inclusivo) {
				filter = new GreaterFilter(extractor, valor);
			} else {
				filter = new LessEqualsFilter(extractor, valor);
			}
			break;
		case Operadores.OPERADOR_MAYOR_IGUAL:
			if (inclusivo) {
				filter = new GreaterEqualsFilter(extractor, valor);
			} else {
				filter = new LessFilter(extractor, valor);
			}
			break;
		case Operadores.OPERADOR_MENOR:
			if (inclusivo) {
				filter = new LessFilter(extractor, valor);
			} else {
				filter = new GreaterEqualsFilter(extractor, valor);
			}
			break;
		case Operadores.OPERADOR_MENOR_IGUAL:
			if (inclusivo) {
				filter = new LessEqualsFilter(extractor, valor);
			} else {
				filter = new GreaterFilter(extractor, valor);
			}
			break;
		}

		return filter;
	}
	
	public Filter construirFiltroString(ValueExtractor extractor, String operador, String valor, Boolean inclusivo) { // NOSONAR

		Filter filter = null;
		switch (operador) {
		case Operadores.OPERADOR_IGUAL:
			if (inclusivo) {
				filter = new EqualsFilter(extractor, valor);
			} else {
				filter = new NotEqualsFilter(extractor, valor);
			}
			break;
		case Operadores.OPERADOR_MAYOR:
			if (inclusivo) {
				filter = new GreaterFilter(extractor, valor);
			} else {
				filter = new LessEqualsFilter(extractor, valor);
			}
			break;
		case Operadores.OPERADOR_MAYOR_IGUAL:
			if (inclusivo) {
				filter = new GreaterEqualsFilter(extractor, valor);
			} else {
				filter = new LessFilter(extractor, valor);
			}
			break;
		case Operadores.OPERADOR_MENOR:
			if (inclusivo) {
				filter = new LessFilter(extractor, valor);
			} else {
				filter = new GreaterEqualsFilter(extractor, valor);
			}
			break;
		case Operadores.OPERADOR_MENOR_IGUAL:
			if (inclusivo) {
				filter = new LessEqualsFilter(extractor, valor);
			} else {
				filter = new GreaterFilter(extractor, valor);
			}
			break;
		}

		return filter;
	}
}