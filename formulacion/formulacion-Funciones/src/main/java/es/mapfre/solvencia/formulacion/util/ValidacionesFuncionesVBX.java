package es.mapfre.solvencia.formulacion.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;

public class ValidacionesFuncionesVBX {
	
	/** Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ValidacionesFuncionesVBX.class);
	
	/**
	 * Función encargada de validar los campos obligatorios de la función
	 * ddEnero
	 * 
	 * @param fechaEntrada
	 *            Identifica la fecha sobre la que se efectua el calculo
	 * @param criterioFecha
	 *            Identifica el criterio de fechas para realizar el calculo
	 */
	public static void validacionCamposObligatoriosDDEnero(final Timestamp fechaEntrada, final String criterioFecha) {

		if (null == fechaEntrada) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FECHA_ENTRADA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FECHA_ENTRADA});
		}

		if (null == criterioFecha || criterioFecha.isEmpty()) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CRITERIO_FEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CRITERIO_FEC});
		}
	}

	/**
	 * Función encargada de realizar las validaciones necesarias para el campo criterioFecha de la Funcion auxiliar ddEnero
	 * 
	 * @param criterioFecha
	 *            Identifica el criterio de fechas para realizar el calculo
	 */
	public static void validacionCriterioFechaFuncionDDEneroEY(final String criterioFecha) {

		if (ConstantsFunciones.CTE_CRI_FECHA_03.equals(criterioFecha) || ConstantsFunciones.CTE_CRI_FECHA_04.equals(criterioFecha)) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA3(criterioFecha, ConstantsFunciones.CTE_CRITERIO_FEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A3, new String[]{criterioFecha, ConstantsFunciones.CTE_CRITERIO_FEC});
		} else if ((null != criterioFecha && !criterioFecha.isEmpty())
				&& (!ConstantsFunciones.TIPO_CRITER_FECHA.containsKey(criterioFecha))) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA2(criterioFecha, ConstantsFunciones.CTE_CRITERIO_FEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A2, new String[]{ConstantsFunciones.CTE_CRITERIO_FEC});
		}
	}
	

	/**
	 * @param beta1
	 * 				Variable Beta1
	 * @param beta2
	 * 				Variable Beta2
	 * @param difercol
	 * 				Diferimiento en cobrar la renta
	 * @param varTCY
	 * 				Resultado de la función naños() en base al criterio establecido entre la fecha de efecto de la suscripción y la fecha de cálculo
	 */
	public static void validarParamEntradaFuncionPuccapDiferParte2 (final BigDecimal beta1, final BigDecimal beta2, final BigDecimal difercol,  final BigDecimal varTCY) {
		
		if (null == beta1) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA1});
		}
		
		if (null == beta2) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA2});
		}
		
		if (null == difercol) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DIFERCOL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DIFERCOL});
		}
				
		if (null == varTCY) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TCY));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TCY});
		}
	}
	
	/**
	 * Funcion para la validar los campos de entrada de la funcion puccapDifer
	 * 
	 * @param varPu
	 * 				Aportación unica del ajuste o de la suscripcion
	 * @param i1BTI
	 * 				Primer Interes tecnico segun Bases Tecnicas Iniciales
	 * @param i2BTI
	 * 				Segundo Interes tecnico segun Bases Tecnicas Iniciales
	 * @param varNr
	 * 				Nº. de renovaciones transcurridas desde la fecha de alta del ajuste o suscripcion hasta TC+fracAnioInc
	 * @param varNrm
	 * 				Nº. de renovaciones transcurridas desde la fecha de alta el ajuste hasta la fecha de fin de casamiento (M), dicho dia incluido
	 * @param varm
	 * 				Numero de años en que aplicamos un primer interes tecnico
	 */
	public static void validarParamEntradaFuncionPuccapDiferParte1(final BigDecimal varPu, final BigDecimal i1BTI, final BigDecimal i2BTI, final Integer varNr, final Integer varNrm, final BigDecimal varm) {
		
		if (null == varPu) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PU));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PU});
		}
		
		if (null == i1BTI) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_I1_BTI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_I1_BTI});
		}
		
		if (null == i2BTI) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_I2_BTI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_I2_BTI});
		}
		
		if (null == varNr) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_NR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_NR});
		}
		
		if (null == varNrm) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_NRM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_NRM});
		}
		
		if (null == varm) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
	}
	
	/**
	 * Función encargada de validar los parametros de entrada de la función mg001Alfa.
	 * 
	 * @param fantRenovacion
	 *            Identifica la fecha de efecto o renovación mensual anterior
	 * @param fproxRenovacion
	 *            Identifica la fecha de la próxima renovación mensual.
	 * @param fcierre
	 *            Identifica la fecha de cierre
	 * @param tipo
	 *           Tipo del cálculo: PRORRATA o FORFAIT
	 * @param criterioFecha
	 * 			 Identifica el criterio de fechas para realizar el cálculo
	 */
	public static void validarCriteriosFuncionMg001Alfa(final Timestamp fantRenovacion, final Timestamp fproxRenovacion,
			final Timestamp fcierre, final String tipo, final String criterioFecha) {
		
		if (null == fantRenovacion) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_ANT_RENOV));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_ANT_RENOV});
		}
		
		if (null == fproxRenovacion) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_PROX_RENO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_PROX_RENO});
		}
		
		if (null == fcierre) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_CIERRE));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_CIERRE});
		}
		
		if (null == tipo ||  tipo.isEmpty()) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_TIPO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_TIPO});
		}
		
		if (null == criterioFecha || criterioFecha.isEmpty()) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CRITERIO_FEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CRITERIO_FEC});
		}
	}

	/**
	 * Función encargada de validar los parametros de entrada de la función mg00xAlfam.
	 * 
	 * @param fantRenovacion Identifica la fecha de efecto o renovación mensual anterior
	 * @param fproxRenovacion Identifica la fecha de la próxima renovación mensual.
	 * @param fcierre Identifica la fecha de cierre
	 * @param tipoCalculo Tipo del cálculo: PRORRATA
	 * @param criterioFecha Identifica el criterio de fechas para realizar el cálculo
	 */
	public static void validarCriteriosFuncionMg00xAlfam(final Timestamp fantRenovacion, final Timestamp fproxRenovacion,
			final Timestamp fcierre, final String tipoCalculo, final String criterioFecha) {
		
		if (null == fantRenovacion) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_ANT_RENOV));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_ANT_RENOV});
		}
		
		if (null == fproxRenovacion) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_PROX_RENO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_PROX_RENO});
		}
		
		if (null == fcierre) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_CIERRE));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_CIERRE});
		}
		
		if (null == tipoCalculo || tipoCalculo.isEmpty()) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_TIPO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_TIPO});
		} else if (ConstantsFunciones.CTE_CAL_FORFAIT.equals(tipoCalculo)) {
			// Si "FORFAIT" devolver error 003
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA3(tipoCalculo, ConstantsFunciones.CTE_TIPO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A3, new String[]{ConstantsFunciones.CTE_TIPO, tipoCalculo});
		}
		
		if (null == criterioFecha || criterioFecha.isEmpty()) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CRITERIO_FEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CRITERIO_FEC});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función PUCCAPcol
	 * @param pu Aportación única del ajuste o de la suscripción.
	 * @param n Duración real del ajuste o suscripción.
	 * @param i1bti Primer Interés técnico según Bases Técnicas Iniciales.
	 * @param i2bti Segundo Interés técnico según Bases Técnicas Iniciales.
	 * @param nr Nº. de renovaciones transcurridas desde la fecha de alta del ajuste o suscripción hasta TC+y
	 * @param nrm Nº. de renovaciones transcurridas desde la fecha de alta el ajuste hasta la fecha de fin de casamiento (M), dicho día incluido.
	 * @param m Número de años en que aplicamos un primer interés técnico.
	 * @param beta1 Variable Beta1.
	 * @param beta2 Variable Beta2.
	 * @param fcalc Fecha de Cálculo.
	 * @param fFinTramo1 Fecha Fin Tramo1.
	 */
	public static void validarParamEntradaFuncionPuccapcol(final BigDecimal pu, final BigDecimal n, final BigDecimal i1bti, final BigDecimal i2bti, final Integer nr,
			final Integer nrm, final BigDecimal m, final BigDecimal beta1, final BigDecimal beta2, final Timestamp fcalc, final Timestamp fFinTramo1){
		if (null == pu){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PU));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PU});
		}
		
		if (null == n){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == i1bti){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_I1_BTI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_I1_BTI});
		}
		
		if (null == i2bti){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_I2_BTI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_I2_BTI});
		}
		
		if (null == nr){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_NR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_NR});
		}
		
		if (null == nrm){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_NRM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_NRM});
		}
		
		if (null == m){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == beta1){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA1});
		}
		
		if (null == beta2){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA2});
		}
		
		if (null == fcalc){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FCAL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FCAL});
		}
		
		if (null == fFinTramo1){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_FIN_TRAMO1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_FIN_TRAMO1});
		}
	}

	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función OBFUTADORC
	 * @param fcal Fecha de cálculo.
	 * @param fecIniSusc Fecha de inicio de suscripción.
	 * @param i1 Primer Interés técnico.
	 * @param i2 Segundo Interés técnico.
	 * @param m Número de años en que aplicamos un primer interés técnico desde fecha suscripción.
	 * @param tcYResultado de la función naños() en base al criterio establecido entre la fecha de efecto de la suscripción y la fecha de cálculo.
	 * @param criterFec Criterio de Fechas para el cálculo.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param x Edad del asegurado a Fecha de suscripción..
	 * @param n Duración real del ajuste o suscripción.
	 * @param gic Gastos de gestión interna sobre Capital.
	 * @param gastGi
	 */
	public static void validarParamEntradaFuncionObfutadorc(final Timestamp fcal, final Timestamp fecIniSusc, final BigDecimal i1, 
			final BigDecimal i2, final BigDecimal m, final BigDecimal tcY, final String criterFec, final List<BigDecimal> valoresTabMort, 
			final BigDecimal w, final BigDecimal x, final BigDecimal n, final BigDecimal gic, final BigDecimal gastGi){
		
		if (null == fcal){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FCAL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FCAL});
		}
		
		if (null == fecIniSusc) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_INI_SUSC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_INI_SUSC});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == i2) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
		
		if (null == m){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == tcY) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TCY));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TCY});
		}
		
		if (null == criterFec || criterFec.isEmpty()) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CRIT_FEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CRIT_FEC});
		}
		
		if (null == valoresTabMort || valoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == w) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
		
		if (null == x) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == n){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == gic) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GIC});
		}
		
		if (null == gastGi) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GASTGI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GASTGI});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Obfutadorcpp301
	 * @param x Edad a fecha de cálculo.
	 * @param tc Anualidades completas transcurridas desde la fecha de efecto hasta la fecha de cálculo.
	 * @param alfa Fracción de mes transcurrida desde efecto mensual anterior hasta la fecha de cierre.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param n Duración del seguro en años.
	 * @param i1 Interés del primer tramo.
	 * @param gic Gastos de gestión interna sobre Capital.
	 */
	public static void validarParamEntradaFuncionObfutadorcpp301(final Integer x, final Integer tc, final BigDecimal alfa, final List<BigDecimal> valoresTabMort,
			final Integer w, final Integer n, final BigDecimal i1, final BigDecimal gic){
		if (null == x) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == tc) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TC});
		}
		
		if (null == alfa) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_ALFA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_ALFA});
		}
		
		if (null == valoresTabMort || valoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == w) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
		
		if (null == n) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == gic) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GIC});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Vidapu
	 * @param x Edad a fecha de cálculo.
	 * @param t Resultado de la función naños() en base al criterio establecido.
	 * @param beta Fracción de año pendiente entre el momento del cálculo y la siguiente anualidad del seguro.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param n Duración del seguro en años.
	 * @param i1 Interés del primer tramo.
	 * @param gic Gastos de gestión interna sobre Capital.
	 */
	public static void validarParamEntradaFuncionVidapu(final Integer x, final Integer t, final BigDecimal beta, final List<BigDecimal> valoresTabMort,
			final Integer w, final Integer n, final BigDecimal i1, final BigDecimal gic){
		
		if (null == x) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == t) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == beta) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA1});
		}
		
		if (null == valoresTabMort || valoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == w) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
		
		if (null == n) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == gic) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GIC});
		}
		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Obfutadorpap301
	 * @param x Edad a fecha de cálculo.
	 * @param t
	 * @param n Duración del seguro en años.
	 * @param fut Variable de Apoyo FUT.
	 * @param prp Porcentaje de revalorización.
	 * @param ifal Variable de Apoyo IFAL.
	 * @param i1 Interés del primer tramo.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param alfa Fracción de año pendiente entre el momento del cálculo y la siguiente anualidad del seguro.
	 */
	public static void validarParamEntradaFuncionObfutadorpap301(final Integer x, final Integer t, final Integer n, final BigDecimal fut, final BigDecimal prp,
			final BigDecimal ifal, final BigDecimal i1, final List<BigDecimal> valoresTabMort, final Integer w, final BigDecimal alfa){
		if (null == x) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == t) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == n) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == fut) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FUT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FUT});
		}
		
		if (null == prp) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PRP});
		}

		if (null == ifal) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_IFAL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_IFAL});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == valoresTabMort || valoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == w) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
		
		if (null == alfa) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_ALFA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_ALFA});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Obfutadorpa301 Parte1
	 * @param x Edad a fecha de cálculo.
	 * @param t
	 * @param n Duración del seguro en años.
	 * @param fut Variable de Apoyo FUT.
	 * @param ifal Variable de Apoyo IFAL.
	 * @param i1 Interés del primer tramo.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 */
	public static void validarParamEntradaFuncionObfutadorpa301Parte1(final Integer x, final Integer t, final Integer n, final BigDecimal fut, final BigDecimal ifal,
			final BigDecimal i1, final List<BigDecimal> valoresTabMort){

		if (null == x) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == t) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == n) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == fut) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FUT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FUT});
		}

		if (null == ifal) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_IFAL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_IFAL});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == valoresTabMort || valoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Obfutadorpa301 Parte2
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param alfa Fracción de año pendiente entre el momento del cálculo y la siguiente anualidad del seguro.
	 * @param tc0 Anualidad en curso.
	 * @param pNAtc Prima Tarada.
	 * @param pprUmic Primas Satisfechas en la anualidad T.
	 * @param fantani Fecha Aniversario Anterior.
	 * @param feccierre Fecha de cierre.
	 * @param fecJ Fecha del periodo a calcular.
	 * @param cformapago Código forma de pago de la prima.
	 */
	public static void validarParamEntradaFuncionObfutadorpa301Parte2(final Integer w, final BigDecimal alfa, final Integer tc0,
			final BigDecimal pNAtc, final BigDecimal pprUmic, final String cformapago){

		if (null == w) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
		
		if (null == alfa) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_ALFA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_ALFA});
		}
		
		if (null == tc0) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TC0));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TC0});
		}
		
		if (null == pNAtc) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PNA0));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PNA0});
		}
		
		if (null == pprUmic) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PRP});
		}
		
		if (null == cformapago) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CFORMAPAGO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CFORMAPAGO});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Obfutadorpa301 Parte3
	 * @param tcm Meses completos transcurridos desde fecha de efecto a fecha de cálculo(fecha de cierre).
	 * @param ttm Número de meses transcurridos desde el efecto a la fecha de vencimiento.
	 * @param ppcapUmic Primas Periodicas Capitalizadas hasta la anualidad en curso.
	 * @param pas Variable de Apoyo PAS.
	 * @param prp Porcentaje de revalorización de Primas.
	 * @param beta Meses transcurridos desde la fecha de cálculo (Fecha de cierre)  hasta la fecha de proyección.
	 */
	public static void validarParamEntradaFuncionObfutadorpa301Parte3(final Integer tcm, final Integer ttm, 
			final BigDecimal ppcapUmic, final BigDecimal pas, final BigDecimal prp, final Integer beta){
		
		if (null == tcm) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TCM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TCM});
		}
		
		if (null == ttm) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TTM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TTM});
		}
		
		if (null == ppcapUmic) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PPCAP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PPCAP});
		}
		
		if (null == pas) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PAS));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PAS});
		}
		
		if (null == prp) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PRP});
		}
		
		if (null == beta) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA1});
		}

	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Puccap
	 * @param tc Anualidad de cálculo.
	 * @param tcm Meses completos transcurridos desde fecha de efecto a fecha de cálculo(fecha de cierre).
	 * @param beta Meses transcurridos desde la fecha de cálculo (Fecha de cierre)  hasta la fecha de proyección.
	 * @param tc0 Anualidad en curso (fecha cálculo).
	 * @param ifal Variable de Apoyo IFAL.
	 * @param ppcap Primas Periodicas Capitalizadas hasta la anualidad en curso.
	 */
	public static void validarParamEntradaFuncionPuccap(final Integer tc, final Integer tcm, final Integer beta, final Integer tc0, 
			final BigDecimal ifal, final BigDecimal ppcap){
		
		if (null == tc) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TC});
		}
		
		if (null == tcm) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TCM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TCM});
		}
		
		if (null == beta) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA1});
		}
		
		if (null == tc0) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TC0));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TC0});
		}
		
		if (null == ifal) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_IFAL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_IFAL});
		}
		
		if (null == ppcap) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PPCAP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PPCAP});
		}
	}
	
	/**
	 *  Función encargada de realizar la validación de obligatoriedad para la función ViveDifer.
	 * @param x Edad actuarial a fecha de efecto.
	 * @param t Anualidad del cálculo.
	 * @param i Tipo de interés.
	 * @param edadCob Edad futura, generalmente la edad de jubilación.
	 * @param lstValoresTabMort Valores de la tabla de mortalidad.
	 */
	public static void validarParamEntradaFuncionViveDifer(final BigDecimal x, final Integer t, final BigDecimal i, final BigDecimal edadCob, 
			final List<BigDecimal> lstValoresTabMort){
		
		if (null == x) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == t) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == i) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == edadCob) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_EDAD_COB));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_EDAD_COB});
		}
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función vpret
	 * @param j Corresponde al período transcurrido desde  la fecha alta ajuste o suscripción  hasta la fecha “j” 
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer Interés técnico
	 * @param i2 Segundo Interés técnico
	 * @param t Anualidad del cálculo
	 */
	public static void validarParamEntradaFuncionVpret(final Integer j, final BigDecimal m, final BigDecimal i1, final BigDecimal i2, final Integer t){
		
		if (null == j){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_J});
		}
		
		if (null == m){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == i2) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
		
		if (null == t) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
	}
	
	
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para las funciones ärx+t y a(f)rx+t Parte 1
	 * @param j Corresponde al período transcurrido desde  la fecha alta ajuste o suscripción  hasta la fecha “j”
	 * @param difer Diferimiento de la renta
	 * @param x Edad a fecha de cálculo
	 * @param w Límite de la tabla de mortalidad
	 * @param ppr Porcentaje revalorización de la renta
	 * @param t Anualidad del cálculo
	 */
	public static void validarParamEntradaFuncionAarxtyAfrxtParte1 (final Integer difer, final Integer x, 
			final Integer w, final BigDecimal ppr, final Integer t){
		
		if (null == difer){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_DIFER));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_DIFER});
		}
		
		if (null == x){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == w){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
		
		if (null == ppr){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PPR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PPR});
		}
		
		if (null == t) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función ärx+t Parte 2
	 * @param lstValoresTabMort Valores de la tabla de mortalidad
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer Interés técnico
	 * @param i2 Segundo Interés técnico
	 */
	public static void validarParamEntradaFuncionAarxtParte2(final List<BigDecimal> lstValoresTabMort, final BigDecimal m, 
			final BigDecimal i1, final BigDecimal i2){
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == m){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == i2) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función a(f)rx+t Parte 2
	 * @param lstValoresTabMort Valores de la tabla de mortalidad
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer Interés técnico
	 * @param i2 Segundo Interés técnico
	 * @param f Forma de pago de la renta
	 */
	public static void validarParamEntradaFuncionAfrxtParte2 (final List<BigDecimal> lstValoresTabMort, final BigDecimal m, final BigDecimal i1, 
			final BigDecimal i2, final Integer f){
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == m){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == i2) {
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
		
		if (null == f){
			if (ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARF));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARF});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad de la función vpost
	 * @param j Corresponde al período transcurrido desde la fecha alta ajuste o suscripcion hasta la fecha j
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer interés técnico
	 * @param i2 Segundo interés técnico
	 * @param t Anualidad del cálculo
	 */
	public static void validarParamEntradaFuncionVpost (final Integer j, final BigDecimal m, final BigDecimal i1,
			final BigDecimal i2, final Integer t) {
		
		if (null == j) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_J});
		}
		
		if (null == m) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == i1) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == i2) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
		
		if (null == t) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad de la función vpost
	 * @param difer Diferimiento de la renta
	 * @param x Edad a fecha de cálculo
	 * @param w Límite de la tabla de mortalidad
	 * @param ppr Porcentaje revalorización de la renta
	 * @param T Anualidad de cálculo
	 * @param valoresTabMort Valores de la tabla de mortalidad
	 * @param M Número de años en que aplicamos un primer intéres técnico
	 * @param I1 Primer interés técnico
	 * @param I2 Segundo interés técnico
	 */
	public static void validarParamEntradaFuncionArxt (final Integer difer, final Integer x, final Integer w, final BigDecimal ppr,
			final Integer T, final List<BigDecimal> valoresTabMort, BigDecimal M, BigDecimal I1, BigDecimal I2 ) {
		
		if (null == difer) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_DIFER));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_DIFER});
		}
		
		if (null == x) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == w) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
		if (null == ppr) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PPR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PPR});
		}
		if (null == T) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		if (null == valoresTabMort) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		if (null == M) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		if (null == I1) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		if (null == I2) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}	
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad de la función agtr
	 * @param difer Diferimiento de la renta
	 * @param x Edad a fecha de cálculo
	 * @param w Límite de la tabla de mortalidad
	 * @param ppr Porcentaje revalorización de la renta
	 * @param t Anualidad del cálculo
	 * @param valoresTabMort Valores de la tabla de mortalidad
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer interés técnico
	 * @param i2 Segundo interés técnico
	 * @param f Forma de pago de la renta
	 * @param agp Número de periodos garantizados, en años completos redondeado por exceso
	 */
	public static void validarParamEntradaFuncionAgtr (final Integer difer, final Integer x, final Integer w, final BigDecimal ppr,
			final Integer t, final List<BigDecimal> valoresTabMort, BigDecimal m, BigDecimal i1, BigDecimal i2, final Integer f,
			final Integer agp) {
		
		if (null == difer) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_DIFER));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_DIFER});
		}
		
		if (null == x) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == w) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
		if (null == ppr) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PPR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PPR});
		}
		if (null == t) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		if (null == valoresTabMort) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		if (null == m) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		if (null == i1) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		if (null == i2) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}	
		if (null == f) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARF));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARF});
		}
		if (null == agp) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_AGP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_AGP});
		}		
		
	}
	
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad de las funciones: ArxDiferAGP y AarxDiferAGP
	 * @param difer Diferimiento de la renta
	 * @param x Edad a fecha de cálculo
	 * @param w Límite de la tabla de mortalidad
	 * @param ppr Porcentaje revalorización de la renta
	 * @param ag Número de periodos garantizados, en años completos
	 * @param valoresTabMort Valores de la tabla de mortalidad
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer interés técnico
	 * @param i2 Segundo interés técnico
	 * @param agp Número de periodos garantizados, en años completos redondeados por exceso
	 * @param varLimSuperior 
	 */
	public static void validarParamEntradaFuncionArxDiferAGPyAarxDiferAGP (final Integer difer, final Integer x, final Integer w, final BigDecimal ppr, final Integer t,
			final List<BigDecimal> valoresTabMort, final BigDecimal m, final BigDecimal i1, final BigDecimal i2, final Integer agp, Integer varLimSuperior) {
		
		if (null == difer) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_DIFER));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_DIFER});
		}
		
		if (null == x) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == w) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
		if (null == ppr) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PPR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PPR});
		}
		if (null == t) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		if (null == valoresTabMort) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		if (null == m) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		if (null == i1) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		if (null == i2) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}	
		if (null == agp) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_AGP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_AGP});
		}	
		if (null == varLimSuperior) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_LIMSUPERIOR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_LIMSUPERIOR});
		}
	}
	
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad de la función afrxDiferAGP
	 * @param difer Diferimiento de la renta
	 * @param x Edad a fecha de cálculo
	 * @param w Límite de la tabla de mortalidad
	 * @param ppr Porcentaje revalorización de la renta
	 * @param t Anualidad del cálculo
	 * @param valoresTabMort Valores de la tabla de mortalidad
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer interés técnico
	 * @param i2 Segundo interés técnico
	 * @param f Forma de pago de la renta
	 * @param ag Número de periodos garantizados, en años completos
	 * @param agp Número de periodos garantizados, en años completos redondeados por exceso
	 * @param varLimSuperior 
	 */
	public static void validarParamEntradaFuncionAfrxDiferAGP (final Integer difer, final Integer x, final Integer w, final BigDecimal ppr, final Integer t,
			final List<BigDecimal> valoresTabMort, final BigDecimal m, final BigDecimal i1, final BigDecimal i2, final Integer f,
			final Integer ag, final Integer agp, final Integer varLimSuperior) {
		
		if (null == difer) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_DIFER));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_DIFER});
		}
		
		if (null == x) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == w) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
		if (null == ppr) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PPR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PPR});
		}
		if (null == t) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		if (null == valoresTabMort) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		if (null == m) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		if (null == i1) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		if (null == i2) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}	
		if (null == f) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARF));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARF});
		}
		if (null == ag) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_AG));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_AG});
		}
		if (null == agp) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_AGP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_AGP});
		}	
		if (null == varLimSuperior) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_LIMSUPERIOR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_LIMSUPERIOR});
		}	
	}
	
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad de las funciones Arxyt y Aarxyt
	 * @param difer Diferimiento de la renta
	 * @param x Edad primera cabeza a fecha de cálculo
	 * @param wx Límite de la tabla de mortalidad
	 * @param y Edad segunda cabeza a fecha de cálculo
	 * @param wy Límite de la tabla de mortalidad segunda cabeza
	 * @param ppr Porcentaje revalorización de la renta
	 * @param t Anualidad del cálculo
	 * @param valoresTabMortX Valores de la tabla de mortalidad primera cabeza
	 * @param valoresTabMortY Valores de la tabla de mortalidad segunda cabeza
	 * @param Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer interés técnico
	 * @param i2 Segundo interés técnico
	 */
	public static void validarParamEntradaFuncionArxytAarxyt (final Integer difer, final Integer x, final Integer wx, final Integer y,
			final Integer wy, final BigDecimal ppr, final Integer t, final List<BigDecimal> valoresTabMortX, final List<BigDecimal> valoresTabMortY,
			final BigDecimal m, final BigDecimal i1, final BigDecimal i2) {
		
		if (null == difer) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_DIFER));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_DIFER});
		}
		
		if (null == x) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == wx) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_WX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_WX});
		}
		if (null == y) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_Y));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_Y});
		}
		if (null == wy) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_WY));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_WY});
		}		
		if (null == ppr) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PPR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PPR});
		}
		if (null == t) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		if (null == valoresTabMortX) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		if (null == valoresTabMortY) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		if (null == m) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		if (null == i1) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		if (null == i2) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriadad de la función afrxyt
	 * @param difer Diferimiento de la renta
	 * @param x Edad primera cabeza a fecha de cálculo
	 * @param wx Límite de la tabla de mortalidad primera cabeza
	 * @param y Edad segunda cabeza a fecha de cálculo
	 * @param wy Límite de la tabla de mortalidad segunda cabeza
	 * @param ppr Porcentaje revalorización de la renta
	 * @param t Anualidad del cálculo
	 * @param valoresTabMortX Valores de la tabla de mortalidad primera cabeza
	 * @param valoresTabMortY Valores de la tabla de mortalidad segunda cabeza
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer interés técnico
	 * @param i2 Segundo interés técnico
	 * @param f Forma de pago de la renta
	 */
	public static void validarParamEntradaFuncionAfrxyt (final Integer difer, final Integer x, final Integer wx, final Integer y, final Integer wy,
			final BigDecimal ppr, final Integer t, final List<BigDecimal> valoresTabMortX, final List<BigDecimal> valoresTabMortY,
			final BigDecimal m, final BigDecimal i1, final BigDecimal i2, final Integer f) {
		
		validarParamEntradaFuncionArxytAarxyt(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2);
		
		if (null == f) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARF));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARF});
		}
		
		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriadad de las funciones arxyDiferAGP y aarxyDiferAGP
	 * @param difer Diferimiento de la renta.
	 * @param x Edad  primera cabeza a fecha de cálculo
	 * @param wx Límite de la tabla de mortalidad primera cabeza
	 * @param y Edad segunda cabeza a fecha de cálculo
	 * @param wy Límite de la tabla de mortalidad segunda cabeza
	 * @param ppr Porcentaje revalorización de la renta
	 * @param t Anualidad del cálculo
	 * @param valoresTabMortX Valores de la tabla de mortalidad primera cabeza
	 * @param valoresTabMortY Valores de la tabla de mortalidad segunda cabeza
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer Interés técnico
	 * @param i2 Segundo Interés técnico
	 * @param agp Número de periodos garantizados, en años completos redondeado por exceso
	 */
	public static void validarParamEntradaFuncionesArxyDiferAGP (final Integer difer, final Integer x, final Integer wx, final Integer y, final Integer wy,
			final BigDecimal ppr, final Integer t, final List<BigDecimal> valoresTabMortX, final List<BigDecimal> valoresTabMortY,
			final BigDecimal m, final BigDecimal i1, final BigDecimal i2, final Integer agp) {
		
		if (null == difer) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_DIFER));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_DIFER});
		}
		
		if (null == x) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == wx) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_WX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_WX});
		}
		if (null == y) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_Y));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_Y});
		}
		if (null == wy) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_WY));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_WY});
		}		
		if (null == ppr) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PPR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PPR});
		}
		if (null == t) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		if (null == valoresTabMortX) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		if (null == valoresTabMortY) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		if (null == m) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		if (null == i1) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		if (null == i2) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
		if (null == agp) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_AGP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_AGP});
		}
		
		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriadad de la función afrxyDiferAGP
	 * @param difer Diferimiento de la renta.
	 * @param x Edad  primera cabeza a fecha de cálculo
	 * @param wx Límite de la tabla de mortalidad primera cabeza
	 * @param y Edad segunda cabeza a fecha de cálculo
	 * @param wy Límite de la tabla de mortalidad segunda cabeza
	 * @param ppr Porcentaje revalorización de la renta
	 * @param t Anualidad del cálculo
	 * @param valoresTabMortX Valores de la tabla de mortalidad primera cabeza
	 * @param valoresTabMortY Valores de la tabla de mortalidad segunda cabeza
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer Interés técnico
	 * @param i2 Segundo Interés técnico
	 * @param agp Número de periodos garantizados, en años completos redondeado por exceso
	 * @param f Forma de pago de la renta
	 */
	public static void validarParamEntradaFuncionAfrxyDiferAGP (final Integer difer, final Integer x, final Integer wx, final Integer y, final Integer wy,
			final BigDecimal ppr, final Integer t, final List<BigDecimal> valoresTabMortX, final List<BigDecimal> valoresTabMortY,
			final BigDecimal m, final BigDecimal i1, final BigDecimal i2, final Integer agp, final Integer f){
		
		validarParamEntradaFuncionesArxyDiferAGP(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, agp);
		
		if (null == f) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARF));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARF});
		}
	}
	
	
	/**
	 * Función encargada de realizar la validación de obligatoriadad de la función AfrxytAGPt
	 * @param difer Diferimiento de la renta
	 * @param x Edad primera cabeza a fecha de cálculo
	 * @param wx Límite de la tabla de mortalidad primera cabeza
	 * @param y Edad segunda cabeza a fecha de cálculo
	 * @param wy Límite de la tabla de mortalidad segunda cabeza
	 * @param ppr Porcentaje revalorización de la renta
	 * @param t Anualidad del cálculo
	 * @param valoresTabMortX Valores de la tabla de mortalidad primera cabeza
	 * @param valoresTabMortY Valores de la tabla de mortalidad segunda cabeza
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer interés técnico
	 * @param i2 Segundo interés técnico
	 * @param f Forma de pago de la renta
	 * @param agp Número de periodos garantizados, en años completos redondeado por exceso
	 */
	public static void validarParamEntradaFuncionAfrxytAGPt (final Integer difer, final Integer x, final Integer wx, final Integer y, final Integer wy,
			final BigDecimal ppr, final Integer t, final List<BigDecimal> valoresTabMortX, final List<BigDecimal> valoresTabMortY,
			final BigDecimal m, final BigDecimal i1, final BigDecimal i2, final Integer f, final Integer agp) {
		
		if (null == difer) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_DIFER));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_DIFER});
		}
		
		if (null == x) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == wx) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_WX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_WX});
		}
		if (null == y) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_Y));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_Y});
		}
		if (null == wy) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_WY));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_WY});
		}		
		if (null == ppr) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PPR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PPR});
		}
		if (null == t) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		if (null == valoresTabMortX) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		if (null == valoresTabMortY) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		if (null == m) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		if (null == i1) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		if (null == i2) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
		if (null == f) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARF));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARF});
		}
		if (null == agp) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_AGP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_AGP});
		}	
		
	}
	
    /**
     * Función encargada de realizar la validación de obligatoriedad de la función ArxytAPGt
     * @param difer Diferimiento de la renta
     * @param x Edad primera cabeza a fecha de cálculo
     * @param wx Límite de la tabla de mortalidad
     * @param y Edad segunda cabeza a fecha de cálculo
     * @param wy Límite de la tabla de mortalidad segunda cabeza
     * @param ppr Porcentaje revalorización de la renta 
     * @param t Anualidad del cálculo
     * @param valoresTabMortX Valores de la tabla de mortalidad primera cabeza
     * @param valoresTabMortY Valores de la tabla de mortaliad segunda cabeza
     * @param m Número de años en que aplicamos un primer interés técnico
     * @param i1 Primer interés técnico
     * @param i2 Segundo interés técnico
     * @param agp Número de periodos garantizados, en años completos redondeados por exceso
     */
	public static void validarParamEntradaFuncionArxytAGPtAarxytAGPt (final Integer difer, final Integer x, final Integer wx, final Integer y,
			final Integer wy, final BigDecimal ppr, final Integer t, final List<BigDecimal> valoresTabMortX, final List<BigDecimal> valoresTabMortY,
			final BigDecimal m, final BigDecimal i1, final BigDecimal i2, final Integer agp) {
		if (null == difer) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_DIFER));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_DIFER});
		}
		
		if (null == x) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == wx) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_WX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_WX});
		}
		if (null == y) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_Y));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_Y});
		}
		if (null == wy) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_WY));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_WY});
		}		
		if (null == ppr) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PPR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PPR});
		}
		if (null == t) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		if (null == valoresTabMortX) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		if (null == valoresTabMortY) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		if (null == m) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		if (null == i1) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		if (null == i2) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
		if (null == agp) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_AGP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_AGP});
		}
		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función VIDA
	 * @param xc Edad a fecha de cálculo
	 * @param xj Edad a fecha del periodo
	 * @param w Límite de la tabla de mortalidad
	 * @param valoresTabMortX Valores de la tabla de mortalidad
	 */
	public static void validarParamEntradaFuncionVida (final BigDecimal xc, final BigDecimal xj, final Integer w, List<BigDecimal> valoresTabMortX) {
		if (null == xc) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_XC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_XC});
		}
		if (null == xj) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_XJ));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_XJ});
		}
		if (null == w) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
		if (null == valoresTabMortX) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_AGP});
		}
		
		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función VT
	 * @param durcierre Número de años existentes entre la fecha de cierre y la fecha de efecto de la póliza. Si es provisión por balance,
	 * 	                consideramos el cierre a las 24 horas
	 * @param actj Número de años existente entre la fecha de cierre y cada uno de los sucesivos vencimientos de las rentas C(j)
	 * @param durit1 Número de años que aplica el primer interés técnico
	 * @param i1 Primer interés técnico
	 * @param i2 Segundo interés técnico
	 */
	public static void validarParamEntradaFuncionVt (final BigDecimal durcierre, final BigDecimal actj, final Integer durit1,
			final BigDecimal i1, final BigDecimal i2) {
		if (null == durcierre) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DURCIERRE));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DURCIERRE});
		}
		if (null == actj) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_ACTJ));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_ACTJ});
		}
		if (null == durit1) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DURIT1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DURIT1});
		}
		if (null == i1) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		if (null == i2) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función VTR
	 * @param j Periodo de cálculo
	 * @param durcierre Número de años existentes entre la fecha de cierre y la fecha de efecto de la póliza. Si es provisión de balance,
	 * 					consieramos el cierre a las 24 horas
	 * @param actj Número de años existente entre la fecha de cierre y cada uno de los sucesivos vencimientos de las rentas C(j)
	 * @param actjant Número de años existente entre la fecha de cierre y el periodo anterior
	 * @param durit1 Número de años que aplica el primer interés técnico
	 * @param i1 Primer interés técnico
	 * @param i2 Segundo interés técnico
	 */
	public static void validarParamEntradaFuncionVtr (final Integer j, final BigDecimal durcierre, final BigDecimal actj, final BigDecimal actjant,
			final Integer durit1, final BigDecimal i1, final BigDecimal i2) {
		if (null == j) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_J});
		}
		if (null == durcierre) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DURCIERRE));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DURCIERRE});
		}
		if (null == actj) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_ACTJ));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_ACTJ});
		}
		if (null == actjant) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_ACTJANT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_ACTJANT});
		}
		if (null == durit1) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DURIT1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DURIT1});
		}
		if (null == i1) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		if (null == i2) {
			if(ValidacionesFuncionesVBX.LOG.isDebugEnabled()) {
				ValidacionesFuncionesVBX.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
	}
	
}
