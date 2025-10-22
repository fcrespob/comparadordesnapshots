package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.tangosol.util.Filter;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.FlujosProbablesKey;
import es.mapfre.solvencia.dao.impl.conversionesBel.ValoresCurvaTipoDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.AsigCurvasTipoUOADao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.FlujosProbablesDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresCurvaTipo;
import es.mapfre.solvencia.dominio.gbt.AsigCurvaTipo;
import es.mapfre.solvencia.dominio.maestro.BaseTecnicaInicial;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.AsigCurvasTipoUOA;
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
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;

public class J880GBT012CurvasCINIIF17 implements Modulo {

	GestorBasesTecnicasException exc = new GestorBasesTecnicasException();
	private FlujosProbablesDao flujosProbablesDao = new FlujosProbablesDao();

	private static final String CLAVE_MESES_CIERRE_NIIF17 = ConstantsModulos.CTE_MESES_CIERRE_NIIF17;

	public static J880GBT012CurvasCINIIF17 INSTANCE = null;
	private FlujosProbables fpClone;

	public static J880GBT012CurvasCINIIF17 getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new J880GBT012CurvasCINIIF17();
		}
		return INSTANCE;
	}

	@Override
	public Object execute(Object... args) {

		final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantesGBT.PARAM_BTC];
		final Umic umic = (Umic) args[ConstantesGBT.PARAM_UMI_BASE_TEC];

		String varMesesCierreNIIF17;
		int mesesCierreN17;
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
			if (umic.getDatosNiif17().getkcarinv17() == null || umic.getDatosNiif17().getkcarinv17().equals("")) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_CARTERAINV17);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_CARTERAINV17);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_CARTERAINV17);
				throw exc;
			}
			if (umic.getDatosNiif17().getkcarinvlir() == null || umic.getDatosNiif17().getkcarinvlir().equals("")) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_CARTERAINVLIR);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_CARTERAINVLIR);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_CARTERAINVLIR);
				throw exc;
			}
			if (umic.getDatosGenerales().getFecCierre() == null) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_FCIERRE);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_FCIERRE);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_FCIERRE);
				throw exc;
			}

			final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
			varMesesCierreNIIF17 = (String) servicio.recuperarDefinicionAuxiliar(umic.getDatosGenerales().getCcartera(),
					umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(),
					ConstantsModulos.CTE_MESES_CIERRE_NIIF17);

			if (null == varMesesCierreNIIF17) {
				mesesCierreN17 = 0;
			} else {
				mesesCierreN17 = Integer.parseInt(varMesesCierreNIIF17);
			}

			List<AsigCurvasTipoUOA> ctu = getCurvasTipoCINIIF17(btcUmic, umic, mesesCierreN17);

			if (ctu.isEmpty()) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_CTU_VACIO);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_CTU_VACIO);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_CTU_VACIO + " Cart.Inversion NIIIF17: "
						+ umic.getDatosNiif17().getkcarinv17());
				throw exc;
			}
			if (ctu.get(0).getkCurva().equals("") || ctu.get(0).getkCurva() == null) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_KCURVA);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_KCURVA);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_KCURVA + " CCart.Inversion NIIIF17: "
						+ umic.getDatosNiif17().getkcarinv17());
				throw exc;
			}

			String kcurva = null;
			Timestamp fechaEf = null;

			if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIFF17OCI)) {
				fechaEf = UtilFechas.getUltimoDiaDelMes(UtilFechas.decreMeses(btcUmic.getFecCierre(), mesesCierreN17));
				kcurva = ctu.get(0).getkCurva();
			} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17LIRIN)) {
				fechaEf = umic.getFechas().getFecinisus();
				kcurva = ctu.get(0).getkCurva();
			} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIF17LIR)) {
				fechaEf = ctu.get(0).getkFecCierreCurv();
				kcurva = ctu.get(0).getkCurva();
			} else {
				fechaEf = UtilFechas
						.decreMeses(new Timestamp(new GregorianCalendar(9999, 12, 31, ConstantsFunciones.CTE_0,
								ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis()), 1);
				kcurva = ctu.get(0).getkCurva();
				// int it = 1;
//				
//				fechaEf = ctu.get(0).getkFecCierreCurv();
//				kcurva = ctu.get(0).getkCurva();
//				while(it < ctu.size()){
//					if(ctu.get(it).getkFecCierreCurv().after(fechaEf)){
//						fechaEf = ctu.get(it).getkFecCierreCurv();
//						kcurva = ctu.get(0).getkCurva();
//					}
//					it++;
//				}
			}

			List<ValoresCurvaTipo> vci = null;
			Timestamp fechaEfecto = null;

			// if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIFF17OCI) ||
			// btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17LIRIN)){
			vci = getValorCurvaTipoCINIIF17(kcurva, fechaEf);

			if (vci.isEmpty()) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_VCI_VACIO);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_VCI_VACIO);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_VCI_VACIO + " Curva: " + kcurva);
				throw exc;
			}

			fechaEfecto = vci.get(0).getFecEfecCurva();
			// }
