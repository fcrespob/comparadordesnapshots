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
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.FuncionesRentas;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo que devuelve la cuantía de fallecimiento de la
 * garantía principal de la póliza.
 * 
 * @author Aleksandar Plamenov Nedyalkov
 *
 */

public class ModuloCSP230 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP230.class);

	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP230;

	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	private static final String CLAVE_BETA_IDMS = ConstantsModulos.CTE_VA_BETA_IDMS;
	private static final String CLAVE_VAR_BETA_IDMS = CLAVE_BETA_IDMS.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCM = ConstantsFunciones.CTE_VAR_TCM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TTM = ConstantsFunciones.CTE_VAR_TTM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_BETA = ConstantsModulos.CTE_VAR_BETA.concat(CLAVE_MODULO);

	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_CSP230;
	}

	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la
	 * llamada a la función que realiza los calculos del modulo.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(Object... args) throws Solvencia2Excepcion {

		BigDecimal resultado = BigDecimal.ZERO;

		try {
			if (ModuloCSP230.LOG.isTraceEnabled()) {
				ModuloCSP230.LOG.trace("Inicio de execute en clase ModuloCSP230");
			}

			// Recuperamos los datos que le pasaremos a la función moduloCSP230
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];

			// Invocamos a la función de calculo CSP230
			resultado = moduloCSP230(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,
					codSubproceso);

		} catch (Solvencia2Excepcion e) {
			ModuloCSP230.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP230.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloCSP230.LOG.isTraceEnabled()) {
			ModuloCSP230.LOG.trace("Fin de execute en clase ModuloCSP230");
		}

		return resultado;
	}

	/**
	 * Módulo de cálculo de la cuantía de fallecimiento de la garantía principal de
	 * la póliza.
	 * 
	 * @param proyUmic
	 * @param bloqueCorriente
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param mapVariables
	 * @param codSubproceso
	 * @return csp230
	 */
	private BigDecimal moduloCSP230(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, Map<String, Object> mapVariables,
			String codSubproceso) {

		BigDecimal csp230 = BigDecimal.ZERO;
		String varCriterEdad;
		String varCriterFec;
		BigDecimal varBetaIdms;
		Timestamp varFechaEfecto = null;
		BigDecimal varCapInm = BigDecimal.ZERO;
		BigDecimal varAuxCap = BigDecimal.ZERO;
		BigDecimal varCapIni = BigDecimal.ZERO;
		BigDecimal varRentIni = BigDecimal.ZERO;
		BigDecimal varPrr = BigDecimal.ZERO;
		Integer varBeta;
		Integer varEdad;
		Integer varAnoNac = 0;
		BigDecimal varTabMort = BigDecimal.ZERO;
		List<BigDecimal> lstValoresTabMort = null;
		BigDecimal varValoresTabMort = BigDecimal.ZERO;
		Integer varn;
		Integer varnmeses;
		Integer varTcm;
		Integer varTtm;
		BigDecimal vartit1 = BigDecimal.ZERO;
		Integer varEdadj;
		BigDecimal varVrtaf = BigDecimal.ZERO;
		BigDecimal varRenta = BigDecimal.ZERO;
		BigDecimal varRentaAct = BigDecimal.ZERO;
		BigDecimal auxiliar;
		BigDecimal auxiliar1;
		BigDecimal auxiliar2;
		BigDecimal auxiliar3;
		Integer auxiliarExponente;

		if (ModuloCSP230.LOG.isTraceEnabled()) {
			ModuloCSP230.LOG.trace("Inicio función << moduloCSP230 >> para la iteracion = {}", iteracion);
		}

		// Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		if (umic.getDatosGenerales().getCsitupol().equals(ConstantsModulos.CTE_DG_CSITU_ANU)) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A4,
					new String[] { "ClaveUmic:" + umic.getKey() });
		}

		// Definición de variables auxiliares para agilizar las operaciones (se llaman
		// varias veces).
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		// Fin de la definición de las variables auxiliares.

		// Variables de apoyo
		varCriterEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(),
				CLAVE_CRIEDAD);
		varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(),
				CLAVE_CRIFEC);
		varBetaIdms = UtilModulos.getVarBetaIDMS(mapVariables, CLAVE_VAR_BETA_IDMS, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(),
				CLAVE_BETA_IDMS);

		ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterEdad);
		ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFec);
		ValidacionesComunesModulos.validarVariableDeApoyoBetaIDMS(varBetaIdms);

		// VariablesModulo
		varFechaEfecto = UtilModulos.getVarFechaEfecto(mapVariables, CLAVE_FEC_EFEC, umic);
		
		varAuxCap = BigDecimal.ONE.divide(varBetaIdms);
		varCapInm = varAuxCap.multiply(umic.getCapitales().getIcapini());
		
		varCapIni = umic.getCapitales().getIcapini();
		// varRentIni = umic.getRentas().getRentini();
		
		varPrr = BigDecimal.ONE.add(umic.getRentas().getPrevrenta().divide(new BigDecimal(100)));
		varEdad = FuncionesAuxiliares.nEdad(fcalc, umic.getAsegurados().getFnacAseg1(), varCriterEdad,
				umic.getRentas().getFecIni(), umic.getDatosGenerales().getEdifer()).intValue();
		varAnoNac = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1());
		lstValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic,
				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterEdad, ConstantesSolvencia.CTE_TABMORT_L);
		varn = umic.getDuraciones().getNdursegano();
		varnmeses = umic.getDuraciones().getNdursegmes();
		varTcm = UtilModulos.getVarTCmIni(mapVariables, CLAVE_VAR_TCM, iteracion, varFechaEfecto, fcalc);
		varTtm = UtilModulos.getVarTCmIni(mapVariables, CLAVE_VAR_TTM, iteracion, varFechaEfecto,
				umic.getFechas().getFecefecfin());
		vartit1 = umic.getBti().getPintertecnI1();

		varBeta = (Integer) mapVariables.get(CLAVE_VAR_BETA);

		if (ConstantsFunciones.CTE_APOR_REDUCIDA.equals(umic.getDatosGenerales().getCsitupol())){
			BigDecimal aux1 = BigDecimal.ONE.divide(varBetaIdms);
			Integer exponente = varn-1;
			BigDecimal aux2 = Util.pow(varPrr, -exponente);
			varRentIni = aux1.multiply(aux2).multiply(varCapIni);
		}else{
			varRentIni = umic.getRentas().getRentini();
		}
		
		
		if (varBeta == null) {
			varBeta = 0;
			mapVariables.put(CLAVE_VAR_BETA, varBeta);
		}

		if (iteracion > 1) {
			varBeta += 1;
			mapVariables.put(CLAVE_VAR_BETA, varBeta);
		}

		if (bloqueCorriente.getFechaDevengo() != null) {
			
			varEdadj = FuncionesAuxiliares.nEdad(bloqueCorriente.getFechaDevengo(), umic.getAsegurados().getFnacAseg1(),
					varCriterEdad, umic.getRentas().getFecIni(), umic.getDatosGenerales().getEdifer()).intValueExact();
			//BigDecimal varVrta = FuncionesRentas.vrta(0, 0, varn, vartit1, varn*12, vartit1, varEdadj, lstValoresTabMort, 12, BigDecimal.ZERO, true, varBeta);
			varVrtaf = FuncionesRentas.vrtaf(0, 0, varn, vartit1, varn*12, vartit1, 12, umic.getRentas().getPrevrenta(), true, varBeta, varTcm, varTtm,  varBeta);

			//varVrta = new BigDecimal("50.6706");
			varRenta = varRentIni.multiply(varPrr.pow((varTcm + varBeta)/12));
			
			varRentaAct = varRenta.divide(new BigDecimal(12)).multiply(varVrtaf);
			
			//Auxiliares para facilitar el cálculo
			auxiliarExponente = (varTtm - varTcm - varBeta)/12;
			auxiliar = vartit1.divide(new BigDecimal(100));
			auxiliar1 = BigDecimal.ONE.add(auxiliar);
			auxiliar2 = auxiliar1.pow(-auxiliarExponente, ConstantsFunciones.MATH_CONTEXT);
			auxiliar3 = varCapIni.multiply(auxiliar2);
			
			csp230 = varCapInm.add(varRentaAct).add(auxiliar3);
		}

		if (ModuloCSP230.LOG.isTraceEnabled()) {
			ModuloCSP230.LOG.trace(
					"Fin función << moduloCSP230 >> de la clase ModuloCSP230, para la iteracion = {}, con resultado csp230 = {}",
					iteracion, csp230);
		}

		return csp230;
	}

}
