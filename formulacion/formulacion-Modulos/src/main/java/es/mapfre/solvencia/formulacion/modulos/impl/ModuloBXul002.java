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
import es.mapfre.solvencia.dominio.salidaCalculo.TotalFlujoProyeccion;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.FuncionesRentas;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas.FechaFr;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo BXul002.
 * Clase encargada del cálculo que devuelve el importe nominal para la proyección de Provisión Matemática por Fórmula Cerrada.
 * La expresión matemática para su determinación es la siguiente:
 * 				Bx(ul002, tcm + beta) = BGMORNL_FAC (tcm + beta) + BGMORNLGIP (tcm + beta)
 * @author apedro
 *
 */
public class ModuloBXul002 implements Modulo {


	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloBXul002.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_BXul002;
	
	private static final String CLAVE_VAR_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_MORT_Q = ConstantsModulos.CTE_VAR_VAL_TAB_MORT_Q.concat(CLAVE_MODULO);
	private static final String CLAVE_VA_CRIT_EDA = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA.concat(CLAVE_MODULO);
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_X = ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_GIPC_PRIMA = ConstantsModulos.CTE_VAR_GIPC_PRIMA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VRTA = ConstantsModulos.CTE_VAR_VRTA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ALFAT = ConstantsFunciones.CTE_ALFAT.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ALFAT30 = ConstantsFunciones.CTE_ALFAT30.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_FACTOR_ALFAT = ConstantsFunciones.CTE_FACTOR_ALFAT.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCM = ConstantsFunciones.CTE_VAR_TCM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_N = ConstantsModulos.CTE_VARN.concat(CLAVE_MODULO);
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
		TotalFlujoProyeccion resultado;
		//Fin variables locales
		
