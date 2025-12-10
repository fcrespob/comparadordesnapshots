package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.SwCobroComisionesKey;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.SwCobroComisionesDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Comisiones;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.SwCobroComisiones;
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
import es.mapfre.solvencia.util.ConstantsFactorias;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.SwCobroCom;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.SwCobroComDao;
import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.SwCobroComKey;

/**
 * Clase que implementa el modulo COM001.
 * Módulo de cálculo que devuelve el importe de comisiones a pagar a los agentes en cada punto necesario.
 * La expresión matemática para su determinación es la siguiente:
 * 				CSP(COM, tc) = %COM_P(tc)/100  * CSP(PRIMA, tc)  +  %COM_PM * Bx(tc)/NPP 
 *
 * @author agonzalezgar
 *
 */
public class ModuloCOM001 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCOM001.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_COM001;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_NPP = ConstantsModulos.CTE_VA_NPP;
	private static final String CLAVE_VAR_NPP = CLAVE_NPP.concat(CLAVE_MODULO);
	
//	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
//	private static final String CLAVE_NUM_ANUA = ConstantsModulos.CTE_NUM_ANUA.concat(CLAVE_MODULO);
	private static final String CLAVE_LST_PROY = ConstantsModulos.CTE_LST_PROY.concat(CLAVE_MODULO);
	private static final String CLAVE_LST_PRI = ConstantsModulos.CTE_LST_PRI.concat(CLAVE_MODULO);
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
			if (ModuloCOM001.LOG.isTraceEnabled()) {
				ModuloCOM001.LOG.trace("Inicio de execute en clase ModuloCOM001");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCOM001
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final Boolean swCobroCom = (Boolean) args[8];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo COM001
			resultado = moduloCOM001(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,swCobroCom);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCOM001.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCOM001.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCOM001.LOG.isTraceEnabled()) {
			ModuloCOM001.LOG.trace("Fin de execute en clase ModuloCOM001");
		}
				
		return resultado;
				
	}
	/**
	 * Módulo de cálculo que devuelve el importe de comisiones a pagar a los agentes en cada punto necesario.
	 * La expresión matemática para su determinación es la siguiente:
	 *			 CSP(COM, tc) = %COM_P(tc)/100  * CSP(PRIMA, tc)  +  %COM_PM * Bx(tc)/NPP
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
	private BigDecimal moduloCOM001(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, Boolean swCobroCom) {
		//Variables locales
		BigDecimal com001 = BigDecimal.ZERO;
		BigDecimal varComi = BigDecimal.ZERO;
		BigDecimal varPrimas = BigDecimal.ZERO;
		BigDecimal varNPP = BigDecimal.ZERO;
		BigDecimal varProvMat = BigDecimal.ZERO;
		String varCriFec;
		int varNumAnualidades = 0;
		//int ubicNperiComi = 0;
		String varBx = ConstantsFunciones.CTE_CADENA_VACIA;
		List<DetalleCorriente> lstDetaCor = null;
		boolean calcularCom001 = true;
		SwCobroComDao swCobroComDao = new SwCobroComDao();
		//Fin variables locales
		
		if (ModuloCOM001.LOG.isTraceEnabled()) {
			ModuloCOM001.LOG.trace("Inicio función << FPTOZC >> de la clase ModuloCOM001, para la iteracion = {}", iteracion);
		}
		
		//Validamos los valores de entrada de la función
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Si no tiene fecha devengo, se devuelve 0
		if (null == bloqueCorriente.getFechaDevengo()) {
				return com001;
		}
		
		if(swCobroCom){
			int anyo = UtilFechas.getAnio(UtilFechas.incrDias(fcalc, 1));
			//anyo--;
			Timestamp limiteInferior = new Timestamp(new GregorianCalendar(anyo, 00, 01, ConstantsFunciones.CTE_0,
					ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
			Timestamp limiteSuperior = new Timestamp(new GregorianCalendar(anyo, 11, 31, ConstantsFunciones.CTE_0,
					ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
			
			if(bloqueCorriente.getFechaDevengo().before(limiteInferior) || bloqueCorriente.getFechaDevengo().after(limiteSuperior)){
				return com001;
			}
		}
		
		/**
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, 
		 * así como las variables internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso 
		 * por los siguientes periodos a calcular. 
			Variables de Apoyo
			-	VarCriterFec --> obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
			-	VarNpp--> obtenerConfiguracion.recuperarVariableApoyo(NPP)
			Variables Módulo
			-	varNumAnualidades = TC(umic.fechas.fecinisus, fcalc) --> dejo la variable en memoria, disponible para el subproceso de la umic.
			Para cualquier periodo j se calculará: 
			-	varNumAnualidades (j)= varNumAnualidades + ENTERO(naños(fcalc, proyUmic(j).varBloque.fecDevengo, varCriterFec)) --> sobreescribo la variable en memoria, disponible para el subproceso de la umic.
		 */
		
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
		}
		
		/**
		 	La siguiente comprobación se hace dentro del propio método.
		 		if (null == varNPP) {
					varNPP = ConstantsFunciones.CTE_OPER_12;
				}
		 */
		varNPP = UtilModulos.getVarNpp(mapVariables, CLAVE_VAR_NPP, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_NPP);
		
		/**
		 	Dado que varTCM solo se usaba para calcular el número de anualidades y varTCM no varía a lo largo de las iteraciones se ha
		 	optado porque varTCM se calcule dentro de varNumAnualidades.
		 */
		
		// Si la modalidad de la UMIC que se trata es del negocio Individuales, se toma la fecha
		// de fin de efecto en lugar de la fecha de inicio. (Cambio de alcance 09/2015)
		if (umic.getDatosGenerales().getCnegocio().equals(ConstantesSolvencia.NEGOCIO_INDIVIDUAL)){
			varNumAnualidades = FuncionesAuxiliares.tc(umic.getFechas().getFecefecini(), bloqueCorriente.getFechaDevengo());
			
		} else {
			varNumAnualidades = FuncionesAuxiliares.tc(umic.getFechas().getFecinisus(), bloqueCorriente.getFechaDevengo());
		}
		
		
		
		/**
		 * Una vez calculado varNumAnualidades(j) se ubicará cada anualidad en el campo umic.comisiones.npericomi1, 2 3 en función del valor 
		 * de cada uno de ellos, para obtener a su vez el campo umic.comisiones.pcomisiona1, 2, 3 y umic.comisiones.basecalculocomi1, 2, 3 correspondientes, 
		 * es decir, con la fecha de devengo del periodo (j) sabemos el NPERICOMI que le aplica. Sabiendo el NPERICOMI1-2-3, 
		 * sabemos también la base de cálculo BASECALCUCOMI1-2-3 (PR-Prima y PV-Provisión) y sabemos el porcentaje correspondiente y PCOMISIONA1-2-3.(Porcentaje).
		 * 
		 * Si el periodo (j) es mayor a lo que indique el valor de NPERICOMI3, no hay más cálculos.
		 */
		
		
		Integer npericomi1 = umic.getComisiones().getNpericomi1();
		Integer npericomi2 = umic.getComisiones().getNpericomi2();
		Integer npericomi3 = umic.getComisiones().getNpericomi3();
		
		SwCobroComKey keycom = new SwCobroComKey(umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza());
		SwCobroCom swCobrocom = swCobroComDao.get(keycom);
		
		BigDecimal porcentaje = BigDecimal.ONE;
		if(null != swCobrocom && null != swCobrocom.getPcorrector()){
			if (swCobrocom.getPcorrector().compareTo(BigDecimal.valueOf(100)) > 0) {
				porcentaje = BigDecimal.ONE;
			}else {
				porcentaje = swCobrocom.getPcorrector();
				porcentaje = porcentaje.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
			}
			
		}
		
		if (0 <= varNumAnualidades && varNumAnualidades < npericomi1){
			varComi = umic.getComisiones().getPcomisiona1().multiply(porcentaje);
			varBx = umic.getComisiones().getBasecalcucomi1();
		} else if (npericomi1 <= varNumAnualidades && varNumAnualidades < (npericomi1 + npericomi2)){
			varComi = umic.getComisiones().getPcomisiona2().multiply(porcentaje);
			varBx = umic.getComisiones().getBasecalcucomi2();
		} else if ((npericomi1 + npericomi2) <= varNumAnualidades && varNumAnualidades < (npericomi1 + npericomi2 + npericomi3)){
			varComi = umic.getComisiones().getPcomisiona3().multiply(porcentaje);
			varBx = umic.getComisiones().getBasecalcucomi3();
		} 
		
		
		//ubicNperiComi = ubicarNumAnualidadesEnNPericomi123(umic.getComisiones(), varNumAnualidades);
		
		//if (ubicNperiComi != ConstantsFunciones.CTE_MENOS_1 && ubicNperiComi != ConstantsFunciones.CTE_0) {
		
		if (varComi.signum() != 0){
		
			/**
			 * - varBx --> se buscará si la base de cálculo es primas o provisión en los datos del bloque de comisiones de la umic (umic.comisiones. basecalculocomi1, 2, 3 ) 
			 * 				en función de la anualidad en la que esté ubicada el periodo (j).
			 * 
			 * 	-	varCom --> se buscará el % de comisiones en los datos del bloque de comisiones de la umic (umic.comisiones.pcomisiona1, 2, 3) 
			 *    en función de la anualidad en la que esté ubicada el periodo (j).
			 */
//			varBx = obtenerBaseCalculoComiPorNPeriComi(umic.getComisiones(), ubicNperiComi);
//			varComi = obtenerComisionesPorNperiComi(umic.getComisiones(), ubicNperiComi);
			
			/**
			 * Si varBx = PR (Primas)
					-	Se obtendrá la corriente de primas (PROY_PRIMAS ) previamente calculada en BEL para la umic. Para ello se llamará a la operación: 
					o	listaCorrienteUmic = obtenerDatos.recuperarProyeccion(btcUmic.fecCierre, BEL, cnegocio, kpoliza, ccanal, ksubpoliza,
						ccartera, kcertificado, fecCierre, nsuscri, kmodalidad, kprestación, kajuste, norden, kgarantia, ctipoaport)
					-	varPrimas = listaCorrienteUmic(j).corrientePrimas.impFlujoNominal
					
					-	varProvMat (j) = 0
				Si varBx = PV (Provisión)
					-	Se evaluará el campo MESPAGOCOMI de la siguiente manera:
						-	Si MESPAGOCOMI no tiene valor
							o	Se calcularan comisiones en todos los periodos j, es decir: 
									varProvMat (j) = listaCorrienteUmic(j). totalFlujoProyeccion. provbtiproy 

						-	Si MESPAGOCOMI = "3112"
							o	Se calcularán comisiones solo en el periodo del año en el que el devengo de la comisión sea 31/12/aaaaa.
					
					-	Se obtendrá la corriente de provisión matemática (PROY_PRV ) previamente calculada en BTI para la umic. Para ello se llamará a la operación: 
					o	listaCorrienteUmic = obtenerDatos.recuperarProyeccion(btcUmic.fecCierre, BTI, cnegocio, kpoliza, ccanal, ksubpoliza,
						ccartera, kcertificado, fecCierre, nsuscri, kmodalidad, kprestación, kajuste, norden, kgarantia, ctipoaport)
					-	varProvMat = listaCorrienteUmic(j).totalFlujoProyeccion.provbtiproy
					-	varComi = 0
					-	varPrimas(j) = 0
			 */			
			if (ConstantsModulos.CTE_COMI_PR.equals(varBx)) {
				//Se obtendrá la corriente de primas (PROY_PRIMAS ) previamente calculada en BEL para la umic. Para ello se llamará a la operación: 
				//listaCorrienteUmic = obtenerDatos.recuperarProyeccion(btcUmic.fecCierre, BEL, cnegocio, kpoliza, ccanal, ksubpoliza, ccartera, kcertificado, fecCierre, nsuscri, kmodalidad, kprestación, kajuste, norden, kgarantia, ctipoaport)
				//varPrimas = listaCorrienteUmic(j).corrientePrimas.impFlujoNominal
				
				if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL)){
					lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI, ConstantsModulos.CTE_BT_BEL, btcUmic.getFecCierre(), umic.getKey());
				}else if(
					btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17) ||
					btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIF17LIR) ||
					btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_N17LIRIN) ||
					btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIFF17OCI) ||
					btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17IF) ||
					btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_N17CLIR) ||
					btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NF17AEN) ||
					btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NF17MFE) ||
					btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NF17GTO)){
					lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI, ConstantsModulos.CTE_BTI, btcUmic.getFecCierre(), umic.getKey());
					
					if(null == lstDetaCor || lstDetaCor.size() == 0){
						mapVariables.remove(CLAVE_LST_PRI);
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI, ConstantsModulos.CTE_BTI_PROY, btcUmic.getFecCierre(), umic.getKey());

					}

				}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA)){
					lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI, ConstantsModulos.CTE_VAL_BELCOA, btcUmic.getFecCierre(), umic.getKey());
				}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR)){
					lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI, ConstantsModulos.CTE_VAL_BELCLR, btcUmic.getFecCierre(), umic.getKey());
				}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTIU)){
					lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI, ConstantsModulos.CTE_VAL_SCRTIU, btcUmic.getFecCierre(), umic.getKey());			
				}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTID)){				
					lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI, ConstantsModulos.CTE_VAL_SCRTID, btcUmic.getFecCierre(), umic.getKey());
				}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRGTO)){				
					lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI, ConstantsModulos.CTE_VAL_SCRGTO, btcUmic.getFecCierre(), umic.getKey());
				}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMFE)){				
					lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI, ConstantsModulos.CTE_VAL_SCRMFE, btcUmic.getFecCierre(), umic.getKey());
				}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMMI)){				
					lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI, ConstantsModulos.CTE_VAL_SCRMMI, btcUmic.getFecCierre(), umic.getKey());
				}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCF)){				
					lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI, ConstantsModulos.CTE_VAL_SCRMCF, btcUmic.getFecCierre(), umic.getKey());
				}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCI)){				
					lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI, ConstantsModulos.CTE_VAL_SCRMCI, btcUmic.getFecCierre(), umic.getKey());
				}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLFE)){				
					lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI, ConstantsModulos.CTE_VAL_SCRLFE, btcUmic.getFecCierre(), umic.getKey());
				}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLMI)){				
					lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI, ConstantsModulos.CTE_VAL_SCRLMI, btcUmic.getFecCierre(), umic.getKey());
				}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC)){				
					lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI, ConstantsModulos.CTE_VAL_SCRINC, btcUmic.getFecCierre(), umic.getKey());
				}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRVM)){				
					lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI, ConstantsModulos.CTE_VAL_SCRVM, btcUmic.getFecCierre(), umic.getKey());
				}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEP)){				
					lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI, ConstantsModulos.CTE_VAL_SCRAEP, btcUmic.getFecCierre(), umic.getKey());
				}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEN)){				
					lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI, ConstantsModulos.CTE_VAL_SCRAEN, btcUmic.getFecCierre(), umic.getKey());
				}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIP)){				
					lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI, ConstantsModulos.CTE_VAL_SCRAIP, btcUmic.getFecCierre(), umic.getKey());
				}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIN)){				
					lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI, ConstantsModulos.CTE_VAL_SCRAIN, btcUmic.getFecCierre(), umic.getKey());
				}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM)){				
					lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI, ConstantsModulos.CTE_VAL_SCRANM, btcUmic.getFecCierre(), umic.getKey());
				}
				
				if(null == lstDetaCor.get(iteracion - 1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_PRIMA)){
					lstDetaCor.get(iteracion - 1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_PRIMA).setImpFlujoNominal(BigDecimal.ZERO);
					varPrimas = lstDetaCor.get(iteracion - 1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_PRIMA).getImpFlujoNominal();

				}else{
					final BloqueCorriente bloquePrima = lstDetaCor.get(iteracion - 1).getBloqueBySubproceso(ConstantsModulos.CTE_PROY_PRIMA);
					if ( null == bloquePrima.getImpFlujoNominal()) {
						bloquePrima.setImpFlujoNominal(BigDecimal.ZERO);
					}
					varPrimas = bloquePrima.getImpFlujoNominal();
				}
			
				varProvMat = BigDecimal.ZERO; 
			} else if (ConstantsModulos.CTE_COMI_PV.equals(varBx)) {
				
				/**
				 * Se evaluará el campo MESPAGOCOMI de la siguiente manera:
				 * -	Si MESPAGOCOMI no tiene valor
				 * 		o	Se calcularan comisiones en todos los periodos j
				 * -	Si MESPAGOCOMI = "3112"
				 * 		o	Se calcularán comisiones solo en el periodo del año en el que el devengo de la comisión sea 31/12/aaaaa.
				 */
				final String mespagocomi = umic.getComisiones().getMespagocomi();
				final Timestamp fecDevengo = bloqueCorriente.getFechaDevengo();
				calcularCom001 = calcularComisiones(mespagocomi, fecDevengo);
				varPrimas = BigDecimal.ZERO;
				
				//Se obtendrá la corriente de provisión matemática (PROY_PRV ) previamente calculada en BTI para la umic. Para ello se llamará a la operación:
				lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PROY, ConstantsModulos.CTE_BTI, btcUmic.getFecCierre(), umic.getKey());
				if(null == lstDetaCor || lstDetaCor.size() == 0){
					mapVariables.remove(CLAVE_LST_PROY);
					lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PROY, ConstantsModulos.CTE_BTI_PROY, btcUmic.getFecCierre(), umic.getKey());

				}
				if(lstDetaCor == null || lstDetaCor.size() == 0){
					varProvMat = BigDecimal.ZERO;
				}else{
					varProvMat = lstDetaCor.get(iteracion - 1).getTotalFlujoProyeccion().getProvbtiproy();
				}
			}
			
			if (calcularCom001) {
				final BigDecimal varComiDiv100 = varComi.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
				//COM001(j) = ((varComi/100) * varPrimas) +  (varComi*0.01) * (varProvMat / varNPP)
				//com001 = varComi.divide(ConstantsFunciones.CTE_OPER_100, ConstantsFunciones.MATH_CONTEXT).multiply(varPrimas).add(varProvMat.divide(varNPP, ConstantsFunciones.MATH_CONTEXT));
				com001 = varComiDiv100.multiply(varPrimas).add(varComiDiv100.multiply(varProvMat.divide(varNPP, ConstantsFunciones.MATH_CONTEXT)));
			}
		}
		
		if (ModuloCOM001.LOG.isTraceEnabled()) {
			ModuloCOM001.LOG.trace("Fin función << FPTOZC >> de la clase ModuloCOM001, para la iteracion = {} con resultado com001 = {}", iteracion, com001);
		}
			
		return com001;
	}
	
	/**
	 * Se evaluará el campo MESPAGOCOMI de la siguiente manera:
	 * -	Si MESPAGOCOMI no tiene valor
	 * 		o	Se calcularan comisiones en todos los periodos j, es decir: 
				-	varProvMat (j) = listaCorrienteUmic(j). totalFlujoProyeccion. provbtiproy 

	 * -	Si MESPAGOCOMI = "3112"
	 * 		o	Se calcularán comisiones solo en el periodo del año en el que el devengo de la comisión sea 31/12/aaaaa.
	 */
	private boolean calcularComisiones(final String mespagocomi, final Timestamp fecDevengo) {
		
		boolean calcularCom001 = false;
		
		Integer diaDevengo = null;
		Integer mesDevengo = null;
		if (fecDevengo != null) {
			diaDevengo = UtilFechas.getDia(fecDevengo);
			mesDevengo = UtilFechas.getMes(fecDevengo);
		}
		
		// Para cumplir las condiciones anteriores, si devolverá un booleano si se cumplen todas las siguientes condiciones:
		calcularCom001 =
				
				// Si MESPAGOCOMI no tiene valor o si MESPAGOCOMI es igual a "MENS", se calcularan comisiones en todos los periodos j 
				null == mespagocomi || mespagocomi.isEmpty() || mespagocomi.equals("MENS") ||
		
				// Si MESPAGOCOMI = "3112" se calcularán comisiones solo en el periodo del año en el que el devengo de la comisión sea 31/12/aaaaa.
				(ConstantsModulos.CTE_DIAMES_3112.equals(mespagocomi) && diaDevengo != null && diaDevengo == 31 && mesDevengo != null && mesDevengo == 12);
		
		return calcularCom001;
	}
	
	/**
	 * Función encargada de ubicar el numero de anualidades del periodo j dentro de un NpericomiX de tal forma que si:
	 * 		- Si NPERICOMI1=1, NPERICOMI2=1, NPERICOMI3=1, se ubicara el número de anualidades con las condiciones definidas en la función obtenerUbicacionTipoCaso1
	 * 		- Si NPERICOMI1=1, NPERICOMI2=2, NPERICOMI3=3, se ubicara el número de anualidades con las condiciones definidas en la función obtenerUbicacionTipoCaso2
	 *  	- Si NPERICOMI1=1, NPERICOMI2=1, NPERICOMI3=99, se ubicara el número de anualidades con las condiciones definidas en la función obtenerUbicacionTipoCaso3
	 * 
	 * @param comUmic Dato comisiones de la umic
	 * @param varNumAnualidades numero de anualides del periodo j
	 * @return nPericomiUbicado 
	 */
	private Integer ubicarNumAnualidadesEnNPericomi123(final Comisiones comUmic, final Integer varNumAnualidades) {
		//Variables locales
		int nPericomiUbicado = 0;
		Integer tipoCaso = 0;
		//Fin variables locales
		
		if (ModuloCOM001.LOG.isTraceEnabled()) {
			ModuloCOM001.LOG.trace("Inicio de la función << ubicarNumAnualidadesEnNPericomi123 >> de la clase ModuloCOM001");
		}
		
		tipoCaso = evalucarCasoNPericomi(comUmic);

		if (tipoCaso.equals(ConstantsFunciones.CTE_1)) {
			nPericomiUbicado = obtenerUbicacionTipoCaso1(varNumAnualidades);
		} else if (tipoCaso.equals(ConstantsFunciones.CTE_2)) {
			nPericomiUbicado = obtenerUbicacionTipoCaso2(varNumAnualidades);
		} else if (tipoCaso.equals(ConstantsFunciones.CTE_3)) {
			nPericomiUbicado = obtenerUbicacionTipoCaso3(varNumAnualidades);
		}
		
		if (ModuloCOM001.LOG.isTraceEnabled()) {
			ModuloCOM001.LOG.trace("Fin de la función << ubicarNumAnualidadesEnNPericomi123 >> de la clase ModuloCOM001");
		}
		
		return Integer.valueOf(nPericomiUbicado);
	}
	
	/**
	 * Función encargada de ubicar el numero de anualidades cuando se cumple que NPERICOMI1=1, NPERICOMI2=1, NPERICOMI3=99, si esta condición previa se cumple se toman las siguientes decisiones:
	 * 			- Si el número de anualidades es 1, queda situado en NPERICOMI1
	 * 			- Si el número de anualidades es 2, queda situado en NPERICOMI2
	 * 			- Si el número de anualidades es 3, queda situado en NPERICOMI3
	 * 
	 * @param varNumAnualidades
	 * @return nPericomiUbicado
	 */
	private Integer obtenerUbicacionTipoCaso3(final Integer varNumAnualidades) {
		//Variables locales
		int nPericomiUbicado = 0;
		//Fin variables locales
		
		if (ModuloCOM001.LOG.isTraceEnabled()) {
			ModuloCOM001.LOG.trace("Inicio de la función << obtenerUbicacionTipoCaso3 >> de la clase ModuloCOM001");
		}
		
		if (varNumAnualidades.equals(ConstantsFunciones.CTE_1)) {
			nPericomiUbicado = ConstantsFunciones.CTE_1;
		} else if (varNumAnualidades.equals(ConstantsFunciones.CTE_2)) {
			nPericomiUbicado = ConstantsFunciones.CTE_2;
		} else {
			nPericomiUbicado = ConstantsFunciones.CTE_3;
		}
		
		if (ModuloCOM001.LOG.isTraceEnabled()) {
			ModuloCOM001.LOG.trace("Fin de la función << obtenerUbicacionTipoCaso3 >> de la clase ModuloCOM001");
		}
		
		return Integer.valueOf(nPericomiUbicado);
	}
	
	/**
	 * Función encargada de ubicar el numero de anualidades cuando se cumple que NPERICOMI1=1, NPERICOMI2=2, NPERICOMI3=3, si esta condición previa se cumple se toman las siguientes decisiones:
	 * 			- Si el número de anualidades es mayor que 6, no se proseguira con el calculo
	 * 			- Si el número de anualidades esta comprendido entre 3 y 6 (este incluido), queda situado en NPERICOMI3
	 * 			- Si el número de anualidades es 3 o 2, queda situado en NPERICOMI2
	 * 			- Si el número de anualidades es 1, queda situado en NPERICOMI1
	 * 
	 * @param varNumAnualidades
	 * @return Integer
	 */
	private Integer obtenerUbicacionTipoCaso2(final Integer varNumAnualidades) {
		//Variables locales
		int nPericomiUbicado = 0;
		//Fin variables locales
		
		if (ModuloCOM001.LOG.isTraceEnabled()) {
			ModuloCOM001.LOG.trace("Inicio de la función << obtenerUbicacionTipoCaso2 >> de la clase ModuloCOM001");
		}
		
		if (varNumAnualidades > ConstantsFunciones.CTE_6) {
			nPericomiUbicado = ConstantsFunciones.CTE_MENOS_1;
		} else if (varNumAnualidades > ConstantsFunciones.CTE_3 && varNumAnualidades <= ConstantsFunciones.CTE_6) {
			nPericomiUbicado = ConstantsFunciones.CTE_3;
		} else if (varNumAnualidades.equals(ConstantsFunciones.CTE_2) || varNumAnualidades.equals(ConstantsFunciones.CTE_3)) {
			nPericomiUbicado = ConstantsFunciones.CTE_2;
		} else {
			nPericomiUbicado = ConstantsFunciones.CTE_1;
		}
		
		if (ModuloCOM001.LOG.isTraceEnabled()) {
			ModuloCOM001.LOG.trace("Fin de la función << obtenerUbicacionTipoCaso2 >> de la clase ModuloCOM001");
		}
		
		return Integer.valueOf(nPericomiUbicado);
	}
	
	/**
	 * Función encargada de ubicar el numero de anualidades cuando se cumple que NPERICOMI1=1, NPERICOMI2=1, NPERICOMI3=1, si esta condición previa se cumple se toman las siguientes decisiones:
	 *			- Si el número de anualidades es mayor que 3, no se proseguira con el calculo
	 *			- Si el número de anualidades es 1, queda situado en NPERICOMI1
	 * 			- Si el número de anualidades es 2, queda situado en NPERICOMI2
	 * 			- Si el número de anualidades es 3, queda situado en NPERICOMI3
	 * @param varNumAnualidades
	 * @return nPericomiUbicado
	 */
	private Integer  obtenerUbicacionTipoCaso1(final Integer varNumAnualidades) {
		//Variables locales
		int nPericomiUbicado = 0;
		//Fin variables locales
		
		if (ModuloCOM001.LOG.isTraceEnabled()) {
			ModuloCOM001.LOG.trace("Inicio de la función << obtenerUbicacionTipoCaso1 >> de la clase ModuloCOM001");
		}
		
		if (varNumAnualidades > ConstantsFunciones.CTE_3) {
			nPericomiUbicado = ConstantsFunciones.CTE_MENOS_1;
		} else if (varNumAnualidades.equals(ConstantsFunciones.CTE_1)) {
			nPericomiUbicado = ConstantsFunciones.CTE_1;
		} else if (varNumAnualidades.equals(ConstantsFunciones.CTE_2)) {
			nPericomiUbicado = ConstantsFunciones.CTE_2;
		} else if (varNumAnualidades.equals(ConstantsFunciones.CTE_3)) {
			nPericomiUbicado = ConstantsFunciones.CTE_3;
		}
		
		if (ModuloCOM001.LOG.isTraceEnabled()) {
			ModuloCOM001.LOG.trace("Fin de la función << obtenerUbicacionTipoCaso1 >> de la clase ModuloCOM001");
		}
		
		return Integer.valueOf(nPericomiUbicado);
		
	}
	
	/**
	 * Función encargada de comprobar en que estado esta los campos umic.comisiones.npericomi1, umic.comisiones.npericomi2 y umic.comisiones.npericomi3 y establecer las siguientes condiciones:
	 * 	1 -NPERICOMI1=1, NPERICOMI2=1, NPERICOMI3=1 significa que cada tramo tiene un año de vigencia, es decir, que NPERICOMI1 aplica el primer año, NPERICOMI2 aplica el segundo año y NPERICOMI3 aplica el tercer año
	 * 	2 -NPERICOMI1=1, NPERICOMI2=2, NPERICOMI3=3 significa que NPERICOMI1 aplica el primer año, NPERICOMI2 aplica el segundo y el tercero y NPERICOMI3 aplica el tercero, cuarto y quinto año
	 *  3 -NPERICOMI1=1, NPERICOMI2=1, NPERICOMI3=99 (este caso es el más habitual) significa que NPERICOMI1 aplica el primer año, NPERICOMI2, aplica el segundo año y NPERICOMI3 aplica el tercer y siguientes años de vigencia de la póliza.
	 * @param comUmic
	 * @return casoNPericomi
	 */
	private Integer evalucarCasoNPericomi (final Comisiones comUmic) {
		//Variables locales
		int casoNPericomi = 0;
		//Fin variables locales
		
		if (ModuloCOM001.LOG.isTraceEnabled()) {
			ModuloCOM001.LOG.trace("Inicio de la función << evalucarCasoNPericomi >> de la clase ModuloCOM001");
		}
		
		/**
		 * Los campos umic.comisiones.npericomi1, umic.comisiones.npericomi2 y umic.comisiones.npericomi3 corresponden al nº de periodos (anualidades) de aplicación de los % de comisiones, pudiendo tomar los siguientes valores:
			Si NPERICOMI1=1, NPERICOMI2=1, NPERICOMI3=1 significa que cada tramo tiene un año de vigencia, es decir, 
			que NPERICOMI1 aplica el primer año, NPERICOMI2 aplica el segundo año y NPERICOMI3 aplica el tercer año.
			
			Si NPERICOMI1=1, NPERICOMI2=2, NPERICOMI3=3 significa que NPERICOMI1 aplica el primer año, NPERICOMI2 
			aplica el segundo y el tercero y NPERICOMI3 aplica el tercero, cuarto y quinto año.
			
			Si NPERICOMI1=1, NPERICOMI2=1, NPERICOMI3=99 (este caso es el más habitual) significa que NPERICOMI1 
			aplica el primer año, NPERICOMI2, aplica el segundo año y NPERICOMI3 aplica el tercer y siguientes años de vigencia de la póliza.
		 */
		casoNPericomi = devolverNpericomi(comUmic, ConstantsFunciones.CTE_1, ConstantsFunciones.CTE_1, ConstantsFunciones.CTE_1, ConstantsFunciones.CTE_1);
		
		if (casoNPericomi == 0) {
			casoNPericomi = devolverNpericomi(comUmic, ConstantsFunciones.CTE_1, ConstantsFunciones.CTE_2, ConstantsFunciones.CTE_3, ConstantsFunciones.CTE_2);
		}
		
		if (casoNPericomi == 0) {
			casoNPericomi = devolverNpericomi(comUmic, ConstantsFunciones.CTE_1, ConstantsFunciones.CTE_1, ConstantsFunciones.CTE_99, ConstantsFunciones.CTE_3);
		}
		
		if (ModuloCOM001.LOG.isTraceEnabled()) {
			ModuloCOM001.LOG.trace("Fin de la función << evalucarCasoNPericomi >> de la clase ModuloCOM001");
		}
		
		return casoNPericomi;
	}
	
	/**
	 * Devuelve el valor dependiendo del valor de salida si los valores de valorEsperadoX coinciden con los valores de comUmic.getNpericomiX
	 * @param comUmic
	 * @param valorEsperado1
	 * @param valorEsperado2
	 * @param valorEsperado3
	 * @param devolver
	 * @return
	 */
	private int devolverNpericomi(final Comisiones comUmic, final Integer valorEsperado1, final Integer valorEsperado2, final Integer valorEsperado3, final Integer devolver) {
		
		int salida = 0;
		
		if (valorEsperado1.equals(comUmic.getNpericomi1()) && valorEsperado2.equals(comUmic.getNpericomi2()) && (valorEsperado3.equals(comUmic.getNpericomi3()) || valorEsperado3.equals(ConstantsFunciones.CTE_99))) {
			salida = devolver;
		}
		
		return salida;
	}
	
	/**
	 * Función encargada de obtener la base de calculo de la comisión, mediante la ubicación previa del numero de anualidades en el npericomi correpondiente
	 * de tal forma que:
	 * 
	 * 	- Si el número de anulidades fue ubicado en el npericomi1 (ubicacionNperiComi = 1) obtenemos el valor umic.comisiones.basecalcucomi1
	 * 	- Si el número de anulidades fue ubicado en el npericomi2 (ubicacionNperiComi = 2) obtenemos el valor umic.comisiones.basecalcucomi2
	 * 	- Si el número de anulidades fue ubicado en el npericomi3 (ubicacionNperiComi = 3) obtenemos el valor umic.comisiones.basecalcucomi3 
	 * 
	 * @param comUmic Dato comisiones de la umic
	 * @param ubicNperiComi indicador de en que npericomi fue ubicado el numero de anualidades del periodo j
	 * @return baseCalculoComi
	 */
	private String obtenerBaseCalculoComiPorNPeriComi(final Comisiones comUmic,  final Integer ubicNperiComi) {
		//Variables locales
		String baseCalculoComi = ConstantsFunciones.CTE_CADENA_VACIA;
		//Fin variables locales
		
		if (ModuloCOM001.LOG.isTraceEnabled()) {
			ModuloCOM001.LOG.trace("Inicio de la función << obtenerBaseCalculoComiPorNumAnualidades >> de la clase ModuloCOM001");
		}

		if (ubicNperiComi.equals(ConstantsFunciones.CTE_1)) {
			baseCalculoComi = comUmic.getBasecalcucomi1();
		} else if (ubicNperiComi.equals(ConstantsFunciones.CTE_2)) {
			baseCalculoComi = comUmic.getBasecalcucomi2();
		} else if (ubicNperiComi.equals(ConstantsFunciones.CTE_3)) {
			baseCalculoComi = comUmic.getBasecalcucomi3();
		}
		
		if (ModuloCOM001.LOG.isTraceEnabled()) {
			ModuloCOM001.LOG.trace("Fin de la función << obtenerBaseCalculoComiPorNumAnualidades >> de la clase ModuloCOM001");
		}
		
		return baseCalculoComi;
	}
	
	/**
	 * Función encargada de obtener el % de comisiones en función del numero de anualidades del periodo j, de tal forma que:
	 * 
	 * 	- Si el número de anulidades fue ubicado en el npericomi1 (ubicacionNperiComi = 1) obtenemos el valor umic.comisiones.pcomisiona1
	 * 	- Si el número de anulidades fue ubicado en el npericomi2 (ubicacionNperiComi = 2) obtenemos el valor umic.comisiones.pcomisiona2
	 * 	- Si el número de anulidades fue ubicado en el npericomi3 (ubicacionNperiComi = 3) obtenemos el valor umic.comisiones.pcomisiona3
	 * 
	 * @param comUmic Dato comisiones de la umic
	 * @param ubicNperiComi indicador de en que npericomi fue ubicado el numero de anualidades del periodo j
	 * @return tantoPorCienComi
	 */
	private BigDecimal obtenerComisionesPorNperiComi(final Comisiones comUmic,  final Integer ubicNperiComi) {
		//Variables locales
		BigDecimal tantoPorCienComi = BigDecimal.ZERO;
		//Fin variables locales
		
		if (ModuloCOM001.LOG.isTraceEnabled()) {
			ModuloCOM001.LOG.trace("Inicio de la función << obtenerComisionesPorNperiComi >> de la clase ModuloCOM001");
		}
		
		if (ubicNperiComi.equals(ConstantsFunciones.CTE_1)) {
			tantoPorCienComi = comUmic.getPcomisiona1();
		} else if (ubicNperiComi.equals(ConstantsFunciones.CTE_2)) {
			tantoPorCienComi = comUmic.getPcomisiona2();
		} else if (ubicNperiComi.equals(ConstantsFunciones.CTE_3)) {
			tantoPorCienComi = comUmic.getPcomisiona3();
		}
		
		if (ModuloCOM001.LOG.isTraceEnabled()) {
			ModuloCOM001.LOG.trace("Fin de la función << obtenerComisionesPorNperiComi >> de la clase ModuloCOM001");
		}
		
		return tantoPorCienComi;
	}

}
