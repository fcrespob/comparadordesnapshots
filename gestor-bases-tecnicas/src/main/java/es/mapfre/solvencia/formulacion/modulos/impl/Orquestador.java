/**
 * MU-2018-071849-C�digo IM00575941: REVISAR CRITERIO RECASAMIENTO IT04
 * Que la fecha de casamiento de la umic sea inferior a la fecha de recasamiento, debe ocurrir siempre.
 * Deber�a haber un aviso cuando se d� que el recasamiento es inferior al casamiento
 */

package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.FlujosProbablesKey;
import es.mapfre.solvencia.dao.impl.gbt.AsigInteresTecnicoDao;
import es.mapfre.solvencia.dao.impl.gbt.ErrorOrquestadorDao;
import es.mapfre.solvencia.dao.impl.gbt.RentaGAPDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.DisenoProcesosDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.FlujosProbablesDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.gbt.AsigInteresTecnico;
import es.mapfre.solvencia.dominio.gbt.ErrorOrquestador;
import es.mapfre.solvencia.dominio.gbt.RentaGAP;
import es.mapfre.solvencia.dominio.maestro.BaseTecnicaInicial;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.BloqueFlujosProbables;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DisenoProcesos;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ElementoSubproceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.excepcionGBT.GestorBasesTecnicasException;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.ConstantsProcesos;
import es.mapfre.solvencia.gbt.util.ConstantesErrores;
import es.mapfre.solvencia.gbt.util.ConstantesGBT;
import es.mapfre.solvencia.gbt.util.ConstantesModulosGBT;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;

/**
 * Clase encargada de implementar el modulo encargado de establecer la base
 * técnica de cálculo necesaria para realizar las operaciones que obtienen los
 * flujos de las distintas corrientes. Como resultado obtendremos una estructura
 * de datos con el detalle de la base técnica de cálculo, btcUmic.
 * 
 */
public class Orquestador implements Modulo {

	/** Cte para log. */
	GestorBasesTecnicasException exc = new GestorBasesTecnicasException();
	private FlujosProbablesDao flujosProbablesDao = new FlujosProbablesDao();
	private static final Logger LOG = LoggerFactory.getLogger(Orquestador.class);
	private static final String PROCESO = "BASE_TEC";
	// private static final String PROCESO = "BASE_TEC2";
	// private static final ExporterGBT exporter = new ExporterGBT();
	public static Orquestador INSTANCE = null;
	private FlujosProbables fpClone;

