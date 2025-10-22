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
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo GZC003.
 * Clase encargada del cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic.
 * La expresión matemática para su determinación es la siguiente:
 * 			GZC(003, ZC) = Capital_inicial * Gic
 * @author apedro
 *
 */
public class ModuloGZC003 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZC003.class);
	
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_GZC003;
	private static final String CLAVE_CALCULO_GZC003 = CLAVE_MODULO;
	
	private static final String CLAVE_NPP = ConstantsModulos.CTE_VA_NPP;
	private static final String CLAVE_VAR_NPP = CLAVE_NPP.concat(CLAVE_MODULO);

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
			if (ModuloGZC003.LOG.isTraceEnabled()) {
				ModuloGZC003.LOG.trace("Inicio de execute en clase ModuloGZC003");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZC003
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];

			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo GZC003
			resultado = moduloGZC003(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloGZC003.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZC003.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloGZC003.LOG.isTraceEnabled()) {
			ModuloGZC003.LOG.trace("Fin de execute en clase ModuloGZC003");
		}
		
		return resultado;
	}
	
	/**
	 * Modulo de cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic.
	 * La expresión matemática para su determinación es la siguiente:
	 * 					GZC(003, ZC) = Capital_inicial * Gic
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
	private BigDecimal moduloGZC003(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal gzc003 = BigDecimal.ZERO;
		BigDecimal varGic;
		BigDecimal varCsp;
		BigDecimal varNPP;
		//Fin variables locales
		
		if (ModuloGZC003.LOG.isTraceEnabled()) {
			ModuloGZC003.LOG.trace("Inicio función << moduloGZC003 >> de la clase ModuloGZC003, para la iteracion = {}", iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if(bloqueCorriente.getFechaDevengo() == null) {
			return gzc003;
		}

		/**
			 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, 
			 * así como las varibales internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso 
			 * por los siguientes periodos a calcular. 
  
					Variables Módulo
					-	varGic = btcUmic.gtoRosspCap;
					-   varCsp = umic.capitales.icapini;
										
					Para cualquier periodo j se calculará GZC003 (j) como: 
					
					GZC003 (j) =   (vargic/100) * varCsp			
			 */
		
		gzc003 = (BigDecimal) mapVariables.get(CLAVE_CALCULO_GZC003);
		
		if (gzc003 == null){
		
			varGic = btcUmic.getGtorosspCap();
			BigDecimal gicEntre100 = varGic.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
			varCsp = umic.getCapitales().getIcapact();
			varNPP = UtilModulos.getVarNpp(mapVariables, CLAVE_VAR_NPP, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_NPP);
		
			gzc003 = (gicEntre100.divide(varNPP, ConstantsFunciones.MATH_CONTEXT)).multiply(varCsp);
			
			mapVariables.put(CLAVE_CALCULO_GZC003, gzc003);
		}
		
		
		if (ModuloGZC003.LOG.isTraceEnabled()) {
			ModuloGZC003.LOG.trace("Fin función << moduloGZC003 >> de la clase ModuloGZC003, para la iteracion = {} con resultado gzc003 = {}", iteracion, gzc003);
		}
			
		return gzc003;
	}

}
