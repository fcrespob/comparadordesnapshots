package es.mapfre.solvencia.formulacion.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;

public class ValidacionesFuncionesGastos {
	
	/** Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ValidacionesFuncionesGastos.class);
	
	/**
	 * Función encargada de validar la obligatoriedad de los parámetros de entrada de la función gastgivit
	 * 
	 * @param fcal Fecha de cálculo
	 * @param fecIniSusc Fecha de inicio de suscripción
	 * @param fecProxRenova Fecha de próxima renovación
	 * @param fecJ Fecha de devengo del periodo J
	 * @param partAnoNR Fracción de año que hay entre la renovación anterior NR y posterior (NR+1)
	 * @param varRY Fracción de año incompleto entre la anterior fecha de renovación y la fecha de cálculo fcal
	 * @param ren Período de tiempo transcurrido desde la fecha de alta del ajuste hasta la fecha de renovación siguiente al momento de cálculo
	 */
	public static void validarParamEntradaFuncionGastgivitParte1(
			final Timestamp fcal, final Timestamp fecIniSusc, final Timestamp fecProxRenova, final Timestamp fecJ,
			final BigDecimal partAnoNR, final BigDecimal varRY, final BigDecimal ren) {
		
		if (null == fcal) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FCAL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FCAL});
		}
		
		if (null == fecIniSusc) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_INI_SUSC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_INI_SUSC});
		}
		
		if (null == fecProxRenova) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_PROX_REV));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_PROX_REV});
		}
		
		if (null == fecJ) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_J});
		}
		
		if (null == partAnoNR) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PART_ANO_NR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PART_ANO_NR});
		}
		
		if (null == varRY) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_RY));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_RY});
		}
		
		if (null == ren) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_REN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_REN});
		}
	}
	
	/**
	 * Función encargada de validar la obligatoriedad de los parámetros de entrada de la función gastgivit
	 * 
	 * @param numi1 Primer Interés técnico
	 * @param numi2 Segundo Interés técnico
	 * @param numm Número de años en que aplicamos un primer interés técnico desde fecha suscripción
	 * @param varTCY Resultado de la función naños() en base al criterio establecido entre la fecha de efecto de la suscripción y la fecha de cálculo
	 * @param criterFec Criterio de Fechas para el cálculo
	 * @param lstValoresTabMort Valores de la tabla de mortalidad
	 * @param varw Edad Máxima de la tabla de mortalidad
	 */
	public static void validarParamEntradaFuncionGastgivitParte2(
			final BigDecimal numi1, final BigDecimal numi2, final BigDecimal numm,
			final BigDecimal varTCY, final String criterFec, final List<BigDecimal> lstValoresTabMort, final BigDecimal varw) {
		
		if (null == numi1) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NUMI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NUMI1});
		}
		
		if (null == numi2) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NUMI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NUMI2});
		}
		
		if (null == numm) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NUMM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NUMM});
		}
		
		if (null == varTCY) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TCY));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TCY});
		}
		
		if (null == criterFec || criterFec.isEmpty()) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CRIT_FEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CRIT_FEC});
		}
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == varw) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
	}
	
	/**
	 * Función encargada de validar la obligatoriedad de los parámetros de entrada de la función gastgivit
	 * 
	 * @param varx Edad del asegurado a Fecha de cálculo
	 */
	public static void validarParamEntradaFuncionGastgivitParte3(final BigDecimal varx) {
		
		if (null == varx) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función GASTGI (parte 1)
	 * @param fcalc Fecha de cálculo.
	 * @param fecIniSusc Fecha de inicio de suscripción.
	 * @param fecJ Fecha de devengo del periodo J.
	 * @param partAnoNR Fracción de año que hay entre la renovación anterior NR y posterior (NR+1).
	 * @param ry Fracción de año incompleto entre la anterior fecha de renovación y la fecha de cálculo fcal.
	 * @param ren Período de tiempo transcurrido desde la fecha de alta del ajuste hasta la fecha de renovación siguiente al momento de cálculo.
	 * @param i1 Primer Interés técnico.
	 * @param i2 Segundo Interés técnico.
	 * @param m Número de años en que aplicamos un primer interés técnico desde fecha suscripción.
	 */
	public static void validarParamEntradaFuncionGastgi1(final Timestamp fcalc, final Timestamp fecIniSusc, final Timestamp fecJ, final BigDecimal partAnoNR, final BigDecimal ry,
			final BigDecimal ren, final BigDecimal i1, final BigDecimal i2, final BigDecimal m){
		
		if (null == fcalc){
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FCAL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FCAL});
		}
		
		if (null == fecIniSusc) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_INI_SUSC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_INI_SUSC});
		}
		
		if (null == fecJ) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_J});
		}
		
		if (null == partAnoNR) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PART_ANO_NR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PART_ANO_NR});
		}
		
		if (null == ry) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_RY));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_RY});
		}
		
		if (null == ren) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_REN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_REN});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == i2) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
		
		if (null == m){
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función GASTGI (parte 2)
	 * @param tcy Resultado de la función naños() en base al criterio establecido entre la fecha de efecto de la suscripción y la fecha de cálculo.
	 * @param criterFec Criterio de Fechas para el cálculo.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param x Edad del asegurado a Fecha de suscripción.
	 * @param zc Edad del asegurado a Fecha de cálculo.
	 * @param nDap Número de años completos que quedan pendientes desde la siguiente renovación a la fecha de cálculo hasta el vencimiento.
	 * @param nDmv Fracción de año comprendida entre la fecha de renovación de la umic inmediata anterior a la fecha de vencimiento del certificado y dicha fecha de vencimiento.
	 */
	public static void validarParamEntradaFuncionGastgi2(final BigDecimal tcy, final String criterFec, final List<BigDecimal> valoresTabMort, final BigDecimal w, 
			final BigDecimal x, final BigDecimal zc, final Integer nDap, final BigDecimal nDmv){
		
		if (null == tcy) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TCY));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TCY});
		}
		
		if (null == criterFec || criterFec.isEmpty()) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CRIT_FEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CRIT_FEC});
		}
		
		if (null == valoresTabMort || valoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == w) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
		
		if (null == x) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == zc) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARZC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARZC});
		}
		
		if (null == nDap) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NDAP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NDAP});
		}
		
		if (null == nDmv) {
			if (ValidacionesFuncionesGastos.LOG.isDebugEnabled()) {
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NDMV));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NDMV});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Gasto
	 * @param renta
	 * @param Gi gastos sobre la renta.
	 * @param primaUnica Prima única Inicial.
	 * @param gipc gastos sobre la prima comercial.
	 * @param gastgivit
	 * @param gasgivitini
	 */
	public static void validarParamEntradaFuncionGasto(final BigDecimal renta, final BigDecimal gi, final BigDecimal primaUnica, final BigDecimal gipc,
			final BigDecimal gastgivit, final BigDecimal gasgivitini){
		if (null == renta){
			if(ValidacionesFuncionesGastos.LOG.isDebugEnabled()){
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_RENTA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_RENTA});	
		}
		
		if (null == gi){
			if(ValidacionesFuncionesGastos.LOG.isDebugEnabled()){
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GI});	
		}
		
		if (null == primaUnica){
			if(ValidacionesFuncionesGastos.LOG.isDebugEnabled()){
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PRIMA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PRIMA});	
		}
		
		if (null == gipc){
			if(ValidacionesFuncionesGastos.LOG.isDebugEnabled()){
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GIPC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GIPC});	
		}
		
		if (null == gastgivit){
			if(ValidacionesFuncionesGastos.LOG.isDebugEnabled()){
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GASGIVIT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GASGIVIT});	
		}
		
		if (null == gasgivitini){
			if(ValidacionesFuncionesGastos.LOG.isDebugEnabled()){
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GASGIVITINI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GASGIVITINI});	
		}
		
	}

	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Gasto
	 * @param t
	 * @param ttm
	 * @param diaVto
	 * @param ggim
	 * @param diaprima
	 * @param bxAnterior
	 */
	public static void validarParamEntradaFuncionGast (final Integer t, final Integer ttm, final Integer diaVto, final BigDecimal ggim, final Integer diaprima, final BigDecimal bxAnterior) {
		if (null == t){
			if(ValidacionesFuncionesGastos.LOG.isDebugEnabled()){
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});	
		}
		
		if (null == ttm){
			if(ValidacionesFuncionesGastos.LOG.isDebugEnabled()){
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TTM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TTM});	
		}
		
		if (null == diaVto){
			if(ValidacionesFuncionesGastos.LOG.isDebugEnabled()){
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DIAVTO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DIAVTO});	
		}
		
		if (null == ggim){
			if(ValidacionesFuncionesGastos.LOG.isDebugEnabled()){
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GGIM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GGIM});	
		}
		
		if (null == diaprima){
			if(ValidacionesFuncionesGastos.LOG.isDebugEnabled()){
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DIAPRIMA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DIAPRIMA});	
		}
		
		if (null == bxAnterior){
			if(ValidacionesFuncionesGastos.LOG.isDebugEnabled()){
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BXANTERIOR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BXANTERIOR});	
		}
	}
	
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Gasto
	 * @param t Periodo mensual de proyección
	 * @param ttm Número de meses transcurridos desde el efecto a la fecha de vencimiento
	 * @param diaVto Día de vencimiento
	 * @param ggim Gastos sobre la prima (mensualizado)
	 * @param diaprima Día de pago de la prima
	 * @param prima 
	 */
	public static void validarParamEntradaFuncionGastp (final Integer t, final Integer ttm, final Integer diaVto, final BigDecimal ggim, final Integer diaprima, final BigDecimal prima) {
		if (null == t){
			if(ValidacionesFuncionesGastos.LOG.isDebugEnabled()){
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});	
		}
		
		if (null == ttm){
			if(ValidacionesFuncionesGastos.LOG.isDebugEnabled()){
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TTM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TTM});	
		}
		
		if (null == diaVto){
			if(ValidacionesFuncionesGastos.LOG.isDebugEnabled()){
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DIAVTO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DIAVTO});	
		}
		
		if (null == ggim){
			if(ValidacionesFuncionesGastos.LOG.isDebugEnabled()){
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GGIM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GGIM});	
		}
		
		if (null == diaprima){
			if(ValidacionesFuncionesGastos.LOG.isDebugEnabled()){
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DIAPRIMA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DIAPRIMA});	
		}
		
		if (null == prima){
			if(ValidacionesFuncionesGastos.LOG.isDebugEnabled()){
				ValidacionesFuncionesGastos.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PRIMA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PRIMA});	
		}
	}
	
	
	
}
