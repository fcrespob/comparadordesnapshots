package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

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

/**.
 * Clase que implementa el modulo VZCIERTA
 * @author agonzalezgar
 *
 */
public class ModuloVZCIERTA implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVZCIERTA.class);

	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_VZCIERTA;
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
			if (ModuloVZCIERTA.LOG.isTraceEnabled()) {
				ModuloVZCIERTA.LOG.trace("Inicio de execute en clase ModuloVZCIERTA");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloVZCIERTA
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
//			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo VZCIERTA
			resultado = moduloVZCIERTA(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVZCIERTA.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVZCIERTA.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloVZCIERTA.LOG.isTraceEnabled()) {
			ModuloVZCIERTA.LOG.trace("Fin de execute en clase ModuloVZCIERTA");
		}
		
		return resultado;
	}
	/**
	 * Modulo de cálculo que devuelve el importe utilizando la funcion VZCIERTA
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
	private BigDecimal moduloVZCIERTA(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic) {
		//Variables locales
		BigDecimal vzcierta;
		//Fin variables locales
		
		if (ModuloVZCIERTA.LOG.isTraceEnabled()) {
			ModuloVZCIERTA.LOG.trace("Inicio función << moduloVZCIERTA >> de la clase ModuloVZCIERTA, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parámetros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		/**
		 * Para cualquier periodo j (j >= 1), se calculará el importe del capital correspondiente al periodo j como: 
		 * VZCIERTA (j) = 1
		 */
		vzcierta = BigDecimal.ONE;
		
		if (ModuloVZCIERTA.LOG.isTraceEnabled()) {
			ModuloVZCIERTA.LOG.trace("Fin función << moduloVZCIERTA >> de la clase ModuloVZCIERTA, para la iteracion = {} con resultado vzcierta = {}", iteracion, vzcierta);
		}
		
		return vzcierta;
	}

}
