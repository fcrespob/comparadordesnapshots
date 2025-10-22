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
import es.mapfre.solvencia.formulacion.util.FuncionesFallecimiento;
import es.mapfre.solvencia.formulacion.util.FuncionesVBX;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo que devuelve el cálculo de PROVISIONES para productos flexibles de aportaciones extraordinarias.
 * 
 * @author apedro
 *
 */
public class ModuloVBX003 implements Modulo {

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVBX003.class);
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VBX003;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	private static final String CLAVE_IFAL = ConstantsModulos.CTE_VA_IFAL;
	private static final String CLAVE_VAR_IFAL = CLAVE_IFAL.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_TIPO_ALFA = ConstantsModulos.CTE_VA_TIPO_ALFA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT;
	private static final String CLAVE_VAR_VAL_TAB_MORT = CLAVE_VAL_TAB_MORT.concat(CLAVE_MODULO);
	//private static final String CLAVE_LST_PROY = ConstantsModulos.CTE_LST_PROY.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_ANT_RENOVA = ConstantsModulos.CTE_VAR_ANT_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PROX_RENOVA = ConstantsModulos.CTE_VAR_PROX_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_X= ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TC0= ConstantsModulos.CTE_VAR_TC0.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_W = ConstantsModulos.CTE_VAR_W.concat(CLAVE_MODULO);
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
		TotalFlujoProyeccion resultado = null;
		//Fin variables locales
		
		try {
			if (ModuloVBX003.LOG.isTraceEnabled()) {
				ModuloVBX003.LOG.trace("Inicio de execute en clase ModuloVBX003");
			}
			
			//Inicio recuperación de datos que le pasaremos a la función moduloVBX003
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			//Fin de la recuperación de los datos que se pasarán a la función moduloVBX003.
			
			//Invocamos a la función de calculo VBX003
			resultado = moduloVBX003(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVBX003.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVBX003.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloVBX003.LOG.isTraceEnabled()) {
			ModuloVBX003.LOG.trace("Fin de execute en clase ModuloVBX003");
		}
		
		return resultado;

	}

	/** 
	 * 	Modulo para resolver el cálculo de PROVISIONES para productos flexibles de aportaciones extraordinarias.
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
	private TotalFlujoProyeccion moduloVBX003(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		String varCriterFecha = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriterEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal varIFal = BigDecimal.ZERO;
		String  varTipoAlfa = ConstantsFunciones.CTE_CADENA_VACIA;
		Timestamp varFechaEfecto = null;
		Integer varN=0;
		Integer varTC0;
		Timestamp varAntRenova;
		Timestamp varProxRenova;
		Integer varX;
		List<BigDecimal> varValoresTabMort;
		Integer varW; 
		BigDecimal varI1;
		BigDecimal varGic;
		BigDecimal varPpcap;
		Integer varTcm;
		Timestamp varFecJ;
		Integer varTC;
		Integer varBeta;
		BigDecimal varCapital;
		BigDecimal varAlfaBx = BigDecimal.ZERO;
		BigDecimal varVidaPU = BigDecimal.ZERO;
		BigDecimal varFallecPU;
		BigDecimal varPuccap;
		BigDecimal varPpr;
		final TotalFlujoProyeccion salida = new TotalFlujoProyeccion();
		BigDecimal vbx003 = BigDecimal.ZERO;
		//Fin variables locales
		
		if (ModuloVBX003.LOG.isTraceEnabled()) {
			ModuloVBX003.LOG.trace("Inicio función << moduloVBX003 >> de la clase ModuloVBX003, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		

		
		// Definición de variables auxiliares para agilizar las operaciones (se llaman varias veces).
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		// Fin de la definición de las variables auxiliares.
		
		// Cálculo y validación de las variables de apoyo.  
		varCriterFecha = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);	
		varCriterEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		varIFal = UtilModulos.getVarIFal(mapVariables, CLAVE_VAR_IFAL, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_IFAL, umic.getBti().getPintertecnI1());
		varTipoAlfa = UtilModulos.getVarTipoAlfa(mapVariables, CLAVE_VAR_TIPO_ALFA, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec());
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFecha);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterEdad);
			ValidacionesComunesModulos.validarVariableDeApoyoVarIFal(varIFal);
			ValidacionesComunesModulos.validarVariableDeApoyoVarTipoAlfa(varTipoAlfa);
			// Fin de la validación de las variables de apoyo.
		}
		
		//Variables Módulo
		if (ConstantsModulos.CTE_DG_CSI_NO_RED.equals(umic.getDatosGenerales().getCsitupol())) {
			// Si la modalidad de la UMIC que se trata es del negocio Individuales, se toma la fecha
			// de fin de efecto en lugar de la fecha de inicio. (Cambio de alcance 09/2015)
			if (umic.getDatosGenerales().getCnegocio().equals(ConstantesSolvencia.NEGOCIO_INDIVIDUAL)){
				varFechaEfecto = umic.getFechas().getFecefecini();
			} else {
				varFechaEfecto = umic.getFechas().getFecinisus();
			}
			
			varN = umic.getDuraciones().getNdursegano();
		} else if (ConstantsModulos.CTE_DG_CSITU_RED.equals(umic.getDatosGenerales().getCsitupol())){
			varFechaEfecto = umic.getFechas().getFecefecred();
			
			if (umic.getDatosGenerales().getCnegocio().equals(ConstantesSolvencia.NEGOCIO_INDIVIDUAL)){
				varN = umic.getDuraciones().getNdursegano() - FuncionesAuxiliares.tc(umic.getFechas().getFecefecini(), varFechaEfecto);
			} else {
				varN = umic.getDuraciones().getNdursegano() - FuncionesAuxiliares.tc(umic.getFechas().getFecinisus(), varFechaEfecto);
			}					
		}
		
		varTC0 = UtilModulos.getVarTC0(mapVariables, CLAVE_VAR_TC0, varFechaEfecto, fcalc);
		
		varAntRenova = (Timestamp) mapVariables.get(CLAVE_VAR_ANT_RENOVA);
		if (varAntRenova == null){
			varAntRenova = UtilFechas.incrAnyo(varFechaEfecto, varTC0);
			if (varAntRenova.before(fcalc)){
				varAntRenova = UtilFechas.decreAnios(varAntRenova, 1);
			}
	
			varProxRenova = UtilFechas.incrAnyo(varAntRenova, 1);
			mapVariables.put(CLAVE_VAR_ANT_RENOVA, varAntRenova);
			mapVariables.put(CLAVE_VAR_PROX_RENOVA, varProxRenova);
		} else {
			varProxRenova = (Timestamp) mapVariables.get(CLAVE_VAR_PROX_RENOVA);
		}
		
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriterEdad, umic.getRentas().getFecIni(), varEdifer).intValue();
		varValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterEdad, ConstantesSolvencia.CTE_TABMORT_L);
		varW = UtilModulos.getVarEdadMaxima2(mapVariables, CLAVE_VAR_W, UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1()), umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1);
		varI1 = btcUmic.getItcalc().get(0);
		varGic = btcUmic.getGtorosspCap();
		varPpcap = umic.getPrimas().getPpcap();
		varTcm = UtilModulos.getVarTCm(mapVariables, CLAVE_VAR_TCM, iteracion, varFechaEfecto, fcalc);
		varCapital = umic.getCapitales().getIcapact();
		varPpr = umic.getPrimas().getPpr();
		//Fin variables modulo
		
		varFecJ = proyUmic.get(iteracion-1).getFechaDesde();
		varTC = FuncionesAuxiliares.tc(varFechaEfecto, varFecJ);
		varBeta = FuncionesAuxiliares.tcm(fcalc, varFecJ);
		
		if (varFecJ.after(varAntRenova) && varFecJ.after(varProxRenova)){
			varAntRenova = varProxRenova;
			varProxRenova = UtilFechas.incrAnyo(varProxRenova, 1);
			mapVariables.put(CLAVE_VAR_ANT_RENOVA, varAntRenova);
			mapVariables.put(CLAVE_VAR_PROX_RENOVA, varProxRenova);
		}
		
		
		//varAlfaBx = FuncionesAuxiliares.mg001Alfa(varAntRenova, varProxRenova, fcalc, varTipoAlfa, varCriterFecha);
		varAlfaBx = FuncionesVBX.mg001Alfa(varAntRenova, varProxRenova, varFecJ, varTipoAlfa, varCriterFecha);
		varFallecPU = FuncionesFallecimiento.fallecpu(varX, varTC, varAlfaBx, varN, varI1, varIFal, varValoresTabMort, varW);
		varPuccap = FuncionesVBX.puccap(varTC, varTcm, varBeta, varTC0, varIFal, varPpcap, varPpr);
		
		if (varCapital.signum() != 0){
			varVidaPU = FuncionesVBX.vidapu(varX, varTC, varAlfaBx, varValoresTabMort, varW, varN, varI1, varGic);
			
			//Se calcula vbx003 = varCapital * varVidaPU  + varFallecPU * varPuccap
			vbx003 = varCapital.multiply(varVidaPU).add(varFallecPU.multiply(varPuccap));
			
			
		} else {
			//Si varCapital es 0, el resultado es igual al segundo operando
			vbx003 = varFallecPU.multiply(varPuccap);
		}
		
		
		//Se retornan también los terminales
		salida.setProvbtiproy(vbx003);
		salida.setTerminalAnterior(vbx003);
		salida.setTerminalPosterior(BigDecimal.ZERO);
		
		if (ModuloVBX003.LOG.isTraceEnabled()) {
			ModuloVBX003.LOG.trace("Fin función << moduloVBX003 >> de la clase ModuloVBX003, para la iteracion = {}, con resultado vbx003 = {}", iteracion, vbx003);
		}
		
		return salida;
	}
	
	
}
