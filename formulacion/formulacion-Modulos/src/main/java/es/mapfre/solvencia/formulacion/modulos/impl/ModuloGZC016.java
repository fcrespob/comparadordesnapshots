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

public class ModuloGZC016 implements Modulo {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZC016.class);
	
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_GZC016;
	
	
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}
	
	@Override
	public Object execute(Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal gzc016 = BigDecimal.ZERO;
		//Fin variables locales
		
		try {
			if (ModuloGZC016.LOG.isTraceEnabled()) {
				ModuloGZC016.LOG.trace("Inicio de execute en clase ModuloGZC016");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZC016
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			//Invocamos a la función de calculo GZC016
			gzc016 = moduloGZC016(proyUmic,iteracion,fcalc, umic, btcUmic, codSubproceso, bloqueCorriente,mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloGZC016.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZC016.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloGZC016.LOG.isTraceEnabled()) {
			ModuloGZC016.LOG.trace("Fin de execute en clase ModuloGZC016");
		}
		
		return gzc016;
	}
	
	private BigDecimal moduloGZC016(final List<DetalleCorriente> proyUmic, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final String codSubproceso, final BloqueCorriente bloqueCorriente, final Map<String, Object> mapVariables){
		
		//Variables locales
		List<DetalleCorriente> varProyGzc004;
		List<DetalleCorriente> varProyGzc015;
		BigDecimal gzc016 = BigDecimal.ZERO;
		BigDecimal vargzc004 = BigDecimal.ZERO;
		BigDecimal vargzc015 = BigDecimal.ZERO;
		Modulo moduloGZC004;
		Modulo moduloGZC015;
		//Fin variables locales
		
		if (ModuloGZC016.LOG.isTraceEnabled()) {
			ModuloGZC016.LOG.trace("Inicio función << moduloGZC016 >> para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		//Variables de modulo
		varProyGzc004 = proyUmic;
		varProyGzc015 = proyUmic;
		
		//Parte funcional
		
		//Crear llamada al moculo GZC004
		//Se llama al módulo GZC004 para obtener el importe nominal del detalle de la corriente.
		moduloGZC004 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZC004);
		vargzc004 = (BigDecimal) moduloGZC004.execute(varProyGzc004, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,codSubproceso);
			//Crear llamada al modulo GZC015
		moduloGZC015 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZC015);
		vargzc015 = (BigDecimal) moduloGZC015.execute(varProyGzc015, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,codSubproceso);
		
		gzc016 = vargzc004.add(vargzc015);
		
		return gzc016;
	} 

}
