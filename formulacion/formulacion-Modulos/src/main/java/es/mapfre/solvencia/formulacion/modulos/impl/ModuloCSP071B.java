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
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * 
 * @author Ignacio J Del Pozo
 *
 */
public class ModuloCSP071B implements Modulo {
		// Inicio de las variables estáticas para agilizar operaciones.
		private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP071B.class);
		private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP071B;
		private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
		private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

		private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
		private static final String CLAVE_VARL = ConstantsModulos.CTE_VARL.concat(CLAVE_MODULO);
	    private static final String CLAVE_VAR_M_FORPAGRENT = ConstantsModulos.CTE_VAR_M_FORPAGRENT.concat(CLAVE_MODULO);
	    private static final String CLAVE_VAR_D = ConstantsModulos.CTE_VAR_D.concat(CLAVE_MODULO);
		// Fin de las variables estáticas usadas para agilizar operaciones.
		
		@Override
		public String getNombreServicio() {
			return CLAVE_MODULO;
		}
		@Override
		public Object execute(Object... args) throws Solvencia2Excepcion {
			//Variables locales
			BigDecimal resultado = BigDecimal.ZERO;
			
			//Fin variables locales
			
			try {
				if (ModuloCSP071B.LOG.isTraceEnabled()) {
					ModuloCSP071B.LOG.trace("Inicio de execute en clase ModuloCSP071B");
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
				//Fin de la recuperación de los datos que se pasarán a la función moduloCSP071.
				
				//Invocamos a la función de calculo CSP071
				resultado = moduloCSP071B(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,codSubproceso);
				
			} catch (Solvencia2Excepcion e) {
				ModuloCSP071B.LOG.error(e.getIncidencia().getTextoError(), e);
				throw e;
			} catch (Exception e) {
				ModuloCSP071B.LOG.error(e.getMessage(), e);
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
			}
			
			if (ModuloCSP071B.LOG.isTraceEnabled()) {
				ModuloCSP071B.LOG.trace("Fin de execute en clase ModuloCSP071B");
			}
			return resultado;
		}
		/**
		 *	Usaremos este módulo para el cálculo de la cuantía de la renta en cada momento según forma de pago.
		 * 
		 * @param proyUmic
		 * 			Estructura detalleCorrientes de la umic
		 * @param iteracion
		 * 			Indica el periodo de proyección J que se está calculando de entre todos los periodos de proyección de la umic (proyUmic)
		 * @param fcalc
		 * 			Fecha de Cálculo 
		 * @param umic
		 * 			Contiene los datos de la Umic que se está procesando.
		 * @param btcUmic
		 * 			Contiene el detalle de la base técnica de cálculo para la umic.
		 * @param codSubproceso 
		 * 			Código el subproceso que se está ejecutando.
		 */
		
		
		private BigDecimal moduloCSP071B(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente,
				final int iteracion, final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic,
				final Map<String, Object> mapVariables, String codSubproceso) {
			//Inicio de las variables locales
			BigDecimal csp071B = BigDecimal.ZERO;
			String varCriterFecha = ConstantsFunciones.CTE_CADENA_VACIA;
			BigDecimal varRenta0 = BigDecimal.ZERO;
			Integer varMForpagrent = 0;
			Timestamp varFechaEfecto;
			BigDecimal varPrr = BigDecimal.ZERO;
			Integer varL = 0;
			BigDecimal	varGipc = BigDecimal.ZERO;
			BigDecimal varPrima = BigDecimal.ZERO;
			BigDecimal varGic = BigDecimal.ZERO;
			Integer	varD ;
			BloqueCorriente varBloque;
			BigDecimal varImpRenta = BigDecimal.ZERO;
			Integer varTCm = 0;
			String sumatorio;
			Integer varBeta = 0;
			Integer varPP = 0;
//816517-INI
			final IObtenerConfiguracion obtenerConf = FachadaServicios.getObtenerConfiguracion();
			CriterioFechas criterioFechas = null;
			String criterioFecPago = ConstantsFunciones.CTE_CADENA_VACIA;
			String criterioFecDev = ConstantsFunciones.CTE_CADENA_VACIA;
//816517-FIN	
			//fin de las variables locales
			
			if (ModuloCSP071B.LOG.isTraceEnabled()) {
				ModuloCSP071B.LOG.trace("Inicio función << moduloCSP071B >> de la clase ModuloCSP071B, para la iteracion = {}", iteracion);
			}
			
			//Validamos los parametros de entrada
			ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

			
			// Cálculo y validación de las variables de apoyo.  
			varCriterFecha = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);	
			
			// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
			if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
				// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
				ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFecha);
				// Fin de la validación de las variables de apoyo.
			}
			
			/**
			 *Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no
			 *varían por periodo, así como las varibales internas que tampoco varían por periodo,
			 *y que se dejarán accesibles para su uso en el subproceso por los siguientes periodos a calcular. 
				
				Variables de Apoyo
				
					VarCriterFec obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
				
					Si alguna de las variables de apoyo  retornadas es nula se devuelve error funcional 005 - 
				No se ha encontrado la Variable de Apoyo &NombreVariableApoyo, finalizando el proceso para la UMIC.
				
				Variables Módulo
				
					Si umic.datosGenerales.cnegocio = ‘I’
				o	varfechaEfecto = umic.fechas.fecefecIni
					En caso contrario:
				o	varfechaEfecto = umic.fechas fecinisus
				
					varRenta0 = umic.rentas.rentini
				Si  forpagrent es no nulo ó forpagrent <> 0
					varM = umic.rentas.forpagrent 
					Si  forpagrent es nulo ó forpagrent =  0
				o	Si umic.rentas. cpagrenta = 1
					varM = 1
				o	Si umic.rentas. cpagrenta = 2
					varM = 2
				o	Si umic.rentas. cpagrenta = 3
					varM = 4
				o	Si umic.rentas. cpagrenta = 4
					varM = 12
					varPrr = umic.rentas.prevrenta
					varPrima = umic.primas.iprimatarada
					varGipc =  btcUmic.gtoRosspPrima
					varGic =btcUmic.gtoRosspCap 
					varD = ndias(umic.fechas.fecinisus, umic.fechas.fecefecini , VarCriterFec)
					varBloque= obtenerConfiguracion.getBloqueBySubproceso(proyUmic, codSubproceso)
				
				Se calcularán las distintas variables internas necesarias para el cálculo, dependientes del periodo j a calcular,
				como se describe a continuación: 
				
				Si estoy en el primer periodo (j=1), se establecerán las siguientes variables internas: 
				
					varTCm = TCm(varfechaEfecto, fcalc);
					varβ = 0
					varI1= btcUmic.Itcalc1:
				Para este tramo se establecerá la variable: 
				varL = ENTERO(nannos(varfechaEfecto, fecFinTramo1, VarCriterFec));
				
					varI2= btcUmic.Itcalc2
			 * @param codSubproceso 

			 */
			
			// Variables Modulo
			if (umic.getDatosGenerales().getCnegocio().equals(ConstantsModulos.CTE_NEGOCIO_INDIVIDUAL)){
				varFechaEfecto=umic.getFechas().getFecefecini();
			}else{
				varFechaEfecto=umic.getFechas().getFecinisus();
			}
			
			
