package es.mapfre.solvencia.formulacion.modulos.impl;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.formulacion.FichaPeriodos;
import es.mapfre.solvencia.dominio.maestro.Rentas;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.Util;

/**
 * Clase que contiene las validaciones usadas en la elaboración del flujo de
 * periodos, tales como: - validacionAtributoCTipoRevRenta -
 * validacionAtributoCPagRenta - validacionAtributoCformaRevRenta -
 * validacionDatosObligatoriosGenPagosVitalicios -
 * validarCamposObligatoriosPeriodosModGAR - validacionBaseTecnica -
 * validacionCriterioPeriodos - validacionTiposPrestacion -
 * validacionVencimientoPeriodos - validacionPeriodicidad -
 * 
 * @author rschacon
 *
 */
public final class ValidacionesFlujoPeriodos {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ValidacionesFlujoPeriodos.class);

	/**
	 * Constructor privado.
	 */
	private ValidacionesFlujoPeriodos() {
		super();
	}

	/**
	 * Función que validará que la forma de revalorización de la renta, deba ser uno
	 * de los siguientes (en parentesis se informan las descripciones asociadas a
	 * los codigos): - 1 (Anno Natural (Enero)) - 2 (Aniversario).
	 * 
	 * @param cTipoRevRenta tipo revalorizacion de la renta
	 */
	public static void validacionAtributoCTipoRevRenta(final String cTipoRevRenta) {
		if (null == cTipoRevRenta || cTipoRevRenta.isEmpty()
				|| (!ConstantsModulos.CTE_VAL_FR_AN_NAT.equals(cTipoRevRenta)
						&& !ConstantsModulos.CTE_VAL_FR_ANIVER.equals(cTipoRevRenta)
						&& !ConstantsModulos.CTE_FR_ANIO_NATU_3.equals(cTipoRevRenta) 
						&& !ConstantsModulos.CTE_FR_ANIO_NATU_4.equals(cTipoRevRenta) 
						&& !ConstantsModulos.CTE_FR_ANIO_NATU_5.equals(cTipoRevRenta) 
						&& !ConstantsModulos.CTE_FR_ANIO_NATU_6.equals(cTipoRevRenta) 
						&& !ConstantsModulos.CTE_FR_ANIO_NATU_7.equals(cTipoRevRenta) 
						&& !ConstantsModulos.CTE_FR_ANIO_NATU_8.equals(cTipoRevRenta)  
						&& !ConstantsModulos.CTE_FR_ANIO_NATU_9.equals(cTipoRevRenta) 
						&& !ConstantsModulos.CTE_FR_ANIO_NATU_O.equals(cTipoRevRenta) 
						&& !ConstantsModulos.CTE_FR_ANIO_NATU_N.equals(cTipoRevRenta) 
						&& !ConstantsModulos.CTE_FR_ANIO_NATU_D.equals(cTipoRevRenta))) {
			
			if (ValidacionesFlujoPeriodos.LOG.isDebugEnabled()) {
				ValidacionesFlujoPeriodos.LOG
						.debug(Util.errorValidacionA2(cTipoRevRenta, ConstantsModulos.CTE_CTIPO_REV_REN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A2,
					new String[] { ConstantsModulos.CTE_CTIPO_REV_REN, cTipoRevRenta });
		}

	}

	/**
	 * Función que validará que la periodicidad del pago de la renta deba ser uno de
	 * los siguientes (en parentesis se informan las descripciones asociadas a los
	 * codigos): - 1 (anual) - 2 (semestral) - 3 (trimestral) - 4 (mensual).
	 * 
	 * @param cpagRenta periodicidad de la renta
	 */
	public static void validacionAtributoCPagRenta(final String cpagRenta) {
		if (null == cpagRenta || cpagRenta.isEmpty() || (!ConstantsModulos.TIPO_CPAG_RENTA.containsKey(cpagRenta))) {
			if (ValidacionesFlujoPeriodos.LOG.isDebugEnabled()) {
				ValidacionesFlujoPeriodos.LOG.debug(Util.errorValidacionA2(cpagRenta, ConstantsModulos.CTE_CPAG_RENTA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A2,
					new String[] { ConstantsModulos.CTE_CPAG_RENTA, cpagRenta });
		}
	}

	/**
	 * Función que validará que el código de revalorización de la renta deba ser uno
	 * de los siguientes (en parentesis se informan las descripciones asociadas a
	 * los codigos) - 'A' (Aritmetica) - 'G' (Geometrica) - 'C' (Constante). - 'N'
	 * (Negativa).
	 *
	 * @param cFormaRevRenta revalorización de la renta
	 */
	public static void validacionAtributoCformaRevRenta(final String cFormaRevRenta) {
		if (null == cFormaRevRenta || cFormaRevRenta.isEmpty()
				|| (!ConstantsModulos.TIPO_CFORMAREVREN.containsKey(cFormaRevRenta))) {
			if (ValidacionesFlujoPeriodos.LOG.isDebugEnabled()) {
				ValidacionesFlujoPeriodos.LOG
						.debug(Util.errorValidacionA2(cFormaRevRenta, ConstantsModulos.CTE_CFOR_REV_REN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A2,
					new String[] { ConstantsModulos.CTE_CFOR_REV_REN, cFormaRevRenta });
		}
	}

	/**
	 * Funcion encargada de validar si los datos de entrada de la funcion
	 * genPagosVitalicios son obligatorios.
	 *
	 * @param fecFinProy Identifica la fecha de fin de los pagos a generar.
	 * @param umicRenta  Identifica la configuracion de la renta para una umic dada
	 */
	public static void validacionDatosObligatoriosGenPagosVitalicios(final Timestamp fecFinProy,
			final Rentas umicRenta) {
		if (null == fecFinProy) {
			if (ValidacionesFlujoPeriodos.LOG.isDebugEnabled()) {
				ValidacionesFlujoPeriodos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_FEC_FIN_PROY));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1,
					new String[] { ConstantsModulos.CTE_FEC_FIN_PROY });
		}

		if (null == umicRenta) {
			if (ValidacionesFlujoPeriodos.LOG.isDebugEnabled()) {
				ValidacionesFlujoPeriodos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_UMIC_RENTA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1,
					new String[] { ConstantsModulos.CTE_UMIC_RENTA });
		}
	}

	/**
	 * Funcion encargada de validar la obligatoriedad los parametros de entrada de
	 * las funcines "periodosModGAR" y "periodos".
	 * 
	 * @param fichaPeriodo Contiene los datos del proceso necesarios para su
	 *                     ejecución
	 * @param umic         umic que se esta tratando
	 * @param fcalc        fecha de calculo
	 */
	public static void validarCamposObligatoriosPeriodosModGAR(final FichaPeriodos fichaPeriodo, final Umic umic,
			final Timestamp fcalc, final Timestamp fecCierre) {

		if (null == fichaPeriodo) {
			if (ValidacionesFlujoPeriodos.LOG.isDebugEnabled()) {
				ValidacionesFlujoPeriodos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_FICHA_PERIODO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1,
					new String[] { ConstantsModulos.CTE_FICHA_PERIODO });
		}

		if (null == umic) {
			if (ValidacionesFlujoPeriodos.LOG.isDebugEnabled()) {
				ValidacionesFlujoPeriodos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1,
					new String[] { ConstantsModulos.CTE_UMIC });
		}

		if (null == fcalc) {
			if (ValidacionesFlujoPeriodos.LOG.isDebugEnabled()) {
				ValidacionesFlujoPeriodos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_FCALC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1,
					new String[] { ConstantsModulos.CTE_FCALC });
		}

		if (null == fecCierre) {
			if (ValidacionesFlujoPeriodos.LOG.isDebugEnabled()) {
				ValidacionesFlujoPeriodos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_FEC_CIERRE));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1,
					new String[] { ConstantsModulos.CTE_FEC_CIERRE });
		}
	}

	/**
	 * Función que validará que el código de la base tecnica sea uno de los
	 * siguientes: (en parentesis se informan las descripciones asociadas a los
	 * codigos )
	 *
	 * - BTI (Base Tecnica Inicial) - BEL - ROSSP
	 *
	 * @param codigoBaseTecnica código de la base tecnica a validar
	 */
	public static void validacionBaseTecnica(final String codigoBaseTecnica) {
		// Comprobamos a que tipo de base tecnica nos estamos enfrentando

		if (null == codigoBaseTecnica || codigoBaseTecnica.isEmpty()
				|| (!ConstantsModulos.TIPO_BASE_TECNICA.containsKey(codigoBaseTecnica))) {
			if (ValidacionesFlujoPeriodos.LOG.isDebugEnabled()) {
				ValidacionesFlujoPeriodos.LOG
						.debug(Util.errorValidacionA3(codigoBaseTecnica, ConstantsModulos.CTE_COD_BT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A3,
					new String[] { ConstantsModulos.CTE_COD_BT, codigoBaseTecnica });
		}
	}

	/**
	 * Función que validará que el criterio de los periodos seba ser uno de los
	 * siguientes: (en parentesis se informan las descripciones asociadas a los
	 * codigos ).
	 *
	 * BTI (BN999XXX - Calculo Periodos de Proyeccion por Criterio) LEIDOS
	 * (BN999XX1- Lectura Periodos BTI Anterior)
	 *
	 * @param criterioPeriodos criterio del periodo
	 */
	public static void validacionCriterioPeriodos(final String criterioPeriodos) {

		// Comprobamos a que tipo de criterios nos estamos enfrentando
		if (null != criterioPeriodos && !criterioPeriodos.isEmpty()
				&& (!ConstantsModulos.CTE_VAL_PER_BTI.equals(criterioPeriodos)
						&& !ConstantsModulos.CTE_VAL_PER_LEI.equals(criterioPeriodos))) {
			if (ValidacionesFlujoPeriodos.LOG.isDebugEnabled()) {
				ValidacionesFlujoPeriodos.LOG
						.debug(Util.errorValidacionA2(criterioPeriodos, ConstantsModulos.CTE_CRI_PER));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A2,
					new String[] { ConstantsModulos.CTE_CRI_PER, criterioPeriodos });
		}
	}

	/**
	 * Función que validará que el tipo de prestación deba ser uno de los
	 * siguientes: (en parentesis se informan las descripciones asociadas a los
	 * codigos)
	 *
	 * - T (Temporal) - V (Vitalicia) -L(Loca).
	 *
	 * @param tipoPrestacion tipo de la prestacion
	 */
	public static void validacionTiposPrestacion(final String tipoPrestacion) {

		// Comprobamos a que tipo de prestacion nos encontramos
		if (null == tipoPrestacion || tipoPrestacion.isEmpty()
				|| (!ConstantsModulos.CTE_VAL_TP_TEM.equals(tipoPrestacion)
						&& !ConstantsModulos.CTE_VAL_TP_VIT.equals(tipoPrestacion)
						&& !ConstantsModulos.CTE_VAL_TP_LOC.equals(tipoPrestacion))) {
			if (ValidacionesFlujoPeriodos.LOG.isDebugEnabled()) {
				ValidacionesFlujoPeriodos.LOG
						.debug(Util.errorValidacionA2(tipoPrestacion, ConstantsModulos.CTE_TIPO_PRES));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC,
					new String[] { tipoPrestacion, ConstantsModulos.CTE_TIPO_PRES });
		}
	}

	/**
	 * Función que validará que el vencimiento de los periodos deba ser uno de los
	 * siguientes: (en parentesis se informan las descripciones asociadas a los
	 * codigos )
	 *
	 * - Vitalicia - Edad Exclusion - Numero fijo de periodos - Tratamiento T.A.R. -
	 * Planificados
	 *
	 * @param vencPeriodo vencimiento del periodo
	 */
	public static void validacionVencimientoPeriodos(final String vencPeriodo) {

		// Comprobamos el vencimiento del periodo al que nos enfrentamos
		if (null == vencPeriodo || vencPeriodo.isEmpty()
				|| (!ConstantsModulos.TIPO_VENC_PERIOD.containsKey(vencPeriodo))) {
			if (ValidacionesFlujoPeriodos.LOG.isDebugEnabled()) {
				ValidacionesFlujoPeriodos.LOG
						.debug(Util.errorValidacionA2(vencPeriodo, ConstantsModulos.CTE_VENCI_PER));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A2,
					new String[] { ConstantsModulos.CTE_VENCI_PER, vencPeriodo });
		}
	}

	/**
	 * Función que validará que la periodicidad deba ser uno de los siguientes: (en
	 * parentesis se informan las descripciones asociadas a los codigos )
	 *
	 * - Planificada - Mensual - Anual.
	 *
	 * @param periodicidad periodiciadad
	 */
	public static void validacionPeriodicidad(final String periodicidad) {

		// Comprobamos la periodicidad con la que nos enfrentamos
		if (null == periodicidad || periodicidad.isEmpty()
				|| (!ConstantsModulos.TIPO_PERIODICIAD.containsKey(periodicidad))) {
			if (ValidacionesFlujoPeriodos.LOG.isDebugEnabled()) {
				ValidacionesFlujoPeriodos.LOG
						.debug(Util.errorValidacionA2(periodicidad, ConstantsModulos.CTE_PERIODICIDAD));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A2,
					new String[] { ConstantsModulos.CTE_PERIODICIDAD, periodicidad });
		}
	}
}
