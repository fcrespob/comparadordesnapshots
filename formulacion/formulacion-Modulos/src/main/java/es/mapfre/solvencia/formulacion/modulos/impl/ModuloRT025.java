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
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo RT025.
 * La expresión matemática para su determinación es la siguiente:
 * 		RT(025,tc) = MINIMO (RT003 , RT010)  
 * @author apedro
 *
 */
public class ModuloRT025 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloRT025.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_RT025;
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
			if (ModuloRT025.LOG.isTraceEnabled()) {
				ModuloRT025.LOG.trace("Inicio de execute en clase ModuloRT025");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloRT025
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			//Invocamos a la función ModuloRT025
			resultado = moduloRT025(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloRT025.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloRT025.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloRT025.LOG.isTraceEnabled()) {
			ModuloRT025.LOG.trace("Fin de execute en clase ModuloRT025");
		}
		
		return resultado;
	}
	/**
	 * Módulo de cálculo de la cuantía por Rescate de las garantías dentro de Solvencia II para las modalidades de MILLÓN VIDA.
	 * La expresión matemática para su determinación es la siguiente:
	 *				 RT(025,tc) = MINIMO (RT003 , RT010)  
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
	 * 			Código del subproceso que se está ejecutando
	 */
	private BigDecimal moduloRT025(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		BigDecimal rt025 = BigDecimal.ZERO;
		BigDecimal varRT003;
		BigDecimal varRT010;
		Modulo moduloRT003;
		Modulo moduloRT010;
		//Fin variables locales
		
		if (ModuloRT025.LOG.isTraceEnabled()) {
			ModuloRT025.LOG.trace("Inicio función << ModuloRT025 >> de la clase ModuloRT025, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		
		if (bloqueCorriente.getFechaDevengo() == null){
			return rt025;
		}
		
		//Se obtienen los módulos necesarios para el cálculo
		moduloRT003 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_RT003);
		moduloRT010 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_RT010);

		//Se ejecutan los módulos necesarios para el cálculo
		varRT003 = (BigDecimal) moduloRT003.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);

		varRT010 = (BigDecimal) moduloRT010.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		
		//Se realiza el cálculo con la fórmula RT025(j) = Mínimo (varRT003(j), varRT010(j))
		rt025 = varRT003.min(varRT010);
		
		if (ModuloRT025.LOG.isTraceEnabled()) {
			ModuloRT025.LOG.trace("Fin función << ModuloRT025 >> de la clase ModuloRT025, para la iteracion = {} con resultado rt025 = {}", iteracion, rt025);
		}
			
		return rt025;
	}
}
