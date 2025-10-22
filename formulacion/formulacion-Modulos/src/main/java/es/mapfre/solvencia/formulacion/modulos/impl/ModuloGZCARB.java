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
 * Clase que implementa el modulo GZCARB.
 *  *
 * @author etorresj
 *
 */
public class ModuloGZCARB implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZCARB.class);


	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_GZCARB;
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
			if (ModuloGZCARB.LOG.isTraceEnabled()) {
				ModuloGZCARB.LOG.trace("Inicio de execute en clase ModuloGZCARB");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZCRTA
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo GZCARB
			resultado = moduloGZCARB(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloGZCARB.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZCARB.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloGZCARB.LOG.isTraceEnabled()) {
			ModuloGZCARB.LOG.trace("Fin de execute en clase ModuloGZCARB");
		}
		
		return resultado;
	}
	
	
	private BigDecimal  moduloGZCARB(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		BigDecimal gzcarb = BigDecimal.ZERO, gzc004 = BigDecimal.ZERO, gzc017 = BigDecimal.ZERO;
		String varTipoPres;
		Modulo moduloGZC004, moduloGZC017;
		//Fin variables locales
		
		if (ModuloGZCARB.LOG.isTraceEnabled()) {
			ModuloGZCARB.LOG.trace("Inicio función << moduloGZCRTA >> de la clase ModuloGZCRTA, para la iteracion = {}", iteracion);
		}
		

		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		
		moduloGZC004 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZC004);
		moduloGZC017 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZC017);
		gzc004 = (BigDecimal) moduloGZC004.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		gzc017 = (BigDecimal) moduloGZC017.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		
		gzcarb = gzc004.add(gzc017);		
		
		
		if (ModuloGZCARB.LOG.isTraceEnabled()) {
			ModuloGZCARB.LOG.trace("Fin función << moduloGZCARB >> de la clase ModuloGZCARB, para la iteracion = {} con resultado GZCARB = {}", iteracion, gzcarb);
		}
		
		return gzcarb;
	}

}
