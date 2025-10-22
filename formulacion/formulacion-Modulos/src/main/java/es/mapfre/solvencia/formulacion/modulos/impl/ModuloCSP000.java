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

public class ModuloCSP000 implements Modulo {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP000.class);
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP000;
	
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}
	@Override
	public Object execute(Object... args) throws Solvencia2Excepcion {
		//Variables locales
				BigDecimal csp000 = BigDecimal.ZERO;
				//Fin variables locales
				
				try {
					if (ModuloCSP000.LOG.isTraceEnabled()) {
						ModuloCSP000.LOG.trace("Inicio de execute en clase ModuloCSP000");
					}
					
					//Recuperamos los datos que le pasaremos a la función moduloCSP000
					final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
					final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
					final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
					final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
					final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
					final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
					final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
					final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
					//Invocamos a la función de calculo CSP000
					csp000 = moduloCSP000(proyUmic,iteracion,fcalc, umic, btcUmic, codSubproceso, bloqueCorriente,mapVariables);
				} catch (Solvencia2Excepcion e) {
					ModuloCSP000.LOG.error(e.getIncidencia().getTextoError(), e);
					throw e;
				} catch (Exception e) {
					ModuloCSP000.LOG.error(e.getMessage(), e);
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
				}
				
				if (ModuloCSP000.LOG.isTraceEnabled()) {
					ModuloCSP000.LOG.trace("Fin de execute en clase ModuloCSP000");
				}
					
				return csp000;
	}
	
	/**
	 * Este módulo de cálculo devuelve una renta nula para el titular. 
	 * Para aquellos casos que solo se valora la reversión. 
	 * No hay plan de rentas cargado en el sistema. 
	 * @param proyUmic
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param codSubproceso
	 * @param bloqueCorriente
	 * @param mapVariables
	 * @return csp000
	 */
	private BigDecimal moduloCSP000(List<DetalleCorriente> proyUmic, int iteracion, Timestamp fcalc, Umic umic,
			DetalleBaseTecnica btcUmic, String codSubproceso, BloqueCorriente bloqueCorriente,
			Map<String, Object> mapVariables) {

		BigDecimal csp000 = BigDecimal.ZERO;
		
		if (ModuloCSP000.LOG.isTraceEnabled()) {
			ModuloCSP000.LOG.trace("Inicio función << moduloCSP000 >> para la iteracion = {}", iteracion);
		}
		
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if (ModuloCSP000.LOG.isTraceEnabled()) {
			ModuloCSP000.LOG.trace("Fin función << moduloCSP000 >> de la clase ModuloCSP000, para la iteracion = {}, con resultado csp000 = {}", iteracion, csp000);
		}
		
		return csp000;
	}
	
}
