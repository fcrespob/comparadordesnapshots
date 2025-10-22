package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.formulacion.CriterioFechas;
import es.mapfre.solvencia.dominio.maestro.Capitales;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.LimitesCapital;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TerminosPMCUmic;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalFlujoProyeccion;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesFallecimiento;
import es.mapfre.solvencia.formulacion.util.FuncionesGastos;
import es.mapfre.solvencia.formulacion.util.FuncionesPrimas;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo VBX700.  
 * 				
 * @author ogperez
 *
 */
public class ModuloVBX700 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVBX700.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VBX700;
	
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;	
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	
	private static final String CLAVE_TCM = ConstantsModulos.CTE_VAR_TCM;
	private static final String CLAVE_VAR_TCM = CLAVE_TCM.concat(CLAVE_MODULO);
	
	private static final String CLAVE_TTM = ConstantsModulos.CTE_VAR_TTM;
	private static final String CLAVE_VAR_TTM = CLAVE_TTM.concat(CLAVE_MODULO);
	
	private static final String CLAVE_X = ConstantsModulos.CTE_VAR_X;
	private static final String CLAVE_VAR_X = CLAVE_X.concat(CLAVE_MODULO);	
	
	private static final String CLAVE_W = ConstantsModulos.CTE_VAR_W;
	private static final String CLAVE_VAR_W = CLAVE_W.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT;
	private static final String CLAVE_VAR_VAL_TAB_MORT = CLAVE_VAL_TAB_MORT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_NP = ConstantsModulos.CTE_VAR_NP;
	private static final String CLAVE_VAR_NP = CLAVE_NP.concat(CLAVE_MODULO);
	
	private static final String CLAVE_T = ConstantsModulos.CTE_VAR_T;
	private static final String CLAVE_VAR_T = CLAVE_T.concat(CLAVE_MODULO);	
	
	private static final String CLAVE_VAR_LIMITE = ConstantsModulos.CTE_VAR_LIMITE.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_LIMITE_CAP = ConstantsModulos.CTE_VAR_LIMITE_CAP.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_LISTA_LIMITE_CAP = ConstantsModulos.CTE_LISTA_LIMITE.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	private static final String CLAVE_VAR_GAST_310 = ConstantsModulos.CTE_VAR_GAST310.concat(CLAVE_MODULO);
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
			
			if (ModuloVBX700.LOG.isTraceEnabled()) {
				ModuloVBX700.LOG.trace("Inicio de execute en clase ModuloVBX700");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloVBX700
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente =  (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			//final String terminal = (String) args[ConstantsModulos.PARAM_P_TERMINAL];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo VBX700
			resultado = moduloVBX700(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVBX700.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVBX700.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloVBX700.LOG.isTraceEnabled()) {
			ModuloVBX700.LOG.trace("Fin de execute en clase ModuloVBX700");
		}
		
		return resultado;
	}
	
	/**
	 * Módulo de cálculo de la provisión matemática de la Umic
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
	 * 
	 */
	private TotalFlujoProyeccion moduloVBX700(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables,
			final String codSubproceso) {		
		//Variables locales
		BigDecimal vbx700 = BigDecimal.ZERO;
		final TotalFlujoProyeccion salida = new TotalFlujoProyeccion();
		Timestamp varfechaEfecto;
		String varCriterioEdad;
		BigDecimal varIfal;
		Integer varTCm;
		Integer varTtm;
		Integer varMescie;
		Integer varDiaVto;
		Integer varDiaPrima;
		BigDecimal varGgim;
		BigDecimal varPna;
		BigDecimal varPrp;
		BigDecimal varX;
		BigDecimal varK;
		BigDecimal varCC06 = BigDecimal.ZERO;
		BigDecimal varCRMax;
		BigDecimal varI1;
		Integer varW;
		List<BigDecimal> varValoresTabMort;
		Integer varNP;
		Integer vart;
		BigDecimal varSaldoAnt = BigDecimal.ZERO;
		BigDecimal varGast = BigDecimal.ZERO;
		BigDecimal varPrima = BigDecimal.ZERO;
		BigDecimal varGastP = BigDecimal.ZERO;
		BigDecimal varCfall = BigDecimal.ZERO;
		BigDecimal varIntG = BigDecimal.ZERO;
		BigDecimal varIntP = BigDecimal.ZERO;
		TerminosPMCUmic terminosPMCsUmic;
		TerminosPMCUmic terminosPMCUmicAnt;
		List<LimitesCapital> varListaLimCap;
		BigDecimal varLimite;
		final IObtenerConfiguracion obtenerConf = FachadaServicios.getObtenerConfiguracion();
		CriterioFechas criterioFechas = null;
		String criterioFecPago = ConstantsFunciones.CTE_CADENA_VACIA;
		String criterioFecDev = ConstantsFunciones.CTE_CADENA_VACIA;
		//Fin variables locales
		
		if (ModuloVBX700.LOG.isTraceEnabled()) {
			ModuloVBX700.LOG.trace("Inicio función << moduloVBX700 >> de la clase ModuloVBX700, para la iteracion = {}", iteracion);
		}
		
		// Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
	
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		
		// Cálculo y validación de las variables de apoyo.
		varCriterioEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterioEdad);
			// Fin de la validación de las variables de apoyo.
		}
		
		criterioFechas = obtenerConf.recuperarCriterioFechas(umic.getDatosGenerales().getKmodalidad(),
				umic.getDatosGenerales().getKgarantia(), umic.getDatosGenerales().getKprestacion(), umic.getDatosAdicionales().getPrestCal(),
				ConstantesSolvencia.CTE_PROY_PRIMA);

		ValidacionesComunesModulos.validarCriterioFechaRecuperado(criterioFechas,
				umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), umic.getDatosGenerales().getKprestacion());

		criterioFecPago = criterioFechas.getFecPago();
		criterioFecDev = criterioFechas.getFecDevengo();
		
		terminosPMCsUmic = UtilModulos.getSetTerminosPMCUmic(umic.getKey(), btcUmic.getBaseTec(), iteracion);
		
		varfechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		varTCm = UtilModulos.getVarTCm(mapVariables, CLAVE_VAR_TCM, iteracion, varfechaEfecto, proyUmic.get(0).getFcierre());
		varTtm = UtilModulos.getVarTCmIni(mapVariables, CLAVE_VAR_TTM, iteracion, varfechaEfecto, umic.getFechas().getFecefecfin());
		varMescie = UtilFechas.getMes(fcalc);
		varDiaVto = UtilFechas.getDia(umic.getFechas().getFecefecfin());
		varDiaPrima = umic.getPrimas().getFdiadepago();
		varGgim = (umic.getBti().getPgastgesin1I().add(umic.getBti().getPgastgesex1I())).multiply(ConstantsFunciones.CTE_OPER_1ENTRE12);
		varGgim = varGgim.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		varPna = umic.getPrimas().getIprimanetaact();
		varPrp = umic.getPrimas().getPrevprima();
		
		
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varfechaEfecto, umic.getAsegurados().getFnacAseg1(), 
				varCriterioEdad, umic.getRentas().getFecIni(), varEdifer);
		
		varCRMax = umic.getCapitales().getCrmax();
		varI1 = btcUmic.getItcalc().get(ConstantsFunciones.CTE_0);
		varIfal = umic.getBti().getPintertecnI1();
		varW = UtilModulos.getVarEdadMaxima2(mapVariables, CLAVE_VAR_W, UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1()),
				umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1);
		varValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic,
				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterioEdad, ConstantesSolvencia.CTE_TABMORT_L);
		
		//Se recupera la lista de limites de capital para la garantía 310
		//varListaLimCap = UtilModulos.getVarListaLimCap(mapVariables, CLAVE_VAR_LISTA_LIMITE_CAP, kmodalidad, kmodalidad, varfechaEfecto, varX);
		
		varNP = (Integer) mapVariables.get(CLAVE_VAR_NP);
		if (varNP == null){
			if (ConstantsFunciones.CTE_FORMPAGO_1.equals(umic.getPrimas().getCformpago()) || ConstantsFunciones.CTE_FORMPAGO_2.equals(umic.getPrimas().getCformpago())){
				varNP = Integer.valueOf(umic.getPrimas().getCformpago());
			} else if (ConstantsFunciones.CTE_FORMPAGO_3.equals(umic.getPrimas().getCformpago())){
				varNP = ConstantsFunciones.CTE_4;
			} else if (ConstantsFunciones.CTE_FORMPAGO_4.equals(umic.getPrimas().getCformpago())){
				varNP = ConstantsFunciones.CTE_12;
			} else if (ConstantsFunciones.CTE_FORMPAGO_9.equals(umic.getPrimas().getCformpago())){
				varNP = ConstantsFunciones.CTE_0;
			} else {
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AU, new String[]{umic.getPrimas().getCformpago()});
			}

			mapVariables.put(CLAVE_VAR_NP, varNP);
		}	
		// Fin Variables Módulo
		
		if (iteracion == ConstantsModulos.CTE_FIRST_ITER) {
			
			vart = varTCm + ConstantsFunciones.CTE_1;
			mapVariables.put(CLAVE_VAR_T, vart);	
			varSaldoAnt = umic.getCapitales().getIsaldo();				
		} else {
			
			vart = (Integer) mapVariables.get(CLAVE_VAR_T) + ConstantsFunciones.CTE_1;
			mapVariables.put(CLAVE_VAR_T, vart);
			
			terminosPMCUmicAnt = UtilModulos.getTerminosPMCUmic(umic.getKey(), btcUmic.getBaseTec(), iteracion - ConstantsFunciones.CTE_1);
			varSaldoAnt = terminosPMCUmicAnt.getBx();	
		}
		varListaLimCap = UtilModulos.getVarListaLimCap(mapVariables, CLAVE_VAR_LISTA_LIMITE_CAP, umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), varfechaEfecto, varX);
		LimitesCapital varLimCap = null;
		if (varListaLimCap != null && !varListaLimCap.isEmpty()){
			varLimCap = (LimitesCapital) mapVariables.get(CLAVE_VAR_LIMITE_CAP);
			if (varLimCap == null){
				varLimCap = varListaLimCap.get(ConstantsFunciones.CTE_0);
				mapVariables.put(CLAVE_VAR_LIMITE_CAP, varLimCap);
			}
			if (vart >= varLimCap.getNmeshasta()){
				for (int i =0; i<varListaLimCap.size();i++){
					if (varListaLimCap.get(i).getNmeshasta() > vart){
						varLimCap = varListaLimCap.get(i);
						mapVariables.put(CLAVE_VAR_LIMITE_CAP, varLimCap);
						varListaLimCap.remove(i);
					}
				}
			}
			
			varLimite = UtilModulos.getVarLimite(mapVariables, CLAVE_VAR_LIMITE, umic.getBti().getRagravado(), varLimCap);
			varK = varLimCap.getPorgamma().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		} else {
			//Si la lista de capitales no existe o está vacía
			varCRMax = umic.getCapitales().getCrmax();
			if (varCRMax != null){
				varLimite = varCRMax;
			} else {
				varLimite = BigDecimal.valueOf(999999999);
			}
			
			varK = ConstantsFunciones.CTE_OPER_0_PUNTO_5;
			if (kmodalidad.equals(ConstantsFunciones.CTE_700)){
				varK = ConstantsFunciones.CTE_OPER_0_PUNTO_1;
			}
		}
		
		BigDecimal gastF= UtilModulos.getGastos310(mapVariables, CLAVE_VAR_GAST_310, kmodalidad, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), umic.getDatosGenerales().getCtipoaport());
		//varK = varLimCap.getPorgamma().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		varGast = FuncionesGastos.gast(vart, varTtm, varDiaVto, varGgim, varDiaPrima, varSaldoAnt);
		if (varNP == ConstantsFunciones.CTE_0) {
			varPrima = BigDecimal.ZERO;
		} else {
			if (umic.getDatosGenerales().getTipoSubriesgo().equals("UNIV")) {
				if (varMescie == 1) {
					varMescie = 12;
				} else {
					varMescie = varMescie-1;//FINP FINP, situar la fecha de renovación un mes antes.
				}
				if (criterioFecDev!= null 
						&& criterioFecDev.equals(ConstantsModulos.CTE_VAL_RENO2)) {
					varMescie = 12 + (UtilFechas.getMes(varfechaEfecto) - UtilFechas.getMes(proyUmic.get(0).getFechaDesde()));
				}
				varPrima = FuncionesPrimas.prima(vart, varTtm, varPna, varNP, varPrp, varMescie, varDiaVto, varDiaPrima,
						varTCm);
				
				if (proyUmic.get(iteracion - 1).getFechaDesde() != null
						&& proyUmic.get(iteracion - 1).getFechaHasta().after(umic.getFechas().getFecefecfin())){
					varPrima = BigDecimal.ZERO;
				}
			} else {
				varPrima = FuncionesPrimas.prima(vart, varTtm, varPna, varNP, varPrp, varMescie, varDiaVto, varDiaPrima,
						varTCm);
			}
		}
		
		
	    varGastP = FuncionesGastos.gastp(vart, varTtm, varDiaVto, varGgim, varDiaPrima, varPrima);
		varCfall = FuncionesFallecimiento.cfall(vart, varTtm, varX.intValue(), varIfal, gastF, varGgim, varK, varDiaVto, varDiaPrima,
				varSaldoAnt, varW, varLimite, varValoresTabMort);
		varIntG = FuncionesPrimas.intg(vart, varTtm, varDiaVto, varGast, varCfall, varI1, varCC06, varSaldoAnt);
		varIntP = FuncionesPrimas.intp(vart, varTtm, varPrima, varDiaVto, varDiaPrima, varI1, varfechaEfecto);
		
		if (iteracion == ConstantsModulos.CTE_FIRST_ITER) {
			vbx700 = varSaldoAnt;
		}else {
			vbx700 = varSaldoAnt.subtract(varGast).subtract(varGastP).subtract(varCfall).add(varIntG).add(varPrima).add(varIntP);
		}
		
		//Almacenamos los datos calculados para el periodo en la estructura TerminosPMCUmic 
		terminosPMCsUmic.setBx(vbx700);
		terminosPMCsUmic.setGast(varGast);
		terminosPMCsUmic.setPrima(varPrima);
		terminosPMCsUmic.setGastp(varGastP);
		terminosPMCsUmic.setCfall(varCfall);
		terminosPMCsUmic.setIntG(varIntG);
		terminosPMCsUmic.setIntP(varIntP);
		
		final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
		servicio.almacenarTerminosPMCUmic(terminosPMCsUmic);
		
		// Para la última iteración sustituimos el capital inicial de la Umic por el valor vbx700 
		if (vart >= varTtm ) {
			Capitales capitales = umic.getCapitales();
			capitales.setIcapact(vbx700);
			umic.setCapitales(capitales);
		}
		
		/* Se retornará:
		 * 	- VBX700 = vbx700  
		 * 	- Terminal anterior = 0
		 *  - Terminal posterior = 0
		 */		

		salida.setProvbtiproy(vbx700);
		salida.setTerminalAnterior(BigDecimal.ZERO);
		salida.setTerminalPosterior(BigDecimal.ZERO);		
		
		
		if (ModuloVBX700.LOG.isTraceEnabled()) {
			ModuloVBX700.LOG.trace("Fin función << moduloVBX700 >> de la clase ModuloVBX700, para la iteracion = {}, con resultado vbx700 = {}", iteracion, vbx700);
		}
		
		return salida;
	}
	
	
}
