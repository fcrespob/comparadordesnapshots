package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.beanio.internal.util.StringUtil;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.FlujosProbablesKey;
import es.mapfre.solvencia.dao.impl.gbt.AsigInteresTecnicoDao;
import es.mapfre.solvencia.dao.impl.gbt.InteresTecnicoDao;
import es.mapfre.solvencia.dao.impl.gbt.RentaGAPDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.FlujosProbablesDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.gbt.AsigInteresTecnico;
import es.mapfre.solvencia.dominio.gbt.InteresTecnico;
import es.mapfre.solvencia.dominio.gbt.RentaGAP;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.BloqueFlujosProbables;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.excepcionGBT.GestorBasesTecnicasException;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.ConstantsProcesos;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.gbt.util.ConstantesErrores;
import es.mapfre.solvencia.gbt.util.ConstantesGBT;
import es.mapfre.solvencia.gbt.util.ConstantesModulosGBT;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;

/**
 * Se obtendr� el tipo de inter�s a aplicar para la actualizaci�n financiera de
 * los flujos en el caso de una ejecuci�n bajo la base t�cnica de ROSSP.
 * 
 * @author Everis
 *
 */
public class J880GBT006TiposInteresROSSP implements Modulo {

	GestorBasesTecnicasException exc = new GestorBasesTecnicasException();
	private FlujosProbablesDao flujosProbablesDao = new FlujosProbablesDao();
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC;
	public static J880GBT006TiposInteresROSSP INSTANCE = null;
	private FlujosProbables fpClone;

