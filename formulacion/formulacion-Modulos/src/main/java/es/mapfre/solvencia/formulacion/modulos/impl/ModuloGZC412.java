package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo GZC412. *
 * 
 * @author etorresj
 *
 */
public class ModuloGZC412 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZC412.class);

	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_GZC412;
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
			if (ModuloGZC412.LOG.isTraceEnabled()) {
				ModuloGZC412.LOG.trace("Inicio de execute en clase ModuloGZC412");
			}

			// Recuperamos los datos que le pasaremos a la función moduloGZC412
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.

			// Invocamos a la función de calculo GZC412
			resultado = moduloGZC412(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,
					codSubproceso);

		} catch (Solvencia2Excepcion e) {
			ModuloGZC412.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZC412.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloGZC412.LOG.isTraceEnabled()) {
			ModuloGZC412.LOG.trace("Fin de execute en clase ModuloGZC412");
		}

		return resultado;
	}

	private BigDecimal moduloGZC412(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente,
			final Integer iteracion, final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic,
			final Map<String, Object> mapVariables, final String codSubproceso) {
		// Variables locales
		BigDecimal gzc412 = BigDecimal.ZERO, gzc004 = BigDecimal.ZERO, gzc017 = BigDecimal.ZERO;
		Modulo moduloGZC004, moduloGZC017;
		BigDecimal varGipc = BigDecimal.ZERO;
		BigDecimal varFlujoNominal_Vida = BigDecimal.ZERO;
		BigDecimal varFlujoNominal_Fall = BigDecimal.ZERO;
		BigDecimal auxiliar = BigDecimal.ZERO;
		BigDecimal calcAux1, calcAux2 = BigDecimal.ZERO;
		List<DetalleCorriente> varProyzc004 = proyUmic;

		// Fin variables locales

		if (ModuloGZC412.LOG.isTraceEnabled()) {
			ModuloGZC412.LOG.trace("Inicio función << moduloGZC412 >> de la clase ModuloGZCRTA, para la iteracion = {}",
					iteracion);
		}

		// Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		varGipc = btcUmic.getGtorosspPrima().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);

		moduloGZC004 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZC004);
		gzc004 = (BigDecimal) moduloGZC004.execute(varProyzc004, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
				mapVariables, codSubproceso);

		if (umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_1_STRING)) {
			if (null == proyUmic.get(iteracion - 1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA)
					.getImpFlujoNominal()) {
				varFlujoNominal_Vida = BigDecimal.ZERO;
			} else {
				varFlujoNominal_Vida = proyUmic.get(iteracion - 1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA)
						.getImpFlujoNominal();
			}
			calcAux1 = BigDecimal.ONE.subtract(varGipc);
			calcAux2 = BigDecimal.ONE.divide(calcAux1, ConstantsFunciones.MATH_CONTEXT);
			auxiliar = calcAux2.subtract(BigDecimal.ONE);
			gzc017 = varFlujoNominal_Vida.multiply(auxiliar);
		}
		
		if (umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_2_STRING)) {
			if (null == proyUmic.get(iteracion - 1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_FALL)
					.getImpFlujoNominal()) {
				varFlujoNominal_Fall = BigDecimal.ZERO;
			} else {
				varFlujoNominal_Fall = proyUmic.get(iteracion - 1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_FALL)
						.getImpFlujoNominal();
			}
			calcAux1 = BigDecimal.ONE.subtract(varGipc);
			calcAux2 = BigDecimal.ONE.divide(calcAux1, ConstantsFunciones.MATH_CONTEXT);
			auxiliar = calcAux2.subtract(BigDecimal.ONE);
			gzc017 = varFlujoNominal_Fall.multiply(auxiliar);
		}
		
		if (umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_3_STRING)) {
			if (null == proyUmic.get(iteracion - 1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_FALL)
					.getImpFlujoNominal()) {
				varFlujoNominal_Fall = BigDecimal.ZERO;
			} else {
				varFlujoNominal_Fall = proyUmic.get(iteracion - 1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_FALL)
						.getImpFlujoNominal();
			}
			calcAux1 = BigDecimal.ONE.subtract(varGipc);
			calcAux2 = BigDecimal.ONE.divide(calcAux1, ConstantsFunciones.MATH_CONTEXT);
			auxiliar = calcAux2.subtract(BigDecimal.ONE);
			gzc017 = varFlujoNominal_Fall.multiply(auxiliar);
		}

	
		gzc412 = gzc004.add(gzc017);

		if (ModuloGZC412.LOG.isTraceEnabled()) {
			ModuloGZC412.LOG.trace(
					"Fin función << moduloGZC412 >> de la clase ModuloGZC412, para la iteracion = {} con resultado GZC412 = {}",
					iteracion, gzc412);
		}

		return gzc412;
	}

}
