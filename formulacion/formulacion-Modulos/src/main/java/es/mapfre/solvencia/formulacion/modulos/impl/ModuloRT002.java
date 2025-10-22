/**MODIFICACION: MU-2019-029631
//FECHA: 30/05/2019
//DESCRIP: Se codifica correctamente el if para que no de problemas si es null.
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
import es.mapfre.solvencia.formulacion.util.Terminales;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo RT002.
 * La expresión matemática para su determinación es la siguiente:
 * 		RT(002,tc) =  K1 * Vx(Tc+1)
 * @author agonzalezgar
 *
 */
public class ModuloRT002 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloRT002.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_RT002;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VA_CRIT_EDA = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_VA_CRIT_EDA.concat(CLAVE_MODULO);	
	
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	private static final String CLAVE_VAR_TC0 = ConstantsModulos.CTE_VAR_TC0.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_X = ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_TERMINAL = ConstantsModulos.CTE_VAR_TERMINAL.concat(CLAVE_MODULO);
	private static final String CLAVE_VTX002 = ConstantsModulos.CTE_VTX002;
	private static final String CLAVE_LST_PROY = ConstantsModulos.CTE_LST_PROY.concat(CLAVE_MODULO);
	private static final String CLAVE_LEIDOBTI = ConstantsModulos.CTE_LEIDOBTI;
	private static final String CLAVE_LEIDOBTIPR = ConstantsModulos.CTE_LEIDOBTIPR;

	
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
			if (ModuloRT002.LOG.isTraceEnabled()) {
				ModuloRT002.LOG.trace("Inicio de execute en clase ModuloRT002");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZC002
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			
			//Invocamos a la función moduloRT002
			resultado = moduloRT002(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloRT002.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloRT002.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloRT002.LOG.isTraceEnabled()) {
			ModuloRT002.LOG.trace("Fin de execute en clase ModuloRT002");
		}
		
		return resultado;
	}
	/**
	 * Módulo de cálculo de la cuantía por Rescate de la garantía de bono en cada punto necesario. (modalidad 209)
	 * La expresión matemática para su determinación es la siguiente:
	 *				 RT(002,tc) =  K1 * Vx(Tc+1)
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
	private BigDecimal moduloRT002(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal varVx2 = BigDecimal.ZERO;
		String varCriterFec = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriterEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		Timestamp varfechaEfecto;
		BigDecimal rt002 = BigDecimal.ZERO;
		List<DetalleCorriente> lstDetaCor;
		//Fin variables locales
		
		if (ModuloRT002.LOG.isTraceEnabled()) {
			ModuloRT002.LOG.trace("Inicio función << moduloRT002 >> de la clase ModuloRT002, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		/**
		 * Variables de Apoyo
		 * -	VarCriterFec = obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
		 * 		o	Si la variable de apoyo retornada es nula se devuelve
		 * 		error funcional 005 - No se ha encontrado la Variable de Apoyo & ID-TEMPORAL, finalizando el proceso para la UMIC.
		 * -	VarCriterEdad = obtenerConfiguracion.recuperarVariableApoyo(ID-CRITERIO)
		 * 		o	Si la variable de apoyo retornada es nula se devuelve
		 * error funcional 005 - No se ha encontrado la Variable de Apoyo ID-CRITERIO, finalizando el proceso para la UMIC.
		 */
		// Cálculo y validación de las variables de apoyo.
		varCriterEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_VA_CRIT_EDA);
		varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFec);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterEdad);
		}
		// Fin del cálculo y la validación de las variables de apoyo.
		
		if (bloqueCorriente.getFechaDevengo() == null) {
			return rt002;
		}
		
		/**
		 * Constantes de Rescates
		 * Para obtener el valor real de las constantes de rescate hay que realizar un servicio de consulta con el código y la duración de la constante.
		 * 
		 * -	codk1 = umic.rescates.krescate1
		 * -	Si el código de la constante  empieza por  KT
		 * 		o	durk1 = TCM(umic.fechas.fecinisus, proyUmic(j).varBloque.fecDevengo);
		 * -	Si el código de la constante empieza por  KC
		 * 		o	durk1 = 999
		 * -	Si el código de la constante empieza por  KM
		 * 		o	durk1 =  umic.duraciones.ndurprima/12
		 * -	Si el código de la constante empieza por  KN
		 * 		o	durk1 = umic.duraciones.ndursemes
		 * -	vark1 -> obtenerConfiguracion.recuperarCtesRescates(codk1, durk1)
		 * 		o	Si el valor de la constante  retornada es nulo se devuelve
		 * 		error funcional 005 - No se ha encontrado la Constante de Rescate  codK1, finalizando el proceso para la UMIC.
		 */
		
		varfechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		final String codk1 = umic.getRescates().getKrescate1();
		final Integer durk1 = UtilModulos.obtenerDuracionCodKX(codk1, varfechaEfecto, bloqueCorriente.getFechaDevengo(), 
				umic.getDuraciones().getNdurprima(), umic.getDuraciones().getNdursegmes());
		
		final BigDecimal vark1 = UtilModulos.getCteRescate(mapVariables, codk1, durk1);
		
		// vark1 depende de durk1; durk1 puede variar entre periodos luego la validación hay que hacerla en cada iteración.
		ValidacionesComunesModulos.validarCteRescateVarkx(codk1, vark1);
		
		
		/**
		 * Variables Módulo
		 * -	varCapital = umic.capitales.icapact -> dejo la variable en memoria, disponible para el subproceso de la umic.
		 * -	varGic =  btcUmic.gtoRosspCap  -> dejo la variable en memoria, disponible para el subproceso de la umic.
		 * -	varI1  = btcUmic.itcalc1  -> dejo la variable en memoria, disponible para el subproceso de la umic.
		 * -	varN = umic.duraciones.ndursegano -> dejo la variable en memoria, disponible para el subproceso de la umic.
		 * -	varDifer = umic.rentas.nadifer -> dejo la variable en memoria, disponible para el subproceso de la umic.
		 * -	Varfecnac = umic.asegurados.fnacAseg1 -> dejo la variable en memoria, disponible para el subproceso de la umic.
		 * -	varTabMort = btcUmic.tabla1Aseg1  -> dejo la variable en memoria, disponible para el subproceso de la umic
		 * -	varAnoNac = Año (umic.asegurados.fnacAseg1);
		 * -	varValoresTabMort = obtenerConfiguracion. recuperarValoresExperiencia(btcUmic.fecCierre, varTabMort, varAnoNac, btcUmic. itcalc1, umic.baseTecIni. psobremort, umic.baseTecIni. priesgo)
		 * -	varTc0 = TC(umic.fechas fecinisus, fcalc) -> dejo la variable en memoria, disponible para el subproceso de la umic.
		 * -	VarX = nedad(umic.fechas fecinisus, Varfecnac, VarCriterEdad) -> dejo la variable en memoria, disponible para el subproceso de la umic.
		 */
		final BigDecimal varCapital = umic.getCapitales().getIcapact();
		final BigDecimal varGic = btcUmic.getGtorosspCap();
		final Integer varN = umic.getDuraciones().getNdursegano();
		final Integer varDifer = umic.getRentas().getNadifer();
		final Timestamp varFecnac = umic.getAsegurados().getFnacAseg1();

		List<BigDecimal> lstValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterEdad, ConstantesSolvencia.CTE_TABMORT_L);
		
		/**
		 * Para cualquier periodo j se calculará:
		 * -	varP = varTc + nanos(fcalc, proyUmic(j).varBloque.fecDevengo, varCriterFec) ->
		 * 		sobreescribo la variable en memoria, disponible para el subproceso de la umic.
		 */
		final Integer varP = UtilModulos.getVarTC0(mapVariables, CLAVE_VAR_TC0, varfechaEfecto, fcalc) + FuncionesAuxiliares.nAnnos(fcalc, bloqueCorriente.getFechaDevengo(), varCriterFec).intValue();
		
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		final BigDecimal varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varfechaEfecto, varFecnac, varCriterEdad, umic.getRentas().getFecIni(), varEdifer);
		
		/**
		 * Se obtendrá la fórmula de terminal a utilizar de la configuración de provisiones por fórmula cerrada.
		 * Si varTerminal = "VTX002"
		 * varVx2 = VTX002(varCapital, varValoresTabMort, varGic, varI1, varDifer, varX, varP, varN)
		 */
		final String varTerminal = UtilModulos.getVarTerminal(mapVariables, CLAVE_VAR_TERMINAL, umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), umic.getDatosAdicionales().getPrestCal(), btcUmic.getBaseTec());
