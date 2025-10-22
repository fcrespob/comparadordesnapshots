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
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * 
 * @author Szilard Toth
 *
 */

public class ModuloCSP116 implements Modulo{
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP116.class);
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP116;
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_N = ConstantsModulos.CTE_VARN.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PRP = ConstantsModulos.CTE_VAR_PRP.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TC = ConstantsModulos.CTE_VAR_TC.concat(CLAVE_MODULO);
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
			if (ModuloCSP116.LOG.isTraceEnabled()) {
				ModuloCSP116.LOG.trace("Inicio de execute en clase ModuloCSP116");
			}
			//Recuperamos los datos que le pasaremos a la función ModuloCSP116
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			//Invocamos a la función moduloCSP116
			resultado = moduloCSP116(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso ,mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloCSP116.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP116.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		if (ModuloCSP116.LOG.isTraceEnabled()) {
			ModuloCSP116.LOG.trace("Fin de execute en clase ModuloCSP116");
		}
		return resultado;
	}
	/**
	 * Usaremos este módulo para el cálculo de la cuantía nominal de la prestación 
	 * a pagar en caso de IAP (Invalidez Absoluta Permanente) de un seguro DOTAL.
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
	private BigDecimal moduloCSP116(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final String codSubproceso, final Map<String, Object> mapVariables) {
		//variables locales
		BigDecimal csp116 = BigDecimal.ZERO;
		Timestamp varFechaEfecto = null;
		Integer varN = null;
		Integer varTC;
		BigDecimal varPNAtc = BigDecimal.ZERO;
		BigDecimal varPRP = BigDecimal.ZERO;
		BigDecimal varCSP116 = BigDecimal.ZERO;
		//Fin variables locales
		if (ModuloCSP116.LOG.isTraceEnabled()) {
			ModuloCSP116.LOG.trace("Inicio de la función << moduloCSP116 >> de la clase ModuloCSP116, para la iteración = {}", iteracion);
		}
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		/**
		 * Si estoy en el primer periodo, se recuperarán los datos de la umic necesarios para el cálculo 
		 * que no varían por periodo, así como las variables internas que tampoco varíen por periodo, 
		 * y que se dejarán accesibles para su uso en el subproceso por los siguientes periodos a calcular.
		 * 
		 * Variables Módulo
		 * 
		 * Si la póliza está en vigor, es decir  umic.datosGenerales.csitupol=  ‘VI’
		 * 		Si umic.datosGenerales.cnegocio = ‘I’
		 * 			varfechaEfecto = umic.fechas.fecefecIni
		 * 		En caso contrario:
		 * 			varfechaEfecto = umic.fechas fecinisus
		 * 		varN = umic.duraciones.ndursegano
		 * 
		 * 	Si la póliza está reducida, es decir  umic.datosGenerales.csitupol=  ‘RE’
		 * 		varfechaEfecto =  umic.fechas.fecefecred
		 * 		Si umic.fechas.fecefecred es nula y umic.datosGenerales.cnegocio = ‘C’:
		 * 			varfechaEfecto = umic.fechas.fecfinpagprim
		 * 		Si umic.datosGenerales.cnegocio = ‘I’:
		 * 			varN = umic.duraciones.ndursegano –tc(umic.fechas fecefecIni, umic.fechas.fecefecred)
		 * 		En caso contrario:
		 * 			varN = umic.duraciones.ndursegano –tc(umic.fechas fecinisus, umic.fechas.fecefecred)
		 * 		
		 * 		varTC = TC(varfechaEfecto, fcalc)
		 *		varPNAtc = umic.primas.iprimaact
		 *		varPRP = umic.primas.prevprima/100
		 *
		 *	Dejo las variables en memoria, disponibles para el subproceso de la umic.
		 *	Se calculará el siguiente sumatorio: 
		 *	varCSP116 = ∑_(j=varTC)^(varN-1)varPNAtc *(1+varPRP )^((TC-j) )
		 *
		 *	Para cualquier periodo j se calculará: 
		 *		Si proyUmic(j).varBloque.fecDevengo > umic.fechas.fecefecfin ó proyUmic(j).varBloque.fecDevengo es nula:
		 *			csp116 = 0; // BigDecimal.ZERO;
		 *		En caso contrario:
		 *			csp116 = varCSP116;
		 * 		
		 */
		
		varFechaEfecto = UtilModulos.getVarFechaEfecto(mapVariables, CLAVE_FEC_EFEC, umic);
		varN = UtilModulos.getVarN(mapVariables, CLAVE_VAR_N, umic);
		varTC = UtilModulos.getVarTC(mapVariables, CLAVE_VAR_TC, varFechaEfecto, fcalc);
		varPNAtc = umic.getPrimas().getIprimanetaact();
		varPRP = UtilModulos.getPorcentaje(mapVariables, CLAVE_VAR_PRP, umic.getPrimas().getPrevprima());

		// Sumatorio
		for(int j = varTC; j == varN-1; j++){
			varCSP116 = varCSP116.add(varPNAtc.multiply(BigDecimal.ONE.add(varPRP).pow(varTC-j)));
		}
		
		if((bloqueCorriente.getFechaDevengo().after(umic.getFechas().getFecefecfin())) || bloqueCorriente.getFechaDevengo() == null){
			csp116 = BigDecimal.ZERO;
		} else {
			csp116 = varCSP116;
		}
		if (ModuloCSP116.LOG.isTraceEnabled()) {
			ModuloCSP116.LOG.trace("Fin de la función << moduloCSP116 >> de la clase ModuloCSP116, para la iteración = {}", iteracion);
		}
		return csp116;
	}
}
	