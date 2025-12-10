package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.FlujosProbablesKey;
import es.mapfre.solvencia.dao.impl.conversionesBel.GastosRealesDao;
import es.mapfre.solvencia.dao.impl.gbt.AsigCurvaTipoDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.AsigCurvasTipoUOADao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.FlujosProbablesDao;
import es.mapfre.solvencia.dao.impl.scr.GastosRealesNF17GTODao;
import es.mapfre.solvencia.dao.impl.scr.GastosRealesSCRGTODao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.conversionesBel.GastosReales;
import es.mapfre.solvencia.dominio.gbt.AsigCurvaTipo;
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
import es.mapfre.solvencia.gbt.util.ConstantesErrores;
import es.mapfre.solvencia.gbt.util.ConstantesGBT;
import es.mapfre.solvencia.gbt.util.ConstantesModulosGBT;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;

public class J880GBT010GastosAdministracionBEL implements Modulo {

	/** Cte para log. */
	private static final Logger LOG = LoggerFactory.getLogger(J880GBT010GastosAdministracionBEL.class);
	GestorBasesTecnicasException exc = new GestorBasesTecnicasException();
	private FlujosProbablesDao flujosProbablesDao = new FlujosProbablesDao();
	public static J880GBT010GastosAdministracionBEL INSTANCE = null;
	private FlujosProbables fpClone;

