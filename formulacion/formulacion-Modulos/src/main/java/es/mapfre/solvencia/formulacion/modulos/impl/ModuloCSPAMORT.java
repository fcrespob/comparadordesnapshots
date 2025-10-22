package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.CuadroUmic;
import es.mapfre.solvencia.dominio.maestro.CuadrosAmortizacion;
import es.mapfre.solvencia.dominio.maestro.Umic;
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
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloCSPAMORT implements Modulo {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSPAMORT.class);
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSPAMORT;
	
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CUADRO_AMORT = ConstantsModulos.CTE_CUADRO_AMORT;
	private static final String CLAVE_VAR_CUADRO_AMORT = CLAVE_CUADRO_AMORT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_FEC_INI_AMORT = ConstantsModulos.CTE_FEC_INI_AMORT;
	private static final String CLAVE_VAR_FEC_INI_AMORT = CLAVE_FEC_INI_AMORT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_CJP = ConstantsModulos.CTE_VAR_CJP.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CUADRO_AMORT_UMIC = ConstantsModulos.CTE_CUADRO_AMORT_UMIC;
	private static final String CLAVE_VAR_CUADRO_AMORT_UMIC = CLAVE_CUADRO_AMORT_UMIC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_NC_MESES = ConstantsModulos.CTE_VAR_NC_MESES;
	private static final String CLAVE_VAR_NC_MESES = CLAVE_NC_MESES.concat(CLAVE_MODULO);

	private static final String CLAVE_NA = ConstantsModulos.CTE_VAR_NA;
	private static final String CLAVE_VAR_NA = CLAVE_NA.concat(CLAVE_MODULO);
