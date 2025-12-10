package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.conversionesBel.ValoresAnulacion;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.scr.ValoresEstres;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada de determinar la probabilidad de que una póliza de antigüedad tc meses en la fecha de cálculo, 
 * fcal, alcance en situación no anulada la fecha j en la que se devenga la prestación C(i,j). 
 * La expresión matemática para su determinación es la siguiente:
 * 			Atc(fcal,j) = (latc + nannos(fcal, j)) / latc
 * 
 * @author agonzalezgar
 *
 */
public class ModuloATC implements Modulo {
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloATC.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_ATC;
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	private static final String CLAVE_VAR_ANULACION = ConstantsModulos.CTE_VAR_ANULACION.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ANULACION_ANM = ConstantsModulos.CTE_VAR_ANULACION_ANM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_POLIZAS_ANO1 = ConstantsModulos.CTE_VAR_VAL_POLIZA_ANO1.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ESTRESES = ConstantsModulos.CTE_VAR_ESTRESES.concat(CLAVE_MODULO);
	
	// Fin de las variables estáticas usadas para agilizar operaciones.


	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/* (non-Javadoc)
	 * @see es.mapfre.solvencia.formulacion.modulos.Modulo#execute(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		//Fin variables locales
		
		try {
			
			if (ModuloATC.LOG.isTraceEnabled()) {
				ModuloATC.LOG.trace("Inicio de execute en clase ModuloATC");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloATC
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función moduloATC
			resultado = moduloATC(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloATC.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloATC.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloATC.LOG.isTraceEnabled()) {
			ModuloATC.LOG.trace("Fin de execute en clase ModuloATC");
		}
		
		return resultado;
	}
	/**
	 *El módulo ATC(fcal,j) determina la probabilidad de que una póliza de antigüedad tc meses en la fecha de cálculo, 
	 *fcal, alcance en situación no anulada la fecha j en la que se devenga la prestación C(i,j). 
	 *
	 * @param proyUmic
	 * 			Estructura detalleCorrientes de la umic
	 * @param bloqueCorriente
	 * 			Bloque de Trabajo de la corriente
	 * @param iteracion
	 * 			Indica el periodo de proyección J que se está calculando de entre todos los periodos de proyección de la umic (proyUmic)
	 * @param fcalc
	 * 			Fecha de calculo
	 * @param umic
	 * 			Contiene los datos de la Umic que se está procesando
	 * @param btcUmic
	 * 			Contiene el detalle de la base técnica de cálculo para la umic
	 * @param mapVariables 
	 * 			mapa con las variables de memoria necesarias
	 */
	private BigDecimal moduloATC(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal atcJ = BigDecimal.ZERO;
		String varCriFec = ConstantsFunciones.CTE_CADENA_VACIA;
		List<ValoresAnulacion> lstValorAnul, lstValorAnulANM = null;
		Timestamp varFechaEfecto;
		//Fin variables locales
		
		if (ModuloATC.LOG.isTraceEnabled()) {
			ModuloATC.LOG.trace("Inicio función << moduloATC >> de la clase ModuloATC, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		BigDecimal varNanos0 = BigDecimal.ZERO;
		BigDecimal varNAnosJDecimal = BigDecimal.ZERO;
		BigDecimal varNanos0Decimal = BigDecimal.ZERO;
		BigDecimal varLaNanos00 = BigDecimal.ZERO;
		BigDecimal varLaNanos01 = BigDecimal.ZERO;
		BigDecimal varLaJ0 = BigDecimal.ZERO;
		BigDecimal varNAnosJ = BigDecimal.ZERO;
		BigDecimal varLaJ00 = BigDecimal.ZERO;
		BigDecimal varLaJ01 = BigDecimal.ZERO;
		BigDecimal varLaJ = BigDecimal.ZERO;
		BigDecimal varPolizasAno1, varPolDespuesAno1 = BigDecimal.ZERO;
		
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		// Cálculo de las variables de apoyo.
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		// Validación de las variables de apoyo.
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
		}
		
		varNanos0 = FuncionesAuxiliares.nAnnos(varFechaEfecto, fcalc, varCriFec);
		
		lstValorAnul = UtilModulos.getVarValoresAnulacion(mapVariables, CLAVE_VAR_ANULACION, btcUmic);
		
		varLaNanos00 = lstValorAnul.get(varNanos0.intValue()).getPolizaVigentes();
		varLaNanos01 = lstValorAnul.get(varNanos0.intValue()+1).getPolizaVigentes();
		varNanos0Decimal = varNanos0.subtract(new BigDecimal(varNanos0.intValue()));
		varLaJ0 = varLaNanos00.add(varNanos0Decimal.multiply(varLaNanos01.subtract(varLaNanos00)));
		
		if ( null == bloqueCorriente.getFechaDevengo()) {
			return atcJ;
		}
		
		UtilFechas.Fecha fecDevengou = UtilFechas.getFecha(bloqueCorriente.getFechaDevengo()); 
		varNAnosJ = FuncionesAuxiliares.nAnnos(fecDevengou.toTimestamp(), varFechaEfecto, varCriFec);
		
		varLaJ00 = lstValorAnul.get(varNAnosJ.intValue()).getPolizaVigentes();
		varLaJ01 = lstValorAnul.get(varNAnosJ.intValue()+1).getPolizaVigentes();
		varNAnosJDecimal = varNAnosJ.subtract(new BigDecimal(varNAnosJ.intValue()));
		varLaJ = varLaJ00.add(varNAnosJDecimal.multiply(varLaJ01.subtract(varLaJ00)));
		
		atcJ = varLaJ.divide(varLaJ0 , ConstantsFunciones.MATH_CONTEXT);
		
		if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM)) {
			BigDecimal varSCR = BigDecimal.ZERO;
			List<ValoresEstres> varSCRS = UtilModulos.getValoresEstres(mapVariables, CLAVE_VAR_ESTRESES, umic.getDatosGenerales().getFecCierre(), btcUmic.getBaseTec());
			if (umic.getDatosGenerales().getCnegocio().equals("C")) {
				if (varSCRS.get(0).getVariable().equals("ANPMC")) {
					varSCR = varSCRS.get(0).getValor().divide(new BigDecimal(100), ConstantsFunciones.MATH_CONTEXT);
				} else {
					varSCR = varSCRS.get(1).getValor().divide(new BigDecimal(100), ConstantsFunciones.MATH_CONTEXT);
				}
			} else {
				if (varSCRS.get(0).getVariable().equals("ANPMI")) {
					varSCR = varSCRS.get(0).getValor().divide(new BigDecimal(100), ConstantsFunciones.MATH_CONTEXT);
				} else {
					varSCR = varSCRS.get(1).getValor().divide(new BigDecimal(100), ConstantsFunciones.MATH_CONTEXT);
				}
			}
			
			if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
				atcJ = BigDecimal.ONE.subtract(varSCR);
			} else {
				atcJ = atcJ.multiply(BigDecimal.ONE.subtract(varSCR));
			}
		}
		
		if (ModuloATC.LOG.isTraceEnabled()) {
			ModuloATC.LOG.trace("Fin función << moduloATC >> de la clase ModuloATC, para la iteracion = {}, con resultado atcJ = {}", iteracion, atcJ);
		}
			
		return atcJ;
	}

}
