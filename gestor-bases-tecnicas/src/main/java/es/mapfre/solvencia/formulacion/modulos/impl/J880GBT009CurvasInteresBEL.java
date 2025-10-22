package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.tangosol.util.Filter;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.FlujosProbablesKey;
import es.mapfre.solvencia.dao.impl.conversionesBel.ValoresCurvaTipoDao;
import es.mapfre.solvencia.dao.impl.gbt.AsigCurvaTipoDao;
import es.mapfre.solvencia.dao.impl.gbt.MetodosAdaptacionDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.FlujosProbablesDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresCurvaTipo;
import es.mapfre.solvencia.dominio.gbt.AsigCurvaTipo;
import es.mapfre.solvencia.dominio.gbt.MetodosAdaptacion;
import es.mapfre.solvencia.dominio.maestro.BaseTecnicaInicial;
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
import es.mapfre.solvencia.gbt.util.ConstantesErrores;
import es.mapfre.solvencia.gbt.util.ConstantesGBT;
import es.mapfre.solvencia.gbt.util.ConstantesModulosGBT;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;

public class J880GBT009CurvasInteresBEL implements Modulo {

	GestorBasesTecnicasException exc = new GestorBasesTecnicasException();
	private FlujosProbablesDao flujosProbablesDao = new FlujosProbablesDao();
	public static J880GBT009CurvasInteresBEL INSTANCE = null;
	private FlujosProbables fpClone;

	public static J880GBT009CurvasInteresBEL getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new J880GBT009CurvasInteresBEL();
		}
		return INSTANCE;
	}

	@Override
	public Object execute(Object... args) {

		final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantesGBT.PARAM_BTC];
		final Umic umic = (Umic) args[ConstantesGBT.PARAM_UMI_BASE_TEC];
		setFpClone(null);
		try {

			// Interes calculado
			final BaseTecnicaInicial bti = umic.getBti();
			final List<BigDecimal> itcalc = new ArrayList<BigDecimal>();
			itcalc.add(bti.getPintertecnI1());
			itcalc.add(bti.getPintertecnI2());
			itcalc.add(bti.getPintertecnI3());
			itcalc.add(bti.getPintertecnI4());
			itcalc.add(bti.getPintertecnI5());
			btcUmic.setItcalc(itcalc);
			// Interes calculado

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
			if (datosGenerales.getTipoSubriesgo() == null || datosGenerales.getTipoSubriesgo().equals("")) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_SUBR);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_SUBR);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_SUBR);
				throw exc;
			}
			if (datosGenerales.getKcarterainv() == null || datosGenerales.getKcarterainv().equals("")) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_CARTERA);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_CARTERA);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_CARTERA);
				throw exc;
			}
//			if (datosGenerales.getPb() == null || datosGenerales.getPb().equals("")) {
//				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_PB);
//				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_PB);
//				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_PB);
//				throw exc;
//			}
			if (umic.getDatosGenerales().getFecCierre() == null) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_FCIERRE);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_FCIERRE);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_FCIERRE);
				throw exc;
			}
			if (umic.getRescates().getRiesgrescI() == null || umic.getRescates().getRiesgrescI().equals("")) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_RRES);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_RRES);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_RRES);
				throw exc;
			}
