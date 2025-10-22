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
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * 
 * @author Ignacio J Del Pozo
 *
 */
public class ModuloCSP204 implements Modulo{
	/**
	 * Cte para log.
	 */	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP204.class);
	
	//incio de las variables estaticas
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP204;
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TC0 = ConstantsModulos.CTE_VAR_TC0.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TC = ConstantsModulos.CTE_VAR_TC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PPR = ConstantsModulos.CTE_VAR_PPR.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PPC_PPAL = ConstantsModulos.CTE_VAR_PPC_PPAL.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PPT_PPAL = ConstantsModulos.CTE_VAR_PPT_PPAL.concat(CLAVE_MODULO);
		
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}
	
	@SuppressWarnings("unchecked")
	public Object execute(Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		//Fin variables locales
		
		try{
			if (ModuloCSP204.LOG.isTraceEnabled()) {
				ModuloCSP204.LOG.trace("Inicio de execute en clase ModuloCSP204");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCSP204
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			//Invocamos a la función moduloCSP204
			resultado = moduloCSP204(proyUmic,bloqueCorriente, iteracion,fcalc,umic,btcUmic,mapVariables, codSubproceso);
			
		}
		catch (Solvencia2Excepcion e) {
			ModuloCSP204.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP204.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP204.LOG.isTraceEnabled()) {
			ModuloCSP204.LOG.trace("Fin de execute en clase ModuloCSP204");
		}
		
		return resultado;
	}

	/**
	 *	Usaremos este módulo para el cálculo de la cuantía nominal de la prestación a pagar en caso de fallecimiento 
	 * 	o invalidez de un seguro COMPLETO.
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
	private BigDecimal moduloCSP204(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//variables locales
		BigDecimal Csp204 = BigDecimal.ZERO ;
		Timestamp varfechaEfecto;
		Integer varTC0;
		BigDecimal varPNAtc;
		BigDecimal varPriTarada;
		BigDecimal varPrimaIni;
		String varCformapago;
		BigDecimal varPPRUmic;
		BigDecimal varPRP;
		Integer varTC;
		BigDecimal varPPR;
		BigDecimal varPPTppal;
		BigDecimal varPPCppal;
		BigDecimal varCSP001;
		Modulo moduloNominal=null;
		List<DetalleCorriente> varProyNominal;
		//Fin variables locales
		
		if (ModuloCSP204.LOG.isTraceEnabled()) {
			ModuloCSP204.LOG.trace("Inicio función << moduloCSP204 >> de la clase ModuloCSP204, para la iteracion = {}", iteracion);
		}
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		/**
		 * Si estoy en el primer periodo, se recuperarán los datos de la umic necesarios para el cálculo que no 
		 * varían por periodo, así como las variables internas que tampoco varíen por periodo, y que se dejarán accesibles
		 * para su uso en el subproceso por los siguientes periodos a calcular.
         *
		 *	Variables Módulo
		 *
		 *	Si la póliza está en vigor, es decir  umic.datosGenerales.csitupol=  ‘VI’
		 *		Si umic.datosGenerales.cnegocio = ‘I’
		 *		varfechaEfecto = umic.fechas.fecefecIni
		 *		En caso contrario:
		 *		varfechaEfecto = umic.fechas fecinisus
		 *	
		 *	
		 *	Si la póliza está reducida, es decir  umic.datosGenerales.csitupol=  ‘RE’
		 *		varfechaEfecto =  umic.fechas.fecefecred
		 *		Si umic.fechas.fecefecred es nula y umic.datosGenerales.cnegocio = ‘C’:
		 *		varfechaEfecto = umic.fechas.fecfinpagprim
		 *	
		 *		varTC0 = TC(varfechaEfecto, fcalc)
		 *		varPNAtc = umic.primas.iprimaact
		 *		varPriTarada = umic.primas.iprimatarada
		 *		varPrimaIni = umic.primas.iprimaini
		 *		varCformapago = umic.primas cformapago
		 *		varPPRUmic = umic.primas.PPR
		 *		varPRP = umic.primas.prevprima 
		 *		varProyNominal = proyUmic
		 *	
		 *	Dejo las variables en memoria, disponibles para el subproceso de la umic.
		 *	
		 *	varBloque= obtenerConfiguracion.getBloqueBySubproceso(proyUmic, codSubproceso)
		 */
		//Calculo de las variables de modulo
		varfechaEfecto=UtilModulos.getVarFechaEfecto(mapVariables, CLAVE_FEC_EFEC, umic);

		varTC0 = UtilModulos.getVarTC0(mapVariables, CLAVE_VAR_TC0, varfechaEfecto, fcalc);
		varPNAtc = umic.getPrimas().getIprimanetaact();
		varPriTarada = umic.getPrimas().getIprimatarada();
		varPrimaIni = umic.getPrimas().getIprimanetaini();
		varCformapago = umic.getPrimas().getCformpago();
		varPPRUmic = umic.getPrimas().getPpr();
		varPRP = umic.getPrimas().getPrevprima();
		varProyNominal = proyUmic;
				
		//Fin de calculo de variables de calculo de modulo
		if((null == bloqueCorriente.getFechaDevengo()) || (bloqueCorriente.getFechaDevengo().after(umic.getFechas().getFecefecfin())))
		{
			Csp204=BigDecimal.ZERO;
		}
		else
		{
			if(!proyUmic.get(iteracion-1).getFechaDesde().after(umic.getFechas().getFecefecfin()))
			{
				varTC = FuncionesAuxiliares.tc(varfechaEfecto,proyUmic.get(iteracion-1).getFechaDesde());
				varPPR = FuncionesPrimas.ppr(varTC0,varTC, varPPRUmic);
				mapVariables.put(CLAVE_VAR_PPR, varPPR);
				varPPTppal = FuncionesPrimas.PPTppal(varPNAtc,varCformapago,varTC0,varTC,varPPRUmic,varPriTarada,varPrimaIni);
				varPPCppal = FuncionesAuxiliares.PPCppal(varPRP, varPrimaIni, varTC);
				mapVariables.put(CLAVE_VAR_PPC_PPAL, varPPCppal);
				mapVariables.put(CLAVE_VAR_PPT_PPAL, varPPTppal);
				
				moduloNominal=FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP001);
				varCSP001=(BigDecimal) moduloNominal.execute(varProyNominal,bloqueCorriente,iteracion,fcalc,umic,btcUmic,codSubproceso);
				Csp204=varCSP001.add(varPPCppal.add(varPPR.add(varPPTppal)));
			}
		}
		
		
		if (ModuloCSP204.LOG.isTraceEnabled()) {
			ModuloCSP204.LOG.trace("Fin de la función << moduloCSP204 >> para la iteración = {}, con resultado Csp204 = {}", iteracion, Csp204);
		}
		return Csp204;
	}
}
