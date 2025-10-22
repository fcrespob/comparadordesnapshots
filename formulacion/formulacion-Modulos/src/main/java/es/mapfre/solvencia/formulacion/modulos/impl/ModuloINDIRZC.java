package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * En seguros complementarios colectivos, es muy habitual usar tasas que no se corresponden con ninguna de las tablas cargadas, y que no son de uso general ya que se dan para colectivos puntuales debidos a subastas y precios prefijados.
 * Por tanto, es necesario desarrollar un módulo que nos permita, a través de la prima, capital y gastos cargados en cartera, deducir la tasa a aplicar.

 * La expresión matemática para su determinación es la siguiente:
 * 				varINDIRzc=  (varPrimaNetaAct )/(varIcapAct )*(1-varGE -varGipc )*1/12
 * @author eugenio.torres
 *
 */
public class ModuloINDIRZC implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloINDIRZC.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_INDIRZC;
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
			if (ModuloINDIRZC.LOG.isTraceEnabled()) {
				ModuloINDIRZC.LOG.trace("Inicio de execute en clase INDIRZC");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloINDIRZC
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de datos.
			
			//Invocamos a la función moduloINDIRZC
			resultado = moduloINDIRZC(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso ,mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloINDIRZC.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloINDIRZC.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloINDIRZC.LOG.isTraceEnabled()) {
			ModuloINDIRZC.LOG.trace("Fin de execute en clase INDIRZC");
		}

		return resultado;
	}
	/**
	 * En seguros complementarios colectivos, es muy habitual usar tasas que no se corresponden con ninguna 
	 * de las tablas cargadas, y que no son de uso general ya que se dan para colectivos puntuales debidos a 
	 * subastas y precios prefijados.
	 * Por tanto, es necesario desarrollar un módulo que nos permita, a través de la prima, capital y 
	 * gastos cargados en cartera, deducir la tasa a aplicar. 
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
	private BigDecimal moduloINDIRZC(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal INDIRzc = BigDecimal.ZERO;
		BigDecimal varGE = BigDecimal.ZERO;
		BigDecimal varGipc;
		BigDecimal varPrimaNetaAct;
		BigDecimal varIcapAct;
		//Fin variables locales
		
		if (ModuloINDIRZC.LOG.isTraceEnabled()) {
			ModuloINDIRZC.LOG.trace("Inicio función << moduloINDIRZC >> de la clase moduloINDIRZC, para la iteracion = {}", iteracion);
		}
		
		//validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);	
		
		varGE = umic.getBti().getPgastgesex1I().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		varGipc = btcUmic.getGtorosspPrima().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		varPrimaNetaAct = umic.getPrimas().getIprimanetaact();
		varIcapAct = umic.getCapitales().getIcapact();
		
		
		if(null == varIcapAct || varIcapAct.setScale(34, RoundingMode.HALF_UP).equals(BigDecimal.ZERO.setScale(34, RoundingMode.HALF_UP))){
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1);
		}
		
		if(!(null == bloqueCorriente.getFechaDevengo())){
			BigDecimal m1 = varPrimaNetaAct.divide(varIcapAct, ConstantsFunciones.MATH_CONTEXT);
			INDIRzc = m1.multiply(BigDecimal.ONE.subtract(varGE).subtract(varGipc)).multiply(ConstantsFunciones.CTE_OPER_1ENTRE12);
		}
		if (ModuloINDIRZC.LOG.isTraceEnabled()) {
			ModuloINDIRZC.LOG.trace("Fin función << moduloINDIRZC >> de la clase ModuloINDIRZC, para la iteracion = {}, con resultado INDIRzc = {}", iteracion, INDIRzc);
		}
		
		return INDIRzc;
	}
}