		try {
			if (ModuloBXul002.LOG.isTraceEnabled()) {
				ModuloBXul002.LOG.trace("Inicio de execute en clase ModuloBxul002");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloBxul002
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.
			
			//Se invoca a la función de calculo Bxul002
			resultado = moduloBxul002(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloBXul002.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloBXul002.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloBXul002.LOG.isTraceEnabled()) {
			ModuloBXul002.LOG.trace("Fin de execute en clase ModuloBxul002");
		}
		
		return resultado;
	}
	
	/**
	 * Módulo de cálculo que devuelve el importe nominal para la proyección de Provisión Matemática por Fórmula Cerrada.
	 * La expresión matemática para su determinación es la siguiente:
	 * 				Bx(ul002, tcm + beta) = BGMORNL_FAC (tcm + beta) + BGMORNLGIP (tcm + beta) 
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
	private TotalFlujoProyeccion moduloBxul002(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		BigDecimal varBxul002 = BigDecimal.ZERO;
		TotalFlujoProyeccion salida = new TotalFlujoProyeccion();
		List<BigDecimal> lstValoresTabMort = null;
		List<BigDecimal> lstValoresIx = null;
		String varCriterioEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriFec = ConstantsFunciones.CTE_CADENA_VACIA;
		Timestamp varFechaEfecto = null;
		Timestamp varFecvcto;
		Integer varPeriocidad;
		BigDecimal varX;
		Integer varTcm;
		Integer varn;
		BigDecimal varCapital;
		BigDecimal varI1;
		BigDecimal vargipcPrima;
		BigDecimal varVrta;
		Integer varAlfat;
		BigDecimal varAlfat30;
		BigDecimal varfactorAlfat;
		Integer varAlfa2;
		BigDecimal varAlfa230;
		BigDecimal varfactorAlfa2;
		BigDecimal varBgmornlgip;
		BigDecimal varBgmornlFac;
		Integer nmeses;
		//Fin variables locales
		
		if (ModuloBXul002.LOG.isTraceEnabled()) {
			ModuloBXul002.LOG.trace("Inicio función << moduloBXul002 >> de la clase ModuloBXul002, para la  iteracion = {}", iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Cálculo de las variables de apoyo.
		varCriterioEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_VA_CRIT_EDA);
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		// Fin del cálculo de las variables de apoyo.
		
		// Validación de las variables de apoyo.
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterioEdad);
		} // Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		// Fin de la validación de las variables de apoyo.
		
		
		//Variables módulo
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		varFecvcto = umic.getFechas().getFecefecfin();
		if (varFecvcto == null){
			varFecvcto = proyUmic.get(proyUmic.size()-1).getFechaDesde();
		}
		
		varPeriocidad = ConstantsFunciones.CTE_12;
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriterioEdad, umic.getRentas().getFecIni(), varEdifer);
		
		
		FechaFr varFR = FuncionesAuxiliares.fr(varFecvcto, varFechaEfecto);
		Timestamp fechaRef = varFR.toTimestamp();

		lstValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic, 
				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterioEdad, ConstantesSolvencia.CTE_TABMORT_L);

		lstValoresIx = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT_Q, umic, btcUmic, 
				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterioEdad, ConstantesSolvencia.CTE_TABMORT_I);
		
		
		varCapital = umic.getCapitales().getIcapact();
		varI1 = btcUmic.getItcalc().get(0);
		nmeses = umic.getDuraciones().getNdursegmes();
		
		//Se calculan las variables la primera vez y después se recuperan para optimizar operaciones.
		varn = (Integer) mapVariables.get(CLAVE_VAR_N);
		vargipcPrima = (BigDecimal) mapVariables.get(CLAVE_VAR_GIPC_PRIMA);
		varVrta = (BigDecimal) mapVariables.get(CLAVE_VAR_VRTA);
		varAlfat = (Integer) mapVariables.get(CLAVE_VAR_ALFAT);
		varAlfat30 = (BigDecimal) mapVariables.get(CLAVE_VAR_ALFAT30);
		varfactorAlfat = (BigDecimal) mapVariables.get(CLAVE_VAR_FACTOR_ALFAT);
		BigDecimal porcentajeMasUno = UtilModulos.getNumPorcentajeMasUno(varI1, mapVariables, CLAVE_MODULO);
		
		if (vargipcPrima == null){
			varn = umic.getDuraciones().getNdursegano();
			BigDecimal aniosDecimal = (BigDecimal.valueOf(nmeses).multiply(ConstantsFunciones.CTE_OPER_1ENTRE12))
					.add(BigDecimal.valueOf(umic.getDuraciones().getNdursegdia()).multiply(ConstantsFunciones.CTE_OPER_1_PARTIDO_365));
			varn = varn + aniosDecimal.intValue();
			vargipcPrima = (btcUmic.getGtorosspPrima().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).multiply(umic.getPrimas().getIprimanetaini());
			varVrta = FuncionesRentas.vrta(0, 0, varn, varI1, 99, varI1, varX.intValue(), lstValoresTabMort, 12, BigDecimal.ZERO, true, nmeses);
			varAlfat = FuncionesAuxiliares.alfa2T(varFechaEfecto, varFecvcto, btcUmic.getFecCierre(), 1);
			varAlfat30 = BigDecimal.valueOf(varAlfat).divide(ConstantsFunciones.CTE_OPER_30, ConstantsFunciones.MATH_CONTEXT);
			BigDecimal potencia = BigDecimal.valueOf(varAlfat).multiply(ConstantsFunciones.CTE_OPER_1_PARTIDO_365);
			varfactorAlfat = Util.pow(porcentajeMasUno, potencia.negate());
			
			mapVariables.put(CLAVE_VAR_N, varn);
			mapVariables.put(CLAVE_VAR_GIPC_PRIMA, vargipcPrima);
			mapVariables.put(CLAVE_VAR_VRTA, varVrta);
			mapVariables.put(CLAVE_VAR_ALFAT, varAlfat);
			mapVariables.put(CLAVE_VAR_ALFAT30, varAlfat30);
			mapVariables.put(CLAVE_VAR_FACTOR_ALFAT, varfactorAlfat);
		}
		
		//Para cualquier periodo j se calculará: 

		if (iteracion == ConstantsModulos.CTE_FIRST_ITER){
			varAlfa2 = FuncionesAuxiliares.alfa2T(varFechaEfecto, varFecvcto, btcUmic.getFecCierre(), 2);
		} else {
			varAlfa2 = FuncionesAuxiliares.alfa2T(varFechaEfecto, varFecvcto, proyUmic.get(iteracion-1).getFechaDesde(), 2);
		}
		varAlfa230 = BigDecimal.valueOf(varAlfa2).divide(ConstantsFunciones.CTE_OPER_30, ConstantsFunciones.MATH_CONTEXT);
		
		//Se calcula tcm en el primer periodo y se incrementa en 1 en periodos posteriores
		varTcm = (Integer) mapVariables.get(CLAVE_VAR_TCM);
		if (varTcm == null) {
			varTcm = FuncionesAuxiliares.tcm(fechaRef, proyUmic.get(iteracion-1).getFechaDesde());
		} else {
			varTcm++;
		}
		mapVariables.put(CLAVE_VAR_TCM, varTcm);
		
		BigDecimal potencia2 = BigDecimal.valueOf(varAlfa2).multiply(ConstantsFunciones.CTE_OPER_1_PARTIDO_365);
		varfactorAlfa2 = Util.pow(porcentajeMasUno, potencia2.negate());
		
		varBgmornlgip = FuncionesAuxiliares.bgmornlgip(varX.intValue(), lstValoresTabMort, varn, varI1, vargipcPrima, varTcm, varAlfat, varAlfat30, varfactorAlfat, varAlfa2, varAlfa230, varfactorAlfa2, varVrta, nmeses);
		varBgmornlFac = FuncionesAuxiliares.bgmornlFac(varX.intValue(), lstValoresIx, varn, varI1, varTcm, varCapital, varAlfa2, varAlfa230, varfactorAlfa2, varPeriocidad, lstValoresTabMort);
		
		
		//Se realiza el cálculo de la fórmula bxul002(j) = varBgmornlfac + varBgmornlgip
		varBxul002 = varBgmornlFac.add(varBgmornlgip);
		
		if (ModuloBXul002.LOG.isTraceEnabled()) {
			ModuloBXul002.LOG.trace("Fin función << ModuloBxul002 >> de la clase ModuloBxul002, para la iteracion = {} con resultado varBxul002 = {}", iteracion, varBxul002);
		}
		
		//Se retorna bxul010 y los terminales
		salida.setProvbtiproy(varBxul002);
		salida.setTerminalAnterior(BigDecimal.ZERO);
		salida.setTerminalPosterior(BigDecimal.ZERO);
		
		return salida;
		
	}
}