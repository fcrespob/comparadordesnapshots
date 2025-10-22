package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo RT010.
 * La expresión matemática para su determinación es la siguiente:
 * 		RT(010,tc) = Bdx(tc,beta)bti * COEFPLUSMIN(Tc,Beta)   
 * @author apedro
 *
 */
public class ModuloRT010 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloRT010.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_RT010;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_LST_PROY = ConstantsModulos.CTE_LST_PROY.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_FACTOR = "varFactor".concat(CLAVE_MODULO);
	
	private IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
	
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	
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
			if (ModuloRT010.LOG.isTraceEnabled()) {
				ModuloRT010.LOG.trace("Inicio de execute en clase ModuloRT010");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloRT010
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			//Invocamos a la función ModuloRT010
			resultado = moduloRT010(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloRT010.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloRT010.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloRT010.LOG.isTraceEnabled()) {
			ModuloRT010.LOG.trace("Fin de execute en clase ModuloRT010");
		}
		
		return resultado;
	}
	/**
	 * Módulo de cálculo de la cuantía por Rescate de las garantías dentro de Solvencia II para las modalidades de MILLÓN VIDA.
	 * La expresión matemática para su determinación es la siguiente:
	 *				 RT(010,tc) = Bdx(tc,beta)bti * COEFPLUSMIN(Tc,Beta)   
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
	 * @param codSubproceso
	 * 			Código del subproceso que se está ejecutando
	 */
	private BigDecimal moduloRT010(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		BigDecimal rt010 = BigDecimal.ZERO;
		BigDecimal varTirIni;
		BigDecimal varTirAct;
		BigDecimal varFactor;
		Integer varDpdtes;
		List<DetalleCorriente> lstDetaCor;
		BigDecimal varCoefPlusMin;
		BigDecimal varBdx;
		String varCriterFec = ConstantsFunciones.CTE_CADENA_VACIA;
		//Fin variables locales
		
		if (ModuloRT010.LOG.isTraceEnabled()) {
			ModuloRT010.LOG.trace("Inicio función << ModuloRT010 >> de la clase ModuloRT010, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		//Si umic.fechas.fecefecfin es nulo se devuelve error funcional A2 -  Valor nulo  incorrecto para el dato umic.fechas.fecefecfin
		if (umic.getFechas().getFecefecfin() == null){
			if (ModuloRT010.LOG.isDebugEnabled()) {
				ModuloRT010.LOG.debug(Util.errorValidacionA2(umic.getFechas().getFecefecfin().toString(), ConstantsModulos.CTE_FEC_EFEC_FIN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A2, new String[]{ConstantsModulos.CTE_FEC_EFEC_FIN});
		}
		
		// Cálculo y validación de las variables de apoyo.
		varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		
		Timestamp varfechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		final String codk1 = umic.getRescates().getKrescate1();
		final Integer durk1 = UtilModulos.obtenerDuracionCodKX(codk1, varfechaEfecto, bloqueCorriente.getFechaDevengo(), 
				umic.getDuraciones().getNdurprima(), umic.getDuraciones().getNdursegmes());
		
		final BigDecimal vark1 = UtilModulos.getCteRescate(mapVariables, codk1, durk1);
		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFec);
		}
		// Fin del cálculo y la validación de las variables de apoyo.
		
		if (bloqueCorriente.getFechaDevengo() == null){
			return rt010;
		}
		
		//Se calcula varFactor la primera vez y se guarda en memoria
		varFactor = (BigDecimal) mapVariables.get(CLAVE_VAR_FACTOR);
		if (varFactor == null){
			varTirIni = umic.getRescates().getTirIni().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
			varTirAct = umic.getRescates().getTirCie().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
			
			final BigDecimal varTirIniMas1 = varTirIni.add(BigDecimal.ONE);
			final BigDecimal varTirActMas1 = varTirAct.add(BigDecimal.ONE);
			varFactor = varTirIniMas1.divide(varTirActMas1, ConstantsFunciones.MATH_CONTEXT);
			mapVariables.put(CLAVE_VAR_FACTOR, varFactor);
		}
		
		//Variables auxiliares para el cálculo
		varDpdtes = FuncionesAuxiliares.nDias(proyUmic.get(iteracion-1).getFechaDesde(), umic.getFechas().getFecefecfin(), varCriterFec);
		varCoefPlusMin = Util.pow(varFactor, BigDecimal.valueOf(varDpdtes).multiply(ConstantsFunciones.CTE_OPER_1_PARTIDO_365));
		
		//Se recupera la proyección previamente calculada en BTI para la umic
		lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PROY, ConstantsModulos.CTE_BTI, btcUmic.getFecCierre(), umic.getKey());
		FlujosProbables flujoProbable = obtenerConfiguracion.recuperarConfProv(umic.getDatosGenerales().getKmodalidad(),
				umic.getDatosGenerales().getKgarantia(), umic.getDatosAdicionales().getPrestCal(), ConstantsModulos.CTE_BTI);
		if(null == lstDetaCor || lstDetaCor.size() == 0
				|| lstDetaCor.get(iteracion - 1).getBt().equals(ConstantsModulos.CTE_BTI_PROY)){
			mapVariables.remove(CLAVE_LST_PROY);
			lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PROY, ConstantsModulos.CTE_BTI_PROY, btcUmic.getFecCierre(), umic.getKey());
			flujoProbable = obtenerConfiguracion.recuperarConfProv(umic.getDatosGenerales().getKmodalidad(),
					umic.getDatosGenerales().getKgarantia(), umic.getDatosAdicionales().getPrestCal(), ConstantsModulos.CTE_BTI_PROY);
		}
		
		if (flujoProbable.getProvNominal().equals(ConstantsFactorias.MODULO_PMRR01)
				|| flujoProbable.getProvNominal().equals(ConstantsFactorias.MODULO_PMRR02)) {
			varBdx = lstDetaCor.get(iteracion - 1).getTotalFlujoProyeccion().getProvbtiproy();
		} else {
			varBdx = lstDetaCor.get(iteracion - 1).getTotalFlujoProyeccion().getTerminalAnterior();
		}
		
		//Se reaiza el cálculo de la fórmula RT010(j) = varBdx(j) *  varCoefPlusMin
		rt010 = varBdx.multiply(varCoefPlusMin);
		
		if (ModuloRT010.LOG.isTraceEnabled()) {
			ModuloRT010.LOG.trace("Fin función << ModuloRT010 >> de la clase ModuloRT010, para la iteracion = {} con resultado rt010 = {}", iteracion, rt010);
		}
			
		return rt010;
	}
}
