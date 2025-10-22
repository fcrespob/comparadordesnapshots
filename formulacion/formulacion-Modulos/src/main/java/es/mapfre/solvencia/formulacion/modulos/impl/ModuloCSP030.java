package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.FuncionesVBX;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del calculo que devuelve la cuantía de fallecimiento de la garantía principal de la póliza.
 * Supone la valoración de las aportaciones extraordinarias
 * La expresión matemática para su determinación es la siguiente:
 *			CSP(030,tc)=  PUCCAP(Tc)  
 *
 * @author apedro
 *
 */
public class ModuloCSP030 implements Modulo {
	
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP030.class);
	
	// Variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP030;
	
	
	private static final String CLAVE_IFAL = ConstantsModulos.CTE_VA_IFAL;
	private static final String CLAVE_VAR_IFAL = CLAVE_IFAL.concat(CLAVE_MODULO);
	
	
	private static final String CLAVE_VAR_TC0 = ConstantsModulos.CTE_VAR_TC0.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	// Fin de las variables estáticas para agilizar operaciones.
	
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
			if (ModuloCSP030.LOG.isTraceEnabled()) {
				ModuloCSP030.LOG.trace("Inicio de execute en clase ModuloCSP030");
			}
			
			//Recuperación de los datos que se pasarán a la función moduloCSP030.
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubProceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			//Fin de la recuperación de los datos que se pasarán a la función moduloCSP030.
			
			//Invocación de la función moduloCSP030
			resultado = moduloCSP030(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubProceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP030.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP030.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP030.LOG.isTraceEnabled()) {
			ModuloCSP030.LOG.trace("Fin de execute en clase ModuloCSP030");
		}
		
		return resultado;
	}
	
	/**
	 * Módulo de cálculo que devuelve la cuantía de fallecimiento de la garantía principal de la póliza.
	 * Supone la valoración de las aportaciones extraordinarias.
	 * La expresión matemática para su determinación es la siguiente:
	 *			CSP(030,tc)=  PUCCAP(Tc)
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
	 * 			código del sub proceso en ejecución
	 */
	private BigDecimal moduloCSP030(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubProceso) {
		//Variables locales
		Timestamp varFechaEfecto = null;
		BigDecimal varIFal = BigDecimal.ZERO;
		Integer varTC0;
		Integer varTcm;
		Integer varTC;
		Integer varBeta;
		BigDecimal varPpr;
		BigDecimal varPpcap = BigDecimal.ZERO;
		BigDecimal csp030 = BigDecimal.ZERO;
		//Fin variables locales
		
		if (ModuloCSP030.LOG.isTraceEnabled()) {
			ModuloCSP030.LOG.trace("Inicio de la función << moduloCSP029 >> para la iteración = {}", iteracion);
		}
				
		//Validación de los campos de entrada.
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Si la póliza está anulada, es decir  umic.datosGenerales.csitupol=  ‘AN’ Se deberá retornar error funcional A4 
		if (umic.getDatosGenerales().getCsitupol().equals(ConstantsModulos.CTE_DG_CSITU_ANU)){
			if (ModuloCSP030.LOG.isDebugEnabled()) {
				ModuloCSP030.LOG.debug(Util.errorValidacionA7(umic.getKey().toString()));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A7, new String[]{umic.getKey().toString()});
		}

		
		// Definición de variables auxiliares para agilizar las operaciones (se llaman varias veces).
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		// Fin de la definición de las variables auxiliares.
		
		// Cálculo de las variables de apoyo.
		varIFal = UtilModulos.getVarIFal(mapVariables, CLAVE_VAR_IFAL, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_IFAL, umic.getBti().getPintertecnI1());
		// Fin del cálculo de las variables de apoyo.
		
		// Validación de las variables de apoyo.
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarIFal(varIFal);
		} // Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		// Fin de la validación de las variables de apoyo.

		
		if(bloqueCorriente.getFechaDevengo() == null || bloqueCorriente.getFechaDevengo().after(umic.getFechas().getFecefecfin()) || 
				!proyUmic.get(iteracion-1).getFechaDesde().before(umic.getFechas().getFecefecfin())) {
			return csp030;
		}
		
		//Variables modulo
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		varTcm = UtilModulos.getVarTCm(mapVariables, CLAVE_VAR_TCM, iteracion, varFechaEfecto, proyUmic.get(0).getFcierre());
		varTC0 = UtilModulos.getVarTC0(mapVariables, CLAVE_VAR_TC0, varFechaEfecto, fcalc);
		varPpcap = umic.getPrimas().getPpcap();
		varPpr = umic.getPrimas().getPpr();
		
		varTC = FuncionesAuxiliares.tc(varFechaEfecto, proyUmic.get(iteracion-1).getFechaDesde());
		varBeta = FuncionesAuxiliares.tcm(fcalc, proyUmic.get(iteracion-1).getFechaDesde());

		//Se calcula CSP030 = PUCCAP (varTC, varTCm, varbeta, vartc0, varIfal, varPpcap)
		csp030 = FuncionesVBX.puccap(varTC, varTcm, varBeta, varTC0, varIFal, varPpcap, varPpr);

		if (ModuloCSP030.LOG.isTraceEnabled()) {
			ModuloCSP030.LOG.trace("Fin de la función << moduloCSP030 >> para la iteración = {}, con resultado csp030 = {}", iteracion, csp030);
		}
		
		return csp030;
	}
	

}
