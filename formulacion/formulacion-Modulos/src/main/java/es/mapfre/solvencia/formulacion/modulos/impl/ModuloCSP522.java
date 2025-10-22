package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.Tab923;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloCSP522 implements Modulo{


	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP522.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP522;

	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC;
	private static final String CLAVE_VAR_TTM = ConstantsModulos.CTE_VAR_TTM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TAB923 = ConstantsModulos.CTE_VAR_TAB923.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_NP = ConstantsModulos.CTE_VAR_NP.concat(CLAVE_MODULO);
	// Fin de las variables estáticas usadas para agilizar operaciones.

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la
	 * llamada a la función que realiza los calculos del modulo.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		// Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		// Fin variables locales

		try {
			if (ModuloCSP522.LOG.isTraceEnabled()) {
				ModuloCSP522.LOG.trace("Inicio de execute en clase CSP522");
			}

			// Recuperamos los datos que le pasaremos a la función moduloPRI002
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de datos.

			// Invocamos a la función moduloPRI002
			resultado = moduloCSP522(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);

		} catch (Solvencia2Excepcion e) {
			ModuloCSP522.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP522.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloCSP522.LOG.isTraceEnabled()) {
			ModuloCSP522.LOG.trace("Fin de execute en clase CSP_522");
		}

		return resultado;
	}

	private BigDecimal moduloCSP522(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente,
			final Integer iteracion, final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic,
			final Map<String, Object> mapVariables) {
		// Variables locales
		String varCriterioFecha;
		Integer varTCm,varTtm;
		BigDecimal csp522 = BigDecimal.ZERO;
		Integer varBeta;
		Timestamp varFechaEfecto;
		int varPP;
		BigDecimal varNP = BigDecimal.ZERO;
		BigDecimal varPNAt;
		BigDecimal varGepc;
		BigDecimal varRPF;
		List<Tab923> varTab923;
		String L1, L2, L3 ,L4 ,L5, L6;
		String PRP1, PRP2, PRP3, PRP4, PRP5, PRP6;
		BigDecimal varP = BigDecimal.ZERO;
		BigDecimal auxL1, auxL2, auxL3, auxL4, auxL5, auxL6;
		Integer auxEntL2, auxEntL3, auxEntL4, auxEntL5, auxEntL6;
		Integer auxMaxL2, auxMaxL3, auxMaxL4, auxMaxL5, auxMaxL6;
		BigDecimal varPL1, varPL2, varPL3, varPL4, varPL5, varPL6;
		// Fin variables locales

		if (ModuloCSP522.LOG.isTraceEnabled()) {
			ModuloCSP522.LOG.trace(
					"Inicio función << ModuloCSP522 >> de la clase ModuloCSP522, para la iteracion = {}", iteracion);
		}
		
		if(bloqueCorriente.getFechaDevengo() == null){	
			return csp522;
		}

		// validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		//varCriterioEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		// Obtención y validación del criterio de fecha.
		varCriterioFecha = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC,
				umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(),
				umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);

		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la
			// forma en que se gestiona el paso de umic).
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterioFecha);
			// Fin de la obtención y validación del criterio de fecha.
		}
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(),
				umic.getFechas(), umic.getCapitales().getIsaldo());		
		// Meses completes transcurridos desde efecto póliza a momento cálculo
		varTCm = UtilModulos.getVarTCmIni(mapVariables, CLAVE_VAR_TCM, ConstantsModulos.CTE_FIRST_ITER, varFechaEfecto,	fcalc);
		varTtm = UtilModulos.getVarTCmIni(mapVariables, CLAVE_VAR_TTM, 0, varFechaEfecto, umic.getFechas().getFecefecfin());
		varBeta = iteracion - 1;
		varPNAt = umic.getPrimas().getIprimatarada();
		varGepc = umic.getBti().getPgastgesex1I().divide(BigDecimal.valueOf(100), ConstantsFunciones.MATH_CONTEXT);
		varRPF = umic.getPrimas().getPrecargfrac();
