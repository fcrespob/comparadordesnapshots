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
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo GZC004.
 * Clase encargada del cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic.
 * La expresión matemática para su determinación es la siguiente:
 * 			GZC(004, ZC) = Flujo_nominal_vida * Gic
 * @author agonzalezgar
 *
 */
public class ModuloGZC004 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZC004.class);
	
	private static final String CLAVE_DIVISION_100 = "DIV_100";

	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_GZC004;
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
			if (ModuloGZC004.LOG.isTraceEnabled()) {
				ModuloGZC004.LOG.trace("Inicio de execute en clase ModuloGZC004");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZC004
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];

			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo GZC004
			resultado = moduloGZC004(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloGZC004.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZC004.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloGZC004.LOG.isTraceEnabled()) {
			ModuloGZC004.LOG.trace("Fin de execute en clase ModuloGZC004");
		}
		
		return resultado;
	}
	
	/**
	 * Modulo de cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic.
	 * La expresión matemática para su determinación es la siguiente:
	 * 					GZC(004, ZC) = Flujo_nominal_vida * Gic
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
	private BigDecimal moduloGZC004(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal gzc004 = BigDecimal.ZERO;
		BigDecimal varGic = BigDecimal.ZERO;
		BigDecimal division100 = null;
		//Fin variables locales
		
		if (ModuloGZC004.LOG.isTraceEnabled()) {
			ModuloGZC004.LOG.trace("Inicio función << moduloGZC004 >> de la clase ModuloGZC004, para la iteracion = {}", iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if(bloqueCorriente.getFechaDevengo() == null) {
			return gzc004;
		}

		/**
			 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, 
			 * así como las varibales internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso 
			 * por los siguientes periodos a calcular. 
  
					Variables Módulo
					-	varGic = btcUmic.gtoRosspCap;
										
					Para cualquier periodo j se calculará GZC004 (j) como: 
					
					GZC004 (j) =   (vargic/100) * proyUmic (j).corrienteVida.impFlujoNominal 			
			 */
		
		
		
		
		final BloqueCorriente bloqueVida = proyUmic.get(iteracion - 1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
		if ( null == bloqueVida.getImpFlujoNominal() ) {
			bloqueVida.setImpFlujoNominal(BigDecimal.ZERO);
		}

		division100 = (BigDecimal) mapVariables.get(CLAVE_DIVISION_100);
		if (division100 == null){
			varGic = btcUmic.getGtorosspCap();
			if (varGic.signum()==0){
				division100 = BigDecimal.ZERO;
			}else{
				division100 = varGic.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
			}
			mapVariables.put(CLAVE_DIVISION_100, division100);
		}
			
		if (division100.signum()==0){
			return gzc004;
		}
		gzc004 = division100.multiply(bloqueVida.getImpFlujoNominal());
		
		if (ModuloGZC004.LOG.isTraceEnabled()) {
			ModuloGZC004.LOG.trace("Fin función << moduloGZC004 >> de la clase ModuloGZC004, para la iteracion = {} con resultado gzc004 = {}", iteracion, gzc004);
		}
			
		return gzc004;
	}

}
