

package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
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
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;


public class ModuloVZCL implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVZCL.class);

	// Estas variables se ponen como estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VZCL;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA.concat(CLAVE_MODULO);

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
			if (ModuloVZCL.LOG.isTraceEnabled()) {
				ModuloVZCL.LOG.trace("Inicio de execute en clase ModuloVZCL");
			}

			// Recuperamos los datos que le pasaremos a la función moduloGZC002
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];

			// Invocamos a la función de calculo VZC
			resultado = moduloVZCL(proyUmic, bloqueCorriente, iteracion, fcalc,
					umic, btcUmic, mapVariables, codSubproceso);

		} catch (Solvencia2Excepcion e) {
			ModuloVZCL.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVZCL.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(
					ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloVZCL.LOG.isTraceEnabled()) {
			ModuloVZCL.LOG.trace("Fin de execute en clase ModuloVZCL");
		}

		return resultado;

	}

	/**
	 * Modulo de cálculo que devuelve los factores de probabilización a aplicar
	 * en el cálculo de la cuantía probable de una garantía. El módulo
	 * Vzcl(fcal,j) determina la probabilidad de que una cabeza de edad zc en la
	 * fecha de cálculo, fcal, alcance con vida la fecha j en la que se devenga
	 * la prestación.
	 *
	 * @param proyUmic
	 *            Estructura detalleCorrientes de la umic
	 * @param bloqueCorriente
	 *            Bloque de Trabajo de la corriente
	 * @param iteracion
	 *            Indica el periodo de proyección J que se está calculando de
	 *            entre todos los periodos de proyección de la umic (proyUmic)
	 * @param fcalc
	 *            Fecha de calculo
	 * @param umic
	 *            Contiene los datos de la Umic que se está procesando
	 * @param btcUmic
	 *            Contiene el detalle de la base técnica de cálculo para la umic
	 * @param mapVariables
	 *            mapa con las variables de memoria necesarias
	 * @param codSubproceso
	 *            Código del subproceso que se está ejecutando
	 * @return vzcj
	 */
	private BigDecimal moduloVZCL(final List<DetalleCorriente> proyUmic,
			final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic,
			final DetalleBaseTecnica btcUmic,
			final Map<String, Object> mapVariables, final String codSubproceso) {
		// Variables locales
		String varCriterioFec = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriterEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal vzcJ = BigDecimal.ZERO;
		
		Modulo moduloVZC;
		Modulo moduloVZC2;
		BigDecimal varVzcl1 = BigDecimal.ZERO;
		BigDecimal varVzcl2 = BigDecimal.ZERO;
		List<DetalleCorriente> varProyVzcl1;
		// Fin variables locales

		if (ModuloVZCL.LOG.isTraceEnabled()) {
			ModuloVZCL.LOG
				.trace("Inicio función << moduloVZCL >> de la clase ModuloVZCL, para la entrada iteracion = {}",
						iteracion);
		}

		// Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic,
				bloqueCorriente, fcalc, umic, btcUmic);


		if (null == bloqueCorriente.getFechaDevengo()) {
			return vzcJ;
		}
		
		varCriterEdad = UtilModulos.getVarCriEdad(mapVariables,
				CLAVE_VAR_CRIEDAD, umic.getDatosGenerales().getCcartera(), umic
						.getDatosGenerales().getKmodalidad(), umic
						.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(),
				ConstantsModulos.CTE_VA_CRIT_EDA);
		varCriterioFec = UtilModulos.getVarCriFec(mapVariables,
				CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic
						.getDatosGenerales().getKmodalidad(), umic
						.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);

		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos
				.validarVariableDeApoyoVarCriEdad(varCriterEdad);
			ValidacionesComunesModulos
				.validarVariableDeApoyoVarCriterioFecha(varCriterioFec);
		}
		varProyVzcl1 = proyUmic; 
		moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
		moduloVZC2 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC2);
		if (umic.getAsegurados().getFnacAseg2() != null && umic.getAsegurados().getCsexAseg2() != null
				&& umic.getAsegurados().getEdadAseg2() != null) {
			varVzcl1 = (BigDecimal) moduloVZC.execute(varProyVzcl1, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			varVzcl2 = (BigDecimal) moduloVZC2.execute(varProyVzcl1, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
			vzcJ = (varVzcl1.add(varVzcl2)).divide(BigDecimal.valueOf(2));
			
		}else {
			varVzcl1 = (BigDecimal) moduloVZC.execute(varProyVzcl1, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			vzcJ = varVzcl1;
		}
		
		if (ModuloVZCL.LOG.isTraceEnabled()) {
			ModuloVZCL.LOG.trace("Fin función << moduloVZCL >> de la clase ModuloVZCL, para la iteracion = {},  con resultado vzcl = {}",
						iteracion, vzcJ);
		}

		return vzcJ;
		
	}

}
