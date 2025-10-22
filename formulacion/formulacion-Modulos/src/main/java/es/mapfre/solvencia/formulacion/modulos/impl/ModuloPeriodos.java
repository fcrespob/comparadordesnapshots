/* MODIFICACION:TAR00400971-NECESIDADES NUEVO SISTEMA DE PROCESOS TÉCNICOS 
   FECHA: 17/12/2018 Se incluye campo KBENCON,SPCOM, y KMODEXT
   AUTOR: INDRA
*/
/**MODIFICACION: MU-2019-066508: 
  FECHA: 20/11/2019   
  DESCRIP: Se saca incidencia si la edad del asegurado a la fecha de calculo supera la edad de las tablas de mortalidad.
*/
package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.s3.internal.Constants;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Fechas;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.OpcionesGeneracion;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.formulacion.util.UtilFechas.Fecha;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion.OrdenAsegurado;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada de implementar el modulo periodos, quedependiendo de la Base
 * Tecnica del proceso en la aplicacion de los criterios de temporalidad y
 * vencimiento para proyecciones que se defina en la tabla de opciones de
 * generacion de flujos por modalidad y garantia, obtendrá como resultado una
 * corriente de fechas (desde/hasta), para que posteriormente se acoplen los
 * distintos calculos de las diversar proyecciones establecidas.
 * 
 * @author agonzalezgar
 *
 */