//816517-INI
			//Recuperamos el criterio de las fechas
			criterioFechas = obtenerConf.recuperarCriterioFechas(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), 
					umic.getDatosGenerales().getKprestacion(), umic.getDatosAdicionales().getPrestCal(), codSubproceso);
		
			//Validamos el criterio de la fecha de pago y la fecha devengo
			criterioFecPago = criterioFechas.getFecPago();
			criterioFecDev = criterioFechas.getFecDevengo();


//			- Si criterioFechas.FecPago y/ó criterioFechas.FecDevengo es igual a ‘EFTEC’
//			Se establecerá varfecini como la fecha de Efecto Técnico, es decir, como el fin de mes del mes de efecto de la UMIC (fechasUmic.fecinisus)
			
			if (ConstantsModulos.CTE_VAL_EFTEC.equals(criterioFecPago) || ConstantsModulos.CTE_VAL_EFTEC.equals(criterioFecDev) ||
				ConstantsModulos.CTE_VAL_EFTIN.equals(criterioFecPago) || ConstantsModulos.CTE_VAL_EFTIN.equals(criterioFecDev)) {
				varFechaEfecto = UtilFechas.getUltimoDiaDelMes(varFechaEfecto);
			}
			
//816517-FIN	
			varRenta0=umic.getRentas().getRentini();
			varMForpagrent = UtilModulos.getVarMForpagrent(mapVariables, CLAVE_VAR_M_FORPAGRENT, umic);
	
			varPrr=umic.getRentas().getPrevrenta();
			varPrima=umic.getPrimas().getIprimatarada();
			varGipc=btcUmic.getGtorosspPrima();
			varGic=btcUmic.getGtorosspCap();
			varD=UtilModulos.getVarD(mapVariables, CLAVE_VAR_D, umic.getFechas().getFecinisus(), umic.getFechas().getFecefecini(),varCriterFecha ); 

