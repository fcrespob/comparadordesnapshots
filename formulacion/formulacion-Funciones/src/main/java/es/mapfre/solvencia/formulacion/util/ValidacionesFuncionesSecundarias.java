package es.mapfre.solvencia.formulacion.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;

public class ValidacionesFuncionesSecundarias {
	
	/** Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ValidacionesFuncionesSecundarias.class);
	
	/**
	 * Funcion encargada de comprobar la obligatoriedad de los atributos de la función ffInvitalicia.
	 * 
	 * @param fnac
	 *            fnac
	 * @param edadMax
	 *            edadMax
	 */
	public static void validacionCampoObligatoriosFuncionFFInvitalicia(final Timestamp fnac, final Integer edadMax) {

		if (null == fnac) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_NACI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_NACI});
		}

		if (edadMax == null || edadMax < 0) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_EDAD_MAXIMA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_EDAD_MAXIMA});
		}
	}
	
	/**
	 * Funcion encargada de comprobar la obligatoriedad de los atributos de la Funcion Y
	 * 
	 * @param finisusc
	 *            finisusc
	 * @param fcalc
	 *            fcalc
	 * @param criterioFecha
	 *            criterioFecha
	 */
	public static void validacionCamposObligatoriosFuncionY(final Timestamp finisusc, final Timestamp fcalc, final String criterioFecha) {

		if (null == finisusc) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_INI_SUS));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_INI_SUS});
		}

		if (null == fcalc) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FECHA_CALCULO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FECHA_CALCULO});
		}

		if (null == criterioFecha || criterioFecha.isEmpty()) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CRITERIO_FEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CRITERIO_FEC});
		}
	}
	
	/**
	 * Función encargada de validar los parametros de entrada de la función mg001Beta.
	 * 
	 * @param fantRenovacion
	 *           Identifica la fecha de efecto o renovación mensual anterior
	 * @param fproxRenovacion
	 *           Identifica la fecha de la próxima renovación mensual.
	 * @param fcierre
	 *           Identifica la fecha de cierre
	 * @param criterioFecha
	 * 			 Identifica el criterio de fechas para realizar el cálculo
	 */
	public static void validarCriteriosFuncionMg001Beta(final Timestamp fantRenovacion, final Timestamp fproxRenovacion, 
			final Timestamp fcierre, final String criterioFecha) {
		if (null == fantRenovacion) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_ANT_RENOV));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_ANT_RENOV});
		}
		
		if (null == fproxRenovacion) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_PROX_RENO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_PROX_RENO});
		}
		
		if (null == fcierre) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_CIERRE));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_CIERRE});
		}
		
		if (null == criterioFecha || criterioFecha.isEmpty()) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CRITERIO_FEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CRITERIO_FEC});
		}
	}
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función VPRE
	 * @param j Corresponde al período transcurrido desde  la fecha alta ajuste o suscripción  hasta la fecha “j”.
	 * @param m Número de años en que aplicamos un primer interés técnico.
	 * @param i1 Primer Interés técnico.
	 * @param i2 Segundo Interés técnico.
	 */
	public static void validarParamEntradaFuncionVpre(final Integer j, final BigDecimal m, final BigDecimal i1, final BigDecimal i2){
		
		if (null == j){
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_J});
		}
		
		if (null == m){
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == i2) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función VPRER
	 * @param varj Corresponde al período transcurrido desde  la fecha alta ajuste o suscripción  hasta la fecha “j”.
	 * @param m Número de años en que aplicamos un primer interés técnico.
	 * @param unoMasI1Entre100 Primer Interés técnico/100  +1.
	 * @param unoMasI2Entre100 Segundo Interés técnico/100  +1.
	 * @param ren Período de tiempo transcurrido desde la fecha de alta del ajuste hasta la fecha de renovación siguiente al momento de cálculo.
	 * 
	 */
	public static void validarParamEntradaFuncionVprer(final BigDecimal varj, final BigDecimal m, final BigDecimal unoMasI1Entre100, final BigDecimal unoMasI2Entre100, 
		final BigDecimal ren, final Integer j){
		
		if (null == varj){
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_J});
		}
		
		if (null == m){
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == unoMasI1Entre100) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == unoMasI2Entre100) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
		
		if (null == ren) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_REN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_REN});
		}
		
		if (null == j){
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_J});
		}
	}
	

	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Vidapa
	 * @param x Edad a fecha de cálculo.
	 * @param t Resultado de la función naños() en base al criterio establecido.
	 * @param alfa Fracción transcurrida desde efecto mensual anterior hasta la fecha de cierre.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param n Duración del seguro en años.
	 * @param i1 Interés del primer tramo.
	 * @param gic Gastos de gestión interna sobre Capital.
	 */
	public static void validarParamEntradaFuncionVidapa(final Integer x, final Integer t, final BigDecimal alfa, final List<BigDecimal> valoresTabMort,
			final Integer w, final Integer n, final BigDecimal i1, final BigDecimal gic){
		
		if (null == x) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == t) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == alfa) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA1});
		}
		
		if (null == valoresTabMort || valoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == w) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
		
		if (null == n) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == gic) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GIC});
		}
		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Futpg
	 * @param tc Anualidad de cálculo.
	 * @param tcm Meses completos transcurridos desde fecha de efecto a fecha de cálculo(fecha de cierre).
	 * @param ttm Número de meses transcurridos desde el efecto a la fecha de vencimiento.
	 * @param beta Meses transcurridos desde la fecha de cálculo (Fecha de cierre)  hasta la fecha de proyección.
	 * @param prp Porcentaje de revalorización de Primas.
	 */
	public static void validarParamEntradaFuncionFutpg(final Integer tc, final Integer tcm, final Integer ttm, final Integer beta, 
			final BigDecimal prp){
		
		if (null == tc) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TC});
		}
		
		if (null == tcm) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TCM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TCM});
		}
		
		if (null == ttm) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TTM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TTM});
		}
		
		if (null == beta) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA1});
		}
		
		if (null == prp) {
			if (ValidacionesFuncionesSecundarias.LOG.isDebugEnabled()) {
				ValidacionesFuncionesSecundarias.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PRP});
		}
	}

}
