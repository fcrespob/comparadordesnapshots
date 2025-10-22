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
 * 
 * @author NFQ
 *
 */

public class ModuloORFCOA implements Modulo {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloORFCOA.class);
	
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_ORFCOA;
	
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
			if (ModuloORFCOA.LOG.isTraceEnabled()) {
				ModuloORFCOA.LOG.trace("Inicio de execute en clase ModuloORFCOA");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCOMPLPTOZC
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de datos.
			
			//Invocamos a la función ModuloORFCOA
			resultado = moduloORFCOA(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloORFCOA.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloORFCOA.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloORFCOA.LOG.isTraceEnabled()) {
			ModuloORFCOA.LOG.trace("Fin de execute en clase ModuloORFCOA");
		}
		
		return resultado;
	}
	
	private BigDecimal moduloORFCOA(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, String codSubproceso) {
		
		BigDecimal orfcoa = BigDecimal.ZERO;
		BigDecimal impFZC = BigDecimal.ZERO;
		BigDecimal impVCVH = BigDecimal.ZERO; 
		Modulo moduloFZC = null;
		Modulo moduloVCVH = null;
		
		if (ModuloORFCOA.LOG.isTraceEnabled()) {
			ModuloORFCOA.LOG.trace("Inicio función << moduloORFCOA >> de la clase ModuloORFCOA, para la iteracion = {}", iteracion);
		}
		
		//Se llama al módulo FZC 
		moduloFZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FZC);
		if(umic.getOtrosDatos().getCestadoAseg1().equalsIgnoreCase("A") || (umic.getOtrosDatos().getCestadoAseg1().equalsIgnoreCase("A") && umic.getOtrosDatos().getCestadoAseg2().equalsIgnoreCase("A"))){
			impFZC = BigDecimal.ONE;
		}else{	
			impFZC = (BigDecimal) moduloFZC.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		}
		//Se llama al módulo VCVH 
		moduloVCVH = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VCVH);
		impVCVH = (BigDecimal) moduloVCVH.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		orfcoa = impFZC.multiply(impVCVH);
		
		if (ModuloORFCOA.LOG.isTraceEnabled()) {
			ModuloORFCOA.LOG.trace("Fin función << ModuloORFCOA >> de la clase ModuloORFCOA, para la iteracion = {} con resultado ORFCOA = {}", iteracion, orfcoa);
		}
		
		return orfcoa;
		
	}

}
