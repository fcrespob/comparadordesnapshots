/**
 * MU-2018-016517-Código IM00258226: CORRECCION MODULO CSP037
 * Se incluye una funcion para formatear un string a un numero de carateres.
 */
/**MODIFICACION: TAR00433819
//FECHA: 24/09/2018
//DESCRIP: Se incluye el tratamiento para los nuevos estados de prorrogas U,F, que se tienen que comportar como VI.
 */

/** MODIFICACION:TAR00400971-NECESIDADES NUEVO SISTEMA DE PROCESOS TÉCNICOS 
  * FECHA: 14/12/2018
  * AUTOR: INDRA
*/
/**MODIFICACION: MU-2019-029631
//FECHA: 30/05/2019
//DESCRIP: Se incluye el tratamiento para los nuevos estados de prorrogas U,F, que se tienen que comportar como VI.
 */
/**
 * MU-2019-038078:NO APLICACION CORRECTA LIMITES FALLECIMIENTO
 *  Se corrige el acceso a la tabla 646
 */

package es.mapfre.solvencia.formulacion.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.conversionesBel.GastosReales;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresAnulacion;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresAnulacionMensuales;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresCurvaTipo;
import es.mapfre.solvencia.dominio.formulacion.CriterioFechas;
import es.mapfre.solvencia.dominio.formulacion.PlanPagos;
import es.mapfre.solvencia.dominio.maestro.Asegurados;
import es.mapfre.solvencia.dominio.maestro.CuadroUmic;
import es.mapfre.solvencia.dominio.maestro.CuadrosAmortizacion;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Fechas;
import es.mapfre.solvencia.dominio.maestro.OtrosDatos;
import es.mapfre.solvencia.dominio.maestro.PagosPlanificados;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.maestro.ValoresLiquidativos;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.IPCGeneralFuturo;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.LimitesCapital;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.Tab35050;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.Tab923;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ValoresConstantesRescate;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TablaConversion;
import es.mapfre.solvencia.dominio.salidaCalculo.TerminosPMCUmic;
import es.mapfre.solvencia.dominio.scr.ValoresEstres;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.modulos.impl.ValidacionesComunesModulos;
import es.mapfre.solvencia.formulacion.util.UtilFechas.Fecha;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;

/**
 * Clase de utilidades genericas
 * 
 */
public final class UtilModulos {
	
	private static final Logger LOG = LoggerFactory.getLogger(UtilModulos.class);
		
	private UtilModulos() { }
	
	@SuppressWarnings("unchecked")
	public static java.math.BigDecimal getCteRescate(final Map<String, Object> mapVariables, String codk1, Integer durk1){
		final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		boolean encontrada = false;
		BigDecimal vark1 = null;
		List<ValoresConstantesRescate> constantesRescate;
		if(codk1 != null){
			final String claveCte = ConstantsModulos.CTE_RESCATE.concat(codk1);
			if(codk1.startsWith(ConstantsModulos.CTE_CODK_KC) || codk1.startsWith(ConstantsModulos.CTE_CODK_KF)){
				if ((vark1 = (BigDecimal) mapVariables.get(claveCte)) == null){
					vark1 = servicio.recuperarCteRescate(codk1, durk1);
					mapVariables.put(claveCte, vark1);
				}
			}else{
				final String claveCtes = ConstantsModulos.CTES_RESCATE.concat(codk1);
				if((vark1 = (BigDecimal) mapVariables.get(claveCte))== null){
					constantesRescate = servicio.recuperarCtesRescate(codk1, durk1);
					mapVariables.put(claveCtes, constantesRescate);
					vark1 = constantesRescate.get(0).getPorckonst();
					mapVariables.put(claveCte, vark1);
				}else{
					constantesRescate = (List<ValoresConstantesRescate>) mapVariables.get(claveCtes);
					while(!encontrada && constantesRescate.size() >0){
						if (constantesRescate.get(0).getKduracion() < durk1){
							constantesRescate.remove(0);
						}else{
							vark1 = constantesRescate.get(0).getPorckonst();
							mapVariables.put(claveCte, vark1);
							
							encontrada = true;
						}
					}
				}
				
			}
		}
		return vark1;
	}
	
	/**
	 * Devuelve valor varCriFec. Si no existe la calcula, la almacena en el Hashmap y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codigoConstante
	 * @return
	 */
	public static String getVarCriFec(final Map<String, Object> mapVariables, final String claveVariable, final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbasetec, final String codigoConstante) {
		return getsetStringRecuperarDefinicionAuxiliar(mapVariables, claveVariable, ccartera, kmodalidad, kgarantia, kbasetec, codigoConstante);
	}
	
	/** 
	 * Devuelve valor NRTA. Si no existe la calcula, la almacena en el Hashmap y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codigoConstante
	 */
	public static Integer getVarNRTA(final Map<String, Object> mapVariables, final String claveVariable, final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbasetec, final String codigoConstante){
		return getsetIntegerRecuperarDefinicionAuxiliar(mapVariables, claveVariable, ccartera, kmodalidad, kgarantia, kbasetec, codigoConstante);
	}
	
	
	/**
	 * Devuelve valor LimPeriodos. Si no existe la calcula, la almacena en el Hashmap y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codigoConstante
	 * @return
	 */
	public static BigDecimal getVarLimPeriodos(final Map<String, Object> mapVariables, final String claveVariable, final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbasetec, final String codigoConstante) {
		return getsetBigDecimalRecuperarDefinicionAuxiliar(mapVariables, claveVariable, ccartera, kmodalidad, kgarantia, kbasetec, codigoConstante);
	}
	/** 
	 * Devuelve valor BETARENTAS. Si no existe la calcula, la almacena en el Hashmap y la devulve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codigoConstante 
	 */
	public static BigDecimal getVarBETARENTAS(final Map<String, Object> mapVariables, final String claveVariable, final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbasetec, final String codigoConstante) {
		return getsetBigDecimalRecuperarDefinicionAuxiliar(mapVariables, claveVariable, ccartera, kmodalidad, kgarantia, kbasetec, codigoConstante);
	}
	
	/** 
	 * Devuelve valor BetaIDMS. Si no existe la calcula, la almacena en el Hashmap y la devulve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codigoConstante 
	 */
	public static BigDecimal getVarBetaIDMS(final Map<String, Object> mapVariables, final String claveVariable, final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbasetec, final String codigoConstante) {
		return getsetBigDecimalRecuperarDefinicionAuxiliar(mapVariables, claveVariable, ccartera, kmodalidad, kgarantia, kbasetec, codigoConstante);
	}
	
	/**
	 * Devuelve el valor varGic/100 o varGipc/100. Si no existe la calcula, la almacena en el Hashmap y la devuelve.
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param varGic
	 * @return
	 */	
	public static BigDecimal getVarGicDiv100(final Map<String, Object> mapVariables, final String claveVariable, final BigDecimal varGic) {
		return getPorcentaje(mapVariables, claveVariable, varGic);
	}
	
