package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.ValoresRescateKey;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.ValoresRescateDao;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ValoresRescate;
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
 * @author Aleksandar Plamenov Nedyalkov
 *
 */

public class ModuloRTVREA implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloRTVREA.class);
	
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_RTVREA;
	private static final String CLAVE_LST_PROY = ConstantsModulos.CTE_LST_PROY.concat(CLAVE_MODULO);

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
			if (ModuloRTVREA.LOG.isTraceEnabled()) {
				ModuloRTVREA.LOG.trace("Inicio de execute en clase ModuloRTVREA");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloRTPMAT
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			//Invocamos a la función ModuloRTPMAT
			resultado = moduloRTVREA(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloRTVREA.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloRTVREA.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloRTVREA.LOG.isTraceEnabled()) {
			ModuloRTVREA.LOG.trace("Fin de execute en clase ModuloRTVREA");
		}
		
		return resultado;
	}
	/**
	 * Módulo de cálculo de la cuantía por Rescate. 
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
	private BigDecimal moduloRTVREA(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {

		BigDecimal rtvrea = BigDecimal.ZERO;
		List<DetalleCorriente> varlistaCorrienteUmic;
		BigDecimal varProvMat;
		BigDecimal varCociente_Medio = BigDecimal.ZERO;
		BigDecimal VM = BigDecimal.ZERO;
		BigDecimal VCA = BigDecimal.ZERO;
		ValoresRescateDao valoresRescateDao = new ValoresRescateDao();
		
		if (ModuloRTVREA.LOG.isTraceEnabled()) {
			ModuloRTVREA.LOG.trace("Inicio función << ModuloRTVREA >> de la clase ModuloRTVREA, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if (bloqueCorriente.getFechaDevengo() != null){
			
			//Se recupera la proyección previamente calculada en BTI para la umic
			varlistaCorrienteUmic = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PROY, ConstantsModulos.CTE_BTI, btcUmic.getFecCierre(), umic.getKey());
			
			if(null == varlistaCorrienteUmic || varlistaCorrienteUmic.size() == 0){
				mapVariables.remove(CLAVE_LST_PROY);
				varlistaCorrienteUmic = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PROY, ConstantsModulos.CTE_BTI_PROY, btcUmic.getFecCierre(), umic.getKey());
			}
			
			varProvMat = varlistaCorrienteUmic.get(iteracion - 1).getTotalFlujoProyeccion().getProvbtiproy();
			
			ValoresRescateKey key = new ValoresRescateKey(umic.getDatosGenerales().getKcarterainv());
			ValoresRescate vr = valoresRescateDao.get(key);

			varCociente_Medio = vr.getVM().divide(vr.getVCA(), ConstantsFunciones.MATH_CONTEXT);
			rtvrea = varProvMat.multiply(varCociente_Medio);
			
			//System.out.println(iteracion + ";" + varProvMat + ";" + vr.getVM() + ";" + vr.getVCA() + ";" + varCociente_Medio + ";" + rtvrea);
			
		}	
		
		if (ModuloRTVREA.LOG.isTraceEnabled()) {
			ModuloRTVREA.LOG.trace("Fin función << ModuloRTVREA >> de la clase ModuloRTVREA, para la iteracion = {} con resultado RTVREA = {}", iteracion, rtvrea);
		}
		
			
		return rtvrea;
	}
}
