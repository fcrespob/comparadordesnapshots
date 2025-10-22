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
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo PRI001.
 * Usaremos este módulo cómo módulo de prima de fallecimiento en 
 * renovaciones para la garantía principal.
 *
 */
public class ModuloPRI001 implements Modulo{
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloPRI001.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_PRI001;
	private static final String CLAVE_VAR_X = ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_GIPC_PRIMA = ConstantsModulos.CTE_VAR_GIPC_PRIMA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_GIC = ConstantsModulos.CTE_VAR_GIC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_GE = ConstantsModulos.CTE_VAR_GE.concat(CLAVE_MODULO);
	private static final String CLAVE_VARI1  = ConstantsModulos.CTE_VARI1.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_DENOMINADOR = ConstantsModulos.CTE_VAR_DENOMINADOR.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_FACTORI1 = ConstantsModulos.CTE_VAR_FACTORI1.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCM_ZC = ConstantsModulos.CTE_VAR_TCM_ZC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCM_RENO_HASTA = ConstantsModulos.CTE_VAR_TCM_RENO_HASTA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_X_RENO_HASTA = ConstantsModulos.CTE_VAR_X_RENO_HASTA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_MORT_Q = ConstantsModulos.CTE_VAR_VAL_TAB_MORT_Q.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ANT_RENOVA = ConstantsModulos.CTE_VAR_ANT_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PROX_RENOVA = ConstantsModulos.CTE_VAR_PROX_RENOVA.concat(CLAVE_MODULO);
	
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la
	 * llamada a la función que realiza los calculos del modulo.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		
		BigDecimal resultado = BigDecimal.ZERO;
				