	public static Orquestador getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Orquestador();
		}
		return INSTANCE;
	}

	@Override
	public String getNombreServicio() {
		return ConstantesModulosGBT.MODULO_BASE_TEC;
	}

	private void informarDatos(Umic umic, DetalleBaseTecnica btcUmic) {

		if ((btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_ROSSP) || btcUmic.getBaseTec().equals("ROSSEAR")
				|| btcUmic.getBaseTec().equals("ROSSEARC") || btcUmic.getBaseTec().equals("ROSSPCSM")
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_ROSSPTE) || btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_ROSSPTI)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_ROSSPGA)) 
				&& (umic.getBti().getFecIniTramo1() != null && umic.getBti().getFecIniTramo2() != null)) {
			final BaseTecnicaInicial bti = umic.getBti();
			try {
				establecerFecIniFinTramoX(umic, btcUmic, bti);
			} catch (Exception e) {

				final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
				Incidencia aviso = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(),
						umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(),
						btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(),
						ConstantsFunciones.CTE_COD_ERROR_I006, ArrayUtils.EMPTY_OBJECT_ARRAY);
				String vacio = "";
				if (null != exc.getInfAmpliada() && !vacio.equals(exc.getInfAmpliada())) {
					String error = exc.getInfAmpliada();
					Incidencia incidencia = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(),
							umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(),
							btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(),
							ConstantsFunciones.CTE_COD_ERROR_I007, new Object[] { error });
					servicio.almacenarIncidencias(incidencia);
				}
				servicio.almacenarIncidencias(aviso);
				flujosProbablesBTI(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(),
						umic.getDatosGenerales().getKprestacion(), umic.getDatosAdicionales().getPrestCal(),
						btcUmic.getBt());

				ArrayList<String> grupoActivoPasivoAsegurados = new ArrayList<String>();
				grupoActivoPasivoAsegurados.add(umic.getBti().getGapI1());
				grupoActivoPasivoAsegurados.add(umic.getBti().getGapI2());
				grupoActivoPasivoAsegurados.add(umic.getBti().getGapI3());
				grupoActivoPasivoAsegurados.add(umic.getBti().getGapI4());
				grupoActivoPasivoAsegurados.add(umic.getBti().getGapI5());
				btcUmic.setGrupoActivoPasivo(grupoActivoPasivoAsegurados);
				ArrayList<String> indCasadoAsegurados = new ArrayList<String>();
				indCasadoAsegurados.add(umic.getBti().getSwcasadoI1());
				indCasadoAsegurados.add(umic.getBti().getSwcasadoI2());
				indCasadoAsegurados.add(umic.getBti().getSwcasadoI3());
				indCasadoAsegurados.add(umic.getBti().getSwcasadoI4());
				indCasadoAsegurados.add(umic.getBti().getSwcasadoI5());
				btcUmic.setSwcasado(indCasadoAsegurados);

				ArrayList<Timestamp> fecIniTramoList = new ArrayList<Timestamp>();
				fecIniTramoList.add(umic.getBti().getFecIniTramo1());
				fecIniTramoList.add(umic.getBti().getFecIniTramo2());
				fecIniTramoList.add(umic.getBti().getFecIniTramo3());
				fecIniTramoList.add(umic.getBti().getFecIniTramo4());
				fecIniTramoList.add(umic.getBti().getFecIniTramo5());
				ArrayList<Timestamp> fecFinTramoList = new ArrayList<Timestamp>();
				fecFinTramoList.add(umic.getBti().getFecFinTramo1());
				fecFinTramoList.add(umic.getBti().getFecFinTramo2());
				fecFinTramoList.add(umic.getBti().getFecFinTramo3());
				fecFinTramoList.add(umic.getBti().getFecFinTramo4());
				fecFinTramoList.add(umic.getBti().getFecFinTramo5());
				btcUmic.setFecInitramo(fecIniTramoList);
				btcUmic.setFecfintramo(fecFinTramoList);

			}
			btcUmic.setFecCierre(umic.getDatosGenerales().getFecCierre());
			btcUmic.setUmicKey(umic.getKey());
			btcUmic.setCcartera(umic.getDatosGenerales().getCcartera());
			btcUmic.setCcanal(umic.getDatosGenerales().getCcanal());
			btcUmic.setCnegocio(umic.getDatosGenerales().getCnegocio());
			btcUmic.setCtipoaport(umic.getKey().getCtipoaport());
			// GBT v1 Inicio - Cambios pedidos
			ArrayList<BigDecimal> edadAsegurados = new ArrayList<BigDecimal>();
			edadAsegurados.add(new BigDecimal(umic.getAsegurados().getEdadAseg1()));
			edadAsegurados.add(new BigDecimal(umic.getAsegurados().getEdadAseg2()));
			edadAsegurados.add(new BigDecimal(umic.getAsegurados().getEdadAseg3()));
			edadAsegurados.add(new BigDecimal(umic.getAsegurados().getEdadAseg4()));
			edadAsegurados.add(new BigDecimal(umic.getAsegurados().getEdadAseg5()));
			btcUmic.setEdadcalc(edadAsegurados);

			btcUmic.setGtoprov(BigDecimal.ZERO);
			btcUmic.setGtoUni(BigDecimal.ZERO);
			btcUmic.setFactor1(BigDecimal.ZERO);
			btcUmic.setFactor1_2(BigDecimal.ZERO);
			btcUmic.setFactor1_3(BigDecimal.ZERO);
			btcUmic.setFactor1_4(BigDecimal.ZERO);
			btcUmic.setFactor1_5(BigDecimal.ZERO);
			final List<BigDecimal> factor2Cero = new ArrayList<BigDecimal>();
			for (int i = 0; i < 10; i++) {
				factor2Cero.add(BigDecimal.ZERO);
			}
			btcUmic.setFactor2(factor2Cero);
			btcUmic.setFactor2_2(factor2Cero);
			btcUmic.setFactor2_3(factor2Cero);
			btcUmic.setFactor2_4(factor2Cero);
			btcUmic.setFactor2_5(factor2Cero);
			// GBT v1 Final - Cambios pedidos

		} else {

			btcUmic.setFecCierre(umic.getDatosGenerales().getFecCierre());
			btcUmic.setUmicKey(umic.getKey());
			btcUmic.setCcartera(umic.getDatosGenerales().getCcartera());
			btcUmic.setCcanal(umic.getDatosGenerales().getCcanal());
			btcUmic.setCnegocio(umic.getDatosGenerales().getCnegocio());
			btcUmic.setCtipoaport(umic.getKey().getCtipoaport());
			// GBT v1 Inicio - Cambios pedidos
			ArrayList<BigDecimal> edadAsegurados = new ArrayList<BigDecimal>();
			edadAsegurados.add(new BigDecimal(umic.getAsegurados().getEdadAseg1()));
			edadAsegurados.add(new BigDecimal(umic.getAsegurados().getEdadAseg2()));
			edadAsegurados.add(new BigDecimal(umic.getAsegurados().getEdadAseg3()));
			edadAsegurados.add(new BigDecimal(umic.getAsegurados().getEdadAseg4()));
			edadAsegurados.add(new BigDecimal(umic.getAsegurados().getEdadAseg5()));
			btcUmic.setEdadcalc(edadAsegurados);
			ArrayList<String> grupoActivoPasivoAsegurados = new ArrayList<String>();
			grupoActivoPasivoAsegurados.add(umic.getBti().getGapI1());
			grupoActivoPasivoAsegurados.add(umic.getBti().getGapI2());
			grupoActivoPasivoAsegurados.add(umic.getBti().getGapI3());
			grupoActivoPasivoAsegurados.add(umic.getBti().getGapI4());
			grupoActivoPasivoAsegurados.add(umic.getBti().getGapI5());
			btcUmic.setGrupoActivoPasivo(grupoActivoPasivoAsegurados);
			ArrayList<String> indCasadoAsegurados = new ArrayList<String>();
			indCasadoAsegurados.add(umic.getBti().getSwcasadoI1());
			indCasadoAsegurados.add(umic.getBti().getSwcasadoI2());
			indCasadoAsegurados.add(umic.getBti().getSwcasadoI3());
			indCasadoAsegurados.add(umic.getBti().getSwcasadoI4());
			indCasadoAsegurados.add(umic.getBti().getSwcasadoI5());
			btcUmic.setSwcasado(indCasadoAsegurados);

			ArrayList<Timestamp> fecIniTramoList = new ArrayList<Timestamp>();
			fecIniTramoList.add(umic.getBti().getFecIniTramo1());
			fecIniTramoList.add(umic.getBti().getFecIniTramo2());
			fecIniTramoList.add(umic.getBti().getFecIniTramo3());
			fecIniTramoList.add(umic.getBti().getFecIniTramo4());
			fecIniTramoList.add(umic.getBti().getFecIniTramo5());
			ArrayList<Timestamp> fecFinTramoList = new ArrayList<Timestamp>();
			fecFinTramoList.add(umic.getBti().getFecFinTramo1());
			fecFinTramoList.add(umic.getBti().getFecFinTramo2());
			fecFinTramoList.add(umic.getBti().getFecFinTramo3());
			fecFinTramoList.add(umic.getBti().getFecFinTramo4());
			fecFinTramoList.add(umic.getBti().getFecFinTramo5());
			btcUmic.setFecInitramo(fecIniTramoList);
			btcUmic.setFecfintramo(fecFinTramoList);
			btcUmic.setGtoprov(BigDecimal.ZERO);
			btcUmic.setGtoUni(BigDecimal.ZERO);
			btcUmic.setFactor1(BigDecimal.ZERO);
			btcUmic.setFactor1_2(BigDecimal.ZERO);
			btcUmic.setFactor1_3(BigDecimal.ZERO);
			btcUmic.setFactor1_4(BigDecimal.ZERO);
			btcUmic.setFactor1_5(BigDecimal.ZERO);
			final List<BigDecimal> factor2Cero = new ArrayList<BigDecimal>();
			for (int i = 0; i < 10; i++) {
				factor2Cero.add(BigDecimal.ZERO);
			}
			btcUmic.setFactor2(factor2Cero);
			btcUmic.setFactor2_2(factor2Cero);
			btcUmic.setFactor2_3(factor2Cero);
			btcUmic.setFactor2_4(factor2Cero);
			btcUmic.setFactor2_5(factor2Cero);
			// GBT v1 Final - Cambios pedidos
		}
	}

	public void establecerFecIniFinTramoX(Umic umic, DetalleBaseTecnica btcUmic, BaseTecnicaInicial bti) {

		final List<Timestamp> lstFecIniTramo = new ArrayList<Timestamp>();
		final List<Timestamp> lstFecFinTramo = new ArrayList<Timestamp>();
		final List<BigDecimal> itcalc = new ArrayList<BigDecimal>();
		final List<String> lstSwcasado = new ArrayList<String>();
		final ArrayList<String> grupoActivoPasivoAsegurados = new ArrayList<String>();

		List<String> criterio = new ArrayList<String>();
		List<BigDecimal> prenta = new ArrayList<BigDecimal>();
		List<String> casado = new ArrayList<String>();
		List<String> gap = new ArrayList<String>();

		if (umic.getBti().getFecIniTramo1() != null) {
			casado.add(umic.getBti().getSwcasadoI1());
			gap.add(umic.getBti().getGapI1());
		}
		if (umic.getBti().getFecIniTramo2() != null) {
			casado.add(umic.getBti().getSwcasadoI2());
			gap.add(umic.getBti().getGapI2());
		}
		if (umic.getBti().getFecIniTramo3() != null) {
			casado.add(umic.getBti().getSwcasadoI3());
			gap.add(umic.getBti().getGapI3());
		}
		if (umic.getBti().getFecIniTramo4() != null) {
			casado.add(umic.getBti().getSwcasadoI4());
			gap.add(umic.getBti().getGapI4());
		}
		if (umic.getBti().getFecIniTramo5() != null) {
			casado.add(umic.getBti().getSwcasadoI5());
			gap.add(umic.getBti().getGapI5());
		}

		// Busco en GAR0 el GAP del primer tramo y la fecha de cierre, para extraer la
		// fecha de recasamiento.

		List<RentaGAP> rgap = getRentasGAP(gap.get(0), umic);

		if (rgap.isEmpty()) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_GAR_VACIO);
			exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_GAR_VACIO);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_GAR_VACIO + "(" + gap.get(0) + ")");
			throw exc;
		} else if (rgap.size() > 1) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_GAR_MAS);
			exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_GAR_MAS);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_GAR_MAS + "(" + gap.get(0) + ")");
			throw exc;
		}

		Timestamp fechaRecasamiento = rgap.get(0).getFfincasado();
		// Buscar en la tabla ITG0 el criterio del tramo 2
		List<AsigInteresTecnico> itg = getInteresTecnico(btcUmic, umic, gap.get(1), casado.get(1));

		if (itg.isEmpty()) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ITG_VACIO);
			exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ITG_VACIO);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ITG_VACIO + " GAP: " + gap.get(1));
			throw exc;
		}

		String criterioRecasamiento = itg.get(0).getKcriterioit();
		// Comprobar que la fecha de recasamiento existe y que tenga el segundo tramo el
		// criterio IT04 en el ITG0 ().
		// Comprobar que la fecha de recasamiento es mayor a la fecha de fin del tramo
		// 1.

		if (criterioRecasamiento.equalsIgnoreCase("IT04") && !(null == fechaRecasamiento)
				&& fechaRecasamiento.after(bti.getFecFinTramo1())) {

			lstFecIniTramo.add(bti.getFecIniTramo1());
			lstFecIniTramo.add(bti.getFecFinTramo1());
			lstFecIniTramo.add(fechaRecasamiento);
			lstFecIniTramo.add(bti.getFecIniTramo4());
			lstFecIniTramo.add(bti.getFecIniTramo5());
			lstFecFinTramo.add(bti.getFecFinTramo1());
			lstFecFinTramo.add(fechaRecasamiento);
			lstFecFinTramo.add(bti.getFecFinTramo2());
			lstFecFinTramo.add(bti.getFecFinTramo4());
			lstFecFinTramo.add(bti.getFecFinTramo5());

			itcalc.add(bti.getPintertecnI1());
			itcalc.add(bti.getPintertecnI2());
			itcalc.add(bti.getPintertecnI2());
			itcalc.add(bti.getPintertecnI4());
			itcalc.add(bti.getPintertecnI5());
			btcUmic.setItcalc(itcalc);

			lstSwcasado.add(bti.getSwcasadoI1());
			lstSwcasado.add(bti.getSwcasadoI1());
			lstSwcasado.add(bti.getSwcasadoI2());
			lstSwcasado.add(bti.getSwcasadoI4());
			lstSwcasado.add(bti.getSwcasadoI5());

			grupoActivoPasivoAsegurados.add(umic.getBti().getGapI1());
			grupoActivoPasivoAsegurados.add(umic.getBti().getGapI1());
			grupoActivoPasivoAsegurados.add(umic.getBti().getGapI2());
			grupoActivoPasivoAsegurados.add(umic.getBti().getGapI4());
			grupoActivoPasivoAsegurados.add(umic.getBti().getGapI5());

			umic.getBti().setGapI2(umic.getBti().getGapI1());
			umic.getBti().setGapI3(umic.getBti().getGapI2());

		} else {

			if (criterioRecasamiento.equalsIgnoreCase("IT04")
					&& ((null == fechaRecasamiento) || !fechaRecasamiento.after(bti.getFecFinTramo1()))) {

				final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
				Incidencia aviso = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(),
						umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(),
						btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(),
						ConstantsFunciones.CTE_COD_ERROR_I008, new Object[] { umic.getBti().getGapI1() });
				servicio.almacenarIncidencias(aviso);
				// WARN: No existe fecha de recasamiento para el GAP X, no se generara el tercer
				// tramo.

			}
