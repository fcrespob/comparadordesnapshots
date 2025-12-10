/**
 * MU-2018-016517-Código IM00258226: CORRECCION MODULO CSP037
 * Se sustituye calculo de TCm por el TCmIni.
 */

package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.formulacion.CriterioFechas;
import es.mapfre.solvencia.dominio.maestro.Rentas;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 *  Clase encargada del cálculo que devuelve en cada momento según forma de pago de la Umic.
 * @author agonzalezgar
 *
 */
public class ModuloCSPINDEX implements Modulo {

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSPINDEX.class);
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSPINDEX;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCMINI = ConstantsModulos.CTE_VAR_TCMINI.concat(CLAVE_MODULO);
	private static final String CLAVE_VARL = ConstantsModulos.CTE_VARL.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
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
			if (ModuloCSPINDEX.LOG.isTraceEnabled()) {
				ModuloCSPINDEX.LOG.trace("Inicio de execute en clase ModuloCSP071");
			}
			
			//Inicio recuperación de datos que le pasaremos a la función moduloCSP071
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];

			resultado = moduloCSPINDEX(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,codSubproceso);

			
		} catch (Solvencia2Excepcion e) {
			ModuloCSPINDEX.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSPINDEX.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSPINDEX.LOG.isTraceEnabled()) {
			ModuloCSPINDEX.LOG.trace("Fin de execute en clase ModuloCSPINDEX");
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

		private BigDecimal moduloCSPINDEX(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
				final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables,final String codSubproceso) {
		
			//Variables locales
		String varCriterFecha = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal varPrima0 = BigDecimal.ZERO;
		int varM = 0;
		BigDecimal varPPRUmic = BigDecimal.ZERO;
		Integer varTCmm = 0;
		int	varTCm = 0;
		int	varTCmini = 0;
		int varBeta = 0;
		int varPP = 0;
		BigDecimal varTirIni = BigDecimal.ZERO;
		BigDecimal varTieCie = BigDecimal.ZERO;
		BigDecimal varGipc = BigDecimal.ZERO;
		BigDecimal varBloque = BigDecimal.ZERO;
		BigDecimal varCapital = BigDecimal.ZERO;
		String sumatorio;
		BigDecimal varImpRenta = BigDecimal.ZERO;
		Timestamp varFechaEfecto;
		BigDecimal varCspIndex = BigDecimal.ZERO;

		final IObtenerConfiguracion obtenerConf = FachadaServicios.getObtenerConfiguracion();
		CriterioFechas criterioFechas = null;
		String criterioFecPago = ConstantsFunciones.CTE_CADENA_VACIA;
		String criterioFecDev = ConstantsFunciones.CTE_CADENA_VACIA;
		
	    //Fin variables locales
		
		if (ModuloCSPINDEX.LOG.isTraceEnabled()) {
			ModuloCSPINDEX.LOG.trace("Inicio función << moduloCSPINDEX >> de la clase ModuloCSPINDEX, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if ( null == bloqueCorriente.getFechaDevengo() ) {
			return BigDecimal.ZERO;
		}
		/**
		 * 
		 * Se realizará la validación de los parámetros de entrada marcados como obligatorios. 
		 * En caso de error se devuelve error funcional A1 - Parámetro obligatorio no informado &NombreAtributoEntrada.
		 *
		 * Si la póliza está anulada, es decir  umic.datosGenerales.csitupol=  ‘AN’
		 * Se deberá retornar error funcional A4 – Umic en estado Anulado & claveUmic
		 *
		 *
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, así como las varibales internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso por los siguientes periodos a calcular. 
		 *
		 *
		 * Variables Módulo
		 *
		 * Si la póliza está en vigor, es decir  umic.datosGenerales.csitupol=  ‘VI’
		 * Si umic.datosGenerales.cnegocio = ‘I’ 
		 * varfechaEfecto = umic.fechas.fecefecIni
		 * En caso contrario:
		 * varfechaEfecto = umic.fechas fecinisus
		 * Si la póliza está reducida, es decir  umic.datosGenerales.csitupol=  ‘RE’
		 * varfechaEfecto =  umic.fechas.fecefecred
		 * Si umic.fechas.fecefecred es nula y umic.datosGenerales.cnegocio = ‘C’:
		 * varfechaEfecto = umic.fechas.fecfinpagprim
		 *
		 * varTCm = TCM(varfechaEfecto, proyUmic(1).fecCierre);
		 * varPrima0 = umic.primas.primanetaini
		 * varTirIni = umic.rescates.tirIni
		 * Si varTirIni = 0:
		 * Se deberá retornar error funcional AX – Tir Inicial = 0 para la umic & claveUmic, finalizando el proceso para la UMIC.
		 *
		 * varTirCie = umic.rescates.tirCie
		 * varPPRUmic = umic.primas.ppr 
		 *
		 * Dejo las variables en memoria, disponibles para el subproceso de la umic.
		 *
		 * varBloque= obtenerConfiguracion.getBloqueBySubproceso(proyUmic, codSubproceso)
		 *
		 * Para cualquier periodo j se calculará: 
		 *
		 * Si j = 1
		 * 		varbeta = 1
		 * En caso contrario: 
		 * 		varbeta =varbeta +1
		 *
		 * Si proyUmic(j).varBloque.fecDevengo > umic.fechas.fecefecfin ó si proyUmic(j).varBloque.fecDevengo es  nula: 
		 *
		 * 		varCspIndex = 0
		 *
	     * En caso contrario (Si proyUmic(j).fecDesde < umic.fechas.fecefecfin):
		 *
	 	 * 		varCspIndex =varPrima0 *(varTircie )/(varTirIni )*〖(1+〖varPPRUmic 〗_(β/12))〗^(β/12)
 		 *
		 */
		
		// Cálculo y validación de las variables de apoyo.  
		//Recuperamos el criterio de las fechas
		varCriterFecha = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);	


		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFecha);
			// Fin de la validación de las variables de apoyo.
		}
		

		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		//Recuperamos el criterio de las fechas
		criterioFechas = obtenerConf.recuperarCriterioFechas(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), 
				umic.getDatosGenerales().getKprestacion(), umic.getDatosAdicionales().getPrestCal(), codSubproceso);
	
		//Validamos el criterio de la fecha de pago y la fecha devengo
		criterioFecPago = criterioFechas.getFecPago();
		criterioFecDev = criterioFechas.getFecDevengo();


