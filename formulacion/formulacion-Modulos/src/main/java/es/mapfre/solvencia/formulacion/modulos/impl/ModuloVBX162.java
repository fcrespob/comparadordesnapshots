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
import es.mapfre.solvencia.formulacion.util.FuncionesVBX;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo VBX162. Módulo que calcula el importe nominal para la proyección de Provisión
 * Matemática por formula cerrada
 * 				
 * @author ogperez
 *
 */
public class ModuloVBX162 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVBX162.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VBX162;
	
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;	
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_M = ConstantsModulos.CTE_VAR_M.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_X = ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_W = ConstantsModulos.CTE_VAR_W.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_J = ConstantsFunciones.CTE_VAR_J.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VBX162 = ConstantsModulos.CTE_VAR_VBX162.concat(CLAVE_MODULO);
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
			
			if (ModuloVBX162.LOG.isTraceEnabled()) {
				ModuloVBX162.LOG.trace("Inicio de execute en clase ModuloVBX162");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloVBX162
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
			
			//Invocamos a la función de calculo VBX162
			resultado = moduloVBX162(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVBX162.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVBX162.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloVBX162.LOG.isTraceEnabled()) {
			ModuloVBX162.LOG.trace("Fin de execute en clase ModuloVBX162");
		}
		
		return resultado;
	}
	
	/**
	 * Módulo de cálculo de la provisión matemática de la Umic
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
	private TotalFlujoProyeccion moduloVBX162(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables,
			final String codSubproceso) {		
		//Variables locales
		BigDecimal vbx162 = BigDecimal.ZERO;
		final TotalFlujoProyeccion salida = new TotalFlujoProyeccion();
		String varCriEdad;
		String varCriFec;
		Timestamp varfechaEfecto = null;
		BigDecimal varM;
		Integer varDifer;
		BigDecimal varppr;
		Integer varX;
		BigDecimal varI1;
		BigDecimal varI2;
		List<BigDecimal> varValoresTabMort;
		Integer varW = 0;
		BigDecimal varFactorGic;
		Integer varF = 0;
		BigDecimal varRentaj;
		Integer varAG;
		Integer varAGP;
		Integer varXDA;
		BigDecimal varLXDA;
		BigDecimal varAfrxDiferAGP;
		Timestamp varfecJ;
		Integer varTc;
		Modulo moduloVBX160;
		Integer varJ;
		Integer varLimSuperior;
		//Fin variables locales
		
		if (ModuloVBX162.LOG.isTraceEnabled()) {
			ModuloVBX162.LOG.trace("Inicio función << moduloVBX162 >> de la clase ModuloVBX162, para la iteracion = {}", iteracion);
		}
		
		
		// Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Cálculo y validación de las variables de apoyo.
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		
		varCriEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
				
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriEdad);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
			// Fin de la validación de las variables de apoyo.
		}
		// Fin variables Apoyo
		
		// Variables Modulo
		varfechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_VAR_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());		
		varM = UtilModulos.getVarM(mapVariables, CLAVE_VAR_M, varfechaEfecto, umic.getBti().getFecFinTramo1(), varCriFec);

		varDifer = umic.getRentas().getNadifer();
		varppr = umic.getRentas().getPrevrenta();
		
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varfechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriEdad,
				umic.getRentas().getFecIni(), varEdifer).intValue();
			
		varI1 = btcUmic.getItcalc().get(0);
		varI2 = btcUmic.getItcalc().get(1);
		varValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic,
				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriEdad, ConstantesSolvencia.CTE_TABMORT_L);
		
		if (umic.getRentas().getTempVit().equals(ConstantsModulos.CTE_VAL_TP_VIT)) {
			varW = UtilModulos.getVarEdadMaxima2(mapVariables, CLAVE_VAR_W, UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1()),
					umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1);
		}
		else if (umic.getRentas().getTempVit().equals(ConstantsModulos.CTE_VAL_TP_TEM)) {
			varW = varX + umic.getDuraciones().getNdursegano();			 
		}
		
		varFactorGic = BigDecimal.ONE.add(btcUmic.getGtorosspCap().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		
		Integer cpagent = Integer.valueOf(umic.getRentas().getCpagrenta());
		if (cpagent == ConstantsFunciones.CTE_1) {
			varF = ConstantsFunciones.CTE_1;
		} else if (cpagent == ConstantsFunciones.CTE_2) {
			varF = ConstantsFunciones.CTE_2;						
		} else if (cpagent == ConstantsFunciones.CTE_3) {
			varF = ConstantsFunciones.CTE_4;
		} else if (cpagent == ConstantsFunciones.CTE_4) {
			varF = ConstantsFunciones.CTE_12;
		} else {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AQ, new String[]{varF.toString()});
		}
		
		varAG = umic.getRentas().getNpergaran()/ConstantsFunciones.CTE_12;
		varRentaj = umic.getRentas().getRentini();
		varAGP = (BigDecimal.valueOf(umic.getRentas().getNpergaran()).divide(BigDecimal.valueOf(varF), 0, RoundingMode.CEILING)).intValue();
			
		varXDA = varX + varDifer + varAGP;
		varLXDA = Util.getVarLx(BigDecimal.valueOf(varXDA), varValoresTabMort);
				
		// Fin variables Modulo 
		
		varfecJ = proyUmic.get(iteracion-1).getFechaDesde();
		varTc= FuncionesAuxiliares.tc(varfechaEfecto, UtilFechas.decreDias(varfecJ, ConstantsFunciones.CTE_1));
		
		varLimSuperior = varW - varX;
	
		varAfrxDiferAGP = FuncionesVBX.afrxDiferAGP(varDifer, varX, varW, varppr, varTc, varValoresTabMort, varM, varI1, varI2, varF, varAG, varAGP, varLimSuperior);
		
		// Para cualquier periodo j se calculará VBX162 como:
		if (varTc >= varAG + varDifer) {
			//Se llama al módulo VBX160 
			moduloVBX160 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VBX160);
			TotalFlujoProyeccion varProyVbx160  = (TotalFlujoProyeccion) moduloVBX160.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			vbx162 = varProyVbx160.getProvbtiproy();
		}
		else {		
			if (iteracion == ConstantsFunciones.CTE_FIRST_ITER || varTc == null) {
				varJ = varTc;
				vbx162 = calcularVBX162(varDifer, varX, varW, varppr, varTc, varValoresTabMort, varM, varI1, varI2, varF, varAGP,
						varRentaj, varLXDA, varAfrxDiferAGP, varFactorGic,varJ);		
				mapVariables.put(CLAVE_VAR_J, varJ);
				mapVariables.put(CLAVE_VAR_VBX162, vbx162);				
			}
			else {
				varJ = (Integer) mapVariables.get(CLAVE_VAR_J);
				if (varJ == varTc) {
					vbx162 = (BigDecimal) mapVariables.get(CLAVE_VAR_VBX162);
				} else if (varJ < varTc) {
					//Se recalculan las variables
					varJ = varTc;
					vbx162 = calcularVBX162(varDifer, varX, varW, varppr, varTc, varValoresTabMort, varM, varI1, varI2, varF, varAGP,
							varRentaj, varLXDA, varAfrxDiferAGP, varFactorGic,varJ);		
					mapVariables.put(CLAVE_VAR_J, varJ);
					mapVariables.put(CLAVE_VAR_VBX162, vbx162);			 
				}				
			}			
		}		
		
		
		// Se retornará:
		salida.setProvbtiproy(vbx162);
		salida.setTerminalAnterior(BigDecimal.ZERO);
		salida.setTerminalPosterior(BigDecimal.ZERO);		
		
		
		if (ModuloVBX162.LOG.isTraceEnabled()) {
			ModuloVBX162.LOG.trace("Fin función << moduloVBX162 >> de la clase ModuloVBX162, para la iteracion = {}, con resultado vbx162 = {}", iteracion, vbx162);
		}
		
		return salida;
	}


	private BigDecimal calcularVBX162(Integer varDifer, Integer varX, Integer varW, BigDecimal varppr, Integer varTc,
			List<BigDecimal> varValoresTabMort, BigDecimal varM,BigDecimal varI1, BigDecimal varI2, Integer varF, Integer varAGP,
			BigDecimal varRentaj, BigDecimal varLXDA, BigDecimal varAfrxt, BigDecimal varFactorGic, Integer varJ) {
		
		BigDecimal varAgtr; 
		BigDecimal varLxT;
		BigDecimal vbx162;
		
		varAgtr = FuncionesVBX.agtr(varDifer, varX, varW, varppr, varTc, varValoresTabMort, varM, varI1, varI2, varF, varAGP);
		varLxT = Util.getVarLx(BigDecimal.valueOf(varX+varTc), varValoresTabMort);
		
		// varLXDA/varLxT  * (varAfrxt )/2
		BigDecimal op1 = (varLXDA.multiply(varAfrxt).multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_5)).divide(varLxT,ConstantsFunciones.MATH_CONTEXT);
		
		// Calculo de : varVBX162  = varRentaj * [varAGTr+ varLXDA/varLxT  * (varAfrxt )/2   ]*varFactorGic
		vbx162 = varRentaj.multiply(varAgtr.add(op1)).multiply(varFactorGic);
		
		return vbx162;		
	}
	
	
}
