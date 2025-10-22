package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
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

public class ModuloCSP504 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP504.class);
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP504;
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TTM = ConstantsModulos.CTE_VAR_TTM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_BETA = ConstantsModulos.CTE_VAR_BETA.concat(CLAVE_MODULO);

	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_CSP504;
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
			if (ModuloCSP504.LOG.isTraceEnabled()) {
				ModuloCSP504.LOG.trace("Inicio de execute en clase ModuloCSP504");
			}
			
			//Recuperamos los datos que le pasaremos a la función ModuloGZC504
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo
			resultado = moduloGZC504(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP504.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP504.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP504.LOG.isTraceEnabled()) {
			ModuloCSP504.LOG.trace("Fin de execute en clase ModuloCSP504");
		}
		
		return resultado;
	}
	
	/**
	 * Modulo de cálculo que devuelve el saldo de la umic (Saldo actual modalidades basadas en movimientos)
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
	private BigDecimal moduloGZC504(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, Map<String, Object> mapVariables,
			String codSubproceso) {
		
		BigDecimal csp504 = BigDecimal.ZERO;
		Timestamp varFechaEfecto = null;
		Timestamp varFecEfecFin = null;
		Integer varTCm = 0, varTtm = 0;
		Integer varBeta = 0;
		
		if (ModuloCSP504.LOG.isTraceEnabled()) {
			ModuloCSP504.LOG.trace("Inicio función << moduloGZC504 >> de la clase ModuloGZC504, para la iteracion = {}", iteracion);
		}
		
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if ( null == bloqueCorriente.getFechaDevengo() )
			return csp504;
		
		if(null == umic.getFechas().getFecefecfin() || umic.getFechas().getFecefecfin().equals(new Timestamp(new GregorianCalendar(9999, 12, 31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis()))){
			varFecEfecFin = proyUmic.get(proyUmic.size()-1).getFechaHasta();
		}else{
			varFecEfecFin = umic.getFechas().getFecefecfin();
		}
		
		if(mapVariables.get(CLAVE_VAR_BETA) == null) {
			varBeta = 0;
			mapVariables.put(CLAVE_VAR_BETA, varBeta);
		}else {
			varBeta = (Integer) mapVariables.get(CLAVE_VAR_BETA);
		}
		 
		varFechaEfecto = UtilModulos.getVarFecEfectoCSP(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(),
				umic.getFechas());
		varTCm = UtilModulos.getVarTCm(mapVariables, CLAVE_VAR_TCM, iteracion, varFechaEfecto, fcalc);

		varTtm = UtilModulos.getVarTCmIni(mapVariables, CLAVE_VAR_TTM, iteracion, varFechaEfecto, varFecEfecFin);
		
		BigDecimal aux = new BigDecimal(varTtm).subtract(new BigDecimal(varTCm));
		BigDecimal aux2 = new BigDecimal(varBeta).divide(aux,ConstantsFunciones.MATH_CONTEXT);
		
		csp504 = umic.getCapitales().getIsaldo().multiply(BigDecimal.ONE.subtract(aux2));
				
		varBeta++;
		mapVariables.put(CLAVE_VAR_BETA, varBeta);
		
		if (ModuloCSP504.LOG.isTraceEnabled()) {
			ModuloCSP504.LOG.trace("Fin función << moduloCSP504 >> de la clase ModuloCSP504, para la iteracion = {} con resultado csp504 = {}", iteracion, csp504);
		}
		
		if(codSubproceso.equals(ConstantsModulos.CTE_PROY_FALL) && csp504.compareTo(BigDecimal.ZERO) == -1){
			csp504 = BigDecimal.ZERO;
		}
		
		return csp504;
	}
	
}
