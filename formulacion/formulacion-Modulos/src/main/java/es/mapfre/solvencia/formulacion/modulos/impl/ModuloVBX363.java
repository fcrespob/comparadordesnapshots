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
import es.mapfre.solvencia.dominio.salidaCalculo.TotalFlujoProyeccion;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.FuncionesFallecimiento;
import es.mapfre.solvencia.formulacion.util.FuncionesGastos;
import es.mapfre.solvencia.formulacion.util.FuncionesRentas;
import es.mapfre.solvencia.formulacion.util.FuncionesVBX;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilFechas.Fecha;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo VBX363.
 * La expresión matemática para su determinación es la siguiente:
 * 				
 * @author apedro
 *
 */
public class ModuloVBX363 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVBX363.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VBX363;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
		
	private static final String CLAVE_VAR_W = ConstantsModulos.CTE_VAR_W.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_M = ConstantsModulos.CTE_VAR_M.concat(CLAVE_MODULO);
	//private static final String CLAVE_VAR_TCY = ConstantsModulos.CTE_VAR_TCY.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_EDAD_CALC = ConstantsModulos.CTE_VAR_EDAD.concat(CLAVE_MODULO);

	private static final String CLAVE_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT;
	private static final String CLAVE_VAR_VAL_TAB_MORT = CLAVE_VAL_TAB_MORT.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_EDAD_MAXIMA_2 = ConstantsModulos.CTE_VAR_EDAD_MAXIMA_2.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_FECHA_VCTO = ConstantsModulos.CTE_VAR_FECHA_VCTO.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ANT_RENOVA = ConstantsModulos.CTE_VAR_ANT_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PROX_RENOVA = ConstantsModulos.CTE_VAR_PROX_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_NR = ConstantsModulos.CTE_VAR_NR.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	private static final String CLAVE_PERIODOS_FALL = "periodosFall".concat(CLAVE_MODULO);
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
			
			if (ModuloVBX363.LOG.isTraceEnabled()) {
				ModuloVBX363.LOG.trace("Inicio de execute en clase ModuloVBX363");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloVBX363
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
			
			//Invocamos a la función de calculo VBX363
			resultado = moduloVBX363(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVBX363.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVBX363.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloVBX363.LOG.isTraceEnabled()) {
			ModuloVBX363.LOG.trace("Fin de execute en clase ModuloVBX363");
		}
		
		return resultado;
	}
	
	/**
	 * Módulo que calcula el importe nominal para la proyección de Provisión Matemática por Fórmula Cerrada
	 *  La expresión matemática para su determinación es la siguiente:
	 * 				VBX363 = RENTA + FALL + GASTO
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
	private TotalFlujoProyeccion moduloVBX363(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables,
			final String codSubproceso) {		
		//Variables locales
		BigDecimal vbx363 = BigDecimal.ZERO;
		final TotalFlujoProyeccion salida = new TotalFlujoProyeccion();
		String varCriFec;
		String varCriEdad;
		BigDecimal varPu;
		Integer varNR;
		BigDecimal varI1;
		BigDecimal varI2;
		BigDecimal varI1PorcentajeMasUno;
		BigDecimal varI2PorcentajeMasUno;
		int varAnoNac;
		int varW = 0;
		BigDecimal varGipc;
		BigDecimal varGic;
		BigDecimal varM = BigDecimal.ZERO;
		int varTc0;
		BigDecimal varTcy = BigDecimal.ZERO;
		BigDecimal varTcyVida = BigDecimal.ZERO;
		BigDecimal varRy = BigDecimal.ZERO;
		BigDecimal vary = BigDecimal.ZERO;
		BigDecimal varRen = BigDecimal.ZERO;
		BigDecimal varPartAnoNR = BigDecimal.ZERO;
		Timestamp varfecJ;
		Timestamp varfcalcJMenos1Dia;
		BigDecimal varZc;
		BigDecimal varGastivit = BigDecimal.ZERO;
		BigDecimal varGastgivitini = BigDecimal.ONE;
		List<BigDecimal> lstTabMort = null;
		Timestamp varAntRenova = null;
		Timestamp varProxRenova = null;
		Timestamp varfecvcto = null;
		Timestamp varAniversario;
		Timestamp varIniRenta;
		BigDecimal varEdadCalc1;
		BigDecimal varActjini363;
		BigDecimal varLxIni;
		BigDecimal varLyIni;
		Integer varn = 0;
		Timestamp varFechaEfecto = null;
		//Fin variables locales
		
		
		if (ModuloVBX363.LOG.isTraceEnabled()) {
			ModuloVBX363.LOG.trace("Inicio función << moduloVBX363 >> de la clase ModuloVBX363, para la iteracion = {}", iteracion);
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
		 */

		
		//INICIO DE VALORES QUE NO CAMBIAN POR ITERACION
		// Se definen estas variables auziliares dado que se llaman varias veces.
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		
		// Cálculo y validación de las variables de apoyo.
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
		varCriEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);

		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriEdad);
		}// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		// Fin de la validación de las variables de apoyo.
		
		
		//Variables módulo
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		if (ConstantsModulos.CTE_DG_CSI_NO_RED.equals(umic.getDatosGenerales().getCsitupol())) {
			varn = umic.getDuraciones().getNdursegano();			
		} else if (ConstantsModulos.CTE_DG_CSITU_RED.equals(umic.getDatosGenerales().getCsitupol())){
			varn = FuncionesAuxiliares.tc(umic.getFechas().getFecefecfin(), varFechaEfecto);
		}		
		
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		BigDecimal varX363 = FuncionesAuxiliares.nEdad(varFechaEfecto, umic.getAsegurados().getFnacAseg1(), ConstantsFunciones.CTE_CRI_FECHA_06, umic.getRentas().getFecIni(), varEdifer);
		BigDecimal varY363 = null;
		if (umic.getAsegurados().getFnacAseg2()!=null){
			varY363 = FuncionesAuxiliares.nEdad(varFechaEfecto, umic.getAsegurados().getFnacAseg2(), ConstantsFunciones.CTE_CRI_FECHA_06, umic.getRentas().getFecIni(), varEdifer);
		}
		
		varPu = umic.getPrimas().getIprimanetaini();
		if (null == btcUmic.getGtorosspPrima()) {
			btcUmic.setGtorosspPrima(BigDecimal.ZERO);
		}
		varGipc = btcUmic.getGtorosspPrima().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		varM = UtilModulos.getVarM(mapVariables, CLAVE_VAR_M, varFechaEfecto, umic.getBti().getFecFinTramo1(), varCriFec);
		varAntRenova = UtilModulos.getVarAntRenova(mapVariables, CLAVE_VAR_ANT_RENOVA, umic.getFechas().getFecdesderenova());
		varProxRenova = UtilModulos.getVarProxRenova(mapVariables, CLAVE_VAR_PROX_RENOVA, umic.getFechas().getFechastarenova()) ;
		varNR = (Integer) mapVariables.get(CLAVE_VAR_NR);
		if (varNR == null){
			varNR = umic.getDuraciones().getNrenovaciones();
			mapVariables.put(CLAVE_VAR_NR, varNR);
		}
		if (umic.getRentas().getCpagrenta().equals(ConstantsModulos.CTE_CFORMPAG_UNICA) && umic.getRentas().getFecIni() == null){
			varIniRenta = varFechaEfecto;
		}else{
			varIniRenta = umic.getRentas().getFecIni();
		}
		varI1 = btcUmic.getItcalc().get(0);
		varI2 = btcUmic.getItcalc().get(1);
		varI1PorcentajeMasUno = UtilModulos.getNumPorcentajeMasUno(varI1, mapVariables, CLAVE_MODULO);
		varI2PorcentajeMasUno = UtilModulos.getNumPorcentajeMasUno(varI2, mapVariables, CLAVE_MODULO);
		varAnoNac = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1());
		//varTcy = UtilModulos.getVarTCy(mapVariables, CLAVE_VAR_TCY, varFecIniSusc, fcalc, varCriFec);
		varTcy = FuncionesAuxiliares.nAnnos(umic.getFechas().getFecinisus(), proyUmic.get(iteracion-1).getFechaDesde(), varCriFec);
		lstTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriEdad, ConstantesSolvencia.CTE_TABMORT_L);
		varEdadCalc1 = UtilModulos.getVarX(mapVariables, CLAVE_VAR_EDAD_CALC, varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriEdad, umic.getRentas().getFecIni(), varEdifer);
		Integer varW1= UtilModulos.getVarEdadMaxima2(mapVariables, CLAVE_VAR_W, varAnoNac, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1);
		if (umic.getRentas().getTempVit().equals(ConstantsModulos.CTE_RENTA_TEMPORAL)){
			varW = varEdadCalc1.intValue() + varn;
		}else if (umic.getRentas().getTempVit().equals(ConstantsModulos.CTE_RENTA_VITALICIA)){
			varW = varW1;
		}else{
			if (LOG.isDebugEnabled()) {
				LOG.debug(Util.errorValidacionA2(umic.getRentas().getTempVit(), ConstantsFunciones.CTE_TEMP_VIT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A2, new String[]{ConstantsFunciones.CTE_TEMP_VIT, umic.getRentas().getTempVit()}); 
		}
		ValidacionesComunesModulos.validarEdadMax(varW, umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia());
		
		varGic = btcUmic.getGtorosspCap().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		varfecvcto = UtilModulos.getVarFecVcto(mapVariables, CLAVE_VAR_FECHA_VCTO, umic, btcUmic, varW, varCriEdad, CLAVE_VAR_EDAD_MAXIMA_2);
		varAniversario = varFechaEfecto;
		//Fin variables módulo
		
		
		//Para cualquier periodo j se calcula:
		
		varfecJ = proyUmic.get(iteracion-1).getFechaDesde();
		varfcalcJMenos1Dia = UtilFechas.decreDias(varfecJ, 1);
		varTc0 = FuncionesAuxiliares.tc(varFechaEfecto, varfcalcJMenos1Dia);
		varZc = varEdadCalc1.add(FuncionesAuxiliares.nAnnos(varFechaEfecto, varfecJ, varCriFec)); 
		varTcyVida = FuncionesAuxiliares.nAnnos(varFechaEfecto, varfecJ, varCriFec);
		
		if (varAntRenova.before(varfecJ) && !varProxRenova.after(varfecJ)) {
			varAntRenova = varProxRenova;
			varProxRenova = UtilFechas.incrAnyo(varProxRenova, ConstantsModulos.CTE_INT_1);
			//Guardamos en memoria las fechas modificadas
			mapVariables.put(CLAVE_VAR_ANT_RENOVA, varAntRenova);
			mapVariables.put(CLAVE_VAR_PROX_RENOVA, varProxRenova);
			
			varNR = varNR + 1;
			mapVariables.put(CLAVE_VAR_NR, varNR);
		}
		
		varAniversario = UtilFechas.incrAnyo(varFechaEfecto, FuncionesAuxiliares.nAnnos(varFechaEfecto, varfecJ, varCriFec).intValue());
		
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
		
		//Se calculan varRy y varPartAnoNR según varNR
		Timestamp fechaDesdeRenovacion = umic.getFechas().getFecdesderenova();
		Timestamp fechaHastaRenovacion = umic.getFechas().getFechastarenova(); 
		
		if (varNR == 0) {
			varRy = getVarPartAnoNRVariableYVarRyVariable(varfecJ, varFechaEfecto, varCriFec);
			
			 if (varFechaEfecto.after(fechaDesdeRenovacion) && 
					 (fechaDesdeRenovacion.before(varIniRenta)  &&  !fechaHastaRenovacion.before(varIniRenta)) ) {
				 if (varfecJ.before(varIniRenta)){ 
					 varPartAnoNR =  getVarPartAnoNRVariableYVarRyVariable(varIniRenta, varFechaEfecto, varCriFec);
				 }else{
					 varPartAnoNR =  getVarPartAnoNRVariableYVarRyVariable(fechaHastaRenovacion, varIniRenta, varCriFec);
				 }
			 } else {
				 varPartAnoNR =  getVarPartAnoNRVariableYVarRyVariable(fechaHastaRenovacion, varFechaEfecto, varCriFec);
			 }
		} else {
			varRy = getVarPartAnoNRVariableYVarRyVariable(varfecJ, varAntRenova, varCriFec);
			
			Fecha calFechaProxRenovacion = UtilFechas.getFecha(varProxRenova);
			Fecha calFechaVencimiento = UtilFechas.getFecha(varfecvcto);
			
			 if (varAntRenova.before(varIniRenta) && !varProxRenova.before(varIniRenta)) {
				 if (varfecJ.before(varIniRenta)){
					 varPartAnoNR = getVarPartAnoNRVariableYVarRyVariable(varIniRenta, varAntRenova, varCriFec);
				 }else{
					 varPartAnoNR =  getVarPartAnoNRVariableYVarRyVariable(fechaHastaRenovacion, varIniRenta, varCriFec);
				 } 
			 } else if (!varfecvcto.before(varAntRenova) && calFechaProxRenovacion.getAnio()== calFechaVencimiento.getAnio()) {
				 if (!varProxRenova.before(varfecJ)) {
					 varPartAnoNR = getVarPartAnoNRVariableYVarRyVariable(varProxRenova, varfecJ, varCriFec);
				 } else {
					 varPartAnoNR = getVarPartAnoNRVariableYVarRyVariable(varfecvcto, varfecJ, varCriFec);
				 }
			 } else {
				 varPartAnoNR = BigDecimal.ONE;
			 }
		}


		varRen = BigDecimal.valueOf(varTc0).add(vary).add(varPartAnoNR).subtract(varRy);
		
		if (!varGipc.equals(BigDecimal.ZERO)){
			// Llamada a gastgivit
			varGastivit = FuncionesGastos.gastgivit(fcalc, varFechaEfecto, varProxRenova, varfecJ, 
					varPartAnoNR, varRy, varRen, varI1, varI2, varM, varTcy, varCriFec,  
					lstTabMort, BigDecimal.valueOf(varW), varEdadCalc1, varZc,
					varI1PorcentajeMasUno, varI2PorcentajeMasUno);
			
			// Llamada a gastgivitini
			varGastgivitini = UtilModulos.getVarGastgivitini(mapVariables, CLAVE_MODULO,
					proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso,
					varI1PorcentajeMasUno, varI2PorcentajeMasUno);
		}
		
		//Se calcula varEdadXIni segun varActjini363. Cuando varActjini363 es 0, varEdadXIni y varEdadYini son igual a varX363 y varY363 respectivamente
		BigDecimal varEdadXIni;
		BigDecimal varEdadYini=null;
		boolean actjini363Es0 = false;
		if (varfecJ.before(umic.getRentas().getFecIni())){
			varActjini363 = BigDecimal.ZERO;
			varEdadXIni = varX363;
			varEdadYini = varY363;
			actjini363Es0 = true;
		}else{
			varActjini363 = FuncionesAuxiliares.nAnnos(umic.getRentas().getFecIni(), varfecJ, varCriFec);
			varEdadXIni = varX363.add(varActjini363);
		}
		
		//Si varEdadXIni>=varW1, se ha llegado al final de la tabla de mortalidad y por lo tanto varLxIni es 0
		if (varEdadXIni.compareTo(BigDecimal.valueOf(varW1))>=0){
			varLxIni = BigDecimal.ZERO;
		}else{
			varLxIni = Util.getVarLx(varEdadXIni, lstTabMort);
		}
		
		//Se calcula varLyIni cuando varY363 no es null. 
		varLyIni = null;
		if (varY363 != null){
			if (!actjini363Es0){
				varEdadYini = varY363.add(varActjini363);
			}
			varLyIni = Util.getVarLx(varEdadYini, lstTabMort);
		}
		
		//Si varY363 es null se envía este valor a renta y fall, sino se envía el entero de varY363
		Integer Y363;
		if (varY363 == null){
			Y363 = null;
		}else{
			Y363 = varY363.intValue();
		}
		
		//Se llama a las funciones necesarias para el cálculo de vbx363
		BigDecimal varRenta = FuncionesRentas.renta(proyUmic, bloqueCorriente, umic, btcUmic, varX363.intValue(), Y363, varLxIni, varLyIni, varTcyVida, varM, lstTabMort, iteracion, varCriFec);

		BigDecimal varFall = FuncionesFallecimiento.fall(proyUmic, bloqueCorriente, umic, btcUmic, varX363.intValue(), Y363, varLxIni, varLyIni, varM, lstTabMort, iteracion, varCriFec, mapVariables, CLAVE_PERIODOS_FALL);
		
		BigDecimal varGasto = FuncionesGastos.gasto(varRenta, varGic, varPu, varGipc, varGastivit, varGastgivitini);
		
		
		//Se realiza el cálculo de VBX363(j) = varRenta + varFall + varGasto
		vbx363 = varRenta.add(varFall).add(varGasto);

		//LOG.warn(varfecJ+";"+varRenta+";"+varFall+";"+varGasto+";"+vbx363);
		
		/**
		 * Se retornarán también los terminales, ceros en este caso:
		 * 		terminalAnterior = 0
		 * 		terminalPosterior= 0
		 */
		salida.setProvbtiproy(vbx363);
		salida.setTerminalAnterior(BigDecimal.ZERO);
		salida.setTerminalPosterior(BigDecimal.ZERO);
		
		
		if (ModuloVBX363.LOG.isTraceEnabled()) {
			ModuloVBX363.LOG.trace("Fin función << moduloVBX363 >> de la clase ModuloVBX363, para la iteracion = {}, con resultado vbx363 = {}", iteracion, vbx363);
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
		resultado = BigDecimal.valueOf((operando1 - operando2)).multiply(ConstantsFunciones.CTE_OPER_1_PARTIDO_365);

		return resultado;
	}

}
