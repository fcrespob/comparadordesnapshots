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
import es.mapfre.solvencia.dominio.salidaCalculo.TotalFlujoProyeccion;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.FuncionesGastos;
import es.mapfre.solvencia.formulacion.util.FuncionesPrimas;
import es.mapfre.solvencia.formulacion.util.FuncionesVBX;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo que devuelve el importe nominal para la proyección de Provisión Matemática por Fórmula Cerrada.
 * La expresión matemática para su determinación es la siguiente:
 * 		VBX308 = ICAPACT * OBFUTADORC(fcalc) + PUCCAP(fcal) * OBFUTADORPRIM(fcal) + PU*GIPC*(GASTGI(FCAL)/GASTGINI)
 * 
 * @author apedro
 *
 */
public class ModuloVBX308 implements Modulo {

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVBX308.class);
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VBX308;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_MODBETA = ConstantsModulos.CTE_VA_MODBETA.concat(CLAVE_MODULO);
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	private static final String CLAVE_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT;
	private static final String CLAVE_VAR_VAL_TAB_MORT = CLAVE_VAL_TAB_MORT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_ANT_RENOVA = ConstantsModulos.CTE_VAR_ANT_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PROX_RENOVA = ConstantsModulos.CTE_VAR_PROX_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_NR = ConstantsModulos.CTE_VAR_NR.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_X= ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TC0= ConstantsModulos.CTE_VAR_TC0.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCY= ConstantsModulos.CTE_VAR_TCY.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_W = ConstantsModulos.CTE_VAR_W.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_GASTGINI = ConstantsModulos.CLAVE_VAR_GASTGINI.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
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
			if (ModuloVBX308.LOG.isTraceEnabled()) {
				ModuloVBX308.LOG.trace("Inicio de execute en clase ModuloVBX308");
			}
			
			//Inicio recuperación de datos que le pasaremos a la función moduloVBX308
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			//Fin de la recuperación de los datos que se pasarán a la función moduloVBX308.
			
			//Invocamos a la función de calculo VBX308
			resultado = moduloVBX308(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVBX308.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVBX308.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloVBX308.LOG.isTraceEnabled()) {
			ModuloVBX308.LOG.trace("Fin de execute en clase ModuloVBX308");
		}
		
		return resultado;

	}

	/** 
	 * Modulo de cálculo que devuelve el importe nominal para la proyección de Provisión Matemática por Fórmula Cerrada.
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
	 */
	private TotalFlujoProyeccion moduloVBX308(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		String varCriterFecha = ConstantsFunciones.CTE_CADENA_VACIA;
		String varBeta = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriterEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		String varModProb;
		String varModNomFall;
		BigDecimal varI1Bti;
		BigDecimal varI2Bti;
		BigDecimal varPU;
		BigDecimal varICapAct;
		BigDecimal varM;
		Timestamp varfecvcto;
		BigDecimal varN;
		Integer varNR;
		Integer varNRM;
		Timestamp varAntRenova;
		Timestamp varProxRenova;
		Timestamp varFrpsgact; 
		BigDecimal varX;
		Integer varW; 
		BigDecimal varI1;
		BigDecimal varI2;
		List<BigDecimal> varValoresTabMort;
		Integer varTc0;
		BigDecimal varTCy;
		BigDecimal varGipc;
		BigDecimal varGic;
		BigDecimal varB1 = BigDecimal.ZERO;
		BigDecimal varB2 = BigDecimal.ZERO;
		Timestamp varAniversario;
		Timestamp varFechaEfecto = null;
		BigDecimal varPartAnoNR;
		BigDecimal vary;
		BigDecimal varRy;
		BigDecimal varPUCCAPcol = BigDecimal.ZERO;
		BigDecimal varObfutadorprim = BigDecimal.ZERO;
		BigDecimal varGastGi = BigDecimal.ZERO;
		BigDecimal varGastgini = BigDecimal.ONE;
		BigDecimal varObfutador = BigDecimal.ZERO;
		final TotalFlujoProyeccion salida = new TotalFlujoProyeccion();
		BigDecimal vbx308 = BigDecimal.ZERO;
		//Fin variables locales
		
		if (ModuloVBX308.LOG.isTraceEnabled()) {
			ModuloVBX308.LOG.trace("Inicio función << moduloVBX308 >> de la clase ModuloVBX308, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		
		// Cálculo y validación de las variables de apoyo.  
		varCriterFecha = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);	
		varBeta = UtilModulos.getsetStringRecuperarDefinicionAuxiliar(mapVariables, CLAVE_VAR_MODBETA, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), ConstantsModulos.CTE_VA_MODBETA);
		varCriterEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFecha);
			ValidacionesComunesModulos.validarVariableDeApoyoVarModBeta(varBeta);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterEdad);
			// Fin de la validación de las variables de apoyo.
		}
		
		//Variables Modulo
		IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		varModProb = servicio.recuperarModulo(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), umic.getDatosAdicionales().getPrestCal(), btcUmic.getBt(), ConstantsModulos.CTE_PROY_VIDA, "02");
		varModNomFall = servicio.recuperarModulo(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), umic.getDatosAdicionales().getPrestCal(), btcUmic.getBt(), ConstantsModulos.CTE_PROY_FALL, "01");

		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		varI1Bti = umic.getBti().getPintertecnI1();
		varI2Bti = umic.getBti().getPintertecnI2();
		BigDecimal varI1Btipct = varI1Bti.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		BigDecimal varI2Btipct = varI2Bti.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		varPU = umic.getPrimas().getIprimatarada();
		varICapAct = umic.getCapitales().getIcapact();
		varM = FuncionesAuxiliares.nAnnos(varFechaEfecto, umic.getBti().getFecFinTramo1(), varCriterFecha);
		
		varfecvcto = umic.getFechas().getFecefecfin();
		if (varfecvcto == null){
			varfecvcto = proyUmic.get(proyUmic.size()-1).getFechaDesde();
		}
		
		varN = FuncionesAuxiliares.nAnnos(varFechaEfecto, varfecvcto, varCriterFecha);
		varNRM = varM.intValue();

		
		varAntRenova = UtilModulos.getVarAntRenova(mapVariables, CLAVE_VAR_ANT_RENOVA, umic.getFechas().getFecdesderenova());
		varProxRenova = UtilModulos.getVarProxRenova(mapVariables, CLAVE_VAR_PROX_RENOVA, umic.getFechas().getFechastarenova());
		
		varFrpsgact = varProxRenova;
		
		varNR = UtilModulos.getVarNR(mapVariables, CLAVE_VAR_NR, umic.getDuraciones().getNrenovaciones());
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriterEdad, umic.getRentas().getFecIni(), varEdifer);
		varI1 = btcUmic.getItcalc().get(0);
		varI2 = btcUmic.getItcalc().get(1);
		BigDecimal numi1PorcentajeMasUno = UtilModulos.getNumPorcentajeMasUno(varI1, mapVariables, CLAVE_MODULO);
		BigDecimal numi2PorcentajeMasUno = UtilModulos.getNumPorcentajeMasUno(varI2, mapVariables, CLAVE_MODULO);
		
		if (varModProb.equals(ConstantsFactorias.MODULO_VZCIERTA)){
			varValoresTabMort = Util.listaProbUno(ConstantsFunciones.CTE_130);
		} else {
			varValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterEdad, ConstantesSolvencia.CTE_TABMORT_L);
		}
		
		varW = UtilModulos.getVarEdadMaxima2(mapVariables, CLAVE_VAR_W, UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1()), umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1);
		varTc0 = UtilModulos.getVarTC0(mapVariables, CLAVE_VAR_TC0, varFechaEfecto, fcalc);
		varTCy = UtilModulos.getVarTCy(mapVariables, CLAVE_VAR_TCY, varFechaEfecto, fcalc, varCriterFecha);
		varGipc = btcUmic.getGtorosspPrima();
		varGic = (btcUmic.getGtorosspCap()).multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		
		
		//VarBeta - indica que tipo de modalidad es para calcular varB1 y varB2
		if (varBeta.equals("CRP")){
			varB1 = varI1Btipct;
			varB2 = varI2Btipct;
		} else if (varBeta.equals("CRPI")){
			varB1 = BigDecimal.ZERO;
			varB2 = BigDecimal.ZERO;
		} else if (varBeta.equals("SR")){
			varB1 = varI1Btipct.add(BigDecimal.ONE);
			varB2 = varI2Btipct.add(BigDecimal.ONE);
		}
		
		//Fin Variables Modulo
		
		
		Timestamp varfecJ = proyUmic.get(iteracion-1).getFechaDesde();
		varTc0 = FuncionesAuxiliares.tc(varFechaEfecto, varfecJ);
		BigDecimal varTCyJ = FuncionesAuxiliares.nAnnos(varFechaEfecto, varfecJ, varCriterFecha);
		BigDecimal varZc = varX.add(varTCyJ);
		boolean varUltRenova = false;
		
		if (!varfecJ.before(varProxRenova)){
			varAntRenova = varProxRenova;
			varProxRenova = UtilFechas.incrAnyo(varProxRenova, 1);
			varNR = varNR + 1;
			varFrpsgact = varProxRenova;
			
			mapVariables.put(CLAVE_VAR_ANT_RENOVA, varAntRenova);
			mapVariables.put(CLAVE_VAR_PROX_RENOVA, varProxRenova);
			mapVariables.put(CLAVE_VAR_NR, varNR);
		}
		
		Integer numAnios = FuncionesAuxiliares.nAnnos(varFechaEfecto, varfecJ, varCriterFecha).intValue();
		varAniversario = UtilFechas.incrAnyo(varFechaEfecto, numAnios);
		vary = UtilModulos.getVarPartAnoNRYvarY(varfecJ, varAniversario, varCriterFecha);
		
		if (varNR == 0){
			varPartAnoNR = UtilModulos.getVarPartAnoNRYvarY(umic.getFechas().getFechastarenova(), varFechaEfecto, varCriterFecha);
			varRy = UtilModulos.getVarPartAnoNRYvarY(varfecJ, varFechaEfecto, varCriterFecha);
		} else {
			if (!varAntRenova.after(varfecvcto) && UtilFechas.getAnio(varProxRenova) == UtilFechas.getAnio(varfecvcto)){
				varUltRenova = true;
				if(!varfecJ.after(varProxRenova)){
					varPartAnoNR = UtilModulos.getVarPartAnoNRYvarY(varProxRenova, varfecJ, varCriterFecha);
				} else {
					varPartAnoNR = UtilModulos.getVarPartAnoNRYvarY(varfecvcto, varfecJ, varCriterFecha);
				}
			} else {
				varPartAnoNR = BigDecimal.ONE;
			}
			
			varRy = UtilModulos.getVarPartAnoNRYvarY(varfecJ, varAntRenova, varCriterFecha);
		}
		
		BigDecimal varRen = BigDecimal.valueOf(varTc0).add(vary).add(varPartAnoNR).subtract(varRy);
		Integer varNDap = varN.subtract(BigDecimal.valueOf(varTc0)).subtract(vary).subtract(varPartAnoNR).add(varRy).intValue();
		BigDecimal varNDmv;
		
		if (varNDap < 0){
			varNDap = 0;
		} 
		
		if (!varUltRenova){
			varNDmv = (varN.subtract(varTCyJ).subtract(varPartAnoNR).add(varRy).subtract(BigDecimal.valueOf(varNDap))).setScale(13, RoundingMode.HALF_DOWN);
		} else {
			varNDmv = varPartAnoNR;
		}
		
		
		
		BigDecimal divisionGastg = BigDecimal.ZERO;
		if (varModNomFall != null){
			if (!varBeta.equals(ConstantsModulos.CTE_SR)){
				varPUCCAPcol = FuncionesVBX.puccapcol(varPU, varN, varI1Btipct, varI2Btipct, varNR, varNRM, varM, varB1, varB2, varfecJ, umic.getBti().getFecFinTramo1(), mapVariables, CLAVE_MODULO);
			}
			
			if (varPUCCAPcol.signum() != 0){
				varObfutadorprim = FuncionesPrimas.obfutadorprim(fcalc, varProxRenova, varFrpsgact, varfecvcto, varfecJ, varPartAnoNR, varRy, varI1, varI2, varM, varN, varTCy, varValoresTabMort, varX, varZc, varNDap, varNDmv, varBeta, varB1, varB2, varI1Bti, varI2Bti, varRen, varTCyJ, numi1PorcentajeMasUno, numi2PorcentajeMasUno);
			}
		}	
		
			if (varGipc.signum() != 0){
				varGastGi = FuncionesGastos.gastgi(varfecJ, varFechaEfecto, varfecJ, varPartAnoNR, varRy, varRen, varI1, varI2, varM, varTCyJ, varCriterFecha, varValoresTabMort, BigDecimal.valueOf(varW), varX, varZc, varNDap, varNDmv, numi1PorcentajeMasUno, numi2PorcentajeMasUno);
				varGastgini = (BigDecimal) mapVariables.get(CLAVE_VAR_GASTGINI);
				if (varGastgini == null){
					varGastgini = FuncionesGastos.gastgini(proyUmic, varfecJ, umic, btcUmic, codSubproceso, mapVariables, numi1PorcentajeMasUno, numi2PorcentajeMasUno);
					mapVariables.put(CLAVE_VAR_GASTGINI, varGastgini);
				}
				
				divisionGastg = varGastGi.divide(varGastgini, ConstantsFunciones.MATH_CONTEXT); // varGastgi()/varGastgini()
			}
			
			if (varICapAct.signum() != 0){
				if (varGipc.signum() == 0){
					varGastGi = FuncionesGastos.gastgi(varfecJ, varFechaEfecto, varfecJ, varPartAnoNR, varRy, varRen, varI1, varI2, varM, varTCyJ, varCriterFecha, varValoresTabMort, BigDecimal.valueOf(varW), varX, varZc, varNDap, varNDmv, numi1PorcentajeMasUno, numi2PorcentajeMasUno);
				}
				varObfutador = FuncionesVBX.obfutadorc(varfecJ, varFechaEfecto, varI1, varI2, varM, varTCyJ, varCriterFecha, varValoresTabMort, BigDecimal.valueOf(varW), varX, varN, varGic, varGastGi, numi1PorcentajeMasUno, numi2PorcentajeMasUno);
			}
		
		
		
		//Se calcula VBX308 como VBX308(j) = ( variCapAct * varObfutadorc )+ (varPUCCAPcol * varObfutadorprim )   + (varPU* (varGipc/100)* (varGastgi()/varGastgini())
		BigDecimal gipcEntre100 = varGipc.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01); //(varGipc/100)
		BigDecimal primerOp = varICapAct.multiply(varObfutador); // variCapAct * varObfutadorc
		BigDecimal segundoOp = varPUCCAPcol.multiply(varObfutadorprim); //varPUCCAPcol * varObfutadorprim
		
		vbx308 = (primerOp).add(segundoOp).add(varPU.multiply(gipcEntre100).multiply(divisionGastg));
		
		
		//Se retornan también los terminales
		salida.setProvbtiproy(vbx308);
		salida.setTerminalAnterior(BigDecimal.ZERO);
		salida.setTerminalPosterior(BigDecimal.ZERO);
		
		if (ModuloVBX308.LOG.isTraceEnabled()) {
			ModuloVBX308.LOG.trace("Fin función << moduloVBX308 >> de la clase ModuloVBX308, para la iteracion = {}, con resultado vbx308 = {}", iteracion, vbx308);
		}
		
		return salida;
	}
	
}
