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
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo VZRTAJUB.
 * Clase encargada de cálculo que devuelve los factores de probabilización a aplicar en  el cálculo de la cuantía probable de una garantía.  
 * La aplicación del módulo del factor biométrico al ser multiplicado por el importe nominal del que ya disponemos, nos dará como resultado el flujo probable de cada punto de la corriente.
 * La expresión matemática para su determinación es la siguiente:
 * 				VZRTAJUB=Vzc1(fcal,j)+KGTO*REVER*[((Vzc1(fcal,difercol)*Vzc2(fcal,j))-Vzc1zc2(fcal,j)]
 * 
 * @author agonzalezgar
 *
 */
public class ModuloVZRTAJUB implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVZRTAJUB.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VZRTAJUB;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_EDAD_CAL1 = ConstantsModulos.CTE_EDAD_CAL1.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_EDAD_CAL2 = ConstantsModulos.CTE_EDAD_CAL2.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_ZC1 = ConstantsModulos.CTE_VAR_ZC1.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ZC2 = ConstantsModulos.CTE_VAR_ZC2.concat(CLAVE_MODULO);

	private static final String CLAVE_VAR_VAL_TAB_MORT1 = ConstantsModulos.CTE_VAR_VAL_TAB_MORT1.concat(CLAVE_MODULO);

	private static final String CLAVE_VAR_VAL_TAB_MORT2 = ConstantsModulos.CTE_VAR_VAL_TAB_MORT2.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_LZC1 = ConstantsModulos.CTE_VAR_LZC1.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_LZC2 = ConstantsModulos.CTE_VAR_LZC2.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_REVER = ConstantsModulos.CTE_VAR_REVER.concat(CLAVE_MODULO);
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
		//Varibles locales
		BigDecimal resultado = BigDecimal.ZERO;
		//Fin variables locales
		
		try {
			if (ModuloVZRTAJUB.LOG.isTraceEnabled()) {
				ModuloVZRTAJUB.LOG.trace("Inicio de execute en clase ModuloVZRTAJUB");
			}
			
			// Recuperamos los datos que le pasaremos a la función moduloVZRTAJUB.
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de datos.
			
			//Llamamos a la función moduloVZRTAJUB.
			resultado = moduloVZRTAJUB(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVZRTAJUB.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVZRTAJUB.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloVZRTAJUB.LOG.isTraceEnabled()) {
			ModuloVZRTAJUB.LOG.trace("Fin de execute en clase ModuloVZRTAJUB");
		}
		
		return resultado;
	}
	/**
	 * Modulo de cálculo que devuelve los factores de probabilización a aplicar en  el cálculo de la cuantía probable de una garantía.  
	 * La aplicación del módulo del factor biométrico al ser multiplicado por el importe nominal del que ya disponemos, nos dará como resultado el flujo probable de cada punto de la corriente.
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
	private BigDecimal moduloVZRTAJUB(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal vzrtajub = BigDecimal.ZERO;
		String varCriFec;
		String varCriEdad;
		BigDecimal varGipc;
		BigDecimal varGic;
		BigDecimal varKgto = BigDecimal.ZERO;
		BigDecimal varEdadCalc1;
		BigDecimal varZc1;
		//BigDecimal varFracc0;
		BigDecimal varEdadCalc2;
		BigDecimal varZc2; 
		BigDecimal varVzc1Difercol = BigDecimal.ZERO;
		BigDecimal varLzc1 = BigDecimal.ZERO;
		BigDecimal varLzc2 = BigDecimal.ZERO;
		//BigDecimal varEdadDifer = BigDecimal.ZERO;
		List<BigDecimal> lstValoresTabMort1 = null;
		List<BigDecimal> lstValoresTabMort2 = null;
		Timestamp varFechaEfecto;
		//Fin variables locales
		
		if (ModuloVZRTAJUB.LOG.isTraceEnabled()) {
			ModuloVZRTAJUB.LOG.trace("Inicio función << moduloVZRTAJUB >> de la clase ModuloVZRTAJUB, para la  iteracion = {}", iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		/**
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, 
		 * así como las variables internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso 
		 * por los siguientes periodos a calcular. 
				Variables de Apoyo
				-	VarCriterEdad --> obtenerConfiguracion.recuperarVariableApoyo(ID-CRITERIO)
				-	VarCriterFec --> obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
				-	Si alguna de las variables de apoyo  retornadas es nulo se devuelve error funcional 005 - 
				No se ha encontrado la Variable de Apoyo &NombreVariableApoyo, finalizando el proceso para la UMIC.
				Si estoy en el primer periodo (j=1), se establecerán las siguientes variables: 
				-	varGipc =  btcUmic.gtoRosspPrima
				-	varGic = btcUmic.gtoRosspCap
				o	Si varGic es no nulo ó mayor que cero --> varKgto = 1
				o	Si varGipc es no nulo ó mayor que cero --> varKgto = 0
				-	VarEdadCalc1 = nedad(umic.fechas.fecinisus, umic.asegurados.fnacAseg1, VarCriterEdad) --> dejo la variable en memoria disponible para el subproceso de la umic.
				-	varAnoNac1 = Año (umic.asegurados.fnacAseg1)
				-	varFracc0 = nanos(umic.fechas.fecinisus, fcalc, VarCriterFec)
				-	VarZc1 = VarEdadCalc1 + varFracc0
				-	VarTabMort1 = btcUmic.tabla1Aseg1 --> dejo la variable en memoria disponible para el subproceso de la umic.
				-	varValoresTabMort1 = obtenerConfiguracion. recuperarValoresExperiencia(btcUmic.fecCierre, varTabMort, varAnoNac1, btcUmic. itcalc1, umic.baseTecIni. psobremort, umic.baseTecIni. priesgo)
				
				-	varLzcEntero= varValoresTabMort1(ENTERO(VarZc1)).wkvalor
				-	varLzcEntero1= varValoresTabMort1(ENTERO(VarZc1)+1).wkvalor 
				-	varLzc1 = varLzcEntero + (ParteDecimal(VarZc1)) * (varLzcEntero1 – varLzcEntero) --> Dejo la variable disponible en memoria para el subproceso de la umic.
				Si umic.asegurados.fnacAseg2  no es  nulo: 
				-	VarEdadCalc2 = nedad(umic.fechas.fecinisus, umic.asegurados.fnacAseg2, VarCriterEdad) --> dejo la variable en memoria disponible para el subproceso de la umic.
				-	varAnoNac2= Año (umic.asegurados.fnacAseg1);
				-	VarZc2 = VarEdadCalc2 + varFracc0
				-	VarTabMort2 = btcUmic.tabla1Aseg2 --> dejo la variable en memoria disponible para el subproceso de la umic.
				-	varValoresTabMort2 = obtenerConfiguracion. recuperarValoresExperiencia(btcUmic.fecCierre, varTabMort2, varAnoNac2, btcUmic. itcalc1, umic.baseTecIni. psobremort, umic.baseTecIni. priesgo)
				
				-	varLzcEntero = varValoresTabMort2(ENTERO(VarZc2)).wkvalor
				-	varLzcEntero1 = varValoresTabMort2(ENTERO(VarZc2)+1).wkvalor
				-	varLzc2 = varLzcEntero + (ParteDecimal(VarZc2) )* (varLzcEntero1 – varLzcEntero) --> Dejo la variable disponible en memoria para el subproceso de la umic.
				-	varRever = (umic.rentas.preversion)/100 --> dejo la variable en memoria disponible para el subproceso de la umic.
				-	Si umic.rentas.fecini <= fcalc 
					varVzc1Difercol = 1. 
				-	Si umic.rentas.fecini  > fcalc  
					o	varEdadDifercol = VarEdadCalc1 + nanos(umic.fechas.fecinisus, umic.rentas. fecini, VarCriterFec)
					o	varLDifercol = valoresTabMort1(varEdadDifercol).wkvalor 
					o	varVzc1Difercol = varLDifercol / varLzc1
				 VarVzc1Difercol--> dejo la variable en memoria disponible para el subproceso de la umic.
		 */
		
		// Cálculo y validación de las variables de apoyo.
		varCriEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
				
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriEdad);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
		}// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		// Fin de la validación de las variables de apoyo.
		
		// Validamos que la fecha de devengo no venga a null
		if (null != bloqueCorriente.getFechaDevengo()) {
			
			// Si la modalidad de la UMIC que se trata es del negocio Individuales, se toma la fecha
			// de fin de efecto en lugar de la fecha de inicio. (Cambio de alcance 09/2015)
			if (umic.getDatosGenerales().getCnegocio().equals(ConstantesSolvencia.NEGOCIO_INDIVIDUAL)){
				varFechaEfecto = umic.getFechas().getFecefecini();
			} else {
				varFechaEfecto = umic.getFechas().getFecinisus();
			}
			varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
			varGipc = btcUmic.getGtorosspPrima();
			varGic = btcUmic.getGtorosspCap();
			
			// TODO En el funcional no viene de esta manera.
			if (varGipc.signum() == 1) {
				varKgto = BigDecimal.ZERO;
			}
			if (varGic.signum() == -1) {
				varKgto = BigDecimal.ONE;
			}
			
			lstValoresTabMort1 = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT1, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriEdad, ConstantesSolvencia.CTE_TABMORT_L);
	
			if (null != umic.getAsegurados().getFnacAseg2()) {
				lstValoresTabMort2 = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT2, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG2, varCriEdad, ConstantesSolvencia.CTE_TABMORT_L);
			}
			
			Integer varEdifer = umic.getDatosGenerales().getEdifer();
			varEdadCalc1 = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL1, varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriEdad, umic.getRentas().getFecIni(), varEdifer);
			varZc1 = UtilModulos.getVarZc(mapVariables, CLAVE_VAR_ZC1, varEdadCalc1, varFechaEfecto, fcalc, varCriFec,varCriEdad);
			varLzc1 = UtilModulos.getVarLzc(mapVariables, CLAVE_VAR_LZC1, varZc1,
			lstValoresTabMort1.get(varZc1.intValue()), lstValoresTabMort1.get(varZc1.intValue() + 1));

			if (null != umic.getAsegurados().getFnacAseg2()) {
				varEdadCalc2 = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL2, varFechaEfecto, umic.getAsegurados().getFnacAseg2(), varCriEdad, umic.getRentas().getFecIni(), varEdifer);			
				varZc2 = UtilModulos.getVarZc(mapVariables, CLAVE_VAR_ZC2, varEdadCalc2, varFechaEfecto, fcalc, varCriFec,varCriEdad);
				varLzc2 = UtilModulos.getVarLzc(mapVariables, CLAVE_VAR_LZC2, varZc2, lstValoresTabMort2.get(varZc2.intValue()), lstValoresTabMort2.get(varZc2.intValue() + 1));
			}
			
			vzrtajub = calculoVzrtajub(mapVariables, umic, bloqueCorriente, varCriFec, varLzc1, varLzc2, varKgto, varVzc1Difercol,
					lstValoresTabMort1,
					lstValoresTabMort2,
					varCriEdad, varEdifer);
		}
		
		if (ModuloVZRTAJUB.LOG.isTraceEnabled()) {
			ModuloVZRTAJUB.LOG.trace("Fin función << moduloVZRTAJUB >> de la clase ModuloVZRTAJUB, para la  iteracion = {}, con resultado vzrtajub = {}", iteracion, vzrtajub);
		}
		
		return vzrtajub;
	}
	
	/**
	 * Función encargada de realizar el calculo final del módulo VZRTAJUB, una vez que se hayab recuperado todas las variables necesarias para ello.
	 * 
	 * @param mapVariables mapa con las variables de memoria necesarias
	 * @param umic datos de la umic
	 * @param bloqueCorriente Bloque de Trabajo de la corriente
	 * @param varCriFec
	 * @param varLzc1
	 * @param varLzc2
	 * @param varKgto
	 * @param varVzc1Difercol
	 * @param lstValoresTabMort1
	 * @param lstValoresTabMort2
	 * @return vzrtajub
	 */
	private BigDecimal calculoVzrtajub(
			final Map<String, Object> mapVariables, final Umic umic, final BloqueCorriente bloqueCorriente, final String varCriFec,
			final BigDecimal varLzc1, final BigDecimal varLzc2, final BigDecimal varKgto, final BigDecimal varVzc1Difercol,
			final List<BigDecimal> lstValoresTabMort1, final List<BigDecimal> lstValoresTabMort2,
			final String varCriEdad, final Integer varEdifer) {
		//Variables locales
		BigDecimal vzrtajub = BigDecimal.ZERO;
		BigDecimal varEdadJ1;
		BigDecimal vzc1;
		BigDecimal vzc2;
		BigDecimal vzc1zc2;
		BigDecimal varEdadJ2;
		//BigDecimal varFracc0;
		BigDecimal varLjEntero;
		BigDecimal varLjEntero1;
		BigDecimal varLj1;
		BigDecimal varLj2;
		Timestamp varFechaEfecto;
		//Variables locales
		
		if (ModuloVZRTAJUB.LOG.isTraceEnabled()) {
			ModuloVZRTAJUB.LOG.trace("Inicio de la función << calculoVzrtajub >> de la clase ModuloVZRTAJUB");
		}
		
		// Si la modalidad de la UMIC que se trata es del negocio Individuales, se toma la fecha
		// de fin de efecto en lugar de la fecha de inicio. (Cambio de alcance 09/2015)
		if (umic.getDatosGenerales().getCnegocio().equals(ConstantesSolvencia.NEGOCIO_INDIVIDUAL)){
			varFechaEfecto = umic.getFechas().getFecefecini();
		} else {
			varFechaEfecto = umic.getFechas().getFecinisus();
		}		

		/**
		 * Para cualquier periodo j (j >= 1), se establecerán las siguientes variables
		 * 		-	VarEdadJ1 = varEdadCalc1 + varFracc0
		 * 		-	varLjEntero = varValoresTabMort1(ENTERO(VarEdadJ1)).wkvalor
		 * 		-	varLjEntero1 = varValoresTabMort1(ENTERO(VarEdadJ1)+1).wkvalor
		 * 		-	varLj1 = varLjEntero + (ParteDecimal(VarEdadJ1)) * (varLjEntero1 – varLjEntero) --> Dejo la variable disponible en memoria para el subproceso de la umic.
		 * 		Si umic.asegurados.fnacAseg2 no es nulo:
		 * 			o	VarEdadJ2 = varEdadCalc2 + varFracc0
		 * 			o	varLjEntero= varValoresTabMort2(ENTERO(VarEdadJ2)).wkvalor
		 * 			o	varLjEntero1= varValoresTabMort2(ENTERO(VarEdadJ2)+1).wkvalor 
		 * 			o	varLj2 = varLjEntero + (ParteDecimal(VarEdadJ2)) * (varLjEntero1 – varLjEntero) --> Dejo la variable disponible en memoria para el subproceso de la umic.
		 * 		Se calcularán las siguientes probabilidades:
		 * 			Vzc1 = varLj1 / varLzc1
		 * 			Si umic.asegurados.fnacAseg2  no es  nulo, se calcularán:
		 * 				Vzc2 = varLj2 / varLzc2
		 * 				Vzc1zc2 = Vzc1 * Vzc2;
		 * 		Se calculará la probabilidad en el periodo j como: 
		 * 		-	Si umic.asegurados.fnacAseg2  no es  nulo: 
		 * 				VZRTAJUB=Vzc1+ varKgto * varRever *[( VarVzc1Difercol *Vzc2)-Vzc1zc2]
		 * 		-	Si umic.asegurados.fnacAseg2  es  nulo: 
		 * 				VZRTAJUB =Vzc1
		 */
		varEdadJ1 = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL1, varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriEdad, umic.getRentas().getFecIni(), varEdifer).add(FuncionesAuxiliares.nAnnos(varFechaEfecto, bloqueCorriente.getFechaDevengo(), varCriFec));
		
		varLjEntero = lstValoresTabMort1.get(varEdadJ1.intValue());
		varLjEntero1 = lstValoresTabMort1.get(varEdadJ1.intValue() + 1);
		//varLj1 = varLjEntero.add(varEdadJ1.remainder(BigDecimal.ONE).multiply(varLjEntero1.subtract(varLjEntero)));
		varLj1 = Util.interpolaPorEdad(varLjEntero, varLjEntero1, varEdadJ1);
		
		vzc1 = varLj1.divide(varLzc1, ConstantsFunciones.MATH_CONTEXT);
		vzrtajub = vzc1;
		
		if (null != umic.getAsegurados().getFnacAseg2()) {
			//varEdadJ2 = UtilModulos.getVarEdadCalc2(mapVariables, CLAVE, null).add(varFracc0);
			
			varEdadJ2 = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL2, varFechaEfecto, umic.getAsegurados().getFnacAseg2(), varCriEdad, umic.getRentas().getFecIni(), varEdifer).add(FuncionesAuxiliares.nAnnos(varFechaEfecto, bloqueCorriente.getFechaDevengo(), varCriFec));
						
			// TODO pendiente de duda. parece que deberia ser lstValoresTabMort2
			varLjEntero = lstValoresTabMort2.get(varEdadJ2.intValue());
			varLjEntero1 = lstValoresTabMort2.get(varEdadJ2.intValue() + 1);
			//varLj2 = varLjEntero.add(varEdadJ2.remainder(BigDecimal.ONE).multiply(varLjEntero1.subtract(varLjEntero)));
			varLj2 = Util.interpolaPorEdad(varLjEntero, varLjEntero1, varEdadJ2);
			
			final BigDecimal varRever = UtilModulos.getVarRever(mapVariables, CLAVE_VAR_REVER, umic.getRentas().getPreversion().divide(ConstantsFunciones.CTE_OPER_100, ConstantsFunciones.MATH_CONTEXT));
			
			vzc2 = varLj2.divide(varLzc2, ConstantsFunciones.MATH_CONTEXT);
			vzc1zc2 = vzc1.multiply(vzc2);
			vzrtajub = vzc1.add(varKgto.multiply(varRever).multiply(varVzc1Difercol.multiply(vzc2).subtract(vzc1zc2)));
		}
		
		if (ModuloVZRTAJUB.LOG.isTraceEnabled()) {
			ModuloVZRTAJUB.LOG.trace("Fin de la función << calculoVzrtajub >> de la clase ModuloVZRTAJUB, con resultado vzrtajub = {}", vzrtajub);
		}
		
		return vzrtajub;
	}
}
