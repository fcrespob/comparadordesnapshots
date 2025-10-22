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
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilFechas.FechaFr;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo BXul024.
 * Clase encargada del cálculo que devuelve el importe nominal para la proyección de Provisión Matemática por Fórmula Cerrada.
 * La expresión matemática para su determinación es la siguiente:
 * 				Bx(ul024, tcm+beta) = BGMORNLGIP (tcm + beta) + {Sumatorio desde k=1 hasta kk} Npartk * VLPk (tcm + beta)
 * @author apedro
 *
 */
public class ModuloBXul024 implements Modulo {


	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloBXul024.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_BXul024;
	
	private static final String CLAVE_VAR_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT.concat(CLAVE_MODULO);
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
			if (ModuloBXul024.LOG.isTraceEnabled()) {
				ModuloBXul024.LOG.trace("Inicio de execute en clase ModuloBxul024");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloBxul024
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			final String terminal = (String) args[ConstantsModulos.PARAM_P_TERMINAL];
			// Fin de la recuperación de los datos.
			
			//Se invoca a la función de calculo Bxul024
			resultado = moduloBxul024(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso, terminal);
			
		} catch (Solvencia2Excepcion e) {
			ModuloBXul024.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloBXul024.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloBXul024.LOG.isTraceEnabled()) {
			ModuloBXul024.LOG.trace("Fin de execute en clase ModuloBxul024");
		}
		
