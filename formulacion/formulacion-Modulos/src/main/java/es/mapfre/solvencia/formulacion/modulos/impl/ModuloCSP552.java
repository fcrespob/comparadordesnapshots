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
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloCSP552 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP552.class);
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP504;
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_DIF = ConstantsModulos.CTE_FEC_DIFERIMIENTO.concat(CLAVE_MODULO);

	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_CSP552;
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
			if (ModuloCSP552.LOG.isTraceEnabled()) {
				ModuloCSP552.LOG.trace("Inicio de execute en clase ModuloCSP552");
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
			resultado = moduloCSP552(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP552.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP552.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP552.LOG.isTraceEnabled()) {
			ModuloCSP552.LOG.trace("Fin de execute en clase ModuloCSP552");
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
	private BigDecimal moduloCSP552(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, Map<String, Object> mapVariables,
			String codSubproceso) {
		
		BigDecimal csp552 = BigDecimal.ZERO;
		Timestamp varFechaEfecto = null;
		BigDecimal varDiferimiento = BigDecimal.ZERO;
		BigDecimal varArect = BigDecimal.ZERO;
		BigDecimal varNumMeses = BigDecimal.ZERO;
		BigDecimal varFactorAnual = BigDecimal.ZERO;
		BigDecimal varADec = BigDecimal.ZERO;
		BigDecimal varAux1 = BigDecimal.ZERO;
		BigDecimal varFactor = BigDecimal.ZERO;
		
		BigDecimal varPu;
		
		if (ModuloCSP552.LOG.isTraceEnabled()) {
			ModuloCSP552.LOG.trace("Inicio función << moduloGZC552 >> de la clase ModuloGZC552, para la iteracion = {}", iteracion);
		}
		
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if (null == bloqueCorriente.getFechaDevengo()) {
			return csp552;
		}
		
		if (umic.getDatosGenerales().getKprestacion().equals("CTA202") 
				&& !umic.getDatosGenerales().getNorden().equals(211) 
				&& codSubproceso.equals(ConstantsModulos.CTE_PROY_FALL)) {
			return csp552;
		}
		if (umic.getDatosGenerales().getKprestacion().equals("CTA302") 
				&& !umic.getDatosGenerales().getNorden().equals(311)
				&& codSubproceso.equals(ConstantsModulos.CTE_PROY_FALL)) {
			return csp552;
		}
		
		if(umic.getDatosGenerales().getKprestacion().equals("CTA202") 
				&& umic.getDatosGenerales().getNorden().equals(110)
				&& (codSubproceso.equals(ConstantsModulos.CTE_PROY_VIDA) || 
				codSubproceso.equals(ConstantsModulos.CTE_PROY_FALL))){
			
			BigDecimal impPago = BigDecimal.ZERO;
			if(null !=proyUmic.get(iteracion - 1).getImpPago()){
				impPago = proyUmic.get(iteracion - 1).getImpPago();
			}
			return impPago;
		}
		
		varFechaEfecto = UtilModulos.getVarFecEfectoCSP(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(),
				umic.getFechas());
		
		//¿¿Saldo??
		//varPu = umic.getPrimas().getIprimanetini();
		varPu = umic.getRentas().getRentini();
		varDiferimiento = new BigDecimal (umic.getRentas().getNadifer());
		//varDiferimiento = new BigDecimal(111);
		varArect = new BigDecimal (umic.getDatosNiif17().getArect()); 
		varNumMeses = varDiferimiento.subtract(varArect);
		
		if (iteracion > varDiferimiento.intValue()) {
			varFactor = BigDecimal.ZERO;
		}
		else {
			if (varNumMeses.compareTo(BigDecimal.ZERO) > 0) {
				if (iteracion <= varArect.intValue()) {
					varFactor = BigDecimal.ONE;
					mapVariables.put(CLAVE_FEC_DIF, UtilFechas.getMes(proyUmic.get(iteracion - 1).getFechaDesde()));
				} else {
					
					if (varNumMeses.compareTo(BigDecimal.ZERO) < 0) {
						varFactorAnual = BigDecimal.ONE;
					} else {
						varFactorAnual = BigDecimal.ONE.divide((varNumMeses.divide(new BigDecimal(12), ConstantsFunciones.MATH_CONTEXT)).add(BigDecimal.ONE), ConstantsFunciones.MATH_CONTEXT);
					}
					
					if (UtilFechas.getMes(proyUmic.get(iteracion - 1).getFechaDesde()) > (int) mapVariables.get(CLAVE_FEC_DIF))
						varAux1 = new BigDecimal(UtilFechas.getAnio(proyUmic.get(iteracion - 1).getFechaDesde()) - UtilFechas.getAnio(varFechaEfecto) + 1);
					else {
						varAux1 = new BigDecimal(UtilFechas.getAnio(proyUmic.get(iteracion - 1).getFechaDesde()) - UtilFechas.getAnio(varFechaEfecto));
					}
					
					varADec = varAux1.subtract(varArect.divide(BigDecimal.valueOf(12), ConstantsFunciones.MATH_CONTEXT));
				
					varFactor = BigDecimal.ONE.subtract(varFactorAnual.multiply(varADec));
				}
			}else {
				varFactor = BigDecimal.ONE;
			}
		}
		
		csp552 = varFactor.multiply(varPu);
		
		if (ModuloCSP552.LOG.isTraceEnabled()) {
			ModuloCSP552.LOG.trace("Fin función << moduloCSP552 >> de la clase ModuloCSP552, para la iteracion = {} con resultado csp552 = {}", iteracion, csp552);
		}
		
		return csp552;
	}
	
}