	/**
	 * Devuelve el resultado del número pasado por parámetro dividido entre 100.
	 * @param mapVariables
	 * @param claveVariable
	 * @param numero
	 * @return
	 */
	public static BigDecimal getPorcentaje (final Map<String, Object> mapVariables, final String claveVariable, final BigDecimal numero){
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		//Siempre guardamos este valor
		if (null == preSalida) {
			salida = numero.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);		
			mapVariables.put(claveVariable, salida);
		} else {
			salida = (BigDecimal)preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Devuelve el resultado de la variable varPorreval
	 * @param mapVariables
	 * @param claveVariable
	 * @param numero
	 * @return
	 */
	public static BigDecimal getvarPorreval(final Map<String, Object> mapVariables, final String claveVariable){
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		//Siempre guardamos este valor
		if (null == preSalida) {
			salida = BigDecimal.ONE.multiply(new BigDecimal(ConstantsFunciones.CTE_5).multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));		
			mapVariables.put(claveVariable, salida);
		} else {
			salida = (BigDecimal)preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Devuelve el valor varGic/100 o varGipc/100. Si no existe la calcula, la almacena en el Hashmap y la devuelve.
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param claveVarTabMort
	 * @param btcUmic
	 * @param umic
	 * @param varCritEdad
	 * @return
	 */	
	public static BigDecimal getVarFactorVrta(final Map<String, Object> mapVariables, final String claveVariable, final String claveVarTabMort,
			final DetalleBaseTecnica btcUmic, final Umic umic, final String varCritEdad) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		//Siempre guardamos este valor
		if (null == preSalida) {
			
			// Variables locales
			Integer varEdad;
			//BigDecimal varI1;
			BigDecimal varI2;
			BigDecimal varPrima;
			BigDecimal varGipc;
			BigDecimal varGipcDiv100;
			List<BigDecimal> varTabMort;
			BigDecimal varVrta;
			BigDecimal unoMasPrimaDivVrta;
			Timestamp varFechaEfecto;
			// Fin variables locales
			
			// Si la modalidad de la UMIC que se trata es del negocio Individuales, se toma la fecha
			// de fin de efecto en lugar de la fecha de inicio. (Cambio de alcance 09/2015)
			if (umic.getDatosGenerales().getCnegocio().equals(ConstantesSolvencia.NEGOCIO_INDIVIDUAL)){
				varFechaEfecto = umic.getFechas().getFecefecini();
			} else {
				varFechaEfecto = umic.getFechas().getFecinisus();
			}			
			
			//varI1 = btcUmic.getItcalc().get(0);
			varI2 = btcUmic.getItcalc().get(1);
			varGipc = btcUmic.getGtorosspPrima();
			varPrima = umic.getPrimas().getIprimatarada();
			Integer varEdifer = umic.getDatosGenerales().getEdifer();
			
			// Calculo de varVrta
			varEdad = FuncionesAuxiliares.nEdad(varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCritEdad, umic.getRentas().getFecIni(), varEdifer).intValue();
			varTabMort = UtilModulos.getVarValoresTabMort(mapVariables, claveVarTabMort, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCritEdad, ConstantesSolvencia.CTE_TABMORT_L);
			varVrta = FuncionesRentas.vrta(ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_99, varI2, ConstantsFunciones.CTE_99, varI2,
					varEdad, varTabMort, ConstantsFunciones.CTE_12, BigDecimal.ZERO, false, 0);
			
			// Calculo de varFactorVrta: (varGic / 100) * ( varPrima / (1 + varVrta) )
			varGipcDiv100 = varGipc.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
			unoMasPrimaDivVrta = varPrima.divide(BigDecimal.ONE.add(varVrta), ConstantsFunciones.MATH_CONTEXT);
			salida = varGipcDiv100.multiply(unoMasPrimaDivVrta);
			
			mapVariables.put(claveVariable, salida);
		} else {
			salida = (BigDecimal)preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Devuelve el valor VarUnoMasVarIFal. Si no existe la calcula, la almacena en el Hashmap y la devuelve.
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param varIFal
	 * @return
	 */
	public static BigDecimal getVarUnoMasVarIFal(final Map<String, Object> mapVariables, final String claveVariable, final BigDecimal varIFal) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		//Siempre guardamos este valor
		if (null == preSalida) {
			salida = BigDecimal.ONE.add(varIFal.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
			mapVariables.put(claveVariable, salida);
		} else {
			salida = (BigDecimal)preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Devuelve el valor getVarUnoMasPrp. Si no existe la calcula, la almacena en el Hashmap y la devuelve.
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param varPrp
	 * @return
	 */
	public static BigDecimal getVarUnoMasPrp(final Map<String, Object> mapVariables, final String claveVariable, final BigDecimal varPrp) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		//Siempre guardamos este valor
		if (null == preSalida) {
			salida = BigDecimal.ONE.add(varPrp.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
			mapVariables.put(claveVariable, salida);
		} else {
			salida = (BigDecimal)preSalida;
		}
		
		return salida;
	}

	/**
	 * Devuelve el valor getPrpDivIfal. Si no existe la calcula, la almacena en el Hashmap y la devuelve.
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param varUnoMasPrp
	 * @param varUnoMasIfal
	 * @return
	 */
	public static BigDecimal getPrpDivIfal(final Map<String, Object> mapVariables, final String claveVariable, final BigDecimal varUnoMasPrp, final BigDecimal varUnoMasIfal) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		//Siempre guardamos este valor
		if (null == preSalida) {
			salida = varUnoMasPrp.divide(varUnoMasIfal, ConstantsFunciones.MATH_CONTEXT);
			mapVariables.put(claveVariable, salida);
		} else {
			salida = (BigDecimal)preSalida;
		}
		
		return salida;
	}
	
	
	/**
	 * Devuelve el valor getVarUnoMenosGepc. Si no existe la calcula, la almacena en el Hashmap y la devuelve.
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param varGepc
	 * @return
	 */
	public static BigDecimal getVarUnoMenosGepc(final Map<String, Object> mapVariables, final String claveVariable, final BigDecimal varGepc) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		//Siempre guardamos este valor
		if (null == preSalida) {
			salida = BigDecimal.ONE.subtract(varGepc.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
			mapVariables.put(claveVariable, salida);
		} else {
			salida = (BigDecimal)preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Devuelve valor varIFal. Si no existe la calcula, la almacena en el Hashmap y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codigoConstante
	 * @return
	 */
	public static BigDecimal getVarIFal(final Map<String, Object> mapVariables, final String claveVariable, final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbasetec, final String codigoConstante, final BigDecimal interes) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		//Siempre guardamos este valor
		if (null == preSalida) {
			final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
			final String defAuxiliar = (String)servicio.recuperarDefinicionAuxiliar(ccartera, kmodalidad, kgarantia, kbasetec, codigoConstante);
			
			// El dato a null se controla fuera, en el módulo, donde se controla con su error personalizado para la variable afectada y generalmente en la primera iteracion
			if (defAuxiliar != null) {
				salida = new BigDecimal((String)defAuxiliar);
				
				if (salida.equals(BigDecimal.ONE)) {
					//Se recupera el de la bti
					salida = interes; 
				}else {
					salida = BigDecimal.ZERO;
				}
				
				mapVariables.put(claveVariable, salida);
			}
		} else {
			salida = (BigDecimal)preSalida;
		}
		return salida;
	}
	
	/**
	 * Devuelve valor varFut. Si no existe la calcula, la almacena en el Hashmap y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codigoConstante
	 * @return
	 */
	public static BigDecimal getVarFut(final Map<String, Object> mapVariables, final String claveVariable, final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbasetec, final String codigoConstante) {
		return getsetBigDecimalRecuperarDefinicionAuxiliar(mapVariables, claveVariable, ccartera, kmodalidad, kgarantia, kbasetec, codigoConstante);
	}
	
	/**
	 * Devuelve valor varIant. Si no existe la calcula, la almacena en el Hashmap y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codigoConstante
	 * @return
	 */
	public static BigDecimal getVarIant(final Map<String, Object> mapVariables, final String claveVariable, final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbasetec, final String codigoConstante) {
		return getsetBigDecimalRecuperarDefinicionAuxiliar(mapVariables, claveVariable, ccartera, kmodalidad, kgarantia, kbasetec, codigoConstante);
	}
	
	/**
	 * Devuelve varTipoAlfa. Si no está almacenada la calcula, la setea en el HASHMAP y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @return
	 */
	public static String getVarTipoAlfa(final Map<String, Object> mapVariables, final String claveVariable, final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbasetec) {
		final Object preSalida = mapVariables.get(claveVariable);
		String salida = null;
		
		if (null == preSalida) {
			final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
			salida = (String) servicio.recuperarDefinicionAuxiliar(ccartera, kmodalidad, kgarantia, kbasetec, ConstantsModulos.CTE_VA_TIPO_ALFA);
			if (null == salida) {
				// Valor por defecto -> PRORRATA
				salida = ConstantsFunciones.CTE_CAL_PRORRATA;
			}
			mapVariables.put(claveVariable, salida);
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + ConstantsModulos.CTE_VA_TIPO_ALFA + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (String)preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Devuelve valor varMescaren. Si no existe la calcula, la almacena en el Hashmap y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codigoConstante
	 * @return
	 */
	public static Integer getVarMescaren(final Map<String, Object> mapVariables, final String claveVariable, final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbasetec, final String codigoConstante) {
		return getsetIntegerRecuperarDefinicionAuxiliar(mapVariables, claveVariable, ccartera, kmodalidad, kgarantia, kbasetec, codigoConstante);
	}
	
	/**
	 * Devuelve valor varCriEdad. Si no existe la calcula, la almacena en el Hashmap y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codigoConstante
	 * @return
	 */
	public static String getVarCriEdad(final Map<String, Object> mapVariables, final String claveVariable, final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbasetec, final String codigoConstante) {
		return getsetStringRecuperarDefinicionAuxiliar(mapVariables, claveVariable, ccartera, kmodalidad, kgarantia, kbasetec, codigoConstante);
	}
	
	/**
	 * Devuelve valor de varBeta. Si no existe la calcula, la almacena en el hashmap y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codigoConstante
	 * @return
	 */
	public static String getVarBeta(final Map<String, Object> mapVariables, final String claveVariable, final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbasetec, final String codigoConstante) {
		return getsetStringRecuperarDefinicionAuxiliar(mapVariables, claveVariable, ccartera, kmodalidad, kgarantia, kbasetec, codigoConstante);
	}
		
	/**
	 * Devuelve valor varPas. Si no existe la calcula, la almacena en el Hashmap y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codigoConstante
	 * @return
	 */
	public static BigDecimal getVarPas(final Map<String, Object> mapVariables, final String claveVariable, final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbasetec, final String codigoConstante) {
		return getsetBigDecimalRecuperarDefinicionAuxiliar(mapVariables, claveVariable, ccartera, kmodalidad, kgarantia, kbasetec, codigoConstante);
	}
	
	/**
	 * Devuelve valor GEdadMax. Si no existe la calcula, la almacena en el Hashmap y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codigoConstante
	 * @return
	 */
	public static BigDecimal getVarGEdadMax(final Map<String, Object> mapVariables, final String claveVariable, final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbasetec, final String codigoConstante) {
		return getsetBigDecimalRecuperarDefinicionAuxiliar(mapVariables, claveVariable, ccartera, kmodalidad, kgarantia, kbasetec, codigoConstante);
	}
	/**
	 * Devuelve el valor de la constante de externalización para el Ptipo Automático. 
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codigoConstante
	 * @return
	 */
	public static Integer getCteExtr(){
		Integer constante = null;
		
		//Se recupera la variable auxiliar que define la constante de extracción
		final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		final Object defAuxiliar = servicio.recuperarDefinicionAuxiliar("CTEEXTR");
		
		
		if (null != defAuxiliar) {
			constante = Integer.valueOf((String)defAuxiliar);

		} else {
			//Si no existe la variable auxiliar se toma la constante por defecto = 500
			constante = 500;
			//constante = 0;
		}
		
		return constante;
	}
	
	/**
	 * Recupera BigDecimal a partir de servicio.recuperarDefinicionAuxiliar(ccartera, kmodalidad, kgarantia, codigoConstante).
	 * Genera un servicio si la variable no existía.
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codigoConstante
	 * @return
	 */
	public static BigDecimal getsetBigDecimalRecuperarDefinicionAuxiliar(final Map<String, Object> mapVariables, final String claveVariable, final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbasetec, final String codigoConstante) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;

		if (null == preSalida) {
			final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
			final Object defAuxiliar = servicio.recuperarDefinicionAuxiliar(ccartera, kmodalidad, kgarantia, kbasetec, codigoConstante);
			
			// El dato a null se controla fuera, en el módulo, donde se controla con su error personalizado para la variable afectada y generalmente en la primera iteracion
			if (null != defAuxiliar) {
				salida = new BigDecimal((String)defAuxiliar);
				mapVariables.put(claveVariable, salida);
				if (UtilModulos.LOG.isTraceEnabled()) {
					UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
				}
			}
		} else {
			salida = (BigDecimal)preSalida;
		}
		return salida;
	}
	
	/**
	 * Recupera Integer a partir de servicio.recuperarDefinicionAuxiliar(ccartera, kmodalidad, kgarantia, codigoConstante).
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codigoConstante
	 * @return
	 */
	public static Integer getsetIntegerRecuperarDefinicionAuxiliar(final Map<String, Object> mapVariables, final String claveVariable, final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbasetec, final String codigoConstante) {
		final Object preSalida = mapVariables.get(claveVariable);
		Integer salida = null;
		
		//Siempre guardamos este valor
		if (null == preSalida) {
			final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
			final Object defAuxiliar = servicio.recuperarDefinicionAuxiliar(ccartera, kmodalidad, kgarantia, kbasetec, codigoConstante);
			
			// El dato a null se controla fuera, en el módulo, donde se controla con su error personalizado para la variable afectada y generalmente en la primera iteracion
			if (null != defAuxiliar) {
				salida = Integer.valueOf((String)defAuxiliar);
				mapVariables.put(claveVariable, salida);
				if (UtilModulos.LOG.isTraceEnabled()) {
					UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
				}
			}
		} else {
			salida = (Integer)preSalida;
		}
		return salida;
	}
	
	/**
	 * Recupera String a partir de servicio.recuperarDefinicionAuxiliar(ccartera, kmodalidad, kgarantia, codigoConstante).
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codigoConstante
	 * @return
	 */
	public static String getsetStringRecuperarDefinicionAuxiliar(final Map<String, Object> mapVariables, final String claveVariable, final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbasetec, final String codigoConstante) {
		final Object preSalida = mapVariables.get(claveVariable);
		String salida = null;
		
		//Siempre guardamos este valor
		if (null == preSalida) {
			final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
			salida = (String)servicio.recuperarDefinicionAuxiliar(ccartera, kmodalidad, kgarantia, kbasetec, codigoConstante);
			
			// El dato a null se controla fuera, en el módulo, donde se controla con su error personalizado para la variable afectada y generalmente en la primera iteracion
			if (null != salida) {
				mapVariables.put(claveVariable, salida);
				if (UtilModulos.LOG.isTraceEnabled()) {
					UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
				}
			}
		} else {
			salida = (String)preSalida;
		}
		return salida;
	}
	
	/**
	 * Devuelve variable del tipo String almacenada en el HashMap.
	 * Si no está almacenada hace lo siguiente:
	 * 			- La calcula (la recibe como parámetro de entrada).
	 * 			- La setea en el HashMap.
	 * 			- La devuelve.
	 */
	private static String setGetString (final Map<String, Object> mapVariables, final String clave, final String variable) {
		final Object preSalida = mapVariables.get(clave);
		String salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = variable;
			mapVariables.put(clave, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + clave + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (String)preSalida;
		}
		return salida;
	}
	
	/**
	 * Devuelve variable del tipo BigDecimal almacenada en el HashMap.
	 * Si no está almacenada hace lo siguiente:
	 * 			- La calcula (la recibe como parámetro de entrada).
	 * 			- La setea en el HashMap.
	 * 			- La devuelve.
	 * @param mapVariables
	 * @param modulo
	 * @param constanteVariable
	 * @param variable
	 * @return
	 */
	private static BigDecimal setGetBigDecimal (final Map<String, Object> mapVariables, final String clave, final BigDecimal variable) {
		final Object preSalida = mapVariables.get(clave);
		BigDecimal salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = variable;
			mapVariables.put(clave, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + clave + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal)preSalida;
		}

		return salida;
	}
	
	/**
	 * Devuelve variable del tipo Integer almacenada en el HashMap.
	 * Si no está almacenada hace lo siguiente:
	 * 			- La calcula (la recibe como parámetro de entrada).
	 * 			- La setea en el HashMap.
	 * 			- La devuelve.
	 * @param mapVariables
	 * @param modulo
	 * @param constanteVariable
	 * @param variable
	 * @return
	 */
	private static Integer setGetInteger (final Map<String, Object> mapVariables, final String clave, final Integer variable) {
		final Object preSalida = mapVariables.get(clave);
		Integer salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = variable;
			mapVariables.put(clave, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + clave + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (Integer)preSalida;
		}

		return salida;
	}
	
	/**
	 * Devuelve variable del tipo List<DetalleCorriente> almacenada en el HashMap.
	 * Si no está almacenada hace lo siguiente:
	 * 			- La calcula (la recibe como parámetro de entrada).
	 * 			- La setea en el HashMap.
	 * 			- La devuelve.
	 * @param mapVariables
	 * @param clave
	 * @param variable
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<DetalleCorriente> setGetListDetalleCorriente (final Map<String, Object> mapVariables, final String clave, final String baseTecnica, final Timestamp fecCierre, final UmicKey claveUmic) {
		final Object preSalida = mapVariables.get(clave);
		List<DetalleCorriente> salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			final IObtenerDatos servicioDatos = FachadaServicios.getObtenerDatos();
			salida = servicioDatos.recuperarProyeccion(baseTecnica, fecCierre, claveUmic);
			mapVariables.put(clave, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + clave + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (List<DetalleCorriente>)preSalida;
		}

		return salida;
	}
	
	/**
	 * Devuelve variable del tipo Timestamp almacenada en el HashMap.
	 * Si no está almacenada hace lo siguiente:
	 * 			- La calcula (la recibe como parámetro de entrada).
	 * 			- La setea en el HashMap.
	 * 			- La devuelve.
	 * @param mapVariables
	 * @param modulo
	 * @param constanteVariable
	 * @param variable
	 * @return
	 */
	private static Timestamp setGetTimestamp (final Map<String, Object> mapVariables, final String clave, final Timestamp variable) {
		final Object preSalida = mapVariables.get(clave);
		Timestamp salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = variable;
			mapVariables.put(clave, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + clave + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (Timestamp)preSalida;
		}

		return salida;
	}
	
	/**
	 *  Se encargará de calcular las fechas de Pago y de Devengo para cada periodo de proyección recuperados de la umic en base al criterio establecido 
	 *  en la tabla de opciones de generación de flujos para la modalidad / garantía de la umic, excepto para la corriente de provisión matemática 
	 *  por fórmula cerrada que se asignarán dichas fechas a inicio de periodo.
		Se recorrerá la estructura proyUmic desde el primer de la misma hasta el último. 
		Para cada periodo de la umic se calcularán la fecha de pago y la fecha de devengo correspondiente a dicho periodo, 
		tal y como se describe a continuación.

	 * 
	 * @param lstPeriodo array de la estructura PERIODOS
	 * @param criterioFechaPago criterio para la fecha de pago
	 * @param criterioFecDev criterio para la fecha devengo
	 * @param subProcesoActual subprocesoActual
	 * @param fcalc fecha de cálculo
	 */
	public static void calcularFechasPagoDevengo(final DetalleCorriente detalle, final String criterioFechaPago, final String criterioFecDev, final String subProcesoActual, final Umic umic, final List<PlanPagos> lstPlanPagos, 
			final List<PagosPlanificados> lstPagosPlani, final Integer numPeriodos, final Integer iteracion, final Map<String, Object> mapVariables, final Timestamp fcalc, final boolean inipg) {

		if (UtilModulos.LOG.isTraceEnabled()) {
			UtilModulos.LOG.trace("Inicio funcion calcularFechasPagoDevengo >> de la clase UtilModulos, para la entrada criterioFechaPago = {} , criterioFechaDevengo = {}, subProcesoActual = {} e iteracion = {}", criterioFechaPago, criterioFecDev, subProcesoActual, iteracion);
		}
			
		// Si el criterio de la generación de las fechas de pago tiene en cuenta la fecha de inicio de la renta y el inicio del periodo supera la fecha de inicio de la renta se corta la generación de fechas de pago
		if ((!criterioFechaPago.equals(ConstantsModulos.CTE_VAL_FINIR) && !criterioFechaPago.equals(ConstantsModulos.CTE_VAL_MITIR)) || (umic.getRentas().getFecIni() != null && !umic.getRentas().getFecIni().before(detalle.getFechaDesde()))) {
			//establecemos la fecha de pago
			if (ConstantsModulos.CTE_PROY_PRV.equals(subProcesoActual)) {
				detalle.getTotalFlujoProyeccion().setFechaPago(UtilModulos.establecerFechaPagoDevengo(criterioFechaPago, detalle, lstPlanPagos, lstPagosPlani, umic, numPeriodos, iteracion, mapVariables, subProcesoActual, ConstantsFunciones.VAR_FECHA_PAGO, fcalc, inipg));
			} else {
				detalle.getBloqueBySubproceso(subProcesoActual).setFechaPago(UtilModulos.establecerFechaPagoDevengo(criterioFechaPago, detalle, lstPlanPagos, lstPagosPlani, umic, numPeriodos, iteracion, mapVariables, subProcesoActual, ConstantsFunciones.VAR_FECHA_PAGO, fcalc, inipg));
			}
		}
		
		// Si el criterio de la generación de las fechas de devengo tiene en cuenta la fecha de inicio de la renta y el inicio del periodo supera la fecha de inicio de la renta se corta la generación de fechas de devengo
		if ((!criterioFecDev.equals(ConstantsModulos.CTE_VAL_FINIR) && !criterioFecDev.equals(ConstantsModulos.CTE_VAL_MITIR)) || (umic.getRentas().getFecIni() != null && !umic.getRentas().getFecIni().before(detalle.getFechaDesde()))) {
			//establecemos la fecha de devengo
			if (ConstantsModulos.CTE_PROY_PRV.equals(subProcesoActual)) {
				detalle.getTotalFlujoProyeccion().setFechaDevengo(UtilModulos.establecerFechaPagoDevengo(criterioFecDev, detalle, lstPlanPagos, lstPagosPlani, umic, numPeriodos, iteracion, mapVariables, subProcesoActual, ConstantsFunciones.VAR_FECHA_DEVENGO, fcalc, inipg));
			} else {
				detalle.getBloqueBySubproceso(subProcesoActual).setFechaDevengo(UtilModulos.establecerFechaPagoDevengo(criterioFecDev, detalle, lstPlanPagos, lstPagosPlani, umic, numPeriodos, iteracion, mapVariables, subProcesoActual, ConstantsFunciones.VAR_FECHA_DEVENGO, fcalc, inipg));
			}
		}
		
		if (UtilModulos.LOG.isTraceEnabled()) {
			UtilModulos.LOG.trace("Fin funcion << calcularFechasPagoDevengo >> de la clase UtilModulos");
		}
	}
	
	/**
	 * 
	 * Funcion que establece la fechaPago, en funcion del criterio de la fecha.
	 * 
	 * @param criFecPagoDeve criterio de pago y devengo
	 * @param detalleActual Elemento j de la estructura detalleCorriente
	 * @param lstPlanPagos Lista de planes de pagos
	 * @param lstPagosPlani Lista de pagos planificados
	 * @param umic Bloque de datos fechas de la umic
	 * @param totalPeriodos numero total de periodos existentes
	 * @param iteracion iteracion por la que vamos
	 * @param mapVariables mapa con las variables de memoria necesarias
	 * @param subProcesoActual subprocesoActual
	 * @return fechaPago
	 */
	public static Timestamp establecerFechaPagoDevengo(final String criFecPagoDeve, final DetalleCorriente detalleActual, final List<PlanPagos> lstPlanPagos, 
			final List<PagosPlanificados> lstPagosPlani, final Umic umic, final Integer totalPeriodos, final Integer iteracion, final Map<String, Object> mapVariables, final String subProcesoActual, final String varibleMemoria, final Timestamp fcalc, final boolean inipg) {
		//Variables locales
		Timestamp fechaPago = null;
		int diaPago;
		//Fin variables locales
		if (ConstantsModulos.CTE_PROY_PRV.equals(subProcesoActual) && ConstantsModulos.CTE_VAL_INIP.equals(criFecPagoDeve)) {
			fechaPago = detalleActual.getFechaDesde();
		} else {
			if (ConstantsModulos.CTE_VAL_FPAGO.equals(criFecPagoDeve)) {
				/**
				 * Si j es el último periodo, la fecha de pago será: 
					periodosUmic (j).fechaHasta
				 */
				if (iteracion.equals(totalPeriodos)) {
//					fechaPago = detalleActual.getFechaHasta();
					fechaPago = detalleActual.getFechaDesde();
				}
			} else if (ConstantsModulos.CTE_VAL_INIP.equals(criFecPagoDeve)) {
				fechaPago = obtenerFechaPagoDevengoCriterioINIP(iteracion, fcalc, detalleActual.getFechaDesde(), umic.getFechas().getFecefecini());
			} else if (ConstantsModulos.CTE_VAL_MITAD.equals(criFecPagoDeve)) {
				/**
				 * Fecha situada justo en la mitad del periodo comprendido entre: periodosUmic (j).fechaDesde y periodosUmic (j).fechaHasta. 
				 * y tomará como valor la fecha situada justo en la mitad del periodo comprendido entre la fecha desde y fecha hasta, redondeando por exceso.
				 *  
					proyUmic(j).fechaPago = add(periodos(j).fechaDesde.DAY,[ periodosUmic (j).fechaHasta- periodosUmic (j).fechaDesde] /2)
					(es decir, si el resultado de la división periodosUmic (j).fechaHasta- periodosUmic (j).fechaDesde /2 no sea un entero, será xx’5, 
					se aproximará al siguiente entero superior).
				 */
				fechaPago = UtilFechas.obtenerFechaMitad(detalleActual.getFechaDesde(), detalleActual.getFechaHasta());
			} else if (ConstantsModulos.CTE_VAL_MITIR.equals(criFecPagoDeve)) {
				fechaPago = UtilFechas.obtenerFechaMitad(detalleActual.getFechaDesde(), detalleActual.getFechaHasta());
				if (umic.getRentas().getFecIni().before(detalleActual.getFechaHasta())) {
					fechaPago = UtilFechas.obtenerFechaMitad(detalleActual.getFechaDesde(), umic.getRentas().getFecIni());				
				}
			} else if (ConstantsModulos.CTE_VAL_FINP.equals(criFecPagoDeve)) {
				fechaPago = detalleActual.getFechaHasta();
			} else if (ConstantsModulos.CTE_VAL_FINIR.equals(criFecPagoDeve)) {
				if (umic.getRentas().getFecIni().before(detalleActual.getFechaHasta())) {
					fechaPago = umic.getRentas().getFecIni();
				} else {
					fechaPago = detalleActual.getFechaHasta();
				}
			} else if (ConstantsModulos.CTE_VAL_PLANI.equals(criFecPagoDeve) || ConstantsModulos.CTE_VAL_PVIDA.equals(criFecPagoDeve) || ConstantsModulos.CTE_VAL_PLANB.equals(criFecPagoDeve) || ConstantsModulos.CTE_VAL_EFTEC.equals(criFecPagoDeve) || ConstantsModulos.CTE_VAL_PLAIN.equals(criFecPagoDeve) || ConstantsModulos.CTE_VAL_EFTIN.equals(criFecPagoDeve) || ConstantsModulos.CTE_VAL_PLANC.equals(criFecPagoDeve)) {
				fechaPago = obtenerFechaPagoDevengoCriterioPLANI(umic, detalleActual, lstPlanPagos, lstPagosPlani, iteracion, subProcesoActual, criFecPagoDeve, inipg);
			} else if (ConstantsModulos.CTE_VAL_RENOV.equals(criFecPagoDeve)) {
				fechaPago = obtenerFechaPagoDevengoCasoEfectoRenovacion(detalleActual, umic, iteracion, mapVariables, varibleMemoria, fcalc);
			}else if (ConstantsModulos.CTE_VAL_IIDIR.equals(criFecPagoDeve)){
				if (detalleActual.getFechaHasta().before(umic.getRentas().getFecIni()) && detalleActual.getFechaDesde().before(umic.getRentas().getFecIni())){
					fechaPago = null;
				}else if(detalleActual.getFechaHasta().after(umic.getRentas().getFecIni()) && !detalleActual.getFechaDesde().after(umic.getRentas().getFecIni())){
					fechaPago = umic.getRentas().getFecIni();
				}else{
					fechaPago = detalleActual.getFechaDesde();
				}
			}else if (ConstantsModulos.CTE_VAL_FIDIR.equals(criFecPagoDeve)){
				if (detalleActual.getFechaHasta().before(umic.getRentas().getFecIni()) && detalleActual.getFechaDesde().before(umic.getRentas().getFecIni())){
					fechaPago = null;
				}else if(detalleActual.getFechaHasta().after(umic.getRentas().getFecIni()) && !detalleActual.getFechaDesde().after(umic.getRentas().getFecIni())){
					fechaPago = umic.getRentas().getFecIni();
				}else{
					fechaPago = detalleActual.getFechaHasta();
				}
			}else if(ConstantsModulos.CTE_VAL_INITA.equals(criFecPagoDeve)){
				
				fechaPago = detalleActual.getFechaDesde();
			}else if(ConstantsModulos.CTE_VAL_MPRI.equals(criFecPagoDeve)){
				diaPago = umic.getPrimas().getFdiadepago();
				if (!umic.getPrimas().getCformpago().equals(ConstantsFunciones.CTE_FORMPAGO_9)){
					if(UtilFechas.getMes(detalleActual.getFechaDesde())==ConstantsModulos.CTE_INT_2)
					{
						if(diaPago==ConstantsModulos.CTE_INT_29)
						{
							diaPago = ConstantsModulos.CTE_INT_28;
						}
					}	
				} else {
					if (umic.getDatosGenerales().getCsitupol().equals(ConstantsFunciones.CTE_POL_NO_RED)){
						if (umic.getDatosGenerales().getCnegocio().equals(ConstantsModulos.CTE_NEGOCIO_INDIVIDUAL)){
							diaPago = UtilFechas.getDia(umic.getFechas().getFecefecini());
						} else {
							diaPago = UtilFechas.getDia(umic.getFechas().getFecinisus());
						}
					} else if (umic.getDatosGenerales().getCsitupol().equals(ConstantsFunciones.CTE_APOR_REDUCIDA)) {
						diaPago = UtilFechas.getDia(umic.getFechas().getFecefecred());
					}
				}
				if (diaPago >= UtilFechas.getDia(detalleActual.getFechaDesde())){
					fechaPago = new Timestamp(new GregorianCalendar(UtilFechas.getAnio(detalleActual.getFechaDesde()), UtilFechas.getMes(detalleActual.getFechaDesde())-1, diaPago, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
				} else {
					fechaPago = new Timestamp(new GregorianCalendar(UtilFechas.getAnio(detalleActual.getFechaDesde()), UtilFechas.getMes(detalleActual.getFechaDesde()), diaPago, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
				}
			} else if(ConstantsModulos.CTE_VAL_PLNTP.equals(criFecPagoDeve)){
				
				detalleActual.setImpPago(BigDecimal.ZERO);
				int index = -1;
				// Para PLANI se deja la fecha de pago en la fecha que esté y NO se lleva la fecha de pago al ultimo dia de mes
				index = Collections.binarySearch(lstPlanPagos, null, new Comparator<PlanPagos>(){

					@Override
					public int compare(PlanPagos arg0, PlanPagos arg1) {
						final Timestamp fecPago = arg0.getFecPago(); 
						if((!fecPago.before(detalleActual.getFechaDesde()) && fecPago.before(detalleActual.getFechaHasta())) ||
								   (( UtilFechas.getDia(fecPago) == ConstantsFunciones.CTE_29 ) && (fecPago.before(detalleActual.getFechaDesde()) && !fecPago.before(detalleActual.getFechaHasta())))){
							return 0;							
						} else if(fecPago.before(detalleActual.getFechaDesde())) {
							return -1;
						} else {
							return 1;
						}
					}
						
				});
				if(index >= 0) {
					if (null != umic.getDatosNiif17() && null != umic.getDatosNiif17().getAgdpg() && umic.getDatosNiif17().getAgdpg().equals("D")) {
						diaPago = UtilFechas.getDia(umic.getFechas().getFecinisus());
						if (diaPago >= UtilFechas.getDia(detalleActual.getFechaDesde())){
							if(diaPago > 28 && UtilFechas.getMes(detalleActual.getFechaDesde()) == 2){
								diaPago = 28;
							}else if (diaPago > 30 && (UtilFechas.getMes(detalleActual.getFechaDesde()) == 4 || 
									UtilFechas.getMes(detalleActual.getFechaDesde()) == 6 || 
									UtilFechas.getMes(detalleActual.getFechaDesde()) == 9 ||
									UtilFechas.getMes(detalleActual.getFechaDesde()) == 11)){
								diaPago = 30;
							}
							fechaPago = new Timestamp(new GregorianCalendar(UtilFechas.getAnio(detalleActual.getFechaDesde()), UtilFechas.getMes(detalleActual.getFechaDesde())-1, diaPago, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
						} else {
							fechaPago = new Timestamp(new GregorianCalendar(UtilFechas.getAnio(detalleActual.getFechaDesde()), UtilFechas.getMes(detalleActual.getFechaDesde()), diaPago, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
						}
					} else if (null != umic.getDatosNiif17() && null != umic.getDatosNiif17().getAgdpg() && umic.getDatosNiif17().getAgdpg().equals("F")) {
						fechaPago = lstPlanPagos.get(index).getFecPago();
						//fechaPago = detalleActual.getFechaHasta();
						fechaPago = UtilFechas.incrDias(fechaPago,(UtilFechas.getDia(UtilFechas.getUltimoDiaDelMes(fechaPago)) - UtilFechas.getDia(fechaPago)));

					}
					
					detalleActual.setImpPago(lstPlanPagos.get(index).getImpPago());
				}
				
//				if (umic.getDatosNiif17().getAgdpg().equals("D")) {
//					diaPago = UtilFechas.getDia(umic.getFechas().getFecinisus());
//					if (diaPago >= UtilFechas.getDia(detalleActual.getFechaDesde())){
//						fechaPago = new Timestamp(new GregorianCalendar(UtilFechas.getAnio(detalleActual.getFechaDesde()), UtilFechas.getMes(detalleActual.getFechaDesde())-1, diaPago, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
//					} else {
//						fechaPago = new Timestamp(new GregorianCalendar(UtilFechas.getAnio(detalleActual.getFechaDesde()), UtilFechas.getMes(detalleActual.getFechaDesde()), diaPago, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
//					}
//				} else if (umic.getDatosNiif17().getAgdpg().equals("F")) {
//					fechaPago = lstPlanPagos.get(iteracion - 1).getFecPago();
//					//fechaPago = detalleActual.getFechaHasta();
//					fechaPago = UtilFechas.incrDias(fechaPago,(UtilFechas.getDia(UtilFechas.getUltimoDiaDelMes(fechaPago)) - UtilFechas.getDia(fechaPago)));
//
//				}

//				if (iteracion <= lstPlanPagos.size()) {
//					detalleActual.setImpPago(lstPlanPagos.get(iteracion - 1).getImpPago());
//				} else {
//					detalleActual.setImpPago(lstPlanPagos.get(lstPlanPagos.size() - 1).getImpPago());
//				}
			} else if (ConstantsModulos.CTE_VAL_RENO2.equals(criFecPagoDeve)) {
                fechaPago = obtenerFechaPagoDevengoCasoEfectoReno2(detalleActual, umic, iteracion, mapVariables, varibleMemoria, fcalc);
			} else if (ConstantsModulos.CTE_VAL_FIJUB.equals(criFecPagoDeve)) {
            	if (umic.getDatosDescuentos().getFecJubilacion().before(detalleActual.getFechaHasta())) {
					fechaPago = umic.getDatosDescuentos().getFecJubilacion();
				} else {
					fechaPago = detalleActual.getFechaHasta();
				}
            	if (!ConstantsModulos.CTE_TIPO_PRES_LOCA.equals(umic.getRentas().getTempVit())) {
            		detalleActual.setImpPago(BigDecimal.ZERO);
    				int index = -1;
    				index = Collections.binarySearch(lstPlanPagos, null, new Comparator<PlanPagos>(){

    					@Override
    					public int compare(PlanPagos arg0, PlanPagos arg1) {
    						final Timestamp fecPago = arg0.getFecPago(); 
    						if((!fecPago.before(detalleActual.getFechaDesde()) && fecPago.before(detalleActual.getFechaHasta())) ||
    								   (( UtilFechas.getDia(fecPago) == ConstantsFunciones.CTE_29 ) && (fecPago.before(detalleActual.getFechaDesde()) && !fecPago.before(detalleActual.getFechaHasta())))){
    							return 0;							
    						} else if(fecPago.before(detalleActual.getFechaDesde())) {
    							return -1;
    						} else {
    							return 1;
    						}
    					}
    						
    				});
    				if(index >= 0) {
    					detalleActual.setImpPago(lstPlanPagos.get(index).getImpPago());
    				}
            	} else {
            		detalleActual.setImpPago(BigDecimal.ZERO);
    				int index = -1;
    				index = Collections.binarySearch(lstPagosPlani, null, new Comparator<PagosPlanificados>(){

    					@Override
    					public int compare(PagosPlanificados arg0, PagosPlanificados arg1) {
    						final Timestamp fecPago = arg0.getFplreaEfecto(); 
    						if((!fecPago.before(detalleActual.getFechaDesde()) && fecPago.before(detalleActual.getFechaHasta())) ||
    								   (( UtilFechas.getDia(fecPago) == ConstantsFunciones.CTE_29 ) && (fecPago.before(detalleActual.getFechaDesde()) && !fecPago.before(detalleActual.getFechaHasta())))){
    							return 0;							
    						} else if(fecPago.before(detalleActual.getFechaDesde())) {
    							return -1;
    						} else {
    							return 1;
    						}
    					}
    						
    				});
    				if(index >= 0) {
    					detalleActual.setImpPago(lstPagosPlani.get(index).getEplreaBruto());
    				}
  
            	}
            	
			} else if (ConstantsModulos.CTE_VAL_MIJUB.equals(criFecPagoDeve)) {
				fechaPago = UtilFechas.obtenerFechaMitad(detalleActual.getFechaDesde(), detalleActual.getFechaHasta());
				if (umic.getDatosDescuentos().getFecJubilacion().before(detalleActual.getFechaHasta())) {
					fechaPago = UtilFechas.obtenerFechaMitad(detalleActual.getFechaDesde(), umic.getDatosDescuentos().getFecJubilacion());				
				}
				
				if (!ConstantsModulos.CTE_TIPO_PRES_LOCA.equals(umic.getRentas().getTempVit())) {
					detalleActual.setImpPago(BigDecimal.ZERO);
					int index = -1;
					index = Collections.binarySearch(lstPlanPagos, null, new Comparator<PlanPagos>(){

						@Override
						public int compare(PlanPagos arg0, PlanPagos arg1) {
							final Timestamp fecPago = arg0.getFecPago(); 
							if((!fecPago.before(detalleActual.getFechaDesde()) && fecPago.before(detalleActual.getFechaHasta())) ||
									   (( UtilFechas.getDia(fecPago) == ConstantsFunciones.CTE_29 ) && (fecPago.before(detalleActual.getFechaDesde()) && !fecPago.before(detalleActual.getFechaHasta())))){
								return 0;							
							} else if(fecPago.before(detalleActual.getFechaDesde())) {
								return -1;
							} else {
								return 1;
							}
						}
							
					});
					if(index >= 0) {
						detalleActual.setImpPago(lstPlanPagos.get(index).getImpPago());
					}
				} else {
					detalleActual.setImpPago(BigDecimal.ZERO);
    				int index = -1;
    				index = Collections.binarySearch(lstPagosPlani, null, new Comparator<PagosPlanificados>(){

    					@Override
    					public int compare(PagosPlanificados arg0, PagosPlanificados arg1) {
    						final Timestamp fecPago = arg0.getFplreaEfecto(); 
    						if((!fecPago.before(detalleActual.getFechaDesde()) && fecPago.before(detalleActual.getFechaHasta())) ||
    								   (( UtilFechas.getDia(fecPago) == ConstantsFunciones.CTE_29 ) && (fecPago.before(detalleActual.getFechaDesde()) && !fecPago.before(detalleActual.getFechaHasta())))){
    							return 0;							
    						} else if(fecPago.before(detalleActual.getFechaDesde())) {
    							return -1;
    						} else {
    							return 1;
    						}
    					}
    						
    				});
    				if(index >= 0) {
    					detalleActual.setImpPago(lstPagosPlani.get(index).getEplreaBruto());
    				}
				}
				
			} else if (ConstantsModulos.CTE_VAL_INIPG.equals(criFecPagoDeve)) {
				fechaPago = obtenerFechaPagoDevengoCriterioINIPG(iteracion, fcalc, detalleActual.getFechaDesde(), umic.getFechas().getFecefecini());
			}
		}
		
		return fechaPago;
	}
	
	/**
	 * Función encargada de obtener la fecha de pago o devengo si el criterio es "PLANI"
	 * 
	 * @param umic Bloque de datos fechas de la umic
	 * @param detalleActual Elemento j de la estructura detalleCorriente
	 * @param lstPlanPagos Lista de planes de pagos
	 * @param lstPagosPlani Lista de pagos planificados
	 * @param iteracion iteracion por la que vamos
	 * @param criFecPagoDeve
	 * @return fechaPagoDev
	 */
	public static Timestamp obtenerFechaPagoDevengoCriterioPLANI(final Umic umic, final DetalleCorriente detalleActual, final List<PlanPagos> lstPlanPagos, 
			final List<PagosPlanificados> lstPagosPlani , final Integer iteracion, final String subProcesoActual, final String criFecPagoDeve, final boolean inipg) {
		//Variables locales
		Timestamp fechaPagoDev = null;
		//Fin variables locales
		
		if (UtilModulos.LOG.isTraceEnabled()) {
			UtilModulos.LOG.trace("Inicio función << obtenerFechaPagoDevengoCriterioPLANI >> de la clase UtilModulos");
		}
		
		/**
		 * Si CriterioFecha (de pago   y/ó de devengo) es PLANI (Pagos Planificados), se calculará la fecha correspondiente (de pago y/ó devengo) como sigue.
		 * 
		 * 		-	Si umic.rentas.tempVit = ‘V’, se leerán los pagos de la umic anteriormente calculados en genPagosVitalicios (planPagos).
		 * 			Para cada periodo j que se está procesando se buscará en la estructura de pagos de la renta el pago de renta
		 * 			correspondiente la periodo tratado de forma que:
		 * 			
		 * 				- Si CriterioFecha = PLANI:
		 * 				o	Si existe pago para el periodo tratado, es decir si:
		 * 					proyUmic(j).fechaDesde <= planPagos(n).FechaPago < proyUmic(j).fechaHasta, se hará:
		 * 						-	proyUmic(j).impPago = planPagos(n).impPago
		 * 						-	proyUmic(j).fecPago o proyUmic(j).fechaDevengo = planPagos(n).FechaPago
		 * 				o	Si NO existe pago para el periodo tratado:
		 * 						-	proyUmic(j).impPago = 0
		 * 						-	proyUmic(j).fechaPago o proyUmic(j).fechaDevego = null
		 * 				
		 * 				- Si CriterioFecha = EFTEC:
		 * 				o	Si existe pago para el periodo tratado, es decir si:
		 * 					proyUmic(j).fechaDesde <= FIN DE MES(planPagos(n).FechaPago) < proyUmic(j).fechaHasta, se hará:
		 * 						-	proyUmic(j).impPago = planPagos(n).impPago
		 * 						-	proyUmic(j).fecPago o proyUmic(j).fechaDevengo = FIN DE MES(planPagos(n).FechaPago)
		 * 				o	Si NO existe pago para el periodo tratado:
		 * 						-	proyUmic(j).impPago = 0
		 * 						-	proyUmic(j).fechaPago o proyUmic(j).fechaDevego = null
		 */
		
		if (ConstantsModulos.CTE_TIPO_PRES_VIT.equals(umic.getRentas().getTempVit()) || (ConstantsModulos.CTE_TIPO_PRES_TEM.equals(umic.getRentas().getTempVit()) && (null == lstPagosPlani || lstPagosPlani.isEmpty())) ) {
			if (!inipg) {
				detalleActual.setImpPago(BigDecimal.ZERO);
			}
			int index = -1;
			if ((ConstantsModulos.CTE_VAL_PLANI.equals(criFecPagoDeve)  || ConstantsModulos.CTE_VAL_PVIDA.equals(criFecPagoDeve) || ConstantsModulos.CTE_VAL_PLANB.equals(criFecPagoDeve) || ConstantsModulos.CTE_VAL_PLAIN.equals(criFecPagoDeve)) && !lstPlanPagos.isEmpty()) {
				// Para PLANI se deja la fecha de pago en la fecha que esté y NO se lleva la fecha de pago al ultimo dia de mes
				index = Collections.binarySearch(lstPlanPagos, null, new Comparator<PlanPagos>(){

					@Override
					public int compare(PlanPagos arg0, PlanPagos arg1) {
						final Timestamp fecPago = arg0.getFecPago(); 
						if((!fecPago.before(detalleActual.getFechaDesde()) && fecPago.before(detalleActual.getFechaHasta())) ||
							    (( UtilFechas.getDia(fecPago) == ConstantsFunciones.CTE_29 ) && (fecPago.before(detalleActual.getFechaDesde()) && !fecPago.before(detalleActual.getFechaHasta())))){
							return 0;							
						} else if(fecPago.before(detalleActual.getFechaDesde())) {
							return -1;
						} else {
							return 1;
						}
					}
					
				});
				if(index >= 0) {
					fechaPagoDev = lstPlanPagos.get(index).getFecPago();
					detalleActual.setImpPago(lstPlanPagos.get(index).getImpPago());
				}
			} else if (ConstantsModulos.CTE_VAL_EFTEC.equals(criFecPagoDeve) || ConstantsModulos.CTE_VAL_EFTIN.equals(criFecPagoDeve)) {
				// Para EFTEC se lleva la fecha de pago al ultimo dia de mes
				index = Collections.binarySearch(lstPlanPagos, null, new Comparator<PlanPagos>(){

					@Override
					public int compare(PlanPagos arg0, PlanPagos arg1) {
						final Timestamp fecPago = UtilFechas.getUltimoDiaDelMes(arg0.getFecPago()); 
						if(!fecPago.before(detalleActual.getFechaDesde()) && fecPago.before(detalleActual.getFechaHasta())) {
							return 0;							
						} else if(fecPago.before(detalleActual.getFechaDesde())) {
							return -1;
						} else {
							return 1;
						}
					}					
				});
				if(index >= 0) {
					fechaPagoDev = UtilFechas.getUltimoDiaDelMes(lstPlanPagos.get(index).getFecPago());				
					detalleActual.setImpPago(lstPlanPagos.get(index).getImpPago());
				}
			} else if ((ConstantsModulos.CTE_VAL_PLANC.equals(criFecPagoDeve)) && !lstPlanPagos.isEmpty()) {
				// Para PLANI se deja la fecha de pago en la fecha que esté y NO se lleva la fecha de pago al ultimo dia de mes
				index = Collections.binarySearch(lstPlanPagos, null, new Comparator<PlanPagos>(){

					@Override
					public int compare(PlanPagos arg0, PlanPagos arg1) {
						final Timestamp fecPago = arg0.getFecPago(); 
						if((!fecPago.before(detalleActual.getFechaDesde()) && fecPago.before(detalleActual.getFechaHasta())) ||
								fecPago.equals(detalleActual.getFechaHasta()) ||
							    (( UtilFechas.getDia(fecPago) == ConstantsFunciones.CTE_29 ) && (fecPago.before(detalleActual.getFechaDesde()) && !fecPago.before(detalleActual.getFechaHasta())))){
							return 0;							
						} else if(fecPago.before(detalleActual.getFechaDesde())) {
							return -1;
						} else {
							return 1;
						}
					}
					
				});
				if(index >= 0) {
					fechaPagoDev = lstPlanPagos.get(index).getFecPago();
					detalleActual.setImpPago(lstPlanPagos.get(index).getImpPago());
				}
			}			
		} else if (ConstantsModulos.CTE_TIPO_PRES_LOCA.equals(umic.getRentas().getTempVit()) || (ConstantsModulos.CTE_TIPO_PRES_TEM.equals(umic.getRentas().getTempVit()) && (null != lstPagosPlani || !(lstPagosPlani.isEmpty())))) {
			/**
			 * -	Si umic.rentas.tempVit = ‘T’, se leerán  los pagos de la umic anteriormente recuperados en obtenerDatos.recuperarPagosPlanificados(pagosPlanificados).
			 * 		Para cada periodo j que se está procesando se buscará  en la estructura de pagos planificados, el pago correspondiente al periodo tratado de forma que:
			 * 		
			 * 		- Si CriterioFecha = PLANI:
			 * 			o	Si existe pago para el periodo tratado, es decir si:
			 * 				proyUmic(j).fechaDesde <= pagosPlanificados(n).fecpago < proyUmic(j).fechaHasta, se hará:
			 * 				-	Si pagosPlanificados(n).fecpago >= Umic.rentas.fecini
			 * 					-	proyUmic(j).impPago = pagosPlanificados(n).eplreaNeto
			 * 					-	proyUmic(j).fechaPago o proyUmic(j).fechaDevego = pagosPlanificados(n).fplreaEfecto
			 * 				-	Si pagosPlanificados(n).fecpago < Umic.rentas.fecini
			 * 					-	proyUmic(j).impPago = 0
			 * 					-	proyUmic(j).fechaPago o proyUmic(j).fechaDevego = null
			 * 			o	Si NO existe pago para el periodo tratado
			 * 				-	proyUmic(j).impPago = 0
			 * 				-	proyUmic(j).fechaPago o proyUmic(j).fechaDevego = null
			 * 
			 * 		- Si CriterioFecha = EFTEC:
			 * 			o	Si existe pago para el periodo tratado, es decir si:
			 * 				proyUmic(j).fechaDesde <= FIN DE MES(pagosPlanificados(n).fecpago) < proyUmic(j).fechaHasta, se hará:
			 * 				-	Si pagosPlanificados(n).fecpago >= Umic.rentas.fecini
			 * 					-	proyUmic(j).impPago = pagosPlanificados(n).eplreaNeto
			 * 					-	proyUmic(j).fechaPago o proyUmic(j).fechaDevego = FIN DE MES(pagosPlanificados(n).fplreaEfecto)
			 * 				-	Si pagosPlanificados(n).fecpago < Umic.rentas.fecini
			 * 					-	proyUmic(j).impPago = 0
			 * 					-	proyUmic(j).fechaPago o proyUmic(j).fechaDevego = null
			 * 			o	Si NO existe pago para el periodo tratado
			 * 				-	proyUmic(j).impPago = 0
			 * 				-	proyUmic(j).fechaPago o proyUmic(j).fechaDevego = null
			 */
			detalleActual.setImpPago(BigDecimal.ZERO);
			int index = -1;
			if (ConstantsModulos.CTE_VAL_PLANI.equals(criFecPagoDeve) || ConstantsModulos.CTE_VAL_PVIDA.equals(criFecPagoDeve) || ConstantsModulos.CTE_VAL_PLANB.equals(criFecPagoDeve) || ConstantsModulos.CTE_VAL_PLAIN.equals(criFecPagoDeve)) {
				// Para PLANI se deja la fecha de pago en la fecha que este y NO se lleva la fecha de pago al ultimo dia de mes
				index = Collections.binarySearch(lstPagosPlani, null, new Comparator<PagosPlanificados>(){

					@Override
					public int compare(PagosPlanificados arg0, PagosPlanificados arg1) {
						final Timestamp fecPago = arg0.getFplreaEfecto();
						if((!fecPago.before(detalleActual.getFechaDesde()) && fecPago.before(detalleActual.getFechaHasta())) || (detalleActual.getFechaHasta().equals(umic.getFechas().getFecefecfin()) && detalleActual.getFechaHasta().equals(fecPago))) {
							return 0;							
						} else if(fecPago.before(detalleActual.getFechaDesde())) {
							return -1;
						} else {
							return 1;
						}
					}
					
				});
				if(index >= 0) {
					fechaPagoDev = lstPagosPlani.get(index).getFplreaEfecto();
					detalleActual.setImpPago(lstPagosPlani.get(index).getEplreaBruto());
				}
			} else if (ConstantsModulos.CTE_VAL_EFTEC.equals(criFecPagoDeve) || ConstantsModulos.CTE_VAL_EFTIN.equals(criFecPagoDeve)) {
				// Para EFTEC se lleva la fecha de pago al ultimo dia de mes
				index = Collections.binarySearch(lstPlanPagos, null, new Comparator<PlanPagos>(){

					@Override
					public int compare(PlanPagos arg0, PlanPagos arg1) {
						final Timestamp fecPago = UtilFechas.getUltimoDiaDelMes(arg0.getFecPago()); 
						if(!fecPago.before(detalleActual.getFechaDesde()) && fecPago.before(detalleActual.getFechaHasta())) {
							return 0;							
						} else if(fecPago.before(detalleActual.getFechaDesde())) {
							return -1;
						} else {
							return 1;
						}
					}					
				});
				if(index >= 0) {
					fechaPagoDev = UtilFechas.getUltimoDiaDelMes(lstPlanPagos.get(index).getFecPago());				
					detalleActual.setImpPago(lstPlanPagos.get(index).getImpPago());
				}
			} else if ((ConstantsModulos.CTE_VAL_PLANC.equals(criFecPagoDeve)) && !lstPlanPagos.isEmpty()) {
				// Para PLANI se deja la fecha de pago en la fecha que esté y NO se lleva la fecha de pago al ultimo dia de mes
				index = Collections.binarySearch(lstPlanPagos, null, new Comparator<PlanPagos>(){

					@Override
					public int compare(PlanPagos arg0, PlanPagos arg1) {
						final Timestamp fecPago = arg0.getFecPago(); 
						if((!fecPago.before(detalleActual.getFechaDesde()) && fecPago.before(detalleActual.getFechaHasta())) ||
								fecPago.equals(detalleActual.getFechaHasta()) ||
							    (( UtilFechas.getDia(fecPago) == ConstantsFunciones.CTE_29 ) && (fecPago.before(detalleActual.getFechaDesde()) && !fecPago.before(detalleActual.getFechaHasta())))){
							return 0;							
						} else if(fecPago.before(detalleActual.getFechaDesde())) {
							return -1;
						} else {
							return 1;
						}
					}
					
				});
				if(index >= 0) {
					fechaPagoDev = lstPlanPagos.get(index).getFecPago();
					detalleActual.setImpPago(lstPlanPagos.get(index).getImpPago());
				}
			}										
		}
		
		if (UtilModulos.LOG.isTraceEnabled()) {
			UtilModulos.LOG.trace("Fin función << obtenerFechaPagoDevengoCriterioPLANI >> de la clase UtilModulos, con resultado fechaPagoDev = {}", fechaPagoDev);
		}
		return fechaPagoDev;
	}
	
	/** Función encargada de obtener la fecha con el criterio INIP
	 * 
	 * @param iteracion
	 * @param fcalc
	 * @param fechaDesde
	 * @param fecinisus
	 * @return resultado
	 */
	public static Timestamp obtenerFechaPagoDevengoCriterioINIP(final Integer iteracion, final Timestamp fcalc, final Timestamp fechaDesde, final Timestamp fecefecini) {
		//Variables locales
		Timestamp fechaPago;
		//Fin variables locales
		
		// Para que se calcule la fecha en el primer periodo, deben coincidir el dia de la fecha de calculo y el dia de aniversario de la póliza
		if (iteracion.equals(ConstantsFunciones.CTE_FIRST_ITER) && UtilFechas.getDia(fcalc) != UtilFechas.getDia(fecefecini)) {
			fechaPago = null;
		} else {
			fechaPago = fechaDesde;
		}
		
		return fechaPago;
	}
	
	/**
	 * Funcion que realiza los calculos necesarios para obtener la fecha de pago o devengo si el criterio de la fecha es "Efecto de renovacion"
	 * 
	 * @param detalleActual Elemento j de la estructura DetalleCorriente
	 * @param umicFechas Bloque de datos fechas de la umic
	 * @param iteracion iteracion por la que vamos
	 * @param mapVariables mapa con las variables de memoria necesarias
	 * @param fcalc fecha de cálculo
	 * 				
	 * @return resultadoFecha
	 */
	private static Timestamp obtenerFechaPagoDevengoCasoEfectoRenovacion(final DetalleCorriente detalleActual, final Umic umic, final Integer iteracion, final Map<String, Object> mapVariables, final String variableMemoria, final Timestamp fcalc) {
		//Variables locales
		Timestamp wFechaRenov = null;
		Timestamp varFechaEfecto = null;
		//Fin variables locales
				
		/**
		 * Si CriterioFecha (de pago   y/ó de devengo) es RENOV (Efecto de Renovación), se calculará la fecha correspondiente (de pago y/ó devengo) como sigue.
	
			Se evaluarán los datos de la umic necesarios para el cálculo. 
			Establecemos una variable interna para ir calculando las fechas de renovación hasta el vencimiento de la umic, o lo que es lo mismo, 
			hasta el último de los periodos de proyección recibidos.
		 */
		
		if (UtilModulos.LOG.isTraceEnabled()) {
			UtilModulos.LOG.trace("Inicio de la funcion << obtenerFechaPagoDevengoCasoEfectoRenovacion >> de la clase UtilModulos");
		}
		
		// Si umic.primas.cformpago = 9 (pago único), no se calcualrán fechas de pago y devengo, terminando así el programa para la umic.
		if(!umic.getPrimas().getCformpago().equals(ConstantsModulos.CTE_CFORMPAG_UNICA)) {
			wFechaRenov = UtilModulos.getWFechaRenov(mapVariables, variableMemoria, null);
			
			if (umic.getDatosGenerales().getCnegocio().equals(ConstantesSolvencia.NEGOCIO_INDIVIDUAL)){
				varFechaEfecto = umic.getFechas().getFecefecini();
			} else {
				varFechaEfecto = umic.getFechas().getFecinisus();
			}

			if(wFechaRenov == null) {
				UtilFechas.Fecha fcalcu = UtilFechas.getFecha(fcalc);
				
				UtilFechas.Fecha wFechaRenovu = UtilFechas.getFecha(varFechaEfecto);
				
				if(wFechaRenovu.getMes() > fcalcu.getMes() || (wFechaRenovu.getMes() == fcalcu.getMes() && wFechaRenovu.getDia() > fcalcu.getDia())) {
					wFechaRenovu.setAnio(fcalcu.getAnio() - 1);
				} else {
					wFechaRenovu.setAnio(fcalcu.getAnio());
				}
				
				wFechaRenov = wFechaRenovu.toTimestamp();
				UtilModulos.setWFechaRenovacion(mapVariables, variableMemoria, wFechaRenov);
			}
			
			/**
			 * Si Umic.fechas.fecdesderenova no es del tipo 29/02/xxxx:
			 * 		wFechaRenov = Umic.fechas.fecdesderenova
			 * 		
			 * 		- Si proyUmic(1).fecDesde y proyUmic(1).fecHasta > wFechaRenov ->
			 * 			- Se establecerá la fecha de la siguiente renovación en función de la forma de pago de las primas (umic.primas.cformpago)
			 * 			y de la fecha de renovación actual wFechaRenov:
			 *				umic.primas.cformpago (periodicidad)	varNumPagos (numero de pagos anuales)
			 *					1  (anual)								1
			 *					2  (semestral)							2
			 *					3  (trimestral)							4
			 *					4  (mensual)							12
			 * 			- Se compondrá la fecha de siguiente forma:
			 * 				Dia(Umic.fechas.fecdesderenova) / Mes(add(wFechaRenov.MONTH,12/(varNumPagos))) / Año(add(wFechaRenov.MONTH,12/(varNumPagos)))
			 * 				Si la fecha calculada es del tipo 29/02/XXXX, se llevará a 28/02/XXXX
			 * 					Si proyUmic(j).fecDesde y proyUmic(j).fecHasta < wFechaRenov ->
			 * 						proyUmic(j).fechaPago = null
			 * 						y/ó
			 * 						proyUmic(j).fechaDevengo = null
			 * 		
			 * 		- Si proyUmic(j).fecDesde y proyUmic(j).fecHasta < wFechaRenov -> periodos anteriores a la próxima renovación, entonces:
			 * 			proyUmic(j).fechaPago = null
			 * 			y/ó
			 * 			proyUmic(j).fechaDevengo = null
			 * 		
			 * 		- Si proyUmic(j).fecDesde <= wFechaRenov < proyUmic(j).fecHasta -> la fecha de renovación está dentro del periodo j, entonces:
			 * 			proyUmic(j).fecPago = wFechaRenov
			 * 			y/ó
			 * 			proyUmic(j).fecDevengo = wFechaRenov
			 * 			
			 * 			- Si wFechaRenov < proyUmic.last(fechasta) se establecerá la fecha de la siguiente renovación en función de la forma de pago
			 * 			de las primas (umic.primas.cformpago) y de la fecha de renovación actual wFechaRenov:
			 * 				umic.primas.cformpago (periodicidad)	varNumPagos (numero de pagos anuales)
			 * 					1  (anual)								1
			 * 					2  (semestral)							2
			 * 					3  (trimestral)							4
			 * 					4  (mensual)							12
			 * 			- Se compondrá la fecha  de siguiente forma:
			 * 				wFechaRenov = Dia(Umic.fechas.fecdesderenova) / Mes(add(wFechaRenov.MONTH,12/(varNumPagos))) / Año(add(wFechaRenov.MONTH,12/(varNumPagos)))
			 * 				Si la fecha calculada es del tipo 29/02/XXXX, se llevará a 28/02/XXXX
			 * 				Se continúan procesando los periodos hasta el final de los mismos.
			 */
			
			// Si la fecha desde y la fecha hasta de proyUmic(1) son mayores que wFechaRenov
			// Se busca la primera fecha de renovación a partir de la fecInisus que cae en el periodo si es que existe (será la primera que cumpla que sea < fecHasta y además >= fecDesde)
			while(wFechaRenov.before(detalleActual.getFechaDesde())) {
				wFechaRenov = UtilFechas.incrMeses(wFechaRenov, varFechaEfecto, ConstantsFunciones.CTE_12 / ConstantsFunciones.FORPAGRENT.get(Integer.valueOf(umic.getPrimas().getCformpago())), false); 
			}

			UtilModulos.setWFechaRenovacion(mapVariables, variableMemoria, wFechaRenov);
			
			if (!detalleActual.getFechaHasta().after(wFechaRenov)) {
				wFechaRenov = null;
			}

			
//			if (detalleActual.getFechaDesde().after(wFechaRenov)) {
//				
//				UtilFechas.Fecha fecinisus = UtilFechas.getFecha(umic.getFechas().getFecinisus());
//				int diaFecInisus = fecinisus.getDia();
//				
//				UtilModulos.setWFechaRenovacion(mapVariables, variableMemoria, UtilFechas.incrMeses(wFechaRenov, umic.getFechas().getFecinisus(), ConstantsFunciones.CTE_12 / ConstantsFunciones.FORPAGRENT.get(Integer.valueOf(umic.getPrimas().getCformpago())), false));
//				
//				// Para que se calcule la fecha en el primer periodo, deben coincidir el dia de la fecha de calculo y el dia de aniversario de la póliza
//				if (iteracion.equals(ConstantsFunciones.CTE_FIRST_ITER) && UtilFechas.getDia(fcalc) != diaFecInisus) {
//					wFechaRenov = null;
//				}
//			} else if (detalleActual.getFechaDesde().before(wFechaRenov) && !wFechaRenov.before(detalleActual.getFechaHasta())) {
//				wFechaRenov = null;
//			} else if (!detalleActual.getFechaDesde().after(wFechaRenov) && wFechaRenov.before(detalleActual.getFechaHasta())) {
//				UtilModulos.setWFechaRenovacion(mapVariables, variableMemoria, UtilFechas.incrMeses(wFechaRenov, umic.getFechas().getFecinisus(), ConstantsFunciones.CTE_12 / ConstantsFunciones.FORPAGRENT.get(Integer.valueOf(umic.getPrimas().getCformpago())), false));
//			}
		}
		
		if (UtilModulos.LOG.isTraceEnabled()) {
			UtilModulos.LOG.trace("Fin de la funcion << obtenerFechaPagoDevengoCasoEfectoRenovacion >> de la clase UtilModulos, con resultado wFechaRenov = {}", wFechaRenov);
		}
		
		return wFechaRenov;
	}
	
	
	/**
     * Funcion que realiza los calculos necesarios para obtener la fecha de pago o devengo si el criterio de la fecha es "Reno2"
     * 
     * @param detalleActual Elemento j de la estructura DetalleCorriente
     * @param umicFechas Bloque de datos fechas de la umic
     * @param iteracion iteracion por la que vamos
     * @param mapVariables mapa con las variables de memoria necesarias
     * @param fcalc fecha de cálculo
     *                 
     * @return resultadoFecha
     */
    private static Timestamp obtenerFechaPagoDevengoCasoEfectoReno2(final DetalleCorriente detalleActual, final Umic umic, final Integer iteracion, final Map<String, Object> mapVariables, final String variableMemoria, final Timestamp fcalc) {
        //Variables locales
        Timestamp wFechaRenov = null;
        Timestamp varFechaEfecto = null;
        //Fin variables locales

        /**
         * Si CriterioFecha (de pago   y/ó de devengo) es RENOV (Efecto de Renovación), se calculará la fecha correspondiente (de pago y/ó devengo) como sigue.

            Se evaluarán los datos de la umic necesarios para el cálculo. 
            Establecemos una variable interna para ir calculando las fechas de renovación hasta el vencimiento de la umic, o lo que es lo mismo, 
            hasta el último de los periodos de proyección recibidos.
         */

        if (UtilModulos.LOG.isTraceEnabled()) {
            UtilModulos.LOG.trace("Inicio de la funcion << obtenerFechaPagoDevengoCasoEfectoRenovacion2 >> de la clase UtilModulos");
        }

        // Si umic.primas.cformpago = 9 (pago único), no se calcualrán fechas de pago y devengo, terminando así el programa para la umic.
        if(!umic.getPrimas().getCformpago().equals(ConstantsModulos.CTE_CFORMPAG_UNICA)) {
            wFechaRenov = UtilModulos.getWFechaRenov(mapVariables, variableMemoria, null);

            if (umic.getDatosGenerales().getCnegocio().equals(ConstantesSolvencia.NEGOCIO_INDIVIDUAL)){
                varFechaEfecto = umic.getFechas().getFecefecini();
            } else {
                varFechaEfecto = umic.getFechas().getFecinisus();
            }

 

            if(wFechaRenov == null) {
                UtilFechas.Fecha fcalcu = UtilFechas.getFecha(fcalc);

                UtilFechas.Fecha wFechaRenovu = UtilFechas.getFecha(varFechaEfecto);

                if(wFechaRenovu.getMes() > fcalcu.getMes() || (wFechaRenovu.getMes() == fcalcu.getMes() && wFechaRenovu.getDia() > fcalcu.getDia())) {
                    wFechaRenovu.setAnio(fcalcu.getAnio() - 1);
                } else {
                    wFechaRenovu.setAnio(fcalcu.getAnio());
                }

                wFechaRenov = wFechaRenovu.toTimestamp();
                UtilModulos.setWFechaRenovacion(mapVariables, variableMemoria, wFechaRenov);
            }

            /**
             * Si Umic.fechas.fecdesderenova no es del tipo 29/02/xxxx:
             *         wFechaRenov = Umic.fechas.fecdesderenova
             *         
             *         - Si proyUmic(1).fecDesde y proyUmic(1).fecHasta > wFechaRenov ->
             *             - Se establecerá la fecha de la siguiente renovación en función de la forma de pago de las primas (umic.primas.cformpago)
             *             y de la fecha de renovación actual wFechaRenov:
             *                umic.primas.cformpago (periodicidad)    varNumPagos (numero de pagos anuales)
             *                    1  (anual)                                1
             *                    2  (semestral)                            2
             *                    3  (trimestral)                            4
             *                    4  (mensual)                            12
             *             - Se compondrá la fecha de siguiente forma:
             *                 Dia(Umic.fechas.fecdesderenova) / Mes(add(wFechaRenov.MONTH,12/(varNumPagos))) / Año(add(wFechaRenov.MONTH,12/(varNumPagos)))
             *                 Si la fecha calculada es del tipo 29/02/XXXX, se llevará a 28/02/XXXX
             *                     Si proyUmic(j).fecDesde y proyUmic(j).fecHasta < wFechaRenov ->
             *                         proyUmic(j).fechaPago = null
             *                         y/ó
             *                         proyUmic(j).fechaDevengo = null
             *         
             *         - Si proyUmic(j).fecDesde y proyUmic(j).fecHasta < wFechaRenov -> periodos anteriores a la próxima renovación, entonces:
             *             proyUmic(j).fechaPago = null
             *             y/ó
             *             proyUmic(j).fechaDevengo = null
             *         
             *         - Si proyUmic(j).fecDesde <= wFechaRenov < proyUmic(j).fecHasta -> la fecha de renovación está dentro del periodo j, entonces:
             *             proyUmic(j).fecPago = wFechaRenov
             *             y/ó
             *             proyUmic(j).fecDevengo = wFechaRenov
             *             
             *             - Si wFechaRenov < proyUmic.last(fechasta) se establecerá la fecha de la siguiente renovación en función de la forma de pago
             *             de las primas (umic.primas.cformpago) y de la fecha de renovación actual wFechaRenov:
             *                 umic.primas.cformpago (periodicidad)    varNumPagos (numero de pagos anuales)
             *                     1  (anual)                                1
             *                     2  (semestral)                            2
             *                     3  (trimestral)                            4
             *                     4  (mensual)                            12
             *             - Se compondrá la fecha  de siguiente forma:
             *                 wFechaRenov = Dia(Umic.fechas.fecdesderenova) / Mes(add(wFechaRenov.MONTH,12/(varNumPagos))) / Año(add(wFechaRenov.MONTH,12/(varNumPagos)))
             *                 Si la fecha calculada es del tipo 29/02/XXXX, se llevará a 28/02/XXXX
             *                 Se continúan procesando los periodos hasta el final de los mismos.
             */

            // Si la fecha desde y la fecha hasta de proyUmic(1) son mayores que wFechaRenov
            // Se busca la primera fecha de renovación a partir de la fecInisus que cae en el periodo si es que existe (será la primera que cumpla que sea < fecHasta y además >= fecDesde)
//            while(wFechaRenov.before(detalleActual.getFechaDesde())) {
//                wFechaRenov = UtilFechas.incrMeses(wFechaRenov, varFechaEfecto, ConstantsFunciones.CTE_12 / ConstantsFunciones.FORPAGRENT.get(Integer.valueOf(umic.getPrimas().getCformpago())), false); 
//            }


            if (umic.getPrimas().getCformpago().equals("4")) {
                while(!wFechaRenov.after(detalleActual.getFechaDesde())
                		&& detalleActual.getFechaHasta().after(wFechaRenov)) {
                    wFechaRenov = UtilFechas.incrMeses(wFechaRenov, varFechaEfecto, ConstantsFunciones.CTE_12 / ConstantsFunciones.FORPAGRENT.get(Integer.valueOf(umic.getPrimas().getCformpago())), false); 
                }

            } else {
                while(wFechaRenov.before(detalleActual.getFechaDesde())) {
                    wFechaRenov = UtilFechas.incrMeses(wFechaRenov, varFechaEfecto, ConstantsFunciones.CTE_12 / ConstantsFunciones.FORPAGRENT.get(Integer.valueOf(umic.getPrimas().getCformpago())), false); 
                }

                if (!detalleActual.getFechaHasta().after(wFechaRenov)) {
                    wFechaRenov = null;
                }
            }

            if (wFechaRenov != null 
            		&& !wFechaRenov.before(umic.getFechas().getFecefecfin())) {
                wFechaRenov = null;
            }

 

            UtilModulos.setWFechaRenovacion(mapVariables, variableMemoria, wFechaRenov);

 

        }

        if (UtilModulos.LOG.isTraceEnabled()) {
            UtilModulos.LOG.trace("Fin de la funcion << obtenerFechaPagoDevengoCasoEfectoRenovacion2 >> de la clase UtilModulos, con resultado wFechaRenov = {}", wFechaRenov);
        }

        return wFechaRenov;
    }
	

	/**
	 * Función encargada de obtener la duración de un determinado codigo en función de la constante por la que empieze dicho codigo
	 * 
	 * @param codKX
	 * @param fecinisus
	 * @param fechaDevengo
	 * @param ndurprima
	 * @param ndursegmes
	 * @return
	 */
	public static Integer obtenerDuracionCodKX(final String codKX, final Timestamp fecinisus, final Timestamp fechaDevengo, final Integer ndurprima, final Integer ndursegmes) {
		//Variables locales
		Integer durkx = 0;
		//Fin variables locales
		if (UtilModulos.LOG.isTraceEnabled()) {
			UtilModulos.LOG.trace("Inicio de la función << obtenerDuracionCodKX >> de la clase UtilModulos");
		}
		if (null != codKX) {
			if (codKX.startsWith(ConstantsModulos.CTE_CODK_KT) || codKX.startsWith(ConstantsModulos.CTE_CODK_KS)) {
				durkx = FuncionesAuxiliares.tcm(fecinisus, fechaDevengo);
			} else if (codKX.startsWith(ConstantsModulos.CTE_CODK_KC) || codKX.startsWith(ConstantsModulos.CTE_CODK_KF)) {
				durkx = ConstantsFunciones.CTE_9999;
			} else if (codKX.startsWith(ConstantsModulos.CTE_CODK_KM)) {
				durkx = ndurprima / ConstantsFunciones.CTE_12;
			} else if (codKX.startsWith(ConstantsModulos.CTE_CODK_KN)) {
				durkx = ndursegmes;
			}
		}
		
		if (UtilModulos.LOG.isTraceEnabled()) {
			UtilModulos.LOG.trace("Fin de la función << obtenerDuracionCodKX >> de la clase UtilModulos");
		}
		
		return durkx;
	}
	
	/**
	 * Función encargada de localizar el pintertecnIX del tramo X correspondiente siempre que se cumplan las siguientes condiciones:
	 * 			1 - La fecha de inicio del tramo X sea menor o igual que la fecha desde del periodo analizado
	 * 			2 - La fecha de fin del tramo X sea mayor o igual que la fecha hasta del periodo analizado
	 * 			3 - El indicador swcasadoIX del tramo X sea igual a 'N' 
	 * 
	 * @param btiUmic Base tecnica incial de la umic
	 * @param fechaDesde Fecha inicio del periodo
	 * @param fechaHasta Fecha fin del periodo
	 * @return resultado
	 */
	public static BigDecimal buscarItcalcX(final DetalleBaseTecnica btcUmic, final Timestamp fechaDesde, final Timestamp fechaHasta, final String casado) {
		//Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		boolean noEncontrado = true;
		final Timestamp fecIniTramo[] = {btcUmic.getFecInitramo().get(0), btcUmic.getFecInitramo().get(1), btcUmic.getFecInitramo().get(2), btcUmic.getFecInitramo().get(3), btcUmic.getFecInitramo().get(4)};
		final Timestamp fecFinTramo[] = {btcUmic.getFecfintramo().get(0), btcUmic.getFecfintramo().get(1), btcUmic.getFecfintramo().get(2), btcUmic.getFecfintramo().get(3), btcUmic.getFecfintramo().get(4)};
		final String casadoX[] = {btcUmic.getSwcasado().get(0), btcUmic.getSwcasado().get(1), btcUmic.getSwcasado().get(2), btcUmic.getSwcasado().get(3), btcUmic.getSwcasado().get(4)};
		final BigDecimal itecalX[] = {btcUmic.getItcalc().get(0), btcUmic.getItcalc().get(1), btcUmic.getItcalc().get(2), btcUmic.getItcalc().get(3), btcUmic.getItcalc().get(4)};
		int longitudVector = 0;
		//Fin variables locales
		
		if (UtilModulos.LOG.isTraceEnabled()) {
			UtilModulos.LOG.trace("Inicio de la función << buscarPintertecnTramoX >> de la clase UtilModulos");
		}
		
		longitudVector = fecIniTramo.length;
		
		/**
		 * Desde i=0, hasta 4, vamos obteniendo la diferencia en dias entre la fechaIniTramoX y la fecha desde y por otro lado
		 * la diferencia entre la fechaFinTramoX y la fecha hasta 
		 */
		for (int i = 0; i < longitudVector && noEncontrado; i++) {
			
			if (null != fecIniTramo[i] && null != fecFinTramo[i]) {				
				// Si es null, entra siempre, sino, solo entra en caso de que el campo coincida con el flag 
				final boolean condicionCasado = casado == null || casado.equals(casadoX[i]);
				
				//Si se cumple la condicion establecida para el tramoX obtenemos el pintertecnX
				if (!fechaDesde.before(fecIniTramo[i]) && !fecFinTramo[i].before(fechaHasta) && condicionCasado) {
					resultado = itecalX[i];
					noEncontrado = false;
				}
			}
		
		}
		
		if (UtilModulos.LOG.isTraceEnabled()) {
			UtilModulos.LOG.trace("Fin de la función << buscarPintertecnTramoX >> de la clase UtilModulos, con resultado = {}", resultado);
		}
		
		return resultado;
	}

	/**
	 * Devuelve el valor del número de anualidades. Si no está almacenado lo calcula, lo setea en el HASHMAP y lo devuelve.
	 * @param mapVariables
	 * @param claveNumAnual
	 * @param iteracion
	 * @param fcalc
	 * @param fechaPago
	 * @param varCriFec
	 * @return
	 */
	public static Integer getVarNumAnualidadesPri003(final Map<String, Object> mapVariables, final String claveNumAnual, final Integer iteracion, final Integer varTC, final Timestamp fcalc, final Timestamp fechaPago, final String varCriFec) {
		final String clave = claveNumAnual + iteracion;
		final Object preSalida = mapVariables.get(clave);
		Integer salida = null;
		
		if (null == preSalida) {
			salida = varTC + FuncionesAuxiliares.nAnnos(fcalc, fechaPago, varCriFec).intValue();
			mapVariables.put(clave, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveNumAnual + " para la iteración " + iteracion + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (Integer)preSalida;
		}

		return salida;
	}
		
	/**
	 * Devuelve varTCM. Si no está almacenada la calcula, la setea y la devuelve.
	 * @param mapVariables
	 * @param modulo
	 * @param iteracion
	 * @param constanteVariable
	 * @param varTCm
	 * @return
	 */
	public static Integer getVarTCm(final Map<String, Object> mapVariables, final String claveVariable, final Integer iteracion, final Timestamp fecinisus, final Timestamp fcalc) {
		final String clave = claveVariable + iteracion;
		final Object preSalida = mapVariables.get(clave);
		Integer salida = null;
			
		if (null == preSalida) {
			salida = FuncionesAuxiliares.tcm(fecinisus, fcalc);
			mapVariables.put(clave, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " para la iteración " + iteracion + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (Integer)preSalida;
		}

		return salida;
	}
	
	/**
	 * @param mapVariables
	 * @param claveVariable
	 * @param fecinisus
	 * @param fcalc
	 * @return
	 */
	public static Integer getVarTCmAPTOTCATC(final Map<String, Object> mapVariables, final String claveVariable, final Timestamp fecinisus, final Timestamp fcalc) {
		final Object preSalida = mapVariables.get(claveVariable);
		Integer salida = null;
		
		if (null == preSalida) {
			UtilFechas.Fecha fcalcu = UtilFechas.getFecha(fcalc);
			UtilFechas.Fecha fecinisusu = UtilFechas.getFecha(fecinisus);
			salida = Math.abs((fcalcu.getAnio() - fecinisusu.getAnio())) * ConstantsFunciones.CTE_12 +
					fcalcu.getMes() - fecinisusu.getMes();
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (Integer)preSalida;
		}

		return salida;
	}
	
	/**
	 * Devuelve varTC si está en memoria. Si no está en memoria la calcula, la asigna a memoria y la devuelve. 
	 * @param mapVariables
	 * @param modulo
	 * @param constanteVariable
	 * @param varFechaEfecto
	 * @param fcalc
	 * @return
	 */
	public static Integer getVarTC(final Map<String, Object> mapVariables, final String claveVariable, final Timestamp varFechaEfecto, final Timestamp fcalc) {
		final Object preSalida = mapVariables.get(claveVariable);
		Integer salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = FuncionesAuxiliares.tc(varFechaEfecto, fcalc);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (Integer)preSalida;
		}

		return salida;
	}
	
	/**
	 * Devuelve varTC0. Si no está almacenado lo calcula, lo almacena y lo devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param fecha
	 * @param fcalc
	 * @return
	 */
	public static Integer getVarTC0(final Map<String, Object> mapVariables, final String claveVariable, final Timestamp fecha, final Timestamp fcalc) {
		final Object preSalida = mapVariables.get(claveVariable);
		Integer salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = FuncionesAuxiliares.tc(fecha, fcalc);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (Integer) preSalida;
		}

		return salida;
	}
	
	/**
	 * Devuelve varL. Si no está almacenada la calcula, la almacena y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param iteracion
	 * @param fecinisus
	 * @param fecFinTramo1
	 * @param varCriterFecha
	 * @return
	 */
	public static Integer getVarL(final Map<String, Object> mapVariables, final String claveVariable, final Integer iteracion,
			final Timestamp fecinisus, final Timestamp fecFinTramo1, final String varCriterFecha) {
		final String clave = claveVariable + iteracion;
		final Object preSalida = mapVariables.get(clave);
		Integer salida = null;
		
		if (null == preSalida) {
			salida = FuncionesAuxiliares.nAnnos(fecinisus, fecFinTramo1, varCriterFecha).intValue();
			mapVariables.put(clave, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + clave + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (Integer) preSalida;
		}

		return salida;
		
	}
	
	/**
	 * Devuelve varLInteger. Si no está almacenada la calcula, la almacena y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param iteracion
	 * @param fecinisus
	 * @param fecfintramoElemCero
	 * @param varCriFec
	 * @return
	 */
	public static BigDecimal getVarLInteger(final Map<String, Object> mapVariables, final String claveVariable, final Integer iteracion, 
			final Timestamp fecinisus, final Timestamp fecfintramoElemCero, final String varCriFec) {
		final String clave = claveVariable + iteracion;
		final Object preSalida = mapVariables.get(clave);
		BigDecimal salida = null;
		
		if (null == preSalida) {
			salida = FuncionesAuxiliares.nAnnos(fecinisus, fecfintramoElemCero, varCriFec);
			mapVariables.put(clave, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + clave + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal) preSalida;
		}

		return salida;
		
	}
	
	/**
	 * Devuelve varLzc para el módulo FPTOZC. Si no está almacenado lo calcula, lo almacena y lo devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param varLzc
	 * @return
	 */
	public static BigDecimal setVarLzcFPTOZC(final Map<String, Object> mapVariables, final String claveVariable, final BigDecimal varLzc) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = varLzc;
			mapVariables.put(claveVariable, salida);
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal)preSalida;
		}

		return salida;
		
	}
	
	/**
	 * Devuelve el valor de varLzc para el módulo FPTOZC.
	 * @param mapVariables
	 * @param claveVariable
	 * @return
	 */
	public static BigDecimal getVarLzcFPTOZC(final Map<String, Object> mapVariables, final String claveVariable) {
		final Object salida = mapVariables.get(claveVariable);
		return (BigDecimal) salida;
	}
	
	/**
	 * Devuelve valor de varEdadCalc1, varEdadCalc1 o varEdadCalc (según parámetros de entrada). Si no existe lo calcula y lo setea en el hashmap.
	 * @param mapVariables
	 * @param modulo
	 * @param fecinisus
	 * @param fnacAseg
	 * @param varCriterioEdad
	 * @return
	 */
	public static BigDecimal getVarEdadCalc(final Map<String, Object> mapVariables, final String claveVariable, final Timestamp fecinisus, 
			final Timestamp fnacAseg, final String varCriterioEdad, final Timestamp fecIniRenta, final Integer eDifer) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = FuncionesAuxiliares.nEdad(fecinisus, fnacAseg, varCriterioEdad, fecIniRenta, eDifer);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal)preSalida;
		}

		return salida;
	}
	
	/**
	 * Devuelve el valor de varLzc1 o varLzc2. En el caso de que no exista lo calcula, lo setea en el hashmap y lo devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param varZc1
	 * @param varLzcEntero
	 * @param varLzcEntero1
	 * @return
	 */
	public static BigDecimal getVarLzc(final Map<String, Object> mapVariables, final String claveVariable, final BigDecimal varZc1, final BigDecimal varLzcEntero, final BigDecimal varLzcEntero1) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			//salida = varLzcEntero.add(varZc1.remainder(BigDecimal.ONE).multiply(varLzcEntero1.subtract(varLzcEntero)));
			salida = Util.interpolaPorEdad(varLzcEntero, varLzcEntero1, varZc1);
			mapVariables.put(claveVariable, salida);
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal)preSalida;
		}

		return salida;
	}
	
	/**
	 * Devuelve valor de varZc1 o varZc2 (según parámetros). Si no existe lo calcula, lo setea en el hashmap y lo devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param varEdadCalc
	 * @param fecinisus
	 * @param fcalc
	 * @param varCriterioFecha
	 * @return
	 */
	public static BigDecimal getVarZc(final Map<String, Object> mapVariables, final String claveVariable, final BigDecimal varEdadCalc, final Timestamp fecinisus, final Timestamp fcalc, final String varCriterioFecha, final String varCriterEdad) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			final BigDecimal varFracc0 = FuncionesAuxiliares.nAnnos(fecinisus, fcalc, varCriterioFecha);
			if(varCriterEdad.equals("05")){
				salida = varEdadCalc.add(varFracc0);
			}else{
				salida = varEdadCalc.setScale( 0, BigDecimal.ROUND_DOWN).add(varFracc0);
			}
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal)preSalida;
		}

		return salida;
	}
	
	public static BigDecimal getVarZcEdadReal(final Map<String, Object> mapVariables, final String claveVariable, final BigDecimal varEdadCalc, final Timestamp fecinisus, final Timestamp fcalc, final String varCriterioFecha) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			final BigDecimal varFracc0 = FuncionesAuxiliares.nAnnos(fecinisus, fcalc, varCriterioFecha);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal)preSalida;
		}

