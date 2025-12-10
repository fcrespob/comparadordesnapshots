package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.ComisionesParticipadasCOMDao;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ComisionesParticipadasCOM;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo com002. Módulo de cálculo que devuelve el
 * importe de comisiones a pagar a los agentes en cada punto necesario sobre
 * productos de Rentas.
 *
 * @author NFQ
 *
 */
public class ModuloCOM002 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCOM002.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_COM002;

	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

	private static final String CLAVE_NPP = ConstantsModulos.CTE_VA_NPP;
	private static final String CLAVE_VAR_NPP = CLAVE_NPP.concat(CLAVE_MODULO);

	private static final String CLAVE_LST_PROY = ConstantsModulos.CTE_LST_PROY.concat(CLAVE_MODULO);
	private static final String CLAVE_LST_PRI = ConstantsModulos.CTE_LST_PRI.concat(CLAVE_MODULO);
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
			if (ModuloCOM002.LOG.isTraceEnabled()) {
				ModuloCOM002.LOG.trace("Inicio de execute en clase ModuloCOM002");
			}

			// Recuperamos los datos que le pasaremos a la función moduloCOM002
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.

			// Invocamos a la función de calculo moduloCOM002
			resultado = moduloCOM002(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,
					codSubproceso);

		} catch (Solvencia2Excepcion e) {
			ModuloCOM002.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCOM002.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloCOM002.LOG.isTraceEnabled()) {
			ModuloCOM002.LOG.trace("Fin de execute en clase ModuloCOM002");
		}

		return resultado;

	}

	/**
	 * Módulo de cálculo que devuelve el importe de comisiones a pagar a los agentes
	 * en cada punto necesario sobre productos de Rentas.
	 * 
	 * @param proyUmic
	 * @param bloqueCorriente
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param mapVariables
	 * @return com002
	 */
	private BigDecimal moduloCOM002(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, Map<String, Object> mapVariables,
			String codSubproceso) {
		BigDecimal com002 = BigDecimal.ZERO;
		String varCriterFec;
		BigDecimal varNpp;
		Integer varNumAnualidades = 0;
		BigDecimal varCom = BigDecimal.ZERO;
		String varBx = ConstantsFunciones.CTE_CADENA_VACIA;
		List<ComisionesParticipadasCOM> varDatosComisiones = null;
		List<DetalleCorriente> lstDetaCor = null;
		boolean calcularCom002 = true;
		BigDecimal varPrimas = BigDecimal.ZERO;
		BigDecimal varProvMat = BigDecimal.ZERO;

		if (ModuloCOM002.LOG.isTraceEnabled()) {
			ModuloCOM002.LOG.trace("Inicio función << moduloCOM002 >> de la clase ModuloCOM002, para la iteracion = {}",
					iteracion);
		}

		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(),
				umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		varNpp = UtilModulos.getVarNpp(mapVariables, CLAVE_VAR_NPP, umic.getDatosGenerales().getCcartera(),
				umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_NPP);

		ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFec);

		if (varNpp == null) {
			varNpp = new BigDecimal(12);
		}

		if (bloqueCorriente.getFechaDevengo() != null) {

			if (umic.getDatosGenerales().getCnegocio().equals(ConstantsModulos.CTE_NEGOCIO_INDIVIDUAL)) {
				varNumAnualidades = FuncionesAuxiliares.tc(umic.getFechas().getFecefecini(),
						bloqueCorriente.getFechaDevengo());
			} else {
				varNumAnualidades = FuncionesAuxiliares.tc(umic.getFechas().getFecinisus(),
						bloqueCorriente.getFechaDevengo());
			}
			
			ComisionesParticipadasCOMDao dao = new ComisionesParticipadasCOMDao();
			
			if(umic.getDatosGenerales().getCnegocio().equals(ConstantsModulos.CTE_NEGOCIO_COLECTIVO)) {
				
				varDatosComisiones = dao.obtenerCominisionParticipada(
						umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(),
						umic.getDatosGenerales().getKgarantia(), fcalc, umic.getFechas().getFecinisus());
				
			}else if(umic.getDatosGenerales().getCnegocio().equals(ConstantsModulos.CTE_NEGOCIO_INDIVIDUAL)){
				
				varDatosComisiones = dao.obtenerCominisionParticipada(
						umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(),
						umic.getDatosGenerales().getKgarantia(), fcalc, umic.getFechas().getFecefecini());
			}
			
			if(varDatosComisiones == null || varDatosComisiones.isEmpty()) {
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DS);	
			}
			
			ComisionesParticipadasCOM comision = varDatosComisiones.get(0);
			// varDatosComisiones

			Integer npericomi1 = comision.getnPeriComi1();
			Integer npericomi2 = comision.getnPeriComi2();
			Integer npericomi3 = comision.getnPeriComi3();

			if (0 <= varNumAnualidades && varNumAnualidades < npericomi1) {
				varCom = comision.getpComisiona1();
				varBx = comision.getBaseCalcComi1();
			} else {
				if (comision.getBaseCalcComi2().equals(ConstantsModulos.CTE_COMI_PV)) {
					varCom = comision.getpComisiona2();
					varBx = comision.getBaseCalcComi2();
				} else {
					if (npericomi1 <= varNumAnualidades && varNumAnualidades < (npericomi1 + npericomi2)) {
						varCom = comision.getpComisiona2();
						varBx = comision.getBaseCalcComi2();
					} else if ((npericomi1 + npericomi2) <= varNumAnualidades
							&& varNumAnualidades < (npericomi1 + npericomi2 + npericomi3)) {
						varCom = comision.getpComisiona3();
						varBx = comision.getBaseCalcComi3();
					} else if (varNumAnualidades >= (npericomi1 + npericomi2 + npericomi3)) {
						return com002;
					}
				}
			}

			if (varCom.signum() != 0) {

				/**
				 * - varBx --> se buscará si la base de cálculo es primas o provisión en los
				 * datos del bloque de comisiones de la umic (umic.comisiones. basecalculocomi1,
				 * 2, 3 ) en función de la anualidad en la que esté ubicada el periodo (j).
				 * 
				 * - varCom --> se buscará el % de comisiones en los datos del bloque de
				 * comisiones de la umic (umic.comisiones.pcomisiona1, 2, 3) en función de la
				 * anualidad en la que esté ubicada el periodo (j).
				 */
//				varBx = obtenerBaseCalculoComiPorNPeriComi(umic.getComisiones(), ubicNperiComi);
//				varComi = obtenerComisionesPorNperiComi(umic.getComisiones(), ubicNperiComi);

				/**
				 * Si varBx = PR (Primas) - Se obtendrá la corriente de primas (PROY_PRIMAS )
				 * previamente calculada en BEL para la umic. Para ello se llamará a la
				 * operación: o listaCorrienteUmic =
				 * obtenerDatos.recuperarProyeccion(btcUmic.fecCierre, BEL, cnegocio, kpoliza,
				 * ccanal, ksubpoliza, ccartera, kcertificado, fecCierre, nsuscri, kmodalidad,
				 * kprestación, kajuste, norden, kgarantia, ctipoaport) - varPrimas =
				 * listaCorrienteUmic(j).corrientePrimas.impFlujoNominal
				 * 
				 * - varProvMat (j) = 0 Si varBx = PV (Provisión) - Se evaluará el campo
				 * MESPAGOCOMI de la siguiente manera: - Si MESPAGOCOMI no tiene valor o Se
				 * calcularan comisiones en todos los periodos j, es decir: varProvMat (j) =
				 * listaCorrienteUmic(j). totalFlujoProyeccion. provbtiproy
				 * 
				 * - Si MESPAGOCOMI = "3112" o Se calcularán comisiones solo en el periodo del
				 * año en el que el devengo de la comisión sea 31/12/aaaaa.
				 * 
				 * - Se obtendrá la corriente de provisión matemática (PROY_PRV ) previamente
				 * calculada en BTI para la umic. Para ello se llamará a la operación: o
				 * listaCorrienteUmic = obtenerDatos.recuperarProyeccion(btcUmic.fecCierre, BTI,
				 * cnegocio, kpoliza, ccanal, ksubpoliza, ccartera, kcertificado, fecCierre,
				 * nsuscri, kmodalidad, kprestación, kajuste, norden, kgarantia, ctipoaport) -
				 * varProvMat = listaCorrienteUmic(j).totalFlujoProyeccion.provbtiproy - varComi
				 * = 0 - varPrimas(j) = 0
				 */
				if (ConstantsModulos.CTE_COMI_PR.equals(varBx)) {
					// Se obtendrá la corriente de primas (PROY_PRIMAS ) previamente calculada en
					// BEL para la umic. Para ello se llamará a la operación:
					// listaCorrienteUmic = obtenerDatos.recuperarProyeccion(btcUmic.fecCierre, BEL,
					// cnegocio, kpoliza, ccanal, ksubpoliza, ccartera, kcertificado, fecCierre,
					// nsuscri, kmodalidad, kprestación, kajuste, norden, kgarantia, ctipoaport)
					// varPrimas = listaCorrienteUmic(j).corrientePrimas.impFlujoNominal

					if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL)) {
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_BT_BEL, btcUmic.getFecCierre(), umic.getKey());
					}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17) ){
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_BT_NIIF17, btcUmic.getFecCierre(), umic.getKey());
					}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIF17LIR)){
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_BT_NIF17LIR, btcUmic.getFecCierre(), umic.getKey());
					}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17IF)){
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_BT_NIIF17IF, btcUmic.getFecCierre(), umic.getKey());
					}else if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIFF17OCI)){
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_BT_NIFF17OCI, btcUmic.getFecCierre(), umic.getKey());					
					} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA)) {
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_VAL_BELCOA, btcUmic.getFecCierre(), umic.getKey());
					} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR)) {
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_VAL_BELCLR, btcUmic.getFecCierre(), umic.getKey());
					} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTIU)) {
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_VAL_SCRTIU, btcUmic.getFecCierre(), umic.getKey());
					} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTID)) {
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_VAL_SCRTID, btcUmic.getFecCierre(), umic.getKey());
					} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRGTO)) {
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_VAL_SCRGTO, btcUmic.getFecCierre(), umic.getKey());
					} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMFE)) {
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_VAL_SCRMFE, btcUmic.getFecCierre(), umic.getKey());
					} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMMI)) {
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_VAL_SCRMMI, btcUmic.getFecCierre(), umic.getKey());
					} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCF)) {
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_VAL_SCRMCF, btcUmic.getFecCierre(), umic.getKey());
					} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCI)) {
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_VAL_SCRMCI, btcUmic.getFecCierre(), umic.getKey());
					} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLFE)) {
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_VAL_SCRLFE, btcUmic.getFecCierre(), umic.getKey());
					} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLMI)) {
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_VAL_SCRLMI, btcUmic.getFecCierre(), umic.getKey());
					} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC)) {
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_VAL_SCRINC, btcUmic.getFecCierre(), umic.getKey());
					} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRVM)) {
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_VAL_SCRVM, btcUmic.getFecCierre(), umic.getKey());
					} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEP)) {
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_VAL_SCRAEP, btcUmic.getFecCierre(), umic.getKey());
					} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEN)) {
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_VAL_SCRAEN, btcUmic.getFecCierre(), umic.getKey());
					} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIP)) {
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_VAL_SCRAIP, btcUmic.getFecCierre(), umic.getKey());
					} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIN)) {
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_VAL_SCRAIN, btcUmic.getFecCierre(), umic.getKey());
					} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM)) {
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_VAL_SCRANM, btcUmic.getFecCierre(), umic.getKey());
					} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17AEN)) {
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_VAL_NF17AEN, btcUmic.getFecCierre(), umic.getKey());
					} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17MFE)) {
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_VAL_NF17MFE, btcUmic.getFecCierre(), umic.getKey());
					} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17GTO)) {
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PRI,
								ConstantsModulos.CTE_VAL_NF17GTO, btcUmic.getFecCierre(), umic.getKey());
					}
					
					final BloqueCorriente bloquePrima = lstDetaCor.get(iteracion - 1)
							.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_PRIMA);
					if (null == bloquePrima.getImpFlujoNominal()) {
						bloquePrima.setImpFlujoNominal(BigDecimal.ZERO);
					}
					varPrimas = bloquePrima.getImpFlujoNominal();

					varProvMat = BigDecimal.ZERO;
				} else if (ConstantsModulos.CTE_COMI_PV.equals(varBx)) {

					/**
					 * Se evaluará el campo MESPAGOCOMI de la siguiente manera: - Si MESPAGOCOMI no
					 * tiene valor o Se calcularan comisiones en todos los periodos j - Si
					 * MESPAGOCOMI = "3112" o Se calcularán comisiones solo en el periodo del año en
					 * el que el devengo de la comisión sea 31/12/aaaaa.
					 */
					final String mespagocomi = umic.getComisiones().getMespagocomi();
					final Timestamp fecDevengo = bloqueCorriente.getFechaDevengo();
					calcularCom002 = calcularComisiones(mespagocomi, fecDevengo);
					varPrimas = BigDecimal.ZERO;

					// Se obtendrá la corriente de provisión matemática (PROY_PRV ) previamente
					// calculada en BTI para la umic. Para ello se llamará a la operación:
					lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PROY, ConstantsModulos.CTE_BTI,
							btcUmic.getFecCierre(), umic.getKey());
					
					if(null == lstDetaCor || lstDetaCor.size() == 0){
						mapVariables.remove(CLAVE_LST_PROY);
						lstDetaCor = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PROY, ConstantsModulos.CTE_BTI_PROY, btcUmic.getFecCierre(), umic.getKey());
					}
					
					if(lstDetaCor == null || lstDetaCor.size() == 0){
						varProvMat = BigDecimal.ZERO;
					}else{
						varProvMat = lstDetaCor.get(iteracion - 1).getTotalFlujoProyeccion().getProvbtiproy();
					}
				}

				if (calcularCom002) {
					final BigDecimal varComiDiv100 = varCom.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
					// COM002(j) = ((varComi/100) * varPrimas) + (varComi*0.01) * (varProvMat /
					// varNPP)
					// com002 = varComi.divide(ConstantsFunciones.CTE_OPER_100,
					// ConstantsFunciones.MATH_CONTEXT).multiply(varPrimas).add(varProvMat.divide(varNPP,
					// ConstantsFunciones.MATH_CONTEXT));
					com002 = varComiDiv100.multiply(varPrimas)
							.add(varComiDiv100.multiply(varProvMat.divide(varNpp, ConstantsFunciones.MATH_CONTEXT)));
				}
			}
		}

		// if(varBx.contentEquals())

		if (ModuloCOM002.LOG.isTraceEnabled()) {
			ModuloCOM002.LOG.trace(
					"Fin función << moduloCOM002 >> de la clase ModuloCOM002, para la iteracion = {} con resultado com002 = {}",
					iteracion, com002);
		}
		return com002;
	}

	private boolean calcularComisiones(final String mespagocomi, final Timestamp fecDevengo) {

		boolean calcularCom002 = false;

		Integer diaDevengo = null;
		Integer mesDevengo = null;
		if (fecDevengo != null) {
			diaDevengo = UtilFechas.getDia(fecDevengo);
			mesDevengo = UtilFechas.getMes(fecDevengo);
		}

		// Para cumplir las condiciones anteriores, si devolverá un booleano si se
		// cumplen todas las siguientes condiciones:
		calcularCom002 =

				// Si MESPAGOCOMI no tiene valor o si MESPAGOCOMI es igual a "MENS", se
				// calcularan comisiones en todos los periodos j
				null == mespagocomi || mespagocomi.isEmpty() || mespagocomi.equals("MENS") ||

				// Si MESPAGOCOMI = "3112" se calcularán comisiones solo en el periodo del año
				// en el que el devengo de la comisión sea 31/12/aaaaa.
						(ConstantsModulos.CTE_DIAMES_3112.equals(mespagocomi) && diaDevengo != null && diaDevengo == 31
								&& mesDevengo != null && mesDevengo == 12);

		return calcularCom002;
	}

}