//INI-8071849
//				else if(!criterioRecasamiento.equalsIgnoreCase("IT04") && (!(null == fechaRecasamiento) && fechaRecasamiento.after(bti.getFecFinTramo1()))){
			else if (!criterioRecasamiento.equalsIgnoreCase("IT04")
					&& (!(null == fechaRecasamiento) && fechaRecasamiento.before(bti.getFecFinTramo1()))) {
//FIN-8071849 
				final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
				Incidencia aviso = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(),
						umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(),
						btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(),
						ConstantsFunciones.CTE_COD_ERROR_I009, new Object[] { fechaRecasamiento });
				servicio.almacenarIncidencias(aviso);
				// WARN: Gap recasado a fecha X sin criterio IT04, no se generara el tercer
				// tramo.

			}

			lstFecIniTramo.add(bti.getFecIniTramo1());

//INI-8071849			
			if (!(null == fechaRecasamiento)) {
				lstFecIniTramo.add(fechaRecasamiento);
			} else {
				lstFecIniTramo.add(bti.getFecIniTramo2());
			}

//FIN-8071849    

			lstFecIniTramo.add(bti.getFecIniTramo3());
			lstFecIniTramo.add(bti.getFecIniTramo4());
			lstFecIniTramo.add(bti.getFecIniTramo5());

