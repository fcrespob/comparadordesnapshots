/** MODIFICACION:TAR00400971-NECESIDADES NUEVO SISTEMA DE PROCESOS TÉCNICOS 
  * Para el correcto calculo del modulo FPTOX2Y1 
  * FECHA: 05/03/2019
  * AUTOR: INDRA
*/
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
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo que devuelve los factores de probabilización a aplicar en  el cálculo de la cuantía probable de una garantía.  
 * El módulo FPTOZC (fcal,j) (fallecimiento en un punto) determina la probabilidad de que una cabeza de edad zc en la fecha de cálculo, fcal, 
 * fallezca en el periodo entre las fechas jant y j, fechas en las que se devengan las prestaciones C(i,jant) y C(i,j) respectivamente 
 * La expresión matemática para su determinación es la siguiente:
 * 				fptozc(fcal,j) = (lzc + nannos(fcal, jant) - lzc + nannos(fcal, j)) / lzc
 * 
 * @author agonzalezgar
 *
 */
public class ModuloFPTOZC implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloFPTOZC.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_FPTOZC;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_EDAD_CAL = ConstantsModulos.CTE_EDAD_CAL.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_ZC = ConstantsModulos.CTE_VAR_ZC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT;
	private static final String CLAVE_VAR_VAL_TAB_MORT = CLAVE_VAL_TAB_MORT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_LZC = ConstantsModulos.CTE_VAR_LZC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_FD_ANT = ConstantsModulos.CTE_VAR_FD_ANT.concat(CLAVE_MODULO);
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
			if (ModuloFPTOZC.LOG.isTraceEnabled()) {
				ModuloFPTOZC.LOG.trace("Inicio de execute en clase FPTOZC");
			}
			
			// Recuperamos los datos que le pasaremos a la función moduloFPTOZC.
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de datos.
			
			//Llamamos a la función moduloFPTOZC.
			resultado = moduloFPTOZC(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloFPTOZC.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloFPTOZC.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloFPTOZC.LOG.isTraceEnabled()) {
			ModuloFPTOZC.LOG.trace("Fin de execute en clase FPTOZC");
		}
		
		return resultado;
	}
	
	/** 
	 * Modulo de cálculo que devuelve los factores de probabilización a aplicar en  el cálculo de la cuantía probable de una garantía.  
	 * El módulo FPTOZC (fcal,j) (fallecimiento en un punto) determina la probabilidad de que una cabeza de edad zc en la fecha de cálculo, fcal, 
	 * fallezca en el periodo entre las fechas jant y j, fechas en las que se devengan las prestaciones C(i,jant) y C(i,j) respectivamente 
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
	private BigDecimal moduloFPTOZC(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		String varCriterioEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriterFec = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal varEdadCalc;
		BigDecimal varEdadNJ = BigDecimal.ZERO;
		List<BigDecimal> lstValoresTabMort = null;
		BigDecimal varFracc1 = BigDecimal.ZERO;
		BigDecimal varFracc2 = BigDecimal.ZERO;
		BigDecimal varZc = BigDecimal.ZERO;
		BigDecimal varLzcEntero = BigDecimal.ZERO;
		BigDecimal varLzcEntero1 = BigDecimal.ZERO;
		BigDecimal varLj = BigDecimal.ZERO;
		BigDecimal varLjEntero = BigDecimal.ZERO;
		BigDecimal varLjEntero1 = BigDecimal.ZERO;
		BigDecimal fptozc = BigDecimal.ZERO;
		BigDecimal varLzc = BigDecimal.ZERO;
		BigDecimal varEdadNJant = BigDecimal.ZERO;
		BigDecimal varLJant = BigDecimal.ZERO;
		Timestamp fecDevAnt = null;
		Timestamp varFechaEfecto = null;
		Integer auxIter = (Integer) mapVariables.get("FPTOZC_Iteracion" + codSubproceso);
		//Fin variables locales
		
		if (ModuloFPTOZC.LOG.isTraceEnabled()) {
			ModuloFPTOZC.LOG.trace("Inicio función << moduloFPTOZC >> de la clase moduloFPTOZC, para la iteracion = {}", iteracion);
		}
		
		// Introducido por nuevos criterios de generación de fechas de pago y devengo (corte de fechas). Si no existe fecha devengo no se realiza el calculo
		if (null == bloqueCorriente.getFechaDevengo() ) {
			return fptozc;
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		/**
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, así como las variables internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso por los siguientes periodos a calcular. 
		 * Variables de Apoyo
		 * - VarCriterEdad --> obtenerConfiguracion.recuperarVariableApoyo(ID-CRITERIO)
		 * - VarCriterFec -->    obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
		 * - Si alguna de las variables de apoyo  retornadas es nulo se devuelve error funcional 005 - No se ha encontrado la Variable de Apoyo &NombreVariableApoyo, finalizando el proceso para la UMIC.
		 * 
		 * Si estoy en el primer periodo (j=1), se establecerán las siguientes variables: 
		 * - varEdadCalc = nedad(umic.fechas.fecinisus, umic.asegurados.fnacAseg1, VarCriterEdad)
		 * 		varEdadCalc --> Dejo la variable disponible en memoria para el subproceso de la umic.
		 * - varAnoNac = Año (umic.asegurados.fnacAseg1)
		 * - varTabMort = btcUmic.tabla1Aseg1
		 * 		varTabMort --> Dejo la variable disponible en memoria para el subproceso de la umic.
		 * - varValoresTabMort = obtenerConfiguracion. recuperarValoresExperiencia(btcUmic.fecCierre, varTabMort, varAnoNac, btcUmic. itcalc1, umic.baseTecIni. psobremort, umic.baseTecIni. priesgo)
		 * - varFracc0 = nanos(umic.fechas.fecinisus, fcalc, VarCriterFec)
		 * - VarZc = VarEdadCalc + varFracc0
		 * - varLzcEntero = valoresTabMort(ENTERO(VarZc)).wkvalor
		 * - varLzcEntero1 = valoresTabMort(ENTERO(VarZc)+1).wkvalor
		 * - varLzc = varLzcEntero + (ParteDecimal(VarZc) )* (varLzcEntero1 – varLzcEntero)
		 * - varFracc1 = naños(fcalc, proyUmic(j).varBloque.fecDevengo, VarCriterFec)
		 * - varEdadNJ = varEdadCalc + naños(fcalc, varFracc1)
		 * - varLjEntero = valoresTabMort(ENTERO(varEdadNJ)).wkvalor
		 * - varLjEntero1 = valoresTabMort(ENTERO(varEdadNJ)+1).wkvalor
		 * - varLj = varLjEntero + (ParteDecimal (varEdadNJ) ) * (varLjEntero1– varLjEntero)
		 * 
		 * Para cualquier periodo j (j > 1), se establecerán las siguientes variables
		 * - varFracc2 = naños(fcalc, proyUmic(j-1).varBloque.fecDevengo,VarCriterFec)
		 * - varEdadNJant = varEdadCalc + varFracc2)
		 * - varLjEntero= valoresTabMort(ENTERO(varEdadNJant)).wkvalor
		 * - varLjEntero1 = valoresTabMort(ENTERO(varEdadNJant)+1).wkvalor
		 * - varLjant = varLjEntero + (ParteDecimal (varEdadNJant) )* (varLjEntero1– varLjEntero)
		 * - varFracc1 = naños(fcalc, proyUmic(j).varBloque.fecDevengo, VarCriterFec)
		 * - varLjEntero = valoresTabMort(ENTERO(varEdadNJ)).wkvalor
		 * - varLjEntero1 = valoresTabMort(ENTERO(varEdadNJ)+1).wkvalor
		 * - varLj = varLjEntero + (ParteDecimal (varEdadNJ) )* (varLjEntero1– varLjEntero)
		 */
		
		// Se definen estas variables auxiliares dado que se llaman varias veces.
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		
		
		// Cálculo y validación de las variables de apoyo.  
		varCriterioEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterioEdad);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFec);
		}// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		// Fin de la validación de las variables de apoyo.
		
				
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		varEdadCalc = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL, varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriterioEdad, umic.getRentas().getFecIni(), varEdifer);
		
		
			varZc = UtilModulos.getVarZc(mapVariables, CLAVE_VAR_ZC, varEdadCalc, varFechaEfecto, fcalc, varCriterFec,varCriterioEdad);
		
		
