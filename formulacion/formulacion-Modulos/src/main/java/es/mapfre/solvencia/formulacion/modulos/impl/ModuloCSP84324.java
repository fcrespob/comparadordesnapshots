package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.conversionesBel.ValoresCurvaTipo;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.maestro.ValoresLiquidativos;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesActualizacionFinanciera;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.servicios.impl.AlmacenarDatos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del calculo que devuelve el Valor del Fondo en cada momento
 * de la PTRI (provisión técnica dónde tomador asume riego inversión) en cada
 * momento. La expresión matemática para su determinación es la siguiente:
 * CSP(84324, tcm+beta) = {Sumatorio desde k=1 hasta kk} Npartk * VLPk(tcm +
 * beta)
 *
 * @author apedro
 *
 */
public class ModuloCSP84324 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP84324.class);

	// Variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP84324;
	private static final String CLAVE_VAR_BETA = ConstantsModulos.CTE_VAR_BETA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_FONDOS_UMIC = ConstantsModulos.CTE_VAR_FONDOSUMIC.concat(CLAVE_MODULO);

	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

	private static final String CLAVE_VA_INTER = ConstantsModulos.CTE_VA_INTER;
	private static final String CLAVE_VAR_CRIINT = ConstantsModulos.CTE_VA_INTER.concat(CLAVE_MODULO);

	private static final String CLAVE_VAR_VLPK = ConstantsModulos.CTE_VAR_VLPK.concat(CLAVE_MODULO);
	// private static final String CLAVE_VAR_FACTOR_IPC =
	// ConstantsModulos.CTE_VAR_FACTOR_IPC.concat(CLAVE_MODULO);

	private static final String CLAVE_POTENCIA = "potencia";
	// private static final String CLAVE_VAR_FFIN =
	// ConstantsModulos.CTE_VAR_FFIN.concat(CLAVE_MODULO);
	private static final String CLAVELISTACURTI = ConstantsModulos.CTE_LST_CURVA_TIPO;
	private static final String CLAVE_VAR_ACT_VIDA = ConstantsModulos.CTE_VAR_ACT_VIDA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ACT_FALL = ConstantsModulos.CTE_VAR_ACT_FALL.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ACT_GAST = ConstantsModulos.CTE_VAR_ACT_GAST.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ACT_COM = ConstantsModulos.CTE_VAR_ACT_COM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ACT_INV = ConstantsModulos.CTE_VAR_ACT_INV.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ACT_RESC = ConstantsModulos.CTE_VAR_ACT_RESC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ACT_PRIM = ConstantsModulos.CTE_VAR_ACT_PRIM.concat(CLAVE_MODULO);

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
			if (ModuloCSP84324.LOG.isTraceEnabled()) {
				ModuloCSP84324.LOG.trace("Inicio de execute en clase ModuloCSP84324");
			}

			// Recuperación de los datos que se pasarán a la función moduloCSP84324.
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubProceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos que se pasarán a la función
			// moduloCSP84324.

			// Invocación de la función moduloCSP84324
			resultado = moduloCSP84324(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,
					codSubProceso);

		} catch (Solvencia2Excepcion e) {
			ModuloCSP84324.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP84324.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloCSP84324.LOG.isTraceEnabled()) {
			ModuloCSP84324.LOG.trace("Fin de execute en clase ModuloCSP84324");
		}

		return resultado;
	}

	/**
	 * Módulo de cálculo que devuelve el Valor del Fondo en cada momento de la PTRI
	 * (provisión técnica dónde tomador asume riego inversión) en cada momento. La
	 * expresión matemática para su determinación es la siguiente: CSP(84324,
	 * tcm+beta) = {Sumatorio desde k=1 hasta kk} Npartk * VLPk(tcm + beta)
	 * 
	 * @param proyUmic        Estructura detalleCorrientes de la umic
	 * @param bloqueCorriente Bloque de Trabajo de la corriente
	 * @param iteracion       Indica el periodo de proyección J que se está
	 *                        calculando de entre todos los periodos de proyección
	 *                        de la umic (proyUmic)
	 * @param fcalc           Fecha de calculo
	 * @param umic            Contiene los datos de la Umic que se está procesando
	 * @param btcUmic         Contiene el detalle de la base técnica de cálculo para
	 *                        la umic
	 * @param mapVariables    mapa con las variables de memoria necesarias
	 * @param codSubproceso   código del sub proceso en ejecución
	 * @return
	 */
	private BigDecimal moduloCSP84324(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente,
			final Integer iteracion, final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic,
			final Map<String, Object> mapVariables, final String codSubProceso) {
		// Variables locales
		BigDecimal csp84324 = BigDecimal.ZERO;
		BigDecimal varCSP84324K = BigDecimal.ZERO;
		Integer varBeta = null;
		// BigDecimal varFactorIpc = BigDecimal.ZERO;
		BigDecimal varVLPK = BigDecimal.ZERO;
		List<ValoresLiquidativos> varFondosUmic;
		List<ValoresCurvaTipo> varValoresCurva = null;
		String varCriterFec = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriterInt = ConstantsFunciones.CTE_CADENA_VACIA;
		Integer varDurj;
		ValoresCurvaTipo plazoAnterior = null;
		ValoresCurvaTipo plazoPosterior = null;
		int varDurAnt = 0;
		BigDecimal varIntAnt = BigDecimal.ZERO;
		int varDurPost = 0;
		BigDecimal varIntPost = BigDecimal.ZERO;
		BigDecimal varFactorIct = BigDecimal.ZERO;
		BigDecimal varCnparticipacion = BigDecimal.ZERO;
		BigDecimal varPrima = BigDecimal.ZERO;
		BigDecimal varActFlujo = BigDecimal.ZERO;
		Modulo moduloAct;
		String varAct;
		List<DetalleCorriente> varProyCopia;
		List<DetalleCorriente> varProyPeriodo;

		// Fin variables locales

		if (ModuloCSP84324.LOG.isTraceEnabled()) {
			ModuloCSP84324.LOG.trace("Inicio de la función << moduloCSP84324 >> para la iteración = {}", iteracion);
		}

		// Validación de los campos de entrada.
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		varBeta = (Integer) mapVariables.get(CLAVE_VAR_BETA);
		if (varBeta == null) {
			varBeta = 0;
			mapVariables.put(CLAVE_VAR_BETA, varBeta);
		} else {
			varBeta = varBeta + 1;
			mapVariables.put(CLAVE_VAR_BETA, varBeta);
		}

		if (bloqueCorriente.getFechaDevengo() == null) {
			return csp84324;
		}

		// Variables de modulo
		varProyCopia = proyUmic;
		varProyPeriodo = proyUmic;

		if (codSubProceso.equals(ConstantsFunciones.CTE_PROY_VIDA)) {
			varAct = UtilModulos.getVarModulo(mapVariables, CLAVE_VAR_ACT_VIDA, umic, btcUmic,
					ConstantsModulos.CTE_PROY_VIDA, "04");

			moduloAct = FactoriaModulos.getModulo(varAct);
			varActFlujo = (BigDecimal) moduloAct.execute(varProyCopia, proyUmic.get(iteracion - 1).getBloqueVida(),
					iteracion, fcalc, umic, btcUmic, mapVariables, codSubProceso);
			varFactorIct = varActFlujo;
		}

		if (codSubProceso.equals(ConstantsModulos.CTE_PROY_FALL)) {

			varAct = UtilModulos.getVarModulo(mapVariables, CLAVE_VAR_ACT_FALL, umic, btcUmic,
					ConstantsModulos.CTE_PROY_FALL, "04");

			moduloAct = FactoriaModulos.getModulo(varAct);
			varActFlujo = (BigDecimal) moduloAct.execute(varProyPeriodo, proyUmic.get(iteracion - 1).getBloqueFall(),
					iteracion, fcalc, umic, btcUmic, mapVariables, codSubProceso);
			varFactorIct = varActFlujo;
		}

		if (codSubProceso.equals(ConstantsModulos.CTE_PROY_GTOS)) {

			varProyCopia.get(iteracion - 1).getBloqueGto()
					.setFechaDevengo(varProyPeriodo.get(iteracion - 1).getFechaHasta());
			varProyCopia.get(iteracion - 1).getBloqueGto()
					.setFechaPago(varProyPeriodo.get(iteracion - 1).getFechaHasta());
			varAct = UtilModulos.getVarModulo(mapVariables, CLAVE_VAR_ACT_GAST, umic, btcUmic,
					ConstantsModulos.CTE_PROY_GTOS, "04");

			moduloAct = FactoriaModulos.getModulo(varAct);
			varActFlujo = (BigDecimal) moduloAct.execute(varProyCopia, proyUmic.get(iteracion - 1).getBloqueGto(),
					iteracion, proyUmic.get(iteracion - 1).getFechaDesde(), umic, btcUmic, mapVariables, codSubProceso);

			varFactorIct = varActFlujo;
		}

		if (codSubProceso.equals(ConstantsModulos.CTE_PROY_COMI)) {

			varProyCopia.get(iteracion - 1).getBloqueComi()
					.setFechaDevengo(varProyPeriodo.get(iteracion - 1).getFechaHasta());
			varProyCopia.get(iteracion - 1).getBloqueComi()
					.setFechaPago(varProyPeriodo.get(iteracion - 1).getFechaHasta());
			varAct = UtilModulos.getVarModulo(mapVariables, CLAVE_VAR_ACT_COM, umic, btcUmic,
					ConstantsModulos.CTE_PROY_COMI, "04");

			moduloAct = FactoriaModulos.getModulo(varAct);
			varActFlujo = (BigDecimal) moduloAct.execute(varProyCopia, proyUmic.get(iteracion - 1).getBloqueComi(),
					proyUmic.get(iteracion - 1).getFechaDesde(), umic, btcUmic, mapVariables, codSubProceso);

			varFactorIct = varActFlujo;

		}

		if (codSubProceso.equals(ConstantsModulos.CTE_PROY_RESC)) {

			varProyCopia.get(iteracion - 1).getBloqueRte()
					.setFechaDevengo(varProyPeriodo.get(iteracion - 1).getFechaHasta());
			varProyCopia.get(iteracion - 1).getBloqueRte()
					.setFechaPago(varProyPeriodo.get(iteracion - 1).getFechaHasta());
			varAct = UtilModulos.getVarModulo(mapVariables, CLAVE_VAR_ACT_RESC, umic, btcUmic,
					ConstantsModulos.CTE_PROY_RESC, "04");

			moduloAct = FactoriaModulos.getModulo(varAct);
			varActFlujo = (BigDecimal) moduloAct.execute(varProyCopia, proyUmic.get(iteracion - 1).getBloqueRte(),
					proyUmic.get(iteracion - 1).getFechaDesde(), umic, btcUmic, mapVariables, codSubProceso);

			varFactorIct = varActFlujo;
		}

		if (codSubProceso.equals(ConstantsModulos.CTE_PROY_COMP)) {

			varProyCopia.get(iteracion - 1).getBloqueCompl()
					.setFechaDevengo(varProyPeriodo.get(iteracion - 1).getFechaHasta());
			varProyCopia.get(iteracion - 1).getBloqueCompl()
					.setFechaPago(varProyPeriodo.get(iteracion - 1).getFechaHasta());
			varAct = UtilModulos.getVarModulo(mapVariables, CLAVE_VAR_ACT_INV, umic, btcUmic,
					ConstantsModulos.CTE_PROY_COMP, "04");

			moduloAct = FactoriaModulos.getModulo(varAct);
			varActFlujo = (BigDecimal) moduloAct.execute(varProyCopia, proyUmic.get(iteracion - 1).getBloqueCompl(),
					iteracion, proyUmic.get(iteracion - 1).getFechaDesde(), umic, btcUmic, mapVariables, codSubProceso);

			varFactorIct = varActFlujo;
		}

		if (codSubProceso.equals(ConstantsModulos.CTE_PROY_PRIMA)) {

			varProyCopia.get(iteracion - 1).getBloquePrim()
					.setFechaDevengo(varProyPeriodo.get(iteracion - 1).getFechaHasta());
			varProyCopia.get(iteracion - 1).getBloquePrim()
					.setFechaPago(varProyPeriodo.get(iteracion - 1).getFechaHasta());
			varAct = UtilModulos.getVarModulo(mapVariables, CLAVE_VAR_ACT_PRIM, umic, btcUmic,
					ConstantsModulos.CTE_PROY_PRIMA, "04");

			moduloAct = FactoriaModulos.getModulo(varAct);
			varActFlujo = (BigDecimal) moduloAct.execute(varProyCopia, proyUmic.get(iteracion - 1).getBloquePrim(),
					iteracion, proyUmic.get(iteracion - 1).getFechaDesde(), umic, btcUmic, mapVariables, codSubProceso);

			varFactorIct = varActFlujo;
		}

		// Cálculo y validación de las variables de apoyo.
		varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(),
				umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		varCriterInt = UtilModulos.getsetStringRecuperarDefinicionAuxiliar(mapVariables, CLAVE_VAR_CRIINT,
				umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(),
				umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_VA_INTER);

		// Si es válido para la iteración 1 ha de ser válido para las siguientes
		// (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFec);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCritInt(varCriterInt);
		} // Si llega a este punto las validaciones anteriores serán válidas para
			// la iteración inicial, luego serán válidas para las siguientes.

		// Variables auxiliares necesarias para el cálculo
		// varFactorIpc = UtilModulos.getFactorIpc(mapVariables, CLAVE_VAR_FACTOR_IPC,
		// CLAVE_VAR_FFIN, proyUmic.get(iteracion-1).getFechaDesde());
		varFondosUmic = UtilModulos.getValoresLiquidativos(mapVariables, CLAVE_VAR_FONDOS_UMIC, umic);

		if (btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRTIU)) {
			varValoresCurva = UtilModulos.setGetListValoresCurvaTipo(mapVariables, CLAVELISTACURTI,
					ConstantsModulos.CURVA_CLR000U, fcalc);
		} else if (btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRTID)) {
			varValoresCurva = UtilModulos.setGetListValoresCurvaTipo(mapVariables, CLAVELISTACURTI,
					ConstantsModulos.CURVA_CLR000D, fcalc);
		} else {
			varValoresCurva = UtilModulos.setGetListValoresCurvaTipo(mapVariables, CLAVELISTACURTI,
					ConstantsModulos.CURVA_CLR0000, fcalc);
		}

		varDurj = FuncionesAuxiliares.nDias(fcalc, bloqueCorriente.getFechaPago(), varCriterFec);
		if (varDurj == 0) {
			varDurj = 1;
		}

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
		int indicePlazoAnterior = obtenerIndicePlazoAnterior(varValoresCurva, varDurj);

		if (indicePlazoAnterior != -1) {
			plazoAnterior = varValoresCurva.get(indicePlazoAnterior);
			varDurAnt = plazoAnterior.getDiasPlazo();
			varIntAnt = plazoAnterior.getPorcentajeInteres();

			if (indicePlazoAnterior < varValoresCurva.size() - 1) {
				plazoPosterior = varValoresCurva.get(indicePlazoAnterior + 1);
				varDurPost = plazoPosterior.getDiasPlazo();
				varIntPost = plazoPosterior.getPorcentajeInteres();
			}
		} else {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_GN);
		}

		// Calcular el interés a aplicar en base al anterior y al posterior
		// varIctj = FuncionesActualizacionFinanciera.calcularTipoInteres(varCriterInt,
		// varDurAnt, varIntAnt, varDurPost, varIntPost, varDurj);

		// varFactorIct =
		// BigDecimal.ONE.add(varIctj.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));

		// Se calcula varFactorIct ^ 1/12 y se almacena para recuperarlo cuando
		// varFactorIct no cambia
		/*
		 * BigDecimal potencia = (BigDecimal)
		 * mapVariables.get(CLAVE_POTENCIA+varFactorIct); if (potencia == null){
		 * potencia = Util.pow(varFactorIct, ConstantsFunciones.CTE_OPER_1ENTRE12);
		 * mapVariables.put(CLAVE_POTENCIA+varFactorIct, potencia); }
		 */

		// Se realiza el cálculo del sumatorio: {Sumatorio desde 0 hasta
		// varFondosUmic.size-1} varNpartK * varVLPK(tcm + β)
		for (int k = 0; k < varFondosUmic.size(); k++) {
			varCnparticipacion = varFondosUmic.get(k).getCnparticipacion();

			if (varCnparticipacion.equals(BigDecimal.ZERO)) {
				final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
				Incidencia aviso = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(),
						umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(),
						btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(),
						ConstantsFunciones.CTE_COD_ERROR_I005, new Object[] { varFondosUmic.get(k).getKfondo() });
				servicio.almacenarIncidencias(aviso);
				varPrima = umic.getPrimas().getIprimanetaact();
			}

			varVLPK = (BigDecimal) mapVariables.get(CLAVE_VAR_VLPK);

			if (!varCnparticipacion.equals(BigDecimal.ZERO)) {
				if (varVLPK == null) {
					varVLPK = varFondosUmic.get(k).getPkvalor();
					mapVariables.put(CLAVE_VAR_VLPK, varVLPK);
				}
				varVLPK = varVLPK.multiply(BigDecimal.ONE.divide(varFactorIct, ConstantsFunciones.MATH_CONTEXT));
			} else {
				varVLPK = varPrima.multiply(BigDecimal.ONE.divide(varFactorIct, ConstantsFunciones.MATH_CONTEXT));
			}

			if (k == 0 && varFondosUmic.get(k).getCnparticipacion().equals(BigDecimal.ZERO)) {
				varCSP84324K = varVLPK;
			} else {
				varCSP84324K = varVLPK.multiply(varFondosUmic.get(k).getCnparticipacion());
			}
			// Se realiza el cálculo de la fórmula {Sumatorio desde 0 hasta
			// varFondosUmic.size-1} varCSP84324K
			csp84324 = csp84324.add(varCSP84324K);
			//System.out.println("FechaDevengo: "+bloqueCorriente.getFechaDevengo()+"
			// varFactorIct: "+varFactorIct+" varVLPK: "+varVLPK+" csp84324: "+csp84324);
		}

		if (ModuloCSP84324.LOG.isTraceEnabled()) {
			ModuloCSP84324.LOG.trace(
					"Fin función << ModuloCSP84324 >> de la clase ModuloCSP84324, para la iteracion = {} con resultado csp84324 = {}",
					iteracion, csp84324);
		}

		return csp84324;
	}

	/**
	 * Función encargada de recuperar los datos necesarios correspondientes al plazo
	 * anterior.
	 * 
	 * @param lstValCurvaTipo Lista de valoresCurva que se han recuperado
	 * @param varDurJ         Duración requerida
	 * @return valorDevolver
	 */
	private int obtenerIndicePlazoAnterior(final List<ValoresCurvaTipo> lstValCurvaTipo, final Integer varDurJ) {
		// Variables locales
		int valorDevolver = -1;
		// Fin variables locales

		// Para cada elemento de la lista de curvastipo
		if (null != lstValCurvaTipo) {
			int low = 0;
			int high = lstValCurvaTipo.size() - 1;

			while (low <= high) {
				int mid = (low + high) >>> 1;
				ValoresCurvaTipo midVal = lstValCurvaTipo.get(mid);

				if (midVal.getDiasPlazo() <= varDurJ) {
					if (mid == lstValCurvaTipo.size() - 1 || lstValCurvaTipo.get(mid + 1).getDiasPlazo() > varDurJ) {
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

		return valorDevolver;
	}

}