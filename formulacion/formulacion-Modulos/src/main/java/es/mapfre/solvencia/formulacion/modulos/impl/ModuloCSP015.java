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
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloCSP015 implements Modulo {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloPRI003R.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP015;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VA_CRIT_EDA = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_VA_CRIT_EDA.concat(CLAVE_MODULO);	
	
	private static final String CLAVE_E1PSJ = ConstantsModulos.CTE_E1PSJ;
	private static final String CLAVE_VAR_E1PSJ = CLAVE_E1PSJ.concat(CLAVE_MODULO);	
	
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_POC_45 = ConstantsModulos.CTE_POC_45.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_POR_60 = ConstantsModulos.CTE_POC_60.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PORREVAL = ConstantsModulos.CTE_VAR_PORREVAL.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_CSP015 = ConstantsModulos.CTE_VAR_CSP015.concat(CLAVE_MODULO);

	
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/**
	 * Módulo que calcula la Renta de viudedad Viud01 (Endesa). Consiste en la 
	 * Módulo que calcula la Renta de viudedad Viud01 (Endesa). Consiste en la aplicación de la siguiente fórmula para obtener el importe de la prestación en la fecha de pago j para el nodo 2, secuencia 1 (es decir, el cónyuge).  
	 * El flujo nominal obtenido es probable dado que varía en función del momento en que fallece el titular y, por tanto, para cada fecha de pago j es necesario acumular el producto de la prestación calculada en cada momento s por la probabilidad de fallecimiento del titular (nodo 1) en el momento anterior, para s desde la fecha de cálculo hasta la fecha de pago j. 
	 * El importe de la prestación en cada momento es función de un dato específico asociado.
	 */
	
	@Override
	public Object execute(Object... args) throws Solvencia2Excepcion {
		
		BigDecimal csp015 = BigDecimal.ZERO;
		
		try {
			if (ModuloCSP015.LOG.isTraceEnabled()) {
				ModuloCSP015.LOG.trace("Inicio de execute en clase ModuloCSP015");
			}
			
			//Recuperamos los datos que le pasaremos a la función ModuloCSP015
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			csp015 = moduloCSP015(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP015.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP015.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
	
		if (ModuloCSP015.LOG.isTraceEnabled()) {
			ModuloCSP015.LOG.trace("Fin de execute en clase ModuloCSP015");
		}
		
		return csp015;
	}

	
	/**
	 * Módulo que calcula la Renta de viudedad Viud01 (Endesa). Consiste en la 
	 * Módulo que calcula la Renta de viudedad Viud01 (Endesa). Consiste en la aplicación de la siguiente fórmula para obtener el importe de la prestación en la fecha de pago j para el nodo 2, secuencia 1 (es decir, el cónyuge).  
	 * El flujo nominal obtenido es probable dado que varía en función del momento en que fallece el titular y, por tanto, para cada fecha de pago j es necesario acumular el producto de la prestación calculada en cada momento s por la probabilidad de fallecimiento del titular (nodo 1) en el momento anterior, para s desde la fecha de cálculo hasta la fecha de pago j. 
	 * El importe de la prestación en cada momento es función de un dato específico asociado.
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
	
	private BigDecimal moduloCSP015(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {
		
		BigDecimal varcsp015 = BigDecimal.ZERO;
		String varCriterFec;
		String varCriterEdad;
		String varE1PSJ;
		Timestamp varFechaEfecto;
		int varAñoEfecto;
		BigDecimal varPorreval;
		BigDecimal varPor60;
		BigDecimal varPor45;
		List<DetalleCorriente> varProyFptozc;  
		List<DetalleCorriente> varProy238;
		Timestamp varfecJ;
		BigDecimal varFptozc;
		Modulo moduloFPTOZC;
		Timestamp varFechaS;
		BigDecimal varPSJ;
		BigDecimal varPSV;
		BigDecimal varC2;
		Timestamp s;
		BigDecimal varcsp015ant;
		String varTitular;
		BigDecimal varCSP238L;
		BigDecimal E1PSJ = BigDecimal.ZERO;
		
		if (ModuloCSP015.LOG.isTraceEnabled()) {
			ModuloCSP015.LOG.trace("Inicio función << moduloCSP015 >> de la clase ModuloCSP015, para la  iteracion = {}", iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Definición de variables auxiliares para agilizar las operaciones (se llaman varias veces).
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		// Fin de la definición de las variables auxiliares.	
		
		//Varibales de apoyo
		varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
		varCriterEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_VA_CRIT_EDA);
		
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFec);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterEdad);
		}
				
		varE1PSJ = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_E1PSJ, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(),ConstantsModulos.CTE_VAR_E1PSJ);
		if(null == varE1PSJ){
		
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_E1PSJ});
			
		}else{
			E1PSJ = UtilModulos.StringToBigDecimal(varE1PSJ);
			E1PSJ = E1PSJ.setScale(2, RoundingMode.HALF_UP);
			E1PSJ = E1PSJ.divide(new BigDecimal("100"));	
		}
		
		varFechaEfecto = UtilModulos.getVarFechaEfectoSimple(mapVariables, CLAVE_FEC_EFEC, umic);
		
		varAñoEfecto = UtilFechas.getMes(varFechaEfecto); //MEMORIA
		
		varPorreval = UtilModulos.getNumPorcentajeMasUno(new BigDecimal(1.5),mapVariables, CLAVE_VAR_PORREVAL);
		
		varPor60 = UtilModulos.getPorcentaje(mapVariables, CLAVE_VAR_POR_60, new BigDecimal(ConstantsFunciones.CTE_60));
		varPor45 = UtilModulos.getPorcentaje(mapVariables, CLAVE_VAR_POC_45, new BigDecimal(45));
		varTitular = umic.getDatosGenerales().getKbencon();
		varProyFptozc = proyUmic;
		varProy238 = proyUmic;
		
		//Parte funcional
		
		if(bloqueCorriente.getFechaDevengo() == null){
			
			return varcsp015;
			
		}
		
		varfecJ = bloqueCorriente.getFechaDevengo(); 
		if(varTitular.equals("101")){
		moduloFPTOZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FPTOZC);
		varFptozc = (BigDecimal) moduloFPTOZC.execute(varProyFptozc, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,codSubproceso);
		varFechaS = bloqueCorriente.getFechaDevengo();
		varPSJ = E1PSJ.multiply(varPorreval.pow(UtilFechas.getAnio(varFechaS)-2001));
		varPSV = varPor45.multiply(varPSJ);
		varC2 = varPor60.multiply(varPSJ).add(varPSV);
			
			varcsp015ant = (BigDecimal) mapVariables.get(CLAVE_VAR_CSP015);
			if(null == varcsp015ant){
				varcsp015 = varC2.multiply(varFptozc);
			}else{
				varcsp015 = varcsp015ant.add(varC2.multiply(varFptozc));
			}
			
		mapVariables.put(CLAVE_VAR_CSP015, varcsp015);
		
		}else{
			Modulo moduloCSP238L = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP238L);
			varCSP238L = (BigDecimal) moduloCSP238L.execute(varProy238,bloqueCorriente, iteracion, fcalc, umic, btcUmic,mapVariables, codSubproceso);
			varcsp015 = varCSP238L;
		}
		
		if (ModuloCSP015.LOG.isTraceEnabled()) {
			ModuloCSP015.LOG.trace("Fin de la función << moduloCSP015 >> de la clase ModuloCSP015, para la iteración = {}", iteracion);
		}
		
		return varcsp015;
	}


}
