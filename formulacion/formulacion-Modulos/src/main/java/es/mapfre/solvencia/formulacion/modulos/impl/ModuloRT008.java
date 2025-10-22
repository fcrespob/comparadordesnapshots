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
import es.mapfre.solvencia.formulacion.util.Terminales;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo RT008.
 * La expresión matemática para su determinación es la siguiente:
 * 				Si Estado=Vigor entonces:
 *					RT(008,tc) = K1 *[Vx(Tc+1)+(1-K3)*(dc/dr-1)*( Vx(Tc+1)- Vx(Tc)) ]- K3*K2 *Pna(0) * [AX(0,X,Tc,M,I1) /AX(0,X,0,M,I1) 
 *  			Si Estado=Reducida entonces:
 *					RT(008,tc) =  RT(009,tc)   
 *
 * @author agonzalezgar
 *
 */
public class ModuloRT008 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloRT008.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_RT008;
	
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
	
	private static final String CLAVE_VAR_DC = ConstantsModulos.CTE_VAR_DC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_DR = ConstantsModulos.CTE_VAR_DR.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT;
	private static final String CLAVE_VAR_VAL_TAB_MORT = CLAVE_VAL_TAB_MORT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	private static final String CLAVE_VAR_X = ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TERMINAL = ConstantsModulos.CTE_VAR_TERMINAL.concat(CLAVE_MODULO);
	private static final String CLAVE_LST_PROY = ConstantsModulos.CTE_LST_PROY.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_DESDERENOV = ConstantsModulos.CTE_VAR_DESDERENOV.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_HASTARENOV = ConstantsModulos.CTE_VAR_HASTARENOV.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_P = ConstantsModulos.CTE_VAR_P.concat(CLAVE_MODULO);
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
			if (ModuloRT008.LOG.isTraceEnabled()) {
				ModuloRT008.LOG.trace("Inicio de execute en clase ModuloRT008");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloRT008
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función moduloRT008
			resultado = moduloRT008(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloRT008.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloRT008.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloRT008.LOG.isTraceEnabled()) {
			ModuloRT008.LOG.trace("Fin de execute en clase ModuloRT008");
		}
		
		return resultado;
	}
	
	/**
	 * Módulo de cálculo de la cuantía por Rescate de la garantía principal en cada punto necesario para pólizas en vigor no reducidas. (modalidad 209)
	 * La expresión matemática para su determinación es la siguiente:
	 * 			Si Estado=Vigor entonces:
	 * 				RT(008,tc) = K1 *[Vx(Tc+1)+(1-K3)*(dc/dr-1)*( Vx(Tc+1)- Vx(Tc)) ]- K3*K2 *Pna(0) * [AX(0,X,Tc,M,I1) /AX(0,X,0,M,I1) 
	 * 			Si Estado=Reducida entonces:
	 * 				RT(008,tc) =  RT(009,tc)
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
	private BigDecimal moduloRT008(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		String varCriterFec = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriterEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal varIfal = BigDecimal.ZERO;
		BigDecimal varPas = BigDecimal.ZERO;
		BigDecimal varFut = BigDecimal.ZERO;
		BigDecimal varIant = BigDecimal.ZERO;
		BigDecimal vark1 = BigDecimal.ZERO;
		BigDecimal vark2 = BigDecimal.ZERO;
		BigDecimal vark3 = BigDecimal.ZERO;
		BigDecimal varVx5 = BigDecimal.ZERO;
		BigDecimal varVx5b = BigDecimal.ZERO;
		BigDecimal rt008 = BigDecimal.ZERO;
		List<DetalleCorriente> lstDetaCor;
		BigDecimal varDr;
		BigDecimal varDc;
		Timestamp varFecDesdeRenova = null;
		Timestamp varFecHastaRenova = null;
		Timestamp varFechaEfecto;
		//Fin variables locales
		
		if (ModuloRT008.LOG.isTraceEnabled()) {
			ModuloRT008.LOG.trace("Inicio función << moduloRT008 >> de la clase ModuloRT008, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		/**
		 * Si la póliza está anulada, es decir umic.datosGenerales.csitupol= ‘AN’
		 * Se deberá retornar error funcional 004 – Umic en estado Anulado & claveUmic
		 * 
		 * Si la póliza está reducida, es decir umic.datosGenerales.csitupol= ‘RE’
		 * Se invocará al módulo RT009.
		 * La póliza debe estar en vigor, es decir umic.datosGenerales.csitupol= ‘VI’
		 */
		if (ConstantsModulos.CTE_DG_CSITU_ANU.equals(umic.getDatosGenerales().getCsitupol())) {
			if (ModuloRT008.LOG.isDebugEnabled()) {
				ModuloRT008.LOG.debug(Util.errorValidacionA7(umic.getIdUmic()));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A7, new String[]{ConstantsModulos.CTE_CLAVE_UMIC});
		} else if (ConstantsModulos.CTE_DG_CSITU_RED.equals(umic.getDatosGenerales().getCsitupol())) {
			final ModuloRT009 moduloRT009 = (ModuloRT009)FactoriaModulos.getModulo(ConstantsFactorias.MODULO_RT009);
			rt008 = (BigDecimal) moduloRT009.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		} else if (ConstantsModulos.CTE_DG_CSI_NO_RED.equals(umic.getDatosGenerales().getCsitupol())) {
			/**
			 * Variables de Apoyo
			 * VarCriterFec -> obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
			 * VarCriterEdad --> obtenerConfiguracion.recuperarVariableApoyo(ID-CRITERIO)
			 * Var Ifal -> obtenerConfiguracion.recuperarVariableApoyo(IFAL)
			 * VarPas -> obtenerConfiguracion.recuperarVariableApoyo(PAS)
			 * VarFut -> obtenerConfiguracion.recuperarVariableApoyo(FUT)
			 * VarIant -> obtenerConfiguracion.recuperarVariableApoyo(IANT)
			 * Si alguna de las variables de apoyo retornadas es nulo se devuelve
			 * error funcional 005 - No se ha encontrado la Variable de Apoyo &NombreVariableApoyo, finalizando el proceso para la UMIC.
			 */
			// Definición de variables auxiliares para agilizar las operaciones (se llaman varias veces).
			final Integer ccartera = umic.getDatosGenerales().getCcartera();
			final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
			final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
			// Fin de la definición de las variables auxiliares.
			
			// Cálculo de las variables de apoyo.
			varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
			varCriterEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_VA_CRIT_EDA);
			varIfal = UtilModulos.getVarIFal(mapVariables, CLAVE_VAR_IFAL, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_IFAL, umic.getBti().getPintertecnI1());
			varPas = UtilModulos.getVarPas(mapVariables, CLAVE_VAR_PAS, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_PAS);
			varFut = UtilModulos.getVarFut(mapVariables, CLAVE_VAR_FUT, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_FUT);
			varIant = UtilModulos.getVarIant(mapVariables, CLAVE_VAR_IANT, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_IANT);
			// Fin del cálculo de las variables de apoyo.
			
			// Validación de las variables de apoyo.
			// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
			if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
				ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFec);
				ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterEdad); 
				ValidacionesComunesModulos.validarVariableDeApoyoVarIFal(varIfal);
				ValidacionesComunesModulos.validarVariableDeApoyoVarPas(varPas);
				ValidacionesComunesModulos.validarVariableDeApoyoVarFut(varFut);
				ValidacionesComunesModulos.validarVariableDeApoyoIant(varIant);
			} // Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
			// Fin de la validación de las variables de apoyo.
			
			if(bloqueCorriente.getFechaDevengo() == null){
				return rt008;
			}
			
			varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
			
			/**
			 * Constantes de Rescates
			 * Para obtener el valor real de las constantes de rescate hay que realizar un servicio de consulta con el código y la duración de la constante.
			 * codk1 = umic.rescates.krescate1
			 * Si el código de la constante empieza por KT
			 * durk1 = TCM(umic.fechas.fecinisus, proyUmic(j).varBloque.fecDevengo);
			 * Si el código de la constante empieza por KC
			 * durk1 = 999
			 * Si el código de la constante empieza por KM
			 * durk1 = umic.duraciones.ndurprima/12
			 * Si el código de la constante empieza por KN
			 * durk1 = umic.duraciones.ndursemes
			 * vark1 -> obtenerConfiguracion.recuperarCtesRescates(codk1, durk1)
			 * Si el valor de la constante retornada es nulo se devuelve error funcional 005 - No se ha encontrado la Constante de Rescate codK1,
			 * finalizando el proceso para la UMIC.
			 * 
			 * codk2 = umic.rescates.krescate2
			 * Si el código de la constante empieza por KT
			 * durk2 = TCM(umic.fechas.fecinisus, proyUmic(j).varBloque.fecDevengo);
			 * Si el código de la constante empieza por KC
			 * durk2 = 999
			 * Si el código de la constante empieza por KM
			 * durk2 = umic.duraciones.ndurprima/12
			 * Si el código de la constante empieza por KN
			 * durk2 = umic.duraciones.ndursemes
			 * vark2 -> obtenerConfiguracion.recuperarCtesRescates(codk2, durk2)
			 * Si el valor de la constante retornada es nulo se devuelve error funcional 005 - No se ha encontrado la Constante de Rescate codk2,
			 * finalizando el proceso para la UMIC.
			 * 
			 * codk3 = umic.rescates.krescate3
			 * Si el código de la constante empieza por KT
			 * durk3 = TCM(umic.fechas.fecinisus, proyUmic(j).varBloque.fecDevengo);
			 * Si el código de la constante empieza por KC
			 * durk3 = 999
			 * Si el código de la constante empieza por KM
			 * durk3 = umic.duraciones.ndurprima/12
			 * Si el código de la constante empieza por KN
			 * durk3 = umic.duraciones.ndursemes
			 * vark3 -> obtenerConfiguracion.recuperarCtesRescates(codk3, durk3)
			 * Si el valor de la constante retornada es nulo se devuelve error funcional 005 - No se ha encontrado la Constante de Rescate codk3,
			 * finalizando el proceso para la UMIC.
			 */
			final String codk1 = umic.getRescates().getKrescate1();
			final String codk2 = umic.getRescates().getKrescate2();
			final String codk3 = umic.getRescates().getKrescate3();
			
			final Integer durk1 = UtilModulos.obtenerDuracionCodKX(
					codk1, varFechaEfecto, bloqueCorriente.getFechaDevengo(),
					umic.getDuraciones().getNdurprima(), umic.getDuraciones().getNdursegmes());
			final Integer durk2 = UtilModulos.obtenerDuracionCodKX(
					codk2, varFechaEfecto, bloqueCorriente.getFechaDevengo(),
					umic.getDuraciones().getNdurprima(), umic.getDuraciones().getNdursegmes());
			final Integer durk3 = UtilModulos.obtenerDuracionCodKX(
					codk3, varFechaEfecto, bloqueCorriente.getFechaDevengo(),
					umic.getDuraciones().getNdurprima(), umic.getDuraciones().getNdursegmes());
			
			vark1 = UtilModulos.getCteRescate(mapVariables, codk1, durk1);
			vark2 = UtilModulos.getCteRescate(mapVariables, codk2, durk2);
			vark3 = UtilModulos.getCteRescate(mapVariables, codk3, durk3);
			// Estas validaciones hay que hacerlas en cada iteración porque la fecha de devengo cambiará.
			ValidacionesComunesModulos.validarCteRescateVarkx(codk1, vark1);
			ValidacionesComunesModulos.validarCteRescateVarkx(codk2, vark2);
			ValidacionesComunesModulos.validarCteRescateVarkx(codk3, vark3);
			// Fin de las validaciones iniciales.
			
			
			
			/**
			 * Variables Módulo
			 * varTc0 = TC(umic.fechas fecinisus, fcalc) -> dejo la variable en memoria, disponible para el subproceso de la umic.
			 * varCapital = umic.capitales.icapini -> dejo la variable en memoria, disponible para el subproceso de la umic.
			 * varPrc= umic.capitales.porevalcap -> dejo la variable en memoria, disponible para el subproceso de la umic.
			 * varPna0 = umic.primas.iprimanetaini -> dejo la variable en memoria disponible para el procesado de la umic.
			 * varPrp = umic.primas.prevprima -> dejo la variable en memoria disponible para el procesado de la umic.
			 * varTipoAport = umic.datosgenerales.ctipoaport -> dejo la variable en memoria disponible para el procesado de la umic.
			 * varSituPol = umic.datosgenerales.csitupol -> dejo la variable en memoria disponible para el procesado de la umic.
			 * varM = umic.duraciones.ndurprima
			 * varN = umic.duraciones.ndursegano
			 * varFallRed = umic.capitales.icapfall -> dejo la variable en memoria disponible para el procesado de la umic.
			 * varGepc = umic.baseTecIni.pgastgesex1I
			 * varGic = btcUmic.gtoRosspCap
			 * VarFecnac = umic.asegurados.fnacAseg1 -> dejo la variable en memoria, disponible para el subproceso de la umic.
			 * varTabMort = btcUmic.tabla1Aseg1  -> dejo la variable en memoria, disponible para el subproceso de la umic
			 * varAnoNac = Año (umic.asegurados.fnacAseg1)
			 * varValoresTabMort = obtenerConfiguracion. recuperarValoresExperiencia(btcUmic.fecCierre, varTabMort, varAnoNac, btcUmic. itcalc1, umic.baseTecIni. psobremort, umic.baseTecIni. priesgo)
			 * VarX = nedad(umic.fechas fecinisus, Varfecnac, VarCriterEdad) -> dejo la variable en memoria, disponible para el subproceso de la umic.
			 * varI1 = btcUmic.itcalc1  --> dejo la variable en memoria, disponible para el subproceso de la umic.
			 * VarDc = ndias(umic.fechas.fdesderenova, umic.datosgenerales.feccierre, VarCriterFec) --> dejo la variable en memoria, disponible para el subproceso de la umic.
			 * VarDr = ndias(umic.fechas.fdesderenova, umic.fechas.fhastarenova , VarCriterFec) --> dejo la variable en memoria, disponible para el subproceso de la umic.
			 * VarTerminal = obtenerConfiguracion.recuperarConfProv(umic.datosgenerales.kmodalidad, umic.datosgenerales.kgarantia, umic.datosgenerales.kprestacion, btcUmic.ktipobt)
			 */
			final BigDecimal varCapital = umic.getCapitales().getIcapini();
			final BigDecimal varPrc = umic.getCapitales().getPorevalcap();
			final BigDecimal varPna0 = umic.getPrimas().getIprimanetaini();
			final BigDecimal varPrp = umic.getPrimas().getPrevprima();
			final String varTipoAport = umic.getDatosGenerales().getCtipoaport();
			final String varSituPol = umic.getDatosGenerales().getCsitupol();
			final Integer varM = umic.getDuraciones().getNdurprima();
			final Integer varN = umic.getDuraciones().getNdursegano();
			final BigDecimal varFallRed = umic.getCapitales().getIcapfall();
			final BigDecimal varGepc = umic.getBti().getPgastgesex1I();
			final BigDecimal varGic = btcUmic.getGtorosspCap();
			final Timestamp varFecnac = umic.getAsegurados().getFnacAseg1();		
			final BigDecimal varI1 = btcUmic.getItcalc().get(0);
						
		
			String varTerminal = UtilModulos.getVarTerminal(mapVariables, CLAVE_VAR_TERMINAL, kmodalidad, kgarantia, umic.getDatosAdicionales().getPrestCal(), btcUmic.getBaseTec());
			
			if (varTerminal.equals("LEIDOBTI")){
				//mapVariables.put(CLAVE_VAR_TERMINAL, null);
				//varTerminal = UtilModulos.getVarTerminal(mapVariables, CLAVE_VAR_TERMINAL, kmodalidad, kgarantia, umic.getDatosGenerales().getKprestacion(), "BTI");
				lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PROY, ConstantsModulos.CTE_BTI, btcUmic.getFecCierre(), umic.getKey());
				varVx5 = lstDetaCor.get(iteracion - 1).getTotalFlujoProyeccion().getTerminalAnterior();
				varVx5b = lstDetaCor.get(iteracion - 1).getTotalFlujoProyeccion().getTerminalPosterior();
				
				if (varVx5 == null){
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DD);
				}	
			}else if(varTerminal.equals("LEIDOBTIPR")){
				lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PROY, ConstantsModulos.CTE_BTI_PROY, btcUmic.getFecCierre(), umic.getKey());
				varVx5 = lstDetaCor.get(iteracion - 1).getTotalFlujoProyeccion().getTerminalAnterior();
				varVx5b = lstDetaCor.get(iteracion - 1).getTotalFlujoProyeccion().getTerminalPosterior();
				
				if (varVx5 == null){
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DD);
				}	
			}
			/**
			 * Para cualquier periodo j se calculará: 
			 * varP = varTc0 + nanos(fcalc, proyUmic(j).varBloque.fecDevengo, varCriterFec) -> sobreescribo la variable en memoria,
			 * disponible para el subproceso de la umic.
			 * -	Si el código de la constante  empieza por  KT
			 *		o	durk1 = TCM(umic.fechas.fecinisus, proyUmic(j).varBloque.fecDevengo);
			 *	-	vark1 -->  obtenerConfiguracion.recuperarCtesRescates(codk1, durk1) --> sobreescribo la variable en memoria, disponible para el subproceso de la umic.
			 *		o	Si el valor de la constante  retornada es nulo se devuelve error funcional 005 - No se ha encontrado la Constante de Rescate  codK1, finalizando el proceso para la UMIC.
			 *	-	Si el código de la constante  empieza por  KT
			 *		o	durk2 = TCM(umic.fechas.fecinisus, proyUmic(j).varBloque.fecDevengo);
			 *	-	vark2 -->  obtenerConfiguracion.recuperarCtesRescates(codk2, durk2) --> sobreescribo la variable en memoria, disponible para el subproceso de la umic.
			 *		o	Si el valor de la constante  retornada es nulo se devuelve error funcional 005 - No se ha encontrado la Constante de Rescate  codK2, finalizando el proceso para la UMIC.
			 *	-	Si el código de la constante  empieza por  KT
			 *		o	durk3 = TCM(umic.fechas.fecinisus, proyUmic(j).varBloque.fecDevengo);
			 *	-	vark3 -->  obtenerConfiguracion.recuperarCtesRescates(codk3, durk3) --> sobreescribo la variable en memoria, disponible para el subproceso de la umic.
	 		 *		o	Si el valor de la constante  retornada es nulo se devuelve error funcional 005 - No se ha encontrado la Constante de Rescate  codK3, finalizando el proceso para la UMIC.
			 */

			Integer varEdifer = umic.getDatosGenerales().getEdifer();
			final BigDecimal varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varFechaEfecto, varFecnac, varCriterEdad, umic.getRentas().getFecIni(), varEdifer);
			final Timestamp fecDesde = proyUmic.get(iteracion-1).getFechaDesde();
			final Timestamp fecHasta = proyUmic.get(iteracion-1).getFechaHasta();
			//final Integer varP = varTc0 + FuncionesAuxiliares.nAnnos(fcalc, bloqueCorriente.getFechaDevengo(), varCriterFec).intValue();
			Integer varP; //= UtilModulos.getVarTC0(mapVariables, CLAVE_VAR_TC0 ,umic.getFechas().getFecinisus(), bloqueCorriente.getFechaDevengo());
			final List<BigDecimal> lstValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterEdad, ConstantesSolvencia.CTE_TABMORT_L);
			
			varP = (Integer) mapVariables.get(CLAVE_VAR_P);
			
			if (varP == null) {
				varP = FuncionesAuxiliares.tc(varFechaEfecto, fecDesde);
				mapVariables.put(CLAVE_VAR_P, varP);
				
				/**
				 * varfecDesdeRenova = umic umic.fechas.fecinisus + entero(varP) años
				 * Si varfecDesdeRenova > proyUmic(j).fecDesde varfecDesdeRenova = varfecDesdeRenova – 1 año
				 * varfecHastaRenova = varfecDesdeRenova + 1 año
				 * varAlfa = alfa(varfecDesdeRenova, varfecHastaRenova, btcUmic.fecCierre, VarTipoAlfa, VarCriterFec)
				 */
				varFecDesdeRenova = UtilFechas.incrAnyo(varFechaEfecto, varP);
				if (varFecDesdeRenova.after(fecDesde)) {
					varFecDesdeRenova = UtilFechas.decreAnios(varFecDesdeRenova, ConstantsFunciones.CTE_1);
				}
				varFecHastaRenova = UtilFechas.incrAnyo(varFecDesdeRenova, ConstantsFunciones.CTE_1);
				varDr = BigDecimal.valueOf(UtilModulos.getVarDr(mapVariables, CLAVE_VAR_DR, varFecDesdeRenova, varFecHastaRenova, varCriterFec));
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
					varDr = BigDecimal.valueOf(UtilModulos.getVarDr(mapVariables, CLAVE_VAR_DR, varFecDesdeRenova, varFecHastaRenova, varCriterFec));
					mapVariables.put(CLAVE_VAR_DESDERENOV, varFecDesdeRenova);
					mapVariables.put(CLAVE_VAR_HASTARENOV, varFecHastaRenova);
				}else{
					varDr = BigDecimal.valueOf(UtilModulos.getVarDr(mapVariables, CLAVE_VAR_DR, varFecDesdeRenova, varFecHastaRenova, varCriterFec));
				}
				
				Integer varPNueva = FuncionesAuxiliares.tc(varFechaEfecto, fecDesde);
				if (varPNueva > varP){
					varP = varPNueva;
					mapVariables.put(CLAVE_VAR_P, varP);
				}
			}
			
			/**
			 * Se obtendrá la fórmula de terminal a utilizar de la configuración de provisiones por fórmula cerrada.
			 * Si varTerminal = "VTX005
			 * varVx5 = VTX005 (varCapital, varPrc, varValoresTabMort, varI1, varDifer, varX, varP, varN, varM,  
			 * varGic, varGepc, varPna0, varPrp, varSituPol, varTipoAport, varIfal, varFallRed, Iant, PAS, FUT)
			 */
			
			if (ConstantsModulos.CTE_VTX005.equals(varTerminal)) {
				
				varVx5 = Terminales.vtx005(varCapital, varPrc, lstValoresTabMort, varI1, varX.intValue(), varP, varN, varM,
					varGic, varGepc, varPna0, varPrp, varSituPol, varTipoAport, varIfal, varFallRed, varIant, varPas, varFut);
				
				/*
				 * TODO De momento calculo el  periodo tc+1 de forma manual. Solución temporal.
				 * varP1 = varTc0 + nanos(fcalc, proyUmic(j).varBloque.fecDevengo + 1 mes, varCriterFec)
				 */
				final Integer varP1 = varP + 1;
				
				varVx5b = Terminales.vtx005(varCapital, varPrc, lstValoresTabMort, varI1, varX.intValue(), varP1, varN, varM,
						varGic, varGepc, varPna0, varPrp, varSituPol, varTipoAport, varIfal, varFallRed, varIant, varPas, varFut);
				
			}
			
			//final BigDecimal varDc = BigDecimal.valueOf(UtilModulos.getVarDc(mapVariables, CLAVE_VAR_DC, umic.getFechas().getFecdesderenova(), umic.getDatosGenerales().getFecCierre(), varCriterFec));
			//final BigDecimal varDr = BigDecimal.valueOf(UtilModulos.getVarDr(mapVariables, CLAVE_VAR_DR, umic.getFechas().getFecdesderenova(), umic.getFechas().getFechastarenova(), varCriterFec));
			/**
			 * Se calculará el importe de rescate correspondiente al periodo j como:
			 * RT008(j) = vark1 * [ varVx5b + (1-vark3) * (dc/(dr-1)) * (varVx5b - varVx5) - vark3 * vark2 * varPna0 * [ (AX(0,varX,varP,varM,varI1)) / (AX(0,varX,0,varM,varI1) ] ]
			 * 
			 * La operacion se va a separar en 4 operaciones:
			 * - operandoDivAX = [ (AX(0,varX,varP,varM,varI1)) / (AX(0,varX,0,varM,varI1) ]
			 * - operandoPna0 = vark3 * vark2 * varPna0 * operandoDivAX
			 * - operandoVarDcYDr = (1-vark3) * (dc/(dr-1)) * (varVx5b - varVx5)
			 * - RT008(j) = vark1 * [ varVx5b + operandoVarDcYDr - operandoPna0 ] ]
			 */
			/** operandoDivAX = [ (AX(0,varX,varP,varM,varI1)) / (AX(0,varX,0,varM,varI1) ] */
			BigDecimal operandoPna0;
			if (vark2.signum()==0 || vark3.signum()==0 || varPna0.signum()==0){
				operandoPna0 = BigDecimal.ZERO;
			}else{
				final BigDecimal operandoDivAX =
						FuncionesRentas.ax(ConstantsFunciones.CTE_0, varX.intValue(), varP, varM, varI1, lstValoresTabMort).divide(
								FuncionesRentas.ax(ConstantsFunciones.CTE_0, varX.intValue(), ConstantsFunciones.CTE_0, varM, varI1, lstValoresTabMort), ConstantsFunciones.MATH_CONTEXT);
				
				/** operandoPna0 = vark3 * vark2 * varPna0 * operandoDivAX */
				operandoPna0 = vark3.multiply(vark2).multiply(varPna0).multiply(operandoDivAX);
			}
			varDc = BigDecimal.valueOf(UtilModulos.getVarDc(mapVariables, CLAVE_VAR_DC, varFecDesdeRenova, fecDesde, varCriterFec));				
			/** operandoVarDcYDr = (1-vark3) * (dc/(dr-1)) * (varVx5b - varVx5) */
			final BigDecimal operandoVarDcYDr = BigDecimal.ONE.subtract(vark3).multiply(varDc.divide(varDr.subtract(BigDecimal.ONE), ConstantsFunciones.MATH_CONTEXT).multiply(varVx5b.subtract(varVx5)));
			
			/** RT008(j) = vark1 * [ varVx5b + operandoVarDcYDr - operandoPna0 ] */
			rt008 = vark1.multiply(varVx5b.add(operandoVarDcYDr).subtract(operandoPna0));
		}
		
		if (ModuloRT008.LOG.isTraceEnabled()) {
			ModuloRT008.LOG.trace("Fin función << moduloRT008 >> de la clase ModuloRT008, para la iteracion = {} con resultado rt008 = {}", iteracion, rt008);
		}
			
		return rt008;
	}
}
