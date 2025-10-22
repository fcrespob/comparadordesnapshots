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
import es.mapfre.solvencia.dominio.maestro.Rentas;
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
 *  Clase encargada del cálculo que devuelve en cada momento según forma de pago de la Umic.
 * @author agonzalezgar
 *
 */
public class ModuloCSP071 implements Modulo {

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP071.class);
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP071;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCMINI = ConstantsModulos.CTE_VAR_TCMINI.concat(CLAVE_MODULO);
	private static final String CLAVE_VARL = ConstantsModulos.CTE_VARL.concat(CLAVE_MODULO);
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
			if (ModuloCSP071.LOG.isTraceEnabled()) {
				ModuloCSP071.LOG.trace("Inicio de execute en clase ModuloCSP071");
			}
			
			//Inicio recuperación de datos que le pasaremos a la función moduloCSP071
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
//816517-INI
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
//816517-FIN		
			//Fin de la recuperación de los datos que se pasarán a la función moduloCSP071.
			
			//Invocamos a la función de calculo CSP071
//816517-INI
			//resultado = moduloCSP071(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			  resultado = moduloCSP071(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,codSubproceso);
//816517-FIN
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP071.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP071.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP071.LOG.isTraceEnabled()) {
			ModuloCSP071.LOG.trace("Fin de execute en clase ModuloCSP071");
		}
		
		return resultado;

	}

	/** 
	 * Modulo de cálculo que devuelve en cada momento según forma de pago de la Umic.
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
//816517-INI
//	    private BigDecimal moduloCSP071(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
//		  		final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		private BigDecimal moduloCSP071(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
				final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables,final String codSubproceso) {
//816517-FIN		
			//Variables locales
		String varCriterFecha = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal varRenta0 = BigDecimal.ZERO;
		int varM = 0;
		BigDecimal varPrr = BigDecimal.ZERO;
		Integer varL = 0;
		int	varTCm = 0;
		int	varTCmini = 0;
		int varBeta = 0;
		int varPP = 0;
		String sumatorio;
		BigDecimal varImpRenta = BigDecimal.ZERO;
		Timestamp varFechaEfecto;
		BigDecimal csp071;
//816517-INI
		final IObtenerConfiguracion obtenerConf = FachadaServicios.getObtenerConfiguracion();
		CriterioFechas criterioFechas = null;
		String criterioFecPago = ConstantsFunciones.CTE_CADENA_VACIA;
		String criterioFecDev = ConstantsFunciones.CTE_CADENA_VACIA;
//816517-FIN		
	    //Fin variables locales
		
		if (ModuloCSP071.LOG.isTraceEnabled()) {
			ModuloCSP071.LOG.trace("Inicio función << moduloCSP071 >> de la clase ModuloCSP071, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if ( null == bloqueCorriente.getFechaDevengo() ) {
			return BigDecimal.ZERO;
		}
		/**
		 * 	La forma de cálculo no varía de periodo a periodo aunque hay algunas cosas a tener en cuenta:
		 * 	-	En el primer periodo se calcularán los valores de ciertas variables auxiliares que no variarán durante los periodos.
		 * 	-	En el resto de periodos se recuperan los valores de estas variables auxiliares.
		 * 	-	Las funciones que trabajan con las variables auxiliares son las que gestionan cuando se "devuelve únicamente" y cuando se "calcula y se devuelve".
		 * 	Variables de Apoyo
		 * 	-	VarCriterFec --> obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
		 * 	-	Si alguna de las variables de apoyo  retornadas es nulo se devuelve error funcional 005 - 
		 * 			No se ha encontrado la Variable de Apoyo &NombreVariableApoyo, finalizando el proceso para la UMIC.
		 * 	Variables Módulo
		 * 	-	varRenta0 = umic.rentas.rentini
		 * 	Si forpagrent es no nulo ó forpagrent <> 0
		 * 		-	varM = umic.rentas.forpagrent
		 * 	-	Si forpagrent es nulo ó forpagrent = 0
		 * 		-	Si umic.rentas.forpagrent = 1
		 * 				varM = 1
		 * 		-	Si umic.rentas.forpagrent = 2
		 * 				varM = 2
		 * 		-	Si umic.rentas.forpagrent = 3
		 * 				varM = 4
		 * 		-	Si umic.rentas.forpagrent  = 4
		 * 				varM = 12
		 * 	-	varPrr = umic.rentas.prevrenta
		 * 	-	varPrima = umic.primas.iprimatarada
		 * 	-	varGipc =  btcUmic.gtoRosspPrima
		 * 	-	varGic = btcUmic.gtoRosspCap
		 * 	-	varD = ndias(umic.fechas.fecinisus, umic.fechas.fecefecini , VarCriterFec)
		 * 	Se calcularán las distintas variables internas necesarias para el cálculo tal y como se describe a continuación: 
		 */
		
		// Cálculo y validación de las variables de apoyo.  
		//Recuperamos el criterio de las fechas
		varCriterFecha = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);	


		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFecha);
			// Fin de la validación de las variables de apoyo.
		}
		

		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
