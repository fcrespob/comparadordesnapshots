package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * 
 * @author Aleksandar Plamenov Nedyalkov
 *
 */

public class ModuloCSP038 implements Modulo {
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP038.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP038;
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_PORINI = ConstantsModulos.CTE_VA_POR_INI;
	private static final String CLAVE_PORDEC = ConstantsModulos.CTE_VA_POR_DEC;
	private static final String CLAVE_PERDEC = ConstantsModulos.CTE_VA_PER_DEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PORINI = CLAVE_PORINI.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PORDEC = CLAVE_PORDEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PERDEC = CLAVE_PERDEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_BETA = ConstantsModulos.CTE_VAR_BETA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_GAMMA = ConstantsModulos.CTE_VAR_GAMMA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PNA = ConstantsModulos.CTE_VAR_PNA.concat(CLAVE_MODULO);

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
			if (ModuloCSP038.LOG.isTraceEnabled()) {
				ModuloCSP038.LOG.trace("Inicio de execute en clase ModuloCSP038");
			}

			// Recuperamos los datos que le pasaremos a la función
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];

			// Invocamos a la función moduloCSP038
			resultado = moduloCSP038(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,
					codSubproceso);

		} catch (Solvencia2Excepcion e) {
			ModuloCSP038.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP038.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloCSP038.LOG.isTraceEnabled()) {
			ModuloCSP038.LOG.trace("Fin de execute en clase ModuloCSP038");
		}

		return resultado;
	}
	
	/**
	 * Modulo de cálculo de la cuantía por FALLECIMIENTO de la garantía principal de esta modalidad
	 * @param proyUmic
	 * @param bloqueCorriente
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param mapVariables
	 * @param codSubproceso
	 * @return csp038
	 */
	private BigDecimal moduloCSP038(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, Map<String, Object> mapVariables,
			String codSubproceso) {

		BigDecimal csp038 = BigDecimal.ZERO;

		BigDecimal varPNA0 = BigDecimal.ZERO;
		BigDecimal varPorIni = BigDecimal.ZERO;
		BigDecimal varPorDec = BigDecimal.ZERO;
		BigDecimal varPerDec = BigDecimal.ZERO;
		BigDecimal varGamma0 = BigDecimal.ZERO;
		BigDecimal varCien = new BigDecimal(100);
		Timestamp varFechaEfecto = null;
		Integer varTCm = 0;
		Integer varBeta = 0;
		BigDecimal varPorGammaTcm;
		BigDecimal varPorGamma1;
		BigDecimal gammaUmic;

		String varCriterFec = ConstantsFunciones.CTE_CADENA_VACIA;

		if (ModuloCSP038.LOG.isTraceEnabled()) {
			ModuloCSP038.LOG.trace("Inicio función << ModuloCSP038 >> de la clase ModuloCSP038, para la iteracion = {}",
					iteracion);
		}

		// Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(),
				umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);

		varPorIni = UtilModulos.getVarPorIni(mapVariables, CLAVE_VAR_PORINI, umic.getDatosGenerales().getCcartera(),
				umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_PORINI);

		varPorDec = UtilModulos.getVarPorIni(mapVariables, CLAVE_VAR_PORDEC, umic.getDatosGenerales().getCcartera(),
				umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_PORDEC);
		
		varPerDec = UtilModulos.getVarPerDec(mapVariables, CLAVE_VAR_PERDEC, umic.getDatosGenerales().getCcartera(),
				umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_PERDEC);

		ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFec);
		ValidacionesComunesModulos.validarVariableDeApoyoVarPorIni(varPorIni);
		ValidacionesComunesModulos.validarVariableDeApoyoVarPorDec(varPorDec);
		ValidacionesComunesModulos.validarVariableDeApoyoVarPerDec(varPerDec);
		
		varPorIni = varPorIni.divide(varCien);
		varPorDec = varPorDec.divide(varCien);

		varFechaEfecto = UtilModulos.getVarFecEfectoCSP(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(),
				umic.getFechas());
		varPNA0 = UtilModulos.getVarPN0(mapVariables, CLAVE_VAR_PNA, umic.getPrimas().getIprimanetaini());
		if(umic.getCapitales().getPorgamma().equals(ConstantsModulos.CTE_OPER_195)){
			gammaUmic = BigDecimal.ZERO;
		}else{
			gammaUmic = umic.getCapitales().getPorgamma();
		}
		varGamma0 = UtilModulos.getVarPN0(mapVariables, CLAVE_VAR_GAMMA,gammaUmic).divide(varCien);
		varTCm = UtilModulos.getVarTCm(mapVariables, CLAVE_VAR_TCM, iteracion, varFechaEfecto, fcalc);
				
		if(mapVariables.get(CLAVE_VAR_BETA) == null) {
			varBeta = 0;
			mapVariables.put(CLAVE_VAR_BETA, varBeta);
		}else {
			varBeta = (Integer) mapVariables.get(CLAVE_VAR_BETA);
		}

		if (bloqueCorriente.getFechaDevengo() != null) {
			
			//Auxiliar para el cálculo de los números enteros y conversión a BigDecimal
			BigDecimal aux = BigDecimal.valueOf(((varTCm + varBeta) / varPerDec.intValue()));
			
			aux = aux.add(BigDecimal.ONE);
			//Auxiliar para facilitar el cálculo con BigDecimal
			BigDecimal aux2 = varPorDec.multiply(aux);

			varPorGammaTcm = varPorIni.subtract(aux2);

			if (varPorGammaTcm.compareTo(varGamma0) == 1) {
				varPorGamma1 = varPorGammaTcm;
			} else {
				varPorGamma1 = varGamma0;
			}

			csp038 = varPNA0.multiply(varPorGamma1);
			
			varBeta++;
			mapVariables.put(CLAVE_VAR_BETA, varBeta);
		}

		if (ModuloCSP038.LOG.isTraceEnabled()) {
			ModuloCSP038.LOG.trace("Fin función << moduloCSP038 >> de la clase ModuloCSP038");
		}
		return csp038;
	}

}