private static final String CLAVE_DELTA = ConstantsModulos.CTE_VAR_DELTA;
	private static final String CLAVE_VAR_DELTA = CLAVE_DELTA.concat(CLAVE_MODULO);																				
	
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
		BigDecimal cspamort = BigDecimal.ZERO;
		//Fin variables locales
		
		try {
			if (ModuloCSPAMORT.LOG.isTraceEnabled()) {
				ModuloCSPAMORT.LOG.trace("Inicio de execute en clase ModuloCSPAMORT");
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
			cspamort = moduloCSPAMORT(proyUmic,iteracion,fcalc, umic, btcUmic, codSubproceso, bloqueCorriente,mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloCSPAMORT.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSPAMORT.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSPAMORT.LOG.isTraceEnabled()) {
			ModuloCSPAMORT.LOG.trace("Fin de execute en clase ModuloCSPAMORT");
		}
		
		return cspamort;
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
	
	
	@SuppressWarnings("unchecked")
	private BigDecimal moduloCSPAMORT(List<DetalleCorriente> proyUmic, int iteracion, Timestamp fcalc, Umic umic,
			DetalleBaseTecnica btcUmic, String codSubproceso, BloqueCorriente bloqueCorriente,
			Map<String, Object> mapVariables) {
		
		//Variables locales
		BigDecimal cspamort = BigDecimal.ZERO;
		String varCriterFec;
		CuadrosAmortizacion varCuadroAmort;
		BigDecimal varIF = BigDecimal.ZERO;
		Integer varncAnos = Integer.valueOf(0);
		Integer varncMeses = Integer.valueOf(0);
		Timestamp varFechaEfecto;
		Timestamp fecIniAmort;
		Integer varMes1 = Integer.valueOf(ConstantsFunciones.CTE_0);
		Integer varMes2 = Integer.valueOf(ConstantsFunciones.CTE_0);
		BigDecimal varC0p = BigDecimal.ZERO;
		BigDecimal varG = BigDecimal.ZERO;
		String varCperamont;
		Integer varCperaseg = Integer.valueOf(ConstantsFunciones.CTE_0);
		Integer varna = Integer.valueOf(ConstantsFunciones.CTE_0);
		Integer varnpa = Integer.valueOf(ConstantsFunciones.CTE_0);
		Integer varpc = Integer.valueOf(ConstantsFunciones.CTE_0);
		Integer varTCm = Integer.valueOf(ConstantsFunciones.CTE_0);
		BigDecimal varDelta;
		BigDecimal varCuotaj;
		BigDecimal varSicuota = BigDecimal.ZERO; 
		BigDecimal varCjp = BigDecimal.ZERO;
		BigDecimal calculoAux;
		ArrayList<CuadroUmic> cuadroAmortUmic;
		BigDecimal anteriorCpj;
		BigDecimal calculoSicuotaAux;
		Integer indice;
		BigDecimal calculoAuxCspamort;
		Integer ndursegano;
		//Fin variables locales
		
		if (ModuloCSPAMORT.LOG.isTraceEnabled()) {
			ModuloCSPAMORT.LOG.trace("Inicio función << moduloCSPAMORT >> para la iteracion = {}", iteracion);
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
		
		//Validacion variables de apoyo
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFec);
		}
		//Fin variables de apoyo
		
		//Variables de modulo
		varCuadroAmort = UtilModulos.getVarCuadroAmortizacion(mapVariables, CLAVE_VAR_CUADRO_AMORT, umic);
		
		//Crear nuevo codigo de error que lanzar
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)){
			ValidacionesComunesModulos.validarCuadrosAmortizacion(varCuadroAmort);
		}
		
		varFechaEfecto = UtilModulos.getVarFechaEfecto(mapVariables, CLAVE_FEC_EFEC, umic);
		
		varIF = varCuadroAmort.getPintermor();
		
		varncAnos = varCuadroAmort.getNpercar();
		
		//varncMeses = UtilModulos.getVarNcMeses(mapVariables, CLAVE_VAR_NC_MESES, varncAnos);
		varncMeses = varncAnos * 12;
		
		fecIniAmort = UtilModulos.incrementarMeses(mapVariables, CLAVE_VAR_FEC_INI_AMORT, varFechaEfecto, null, varncMeses, false);
		
		varC0p = umic.getCapitales().getIcapini();
		
		varG = varCuadroAmort.getPorcrec();
		
		varCperamont = varCuadroAmort.getCperamont();
		
		if(varCperamont.equals(ConstantsFunciones.CTE_1_STRING)){
			varnpa = 1;
			varpc = 1;
			
		}else if(varCperamont.equals(ConstantsFunciones.CTE_2_STRING)){
			varnpa = 2;
			varpc = 2;
			
		}else if(varCperamont.equals(ConstantsFunciones.CTE_3_STRING)){
			varnpa = 4;
			varpc = 4;
			
		}else if(varCperamont.equals(ConstantsFunciones.CTE_4_STRING)){
			varnpa = 12;
			varpc = 12;
			
		}else if(varCperamont.equals(ConstantsFunciones.CTE_5_STRING)){
			varnpa = 6;
			varpc = 6;		
		}
		
		varCperaseg = varCuadroAmort.getCperaseg();
		
		varna = UtilModulos.getVarNa(mapVariables, CLAVE_VAR_NA, varCperaseg, umic.getDuraciones().getNdursegano(), varncMeses);		
		
		varna = varna + umic.getDuraciones().getNdursegmes();		
		
	
		varDelta = UtilModulos.getVarDelta(mapVariables, CLAVE_VAR_DELTA,varna,varnpa,varG, varMes1, varMes2, varIF);
		
		//Fin variables modulo
		
		//Calculo cuadro de amortizacion
		cuadroAmortUmic = UtilModulos.getCuadroAmortizacion(mapVariables, CLAVE_CUADRO_AMORT_UMIC, umic, varnpa, varna, varpc, varCuadroAmort, varDelta, varncMeses, varFechaEfecto, varMes1, varMes2);
		//Fin calculo cuadro de amortizacion
		
		//Funcional
		if(ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)){
			
			varTCm = UtilModulos.getVarTCm(mapVariables, CLAVE_VAR_TCM, ConstantsModulos.CTE_FIRST_ITER, varFechaEfecto, bloqueCorriente.getFechaDevengo());
		
		}else{
			
			varTCm = (Integer) mapVariables.get(CLAVE_VAR_TCM + (iteracion - 1));
			varTCm = varTCm + ConstantsFunciones.CTE_1;
			mapVariables.put(CLAVE_VAR_TCM + iteracion, varTCm);
			
		}
		
		if(bloqueCorriente.getFechaDevengo().equals(null)){
			
			cspamort = BigDecimal.ZERO;
			
		}else{
			
			if(varTCm < cuadroAmortUmic.size()){
				
				varCjp = cuadroAmortUmic.get(varTCm).getVarCpj();
				calculoAuxCspamort = BigDecimal.ONE.add((varIF.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).divide(ConstantsFunciones.CTE_OPER_12, ConstantsFunciones.MATH_CONTEXT)); 
				cspamort = varCjp.multiply(calculoAuxCspamort);
			
			}else{
			
				cspamort = BigDecimal.ZERO;			
			}

		}	
		
		if (ModuloCSPAMORT.LOG.isTraceEnabled()) {
			ModuloCSPAMORT.LOG.trace("Fin función << ModuloCSPAMORT >> de la clase ModuloCSPAMORT, para la iteracion = {}, con resultado cspamort = {}", iteracion, cspamort);
		}
		
		return cspamort;
	}
	
}
