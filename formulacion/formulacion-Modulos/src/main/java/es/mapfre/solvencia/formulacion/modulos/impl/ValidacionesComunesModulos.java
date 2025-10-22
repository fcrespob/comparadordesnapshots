package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.conversionesBel.GastosReales;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresAnulacion;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresAnulacionMensuales;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresCurvaTipo;
import es.mapfre.solvencia.dominio.formulacion.CriterioFechas;
import es.mapfre.solvencia.dominio.formulacion.FichaProcesoModulo;
import es.mapfre.solvencia.dominio.formulacion.Periodo;
import es.mapfre.solvencia.dominio.maestro.CuadrosAmortizacion;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.LimitesCapital;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.OpcionesGeneracion;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.Util;

/**
 * Clase que contiene validaciones comunes para los distintos modulos distintos modulos de la aplicación.
 * @author rschacon
 *
 */
public final class ValidacionesComunesModulos {

	/** Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ValidacionesComunesModulos.class);

	/** Constructor privado.
	 */
	private ValidacionesComunesModulos() { super(); }

	/** 
	 * Función que valida la obligatoriedad de los parametros de entrada de cada uno de los distintos modulos de la aplicación.
	 * 
	 * @param proyUmic Estructura detalleCorrientes de la umic
	 * @param bloqueCorriente Bloque de Trabajo de la corriente
	 * @param fcalc Fecha de calculo
	 * @param umic Contiene los datos de la Umic que se está procesando
	 * @param btcUmic Contiene el detalle de la base técnica de cálculo para la umic
	 */
	public static void validarParamEntrada(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic) {
		
		//Validamos datos obligatorios
		if (null == proyUmic) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_PROY_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsModulos.CTE_PROY_UMIC});
		}

		if (null == bloqueCorriente) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_BLOQUE_CORRIENTE));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsModulos.CTE_BLOQUE_CORRIENTE});
		}

		if (null == fcalc) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_FCALC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsModulos.CTE_FCALC});
		}

		if (null == umic) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsModulos.CTE_UMIC});
		}
		
		if (null == btcUmic) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_BTC_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsModulos.CTE_BTC_UMIC});
		}
	}
	
	/** 
	 * Función que valida la obligatoriedad de los parametros de entrada de cada uno de los distintos modulos de la aplicación (No valida el bloque de la corriente).
	 * 
	 * @param proyUmic Estructura detalleCorrientes de la umic
	 * @param bloqueCorriente Bloque de Trabajo de la corriente
	 * @param fcalc Fecha de calculo
	 * @param umic Contiene los datos de la Umic que se está procesando
	 * @param btcUmic Contiene el detalle de la base técnica de cálculo para la umic
	 */
	public static void validarParamEntrada(final List<DetalleCorriente> proyUmic, final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic) {
		
		//Validamos datos obligatorios
		if (null == proyUmic) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_PROY_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsModulos.CTE_PROY_UMIC});
		}

		if (null == fcalc) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_FCALC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsModulos.CTE_FCALC});
		}

		if (null == umic) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsModulos.CTE_UMIC});
		}
		
		if (null == btcUmic) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_BTC_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsModulos.CTE_BTC_UMIC});
		}
	}
	
	
	public static void validarParamEntrada(final Periodo periodo, final DetalleCorriente detalleCorriente,
			final FichaProcesoModulo fichaProceso, final Umic umic) {
		
		//Validamos datos obligatorios
		if (null == periodo) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_PERIODO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsModulos.CTE_PERIODO});
		}

		if (null == detalleCorriente) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_DET_CORRIENTE));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsModulos.CTE_DET_CORRIENTE});
		}

		if (null == fichaProceso) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_FICHA_PROCESO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsModulos.CTE_FICHA_PROCESO});
		}

		if (null == umic) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsModulos.CTE_UMIC});
		}
	}
	
	/** 
	 * Función que valida la obligatoriedad de los parametros de entrada de la provision matematica.
	 * 
	 * @param proyUmic Estructura detalleCorrientes de la umic
	 * @param fcalc Fecha de calculo
	 * @param umic  Contiene los datos de la Umic que se está procesando.
	 * @param btcUmic Contiene el detalle de la base técnica de cálculo para la umic.
	 */
	public static void validarParamEntradaProvisionMatematica(final List<DetalleCorriente> proyUmic, final Timestamp fcalc, final Umic umic,
			final DetalleBaseTecnica btcUmic) {
		
		//Validamos datos obligatorios
		if (null == proyUmic) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_PROY_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsModulos.CTE_PROY_UMIC});
		}

		if (null == fcalc) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_FCALC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsModulos.CTE_FCALC});
		}
		
		if (null == umic) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsModulos.CTE_UMIC});
		}
		
		if (null == btcUmic) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_BTC_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsModulos.CTE_BTC_UMIC});
		}
	}
	
	/**
	 * Función encargada de validar que la variable de apoyo ID-TEMPORAL no sea nula
	 * 
	 * @param varCriterFec variable con el de la variable de apoyo criterio de fecha
	 */
	public static void validarVariableDeApoyoVarCriterioFecha(final String varCriterFec) {
		
		if (null == varCriterFec) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA6(ConstantsModulos.CTE_VA_CRIT_FEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A6, new String[]{ConstantsModulos.CTE_VA_CRIT_FEC});
		}
	}
	
	/** 
	 * Función encargada de validar que la variable de apoyo NRTA no sea nula
	 * 
	 * @param NRTA 
	 */
	public static void validarVariableDeApoyoNRTA(final Integer NRTA) {
		
		if (null == NRTA) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA6(ConstantsModulos.CTE_NRTA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A6, new String[]{ConstantsModulos.CTE_NRTA});
		}
	}
	
	/** 
	 * Función encargada de validar que la variable de apoyo BETARENTAS no sea nula
	 * 
	 * @param BETARENTAS 
	 */
	public static void validarVariableDeApoyoBETARENTAS(final BigDecimal BETARENTAS) {
		
		if (null == BETARENTAS) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA6(ConstantsModulos.CTE_BETARENTAS));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A6, new String[]{ConstantsModulos.CTE_BETARENTAS});
		}
	}
	
	/** 
	 * Función encargada de validar que la variable de apoyo BetaIDMS no sea nula
	 * 
	 * @param BetaIDMS 
	 */
	public static void validarVariableDeApoyoBetaIDMS(final BigDecimal BetaIDMS) {
		
		if (null == BetaIDMS) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA6(ConstantsModulos.CTE_BETAIDMS));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A6, new String[]{ConstantsModulos.CTE_BETAIDMS});
		}
	}
	
	/**
	 * Función encargada de validar que la variable de apoyo CRITER_INTER no sea nula
	 * 
	 * @param varCriterInt variable con el valor de la variable de apoyo CRITER_INTER
	 */
	public static void validarVariableDeApoyoVarCritInt(final String varCriterInt) {
		
		if (null == varCriterInt) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA6(ConstantsModulos.CTE_VA_CRIT_INT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A6, new String[]{ConstantsModulos.CTE_VA_CRIT_INT});
		}
	}
	
	/**
	 * Función encargada de validar que la variable de apoyo IFAL no sea nula
	 * 
	 * @param varIFal variable con el valor de la variable de apoyo IFAL
	 */
	public static void validarVariableDeApoyoVarIFal(final BigDecimal varIFal) {
		
		if (null == varIFal) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA6(ConstantsModulos.CTE_VA_IFAL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A6, new String[]{ConstantsModulos.CTE_VA_IFAL});
		}
	}
	
	/**
	 * Función encargada de validar que la variable de apoyo PAS no sea nula
	 * 
	 * @param varPas variable con el valor de la variable de apoyo PAS
	 */
	public static void validarVariableDeApoyoVarPas(final BigDecimal varPas) {
		
		if (null == varPas) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA6(ConstantsModulos.CTE_VA_PAS));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A6, new String[]{ConstantsModulos.CTE_VA_PAS});
		}
	}
	
	/**
	 * Función encargada de validar que la variable de apoyo FUT no sea nula
	 * 
	 * @param varFut variable con el valor de la variable de apoyo FUT
	 */
	public static void validarVariableDeApoyoVarFut(final BigDecimal varFut) {
		
		if (null == varFut) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA6(ConstantsModulos.CTE_VA_FUT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A6, new String[]{ConstantsModulos.CTE_VA_FUT});
		}
	}
	
	/**
	 * Función encargada de validar que la variable de apoyo MESCAREN no sea nula
	 * 
	 * @param varMescaren variable con el valor de la variable de apoyo MESCAREN
	 */
	public static void validarVariableDeApoyoVarMescaren(final Integer varMescaren) {
		
		if (null == varMescaren) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA6(ConstantsModulos.CTE_VA_MESCAREN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A6, new String[]{ConstantsModulos.CTE_VA_MESCAREN});
		}
	}
	
	/**
	 * Función encargada de validar que la variable de apoyo ID-CRITERIO no sea nula
	 * 
	 * @param varCriterioEdad variable con el valor de la variable de apoyo ID-CRITERIO
	 */
	public static void validarVariableDeApoyoVarCriEdad(final String varCriterioEdad) {
		
		if (null == varCriterioEdad) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA6(ConstantsModulos.CTE_VA_CRIT_EDA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A6, new String[]{ConstantsModulos.CTE_VA_CRIT_EDA});
		}
	}
	
	
	/**
	 * Función encargada de validar que la variable de apoyo MODBETA no sea nula
	 * 
	 * @param varModBeta variable con el valor de la variable de apoyo MODBETA
	 */
	public static void validarVariableDeApoyoVarModBeta(final String varModBeta) {
		
		if (null == varModBeta) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA6(ConstantsModulos.CTE_VA_MODBETA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A6, new String[]{ConstantsModulos.CTE_VA_MODBETA});
		}
	}
	
	/**
	 * Función encargada de validar que la variable de apoyo MOD_BETA no sea nula
	 * 
	 * @param varModBeta variable con el valor de la variable de apoyo MOD_BETA
	 */
	public static void validarVariableDeApoyoVarMod_Beta(final BigDecimal varModBeta) {
		
		if (null == varModBeta) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA6(ConstantsModulos.CTE_VA_MOD_BETA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A6, new String[]{ConstantsModulos.CTE_VA_MOD_BETA});
		}
	}
	
	/**
	 * Función encargada de validar que las varables varkx (siendo los posibles valores de k 1,2 y 3) que estas no tengan un valor nulo.
	 * 
	 * @param varKX valor a remplazar en el mensajes de error
	 * @param valorVarKx  variable con el valor de la constante de rescate analizada
	 */
	public static void validarCteRescateVarkx(final String varKX, final BigDecimal valorVarKx) {
		
		if(null == varKX){
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionAB());
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AB);
		}else if (null == valorVarKx) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA8(varKX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A8, new String[]{varKX});
		}
	}
	
	/**
	 * Función encargada de validar que la variable de apoyo IANT no sea nula
	 * 
	 * @param iant
	 */
	public static void validarVariableDeApoyoIant(final BigDecimal iant) {
		
		if (null == iant) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA6(ConstantsModulos.CTE_VA_IANT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A6, new String[]{ConstantsModulos.CTE_VA_IANT});
		}
	}
	
	/**
	 * Función encargada de validar que la variable de apoyo TIPO_ALFA no sea nula
	 * 
	 * @param varTipoAlfa
	 */
	public static void validarVariableDeApoyoVarTipoAlfa(final String varTipoAlfa) {
		
		if (null == varTipoAlfa) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA6(ConstantsModulos.CTE_VA_TIPO_ALFA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A6, new String[]{ConstantsModulos.CTE_VA_TIPO_ALFA});
		}
	}
	
	/**
	 * Función que valida el rango de valores de los parametros, permitidos para los criterios de fecha pago y fecha devengo.
	 * 
	 * @param criterioFecPago
	 *            criterio fecha pago
	 * @param criterioFecDev
	 *            criterio fecha devengo
	 */
	public static void validarCriterioFechaPagoDevengo(final String criterioFecPago, final String criterioFecDev, final String subProceso) {
		
		if (null == criterioFecPago || criterioFecPago.isEmpty()
				|| (!ConstantsModulos.TIPO_FEC_PAG_DEV.containsKey(criterioFecPago))) {

			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionAF("pago", criterioFecPago, subProceso));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AF, new String[]{"pago", criterioFecPago, subProceso});
		}

		if (null == criterioFecDev || criterioFecDev.isEmpty()
				|| (!ConstantsModulos.TIPO_FEC_PAG_DEV.containsKey(criterioFecDev))) {

			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionAF("devengo", criterioFecDev, subProceso));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AF, new String[]{"devengo", criterioFecDev, subProceso});
		}
	}

	/**
	 * Función que valida el criterio de fechas recuperado del servicio de datos.
	 * 
	 * @param criterioFecha
	 *            criterio fecha
	 * @param kmodalidad
	 *            modalidad
	 * @param kgarantia
	 *            garantia
	 * @param kprestacion
	 *            prestacion
	 */
	public static void validarCriterioFechaRecuperado(final CriterioFechas criterioFecha, final Integer kmodalidad, final Integer kgarantia, final String kprestacion) {
		if (null == criterioFecha) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_D1, new String[]{kmodalidad.toString(), kgarantia.toString(), kprestacion});
		}

	}

	/**
	 * Función que valida el criterio de edad recuperado del servicio de datos.
	 * 
	 * @param criterioEdad
	 *            criterio edad
	 * @param kmodalidad
	 *            modalidad
	 * @param kgarantia
	 *            garantia
	 */
	public static void validarCriterioEdadRecuperado(final String criterioFecha, final Integer kmodalidad, final Integer kgarantia) {
		if (null == criterioFecha) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_D2, new String[]{Util.errorString(kmodalidad), Util.errorString(kgarantia)});
		}
	}
	
	/**
	 * Función que valida la edad máxima recuperada del servicio de datos.
	 * 
	 * @param edadMax
	 *            edad máxima
	 * @param kmodalidad
	 *            modalidad
	 * @param kgarantia
	 *            garantia
	 */
	public static void validarEdadMax(final Integer edadMax, final Integer kmodalidad, final Integer kgarantia) {
		if (null == edadMax) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_D3, new String[]{Util.errorString(kmodalidad), Util.errorString(kgarantia)});
		}
	}
	
	/**
	 * Función que valida las opciones de generación recuperadas del servicio de datos.
	 * 
	 * @param opGeneracion
	 *            opciones de generación
	 * @param kmodalidad
	 *            modalidad
	 * @param kgarantia
	 *            garantia
	 */
	public static void validarOpcionesGeneracion(final OpcionesGeneracion opGeneracion, final Integer kmodalidad, final Integer kgarantia, final String kprestacion) {
		if (null == opGeneracion) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_D4, new String[]{Util.errorString(kmodalidad), Util.errorString(kgarantia), Util.errorString(kprestacion)});
		}
	}
	
	/**
	 * Función que valida el objeto LimitesCapital recuperado del servicio de datos.
	 * 
	 * @param limitesCapital
	 *            limites capital
	 * @param kmodalidad
	 *            modalidad
	 * @param kgarantia
	 *            garantia
	 * @param fecinisus
	 *            fecha inicio suscripcion
	 * @param varX
	 *            x
	 * @param varK
	 *            k
	 */
	public static void validarLimitesCapital(final LimitesCapital limitesCapital, final Integer kmodalidad, final Integer kgarantia, final Timestamp fecinisus, final Integer varX, final Integer varK) {
		if (null == limitesCapital) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_D5, new String[]{Util.errorString(kmodalidad), Util.errorString(kgarantia), Util.errorString(fecinisus), Util.errorString(varX), Util.errorString(varK)});
		}
	}
	
	/**
	 * Función que valida el objeto LimitesCapital recuperado del servicio de datos.
	 * 
	 * @param limitesCapital
	 *            limites capital
	 * @param kmodalidad
	 *            modalidad
	 * @param kgarantia
	 *            garantia
	 * @param fecinisus
	 *            fecha inicio suscripcion
	 * @param varX
	 *            x
	 */
	public static void validarListaLimitesCapital(final List<LimitesCapital> limitesCapital, final Integer kmodalidad, final Integer kgarantia, final Timestamp fecinisus, final Integer varX) {
		if (null == limitesCapital) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_D5, new String[]{Util.errorString(kmodalidad), Util.errorString(kgarantia), Util.errorString(fecinisus), Util.errorString(varX)});
		}
	}

	/**
	 * Función que valida los datos obtenidos de CONF_FLUJOS_PROB recuperados del servicio de datos.
	 * 
	 * @param flujos
	 *            flujos probables
	 * @param kmodalidad
	 *            modalidad
	 * @param kgarantia
	 *            garantia
	 * @param kprestacion
	 *            prestacion
	 * @param baseTec
	 *            base técnica
	 */
	public static void validarConfProv(final FlujosProbables flujos, final Integer kmodalidad, final Integer kgarantia, final String kprestacion, final String baseTec) {
		if (null == flujos) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_D6, new String[]{Util.errorString(kmodalidad), Util.errorString(kgarantia), Util.errorString(kprestacion), Util.errorString(baseTec)});
		}
	}
	

	/**
	 * Función que valida la lista de ValoresAnulacionMensuales recuperados del servicio de datos.
	 * 
	 * @param valoresTasasAnul
	 *            valores tasas anulación
	 * @param tablaTanul
	 *            tabla anulación
	 * @param fcurvaAn
	 *            fecha curva anulación
	 */
	public static void validarTasasAnulMensuales(final List<ValoresAnulacionMensuales> valoresTasasAnul, final String tablaTanul, final Timestamp fcurvaAn) {
		if (null == valoresTasasAnul || valoresTasasAnul.isEmpty()) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_D8, new String[]{Util.errorString(tablaTanul), Util.errorString(fcurvaAn)});
		}
	}
	
	/**
	 * Función que valida la lista de ValoresAnulacion recuperados del servicio de datos.
	 * 
	 * @param valoresTasasAnul
	 *            valores tasas anulación
	 * @param tablaTanul
	 *            tabla anulación
	 * @param fcurvaAn
	 *            fecha curva anulación
	 */
	public static void validarTasasAnul(final List<ValoresAnulacion> valoresTasasAnul, final String tablaTanul, final Timestamp fcurvaAn) {
		if (null == valoresTasasAnul || valoresTasasAnul.isEmpty()) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_D8, new String[]{Util.errorString(tablaTanul), Util.errorString(fcurvaAn)});
		}
	}
	
	/**
	 * Función que valida el valos del cuadro de amortización para una determinada Umic recuerado del servicio de datos.
	 * @param cuadroAmortizacion
	 * @param key
	 */
	public static void validarCuadrosAmortizacion(final CuadrosAmortizacion cuadroAmortizacion) {
		if (null == cuadroAmortizacion) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AW);
		}
	}
	
	/**
	 * Función que valida el objeto de tipo GastosReales recuperado del servicio de datos.
	 * 
	 * @param gastosReales
	 *            Estructura GastosReales
	 * @param kmodalidad
	 *            modalidad
	 * @param baseTec
	 *            base técnica
	 * @param ccanal
	 *            canal
	 * @param cnegocio
	 *            negocio
	 * @param kramo
	 *            ramo
	 * @param fecCierre
	 *            Fecha de cierre
	 */
	public static void validarGastosReales(final GastosReales gastosReales, final Integer kmodalidad, final String baseTec, final Integer ccanal, final String cnegocio, final String kramo, final Timestamp fecCierre, final String matching) {
		if (null == gastosReales) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_D9, new String[]{Util.errorString(kmodalidad), Util.errorString(baseTec), Util.errorString(ccanal), Util.errorString(cnegocio), Util.errorString(kramo), Util.errorString(fecCierre), Util.errorString(matching)});
		}
	}
	
	/**
	 * Función que valida la lista de ValoresCurvaTipo recuperados del servicio de datos.
	 * 
	 * @param valoresTipos
	 *            Lista de ValoresCurvaTipo
	 * @param codCurvaTipo
	 *            código de tipo de curva
	 * @param fecCierre
	 *            Fecha de cierre
	 */
	public static void validarValoresCurvaTipo(final List<ValoresCurvaTipo> valoresTipos, final String codCurvaTipo, final Timestamp fecCierre) {
		if (null == valoresTipos || valoresTipos.isEmpty()) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DA, new String[]{Util.errorString(codCurvaTipo), Util.errorString(fecCierre)});
		}
	}
	
	/**
	 * Función que valida el ipc recuperado del servicio de datos.
	 * 
	 * @param varIpc
	 *            ipc
	 * @param fecha
	 *            fecha
	 */
	public static void validarIpcFuturo(final BigDecimal varIpc, final Timestamp fecha) {
		if (null == varIpc) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DB, new String[]{Util.errorString(fecha)});
		}
	}
	
	public static void validarDatosBel(final DetalleBaseTecnica btcUmic){
		
		if (btcUmic.getFactorInterpolInteresesBel() == null){
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A2, new String[]{Util.errorString(btcUmic.getPerTransBel()), "FactorInterpolInteresesBel"});
		}
	}
	
	public static void validarBtcUmicROSSP(DetalleBaseTecnica btcUmicROSSP){
		if (btcUmicROSSP == null){
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A2, new String[]{Util.errorString(btcUmicROSSP), "btcUmicROSSP"});
		}
	}
	
	/**
	 * Función encargada de validar que la variable de apoyo POR_INI no sea nula
	 * 
	 * @param varPorIni variable con el de la variable de apoyo por ini
	 */
	public static void validarVariableDeApoyoVarPorIni(final BigDecimal varPorIni) {
		
		if (null == varPorIni) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA6(ConstantsModulos.CTE_VA_POR_INI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A6, new String[]{ConstantsModulos.CTE_VA_POR_INI});
		}
	}
	
	/**
	 * Función encargada de validar que la variable de apoyo POR_DEC no sea nula
	 * 
	 * @param varPorIni variable con el de la variable de apoyo por dec
	 */
	public static void validarVariableDeApoyoVarPorDec(final BigDecimal varPorDec) {
		
		if (null == varPorDec) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA6(ConstantsModulos.CTE_VA_POR_DEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A6, new String[]{ConstantsModulos.CTE_VA_POR_DEC});
		}
	}
	
	/**
	 * Función encargada de validar que la variable de apoyo PER_DEC no sea nula
	 * 
	 * @param varPorIni variable con el de la variable de apoyo per dec
	 */
	public static void validarVariableDeApoyoVarPerDec(final BigDecimal varPerDec) {
		
		if (null == varPerDec) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA6(ConstantsModulos.CTE_VA_PER_DEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A6, new String[]{ConstantsModulos.CTE_VA_PER_DEC});
		}
	}
	
	/**
	 * Función encargada de validar que la variable de apoyo MESES_CIERRE_NIIF17 no sea nula
	 * 
	 * @param varMesesCierres variable con el de la variable de apoyo meses de cierres
	 */
	public static void validarVariableDeApoyoVarMesesCierres(final String varMesesCierres) {
		
		if (null == varMesesCierres) {
			if (ValidacionesComunesModulos.LOG.isDebugEnabled()) {
				ValidacionesComunesModulos.LOG.debug(Util.errorValidacionA6(ConstantsModulos.CTE_VA_MES_CIER));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A6, new String[]{ConstantsModulos.CTE_VA_MES_CIER});
		}
	}
}
