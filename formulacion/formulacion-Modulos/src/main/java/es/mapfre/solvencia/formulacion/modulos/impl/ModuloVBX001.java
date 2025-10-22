package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalFlujoProyeccion;
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
 * Clase que implementa el modulo VBX001.
 * La expresión matemática para su determinación es la siguiente:
 *					Bx(Tc,alfa)= Vx(Tc)+alfa*[Vx(Tc+1)-Vx(Tc)]+(1-alfa)*Pinv(Tc)
 * @author agonzalezgar
 *
 */
public class ModuloVBX001 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVBX001.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VBX001;
	
	private static final String CLAVE_IFAL = ConstantsModulos.CTE_VA_IFAL;
	private static final String CLAVE_VAR_IFAL = CLAVE_IFAL.concat(CLAVE_MODULO);
	
	private static final String CLAVE_PAS = ConstantsModulos.CTE_VA_PAS;
	private static final String CLAVE_VAR_PAS = CLAVE_PAS.concat(CLAVE_MODULO);
	
	private static final String CLAVE_FUT = ConstantsModulos.CTE_VA_FUT;
	private static final String CLAVE_VAR_FUT = CLAVE_FUT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_IANT = ConstantsModulos.CTE_VA_IANT;
	private static final String CLAVE_VAR_IANT = CLAVE_IANT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT;
	private static final String CLAVE_VAR_VAL_TAB_MORT = CLAVE_VAL_TAB_MORT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_ALFA = ConstantsModulos.CTE_VAR_ALFA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TIPO_ALFA = ConstantsModulos.CTE_VA_TIPO_ALFA.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_X = ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_P = ConstantsModulos.CTE_VAR_P.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_DESDERENOV = ConstantsModulos.CTE_VAR_DESDERENOV.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_HASTARENOV = ConstantsModulos.CTE_VAR_HASTARENOV.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_P1 = ConstantsModulos.CTE_VAR_P1.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VX = ConstantsModulos.CTE_VAR_VX.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VX1 = ConstantsModulos.CTE_VAR_VX1.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PINV = ConstantsModulos.CTE_VAR_PINV.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PNA = ConstantsModulos.CTE_VAR_PNA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PRP = ConstantsModulos.CTE_VAR_PRP.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_UNO_MAS_VAR_PRP = ConstantsModulos.CTE_UNO_MAS_VAR_PRP.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_UNO_MENOS_VAR_GEPC = ConstantsModulos.CTE_UNO_MENOS_VAR_GEPC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_N = ConstantsModulos.CTE_N.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_M1 = ConstantsModulos.CTE_VAR_M.concat(CLAVE_MODULO);
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
		//Fin varaibles locales
		
		try {
			if (ModuloVBX001.LOG.isTraceEnabled()) {
				ModuloVBX001.LOG.trace("Inicio de execute en clase ModuloVBX001");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloVBX001
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			//final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String terminal = (String) args[ConstantsModulos.PARAM_P_TERMINAL];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo VBX001
			resultado = moduloVBX001(proyUmic, iteracion, fcalc, umic, btcUmic, mapVariables, terminal);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVBX001.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVBX001.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloVBX001.LOG.isTraceEnabled()) {
			ModuloVBX001.LOG.trace("Fin de execute en clase ModuloVBX001");
		}
		return resultado;
	}
	
	/**Módulo que calcula el importe nominal para la proyección de Provisión Matemática por Fórmula Cerrada
	 * La expresión matemática para su determinación es la siguiente:
	 * 				Bx(Tc,alfa)= Vx(Tc)+alfa*[Vx(Tc+1)-Vx(Tc)]+(1-alfa)*Pinv(Tc).
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
	 * @param terminal
	 * 			Terminal de cálculo
	 */
	private TotalFlujoProyeccion moduloVBX001(final List<DetalleCorriente> proyUmic, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables,
			final String terminal) {
		//Variables locales
		final TotalFlujoProyeccion salida = new TotalFlujoProyeccion();
		BigDecimal vbx001 = BigDecimal.ZERO;
		String varCriFec;
		String varCriEdad;
		BigDecimal varIfal = BigDecimal.ZERO;
		BigDecimal varPas = BigDecimal.ZERO;
		BigDecimal varFut = BigDecimal.ZERO;
		BigDecimal varIant = BigDecimal.ZERO;
		String  varTipoAlfa;
		BigDecimal varCapital = BigDecimal.ZERO;
		BigDecimal varPrc;
		BigDecimal varPNA0;
		BigDecimal varPrp;
		String varTipoAport;
		String varSituPol;
		Integer varM;
		Integer varM1;
		Integer varN = 0;
		BigDecimal varFallRed;
		BigDecimal varGepc;
		BigDecimal varGic;
		BigDecimal varGipc;
		Timestamp varFecnac;
		BigDecimal varX;
		BigDecimal varI1;
		BigDecimal varI2;
		Integer varp;
		Integer varpRenova;
		BigDecimal varPna = BigDecimal.ZERO;
		BigDecimal varPinv = BigDecimal.ZERO;
		BigDecimal varAlfa = BigDecimal.ZERO;
		BigDecimal varVx = null;
		BigDecimal varVx1 = null;
		int varp1 = 0;
		Timestamp varFecDesdeRenova = null;
		Timestamp varFecHastaRenova = null;
		DetalleCorriente detCorr;
		Timestamp fecDesde;
		Timestamp fecDesdeMenos1Dia;
		BigDecimal varCapTc;
		Integer varDifer;
		//Fin variables locales
		
		
		
		if (ModuloVBX001.LOG.isTraceEnabled()) {
			ModuloVBX001.LOG.trace("Inicio función << moduloVBX001 >> de la clase ModuloVBX001, para la iteracion = {}", iteracion);
		}
		
		//Validacion campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, fcalc, umic, btcUmic);
		
		/**
		 * Si estoy en el primer periodo, se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, así como las variables internas que tampoco varíen por periodo, y que se dejarán accesibles para su uso en el subproceso por los siguientes periodos a calcular.
			Variables de Apoyo
			-	VarCriterFec --> obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
			-	VarCriterEdad --> obtenerConfiguracion.recuperarVariableApoyo(ID-CRITERIO)
			-	VarIfal --> obtenerConfiguracion.recuperarVariableApoyo(IFAL)
			-	VarPas --> obtenerConfiguracion.recuperarVariableApoyo(PAS)
			-	VarFut --> obtenerConfiguracion.recuperarVariableApoyo(FUT)
			-	VarIAnt  --> obtenerConfiguracion.recuperarVariableApoyo(IANT)
			-	VarTipoAlfa --> obtenerConfiguracion.recuperarVariableApoyo(TIPO_ALFA)
			
			o	Si alguna de las variables de apoyo  retornadas es nulo se devuelve error funcional 005 - 
			No se ha encontrado la Variable de Apoyo &NombreVariableApoyo, finalizando el proceso para la UMIC.
			
			Variables Módulo
			-	varCapital =  umic.capitales.icapini  --> dejo la variable en memoria, disponible para el subproceso de la umic.
			-	varPrc= umic.capitales.porevalcap  --> dejo la variable en memoria, disponible para el subproceso de la umic.
			-	varPna0 = umic.primas.iprimanetaini --> dejo la variable en memoria disponible para el procesado de la umic.
			-	varPrp = umic.primas.prevprima --> dejo la variable en memoria disponible para el procesado de la umic.
			-	varTipoAport = umic.datosgenerales.ctipoaport --> dejo la variable en memoria disponible para el procesado de la umic.
			-	varSituPol = umic.datosgenerales.csitupol --> dejo la variable en memoria disponible para el procesado de la umic.
			-	varM =  umic.duraciones.ndurprima
			-	varN = umic.duraciones.ndursegano
			-	varFallRed = umic.capitales.icapfall --> dejo la variable en memoria disponible para el procesado de la umic.
			-	varGepc = umic.baseTecIni.pgastgesex1
			-	varGic = btcUmic.gtoRosspCap
			-	VarFecnac = umic.asegurados.fnacAseg1 --> dejo la variable en memoria, disponible para el subproceso de la umic.
			-	varTabMort = btcUmic.tabla1Aseg1 --> dejo la variable en memoria, disponible para el subproceso de la umic
			-	varAnoNac = Año (umic.asegurados.fnacAseg1);
			-	varValoresTabMort = obtenerConfiguracion. recuperarValoresExperiencia(btcUmic.fecCierre, varTabMort, varAnoNac, btcUmic. itcalc1, umic.baseTecIni. psobremort, umic.baseTecIni. priesgo)
			-   varI1 = btcUmic.itcalc1 --> dejo la variable en memoria, disponible para el subproceso de la umic.
			-	VarX = nedad(umic.fechas fecinisus, Varfecnac, VarCriterEdad) --> dejo la variable en memoria, disponible para el subproceso de la umic.
			
			-	varTc0 = TC(varFechaEfecto, fcalc) --> dejo la variable en memoria, disponible para el subproceso de la umic.
			-	varDifer = TC(umic.fechas fecinisus, umic.rentas. fecini) --> dejo la variable en memoria, disponible para el subproceso de la umic.
		 */
		
		
		// Se definen estas variables auziliares dado que se llaman varias veces.
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		
		// Cálculo de las variables de apoyo.
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
		varCriEdad = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		varTipoAlfa = UtilModulos.getVarTipoAlfa(mapVariables, CLAVE_VAR_TIPO_ALFA, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec());
		// Fin del cálculo de las variables de apoyo.
		
		// Validación de las variables de apoyo.
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriEdad);
		} // Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		
		
		//Si el terminal es VTX001, VTX002, VTXVEPU o VTXTPFPU no es necesario calcular las variables de apoyo IFAL, IANT, PAS y FUT
		if (!ConstantsModulos.CTE_VTX001.equals(terminal) && !ConstantsModulos.CTE_VTX002.equals(terminal) &&
				!ConstantsModulos.CTE_VTXVEPU.equals(terminal) && !ConstantsModulos.CTE_VTXTPFPU.equals(terminal)){
			varIfal = UtilModulos.getVarIFal(mapVariables, CLAVE_VAR_IFAL, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_IFAL, umic.getBti().getPintertecnI1());
			varPas = UtilModulos.getVarPas(mapVariables, CLAVE_VAR_PAS, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_PAS);
			varFut = UtilModulos.getVarFut(mapVariables, CLAVE_VAR_FUT, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_FUT);
			varIant = UtilModulos.getVarIant(mapVariables, CLAVE_VAR_IANT, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_IANT);
			if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
				ValidacionesComunesModulos.validarVariableDeApoyoVarIFal(varIfal);
				ValidacionesComunesModulos.validarVariableDeApoyoIant(varIant);
			}
		}
		
		if (null == varTipoAlfa) {
			varTipoAlfa = ConstantsFunciones.CTE_CAL_PRORRATA;
		}
		
		if (varPas == null){
			varPas = BigDecimal.ZERO;
		}
		
		if (varFut == null){
			varFut = BigDecimal.ZERO;
		}
		// Fin de la validación de las variables de apoyo.
		
		Timestamp varFechaIni = null;
		Timestamp varFechaIniEfecto = null;
		varSituPol = umic.getDatosGenerales().getCsitupol();
		if (ConstantsModulos.CTE_DG_CSI_NO_RED.equals(varSituPol)) {
			// Si la modalidad de la UMIC que se trata es del negocio Individuales, se toma la fecha
			// de fin de efecto en lugar de la fecha de inicio. (Cambio de alcance 09/2015)
			if (umic.getDatosGenerales().getCnegocio().equals(ConstantesSolvencia.NEGOCIO_INDIVIDUAL)){
				varFechaIni = umic.getFechas().getFecefecini();
			} else {
				varFechaIni = umic.getFechas().getFecinisus();
			}
			
			varFechaIniEfecto = varFechaIni;
			varN = umic.getDuraciones().getNdursegano();
		} else if (ConstantsModulos.CTE_DG_CSITU_RED.equals(varSituPol)) {
			varFechaIni = umic.getFechas().getFecefecred();
			
			if (umic.getDatosGenerales().getCnegocio().equals(ConstantesSolvencia.NEGOCIO_INDIVIDUAL)){
				varN = umic.getDuraciones().getNdursegano() - FuncionesAuxiliares.nAnnos(umic.getFechas().getFecefecini(), varFechaIni, varCriFec).intValue();
				varFechaIniEfecto = umic.getFechas().getFecefecini();
			} else {
				varN = umic.getDuraciones().getNdursegano() - FuncionesAuxiliares.nAnnos(umic.getFechas().getFecinisus(), varFechaIni, varCriFec).intValue();
				varFechaIniEfecto = umic.getFechas().getFecinisus();
			}
			
		}
		
		if (ConstantsModulos.CTE_VTX003.equals(terminal) || ConstantsModulos.CTE_VTXTPFPU.equals(terminal)){
			varCapital = umic.getCapitales().getIcapact();
		}else{
			varCapital = umic.getCapitales().getIcapini();
		}

		varPrc = umic.getCapitales().getPorevalcap();
		varPNA0 = umic.getPrimas().getIprimanetaini();
		varPrp = umic.getPrimas().getPrevprima();
		varTipoAport = umic.getDatosGenerales().getCtipoaport();
		varM = umic.getDuraciones().getNdurprima();
		varFallRed = umic.getCapitales().getIcapfall();
		varGepc = umic.getBti().getPgastgesex1I();
		varGic = btcUmic.getGtorosspCap();
		varGipc =btcUmic.getGtorosspPrima();
		varFecnac = umic.getAsegurados().getFnacAseg1();
		varI1 = btcUmic.getItcalc().get(0);
		varI2 = btcUmic.getItcalc().get(1);
		varCapTc = umic.getCapitales().getIcapact();
		varDifer = umic.getRentas().getNadifer();
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		
		List<BigDecimal> lstValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriEdad, ConstantesSolvencia.CTE_TABMORT_L);
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varFechaIni, varFecnac, varCriEdad, umic.getRentas().getFecIni(), varEdifer);
				
		/**
		 * Para cualquier periodo j se calculará: 
				varP = varTc0 + nanos(fcalc, proyUmic(j).varBloque.fecDesde, varCriterFec) --> sobreescribo la variable en memoria, disponible para el subproceso de la umic.
			Si terminal = VTX005 se calculará:
			
				Si j = 1 se calculará: 
					Si varM > varP entonces 
						varPna =  varPna0 * (1+(varPrp* 0.01))^(varP ) 
						varPinv = varPna * (1- (varGepc* 0.01) 
					En cualquier otro caso
						varPna =  0
						varPinv = 0
			
				varAlfa = alfa(umic.fechas.fecdesderenova, umic.fechas.fechastarenova, btcUmic.fecCierre, VarTipoAlfa, VarCriterFec);
			
			Para cualquier j se calculará:
				varVx5 = VTX005 (varCapital, varPrc, valoresTabMort, varI1, varDifer, varX, varP, varN, varM,  varGic, varGepc, varPna0, varPrp, varSituPol, varTipoAport, varIfal, varFallRed, varIant, varPas, varFut)
				varP1  = varTc0 + nanos(fcalc, proyUmic(j).varBloque.fecHasta, varCriterFec)
				varVx51 = VTX005 (varCapital, varPrc, valoresTabMort, varI1, varDifer, varX, varP1, varN, varM,  varGic, varGepc, varPna0, varPrp, varSituPol, varTipoAport, varIfal, varFallRed, varIant, varPas, varFut)
				
				Finalmente se hallará VBX001 como: 
					vbx001(j) = varVx5  + varAlfa * (varVx51 - varVx5) + (1 – varAlfa)* varPinv
				proyUmic(j).impFlujoNominal  =  vbx001(j)
		 */
		detCorr = proyUmic.get(iteracion - 1);
		fecDesde = detCorr.getFechaDesde();
		fecDesdeMenos1Dia = UtilFechas.decreDias(fecDesde, 1);
		
		
		varp = (Integer)mapVariables.get(CLAVE_VAR_P);
		
		// Se comprueba si varP es nulo
		if (varp == null) {
			varp = FuncionesAuxiliares.tc(varFechaIni, fecDesdeMenos1Dia);
			varpRenova = FuncionesAuxiliares.tc(varFechaIniEfecto, fecDesdeMenos1Dia);
			mapVariables.put(CLAVE_VAR_P, varp);
			//varpRenov = FuncionesAuxiliares.tc(umic.getFechas().getFecinisus(), fecDesde);
			/**
			 * varfecDesdeRenova = umic umic.fechas.fecinisus + entero(varP) años
			 * Si varfecDesdeRenova > proyUmic(j).fecDesde varfecDesdeRenova = varfecDesdeRenova – 1 año
			 * varfecHastaRenova = varfecDesdeRenova + 1 año
			 * varAlfa = alfa(varfecDesdeRenova, varfecHastaRenova, btcUmic.fecCierre, VarTipoAlfa, VarCriterFec)
			 */
			
			
			
			varFecDesdeRenova = UtilFechas.incrAnyo(varFechaIniEfecto, varpRenova);
			if (varFecDesdeRenova.after(fecDesde)) {
				varFecDesdeRenova = UtilFechas.decreAnios(varFecDesdeRenova, ConstantsFunciones.CTE_1);
			}
			varFecHastaRenova = UtilFechas.incrAnyo(varFecDesdeRenova, ConstantsFunciones.CTE_1);
			varAlfa = UtilModulos.getVarAlfa(mapVariables, CLAVE_VAR_ALFA, varFecDesdeRenova, varFecHastaRenova, fecDesdeMenos1Dia, varTipoAlfa, varCriFec);
			mapVariables.put(CLAVE_VAR_DESDERENOV, varFecDesdeRenova);
			mapVariables.put(CLAVE_VAR_HASTARENOV, varFecHastaRenova);
			mapVariables.put(CLAVE_VAR_ALFA, varAlfa);

					
			// Si varM mayor que varP
			if (varM > varp) {
				if (ConstantsModulos.CTE_REV_GEO.contains(umic.getPrimas().getCformarevprim())){
					varPna = varPNA0.multiply(Util.pow(UtilModulos.getVarUnoMasPrp(mapVariables, CLAVE_VAR_UNO_MAS_VAR_PRP, varPrp), varp));
				} else if (ConstantsModulos.CTE_REV_ARI.contains(umic.getPrimas().getCformarevprim())){
					varPna = varPNA0.multiply(BigDecimal.ONE.add(UtilModulos.getPorcentaje(mapVariables, CLAVE_VAR_PRP, varPrp).multiply(BigDecimal.valueOf(varp))));
				} else {
					varPna = varPNA0;
				}
				
				varPinv = varPna.multiply(UtilModulos.getVarUnoMenosGepc(mapVariables, CLAVE_VAR_UNO_MENOS_VAR_GEPC, varGepc));
				mapVariables.put(CLAVE_VAR_PINV, varPinv);
			} else {
				varPinv = BigDecimal.ZERO;
				varPna = BigDecimal.ZERO;
				mapVariables.put(CLAVE_VAR_PINV, varPinv);
				mapVariables.put(CLAVE_VAR_PNA, varPna);
			}
			
			if (ConstantsModulos.CTE_VTX002.equals(terminal)){
				varVx = Terminales.vtx002(varCapTc, lstValoresTabMort, varGic, varI1, varDifer, varX.intValue(), varp, varN);
				mapVariables.put(CLAVE_VAR_VX, varVx);
				varp1 = varp + 1;
				mapVariables.put(CLAVE_VAR_P1, varp1);
				varVx1 = Terminales.vtx002(varCapTc, lstValoresTabMort, varGic, varI1, varDifer, varX.intValue(), varp1, varN);
				mapVariables.put(CLAVE_VAR_VX1, varVx1);
				
			} else if (ConstantsModulos.CTE_VTX005.equals(terminal)) {
				
				
				varVx = Terminales.vtx005(varCapital, varPrc, lstValoresTabMort, varI1, varX.intValue(), varp, 
						varN, varM, varGic, varGepc, varPNA0, varPrp, varSituPol, varTipoAport, varIfal, varFallRed, varIant, varPas, varFut);
				mapVariables.put(CLAVE_VAR_VX, varVx);
				
				varp1 = varp + 1;
				mapVariables.put(CLAVE_VAR_P1, varp1);
				
				
				varVx1 = Terminales.vtx005(varCapital, varPrc, lstValoresTabMort, varI1, varX.intValue(), varp1, 
						varN, varM, varGic, varGepc, varPNA0, varPrp, varSituPol, varTipoAport, varIfal, varFallRed, varIant, varPas, varFut);
				mapVariables.put(CLAVE_VAR_VX1, varVx1);
				// Fin if (ConstantsModulos.CTE_VTX005.equals(terminal))

			} else if(ConstantsModulos.CTE_VTX003.equals(terminal)){
				varVx = Terminales.vtx003(varCapital, lstValoresTabMort, varI1, varX.intValue(), varp, varN, 
						varM, varGipc, varGepc, varPNA0, varPrp, varIfal, varIant);
				mapVariables.put(CLAVE_VAR_VX, varVx);
				varp1 = varp + 1;
				mapVariables.put(CLAVE_VAR_P1, varp1);
				if (varp<varN){
					varVx1 = Terminales.vtx003(varCapital, lstValoresTabMort, varI1, varX.intValue(), varp1, varN, 
							varM, varGipc, varGepc, varPNA0, varPrp, varIfal, varIant);
				}else{
					varVx1 = varVx;
				}
				mapVariables.put(CLAVE_VAR_VX1, varVx1);
				
			} else if (ConstantsModulos.CTE_VTX004.equals(terminal)){
				varVx = Terminales.vtx004(varCapital, varPrc, lstValoresTabMort, varI1, varX.intValue(), varp, varN, 
						varM, varGic, varGepc, varPNA0, varPrp, varSituPol, varTipoAport, varIfal, varFallRed, varIant, varPas, varFut);
				mapVariables.put(CLAVE_VAR_VX, varVx);
				
				varp1 = varp + 1;
				mapVariables.put(CLAVE_VAR_P1, varp1);
				
				varVx1 = Terminales.vtx004(varCapital, varPrc, lstValoresTabMort, varI1, varX.intValue(), varp1, varN,
						varM, varGic, varGepc, varPNA0, varPrp, varSituPol, varTipoAport, varIfal, varFallRed, varIant, varPas, varFut);
				mapVariables.put(CLAVE_VAR_VX1, varVx1);
			} else if (ConstantsModulos.CTE_VTX001.equals(terminal)){
				varVx = Terminales.vtx001();
				varVx1 = varVx;
				mapVariables.put(CLAVE_VAR_VX, varVx);
				mapVariables.put(CLAVE_VAR_VX1, varVx1);
				
				varp1 = varp + 1;
				mapVariables.put(CLAVE_VAR_P1, varp1);
			} else if (ConstantsModulos.CTE_VTXVEPU.equals(terminal)){
				varM1 = FuncionesAuxiliares.nAnnos(varFechaIniEfecto, umic.getBti().getFecFinTramo1(), varCriFec).intValue();
				varN = FuncionesAuxiliares.nAnnos(varFechaIniEfecto, proyUmic.get(proyUmic.size()-1).getFechaDesde(), varCriFec).intValue();
				mapVariables.put(CLAVE_VAR_M1, varM1);
				mapVariables.put(CLAVE_VAR_N, varN);
				
				
				varVx = Terminales.vtxVepu(varFecnac, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), varX, varp, varN, varPrc, lstValoresTabMort, varM1, varI1, varI2, varCapital, varPNA0, varGic, varGipc, varCriFec, mapVariables, varFechaIniEfecto, varPinv);
				mapVariables.put(CLAVE_VAR_VX, varVx);
				
				varp1 = varp + 1;
				mapVariables.put(CLAVE_VAR_P1, varp1);
				
				if (umic.getDatosGenerales().getCtipoaport().equals(ConstantsFunciones.CTE_APOR_UNICA)){
					varPinv = BigDecimal.ZERO;
				}
				varVx1 = Terminales.vtxVepu(varFecnac, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(),  varX, varp1, varN, varPrc, lstValoresTabMort, varM1, varI1, varI2, varCapital, varPNA0, varGic, varGipc, varCriFec, mapVariables, varFechaIniEfecto, varPinv);
				mapVariables.put(CLAVE_VAR_VX1, varVx1);
				
			} else if (ConstantsModulos.CTE_VTXTPFPU.equals(terminal)){
				varM1 = FuncionesAuxiliares.nAnnos(varFechaIniEfecto, umic.getBti().getFecFinTramo1(), varCriFec).intValue();
				mapVariables.put(CLAVE_VAR_M1, varM1);
				
				varVx = Terminales.vtxTpfpu(varFecnac, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(),  varX, varp, varN, varPrc, lstValoresTabMort, varM1, varI1, varI2, varCapital, varPNA0, varGic, varGipc, varCriFec, mapVariables, varFechaIniEfecto, varPinv);
				mapVariables.put(CLAVE_VAR_VX, varVx);
				
				varp1 = varp + 1;
				mapVariables.put(CLAVE_VAR_P1, varp1);

				if (umic.getDatosGenerales().getCtipoaport().equals(ConstantsFunciones.CTE_APOR_UNICA)){
					varPinv = BigDecimal.ZERO;
				}
				varVx1 = Terminales.vtxTpfpu(varFecnac, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(),  varX, varp1, varN, varPrc, lstValoresTabMort, varM1, varI1, varI2, varCapital, varPNA0, varGic, varGipc, varCriFec, mapVariables, varFechaIniEfecto, varPinv);
				mapVariables.put(CLAVE_VAR_VX1, varVx1);
			}

			
			
		} else {
			
			/**
			 * varfecDesdeRenova = varfecHastaRenova -> Se guardan las variables en memoria para el subproceso de la umic.
			 * varfecHastaRenova = varfecHastaRenova + 1 año. -> Se guardan las variables en memoria para el subproceso de la umic.
			 * varAlfa = alfa(varfecDesdeRenova, varfecHastaRenova, btcUmic.fecCierre, VarTipoAlfa, VarCriterFec)
			 */
			varFecDesdeRenova = (Timestamp) mapVariables.get(CLAVE_VAR_DESDERENOV);
			varFecHastaRenova = (Timestamp) mapVariables.get(CLAVE_VAR_HASTARENOV);
			if (fecDesde.after(varFecHastaRenova)) {
				varFecDesdeRenova = varFecHastaRenova;
				varFecHastaRenova = UtilFechas.incrAnyo(varFecHastaRenova, ConstantsFunciones.CTE_1);
				
				mapVariables.put(CLAVE_VAR_DESDERENOV, varFecDesdeRenova);
				mapVariables.put(CLAVE_VAR_HASTARENOV, varFecHastaRenova);
			}
			varAlfa = UtilModulos.getVarAlfa(mapVariables, CLAVE_VAR_ALFA, varFecDesdeRenova, varFecHastaRenova, fecDesdeMenos1Dia, varTipoAlfa, varCriFec);
			mapVariables.put(CLAVE_VAR_ALFA, varAlfa);
			
			Integer varPNueva = FuncionesAuxiliares.tc(varFechaIni, fecDesdeMenos1Dia);
			
			if (varPNueva > varp) {
				
				// Se actualiza varP con la nueva calculada
				varp = varPNueva;
				mapVariables.put(CLAVE_VAR_P, varp);
				varVx1 = (BigDecimal) mapVariables.get(CLAVE_VAR_VX1);
				varVx = varVx1;
				mapVariables.put(CLAVE_VAR_VX, varVx);
				
				// Si varM mayor que varP
				if (varM > varp) {
					if (ConstantsModulos.CTE_REV_GEO.contains(umic.getPrimas().getCformarevprim())){
						varPna = varPNA0.multiply(Util.pow(UtilModulos.getVarUnoMasPrp(mapVariables, CLAVE_VAR_UNO_MAS_VAR_PRP, varPrp), varp));
					} else if (ConstantsModulos.CTE_REV_ARI.contains(umic.getPrimas().getCformarevprim())){
						varPna = varPNA0.multiply(BigDecimal.ONE.add(UtilModulos.getPorcentaje(mapVariables, CLAVE_VAR_PRP, varPrp).multiply(BigDecimal.valueOf(varp))));
					} else {
						varPna = varPNA0;
					}
					
					varPinv = varPna.multiply(UtilModulos.getVarUnoMenosGepc(mapVariables, CLAVE_VAR_UNO_MENOS_VAR_GEPC, varGepc));
					mapVariables.put(CLAVE_VAR_PINV, varPinv);
					mapVariables.put(CLAVE_VAR_PNA, varPna);
					
				} else {
					varPinv = BigDecimal.ZERO;
					varPna = BigDecimal.ZERO;
					mapVariables.put(CLAVE_VAR_PINV, varPinv);
					mapVariables.put(CLAVE_VAR_PNA, varPna);
				}
				// Incrementar en 1 y guardar en el mapa
				varp1 = (int) mapVariables.get(CLAVE_VAR_P1);
				varp1 = varp + 1;
				mapVariables.put(CLAVE_VAR_P1, varp1);
				
				if (ConstantsModulos.CTE_VTX002.equals(terminal)){
					varVx1 = Terminales.vtx002(varCapTc, lstValoresTabMort, varGic, varI1, varDifer, varX.intValue(), varp1, varN);
					mapVariables.put(CLAVE_VAR_VX1, varVx1);
					
				}else if (ConstantsModulos.CTE_VTX005.equals(terminal)){
					varVx1 = Terminales.vtx005(varCapital, varPrc, lstValoresTabMort, varI1, varX.intValue(), varp1, 
							varN, varM, varGic, varGepc, varPNA0, varPrp, varSituPol, varTipoAport, varIfal, varFallRed, varIant, varPas, varFut);
					mapVariables.put(CLAVE_VAR_VX1, varVx1);
					
				} else if(ConstantsModulos.CTE_VTX003.equals(terminal)){
					if (varp<varN){
						varVx1 = Terminales.vtx003(varCapital, lstValoresTabMort, varI1, varX.intValue(), varp1, varN, 
							varM, varGipc, varGepc, varPNA0, varPrp, varIfal, varIant);
					}else{
						varVx1 = varVx;
					}
					mapVariables.put(CLAVE_VAR_VX1, varVx1);
					
				} else if (ConstantsModulos.CTE_VTX004.equals(terminal)){
					varVx1 = Terminales.vtx004(varCapital, varPrc, lstValoresTabMort, varI1, varX.intValue(), varp1, 
							varN, varM, varGic, varGepc, varPNA0, varPrp, varSituPol, varTipoAport, varIfal, varFallRed, varIant, varPas, varFut);
					mapVariables.put(CLAVE_VAR_VX1, varVx1);
				} else if (ConstantsModulos.CTE_VTX001.equals(terminal)){
					varVx = Terminales.vtx001();
					varVx1 = varVx;
					mapVariables.put(CLAVE_VAR_VX, varVx);
					mapVariables.put(CLAVE_VAR_VX1, varVx1);
				} else if (ConstantsModulos.CTE_VTXVEPU.equals(terminal)){
					varM1 = (Integer) mapVariables.get(CLAVE_VAR_M1);
					varN = (Integer) mapVariables.get(CLAVE_VAR_N);
					varVx1 = Terminales.vtxVepu(varFecnac, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(),  varX, varp1, varN, varPrc, lstValoresTabMort, varM1, varI1, varI2, varCapital, varPNA0, varGic, varGipc, varCriFec, mapVariables, varFechaIniEfecto, varPinv);
					mapVariables.put(CLAVE_VAR_VX1, varVx1);
				} else if (ConstantsModulos.CTE_VTXTPFPU.equals(terminal)){
					varM1 = (Integer) mapVariables.get(CLAVE_VAR_M1);
					varVx1 = Terminales.vtxTpfpu(varFecnac, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(),  varX, varp1, varN, varPrc, lstValoresTabMort, varM1, varI1, varI2, varCapital, varPNA0, varGic, varGipc, varCriFec, mapVariables, varFechaIniEfecto, varPinv);
					mapVariables.put(CLAVE_VAR_VX1, varVx1);
				}

			}
		}
		

		varAlfa = (BigDecimal) mapVariables.get(CLAVE_VAR_ALFA);
		varVx = (BigDecimal) mapVariables.get(CLAVE_VAR_VX);
		varVx1 = (BigDecimal) mapVariables.get(CLAVE_VAR_VX1);
		varPinv = (BigDecimal) mapVariables.get(CLAVE_VAR_PINV);
				
				
		// Se han detectado casos en los que las variables no cambian
		vbx001 = varVx.add(varAlfa.multiply(varVx1.subtract(varVx))).add(BigDecimal.ONE.subtract(varAlfa).multiply(varPinv));
		
		
		if (ModuloVBX001.LOG.isTraceEnabled()) {
			ModuloVBX001.LOG.trace("Fin función << moduloVBX001 >> de la clase ModuloVBX001, para la iteracion = {} con resultado vbx001 = {}, terminalAnterior = {}, terminalPosterior = {}", iteracion, vbx001, varVx, varVx1);
		}
		
		/**
		 * Y se  retornarán también los terminales:
		 * 		terminalAnterior = varVx5
		 * 		terminalPosterior = varVx51
		 */
		
		salida.setProvbtiproy(vbx001);
		salida.setTerminalAnterior(varVx);
		salida.setTerminalPosterior(varVx1);
		
		return salida;
	}
}
