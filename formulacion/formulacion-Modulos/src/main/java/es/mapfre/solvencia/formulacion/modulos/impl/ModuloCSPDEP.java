package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Rentas;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.modulos.impl.ValidacionesComunesModulos;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloCSPDEP implements Modulo {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSPDEP.class);
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSPDEP;
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);					
	private static final String CLAVE_VAR_X = ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_W = ConstantsModulos.CTE_VAR_WX.concat(CLAVE_MODULO);
	private static final String CLAVE_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT;
	private static final String CLAVE_VAR_VAL_TAB_MORT = CLAVE_VAL_TAB_MORT.concat(CLAVE_MODULO);
	private static final String CLAVE_NPA = ConstantsModulos.CTE_VAR_NPA;
	private static final String CLAVE_VAR_NPA = CLAVE_NPA.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC2 = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);
	
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}
	
	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la llamada a la función que realiza los calculos del modulo.
	 */
	
	@Override
	public Object execute(Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal cspdep = BigDecimal.ZERO;
		//Fin variables locales
		
		try {
			if (ModuloCSPDEP.LOG.isTraceEnabled()) {
				ModuloCSPDEP.LOG.trace("Inicio de execute en clase ModuloCSPDEP");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCSPAMORT
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			//Invocamos a la función de calculo CSPAMORT
			cspdep = moduloCSPDEP(proyUmic,iteracion,fcalc, umic, btcUmic, codSubproceso, bloqueCorriente,mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloCSPDEP.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSPDEP.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSPDEP.LOG.isTraceEnabled()) {
			ModuloCSPDEP.LOG.trace("Fin de execute en clase ModuloCSPDEP");
		}
		
		return cspdep;
	}
	
	
	/**
	 * Módulo para el cálculo de los capitales vivos del préstamo y cálculo de los capitales asegurados.
	 * @param proyUmic
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param codSubproceso
	 * @param bloqueCorriente
	 * @param mapVariables
	 * @return cspamort
	 */
	
	
	private BigDecimal moduloCSPDEP(List<DetalleCorriente> proyUmic, int iteracion, Timestamp fcalc, Umic umic,
			DetalleBaseTecnica btcUmic, String codSubproceso, BloqueCorriente bloqueCorriente,
			Map<String, Object> mapVariables) {
		
		//Variables locales
		BigDecimal cspdep = BigDecimal.ZERO;
		String varCriterFec = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriterioEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		Timestamp varFechaEfecto = null;
		Integer varTCm = Integer.valueOf(ConstantsFunciones.CTE_0);
		Integer varBeta = null;
		BigDecimal varI1 = BigDecimal.ZERO;
		BigDecimal varVA = BigDecimal.ONE;
		BigDecimal varPA = BigDecimal.ONE;
		BigDecimal varRentaA1 = BigDecimal.ONE;
		BigDecimal varP1;
		BigDecimal varV1;
		BigDecimal factorJ;
		BigDecimal factorJ1;
		BigDecimal aux_factorJ;
		BigDecimal aux_factorJ1;
		BigDecimal varEj;
		BigDecimal varRenta;
		BigDecimal varGIC;
		Integer varM;
		String varNPA;
		BigDecimal j;
		Integer x = 0;
		BigDecimal factorJ_1;
		BigDecimal factorJ_2;
		BigDecimal factorJ_3;
		BigDecimal factorJ1_1;
		BigDecimal factorJ1_2;
		BigDecimal factorJ1_3;
		BigDecimal qz1;
		BigDecimal           varX;
		String           	 varCriEdad;
		Integer              varEdifer;
		BigDecimal t1;
		BigDecimal t2 = BigDecimal.ZERO;
		BigDecimal t3;
		BigDecimal t2_1;
		BigDecimal t2_2;
		BigDecimal t2_3;
		Integer varW;
		List<BigDecimal> lstValoresTabMort = null;
		DetalleBaseTecnica  varBtcUmic2;
		String TablaIni;
		String TablaFin;
		BigDecimal LxEntero = BigDecimal.ZERO;
		BigDecimal LxEntero1 = BigDecimal.ZERO;
		BigDecimal LxEntero2 = BigDecimal.ZERO;
		Integer LxEnt1 = 0;
		Integer j1 = 0;
		
		
		//Fin variables locales
		
		if (ModuloCSPDEP.LOG.isTraceEnabled()) {
			ModuloCSPDEP.LOG.trace("Inicio función << moduloCSPDEP >> para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		// Definición de variables auxiliares para agilizar las operaciones (se llaman varias veces).
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		// Fin de la definición de las variables auxiliares.		
		
		
		//Variables de apoyo
		varCriterioEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
		
		//Validacion variables de apoyo
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFec);
		}
		
		if(bloqueCorriente.getFechaDevengo() == null){
			return cspdep;
			
		}
		
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		varCriEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		
		varNPA = UtilModulos.getsetStringRecuperarDefinicionAuxiliar(mapVariables, CLAVE_VAR_NPA, ccartera, kmodalidad, kgarantia,btcUmic.getBaseTec(), CLAVE_NPA);
		if (varNPA == null) {
			varNPA = "0";
		}
		varBeta = iteracion - 1;
		varTCm = UtilModulos.getVarTCmIni(mapVariables, CLAVE_VAR_TCM, ConstantsModulos.CTE_FIRST_ITER, varFechaEfecto,
				fcalc);
		
		final String codk1 = umic.getRescates().getKrescate1();
		final Integer durk1 = UtilModulos.obtenerDuracionCodKX(codk1, varFechaEfecto, bloqueCorriente.getFechaDevengo(), 
				umic.getDuraciones().getNdurprima(), umic.getDuraciones().getNdursegmes());
		
		BigDecimal vark = UtilModulos.getCteRescate(mapVariables, codk1, durk1);
		if (vark == null) {
			vark = BigDecimal.ZERO;
		}
		varI1 = btcUmic.getItcalc().get(0);
		varRenta = umic.getRentas().getRentact();
		varGIC = umic.getBti().getPgastgesin1I().divide(new BigDecimal(100), ConstantsFunciones.MATH_CONTEXT);
		
		varM = obtenerVarM(umic.getRentas());
		
		varEdifer = umic.getDatosGenerales().getEdifer();
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriEdad, umic.getRentas().getFecIni(), varEdifer);
		x = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriEdad, umic.getRentas().getFecIni(), varEdifer).intValue();
		TablaIni = btcUmic.getTablasConversionAsegurado().get(0).get(0).getTablaInicio();
		TablaFin = btcUmic.getTablasConversionAsegurado().get(0).get(0).getTablaFin();
		varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC2);
		if(null == varBtcUmic2){
			varBtcUmic2 = new DetalleBaseTecnica();
			try {
				PropertyUtils.copyProperties(varBtcUmic2, btcUmic);
			} catch (Exception e) {
				ModuloCSPDEP.LOG.error(e.getMessage());
			}
			
			varBtcUmic2.setTablacalc1aseg1(btcUmic.getTablacalc2aseg1());
			varBtcUmic2.getTablasConversionAsegurado().get(0).get(0).setTablaFin(btcUmic.getTablacalc2aseg1());
			varBtcUmic2.getTablasConversionAsegurado().get(0).get(0).setTablaInicio(btcUmic.getTablacalc2aseg1());
			mapVariables.put(CLAVE_BTC_UMIC2, varBtcUmic2);
		}
		
		varW = UtilModulos.getVarEdadMaxima2(mapVariables, CLAVE_VAR_W, UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1()), umic, varBtcUmic2,
				IObtenerConfiguracion.OrdenAsegurado.ASEG1);
		