//INI-816517: Se sustituye calculo de TCm por el TCmIni.
//            si venimos desde el csp462, y estamos calculando el sumatorio de las rentas pagadas hasta la fecha de devengo, debemos hacerlo con tcm=0			
			sumatorio = (String) mapVariables.get("SUMATORIO");
 			if (!(sumatorio == ConstantsFunciones.CTE_CADENA_VACIA )) {
// 				varTCm = UtilModulos.getVarTCm(mapVariables, CLAVE_VAR_TCM, ConstantsModulos.CTE_FIRST_ITER, varFechaEfecto, fcalc);
 					if (ConstantsModulos.CTE_VAL_EFTEC.equals(criterioFecPago) || ConstantsModulos.CTE_VAL_EFTEC.equals(criterioFecDev)||
 						ConstantsModulos.CTE_VAL_EFTIN.equals(criterioFecPago) || ConstantsModulos.CTE_VAL_EFTIN.equals(criterioFecDev)) {
 						varTCm = UtilModulos.getVarTCm(mapVariables, CLAVE_VAR_TCM, ConstantsModulos.CTE_FIRST_ITER, varFechaEfecto, fcalc);
 				}    
 				else {
 						varTCm = UtilModulos.getVarTCmIni(mapVariables, CLAVE_VAR_TCM, ConstantsModulos.CTE_FIRST_ITER, varFechaEfecto, fcalc);
 				}    					
 			}
 			varBeta = iteracion - 1;	
//FIN-816517
			varL = UtilModulos.getVarL(mapVariables, CLAVE_VARL, ConstantsModulos.CTE_FIRST_ITER, varFechaEfecto, umic.getBti().getFecFinTramo1(), varCriterFecha);
			
			
			if (null != bloqueCorriente.getFechaDevengo()){
				
				varPP = (varTCm + varBeta) % (ConstantsFunciones.CTE_12 / varMForpagrent);
				
				
				if (varMForpagrent == ConstantsFunciones.CTE_0) {
					varImpRenta = BigDecimal.ZERO;
				} else if (varMForpagrent > 0) {
					
					varPP = (varTCm + varBeta) % (ConstantsFunciones.CTE_12 / varMForpagrent);
					
					if (varPP == 0) {
						if (!varPrr.equals(BigDecimal.ZERO)) {
							varImpRenta = calcularVarImporteRentaCasoVarTmcMayorCeroYPPIgualCero(varRenta0, varPrr, varTCm, varBeta);
						} else {
							// Si varPrr = 0 -> varImpRenta = varRenta0
							varImpRenta = varRenta0;
						}
					} else {
						varImpRenta = BigDecimal.ZERO;
					}
				}
				
				
				csp071B = varImpRenta;
					
			}

			if (ModuloCSP071B.LOG.isTraceEnabled()) {
				ModuloCSP071B.LOG.trace("Fin función << moduloCSP071 >> de la clase ModuloCSP071B, para la iteracion = {}, con resultado csp071 = {}", iteracion, csp071B);
			}
			
			return csp071B;
		}
			/**
	 * Función encargada de realizar el siguiente calculo (perteneciente al modulo CSP071B):
	 * 		varImpRenta= varRenta0 * ((1+varPrr/100))^(ENTERO((varTCm+ varβ -1)/12))
	 * Cuando varTCm > 0, varPP = 0 y varPrr = 0
	 * 
	 * @param varRenta0 varRenta0 = umic.rentas.rentini
	 * @param varTCm  varTCm = TCm(umic.fechas.fecinisus, fcalc)	
	 * @param varBeta varβ = TCM(fcalc, proyUmic(j).varBloque.fecDevengo)
	 * @return varImpRenta
	 */
	private BigDecimal calcularVarImporteRentaCasoVarTmcMayorCeroYPPIgualCero(final BigDecimal varRenta0, final BigDecimal varPrr, final Integer varTCm, 
			final Integer varBeta) {
		//Variables locales
		BigDecimal base;
		BigDecimal varImpRenta = BigDecimal.ZERO;
		int oper1;
		int elevado;
		//Fin variables locales
		
		if (ModuloCSP071B.LOG.isTraceEnabled()) {
			ModuloCSP071B.LOG.trace("Inicio de la función << calcularVarImporteRentaCasoVarTmcMayorCeroYPPIgualCero >> de la clase ModuloCSP071B");
		}
		
		//base <--(1+varPrr/100)
		base = BigDecimal.ONE.add(varPrr.divide(ConstantsFunciones.CTE_OPER_100, ConstantsFunciones.MATH_CONTEXT));
		//oper1 <-- varTCm +ß-1
		oper1 = (varTCm + varBeta - 1);
		elevado = oper1 / ConstantsFunciones.CTE_12;
		//varImpRenta <-- varRenta0 * base^elevado
		varImpRenta = varRenta0.multiply(Util.pow(base, elevado));
		
		if (ModuloCSP071B.LOG.isTraceEnabled()) {
			ModuloCSP071B.LOG.trace("Fin de la función << calcularVarImporteRentaCasoVarTmcMayorCeroYPPIgualCero >> de la clase ModuloCSP071B");
		}
		
		return varImpRenta;
	}

}