//816517-INI
		//Recuperamos el criterio de las fechas
		criterioFechas = obtenerConf.recuperarCriterioFechas(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), 
				umic.getDatosGenerales().getKprestacion(), umic.getDatosAdicionales().getPrestCal(), codSubproceso);
	
		//Validamos el criterio de la fecha de pago y la fecha devengo
		criterioFecPago = criterioFechas.getFecPago();
		criterioFecDev = criterioFechas.getFecDevengo();


//		- Si criterioFechas.FecPago y/ó criterioFechas.FecDevengo es igual a ‘EFTEC’
//		Se establecerá varfecini como la fecha de Efecto Técnico, es decir, como el fin de mes del mes de efecto de la UMIC (fechasUmic.fecinisus)
		
		if (ConstantsModulos.CTE_VAL_EFTEC.equals(criterioFecPago) || ConstantsModulos.CTE_VAL_EFTEC.equals(criterioFecDev) ||
			ConstantsModulos.CTE_VAL_EFTIN.equals(criterioFecPago) || ConstantsModulos.CTE_VAL_EFTIN.equals(criterioFecDev)) {
			varFechaEfecto = UtilFechas.getUltimoDiaDelMes(varFechaEfecto);
		}
		
//816517-FIN	
		
		varRenta0 = umic.getRentas().getRentini();
		varPrr = umic.getRentas().getPrevrenta();
		varM = obtenerVarM(umic.getRentas());
	
		/**
		 * Si estoy en el primer periodo (j=1), se establecerán las siguientes variables internas: 
		 * 	-	varTCm = TCm(umic.fechas.fecinisus, fcalc)
		 * 	- 	varI1= btcUmic.Itcalc1:
		 * 			Para este tramo se establecerá la variable: 
		 * 			varL = ENTERO(nannos(umic.fechas.fecinisus, fecFinTramo1, VarCriterFec))
		 * 	-	varI2= btcUmic.Itcalc2
		 * Para cualquier periodo J se calculará:
		 * 	-	varβ = TCM(fcalc, proyUmic(j).varBloque.fecDevengo)
		 * 	-	varPP = MOD(varTCm + varβ -1,12/ varM) 
		 */
		
		// varTCm = TCm(umic.fechas.fecinisus, fcalc)

//816517-INI	
		if (ConstantsModulos.CTE_VAL_EFTEC.equals(criterioFecPago) || ConstantsModulos.CTE_VAL_EFTEC.equals(criterioFecDev) ||
				ConstantsModulos.CTE_VAL_EFTIN.equals(criterioFecPago) || ConstantsModulos.CTE_VAL_EFTIN.equals(criterioFecDev)) {
				varTCm = UtilModulos.getVarTCm(mapVariables, CLAVE_VAR_TCM, ConstantsModulos.CTE_FIRST_ITER, varFechaEfecto, fcalc);
		}    
		else {
				varTCm = UtilModulos.getVarTCmIni(mapVariables, CLAVE_VAR_TCM, ConstantsModulos.CTE_FIRST_ITER, varFechaEfecto, fcalc);
		}    
