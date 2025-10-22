package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
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
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * 
 * @author Szilard Toth
 *
 */

public class ModuloPRI001CSR implements Modulo{
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloPRI001CSR.class);
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_PRI001CSR;
	private static final String CLAVE_VA_CRIT_EDA = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA.concat(CLAVE_MODULO);
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_GIPC = ConstantsModulos.CTE_VAR_GIPC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_GIC = ConstantsModulos.CTE_VAR_GIC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_GE = ConstantsModulos.CTE_VAR_GE.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_DENOMINADOR = ConstantsModulos.CTE_VAR_DENOMINADOR.concat(CLAVE_MODULO);
	private static final String CLAVE_VARI1 = ConstantsModulos.CTE_VARI1.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_FACTORI1 = ConstantsModulos.CTE_VAR_FACTOR_I1.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_FACTOR_RIESGO = ConstantsModulos.CTE_VAR_FACTOR_RIESGO.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_X = ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCM_ZC = ConstantsModulos.CTE_VAR_TCM_ZC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCM_RENO_HASTA = ConstantsModulos.CTE_VAR_TCM_RENO_HASTA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_X_RENO_HASTA = ConstantsModulos.CTE_VAR_X_RENO_HASTA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_INV = ConstantsModulos.CTE_VAR_VAL_TAB_INV.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ANT_RENOVA = ConstantsModulos.CTE_VAR_ANT_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PROX_RENOVA = ConstantsModulos.CTE_VAR_PROX_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_INV_SCR = ConstantsModulos.CTE_VAR_VAL_TAB_INV_SCR.concat(CLAVE_MODULO);
	
	// Fin de las variables estáticas usadas para agilizar operaciones.
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		//Fin variables locales
		try {
			if (ModuloPRI001CSR.LOG.isTraceEnabled()) {
				ModuloPRI001CSR.LOG.trace("Inicio de execute en clase ModuloPRI001CSR");
			}
			//Recuperamos los datos que le pasaremos a la función ModuloPRI001C
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			
			//Invocamos a la función moduloPRI001C
			resultado = moduloPRI001CSR(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso ,mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloPRI001CSR.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloPRI001CSR.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		if (ModuloPRI001CSR.LOG.isTraceEnabled()) {
			ModuloPRI001CSR.LOG.trace("Fin de execute en clase ModuloPRI001CSR");
		}
		return resultado;
	}
	/**
	 * 
	 * Usaremos este módulo cómo módulo de prima de fallecimiento 
	 * en renovaciones para la garantía complementaria.
	 * 
	 * @param proyUmic
	 * 			Estructura detalleCorrientes de la umic
	 * @param bloqueCorriente
	 * 			Bloque de Trabajo de la corriente
	 * @param iteracion
	 * 			Indica el periodo de proyección J que se está calculando de entre todos los periodos de proyección de la umic (proyUmic)
	 * @param fcalc
	 * 			Fecha de calculo
	 * @param umic
	 * 			Contiene los datos de la Umic que se está procesando
	 * @param btcUmic
	 * 			Contiene el detalle de la base técnica de cálculo para la umic
	 * @param codSubproceso
	 * 			Código del subproceso que se está ejecutando.
	 * @param mapVariables 
	 * 			mapa con las variables de memoria necesarias
	 */
	@SuppressWarnings({"unused" })
	private BigDecimal moduloPRI001CSR(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final String codSubproceso, final Map<String, Object> mapVariables) {
		//variables locales
		BigDecimal pri001C = BigDecimal.ZERO;
		BigDecimal varGipc = BigDecimal.ZERO;
		BigDecimal varGic = BigDecimal.ZERO;
		BigDecimal varGE = BigDecimal.ZERO;
		BigDecimal varDenominador = BigDecimal.ZERO;
		BigDecimal varI1 = BigDecimal.ZERO;
		BigDecimal varFactorI1 = BigDecimal.ZERO;
		BigDecimal varPriesgo = BigDecimal.ZERO;
		BigDecimal varFactorRiesgo = BigDecimal.ZERO;
		int varAnoNac;
		String varCriterioEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriFec = ConstantsFunciones.CTE_CADENA_VACIA;
		String varTm;
		String varTabInv;
		List<BigDecimal> varValoresTabInv;
		BigDecimal varIXRenoHasta;
		Timestamp varAntRenova;
		Timestamp varProxRenova;
		Timestamp varFechaEfecto = null;
		BigDecimal varP = BigDecimal.ZERO;
		BigDecimal varX;
		BigDecimal varTcmZc;
		BigDecimal varTcmRenoHasta;
		BigDecimal varXRenoHasta;
		BigDecimal varComplementarioj = BigDecimal.ZERO;
		BigDecimal varNP = BigDecimal.ZERO;
		BigDecimal varEdadCalc;
		BigDecimal varFracc0;
		BigDecimal varZc;
		//Fin variables locales
		if (ModuloPRI001CSR.LOG.isTraceEnabled()) {
			ModuloPRI001CSR.LOG.trace("Inicio de la función << moduloPRI001CSR >> de la clase ModuloPRI001CSR, para la iteración = {}", iteracion);
		}
		//Validamos los parametros de entrada
        ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		/**
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios 
		 * para el cálculo que no varían por periodo, así como las varibales internas que 
		 * tampoco varían por periodo, y que se dejarán accesibles para su uso en el 
		 * subproceso por los siguientes periodos a calcular. 
		 * 	
		 * 	Variables Módulo
		 * 		varGipc =  btcUmic.gtoRosspPrima /100 
		 * 		varGic = btcUmic.gtoRosspCap /100
		 * 		varGE = umic.baseTecIni.pgastgesex1I/100
		 * 		varDenominador = 1 – varGipc - varGE
		 * 		varI1 = btcUmic.itcalc1/100
		 * 		varFactorI1 = (1+ varI1 )^0,5
		 * 		varTm = btcUmic.tabla1Aseg1
		 * 		varAnoNac = Año (umic.asegurados.fnacAseg1)
		 * 		varPriesgo = umic.baseTecIni.priesgo
		 * 		varFactorRiesgo = 1 + varPriesgo
		 * 		varTabInv= btcUmic.tabla2Aseg1
		 * 		varValoresTabInv= obtenerConfiguracion. recuperarValoresExperiencia(btcUmic.fecCierre, varTabInv, varAnoNac, btcUmic. itcalc1, umic.baseTecIni. psobremort, umic.baseTecIni. priesgo)
		 * 		varAntRenova = umic.fechas.fecdesderenova
		 * 		varProxRenova = umic.fechas.fechastarenova
		 * 		varP = 12
		 * 
		 * 	Si la póliza no está reducida, es decir  umic.datosGenerales.csitupol=  ‘VI’
		 * 		Si umic.datosGenerales.cnegocio = 'I'
		 * 			varfechaEfecto = umic.fechas.fecefecIni
		 * 		En caso contrario:
		 * 			varfechaEfecto = umic.fechas.fecinisus
		 * 	Si la póliza está reducida, es decir  umic.datosGenerales.csitupol=  ‘RE’
		 * 		varfechaEfecto = umic.fechas.fecefecred
		 * 		Si umic.fechas.fecefecred es nula y umic.datosGenerales.cnegocio = ‘C’:
		 * 			varfechaEfecto = umic.fechas.fecfinpagprim
		 * 
		 * 		varX= nedad(varfechaEfecto, umic.asegurados.fnacAseg1, VarCriterEdad, umic.rentas.fecini) --> dejo la variable en memoria, disponible para el subproceso de la umic.
		 * 		varTcmZc = TCM(varfechaEfecto, Fcalc) 
		 * 		varTcmRenoHasta = TCM(Fcalc, varProxRenova)   
		 * 		varXRenoHasta = x + entero((varTcmZc + varTcmRenoHasta)/ varP)
		 * 		varIxRenoHasta  = varValoresTabInv (varXRenoHasta).kwvalor
		 * 		
		 * 	Dejo las variables en memoria para el subproceso de la umic.
		 * 	
		 * 	Para cualquier periodo j (j >= 1), se calculará el importe de la prima correspondiente al periodo j como: 
		 * 		Si proyUmic(j).fecPago es nula: 
		 * 			pri001C = BigDecimal.ZERO;
		 * 		Si proyUmic(j).fecPago es no nula:
		 * 			Si proyUmic(j). fecPago >  varAntRenova  y  proyUmic(j). fecPago >  varProxRenova  (periodo en el que renueva la póliza)
		 * 				varAntRenova = varProxRenova varProxRenova = varProxRenova + 1 año. --> Se guardan las variables en memoria para el subproceso de la umic.
		 * 				varTcmRenoHasta = TCM(Fcalc, varProxRenova) 
		 * 				varXRenoHasta = x + entero((varTcmZc + varTcmRenoHasta)/ varP)
		 * 				varIxRenoHasta  = varValoresTabInv (varXRenoHasta).kwvalor
		 * 			--> Se sobreescriben las variables en memoria para el subproceso de la umic.
		 * 
		 * 		Si no se cumple la condición se mantendrá el valor de las variables varAntRenova, varProxRenova, varTcmRenoHasta, varXRenoHasta, varIxRenoHasta  
		 * 			
		 *	varComplementarioj = proyUmic(j).corrienteComplementario.impFlujoNominal
		 *	
		 *	PRI001C(j)=varComplementarioj * [((varFactorI1 * varIxRenoHasta* varFactorRiesgo )+varGic ))/(varDenominador )]
		 */
        // Calculo variables de apoyo
		varCriterioEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_VA_CRIT_EDA);
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
     	
		if (iteracion.equals(ConstantsFunciones.CTE_FIRST_ITER)){
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterioEdad);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
		}
		
		// Fin variables de apoyo
        
		// Variables modulo
		varGipc = UtilModulos.getVarGicDiv100(mapVariables, CLAVE_VAR_GIPC, btcUmic.getGtorosspPrima());
		varGic = UtilModulos.getVarGicDiv100(mapVariables, CLAVE_VAR_GIC, btcUmic.getGtorosspCap());
		varGE = UtilModulos.getPorcentaje(mapVariables, CLAVE_VAR_GE, umic.getBti().getPgastgesex1I());
		varDenominador = UtilModulos.getVarDenominador(mapVariables, CLAVE_VAR_DENOMINADOR, BigDecimal.ONE, varGipc, varGE);
		varI1 = UtilModulos.getPorcentaje(mapVariables, CLAVE_VARI1, btcUmic.getItcalc().get(0));
		varFactorI1 = UtilModulos.getVarFactorI1(mapVariables, CLAVE_VAR_FACTORI1, BigDecimal.ONE, varI1, ConstantsFunciones.CTE_OPER_0_PUNTO_5);
		varTm = btcUmic.getTablacalc1aseg1();
		varAnoNac = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1());
		varPriesgo = umic.getBti().getPriesgo();
		varFactorRiesgo = UtilModulos.getVarFactorRiesgo(mapVariables, CLAVE_VAR_FACTOR_RIESGO, varPriesgo);
		varTabInv = btcUmic.getTablacalc2aseg1();
		
		varValoresTabInv = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_INV, umic, btcUmic,
				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterioEdad, ConstantesSolvencia.CTE_TABMORT_I);
		varAntRenova = UtilModulos.getWFechaRenov(mapVariables, CLAVE_VAR_ANT_RENOVA, umic.getFechas().getFecdesderenova());
		varProxRenova = UtilModulos.getVarProxRenova(mapVariables, CLAVE_VAR_PROX_RENOVA, umic.getFechas().getFechastarenova());
		varP = ConstantsFunciones.CTE_OPER_12;
		varFechaEfecto = UtilModulos.getVarFechaEfecto(mapVariables, CLAVE_FEC_EFEC, umic);
		
		if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC)){
			// Se calcula la edad en la fecha de cálculo
			varEdadCalc = FuncionesAuxiliares.nEdad(varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriterioEdad, umic.getRentas().getFecIni(), umic.getDatosGenerales().getEdifer());
			varFracc0 = FuncionesAuxiliares.nAnnos(varFechaEfecto, fcalc, varCriFec);
			varZc = varEdadCalc.add(varFracc0);
			// Se estresa incapacidad
			varValoresTabInv = UtilModulos.getValValoresIxSCRINC(mapVariables, CLAVE_VAR_VAL_TAB_INV_SCR, umic, btcUmic, varValoresTabInv, varZc);
		}
		
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriterioEdad, umic.getRentas().getFecIni(), umic.getDatosGenerales().getEdifer()); 
		varTcmZc = new BigDecimal(UtilModulos.getVarTCm(mapVariables, CLAVE_VAR_TCM_ZC, ConstantsFunciones.CTE_FIRST_ITER, varFechaEfecto, fcalc));
		varTcmRenoHasta = new BigDecimal(UtilModulos.getVarTCm(mapVariables, CLAVE_VAR_TCM_RENO_HASTA, ConstantsFunciones.CTE_FIRST_ITER, fcalc,varProxRenova));
		varXRenoHasta = UtilModulos.getVarXRenoHasta(mapVariables, CLAVE_VAR_X_RENO_HASTA, varX, varTcmZc, varTcmRenoHasta, varP);
		varIXRenoHasta = varValoresTabInv.get(varXRenoHasta.intValue());
		
		if (ConstantsFunciones.CTE_4_STRING.equals(umic.getPrimas().getCformpago())) {
			varNP = ConstantsFunciones.CTE_OPER_12;
		} else if (ConstantsFunciones.CTE_3_STRING.equals(umic.getPrimas().getCformpago())) {
			varNP = ConstantsFunciones.CTE_OPER_4;
		} else if (ConstantsFunciones.CTE_1_STRING.equals(umic.getPrimas().getCformpago()) || ConstantsFunciones.CTE_2_STRING.equals(umic.getPrimas().getCformpago())) {
			varNP = new BigDecimal(umic.getPrimas().getCformpago());
		}
		
		if(!(null == bloqueCorriente.getFechaPago())){
			
			if(bloqueCorriente.getFechaPago().after(umic.getFechas().getFecefecfin())){
				if((bloqueCorriente.getFechaPago().after(varAntRenova)) && 
				   (bloqueCorriente.getFechaPago().after(varProxRenova))){
					varAntRenova = varProxRenova;
					mapVariables.put(CLAVE_VAR_ANT_RENOVA, varAntRenova);
					varProxRenova = UtilModulos.incrementaVarProxRenova(mapVariables, CLAVE_VAR_PROX_RENOVA);
					varTcmRenoHasta = new BigDecimal(FuncionesAuxiliares.tcm(fcalc, varProxRenova));
					mapVariables.put(CLAVE_VAR_TCM_RENO_HASTA, varTcmRenoHasta);
					varXRenoHasta = varX.add(new BigDecimal(varTcmZc.add(varTcmRenoHasta).divide(varP).intValue()));
					mapVariables.put(CLAVE_VAR_X_RENO_HASTA, varXRenoHasta);
					varIXRenoHasta = varValoresTabInv.get(varXRenoHasta.intValue());
				}
				varComplementarioj = proyUmic.get(iteracion - 1).getBloqueCompl().getImpFlujoNominal();
				pri001C = varComplementarioj.multiply(((varFactorI1.multiply(varIXRenoHasta).multiply(varFactorRiesgo)).add(varGic)).divide(varDenominador, ConstantsFunciones.MATH_CONTEXT));
			}else{
				pri001C = umic.getPrimas().getIprimatarada().multiply(BigDecimal.ONE.divide(varNP, ConstantsFunciones.MATH_CONTEXT));
			}
		}
		if (ModuloPRI001CSR.LOG.isTraceEnabled()) {
			ModuloPRI001CSR.LOG.trace("Fin de la función << moduloPRI001CSR >> de la clase ModuloPRI001CSR, para la iteración = {}", iteracion);
		}
		return pri001C;
	}
}
