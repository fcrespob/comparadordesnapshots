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
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo VBX164. Módulo de provisiones para productos de rentas a dos cabezas sin reembolso 
 * 				
 * @author ogperez
 *
 */
public class ModuloVBX164 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVBX164.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VBX164;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO); 
	private static final String CLAVE_VAR_M = ConstantsModulos.CTE_VAR_M.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_Y = ConstantsModulos.CTE_VAR_Y.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_X = ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_MORT_X = ConstantsModulos.CTE_VAR_VAL_TAB_MORT_X.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_MORT_Y = ConstantsModulos.CTE_VAR_VAL_TAB_MORT_Y.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_WX = ConstantsModulos.CTE_VAR_WX.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_WY = ConstantsModulos.CTE_VAR_WY.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_J = ConstantsFunciones.CTE_VAR_J.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VBX164 = ConstantsModulos.CTE_VAR_VBX164.concat(CLAVE_MODULO);
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
			
			if (ModuloVBX164.LOG.isTraceEnabled()) {
				ModuloVBX164.LOG.trace("Inicio de execute en clase ModuloVBX164");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloVBX164
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
			
			//Invocamos a la función de calculo VBX164
			resultado = moduloVBX164(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVBX164.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVBX164.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloVBX164.LOG.isTraceEnabled()) {
			ModuloVBX164.LOG.trace("Fin de execute en clase ModuloVBX164");
		}
		
		return resultado;
	}
	
	/**
	 * Módulo de provisiones para productos de rentas a dos cabezas sin reembolso
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
	private TotalFlujoProyeccion moduloVBX164(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables,
			final String codSubproceso) {		
		//Variables locales
		BigDecimal vbx164 = BigDecimal.ZERO;
		final TotalFlujoProyeccion salida = new TotalFlujoProyeccion();
		String varCriFec;
		String varCriEdad;
		Timestamp varfechaEfecto = null;
		BigDecimal varM;
		Timestamp varIniRenta;
		BigDecimal varppr;
		BigDecimal varRever;
		Integer varDifer;
		Integer varX;
		Integer varY;
		BigDecimal varI1;
		BigDecimal varI2;
		List<BigDecimal> varValoresTabMortX;
		List<BigDecimal> varValoresTabMortY;
		Integer varWX = 0;
		Integer varWY = 0;
		BigDecimal varFactorGic;
		Integer varF = 0;
		BigDecimal varRentaj;
		Timestamp varfecJ;
		Integer varTc;
		Integer varJ;
		//Fin variables locales
		
		if (ModuloVBX164.LOG.isTraceEnabled()) {
			ModuloVBX164.LOG.trace("Inicio función << moduloVBX164 >> de la clase ModuloVBX164, para la iteracion = {}", iteracion);
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
		
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varfechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriEdad, varIniRenta, varEdifer).intValue();
		varY = UtilModulos.getVarX(mapVariables, CLAVE_VAR_Y, varfechaEfecto, umic.getAsegurados().getFnacAseg2(), varCriEdad, varIniRenta, varEdifer).intValue();
		
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
		} else if (umic.getRentas().getTempVit().equals(ConstantsModulos.CTE_VAL_TP_TEM)) {
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
		// Fin Variables Modulo
		
		// Para cualquier periodo j se calculará VBX164 como: 
		varfecJ = proyUmic.get(iteracion-ConstantsFunciones.CTE_1).getFechaDesde();
		varTc = FuncionesAuxiliares.tc(varfechaEfecto, UtilFechas.decreDias(varfecJ, ConstantsFunciones.CTE_1));
		varJ = (Integer) mapVariables.get(CLAVE_VAR_J);
		if (iteracion == ConstantsFunciones.CTE_FIRST_ITER) {
			varJ = varTc;
			vbx164 = calcularVBX164(varDifer, varX, varWX, varY, varWY, varppr, varTc, varValoresTabMortX, varValoresTabMortY, 
					varM, varI1, varI2, varF, varRentaj, varFactorGic, varRever);
			
			mapVariables.put(CLAVE_VAR_J, varJ);
			mapVariables.put(CLAVE_VAR_VBX164, vbx164);
		} else {
			if (varJ != null && varJ == varTc) {
				// Se conservan las variables
				vbx164 = (BigDecimal) mapVariables.get(CLAVE_VAR_VBX164);
			} else {
				// Se recalculan las variables
				varJ = varTc;					
				vbx164 = calcularVBX164(varDifer, varX, varWX, varY, varWY, varppr, varTc, varValoresTabMortX, varValoresTabMortY, 
						varM, varI1, varI2, varF, varRentaj, varFactorGic, varRever);
				
				mapVariables.put(CLAVE_VAR_J, varJ);
				mapVariables.put(CLAVE_VAR_VBX164, vbx164);
			}
		}
		
		
		// Se retornará:
		salida.setProvbtiproy(vbx164);
		salida.setTerminalAnterior(BigDecimal.ZERO);
		salida.setTerminalPosterior(BigDecimal.ZERO);		
		
		
		if (ModuloVBX164.LOG.isTraceEnabled()) {
			ModuloVBX164.LOG.trace("Fin función << moduloVBX164 >> de la clase ModuloVBX164, para la iteracion = {}, con resultado vbx164 = {}", iteracion, vbx164);
		}
		
		return salida;
	}
	
	
	
	private BigDecimal calcularVBX164 (Integer varDifer, Integer varX,  Integer varWX, Integer varY, Integer varWY, BigDecimal varppr,
			Integer varTc, List<BigDecimal> varValoresTabMortX, List<BigDecimal> varValoresTabMortY, BigDecimal varM, BigDecimal varI1,
			BigDecimal varI2, Integer varF, BigDecimal varRentaj, BigDecimal varFactorGic, BigDecimal varRever) {
		// Variables locales
		BigDecimal vbx164 = null;
		BigDecimal varAfrxt;
		BigDecimal varAfryt;
		BigDecimal varAfrxyt;
		BigDecimal op1;
		BigDecimal op2;
		BigDecimal op3;
		
		// Se calcula el valor de vbx164 como:
		// VBX164(j) = (varRentaj)/2*varFactorGic*[varRever*((varAfrxt +varAfryt)/2)+ (1-2*varRever)*varAfrxyt]   
		
		varAfrxt = FuncionesVBX.afrxt(varDifer, varX, varWX, varppr, varTc, varValoresTabMortX, varM, varI1, varI2, varF);
		varAfryt = FuncionesVBX.afrxt(varDifer, varY, varWY, varppr, varTc, varValoresTabMortY, varM, varI1, varI2, varF);
		varAfrxyt = FuncionesVBX.afrxyt(varDifer, varX, varWX, varY, varWY, varppr, varTc, varValoresTabMortX,
				varValoresTabMortY, varM, varI1, varI2, varF);
		
		op1 = varRentaj.multiply(varFactorGic).multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_5);
		op2 = varRever.multiply(varAfrxt.add(varAfryt));
		op3 = (BigDecimal.ONE.subtract(varRever.multiply(ConstantsFunciones.CTE_OPER_2))).multiply(varAfrxyt);
		
		vbx164 = op1.multiply(op2.add(op3));				
		
		return vbx164;
	}
	
}