	public static J880GBT010GastosAdministracionBEL getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new J880GBT010GastosAdministracionBEL();
		}
		return INSTANCE;
	}
	@Override
	public String getNombreServicio() {
		return ConstantesModulosGBT.BEL_GA;
	}

	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la
	 * llamada a la función que realiza los calculos del modulo.
	 */
	public final Object execute(final Object... args) {

		if (J880GBT010GastosAdministracionBEL.LOG.isTraceEnabled()) {
			J880GBT010GastosAdministracionBEL.LOG.trace("Inicio de execute en clase ModuloJ880JGBT11");
		}

		exc.setGeneradorError(getNombreServicio());
		exc.setTipoError(ConstantesErrores.CTE_ERROR);
		final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantesGBT.PARAM_BTC];
		final Umic umic = (Umic) args[ConstantesGBT.PARAM_UMI_BASE_TEC];
		setFpClone(null);
		try {
			// Calculo de base tecnica

			// Realizar la conversion de BTI a ROSSP o BEL
			GastosReales elementoGastorReales = recogerDatosEntrada(umic, btcUmic);
			obtenerDatosSalida(btcUmic, elementoGastorReales);

		} catch (Exception e) {

			btcUmic.setGtoUni(BigDecimal.ZERO);
			btcUmic.setGtoprov(BigDecimal.ZERO);
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
				|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)
				|| bt.equals(ConstantesSolvencia.BASE_NF17GTO) || bt.equals(ConstantesSolvencia.BASE_NF17AEN)
				|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)) {

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
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO) || bt.equals(ConstantesSolvencia.BASE_NF17AEN)
					|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)) {

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
		// BTI para provi�
		FlujosProbablesKey keyBti = new FlujosProbablesKey();
		if (bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
				|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
				|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)
				|| bt.equals(ConstantesSolvencia.BASE_BEL) || bt.equals(ConstantesSolvencia.BASE_BELCOA)
				|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")
				|| bt.equals(ConstantesSolvencia.BASE_NF17GTO) || bt.equals(ConstantesSolvencia.BASE_NF17AEN)
				|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)){
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
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO) || bt.equals(ConstantesSolvencia.BASE_NF17AEN)
					|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)){
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
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO) || bt.equals(ConstantesSolvencia.BASE_NF17AEN)
					|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)){
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
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO) || bt.equals(ConstantesSolvencia.BASE_NF17AEN)
					|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)){
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
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO) || bt.equals(ConstantesSolvencia.BASE_NF17AEN)
					|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)){
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
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO) || bt.equals(ConstantesSolvencia.BASE_NF17AEN)
					|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)){
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
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO) || bt.equals(ConstantesSolvencia.BASE_NF17AEN)
					|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)){
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
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO) || bt.equals(ConstantesSolvencia.BASE_NF17AEN)
					|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)){
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
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO) || bt.equals(ConstantesSolvencia.BASE_NF17AEN)
					|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)){
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
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO) || bt.equals(ConstantesSolvencia.BASE_NF17AEN)
					|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)){
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
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO) || bt.equals(ConstantesSolvencia.BASE_NF17AEN)
					|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)){
				fp.setProvTerminal(ConstantsProcesos.CTE_LEIDO_BTI);
			}else{
				fp.setProvTerminal(ConstantsProcesos.CTE_LEIDO_BTI);
			}
		}
	}

	private GastosReales recogerDatosEntrada(final Umic umic, final DetalleBaseTecnica btcUmic) {

		final DatosGenerales datosGenerales = umic.getDatosGenerales();

		final String baseTec = btcUmic.getBaseTec();
		final Timestamp fchCierre = datosGenerales.getFecCierre();
		final Integer compania = datosGenerales.getCcanal();
		final String negocio = datosGenerales.getCnegocio();
		final String ramo = datosGenerales.getKramo();
		final Integer modalidad = datosGenerales.getKmodalidad();

		comprobarDatos(baseTec, compania, negocio, ramo, modalidad, fchCierre);

		GastosRealesDao grDao = new GastosRealesDao();
		GastosRealesSCRGTODao grSCRGTODao = new GastosRealesSCRGTODao();
		GastosRealesNF17GTODao grNF17GTODao = new GastosRealesNF17GTODao();

		List<GastosReales> gr;
		String kcurva = null;
		
		
		List<AsigCurvaTipo> aci = getCurvasTipo(btcUmic, umic);
		if (null == aci || aci.isEmpty()) {
			kcurva = "Sin_Curva";
		} else {
			kcurva = aci.get(0).getKcurva();
		}
			
		
		if (baseTec.equals(ConstantsModulos.CTE_VAL_SCRGTO)) {
			if (kcurva != null 
					&& kcurva.equals("CLR_MA0")) {
				gr = grSCRGTODao.getGastosReales(compania, negocio, fchCierre, modalidad, ramo, baseTec, true, "S");
			} else {
				gr = grSCRGTODao.getGastosReales(compania, negocio, fchCierre, modalidad, ramo, baseTec, true, "N");
			}
		} else if (baseTec.equals(ConstantsModulos.CTE_VAL_NF17GTO)) {
			if (kcurva != null 
					&& kcurva.equals("CLR_MA0")) {
				gr = grNF17GTODao.getGastosReales(compania, negocio, fchCierre, modalidad, ramo, baseTec, true, "S");
			} else {
				gr = grNF17GTODao.getGastosReales(compania, negocio, fchCierre, modalidad, ramo, baseTec, true, "N");
			}
		} else if (baseTec.equals(ConstantsModulos.CTE_VAL_SCRMFE) || baseTec.equals(ConstantsModulos.CTE_VAL_SCRMMI)
				|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRMCF) || baseTec.equals(ConstantsModulos.CTE_VAL_SCRMCI)
				|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRLFE) || baseTec.equals(ConstantsModulos.CTE_VAL_SCRLMI)
				|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRVM) || baseTec.equals(ConstantsModulos.CTE_VAL_SCRINC)
				|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRAEN) || baseTec.equals(ConstantsModulos.CTE_VAL_SCRAEP)
				|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRAIN) || baseTec.equals(ConstantsModulos.CTE_VAL_SCRAIP)
				|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRANM) || baseTec.equals(ConstantsModulos.CTE_VAL_SCRTIU)
				|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRTID) || baseTec.equals(ConstantesSolvencia.BASE_NIIF17)
				|| baseTec.equals(ConstantesSolvencia.BASE_NIF17LIR) || baseTec.equals(ConstantesSolvencia.BASE_N17LIRIN)
				|| baseTec.equals(ConstantesSolvencia.BASE_NIFF17OCI) || baseTec.equals(ConstantesSolvencia.BASE_N17CLIR)
				|| baseTec.equals(ConstantsModulos.CTE_VAL_NF17AEN) || baseTec.equals(ConstantsModulos.CTE_VAL_NF17MFE)) {
			if (kcurva != null 
					&& kcurva.equals("CLR_MA0")) {
				gr = grDao.getGastosReales(compania, negocio, fchCierre, modalidad, ramo, ConstantsModulos.CTE_BT_BEL,
						true, "S");
			} else {
				gr = grDao.getGastosReales(compania, negocio, fchCierre, modalidad, ramo, ConstantsModulos.CTE_BT_BEL,
						true, "N");
			}
		} else if (baseTec.equals(ConstantesSolvencia.BASE_NIIF17IF)) {
			Timestamp fecCierreAnterior = UtilFechas.decreMeses(fchCierre, 1);
			fecCierreAnterior = UtilFechas.getUltimoDiaDelMes(fecCierreAnterior);
			if (kcurva != null 
					&& kcurva.equals("CLR_MA0")) {
				gr = grDao.getGastosReales(compania, negocio, fecCierreAnterior, modalidad, ramo,
						ConstantsModulos.CTE_BT_BEL, true, "S");
			} else {
				gr = grDao.getGastosReales(compania, negocio, fecCierreAnterior, modalidad, ramo,
						ConstantsModulos.CTE_BT_BEL, true, "N");
			}
		} else {
			if (kcurva != null 
					&& kcurva.equals("CLR_MA0")) {
				gr = grDao.getGastosReales(compania, negocio, fchCierre, modalidad, ramo, baseTec, true, "S");
			} else {
				gr = grDao.getGastosReales(compania, negocio, fchCierre, modalidad, ramo, baseTec, true, "N");
			}
		}
		GastosReales elementGR = null;
		if (gr == null || gr.isEmpty()) {
			if (baseTec.equals(ConstantsModulos.CTE_VAL_SCRGTO)) {
				if (kcurva != null 
						&& kcurva.equals("CLR_MA0")) {
					gr = grSCRGTODao.getGastosReales(compania, negocio, fchCierre, 0, ramo, baseTec, true, "S");
				} else {
					gr = grSCRGTODao.getGastosReales(compania, negocio, fchCierre, 0, ramo, baseTec, true, "N");
				}
			} else if (baseTec.equals(ConstantsModulos.CTE_VAL_NF17GTO)) {
				if (kcurva != null 
						&& kcurva.equals("CLR_MA0")) {
					gr = grNF17GTODao.getGastosReales(compania, negocio, fchCierre, 0, ramo, baseTec, true, "S");
				} else {
					gr = grNF17GTODao.getGastosReales(compania, negocio, fchCierre, 0, ramo, baseTec, true, "N");
				}
			} else if (baseTec.equals(ConstantsModulos.CTE_VAL_SCRMFE)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRMMI)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRMCF)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRMCI)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRLFE)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRLMI) 
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRVM)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRINC)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRAEN)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRAEP)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRAIN)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRAIP)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRANM)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRTIU)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRTID)
					|| baseTec.equals(ConstantesSolvencia.BASE_NIIF17)
					|| baseTec.equals(ConstantesSolvencia.BASE_NIF17LIR)
					|| baseTec.equals(ConstantesSolvencia.BASE_NIFF17OCI)
					|| baseTec.equals(ConstantesSolvencia.BASE_N17CLIR)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_NF17AEN)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_NF17MFE)) {
				if (kcurva != null 
						&& kcurva.equals("CLR_MA0")) {
					gr = grDao.getGastosReales(compania, negocio, fchCierre, 0, ramo, ConstantsModulos.CTE_BT_BEL, true, "S");
				} else {
					gr = grDao.getGastosReales(compania, negocio, fchCierre, 0, ramo, ConstantsModulos.CTE_BT_BEL, true, "N");
				}
			} else if (baseTec.equals(ConstantesSolvencia.BASE_N17LIRIN)) {
				if (kcurva != null 
						&& kcurva.equals("CLR_MA0")) {
					gr = grDao.getGastosReales(compania, negocio, umic.getFechas().getFecinisus(), 0, ramo,
							ConstantsModulos.CTE_BT_BEL, true, "S");
				} else {
					gr = grDao.getGastosReales(compania, negocio, umic.getFechas().getFecinisus(), 0, ramo,
							ConstantsModulos.CTE_BT_BEL, true, "N");
				}
			} else if (baseTec.equals(ConstantesSolvencia.BASE_NIIF17IF)) {
				int mesesCierreN17 = 0;
				final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
				String varMesesCierreNIIF17 = (String) servicio.recuperarDefinicionAuxiliar(umic.getDatosGenerales().getCcartera(),
						umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), 
						ConstantsModulos.CTE_MESES_CIERRE_NIIF17);
				
				if (null == varMesesCierreNIIF17) {
					mesesCierreN17 = 0;
				} else {
					mesesCierreN17 = Integer.parseInt(varMesesCierreNIIF17);
				}

				Timestamp fecCierreAnterior = UtilFechas.decreMeses(fchCierre, mesesCierreN17);
				if (kcurva != null 
						&& kcurva.equals("CLR_MA0")) {
					gr = grDao.getGastosReales(compania, negocio, fecCierreAnterior, 0, ramo, ConstantsModulos.CTE_BT_BEL,
							true, "S");
				} else {
					gr = grDao.getGastosReales(compania, negocio, fecCierreAnterior, 0, ramo, ConstantsModulos.CTE_BT_BEL,
							true, "N");
				}
			} else {
				if (kcurva != null 
						&& kcurva.equals("CLR_MA0")) {
					gr = grDao.getGastosReales(compania, negocio, fchCierre, 0, ramo, baseTec, true, "S");
				} else {
					gr = grDao.getGastosReales(compania, negocio, fchCierre, 0, ramo, baseTec, true, "N");
				}
			}
			if (gr == null || gr.isEmpty()) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_GASTOSREALES_NO_RESULTS);
				exc.setTextoError(ConstantesErrores.CTE_DESC_GASTOSREALES_NO_RESULTS);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_GASTOSREALES_NO_RESULTS);
				throw exc;
			} else {
				if (gr.size() > 1) {
					exc.setCodigoRetorno(ConstantesErrores.CTE_COD_GASTOSREALES_VARIOS_REGISTROS);
					exc.setTextoError(ConstantesErrores.CTE_DESC_GASTOSREALES_VARIOS_REGISTROS);
					exc.setInfAmpliada(ConstantesErrores.CTE_DESL_GASTOSREALES_VARIOS_REGISTROS);
					throw exc;
				} else {
					elementGR = gr.get(0);
				}
			}
		} else {
			if (gr.size() > 1) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_GASTOSREALES_VARIOS_REGISTROS);
				exc.setTextoError(ConstantesErrores.CTE_DESC_GASTOSREALES_VARIOS_REGISTROS);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_GASTOSREALES_VARIOS_REGISTROS);
				throw exc;
			} else {
				elementGR = gr.get(0);
			}
		}

		return elementGR;
	}

	private void comprobarDatos(String baseTec, Integer compania, String negocio, String ramo, Integer modalidad,
			Timestamp fchCierre) {
		if (baseTec == null || baseTec.equals("")) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_BT);
			exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_BT);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_BT);
			throw exc;
		}
		if (compania == null || compania == 0) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_COMPANIA);
			exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_COMPANIA);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_COMPANIA);
			throw exc;
		}
		if (negocio == null || negocio.equals("")) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_NEG);
			exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_NEG);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_NEG);
			throw exc;
		}
		if (ramo == null || ramo.equals("")) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_RAMO);
			exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_RAMO);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_RAMO);
			throw exc;
		}
		if (modalidad == null) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_MOD);
			exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_MOD);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_MOD);
			throw exc;
		}
		if (fchCierre == null) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_FCIERRE);
			exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_FCIERRE);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_FCIERRE);
			throw exc;
		}

	}

	public List<AsigCurvaTipo> getCurvasTipo(DetalleBaseTecnica btcUmic, Umic umic) {
		List<AsigCurvaTipo> result = null;
		List<AsigCurvaTipo> curvaTipo = new ArrayList<AsigCurvaTipo>();
		AsigCurvaTipoDao dao = new AsigCurvaTipoDao();
		String pagoU = "N";

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
					|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_N17CLIR)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17AEN)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17GTO)
					|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17MFE)) {

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
											|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_N17CLIR)
											|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17AEN)
											|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17GTO)
											|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17MFE))))
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
	
	private void obtenerDatosSalida(final DetalleBaseTecnica detalleBT, GastosReales elementoGR) {
		detalleBT.setGtoUni(elementoGR.getGastoPorUmic());
		detalleBT.setGtoprov(elementoGR.getPctGastoProv());
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
