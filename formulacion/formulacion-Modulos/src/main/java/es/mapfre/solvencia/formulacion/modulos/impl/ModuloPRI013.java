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
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo que devuelve la prima en el periodo que le corresponda pagar al asegurado.
 * Este módulo es el que utilizaremos para indicar que es necesario recalcular la prima de esa garantía en el momento TC.  
 * Serán objeto de este recálculo las garantías con renovación tipo TAR donde la prima en el momento TC no se obtiene como la 
 * revalorización de una prima inicial y un crecimiento PRP predefinido.
 * La expresión matemática para su determinación es la siguiente:
 * 				PRI(013,tc)= Csp(Tc)*TARIFA(TC)/1000
 * 
 * @author apedro
 *
 */
public class ModuloPRI013 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloPRI013.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_PRI013;
	private static final String CLAVELISTACORRUMIC = ConstantsModulos.CTE_LST_CORR_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_INV = ConstantsModulos.CTE_VAR_VAL_TAB_INV.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC;
	private static final String CLAVE_VAR_VAL_TAB_INV_SCR = ConstantsModulos.CTE_VAR_VAL_TAB_INV_SCR.concat(CLAVE_MODULO);
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
			if (ModuloPRI013.LOG.isTraceEnabled()) {
				ModuloPRI013.LOG.trace("Inicio de execute en clase ModuloPRI013");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloPRI013
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de datos.
			
			//Invocamos a la función moduloPRI013
			resultado = moduloPRI013(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloPRI013.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloPRI013.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloPRI013.LOG.isTraceEnabled()) {
			ModuloPRI013.LOG.trace("Fin de execute en clase ModuloPRI013");
		}
		
		return resultado;
	}
	
	/**
	 * Modulo de cálculo que devuelve la prima en el periodo que le corresponda pagar al asegurado.
	 *  Este módulo es el que utilizaremos para indicar que es necesario recalcular la prima de esa garantía en el momento TC.  
	 * Serán objeto de este recálculo las garantías con renovación tipo TAR donde la prima en el momento TC no se obtiene como la 
	 * revalorización de una prima inicial y un crecimiento PRP predefinido.
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
	private BigDecimal moduloPRI013(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal prim013 = BigDecimal.ZERO;
		Timestamp varfecefec=null;
		Integer varTC;
		String varCriterioEdad;
		String varCriFec;
		BigDecimal varGipc;
		BigDecimal varGE;
		BigDecimal varXTC;
		BigDecimal varTasaTc = BigDecimal.ZERO;
		BigDecimal varTarifaTC;
		BigDecimal varCSPTC;
		List<BigDecimal> varValoresTabInv;
		BigDecimal varFracc0;
		BigDecimal varZc;
		//Fin variables locales
		
		if (ModuloPRI013.LOG.isTraceEnabled()) {
			ModuloPRI013.LOG.trace("Inicio función << moduloPRI013 >> de la clase ModuloPRI013, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Obtención y validación del criterio de edad.
		varCriterioEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos
				.validarVariableDeApoyoVarCriEdad(varCriterioEdad);
			ValidacionesComunesModulos
				.validarVariableDeApoyoVarCriterioFecha(varCriFec);
			
		}
		// Fin de la obtención y validación del criterio de edad.
		
		
		// Si no tiene fecha de pago, o la forma de pago es 9 (unica), no se calcula
		if(bloqueCorriente.getFechaPago() == null || ConstantsModulos.CTE_CFORMPAG_UNICA.equals(umic.getPrimas().getCformpago()) 
				|| !bloqueCorriente.getFechaPago().before(umic.getFechas().getFecefecfin())) {
			return prim013;
		}
		
		//Si la poliza es reducida y la fecha de reduccion es anterior a la fecha desde, no se calcula
		if (!UtilModulos.getCalcularSiReducidaPRI(umic.getDatosGenerales().getCsitupol(), umic.getDatosGenerales().getCnegocio(), umic.getFechas(), proyUmic.get(iteracion-1).getFechaDesde())){
			return prim013;
		}
		
		//Variables Módulo
		varGipc = btcUmic.getGtorosspPrima();
		UtilModulos.validarVariable(varGipc, "varGipc");
		varGE = umic.getBti().getPgastgesex1I();
		UtilModulos.validarVariable(varGE, "varGE");
		
		List<DetalleCorriente> listaCorrienteUmic = UtilModulos
				.getListaCorrienteUmic(mapVariables, CLAVELISTACORRUMIC,
						ConstantsModulos.CTE_BTI, btcUmic.getFecCierre(),
						umic.getKey());
		
		if(null == listaCorrienteUmic || listaCorrienteUmic.isEmpty()){
			mapVariables.remove(CLAVELISTACORRUMIC);
			listaCorrienteUmic = UtilModulos
					.getListaCorrienteUmic(mapVariables, CLAVELISTACORRUMIC,
							ConstantsModulos.CTE_BTI_PROY, btcUmic.getFecCierre(),
							umic.getKey());
		}
		
		varfecefec = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
				
		varValoresTabInv = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_INV, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterioEdad, ConstantesSolvencia.CTE_TABMORT_I);
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		BigDecimal varEdadCalc = FuncionesAuxiliares.nEdad(varfecefec, umic.getAsegurados().getFnacAseg1(), varCriterioEdad, umic.getRentas().getFecIni(), varEdifer);
		
		if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC)){
			// Se calcula la edad en la fecha de cálculo
			varFracc0 = FuncionesAuxiliares.nAnnos(varfecefec, fcalc, varCriFec);
			varZc = varEdadCalc.add(varFracc0);
			// Se estresa incapacidad
			varValoresTabInv = UtilModulos.getValValoresIxSCRINC(mapVariables, CLAVE_VAR_VAL_TAB_INV_SCR, umic, btcUmic, varValoresTabInv, varZc);
		}
		
		varTC = FuncionesAuxiliares.tc(varfecefec, bloqueCorriente.getFechaPago());
		varXTC = varEdadCalc.add(BigDecimal.valueOf(varTC));
		varTasaTc = varValoresTabInv.get(varXTC.intValue());
		
		BigDecimal varTasaTcEntre1000 = varTasaTc.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_001);
		BigDecimal divisor = BigDecimal.ONE.subtract(varGE).subtract(varGipc);
		
		// Se calcula PRI013 = varCSPTC * (varTarifaTC /1000)
		varTarifaTC = ConstantsFunciones.CTE_OPER_1000.multiply(varTasaTcEntre1000.divide(divisor, ConstantsFunciones.MATH_CONTEXT));
		varCSPTC = listaCorrienteUmic.get(iteracion-1).getBloqueCompl().getImpFlujoNominal(); 
		
		prim013 = varCSPTC.multiply(varTarifaTC.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_001));
			

		if (ModuloPRI013.LOG.isTraceEnabled()) {
			ModuloPRI013.LOG.trace("Fin función << moduloPRI013 >> de la clase ModuloPRI013, para la iteracion = {}, con resultado prim013 = {}", iteracion, prim013);
		}
			
		return prim013;
	}

}
