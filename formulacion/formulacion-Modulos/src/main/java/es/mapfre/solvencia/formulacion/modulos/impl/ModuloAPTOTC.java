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
 * Clase encargada de determina la probabilidad de que una póliza de antigüedad tc meses  en la fecha de cálculo, fcal, se anule entre las fechas jant y j, 
 * fechas en las que se devengan las prestaciones C(i,jant) y C(i,j) respectivamente
 * La expresión matemática para su determinación es la siguiente:
 * 				Aptotc(fcal, j) = (lazc + nannos(fcal, jant) - latc + nannos(fcal, j)) / latc
 * @author agonzalezgar
 *
 */
public class ModuloAPTOTC implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloAPTOTC.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_APTOTC;
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);	
	
	private static final String CLAVE_VAR_ANULACION = ConstantsModulos.CTE_VAR_ANULACION.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	private static final String CLAVE_VAR_POLIZAS_ANO1 = ConstantsModulos.CTE_VAR_VAL_POLIZA_ANO1.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ANULACION_ANM = ConstantsModulos.CTE_VAR_ANULACION_ANM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ESTRESES = ConstantsModulos.CTE_VAR_ESTRESES.concat(CLAVE_MODULO);
	// Fin de las variables estáticas usadas para agilizar operaciones.
	
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
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
			if (ModuloAPTOTC.LOG.isTraceEnabled()) {
				ModuloAPTOTC.LOG.trace("Inicio de execute en clase ModuloAPTOTC");
			}
			
			// Recuperamos los datos que le pasaremos a la función moduloAPTOTC.
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de datos.
			
			//Invocamos a la función moduloAPTOTC
			resultado = moduloAPTOTC(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloAPTOTC.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloAPTOTC.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloAPTOTC.LOG.isTraceEnabled()) {
			ModuloAPTOTC.LOG.trace("Fin de execute en clase ModuloAPTOTC");
		}
		
		return resultado;
	}
	/**
	 * Modulo que determina la probabilidad de que una póliza de antigüedad tc meses  en la fecha de cálculo, fcal, se anule entre las fechas jant y j, 
	 * fechas en las que se devengan las prestaciones C(i,jant) y C(i,j) respectivamente
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
	private BigDecimal moduloAPTOTC(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, String codSubproceso) {
		
		//Variables locales
		BigDecimal aptotc = BigDecimal.ZERO;
		String varCriFec = ConstantsFunciones.CTE_CADENA_VACIA;
		List<ValoresAnulacion> lstValorAnul, lstValorAnulANM = null;
		Timestamp varFechaEfecto;
		Integer it = (Integer) mapVariables.get("IT_APTOTC");
		if (null == it) {
			it = 1;
		}
		//Fin variables locales
		
		if (ModuloAPTOTC.LOG.isTraceEnabled()) {
			ModuloAPTOTC.LOG.trace("Inicio función << moduloAPTOTC >> de la clase ModuloAPTOTC, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		if ( null == bloqueCorriente.getFechaDevengo()) {
			return aptotc;
		}
		
		/**
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo,
		 * así como las variables internas que tampoco varían por periodo,
		 * y que se dejarán accesibles para su uso en el subproceso por los siguientes periodos a calcular:
		 * 		
		 * 		- varTipoInteres -> se buscará en los datos de la base técnica inicial de la umic (btcUmic.Itcalc1)
		 * 		el porcentaje de interés correspondiente al tramo de interés que cumpla con las siguientes condiciones:
		 * 			- fecIniTramoX <= proyUmic(j).feccierre
		 * 			- fecFinTramoX >= proyUmic(j).feccierre
		 * 
		 * 		-	varTC = TC(varfechaEfecto, fcalc)
		 * 		-	varValoresAnul= obtenerConfiguracion.recuperarTasasAnul con los parÃ¡metros: 
		 * 		-	codTablaAnul = btcUmic.tablaTanul
		 * 		-	fecAnul = btcUmic. fecCurvaAn
		 * 		-	varLaTc = varValoresAnul(varTC).polizasVig -> Dato de pÃ³lizas vigentes de varValoresAnul para naniosDesde = varTC
		 * 
		 * Variables de Apoyo:
		 * 		- VarCriterFec -> obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
		 * 		Si la variable de apoyo retornada es nulo se devuelve
		 * 		error funcional 005 - No se ha encontrado la Variable de Apoyo ID-TEMPORAL, finalizando el proceso para la UMIC.
		 */
		
		BigDecimal varNanos0 = BigDecimal.ZERO;
		BigDecimal varNAnosJDecimal = BigDecimal.ZERO;
		BigDecimal varNanos0Decimal = BigDecimal.ZERO;
		BigDecimal varNanosJantDecimal = BigDecimal.ZERO;
		BigDecimal varLaNanos00 = BigDecimal.ZERO;
		BigDecimal varLaNanos01 = BigDecimal.ZERO;
		BigDecimal varLaJ0 = BigDecimal.ZERO;
		BigDecimal varNAnosJ = BigDecimal.ZERO;
		BigDecimal varLaJ00 = BigDecimal.ZERO;
		BigDecimal varLaJ01 = BigDecimal.ZERO;
		BigDecimal varLaJ = BigDecimal.ZERO;
		
		BigDecimal varNAnosJant = BigDecimal.ZERO;
		BigDecimal varLaJant00 = BigDecimal.ZERO;
		BigDecimal varLaJant01 = BigDecimal.ZERO;
		BigDecimal varLaJant = BigDecimal.ZERO;
		BigDecimal varPolizasAno1 = BigDecimal.ZERO; 
		BigDecimal varPolDespuesAno1 = BigDecimal.ZERO;
				
		// Cálculo y validación de las variables de apoyo.
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
		}
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		varNanos0 = FuncionesAuxiliares.nAnnos(varFechaEfecto, fcalc, varCriFec);
		
		lstValorAnul = UtilModulos.getVarValoresAnulacion(mapVariables, CLAVE_VAR_ANULACION, btcUmic);
		
		varLaNanos00 = lstValorAnul.get(varNanos0.intValue()).getPolizaVigentes();
		varLaNanos01 = lstValorAnul.get(varNanos0.intValue()+1).getPolizaVigentes();
		varNanos0Decimal = varNanos0.subtract(new BigDecimal(varNanos0.intValue()));
		varLaJ0 = varLaNanos00.add(varNanos0Decimal.multiply(varLaNanos01.subtract(varLaNanos00)));
		
		Timestamp fecDevengo = bloqueCorriente.getFechaDevengo(); 
		varNAnosJ = FuncionesAuxiliares.nAnnos(fecDevengo, varFechaEfecto, varCriFec);
		
		varLaJ00 = lstValorAnul.get(varNAnosJ.intValue()).getPolizaVigentes();
		varLaJ01 = lstValorAnul.get(varNAnosJ.intValue()+1).getPolizaVigentes();
		varNAnosJDecimal = varNAnosJ.subtract(new BigDecimal(varNAnosJ.intValue()));
		varLaJ = varLaJ00.add(varNAnosJDecimal.multiply(varLaJ01.subtract(varLaJ00)));
		
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			aptotc = (varLaJ0.subtract(varLaJ)).divide(varLaJ0, ConstantsFunciones.MATH_CONTEXT);
		} else {
			Timestamp fecDevAnterior = proyUmic.get(iteracion-2).getBloqueBySubproceso(codSubproceso).getFechaDevengo();
			
			Timestamp auxFecha = (Timestamp) mapVariables.get("FEC_AUX_APTOTC");
			
			if (auxFecha == null ) {
				if (bloqueCorriente.getFechaDevengo() != null) {
					auxFecha = bloqueCorriente.getFechaDevengo();
				} else {
					auxFecha = UtilFechas.getUltimoDiaDelMes(fcalc);
				}
				mapVariables.put("FEC_AUX_APTOTC", auxFecha);
			}
			if (fecDevAnterior == null) {
					//return BigDecimal.ZERO;
					Timestamp auxFecDev = (Timestamp) mapVariables.get("FEC_DEVENGO_APTOTC");
					if (auxFecDev == null ) {
						auxFecDev = UtilFechas.getUltimoDiaDelMes(fcalc);
					} else {
						auxFecDev = UtilFechas.plusMeses(auxFecDev, 1);
						if (UtilFechas.getMes(auxFecDev) == 2) {
							auxFecDev = UtilFechas.getUltimoDiaDelMes(auxFecDev);
							
						} else {
							if (UtilFechas.getDia(auxFecDev) != UtilFechas.getDia(auxFecha)) {
								auxFecDev = UtilFechas.incrDias(auxFecDev, UtilFechas.getDia(auxFecha) - UtilFechas.getDia(auxFecDev));
							}
						}
					}
					mapVariables.put("FEC_DEVENGO_APTOTC", auxFecDev);
					fecDevAnterior = auxFecDev;
				} else {
					mapVariables.put("FEC_DEVENGO_APTOTC", bloqueCorriente.getFechaDevengo());
				}
			 
			varNAnosJant = FuncionesAuxiliares.nAnnos(fecDevAnterior, varFechaEfecto, varCriFec);
			
			varLaJant00 = lstValorAnul.get(varNAnosJant.intValue()).getPolizaVigentes();
			varLaJant01 = lstValorAnul.get(varNAnosJant.intValue()+1).getPolizaVigentes();
			varNanosJantDecimal = varNAnosJant.subtract(new BigDecimal(varNAnosJant.intValue()));
			varLaJant = varLaJant00.add(varNanosJantDecimal.multiply(varLaJant01.subtract(varLaJant00)));
			
			aptotc = (varLaJant.subtract(varLaJ)).divide(varLaJ0, ConstantsFunciones.MATH_CONTEXT);
		}
		
		if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM)) {
			BigDecimal varSCR = BigDecimal.ZERO;;
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
			
			if (ConstantsModulos.CTE_FIRST_ITER.equals(it)) {
				aptotc = varSCR;
				mapVariables.put("IT_APTOTC", it + 1);
			} else {
				aptotc = aptotc.multiply(BigDecimal.ONE.subtract(varSCR));
			}
		}

		if (ModuloAPTOTC.LOG.isTraceEnabled()) {
			ModuloAPTOTC.LOG.trace("Fin función << moduloAPTOTC >> de la clase ModuloAPTOTC, para la iteracion = {}, con resultado aptotc = {}", iteracion, aptotc);
		}
		
		return aptotc;
	}
}
