package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo que devuelve los factores de probabilización a aplicar en  el cálculo de la cuantía probable
 * de una garantía.
 * Este módulo determina la probabilidad de que una cabeza de edad zc en la fecha de cálculo, fcal, llegue vivo y se produzca 
 * el fallecimiento accidental en la fecha j en la que se devenga la prestación C(i,j).
 * La expresión matemática para su determinación es la siguiente:
 * 			COMPL(fcal, j) = (l(zc+naños(fcal,j)) / l(zc)) * (COMPL(zc+naños(fcal,j))
 * 
 * @author apedro
 *
 */
public class ModuloCOMPLPTOZC implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCOMPLPTOZC.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_COMPLPTOZC;
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_NPP = ConstantsModulos.CTE_VA_NPP;
	private static final String CLAVE_VAR_NPP = CLAVE_NPP.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_INV = ConstantsModulos.CTE_VAR_VAL_TAB_INV.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC;
	private static final String CLAVE_VAR_VAL_TAB_INV_SCR = ConstantsModulos.CTE_VAR_VAL_TAB_INV_SCR.concat(CLAVE_MODULO);
	// Fin de las variables estáticas usadas para agilizar operaciones.

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}
	
	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la llamada a la función que realiza los calculos del modulo.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		//Fin variables locales
		
		try {
			if (ModuloCOMPLPTOZC.LOG.isTraceEnabled()) {
				ModuloCOMPLPTOZC.LOG.trace("Inicio de execute en clase ModuloCOMPLPTOZC");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCOMPLPTOZC
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de datos.
			
			//Invocamos a la función moduloCOMPLPTOZC
			resultado = moduloCOMPLPTOZC(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCOMPLPTOZC.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCOMPLPTOZC.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCOMPLPTOZC.LOG.isTraceEnabled()) {
			ModuloCOMPLPTOZC.LOG.trace("Fin de execute en clase ModuloCOMPL");
		}
		
		return resultado;
	}
	
	/**
	 * Modulo de cálculo que devuelve los factores de probabilización a aplicar en  el cálculo de la cuantía probable de 
	 * una garantía.  
	 * El módulo COMPL(fcal,j) determina la probabilidad de que una cabeza de edad zc en la fecha de cálculo, fcal, llegue vivo 
	 * y se produzca el fallecimiento accidental en la fecha j en la que se devenga la prestación C(i,j).
	 * 
	 * @param proyUmic
	 * 			Estructura detalleCorrientes de la umic
	 * @param bloqueCorriente
	 * 			Bloque de Trabajo de la corriente
	 * @param iteracion
	 * 			Indica el periodo de proyección J que se está calculando de entre todos los periodos de proyección de la umic (proyUmic)
	 * @param fcalc
	 * 			Fecha de calculo
	 * @param umic
	 * 			Contiene los datos de la Umic que se está procesando
	 * @param btcUmic
	 * 			Contiene el detalle de la base técnica de cálculo para la umic
	 * @param mapVariables 
	 * 			mapa con las variables de memoria necesarias
	 */
	private BigDecimal moduloCOMPLPTOZC(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal complptozc = BigDecimal.ZERO;
		Timestamp varfecefec=null;
		String varCriFec;
		String varCriterioEdad;
		BigDecimal varNPP;
		BigDecimal varEdadCalc;
		BigDecimal varFracc0;
		BigDecimal varZc;
		List<BigDecimal> varValoresTabMort;
		BigDecimal varLzc;
		List<BigDecimal> varValoresTabInv = null;
		//Fin variables locales
		
		if (ModuloCOMPLPTOZC.LOG.isTraceEnabled()) {
			ModuloCOMPLPTOZC.LOG.trace("Inicio función << moduloCOMPLPTOZC >> de la clase ModuloCOMPLPTOZC, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Obtención y validación de los criterios de edad y fecha.
		varCriterioEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterioEdad);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
		}
		// Fin de la obtención y validación de los criterios de edad y fecha.
		
		
		// Si no tiene fecha de devengo no se calcula
		if(bloqueCorriente.getFechaDevengo() == null ) {
			return complptozc;
		}
		
		
		//Variables Módulo
		
		varfecefec = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		varNPP = UtilModulos.getVarNpp(mapVariables, CLAVE_VAR_NPP, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_NPP);
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		varEdadCalc = FuncionesAuxiliares.nEdad(varfecefec, umic.getAsegurados().getFnacAseg1(), varCriterioEdad, umic.getRentas().getFecIni(), varEdifer);
		varFracc0 = FuncionesAuxiliares.nAnnos(varfecefec, fcalc, varCriFec);
		varZc = varEdadCalc.add(varFracc0);
		varValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterioEdad, ConstantesSolvencia.CTE_TABMORT_L);
		
		varLzc = Util.getVarLx(varZc, varValoresTabMort);
		
		varValoresTabInv = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_INV, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterioEdad, ConstantesSolvencia.CTE_TABMORT_I);
		
		BigDecimal varFraccj = FuncionesAuxiliares.nAnnos(fcalc, bloqueCorriente.getFechaDevengo(), varCriFec);
		BigDecimal varEdadJ = varZc.add(varFraccj);
		
		if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC)){
			// Se estresa incapacidad
			varValoresTabInv = UtilModulos.getValValoresIxSCRINC(mapVariables, CLAVE_VAR_VAL_TAB_INV_SCR, umic, btcUmic, varValoresTabInv, varZc);
		}
		
		BigDecimal varLj = Util.getVarLx(varEdadJ, varValoresTabMort);
		BigDecimal varTasaJ;
		
		if(varEdadJ.intValue() < 130){
			varTasaJ = varValoresTabInv.get(varEdadJ.intValue()).multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_001);
		}else{
			varTasaJ = BigDecimal.ZERO;
		}
		BigDecimal varComplzcj = varTasaJ.divide(varNPP, ConstantsFunciones.MATH_CONTEXT);
		
		//Se calcula COMPLFPTOZC (j)  = varLj/(varLzc ) * varComplzcJ
		if(!(varLzc.setScale(34, RoundingMode.HALF_UP).equals(BigDecimal.ZERO.setScale(34, RoundingMode.HALF_UP)))){
			complptozc = (varLj.divide(varLzc, ConstantsFunciones.MATH_CONTEXT)).multiply(varComplzcj);
		}else{
			complptozc = BigDecimal.ZERO;
		}
		if (null != umic.getFechas().getFecefecfin() &&
				umic.getFechas().getFecefecfin().equals(bloqueCorriente.getFechaDevengo())&&
				!bloqueCorriente.getFechaPago().before(umic.getFechas().getFecefecfin())){
			complptozc = BigDecimal.ZERO;
		}
		
		if (ModuloCOMPLPTOZC.LOG.isTraceEnabled()) {
			ModuloCOMPLPTOZC.LOG.trace("Fin función << moduloCOMPLPTOZC >> de la clase ModuloCOMPLPTOZC, para la iteracion = {}, con resultado complptozc = {}", iteracion, complptozc);
		}
			
		return complptozc;
	}

}