//		varValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic,
//				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterioEdad, ConstantesSolvencia.CTE_TABMORT_L);
		//factorAnual = BigDecimal.ONE.add(varPRP.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		
		varTab923 = UtilModulos.getTab923(mapVariables, CLAVE_VAR_TAB923, umic.getDatosGenerales().getKmodalidad(), fcalc, UtilFechas.decreMeses(new Timestamp(new GregorianCalendar(9999,12,31,ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis()), 1));
		
		if(null == varTab923 || varTab923.isEmpty() || null == varTab923.get(0)){
			
			L1 = "0";
			L2 = "0";
			L3 = "0";
			L4 = "0";
			L5 = "0";
			L6 = "0";
			PRP1 = "0";			
			PRP2 = "0";			
			PRP3 = "0";					
			PRP4 = "0";			
			PRP5 = "0";	
			PRP6 = "0";
			PRP6 = "0";

		}else{
			
			if(null == varTab923.get(0).getl1()){
				L1 = "0";
			}else{
				L1 = varTab923.get(0).getl1();
			}
			
			if(null == varTab923.get(0).getl2()){
				L2 = "0";
			}else{
				L2 = varTab923.get(0).getl2();
			}
			
			if(null == varTab923.get(0).getl3()){
				L3 = "0";
			}else{
				L3 = varTab923.get(0).getl3();
			}
			
			if(null == varTab923.get(0).getl4()){
				L4 = "0";
			}else{
				L4 = varTab923.get(0).getl4();
			}
			
			if(null == varTab923.get(0).getl5()){
				L5 = "0";
			}else{
				L5 = varTab923.get(0).getl5();
			}
			
			if(null == varTab923.get(0).getl6()){
				L6 = "0";
			}else{
				L6 = varTab923.get(0).getl6();
			}
			
			if(null == varTab923.get(0).getPRP1()){
				PRP1 = "0";
			}else{
				PRP1 = varTab923.get(0).getPRP1();
			}
			
			if(null == varTab923.get(0).getPRP2()){
				PRP2 = "0";
			}else{
				PRP2 = varTab923.get(0).getPRP2();
			}
			
			if(null == varTab923.get(0).getPRP3()){
				PRP3 = "0";
			}else{
				PRP3 = varTab923.get(0).getPRP3();
			}
			
			if(null == varTab923.get(0).getPRP4()){
				PRP4 = "0";
			}else{
				PRP4 = varTab923.get(0).getPRP4();
			}
			
			if(null == varTab923.get(0).getPRP5()){
				PRP5 = "0";
			}else{
				PRP5 = varTab923.get(0).getPRP5();
			}
		
			if(null == varTab923.get(0).getPRP6()){
				PRP6 = "0";
			}else{
				PRP6 = varTab923.get(0).getPRP6();
			}
			
			PRP6 = varTab923.get(0).getPRP6();
			if (PRP6 == null) {
				PRP6 = "0";
			}
		}
		if (L1.equals("99")) {
			L2 = "99";
			L3 = "99";
			L4 = "99";
			L5 = "99";
			L6 = "99";
		}else if (L2.equals("99")) {
			L3 = "99";
			L4 = "99";
			L5 = "99";
			L6 = "99";
		} else if (L3.equals("99")) {
			L4 = "99";
			L5 = "99";
			L6 = "99";
		} else if (L4.equals("99")) {
			L5 = "99";
			L6 = "99";
		} else if (L5.equals("99")) {
			L6 = "99";
		}
		/**
		 * Calcular el nº de pagos por anualidad evaluando el campo umic.primas.cformpago de la siguiente manera:
			Si umic.primas.cformpago = 4
			    varNP = 12
			Si umic.primas.cformpago = 3
			    varNP =4
			Si umic.primas.cformpago =1 o 2
			    varNP = umic.primas.cformpago
			varNP --> dejo la variable en memoria, disponible para el subproceso de la umic.

		 */
		
		varNP = (BigDecimal) mapVariables.get(CLAVE_VAR_NP);
		
		if (ConstantsFunciones.CTE_APOR_REDUCIDA.equals(umic.getDatosGenerales().getCsitupol())){
			varNP = BigDecimal.ZERO;
			mapVariables.put(CLAVE_VAR_NP, varNP);
		}
		if (varNP == null){
			if (umic.getPrimas().getCformpago().equals(ConstantsFunciones.CTE_FORMPAGO_4)){
				varNP = BigDecimal.valueOf(ConstantsFunciones.CTE_12);
			} else if (umic.getPrimas().getCformpago().equals(ConstantsFunciones.CTE_FORMPAGO_3)){
				varNP = BigDecimal.valueOf(ConstantsFunciones.CTE_4);;
			} else if (umic.getPrimas().getCformpago().equals(ConstantsFunciones.CTE_FORMPAGO_1) || umic.getPrimas().getCformpago().equals(ConstantsFunciones.CTE_FORMPAGO_2)){
				varNP = new BigDecimal(umic.getPrimas().getCformpago());
			}
			
			mapVariables.put(CLAVE_VAR_NP, varNP);
		}
		
		varPP = (varTCm + varBeta) % (ConstantsFunciones.CTE_12 / varNP.intValue());

		if(varPP==0) {
			if (UtilModulos.StringToBigDecimal(L1).intValue() > ((varTCm + varBeta) / ConstantsFunciones.CTE_12)){
				auxL1 = BigDecimal.valueOf((varTCm + varBeta) / ConstantsFunciones.CTE_12);
			} else {
				auxL1 = UtilModulos.StringToBigDecimal(L1);
			}
			auxMaxL2 = ((varTCm + varBeta) / ConstantsFunciones.CTE_12) - UtilModulos.StringToBigDecimal(L1).intValue() + 1;
			if (auxMaxL2 > 0) {
				auxEntL2 = auxMaxL2;
			} else {
				auxEntL2 = 0;
			}
			if (UtilModulos.StringToBigDecimal(L2).intValue() > auxEntL2){
				auxL2 = BigDecimal.valueOf(auxEntL2);
			} else {
				auxL2 = UtilModulos.StringToBigDecimal(L2);
			}
			auxMaxL3 = ((varTCm + varBeta) / ConstantsFunciones.CTE_12) - UtilModulos.StringToBigDecimal(L1).intValue() - UtilModulos.StringToBigDecimal(L2).intValue() + 1;
			if (auxMaxL3 > 0) {
				auxEntL3 = auxMaxL3;
			} else {
				auxEntL3 = 0;
			}
			if (UtilModulos.StringToBigDecimal(L3).intValue() > auxEntL3){
				auxL3 = BigDecimal.valueOf(auxEntL3);;
			} else {
				auxL3 = UtilModulos.StringToBigDecimal(L3);
			}
			auxMaxL4 = ((varTCm + varBeta) / ConstantsFunciones.CTE_12) - UtilModulos.StringToBigDecimal(L1).intValue() - UtilModulos.StringToBigDecimal(L2).intValue() - UtilModulos.StringToBigDecimal(L3).intValue() + 1;
			if (auxMaxL4 > 0) {
				auxEntL4 = auxMaxL4;
			} else {
				auxEntL4 = 0;
			}
			if (UtilModulos.StringToBigDecimal(L4).intValue() > ((varTCm + varBeta) / ConstantsFunciones.CTE_12)){
				auxL4 = BigDecimal.valueOf(auxEntL4);
			} else {
				auxL4 = UtilModulos.StringToBigDecimal(L4);
			}
			auxMaxL5 = ((varTCm + varBeta) / ConstantsFunciones.CTE_12) - UtilModulos.StringToBigDecimal(L1).intValue() - UtilModulos.StringToBigDecimal(L2).intValue() - UtilModulos.StringToBigDecimal(L3).intValue() - UtilModulos.StringToBigDecimal(L4).intValue() + 1;
			if (auxMaxL5 > 0) {
				auxEntL5 = auxMaxL5;
			} else {
				auxEntL5 = 0;
			}
			if (UtilModulos.StringToBigDecimal(L5).intValue() > auxEntL5){
				auxL5 = BigDecimal.valueOf(auxEntL5);
			} else {
				auxL5 = UtilModulos.StringToBigDecimal(L5);
			}
			auxMaxL6 = ((varTCm + varBeta) / ConstantsFunciones.CTE_12) - UtilModulos.StringToBigDecimal(L1).intValue() - UtilModulos.StringToBigDecimal(L2).intValue() - UtilModulos.StringToBigDecimal(L3).intValue() - UtilModulos.StringToBigDecimal(L4).intValue() - UtilModulos.StringToBigDecimal(L5).intValue() + 1;
			if (auxMaxL6 > 0) {
				auxEntL6 = auxMaxL6;
			} else {
				auxEntL6 = 0;
			}
			if (UtilModulos.StringToBigDecimal(L6).intValue() > auxEntL6){
				auxL6 = BigDecimal.valueOf(auxEntL6);
			} else {
				auxL6 = UtilModulos.StringToBigDecimal(L6);
			}
			
			varPL1 = Util.pow((BigDecimal.ONE.add(UtilModulos.StringToBigDecimal(PRP1).divide(BigDecimal.valueOf(10000)), ConstantsFunciones.MATH_CONTEXT)),auxL1);
			varPL2 = Util.pow((BigDecimal.ONE.add(UtilModulos.StringToBigDecimal(PRP2).divide(BigDecimal.valueOf(10000)), ConstantsFunciones.MATH_CONTEXT)),auxL2);
			varPL3 = Util.pow((BigDecimal.ONE.add(UtilModulos.StringToBigDecimal(PRP3).divide(BigDecimal.valueOf(10000)), ConstantsFunciones.MATH_CONTEXT)),auxL3);
			varPL4 = Util.pow((BigDecimal.ONE.add(UtilModulos.StringToBigDecimal(PRP4).divide(BigDecimal.valueOf(10000)), ConstantsFunciones.MATH_CONTEXT)),auxL4);
			varPL5 = Util.pow((BigDecimal.ONE.add(UtilModulos.StringToBigDecimal(PRP5).divide(BigDecimal.valueOf(10000)), ConstantsFunciones.MATH_CONTEXT)),auxL5);
			varPL6 = Util.pow((BigDecimal.ONE.add(UtilModulos.StringToBigDecimal(PRP6).divide(BigDecimal.valueOf(10000)), ConstantsFunciones.MATH_CONTEXT)),auxL6);

			varP = varPL1.multiply(varPL2).multiply(varPL3).multiply(varPL4).multiply(varPL5).multiply(varPL6);
		}else {
			varP=BigDecimal.ZERO;
		}
		if(varBeta+varTCm == varTtm) {
			csp522 = BigDecimal.ZERO;
		}else{
			csp522 = varPNAt.multiply(BigDecimal.ONE.subtract(varGepc)).multiply(BigDecimal.ONE.add((varRPF.divide(ConstantsFunciones.CTE_OPER_100)))).multiply(varP.divide(varNP, ConstantsFunciones.MATH_CONTEXT));
		}

		if (ModuloCSP522.LOG.isTraceEnabled()) {
			ModuloCSP522.LOG.trace(
					"Fin función << moduloCSP522 >> de la clase ModuloCSP522, para la iteracion = {}, con resultado csp_522 = {}",
					iteracion, csp522);
		}
		return csp522;
	}
}