//			if (umic.getPrimas().getCformpago() == null || umic.getPrimas().getCformpago().equals("")) {
//				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_FPAGO);
//				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_FPAGO);
//				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_FPAGO);
//				throw exc;
//			}

			List<AsigCurvaTipo> aci = getCurvasTipo(btcUmic, umic);
			if (aci.isEmpty()) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ACI_VACIO);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ACI_VACIO);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ACI_VACIO + " Cart.Inversion: "
						+ umic.getDatosGenerales().getKcarterainv());
				throw exc;
			}
			if (aci.get(0).getKcurva().equals("") || aci.get(0).getKcurva() == null) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_KCURVA);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_KCURVA);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_KCURVA + " CCart.Inversion: "
						+ umic.getDatosGenerales().getKcarterainv());
				throw exc;
			}
			String kcurva = aci.get(0).getKcurva();
			/*
			 * if(aci.get(0).getSperiodotrans().equals("") ||
			 * aci.get(0).getSperiodotrans()==null){
			 * exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_PER);
			 * exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_PER);
			 * exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_PER); throw exc; }
			 */
			List<ValoresCurvaTipo> vci = getValorCurvaTipo(kcurva, umic);
			if (vci.isEmpty()) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_VCI_VACIO);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_VCI_VACIO);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_VCI_VACIO + " Curva: " + kcurva);
				throw exc;
			}

			Timestamp fechaEfecto = vci.get(0).getFecEfecCurva();

			btcUmic.setCurvaTi(kcurva);
			btcUmic.setFcurvaTi(fechaEfecto);
			if (null != aci.get(0).getSperiodotrans() && null != aci.get(0).getKmadaptac()) {

				String periodo = aci.get(0).getSperiodotrans();
				String metAdap = aci.get(0).getKmadaptac();

				if (periodo.equalsIgnoreCase("S")) {
					// Cuando se tiene periodo transitorio, se indica que hay periodo transitorio y
					// se informa el m�todo correspondiente
					btcUmic.setMetodoPtBel(metAdap);
					btcUmic.setPerTransBel(true);
					// Cuando se tiene periodo transitorio, se indica que hay periodo transitorio y
					// se informa el m�todo correspondiente
					if (metAdap == null || metAdap.equals("")) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_PER_MET);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_PER_MET);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_PER_MET);
						throw exc;
					}
					List<MetodosAdaptacion> mvi = getMetodoAdaptacion(metAdap, umic);
					if (mvi.isEmpty()) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_MVI_VACIO);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_MVI_VACIO);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_MVI_VACIO + " Met.Adap: " + metAdap);
						throw exc;
					} else if (mvi.size() > 1) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_MVI_MAS);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_MVI_MAS);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_MVI_MAS + " Met.Adap: " + metAdap);
						throw exc;
					}
					if (mvi.get(0).getCperpend() == null) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_PERPEN);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_PERPEN);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_PERPEN);
						throw exc;
					}
					if (mvi.get(0).getCperutil() == null || mvi.get(0).getCperutil() == 0) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_PERUT);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_PERUT);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_PERUT);
						throw exc;
					}
					double perPend = new Double(mvi.get(0).getCperpend());
					double perUtil = new Double(mvi.get(0).getCperutil());
					if (perPend > perUtil) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_FACT_INT2);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_FACT_INT2);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_FACT_INT2);
						throw exc;
					}
					BigDecimal factorBel = new BigDecimal(perPend / perUtil);
					factorBel = factorBel.setScale(8, BigDecimal.ROUND_HALF_UP);
					btcUmic.setFactorInterpolInteresesBel(factorBel);
				}
			}
		} catch (Exception e) {
			final BaseTecnicaInicial bti = umic.getBti();
			final List<BigDecimal> itcalc = new ArrayList<BigDecimal>();
			itcalc.add(bti.getPintertecnI1());
			itcalc.add(bti.getPintertecnI2());
			itcalc.add(bti.getPintertecnI3());
			itcalc.add(bti.getPintertecnI4());
			itcalc.add(bti.getPintertecnI5());
			btcUmic.setItcalc(itcalc);
			btcUmic.setCurvaTi(null);
			btcUmic.setFcurvaTi(null);
			btcUmic.setMetodoPtBel(null);
			btcUmic.setPerTransBel(null);
			btcUmic.setFactorInterpolInteresesBel(null);
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

		}

		return null;
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
				|| bt.equals(ConstantesSolvencia.BASE_SCRAIP) || bt.equals(ConstantesSolvencia.BASE_SCRANM)
				|| bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
				|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
				|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)) {

			key = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacion, ConstantesSolvencia.BASE_BEL);
		} else {
			key = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacion, bt);
		}

		FlujosProbables fp = flujosProbablesDao.get(key);
		if (null == fp) {
			if (bt.equals(ConstantesSolvencia.BASE_SCRMFE) || bt.equals(ConstantesSolvencia.BASE_SCRMMI)
					|| bt.equals(ConstantesSolvencia.BASE_SCRVM) || bt.equals(ConstantesSolvencia.BASE_SCRMCF)
					|| bt.equals(ConstantesSolvencia.BASE_SCRMCI) || bt.equals(ConstantesSolvencia.BASE_SCRLFE)
					|| bt.equals(ConstantesSolvencia.BASE_SCRLMI) || bt.equals(ConstantesSolvencia.BASE_SCRINC)
					|| bt.equals(ConstantesSolvencia.BASE_SCRTIU) || bt.equals(ConstantesSolvencia.BASE_SCRTID)
					|| bt.equals(ConstantesSolvencia.BASE_SCRGTO) || bt.equals(ConstantesSolvencia.BASE_SCRAEN)
					|| bt.equals(ConstantesSolvencia.BASE_SCRAEP) || bt.equals(ConstantesSolvencia.BASE_SCRAIN)
					|| bt.equals(ConstantesSolvencia.BASE_SCRAIP) || bt.equals(ConstantesSolvencia.BASE_SCRANM)
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
					|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)) {

				key = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacionGen, ConstantesSolvencia.BASE_BEL);
			} else {
				key = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacionGen, bt);
			}

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
				|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")) {
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
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")) {
				keyBti = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacionGen, ConstantsModulos.CTE_VAL_BTI_PROY);
			}else{
				keyBti = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacionGen, ConstantsModulos.CTE_VAL_BTI);
			}
			fpBti = flujosProbablesDao.get(keyBti);
		} // Fallecimiento
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
		// Anulaci�n
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

	public List<AsigCurvaTipo> getCurvasTipo(DetalleBaseTecnica btcUmic, Umic umic) {
		List<AsigCurvaTipo> result = null;
		List<AsigCurvaTipo> curvaTipo = new ArrayList<AsigCurvaTipo>();
		AsigCurvaTipoDao dao = new AsigCurvaTipoDao();
		String pagoU = "N";
		if (null != umic.getPrimas() && null != umic.getPrimas().getCformpago() && umic.getPrimas().getCformpago().equalsIgnoreCase("9"))
			pagoU = "S";

		String indicadorKapbel;
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
			exc.setGeneradorError(getNombreServicio());
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_KAPBEL);
			exc.setTipoError(ConstantesErrores.CTE_ERROR);
			exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_KAPBEL);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_KAPBEL);
			throw exc;
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

	public List<ValoresCurvaTipo> getValorCurvaTipo(String kcurva, Umic umic) {
		List<ValoresCurvaTipo> result = null;
		ValoresCurvaTipoDao dao = new ValoresCurvaTipoDao();
		result = dao.getByFecha(kcurva, umic.getDatosGenerales().getFecCierre());
		return result;
	}

	public List<MetodosAdaptacion> getMetodoAdaptacion(String metAdap, Umic umic) {
		List<MetodosAdaptacion> result = null;
		MetodosAdaptacionDao dao = new MetodosAdaptacionDao();
		result = dao.obtenerMetAdaptacion(metAdap, umic.getDatosGenerales().getFecCierre());
		return result;
	}

	@Override
	public String getNombreServicio() {
		return ConstantesModulosGBT.BEL_TI;
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
