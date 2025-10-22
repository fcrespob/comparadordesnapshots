package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.Tab923;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.modulos.impl.ValidacionesComunesModulos;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloCSPGDEP implements Modulo {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSPGDEP.class);
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSPGDEP;
	
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_NP = ConstantsModulos.CTE_VAR_NP.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_GF = ConstantsModulos.CTE_VAR_GF.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_GIPC = ConstantsModulos.CTE_VAR_GIPC.concat(CLAVE_MODULO);
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	private static final String CLAVE_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT;
	private static final String CLAVE_VAR_VAL_TAB_MORT = CLAVE_VAL_TAB_MORT.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_W = ConstantsModulos.CTE_VAR_WX.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC2 = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TAB923 = ConstantsModulos.CTE_VAR_TAB923.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_X = ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_INV = ConstantsModulos.CTE_VAR_VAL_TAB_INV.concat(CLAVE_MODULO);
	
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
		BigDecimal cspgdep = BigDecimal.ZERO;
		//Fin variables locales
		
		try {
			if (ModuloCSPGDEP.LOG.isTraceEnabled()) {
				ModuloCSPGDEP.LOG.trace("Inicio de execute en clase ModuloCSPDEP");
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
			cspgdep = moduloCSPDEP(proyUmic,iteracion,fcalc, umic, btcUmic, codSubproceso, bloqueCorriente,mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloCSPGDEP.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSPGDEP.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSPGDEP.LOG.isTraceEnabled()) {
			ModuloCSPGDEP.LOG.trace("Fin de execute en clase ModuloCSPDEP");
		}
		
		return cspgdep;
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
		BigDecimal cspgdep = BigDecimal.ZERO;
		String varCriterFec;
		String varCriEdad;

		Timestamp varFechaEfecto = null;
		Integer varX = 0;
		
		BigDecimal varNP = BigDecimal.ZERO;
		BigDecimal varGipc = BigDecimal.ZERO;
		BigDecimal varGF = BigDecimal.ZERO;
		BigDecimal varPNA = BigDecimal.ZERO;
		BigDecimal tit1 = BigDecimal.ZERO;
		List<BigDecimal> lstValoresTabMort = null;
		BigDecimal aux_cspgdep1 = BigDecimal.ZERO;
		BigDecimal aux_cspgdep2 = BigDecimal.ZERO;
		BigDecimal aux_cspgdep3 = BigDecimal.ZERO;
		Integer varW;
		DetalleBaseTecnica  varBtcUmic2;
		List<Tab923> varTab923;
		BigDecimal rnodep = BigDecimal.ZERO;
		BigDecimal rnodepc = BigDecimal.ZERO;
		BigDecimal aux_rnodep = BigDecimal.ZERO;
		BigDecimal aux_rnodepc = BigDecimal.ZERO;
		BigDecimal aux_cspgdep = BigDecimal.ZERO;
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		List<BigDecimal>     varValoresTabInv;
		List<BigDecimal>     varValoresLix;
		BigDecimal sobreriesgo = umic.getBti().getPriesgo();
		
		//Fin variables locales
		
		if (ModuloCSPGDEP.LOG.isTraceEnabled()) {
			ModuloCSPGDEP.LOG.trace("Inicio función << moduloCSPDEP >> para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		// Definición de variables auxiliares para agilizar las operaciones (se llaman varias veces).
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		// Fin de la definición de las variables auxiliares.		
		
		
		//Variables de apoyo
		varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
		varCriEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		//Validacion variables de apoyo
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFec);
		}
		
		if(bloqueCorriente.getFechaDevengo() == null){
			return cspgdep;
		}
		
		tit1 = umic.getBti().getPintertecnI1();
		
		varGipc = UtilModulos.getVarGicDiv100(mapVariables, CLAVE_VAR_GIPC, btcUmic.getGtorosspPrima());
		varGF = UtilModulos.getVarGF(mapVariables, CLAVE_VAR_GF, umic.getBti().getPgastgesin3I());
		varPNA = umic.getPrimas().getIprimatarada();
		
		varNP = (BigDecimal) mapVariables.get(CLAVE_VAR_NP);
		
		varW = UtilModulos.getVarEdadMaxima2(mapVariables, CLAVE_VAR_W, UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1()), umic, btcUmic,
				IObtenerConfiguracion.OrdenAsegurado.ASEG1);
		lstValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic,
				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriEdad, ConstantesSolvencia.CTE_TABMORT_L);
		
		varEdifer = umic.getDatosGenerales().getEdifer();
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriEdad, umic.getRentas().getFecIni(), varEdifer).intValue();
		
		varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC2);
		if(null == varBtcUmic2){
			varBtcUmic2 = new DetalleBaseTecnica();
			
			try {
				
				PropertyUtils.copyProperties(varBtcUmic2, btcUmic);
				
			} catch (Exception e) {
				ModuloCSPGDEP.LOG.error(e.getMessage());
			}
			
			varBtcUmic2.setTablacalc1aseg1(btcUmic.getTablacalc3aseg1());
			mapVariables.put(CLAVE_BTC_UMIC2, varBtcUmic2);
		}
		varValoresTabInv = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_INV, umic, varBtcUmic2,
				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriEdad, ConstantesSolvencia.CTE_TABMORT_I);
		varValoresLix = FuncionesAuxiliares.obtenerLix(varValoresTabInv, sobreriesgo);

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
		
		varTab923 = UtilModulos.getTab923(mapVariables, CLAVE_VAR_TAB923, umic.getDatosGenerales().getKmodalidad(), fcalc, UtilFechas.decreMeses(new Timestamp(new GregorianCalendar(9999,12,31,ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis()), 1));
		
		rnodepc = (BigDecimal) mapVariables.get("CLAVE_RNODEPC");
		if (null == rnodepc) {
			rnodepc = FuncionesAuxiliares.RNoDepC(0, 0, varX, lstValoresTabMort, varValoresLix, varTab923, varW, tit1).setScale(ConstantsFunciones.CTE_8, RoundingMode.HALF_UP);
			mapVariables.put("CLAVE_RNODEPC", rnodepc);
		}
		rnodep = (BigDecimal) mapVariables.get("CLAVE_RNODEP");
		if (null == rnodep) {
			rnodep = FuncionesAuxiliares.RNoDep(0, 0, varX, lstValoresTabMort, varValoresLix, varW, tit1).setScale(ConstantsFunciones.CTE_8, RoundingMode.HALF_UP);
			mapVariables.put("CLAVE_RNODEP", rnodep);
		}
		aux_rnodep = rnodep.setScale(ConstantsFunciones.CTE_8, RoundingMode.HALF_UP);
		aux_rnodepc = rnodepc.setScale(ConstantsFunciones.CTE_8, RoundingMode.HALF_UP);
		aux_cspgdep = (BigDecimal) mapVariables.get("CLAVE_CSPGDEP");
		if (null == aux_cspgdep) {
			if (!varNP.equals(BigDecimal.ZERO)) {
				aux_cspgdep1 = varPNA.divide(BigDecimal.valueOf(12), ConstantsFunciones.MATH_CONTEXT);
				aux_cspgdep2 = aux_cspgdep1.multiply(varGipc).multiply(aux_rnodepc);
				aux_cspgdep3 = aux_cspgdep2.divide(aux_rnodep, ConstantsFunciones.MATH_CONTEXT);
				cspgdep = aux_cspgdep3.add(varGF.divide(aux_rnodep, ConstantsFunciones.MATH_CONTEXT));
			} else {
				cspgdep = (varPNA.multiply(varGipc).add(varGF)).divide(rnodep, ConstantsFunciones.MATH_CONTEXT);
			}
			mapVariables.put("CLAVE_CSPGDEP", cspgdep);
		}else {
			cspgdep = aux_cspgdep;
		}
		
		if (ModuloCSPGDEP.LOG.isTraceEnabled()) {
			ModuloCSPGDEP.LOG.trace("Fin función << ModuloCSPGDEP >> de la clase ModuloCSPGDEP, para la iteracion = {}, con resultado cspgdep = {}", iteracion, cspgdep);
		}
		
		return cspgdep;
	}
	
}
