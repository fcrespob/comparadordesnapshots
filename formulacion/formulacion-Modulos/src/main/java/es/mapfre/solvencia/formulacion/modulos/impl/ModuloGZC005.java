/**MODIFICACION: TAR00433819
  FECHA: 27/09/2018
  DESCRIP: Se modifica la forma de recuperar la lista de proyecciones, ya que estaba recogiendo una proyeccion menos.
 */
package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo GZC005.
 * Clase encargada del cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic
 * La expresión matemática para su determinación es la siguiente:
 * 				GZC(005, tcm, beta) = gic/100   * Renta(Tmc, beta) +   Gipc/100 * PRIMA/(1 + vrta(0,0,99,i2,99,i2,x,tm,12,0))
 * @author agonzalezgar
 *
 */
public class ModuloGZC005 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZC005.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_GZC005;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_FACTOR_VRTA = ConstantsModulos.CTE_VAR_FACTOR_VRTA.concat(CLAVE_MODULO);
	
	private static final String CLAVE_LST_PROY = ConstantsModulos.CTE_LST_PROY.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_GIC_DIV_100 = ConstantsModulos.CTE_VAR_GIC_DIV_100;
	private static final String CLAVE_VAR_GIC_DIV_100_MOD = CLAVE_VAR_GIC_DIV_100.concat(CLAVE_MODULO);
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
			if (ModuloGZC005.LOG.isTraceEnabled()) {
				ModuloGZC005.LOG.trace("Inicio de execute en clase ModuloGZC005");
			}
			
			//Inicio recuperación de datos que le pasaremos a la función moduloGZC005
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			//Fin de la recuperación de los datos que se pasarán a la función moduloGZC005.
			
			//Invocamos a la función de calculo moduloGZC005
			resultado = moduloGZC005(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloGZC005.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZC005.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloGZC005.LOG.isTraceEnabled()) {
			ModuloGZC005.LOG.trace("Fin de execute en clase ModuloGZC005");
		}
		
		return resultado;
	}
	/**
	 * Modulo de cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic
	 * La expresión matemática para su determinación es la siguiente:
	 * 				GZC(005, tcm, beta) = gic/100   * Renta(Tmc, beta) +   Gipc/100 * PRIMA/(1 + vrta(0,0,99,i2,99,i2,x,tm,12,0))
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
	private BigDecimal moduloGZC005(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal gzc005 = BigDecimal.ZERO;
		String varCritEdad;
		String varCriFec;
		BigDecimal varGic;
		BigDecimal varGicDiv100 = BigDecimal.ZERO;
		BigDecimal varFactorVrta = BigDecimal.ZERO;
		List<DetalleCorriente> lstProyeccion;
		final DatosGenerales datosGen;
		//Fin variables locales
				
		if (ModuloGZC005.LOG.isTraceEnabled()) {
			ModuloGZC005.LOG.trace("Inicio función << moduloGZC005 >> de la clase ModuloGZC005, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if(bloqueCorriente.getFechaDevengo() == null) {
			return gzc005;
		}
		
		/**
		 	En todos los periodos se ejecutan las mismas operaciones y métodos, sin embargo, cabe hacer la siguiente distinción:
		 		- Si estoy en el primer periodo (j=1) se calcula el valor de las variables auxiliares que no varían a lo largo de los periodos.
		 		- En el resto de iteraciones recupero esos valores sin tener que calcularlos de nuevo (aunque se llamen a las mismas funciones, ellas gestionan cuando deben recuperar o recuperar + calcular).
		 	Ejecución del módulo:
				Variables de Apoyo:
					-	VarCriterEdad --> obtenerConfiguracion.recuperarVariableApoyo(ID-CRITERIO)
					-	VarCriterFec --> obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
					-	Si alguna de las variables de apoyo  retornadas es nulo se devuelve error funcional 005 - 
							No se ha encontrado la Variable de Apoyo &NombreVariableApoyo, finalizando el proceso para la UMIC.
				Variables Módulo
					-	varPrima = umic.primas.iprimatarada
					-	varGipc = btcUmic.gtoRosspPrima
					-	varGic = btcUmic.gtoRosspCap
					-	varTm = btcUmic.tabla1Aseg1
				Se calcularán las distintas variables internas necesarias para el cálculo, dependientes del periodo j a calcular, como se describe a continuación: 
					-	varEdad = entero(nedad(umic.fechas.fecinisus, umic.asegurados.fnacAseg1, VarCriterEdad))
					-	varAnoNac = Año (umic.asegurados.fnacAseg1)
					-	varTabMort = btcUmic.tabla1Aseg1 --> Dejo la variable disponible en memoria para el subproceso de la umic.
					-	varValoresTabMort = obtenerConfiguracion. recuperarValoresExperiencia(btcUmic.fecCierre, varTabMort, varAnoNac, btcUmic. itcalc1, umic.baseTecIni. psobremort, umic.baseTecIni. priesgo)
				Para cualquier periodo j se calculará el capital en el periodo como se describe a continuación: 
					-	varI2 = btcUmic.Itcalc2
				
				-	varVrta = Vrta(0,0,99, varI2,99, varI2, VarEdad, varTm,12,0)
		 */
		// Cálculo y validación de las variables de apoyo.  
		datosGen = umic.getDatosGenerales();
		varCritEdad = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIEDAD, datosGen.getCcartera(), datosGen.getKmodalidad(), datosGen.getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, datosGen.getCcartera(), datosGen.getKmodalidad(), datosGen.getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCritEdad);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
			// Fin de la validación de las variables de apoyo.
		}
		
		// Calculo de varFactorVrta. Dentro se calcula solo una vez varEdad, varTabMort, varVrta, varGipcDiv100 y unoMasPrimaDivVrta
		varFactorVrta = UtilModulos.getVarFactorVrta(mapVariables, CLAVE_VAR_FACTOR_VRTA, CLAVE_VAR_VAL_TAB_MORT, btcUmic, umic, varCritEdad);
		
		// Si varGic = 0 --> gzc005 = varFactorVrta
		varGic = btcUmic.getGtorosspCap();
		if (varGic.equals(BigDecimal.ZERO)) {
			gzc005 = varFactorVrta;
		} else {
			lstProyeccion = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PROY, btcUmic.getBaseTec(), btcUmic.getFecCierre(), umic.getKey());
			
			//GZC005(j) = (varGic/100) * proyUmic(j).corrienteVida.impFlujoNominal + (varGipc/100) * (varFactorVrta))
			// Se usa (iteración -1) porque en el subproceso la primera iteración vale UNO y no CERO.
			varGicDiv100 = UtilModulos.getVarGicDiv100(mapVariables, ModuloGZC005.CLAVE_VAR_GIC_DIV_100_MOD, varGic);
//INI-TAR00433819
//			gzc005 = varGicDiv100.multiply(lstProyeccion.get(iteracion - 1).getBloqueVida().getImpFlujoNominal()).add(varFactorVrta);
			gzc005 = varGicDiv100.multiply(proyUmic.get(iteracion - 1).getBloqueVida().getImpFlujoNominal()).add(varFactorVrta);
//FIN-TAR00433819
		}
		
		if (ModuloGZC005.LOG.isTraceEnabled()) {
			ModuloGZC005.LOG.trace("Fin función << moduloGZC005 >> de la clase ModuloGZC005, para la iteracion = {}, con resultado gzc005 = {}", iteracion, gzc005);
		}
		
		return gzc005;
	}
}