//816517-FIN		
		varBeta = iteracion - 1;


		varL = UtilModulos.getVarL(mapVariables, CLAVE_VARL, ConstantsModulos.CTE_FIRST_ITER, varFechaEfecto, umic.getBti().getFecFinTramo1(), varCriterFecha);
		
		// β – Periodo mensual de proyección: Secuencial con inicio en valor 0, y final en el último periodo de proyección.

		
		/**
		 * Una vez establecidas las variables del cálculo en el periodo j, se calculará el capital en el periodo como se describe a continuación: 
			Si varTCm =   0  se cargará la variable interna varImpRenta  varImpRenta = 0.
			Si varTCm >  0  se evaluará la variable varPP.
			-	Si varPP = 0 se cargará la variable interna varImpRenta  con: 
					varImpRenta= varRenta0 * ((1+varPrr/100))^(ENTERO((varTCm+ varβ -1)/12))
					
			-	SI varPP<>0   -->  se cargará la variable interna varImpRenta  varImpRenta = 0
			SI varL<30 y VarTCm+varβ>varL*12 entonces recalculamos y sobrescribimos varImpRenta   como: 
				varImpRenta = umic.rentas.rentmini
		 */
		
		/*Una vez establecidas las variables del cálculo en el periodo j, se calculará el capital en el periodo como se describe a continuación*/
		/*varTCm >  0  se evaluará la variable varPP. Si varPP = 0 se cargará la variable interna varImpRenta  con: */
		if (varM == 0) {
			varImpRenta = BigDecimal.ZERO;
		} else if (varM > 0) {
			
			//	varPP = MOD(varTCm + varβ, 12/ varM) 
 			varPP = (varTCm + varBeta) % (ConstantsFunciones.CTE_12 / varM);

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
		
		if (varL < ConstantsFunciones.CTE_30 && (varTCm + varBeta) > (varL * ConstantsFunciones.CTE_12)) {
			varImpRenta = umic.getRentas().getRentmini();
		}

 		
		csp071 = varImpRenta;
		
		if (ModuloCSP071.LOG.isTraceEnabled()) {
			ModuloCSP071.LOG.trace("Fin función << moduloCSP071 >> de la clase ModuloCSP071, para la iteracion = {}, con resultado csp071 = {}", iteracion, csp071);
		}
		
		return csp071;
	}
	
	/**
	 * Función encargada de realizar el siguiente calculo (perteneciente al modulo CSP071):
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
		
		if (ModuloCSP071.LOG.isTraceEnabled()) {
			ModuloCSP071.LOG.trace("Inicio de la función << calcularVarImporteRentaCasoVarTmcMayorCeroYPPIgualCero >> de la clase ModuloCSP071");
		}
		
		//base <--(1+varPrr/100)
		base = BigDecimal.ONE.add(varPrr.divide(ConstantsFunciones.CTE_OPER_100, ConstantsFunciones.MATH_CONTEXT));
		//oper1 <-- varTCm +ß-1
		oper1 = (varTCm + varBeta - 1);
		elevado = oper1 / ConstantsFunciones.CTE_12;
		//varImpRenta <-- varRenta0 * base^elevado
		varImpRenta = varRenta0.multiply(Util.pow(base, elevado));
		
		if (ModuloCSP071.LOG.isTraceEnabled()) {
			ModuloCSP071.LOG.trace("Fin de la función << calcularVarImporteRentaCasoVarTmcMayorCeroYPPIgualCero >> de la clase ModuloCSP071");
		}
		
		return varImpRenta;
	}
	
	/**
	 * Establece el valor varM usado en el calculo del modulo CSP0071
	 * 
	 * @param rentasUmic Rentas de la umic tratada
	 * @return varM
	 */
	private Integer obtenerVarM(final Rentas rentasUmic) {
		//Variables locales
		int varM = 0;
		//Fin variables locales
		
		if (ModuloCSP071.LOG.isTraceEnabled()) {
			ModuloCSP071.LOG.trace("Inicio de la función << obtenerVarM >> de la clase ModuloCSP071");
		}
		
		final Integer forpagrent = rentasUmic.getForpagrent();
		
		if (null != forpagrent && !ConstantsFunciones.CTE_0.equals(forpagrent)) {
			varM = rentasUmic.getForpagrent();
		} else {
			if (rentasUmic.getCpagrenta() == null){
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AQ,  new String[]{"null"});
			}
			varM = ConstantsFunciones.FORPAGRENT.get(Integer.valueOf(rentasUmic.getCpagrenta()));
			
		}
		
		if (ModuloCSP071.LOG.isTraceEnabled()) {
			ModuloCSP071.LOG.trace("Fin de la función << obtenerVarM >> de la clase ModuloCSP071, con resultado varM = {}", varM);
		}
		
		return Integer.valueOf(varM);
	}
}