//INI-8071849
			if (!(null == fechaRecasamiento)) {
				lstFecFinTramo.add(fechaRecasamiento);
			} else {
				lstFecFinTramo.add(bti.getFecFinTramo1());
			}
//FIN-8071849
			lstFecFinTramo.add(bti.getFecFinTramo2());
			lstFecFinTramo.add(bti.getFecFinTramo3());
			lstFecFinTramo.add(bti.getFecFinTramo4());
			lstFecFinTramo.add(bti.getFecFinTramo5());

			lstSwcasado.add(bti.getSwcasadoI1());
			lstSwcasado.add(bti.getSwcasadoI2());
			lstSwcasado.add(bti.getSwcasadoI3());
			lstSwcasado.add(bti.getSwcasadoI4());
			lstSwcasado.add(bti.getSwcasadoI5());

			grupoActivoPasivoAsegurados.add(umic.getBti().getGapI1());
			grupoActivoPasivoAsegurados.add(umic.getBti().getGapI2());
			grupoActivoPasivoAsegurados.add(umic.getBti().getGapI3());
			grupoActivoPasivoAsegurados.add(umic.getBti().getGapI4());
			grupoActivoPasivoAsegurados.add(umic.getBti().getGapI5());

		}
		// Si el segundo tramo es IT04 y no exite o es anterior (a la fecha de fin del
		// tramo 1) fecha de recasamiento no existe en el primer tramo
		// WARN: No existe fecha de recasamiento para el GAP X, no se generara el tercer
		// tramo.
		// Si existe fecha de recasamiento pero no tiene el IT04
		// WARN: Gap recasado a fecha X sin criterio IT04, no se generara el tercer
		// tramo.
		//
		// Si se cumplen las dos condiciones
		// Crearemos un segundo tramo desde la fecha de fin del tramo uno hasta la fecha
		// de recasamiento, y un tercer tramo desde la feecha de recasamiento
		// hasta la fecha fin del tramo 2 de la BT Inicial..

		btcUmic.setFecInitramo(lstFecIniTramo);
		btcUmic.setFecfintramo(lstFecFinTramo);
		btcUmic.setSwcasado(lstSwcasado);
		btcUmic.setGrupoActivoPasivo(grupoActivoPasivoAsegurados);

	}

	private void flujosProbablesBTI(Integer kmodalidad, Integer kgarantia, String kprestacion, String kprestacionGen,
			String bt) {
		FlujosProbablesKey key;
		
		if(bt.equals(ConstantesSolvencia.BASE_ROSSPTE) || bt.equals(ConstantesSolvencia.BASE_ROSSPTI) || bt.equals(ConstantesSolvencia.BASE_ROSSPGA)){	
			bt = "ROSSP";
		}

		// Para las bases t�cnicas de SCR, se recuperan los flujos probables de BEL
		if (bt.equals(ConstantesSolvencia.BASE_SCRMFE) || bt.equals(ConstantesSolvencia.BASE_SCRMMI)
				|| bt.equals(ConstantesSolvencia.BASE_SCRVM) || bt.equals(ConstantesSolvencia.BASE_SCRMCF)
				|| bt.equals(ConstantesSolvencia.BASE_SCRMCI) || bt.equals(ConstantesSolvencia.BASE_SCRLFE)
				|| bt.equals(ConstantesSolvencia.BASE_SCRLMI) || bt.equals(ConstantesSolvencia.BASE_SCRINC)
				|| bt.equals(ConstantesSolvencia.BASE_SCRTIU) || bt.equals(ConstantesSolvencia.BASE_SCRTID)
				|| bt.equals(ConstantesSolvencia.BASE_SCRGTO) || bt.equals(ConstantesSolvencia.BASE_SCRAEN)
				|| bt.equals(ConstantesSolvencia.BASE_SCRAEP) || bt.equals(ConstantesSolvencia.BASE_SCRAIN)
				|| bt.equals(ConstantesSolvencia.BASE_SCRAIP) || bt.equals(ConstantesSolvencia.BASE_SCRANM)) {

			key = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacion, ConstantesSolvencia.BASE_BEL);
		} else {
			key = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacion, bt);
		}

		FlujosProbables fp = flujosProbablesDao.get(key);
		if (null == fp) {
			key = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacionGen, bt);
			fp = flujosProbablesDao.get(key);
		}
		try {
			setFpClone(fp.clonar());
			INSTANCE = this;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// BTI para provi
		FlujosProbablesKey keyBti = new FlujosProbablesKey();
		if (bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
				|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
				|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)
				|| bt.equals(ConstantesSolvencia.BASE_BEL) || bt.equals(ConstantesSolvencia.BASE_BELCOA)
				|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")){
			keyBti = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacion,
					ConstantsModulos.CTE_VAL_BTI_PROY);
		}else{
			keyBti = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacion,
					ConstantsModulos.CTE_VAL_BTI);
		}
		FlujosProbables fpBti = flujosProbablesDao.get(keyBti);
		if (null == fpBti) {
			if (bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
					|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)
					|| bt.equals(ConstantesSolvencia.BASE_BEL) || bt.equals(ConstantesSolvencia.BASE_BELCOA)
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")){
				keyBti = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacionGen, ConstantsModulos.CTE_VAL_BTI_PROY);
			}else{
				keyBti = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacionGen, ConstantsModulos.CTE_VAL_BTI);
			}
			fpBti = flujosProbablesDao.get(keyBti);
		}
		// Fallecimiento
		BloqueFlujosProbables fall = fp.getFall();
		if (null == fpBti.getFall().getNominal()) {
			fall.setActualizado(fpBti.getFall().getActualizado());
			fall.setNoanulado(fpBti.getFall().getNoanulado());
			fall.setNominal(fpBti.getFall().getNominal());
			fall.setProbable(fpBti.getFall().getProbable());
		} else {
			if (bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
					|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)
					|| bt.equals(ConstantesSolvencia.BASE_BEL) || bt.equals(ConstantesSolvencia.BASE_BELCOA)
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")){
				fall.setActualizado(ConstantsProcesos.CTE_LEIDO_BTIPR);
				fall.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTIPR);
				fall.setNominal(ConstantsProcesos.CTE_LEIDO_BTIPR);
				fall.setProbable(ConstantsProcesos.CTE_LEIDO_BTIPR);
			}else{
				fall.setActualizado(ConstantsProcesos.CTE_LEIDO_BTI);
				fall.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTI);
				fall.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
				fall.setProbable(ConstantsProcesos.CTE_LEIDO_BTI);
			}
		}
		fall.setProvi(fpBti.getFall().getProvi());
		// Vida
		BloqueFlujosProbables vida = fp.getVida();
		if (null == fpBti.getVida().getNominal()) {
			vida.setActualizado(fpBti.getVida().getActualizado());
			vida.setNoanulado(fpBti.getVida().getNoanulado());
			vida.setNominal(fpBti.getVida().getNominal());
			vida.setProbable(fpBti.getVida().getProbable());
		} else {
			if (bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
					|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)
					|| bt.equals(ConstantesSolvencia.BASE_BEL) || bt.equals(ConstantesSolvencia.BASE_BELCOA)
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")){
				vida.setActualizado(ConstantsProcesos.CTE_LEIDO_BTIPR);
				vida.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTIPR);
				vida.setNominal(ConstantsProcesos.CTE_LEIDO_BTIPR);
				vida.setProbable(ConstantsProcesos.CTE_LEIDO_BTIPR);
			}else{
				vida.setActualizado(ConstantsProcesos.CTE_LEIDO_BTI);
				vida.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTI);
				vida.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
				vida.setProbable(ConstantsProcesos.CTE_LEIDO_BTI);
			}
		}
		vida.setProvi(fpBti.getVida().getProvi());
		// Anulaci�n
		BloqueFlujosProbables anul = fp.getAnul();
		if (null == fpBti.getAnul().getNominal()) {
			anul.setActualizado(fpBti.getAnul().getActualizado());
			anul.setNoanulado(fpBti.getAnul().getNoanulado());
			anul.setNominal(fpBti.getAnul().getNominal());
			anul.setProbable(fpBti.getAnul().getProbable());
		} else {
			if (bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
					|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)
					|| bt.equals(ConstantesSolvencia.BASE_BEL) || bt.equals(ConstantesSolvencia.BASE_BELCOA)
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")){
				anul.setActualizado(ConstantsProcesos.CTE_LEIDO_BTIPR);
				anul.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTIPR);
				anul.setNominal(ConstantsProcesos.CTE_LEIDO_BTIPR);
				anul.setProbable(ConstantsProcesos.CTE_LEIDO_BTIPR);
			}else{
				anul.setActualizado(ConstantsProcesos.CTE_LEIDO_BTI);
				anul.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTI);
				anul.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
				anul.setProbable(ConstantsProcesos.CTE_LEIDO_BTI);
			}
		}
		anul.setProvi(fpBti.getAnul().getProvi());
		// Comisiones
		BloqueFlujosProbables comi = fp.getComi();
		if (null == fpBti.getComi().getNominal()) {
			comi.setActualizado(fpBti.getComi().getActualizado());
			comi.setNoanulado(fpBti.getComi().getNoanulado());
			comi.setNominal(fpBti.getComi().getNominal());
			comi.setProbable(fpBti.getComi().getProbable());
		} else {
			if (bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
					|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)
					|| bt.equals(ConstantesSolvencia.BASE_BEL) || bt.equals(ConstantesSolvencia.BASE_BELCOA)
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")){
				comi.setActualizado(ConstantsProcesos.CTE_LEIDO_BTIPR);
				comi.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTIPR);
				comi.setNominal(ConstantsProcesos.CTE_LEIDO_BTIPR);
				comi.setProbable(ConstantsProcesos.CTE_LEIDO_BTIPR);
			}else{
				comi.setActualizado(ConstantsProcesos.CTE_LEIDO_BTI);
				comi.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTI);
				comi.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
				comi.setProbable(ConstantsProcesos.CTE_LEIDO_BTI);
			}
			
		}
		comi.setProvi(fpBti.getComi().getProvi());
		// Gastos
		BloqueFlujosProbables gast = fp.getGast();
		if (null == fpBti.getGast().getNominal()) {
			gast.setActualizado(fpBti.getGast().getActualizado());
			gast.setNoanulado(fpBti.getGast().getNoanulado());
			gast.setNominal(fpBti.getGast().getNominal());
			gast.setProbable(fpBti.getGast().getProbable());
		} else {
			if (bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
					|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)
					|| bt.equals(ConstantesSolvencia.BASE_BEL) || bt.equals(ConstantesSolvencia.BASE_BELCOA)
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")){
				gast.setActualizado(ConstantsProcesos.CTE_LEIDO_BTIPR);
				gast.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTIPR);
				gast.setNominal(ConstantsProcesos.CTE_LEIDO_BTIPR);
				gast.setProbable(ConstantsProcesos.CTE_LEIDO_BTIPR);
			}else{
				gast.setActualizado(ConstantsProcesos.CTE_LEIDO_BTI);
				gast.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTI);
				gast.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
				gast.setProbable(ConstantsProcesos.CTE_LEIDO_BTI);
			}
			
		}
		gast.setProvi(fpBti.getGast().getProvi());
		// Invalidez
		BloqueFlujosProbables inv = fp.getInva();
		if (null == fpBti.getInva().getNominal()) {
			inv.setActualizado(fpBti.getInva().getActualizado());
			inv.setNoanulado(fpBti.getInva().getNoanulado());
			inv.setNominal(fpBti.getInva().getNominal());
			inv.setProbable(fpBti.getInva().getProbable());
		} else {
			if (bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
					|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)
					|| bt.equals(ConstantesSolvencia.BASE_BEL) || bt.equals(ConstantesSolvencia.BASE_BELCOA)
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")){
				inv.setActualizado(ConstantsProcesos.CTE_LEIDO_BTIPR);
				inv.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTIPR);
				inv.setNominal(ConstantsProcesos.CTE_LEIDO_BTIPR);
				inv.setProbable(ConstantsProcesos.CTE_LEIDO_BTIPR);
			}else{
				inv.setActualizado(ConstantsProcesos.CTE_LEIDO_BTI);
				inv.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTI);
				inv.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
				inv.setProbable(ConstantsProcesos.CTE_LEIDO_BTI);
			}
			
		}
		inv.setProvi(fpBti.getInva().getProvi());
		// Prima
		BloqueFlujosProbables prim = fp.getPrim();
		if (null == fpBti.getPrim().getNominal()) {
			prim.setActualizado(fpBti.getPrim().getActualizado());
			prim.setNoanulado(fpBti.getPrim().getNoanulado());
			prim.setNominal(fpBti.getPrim().getNominal());
			prim.setProbable(fpBti.getPrim().getProbable());
		} else {
			if (bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
					|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)
					|| bt.equals(ConstantesSolvencia.BASE_BEL) || bt.equals(ConstantesSolvencia.BASE_BELCOA)
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")){
				prim.setActualizado(ConstantsProcesos.CTE_LEIDO_BTIPR);
				prim.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTIPR);
				prim.setNominal(ConstantsProcesos.CTE_LEIDO_BTIPR);
				prim.setProbable(ConstantsProcesos.CTE_LEIDO_BTIPR);
			}else{
				prim.setActualizado(ConstantsProcesos.CTE_LEIDO_BTI);
				prim.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTI);
				prim.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
				prim.setProbable(ConstantsProcesos.CTE_LEIDO_BTI);
			}
		}
		prim.setProvi(fpBti.getPrim().getProvi());
		// Nominal y terminal
		if (null == fpBti.getProvNominal()) {
			fp.setProvNominal(fpBti.getProvNominal());
		} else {
			if (bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
					|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)
					|| bt.equals(ConstantesSolvencia.BASE_BEL) || bt.equals(ConstantesSolvencia.BASE_BELCOA)
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")){
				fp.setProvNominal(ConstantsProcesos.CTE_LEIDO_BTIPR);

			}else{
				fp.setProvNominal(ConstantsProcesos.CTE_LEIDO_BTI);
			}
		}
		if (null == fpBti.getProvTerminal()) {
			fp.setProvTerminal(fpBti.getProvTerminal());
		} else {
			if (bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
					|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)
					|| bt.equals(ConstantesSolvencia.BASE_BEL) || bt.equals(ConstantesSolvencia.BASE_BELCOA)
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")){
				fp.setProvTerminal(ConstantsProcesos.CTE_LEIDO_BTIPR);
			}else{
				fp.setProvTerminal(ConstantsProcesos.CTE_LEIDO_BTI);
			}
		}
	}

	public List<RentaGAP> getRentasGAP(String gap, Umic umic) {
		List<RentaGAP> result = null;
		RentaGAPDao dao = new RentaGAPDao();
		result = dao.obtenerRentaGAP(gap, umic.getDatosGenerales().getFecCierre());
		return result;
	}

	public List<AsigInteresTecnico> getInteresTecnico(DetalleBaseTecnica btcUmic, Umic umic, String gap,
			String casado) {
		List<AsigInteresTecnico> result = null;
		AsigInteresTecnicoDao dao = new AsigInteresTecnicoDao();
		if (btcUmic.getBaseTec().equals("ROSSPCSM") || btcUmic.getBaseTec().equals("ROSSPTE") || btcUmic.getBaseTec().equals("ROSSPTI") || btcUmic.getBaseTec().equals("ROSSPGA")) {
			result = dao.obtenerIntTecnico(umic.getDatosGenerales().getFecCierre(), "ROSSP", gap,
					umic.getDatosGenerales().getReglamento(), casado);
		} else if(btcUmic.getBaseTec().equals("BTCOATF")){
			result = dao.obtenerIntTecnico(umic.getDatosGenerales().getFecCierre(), "BTCOA", gap,
					umic.getDatosGenerales().getReglamento(), casado);
		} else {
			result = dao.obtenerIntTecnico(umic.getDatosGenerales().getFecCierre(), btcUmic.getBaseTec(), gap,
					umic.getDatosGenerales().getReglamento(), casado);
		}
		return result;
	}

	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la
	 * llamada a la función que realiza los calculos del modulo.
	 */
	public final Object execute(final Object... args) {

		if (Orquestador.LOG.isTraceEnabled()) {
			Orquestador.LOG.trace("Inicio de execute en clase ModuloBaseTecnica");
		}

		DetalleBaseTecnica btcUmic = null;
		Umic umic = null;
		Boolean simulacion = null;
		setFpClone(null);
		try {
			// Calculo de base tecnica
			btcUmic = (DetalleBaseTecnica) args[ConstantesGBT.PARAM_BTC];
			umic = (Umic) args[ConstantesGBT.PARAM_UMI_BASE_TEC];
			simulacion = (Boolean) args[ConstantesGBT.PARAM_SIMUL];

//			File fichero = new File(FICHERO_RES);
//			if(!fichero.exists()){
//				//Escribir la cabecera
//				FileWriter fw = new FileWriter(fichero);
//				PrintWriter pw = new PrintWriter(fw);
//				pw.println(btcUmic.camposTabString());
//				fw.close();
//			}
//			File ficheroErr = new File(FICHERO_ERR);
//			if(!ficheroErr.exists()){
//				//Escribir la cabecera
//				FileWriter fwErr = new FileWriter(ficheroErr);
//				PrintWriter pwErr = new PrintWriter(fwErr);
//				pwErr.println(new ErrorOrquestador().camposTabString());
//				fwErr.close();
//			}

			informarDatos(umic, btcUmic);
			// Ejecutar los modulos
			moduloBaseTecnica(btcUmic, umic, simulacion);

			// DetalleBaseTecnicaDao dbtDao = new DetalleBaseTecnicaDao();
			// dbtDao.put(btcUmic.getKey(), btcUmic);
			// exporter.putCacheRes(btcUmic.getKey(), btcUmic);

		} catch (GestorBasesTecnicasException e) {
			Orquestador.LOG.error(e.getTextoError(), e);
			UmicKey clave = umic.getKey();
			ErrorOrquestador errorOrq = new ErrorOrquestador(btcUmic.getBaseTec(), umic.getDatosGenerales().getCcanal(),
					umic.getDatosGenerales().getFecCierre(), clave.getKmodalidad(), clave.getKpoliza(),
					clave.getKsubpoliza(), clave.getKcertificado(), clave.getNsuscri(), clave.getNorden(),
					clave.getKgarantia(), clave.getKprestacion(), clave.getKajuste(), clave.getCtipoaport(),
					e.getGeneradorError(), e.getInfAmpliada());

			ErrorOrquestadorDao eoDao = new ErrorOrquestadorDao();
			eoDao.put(errorOrq.getKey(), errorOrq);
			// exporter.putCacheErr(errorOrq.getKey(), errorOrq);

			throw e;
		} catch (Exception e) {
			Orquestador.LOG.error(e.getMessage(), e);
//			try {
			throw e;
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}

		}

		if (Orquestador.LOG.isTraceEnabled()) {
			Orquestador.LOG.trace("Fin de execute en clase ModuloBaseTecnica");
		}

		return btcUmic;
	}

	/**
	 * Ejecuta todos los m�dulos que vienen en la cache de diseno procesos.
	 * 
	 * @param umic
	 * @param detalleBT
	 * @throws GestorBasesTecnicasException
	 */
	private void moduloBaseTecnica(final DetalleBaseTecnica detalleBT, final Umic umic, final Boolean simulacion)
			throws GestorBasesTecnicasException {
		List<DisenoProcesos> dps;
		DisenoProcesosDao dpDao = new DisenoProcesosDao();

		if (detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMFE)
				|| detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMMI)
				|| detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLFE)
				|| detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLMI)
				|| detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCF)
				|| detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCI)
				|| detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC)
				|| detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRVM)
				|| detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRGTO)
				|| detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTIU)
				|| detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTID)
				|| detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEP)
				|| detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEN)
				|| detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIP)
				|| detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIN)
				|| detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM)) {

			// En las bases t�cnicas de estr�s obtenemos el dise�o de procesos de BEL
			dps = dpDao.obtenerDisenoProcesos(PROCESO, umic.getDatosGenerales().getCcanal(),
					ConstantsModulos.CTE_BT_BEL);
		} else if (detalleBT.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17)
				|| detalleBT.getBaseTec().equals(ConstantesSolvencia.BASE_NIF17LIR)
				|| detalleBT.getBaseTec().equals(ConstantesSolvencia.BASE_N17LIRIN)
				|| detalleBT.getBaseTec().equals(ConstantesSolvencia.BASE_NIFF17OCI)
				|| detalleBT.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17IF)
				|| detalleBT.getBaseTec().equals(ConstantesSolvencia.BASE_N17CLIR)) {
			// Se obtiene el proceso BASE_TEC de NIIF17
			dps = dpDao.obtenerDisenoProcesos(PROCESO, umic.getDatosGenerales().getCcanal(),
					ConstantsModulos.CTE_BT_NIIF17);
		} else {
			if (detalleBT.getBaseTec().equals(ConstantesSolvencia.BASE_ROSSPTE)
					|| detalleBT.getBaseTec().equals(ConstantesSolvencia.BASE_ROSSPTI)
					|| detalleBT.getBaseTec().equals(ConstantesSolvencia.BASE_ROSSPGA)) {
				dps = dpDao.obtenerDisenoProcesos(PROCESO, umic.getDatosGenerales().getCcanal(), ConstantesSolvencia.BASE_ROSSP);
			} else {
				dps = dpDao.obtenerDisenoProcesos(PROCESO, umic.getDatosGenerales().getCcanal(), detalleBT.getBaseTec());
			}
		}

		GestorBasesTecnicasException exc = new GestorBasesTecnicasException();
		if (dps.isEmpty()) {
			// exc.setGeneradorError(getNombreServicio());
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_DIP_VACIO);
			exc.setTipoError(ConstantesErrores.CTE_ERROR);
			exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_DIP_VACIO);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_DIP_VACIO);
			throw exc;
		}
		if (dps.size() > 1) {
			// exc.setGeneradorError(getNombreServicio());
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_DIP_MAS);
			exc.setTipoError(ConstantesErrores.CTE_ERROR);
			exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_DIP_MAS);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_DIP_MAS);
			throw exc;
		}
		if (dps.get(0).getElementosSubprocesos().isEmpty()) {
			// exc.setGeneradorError(getNombreServicio());
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_SUBP_VACIO);
			exc.setTipoError(ConstantesErrores.CTE_ERROR);
			exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_SUBP_VACIO);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_SUBP_VACIO);
			throw exc;
		}
		for (ElementoSubproceso elementoSubproceso : ((DisenoProcesos) dps.get(0)).getElementosSubprocesos()) {
			Modulo mod = FactoriaModulos.getModulo(elementoSubproceso.getCelement());
			if (mod == null) {
				// exc.setGeneradorError(getNombreServicio());
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_MODULO_NO_EXISTE);
				exc.setTipoError(ConstantesErrores.CTE_ERROR);
				exc.setTextoError("El m�dulo " + elementoSubproceso.getCelement() + " no existe");
				exc.setInfAmpliada("El m�dulo " + elementoSubproceso.getCelement() + " no existe");
				throw exc;
			}
			if (mod.getNombreServicio() == PROCESO) {
				// exc.setGeneradorError(getNombreServicio());
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_BASETEC);
				exc.setTipoError(ConstantesErrores.CTE_ERROR);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_BASETEC);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_BASETEC);
				throw exc;
			}
			try {
				mod.execute(detalleBT, umic, simulacion);
			} catch (GestorBasesTecnicasException e) {
				throw e;
			} catch (Exception e) {
				// exc.setGeneradorError(getNombreServicio());
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_MOD_NO_CONT_ORQ);
				exc.setTipoError(ConstantesErrores.CTE_ERROR);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_MOD_NO_CONT);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_MOD_NO_CONT);
				throw exc;
			}
		}
	}

	public FlujosProbables getFpClone() {
		return fpClone;
	}

	public void setFpClone(FlujosProbables fpClone) {
		this.fpClone = fpClone;
	}

	public static void setNullInstance() {
		INSTANCE = null;
	}
}
