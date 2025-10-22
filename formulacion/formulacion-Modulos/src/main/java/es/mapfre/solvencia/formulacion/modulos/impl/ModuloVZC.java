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
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo que devuelve los factores de probabilización a
 * aplicar en el cálculo de la cuantía probable de una garantía. El módulo
 * Vzc(fcal,j) determina la probabilidad de que una cabeza de edad zc en la
 * fecha de cálculo, fcal, alcance con vida la fecha j en la que se devenga la
 * prestación. La expresión matemática para su determinación es la siguiente:
 * VZC(fcal, j) = (lzc + nannos(fcal,j)) /lzc
 * 
 * @author agonzalezgar
 *
 */
public class ModuloVZC implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVZC.class);

	// Estas variables se ponen como estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VZC;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	private static final String CLAVE_VAR_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_EDAD_CAL = ConstantsModulos.CTE_EDAD_CAL.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ZC = ConstantsModulos.CTE_VAR_ZC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_LZC = ConstantsModulos.CTE_VAR_LZC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT.concat(CLAVE_MODULO);

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
		// Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		// Fin variables locales

		try {
			if (ModuloVZC.LOG.isTraceEnabled()) {
				ModuloVZC.LOG.trace("Inicio de execute en clase ModuloVZC");
			}

			// Recuperamos los datos que le pasaremos a la función moduloGZC002
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];

			// Invocamos a la función de calculo VZC
			resultado = moduloVZC(proyUmic, bloqueCorriente, iteracion, fcalc,
					umic, btcUmic, mapVariables, codSubproceso);

		} catch (Solvencia2Excepcion e) {
			ModuloVZC.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVZC.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(
					ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloVZC.LOG.isTraceEnabled()) {
			ModuloVZC.LOG.trace("Fin de execute en clase ModuloVZC");
		}

		return resultado;

	}

	/**
	 * Modulo de cálculo que devuelve los factores de probabilización a aplicar
	 * en el cálculo de la cuantía probable de una garantía. El módulo
	 * Vzc(fcal,j) determina la probabilidad de que una cabeza de edad zc en la
	 * fecha de cálculo, fcal, alcance con vida la fecha j en la que se devenga
	 * la prestación.
	 *
	 * @param proyUmic
	 *            Estructura detalleCorrientes de la umic
	 * @param bloqueCorriente
	 *            Bloque de Trabajo de la corriente
	 * @param iteracion
	 *            Indica el periodo de proyección J que se está calculando de
	 *            entre todos los periodos de proyección de la umic (proyUmic)
	 * @param fcalc
	 *            Fecha de calculo
	 * @param umic
	 *            Contiene los datos de la Umic que se está procesando
	 * @param btcUmic
	 *            Contiene el detalle de la base técnica de cálculo para la umic
	 * @param mapVariables
	 *            mapa con las variables de memoria necesarias
	 * @param codSubproceso
	 *            Código del subproceso que se está ejecutando
	 * @return vzcj
	 */
	private BigDecimal moduloVZC(final List<DetalleCorriente> proyUmic,
			final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic,
			final DetalleBaseTecnica btcUmic,
			final Map<String, Object> mapVariables, final String codSubproceso) {
		// Variables locales
		String varCriterioFec = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriterEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		String varTabMort = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal varLzc = BigDecimal.ZERO;
		BigDecimal varEdadJ = BigDecimal.ZERO;
		BigDecimal varLjEntero = BigDecimal.ZERO;
		BigDecimal varLjEntero1 = BigDecimal.ZERO;
		BigDecimal varLj = BigDecimal.ZERO;
		BigDecimal vzcJ = BigDecimal.ZERO;
		BigDecimal varZc = BigDecimal.ZERO;
		List<BigDecimal> lstValoresTabMort = null;
		BigDecimal varFraccj = BigDecimal.ZERO;
		BigDecimal varEdadCalc = BigDecimal.ZERO;
		Timestamp varFechaEfecto = null;
		// Fin variables locales

		if (ModuloVZC.LOG.isTraceEnabled()) {
			ModuloVZC.LOG
				.trace("Inicio función << moduloVZC >> de la clase ModuloVZC, para la entrada iteracion = {}",
						iteracion);
		}

		// Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic,
				bloqueCorriente, fcalc, umic, btcUmic);


		if (null == bloqueCorriente.getFechaDevengo()) {
			return vzcJ;
		}
		varCriterEdad = UtilModulos.getVarCriEdad(mapVariables,
				CLAVE_VAR_CRIEDAD, umic.getDatosGenerales().getCcartera(), umic
						.getDatosGenerales().getKmodalidad(), umic
						.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(),
				ConstantsModulos.CTE_VA_CRIT_EDA);
		varCriterioFec = UtilModulos.getVarCriFec(mapVariables,
				CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic
						.getDatosGenerales().getKmodalidad(), umic
						.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);

		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos
				.validarVariableDeApoyoVarCriEdad(varCriterEdad);
			ValidacionesComunesModulos
				.validarVariableDeApoyoVarCriterioFecha(varCriterioFec);
		}

		/**
		 * Si estoy en el primer periodo (j=1) se calculan aquellos datos de la
		 * umic que son constantes en todos los periodos, así como las variables
		 * internas que tampoco varían por periodo, y que se dejarán accesibles
		 * para su uso en el subproceso por los siguientes periodos a calcular.
		 * Variables de Apoyo - VarCriterEdad -->
		 * obtenerConfiguracion.recuperarVariableApoyo(ID-CRITERIO) -
		 * VarCriterFec -->
		 * obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL) - Si alguna
		 * de las variables de apoyo retornadas es nulo se devuelve error
		 * funcional 005 - No se ha encontrado la Variable de Apoyo
		 * &NombreVariableApoyo, finalizando el proceso para la UMIC.
		 * 
		 * Si estoy en el primer periodo (j=1), se calcularán las siguientes
		 * variables (en el resto de periodos de recuperarán). Como el proceso
		 * es similar en j=1 ó j>1 la programación se hará de la misma forma. -
		 * varEdadCalc = nedad(fcalc, umic.asegurados.fnacAseg1, VarCriterEdad)
		 * varEdadCalc --> Dejo la variable disponible en memoria para le
		 * subproceso de la umic. - varAnoNac = Año (umic.asegurados.fnacAseg1);
		 * - varFracc0 = nanos(umic.fechas.fecinisus, fcalc, VarCriterFec);
		 * 
		 * - VarZc = ParteEntera(VarEdadCalc) + varFracc0
		 * 
		 * - varTabMort = btcUmic.tabla1Aseg1 varTabMort --> Dejo la variable
		 * disponible en memoria para le subproceso de la umic.
		 * 
		 * - varValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables,
		 * ConstantsFactorias.MODULO_VZC, ConstantsModulos.CTE_VAR_TAB_MORT,
		 * btcUmic.getFecCierre(), Integer.valueOf(varTabMort),
		 * String.valueOf(varAnoNac), btcUmic.getItcalc().get(0),
		 * umic.getBti().getPsobremort(), umic.getBti().getPriesgo());
		 * varLzcEntero = lstValoresTabMort.get(varZc.intValue());
		 * 
		 * - varLzcEntero= valoresTabMort(ENTERO(VarZc)).wkvalor -
		 * varLzcEntero1= valoresTabMort(ENTERO(VarZc)+1).wkvalor - varLzc =
		 * varLzcEntero + varFracc0 * (varLzcEntero1 – varLzcEntero)
		 * 
		 * 
		 * Para cualquier periodo j (j >= 1), se establecerán las siguientes
		 * variables: Si codSubproceso <> "PROY_PRV": - varFraccj= naños(fcalc,
		 * proyUmic(j). varBloque .fecDevengo, VarCriterFec). - varEdadJ = VarZc
		 * + varFraccj Si codSubproceso = "PROY_PRV": - varFraccj= naños(fcalc,
		 * proyUmic(j).fecDesde, VarCriterFec). - varEdadJ = VarZc + varFraccj
		 * 
		 * - varLjEntero= valoresTabMort(ENTERO(varEdadJ)).wkvalor -
		 * varLjEntero1= valoresTabMort(ENTERO(varEdadJ)+1).wkvalor - varLj=
		 * varLjEntero + varFraccj * (varLjEntero1– varLjEntero)
		 */

		// Estos valores NO VARÍAN con cada iteración.
		// Si no están calculados getXXX los calcula y los devuelve.
		// Si ya están calculados getXXX los devuelve directamente.

		if (ConstantsFunciones.CTE_APOR_REDUCIDA.equals(umic.getDatosGenerales().getCsitupol()) &&
				ConstantesSolvencia.NEGOCIO_COLECTIVO.equals(umic.getDatosGenerales().getCnegocio())) {
			varFechaEfecto = umic.getFechas().getFecinisus();
		} else {
			varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), 
					umic.getCapitales().getIsaldo());
		}
		
		/**
		 * varZc = getVarZc(mapVariables, modulo, constanteVariable,
		 * varEdadCalc, varfechaEfecto, fcalc, varCriterioFecha)
		 * getVarZc(...) realiza internamente las siguientes acciones: - Si está
		 * calculada la devuelve. - Si no está calculada: - varFracc0 =
		 * nanos(varfechaEfecto, fcalc, VarCriterFec); - VarZc =
		 * ParteEntera(VarEdadCalc1) +varFracc0
		 */
		Timestamp fecNacAseg = umic.getAsegurados().getFnacAseg1();
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		if (umic.getDatosGenerales().getKmodalidad().equals(ConstantsModulos.MOD_363)){
			varEdadCalc = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL + fecNacAseg, varFechaEfecto, fecNacAseg, ConstantsFunciones.CTE_CRI_FECHA_06, umic.getRentas().getFecIni(), varEdifer);
		}else{
			varEdadCalc = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL + fecNacAseg, varFechaEfecto, fecNacAseg, varCriterEdad, umic.getRentas().getFecIni(), varEdifer);
		}

		if (umic.getDatosGenerales().getKmodalidad().equals(ConstantsModulos.MOD_363)){
			if (fcalc.after(umic.getRentas().getFecIni())){
				varZc = UtilModulos.getVarZc(mapVariables,
						CLAVE_VAR_ZC + fecNacAseg + fcalc,
						varEdadCalc, umic.getRentas().getFecIni(), fcalc,
						varCriterioFec,varCriterEdad);
			}else{
				varZc = varEdadCalc;
			}
		}else{ 
			varZc = UtilModulos.getVarZc(mapVariables,
					CLAVE_VAR_ZC + fecNacAseg + fcalc,
					varEdadCalc, varFechaEfecto, fcalc,
					varCriterioFec,varCriterEdad);
		}
		
		varTabMort = btcUmic.getTablacalc1aseg1();

		lstValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables,
				CLAVE_VAR_VAL_TAB_MORT + varTabMort + fecNacAseg, umic, btcUmic,
				IObtenerConfiguracion.OrdenAsegurado.ASEG1,
				varCriterEdad, ConstantesSolvencia.CTE_TABMORT_L);		
		

		/**
		 * varLzc = getVarLzc(mapVariables, modulo, constanteVariable, varZc1,
		 * varLzcEntero, varLzcEntero1).
		 * varLzcEntero=lstValoresTabMort1.get(varZc1.intValue())
		 * varLzcEntero1=lstValoresTabMort1.get(varZc1.intValue() + 1)
		 * getVarLzc(...) realiza internamente las siguientes acciones: - Si
		 * está calculada la devuelve. - Si no está calculada: - varLzc1 =
		 * varLzc1Entero + parteDecimal(VarZc1) * (varLzc1Entero1 –
		 * varLzc1Entero)
		 */
		// Optimizacion. Solo es necesario recalcular varLzc cuando cambie varZc. La clave depende del valor de varZc
		varLzc = UtilModulos.getVarLzcVZC(mapVariables, CLAVE_VAR_LZC + varZc + varTabMort + fecNacAseg, varZc, lstValoresTabMort);
		
		// Estos valores SÍ VARÍAN con cada iteración.

		varFraccj = FuncionesAuxiliares.nAnnos(fcalc,
				bloqueCorriente.getFechaDevengo(), varCriterioFec);
