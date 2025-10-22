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
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloPRI005 implements Modulo {
	
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloPRI005.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
		private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_PRI005;
		
		private static final String CLAVE_VAR_M = ConstantsModulos.CTE_VAR_M.concat(CLAVE_MODULO);
		
		private static final String CLAVE_PRI005 = ConstantsModulos.CTE_VAR_PPRI005;
		private static final String CLAVE_VAR_PRI005 = CLAVE_PRI005.concat(CLAVE_MODULO);
		
		private static final String CLAVE_FEC_EFECTO = ConstantsModulos.CTE_FEC_EFEC;
		private static final String CLAVE_VAR_FEC_EFECTO = CLAVE_FEC_EFECTO.concat(CLAVE_MODULO);
		
		private static final String CLAVE_X = ConstantsModulos.CTE_VAR_X;
		private static final String CLAVE_VAR_X = CLAVE_X.concat(CLAVE_MODULO);
		
		private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
		private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
		
		private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
		private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
		
		private static final String CLAVE_W = ConstantsModulos.CTE_VAR_W;
		private static final String CLAVE_VAR_W = CLAVE_W.concat(CLAVE_MODULO);
		
		private static final String CLAVE_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT;
		private static final String CLAVE_VAR_VAL_TAB_MORT = CLAVE_VAL_TAB_MORT.concat(CLAVE_MODULO);
		
		private static final String CLAVE_RENT_GEO_2IT = ConstantsModulos.CTE_VAR_RENT_GEO_2IT;
		private static final String CLAVE_VAR_RENT_GEO_2IT = CLAVE_RENT_GEO_2IT.concat(CLAVE_MODULO);
		
		private static final String CLAVE_TC = ConstantsModulos.CTE_VAR_TC;
		private static final String CLAVE_VAR_TC = CLAVE_TC.concat(CLAVE_MODULO);
		
		private static final String CLAVE_I1 = ConstantsModulos.CTE_VARI1;
		private static final String CLAVE_VAR_I1 = CLAVE_I1.concat(CLAVE_MODULO);
		
		private static final String CLAVE_I2 = ConstantsModulos.CTE_VARI2;
		private static final String CLAVE_VAR_I2 = CLAVE_I2.concat(CLAVE_MODULO);
		
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	
	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la llamada a la función que realiza los calculos del modulo.
	 */
	
	@Override
	public Object execute(Object... args) throws Solvencia2Excepcion {
		//Variables locales
			BigDecimal resultado = BigDecimal.ZERO;
		//Fin variables locales
			try {
				if (ModuloPRI005.LOG.isTraceEnabled()) {
					ModuloPRI005.LOG.trace("Inicio de execute en clase ModuloPRI005");
				}
				
				//Recuperamos los datos que le pasaremos a la función moduloPRI005
				final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
				final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
				final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
				final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
				final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
				final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
				final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
				final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
				// Fin de la recuperación de datos.
				
				//Invocamos a la función moduloPRI005
				resultado = moduloPRI005(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,codSubproceso);
				
			} catch (Solvencia2Excepcion e) {
				ModuloPRI005.LOG.error(e.getIncidencia().getTextoError(), e);
				throw e;
			} catch (Exception e) {
				ModuloPRI005.LOG.error(e.getMessage(), e);
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
			}
			
			if (ModuloPRI005.LOG.isTraceEnabled()) {
				ModuloPRI005.LOG.trace("Fin de execute en clase ModuloPRI005");
			}
			
			return resultado;
	}
	/** 
	 * Módulo de cálculo de la cuantía nominal de primas. Representa el valor actual en origen de las primas futuras. 
	 * @param proyUmic
	 * @param bloqueCorriente
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param mapVariables
	 * @param codSubproceso
	 * @return pri005
	 */
	private BigDecimal moduloPRI005(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, Map<String, Object> mapVariables, final String codSubproceso) {
		
		//Variables locales
		BigDecimal pri005 = BigDecimal.ZERO; //No inicializar
		BigDecimal varPrimaIni = BigDecimal.ZERO;
		Integer varMRTA = ConstantsFunciones.CTE_0;
		BigDecimal varPrevPrima = BigDecimal.ZERO;
		BigDecimal varI1 = BigDecimal.ZERO;
		BigDecimal varI2 = BigDecimal.ZERO;
		Timestamp varfechaEfecto = null;
		Timestamp varfecfintramo1 = null;
		BigDecimal varM =  BigDecimal.ZERO;
		String varCriterFec;
		Integer varAnoNac = ConstantsFunciones.CTE_0;
		String varCriterEdad;
		Integer varEdifer = ConstantsFunciones.CTE_0;
		BigDecimal varX = BigDecimal.ZERO;
		Integer varW = ConstantsFunciones.CTE_0;
		String varTabMort;
		List<BigDecimal> varValoresTabMort;
		BigDecimal varRentgeo2it;
		BigDecimal varpri005;
		BigDecimal varFactorI1;
		BigDecimal varFactorI2;
		
		//Fin variables locales
		
		if (ModuloPRI005.LOG.isTraceEnabled()) {
			ModuloPRI005.LOG.trace("Inicio función << moduloPRI005 >> para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Definición de variables auxiliares para agilizar las operaciones (se llaman varias veces).
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		// Fin de la definición de las variables auxiliares.
		
		
		//Variables de apoyo
		varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
		varCriterEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		
		
		//Variables Modulo
		varPrimaIni = umic.getPrimas().getIprimanetaini();
		varMRTA = UtilModulos.getVarTC(mapVariables, CLAVE_VAR_TC, umic.getFechas().getFecinipagprim(), umic.getFechas().getFecfinpagprim());
		varPrevPrima = umic.getPrimas().getPrevprima();
		
		varfechaEfecto = UtilModulos.getVarFechaEfecto(mapVariables, CLAVE_VAR_FEC_EFECTO, umic); 
		
		varI1 = UtilModulos.getNumPorcentajeMasUno(btcUmic.getItcalc().get(0), mapVariables, CLAVE_VAR_I1);
		varI2 = UtilModulos.getNumPorcentajeMasUno(btcUmic.getItcalc().get(1), mapVariables, CLAVE_VAR_I2);
		varfecfintramo1 = btcUmic.getFecfintramo().get(0);
		varM = UtilModulos.getVarM(mapVariables, CLAVE_VAR_M, varfechaEfecto, varfecfintramo1, varCriterFec);
		varAnoNac = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1()); 
		varEdifer = umic.getDatosGenerales().getEdifer();
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varfechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriterEdad, umic.getRentas().getFecIni(), varEdifer);
		varW = UtilModulos.getVarEdadMaxima2(mapVariables, CLAVE_VAR_W, varAnoNac, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1);
		varTabMort = btcUmic.getTablacalc1aseg1();
		varValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic, 
				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterEdad, ConstantesSolvencia.CTE_TABMORT_L);
		
		varRentgeo2it = UtilModulos.getVarRentgeo2it(mapVariables, CLAVE_VAR_RENT_GEO_2IT ,varX, 0, varMRTA, varPrevPrima, varValoresTabMort, varM, varI1, varI2);
		
		varpri005 = UtilModulos.getVarPri005(mapVariables, CLAVE_VAR_PRI005, varPrimaIni, varRentgeo2it);
		
		
		//Para cualquier iteracion
		if(bloqueCorriente.getFechaPago().after(umic.getFechas().getFecefecfin()) || bloqueCorriente.getFechaPago().equals(null)){
			
			pri005 = BigDecimal.ZERO;
			
		}else{
			
			pri005 = varpri005;
			
		}
		if (ModuloPRI005.LOG.isTraceEnabled()) {
			ModuloPRI005.LOG.trace("Fin función << moduloPRI005 >> de la clase ModuloPRI005, para la iteracion = {}, con resultado csp012 = {}", iteracion, pri005);
		}
		
		return pri005;
	}

}
