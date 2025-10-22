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
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada de determinar el cálculo de los factores de actualización financiera para la garantía  
 * en un periodo dado así como el Flujo Actualizado de dicho periodo.
 * La expresión matemática para su determinación es la siguiente:
 * 			Si fcalc <= ffincas entonces
 * 					Si j <= ffincas entonces
 * 							Actfin(fcal,j) = (1 + it(CT))^ -(nannos(fcal,j))
 * 					Sino
 * 							Actfin(fcal,j) = (1 + it(CT))^ -(nannos(fcal,ffincas)) * (1 + it2)^ -(nannos(ffincas, j))
 * 			Sino
 * 					Actfin(fcal,j) = (1 + it2)^ -(nannos(fcal,j))
 * 
 * @author agonzalezgar
 *
 */
public class ModuloACT001 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloACT001.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_ACT001;
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final BigDecimal CTE_0_PUNTO_01 = ConstantsFunciones.CTE_OPER_0_PUNTO_01;
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
			
			if (ModuloACT001.LOG.isTraceEnabled()) {
				ModuloACT001.LOG.trace("Inicio de execute en clase ModuloACT001");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloACT001
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo moduloACT001
			resultado = moduloACT001(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloACT001.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloACT001.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloACT001.LOG.isTraceEnabled()) {
			ModuloACT001.LOG.trace("Fin de execute en clase ModuloACT001");
		}
		
		return resultado;
	}
	
	/**
	 * El módulo ACT001 determina el cálculo de los factores de actualización financiera para la garantía  
	 * en un periodo dado así como el Flujo Actualizado de dicho periodo.
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
	private BigDecimal moduloACT001(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		BigDecimal act001 = BigDecimal.ZERO;
		String varCriterFec = ConstantsFunciones.CTE_CADENA_VACIA;
		Timestamp varffincas;
		Timestamp varffincas1;
		Timestamp varffincas2;
		Timestamp varFecha = null;
		BigDecimal varI1 = BigDecimal.ZERO;
		BigDecimal varI2 = BigDecimal.ZERO;
		BigDecimal varI3 = BigDecimal.ZERO;
		BigDecimal varAnoJ = BigDecimal.ZERO;
		BigDecimal varAnoffincas = BigDecimal.ZERO;
		BigDecimal varAnoffincas1 = BigDecimal.ZERO;
		BigDecimal varAnoffincas2 = BigDecimal.ZERO;
		BigDecimal atcfinJ = BigDecimal.ZERO;
		BigDecimal Datos1 = BigDecimal.ZERO;
		BigDecimal Datos2 = BigDecimal.ZERO;
		BigDecimal Datos3 = BigDecimal.ZERO;
		//Fin variables locales
		
		if (ModuloACT001.LOG.isTraceEnabled()) {
			ModuloACT001.LOG.trace("Inicio función << moduloACT001 >> de la clase ModuloACT001, para la iteracion = {}", iteracion);
		}
			
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		/**
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo,
		 * así como las variables internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso 
		 * por los siguientes periodos a calcular. 
				Variables de Apoyo
				-	VarCriterFec --> obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
				-	Si la variable de apoyo  retornada es nulo se devuelve error funcional 005 - No se ha encontrado la Variable de Apoyo ID-TEMPORAL, 
				finalizando el proceso para la UMIC.
				Variables Módulo
					-	varI1 = btcUmic.Itcalc1
					-	varI2 = btcUmic.Itcalc2
					-	Si  varI2 es  no nulo y  > 0  --> varffincas = fecIniTramo2 
					-	Si  varI2 es  nulo  ó  0  -->   varffincas = fecfinTramo1 
				Para cualquier periodo j:
					-	Si codSubproceso = "PROY_PRV":
						varFecha = proyUmic(j).fecDesde
					-	Si codSubproceso <> "PROY_PRV":
						varFecha = proyUmic(j).varBloque.fecPago
				Se calculará la actualización financiera como: 
				Si  varI2 es  nulo  ó  0 se calculará  AtcfinJ como: 
					-	varAnoJ = nannos(fcalc, varFecha, VarCriterFec)
					actfin =(1+vari1/100)^ -(varAnoJ)
				Si  varI2 es  no nulo y  > 0:  
					-	Si fcalc <= varffincas entonces:
							o	Si varFecha <= varffincas entonces:
								varAnoJ = nannos(fcalc, varFecha, VarCriterFec);
								actfin =(1+vari1/100)^ -(varAnoJ)
							o	Si varFecha > varffincas entonces:
								varAnoJ = nannos(varffincas, varFecha, VarCriterFec);
								varAnoffincas= nannos(varffincas, fcalc, VarCriterFec);
				            	actfin =(1+vari1/100)^ -(varAnoffincas) * (1 + vari2/100)^ -(varAnoJ)
					-	Si fcalc > varffincas entonces:
							varAnoJ = nannos(fcalc, varFecha, VarCriterFec);
 							actfin =  (1 + vari2/100)^ -(varAnoJ)
		 */	
		// Cálculo y validación de las variables de apoyo.
		varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);

		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFec);
		}// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		// Fin de la validación de las variables de apoyo.
		
		varI1 = btcUmic.getItcalc().get(0);
		varI2 = btcUmic.getItcalc().get(1);
		varI3 = btcUmic.getItcalc().get(2);
		varffincas1 = btcUmic.getFecfintramo().get(0);
		varffincas2 = btcUmic.getFecfintramo().get(1);
		
		/**
		 * -	Si codSubproceso = "PROY_PRV":
		 * 		varFecha = proyUmic(j).fecDesde
		 * -	Si codSubproceso <> "PROY_PRV":
		 * 		varFecha = proyUmic(j).varBloque.fecPago
		 */
		if (ConstantsModulos.CTE_PROY_PRV.equals(codSubproceso)) {
			varFecha = proyUmic.get(iteracion - 1).getFechaDesde();
		} else {
			varFecha = bloqueCorriente.getFechaPago();
		}
		
		
		
		if (null == varFecha) {
			atcfinJ = BigDecimal.ZERO;
		} else {			
			//varAnoJ <-- nannos(fcalc, varFecha, VarCriterFec)
			varAnoJ = FuncionesAuxiliares.nAnnos(fcalc, varFecha, varCriterFec);
			
			if (null == varI2 || varI2.equals(BigDecimal.ZERO)) {
				//varffincas = btcUmic.getFecfintramo().get(ConstantsFunciones.CTE_0);
				
				//Si  varI2 es  nulo  ó  0 se calculará  AtcfinJ como:
				//atcfinJ <--(1 + varI1/100) ^ -varAnoJ
				atcfinJ = Util.pow(BigDecimal.ONE.add(varI1.multiply(CTE_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT)), varAnoJ.negate());
				
				//Si  varI2 es  no nulo y  > 0 y varI3 es  nulo
			} else if(null != varI2 && varI2 != BigDecimal.ZERO && (null == varI3 || varI3.equals(BigDecimal.ZERO))){
				
				//Si fcalc <= varffincas1 
				if (!fcalc.after(varffincas1)){
					
					//o	Si varFecha <=  varffincas1
					if(!varFecha.after(varffincas1)){
						
						varAnoffincas1 = FuncionesAuxiliares.nAnnos(fcalc, varFecha, varCriterFec);
						atcfinJ = Util.pow(BigDecimal.ONE.add(varI1.multiply(CTE_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT)), varAnoffincas1.negate());
						
					} else {
						varAnoffincas1 = FuncionesAuxiliares.nAnnos(varffincas1, fcalc, varCriterFec);
						varAnoffincas2 = FuncionesAuxiliares.nAnnos(varffincas1, varFecha, varCriterFec);

						Datos1 = Util.pow(BigDecimal.ONE.add(varI1.multiply(CTE_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT)), varAnoffincas1.negate());
						Datos2 = Util.pow(BigDecimal.ONE.add(varI2.multiply(CTE_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT)), varAnoffincas2.negate());
						
						atcfinJ =  Datos1.multiply(Datos2);
					}
				}else{
					
					varAnoffincas2 = FuncionesAuxiliares.nAnnos(fcalc, varFecha, varCriterFec);
					atcfinJ = Util.pow(BigDecimal.ONE.add(varI2.multiply(CTE_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT)), varAnoffincas2.negate());

					
				}
				//Si  varI2 es  no nulo y  > 0  y varI3 es  no nulo y  > 0
			} else if(null != varI2 && varI2 != BigDecimal.ZERO && null != varI3 && varI3 != BigDecimal.ZERO){
				
				//Si varFecha <=  varffincas1
				if (!varFecha.after(varffincas1)){
					
					varAnoJ = FuncionesAuxiliares.nAnnos(fcalc, varFecha, varCriterFec);
					atcfinJ = Util.pow(BigDecimal.ONE.add(varI1.multiply(CTE_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT)), varAnoJ.negate());
				
				//Si  varffincas1 < varFecha  <= varffincas2
				} else if( varffincas1.before(varFecha) && !varFecha.after(varffincas2)){
					
					if (!fcalc.after(varffincas1)){
					
					varAnoJ = FuncionesAuxiliares.nAnnos(varffincas1, varFecha, varCriterFec);
					varAnoffincas1 = FuncionesAuxiliares.nAnnos(fcalc, varffincas1, varCriterFec);
					
					Datos1 = Util.pow(BigDecimal.ONE.add(varI1.multiply(CTE_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT)), varAnoffincas1.negate());
					Datos2 = Util.pow(BigDecimal.ONE.add(varI2.multiply(CTE_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT)), varAnoJ.negate());
					
					atcfinJ =  Datos1.multiply(Datos2);
					
					}else{
						
						varAnoJ = FuncionesAuxiliares.nAnnos(fcalc, varFecha, varCriterFec);
						Datos2 = Util.pow(BigDecimal.ONE.add(varI2.multiply(CTE_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT)), varAnoJ.negate());

						atcfinJ = Datos2;
						
					}
					
				//Si varFecha > varffincas2 
				} else if(varFecha.after(varffincas2)){
					
					if (!fcalc.after(varffincas1)){
						
						varAnoJ = FuncionesAuxiliares.nAnnos(varffincas2, varFecha, varCriterFec);
						varAnoffincas1 = FuncionesAuxiliares.nAnnos(fcalc, varffincas1, varCriterFec);
						varAnoffincas2 = FuncionesAuxiliares.nAnnos(varffincas1, varffincas2, varCriterFec);
					
						Datos1 = Util.pow(BigDecimal.ONE.add(varI1.multiply(CTE_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT)), varAnoffincas1.negate());
						Datos2 = Util.pow(BigDecimal.ONE.add(varI2.multiply(CTE_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT)), varAnoffincas2.negate());
						Datos3 = Util.pow(BigDecimal.ONE.add(varI3.multiply(CTE_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT)), varAnoJ.negate());
					
						atcfinJ = Datos1.multiply(Datos2).multiply(Datos3);
						
					}else if(varffincas1.before(fcalc) && !fcalc.after(varffincas2)){
						
						varAnoJ = FuncionesAuxiliares.nAnnos(varffincas2, varFecha, varCriterFec);
						varAnoffincas2 = FuncionesAuxiliares.nAnnos(fcalc, varffincas2, varCriterFec);
					
						Datos2 = Util.pow(BigDecimal.ONE.add(varI2.multiply(CTE_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT)), varAnoffincas2.negate());
						Datos3 = Util.pow(BigDecimal.ONE.add(varI3.multiply(CTE_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT)), varAnoJ.negate());
					
						atcfinJ = Datos2.multiply(Datos3);
						
					}else{
						
						varAnoJ = FuncionesAuxiliares.nAnnos(fcalc, varFecha, varCriterFec);
					
						Datos3 = Util.pow(BigDecimal.ONE.add(varI3.multiply(CTE_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT)), varAnoJ.negate());
					
						atcfinJ = Datos3;
					
					}
				}
			}
		}		
		act001 = atcfinJ;
		
		if (ModuloACT001.LOG.isTraceEnabled()) {
			ModuloACT001.LOG.trace("Fin función << moduloACT001 >> de la clase ModuloACT001, para la iteracion = {}, con resultado capitalCartera = {}", iteracion, act001);
		}
		
		return act001;
	}

}
