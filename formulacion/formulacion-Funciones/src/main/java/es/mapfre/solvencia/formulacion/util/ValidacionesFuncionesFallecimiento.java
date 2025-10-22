package es.mapfre.solvencia.formulacion.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;

public class ValidacionesFuncionesFallecimiento {
	
	/** Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ValidacionesFuncionesFallecimiento.class);
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función vfal (Parte2)
	 * @param prima 
	 * 				prima 
	 * @param cRm 
	 * 				Capital en riesgo máximo
	 * @param gamma 
	 * 				% sobre prima para capital fallecimiento
	 * @param mescare 
	 * 				Meses carencia 
	 */
	public static void validacionCamposFuncionVFalParte2(final BigDecimal prima, final BigDecimal gamma, final Integer mescare) {
		
		if (null == prima) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PRIMA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PRIMA});
		}
		
		if (null == gamma) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GAMMA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GAMMA});
		}
		
		if (null == mescare) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_MES_CAREN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_MES_CAREN});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función vfal (Parte1)
	 * 
	 * @param mesesCompletos 
	 * 				Meses completos transcurridos desde efecto (técnico) póliza a fecha cálculo
	 * @param tipoIPrimerTramo 
	 * 				Tipo de interés primer tramo
	 * @param duracionPrimerTra 
	 * 				Duración primer tramo
	 * @param tipoISegundoTramo 
	 * 				Tipo de interés segundo tramo
	 * @param edadActuarial 
	 * 				Edad actuarial a fecha efecto técnico.
	 * @param lstValoresTabMort
	 * 				Valores de la tabla de mortalidad
	 */
	public static void validacionCamposFuncionVFalParte1(final Integer mesesCompletos, final BigDecimal tipoIPrimerTramo, final Integer duracionPrimerTra,
			final BigDecimal tipoISegundoTramo, final Integer edadActuarial, final List<BigDecimal> lstValoresTabMort) {
		if (null == mesesCompletos) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_MESES_COMPLET));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_MESES_COMPLET});
		}
		if (null == tipoIPrimerTramo) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_TI_PRIMER_TRA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_TI_PRIMER_TRA});
		}
		if (null == duracionPrimerTra) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DUR_PRIMER_TR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DUR_PRIMER_TR});
		}
		if (null == tipoISegundoTramo) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_TI_SEGUNDO_TR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_TI_SEGUNDO_TR});
		}
		if (null == edadActuarial) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_EDAD_ACTUARIA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_EDAD_ACTUARIA});
		}
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
	}
	
	/**
	 * Función Función encargada de validar la segunda parte de los parametros de entrada de la función capCriFallec.
	 * 
	 * @param iant
	 * @param valoresTabMort Valores de la tabla de mortalidad
	 */
	public static void validarCamposEntradaFuncionCapCriFallecParte2(final BigDecimal iant, final List<BigDecimal> lstValoresTabMort) {
		
		if (null == iant) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_IANT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_IANT});
		}
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
	}
	
	/**
	 * Función Función encargada de validar la primera parte de los parametros de entrada de la función capCriFallec.
	 * 
	 * @param varx Edad x del asegurado
	 * @param varp Desplazamiento de la renta
	 * @param varn Duración del seguro en años
	 * @param ifal Variable de apoyo IFAL
	 * @param vari1 Interés 1 de la base contable tratada
	 */
	public static void validarCamposEntradaFuncionCapCriFallecParte1(final Integer varx, final Integer varp,
			final Integer varn, final BigDecimal ifal, final BigDecimal vari1) {
		
		if (null == varx) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == varp) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARP});
		}
		
		if (null == varn) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == vari1) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == ifal) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_IFAL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_IFAL});
		}
	}

	/**
	 * Función encargada de realizar la validación de la segunda parte de los campos de entrada de la función "geoCriFallec"
	 * 
	 * @param prp Porcentaje de revalorización de Primas
	 * @param lstValoresTabMort Valores de la tabla de mortalidad
	 */
	public static void validarCamposEntradaFuncionGeoAriCriFallecParte2(final BigDecimal prp, final List<BigDecimal> lstValoresTabMort, final BigDecimal pas, final BigDecimal fut) {
		
		if (null == prp) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PRP});
		}
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == pas) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PAS));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PAS});
		}
		
		if (null == fut) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FUT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FUT});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de la primera parte de los campos de entrada de la función "geoCriFallec"
	 * 
	 * @param varx Edad x del asegurado
	 * @param vartc Desplazamiento de la renta
	 * @param varn Duración del seguro en años
	 * @param ifal Variable de apoyo IFAL
	 * @param vari1 Interés 1 de la base contable tratada
	 */
	public static void validarCamposEntradaFuncionGeoAriCriFallecParte1(final Integer varx, final Integer varp, final Integer varn, final BigDecimal ifal, 
			final BigDecimal vari1) {
		
		if (null == varx) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		if (null == varp) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARP});
		}
		if (null == varn) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == vari1) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		if (null == ifal) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_IFAL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_IFAL});
		}
	}

	/**
	 * Función encargada de validar la primera parte de los parametros de la función CFallec
	 * 
	 * @param fecIni Fecha de inicio de cobro de la renta
	 * @param fecAntRenova Fecha de anterior renovación
	 * @param varm Número de años en que aplicamos un primer interés técnico desde fecha suscripción
	 * @param varn Duración real del ajuste o suscripción
	 * @param nirp Número de años completos que quedan pendientes desde la siguiente renovación a la fecha de cálculo hasta la fecha de inicio de cobro de rentas
	 * @param lstValoresTabMort Valores de la tabla de mortalidad
	 * @param varzc Edad del asegurado a Fecha de cálculo
	 */
	public static void validarParamEntradaFuncionCFallecParte1(final Timestamp fecIni, final Timestamp fecProxRenova, final BigDecimal varm, final BigDecimal varn, final Integer nirp, final List<BigDecimal> lstValoresTabMort, final BigDecimal varzc) {
		
		if (null == fecIni) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_INI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_INI});
		}
		
		if (null == fecProxRenova) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_PROX_RENO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_PROX_RENO});
		}
		
		if (null == varm) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == varn) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == nirp) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NIRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NIRP});
		}
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == varzc) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARZC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARZC});
		}
	
	}
	
	/**
	 * Función encargada de validar la segunda parte de los parametros de la función CFallec
	 * 
	 * @param ren Período de tiempo transcurrido desde la fecha de alta del ajuste hasta la fecha de renovación siguiente al momento de cálculo
	 * @param vvida Actualización financiera a fcal desde la fecha de pago de cada renta j.
	 * @param beta1 Variable Beta1
	 * @param beta2 Variable Beta2
	 * @param i1Bti Primer Interés técnico según Bases Técnicas Iniciales
	 * @param i2Bti Segundo Interés técnico según Bases Técnicas Iniciales
	 * @param varI1 Primer Interés técnico 
	 * @param varI2 Segundo Interés técnico 
	 */
	public static void validarParamEntradaFuncionCFallecParte2(final BigDecimal ren, final BigDecimal vvida, final BigDecimal beta1, final BigDecimal beta2, final BigDecimal i1Bti, final BigDecimal i2Bti, final BigDecimal varI1, final BigDecimal varI2) {
		if (null == ren) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_REN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_REN});
		}
		
		if (null == vvida) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VVIDA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VVIDA});
		}
		
		if (null == beta1) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA1});
		}
		
		if (null == beta2) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA2});
		}
		
		if (null == i1Bti) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_I1_BTI_MINUS));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_I1_BTI_MINUS});
		}
		
		if (null == i2Bti) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_I2_BTI_MINUS));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_I2_BTI_MINUS});
		}
		
		if (null == varI1) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == varI2) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
	}
	
	/**
	 * Función encargada de validar la obligatoriedad de la primera parte de los parametros de la función Cfallecvt
	 * 
	 * @param fecIni Fecha de inicio de cobro de la renta
	 * @param fecAntRenova Fecha de anterior renovación
	 * @param varm Número de años en que aplicamos un primer interés técnico desde fecha suscripción
	 * @param varn Duración real del ajuste o suscripción
	 * @param nirp Número de años completos que quedan pendientes desde la siguiente renovación a la fecha de cálculo hasta la fecha de inicio de cobro de rentas
	 * @param nirmv Fracción de año comprendida entre la fecha de renovación inmediata anterior a la fecha de inicio de cobro de renta y dicha fecha de inicio cobro renta
	 * @param lstValoresTabMort Valores de la tabla de mortalidad
	 */
	public static void validarParamEntradaFuncionCfallecVtParte1(final Timestamp fecIni, final Timestamp fecProxRenova, final BigDecimal varm, final BigDecimal varn, final Integer nirp, final BigDecimal nirmv, final List<BigDecimal> lstValoresTabMort) {
		
		if (null == fecIni) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_INI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_INI});
		}
		
		if (null == fecProxRenova) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_PROX_RENO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_PROX_RENO});
		}
		
		if (null == varm) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == varn) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == nirp) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NIRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NIRP});
		}
		
		if (null == nirmv) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NIRMV));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NIRMV});
		}
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
	}
	
	/**
	 * Función encargada de validar la obligatoriedad de la segunda parte de los parametros de la función Cfallecvt
	 * 
	 * @param varx Edad del asegurado a Fecha de Efecto
	 * @param varzc Edad del asegurado a Fecha de cálculo
	 * @param xren Edad en la renovación siguiente a la fecha de cálculo fcal
	 * @param ren  Período de tiempo transcurrido desde la fecha de alta del ajuste hasta la fecha de renovación siguiente al momento de cálculo
	 * @param vmort Actualización financiera a fcal a la mitad del período que va hasta la próxima renovación
	 * @param vvida Actualización financiera a fcal desde la fecha de pago de cada renta j.
	 * @param difercol Diferimiento en cobrar la renta
	 */
	public static void validarParamEntradaFuncionCfallecVtParte2(final BigDecimal varx, final BigDecimal varzc, final BigDecimal xren, final BigDecimal ren, final BigDecimal vmort, final BigDecimal vvida, final BigDecimal difercol) {
		if (null == varx) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == varzc) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARZC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARZC});
		}
		
		if (null == xren) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_XREN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_XREN});
		}
		
		if (null == ren) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_REN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_REN});
		}
		
		if (null == vmort) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VMORT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VMORT});
		}
		
		if (null == vvida) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VVIDA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VVIDA});
		}
		
		if (null == difercol) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DIFERCOL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DIFERCOL});
		}
	}
	
	/**
	 * Función encargada de validar la obligatoriedad de la tercera parte de los parametros de la función Cfallecvt
	 * 
	 * @param modBeta Variable de Apoyo MODBETA
	 * @param beta1 Variable Beta1
	 * @param beta2 Variable Beta2
	 * @param i1Bti Primer Interés técnico según Bases Técnicas Iniciales
	 * @param i2Bti Segundo Interés técnico según Bases Técnicas Iniciales
	 * @param varI1 Primer Interés técnico 
	 * @param varI2 Segundo Interés técnico 
	 */
	public static void validarParamEntradaFuncionCfallecVtParte3(final String modBeta, final BigDecimal beta1, final BigDecimal beta2, final BigDecimal i1Bti, final BigDecimal i2Bti, final BigDecimal varI1, final BigDecimal varI2) {
		
		if (null == modBeta || modBeta.isEmpty()) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_MOD_BETA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_MOD_BETA});
		}
		
		if (null == beta1) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA1});
		}
		
		if (null == beta2) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA2});
		}
		
		if (null == i1Bti) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_I1_BTI_MINUS));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_I1_BTI_MINUS});
		}
		
		if (null == i2Bti) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_I2_BTI_MINUS));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_I2_BTI_MINUS});
		}
		
		if (null == varI1) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == varI2) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función CFFALLECPAG
	 * @param m Número de años en que aplicamos un primer interés técnico desde fecha suscripción.
	 * @param n Duración real del ajuste o suscripción.
	 * @param nDap Número de años completos que quedan pendientes desde la siguiente renovación a la fecha de cálculo hasta el vencimiento.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param zc Edad del asegurado a Fecha de cálculo.
	 * @param xRen Edad del asegurado a Fecha de Efecto.
	 * @param ren Período de tiempo transcurrido desde la fecha de alta del ajuste hasta la fecha de renovación siguiente al momento de cálculo.
	 * @param vVida Actualización financiera a fcal desde la fecha de pago de cada renta j.
	 * @param beta1 Variable Beta1.
	 * @param beta2 Variable Beta2.
	 * @param i1Bti Primer Interés técnico según Bases Técnicas Iniciales.
	 * @param i2Bti Segundo Interés técnico según Bases Técnicas Iniciales.
	 * @param i1 Primer Interés técnico.
	 * @param i2 Segundo Interés técnico.
	 */
	public static void validarParamEntradaFuncionCfallecpag(final BigDecimal m, final BigDecimal n, final Integer nDap, final List<BigDecimal> valoresTabMort, final BigDecimal zc, 
			final BigDecimal xRen, final BigDecimal ren, final BigDecimal vVida, final BigDecimal beta1, final BigDecimal beta2, final BigDecimal i1Bti, 
			final BigDecimal i2Bti, final BigDecimal i1, final BigDecimal i2){
		
		if (null == m){
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == n){
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == nDap){
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NDAP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NDAP});
		}
		
		if (null == valoresTabMort || valoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == zc){
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARZC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARZC});
		}
		
		if (null == xRen) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_XREN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_XREN});
		}
		
		if (null == ren) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_REN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_REN});
		}
		
		if (null == vVida) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VVIDA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VVIDA});
		}
		
		if (null == beta1){
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA1});
		}
		
		if (null == beta2){
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA2});
		}
		
		if (null == i1Bti){
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_I1_BTI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_I1_BTI});
		}
		
		if (null == i2Bti){
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_I2_BTI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_I2_BTI});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == i2) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
	}

	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Fall
	 * @param proyUmic Estructura detalleCorrientes de la umic.
	 * @param bloqueCorriente Indica el periodo de proyección J que se está calculando de entre todos los periodos de proyección de la umic (proyUmic).
	 * @param umic Contiene los datos de la Umic que se está procesando.
	 * @param btcUmic Contiene el detalle de la base técnica de cálculo para la umic.
	 * @param x363 Edad actuarial a la fecha de inicio de pago de renta del titular.
	 * @param lxIni Probabilidad supervivencia a  Edad X363ini.
	 * @param m Duración en que aplica el primer interés técnico.
	 * @param lstValoresTabMort Valores de la tabla de mortalidad.
	 * @param varCriterFec
	 */
	public static void validarParamEntradaFuncionFall(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, 
			final Umic umic, final DetalleBaseTecnica btcUmic, final Integer x363, final BigDecimal lxIni, final BigDecimal m, 
			final List<BigDecimal> lstValoresTabMort, final String varCriterFec){
		if (null == proyUmic || proyUmic.isEmpty()) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PROY_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PROY_UMIC});
		}
		
		if (null == bloqueCorriente){
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BLOQUE_CORR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BLOQUE_CORR});
		}
		
		if (null == umic){
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_UMIC});
		}
		
		if (null == btcUmic){
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BTC_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BTC_UMIC});
		}
		
		if (null == x363){
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_X363));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_X363});
		}
		
		if (null == lxIni){
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_LX_INI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_LX_INI});
		}
		
		if (null == m){
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == varCriterFec){
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CRIT_FEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CRIT_FEC});
		}
		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función FALLECPAGVT (parte 1)
	 * @param m Número de años en que aplicamos un primer interés técnico desde fecha suscripción.
	 * @param n Duración real del ajuste o suscripción.
	 * @param nDap Número de años completos que quedan pendientes desde la siguiente renovación a la fecha de cálculo hasta el vencimiento.
	 * @param nDmv Fracción de año comprendida entre la fecha de renovación de la umic inmediata anterior a la fecha de vencimiento del certificado y dicha fecha de vencimiento.
	 * @param frpsgact Fecha de Renovación posterior a la fecha de cierre.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param x Edad del asegurado a Fecha de Efecto.
	 * @param zc Edad del asegurado a Fecha de cálculo.
	 * @param xRen Edad en la renovación siguiente a la fecha de cálculo fcal.
	 * @param ren Período de tiempo transcurrido desde la fecha de alta del ajuste hasta la fecha de renovación siguiente al momento de cálculo.
	 * @param vMort Actualización financiera a fcal a la mitad del período que va hasta la próxima renovación.
	 */
	public static void validarParamEntradaFuncionFallecpagvt1(final BigDecimal m, final BigDecimal n, final Integer nDap, final BigDecimal nDmv, 
			final Timestamp frpsgact, final List<BigDecimal> valoresTabMort, final BigDecimal x, final BigDecimal zc, 
			final BigDecimal xRen, final BigDecimal vMort){
		
		if (null == m){
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == n){
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == nDap){
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NDAP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NDAP});
		}
		
		if (null == nDmv) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NDMV));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NDMV});
		}
		
		if (null == frpsgact) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_REN_PSGACT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_REN_PSGACT});
		}
		
		if (null == valoresTabMort || valoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == x) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == zc){
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARZC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARZC});
		}
		
		if (null == xRen) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_XREN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_XREN});
		}
		
		if (null == vMort) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VMORT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VMORT});
		}

	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función FALLECPAGVT (parte 2)
	 * @param vVida Actualización financiera a fcal desde la fecha de pago de cada renta j.
	 * @param tcy Distancia en Años entre la fecha de suscricpión y la fecha de cálculo.
	 * @param modBeta Variable de Apoyo MODBETA.
	 * @param beta1 Variable Beta1.
	 * @param beta2 Variable Beta2.
	 * @param i1Bti Primer Interés técnico según Bases Técnicas Iniciales.
	 * @param i2Bti Segundo Interés técnico según Bases Técnicas Iniciales.
	 * @param i1 Primer Interés técnico.
	 * @param i2 Segundo Interés técnico.
	 * @param fecvcto Fecha vencimiento.
	 */
	public static void validarParamEntradaFuncionFallecpagvt2(final BigDecimal vVida, final BigDecimal tcy, final String modBeta, 
			final BigDecimal beta1, final BigDecimal beta2, final BigDecimal i1Bti, final BigDecimal i2Bti, final BigDecimal i1, 
			final BigDecimal i2, final Timestamp fecvcto){
		
		if (null == vVida) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VVIDA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VVIDA});
		}
		
		if (null == tcy) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TCY));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TCY});
		}
		
		if (null == modBeta || modBeta.isEmpty()) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_MOD_BETA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_MOD_BETA});
		}
		
		if (null == beta1) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA1});
		}
		
		if (null == beta2) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA2});
		}
		
		if (null == i1Bti) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_I1_BTI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_I1_BTI});
		}
		
		if (null == i2Bti) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_I2_BTI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_I2_BTI});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == i2) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
		
		if (null == fecvcto){
			if(ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()){
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_VTO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_VTO});
		}

	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Morpend
	 * @param x Edad a fecha de cálculo.
	 * @param t Resultado de la función naños() en base al criterio establecido.
	 * @param beta Fracción de año pendiente entre el momento del cálculo y la siguiente anualidad del seguro.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param n Duración del seguro en años.
	 * @param i1 Interés del primer tramo.
	 */
	public static void validarParamEntradaFuncionMorpend(final Integer x, final Integer t, final BigDecimal beta, final List<BigDecimal> valoresTabMort,
			final Integer w, final Integer n, final BigDecimal i1){
		
		if (null == x) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == t) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == beta) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA1});
		}
		
		if (null == valoresTabMort || valoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == w) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
		
		if (null == n) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para las funciones Pasadaca y Pascap
	 * @param x Edad a fecha de cálculo.
	 * @param t Resultado de la función naños() en base al criterio establecido.
	 * @param ifal Variable de Apoyo IFAL.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param n Duración del seguro en años.
	 * @param i1 Interés del primer tramo.
	 */
	public static void validarParamEntradaFuncionesPasadacaPascap(final Integer x, final Integer t, final BigDecimal ifal, final List<BigDecimal> valoresTabMort,
			final Integer w, final Integer n, final BigDecimal i1){
		
		if (null == x) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == t) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == ifal) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_IFAL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_IFAL});
		}
		
		if (null == w) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
		
		if (null == n) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Fallecpu
	 * @param x Edad a fecha de cálculo.
	 * @param t
	 * @param beta Fracción de año pendiente entre el momento del cálculo y la siguiente anualidad del seguro.
	 * @param n Duración del seguro en años.
	 * @param i1 Interés del primer tramo.
	 * @param ifal Variable de Apoyo IFAL.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 */
	public static void validarParamEntradaFuncionFallecpu(final Integer x, final Integer t, final BigDecimal beta, final Integer n, final BigDecimal i1,
			final BigDecimal ifal, final List<BigDecimal> valoresTabMort, final Integer w){
		
		if (null == x) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == t) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == beta) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA1});
		}
		
		if (null == n) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == ifal) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_IFAL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_IFAL});
		}
		
		if (null == valoresTabMort || valoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == w) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Segeo2it
	 * @param difer Años de diferimiento hasta el inicio de la prestación.
	 * @param x Edad actuarial a fecha de efecto.
	 * @param t Anualidad del cálculo.
	 * @param n Duración del seguro.
	 * @param prg Porcentaje de la revalorización geométrica.
	 * @param lstValoresTabMort Valores de la tabla de mortalidad.
	 * @param m Número de años en que aplicamos un primer interés técnico.
	 * @param unoMasI1Entre100 Primer Interés técnico mas 1 entre 100.
	 * @param unoMasI2Entre100 Segundo Interés técnico mas 1 entre 100.
	 */
	public static void validarParamEntradaFuncionSegeo2it(final Integer difer, final BigDecimal x, final Integer t, 
			final Integer n, final BigDecimal prg, final List<BigDecimal> lstValoresTabMort, final BigDecimal m, 
			final BigDecimal unoMasI1Entre100, final BigDecimal unoMasI2Entre100){
		
		if (null == difer) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_DIFER));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_DIFER});
		}
		
		if (null == x) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == t) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == n) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == prg) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PRG));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PRG});
		}
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == m){
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == unoMasI1Entre100) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == unoMasI2Entre100) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
		
	}
	
	/**
	 * Función encargada de validar la obligatoriedad de la segunda parte de los parametros de la función Cfall
	 * @param t Periodo mensual de proyección
	 * @param ttm Número de meses transcurridos desde el efecto hasta la fecha de vencimiento
	 * @param x Edad del asegurado
	 * @param ifal Variable de apoyo IFAL
	 * @param gtosF Gastos sobre el fondo del periodo
	 */
	public static void validarParamEntradaFuncionCfallParte1 (final Integer t, final Integer ttm, final Integer x, final BigDecimal i1, final BigDecimal gtosF) {
		
		if (null == t) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == ttm) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TTM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TTM});
		}
		
		if (null == x) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == gtosF) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GTOSF));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GTOSF});
		}
	}
	
	
	/**
	 * Función encargada de validar la obligatoriedad de la segunda parte de los parametros de la función Cfall
	 * @param ggim Gastos sobre la prima (mensualizado)
	 * @param k % Capital en riesgo 
	 * @param diaVto Día de vencimiento
	 * @param diaPrima Día de pago de la prima
	 * @param bxAnterior Saldo inicial del periodo
	 */
	public static void validarParamEntradaFuncionCfallParte2 (final BigDecimal ggim, final BigDecimal k, final Integer diaVto,
			final Integer diaPrima, final BigDecimal bxAnterior) {
		
		if (null == k) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARK));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARK});
		}
		
		if (null == diaVto) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DIAVTO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DIAVTO});
		}
		
		if (null == diaPrima) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DIAPRIMA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DIAVTO});
		}
		
		if (null == bxAnterior) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BXANTERIOR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BXANTERIOR});
		}
	}

	
	/**
	 * Función encargada de validar la obligatoriedad de la segunda parte de los parametros de la función Cfall
	 * @param w Edad máxima de la tabla de mortalidad
	 * @param CRMax Capital en riesgo máximo
	 * @param valoresTabMort Valores de la tabla de mortalidad
	 */
	public static void validarParamEntradaFuncionCfallParte3 (final Integer w, final BigDecimal CRMax, final List<BigDecimal> valoresTabMort) {
		
		if (null == w) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
	
		if (null == CRMax) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_CRMAX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_CRMAX});
		}
		
		if (null == valoresTabMort) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}		
	}	

	
	/**
	 * Función encargada de validar la obligatoriedad para la función CF
	 * @param j Periodo de cálculo
	 * @param proyUmic Estructura detalleCorrientes de la umic
	 * @param cfant Capital de Fallecimiento 
	 * @param pu Aportación única del ajuste o de la suscripción
	 */
	public static void validarParamEntradaFuncionCf (final Integer j, final List<DetalleCorriente> proyUmic, final BigDecimal cfant, 
			final BigDecimal pu, final Integer varTcm) {
		if (null == j) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_J});
		}
		if (null == proyUmic) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PROY_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PROY_UMIC});
		}
		if (null == cfant) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CFANT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CFANT});
		}
		if (null == pu) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PU));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PU});
		}
		if (null == varTcm) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TCM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TCM});
		}
		
	}
	
	/**
	 * Función encargada de validar la obligatoriedad para la función FALLEC
	 * @param j Periodo de cálculo
	 * @param xc Edad a fecha de calculo
	 * @param xj Edad a fecha del periodo
	 * @param xjant Edad a fecha del periodo anterior
	 * @param valoresTabMortX Valores de la tabla de mortalidad
	 */
	public static void validarParamEntradaFuncionFallec (final Integer j, final BigDecimal xc, final BigDecimal xj, final BigDecimal xjant,
			final List<BigDecimal> valoresTabMortX) {
		if (null == j) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_J});
		}
		if (null == xc) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_XC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_XC});
		}
		if (null == xj) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_XJ));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_XJ});
		}
		if (null == xjant) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_XJANT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_XJANT});
		}
		if (null == valoresTabMortX) {
			if (ValidacionesFuncionesFallecimiento.LOG.isDebugEnabled()) {
				ValidacionesFuncionesFallecimiento.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}		
	}
	
	
}
