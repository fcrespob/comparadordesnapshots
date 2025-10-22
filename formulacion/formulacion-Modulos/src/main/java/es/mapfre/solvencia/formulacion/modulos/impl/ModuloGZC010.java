package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dao.impl.gbt.AsigCurvaTipoDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.AsigCurvasTipoUOADao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.conversionesBel.GastosReales;
import es.mapfre.solvencia.dominio.gbt.AsigCurvaTipo;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.AsigCurvasTipoUOA;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo GZC010. Clase encargada del cálculo que
 * devuelve la cuantía nominal por Gastos de Administración en cada periodo de
 * la Umic para la base técnica BEL La expresión matemática para su
 * determinación es la siguiente: GZC(010, ZC) = [GTO_UMIC * [Productorio desde
 * j=1 hasta t de (1 + (IPC_GTOS_BEL(j)/100))] + (%GTO_PROV/100) * Bx(tc)] / NPP
 * 
 * @author agonzalezgar
 *
 */
public class ModuloGZC010 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(ModuloGZC010.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_GZC010;
	private static final String CLAVE_NPP = ConstantsModulos.CTE_VAR_NPP;
	private static final String CLAVE_VAR_NPP = CLAVE_NPP.concat(CLAVE_MODULO);
	
	private static final String CLAVEINCRIPC = ConstantsModulos.CTE_VAR_INCRIPC
			.concat(CLAVE_MODULO);
	private static final String CLAVEGTOANUAL = ConstantsModulos.CTE_VAR_GTOANUAL
			.concat(CLAVE_MODULO);
	private static final String CLAVEPCTGTOPROVNPP = ConstantsModulos.CTE_VAR_CLAVEPCTGTOPROVNPP
			.concat(CLAVE_MODULO);
	private static final String CLAVE_CRIT_FEC_DEVE = ConstantsModulos.CTE_CRIT_FEC_DEVE
			.concat(CLAVE_MODULO);
	private static final String CLAVEGTOREALUMIC = ConstantsModulos.CTE_VAR_GTO_REAL
			.concat(CLAVE_MODULO);
	private static final String CLAVELISTACORRUMIC = ConstantsModulos.CTE_LST_CORR_UMIC
			.concat(CLAVE_MODULO);
	
	private static final String CLAVE_KCURVA = ConstantsModulos.CTE_VAR_KCURVA;
	private static final String CLAVE_VAR_KCURVA = CLAVE_KCURVA.concat(CLAVE_MODULO);
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
			if (ModuloGZC010.LOG.isTraceEnabled()) {
				ModuloGZC010.LOG.trace("Inicio de execute en clase ModuloGZC010");
			}

			// Recuperamos los datos que le pasaremos a la función moduloGZC010
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de datos.

			// Invocamos a la función de calculo GZC010
			resultado = moduloGZC010(proyUmic, bloqueCorriente, iteracion,
					fcalc, umic, btcUmic, mapVariables, codSubproceso);

		} catch (Solvencia2Excepcion e) {
			ModuloGZC010.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZC010.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(
					ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloGZC010.LOG.isTraceEnabled()) {
			ModuloGZC010.LOG.trace("Fin de execute en clase ModuloGZC010");
		}

		return resultado;
	}

	/**
	 * Modulo de cálculo que devuelve la cuantía nominal por Gastos de
	 * Administración en cada periodo de la Umic para la base técnica BEL La
	 * expresión matemática para su determinación es la siguiente: GZC(010, ZC)
	 * = [GTO_UMIC * [Productorio desde j=1 hasta t de (1 +
	 * (IPC_GTOS_BEL(j)/100))] + (%GTO_PROV/100) * Bx(tc)] / NPP
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
	 */
	private BigDecimal moduloGZC010(final List<DetalleCorriente> proyUmic,
			final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic,
			final DetalleBaseTecnica btcUmic,
			final Map<String, Object> mapVariables, final String codSubproceso) {

		// Variables locales.
		BigDecimal gzc010 = BigDecimal.ZERO;
		BigDecimal varGtoUmic = BigDecimal.ZERO;
		BigDecimal varPctGtoProv = BigDecimal.ZERO;
		// Fin de las variables locales.

		// Variables finales calculadas para optimizar la ejecución del módulo.
		BigDecimal varNpp;
		GastosReales varGtoRealUmic = null;
		BigDecimal varProyMat = BigDecimal.ZERO;
		String varCriterioFechas;
		Timestamp fecDev = bloqueCorriente.getFechaDevengo();
		// Fin variables locales

		if (ModuloGZC010.LOG.isTraceEnabled()) {
			ModuloGZC010.LOG
					.trace("Inicio función << moduloGZC010 >> de la clase ModuloGZC010, para la iteracion = {}",
							iteracion);
		}
		
		// Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic,
				bloqueCorriente, fcalc, umic, btcUmic);
		
		if (fecDev == null) {
			return BigDecimal.ZERO;
		}
		
		final Object kCurvaMap = (String)mapVariables.get(CLAVE_VAR_KCURVA);
		String kcurva = null;
		if (null == kCurvaMap) {
			
			List<AsigCurvaTipo> aci = getCurvasTipo(btcUmic, umic);
			if (null == aci || aci.isEmpty()) {
				kcurva = "Sin_Curva";
			} else {
				kcurva = aci.get(0).getKcurva();
			}
			
			mapVariables.put(CLAVE_VAR_KCURVA, kcurva);
		} else {
			kcurva = (String)kCurvaMap;
		}
		
		if (kcurva != null 
				&& kcurva.equals("CLR_MA0")) {
			varGtoRealUmic = UtilModulos.getVarGtoRealUmic(
					mapVariables, CLAVEGTOREALUMIC, btcUmic.getBaseTec(), umic
							.getDatosGenerales().getCcanal(), umic
							.getDatosGenerales().getCnegocio(), umic
							.getDatosGenerales().getKramo(), umic.getKey()
							.getKmodalidad(), btcUmic.getFecCierre(), "S");
		} else {
			varGtoRealUmic = UtilModulos.getVarGtoRealUmic(
					mapVariables, CLAVEGTOREALUMIC, btcUmic.getBaseTec(), umic
							.getDatosGenerales().getCcanal(), umic
							.getDatosGenerales().getCnegocio(), umic
							.getDatosGenerales().getKramo(), umic.getKey()
							.getKmodalidad(), btcUmic.getFecCierre(), "N");
		}
		
		/**
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la
		 * umic necesarios para el cálculo que no varían por periodo, así como
		 * las varibales internas que tampoco varían por periodo, y que se
		 * dejarán accesibles para su uso en el subproceso por los siguientes
		 * periodos a calcular.
		 * 
		 * Variables de Apoyo - VarNpp -->
		 * obtenerConfiguracion.recuperarVariableApoyo(NPP) Si VarNpp así
		 * obtenido es nulo ó no se ha recuperado variable, se asignará VarNpp =
		 * 12
		 * 
		 * Variables Módulo - varGtoRealUmic -->
		 * obtenerConfiguracion.recuperarGastosReales(proyUmic(j).bt,
		 * proyUmic(j).ccanal, proyUmic(j).cnegocio, proyUmic(j).kramo,
		 * proyUmic(j).kmodalidad, proyUmic (j).fecCierre) - varGtoUmic =
		 * varGtoRealUmic.gastoUmic - varPctGtoProv= varGtoRealUmic.
		 * pctGastoProv
		 * 
		 * - Se obtendrá la corriente de provisión matemática (PROY_PRV)
		 * previamente calculada en BTI la umic. Para ello se llamará a la
		 * operación: - listaCorrienteUmic =
		 * obtenerDatos.recuperarProyeccion(btcUmic.fecCierre, BTI, cnegocio,
		 * kpoliza, ccanal, ksubpoliza, ccartera, kcertificado, fecCierre,
		 * nsuscri, kmodalidad, kprestación, kajuste, norden, kgarantia,
		 * ctipoaport)
		 * 
		 * - varIpcJ --> obtenerConfiguracion.recuperarIpcFuturo(proyUmic
		 * (j).fecCierre)
		 * 
		 * Para cualquier periodo j se obtendrán los datos necesarios: y se
		 * calculará GZC010 (j) como: GZC010(j) = [varGtoUmic * [Productorio
		 * desde j = 1 hasta t de (1 + (varIpcJ/100))] + (varPctGtoProv / 100) *
		 * varProvMat] / varNpp
		 */

		/**
		 *
		 * - varGtoRealUmic -->
		 * obtenerConfiguracion.recuperarGastosReales(proyUmic(j).bt,
		 * proyUmic(j).ccanal, proyUmic(j).cnegocio, proyUmic(j).kramo,
		 * proyUmic(j).kmodalidad, proyUmic (j).fecCierre) - varGtoUmic =
		 * varGtoRealUmic.gastoUmic - varPctGtoProv= varGtoRealUmic.
		 * pctGastoProv
		 * 
		 * - Se obtendrá la corriente de provisión matemática (PROY_PRV)
		 * previamente calculada en BTI la umic. Para ello se llamará a la
		 * operación: - listaCorrienteUmic =
		 * obtenerDatos.recuperarProyeccion(btcUmic.fecCierre, BTI, cnegocio,
		 * kpoliza, ccanal, ksubpoliza, ccartera, kcertificado, fecCierre,
		 * nsuscri, kmodalidad, kprestación, kajuste, norden, kgarantia,
		 * ctipoaport)
		 */
		
		varGtoUmic = varGtoRealUmic.getGastoPorUmic();
		varPctGtoProv = varGtoRealUmic.getPctGastoProv();
		varNpp = UtilModulos.getVarNpp(mapVariables, CLAVE_VAR_NPP, umic
				.getDatosGenerales().getCcartera(), umic.getDatosGenerales()
				.getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_NPP);
		
		BigDecimal varGtoAnual = UtilModulos.getVarGtoAnual(mapVariables, CLAVEGTOANUAL, CLAVEINCRIPC, varGtoUmic, fecDev, btcUmic.getBt());
		gzc010 = varGtoAnual.divide(varNpp, ConstantsFunciones.MATH_CONTEXT);
		
		if (!varPctGtoProv.equals(BigDecimal.ZERO)) {
	
			/**
			 * Se recuperará el criterio de fechas de la corriente de gastos para la
			 * modalidad, garantía de la umic. Para ello se invocará a la operación:
			 * varCriterioFechas =
			 * obtenerConfiguracion.recuperarCriterioFechas(proyUmic(j).kmodalidad,
			 * proyUmic(j). kgarantia, ‘PROY_GTOS’);
			 * 
			 * - Si varCriterioFechas.FechaDevengo es (FINP Ó FINIR): - varProvMat =
			 * listaCorrienteUmic(j+1).totalFlujoProyeccion.provbtiproy - Si
			 * varCriterioFechas.FechaDevengo es INIP: - varProvMat =
			 * listaCorrienteUmic(j). totalFlujoProyeccion.provbtiproy
			 */
			// Obtención y validación del criterio de fecha.
			varCriterioFechas = UtilModulos.getCriterioFecDev(mapVariables,
					CLAVE_CRIT_FEC_DEVE, umic.getKey().getKmodalidad(), umic
							.getKey().getKgarantia(), umic.getDatosGenerales().getKprestacion(), umic.getDatosAdicionales().getPrestCal());
			
			// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
			if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
				ValidacionesComunesModulos
					.validarVariableDeApoyoVarCriterioFecha(varCriterioFechas);
			}
			// Fin de la obtención y validación del criterio de fecha.
			
			List<DetalleCorriente> listaCorrienteUmic = UtilModulos
					.getListaCorrienteUmic(mapVariables, CLAVELISTACORRUMIC,
							ConstantsModulos.CTE_BTI, btcUmic.getFecCierre(),
							umic.getKey());
			
			if(null == listaCorrienteUmic || listaCorrienteUmic.isEmpty()){
				mapVariables.remove(CLAVELISTACORRUMIC);
				listaCorrienteUmic = UtilModulos
						.getListaCorrienteUmic(mapVariables, CLAVELISTACORRUMIC,
								ConstantsModulos.CTE_BTI_PROY, btcUmic.getFecCierre(),
								umic.getKey());
			}
			
			if (ConstantsModulos.CTE_VAL_FINP.equals(varCriterioFechas) || ConstantsModulos.CTE_VAL_FINIR.equals(varCriterioFechas)) {
				if (iteracion < listaCorrienteUmic.size()) {
					varProyMat = listaCorrienteUmic.get(iteracion).getTotalFlujoProyeccion().getProvbtiproy();
				} else {
					varProyMat = listaCorrienteUmic.get(iteracion - 1).getTotalFlujoProyeccion().getProvbtiproy();
				}
			} else if (ConstantsModulos.CTE_VAL_INIP.equals(varCriterioFechas)) {
				varProyMat = listaCorrienteUmic.get(iteracion - 1).getTotalFlujoProyeccion().getProvbtiproy();
			} else if (ConstantsModulos.CTE_VAL_PLANB.equals(varCriterioFechas)) {
				varProyMat = listaCorrienteUmic.get(iteracion - 1).getTotalFlujoProyeccion().getProvbtiproy();
			}
			
			if (null == varProyMat) {
				varProyMat = BigDecimal.ZERO;
			}
			
			//varPctGtoProvNPP
			BigDecimal varPctGtoProvNPP = UtilModulos.getVarPctGtoProvNPP(mapVariables, CLAVEPCTGTOPROVNPP, varPctGtoProv, varNpp);
			
			gzc010 = gzc010.add(varPctGtoProvNPP.multiply(varProyMat, ConstantsFunciones.MATH_CONTEXT));
		}
		

		if (ModuloGZC010.LOG.isTraceEnabled()) {
			ModuloGZC010.LOG
					.trace("Fin función << moduloGZC010 >> de la clase ModuloGZC010, para la iteracion = {} con resultado gzc010 = {}",
							iteracion, gzc010);
		}

		return gzc010;
	}
	
	public List<AsigCurvaTipo> getCurvasTipo(DetalleBaseTecnica btcUmic, Umic umic) {
		List<AsigCurvaTipo> result = null;
		List<AsigCurvaTipo> curvaTipo = new ArrayList<AsigCurvaTipo>();
		AsigCurvaTipoDao dao = new AsigCurvaTipoDao();
		String pagoU = "N";

		String indicadorKapbel = null;
		try {
			Timestamp fecEfectoSuscripcion = umic.getFechas().getFecinisus();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date;
			date = dateFormat.parse("31/12/2015");
			long time = date.getTime();
			Timestamp fechaBEL = new Timestamp(time);

			if (fechaBEL.before(fecEfectoSuscripcion)) {
				indicadorKapbel = "P";
			} else {
				indicadorKapbel = "A";
			}
		} catch (ParseException e) {
			ModuloGZC010.LOG.error(e.getMessage(), e);
		}

		if (btcUmic.getBaseTec().equals("BELCLR")) {
			AsigCurvaTipo curva = new AsigCurvaTipo();
			curva.setKcurva("CLR0000");
			// result.set(0, curva);
			// result.get(0).setKcurva("CLR0000");
			curvaTipo.add(curva);
			return curvaTipo;

		} else {

			// Para las bases t�cnicas de SCR que no estresen tipos de inter�s, se
			// recuperar� la curva de BEL
			if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMFE)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMMI)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCF)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCI)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRVM)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLFE)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLMI)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRGTO)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEN)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEP)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIN)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIP)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM)
					|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17)
					|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIF17LIR)
					|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_N17LIRIN)
					|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIFF17OCI)
					|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17IF)
					|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_N17CLIR)) {

				result = dao.obtenerCurvaTipo(ConstantsModulos.CTE_BT_BEL, umic.getDatosGenerales().getTipoSubriesgo(),
						umic.getRescates().getRiesgrescI(), pagoU, umic.getDatosGenerales().getKcarterainv(),
						umic.getDatosGenerales().getPb(), indicadorKapbel, umic.getDatosGenerales().getFecCierre());
			} else {
				result = dao.obtenerCurvaTipo(btcUmic.getBaseTec(), umic.getDatosGenerales().getTipoSubriesgo(),
						umic.getRescates().getRiesgrescI(), pagoU, umic.getDatosGenerales().getKcarterainv(),
						umic.getDatosGenerales().getPb(), indicadorKapbel, umic.getDatosGenerales().getFecCierre());
			}

			if (result.size() == 1) {
				if ((result.get(0).getKcurva().equals(ConstantsModulos.CURVA_CLR_MA0)
						|| result.get(0).getKcurva().equals(ConstantsModulos.CURVA_CLRMA0U)
						|| result.get(0).getKcurva().equals(ConstantsModulos.CURVA_CLRMA0D))
						&& (umic.getDatosGenerales().getKramo().equals("152")
								|| umic.getDatosGenerales().getKramo().equals("158")
								|| umic.getDatosGenerales().getKramo().equals("159")
								|| umic.getDatosGenerales().getKramo().equals("151"))
						&& umic.getDatosGenerales().getTipoSubriesgo().equals("INCA")) {
					if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTID)) {
						result.get(0).setKcurva(ConstantsModulos.CURVA_CLRVOLD);
					} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTIU)) {
						result.get(0).setKcurva(ConstantsModulos.CURVA_CLRVOLU);
					} else {
						result.get(0).setKcurva(ConstantsModulos.CURVA_CLR_VOL);
					}
				}
				return result;
			} else {
				boolean encontrado = false;
				int i = 0;
				while (i < result.size() && !encontrado) {

					if ((result.get(i).getKbasetec().equals(btcUmic.getBaseTec())
							|| (result.get(i).getKbasetec().equals(ConstantsModulos.CTE_BT_BEL)
									&& (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMFE)
											|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMMI)
											|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCF)
											|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCI)
											|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRVM)
											|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLFE)
											|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLMI)
											|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC)
											|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRGTO)
											|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEN)
											|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEP)
											|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIN)
											|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIP)
											|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM)
											|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTIU)
											|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTID)
											|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17)
											|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIF17LIR)
											|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_N17LIRIN)
											|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIFF17OCI)
											|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17IF)
											|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_N17CLIR))))
							&& result.get(i).getKriesgo().equals(umic.getDatosGenerales().getTipoSubriesgo())
							&& result.get(i).getKriesgorescate().equals(umic.getRescates().getRiesgrescI())
							&& result.get(i).getKpagounico().equals(pagoU)
							&& result.get(i).getKcarterainv().equals(umic.getDatosGenerales().getKcarterainv())
							&& result.get(i).getKpb().equals(umic.getDatosGenerales().getPb())
							&& result.get(i).getKapbel().equals(indicadorKapbel)) {

						if ((result.get(i).getKcurva().equals(ConstantsModulos.CURVA_CLR_MA0)
								|| result.get(i).getKcurva().equals(ConstantsModulos.CURVA_CLRMA0U)
								|| result.get(i).getKcurva().equals(ConstantsModulos.CURVA_CLRMA0D))
								&& (umic.getDatosGenerales().getKramo().equals("152")
										|| umic.getDatosGenerales().getKramo().equals("158")
										|| umic.getDatosGenerales().getKramo().equals("159"))
								&& umic.getDatosGenerales().getTipoSubriesgo().equals("INCA")) {
							if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTIU)) {
								result.get(i).setKcurva(ConstantsModulos.CURVA_CLRVOLU);
							} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTID)) {
								result.get(i).setKcurva(ConstantsModulos.CURVA_CLRVOLD);
							} else {
								result.get(i).setKcurva(ConstantsModulos.CURVA_CLR_VOL);
							}
						}

						curvaTipo.add(result.get(i));
						encontrado = true;

					}
					i++;
				}
			}
			return curvaTipo;

		}
	}
	
}


