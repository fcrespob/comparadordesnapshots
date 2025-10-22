package es.mapfre.solvencia.formulacion.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;

public class ValidacionesFuncionesPrimas {
	
	/** Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ValidacionesFuncionesPrimas.class);
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad de la primera parte de los parametros de la función Obadorprim
	 * 
	 * @param fcal Fecha de cálculo
	 * @param fecIniSusc Fecha de inicio de suscripción 
	 * @param fecAntRenova Fecha de anterior renovación 
	 * @param fecProxRenova Fecha de próxima renovación 
	 * @param fecIni Fecha de inicio de cobro de la renta
	 * @param fecJ Fecha de devengo del periodo J 
	 * @param varj Corresponde al período transcurrido desde  la fecha alta ajuste o suscripción  hasta la fecha “j”
	 */
	public static void validarParamentradaFuncionObadorprimParte1(final Timestamp fcal, final Timestamp fecIniSusc, final Timestamp fecAntRenova, final Timestamp fecProxRenova, final Timestamp fecIni, final Timestamp fecJ,
			final BigDecimal varj) {
		
		if (null == fcal) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FCAL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FCAL});
		}
		
		if (null == fecIniSusc) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_INI_SUSC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_INI_SUSC});
		}
		
		if (null == fecAntRenova) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_ANT_RENOV));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_ANT_RENOV});
		}
		
		if (null == fecProxRenova) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_PROX_REV));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_PROX_REV});
		}
		
		if (null == fecIni) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_INI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_INI});
		}
				
		if (null == fecJ) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_J});
		}
		
		if (null == varj) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_J});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad de la segunda parte de los parametros de la función Obadorprim
	 * 
	 * @param partAnoNR Fracción de año que hay entre la renovación anterior NR y posterior (NR+1).
	 * @param varRy Fracción de año incompleto entre la anterior fecha de renovación y la fecha de cálculo fcal.
	 * @param vari1 Primer Interés técnico 
	 * @param vari2 Segundo Interés técnico 
	 * @param varm Número de años en que aplicamos un primer interés técnico desde fecha suscripción
	 * @param varTc Anualidades completas transcurridas desde la fecha de efecto hasta la fecha de cálculo.
	 * @param criterFec Criterio de Fechas para el cálculo
	 */
	public static void validarParamentradaFuncionObadorprimParte2(final BigDecimal partAnoNR, final BigDecimal varRy, final BigDecimal vari1, final BigDecimal vari2, final BigDecimal varm, final Integer varTc, final String criterFec) {
		
		if (null == partAnoNR) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PART_ANO_NR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PART_ANO_NR});
		}
		
		if (null == varRy) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_RY));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_RY});
		}
		
		if (null == vari1) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == vari2) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
		
		if (null == varm) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == varTc) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TC});
		}
		
		if (null == criterFec || criterFec.isEmpty()) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CRIT_FEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CRIT_FEC});
		}
		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad de la tercera parte de los parametros de la función Obadorprim
	 * 
	 * @param lstValoresTabMort Valores de la tabla de mortalidad
	 * @param varw Edad Máxima de la tabla de mortalidad
	 * @param varx Edad del asegurado a Fecha de Efecto
	 * @param varzc Edad del asegurado a Fecha de cálculo
	 * @param vary Dato a nivel ajuste o suscripción que representa la fracción de año incompleto desde la fecha de alta del ajuste a la fecha de cálculo fcal
	 * @param difercol Diferimiento en cobrar la renta
	 */
	public static void validarParamentradaFuncionObadorprimParte3(final List<BigDecimal> lstValoresTabMort,
			final BigDecimal varw, final BigDecimal varx, final BigDecimal varzc, final BigDecimal vary, final BigDecimal difercol) {
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == varw) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
		
		if (null == varx) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == varzc) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARZC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARZC});
		}
		
		if (null == vary) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_Y));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_Y});
		}
		
		if (null == difercol) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DIFERCOL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DIFERCOL});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad de la cuarta parte de los parametros de la función Obadorprim
	 * 
	 * @param modBeta Variable de apoyo MODBETA
	 * @param beta1 Variable Beta1
	 * @param beta2 Variable Beta2
	 * @param varn
	 * @param i1Bti
	 * @param i2Bti
	 */
	public static void validarParamentradaFuncionObadorprimParte4(final String modBeta, final BigDecimal beta1, final BigDecimal beta2, final BigDecimal varn, final BigDecimal i1Bti, final BigDecimal i2Bti) {
		
		if (null == modBeta || modBeta.isEmpty()) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_MOD_BETA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_MOD_BETA});
		}
		
		if (null == beta1) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA1});
		}
		
		if (null == beta2) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA2});
		}
		
		if (null == varn) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == i1Bti) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_I1_BTI_MINUS));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_I1_BTI_MINUS});
		}
		
		if (null == i2Bti) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_I2_BTI_MINUS));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_I2_BTI_MINUS});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función OBFUTADORPRIM (parte 1)
	 * @param fcalc Fecha de cálculo.
	 * @param fecProxRenova Fecha de próxima renovación.
	 * @param frpdgact Fecha de Renovación posterior a la fecha de cierre.
	 * @param fecvto Fecha de vencimiento.
	 * @param fecJ Fecha de devengo del periodo J.
	 * @param j Corresponde al período transcurrido desde  la fecha alta ajuste o suscripción  hasta la fecha “j”.
	 * @param partAnoNR Fracción de año que hay entre la renovación anterior NR y posterior (NR+1).
	 * @param ry Fracción de año incompleto entre la anterior fecha de renovación y la fecha de cálculo fcal.
	 * @param i1 Primer Interés técnico.
	 * @param i2 Segundo Interés técnico.
	 * @param m Número de años en que aplicamos un primer interés técnico desde fecha suscripción.
	 * @param n Duración real del ajuste o suscripción.
	 */
	public static void validarParamEntradaFuncionObfutadorprim1(final Timestamp fcalc, final Timestamp fecProxRenova,
			final Timestamp frpdgact, final Timestamp fecvto, final Timestamp fecJ, final BigDecimal partAnoNR, final BigDecimal ry,
			final BigDecimal i1, final BigDecimal i2, final BigDecimal m, final BigDecimal n){
		
		if (null == fcalc){
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FCAL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FCAL});
		}
		
		if (null == fecProxRenova) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_PROX_REV));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_PROX_REV});
		}
		
		if (null == frpdgact) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_REN_PSGACT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_REN_PSGACT});
		}
		
		if (null == fecvto){
			if(ValidacionesFuncionesPrimas.LOG.isDebugEnabled()){
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_VTO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_VTO});
		}
		
		if (null == fecJ) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_J});
		}
		
		
		if (null == partAnoNR) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PART_ANO_NR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PART_ANO_NR});
		}
		
		if (null == ry) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_RY));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_RY});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == i2) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
		
		if (null == m){
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == n){
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
	}
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función OBFUTADORPRIM (parte 2)
	 * * @param tcy Distancia en Años entre la fecha de suscricpión y la fecha de cálculo.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param x Edad del asegurado a Fecha de Efecto.
	 * @param zc Edad del asegurado a Fecha de cálculo.
	 * @param nDaP Número de años completos que quedan pendientes desde la siguiente renovación a la fecha de cálculo hasta el vencimiento.
	 * @param nDmv Fracción de año comprendida entre la fecha de renovación de la umic inmediata anterior a la fecha de vencimiento del certificado y dicha fecha de vencimiento.
	 * @param modBeta Variable de apoyo MODBETA.
	 * @param beta1 Variable Beta1.
	 * @param beta2 Variable Beta2.
	 * @param i1Bti Primer Interés técnico según Bases Técnicas Iniciales.
	 * @param i2Bti Segundo Interés técnico según Bases Técnicas Iniciales.
	 * @param ren Período de tiempo transcurrido desde la fecha de alta del ajuste hasta la fecha de renovación siguiente al momento de cálculo.
	 */
	public static void validarParamEntradaFuncionObfutadorprim2(final BigDecimal tcy, final List<BigDecimal> valoresTabMort,
			final BigDecimal x, final BigDecimal zc, final Integer nDaP, final BigDecimal nDmv, final String modBeta,
			final BigDecimal beta1, final BigDecimal beta2, final BigDecimal i1Bti, final BigDecimal i2Bti, final BigDecimal ren){
		
		if (null == tcy) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TCY));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TCY});
		}
		
		if (null == valoresTabMort || valoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		
		if (null == x) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == zc){
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARZC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARZC});
		}
		
		if (null == nDaP) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NDAP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NDAP});
		}
		
		if (null == nDmv) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NDMV));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NDMV});
		}
		
		if (null == modBeta || modBeta.isEmpty()) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_MOD_BETA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_MOD_BETA});
		}
		
		if (null == beta1) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA1});
		}
		
		if (null == beta2) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA2});
		}
		
		if (null == i1Bti) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_I1_BTI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_I1_BTI});
		}
		
		if (null == i2Bti) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_I2_BTI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_I2_BTI});
		}
		
		if (null == ren) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_REN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_REN});
		}
		
	}
	
	/**
	 * Función que valida la obligatoriedad de los parámetros de entrada de la función recargofro
	 * 
	 * @param numPagos Nº de Pagos 
	 * @param interes Interés Técnico
	 */
	public static void validarParamEntradaFuncionRecargoFro(final BigDecimal numPagos, final BigDecimal interes) {
		
		if (null == numPagos) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NUM_PAGOS));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NUM_PAGOS});
		}
		
		if (null == interes) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_INTERES));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_INTERES});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Sumaf
	 * @param t Primer térnimo de la progresión.
	 * @param j Término j ésimo de la progresión.
	 * @param n Último término de la progresión.
	 * @param prp Porcentaje de  revalorización.
	 */
	public static void validarParamEntradaFuncionSumaf(final Integer t, final Integer j, final Integer n, final BigDecimal prp){
		
		if (null == t) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == j){
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_J});
		}
		
		if (null == n) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == prp) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PRP});
		}
		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Fobligado
	 * @param x Edad a fecha de cálculo.
	 * @param t
	 * @param alfa Fracción de año pendiente entre el momento del cálculo y la siguiente anualidad del seguro.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param n Duración del seguro en años.
	 * @param i1 Interés del primer tramo.
	 * @param gepc Gastos de gestión externa.
	 * @param prp Porcentaje de revalorización.
	 */
	public static void validarParamEntradaFuncionFobligado(final Integer x, final Integer t, final BigDecimal alfa, final List<BigDecimal> valoresTabMort,
			final Integer w, final Integer n, final BigDecimal i1, final BigDecimal gepc, final BigDecimal prp){
		if (null == x) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == t) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == alfa) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_ALFA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_ALFA});
		}
		
		if (null == valoresTabMort || valoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == w) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
		
		if (null == n) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == gepc) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GEPC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GEPC});
		}
		
		if (null == prp) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PRP});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Sumapc
	 * @param j Término j ésimo de la progresión.
	 * @param t
	 * @param prp Porcentaje de  revalorización.
	 * @param ifal Variable de Apoyo IFAL.
	 */
	public static void validarParamEntradaFuncionSumapc(final Integer j, final Integer t, final BigDecimal prp, final BigDecimal ifal){

		if (null == j){
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_J});
		}
		
		if (null == t) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == prp) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PRP});
		}
		
		if (null == ifal) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_IFAL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_IFAL});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Futuras
	 * @param x Edad a fecha de cálculo.
	 * @param t
	 * @param n Duración del seguro en años.
	 * @param fut Variable de Apoyo FUT.
	 * @param prp Porcentaje de revalorización.
	 * @param ifal Variable de Apoyo IFAL.
	 * @param i1 Interés del primer tramo.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 */
	public static void validarParamEntradaFuncionFuturas(final Integer x, final Integer t, final Integer n, final BigDecimal fut, final BigDecimal prp,
			final BigDecimal ifal, final BigDecimal i1, final List<BigDecimal> valoresTabMort, final Integer w){
		
		if (null == x) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == t) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == n) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == fut) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FUT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FUT});
		}
		
		if (null == prp) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PRP});
		}

		if (null == ifal) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_IFAL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_IFAL});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == valoresTabMort || valoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == w) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Ppr
	 * @param tc Anualidad en curso.
	 * @param t Anualidad a calcular.
	 * @param pprUmic Primas Satisfechas en la anualidad T.
	 */
	public static void validarParamEntradaFuncionPpr(final Integer tc, final Integer t, final BigDecimal pprUmic){
		
		if (null == tc) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TC});
		}
		
		if (null == t) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == pprUmic) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PRP});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para las funciones Pendpa y Pagadpa
	 * @param cformapago Código forma de pago de la prima.
	 * @param tc Anualidad en curso.
	 * @param t Anualidad a calcular.
	 * @param pprUmic Primas Satisfechas en la anualidad T.
	 * @param primatotal Prima Total.
	 * @param primaIni Prima Inicial.
	 */
	public static void validarParamEntradaFuncionesPendpaPagadpa(final String cformapago, final Integer tc, final Integer t, final BigDecimal pprUmic,
			final BigDecimal primatotal, final BigDecimal primaIni){
		
		if (null == cformapago) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CFORMAPAGO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CFORMAPAGO});
		}
		
		if (null == tc) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TC});
		}
		
		if (null == t) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == pprUmic) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PPR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PPR});
		}
		
		if (null == primatotal) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PRIMA_TOTAL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PRIMA_TOTAL});
		}
		
		if (null == primaIni) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PRIMA_INI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PRIMA_INI});
		}
		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Paeint Parte1
	 * @param x Edad a fecha de cálculo.
	 * @param t
	 * @param bbeta Porción de año restante hasta el próximo aniversario de la póliza.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param gepc Gastos de gestión externa.
	 */
	public static void validarParamEntradaFuncionPaeintParte1 (final Integer x, final Integer t, final BigDecimal bbeta, final List<BigDecimal> valoresTabMort,
			final Integer w, final BigDecimal gepc){
		
		if (null == x) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == t) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == bbeta) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA1});
		}
		
		if (null == valoresTabMort || valoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == w) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
		
		if (null == gepc) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GEPC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GEPC});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Paeint Parte2
	 * @param cformapago Código forma de pago de la prima.
	 * @param tc0 Anualidad en curso.
	 * @param pnatc Prima Tarada.
	 * @param ifal Variable de Apoyo IFAL.
	 * @param pprUmic Primas Satisfechas en la anualidad T.
	 */
	public static void validarParamEntradaFuncionPaeintParte2 (final String cformapago, final Integer tc0, final BigDecimal pnatc,
			final BigDecimal ifal, final BigDecimal pprUmic){
		
		if (null == cformapago) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CFORMAPAGO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CFORMAPAGO});
		}
		
		if (null == tc0) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TC0));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TC0});
		}
		
		if (null == ifal) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_IFAL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_IFAL});
		}
		
		if (null == pnatc) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PNA0));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PNA0});
		}
		
		if (null == pprUmic) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PRP});
		}

	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función FutFlex Parte1
	 * @param tc Anualidad de cálculo.
	 * @param tcm Meses completos transcurridos desde fecha de efecto a fecha de cálculo(fecha de cierre).
	 * @param ttm Número de meses transcurridos desde el efecto a la fecha de vencimiento.
	 * @param beta Meses transcurridos desde la fecha de cálculo (Fecha de cierre)  hasta la fecha de proyección.
	 * @param pNAtc Prima Tarada.
	 */
	public static void validarParamEntradaFuncionFutflexParte1(final Integer tc, final Integer tcm, final Integer ttm, final Integer beta, 
			final BigDecimal pNAtc){
		
		if (null == tc) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TC});
		}
		
		if (null == tcm) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TCM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TCM});
		}
		
		if (null == ttm) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TTM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TTM});
		}
		
		if (null == beta) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA1});
		}
		
		if (null == pNAtc) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PNA0));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PNA0});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función FutFlex Parte2
	 * @param pas Variable de Apoyo PAS.
	 * @param fut Variable de Apoyo FUT.
	 * @param prp Porcentaje de revalorización de Primas.
	 * @param tit1 Interés del primer tramo.
	 */
	public static void validarParamEntradaFuncionFutflexParte2(final BigDecimal pas, final BigDecimal fut, final BigDecimal prp, 
			final BigDecimal tit1){
		
		if (null == pas) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PAS));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PAS});
		}
		
		if (null == fut) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FUT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FUT});
		}
		
		if (null == prp) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PRP});
		}
		
		if (null == tit1) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Sumpg
	 * @param tc Anualidad de cálculo.
	 * @param tcm Meses completos transcurridos desde fecha de efecto a fecha de cálculo(fecha de cierre).
	 * @param beta Meses transcurridos desde la fecha de cálculo (Fecha de cierre)  hasta la fecha de proyección.
	 * @param prp Porcentaje de revalorización de Primas.
	 * @param tit1 Interés del primer tramo.
	 */
	public static void validarParamEntradaFuncionSumpg(final Integer tc, final Integer tcm, final Integer beta, final BigDecimal prp, 
			final BigDecimal tit1){
		
		if (null == tc) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TC});
		}
		
		if (null == tcm) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TCM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TCM});
		}
		
		
		if (null == beta) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA1});
		}
		
		if (null == prp) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PRP});
		}
		
		if (null == tit1) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Ppcap Parte1
	 * @param tc Anualidad a calcular.
	 * @param tcm Meses completos transcurridos desde fecha de efecto a fecha de cálculo(fecha de cierre).
	 * @param ttm Número de meses transcurridos desde el efecto a la fecha de vencimiento.
	 * @param beta Meses transcurridos desde la fecha de cálculo (Fecha de cierre)  hasta la fecha de proyección.
	 * @param tc0 Anualidad en curso.
	 */
	public static void validarParamEntradaFuncionPpcapParte1(final Integer tc, final Integer tcm, final Integer ttm, 
			final Integer beta, final Integer tc0){
		
		if (null == tc) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TC});
		}
		
		if (null == tcm) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TCM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TCM});
		}
		
		if (null == ttm) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TTM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TTM});
		}
		
		if (null == beta) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA1});
		}
		
		if (null == tc0) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TC0));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TC0});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Ppcap Parte2
	 * @param ifal Variable de Apoyo IFAL.
	 * @param ppcapUmic Primas Periodicas Capitalizadas hasta la anualidad en curso.
	 * @param n Duración del seguro en años.
	 * @param pprUmic Primas Satisfechas en la anualidad T.
	 * @param pNAtc Prima Tarada.
	 */
	public static void validarParamEntradaFuncionPpcapParte2(final BigDecimal ifal, final BigDecimal ppcapUmic, final Integer n, 
			final BigDecimal pprUmic, final BigDecimal pNAtc){
		
		if (null == ifal) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_IFAL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_IFAL});
		}
		
		if (null == ppcapUmic) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PPCAP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PPCAP});
		}
		
		if (null == n) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == pprUmic) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PRP});
		}
		
		if (null == pNAtc) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PNA0));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PNA0});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Ppcap Parte3
	 * @param cformapago Código forma de pago de la prima.
	 * @param prp Porcentaje de revalorización de Primas.
	 * @param pas Variable de Apoyo PAS.
	 * @param fut Variable de Apoyo FUT.
	 * @param tit1 Interés del primer tramo
	 * @param primaIni Prima Inicial.
	 */
	public static void validarParamEntradaFuncionPpcapParte3(final String cformapago, final BigDecimal prp, final BigDecimal pas, 
			final BigDecimal fut, final BigDecimal tit1, final BigDecimal primaIni){
		
		if (null == cformapago) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CFORMAPAGO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CFORMAPAGO});
		}
		
		if (null == prp) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PRP});
		}
		
		if (null == pas) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PAS));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PAS});
		}
		
		if (null == fut) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FUT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FUT});
		}
		
		if (null == tit1) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == primaIni) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PRIMA_INI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PRIMA_INI});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Prima Parte1
	 * @param t Periodo mensual de proyección
	 * @param ttm Número de meses transcurridos desde el efecto a la fecha de vencimiento
	 * @param pna Prima del periodo
	 * @param np Forma de pago de la prima
	 */
	public static void validarParamEntradaFuncionPrimaParte1 (final Integer t, final Integer ttm, final BigDecimal pna, final Integer np) {
		
		if (null == t) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == ttm) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TTM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TTM});
		}
		
		if (null == pna) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PNA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PNA});
		}
		
		if (null == np) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NP});
		}
		
	}
	
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Prima Parte2
	 * @param prp Porcentaje de revalorización de Primas
	 * @param mesefect Mes de la fecha de efecto
	 * @param diaVto Día de vencimiento
	 * @param diaPrima Día de pago de la prima
	 */
	public static void validarParamEntradaFuncionPrimaParte2 (final BigDecimal prp, final Integer mesefect, final Integer diaVto, final Integer diaPrima) {
		
		if (null == prp) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PRP});
		}
		
		if (null == mesefect) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_MES_EFECTO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_MES_EFECTO});
		}
		
		if (null == diaVto) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DIAVTO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DIAVTO});
		}
		
		if (null == diaPrima) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DIAPRIMA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DIAPRIMA});
		}
	}
	
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función intp
	 * @param t Periodo mensual de proyección
	 * @param ttm Número de meses trnascurridos desde el efecto a la fecha de vencimiento
	 * @param prima 
	 * @param diaVto Día de vencimiento
	 * @param diaprima Día de pago de la prima
	 * @param i1 Tramo de interés 1
	 */
	public static void validarParamEntradaFuncionIntp (final Integer t,final Integer ttm, final BigDecimal prima, final Integer diaVto,
			final Integer diaPrima, final BigDecimal i1) {
		
		if (null == t) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == ttm) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TTM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TTM});
		}
		
		if (null == prima) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PRIMA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PRIMA});
		}
		
		if (null == diaVto) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DIAVTO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DIAVTO});
		}
		
		if (null == diaPrima) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DIAPRIMA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DIAPRIMA});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
	}
	
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función intp parte1
	 * @param t Periodo mensual de proyección
	 * @param ttm Número de meses transcurrido desde el efecto a la fecha de vencimiento
	 * @param diaVto Día de vencimiento
	 * @param gast Gasto de gestión
	 */
	public static void validarParamEntradaFuncionIntgParte1 (final Integer t, final Integer ttm, final Integer diaVto, final BigDecimal gast) {
		
		if (null == t) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == ttm) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TTM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TTM});
		}
		
		if (null == diaVto) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DIAVTO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DIAVTO});
		}
		
		if (null == gast) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GAST));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GAST});
		}
		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función intp parte2
	 * @param cfall Componente de gastos sobre el fondo
	 * @param i1 Tramo de interés 1
	 * @param CC06 Costo de exoneración pago primas por sorteo
	 * @param bxAnterior Saldo inicial del periodo
	 */
	public static void validarParamEntradaFuncionIntgParte2 (final BigDecimal cfall, final BigDecimal i1, final BigDecimal CC06, final BigDecimal bxAnterior) {
		
		if (null == cfall) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CFALL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CFALL});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == CC06) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CC06));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CC06});
		}
		
		if (null == bxAnterior) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BXANTERIOR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BXANTERIOR});
		}
		
		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función PPTppal
	 * @param pnaTc La prima neta anual actual a fecha de cierre
	 * @param cformapago Codigo forma de pago de la prima
	 * @param TC Anualidad en curso
	 * @param T Anualidad a calcular
	 * @param pprUmic Primas Satisfechas en la anualidad T
	 * @param primaTarada Prima Tarada
	 * @param primaIni Prima Inicial
	 */
	
	public static void validarParamEntradaFuncionPPTppal(final BigDecimal pnaTc, final String cformapago,final Integer TC,final Integer T,
			final BigDecimal pprUmic, final BigDecimal primaTarada, final BigDecimal primaIni) {
		
		if (null == pnaTc) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PNATC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PNATC});
		}
		
		if (null == cformapago) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CFORMAPAGO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CFORMAPAGO});
		}
		
		if (null == TC) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TC});
		}
		
		if (null == T) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == pprUmic) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PPR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PPR});
		}
		
		if (null == primaTarada) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PRIMA_TARADA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PRIMA_TARADA});
		}
		
		if (null == primaIni) {
			if (ValidacionesFuncionesPrimas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesPrimas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PRIMA_INI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PRIMA_INI});
		}
	}

}
