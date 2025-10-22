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
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/** 
 *
 * @author Aleksandar Plamenov Nedyalkov
 *
 */

public class ModuloRTVRES implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloRTVRES.class);
	
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_RTVRES;
	private static final String CLAVE_LST_PROY = ConstantsModulos.CTE_LST_PROY.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 

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
			if (ModuloRTVRES.LOG.isTraceEnabled()) {
				ModuloRTVRES.LOG.trace("Inicio de execute en clase ModuloRTVRES");
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
			resultado = moduloRTVRES(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloRTVRES.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloRTVRES.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloRTVRES.LOG.isTraceEnabled()) {
			ModuloRTVRES.LOG.trace("Fin de execute en clase ModuloRTVRES");
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
	private BigDecimal moduloRTVRES(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {

		BigDecimal rtvres = BigDecimal.ZERO;
		List<DetalleCorriente> varlistaCorrienteUmic;
		BigDecimal varProvMat;
		Timestamp varfechaEfecto;
		List<DetalleCorriente> varProyRtvrea = null;
		Modulo moduloRTVREA;
		BigDecimal varRtvrea = BigDecimal.ZERO;
		
		if (ModuloRTVRES.LOG.isTraceEnabled()) {
			ModuloRTVRES.LOG.trace("Inicio función << ModuloRTVRES >> de la clase ModuloRTVRES, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		varfechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		final String codk1 = umic.getRescates().getKrescate1();
		final Integer durk1 = UtilModulos.obtenerDuracionCodKX(codk1, varfechaEfecto, bloqueCorriente.getFechaDevengo(), 
				umic.getDuraciones().getNdurprima(), umic.getDuraciones().getNdursegmes());
		
		final BigDecimal vark1 = UtilModulos.getCteRescate(mapVariables, codk1, durk1);
		
		// vark1 depende de durk1; durk1 puede variar entre periodos luego la validación hay que hacerla en cada iteración.
		ValidacionesComunesModulos.validarCteRescateVarkx(codk1, vark1);
		
		moduloRTVREA = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_RTVREA);
		
		if (bloqueCorriente.getFechaDevengo() != null){
			
			//Se recupera la proyección previamente calculada en BTI para la umic
			varlistaCorrienteUmic = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PROY, ConstantsModulos.CTE_BTI, btcUmic.getFecCierre(), umic.getKey());
			if(null == varlistaCorrienteUmic || varlistaCorrienteUmic.size() == 0){
				mapVariables.remove(CLAVE_LST_PROY);
				varlistaCorrienteUmic = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PROY, ConstantsModulos.CTE_BTI_PROY, btcUmic.getFecCierre(), umic.getKey());
			}
			varProvMat = varlistaCorrienteUmic.get(iteracion - 1).getTotalFlujoProyeccion().getProvbtiproy();

			varProyRtvrea = proyUmic;
			varRtvrea = (BigDecimal) moduloRTVREA.execute(varProyRtvrea, bloqueCorriente, iteracion, fcalc, umic,
					btcUmic, mapVariables, codSubproceso);
			
			if (vark1 == null) {
				rtvres = varProvMat;
			}else {
				rtvres = varProvMat.multiply(vark1);	
			}
			
			if (varRtvrea.compareTo(rtvres) < 0) {
				rtvres = varRtvrea;
			}
			//System.out.println(iteracion + ";" + varProvMat + ";" + varRtvrea + ";" + vark1 + ";" + rtvres );
			
		}	
		
		if (ModuloRTVRES.LOG.isTraceEnabled()) {
			ModuloRTVRES.LOG.trace("Fin función << ModuloRTRTVRES >> de la clase ModuloRTRTVRES, para la iteracion = {} con resultado RTVRES = {}", iteracion, rtvres);
		}
		
		
			
		return rtvres;
	}
}