//		varZc = UtilModulos.getVarZc(mapVariables, CLAVE_VAR_ZC, varEdadCalc, varFechaEfecto, fcalc, varCriterFec);
		varFracc1 = FuncionesAuxiliares.nAnnos(fcalc, bloqueCorriente.getFechaDevengo(), varCriterFec);
		// Cambio Fase VIII
		if (bloqueCorriente.getFechaDevengo() != null) {
//INI-TAR00400971
			if ((umic.getDatosGenerales().getKprestacion() != null) && (umic.getDatosGenerales().getKprestacion().equals("RS901"))) {
				varFracc1 = FuncionesAuxiliares.nAnnos(fcalc,
						proyUmic.get(iteracion-1).getFechaDesde(), varCriterFec);	
			}
//FIN-TAR00400971
			else {
				varFracc1 = FuncionesAuxiliares.nAnnos(fcalc, bloqueCorriente.getFechaDevengo(), varCriterFec);
			}
		}
		else {
			varFracc1 = BigDecimal.ZERO;
		}
		lstValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic,
				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterioEdad, ConstantesSolvencia.CTE_TABMORT_L);
			
		varEdadNJ = varZc.add(varFracc1);
		
		//LOG.warn(varEdadCalc+";"+varZc+";"+varFracc1+";"+varEdadNJ);
		//if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
		if (UtilModulos.getVarFecDevengoAnterior(mapVariables, CLAVE_VAR_FD_ANT, iteracion - 1) == null) {
			
			// Recuperación de variables auxiliares adicionales.
			varLzcEntero = lstValoresTabMort.get(varZc.intValue());
			varLzcEntero1 = lstValoresTabMort.get(varZc.intValue() + 1);
			varLzc = Util.interpolaPorEdad(varLzcEntero, varLzcEntero1, varZc);
			varLzc = UtilModulos.setVarLzcFPTOZC(mapVariables, CLAVE_VAR_LZC, varLzc);
		}
		
		if (iteracion == ConstantsModulos.CTE_FIRST_ITER
				|| auxIter == null){
			mapVariables.put("FPTOZC_Iteracion" + codSubproceso, iteracion);
			mapVariables.put("FPTOZC_FecDevAnt" + codSubproceso, bloqueCorriente.getFechaDevengo());
			varLjEntero = lstValoresTabMort.get(varEdadNJ.intValue());
			varLjEntero1 = lstValoresTabMort.get(varEdadNJ.intValue() + 1);
			varLj = Util.interpolaPorEdad(varLjEntero, varLjEntero1, varEdadNJ);
			
			varLzc = UtilModulos.getVarLzcFPTOZC(mapVariables, CLAVE_VAR_LZC);

			//Se calculará la probabilidad en el periodo j como
			//j=1 --> fptozc = ( varLzc - varLj)/ varLzc
			fptozc = (varLzc.subtract(varLj)).divide(varLzc, ConstantsFunciones.MATH_CONTEXT);
			
		} else {
			
			// Se puede recuperar el varFracc2 si lo guardamos en el mapVariables al final de cada periodo
			fecDevAnt = UtilModulos.getVarFecDevengoAnterior(mapVariables, CLAVE_VAR_FD_ANT, iteracion - 1);
			if (((String) mapVariables.get("VZCREVER_MODULO_FALL")) != null
					&& ((String) mapVariables.get("VZCREVER_MODULO_FALL")).equals(ConstantsFactorias.MODULO_VZC412)) {
				fecDevAnt = bloqueCorriente.getFechaDevengo();
				mapVariables.put("FPTOZC_FecDevAnt" + codSubproceso, bloqueCorriente.getFechaDevengo());
			}
			if (fecDevAnt == null){
							
				//if (umic.getRentas().getFecIni() == null || UtilFechas. umic.getRentas().getFecIni().after(fcalc)) {
				if (umic.getRentas().getFecIni() == null || umic.getRentas().getFecIni().compareTo(fcalc) < 0){
					//fecDevAnt = proyUmic.get(iteracion-2).getBloqueBySubproceso(codSubproceso).getFechaDevengo();		
					//varFracc2 = FuncionesAuxiliares.nAnnos(fcalc, fecDevAnt, varCriterFec);
					if((umic.getDatosGenerales().getKprestacion() != null) && (umic.getDatosGenerales().getKprestacion().equals("RS901"))){
						varFracc2 = FuncionesAuxiliares.nAnnos(fcalc, proyUmic.get(iteracion - 2).getFechaDesde(), varCriterFec);
					}else{
						fecDevAnt = proyUmic.get(iteracion-2).getBloqueBySubproceso(codSubproceso).getFechaDevengo();		
						Timestamp auxFecha = (Timestamp) mapVariables.get("FEC_AUX_FPTOZC");
						
						if (auxFecha == null ) {
							if (bloqueCorriente.getFechaDevengo() != null) {
								auxFecha = bloqueCorriente.getFechaDevengo();
							} else {
								auxFecha = UtilFechas.getUltimoDiaDelMes(fcalc);
							}
							mapVariables.put("FEC_AUX_APTOTC", auxFecha);
						}
						if (fecDevAnt == null) {
								//return BigDecimal.ZERO;
								Timestamp auxFecDev = (Timestamp) mapVariables.get("FEC_AUX_FPTOZC");
								if (auxFecDev == null ) {
									auxFecDev = UtilFechas.getUltimoDiaDelMes(fcalc);
								} else {
									auxFecDev = UtilFechas.plusMeses(auxFecDev, 1);
									if (UtilFechas.getMes(auxFecDev) == 2) {
										auxFecDev = UtilFechas.getUltimoDiaDelMes(auxFecDev);
										
									} else {
										if (UtilFechas.getDia(auxFecDev) != UtilFechas.getDia(auxFecha)) {
											auxFecDev = UtilFechas.incrDias(auxFecDev, UtilFechas.getDia(auxFecha) - UtilFechas.getDia(auxFecDev));
										}
									}
								}
								mapVariables.put("FEC_AUX_FPTOZC", auxFecDev);
								fecDevAnt = auxFecDev;
							} else {
								mapVariables.put("FEC_AUX_FPTOZC", bloqueCorriente.getFechaDevengo());
							}
						varFracc2 = FuncionesAuxiliares.nAnnos(fcalc, fecDevAnt, varCriterFec);
					}
				}else {
					varFracc2 = FuncionesAuxiliares.nAnnos(fcalc, fcalc, varCriterFec);
				}
							
			}else {
				varFracc2 = FuncionesAuxiliares.nAnnos(fcalc, fecDevAnt, varCriterFec);
			}
			
			// varLJant
			varEdadNJant = varZc.add(varFracc2);
			if (varEdadNJant.compareTo(BigDecimal.valueOf(lstValoresTabMort.size()-2)) == 1){
				varLJant = BigDecimal.ZERO;
			}else{
				varLjEntero = lstValoresTabMort.get(varEdadNJant.intValue());
				varLjEntero1 = lstValoresTabMort.get(varEdadNJant.intValue() + 1);
				varLJant = Util.interpolaPorEdad(varLjEntero, varLjEntero1, varEdadNJant);
			}
			
			if (varEdadNJ.compareTo(BigDecimal.valueOf(lstValoresTabMort.size()-2)) == 1){
				varLj = BigDecimal.ZERO;
			}else{
				// Se reutilizan varLjEntero y varLjEntero1 para calcular varLj
				varLjEntero = lstValoresTabMort.get(varEdadNJ.intValue());
				varLjEntero1 = lstValoresTabMort.get(varEdadNJ.intValue() + 1);
				varLj = Util.interpolaPorEdad(varLjEntero, varLjEntero1, varEdadNJ);
			}
			
			//recuperamos el valor de varLzc calculado en el periodo j=1 
			varLzc = UtilModulos.getVarLzcFPTOZC(mapVariables, CLAVE_VAR_LZC);
			
			//Se calculará la probabilidad en el periodo j como
			//j>1 --> fptozc <-- ( varLjant - varLj)/ varLzc
			fptozc = (varLJant.subtract(varLj)).divide(varLzc, ConstantsFunciones.MATH_CONTEXT);
			
		}
		// Se guarda la fecha devengo del periodo actual para usarlo en las siguientes iteraciones
		if((umic.getDatosGenerales().getKprestacion() != null) && (umic.getDatosGenerales().getKprestacion().equals("RS901"))){
			UtilModulos.setVarFecDevengoAnterior(mapVariables, CLAVE_VAR_FD_ANT, iteracion, proyUmic.get(iteracion-1).getFechaDesde());
		}else{
			UtilModulos.setVarFecDevengoAnterior(mapVariables, CLAVE_VAR_FD_ANT, iteracion, bloqueCorriente.getFechaDevengo());
		}
		
		if (ModuloFPTOZC.LOG.isTraceEnabled()) {
			ModuloFPTOZC.LOG.trace("Fin función << moduloFPTOZC >> de la clase moduloFPTOZC, para la iteracion = {}, con resultado fptozc = {}", iteracion, fptozc);
		}
		
		//return fptozc.setScale(ConstantsFunciones.CTE_8, RoundingMode.HALF_DOWN);
		return fptozc;
	}

}