		return salida;
	}
	
	
	/**
	 * Devuelve varRever. Si no está almacenada la calcula, la almacena y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param preversion
	 * @return
	 */
	public static BigDecimal getVarRever(final Map<String, Object> mapVariables, final String claveVariable, final BigDecimal preversion) {
		final BigDecimal preSalida = (BigDecimal)mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		if (null == preSalida) {
			salida = preversion.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
			
		} else {
			salida = preSalida;
		}

		return salida;
	}
	
	/**
	 * Devuelve el valor de varW. Si no está almacenada la calcula, la almacena y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param fecCierre
	 * @param varTm
	 * @param varAnoNac
	 * @param varI1
	 * @param psobremort
	 * @param priesgo
	 * @return
	 */
//	public static Integer getVarW(final Map<String, Object> mapVariables, final String claveVariable,
//			final Timestamp fecCierre, final String varTm, final int varAnoNac, final BigDecimal varI1,
//			final java.math.BigDecimal psobremort, final java.math.BigDecimal priesgo) {
//		
//		final Object preSalida = mapVariables.get(claveVariable);
//		Integer salida = null;
//		
//		if (varAnoNac<ConstantsModulos.CTE_ANNO_MIN){
//			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC, new String[]{String.valueOf(varAnoNac), "Año nacimiento aseg"});
//		}
//		
//		if (null == preSalida) {
//			final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
//			salida = servicio.recuperarEdadMax(fecCierre, Integer.valueOf(varTm), String.valueOf(varAnoNac), varI1, psobremort, priesgo);
//			
//			// El dato a null se controla fuera, en el módulo, donde se controla con su error personalizado para la variable afectada y generalmente en la primera iteracion
//			if (null != salida) {
//				mapVariables.put(claveVariable, salida);
//				
//				if (UtilModulos.LOG.isTraceEnabled()) {
//					UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
//				}
//			}
//		} else {
//			salida = (Integer)preSalida;
//		}
//
//		return salida;
//	}
	
	/**
	 * Devuelve el valor de EdadMaxima2. Si no está almacenada la calcula, la almacena y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param varAnoNac
	 * @param umic
	 * @param btcUmic
	 * @param ordenAsegurado
	 * @return
	 */
	
	public static Integer getVarEdadMaxima2(final Map<String, Object> mapVariables, final String claveVariable,
			final int varAnoNac, final Umic umic, final DetalleBaseTecnica btcUmic, final IObtenerConfiguracion.OrdenAsegurado ordenAsegurado) {
		
		final Object preSalida = mapVariables.get(claveVariable);
		Integer salida = null;
		
		if (varAnoNac<ConstantsModulos.CTE_ANNO_MIN){
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC, new String[]{String.valueOf(varAnoNac), "Año nacimiento aseg"});
		}
		
		if (null == preSalida) {
			final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
			salida = servicio.recuperarEdadMax(umic, btcUmic,  String.valueOf(varAnoNac), umic.getAsegurados().getCsexAseg1(), 
					ordenAsegurado);

			// El dato a null se controla fuera, en el módulo, donde se controla con su error personalizado para la variable afectada y generalmente en la primera iteracion
			if (null != salida) {
				mapVariables.put(claveVariable, salida);
				
				if (UtilModulos.LOG.isTraceEnabled()) {
					UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
				}
			}
		} else {
			salida = (Integer)preSalida;
		}

		return salida;
	}
	
	/**
	 * Devuelve el valor de varVZC1Difercol. Si no está almacenada la calcula, la almacena y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param fecInisus
	 * @param fecIniRta
	 * @param fcalc
	 * @param varEdadCalc1
	 * @param lstValoresTabMort1
	 * @param varLzc1
	 * @param varCriterioFecha
	 * @param valorDefecto
	 * @return
	 */
	public static BigDecimal getVarVzc1Difercol(final Map<String, Object> mapVariables, final String claveVariable, final Timestamp fecInisus, final Timestamp fecIniRta, final Timestamp fcalc, final BigDecimal varEdadCalc1, final List<BigDecimal> lstValoresTabMort1, final BigDecimal varLzc1, final String varCriterioFecha, final BigDecimal valorDefecto) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = valorDefecto;
			/**
				varVzc1Difercol = 1. 					
			-	Si umic.rentas.fecini  > fcalc 
				-	varLDifercolEntero= valoresTabMort(ENTERO(varEdadDifer)).wkvalor 
				-	varLDifercolEntero1 = valoresTabMort(ENTERO(varEdadDifer)+1).wkvalor 
				-	varLDifercol = varLDifercolEntero + parteDecimal(varEdadDifer) * (varLDifercolEntero1 – varLDifercolEntero)
				-	varVzc1Difercol = varLDifercol / varLzc1 VarVzc1Difercol --> dejo la variable en memoria disponible para el subproceso de la umic.
			*/
			if (fcalc.before(fecIniRta)) {
				//final BigDecimal varLDifercol = varLDifercolEnt.add(varEdadDifer.remainder(BigDecimal.ONE).multiply(varLDifercolEnt1.subtract(varLDifercolEnt)));
				/**
				 	- varEdadDifer  = ParteEntera(VarEdadCalc1) + varFraccDifer
				 */
				final BigDecimal varEdadDifer = varEdadCalc1.add(FuncionesAuxiliares.nAnnos(fecInisus, fecIniRta, varCriterioFecha));
				int varEdadDiferEntero = varEdadDifer.intValue();
				final BigDecimal varLDifercol = Util.interpolaPorEdad(lstValoresTabMort1.get(varEdadDiferEntero), lstValoresTabMort1.get(varEdadDiferEntero + 1), varEdadDifer);
				salida = varLDifercol.divide(varLzc1,ConstantsFunciones.MATH_CONTEXT);
			}
			mapVariables.put(claveVariable, salida);
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal)preSalida;
		}

		return salida;
	}
	
	/**
	 * Metodo que obtiene el valor de varJcasado. En el caso de que no exista lo calcula y lo setea en el hashmap
	 * 
	 * @param mapVariables
	 * @param modulo
	 * @param varJcasado
	 * @return mapVariables.get(ConstantsModulos.CTE_VAR_J_CASADO.concat(modulo))
	 */
	public static Integer getVarJcasado(final Map<String, Object> mapVariables, final String modulo, final Integer varJcasado) {
		final String clave = ConstantsModulos.CTE_VAR_J_CASADO.concat(modulo);
		Object preSalida = null;
		Integer salida = null;
		
		//Siempre guardamos los cambios en esta variable
		if (null != varJcasado) {
			mapVariables.put(clave, varJcasado);
		}
		
		preSalida = mapVariables.get(clave);
		if (preSalida != null) {
			salida = (Integer)preSalida;
		}
		return salida;
	}
	
	/**
	 * Metodo que obtiene el valor de varImpAcumulado. En el caso de que no exista lo calcula y lo setea en el hashmap.
	 * 
	 * @param mapVariables
	 * @param modulo
	 * @param varImpAcumulado
	 * @return mapVariables.get(ConstantsModulos.CTE_IMP_ACUMULADO.concat(modulo))
	 */
	public static BigDecimal getVarImpAcumulado(final Map<String, Object> mapVariables, final String modulo, final BigDecimal varImpAcumulado) {
		final String clave = ConstantsModulos.CTE_IMP_ACUMULADO.concat(modulo);
		Object preSalida = mapVariables.get(clave);
		BigDecimal salida = null;
		
		if (null == preSalida) {
			// Se inicializa a BigDecimal.ZERO
			salida = varImpAcumulado;
		} else {
			salida = varImpAcumulado.add((BigDecimal) preSalida);
		}
		mapVariables.put(clave, salida);
		
		return salida;
	}
	
	/**
	 * Metodo que obtiene el valor de wFechaRenov para el periodo actual.
	 * En el caso de que no exista lo calcula y lo setea en el hashmap.
	 * 
	 * @param mapVariables
	 * @param modulo
	 * @param iteracion
	 * @param wFechaRenov
	 * @return varFecRenov
	 */
	public static Timestamp getWFechaRenov(final Map<String, Object> mapVariables,  final String variableMemoria, final Timestamp wFechaRenov) {
		return setGetTimestamp(mapVariables, variableMemoria, wFechaRenov);
	}
	
	/**
	 * Metodo que cambia el valor de la fecha de renovación dependiendo si es fecha de pago o fecha de devengo
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param valor
	 */
	public static void setWFechaRenovacion(Map<String, Object> mapVariables, String claveVariable, Timestamp valor) {
		mapVariables.put(claveVariable, valor);
	}
	
	/**
	 * Devuelve valor de varPN0. Si no está almacenado en memoria lo calcula, lo almacena en el HASHMAP y lo devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param varPn0
	 * @return
	 */
	public static BigDecimal getVarPN0(final Map<String, Object> mapVariables, final String claveVariable, final BigDecimal varPn0) {
		return setGetBigDecimal(mapVariables, claveVariable, varPn0);
	}	
	
	/**
	 * Devuelve varn para el caso concreto del módulo VBX362. En el caso de que no exista lo calcula y lo setea en el hashmap.
	 * @param mapVariables
	 * @param claveVariable
	 * @param fecinisus
	 * @param fechaHastaUltimoPeriodo
	 * @param varCriFec
	 * @return
	 */
	public static BigDecimal getVarNVBX362(final Map<String, Object> mapVariables, final String claveVariable, final Timestamp fecinisus, final Timestamp fechaHastaUltimoPeriodo, final String varCriFec) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = FuncionesAuxiliares.nAnnos(fecinisus, fechaHastaUltimoPeriodo, varCriFec);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal) preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Devuelve varDiferCol. Si no está almacenada la calcula, la almacena y la devuelve.
	 * @param mapVariables
	 * @param modulo
	 * @param fecinisus
	 * @param fecIni
	 * @param varCriFec
	 * @return
	 */
	public static BigDecimal getVarDiferColVBX362(final Map<String, Object> mapVariables, final String modulo, final Timestamp fecinisus, final Timestamp fecIni, final Timestamp fecJ, final String varCriFec) {
		final BigDecimal preSalida = (BigDecimal) mapVariables.get(ConstantsModulos.CTE_VAR_DIFER_COL.concat(modulo));
		BigDecimal varDifercol = null;
		
		
		if (null == preSalida) {
			varDifercol = FuncionesAuxiliares.nAnnos(fecinisus, fecIni, varCriFec);
			mapVariables.put(ConstantsModulos.CTE_VAR_DIFER_COL.concat(modulo), varDifercol);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + ConstantsModulos.CTE_VAR_DIFER_COL + " no tiene valor por lo que se calcula = {}", varDifercol);
			}
		} else {
			varDifercol = preSalida;
		}

		return varDifercol;
	}
	
	/**
	 * Devuelve varDiferCol. Si no está almacenada la calcula, la almacena y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param valorDefecto
	 * @param fecinisus
	 * @param fecIni
	 * @param varCriFec
	 * @return
	 */
	public static BigDecimal getVarDiferCol(final Map<String, Object> mapVariables, final String claveVariable, final BigDecimal valorDefecto, final Timestamp fecinisus, final Timestamp fecIni, final String varCriFec) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		if (null == preSalida) {
			salida = FuncionesAuxiliares.nAnnos(fecinisus, fecIni, varCriFec);
			if ( BigDecimal.ONE.compareTo(salida) < 0) {
				// varDiferCol no puede ser mayor que BigDecimal.ONE.
				salida = BigDecimal.ONE;
			}
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal)preSalida;
		}

		return salida;
	}
	
	/**
	 * Devuelve valor varPo. Si no está almacenada la calcula, la guarda en el Hashmap y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param varFechaEfecto
	 * @param fcalc
	 * @return
	 */
	public static Integer getVarP0(final Map<String, Object> mapVariables, final String claveVariable, final Timestamp varFechaEfecto, final Timestamp fcalc) {
		final Object preSalida = mapVariables.get(claveVariable);
		Integer salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			
			salida = FuncionesAuxiliares.tc(varFechaEfecto, fcalc);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (Integer)preSalida;
		}

		return salida;	
	}
		
	/**
	 * Devuelve valor varX. Si no está almacenada la calcula, la guarda en el Hashmap y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param fechaReferencia
	 * @param fnacAseg
	 * @param varCriEdad
	 * @return
	 */
	public static BigDecimal getVarX(final Map<String, Object> mapVariables, final String claveVariable, final Timestamp fechaReferencia, 
			final Timestamp fnacAseg, final String varCriEdad, final Timestamp fecIniRenta, final Integer eDifer) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = FuncionesAuxiliares.nEdad(fechaReferencia, fnacAseg, varCriEdad, fecIniRenta, eDifer);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal) preSalida;
		}

		return salida;
	}

	/**
	 * Devuelve varLimCap. Si no está almacenado lo calcula, lo almacena y lo devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param kmodalidad
	 * @param kgarantia
	 * @param fecinisus
	 * @param varX
	 * @param varK
	 * @return
	 */
	public static LimitesCapital getVarLimCap(final Map<String, Object> mapVariables, final String claveVariable,
			final Integer kmodalidad, final Integer kgarantia, final Timestamp fecinisus, final BigDecimal varX, final Integer varK) {
		final Object preSalida = mapVariables.get(claveVariable);
		LimitesCapital salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
			salida = servicio.recuperarLimitesCapital(String.valueOf(kmodalidad), String.valueOf(kgarantia), fecinisus, varX.intValue(), varK);
			ValidacionesComunesModulos.validarLimitesCapital(salida, kmodalidad, kgarantia, fecinisus, varX.intValue(), varK);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (LimitesCapital) preSalida;
		}

		return salida;
	}
	
	/**
	 * Devuelve varListaLimCap. Si no está almacenado lo calcula, lo almacena y lo devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param kmodalidad
	 * @param kgarantia
	 * @param fecinisus
	 * @param varX
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<LimitesCapital> getVarListaLimCap(final Map<String, Object> mapVariables, final String claveVariable,
			final Integer kmodalidad, final Integer kgarantia, final Timestamp fecinisus, final BigDecimal varX) {
		final Object preSalida = mapVariables.get(claveVariable);
		List<LimitesCapital> salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
//INI-816517: Se formatean la modalidad y garantia antes de hacer el acceso a la tabla 646
			String modalidadformateada = Util.stringformateado(String.valueOf(kmodalidad),3,"0");
			String garantiaformateada = Util.stringformateado(String.valueOf(kgarantia),3,"0");
			salida = servicio.recuperarListaLimitesCapital(modalidadformateada, garantiaformateada, fecinisus, varX.intValue());
//			salida = servicio.recuperarListaLimitesCapital(String.valueOf(kmodalidad), String.valueOf(kgarantia), fecinisus, varX.intValue());
//FIN-816517
			//ValidacionesComunesModulos.validarListaLimitesCapital(salida, kmodalidad, kgarantia, fecinisus, varX.intValue());
			
//INI-938078: ordenamos los datos obtenidos de la tabla por fecha			
			salida = sort(salida);
//FIN-938078
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (List<LimitesCapital>) preSalida;
		}

		return salida;
	}

//INI-938078: ordenamos los datos obtenidos de la tabla 646 por fecha			
		/**
		 * Devuelve lista ordenada
		 * @param LimitesCapital
		 * @returnLimitesCapital
		 */	
		public static List<LimitesCapital> sort(List<LimitesCapital> myarray) {      
	        int n = myarray.size();
	        int i = 0;
	        LimitesCapital dato1;
	        LimitesCapital dato2;
	        LimitesCapital datoTMP;
	        boolean ordenado = false;
	        boolean cambio = false;
	        while(!ordenado) {
	           cambio = false;
	              for(i=n-1; i>=0; i--) {
	                        if(i>0) {
	                        dato1=myarray.get(i);
	                        dato2=myarray.get(i-1);
	                        if(dato1.getFefecfin().before(dato2.getFefecfin())) {
	                              datoTMP=dato1;
	                              myarray.remove(i);
	                              myarray.add((i-1),datoTMP);                              
	                              cambio=true;
	                        
	                        }
	                 }                              
	              }
	              if(!cambio) {
	                 ordenado = true;
	              }
	        }
	        
	        return myarray;
	     } 

