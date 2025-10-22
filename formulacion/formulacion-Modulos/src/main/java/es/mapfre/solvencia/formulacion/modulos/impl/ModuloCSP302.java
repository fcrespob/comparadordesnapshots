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
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.FuncionesPrimas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo de la cuantía nominal de la prestación a pagar en
 * caso de supervivencia o fallecimiento para la modalidad 302 (Flexibles).
 * 
 * @author Aleksandar Plamenov Nedyalkov
 *
 */

public class ModuloCSP302 implements Modulo {
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP302.class);

	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP302;
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TC0 = ConstantsFunciones.CTE_VAR_TC0.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCM = ConstantsFunciones.CTE_VAR_TCM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TC = ConstantsFunciones.CTE_VAR_TC.concat(CLAVE_MODULO);
	private static final String CLAVE_BETA = ConstantsModulos.CTE_VAR_BETA;
	private static final String CLAVE_VAR_BETA = CLAVE_BETA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TC_ANT = ConstantsModulos.CTE_VAR_TC_ANT.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PNATC_ANT = ConstantsModulos.CTE_VAR_PNATC_ANT.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PPT = ConstantsModulos.CTE_VAR_PPT.concat(CLAVE_MODULO);

	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_CSP302;
	}

	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la
	 * llamada a la función que realiza los calculos del modulo.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(Object... args) throws Solvencia2Excepcion {

		BigDecimal resultado = BigDecimal.ZERO;

		try {
			if (ModuloCSP302.LOG.isTraceEnabled()) {
				ModuloCSP302.LOG.trace("Inicio de execute en clase ModuloCSP302");
			}

			// Recuperamos los datos que le pasaremos a la función ModuloCSP302
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];

			// Invocamos a la función de calculo CSP238L
			resultado = moduloCSP302(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,
					codSubproceso);

		} catch (Solvencia2Excepcion e) {
			ModuloCSP302.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP302.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloCSP302.LOG.isTraceEnabled()) {
			ModuloCSP302.LOG.trace("Fin de execute en clase ModuloCSP302");
		}

		return resultado;
	}

	/**
	 * Cálculo de la cuantía nominal de la prestación a pagar en caso de
	 * supervivencia o fallecimiento para la modalidad 302 (Flexibles).
	 * 
	 * @param proyUmic
	 * @param bloqueCorriente
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param mapVariables
	 * @param codSubproceso
	 * @return csp302
	 */
	private BigDecimal moduloCSP302(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, Map<String, Object> mapVariables,
			String codSubproceso) {
		BigDecimal csp302 = BigDecimal.ZERO;
		Timestamp varFechaEfecto;
		Integer varTC0;
		Integer varTCm;
		BigDecimal varPriTarada;
		BigDecimal varPrimaIni;
		String varCformapago;
		BigDecimal varPPR;
		BigDecimal varPRP;
		BigDecimal varIcapAct;
		BigDecimal varPpc;
		// List<DetalleCorriente> varProyNominal;
		Integer varBeta;
		Integer varTc;
		BigDecimal varPendpa;
		BigDecimal varPPT;
		BigDecimal varPnaTc = BigDecimal.ZERO;
		Integer varTcAnt;
		BigDecimal varPnaTcAnt;
		BigDecimal varSumaF;
		BigDecimal aux1;
		BigDecimal aux2;

		if (ModuloCSP302.LOG.isTraceEnabled()) {
			ModuloCSP302.LOG.trace("Inicio función << moduloCSP302 >> para la iteracion = {}", iteracion);
		}

		// Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		varFechaEfecto = UtilModulos.getVarFechaEfecto(mapVariables, CLAVE_FEC_EFEC, umic);
		varTC0 = UtilModulos.getVarTC0(mapVariables, CLAVE_VAR_TC0, varFechaEfecto, fcalc);
		varTCm = UtilModulos.getVarTCm(mapVariables, CLAVE_VAR_TCM, iteracion, varFechaEfecto, fcalc);

		varPriTarada = umic.getPrimas().getIprimatarada();
		varPrimaIni = umic.getPrimas().getIprimanetaini();
		varCformapago = umic.getPrimas().getCformpago();
		varPPR = umic.getPrimas().getPpr();
		varPRP = umic.getPrimas().getPrevprima();
		varIcapAct = umic.getCapitales().getIcapact();
		varPpc = umic.getPrimas().getPpc();
		varBeta = UtilModulos.getVarBeta0(mapVariables, CLAVE_VAR_BETA);
		varTcAnt = UtilModulos.getVarTcAnt(mapVariables, CLAVE_VAR_TC_ANT);
		varPnaTcAnt = UtilModulos.getVarPnaTcAnt(mapVariables, CLAVE_VAR_PNATC_ANT);
		
		
		if (iteracion > 1 && !proyUmic.get(iteracion-1).getFechaHasta().equals(proyUmic.get(iteracion-2).getFechaHasta())) {
			varBeta += 1;
			mapVariables.put(CLAVE_VAR_BETA, varBeta);
		}
		if(codSubproceso.equals("PROY_VIDA") && iteracion == 1){
				varPendpa = FuncionesPrimas.pendpa(varCformapago, varTC0, varTC0, varPPR, varPriTarada, varPrimaIni);
				varPPT = varPriTarada.multiply(varPendpa);
				mapVariables.put(CLAVE_VAR_PPT, varPPT);
		}
		
		if (bloqueCorriente.getFechaDevengo() == null
				|| bloqueCorriente.getFechaDevengo().after(umic.getFechas().getFecefecfin())) {
			return csp302;
		}
		
		varTc = FuncionesAuxiliares.tc(varFechaEfecto, bloqueCorriente.getFechaDevengo());
		if(null == mapVariables.get(CLAVE_VAR_PPT)){
			varPendpa = FuncionesPrimas.pendpa(varCformapago, varTC0, varTc, varPPR, varPriTarada, varPrimaIni);
			varPPT = varPriTarada.multiply(varPendpa);
			mapVariables.put(CLAVE_VAR_PPT, varPPT);
		}else{
			varPPT = (BigDecimal) mapVariables.get(CLAVE_VAR_PPT);
		}
		
		if(varPnaTcAnt.equals(BigDecimal.ZERO)){
			varPnaTcAnt = varPriTarada.multiply((varPRP.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).add(BigDecimal.ONE));
			mapVariables.put(CLAVE_VAR_PNATC_ANT, varPnaTcAnt);
		}

		varSumaF = FuncionesPrimas.sumaf((varTCm / 12) + 1, varTCm / 12, ((varTCm + varBeta) / 12) + 1, varPRP);
				
		aux1 = varPpc.add(varPPR).add(varPPT);
		aux2 = varPnaTcAnt.multiply(varSumaF);
		csp302 = varIcapAct.add(aux1).add(aux2);
				
		if (ModuloCSP302.LOG.isTraceEnabled()) {
			ModuloCSP302.LOG.trace(
					"Fin función << moduloCSP302 >> de la clase ModuloCSP302, para la iteracion = {}, con resultado csp302 = {}",
					iteracion, csp302);
		}

		return csp302;
	}
}
