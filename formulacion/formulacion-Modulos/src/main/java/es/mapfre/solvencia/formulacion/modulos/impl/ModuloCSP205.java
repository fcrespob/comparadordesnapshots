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
public class ModuloCSP205 implements Modulo {

	/**
	 * Cte para log.
	 */	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP205.class);
	
	//incio de las variables estaticas
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP205;
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VARN = ConstantsModulos.CTE_VARN.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TC0 = ConstantsModulos.CTE_VAR_TC0.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TC = ConstantsModulos.CTE_VAR_TC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PPR = ConstantsModulos.CTE_VAR_PPR.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PPC_PPAL = ConstantsModulos.CTE_VAR_PPC_PPAL.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PPT_PPAL = ConstantsModulos.CTE_VAR_PPT_PPAL.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_SUMA_F = ConstantsModulos.CTE_VAR_SUMA_F.concat(CLAVE_MODULO);
	
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
			if (ModuloCSP205.LOG.isTraceEnabled()) {
				ModuloCSP205.LOG.trace("Inicio de execute en clase ModuloCSP205");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCSP205
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
		
			//Invocamos a la función moduloCSP205
			resultado = moduloCSP205(proyUmic,bloqueCorriente, iteracion,fcalc,umic,btcUmic,codSubproceso,mapVariables);
		}
		catch (Solvencia2Excepcion e) {
			ModuloCSP205.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP205.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP205.LOG.isTraceEnabled()) {
			ModuloCSP205.LOG.trace("Fin de execute en clase ModuloCSP205");
		}
		
		return resultado;
	}
	
	/**
	 *	Usaremos este módulo para el cálculo de la cuantía nominal de la Prestación a
	 *	pagar en caso de supervivencia para las modalidades del negocio individual.
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
	

	private BigDecimal moduloCSP205(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,  final Map<String, Object> mapVariables) {
		//variables locales
		BigDecimal Csp205 = BigDecimal.ZERO;
		Timestamp varfechaEfecto;
		Integer varN;
		Integer varTC0;
		BigDecimal varPNAtc;
		BigDecimal varPriTarada;
		BigDecimal varPrimaIni;
		String varCformapago;
		BigDecimal varPPRUmic;
		List<DetalleCorriente> varProyNominal;
		BigDecimal varPRP;
		Integer varTC;
		BigDecimal varPPR;
		BigDecimal varPPTppal;
		BigDecimal varPPCppal;
		BigDecimal varCSP001;
		Modulo moduloNominal=null;
		BigDecimal varSumaF;
		BigDecimal varPNAtc1;
		//Fin variables locales
		
		if (ModuloCSP205.LOG.isTraceEnabled()) {
			ModuloCSP205.LOG.trace("Inicio función << moduloCSP205 >> de la clase ModuloCSP205, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		/**
		 * 	Si estoy en el primer periodo, se recuperarán los datos de la umic necesarios para el cálculo que no varían
		 * por periodo, así como las variables internas que tampoco varíen por periodo, y que se dejarán accesibles para
		 * su uso en el subproceso por los siguientes periodos a calcular.
		 *	
		 *	Variables Módulo
		 *
		 *	Si la póliza está en vigor, es decir  umic.datosGenerales.csitupol=  ‘VI’
		 *		Si umic.datosGenerales.cnegocio = ‘I’
		 *		varfechaEfecto = umic.fechas.fecefecIni
		 *		En caso contrario:
		 *		varfechaEfecto = umic.fechas fecinisus
		 *	
		 *		varN = umic.duraciones.ndursegano 
		 *	
		 *	Si la póliza está reducida, es decir  umic.datosGenerales.csitupol=  ‘RE’
		 *		varfechaEfecto =  umic.fechas.fecefecred
		 *		Si umic.fechas.fecefecred es nula y umic.datosGenerales.cnegocio = ‘C’:
		 *		varfechaEfecto = umic.fechas.fecfinpagprim
		 *		Si umic.datosGenerales.cnegocio = ‘I’ 
		 *		varN = umic.duraciones.ndursegano –tc(umic.fechas fecefecIni, umic.fechas.fecefecred)
		 *		En caso contrario:
		 *		varN = umic.duraciones.ndursegano –tc(umic.fechas fecinisus, umic.fechas.fecefecred)
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
		 *		varBloque= obtenerConfiguracion.getBloqueBySubproceso(proyUmic, codSubproceso)
		 *
		 */
		//Calculo de las variables de modulo
		
		varfechaEfecto=UtilModulos.getVarFechaEfecto(mapVariables, CLAVE_FEC_EFEC, umic);
		varN=UtilModulos.getVarN(mapVariables, CLAVE_VARN, umic);
		
		varTC0=UtilModulos.getVarTC0(mapVariables, CLAVE_VAR_TC0, varfechaEfecto, fcalc);
		varPNAtc=umic.getPrimas().getIprimanetaact();
		varPriTarada=umic.getPrimas().getIprimatarada();
		varPrimaIni=umic.getPrimas().getIprimanetaini();
		varCformapago=umic.getPrimas().getCformpago();
		varPPRUmic=umic.getPrimas().getPpr();
		varPRP = umic.getPrimas().getPrevprima();
		varProyNominal= proyUmic;

		if((null == bloqueCorriente.getFechaDevengo()) || (bloqueCorriente.getFechaDevengo().after(umic.getFechas().getFecefecfin())))
		{
			Csp205=BigDecimal.ZERO;
		}
		else
		{
			if(proyUmic.get(iteracion-1).getFechaDesde().before(umic.getFechas().getFecefecfin()))
			{
				varTC = FuncionesAuxiliares.tc(varfechaEfecto,proyUmic.get(iteracion-1).getFechaDesde()) + 1;
				varPPR = FuncionesPrimas.ppr(varTC0,varTC, varPPRUmic);
				varPPCppal = FuncionesAuxiliares.PPCppal(varPRP, varPrimaIni, varTC);
				varPPTppal = FuncionesPrimas.PPTppal(varPNAtc,varCformapago,varTC0,varTC,varPPRUmic,varPriTarada,varPrimaIni);
				
				if(varTC > varTC0){
					varSumaF = BigDecimal.ZERO;
				}else{
				varSumaF=FuncionesPrimas.sumaf(varTC0, varTC, varN, varPRP);
				}
				
				moduloNominal=FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP001);
				varCSP001=(BigDecimal) moduloNominal.execute(varProyNominal,bloqueCorriente, iteracion,fcalc,umic,btcUmic,codSubproceso);
				
				varPNAtc1=varPNAtc.multiply((BigDecimal.ONE.add(varPRP.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).pow(varTC-varTC0+1, ConstantsFunciones.MATH_CONTEXT)));

				Csp205=varCSP001.add(varPPCppal.add(varPPR.add(varPPTppal))).add(varPNAtc1.multiply(varSumaF));
			}
		}
		if (ModuloCSP205.LOG.isTraceEnabled()) {
			ModuloCSP205.LOG.trace("Fin de la función << moduloCSP205 >> para la iteración = {}, con resultado Csp205 = {}", iteracion, Csp205);
		}
		return Csp205;
	}	
}