//INI-929631
//		if (varTerminal.equals("LEIDOBTI")){
		if (CLAVE_LEIDOBTI.equals(varTerminal)){
//FIN-929631
			lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PROY, ConstantsModulos.CTE_BTI, btcUmic.getFecCierre(), umic.getKey());
			
			varVx2 = lstDetaCor.get(iteracion - 1).getTotalFlujoProyeccion().getProvbtiproy();
			
			if (varVx2 == null){
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DD);
			}
			
		}else if(CLAVE_LEIDOBTIPR.equals(varTerminal)){
			lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PROY, ConstantsModulos.CTE_BTI_PROY, btcUmic.getFecCierre(), umic.getKey());
			varVx2 = lstDetaCor.get(iteracion - 1).getTotalFlujoProyeccion().getProvbtiproy();
			
			if (varVx2 == null){
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DD);
			}
	
		
		}else if (CLAVE_VTX002.equals(varTerminal)) {
			final BigDecimal varI1 = btcUmic.getItcalc().get(0);
			varVx2 = Terminales.vtx002(varCapital, lstValoresTabMort,
					varGic, varI1, varDifer, varX.intValue(), varP, varN);
		}
		
		/**
		 * Se calculará el importe de rescate correspondiente al periodo j como:
		 * RT002 (j) = vark1 * varVx2
		 */
		rt002 = vark1.multiply(varVx2);
		
		if (ModuloRT002.LOG.isTraceEnabled()) {
			ModuloRT002.LOG.trace("Fin función << moduloRT002 >> de la clase ModuloRT002, para la iteracion = {} con resultado rt002 = {}", iteracion, rt002);
		}
			
		return rt002;
	}
}
