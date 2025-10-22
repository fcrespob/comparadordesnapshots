package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo que devuelve los factores de probabilización a aplicar en  el cálculo de la cuantía probable de una garantía.  
 * La aplicación del módulo del factor biométrico al ser multiplicado por el importe nominal del que ya disponemos, 
 * nos dará como resultado el flujo probable de cada punto de la corriente.
 * La expresión matemática para su determinación es la siguiente:
 * 				VZREVER=Vzc1(fcal,j)+REVER*[((Vzc1(fcal,difercol)*Vzc2(fcal,j))-Vzc1zc2(fcal,j)]
 * 
 * @author agonzalezgar
 *
 */
public class ModuloVZREVER2 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVZREVER2.class);
	
	
	// Inicio de las variables estáticas para agilizar operaciones.
	
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VZREVER2;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VA_CRIT_EDA = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_VA_CRIT_EDA.concat(CLAVE_MODULO);	
	
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	public static final String CLAVE_VAR_EDAD_CAL1 = ConstantsModulos.CTE_EDAD_CAL1.concat(CLAVE_MODULO);
	public static final String CLAVE_VAR_EDAD_CAL2 = ConstantsModulos.CTE_EDAD_CAL2.concat(CLAVE_MODULO);
	public static final String CLAVE_VAR_EDAD_J1_ENTERO = ConstantsModulos.CTE_VAR_EDAD_J1_ENTERO.concat(CLAVE_MODULO);
	public static final String CLAVE_VAR_EDAD_J2_ENTERO = ConstantsModulos.CTE_VAR_EDAD_J2_ENTERO.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_ZC1 = ConstantsModulos.CTE_VAR_ZC1.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ZC2 = ConstantsModulos.CTE_VAR_ZC2.concat(CLAVE_MODULO);
	
	public static final String CLAVE_VAR_VAL_TAB_MORT1 = ConstantsModulos.CTE_VAR_VAL_TAB_MORT1.concat(CLAVE_MODULO);
	public static final String CLAVE_VAR_VAL_TAB_MORT2 = ConstantsModulos.CTE_VAR_VAL_TAB_MORT2.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_LZC1 = ConstantsModulos.CTE_VAR_LZC1.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_LZC2 = ConstantsModulos.CTE_VAR_LZC2.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_REVER = ConstantsModulos.CTE_VAR_REVER.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_DIFERCOL = ConstantsModulos.CTE_VAR_DIFERCOL.concat(CLAVE_MODULO);
	
	public static final String CLAVE_MAPA_ANNOS = ConstantsModulos.CTE_MAPA_ANNOS.concat(CLAVE_MODULO);
	public static final String CLAVE_PARENT_MODULE = "parentModule".concat(CLAVE_MODULO);
	public static final String CLAVE_STOREN_ANNOS = "storenAnnos".concat(CLAVE_MODULO);
	public static final String CLAVE_INCR_FECHA ="incrFecha".concat(CLAVE_MODULO);
	
	public static final String CTE_PROB_J = "probJ".concat(CLAVE_MODULO);
	public static final String CTE_PROB_J1 = "probJ1".concat(CLAVE_MODULO);
	public static final String CTE_PROB_J1_MENOSJ = "probJ1-J".concat(CLAVE_MODULO);
	public static final String CTE_PROB2_J = "prob2J".concat(CLAVE_MODULO);
	public static final String CTE_PROB2_J1 = "prob2J1".concat(CLAVE_MODULO);
	public static final String CTE_PROB2_J1_MENOSJ = "prob2J1-J".concat(CLAVE_MODULO);
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
		BigDecimal resultado = BigDecimal.ZERO;
		//Fin variables locales
		
		try {
			
			if (ModuloVZREVER2.LOG.isTraceEnabled()) {
				ModuloVZREVER2.LOG.trace("Inicio de execute en clase ModuloVZREVER2");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZC002
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubProceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			
			//Invocamos a la función de calculo VZREVER
			resultado = moduloVZREVER2(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubProceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVZREVER2.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVZREVER2.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloVZREVER2.LOG.isTraceEnabled()) {
			ModuloVZREVER2.LOG.trace("Fin de execute en clase ModuloVZREVER2");
		}
		
		return resultado;
	}
	/**
	 * Modulo de cálculo que devuelve los factores de probabilización a aplicar en  el cálculo de la cuantía probable de una garantía.  
	 * La aplicación del módulo del factor biométrico al ser multiplicado por el importe nominal del que ya disponemos, 
	 * nos dará como resultado el flujo probable de cada punto de la corriente.
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
	 * @return
	 */

	private BigDecimal moduloVZREVER2(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubProceso) {
		//Variables locales
		String varCriterioEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriterioFecha = ConstantsFunciones.CTE_CADENA_VACIA;

		BigDecimal varRever = BigDecimal.ZERO;
		BigDecimal vzrever = BigDecimal.ZERO;
		BigDecimal varZc1 = BigDecimal.ZERO;
		BigDecimal varZc2 = BigDecimal.ZERO;
		BigDecimal varVzc1_r = BigDecimal.ZERO;
		BigDecimal varVzc2_r = BigDecimal.ZERO;
		BigDecimal varAct = BigDecimal.ZERO;
		BigDecimal varVzc1 = BigDecimal.ZERO;
		BigDecimal varVzc2 = BigDecimal.ZERO;
		Timestamp varfechaEfecto = null;
		Timestamp fechaDevengo = null;
		Modulo mVzc1, mVzc2, mAct;
		BigDecimal vyant = BigDecimal.ZERO;
		BigDecimal vy = BigDecimal.ZERO;
		BigDecimal vxant = BigDecimal.ZERO;
		BigDecimal vx = BigDecimal.ZERO;
		BigDecimal flujoX = BigDecimal.ZERO;
		BigDecimal flujoY = BigDecimal.ZERO;
		BigDecimal factProbY = BigDecimal.ZERO;
		BigDecimal factProb = BigDecimal.ZERO;
		BigDecimal difercol_rr = BigDecimal.ONE;
		BigDecimal difercol = BigDecimal.ONE;
		BigDecimal cuantia = BigDecimal.ZERO;
		BigDecimal vxy = BigDecimal.ZERO;
		String ultimaIteracion = (String) mapVariables.get("VZCREVER_NEW_Iteracion" + codSubProceso);
		String varModVzc1, varModVzc2, varModAct;
		Timestamp fIniRenta;
		Boolean diferimiento = true;
		List<DetalleCorriente> varProyCopia = proyUmic;
		Timestamp varFechaDevengoAuxCopia = null;
		Timestamp varFechaPagoAuxCopia = null;
		Timestamp fcalc2 = (Timestamp) mapVariables.get("VZCREVER_FCALC");
		List<BigDecimal> lstValoresTabMort1 = null;
		BigDecimal	varEdadCalc1 = BigDecimal.ZERO;
		BigDecimal varLzc1 = BigDecimal.ZERO;
		BigDecimal varLzc1_rr = BigDecimal.ZERO;
		BigDecimal varZc1_rr = BigDecimal.ZERO;
		String varModFall;
		String varModFall228;
		Timestamp varFechaPagoAux = null;
		//Fin variables locales
		
		if (ModuloVZREVER2.LOG.isTraceEnabled()) {
			ModuloVZREVER2.LOG.trace("Inicio función << moduloVZREVER2 >> de la clase ModuloVZREVER2, para la entrada iteracion = {}",	iteracion);
		}
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, fcalc, umic, btcUmic);
		varCriterioEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_VA_CRIT_EDA);
		varCriterioFecha = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterioEdad);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterioFecha);
		}
	
		fechaDevengo = bloqueCorriente.getFechaDevengo();
	
		if (null != fcalc) {
			 
			Timestamp fecNacAseg = umic.getAsegurados().getFnacAseg1();
			varFechaDevengoAuxCopia = varProyCopia.get(iteracion-1).getBloqueBySubproceso(codSubProceso).getFechaDevengo();
			varFechaPagoAuxCopia = varProyCopia.get(iteracion-1).getBloqueBySubproceso(codSubProceso).getFechaPago();
			varfechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
			
			Integer varEdifer = umic.getDatosGenerales().getEdifer();
			varEdadCalc1 = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL1, varfechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriterioEdad, umic.getRentas().getFecIni(), varEdifer);
			
			
			varModVzc1 = ConstantsFactorias.MODULO_VZC;
			varModVzc2 = ConstantsFactorias.MODULO_VZC2;
			varModAct = ConstantsFactorias.MODULO_ACT001;
			
			// Ahora calculamos Vzc1 sin ser recursivo
			mVzc1 = FactoriaModulos.getModulo(varModVzc1);
			varVzc1 = (BigDecimal) mVzc1.execute(proyUmic, proyUmic.get(iteracion-1).getBloqueBySubproceso(codSubProceso),iteracion, fcalc2, umic, btcUmic, mapVariables, codSubProceso);
			
			// Ahora calculamos Vzc2 desde la fecha devengo a la fcalc
			mVzc2 = FactoriaModulos.getModulo(varModVzc2);
			if (null == umic.getAsegurados().getFnacAseg2() || null == umic.getAsegurados().getCsexAseg2() ||
					null == umic.getAsegurados().getEdadAseg2()) {
				varVzc2 = BigDecimal.ZERO;
			} else {
				varVzc2 = (BigDecimal) mVzc2.execute(proyUmic, proyUmic.get(iteracion-1).getBloqueBySubproceso(codSubProceso),iteracion, fcalc2, umic, btcUmic, mapVariables, codSubProceso);
			}
			proyUmic.get(iteracion-1).getBloqueBySubproceso(codSubProceso).setFechaPago(proyUmic.get(iteracion-1).getFechaHasta());
			proyUmic.get(iteracion-1).getBloqueBySubproceso(codSubProceso).setFechaDevengo(proyUmic.get(iteracion-1).getFechaHasta());
			
			// Calculamos el Vzc1 recursivo
			varVzc1_r = (BigDecimal) mVzc1.execute(proyUmic, proyUmic.get(iteracion-1).getBloqueBySubproceso(codSubProceso),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, codSubProceso);
			// Ahora calculamos el Vzc2 recursivo
			if (null == umic.getAsegurados().getFnacAseg2() || null == umic.getAsegurados().getCsexAseg2() ||
					null == umic.getAsegurados().getEdadAseg2()) {
				varVzc2_r = BigDecimal.ZERO;
			} else {
				varVzc2_r = (BigDecimal) mVzc2.execute(proyUmic, proyUmic.get(iteracion-1).getBloqueBySubproceso(codSubProceso),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, codSubProceso);
			}
			proyUmic.get(iteracion-1).getBloqueBySubproceso(codSubProceso).setFechaPago(varFechaPagoAuxCopia);
			proyUmic.get(iteracion-1).getBloqueBySubproceso(codSubProceso).setFechaDevengo(varFechaDevengoAuxCopia);
			
			// Ahora calculamos la parte de la actualizacion
			if (fechaDevengo == null) {
				proyUmic.get(iteracion-1).getBloqueBySubproceso(codSubProceso).setFechaPago(UtilFechas.getUltimoDiaDelMes(proyUmic.get(iteracion-1).getFechaDesde()));
			}
			
			varFechaPagoAux = proyUmic.get(iteracion-1).getBloqueBySubproceso(codSubProceso).getFechaPago();
			proyUmic.get(iteracion-1).getBloqueBySubproceso(codSubProceso).setFechaPago(proyUmic.get(iteracion-1).getFechaHasta());
			mAct = FactoriaModulos.getModulo(varModAct);
			varAct = (BigDecimal) mAct.execute(proyUmic, proyUmic.get(iteracion-1).getBloqueBySubproceso(codSubProceso),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, codSubProceso);
			varRever = UtilModulos.getVarRever(mapVariables, CLAVE_VAR_REVER, umic.getRentas().getPreversion());
			proyUmic.get(iteracion-1).getBloqueBySubproceso(codSubProceso).setFechaPago(varFechaPagoAux);
			
			if (fechaDevengo == null) {
				proyUmic.get(iteracion-1).getBloqueBySubproceso(codSubProceso).setFechaPago(null);
			}
			
			if (ultimaIteracion == null) {
				mapVariables.put("VZCREVER_NEW_Iteracion" + codSubProceso, "S");
			} else {
				vyant = (BigDecimal) mapVariables.get("VZCREVER_VYANT" + codSubProceso);
				vxant = (BigDecimal) mapVariables.get("VZCREVER_VXANT" + codSubProceso);		
			}
			
			// Para saber si tenemos o no diferimiento
			fIniRenta = umic.getRentas().getFecIni();
			
			if (umic.getRentas().getFecIni() != null) {
				
				lstValoresTabMort1 = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT1, umic, btcUmic, 
						IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterioEdad, ConstantesSolvencia.CTE_TABMORT_L);
				varZc1 = UtilModulos.getVarZc(mapVariables, CLAVE_VAR_ZC1, varEdadCalc1, varfechaEfecto, fcalc2, varCriterioFecha,varCriterioEdad);
				
				int varZc1Entero = varZc1.intValue();
				if(varZc1Entero > lstValoresTabMort1.size()-2) {
					varLzc1 = BigDecimal.ZERO;
				} else {
					varLzc1 = UtilModulos.getVarLzc(mapVariables, CLAVE_VAR_LZC1 + codSubProceso, varZc1, lstValoresTabMort1.get(varZc1Entero), lstValoresTabMort1.get(varZc1Entero + 1));
				}
				
				difercol = UtilModulos.getVarVzc1Difercol(mapVariables, CLAVE_VAR_DIFERCOL + codSubProceso + fcalc2, varfechaEfecto, umic.getRentas().getFecIni(), fcalc2, 
						varEdadCalc1, lstValoresTabMort1, varLzc1, varCriterioFecha, BigDecimal.ONE);
				
				varZc1_rr = UtilModulos.getVarZc(mapVariables, CLAVE_VAR_ZC1  + fecNacAseg + fcalc + codSubProceso, varEdadCalc1, varfechaEfecto, fcalc, varCriterioFecha,varCriterioEdad);
				
				int varZc1Entero_rr = varZc1_rr.intValue();
				if(varZc1Entero_rr > lstValoresTabMort1.size()-2) {
					varLzc1_rr = BigDecimal.ZERO;
				} else {
					varLzc1_rr = UtilModulos.getVarLzc(mapVariables, CLAVE_VAR_LZC1 + varZc1_rr + codSubProceso, varZc1_rr, lstValoresTabMort1.get(varZc1Entero_rr), lstValoresTabMort1.get(varZc1Entero_rr + 1));
				}
				
				difercol_rr = UtilModulos.getVarVzc1Difercol(mapVariables, CLAVE_VAR_DIFERCOL + codSubProceso + fcalc, varfechaEfecto, umic.getRentas().getFecIni(), fcalc, 
						varEdadCalc1, lstValoresTabMort1, varLzc1_rr, varCriterioFecha, BigDecimal.ONE);
			}
			
			// La cuantía es el flujo nominal 
			varModFall = (String) mapVariables.get("VZCREVER_MODULO_FALL");
			varModFall228 = (String) mapVariables.get("VZCREVER_MODULO_FALL_228");
			if (varModFall == null) {
				varModFall = ConstantsFactorias.MODULO_VZC;
			}
			if (varModFall228 == null) {
				varModFall228 = varModFall;
			}
			cuantia = proyUmic.get(iteracion-1).getBloqueBySubproceso(codSubProceso).getImpFlujoNominal();
			if (difercol.equals(BigDecimal.ZERO)) {
				flujoY = cuantia.multiply(varRever);
			} else {
				if (varModFall.equals(ConstantsFactorias.MODULO_VRTAMIX)) {
					Modulo mVrtamix = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VRTAMIX);
					BigDecimal auxVratamix = (BigDecimal) mVrtamix.execute(proyUmic, proyUmic.get(iteracion-1).getBloqueBySubproceso(codSubProceso),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, codSubProceso);
					if (((String) mapVariables.get("VRTAMIX_MOD")).equals("VZCIERTA")) {
						varVzc1 = BigDecimal.ONE;
						varVzc1_r = BigDecimal.ONE;
					}
					flujoY = cuantia.multiply(varRever).multiply(BigDecimal.ONE.subtract(varVzc1));
				} else {
					flujoY = cuantia.multiply(varRever).multiply(BigDecimal.ONE.subtract(varVzc1.divide(difercol_rr, ConstantsFunciones.MATH_CONTEXT)));
				}
				
			}
			
			if (varModFall.equals(ConstantsFactorias.MODULO_VARBVIT)
					|| varModFall.equals(ConstantsFactorias.MODULO_VARB167)
					|| varModFall.equals(ConstantsFactorias.MODULO_VARBFPC)) {
				flujoY = cuantia.multiply(BigDecimal.ONE.subtract(varVzc1));
			}
			
			if (varModFall.equals(ConstantsFactorias.MODULO_VARBVIT) 
					&& umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_2_STRING)
					&& umic.getOtrosDatos().getCestadoAseg1().equals("A")) {
				flujoY = cuantia;
			}
			
			if (varModFall.equals(ConstantsFactorias.MODULO_VZC412)) {
				Modulo mVzcierta = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZCIERTA);
				BigDecimal auxVzcierta = (BigDecimal) mVzcierta.execute(proyUmic, proyUmic.get(iteracion-1).getBloqueBySubproceso(codSubProceso),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, codSubProceso);
				Modulo mFptozc = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FPTOZC);
				BigDecimal auxFptozc = (BigDecimal) mFptozc.execute(proyUmic, proyUmic.get(iteracion-1).getBloqueBySubproceso(codSubProceso),iteracion, fcalc2, umic, btcUmic, mapVariables, codSubProceso);
				
				flujoY = cuantia.multiply(BigDecimal.ONE.subtract(auxVzcierta.multiply(auxFptozc)));
			}
			
			if (varModFall.equals(ConstantsFactorias.MODULO_VRTAMIX)) {
				int iter;
				factProbY = varVzc2.multiply(difercol_rr);
				Modulo mVrtamix = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VRTAMIX);
				BigDecimal auxVratamix = (BigDecimal) mVrtamix.execute(proyUmic, proyUmic.get(iteracion-1).getBloqueBySubproceso(codSubProceso),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, codSubProceso);
				Timestamp varFinRvc = (Timestamp) mapVariables.get("VRTAMIX_finrvc");
				varFinRvc =  UtilFechas.getUltimoDiaDelMes(UtilFechas.plusMeses(varFinRvc, 1));
				if (proyUmic.get(0).getBloqueBySubproceso(codSubProceso).getFechaDevengo() == null) {
					iter = 0;
				} else {
					iter = FuncionesAuxiliares.tcm(proyUmic.get(0).getBloqueBySubproceso(codSubProceso).getFechaDevengo(), varFinRvc);
				}
				
				BigDecimal difercol_x = (BigDecimal) mVzc1.execute(proyUmic, proyUmic.get(iter).getBloqueBySubproceso(codSubProceso),iter+1, fcalc2, umic, btcUmic, mapVariables, codSubProceso);

				if (((String) mapVariables.get("VRTAMIX_MOD")).equals("VZCIERTA")) {
					factProb = BigDecimal.ONE;
				} else {
					factProb = varVzc1_r.multiply(difercol_x);
				}
			} else {
				factProb = varVzc1_r;
			}
			
			if (codSubProceso.equals(ConstantsModulos.CTE_PROY_FALL) 
					&& varModFall.equals(ConstantsFactorias.MODULO_FPTOZC)
					|| varModFall.equals(ConstantsFactorias.MODULO_VZC412)) {
				flujoX = cuantia.multiply(BigDecimal.ONE.subtract(varVzc1_r));
				vx = flujoX.add(vxant.multiply(factProb).multiply(varAct));
			} else if (codSubProceso.equals(ConstantsModulos.CTE_PROY_FALL) && varModFall228.equals(ConstantsFactorias.MODULO_FPTOZC)) {
				flujoX = BigDecimal.ONE.subtract(varVzc1_r).multiply(varAct);
				vx = flujoX.add(vxant.multiply(factProb).multiply(varAct));
			} else {
				if (varModFall.equals(ConstantsFactorias.MODULO_VRTAMIX)) {
					flujoX = cuantia.multiply(factProb).multiply(varAct);
					vx = flujoX.add(vxant.multiply(varVzc1_r).multiply(varAct));
				} else {
					flujoX = cuantia.multiply(varVzc1_r).multiply(varAct);
					vx = flujoX.add(vxant.multiply(factProb).multiply(varAct));
				}
				
			}
			
			factProbY = varVzc2_r.multiply(difercol_rr);
			mapVariables.put("VZCREVER_VXANT" + codSubProceso, vx);
			if (varVzc2_r.compareTo(BigDecimal.ZERO) != 0
					|| varModFall.equals(ConstantsFactorias.MODULO_VZC412)) {
				vy = flujoY.add(vyant.multiply(varVzc2_r).multiply(varAct));
			} else {
				vy = BigDecimal.ZERO;
			}
			
			if (varModFall.equals(ConstantsFactorias.MODULO_VARB167)) {
				Modulo mVzcierta = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZCIERTA);
				BigDecimal auxVzcierta = (BigDecimal) mVzcierta.execute(proyUmic, proyUmic.get(iteracion-1).getBloqueBySubproceso(codSubProceso),iteracion, proyUmic.get(iteracion-1).getFechaDesde(), umic, btcUmic, mapVariables, codSubProceso);
				vy = flujoY.add(vyant.multiply(auxVzcierta).multiply(varAct));
			}
			
			if (codSubProceso.equals(ConstantsModulos.CTE_PROY_FALL)
					&& !varModFall.equals(ConstantsFactorias.MODULO_VZC412)) {
				vy = BigDecimal.ZERO;
			}
			
			mapVariables.put("VZCREVER_VYANT" + codSubProceso, vy);
			vxy = vx.add(vy);
			
		}
		
		if (null == fcalc) {
			vyant = (BigDecimal) mapVariables.get("VZCREVER_VYANT" + codSubProceso);
			if (vyant == null) {
				vyant = BigDecimal.ZERO;
			}
			vxant = (BigDecimal) mapVariables.get("VZCREVER_VXANT" + codSubProceso);	
			if (vxant == null) {
				vxant = BigDecimal.ZERO;
			}
			vxy = vxant.add(vyant);
		}
		
		if (ModuloVZREVER2.LOG.isTraceEnabled()) {
			ModuloVZREVER2.LOG.trace("Fin función << moduloVZREVER2 >> de la clase ModuloVZREVER2, para la iteracion = {}, con resultado vzrever2 = {}", iteracion, vzrever);
		}
		
		vzrever = vxy;
		return vzrever;
	}
	
}