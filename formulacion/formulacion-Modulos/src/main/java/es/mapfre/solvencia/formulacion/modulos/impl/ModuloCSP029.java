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
import es.mapfre.solvencia.formulacion.util.FuncionesPrimas;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del calculo que devuelve la cuantía de fallecimiento de la garantía principal de la póliza.
 * Supone la valoración del reembolso de primas en productos geométricos.
 * La expresión matemática para su determinación es la siguiente:
 *			CSP(029,tc) = PPcap(Tc)+PPR(Tc,beta)+Pna(Tc)*[Pendpa(Fantani,Fcierre,Fp) +Fut *(1+PRP/100)*SUMAF(Tc+1,Tc,n,PRP)]
 *
 * @author apedro
 *
 */
public class ModuloCSP029 implements Modulo {
	
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP029.class);
	
	// Variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP029;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_IFAL = ConstantsModulos.CTE_VA_IFAL;
	private static final String CLAVE_VAR_IFAL = CLAVE_IFAL.concat(CLAVE_MODULO);
	
	private static final String CLAVE_PAS = ConstantsModulos.CTE_VA_PAS;
	private static final String CLAVE_VAR_PAS = CLAVE_PAS.concat(CLAVE_MODULO);
	
	private static final String CLAVE_FUT = ConstantsModulos.CTE_VA_FUT;
	private static final String CLAVE_VAR_FUT = CLAVE_FUT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_BETA = ConstantsModulos.CTE_VAR_BETA;
	private static final String CLAVE_VAR_BETA= CLAVE_BETA.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_TC0 = ConstantsModulos.CTE_VAR_TC0.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TTM = ConstantsModulos.CTE_VAR_TTM.concat(CLAVE_MODULO);
	// Fin de las variables estáticas para agilizar operaciones.
	
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
			if (ModuloCSP029.LOG.isTraceEnabled()) {
				ModuloCSP029.LOG.trace("Inicio de execute en clase ModuloCSP029");
			}
			
			//Recuperación de los datos que se pasarán a la función moduloCSP029.
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubProceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			//Fin de la recuperación de los datos que se pasarán a la función moduloCSP029.
			
			//Invocación de la función moduloCSP029
			resultado = moduloCSP029(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubProceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP029.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP029.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP029.LOG.isTraceEnabled()) {
			ModuloCSP029.LOG.trace("Fin de execute en clase ModuloCSP029");
		}
		
		return resultado;
	}
	
	/**
	 * Módulo de cálculo que devuelve la cuantía de fallecimiento de la garantía principal de la póliza.
	 * Supone la valoración del reembolso de primas en productos geométricos.
	 * La expresión matemática para su determinación es la siguiente:
	 *			CSP(029,tc) = PPcap(Tc)+PPR(Tc,beta)+Pna(Tc)*[Pendpa(Fantani,Fcierre,Fp) +Fut *(1+PRP/100)*SUMAF(Tc+1,Tc,n,PRP)]
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
	 * 			código del sub proceso en ejecución
	 */
	private BigDecimal moduloCSP029(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubProceso) {
		//Variables locales
		Timestamp varFechaEfecto = null;
		String varCriterFecha = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal varIFal = BigDecimal.ZERO;
		BigDecimal varPas = BigDecimal.ZERO;
		BigDecimal varFut = BigDecimal.ZERO;
		BigDecimal csp029 = BigDecimal.ZERO;
		Integer varN = 0;
		Integer varTC0;
		Integer varTC;
		Integer varTcm;
		Integer varTtm;
		Integer varBeta;
		BigDecimal varFactor;
		BigDecimal varPPR = BigDecimal.ZERO;
		BigDecimal varPendpa = BigDecimal.ZERO;
		BigDecimal varSumaF = BigDecimal.ZERO;
		BigDecimal varPPcap = BigDecimal.ZERO;
		//Fin variables locales
		
		if (ModuloCSP029.LOG.isTraceEnabled()) {
			ModuloCSP029.LOG.trace("Inicio de la función << moduloCSP029 >> para la iteración = {}", iteracion);
		}
				
		//Validación de los campos de entrada.
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Si la póliza está anulada, es decir  umic.datosGenerales.csitupol=  ‘AN’ Se deberá retornar error funcional A4 
		if (umic.getDatosGenerales().getCsitupol().equals(ConstantsModulos.CTE_DG_CSITU_ANU)){
			if (ModuloCSP029.LOG.isDebugEnabled()) {
				ModuloCSP029.LOG.debug(Util.errorValidacionA7(umic.getKey().toString()));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A7, new String[]{umic.getKey().toString()});
		}

		
		// Definición de variables auxiliares para agilizar las operaciones (se llaman varias veces).
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		// Fin de la definición de las variables auxiliares.
		
		// Cálculo de las variables de apoyo.
		varCriterFecha = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
		varIFal = UtilModulos.getVarIFal(mapVariables, CLAVE_VAR_IFAL, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_IFAL, umic.getBti().getPintertecnI1());
		varPas = UtilModulos.getVarPas(mapVariables, CLAVE_VAR_PAS, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_PAS);
		varFut = UtilModulos.getVarFut(mapVariables, CLAVE_VAR_FUT, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_FUT);
		// Fin del cálculo de las variables de apoyo.
		
		// Validación de las variables de apoyo.
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFecha);
			ValidacionesComunesModulos.validarVariableDeApoyoVarIFal(varIFal);
			ValidacionesComunesModulos.validarVariableDeApoyoVarPas(varPas);
			ValidacionesComunesModulos.validarVariableDeApoyoVarFut(varFut);
		} // Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		// Fin de la validación de las variables de apoyo.

		
		if(bloqueCorriente.getFechaDevengo() == null || bloqueCorriente.getFechaDevengo().after(umic.getFechas().getFecefecfin()) || 
				!proyUmic.get(iteracion-1).getFechaDesde().before(umic.getFechas().getFecefecfin())) {
			return csp029;
		}
		
		//Variables modulo
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
		varTcm = UtilModulos.getVarTCmIni(mapVariables, CLAVE_VAR_TCM, 0, varFechaEfecto, fcalc);
		varTtm = UtilModulos.getVarTCmIni(mapVariables, CLAVE_VAR_TTM, 0, varFechaEfecto, umic.getFechas().getFecefecfin());
		BigDecimal varPNAtc = umic.getPrimas().getIprimatarada();
		String varCformapago = umic.getPrimas().getCformpago();
		BigDecimal varPPRUmic = umic.getPrimas().getPpr();
		BigDecimal varPRP = umic.getPrimas().getPrevprima();
		BigDecimal ppcapUmic = umic.getPrimas().getPpcap();
		BigDecimal tit1 = umic.getBti().getPintertecnI1();
		BigDecimal varPrimaIni = umic.getPrimas().getIprimanetaini();
		
		
		Timestamp varFecAntRenova = UtilFechas.incrAnyo(varFechaEfecto, varTC0);
		
		if (varFecAntRenova.after(fcalc)){
			varFecAntRenova = UtilFechas.decreAnios(varFecAntRenova, 1);
		}
		
		//Se llama a las funciones auxiliares necesarias para el cálculo
		varTC = FuncionesAuxiliares.tc(varFechaEfecto, proyUmic.get(iteracion-1).getFechaDesde());
		
		Object vBeta = mapVariables.get(CLAVE_VAR_BETA);
		
		
		if (null == vBeta) {
			varBeta = FuncionesAuxiliares.tcmIni(fcalc, proyUmic.get(iteracion-1).getFechaDesde());
			mapVariables.put(CLAVE_VAR_BETA, varBeta);
			
		} else {
			varBeta = (Integer) vBeta + 1;
			mapVariables.put(CLAVE_VAR_BETA, varBeta);
		}
		
		
		varPendpa = FuncionesPrimas.pendpa(varCformapago, varTC0, varTC, varPPRUmic, varPNAtc, varPrimaIni);
		
		
		if(varTC == varTC0){
			varPPcap = ppcapUmic;
			varPPR = varPPRUmic;
			varFactor = varFut;
			varSumaF = FuncionesPrimas.sumaf(varTC0, varTC, varN, varPRP);
		} else {
			varFactor = (BigDecimal.ONE.add(varPRP.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01))).multiply(varFut);
			varSumaF = BigDecimal.ZERO;
			varPPR = FuncionesPrimas.ppr(varTC0, varTC, varPPRUmic);
			
			varPPcap = FuncionesPrimas.ppcap(varTC, varTcm, varTtm, varBeta, varTC0, varIFal, ppcapUmic, varN, varPPRUmic, 
					varPNAtc, varCformapago, varPRP, varPas, varFut, tit1, varPrimaIni);
		}
		
			
		// Se calcula CSP029 = varPPcap+varPPR+ varPNAtc * [varPendpa+( varFactor *  varSumaF )]
		BigDecimal operador = varPendpa.add(varFactor.multiply(varSumaF));
		csp029 = varPPcap.add(varPPR).add(varPNAtc.multiply(operador));
		
		
		if (ModuloCSP029.LOG.isTraceEnabled()) {
			ModuloCSP029.LOG.trace("Fin de la función << moduloCSP029 >> para la iteración = {}, con resultado csp029 = {}", iteracion, csp029);
		}
		
		return csp029;
	}

}
