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
import es.mapfre.solvencia.formulacion.util.FuncionesVBX;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo VBX462. Módulo que calcula el importe nominal para la proyección de Provisión Matemática 
 * por Fórmula Cerrada 
 * 				
 * @author ogperez
 *
 */
public class ModuloVBX462 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVBX462.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VBX462;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);

	private static final String CLAVE_VAR_X = ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_XC = ConstantsModulos.CTE_VAR_XC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_FPR = ConstantsModulos.CTE_VAR_FPR.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_W = ConstantsModulos.CTE_VAR_WX.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_CFANT = ConstantsModulos.CTE_VAR_CFANT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
	
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
			
			if (ModuloVBX462.LOG.isTraceEnabled()) {
				ModuloVBX462.LOG.trace("Inicio de execute en clase ModuloVBX462");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloVBX462
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
			
			//Invocamos a la función de calculo VBX462
			resultado = moduloVBX462(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVBX462.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVBX462.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloVBX462.LOG.isTraceEnabled()) {
			ModuloVBX462.LOG.trace("Fin de execute en clase ModuloVBX462");
		}
		
		return resultado;
	}
	
	/**
	 * Módulo que calcula el importe nominal para la proyección de Provisión Matemática por Fórmula Cerrada
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
	 * 
	 */
	private TotalFlujoProyeccion moduloVBX462(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables,
			final String codSubproceso) {		
		//Variables locales
		BigDecimal vbx462 = BigDecimal.ZERO;
		final TotalFlujoProyeccion salida = new TotalFlujoProyeccion();
		String varCriFec;
		String varCriEdad;
		BigDecimal varPU;
		Timestamp varfechaEfecto;
		Timestamp varFecCierre;
		Timestamp varFecCalc;
		BigDecimal vardurcierre;
		Integer vardurit1;
		Timestamp varIniRenta;
		Integer varEdifer;
		BigDecimal varX;
		BigDecimal varXC;
		BigDecimal varI1;
		BigDecimal varI2;
		String varTm;
		List<BigDecimal> varValoresTabMort;
		Timestamp varVcto;
		Integer varTcm;
		Integer varW;
		BigDecimal varGipc;
		BigDecimal varGE;
		Integer varFPR;
		BigDecimal varppr;
		Timestamp varfecvcto;
		Integer varNRTAMAX;
		BigDecimal op1;
		BigDecimal op2;
		BigDecimal op3;
		//Timestamp varfecJ;
		//Timestamp varfecJant;
		BigDecimal varXactj;
		BigDecimal varXactjant;
		BigDecimal varActj;
		BigDecimal varActjant;
		BigDecimal varCj;
		BigDecimal varVIDAj = BigDecimal.ZERO;
		BigDecimal varVTj = BigDecimal.ZERO;
		BigDecimal varCFj;
		BigDecimal varCFant;
		BigDecimal varFallecj;
		BigDecimal varVTRj;		
		//Fin variables locales
		
		if (ModuloVBX462.LOG.isTraceEnabled()) {
			ModuloVBX462.LOG.trace("Inicio función << moduloVBX462 >> de la clase ModuloVBX462, para la iteracion = {}", iteracion);
		}
		
		// Validación de los parámetros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Cálculo y validación de las variables de apoyo
		Integer ccartera = umic.getDatosGenerales().getCcartera();
		Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
		varCriEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriEdad);
			// Fin de la validación de las variables de apoyo.
		}
		// Fin variables Apoyo
		
		// Variables modulo
		varPU = umic.getPrimas().getIprimatarada();
		
		if (ConstantesSolvencia.NEGOCIO_INDIVIDUAL.equals(umic.getDatosGenerales().getCnegocio())) {
			varfechaEfecto = umic.getFechas().getFecefecini();
			
			varIniRenta = umic.getFechas().getFecefecini();
		} else {
			varfechaEfecto = umic.getFechas().getFecinisus();
			
			if (ConstantsFunciones.CTE_FORMPAGO_9.equals(umic.getRentas().getCpagrenta()) && umic.getRentas().getFecIni() == null ) {
				varIniRenta = varfechaEfecto;
			} else {
				varIniRenta = umic.getRentas().getFecIni();
			}
		}
			
		varFecCierre = UtilFechas.decreDias(proyUmic.get(iteracion-ConstantsFunciones.CTE_1).getFechaDesde(),ConstantsFunciones.CTE_1);
		varFecCalc = proyUmic.get(iteracion-ConstantsFunciones.CTE_1).getFechaDesde();
		
		vardurcierre = FuncionesAuxiliares.nAnnos(varFecCierre, varfechaEfecto, varCriFec).add(ConstantsFunciones.CTE_OPER_1_PARTIDO_365);
		vardurit1 = FuncionesAuxiliares.nAnnos(fcalc, umic.getBti().getFecFinTramo1(), varCriFec).intValue();
		
		varEdifer = umic.getDatosGenerales().getEdifer();
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varFecCalc, umic.getAsegurados().getFnacAseg1(), varCriEdad,
				umic.getRentas().getFecIni(), varEdifer);
		
		BigDecimal varTerminoXC = UtilModulos.getVarX(mapVariables, CLAVE_VAR_XC, varfechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriEdad,
				umic.getRentas().getFecIni(), varEdifer);
		varXC = varTerminoXC.add(vardurcierre);
		
		varI1 = btcUmic.getItcalc().get(ConstantsFunciones.CTE_0);
		varI2 = btcUmic.getItcalc().get(ConstantsFunciones.CTE_1);
		varTm = btcUmic.getTablacalc1aseg1();
		varValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic, 
				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriEdad, ConstantesSolvencia.CTE_TABMORT_L);
		
		varVcto = umic.getRentas().getFecFin();
		
		varTcm = UtilModulos.getVarK(mapVariables, CLAVE_VAR_TCM, varfechaEfecto, fcalc);
		
		varGipc = btcUmic.getGtorosspPrima().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		varGE = umic.getBti().getPgastgesex1I().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		
		varppr = umic.getRentas().getPrevrenta();
		BigDecimal unoMasPprEntre100 = BigDecimal.ONE.add(varppr.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		
		varFPR = (Integer) mapVariables.get(CLAVE_VAR_FPR);
		if (varFPR == null) {
			Integer forpagrent = umic.getRentas().getForpagrent();
			if (forpagrent != null && forpagrent != ConstantsFunciones.CTE_0) {
				varFPR = umic.getRentas().getForpagrent();
			} else {
				String cpagrenta = umic.getRentas().getCpagrenta();
				if (ConstantsFunciones.CTE_FORMPAGO_1.equals(cpagrenta)) {
					varFPR = ConstantsFunciones.CTE_1;
				} else if (ConstantsFunciones.CTE_FORMPAGO_2.equals(cpagrenta)) {
					varFPR = ConstantsFunciones.CTE_2;
				} else if (ConstantsFunciones.CTE_FORMPAGO_3.equals(cpagrenta)) {
					varFPR = ConstantsFunciones.CTE_4;
				} else if (ConstantsFunciones.CTE_FORMPAGO_4.equals(cpagrenta)) {
					varFPR = ConstantsFunciones.CTE_12;
				}
			}
			mapVariables.put(CLAVE_VAR_FPR, varFPR);			
		}
		
		
		if (ConstantsModulos.CTE_VAL_TP_VIT.equals(umic.getRentas().getTempVit())) {
			varW = UtilModulos.getVarEdadMaxima2(mapVariables, CLAVE_VAR_W, UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1()), umic, btcUmic,
					IObtenerConfiguracion.OrdenAsegurado.ASEG1);
			
			Integer varEdadCalc1;
			
			varEdadCalc1 = FuncionesAuxiliares.nEdad(varfechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriEdad, 
					umic.getRentas().getFecIni(), varEdifer).intValue();
			varfecvcto  = UtilFechas.incrAnyo(varfechaEfecto, varW - varEdadCalc1);			
		} else {
			varW = varX.intValue() + umic.getDuraciones().getNdursegano();
			varfecvcto = umic.getFechas().getFecefecfin();		
		}		
		
		
		
		// Para cualquier periodo j se calculará VBX462 como: 
		// VBX462(j) = ∑_(j=infball)^(varNRTAMAX)▒[ (varCj*varVidaj*varVTj) + (varCF*varFallecj*varVTRj) ] * ((1-varGE) / (1-varGipc-varGE)) 
		
		varNRTAMAX = ((varW - varXC.intValue()) * ConstantsFunciones.CTE_12) + ConstantsFunciones.CTE_1;
		
		//TODO: asegurarse de que esto es así
		if (varNRTAMAX > proyUmic.size()){
			varNRTAMAX = proyUmic.size();
		}
		
		
		op3 = BigDecimal.ONE.subtract(varGE).divide(BigDecimal.ONE.subtract(varGipc).subtract(varGE), ConstantsFunciones.MATH_CONTEXT);
		
		for (int j=iteracion; j<=varNRTAMAX; j++) {
			//varfecJ = proyUmic.get(j-ConstantsFunciones.CTE_1).getFechaDesde();
		   
		    varActj = FuncionesAuxiliares.nAnnos(proyUmic.get(j-ConstantsFunciones.CTE_1).getFechaHasta(),varFecCierre, varCriFec)
		    		.subtract(ConstantsFunciones.CTE_OPER_1_PARTIDO_365);
		    
		    varCFant = (BigDecimal) mapVariables.get(CLAVE_VAR_CFANT);
		    
			if (varFecCierre.equals(UtilFechas.decreDias(proyUmic.get(j-ConstantsFunciones.CTE_1).getFechaDesde(), ConstantsFunciones.CTE_1))) {
				//varfecJant = proyUmic.get(j-ConstantsFunciones.CTE_1).getFechaDesde();
				varActjant = varActj;	
				varCFj = FuncionesFallecimiento.cf(j, proyUmic, BigDecimal.ZERO, varPU, varTcm);
			} else {
				//varfecJant = proyUmic.get(j-ConstantsFunciones.CTE_2).getFechaDesde();
				varActjant = FuncionesAuxiliares.nAnnos(proyUmic.get(j-ConstantsFunciones.CTE_2).getFechaHasta(),varFecCierre, varCriFec)
						.subtract(ConstantsFunciones.CTE_OPER_1_PARTIDO_365);
				varCFj = FuncionesFallecimiento.cf(j, proyUmic, varCFant, varPU, varTcm);
			}
			
			varCFant = varCFj;
			mapVariables.put(CLAVE_VAR_CFANT, varCFant);
			

			varXactj = varXC.add(varActj);
			varXactjant = varXC.add(varActjant);
			
			//varCj = proyUmic.get(j-ConstantsFunciones.CTE_1).getImpPago();
			BigDecimal rentaInicial = proyUmic.get(j-ConstantsFunciones.CTE_1).getImpPago(); 
			Integer factor = varActj.add(vardurcierre).subtract(ConstantsFunciones.CTE_OPER_1_PARTIDO_365).intValue();
			
			varCj = rentaInicial.multiply( Util.pow(unoMasPprEntre100, factor));
			varVIDAj = FuncionesVBX.vida(varXC, varXactj, varW, varValoresTabMort);
			varVTj = FuncionesVBX.vt(vardurcierre, varActj, vardurit1, varI1, varI2);
			varFallecj = FuncionesFallecimiento.fallec(j, varXC, varXactj, varXactjant, varValoresTabMort);
			varVTRj = FuncionesVBX.vtr(j, vardurcierre, varActj, varActjant, vardurit1, varI1, varI2);
			
			op1 = varCj.multiply(varVIDAj).multiply(varVTj);
			op2 = varCFj.multiply(varFallecj).multiply(varVTRj);
			 
			BigDecimal terminoVbx462 = op1.add(op2);
			vbx462 = vbx462.add(terminoVbx462);
			
			//LOG.warn("VBX462: {};{};{};{};{};{};{};{};{};{}",iteracion,j,varCj,varVIDAj,varVTj,varCFj,varFallecj,varVTRj,terminoVbx462,vbx462);
			
		}
		
		vbx462 = vbx462.multiply(op3);
		
		// Se retornará:
		salida.setProvbtiproy(vbx462);
		salida.setTerminalAnterior(BigDecimal.ZERO);
		salida.setTerminalPosterior(BigDecimal.ZERO);			
		
		if (ModuloVBX462.LOG.isTraceEnabled()) {
			ModuloVBX462.LOG.trace("Fin función << moduloVBX462 >> de la clase ModuloVBX462, para la iteracion = {}, con resultado vbx462 = {}", iteracion, vbx462);
		} 
		
		return salida;
	}
	
}
