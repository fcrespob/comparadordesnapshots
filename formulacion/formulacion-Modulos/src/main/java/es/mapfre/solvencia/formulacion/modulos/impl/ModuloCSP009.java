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
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * 
 * @author Ignacio J Del Pozo
 *
 */
public class ModuloCSP009 implements Modulo {
	
	/**
	 * Cte para log.
	 */	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP009.class);
	
	//incio de las variables estaticas
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP009;
	private static final String CLAVE_BETAIDMS = ConstantsModulos.CTE_BETAIDMS.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_FACTOR_REV = ConstantsModulos.CTE_VAR_FACTOR_REV.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}
	
	@SuppressWarnings("unchecked")
	public Object execute(Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		//Fin variables locales
		
		try{
			if (ModuloCSP009.LOG.isTraceEnabled()) {
				ModuloCSP009.LOG.trace("Inicio de execute en clase ModuloCSP009");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCSP009
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];

			//Invocamos a la función moduloCSP009
			resultado = moduloCSP009(proyUmic,bloqueCorriente,iteracion,fcalc,umic,btcUmic,mapVariables,codSubproceso);
		}
		catch (Solvencia2Excepcion e) {
			ModuloCSP009.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP009.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP009.LOG.isTraceEnabled()) {
			ModuloCSP009.LOG.trace("Fin de execute en clase ModuloCSP009");
		}
		
		return resultado;
	}
	
	
	/**
	 * Usaremos este módulo para el cálculo de la cuantía nominal de la prestación complementaria a
	 * utilizar en las modalidades del negocio de Individuales.
	 * 
	 * @param proyUmic
	 * 			Estructura detalleCorrientes de la umic
	 * @param iteracion
	 * 			Indica el periodo de proyección J que se está calculando de entre todos los periodos de proyección de la umic (proyUmic)
	 * @param Fcalc
	 * 			Fecha de Cálculo 
	 * @param umic
	 * 			Contiene los datos de la Umic que se está procesando.
	 * @param btcUmic
	 * 			Contiene el detalle de la base técnica de cálculo para la umic.
	 * @param codSubproceso 
	 * 			Código el subproceso que se está ejecutando.
	 */
	private BigDecimal moduloCSP009(final List<DetalleCorriente> proyUmic,final BloqueCorriente bloqueCorriente, final Integer iteracion, final Timestamp fcalc,final Umic umic,
			final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables,final String codSubproceso)
	{
		//variables locales
		BigDecimal varBetalIDMS = BigDecimal.ZERO;
		BigDecimal csp009 = BigDecimal.ZERO;
		BigDecimal varFactorRev;
		BigDecimal varIcapIni;
		Integer varTC;
		Timestamp varfechaEfecto = null;
		Modulo moduloNominal=null;
		BigDecimal varCSP001;
		List<DetalleCorriente> varProyNominal;
		//Fin variables locales
		
		if (ModuloCSP009.LOG.isTraceEnabled()) {
			ModuloCSP009.LOG.trace("Inicio de la función << moduloCSP009 >> de la clase moduloCSP005, para la iteración = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		/**
		 * Si estoy en el primer periodo, se recuperarán los datos de la umic necesarios para el cálculo
		 * que no varían por periodo, así como las variables internas que tampoco varíen
		 * por periodo, y que se dejarán accesibles para su uso en el subproceso por los siguientes 
		 * periodos a calcular.

			Variables de Apoyo
			
				VarBetaIDMS obtenerConfiguracion.recuperarVariableApoyo(BETAIDMS)
			
			Si alguna de las variable de apoyo  retornada es nula se devuelve error funcional 005 - No se ha encontrado la Variable de Apoyo & NombreVariableApoyo, finalizando el proceso para la UMIC.
			
			Varibles Módulo
			
				varPrc= umic.capitales.porevalcap/100  dejo la variable en memoria, disponible para el subproceso de la umic.
				varFactorRev = 1 + varPrc  dejo la variable en memoria, disponible para el subproceso de la umic.
			
			Para cualquier periodo j (j >= 1), se calculará el importe del capital correspondiente al periodo j como: 
			
				Si la póliza no está reducida, es decir  umic.datosGenerales.csitupol=  ‘VI’
			o	Si umic.datosGenerales.cnegocio = ‘I’ 
				varfechaEfecto = umic.fechas.fecefecIni
			o	En caso contrario:
				varfechaEfecto = umic.fechas.fecinisus
				Si la póliza está reducida, es decir  umic.datosGenerales.csitupol=  ‘RE’
			o	varfechaEfecto = umic.fechas.fecefecred
			o	Si umic.fechas.fecefecred es nula y umic.datosGenerales.cnegocio = ‘C’:
				varfechaEfecto = umic.fechas.fecfinpagprim

		 */
		
		// Definición de variables auxiliares para agilizar las operaciones (se llaman varias veces).
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		// Cálculo de las variables de apoyo.
		varBetalIDMS =UtilModulos.getVarBetaIDMS(mapVariables, CLAVE_BETAIDMS, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), ConstantsModulos.CTE_BETAIDMS); 
				
		// Fin del cálculo de las variables de apoyo.
		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoBetaIDMS(varBetalIDMS);
		} // Fin de la validación.

		//Calculo de las variables de modulo
		varFactorRev=UtilModulos.getVarUnoMasPrp(mapVariables, CLAVE_VAR_FACTOR_REV, umic.getCapitales().getPorevalcap());
		varIcapIni = umic.getCapitales().getIcapini();
		//Fin de calculo de variables de calculo de modulo
		
		varfechaEfecto = UtilModulos.getVarFechaEfecto(mapVariables, CLAVE_FEC_EFEC, umic);
		varProyNominal = proyUmic;
		//moduloNominal=FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP001);
		//varCSP001=(BigDecimal) moduloNominal.execute(varProyNominal,bloqueCorriente,iteracion,fcalc,umic,btcUmic,mapVariables,codSubproceso);
		
		if(bloqueCorriente.getFechaDevengo().after(umic.getFechas().getFecefecfin()) || null == bloqueCorriente.getFechaDevengo()){
			csp009 = BigDecimal.ZERO;
		}else{
			if(proyUmic.get(iteracion-1).getFechaDesde().before(umic.getFechas().getFecefecfin())){
				
				varTC = FuncionesAuxiliares.tc(varfechaEfecto,bloqueCorriente.getFechaDevengo());
				
				csp009=varBetalIDMS.multiply(varIcapIni.multiply(varFactorRev.pow(varTC.intValue())));
				
			}else{
				
				csp009 = BigDecimal.ZERO;
				
			}
		}
	 
		return csp009;
	}
}
