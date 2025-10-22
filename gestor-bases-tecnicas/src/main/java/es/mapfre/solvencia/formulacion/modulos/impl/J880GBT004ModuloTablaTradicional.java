package es.mapfre.solvencia.formulacion.modulos.impl;

import org.apache.commons.lang3.ArrayUtils;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.FlujosProbablesKey;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.FlujosProbablesDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
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

public class J880GBT004ModuloTablaTradicional implements Modulo {

	GestorBasesTecnicasException exc = new GestorBasesTecnicasException();
	private FlujosProbablesDao flujosProbablesDao = new FlujosProbablesDao();
	public static J880GBT004ModuloTablaTradicional INSTANCE = null;
	private FlujosProbables fpClone;

	public static J880GBT004ModuloTablaTradicional getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new J880GBT004ModuloTablaTradicional();
		}
		return INSTANCE;
	}
	@Override
	public Object execute(Object... args) {
		final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantesGBT.PARAM_BTC];
		final Umic umic = (Umic) args[ConstantesGBT.PARAM_UMI_BASE_TEC];
		setFpClone(null);
		try {
			// Calculo de base tecnica
			// final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica)
			// args[ConstantesGBT.PARAM_BTC];
			btcUmic.setIndTabExp('T');

		} catch (Exception e) {

			btcUmic.setIndTabExp('T');
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
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)){
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
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)){
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
		// Anulación
		BloqueFlujosProbables anul = fp.getAnul();
		if (null == fpBti.getAnul().getNominal()) {
			anul.setActualizado(fpBti.getAnul().getActualizado());
			anul.setNoanulado(fpBti.getAnul().getNoanulado());
			anul.setNominal(fpBti.getAnul().getNominal());
			anul.setProbable(fpBti.getAnul().getProbable());
		} else {
			if (bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
					|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)){
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
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)){
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
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)){
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
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)){
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
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)){
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
				fp.setProvTerminal(ConstantsProcesos.CTE_LEIDO_BTIPR);
			}else{
				fp.setProvTerminal(ConstantsProcesos.CTE_LEIDO_BTI);
			}
		}
	}

	@Override
	public String getNombreServicio() {
		return ConstantesModulosGBT.MOD_TT;
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
