package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Asegurados;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TablaConversion;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;


public class ModuloFPTOZCL implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloFPTOZCL.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_FPTOZCL;
	private static final String CLAVE_UMIC2 = ConstantsModulos.CTE_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC2 = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_EDAD_CAL = ConstantsModulos.CTE_EDAD_CAL.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_ZC = ConstantsModulos.CTE_VAR_ZC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_FD_ANT = ConstantsModulos.CTE_VAR_FD_ANT.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC;
	private static final String CLAVE_SEX_ASEG1 = ConstantsModulos.CTE_SEX_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_EDAD_ASEG1 = ConstantsModulos.CTE_EDAD_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_FNAC_ASEG1 = ConstantsModulos.CTE_FNAC_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACALC_ASEG1 = ConstantsModulos.CTE_TABLACALC_ASEG1.concat(CLAVE_MODULO);
	
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
			if (ModuloFPTOZCL.LOG.isTraceEnabled()) {
				ModuloFPTOZCL.LOG.trace("Inicio de execute en clase FPTOZCL");
			}
			
			// Recuperamos los datos que le pasaremos a la función moduloFPTOZC.
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de datos.
			
			//Llamamos a la función moduloFPTOZC.
			resultado = moduloFPTOZCL(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloFPTOZCL.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloFPTOZCL.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloFPTOZCL.LOG.isTraceEnabled()) {
			ModuloFPTOZCL.LOG.trace("Fin de execute en clase FPTOZCL");
		}
		
		return resultado;
	}
	
	/** 
	 * Modulo de cálculo que devuelve los factores de probabilización a aplicar en  el cálculo de la cuantía probable de una garantía.  
	 * El módulo FPTOZC (fcal,j) (fallecimiento en un punto) determina la probabilidad de que una cabeza de edad zc en la fecha de cálculo, fcal, 
	 * fallezca en el periodo entre las fechas jant y j, fechas en las que se devengan las prestaciones C(i,jant) y C(i,j) respectivamente 
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
	 */
	private BigDecimal moduloFPTOZCL(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		String varCriterioEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriterFec = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal varEdadCalc;
		BigDecimal varZc = BigDecimal.ZERO;
		BigDecimal fptozcl = BigDecimal.ZERO;
		Timestamp varFechaEfecto = null;
		
		BigDecimal fptozcl1 = BigDecimal.ZERO;
		BigDecimal fptozcl2 = BigDecimal.ZERO;
		Modulo moduloFPTOZC;
		List<DetalleCorriente> varProyFptozcl1;
		List<DetalleCorriente> varProyFptozcl2;
		Umic varUmic2;
		DetalleBaseTecnica varBtcUmic, varBtcUmic2;
		//Fin variables locales
		
		if (ModuloFPTOZCL.LOG.isTraceEnabled()) {
			ModuloFPTOZCL.LOG.trace("Inicio función << moduloFPTOZCL >> de la clase moduloFPTOZCL, para la iteracion = {}", iteracion);
		}
		
		// Introducido por nuevos criterios de generación de fechas de pago y devengo (corte de fechas). Si no existe fecha devengo no se realiza el calculo
		if (null == bloqueCorriente.getFechaDevengo() ) {
			return fptozcl;
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		/**
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, así como las variables internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso por los siguientes periodos a calcular. 
		 * Variables de Apoyo
		 * - VarCriterEdad --> obtenerConfiguracion.recuperarVariableApoyo(ID-CRITERIO)
		 * - VarCriterFec -->    obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
		 * - Si alguna de las variables de apoyo  retornadas es nulo se devuelve error funcional 005 - No se ha encontrado la Variable de Apoyo &NombreVariableApoyo, finalizando el proceso para la UMIC.
		 * 
		 * Si estoy en el primer periodo (j=1), se establecerán las siguientes variables: 
		 * - varEdadCalc = nedad(umic.fechas.fecinisus, umic.asegurados.fnacAseg1, VarCriterEdad)
		 * 		varEdadCalc --> Dejo la variable disponible en memoria para el subproceso de la umic.
		 * - varAnoNac = Año (umic.asegurados.fnacAseg1)
		 * - varTabMort = btcUmic.tabla1Aseg1
		 * 		varTabMort --> Dejo la variable disponible en memoria para el subproceso de la umic.
		 * - varValoresTabMort = obtenerConfiguracion. recuperarValoresExperiencia(btcUmic.fecCierre, varTabMort, varAnoNac, btcUmic. itcalc1, umic.baseTecIni. psobremort, umic.baseTecIni. priesgo)
		 * - varFracc0 = nanos(umic.fechas.fecinisus, fcalc, VarCriterFec)
		 * - VarZc = VarEdadCalc + varFracc0
		 * - varLzcEntero = valoresTabMort(ENTERO(VarZc)).wkvalor
		 * - varLzcEntero1 = valoresTabMort(ENTERO(VarZc)+1).wkvalor
		 * - varLzc = varLzcEntero + (ParteDecimal(VarZc) )* (varLzcEntero1 – varLzcEntero)
		 * - varFracc1 = naños(fcalc, proyUmic(j).varBloque.fecDevengo, VarCriterFec)
		 * - varEdadNJ = varEdadCalc + naños(fcalc, varFracc1)
		 * - varLjEntero = valoresTabMort(ENTERO(varEdadNJ)).wkvalor
		 * - varLjEntero1 = valoresTabMort(ENTERO(varEdadNJ)+1).wkvalor
		 * - varLj = varLjEntero + (ParteDecimal (varEdadNJ) ) * (varLjEntero1– varLjEntero)
		 * 
		 * Para cualquier periodo j (j > 1), se establecerán las siguientes variables
		 * - varFracc2 = naños(fcalc, proyUmic(j-1).varBloque.fecDevengo,VarCriterFec)
		 * - varEdadNJant = varEdadCalc + varFracc2)
		 * - varLjEntero= valoresTabMort(ENTERO(varEdadNJant)).wkvalor
		 * - varLjEntero1 = valoresTabMort(ENTERO(varEdadNJant)+1).wkvalor
		 * - varLjant = varLjEntero + (ParteDecimal (varEdadNJant) )* (varLjEntero1– varLjEntero)
		 * - varFracc1 = naños(fcalc, proyUmic(j).varBloque.fecDevengo, VarCriterFec)
		 * - varLjEntero = valoresTabMort(ENTERO(varEdadNJ)).wkvalor
		 * - varLjEntero1 = valoresTabMort(ENTERO(varEdadNJ)+1).wkvalor
		 * - varLj = varLjEntero + (ParteDecimal (varEdadNJ) )* (varLjEntero1– varLjEntero)
		 */
		
		// Se definen estas variables auxiliares dado que se llaman varias veces.
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		final String kprestacion = umic.getDatosGenerales().getKprestacion();
		
		
		// Cálculo y validación de las variables de apoyo.  
		varCriterioEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterioEdad);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFec);
			mapVariables.put(CLAVE_SEX_ASEG1, umic.getAsegurados().getCsexAseg1());
			mapVariables.put(CLAVE_EDAD_ASEG1, umic.getAsegurados().getEdadAseg1());
			mapVariables.put(CLAVE_FNAC_ASEG1, umic.getAsegurados().getFnacAseg1());
			mapVariables.put(CLAVE_TABLACALC_ASEG1, btcUmic.getTablacalc1aseg1());
		}// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		// Fin de la validación de las variables de apoyo.
		
				
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		varEdadCalc = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL, varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriterioEdad, umic.getRentas().getFecIni(), varEdifer);
		
		
			varZc = UtilModulos.getVarZc(mapVariables, CLAVE_VAR_ZC, varEdadCalc, varFechaEfecto, fcalc, varCriterFec,varCriterioEdad);
		
		
		varProyFptozcl1 = proyUmic;
		moduloFPTOZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FPTOZC);
		if (umic.getAsegurados().getFnacAseg2() != null && umic.getAsegurados().getCsexAseg2() != null
				&& umic.getAsegurados().getEdadAseg2() != null) {
			fptozcl1 = (BigDecimal) moduloFPTOZC.execute(varProyFptozcl1, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
			varUmic2 = (Umic) mapVariables.get(CLAVE_UMIC2);

			if (varUmic2 == null) {
				varUmic2 = new Umic();
				try {

					PropertyUtils.copyProperties(varUmic2, umic);

				} catch (Exception e) {
					ModuloFPTOZCL.LOG.error(e.getMessage());
				}
				Asegurados aseg = new Asegurados();
				aseg.setFnacAseg1(umic.getAsegurados().getFnacAseg2());
				aseg.setCsexAseg1(umic.getAsegurados().getCsexAseg2());
				aseg.setEdadAseg1(umic.getAsegurados().getEdadAseg2());
				varUmic2.setAsegurados(aseg);

				mapVariables.put(CLAVE_UMIC2, varUmic2);
			}

			varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC2);
			if (null == varBtcUmic2) {
				varBtcUmic2 = new DetalleBaseTecnica();

				try {

					PropertyUtils.copyProperties(varBtcUmic2, btcUmic);

				} catch (Exception e) {
					ModuloFPTOZCL.LOG.error(e.getMessage());
				}

				varBtcUmic2.setTablacalc1aseg1(btcUmic.getTablacalc1aseg2());

				if ((!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL))
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA))
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR))) {
					if (btcUmic.getTablasConversionAsegurado() == null
							|| btcUmic.getTablasConversionAsegurado().isEmpty()) {
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC,
								new String[] { null, "tablasConversionAsegurado" });
					}

					List<List<TablaConversion>> tablaConvAseg = new ArrayList<List<TablaConversion>>();
					tablaConvAseg.add(btcUmic.getTablasConversionAsegurado().get(1));

					varBtcUmic2.setTablasConversionAsegurado(tablaConvAseg);
				} else {
					List<Integer> tablaBaseExp = new ArrayList<Integer>();
					tablaBaseExp.add(btcUmic.getTablaBaseExp().get(1));
					varBtcUmic2.setTablaBaseExp(tablaBaseExp);
				}

				mapVariables.put(CLAVE_BTC_UMIC2, varBtcUmic2);

			}
			varProyFptozcl2 = proyUmic;
			fptozcl2 = (BigDecimal) moduloFPTOZC.execute(varProyFptozcl2, bloqueCorriente, iteracion, fcalc, varUmic2, varBtcUmic2, mapVariables, codSubproceso);
			
			fptozcl = (fptozcl1.add(fptozcl2)).divide(BigDecimal.valueOf(2));
		}else {
			fptozcl1 = (BigDecimal) moduloFPTOZC.execute(varProyFptozcl1, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			fptozcl = fptozcl1;
		}
			
		// Se guarda la fecha devengo del periodo actual para usarlo en las siguientes iteraciones
		UtilModulos.setVarFecDevengoAnterior(mapVariables, CLAVE_VAR_FD_ANT, iteracion, bloqueCorriente.getFechaDevengo());
		
		if (ModuloFPTOZCL.LOG.isTraceEnabled()) {
			ModuloFPTOZCL.LOG.trace("Fin función << moduloFPTOZCL >> de la clase moduloFPTOZCL, para la iteracion = {}, con resultado fptozcl = {}", iteracion, fptozcl);
		}
		
		if (iteracion == proyUmic.size()) {
			umic.getAsegurados().setCsexAseg1((String) mapVariables.get(CLAVE_SEX_ASEG1));
			umic.getAsegurados().setEdadAseg1((Integer) mapVariables.get(CLAVE_EDAD_ASEG1));
			umic.getAsegurados().setFnacAseg1((Timestamp) mapVariables.get(CLAVE_FNAC_ASEG1));
			btcUmic.setTablacalc1aseg1((String) mapVariables.get(CLAVE_TABLACALC_ASEG1));
		}

		
		return fptozcl;
	}

}