		return resultado;
	}
	
	/**
	 * Módulo de cálculo que devuelve el importe nominal para la proyección de Provisión Matemática por Fórmula Cerrada.
	 * La expresión matemática para su determinación es la siguiente:
	 * 				Bx(ul024, tcm+beta) = BGMORNLGIP (tcm + beta) + {Sumatorio desde k=1 hasta kk} Npartk * VLPk (tcm + beta)
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
	@SuppressWarnings("unchecked")
	private TotalFlujoProyeccion moduloBxul024(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso, final String terminal) {
		//Variables locales
		BigDecimal varBxul024 = BigDecimal.ZERO;
		TotalFlujoProyeccion salida = new TotalFlujoProyeccion();
		List<BigDecimal> lstValoresTabMort = null;
		String varCriterioEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriFec = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal varX;
		Integer varTcm;
		Integer varn;
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
		BigDecimal varFondo;
		Integer nmeses;
		Timestamp varFechaEfecto;
		//Fin variables locales
		
		if (ModuloBXul024.LOG.isTraceEnabled()) {
			ModuloBXul024.LOG.trace("Inicio función << moduloBXul024 >> de la clase ModuloBXul024, para la  iteracion = {}", iteracion);
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
		
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		FechaFr varFR = FuncionesAuxiliares.fr(umic.getFechas().getFecefecfin(), varFechaEfecto);
		Timestamp fechaRef = varFR.toTimestamp();
		
		//Variables módulo. Se dejan en memoria para el subproceso de la umic
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriterioEdad, umic.getRentas().getFecIni(), varEdifer);
		//Se calcula tcm en el primer periodo y se incrementa en 1 en periodos posteriores
		varTcm = (Integer) mapVariables.get(CLAVE_VAR_TCM);
		if(varTcm == null){
			varTcm = FuncionesAuxiliares.tcm(fechaRef, proyUmic.get(iteracion-1).getFechaDesde());
		}else{
			varTcm++;
		}
		mapVariables.put(CLAVE_VAR_TCM, varTcm);

		//Si las tablas de inicio y fin son nulas o 0, se recupera la tabla de mortalidad 750 --> tabla con todos los valores = 1000000
		if ((btcUmic.getTablasConversionAsegurado().get(0).get(0).getTablaInicio() == null  || Integer.valueOf(btcUmic.getTablasConversionAsegurado().get(0).get(0).getTablaInicio()) == 0)
				&& (btcUmic.getTablasConversionAsegurado().get(0).get(0).getTablaFin() == null || Integer.valueOf(btcUmic.getTablasConversionAsegurado().get(0).get(0).getTablaFin()) == 0)){
			lstValoresTabMort = (List<BigDecimal>) mapVariables.get(CLAVE_VAR_VAL_TAB_MORT);
			if (lstValoresTabMort == null){
				final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
				String wkanacimiento = Integer.toString(UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1()));
				lstValoresTabMort = servicio.recuperarValorDeMortalidad(umic.getDatosGenerales().getFecCierre(), 750, wkanacimiento, umic.getBti().getPsobremort(),  umic.getBti().getPriesgo()).getGvalor();
				mapVariables.put(CLAVE_VAR_VAL_TAB_MORT, lstValoresTabMort);
			}
		} else {
			lstValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic, 
					IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterioEdad, ConstantesSolvencia.CTE_TABMORT_L);
		}
		
		
		varn = umic.getDuraciones().getNdursegano();
		nmeses = umic.getDuraciones().getNdursegmes();
		varI1 = btcUmic.getItcalc().get(0);
		vargipcPrima = (BigDecimal) mapVariables.get(CLAVE_VAR_GIPC_PRIMA);
		varVrta = (BigDecimal) mapVariables.get(CLAVE_VAR_VRTA);
		varAlfat = (Integer) mapVariables.get(CLAVE_VAR_ALFAT);
		varAlfat30 = (BigDecimal) mapVariables.get(CLAVE_VAR_ALFAT30);
		varfactorAlfat = (BigDecimal) mapVariables.get(CLAVE_VAR_FACTOR_ALFAT);
		BigDecimal porcentajeMasUno = UtilModulos.getNumPorcentajeMasUno(varI1, mapVariables, CLAVE_MODULO);
		if (vargipcPrima == null){
			vargipcPrima = (btcUmic.getGtorosspPrima().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).multiply(umic.getPrimas().getIprimanetaini());
			varVrta = FuncionesRentas.vrta(0, 0, varn, varI1, 99, varI1, varX.intValue(), lstValoresTabMort, 12, BigDecimal.ZERO, true, nmeses);
			varAlfat = FuncionesAuxiliares.alfa2T(varFechaEfecto, umic.getFechas().getFecefecfin(), btcUmic.getFecCierre(), 1);
			varAlfat30 = BigDecimal.valueOf(varAlfat).divide(ConstantsFunciones.CTE_OPER_30, ConstantsFunciones.MATH_CONTEXT);
			BigDecimal potencia = BigDecimal.valueOf(varAlfat).multiply(ConstantsFunciones.CTE_OPER_1_PARTIDO_365);
			varfactorAlfat = Util.pow(porcentajeMasUno, potencia.negate());
			mapVariables.put(CLAVE_VAR_GIPC_PRIMA, vargipcPrima);
			mapVariables.put(CLAVE_VAR_VRTA, varVrta);
			mapVariables.put(CLAVE_VAR_ALFAT, varAlfat);
			mapVariables.put(CLAVE_VAR_ALFAT30, varAlfat30);
			mapVariables.put(CLAVE_VAR_FACTOR_ALFAT, varfactorAlfat);
		}
		
		//Para cualquier periodo j se calcula: 
		if (iteracion == ConstantsModulos.CTE_FIRST_ITER){
			varAlfa2 = FuncionesAuxiliares.alfa2T(varFechaEfecto, umic.getFechas().getFecefecfin(), btcUmic.getFecCierre(), 2);
		}else{
			varAlfa2 = FuncionesAuxiliares.alfa2T(varFechaEfecto, umic.getFechas().getFecefecfin(), proyUmic.get(iteracion-1).getFechaDesde(), 2);
		}
		
		varAlfa230 = BigDecimal.valueOf(varAlfa2).divide(ConstantsFunciones.CTE_OPER_30, ConstantsFunciones.MATH_CONTEXT);
		
		BigDecimal potencia2 = BigDecimal.valueOf(varAlfa2).multiply(ConstantsFunciones.CTE_OPER_1_PARTIDO_365);
		varfactorAlfa2 = Util.pow(porcentajeMasUno, potencia2.negate());
		
		varBgmornlgip = FuncionesAuxiliares.bgmornlgip(varX.intValue(), lstValoresTabMort, varn, varI1, vargipcPrima, varTcm, varAlfat, varAlfat30, varfactorAlfat, varAlfa2, varAlfa230, varfactorAlfa2, varVrta, nmeses);
		
		varFondo = proyUmic.get(iteracion-1).getBloqueFall().getImpFlujoNominal();

		
		if (varFondo == null){
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DF, new String[]{"Fallecimiento"});
		} else {
			//Se realiza el cálculo de la fórmula bxul024(j) = varBgmornlgip + varFondo
			varBxul024 = varBgmornlgip.add(varFondo);
		}
		
		if (ModuloBXul024.LOG.isTraceEnabled()) {
			ModuloBXul024.LOG.trace("Fin función << ModuloBxul024 >> de la clase ModuloBxul024, para la iteracion = {} con resultado varBxul024 = {}", iteracion, varBxul024);
		}
		
		//Se retorna bxul024 y los terminales
		salida.setProvbtiproy(varBxul024);
		salida.setTerminalAnterior(BigDecimal.ZERO);
		salida.setTerminalPosterior(BigDecimal.ZERO);
		
		return salida;
		
	}
}