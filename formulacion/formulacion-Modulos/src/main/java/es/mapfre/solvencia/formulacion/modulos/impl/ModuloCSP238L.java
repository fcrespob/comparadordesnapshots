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
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.impl.ObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Módulo que calcula una renta con crecimiento geométrico hasta una 
 * edad (conforme a criterio edad de cálculo), que por defecto será 65 
 * años. Por tanto, es una prestación cuya cuantía parte de un importe 
 * determinado y revaloriza de forma geométrica, pero solamente hasta 
 * alcanzar el asegurado la edad límite (indicada por GEDEADMAX, por 
 * defecto 65).
 * 
 */
public class ModuloCSP238L implements Modulo {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP238L.class);
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP238L;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	private static final String CLAVE_GEDADMAX = ConstantsModulos.CTE_VAR_GEDADMAX;
	private static final String CLAVE_VAR_GEDADMAX = CLAVE_GEDADMAX.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PORREVAL = ConstantsModulos.CTE_VAR_PORREVAL.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ANO_MAX = ConstantsModulos.CTE_VAR_ANO_MAX.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_CMAX = ConstantsModulos.CTE_VAR_CMAX.concat(CLAVE_MODULO);
	
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	@SuppressWarnings("unchecked")
	public Object execute(Object... args) throws Solvencia2Excepcion {

		BigDecimal resultado = BigDecimal.ZERO;
		
		try {
			if (ModuloCSP238L.LOG.isTraceEnabled()) {
				ModuloCSP238L.LOG.trace("Inicio de execute en clase ModuloCSP238L");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCSP238L
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			//Invocamos a la función de calculo CSP238L
			resultado = moduloCSP238L(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		
		} catch (Solvencia2Excepcion e) {
			ModuloCSP238L.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP238L.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP238L.LOG.isTraceEnabled()) {
			ModuloCSP238L.LOG.trace("Fin de execute en clase ModuloCSP238L");
		}
		
		return resultado;
	}

	/**
	 * Módulo que calcula una renta con crecimiento geométrico hasta una 
	 * edad (conforme a criterio edad de cálculo), que por defecto será 65 
	 * años.
	 * 
	 * @param proyUmic
	 * @param bloqueCorriente
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param mapVariables
	 * @return
	 */
	private BigDecimal moduloCSP238L(final List<DetalleCorriente> proyUmic       , 
			                         final BloqueCorriente        bloqueCorriente, 
			                         final int                    iteracion      ,
			                         final Timestamp              fcalc          , 
			                         final Umic                   umic           , 
			                         final DetalleBaseTecnica     btcUmic        , 
			                         final Map<String, Object>    mapVariables   ,
			                         final String                 codSubproceso  ) {
		
		BigDecimal csp238L = BigDecimal.ZERO;
		String varCriterEdad;
		String varCriterFec;
		BigDecimal varGEdadMax;
		Timestamp varFechaEfecto;
		BigDecimal varC0;
		BigDecimal varPorreval;
		Timestamp varFecIniPago;
		Timestamp varFinRenta;
		Integer varAno0;
		Integer varAnoNac;
		BigDecimal varAnoMax;
		BigDecimal varCMax;
		Integer varAnoj;
		
		if (ModuloCSP238L.LOG.isTraceEnabled()) {
			ModuloCSP238L.LOG.trace("Inicio función << moduloCSP238L >> para la iteracion = {}", iteracion);
		}

		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		/**
		 * Variables de Apoyo
         * 	VarCriterEdad  obtenerConfiguracion.recuperarVariableApoyo(ID-CRITERIO)
         * 	VarCriterFec     obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
         * 
         * 	Si alguna de las variables de apoyo  retornadas es nulo se devuelve error funcional 005 - No se ha encontrado la Variable de Apoyo &NombreVariableApoyo, finalizando el proceso para la UMIC.
         * 
         * 	varGEdadMax  obtenerConfiguración.recuperarVaribleApoyo(GEDEADMAX)
         * Si no se encuentra la variable GEDEADMAX se hará:
         * 	varGEdadMax = 65;
         * 
         * Si estoy en el primer periodo (j=1), se establecerán las siguientes variables: 
         * 
         * 	Si umic.datosGenerales.cnegocio = ‘I’ 
         * 	varfechaEfecto = umic.fechas.fecefecIni
         * 	En caso contrario: 
         * 	varfechaEfecto = umic.fechas fecinisus
         * 
         * 	varC0 =  umic.rentas.rentini
         * 	varPorreval = 1  + (umic.rentas.prevrenta)/100    dejo la variable en memoria disponible para el subproceso de la umic.
         * 	varfecIniPago = umic.rentas.fecini
         * 	varfinRenta = umic.rentas.fecfin
         * 	varAño0 = Año(umic.rentas.fecini)
         * 	varBloque= obtenerConfiguracion.getBloqueBySubproceso(proyUmic, codSubproceso)
         * 	varAnoNac = Año (umic.asegurados.fnacAseg1);
         * 	varAnoMax = varAnoNac + varEdadMax
         * 	varCMax = varC0* 〖(varPorreval )〗^(varAnoMax  -varAño0  )
         * 
         * 
         * 
         * Para cualquier periodo j (j >= 1), se establecerán las siguientes variables: 
         * 
         * 	Si proyUmic(j).varBloque.fecDevengo es nula: 
         * 
         * csp238l = 0
         * 
         * 	En caso contrario: 
         * 
         * 	Si proyUmic(j).varBloque.fecDevengo es < varfecIniPago
         * 
         * csp238l = 0
         *     	
         * 	En caso contrario:
         * 	Si proyUmic(j).varBloque.fecDevengo es <= varfinRenta  ó varfinRenta es nula: 
         * 	varAñoj = TC(varfechaEfecto, fcalc)
         * 	Si varAnoNac + varGEdadMax < varAñoj
         * 
         *     csp238l = varC0* 〖(varPorreval )〗^(varAñoj -varAño0  )  
         * 
         * 
         * 	En caso contrario (Si varAnoNac + varGEdadMax > varAñoj):
         * 
         *    csp238l = varCMax 
         * 
         * 	En caso contrario  (proyUmic(j).varBloque.fecDevengo es > varfinRenta)
         * 
         * csp238l = 0
         *
		 */
		
		// Definición de variables auxiliares para agilizar las operaciones (se llaman varias veces).
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		// Fin de la definición de las variables auxiliares.

		// Variables de apoyo
		varCriterEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
		varGEdadMax = UtilModulos.getVarGEdadMax(mapVariables, CLAVE_VAR_GEDADMAX, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_GEDADMAX);
		
		if (null == varGEdadMax){
			varGEdadMax = BigDecimal.valueOf(65);
		}
		
		// Validación de las variables de apoyo.
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterEdad);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFec);
		}	
		// Fin de la validación de las variables de apoyo.

		// Variables de módulo
		varFechaEfecto = UtilModulos.getVarFechaEfecto(mapVariables, CLAVE_FEC_EFEC, umic);
		varC0 = umic.getRentas().getRentini();
		varPorreval = UtilModulos.getNumPorcentajeMasUno(umic.getRentas().getPrevrenta(),mapVariables, CLAVE_VAR_PORREVAL);
		varFecIniPago = umic.getRentas().getFecIni();
		varFinRenta = umic.getRentas().getFecFin();
		
		//Controlar error en el caso de que la fecha de inicio de pago o fecha fin renta sea nula
		if(null == varFecIniPago){
			
			ModuloCSP238L.LOG.error("La fecha de inicio de la renta es nula");
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_I001);
			
		}
		if( null == varFinRenta && !umic.getRentas().getTempVit().equals("V")){
			
			ModuloCSP238L.LOG.error("La fecha de fin de la renta es nula");
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_I004);
			
		}
		
		varAno0 = UtilFechas.getAnio(varFecIniPago);
		varAnoNac = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1());
		varAnoMax = UtilModulos.getVarAnoMax(mapVariables, CLAVE_VAR_ANO_MAX, BigDecimal.valueOf(varAnoNac), varGEdadMax);
		BigDecimal aux = varAnoMax.subtract(BigDecimal.valueOf(varAno0));
		if(aux.equals(BigDecimal.ZERO) || aux.compareTo(BigDecimal.ZERO) == -1){
			
			varCMax = varC0;
			
		}else{
			
			varCMax = UtilModulos.getVarCMax(mapVariables, CLAVE_VAR_CMAX, varC0, varPorreval, varAnoMax.intValue(), varAno0);
			
		}
		// Fin variables de módulo
		
		// Para cualquier iteración
		if (!(null == bloqueCorriente.getFechaDevengo()) && 
				!(bloqueCorriente.getFechaDevengo().before(varFecIniPago)) && 
				(null == varFinRenta  || 
					!bloqueCorriente.getFechaDevengo().after(varFinRenta))){
			
			varAnoj = FuncionesAuxiliares.tc(varFechaEfecto, fcalc);
			if (varAnoNac + varGEdadMax.intValue() < varAnoj){
				csp238L = varC0.multiply(varPorreval.pow(varAnoj - varAno0, ConstantsFunciones.MATH_CONTEXT), ConstantsFunciones.MATH_CONTEXT);
			} else {
				csp238L = varCMax;
			}
			
		}
		
		if (ModuloCSP238L.LOG.isTraceEnabled()) {
			ModuloCSP238L.LOG.trace("Fin función << moduloCSP238L >> de la clase ModuloCSP238L, para la iteracion = {}, con resultado capitalCartera = {}", iteracion, csp238L);
		}
		
		return csp238L;
	}

}
