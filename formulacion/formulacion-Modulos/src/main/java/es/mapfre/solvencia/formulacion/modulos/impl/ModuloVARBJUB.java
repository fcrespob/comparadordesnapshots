package es.mapfre.solvencia.formulacion.modulos.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
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

/**
 * 
 * @author Aleksandar Plamenov Nedyalkov
 *
 */

public class ModuloVARBJUB implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVARBJUB.class);

	// Inicio de las variables estáticas para agilizar operaciones.

	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VARBJUB;

	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

	private static final String CLAVE_VAR_UMIC_COPIA = ConstantsModulos.CTE_VA_UMIC_COPIA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_BTCUMIC_COPIA = ConstantsModulos.CTE_VA_BTCUMIC_COPIA.concat(CLAVE_MODULO);

	private static final String CLAVE_VA_CRIT_EDA = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_VA_CRIT_EDA.concat(CLAVE_MODULO);

	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC;
	public static final String CLAVE_VAR_EDAD_CAL1 = ConstantsModulos.CTE_EDAD_CAL1.concat(CLAVE_MODULO);

	private static final String CLAVE_VAR_ZC1 = ConstantsModulos.CTE_VAR_ZC1.concat(CLAVE_MODULO);

	public static final String CLAVE_VAR_VAL_TAB_MORT1 = ConstantsModulos.CTE_VAR_VAL_TAB_MORT1.concat(CLAVE_MODULO);

	private static final String CLAVE_VAR_LZC1 = ConstantsModulos.CTE_VAR_LZC1.concat(CLAVE_MODULO);

	private static final String CLAVE_VAR_DIFERCOL = ConstantsModulos.CTE_VAR_DIFERCOL.concat(CLAVE_MODULO);
	// Fin de las variables estáticas usadas para agilizar operaciones.

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la
	 * llamada a la función que realiza los calculos del modulo.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		// Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		// Fin variables locales

		try {

			if (ModuloVARBJUB.LOG.isTraceEnabled()) {
				ModuloVARBJUB.LOG.trace("Inicio de execute en clase ModuloVARBJUB");
			}

			// Recuperamos los datos
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubProceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];

			// Invocamos a la función de calculo VARBJUB
			resultado = moduloVARBJUB(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,
					codSubProceso);

		} catch (Solvencia2Excepcion e) {
			ModuloVARBJUB.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVARBJUB.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloVARBJUB.LOG.isTraceEnabled()) {
			ModuloVARBJUB.LOG.trace("Fin de execute en clase ModuloVARBJUB");
		}

		return resultado;
	}

	/**
	 * Modulo de cálculo que devuelve los factores de probabilización a aplicar en
	 * el cálculo de la cuantía probable de una garantía, Modulo Supervivencia Renta
	 * Diferida con Reversión por nodo y 2 niveles.
	 * 
	 * @param proyUmic
	 * @param bloqueCorriente
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param mapVariables
	 * @param codSubProceso
	 * @return varbjub
	 */
	private BigDecimal moduloVARBJUB(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, Map<String, Object> mapVariables,
			String codSubProceso) {
		BigDecimal varbjub = BigDecimal.ZERO;
		String varCriterioEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriterioFecha = ConstantsFunciones.CTE_CADENA_VACIA;
		Umic varUmicCopia = new Umic();
		DetalleBaseTecnica varbtcUmicCopia = new DetalleBaseTecnica();
		Integer varIndAsegOrigen = 0;
		BigDecimal varLzc1 = BigDecimal.ZERO;
		BigDecimal varEdadDifer = BigDecimal.ZERO;
		BigDecimal varVzc1zc2 = BigDecimal.ZERO;;
		BigDecimal varVzc1 = null;
		BigDecimal varVzc2 = null;
		BigDecimal varVzc1Difercol;
		BigDecimal varEdadCalc1;
		List<BigDecimal> lstValoresTabMort1 = null;
		Timestamp varFechaEfecto;
		Modulo moduloVZC;
		Modulo moduloVZC2;
		String tablaOrigen = null;
		Timestamp fNacAsegOrigen = null;
		String sexAsegOrigen = null;
		Integer edadAsegOrigen = null;
		List<DetalleCorriente> varProyVzc = null;
		List<DetalleCorriente> varProyVZC2 = null;

		if (ModuloVARBJUB.LOG.isTraceEnabled()) {
			ModuloVARBJUB.LOG.trace(
					"Inicio función << ModuloVARBJUB >> de la clase ModuloVARBJUB, para la entrada iteracion = {}",
					iteracion);
		}
		// Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, fcalc, umic, btcUmic);

		varCriterioEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD,
				umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(),
				umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_VA_CRIT_EDA);
		varCriterioFecha = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC,
				umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(),
				umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);

		ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterioEdad);
		ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterioFecha);

		if (mapVariables.get(CLAVE_VAR_UMIC_COPIA) == null) {
			try {
				PropertyUtils.copyProperties(varUmicCopia, umic);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mapVariables.put(CLAVE_VAR_UMIC_COPIA, varUmicCopia);
		} else {
			varUmicCopia = (Umic) mapVariables.get(CLAVE_VAR_UMIC_COPIA);
		}

		if (btcUmic.getBaseTec().equalsIgnoreCase("NIIF17") || btcUmic.getBaseTec().equalsIgnoreCase("BEL") || mapVariables.get(CLAVE_VAR_BTCUMIC_COPIA) == null) {
			try {
				PropertyUtils.copyProperties(varbtcUmicCopia, btcUmic);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mapVariables.put(CLAVE_VAR_BTCUMIC_COPIA, varbtcUmicCopia);
		} else {
			varbtcUmicCopia = (DetalleBaseTecnica) mapVariables.get(CLAVE_VAR_BTCUMIC_COPIA);
		}

		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(),
				umic.getFechas(), umic.getCapitales().getIsaldo());

		if (umic.getDatosGenerales().getKmodalidad().equals(228)
				|| umic.getDatosGenerales().getKmodalidad().equals(362)) {
			if (umic.getDatosDescuentos().getFecJubilacion() == null) {
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_HB);
			}
			if (fcalc.before(umic.getDatosDescuentos().getFecJubilacion())) {
				Integer varEdifer = umic.getDatosGenerales().getEdifer();
				varEdadCalc1 = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL1, varFechaEfecto,
						umic.getAsegurados().getFnacAseg1(), varCriterioEdad, umic.getRentas().getFecIni(), varEdifer);

				lstValoresTabMort1 = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT1, umic, btcUmic,
						IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterioEdad, ConstantesSolvencia.CTE_TABMORT_L);

				varEdadDifer = UtilModulos.getVarZc(mapVariables, CLAVE_VAR_ZC1, varEdadCalc1, varFechaEfecto, fcalc,
						varCriterioFecha,varCriterioEdad);
				
				int varLDifercolEntero = varEdadDifer.intValue();
				//varLDifercolEntero = varZc1Entero
				
				if (varLDifercolEntero > lstValoresTabMort1.size() - 2) {
					varLzc1 = BigDecimal.ZERO;
				} else {
					varLzc1 = UtilModulos.getVarLzc(mapVariables, CLAVE_VAR_LZC1, varEdadDifer,
							lstValoresTabMort1.get(varLDifercolEntero), lstValoresTabMort1.get(varLDifercolEntero + 1));
				}

				varVzc1Difercol = UtilModulos.getVarVzc1DifercolJubilacion(mapVariables, CLAVE_VAR_DIFERCOL, varFechaEfecto,
						umic.getDatosDescuentos().getFecJubilacion(), fcalc, varEdadCalc1, lstValoresTabMort1, varLzc1, varCriterioFecha,
						BigDecimal.ONE);
			} else {
				varVzc1Difercol = BigDecimal.ONE;
			}
		} else {
			if (umic.getRentas().getFecIni().after(fcalc)) {

				Integer varEdifer = umic.getDatosGenerales().getEdifer();
				varEdadCalc1 = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL1, varFechaEfecto,
						umic.getAsegurados().getFnacAseg1(), varCriterioEdad, umic.getRentas().getFecIni(), varEdifer);

				lstValoresTabMort1 = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT1, umic, btcUmic,
						IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterioEdad, ConstantesSolvencia.CTE_TABMORT_L);

				varEdadDifer = UtilModulos.getVarZc(mapVariables, CLAVE_VAR_ZC1, varEdadCalc1, varFechaEfecto, fcalc,
						varCriterioFecha,varCriterioEdad);
				
				int varLDifercolEntero = varEdadDifer.intValue();
				//varLDifercolEntero = varZc1Entero
				
				if (varLDifercolEntero > lstValoresTabMort1.size() - 2) {
					varLzc1 = BigDecimal.ZERO;
				} else {
					varLzc1 = UtilModulos.getVarLzc(mapVariables, CLAVE_VAR_LZC1, varEdadDifer,
							lstValoresTabMort1.get(varLDifercolEntero), lstValoresTabMort1.get(varLDifercolEntero + 1));
				}

				varVzc1Difercol = UtilModulos.getVarVzc1Difercol(mapVariables, CLAVE_VAR_DIFERCOL, varFechaEfecto,
						umic.getRentas().getFecIni(), fcalc, varEdadCalc1, lstValoresTabMort1, varLzc1, varCriterioFecha,
						BigDecimal.ONE);
			} else {
				varVzc1Difercol = BigDecimal.ONE;
			}
		}
		

		// ==============================================================================

		if (null != bloqueCorriente.getFechaDevengo()) {
			if (umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_1_STRING)) {
				varProyVzc = proyUmic;
				moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
				varVzc1 = (BigDecimal) moduloVZC.execute(varProyVzc, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
						mapVariables, codSubProceso);

				varbjub = varVzc1;

			} else if (umic.getDatosGenerales().getNorden().toString().substring(0, 1)
					.equals(ConstantsFunciones.CTE_2_STRING)) {
				varProyVzc = proyUmic;
				moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
				varVzc1 = (BigDecimal) moduloVZC.execute(varProyVzc, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
						mapVariables, codSubProceso);

				// sobreescribir el asegurado1 con los datos del asegurado 2
				
				if (umic.getDatosGenerales().getNorden().toString().substring(1, 2)
						.equals(ConstantsFunciones.CTE_1_STRING)) {
					varIndAsegOrigen = ConstantsFunciones.CTE_2;

					tablaOrigen = btcUmic.getTablacalc1aseg2();
					fNacAsegOrigen = umic.getAsegurados().getFnacAseg2();
					sexAsegOrigen = umic.getAsegurados().getCsexAseg2();
					edadAsegOrigen = umic.getAsegurados().getEdadAseg2();
					
					mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG2);
				}

				if (umic.getDatosGenerales().getNorden().toString().substring(1, 2)
						.equals(ConstantsFunciones.CTE_2_STRING)) {
					
					if (null != btcUmic.getTablacalc1aseg3() 
							&& null != umic.getAsegurados().getFnacAseg3() 
							&& null != umic.getAsegurados().getCsexAseg3() 
							&& null != umic.getAsegurados().getEdadAseg3()){
						
					
						varIndAsegOrigen = ConstantsFunciones.CTE_3;

						tablaOrigen = btcUmic.getTablacalc1aseg3();
						fNacAsegOrigen = umic.getAsegurados().getFnacAseg3();
						sexAsegOrigen = umic.getAsegurados().getCsexAseg3();
						edadAsegOrigen = umic.getAsegurados().getEdadAseg3();
						
						mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG3);
						
					} else {
						varIndAsegOrigen = ConstantsFunciones.CTE_2;

						tablaOrigen = btcUmic.getTablacalc1aseg2();
						fNacAsegOrigen = umic.getAsegurados().getFnacAseg2();
						sexAsegOrigen = umic.getAsegurados().getCsexAseg2();
						edadAsegOrigen = umic.getAsegurados().getEdadAseg2();
						
						mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG2);
					}
				}
				
				if (umic.getDatosGenerales().getNorden().toString().substring(1, 2)
						.equals(ConstantsFunciones.CTE_3_STRING)) {
					
					if(null != btcUmic.getTablacalc1aseg4() 
							&& null != umic.getAsegurados().getFnacAseg4() 
							&& null != umic.getAsegurados().getCsexAseg4() 
							&& null != umic.getAsegurados().getEdadAseg4()){
						varIndAsegOrigen = ConstantsFunciones.CTE_4;

						tablaOrigen = btcUmic.getTablacalc1aseg4();
						fNacAsegOrigen = umic.getAsegurados().getFnacAseg4();
						sexAsegOrigen = umic.getAsegurados().getCsexAseg4();
						edadAsegOrigen = umic.getAsegurados().getEdadAseg4();
						
						mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG4);
						
					} else if (null != btcUmic.getTablacalc1aseg3() 
							&& null != umic.getAsegurados().getFnacAseg3() 
							&& null != umic.getAsegurados().getCsexAseg3() 
							&& null != umic.getAsegurados().getEdadAseg3()){
						varIndAsegOrigen = ConstantsFunciones.CTE_3;

						tablaOrigen = btcUmic.getTablacalc1aseg3();
						fNacAsegOrigen = umic.getAsegurados().getFnacAseg3();
						sexAsegOrigen = umic.getAsegurados().getCsexAseg3();
						edadAsegOrigen = umic.getAsegurados().getEdadAseg3();
						
						mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG3);
						
					} else {
						varIndAsegOrigen = ConstantsFunciones.CTE_2;

						tablaOrigen = btcUmic.getTablacalc1aseg2();
						fNacAsegOrigen = umic.getAsegurados().getFnacAseg2();
						sexAsegOrigen = umic.getAsegurados().getCsexAseg2();
						edadAsegOrigen = umic.getAsegurados().getEdadAseg2();
						
						mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG2);
					}
					
					
				}
				
				if (umic.getDatosGenerales().getNorden().toString().substring(1, 2)
						.equals(ConstantsFunciones.CTE_4_STRING)) {
					
					if(null != btcUmic.getTablacalc1aseg5() 
							&& null != umic.getAsegurados().getFnacAseg5() 
							&& null != umic.getAsegurados().getCsexAseg5() 
							&& null != umic.getAsegurados().getEdadAseg5()){
						varIndAsegOrigen = ConstantsFunciones.CTE_5;

						tablaOrigen = btcUmic.getTablacalc1aseg5();
						fNacAsegOrigen = umic.getAsegurados().getFnacAseg5();
						sexAsegOrigen = umic.getAsegurados().getCsexAseg5();
						edadAsegOrigen = umic.getAsegurados().getEdadAseg5();
						
						mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG5);
					
					} else if(null != btcUmic.getTablacalc1aseg4() 
							&& null != umic.getAsegurados().getFnacAseg4() 
							&& null != umic.getAsegurados().getCsexAseg4() 
							&& null != umic.getAsegurados().getEdadAseg4()){
						varIndAsegOrigen = ConstantsFunciones.CTE_4;

						tablaOrigen = btcUmic.getTablacalc1aseg4();
						fNacAsegOrigen = umic.getAsegurados().getFnacAseg4();
						sexAsegOrigen = umic.getAsegurados().getCsexAseg4();
						edadAsegOrigen = umic.getAsegurados().getEdadAseg4();
						
						mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG4);
						
					} else if (null != btcUmic.getTablacalc1aseg3() 
							&& null != umic.getAsegurados().getFnacAseg3() 
							&& null != umic.getAsegurados().getCsexAseg3() 
							&& null != umic.getAsegurados().getEdadAseg3()){
						varIndAsegOrigen = ConstantsFunciones.CTE_3;

						tablaOrigen = btcUmic.getTablacalc1aseg3();
						fNacAsegOrigen = umic.getAsegurados().getFnacAseg3();
						sexAsegOrigen = umic.getAsegurados().getCsexAseg3();
						edadAsegOrigen = umic.getAsegurados().getEdadAseg3();
						
						mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG3);
						
					} else {
						varIndAsegOrigen = ConstantsFunciones.CTE_2;

						tablaOrigen = btcUmic.getTablacalc1aseg2();
						fNacAsegOrigen = umic.getAsegurados().getFnacAseg2();
						sexAsegOrigen = umic.getAsegurados().getCsexAseg2();
						edadAsegOrigen = umic.getAsegurados().getEdadAseg2();
						
						mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG2);
					}
				}
				
				if(varbtcUmicCopia.getTablasConversionAsegurado().size() > 1) {
					UtilModulos.fSobreescribirAsegurado(varUmicCopia, varbtcUmicCopia, tablaOrigen, varIndAsegOrigen,
							fNacAsegOrigen, sexAsegOrigen, edadAsegOrigen);
				}
				varbtcUmicCopia.setTablaBaseExpList(btcUmic.getTablaBaseExpList());
				
				varProyVZC2 = proyUmic;
				moduloVZC2 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
				varVzc2 = (BigDecimal) moduloVZC2.execute(varProyVZC2, bloqueCorriente, iteracion, fcalc, varUmicCopia,
						varbtcUmicCopia, mapVariables, codSubProceso);

				if (umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_A)) {
					if (umic.getDatosGenerales().getKmodalidad().equals(228)
							|| umic.getDatosGenerales().getKmodalidad().equals(362)) {
						if (fcalc.before(umic.getDatosDescuentos().getFecJubilacion())) {
							varbjub = BigDecimal.ZERO;
						} else {
							varbjub = varVzc2;
						}
					} else {
						if (fcalc.before(umic.getRentas().getFecIni())) {
							varbjub = BigDecimal.ZERO;
						} else {
							varbjub = varVzc2;
						}
					}
					
				} else {
					if (umic.getDatosGenerales().getKmodalidad().equals(228)
							|| umic.getDatosGenerales().getKmodalidad().equals(362)) {
						if (fcalc.before(umic.getDatosDescuentos().getFecJubilacion())) {
							varVzc1zc2 = varVzc1.multiply(varVzc2);
							varbjub = (varVzc1Difercol.multiply(varVzc2)).subtract(varVzc1zc2);
						} else {
							varVzc1zc2 = varVzc1.multiply(varVzc2);
							varbjub = varVzc2.subtract(varVzc1zc2);
						}
					} else {
						if (fcalc.before(umic.getRentas().getFecIni())) {
							varVzc1zc2 = varVzc1.multiply(varVzc2);
							varbjub = (varVzc1Difercol.multiply(varVzc2)).subtract(varVzc1zc2);
						} else {
							varVzc1zc2 = varVzc1.multiply(varVzc2);
							varbjub = varVzc2.subtract(varVzc1zc2);
						}
					}
					
				}
				
			} else if (umic.getDatosGenerales().getNorden().toString().substring(0, 1)
					.equals(ConstantsFunciones.CTE_3_STRING)) {
				varProyVzc = proyUmic;
				moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
				varVzc1 = (BigDecimal) moduloVZC.execute(varProyVzc, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
						mapVariables, codSubProceso);

				// sobreescribir el asegurado1 con los datos del asegurado 2
				
				varIndAsegOrigen = ConstantsFunciones.CTE_5;

				tablaOrigen = btcUmic.getTablacalc1aseg5();
				fNacAsegOrigen = umic.getAsegurados().getFnacAseg5();
				sexAsegOrigen = umic.getAsegurados().getCsexAseg5();
				edadAsegOrigen = umic.getAsegurados().getEdadAseg5();
				
				mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG5);
				
				
				if(varbtcUmicCopia.getTablasConversionAsegurado().size() > 1) {
					UtilModulos.fSobreescribirAsegurado(varUmicCopia, varbtcUmicCopia, tablaOrigen, varIndAsegOrigen,
							fNacAsegOrigen, sexAsegOrigen, edadAsegOrigen);
				}
				
				varbtcUmicCopia.setTablaBaseExpList(btcUmic.getTablaBaseExpList());
				varProyVZC2 = proyUmic;
				moduloVZC2 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
				varVzc2 = (BigDecimal) moduloVZC2.execute(varProyVZC2, bloqueCorriente, iteracion, fcalc, varUmicCopia,
						varbtcUmicCopia, mapVariables, codSubProceso);

				if (umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_A)) {
					if (umic.getDatosGenerales().getKmodalidad().equals(228)
							|| umic.getDatosGenerales().getKmodalidad().equals(362)) {
						if (fcalc.before(umic.getDatosDescuentos().getFecJubilacion())) {
							varbjub = BigDecimal.ZERO;
						} else {
							varbjub = varVzc2;
						}
					} else {
						if (fcalc.before(umic.getRentas().getFecIni())) {
							varbjub = BigDecimal.ZERO;
						} else {
							varbjub = varVzc2;
						}
					}
					
				} else {
					if (umic.getDatosGenerales().getKmodalidad().equals(228)
							|| umic.getDatosGenerales().getKmodalidad().equals(362)) {
						if (fcalc.before(umic.getDatosDescuentos().getFecJubilacion())) {
							varVzc1zc2 = varVzc1.multiply(varVzc2);
							varbjub = (varVzc1Difercol.multiply(varVzc2)).subtract(varVzc1zc2);
						} else {
							varVzc1zc2 = varVzc1.multiply(varVzc2);
							varbjub = varVzc2.subtract(varVzc1zc2);
						}
					} else {
						if (fcalc.before(umic.getRentas().getFecIni())) {
							varVzc1zc2 = varVzc1.multiply(varVzc2);
							varbjub = (varVzc1Difercol.multiply(varVzc2)).subtract(varVzc1zc2);
						} else {
							varVzc1zc2 = varVzc1.multiply(varVzc2);
							varbjub = varVzc2.subtract(varVzc1zc2);
						}
					}
					
				}
			}
		}
		

		if (ModuloVARBJUB.LOG.isTraceEnabled()) {
			ModuloVARBJUB.LOG.trace(
					"Fin función << moduloVARBJUB >> de la clase ModuloVARBJUB, para la iteracion = {}, con resultado varbjub = {}",
					iteracion, varbjub);
		}

		return varbjub;
	}
}
