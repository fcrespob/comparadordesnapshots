package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilFechas.Fecha;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloVIUE102 implements Modulo {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVIUE102.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VIUE102;	
	
	private static final String CLAVE_ANOESP = ConstantsModulos.CTE_ANOESP;
	private static final String CLAVE_VAR_ANOESP = CLAVE_ANOESP.concat(CLAVE_MODULO);	
	private static final String CLAVE_E1PSJ= ConstantsModulos.CTE_E1PSJ;
	private static final String CLAVE_VAR_E1PSJ = CLAVE_E1PSJ.concat(CLAVE_MODULO);	
	private static final String CLAVE_E1PSV= ConstantsModulos.CTE_E1PSV;
	private static final String CLAVE_VAR_E1PSV = CLAVE_E1PSV.concat(CLAVE_MODULO);
	private static final String CLAVE_EGARB = ConstantsModulos.CTE_EGARB;
	private static final String CLAVE_VAR_EGARB = CLAVE_EGARB.concat(CLAVE_MODULO);
	private static final String CLAVE_PRPSS = ConstantsModulos.CTE_PRPSS;
	private static final String CLAVE_PRPSS_2001 = ConstantsModulos.CTE_PRPSS_2001;
	private static final String CLAVE_PRPSS_2002 = ConstantsModulos.CTE_PRPSS_2002;
	private static final String CLAVE_PRPSS_REST = ConstantsModulos.CTE_PRPSS_REST;
	private static final String CLAVE_VAR_PRPSS = CLAVE_PRPSS.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PRPSS_2001 = CLAVE_PRPSS_2001.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PRPSS_2002 = CLAVE_PRPSS_2002.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PRPSS_REST = CLAVE_PRPSS_REST.concat(CLAVE_MODULO);
	
	
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/**
	 * Módulo de cuantía nominal para viudedado.
	 */
	
	@Override
	public Object execute(Object... args) throws Solvencia2Excepcion {
		
		BigDecimal viue102= BigDecimal.ZERO;
		
		try {
			if (ModuloVIUE102.LOG.isTraceEnabled()) {
				ModuloVIUE102.LOG.trace("Inicio de execute en clase ModuloVIUE102");
			}
			
			//Recuperamos los datos que le pasaremos a la función ModuloCSPVIUE01
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			viue102 = moduloVIUE102(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVIUE102.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVIUE102.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
	
		if (ModuloVIUE102.LOG.isTraceEnabled()) {
			ModuloVIUE102.LOG.trace("Fin de execute en clase ModuloVIUE102");
		}
		
		return viue102;
	}

	
	/**
	 * Módulo de cuantía nominal para viudedad
	 * 
	 * @param proyUmic
	 * @param bloqueCorriente
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param codSubproceso
	 * @param mapVariables
	 * @return
	 */
	
	private BigDecimal moduloVIUE102(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {
		
		BigDecimal varviue102 = BigDecimal.ZERO;
		BigDecimal varFactorPrpss = BigDecimal.ZERO;
		String  varAnoEspStr, varE1PSVStr, varEGARBStr, varPrpssStr, varE1PSJStr;
		BigDecimal varAnoEsp, varE1PSV, varEGARB, varPrpss;
		Fecha varFecJ;
		Integer varAnoj;
		BigDecimal varPSJj, varPSVj;
		String varPrpssStr2001, varPrpssStr2002, varPrpssStrREST;
		BigDecimal varPrpss2001 = BigDecimal.ZERO, varPrpss2002 = BigDecimal.ZERO, varPrpssREST = BigDecimal.ZERO; 
		BigDecimal auxPRPSS2001 = BigDecimal.ZERO;
		BigDecimal auxPRPSS2002 = BigDecimal.ZERO;
		BigDecimal auxPRPSSREST = BigDecimal.ZERO;
		
		if (ModuloVIUE102.LOG.isTraceEnabled()) {
			ModuloVIUE102.LOG.trace("Inicio función << moduloVIUE102 >> de la clase ModuloVIUE102, para la  iteracion = {}", iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Fin de la definición de las variables auxiliares.	
		
		if(null == bloqueCorriente.getFechaDevengo()){
			
			return varviue102;	
			
		}
		
		//Varibales de apoyo
		varAnoEspStr = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_ANOESP, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_ANOESP);
		if(null == varAnoEspStr){
			
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_ANOESP});
			
		}else{
			varAnoEsp = new BigDecimal(varAnoEspStr);
		}
		varE1PSVStr = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_E1PSV, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_E1PSV);
		if(null == varE1PSVStr){
			
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_E1PSV});
			
		}else{
			
			varE1PSV = UtilModulos.StringToBigDecimal(varE1PSVStr);
			varE1PSV = varE1PSV.divide(new BigDecimal("100"));

		}
		varEGARBStr = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_EGARB, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_EGARB);
		if(null == varEGARBStr){
			
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_EGARB});
			
		}else{
			
			varEGARB = UtilModulos.StringToBigDecimal(varEGARBStr);
			varEGARB = varEGARB.divide(new BigDecimal("100"));

		}
		if(varAnoEsp.equals(new BigDecimal(2001))){
			varPrpssStr2001 = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_PRPSS_2001, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_PRPSS_2001);
			if(null == varPrpssStr2001){

				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_PRPSS_2001});

			}else{

				varPrpss2001 = UtilModulos.StringToBigDecimal(varPrpssStr2001);

			}
			varPrpssStr2002 = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_PRPSS_2002, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_PRPSS_2002);
			if(null == varPrpssStr2002){

				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_PRPSS_2002});

			}else{

				varPrpss2002 = UtilModulos.StringToBigDecimal(varPrpssStr2002);

			}	
			varPrpssStrREST = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_PRPSS_REST, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_PRPSS_REST);
			if(null == varPrpssStrREST){

				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_PRPSS_REST});

			}else{

				varPrpssREST = UtilModulos.StringToBigDecimal(varPrpssStrREST);

			}
		}else if(varAnoEsp.equals(new BigDecimal(2002))){
			varPrpssStr2002 = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_PRPSS_2002, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_PRPSS_2002);
			if(null == varPrpssStr2002){

				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_PRPSS_2002});

			}else{

				varPrpss2002 = UtilModulos.StringToBigDecimal(varPrpssStr2002);

			}	
			varPrpssStrREST = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_PRPSS_REST, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_PRPSS_REST);
			if(null == varPrpssStrREST){

				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_PRPSS_REST});

			}else{

				varPrpssREST = UtilModulos.StringToBigDecimal(varPrpssStrREST);

			}
		}else if(varAnoEsp.compareTo(new BigDecimal(2002)) == 1){
			varPrpssStrREST = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_PRPSS_REST, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_PRPSS_REST);
			if(null == varPrpssStrREST){

				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_PRPSS_REST});

			}else{

				varPrpssREST = UtilModulos.StringToBigDecimal(varPrpssStrREST);

			}
		}
		
		//varFactorPrpss = BigDecimal.ONE.add(varPrpss);
		varFecJ = UtilFechas.getFecha(bloqueCorriente.getFechaDevengo());
		varAnoj = UtilFechas.getAnio(varFecJ.toTimestamp());	
			
		//varviue102 = varEGARB.subtract(varE1PSV).multiply((varFactorPrpss).pow(varAnoj-varAnoEsp.intValue()));
					
		if(varAnoEsp.equals(new BigDecimal(2001))){
			auxPRPSS2001 = BigDecimal.ONE.add(varPrpss2001);
			auxPRPSS2002 = BigDecimal.ONE.add(varPrpss2002);
			auxPRPSSREST = BigDecimal.ONE.add(varPrpssREST);
			
			varviue102 = varEGARB.subtract(varE1PSV.multiply(auxPRPSS2001).multiply(auxPRPSS2002).multiply(auxPRPSSREST.pow(varAnoj-varAnoEsp.intValue()-2)));
			
		}else if(varAnoEsp.equals(new BigDecimal(2002))){
			auxPRPSS2002 = BigDecimal.ONE.add(varPrpss2002);
			auxPRPSSREST = BigDecimal.ONE.add(varPrpssREST);
			
			varviue102 = varEGARB.subtract(varE1PSV.multiply(auxPRPSS2002).multiply(auxPRPSSREST.pow(varAnoj-varAnoEsp.intValue()-1)));
			
		}else if(varAnoEsp.compareTo(new BigDecimal(2002)) == 1){
			auxPRPSSREST = BigDecimal.ONE.add(varPrpssREST);
			
			varviue102 = varEGARB.subtract(varE1PSV.multiply(auxPRPSSREST.pow(varAnoj-varAnoEsp.intValue())));
			
		}		
		
		if (ModuloVIUE102.LOG.isTraceEnabled()) {
			ModuloVIUE102.LOG.trace("Fin de la función << moduloVIUE102 >> de la clase ModuloVIUE102, para la iteración = {}", iteracion);
		}
		
		return varviue102;
	}


}
