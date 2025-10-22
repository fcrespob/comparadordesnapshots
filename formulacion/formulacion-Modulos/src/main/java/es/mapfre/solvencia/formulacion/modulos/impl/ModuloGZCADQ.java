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
 * 
 * Usaremos este módulo para aquellas modalidades cuyo Capital 
 * revaloriza geométricamente al IPC, y siempre que tengamos que 
 * provocar la renovación tácita como consecuencia de la 
 * aplicación del CRITERIO TAR
 * 
 * @author Szilard Toth
 *
 */

public class ModuloGZCADQ implements Modulo{

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZCADQ.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_GZCADQ;
	
	private static final String CLAVE_VAR_PORC_G= ConstantsModulos.CTE_VAR_PORC_G;
	private static final String CLAVE_VAR_PORC_P= ConstantsModulos.CTE_VAR_PORC_P;

	
	
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
			
			if (ModuloGZCADQ.LOG.isTraceEnabled()) {
				ModuloGZCADQ.LOG.trace("Inicio de execute en clase ModuloGZCADQ");
			}
			
			//Recuperamos los datos que le pasaremos a la función ModuloCSP011
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			//Invocamos a la función moduloCSP011
			resultado = moduloGZCADQ(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso,mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloGZCADQ.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZCADQ.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloGZCADQ.LOG.isTraceEnabled()) {
			ModuloGZCADQ.LOG.trace("Fin de execute en clase ModuloGZCADQ");
		}
		
		return resultado;
	}
	
	/**
	 * Usaremos este módulo para aquellas modalidades cuyo Capital revaloriza geométricamente al IPC, y siempre que tengamos que provocar la renovación tácita como consecuencia de la aplicación del CRITERIO TAR.
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

	private BigDecimal moduloGZCADQ(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final String codSubproceso,final Map<String, Object> mapVariables) {
		//variables locales
		BigDecimal gzcadq = BigDecimal.ZERO;
		BigDecimal porc_p = BigDecimal.ZERO;
		BigDecimal porc_g = BigDecimal.ZERO;
		String varApoyoPorc_p = ConstantsFunciones.CTE_CADENA_VACIA;
		String varApoyoPorc_g = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal varNominalGastos = BigDecimal.ZERO;
		BigDecimal varNominalPrimas = BigDecimal.ZERO;
		//Fin variables locales
		
		if (ModuloGZCADQ.LOG.isTraceEnabled()) {
			ModuloGZCADQ.LOG.trace("Inicio de la función << moduloGZCADQ >> de la clase ModuloGZCADQ, para la iteración = {}", iteracion);
		}	
		
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		
		/**
		 * 
		 *GZCADQ j =(PRIMAj×porc_p)+(GASTOSj×porc_g)
 		 *Recuperamos la corriente de Gastos previamente calculada en PROY_GTOS.(GASTOSj)
 		 *Recuperamos la corriente de Primas previamente calculada en PROY_PRIMA (PRIMAj)
 		 *salvo en el caso de tratarse de primas únicas donde (en j=0) se recuperará de la UMIC del Maestro la Prima Neta Inicial. 
 		 *porc_p: % recuperado de tabla donde se define a nivel Ramo. En caso de no estar informado en tabla se le asignará valor 0. 
		 *porc_g: % recuperado de tabla donde se define a nivel Ramo. En caso de no estar informado en tabla se le asignará valor 0. 
		 * 
		 * 
		 */
		
		
		if(null != bloqueCorriente.getFechaDevengo()) { 
		   
			final BloqueCorriente bloqueGastos = proyUmic.get(iteracion - 1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_GTOS);
			final BloqueCorriente bloquePrimas = proyUmic.get(iteracion - 1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_PRIMA);
			varApoyoPorc_p = UtilModulos.getsetStringRecuperarDefinicionAuxiliar(mapVariables, CLAVE_VAR_PORC_P, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), ConstantsModulos.CTE_VAR_PORC_P);
			if (varApoyoPorc_p == null){
				porc_p = BigDecimal.ONE;
			}else {
				porc_p = new BigDecimal (varApoyoPorc_p).divide(new BigDecimal("100")); 
			}
			
			varApoyoPorc_g = UtilModulos.getsetStringRecuperarDefinicionAuxiliar(mapVariables, CLAVE_VAR_PORC_G, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), ConstantsModulos.CTE_VAR_PORC_G);
			if ( null == varApoyoPorc_g ) {
				porc_g = BigDecimal.ONE;
			}else {
				porc_g = new BigDecimal (varApoyoPorc_g).divide(new BigDecimal("100")); 
			}
			if (bloquePrimas.getImpFlujoNominal() != null) {
				varNominalPrimas = bloquePrimas.getImpFlujoNominal();
			}
			if (bloqueGastos.getImpFlujoNominal() != null) {
				varNominalGastos = bloqueGastos.getImpFlujoNominal();
			}
			if (umic.getPrimas().getCformpago().equals(ConstantsModulos.CTE_CFORMPAG_UNICA) && ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
				varNominalPrimas = umic.getPrimas().getIprimanetaini();
			}
			 
			gzcadq = (varNominalPrimas.multiply(porc_p)).add(varNominalGastos.multiply(porc_g));
			
					
		}
		
		if (ModuloGZCADQ.LOG.isTraceEnabled()) {
			ModuloGZCADQ.LOG.trace("Fin de la función << moduloGZCADQ >> de la clase ModuloGZCADQ, para la iteración = {}", iteracion);
		}
		return gzcadq;
	}
}
