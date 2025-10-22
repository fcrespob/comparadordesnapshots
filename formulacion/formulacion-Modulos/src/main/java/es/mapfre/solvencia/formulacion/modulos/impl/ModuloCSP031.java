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
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del calculo que devuelve la cuantía de fallecimiento de la garantía principal.
 * La expresión matemática para su determinación es la siguiente:
 *			CSP(031,tc) = PN0 * (1+Ifal/100)^Tc+1
 *
 * @author apedro
 *
 */
public class ModuloCSP031 implements Modulo {
	
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP031.class);
	
	// Variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP031;
	
	private static final String CLAVE_IFAL = ConstantsModulos.CTE_VA_IFAL;
	private static final String CLAVE_VAR_IFAL = CLAVE_IFAL.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TC = ConstantsModulos.CTE_VAR_TC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_UNO_MAS_VAR_IFAL = ConstantsModulos.CTE_UNO_MAS_VAR_IFAL.concat(CLAVE_MODULO);
	private static final String CLAVE_RESULTADO = "Resultado".concat(CLAVE_MODULO);
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
			if (ModuloCSP031.LOG.isTraceEnabled()) {
				ModuloCSP031.LOG.trace("Inicio de execute en clase ModuloCSP031");
			}
			
			//Recuperación de los datos que se pasarán a la función moduloCSP031.
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubProceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			//Fin de la recuperación de los datos que se pasarán a la función moduloCSP031.
			
			//Invocación de la función moduloCSP031
			resultado = moduloCSP031(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubProceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP031.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP031.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP031.LOG.isTraceEnabled()) {
			ModuloCSP031.LOG.trace("Fin de execute en clase ModuloCSP031");
		}
		
		return resultado;
	}
	
	/**
	 * Módulo de cálculo que devuelve la cuantía de fallecimiento de la garantía principal.
	 * La expresión matemática para su determinación es la siguiente:
	 *			CSP(031,tc) = PN0 * (1+Ifal/100)^Tc+1
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
	private BigDecimal moduloCSP031(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubProceso) {
		//Variables locales
		Timestamp varFechaEfecto = null;
		BigDecimal varIFal = BigDecimal.ZERO;
		BigDecimal varPN0;
		BigDecimal csp031 = BigDecimal.ZERO;
		Integer varTC;
		Integer varTCaux;
		BigDecimal varCSP031;
		//Fin variables locales
		
		if (ModuloCSP031.LOG.isTraceEnabled()) {
			ModuloCSP031.LOG.trace("Inicio de la función << moduloCSP031 >> para la iteración = {}", iteracion);
		}
				
		//Validación de los campos de entrada.
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Si umic.fechas.fecefecfin es nulo se devuelve error funcional A2 -  Valor nulo  incorrecto para el dato umic.fechas.fecefecfin
		if (umic.getFechas().getFecefecfin() == null){
			if (ModuloCSP031.LOG.isDebugEnabled()) {
				ModuloCSP031.LOG.debug(Util.errorValidacionA2(umic.getFechas().getFecefecfin().toString(), ConstantsModulos.CTE_FEC_EFEC_FIN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A2, new String[]{ConstantsModulos.CTE_FEC_EFEC_FIN});
		}
		/**
		 	Para todas las iteraciones el proceso es el mismo.
		 	- Variables de Apoyo 
				-	VarIfal --> obtenerConfiguracion.recuperarVariableApoyo(IFAL)
			- Otras variables del módulos:
				- varfechaEfecto = umic.fechas fecinisus (se almacena para su uso posterior).
				- varBloque = obtenerConfiguracion.getBloqueBySubproceso(proyUmic, codSubproceso).
				- varTC = 	TC(varfechaEfecto, proyUmic(j).fecDesde
			- Se calcula csp031 como -- > CSP031(j) = varPN0 * (1+varIfal/100)^varTC+1
		 */
		
		//Cálculo de las variables que no varían por período
		varPN0 = umic.getPrimas().getIprimanetaini();
		
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
			return csp031;
		}
		
		// Cálculo de las variables auxiliares necesarias para el cálculo
		
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		varTC = (Integer) mapVariables.get(CLAVE_VAR_TC);
		final BigDecimal unoMasVarIFal = UtilModulos.getVarUnoMasVarIFal(mapVariables, CLAVE_VAR_UNO_MAS_VAR_IFAL, varIFal);
		
		if (varTC == null){
			varTC = FuncionesAuxiliares.tc(varFechaEfecto, proyUmic.get(iteracion-1).getFechaDesde());
			mapVariables.put(CLAVE_VAR_TC,varTC);
			//Cálculo de la fórmula: csp031 = varPN0 * (1+(varIfal/100)) ^ varTC+1
			csp031 = varPN0.multiply(Util.pow(unoMasVarIFal, varTC+1));
			mapVariables.put(CLAVE_RESULTADO, csp031);
		} else {
			varTCaux = FuncionesAuxiliares.tc(varFechaEfecto, proyUmic.get(iteracion-1).getFechaDesde());
			varCSP031 = (BigDecimal) mapVariables.get(CLAVE_RESULTADO);
			if (!varTC.equals(varTCaux)){ //Si el valor de tc ha cambiado se calcula csp031:
				mapVariables.put(CLAVE_VAR_TC,varTCaux); 
				//Cálculo de la fórmula: csp031 = varCSP031* (1+(varIfal/100))
				csp031 = varCSP031.multiply(unoMasVarIFal);
				mapVariables.put(CLAVE_RESULTADO, csp031);
			} else { //Si no ha cambiado se iguala csp031 al resultado almacenado anteriormente
				csp031 = varCSP031;
			}
		}

		if (ModuloCSP031.LOG.isTraceEnabled()) {
			ModuloCSP031.LOG.trace("Fin de la función << moduloCSP031 >> para la iteración = {}, con resultado csp031 = {}", iteracion, csp031);
		}
		
		return csp031;
	}
	

}
