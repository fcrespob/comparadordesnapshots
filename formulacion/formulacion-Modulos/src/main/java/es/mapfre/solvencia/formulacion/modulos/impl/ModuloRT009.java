/**MODIFICACION: TAR00433819
  FECHA: 27/09/2018
  DESCRIP: Se modifica la forma de preguntar en el if, paar evitar cancelacion
 */

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
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo RT009.
 * La expresión matemática para su determinación es la siguiente:
 * 					RT(009,tc) =  = K2*Pna(0)  +  K1 * [ Vx(Tc+1)+ (dc/dr-1) * ( Vx(Tc+1) - Vx(Tc) ) ]
 * @author agonzalezgar
 *
 */
public class ModuloRT009 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloRT009.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_RT009;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VA_CRIT_EDA = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_VA_CRIT_EDA.concat(CLAVE_MODULO);
	
	private static final String CLAVE_IFAL = ConstantsModulos.CTE_VA_IFAL;
	private static final String CLAVE_VAR_IFAL = CLAVE_IFAL.concat(CLAVE_MODULO);
	
	private static final String CLAVE_PAS = ConstantsModulos.CTE_VA_PAS;
	private static final String CLAVE_VAR_PAS = CLAVE_PAS.concat(CLAVE_MODULO);
	
	private static final String CLAVE_FUT = ConstantsModulos.CTE_VA_FUT;
	private static final String CLAVE_VAR_FUT = CLAVE_FUT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_IANT = ConstantsModulos.CTE_VA_IANT;
	private static final String CLAVE_VAR_IANT = CLAVE_IANT.concat(CLAVE_MODULO);
	
	
	private static final String CLAVE_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT;
	private static final String CLAVE_VAR_VAL_TAB_MORT = CLAVE_VAL_TAB_MORT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_X = ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_TERMINAL = ConstantsModulos.CTE_VAR_TERMINAL.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_DC = ConstantsModulos.CTE_VAR_DC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_DR = ConstantsModulos.CTE_VAR_DR.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VTX005 = ConstantsModulos.CTE_VTX005;
	private static final String CLAVE_VTX003 = ConstantsModulos.CTE_VTX003;
	private static final String CLAVE_LEIDOBTI = ConstantsModulos.CTE_LEIDOBTI;
	private static final String CLAVE_LEIDOBTIPR = ConstantsModulos.CTE_LEIDOBTIPR;

	
	private static final String CLAVE_LST_PROY = ConstantsModulos.CTE_LST_PROY.concat(CLAVE_MODULO);
	
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	private static final String CLAVE_VAR_DESDERENOV = ConstantsModulos.CTE_VAR_DESDERENOV.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_HASTARENOV = ConstantsModulos.CTE_VAR_HASTARENOV.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_P = ConstantsModulos.CTE_VAR_P.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_VTX005b = ConstantsModulos.CTE_VAR_VX1.concat(CLAVE_MODULO);
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
			
			if (ModuloRT009.LOG.isTraceEnabled()) {
				ModuloRT009.LOG.trace("Inicio de execute en clase ModuloRT009");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloRT009
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo RT009
			resultado = moduloRT009(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloRT009.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloRT009.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloRT009.LOG.isTraceEnabled()) {
			ModuloRT009.LOG.trace("Fin de execute en clase ModuloRT009");
		}
		
		return resultado;
	}
	
	/**
	 * Módulo de cálculo de la cuantía por Rescate de la garantía principal en cada punto necesario para pólizas reducidas. (modalidad 209)
	 * La expresión matemática para su determinación es la siguiente:
	 * 				RT(009,tc) =  = K2*Pna(0)  +  K1 * [ Vx(Tc+1)+ (dc/dr-1) * ( Vx(Tc+1) - Vx(Tc) ) ]
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
	private BigDecimal moduloRT009(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		BigDecimal rt009 = BigDecimal.ZERO;
		String codK1;
		String codK2;
		int durk1;
		int durk2;
		BigDecimal varK1 = BigDecimal.ZERO;
		BigDecimal varK2 = BigDecimal.ZERO;
		BigDecimal varPas = BigDecimal.ZERO;
		BigDecimal varFut = BigDecimal.ZERO;
		BigDecimal varIant = BigDecimal.ZERO;
		BigDecimal varIfal = BigDecimal.ZERO;
		String varCriFec = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal varCapital;
		BigDecimal varPna0;
		BigDecimal varPrc;
		BigDecimal varPrp;
		String varTipoAport;
		String varSituaPol;
		int varM;
		int varN;
		BigDecimal varFallRed;
		BigDecimal varGepc;
		BigDecimal varGic;
		BigDecimal varGipc;
		Timestamp varFecnac;
		Integer varP;
		Integer varPRenov;
		BigDecimal varVx5 = BigDecimal.ZERO;
		BigDecimal varVx5b = BigDecimal.ZERO;
		BigDecimal vari1;
		List<DetalleCorriente> lstDetaCor;
		BigDecimal varDr;
		BigDecimal varDc;
		Timestamp varFecDesdeRenova = null;
		Timestamp varFecHastaRenova = null;
		Timestamp varFecEfecto = null;
		Timestamp varFechaIni;
		//final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		//Fin variables locales
		
		if (ModuloRT009.LOG.isTraceEnabled()) {
			ModuloRT009.LOG.trace("Inicio función << moduloRT009 >> de la clase ModuloRT009, para la iteracion = {}", iteracion);
		}

		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		//Cambio Fase II
		if (umic.getDatosGenerales().getCsitupol().equals(ConstantsModulos.CTE_DG_CSITU_ANU)){
			if (ModuloRT009.LOG.isDebugEnabled()) {
				ModuloRT009.LOG.debug(Util.errorValidacionA7(umic.getKey().toString()));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A7, new String[]{});
		
		}//Fin cambio
		
		/**
		 * Variables de Apoyo
			-	VarCriterFec --> obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
			-	VarCriterEdad --> obtenerConfiguracion.recuperarVariableApoyo(ID-CRITERIO)
			-	VarIfal  --> obtenerConfiguracion.recuperarVariableApoyo(IFAL)
			-	VarPas --> obtenerConfiguracion.recuperarVariableApoyo(PAS)
			-	VarFut --> obtenerConfiguracion.recuperarVariableApoyo(FUT)
			-	VarIant --> obtenerConfiguracion.recuperarVariableApoyo(IANT)
			o	Si alguna de las variables de apoyo  retornadas es nulo se devuelve error funcional 005 - 
			No se ha encontrado la Variable de Apoyo &NombreVariableApoyo, finalizando el proceso para la UMIC.
		 */
		
		// Definición de variables auxiliares para agilizar las operaciones (se llaman varias veces).
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		// Fin de la definición de las variables auxiliares.
		
		// Cálculo de las variables de apoyo.
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
		varCriEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_VA_CRIT_EDA);
		varIfal = UtilModulos.getVarIFal(mapVariables, CLAVE_VAR_IFAL, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_IFAL, umic.getBti().getPintertecnI1());
		varPas = UtilModulos.getVarPas(mapVariables, CLAVE_VAR_PAS, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_PAS);
		varFut = UtilModulos.getVarFut(mapVariables, CLAVE_VAR_FUT, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_FUT);
		varIant = UtilModulos.getVarIant(mapVariables, CLAVE_VAR_IANT, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_IANT);
		// Fin del cálculo de las variables de apoyo.
		
		// Validación de las variables de apoyo.
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriEdad); 
			ValidacionesComunesModulos.validarVariableDeApoyoVarIFal(varIfal);
			ValidacionesComunesModulos.validarVariableDeApoyoVarPas(varPas);
			ValidacionesComunesModulos.validarVariableDeApoyoVarFut(varFut);
			ValidacionesComunesModulos.validarVariableDeApoyoIant(varIant);
		} // Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		// Fin de la validación de las variables de apoyo.
		
		if(bloqueCorriente.getFechaDevengo() == null){
			return rt009;
		}
		
		
		varFechaIni = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		/**
		 * Constantes de Rescates
			-	codk1 = umic.rescates.krescate1
			-	Si el código de la constante  empieza por  KT
					o	durk1 = TCM(umic.fechas.fecinisus, proyUmic(j).varBloque.fecDevengo);
			-	Si el código de la constante empieza por  KC
					o	durk1 = 999
			-	Si el código de la constante empieza por  KM
					o	durk1 =  umic.duraciones.ndurprima/12
			-	Si el código de la constante empieza por  KN
					o	durk1 = umic.duraciones.ndursemes
			-	vark1 -->  obtenerConfiguracion.recuperarCtesRescates(codk1, durk1)
				o	Si el valor de la constante  retornada es nulo se devuelve error funcional 005 - 
				No se ha encontrado la Constante de Rescate  codK1, finalizando el proceso para la UMIC.
			
			-	codk2 = umic.rescates.krescate2
			-	Si el código de la constante  empieza por  KT
				o	durk2 = TCM(umic.fechas.fecinisus, proyUmic(j).varBloque.fecDevengo);
			-	Si el código de la constante empieza por  KC
				o	durk2 = 999
			-	Si el código de la constante empieza por  KM
				o	durk2 =  umic.duraciones.ndurprima/12
			-	Si el código de la constante empieza por  KN
				o	durk2 = umic.duraciones.ndursemes
			-	vark2 -->  obtenerConfiguracion.recuperarCtesRescates(codk2, durk2)
				o	Si el valor de la constante  retornada es nulo se devuelve error funcional 005 - 
				No se ha encontrado la Constante de Rescate  codk2, finalizando el proceso para la UMIC.
		 */
		codK1 = umic.getRescates().getKrescate1();
		codK2 = umic.getRescates().getKrescate2();
		
		durk1 = UtilModulos.obtenerDuracionCodKX(codK1, varFechaIni, bloqueCorriente.getFechaDevengo(), 
				umic.getDuraciones().getNdurprima(), umic.getDuraciones().getNdursegmes());
		durk2 = UtilModulos.obtenerDuracionCodKX(codK2, varFechaIni, bloqueCorriente.getFechaDevengo(), 
				umic.getDuraciones().getNdurprima(), umic.getDuraciones().getNdursegmes());
		
		varK1 = UtilModulos.getCteRescate(mapVariables, codK1, durk1);
		varK2 = UtilModulos.getCteRescate(mapVariables, codK2, durk2);
		ValidacionesComunesModulos.validarCteRescateVarkx(codK1, varK1);
		ValidacionesComunesModulos.validarCteRescateVarkx(codK2, varK2);
		
		
		
		/**
		 * Variables Módulo
			-	varCapital --> umic.capitales.icapfall dejo la variable en memoria, disponible para el subproceso de la umic.
			-	varPna0 = umic.primas.iprimanetaini --> dejo la variable en memoria disponible para el procesado de la umic.
			-	varPrc= umic.capitales.porevalcap --> dejo la variable en memoria, disponible para el subproceso de la umic.
			-	varPrp = umic.primas.prevprima --> dejo la variable en memoria disponible para el procesado de la umic.
			-	varTipoAport = umic.datosgenerales.ctipoaport --> dejo la variable en memoria disponible para el procesado de la umic.
			-	varSituPol = umic.datosgenerales.csitupol --> dejo la variable en memoria disponible para el procesado de la umic.
			-	varM =  umic.duraciones.ndurprima
			-	varN = umic.duraciones.ndursegano
			-	varFallRed = umic.capitales.icapfall --> dejo la variable en memoria disponible para el procesado de la umic.
			-	varGepc = umic.baseTecIni.pgastgesex1I
			-	varGic = btcUmic.gtoRosspCap
			-	VarFecnac = umic.asegurados.fnacAseg1 --> dejo la variable en memoria, disponible para el subproceso de la umic.
			-	varTabMort = btcUmic.tabla1Aseg1  --> dejo la variable en memoria, disponible para el subproceso de la umic
			-	varAnoNac = Año (umic.asegurados.fnacAseg1);
			-	varValoresTabMort = obtenerConfiguracion. recuperarValoresExperiencia(btcUmic.fecCierre, varTabMort, varAnoNac, btcUmic. itcalc1, umic.baseTecIni. psobremort, umic.baseTecIni. priesgo)
			-	varI1 = btcUmic.itcalc1  --> dejo la variable en memoria, disponible para el subproceso de la umic.
			-	VarDc = ndias(umic.fechas.fdesderenova, umic.datosgenerales.feccierre , VarCriterFec) --> dejo la variable en memoria, disponible para el subproceso de la umic.
			-	VarDr = ndias(umic.fechas.fdesderenova, umic.fechas.fhastarenova , VarCriterFec) --> dejo la variable en memoria, disponible para el subproceso de la umic
			-	VarTerminal = obtenerConfiguracion. recuperarConfProv (umic.datosgenerales.kmodalidad, umic.datosgenerales.kgarantia, umic.datosgenerales.kprestacion, fichaProceso.ktipobt)
			-	VarX = nedad(umic.fechas fecefecred, Varfecnac, VarCriterEdad) --> dejo la variable en memoria, disponible para el subproceso de la umic.
			-	varTc0 = TC( umic.fechas.fecefecred, fcalc) --> dejo la variable en memoria, disponible para el subproceso de la umic.
		 */

		final String varTerminal = UtilModulos.getVarTerminal(mapVariables, CLAVE_VAR_TERMINAL, kmodalidad, kgarantia, umic.getDatosAdicionales().getPrestCal(), btcUmic.getBaseTec());
		if (CLAVE_VTX003.equals(varTerminal)){
			varCapital = umic.getCapitales().getIcapact();
		}else{
			varCapital = umic.getCapitales().getIcapfall();
		}
		if (umic.getDatosGenerales().getCsitupol().equals(ConstantsModulos.CTE_DG_CSI_NO_RED)){
			varFecEfecto = varFechaIni;
		}else if (umic.getDatosGenerales().getCsitupol().equals(ConstantsModulos.CTE_DG_CSITU_RED)){
			varFecEfecto = umic.getFechas().getFecefecred();
		}

		varPna0 = umic.getPrimas().getIprimanetaini();
		varPrc = umic.getCapitales().getPorevalcap();
		varPrp = umic.getPrimas().getPrevprima();
		varTipoAport = umic.getDatosGenerales().getCtipoaport();
		varSituaPol = umic.getDatosGenerales().getCsitupol();
		varM = umic.getDuraciones().getNdurprima();
		//Cambio Fase II
		varN = FuncionesAuxiliares.nAnnos(varFecEfecto, proyUmic.get(proyUmic.size() - 1).getFechaDesde(), varCriFec).intValue();
		varGipc = btcUmic.getGtorosspPrima();
		//Fin cambio
		varFallRed = umic.getCapitales().getIcapfall();
		varGepc = umic.getBti().getPgastgesex1I();
		varGic = btcUmic.getGtorosspCap();
		varFecnac = umic.getAsegurados().getFnacAseg1();
		vari1 = btcUmic.getItcalc().get(0);
		Integer varEdifer = umic.getDatosGenerales().getEdifer();		
		
		/**
		 * Para cualquier periodo j se calculará: 
			-	varP = varTc0 + nanos(fcalc, proyUmic(j).varBloque.fecDevengo, varCriterFec) --> sobreescribo la variable en memoria, disponible para el subproceso de la umic.
			-	Si el código de la constante  empieza por  KT
				o	durk1 = TCM(umic.fechas.fecinisus, proyUmic(j).varBloque.fecDevengo);
			-	vark1 -->  obtenerConfiguracion.recuperarCtesRescates(codk1, durk1) --> sobreescribo la variable en memoria, disponible para el subproceso de la umic.
				o	Si el valor de la constante  retornada es nulo se devuelve error funcional 005 - No se ha encontrado la Constante de Rescate  codK1, finalizando el proceso para la UMIC.
			-	Si el código de la constante  empieza por  KT
				o	durk2 = TCM(umic.fechas.fecinisus, proyUmic(j).varBloque.fecDevengo);
			-	vark2 -->  obtenerConfiguracion.recuperarCtesRescates(codk2, durk2) --> sobreescribo la variable en memoria, disponible para el subproceso de la umic.
				o	Si el valor de la constante  retornada es nulo se devuelve error funcional 005 - No se ha encontrado la Constante de Rescate  codK2, finalizando el proceso para la UMIC.
		 */
		final List<BigDecimal> lstValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriEdad, ConstantesSolvencia.CTE_TABMORT_L);
		final BigDecimal varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varFecEfecto, varFecnac, varCriEdad, umic.getRentas().getFecIni(), varEdifer);
		final Timestamp fecDesde = proyUmic.get(iteracion-1).getFechaDesde();
		final Timestamp fecHasta = proyUmic.get(iteracion-1).getFechaHasta();
		
		varP = (Integer) mapVariables.get(CLAVE_VAR_P);
		
		if (varP == null) {
			//Cambio Fase II
			varP = FuncionesAuxiliares.tc(varFecEfecto, fecDesde);
			//Fin cambio
			mapVariables.put(CLAVE_VAR_P, varP);
			varPRenov = FuncionesAuxiliares.tc(varFechaIni, fecDesde);
			
			/**
			 * varfecDesdeRenova = umic umic.fechas.fecinisus + entero(varP) años
			 * Si varfecDesdeRenova > proyUmic(j).fecDesde varfecDesdeRenova = varfecDesdeRenova – 1 año
			 * varfecHastaRenova = varfecDesdeRenova + 1 año
			 * varAlfa = alfa(varfecDesdeRenova, varfecHastaRenova, btcUmic.fecCierre, VarTipoAlfa, VarCriterFec)
			 */
			varFecDesdeRenova = UtilFechas.incrAnyo(varFechaIni, varPRenov);
			if (varFecDesdeRenova.after(fecDesde)) {
				varFecDesdeRenova = UtilFechas.decreAnios(varFecDesdeRenova, ConstantsFunciones.CTE_1);
			}
			varFecHastaRenova = UtilFechas.incrAnyo(varFecDesdeRenova, ConstantsFunciones.CTE_1);
			varDr = BigDecimal.valueOf(UtilModulos.getVarDr(mapVariables, CLAVE_VAR_DR, varFecDesdeRenova, varFecHastaRenova, varCriFec));
			mapVariables.put(CLAVE_VAR_DESDERENOV, varFecDesdeRenova);
			mapVariables.put(CLAVE_VAR_HASTARENOV, varFecHastaRenova);
			
		}else {
			
			/**
			 * varfecDesdeRenova = varfecHastaRenova -> Se guardan las variables en memoria para el subproceso de la umic.
			 * varfecHastaRenova = varfecHastaRenova + 1 año. -> Se guardan las variables en memoria para el subproceso de la umic.
			 * varAlfa = alfa(varfecDesdeRenova, varfecHastaRenova, btcUmic.fecCierre, VarTipoAlfa, VarCriterFec)
			 */
			varFecDesdeRenova = (Timestamp) mapVariables.get(CLAVE_VAR_DESDERENOV);
			varFecHastaRenova = (Timestamp) mapVariables.get(CLAVE_VAR_HASTARENOV);
			if (!fecDesde.after(varFecHastaRenova) && varFecHastaRenova.before(fecHasta)) {
				varFecDesdeRenova = varFecHastaRenova;
				varFecHastaRenova = UtilFechas.incrAnyo(varFecHastaRenova, ConstantsFunciones.CTE_1);
				varDr = BigDecimal.valueOf(UtilModulos.getVarDr(mapVariables, CLAVE_VAR_DR, varFecDesdeRenova, varFecHastaRenova, varCriFec));
				mapVariables.put(CLAVE_VAR_DESDERENOV, varFecDesdeRenova);
				mapVariables.put(CLAVE_VAR_HASTARENOV, varFecHastaRenova);
			}else{
				varDr = BigDecimal.valueOf(UtilModulos.getVarDr(mapVariables, CLAVE_VAR_DR, varFecDesdeRenova, varFecHastaRenova, varCriFec));
			}
			
			//Cambio Fase II
			Integer varPNueva = FuncionesAuxiliares.tc(varFecEfecto, fecDesde);
			//Fin cambio
			if (varPNueva > varP){
				varP = varPNueva;
				mapVariables.put(CLAVE_VAR_P, varP);
			}
		}
	
		/**
		 * Se obtendrá la fórmula de terminal a utilizar de la configuración de provisiones por fórmula cerrada.
		 * Si varTerminal = "VTX005"
		 * varVx5 = VTX005 (varCapital, varPrc, valoresTabMort, varI1, varDifer, varX, varP, varN,varM, varGic, varGepc, varPna0, varPrp, varSituPol, varTipoAport, varIfal, varFallRed, varIant, varPas, varFut)
		 * varP1 = varP + 1
		 * varVx5b = VTX005 (varCapital, varPrc, valoresTabMort, varI1, varDifer, varX, varP1, varN,varM, varGic, varGepc, varPna0, varPrp, varSituPol, varTipoAport, varIfal, varFallRed, varIant, varPas, varFut)
		 */
		
		final Integer varP1;
