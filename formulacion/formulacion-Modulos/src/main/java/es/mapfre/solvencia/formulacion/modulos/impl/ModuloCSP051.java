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
import es.mapfre.solvencia.formulacion.util.Terminales;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del calculo que devuelve el capital de fallecimiento correspondiente al Terminal Actual Bonos y 
 * Provision Matematica de Balance Bono Año (Rentabilidad).
 * La expresión matemática para su determinación es la siguiente:
 * 				CSP(051,tc)= Vx(tc)
 * siendo Vx(Tc)= Capital(Tc)*[DIF(x,Tc,n,I1)/(1-((l[x+Tc]-l[x+n]) * (1+I1/100)^-0.5/l[x+Tc])) + Gic/100* AX(0,x,Tc,n,I1)]
 * 
 * @author agonzalezgar
 *
 */
public class ModuloCSP051 implements Modulo {

	/**
	 * Log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP051.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP051;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	private static final String CLAVE_VAR_X = ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_P0 = ConstantsModulos.CTE_VAR_P0.concat(CLAVE_MODULO);
	
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
			if (ModuloCSP051.LOG.isTraceEnabled()) {
				ModuloCSP051.LOG.trace("Inicio de execute en clase ModuloCSP051");
			}
			
			//Inicio recuperación de datos que le pasaremos a la función moduloCSP051
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];

			//Invocamos a la función moduloCSP051
			resultado = moduloCSP051(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP051.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP051.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP051.LOG.isTraceEnabled()) {
			ModuloCSP051.LOG.trace("Fin de execute en clase ModuloCSP051");
		}
		
		return resultado;
	}
	/**
	 * Modulo de calculo que devuelve el capital de fallecimiento correspondiente al Terminal Actual Bonos y 
	 * Provision Matematica de Balance Bono Año (Rentabilidad).
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
	private BigDecimal moduloCSP051(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables loclaes
		BigDecimal csp051 = BigDecimal.ZERO;
		BigDecimal varCapTc = BigDecimal.ZERO;
		BigDecimal varGic = BigDecimal.ZERO;
		BigDecimal varI1 = BigDecimal.ZERO;
		int varDifer = 0;
		int varn = 0;
		int varp = 0;
		String varCriterioEdad;
		String varCritFec = ConstantsFunciones.CTE_CADENA_VACIA;
		Timestamp varFechaEfecto = null;
		Timestamp varFecNac = null;
		BigDecimal varX = null;
		List<BigDecimal> lstValoresTabMort = null;
		//Fin variables locales

		if (ModuloCSP051.LOG.isTraceEnabled()) {
			ModuloCSP051.LOG.trace("Inicio función << moduloCSP051 >> de la clase ModuloCSP051, para la iteracion = {}", iteracion);
		}
			
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		/**
		 * Variables de Apoyo
			o	VarCriterEdad --> obtenerConfiguracion.recuperarVariableApoyo(ID-CRITERIO)
			o	VarCriterFec  --> obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
			Si la variable de apoyo  retornada es nula se devuelve error funcional 005 - No se ha encontrado la Variable de Apoyo ID-CRITERIO, finalizando el proceso para la UMIC.

		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, 
		 * así como las varibales internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso 
		 * por los siguientes periodos a calcular. 

			o	varCapTc = umic.capitales.icapact  --> dejo la variable en memoria, disponible para el subproceso de la umic.
			o	varGic = btcUmic.gtoRosspCap     --> dejo la variable en memoria, disponible para el subproceso de la umic.
			o	varI1 = btcUmic. itcalc1  --> dejo la variable en memoria, disponible para el subproceso de la umic.
			o	varN = umic.duraciones.ndursegano  --> dejo la variable en memoria, disponible para el subproceso de la umic.
			o	varDifer = umic.rentas.nadifer --> dejo la variable en memoria, disponible para el subproceso de la umic.
			o	varfechaEfecto = umic.fechas fecinisus --> dejo la variable en memoria, disponible para el subproceso de la umic.
			o	Varfecnac = umic.asegurados.fnacAseg1 --> dejo la variable en memoria, disponible para el subproceso de la umic.
			o	varTabMort = btcUmic.tablaAseg1  --> dejo la variable en memoria, disponible para el subproceso de la umic
			o	varAnoNac = Año (umic.asegurados.fnacAseg1);
			o	varValoresTabMort = obtenerConfiguracion. recuperarValoresExperiencia(btcUmic.fecCierre, varTabMort, varAnoNac, btcUmic. itcalc1, umic.baseTecIni. psobremort, umic.baseTecIni. priesgo)
			o	VarCriterEdad --> obtenerConfiguracion.recuperarVariableApoyo(ID-CRITERIO)
			
			Establecido el efecto de  la umic calculamos las anualidades transcurridas desde varfechaEfecto hasta la fecha de cálculo, 
			invocando a  la función auxiliar TC: 
			
			Varp0 = TC(varfechaEfecto, fcalc)
			Varp0 --> dejo la variable en memoria, disponible para el subproceso de la umic.

			varx = nedad(varfechaEfecto, Varfecnac, VarCriterEdad) --> dejo la variable en memoria, disponible para el subproceso de la umic.
			
			Para cualquier periodo j, se hallará: 
				varp = varp0 +  nanos(fcalc, proyUmic(j).varBloque.fecDevengo, VarCriterFec).
				varp --> sobreescribo la variable en memoria, disponible para el subproceso.
		 */
		
		// Cálculo y validación de las variables de apoyo.
		varCritFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		varCriterioEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIEDAD);

		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCritFec);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterioEdad);
		}
		// Fin del cálculo y la validación de las variables de apoyo.
		
		if (bloqueCorriente.getFechaDevengo()== null){
			return csp051;
		}

		if(bloqueCorriente.getFechaDevengo().after(umic.getFechas().getFecefecfin()) || !proyUmic.get(iteracion - 1).getFechaDesde().before(umic.getFechas().getFecefecfin())) {
			return csp051;
		}

		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		// Dado que se guardan en estas variables locales, no se dejan en memoria.
		varFecNac = umic.getAsegurados().getFnacAseg1();
		varCapTc = umic.getCapitales().getIcapact();
		varGic = btcUmic.getGtorosspCap();
		varI1 = btcUmic.getItcalc().get(0);
		varn = umic.getDuraciones().getNdursegano();
		varDifer = umic.getRentas().getNadifer();

		// Cálculo del valor de varP
		varp = UtilModulos.getVarP0(mapVariables, CLAVE_VAR_P0, varFechaEfecto, fcalc) + FuncionesAuxiliares.nAnnos(fcalc, bloqueCorriente.getFechaDevengo(), varCritFec).intValue();		
		
		// Se crean variables auxiliares para calcular csp051.
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varFechaEfecto, varFecNac, varCriterioEdad, umic.getRentas().getFecIni(), varEdifer);
		
		lstValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterioEdad, ConstantesSolvencia.CTE_TABMORT_L);
		
		// Se calcula csp051 <-- VTX002 (varCapTc, varValoresTabMort, varGic, varI1, varDifer,varx, varp, varN) 
		csp051 = Terminales.vtx002(varCapTc, lstValoresTabMort,
				varGic, varI1, varDifer, varX.intValue(), varp, varn);
		
		if (ModuloCSP051.LOG.isTraceEnabled()) {
			ModuloCSP051.LOG.trace("Fin función << moduloCSP051 >> de la clase ModuloCSP051, para la iteracion = {}, con resultado csp051 = {}", iteracion, csp051);
		}
		
		return csp051;
	}

}
