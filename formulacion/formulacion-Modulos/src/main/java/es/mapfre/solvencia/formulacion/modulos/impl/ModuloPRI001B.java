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
 * Clase que implementa el modulo PRI001B.
 * Usaremos este módulo cómo módulo de prima para modalidades de 
 * tares con gastos fijos en renovaciones.
 *
 */
public class ModuloPRI001B implements Modulo{

	private static final Logger LOG = LoggerFactory.getLogger(ModuloPRI001B.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_PRI001B;
	private static final String CLAVE_VAR_X = ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_GIPC = ConstantsModulos.CTE_VAR_GIPC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_GIC = ConstantsModulos.CTE_VAR_GIC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_GE = ConstantsModulos.CTE_VAR_GE.concat(CLAVE_MODULO);
	private static final String CLAVE_VARI1  = ConstantsModulos.CTE_VARI1.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_DENOMINADOR = ConstantsModulos.CTE_VAR_DENOMINADOR.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_FACTORI1 = ConstantsModulos.CTE_VAR_FACTORI1.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCM_ZC = ConstantsModulos.CTE_VAR_TCM_ZC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCM_RENO_HASTA = ConstantsModulos.CTE_VAR_TCM_RENO_HASTA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_X_RENO_HASTA = ConstantsModulos.CTE_VAR_X_RENO_HASTA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_MORT_Q = ConstantsModulos.CTE_VAR_VAL_TAB_MORT_Q.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ANT_RENOVA = ConstantsModulos.CTE_VAR_ANT_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PROX_RENOVA = ConstantsModulos.CTE_VAR_PROX_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PRIESGO = ConstantsModulos.CTE_VAR_PRIESGO.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_FACTOR_RIESGO = ConstantsModulos.CTE_VAR_FACTOR_RIESGO.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_INV = ConstantsModulos.CTE_VAR_VAL_TAB_INV.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_GF = ConstantsModulos.CTE_VAR_GF.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_REVALG = ConstantsModulos.CTE_VAR_REVALG.concat(CLAVE_MODULO);
	
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/**
	 * Usaremos este módulo cómo módulo de prima para modalidades de 
	 * tares con gastos fijos en renovaciones.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(Object... args) throws Solvencia2Excepcion {
		
		BigDecimal resultado = BigDecimal.ZERO;
		
		try {
			if (ModuloPRI001B.LOG.isTraceEnabled()) {
				ModuloPRI001B.LOG.trace("Inicio de execute en clase ModuloPRI001B");
			}
			
			//Recuperamos los datos que le pasaremos a la función ModuloPRI001B
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			resultado = moduloPRI001B(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloPRI001B.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloPRI001B.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
	
		if (ModuloPRI001B.LOG.isTraceEnabled()) {
			ModuloPRI001B.LOG.trace("Fin de execute en clase ModuloPRI001B");
		}
		
		return resultado;
	}

	/**
	 * Usaremos este módulo cómo módulo de prima para modalidades de 
	 * tares con gastos fijos en renovaciones.
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
	private BigDecimal moduloPRI001B(List<DetalleCorriente> proyUmic, 
			                         BloqueCorriente        bloqueCorriente, 
			                         int                    iteracion,
			                         Timestamp              fcalc, 
			                         Umic                   umic, 
			                         DetalleBaseTecnica     btcUmic, 
			                         String                 codSubproceso,
			                         Map<String, Object>    mapVariables) {
		
		BigDecimal           pri001b = BigDecimal.ZERO;
		BigDecimal 			 varP;
		BigDecimal 	 		 varGic;
		BigDecimal 	 		 varGE;
		BigDecimal 	 		 varDenominador;
		BigDecimal 	 		 varI1;
		BigDecimal 	 		 varFactorI1;
		String               varTm;
		Timestamp            varAntRenova;
		Timestamp 			 varProxRenova;
		Timestamp        	 varFechaEfecto = null;
		List<BigDecimal>     varValoresQx = null;
		String           	 varCriEdad;
		BigDecimal           varX;
		Integer              varEdifer;
		BigDecimal           varTcmZc;
		BigDecimal           varTcmRenoHasta;
		BigDecimal           varXRenoHasta = BigDecimal.ZERO;
		Integer              varAnoNac;
		BigDecimal           varQxRenoHasta;
		BigDecimal           varPriesgo;
		BigDecimal           varFactorRiesgo;
		String               varTabInv;
		String               varspcom;
		List<BigDecimal>     varValoresTabInv;
		BigDecimal           varGF;
		BigDecimal           varRevalg;
		BigDecimal           varIxRenoHasta;
		BigDecimal           varGipc;
		BigDecimal           varCapGar;
		BigDecimal           varTarifa;
		
		/**
		 * Se realizará la validación de los parámetros de entrada marcados como obligatorios. 
		 * En caso de error se devuelve error funcional A1 - Parámetro obligatorio no informado &NombreAtributoEntrada.
		 * 
		 * 
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, así como las varibales internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso por los siguientes periodos a calcular. 
		 * 
		 *   
		 * Variables Módulo
		 * 	varGipc =  btcUmic.gtoRosspPrima /100 
		 * 	varGic = btcUmic.gtoRosspCap /100 
		 * 	varGE = umic.baseTecIni.pgastgesex1I/100 
		 * 	varDenominador = 1 – varGipc - varGE
		 * 	varI1 = btcUmic.itcalc1/100 
		 * 	varFactorI1 = (1+ varI1 )^0,5 
		 * 	varTm = btcUmic.tabla1Aseg1
		 * 	varAnoNac = Año (umic.asegurados.fnacAseg1)
		 * 	varPriesgo = umic.baseTecIni.priesgo/100
		 * 	varFactorRiesgo = 1 + varPriesgo
		 * 	varTabInv= btcUmic.tabla2Aseg1
		 * 	varspcom = umic.datosgenerales.spcom
		 * 	varValoresTabInv= obtenerConfiguracion. recuperarValoresExperiencia(btcUmic.fecCierre, varTabInv, varAnoNac, btcUmic. itcalc1, umic.baseTecIni.psobremort, umic.baseTecIni.priesgo)
		 * 	varTm = btcUmic.tabla1Aseg1
		 * 	varValoresQx = obtenerConfiguracion.recuperarValoresQx (btcUmic.fecCierre, varTm, varAnoNac, btcUmic.itcalc1, umic.baseTecIni. psobremort, umic.baseTecIni.priesgo)
		 * 	varAntRenova = umic.fechas.fecdesderenova
		 * 	varProxRenova = umic.fechas.fechastarenova
		 * 	varP = 12
		 * 	varGF = umic.baseTecIni.pgastgesin3 Se trata como importe, no como porcentaje.
		 * 	varRevalg = umic.baseTecIni.pgastgesi4/100
		 *  
		 * **Nota: ponemos valores por defecto a varGF y varRevalg ya que de momento van a llegar a cero hasta que se modifique la generación del maestro para que vuelque estos datos.  
		 * 	Si varGF = 0  varGF = 10
		 * 	Si varRevalg = 0  varRevalg = 0,1
		 * 
		 * Dejo las variables en memoria para el subrpoceso de la umic.

		 * 	Si la póliza no está reducida, es decir  umic.datosGenerales.csitupol=  ‘VI’
		 * 	Si umic.datosGenerales.cnegocio = ‘I’ 
		 * 	varfechaEfecto = umic.fechas.fecefecIni
		 * 	En caso contrario:
		 * 	varfechaEfecto = umic.fechas.fecinisus
		 * 	Si la póliza está reducida, es decir  umic.datosGenerales.csitupol=  ‘RE’
		 * 	varfechaEfecto = umic.fechas.fecefecred
		 * 	Si umic.fechas.fecefecred es nula y umic.datosGenerales.cnegocio = ‘C’:
		 * 	varfechaEfecto = umic.fechas.fecfinpagprim
		 * 
		 * 	varX= nedad(varfechaEfecto, umic.asegurados.fnacAseg1, VarCriterEdad, umic.rentas.fecini)  dejo la variable en memoria, disponible para el subproceso de la umic.
		 * 	varTcmZc = TCM(varfechaEfecto, Fcalc)   
		 * 	varTcmRenoHasta = TCM(Fcalc, varProxRenova)   
		 * 	varXRenoHasta = x + entero((varTcmZc + varTcmRenoHasta)/ varP)
		 * 	varIxRenoHasta  = varValoresTabInv(varXRenoHasta).kwvalor
		 * 	varQxRenoHasta  = varValoresQx(varXRenoHasta).kwvalor
		 * 
		 * Dejo las variables en memoria para el subproceso de la umic.
		 * 
		 * Para cualquier periodo j (j >= 1), se calculará el importe de la prima correspondiente al periodo j como: 
		 * 
		 * 	Si proyUmic(j).fecPago es nula: 
		 * 
		 * PRI001B(j) = 0
		 * 
		 * 	Si proyUmic(j).fecPago es no nula: 
		 * 
		 * 	Si proyUmic(j). fecPago >  varAntRenova  y  proyUmic(j). fecPago >  varProxRenova  (periodo en el que reniueva la póliza)
		 * 	varAntRenova = varProxRenova varProxRenova = varProxRenova + 1 año.  Se guardan las variables en memoria para el subproceso de la umic.
		 * 	varTcmRenoHasta = TCM(Fcalc, varProxRenova) 
		 * 	varXRenoHasta = x + entero((varTcmZc + varTcmRenoHasta)/ varP)
		 * 	varIxRenoHasta  = varValoresTabInv (varXRenoHasta).kwvalor
		 * 	varQxRenoHasta  = varValoresQx (varXRenoHasta).kwvalor
		 * 	varGF = varGF * (1 +  varRevalg)
		 *  Se sobreescriben las variables en memoria para el subproceso de la umic.
		 * 
		 * 	Si no se cumple la condición se mantendrá el valor de las variables varAntRenova, varProxRenova, varTcmRenoHasta, varXRenoHasta, varIxRenoHasta , varQxRenoHasta  y varGF
		 * 
		 * 	Si varspcom = ‘P’
		 * 
		 * 	varCapGar = proyUmic(j).corrienteFallecimiento.impFlujoNominal
		 * 	varTarifa = TARIFAxrenohasta (varFactorI1,vargic, varDenominador, varQxRenoHasta , varspcom,varpriesgo)
		 * 
		 * 	En caso contrario: 
		 * 
		 * 	varCapGar = proyUmic(j).corrienteComplementario.impFlujoNominal
		 * 	varTarifa = TARIFAxrenohasta (varFactorI1,vargic, varDenominador, varIxRenoHasta  , varspcom,varpriesgo)
		 * 
		 * PRI001B(j)=varCapGar * varTarifa*varGF
		 */
		
		if (ModuloPRI001B.LOG.isTraceEnabled()) {
			ModuloPRI001B.LOG.trace("Inicio función << moduloPRI001B >> de la clase ModuloPRI001B, para la  iteracion = {}", iteracion);
		}

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
		
		//Variables módulo
		varGipc = UtilModulos.getVarGicDiv100(mapVariables, CLAVE_VAR_GIPC, btcUmic.getGtorosspPrima());
		varGic = UtilModulos.getVarGicDiv100(mapVariables, CLAVE_VAR_GIC, btcUmic.getGtorosspCap());
		varGE = UtilModulos.getPorcentaje(mapVariables, CLAVE_VAR_GE, umic.getBti().getPgastgesex1I());
		varDenominador = UtilModulos.getVarDenominador(mapVariables, CLAVE_VAR_DENOMINADOR, BigDecimal.ONE, varGipc, varGE);
		varI1 = UtilModulos.getPorcentaje(mapVariables, CLAVE_VARI1, btcUmic.getItcalc().get(0));
		varFactorI1 = UtilModulos.getVarFactorI1(mapVariables, CLAVE_VAR_FACTORI1, BigDecimal.ONE, varI1, ConstantsFunciones.CTE_OPER_0_PUNTO_5);
		varTm = btcUmic.getTablacalc1aseg1();
		varAnoNac = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1());
		varPriesgo = UtilModulos.getPorcentaje(mapVariables, CLAVE_VAR_PRIESGO, umic.getBti().getPriesgo());
		varFactorRiesgo = UtilModulos.getVarFactorRiesgo(mapVariables, CLAVE_VAR_FACTOR_RIESGO, varPriesgo);
		varTabInv = btcUmic.getTablacalc2aseg1();
		varspcom = umic.getDatosGenerales().getSpcom();
		varValoresTabInv = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_INV, umic, btcUmic,
				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriEdad, ConstantesSolvencia.CTE_TABMORT_I);
		varTm = btcUmic.getTablacalc1aseg1();
		varValoresQx = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT_Q, umic, btcUmic, 
				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriEdad, ConstantesSolvencia.CTE_TABMORT_Q);
		varAntRenova = UtilModulos.getWFechaRenov(mapVariables, CLAVE_VAR_ANT_RENOVA, umic.getFechas().getFecdesderenova());
		varProxRenova = UtilModulos.getVarProxRenova(mapVariables, CLAVE_VAR_PROX_RENOVA, umic.getFechas().getFechastarenova());
		varP = ConstantsFunciones.CTE_OPER_12;
		varGF = UtilModulos.getVarGF(mapVariables, CLAVE_VAR_GF, umic.getBti().getPgastgesin3I());
		varRevalg = UtilModulos.getPorcentaje(mapVariables, CLAVE_VAR_REVALG, umic.getBti().getPgastgesin4I());
		
		if (varGF.equals(BigDecimal.ZERO)){
			varGF = BigDecimal.TEN;
		} 
		
		if (varRevalg.equals(BigDecimal.ZERO)){
			varRevalg = ConstantsFunciones.CTE_OPER_0_PUNTO_1;
		}
		
		varFechaEfecto = UtilModulos.getVarFechaEfecto(mapVariables, CLAVE_FEC_EFEC, umic);
		
		varEdifer = umic.getDatosGenerales().getEdifer();
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriEdad, umic.getRentas().getFecIni(), varEdifer);
		
		varTcmZc = new BigDecimal(UtilModulos.getVarK(mapVariables, CLAVE_VAR_TCM_ZC, varFechaEfecto, fcalc)); 
		varTcmRenoHasta = new BigDecimal(UtilModulos.getVarK(mapVariables, CLAVE_VAR_TCM_RENO_HASTA, fcalc, varProxRenova));
		varXRenoHasta = UtilModulos.getVarXRenoHasta(mapVariables, CLAVE_VAR_X_RENO_HASTA, varX, varTcmZc, varTcmRenoHasta, varP);
		varIxRenoHasta = varValoresTabInv.get(varXRenoHasta.intValue());
		varQxRenoHasta = varValoresQx.get(varXRenoHasta.intValue());
		
		
		if (bloqueCorriente.getFechaPago().equals(null)){
			
			if (ModuloPRI001B.LOG.isTraceEnabled()) {
				ModuloPRI001B.LOG.trace("Fin función << moduloPRI001B >> de la clase ModuloPRI001B, para la iteracion = {}, con resultado vbx308 = {}", iteracion, pri001b);
			}
			
			return pri001b;
			
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
						varIxRenoHasta = varValoresTabInv.get(varXRenoHasta.intValue());
						varQxRenoHasta = varValoresQx.get(varXRenoHasta.intValue());
						varGF = varGF.multiply(BigDecimal.ONE.add(varRevalg));
						mapVariables.put(CLAVE_VAR_GF, varGF);
				}
				
				if (varspcom.equals(ConstantsFunciones.CTE_GARANTIA_PRINCIPAL)) {
					varCapGar = proyUmic.get(iteracion-1).getBloqueFall().getImpFlujoNominal();
					varTarifa = FuncionesAuxiliares.tarifaXRenoHasta(varFactorI1, varGic, varDenominador, varQxRenoHasta, varspcom, varPriesgo);
				} else {
					varCapGar = proyUmic.get(iteracion-1).getBloqueCompl().getImpFlujoNominal();
					varTarifa = FuncionesAuxiliares.tarifaXRenoHasta(varFactorI1, varGic, varDenominador, varIxRenoHasta, varspcom, varPriesgo);
				}
				
				pri001b = varCapGar.multiply(varTarifa).multiply(varGF);
				
			} else {
				pri001b = umic.getPrimas().getIprimatarada().multiply(BigDecimal.ONE.add(umic.getPrimas().getPrecargfrac().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)));
			}
			
		}
		
		if (ModuloPRI001B.LOG.isTraceEnabled()) {
			ModuloPRI001B.LOG.trace("Fin función << moduloPRI001B >> de la clase ModuloPRI001B, para la iteracion = {}, con resultado pri001b = {}", iteracion, pri001b);
		}
		
		return pri001b;
	}
	
}
