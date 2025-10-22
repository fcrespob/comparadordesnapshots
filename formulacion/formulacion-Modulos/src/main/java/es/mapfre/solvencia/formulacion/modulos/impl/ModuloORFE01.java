package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.FlujosProbablesKey;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.FlujosProbablesDao;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
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
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloORFE01 implements Modulo {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloPRI003R.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_ORFE01;	
	
	private static final String CLAVE_E2PST = ConstantsModulos.CTE_E2PST;
	private static final String CLAVE_VAR_E2PST = CLAVE_E2PST.concat(CLAVE_MODULO);	
	private static final String CLAVE_ANOESP = ConstantsModulos.CTE_ANOESP;
	private static final String CLAVE_VAR_ANOESP = CLAVE_ANOESP.concat(CLAVE_MODULO);	
	private static final String CLAVE_E1PSJ = ConstantsModulos.CTE_E1PSJ;
	private static final String CLAVE_VAR_E1PSJ = CLAVE_E1PSJ.concat(CLAVE_MODULO);
	private static final String CLAVE_PORORFSS = ConstantsModulos.CTE_PORORFSS;
	private static final String CLAVE_VAR_PORORFSS = CLAVE_PORORFSS.concat(CLAVE_MODULO);
	private static final String CLAVE_PRPSS = ConstantsModulos.CTE_PRPSS;
	private static final String CLAVE_PRPSS_2001 = ConstantsModulos.CTE_PRPSS_2001;
	private static final String CLAVE_PRPSS_2002 = ConstantsModulos.CTE_PRPSS_2002;
	private static final String CLAVE_PRPSS_REST = ConstantsModulos.CTE_PRPSS_REST;
	private static final String CLAVE_VAR_PRPSS_2001 = CLAVE_PRPSS_2001.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PRPSS_2002 = CLAVE_PRPSS_2002.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PRPSS_REST = CLAVE_PRPSS_REST.concat(CLAVE_MODULO);	
	private static final String CLAVE_VAR_PRPSS = CLAVE_PRPSS.concat(CLAVE_MODULO);
	private static final String CLAVE_LIMITEVIU = ConstantsModulos.CTE_LIMITEVIU;
	private static final String CLAVE_VAR_LIMITEVIU = CLAVE_LIMITEVIU.concat(CLAVE_MODULO);
	
	
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/**
	 * Módulo que calcula una prestación temporal de orfandad, cuyo vencimiento dependerá de cada póliza, y estará definido en la tabla de datos de Sepi / Endesa.
	 */
	
	@Override
	public Object execute(Object... args) throws Solvencia2Excepcion {
		
		BigDecimal varorfe01= BigDecimal.ZERO;
		
		try {
			if (ModuloORFE01.LOG.isTraceEnabled()) {
				ModuloORFE01.LOG.trace("Inicio de execute en clase ModuloORFE01");
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
			
			varorfe01 = moduloORFE01(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloORFE01.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloORFE01.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
	
		if (ModuloORFE01.LOG.isTraceEnabled()) {
			ModuloORFE01.LOG.trace("Fin de execute en clase ModuloORFE01");
		}
		
		return varorfe01;
	}

	
	/**
	 * Módulo que calcula una prestación temporal de orfandad, cuyo vencimiento dependerá de cada póliza, y estará definido en la tabla de datos de Sepi / Endesa
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
	
	private BigDecimal moduloORFE01(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {
		
		BigDecimal varorfe01 = BigDecimal.ZERO;
		BigDecimal varFactorPrpss = BigDecimal.ZERO;
		String  varAnoEspStr, varPrpssStr, varE1PSJStr, varPororfssStr;
		BigDecimal varAnoEsp = BigDecimal.ONE;
		BigDecimal varPrpss;
		BigDecimal varE1PSJ = BigDecimal.ONE; 
		BigDecimal varPororfss = BigDecimal.ONE; 
		BigDecimal varPct = BigDecimal.ONE;
		final IObtenerDatos servicioDatos = FachadaServicios.getObtenerDatos();
		List<DetalleCorriente> varProyUmicTitular = null;
		Umic umicTitular;
		Fecha varFecJ;
		Integer varAnoj;
		BigDecimal varPSJj = BigDecimal.ZERO, varPSOj, varC1sant = null;
		String varPrpssStr2001, varPrpssStr2002, varPrpssStrREST;
		BigDecimal varPrpss2001 = BigDecimal.ZERO, varPrpss2002 = BigDecimal.ZERO, varPrpssREST = BigDecimal.ZERO; 
		BigDecimal auxPRPSS2001 = BigDecimal.ZERO;
		BigDecimal auxPRPSS2002 = BigDecimal.ZERO;
		BigDecimal auxPRPSSREST = BigDecimal.ZERO;
		Modulo moduloNominalUmicTitular = null;
		List<DetalleCorriente> calculoProyUmicTitular;
		FlujosProbablesDao flujosProbablesDao = new FlujosProbablesDao();
		
		if (ModuloORFE01.LOG.isTraceEnabled()) {
			ModuloORFE01.LOG.trace("Inicio función << moduloORFE01 >> de la clase ModuloORFE01, para la  iteracion = {}", iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Fin de la definición de las variables auxiliares.	
		
		if(null == bloqueCorriente.getFechaDevengo()){
			return varorfe01;
		}
		
		//Varibales de apoyo
		umicTitular = servicioDatos.recuperarUmicTitular(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), ConstantsModulos.CTE_UMIC_PPAL, umic.getRentas().getFecIni(), umic.getRentas().getForpagrent(), umic.getRentas().getCpagrenta());
		if(umicTitular == null){
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DL, new String[]{umic.getDatosGenerales().getKmodalidad().toString(), umic.getDatosGenerales().getKpoliza().toString(), umic.getDatosGenerales().getKsubpoliza().toString(), umic.getDatosGenerales().getKcertificado().toString(), umic.getDatosGenerales().getNsuscri().toString()});
		}
		varProyUmicTitular  = servicioDatos.recuperarProyeccion(ConstantsModulos.CTE_BTI, btcUmic.getFecCierre(), umicTitular.getKey());
		
		if(null == varProyUmicTitular || varProyUmicTitular.size() == 0){
			varProyUmicTitular  = servicioDatos.recuperarProyeccion(ConstantsModulos.CTE_BTI_PROY, btcUmic.getFecCierre(), umicTitular.getKey());
		}
		
		if(varProyUmicTitular.size() == ConstantsFunciones.CTE_0){
			FlujosProbablesKey key = new FlujosProbablesKey(umicTitular.getDatosGenerales().getKmodalidad(), umicTitular.getDatosGenerales().getKgarantia(), umicTitular.getDatosGenerales().getKprestacion(), btcUmic.getBt());
			FlujosProbables fp = flujosProbablesDao.get(key);
			if(null == fp){
				key = new FlujosProbablesKey(umicTitular.getDatosGenerales().getKmodalidad(), umicTitular.getDatosGenerales().getKgarantia(), umicTitular.getDatosAdicionales().getPrestCal(), btcUmic.getBt());
				fp = flujosProbablesDao.get(key);
			}

			calculoProyUmicTitular = proyUmic;
			moduloNominalUmicTitular = FactoriaModulos.getModulo(fp.getVida().getNominal());
			if(null != moduloNominalUmicTitular){
				varC1sant = (BigDecimal) moduloNominalUmicTitular.execute(calculoProyUmicTitular, bloqueCorriente, iteracion, fcalc, umicTitular, btcUmic, mapVariables, codSubproceso);
			}		
			if(null == varC1sant){

				varC1sant = BigDecimal.ZERO;

			}
			//throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DM, new Object[]{});
		}else{
			if(null == varProyUmicTitular.get(iteracion-1).getBloqueBySubproceso(codSubproceso) || null == varProyUmicTitular.get(iteracion-1).getBloqueBySubproceso(codSubproceso).getImpFlujoNominal()){
				varC1sant = BigDecimal.ZERO;
			}else{
				varC1sant = varProyUmicTitular.get(iteracion-1).getBloqueBySubproceso(codSubproceso).getImpFlujoNominal();
			}
		}


		
		varPct = ConstantsFunciones.CTE_OPER_20.divide(ConstantsFunciones.CTE_OPER_100);
		varAnoEspStr = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_ANOESP, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_ANOESP);
		if(varAnoEspStr == null){
			
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_ANOESP});
			
		}else{
			//varAnoEspStr = varAnoEspStr.substring(5);
			varAnoEsp = UtilModulos.StringToBigDecimal(varAnoEspStr);
		}
		
		varE1PSJStr = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_E1PSJ, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_E1PSJ);
		if(null == varE1PSJStr){
			
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_E1PSJ});
			
		}else{
			varE1PSJ = UtilModulos.StringToBigDecimal(varE1PSJStr);
			varE1PSJ = varE1PSJ.setScale(2, RoundingMode.HALF_UP);
			varE1PSJ = varE1PSJ.divide(ConstantsFunciones.CTE_OPER_100);	
			//varE1PSJ = UtilModulos.StringToBigDecimal(varE1PSJStr);

		}
		varPororfssStr = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_PORORFSS, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_PORORFSS);
		if(null == varPororfssStr){
			
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_PORORFSS});
			
		}else{
			
			varPororfss = UtilModulos.StringToBigDecimal(varPororfssStr);

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
		/*if(null == varProyUmicTitular.get(iteracion-1).getBloquePrim() || null == varProyUmicTitular.get(iteracion-1).getBloquePrim().getImpFlujoNominal()){
			varC1sant = BigDecimal.ZERO;
		}else{
			varC1sant = varProyUmicTitular.get(iteracion-1).getBloquePrim().getImpFlujoNominal();
		}*/
		if(null == varFecJ){
			varorfe01 = BigDecimal.ZERO;
		}else{
			varAnoj = UtilFechas.getAnio(varFecJ.toTimestamp());	
			//varPSJj = varE1PSJ.multiply((varFactorPrpss).pow(varAnoj-varAnoEsp.intValue()));
			
			if(varAnoEsp.equals(new BigDecimal(2001))){
				
				auxPRPSS2001 = BigDecimal.ONE.add(varPrpss2001);
				auxPRPSS2002 = BigDecimal.ONE.add(varPrpss2002);
				auxPRPSSREST = BigDecimal.ONE.add(varPrpssREST);
				
				varPSJj = varE1PSJ.multiply(auxPRPSS2001).multiply(auxPRPSS2002).multiply(auxPRPSSREST.pow(varAnoj-varAnoEsp.intValue()+2));
			
			}else if(varAnoEsp.equals(new BigDecimal(2002))){
				auxPRPSS2002 = BigDecimal.ONE.add(varPrpss2002);
				auxPRPSSREST = BigDecimal.ONE.add(varPrpssREST);
				
				varPSJj = varE1PSJ.multiply(auxPRPSS2002).multiply(auxPRPSSREST.pow(varAnoj-varAnoEsp.intValue()+1));
			
			}else if(varAnoEsp.compareTo(new BigDecimal(2002)) == 1){
				auxPRPSSREST = BigDecimal.ONE.add(varPrpssREST);
			
				varPSJj = varE1PSJ.multiply(auxPRPSSREST.pow(varAnoj-varAnoEsp.intValue()));
			
			}
			
			varPSOj = varPororfss.multiply(varPSJj);
			varorfe01 = varPct.multiply((varPSJj).add(varC1sant)).subtract(varPSOj);
		}				
		if (ModuloORFE01.LOG.isTraceEnabled()) {
			ModuloORFE01.LOG.trace("Fin de la función << moduloORFE01 >> de la clase ModuloORFE01, para la iteración = {}", iteracion);
		}
		
		return varorfe01;
	}


}
