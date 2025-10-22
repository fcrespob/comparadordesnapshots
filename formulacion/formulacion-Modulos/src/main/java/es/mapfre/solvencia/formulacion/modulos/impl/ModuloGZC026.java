package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Usaremos este módulo para el cálculo de la cuantía nominal de gastos sobre
 * prima para la garantía 26.
 * 
 * @author NFQ
 *
 */
public class ModuloGZC026 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZC026.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_GZC026;
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC;

	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	// Fin de las variables estáticas usadas para agilizar operaciones.

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.mapfre.solvencia.formulacion.modulos.Modulo#execute(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		// Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		// Fin variables locales

		try {
			if (ModuloGZC026.LOG.isTraceEnabled()) {
				ModuloGZC026.LOG.trace("Inicio de execute en clase ModuloGZC026");
			}

			// Recuperamos los datos que le pasaremos a la función ModuloGZC026
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.

			// Invocamos a la función de calculo GZC026
			resultado = moduloGZC026(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,
					codSubproceso);

		} catch (Solvencia2Excepcion e) {
			ModuloGZC026.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZC026.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloGZC026.LOG.isTraceEnabled()) {
			ModuloGZC026.LOG.trace("Fin de execute en clase ModuloGZC026");
		}

		return resultado;
	}

	/**
	 * Usaremos este módulo para el cálculo de la cuantía nominal de gastos sobre
	 * prima para la garantía 26.
	 * 
	 * @param proyUmic
	 * @param bloqueCorriente
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param mapVariables
	 * @param codSubproceso
	 * @return
	 */
	private BigDecimal moduloGZC026(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, Map<String, Object> mapVariables,
			String codSubproceso) {

		BigDecimal gzc026 = BigDecimal.ZERO;
		String varCriterFec;
		Integer varDiasBeta;
		Integer varDiasVto;
		Timestamp varFecVcto;
		BigDecimal varPrimaTarada;
		BigDecimal varGipc;

		if (ModuloGZC026.LOG.isTraceEnabled()) {
			ModuloGZC026.LOG.trace("Inicio función << moduloGZC026 >> de la clase ModuloGZC026, para la iteracion = {}",
					iteracion);
		}

		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		if (bloqueCorriente.getFechaDevengo() != null) {

			varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(),
					umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);

			
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			Date date = null;
			try {
				date = dateFormat.parse("99991231");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			long time = date.getTime();

			if (!umic.getFechas().getFecefecfin().equals(new Timestamp(time))) {
				varFecVcto = umic.getFechas().getFecefecfin();
			} else {
				varFecVcto = proyUmic.get(proyUmic.size()-1).getFechaHasta();
			}

			varDiasVto = FuncionesAuxiliares.nDias(fcalc, varFecVcto, varCriterFec);
			varPrimaTarada = umic.getPrimas().getIprimatarada();
			varGipc = umic.getBti().getPgastgesin2I().divide(new BigDecimal("100"), ConstantsFunciones.MATH_CONTEXT);

//			varDiasBeta = FuncionesAuxiliares.nDias(bloqueCorriente.getFechaDevengo(), umic.getFechas().getFecefecfin(),
//					varCriterFec);
//			
			Timestamp varfechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());

			
			varDiasBeta = FuncionesAuxiliares.tcm(varfechaEfecto, varFecVcto);

			//BigDecimal aux1 = new BigDecimal(varDiasBeta).divide(new BigDecimal(varDiasVto), ConstantsFunciones.MATH_CONTEXT);

			gzc026 = varPrimaTarada.multiply(varGipc).divide(new BigDecimal(varDiasBeta), ConstantsFunciones.MATH_CONTEXT);
		}

		if (ModuloGZC026.LOG.isTraceEnabled()) {
			ModuloGZC026.LOG.trace(
					"Fin función << moduloGZC026 >> de la clase ModuloGZC026, para la iteracion = {}, con resultado gzc026 = {}",
					iteracion, gzc026);
		}
		return gzc026;
	}

}