public class ModuloPeriodos implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloPeriodos.class);

	private static final String CLAVE_VAR_CTE_VFECFINTRAMO = ConstantsModulos.CTE_VFECFINTRAMO
			.concat(ConstantsFactorias.MODULO_PERIODOS);
	private static final String CLAVE_VAR_CTE_VINTCALC = ConstantsModulos.CTE_VINTCALC
			.concat(ConstantsFactorias.MODULO_PERIODOS);

	private static final String CLAVE_LIM_TAR = ConstantsModulos.CTE_LIM_TAR;
	private static final String CLAVE_VAR_LIM_TAR = CLAVE_LIM_TAR.concat(ConstantsFactorias.MODULO_PERIODOS);

	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(ConstantsFactorias.MODULO_PERIODOS);

	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(ConstantsFactorias.MODULO_PERIODOS);

	private static final String CLAVE_VAR_EDAD_CAL = ConstantsModulos.CTE_EDAD_CAL
			.concat(ConstantsFactorias.MODULO_PERIODOS);

	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC
			.concat(ConstantsFactorias.MODULO_PERIODOS);

	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_PERIODOS;
	}

	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la
	 * llamada a la función que realiza los calculos del modulo.
	 */
	@SuppressWarnings("unchecked")
	public final Object execute(final Object... args) throws Solvencia2Excepcion {

		if (ModuloPeriodos.LOG.isTraceEnabled()) {
			ModuloPeriodos.LOG.trace("Inicio de execute en clase ModuloPeriodos");
		}

		Umic umic = null;
		DetalleBaseTecnica btc = null;
		List<DetalleCorriente> lstCorrientes;

		try {
			// Calculo de periodos
			btc = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_BTC];
			umic = (Umic) args[ConstantsModulos.PARAM_UMI_PERIODO];
			final FichaProceso fichaProceso = (FichaProceso) args[ConstantsModulos.PARAM_FCA_PERIODO];
			final Timestamp fecCierre = (Timestamp) args[ConstantsModulos.PARAM_FCA_CIERRE];
			lstCorrientes = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_LST_CORRIEN];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_MAP_VARIABLES_PERI];

			// Llamamos a la función periodos
			periodos(btc, umic, fichaProceso, fecCierre, lstCorrientes, mapVariables);

		} catch (Solvencia2Excepcion e) {
			ModuloPeriodos.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloPeriodos.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloPeriodos.LOG.isTraceEnabled()) {
			ModuloPeriodos.LOG.trace("Fin de execute en clase ModuloPeriodos");
		}

		return null;
	}

	/**
	 * El modulo PERIODOS consiste, dependiendo de la Base Tecnica del proceso en la
	 * aplicacion de los criterios de temporalidad y vencimiento para proyecciones
	 * que se defina en la tabla de opciones de generacion de flujos por modalidad y
	 * garantia. Como resultado obtendremos una corrientes de fechas, desde y hasta,
	 * donde posteriormente se acoplaron los calculos de las distintas proyecciones.
	 *
	 * @param btc           Contiene el tipo de base tecnica
	 * @param umic          Contiene la Umic que se está procesando
	 * @param fcalc         Contiene la fecha a la que se efectúa el cálculo.
	 * @param fecCierre     Contiene la fecha de cierre del proceso.
	 * @param lstCorrientes Corriente de la umic.
	 * @param mapVariables
	 */
	private void periodos(final DetalleBaseTecnica btc, final Umic umic, final FichaProceso fichaProceso,
			final Timestamp fecCierre, final List<DetalleCorriente> lstCorrientes,
			final Map<String, Object> mapVariables) {
		// Variables locales
		final IObtenerDatos servicio = FachadaServicios.getObtenerDatos();
		final IObtenerConfiguracion obtConfig = FachadaServicios.getObtenerConfiguracion();
		final IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
		String vencimiento;
		OpcionesGeneracion opGeneracion = null;
		// Fin variables locales

		if (ModuloPeriodos.LOG.isTraceEnabled()) {
			ModuloPeriodos.LOG.trace(
					"Inicio funcion << periodos >> de la clase ModuloPeriodos, para la entrada fcalc = {}",
					fichaProceso.getFcalc());
		}

		opGeneracion = obtConfig.recuperarOpcionesGeneracionPrestCal(umic.getDatosGenerales().getKmodalidad(),
				umic.getDatosGenerales().getKgarantia(), umic.getDatosGenerales().getKprestacion(), umic.getDatosAdicionales().getPrestCal());
		ValidacionesComunesModulos.validarOpcionesGeneracion(opGeneracion, umic.getDatosGenerales().getKmodalidad(),
				umic.getDatosGenerales().getKgarantia(), umic.getDatosGenerales().getKprestacion());
		if (null == umic.getFechas().getFecefecfin()) {

			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AE);

		}

		if (umic.getFechas().getFecefecfin().before(fichaProceso.getFcalc())) {
			// Si la fecha de vencimiento es menor que la fecha de calculo, no
			// se calculan periodos. Se levanta una excepcion.

			if (umic.getDatosGenerales().getKmodalidad() == 700 || umic.getDatosGenerales().getKmodalidad() == 811
					|| umic.getDatosGenerales().getKmodalidad() == 827
					|| umic.getDatosGenerales().getKmodalidad() == 809
					|| umic.getDatosGenerales().getKmodalidad() == 816
					|| umic.getDatosGenerales().getKmodalidad() == 856
					|| umic.getDatosGenerales().getKmodalidad() == 857) {

				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AX,
						new String[] { umic.getFechas().getFecefecfin().toString(), fichaProceso.getFcalc().toString(),
								umic.getCapitales().getIsaldo().toString() });

			} else {

				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AG, new String[] {
						umic.getFechas().getFecefecfin().toString(), fichaProceso.getFcalc().toString() });

			}
		}
		
		if (fichaProceso.getKuejecucion().equals(ConstantesSolvencia.CTE_UNID_EJEC_SWCOBROCOMISIONES)){
			mapVariables.put("GP_SWCOBROCOM", ConstantesSolvencia.CTE_UNID_EJEC_SWCOBROCOMISIONES);
		}else {
			mapVariables.put("GP_SWCOBROCOM", fichaProceso.getKuejecucion());
		}

		// ktipobt = fichaPeriodos.ktipobt = 'BTI'
		if (ConstantsModulos.CTE_BTI.equals(btc.getBaseTec())) {
			periodosModGAR(btc, opGeneracion, umic, fichaProceso, lstCorrientes, mapVariables);
		}else if(ConstantsModulos.CTE_BTI_PROY.equals(btc.getBaseTec())){
			periodosModGAR(btc, opGeneracion, umic, fichaProceso, lstCorrientes, mapVariables);
		}else {
			/**
			 * • varProyBTI= obternerDatos.recuperarProyeccion con los parámetros: o
			 * btcUmic.fecCierre o BTI o claveUmic o Umic.datosGenerales.kmodalidad o
			 * Umic.datosGenerales.kpoliza o Umic.datosGenerales.ksubpoliza o
			 * Umic.datosGenerales.kcertificado o Umic.datosGenerales.nsuscri o
			 * Umic.datosGenerales.norden o Umic.datosGenerales.kgarantia o
			 * Umic.datosGenerales.kprestacion o Umic.datosGenerales.kajuste o
			 * Umic.datosGenerales.ctipoaport
			 */
			List<DetalleCorriente> lstDetallesBTI = servicio.recuperarProyBTI(fecCierre, umic.getKey());
			if (lstDetallesBTI == null || lstDetallesBTI.isEmpty()) {
				lstDetallesBTI = servicio.recuperarProyBTIPROY(fecCierre, umic.getKey());
			}
			vencimiento = opGeneracion.getVencimiento();
			if (lstDetallesBTI == null || lstDetallesBTI.isEmpty()
					|| ((ConstantsModulos.CTE_BT_BEL.equals(btc.getBaseTec())
							|| ConstantsModulos.CTE_VAL_BELCOA.equals(btc.getBaseTec())
							|| ConstantsModulos.CTE_VAL_BELCLR.equals(btc.getBaseTec())
							|| ConstantsModulos.CTE_VAL_SCRTID.equals(btc.getBaseTec())
							|| ConstantsModulos.CTE_VAL_SCRTIU.equals(btc.getBaseTec())
							|| ConstantsModulos.CTE_VAL_SCRGTO.equals(btc.getBaseTec())
							|| ConstantsModulos.CTE_VAL_SCRMFE.equals(btc.getBaseTec())
							|| ConstantsModulos.CTE_VAL_SCRMMI.equals(btc.getBaseTec())
							|| ConstantsModulos.CTE_VAL_SCRMCF.equals(btc.getBaseTec())
							|| ConstantsModulos.CTE_VAL_SCRMCI.equals(btc.getBaseTec())
							|| ConstantsModulos.CTE_VAL_SCRLFE.equals(btc.getBaseTec())
							|| ConstantsModulos.CTE_VAL_SCRLMI.equals(btc.getBaseTec())
							|| ConstantsModulos.CTE_VAL_SCRINC.equals(btc.getBaseTec())
							|| ConstantsModulos.CTE_VAL_SCRVM.equals(btc.getBaseTec())
							|| ConstantsModulos.CTE_VAL_SCRAEP.equals(btc.getBaseTec())
							|| ConstantsModulos.CTE_VAL_SCRAEN.equals(btc.getBaseTec())
							|| ConstantsModulos.CTE_VAL_SCRAIP.equals(btc.getBaseTec())
							|| ConstantsModulos.CTE_VAL_SCRAIN.equals(btc.getBaseTec())
							|| ConstantsModulos.CTE_VAL_SCRANM.equals(btc.getBaseTec()))
							&& ConstantsModulos.CTE_VAL_TAR.equals(vencimiento))) {
				// Si lstDetallesBTI es vacio
				periodosModGAR(btc, opGeneracion, umic, fichaProceso, lstCorrientes, mapVariables);
			} else {
				// Asignar periodos, solo si lstPeriodosBTI tiene datos
				asignarPeriodos(lstCorrientes, lstDetallesBTI, btc, fecCierre, mapVariables);
			}
		}

		// Almacenamos la proyección
		almacenarDatos.almacenarProyeccion(lstCorrientes);

		if (ModuloPeriodos.LOG.isTraceEnabled()) {
			ModuloPeriodos.LOG.trace("Fin funcion << periodos >> de la clase ModuloPeriodos");
		}

	}

	/**
	 * Una vez recuperados los periodos previamente calculados en BTI, se volcaran
	 * dichos periodos en la estructura de salida PERIODOS.
	 *
	 * @param lstPeriodos Lista de Periodos
	 * @return lstPeriodos
	 */
	private void asignarPeriodos(final List<DetalleCorriente> lstDetalles, final List<DetalleCorriente> lstDetalleBti,
			final DetalleBaseTecnica btc, final Timestamp fecCierre, final Map<String, Object> mapVariables) {

		if (ModuloPeriodos.LOG.isTraceEnabled()) {
			ModuloPeriodos.LOG.trace("Inicio funcion << asignarPeriodos >> de la clase ModuloPeriodos");
		}

		for (DetalleCorriente detalle : lstDetalleBti) {
			detalle.setBt(btc.getBt());
			detalle.setFcierre(fecCierre);

			detalle.setBloqueComi(null);
			detalle.setBloqueCompl(null);
			detalle.setBloqueFall(null);
			detalle.setBloqueGto(null);
			detalle.setBloquePrim(null);
			detalle.setBloqueRte(null);
			detalle.setBloqueVida(null);
			detalle.setTotalFlujoProyeccion(null);
			if (detalle.getBt().equals(ConstantsModulos.CTE_BT_BEL)
					|| detalle.getBt().equals(ConstantsModulos.CTE_VAL_BELCOA)
					|| detalle.getBt().equals(ConstantsModulos.CTE_VAL_BELCLR)
					|| detalle.getBt().equals(ConstantsModulos.CTE_VAL_SCRTIU)
					|| detalle.getBt().equals(ConstantsModulos.CTE_VAL_SCRTID)
					|| detalle.getBt().equals(ConstantsModulos.CTE_VAL_SCRGTO)
					|| detalle.getBt().equals(ConstantsModulos.CTE_VAL_SCRMFE)
					|| detalle.getBt().equals(ConstantsModulos.CTE_VAL_SCRMMI)
					|| detalle.getBt().equals(ConstantsModulos.CTE_VAL_SCRMCF)
					|| detalle.getBt().equals(ConstantsModulos.CTE_VAL_SCRMCI)
					|| detalle.getBt().equals(ConstantsModulos.CTE_VAL_SCRLFE)
					|| detalle.getBt().equals(ConstantsModulos.CTE_VAL_SCRLMI)
					|| detalle.getBt().equals(ConstantsModulos.CTE_VAL_SCRINC)
					|| detalle.getBt().equals(ConstantsModulos.CTE_VAL_SCRVM)
					|| detalle.getBt().equals(ConstantsModulos.CTE_VAL_SCRAEP)
					|| detalle.getBt().equals(ConstantsModulos.CTE_VAL_SCRAEN)
					|| detalle.getBt().equals(ConstantsModulos.CTE_VAL_SCRAIP)
					|| detalle.getBt().equals(ConstantsModulos.CTE_VAL_SCRAIN)
					|| detalle.getBt().equals(ConstantsModulos.CTE_VAL_SCRANM)) {
				detalle.setIntfeccal(BigDecimal.ZERO);
			} else {
				detalle.setIntfeccal(
						calcularIntFecDesde(btc, detalle.getFechaDesde(), mapVariables, lstDetalles.size() + 1));
			}

			// Para las bases técnicas indicadas, únicamente realizaremos los cálculos del
			// primer año.
			if (btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCF)
					|| btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCI)) {
				Timestamp varFecProxAno = UtilFechas.incrAnyo(btc.getFecCierre(), ConstantsModulos.CTE_INT_1);
				if (!varFecProxAno.after(detalle.getFechaDesde())) {
					return;
				}
			}

			// Seteamos el elemento en la estructura de salida y no en la
			// recuperada, dicha estructura de salida debe estar vacia
			lstDetalles.add(detalle);
		}

		if (ModuloPeriodos.LOG.isTraceEnabled()) {
			ModuloPeriodos.LOG.trace("Fin Inicio funcion << asignarPeriodos >> de la clase ModuloPeriodos");
		}

	}

	/**
	 * Función encargada de obtener la ffinvitalicia para una umic de tipo VITALICIA
	 * 
	 * @param umic    datos de la umic
	 * @param btcUmic datos del detalle de la base tecnica
	 * @return
	 */
	private Timestamp obtenerFechaFinPeriodos(final Umic umic, final DetalleBaseTecnica btcUmic) {
		// Variables locales
		Timestamp fechaFin = null;
		Integer varAnoNac1 = ConstantsFunciones.CTE_0;
		Integer varAnoNac2 = ConstantsFunciones.CTE_0;
		Integer varAnoNac3 = ConstantsFunciones.CTE_0;
		Integer varAnoNac4 = ConstantsFunciones.CTE_0;
		Integer varEdadMax1 = ConstantsFunciones.CTE_0;
		Integer varEdadMax2 = ConstantsFunciones.CTE_0;
		Integer varEdadMax3 = ConstantsFunciones.CTE_0;
		Integer varEdadMax4 = ConstantsFunciones.CTE_0;
		Timestamp varFecSusc = null;
		Integer varEdadCalc1 = ConstantsFunciones.CTE_0;
		Integer varEdadCalc2 = ConstantsFunciones.CTE_0;
		Integer varEdadCalc3 = ConstantsFunciones.CTE_0;
		Integer varEdadCalc4 = ConstantsFunciones.CTE_0;
		String criterioEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		int incrAnio = 0;
		final IObtenerConfiguracion obtConfi = FachadaServicios.getObtenerConfiguracion();
		// Fin variables locales

		/**
		 * Se invocará al servicio obtenerConfiguracion.recuperarEdadMax con los
		 * parámetros: varAnoNac1 = Año (umic.asegurados.fnacAseg1); - varEdadMax1 =
		 * obtenerConfiguracion.recuperarEdadMax (umic.datosgenerales.fecCierre,
		 * btcUmic.tabla1Aseg1, varAnoNac1, btcUmic.itcalc1, umic.baseTecIni.
		 * psobremort, umic.baseTecIni. priesgo) Si umic.asegurados.fnacAseg2 es no
		 * nulo, se hará: varAnoNac2 = Año (umic.asegurados.fnacAseg2); - Si
		 * btcUmic.tabla1Aseg1 <> btcUmic.tabla1Aseg2 - varEdadMax2 =
		 * obtenerConfiguracion.recuperarEdadMax (umic.datosgenerales.fecCierre,
		 * btcUmic.tabla1Aseg2, varAnoNac2, btcUmic.itcalc1, umic.baseTecIni.
		 * psobremort, umic.baseTecIni. priesgo)
		 * 
		 * - Si btcUmic.tabla1Aseg1 = btcUmic.tabla1Aseg2 - varEdadMax2 = varEdadMax1
		 * 3.7.4 ffinvitalicia
		 * 
		 * Una vez obtendida la/s EdadMax de los asegurados, se invocará a la función:
		 * 
		 * ffinvitalicia (fnac, EdadMax) para hallar la fecha fin de los periodos. donde
		 * fnac es la fecha de nacimiento del asegurado en la UMIc, es decir:
		 * 
		 * fnac = umic.asegurados.fnacAseg1 de forma que: ffinvitalicia = add(fnac.YEAR,
		 * EdadMax) varFecSusc = umic.fechas.fecinisus Si umic.asegurados.fnacAseg2 es
		 * nulo se hará: - varEdadCalc1 = entero(nedad(umic.fechas.fecinisus,
		 * umic.asegurados.fnacAseg1, VarCriterEdad)) - Ffinvitalicia1 = varFecSusc +
		 * Años(varEdadMax1 - varEdadCalc1)  se añaden (varEdadMax1 - varEdadCalc1)
		 * años a la fecha de suscripción. Si umic.asegurados.fnacAseg2 es NO nulo se
		 * hará: - varEdadCalc 1= entero(nedad(umic.fechas.fecinisus,
		 * umic.asegurados.fnacAseg1, VarCriterEdad)) - Ffinvitalicia1 = varFecSusc +
		 * Años (varEdadMax1 - varEdadCalc1) - varEdadCalc2=
		 * entero(nedad(umic.fechas.fecinisus, umic.asegurados.fnacAseg2,
		 * VarCriterEdad)) - Ffinvitalicia2= varFecSusc + Años(varEdadMax2 –
		 * varEdadCalc2) Se establecerá la fecha fin como la mayor de las fechas así
		 * halladas, es decir: Ffinvitalicia = mayor (Ffinvitalicia1, Ffinvitalicia2)
		 * 
		 */
		varAnoNac1 = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1());

		if (varAnoNac1 < ConstantsModulos.CTE_ANNO_MIN) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC,
					new String[] { varAnoNac1.toString(), "año nacimiento " + ConstantsModulos.CTE_ASEG_1 });
		}
		criterioEdad = (String) obtConfi.recuperarDefinicionAuxiliar(umic.getDatosGenerales().getCcartera(),
				umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(),
				ConstantsModulos.CTE_VA_CRIT_EDA);
		ValidacionesComunesModulos.validarCriterioEdadRecuperado(criterioEdad, umic.getDatosGenerales().getKmodalidad(),
				umic.getDatosGenerales().getKgarantia());
		varFecSusc = umic.getFechas().getFecinisus();

		if (btcUmic.getTablacalc1aseg1() == null || Integer.valueOf(btcUmic.getTablacalc1aseg1()) == 0) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AD,
					new String[] { Util.errorString(btcUmic.getTablacalc1aseg2()), ConstantsModulos.CTE_ASEG_1 });
		}

		varEdadMax1 = obtConfi.recuperarEdadMax(umic, btcUmic, String.valueOf(varAnoNac1),
				umic.getAsegurados().getCsexAseg1(), OrdenAsegurado.ASEG1);

		ValidacionesComunesModulos.validarEdadMax(varEdadMax1, umic.getDatosGenerales().getKmodalidad(),
				umic.getDatosGenerales().getKgarantia());

		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		varEdadCalc1 = FuncionesAuxiliares.nEdad(varFecSusc, umic.getAsegurados().getFnacAseg1(), criterioEdad,
				umic.getRentas().getFecIni(), varEdifer).intValue();

		if (null != umic.getAsegurados().getFnacAseg2()) {
			varAnoNac2 = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg2());
			if (varAnoNac2 < ConstantsModulos.CTE_ANNO_MIN) {
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC,
						new String[] { varAnoNac2.toString(), "año nacimiento " + ConstantsModulos.CTE_ASEG_2 });
			}
			if (!btcUmic.getTablacalc1aseg1().equals(btcUmic.getTablacalc1aseg2())) {
				if (btcUmic.getTablacalc1aseg2() == null || Integer.valueOf(btcUmic.getTablacalc1aseg2()) == 0) {
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AD, new String[] {
							Util.errorString(btcUmic.getTablacalc1aseg2()), ConstantsModulos.CTE_ASEG_2 });
				}
				varEdadMax2 = obtConfi.recuperarEdadMax(umic, btcUmic, String.valueOf(varAnoNac2),
						umic.getAsegurados().getCsexAseg2(), OrdenAsegurado.ASEG2);
				ValidacionesComunesModulos.validarEdadMax(varEdadMax2, umic.getDatosGenerales().getKmodalidad(),
						umic.getDatosGenerales().getKgarantia());
			} else {
				varEdadMax2 = varEdadMax1;
			}

			varEdadCalc2 = FuncionesAuxiliares.nEdad(varFecSusc, umic.getAsegurados().getFnacAseg2(), criterioEdad,
					umic.getRentas().getFecIni(), varEdifer).intValue();

			incrAnio = Math.max(varEdadMax1 - varEdadCalc1, varEdadMax2 - varEdadCalc2);
			fechaFin = UtilFechas.incrAnyo(varFecSusc, incrAnio);
			
			if (null != umic.getAsegurados().getFnacAseg3()) {
				varAnoNac3 = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg3());
				if (varAnoNac3 < ConstantsModulos.CTE_ANNO_MIN) {
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC,
							new String[] { varAnoNac3.toString(), "año nacimiento " + ConstantsModulos.CTE_ASEG_3 });
				}
				if (!btcUmic.getTablacalc1aseg2().equals(btcUmic.getTablacalc1aseg3())) {
					if (btcUmic.getTablacalc1aseg3() == null || Integer.valueOf(btcUmic.getTablacalc1aseg3()) == 0) {
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AD, new String[] {
								Util.errorString(btcUmic.getTablacalc1aseg3()), ConstantsModulos.CTE_ASEG_3 });
					}
					varEdadMax3 = obtConfi.recuperarEdadMax(umic, btcUmic, String.valueOf(varAnoNac3),
							umic.getAsegurados().getCsexAseg3(), OrdenAsegurado.ASEG3);
					ValidacionesComunesModulos.validarEdadMax(varEdadMax3, umic.getDatosGenerales().getKmodalidad(),
							umic.getDatosGenerales().getKgarantia());
				} else {
					varEdadMax3 = varEdadMax2;
				}

				varEdadCalc3 = FuncionesAuxiliares.nEdad(varFecSusc, umic.getAsegurados().getFnacAseg3(), criterioEdad,
						umic.getRentas().getFecIni(), varEdifer).intValue();

				incrAnio = Math.max(varEdadMax2 - varEdadCalc2, varEdadMax3 - varEdadCalc3);
				fechaFin = UtilFechas.incrAnyo(varFecSusc, incrAnio);

			}
			
			if (null != umic.getAsegurados().getFnacAseg4()) {
				varAnoNac4 = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg4());
				if (varAnoNac4 < ConstantsModulos.CTE_ANNO_MIN) {
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC,
							new String[] { varAnoNac4.toString(), "año nacimiento " + ConstantsModulos.CTE_ASEG_4 });
				}
				if (!btcUmic.getTablacalc1aseg3().equals(btcUmic.getTablacalc1aseg4())) {
					if (btcUmic.getTablacalc1aseg4() == null || Integer.valueOf(btcUmic.getTablacalc1aseg4()) == 0) {
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AD, new String[] {
								Util.errorString(btcUmic.getTablacalc1aseg4()), ConstantsModulos.CTE_ASEG_4 });
					}
					varEdadMax4 = obtConfi.recuperarEdadMax(umic, btcUmic, String.valueOf(varAnoNac4),
							umic.getAsegurados().getCsexAseg4(), OrdenAsegurado.ASEG4);
					ValidacionesComunesModulos.validarEdadMax(varEdadMax4, umic.getDatosGenerales().getKmodalidad(),
							umic.getDatosGenerales().getKgarantia());
				} else {
					varEdadMax4 = varEdadMax3;
				}

				varEdadCalc4 = FuncionesAuxiliares.nEdad(varFecSusc, umic.getAsegurados().getFnacAseg4(), criterioEdad,
						umic.getRentas().getFecIni(), varEdifer).intValue();

				incrAnio = Math.max(varEdadMax3 - varEdadCalc3, varEdadMax4 - varEdadCalc4);
				fechaFin = UtilFechas.incrAnyo(varFecSusc, incrAnio);

			}
	
		} else {
//INI-966508
			if (varEdadCalc1 > varEdadMax1) {
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DR,
						new String[] {varEdadCalc1.toString(),varEdadMax1.toString() });
			}
			else{ 
//FIN-966508			
			fechaFin = UtilFechas.incrAnyo(varFecSusc, varEdadMax1 - varEdadCalc1);
				}
		}

		return fechaFin;
	}

	/**
	 * El subproceso PERIODOS bajo una base tecnica inicial, consiste en la
	 * aplicacion de los criterios de temporalidad y vencimiento para proyecciones
	 * que se defina en la tabla de opciones de generacion de flujos por modalidad y
	 * garantia. Como resultado obtendremos una corrientes de fechas, desde y hasta,
	 * donde posteriormente se acoplaran los calculos de las distintas proyecciones.
	 * 
	 * @param btc           DetalleBaseTecnica
	 *
	 * @param opGeneracion  Contiene los datos del proceso necesarios para su
	 *                      ejecución
	 * @param umic          Contiene la umic que se está procesando
	 * @param fcalc         Contiene la fecha a la que se efectúa el cálculo
	 * @param fecCierre     Contiene la fecha de cierre del proceso.
	 * @param lstCorrientes Corriente de la umic.
	 */
	private void periodosModGAR(final DetalleBaseTecnica btc, final OpcionesGeneracion opGeneracion, final Umic umic,
			final FichaProceso fichaProceso, final List<DetalleCorriente> lstCorrientes,
			final Map<String, Object> mapVariables) {
		// Variables locales
		Timestamp fechaFinPeriodos = null;
		String vencimiento = ConstantsFunciones.CTE_CADENA_VACIA;
		Timestamp ffinvitalicia = null;
		// Fin variables locales

		if (ModuloPeriodos.LOG.isTraceEnabled()) {
			ModuloPeriodos.LOG.trace(
					"Inicio funcion << periodosModGAR >> de la clase ModuloPeriodos, para la entrada fcalc = {} ",
					fichaProceso.getFcalc());
		}

		// Validamos el vencimiento de los periodos
		ValidacionesFlujoPeriodos.validacionVencimientoPeriodos(opGeneracion.getVencimiento());

		vencimiento = opGeneracion.getVencimiento();
		if (umic.getFechas().getFecefecfin() == null) {
			// Si la fecha de vencimiento es nula se levanta una excepcion
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AE);
		}

		if (umic.getFechas().getFecefecfin().before(fichaProceso.getFcalc())) {
			// Si la fecha de vencimiento es menor que la fecha de calculo, no
			// se calculan periodos. Se levanta una excepcion.

			if (umic.getDatosGenerales().getKmodalidad() == 700 || umic.getDatosGenerales().getKmodalidad() == 811
					|| umic.getDatosGenerales().getKmodalidad() == 827
					|| umic.getDatosGenerales().getKmodalidad() == 809
					|| umic.getDatosGenerales().getKmodalidad() == 816
					|| umic.getDatosGenerales().getKmodalidad() == 856
					|| umic.getDatosGenerales().getKmodalidad() == 857) {

				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ,
						new String[] { umic.getFechas().getFecefecfin().toString(), fichaProceso.getFcalc().toString(),
								umic.getCapitales().getIsaldo().toString() });

			} else {

				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AG, new String[] {
						umic.getFechas().getFecefecfin().toString(), fichaProceso.getFcalc().toString() });

			}
		}

		Fecha fecEfecFinu = UtilFechas.getFecha(umic.getFechas().getFecefecfin());

		// VctoPeridos
		if (ConstantsModulos.CTE_VAL_VENCI.equals(vencimiento) || ConstantsModulos.CTE_VAL_TAR.equals(vencimiento)) {
			// Si el año de (umic.fechas.fecefecfin) = 9999 se tratará como si
			// fuese una prestación vitalicia, sino no se tomara el campo
			// umic.fechas.fecefecfin tal cual
			if (fecEfecFinu.getAnio() == ConstantsFunciones.CTE_9999.intValue()) {
				ffinvitalicia = obtenerFechaFinPeriodos(umic, btc);
				vencimiento = ConstantsModulos.CTE_VAL_VITAL;
			}
		} else if (ConstantsModulos.CTE_VAL_VITAL.equals(vencimiento)) {
			ffinvitalicia = obtenerFechaFinPeriodos(umic, btc);
		}

		fechaFinPeriodos = writeVariablefFinPeriodos(vencimiento, umic, ffinvitalicia, btc, mapVariables,
				fichaProceso.getFcalc());

		// Para las bases técnicas indicadas, únicamente realizaremos los cálculos del
		// primer año.
		if (btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCF)
				|| btc.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCI)) {
			Timestamp varFecProxAno = UtilFechas.incrAnyo(btc.getFecCierre(), ConstantsModulos.CTE_INT_1);
			if (varFecProxAno.before(fechaFinPeriodos)) {
				fechaFinPeriodos = varFecProxAno;
			}
		}
		// Validamos la periodicidad
				 if ((fichaProceso.getKsistema().equals(ConstantesSolvencia.CTE_SISTEMA)
						&& fichaProceso.getKprotecnico().equals(ConstantesSolvencia.CTE_PROYECTO_TECNICO)
						&& fichaProceso.getKuejecucion().equals(ConstantesSolvencia.CTE_UMIC_EJECUCION))){
					
					if (ConstantsModulos.CTE_VAL_RENO.equals(opGeneracion.getPeriodicidad())
							|| ConstantsModulos.CTE_VAL_REVEN.equals(opGeneracion.getPeriodicidad())) {
						calcularPeriodosRenovacion(fechaFinPeriodos, fichaProceso.getFcalc(), umic, btc.getBaseTec(),
								lstCorrientes, opGeneracion.getPeriodicidad(), mapVariables, btc);
					} else if (ConstantsModulos.CTE_VAL_MENSU.equals(opGeneracion.getPeriodicidad())) {
						calcularPeriodosMensuales(fechaFinPeriodos, fichaProceso.getFcalc(), umic, btc.getBaseTec(),
								lstCorrientes, mapVariables, btc);
					} else if (ConstantsModulos.CTE_VAL_MNAT.equals(opGeneracion.getPeriodicidad())) {
						calcularPeriodosMesesNaturales(fechaFinPeriodos, fichaProceso.getFcalc(), umic, btc.getBaseTec(),
								lstCorrientes, mapVariables, btc);
					}
					
				}
				else if(fichaProceso.getKsistema().equals(ConstantesSolvencia.CTE_SISTEMA)
					    && fichaProceso.getKprotecnico().equals(ConstantesSolvencia.CTE_PROCESO_TECNICO_NIIF17)
					    && fichaProceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTI5)) {
					ValidacionesFlujoPeriodos.validacionPeriodicidad(opGeneracion.getPeriodicidad());
					calcularPeriodosPrimas(fechaFinPeriodos, fichaProceso.getFcalc(), umic, btc.getBaseTec(), lstCorrientes,
							opGeneracion.getPeriodicidad(), mapVariables, btc);
				} 
				else {
					if (ConstantsModulos.CTE_VAL_RENO.equals(opGeneracion.getPeriodicidad())
							|| ConstantsModulos.CTE_VAL_REVEN.equals(opGeneracion.getPeriodicidad())) {
						calcularPeriodosRenovacion(fechaFinPeriodos, fichaProceso.getFcalc(), umic, btc.getBaseTec(),
								lstCorrientes, opGeneracion.getPeriodicidad(), mapVariables, btc);
					} else if (ConstantsModulos.CTE_VAL_MENSU.equals(opGeneracion.getPeriodicidad())) {
						calcularPeriodosMensuales(fechaFinPeriodos, fichaProceso.getFcalc(), umic, btc.getBaseTec(),
								lstCorrientes, mapVariables, btc);
					} else if (ConstantsModulos.CTE_VAL_MNAT.equals(opGeneracion.getPeriodicidad())) {
						calcularPeriodosMesesNaturales(fechaFinPeriodos, fichaProceso.getFcalc(), umic, btc.getBaseTec(),
								lstCorrientes, mapVariables, btc);
					}
				}


		if (ModuloPeriodos.LOG.isTraceEnabled()) {
			ModuloPeriodos.LOG.trace("Fin funcion << periodosModGAR >> de la clase ModuloPeriodos");
		}

	}

	/**
	 * Funcióen encargada de establecer una determinada fecha como fecha fin de los
	 * periodos, en función del vencimiento del periodo, los criterios son los
	 * siguientes: - Si vctoPeriodos = Vencimiento fFinPeriodos =
	 * umic.fechas.fecefecfin - Si vctoPeriodos = Vencimiento Vitalicia fFinPeriodos
	 * = fecfinvitalicia (salida de actividad ffinVitalicia)
	 * 
	 * @param venPeriodos
	 * @param fecEfecFin
	 * @param ffinvitalicia
	 * @return
	 */
	private Timestamp writeVariablefFinPeriodos(final String venPeriodos, final Umic umic,
			final Timestamp ffinvitalicia, final DetalleBaseTecnica btc, final Map<String, Object> mapVariables,
			final Timestamp fcalc) {
		// Variables locales
		Timestamp fechaFinPeriodos = null;
		BigDecimal varLimPeriodos = BigDecimal.ZERO;
		BigDecimal varLimPeriodosM = BigDecimal.ZERO;
		String varCriterEdad;
		String varCriterFec;
		Integer varEdadMax;
		Timestamp varAntRenova = null;
		Timestamp varProxRenova = null;
		Timestamp varFechaEfecto = null;
		BigDecimal varEdadCalc;
		Integer varEdifer;
		Integer varAnoNac;
		BigDecimal varFracc0;
		BigDecimal varEdadProxRenova;
		// Fin variables locales

		if (ModuloPeriodos.LOG.isTraceEnabled()) {
			ModuloPeriodos.LOG.trace("Inicio de función <<  writeVariablefFinPeriodos >> de la clase ModuloPeriodos");
		}

		if (ConstantsModulos.CTE_VAL_VITAL.equals(venPeriodos)) {
			fechaFinPeriodos = ffinvitalicia;
		} else if (ConstantsModulos.CTE_VAL_VENCI.equals(venPeriodos)) {
			fechaFinPeriodos = umic.getFechas().getFecefecfin();
		} else if (ConstantsModulos.CTE_VAL_TAR.equals(venPeriodos)) {
			if ((!ConstantsModulos.CTE_BT_BEL.equals(btc.getBaseTec()))
					&& (!ConstantsModulos.CTE_VAL_BELCOA.equals(btc.getBaseTec()))
					&& (!ConstantsModulos.CTE_VAL_BELCLR.equals(btc.getBaseTec()))
					&& (!ConstantsModulos.CTE_VAL_SCRTIU.equals(btc.getBaseTec()))
					&& (!ConstantsModulos.CTE_VAL_SCRTID.equals(btc.getBaseTec()))
					&& (!ConstantsModulos.CTE_VAL_SCRAEP.equals(btc.getBaseTec()))
					&& (!ConstantsModulos.CTE_VAL_SCRAEN.equals(btc.getBaseTec()))
					&& (!ConstantsModulos.CTE_VAL_SCRAIP.equals(btc.getBaseTec()))
					&& (!ConstantsModulos.CTE_VAL_SCRAIN.equals(btc.getBaseTec()))
					&& (!ConstantsModulos.CTE_VAL_SCRANM.equals(btc.getBaseTec()))
					&& (!ConstantsModulos.CTE_VAL_SCRGTO.equals(btc.getBaseTec()))
					&& (!ConstantsModulos.CTE_VAL_SCRINC.equals(btc.getBaseTec()))
					&& (!ConstantsModulos.CTE_VAL_SCRMFE.equals(btc.getBaseTec()))
					&& (!ConstantsModulos.CTE_VAL_SCRMMI.equals(btc.getBaseTec()))
					&& (!ConstantsModulos.CTE_VAL_SCRLMI.equals(btc.getBaseTec()))
					&& (!ConstantsModulos.CTE_VAL_SCRLFE.equals(btc.getBaseTec()))
					&& (!ConstantsModulos.CTE_VAL_SCRVM.equals(btc.getBaseTec()))
					&& (!ConstantsModulos.CTE_VAL_SCRMCF.equals(btc.getBaseTec()))
					&& (!ConstantsModulos.CTE_VAL_SCRMCI.equals(btc.getBaseTec()))) {
				fechaFinPeriodos = umic.getFechas().getFecefecfin();
			} else {
				final Integer ccartera = umic.getDatosGenerales().getCcartera();
				final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
				final Integer kgarantia = umic.getDatosGenerales().getKgarantia();

				varLimPeriodos = UtilModulos.getVarLimPeriodos(mapVariables, CLAVE_VAR_LIM_TAR, ccartera, kmodalidad,
						kgarantia, btc.getBaseTec(), CLAVE_LIM_TAR);
				varLimPeriodosM = varLimPeriodos.multiply(new BigDecimal(ConstantsFunciones.CTE_12));
				if (!UtilModulos.compararTresFechasMenorIgual(btc.getFecCierre(), umic.getFechas().getFecefecfin(),
						UtilFechas.incrMeses(btc.getFecCierre(), null, varLimPeriodosM.intValue(), false))) {

					fechaFinPeriodos = umic.getFechas().getFecefecfin();

				} else {
					// Variables de apoyo
					varCriterEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad,
							kgarantia, btc.getBaseTec(), CLAVE_CRIEDAD);
					varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC,
							umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(),
							umic.getDatosGenerales().getKgarantia(), btc.getBaseTec(), CLAVE_CRIFEC);

					// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la
					// forma en que se gestiona el paso de umic).
					// Validación de las variables de apoyo.
					ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterEdad);
					ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFec);

					varEdadMax = umic.getAsegurados().getGedadMax();

					if (null == varEdadMax || varEdadMax.equals(ConstantsFunciones.CTE_0)) {
						varEdadMax = ConstantsFunciones.CTE_65;
					}

					varAntRenova = umic.getFechas().getFecdesderenova();
					varProxRenova = umic.getFechas().getFechastarenova();

					varFechaEfecto = UtilModulos.getVarFechaEfecto(mapVariables, CLAVE_FEC_EFEC, umic);

					varEdifer = umic.getDatosGenerales().getEdifer();
					if (umic.getDatosGenerales().getKmodalidad().equals(ConstantsModulos.MOD_363)) {
						varEdadCalc = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL, varFechaEfecto,
								umic.getAsegurados().getFnacAseg1(), ConstantsFunciones.CTE_CRI_FECHA_06,
								umic.getRentas().getFecIni(), varEdifer);
						if (fcalc.after(umic.getRentas().getFecIni())) {
							varFracc0 = FuncionesAuxiliares.nAnnos(umic.getRentas().getFecIni(), varProxRenova,
									varCriterFec);
						} else {
							varFracc0 = BigDecimal.ZERO;
						}
					} else {
						varEdadCalc = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL, varFechaEfecto,
								umic.getAsegurados().getFnacAseg1(), varCriterEdad, umic.getRentas().getFecIni(),
								varEdifer);
						varFracc0 = FuncionesAuxiliares.nAnnos(varFechaEfecto, varProxRenova, varCriterFec);

					}

					varAnoNac = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1());
					varEdadProxRenova = varEdadCalc.add(varFracc0);
					if (varEdadProxRenova.compareTo(new BigDecimal(varEdadMax)) == 1) {
						fechaFinPeriodos = umic.getFechas().getFecefecfin();

					} else {
						fechaFinPeriodos = UtilFechas.incrMeses(umic.getFechas().getFecefecfin(), null,
								varLimPeriodosM.intValue(), false);

					}

				}
			}

		}else if(ConstantsModulos.CTE_VAL_BENEF.equals(venPeriodos))  {
			fechaFinPeriodos = calcularFinBenef(umic, btc, mapVariables);
		}else if(ConstantsModulos.CTE_VAL_VIBMV.equals(venPeriodos)){
			//Añadir comprovacion de nullpointer
			fechaFinPeriodos = btc.getFecfintramo().get(0);
		}

		if (ModuloPeriodos.LOG.isTraceEnabled()) {
			ModuloPeriodos.LOG.trace("Fin de función <<  writeVariablefFinPeriodos >> de la clase ModuloPeriodos");
		}

		return fechaFinPeriodos;
	}

	/**
	 * Funcion encargada de generar periodos DESDE y HASTA con un horizonte mensual,
	 * a partir de la fecha de cálculo establecida hasta la fecha fFinPeriodos.
	 *
	 * @param fFinPeriodos  fecha fin periodos
	 * @param fcalc         fecha calculo
	 * @param umic          Datos de la umic
	 * @param ktipobt       Tipo de la base tecnica
	 * @param lstCorrientes Corriente de la umic.
	 *
	 */
	private void calcularPeriodosMensuales(final Timestamp fFinPeriodos, final Timestamp fcalc, final Umic umic,
			final String ktipobt, final List<DetalleCorriente> lstCorrientes, Map<String, Object> mapVariables,
			final DetalleBaseTecnica detalleBaseTecnica) {
		// Variables locales
		DetalleCorriente detalleActual = null;
		// Fin variables locales

		if (ModuloPeriodos.LOG.isTraceEnabled()) {
			ModuloPeriodos.LOG.trace(
					"Inicio funcion << calcularPeriodosMensuales >> de la clase ModuloPeriodos, para la entrada fFinPeriodos = {} y fcalc = {}",
					fFinPeriodos, fcalc);
		}

		if (!fFinPeriodos.before(fcalc)) {
			detalleActual = new DetalleCorriente();
			detalleActual.setFechaDesde(UtilFechas.getPrimerDiaDelMes(fcalc));
			detalleActual.setFechaHasta(UtilFechas.getUltimoDiaDelMes(fcalc));
			
			if(detalleActual.getFechaDesde().before(fcalc)){
				detalleActual.setFechaDesde(fcalc);
			}
			
			if(detalleActual.getFechaDesde().before(detalleBaseTecnica.getFecInitramo().get(0))){
				detalleActual.setFechaDesde(detalleBaseTecnica.getFecInitramo().get(0));	
			}
			
			asignarRestoDatosAlPeriodo(detalleActual, umic, ktipobt, mapVariables, lstCorrientes.size() + 1,
					detalleBaseTecnica);
			lstCorrientes.add(detalleActual);

			while (!fFinPeriodos.before(detalleActual.getFechaHasta())) {
				final Timestamp fecHastaPrev = detalleActual.getFechaHasta();
				detalleActual = new DetalleCorriente();
				detalleActual.setFechaDesde(fecHastaPrev);
				detalleActual.setFechaHasta(UtilFechas.incrMeses(detalleActual.getFechaDesde(), null, 1, true));
				asignarRestoDatosAlPeriodo(detalleActual, umic, ktipobt, mapVariables, lstCorrientes.size() + 1,
						detalleBaseTecnica);
				lstCorrientes.add(detalleActual);
			}
			if (UtilFechas.getAnio(umic.getFechas().getFecefecfin()) == ConstantsFunciones.CTE_9999) {
				detalleActual = new DetalleCorriente();
				detalleActual.setFechaDesde(fFinPeriodos);
				detalleActual.setFechaHasta(UtilFechas.incrMeses(detalleActual.getFechaDesde(), null, 1, true));
				asignarRestoDatosAlPeriodo(detalleActual, umic, ktipobt, mapVariables, lstCorrientes.size() + 1,
						detalleBaseTecnica);
				lstCorrientes.add(detalleActual);
			}

		}

		if (umic.getRentas().getTempVit() != null && (umic.getRentas().getTempVit().equals("T") || umic.getRentas().getTempVit().equals("L"))) {
			lstCorrientes.remove(lstCorrientes.size() - 1);
		}

		if (ModuloPeriodos.LOG.isTraceEnabled()) {
			ModuloPeriodos.LOG.trace("Fin funcion << calcularPeriodosMensuales >> de la clase ModuloPeriodos");
		}

	}

	/**
	 * Funcion encargada de generar periodos DESDE y HASTA con un horizonte mensual,
	 * por meses naturales.
	 *
	 * @param fFinPeriodos  fecha fin periodos
	 * @param fcalc         fecha calculo
	 * @param umic          Datos de la umic
	 * @param ktipobt       Tipo de la base tecnica
	 * @param lstCorrientes Corriente de la umic.
	 *
	 */
	private void calcularPeriodosMesesNaturales(final Timestamp fFinPeriodos, final Timestamp fcalc, final Umic umic,
			final String ktipobt, final List<DetalleCorriente> lstCorrientes, final Map<String, Object> mapVariables,
			DetalleBaseTecnica detalleBaseTecnica) {
		// Variables locales
		DetalleCorriente detalleActual = null;
		// Fin variables locales

		if (ModuloPeriodos.LOG.isTraceEnabled()) {
			ModuloPeriodos.LOG.trace(
					"Inicio funcion << calcularPeriodosMesesNaturales >> de la clase ModuloPeriodos, para la entrada fFinPeriodos = {} y fcalc = {}",
					fFinPeriodos, fcalc);
		}

		if (!fFinPeriodos.before(fcalc)) {
			detalleActual = new DetalleCorriente();
			
			
			
			detalleActual.setFechaDesde(UtilFechas.getPrimerDiaDelMes(fcalc));
			detalleActual.setFechaHasta((UtilFechas.incrMeses(detalleActual.getFechaDesde(), null, 1, false)));
			
			if(detalleActual.getFechaDesde().before(fcalc)){
				detalleActual.setFechaDesde(fcalc);
			}
			
			if(detalleActual.getFechaDesde().before(detalleBaseTecnica.getFecInitramo().get(0))){
				detalleActual.setFechaDesde(detalleBaseTecnica.getFecInitramo().get(0));	
			}
			
			asignarRestoDatosAlPeriodo(detalleActual, umic, ktipobt, mapVariables, lstCorrientes.size() + 1,
					detalleBaseTecnica);
			lstCorrientes.add(detalleActual);

			while (!fFinPeriodos.before(detalleActual.getFechaHasta())) {
				final Timestamp fecHastaPrev = detalleActual.getFechaHasta();
				detalleActual = new DetalleCorriente();
				detalleActual.setFechaDesde(fecHastaPrev);
				detalleActual.setFechaHasta(UtilFechas.incrMeses(detalleActual.getFechaDesde(), null, 1, false));
				asignarRestoDatosAlPeriodo(detalleActual, umic, ktipobt, mapVariables, lstCorrientes.size() + 1,
						detalleBaseTecnica);
				lstCorrientes.add(detalleActual);
			}
		}

		// if (umic.getRentas().getTempVit()!= null &&
		// umic.getRentas().getTempVit().equals("T")){
		// lstCorrientes.remove(lstCorrientes.size()-1);
		// }

		if (ModuloPeriodos.LOG.isTraceEnabled()) {
			ModuloPeriodos.LOG.trace("Fin funcion << calcularPeriodosMensuales >> de la clase ModuloPeriodos");
		}

	}

	/**
	 * Función encargada de obtener la fecha hasta para un criterio generado por el
	 * criterio de renovacion
	 * 
	 * @param umic          datos de la umic
	 * @param detalleActual detalle calculado para la interacion "n"
	 * @return
	 */
	public GregorianCalendar calcularFechaDesdePeriodoRenovacion(final Umic umic,
			final DetalleCorriente detalleActual) {
		// Variables locales
		final GregorianCalendar calFechaEspecial = new GregorianCalendar();
		final GregorianCalendar calRenov = new GregorianCalendar();

		calRenov.setTime(umic.getFechas().getFechastarenova());

		calFechaEspecial.setTime(detalleActual.getFechaDesde());
		calFechaEspecial.set(Calendar.HOUR, 0);
		calFechaEspecial.set(Calendar.MINUTE, 0);
		calFechaEspecial.set(Calendar.SECOND, 0);
		calFechaEspecial.set(Calendar.MILLISECOND, 0);
		calFechaEspecial.add(Calendar.MONTH, 1);

		int diaDelMes = calFechaEspecial.get(Calendar.DAY_OF_MONTH);
		int diaRenov = calRenov.get(Calendar.DAY_OF_MONTH);

		if (diaDelMes != diaRenov) {
			int ultimoDiaMes = calFechaEspecial.getActualMaximum(Calendar.DAY_OF_MONTH);
			if (ultimoDiaMes == ConstantsFunciones.CTE_29.intValue()) {
				ultimoDiaMes = ConstantsFunciones.CTE_28.intValue();
			}
			if (ultimoDiaMes < diaRenov) {
				calFechaEspecial.set(Calendar.DAY_OF_MONTH, ultimoDiaMes);
			} else {
				calFechaEspecial.set(Calendar.DAY_OF_MONTH, diaRenov);
			}
		}

		return calFechaEspecial;
	}

	public GregorianCalendar calcularFechaHastaPeriodoMensual(final Umic umic, final DetalleCorriente detalleActual) {
		// Variables locales
		final GregorianCalendar calFechaEspecial = new GregorianCalendar();

		calFechaEspecial.setTime(detalleActual.getFechaDesde());
		calFechaEspecial.set(Calendar.HOUR, 0);
		calFechaEspecial.set(Calendar.MINUTE, 0);
		calFechaEspecial.set(Calendar.SECOND, 0);
		calFechaEspecial.set(Calendar.MILLISECOND, 0);
		calFechaEspecial.add(Calendar.MONTH, 1);

		int ultimoDiaMes = calFechaEspecial.getActualMaximum(Calendar.DAY_OF_MONTH);
		if (ultimoDiaMes == ConstantsFunciones.CTE_29.intValue()) {
			ultimoDiaMes = ConstantsFunciones.CTE_28.intValue();
		}
		calFechaEspecial.set(Calendar.DAY_OF_MONTH, ultimoDiaMes);

		return calFechaEspecial;
	}

	/**
	 * Función encargada de calcular el flujo de periodos, si la periodicidad es
	 * Renovación
	 * 
	 * @param fFinPeriodos  fecha fin periodos
	 * @param fcalc         fecha calculo
	 * @param umic          Datos de la umic
	 * @param ktipobt       Tipo de la base tecnica
	 * @param lstCorrientes Corriente de la umic.
	 */
	private void calcularPeriodosRenovacion(final Timestamp fFinPeriodos, final Timestamp fcalc, final Umic umic,
			final String ktipobt, final List<DetalleCorriente> lstCorrientes, final String criterioGen,
			Map<String, Object> mapVariables, final DetalleBaseTecnica detalleBaseTecnica) {
		// Variables locales
		int iteracion = 1;
		DetalleCorriente detalleActual = null;
		// Fin variables locales

		if (ModuloPeriodos.LOG.isTraceEnabled()) {
			ModuloPeriodos.LOG
					.trace("Inicio de la función << calcularPeriodosRenovacion >> de la clase ModuloPeriodos");
		}

		/**
		 * A partir de la fecha de cálculo indicada en la entrada (que no la fecha de
		 * cierre) deben generarse periodos DESDE y HASTA, hasta la fecha fFinPeriodos
		 * (anteriormente calculada) de la siguiente forma:
		 */

		/**
		 * El primer periodo a generar se calcularía cómo: Si
		 * Dia(Umic.fechas.fechastarenova) es 29/02/xxxx se hará: -
		 * periodos(1).fechaDesde = fcalc; - periodos(1).fechaHasta --> Se compondrá la
		 * fecha de siguiente forma: 28 / Mes(fcalc) / Año(fcalc)
		 * 
		 * Si Dia(Umic.fechas.fechastarenova) es igual a 1 se hará: -
		 * periodos(1).fechaDesde = PrimerDiaMes(fcalc) - periodos(1).fechaHasta =
		 * add(periodos(1).fechaDesde.MONTH, 1)
		 * 
		 * Si Dia(Umic.fechas.fechastarenova) es distinto de 1 y de 29 se hará:
		 * 
		 * - periodos(1).fechaDesde = fcalc; - periodos(1).fechaHasta --> Se compondrá
		 * la fecha de siguiente forma: Dia(Umic.fechas.fechastarenova) / Mes(fcalc) /
		 * Año(fcalc)
		 * 
		 */
		Fecha fecHastaRenovau = new Fecha(new LocalDateTime());
		if (!fFinPeriodos.before(fcalc)) {
			GregorianCalendar calHastaRenova = new GregorianCalendar();
			if (criterioGen.equals(ConstantsModulos.CTE_VAL_RENO)) {
				calHastaRenova = new GregorianCalendar();
				calHastaRenova.setTime(umic.getFechas().getFechastarenova());
				fecHastaRenovau = UtilFechas.getFecha(umic.getFechas().getFechastarenova());
			} else if (criterioGen.equals(ConstantsModulos.CTE_VAL_REVEN)) {
				calHastaRenova = new GregorianCalendar();
				calHastaRenova.setTime(umic.getFechas().getFecefecfin());
				fecHastaRenovau = UtilFechas.getFecha(umic.getFechas().getFecefecfin());
			}
			detalleActual = new DetalleCorriente();
			GregorianCalendar calHasta = new GregorianCalendar();
			calHasta.setTime(fcalc);

			if (fecHastaRenovau.getDia() != ConstantsFunciones.CTE_1
					&& (fecHastaRenovau.getDia() != ConstantsFunciones.CTE_29
							|| fecHastaRenovau.getMes() != ConstantsFunciones.CTE_2)) {
				detalleActual.setFechaDesde(fcalc);

				int diaRenov = fecHastaRenovau.getDia();

				int ultimoDiaMes = calHasta.getActualMaximum(Calendar.DAY_OF_MONTH);
				if (ultimoDiaMes == ConstantsFunciones.CTE_29.intValue()) {
					ultimoDiaMes = ConstantsFunciones.CTE_28.intValue();
				}
				if (ultimoDiaMes < diaRenov) {
					calHasta.set(Calendar.DAY_OF_MONTH, ultimoDiaMes);
				} else {
					calHasta.set(Calendar.DAY_OF_MONTH, diaRenov);
				}

				detalleActual.setFechaHasta(new Timestamp(calHasta.getTimeInMillis()));
			} else if (fecHastaRenovau.getDia() == ConstantsFunciones.CTE_29.intValue()
					&& fecHastaRenovau.getMes() == ConstantsFunciones.CTE_2) {
				detalleActual.setFechaDesde(fcalc);
				calHasta.set(Calendar.DAY_OF_MONTH, ConstantsFunciones.CTE_28);
				detalleActual.setFechaHasta(new Timestamp(calHasta.getTimeInMillis()));
			} else { // Fecha de renovación día 1 de mes
				
				calHasta.set(Calendar.DAY_OF_MONTH, ConstantsFunciones.CTE_1);
				detalleActual.setFechaDesde(new Timestamp(calHasta.getTimeInMillis()));
				calHasta.add(Calendar.MONTH, ConstantsFunciones.CTE_1);
				detalleActual.setFechaHasta(new Timestamp(calHasta.getTimeInMillis()));
				
				if(detalleActual.getFechaDesde().before(fcalc)){
					
					calHasta.setTime(fcalc);
					detalleActual.setFechaDesde(new Timestamp(calHasta.getTimeInMillis()));
				
				}
				
				if(detalleActual.getFechaDesde().before(detalleBaseTecnica.getFecInitramo().get(0))){
					detalleActual.setFechaDesde(detalleBaseTecnica.getFecInitramo().get(0));	
				}
				
			}

			// Para cualquier n se asignará el resto de campos
			asignarRestoDatosAlPeriodo(detalleActual, umic, ktipobt, mapVariables, lstCorrientes.size() + 1,
					detalleBaseTecnica);

			lstCorrientes.add(detalleActual);
			// ModuloPeriodos.LOG.debug("{}",
			// UtilFechas.obtenerStringFechaConFormato(detalleActual.getFechaHasta()));

			while (fFinPeriodos.after(detalleActual.getFechaHasta())) {
				/**
				 * A partir de este primer periodo se generarán el resto de periodos como se
				 * muestra a continuación: - periodos(n).fechaDesde = periodos(n-1).fechaHasta;
				 */
				detalleActual = new DetalleCorriente();
				detalleActual.setFechaDesde(lstCorrientes.get(iteracion - 1).getFechaHasta());

				detalleActual.setFechaHasta(
						UtilFechas.incrMeses(detalleActual.getFechaDesde(), fecHastaRenovau.toTimestamp(), 1, false));

				// Para cualquier n se asignará el resto de campos
				asignarRestoDatosAlPeriodo(detalleActual, umic, ktipobt, mapVariables, lstCorrientes.size() + 1,
						detalleBaseTecnica);

				lstCorrientes.add(detalleActual);

				// Contador para controlar el numero de periodos
				iteracion++;
				// ModuloPeriodos.LOG.debug("{}",
				// UtilFechas.obtenerStringFechaConFormato(detalleActual.getFechaHasta()));
			}

			final Fecha fecEfecFinu = UtilFechas.getFecha(umic.getFechas().getFecefecfin());
			if (fecEfecFinu.getAnio() == ConstantsFunciones.CTE_9999) {
				detalleActual = new DetalleCorriente();
				detalleActual.setFechaDesde(fFinPeriodos);
				detalleActual.setFechaHasta(
						UtilFechas.incrMeses(detalleActual.getFechaDesde(), fecHastaRenovau.toTimestamp(), 1, false));
				asignarRestoDatosAlPeriodo(detalleActual, umic, ktipobt, mapVariables, lstCorrientes.size() + 1,
						detalleBaseTecnica);
				lstCorrientes.add(detalleActual);
				// ModuloPeriodos.LOG.debug("{}",
				// UtilFechas.obtenerStringFechaConFormato(detalleActual.getFechaHasta()));
			} else {
				detalleActual.setFechaHasta(fFinPeriodos);
				detalleActual = new DetalleCorriente();
				detalleActual.setFechaDesde(fFinPeriodos);
				detalleActual.setFechaHasta(fFinPeriodos);
				asignarRestoDatosAlPeriodo(detalleActual, umic, ktipobt, mapVariables, lstCorrientes.size() + 1,
						detalleBaseTecnica);
				lstCorrientes.add(detalleActual);
			}

		}

		if (umic.getRentas().getTempVit() != null) {
			if (umic.getRentas().getTempVit().equals("T") || umic.getRentas().getTempVit().equals("L")) {
				lstCorrientes.remove(lstCorrientes.size() - 1);
			} else if (umic.getRentas().getTempVit().equals("V") && criterioGen.equals(ConstantsModulos.CTE_VAL_RENO)
					&& UtilFechas.getDia(lstCorrientes.get(lstCorrientes.size() - 1).getFechaDesde()) != UtilFechas
							.getDia(lstCorrientes.get(lstCorrientes.size() - 2).getFechaHasta())) {
				detalleActual.setFechaDesde(lstCorrientes.get(lstCorrientes.size() - 2).getFechaHasta());
				detalleActual.setFechaHasta(
						UtilFechas.incrMeses(detalleActual.getFechaDesde(), fecHastaRenovau.toTimestamp(), 1, false));
				lstCorrientes.set(lstCorrientes.size() - 1, detalleActual);
			}
			// Si la UMIC está en el último periodo de vigencia, se corrige el
			// haber añadido un último periodo para el ajuste de las fechas
			// desde y hasta.
			// Ejemplo: UMIC 308 456445 4 11578 341798 10 a fecha de cierre
			// 201503
		} else if (lstCorrientes.size() == 2) {
			if (lstCorrientes.get(0).getFechaDesde().equals(lstCorrientes.get(1).getFechaDesde())
					&& lstCorrientes.get(0).getFechaHasta().equals(lstCorrientes.get(1).getFechaHasta())) {
				lstCorrientes.remove(1);
			}
		}

		if (ModuloPeriodos.LOG.isTraceEnabled()) {
			ModuloPeriodos.LOG.trace("Fin de la función << calcularPeriodosRenovacion >> de la clase ModuloPeriodos");
		}

	}

	/**
	 * Funcion encargada de generar periodos DESDE y HASTA para el proceso de
	 * "chequeo de primas"
	 *
	 * @param fFinPeriodos  fecha fin periodos
	 * @param fcalc         fecha calculo
	 * @param umic          Datos de la umic
	 * @param ktipobt       Tipo de la base tecnica
	 * @param lstCorrientes Corriente de la umic.
	 *
	 */
	private void calcularPeriodosPrimas(final Timestamp fFinPeriodos, final Timestamp fcalc, final Umic umic,
			final String ktipobt, final List<DetalleCorriente> lstCorrientes, final String criterioGen,
			Map<String, Object> mapVariables, final DetalleBaseTecnica detalleBaseTecnica) {
		// Variables locales
		int iteracion = 1;
		int diaRenov;
		int diaDesde;
		DetalleCorriente detalleActual = null;
		// Fin variables locales

		if (ModuloPeriodos.LOG.isTraceEnabled()) {
			ModuloPeriodos.LOG.trace("Inicio de la función << calcularPeriodosPrimas >> de la clase ModuloPeriodos");
		}

		/**
		 * A partir de la fecha de cálculo indicada en la entrada deben generarse
		 * periodos DESDE y HASTA, hasta la fecha fFinPeriodos (anteriormente calculada)
		 * de la siguiente forma:
		 */

		Fecha fecHasta = new Fecha(new LocalDateTime());
		GregorianCalendar calHasta = new GregorianCalendar();
		if (!fFinPeriodos.before(fcalc)) {
			detalleActual = new DetalleCorriente();
			if (umic.getDatosGenerales().getCnegocio().equals("I")) {
				detalleActual.setFechaDesde(umic.getFechas().getFecinisus());
			} else {
				detalleActual.setFechaDesde(umic.getFechas().getFecefecini());
			}
			
			if (!(detalleActual.getFechaDesde().after(detalleBaseTecnica.getFecInitramo().get(0)) || detalleActual.getFechaDesde().equals(detalleBaseTecnica.getFecInitramo().get(0)))
					&& (detalleActual.getFechaDesde().before(detalleBaseTecnica.getFecfintramo().get(0)) || detalleActual.getFechaDesde().equals(detalleBaseTecnica.getFecfintramo().get(0)))) {
				detalleActual.setFechaDesde(detalleBaseTecnica.getFecInitramo().get(0));
			}
			
			
			if(criterioGen.equals(ConstantsModulos.CTE_VAL_RENO)) {
				diaRenov = UtilFechas.getFecha(umic.getFechas().getFechastarenova()).getDia();
				diaDesde = UtilFechas.getFecha(detalleActual.getFechaDesde()).getDia();
				if(diaDesde >= diaRenov) {
					calHasta.setTime(UtilFechas.incrMeses(fcalc, null, 1, false));
				}
				else {
					calHasta.setTime(fcalc);
				}
				calHasta.set(Calendar.DAY_OF_MONTH, diaRenov);
			}else if (criterioGen.equals(ConstantsModulos.CTE_VAL_REVEN)) {
				diaRenov = UtilFechas.getFecha(umic.getFechas().getFecefecfin()).getDia();
				diaDesde = UtilFechas.getFecha(detalleActual.getFechaDesde()).getDia();
				if(diaDesde >= diaRenov) {
					calHasta.setTime(UtilFechas.incrMeses(fcalc, null, 1, false));
				}
				else {
					calHasta.setTime(fcalc);
				}
				calHasta.set(Calendar.DAY_OF_MONTH, diaRenov);
			}
			else {
				calHasta.setTime(UtilFechas.incrMeses(fcalc, null, 1, false));
			}
			
			detalleActual.setFechaHasta(new Timestamp(calHasta.getTimeInMillis()));
			fecHasta = UtilFechas.getFecha(detalleActual.getFechaHasta());

			if (fecHasta.getDia() != ConstantsFunciones.CTE_1
					&& (fecHasta.getDia() != ConstantsFunciones.CTE_29
							|| fecHasta.getMes() != ConstantsFunciones.CTE_2)) {

				diaRenov = fecHasta.getDia();

				int ultimoDiaMes = calHasta.getActualMaximum(Calendar.DAY_OF_MONTH);
				if (ultimoDiaMes == ConstantsFunciones.CTE_29.intValue()) {
					ultimoDiaMes = ConstantsFunciones.CTE_28.intValue();
				}
				if (ultimoDiaMes < diaRenov) {
					calHasta.set(Calendar.DAY_OF_MONTH, ultimoDiaMes);
				} else {
					calHasta.set(Calendar.DAY_OF_MONTH, diaRenov);
				}

				detalleActual.setFechaHasta(new Timestamp(calHasta.getTimeInMillis()));
			} else if (fecHasta.getDia() == ConstantsFunciones.CTE_29.intValue()
					&& fecHasta.getMes() == ConstantsFunciones.CTE_2) {
				calHasta.set(Calendar.DAY_OF_MONTH, ConstantsFunciones.CTE_28);
				detalleActual.setFechaHasta(new Timestamp(calHasta.getTimeInMillis()));
			}

			// Para cualquier n se asignará el resto de campos
			asignarRestoDatosAlPeriodo(detalleActual, umic, ktipobt, mapVariables, lstCorrientes.size() + 1,
					detalleBaseTecnica);
			lstCorrientes.add(detalleActual);
			//asdf
			
			while (fFinPeriodos.after(detalleActual.getFechaHasta())) {
				/**
				 * A partir de este primer periodo se generarán el resto de periodos como se
				 * muestra a continuación: - periodos(n).fechaDesde = periodos(n-1).fechaHasta;
				 */
				detalleActual = new DetalleCorriente();
				detalleActual.setFechaDesde(lstCorrientes.get(iteracion - 1).getFechaHasta());

				detalleActual.setFechaHasta(UtilFechas.incrMeses(detalleActual.getFechaDesde(), fecHasta.toTimestamp(), 1, false));

				// Para cualquier n se asignará el resto de campos
				asignarRestoDatosAlPeriodo(detalleActual, umic, ktipobt, mapVariables, lstCorrientes.size() + 1,
						detalleBaseTecnica);

				lstCorrientes.add(detalleActual);

				// Contador para controlar el numero de periodos
				iteracion++;
			}
			
			if(criterioGen.equals(ConstantsModulos.CTE_VAL_RENO) || criterioGen.equals(ConstantsModulos.CTE_VAL_REVEN)) {
				final Fecha fecEfecFinu = UtilFechas.getFecha(umic.getFechas().getFecefecfin());
				if (fecEfecFinu.getAnio() == ConstantsFunciones.CTE_9999) {
					detalleActual = new DetalleCorriente();
					detalleActual.setFechaDesde(fFinPeriodos);
					detalleActual.setFechaHasta(
							UtilFechas.incrMeses(detalleActual.getFechaDesde(), fecHasta.toTimestamp(), 1, false));
					asignarRestoDatosAlPeriodo(detalleActual, umic, ktipobt, mapVariables, lstCorrientes.size() + 1,
							detalleBaseTecnica);
					lstCorrientes.add(detalleActual);
				} else {
					detalleActual.setFechaHasta(fFinPeriodos);
					detalleActual = new DetalleCorriente();
					detalleActual.setFechaDesde(fFinPeriodos);
					detalleActual.setFechaHasta(fFinPeriodos);
					asignarRestoDatosAlPeriodo(detalleActual, umic, ktipobt, mapVariables, lstCorrientes.size() + 1,
							detalleBaseTecnica);
					lstCorrientes.add(detalleActual);
				}
			}
		}
		if(criterioGen.equals(ConstantsModulos.CTE_VAL_RENO) || criterioGen.equals(ConstantsModulos.CTE_VAL_REVEN)) {
			if (umic.getRentas().getTempVit() != null) {
				if (umic.getRentas().getTempVit().equals("T") || umic.getRentas().getTempVit().equals("L")) {
					lstCorrientes.remove(lstCorrientes.size() - 1);
				} else if (umic.getRentas().getTempVit().equals("V") && criterioGen.equals(ConstantsModulos.CTE_VAL_RENO)
						&& UtilFechas.getDia(lstCorrientes.get(lstCorrientes.size() - 1).getFechaDesde()) != UtilFechas
								.getDia(lstCorrientes.get(lstCorrientes.size() - 2).getFechaHasta())) {
					detalleActual.setFechaDesde(lstCorrientes.get(lstCorrientes.size() - 2).getFechaHasta());
					detalleActual.setFechaHasta(
							UtilFechas.incrMeses(detalleActual.getFechaDesde(), fecHasta.toTimestamp(), 1, false));
					lstCorrientes.set(lstCorrientes.size() - 1, detalleActual);
				}
				// Si la UMIC está en el último periodo de vigencia, se corrige el
				// haber añadido un último periodo para el ajuste de las fechas
				// desde y hasta.
				// Ejemplo: UMIC 308 456445 4 11578 341798 10 a fecha de cierre
				// 201503
			} else if (lstCorrientes.size() == 2) {
				if (lstCorrientes.get(0).getFechaDesde().equals(lstCorrientes.get(1).getFechaDesde())
						&& lstCorrientes.get(0).getFechaHasta().equals(lstCorrientes.get(1).getFechaHasta())) {
					lstCorrientes.remove(1);
				}
			}
		}

		if (ModuloPeriodos.LOG.isTraceEnabled()) {
			ModuloPeriodos.LOG.trace("Fin de la función << calcularPeriodosPrimas >> de la clase ModuloPeriodos");
		}

	}

	/**
	 * Función encargada de asignar el resto de campos para cualquier n en las
	 * funciones "calcularPeriodosRenovacion" y "calcularPeriodosMensuales"
	 * 
	 * @param periodoActual Periodo actual
	 * @param umic          Datos de la umic
	 * @param ktipobt       Tipo de la base tecnica
	 * 
	 */
	private void asignarRestoDatosAlPeriodo(final DetalleCorriente periodoActual, final Umic umic, final String ktipobt,
			final Map<String, Object> mapVariables, final Integer iteracion,
			final DetalleBaseTecnica detalleBaseTecnica) {
		/**
		 * Para cualquier n se asignará el resto de campos del array como: - proyUmic
		 * (n).cnegocio = umic.datosGenerales.cnegocio - proyUmic (n).ccanal =
		 * umic.datosGenerales.ccanal - proyUmic (n).ccartera =
		 * umic.datosGenerales.ccartera - proyUmic (n).fcierre =
		 * umic.datosGenerales.fecCierre - proyUmic (n).bt = btcUmic.btcalc - proyUmic
		 * (n).kmodalidad umic.datosGenerales.kmodalidad - proyUmic (n).kpoliza =
		 * umic.datosGenerales.kpoliza - proyUmic (n).ksubpoliza =
		 * umic.datosGenerales.ksubpoliza - proyUmic (n).kcertificado =
		 * umic.datosGenerales.kcertificado - proyUmic (n).nsuscri =
		 * umic.datosGenerales.nsuscri - proyUmic (n).norden =
		 * umic.datosGenerales.norden - proyUmic (n).kgarantia =
		 * umic.datosGenerales.kgarantia - proyUmic (n).kprestacion =
		 * umic.datosGenerales.kprestacion - proyUmic (n).kajuste =
		 * umic.datosGenerales.kajuste - proyUmic (n).ctipoaport =
		 * umic.datosGenerales.ctipoaport - proyUmic (n).kramo =
		 * umic.datosGenerales.kramo - proyUmic (n).fsuscri = umic.fechas.fecinisus -
		 * proyUmic (n).kcarterainv = umic.datosGenerales.
		 * umic.datosGenerales.kcarterainv - proyUmic (n).gapAct = umic.datosGenerales.
		 * gapAct - proyUmic (n).intFecDesde = CalcularIntFecDesde(btcUmic, proyUmic
		 * (n).fechaDesde)
		 */

		if (ModuloPeriodos.LOG.isTraceEnabled()) {
			ModuloPeriodos.LOG
					.trace("Inicio de la función << asignarRestoDatosAlPeriodo >> de la clase ModuloPeriodos");
		}

		final Fechas fechas = umic.getFechas();
		periodoActual.setCnegocio(umic.getDatosGenerales().getCnegocio());
		periodoActual.setCcanal(umic.getDatosGenerales().getCcanal());
		periodoActual.setCcartera(umic.getDatosGenerales().getCcartera());
		periodoActual.setFcierre(umic.getDatosGenerales().getFecCierre());
		periodoActual.setBt(ktipobt);
		periodoActual.setKmodalidad(umic.getDatosGenerales().getKmodalidad());
		periodoActual.setKpoliza(umic.getDatosGenerales().getKpoliza());
		periodoActual.setKsubpoliza(umic.getDatosGenerales().getKsubpoliza());
		periodoActual.setKcertificado(umic.getDatosGenerales().getKcertificado());
		periodoActual.setNsuscri(umic.getDatosGenerales().getNsuscri());
		periodoActual.setNorden(umic.getDatosGenerales().getNorden());
		periodoActual.setKgarantia(umic.getDatosGenerales().getKgarantia());
		periodoActual.setKprestacion(umic.getDatosGenerales().getKprestacion());
		periodoActual.setKajuste(umic.getDatosGenerales().getKajuste());
		periodoActual.setCtipoaport(umic.getDatosGenerales().getCtipoaport());
		periodoActual.setKramo(umic.getDatosGenerales().getKramo());
		periodoActual.setFsuscri(fechas.getFecinisus());
		periodoActual.setKcarterainv(umic.getDatosGenerales().getKcarterainv());
		periodoActual.setGapAct(umic.getDatosGenerales().getGapAct());
		periodoActual.setGapAct(umic.getDatosGenerales().getGapAct());
//INI-TAR00400971				
		periodoActual.setSpcom(umic.getDatosGenerales().getSpcom());
		periodoActual.setKmodext(umic.getDatosGenerales().getKmodext());
		periodoActual.setKbencon(umic.getDatosGenerales().getKbencon());
//FIN-TAR00400971	
		
		//NIIF17 asigancion uoa y kcontrato a cada periodo
		periodoActual.setUoa(umic.getDatosNiif17().getuoa());
		periodoActual.setKcontrato(umic.getDatosNiif17().getkcarcontacto());
		
		if (periodoActual.getBt().equals(ConstantsModulos.CTE_BT_BEL)
				|| periodoActual.getBt().equals(ConstantsModulos.CTE_VAL_BELCOA)
				|| periodoActual.getBt().equals(ConstantsModulos.CTE_VAL_BELCLR)
				|| periodoActual.getBt().equals(ConstantsModulos.CTE_VAL_SCRTIU)
				|| periodoActual.getBt().equals(ConstantsModulos.CTE_VAL_SCRTID)
				|| periodoActual.getBt().equals(ConstantsModulos.CTE_VAL_SCRGTO)
				|| periodoActual.getBt().equals(ConstantsModulos.CTE_VAL_SCRMFE)
				|| periodoActual.getBt().equals(ConstantsModulos.CTE_VAL_SCRMMI)
				|| periodoActual.getBt().equals(ConstantsModulos.CTE_VAL_SCRMCF)
				|| periodoActual.getBt().equals(ConstantsModulos.CTE_VAL_SCRMCI)
				|| periodoActual.getBt().equals(ConstantsModulos.CTE_VAL_SCRLFE)
				|| periodoActual.getBt().equals(ConstantsModulos.CTE_VAL_SCRLMI)
				|| periodoActual.getBt().equals(ConstantsModulos.CTE_VAL_SCRINC)
				|| periodoActual.getBt().equals(ConstantsModulos.CTE_VAL_SCRVM)
				|| periodoActual.getBt().equals(ConstantsModulos.CTE_VAL_SCRAEP)
				|| periodoActual.getBt().equals(ConstantsModulos.CTE_VAL_SCRAEN)
				|| periodoActual.getBt().equals(ConstantsModulos.CTE_VAL_SCRAIP)
				|| periodoActual.getBt().equals(ConstantsModulos.CTE_VAL_SCRAIN)
				|| periodoActual.getBt().equals(ConstantsModulos.CTE_VAL_SCRANM)) {
			periodoActual.setIntfeccal(BigDecimal.ZERO);
		} else {
			periodoActual.setIntfeccal(
					calcularIntFecDesde(detalleBaseTecnica, periodoActual.getFechaDesde(), mapVariables, iteracion));
		}

		if (ModuloPeriodos.LOG.isTraceEnabled()) {
			ModuloPeriodos.LOG.trace("Fin de la función << asignarRestoDatosAlPeriodo >> de la clase ModuloPeriodos");
		}
	}

	/**
	 * función que calcula el interés técnico a Fecha de Desde del periodo para la
	 * umic
	 * 
	 * @param detalleBT
	 * @param fechaDesde
	 * @return
	 */
	public BigDecimal calcularIntFecDesde(final DetalleBaseTecnica detalleBT, final Timestamp fechaDesde,
			final Map<String, Object> mapVariables, final Integer iteracion) {

		final List<Timestamp> fecInitramo = detalleBT.getFecInitramo();
		final List<Timestamp> fecFintramo = detalleBT.getFecfintramo();
		Timestamp vfecfinTramo;
		BigDecimal vintCalc = null;

		/**
		 * Si j =1 se buscará el tramo de interés con : - fecIniTramo(i) <= proyUmic
		 * (n).fechaDesde <= fecFinTramo(i) - Se almacenará vfecfinTramo =
		 * fecFinTramo(i) en memoria para le subrpoceso de la umic.
		 * 
		 * Para el tramo así hallado se retornará el porcentaje de interés
		 * correspondiente a ese elemento del array de tramos de interés: - vintCalc =
		 * itcalc(i)  Se almacenará vintCalc en memoria para le subproceso de la umic.
		 * 
		 * Se retornará vintCalc
		 */
		if (ConstantsFunciones.CTE_FIRST_ITER == iteracion) {
			for (int i = 0; i < fecInitramo.size(); i++) {
				if (fecInitramo.get(i) != null && fecFintramo.get(i) != null) {
					if ((fechaDesde.after(fecInitramo.get(i)) || fechaDesde.equals(fecInitramo.get(i)))
							&& (fechaDesde.before(fecFintramo.get(i)) || fechaDesde.equals(fecFintramo.get(i)))) {
						vintCalc = detalleBT.getItcalc().get(i);
						vfecfinTramo = fecFintramo.get(i);
						mapVariables.put(CLAVE_VAR_CTE_VFECFINTRAMO, vfecfinTramo);
						mapVariables.put(CLAVE_VAR_CTE_VINTCALC, vintCalc);
					}else if(mapVariables.get("GP_SWCOBROCOM").equals(ConstantesSolvencia.CTE_UNID_EJEC_SWCOBROCOMISIONES)) {
						vintCalc = detalleBT.getItcalc().get(i);
						vfecfinTramo = fecFintramo.get(i);
						mapVariables.put(CLAVE_VAR_CTE_VFECFINTRAMO, vfecfinTramo);
						mapVariables.put(CLAVE_VAR_CTE_VINTCALC, vintCalc);
					}
				}
			}

			/**
			 * Si j >1 se comproborá: - Si proyUmic (n).fechaDesde <= vfecfinTramo  Se
			 * retornará vintCalc almacenado en memoria. - Si proyUmic (n).fechaDesde >
			 * vfecfinTramo , se buscará el tramos de interés con : o fecIniTramo(i) <=
			 * proyUmic (n).fechaDesde <= fecFinTramo(i) Para el tramo así hallado se
			 * retornará el porcentaje de interés correspondiente a ese elemento del array
			 * de tramos de interés: - vintCalc = itcalc(i)  Se almacenará vintCalc en
			 * memoria para le subrpoceso de la umic.
			 * 
			 */
		} else {
			vfecfinTramo = (Timestamp) mapVariables.get(CLAVE_VAR_CTE_VFECFINTRAMO);
			if (vfecfinTramo == null) {
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_GD, new String[] {});
			}

			if (fechaDesde.after(vfecfinTramo) || fechaDesde.equals(vfecfinTramo)) {
				for (int i = 0; i < fecInitramo.size(); i++) {
					if (fecInitramo.get(i) != null && fecFintramo.get(i) != null) {
						if ((fechaDesde.after(fecInitramo.get(i)) || fechaDesde.equals(fecInitramo.get(i)))
								&& (fechaDesde.before(fecFintramo.get(i)) || fechaDesde.equals(fecFintramo.get(i)))) {
							vintCalc = detalleBT.getItcalc().get(i);
							mapVariables.put(CLAVE_VAR_CTE_VINTCALC, vintCalc);
						}
					}
				}
			} else {
				vintCalc = (BigDecimal) mapVariables.get(CLAVE_VAR_CTE_VINTCALC);
			}
		}
		return vintCalc;
	}

	/**
	 * Función que calcula el fin de fecha de beneficio
	 * 
	 * @param umic
	 * @param btc
	 */
	private Timestamp calcularFinBenef(final Umic umic, final DetalleBaseTecnica btc,
			final Map<String, Object> mapVariables) {
		Timestamp ffinvitaliciaBenef = null;
		Integer varIDHijo = 0;
		String varMinusvalido = ConstantsModulos.CTE_N;
		Timestamp varFecNac = null;
		OrdenAsegurado ordenAsegurado = null;
		String varSexo = null;
		String varCriterEdad;
		Integer varEdadMax;
		BigDecimal varEdadCalc = BigDecimal.ZERO;

		IObtenerConfiguracion obtConfi = FachadaServicios.getObtenerConfiguracion();

		String varPrestacion = "CAB_" + umic.getDatosGenerales().getKprestacion();
		String varCabeza = (String) obtConfi.recuperarDefinicionAuxiliar(umic.getDatosGenerales().getCcartera(),
				umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btc.getBaseTec(), varPrestacion);

		if (varCabeza == null) {
			varCabeza = ConstantsFunciones.CTE_2_STRING;
		}

		if (varCabeza.equals(ConstantsFunciones.CTE_2_STRING)) {
			varIDHijo = 2;
			ordenAsegurado = OrdenAsegurado.ASEG2;
			varFecNac = umic.getAsegurados().getFnacAseg2();
			varSexo = umic.getAsegurados().getCsexAseg2();
		} else if (varCabeza.equals(ConstantsFunciones.CTE_3_STRING)) {
			if (umic.getAsegurados().getFnacAseg3() != null && umic.getAsegurados().getCsexAseg3() != null
					&& umic.getAsegurados().getEdadAseg3() != null) {
				if (umic.getOtrosDatos().getCestadoAseg3().equals(ConstantsModulos.CTE_CESTADO_ASEG_M)) {
					varIDHijo = 3;
					ordenAsegurado = OrdenAsegurado.ASEG3;
					varMinusvalido = ConstantsModulos.CTE_S;
					varFecNac = umic.getAsegurados().getFnacAseg3();
					varSexo = umic.getAsegurados().getCsexAseg3();
				}
			}

			if (umic.getAsegurados().getFnacAseg4() != null && umic.getAsegurados().getCsexAseg4() != null
					&& umic.getAsegurados().getEdadAseg4() != null) {
				if (umic.getOtrosDatos().getCestadoAseg4().equals(ConstantsModulos.CTE_CESTADO_ASEG_M)) {
					if(umic.getAsegurados().getFnacAseg4().after(varFecNac) || varFecNac == null) {
						varIDHijo = 4;
						ordenAsegurado = OrdenAsegurado.ASEG4;
						varMinusvalido = ConstantsModulos.CTE_S;
						varFecNac = umic.getAsegurados().getFnacAseg4();
						varSexo = umic.getAsegurados().getCsexAseg4();
					}
				}
			}

			if (umic.getAsegurados().getFnacAseg5() != null && umic.getAsegurados().getCsexAseg5() != null
					&& umic.getAsegurados().getEdadAseg5() != null) {
				if (umic.getOtrosDatos().getCestadoAseg5().equals(ConstantsModulos.CTE_CESTADO_ASEG_M)) {
					if(umic.getAsegurados().getFnacAseg5().after(varFecNac) || varFecNac == null) {
						varIDHijo = 5;
						ordenAsegurado = OrdenAsegurado.ASEG5;
						varMinusvalido = ConstantsModulos.CTE_S;
						varFecNac = umic.getAsegurados().getFnacAseg5();
						varSexo = umic.getAsegurados().getCsexAseg5();
					}
				}
			}
			
			if(varIDHijo == 0 || varMinusvalido.equals(ConstantsModulos.CTE_N)){
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_GU);
			}
		} else {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC,
					new String[] { "varCabeza", varCabeza });
		}
		
//		if (!varCabeza.equalsIgnoreCase(ConstantsFunciones.CTE_2_STRING) && (varIDHijo == 0 || varMinusvalido.equals(ConstantsModulos.CTE_N))) {
//			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_GU);
//		} else {
			varEdadMax = obtConfi.recuperarEdadMax(umic, btc, String.valueOf(UtilFechas.getAnio(varFecNac)), varSexo, ordenAsegurado);
			varCriterEdad = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIEDAD,
					umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(),
					umic.getDatosGenerales().getKgarantia(), btc.getBaseTec(), CLAVE_CRIEDAD);
			varEdadCalc = FuncionesAuxiliares.nEdad(umic.getFechas().getFecinisus(), varFecNac, varCriterEdad,
					umic.getRentas().getFecIni(), umic.getDatosGenerales().getEdifer());
			ffinvitaliciaBenef = UtilFechas.incrAnyo(umic.getFechas().getFecinisus(),
					varEdadMax - varEdadCalc.intValue());
		//}

		return ffinvitaliciaBenef;
	}

	
	
}
