package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.FlujosProbablesKey;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.FlujosProbablesDao;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilFechas.Fecha;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloVIUS01 implements Modulo {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloPRI003R.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VIUS01;	
	
	private static final String CLAVE_ANOESP = ConstantsModulos.CTE_ANOESP;
	private static final String CLAVE_VAR_ANOESP = CLAVE_ANOESP.concat(CLAVE_MODULO);	
	private static final String CLAVE_P1JUB= ConstantsModulos.CTE_VAR_P1JUB;
	private static final String CLAVE_VAR_P1JUB = CLAVE_P1JUB.concat(CLAVE_MODULO);
	private static final String CLAVE_P2VIV = ConstantsModulos.CTE_VAR_P2VIV;
	private static final String CLAVE_VAR_P2VIV = CLAVE_P2VIV.concat(CLAVE_MODULO);
	private static final String CLAVE_PRPSS = ConstantsModulos.CTE_PRPSS;
	private static final String CLAVE_PRPSS_2001 = ConstantsModulos.CTE_PRPSS_2001;
	private static final String CLAVE_PRPSS_2002 = ConstantsModulos.CTE_PRPSS_2002;
	private static final String CLAVE_PRPSS_REST = ConstantsModulos.CTE_PRPSS_REST;
	private static final String CLAVE_VAR_PRPSS = CLAVE_PRPSS.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PRPSS_2001 = CLAVE_PRPSS_2001.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PRPSS_2002 = CLAVE_PRPSS_2002.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PRPSS_REST = CLAVE_PRPSS_REST.concat(CLAVE_MODULO);
	private static final String CLAVE_P3TOP = ConstantsModulos.CTE_VAR_P3TOP;
	private static final String CLAVE_VAR_P3TOP = CLAVE_P3TOP.concat(CLAVE_MODULO);
	private static final String CLAVE_SWC1S = ConstantsModulos.CTE_VAR_SWC1S;
	private static final String CLAVE_VAR_SWC1S = CLAVE_SWC1S.concat(CLAVE_MODULO);
	private static final String CLAVELISTACORRUMIC = ConstantsModulos.CTE_LST_CORR_UMIC.concat(CLAVE_MODULO);

	
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/**
	 * Módulo de cuantía nominal para viudedado.
	 */
	
	@Override
	public Object execute(Object... args) throws Solvencia2Excepcion {
		
		BigDecimal cspvius01= BigDecimal.ZERO;
		
		try {
			if (ModuloVIUS01.LOG.isTraceEnabled()) {
				ModuloVIUS01.LOG.trace("Inicio de execute en clase ModuloVIUS01");
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
			
			cspvius01 = moduloVIUS01(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVIUS01.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVIUS01.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
	
		if (ModuloVIUS01.LOG.isTraceEnabled()) {
			ModuloVIUS01.LOG.trace("Fin de execute en clase ModuloVIUS01");
		}
		
		return cspvius01;
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
	
	private BigDecimal moduloVIUS01(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {
		
		BigDecimal varvius01 = BigDecimal.ZERO;
		BigDecimal varFactorPrpss = BigDecimal.ZERO;
		BigDecimal varPct = BigDecimal.ZERO;
		String  varAnoEspStr, varP1JUBStr, varP2VIVStr, varSWC1SStr, varP3TOPStr, varPrpssStr;
		BigDecimal varAnoEsp, varP1JUB, varP2VIV, varP3TOP = BigDecimal.ZERO, varPrpss;
		final IObtenerDatos servicioDatos = FachadaServicios.getObtenerDatos();
		List<DetalleCorriente> varProyUmicTitular = null;
		Umic umicTitular;
		Fecha varFecJ;
		Integer varAnoj;
		BigDecimal varPSJj = BigDecimal.ZERO, varPSVj = BigDecimal.ZERO, varC1sant = BigDecimal.ZERO;
		BigDecimal varNumPrestaciones;
		BigDecimal varNs = BigDecimal.ZERO;
		String varPrpssStr2001, varPrpssStr2002, varPrpssStrREST;
		BigDecimal varPrpss2001 = BigDecimal.ZERO, varPrpss2002 = BigDecimal.ZERO, varPrpssREST = BigDecimal.ZERO; 
		BigDecimal auxPRPSS2001 = BigDecimal.ZERO;
		BigDecimal auxPRPSS2002 = BigDecimal.ZERO;
		BigDecimal auxPRPSSREST = BigDecimal.ZERO;
		List<Umic> umics;
		UmicKey varClavePrincipal;
		Modulo moduloNominalUmicTitular = null;
		List<DetalleCorriente> calculoProyUmicTitular;
		FlujosProbablesDao flujosProbablesDao = new FlujosProbablesDao();

		
		if (ModuloVIUS01.LOG.isTraceEnabled()) {
			ModuloVIUS01.LOG.trace("Inicio función << moduloVIUS01 >> de la clase ModuloVIUS01, para la  iteracion = {}", iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Fin de la definición de las variables auxiliares.	
		
		//Varibales de apoyo
		varPct = ConstantsFunciones.CTE_OPER_75.divide(ConstantsFunciones.CTE_OPER_100);
		varAnoEspStr = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_ANOESP, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_ANOESP);
		if(null == varAnoEspStr){
			
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_ANOESP});
			
		}else{
			varAnoEsp = new BigDecimal(varAnoEspStr);
		}
		varP1JUBStr = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_P1JUB, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_P1JUB);
		if(null == varP1JUBStr){
			varP1JUB = BigDecimal.ZERO;
			//throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_P1JUB});
			final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
			Incidencia aviso1 = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(), umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(), btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(), ConstantsFunciones.CTE_COD_ERROR_DN, new Object[]{CLAVE_P1JUB});
			servicio.almacenarIncidencias(aviso1);
			
		}else{
			varP1JUB = new BigDecimal(varP1JUBStr).setScale(2, RoundingMode.HALF_UP);
			varP1JUB = varP1JUB.divide(new BigDecimal("100"));
			//varP1JUB = UtilModulos.StringToBigDecimal(varP1JUBStr);
			
		}
	
		varP2VIVStr = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_P2VIV, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_P2VIV);
		if(null == varP2VIVStr){
			
			//throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_P2VIV});
			varP2VIV = BigDecimal.ZERO;
			//throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_P1JUB});
			final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
			Incidencia aviso2 = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(), umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(), btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(), ConstantsFunciones.CTE_COD_ERROR_DP, new Object[]{CLAVE_P2VIV});
			servicio.almacenarIncidencias(aviso2);
			
		}else{
			varP2VIV = new BigDecimal(varP2VIVStr).setScale(2, RoundingMode.HALF_UP);
			varP2VIV = varP2VIV.divide(new BigDecimal("100"));
			//varP2VIV = UtilModulos.StringToBigDecimal(varP2VIVStr);
		
		}
		
		varSWC1SStr = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_SWC1S, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_SWC1S);
		
		/*if(null == varSWC1SStr){
			
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_SWC1S});
			
		}*/
		
		
		if(null == varSWC1SStr  || !varSWC1SStr.equals("S")){
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
			}else{
				if(null == varProyUmicTitular.get(iteracion-1).getBloqueBySubproceso(codSubproceso) || null == varProyUmicTitular.get(iteracion-1).getBloqueBySubproceso(codSubproceso).getImpFlujoNominal()){
					varC1sant = BigDecimal.ZERO;
				}else{
					varC1sant = varProyUmicTitular.get(iteracion-1).getBloqueBySubproceso(codSubproceso).getImpFlujoNominal();
				}
			}
			
		}else{
			varP3TOPStr = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_P3TOP, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_P3TOP);
			if(null == varP3TOPStr){
			
				//throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_P3TOP});
				varP3TOP = BigDecimal.ZERO;
				//throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_P1JUB});
				final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
				Incidencia aviso3 = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(), umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(), btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(), ConstantsFunciones.CTE_COD_ERROR_DQ, new Object[]{CLAVE_P3TOP});
				servicio.almacenarIncidencias(aviso3);
				
			}else{
				varP3TOP = new BigDecimal(varP3TOPStr).setScale(2, RoundingMode.HALF_UP);
				varP3TOP = varP3TOP.divide(new BigDecimal("100"));
				//varP3TOP = UtilModulos.StringToBigDecimal(varP3TOPStr);
			}	
			
			varC1sant = varP3TOP.divide(ConstantsFunciones.CTE_OPER_14, ConstantsFunciones.MATH_CONTEXT);
			
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
		
		final IObtenerDatos obtenerDatos = FachadaServicios.getObtenerDatos();		
		umics = servicioDatos.recuperarTodasUmicsTitularSuscripcion(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), ConstantsModulos.CTE_UMIC_PPAL);
		
		varNumPrestaciones=BigDecimal.ONE;
		if(umic.getAsegurados().getFnacAseg2() != null)
			varNumPrestaciones.add(BigDecimal.ONE);
		if(umic.getAsegurados().getFnacAseg3() != null)
			varNumPrestaciones.add(BigDecimal.ONE);
		if(umic.getAsegurados().getFnacAseg4() != null)
			varNumPrestaciones.add(BigDecimal.ONE);
		if(umic.getAsegurados().getFnacAseg5() != null)
			varNumPrestaciones.add(BigDecimal.ONE);
		if(umic.getRentas().getForpagrent() == null)
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DK);
		
		for(int i = 0; i < umics.size(); i++){
		
			varNs = varNs.add(new BigDecimal(umics.get(i).getRentas().getForpagrent()));
			
		}
		
		varFecJ = UtilFechas.getFecha(bloqueCorriente.getFechaDevengo());
		varAnoj = UtilFechas.getAnio(varFecJ.toTimestamp());	
		
		if(varAnoEsp.equals(new BigDecimal(2001))){
			auxPRPSS2001 = BigDecimal.ONE.add(varPrpss2001);
			auxPRPSS2002 = BigDecimal.ONE.add(varPrpss2002);
			auxPRPSSREST = BigDecimal.ONE.add(varPrpssREST);
			
			varPSJj = (varP1JUB.multiply(auxPRPSS2001).multiply(auxPRPSS2002).multiply(auxPRPSSREST.pow(varAnoj-varAnoEsp.intValue()-2))).divide(varNs, ConstantsFunciones.MATH_CONTEXT);
			varPSVj = (varP2VIV.multiply(auxPRPSS2001).multiply(auxPRPSS2002).multiply(auxPRPSSREST.pow(varAnoj-varAnoEsp.intValue()-2))).divide(varNs, ConstantsFunciones.MATH_CONTEXT);
		
			
		}else if(varAnoEsp.equals(new BigDecimal(2002))){
			auxPRPSS2002 = BigDecimal.ONE.add(varPrpss2002);
			auxPRPSSREST = BigDecimal.ONE.add(varPrpssREST);
			
			varPSJj = (varP1JUB.multiply(auxPRPSS2002).multiply(auxPRPSSREST.pow(varAnoj-varAnoEsp.intValue()-1))).divide(varNs, ConstantsFunciones.MATH_CONTEXT);
			varPSVj = (varP2VIV.multiply(auxPRPSS2002).multiply(auxPRPSSREST.pow(varAnoj-varAnoEsp.intValue()-1))).divide(varNs, ConstantsFunciones.MATH_CONTEXT);
			
			
		}else if(varAnoEsp.compareTo(new BigDecimal(2002)) == 1){
			auxPRPSSREST = BigDecimal.ONE.add(varPrpssREST);
			
			varPSJj = (varP1JUB.multiply(auxPRPSSREST.pow(varAnoj-varAnoEsp.intValue()))).divide(varNs, ConstantsFunciones.MATH_CONTEXT);
			varPSVj = (varP2VIV.multiply(auxPRPSSREST.pow(varAnoj-varAnoEsp.intValue()))).divide(varNs, ConstantsFunciones.MATH_CONTEXT);
			
		}
		
		
		
		
		varvius01 = varPct.multiply(varPSJj.add(varC1sant)).subtract(varPSVj);
				
		if (ModuloVIUS01.LOG.isTraceEnabled()) {
			ModuloVIUS01.LOG.trace("Fin de la función << moduloVIUS01 >> de la clase ModuloVIUS01, para la iteración = {}", iteracion);
		}
		
		return varvius01;
	}

}