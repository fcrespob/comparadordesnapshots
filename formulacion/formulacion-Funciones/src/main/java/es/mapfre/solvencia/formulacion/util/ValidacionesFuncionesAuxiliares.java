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


/**
 * Clase que encapsula las validaciones de distintas funciones auxiliares
 * @author rschacon
 *
 */
public final class ValidacionesFuncionesAuxiliares {
	/** Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ValidacionesFuncionesAuxiliares.class);

	/** Constructor privado.
	 */
	private ValidacionesFuncionesAuxiliares() { super(); }
	
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
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PROY_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PROY_UMIC});
		}

		if (null == bloqueCorriente) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BLOQUE_CORR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BLOQUE_CORR});
		}

		if (null == fcalc) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FCALC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FCALC});
		}

		if (null == umic) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_UMIC});
		}
		
		if (null == btcUmic) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BTC_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BTC_UMIC});
		}
	}
	
	/** 
	 * Función que valida la obligatoriedad de los parametros de entrada de cada uno de los distintos modulos de la aplicación.
	 * 
	 * @param proyUmic Estructura detalleCorrientes de la umic
	 * @param fcalc Fecha de calculo
	 * @param umic Contiene los datos de la Umic que se está procesando
	 * @param btcUmic Contiene el detalle de la base técnica de cálculo para la umic
	 */
	public static void validarParamEntrada(final List<DetalleCorriente> proyUmic, final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic) {
		
		//Validamos datos obligatorios
		if (null == proyUmic) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PROY_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PROY_UMIC});
		}

		if (null == fcalc) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FCALC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FCALC});
		}

		if (null == umic) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_UMIC});
		}
		
		if (null == btcUmic) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BTC_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BTC_UMIC});
		}
	}
	

	/**
	 * Funcion encargada de validar la obligatoriedad de los atributos de la Funcion TC.
	 * 
	 * @param fefecto
	 *            fefecto
	 * @param fcalc
	 *            fcalc
	 */
	public static void validacionCamposObligatoriosFuncionTCyTMC(final Timestamp fefecto, final Timestamp fcalc) {

		if (null == fefecto) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_EFE_POL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_EFE_POL});
		}

		if (null == fcalc) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FECHA_CALCULO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FECHA_CALCULO});
		}
	}

	/**
	 * Funcion encargada de validar el parametro criterioEdad de la Funcion nEdad.
	 * 
	 * @param criterioEdad
	 *            criterioEdad
	 */
	public static void validacionCampoCriterioEdadFuncionNEdad(final String criterioEdad) {

		if (ConstantsFunciones.CTE_CRI_FECHA_01.equals(criterioEdad) || ConstantsFunciones.CTE_CRI_FECHA_04.equals(criterioEdad)) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA3(criterioEdad, ConstantsFunciones.CTE_CRITERIO_FEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A3, new String[]{criterioEdad, ConstantsFunciones.CTE_CRITERIO_FEC});
		} else if ((null != criterioEdad && !criterioEdad.isEmpty())
				&& (!ConstantsFunciones.TIPO_CRITER_EDAD.containsKey(criterioEdad))) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA2(criterioEdad, ConstantsFunciones.CTE_CRITERIO_FEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A2, new String[]{criterioEdad, ConstantsFunciones.CTE_CRITERIO_FEC});
		}
	}

	/**
	 * Funcion encargada de validar la obligatoriedad de los parametros de la función nEdad
	 * 
	 * @param fcalc
	 *            fcalc
	 * @param fnac
	 *            fnac
	 * @param criterioEdad
	 *            criterioEdad
	 */
	public static void validacionCamposObligatoriosFuncionNEdad(final Timestamp fcalc, final Timestamp fnac, final String criterioEdad, final Timestamp fecIniRenta) {

		if (null == fcalc) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FECHA_CALCULO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FECHA_CALCULO});
		}

		if (null == fnac) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_NACI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_NACI});
		}

		if (null == criterioEdad || criterioEdad.isEmpty()) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CRITERIO_EDAD));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CRITERIO_EDAD});
		}

		if (ConstantsFunciones.CTE_CRI_FECHA_06.equals(criterioEdad) && fecIniRenta == null){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA10(criterioEdad));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A10, new String[]{criterioEdad});
		}
	}

	/**
	 * Funcion encargada de validar el campo criterio fecha de la función nAnnos
	 * 
	 * @param criterioFecha
	 *            Identifica el criterio de fechas para realizar el calculo
	 */
	public static void validacionCampoCriterioFechaFuncionNANNOS(final String criterioFecha) {

		if (ConstantsFunciones.CTE_CRI_FECHA_04.equals(criterioFecha)) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA3(criterioFecha, ConstantsFunciones.CTE_CRITERIO_FEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A3, new String[]{criterioFecha, ConstantsFunciones.CTE_CRITERIO_FEC});

		} else if ((null != criterioFecha && !criterioFecha.isEmpty())
				&& (!ConstantsFunciones.TIPO_CRITER_FECHA.containsKey(criterioFecha))) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA2(criterioFecha, ConstantsFunciones.CTE_CRITERIO_FEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A2, new String[]{criterioFecha, ConstantsFunciones.CTE_CRITERIO_FEC});
		}
	}

	/**
	 * Función encargada de validar si los parametros de entrada de la función nAnnos estan informados
	 * 
	 * @param fecha1
	 *            Identifica la primera fecha sobre la que se efectua el calculo
	 * @param fecha2
	 *            Identifica la segunda fecha sobre la que se efectua el calculo
	 * @param criterioFecha
	 *            Identifica el criterio de fechas para realizar el calculo
	 */
	public static void validacionCamposObligatoriosFuncionNANNOS(final Timestamp fecha1, final Timestamp fecha2, final String criterioFecha) {

		if (null == fecha1) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FECHA_1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FECHA_1});
		}

		if (null == fecha2) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FECHA_2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FECHA_2});
		}

		if (null == criterioFecha || criterioFecha.isEmpty()) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CRITERIO_FEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CRITERIO_FEC});
		}
	}
	
	/**
	 * Función encargada de validar que la variable de apoyo ID-CRITERIO no sea nula
	 * 
	 * @param varCriterioEdad variable con el valor de la variable de apoyo ID-CRITERIO
	 */
	public static void validarVariableDeApoyoVarCriEdad(final String varCriterioEdad) {
		
		if (null == varCriterioEdad) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA6(ConstantsFunciones.CTE_VA_CRIT_EDA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A6, new String[]{ConstantsFunciones.CTE_VA_CRIT_EDA});
		}
	}
	
	/**
	 * Función encargada de validar que la variable de apoyo ID-TEMPORAL no sea nula
	 * 
	 * @param varCriterFec variable con el de la variable de apoyo criterio de fecha
	 */
	public static void validarVariableDeApoyoVarCriterioFecha(final String varCriterFec) {
		
		if (null == varCriterFec) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA6(ConstantsFunciones.CTE_VA_CRIT_FEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A6, new String[]{ConstantsFunciones.CTE_VA_CRIT_FEC});
		}
	}
	
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función FR
	 * @param fecVcto Identifica la fecha de vencimiento.
	 * @param fcalc Identifica la fecha de cálculo.
	 */
	public static void validarParamEntradaFuncionFR(final Timestamp fecVcto, final Timestamp fcalc){
		if (null == fecVcto){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_VTO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_VTO});
		}
		
		if (null == fcalc){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FCALC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FCALC});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Alafa2T
	 * @param fecEfecto Identifica la fecha de efecto de la umic.
	 * @param fecVcto Identifica la fecha de vencimiento de la umic.
	 * @param fcalc Identifica la fecha de cálculo.
	 * @param tipo Tipo de cálculo: 
	 *		-	1: AlfaT
	 *		-	2: Alfa2
	 */
	public static void validarParamEntradaFuncionAlfaT2(final Timestamp fecEfecto, final Timestamp fecVcto, 
			final Timestamp fcalc, final Integer tipo){
		if (null == fecEfecto){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_EFECTO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_EFECTO});
		}
		
		if (null == fecVcto){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_VTO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_VTO});
		}
		
		if (null == fcalc){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FCALC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FCALC});
		}
		
		if (null == tipo){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_TIPO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_TIPO});
		}
		
		if (!(tipo == 1) && !(tipo ==2)){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA2(tipo.toString(), ConstantsFunciones.CTE_TIPO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A2, new String[]{ConstantsFunciones.CTE_TIPO, tipo.toString()});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Bgmornlgip Parte1
	 * @param x Edad actuarial a fecha efecto técnico.
	 * @param lstValoresTabMort Valores de la tabla de mortalidad.
	 * @param N Duración del seguro (desde fecha efecto técnico (en años)).
	 * @param I1 Tipo de interés.
	 * @param gipcPrima Gastos de gestión interna sobre Prima* Prima inicial del seguro.
	 * @param tcm Meses completos transcurridos desde efecto hasta cálculo provisión.
	 */
	public static void validarParamEntradaFuncionBgmornlgipParte1(final Integer x, final List<BigDecimal> lstValoresTabMort,  
			final Integer n, final BigDecimal i1, final BigDecimal gipcPrima, final Integer tcm){
		if (null == x){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});	
		}
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
	
		if (null == n){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == i1){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == gipcPrima){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GIPC_PRIMA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GIPC_PRIMA});
		}
		
		if (null == tcm){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_MESES_COMPLET));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_MESES_COMPLET});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Bgmornlgip Parte2
	 * @param alfat 
	 * @param alfat30 Factor = (alfat/30).
	 * @param factorAlfat Factor = [(1+I1)]^(-(Alfat/365))  
	 * @param alfa2
	 * @param alfa230   Factor = (alfa2/30)
	 * @param factorAlfa2 Factor = [(1+I1)]^(-(Alfa2/365))
	 * @param vrta Renta actuarial pospagable desplazada 0 meses.
	 */
	public static void validarParamEntradaFuncionBgmornlgipParte2 (final Integer alfat, final BigDecimal alfat30, 
			final BigDecimal factorAlfat, final Integer alfa2, final BigDecimal alfa230, final BigDecimal factorAlfa2, 
			final BigDecimal vrta){
	
		if (null == alfat){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_ALFAT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_ALFAT});
		}
		
		if (null == alfat30){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_ALFAT30));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_ALFAT30});
		}
		
		if (null == factorAlfat){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FACTOR_ALFAT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FACTOR_ALFAT});
		}
		
		if (null == alfa2){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_ALFA2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_ALFA2});
		}
		
		if (null == alfa230){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_ALFA230));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_ALFA230});
		}
		
		if (null == factorAlfa2){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FACTOR_ALFA2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FACTOR_ALFA2});
		}
		
		if (null == vrta){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VRTA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VRTA});
		}
	}
	
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Bgmornl
	 * @param x Edad actuarial a fecha efecto técnico.
	 * @param lstValoresTabMort Valores de la tabla de mortalidad (probabilidad Vida)
	 * @param n Duración del seguro (en años)
	 * @param i1 Tipo de interés
	 * @param tcm Meses completos transcurridos desde efecto hasta cálculo provisión
	 * @param alfa2
	 * @param alfa230 Factor = (alfa2/30)
	 * @param factorAlfa2 Factor = [(1+I1/100)] ^(-(Alfa2/365))  
	 * @param p Periodicidad del cálculo
	 */
	public static void validarParamEntradaFuncionBgmornl(final Integer x, final List<BigDecimal> lstValoresTabMort,  
			final Integer n, final BigDecimal i1, final Integer tcm, final Integer alfa2, final BigDecimal alfa230, 
			final BigDecimal factorAlfa2, final Integer p){
		if (null == x){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});	
		}
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
	
		if (null == n){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == i1){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
				
		if (null == tcm){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_MESES_COMPLET));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_MESES_COMPLET});
		}
		
		if (null == alfa2){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_ALFA2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_ALFA2});
		}
		
		if (null == alfa230){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_ALFA230));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_ALFA230});
		}
		
		if (null == factorAlfa2){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FACTOR_ALFA2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FACTOR_ALFA2});
		}
		
		if (null == p){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARP});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Lfrac
	 * @param x Edad a fecha de cálculo.
	 * @param t Anualidad en curso.
	 * @param beta Número de meses trascurridos desde el cierre hasta el momento de la proyección.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 */
	public static void validarParamEntradaFuncionLfrac(final Integer x, final Integer t, final BigDecimal beta, final List<BigDecimal> valoresTabMort,
			final Integer w){
		
		if (null == x) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == t) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == beta) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA1});
		}
		
		if (null == valoresTabMort || valoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == w) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARW));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARW});
		}
		
	}
	

	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Bvida
	 * @param x Edad actuarial a fecha efecto técnico.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param n Duración del seguro desde fecha efecto técnico (en años).
	 * @param i1 Tipo de interés.
	 * @param tcm Meses completos transcurridos desde efecto hasta cálculo provisión.
	 * @param alfat
	 * @param alfa2
	 */
	public static void validarParamEntradaFuncionBvida(final Integer x, final List<BigDecimal> valoresTabMort, final Integer n, final BigDecimal i1,
			final Integer tcm, final Integer alfat, final Integer alfa2, final BigDecimal icapini){
		
		if (null == x) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == valoresTabMort || valoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == n) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == tcm){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_MESES_COMPLET));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_MESES_COMPLET});
		}
		
		if (null == alfat){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_ALFAT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_ALFAT});
		}
		
		if (null == alfa2){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_ALFA2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_ALFA2});
		}
		
		if (null == icapini){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CAPITAL_INI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CAPITAL_INI});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función BgmornlFac Parte1
	 * @param x Edad actuarial a fecha efecto técnico.
	 * @param valoresLx Valores de la tabla de mortalidad (probabilidad de invalidez).
	 * @param n Duración del seguro (en años).
	 * @param i1 Tipo de interés.
	 * @param tcm Meses completos transcurridos desde efecto hasta cálculo provisión.
	 * @param capital Capital Actual de cartera.
	 */
	public static void validarParamEntradaFuncionBgmornlFacParte1(final Integer x, final List<BigDecimal> valoresLx, final Integer n, final BigDecimal i1,
			final Integer tcm, final BigDecimal capital){
		
		if (null == x) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == n) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == i1) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == tcm){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_MESES_COMPLET));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_MESES_COMPLET});
		}
		
		if (null == capital){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CAPITAL_GAR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CAPITAL_GAR});
		}
		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función BgmornlFac Parte2
	 * @param alfa2
	 * @param alfa230 Factor = (alfa2/30).
	 * @param factorAlfa2 Factor = 〖(1+I1/100) 〗^(-(Alfa2/365)).
	 * @param p Periodicidad del cálculo.
	 */
	public static void validarParamEntradaFuncionBgmornlFacParte2(final Integer alfa2, final BigDecimal alfa230, 
			final BigDecimal factorAlfa2, final Integer p){
		
		if (null == alfa2){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_ALFA2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_ALFA2});
		}
		
		if (null == alfa230){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_ALFA230));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_ALFA230});
		}
		
		if (null == factorAlfa2){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FACTOR_ALFA2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FACTOR_ALFA2});
		}
		
		if (null == p) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARP});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función EdadCob.
	 * @param fnac Fecha de Nacimiento
	 * @param poliza Póliza
	 * @param subpoliza Subpóliza
	 * @param certificado Certificado
	 * @param nsuscripcion Suscripcion
	 * @param criterioFecha 
	 */
	public static void validarParamEntradaFuncionEdadCob(final Timestamp fnac, final Long poliza, final Integer subpoliza, final Integer certificado,
			final Integer nsuscripcion, final String criterioFecha){
		
		if (null == fnac) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_NACI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_NACI});
		}
		
		if (null == poliza) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_POLIZA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_POLIZA});
		}
		
		if (null == subpoliza) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_SUBPOLIZA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_SUBPOLIZA});
		}
		
		if (null == certificado) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CERTIFICADO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CERTIFICADO});
		}
		
		if (null == nsuscripcion) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NSUSCRIPCION));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CERTIFICADO});
		}
		
		if (null == criterioFecha || criterioFecha.isEmpty()) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CRITERIO_FEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CRITERIO_FEC});
		}
	}
	//--------------------------------------------------------
	/**
	 * Función Función encargada de realizar la validación de obligatoriedad para la función nrta.
	 * @param nrta Duración en años de las rentas garantizadas
	 * @param fpr Número de pagos al año
	 * @param i1bti Interés del primer tramo
	 * @pram prr Porcentaje de revalorización de la renta
	 * @return
	 */
	public static void validarParamEntradaFuncionVacf(final Integer nrta,final Integer fpr,final BigDecimal i1bti, final BigDecimal prr) {
		
		if (null==nrta){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NRTA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NRTA});
		}
		
		if (null==fpr){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FPR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FPR});
		}
		
		if (null==i1bti){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_I1_BTI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_I1_BTI});
		}
		
		if (null==prr){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PRR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PRR});
		}
		
	}
	
	/**
	 * Función Auxiliar para el cálculo de la cuantía nominal de vida de Tares con prestación en forma de renta.
	 * @param NRTA Duración en años de las rentas garantizadas
	 * @param FPR Número de pagos al año
	 * @param I1bti Interés del primer tramo
	 * @pram PRR Porcentaje de revalorización de la renta
				
	 * @return
	 */
	public static void validarParamEntradaFuncionPPTppal(final BigDecimal pnaTc, final String cformapago, final Integer TC, final Integer T,
			final BigDecimal pprUmic, final BigDecimal primaTarada, final BigDecimal primaIni){
				
		if (null==pnaTc){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PNATC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PNATC});
		}
		
		if (null==cformapago){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CFORMAPAGO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CFORMAPAGO});
		}
		
		if (null==TC){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_TC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_TC});
		}
		
		if (null==T){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_T});
		}
		
		if (null==pprUmic){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PPRUMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PPRUMIC});
		}
		
		if (null==primaTarada){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PRIMA_TARADA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PRIMA_TARADA});
		}
		if (null==primaIni){
				if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
					ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PRIMA_INI));
				}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PRIMA_INI});
		}
		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función tarifaXRenoHasta.
	 * @param factorI1
	 * @param gic
	 * @param factorGasto
	 * @param probXRenoHasta
	 * @param spCom
	 * @return Double
	 */
	public static void validarParamEntradaFuncionTarifaXRenoHasta (final BigDecimal factorI1      , 
			                                                       final BigDecimal gic           ,
			                                                       final BigDecimal factorGasto   ,
			                                                       final BigDecimal probXRenoHasta,
			                                                       final String     spCom         ){
		
		if (null == factorI1) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FACTOR_I1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FACTOR_I1});
		}
		
		if (null == gic) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GIC});
		}
		
		if (null == factorGasto) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FACTOR_GASTO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FACTOR_GASTO});
		}
		
		if (null == probXRenoHasta) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PROB_X_RENOV_HASTA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PROB_X_RENOV_HASTA});
		}
		
		if (null == spCom) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_SP_COM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_SP_COM});
		}
		
	}
	
	public static void validarParamEntradaFuncionCuota(Integer j, Integer nc, Integer pc, BigDecimal iF,
			BigDecimal delta, BigDecimal cp0, Integer na) {
		
		if (null==j){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_J});
		}	
		if (null==nc){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NC});
		}
		if (null==pc){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PC});
		}
		if (null==iF){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_IF));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_IF});
		}
		if (null==delta){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DELTA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DELTA});
		}
		if (null==cp0){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CP0));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CP0});
		}
		if (null==na){
			if(ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()){
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NA});
		}
		
	}
	
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función delta.
	 * @param npa numero o periodicidad de pagos en la amortización
	 * @param g crecimiento de la cuota amortización
	 * @param mes1 Meses de pagos adicionales. Se considerará su valor como cero (ya que actualmente ningún producto considera pagos extraordinarios).
	 * @param mes2 Meses de pagos adicionales. Se considerará su valor como cero (ya que actualmente ningún producto considera pagos extraordinarios).
	 * @param If interés financiero del préstamo
	 */
	public static void validarParamEntradaFuncionDelta(final Integer na, final Integer npa, final BigDecimal g, final Integer mes1, final Integer mes2, final BigDecimal If){
		if(null == na){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NA});
		}
		if(null == npa){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NPA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NPA});
		}
		if(null == g){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_G));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_G});
		}
		if(null == mes1){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_MES1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_MES1});
		}
		if(null == mes2){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_MES2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_MES2});
		}
		if(null == If){
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_IF));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_IF});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función PPCppal.
	 * @param varPRP
	 * @param varPrimaIni
	 * @param varTC
	 */
	public static void validarParamEntradaFuncionPPCppal (final BigDecimal varPRP      , 
			                                                       final BigDecimal varPrimaIni           ,
			                                                       final Integer vartc){
		
		
		if (null == varPRP) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PRP});
		}
		if (null == varPrimaIni) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_IPRIMANETA_INI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_IPRIMANETA_INI});
		}
		if (null == vartc) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TC});
		}
	}	
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función BRNM.
	 * @param j
	 * @param c1bbc
	 * @param edadExp
	 */
	public static void validarParamEntradaFuncionBRNM (final Integer j, 
													   final BigDecimal c1bbc, 
													   final BigDecimal edadExp){
		
		
		if (null == j) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_J});
		}
		if (null == c1bbc) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_C1BBC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_C1BBC});
		}
		if (null == edadExp) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_EDADEXP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_EDADEXP});
		}
	}	
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función CTA.
	 * @param j
	 * @param c1bbc
	 * @param edadExp
	 */
	public static void validarParamEntradaFuncionCTA (final Integer j, 
													  final BigDecimal c1bbc, 
													  final BigDecimal edadExp){
		
		
		if (null == j) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_J});
		}
		if (null == c1bbc) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_C1BBC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_C1BBC});
		}
		if (null == edadExp) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_EDADEXP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_EDADEXP});
		}
	}	
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función PUPM.
	 * @param j
	 * @param lstValoresTabMort
	 * @param x
	 */
	public static void validarParamEntradaFuncionPUPM (final Integer j, 
													   final List<BigDecimal> lstValoresTabMort, 
													   final BigDecimal x,
													   final Timestamp fecJ,
													   final String criterioedad,
													   final Timestamp fnac){
		
		
		if (null == j) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_J});
		}
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		if (null == x) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_EDAD_ASEG));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_EDAD_ASEG});
		}
		if (null == fecJ) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_J});
		}
		if (null == criterioedad) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CRITERIO_EDAD));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CRITERIO_EDAD});
		}
		if (null == fnac) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_NACI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_NACI});
		}
		
		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función CPP.
	 * @param j
	 * @param c1bbc
	 * @param edadExp
	 * @param lstValoresTabMort
	 * @param x
	 */
	public static void validarParamEntradaFuncionCPP (final Integer j,
													  final BigDecimal edadExp,
													  final List<BigDecimal> lstValoresTabMort, 
													  final BigDecimal x,
													  final Timestamp fecJ,
													  final String criterioedad,
													  final Timestamp fnac){
		
		
		if (null == j) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_J});
		}
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		if (null == x) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_EDAD_ASEG));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_EDAD_ASEG});
		}
		if (null == edadExp) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_EDADEXP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_EDADEXP});
		}
		if (null == fecJ) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_J});
		}
		if (null == criterioedad) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CRITERIO_EDAD));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CRITERIO_EDAD});
		}
		if (null == fnac) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_NACI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_NACI});
		}
	}	
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función INCR.
	 * @param j
	 * @param fecJ
	 * @param Tabla2000
	 * @param nva
	 * @param kbencon
	 * @param aInicio
	 */
	public static void validarParamEntradaFuncionINCR (final Integer j, 
													   final Timestamp fecJ, 
													   final BigDecimal Tabla2000,  
													   final String kbencon, 
													   Integer aInicio){
		
		
		if (null == j) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_J});
		}
		
		if (null == fecJ) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_J));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_J});
		}
		if (Tabla2000 == null) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_TABLA_2000));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_TABLA_2000});
		}
		
		if (kbencon.equals(null)) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_KBENCON));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_KBENCON});
		}
		if (null == aInicio) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_ANIOINICIO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_ANIOINICIO});
		}
	}	
	
	public static void validarParamEntradaFuncionRNoDep (final Integer varTCm, 
			 											 final Integer varBeta, 
			 											 final Integer x,  
			 											 final List<BigDecimal> lstValoresTabMort, 
			 											 final List<BigDecimal> lstValoresTabMort3,
			 											 final Integer w, 
			 											 final BigDecimal tit1){


		if (null == varTCm) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_TCM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_TCM});
		}
		
		if (null == varBeta) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA});
		}
		
		if (null == w) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_WX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_WX});
		}
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == lstValoresTabMort3 || lstValoresTabMort3.isEmpty()) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == x) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_EDAD_ASEG));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_EDAD_ASEG});
		}
		
		if (null == tit1) {
			if (ValidacionesFuncionesAuxiliares.LOG.isDebugEnabled()) {
				ValidacionesFuncionesAuxiliares.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
	}
	
}
