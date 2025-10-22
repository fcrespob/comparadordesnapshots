package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import es.mapfre.solvencia.formulacion.util.FuncionesVBX;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * 
 * @author Szilard Toth
 *
 */

public class ModuloCSP202 implements Modulo{
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP202.class);
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP202;
	private static final String CLAVE_IFAL = ConstantsModulos.CTE_VA_IFAL;
	private static final String CTE_BLOQUE = ConstantsModulos.CTE_VAR_BLOQUE;
	private static final String CTE_BLOQUE_CONCAT = CTE_BLOQUE.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_IFAL = CLAVE_IFAL.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TC0 = ConstantsModulos.CTE_VAR_TC0.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_BETA = ConstantsModulos.CTE_VAR_BETA.concat(CLAVE_MODULO);
	// Fin de las variables estáticas usadas para agilizar operaciones.
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		//Fin variables locales
		try {
			if (ModuloCSP202.LOG.isTraceEnabled()) {
				ModuloCSP202.LOG.trace("Inicio de execute en clase ModuloCSP202");
			}
			//Recuperamos los datos que le pasaremos a la función ModuloCSP202
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			//Invocamos a la función moduloCSP202
			resultado = moduloCSP202(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso ,mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloCSP202.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP202.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		if (ModuloCSP202.LOG.isTraceEnabled()) {
			ModuloCSP202.LOG.trace("Fin de execute en clase ModuloCSP202");
		}
		return resultado;
	}
	/**
	 * 
	 * Usaremos este módulo para el cálculo de la cuantía nominal de 
	 * la prestación de fallecimiento en Seguros mixtos Individuales
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
	 * @param codSubproceso
	 * 			Código del subproceso que se está ejecutando.
	 * @param mapVariables 
	 * 			mapa con las variables de memoria necesarias
	 */
	private BigDecimal moduloCSP202(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final String codSubproceso, final Map<String, Object> mapVariables) {
		//variables locales
		BigDecimal csp202 = BigDecimal.ZERO;
		BigDecimal varIFal = BigDecimal.ZERO;
		Timestamp varFechaEfecto = null;
		Integer varTCm = 0;
		Integer varTC = 0;
		Integer varTC0 = 0;
		Integer varBeta = 0;
		BigDecimal varPpcap = BigDecimal.ZERO;
		BigDecimal varPPRUmic = BigDecimal.ZERO;
		BloqueCorriente varBloque;
		BigDecimal varPuccap = BigDecimal.ZERO;
		BigDecimal varPPR = BigDecimal.ZERO;
		//Fin variables locales
		if (ModuloCSP202.LOG.isTraceEnabled()) {
			ModuloCSP202.LOG.trace("Inicio de la función << moduloCSP202 >> de la clase ModuloCSP202, para la iteración = {}", iteracion);
		}
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		/**
		 * 	Si la póliza está anulada, es decir  umic.datosGenerales.csitupol=  ‘AN’
		 *		Se deberá retornar error funcional A4 – Umic en estado Anulado & claveUmic
		 *
		 *	Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic 
		 *	necesarios para el cálculo que no varían por periodo, así como las 
		 *	varibales internas que tampoco varían por periodo, y que se dejarán accesibles 
		 *	para su uso en el subproceso por los siguientes periodos a calcular. 
		 *
		 *	Variables de Apoyo
		 *		VarIfal --> obtenerConfiguracion.recuperarVariableApoyo(IFAL)
		 *		Si  la variable de apoyo  retornadas es nula se devuelve error funcional A6 
		 *			- No se ha encontrado la Variable de Apoyo &NombreVariableApoyo, 
		 *			finalizando el proceso para la UMIC.
		 *
		 *		Si  VarIfal = 1 --> Sobreescribimos la variable: VarIfal = umic.baseTecIni.pintertecnI1
		 *		Si VarIfal = 2 --> Sobreescribimos la variable: VarIfal = 0
		 *
		 *	Variables Módulo
		 *		Si la póliza está en vigor, es decir  umic.datosGenerales.csitupol=  ‘VI’
		 *			Si umic.datosGenerales.cnegocio = ‘I’ 
		 *				varfechaEfecto = umic.fechas.fecefecIni
		 *			En caso contrario:
		 *				varfechaEfecto = umic.fechas fecinisus
		 *		Si la póliza está reducida, es decir  umic.datosGenerales.csitupol=  ‘RE’
		 *			varfechaEfecto =  umic.fechas.fecefecred
		 *			Si umic.fechas.fecefecred es nula y umic.datosGenerales.cnegocio = ‘C’:
		 *				varfechaEfecto = umic.fechas.fecfinpagprim
		 *		varTCm = TCM(varfechaEfecto, proyUmic(1).fecCierre);
		 *		varTC0 = TC(varfechaEfecto, fcalc)
		 *		varPpcap = umic.primas.ppcap
		 *		varPPRUmic = umic.primas.ppr
		 *		
		 *	Dejo las variables en memoria, disponibles para el subproceso de la umic.
		 *	Para cualquier periodo j se calculará: 
		 *		Si proyUmic(j).varBloque.fecDevengo > umic.fechas.fecefecfin ó si proyUmic(j).varBloque.fecDevengo es  nula: 
		 *			csp202 = BigDecimal.ZERO;
		 *		En caso contrario:
		 *			Si proyUmic(j).fecDesde < umic.fechas.fecefecfin
		 *				varTC= TC(varfechaEfecto, proyUmic(j).fecDesde)
		 *				varbeta = TCM (fcalc, proyUmic(j).fecDesde)
		 *				varPuccap = PUCCAP (varTC, varTCm, varbeta, vartc0, varIfal, varPpcap, varPPRUmic)
		 *				varPPR = PPR (varTc0, varTC, varPPRUmic)
		 * 
		 *			csp202 = varPuccap + varPPR
		 */
		
		if(umic.getDatosGenerales().getCsitupol() == ConstantsModulos.CTE_DG_CSITU_ANU){
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A4, new String[]{ConstantsFunciones.CTE_CLA_UMIC_REM, umic.getIdUmic()});
		}
		// Variables de apoyo
		varIFal = UtilModulos.getVarIFal(mapVariables, CLAVE_VAR_IFAL, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_IFAL, umic.getBti().getPintertecnI1());
		
		if(iteracion.equals(ConstantsFunciones.CTE_FIRST_ITER)){
			ValidacionesComunesModulos.validarVariableDeApoyoVarIFal(varIFal);
			if(varIFal.equals(BigDecimal.ONE)){
				varIFal = umic.getBti().getPintertecnI1();
				mapVariables.put(CLAVE_VAR_IFAL, varIFal);
			} else if (varIFal.equals(ConstantsFunciones.CTE_OPER_2)){
				varIFal = BigDecimal.ZERO;
				mapVariables.put(CLAVE_VAR_IFAL, varIFal);
			}
		}
		
		// Fin variables de apoyo
		
		// Variables módulo
		varFechaEfecto = UtilModulos.getVarFechaEfecto(mapVariables, CLAVE_FEC_EFEC, umic);
			
		varTCm = UtilModulos.getVarTCm(mapVariables, CLAVE_VAR_TCM, ConstantsFunciones.CTE_FIRST_ITER, varFechaEfecto, proyUmic.get(0).getFcierre());
		varTC0 = UtilModulos.getVarTC0(mapVariables, CLAVE_VAR_TC0, varFechaEfecto, fcalc);
		varPpcap = umic.getPrimas().getPpcap();
		varPPRUmic = umic.getPrimas().getPpr();

		if(iteracion.equals(ConstantsFunciones.CTE_FIRST_ITER)){
			if(UtilFechas.getDia(varFechaEfecto) == UtilFechas.getDia(proyUmic.get(ConstantsFunciones.CTE_FIRST_ITER).getFcierre()))
			{
				
				varBeta = 1;	
				
			}else{
				
				varBeta = 0; 
				
			}
			mapVariables.put(CLAVE_VAR_BETA, varBeta);
		}else{
			varBeta = (Integer) mapVariables.get(CLAVE_VAR_BETA);
			varBeta += 1;
			mapVariables.put(CLAVE_VAR_BETA, varBeta);
		}
		
		
		Timestamp comprobacion = null;
		if((bloqueCorriente.getFechaDevengo().after(umic.getFechas().getFecefecfin())) || (null == bloqueCorriente.getFechaDevengo())){
			csp202 = BigDecimal.ZERO;
		} else {
			if(proyUmic.get(iteracion-1).getFechaDesde().before(umic.getFechas().getFecefecfin())){
				varTC = FuncionesAuxiliares.tc(varFechaEfecto, bloqueCorriente.getFechaDevengo());
				
				varPuccap = FuncionesVBX.puccap(varTC, varTCm, varBeta, varTC0, varIFal, varPpcap, varPPRUmic);
				varPPR = FuncionesPrimas.ppr(varTC0, varTC, varPPRUmic);
			}
			csp202 = varPuccap.add(varPPR);
		}
		// Fin variables módulo
		if (ModuloCSP202.LOG.isTraceEnabled()) {
			ModuloCSP202.LOG.trace("Fin de la función << moduloCSP202 >> de la clase ModuloCSP202, para la iteración = {}", iteracion);
		}
		return csp202;
	}
}
