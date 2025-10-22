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
 * Clase encargada del calculo que devuelve la Valoracion del reembolso de primas en productos 
 * GEOMETRICOS reducidos con reembolso de intereses por años transcurridos.
 * La expresión matemática para su determinación es la siguiente:
 *		 	CSP(005,tc) = fall_Red(0)*(1+Ifal/100)^ Tc
 *
 * @author rschacon
 *
 */
public class ModuloCSP005 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP005.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP005;
	
	private static final String CLAVE_IFAL = ConstantsModulos.CTE_VA_IFAL;
	private static final String CLAVE_VAR_IFAL = CLAVE_IFAL.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
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
			
			if (ModuloCSP005.LOG.isTraceEnabled()) {
				ModuloCSP005.LOG.trace("Inicio de execute en clase ModuloCSP005");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCSP005
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			
			//Invocamos a la función moduloCSP005
			resultado = moduloCSP005(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloCSP005.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP005.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP005.LOG.isTraceEnabled()) {
			ModuloCSP005.LOG.trace("Fin de execute en clase ModuloCSP005");
		}
		
		return resultado;
	}
	
	/**
	 * Modulo de calculo que devuelve la Valoracion del reembolso de primas en productos 
	 * GEOMETRICOS reducidos con reembolso de intereses por años transcurridos.
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
	private BigDecimal moduloCSP005(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//variables locales
		int varNumAnua;
		Timestamp varFechaEfecto = null;
		BigDecimal csp005 = BigDecimal.ZERO;
		BigDecimal varfallRed = BigDecimal.ZERO;
		String varCriFec = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal varIFal = BigDecimal.ZERO;
		//Fin variables locales
		
		if (ModuloCSP005.LOG.isTraceEnabled()) {
			ModuloCSP005.LOG.trace("Inicio de la función << moduloCSP005 >> de la clase moduloCSP005, para la iteración = {}", iteracion);
		}
		
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		/**
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, 
		 * así como las varibales internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso 
		 * por los siguientes periodos a calcular. 

			-	varfallRed = umic.capitales.icapfall --> dejo la variable en memoria disponible para el procesado de la umic.
			Variables de Apoyo
			-	Var Ifal --> obtenerConfiguracion.recuperarVariableApoyo(IFAL)
			-	VarCriterFec -->    obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
			-	Si la variable de apoyo  retornada es nula se devuelve error funcional 005 - No se ha encontrado la Variable de Apoyo IFAL ,finalizando el proceso para la UMIC.
			
			Variables Módulo
			
			o	varfechaEfecto = umic.fechas.fecefecred
			o	varfechaEfecto --> dejo la variable en memoria, disponible para el subproceso de la umic.
			
			Establecido el efecto de  la umic calculamos las anualidades transcurridas desde varfechaEfecto hasta la fecha de cálculo, 
			invocando a  la función auxiliar TC: 
			
			varTC = TC(varfechaEfecto, fcalc)
			varTC --> dejo la variable en memoria, disponible para el subproceso de la umic.
			
			Para cualquier periodo j se calculará: 
			varNumAnualidades= varTC +  naños(fcalc, proyUmic(j).varBloque.fecDevengo, VarCriterFec).
			
			varNumAnualidades  --> sobreescribo la variable en memoria, disponible para el subproceso.

		 */
		
		
		// Cálculo de las variables de apoyo.
		varIFal = UtilModulos.getVarIFal(mapVariables, CLAVE_VAR_IFAL, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_IFAL, umic.getBti().getPintertecnI1());
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		// Fin del cálculo de las variables de apoyo.
		
		// Validación de las variables de apoyo.
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarIFal(varIFal);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
		} // Fin de la validación de las variables de apoyo.
		
		if (bloqueCorriente.getFechaDevengo()== null){
			return csp005;
		}

		if(bloqueCorriente.getFechaDevengo().after(umic.getFechas().getFecefecfin()) || !proyUmic.get(iteracion - 1).getFechaDesde().before(umic.getFechas().getFecefecfin())) {
			return csp005;
		}
		
		varfallRed = umic.getCapitales().getIcapfall();
		
		if (varIFal.signum()==0){
			csp005 = varfallRed;
		} else {

			//	varfechaEfecto = umic.fechas.fecefecred.
			varFechaEfecto = umic.getFechas().getFecefecred();
					
			//Obtenemos el valor de varNumAnua
			varNumAnua = FuncionesAuxiliares.tc(varFechaEfecto, proyUmic.get(iteracion-1).getFechaDesde());

			
			//Calculamos el valor csp005 <-- varfallRed *(1+varIfal/100)^(varNumAnualidades)
			csp005 = varfallRed.multiply(Util.pow(BigDecimal.ONE.add(varIFal.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT)), varNumAnua));
			
		}
		
		if (ModuloCSP005.LOG.isTraceEnabled()) {
			ModuloCSP005.LOG.trace("Fin de la función << moduloCSP005 >> de la clase moduloCSP005, pra la iteración = {}, con resultado csp005 = {}", iteracion, csp005);
		}
		return csp005;
	}

}