//		- Si criterioFechas.FecPago y/ó criterioFechas.FecDevengo es igual a ‘EFTEC’
//		Se establecerá varfecini como la fecha de Efecto Técnico, es decir, como el fin de mes del mes de efecto de la UMIC (fechasUmic.fecinisus)
		
		if (ConstantsModulos.CTE_VAL_EFTEC.equals(criterioFecPago) || ConstantsModulos.CTE_VAL_EFTEC.equals(criterioFecDev)||
			ConstantsModulos.CTE_VAL_EFTIN.equals(criterioFecPago) || ConstantsModulos.CTE_VAL_EFTIN.equals(criterioFecDev)) {
			varFechaEfecto = UtilFechas.getUltimoDiaDelMes(varFechaEfecto);
		}
		
		if (null == UtilFechas.getFecha(bloqueCorriente.getFechaDevengo())) {
			return varCspIndex;
		}	
		
		// Si la póliza no está reducida
		if (umic.getDatosGenerales().getCsitupol().equals("VI")) {
			if (umic.getDatosGenerales().getCnegocio().equals("I")) {
				varFechaEfecto = umic.getFechas().getFecefecini();
			} else {
				varFechaEfecto = umic.getFechas().getFecinisus();
			}
		}
		// Si la póliza está reducida
		if (umic.getDatosGenerales().getCsitupol().equals("RE")) {
			varFechaEfecto = umic.getFechas().getFecefecred();
			if (umic.getFechas().getFecefecred().equals(null)
					&& umic.getDatosGenerales().getCnegocio().equals("C")) {
				varFechaEfecto = umic.getFechas().getFecfinpagprim();
			}
		}
		
		/*varTCm = UtilModulos.getVarTCm(mapVariables, CLAVE_VAR_TCM, ConstantsModulos.CTE_FIRST_ITER, varFechaEfecto, proyUmic.get(1).getFcierre());
		varPrima0 = umic.getPrimas().getIprimanetaini();
		varTirIni = umic.getRescates().getTirIni();
		if ( varTirIni.equals(BigDecimal.ZERO) ) {
			// Se retorna un error
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_GG);
			
		}*/
		varTieCie = umic.getRescates().getTirCie().divide(BigDecimal.valueOf(100));
		varCapital = umic.getCapitales().getIcapact();
		//varPPRUmic = umic.getPrimas().getPpr();

		//varBloque= obtenerConf.getBloqueBySubproceso(proyUmic, codSubproceso);
	
		/**
		 * Si estoy en el primer periodo (j=1), se establecerán las siguientes variables internas: 
		 *
		 * 	-	varβ = varβ + 1;
		 */
		 
	
		//varBeta = iteracion - 1;

		if (proyUmic.get(iteracion - 1).getBloqueBySubproceso(codSubproceso).getFechaDevengo().after(umic.getFechas().getFecefecfin()) || (proyUmic.get(iteracion - 1).getBloqueBySubproceso(codSubproceso).getFechaDevengo() == null)) {
			varCspIndex = BigDecimal.ZERO;
		}else {
			//varCspIndex = varPrima0.multiply(varTieCie.divide(varTirIni, ConstantsFunciones.MATH_CONTEXT)).multiply((BigDecimal.ONE.add(varPPRUmic)).pow((varBeta)/12));
			varCspIndex = varCapital.multiply(varTieCie);
		}
	
		if (ModuloCSPINDEX.LOG.isTraceEnabled()) {
			ModuloCSPINDEX.LOG.trace("Fin función << moduloCSPINDEX >> de la clase ModuloCSPINDEX, para la iteracion = {}, con resultado cspIndex = {}", iteracion, varCspIndex);
		}
		
		return varCspIndex;
	}
	
}
