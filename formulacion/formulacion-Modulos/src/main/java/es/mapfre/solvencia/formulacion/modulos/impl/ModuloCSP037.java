/**
 * MU-2018-071847:Código IM00574169: INCIDENCIAS EN CALCULO CLOUD BMV
 * Se incluye el correcto tratamiento a los datos de la tabla 646
 */
/**
 * MU-2019-038078:NO APLICACION CORRECTA LIMITES FALLECIMIENTO
 *  Se corrige el acceso a la tabla 646
 */
package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.LimitesCapital;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo que devuelve en cada momento según forma de pago de la Umic.
 * La expresión matemática para su determinación es la siguiente:
 * 			Si tcm+β (número de meses) < =mescaren
 * 				CSP (037, tcm+β)= PNA(0)
 * 			Sino
 * 				CSP(037, tcm+β) = MIN(PNA(0) * (GAMMA/100) , PNA(0)+LIMITE) 
 * @author agonzalezgar
 *
 */
public class ModuloCSP037 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP037.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP037;
	
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	
	private static final String CLAVE_MESCAREN = ConstantsModulos.CTE_VA_MESCAREN;
	private static final String CLAVE_VA_MESCAREN = CLAVE_MESCAREN.concat(CLAVE_MODULO);
	
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_LIMITE_CAP = ConstantsModulos.CTE_VAR_LIMITE_CAP.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_X = ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_LIMITE = ConstantsModulos.CTE_VAR_LIMITE.concat(CLAVE_MODULO);
	private static final String CLAVE_LISTA_LIMITE_CAP = ConstantsModulos.CTE_LISTA_LIMITE.concat(CLAVE_MODULO);
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
			if (ModuloCSP037.LOG.isTraceEnabled()) {
				ModuloCSP037.LOG.trace("Inicio de execute en clase ModuloCSP037");
			}
			
			//Inicio recuperación de datos que le pasaremos a la función moduloCSP037
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			//Fin de la recuperación de los datos que se pasarán a la función moduloCSP071.
			
			//Invocamos a la función moduloCSP037
			resultado = moduloCSP037(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP037.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP037.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP037.LOG.isTraceEnabled()) {
			ModuloCSP037.LOG.trace("Fin de execute en clase ModuloCSP037");
		}
		
		return resultado;
	}
	/**
	 * Modulo de cálculo que devuelve en cada momento según forma de pago de la Umic.
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
	private BigDecimal moduloCSP037(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal varLimite = BigDecimal.ZERO;
		BigDecimal varGamma = BigDecimal.ZERO;
		Integer varTcm = 0;
		Integer varMescaren;
		BigDecimal csp037 = BigDecimal.ZERO;
		int varBeta = 0;
		BigDecimal varPn0 = BigDecimal.ZERO;
		BigDecimal varx;
		LimitesCapital varLimCap = null;
		List<LimitesCapital> varListaLimCap;
		String varCriterioEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		Timestamp varFechaEfecto;
		BigDecimal varCRMax;
		//Fin variables locales

		if (ModuloCSP037.LOG.isTraceEnabled()) {
			ModuloCSP037.LOG.trace("Inicio función << moduloCSP037 >> de la clase ModuloCSP037, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Definición de variables auxiliares para agilizar las operaciones (se llaman varias veces).
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		// Fin de la definición de las variables auxiliares.
		
		// Cálculo y validación de las variables de apoyo.  
		varCriterioEdad = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		varMescaren = UtilModulos.getVarMescaren(mapVariables, CLAVE_VA_MESCAREN, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_MESCAREN);
		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterioEdad);
			ValidacionesComunesModulos.validarVariableDeApoyoVarMescaren(varMescaren);
		}// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		// Fin de la validación de las variables de apoyo.
		
		// Introducido por nuevos criterios de generación de fechas de pago y devengo (corte de fechas). Si no existe fecha devengo no se realiza el calculo
		if (null == bloqueCorriente.getFechaDevengo() ) {
			return csp037;
		}
		
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		/**
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, 
		 * así como las varibales internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso 
		 * por los siguientes periodos a calcular. 
			Variables de Apoyo
				-	VarMescaren --> obtenerConfiguracion.recuperarVariableApoyo(MESCAREN)
				-	Si la variable de apoyo  retornada es nula se devuelve error funcional 005 - No se ha encontrado la Variable de Apoyo MESCAREN, finalizando el proceso para la UMIC.
			Variables Módulo
				-	varPN0 = umic.primas.iprimanetaini --> dejo la variable en memoria disponible para el procesado de la umic.
			Se calcularán las distintas variables internas necesarias para el cálculo, dependientes del periodo j a calcular, como se describe a continuación: 
		 	Si estoy en el primer periodo (j=1), se establecerán las siguientes variables internas: 
				-	varTCm = TCm(umic.fechas.feciLnisus, fcalc)
				-	varβ = TCM(fcalc, proyUmic(j).varBloque.fecDevengo)
				-	varX= nedad(umic.fechas.fecinisus, umic.asegurados.fnacAseg1, VarCriterEdad)
				-	varLimCap = obtenerConfiguracion.recuperarLimitesCapital (umic.datosgenerales.kmodalidad,  umic.datosgenerales.kgarantia, umic.fechas.fecinisus, varX, varTCm);
				
				-	VarGamma = umic.capitales.porgamma
				
					Si umic.baseTecIni.ragravado = ‘N’ :
						o	VarLimite= varLimCap. Ecaphasta
					Si umic.baseTecIni.ragravado = ‘S’ :
						o	VarLimite = varLimCap. ecaphastaAgra

				
				Si estoy en cualquier otro periodo distinto del primero (j>1), se sobreeescribirán las siguientes variables internas: 
				-	varTCm = varTCm  +1; 
				-	varβ = TCM(fcalc, proyUmic(j).varBloque.fecDevengo)
		 *
		 */
		
		varPn0 = umic.getPrimas().getIprimanetaini();
		
		varTcm = UtilModulos.getVarTCm(mapVariables, CLAVE_VAR_TCM, iteracion, varFechaEfecto, fcalc);
		
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		varx = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriterioEdad, umic.getRentas().getFecIni(), varEdifer);
		
		varListaLimCap = UtilModulos.getVarListaLimCap(mapVariables, CLAVE_LISTA_LIMITE_CAP, kmodalidad, kgarantia, varFechaEfecto, varx);
		
		varGamma = umic.getCapitales().getPorgamma();

		varBeta = FuncionesAuxiliares.tcm(fcalc, bloqueCorriente.getFechaDevengo());
		
		/**
		 * Una vez establecidas las variables del cálculo en el periodo j, se calculará el capital en el periodo como se describe a continuación: 
			Si varTCm + varβ  < = VarMescaren: 
				CSP037(j) =  varPN0
			Si varTCm + varβ  > VarMescaren: 
				CSP037(j) =  Mínimo [(varPN0* (VarGamma )/100),(varPN0+VarLimite  )] 
			Finalmente asignaremos la cuantía nominal del periodo como: 
				proyVidaUmic(j).impFlujoNominal = CSP037(j)
		 */
		if (varTcm + varBeta < varMescaren) {
			csp037 = varPn0;
		} else {
			if (varListaLimCap != null && !varListaLimCap.isEmpty()){
				varLimCap = (LimitesCapital) mapVariables.get(CLAVE_VAR_LIMITE_CAP);
				if (varLimCap == null){
					varLimCap = varListaLimCap.get(0);
					mapVariables.put(CLAVE_VAR_LIMITE_CAP, varLimCap);
				}
				if (varTcm + varBeta >= varLimCap.getNmeshasta()){
					for (int i =0; i<varListaLimCap.size();i++){
//INI-871847
//						if (varListaLimCap.get(i).getNmeshasta() > varTcm + varBeta){
//							varLimCap = varListaLimCap.get(i);
//							mapVariables.put(CLAVE_VAR_LIMITE_CAP, varLimCap);
//							varListaLimCap.remove(i);
//						}
//INI-938078: sE incluye la fecha de efecto y si se encuentra el registro se sale del bucle.
// 						if (varListaLimCap.get(i).getNmeshasta() > varTcm + varBeta)  {
						if ((varListaLimCap.get(i).getNmeshasta() > varTcm + varBeta) && (varFechaEfecto.before(varListaLimCap.get(i).getFefecfin()))) {
							varLimCap = varListaLimCap.get(i);
						    mapVariables.put(CLAVE_VAR_LIMITE_CAP, varLimCap);
						    i = varListaLimCap.size() + 1;
						}
//FIN-871847
					}
				}
				
				varLimite = UtilModulos.getVarLimite(mapVariables, CLAVE_VAR_LIMITE, umic.getBti().getRagravado(), varLimCap);
			} else {
				//Si la lista de caitales no existe o está vacía
				varCRMax = umic.getCapitales().getCrmax();
				if (varCRMax != null){
					varLimite = varCRMax;
				} else {
					varLimite = BigDecimal.valueOf(999999999);
				}
			}

			csp037 = varPn0.multiply(varGamma.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT)).min(varPn0.add(varLimite));
		}
		
		if (ModuloCSP037.LOG.isTraceEnabled()) {
			ModuloCSP037.LOG.trace("Fin función << moduloCSP037 >> de la clase ModuloCSP037,  para la iteracion = {}, con resultado csp037 = {}", iteracion, csp037);
		}
		
		return csp037;
	}

}
