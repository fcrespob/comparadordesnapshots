package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo que devuelve los factores de probabilización a aplicar en  el cálculo de la cuantía probable de una garantía.  
 * La aplicación del módulo del factor biométrico al ser multiplicado por el importe nominal del que ya disponemos, 
 * nos dará como resultado el flujo probable de cada punto de la corriente.
 * La expresión matemática para su determinación es la siguiente:
 * 				VZREVER=Vzc1(fcal,j)+REVER*[((Vzc1(fcal,difercol)*Vzc2(fcal,j))-Vzc1zc2(fcal,j)]
 * 
 * @author agonzalezgar
 *
 */
public class ModuloVZREVER implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVZREVER.class);
	
	
	// Inicio de las variables estáticas para agilizar operaciones.
	
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VZREVER;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VA_CRIT_EDA = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_VA_CRIT_EDA.concat(CLAVE_MODULO);	
	
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	public static final String CLAVE_VAR_EDAD_CAL1 = ConstantsModulos.CTE_EDAD_CAL1.concat(CLAVE_MODULO);
	public static final String CLAVE_VAR_EDAD_CAL2 = ConstantsModulos.CTE_EDAD_CAL2.concat(CLAVE_MODULO);
	public static final String CLAVE_VAR_EDAD_J1_ENTERO = ConstantsModulos.CTE_VAR_EDAD_J1_ENTERO.concat(CLAVE_MODULO);
	public static final String CLAVE_VAR_EDAD_J2_ENTERO = ConstantsModulos.CTE_VAR_EDAD_J2_ENTERO.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_ZC1 = ConstantsModulos.CTE_VAR_ZC1.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ZC2 = ConstantsModulos.CTE_VAR_ZC2.concat(CLAVE_MODULO);
	
	public static final String CLAVE_VAR_VAL_TAB_MORT1 = ConstantsModulos.CTE_VAR_VAL_TAB_MORT1.concat(CLAVE_MODULO);
	public static final String CLAVE_VAR_VAL_TAB_MORT2 = ConstantsModulos.CTE_VAR_VAL_TAB_MORT2.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_LZC1 = ConstantsModulos.CTE_VAR_LZC1.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_LZC2 = ConstantsModulos.CTE_VAR_LZC2.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_REVER = ConstantsModulos.CTE_VAR_REVER.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_DIFERCOL = ConstantsModulos.CTE_VAR_DIFERCOL.concat(CLAVE_MODULO);
	
	public static final String CLAVE_MAPA_ANNOS = ConstantsModulos.CTE_MAPA_ANNOS.concat(CLAVE_MODULO);
	public static final String CLAVE_PARENT_MODULE = "parentModule".concat(CLAVE_MODULO);
	public static final String CLAVE_STOREN_ANNOS = "storenAnnos".concat(CLAVE_MODULO);
	public static final String CLAVE_INCR_FECHA ="incrFecha".concat(CLAVE_MODULO);
	
	public static final String CTE_PROB_J = "probJ".concat(CLAVE_MODULO);
	public static final String CTE_PROB_J1 = "probJ1".concat(CLAVE_MODULO);
	public static final String CTE_PROB_J1_MENOSJ = "probJ1-J".concat(CLAVE_MODULO);
	public static final String CTE_PROB2_J = "prob2J".concat(CLAVE_MODULO);
	public static final String CTE_PROB2_J1 = "prob2J1".concat(CLAVE_MODULO);
	public static final String CTE_PROB2_J1_MENOSJ = "prob2J1-J".concat(CLAVE_MODULO);
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
			
			if (ModuloVZREVER.LOG.isTraceEnabled()) {
				ModuloVZREVER.LOG.trace("Inicio de execute en clase ModuloVZREVER");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZC002
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubProceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			
			//Invocamos a la función de calculo VZREVER
			resultado = moduloVZREVER(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubProceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVZREVER.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVZREVER.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloVZREVER.LOG.isTraceEnabled()) {
			ModuloVZREVER.LOG.trace("Fin de execute en clase ModuloVZREVER");
		}
		
		return resultado;
	}
	/**
	 * Modulo de cálculo que devuelve los factores de probabilización a aplicar en  el cálculo de la cuantía probable de una garantía.  
	 * La aplicación del módulo del factor biométrico al ser multiplicado por el importe nominal del que ya disponemos, 
	 * nos dará como resultado el flujo probable de cada punto de la corriente.
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
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private BigDecimal moduloVZREVER(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubProceso) {
		//Variables locales
		String varCriterioEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriterioFecha = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal	varEdadCalc1 = BigDecimal.ZERO;
		BigDecimal varLzc1 = BigDecimal.ZERO;
		BigDecimal varEdadCalc2 = BigDecimal.ZERO;
		BigDecimal varLzc2 = BigDecimal.ZERO;
		BigDecimal varRever = BigDecimal.ZERO;
		BigDecimal varVzc1Difercol = BigDecimal.ONE;
		BigDecimal	varEdadJ1 = BigDecimal.ZERO;
		BigDecimal varLj1Entero = BigDecimal.ZERO;
		BigDecimal varLj1Entero1 = BigDecimal.ZERO;
		BigDecimal	varEdadJ2 = BigDecimal.ZERO;
		BigDecimal varLj2Entero = BigDecimal.ZERO;
		BigDecimal varLj2Entero1 = BigDecimal.ZERO;
		BigDecimal vzrever = BigDecimal.ZERO;
		BigDecimal varZc1 = BigDecimal.ZERO;
		BigDecimal varZc2 = BigDecimal.ZERO;
		BigDecimal vzc1 = BigDecimal.ZERO;
		BigDecimal vzc2 = BigDecimal.ZERO;
		BigDecimal vzc1zc2 = BigDecimal.ZERO;
		List<BigDecimal> lstValoresTabMort1 = null;
		List<BigDecimal> lstValoresTabMort2 = null;
		BigDecimal varFraccj = BigDecimal.ZERO;
		Timestamp varfechaEfecto;
		Timestamp fechaDevengo = null;
		//Fin variables locales
		
		if (ModuloVZREVER.LOG.isTraceEnabled()) {
			ModuloVZREVER.LOG.trace("Inicio función << moduloVZREVER >> de la clase ModuloVZREVER, para la entrada iteracion = {}",	iteracion);
		}
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, fcalc, umic, btcUmic);
		varCriterioEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_VA_CRIT_EDA);
		varCriterioFecha = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterioEdad);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterioFecha);
		}
		/**
		 * En todos los periodos en los que se esté en una fecha de devengo se harán los cálculos.
		 * Aunque el programa hará llamadas para calcular todas las variables cada vez que se esté en estas fechas, algunas variables
		 * no varían durante la ejecución, entonces el programa retornará directamente su valor (que ha sido almacenado la primera vez que se calcularon).
		 * De esta forma se simula el hecho siguiente (NOTA: se pasan los cálculos de variables del periodo j=1 al primer periodo en el que hay devengo). 
		 * --> estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, 
		 * --> así como las variables internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso por los siguientes periodos a calcular.
			Variables de Apoyo
				-	VarCriterEdad --> obtenerConfiguracion.recuperarVariableApoyo(ID-CRITERIO)
				-	VarCriterFec -->    obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
				-	Si alguna de las variables de apoyo  retornadas es nulo se devuelve error funcional 005 - No se ha encontrado la Variable de Apoyo &NombreVariableApoyo, finalizando el proceso para la UMIC.
			Si estoy en el primer periodo de devengo (j=1), se establecerán las siguientes variables (en el resto de periodos de devengo el programa retornará su valor directamente): 
				-	VarEdadCalc1 = nedad(umic.fechas.fecinisus, umic.asegurados.fnacAseg1, VarCriterEdad), varEdadCalc1 --> dejo la variable en memoria disponible para el subproceso de la umic.
				- 	varAnoNac1 = Año(umic.asegurados.fnacAseg1)
				-	varFracc0 = nanos(umic.fechas.fecinisus, fcalc, VarCriterFec);
				-	VarZc1 = ParteEntera(VarEdadCalc1) +varFracc0
				-	VarTabMort1 = btcUmic.tabla1Aseg1, varTabMort1 --> dejo la variable en memoria disponible para el subproceso de la umic.
				- 	varValoresTabMort1= obtenerConfiguracion. recuperarValoresExperiencia(fichaProceso.fecCierre, varTabMort1, varAnoNac1, btcUmic. itcalc1, umic.baseTecIni. psobremort, umic.baseTecIni. priesgo)
				-	varLzc1Entero= valoresTabMort(ENTERO(VarZc1)).wkvalor 
				-	varLzc1Entero1= valoresTabMort(ENTERO(VarZc1)+1).wkvalor 
				-	varLzc 1= varLzc1Entero + parteDecimal(VarZc1) * (varLzc1Entero1 – varLzc1Entero)

				Si umic.asegurados.fnacAseg2  no es  nulo: 
					-	VarEdadCalc2 = nedad(umic.fechas.fecinisus, umic.asegurados.fnacAseg2, VarCriterEdad), varEdadCalc2 --> dejo la variable en memoria disponible para el subproceso de la umic.
					-	varAnoNac2 = Año(umic.asegurados.fnacAseg2)
					-	VarZc2 = ParteEntera(VarEdadCalc2) + varFracc0
					-	VarTabMort2 =  btcUmic.tabla1Aseg2, varTabMort2 --> SE DEJA la variable en memoria disponible para el subproceso de la umic.
					-	-	varValoresTabMort 2= obtenerConfiguracion. recuperarValoresExperiencia(btcUmic.fecCierre, varTabMort2, varAnoNac2, btcUmic. itcalc1, umic.baseTecIni. psobremort, umic.baseTecIni. priesgo)
					-	varLzc2Entero= valoresTabMort(ENTERO(VarZc2)).wkvalor 
					-	varLzc2Entero1= valoresTabMort(ENTERO(VarZc2)+1).wkvalor 
					-	varLzc 2= varLzc2Entero + parteDecimal(VarZc2) * (varLzc2Entero1 – varLzc2Entero)

				-	varRever = (umic.rentas.preversion)/100, varRever  --> dejo la variable en memoria disponible para el subproceso de la umic.
				-	Si umic.rentas.fecini no es nulo
					-	Si umic.rentas.fecini <= fcalc 
						-	varVzc1Difercol = 1
					-	Si umic.rentas.fecini > fcalc 
					 	-	varFraccDifer = nanos(umic.fechas.fecinisus, umic.rentas. fecini, VarCriterFec)
						-	varEdadDifer = ParteEntera(VarEdadCalc1) + varFraccDifer
						-	varLDifercolEntero = valoresTabMort(ENTERO(varEdadDifer)).wkvalor 
						-	varLDifercolEntero1 = valoresTabMort(ENTERO(varEdadDifer)+1).wkvalor 
						-	varLDifercol = varLDifercolEntero + parteDecimal(varEdadDifer) * (varLDifercolEntero1 – varLDifercolEntero)
						-	varVzc1Difercol = varLDifercol / varLzc1 VarVzc1Difercol --> dejo la variable en memoria disponible para el subproceso de la umic.
			 Para cualquier periodo j (en el que se esté en una fecha de devengo), se establecerán las siguientes variables a partir de las variables que se almacenaron previamente (en el primer periodo de devengo):    
				-	varFraccj= naños(umic.fechas.fecinisus, proyUmic(j).varBloque.fecDevengo, VarCriterFec)
				-	VarEdadJ1 = VarZc1  + varFraccj
				-	varLj1Entero= valoresTabMort(ENTERO(varEdadJ1)).wkvalor 
				-	varLj1Entero1= valoresTabMort(ENTERO(varEdadJ1)+1).wkvalor 
				-	varLj1= varLj1Entero + PARTE_DECIMAL(VarEdadJ1) * (varLj1Entero1– varLj1Entero)

				Si umic.asegurados.fnacAseg2  no es  nulo:
				- 	varFraccj = naños(umic.fechas.fecinisus, proyUmic(j).varBloque.fecDevengo, VarCriterFec).
				-	VarEdadJ2 = VarZc2  + varFraccj
				-	varLj2Entero= valoresTabMort(ENTERO(varEdadJ2)).wkvalor 
				-	varLj2Entero1= valoresTabMort(ENTERO(varEdadJ2)+1).wkvalor 
				-	varLj2= varLj2Entero + PARTE_DECIMAL(VarEdadJ2) * (varLj2Entero1– varLj2Entero)

				Vzc1 = varLj1 / varLzc1
				Si umic.asegurados.fnacAseg2  no es  nulo, se calcularán:
					Vzc2 = varLj2 / varLzc2
					Vzc1zc2 = Vzc1 * Vzc2
			Si no se está en un periodo que coincida con una fecha de devengo el resultado será vzrever=BIGDECIMAL(ZERO).
		 */
		
		fechaDevengo = bloqueCorriente.getFechaDevengo();

		
		if (null != fechaDevengo) {
			 
			// Estos valores NO VARÍAN con cada iteración.
			
			varfechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
			
			/**
				varZc1 = getVarZc(mapVariables, modulo, constanteVariable, varEdadCalc, umic.fechas.fecinisus, fcalc, varCriterioFecha)
			 	getVarZc(...) realiza internamente las siguientes acciones:
			  	-	Si está calculada la devuelve.
			  	-	Si no está calculada:
			  		- varFracc0 = nanos(umic.fechas.fecinisus, fcalc, VarCriterFec);
					- VarZc1 = ParteEntera(VarEdadCalc1) +varFracc0
			 */
			Integer varEdifer = umic.getDatosGenerales().getEdifer();
			varEdadCalc1 = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL1, varfechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriterioEdad, umic.getRentas().getFecIni(), varEdifer);
			
			
			varZc1 = UtilModulos.getVarZc(mapVariables, CLAVE_VAR_ZC1, varEdadCalc1, varfechaEfecto, fcalc, varCriterioFecha,varCriterioEdad);
			int varZc1Entero = varZc1.intValue();

			/**
				  varLzc1 = getVarLzc(mapVariables, modulo, constanteVariable, varZc1, varLzcEntero, varLzcEntero1).
				  varLzcEntero=lstValoresTabMort1.get(varZc1.intValue())
				  varLzcEntero1=lstValoresTabMort1.get(varZc1.intValue() + 1)
				  getVarLzc(...) realiza internamente las siguientes acciones:
				  - Si está calculada la devuelve.
				  - Si no está calculada:
				  		- varLzc1 = varLzc1Entero + parteDecimal(VarZc1) * (varLzc1Entero1 – varLzc1Entero)
			 */
			
			
			lstValoresTabMort1 = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT1, umic, btcUmic, 
					IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterioEdad, ConstantesSolvencia.CTE_TABMORT_L);
			
		
			if(varZc1Entero > lstValoresTabMort1.size()-2) {
				varLzc1 = BigDecimal.ZERO;
			} else {
				varLzc1 = UtilModulos.getVarLzc(mapVariables, CLAVE_VAR_LZC1, varZc1, lstValoresTabMort1.get(varZc1Entero), lstValoresTabMort1.get(varZc1Entero + 1));
			}

			if (mapVariables.get(CLAVE_PARENT_MODULE) != null) {
				if(mapVariables.get(CLAVE_STOREN_ANNOS) != null) {
					varFraccj = FuncionesAuxiliares.nAnnos(fcalc, fechaDevengo,
							varCriterioFecha);
					Map<Timestamp, BigDecimal> mapanAnnos;
					if (null != (mapanAnnos = (Map<Timestamp, BigDecimal>) mapVariables
							.get(CLAVE_MAPA_ANNOS))) {
						mapanAnnos.put(fechaDevengo, varFraccj);
					}
					
				} else {
					Map<Timestamp, BigDecimal> mapanAnnos;
					BigDecimal incr;
					if (null != (mapanAnnos = (Map<Timestamp, BigDecimal>) mapVariables
							.get(CLAVE_MAPA_ANNOS))
							&& null != (incr = (BigDecimal) mapVariables
									.get(CLAVE_INCR_FECHA))) {
						final BigDecimal nAnnos;
						if (null != (nAnnos = mapanAnnos.get(fechaDevengo))) {
							// Se incrementa el valor
							varFraccj = nAnnos.subtract(incr);
						} else {
							varFraccj = FuncionesAuxiliares.nAnnos(fcalc, fechaDevengo,
									varCriterioFecha);
						}
					} else {
						varFraccj = FuncionesAuxiliares.nAnnos(fcalc, fechaDevengo,
								varCriterioFecha);							
					}
				}
			} else {
				varFraccj = FuncionesAuxiliares.nAnnos(fcalc, fechaDevengo,
						varCriterioFecha);
			}
			
			if (varLzc1.signum() != 0) {
				// Estos valores SÍ VARÍAN con cada iteración.

				varEdadJ1 = varZc1.add(varFraccj);
				int varEdadJ1Entero = varEdadJ1.intValue();
				if(varEdadJ1Entero <= lstValoresTabMort1.size()-2) {
					varLj1Entero = lstValoresTabMort1.get(varEdadJ1Entero);
					if (varLj1Entero.signum() != 0) {
						Integer varEdadJ1EnteroAnt = (Integer)mapVariables.get(CLAVE_VAR_EDAD_J1_ENTERO);
						BigDecimal probJ;
						BigDecimal diferJ1J;
						if(varEdadJ1EnteroAnt != null && varEdadJ1EnteroAnt.intValue() == varEdadJ1Entero) {
							// Se recuperan los valores almacenados de una iteración previa
							probJ = (BigDecimal)mapVariables.get(CTE_PROB_J);
							diferJ1J = (BigDecimal)mapVariables.get(CTE_PROB_J1_MENOSJ);
						} else {
							// La expresión final es algo como esto: varLj1Entero/varLzc1 + (varLj1Entero1-varLj1Entero)*parteDecimal(varEdadJ1)/varLzc1
							// Se calculan los términos constantes para ser reutilizados mientras no cambie la indexación de las tablas de experiencia
														
							if(varEdadJ1EnteroAnt != null && varEdadJ1Entero == varEdadJ1EnteroAnt + 1) {
								// Si el salto es sólo de un índice se establece la probabilidad de J1 como la de J1Sig
								probJ = (BigDecimal)mapVariables.get(CTE_PROB_J1); 
							} else {
								probJ = varLj1Entero.divide(varLzc1, ConstantsFunciones.MATH_CONTEXT);
							}
							mapVariables.put(CTE_PROB_J, probJ);
							
							mapVariables.put(CLAVE_VAR_EDAD_J1_ENTERO, varEdadJ1Entero);
							
							varLj1Entero1 = lstValoresTabMort1
									.get(varEdadJ1Entero + 1);
							
							BigDecimal probJ1 = varLj1Entero1.divide(varLzc1, ConstantsFunciones.MATH_CONTEXT);
							mapVariables.put(CTE_PROB_J1, probJ1);
							
							diferJ1J = probJ1.subtract(probJ);
							mapVariables.put(CTE_PROB_J1_MENOSJ, diferJ1J);
							
//							varLj1 = Util.interpolaPorEdad(varLj1Entero,
//									varLj1Entero1, varEdadJ1);
	
							// Resultado si solo hubiera un asegurado:
//							vzc1 = varLj1.divide(varLzc1,
//									ConstantsFunciones.MATH_CONTEXT);
						}
						vzc1 = probJ.add(diferJ1J.multiply(varEdadJ1.subtract(varEdadJ1.setScale(0, RoundingMode.DOWN)), ConstantsFunciones.MATH_CONTEXT));
					} else {
						vzc1 = BigDecimal.ZERO;
					}
				} else {
					vzc1 = BigDecimal.ZERO;
				}
			} else {
				vzc1 = BigDecimal.ZERO;
			}
			vzrever = vzc1;				

			if (umic.getRentas().getFecIni() != null) {
				if(umic.getAsegurados().getFnacAseg2() != null && umic.getRentas().getPreversion().signum() != 0) {
					//Recuperamos las variables de memoria calculadas en el periodo j=1

					// Datos del Asegurado1 necesarios para el cálculo de vzrever en el caso de que haya DOS asegurados.

					/**
					   varVzc1Difercol = getVarVzc1Difercol(mapVariables, modulo, constanteVariable, umic.fechas.fecinisus, umic.asegurados.fnacAseg1, varCriterioEdad).
					 	getVarLzc(...) realiza internamente las siguientes acciones:
					 	- Si está calculada la devuelve:
					 	- Si no está calculada:
					 		- varVzc1Difercol = 1. 					
							- Si umic.rentas.fecini  > fcalc 
								-	varLDifercolEntero= valoresTabMort(ENTERO(varEdadDifer)).wkvalor 
								-	varLDifercolEntero1 = valoresTabMort(ENTERO(varEdadDifer)+1).wkvalor 
								-	varLDifercol = varLDifercolEntero + parteDecimal(varEdadDifer) * (varLDifercolEntero1 – varLDifercolEntero)
								-	varVzc1Difercol = varLDifercol / varLzc1 VarVzc1Difercol --> dejo la variable en memoria disponible para el subproceso de la umic.
					 */
//					varVzc1Difercol = UtilModulos.getVarVzc1Difercol(mapVariables, CLAVE_VAR_DIFERCOL, umic.getFechas().getFecinisus(), umic.getRentas().getFecIni(), fcalc, 
//							varEdadCalc1, lstValoresTabMort1, 
//							varLzc1, varCriterioFecha, BigDecimal.ONE);
					
					varVzc1Difercol = UtilModulos.getVarVzc1Difercol(mapVariables, CLAVE_VAR_DIFERCOL, varfechaEfecto, umic.getRentas().getFecIni(), fcalc, 
							varEdadCalc1, lstValoresTabMort1, 
							varLzc1, varCriterioFecha, BigDecimal.ONE);
					
					// Datos del Asegurado2.
					/**
					 - Similar a varZc1 (ver más arriba).
					 */
					varEdadCalc2 = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL2, varfechaEfecto, umic.getAsegurados().getFnacAseg2(), varCriterioEdad, umic.getRentas().getFecIni(), varEdifer);
					
					
					varZc2 = UtilModulos.getVarZc(mapVariables, CLAVE_VAR_ZC2, varEdadCalc2, varfechaEfecto, fcalc, varCriterioFecha,varCriterioEdad);
					int varZc2Entero = varZc2.intValue();
					
					/**
					 - Similar a varLzc1 (ver más arriba).
					 */
					
					
					lstValoresTabMort2 = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT2, umic, btcUmic,
							IObtenerConfiguracion.OrdenAsegurado.ASEG2, varCriterioEdad, ConstantesSolvencia.CTE_TABMORT_L);
					
					if(varZc2Entero > lstValoresTabMort2.size()-2) {
						varLzc2 = BigDecimal.ZERO;
					} else {
						varLzc2	= UtilModulos.getVarLzc(mapVariables, CLAVE_VAR_LZC2, varZc2, lstValoresTabMort2.get(varZc2Entero), lstValoresTabMort2.get(varZc2Entero + 1));
					}
														
					if (varLzc2.signum() != 0) {
						varEdadJ2 = varZc2.add(varFraccj);
						int varEdadJ2Entero = varEdadJ2.intValue();
						if(varEdadJ2Entero <= lstValoresTabMort2.size()-2) {
							varLj2Entero = lstValoresTabMort2.get(varEdadJ2Entero);
							if (varLj2Entero.signum() != 0) {
								Integer varEdadJ2EnteroAnt = (Integer)mapVariables.get(CLAVE_VAR_EDAD_J2_ENTERO);
								BigDecimal prob2J;
								BigDecimal difer2J1J;
								if(varEdadJ2EnteroAnt != null && varEdadJ2EnteroAnt.intValue() == varEdadJ2Entero) {
									// Se recuperan los valores almacenados de una iteración previa
									prob2J = (BigDecimal)mapVariables.get(CTE_PROB2_J);
									difer2J1J = (BigDecimal)mapVariables.get(CTE_PROB2_J1_MENOSJ);
								} else {
									// La expresión final es algo como esto: varLj2Entero/varLzc2 + (varLj2Entero1-varLj2Entero)*parteDecimal(varEdadJ2)/varLzc2
									// Se calculan los términos constantes para ser reutilizados mientras no cambie la indexación de las tablas de experiencia
																
									if(varEdadJ2EnteroAnt != null && varEdadJ2Entero == varEdadJ2EnteroAnt + 1) {
										// Si el salto es sólo de un índice se establece la probabilidad de J1 como la de J1Sig
										prob2J = (BigDecimal)mapVariables.get(CTE_PROB2_J1); 
									} else {
										prob2J = varLj2Entero.divide(varLzc2, ConstantsFunciones.MATH_CONTEXT);
									}
									mapVariables.put(CTE_PROB2_J, prob2J);
									
									mapVariables.put(CLAVE_VAR_EDAD_J2_ENTERO, varEdadJ2Entero);
									
									varLj2Entero1 = lstValoresTabMort2
											.get(varEdadJ2Entero + 1);
									
									BigDecimal prob2J1 = varLj2Entero1.divide(varLzc2, ConstantsFunciones.MATH_CONTEXT);
									mapVariables.put(CTE_PROB2_J1, prob2J1);
									
									difer2J1J = prob2J1.subtract(prob2J);
									mapVariables.put(CTE_PROB2_J1_MENOSJ, difer2J1J);
									 
								}
								vzc2 = prob2J.add(difer2J1J.multiply(varEdadJ2.subtract(varEdadJ2.setScale(0, RoundingMode.DOWN)), ConstantsFunciones.MATH_CONTEXT));
																							
								// Cálculo de vzrever relacionando los datos ambos asegurados.
								vzc1zc2 = vzc1.multiply(vzc2);
								varRever = UtilModulos.getVarRever(mapVariables, CLAVE_VAR_REVER, umic.getRentas().getPreversion());
								//VZREVER=Vzc1+ varRever *[( VarVzc1Difercol *Vzc2)-Vzc1zc2]
								vzrever = vzc1.add(varRever.multiply(varVzc1Difercol.multiply(vzc2).subtract(vzc1zc2)));
								//LOG.warn(vzc1+";"+vzc2+";"+varVzc1Difercol+";"+vzrever);
							} else {
								vzrever = vzc1;
							}											
						} else {
							vzrever = vzc1;
						}
					} else {
						vzrever = vzc1;
					}
				} 
			}else if(ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
				// Solo se almacena una incidencia 
				final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
				Incidencia aviso = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(), umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(), btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(), ConstantsFunciones.CTE_COD_ERROR_I001, ArrayUtils.EMPTY_OBJECT_ARRAY);
				servicio.almacenarIncidencias(aviso);					
			}
		}
		
		if (ModuloVZREVER.LOG.isTraceEnabled()) {
			ModuloVZREVER.LOG.trace("Fin función << moduloVZREVER >> de la clase ModuloVZREVER, para la iteracion = {}, con resultado vzrever = {}", iteracion, vzrever);
		}
		
		return vzrever;
	}
	
}