//		varValoresQx = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT_Q, umic, varBtcUmic2, 
//				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriEdad, ConstantesSolvencia.CTE_TABMORT_Q);
		mapVariables.put(CLAVE_BTC_UMIC2, varBtcUmic2);
		lstValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, varBtcUmic2,
				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterioEdad, ConstantesSolvencia.CTE_TABMORT_L);
		//Fin variables de apoyo
		
		//Variables de modulo
		
		btcUmic.getTablasConversionAsegurado().get(0).get(0).setTablaFin(TablaFin);
		btcUmic.getTablasConversionAsegurado().get(0).get(0).setTablaInicio(TablaIni);
		
		
		if ((varTCm + varBeta) / ConstantsFunciones.CTE_12 == ConstantsFunciones.CTE_0) {
			varEj = ConstantsFunciones.CTE_OPER_1ENTRE4;
		} else if ((varTCm + varBeta) / ConstantsFunciones.CTE_12 == ConstantsFunciones.CTE_1) {
			varEj = ConstantsFunciones.CTE_OPER_3ENTRE4;
		} else if ((varTCm + varBeta) / ConstantsFunciones.CTE_12 == ConstantsFunciones.CTE_2) {
			varEj = ConstantsFunciones.CTE_OPER_3ENTRE4;
		} else {
			varEj = ConstantsFunciones.CTE_OPER_1;
		}
		
		j = varX.add(BigDecimal.valueOf((varTCm+varBeta)/12));
		j1 = (varTCm+varBeta)/12;
		
		factorJ_1 = new BigDecimal(-0.00008399).multiply((j.subtract(BigDecimal.valueOf(54))).pow(3));
		factorJ_2 = new BigDecimal(0.01381434).multiply((j.subtract(BigDecimal.valueOf(54))).pow(2));
		factorJ_3 = new BigDecimal(0.72009634).multiply((j.subtract(BigDecimal.valueOf(54))));
		factorJ = factorJ_1.add(factorJ_2).subtract(factorJ_3).add(BigDecimal.valueOf(12.93342397));
		
		factorJ1_1 = new BigDecimal(-0.00008399).multiply((j.subtract(BigDecimal.valueOf(54))).add(BigDecimal.ONE).pow(3));
		factorJ1_2 = new BigDecimal(0.01381434).multiply((j.subtract(BigDecimal.valueOf(54))).add(BigDecimal.ONE).pow(2));
		factorJ1_3 = new BigDecimal(0.72009634).multiply((j.subtract(BigDecimal.valueOf(54))).add(BigDecimal.ONE));
		factorJ1 = factorJ1_1.add(factorJ1_2).subtract(factorJ1_3).add(BigDecimal.valueOf(12.93342397));
		
		
		if (j.intValue() >= lstValoresTabMort.size() || lstValoresTabMort.get(j.intValue()) == BigDecimal.ZERO || lstValoresTabMort.get(j.intValue()) == null){
			aux_factorJ = BigDecimal.ZERO;
		} else if (j.intValue() + 1 >= lstValoresTabMort.size()) {
			aux_factorJ = factorJ.multiply(lstValoresTabMort.get(j.intValue())).divide(lstValoresTabMort.get(j.intValue()), ConstantsFunciones.MATH_CONTEXT); 
		} else {
			if (lstValoresTabMort.get(j.intValue()) == null || lstValoresTabMort.get(j.intValue()) == BigDecimal.ZERO){
				aux_factorJ = BigDecimal.ZERO;
			} else {
				aux_factorJ = factorJ.multiply(lstValoresTabMort.get(j.intValue()).subtract(lstValoresTabMort.get(j.intValue()+1))).divide(lstValoresTabMort.get(j.intValue()), ConstantsFunciones.MATH_CONTEXT); 
			}
		}
		if (j.intValue()+1 < lstValoresTabMort.size() && lstValoresTabMort.get(j.intValue()+1) != BigDecimal.ZERO) {
			if (j.intValue() + 2 >= lstValoresTabMort.size()){
				aux_factorJ1 = factorJ1.multiply(lstValoresTabMort.get(j.intValue()+1)).divide(lstValoresTabMort.get(j.intValue()+1), ConstantsFunciones.MATH_CONTEXT); 
			} else {
				aux_factorJ1 = factorJ1.multiply(lstValoresTabMort.get(j.intValue()+1).subtract(lstValoresTabMort.get(j.intValue()+2))).divide(lstValoresTabMort.get(j.intValue()+1), ConstantsFunciones.MATH_CONTEXT); 
			}
		} else {
			aux_factorJ1 = BigDecimal.ZERO;
		}
		
		qz1 = (aux_factorJ.add(aux_factorJ1)).divide(new BigDecimal (2), ConstantsFunciones.MATH_CONTEXT);
		
		if (qz1.compareTo(BigDecimal.ONE) > 0) {
			qz1 = BigDecimal.ONE;
		}
		
		varP1 = Util.pow(BigDecimal.ONE.subtract(qz1), new BigDecimal(1).divide(new BigDecimal(12), ConstantsFunciones.MATH_CONTEXT));
		varV1 = Util.pow(BigDecimal.ONE.add(varI1.multiply(BigDecimal.valueOf(0.01))), new BigDecimal(-1).divide(new BigDecimal(12), ConstantsFunciones.MATH_CONTEXT));;
		varNPA = "0";
		for (int k = 0; k <= 12; k++) {
			
			if (k == 0) {
				varRentaA1 = varPA.multiply(varVA).multiply(new BigDecimal (varNPA));
			} else {
				varRentaA1 = varRentaA1.add(varPA.multiply(varVA));
			}
			varPA = varPA.multiply(varP1);
			varVA = varVA.multiply(varV1);
		}
			
		BigDecimal t1_1 = varRentaA1.divide(BigDecimal.valueOf(12), ConstantsFunciones.MATH_CONTEXT);
		BigDecimal t1_2 = BigDecimal.ONE.subtract(qz1).multiply(Util.pow(BigDecimal.ONE.add(varI1.divide(BigDecimal.valueOf(100))), new BigDecimal(-3).divide(new BigDecimal(2), ConstantsFunciones.MATH_CONTEXT)));
		t3 = varEj.multiply(BigDecimal.ONE.add(varGIC)).multiply(varRenta).multiply(BigDecimal.valueOf(varM));
		
		for (int h=1; h <= varW - (j.intValue()+1); h++) {
			if (x+j1+h+3/2+1 >= lstValoresTabMort.size()) {
				t2_1 =  BigDecimal.ZERO;
			} else {
				LxEntero = lstValoresTabMort.get(x+j1+h+3/2);
				LxEnt1 = x+j1+h+3/2;
				LxEntero1 = (new BigDecimal(x).add(new BigDecimal (j1)).add(new BigDecimal(h)).add(new BigDecimal(3).divide(new BigDecimal(2), ConstantsFunciones.MATH_CONTEXT))).subtract(new BigDecimal(LxEnt1));
				LxEntero2 = lstValoresTabMort.get(x+j1+h+3/2+1).subtract(lstValoresTabMort.get(x+j1+h+3/2));
				t2_1 = LxEntero.add(LxEntero1.multiply(LxEntero2));
			}
			if (x+j1+3/2+1 > lstValoresTabMort.size()) {
				t2_2 =  BigDecimal.ZERO;
			} else {
				LxEntero = lstValoresTabMort.get(x+j1+3/2);
				LxEnt1 = x+j1+3/2;
				LxEntero1 = (new BigDecimal(x).add(new BigDecimal (j1)).add(new BigDecimal(3).divide(new BigDecimal(2)), ConstantsFunciones.MATH_CONTEXT)).subtract(new BigDecimal(LxEnt1));
				LxEntero2 = lstValoresTabMort.get(x+j1+3/2+1).subtract(lstValoresTabMort.get(x+j1+3/2));
				t2_2 = LxEntero.add(LxEntero1.multiply(LxEntero2));
			}
			t2_3 =  Util.pow(BigDecimal.ONE.add(varI1.divide(new BigDecimal(100), ConstantsFunciones.MATH_CONTEXT)),-h);
			if (t2_2.compareTo(BigDecimal.ZERO) != 0) {
				t2 = t2.add(t2_1.multiply(t2_3).divide(t2_2,ConstantsFunciones.MATH_CONTEXT));
			}
		}
		
		t1 = t1_2.multiply(t2.add(new BigDecimal(11).divide(new BigDecimal(24), ConstantsFunciones.MATH_CONTEXT)));
		t2 = t1.add(t1_1);
		
		cspdep = t2.multiply(t3);
		
		if (ModuloCSPDEP.LOG.isTraceEnabled()) {
			ModuloCSPDEP.LOG.trace("Fin función << ModuloCSPDEP >> de la clase ModuloCSPDEP, para la iteracion = {}, con resultado cspdep = {}", iteracion, cspdep);
		}
		
		return cspdep;
	}
	
	private Integer obtenerVarM(final Rentas rentasUmic) {
		//Variables locales
		int varM = 0;
		//Fin variables locales
		
		if (ModuloCSPDEP.LOG.isTraceEnabled()) {
			ModuloCSPDEP.LOG.trace("Inicio de la función << obtenerVarM >> de la clase ModuloCSPDEP");
		}
		
		final Integer forpagrent = rentasUmic.getForpagrent();
		
		if (null != forpagrent && !ConstantsFunciones.CTE_0.equals(forpagrent)) {
			varM = rentasUmic.getForpagrent();
		} else {
			if (rentasUmic.getCpagrenta() == null){
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AQ,  new String[]{"null"});
			}
			varM = ConstantsFunciones.FORPAGRENT.get(Integer.valueOf(rentasUmic.getCpagrenta()));
			
		}
		
		if (ModuloCSPDEP.LOG.isTraceEnabled()) {
			ModuloCSPDEP.LOG.trace("Fin de la función << obtenerVarM >> de la clase ModuloCSPDEP, con resultado varM = {}", varM);
		}
		
		return Integer.valueOf(varM);
	}
	
}