//FIN-938078	
			
	
	
	/**
	 * Devuelve varK. Si no está almacenada la calcula, la almacena y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param fecinisus
	 * @param fcalc
	 * @return
	 */
	public static Integer getVarK(final Map<String, Object> mapVariables, final String claveVariable, final Timestamp fecinisus, final Timestamp fcalc) {
		final Object preSalida = mapVariables.get(claveVariable);
		Integer salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = FuncionesAuxiliares.tcm(fecinisus, fcalc);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (Integer) preSalida;
		}

		return salida;
	}
	//INI-816517	
		/**
			 * Devuelve varK. Si no está almacenada la calcula, la almacena y la devuelve.
			 * @param mapVariables
			 * @param claveVariable
			 * @param fecinisus
			 * @param fcalc
			 * @return
			 */
			public static Integer getVarKini(final Map<String, Object> mapVariables, final String claveVariable, final Timestamp fecinisus, final Timestamp fcalc) {
				final Object preSalida = mapVariables.get(claveVariable);
				Integer salida = null;
				
				// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
				if (null == preSalida) {
					salida = FuncionesAuxiliares.tcmIni(fecinisus, fcalc);
					mapVariables.put(claveVariable, salida);
					
					if (UtilModulos.LOG.isTraceEnabled()) {
						UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
					}
				} else {
					salida = (Integer) preSalida;
				}

				return salida;
			}
	//FIN-816517

	/**
	 * Metodo que obtiene el valor de varLimite. En el caso de que no exista lo calcula y lo setea en el hashmap.
	 * 
	 * @param mapVariables
	 * @param modulo
	 * @param varLimite
	 * @return valorLimite
	 */
	public static BigDecimal getVarLimite(final Map<String, Object> mapVariables, final String claveVariable, 
			final String ragravado, final LimitesCapital varLimCap) {
		
		
		BigDecimal salida = null;
		
		
		if (ConstantsModulos.CTE_N.equals(ragravado)) {
			salida = varLimCap.getEcaphasta();
		}  else if (ConstantsModulos.CTE_S.equals(ragravado)) {
			salida = varLimCap.getEcaphastaAgra();
		}

		mapVariables.put(claveVariable, salida);
		
		if (UtilModulos.LOG.isTraceEnabled()) {
			UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
		}
		
		return salida;
	}
	
	/**
	 * Metodo que obtiene el valor de varFecFinProy para el periodo actual.
	 * En el caso de que no exista lo calcula y lo setea en el hashmap.
	 * 
	 * @param mapVariables
	 * @param modulo
	 * @param varFecFinProy
	 * @return
	 */
	public static Timestamp getVarFecFinProy(final Map<String, Object> mapVariables, final String modulo, final Timestamp varFecFinProy) {
		final String clave = ConstantsModulos.CTE_VAR_FEC_FPROY.concat(modulo);
		return setGetTimestamp(mapVariables, clave, varFecFinProy);
	}
	
	/**
	 * Metodo que obtiene el valor de vaAnnos. En el caso de que no exista lo calcula y lo setea en el hashmap.
	 * @param mapVariables
	 * @param claveVariable
	 * @param fecdesderenova
	 * @param fechaPago
	 * @param varCriterioFecha
	 * @return
	 */
	public static BigDecimal getVaAnnos(final Map<String, Object> mapVariables, final String claveVariable, final Timestamp fecdesderenova, final Timestamp fechaPago, final String varCriterioFecha) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = FuncionesAuxiliares.nAnnos(fecdesderenova, fechaPago, varCriterioFecha);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal) preSalida;
		}

		return salida;
	}
	
	/**
	 * Metodo que obtiene el valor de varRecargo para el periodo actual.
	 * En el caso de que no exista lo calcula y lo setea en el hashmap.
	 * @param mapVariables
	 * @param claveVariable
	 * @param iteracion
	 * @param varNP
	 * @param varInt
	 * @return
	 */
	public static BigDecimal getVarRecargo(final Map<String, Object> mapVariables, final String claveVariable, final Integer iteracion, final BigDecimal varNP, final BigDecimal varInt) {
		final String clave = claveVariable + iteracion;
		final Object preSalida = mapVariables.get(clave);
		BigDecimal salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = FuncionesPrimas.recargofro(varNP, varInt);
			mapVariables.put(clave, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + clave + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal) preSalida;
		}

		return salida;
		
	}
	
	/**
	 * Devuelve varNR. Si no está almacenado le asigna el parámetro varNR, lo almacena y lo devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param varNR
	 * @return
	 */
	public static Integer getVarNR(final Map<String, Object> mapVariables, final String claveVariable, final Integer varNR) {
		return setGetInteger(mapVariables, claveVariable, varNR);
	}

	/**
	 * Recupera el valor de varNR del HashMap; luego lo incrementa varNR en uno, lo almacena en el HashMap y lo devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @return
	 */
	public static Integer incrementaVarNR(final Map<String, Object> mapVariables, final String claveVariable) {
		Integer varNR = (Integer)mapVariables.get(claveVariable);
		varNR++;
		mapVariables.put(claveVariable, varNR);
		return varNR;
	}
	
	/**
	 * Metodo que obtiene el valor de varY. En el caso de que no exista lo calcula y lo setea en el hashmap.
	 * 
	 * @param mapVariables
	 * @param modulo
	 * @param varY
	 * @return valorY
	 */
	public static BigDecimal getVarY(final Map<String, Object> mapVariables, final String clave, final Timestamp fecha1, final Timestamp fecha2, final String varCriFec) {
		//final Object preSalida = mapVariables.get(clave);
		BigDecimal salida = null;
		Integer operando1 = 0;
		Integer operando2 = 0;
		GregorianCalendar calFecha1 = null;
		GregorianCalendar calFecha2 = null;
		
		//if (null == preSalida) {
			calFecha1 = new GregorianCalendar();
			calFecha2 = new GregorianCalendar();
			calFecha1.setTime(fecha1);
			calFecha2.setTime(fecha2);
			
			operando1 = calFecha1.get(Calendar.YEAR) * ConstantsFunciones.CTE_365 + FuncionesVBX.ddEnero(fecha1, varCriFec) + calFecha1.get(Calendar.DAY_OF_MONTH);
			operando2 = calFecha2.get(Calendar.YEAR) * ConstantsFunciones.CTE_365 + FuncionesVBX.ddEnero(fecha2, varCriFec) + calFecha2.get(Calendar.DAY_OF_MONTH);
			salida = BigDecimal.valueOf(operando1 - operando2).divide(ConstantsFunciones.CTE_OPER_365, ConstantsFunciones.MATH_CONTEXT);
			mapVariables.put(clave, salida);
//		} else {
//			salida = (BigDecimal) preSalida;
//		}
		
		return salida;
	}
	
	/**
	 * Devuelve varNRM. Si no está almacenada la calcula, la setea en el HASHMAP y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param fecinisus
	 * @param fecFinTramo1
	 * @param varCriFec
	 * @return
	 */
	public static Integer getVarNRM(final Map<String, Object> mapVariables, final String claveVariable, final Timestamp fecinisus, final Timestamp fecFinTramo1, final String varCriFec) {
		final Object preSalida = mapVariables.get(claveVariable);
		Integer salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = FuncionesAuxiliares.nAnnos(fecinisus, fecFinTramo1, varCriFec).intValue();
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (Integer) preSalida;
		}

		return salida;
	}
	
	/**
	 * Devuelve el valor de la variable varM. Si no está almacenada la calcula, la guarda y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param fecinisus
	 * @param fecFinTramo1
	 * @param varCriFec
	 * @return
	 */
	public static BigDecimal getVarM(final Map<String, Object> mapVariables, final String claveVariable, final Timestamp fecinisus, final Timestamp fecFinTramo1, final String varCriFec) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = FuncionesAuxiliares.nAnnos(fecinisus, fecFinTramo1, varCriFec);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal) preSalida;
		}

		return salida;
	}
	
	/**
	 * Devuelve el valor de la variable varpri005. Si no está almacenada la calcula, la guarda y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param varPrimaIni
	 * @param varRentgeo2it
	 * @return salida
	 */
	public static BigDecimal getVarPri005(final Map<String, Object> mapVariables, final String claveVariable, BigDecimal varPrimaIni, BigDecimal varRentgeo2it) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = varPrimaIni.multiply(varRentgeo2it);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal) preSalida;
		}

		return salida;
	}
	
	/**
	 * Devuelve el valor de la variable varpri005. Si no está almacenada la calcula, la guarda y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param varncmeses
	 * @return
	 */
	public static Integer getVarNcMeses(final Map<String, Object> mapVariables, final String claveVariable, Integer varncanos){
		final Object preSalida = mapVariables.get(claveVariable);
		Integer salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = ConstantsFunciones.CTE_12 * varncanos;
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida =  (Integer) preSalida;
		}

		return salida;
		
		
	}
	
	public static Integer getVarNa(final Map<String, Object> mapVariables, final String claveVariable,final Integer varCperaseg,final Integer ndursegano, Integer varncMeses){
		final Object preSalida = mapVariables.get(claveVariable);
		Integer salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			if(varCperaseg != ConstantsFunciones.CTE_0){
				
				salida = varCperaseg;
			
			}else{
				
				salida = (ndursegano * ConstantsFunciones.CTE_12) - varncMeses; 
				
			}
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida =  (Integer) preSalida;
		}

		return salida;
		
		
	}
	
	
	
	
	/**
	 * Devuelve el valor de la variable varM. Si no está almacenada la calcula, la guarda y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param fecFinTramo1
	 * @param fecIniTramo1
	 * @param varCriFec
	 * @return
	 */
	public static BigDecimal getVarMCSP362(final Map<String, Object> mapVariables, final String claveVariable, final Timestamp fecFinTramo1, final Timestamp fecIniTramo1, final String varCriFec) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		if (null == preSalida) {
			salida = FuncionesAuxiliares.nAnnos(fecFinTramo1, fecIniTramo1, varCriFec);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal) preSalida;
		}

		return salida;
	}
	
	/**
	 * Metodo que obtiene el valor de varRen. En el caso de que no exista lo calcula y lo setea en el hashmap
	 * 
	 * @param mapVariables
	 * @param modulo
	 * @param varRen
	 * @return
	 */
	public static BigDecimal getVarRen(final Map<String, Object> mapVariables, final String modulo, final String variable, final Timestamp fecinisus, final Timestamp fecHastaRenova, final String varCriFec) {
		final Object preSalida = mapVariables.get(variable.concat(modulo));
		BigDecimal salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = FuncionesAuxiliares.nAnnos(fecinisus, fecHastaRenova, varCriFec);
			mapVariables.put(variable.concat(modulo), salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + variable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal) preSalida;
		}

		return salida;
	}
	
	/**
	 * Devuelve varTCy. Si no está almacenado lo calcula, lo asigna en el Hashmap y lo devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param fecinisus
	 * @param fcal
	 * @param varCriFec
	 * @return
	 */
	public static BigDecimal getVarTCy(final Map<String, Object> mapVariables, final String claveVariable, final Timestamp fecinisus, final Timestamp fcal, final String varCriFec) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = FuncionesAuxiliares.nAnnos(fecinisus, fcal, varCriFec);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal) preSalida;
		}

		return salida;
	}
	
	/**
	 * Metodo que obtiene el valor de varDc. En el caso de que no exista lo calcula y lo setea en el hashmap
	 * @param mapVariables
	 * @param claveVariable
	 * @param fecdesderenova
	 * @param fecCierre
	 * @param varCriterFec
	 * @return
	 */
	public static Integer getVarDc(final Map<String, Object> mapVariables, final String claveVariable, final Timestamp fecdesderenova, final Timestamp fecCierre, final String varCriterFec) {

		Integer salida = null;

		salida = FuncionesAuxiliares.nDias(fecdesderenova, fecCierre, varCriterFec);
		mapVariables.put(claveVariable, salida);
		
		if (UtilModulos.LOG.isTraceEnabled()) {
			UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
		}


		return salida;
	}
	
	/**
	 * Metodo que obtiene el valor de varDr. En el caso de que no exista lo calcula y lo setea en el hashmap
	 * @param mapVariables
	 * @param claveVariable
	 * @param fecdesderenova
	 * @param fechastarenova
	 * @param varCriterFec
	 * @return
	 */
	public static Integer getVarDr(final Map<String, Object> mapVariables, final String claveVariable, final Timestamp fecdesderenova, final Timestamp fechastarenova, final String varCriterFec) {
		final Object preSalida = mapVariables.get(claveVariable);
		Integer salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = FuncionesAuxiliares.nDias(fecdesderenova, fechastarenova, varCriterFec);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (Integer) preSalida;
		}

		return salida;
	}
	
	/**
	 * Devuelve varUmicProyecBTI. Si no está almacenada la calcula, la setea en el Hashmap y la devuelve.
	 * @param mapVariables
	 * @param modulo
	 * @param varUmicProyecBTI
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<DetalleCorriente> getVarUmicProyecBTI(final Map<String, Object> mapVariables, final String modulo, final List<DetalleCorriente> varUmicProyecBTI) {
		final String clave = ConstantsModulos.CTE_VAR_UMIC_PROYEC_BTI.concat(modulo);
		final Object preSalida = mapVariables.get(clave);
		List<DetalleCorriente> salida = null;

		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = varUmicProyecBTI;
			mapVariables.put(clave, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable ".concat(clave).concat(" no tiene valor por lo que se calcula = {}"), varUmicProyecBTI);
			}
		} else {
			salida = (List<DetalleCorriente>)preSalida;
		}
		return salida;
	}

	/**
	 * Obtiene varLstProyeccion. Si no está almacenada la calcula, la guarda en el HashMap y la devuelve.
	 * @param mapVariables
	 * @param modulo
	 * @param constanteVariable
	 * @param varLstProy
	 * @return
	 */
	public static List<DetalleCorriente> getVarLstProyeccion(final Map<String, Object> mapVariables, final String claveVariable, final String baseTecnica, final Timestamp fecCierre, final UmicKey claveUmic) {
		return setGetListDetalleCorriente(mapVariables, claveVariable, baseTecnica, fecCierre, claveUmic);
	}
	
	/**
	 * Devuelve varD. Si no está almacenada la calcula, la almacena y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param fecinisus
	 * @param fecefecini
	 * @param varCriFec
	 * @return
	 */
	public static Integer getVarD(final Map<String, Object> mapVariables, final String claveVariable, final Timestamp fecinisus, final Timestamp fecefecini, final String varCriFec) {
		final Object preSalida = mapVariables.get(claveVariable);
		Integer salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = FuncionesAuxiliares.nDias(fecinisus, fecefecini, varCriFec);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (Integer) preSalida;
		}

		return salida;
	}
	
	/** 
	 * Devuelve varCSP012. Si no está almacenada la calcula, la almacena en el mapVariables y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param varIcapAct
	 * @param BETARENTAS
	 * @param varVacf
	 * @return salida
	 */
	public static BigDecimal getVarCSP012 (final Map<String, Object> mapVariables, final String claveVariable, final BigDecimal varIcapAct ,final BigDecimal BETARENTAS,final BigDecimal varVacf){
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = BigDecimal.ZERO;
		
		if(null == preSalida){
			salida = varIcapAct.multiply(BETARENTAS.add(varVacf), ConstantsFunciones.MATH_CONTEXT);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
			
		}else{
			
			salida = (BigDecimal) preSalida;
		}
		
		return salida;
	}
	
	
	/**
	 * Devuelve varVacf. Si no está almacenada la calcula, la almacena en el mapVariables y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param NRTA
	 * @param varFPR
	 * @param varl1Bti
	 * @return salida
	 */
	
	public static BigDecimal getVarVacf (final Map<String, Object> mapVariables, final String claveVariable, final Integer NRTA, final Integer varFPR, final BigDecimal varl1Bti, final BigDecimal varPrr){
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = BigDecimal.ZERO;
		
		if(null == preSalida){
			//No se tiene acceso a la implementacion de Vacf
			salida = FuncionesAuxiliares.vacf(NRTA,varFPR, varl1Bti, varPrr);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
			
		}else{
			
			salida = (BigDecimal) preSalida;
		}
		
		return salida;
	} 
	
	/**
	 * Metodo que obtiene el valor de varRy. En el caso de que no exista lo calcula y lo setea en el hashmap
	 * 
	 * @param mapVariables
	 * @param modulo
	 * @param varRy
	 * @return
	 */	
	public static BigDecimal getVarRy(final Map<String, Object> mapVariables, final String modulo, final String constanteVariable ,final BigDecimal varRy) {
		return setGetBigDecimal(mapVariables, constanteVariable.concat(modulo), varRy);
	}
	
	/**
	 * Metodo que obtiene el valor de varPartAnoNR y vary
	 * @param fecha1
	 * @param fecha2
	 * @param varCriFec
	 * @return
	 */
	public static BigDecimal getVarPartAnoNRYvarY(final Timestamp fecha1, final Timestamp fecha2, final String varCriFec) {
		//Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		Integer operando1 = 0;
		Integer operando2 = 0;
		Fecha calFecha1 = UtilFechas.getFecha(fecha1);
		Fecha calFecha2 = UtilFechas.getFecha(fecha2);
		//Fin variables locales


		operando1 = calFecha1.getAnio() * ConstantsFunciones.CTE_365 + FuncionesVBX.ddEnero(fecha1, varCriFec) + calFecha1.getDia();
		operando2 = calFecha2.getAnio() * ConstantsFunciones.CTE_365 + FuncionesVBX.ddEnero(fecha2, varCriFec) + calFecha2.getDia();
		
		resultado = BigDecimal.valueOf((operando1 - operando2)).multiply(ConstantsFunciones.CTE_OPER_1_PARTIDO_365);


		return resultado;
	}

	/**
	 * Metodo que obtiene el valor de varTerminal. En el caso de que no exista lo calcula y lo setea en el hashmap
	 * 
	 * @param mapVariables
	 * @param modulo
	 * @param varTerminal
	 * @return
	 */
	public static String getVarTerminal(final Map<String, Object> mapVariables, final String claveVariable, final Integer kmodalidad, final Integer kgarantia, final String kprestacion, final String baseTec) {
		
		final Object preSalida = mapVariables.get(claveVariable);
		String salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
			final FlujosProbables flujos = servicio.recuperarConfProv(kmodalidad, kgarantia, kprestacion, baseTec);
			ValidacionesComunesModulos.validarConfProv(flujos, kmodalidad, kgarantia, kprestacion, baseTec);
			salida = flujos.getProvTerminal();
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (String)preSalida;
		}

		return salida;	
	}

	
	/**
	 * Metodo que obtiene el valor de ProvNominal. En el caso de que no exista lo calcula y lo setea en el hashmap
	 * 
	 * @param mapVariables
	 * @param modulo
	 * @param varTerminal
	 * @return
	 */
	public static String getProvNominal(final Map<String, Object> mapVariables, final String claveVariable, final Integer kmodalidad, final Integer kgarantia, final String kprestacion, final String baseTec) {
		
		final Object preSalida = mapVariables.get(claveVariable);
		String salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
			final FlujosProbables flujos = servicio.recuperarConfProv(kmodalidad, kgarantia, kprestacion, baseTec);
			ValidacionesComunesModulos.validarConfProv(flujos, kmodalidad, kgarantia, kprestacion, baseTec);
			salida = flujos.getProvNominal();
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (String)preSalida;
		}

		return salida;	
	}
	
	/**
	 * Devuelve varAlfa. Si no está almacenada la calcula, la almacena y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param fecdesderenova
	 * @param fechastarenova
	 * @param fecCierre
	 * @param varTipoAlfa
	 * @param varCriFec
	 * @return
	 */
	public static BigDecimal getVarAlfa(final Map<String, Object> mapVariables, final String claveVariable, final Timestamp fecdesderenova, final Timestamp fechastarenova, final Timestamp fecCierre, final String varTipoAlfa, final String varCriFec) {
		final String claveCompuesta = claveVariable + fecdesderenova + fechastarenova + fecCierre;
		final Object preSalida = mapVariables.get(claveCompuesta);
		BigDecimal salida = null;
		
		if (null == preSalida) {
			salida = FuncionesVBX.mg001Alfa(fecdesderenova, fechastarenova, fecCierre, varTipoAlfa, varCriFec);
			mapVariables.put(claveCompuesta, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveCompuesta + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal)preSalida;
		}

		return salida;	
	}
	
	/**
	 * Devuelve varAlfaM. Si no está almacenada la calcula, la almacena y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param fecdesderenova
	 * @param fechastarenova
	 * @param fecCierre
	 * @param varTipoAlfa
	 * @param varCriFec
	 * @return
	 */
	public static BigDecimal getVarAlfam(final Map<String, Object> mapVariables, final String claveVariable,
			final Timestamp fecdesderenova, final Timestamp fechastarenova, final Timestamp fecCierre, final String varTipoAlfa, final String varCriFec) {
		
		//final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		//if (null == preSalida) {
			salida = FuncionesVBX.mg00xAlfam(fecdesderenova, fechastarenova, fecCierre, varTipoAlfa, varCriFec);
			//mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
//		} else {
//			salida = (BigDecimal)preSalida;
//		}

		return salida;	
		
	}
	
	/**
	 * Devuelve el valor de varLzc para el modulo VZC. En el caso de que no exista lo calcula, lo setea en el hashmap y lo devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param varZc
	 * @param varLzcEntero
	 * @param varLzcEntero1
	 * @return
	 */
	public static BigDecimal getVarLzcVZC(final Map<String, Object> mapVariables, final String claveVariable,
			final BigDecimal varZc, final List<BigDecimal> lstValoresTabMort) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			if (varZc.intValue() > lstValoresTabMort.size()-2){
				salida = BigDecimal.ZERO;
			}else{
				BigDecimal varLzcEntero = lstValoresTabMort.get(varZc.intValue());
				BigDecimal varLzcEntero1 = lstValoresTabMort.get(varZc.intValue() + 1);
				//salida = varLzcEntero.add(varZc.remainder(BigDecimal.ONE).multiply(varLzcEntero1.subtract(varLzcEntero)));
				salida = Util.interpolaPorEdad(varLzcEntero, varLzcEntero1, varZc);
			}
			mapVariables.put(claveVariable, salida);
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal)preSalida;
		}

		return salida;
	}
	
	/**
	 * Devuelve el conjunto de valores de una tabla de mortalidad. Si no están almacenados en el Hashmap los localiza, los almacena en el hashmap y los devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param umic
	 * @param btc
	 * @param varTabMort
	 * @param idCalc
	 * @param ordenAsegurado
	 * @param criterioEdad
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<BigDecimal> getVarValoresTabMort(final Map<String, Object> mapVariables, final String claveVariable, final Umic umic, 
			final DetalleBaseTecnica btc, final IObtenerConfiguracion.OrdenAsegurado ordenAsegurado, 
			final String criterioEdad, final String tipoValores) {
		final Object preSalida = mapVariables.get(claveVariable);
		List<BigDecimal> salida = null;
		IObtenerConfiguracion.OrdenAsegurado oAseg = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();

			Timestamp fecNacAseg = null;
			String sexAseg = null;
			
			switch(ordenAsegurado) {
			case ASEG1:
				fecNacAseg = umic.getAsegurados().getFnacAseg1();
				sexAseg = umic.getAsegurados().getCsexAseg1();
				break;
			case ASEG2:
				fecNacAseg = umic.getAsegurados().getFnacAseg2();
				sexAseg = umic.getAsegurados().getCsexAseg2();
				break;
			case ASEG3:
				fecNacAseg = umic.getAsegurados().getFnacAseg3();
				sexAseg = umic.getAsegurados().getCsexAseg3();
				break;
			case ASEG4:
				fecNacAseg = umic.getAsegurados().getFnacAseg4();
				sexAseg = umic.getAsegurados().getCsexAseg4();
				break;
			case ASEG5:
				fecNacAseg = umic.getAsegurados().getFnacAseg5();
				sexAseg = umic.getAsegurados().getCsexAseg5();
				break;				
			}
			
			Timestamp fecIniRenta = umic.getRentas().getFecIni();
			Integer varEdifer = umic.getDatosGenerales().getEdifer();
			if ((btc.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL) || 
					btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA) || 
					btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR) || 
					btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTIU) || 
					btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTID) || 
					btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRGTO) || 
					btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMFE) || 
					btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMMI) || 
					btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCF) || 
					btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCI) || 
					btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLFE) || 
					btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLMI) || 
					btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC) || 
					btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRVM)  || 
					btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEP) || 
					btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEN) || 
					btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIP) || 
					btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIN) || 
					btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM) ||
					btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM) || 
					btc.getBaseTec().equals(ConstantesSolvencia.BASE_NIF17LIR) || 
					btc.getBaseTec().equals(ConstantesSolvencia.BASE_N17LIRIN) || 
					btc.getBaseTec().equals(ConstantesSolvencia.BASE_NIFF17OCI) || 
					btc.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17IF) ||
					btc.getBaseTec().equals(ConstantesSolvencia.BASE_N17CLIR) || 
					btc.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17)  ||
					btc.getBaseTec().equals(ConstantesSolvencia.BASE_NF17AEN) ||
					btc.getBaseTec().equals(ConstantesSolvencia.BASE_NF17MFE) ||
					btc.getBaseTec().equals(ConstantesSolvencia.BASE_NF17GTO))
				&& !tipoValores.equals(ConstantesSolvencia.CTE_TABMORT_I)) {
				
				Timestamp fechaEfecto = null;
//INI-929631
				if (umic.getDatosGenerales().getCsitupol().equals("VI") ||					
					(umic.getDatosGenerales().getCsitupol().equals(ConstantsModulos.CTE_DG_CSITU_PRORROGA_PU))    ||
					((umic.getDatosGenerales().getCsitupol().equals(ConstantsModulos.CTE_DG_CSITU_PRORROGA_PP)))){
//FIN-929631
					fechaEfecto = umic.getFechas().getFecinisus();
				} else if (umic.getDatosGenerales().getCsitupol().equals("RE")){
					
					if(null == umic.getFechas().getFecefecred() && ConstantesSolvencia.NEGOCIO_COLECTIVO.equals(umic.getDatosGenerales().getCnegocio())){
						fechaEfecto = umic.getFechas().getFecfinpagprim();
					}else{
						fechaEfecto = umic.getFechas().getFecefecred();
					}
					
				}
				final int edadAseg = FuncionesAuxiliares.nEdad(fechaEfecto, fecNacAseg, criterioEdad, fecIniRenta, varEdifer).intValue();
				final String anio = Integer.toString(UtilFechas.getAnio(fecNacAseg));
				if (umic.getDatosGenerales().getSpcom().equals("S") && 
						(umic.getDatosGenerales().getTipoSubriesgo().equals("INCA") || umic.getDatosGenerales().getTipoSubriesgo().equals("FACC"))) {
					Umic umicCopia = umic;
					String tipoSubriesgo = umic.getDatosGenerales().getTipoSubriesgo();
					umicCopia.getDatosGenerales().setTipoSubriesgo("FALL");
					salida = servicio.recuperarValoresExperiencia(umicCopia, btc, anio, sexAseg, edadAseg, tipoValores, ordenAsegurado);
					umicCopia.getDatosGenerales().setTipoSubriesgo(tipoSubriesgo);
					umic.getDatosGenerales().setTipoSubriesgo(tipoSubriesgo);
				} else {
					IObtenerConfiguracion.OrdenAsegurado VZCASEG = (IObtenerConfiguracion.OrdenAsegurado) mapVariables.get("VZCASEG");
					
					if (VZCASEG != null) {
						switch (VZCASEG) {
							case ASEG1:
								oAseg = IObtenerConfiguracion.OrdenAsegurado.ASEG1;
								break;
							case ASEG2:
								oAseg = IObtenerConfiguracion.OrdenAsegurado.ASEG2;
								break;
							case ASEG3:
								oAseg = IObtenerConfiguracion.OrdenAsegurado.ASEG3;
								break;
							case ASEG4:
								oAseg = IObtenerConfiguracion.OrdenAsegurado.ASEG4;
								break;
							case ASEG5:
								oAseg = IObtenerConfiguracion.OrdenAsegurado.ASEG5;
								break;
								
						}	
						salida = servicio.recuperarValoresExperiencia(umic, btc, anio, sexAseg, edadAseg, tipoValores, oAseg);
					} else {
						salida = servicio.recuperarValoresExperiencia(umic, btc, anio, sexAseg, edadAseg, tipoValores, ordenAsegurado);
					}
				}
			} else {
				final int edadAseg = FuncionesAuxiliares.nEdad(umic.getFechas().getFecinisus(), fecNacAseg, criterioEdad, fecIniRenta, varEdifer).intValue();
				final String anio = Integer.toString(UtilFechas.getAnio(fecNacAseg));
				salida = servicio.recuperarValoresExperiencia(umic, btc, anio, sexAseg, edadAseg, tipoValores, ordenAsegurado);
			}
			mapVariables.put(claveVariable, salida);
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (List<BigDecimal>)preSalida;
		}

		return salida;
	}
	
	
	
	/**
	 * Devuelve getVarValoresAnulacionMensuales. Si no está almacenada la calcula, la almacena y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param btcUmic
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<ValoresAnulacionMensuales> getVarValoresAnulacionMensuales(final Map<String, Object> mapVariables, final String claveVariable, final DetalleBaseTecnica btcUmic) {
		final Object preSalida = mapVariables.get(claveVariable);
		List<ValoresAnulacionMensuales> salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
			salida = servicio.recuperarTasasAnulMensuales(btcUmic.getTablaTanul(), btcUmic.getFtablaAn());
			ValidacionesComunesModulos.validarTasasAnulMensuales(salida, btcUmic.getTablaTanul(), btcUmic.getFtablaAn());
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (List<ValoresAnulacionMensuales>) preSalida;
		}
	
		return salida;
	}
	
	/**
	 * Devuelve getVarValoresAnulacion. Si no está almacenada la calcula, la almacena y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param btcUmic
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<ValoresAnulacion> getVarValoresAnulacion(final Map<String, Object> mapVariables, final String claveVariable, final DetalleBaseTecnica btcUmic) {
		final Object preSalida = mapVariables.get(claveVariable);
		List<ValoresAnulacion> salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
			salida = servicio.recuperarTasasAnul(btcUmic.getTablaTanul(), btcUmic.getFtablaAn(), btcUmic.getBaseTec());
			ValidacionesComunesModulos.validarTasasAnul(salida, btcUmic.getTablaTanul(), btcUmic.getFtablaAn());
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (List<ValoresAnulacion>) preSalida;
		}
	
		return salida;
	}

	/**
	 * Devuelve el valor de varGtoRealUmic. Si no está almacenado lo calcula, lo almacena en el HASHMAP y lo devuelve.
	 * @param mapVariables
	 * @param codigoVariable
	 * @param baseTec
	 * @param ccanal
	 * @param cnegocio
	 * @param kramo
	 * @param kmodalidad
	 * @param fecCierre
	 * @return
	 */
	public static GastosReales getVarGtoRealUmic(final Map<String, Object> mapVariables, final String codigoVariable, final String baseTec, final Integer ccanal, final String cnegocio, final String kramo, final Integer kmodalidad, final Timestamp fecCierre, final String matching) {
		final GastosReales preSalida = (GastosReales)mapVariables.get(codigoVariable);
		GastosReales salida = null;
		if (null == preSalida) {
			final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
			salida = servicio.recuperarGastosReales(baseTec, ccanal, cnegocio, kramo, kmodalidad, fecCierre, matching);
			ValidacionesComunesModulos.validarGastosReales(salida, kmodalidad, baseTec, ccanal, cnegocio, kramo, fecCierre, matching);
			mapVariables.put(codigoVariable, salida);
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + ConstantsModulos.CTE_VAR_GTO_REAL + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = preSalida;
		}

		return salida;
	}
	
	/**
	 * Metodo que obtiene el valor de lstCorrUmic. En el caso de que no exista lo calcula y lo setea en el hashmap
	 * 
	 * @param mapVariables
	 * @param modulo
	 * @param lstCorrUmic
	 * @return
	 */
	public static List<DetalleCorriente> getListaCorrienteUmic(final Map<String, Object> mapVariables, final String codigoVariable, final String baseTecnica, final Timestamp fecCierre, final UmicKey claveUmic) {
		return setGetListDetalleCorriente(mapVariables, codigoVariable, baseTecnica, fecCierre, claveUmic);
	}
	
	/**
	 * Devuelve valor varNpp. Si no existe la calcula, la almacena en el Hashmap y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codigoConstante
	 * @return
	 */
	public static BigDecimal getVarNpp(final Map<String, Object> mapVariables, final String claveVariable, final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbasetec, final String codigoConstante) {
		final Object preSalida = (BigDecimal)mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		if (null == preSalida) {
			final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
			salida = (BigDecimal)servicio.recuperarDefinicionAuxiliar(ccartera, kmodalidad, kgarantia, kbasetec, codigoConstante);
			
			// Esta es una especificación concreta que se hace dentro del método para que no haga esta comprobación para cada iteración de COM001.
			if (null == salida) {
				salida = ConstantsFunciones.CTE_OPER_12;
			}
			
			mapVariables.put(claveVariable, salida);
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal)preSalida;
		}
		return salida;
	}
	
	/**
	 * Devuelve varCriterioFechas. Si no está almacenada la calcula, la setea en el HASHMAP y la devuelve.
	 * 
	 * @param mapVariables
	 * @param codigoVariable
	 * @param kmodalidad
	 * @param kgarantia
	 * @return
	 */
	public static String getCriterioFecDev(final Map<String, Object> mapVariables, final String codigoVariable, final Integer kmodalidad, final Integer kgarantia, String kprestacion, String kprestcal) {
		final String preSalida = (String)mapVariables.get(codigoVariable);
		String salida = null;
		if (null == preSalida) {
			final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
			final CriterioFechas criterFec = servicio.recuperarCriterioFechas(kmodalidad, kgarantia, kprestacion, kprestcal, ConstantsModulos.CTE_PROY_GTOS);
			ValidacionesComunesModulos.validarCriterioFechaRecuperado(criterFec, kmodalidad, kgarantia, kprestacion);
			salida = criterFec.getFecDevengo();
			mapVariables.put(codigoVariable, salida);
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable ".concat(ConstantsModulos.CTE_CRIT_FEC_DEVE).concat(" no tiene valor por lo que se calcula = {}"), salida);
			}
		} else {
			salida = preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Devuelve valor de varASubX. Si no está almacenada la calcula, la almacena y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param proyUmic
	 * @param bloqueCorriente
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param codSubproceso
	 * @return
	 */
	public static BigDecimal getVarASubX(final Map<String, Object> mapVariables, final String claveVariable, final List<DetalleCorriente> proyUmic, 
			final BloqueCorriente bloqueCorriente, final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic,
			final String codSubproceso) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		//TODO Son muchos parámetros, si realmente solo la calcula la primera vez quizás sería interesante dividirlo en dos funciones (una que calcule y devuelva; otra que solo devuelva).
		if (null == preSalida) {
			salida = FuncionesSecundarias.aSubX(proyUmic, bloqueCorriente, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal)preSalida;
		}

		return salida;
	}

	/**
	 * Devuelve valor de varASubXD. Si no está almacenada la calcula, la almacena y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param proyUmic
	 * @param bloqueCorriente
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param codSubproceso
	 * @return
	 */
	public static BigDecimal getVarASubXD(final Map<String, Object> mapVariables, final String claveVariable, final List<DetalleCorriente> proyUmic, 
			final BloqueCorriente bloqueCorriente, final Integer iteracion, final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic,
			final String codSubproceso) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		//TODO Son muchos parámetros, si realmente solo la calcula la primera vez quizás sería interesante dividirlo en dos funciones (una que calcule y devuelva; otra que solo devuelva).
		if (null == preSalida) {
			salida = FuncionesActualizacionFinanciera.aSubXD(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal)preSalida;
		}

		return salida;

	}
	
	/**
	 * Devuelve varGastgivitini. Si no está almacenada la calcula, la setea en el hashmap y la devuelve.
	 * @param mapVariables
	 * @param modulo
	 * @param proyUmic
	 * @param bloqueCorriente
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param codSubproceso
	 * @return
	 */
	public static BigDecimal getVarGastgivitini(final Map<String, Object> mapVariables, final String modulo, 
			final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final String codSubproceso,
			final BigDecimal varI1PorcentajeMasUno, final BigDecimal varI2PorcentajeMasUno) {
		
		final String clave = ConstantsModulos.CTE_VAR_GGVTN.concat(modulo);
		final Object preSalida = mapVariables.get(clave);
		BigDecimal salida = null;
		
		//TODO Son muchos parámetros, si realmente solo la calcula la primera vez quizás sería interesante dividirlo en dos funciones (una que calcule y devuelva; otra que solo devuelva).
		if (null == preSalida) {
			salida = FuncionesGastos.gastgivitini(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso, varI1PorcentajeMasUno, varI2PorcentajeMasUno);
			mapVariables.put(clave, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + clave + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal)preSalida;
		}

		return salida;
	}
	
	/**
	 * Asigna un valor a la fecha de devengo para ser usado en iteraciones posteriores.
	 * @param mapVariables
	 * @param claveVariable
	 * @param iteracion
	 * @param varFecDevAnt
	 */
	public static void setVarFecDevengoAnterior(final Map<String, Object> mapVariables, final String claveVariable, final Integer iteracion, final Timestamp varFecDevAnt) {
		final String clave = claveVariable + iteracion;
		mapVariables.put(clave, varFecDevAnt);
	}
	
	/**
	 * Devuelve la fecha de devengo de la iteración anterior.
	 * @param mapVariables
	 * @param claveVariable
	 * @param iteracion
	 * @return
	 */
	public static Timestamp getVarFecDevengoAnterior(final Map<String, Object> mapVariables, final String claveVariable, final Integer iteracion) {
		final String clave = claveVariable + iteracion;
		return (Timestamp) mapVariables.get(clave);
	}

	/**
	 * Devuelve varK1. Si no está almacenada la calcula, la almacena y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param codK1
	 * @param fecinisus
	 * @param fechaDevengo
	 * @param ndurprima
	 * @param ndursegmes
	 * @return
	 */
	public static BigDecimal getVarK1(final Map<String, Object> mapVariables,final String claveVariable, final String codK1,
			final Timestamp fecinisus, final Timestamp fechaDevengo, final Integer ndurprima, final Integer ndursegmes) {
		
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			final int durk1 = obtenerDuracionCodKX(codK1, fecinisus, fechaDevengo, ndurprima, ndursegmes);	
			salida = getCteRescate(mapVariables, codK1, durk1);
			
			// El dato a null se controla fuera, en el módulo, donde se controla con su error personalizado para la variable afectada y generalmente en la primera iteracion
			if (null != salida) {
				mapVariables.put(claveVariable, salida);
				
				if (UtilModulos.LOG.isTraceEnabled()) {
					UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
				}
			}
		} else {
			salida = (BigDecimal)preSalida;
		}

		return salida;
		
	}
	
	/**
	 * Funcion que obtiene el valor de delta, y en el caso de no tenerlo en memoria se calcula.
	 * @param mapVariables
	 * @param claveVariable
	 * @param varna
	 * @param varnpa
	 * @param varG
	 * @param varMes1
	 * @param varMes2
	 * @param varIF
	 * @return
	 */
	public static BigDecimal getVarDelta(final Map<String, Object> mapVariables, final String claveVariable, final Integer varna, final Integer varnpa, final BigDecimal varG, final Integer varMes1, final Integer varMes2, final BigDecimal varIF) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		if (null == preSalida) {
			salida = FuncionesAuxiliares.delta(varna,varnpa,varG, varMes1, varMes2, varIF);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal) preSalida;
		}

		return salida;
	}
	
	/**
	 * Devuelve ListValoresCurvaTipo. Si no está almacenada la calcula, la setea en el Hashmap y la devuelve.
	 * @param mapVariables
	 * @param clave
	 * @param codCurvaTipo
	 * @param fecCierre
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<ValoresCurvaTipo> setGetListValoresCurvaTipo(final Map<String, Object> mapVariables, final String clave,
			final String codCurvaTipo, final Timestamp fecCierre) {
		final Object preSalida = mapVariables.get(clave);
		List<ValoresCurvaTipo> salida = null;
		// PYAM0001-TAR00163093-Modificación_ACTBEL-INI
		// Se comprueba si la curva almacenanda en hasmap es la misma que la que se pasa por parametro.

		String codCurva = "";

		if (preSalida instanceof List) {
			List entrada = (List) preSalida;
			if (entrada.get(0) instanceof ValoresCurvaTipo) {
				List<ValoresCurvaTipo> entrada2 = (List<ValoresCurvaTipo>) preSalida;
				codCurva = entrada2.get(0).getCodCurvaTipos();
			}
		}

		// Si la variable no tiene valor o no es la misma que tenia almacenada se calcula con los parametros de la funcion y se setea en el hashmap

		// if (null == preSalida) {

		if (!codCurva.equals(codCurvaTipo)) {
			// PYAM0001-TAR00163093-Modificación_ACTBEL-FIN

			final IObtenerConfiguracion servicioConfiguracion = FachadaServicios.getObtenerConfiguracion();
			salida = servicioConfiguracion.recuperarValoresTipos(codCurvaTipo, fecCierre);
			ValidacionesComunesModulos.validarValoresCurvaTipo(salida, codCurvaTipo, fecCierre);
			mapVariables.put(clave, salida);

			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + clave + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (List<ValoresCurvaTipo>) preSalida;
		}

		return salida;
	}

	/**
	 * Devuelve varGtoAnual. Si no está almacenada la calcula, la setea en el Hashmap y la devuelve.
	 * @param mapVariables
	 * @param clavegtoanual
	 * @param claveincripc
	 * @param varGtoUmic
	 * @param fecDev
	 * @return
	 */
	public static BigDecimal getVarGtoAnual(final Map<String, Object> mapVariables, final String clavegtoanual, final String claveincripc,
			final BigDecimal varGtoUmic, final Timestamp fecDev, final String bt) {
		final Object preSalida = mapVariables.get(clavegtoanual);
		BigDecimal salida = null;
		BigDecimal varGtoCalculo = null;
		
		// La primera vez que entre y no se haya calculado ninguna vez varGtoAnual, se calcula con varGtoUmic y se guarda como varGtoAnual
		if (preSalida == null) {
			varGtoCalculo = varGtoUmic;
		}
		
		// Si varGtoAnual ya ha sido calculado al menos una vez, usar varGtoAnual y guardar como varGtoAnual
		else {
			varGtoCalculo = (BigDecimal)preSalida;
		}
		
		// Se obtiene el anio actual para ver si varIpcJ ya ha sido calculado para el año de la fecha devengo
		// De esta manera no es necesario obtener el año de la fecha devengo de la iteracion anterior
		final int anio = UtilFechas.getFecha(fecDev).getAnio();
		final String claveincripcAnio = claveincripc + anio;
		BigDecimal varIpcJ = null;
		
		// Calcular varIpcJ solo una vez por año, esto se controla al buscar en el mapVariables pasando claveincripc + año de la fecha devengo
		Object varIpcJObject = mapVariables.get(claveincripcAnio);
		
		// Si varIpcJObject es null, calcular varIpcJ para ese año y guardarla
		if (varIpcJObject == null) {
			final IObtenerConfiguracion servicioConfiguracion = FachadaServicios.getObtenerConfiguracion();
			final BigDecimal varIpc = servicioConfiguracion.recuperarIpcFuturo(fecDev, bt);
			ValidacionesComunesModulos.validarIpcFuturo(varIpc, fecDev);
			varIpcJ = BigDecimal.ONE.add(varIpc.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT));
			mapVariables.put(claveincripcAnio, varIpcJ);
			
			// varGtoAnual = varGtoCalculo * (1 + (varIpcJ / 100))
			salida = varGtoCalculo.multiply(varIpcJ);
			mapVariables.put(clavegtoanual, salida);
		}
		
		// Si varIpcJObject no es null, no hace falta recalcularlo para ese año, ni acumular el valor, se devuelve directamente varGtoCalculo
		else {
			salida  = varGtoCalculo;
		}
		
		return salida;
	}
	
	/**
	 * Devuelve varPctGtoProvNpp. Si no está almacenada la calcula, la setea en el Hashmap y la devuelve.
	 * @param mapVariables
	 * @param clavepctgtoprovnpp
	 * @param varPctGtoProv
	 * @param varNpp
	 * @return
	 */
	public static BigDecimal getVarPctGtoProvNPP(final Map<String, Object> mapVariables, final String clavepctgtoprovnpp, final BigDecimal varPctGtoProv, final BigDecimal varNpp) {
		final Object preSalida = mapVariables.get(clavepctgtoprovnpp);
		BigDecimal salida = null;
		if (null == preSalida) {
			salida = varPctGtoProv.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT).divide(varNpp, ConstantsFunciones.MATH_CONTEXT);
			mapVariables.put(clavepctgtoprovnpp, salida);
		} else {
			salida = (BigDecimal)preSalida;
		}
		return salida;		
	}

	/**
	 * Recupera el valor de varProxRenova para CSP362. Si no está almacenada asigna el de la fecha hasta renovación y lo devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param fechastarenova
	 * @return
	 */
	public static Timestamp getVarProxRenova(final Map<String, Object> mapVariables, final String claveVariable, final Timestamp fechastarenova) {
		return setGetTimestamp(mapVariables, claveVariable, fechastarenova);
	}
	
	/**
	 * Con independencia de que en el HashMap esté almacenada, aumenta en uno el valor de varProxRenova de CSP362, lo almacena y lo devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @return
	 */
	public static Timestamp incrementaVarProxRenova(final Map<String, Object> mapVariables, final String claveVariable) {
		Timestamp varProxRenova = (Timestamp)mapVariables.get(claveVariable);
		varProxRenova = UtilFechas.incrAnyo(varProxRenova, 1);
		mapVariables.put(claveVariable, varProxRenova);
		return varProxRenova;
	}

	/**
	 * Recupera varPUCCAPdifer. Si no está calculado le asigna el valor por defecto introducido (zero).
	 * @param mapVariables
	 * @param claveVariable
	 * @param zero
	 * @return
	 */
	public static BigDecimal getVarPUCCAPdifer(final Map<String, Object> mapVariables, final String claveVariable, final BigDecimal zero) {
		return setGetBigDecimal(mapVariables, claveVariable, zero);
	}

	/**
	 * Asigna un nuevo valor a varPUCCAPdifer, lo almacena en el HashMap y lo devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param valor
	 * @return
	 */
	public static BigDecimal setVarPUCCAPdifer(Map<String, Object> mapVariables, String claveVariable, BigDecimal valor) {
		mapVariables.put(claveVariable, valor);
		return valor;
	}

	/**
	 * Asigna un nuevo valor a varTCy1, lo asigna en el hashMap y lo devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param valor
	 * @return
	 */
	public static BigDecimal setVarTCy1(Map<String, Object> mapVariables, String claveVariable, BigDecimal valor) {
		mapVariables.put(claveVariable, valor);
		return valor;
	}
	
	/**
	 * Metodo que obtiene el valor de varFechaDevengo para el periodo actual.
	 * En el caso de que no exista lo calcula y lo setea en el hashmap.
	 * @param mapVariables
	 * @param modulo
	 * @param iteracion
	 * @param varFechaDevengo
	 * @return valorFecDev
	 */
	public static Timestamp getVarFechaDevengo(final Map<String, Object> mapVariables, final String modulo, final Integer iteracion, final Timestamp varFechaDevengo) {
		final String clave = ConstantsModulos.CTE_VAR_FEC_DEV.concat(modulo) + iteracion;
		return setGetTimestamp(mapVariables, clave, varFechaDevengo);
	}
	
	/**
	 * Metodo que obtiene el valor de la fecha devengo de una iteracion determinada
	 * 
	 * @param mapVariables
	 * @param modulo
	 * @param iteracion
	 * @return Timestamp
	 */
	public static Timestamp getFechaDevengo(final Map<String, Object> mapVariables, final String modulo, final Integer iteracion) {
		final String clave = ConstantsModulos.CTE_VAR_FEC_DEV.concat(modulo) + iteracion;
		return (Timestamp) mapVariables.get(clave);
	}

	/**
	 * Metodo que obtiene el valor de varCsp para el periodo actual.
	 * En el caso de que no exista lo calcula y lo setea en el hashmap.
	 * @param mapVariables
	 * @param claveVarCsp
	 * @param proyUmic
	 * @return
	 */
	public static BigDecimal getVarCsp(final Map<String, Object> mapVariables, final String claveVarCsp, final List<DetalleCorriente> proyUmic) {
		
		final Object preSalida = mapVariables.get(claveVarCsp);
		BigDecimal salida = null;
		
		if (null == preSalida) {
			salida = proyUmic.get(proyUmic.size() - 1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA).getImpFlujoNominal();
			mapVariables.put(claveVarCsp, salida);
		} else {
			salida = (BigDecimal)preSalida;
		}
		
		return salida;
	}
		
	/**
	 * Función encargada de determinar si una corriente tiene fechas de pago o de devengo
	 * 
	 * @param lstDetalleCorriente
	 * @param subProceso
	 * @return
	 */
	public static boolean comprobarProyeccionConFechas(final List<DetalleCorriente> lstDetalleCorriente, final String subProceso) {
		//Variables locales
		boolean tieneFechas = false;
		Timestamp fechaDevengo = null;
		Timestamp fechaPago = null;
		//Fin variables locales
		
		for(DetalleCorriente detalleActual : lstDetalleCorriente) {
			BloqueCorriente bloqueActual = detalleActual.getBloqueBySubproceso(subProceso);
			fechaDevengo = bloqueActual.getFechaDevengo();
			fechaPago = bloqueActual.getFechaPago();
			
			if (null != fechaDevengo || null != fechaPago) {
				tieneFechas = true;
				break;
			}
		}
		
		return tieneFechas;
	}
	
	/**
	 * Metodo que obtiene el valor de varBeta1 o varBeta2 para el periodo actual.
	 * En el caso de que no exista lo calcula y lo setea en el hashmap.
	 * @param mapVariables
	 * @param clave
	 * @param varBeta
	 * @param varIXBti
	 * @return
	 */
	public static BigDecimal getVarBetaX(final Map<String, Object> mapVariables, final String clave, final String varBeta, final BigDecimal varIXBti) {
		final Object preSalida = mapVariables.get(clave);
		BigDecimal salida = null;
		
		if (null == preSalida) {
			if (ConstantsModulos.CTE_CRP.equals(varBeta)) {
				salida = varIXBti.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
			} else if (ConstantsModulos.CTE_CRPI.equals(varBeta)) {
				salida = BigDecimal.ZERO;
			} 
			
//			else if (ConstantsModulos.CTE_SR.equals(varBeta)) {
//				salida = BigDecimal.ONE.add(varIXBti);
//			}
			mapVariables.put(clave, salida);
		} else {
			salida = (BigDecimal) preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Función encargada de obtener la fechaVcto.En el caso de que no exista lo calcula y lo setea en el hashmap.
	 * 
	 * @param mapVariables
	 * @param clave
	 * @param umic
	 * @param btcUmic
	 * @param varW
	 * @param varCriEdad
	 * @param claveEdadMaxima2
	 * @return
	 */
	public static Timestamp getVarFecVcto(final Map<String, Object> mapVariables, final String clave, final Umic umic, final DetalleBaseTecnica btcUmic, final Integer varW, final String varCriEdad, final String claveEdadMaxima2 ) {
		final Object preSalida = mapVariables.get(clave);
		Timestamp salida = null;
		Integer varEdadCalc1 = 0;
		Integer varEdadCalc2 = 0;
		Timestamp fFinvitalicia1 = null;
		Timestamp fFinvitalicia2 = null;
		Integer varAnoNac2 = 0;
		Integer varEdadMax2 = 0;
		Timestamp varFechaEfecto;
		
		// Si la modalidad de la UMIC que se trata es del negocio Individuales, se toma la fecha
		// de fin de efecto en lugar de la fecha de inicio. (Cambio de alcance 09/2015)
		if (umic.getDatosGenerales().getCnegocio().equals(ConstantesSolvencia.NEGOCIO_INDIVIDUAL)){
			varFechaEfecto = umic.getFechas().getFecefecini();
		} else {
			varFechaEfecto = umic.getFechas().getFecinisus();
		}			
		
		if (null == preSalida) {
			if (ConstantsModulos.CTE_RENTA_TEMPORAL.equals(umic.getRentas().getTempVit())) {
				salida = umic.getFechas().getFecefecfin();
			} else if (ConstantsModulos.CTE_RENTA_VITALICIA.equals(umic.getRentas().getTempVit())) {
				Integer varEdifer = umic.getDatosGenerales().getEdifer();
				if (null == umic.getAsegurados().getFnacAseg2()) {
					varEdadCalc1 = FuncionesAuxiliares.nEdad(varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriEdad, umic.getRentas().getFecIni(), varEdifer).intValue();
					fFinvitalicia1 = UtilFechas.incrAnyo(varFechaEfecto, varW - varEdadCalc1);
					salida = fFinvitalicia1;
				} else {
					varEdadCalc1 = FuncionesAuxiliares.nEdad(varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriEdad, umic.getRentas().getFecIni(), varEdifer).intValue();
					varEdadCalc2 = FuncionesAuxiliares.nEdad(varFechaEfecto, umic.getAsegurados().getFnacAseg2(), varCriEdad, umic.getRentas().getFecIni(), varEdifer).intValue();
					varAnoNac2 = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg2());
					if (btcUmic.getTablacalc1aseg1().equals(btcUmic.getTablacalc1aseg2())) {
						varEdadMax2 = UtilModulos.getVarEdadMaxima2(mapVariables, claveEdadMaxima2, varAnoNac2, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG2);
						ValidacionesComunesModulos.validarEdadMax(varEdadMax2, umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia());
					} else {
						varEdadMax2 = varW;
					}
					fFinvitalicia1 = UtilFechas.incrAnyo(varFechaEfecto, varW - varEdadCalc1);
					fFinvitalicia2 = UtilFechas.incrAnyo(varFechaEfecto, varEdadMax2 - varEdadCalc2);
					
					salida = UtilFechas.obtenerFechaMayor(fFinvitalicia1, fFinvitalicia2);
				}
				
			}
			mapVariables.put(clave, salida);
		} else {
			salida = (Timestamp) preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Función encargada de obtener la varAntRenova.En el caso de que no exista lo calcula y lo setea en el hashmap.
	 * 
	 * @param mapVariables
	 * @param clave
	 * @param fechaAntRenovacion
	 * @return
	 */
	public static Timestamp getVarAntRenova(final Map<String, Object> mapVariables, final String clave, final Timestamp fechaAntRenovacion) {
		final Object preSalida = mapVariables.get(clave);
		Timestamp salida = null;
		
		if (null == preSalida) {
			salida = fechaAntRenovacion;
			mapVariables.put(clave, salida);
		} else {
			salida = (Timestamp) preSalida;
		}
		
		return salida;
	}
	
	
	/**
	 * Devuelve el valor pasado a porcentaje y sumado a uno
	 * 
	 * @param num
	 * @return 1 + (num/100)
	 */
	public static BigDecimal getNumPorcentajeMasUno(final BigDecimal num, final Map<String, Object> mapVariables, final String claveModulo) {
		String clave = num+claveModulo;
		final Object preSalida = mapVariables.get(clave);
		BigDecimal salida;
		
		if (preSalida == null){
			salida = BigDecimal.ONE.add(num.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
			mapVariables.put(clave, salida);
		}else{
			salida = (BigDecimal) preSalida;
		}
		
		return salida;
	}
	/**
	 * Devuelve una lista de valores liquidativos 
	 * @param mapVariables
	 * @param claveVariable
	 * @param umic
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<ValoresLiquidativos> getValoresLiquidativos(final Map<String, Object> mapVariables, final String claveVariable, final Umic umic){
		final Object preSalida = mapVariables.get(claveVariable);
		List<ValoresLiquidativos> salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (preSalida == null){
			final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
			Long poliza = umic.getDatosGenerales().getKpoliza();
			String modalidad = umic.getDatosGenerales().getKmodalidad().toString();
			String ramo = umic.getDatosGenerales().getKramo();
			Integer certificado = umic.getDatosGenerales().getKcertificado();
			salida = servicio.recuperarValoresLiquidativos(poliza, modalidad, ramo,certificado);
			
			if(null ==  salida || salida.isEmpty()){
				salida = servicio.recuperarValoresLiquidativos(poliza, modalidad, ramo,null);
			}
			if(null ==  salida || salida.isEmpty()){
				salida = servicio.recuperarValoresLiquidativos(poliza, modalidad, ramo,new Integer(0));
			}
			
			mapVariables.put(claveVariable, salida);
		}else{
			salida = (List<ValoresLiquidativos>) preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Devuelve el factorIpc, calculado a partir de el valor ipc0Pipcgas dentro de la estructura de intereses Ipc, cuyo registro
	 * cumpla con las condiciones varIpc.ipc0Finicio  <=  proyUmic(j).varBloque. fecDesde <= varIpc.ipc0Ffin 
	 * @param mapVariables
	 * @param claveVariable
	 * @param fecDesde
	 * @return
	 */
	public static BigDecimal getFactorIpc(final Map<String, Object> mapVariables, String claveVariable, String claveVarFfin, Timestamp fecDesde ,
            final String bt){
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		BigDecimal varFactorIpc;
		Timestamp varFfin;
		IPCGeneralFuturo varIpc;
		BigDecimal varIpcJ=null;
		final IObtenerConfiguracion servicioConfiguracion = FachadaServicios.getObtenerConfiguracion();
		
		if (preSalida == null){
			/**
			 * Se buscará dentro de la estructura de intereses varIpc el registro que cumpla con las condiciones: 
			** varIpc.ipc0Finicio  <=  proyUmic(j).varBloque. fecDesde <= varIpc.ipc0Ffin
			** Para el registro así encontrado:
			** 		•	varIpcJ  = varIpc.ipc0Pipcgas 
			**		•	varFactorIpc = 1 + (varIpcJ /100)--> Se deja la variable en memoria
			**		•	varFfin = varIpc.ipc0Ffin --> Se deja la variable en memoria
			**/
			varIpc = servicioConfiguracion.recuperarFondosIpcFuturo(fecDesde, bt);
			varIpcJ = varIpc.getPipcgas();
			varFactorIpc = varIpcJ.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01).add(BigDecimal.ONE);
			varFfin = varIpc.getFfin();
			mapVariables.put(claveVarFfin, varFfin);
			mapVariables.put(claveVariable, varFactorIpc);
		}else{
			/**
			 * Si proyUmic(j).varBloque. fecDesde  > varFfin, se deberá recuperar el nuevo Ipc: 
			 */
			varFfin = (Timestamp) mapVariables.get(claveVarFfin);
			if (fecDesde.after(varFfin)){
				varIpc = servicioConfiguracion.recuperarFondosIpcFuturo(fecDesde, bt);
				varIpcJ = varIpc.getPipcgas();
				varFactorIpc = varIpcJ.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01).add(BigDecimal.ONE);
				varFfin = varIpc.getFfin();
				mapVariables.put(claveVarFfin, varFfin);
				mapVariables.put(claveVariable, varFactorIpc);
			}else{
				/**
				 * En caso contrario (proyUmic(j).varBloque. fecDesde  <= varFfin), se conserva el valor 
				 * de las variables varFactorIpc y varFfin.
				 */
				varFactorIpc = (BigDecimal) preSalida;
			}
		}
		
		salida = varFactorIpc;
		
		return salida;
	}
	
	/**
	 * Valida que la variable no sea nula. Si lo es genera la incidencia correspondiente.
	 * @param variable
	 * @param nombreVariable
	 */
	public static void validarVariable(Object variable, String nombreVariable){
		if (variable == null){
			if (UtilModulos.LOG.isDebugEnabled()) {
				UtilModulos.LOG.debug(Util.errorValidacionA2(Util.errorString(variable), nombreVariable));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A2, new String[]{Util.errorString(variable), nombreVariable});
		}
	}
	
	/**
	 * Devuelve varTCM. Si no está almacenada la calcula, la setea y la devuelve.
	 * @param mapVariables
	 * @param modulo
	 * @param iteracion
	 * @param constanteVariable
	 * @param varTCm
	 * @return
	 */
	public static Integer getVarTCmIni(final Map<String, Object> mapVariables, final String claveVariable, final Integer iteracion, final Timestamp fecinisus, final Timestamp fcalc) {
		final String clave = claveVariable + iteracion;
		final Object preSalida = mapVariables.get(clave);
		Integer salida = null;
			
		if (null == preSalida) {
			salida = FuncionesAuxiliares.tcmIni(fecinisus, fcalc);
			mapVariables.put(clave, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " para la iteración " + iteracion + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (Integer)preSalida;
		}

		return salida;
	}
	
	public static BigDecimal gerVarAjuste (final Map<String, Object> mapVariables, final String claveVariable, final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbastec){
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		if (null == preSalida){
			final IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
			salida = (BigDecimal) obtenerConfiguracion.recuperarDefinicionAuxiliar(ccartera, kmodalidad, kgarantia, kbastec, "AJUSTE");
			if (salida == null){
				salida = BigDecimal.ONE;
			}
			
			mapVariables.put(claveVariable, salida);
			
		} else {
			salida = (BigDecimal) preSalida;
		}
		
		return salida;
	}
	
	
	/**
	 * Devuelve TerminosPMCUmic. 
	 * @param clavUmicKey
	 * @param baseTec
	 */
	public static TerminosPMCUmic getTerminosPMCUmic (final UmicKey umicKey, final String baseTec, Integer iteracion) {
		TerminosPMCUmic terminosPMCsUmic;
		
		final IObtenerDatos servicio = FachadaServicios.getObtenerDatos();
		terminosPMCsUmic = servicio.recuperarTerminosPMCUmic(umicKey, baseTec, iteracion);
		
		return terminosPMCsUmic;
	}
	
	/**
	 * Recupera TerminosPMCUmic a partir de servicio.recuperarTerminosPMCUmic(umicKey, baseTec);
	 * @param umicKey
	 * @param baseTec
	 * @return
	 */
	public static TerminosPMCUmic getSetTerminosPMCUmic (final UmicKey umicKey, final String baseTec, Integer iteracion) {

		TerminosPMCUmic terminosPMCsUmic;

		terminosPMCsUmic = getTerminosPMCUmic(umicKey, baseTec, iteracion);
		
		if (terminosPMCsUmic == null){
			terminosPMCsUmic = new TerminosPMCUmic();
			terminosPMCsUmic.setBt(baseTec);
			terminosPMCsUmic.setClaveUmic(umicKey);
			terminosPMCsUmic.setIteracion(iteracion);
			final IAlmacenarDatos servicio2 = FachadaServicios.getAlmacenarDatos();
			servicio2.almacenarTerminosPMCUmic(terminosPMCsUmic);
		}
		
		return terminosPMCsUmic;
	}
	
	/**
	 * Recupera la Fecha de Efecto en base a la reducción y el negocio de la UMIC
	 * @param csitupol
	 * @param cnegocio
	 * @param fechas
	 * @return
	 */
	public static Timestamp getVarFecEfecto(final Map<String, Object> mapVariables, final String claveVariable, final DatosGenerales datosGenerales, final Fechas fechas, final BigDecimal isaldo){
		final Object preSalida = mapVariables.get(claveVariable);
		Timestamp varFechaEfecto=null;
		
		
		if (preSalida == null){
			/**
			 *  -	Si la póliza no está reducida, es decir  umic.datosGenerales.csitupol=  ‘VI’
			 *  	o	Si umic.datosGenerales.cnegocio = ‘I’ 
			 *  			varfechaEfecto = umic.fechas.fecefecIni
			 *		o	En caso contrario
			 *				varfechaEfecto = umic.fechas.fecinisus
			 *	
			 *	-	Si la póliza está reducida, es decir  umic.datosGenerales.csitupol=  ‘RE’
			 *		o	varfechaEfecto = umic.fechas.fecefecred
			 */
			
			//INI-TAR00433819			
			if (ConstantsFunciones.CTE_POL_NO_RED.equals(datosGenerales.getCsitupol())      ||
			(ConstantsFunciones.CTE_POL_PRORROGA_PU.equals(datosGenerales.getCsitupol())    ||
			(ConstantsFunciones.CTE_POL_PRORROGA_PP.equals(datosGenerales.getCsitupol()))))   {
				//FIN-TAR00433819	
				if (ConstantesSolvencia.NEGOCIO_INDIVIDUAL.equals(datosGenerales.getCnegocio())){
					varFechaEfecto = fechas.getFecefecini();
				} else {
					varFechaEfecto = fechas.getFecinisus();					
				}
				
			} else if (ConstantsFunciones.CTE_APOR_REDUCIDA.equals(datosGenerales.getCsitupol())) {
				varFechaEfecto = fechas.getFecefecred(); // este venga informado. 
				
				if (varFechaEfecto == null){
					if (ConstantesSolvencia.NEGOCIO_COLECTIVO.equals(datosGenerales.getCnegocio())){
						varFechaEfecto = fechas.getFecfinpagprim(); // se recupere otra fecha
					} else {
						//Si el negocio es individual y existe saldo, la fecha de efecto será la de inicio de efecto
						if (isaldo != null && !BigDecimal.ZERO.equals(isaldo)){
							varFechaEfecto = fechas.getFecefecini();
						} else if (ConstantsFunciones.CTE_310.equals(datosGenerales.getKgarantia())){
							//Si la UMIC es de la garantía 310 no tendrá saldo, por lo que hay que recuperar este campo de la UMIC principal
							UmicKey claveP = UtilUmicPrincipal.getUmicPrincipal(mapVariables, ConstantsModulos.CLAVE_VAR_CLAVEPRINCIPAL, datosGenerales);
							BigDecimal isaldoP = getIsaldo(claveP);
							if (isaldoP != null && !BigDecimal.ZERO.equals(isaldoP)){
								varFechaEfecto = fechas.getFecefecini();
							} else {
								throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AT);
							}
							
						} else {
							throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AT);
						}
					}
				}
			}
			
			mapVariables.put(claveVariable, varFechaEfecto);
			
		} else {
			varFechaEfecto = (Timestamp) preSalida;
		}
		
		return varFechaEfecto;
	}
	
	public static Timestamp getVarFecEfectoGBT(final String claveVariable, final DatosGenerales datosGenerales, final Fechas fechas, final BigDecimal isaldo){
		Timestamp varFechaEfecto=null;
		
		
		
			/**
			 *  -	Si la póliza no está reducida, es decir  umic.datosGenerales.csitupol=  ‘VI’
			 *  	o	Si umic.datosGenerales.cnegocio = ‘I’ 
			 *  			varfechaEfecto = umic.fechas.fecefecIni
			 *		o	En caso contrario
			 *				varfechaEfecto = umic.fechas.fecinisus
			 *	
			 *	-	Si la póliza está reducida, es decir  umic.datosGenerales.csitupol=  ‘RE’
			 *		o	varfechaEfecto = umic.fechas.fecefecred
			 */
			
			if (ConstantsFunciones.CTE_POL_NO_RED.equals(datosGenerales.getCsitupol())) {
				if (ConstantesSolvencia.NEGOCIO_INDIVIDUAL.equals(datosGenerales.getCnegocio())){
					varFechaEfecto = fechas.getFecefecini();
				} else {
					varFechaEfecto = fechas.getFecinisus();					
				}
				
			} else if (ConstantsFunciones.CTE_APOR_REDUCIDA.equals(datosGenerales.getCsitupol())) {
				varFechaEfecto = fechas.getFecefecred();
				
				if (varFechaEfecto == null){
					if (ConstantesSolvencia.NEGOCIO_COLECTIVO.equals(datosGenerales.getCnegocio())){
						varFechaEfecto = fechas.getFecfinpagprim();
					} else {
						//Si el negocio es individual y existe saldo, la fecha de efecto será la de inicio de efecto
						if (isaldo != null && !BigDecimal.ZERO.equals(isaldo)){
							varFechaEfecto = fechas.getFecefecini();
						} else if (ConstantsFunciones.CTE_310.equals(datosGenerales.getKgarantia())){
							//Si la UMIC es de la garantía 310 no tendrá saldo, por lo que hay que recuperar este campo de la UMIC principal
							UmicKey claveP = UtilUmicPrincipal.getUmicPrincipalGBT(ConstantsModulos.CLAVE_VAR_CLAVEPRINCIPAL, datosGenerales);
							BigDecimal isaldoP = getIsaldo(claveP);
							if (isaldoP != null && !BigDecimal.ZERO.equals(isaldoP)){
								varFechaEfecto = fechas.getFecefecini();
							} else {
								throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AT);
							}
							
						} else {
							throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AT);
						}
					}
				}
			}
			
		return varFechaEfecto;
	}
	
	/**
	 * Recupera la Fecha de Efecto en base a la reducción y el negocio de la UMIC para caso primas flexibles 
	 * @param csitupol
	 * @param cnegocio
	 * @param fechas
	 * @return
	 */
	public static Timestamp getVarFecEfectoPrimas(final Map<String, Object> mapVariables, final String claveVariable, final DatosGenerales datosGenerales, final Fechas fechas, final BigDecimal isaldo){
		final Object preSalida = mapVariables.get(claveVariable);
		Timestamp varFechaEfecto=null;
		
		
		if (preSalida == null){
			
			if (ConstantsFunciones.CTE_POL_NO_RED.equals(datosGenerales.getCsitupol())) {
				if (ConstantesSolvencia.NEGOCIO_INDIVIDUAL.equals(datosGenerales.getCnegocio())){
					varFechaEfecto = fechas.getFecefecini();
				} else {
					if (datosGenerales.getTipoflexprim() == null) {
						varFechaEfecto = fechas.getFecinisus();
					} else {
						varFechaEfecto = fechas.getFecinipagprim();
					}					
				}
				
			} else if (ConstantsFunciones.CTE_APOR_REDUCIDA.equals(datosGenerales.getCsitupol())) {
				varFechaEfecto = fechas.getFecefecred();
				
				if (varFechaEfecto == null){
					if (ConstantesSolvencia.NEGOCIO_COLECTIVO.equals(datosGenerales.getCnegocio())){
						varFechaEfecto = fechas.getFecfinpagprim();
					} else {
						//Si el negocio es individual y existe saldo, la fecha de efecto será la de inicio de efecto
						if (isaldo != null && !BigDecimal.ZERO.equals(isaldo)){
							varFechaEfecto = fechas.getFecefecini();
						} else if (ConstantsFunciones.CTE_310.equals(datosGenerales.getKgarantia())){
							//Si la UMIC es de la garantía 310 no tendrá saldo, por lo que hay que recuperar este campo de la UMIC principal
							UmicKey claveP = UtilUmicPrincipal.getUmicPrincipal(mapVariables, ConstantsModulos.CLAVE_VAR_CLAVEPRINCIPAL, datosGenerales);
							BigDecimal isaldoP = getIsaldo(claveP);
							if (isaldoP != null && !BigDecimal.ZERO.equals(isaldoP)){
								varFechaEfecto = fechas.getFecefecini();
							} else {
								throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AT);
							}
							
						} else {
							throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AT);
						}
					}
				}
			}
			
			mapVariables.put(claveVariable, varFechaEfecto);
			
		} else {
			varFechaEfecto = (Timestamp) preSalida;
		}
		
		return varFechaEfecto;
	}
	
	
	
	/**
	 * Clona las estructuras de datos Umic y DetalleBaseTecnica con los datos del segundo asegurado en los campos del primer asegurado
	 * @param mapVariables
	 * @param claveUmic
	 * @param claveBtcumic
	 * @param umic
	 * @param btcUmic
	 */
	public static void clonarUmicBtcumic(final Map<String, Object> mapVariables, final String claveUmic, final String claveBtcumic, 
			final Umic umic, final DetalleBaseTecnica btcUmic){
		
		Umic varUmic2;
		DetalleBaseTecnica varBtcUmic2;
		
		varUmic2 = new Umic();
		varBtcUmic2 = new DetalleBaseTecnica();
		try {
			//Se clona la Umic y la btcUmic para asignar los datos del asegurado2 al asegurado1 de la nueva Umic 
			// y btcUmic (con este método no se clonan las listas ni los sub-objetos)
			PropertyUtils.copyProperties(varUmic2, umic);
			PropertyUtils.copyProperties(varBtcUmic2, btcUmic);
		} catch (Exception e) {
			UtilModulos.LOG.error(e.getMessage());
		}
		
		// Se asignan los datos del asegurado2 al asegurado1 de la nueva Umic y btcUmic  
		Asegurados asegurados2 = new Asegurados();
		OtrosDatos od2 = new OtrosDatos();
		asegurados2.setFnacAseg1(umic.getAsegurados().getFnacAseg2());
		asegurados2.setCsexAseg1(umic.getAsegurados().getCsexAseg2());
		asegurados2.setEdadAseg1(umic.getAsegurados().getEdadAseg2());
		od2.setCestadoAseg1(umic.getOtrosDatos().getCestadoAseg2());
//INI-TAR00400971: Se graban los datos de intereses
		od2.setSigpitertecn1((umic.getOtrosDatos().getSigpitertecn1()));
		od2.setSigpitertecn2((umic.getOtrosDatos().getSigpitertecn2()));
		od2.setSigpitertecn3((umic.getOtrosDatos().getSigpitertecn3()));
		od2.setSigpitertecn4((umic.getOtrosDatos().getSigpitertecn4()));
		od2.setSigpitertecn5((umic.getOtrosDatos().getSigpitertecn5()));
		od2.setPregrupo(umic.getOtrosDatos().getPregrupo());
		od2.setIndinval(umic.getOtrosDatos().getIndinval());
//FIN-TAR00400971:
		varUmic2.setAsegurados(asegurados2);
		varUmic2.setOtrosDatos(od2);
		
		varBtcUmic2.setTablacalc1aseg1(btcUmic.getTablacalc1aseg2());
		
		if ((!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL))  && 
				(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA)) && 
				(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR)) && 
				(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTIU)) && 
				(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTID)) && 
				(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRGTO)) && 
				(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMFE)) && 
				(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMMI)) && 
				(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCF)) && 
				(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCI)) && 
				(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLFE)) && 
				(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLMI)) && 
				(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC)) && 
				(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRVM))  && 
				(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEP)) && 
				(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEN)) && 
				(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIP)) && 
				(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIN)) && 
				(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM))
				&& (!btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIF17LIR)) 
				&& (!btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_N17LIRIN)) 
				&& (!btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIFF17OCI)) 
				&& (!btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17IF))
				&& (!btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_N17CLIR)) 
				&& (!btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17))
				&& (!btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NF17MFE))
				&& (!btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NF17GTO))
				&& (!btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NF17AEN))){
			if (btcUmic.getTablasConversionAsegurado() == null || btcUmic.getTablasConversionAsegurado().isEmpty()){
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC, new String[]{null, "tablasConversionAsegurado"});
			}
			
			List<List<TablaConversion>> tablaConvAseg = new ArrayList<List<TablaConversion>>();
			tablaConvAseg.add(btcUmic.getTablasConversionAsegurado().get(1));

			varBtcUmic2.setTablasConversionAsegurado(tablaConvAseg);
		} else {
			List<Integer> tablaBaseExp = new ArrayList<Integer>();
			tablaBaseExp.add(btcUmic.getTablaBaseExp().get(1));
			varBtcUmic2.setTablaBaseExp(tablaBaseExp);
			List<List<Integer>> tablaBaseExpList = new ArrayList<List<Integer>>();
			tablaBaseExpList.add(btcUmic.getTablaBaseExpList().get(1));
			varBtcUmic2.setTablaBaseExpList(tablaBaseExpList);
		}
		
		
		//Se almacena umic2 y varUmic2 (solo es necesario clonar y asignar los datos la primera vez)
		mapVariables.put(claveUmic, varUmic2);
		mapVariables.put(claveBtcumic, varBtcUmic2);
		
	}
	
	public static BigDecimal getGastos310 (final Map<String, Object> mapVariables, final String claveVariable, final Integer kmodalidad, final Long kpoliza, final Integer ksubpoliza, 
			final Integer kcertificado, final Integer nsuscri, final String ctipoaport){
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		if (null == preSalida){
			final IObtenerDatos obtenerDatos = FachadaServicios.getObtenerDatos();
			Umic umic310 = obtenerDatos.recuperarUmic310(kmodalidad, kpoliza, ksubpoliza, kcertificado, nsuscri, ctipoaport);

			if (umic310 != null){
				salida = umic310.getBti().getPgastgesex1I().add(umic310.getBti().getPgastgesin1I()).add(umic310.getBti().getPgastgesin2I());
				salida = salida.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
			} else {
				salida = BigDecimal.ZERO;
			}

			mapVariables.put(claveVariable, salida);
			
		} else {
			salida = (BigDecimal) preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Recupera Var Diferimiento y la almacena en el mapa
	 * @param mapVariables
	 * @param claveVariable
	 * @param kpoliza
	 * @param ksubpoliza
	 * @param kcertificado
	 * @return
	 */
	public static Timestamp gerVarDiferimiento (final Map<String, Object> mapVariables, final String claveVariable, final Long kpoliza, final Integer ksubpoliza, final Integer kcertificado, final Integer nsuscripcion){
		final Object preSalida = mapVariables.get(claveVariable);
		Timestamp salida = null;
		
		if (null == preSalida){
			final IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
			salida = (Timestamp) obtenerConfiguracion.recuperarVarFdiferimiento(kpoliza, ksubpoliza, kcertificado, nsuscripcion);

			mapVariables.put(claveVariable, salida);
			
		} else {
			salida = (Timestamp) preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Recupera E1PSJ y la almacena en el mapa
	 * @param mapVariables
	 * @param claveVariable
	 * @param kpoliza
	 * @param ksubpoliza
	 * @param kcertificado
	 * @return
	 */
	public static BigDecimal getVarE1PSJ(final Map<String, Object> mapVariables, final String claveVariable, final Long kpoliza, final Integer ksubpoliza, final Integer kcertificado, final Integer nsuscripcion){
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		if (null == preSalida){
			final IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
			salida = (BigDecimal) obtenerConfiguracion.recuperarVarE1PSJ(kpoliza, ksubpoliza, kcertificado, nsuscripcion);

			mapVariables.put(claveVariable, salida);
			
		} else {
			salida = (BigDecimal) preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Recupera VZC2 y la almacena en el mapa
	 * @param mapVariables
	 * @param claveVariable
	 * @param kpoliza
	 * @param ksubpoliza
	 * @param kcertificado
	 * @return
	 */
	public static BigDecimal getVarVZC2(final Map<String, Object> mapVariables, final String claveVariable, final Long kpoliza, final Integer ksubpoliza, final Integer kcertificado, final Integer nsuscripcion){
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		if (null == preSalida){
			final IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
			salida = (BigDecimal) obtenerConfiguracion.recuperarVarVZC2(kpoliza, ksubpoliza, kcertificado, nsuscripcion);

			mapVariables.put(claveVariable, salida);
			
		} else {
			salida = (BigDecimal) preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Recupera Tabla2000 y la almacena en el mapa
	 * @param mapVariables
	 * @param claveVariable
	 * @param codigo
	 * @return
	 */
	public static BigDecimal getVarTabla2000(final Map<String, Object> mapVariables, final String claveVariable, final String codigo){
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
        String codigo_entrada = ConstantsFunciones.CTE_PREF_COD_T2000 + String.format(ConstantsFunciones.CTE_FORMAT_COD_T2000, Integer.parseInt(codigo));
        
		if (null == preSalida){
			final IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
			salida = (BigDecimal) obtenerConfiguracion.recuperarVarTabla2000(codigo_entrada);
			
			mapVariables.put(claveVariable, salida);
			
		} else {
			salida = (BigDecimal) preSalida;
		}
		
		return salida;
	}
	/**
	 * Recupera E3SAL y la almacena en el mapa
	 * @param mapVariables
	 * @param claveVariable
	 * @param kpoliza
	 * @param ksubpoliza
	 * @param kcertificado
	 * @return
	 */
	public static BigDecimal getVarE3SAL(final Map<String, Object> mapVariables, final String claveVariable, final Long kpoliza, final Integer ksubpoliza, final Integer kcertificado, final Integer nsuscripcion){
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		if (null == preSalida){
			final IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
			salida = (BigDecimal) obtenerConfiguracion.recuperarVarE3SAL(kpoliza, ksubpoliza, kcertificado, nsuscripcion);
			
			mapVariables.put(claveVariable, salida);
			
		} else {
			salida = (BigDecimal) preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Recupera E1PSJ y la almacena en el mapa
	 * @param mapVariables
	 * @param claveVariable
	 * @param kpoliza
	 * @param ksubpoliza
	 * @param kcertificado
	 * @return
	 */
	public static String getVarC2S(final Map<String, Object> mapVariables, final String claveVariable, final Long kpoliza, final Integer ksubpoliza, final Integer kcertificado, final Integer nsuscripcion){
		final Object preSalida = mapVariables.get(claveVariable);
		String salida = null;
		
		if (null == preSalida){
			final IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
			salida = (String) obtenerConfiguracion.recuperarVarC2S(kpoliza, ksubpoliza, kcertificado, nsuscripcion);

			mapVariables.put(claveVariable, salida);
			
		} else {
			salida = (String) preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Recupera PRPSS y la almacena en el mapa
	 * @param mapVariables
	 * @param claveVariable
	 * @param kpoliza
	 * @param ksubpoliza
	 * @param kcertificado
	 * @return
	 */
	public static BigDecimal getVarPrpss(final Map<String, Object> mapVariables, final String claveVariable, final Long kpoliza, final Integer ksubpoliza, final Integer kcertificado, final Integer nsuscripcion){
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		if (null == preSalida){
			final IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
			salida = (BigDecimal) obtenerConfiguracion.recuperarVarPrpss(kpoliza, ksubpoliza, kcertificado, nsuscripcion);

			mapVariables.put(claveVariable, salida);
			
		} else {
			salida = (BigDecimal) preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Recupera ANOESP y la almacena en el mapa
	 * @param mapVariables
	 * @param claveVariable
	 * @param kpoliza
	 * @param ksubpoliza
	 * @param kcertificado
	 * @return
	 */
	public static BigDecimal getVarAnoEsp(final Map<String, Object> mapVariables, final String claveVariable, final Long kpoliza, final Integer ksubpoliza, final Integer kcertificado, final Integer nsuscripcion){
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		if (null == preSalida){
			final IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
			salida = (BigDecimal) obtenerConfiguracion.recuperarVarAnoEsp(kpoliza, ksubpoliza, kcertificado, nsuscripcion);

			mapVariables.put(claveVariable, salida);
			
		} else {
			salida = (BigDecimal) preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Recupera PORVIUSS y la almacena en el mapa
	 * @param mapVariables
	 * @param claveVariable
	 * @param kpoliza
	 * @param ksubpoliza
	 * @param kcertificado
	 * @return
	 */
	public static BigDecimal getVarPorviuss(final Map<String, Object> mapVariables, final String claveVariable, final Long kpoliza, final Integer ksubpoliza, final Integer kcertificado, final Integer nsuscripcion){
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		if (null == preSalida){
			final IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
			salida = (BigDecimal) obtenerConfiguracion.recuperarVarPorvius(kpoliza, ksubpoliza, kcertificado, nsuscripcion);

			mapVariables.put(claveVariable, salida);
			
		} else {
			salida = (BigDecimal) preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Recupera el campo  ISALDO de la UMIC cuya clave se pasa por parámetro
	 * @param mapVariables
	 * @param claveVariable
	 * @param umicKey
	 * @return
	 */
	public static BigDecimal getIsaldo (final UmicKey umicKey){
		BigDecimal salida = null;

		final IObtenerDatos obtenerDatos = FachadaServicios.getObtenerDatos();
		Umic umic = (Umic) obtenerDatos.recuperarUmic(umicKey);
		if (umic == null){
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AV, 
					new String[]{umicKey.getKmodalidad().toString(), umicKey.getKpoliza().toString(), umicKey.getKsubpoliza().toString(), umicKey.getKcertificado().toString(), umicKey.getNsuscri().toString()});
		}
		
		salida = umic.getCapitales().getIsaldo();
			
		
		return salida;
	}
	
	public static Boolean getCalcularSiReducidaPRI(final String csitupol, final String cnegocio, final Fechas fechas, final Timestamp fecDesde){
		
		Timestamp fechaReduccion;
		
		if (ConstantsFunciones.CTE_APOR_REDUCIDA.equals(csitupol)) {
			fechaReduccion = fechas.getFecefecred();
			
			if (fechaReduccion == null){
				if (ConstantesSolvencia.NEGOCIO_COLECTIVO.equals(cnegocio)){
					fechaReduccion = fechas.getFecfinpagprim();
				} else {
					//TODO: excepcion
				}
				
			}
		} else {
			return true;
		}
		
		if (fechaReduccion.before(fecDesde)){
			return false;
		} else {
			return true;
		}
		
	}
	
	public static Boolean compararTresFechasMenorIgual (final Timestamp fechaMenor, final Timestamp fechaMedio, final Timestamp fechaMayor){
		return ((fechaMenor.before(fechaMedio) || fechaMenor.equals(fechaMedio)) &&
				(fechaMedio.before(fechaMayor) || fechaMedio.equals(fechaMayor)));
	}
	
	/**
	 * Recupera el valor de la variable varDenominador
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param numero1
	 * @param numero2
	 * @param numero3
	 * @return
	 */
	
	public static BigDecimal getVarDenominador(final Map<String, Object> mapVariables, final String claveVariable, final BigDecimal numero1, final BigDecimal numero2, final BigDecimal numero3){
		final BigDecimal preSalida = (BigDecimal)mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		if (null == preSalida) {
			salida = numero1.subtract(numero2).subtract(numero3);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
			
		} else {
			salida = preSalida;
		}

		return salida;
	}
	
	/**
	 * Recupera el valor de la variable varFactorI1
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param suma1
	 * @param suma2
	 * @param potencia
	 * @return
	 */
	public static BigDecimal getVarFactorI1(final Map<String, Object> mapVariables, final String claveVariable, final BigDecimal suma1, final BigDecimal suma2, final BigDecimal potencia){
		final BigDecimal preSalida = (BigDecimal) mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		if (null == preSalida) {
			salida = Util.pow(suma1.add(suma2), potencia);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
			
		} else {
			salida = preSalida;
		}
		return salida;
	}
	
	/**
	 * Recupera el valor de la variable varXRenoHasta
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param varX
	 * @param varTcmZc
	 * @param varTcmRenoHasta
	 * @param varP
	 * @return
	 */
	public static BigDecimal getVarXRenoHasta(final Map<String, Object> mapVariables   , 
			                                  final String              claveVariable  , 
			                                  final BigDecimal          varX           , 
			                                  final BigDecimal          varTcmZc       , 
			                                  final BigDecimal          varTcmRenoHasta, 
			                                  final BigDecimal          varP           ){
		final BigDecimal preSalida = (BigDecimal) mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		if (null == preSalida) {
			salida = varX.add(new BigDecimal(varTcmZc.add(varTcmRenoHasta).divide(varP, ConstantsFunciones.MATH_CONTEXT).intValue()));
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
			
		} else {
			salida = preSalida;
		}
		return salida;
	}
	
	/**
	 * Recupera el valor de la variable varFactorRiesgo
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param numero
	 * @return
	 */
	public static BigDecimal getVarFactorRiesgo(final Map<String, Object> mapVariables , 
			                                    final String              claveVariable, 
			                                    final BigDecimal          numero       ){
		final BigDecimal preSalida = (BigDecimal) mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		if (null == preSalida) {
			salida = numero.add(BigDecimal.ONE);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
			
		} else {
			salida = preSalida;
		}
		return salida;
	}
	
	/**
	 * Recupera el valor de la variable varFechaEfecto
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param umic
	 * @return
	 */
	public static Timestamp getVarFechaEfectoSimple (final Map<String, Object> mapVariables , 
                                               final String              claveVariable,
                                               final Umic                umic         ){
		final Timestamp preSalida = (Timestamp) mapVariables.get(claveVariable);
		Timestamp salida = null; 
		
		if (null == preSalida) {
				if (umic.getDatosGenerales().getCnegocio().equals(ConstantsModulos.CTE_NEGOCIO_INDIVIDUAL)){
					salida = umic.getFechas().getFecefecini();
				} else {
					salida = umic.getFechas().getFecinisus();
				}
			mapVariables.put(claveVariable, salida);
		} else {
			salida = preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Recupera el valor de la variable varGF
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param numero
	 * @return
	 */
	public static BigDecimal getVarGF (final Map<String, Object> mapVariables , 
                                       final String              claveVariable, 
                                       final BigDecimal          numero       ){
		return setGetBigDecimal(mapVariables, claveVariable, numero);
	}
	
	/**
	 * Recupera el valor de la variable varFechaEfecto
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param umic
	 * @return
	 */
	public static Timestamp getVarFechaEfecto (final Map<String, Object> mapVariables , 
                                               final String              claveVariable,
                                               final Umic                umic         ){
		final Timestamp preSalida = (Timestamp) mapVariables.get(claveVariable);
		Timestamp salida = null; 
		
		if (null == preSalida) {
			if (umic.getDatosGenerales().getCsitupol().equals(ConstantsFunciones.CTE_POL_NO_RED)){
				if (umic.getDatosGenerales().getCnegocio().equals(ConstantsModulos.CTE_NEGOCIO_INDIVIDUAL)){
					salida = umic.getFechas().getFecefecini();
				} else {
					salida = umic.getFechas().getFecinisus();
				}
			} else if (umic.getDatosGenerales().getCsitupol().equals(ConstantsFunciones.CTE_APOR_REDUCIDA)) {
				salida = umic.getFechas().getFecefecred();
				if (salida.equals(null) && umic.getDatosGenerales().getCnegocio().equals(ConstantsModulos.CTE_NEGOCIO_COLECTIVO)){
					salida = umic.getFechas().getFecfinpagprim();
				}
			}
			mapVariables.put(claveVariable, salida);
		} else {
			salida = preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Recupera el valor de la variable varRentgeo2it
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param x
	 * @param t
	 * @param n
	 * @param prg
	 * @param lstValoresTabMort
	 * @param m
	 * @param unoMasI1Entre100
	 * @param unoMasI2Entre100
	 * @return
	 */
	public static BigDecimal getVarRentgeo2it(final Map<String, Object> mapVariables     , 
                                              final String              claveVariable    , 
                                              final BigDecimal          x                , 
                                              final Integer 		     t                , 
                                              final Integer             n                , 
                                              final BigDecimal          prg              , 
                                              final List<BigDecimal>    lstValoresTabMort, 
                                              final BigDecimal          m                , 
                                              final BigDecimal          unoMasI1Entre100 , 
                                              final BigDecimal          unoMasI2Entre100 ){
		final BigDecimal preSalida = (BigDecimal) mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		if (null == preSalida) {
			salida = FuncionesRentas.rentgeo2it(x, t, n, prg, lstValoresTabMort, m, unoMasI1Entre100, unoMasI2Entre100, mapVariables);
			mapVariables.put(claveVariable, salida);
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = preSalida;
		}
		return salida;
	}
	
	/**
	 * Recupera el valor de la variable varIpc
	 * @param mapVariables
	 * @param claveVariable
	 * @param fechaCierre
	 * @return
	 */
	public static IPCGeneralFuturo getVarIpc (final Map<String, Object> mapVariables     , 
                                              final String              claveVariable    ,
                                              final Timestamp           fechaCierre      ,
                                              final String              bt               ){
		final IPCGeneralFuturo presalida = (IPCGeneralFuturo) mapVariables.get(claveVariable);
		IPCGeneralFuturo salida = null;
		
		if (null == presalida) {
			final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
			salida = servicio.recuperarFondosIpcFuturo(fechaCierre, bt);
			mapVariables.put(claveVariable, salida);
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = presalida;
		}
		
		return salida;
	}
	/**
	* Recupera el valor del modulo a que se asocia la peticion recuperarModulo.
	 * @param mapVariables
	 * @param claveVariable
	 * @param umic
	 * @param btcUmic
	 * @param PROY
	 * @param Number
	 * @return
	 */
	public static String getVarModulo (final Map<String, Object> mapVariables, 
			final String              claveVariable    ,
			final Umic umic, final DetalleBaseTecnica btcUmic, final String PROY, final String Number){
		
		final Object presalida = mapVariables.get(claveVariable);
		String salida = null;

		if (null == presalida) {
			final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
			salida = servicio.recuperarModulo(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), umic.getDatosAdicionales().getPrestCal(), btcUmic.getBt(), PROY, Number);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (String) presalida;
		}

		return salida;
	}		
	
	/**
	 * Recupera el valor de la variable varCSP011
	 * @param mapVariables
	 * @param claveVariable
	 * @param icapact
	 * @return
	 */
	public static BigDecimal getVarCSP011 (final Map<String, Object> mapVariables , 
			                               final String              claveVariable,
			                               final BigDecimal          icapact      ){
		final BigDecimal presalida = (BigDecimal) mapVariables.get(claveVariable);
		BigDecimal salida = null;

		if (null == presalida) {
			salida = icapact;
			mapVariables.put(claveVariable, salida);
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = presalida;
		}

		return salida;
	}
	/**
	 * Recupera el valor de la variable varMForpagrent
	 * @param mapVariables
	 * @param claveVariable
	 * @param umic
	 * @return
	 */
	public static Integer getVarMForpagrent(final Map<String, Object> mapVariables , 
			                                final String              claveVariable, 
			                                final Umic                umic         ){
		
		final Integer presalida = (Integer) mapVariables.get(claveVariable);
		Integer salida = null;
		
		if (null == presalida){
			Integer varForpagrent = umic.getRentas().getForpagrent();
			if (!varForpagrent.equals(null) &&
				!varForpagrent.equals(ConstantsFunciones.CTE_0)){
					salida = umic.getRentas().getForpagrent();
			} else {
				Map<Integer, Integer> fp = ConstantsFunciones.FORPAGRENT;
				salida = fp.get(Integer.parseInt(umic.getRentas().getCpagrenta()));
			}
			mapVariables.put(claveVariable, salida);
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = presalida;
		}
		
		return salida;
		
	}

	/**
	 * Recupera el valor de la variable getVarMperGaran
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param varNperGaran
	 * @param varM
	 * @return
	 */
	public static BigDecimal getVarMperGaran(final Map<String, Object> mapVariables , 
			                                 final String              claveVariable,
			                                 final BigDecimal          varNperGaran , 
			                                 final Integer             varM         ) {
		BigDecimal presalida = (BigDecimal) mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		if (null == presalida){
			salida = varNperGaran.subtract(BigDecimal.ONE).multiply(ConstantsFunciones.CTE_OPER_12.divide(new BigDecimal(varM)));
			mapVariables.put(claveVariable, salida);
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = presalida;
		}
		
		return salida ;
	}

	/**
	 * Recupera el valor de la variable getVarNperGaran
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param varIprimanetaIni
	 * @param varRentIni
	 * @param varM
	 * @return
	 */
	public static BigDecimal getVarNperGaran(final Map<String, Object> mapVariables    , 
			                                 final String              claveVariable   ,
			                                 final BigDecimal          varIprimanetaIni, 
			                                 final BigDecimal          varRentIni      , 
			                                 final Integer             varM            ) {
		BigDecimal presalida = (BigDecimal) mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		if (null == presalida){
			salida = varIprimanetaIni.divide(varRentIni.multiply(ConstantsFunciones.CTE_OPER_12.divide(new BigDecimal(varM))));
			mapVariables.put(claveVariable, salida);
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = presalida;
		}
		
		return salida;
	}
	/**
	 * Recupera el valor de la variable getVarFecIni
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param umic
	 * @param varFechaEfecto
	 * @param varM
	 * @return
	 */
	public static Timestamp getVarFecIni(final Map<String, Object> mapVariables  , 
			                             final String              claveVariable , 
			                             final Umic                umic          ,
			                             final Timestamp           varFechaEfecto, 
			                             final Integer             varM          ) {
		
		Timestamp presalida = (Timestamp) mapVariables.get(claveVariable);
		Timestamp salida = null;
		
		if (null == presalida){
			if(null != umic.getRentas().getFecIni()){
				salida = umic.getRentas().getFecIni();
			} else {
				salida = UtilFechas.incrMeses(varFechaEfecto, null, 12/varM, false);
			}
			mapVariables.put(claveVariable, salida);
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = presalida;
		}
		
		return salida;
	}
	
	/**
	 * Incrementar los meses de una fecha guardando la variable
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param fecha
	 * @param fechaModelo
	 * @param meses
	 * @param trasladarAFinalDeMes
	 * @return
	 */
	public static Timestamp incrementarMeses (final Map<String, Object> mapVariables        , 
            						          final String              claveVariable       , 
                                              final Timestamp           fecha               ,
                                              final Timestamp           fechaModelo         ,
                                              final int                 meses               ,
                                              final boolean             trasladarAFinalDeMes){
		Timestamp presalida = (Timestamp) mapVariables.get(claveVariable);
		Timestamp salida = null;
		
		if (null == presalida){
			salida = UtilFechas.incrMeses(fecha, fechaModelo, meses, trasladarAFinalDeMes);
			mapVariables.put(claveVariable, salida);
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = presalida;
		}
		
		return salida;
	}
	/**
	 * Recupera el valor de los cuadros de amortizacion para una determianda umic
	 * @param mapVariables
	 * @param claveVariable
	 * @param umic
	 * @return
	 */
	public static CuadrosAmortizacion getVarCuadroAmortizacion(final Map<String, Object> mapVariables, final String claveVariable, final Umic umic) {
		final Object preSalida = mapVariables.get(claveVariable);
		CuadrosAmortizacion salida = null;
		
		//Siempre guardamos este valor
		if (null == preSalida) {
			final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
			salida = servicio.recuperarCuadrosAmortizacion(umic);
			mapVariables.put(claveVariable, salida);
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
				
		} else {
			salida = (CuadrosAmortizacion)preSalida;
		}
		
		return salida;
	}
	
	
	/**
	 * Recupera el valor de la variable getVarN
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param umic
	 * @return
	 */
	public static Integer getVarN (final Map<String, Object> mapVariables , 
			                         final String              claveVariable,
			                         final Umic                umic         ){
		final Integer preSalida = (Integer) mapVariables.get(claveVariable);
		Integer salida = null; 

		if (null == preSalida) {
			if (umic.getDatosGenerales().getCsitupol().equals(ConstantsFunciones.CTE_POL_NO_RED)){
				salida = umic.getDuraciones().getNdursegano();
			} else if (umic.getDatosGenerales().getCsitupol().equals(ConstantsFunciones.CTE_APOR_REDUCIDA)) {
				if (umic.getDatosGenerales().getCnegocio().equals(ConstantsModulos.CTE_NEGOCIO_INDIVIDUAL)){
					salida = umic.getDuraciones().getNdursegano() - FuncionesAuxiliares.tc(umic.getFechas().getFecefecini(), umic.getFechas().getFecefecred());
				} else {
					salida = umic.getDuraciones().getNdursegano() - FuncionesAuxiliares.tc(umic.getFechas().getFecinisus(), umic.getFechas().getFecefecred());
				}
			}
			mapVariables.put(claveVariable, salida);
		} else {
			salida = preSalida;
		}

		return salida;
	}
	
	/**
	 * Recupera el valor de la variable varFactorGF
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param varFactorGF
	 * @return
	 */
	public static BigDecimal getVarFactorGF (final Map<String, Object> mapVariables , 
			                                 final String              claveVariable,
			                                 final BigDecimal          varFactorGF  ){
		final BigDecimal preSalida = (BigDecimal) mapVariables.get(claveVariable);
		BigDecimal salida = null; 

		if (null == preSalida) {
			salida = varFactorGF.divide(ConstantsFunciones.CTE_OPER_12, ConstantsFunciones.MATH_CONTEXT);
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
			mapVariables.put(claveVariable, salida);
		} else {
			salida = preSalida;
		}

		return salida;
	}
	
	/**
	 * Recupera el valor de la variable varVRTA
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param varFactorGF
	 * @param mesesCompletos 
	 * @param diferRenta 
	 * @param duracionRenta 
	 * @param tipoIPrimerTramo 
	 * @param duracionPrimerTra 
	 * @param tipoISegundoTramo 
	 * @param edadActuarial 
	 * @param lstValoresTabMort 
	 * @param numPagos 
	 * @param porcenRevalRen 
	 * @param indUL 
	 * @param nmeses 
	 * @return
	 */
	public static BigDecimal getVarVRTA (final Map<String, Object> mapVariables , 
			                             final String              claveVariable,
			                             final Integer             mesesCompletos, 
			                             final Integer             diferRenta, 
			                             final Integer             duracionRenta, 
			                             final BigDecimal          tipoIPrimerTramo, 
			                             final Integer             duracionPrimerTra, 
			                             final BigDecimal          tipoISegundoTramo, 
			                             final Integer             edadActuarial, 
			                             final List<BigDecimal>    lstValoresTabMort, 
			                             final Integer             numPagos, 
			                             final BigDecimal          porcenRevalRen, 
			                             final boolean             indUL, 
			                             final Integer             nmeses    ){
		final BigDecimal preSalida = (BigDecimal) mapVariables.get(claveVariable);
		BigDecimal salida = null; 

		if (null == preSalida) {
			salida = FuncionesRentas.vrta(mesesCompletos, diferRenta, duracionRenta, tipoIPrimerTramo, duracionPrimerTra, tipoISegundoTramo, edadActuarial, lstValoresTabMort, numPagos, porcenRevalRen, indUL, nmeses);
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
			mapVariables.put(claveVariable, salida);
		} else {
			salida = preSalida;
		} 

		return salida;
	}

	/**
	 * Recupera el valor de la variable varFactorVRTA para el m�dulo GZCAMORT
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param varGipc
	 * @param varPrima
	 * @param varVRTA
	 * @return
	 */
	public static BigDecimal getVarFactorVrtaGZCAMORT(final Map<String, Object> mapVariables , 
                                                      final String              claveVariable, 
                                                      final BigDecimal          varGipc      , 
                                                      final BigDecimal          varPrima     , 
                                                      final BigDecimal          varVRTA1     ,
                                                      final BigDecimal          varVRTA2      ) {
		
		final BigDecimal preSalida = (BigDecimal) mapVariables.get(claveVariable);
		BigDecimal salida = null; 
		if (null == preSalida) {
			BigDecimal Operador1 = BigDecimal.ONE.add(varVRTA1);
			BigDecimal Operador2 = BigDecimal.ONE.add(varVRTA2);
			BigDecimal varVRTA = Operador1.divide(Operador2,ConstantsFunciones.MATH_CONTEXT);
			
			salida = varGipc.multiply(varPrima.multiply(varVRTA));
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
			
			mapVariables.put(claveVariable, salida);
		} else {
			salida = preSalida;
		}
		
		return salida;
	}
	
	
	
	@SuppressWarnings({"unchecked"})
	public static ArrayList<CuadroUmic> getCuadroAmortizacion(final Map<String, Object> mapVariables, final String claveVariable, final Umic umic, final Integer varnpa, final Integer varna, final Integer varpc, final CuadrosAmortizacion varCuadroAmort, final BigDecimal varDelta, final Integer varncMeses,final Timestamp varfechaEfecto,final Integer varMes1,final Integer varMes2) {
		final Object preSalida = mapVariables.get(claveVariable);
		ArrayList<CuadroUmic> salida = new ArrayList<CuadroUmic>();
		BigDecimal calculoSicuotaAux;
		BigDecimal anteriorCpj;
		BigDecimal calculoAux;
		BigDecimal calculoAuxExp;
		BigDecimal varRES;
		BigDecimal varCapitaFI = BigDecimal.ZERO;
		BigDecimal varExpAux = BigDecimal.ZERO;
		BigDecimal varNumAux = BigDecimal.ZERO;
		BigDecimal varMulAux = BigDecimal.ZERO;
		//Siempre guardamos este valor
		if (null == preSalida) {
				
			for(int i = 0; i <= umic.getDuraciones().getNdursegano() * 12 + umic.getDuraciones().getNdursegmes(); i++){
				
				CuadroUmic cu = new CuadroUmic();
				
				if(i==0){
					cu.setVarMes(ConstantsFunciones.CTE_0);
				}else{
					cu.setVarMes(salida.get(i-1).getVarMes() + ConstantsFunciones.CTE_1);
				}
				
				varExpAux = (BigDecimal.valueOf(-varna).multiply(BigDecimal.valueOf(varnpa))).divide(BigDecimal.valueOf(12),ConstantsFunciones.MATH_CONTEXT);
				varNumAux = BigDecimal.ONE.add(varCuadroAmort.getPintermor().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01).divide(BigDecimal.valueOf(varnpa),ConstantsFunciones.MATH_CONTEXT));
				varMulAux = BigDecimal.valueOf(Math.pow(varNumAux.doubleValue(), varExpAux.doubleValue()));
				varCapitaFI = varCuadroAmort.getCapitalIni();
				varRES = (varCapitaFI.divide(umic.getCapitales().getIcapini(),ConstantsFunciones.MATH_CONTEXT)).multiply(varMulAux);
				
				cu.setVarFecha(UtilFechas.incrMeses(varfechaEfecto, null, cu.getVarMes(), false));
				cu.setVarCuotaj(FuncionesAuxiliares.cuota(cu.getVarMes(), varncMeses, varpc, varCuadroAmort.getPintermor(), varDelta, umic.getCapitales().getIcapini(), varna, varRES));
					
				if(cu.getVarMes() <= varncMeses){
						
					if((varncMeses - cu.getVarMes()) % (ConstantsFunciones.CTE_12 / varpc) == 0){
						cu.setVarSicuota(BigDecimal.ONE);
					}else{				
						cu.setVarSicuota(BigDecimal.ZERO);
					}
					
					if(i==0){
						anteriorCpj = umic.getCapitales().getIcapini();
						cu.setVarCpj(anteriorCpj);
					}else{
						anteriorCpj = salida.get(i-1).getVarCpj();
						calculoAuxExp = BigDecimal.valueOf(varpc).divide(BigDecimal.valueOf(12),ConstantsFunciones.MATH_CONTEXT);
						calculoAux = (BigDecimal.ONE.add(varCuadroAmort.getPintermor().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01).divide(BigDecimal.valueOf(varpc),ConstantsFunciones.MATH_CONTEXT)));
						calculoAux = BigDecimal.valueOf(Math.pow(calculoAux.doubleValue(), calculoAuxExp.doubleValue()));
						cu.setVarCpj((anteriorCpj.multiply(calculoAux)).subtract(cu.getVarCuotaj().multiply(cu.getVarSicuota())));
					}
						
				}else{
					if((varna - cu.getVarMes()) % (ConstantsFunciones.CTE_12 / varnpa) == 0){
						calculoSicuotaAux = (BigDecimal.ONE.add(varCuadroAmort.getPorcrec().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01))).pow((cu.getVarMes() - ConstantsFunciones.CTE_1) / 12);
						cu.setVarSicuota(calculoSicuotaAux);
					}else{				
						cu.setVarSicuota(BigDecimal.ZERO);
					}

					if(UtilFechas.getMes(cu.getVarFecha()) == varMes1 || UtilFechas.getMes(cu.getVarFecha()) == varMes2){

						cu.setVarSicuota(cu.getVarSicuota().multiply(ConstantsFunciones.CTE_OPER_2));

					}
						
					if(i==0){
						anteriorCpj = umic.getCapitales().getIcapini();
						cu.setVarCpj(anteriorCpj);
					}else{
						anteriorCpj = salida.get(i-1).getVarCpj();
						calculoAuxExp = BigDecimal.valueOf(varnpa).divide(BigDecimal.valueOf(12),ConstantsFunciones.MATH_CONTEXT);
						calculoAux = (BigDecimal.ONE.add(varCuadroAmort.getPintermor().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01).divide(BigDecimal.valueOf(varnpa),ConstantsFunciones.MATH_CONTEXT)));
						calculoAux = BigDecimal.valueOf(Math.pow(calculoAux.doubleValue(), calculoAuxExp.doubleValue()));
						cu.setVarCpj(anteriorCpj.multiply(calculoAux,ConstantsFunciones.MATH_CONTEXT).subtract(cu.getVarCuotaj().multiply(cu.getVarSicuota(),ConstantsFunciones.MATH_CONTEXT)));
					}
				}
					
				salida.add(cu);
			}
				
			mapVariables.put(claveVariable, salida);
			
				if (UtilModulos.LOG.isTraceEnabled()) {
					UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
				}
				
		} else {
			salida = (ArrayList<CuadroUmic>)preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Recupera el valor de la variable getVarAnoMax
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param numero1
	 * @param numero2
	 * @return
	 */
	public static BigDecimal getVarAnoMax(final Map<String, Object> mapVariables , 
			                              final String              claveVariable, 
			                              final BigDecimal          numero1      ,
			                              final BigDecimal          numero2      ){
		final BigDecimal preSalida = (BigDecimal) mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		if (null == preSalida) {
			salida = numero1.add(numero2);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
			
		} else {
			salida = preSalida;
		}
		return salida;
	}
	
	/**
	 * Recupera el valor de la variable varCMax
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param numero1
	 * @param numero2
	 * @return
	 */
	public static BigDecimal getVarCMax(final Map<String, Object> mapVariables , 
			                            final String              claveVariable, 
			                            final BigDecimal          varC0        ,
			                            final BigDecimal          varPorreval  ,
			                            final Integer             varAnoMax    ,
			                            final Integer             varAno0      ){
		final BigDecimal preSalida = (BigDecimal) mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		if (null == preSalida) {
			salida = varC0.multiply(varPorreval.pow(varAnoMax - varAno0, ConstantsFunciones.MATH_CONTEXT), ConstantsFunciones.MATH_CONTEXT);
			mapVariables.put(claveVariable, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
			
		} else {
			salida = preSalida;
		}
		return salida;
	}
	
	/**
	 * Recupera el dato del fichero de datos especificos y lo almacena en el mapa
	 * @param mapVariables
	 * @param claveVariable
	 * @param kpoliza
	 * @param ksubpoliza
	 * @param kcertificado
	 * @return
	 */
	public static String getDatosEspecificosUmic (final Map<String, Object> mapVariables, final String claveVariable, final Long kpoliza, final Integer ksubpoliza, final Integer kcertificado, final Integer nsuscripcion, final String codigo ){
		final Object preSalida = mapVariables.get(claveVariable);
		String salida = null;
		
		if (null == preSalida){
			final IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
			//1er nivel 
			salida = (String) obtenerConfiguracion.recuperarDatosEspecificosUmic(kpoliza, ksubpoliza, kcertificado, nsuscripcion, codigo);
			
			if(null == salida){
				//2º nivel suscripcion = 0
				salida = (String) obtenerConfiguracion.recuperarDatosEspecificosUmic(kpoliza, ksubpoliza, kcertificado, Integer.valueOf(0), codigo);
				
			}
			
			if(null == salida){
				//3º nivel certificado = 0
				salida = (String) obtenerConfiguracion.recuperarDatosEspecificosUmic(kpoliza, ksubpoliza, Integer.valueOf(0), nsuscripcion, codigo);
			}
			
			mapVariables.put(claveVariable, salida);
			
		} else {
			salida = (String) preSalida;
		}
		
		return salida;
	}
	/**
	 * Dado un valor, se comprueba si es decimal y si es asi se pasa de String con "," a BigDecimal
	 * @param dato
	 * 
	 */
	public static BigDecimal StringToBigDecimal (final String dato){

		BigDecimal datoDecimal;
		
		if(dato.indexOf(",") != -1){
			
			String[] DecimalYEntera = dato.split(",");
			
			BigDecimal Entera = new BigDecimal(DecimalYEntera[0]);
			//if(null != DecimalYEntera[1] && !DecimalYEntera[1].equals("")){	
			BigDecimal Decimal = new BigDecimal(DecimalYEntera[1]).divide(new BigDecimal("10").pow(DecimalYEntera[1].length()));
			datoDecimal = Entera.add(Decimal);
			
			}else{
				datoDecimal = new BigDecimal(dato);
			}
			
			//datoDecimal = datoDecimal.setScale(DecimalYEntera[1].length(), RoundingMode.HALF_UP);
			
		return datoDecimal;
	}
	
	/**
	 * Recupera los valores de estrés
	 * @param mapVariables
	 * @param claveVariable
	 * @param feccierre
	 * @param bt
	 * @return 
	 */
	public static List<ValoresEstres> getValoresEstres (final Map<String, Object> mapVariables, final String claveVariable, final Timestamp feccierre, final String bt){
		final Object preSalida = mapVariables.get(claveVariable);
		List<ValoresEstres> salida = null;
		
		if (null == preSalida){
			final IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
			salida = (List<ValoresEstres>) obtenerConfiguracion.recuperarValoresEstres(feccierre, bt);

			mapVariables.put(claveVariable, salida);
			
		} else {
			salida = (List<ValoresEstres>) preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Estresa las tasas de anulación
	 * @param mapVariables
	 * @param claveVariable
	 * @param feccierre
	 * @param bt
	 * @return 
	 */
	public static List<ValoresAnulacion> getValEstresSCRANM (final Map<String, Object> mapVariables, final String claveVariable, final String cnegocio, 
			final DetalleBaseTecnica btcUmic, final BigDecimal varTC0, final BigDecimal varPolizasAno1){
		
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal varPolizasVig, varDecimal, varFactor1;
		List<ValoresAnulacion> salida = null;
		
		if (null == preSalida){
			final IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
			// Recuperamos las tasas de anulación
			salida = obtenerConfiguracion.recuperarTasasAnul(btcUmic.getTablaTanul(), btcUmic.getFtablaAn(), btcUmic.getBaseTec());
			ValidacionesComunesModulos.validarTasasAnul(salida, btcUmic.getTablaTanul(), btcUmic.getFtablaAn());
			
			// Actualizamos las pólizas vigentes teniendo en cuenta el estrés del primer año
			for (int i = varTC0.intValue() + 2; i<salida.size();i++){
				if (i == varTC0.intValue() + 2){
					varDecimal = BigDecimal.ONE.subtract(varTC0.subtract(new BigDecimal(varTC0.intValue())));
					varFactor1 = BigDecimal.ONE.subtract(salida.get(i-1).getProbabAnul().multiply(varDecimal));
					varPolizasVig = varPolizasAno1.multiply(varFactor1);
				} else {
					varPolizasVig = salida.get(i-1).getPolizaVigentes().multiply(BigDecimal.ONE.subtract(salida.get(i-1).getProbabAnul()));
				}
				salida.get(i).setPolizaVigentes(varPolizasVig);
			}	
			mapVariables.put(claveVariable, salida);
			
		} else {
			salida = (List<ValoresAnulacion>) preSalida;
		}

		return salida;
	}
	
	public static BigDecimal getValPolizasAnuAno1SCRANM (final Map<String, Object> mapVariables, final String claveVariable, final Umic umic, final DetalleBaseTecnica btc, final BigDecimal varLaJ0){
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal varEstres;
		BigDecimal salida = null;
		
		if (null == preSalida){
			final IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
			List<ValoresEstres> varValEstres = (List<ValoresEstres>) obtenerConfiguracion.recuperarValoresEstres(umic.getDatosGenerales().getFecCierre(), btc.getBaseTec());
			if (umic.getDatosGenerales().getCnegocio().equals(ConstantsModulos.CTE_NEGOCIO_INDIVIDUAL)){
				if (varValEstres.get(0).getVariable().equals(ConstantsModulos.CTE_SCR_ANPCI)){
					varEstres = varValEstres.get(0).getValor();
				} else {
					varEstres = varValEstres.get(1).getValor();
				}
			} else {
				if (varValEstres.get(0).getVariable().equals(ConstantsModulos.CTE_SCR_ANPCC)){
					varEstres = varValEstres.get(0).getValor();
				} else {
					varEstres = varValEstres.get(1).getValor();
				}
			}
			// Se calculan las pólizas restantes a final del primer año
			salida = varLaJ0.multiply(BigDecimal.ONE.subtract(varEstres.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)));
			mapVariables.put(claveVariable, salida);
		} else {
			salida = (BigDecimal) preSalida;
		}

		return salida;
	}
	
	public static List<BigDecimal> getValValoresIxSCRINC (final Map<String, Object> mapVariables, final String claveVariable, final Umic umic, final DetalleBaseTecnica btc, final List<BigDecimal> varValoresTabInv,
			final BigDecimal edad0){
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal inca1, incra, valIxEstres;
		List<BigDecimal> salida = new ArrayList<BigDecimal>();
		
		if (null == preSalida){
			final IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
			List<ValoresEstres> varValEstres = (List<ValoresEstres>) obtenerConfiguracion.recuperarValoresEstres(umic.getDatosGenerales().getFecCierre(), btc.getBaseTec());

			if (varValEstres.get(0).getVariable().equals(ConstantsModulos.CTE_SCR_INCA1)){
				inca1 = varValEstres.get(0).getValor();
				incra = varValEstres.get(1).getValor();
			} else {
				inca1 = varValEstres.get(1).getValor();
				incra = varValEstres.get(0).getValor();
			}
			
			for (int i = 0; i< varValoresTabInv.size(); i++){
				if (i < edad0.intValue()){
					valIxEstres = varValoresTabInv.get(i);
				} else if (i == edad0.intValue()){
					valIxEstres = varValoresTabInv.get(i).multiply(BigDecimal.ONE.add(inca1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)));
				} else {
					valIxEstres = varValoresTabInv.get(i).multiply(BigDecimal.ONE.add(incra.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)));
				}
				salida.add(valIxEstres);
			}
			mapVariables.put(claveVariable, salida);
		} else {
			salida = (List<BigDecimal>) preSalida;
		}

		return salida;
	}
	

	/**
	 * Devuelve varLm. Si no está almacenada la calcula, la almacena y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param iteracion
	 * @param fecinisus
	 * @param fecFinTramo1
	 * @param varCriterFecha
	 * @return
	 */
	public static Integer getVarLm(final Map<String, Object> mapVariables, final String claveVariable, final Integer iteracion,
			final Timestamp fecIniTramo1, final Timestamp fecFinTramo1, final String varCriterFecha) {
		final String clave = claveVariable + iteracion;
		final Object preSalida = mapVariables.get(clave);
		Integer salida = null;
		
		if (null == preSalida) {
			salida = FuncionesAuxiliares.tcm(fecIniTramo1, fecFinTramo1);
			mapVariables.put(clave, salida);
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + clave + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (Integer) preSalida;
		}

		return salida;
		
	}
	
	/**
	 * Asigna un valor a la fecha de diferimiento para ser usado en iteraciones posteriores.
	 * @param mapVariables
	 * @param claveVariable
	 * @param iteracion
	 * @param varFecDevAnt
	 */
	public static void setVarFecDiferimiento(final Map<String, Object> mapVariables, final String claveVariable, final Timestamp varFecDevAnt) {
		final String clave = claveVariable;
		mapVariables.put(clave, varFecDevAnt);
	}
	
	/**
	 * Devuelve la fecha de diferimiento de la iteración anterior.
	 * @param mapVariables
	 * @param claveVariable
	 * @param iteracion
	 * @return
	 */
	public static Timestamp getVarFecDiferimiento(final Map<String, Object> mapVariables, final String claveVariable) {
		final String clave = claveVariable;
		return (Timestamp) mapVariables.get(clave);
	}
	
	/**
	 * Sobrescribe los datos y base técnica de un asegurado sobre los datos de otro
	 * 
	 * @param umicCopia
	 * @param btcUmicCopia
	 * @param tablaOrigen
	 * @param indAsegOrigen
	 * @param fNacAsegOrigen
	 * @param sexAsegOrigen
	 * @param edadAsegOrgien
	 */
	public static void fSobreescribirAsegurado(final Umic umicCopia, final DetalleBaseTecnica btcUmicCopia,
			String tablaOrigen, Integer indAsegOrigen, Timestamp fNacAsegOrigen, String sexAsegOrigen,
			Integer edadAsegOrgien) {
		
		if (umicCopia == null || btcUmicCopia == null || tablaOrigen == null || indAsegOrigen == null
				|| fNacAsegOrigen == null || sexAsegOrigen == null || edadAsegOrgien == null) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1);
		}

		btcUmicCopia.setTablacalc1aseg1(tablaOrigen);
		
		Asegurados aseg = new Asegurados();
		aseg.setCsexAseg1(sexAsegOrigen);
		aseg.setFnacAseg1(fNacAsegOrigen);
		aseg.setEdadAseg1(edadAsegOrgien);
		
		umicCopia.setAsegurados(aseg);
		
		if (!btcUmicCopia.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL)
				&& !btcUmicCopia.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA)
				&& !btcUmicCopia.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR) 
				&& !btcUmicCopia.getBaseTec().equals(ConstantesSolvencia.BASE_NIF17LIR) 
				&& !btcUmicCopia.getBaseTec().equals(ConstantesSolvencia.BASE_N17LIRIN) 
				&& !btcUmicCopia.getBaseTec().equals(ConstantesSolvencia.BASE_NIFF17OCI) 
				&& !btcUmicCopia.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17IF)
				&& !btcUmicCopia.getBaseTec().equals(ConstantesSolvencia.BASE_N17CLIR) 
				&& !btcUmicCopia.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17)
				&& !btcUmicCopia.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTIU)  
				&& !btcUmicCopia.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTID) 
				&& !btcUmicCopia.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRGTO) 
				&& !btcUmicCopia.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMFE)  
				&& !btcUmicCopia.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMMI)  
				&& !btcUmicCopia.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCF)  
				&& !btcUmicCopia.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCI)  
				&& !btcUmicCopia.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLFE)  
				&& !btcUmicCopia.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLMI) 
				&& !btcUmicCopia.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC)  
				&& !btcUmicCopia.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRVM)   
				&& !btcUmicCopia.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEP)  
				&& !btcUmicCopia.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEN)  
				&& !btcUmicCopia.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIP)  
				&& !btcUmicCopia.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIN)  
				&& !btcUmicCopia.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM)
				&& !btcUmicCopia.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17MFE)
				&& !btcUmicCopia.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17AEN)
				&& !btcUmicCopia.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17GTO)) {
			if (btcUmicCopia.getTablasConversionAsegurado() == null
					|| btcUmicCopia.getTablasConversionAsegurado().isEmpty()) {
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC,
						new String[] { null, "tablasConversionAsegurado" });
			}
			
			List<List<TablaConversion>> tablaConvAseg = new ArrayList<List<TablaConversion>>();
			tablaConvAseg.add(btcUmicCopia.getTablasConversionAsegurado().get(indAsegOrigen - 1));
			btcUmicCopia.setTablasConversionAsegurado(tablaConvAseg);
		} else {
			List<Integer> tablaBaseExp = new ArrayList<Integer>();
			tablaBaseExp.add(btcUmicCopia.getTablaBaseExp().get(indAsegOrigen - 1));
			btcUmicCopia.setTablaBaseExp(tablaBaseExp);
			List<List<Integer>> tablaBaseExpList = new ArrayList<List<Integer>>();
			tablaBaseExpList.add(btcUmicCopia.getTablaBaseExpList().get(indAsegOrigen - 1));
			btcUmicCopia.setTablaBaseExpList(tablaBaseExpList);
		}
	}
	
	public static Timestamp getVarFecEfectoCSP(final Map<String, Object> mapVariables, final String claveVariable,
			final DatosGenerales datosGenerales, final Fechas fechas) {
		final Object preSalida = mapVariables.get(claveVariable);
		Timestamp varFechaEfecto = null;

		if (preSalida == null) {

			if (datosGenerales.getCsitupol().contentEquals(ConstantsModulos.CTE_DG_CSI_NO_RED)) {

				if (datosGenerales.getCnegocio().equals(ConstantsModulos.CTE_NEGOCIO_INDIVIDUAL)) {
					varFechaEfecto = fechas.getFecefecini();
				} else {
					varFechaEfecto = fechas.getFecinisus();
				}
			} else if (datosGenerales.getCsitupol().contentEquals(ConstantsModulos.CTE_DG_CSITU_RED)) {
				varFechaEfecto = fechas.getFecefecred();

				if (fechas.getFecefecred() == null
						&& datosGenerales.getCnegocio().equals(ConstantsModulos.CTE_NEGOCIO_COLECTIVO)) {
					varFechaEfecto = fechas.getFecfinpagprim();
				}
			}

			mapVariables.put(claveVariable, varFechaEfecto);

		} else {
			varFechaEfecto = (Timestamp) preSalida;
		}

		return varFechaEfecto;
	}
	
	/**
	 * Devuelve valor varPorIni. Si no existe la calcula, la almacena en el Hashmap
	 * y la devuelve.
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codigoConstante
	 * @return
	 */
	public static BigDecimal getVarPorIni(final Map<String, Object> mapVariables, final String claveVariable,
			final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbasetec, final String codigoConstante) {
		return new BigDecimal(getsetStringRecuperarDefinicionAuxiliar(mapVariables, claveVariable, ccartera, kmodalidad, kgarantia,
				kbasetec, codigoConstante));
	}
	
	/**
	 * Devuelve valor varPorDec. Si no existe la calcula, la almacena en el Hashmap
	 * y la devuelve.
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codigoConstante
	 * @return
	 */
	public static BigDecimal getVarPorDec(final Map<String, Object> mapVariables, final String claveVariable,
			final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbasetec,
			final String codigoConstante) {
		return new BigDecimal(getsetStringRecuperarDefinicionAuxiliar(mapVariables, claveVariable, ccartera, kmodalidad, kgarantia,
				kbasetec, codigoConstante));
	}
	
	/**
	 * Devuelve valor varPerDec. Si no existe la calcula, la almacena en el Hashmap
	 * y la devuelve.
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codigoConstante
	 * @return
	 */
	public static BigDecimal getVarPerDec(final Map<String, Object> mapVariables, final String claveVariable,
			final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbasetec, 
			final String codigoConstante) {
		return new BigDecimal(getsetStringRecuperarDefinicionAuxiliar(mapVariables, claveVariable, ccartera, kmodalidad, kgarantia,
				kbasetec, codigoConstante));
	}
	/**
	 * Devuelve valor varMesesCierre. Si no existe la calcula, la almacena en el Hashmap
	 * y la devuelve.
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param ccartera
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codigoConstante
	 * @return
	 */
	public static String getVarMesesCierres(final Map<String, Object> mapVariables, final String claveVariable,
			final Integer ccartera, final Integer kmodalidad, final Integer kgarantia, final String kbasetec,
			final String codigoConstante) {
		return getsetStringRecuperarDefinicionAuxiliar(mapVariables, claveVariable, ccartera, kmodalidad, kgarantia,
				kbasetec, codigoConstante);
	}
	
	/**
	 * Devuelve varBeta. Si no está almacenada la calcula, la setea y la devuelve.
	 * 
	 * @param mapVariables
	 * @param modulo
	 * @param iteracion
	 * @param constanteVariable
	 * @param varTCm
	 * @return
	 */
	public static Integer getVarBeta0(final Map<String, Object> mapVariables, final String claveVariable) {
		final String clave = claveVariable;
		final Object preSalida = mapVariables.get(clave);
		Integer salida = null;

		if (null == preSalida) {
			salida = 0;
			mapVariables.put(clave, salida);

			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable
						+ " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (Integer) preSalida;
		}

		return salida;
	}
	
	/**
	 * Devuelve varTcAnt. Si no está almacenada la calcula, la setea y la devuelve.
	 * 
	 * @param mapVariables
	 * @param modulo
	 * @param iteracion
	 * @param constanteVariable
	 * @param varTCm
	 * @return
	 */
	public static Integer getVarTcAnt(final Map<String, Object> mapVariables, final String claveVariable) {
		final String clave = claveVariable;
		final Object preSalida = mapVariables.get(clave);
		Integer salida = null;

		if (null == preSalida) {
			salida = 0;
			mapVariables.put(clave, salida);

			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable 
						+ " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (Integer) preSalida;
		}

		return salida;
	}
	
	/**
	 * Devuelve varTcAnt. Si no está almacenada la calcula, la setea y la devuelve.
	 * 
	 * @param mapVariables
	 * @param constanteVariable
	 * @return
	 */
	public static BigDecimal getVarPnaTcAnt(final Map<String, Object> mapVariables, final String claveVariable) {
		final String clave = claveVariable;
		final Object preSalida = mapVariables.get(clave);
		BigDecimal salida = null;

		if (null == preSalida) {
			salida = BigDecimal.ZERO;
			mapVariables.put(clave, salida);

			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable 
						+ " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal) preSalida;
		}

		return salida;
	}
	public static Timestamp getVarFecEfectoFPTOZC(final Map<String, Object> mapVariables, final String claveVariable, final DatosGenerales datosGenerales, final Fechas fechas, final BigDecimal isaldo){
		final Object preSalida = mapVariables.get(claveVariable);
		Timestamp varFechaEfecto=null;
		
		
		if (preSalida == null){
			/**
			 *  -	Si la póliza no está reducida, es decir  umic.datosGenerales.csitupol=  ‘VI’
			 *  	o	Si umic.datosGenerales.cnegocio = ‘I’ 
			 *  			varfechaEfecto = umic.fechas.fecefecIni
			 *		o	En caso contrario
			 *				varfechaEfecto = umic.fechas.fecinisus
			 *	
			 *	-	Si la póliza está reducida, es decir  umic.datosGenerales.csitupol=  ‘RE’
			 *		o	varfechaEfecto = umic.fechas.fecefecred
			 */
			
			//INI-TAR00433819			
			if (ConstantsFunciones.CTE_POL_NO_RED.equals(datosGenerales.getCsitupol())      ||
			(ConstantsFunciones.CTE_POL_PRORROGA_PU.equals(datosGenerales.getCsitupol())    ||
			(ConstantsFunciones.CTE_POL_PRORROGA_PP.equals(datosGenerales.getCsitupol()))))   {
				//FIN-TAR00433819	
				if (ConstantesSolvencia.NEGOCIO_INDIVIDUAL.equals(datosGenerales.getCnegocio())){
					varFechaEfecto = fechas.getFecefecini();
				} else {
					varFechaEfecto = fechas.getFecinisus();					
				}
				
			} else if (ConstantsFunciones.CTE_APOR_REDUCIDA.equals(datosGenerales.getCsitupol())) {
				
				if (varFechaEfecto == null){
					if (ConstantesSolvencia.NEGOCIO_COLECTIVO.equals(datosGenerales.getCnegocio())){
						varFechaEfecto = fechas.getFecinisus(); 
					} else {
						//Si el negocio es individual y existe saldo, la fecha de efecto será la de inicio de efecto
						if (isaldo != null && !BigDecimal.ZERO.equals(isaldo)){
							varFechaEfecto = fechas.getFecefecini();
						} else if (ConstantsFunciones.CTE_310.equals(datosGenerales.getKgarantia())){
							//Si la UMIC es de la garantía 310 no tendrá saldo, por lo que hay que recuperar este campo de la UMIC principal
							UmicKey claveP = UtilUmicPrincipal.getUmicPrincipal(mapVariables, ConstantsModulos.CLAVE_VAR_CLAVEPRINCIPAL, datosGenerales);
							BigDecimal isaldoP = getIsaldo(claveP);
							if (isaldoP != null && !BigDecimal.ZERO.equals(isaldoP)){
								varFechaEfecto = fechas.getFecefecini();
							} else {
								throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AT);
							}
							
						} else if (ConstantsFunciones.CTE_10.equals(datosGenerales.getKgarantia())){
							varFechaEfecto = fechas.getFecefecini();
							
						} else {
							throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AT);
						}
					}
				}
			}
			
			mapVariables.put(claveVariable, varFechaEfecto);
			
		} else {
			varFechaEfecto = (Timestamp) preSalida;
		}
		
		return varFechaEfecto;
	}
	
	public static List<Tab923> getTab923 (final Map<String, Object> mapVariables, final String claveVariable, final Integer kmodalidad, final Timestamp finip, final Timestamp ffinp ){
		final Object preSalida = mapVariables.get(claveVariable);
		List<Tab923> salida = null;
		
		if (null == preSalida){
			final IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
			//1er nivel 
			salida = (List<Tab923>) obtenerConfiguracion.recuperarDatosTab923(kmodalidad, finip, ffinp);
		}
		
		return salida;
	}
	
	/**
	 * Recupera el dato del fichero de tab35050 y lo almacena en el mapa
	 * @param mapVariables
	 * @param claveVariable
	 * @param kpoliza
	 * @param ksubpoliza
	 * @param kcertificado
	 * @return
	 */
	public static List<Tab35050> getTab35050 (final String kramo, final Integer kmodalidad){
		
		List<Tab35050> salida = null;
			final IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
			 
			salida = (List<Tab35050>) obtenerConfiguracion.recuperarDatosTab35050(kramo, kmodalidad);
		
		
		return salida;
	}
	
	/**
	 * Recupera el valor de la variable varFactorVRTA para el m�dulo GZCAMORT
	 * 
	 * @param mapVariables
	 * @param claveVariable
	 * @param varGipc
	 * @param varPrima
	 * @param varVRTA
	 * @return
	 */
	public static BigDecimal getVarFactorVrtaGZCAMORTB(final Map<String, Object> mapVariables , 
                                                      final String              claveVariable, 
                                                      final BigDecimal          varGipc      , 
                                                      final BigDecimal          varPrima     , 
                                                      final BigDecimal          varVRTA1     ,
                                                      final BigDecimal          varVRTA2      ) {
		
		final BigDecimal preSalida = (BigDecimal) mapVariables.get(claveVariable);
		BigDecimal salida = null; 
		if (null == preSalida) {
			BigDecimal Operador1 = ConstantsFunciones.CTE_OPER_1.add(varVRTA1);
			BigDecimal Operador2 = ConstantsFunciones.CTE_OPER_12.add(varVRTA2);
			BigDecimal varVRTA = Operador1.divide(Operador2,ConstantsFunciones.MATH_CONTEXT);
			
			salida = varGipc.multiply(varPrima.multiply(varVRTA));
			
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
			
			mapVariables.put(claveVariable, salida);
		} else {
			salida = preSalida;
		}
		
		return salida;
	}
	
	/**
	 * Devuelve el valor de varVZC1Difercol. Si no está almacenada la calcula, la almacena y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param fecInisus
	 * @param fecIniRta
	 * @param fcalc
	 * @param varEdadCalc1
	 * @param lstValoresTabMort1
	 * @param varLzc1
	 * @param varCriterioFecha
	 * @param valorDefecto
	 * @return
	 */
	public static BigDecimal getVarVzc1DifercolJubilacion(final Map<String, Object> mapVariables, final String claveVariable, final Timestamp fecInisus, final Timestamp fecJubilacion, final Timestamp fcalc, final BigDecimal varEdadCalc1, final List<BigDecimal> lstValoresTabMort1, final BigDecimal varLzc1, final String varCriterioFecha, final BigDecimal valorDefecto) {
		final Object preSalida = mapVariables.get(claveVariable);
		BigDecimal salida = null;
		
		// Si la variable no tiene valor se calcula con los parametros de la funcion y se setea en el hashmap
		if (null == preSalida) {
			salida = valorDefecto;
			/**
				varVzc1Difercol = 1. 					
			-	Si umic.rentas.fecini  > fcalc 
				-	varLDifercolEntero= valoresTabMort(ENTERO(varEdadDifer)).wkvalor 
				-	varLDifercolEntero1 = valoresTabMort(ENTERO(varEdadDifer)+1).wkvalor 
				-	varLDifercol = varLDifercolEntero + parteDecimal(varEdadDifer) * (varLDifercolEntero1 – varLDifercolEntero)
				-	varVzc1Difercol = varLDifercol / varLzc1 VarVzc1Difercol --> dejo la variable en memoria disponible para el subproceso de la umic.
			*/
			if (fcalc.before(fecJubilacion)) {
				//final BigDecimal varLDifercol = varLDifercolEnt.add(varEdadDifer.remainder(BigDecimal.ONE).multiply(varLDifercolEnt1.subtract(varLDifercolEnt)));
				/**
				 	- varEdadDifer  = ParteEntera(VarEdadCalc1) + varFraccDifer
				 */
				final BigDecimal varEdadDifer = varEdadCalc1.add(FuncionesAuxiliares.nAnnos(fecInisus, fecJubilacion, varCriterioFecha));
				int varEdadDiferEntero = varEdadDifer.intValue();
				final BigDecimal varLDifercol = Util.interpolaPorEdad(lstValoresTabMort1.get(varEdadDiferEntero), lstValoresTabMort1.get(varEdadDiferEntero + 1), varEdadDifer);
				salida = varLDifercol.divide(varLzc1,ConstantsFunciones.MATH_CONTEXT);
			}
			mapVariables.put(claveVariable, salida);
			if (UtilModulos.LOG.isTraceEnabled()) {
				UtilModulos.LOG.trace("Util Modulos. La variable " + claveVariable + " no tiene valor por lo que se calcula = {}", salida);
			}
		} else {
			salida = (BigDecimal)preSalida;
		}

		return salida;
	}
	
	/** Función encargada de obtener la fecha con el criterio INIPB
	 * 
	 * @param iteracion
	 * @param fcalc
	 * @param fechaDesde
	 * @param fecinisus
	 * @return resultado
	 */
	public static Timestamp obtenerFechaPagoDevengoCriterioINIPG(final Integer iteracion, final Timestamp fcalc, final Timestamp fechaDesde, final Timestamp fecefecini) {
		//Variables locales
		Timestamp fechaPago;
		//Fin variables locales
		
		fechaPago = fechaDesde;
		
		return fechaPago;
	}
}