//INI-TAR00400971
		if ((umic.getDatosGenerales().getKprestacion() != null) && (umic.getDatosGenerales().getKprestacion().equals("RS901"))) {
			varFraccj = FuncionesAuxiliares.nAnnos(fcalc,
					proyUmic.get(iteracion-1).getFechaDesde(), varCriterioFec);	
			}
//FIN-TAR00400971

		varEdadJ = varZc.add(varFraccj);

		/**
		 * - varLjEntero= valoresTabMort(ENTERO(varEdadJ)).wkvalor -
		 * varLjEntero1= valoresTabMort(ENTERO(varEdadJ)+1).wkvalor - varLj=
		 * varLjEntero + varFraccj * (varLjEntero1– varLjEntero)
		 */
		if (BigDecimal.ZERO.compareTo(varLzc) != 0) {
			if(varEdadJ.compareTo(BigDecimal.valueOf(lstValoresTabMort.size()-2)) == 1) {
				vzcJ = BigDecimal.ZERO;
			} else {
				varLjEntero = lstValoresTabMort.get(varEdadJ.intValue());
				if (BigDecimal.ZERO.compareTo(varLjEntero) != 0) {
					varLjEntero1 = lstValoresTabMort.get(varEdadJ.intValue() + 1);
					varLj = Util.interpolaPorEdad(varLjEntero, varLjEntero1, varEdadJ);
					
					// Se calculará la probabilidad en el periodo j como VzcJ = varLj /
					// varLzc
					vzcJ = varLj.divide(varLzc, ConstantsFunciones.MATH_CONTEXT);
				} else {
					vzcJ = BigDecimal.ZERO;
				}
			}
		} else {
			vzcJ = BigDecimal.ZERO;
		}
						
		if (ModuloVZC.LOG.isTraceEnabled()) {
			ModuloVZC.LOG.trace("Fin función << moduloVZC >> de la clase ModuloVZC, para la iteracion = {},  con resultado capitalCartera = {}",
						iteracion, vzcJ);
		}

		return vzcJ;
	}

}