		try {
			if (ModuloPRI001.LOG.isTraceEnabled()) {
				ModuloPRI001.LOG.trace("Inicio de execute en clase ModuloPRI001");
			}
			
			//Recuperamos los datos que le pasaremos a la función ModuloPRI001
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			resultado = moduloPRI001(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloPRI001.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloPRI001.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
	
		if (ModuloPRI001.LOG.isTraceEnabled()) {
			ModuloPRI001.LOG.trace("Fin de execute en clase ModuloPRI001");
		}
		
		return resultado;
	}
	
	/**
	 * Usaremos este módulo cómo módulo de prima de fallecimiento en renovaciones para la garantía principal.
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
	private BigDecimal moduloPRI001 (final List<DetalleCorriente> proyUmic       ,
			                         final BloqueCorriente        bloqueCorriente,
			                         final int                    iteracion      ,
			                         final Timestamp              fcalc          ,
			                         final Umic                   umic           ,
			                         final DetalleBaseTecnica     btcUmic        ,
			                         final String                 codSubproceso  ,
			                         final Map<String, Object>    mapVariables   ){
		
		BigDecimal           pri001 = BigDecimal.ZERO;
		BigDecimal 			 varP;
		BigDecimal 	 		 vargipcPrima;
		BigDecimal 	 		 varGic;
		BigDecimal 	 		 varGE;
		BigDecimal 	 		 varDenominador;
		BigDecimal 	 		 varI1;
		BigDecimal 	 		 varFactorI1;
		Integer              varTm;
		Timestamp            varAntRenova;
		Timestamp 			 varProxRenova;
		Timestamp        	 varFechaEfecto;
		List<BigDecimal>     varValoresQx = null;
		String           	 varCriEdad;
		BigDecimal           varX;
		Integer              varEdifer;
		BigDecimal           varTcmZc;
		BigDecimal           varTcmRenoHasta;
		BigDecimal           varXRenoHasta = BigDecimal.ZERO;
		Integer              varAnoNac;
		BigDecimal			 varFallcj;
		BigDecimal           varQxRenoHasta;
		BigDecimal 			 calculoAux;
		BigDecimal			 varNP = BigDecimal.ZERO;
		
		if (ModuloPRI001.LOG.isTraceEnabled()) {
			ModuloPRI001.LOG.trace("Inicio función << moduloPRI001 >> de la clase ModuloPRI001, para la  iteracion = {}", iteracion);
		}
		
		/**
		 * Se realizará la validación de los parámetros de entrada marcados como obligatorios. 
		 * En caso de error se devuelve error funcional A1 - Parámetro obligatorio no informado &NombreAtributoEntrada.
		 * 
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, así como las varibales internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso por los siguientes periodos a calcular. 
		 * 	  
		 * Variables Módulo
		 * varGipc =  btcUmic.gtoRosspPrima /100 
		 * varGic = btcUmic.gtoRosspCap /100 
		 * varGE = umic.baseTecIni.pgastgesex1I /100 
		 * varDenominador = 1 – varGipc - varGE
		 * varI1 = btcUmic.itcalc1/100 
		 * varFactorI1 = (1+ varI1  )^0,5 
		 * varTm = btcUmic.tabla1Aseg1
		 * varAnoNac = Año (umic.asegurados.fnacAseg1)
		 * varValoresQx = obtenerConfiguracion.recuperarValoresQx (btcUmic.fecCierre, varTm, varAnoNac, btcUmic.itcalc1, umic.baseTecIni. psobremort, umic.baseTecIni.priesgo)
		 * varAntRenova = umic.fechas.fecdesderenova
		 * varProxRenova = umic.fechas.fechastarenova
		 * varP = 12
		 * 
		 * Si la póliza no está reducida, es decir  umic.datosGenerales.csitupol=  ‘VI’
		 * Si umic.datosGenerales.cnegocio = ‘I’ 
		 * varfechaEfecto = umic.fechas.fecefecIni
		 * En caso contrario:
		 * varfechaEfecto = umic.fechas.fecinisus
		 * Si la póliza está reducida, es decir  umic.datosGenerales.csitupol=  ‘RE’
		 * varfechaEfecto = umic.fechas.fecefecred
		 * Si umic.fechas.fecefecred es nula y umic.datosGenerales.cnegocio = ‘C’:
		 * varfechaEfecto = umic.fechas.fecfinpagprim
		 * 
		 * varX= nedad(varfechaEfecto, umic.asegurados.fnacAseg1, VarCriterEdad, umic.rentas.fecini)  dejo la variable en memoria, disponible para el subproceso de la umic.
		 * varTcmZc = TCM(varfechaEfecto, Fcalc)   
		 * varTcmRenoHasta = TCM(Fcalc, varProxRenova)   
		 * varXRenoHasta = x + entero((varTcmZc + varTcmRenoHasta)/ varP)
		 * varQxRenoHasta  = varValoresQx (varXRenoHasta).kwvalor
		 * Dejo las variables en memoria para el subproceso de la umic.
		 * 
		 * Para cualquier periodo j (j >= 1), se calculará el importe de la prima correspondiente al periodo j como: 
		 * 
		 * Si proyUmic(j).fecPago es nula: 
		 * 
		 * PRI001(j) = 0
		 * 
		 * Si proyUmic(j).fecPago es no nula: 
		 * 
		 * Si proyUmic(j). fecPago >  varAntRenova  y  proyUmic(j). fecPago >  varProxRenova  (periodo en el que reniueva la póliza)
		 * varAntRenova = varProxRenova varProxRenova = varProxRenova + 1 año.  Se guardan las variables en memoria para el subproceso de la umic.
		 * varTcmRenoHasta = TCM(Fcalc, varProxRenova) 
		 * varXRenoHasta = x + entero((varTcmZc + varTcmRenoHasta)/ varP)
		 * varQxRenoHasta  = varValoresQx (varXRenoHasta).kwvalor
		 * Se sobreescriben las variables en memoria para el subproceso de la umic.
		 * 
		 * Si no se cumple la condición se mantendrá el valor de las variables varAntRenova, varProxRenova, varTcmRenoHasta, varXRenoHasta, varQxRenoHasta  
		 * 
		 * varFallcj = proyUmic(j).corrienteFallecimiento.impFlujoNominal
		 * 
		 * PRI001(j)=varFallcj * [((varFactorI1 * varQxRenoHasta)+varGic ))/(varDenominador )]
		 */
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Definición de variables auxiliares para agilizar las operaciones (se llaman varias veces).
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		
		// Cálculo de las variables de apoyo.
		varCriEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		
		
		// Validación de las variables de apoyo.
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriEdad);
		}
		
		//Variables Módulo
		vargipcPrima = UtilModulos.getVarGicDiv100(mapVariables, CLAVE_VAR_GIPC_PRIMA, btcUmic.getGtorosspPrima()); 
		varGic = UtilModulos.getVarGicDiv100(mapVariables, CLAVE_VAR_GIC, btcUmic.getGtorosspCap());
		varGE = UtilModulos.getVarGicDiv100(mapVariables, CLAVE_VAR_GE, umic.getBti().getPgastgesex1I());
		varDenominador = UtilModulos.getVarDenominador(mapVariables, CLAVE_VAR_DENOMINADOR, BigDecimal.ONE, vargipcPrima, varGE);
		varI1 = UtilModulos.getVarGicDiv100(mapVariables, CLAVE_VARI1, btcUmic.getItcalc().get(0));
		varFactorI1 = UtilModulos.getVarFactorI1(mapVariables, CLAVE_VAR_FACTORI1, BigDecimal.ONE, varI1, ConstantsFunciones.CTE_OPER_0_PUNTO_5);
		varTm = Integer.valueOf(btcUmic.getTablacalc1aseg1());
		varAnoNac = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1());
		varValoresQx = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT_Q, umic, btcUmic, 
				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriEdad, ConstantesSolvencia.CTE_TABMORT_Q);
		varAntRenova = UtilModulos.getWFechaRenov(mapVariables, CLAVE_VAR_ANT_RENOVA, umic.getFechas().getFecdesderenova());
		varProxRenova = UtilModulos.getVarProxRenova(mapVariables, CLAVE_VAR_PROX_RENOVA, umic.getFechas().getFechastarenova());
		varP = ConstantsFunciones.CTE_OPER_12;
		varFechaEfecto = UtilModulos.getVarFechaEfecto(mapVariables, CLAVE_FEC_EFEC, umic);
		
		varEdifer = umic.getDatosGenerales().getEdifer();
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriEdad, umic.getRentas().getFecIni(), varEdifer);
		
		varTcmZc = new BigDecimal(UtilModulos.getVarK(mapVariables, CLAVE_VAR_TCM_ZC, varFechaEfecto, fcalc)); 
		varTcmRenoHasta = new BigDecimal(UtilModulos.getVarK(mapVariables, CLAVE_VAR_TCM_RENO_HASTA, fcalc, varProxRenova));
		varXRenoHasta = UtilModulos.getVarXRenoHasta(mapVariables, CLAVE_VAR_X_RENO_HASTA, varX, varTcmZc, varTcmRenoHasta, varP);
		varQxRenoHasta = varValoresQx.get(varXRenoHasta.intValue());
		
		if (ConstantsFunciones.CTE_4_STRING.equals(umic.getPrimas().getCformpago())) {
			varNP = ConstantsFunciones.CTE_OPER_12;
		} else if (ConstantsFunciones.CTE_3_STRING.equals(umic.getPrimas().getCformpago())) {
			varNP = ConstantsFunciones.CTE_OPER_4;
		} else if (ConstantsFunciones.CTE_1_STRING.equals(umic.getPrimas().getCformpago()) || ConstantsFunciones.CTE_2_STRING.equals(umic.getPrimas().getCformpago())) {
			varNP = new BigDecimal(umic.getPrimas().getCformpago());
		}
		
		if (null == bloqueCorriente.getFechaPago()) {
			
			if (ModuloPRI001.LOG.isTraceEnabled()) {
				ModuloPRI001.LOG.trace("Fin función << moduloGZC001 >> de la clase ModuloGZC001, para la  iteracion = {}, con resultado gzc001 = {}", iteracion, pri001);
			}
			
			return pri001;
			
		} else {
			
			if(bloqueCorriente.getFechaPago().after(umic.getFechas().getFecefecfin())){
			
			if (bloqueCorriente.getFechaPago().after(varAntRenova) && 
				bloqueCorriente.getFechaPago().after(varProxRenova)){
				varAntRenova = varProxRenova;
				mapVariables.put(CLAVE_VAR_ANT_RENOVA, varAntRenova);
				varProxRenova = UtilModulos.incrementaVarProxRenova(mapVariables, CLAVE_VAR_PROX_RENOVA);
				varTcmRenoHasta = new BigDecimal(FuncionesAuxiliares.tcm(fcalc, varProxRenova));
				mapVariables.put(CLAVE_VAR_TCM_RENO_HASTA, varTcmRenoHasta);
				varXRenoHasta = varX.add(new BigDecimal(varTcmZc.add(varTcmRenoHasta).divide(varP).intValue()));
				mapVariables.put(CLAVE_VAR_X_RENO_HASTA, varXRenoHasta);
				varQxRenoHasta = varValoresQx.get(varXRenoHasta.intValue());
			}
			varFallcj = proyUmic.get(iteracion).getBloqueFall().getImpFlujoNominal();
			
			calculoAux = varGic.add(varFactorI1.multiply(varQxRenoHasta));
			pri001 = varFallcj.multiply(calculoAux.divide(varDenominador, ConstantsFunciones.MATH_CONTEXT));
			}else{
				
				pri001 = umic.getPrimas().getIprimatarada().multiply(BigDecimal.ONE.add(umic.getPrimas().getPrecargfrac().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01))).divide(varNP, ConstantsFunciones.MATH_CONTEXT);
			}
		}
		
		
		
		if (ModuloPRI001.LOG.isTraceEnabled()) {
			ModuloPRI001.LOG.trace("Fin función << moduloGZC001 >> de la clase ModuloGZC001, para la  iteracion = {}, con resultado gzc001 = {}", iteracion, pri001);
		}
		
		return pri001;
		
	}
	
}
