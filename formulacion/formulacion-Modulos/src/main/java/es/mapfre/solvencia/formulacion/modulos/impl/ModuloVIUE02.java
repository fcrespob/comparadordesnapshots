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
import es.mapfre.solvencia.servicios.impl.ObtenerDatos;													   
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloVIUE02 implements Modulo {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloPRI003R.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VIUE02;	
	
	private static final String CLAVE_E2PST = ConstantsModulos.CTE_E2PST;
	private static final String CLAVE_VAR_E2PST = CLAVE_E2PST.concat(CLAVE_MODULO);	
	private static final String CLAVE_ANOESP = ConstantsModulos.CTE_ANOESP;
	private static final String CLAVE_VAR_ANOESP = CLAVE_ANOESP.concat(CLAVE_MODULO);	
	private static final String CLAVE_E1PSJ = ConstantsModulos.CTE_E1PSJ;
	private static final String CLAVE_VAR_E1PSJ = CLAVE_E1PSJ.concat(CLAVE_MODULO);
	private static final String CLAVE_PORVIUSS = ConstantsModulos.CTE_PORVIUSS;
	private static final String CLAVE_VAR_PORVIUSS = CLAVE_PORVIUSS.concat(CLAVE_MODULO);
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
		
		BigDecimal viue02= BigDecimal.ZERO;
		
		try {
			if (ModuloVIUE02.LOG.isTraceEnabled()) {
				ModuloVIUE02.LOG.trace("Inicio de execute en clase ModuloVIUE02");
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
			
			viue02 = moduloVIUE02(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVIUE02.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVIUE02.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
	
		if (ModuloVIUE02.LOG.isTraceEnabled()) {
			ModuloVIUE02.LOG.trace("Fin de execute en clase ModuloVIUE02");
		}
		
		return viue02;
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
	
	private BigDecimal moduloVIUE02(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {
		
		BigDecimal varviue02 = BigDecimal.ZERO;
		BigDecimal varFactorPrpss = BigDecimal.ZERO;
		String  varAnoEspStr, varPrpssStr, varE1PSJStr, varPorviussStr, VarPsj0Str;
		int anoEsp;
		BigDecimal varAnoEsp = BigDecimal.ONE;
		BigDecimal varPrpss;
		BigDecimal varE1PSJ = BigDecimal.ONE; 
		BigDecimal varPorviuss = BigDecimal.ONE; 
		BigDecimal VarPsj0 = BigDecimal.ONE;
		BigDecimal varPct = BigDecimal.ONE;
		final IObtenerDatos servicioDatos = FachadaServicios.getObtenerDatos();
		List<DetalleCorriente> varProyUmicTitular = null;
		Umic umicTitular;
		Fecha varFecJ;
		Integer varAnoj;
		BigDecimal varPSJj = BigDecimal.ZERO, varPSVj = BigDecimal.ZERO, varC1sant = null;
		String varPrpssStr2001, varPrpssStr2002, varPrpssStrREST;
		BigDecimal varPrpss2001 = BigDecimal.ZERO, varPrpss2002 = BigDecimal.ZERO, varPrpssREST = BigDecimal.ZERO; 
		BigDecimal auxPRPSS2001 = BigDecimal.ZERO;
		BigDecimal auxPRPSS2002 = BigDecimal.ZERO;
		BigDecimal auxPRPSSREST = BigDecimal.ZERO;
		BigDecimal aux1 = BigDecimal.ZERO;
		BigDecimal aux2 = BigDecimal.ZERO;
		BigDecimal aux1_2 = BigDecimal.ZERO;
		BigDecimal aux3 = BigDecimal.ZERO;
		BigDecimal aux4 = BigDecimal.ZERO;
		BigDecimal aux5 = BigDecimal.ZERO;
		int auxiliarPow;
		BigDecimal calculoAuxiliar = BigDecimal.ZERO;
		Modulo moduloNominalUmicTitular = null;
		List<DetalleCorriente> calculoProyUmicTitular;
		FlujosProbablesDao flujosProbablesDao = new FlujosProbablesDao();
		
		if (ModuloVIUE02.LOG.isTraceEnabled()) {
			ModuloVIUE02.LOG.trace("Inicio función << moduloVIUE02 >> de la clase ModuloVIUE02, para la  iteracion = {}", iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Fin de la definición de las variables auxiliares.	
		
		if(null == bloqueCorriente.getFechaDevengo()){
			
			return varviue02;	
			
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
//			FlujosProbablesKey key = new FlujosProbablesKey(umicTitular.getDatosGenerales().getKmodalidad(), umicTitular.getDatosGenerales().getKgarantia(), umicTitular.getDatosGenerales().getKprestacion(), btcUmic.getBt());
//			FlujosProbables fp = flujosProbablesDao.get(key);
//			if(null == fp){
//				key = new FlujosProbablesKey(umicTitular.getDatosGenerales().getKmodalidad(), umicTitular.getDatosGenerales().getKgarantia(), umicTitular.getDatosAdicionales().getPrestCal(), btcUmic.getBt());
//				fp = flujosProbablesDao.get(key);
//			}
			
			FlujosProbablesKey key;
			//throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DM, new Object[]{});
			if(btcUmic.getBt().equals("ROSSPGA") || btcUmic.getBt().equals("ROSSPTI")  || btcUmic.getBt().equals("ROSSPTE")){
				 key = new FlujosProbablesKey(umicTitular.getDatosGenerales().getKmodalidad(), umicTitular.getDatosGenerales().getKgarantia(), umicTitular.getDatosGenerales().getKprestacion(), "ROSSP");
			}else{
				 key = new FlujosProbablesKey(umicTitular.getDatosGenerales().getKmodalidad(), umicTitular.getDatosGenerales().getKgarantia(), umicTitular.getDatosGenerales().getKprestacion(), btcUmic.getBt());
			}
			FlujosProbables fp = flujosProbablesDao.get(key);
			if(null == fp){
				if(btcUmic.getBt().equals("ROSSPGA") || btcUmic.getBt().equals("ROSSPTI")  || btcUmic.getBt().equals("ROSSPTE")){
					key = new FlujosProbablesKey(umicTitular.getDatosGenerales().getKmodalidad(), umicTitular.getDatosGenerales().getKgarantia(), umicTitular.getDatosAdicionales().getPrestCal(), "ROSSP");
				}else{
					key = new FlujosProbablesKey(umicTitular.getDatosGenerales().getKmodalidad(), umicTitular.getDatosGenerales().getKgarantia(), umicTitular.getDatosAdicionales().getPrestCal(), btcUmic.getBt());
				}

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
		
		
		
		
		if(umic.getDatosGenerales().getNsuscri() == 1){ 
			VarPsj0Str = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_E2PST, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_E2PST);
			if(null == VarPsj0Str){
				
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_E2PST});
				
			}else{
				VarPsj0 = UtilModulos.StringToBigDecimal(VarPsj0Str);
				VarPsj0 = VarPsj0.divide(ConstantsFunciones.CTE_OPER_100);
				varPct = ConstantsFunciones.CTE_OPER_60.divide(ConstantsFunciones.CTE_OPER_100);
			}
		}else{
			anoEsp = UtilFechas.getAnio(umic.getFechas().getFecinisus());
			varAnoEsp = new BigDecimal(anoEsp);
			varE1PSJStr = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_E1PSJ, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_E1PSJ);
			if(null == varE1PSJStr){
				
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_E1PSJ});
				
			}else{
				varE1PSJ = UtilModulos.StringToBigDecimal(varE1PSJStr);
				varE1PSJ = varE1PSJ.setScale(2, RoundingMode.HALF_UP);
				varE1PSJ = varE1PSJ.divide(ConstantsFunciones.CTE_OPER_100);	
				//varE1PSJ = UtilModulos.StringToBigDecimal(varE1PSJStr);

				
			}
			varPorviussStr = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_PORVIUSS, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_PORVIUSS);
			if(null == varPorviussStr){
				
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_PORVIUSS});
				
			}else{
				
				varPorviuss = UtilModulos.StringToBigDecimal(varPorviussStr);

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
		}
		varFecJ = UtilFechas.getFecha(bloqueCorriente.getFechaDevengo());
		
			if(umic.getDatosGenerales().getNsuscri() == 1){
				aux1 = VarPsj0.multiply(ConstantsFunciones.CTE_OPER_14);
				aux2 = varC1sant.multiply(ConstantsFunciones.CTE_OPER_12);
				aux1_2 = aux1.add(aux2);
				aux3 = varPct.multiply(aux1_2);
				
				varviue02 = aux3.divide(ConstantsFunciones.CTE_OPER_12, ConstantsFunciones.MATH_CONTEXT);
			}else{
				varFecJ = UtilFechas.getFecha(bloqueCorriente.getFechaDevengo());
				varAnoj = UtilFechas.getAnio(varFecJ.toTimestamp());	
				if(varAnoEsp.equals(new BigDecimal(2001))){
					
					auxPRPSS2001 = BigDecimal.ONE.add(varPrpss2001);
					auxPRPSS2002 = BigDecimal.ONE.add(varPrpss2002);
					auxPRPSSREST = BigDecimal.ONE.add(varPrpssREST);
					auxiliarPow = varAnoj-varAnoEsp.intValue()-2;
					calculoAuxiliar = auxPRPSSREST.pow(auxiliarPow);
					varPSJj = varE1PSJ.multiply(auxPRPSS2001).multiply(auxPRPSS2002).multiply(calculoAuxiliar);
				
				}else if(varAnoEsp.equals(new BigDecimal(2002))){
					auxPRPSS2002 = BigDecimal.ONE.add(varPrpss2002);
					auxPRPSSREST = BigDecimal.ONE.add(varPrpssREST);
					auxiliarPow = varAnoj-varAnoEsp.intValue()-1;
					calculoAuxiliar = auxPRPSSREST.pow(auxiliarPow); 
					varPSJj = varE1PSJ.multiply(auxPRPSS2002).multiply(calculoAuxiliar);
				
				}else if(varAnoEsp.compareTo(new BigDecimal(2002)) == 1){
					auxPRPSSREST = BigDecimal.ONE.add(varPrpssREST);
					auxiliarPow = varAnoj-varAnoEsp.intValue();
					calculoAuxiliar = auxPRPSSREST.pow(auxiliarPow);
					varPSJj = varE1PSJ.multiply(calculoAuxiliar);
				
				}
				
				varPct = ConstantsFunciones.CTE_OPER_60.divide(ConstantsFunciones.CTE_OPER_100);
				varPSVj = varPorviuss.multiply(varPSJj);
				aux1 = varPSJj.multiply(ConstantsFunciones.CTE_OPER_14);
				aux2 = varC1sant.multiply(ConstantsFunciones.CTE_OPER_12);
				aux1_2 = aux1.add(aux2);
				aux3 = varPct.multiply(aux1_2);
				aux4 = varPSVj.multiply(ConstantsFunciones.CTE_OPER_14);
				aux5 = aux3.subtract(aux4);
				varviue02 = aux5.divide(ConstantsFunciones.CTE_OPER_12, ConstantsFunciones.MATH_CONTEXT);
			}
			
						
		if (ModuloVIUE02.LOG.isTraceEnabled()) {
			ModuloVIUE02.LOG.trace("Fin de la función << moduloVIUE02 >> de la clase ModuloVIUE02, para la iteración = {}", iteracion);
		}
		
		return varviue02;
	}


}
