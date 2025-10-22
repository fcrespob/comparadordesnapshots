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
import es.mapfre.solvencia.formulacion.util.FuncionesRentas;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo GZC024.
 * Clase encargada del cálculo que devuelve la cuantía nominal de gastos de la garantía.
 * La expresión matemática para su determinación es la siguiente:
 * 				GZC(024, beta) = ((Gipc/100)*PRIMA) / ((altat/30) + (1+i) ^ (- alfat/365) * Vrta (0,0,n/12,i1,x,tm,12,0))
 * @author apedro
 *
 */
public class ModuloGZC024 implements Modulo {


	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZC024.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_GZC024;
	
	private static final String CLAVE_CALCULO_GZC024 = CLAVE_MODULO;
	private static final String CLAVE_VAR_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT.concat(CLAVE_MODULO);
	private static final String CLAVE_VA_CRIT_EDA = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA.concat(CLAVE_MODULO);
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
			if (ModuloGZC024.LOG.isTraceEnabled()) {
				ModuloGZC024.LOG.trace("Inicio de execute en clase ModuloGZC024");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZC024
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.
			
			//Se invoca a la función de calculo GZC024
			resultado = moduloGZC024(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloGZC024.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZC024.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloGZC024.LOG.isTraceEnabled()) {
			ModuloGZC024.LOG.trace("Fin de execute en clase ModuloGZC024");
		}
		
		return resultado;
	}
	
	/**
	 * Módulo de cálculo que devuelve la cuantía nominal de gastos de la garantía.
	 * La expresión matemática para su determinación es la siguiente:
	 * 			GZC(024, beta) = ((Gipc/100)*PRIMA) / ((altat/30) + (1+i) ^ (- alfat/365) * Vrta (0,0,n/12,i1,x,tm,12,0))
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
	private BigDecimal moduloGZC024(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		BigDecimal varGZC024 = BigDecimal.ZERO;
		BigDecimal gtoRosspPrimaEntre100;
		BigDecimal varFactor;
		Integer varAlfat;
		BigDecimal varAlfat30;
		BigDecimal varI1;
		BigDecimal varfactorAlfat;
		Integer varEdad=0;
		List<BigDecimal> lstValoresTabMort = null;
		Integer varn;
		BigDecimal varVrta;
		BigDecimal alfatEntre365;
		BigDecimal denominador;
		String varCriterioEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		Integer nmeses;
		Timestamp varFechaEfecto;
		//Fin variables locales
		
		if (ModuloGZC024.LOG.isTraceEnabled()) {
			ModuloGZC024.LOG.trace("Inicio función << moduloGZC024 >> de la clase ModuloGZC024, para la  iteracion = {}", iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		varCriterioEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_VA_CRIT_EDA);
		
		if (bloqueCorriente.getFechaDevengo() == null) {
			return BigDecimal.ZERO;
		}
		
		if (mapVariables.get(CLAVE_CALCULO_GZC024)== null){
			
			varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
			
			//Variables de módulo
			gtoRosspPrimaEntre100 = btcUmic.getGtorosspPrima().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT);
			varFactor = gtoRosspPrimaEntre100.multiply(umic.getPrimas().getIprimanetaini());
			varAlfat = FuncionesAuxiliares.alfa2T(varFechaEfecto, umic.getFechas().getFecefecfin(), fcalc, 1);
			varAlfat30 = BigDecimal.valueOf(varAlfat).divide(BigDecimal.valueOf(30), ConstantsFunciones.MATH_CONTEXT);
			varI1 = btcUmic.getItcalc().get(0);
			alfatEntre365 = BigDecimal.valueOf(varAlfat).multiply(ConstantsFunciones.CTE_OPER_1_PARTIDO_365, ConstantsFunciones.MATH_CONTEXT);
			varfactorAlfat = Util.pow(BigDecimal.ONE.add(varI1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)), alfatEntre365.negate());
			Integer varEdifer = umic.getDatosGenerales().getEdifer();
			varEdad = FuncionesAuxiliares.nEdad(varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriterioEdad, umic.getRentas().getFecIni(), varEdifer).intValue();
			
			//Si las tablas de inicio y fin son nulas, se recupera la tabla de mortalidad 750 --> tabla con todos los valores = 1000000
			if ((btcUmic.getTablasConversionAsegurado().get(0).get(0).getTablaInicio() == null || Integer.valueOf(btcUmic.getTablasConversionAsegurado().get(0).get(0).getTablaInicio()) == 0)
					&& (btcUmic.getTablasConversionAsegurado().get(0).get(0).getTablaFin() == null || Integer.valueOf(btcUmic.getTablasConversionAsegurado().get(0).get(0).getTablaFin()) == 0)){
				final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
				String wkanacimiento = Integer.toString(UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1()));
				lstValoresTabMort = servicio.recuperarValorDeMortalidad(umic.getDatosGenerales().getFecCierre(), 750, wkanacimiento, umic.getBti().getPsobremort(),  umic.getBti().getPriesgo()).getGvalor();
			} else {
				lstValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic, 
						IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterioEdad, ConstantesSolvencia.CTE_TABMORT_L);
			}
			
			
			varn = umic.getDuraciones().getNdursegano();
			nmeses = umic.getDuraciones().getNdursegmes();
			varVrta = FuncionesRentas.vrta(0, 0, varn, varI1, 99, varI1, varEdad, lstValoresTabMort, 12, BigDecimal.ZERO, true, nmeses);
			
			//Se raliza el cálculo de la fórmula varGZC024 = varFactor / varAlfa30 + (varfactorAlfat * varVrta)
			denominador = varAlfat30.add(varfactorAlfat.multiply(BigDecimal.ONE.add(varVrta)));
			varGZC024 = varFactor.divide(denominador, ConstantsFunciones.MATH_CONTEXT);
			
			//Se almacena el resultado para el resto de períodos
			mapVariables.put(CLAVE_CALCULO_GZC024, varGZC024);
		} else {
			varGZC024 = (BigDecimal) mapVariables.get(CLAVE_CALCULO_GZC024); //Se recupera el resultado almacenado
		}
		
		if (ModuloGZC024.LOG.isTraceEnabled()) {
			ModuloGZC024.LOG.trace("Fin función << ModuloGZC024 >> de la clase ModuloGZC024, para la iteracion = {} con resultado varGZC024 = {}", iteracion, varGZC024);
		}
		
		return varGZC024;
		
	}
}