//			else{
//				vci = getValorCurvaTipoN17(kcurva);
//				
//				if (vci.isEmpty()) {
//					exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_VCI_VACIO);
//					exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_VCI_VACIO);
//					exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_VCI_VACIO + " Curva: " + kcurva);
//					throw exc;
//				}
//				
//				int iter = 1;
//				fechaEfecto = vci.get(0).getFecEfecCurva();
//				while(iter < vci.size()){
//					if(vci.get(iter).getFecEfecCurva().after(fechaEfecto)){
//						fechaEfecto = vci.get(iter).getFecEfecCurva();
//					}
//					iter++;
//				}
//			}
			if(btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIF17LIR)){
				btcUmic.setFcurvaTi(fechaEf);
			}else{
				btcUmic.setFcurvaTi(fechaEfecto);
			}
			
			btcUmic.setCurvaTi(kcurva);
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

	public List<AsigCurvasTipoUOA> getCurvasTipoCINIIF17(DetalleBaseTecnica btcUmic, Umic umic, int meses) {

		List<AsigCurvasTipoUOA> result = new ArrayList<AsigCurvasTipoUOA>();
		AsigCurvasTipoUOADao dao = new AsigCurvasTipoUOADao();
		// int meses = Integer.parseInt(mesesCierre);

		if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17LIRIN)) {
			result = dao.obtenerCurvaTipoCINIIF17(ConstantsModulos.CTE_BT_NIIF17, umic.getDatosNiif17().getuoa(),
					umic.getDatosNiif17().getkcarinv17());
		} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIF17LIR)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17CLIR)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17IF)) {
			result = dao.obtenerCurvaTipoNiif17Lir(ConstantsModulos.CTE_BT_NIIF17, umic.getDatosNiif17().getuoa(),
					umic.getDatosNiif17().getkcarinvlir(), umic.getFechas().getFecinisus(),
					umic.getDatosNiif17().getkcurvalir());
		} else if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIFF17OCI)) {
			Timestamp fecCierreAnt = UtilFechas
					.getUltimoDiaDelMes(UtilFechas.decreMeses(btcUmic.getFecCierre(), meses));
			result = dao.obtenerCurvaTipoCINIIF17(ConstantsModulos.CTE_BT_NIFF17OCI, umic.getDatosNiif17().getuoa(),
					umic.getDatosNiif17().getkcarinv17());
		} else {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_NO_BT_NIIIF17);
			exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_NO_BT_NIIF17);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_BT_NO_NIIF17);
			throw exc;
		}

		if (result.size() == 0) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_NO_BT_VALORES);
			exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_NO_BT_VALORES);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_NO_BT_VALORES);
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

	@Override
	public String getNombreServicio() {
		return ConstantesModulosGBT.NIIF17_CT;
	}

	private void flujosProbablesBTI(Integer kmodalidad, Integer kgarantia, String kprestacion, String kprestacionGen,
			String bt) {
		FlujosProbablesKey key;

		// Para las bases técnicas de SCR, se recuperan los flujos probables de
		// BEL
		if (bt.equals(ConstantesSolvencia.BASE_SCRMFE) || bt.equals(ConstantesSolvencia.BASE_SCRMMI)
				|| bt.equals(ConstantesSolvencia.BASE_SCRVM) || bt.equals(ConstantesSolvencia.BASE_SCRMCF)
				|| bt.equals(ConstantesSolvencia.BASE_SCRMCI) || bt.equals(ConstantesSolvencia.BASE_SCRLFE)
				|| bt.equals(ConstantesSolvencia.BASE_SCRLMI) || bt.equals(ConstantesSolvencia.BASE_SCRINC)
				|| bt.equals(ConstantesSolvencia.BASE_SCRTIU) || bt.equals(ConstantesSolvencia.BASE_SCRTID)
				|| bt.equals(ConstantesSolvencia.BASE_SCRGTO) || bt.equals(ConstantesSolvencia.BASE_SCRAEN)
				|| bt.equals(ConstantesSolvencia.BASE_SCRAEP) || bt.equals(ConstantesSolvencia.BASE_SCRAIN)
				|| bt.equals(ConstantesSolvencia.BASE_SCRAIP) || bt.equals(ConstantesSolvencia.BASE_SCRANM)) {

			key = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacion, ConstantesSolvencia.BASE_BEL);
		} else if (bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
				|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
				|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)) {
			key = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacion, ConstantesSolvencia.BASE_BTIPROY);
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
					|| bt.equals(ConstantesSolvencia.BASE_SCRAIP) || bt.equals(ConstantesSolvencia.BASE_SCRANM)) {

				key = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacionGen, ConstantesSolvencia.BASE_BEL);
			} else if (bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
					|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)) {
				key = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacionGen, ConstantesSolvencia.BASE_BTIPROY);
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
				|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)){
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
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)){
				keyBti = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacionGen, ConstantsModulos.CTE_VAL_BTI_PROY);
			}else{
				keyBti = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacionGen, ConstantsModulos.CTE_VAL_BTI);
			}
			fpBti = flujosProbablesDao.get(keyBti);
		} // Fallecimiento
		BloqueFlujosProbables fall = fp.getFall();
		if (null == fpBti.getFall().getNominal()) {
			fall.setActualizado(fpBti.getFall().getActualizado());
		} else {
			fall.setActualizado(ConstantsProcesos.CTE_ERROR_BT_ACT);

		}
		fall.setProvi(fpBti.getFall().getProvi());
		// Vida
		BloqueFlujosProbables vida = fp.getVida();
		if (null == fpBti.getVida().getNominal()) {
			vida.setActualizado(fpBti.getVida().getActualizado());
		} else {
			vida.setActualizado(ConstantsProcesos.CTE_ERROR_BT_ACT);
		}
		vida.setProvi(fpBti.getVida().getProvi());
		// Anulación
		BloqueFlujosProbables anul = fp.getAnul();
		if (null == fpBti.getAnul().getNominal()) {
			anul.setActualizado(fpBti.getAnul().getActualizado());
		} else {
			anul.setActualizado(ConstantsProcesos.CTE_ERROR_BT_ACT);
		}
		anul.setProvi(fpBti.getAnul().getProvi());
		// Comisiones
		BloqueFlujosProbables comi = fp.getComi();
		if (null == fpBti.getComi().getNominal()) {
			comi.setActualizado(fpBti.getComi().getActualizado());
		} else {
			comi.setActualizado(ConstantsProcesos.CTE_ERROR_BT_ACT);
		}
		comi.setProvi(fpBti.getComi().getProvi());
		// Gastos
		BloqueFlujosProbables gast = fp.getGast();
		if (null == fpBti.getGast().getNominal()) {
			gast.setActualizado(fpBti.getGast().getActualizado());
		} else {
			gast.setActualizado(ConstantsProcesos.CTE_ERROR_BT_ACT);
		}
		gast.setProvi(fpBti.getGast().getProvi());
		// Invalidez
		BloqueFlujosProbables inv = fp.getInva();
		if (null == fpBti.getInva().getNominal()) {
			inv.setActualizado(fpBti.getInva().getActualizado());
		} else {
			inv.setActualizado(ConstantsProcesos.CTE_ERROR_BT_ACT);
		}
		inv.setProvi(fpBti.getInva().getProvi());
		// Prima
		BloqueFlujosProbables prim = fp.getPrim();
		if (null == fpBti.getPrim().getNominal()) {
			prim.setActualizado(fpBti.getPrim().getActualizado());
		} else {
			prim.setActualizado(ConstantsProcesos.CTE_ERROR_BT_ACT);
		}
		prim.setProvi(fpBti.getPrim().getProvi());
		// Nominal y terminal
		if (null == fpBti.getProvNominal()) {
			fp.setProvNominal(fpBti.getProvNominal());
		} else {
			if (bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
					|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)){
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
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)){
				fp.setProvNominal(ConstantsProcesos.CTE_LEIDO_BTIPR);
			}else{
				fp.setProvTerminal(ConstantsProcesos.CTE_LEIDO_BTI);
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
