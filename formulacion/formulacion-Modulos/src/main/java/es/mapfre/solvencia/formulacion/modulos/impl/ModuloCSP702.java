package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.aggregator.BigDecimalSum;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.LimitesCapital;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo CSP702.
 * Clase encargada del cálculo que devuelve la cuantía nominal de fallecimiento de la Umic
 * @author ogperez
 *
 */
public class ModuloCSP702 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP702.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP702;
	
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;	
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC;
	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_T = ConstantsModulos.CTE_VAR_T.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_X = ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_LIMITE = ConstantsModulos.CTE_VAR_LIMITE.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_LIMITE_CAP = ConstantsModulos.CTE_VAR_LIMITE_CAP.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_LISTA_LIMITE_CAP = ConstantsModulos.CTE_LISTA_LIMITE.concat(CLAVE_MODULO);
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
			
			if (ModuloCSP702.LOG.isTraceEnabled()) {
				ModuloCSP702.LOG.trace("Inicio de execute en clase ModuloCSP702");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCSP702
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo ModuloCSP702
			resultado = moduloCSP702(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP702.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP702.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP702.LOG.isTraceEnabled()) {
		}
		
		return resultado;
	}
	/**
	 * Modulo de cálculo que devuelve la cuantía nominal de fallecimiento de la Umic
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
	private BigDecimal  moduloCSP702(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales	
		BigDecimal csp702 = BigDecimal.ZERO;
		BigDecimal csp702aux = BigDecimal.ZERO;
		String varCriterioEdad;
		Timestamp varfechaEfecto;
		Integer varTCm;
		Integer vart;
		BigDecimal varK;
		BigDecimal varCRMax;
		BigDecimal varBxAntGast = BigDecimal.ZERO;
		BigDecimal varX;
		List<LimitesCapital> varListaLimCap;
		//Fin variables locales
		
		if (ModuloCSP702.LOG.isTraceEnabled()) {
			ModuloCSP702.LOG.trace("Inicio función << ModuloCSP702 >> de la clase ModuloCSP702, para la iteracion = {}", iteracion);
		}
		
		if (bloqueCorriente.getFechaDevengo() == null) {
			return csp702;
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		varCriterioEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		
		// Variables modulo
		varfechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varfechaEfecto, umic.getAsegurados().getFnacAseg1(), 
				varCriterioEdad, umic.getRentas().getFecIni(), varEdifer);
		
		varTCm = UtilModulos.getVarTCm(mapVariables, CLAVE_VAR_TCM, iteracion, varfechaEfecto, proyUmic.get(0).getFcierre());
		
		if (iteracion == ConstantsModulos.CTE_FIRST_ITER) {
			vart = varTCm + ConstantsFunciones.CTE_1;
			mapVariables.put(CLAVE_VAR_T, vart);			
		} else {
			vart = (Integer) mapVariables.get(CLAVE_VAR_T) + ConstantsFunciones.CTE_1;
			mapVariables.put(CLAVE_VAR_T, vart);
					
		}
		
		varListaLimCap = UtilModulos.getVarListaLimCap(mapVariables, CLAVE_VAR_LISTA_LIMITE_CAP, umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), varfechaEfecto, varX);
		LimitesCapital varLimCap;
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
			
			varCRMax = UtilModulos.getVarLimite(mapVariables, CLAVE_VAR_LIMITE, umic.getBti().getRagravado(), varLimCap);
			varK = varLimCap.getPorgamma().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		} else {
			//Si la lista de caitales no existe o está vacía
			varCRMax = umic.getCapitales().getCrmax();
			if (varCRMax == null){
				varCRMax = BigDecimal.valueOf(999999999);
			}
			
			varK = ConstantsFunciones.CTE_OPER_0_PUNTO_5;
			if (umic.getDatosGenerales().getKmodalidad().equals(ConstantsFunciones.CTE_700)){
				varK = ConstantsFunciones.CTE_OPER_0_PUNTO_1;
			}
		}
		if (varK.equals(BigDecimal.ZERO)){
			varK = BigDecimal.ONE;
		}
		// Fin variables modulo
		
		if (umic.getDatosGenerales().getTipoSubriesgo().equals("UNIV")) {

			varBxAntGast = obtenenerBxAntGastUNIV2(btcUmic.getBt(), iteracion, umic.getDatosGenerales());

		}else {
			
			if (!iteracion.equals(ConstantsFunciones.CTE_FIRST_ITER)) {

				varBxAntGast = obtenenerBxAntGast(btcUmic.getBt(), iteracion+1, umic.getDatosGenerales());
				

			} else {

				varBxAntGast = obtenenerBxAntGast(btcUmic.getBt(), iteracion+1, umic.getDatosGenerales());
			}
		}
		
		
		if(varBxAntGast == null) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AZ);
		}
		
		if(varK.multiply(varBxAntGast).compareTo(varCRMax) < 0) {
			csp702 = varBxAntGast.multiply(BigDecimal.ONE.add(varK));
		}else {
			csp702 = varBxAntGast.add(varCRMax);
		}
		
		if (ModuloCSP702.LOG.isTraceEnabled()) {
			ModuloCSP702.LOG.trace("Fin función << ModuloCSP702 >> de la clase ModuloCSP702, para la iteracion = {} con resultado csp702 = {}", iteracion, csp702);
		}
			
		
		return csp702;
	}

	
	/*
	 * Obtener la suma de saldos de la iteración anterior
	 */
	private BigDecimal obtenenerBxAntGast(String bt, Integer iteracion, DatosGenerales datosGenerales) {

		NamedCache TerminosPMCUmic = CacheFactory.getCache(ConstantsModulos.CACHE_DATOS_CALCULADOS_UMIC);
		EqualsFilter isKbasetec = new EqualsFilter(new ReflectionExtractor(ConstantsModulos.GET_BT),bt);
		EqualsFilter isIteracion =  new EqualsFilter(new ReflectionExtractor(ConstantsModulos.GET_ITERACION), iteracion-ConstantsFunciones.CTE_1);
		
		EqualsFilter isKajuste = new EqualsFilter(
				new ChainedExtractor(new ReflectionExtractor(ConstantsModulos.GET_CLAVEUMIC), new ReflectionExtractor(ConstantsModulos.GET_KAJUSTE)),
				datosGenerales.getKajuste());
		
		EqualsFilter isKcertificado = new EqualsFilter(
				new ChainedExtractor(new ReflectionExtractor(ConstantsModulos.GET_CLAVEUMIC), new ReflectionExtractor(ConstantsModulos.GET_KCERTIFICADO)),
				datosGenerales.getKcertificado());
		
		EqualsFilter isKmodalidad = new EqualsFilter(
				new ChainedExtractor(new ReflectionExtractor(ConstantsModulos.GET_CLAVEUMIC), new ReflectionExtractor(ConstantsModulos.GET_KMODALIDAD)),
				datosGenerales.getKmodalidad());
		
		EqualsFilter isKpoliza = new EqualsFilter(
				new ChainedExtractor(new ReflectionExtractor(ConstantsModulos.GET_CLAVEUMIC), new ReflectionExtractor(ConstantsModulos.GET_KPOLIZA)),
				datosGenerales.getKpoliza());
		
		EqualsFilter isKprestacion = new EqualsFilter(
				new ChainedExtractor(new ReflectionExtractor(ConstantsModulos.GET_CLAVEUMIC), new ReflectionExtractor(ConstantsModulos.GET_KPRESTACION)),
				datosGenerales.getKprestacion());
		
		EqualsFilter isKsubpoliza = new EqualsFilter(
				new ChainedExtractor(new ReflectionExtractor(ConstantsModulos.GET_CLAVEUMIC), new ReflectionExtractor(ConstantsModulos.GET_KSUBPOLIZA)),
				datosGenerales.getKsubpoliza());
		
		EqualsFilter isNsuscri = new EqualsFilter(
				new ChainedExtractor(new ReflectionExtractor(ConstantsModulos.GET_CLAVEUMIC), new ReflectionExtractor(ConstantsModulos.GET_NSUSCRI)),
				datosGenerales.getNsuscri());
		
		
		Filter[] arrayFiltros =  new Filter[]{isKbasetec, isIteracion, isKajuste, isKcertificado, isKmodalidad, isKpoliza, isKprestacion,
				isKsubpoliza,isNsuscri};
		
		Filter filter = new AllFilter(arrayFiltros);
					
		
		return (BigDecimal) TerminosPMCUmic.aggregate(filter, new BigDecimalSum(ConstantsModulos.GET_BX));
			
	}
	/*
	 * Obtener la suma de saldos de la iteración anterior
	 */
	private BigDecimal obtenenerBxAntGastUNIV(String bt, Integer iteracion, DatosGenerales datosGenerales) {

		NamedCache TerminosPMCUmic = CacheFactory.getCache(ConstantsModulos.CACHE_DATOS_CALCULADOS_UMIC);
		EqualsFilter isKbasetec = new EqualsFilter(new ReflectionExtractor(ConstantsModulos.GET_BT),bt);
		EqualsFilter isIteracion =  new EqualsFilter(new ReflectionExtractor(ConstantsModulos.GET_ITERACION), iteracion-ConstantsFunciones.CTE_1);
		
		EqualsFilter isKajuste = new EqualsFilter(
				new ChainedExtractor(new ReflectionExtractor(ConstantsModulos.GET_CLAVEUMIC), new ReflectionExtractor(ConstantsModulos.GET_KAJUSTE)),
				datosGenerales.getKajuste());
		
		EqualsFilter isKcertificado = new EqualsFilter(
				new ChainedExtractor(new ReflectionExtractor(ConstantsModulos.GET_CLAVEUMIC), new ReflectionExtractor(ConstantsModulos.GET_KCERTIFICADO)),
				datosGenerales.getKcertificado());
		
		EqualsFilter isKmodalidad = new EqualsFilter(
				new ChainedExtractor(new ReflectionExtractor(ConstantsModulos.GET_CLAVEUMIC), new ReflectionExtractor(ConstantsModulos.GET_KMODALIDAD)),
				datosGenerales.getKmodalidad());
		
		EqualsFilter isKpoliza = new EqualsFilter(
				new ChainedExtractor(new ReflectionExtractor(ConstantsModulos.GET_CLAVEUMIC), new ReflectionExtractor(ConstantsModulos.GET_KPOLIZA)),
				datosGenerales.getKpoliza());
		
		EqualsFilter isKprestacion = new EqualsFilter(
				new ChainedExtractor(new ReflectionExtractor(ConstantsModulos.GET_CLAVEUMIC), new ReflectionExtractor(ConstantsModulos.GET_KPRESTACION)),
				datosGenerales.getKprestacion());
		
		EqualsFilter isKsubpoliza = new EqualsFilter(
				new ChainedExtractor(new ReflectionExtractor(ConstantsModulos.GET_CLAVEUMIC), new ReflectionExtractor(ConstantsModulos.GET_KSUBPOLIZA)),
				datosGenerales.getKsubpoliza());
		
		EqualsFilter isNsuscri = new EqualsFilter(
				new ChainedExtractor(new ReflectionExtractor(ConstantsModulos.GET_CLAVEUMIC), new ReflectionExtractor(ConstantsModulos.GET_NSUSCRI)),
				datosGenerales.getNsuscri());
		
		
		Filter[] arrayFiltros =  new Filter[]{isKbasetec, isIteracion, isKajuste, isKcertificado, isKmodalidad, isKpoliza, isKprestacion,
				isKsubpoliza};
		
		Filter filter = new AllFilter(arrayFiltros);
					
		
		return (BigDecimal) TerminosPMCUmic.aggregate(filter, new BigDecimalSum(ConstantsModulos.GET_BX));
			
	}
	
	private BigDecimal obtenenerBxAntGastUNIV2(String bt, Integer iteracion, DatosGenerales datosGenerales) {

		final IObtenerDatos obtenerDatos = FachadaServicios.getObtenerDatos();
		
		List<Umic> umicsUniv = obtenerDatos.recuperarTodasUmicsUniv(datosGenerales.getKmodalidad(),
				datosGenerales.getKpoliza() ,datosGenerales.getKsubpoliza());
		BigDecimal sumatorio = BigDecimal.ZERO;
		for (int i = 0; i < umicsUniv.size(); i++) {
			List<DetalleCorriente> varProyUniv  = obtenerDatos.recuperarProyeccionCualquierNodo(bt, umicsUniv.get(i).getDatosGenerales().getFecCierre(), umicsUniv.get(i).getKey());
			if (!varProyUniv.isEmpty() && (varProyUniv.get(i).getKgarantia() != 10) 
					&& (varProyUniv.get(i).getTotalFlujoProyeccion() != null)
					&& (varProyUniv.get(i).getTotalFlujoProyeccion().getProvbtiproy() != null)) {
				if (iteracion - 1 < varProyUniv.size()) {
					sumatorio = sumatorio.add(varProyUniv.get(iteracion - 1).getTotalFlujoProyeccion().getProvbtiproy());
				}
			} 
		}

		return sumatorio;
			
	}

}
