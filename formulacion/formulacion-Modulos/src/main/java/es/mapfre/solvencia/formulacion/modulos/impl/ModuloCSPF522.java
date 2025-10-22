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
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloCSPF522 implements Modulo{


	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSPF522.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSPF522;

	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC;
	private static final String CLAVE_VAR_TTM = ConstantsModulos.CTE_VAR_TTM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TAB923 = ConstantsModulos.CTE_VAR_TAB923.concat(CLAVE_MODULO);
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
			if (ModuloCSPF522.LOG.isTraceEnabled()) {
				ModuloCSPF522.LOG.trace("Inicio de execute en clase CSPF522");
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
			resultado = moduloCSPF522(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);

		} catch (Solvencia2Excepcion e) {
			ModuloCSPF522.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSPF522.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloCSPF522.LOG.isTraceEnabled()) {
			ModuloCSPF522.LOG.trace("Fin de execute en clase CSPF522");
		}

		return resultado;
	}

	private BigDecimal moduloCSPF522(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente,
			final Integer iteracion, final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic,
			final Map<String, Object> mapVariables) {
		// Variables locales
		String varCriterioFecha;
		Integer varTCm,varTtm;
		BigDecimal cspf522 = BigDecimal.ZERO;
		Integer varBeta;
		Timestamp varFechaEfecto;
		BigDecimal varNp = BigDecimal.ZERO;
		BigDecimal varPNA;
		BigDecimal varPRP = BigDecimal.ONE;
		BigDecimal varEj = BigDecimal.ONE;;
		BigDecimal varSumpg = BigDecimal.ZERO;
		BigDecimal varPrim = BigDecimal.ONE;
		String L1, L2, L3, L4, L5, L6;
		String PRP1, PRP2, PRP3, PRP4, PRP5, PRP6;
		List<Tab923> varTab923;
		
		// Fin variables locales

		if (ModuloCSPF522.LOG.isTraceEnabled()) {
			ModuloCSPF522.LOG.trace(
					"Inicio función << ModuloCSPF522 >> de la clase ModuloCSPF522, para la iteracion = {}", iteracion);
		}
		
		if(bloqueCorriente.getFechaDevengo() == null){	
			return cspf522;
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
		varPNA = umic.getCapitales().getIcapini();
		
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
		
		if (ConstantsFunciones.CTE_4_STRING.equals(umic.getPrimas().getCformpago())) {
			varNp = ConstantsFunciones.CTE_OPER_12;
		} else if (ConstantsFunciones.CTE_3_STRING.equals(umic.getPrimas().getCformpago())) {
			varNp = ConstantsFunciones.CTE_OPER_4;
		} else if (ConstantsFunciones.CTE_1_STRING.equals(umic.getPrimas().getCformpago()) || ConstantsFunciones.CTE_2_STRING.equals(umic.getPrimas().getCformpago())) {
			varNp = new BigDecimal(umic.getPrimas().getCformpago());
		}
		
		
		
		for (int j = 0; j <= ((varTCm+varBeta)/12); j++) {
			varPRP = UtilModulos.StringToBigDecimal(PRP1).divide(BigDecimal.valueOf(100));
			if (j >= UtilModulos.StringToBigDecimal(L1).intValue()) {
				varPRP = UtilModulos.StringToBigDecimal(PRP2);
			}
			if (j > UtilModulos.StringToBigDecimal(L2).intValue()) {
				varPRP = UtilModulos.StringToBigDecimal(PRP3);
			}
			if (j > UtilModulos.StringToBigDecimal(L3).intValue()) {
				varPRP = UtilModulos.StringToBigDecimal(PRP4);
			}
			if (j > UtilModulos.StringToBigDecimal(L4).intValue()) {
				varPRP = UtilModulos.StringToBigDecimal(PRP5);
			}
			if (j > UtilModulos.StringToBigDecimal(L5).intValue()) {
				varPRP = UtilModulos.StringToBigDecimal(PRP6);
			}
			varPrim = varPrim.multiply(BigDecimal.ONE.add(varPRP.divide(BigDecimal.valueOf(10000))));
			varSumpg = varSumpg.add(varPrim);
		}
		
		if(varBeta+varTCm >= varTtm) {
			cspf522 = BigDecimal.ZERO;
		}else if (varNp.compareTo(BigDecimal.ZERO) == 0){
			cspf522 = varPNA.multiply(varEj);
		} else {
			cspf522 = varPNA.multiply(varSumpg).multiply(varEj);
		}

		if (ModuloCSPF522.LOG.isTraceEnabled()) {
			ModuloCSPF522.LOG.trace(
					"Fin función << moduloCSPF522 >> de la clase ModuloCSP522, para la iteracion = {}, con resultado cspf522 = {}",
					iteracion, cspf522);
		}

		return cspf522;
	}

}
