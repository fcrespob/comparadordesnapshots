package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalFlujoProyeccion;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesActualizacionFinanciera;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.FuncionesGastos;
import es.mapfre.solvencia.formulacion.util.FuncionesPrimas;
import es.mapfre.solvencia.formulacion.util.FuncionesVBX;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilFechas.Fecha;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo VBX362.
 * La expresión matemática para su determinación es la siguiente:
 * 				(TC, y)BdxPU ={Sumatorio desde j=fcal hasta fnac[CSP047(j) * VVIDA(fcal,actj)* VZREVER(fcal, j)]} * (1+GI) + PUCCAPDIFER(fcal) * OBADORPRIM(fcal) +
 * 				PU * Gipc *(GASTGIVIT(fcal)/GASTGIVITINI)
 * @author agonzalezgar
 *
 */
public class ModuloVBX362 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVBX362.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VBX362;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	
	private static final String CLAVE_MOD_BETA = ConstantsModulos.CTE_VA_MODBETA;
	private static final String CLAVE_VAR_MOD_BETA = CLAVE_CRIEDAD.concat(CLAVE_MOD_BETA);
		
	private static final String CLAVE_VAR_W = ConstantsModulos.CTE_VAR_W.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_M = ConstantsModulos.CTE_VAR_M.concat(CLAVE_MODULO);
	private static final String CLAVE_VARN = ConstantsModulos.CTE_VARN.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_X = ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);

	private static final String CLAVE_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT;
	private static final String CLAVE_VAR_VAL_TAB_MORT = CLAVE_VAL_TAB_MORT.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_BETA1 = ConstantsModulos.CTE_VAR_BETA1.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_BETA2 = ConstantsModulos.CTE_VAR_BETA2.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_EDAD_MAXIMA_2 = ConstantsModulos.CTE_VAR_EDAD_MAXIMA_2.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_FECHA_VCTO = ConstantsModulos.CTE_VAR_FECHA_VCTO.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ANT_RENOVA = ConstantsModulos.CTE_VAR_ANT_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PROX_RENOVA = ConstantsModulos.CTE_VAR_PROX_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_NR = ConstantsModulos.CTE_VAR_NR.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TC = ConstantsModulos.CTE_VAR_TC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_MAPA_ANNOS = "mapanAnnos".concat(CLAVE_MODULO);
	private static final String CLAVE_LISTADO_POTENCIAS = "listadoPotencias".concat(CLAVE_MODULO);
	

	// Fin de las variables estáticas usadas para agilizar operaciones.

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}
	
	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la llamada a la función que realiza los calculos del modulo.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		//Variables locales
		TotalFlujoProyeccion resultado = null;
		//Fin variables locales
		
		try {
			
			if (ModuloVBX362.LOG.isTraceEnabled()) {
				ModuloVBX362.LOG.trace("Inicio de execute en clase ModuloVBX362");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloVBX362
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente =  (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			//final String terminal = (String) args[ConstantsModulos.PARAM_P_TERMINAL];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo VBX362
			resultado = moduloVBX362(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVBX362.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVBX362.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloVBX362.LOG.isTraceEnabled()) {
			ModuloVBX362.LOG.trace("Fin de execute en clase ModuloVBX362");
		}
		
		return resultado;
	}
	
	/**
	 * Módulo que calcula el importe nominal para la proyección de Provisión Matemática por Fórmula Cerrada
	 *  La expresión matemática para su determinación es la siguiente:
	 * 				(TC, y)BdxPU ={Sumatorio desde j=fcal hasta fnac[CSP047(j) * VVIDA(fcal,actj)* VZREVER(fcal, j)]} * (1+GI) + PUCCAPDIFER(fcal) * OBADORPRIM(fcal) +
 	 *				PU * Gipc *(GASTGIVIT(fcal)/GASTGIVITINI)
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
	 * @param mapVariables 
	 * 			mapa con las variables de memoria necesarias
	 * @param codSubproceso
	 * 			Código del subproceso que se está ejecutando
	 * @param terminal
	 * 			Terminal de cálculo
	 */
	@SuppressWarnings("unchecked")
	private TotalFlujoProyeccion moduloVBX362(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables,
			final String codSubproceso) {		
		//Variables locales
		BigDecimal vbx362 = BigDecimal.ZERO;
		final TotalFlujoProyeccion salida = new TotalFlujoProyeccion();
		String varCriFec;
		String varBeta;
		String varCriEdad;
		BigDecimal varI1bti;
		BigDecimal varI2bti;
		BigDecimal varPu;
		Integer varNRM;
		Integer varNR;
		BigDecimal varI1;
		BigDecimal varI2;
		BigDecimal varI1PorcentajeMasUno;
		BigDecimal varI2PorcentajeMasUno;
		int varAnoNac;
		int varW = 0;
		BigDecimal varGipc;
		BigDecimal varGic;
		BigDecimal varGicPorcentajeMasUno;
		BigDecimal varGipcPorcentaje;
		BigDecimal varM = BigDecimal.ZERO;
		BigDecimal varN = BigDecimal.ZERO;
		BigDecimal varDifercol = BigDecimal.ZERO;
		BigDecimal varX = BigDecimal.ZERO;
		int varTc0;
		BigDecimal varBeta1 = BigDecimal.ZERO;
		BigDecimal varBeta2 = BigDecimal.ZERO;
		BigDecimal varTcy = BigDecimal.ZERO;
		BigDecimal varTcyVida = BigDecimal.ZERO;
		BigDecimal varTcyPu = BigDecimal.ZERO;
		BigDecimal varRy = BigDecimal.ZERO;
		BigDecimal vary = BigDecimal.ZERO;
		BigDecimal varPUCCAPdifer = BigDecimal.ZERO;
		BigDecimal varRen = BigDecimal.ZERO;
		BigDecimal varPartAnoNR = BigDecimal.ZERO;
		Timestamp varfecJ;
		Timestamp varfcalcJMenos1Dia;
		Timestamp varfecJ1;
		BigDecimal varJ;
		BigDecimal varJ1;
		BigDecimal varZc;
		BigDecimal varVVida = BigDecimal.ZERO;
		BigDecimal varVProb = BigDecimal.ZERO;
		BigDecimal varCSP238 = BigDecimal.ZERO;
		BigDecimal varVVidaCSP238 = BigDecimal.ZERO;
		Modulo modProbable;
		BigDecimal varObadorprim = BigDecimal.ZERO;
		BigDecimal varGastivit = BigDecimal.ZERO;
		BigDecimal varGastgivitini = BigDecimal.ONE;
		List<BigDecimal> lstTabMort = null;
		Timestamp varAntRenova = null;
		Timestamp varProxRenova = null;
		Timestamp varFecVcto = null;
		Timestamp varAniversario;
		Timestamp varFechaEfecto;
		BigDecimal primerOperando = BigDecimal.ZERO;
		BigDecimal segundOperando = BigDecimal.ZERO;
		BigDecimal tercerOperando = BigDecimal.ZERO;
		//Fin variables locales
		if (ModuloVBX362.LOG.isTraceEnabled()) {
			ModuloVBX362.LOG.trace("Inicio función << moduloVBX362 >> de la clase ModuloVBX362, para la iteracion = {}", iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, fcalc, umic, btcUmic);
		
		/**
		 * Si estoy en el primer periodo, se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, 
		 * así como las variables internas que tampoco varíen por periodo, y que se dejarán accesibles para su uso en el subproceso 
		 * por los siguientes periodos a calcular.
		 * 
				Variables de Apoyo
				-	VarCriterFec--> obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
				-	VarBeta --> obtenerConfiguracion.recuperarVariableApoyo(MODBETA)
				-	VarCriterEdad --> obtenerConfiguracion.recuperarVariableApoyo(ID-CRITERIO)
				
				Si alguna de las variable de apoyo  retornada es nula se devuelve error funcional 005 - 
				No se ha encontrado la Variable de Apoyo & NombreVariableApoyo, finalizando el proceso para la UMIC.
				
				Variables Módulo
				-	varI1Bti = umic.baseTecIni. pintertecnI1
				-	varI2Bti =  umic.baseTecIni. pintertecnI2
				-	varPU =  umic.primas.iprimatarada
				-	varNR = umic.duraciones.nrenovaciones
				-	varI1= btcUmic.itcalc1
				-	varI2= btcUmic.itcalc2
				-	varTm = btcUmic.tabla1Aseg1
				-	varAnoNac = Año (umic.asegurados.fnacAseg1);
				-	varVcto = umic.fechas.fecfin
				-	varValoresTabMort = obtenerConfiguracion. recuperarValoresExperiencia(umic.datosgenerales.fecCierre, varTm, varAnoNac, varI1, umic.baseTecIni. psobremort, umic.baseTecIni. priesgo)
				-	varW = obtenerConfiguracion.recuperarEdadMax (umic.datosgenerales.fecCierre, varTm, varAnoNac, varI1, umic.baseTecIni. psobremort, umic.baseTecIni. priesgo)
				-	varGipc =  btcUmic .gtoRosspPrima
				-	varGic = btcUmic. gtoRosspCap
				-	VarBeta   --> indica que tipo de modalidad es para calcular varBeta1 y varBeta2
				-	varM = nanos(umic.fechas.fecinisus umic. baseTecIni. fecfinTramo1,  VarCriterFec)
				-	varN =  nanos(umic.fechas.fecinisus, proyUmic.last(fecHasta), VarCriterFec)
				-	varNRM = parte entera (varM)
				-	varAntRenova = umic.fechas. fecdesderenova
				-	varProxRenova = umic.fechas. fechastarenova
				-	varNR = umic.duraciones.nrenovaciones

				-	varDifercol = nanos(umic.fechas.fecinisus,umic.renta.fecini, VarCriterFec)
				-	Si varDifercol > 1 -->  Se sobreescribirá la variable con varDifercol = 1
				-	varX= nedad(umic.fechas.fecinisus, umic.asegurados.fnacAseg1, VarCriterEdad)
				-	VarTc0 = TC(umic.fechas fecinisus, fcalc)
				-	varRen= nanos (umic.fechas.fecinisus, umic.fechas.fechastarenova, VarCriterFec)
				-	varGast (j) = proyUmic(j)
				-	varGastgivitini(j) = GASTGIVITINI(proyUmic, fcalc, umic, btcUmic, codSubproceso)
				-	varTCY = nanos (umic.fechas.fecinisus, fcalc, VarCriterFec)
				-	varTCɣ 1 = varTCɣ
				-	vary =[(año(umic.fechas.fecinisus)*365+ddenero(umic.fechas. fecinisus, VarCriterFec)+dia(umic.fechas. fecinisus))-(año(fcalc)*365+ddenero(fcalc, VarCriterFec)+dia(fcalc))]/365
				-	varPUCCAPdifer = PUCCAPdifer (varPU, varI1Bti ,varI2Bti ,varNR ,varNRM ,varM,varbeta1 ,varbeta2, varDifercol ,varTCY)
		 */
		
		
		//INICIO DE VALORES QUE NO CAMBIAN POR ITERACION
		// Se definen estas variables auziliares dado que se llaman varias veces.
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		
		// Cálculo y validación de las variables de apoyo.
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
		varCriEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		varBeta = UtilModulos.getVarBeta(mapVariables, CLAVE_VAR_MOD_BETA, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_MOD_BETA);

		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriEdad);
			ValidacionesComunesModulos.validarVariableDeApoyoVarModBeta(varBeta);
		}// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		// Fin de la validación de las variables de apoyo.
		
		IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		String varModProb = servicio.recuperarModulo(kmodalidad, kgarantia, umic.getDatosAdicionales().getPrestCal(), btcUmic.getBt(), ConstantsModulos.CTE_PROY_VIDA, "02");
		String varModFall = servicio.recuperarModulo(kmodalidad, kgarantia, umic.getDatosAdicionales().getPrestCal(), btcUmic.getBt(), ConstantsModulos.CTE_PROY_FALL, "01");

		// Si la modalidad de la UMIC que se trata es del negocio Individuales, se toma la fecha
		// de fin de efecto en lugar de la fecha de inicio. (Cambio de alcance 09/2015)
		if (umic.getDatosGenerales().getCnegocio().equals(ConstantesSolvencia.NEGOCIO_INDIVIDUAL)){
			varFechaEfecto = umic.getFechas().getFecefecini();
		} else {
			varFechaEfecto = umic.getFechas().getFecinisus();
		}			
		
		varI1bti = umic.getBti().getPintertecnI1();
		varI2bti = umic.getBti().getPintertecnI2();
		varPu = umic.getPrimas().getIprimatarada();
		varM = UtilModulos.getVarM(mapVariables, CLAVE_VAR_M, varFechaEfecto, umic.getBti().getFecFinTramo1(), varCriFec);
		varN = UtilModulos.getVarNVBX362(mapVariables, CLAVE_VARN, varFechaEfecto, proyUmic.get(proyUmic.size() - 1).getFechaDesde(), varCriFec);
		varNRM = varM.intValue();
		varAntRenova = UtilModulos.getVarAntRenova(mapVariables, CLAVE_VAR_ANT_RENOVA, umic.getFechas().getFecdesderenova());
		varProxRenova = UtilModulos.getVarProxRenova(mapVariables, CLAVE_VAR_PROX_RENOVA, umic.getFechas().getFechastarenova()) ;
		
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriEdad, umic.getRentas().getFecIni(), varEdifer);
		varI1 = btcUmic.getItcalc().get(0);
		varI2 = btcUmic.getItcalc().get(1);
		varI1PorcentajeMasUno = UtilModulos.getNumPorcentajeMasUno(varI1, mapVariables, CLAVE_MODULO);
		varI2PorcentajeMasUno = UtilModulos.getNumPorcentajeMasUno(varI2, mapVariables, CLAVE_MODULO);
		varAnoNac = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1());
		
		if (umic.getRentas().getTempVit().equals(ConstantsModulos.CTE_RENTA_TEMPORAL)){
			varW = varX.intValue() + umic.getDuraciones().getNdursegano();
		}else if (umic.getRentas().getTempVit().equals(ConstantsModulos.CTE_RENTA_VITALICIA)){
			varW = UtilModulos.getVarEdadMaxima2(mapVariables, CLAVE_VAR_W, varAnoNac, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1);
		}else{
			if (LOG.isDebugEnabled()) {
				LOG.debug(Util.errorValidacionA2(umic.getRentas().getTempVit(), ConstantsFunciones.CTE_TEMP_VIT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A2, new String[]{ConstantsFunciones.CTE_TEMP_VIT, umic.getRentas().getTempVit()}); 
		}
		
		ValidacionesComunesModulos.validarEdadMax(varW, umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia());
		varGipc = btcUmic.getGtorosspPrima();
		varGic = btcUmic.getGtorosspCap();
		varGicPorcentajeMasUno = UtilModulos.getNumPorcentajeMasUno(varGic, mapVariables, CLAVE_MODULO);
		varGipcPorcentaje = varGipc.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		
		varNR = (Integer) mapVariables.get(CLAVE_VAR_NR);
		if (varNR == null){
			varNR = umic.getDuraciones().getNrenovaciones();
			mapVariables.put(CLAVE_VAR_NR, varNR);
		}
		
		/**
		 * -	Si VarBeta  = CRP
					-	varBeta1 = varI1Bti 
					-	varBeta2= varI2Bti
				-	Si VarBeta  = CRPI
					-	varBeta1 = 0
					-	varBeta2= 0
				-	Si VarBeta  = SR
					-	varBeta1 = 1 + varI1Bti
					-	varBeta2= 1 + varI2Bti

		 */
		varBeta1 = UtilModulos.getVarBetaX(mapVariables, CLAVE_VAR_BETA1, varBeta, varI1bti);
		varBeta2 = UtilModulos.getVarBetaX(mapVariables, CLAVE_VAR_BETA2, varBeta, varI2bti);
		
		/**
		 *  -	Si  umic.rentas.tempVit = ‘T’  -->  varfecvcto  = umic.fechas.fecefecfin
			-	Si  umic.rentas.tempVit = ‘V’  
					varFecSusc = umic.fechas.fecinisus
					Si umic.asegurados.fnacAseg2 es nulo se hará: 
						-	varEdadCalc1 = entero(nedad(umic.fechas.fecinisus, umic.asegurados.fnacAseg1, VarCriterEdad))
						-	Ffinvitalicia = varFecSusc + Años(varEdadMax1   - varEdadCalc1) --> se añaden (varEdadMax1   - varEdadCalc1) años  a la fecha de suscripción. 
					Si umic.asegurados.fnacAseg2 es NO nulo se hará: 
						-	varEdadCalc 1= entero(nedad(umic.fechas.fecinisus, umic.asegurados.fnacAseg1, VarCriterEdad))
						-	Ffinvitalicia1 = varFecSusc + Años (varEdadMax1   - varEdadCalc1)
						-	varEdadCalc2= entero(nedad(umic.fechas.fecinisus, umic.asegurados.fnacAseg2, VarCriterEdad))
						-	Ffinvitalicia2= varFecSusc + Años(varEdadMax2 – varEdadCalc2)
					 Se establecerá la fecha fin como la mayor de las fechas así halladas, es decir: 
					 varfecvcto  = mayor (Ffinvitalicia1, Ffinvitalicia2)
		 */
		varFecVcto = UtilModulos.getVarFecVcto(mapVariables, CLAVE_VAR_FECHA_VCTO, umic, btcUmic, varW, varCriEdad, CLAVE_VAR_EDAD_MAXIMA_2);
		
		//Timestamp fechaInicioSuscripcion = umic.getFechas().getFecinisus();
		
		//FIN DE VALORES QUE NO CAMBIAN POR ITERACION
		
		
		
		/**
		 * Para cualquier periodo j se calculará VBX362 como:
		 *                varVcto
		 * VBX362(j) = [ {SUMATORIO[varCSP238 * varVVida * varVZRever]} * (1+varGi) ] + ( varPUCCAPdifer * varObadorprim  ) + (varPU * varGipc * (varGastivit/varGastgivitini) )
		 *                varfecJ
		 * 
		 * Donde cada término de la fórmula se obtendrá como sigue:
		 * 
		 * 		varfecJ = proyUmic(j).varBloque.fecDesde
		 * 		varJ = VarTc0 +  nanos(fcalc, varfecJ, CriterFec).
		 * 		VarZc = varX + nanos(umic.fechas.fecinisus, vafecJ, CriterFec)
		 */
		
		//INICIO DE VALORES QUE SI CAMBIAN POR ITERACION
		varfecJ = proyUmic.get(iteracion - 1).getFechaDesde();
		varfcalcJMenos1Dia = UtilFechas.decreDias(varfecJ, 1);
		varTc0 = FuncionesAuxiliares.tc(varFechaEfecto, varfcalcJMenos1Dia);
		varTcy = FuncionesAuxiliares.nAnnos(varFechaEfecto, varfecJ, varCriFec);
		
		if (umic.getRentas().getFecIni() == null) {
			varDifercol = BigDecimal.ZERO;
		} else {
			varDifercol = UtilModulos.getVarDiferColVBX362(mapVariables, CLAVE_MODULO, varFechaEfecto, umic.getRentas().getFecIni(), varfecJ, varCriFec);
		}
		
		varJ = BigDecimal.valueOf(varTc0);
		varZc = varX.add(varTcy); 
		
		/**
		 * o	Si proyUmic(j).fechaDesde  <= varProxRenova < proyUmic(j).fechaHasta 
					-	varAntRenova = varProxRenova
					-	varProxRenova = varProxRenova + 1 año.
					-	varNR = varNR +1
					-	varTCɣ 1= varTCɣ +  nanos (fcalc, varProxRenova, VarCriterFec)
		   o	Si  no se cumple la condición se mantendrá el valor de las variables varA ntRenova, varProxRenova, varNR y varTCɣ.

		 */
				varAniversario = varFechaEfecto;
		
		
		if (varAntRenova.before(varfecJ) && !varProxRenova.after(varfecJ)) {
			varAntRenova = varProxRenova;
			varProxRenova = UtilFechas.incrAnyo(varProxRenova, ConstantsModulos.CTE_INT_1);
			//Guardamos en memoria las fechas modificadas
			mapVariables.put(CLAVE_VAR_ANT_RENOVA, varAntRenova);
			mapVariables.put(CLAVE_VAR_PROX_RENOVA, varProxRenova);
			
			varNR = varNR + 1;
			mapVariables.put(CLAVE_VAR_NR, varNR);
		}
		
		varAniversario = UtilFechas.incrAnyo(varFechaEfecto,varTcy.intValue());
		final Fecha fecJ = UtilFechas.getFecha(varfecJ);
		final Fecha aniversario = UtilFechas.getFecha(varAniversario);
		int dias1;
		int dias2;
		
		//Dias1 = anio(fecha1)*365 + ddenero(fecha1, CriterioFecha) + * dia(fecha1)
		dias1 = fecJ.getAnio() * ConstantsFunciones.CTE_365 + FuncionesVBX.ddEnero(varfecJ, varCriFec) + fecJ.getDia();

		//Dias2 = anio(fecha2)*365 + ddenero(fecha2, CriterioFecha) + dia(fecha2)
		dias2 = aniversario.getAnio() * ConstantsFunciones.CTE_365 + FuncionesVBX.ddEnero(varAniversario, varCriFec) + aniversario.getDia();

		// numAnnos = (dias2 - dias1) /365 
		vary = BigDecimal.valueOf(Math.abs(dias2 - dias1)).multiply(ConstantsFunciones.CTE_OPER_1_PARTIDO_365);
		
		
		
		/**
		 *  -	Si varNR = 0  
					-	Si umic.fechas.fecinisus > umic.fechas.fecdesderenova  y  umic.fechas.fecdesderenova < umic.rentas.fecini <= umic.fechas.fechastarenova
						 -	vaPartAnoNR = =[(año(umic.rentas.fecini)*365+ddenero(umic.rentas.fecini, VarCriterFec)+dia(umic.rentas.fecini))-(año(umic.fechas.fecinisus)*365+ddenero(umic.fechas.fecinisus, VarCriterFec)+dia(umic.fechas.fecinisus))]/365
					
					-	Si umic.fechas.fecinisus > umic.fechas.fecdesderenova y umic.rentas.fecini no está comprendida entre umic.fechas.fecdesderenova y umic.fechas.fechastarenova (es decir umic.fechas.fecdesderenova < umic.rentas.fecini <= umic.fechas.fechastarenova)
						 -	vaPartAnoNR = [(año(umic.fechas.fechastarenova)*365+ddenero(umic.fechas.fechastarenova, VarCriterFec)+dia(umic.fechas.fechastarenova))-(año(umic.fechas.fecinisus)*365+ddenero(umic.fechas.fecinisus, VarCriterFec)+dia(umic.fechas.fecinisus))]/365
					
					
			-	Si varNR > 0  
				-	Si varAntRenova < umic.rentas.fecini <= varProxRenova
						-	vaPartAnoNR =[(año(umic.rentas.fecini)*365+ddenero(umic.rentas.fecini, VarCriterFec)+dia(umic.rentas.fecini))-(año(varAntRenova)*365+ddenero(varAntRenova VarCriterFec)+dia(varAntRenova))]/365
				-	Si varAntRenova <=  varfecvcto    y año(varProxRenova) =  año(varfecvcto ) :
						•	Si varfecJ <= varProxRenova:
							o	vaPartAnoNR = =[(año(varProxRenova)*365+ddenero(varProxRenova, VarCriterFec)+dia(varProxRenova))-(año(varfecJ)*365+ddenero(varfecJ, VarCriterFec)+dia(varfecJ))]/365
						•	Si varfecJ > varProxRenova:
							o	vaPartAnoNR = [(año(varfecvcto   )*365+ddenero(varfecvcto  , VarCriterFec)+dia(varfecvcto  ))-(año(varfecJ)*365+ddenero(varfecJ, VarCriterFec)+dia(varfecJ))]/365
				En cualquier otro caso:
					•	vaPartAnoNR = =[(año(varProxRenova)*365+ddenero(varProxRenova, VarCriterFec)+dia(varProxRenova))-(año(varfecJ)*365+ddenero(varfecJ, VarCriterFec)+dia(varfecJ))]/365
		 */
		
		
		Timestamp fechaDesdeRenovacion = umic.getFechas().getFecdesderenova();
		Timestamp fechaHastaRenovacion = umic.getFechas().getFechastarenova();
		Timestamp fechaInicioRenta = umic.getRentas().getFecIni();
		
		if (fechaInicioRenta == null) {
			fechaInicioRenta = umic.getFechas().getFecinisus();
		}
		
		if (varNR == 0) {
			 if (varFechaEfecto.after(fechaDesdeRenovacion) && 
					 (fechaDesdeRenovacion.before(fechaInicioRenta)  &&  !fechaHastaRenovacion.before(fechaInicioRenta)) ) {
				 if (varfecJ.before(fechaInicioRenta)){
					 varPartAnoNR =  getVarPartAnoNRVariableYVarRyVariable(fechaInicioRenta, varFechaEfecto, varCriFec);
				 }else{
					 varPartAnoNR =  getVarPartAnoNRVariableYVarRyVariable(fechaHastaRenovacion, fechaInicioRenta, varCriFec);
				 }
			 } else {
				 varPartAnoNR =  getVarPartAnoNRVariableYVarRyVariable(fechaHastaRenovacion, varFechaEfecto, varCriFec);
			 }
		} else {
			Fecha calFechaProxRenovacion = UtilFechas.getFecha(varProxRenova);
			Fecha calFechaVencimiento = UtilFechas.getFecha(varFecVcto);
			
			 if (varAntRenova.before(fechaInicioRenta) && !varProxRenova.before(fechaInicioRenta)) {
				 if (varfecJ.before(fechaInicioRenta)){
					 varPartAnoNR = getVarPartAnoNRVariableYVarRyVariable(fechaInicioRenta, varAntRenova, varCriFec);
				 }else{
					 varPartAnoNR =  getVarPartAnoNRVariableYVarRyVariable(fechaHastaRenovacion, fechaInicioRenta, varCriFec);
				 } 
			 } else if (!varFecVcto.before(varAntRenova) && calFechaProxRenovacion.getAnio()== calFechaVencimiento.getAnio()) {
				 if (!varProxRenova.before(varfecJ)) {
					 varPartAnoNR = getVarPartAnoNRVariableYVarRyVariable(varProxRenova, varfecJ, varCriFec);
				 } else {
					 varPartAnoNR = getVarPartAnoNRVariableYVarRyVariable(varFecVcto, varfecJ, varCriFec);
				 }
			 } else {
				 varPartAnoNR = BigDecimal.ONE;
			 }
		}

		/**
		 * 		-	varRen= nanos (umic.fechas.fecinisus, varProxRenova, VarCriterFec)
				-	varRɣ =[(año(varAntRenova)*365+ddenero(varAntRenova, VarCriterFec)+dia(varAntRenova))-(año(varfecJ)*365+ddenero(varfecJ, VarCriterFec)+dia(varfecJ))]/365			
				-	varVVida(j) = VVIDA(varJ, varI1, varI2,varM,varTCɣ1)
				-	varVZRever(j)= VZREVER (varProyVZRever , periodoProyeccion , fcalc , umic,btcUmic,codSubproceso) 
				-	varCSP238(j)= CSP238 (varProyCsp238, periodoProyeccion , fcalc , umic,btcUmic,codSubproceso)
				-	varPUCCAPdifer = PUCCAPdifer (varPU, varI1Bti ,varI2Bti ,varNR ,varNRM ,varM,varBeta1 ,varBeta2, varDifercol ,varTCɣ1) 
				-	varObadorprim = OBADORPRIM(fcalc, umic.fechas.fecinisus, varAntRenova, varProxRenova,umic.rentas.fecini, umic.rentas.fecfin, varfecJ, varJ ,varPartAnoNR,varRɣ,varI1,varI2,varM, varN,VarTc0, VarCriterFec,varValoresTabMort,varW,varX,varZC, varɣ, varDifercol, VarBeta ,varBeta1 ,varBeta2,varI1Bti,varI2Bti);
				-	varGastivit = GASTIVIT (fcalc, umic.fechas.fecinisus, varAntRenova, varfecJ, varPartAnoNR, varRɣ, varRen, varI1, varI2,varM,varTCɣ1, VarCriterFec, varValoresTabMort, varW, varX);
				-	varGastgivitini = GASTGIVITINI(proyUmic, fcalc, umic, btcUmic, codSubproceso)
		 */
		
		
		if (varNR == 0){
			varRy = getVarPartAnoNRVariableYVarRyVariable(varfecJ, varFechaEfecto, varCriFec);
		}else{
			varRy = getVarPartAnoNRVariableYVarRyVariable(varfecJ, varAntRenova, varCriFec);
		}
		
		varRen = BigDecimal.valueOf(varTc0).add(vary).add(varPartAnoNR).subtract(varRy);
		
		varTcyPu = varTcy;
		
		if (ConstantsModulos.CTE_SR.equals(varBeta) || varModFall==null){
			varPUCCAPdifer = BigDecimal.ZERO;
		}else{
			//-	varPUCCAPdifer = PUCCAPdifer (varPU, varI1Bti ,varI2Bti ,varNR ,varNRM ,varM,varBeta1 ,varBeta2, varDifercol ,varTCɣ1) 
			varPUCCAPdifer = FuncionesVBX.puccapDifer(varPu, varI1bti, varI2bti, varNR, varNRM, varM, varBeta1, varBeta2, varDifercol, varTcyPu);
		}

		if (varPUCCAPdifer.equals(BigDecimal.ZERO) || varModFall==null){
			varObadorprim = BigDecimal.ZERO;
		}else{
			if (varfecJ.after(fechaInicioRenta)){
				varObadorprim = BigDecimal.ZERO;
			}else{
				lstTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriEdad, ConstantesSolvencia.CTE_TABMORT_L);
				//Llamada a obadorprim		
				varObadorprim = FuncionesPrimas.obadorprim(varfecJ, varFechaEfecto, varAntRenova, varProxRenova, 
						fechaInicioRenta, varFecVcto, varfecJ, varJ, varPartAnoNR,
						varRy, varI1, varI2, varM, varTc0, varCriFec, 
						lstTabMort,
						BigDecimal.valueOf(varW), varX, varZc, vary, varDifercol,
						varBeta, varBeta1, varBeta2, varN, varI1bti, varI2bti, varRen,
						varI1PorcentajeMasUno, varI2PorcentajeMasUno);
			}
		}
		
		if (!varGipc.equals(BigDecimal.ZERO)){
			if (varModProb.equals(ConstantsFactorias.MODULO_VZCIERTA)){
				lstTabMort = Util.listaProbUno(ConstantsFunciones.CTE_130);
			}else{
				lstTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriEdad, ConstantesSolvencia.CTE_TABMORT_L);
			}
			// Llamada a gastgivit
			varGastivit = FuncionesGastos.gastgivit(varfecJ, varFechaEfecto, varProxRenova, varfecJ,
					varPartAnoNR, varRy, varRen, varI1, varI2, varM, varTcy, varCriFec,
					lstTabMort, BigDecimal.valueOf(varW), varX, varZc,
					varI1PorcentajeMasUno, varI2PorcentajeMasUno);
			// Llamada a gastgivitini
			varGastgivitini = UtilModulos.getVarGastgivitini(mapVariables, CLAVE_MODULO,
					proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso,
					varI1PorcentajeMasUno, varI2PorcentajeMasUno);
		}
		
		BigDecimal varSumatorio = BigDecimal.ZERO;
		int iteracionBucleVBX362 = iteracion;
		
		int longitudLstDetalleCorriente = proyUmic.size();
		DetalleCorriente detalleActual = null;
		
		Map <String, Object> mapVariablesVZRever = new HashMap<String, Object>();
		//Se añaden las variables que no cambian entre invocaciones del VZREVER
		mapVariablesVZRever.put(ModuloVZREVER.CLAVE_VAR_VAL_TAB_MORT1, mapVariables.get(ModuloVZREVER.CLAVE_VAR_VAL_TAB_MORT1));
		mapVariablesVZRever.put(ModuloVZREVER.CLAVE_VAR_VAL_TAB_MORT2, mapVariables.get(ModuloVZREVER.CLAVE_VAR_VAL_TAB_MORT2));
		mapVariablesVZRever.put(ModuloVZREVER.CLAVE_VAR_EDAD_CAL1, mapVariables.get(ModuloVZREVER.CLAVE_VAR_EDAD_CAL1));
		mapVariablesVZRever.put(ModuloVZREVER.CLAVE_VAR_EDAD_CAL2, mapVariables.get(ModuloVZREVER.CLAVE_VAR_EDAD_CAL2));
		mapVariablesVZRever.put(ModuloVZREVER.CLAVE_PARENT_MODULE, CLAVE_MODULO);
		
		
		Map<Timestamp, BigDecimal> mapanAnnos;
		
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			mapanAnnos = new HashMap<Timestamp, BigDecimal>();
			mapVariablesVZRever.put(ModuloVZREVER.CLAVE_STOREN_ANNOS, Boolean.TRUE);
			mapVariables.put(CLAVE_MAPA_ANNOS, mapanAnnos);			
		} else {
			//Calcular el incremento de la fecha respecto a la de la primera iteración
			mapVariablesVZRever.put(ModuloVZREVER.CLAVE_INCR_FECHA, FuncionesAuxiliares.nAnnos(proyUmic.get(0).getFechaDesde(), proyUmic.get(iteracion - 1).getFechaDesde(), varCriFec));
			mapanAnnos = (Map<Timestamp, BigDecimal>)mapVariables.get(CLAVE_MAPA_ANNOS);
		}
		mapVariablesVZRever.put(ModuloVZREVER.CLAVE_MAPA_ANNOS, mapanAnnos);
		

		modProbable= FactoriaModulos.getModulo(varModProb);
		varTcyVida = varTcy;
		
		BigDecimal factorTC = BigDecimal.ZERO; 
		
		List<BigDecimal> listaPotencias = (List<BigDecimal>)mapVariables.get(CLAVE_LISTADO_POTENCIAS);
		int indicePotencias = 0;
		
		boolean calcularvVida = false;
		if(listaPotencias==null) {
			calcularvVida = true;
			listaPotencias = new ArrayList<BigDecimal>();	
			mapVariables.put(CLAVE_LISTADO_POTENCIAS, listaPotencias);
		} else {
			//  Se calcula el factorTC
			BigDecimal varTcyVidaAnt = (BigDecimal)mapVariables.get(CLAVE_VAR_TC);
			if(varTcyVida.compareTo(varM) <= 0) {
				//Si el Tc está por debajo del cambio de tramo se aplica el i1
				factorTC = Util.pow(varI1PorcentajeMasUno, varTcyVida.subtract(varTcyVidaAnt)); 
			} else {
				if(varTcyVidaAnt.compareTo(varM) >= 0) {
					//Si el Tc anterior está por encima del cambio de tramo se aplica el i2
					factorTC = Util.pow(varI2PorcentajeMasUno, varTcyVida.subtract(varTcyVidaAnt));
				} else {
					// Cambio de tramo de interés. Entra en juego el varM
					factorTC = Util.pow(varI1PorcentajeMasUno, varM.subtract(varTcyVidaAnt)).multiply(Util.pow(varI2PorcentajeMasUno, varTcyVida.subtract(varM)), ConstantsFunciones.MATH_CONTEXT);
				}
			}
			
			// Se actualiza la lista de potencias con el factor
			for(int i=0; i < listaPotencias.size(); i++) {
				listaPotencias.set(i, listaPotencias.get(i).multiply(factorTC, ConstantsFunciones.MATH_CONTEXT));							
			}
			
		}
		mapVariables.put(CLAVE_VAR_TC, varTcyVida);
				
		BloqueCorriente bloqueVida = null;
		for (int i = iteracion - 1 ; i < longitudLstDetalleCorriente; i++ ) {
			detalleActual = proyUmic.get(i);
			
			bloqueVida = detalleActual.getBloqueVida();
			varfecJ1 = bloqueVida.getFechaPago();
			
			if (null != varfecJ1) {
				// Sólo hacemos cuentas cuando varCSP238 no sea 0
				varCSP238 = proyUmic.get(iteracionBucleVBX362 - 1).getImpPago();
				if ( null != varCSP238 ) {
					if(calcularvVida) {
						varJ1 = FuncionesAuxiliares.nAnnos(varFechaEfecto, varfecJ1, varCriFec);
						varVVida = FuncionesActualizacionFinanciera.vVida(varJ1, varI1, varI2, varM, varTcyVida, varI1PorcentajeMasUno, varI2PorcentajeMasUno);
						varVVidaCSP238 = varVVida.multiply(varCSP238.setScale(2, RoundingMode.HALF_DOWN));
						listaPotencias.add(varVVidaCSP238);
					} else {
						varVVidaCSP238 = listaPotencias.get(indicePotencias);
						indicePotencias++;
					}
										 
					varVProb = (BigDecimal) modProbable.execute(proyUmic, bloqueVida, iteracionBucleVBX362, varfecJ, umic, btcUmic, mapVariablesVZRever, codSubproceso);					

					varSumatorio = varSumatorio.add(varVVidaCSP238.multiply(varVProb));
					
				}
			}
						
			iteracionBucleVBX362++;
			
		}
		
		//Se guardan las variables del VZREVER que quieren conservarse entre periodos del VBX362
		mapVariables.put(ModuloVZREVER.CLAVE_VAR_VAL_TAB_MORT1, mapVariablesVZRever.get(ModuloVZREVER.CLAVE_VAR_VAL_TAB_MORT1));
		mapVariables.put(ModuloVZREVER.CLAVE_VAR_VAL_TAB_MORT2, mapVariablesVZRever.get(ModuloVZREVER.CLAVE_VAR_VAL_TAB_MORT2));
		mapVariables.put(ModuloVZREVER.CLAVE_VAR_EDAD_CAL1, mapVariablesVZRever.get(ModuloVZREVER.CLAVE_VAR_EDAD_CAL1));
		mapVariables.put(ModuloVZREVER.CLAVE_VAR_EDAD_CAL2, mapVariablesVZRever.get(ModuloVZREVER.CLAVE_VAR_EDAD_CAL2));
		mapanAnnos.remove(varfecJ);
		
		
		// Se elimina el primer resultado si es que se calculó para el primer periodo
		if(proyUmic.get(iteracion-1).getBloqueVida().getFechaPago() != null) {
			if(!listaPotencias.isEmpty()) {
				listaPotencias.remove(0);
			}
		}

		primerOperando = varSumatorio.multiply(varGicPorcentajeMasUno);

		segundOperando = varPUCCAPdifer.multiply(varObadorprim);
		
		if (varGipcPorcentaje.signum()!=0){
			tercerOperando = varPu.multiply(varGipcPorcentaje).multiply(varGastivit.divide(varGastgivitini, ConstantsFunciones.MATH_CONTEXT));
		}

		
		vbx362 = primerOperando.add(segundOperando).add(tercerOperando);

		//LOG.warn(varfecJ.toString()+";"+varSumatorio+";"+varPUCCAPdifer+";"+varObadorprim+";"+vbx362+";"+varGastivit+";"+varGastgivitini+";");
		
		//LOG.warn(primerOperando+";"+segundOperando+";"+tercerOperando+";"+varSumatorio+";"+vbx362+";"+varRy+";"+vary+";"+varfecJ+";"+varAntRenova+";"+varProxRenova+";"+varPUCCAPdifer+";"+varObadorprim);
		
		
		/**
		 * Se calcula vbx362
		 * Y se retornarán también los terminales, ceros en este caso:
		 * 		terminalAnterior = 0
		 * 		terminalPosterior= 0
		 */
		salida.setProvbtiproy(vbx362);
		salida.setTerminalAnterior(BigDecimal.ZERO);
		salida.setTerminalPosterior(BigDecimal.ZERO);
		
		//FIN DE VALORES QUE SI CAMBIAN POR ITERACION
		
		if (ModuloVBX362.LOG.isTraceEnabled()) {
			ModuloVBX362.LOG.trace("Fin función << moduloVBX362 >> de la clase ModuloVBX362, para la iteracion = {}, con resultado vbx362 = {}", iteracion, vbx362);
		}
		
		return salida;
	}
	
	/**
	 * Función que realiza el calculo de las variables varPartAnoNR y VarRy, para aquellos valores que pueden variar en cada periodo
	 * 
	 * @param fecha1
	 * @param fecha2
	 * @param varCriFec
	 * @return
	 */
	private BigDecimal getVarPartAnoNRVariableYVarRyVariable(final Timestamp fecha1, final Timestamp fecha2, final String varCriFec){
		//Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		Integer operando1 = 0;
		Integer operando2 = 0;
		Fecha calFecha1 = UtilFechas.getFecha(fecha1);
		Fecha calFecha2 = UtilFechas.getFecha(fecha2);
		//Fin variables locales
		
		
		operando1 = calFecha1.getAnio() * ConstantsFunciones.CTE_365 + FuncionesVBX.ddEnero(fecha1, varCriFec) + calFecha1.getDia();
		operando2 = calFecha2.getAnio() * ConstantsFunciones.CTE_365 + FuncionesVBX.ddEnero(fecha2, varCriFec) + calFecha2.getDia();
		//resultado = BigDecimal.valueOf((operando1 - operando2)).divide(ConstantsFunciones.CTE_OPER_365, ConstantsFunciones.MATH_CONTEXT);
		resultado = BigDecimal.valueOf((operando1 - operando2)).multiply(ConstantsFunciones.CTE_OPER_1_PARTIDO_365);
		
		
		return resultado;
	}

}