//INI-TAR00433819
//		if (varTerminal.equals("LEIDOBTI")){
		if (CLAVE_LEIDOBTI.equals(varTerminal)){
//FIN-TAR00433819
			lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PROY, ConstantsModulos.CTE_BTI, btcUmic.getFecCierre(), umic.getKey());
			varVx5 = lstDetaCor.get(iteracion - 1).getTotalFlujoProyeccion().getTerminalAnterior();
			varVx5b = lstDetaCor.get(iteracion - 1).getTotalFlujoProyeccion().getTerminalPosterior();
			
			if (varVx5 == null){
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DD);
			}
			
		}else if(CLAVE_LEIDOBTIPR.equals(varTerminal)){
		
			lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PROY, ConstantsModulos.CTE_BTI_PROY, btcUmic.getFecCierre(), umic.getKey());
			varVx5 = lstDetaCor.get(iteracion - 1).getTotalFlujoProyeccion().getTerminalAnterior();
			varVx5b = lstDetaCor.get(iteracion - 1).getTotalFlujoProyeccion().getTerminalPosterior();
			
			if (varVx5 == null){
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DD);
			}
		
		}else if (CLAVE_VTX005.equals(varTerminal)) {
			
			if (mapVariables.get(CLAVE_VAR_VTX005b) == null){
				varVx5 = Terminales.vtx005(varCapital, varPrc, lstValoresTabMort, vari1, varX.intValue(), varP, varN, varM,
						varGic, varGepc, varPna0, varPrp, varSituaPol, varTipoAport, varIfal, varFallRed, varIant, varPas, varFut);
			}else{
				varVx5b = (BigDecimal) mapVariables.get(CLAVE_VAR_VTX005b);
				varVx5 = varVx5b;
			}
			
			//final Timestamp fecDevjMas1 = proyUmic.get(iteracion - 1).getBloqueBySubproceso(codSubproceso).getFechaDevengo();
			varP1 = varP+1;
			
			/**
			 * varVx5b = VTX005 (varCapital, varPrc, valoresTabMort, varI1, varDifer, varX, varP1, varN, varM, varGic,
			 * varGepc, varPna0, varPrp, varSituPol, varTipoAport, varIfal, varFallRed, varIant, varPas, varFut)
			 */
			varVx5b = Terminales.vtx005(varCapital, varPrc, lstValoresTabMort, vari1, varX.intValue(), varP1, varN, varM, varGic,
			varGepc, varPna0, varPrp, varSituaPol, varTipoAport, varIfal, varFallRed, varIant, varPas, varFut);
			
			mapVariables.put(CLAVE_VAR_VTX005b,varVx5b);
		}
		//Cambio Fase II
		else if (CLAVE_VTX003.equals(varTerminal)){
			if (mapVariables.get(CLAVE_VAR_VTX005b) == null){
				varVx5 = Terminales.vtx003(varCapital, lstValoresTabMort, vari1, varX.intValue(), varP, varN, varM,
						varGipc, varGepc, varPna0, varPrp, varIfal, varIant); //Gic? Gipc?
			}else{
				varVx5b = (BigDecimal) mapVariables.get(CLAVE_VAR_VTX005b);
				varVx5 = varVx5b;
			}
			
			varP1 = varP+1;
			varVx5b = Terminales.vtx003(varCapital, lstValoresTabMort, vari1, varX.intValue(), varP1, varN, varM,
					varGipc, varGepc, varPna0, varPrp, varIfal, varIant);//Gic? Gipc?
			
			mapVariables.put(CLAVE_VAR_VTX005b,varVx5b);
		}//Fin cambio
		
		//Se calculará el importe de rescate correspondiente al periodo j como: 
		//RT009 (j)=vark2*varPna0+vark1*[varVx5b+ (dc/(dr-1))*(varVx5b – varVx5)]
		varDc = BigDecimal.valueOf(UtilModulos.getVarDc(mapVariables, CLAVE_VAR_DC, varFecDesdeRenova, fecDesde, varCriFec));
		
		final BigDecimal operando1 = varVx5b.add(varDc.divide(varDr.subtract(BigDecimal.ONE), ConstantsFunciones.MATH_CONTEXT).multiply(varVx5b.subtract(varVx5)));
		
		rt009 = varK2.multiply(varPna0).add(varK1.multiply(operando1));
		
		if (ModuloRT009.LOG.isTraceEnabled()) {
			ModuloRT009.LOG.trace("Fin función << moduloRT009 >> de la clase ModuloRT009, para la iteracion = {} con resultado rt009 = {}", iteracion, rt009);
		}
			
		return rt009;
	}
}
