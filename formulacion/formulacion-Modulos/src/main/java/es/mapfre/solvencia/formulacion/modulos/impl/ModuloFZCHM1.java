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
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Modulo de cálculo que devuelve los factores de probabilización a aplicar en
 * el cálculo de la cuantía probable de una garantía. El módulo FZCHM1(fcal,j)
 * calculará la probabilidad del mismo modo que Fzchm1 cuando exista un hijo
 * minusválido. Se aplicara para las polizas 421021 y 421023, con prestaciones
 * RS216, RS217 y RS218.
 *
 */
public class ModuloFZCHM1 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloFZCHM1.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_FZCHM1;
	private static final String CLAVE_UMIC2 = ConstantsModulos.CTE_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC2 = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_SEX_ASEG1 = ConstantsModulos.CTE_SEX_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_EDAD_ASEG1 = ConstantsModulos.CTE_EDAD_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_FNAC_ASEG1 = ConstantsModulos.CTE_FNAC_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACALC_ASEG1 = ConstantsModulos.CTE_TABLACALC_ASEG1.concat(CLAVE_MODULO)
			.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG1 = ConstantsModulos.CTE_TABLACONV_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG2 = ConstantsModulos.CTE_TABLACONV_ASEG2.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG3 = ConstantsModulos.CTE_TABLACONV_ASEG3.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG4 = ConstantsModulos.CTE_TABLACONV_ASEG4.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG5 = ConstantsModulos.CTE_TABLACONV_ASEG5.concat(CLAVE_MODULO);
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
			if (ModuloFZCHM1.LOG.isTraceEnabled()) {
				ModuloFZCHM1.LOG.trace("Inicio de execute en clase ModuloFZCHM1");
			}

			// Recuperamos los datos que le pasaremos a la función moduloFZCHM1
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de datos.

			// Invocamos a la función ModuloFZCHM1
			resultado = moduloFZCHM1(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso,
					mapVariables);

		} catch (Solvencia2Excepcion e) {
			ModuloFZCHM1.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloFZCHM1.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloFZCHM1.LOG.isTraceEnabled()) {
			ModuloFZCHM1.LOG.trace("Fin de execute en clase FZCHM1");
		}

		return resultado;
	}

	private BigDecimal moduloFZCHM1(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {

		// Variables locales
		BigDecimal FZCHM1 = BigDecimal.ZERO;
		Integer varIdHijo = 0;
		Modulo moduloFZCHM;
		BigDecimal varVzchm;
		List<DetalleCorriente> varProyFzchm = null;
		// Fin variables locales

		if (ModuloFZCHM1.LOG.isTraceEnabled()) {
			ModuloFZCHM1.LOG.trace("Inicio función << moduloFZCHM1 >> de la clase moduloFZCHM1, para la iteracion = {}",
					iteracion);
		}

		// validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		moduloFZCHM = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FZCHM);
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion) || (Timestamp) mapVariables.get(CLAVE_FNAC_ASEG1) == null) {
//			varcsporfa_jant = BigDecimal.ZERO;
//			mapVariables.put(CLAVE_VAR_CSPORFA, varcsporfa_jant);
			mapVariables.put(CLAVE_SEX_ASEG1, umic.getAsegurados().getCsexAseg1());
			mapVariables.put(CLAVE_EDAD_ASEG1, umic.getAsegurados().getEdadAseg1());
			mapVariables.put(CLAVE_FNAC_ASEG1, umic.getAsegurados().getFnacAseg1());
			mapVariables.put(CLAVE_TABLACALC_ASEG1, btcUmic.getTablacalc1aseg1());
			mapVariables.put(CLAVE_TABLACONV_ASEG1, btcUmic.getTablasConversionAsegurado().get(0));
			mapVariables.put(CLAVE_TABLACONV_ASEG2, btcUmic.getTablasConversionAsegurado().get(1));
			mapVariables.put(CLAVE_TABLACONV_ASEG3, btcUmic.getTablasConversionAsegurado().get(2));
			mapVariables.put(CLAVE_TABLACONV_ASEG4, btcUmic.getTablasConversionAsegurado().get(3));
			mapVariables.put(CLAVE_TABLACONV_ASEG5, btcUmic.getTablasConversionAsegurado().get(4));
		}
		if (bloqueCorriente.getFechaDevengo() != null) {

			if (umic.getAsegurados().getFnacAseg3() != null && umic.getAsegurados().getCsexAseg3() != null
					&& umic.getAsegurados().getEdadAseg3() != null) {
				if (umic.getOtrosDatos().getCestadoAseg3().equals(ConstantsModulos.CTE_CESTADO_ASEG_M)) {
					varIdHijo = 3;
				}

			}

			if (umic.getAsegurados().getFnacAseg4() != null && umic.getAsegurados().getCsexAseg4() != null
					&& umic.getAsegurados().getEdadAseg4() != null) {
				if (umic.getOtrosDatos().getCestadoAseg4().equals(ConstantsModulos.CTE_CESTADO_ASEG_M)
						&& umic.getAsegurados().getFnacAseg4().after(umic.getAsegurados().getFnacAseg3())) {
					varIdHijo = 4;
				}
			}

			if (umic.getAsegurados().getFnacAseg5() != null && umic.getAsegurados().getCsexAseg5() != null
					&& umic.getAsegurados().getEdadAseg5() != null) {
				if (umic.getOtrosDatos().getCestadoAseg5().equals(ConstantsModulos.CTE_CESTADO_ASEG_M)
						&& umic.getAsegurados().getFnacAseg5().after(umic.getAsegurados().getFnacAseg4())) {
					varIdHijo = 5;
				}
			}
			if (varIdHijo==0) {

				FZCHM1 = BigDecimal.ONE;

			} else {
				
				varProyFzchm = proyUmic;
				varVzchm = (BigDecimal) moduloFZCHM.execute(varProyFzchm, bloqueCorriente, iteracion, fcalc, umic,
						btcUmic, mapVariables, codSubproceso);
				FZCHM1 = varVzchm;
				}
			
		} else {
			FZCHM1 = BigDecimal.ZERO;
		}
		if (iteracion == proyUmic.size()) {
			umic.getAsegurados().setCsexAseg1((String) mapVariables.get(CLAVE_SEX_ASEG1));
			umic.getAsegurados().setEdadAseg1((Integer) mapVariables.get(CLAVE_EDAD_ASEG1));
			umic.getAsegurados().setFnacAseg1((Timestamp) mapVariables.get(CLAVE_FNAC_ASEG1));
			btcUmic.setTablacalc1aseg1((String) mapVariables.get(CLAVE_TABLACALC_ASEG1));
			List<List<TablaConversion>> tablaConvAseg = new ArrayList<List<TablaConversion>>();
			

			if (mapVariables.get(CLAVE_TABLACONV_ASEG1) != null) {
				tablaConvAseg.add((List<TablaConversion>) (mapVariables.get(CLAVE_TABLACONV_ASEG1)));
			}												
			if (mapVariables.get(CLAVE_TABLACONV_ASEG2) != null) {
				tablaConvAseg.add((List<TablaConversion>) (mapVariables.get(CLAVE_TABLACONV_ASEG2)));
			}
			if (mapVariables.get(CLAVE_TABLACONV_ASEG3) != null) {
				tablaConvAseg.add((List<TablaConversion>) (mapVariables.get(CLAVE_TABLACONV_ASEG3)));
			}
			if (mapVariables.get(CLAVE_TABLACONV_ASEG4) != null) {
				tablaConvAseg.add((List<TablaConversion>) (mapVariables.get(CLAVE_TABLACONV_ASEG4)));
			}
			if (mapVariables.get(CLAVE_TABLACONV_ASEG5) != null) {
				tablaConvAseg.add((List<TablaConversion>) (mapVariables.get(CLAVE_TABLACONV_ASEG5)));
			}
			
			btcUmic.setTablasConversionAsegurado(tablaConvAseg);
		}
		if (ModuloFZCHM1.LOG.isTraceEnabled()) {
			ModuloFZCHM1.LOG.trace(
					"Fin función << moduloFZCHM1>> de la clase ModuloFZCHM1, para la iteracion = {}, con resultado FZCHM1 = {}",
					iteracion, FZCHM1);
		}
		return FZCHM1;
	}
}
