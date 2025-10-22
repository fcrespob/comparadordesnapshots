package es.mapfre.solvencia.formulacion.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;

public class ValidacionesFuncionesActualizacionFinanciera {
	
	/** Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ValidacionesFuncionesActualizacionFinanciera.class);
	
	/**
	 * Función encargada de validar la obligatoriedad de los campos de la función calcularTiposInteres
	 * 
	 * @param criterioInteres
	 * 				Identifica el criterio de interpolación para la obtención del interés a aplicar.
	 * @param durAnt
	 * 				Duración inmediatamente anterior a la duración requerida.
	 * @param intAnt
	 * 				Tipo de interés asociado a la duración inmediatamente anterior a la duración requerida.
	 * @param durPost
	 * 				Duración inmediatamente posterior a la duración requerida.
	 * @param intPost
	 * 				Tipo de interés asociado a la duración inmediatamente posterior a la duración requerida.
	 * @param durJ
	 * 				Duración requerida
	 */
	public static void validacionObligatoriosFuncionCalcularTiposInteres(final String criterioInteres, final Integer durAnt, final BigDecimal intAnt,
			final Integer durPost, final BigDecimal intPost, final Integer durJ) {
		
		if (null == criterioInteres || criterioInteres.isEmpty()) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CRI_INTERES));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CRI_INTERES});
		}
		
		if (null == durAnt) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DUR_ANTERIOR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DUR_ANTERIOR});
		}
		
		if (null == intAnt) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_INT_ANTERIOR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_INT_ANTERIOR});
		}
		
		if (null == durPost) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DUR_POSTERIOR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DUR_POSTERIOR});
		}
				
		if (null == intPost) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_INT_POSTERIOR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_INT_POSTERIOR});
		}
		
		if (null == durJ) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DUR_REQUERIDA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DUR_REQUERIDA});
		}
	}
	
	public static void validarCriterioInteresFuncionCalcularTipoInteres(final String criterioInteres) {
		if (null != criterioInteres && !criterioInteres.isEmpty() &&
				!ConstantsFunciones.CTE_CRI_IN_ME_DIA.equals(criterioInteres) && !ConstantsFunciones.CTE_CRI_IN_SUPERI.equals(criterioInteres)) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA2(criterioInteres, ConstantsFunciones.CTE_CRI_INTERES));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A2, new String[]{ConstantsFunciones.CTE_CRI_INTERES, criterioInteres});
		} 
	}
	
	/**
	 *  Función encargada de validar los parametros de entrada de la función dif.
	 * @param edadAsegurado
	 * 					Edad x del asegurado
	 * @param desRenta
	 * 				Desplazamiento de la renta
	 * @param durSeguro
	 * 				Duración del seguro
	 * @param inTramo1
	 * 				Tramo de interés 1
	 * @param lstValoresTabMort
	 * 				Valores de la tabla de mortalidad
	 */
	public static void validarParamEntradaFuncionDif(final Integer edadAsegurado, final Integer desRenta, final Integer durSeguro, final BigDecimal inTramo1, 
			final List<BigDecimal> lstValoresTabMort) {
		
		if (null == edadAsegurado) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_EDAD_ASEG));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_EDAD_ASEG});
		}
		
		if (null == desRenta) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DES_RENTA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DES_RENTA});
		}
		
		if (null == durSeguro) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DUR_SEGURO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DUR_SEGURO});
		}
		
		if (null == inTramo1) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_IN_TRAMO1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_IN_TRAMO1});
		}
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
	}
	
	/**
	 * Función encargada de validar la obligatoriedad de los parámetros de entrada de la función vVida
	 * 
	 * @param actj Corresponde al período transcurrido desde  la fecha alta ajuste o suscripción  hasta la fecha "j"
	 * @param numi1 Primer Interés técnico
	 * @param numi2 Segundo Interés técnico
	 * @param numm Número de años en que aplicamos un primer interés técnico desde fecha suscripción
	 * @param varTCY Resultado de la función naños() en base al criterio establecido entre la fecha de efecto de la suscripción y la fecha de cálculo
	 */
	public static void validarParamEntradaFuncionVVida(final BigDecimal actj, final BigDecimal numi1, final BigDecimal numi2, final BigDecimal numm, final BigDecimal varTCY) {
		
		if (null == actj) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_ACTJ));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_ACTJ});
		}
		
		if (null == numm) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NUMM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NUMM});
		}
		
		if (null == numi1) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NUMI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NUMI1});
		}
		
		if (null == numi2) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NUMI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NUMI2});
		}
		
		if (null == varTCY) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TCY));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TCY});
		}
	}
	
	/**
	 * Función encargada de validar la obligatoriedad de los parámetros de entrada de la función vmort
	 * 
	 * @param numi1 Primer Interés técnico
	 * @param numi2 Segundo Interés técnico
	 * @param numm Número de años en que aplicamos un primer interés técnico desde fecha suscripción
	 * @param numn Duración real del ajuste o suscripción
	 * @param varTCY Resultado de la función naños() en base al criterio establecido entre la fecha de efecto de la suscripción y la fecha de cálculo
	 * @param partAnoNR Fracción de año que hay entre la renovación anterior NR y posterior (NR+1).
	 * @param varRY Fracción de año incompleto entre la anterior fecha de renovación y la fecha de cálculo fcal.
	 */
	public static void validarParamEntradaFuncionVMort(final BigDecimal numi1, final BigDecimal numi2, final BigDecimal numm, final BigDecimal numn,
			final BigDecimal varTCY, final BigDecimal partAnoNR, final BigDecimal varRY) {
		
		if (null == numi1) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NUMI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NUMI1});
		}
		
		if (null == numi2) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NUMI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NUMI2});
		}
		
		if (null == numm) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NUMM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NUMM});
		}
		
		if (null == numn) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NUMN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NUMN});
		}
		
		if (null == varTCY) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TCY));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TCY});
		}
		
		if (null == partAnoNR) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PART_ANO_NR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PART_ANO_NR});
		}
		
		if (null == varRY) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_RY));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_RY});
		}
	}

	/**
	 * Función encargada de validar la primera parte de los campos de entrada de la función rflex
	 * 
	 * @param vari1 Primer Interés técnico 
	 * @param vari2 Segundo Interés técnico 
	 * @param i1Bti Primer Interés técnico según Bases Técnicas Iniciales
	 * @param i2Bti Segundo Interés técnico según Bases Técnicas Iniciales
	 * @param beta1 Variable Beta1
	 */
	public static void validarParamEntradaFuncionRflexParte1(final BigDecimal vari1, final BigDecimal vari2, final BigDecimal i1Bti, final BigDecimal i2Bti, final BigDecimal beta1) {
		
		if (null == vari1) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == vari2) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
		
		if (null == i1Bti) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_I1_BTI_MINUS));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_I1_BTI_MINUS});
		}
		
		if (null == i2Bti) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_I2_BTI_MINUS));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_I2_BTI_MINUS});
		}
		
		if (null == beta1) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA1});
		}
	}
	
	/**
	 * Función encargada de validar la segunda parte de los campos de entrada de la función rflex
	 * 
	 * @param beta2 Variable Beta2
	 * @param varm Número de años en que aplicamos un primer interés técnico desde fecha suscripción
	 * @param varn Duración real del ajuste o suscripción
	 * @param ren Período de tiempo transcurrido desde la fecha de alta del ajuste hasta la fecha de renovación siguiente al momento de cálculo
	 * @param varj Corresponde al período transcurrido desde  la fecha alta ajuste o suscripción  hasta la fecha “j” 
	 */
	public static void validarParamEntradaFuncionRflexParte2 ( final BigDecimal beta2, final BigDecimal varm, final BigDecimal varn, final BigDecimal ren, final BigDecimal varj) {
		
		if (null == beta2) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA2});
		}
		
		if (null == varm) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == varn) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == ren) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_REN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_REN});
		}
		
		if (null == varj) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_J});
		}
	}
	
	/**
	 * Función encargada de validar la primera parte delos campos de entrada de la función ActFall
	 * 
	 * @param vari1 Primer Interés técnico 
	 * @param vari2 Segundo Interés técnico
	 * @param i1Bti Primer Interés técnico según Bases Técnicas Iniciales
	 * @param i2Bti Segundo Interés técnico según Bases Técnicas Iniciales
	 * @param beta1 Variable Beta1
	 * @param beta2 Variable Beta2
	 */
	public static void validarParamEntradaFuncionActFallParte1(final BigDecimal vari1, final BigDecimal vari2, final BigDecimal i1Bti, final BigDecimal i2Bti, final BigDecimal beta1, final BigDecimal beta2) {
		
		if (null == vari1) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == vari2) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
		
		if (null == i1Bti) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_I1_BTI_MINUS));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_I1_BTI_MINUS});
		}
		
		if (null == i2Bti) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_I2_BTI_MINUS));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_I2_BTI_MINUS});
		}
		
		if (null == beta1) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA1});
		}
		
		if (null == beta2) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA2});
		}		
	}
	
	/**
	 * Función encargada de validar la segunda parte delos campos de entrada de la función ActFall
	 * 
	 * @param varm Número de años en que aplicamos un primer interés técnico desde fecha suscripción
	 * @param ren Período de tiempo transcurrido desde la fecha de alta del ajuste hasta la fecha de renovación siguiente al momento de cálculo
	 * @param difercol Diferimiento en cobrar la renta
	 * @param nirp Número de años completos que quedan pendientes desde la siguiente renovación a la fecha de cálculo hasta la fecha de inicio de cobro de rentas
	 * @param nirmv Fracción de año comprendida entre la fecha de renovación inmediata anterior a la fecha de inicio de cobro de renta y dicha fecha de inicio cobro renta
	 */
	public static void validarParamEntradaFuncionActFallParte2 (final BigDecimal varm, final BigDecimal ren, final BigDecimal difercol, final Integer nirp, final BigDecimal nirmv) {
		
		if (null == varm) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == ren) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_REN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_REN});
		}
		
		if (null == difercol) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DIFERCOL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DIFERCOL});
		}
		
		if (null == nirp) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NIRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NIRP});
		}
		
		if (null == nirmv) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NIRMV));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NIRMV});
		}
	}
	
	/**
	 * 
	 * Función encargada de validar la primera parte de la función Cmorpend.
	 * 
	 * @param fecIni Fecha de inicio de cobro de la renta
	 * @param fecAntRenova Fecha de anterior renovación
	 * @param ndap Número de años completos que quedan pendientes desde la siguiente renovación a la fecha de cálculo hasta el vencimiento
	 * @param nirp Número de años completos que quedan pendientes desde la siguiente renovación a la fecha de cálculo hasta la fecha de inicio de cobro de rentas
	 * @param lstValoresTabMort Valores de la tabla de mortalidad
	 */
	public static void validarParamEntradaFuncionCmorpendParte1(final Timestamp fecIni, final Timestamp fecProxRenova, final Integer ndap, final Integer nirp, final List<BigDecimal> lstValoresTabMort) {
		
		if (null == fecIni) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_INI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_INI});
		}
		
		if (null == fecProxRenova) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_PROX_RENO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_PROX_RENO});
		}
		
		if (null == ndap) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NDAP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NDAP});
		}
		
		if (null == nirp) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NIRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NIRP});
		}
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
	}
	
	/**
	 * Función encargada de validar la segundo parte de la función Cmorpend
	 * 
	 * @param varzc Edad del asegurado a Fecha de cálculo
	 * @param xren Edad en la renovación siguiente a la fecha de cálculo fcal.
	 * @param vmort Actualización financiera a fcal a la mitad del período que va hasta la próxima renovación
	 */
	public static void validarParamEntradaFuncionCmorpendParte2(final BigDecimal varzc, final BigDecimal xren, final BigDecimal vmort) {
		
		if (null == varzc) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARZC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARZC});
		}
		
		if (null == xren) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_XREN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_XREN});
		}
		
		if (null == vmort) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VMORT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VMORT});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función ActVbx
	 * @param var1 Duracion periodo de actualización 1.  
	 * @param var2 Duracion periodo de actualización 2.
	 * @param m Número de años en que aplicamos un primer interés técnico.
	 * @param unoMasI1Entre100 Primer Interés técnico mas 1 entre 100.
	 * @param unoMasI2Entre100 Segundo Interés técnico mas 1 entre 100.
	 */
	public static void validarParamEntradaFuncionActVbx(final BigDecimal var1, final BigDecimal var2, final BigDecimal m, final BigDecimal unoMasI1Entre100, 
			final BigDecimal unoMasI2Entre100){
		
		if (null == var1) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_1});
		}
		
		if (null == var2) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_2});
		}
		
		if (null == m){
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == unoMasI1Entre100) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == unoMasI2Entre100) {
			if (ValidacionesFuncionesActualizacionFinanciera.LOG.isDebugEnabled()) {
				ValidacionesFuncionesActualizacionFinanciera.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
		
	}
	
}
