package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalFlujoProyeccion;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloPMRR02 implements Modulo {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloPMRR02.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_PMRR02;
	private static final String CLAVE_VAR_PROB_VIDA = ConstantsModulos.CTE_VAR_PROB_VIDA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PROB_FALL = ConstantsModulos.CTE_VAR_PROB_FALL.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PROB_GAST = ConstantsModulos.CTE_VAR_PROB_GAST.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PROB_COM = ConstantsModulos.CTE_VAR_PROB_COM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PROB_INV = ConstantsModulos.CTE_VAR_PROB_INV.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PROB_RESC = ConstantsModulos.CTE_VAR_PROB_RESC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PROB_PRIM = ConstantsModulos.CTE_VAR_PROB_PRIM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ACT_VIDA = ConstantsModulos.CTE_VAR_ACT_VIDA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ACT_FALL = ConstantsModulos.CTE_VAR_ACT_FALL.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ACT_GAST = ConstantsModulos.CTE_VAR_ACT_GAST.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ACT_COM = ConstantsModulos.CTE_VAR_ACT_COM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ACT_INV = ConstantsModulos.CTE_VAR_ACT_INV.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ACT_RESC = ConstantsModulos.CTE_VAR_ACT_RESC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ACT_PRIM = ConstantsModulos.CTE_VAR_ACT_PRIM.concat(CLAVE_MODULO);
	private static final String CLAVE_VXJ_VIDA = ConstantsModulos.CTE_VXJ_VIDA.concat(CLAVE_MODULO);
	private static final String CLAVE_VXJ_COM = ConstantsModulos.CTE_VXJ_COM.concat(CLAVE_MODULO);
	private static final String CLAVE_VXJ_INV = ConstantsModulos.CTE_VXJ_INV.concat(CLAVE_MODULO);
	private static final String CLAVE_VXJ_PRIM = ConstantsModulos.CTE_VXJ_PRIM.concat(CLAVE_MODULO);
	private static final String CLAVE_VXJ_GTO = ConstantsModulos.CTE_VXJ_GTO.concat(CLAVE_MODULO);
	private static final String CLAVE_VXJ_RTE = ConstantsModulos.CTE_VXJ_RTE.concat(CLAVE_MODULO);
	private static final String CLAVE_VXJ_FALL = ConstantsModulos.CTE_VXJ_FALL.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_INV_PROB = ConstantsModulos.CTE_VAR_INV_PROB.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ACT_FIN = ConstantsModulos.CTE_VAR_ACT_FIN.concat(CLAVE_MODULO);
	private static final String CLAVE_IMP_FLUJ_ACT_FALL = ConstantsModulos.CTE_IMP_FLUJ_ACT_FALL.concat(CLAVE_MODULO);
	private static final String CLAVE_IMP_FLUJ_ACT_GTO = ConstantsModulos.CTE_IMP_FLUJ_ACT_GTO.concat(CLAVE_MODULO);
	private static final String CLAVE_IMP_FLUJ_ACT_RESC = ConstantsModulos.CTE_IMP_FLUJ_ACT_RESC.concat(CLAVE_MODULO);
	private static final String CLAVE_IMP_FLUJ_ACT_COMPL = ConstantsModulos.CTE_IMP_FLUJ_ACT_COMPL.concat(CLAVE_MODULO);
	private static final String CLAVE_IMP_FLUJ_ACT_PRIM = ConstantsModulos.CTE_IMP_FLUJ_ACT_PRIM.concat(CLAVE_MODULO);
	private static final String CLAVE_IMP_FLUJ_ACT_COM = ConstantsModulos.CTE_IMP_FLUJ_ACT_COM.concat(CLAVE_MODULO);
	private static final String CLAVE_IMP_FLUJ_ACT_VIDA = ConstantsModulos.CTE_IMP_FLUJ_ACT_VIDA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PROB_VIDA_PPAL = ConstantsModulos.CTE_VAR_PROB_VIDA.concat(CLAVE_MODULO).concat("PPAL");
	private static final String CLAVE_VAR_PROB_FALL_PPAL = ConstantsModulos.CTE_VAR_PROB_FALL.concat(CLAVE_MODULO).concat("PPAL");
	
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/**
	 * Módulo que calcula el importe nominal para la proyección
	 * de Provisión Matemática Recurrente 
	 *  */
	
	@Override
	public Object execute(Object... args) throws Solvencia2Excepcion {
		
		//Variables locales
		TotalFlujoProyeccion resultado;
		//Fin variables locales
		
		try {
			if (ModuloPMRR02.LOG.isTraceEnabled()) {
				ModuloPMRR02.LOG.trace("Inicio de execute en clase ModuloPMRR03");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloPMRR01
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.
			
			//Se invoca a la función de calculo PMRR01
			resultado = moduloPMRR02(proyUmic, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloPMRR02.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloPMRR02.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloPMRR02.LOG.isTraceEnabled()) {
			ModuloPMRR02.LOG.trace("Fin de execute en clase ModuloPMRR01");
		}
		
		return resultado;
		
	}

	/**
	 * Módulo que calcula el importe nominal para la proyección
	 * de Provisión Matemática por Fórmula Cerrada
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
	
	
	private TotalFlujoProyeccion moduloPMRR02(List<DetalleCorriente> proyUmic,
			int iteracion, Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, Map<String, Object> mapVariables,
			String codSubproceso) {

		//Variables locales
		TotalFlujoProyeccion salida = new TotalFlujoProyeccion();
		String varProbVida, varProbFall, varProbGast, varProbCom, varProbInv, varProbResc, varProbPrim, varActVida, varActFall, varActGast, varActCom,varActInv,varActResc,varActPrim;
		String varModProb,varModProbFall;
		String varModAct,varModProbNomFall,varModProbNomInv;
		String varModAnu = "";
		BigDecimal varterminalAnterior = BigDecimal.ZERO;
		BigDecimal terminalPosterior = BigDecimal.ZERO;
		int varCalcProbVida = 1,varCalcProbFall = 1,varCalcProbGast = 1,varCalcProbComi = 1,varCalcProbResc = 1, varCalcProbPrim = 1, varCalcProbInv = 1;
		int varCalcActVida,varCalcActFall,varCalcActGast,varCalcActComi,varCalcActResc,varCalcActPrim,varCalcActInv;
		List<DetalleCorriente> varProyCopia;
		List<DetalleCorriente> varProyPeriodo;
		BigDecimal varActFlujoVida = BigDecimal.ZERO;
		BigDecimal varActFlujoFall = BigDecimal.ZERO;
		BigDecimal varActFlujoGast = BigDecimal.ZERO;
		BigDecimal varActFlujoComi = BigDecimal.ZERO;
		BigDecimal varActFlujoResc = BigDecimal.ZERO;
		BigDecimal varActFlujoInv = BigDecimal.ZERO;
		BigDecimal varActFlujoPrim = BigDecimal.ZERO;
		Modulo actVida,actFall,actGast,actComi,actResc,actInv,actPrim;
		BigDecimal Vxj;
		BigDecimal VxjantVida;
		BigDecimal VxjantFall;
		BigDecimal VxjantGto;
		BigDecimal VxjantCom;
		BigDecimal VxjantRte;
		BigDecimal VxjantPrim;
		BigDecimal VxjantInv;
		BigDecimal VxjVida;
		BigDecimal VxjFall;
		BigDecimal VxjGto;
		BigDecimal VxjCom;
		BigDecimal VxjRte;
		BigDecimal VxjPrim;
		BigDecimal VxjInv;
		BigDecimal varProbPeriodo;
		BigDecimal varAnuPeriodo;
		BigDecimal varInvProbj;
		Modulo probVida,probFall,probComi, probResc,probGast,probInv,probPrim;
		Modulo modProb, modProbFall, modAnu;
		Timestamp varFecPago;
		BigDecimal varActPeriodo;
		BigDecimal varActFinj = null;
		Timestamp varFecDevengoAux;
		Timestamp varFecPagoAux;
		String varProbVidaPPAL;
		final IObtenerDatos obtenerDatos = FachadaServicios.getObtenerDatos();
		Timestamp varFechaPagoAux = null;
		
		//Fin variables locales
		if (ModuloPMRR02.LOG.isTraceEnabled()) {
			ModuloPMRR02.LOG.trace("Inicio función << moduloPMRR02 >> de la clase ModuloBXul001, para la  iteracion = {}", iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, fcalc, umic, btcUmic);
		
		//Variables de modulo
		varProyCopia = proyUmic;
		varProyPeriodo = proyUmic;
		
		varProbVida = UtilModulos.getVarModulo(mapVariables,CLAVE_VAR_PROB_VIDA,umic, btcUmic, ConstantsModulos.CTE_PROY_VIDA, "02");
		varProbFall = UtilModulos.getVarModulo(mapVariables,CLAVE_VAR_PROB_FALL,umic, btcUmic, ConstantsModulos.CTE_PROY_FALL, "02");
		varProbGast = UtilModulos.getVarModulo(mapVariables,CLAVE_VAR_PROB_GAST,umic, btcUmic, ConstantsModulos.CTE_PROY_GTOS, "02");
		varProbCom = UtilModulos.getVarModulo(mapVariables,CLAVE_VAR_PROB_COM,umic, btcUmic, ConstantsModulos.CTE_PROY_COMI, "02");
		varProbInv = UtilModulos.getVarModulo(mapVariables,CLAVE_VAR_PROB_INV,umic, btcUmic, ConstantsModulos.CTE_PROY_COMP, "02");
		varProbResc = UtilModulos.getVarModulo(mapVariables,CLAVE_VAR_PROB_RESC,umic, btcUmic, ConstantsModulos.CTE_PROY_RESC, "02");
		varProbPrim = UtilModulos.getVarModulo(mapVariables,CLAVE_VAR_PROB_PRIM,umic, btcUmic, ConstantsModulos.CTE_PROY_PRIMA, "02");
		varActVida = UtilModulos.getVarModulo(mapVariables,CLAVE_VAR_ACT_VIDA,umic, btcUmic, ConstantsModulos.CTE_PROY_VIDA, "04");
		varActFall = UtilModulos.getVarModulo(mapVariables,CLAVE_VAR_ACT_FALL,umic, btcUmic, ConstantsModulos.CTE_PROY_FALL, "04");
		varActGast = UtilModulos.getVarModulo(mapVariables,CLAVE_VAR_ACT_GAST,umic, btcUmic, ConstantsModulos.CTE_PROY_GTOS, "04");
		varActCom = UtilModulos.getVarModulo(mapVariables,CLAVE_VAR_ACT_COM,umic, btcUmic, ConstantsModulos.CTE_PROY_COMI, "04");
		varActInv = UtilModulos.getVarModulo(mapVariables,CLAVE_VAR_ACT_INV,umic, btcUmic, ConstantsModulos.CTE_PROY_COMP, "04");
		varActResc = UtilModulos.getVarModulo(mapVariables,CLAVE_VAR_ACT_RESC,umic, btcUmic, ConstantsModulos.CTE_PROY_RESC, "04");
		varActPrim = UtilModulos.getVarModulo(mapVariables,CLAVE_VAR_ACT_PRIM,umic, btcUmic, ConstantsModulos.CTE_PROY_PRIMA, "04");
		
		mapVariables.put("VZCREVER_FCALC", fcalc);
		
		UmicKey umicPrincipal = obtenerDatos.recuperarUmicPrincipalPMRR02(umic.getDatosGenerales().getKmodalidad(),
				umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
				umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(),
				umic.getDatosGenerales().getCtipoaport(), umic.getDatosGenerales().getKprestacion(), umic.getDatosGenerales().getNorden());
		Umic umicTitular = obtenerDatos.recuperarUmic(umicPrincipal);
		
		varProbVidaPPAL = UtilModulos.getVarModulo(mapVariables,CLAVE_VAR_PROB_VIDA_PPAL,umicTitular, btcUmic, ConstantsModulos.CTE_PROY_VIDA, "02");
		if (varProbVidaPPAL == null) {
			varProbFall = UtilModulos.getVarModulo(mapVariables,CLAVE_VAR_PROB_FALL_PPAL,umic, btcUmic, ConstantsModulos.CTE_PROY_FALL, "02");
			if (varProbFall == null) {
				varModProb = ConstantsFactorias.MODULO_VZC;
				mapVariables.put("VZCREVER_MODULO_FALL", ConstantsFactorias.MODULO_VZC);	
			} else {
				if (varProbFall == ConstantsFactorias.MODULO_FPTOZC) {
					varModProb = ConstantsFactorias.MODULO_VZC;
				} else if (varProbFall == ConstantsFactorias.MODULO_FPTODIFER){
					varModProb = ConstantsFactorias.MODULO_VZREVER2;
				} else if (varProbFall == ConstantsFactorias.MODULO_FPTOX2Y1) {
					varModProb = ConstantsFactorias.MODULO_VZC2;
				} else {
					varModProb = ConstantsFactorias.MODULO_VZC;
				}
				mapVariables.put("VZCREVER_MODULO_FALL", varProbFall);	
			}
		} else {
			varModProb = varProbVidaPPAL;
			varProbFall = UtilModulos.getVarModulo(mapVariables,CLAVE_VAR_PROB_FALL_PPAL,umic, btcUmic, ConstantsModulos.CTE_PROY_FALL, "02");
			mapVariables.put("VZCREVER_MODULO_FALL", varModProb);
			mapVariables.put("VZCREVER_MODULO_FALL_228", varProbFall);
		}

		varModProbFall = ConstantsFactorias.MODULO_FPTOZC;
		varModAct = ConstantsFactorias.MODULO_ACT001;
		
		
		varModProbNomFall = ConstantsFactorias.MODULO_FZC;
		varModProbNomInv = ConstantsFactorias.MODULO_COMPLPTOZC;
		
		if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL) || 
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA) ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR) ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTIU) ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTID) ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRGTO) ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMFE) ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMMI) ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCF) ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCI) ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLFE) ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLMI) ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC) ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRVM)  ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEP) ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEN) ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIP) ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIN) ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM)){
			varModAct = ConstantsFactorias.MODULO_ACTBEL2;
			varModAnu = ConstantsFactorias.MODULO_ATC;
		}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17) || btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIF17LIR)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17LIRIN)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17CLIR)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17IF) || btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIFF17OCI)){
			varModAct = ConstantsFactorias.MODULO_ACTNIIF17;
			varModAnu = ConstantsFactorias.MODULO_ATC;
		}
		
		if(varProbVida == null){ varCalcProbVida = 0; }
		if(varProbFall == null){ varCalcProbFall = 0; }
		if(varProbGast == null){ varCalcProbGast = 0; }
		if(varProbCom == null){ varCalcProbComi = 0; }
		if(varProbResc == null){ varCalcProbResc = 0; }
		if(varProbPrim == null){ varCalcProbPrim = 0; }
		if(varProbInv == null){ varCalcProbInv = 0; }
		
		if(varActVida == null){ varCalcActVida = 0; }
		if(varActFall == null){ varCalcActFall = 0; }
		if(varActGast == null){ varCalcActGast = 0; }
		if(varActCom == null){ varCalcActComi = 0; }
		if(varActResc == null){ varCalcActResc = 0; }
		if(varActPrim == null){ varCalcActPrim = 0; }
		if(varActInv == null){ varCalcActInv = 0; }
		
		Timestamp varFechaDevengoAuxCopia = null;
		Timestamp varFechaPagoAuxCopia = null;
		BigDecimal varProbPeriodoVida = BigDecimal.ZERO;
		BigDecimal varProbPeriodoFall = BigDecimal.ZERO;
		BigDecimal varProbPeriodoGast = BigDecimal.ZERO;
		BigDecimal varProbPeriodoPrim = BigDecimal.ZERO;
		BigDecimal impActVidaAux = BigDecimal.ZERO,impActFallAux = BigDecimal.ZERO,impActComiAux = BigDecimal.ZERO,impActComplAux = BigDecimal.ZERO,impActRteAux = BigDecimal.ZERO, impActGtoAux = BigDecimal.ZERO,impActPrimAux = BigDecimal.ZERO;
		BigDecimal auxVida = BigDecimal.ZERO, auxFall = BigDecimal.ZERO, auxGto = BigDecimal.ZERO, auxResc = BigDecimal.ZERO, auxCompl = BigDecimal.ZERO, auxPrim = BigDecimal.ZERO, auxCom = BigDecimal.ZERO;

		if (varModProb.equals(ConstantsFactorias.MODULO_VZREVER) 
				|| varModProb.equals(ConstantsFactorias.MODULO_VRTAVIT)
				|| varModProb.equals(ConstantsFactorias.MODULO_VRTAMIX)) {
			varModProb = ConstantsFactorias.MODULO_VZREVER2;
			probVida = FactoriaModulos.getModulo(varModProb);
			BigDecimal pmrrVida = BigDecimal.ZERO;
			BigDecimal pmrrFall = BigDecimal.ZERO;
			BigDecimal pmrrGast = BigDecimal.ZERO;
			BigDecimal pmrrComi = BigDecimal.ZERO;
			BigDecimal pmrrResc = BigDecimal.ZERO;
			BigDecimal pmrrInv = BigDecimal.ZERO;
			BigDecimal pmrrPrim = BigDecimal.ZERO;
			if(varCalcProbVida == ConstantsFunciones.CTE_1){
				pmrrVida = (BigDecimal) probVida.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueVida(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_VIDA);
			}
			if(varCalcProbFall == ConstantsFunciones.CTE_1){
				pmrrFall = (BigDecimal) probVida.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueFall(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_FALL);
			}
			if(varCalcProbGast == ConstantsFunciones.CTE_1){
				pmrrGast = (BigDecimal) probVida.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueGto(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_GTOS);
			}
			if(varCalcProbComi == ConstantsFunciones.CTE_1){
				pmrrComi = (BigDecimal) probVida.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueComi(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_COMI);
			}
			if(varCalcProbResc == ConstantsFunciones.CTE_1){
				pmrrResc = (BigDecimal) probVida.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueRte(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_RESC);
			}
			if(varCalcProbInv == ConstantsFunciones.CTE_1){
				pmrrInv = (BigDecimal) probVida.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueCompl(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_COMP);
			}
			if(varCalcProbPrim == ConstantsFunciones.CTE_1){
				pmrrPrim = (BigDecimal) probVida.execute(varProyCopia, proyUmic.get(iteracion-1).getBloquePrim(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_PRIMA);
			}
			
			BigDecimal pmrr = pmrrVida.add(pmrrFall).add(pmrrGast).add(pmrrComi).add(pmrrResc).add(pmrrInv).subtract(pmrrPrim);
			pmrr = pmrr.multiply(umic.getDatosCoaseguro().getPcoaseg().divide(ConstantsFunciones.CTE_OPER_100, ConstantsFunciones.MATH_CONTEXT));
			salida.setProvbtiproy(pmrr.setScale(ConstantsFunciones.CTE_2, RoundingMode.HALF_DOWN));

			return salida;
		}
		
		if(varCalcProbVida == ConstantsFunciones.CTE_1){
			varFechaDevengoAuxCopia = varProyPeriodo.get(iteracion-1).getBloqueVida().getFechaDevengo();
			varFechaPagoAuxCopia = varProyPeriodo.get(iteracion-1).getBloqueVida().getFechaPago();
			
			if(null != varProyPeriodo.get(iteracion-1).getBloqueVida().getImpFlujoActualizado()){
				impActVidaAux = varProyPeriodo.get(iteracion-1).getBloqueVida().getImpFlujoActualizado();
			}
			
			probVida = FactoriaModulos.getModulo(varProbVida);
			if (varProbVida.equals(ConstantsFactorias.MODULO_VZREVER2)) {
				varProbPeriodoVida = (BigDecimal) probVida.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueVida(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_VIDA);
			} else {
				varProbPeriodoVida = (BigDecimal) probVida.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueVida(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, codSubproceso);
			}
			
			actVida = FactoriaModulos.getModulo(varActVida);
			if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17) || btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIF17LIR)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17LIRIN)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17CLIR)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17IF) || btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIFF17OCI)) {
				
				varActFlujoVida = (BigDecimal) actVida.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueVida(),iteracion,fcalc, umic, btcUmic, mapVariables, codSubproceso + ConstantsModulos.CTE_PROY_VIDA);
				
			} else {
				varActFlujoVida = (BigDecimal) actVida.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueVida(),iteracion,proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, codSubproceso);
			}
			
			if(null != varProyCopia.get(iteracion-1).getBloqueVida().getImpFlujoNominal()){
				varProyCopia.get(iteracion-1).getBloqueVida().setImpFlujoActualizado(varProyCopia.get(iteracion-1).getBloqueVida().getImpFlujoNominal().multiply(varProbPeriodoVida).multiply(varActFlujoVida));
			}else {
				varProyCopia.get(iteracion-1).getBloqueVida().setImpFlujoActualizado(BigDecimal.ZERO);
			}
			
			//Guardo el valor del Importe del flujo actualizado para poder que en la procima iteracion se pueda utilizar, sin machacar su valor al final de esta iteracion.
			if(null != varProyCopia.get(iteracion-1).getBloqueVida().getImpFlujoActualizado()){
				auxVida = varProyCopia.get(iteracion-1).getBloqueVida().getImpFlujoActualizado();
			}
			
			if ((varModProb.equals(ConstantsFactorias.MODULO_VARBVIT) 
					|| varModProb.equals(ConstantsFactorias.MODULO_VARB167) 
					|| varModProb.equals(ConstantsFactorias.MODULO_VARBFPC))
					&& umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_2_STRING)) {
				String auxvarModProb = ConstantsFactorias.MODULO_VZREVER2;
				probVida = FactoriaModulos.getModulo(auxvarModProb);
				BigDecimal pmrrVida = (BigDecimal) probVida.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueVida(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_VIDA);
				BigDecimal auxVzrever2 = (BigDecimal) mapVariables.get("VZCREVER_VYANT" + ConstantsModulos.CTE_PROY_VIDA);
				auxVida = auxVzrever2;
			}
			
			if (varModProb.equals(ConstantsFactorias.MODULO_VZC412)) {
				String auxvarModProb = ConstantsFactorias.MODULO_VZREVER2;
				mapVariables.put("VZREVER2_PMRR", "VZREVER2_PMRR");
				probVida = FactoriaModulos.getModulo(auxvarModProb);
				BigDecimal pmrrVida = (BigDecimal) probVida.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueVida(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_VIDA);
				BigDecimal auxVzrever2 = (BigDecimal) mapVariables.get("VZCREVER_VYANT" + ConstantsModulos.CTE_PROY_VIDA);
				auxVida = auxVzrever2;
			}
			
			mapVariables.put(CLAVE_IMP_FLUJ_ACT_VIDA, auxVida);
			
			//Restablezco las fechas iniciales del proyUmic que se habian sobreescrito al principio de esta corriente.
			proyUmic.get(iteracion-1).getBloqueVida().setFechaDevengo(varFechaDevengoAuxCopia);
			proyUmic.get(iteracion-1).getBloqueVida().setFechaPago(varFechaPagoAuxCopia);
			
		}
		
		if(varCalcProbFall == ConstantsFunciones.CTE_1){
			
			varFechaDevengoAuxCopia = varProyPeriodo.get(iteracion-1).getBloqueFall().getFechaDevengo();
			varFechaPagoAuxCopia = varProyPeriodo.get(iteracion-1).getBloqueFall().getFechaPago();
			
			if(null != varProyPeriodo.get(iteracion-1).getBloqueFall().getImpFlujoActualizado()){
				impActFallAux = varProyPeriodo.get(iteracion-1).getBloqueFall().getImpFlujoActualizado();
			}
			
			proyUmic.get(iteracion-1).getBloqueFall().setFechaPago(proyUmic.get(iteracion-1).getFechaHasta());
			proyUmic.get(iteracion-1).getBloqueFall().setFechaDevengo(proyUmic.get(iteracion-1).getFechaHasta());

			probFall = FactoriaModulos.getModulo(varProbFall); 
			
			if (varModProbNomFall.equals(ConstantsFactorias.MODULO_VZREVER2)) {
				varProbPeriodoFall = (BigDecimal) probFall.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueFall(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic,mapVariables,ConstantsModulos.CTE_PROY_FALL);
			} else {
				varProbPeriodoFall = (BigDecimal) probFall.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueFall(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic,mapVariables,codSubproceso);
			}
			
			proyUmic.get(iteracion-1).getBloqueFall().setFechaPago(varFechaPagoAuxCopia);
			proyUmic.get(iteracion-1).getBloqueFall().setFechaDevengo(varFechaDevengoAuxCopia);
				
			actFall = FactoriaModulos.getModulo(varActFall);
			varFechaPagoAux = proyUmic.get(iteracion-1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_FALL).getFechaPago();
			
			if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17) || btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIF17LIR)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17LIRIN)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17CLIR)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17IF) || btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIFF17OCI)) {
				
				varActFlujoFall = (BigDecimal) actFall.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueFall(),iteracion,fcalc, umic, btcUmic, mapVariables, codSubproceso + ConstantsModulos.CTE_PROY_FALL);
				
			} else {
				varActFlujoFall = (BigDecimal) actFall.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueFall(),iteracion,proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, codSubproceso);
			}
			proyUmic.get(iteracion-1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_FALL).setFechaPago(varFechaPagoAux);
			
			if(null != varProyCopia.get(iteracion-1).getBloqueFall().getImpFlujoNominal()){
				varProyCopia.get(iteracion-1).getBloqueFall().setImpFlujoActualizado(varProyCopia.get(iteracion-1).getBloqueFall().getImpFlujoNominal().multiply(varProbPeriodoFall).multiply(varActFlujoFall));
			}else{
				varProyCopia.get(iteracion-1).getBloqueFall().setImpFlujoActualizado(BigDecimal.ZERO);
			}
			
			if(null != varProyCopia.get(iteracion-1).getBloqueFall().getImpFlujoActualizado()){
				auxFall = varProyCopia.get(iteracion-1).getBloqueFall().getImpFlujoActualizado();
			}
			
			if ((varModProb.equals(ConstantsFactorias.MODULO_VARBVIT) || varModProb.equals(ConstantsFactorias.MODULO_VARB167) || varModProb.equals(ConstantsFactorias.MODULO_VARBFPC))
					&& umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_2_STRING)) {
				String auxvarModProb = ConstantsFactorias.MODULO_VZREVER2;
				probFall = FactoriaModulos.getModulo(auxvarModProb);
				BigDecimal pmrrFall = (BigDecimal) probFall.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueFall(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_FALL);
				BigDecimal auxVzrever2 = (BigDecimal) mapVariables.get("VZCREVER_VYANT" + ConstantsModulos.CTE_PROY_FALL);
				auxFall = auxVzrever2;
			}
			
			if (varModProb.equals(ConstantsFactorias.MODULO_VZC412)) {
				String auxvarModProb = ConstantsFactorias.MODULO_VZREVER2;
				probFall = FactoriaModulos.getModulo(auxvarModProb);
				BigDecimal pmrrFall = (BigDecimal) probFall.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueFall(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_FALL);
				BigDecimal auxVzrever2 = (BigDecimal) mapVariables.get("VZCREVER_VYANT" + ConstantsModulos.CTE_PROY_FALL);
				auxFall = auxVzrever2;
			}
			
			mapVariables.put(CLAVE_IMP_FLUJ_ACT_FALL, auxFall);
			
			proyUmic.get(iteracion-1).getBloqueFall().setFechaDevengo(varFechaDevengoAuxCopia);
			proyUmic.get(iteracion-1).getBloqueFall().setFechaPago(varFechaPagoAuxCopia);

		}
		
		if(varCalcProbGast == ConstantsFunciones.CTE_1){
			
			varFechaDevengoAuxCopia = varProyPeriodo.get(iteracion-1).getBloqueGto().getFechaDevengo();
			varFechaPagoAuxCopia = varProyPeriodo.get(iteracion-1).getBloqueGto().getFechaPago();
			
			if(null != varProyPeriodo.get(iteracion-1).getBloqueGto().getImpFlujoActualizado()){
				impActGtoAux = varProyPeriodo.get(iteracion-1).getBloqueGto().getImpFlujoActualizado();
			}
			
			probGast = FactoriaModulos.getModulo(varProbGast); 
			
			if (varProbGast.equals(ConstantsFactorias.MODULO_VZREVER2)) {
				varProbPeriodoGast = (BigDecimal) probGast.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueGto(), iteracion,proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_GTOS);
			} else {
				varProbPeriodoGast = (BigDecimal) probGast.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueGto(), iteracion,proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, codSubproceso);
			}

			actGast = FactoriaModulos.getModulo(varActGast);
			varFechaPagoAux = proyUmic.get(iteracion-1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_GTOS).getFechaPago();
			proyUmic.get(iteracion-1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_GTOS).setFechaPago(proyUmic.get(iteracion-1).getFechaHasta());
			if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17) || btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIF17LIR)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17LIRIN)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17CLIR)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17IF) || btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIFF17OCI)) {

				varActFlujoGast = (BigDecimal) actGast.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueGto(),iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso + ConstantsModulos.CTE_PROY_GTOS);
				
			} else {
				varActFlujoGast = (BigDecimal) actGast.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueGto(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, codSubproceso);
			}
			proyUmic.get(iteracion-1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_GTOS).setFechaPago(varFechaPagoAux);
			
			if (null != varProyCopia.get(iteracion - 1).getBloqueGto().getImpFlujoNominal()) {
				varProyCopia.get(iteracion - 1).getBloqueGto().setImpFlujoActualizado(varProyCopia.get(iteracion - 1)
						.getBloqueGto().getImpFlujoNominal().multiply(varProbPeriodoGast).multiply(varActFlujoGast));
			} else {
				varProyCopia.get(iteracion - 1).getBloqueGto().setImpFlujoActualizado(BigDecimal.ZERO);
			}
			if(null != varProyCopia.get(iteracion-1).getBloqueGto().getImpFlujoActualizado()){
				auxGto = varProyCopia.get(iteracion-1).getBloqueGto().getImpFlujoActualizado();
			}
			
			if ((varModProb.equals(ConstantsFactorias.MODULO_VARBVIT) || varModProb.equals(ConstantsFactorias.MODULO_VARB167) || varModProb.equals(ConstantsFactorias.MODULO_VARBFPC))
					&& umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_2_STRING)) {
				String auxvarModProb = ConstantsFactorias.MODULO_VZREVER2;
				probGast = FactoriaModulos.getModulo(auxvarModProb);
				BigDecimal pmrrGto = (BigDecimal) probGast.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueGto(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_GTOS);
				BigDecimal auxVzrever2 = (BigDecimal) mapVariables.get("VZCREVER_VYANT" + ConstantsModulos.CTE_PROY_GTOS);
				auxFall = auxVzrever2;
			}
			
			if (varModProb.equals(ConstantsFactorias.MODULO_VZC412)) {
				String auxvarModProb = ConstantsFactorias.MODULO_VZREVER2;
				probGast = FactoriaModulos.getModulo(auxvarModProb);
				BigDecimal pmrrGto = (BigDecimal) probGast.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueGto(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_GTOS);
				BigDecimal auxVzrever2 = (BigDecimal) mapVariables.get("VZCREVER_VYANT" + ConstantsModulos.CTE_PROY_GTOS);
				auxGto = auxVzrever2;
			}
			
			mapVariables.put(CLAVE_IMP_FLUJ_ACT_GTO ,auxGto);
			
			proyUmic.get(iteracion-1).getBloqueGto().setFechaDevengo(varFechaDevengoAuxCopia);
			proyUmic.get(iteracion-1).getBloqueGto().setFechaPago(varFechaPagoAuxCopia);
			
		}
		
		BigDecimal varProbPeriodoComi = BigDecimal.ZERO;
		if(varCalcProbComi == ConstantsFunciones.CTE_1){
			varFechaDevengoAuxCopia = varProyPeriodo.get(iteracion-1).getBloqueComi().getFechaDevengo();
			varFechaPagoAuxCopia = varProyPeriodo.get(iteracion-1).getBloqueComi().getFechaPago();
			
			if(null != varProyPeriodo.get(iteracion-1).getBloqueComi().getImpFlujoActualizado()){
				impActComiAux = varProyPeriodo.get(iteracion-1).getBloqueComi().getImpFlujoActualizado();
			}
			
			probComi = FactoriaModulos.getModulo(varProbCom); 
			if (varProbCom.equals(ConstantsFactorias.MODULO_VZREVER2)) {
				varProbPeriodoComi = (BigDecimal) probComi.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueComi(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_COMI);
			} else {
				varProbPeriodoComi = (BigDecimal) probComi.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueComi(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, codSubproceso);
			}
			
			actComi = FactoriaModulos.getModulo(varActCom);
			varFechaPagoAux = proyUmic.get(iteracion-1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_COMI).getFechaPago();
			proyUmic.get(iteracion-1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_COMI).setFechaPago(proyUmic.get(iteracion-1).getFechaHasta());
			
			if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17) || btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIF17LIR)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17LIRIN)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17CLIR)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17IF) || btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIFF17OCI)) {
				
				varActFlujoComi = (BigDecimal) actComi.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueComi(),iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso + ConstantsModulos.CTE_PROY_COMI);
				
			} else {
				varActFlujoComi = (BigDecimal) actComi.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueComi(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, codSubproceso);
			}
			
			proyUmic.get(iteracion-1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_COMI).setFechaPago(varFechaPagoAux);
			
			if(null != varProyCopia.get(iteracion-1).getBloqueComi().getImpFlujoNominal()){
				varProyCopia.get(iteracion-1).getBloqueComi().setImpFlujoActualizado(varProyCopia.get(iteracion-1).getBloqueComi().getImpFlujoNominal().multiply(varProbPeriodoComi).multiply(varActFlujoComi));
			}else{
				varProyCopia.get(iteracion-1).getBloqueComi().setImpFlujoActualizado(BigDecimal.ZERO);
			}
			
			if(null != varProyCopia.get(iteracion-1).getBloqueComi().getImpFlujoActualizado()){
				auxCom = varProyCopia.get(iteracion-1).getBloqueComi().getImpFlujoActualizado();
			}
			
			if ((varModProb.equals(ConstantsFactorias.MODULO_VARBVIT) || varModProb.equals(ConstantsFactorias.MODULO_VARB167) || varModProb.equals(ConstantsFactorias.MODULO_VARBFPC))
					&& umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_2_STRING)) {
				String auxvarModProb = ConstantsFactorias.MODULO_VZREVER2;
				probComi = FactoriaModulos.getModulo(auxvarModProb);
				BigDecimal pmrrComi = (BigDecimal) probComi.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueComi(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_COMI);
				BigDecimal auxVzrever2 = (BigDecimal) mapVariables.get("VZCREVER_VYANT" + ConstantsModulos.CTE_PROY_COMI);
				auxFall = auxVzrever2;
			}
			
			if (varModProb.equals(ConstantsFactorias.MODULO_VZC412)) {
				String auxvarModProb = ConstantsFactorias.MODULO_VZREVER2;
				probComi = FactoriaModulos.getModulo(auxvarModProb);
				BigDecimal pmrrComi = (BigDecimal) probComi.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueComi(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_COMI);
				BigDecimal auxVzrever2 = (BigDecimal) mapVariables.get("VZCREVER_VYANT" + ConstantsModulos.CTE_PROY_COMI);
				auxCom = auxVzrever2;
			}
			
			mapVariables.put(CLAVE_IMP_FLUJ_ACT_COM ,auxCom);
			proyUmic.get(iteracion-1).getBloqueComi().setFechaDevengo(varFechaDevengoAuxCopia);
			proyUmic.get(iteracion-1).getBloqueComi().setFechaPago(varFechaPagoAuxCopia);
			
		}
		
		BigDecimal varProbPeriodoResc = BigDecimal.ZERO;
		if(varCalcProbResc == ConstantsFunciones.CTE_1){
			varFechaDevengoAuxCopia = varProyPeriodo.get(iteracion-1).getBloqueRte().getFechaDevengo();
			varFechaPagoAuxCopia = varProyPeriodo.get(iteracion-1).getBloqueRte().getFechaPago();
			
			if(null != varProyPeriodo.get(iteracion-1).getBloqueRte().getImpFlujoActualizado()){
				impActRteAux = varProyPeriodo.get(iteracion-1).getBloqueRte().getImpFlujoActualizado();
			}	
			
			probResc = FactoriaModulos.getModulo(varProbResc); 
			if (varProbResc.equals(ConstantsFactorias.MODULO_VZREVER2)) {
				varProbPeriodoResc = (BigDecimal) probResc.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueRte(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_RESC);
			} else {
				varProbPeriodoResc = (BigDecimal) probResc.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueRte(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, codSubproceso);
			}
			
			actResc = FactoriaModulos.getModulo(varActResc);
			varFechaPagoAux = proyUmic.get(iteracion-1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_RESC).getFechaPago();
			proyUmic.get(iteracion-1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_RESC).setFechaPago(proyUmic.get(iteracion-1).getFechaHasta());
			
			if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17) || btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIF17LIR)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17LIRIN)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17CLIR)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17IF) || btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIFF17OCI)) {
				
				varActFlujoResc = (BigDecimal) actResc.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueRte(), iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso + ConstantsModulos.CTE_PROY_RESC);
				
			} else {
				varActFlujoResc = (BigDecimal) actResc.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueRte(), iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, codSubproceso);
			}
			
			proyUmic.get(iteracion-1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_RESC).setFechaPago(varFechaPagoAux);
			
			if(null != varProyCopia.get(iteracion-1).getBloqueRte().getImpFlujoNominal()){
				varProyCopia.get(iteracion-1).getBloqueRte().setImpFlujoActualizado(varProyCopia.get(iteracion-1).getBloqueRte().getImpFlujoNominal().multiply(varProbPeriodoResc).multiply(varActFlujoResc));
			}else{
				varProyCopia.get(iteracion-1).getBloqueRte().setImpFlujoActualizado(BigDecimal.ZERO);
			}
			
			if(null != varProyPeriodo.get(iteracion-1).getBloqueRte().getImpFlujoActualizado()){
				auxResc = varProyCopia.get(iteracion-1).getBloqueRte().getImpFlujoActualizado();
			}
			
			if ((varModProb.equals(ConstantsFactorias.MODULO_VARBVIT) || varModProb.equals(ConstantsFactorias.MODULO_VARB167) || varModProb.equals(ConstantsFactorias.MODULO_VARBFPC))
					&& umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_2_STRING)) {
				String auxvarModProb = ConstantsFactorias.MODULO_VZREVER2;
				probResc = FactoriaModulos.getModulo(auxvarModProb);
				BigDecimal pmrrResc = (BigDecimal) probResc.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueRte(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_RESC);
				BigDecimal auxVzrever2 = (BigDecimal) mapVariables.get("VZCREVER_VYANT" + ConstantsModulos.CTE_PROY_RESC);
				auxFall = auxVzrever2;
			}
			
			if (varModProb.equals(ConstantsFactorias.MODULO_VZC412)) {
				String auxvarModProb = ConstantsFactorias.MODULO_VZREVER2;
				probResc = FactoriaModulos.getModulo(auxvarModProb);
				BigDecimal pmrrResc = (BigDecimal) probResc.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueRte(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_RESC);
				BigDecimal auxVzrever2 = (BigDecimal) mapVariables.get("VZCREVER_VYANT" + ConstantsModulos.CTE_PROY_RESC);
				auxResc = auxVzrever2;
			}
			
			mapVariables.put(CLAVE_IMP_FLUJ_ACT_RESC ,auxResc);
			proyUmic.get(iteracion-1).getBloqueRte().setFechaDevengo(varFechaDevengoAuxCopia);
			proyUmic.get(iteracion-1).getBloqueRte().setFechaPago(varFechaPagoAuxCopia);
		}
		
		BigDecimal varProbPeriodoInv = BigDecimal.ZERO;
		if(varCalcProbInv == ConstantsFunciones.CTE_1){
			varFechaDevengoAuxCopia = varProyPeriodo.get(iteracion-1).getBloqueCompl().getFechaDevengo();
			varFechaPagoAuxCopia = varProyPeriodo.get(iteracion-1).getBloqueCompl().getFechaPago();
			
			if(null != varProyPeriodo.get(iteracion-1).getBloqueCompl().getImpFlujoActualizado()){
				impActComplAux = varProyPeriodo.get(iteracion-1).getBloqueCompl().getImpFlujoActualizado();
			}
			
			probInv = FactoriaModulos.getModulo(varModProbNomInv); 
			if (varModProbNomInv.equals(ConstantsFactorias.MODULO_VZREVER2)) {
				varProbPeriodoInv = (BigDecimal) probInv.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueCompl(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_COMP);
			} else {
				varProbPeriodoInv = (BigDecimal) probInv.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueCompl(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, codSubproceso);
			}
			
			actInv = FactoriaModulos.getModulo(varActInv);
			varFechaPagoAux = proyUmic.get(iteracion-1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_COMP).getFechaPago();
			proyUmic.get(iteracion-1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_COMP).setFechaPago(proyUmic.get(iteracion-1).getFechaHasta());
			
			if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17) || btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIF17LIR)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17LIRIN)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17CLIR)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17IF) || btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIFF17OCI)) {
				
				varActFlujoInv = (BigDecimal) actInv.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueCompl(),iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso + ConstantsModulos.CTE_PROY_COMP);
				
			} else {
				varActFlujoInv = (BigDecimal) actInv.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueCompl(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, codSubproceso);
			}
			
			proyUmic.get(iteracion-1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_COMP).setFechaPago(varFechaPagoAux);
			
			if(null != varProyCopia.get(iteracion-1).getBloqueCompl().getImpFlujoNominal()){
				varProyCopia.get(iteracion-1).getBloqueCompl().setImpFlujoActualizado(varProyCopia.get(iteracion-1).getBloqueCompl().getImpFlujoNominal().multiply(varProbPeriodoInv).multiply(varActFlujoInv));
			}else{
				varProyCopia.get(iteracion-1).getBloqueCompl().setImpFlujoActualizado(BigDecimal.ZERO);
			}
			
			if(null != varProyCopia.get(iteracion-1).getBloqueCompl().getImpFlujoActualizado()){
				auxCompl = varProyCopia.get(iteracion-1).getBloqueCompl().getImpFlujoActualizado();
			}
			
			if ((varModProb.equals(ConstantsFactorias.MODULO_VARBVIT) || varModProb.equals(ConstantsFactorias.MODULO_VARB167) || varModProb.equals(ConstantsFactorias.MODULO_VARBFPC))
					&& umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_2_STRING)) {
				String auxvarModProb = ConstantsFactorias.MODULO_VZREVER2;
				probInv = FactoriaModulos.getModulo(auxvarModProb);
				BigDecimal pmrrInv = (BigDecimal) probInv.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueCompl(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_COMP);
				BigDecimal auxVzrever2 = (BigDecimal) mapVariables.get("VZCREVER_VYANT" + ConstantsModulos.CTE_PROY_COMP);
				auxFall = auxVzrever2;
			}
			
			if (varModProb.equals(ConstantsFactorias.MODULO_VZC412) ) {
				String auxvarModProb = ConstantsFactorias.MODULO_VZREVER2;
				probInv = FactoriaModulos.getModulo(auxvarModProb);
				BigDecimal pmrrInv = (BigDecimal) probInv.execute(varProyCopia, proyUmic.get(iteracion-1).getBloqueCompl(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_COMP);
				BigDecimal auxVzrever2 = (BigDecimal) mapVariables.get("VZCREVER_VYANT" + ConstantsModulos.CTE_PROY_COMP);
				auxCompl = auxVzrever2;
			}
			
			mapVariables.put(CLAVE_IMP_FLUJ_ACT_COMPL ,auxCompl);
			
			proyUmic.get(iteracion-1).getBloqueCompl().setFechaDevengo(varFechaDevengoAuxCopia);
			proyUmic.get(iteracion-1).getBloqueCompl().setFechaPago(varFechaPagoAuxCopia);
		}
		
		if(varCalcProbPrim == ConstantsFunciones.CTE_1){
			varFechaDevengoAuxCopia = varProyPeriodo.get(iteracion-1).getBloquePrim().getFechaDevengo();
			varFechaPagoAuxCopia = varProyPeriodo.get(iteracion-1).getBloquePrim().getFechaPago();
			if(null != varProyPeriodo.get(iteracion-1).getBloquePrim().getImpFlujoActualizado()){
				impActPrimAux = varProyPeriodo.get(iteracion-1).getBloquePrim().getImpFlujoActualizado();
			}			
			
			probPrim = FactoriaModulos.getModulo(varProbPrim); 
			if (varProbPrim.equals(ConstantsFactorias.MODULO_VZREVER2)) {
				varProbPeriodoPrim = (BigDecimal) probPrim.execute(varProyCopia, proyUmic.get(iteracion-1).getBloquePrim(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_PRIMA);
			} else {
				varProbPeriodoPrim = (BigDecimal) probPrim.execute(varProyCopia, proyUmic.get(iteracion-1).getBloquePrim(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, codSubproceso);
			}
			
			
			actPrim = FactoriaModulos.getModulo(varActPrim);
			varFechaPagoAux = proyUmic.get(iteracion-1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_PRIMA).getFechaPago();
			proyUmic.get(iteracion-1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_PRIMA).setFechaPago(proyUmic.get(iteracion-1).getFechaHasta());
			
			if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17) || btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIF17LIR)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17LIRIN)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17CLIR)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17IF) || btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIFF17OCI)) {
				
				varActFlujoPrim = (BigDecimal) actPrim.execute(varProyCopia, proyUmic.get(iteracion-1).getBloquePrim(),iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso + ConstantsModulos.CTE_PROY_PRIMA);
				
			} else {
				varActFlujoPrim = (BigDecimal) actPrim.execute(varProyCopia, proyUmic.get(iteracion-1).getBloquePrim(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, codSubproceso);
			}
			
			proyUmic.get(iteracion-1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_PRIMA).setFechaPago(varFechaPagoAux);
			
			if(null != varProyCopia.get(iteracion-1).getBloquePrim().getImpFlujoNominal()){
				varProyCopia.get(iteracion-1).getBloquePrim().setImpFlujoActualizado(varProyCopia.get(iteracion-1).getBloquePrim().getImpFlujoNominal().multiply(varProbPeriodoPrim).multiply(varActFlujoPrim));
			}else{
				varProyCopia.get(iteracion-1).getBloquePrim().setImpFlujoActualizado(BigDecimal.ZERO);
			}
			
			if(null != varProyCopia.get(iteracion-1).getBloquePrim().getImpFlujoActualizado()){
				auxPrim = varProyCopia.get(iteracion-1).getBloquePrim().getImpFlujoActualizado();
			}
			
			if ((varModProb.equals(ConstantsFactorias.MODULO_VARBVIT) || varModProb.equals(ConstantsFactorias.MODULO_VARB167) || varModProb.equals(ConstantsFactorias.MODULO_VARBFPC))
					&& umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_2_STRING)) {
				String auxvarModProb = ConstantsFactorias.MODULO_VZREVER2;
				probPrim = FactoriaModulos.getModulo(auxvarModProb);
				BigDecimal pmrrPrim = (BigDecimal) probPrim.execute(varProyCopia, proyUmic.get(iteracion-1).getBloquePrim(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_PRIMA);
				BigDecimal auxVzrever2 = (BigDecimal) mapVariables.get("VZCREVER_VYANT" + ConstantsModulos.CTE_PROY_PRIMA);
				auxPrim = auxVzrever2;
			}
			
			if (varModProb.equals(ConstantsFactorias.MODULO_VZC412) ) {
				String auxvarModProb = ConstantsFactorias.MODULO_VZREVER2;
				probPrim = FactoriaModulos.getModulo(auxvarModProb);
				BigDecimal pmrrPrim = (BigDecimal) probPrim.execute(varProyCopia, proyUmic.get(iteracion-1).getBloquePrim(),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, ConstantsModulos.CTE_PROY_PRIMA);
				BigDecimal auxVzrever2 = (BigDecimal) mapVariables.get("VZCREVER_VYANT" + ConstantsModulos.CTE_PROY_PRIMA);
				auxPrim = auxVzrever2;
			}
			
			mapVariables.put(CLAVE_IMP_FLUJ_ACT_PRIM, auxPrim);
			
			proyUmic.get(iteracion-1).getBloquePrim().setFechaDevengo(varFechaDevengoAuxCopia);
			proyUmic.get(iteracion-1).getBloquePrim().setFechaPago(varFechaPagoAuxCopia);
		}
		
		
		if (iteracion == proyUmic.size()) {		
			VxjantVida = BigDecimal.ZERO;
			VxjantFall = BigDecimal.ZERO;
			VxjantGto = BigDecimal.ZERO;
			VxjantCom = BigDecimal.ZERO;
			VxjantRte = BigDecimal.ZERO;
			VxjantInv = BigDecimal.ZERO;
			VxjantPrim = BigDecimal.ZERO;
		}else{
			VxjantVida = (BigDecimal) mapVariables.get(CLAVE_VXJ_VIDA);
			if (VxjantVida == null) {
				VxjantVida = BigDecimal.ZERO;
			}
			VxjantFall = (BigDecimal) mapVariables.get(CLAVE_VXJ_FALL);
			if (VxjantFall == null) {
				VxjantFall = BigDecimal.ZERO;
			}
			VxjantGto = (BigDecimal) mapVariables.get(CLAVE_VXJ_GTO);
			if (VxjantGto == null) {
				VxjantGto = BigDecimal.ZERO;
			}
			VxjantCom = (BigDecimal) mapVariables.get(CLAVE_VXJ_COM);
			if (VxjantCom == null) {
				VxjantCom = BigDecimal.ZERO;
			}
			VxjantRte = (BigDecimal) mapVariables.get(CLAVE_VXJ_RTE);
			if (VxjantRte == null) {
				VxjantRte = BigDecimal.ZERO;
			}
			VxjantInv = (BigDecimal) mapVariables.get(CLAVE_VXJ_INV);
			if (VxjantInv == null) {
				VxjantInv = BigDecimal.ZERO;
			}
			VxjantPrim = (BigDecimal) mapVariables.get(CLAVE_VXJ_PRIM);
			if (VxjantPrim == null) {
				VxjantPrim = BigDecimal.ZERO;
			}
		}
		
		varFecDevengoAux = varProyPeriodo.get(iteracion-1).getBloqueVida().getFechaDevengo();
		varFecPagoAux = varProyPeriodo.get(iteracion-1).getBloqueVida().getFechaPago();
		
		
		modProb = FactoriaModulos.getModulo(varModProb);
		if (varModProb.equals(ConstantsFactorias.MODULO_VZREVER2)) {
			varProbPeriodo = BigDecimal.ONE;

		} else {
			proyUmic.get(iteracion-1).getBloqueVida().setFechaPago(proyUmic.get(iteracion-1).getFechaHasta());
			proyUmic.get(iteracion-1).getBloqueVida().setFechaDevengo(proyUmic.get(iteracion-1).getFechaHasta());
			varProbPeriodo = (BigDecimal) modProb.execute(varProyPeriodo, proyUmic.get(iteracion-1).getBloqueVida(), iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, codSubproceso);
		}
		
		proyUmic.get(iteracion-1).getBloqueVida().setFechaPago(varFecPagoAux);
		proyUmic.get(iteracion-1).getBloqueVida().setFechaDevengo(varFecDevengoAux);
		
		if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL) || 
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA) || 
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR) || 
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTIU) || 
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTID) || 
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRGTO) || 
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMFE) || 
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMMI) || 
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCF) || 
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCI) || 
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLFE) || 
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLMI) || 
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC) ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRVM)  ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEP) ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEN) ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIP) ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIN) ||
				btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM) || 
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIF17LIR) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_N17LIRIN) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIFF17OCI) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17IF) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_N17CLIR) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NF17AEN) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NF17MFE) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NF17GTO)){
			modAnu = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_ATC);
			
			Timestamp varFecDevengoAux2 = varProyPeriodo.get(iteracion-1).getBloqueVida().getFechaDevengo();
			Timestamp varFecPagoAux2 = varProyPeriodo.get(iteracion-1).getBloqueVida().getFechaPago();
			proyUmic.get(iteracion-1).getBloqueVida().setFechaPago(proyUmic.get(iteracion-1).getFechaHasta());
			proyUmic.get(iteracion-1).getBloqueVida().setFechaDevengo(proyUmic.get(iteracion-1).getFechaHasta());
			
			varAnuPeriodo = (BigDecimal) modAnu.execute(varProyPeriodo, proyUmic.get(iteracion-1).getBloqueVida(), iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, codSubproceso);

			proyUmic.get(iteracion-1).getBloqueVida().setFechaPago(varFecPagoAux2);
			proyUmic.get(iteracion-1).getBloqueVida().setFechaDevengo(varFecDevengoAux2);
			
		}else{
			varAnuPeriodo = BigDecimal.ONE;
		}
		
		//Calculo de la actualizacion financiera del periodo
		actVida = FactoriaModulos.getModulo(varModAct);
		Timestamp fechaAux = proyUmic.get(iteracion-1).getBloqueVida().getFechaPago();
		Timestamp fechaDevAux = proyUmic.get(iteracion-1).getBloqueVida().getFechaDevengo();
		proyUmic.get(iteracion-1).getBloqueVida().setFechaPago(proyUmic.get(iteracion-1).getFechaDesde());
		proyUmic.get(iteracion-1).getBloqueVida().setFechaDevengo(proyUmic.get(iteracion-1).getFechaDesde());
		varActPeriodo = (BigDecimal) actVida.execute(varProyPeriodo, proyUmic.get(iteracion-1).getBloqueVida(), iteracion, proyUmic.get(iteracion-1).getFechaHasta(), umic, btcUmic, mapVariables, codSubproceso + ConstantsModulos.CTE_PROY_VIDA);
		proyUmic.get(iteracion-1).getBloqueVida().setFechaPago(fechaAux);
		proyUmic.get(iteracion-1).getBloqueVida().setFechaDevengo(fechaDevAux);
		varFecPago = varProyPeriodo.get(iteracion-1).getBloqueVida().getFechaPago();

		if (varProbPeriodo.equals(BigDecimal.ZERO) || varAnuPeriodo.equals(BigDecimal.ZERO)){
			varInvProbj = BigDecimal.ZERO;
		} else {
			varInvProbj = varProbPeriodo.multiply(varAnuPeriodo);
		}
		
		
		
		varActFinj = varActPeriodo;
		
		BigDecimal valorFlujoActualizado = BigDecimal.ZERO;

			if (varCalcProbVida == ConstantsFunciones.CTE_1){
				
				//Cargo el valor del importe del flujo actualizado de esta corriente de la iteracion anterior
				valorFlujoActualizado = (BigDecimal) mapVariables.get(CLAVE_IMP_FLUJ_ACT_VIDA);
				
				if(null == valorFlujoActualizado){
					valorFlujoActualizado = BigDecimal.ZERO;
				}
				
				if ((varModProb.equals(ConstantsFactorias.MODULO_VARBVIT) || varModProb.equals(ConstantsFactorias.MODULO_VARB167) || varModProb.equals(ConstantsFactorias.MODULO_VARBFPC))
						&& umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_2_STRING)){
					VxjVida = valorFlujoActualizado;
				} else {
					VxjVida = (VxjantVida.multiply(varInvProbj).multiply(varActFinj)).add(valorFlujoActualizado);
				}
				
				if (varModProb.equals(ConstantsFactorias.MODULO_VZC412) 
						&& !umic.getDatosGenerales().getKbencon().equals("TIT")) {
					VxjVida = valorFlujoActualizado;
				}
				if (varModProb.equals(ConstantsFactorias.MODULO_VZC412) 
						&& umic.getDatosGenerales().getKbencon().equals("TIT")) {
					VxjVida = (BigDecimal) mapVariables.get("VZCREVER_VXANT" + ConstantsModulos.CTE_PROY_VIDA);
				}
				
			} else {
				VxjVida = BigDecimal.ZERO;
			}
			
			if (varCalcProbFall == ConstantsFunciones.CTE_1){
				valorFlujoActualizado = (BigDecimal) mapVariables.get(CLAVE_IMP_FLUJ_ACT_FALL);
				if(null == valorFlujoActualizado){
					valorFlujoActualizado = BigDecimal.ZERO;
				}
				
				if ((varModProb.equals(ConstantsFactorias.MODULO_VARBVIT) || varModProb.equals(ConstantsFactorias.MODULO_VARB167) || varModProb.equals(ConstantsFactorias.MODULO_VARBFPC))
						&& umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_2_STRING)){
					VxjFall = valorFlujoActualizado;
				} else {
					VxjFall = (VxjantFall.multiply(varInvProbj).multiply(varActFinj)).add(valorFlujoActualizado);
				}
				
				if (varModProb.equals(ConstantsFactorias.MODULO_VZC412) 
						&& !umic.getDatosGenerales().getKbencon().equals("TIT")) {
					VxjFall = valorFlujoActualizado;
				}
				if (varModProb.equals(ConstantsFactorias.MODULO_VZC412) 
						&& umic.getDatosGenerales().getKbencon().equals("TIT")) {
					VxjFall = (BigDecimal) mapVariables.get("VZCREVER_VXANT" + ConstantsModulos.CTE_PROY_FALL);
				}
				
			} else {
				VxjFall = BigDecimal.ZERO;
			}
			
			if (varCalcProbGast == ConstantsFunciones.CTE_1){
				valorFlujoActualizado = (BigDecimal) mapVariables.get(CLAVE_IMP_FLUJ_ACT_GTO);
				if(null == valorFlujoActualizado){
					valorFlujoActualizado = BigDecimal.ZERO;
				}

				if ((varModProb.equals(ConstantsFactorias.MODULO_VARBVIT) || varModProb.equals(ConstantsFactorias.MODULO_VARB167) || varModProb.equals(ConstantsFactorias.MODULO_VARBFPC))
						&& umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_2_STRING)){
					VxjGto = valorFlujoActualizado;
				} else {
					VxjGto = (VxjantGto.multiply(varInvProbj).multiply(varActFinj)).add(valorFlujoActualizado);
				}
				
				if (varModProb.equals(ConstantsFactorias.MODULO_VZC412) 
						&& !umic.getDatosGenerales().getKbencon().equals("TIT")) {
					VxjGto = valorFlujoActualizado;
				}
				if (varModProb.equals(ConstantsFactorias.MODULO_VZC412) 
						&& umic.getDatosGenerales().getKbencon().equals("TIT")) {
					VxjGto = (BigDecimal) mapVariables.get("VZCREVER_VXANT" + ConstantsModulos.CTE_PROY_GTOS);
				}

			} else {
				VxjGto = BigDecimal.ZERO;
			}

			if (varCalcProbInv == ConstantsFunciones.CTE_1){
				valorFlujoActualizado = (BigDecimal) mapVariables.get(CLAVE_IMP_FLUJ_ACT_COMPL);
				if(null == valorFlujoActualizado){
					valorFlujoActualizado = BigDecimal.ZERO;
				}

				if ((varModProb.equals(ConstantsFactorias.MODULO_VARBVIT) || varModProb.equals(ConstantsFactorias.MODULO_VARB167) || varModProb.equals(ConstantsFactorias.MODULO_VARBFPC))
						&& umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_2_STRING)){
					VxjInv = valorFlujoActualizado;
				} else {
					VxjInv = (VxjantInv.multiply(varInvProbj).multiply(varActFinj)).add(valorFlujoActualizado);
				}
				
				if (varModProb.equals(ConstantsFactorias.MODULO_VZC412) 
						&& !umic.getDatosGenerales().getKbencon().equals("TIT")) {
					VxjInv = valorFlujoActualizado;
				}
				if (varModProb.equals(ConstantsFactorias.MODULO_VZC412) 
						&& umic.getDatosGenerales().getKbencon().equals("TIT")) {
					VxjInv = (BigDecimal) mapVariables.get("VZCREVER_VXANT" + ConstantsModulos.CTE_PROY_COMP);
				}

			} else {
				VxjInv = BigDecimal.ZERO;
			}
			
			if (varCalcProbComi == ConstantsFunciones.CTE_1){
				valorFlujoActualizado = (BigDecimal) mapVariables.get(CLAVE_IMP_FLUJ_ACT_COM);

				if ((varModProb.equals(ConstantsFactorias.MODULO_VARBVIT) || varModProb.equals(ConstantsFactorias.MODULO_VARB167) || varModProb.equals(ConstantsFactorias.MODULO_VARBFPC))
						&& umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_2_STRING)){
					VxjCom = valorFlujoActualizado;
				} else {
					VxjCom = (VxjantCom.multiply(varInvProbj).multiply(varActFinj)).add(valorFlujoActualizado);
				}
				
				if (varModProb.equals(ConstantsFactorias.MODULO_VZC412) 
						&& !umic.getDatosGenerales().getKbencon().equals("TIT")) {
					VxjCom = valorFlujoActualizado;
				}
				if (varModProb.equals(ConstantsFactorias.MODULO_VZC412) 
						&& umic.getDatosGenerales().getKbencon().equals("TIT")) {
					VxjCom = (BigDecimal) mapVariables.get("VZCREVER_VXANT" + ConstantsModulos.CTE_PROY_COMI);
				}

			} else {
				VxjCom = BigDecimal.ZERO;
			}
			
			if (varCalcProbResc == ConstantsFunciones.CTE_1){
				valorFlujoActualizado = (BigDecimal) mapVariables.get(CLAVE_IMP_FLUJ_ACT_RESC);

				if ((varModProb.equals(ConstantsFactorias.MODULO_VARBVIT) || varModProb.equals(ConstantsFactorias.MODULO_VARB167) || varModProb.equals(ConstantsFactorias.MODULO_VARBFPC))
						&& umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_2_STRING)){
					VxjRte = valorFlujoActualizado;
				} else {
					VxjRte = (VxjantRte.multiply(varInvProbj).multiply(BigDecimal.ONE.subtract(varActFinj))).add(valorFlujoActualizado);
				}
				
				if (varModProb.equals(ConstantsFactorias.MODULO_VZC412) 
						&& !umic.getDatosGenerales().getKbencon().equals("TIT")) {
					VxjRte = valorFlujoActualizado;
				}
				if (varModProb.equals(ConstantsFactorias.MODULO_VZC412) 
						&& umic.getDatosGenerales().getKbencon().equals("TIT")) {
					VxjRte = (BigDecimal) mapVariables.get("VZCREVER_VXANT" + ConstantsModulos.CTE_PROY_RESC);
				}
				
			} else {
				VxjRte = BigDecimal.ZERO;
			}
			
			if (varCalcProbPrim == ConstantsFunciones.CTE_1){
				valorFlujoActualizado = (BigDecimal) mapVariables.get(CLAVE_IMP_FLUJ_ACT_PRIM);
				if(null == valorFlujoActualizado){
					valorFlujoActualizado = BigDecimal.ZERO;
				}

				if ((varModProb.equals(ConstantsFactorias.MODULO_VARBVIT) || varModProb.equals(ConstantsFactorias.MODULO_VARB167) || varModProb.equals(ConstantsFactorias.MODULO_VARBFPC))
						&& umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_2_STRING)){
					VxjPrim = valorFlujoActualizado;
				} else {
					VxjPrim = (VxjantPrim.multiply(varInvProbj).multiply(varActFinj)).add(valorFlujoActualizado);
				}
				
				if (varModProb.equals(ConstantsFactorias.MODULO_VZC412) 
						&& !umic.getDatosGenerales().getKbencon().equals("TIT")) {
					VxjPrim = valorFlujoActualizado;
				}
				if (varModProb.equals(ConstantsFactorias.MODULO_VZC412) 
						&& umic.getDatosGenerales().getKbencon().equals("TIT")) {
					VxjPrim = (BigDecimal) mapVariables.get("VZCREVER_VXANT" + ConstantsModulos.CTE_PROY_PRIMA);
				}

			} else {
				VxjPrim = BigDecimal.ZERO;
			}
		
		// Guardamos las variables para la próxima iteración
		mapVariables.put(CLAVE_VAR_INV_PROB, varInvProbj);
		mapVariables.put(CLAVE_VAR_ACT_FIN, varActFinj);
		mapVariables.put(CLAVE_VXJ_VIDA, VxjVida);
		mapVariables.put(CLAVE_VXJ_FALL, VxjFall);
		mapVariables.put(CLAVE_VXJ_GTO, VxjGto);
		mapVariables.put(CLAVE_VXJ_COM, VxjCom);
		mapVariables.put(CLAVE_VXJ_RTE, VxjRte);
		mapVariables.put(CLAVE_VXJ_PRIM, VxjPrim);
		mapVariables.put(CLAVE_VXJ_INV, VxjInv);
		
		Vxj = VxjVida.add(VxjFall).add(VxjGto).add(VxjInv).add(VxjCom).add(VxjRte).subtract(VxjPrim);
				
		if(varCalcProbVida == ConstantsFunciones.CTE_1){
			proyUmic.get(iteracion-1).getBloqueVida().setImpFlujoActualizado(impActVidaAux);
		}
		if(varCalcProbFall == ConstantsFunciones.CTE_1){
			proyUmic.get(iteracion-1).getBloqueFall().setImpFlujoActualizado(impActFallAux);
		}
		if(varCalcProbGast == ConstantsFunciones.CTE_1){
			proyUmic.get(iteracion-1).getBloqueGto().setImpFlujoActualizado(impActGtoAux);
		}
		if(varCalcProbComi == ConstantsFunciones.CTE_1){
			mapVariables.put(CLAVE_IMP_FLUJ_ACT_COM, auxCom);
			proyUmic.get(iteracion-1).getBloqueComi().setImpFlujoActualizado(impActComiAux);
		}
		if(varCalcProbResc == ConstantsFunciones.CTE_1){
			mapVariables.put(CLAVE_IMP_FLUJ_ACT_RESC, auxResc);
			proyUmic.get(iteracion-1).getBloqueRte().setImpFlujoActualizado(impActRteAux);
		}
		if(varCalcProbInv == ConstantsFunciones.CTE_1){
			mapVariables.put(CLAVE_IMP_FLUJ_ACT_COMPL, auxCompl);
			proyUmic.get(iteracion-1).getBloqueCompl().setImpFlujoActualizado(impActComplAux);
		}
		if(varCalcProbPrim == ConstantsFunciones.CTE_1){
			mapVariables.put(CLAVE_IMP_FLUJ_ACT_PRIM, auxPrim);
			proyUmic.get(iteracion-1).getBloquePrim().setImpFlujoActualizado(impActPrimAux);
		}
		
		if (umic.getDatosCoaseguro().getPcoaseg() != null 
				&& !umic.getDatosCoaseguro().getPcoaseg().equals(BigDecimal.ZERO)) {
			Vxj = Vxj.multiply(umic.getDatosCoaseguro().getPcoaseg().divide(ConstantsFunciones.CTE_OPER_100, ConstantsFunciones.MATH_CONTEXT));
		}
				
		salida.setProvbtiproy(Vxj.setScale(ConstantsFunciones.CTE_2, RoundingMode.HALF_DOWN));
		salida.setTerminalAnterior(varterminalAnterior);
		salida.setTerminalPosterior(terminalPosterior);

		if (ModuloPMRR02.LOG.isTraceEnabled()) {
			ModuloPMRR02.LOG.trace("Fin función << moduloPMRR02 >> de la clase ModuloPMRR01, para la iteracion = {} con resultado varPMRR02 = {}", iteracion, Vxj);
		}
		
		return salida;
	}

	
}
