package es.mapfre.solvencia.formulacion.modulos.impl;

/* MODIFICACION : SOLVENCIA II - C�LCULO Y GENERACI�N DE FLUJOS FASE V -  TAR00163093-Modificación_ACTBEL
 * 
 */
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.conversionesBel.ValoresCurvaTipo;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesActualizacionFinanciera;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada de determinar el cálculo de los factores de actualización
 * financiera para la garantía en un periodo dado así como el Flujo Actualizado
 * de dicho periodo para la Base Técnica BEL. La expresión matemática para su
 * determinación es la siguiente: Actfin(fcal,j) = (1 + it(CT))^
 * -(nannos(fcal,j))
 * 
 * @author agonzalezgar
 *
 */
public class ModuloACTBEL2 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloACTBEL2.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_ACTBEL2;

	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

	private static final String CLAVE_VA_INTER = ConstantsModulos.CTE_VA_INTER;
	private static final String CLAVE_VAR_CRIINT = ConstantsModulos.CTE_VA_INTER.concat(CLAVE_MODULO);

	private static final String CLAVELISTACURTI = ConstantsModulos.CTE_LST_CURVA_TIPO;

	// Fin de las variables estáticas usadas para agilizar operaciones.

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
			if (ModuloACTBEL2.LOG.isTraceEnabled()) {
				ModuloACTBEL2.LOG.trace("Inicio de execute en clase ModuloACTBEL2");
			}

			// Recuperamos los datos que le pasaremos a la función moduloACTBEL
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de datos.

			// Invocamos a la función de calculo ACTBEL
			resultado = moduloACTBEL2(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);

		} catch (Solvencia2Excepcion e) {
			ModuloACTBEL2.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloACTBEL2.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloACTBEL2.LOG.isTraceEnabled()) {
			ModuloACTBEL2.LOG.trace("Fin de execute en clase ModuloACTBEL2");
		}

		return resultado;
	}

	/**
	 * El módulo ACTBEL determina el cálculo de los factores de actualización
	 * financiera para la garantía en un periodo dado así como el Flujo
	 * Actualizado de dicho periodo para la Base Técnica BEL
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
	 */
	private BigDecimal moduloACTBEL2(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente,
			final Integer iteracion, final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic,
			final Map<String, Object> mapVariables) {

		// Variables locales
		String varCriterFec = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriterInt = ConstantsFunciones.CTE_CADENA_VACIA;
		List<ValoresCurvaTipo> varValoresCurva = null;
		List<ValoresCurvaTipo> varValoresCurvaIni = null;
		int varDurJ = 0;
		int varDurAnt = 0;
		BigDecimal varIntAnt = BigDecimal.ZERO;
		int varDurPost = 0;
		BigDecimal varIntPost = BigDecimal.ZERO;
		BigDecimal varIct = BigDecimal.ZERO;
		Timestamp varFecJ = null;
		BigDecimal actbel2 = BigDecimal.ZERO;
		Integer maximoDiasPLazo = ConstantsFunciones.CTE_1;
		BigDecimal maximoPorcentajeInteres = BigDecimal.ZERO;
		int elementosLstCurTipo = 0;

		final IObtenerDatos obtenerDatos = FachadaServicios.getObtenerDatos();

		ValoresCurvaTipo plazoAnterior = null;
		ValoresCurvaTipo plazoPosterior = null;

		int indicePlazoAnterior = -1;
		// Fin variables locales

		if (ModuloACTBEL2.LOG.isTraceEnabled()) {
			ModuloACTBEL2.LOG.trace(
					"Inicio función << moduloACTBEL2 >> de la clase ModuloACTBEL2, para la iteracion = {}", iteracion);
		}

		// validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		if (null == bloqueCorriente.getFechaPago()) {
			return actbel2;
		}
		/**
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la
		 * umic necesarios para el cálculo que no varían por periodo, así como
		 * las variables internas que tampoco varían por periodo, y que se
		 * dejarán accesibles para su uso en el subproceso por los siguientes
		 * periodos a calcular. Variables de Apoyo - VarCriterFec <--
		 * obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL) -
		 * VarCriterInt <--
		 * obtenerConfiguracion.recuperarVariableApoyo(CRITER_INTER) - Si alguna
		 * de las variables de apoyo retornadas es nulo se devuelve error
		 * funcional 005 - No se ha encontrado la Variable de Apoyo
		 * &NombreVariableApoyo, finalizando el proceso para la UMIC. Datos de
		 * Configuración: - varValoresCurva =
		 * obtenerConfiguracion.recuperarValoresTipos(fichaProceso.ktipobt,
		 * fcalc, umic.DatosGenerales.carterainv)
		 */
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
		} // Si llega a este punto las validaciones anteriores serán válidas
			// para
			// la iteración inicial, luego serán válidas para las siguientes.
			// Fin de la validación de las variables de apoyo.
			// Datos de Configuración:
			// varValoresCurva =
			// obtenerConfiguracion.recuperarValoresTipos(btcUmic.ktipobt,
			// fcalc,
			// umic.DatosGenerales.carterainv)

		// PYAM0001-TAR00163093-Modificación_ACTBEL-INI

		// NFQ: 13/08/2019
		// A la hora de recuperar los valores de las curvas TI, tenemos que
		// tener en cuenta si estamos en periodo casado o no.
		// 1. Si estamos en periodo casado  usa la curva que recupera de la
		// tabla
		// 2. Si estamos en periodo no casado  usa la CLR000

		//
		String casadoX = ConstantsFunciones.CTE_CADENA_VACIA;

		/*
		 * if (btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRTID)){
		 * varValoresCurvaIni =
		 * UtilModulos.setGetListValoresCurvaTipo(mapVariables,
		 * CLAVELISTACURTI,ConstantsModulos.CURVA_CLR000D,
		 * btcUmic.getFcurvaTi()); } else if
		 * (btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRTIU)) {
		 * varValoresCurvaIni =
		 * UtilModulos.setGetListValoresCurvaTipo(mapVariables,
		 * CLAVELISTACURTI,ConstantsModulos.CURVA_CLR000U,
		 * btcUmic.getFcurvaTi()); } else { varValoresCurvaIni =
		 * UtilModulos.setGetListValoresCurvaTipo(mapVariables,
		 * CLAVELISTACURTI,ConstantsModulos.CURVA_CLR0000,
		 * btcUmic.getFcurvaTi()); }
		 */

		casadoX = obtenerFechasIniFinTramoParaCalculoProvi(btcUmic, bloqueCorriente.getFechaPago());

		if (ConstantsModulos.CTE_N.equals(casadoX)) {
			if (btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRTID)) {
				varValoresCurvaIni = UtilModulos.setGetListValoresCurvaTipo(mapVariables, CLAVELISTACURTI,
						ConstantsModulos.CURVA_CLR000D, btcUmic.getFcurvaTi());
			} else if (btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRTIU)) {
				varValoresCurvaIni = UtilModulos.setGetListValoresCurvaTipo(mapVariables, CLAVELISTACURTI,
						ConstantsModulos.CURVA_CLR000U, btcUmic.getFcurvaTi());
			} else {
				varValoresCurvaIni = UtilModulos.setGetListValoresCurvaTipo(mapVariables, CLAVELISTACURTI,
						ConstantsModulos.CURVA_CLR0000, btcUmic.getFcurvaTi());
			}
		} else {
			varValoresCurvaIni = UtilModulos.setGetListValoresCurvaTipo(mapVariables, CLAVELISTACURTI,
					btcUmic.getCurvaTi(), btcUmic.getFcurvaTi());
		}

		elementosLstCurTipo = varValoresCurvaIni.size();

		if (elementosLstCurTipo != 0) {
			maximoDiasPLazo = varValoresCurvaIni.get(elementosLstCurTipo - 1).getDiasPlazo();
			maximoPorcentajeInteres = varValoresCurvaIni.get(elementosLstCurTipo - 1).getPorcentajeInteres();
		}

		// Para cualquier periodo j
		// varDurJ = ndias(fcalc, proyUmic(j).fecPago, VarCriterFec)
		/**
		 * - Si la diferencia en días es cero, se fuerza el resultado a 1, es
		 * decir: si varDurj = 0, entonces varDurj = 1
		 */
		if (bloqueCorriente.getFechaPago() == null) {
			actbel2 = BigDecimal.ZERO;
		} else {
			varDurJ = FuncionesAuxiliares.nDias(fcalc, bloqueCorriente.getFechaPago(), varCriterFec);
			if (varDurJ == ConstantsFunciones.CTE_0) {
				varDurJ = ConstantsFunciones.CTE_1;
			}
		}
		/**
		 * Si btcUmic.btcalc = 'BEL' y (btcUmic.txt_per_trans = 'S' Y
		 * btcUmic.num_factor_interpo_it_bel <> 0 ): btcUmicROSSP =
		 * (btcUmic.fecCierre,’ROSSP’,claveUmic) Con esta fecha buscaremos en
		 * los datos de la umic el tramo de intenreés en el que se sitúa dicha
		 * fecha, que será el tramo que cumpla las siguientes condiciones:
		 * btcUmicROSSP. fecIniTramox <= btcUmic.fec_curva_ti < btcUmicROSSP.
		 * fecFinTramoX Para el tramo así obtenido recuperamos el porcentaje de
		 * interés ROSSP antes recuperado: varIctROSSP =
		 * btcUmicROSSP.NUM_ITCALC_X Por otro lado, a la curva de valores
		 * recuperada se le aplicará el factor de interpolación BEL
		 * (btcUmic.num_factor_interpo_it_bel), de forma que, para todo j de la
		 * tabla se hará: varValoresCurva(j).porcInteres =
		 * varValoresCurva(j).porcInteres + (btcUmic.num_factor_interpo_it_bel *
		 * (varIctROSSP – varValoresCurva(j).porcInteres
		 * 
		 */

		if (mapVariables.get("varItcROSSP") == null) {
			if ((btcUmic.getBt().equals(ConstantsModulos.CTE_BT_BEL)
					|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_BELCOA)
					|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_BELCLR)
					|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRTIU)
					|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRTID)
					|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRGTO)
					|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRMFE)
					|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRMMI)
					|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRMCF)
					|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRMCI)
					|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRLFE)
					|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRLMI)
					|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRINC)
					|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRVM)
					|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRAEP)
					|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRAEN)
					|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRAIP)
					|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRAIN)
					|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRANM)) && btcUmic.getPerTransBel() != null) {

				ValidacionesComunesModulos.validarDatosBel(btcUmic);
				if (btcUmic.getPerTransBel() && btcUmic.getFactorInterpolInteresesBel().signum() != 0) {
					DetalleBaseTecnica btcUmicROSSP = null;

					if (btcUmic.getBt().equals(ConstantsModulos.CTE_BT_BEL)
							|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRTIU)
							|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRTID)
							|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRGTO)
							|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRMFE)
							|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRMMI)
							|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRMCF)
							|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRMCI)
							|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRLFE)
							|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRLMI)
							|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRINC)
							|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRVM)
							|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRAEP)
							|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRAEN)
							|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRAIP)
							|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRAIN)
							|| btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_SCRANM)) {
						btcUmicROSSP = obtenerDatos.recuperarBTCUmic(btcUmic.getFecCierre(),
								ConstantsModulos.CTE_BT_ROSSP, umic.getKey());
					} else if (btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_BELCOA)) {
						btcUmicROSSP = obtenerDatos.recuperarBTCUmic(btcUmic.getFecCierre(),
								ConstantsModulos.CTE_VAL_BTCOA, umic.getKey());
					} else if (btcUmic.getBt().equals(ConstantsModulos.CTE_VAL_BELCLR)) {
						btcUmicROSSP = obtenerDatos.recuperarBTCUmic(btcUmic.getFecCierre(),
								ConstantsModulos.CTE_VAL_ROSSP, umic.getKey());
					}

					ValidacionesComunesModulos.validarBtcUmicROSSP(btcUmicROSSP);
					Timestamp fCurvaTi = btcUmic.getFcurvaTi();
					BigDecimal varItcROSSP = BigDecimal.ZERO;
					for (int i = 0; i < btcUmicROSSP.getFecInitramo().size()
							&& btcUmicROSSP.getFecInitramo().get(i) != null; i++) {
						if (!(btcUmicROSSP.getFecInitramo().get(i).after(fCurvaTi))
								&& btcUmicROSSP.getFecfintramo().get(i).after(fCurvaTi)) {
							varItcROSSP = btcUmicROSSP.getItcalc().get(i);
						}
					}

					varValoresCurva = copiarValoresCurvaTipo(varValoresCurvaIni);
					for (ValoresCurvaTipo valorC : varValoresCurva) {
						BigDecimal interes = valorC.getPorcentajeInteres()
								.add((BigDecimal.ONE.subtract(btcUmic.getFactorInterpolInteresesBel()))
										.multiply(varItcROSSP.subtract(valorC.getPorcentajeInteres())));
						valorC.setPorcentajeInteres(interes);
					}

					mapVariables.put(CLAVELISTACURTI, varValoresCurva);
					mapVariables.put("varItcROSSP", varItcROSSP);
				}

			} else {
				varValoresCurva = varValoresCurvaIni;
			}
		} else {
			varValoresCurva = varValoresCurvaIni;
		}

		if (varDurJ >= maximoDiasPLazo.intValue()) {
			varIct = maximoPorcentajeInteres;
		} else {
			/**
			 * De entre los valores de tipos almacenados en varValoresCurva, se
			 * buscarán aquellos que cumplen con la siguiente condición: Plazo
			 * anterior: varValoresCurva.diasPlazo --> máximo de entre los
			 * valores con varValoresCurva.diasPlazo < varDurJ recuperando: -
			 * varDurAnt = diasPlazo - varIntAnt = porcentajeInteres Plazo
			 * posterior: varValoresCurva.diasPlazo --> máximo de entre los
			 * valores con varValoresCurva.diasPlazo > varDurJ recuperando: -
			 * varDurPost= diasPlazo - varIntPost= porcentajeInteres
			 */
			// Obtenemos el plazo anterior y el plazo posterior
			indicePlazoAnterior = obtenerIndicePlazoAnterior(varValoresCurva, varDurJ);

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
			 * Ahora se deberá calcualr el interés a aplicar en base al anterior
			 * y al posterior: varIct=calcularTipoInteres(VarCriterInt,
			 * varDurAnt,varIntAnt,varDurPost,varIntPost,varDurJ) varFecJ =
			 * proyUmic(j).fecPago
			 */
			// varIct=calcularTipoInteres(VarCriterInt,
			// varDurAnt,varIntAnt,varDurPost,varIntPost,varDurJ)
			varIct = FuncionesActualizacionFinanciera.calcularTipoInteres(varCriterInt, varDurAnt, varIntAnt,
					varDurPost, varIntPost, varDurJ);
		}

		// varFecJ = periodosUmic(j).fecPago
		varFecJ = bloqueCorriente.getFechaPago();
		// actfinj <- (1 + varIct/100) ^ -(nannos(fcal,varFecJ,varCriterioFec))
		actbel2 = Util.pow(
				BigDecimal.ONE.add(varIct.divide(ConstantsFunciones.CTE_OPER_100, ConstantsFunciones.MATH_CONTEXT)),
				FuncionesAuxiliares.nAnnos(fcalc, varFecJ, varCriterFec).negate());

		if (ModuloACTBEL2.LOG.isTraceEnabled()) {
			ModuloACTBEL2.LOG.trace(
					"Fin función << moduloACTBEL2 >> de la clase ModuloACTBEL2, para la iteracion = {}, con resultado actbel2 = {}",
					iteracion, actbel2);
		}

		return actbel2;
	}

	/**
	 * Función encargada de recuperar los datos necesarios correspondientes al
	 * plazo anterior.
	 * 
	 * @param lstValCurvaTipo
	 *            Lista de valoresCurva que se han recuperado
	 * @param varDurJ
	 *            Duración requerida
	 * @return valorDevolver
	 */
	private int obtenerIndicePlazoAnterior(final List<ValoresCurvaTipo> lstValCurvaTipo, final Integer varDurJ) {
		// Variables locales
		int valorDevolver = -1;
		// Fin variables locales

		if (ModuloACTBEL2.LOG.isTraceEnabled()) {
			ModuloACTBEL2.LOG.trace("Inicio de la función << obtenerIndicePlazoAnterior >> de la clase ModuloACTBEL2");
		}

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

		if (ModuloACTBEL2.LOG.isTraceEnabled()) {
			ModuloACTBEL2.LOG.trace("Fin de la función << obtenerIndicePlazoAnterior >> de la clase ModuloACTBEL2");
		}

		return valorDevolver;
	}

	/**
	 * Función encargada de recuperar los datos necesarios correspondientes al
	 * plazo posterior.
	 * 
	 * @param lstValCurvaTipo
	 *            Lista de valoresCurva que se han recuperado
	 * @param varDurJ
	 *            Duración requerida
	 * @return valorDevolver
	 */
	private ValoresCurvaTipo obtenerPlazoPosterior(final List<ValoresCurvaTipo> lstValCurvaTipo,
			final Integer varDurJ) {
		// Variables locales
		ValoresCurvaTipo valorDevolver = null;
		// Fin variables locales

		if (ModuloACTBEL2.LOG.isTraceEnabled()) {
			ModuloACTBEL2.LOG.trace("Inicio de la función << obtenerPlazoPosterior >> de la clase ModuloACTBEL2");
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

		if (ModuloACTBEL2.LOG.isTraceEnabled()) {
			ModuloACTBEL2.LOG.trace("Fin de la función << obtenerPlazoPosterior >> de la clase ModuloACTBEL2");
		}

		return valorDevolver;
	}

	/**
	 * Función encargada de copiar la lista de ValoresCurvaTipo recuperada a una
	 * nueva lista (para no sobreescribir la lista original en las operaciones)
	 * 
	 * @param valores
	 *            Lista a copiar
	 * @return listaCopiada Lista resultado de la copia
	 */
	private List<ValoresCurvaTipo> copiarValoresCurvaTipo(final List<ValoresCurvaTipo> valores) {
		List<ValoresCurvaTipo> listaCopiada = new ArrayList<ValoresCurvaTipo>();

		for (ValoresCurvaTipo valorC : valores) {
			ValoresCurvaTipo valorNuevo = new ValoresCurvaTipo();
			try {
				PropertyUtils.copyProperties(valorNuevo, valorC);
			} catch (Exception e) {
				ModuloACTBEL2.LOG.error(e.getMessage());
			}
			listaCopiada.add(valorNuevo);
		}

		return listaCopiada;
	}

	// PYAM0001-TAR00163093-Modificación_ACTBEL-INI

	/**
	 * Función encargada de retornar el casado del tramo que cumpla las
	 * siguientes condiciones: 1 - La fecha inicial del tramo sea menor o igual
	 * que la fecha de calculo (fcal) 2 - La fecha de calculo (fcal) sea menor o
	 * igual que la fecha final del tramo
	 * 
	 * @param btiUmic
	 *            Objeto donde se encuentran los datos relacionados con los
	 *            tramos
	 * @param fcalc
	 *            Fecha en la que se inicia el calculo
	 * @return casadoX
	 */
	private static String obtenerFechasIniFinTramoParaCalculoProvi(final DetalleBaseTecnica btcUmic,
			final Timestamp fcalc) {
		// Variables locales
		String resultado = ConstantsFunciones.CTE_CADENA_VACIA;
		// int diasFecIniFCal;
		// int diasFCalFFin;
		boolean noEncontrado = true;
		final Timestamp fecIniTramo[] = { btcUmic.getFecInitramo().get(0), btcUmic.getFecInitramo().get(1),
				btcUmic.getFecInitramo().get(2), btcUmic.getFecInitramo().get(3), btcUmic.getFecInitramo().get(4) };
		final Timestamp fecFinTramo[] = { btcUmic.getFecfintramo().get(0), btcUmic.getFecfintramo().get(1),
				btcUmic.getFecfintramo().get(2), btcUmic.getFecfintramo().get(3), btcUmic.getFecfintramo().get(4) };
		final String casadoX[] = { btcUmic.getSwcasado().get(0), btcUmic.getSwcasado().get(1),
				btcUmic.getSwcasado().get(2), btcUmic.getSwcasado().get(3), btcUmic.getSwcasado().get(4) };
		int longitudVector = 0;
		// Fin variables locales

		try {

			longitudVector = fecIniTramo.length;
			/**
			 * Desde i=0, hasta 4, vamos obteniendo la diferencia en dias entre
			 * la fechaIniTramoX y la fecha del parametro fcalc y por otro lado
			 * la diferencia entre la fecha del parametro fcalc y la fecha hasta
			 */
			for (int i = 0; i < longitudVector && noEncontrado; i++) {
				if (null != fecIniTramo[i] && null != fecFinTramo[i]) {
					// diasFecIniFCal =
					// UtilFechas.diferenciasDeFechas(fecIniTramo[i], fcalc);
					// diasFCalFFin = UtilFechas.diferenciasDeFechas(fcalc,
					// fecFinTramo[i]);

					// Si se cumple la condicion establecida para el tramoX
					// obtenemos el casadoX
					// fecIniTramo <= fcalc < fecFinTramo
					if ((!fcalc.before(fecIniTramo[i]) && fecFinTramo[i].after(fcalc))
							|| ((!fcalc.before(fecIniTramo[i]) && !fecFinTramo[i].before(fcalc))
									&& ((i < longitudVector - 1 && fecIniTramo[i + 1] == null)
											|| i == longitudVector - 1))) {
						resultado = casadoX[i];
						noEncontrado = false;
					}
				}
			}
		} catch (Exception e) {
			ModuloACTBEL2.LOG.error(e.getMessage(), e);
			resultado = ConstantsFunciones.CTE_CADENA_VACIA;
		}
		return resultado;
	}
	// PYAM0001-TAR00163093-Modificación_ACTBEL-FIN
}
