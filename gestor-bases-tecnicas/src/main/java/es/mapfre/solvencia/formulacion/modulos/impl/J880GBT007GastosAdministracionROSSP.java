package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.FlujosProbablesKey;
import es.mapfre.solvencia.dao.impl.gbt.DiferencialGastosDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.FlujosProbablesDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.gbt.DiferencialGastos;
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

public class J880GBT007GastosAdministracionROSSP implements Modulo {

	/** Cte para log. */
	private static final Logger LOG = LoggerFactory.getLogger(J880GBT007GastosAdministracionROSSP.class);
	GestorBasesTecnicasException exc = new GestorBasesTecnicasException();
	private FlujosProbablesDao flujosProbablesDao = new FlujosProbablesDao();
	public static J880GBT007GastosAdministracionROSSP INSTANCE = null;
	private FlujosProbables fpClone;

	public static J880GBT007GastosAdministracionROSSP getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new J880GBT007GastosAdministracionROSSP();
		}
		return INSTANCE;
	}
	@Override
	public String getNombreServicio() {
		return ConstantesModulosGBT.ROSSP_GA;
	}

	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la
	 * llamada a la función que realiza los calculos del modulo.
	 */
	public final Object execute(final Object... args) {

		if (J880GBT007GastosAdministracionROSSP.LOG.isTraceEnabled()) {
			J880GBT007GastosAdministracionROSSP.LOG.trace("Inicio de execute en clase ModuloJ880JGBT10");
		}

		exc.setGeneradorError(getNombreServicio());
		exc.setTipoError(ConstantesErrores.CTE_ERROR);

		final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantesGBT.PARAM_BTC];
		final Umic umic = (Umic) args[ConstantesGBT.PARAM_UMI_BASE_TEC];
		setFpClone(null);
		try {
			// Calculo de base tecnica

			// Realizar la conversion de BTI a ROSSP o BEL
			if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_ROSSPGA)) {
				final BaseTecnicaInicial bti = umic.getBti();
				btcUmic.setGtorosspPrima(bti.getPgastgesin2I());
				btcUmic.setGtorosspCap(bti.getPgastgesin1I());
				btcUmic.setGtorosspProv(bti.getPgastgesin3I());
			}else{
				
				DiferencialGastos elementoDiferencialGastos = recogerDatosEntrada(umic, btcUmic);
				obtenerDatosSalida(btcUmic, elementoDiferencialGastos, umic);
			}

		} catch (Exception e) {

			btcUmic.setGtorosspCap(null);
			btcUmic.setGtorosspPrima(null);
			btcUmic.setGtorosspProv(null);
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
		
		return btcUmic;
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
				|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)
				|| bt.equals(ConstantesSolvencia.BASE_BEL) || bt.equals(ConstantesSolvencia.BASE_BELCOA)
				|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")
				|| bt.equals(ConstantesSolvencia.BASE_NF17AEN) || bt.equals(ConstantesSolvencia.BASE_NF17MFE)
				|| bt.equals(ConstantesSolvencia.BASE_NF17GTO)){
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
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")
					|| bt.equals(ConstantesSolvencia.BASE_NF17AEN) || bt.equals(ConstantesSolvencia.BASE_NF17MFE)
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO)){
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
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")
					|| bt.equals(ConstantesSolvencia.BASE_NF17AEN) || bt.equals(ConstantesSolvencia.BASE_NF17MFE)
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO)){
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
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")
					|| bt.equals(ConstantesSolvencia.BASE_NF17AEN) || bt.equals(ConstantesSolvencia.BASE_NF17MFE)
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO)){
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
		// Anulacion
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
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")
					|| bt.equals(ConstantesSolvencia.BASE_NF17AEN) || bt.equals(ConstantesSolvencia.BASE_NF17MFE)
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO)){
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
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")
					|| bt.equals(ConstantesSolvencia.BASE_NF17AEN) || bt.equals(ConstantesSolvencia.BASE_NF17MFE)
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO)){
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
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")
					|| bt.equals(ConstantesSolvencia.BASE_NF17AEN) || bt.equals(ConstantesSolvencia.BASE_NF17MFE)
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO)){
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
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")
					|| bt.equals(ConstantesSolvencia.BASE_NF17AEN) || bt.equals(ConstantesSolvencia.BASE_NF17MFE)
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO)){
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
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")
					|| bt.equals(ConstantesSolvencia.BASE_NF17AEN) || bt.equals(ConstantesSolvencia.BASE_NF17MFE)
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO)){
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
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")
					|| bt.equals(ConstantesSolvencia.BASE_NF17AEN) || bt.equals(ConstantesSolvencia.BASE_NF17MFE)
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO)){
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
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")
					|| bt.equals(ConstantesSolvencia.BASE_NF17AEN) || bt.equals(ConstantesSolvencia.BASE_NF17MFE)
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO)){
				fp.setProvTerminal(ConstantsProcesos.CTE_LEIDO_BTIPR);
			}else{
				fp.setProvTerminal(ConstantsProcesos.CTE_LEIDO_BTI);
			}
		}
	}

	private DiferencialGastos recogerDatosEntrada(final Umic umic, final DetalleBaseTecnica btcUmic) {

		final DatosGenerales datosGenerales = umic.getDatosGenerales();
		String baseTec = btcUmic.getBaseTec();
		
		if(baseTec.equals(ConstantesSolvencia.BASE_ROSSPTE) || baseTec.equals(ConstantesSolvencia.BASE_ROSSPTI)){	
			baseTec = "ROSSP";
		}
		
		final Timestamp fchCierre = datosGenerales.getFecCierre();
		final Integer modalidad = datosGenerales.getKmodalidad();
		final Integer garantia = datosGenerales.getKgarantia();

		comprobarDatos(baseTec, modalidad, garantia, fchCierre);

		DiferencialGastosDao dgDao = new DiferencialGastosDao();
		List<DiferencialGastos> dg = dgDao.obtenerDifGastos(baseTec, modalidad, garantia, fchCierre);
		DiferencialGastos elementDG = null;

		if (dg == null || dg.isEmpty()) {
			dg = dgDao.obtenerDifGastos(baseTec, modalidad, 0, fchCierre);
			if (dg == null || dg.isEmpty()) {
				dg = dgDao.obtenerDifGastos(baseTec, 0, 0, fchCierre);
				if (dg == null || dg.isEmpty()) {
					elementDG = new DiferencialGastos();
					elementDG.setPdifegap(new BigDecimal(0.0));
					elementDG.setPdifegac(new BigDecimal(0.0));
					elementDG.setPdifegar(new BigDecimal(0.0));
				} else {
					if (dg.size() > 1) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_DIFGASTOS_VARIOS_REGISTROS);
						exc.setTextoError(ConstantesErrores.CTE_DESC_DIFGASTOS_VARIOS_REGISTROS);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_DIFGASTOS_VARIOS_REGISTROS);
						throw exc;
					} else {
						elementDG = dg.get(0);
					}
				}
			} else {
				if (dg.size() > 1) {
					exc.setCodigoRetorno(ConstantesErrores.CTE_COD_DIFGASTOS_VARIOS_REGISTROS);
					exc.setTextoError(ConstantesErrores.CTE_DESC_DIFGASTOS_VARIOS_REGISTROS);
					exc.setInfAmpliada(ConstantesErrores.CTE_DESL_DIFGASTOS_VARIOS_REGISTROS);
					throw exc;
				} else {
					elementDG = dg.get(0);
				}
			}
		} else {
			if (dg.size() > 1) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_DIFGASTOS_VARIOS_REGISTROS);
				exc.setTextoError(ConstantesErrores.CTE_DESC_DIFGASTOS_VARIOS_REGISTROS);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_DIFGASTOS_VARIOS_REGISTROS);
				throw exc;
			} else {
				elementDG = dg.get(0);
			}
		}

		return elementDG;
	}

	private void comprobarDatos(String baseTec, Integer modalidad, Integer garantia, Timestamp fchCierre) {
		if (baseTec == null || baseTec.equals("")) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_BT);
			exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_BT);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_BT);
			throw exc;
		}
		if (modalidad == null) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_MOD);
			exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_MOD);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_MOD);
			throw exc;
		}
		if (garantia == null) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_GAR);
			exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_GAR);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_GAR);
			throw exc;
		}
		if (fchCierre == null) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_FCIERRE);
			exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_FCIERRE);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_FCIERRE);
			throw exc;
		}
	}

	private void obtenerDatosSalida(final DetalleBaseTecnica detalleBT, DiferencialGastos elementDG, final Umic umic) {

		final BaseTecnicaInicial bti = umic.getBti();

		BigDecimal gtorosspCap;
		BigDecimal gtorosspPrima;
		BigDecimal gtorosspProv;

		BigDecimal pgastgesin1I = bti.getPgastgesin1I();
		BigDecimal pgastgesin2I = bti.getPgastgesin2I();
		BigDecimal pgastgesin3I = bti.getPgastgesin3I();

		BigDecimal pdifegac = elementDG.getPdifegac();
		BigDecimal pdifegap = elementDG.getPdifegap();
		BigDecimal pdifegar = elementDG.getPdifegar();

		comprobarValores(pgastgesin1I, pgastgesin2I, pgastgesin3I, pdifegac, pdifegap, pdifegar);

		BigDecimal unidad = new BigDecimal(1);
		BigDecimal constante = new BigDecimal(100);

		gtorosspCap = pgastgesin1I.multiply((pdifegac).add(unidad));

		gtorosspPrima = pgastgesin2I.multiply((pdifegap).add(unidad));

		gtorosspProv = pgastgesin3I.multiply((pdifegar).add(unidad));

		int comparacion = gtorosspCap.compareTo(new BigDecimal(9999.9999));

		if (comparacion == 1) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_GTOCAP_MAXIMO);
			exc.setTextoError(ConstantesErrores.CTE_DESC_GTOCAP_MAXIMO);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_GTOCAP_MAXIMO);
			throw exc;
		}

		comparacion = gtorosspPrima.compareTo(new BigDecimal(9999.9999));

		if (comparacion == 1) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_GTOPRI_MAXIMO);
			exc.setTextoError(ConstantesErrores.CTE_DESC_GTOPRI_MAXIMO);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_GTOPRI_MAXIMO);
			throw exc;
		}

		comparacion = gtorosspProv.compareTo(new BigDecimal(999.99999));

		if (comparacion == 1) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_GTOPROV_MAXIMO);
			exc.setTextoError(ConstantesErrores.CTE_DESC_GTOPROV_MAXIMO);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_GTOPROV_MAXIMO);
			throw exc;
		}

		gtorosspCap = gtorosspCap.setScale(4, BigDecimal.ROUND_HALF_UP);
		gtorosspPrima = gtorosspPrima.setScale(4, BigDecimal.ROUND_HALF_UP);
		gtorosspProv = gtorosspProv.setScale(5, BigDecimal.ROUND_HALF_UP);

		detalleBT.setGtorosspCap(gtorosspCap);
		detalleBT.setGtorosspPrima(gtorosspPrima);
		detalleBT.setGtorosspProv(gtorosspProv);

	}

	private void comprobarValores(BigDecimal pgastgesin1i, BigDecimal pgastgesin2i, BigDecimal pgastgesin3i,
			BigDecimal pdifegap, BigDecimal pdifeprp, BigDecimal pdifersp) {

		if (pgastgesin1i == null) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_PGASTGESIN1I_VACIO);
			exc.setTextoError(ConstantesErrores.CTE_DESC_PGASTGESIN1I_VACIO);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_PGASTGESIN1I_VACIO);
			throw exc;
		}
		if (pgastgesin2i == null) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_PGASTGESIN2I_VACIO);
			exc.setTextoError(ConstantesErrores.CTE_DESC_PGASTGESIN2I_VACIO);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_PGASTGESIN2I_VACIO);
			throw exc;
		}
		if (pgastgesin3i == null) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_PGASTGESIN3I_VACIO);
			exc.setTextoError(ConstantesErrores.CTE_DESC_PGASTGESIN3I_VACIO);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_PGASTGESIN3I_VACIO);
			throw exc;
		}
		if (pdifegap == null) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_PDIFEGAP_VACIO);
			exc.setTextoError(ConstantesErrores.CTE_DESC_PDIFEGAP_VACIO);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_PDIFEGAP_VACIO);
			throw exc;
		}
		if (pdifeprp == null) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_PDIFEPRP_VACIO);
			exc.setTextoError(ConstantesErrores.CTE_DESC_PDIFEPRP_VACIO);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_PDIFEPRP_VACIO);
			throw exc;
		}
		if (pdifersp == null) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_PDIFERSP_VACIO);
			exc.setTextoError(ConstantesErrores.CTE_DESC_PDIFERSP_VACIO);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_PDIFERSP_VACIO);
			throw exc;
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
