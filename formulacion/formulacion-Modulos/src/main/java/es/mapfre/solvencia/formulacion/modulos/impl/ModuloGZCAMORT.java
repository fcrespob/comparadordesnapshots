package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;							  
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
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
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Usaremos este módulo cómo módulo para el cálculo de la cuantía nominal de gastos de amortización
 */
public class ModuloGZCAMORT implements Modulo {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZCAMORT.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_GZC_AMORT;
	private static final String CLAVE_VAR_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_GIC = ConstantsModulos.CTE_VAR_GIC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_GIPC = ConstantsModulos.CTE_VAR_GIPC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_FACTOR_GF = ConstantsModulos.CTE_VAR_FACTOR_GF.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_FACTOR_PRGF = ConstantsModulos.CTE_VAR_FACTOR_PRGF.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_X = ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VRTA = ConstantsModulos.CTE_VAR_VRTA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_FACTOR_VRTA = ConstantsModulos.CTE_VAR_FACTOR_VRTA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_FACTOR_VRTAM = ConstantsModulos.CTE_VAR_FACTOR_VRTAM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_FACTOR_VRTAN = ConstantsModulos.CTE_VAR_FACTOR_VRTAN.concat(CLAVE_MODULO);

	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);																			  
	@Override
	public String getNombreServicio() {
		
		return CLAVE_MODULO;
	}

	@SuppressWarnings("unchecked")
	public Object execute(Object... args) throws Solvencia2Excepcion {
		
		BigDecimal resultado = BigDecimal.ZERO;
		
		try {
			if (ModuloGZCAMORT.LOG.isTraceEnabled()) {
				ModuloGZCAMORT.LOG.trace("Inicio de execute en clase ModuloGZCAMORT");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZCAMORT
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.
			
			//Se invoca a la función de calculo GZCAMORT
			resultado = moduloGZCAMORT(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloGZCAMORT.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZCAMORT.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloGZCAMORT.LOG.isTraceEnabled()) {
			ModuloGZCAMORT.LOG.trace("Fin de execute en clase ModuloGZCAMORT");
		}
		
		return resultado;
	}

	/**
	 * Módulo para el cálculo de la cuantía nominal de gastos de amortización
	 * 
	 * @param proyUmic
	 * @param bloqueCorriente
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param mapVariables
	 * @param codSubproceso
	 * @return
	 */
	private BigDecimal moduloGZCAMORT(final List<DetalleCorriente> proyUmic, 
			                          final BloqueCorriente        bloqueCorriente, 
			                          final int                    iteracion,
			                          final Timestamp              fcalc, 
			                          final Umic                   umic, 
			                          final DetalleBaseTecnica     btcUmic, 
			                          final Map<String, Object>    mapVariables,
			                          final String                 codSubproceso) {
		
		BigDecimal       varGzcAmort = BigDecimal.ZERO;
		String           varCriterioEdad;
		String 			 varCriterFec;				  
		BigDecimal       varGic;
		BigDecimal       varGipc;
		BigDecimal       varFactorGF;
		BigDecimal       varFactorPRGF;
		BigDecimal       varPrima;
		BigDecimal       varI1;
		BigDecimal       varI2;
		String           varTabMort;
		Integer          varAnoNac;
		List<BigDecimal> varValoresTabMort;
		Timestamp		 varFechaEfecto;
		BigDecimal       varX;   
		Integer          varTcm;
		BigDecimal       varVRTA;
		BigDecimal       varFactorVRTA;
		Integer          varBeta;
		BigDecimal       varCj = BigDecimal.ZERO;
		Integer          varM;
		Integer          varN;
		BigDecimal       varVRTAM;
		BigDecimal       varVRTAN;
		
		if (ModuloGZCAMORT.LOG.isTraceEnabled()) {
			ModuloGZCAMORT.LOG.trace("Inicio función << moduloGZCAMORT >> de la clase ModuloGZCAMORT, para la  iteracion = {}", iteracion);
		}
		
		/**
		 *  Variables de Apoyo
		 * 
		 *  	VarCriterFec obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
		 * 
		 *  	Si la variable de apoyo retornada es nula se devuelve error funcional 005 - No se ha encontrado la Variable de Apoyo &NombreVariableApoyo, finalizando el proceso para la UMIC.
		 *  Variables Módulo
		 * 
		 *  	varGic =btcUmic.gtoRosspCap/100
		 *  	varGipc =btcUmic.gtoRosspPrima/100
		 *  	varFactorGF  = umic.baseTecIni.pgastgesin3/12
		 *  	varFactorPRGF = 1 + (umic.baseTecIni.pgastgesin4/100)
		 *  	varPrima = umic.primas.primanetaini
		 *  	varI2 = btcUmic.itcalc2
		 *  	varTabMort = btcUmic.tabla1Aseg1
		 *  	varAnoNac = Año (umic.asegurados.fnacAseg1);
		 *  	varValoresTabMort = obtenerConfiguracion. recuperarValoresExperiencia(btcUmic.fecCierre, varTabMort, varAnoNac, btcUmic. itcalc1, umic.baseTecIni.psobremort, umic.baseTecIni. priesgo)
		 *  	Si la póliza no está reducida, es decir,  umic.datosGenerales.csitupol=  ‘VI’
		 *  	Si umic.datosGenerales.cnegocio = ‘I’ 
		 *  	varfechaEfecto = umic.fechas.fecefecIni
		 *  	En caso contrario: 
		 *  	varfechaEfecto = umic.fechas.fecinisus
		 *  	Si la póliza está reducida, es decir  umic.datosGenerales.csitupol=  ‘RE’
		 *  	varfechaEfecto = umic.fechas.fecefecred
		 *  	Si umic.fechas.fecefecred es nula y umic.datosGenerales.cnegocio = ‘C’:
		 *  	varfechaEfecto = umic.fechas.fecfinpagprim
		 * 
		 *  	varX = nedad(varfechaEfecto, umic.asegurados.fnacAseg1, varCriterEdad, umic.rentas.fecini)
		 *  	VarTcm = TCM (findemes(varfechaEfecto),fcalc);
		 *  	varVRTA = VRTA (0,0,99, varI2, varX, varValoresTabMort,12,0);
		 *  	varFactorVRTA = varGipc * (varPrima/(1+ varVRTA ))
		 * 
		 *  Dejo las variables en memoria, disponibles para el subproceso de la umic.
		 * 
		 * 
		 *  Se calcularán las distintas variables internas necesarias para el cálculo, dependientes del periodo j a calcular, como se describe a continuación: 
		 * 
		 *  Para cualquier periodo J se calcuará: 
		 * 
		 *  	Si J = 1
		 *  	varBeta= 0
		 *  	En caso contrario:
		 *  	varBeta= varBeta +1
		 * 
		 *  	 Si proyUmic(j).varBloque.fecDevengo es nula: 
		 *           
		 *      	Gzc_Amort (j)= 0
		 * 
		 *  	Si proyUmic(j).varBloque.fecDevengo es no nula: 
		 * 
		 *  	varCj = proyUmic(j).corrienteFallecimiento.impFlujoNominal
		 * 
		 *  Finalmente, para todo j:
		 * 
		 *  Gzc_Amort (j) = (varGic * varCj) + varFactorVRTA + [varFactorGF *  (varFactorPRGF )^(entero((varTCM+  varBeta)/12))]
		 */
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Variables de apoyo
		varCriterioEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), ConstantsModulos.CTE_VA_CRIT_EDA);
		varCriterFec =  UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), ConstantsModulos.CTE_VA_CRIT_FEC);
		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterioEdad);
				ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFec);										 
		}// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		// Fin de la validación de las variables de apoyo.
		
		// Variables de módulo
		varGic = UtilModulos.getVarGicDiv100(mapVariables, CLAVE_VAR_GIC, btcUmic.getGtorosspCap());
		varGipc = UtilModulos.getPorcentaje(mapVariables, CLAVE_VAR_GIPC, btcUmic.getGtorosspPrima());
		varFactorGF = UtilModulos.getVarFactorGF(mapVariables, CLAVE_VAR_FACTOR_GF, umic.getBti().getPgastgesin3I());
		varFactorPRGF = UtilModulos.getNumPorcentajeMasUno(umic.getBti().getPgastgesin4I(), mapVariables, CLAVE_VAR_FACTOR_PRGF);
		varPrima = umic.getPrimas().getIprimanetaini();
		varI2 = btcUmic.getItcalc().get(ConstantsModulos.CTE_INT_1);
		varI1 = btcUmic.getItcalc().get(0);
		varTabMort = btcUmic.getTablacalc1aseg1();
		varAnoNac = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1());
		varValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterioEdad, ConstantesSolvencia.CTE_TABMORT_L);
		varFechaEfecto = UtilModulos.getVarFechaEfecto(mapVariables, CLAVE_FEC_EFEC, umic);
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriterioEdad, umic.getRentas().getFecIni(), umic.getDatosGenerales().getEdifer());
		varTcm = UtilModulos.getVarTCm(mapVariables, CLAVE_VAR_TCM, ConstantsModulos.CTE_FIRST_ITER, UtilFechas.getUltimoDiaDelMes(varFechaEfecto), fcalc);
		varM = umic.getDuraciones().getNdurprima(); 
		varN = umic.getDuraciones().getNdursegano() + umic.getDuraciones().getNdursegmes()/12;
		
		/*varVRTA = UtilModulos.getVarVRTA(mapVariables, CLAVE_VAR_VRTA, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_99, varI2, ConstantsFunciones.CTE_99, varI2,
				varX.intValue(), varValoresTabMort, ConstantsFunciones.CTE_12, BigDecimal.ZERO, false, ConstantsFunciones.CTE_0);*/
		
		
		varVRTAM = UtilModulos.getVarVRTA(mapVariables, CLAVE_VAR_FACTOR_VRTAM, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, varM-1, varI1, ConstantsFunciones.CTE_99, varI2,
				varX.intValue(), varValoresTabMort, ConstantsFunciones.CTE_1, BigDecimal.ZERO, false, umic.getDuraciones().getNdursegmes());
		varVRTAN = UtilModulos.getVarVRTA(mapVariables, CLAVE_VAR_FACTOR_VRTAN, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, varN-1, varI1, ConstantsFunciones.CTE_99, varI2,
				varX.intValue(), varValoresTabMort, ConstantsFunciones.CTE_12, BigDecimal.ZERO, false, umic.getDuraciones().getNdursegmes());
		varFactorVRTA = UtilModulos.getVarFactorVrtaGZCAMORT(mapVariables, CLAVE_VAR_FACTOR_VRTA, varGipc, varPrima, varVRTAM, varVRTAN);
		// Fin de variables de módulo
		
		// Para cualquier periodo
		varBeta = iteracion - 1;
		
		if (null != bloqueCorriente.getFechaDevengo()){
			if(ConstantsModulos.CTE_UMIC_PRINCIPAL.equals(umic.getDatosGenerales().getSpcom())){
				varCj = proyUmic.get(iteracion-1).getBloqueFall().getImpFlujoNominal();
			}else{
				varCj = proyUmic.get(iteracion-1).getBloqueCompl().getImpFlujoNominal();
			}
			
			varGzcAmort = (varGic.divide(ConstantsFunciones.CTE_OPER_12,ConstantsFunciones.MATH_CONTEXT).multiply(varCj)).add(varFactorVRTA).add(varFactorGF.multiply(varFactorPRGF.pow((varTcm + varBeta)/ConstantsFunciones.CTE_12)));
		}
			
		if (ModuloGZCAMORT.LOG.isTraceEnabled()) {
			ModuloGZCAMORT.LOG.trace("Fin función << moduloGZCAMORT >> de la clase ModuloGZCAMORT, para la iteracion = {} con resultado varGZC024 = {}", iteracion, varGzcAmort);
		}
		
		return varGzcAmort;
	}

}
