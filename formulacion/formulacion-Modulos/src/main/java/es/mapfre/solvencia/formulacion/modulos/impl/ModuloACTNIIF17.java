package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dao.impl.conversionesBel.ValoresCurvaTipoDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.AsigCurvasTipoUOADao;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresCurvaTipo;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.AsigCurvasTipoUOA;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.excepcionGBT.GestorBasesTecnicasException;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesActualizacionFinanciera;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;
import es.mapfre.solvencia.formulacion.util.Util;

public class ModuloACTNIIF17 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloACTNIIF17.class);

	// Inicio de las variables estaticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_ACTNIIF17;
	private static final String CLAVE_MESCIER = ConstantsModulos.CTE_MESES_CIERRE_NIIF17;
	private static final String CLAVE_VAR_MESCIER = CLAVE_MESCIER.concat(CLAVE_MODULO);
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

	private static final String CLAVE_VA_INTER = ConstantsModulos.CTE_VA_INTER;
	private static final String CLAVE_VAR_CRIINT = ConstantsModulos.CTE_VA_INTER.concat(CLAVE_MODULO);
	// Fin de las variables estaticas usadas para agilizar operaciones.
	GestorBasesTecnicasException exc = new GestorBasesTecnicasException();

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/**
	 * Funcion encargada de obtener los parametros necesarios y de realizar la
	 * llamada a la funcion que realiza los calculos del modulo.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		// Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		// Fin variables locales

		try {
			if (ModuloACTNIIF17.LOG.isTraceEnabled()) {
				ModuloACTNIIF17.LOG.trace("Inicio de execute en clase ModuloACTNIIF17");
			}

			// Recuperamos los datos que le pasaremos a la funcion moduloACTNIIF17
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperacion de datos.

			// Invocamos a la funcion de calculo ACTNIIF17
			resultado = moduloACTNIIF17(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);

		} catch (Solvencia2Excepcion e) {
			ModuloACTNIIF17.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloACTNIIF17.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloACTNIIF17.LOG.isTraceEnabled()) {
			ModuloACTNIIF17.LOG.trace("Fin de execute en clase ModuloACTNIIF17");
		}

		return resultado;
	}

	/**
	 * El modulo ACTNIIF17 determina el calculo de los factores de actualizacion
	 * financiera para la garantia en un periodo dado asi como el Flujo Actualizado
	 * de dicho periodo para la Base Tecnica NIIF17.
	 * 
	 * @param proyUmic        Estructura detalleCorrientes de la umic
	 * @param bloqueCorriente Bloque de Trabajo de la corriente
	 * @param iteracion       Indica el periodo de proyeccion J que se esta
	 *                        calculando de entre todos los periodos de proyeccion
	 *                        de la umic (proyUmic)
	 * @param fcalc           Fecha de calculo
	 * @param umic            Contiene los datos de la Umic que se esta procesando
	 * @param btcUmic         Contiene el detalle de la base tecnica de calculo para
	 *                        la umic
	 * @param mapVariables    mapa con las variables de memoria necesarias
	 */
	private BigDecimal moduloACTNIIF17(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente,
			final Integer iteracion, final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic,
			final Map<String, Object> mapVariables) {
		// Variables locales
		BigDecimal actniif17 = BigDecimal.ZERO;
		String varCriterFec = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriterInt = ConstantsFunciones.CTE_CADENA_VACIA;
		String varMesesCierres = null;
		Integer varMesesCierresInt = null;
		ValoresCurvaTipoDao valoresDao = new ValoresCurvaTipoDao();
		int varDurJ = 0;
		List<ValoresCurvaTipo> varValoresCurva = null;
		List<AsigCurvasTipoUOA> varCurvaTipos = null;
		String curvaTipo = ConstantsFunciones.CTE_CADENA_VACIA;
		Timestamp varFecCierreAnt = null;
		String varMesesCierreNIIF17;
		int mesesCierreN17;
		// Fin variables locales
		Integer maximoDiasPLazo = ConstantsFunciones.CTE_1;
		BigDecimal maximoPorcentajeInteres = BigDecimal.ZERO;
		int elementosLstCurTipo = 0;
		BigDecimal varIct = BigDecimal.ZERO;

		ValoresCurvaTipo plazoAnterior = null;
		ValoresCurvaTipo plazoPosterior = null;

		int varDurAnt = 0;
		BigDecimal varIntAnt = BigDecimal.ZERO;
		int varDurPost = 0;
		BigDecimal varIntPost = BigDecimal.ZERO;
		
		int varNumDias = 0;

		int indicePlazoAnterior = -1;

		Timestamp varFecJ = null;

		if (ModuloACTNIIF17.LOG.isTraceEnabled()) {
			ModuloACTNIIF17.LOG.trace(
					"Inicio funcion << ModuloACTNIIF17 >> de la clase ModuloACTNIIF17, para la iteracion = {}",
					iteracion);
		}

		varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(),
				umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		varCriterInt = UtilModulos.getsetStringRecuperarDefinicionAuxiliar(mapVariables, CLAVE_VAR_CRIINT,
				umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(),
				umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_VA_INTER);
		varMesesCierres = UtilModulos.getVarMesesCierres(mapVariables, CLAVE_VAR_MESCIER,
				umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(),
				umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_MESCIER);

		if(null == varMesesCierres){
			mesesCierreN17 = 0;
		}else{
			mesesCierreN17 = Integer.parseInt(varMesesCierres);
		}
		
		if (varCriterFec != "01") {
			varCriterFec = "01";
		}

		if (varCriterInt == null) {
			varCriterInt = "01";
		}

		if (varMesesCierres == null) {
			varMesesCierres = "1";
		}

		ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFec);
		ValidacionesComunesModulos.validarVariableDeApoyoVarCritInt(varCriterInt);
		ValidacionesComunesModulos.validarVariableDeApoyoVarMesesCierres(varMesesCierres);

		String kcurva = btcUmic.getCurvaTi();
		Timestamp fechaEf = btcUmic.getFcurvaTi();
		
		if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIFF17OCI)){
			fechaEf =  UtilFechas.getUltimoDiaDelMes(UtilFechas.decreMeses(btcUmic.getFecCierre(), mesesCierreN17));
			//kcurva = ctu.get(0).getkCurva();
		}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17LIRIN)){
			fechaEf = umic.getFechas().getFecinisus();
			//kcurva = ctu.get(0).getkCurva();
		}
		
		if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIFF17OCI) || btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17LIRIN)){
			varValoresCurva = getValorCurvaTipoCINIIF17(kcurva, fechaEf);
	
		}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIF17LIR)){
			varValoresCurva = getValorCurvaTipoCINIIF17(kcurva, fechaEf);
		}else{
			varValoresCurva = getValorCurvaTipoCINIIF17(kcurva, UtilFechas.decreMeses(new Timestamp(new GregorianCalendar(9999,12,31,ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis()), 1));
		}
		
		elementosLstCurTipo = varValoresCurva.size();
		// Calculamos varNumDias
		if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIF17LIR)) {
			if (iteracion - 1 < elementosLstCurTipo) {
				varNumDias = FuncionesAuxiliares.nDias(fechaEf, umic.getDatosGenerales().getFecCierre(), varCriterFec);
			}else {
				varNumDias = FuncionesAuxiliares.nDias(fechaEf, umic.getDatosGenerales().getFecCierre(), varCriterFec);
			}
		}else {
			varNumDias = 0;
		}
		
		if (elementosLstCurTipo != 0) {
			maximoDiasPLazo = varValoresCurva.get(elementosLstCurTipo - 1).getDiasPlazo();
			maximoPorcentajeInteres = varValoresCurva.get(elementosLstCurTipo - 1).getPorcentajeInteres();
		}

		if (bloqueCorriente == null) {
			varFecJ = proyUmic.get(iteracion - 1).getFechaDesde();
		} else {
			varFecJ = bloqueCorriente.getFechaPago();
		}

		if (varFecJ != null) {
			varDurJ = FuncionesAuxiliares.nDias(fcalc,
					varFecJ, varCriterFec);
			if (varDurJ == 0) {
				varDurJ = 1;
			}

			if (varDurJ >= maximoDiasPLazo.intValue()) {
				varIct = maximoPorcentajeInteres;
			} else {
				/**
				 * De entre los valores de tipos almacenados en varValoresCurva, se buscarán
				 * aquellos que cumplen con la siguiente condición: Plazo anterior:
				 * varValoresCurva.diasPlazo --> máximo de entre los valores con
				 * varValoresCurva.diasPlazo < varDurJ recuperando: - varDurAnt = diasPlazo -
				 * varIntAnt = porcentajeInteres Plazo posterior: varValoresCurva.diasPlazo -->
				 * máximo de entre los valores con varValoresCurva.diasPlazo > varDurJ
				 * recuperando: - varDurPost= diasPlazo - varIntPost= porcentajeInteres
				 */
				// Obtenemos el plazo anterior y el plazo posterior
				indicePlazoAnterior = obtenerIndicePlazoAnterior(varValoresCurva, varDurJ + varNumDias);
			
				if (indicePlazoAnterior != -1) {
					plazoAnterior = varValoresCurva.get(indicePlazoAnterior);
					varDurAnt = plazoAnterior.getDiasPlazo();
					varIntAnt = plazoAnterior.getPorcentajeInteres();

					if (indicePlazoAnterior < varValoresCurva.size() - 1) {
						plazoPosterior = varValoresCurva.get(indicePlazoAnterior + 1);
						varDurPost = plazoPosterior.getDiasPlazo();
						varIntPost = plazoPosterior.getPorcentajeInteres();
					}
				}

				/**
				 * Ahora se deberá calcualr el interés a aplicar en base al anterior y al
				 * posterior: varIct=calcularTipoInteres(VarCriterInt,
				 * varDurAnt,varIntAnt,varDurPost,varIntPost,varDurJ) varFecJ =
				 * proyUmic(j).fecPago
				 */
				varIct = FuncionesActualizacionFinanciera.calcularTipoInteres(varCriterInt, varDurAnt, varIntAnt,
						varDurPost, varIntPost, varDurJ + varNumDias);
			}

			actniif17 = Util.pow(
					BigDecimal.ONE.add(varIct.divide(ConstantsFunciones.CTE_OPER_100, ConstantsFunciones.MATH_CONTEXT)),
					FuncionesAuxiliares.nAnnos(fcalc, varFecJ, varCriterFec).negate());
		}
		
		if (ModuloACTNIIF17.LOG.isTraceEnabled()) {
			ModuloACTNIIF17.LOG.trace(
					"Fin funcion << ModuloACTNIIF17 >> de la clase ModuloACTNIIF17, para la iteracion = {}, con resultado actniif17 = {}",
					iteracion, actniif17);
		}

		return actniif17;
	}

	public List<AsigCurvasTipoUOA> getCurvasTipoCINIIF17(DetalleBaseTecnica btcUmic, Umic umic, int meses) {

		List<AsigCurvasTipoUOA> result = new ArrayList<AsigCurvasTipoUOA>();
		AsigCurvasTipoUOADao dao = new AsigCurvasTipoUOADao();
		//int meses = Integer.parseInt(mesesCierre);

		if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17) || btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17LIRIN)) {
			result = dao.obtenerCurvaTipoCINIIF17(ConstantsModulos.CTE_BT_NIIF17, umic.getDatosNiif17().getuoa(),
					umic.getDatosNiif17().getkcarinv17());
		} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIF17LIR)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17CLIR) || btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17IF)) {
			result = dao.obtenerCurvaTipoNiif17Lir(ConstantsModulos.CTE_BT_NIIF17, umic.getDatosNiif17().getuoa(),
					umic.getDatosNiif17().getkcarinvlir(), umic.getFechas().getFecinisus(), umic.getDatosNiif17().getkcurvalir());
		} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIFF17OCI)) {
			Timestamp fecCierreAnt = UtilFechas.getUltimoDiaDelMes(UtilFechas.decreMeses(btcUmic.getFecCierre(), meses));
			result = dao.obtenerCurvaTipoCINIIF17(ConstantsModulos.CTE_BT_NIFF17OCI, umic.getDatosNiif17().getuoa(),
					umic.getDatosNiif17().getkcarinv17());
		} else {
			exc.setCodigoRetorno(ConstantsModulos.CTE_COD_ERROR_NO_BT_NIIIF17);
			exc.setTextoError(ConstantsModulos.CTE_DESC_ERROR_NO_BT_NIIF17);
			exc.setInfAmpliada(ConstantsModulos.CTE_DESL_ERROR_BT_NO_NIIF17);
			throw exc;
		}
		
		if (result.size() == 0) {
			exc.setCodigoRetorno(ConstantsModulos.CTE_COD_ERROR_NO_BT_VALORES);
			exc.setTextoError(ConstantsModulos.CTE_DESC_ERROR_NO_BT_VALORES);
			exc.setInfAmpliada(ConstantsModulos.CTE_DESL_ERROR_NO_BT_VALORES);
			throw exc;

		} else {
			return result;
		}
	}

	public List<ValoresCurvaTipo> getValorCurvaTipoCINIIF17(String kcurva, Timestamp lfEfec) {
		List<ValoresCurvaTipo> result = null;
		ValoresCurvaTipoDao dao = new ValoresCurvaTipoDao();
		result = dao.getByBtNIFF17(kcurva, lfEfec);
		return result;
	}
	
	public List<ValoresCurvaTipo> getValorCurvaTipoN17(String kcurva) {
		List<ValoresCurvaTipo> result = null;
		ValoresCurvaTipoDao dao = new ValoresCurvaTipoDao();
		result = dao.getByCurvaNIFF17(kcurva);
		return result;
	}

	/**
	 * Función encargada de recuperar los datos necesarios correspondientes al plazo
	 * anterior.
	 * 
	 * @param lstValCurvaTipo Lista de valoresCurva que se han recuperado
	 * @param varDurJ         Duración requerida
	 * @return valorDevolver
	 */
	private int obtenerIndicePlazoAnterior(
			final List<ValoresCurvaTipo> lstValCurvaTipo, final Integer varDurJ) {
		// Variables locales
		int valorDevolver = -1;
		// Fin variables locales

		if (ModuloACTNIIF17.LOG.isTraceEnabled()) {
			ModuloACTNIIF17.LOG
					.trace("Inicio de la función << obtenerIndicePlazoAnterior >> de la clase ModuloACTBEL");
		}

		// Para cada elemento de la lista de curvastipo
		if (null != lstValCurvaTipo) {
			int low = 0;
			int high = lstValCurvaTipo.size()-1;

			while (low <= high) {
				int mid = (low + high) >>> 1;
				ValoresCurvaTipo midVal = lstValCurvaTipo.get(mid);
				
				if(midVal.getDiasPlazo() <= varDurJ) {
					if(mid == lstValCurvaTipo.size()-1 || lstValCurvaTipo.get(mid + 1).getDiasPlazo() > varDurJ) {
						valorDevolver = mid;
						break;
					} else {
						low = mid + 1;
					}					
				} else {
					high = mid - 1;
				}
			}
		}

		if (ModuloACTNIIF17.LOG.isTraceEnabled()) {
			ModuloACTNIIF17.LOG
					.trace("Fin de la función << obtenerIndicePlazoAnterior >> de la clase ModuloACTNIIF17");
		}

		return valorDevolver;
	}

	/**
	 * Función encargada de recuperar los datos necesarios correspondientes al plazo
	 * posterior.
	 * 
	 * @param lstValCurvaTipo Lista de valoresCurva que se han recuperado
	 * @param varDurJ         Duración requerida
	 * @return valorDevolver
	 */
	private ValoresCurvaTipo obtenerPlazoPosterior(final List<ValoresCurvaTipo> lstValCurvaTipo,
			final Integer varDurJ) {
		// Variables locales
		ValoresCurvaTipo valorDevolver = null;
		// Fin variables locales

		if (ModuloACTNIIF17.LOG.isTraceEnabled()) {
			ModuloACTNIIF17.LOG.trace("Inicio de la función << obtenerPlazoPosterior >> de la clase ModuloACTNIIF17");
		}

		// Para cada elemento de la lista de curvastipo
		if (null != lstValCurvaTipo) {
			for (ValoresCurvaTipo valorActual : lstValCurvaTipo) {
				// si los dias de plazo es mayor que el valor de varDurJ y no
				// hay otro valor previo mas grande
				if (valorActual.getDiasPlazo() > varDurJ) {
					valorDevolver = valorActual;
					break;
				}
			}
		}

		if (ModuloACTNIIF17.LOG.isTraceEnabled()) {
			ModuloACTNIIF17.LOG.trace("Fin de la función << obtenerPlazoPosterior >> de la clase ModuloACTNIIF17");
		}

		return valorDevolver;
	}
}