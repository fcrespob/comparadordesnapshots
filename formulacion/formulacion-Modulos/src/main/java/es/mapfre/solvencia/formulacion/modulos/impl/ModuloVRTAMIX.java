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
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo VRTA162.
 * 		  
 * @author apedro
 *
 */
public class ModuloVRTAMIX implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVRTAMIX.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VRTAMIX;
	private static final String CLAVE_VAR_FIN_RVC = ConstantsModulos.CTE_VA_FIN_RVC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_M_FORPAGRENT = ConstantsModulos.CTE_VAR_M_FORPAGRENT.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_N_PER_GARAN = ConstantsModulos.CTE_VAR_N_PER_GARAN.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_M_PER_GARAN = ConstantsModulos.CTE_VAR_M_PER_GARAN.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_FEC_INI = ConstantsModulos.CTE_VAR_FEC_INI.concat(CLAVE_MODULO);
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
			if (ModuloVRTAMIX.LOG.isTraceEnabled()) {
				ModuloVRTAMIX.LOG.trace("Inicio de execute en clase ModuloVRTAMIX");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloVRTA162
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			//Invocamos a la función ModuloVRTA162
			resultado = moduloVRTAMIX(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVRTAMIX.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVRTAMIX.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloVRTAMIX.LOG.isTraceEnabled()) {
			ModuloVRTAMIX.LOG.trace("Fin de execute en clase ModuloVRTAMIX");
		}
		
		return resultado;
	}
	/**
	 * Módulo de cálculo que devuelve los factores de probabilización a aplicar en  el cálculo de la cuantía probable de la garantía.  			   
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
	 * @param codSubproceso
	 * 			Código del subproceso que se está ejecutando
	 */
	private BigDecimal moduloVRTAMIX(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		BigDecimal vrtamix = BigDecimal.ZERO;
		BigDecimal varMperGaran;
		Integer varMForpagrent = 0;
		Timestamp varFinRvc;
		Timestamp varFechaEfecto = null;
		Timestamp varFecIni = null;
		BigDecimal varVcierta;
		BigDecimal varVrtavit;
		BigDecimal varRentIni;
		BigDecimal varIprimanetaIni;
		BigDecimal varNperGaran;
		Modulo moduloVZCIERTA;
		Modulo moduloVRTAVIT;
		List<DetalleCorriente> varProyVzcierta;
		List<DetalleCorriente> varProyVrtavit;
		//Fin variables locales
		
		if (ModuloVRTAMIX.LOG.isTraceEnabled()) {
			ModuloVRTAMIX.LOG.trace("Inicio función << ModuloVRTAMIX >> de la clase ModuloVRTAMIX, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		varMForpagrent = UtilModulos.getVarMForpagrent(mapVariables, CLAVE_VAR_M_FORPAGRENT, umic);
		
		if(umic.getRentas().getNpergaran() != null){
			varNperGaran = new BigDecimal(umic.getRentas().getNpergaran());
		} else {
			varRentIni = umic.getRentas().getRentini();
			varIprimanetaIni = umic.getPrimas().getIprimanetaini();
			varNperGaran = UtilModulos.getVarNperGaran(mapVariables, CLAVE_VAR_N_PER_GARAN, varIprimanetaIni, varRentIni, varMForpagrent); 
		}
		
		varFechaEfecto = UtilModulos.getVarFechaEfecto(mapVariables, CLAVE_FEC_EFEC, umic);
		varMperGaran = UtilModulos.getVarMperGaran(mapVariables, CLAVE_VAR_M_PER_GARAN, varNperGaran, varMForpagrent); 
		varFecIni = UtilModulos.getVarFecIni(mapVariables, CLAVE_VAR_FEC_INI, umic, varFechaEfecto, varMForpagrent);
		varFinRvc = UtilModulos.incrementarMeses(mapVariables, CLAVE_VAR_FIN_RVC, varFecIni, null, varMperGaran.intValue(), true);
		
		if (null != bloqueCorriente.getFechaDevengo()){
			if (!bloqueCorriente.getFechaDevengo().after(varFinRvc)){
				varProyVzcierta = proyUmic;
				moduloVZCIERTA = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZCIERTA);
				varVcierta = (BigDecimal) moduloVZCIERTA.execute(varProyVzcierta, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
				vrtamix = varVcierta;
			} else {
				varProyVrtavit = proyUmic;
				moduloVRTAVIT = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VRTAVIT);
				varVrtavit = (BigDecimal) moduloVRTAVIT.execute(varProyVrtavit, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
				vrtamix = varVrtavit;
			}
		}
		
		if (ModuloVRTAMIX.LOG.isTraceEnabled()) {
			ModuloVRTAMIX.LOG.trace("Fin función << ModuloVRTAMIX >> de la clase ModuloVRTAMIX, para la iteracion = {} con resultado vrtamix = {}", iteracion, vrtamix);
		}
			
		return vrtamix;
	}
}
