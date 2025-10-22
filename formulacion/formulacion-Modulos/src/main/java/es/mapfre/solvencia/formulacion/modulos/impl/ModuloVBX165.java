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
import es.mapfre.solvencia.formulacion.util.FuncionesVBX;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo VBX165. Módulo de provisiones para productos de rentas a dos cabezas con reembolso 
 * 				
 * @author ogperez
 *
 */
public class ModuloVBX165 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVBX165.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VBX165;
	
	private static final String CLAVE_CRIFEC = ConstantsFunciones.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CRIEDAD = ConstantsFunciones.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_FEC_EFEC = ConstantsFunciones.CTE_FEC_EFECTO.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_M = ConstantsModulos.CTE_VAR_M.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_X = ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_Y = ConstantsModulos.CTE_VAR_Y.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_MORT_X = ConstantsModulos.CTE_VAR_VAL_TAB_MORT_X.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_MORT_Y = ConstantsModulos.CTE_VAR_VAL_TAB_MORT_Y.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_WX = ConstantsModulos.CTE_VAR_WX.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_WY = ConstantsModulos.CTE_VAR_WY.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_J = ConstantsFunciones.CTE_VAR_J.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PIMSI = ConstantsModulos.CTE_VAR_PIMSI.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VBX165 = ConstantsModulos.CTE_VAR_VBX165.concat(CLAVE_MODULO);
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
			
			if (ModuloVBX165.LOG.isTraceEnabled()) {
				ModuloVBX165.LOG.trace("Inicio de execute en clase ModuloVBX165");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloVBX165
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
			
			//Invocamos a la función de calculo VBX165
			resultado = moduloVBX165(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVBX165.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVBX165.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloVBX165.LOG.isTraceEnabled()) {
			ModuloVBX165.LOG.trace("Fin de execute en clase ModuloVBX165");
		}
		
		return resultado;
	}
	
	/**
	 * Módulo de provisiones para productos de rentas a dos cabezas con  reembolso
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
	private TotalFlujoProyeccion moduloVBX165(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables,
			final String codSubproceso) {		
		//Variables locales
		BigDecimal vbx165 = BigDecimal.ZERO;
		final TotalFlujoProyeccion salida = new TotalFlujoProyeccion();
		String varCriFec;
		String varCriEdad;
		Timestamp varfechaEfecto;
		BigDecimal varM;
		Timestamp varIniRenta;
		BigDecimal varppr;
		BigDecimal opPpr = BigDecimal.ONE;
		BigDecimal varRever = null;
		Integer varDifer;
		Integer varEdifer;
		Integer varX;
		Integer varY;
		BigDecimal varI1;
		BigDecimal varI2;
		List<BigDecimal> varValoresTabMortX;
		List<BigDecimal> varValoresTabMortY;
		Integer varWX = null;
		Integer varWY = null;
		BigDecimal varFactorGic;
		Integer varF = 0;
		BigDecimal varRentaj;
		Integer varAG;
		BigDecimal varPna;
		String varPimSi;
		Timestamp varfecJ;
		Integer varTC;
		Integer varJ;
		BigDecimal varSumatorio = BigDecimal.ZERO;
		BigDecimal varPrimPag;
		Modulo moduloVBX164 = null;	
		moduloVBX164 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VBX164);
		TotalFlujoProyeccion vbx164;
		//Fin variables locales
		
		if (ModuloVBX165.LOG.isTraceEnabled()) {
			ModuloVBX165.LOG.trace("Inicio función << moduloVBX165 >> de la clase ModuloVBX165, para la iteracion = {}", iteracion);
		}
		
		// Validación de los parámetros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
	
		// Cálculo y validación de las variables de apoyo
		Integer ccartera = umic.getDatosGenerales().getCcartera();
		Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		
		varCriFec = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
		varCriEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriEdad);
			// Fin de la validación de las variables de apoyo.
		}
		// Fin variables Apoyo
		
		// Variables Modulo
		varfechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_VAR_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		varM = UtilModulos.getVarM(mapVariables, CLAVE_VAR_M, varfechaEfecto, umic.getBti().getFecFinTramo1(), varCriFec);

		if (ConstantesSolvencia.NEGOCIO_INDIVIDUAL.equals(umic.getDatosGenerales().getCnegocio())){
			varIniRenta = umic.getFechas().getFecefecini();
		} else {
			varIniRenta = umic.getRentas().getFecIni();
		}

		varppr = umic.getRentas().getPrevrenta();
		varRever = umic.getRentas().getPreversion().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		varDifer = umic.getRentas().getNadifer();
		varEdifer = umic.getDatosGenerales().getEdifer();
		
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varfechaEfecto, umic.getAsegurados().getFnacAseg1(),
				varCriEdad, varIniRenta, varEdifer).intValue();
		varY = UtilModulos.getVarX(mapVariables, CLAVE_VAR_Y, varfechaEfecto, umic.getAsegurados().getFnacAseg2(),
				varCriEdad, varIniRenta, varEdifer).intValue();
		
		varI1 = btcUmic.getItcalc().get(ConstantsFunciones.CTE_0);
		varI2 = btcUmic.getItcalc().get(ConstantsFunciones.CTE_1);
		
		
		varValoresTabMortX = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT_X, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1,
				varCriEdad, ConstantesSolvencia.CTE_TABMORT_L);
		varValoresTabMortY = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT_Y, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG2,
				varCriEdad, ConstantesSolvencia.CTE_TABMORT_L);
		
		if (umic.getRentas().getTempVit().equals(ConstantsModulos.CTE_VAL_TP_VIT)) {
			varWX = UtilModulos.getVarEdadMaxima2(mapVariables, CLAVE_VAR_WX, UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1()), umic, btcUmic,
					IObtenerConfiguracion.OrdenAsegurado.ASEG1);
			varWY = UtilModulos.getVarEdadMaxima2(mapVariables, CLAVE_VAR_WY, UtilFechas.getAnio(umic.getAsegurados().getFnacAseg2()), umic, btcUmic,
					IObtenerConfiguracion.OrdenAsegurado.ASEG2); 
		} else if (umic.getRentas().getTempVit().endsWith(ConstantsModulos.CTE_VAL_TP_TEM)) {
			varWX = varX + umic.getDuraciones().getNdursegano();
			varWY = varY + umic.getDuraciones().getNdursegano();			
		}
		
		varFactorGic = BigDecimal.ONE.add(btcUmic.getGtorosspCap().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		
		Integer cpgarent = Integer.valueOf(umic.getRentas().getCpagrenta());
		if (cpgarent == ConstantsFunciones.CTE_1) {
			varF = ConstantsFunciones.CTE_1;			
		} else if (cpgarent == ConstantsFunciones.CTE_2) {
			varF = ConstantsFunciones.CTE_2;
		} else if (cpgarent == ConstantsFunciones.CTE_3) {
			varF = ConstantsFunciones.CTE_4;
		} else if (cpgarent == ConstantsFunciones.CTE_4) {
			varF = ConstantsFunciones.CTE_12;
		} else {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AQ, new String[]{varF.toString()});
		}
		
		varRentaj = umic.getRentas().getRentini();
		varAG = umic.getRentas().getNpergaran() / ConstantsFunciones.CTE_12;
		varPna = umic.getPrimas().getIprimanetaini();
		varPimSi = "N";
		
		if (mapVariables.get(CLAVE_VAR_PIMSI) == null) {
			varPimSi = "N";
		} else {
			varPimSi = (String) mapVariables.get(CLAVE_VAR_PIMSI);
		}
		
		// Fin variables modulo
		
		BigDecimal unoMasPprEntre100 = BigDecimal.ONE.add(varppr.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		varfecJ = proyUmic.get(iteracion-ConstantsFunciones.CTE_1).getFechaDesde();
		//varTC = UtilModulos.getVarTC(mapVariables, CLAVE_VAR_J, varfechaEfecto,UtilFechas.decreDias(varfecJ, ConstantsFunciones.CTE_1));
		varTC = FuncionesAuxiliares.tc(varfechaEfecto, UtilFechas.decreDias(varfecJ, ConstantsFunciones.CTE_1));
		
		// Calculos a realizar en la primera iteracion
		if (iteracion.equals(ConstantsFunciones.CTE_FIRST_ITER)) {
			varJ = varTC;
			mapVariables.put(CLAVE_VAR_J, varJ);
			
			for (int j=varDifer; j<=varTC-ConstantsFunciones.CTE_1; j++) {
				if (j>varDifer){
					opPpr = opPpr.multiply(unoMasPprEntre100);
				}
				varSumatorio = varSumatorio.add(opPpr);
			}
			
			varPrimPag = varRentaj.multiply(BigDecimal.valueOf(varF)).multiply(varSumatorio);
		
			if (varPna.compareTo(varPrimPag) <= ConstantsFunciones.CTE_0) {
				varPimSi = "S";
				
				vbx164 = (TotalFlujoProyeccion) moduloVBX164.execute(proyUmic, bloqueCorriente, iteracion, fcalc,
						umic, btcUmic, mapVariables, codSubproceso);
				
				vbx165 = vbx164.getProvbtiproy();
				
				mapVariables.put(CLAVE_VAR_PIMSI, varPimSi);
				mapVariables.put(CLAVE_VAR_VBX165, vbx165);

			} else {
				vbx165 = CalcularVBX165(varPna, varRentaj, varF, varSumatorio, varDifer, varTC, varX, varY, varValoresTabMortX, varValoresTabMortY, 
						varM, varRever, varppr, varWX, varWY, varI1, varI2, varAG, varFactorGic);
				mapVariables.put(CLAVE_VAR_VBX165, vbx165);	
			}	
		} else { // Para cualquier periodo j>1 se calculará VBX165 como:
			
			varJ = (Integer) mapVariables.get(CLAVE_VAR_J);
			
			if (varJ == varTC) {
				vbx165 = (BigDecimal) mapVariables.get(CLAVE_VAR_VBX165);				
			} else {
				
				varJ=varTC;
				mapVariables.put(CLAVE_VAR_J, varJ);
				
				if (varPimSi == "N") {
					for (int j=varDifer; j<=varJ-ConstantsFunciones.CTE_1; j++) {
						if (j>varDifer){
							opPpr = opPpr.multiply(unoMasPprEntre100);
						}
						varSumatorio = varSumatorio.add(opPpr);
					}
					
					varPrimPag = varRentaj.multiply(BigDecimal.valueOf(varF)).multiply(varSumatorio);
					if (varPna.compareTo(varPrimPag) <= ConstantsFunciones.CTE_0) {
						varPimSi = "S";
						mapVariables.put(CLAVE_VAR_PIMSI, varPimSi);
					}
					
					//vbx165 = (BigDecimal) mapVariables.get(CLAVE_VAR_VBX165); //???
					
				}
				
				if (varPimSi == "S") {
					
					vbx164 = (TotalFlujoProyeccion) moduloVBX164.execute(proyUmic, bloqueCorriente, iteracion, fcalc,
							umic, btcUmic, mapVariables, codSubproceso);
					
					vbx165 = vbx164.getProvbtiproy();
					mapVariables.put(CLAVE_VAR_VBX165, vbx165);
										
				} else {
					vbx165 = CalcularVBX165(varPna, varRentaj, varF, varSumatorio, varDifer, varTC, varX, varY, varValoresTabMortX,
							varValoresTabMortY, varM, varRever, varppr, varWX, varWY, varI1, varI2, varAG, varFactorGic);
					mapVariables.put(CLAVE_VAR_VBX165, vbx165);
				}				
			}
		}
		
					
		// Se retornará:
		salida.setProvbtiproy(vbx165);
		salida.setTerminalAnterior(BigDecimal.ZERO);
		salida.setTerminalPosterior(BigDecimal.ZERO);		
		
		if (ModuloVBX165.LOG.isTraceEnabled()) {
			ModuloVBX165.LOG.trace("Fin función << moduloVBX165 >> de la clase ModuloVBX165, para la iteracion = {}, con resultado vbx165 = {}", iteracion, vbx165);
		}
		
		return salida;
	}
	
	
	private BigDecimal CalcularVBX165 (BigDecimal varPna, BigDecimal varRentaj, Integer varF, BigDecimal varSumatorio, 
			Integer varDifer, Integer varTC, Integer varX, Integer varY, List<BigDecimal> varValoresTabMortX,
			List<BigDecimal> varValoresTabMortY, BigDecimal varM, BigDecimal varRever, BigDecimal varppr, Integer varWX, Integer varWY
			, BigDecimal varI1,BigDecimal varI2, Integer varAG, BigDecimal varFactorGic) {
		// Variables locales
		BigDecimal vbx165 = BigDecimal.ONE;
		Integer varLimInf;
		BigDecimal varAGP1;
		BigDecimal varAGP2 = BigDecimal.ZERO;
		Integer varAGP;
		BigDecimal varLxt;
		BigDecimal varLyt;
		BigDecimal varafrxytAGPt;
		BigDecimal varAfrxDiferAGP;
		BigDecimal varAfryDiferAGP;
		BigDecimal varAfrxyDiferAGP;
		BigDecimal varLxDiferAGP;
		BigDecimal varLyDiferAGP;
		BigDecimal varAGTr;
		BigDecimal varRentaPeriodo;
		BigDecimal varTermino1;
		BigDecimal varTermino2_1;
		BigDecimal varTermino2_2;
		BigDecimal varTermino2;
		BigDecimal varTermino3;
		BigDecimal varTermino4;
		BigDecimal varSumaTerminos;
		Integer varLimSuperiorX;
		Integer varLimSuperiorY;
		// Fin variables locales
			
		varAGP1 = varPna.subtract((varRentaj.multiply(BigDecimal.valueOf(varF)).multiply(varSumatorio)))
				.divide(BigDecimal.valueOf(varF).multiply(varRentaj), ConstantsFunciones.MATH_CONTEXT);
		
		
		if (varTC < varDifer) {
			varLimInf = varDifer;
		} else {
			varLimInf = varTC;
		}
		
		int t = varLimInf;
		BigDecimal unoMasPprEntre100 = BigDecimal.ONE.add(varppr.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		
		varAGP2 = calcularVarAPG2 (unoMasPprEntre100, varAGP2, t, varX, varValoresTabMortX, varY, varValoresTabMortY, varF, varRever, varppr, varDifer  );
				
		while ( varAGP2.compareTo(varAGP1) < 0) {			
			t++;
			BigDecimal SumAGP2 = calcularVarAPG2(unoMasPprEntre100,varAGP2, t, varX, varValoresTabMortX, varY, varValoresTabMortY, varF, varRever, varppr, varDifer);
			varAGP2 = varAGP2.add(SumAGP2);	
		}
		
		varAGP = BigDecimal.valueOf(t - varDifer + ConstantsFunciones.CTE_1).intValue();
		
		varafrxytAGPt = FuncionesVBX.afrxytAGPt(varDifer, varX, varWX, varY, varWY, varppr, varTC, varValoresTabMortX, varValoresTabMortY,
				varM, varI1, varI2, varF, varAGP);

		varLimSuperiorX = varWX - varX - varAGP - varDifer;
		varLimSuperiorY = varWY - varY - varAGP - varDifer;
		
		varAfrxDiferAGP = FuncionesVBX.afrxDiferAGP(varDifer, varX, varWX, varppr, varTC, varValoresTabMortX, varM,
				varI1, varI2, varF, varAG, varAGP, varLimSuperiorX);
		
		varAfryDiferAGP = FuncionesVBX.afrxDiferAGP(varDifer, varY, varWY, varppr, varTC, varValoresTabMortY, varM,
				varI1, varI2, varF, varAG, varAGP, varLimSuperiorY);

		varAfrxyDiferAGP = FuncionesVBX.afrxyDiferAGP(varDifer, varX, varWX, varY, varWY, varppr, varTC, varValoresTabMortX,
				varValoresTabMortY, varM, varI1, varI2, varAGP, varF);

		
		varLxt = Util.getVarLx(BigDecimal.valueOf(varX + varTC), varValoresTabMortX);
		varLyt = Util.getVarLx(BigDecimal.valueOf(varY + varTC), varValoresTabMortY);

		varLxDiferAGP = Util.getVarLx(BigDecimal.valueOf(varX + varDifer + varAGP), varValoresTabMortX);
		varLyDiferAGP = Util.getVarLx(BigDecimal.valueOf(varY + varDifer + varAGP), varValoresTabMortY);
		
		varAGTr = FuncionesVBX.agtr(varDifer, varX, varWX, varppr, varTC, varValoresTabMortX, varM, varI1, varI2, varF, varAGP);
		 
		varRentaPeriodo = varRentaj.multiply(varRever).multiply(varAGTr);
		
		varTermino1 = (BigDecimal.ONE.subtract(varRever)).multiply(varafrxytAGPt);
		varTermino2_1 = varRever.multiply(varAfrxDiferAGP.add(varAfryDiferAGP));
		varTermino2_2 = (BigDecimal.ONE.subtract(ConstantsFunciones.CTE_OPER_2.multiply(varRever))).multiply(varAfrxyDiferAGP);
		varTermino2 = varLxDiferAGP.multiply(varLyDiferAGP).multiply(varTermino2_1.add(varTermino2_2))
				.divide(varLxt.multiply(varLyt), ConstantsFunciones.MATH_CONTEXT);
		varTermino3 = (varLyDiferAGP.divide(varLyt, ConstantsFunciones.MATH_CONTEXT)).
				multiply(BigDecimal.ONE.subtract(varLxDiferAGP.divide(varLxt, ConstantsFunciones.MATH_CONTEXT))).
				multiply(varRever).multiply(varAfryDiferAGP);
		varTermino4 = (varLxDiferAGP.divide(varLxt, ConstantsFunciones.MATH_CONTEXT)).
				multiply(BigDecimal.ONE.subtract(varLyDiferAGP.divide(varLyt, ConstantsFunciones.MATH_CONTEXT))).
				multiply(varRever).multiply(varAfrxDiferAGP);
		
		varSumaTerminos = varTermino1.add(varTermino2).add(varTermino3).add(varTermino4);
		  
		vbx165 = (varRentaPeriodo.add(varRentaj.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_5).multiply(varSumaTerminos))).
				multiply(varFactorGic);
			
		return vbx165;
	}

	
	/**
	 * Función que calcula el termino varAGP2 para un valor de t
	 * @param unoMasPprEntre100
	 * @param varAGP2 
	 * @param t
	 * @param varX
	 * @param varValoresTabMortX
	 * @param varY
	 * @param varValoresTabMortY
	 * @param varF
	 * @param varRever
	 * @param varppr
	 * @param varDifer
	 * @return
	 */
	private BigDecimal calcularVarAPG2(BigDecimal unoMasPprEntre100, BigDecimal varAGP2, int t, Integer varX, List<BigDecimal> varValoresTabMortX,
			Integer varY, List<BigDecimal> varValoresTabMortY, Integer varF, BigDecimal varRever, BigDecimal varppr, Integer varDifer) {
		 // Variables locales
		BigDecimal varLxt;
		BigDecimal varLyt;
		BigDecimal varLxt1;
		BigDecimal varLyt1; 
		BigDecimal varTermt1;
		BigDecimal varTermt2;
		BigDecimal varTerminotParte2;
		BigDecimal varTerminot;
		BigDecimal opPpr;		
		// Fin variables locales
		
		varLxt = Util.getVarLxEntero(varX + t, varValoresTabMortX);
		varLyt = Util.getVarLxEntero(varY + t, varValoresTabMortY);
		varLxt1 = Util.getVarLxEntero(varX + t + ConstantsFunciones.CTE_1, varValoresTabMortX);
		varLyt1 = Util.getVarLxEntero(varY + t + ConstantsFunciones.CTE_1, varValoresTabMortY);
		varTermt1 = (BigDecimal.valueOf(varF).subtract(BigDecimal.ONE)).multiply(varLxt).multiply(varLyt);
		varTermt2 = (BigDecimal.valueOf(varF).add(BigDecimal.ONE)).multiply(varLxt1).multiply(varLyt1);
		
		varTerminotParte2 = (BigDecimal.ONE.subtract(varRever)).divide(ConstantsFunciones.CTE_OPER_2
				.multiply(BigDecimal.valueOf(varF)).multiply(varLxt).multiply(varLyt), ConstantsFunciones.MATH_CONTEXT);
		
		varTerminot = varRever.add(varTerminotParte2.multiply(varTermt1.add(varTermt2)));
		
		opPpr = Util.pow(unoMasPprEntre100, t - varDifer);	
		
		varAGP2 = opPpr.max(varTerminot);
		
		return varAGP2;
	}
}
