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
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo VZG2C
 * La expresión matemática para su determinación es la siguiente:
 * 		  vzg2c (j) = varRever * [varVzc(j) + varVzc2(j)] +[(1 – 2 * varRever) * varVzc(j) * varVzc2(j)]
 * @author ogperez
 *
 */
public class ModuloVZG2C implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVZG2C.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VZG2C;
	private static final String CLAVE_VAR_REVER = ConstantsModulos.CTE_VAR_REVER.concat(CLAVE_MODULO);
	//private static final String CLAVE_VAR_FCIERTA = ConstantsModulos.CTE_VAR_FCIERTA.concat(CLAVE_MODULO);
	private static final String CLAVE_UMIC2 = ConstantsModulos.CTE_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC2 = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);
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
			if (ModuloVZG2C.LOG.isTraceEnabled()) {
				ModuloVZG2C.LOG.trace("Inicio de execute en clase ModuloVRTA363");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloVZG2C
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			//Invocamos a la función ModuloVZG2C
			resultado = moduloVZG2C(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVZG2C.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVZG2C.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloVZG2C.LOG.isTraceEnabled()) {
			ModuloVZG2C.LOG.trace("Fin de execute en clase ModuloVRTA363");
		}
		
		return resultado;
	}
	/**
	 * Modulo de cálculo que devuelve los factores de probabilización a aplicar en  el cálculo de la cuantía 
	 * probable de la garantía principal de Modalidades Rentas de Jubilación con Reembolso de Reservas (Mod. 363)   		
	 * La expresión matemática para su determinación es la siguiente:	   
	 *			VRTA363 = Vzc1(fcierta, j) + REVER * [Vzc2(fcierta,j) - Vzc1zc2(fcierta,j)]
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
	private BigDecimal moduloVZG2C(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		BigDecimal vzg2c = BigDecimal.ZERO;
		BigDecimal varRever;
		BigDecimal varVzc;
		BigDecimal varVzc2;
		Modulo moduloVZC;
		Umic varUmic2;
		DetalleBaseTecnica varBtcUmic2;
		//Fin variables locales
		
		if (ModuloVZG2C.LOG.isTraceEnabled()) {
			ModuloVZG2C.LOG.trace("Inicio función << ModuloVZG2C >> de la clase ModuloVZG2C, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if (bloqueCorriente.getFechaDevengo() == null){
			return vzg2c;
		}
		
		
		//Se calculan y almacenan las variables del módulo que no camian entre periodos
		varRever = (BigDecimal) mapVariables.get(CLAVE_VAR_REVER);
		if (varRever == null){
			varRever = umic.getRentas().getPreversion().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
			mapVariables.put(CLAVE_VAR_REVER, varRever);
		}

		moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
		varVzc = (BigDecimal) moduloVZC.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		
		//Si existe un segundo asegurado:
		if (umic.getAsegurados().getFnacAseg2() != null) {
			
			varUmic2 = (Umic) mapVariables.get(CLAVE_UMIC2);
			varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC2);
			if (varUmic2 == null) {
				//Se clona la Umic y la btcUmic para asignar los datos del asegurado2 al asegurado1 de la nueva Umic y btcUmic 
				UtilModulos.clonarUmicBtcumic(mapVariables, CLAVE_UMIC2, CLAVE_BTC_UMIC2, umic, btcUmic);
				varUmic2 = (Umic) mapVariables.get(CLAVE_UMIC2);
				varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC2);
			}
			
			mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG2);
			
			varVzc2 = (BigDecimal) moduloVZC.execute(proyUmic, bloqueCorriente, iteracion, fcalc, varUmic2, varBtcUmic2, mapVariables, codSubproceso);
			
			//Se realiza el cálculo de: vzg2c (j) = varRever * [varVzc(j) + varVzc2(j)] +[(1 – 2 * varRever) * varVzc(j) * varVzc2(j)]
			BigDecimal op1 = varRever.multiply(varVzc.add(varVzc2));
			BigDecimal op2 = BigDecimal.ONE.subtract(BigDecimal.valueOf(2).multiply(varRever)).multiply(varVzc).multiply(varVzc2);
			vzg2c = op1.add(op2);
								
		}
		else {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AS);			
		}		
		
		
		if (ModuloVZG2C.LOG.isTraceEnabled()) {
			ModuloVZG2C.LOG.trace("Fin función << ModuloVZG2C >> de la clase ModuloVZG2C, para la iteracion = {} con resultado vzg2c = {}", iteracion, vzg2c);
		}
		
		return vzg2c;			

	}
}
