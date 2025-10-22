package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Asegurados;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TablaConversion;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesActualizacionFinanciera;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.FuncionesRentas;
import es.mapfre.solvencia.formulacion.util.FuncionesVBX;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.servicios.impl.ObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Es una variación del módulo CSP363, pero partiendo desde la fcal en vez de una fcierta, 
 * y forzando el cálculo del Vzc2 con tabla generacional. La tabla generacional a utilizar será informada en la nueva tabla de Datos Sepi/Endesa.
 * Este módulo debe distinguir si el titular está en vigor o anulado por motivo distinto a 17
 * @author 
 *
 */
public class ModuloCSP327 implements Modulo {

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP327.class);
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP327; 
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

	private static final String CLAVE_VA_CRIT_EDA = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_VA_CRIT_EDA.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_WX = ConstantsModulos.CTE_VAR_WX.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_EDAD_MAXIMA_2 = ConstantsModulos.CTE_VAR_EDAD_MAXIMA_2.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_FECHA_VCTO = ConstantsModulos.CTE_VAR_FECHA_VCTO.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_ASUBX = ConstantsModulos.CTE_VAR_ASUBX.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_MODBETA = ConstantsModulos.CTE_VA_MODBETA.concat(CLAVE_MODULO);

	private static final String CLAVE_VAR_ANT_RENOVA = ConstantsModulos.CTE_VAR_ANT_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PROX_RENOVA = ConstantsModulos.CTE_VAR_PROX_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_NR = ConstantsModulos.CTE_VAR_NR.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_M = ConstantsModulos.CTE_VAR_M.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_W = ConstantsModulos.CTE_VAR_W.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	
	private static final String CLAVE_VZC2 = ConstantsModulos.CTE_VZC2;
	private static final String CLAVE_VAR_VZC2 = CLAVE_VZC2.concat(CLAVE_MODULO);	
	private static final String CLAVE_UMIC2 = ConstantsModulos.CTE_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC2 = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VZC2H = ConstantsModulos.CTE_VZC2H;
	private static final String CLAVE_VAR_VZC2H = CLAVE_VZC2H.concat(CLAVE_MODULO);
	private static final String CLAVE_VZC2M = ConstantsModulos.CTE_VZC2M;
	private static final String CLAVE_VAR_VZC2M = CLAVE_VZC2H.concat(CLAVE_MODULO);
	
	private static final String CLAVE_NPP = ConstantsModulos.CTE_VA_NPP;
	private static final String CLAVE_VAR_NPP = CLAVE_NPP.concat(CLAVE_MODULO);


	
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
			if (ModuloCSP327.LOG.isTraceEnabled()) {
				ModuloCSP327.LOG.trace("Inicio de execute en clase ModuloCSP327");
			}
			
			//Inicio recuperación de datos que le pasaremos a la función moduloCSP333
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			//Fin de la recuperación de los datos que se pasarán a la función moduloCSP333.
			
			//Invocamos a la función de calculo CSP327
			resultado = moduloCSP327(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP327.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP327.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP327.LOG.isTraceEnabled()) {
			ModuloCSP327.LOG.trace("Fin de execute en clase ModuloCSP327");
		}
		
		return resultado;
		
	}

	/** 
	 * Es una variación del módulo CSP363, pero partiendo desde la fcal en vez de una fcierta, 
	 * y forzando el cálculo del Vzc2 con tabla generacional. La tabla generacional a utilizar será informada en la nueva tabla de Datos Sepi/Endesa.
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
	 * @param codSubproceso
	 *            Código del subproceso que se está ejecutando
	 */
	private BigDecimal moduloCSP327(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {

		//Variables locales
		String varCriterFecha = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriFec = ConstantsFunciones.CTE_CADENA_VACIA;
		Timestamp varffincas = null;
		BigDecimal varGic;
		BigDecimal varPcoaseg = null;
		BigDecimal varCapitalBono;
		BigDecimal varVzc = null;
		BigDecimal varNpp = null;
		BigDecimal varActvcto = null;
		Timestamp varfechaEfecto = null;
		BigDecimal varCSP327 = BigDecimal.ZERO;
		BigDecimal varI1 = BigDecimal.ZERO;
		BigDecimal varI2 = BigDecimal.ZERO;
		Modulo moduloVZCVCTO = null;
		Modulo moduloACTVCTO = null;
		List<DetalleCorriente> varProyC2 = proyUmic;	
		List<DetalleCorriente> varProyVzc = proyUmic;
		List<DetalleCorriente> varProyActvcto = proyUmic;
		String varTmX;
		
		List<BigDecimal> varValoresTabMortX;
		//Fin variables locales
		
		if (ModuloCSP327.LOG.isTraceEnabled()) {
			ModuloCSP327.LOG.trace("Inicio función << moduloCSP333 >> de la clase ModuloCSP333, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if (null == bloqueCorriente.getFechaDevengo()) {
			return varCSP327;
		}
		
		varfechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
	
		varCriterFecha = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);	
		
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFecha);
			// Fin de la validación de las variables de apoyo.
		}

		// Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		// Cálculo de las variables de apoyo.
		varCriEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD,
				umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(),
				umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_VA_CRIT_EDA);
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(),
				umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		// Fin del cálculo de las variables de apoyo.

		// Validación de las variables de apoyo.
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la
		// forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriEdad);
		} // Si llega a este punto las validaciones anteriores serán válidas para la
		// iteración inicial, luego serán válidas para las siguientes.
		// Fin de la validación de las variables de apoyo.

		// Variables de módulo
		
		if (umic.getDatosGenerales().getCsitupol().equals("VI")) {
			if (umic.getDatosGenerales().getCnegocio().equals("I")) {
				varfechaEfecto = umic.getFechas().getFecefecini();
			} else {
				varfechaEfecto = umic.getFechas().getFecinisus();
			}
		}
		// Si la póliza está reducida
		if (umic.getDatosGenerales().getCsitupol().equals("RE")) {
			varfechaEfecto = umic.getFechas().getFecefecred();
			if (null==umic.getFechas().getFecefecred()
					& umic.getDatosGenerales().getCnegocio().equals("C")) {
				varfechaEfecto = umic.getFechas().getFecfinpagprim();
			}
		}
		varNpp = UtilModulos.getVarNpp(mapVariables, CLAVE_VAR_NPP, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_NPP);
		if (varNpp == null) {
			varNpp = BigDecimal.valueOf(12);
		}
		
		varI1 = btcUmic.getItcalc().get(0);
		varI2 = btcUmic.getItcalc().get(1);
		if (varI2 != null && varI2.compareTo(BigDecimal.ZERO) > 0) {
			varffincas = btcUmic.getFecfintramo().get(1);
		}
		varGic = btcUmic.getGtorosspCap();
		
		if(null == varGic ){
			varGic = BigDecimal.ZERO;
		}
		
		varCapitalBono = umic.getCapitales().getIcapact();
		varPcoaseg = umic.getDatosCoaseguro().getPcoaseg().divide(new BigDecimal(100));
		varProyVzc = proyUmic;
		varProyActvcto = proyUmic;
		varTmX = btcUmic.getTablacalc1aseg1();
		
		
		//varValoresTabMortX = obtenerConfiguracion.recuperarValoresExperiencia(btcUmic.getFecCierre(), varTmX, varAnoNacX, varI1, umic.getBti().getPsobremort(), umic.getBti().getPriesgo());
		varValoresTabMortX = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriEdad, ConstantesSolvencia.CTE_TABMORT_L);
		moduloVZCVCTO = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZCVCTO);
		varVzc = (BigDecimal) moduloVZCVCTO.execute(varProyVzc, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,codSubproceso);
		moduloACTVCTO = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_ACTVCTO);
		varActvcto = (BigDecimal) moduloACTVCTO.execute(varProyActvcto, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,codSubproceso);
		
		//varCSP327 = varCapitalBono.multiply((varLxvto.divide(varLxDesde, ConstantsFunciones.MATH_CONTEXT).multiply(varActFin)).divide((BigDecimal.ONE.subtract(varSumatorio)), ConstantsFunciones.MATH_CONTEXT).add((varGic.multiply(varax))));
		varGic = varGic.divide(new BigDecimal(100), ConstantsFunciones.MATH_CONTEXT);
		
		if(umic.getDatosGenerales().getSpcom().equals("B")){
			varCSP327 = varCapitalBono.multiply(varActvcto.multiply(varVzc.multiply(BigDecimal.ONE.add(varGic.divide(varNpp, ConstantsFunciones.MATH_CONTEXT)))));
		}else {
			varCSP327 = varCapitalBono.multiply(varPcoaseg.multiply(varActvcto.multiply(varVzc.multiply(BigDecimal.ONE.add(varGic.divide(varNpp, ConstantsFunciones.MATH_CONTEXT))))));
		}
		
		if (ModuloCSP327.LOG.isTraceEnabled()) {
			ModuloCSP327.LOG.trace("Fin función << moduloCSP327 >> de la clase ModuloCSP327, para la iteracion = {}, con resultado csp327 = {}", iteracion, varCSP327);
		}
	
		return varCSP327;
	}
	
}
