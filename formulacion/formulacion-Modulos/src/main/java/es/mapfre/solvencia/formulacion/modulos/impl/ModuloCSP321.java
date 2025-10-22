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
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilFechas.Fecha;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloCSP321 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP321.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP321;

	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

	private static final String CLAVE_VAR_TC = ConstantsModulos.CTE_VAR_TC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC;
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
			if (ModuloCSP321.LOG.isTraceEnabled()) {
				ModuloCSP321.LOG.trace("Inicio de execute en clase PRI002");
			}

			// Recuperamos los datos que le pasaremos a la función moduloPRI002
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de datos.

			// Invocamos a la función moduloPRI002
			resultado = moduloCSP321(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);

		} catch (Solvencia2Excepcion e) {
			ModuloCSP321.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP321.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloCSP321.LOG.isTraceEnabled()) {
			ModuloCSP321.LOG.trace("Fin de execute en clase CSP_321");
		}

		return resultado;
	}

	private BigDecimal moduloCSP321(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente,
			final Integer iteracion, final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic,
			final Map<String, Object> mapVariables) {
		// Variables locales
		String varCriterioFecha;
		Integer varTCm;
		BigDecimal csp321 = BigDecimal.ZERO;
		BigDecimal coeficienteCarencia = BigDecimal.ZERO;
		Integer varBeta;
		Timestamp varFechaEfecto;
		BigDecimal varIcapIni = BigDecimal.ZERO;
		BigDecimal varPRC;
		Integer exponente;
		// Fin variables locales

		if (ModuloCSP321.LOG.isTraceEnabled()) {
			ModuloCSP321.LOG.trace(
					"Inicio función << ModuloCSP321 >> de la clase ModuloCSP321, para la iteracion = {}", iteracion);
		}
		
		if(bloqueCorriente.getFechaDevengo() == null){	
			return csp321;
		}

		// validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		// Obtención y validación del criterio de fecha.
		varCriterioFecha = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC,
				umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(),
				umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);

		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la
			// forma en que se gestiona el paso de umic).
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterioFecha);
			// Fin de la obtención y validación del criterio de fecha.
		}
		varIcapIni = umic.getCapitales().getIcapini();
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(),
				umic.getFechas(), umic.getCapitales().getIsaldo());
		// Meses completes transcurridos desde efecto póliza a momento cálculo
		varTCm = UtilModulos.getVarTCmIni(mapVariables, CLAVE_VAR_TCM, ConstantsModulos.CTE_FIRST_ITER, varFechaEfecto,
				fcalc);
		// β – Periodo mensual de proyección: Secuencial con inicio en valor 0, y final
		// en el último periodo de proyección.
		varBeta = iteracion - 1;
		Fecha fechaEfecto = UtilFechas.getFecha(varFechaEfecto);
		Fecha fechaPago = UtilFechas.getFecha(bloqueCorriente.getFechaPago());
		exponente = (varTCm + varBeta) / ConstantsFunciones.CTE_12;
		varPRC = umic.getCapitales().getPorevalcap().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);

		if (exponente == ConstantsFunciones.CTE_0) {
			coeficienteCarencia = ConstantsFunciones.CTE_OPER_1ENTRE4;
		} else if (exponente == ConstantsFunciones.CTE_1) {
			coeficienteCarencia = ConstantsFunciones.CTE_OPER_3ENTRE4;
		} else if (exponente == ConstantsFunciones.CTE_2) {
			coeficienteCarencia = ConstantsFunciones.CTE_OPER_3ENTRE4;
		} else if (exponente > ConstantsFunciones.CTE_2) {
			coeficienteCarencia = ConstantsFunciones.CTE_OPER_1;
		}
		/**
		 * Cf_321=Ej*ICAPINI*(1+prC/100)^(ent(tcm+beta)/12))
		 */

		csp321 = coeficienteCarencia.multiply(varIcapIni).multiply((Util.pow(BigDecimal.ONE.add(varPRC), exponente)));
				

		if (ModuloCSP321.LOG.isTraceEnabled()) {
			ModuloCSP321.LOG.trace(
					"Fin función << moduloCSP321 >> de la clase ModuloCSP321, para la iteracion = {}, con resultado csp321 = {}",
					iteracion, csp321);
		}

		return csp321;
	}

}