	public static J880GBT006TiposInteresROSSP getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new J880GBT006TiposInteresROSSP();
		}
		return INSTANCE;
	}
	@Override
	public Object execute(Object... args) {

		final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantesGBT.PARAM_BTC];
		final Umic umic = (Umic) args[ConstantesGBT.PARAM_UMI_BASE_TEC];
		setFpClone(null);
		try {
			// Base tecnica inicial de la umic y Datos generales
			final DatosGenerales datosGenerales = umic.getDatosGenerales();

			exc.setGeneradorError(getNombreServicio());
			exc.setTipoError(ConstantesErrores.CTE_ERROR);

			if (btcUmic.getBaseTec() == null || btcUmic.getBaseTec().equals("")) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_BT);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_BT);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_BT);
				throw exc;
			}
			if (datosGenerales.getGapAct() == null || datosGenerales.getGapAct().equals("")) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_GAP);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_GAP);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_GAP);
				throw exc;
			}
			if (datosGenerales.getReglamento() == null || datosGenerales.getReglamento().equals("")) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_REG);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_REG);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_REG);
				throw exc;
			}
			if (datosGenerales.getSwcasado() == null || datosGenerales.getSwcasado().equals("")) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_SWCAS);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_SWCAS);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_SWCAS);
				throw exc;
			}
			if (umic.getDatosGenerales().getFecCierre() == null) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_FCIERRE);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_FCIERRE);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_FCIERRE);
				throw exc;
			}

			List<BigDecimal> itcal = new ArrayList<BigDecimal>();
			if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_ROSSPTI)) {
				
				itcal.add(umic.getBti().getPintertecnI1());
				itcal.add(umic.getBti().getPintertecnI2());
				itcal.add(umic.getBti().getPintertecnI3());
				itcal.add(umic.getBti().getPintertecnI4());
				itcal.add(umic.getBti().getPintertecnI5());

			}else{
				itcal.add(umic.getBti().getPintertecnI1());
				itcal.add(umic.getBti().getPintertecnI2());
				if (null != umic.getBti().getGapI3()) {
					itcal.add(btcUmic.getItcalc().get(2));
				} else {
					itcal.add(umic.getBti().getPintertecnI3());
				}
				itcal.add(umic.getBti().getPintertecnI4());
				itcal.add(umic.getBti().getPintertecnI5());
	
				List<String> casado = new ArrayList<String>();
				List<String> gap = new ArrayList<String>();
	
				if (umic.getBti().getFecIniTramo1() != null) {
					casado.add(umic.getBti().getSwcasadoI1());
					gap.add(umic.getBti().getGapI1());
				}
				if (umic.getBti().getFecIniTramo2() != null) {
					if (null != umic.getBti().getGapI3()) {
						casado.add(btcUmic.getSwcasado().get(1));
					} else {
						casado.add(umic.getBti().getSwcasadoI2());
					}
					gap.add(umic.getBti().getGapI2());
				}
				if (umic.getBti().getFecIniTramo3() != null) {
					casado.add(umic.getBti().getSwcasadoI3());
					gap.add(umic.getBti().getGapI3());
				}
				if (null != umic.getBti().getGapI3()) {
					casado.add(btcUmic.getSwcasado().get(2));
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
	
				List<BigDecimal> pitmed = new ArrayList<BigDecimal>();
				List<BigDecimal> pitmaxcalc = new ArrayList<BigDecimal>();
				List<String> criterio = new ArrayList<String>();
				List<BigDecimal> prenta = new ArrayList<BigDecimal>();
	
				if (umic.getBti().getSwcasadoI1() == null || umic.getBti().getSwcasadoI1().equals("")) {
					exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_SWCAS);
					exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_SWCAS);
					exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_SWCAS);
					throw exc;
				}
				if (gap.get(0) == null || gap.get(0).equals("")) {
					exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_GAP);
					exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_GAP);
					exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_GAP);
					throw exc;
				}
				if (casado.get(0) == null || casado.get(0).equals("")) {
					exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_CAS);
					exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_CAS);
					exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_CAS);
					throw exc;
				}
				List<AsigInteresTecnico> itg = getInteresTecnico(btcUmic, umic, gap.get(0), casado.get(0));
				if (itg.isEmpty()) {
					exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ITG_VACIO);
					exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ITG_VACIO);
					exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ITG_VACIO + " GAP: " + gap.get(0));
					throw exc;
				}
				pitmed.add(itg.get(0).getPitmedio());
				pitmaxcalc.add(itg.get(0).getPitmaxcalc());
				criterio.add(itg.get(0).getKcriterioit());
	
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
				prenta.add(rgap.get(0).getPrentabil());
	
				if (umic.getBti().getFecIniTramo2() != null) {
					if (gap.get(1) == null || casado.get(1) == null) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_GAP_CAS);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_GAP_CAS);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_GAP_CAS);
						throw exc;
					}
					itg = getInteresTecnico(btcUmic, umic, gap.get(1), casado.get(1));
					if (itg.isEmpty()) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ITG_VACIO);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ITG_VACIO);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ITG_VACIO + " GAP: " + gap.get(1));
						throw exc;
					}
					pitmed.add(itg.get(0).getPitmedio());
					pitmaxcalc.add(itg.get(0).getPitmaxcalc());
					criterio.add(itg.get(0).getKcriterioit());
	
					rgap = getRentasGAP(gap.get(1), umic);
	
					if (rgap.isEmpty()) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_GAR_VACIO);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_GAR_VACIO);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_GAR_VACIO + "(" + gap.get(1) + ")");
						throw exc;
					} else if (rgap.size() > 1) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_GAR_MAS);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_GAR_MAS);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_GAR_MAS + "(" + gap.get(1) + ")");
						throw exc;
					}
					prenta.add(rgap.get(0).getPrentabil());
				}
	
				if (null != umic.getBti().getGapI3()) {
					if (gap.get(2) == null || casado.get(2) == null) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_GAP_CAS);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_GAP_CAS);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_GAP_CAS);
						throw exc;
					}
					itg = getInteresTecnico(btcUmic, umic, gap.get(2), casado.get(2));
					if (itg.isEmpty()) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ITG_VACIO);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ITG_VACIO);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ITG_VACIO + " GAP: " + gap.get(2));
						throw exc;
					}
					pitmed.add(itg.get(0).getPitmedio());
					pitmaxcalc.add(itg.get(0).getPitmaxcalc());
					criterio.add(itg.get(0).getKcriterioit());
	
					rgap = getRentasGAP(gap.get(2), umic);
	
					if (rgap.isEmpty()) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_GAR_VACIO);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_GAR_VACIO);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_GAR_VACIO + "(" + gap.get(2) + ")");
						throw exc;
					} else if (rgap.size() > 1) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_GAR_MAS);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_GAR_MAS);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_GAR_MAS + "(" + gap.get(2) + ")");
						throw exc;
					}
					prenta.add(rgap.get(0).getPrentabil());
				}
	
				if (umic.getBti().getFecIniTramo3() != null) {
					if (gap.get(2) == null || casado.get(2) == null) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_GAP_CAS);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_GAP_CAS);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_GAP_CAS);
						throw exc;
					}
					itg = getInteresTecnico(btcUmic, umic, gap.get(2), casado.get(2));
					if (itg.isEmpty()) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ITG_VACIO);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ITG_VACIO);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ITG_VACIO + " GAP: " + gap.get(2));
						throw exc;
					}
					pitmed.add(itg.get(0).getPitmedio());
					pitmaxcalc.add(itg.get(0).getPitmaxcalc());
					criterio.add(itg.get(0).getKcriterioit());
	
					rgap = getRentasGAP(gap.get(2), umic);
	
					if (rgap.isEmpty()) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_GAR_VACIO);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_GAR_VACIO);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_GAR_VACIO + "(" + gap.get(2) + ")");
						throw exc;
					} else if (rgap.size() > 1) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_GAR_MAS);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_GAR_MAS);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_GAR_MAS + "(" + gap.get(2) + ")");
						throw exc;
					}
					prenta.add(rgap.get(0).getPrentabil());
				}
	
				if (umic.getBti().getFecIniTramo4() != null) {
					if (gap.get(3) == null || casado.get(3) == null) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_GAP_CAS);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_GAP_CAS);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_GAP_CAS);
						throw exc;
					}
					itg = getInteresTecnico(btcUmic, umic, gap.get(3), casado.get(3));
					if (itg.isEmpty()) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ITG_VACIO);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ITG_VACIO);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ITG_VACIO + " GAP: " + gap.get(3));
						throw exc;
					}
					pitmed.add(itg.get(0).getPitmedio());
					pitmaxcalc.add(itg.get(0).getPitmaxcalc());
					criterio.add(itg.get(0).getKcriterioit());
	
					rgap = getRentasGAP(gap.get(3), umic);
	
					if (rgap.isEmpty()) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_GAR_VACIO);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_GAR_VACIO);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_GAR_VACIO + "(" + gap.get(3) + ")");
						throw exc;
					} else if (rgap.size() > 1) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_GAR_MAS);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_GAR_MAS);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_GAR_MAS + "(" + gap.get(3) + ")");
						throw exc;
					}
					prenta.add(rgap.get(0).getPrentabil());
				}
	
				if (umic.getBti().getFecIniTramo5() != null) {
					if (gap.get(4) == null || casado.get(4) == null) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_GAP_CAS);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_GAP_CAS);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_GAP_CAS);
						throw exc;
					}
					itg = getInteresTecnico(btcUmic, umic, gap.get(4), casado.get(4));
					if (itg.isEmpty()) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ITG_VACIO);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ITG_VACIO);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ITG_VACIO + " GAP: " + gap.get(4));
						throw exc;
					}
					pitmed.add(itg.get(0).getPitmedio());
					pitmaxcalc.add(itg.get(0).getPitmaxcalc());
					criterio.add(itg.get(0).getKcriterioit());
	
					rgap = getRentasGAP(gap.get(4), umic);
	
					if (rgap.isEmpty()) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_GAR_VACIO);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_GAR_VACIO);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_GAR_VACIO + "(" + gap.get(4) + ")");
						throw exc;
					} else if (rgap.size() > 1) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_GAR_MAS);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_GAR_MAS);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_GAR_MAS + "(" + gap.get(4) + ")");
						throw exc;
					}
					prenta.add(rgap.get(0).getPrentabil());
				}
	
				List<Timestamp> fechasInicioTramos = new ArrayList<Timestamp>();
				if (null != umic.getBti().getGapI3()) {
					fechasInicioTramos.add(btcUmic.getFecInitramo().get(0));
					fechasInicioTramos.add(btcUmic.getFecInitramo().get(1));
					fechasInicioTramos.add(btcUmic.getFecInitramo().get(2));
				} else {
					fechasInicioTramos.add(umic.getBti().getFecIniTramo1());
					fechasInicioTramos.add(umic.getBti().getFecIniTramo2());
					fechasInicioTramos.add(umic.getBti().getFecIniTramo3());
				}
				fechasInicioTramos.add(umic.getBti().getFecIniTramo4());
				fechasInicioTramos.add(umic.getBti().getFecIniTramo5());
	
				List<Timestamp> fechasFinTramos = new ArrayList<Timestamp>();
				if (null != umic.getBti().getGapI3()) {
					fechasFinTramos.add(btcUmic.getFecfintramo().get(0));
					fechasFinTramos.add(btcUmic.getFecfintramo().get(1));
					fechasFinTramos.add(btcUmic.getFecfintramo().get(2));
				} else {
					fechasFinTramos.add(umic.getBti().getFecFinTramo1());
					fechasFinTramos.add(umic.getBti().getFecFinTramo2());
					fechasFinTramos.add(umic.getBti().getFecFinTramo3());
				}
				fechasFinTramos.add(umic.getBti().getFecFinTramo4());
				fechasFinTramos.add(umic.getBti().getFecFinTramo5());
	
				List<String> tramosCasados = new ArrayList<String>();
				if (null != umic.getBti().getGapI3()) {
					tramosCasados.add(btcUmic.getSwcasado().get(0));
					tramosCasados.add(btcUmic.getSwcasado().get(1));
					tramosCasados.add(btcUmic.getSwcasado().get(2));
				} else {
					tramosCasados.add(umic.getBti().getSwcasadoI1());
					tramosCasados.add(umic.getBti().getSwcasadoI2());
					tramosCasados.add(umic.getBti().getSwcasadoI3());
				}
				tramosCasados.add(umic.getBti().getSwcasadoI4());
				tramosCasados.add(umic.getBti().getSwcasadoI5());
	
				Boolean swcasado = true;
	
				// Comprueba en que tramo se encuentra la fecha de cierre, para contabilizar el
				// swcasado de los distintos tramos a partir de ese.
				Integer tramo = 0;
				for (int i = 0; i < fechasInicioTramos.size(); i++) {
					if (!(umic.getDatosGenerales().getFecCierre().before(fechasInicioTramos.get(i)))
							&& umic.getDatosGenerales().getFecCierre().before(fechasFinTramos.get(i))) {
						tramo = i;
						break;
					}
				}
				// Comprueba si alguno de los tramos desde el que se encuentra la fecha cierra
				// es casada o no
				for (int i = tramo; i < tramosCasados.size(); i++) {
					if (tramosCasados.get(i).equals("S")) {
						swcasado = false;
						break;
					}
				}
				for (int index = 0; index < casado.size(); index++) {
					BigDecimal intSub = itcal.get(index);
					if (criterio.get(index).equalsIgnoreCase("")) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_CRIT);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_CRIT);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_CRIT);
						throw exc;
					} else if (criterio.get(index).equalsIgnoreCase("IT01")) {
						if (prenta.get(index).compareTo(pitmed.get(index)) < 0) { // prenta < pitmed
							if (prenta.get(index).compareTo(intSub) < 0) { // prenta < subscripcion
								itcal.set(index, prenta.get(index));
							}
						}
					} else if (criterio.get(index).equalsIgnoreCase("IT02")) {
						List<InteresTecnico> itDGS = getInteresTecnicoDGS(umic);
						if (itDGS.isEmpty()) {
							exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ITR_VACIO);
							exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ITR_VACIO);
							exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ITR_VACIO);
							throw exc;
						} else if (itDGS.size() > 1) {
							exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ITR_MAS);
							exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ITR_MAS);
							exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ITR_MAS);
							throw exc;
						}
						BigDecimal pitref = itDGS.get(0).getPitref();
						if (prenta.get(index).compareTo(pitmed.get(index)) < 0) { // prenta < pitmed
							BigDecimal aux = null;
							aux = (prenta.get(index).compareTo(pitref) < 0) ? prenta.get(index) : pitref;
	
							if (aux.compareTo(intSub) < 0) { // aux < subscripcion
								itcal.set(index, aux);
							}
						} else { // prenta >= pitmed
							if (pitref.compareTo(intSub) < 0) { // pitref < subscripcion
								itcal.set(index, pitref);
							}
						}
					} else if (criterio.get(index).equalsIgnoreCase("IT03")) {
						if (prenta.get(index).compareTo(pitmed.get(index)) < 0) {
							itcal.set(index, intSub.subtract(pitmed.get(index).subtract(prenta.get(index))));
						}
					} else if (criterio.get(index).equalsIgnoreCase("IT04")) {
	
						/*
						* if(index < ConstantsFunciones.CTE_2){
						* 
						* itcal.set(index,
						* intSub.subtract(pitmed.get(index).subtract(prenta.get(index))));
						* 
						* }
						*/
	
						List<InteresTecnico> itDGS = getInteresTecnicoDGS(umic);
						if (itDGS.isEmpty()) {
							exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ITR_VACIO);
							exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ITR_VACIO);
							exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ITR_VACIO);
							throw exc;
						} else if (itDGS.size() > 1) {
							exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ITR_MAS);
							exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ITR_MAS);
							exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ITR_MAS);
							throw exc;
						}
						BigDecimal pitref = itDGS.get(0).getPitref();
	
						if (index == ConstantsFunciones.CTE_2) {
							// Calcular con el dato de la base tecnica inicial, o con el calculo hecho
							// anteriormente para el tramo 2 nuevo.
	
							/*
							* if(pitref.compareTo(umic.getBti().getPintertecnI2().subtract(pitmaxcalc.get(2
							* ))) < 0){ itcal.set(index,
							* umic.getBti().getPintertecnI2().subtract(pitmaxcalc.get(2))); }else{
							* itcal.set(index,pitref); }
							*/
	
							if (pitref.compareTo(umic.getBti().getPintertecnI2()) < 0) {
								itcal.set(index, pitref);
							} else {
								itcal.set(index, umic.getBti().getPintertecnI2());
							}
	
						}
	
					} else if (criterio.get(index).equalsIgnoreCase("IT05")) {
						// Si no hay casado en toda la duracion pendiente
						if (swcasado) {
							List<InteresTecnico> itDGS = getInteresTecnicoDGSmaxIT05(umic);
							if (itDGS.isEmpty()) {
								exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ITR_VACIO);
								exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ITR_VACIO);
								exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ITR_VACIO);
								throw exc;
							} else if (itDGS.size() > 1) {
								exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ITR_MAS);
								exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ITR_MAS);
								exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ITR_MAS);
								throw exc;
							}
							// Caclcular DGS con la fecha de efecto
							BigDecimal pitref = itDGS.get(0).getPitref();
							if (prenta.get(index).compareTo(pitmed.get(index)) < 0) { // prenta < pitmed
								BigDecimal aux = null;
								aux = (prenta.get(index).compareTo(pitref) < 0) ? prenta.get(index) : pitref;
	
								if (aux.compareTo(intSub) < 0) { // aux < subscripcion
									itcal.set(index, aux);
								}
							} else { // prenta >= pitmed
								if (pitref.compareTo(intSub) < 0) { // pitref < subscripcion
									itcal.set(index, pitref);
								}
							}
							// Alguno casado de los tramos pendientes
						} else {
							List<InteresTecnico> itDGS = getInteresTecnicoDGSactual(umic);
							if (itDGS.isEmpty()) {
								exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ITR_VACIO);
								exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ITR_VACIO);
								exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ITR_VACIO);
								throw exc;
							} else if (itDGS.size() > 1) {
								exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ITR_MAS);
								exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ITR_MAS);
								exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ITR_MAS);
								throw exc;
							}
							// BigDecimal pitref = itDGS.get(0).getPitref();
							// DGS con fecha de calculo
							if (prenta.get(index).compareTo(pitmed.get(index)) < 0) { // prenta < pitmed
								BigDecimal aux = null;
								aux = (prenta.get(index).compareTo(pitmaxcalc.get(index)) < 0) ? prenta.get(index)
										: pitmaxcalc.get(index);
	
								if (aux.compareTo(intSub) < 0) { // aux < subscripcion
									itcal.set(index, aux);
								}
							} else { // prenta >= pitmed
								if (pitmaxcalc.get(index).compareTo(intSub) < 0) { // pitref < subscripcion
									itcal.set(index, pitmaxcalc.get(index));
								}
							}
	
						}
					} else if (criterio.get(index).equalsIgnoreCase("IT06")) {
						// No casado en toda la duracion pendiente
						if (swcasado) {
							if (prenta.get(index).compareTo(pitmed.get(index)) < 0) { // prenta < pitmed
								if (prenta.get(index).compareTo(intSub) < 0) { // prenta < subscripcion
									itcal.set(index, prenta.get(index));
								}
							}
							// Cuando la operacion tiene un tramo casado en vigor entre Cierre y Vencimiento
						} else {
							List<InteresTecnico> itDGS = getInteresTecnicoDGSactual(umic);
							if (itDGS.isEmpty()) {
								exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ITR_VACIO);
								exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ITR_VACIO);
								exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ITR_VACIO);
								throw exc;
							} else if (itDGS.size() > 1) {
								exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ITR_MAS);
								exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ITR_MAS);
								exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ITR_MAS);
								throw exc;
							}
							// BigDecimal pitref = itDGS.get(0).getPitref();
							if (prenta.get(index).compareTo(pitmed.get(index)) < 0) { // prenta < pitmed
								BigDecimal aux = null;
								aux = (prenta.get(index).compareTo(pitmaxcalc.get(index)) < 0) ? prenta.get(index)
										: pitmaxcalc.get(index);
	
								if (aux.compareTo(intSub) < 0) { // aux < subscripcion
									itcal.set(index, aux);
								}
							} else { // prenta >= pitmed
								if (pitmaxcalc.get(index).compareTo(intSub) < 0) { // pitref < subscripcion
									itcal.set(index, pitmaxcalc.get(index));
								}
							}
	
						}
					} else if (criterio.get(index).equalsIgnoreCase("IT07")) {
	
						if (prenta.get(index).compareTo(pitmed.get(index)) < 0) { // prenta < pitmed
							BigDecimal aux = null;
							aux = (prenta.get(index).compareTo(pitmaxcalc.get(index)) < 0) ? prenta.get(index)
									: pitmaxcalc.get(index);
	
							if (aux.compareTo(intSub) < 0) { // aux < subscripcion
								itcal.set(index, aux);
							}
						} else { // prenta >= pitmed
							if (pitmaxcalc.get(index).compareTo(intSub) < 0) { // pitref < subscripcion
								itcal.set(index, pitmaxcalc.get(index));
							}
						}
	
					} else {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_CRIT_NOVALIDO);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_CRIT_NOVALIDO);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_CRIT_NOVALIDO);
						throw exc;
					}
				}
			}

			btcUmic.setItcalc(itcal);

		} catch (Exception e) {

			List<BigDecimal> itcal = new ArrayList<BigDecimal>();
			itcal.add(umic.getBti().getPintertecnI1());
			itcal.add(umic.getBti().getPintertecnI2());
			itcal.add(umic.getBti().getPintertecnI3());
			itcal.add(umic.getBti().getPintertecnI4());
			itcal.add(umic.getBti().getPintertecnI5());

			btcUmic.setItcalc(itcal);

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
			// umic.getDatosAdicionales().getPrestCal()
		}
		return null;
	}

	private void flujosProbablesBTI(Integer kmodalidad, Integer kgarantia, String kprestacion, String kprestacionGen,
			String bt) {
		
		if(bt.equals(ConstantesSolvencia.BASE_ROSSPTE) || bt.equals(ConstantesSolvencia.BASE_ROSSPTI) || bt.equals(ConstantesSolvencia.BASE_ROSSPGA)){	
			bt = "ROSSP";
		}
		
		FlujosProbablesKey key = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacion, bt);
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
				|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals(ConstantesSolvencia.BASE_NF17AEN) 
				|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)  || bt.equals(ConstantesSolvencia.BASE_NF17GTO)){
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
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals(ConstantesSolvencia.BASE_NF17AEN) 
					|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)  || bt.equals(ConstantesSolvencia.BASE_NF17GTO)){
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
			// fall.setNoanulado(fpBti.getFall().getNoanulado());
			// fall.setNominal(fpBti.getFall().getNominal());
			// fall.setProbable(fpBti.getFall().getProbable());
		} else {
			fall.setActualizado(ConstantsProcesos.CTE_ERROR_BT_ACT);
			// fall.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTI);
			// fall.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
			// fall.setProbable(ConstantsProcesos.CTE_LEIDO_BTI);
		}
		fall.setProvi(fpBti.getFall().getProvi());
		// Vida
		BloqueFlujosProbables vida = fp.getVida();
		if (null == fpBti.getVida().getNominal()) {
			vida.setActualizado(fpBti.getVida().getActualizado());
			// vida.setNoanulado(fpBti.getVida().getNoanulado());
			// vida.setNominal(fpBti.getVida().getNominal());
			// vida.setProbable(fpBti.getVida().getProbable());
		} else {
			vida.setActualizado(ConstantsProcesos.CTE_ERROR_BT_ACT);
			// vida.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTI);
			// vida.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
			// vida.setProbable(ConstantsProcesos.CTE_LEIDO_BTI);
		}
		vida.setProvi(fpBti.getVida().getProvi());
		// Anulacion
		BloqueFlujosProbables anul = fp.getAnul();
		if (null == fpBti.getAnul().getNominal()) {
			anul.setActualizado(fpBti.getAnul().getActualizado());
			// anul.setNoanulado(fpBti.getAnul().getNoanulado());
			// anul.setNominal(fpBti.getAnul().getNominal());
			// anul.setProbable(fpBti.getAnul().getProbable());
		} else {
			anul.setActualizado(ConstantsProcesos.CTE_ERROR_BT_ACT);
			// anul.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTI);
			// anul.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
			// anul.setProbable(ConstantsProcesos.CTE_LEIDO_BTI);
		}
		anul.setProvi(fpBti.getAnul().getProvi());
		// Comisiones
		BloqueFlujosProbables comi = fp.getComi();
		if (null == fpBti.getComi().getNominal()) {
			comi.setActualizado(fpBti.getComi().getActualizado());
			// comi.setNoanulado(fpBti.getComi().getNoanulado());
			// comi.setNominal(fpBti.getComi().getNominal());
			// comi.setProbable(fpBti.getComi().getProbable());
		} else {
			comi.setActualizado(ConstantsProcesos.CTE_ERROR_BT_ACT);
			// comi.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTI);
			// comi.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
			// comi.setProbable(ConstantsProcesos.CTE_LEIDO_BTI);
		}
		comi.setProvi(fpBti.getComi().getProvi());
		// Gastos
		BloqueFlujosProbables gast = fp.getGast();
		if (null == fpBti.getGast().getNominal()) {
			gast.setActualizado(fpBti.getGast().getActualizado());
			// gast.setNoanulado(fpBti.getGast().getNoanulado());
			// gast.setNominal(fpBti.getGast().getNominal());
			// gast.setProbable(fpBti.getGast().getProbable());
		} else {
			gast.setActualizado(ConstantsProcesos.CTE_ERROR_BT_ACT);
			// gast.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTI);
			// gast.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
			// gast.setProbable(ConstantsProcesos.CTE_LEIDO_BTI);
		}
		gast.setProvi(fpBti.getGast().getProvi());
		// Invalidez
		BloqueFlujosProbables inv = fp.getInva();
		if (null == fpBti.getInva().getNominal()) {
			inv.setActualizado(fpBti.getInva().getActualizado());
			// inv.setNoanulado(fpBti.getInva().getNoanulado());
			// inv.setNominal(fpBti.getInva().getNominal());
			// inv.setProbable(fpBti.getInva().getProbable());
		} else {
			inv.setActualizado(ConstantsProcesos.CTE_ERROR_BT_ACT);
			// inv.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTI);
			// inv.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
			// inv.setProbable(ConstantsProcesos.CTE_LEIDO_BTI);
		}
		inv.setProvi(fpBti.getInva().getProvi());
		// Prima
		BloqueFlujosProbables prim = fp.getPrim();
		if (null == fpBti.getPrim().getNominal()) {
			prim.setActualizado(fpBti.getPrim().getActualizado());
			// prim.setNoanulado(fpBti.getPrim().getNoanulado());
			// prim.setNominal(fpBti.getPrim().getNominal());
			// prim.setProbable(fpBti.getPrim().getProbable());
		} else {
			prim.setActualizado(ConstantsProcesos.CTE_ERROR_BT_ACT);
			// prim.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTI);
			// prim.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
			// prim.setProbable(ConstantsProcesos.CTE_LEIDO_BTI);
		}
		prim.setProvi(fpBti.getPrim().getProvi());
		// Nominal y terminal
		if (null == fpBti.getProvNominal()) {
			fp.setProvNominal(fpBti.getProvNominal());
		} else {
			if (bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
					|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR) 
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals(ConstantesSolvencia.BASE_NF17AEN) 
					|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)  || bt.equals(ConstantesSolvencia.BASE_NF17GTO)){
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
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals(ConstantesSolvencia.BASE_NF17AEN) 
					|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)  || bt.equals(ConstantesSolvencia.BASE_NF17GTO)){
				fp.setProvTerminal(ConstantsProcesos.CTE_LEIDO_BTIPR);
			}else{
				fp.setProvTerminal(ConstantsProcesos.CTE_LEIDO_BTI);
			}
		}
	}

	public List<AsigInteresTecnico> getInteresTecnico(DetalleBaseTecnica btcUmic, Umic umic, String gap,
			String casado) {
		List<AsigInteresTecnico> result = null;
		AsigInteresTecnicoDao dao = new AsigInteresTecnicoDao();
		if (btcUmic.getBaseTec().equals("ROSSPCSM") || btcUmic.getBaseTec().equals("ROSSPTE") || btcUmic.getBaseTec().equals("ROSSPGA") ) {
			result = dao.obtenerIntTecnico(umic.getDatosGenerales().getFecCierre(), "ROSSP", gap,
					umic.getDatosGenerales().getReglamento(), casado);
		} else if(btcUmic.getBaseTec().equals("BTCOATF")){
			result = dao.obtenerIntTecnico(umic.getDatosGenerales().getFecCierre(), "BTCOA", gap,
					umic.getDatosGenerales().getReglamento(), casado);
			
		} else {
			result = dao.obtenerIntTecnico(umic.getDatosGenerales().getFecCierre(), btcUmic.getBaseTec(), gap,
					umic.getDatosGenerales().getReglamento(), casado);
		}
//		result = dao.obtenerIntTecnico(umic.getDatosGenerales().getFecCierre(), btcUmic.getBaseTec(), gap, umic.getDatosGenerales().getReglamento(), casado);
		return result;
	}

	public List<RentaGAP> getRentasGAP(String gap, Umic umic) {
		List<RentaGAP> result = null;
		RentaGAPDao dao = new RentaGAPDao();
		result = dao.obtenerRentaGAP(gap, umic.getDatosGenerales().getFecCierre());
		return result;
	}

	public List<InteresTecnico> getInteresTecnicoDGS(Umic umic) {
		List<InteresTecnico> result = null;
		InteresTecnicoDao dao = new InteresTecnicoDao();
		result = dao.obtenerInteresTencico(umic.getDatosGenerales().getFecCierre());
		return result;
	}

	public List<InteresTecnico> getInteresTecnicoDGSefecto(Umic umic) {
		List<InteresTecnico> result = null;
		InteresTecnicoDao dao = new InteresTecnicoDao();
		Timestamp varFechaEfecto = null;

		if (ConstantsFunciones.CTE_APOR_REDUCIDA.equals(umic.getDatosGenerales().getCsitupol())
				&& ConstantesSolvencia.NEGOCIO_COLECTIVO.equals(umic.getDatosGenerales().getCnegocio())) {
			varFechaEfecto = umic.getFechas().getFecinisus();
		} else {
			varFechaEfecto = UtilModulos.getVarFecEfectoGBT(CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(),
					umic.getCapitales().getIsaldo());
		}
		result = dao.obtenerInteresTencico(varFechaEfecto);
		return result;
	}

	public List<InteresTecnico> getInteresTecnicoDGSactual(Umic umic) {
		List<InteresTecnico> result = null;
		InteresTecnicoDao dao = new InteresTecnicoDao();
		result = dao.obtenerInteresTencico(
				UtilFechas.incrDias(umic.getDatosGenerales().getFecCierre(), ConstantsFunciones.CTE_1));
		return result;
	}

	public List<InteresTecnico> getInteresTecnicoDGSmaxIT05(Umic umic) {

		List<InteresTecnico> result = null;
		InteresTecnicoDao dao = new InteresTecnicoDao();

		if (ConstantesSolvencia.NEGOCIO_COLECTIVO.equals(umic.getDatosGenerales().getCnegocio())) {
			result = dao.obtenerInteresTencicoIT05(umic.getFechas().getFecefecini());
		} else {
			result = dao.obtenerInteresTencicoIT05(umic.getFechas().getFecinisus());
		}

		return result;

	}

	@Override
	public String getNombreServicio() {
		return ConstantesModulosGBT.ROSSP_TI;
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
