package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.FlujosProbablesKey;
import es.mapfre.solvencia.dao.impl.gbt.AsigTasasAnulDao;
import es.mapfre.solvencia.dao.impl.gbt.TasasAnulacionDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.FlujosProbablesDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.gbt.AsigTasasAnul;
import es.mapfre.solvencia.dominio.gbt.TasasAnulacion;
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

/**
 * Clase encargada de implementar el modulo encargado de establecer la base
 * técnica de cálculo necesaria para realizar las operaciones que obtienen los
 * flujos de las distintas corrientes. Como resultado obtendremos una estructura
 * de datos con el detalle de la base técnica de cálculo, btcUmic.
 * 
 * @author jguijarro
 */
public class J880GBT011TasasAnulacionBEL implements Modulo {
	/** Cte para log. */
	private static final Logger LOG = LoggerFactory.getLogger(J880GBT011TasasAnulacionBEL.class);
	GestorBasesTecnicasException exc = new GestorBasesTecnicasException();
	private FlujosProbablesDao flujosProbablesDao = new FlujosProbablesDao();
	public static J880GBT011TasasAnulacionBEL INSTANCE = null;
	private FlujosProbables fpClone;

	public static J880GBT011TasasAnulacionBEL getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new J880GBT011TasasAnulacionBEL();
		}
		return INSTANCE;
	}

	@Override
	public String getNombreServicio() {
		return ConstantesModulosGBT.BEL_TA;
	}

	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la
	 * llamada a la función que realiza los calculos del modulo.
	 */
	public final Object execute(final Object... args) {

		if (J880GBT011TasasAnulacionBEL.LOG.isTraceEnabled()) {
			J880GBT011TasasAnulacionBEL.LOG.trace("Inicio de execute en clase ModuloJ880JGBT13");
		}

		final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantesGBT.PARAM_BTC];
		final Umic umic = (Umic) args[ConstantesGBT.PARAM_UMI_BASE_TEC];
		setFpClone(null);
		try {
			exc.setGeneradorError(getNombreServicio());
			exc.setTipoError(ConstantesErrores.CTE_ERROR);

			// Calculo de base tecnica

			// Realizar la conversion de BTI a ROSSP o BEL
			TasasAnulacion elementoTasasAnul = recogerDatosEntrada(btcUmic, umic);
			obtenerDatosSalida(btcUmic, elementoTasasAnul);

		} catch (Exception e) {
			btcUmic.setTablaTanul(null);
			btcUmic.setFtablaAn(null);
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
				|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)) {

			key = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacion, ConstantesSolvencia.BASE_BEL);
		} else {
			key = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacion, bt);
		}

		FlujosProbables fp = flujosProbablesDao.get(key);
		if (null == fp) {
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
			// fall.setActualizado(fpBti.getFall().getActualizado());
			fall.setNoanulado(fpBti.getFall().getNoanulado());
			// fall.setNominal(fpBti.getFall().getNominal());
			// fall.setProbable(fpBti.getFall().getProbable());
		} else {
			// fall.setActualizado(ConstantsProcesos.CTE_LEIDO_BTI);
			fall.setNoanulado(ConstantsProcesos.CTE_ERROR_BT_FPNA);
			// fall.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
			// fall.setProbable(ConstantsProcesos.CTE_LEIDO_BTI);
		}
		fall.setProvi(fpBti.getFall().getProvi());
		// Vida
		BloqueFlujosProbables vida = fp.getVida();
		if (null == fpBti.getVida().getNominal()) {
			// vida.setActualizado(fpBti.getVida().getActualizado());
			vida.setNoanulado(fpBti.getVida().getNoanulado());
			// vida.setNominal(fpBti.getVida().getNominal());
			// vida.setProbable(fpBti.getVida().getProbable());
		} else {
			// vida.setActualizado(ConstantsProcesos.CTE_LEIDO_BTI);
			vida.setNoanulado(ConstantsProcesos.CTE_ERROR_BT_FPNA);
			// vida.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
			// vida.setProbable(ConstantsProcesos.CTE_LEIDO_BTI);
		}
		vida.setProvi(fpBti.getVida().getProvi());
		// Anulaci�n
		BloqueFlujosProbables anul = fp.getAnul();
		if (null == fpBti.getAnul().getNominal()) {
			// anul.setActualizado(fpBti.getAnul().getActualizado());
			anul.setNoanulado(fpBti.getAnul().getNoanulado());
			// anul.setNominal(fpBti.getAnul().getNominal());
			// anul.setProbable(fpBti.getAnul().getProbable());
		} else {
			// anul.setActualizado(ConstantsProcesos.CTE_LEIDO_BTI);
			anul.setNoanulado(ConstantsProcesos.CTE_ERROR_BT_FPNA);
			// anul.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
			// anul.setProbable(ConstantsProcesos.CTE_LEIDO_BTI);
		}
		anul.setProvi(fpBti.getAnul().getProvi());
		// Comisiones
		BloqueFlujosProbables comi = fp.getComi();
		if (null == fpBti.getComi().getNominal()) {
			// comi.setActualizado(fpBti.getComi().getActualizado());
			comi.setNoanulado(fpBti.getComi().getNoanulado());
			// comi.setNominal(fpBti.getComi().getNominal());
			// comi.setProbable(fpBti.getComi().getProbable());
		} else {
			// comi.setActualizado(ConstantsProcesos.CTE_LEIDO_BTI);
			comi.setNoanulado(ConstantsProcesos.CTE_ERROR_BT_FPNA);
			// comi.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
			// comi.setProbable(ConstantsProcesos.CTE_LEIDO_BTI);
		}
		comi.setProvi(fpBti.getComi().getProvi());
		// Gastos
		BloqueFlujosProbables gast = fp.getGast();
		if (null == fpBti.getGast().getNominal()) {
			// gast.setActualizado(fpBti.getGast().getActualizado());
			gast.setNoanulado(fpBti.getGast().getNoanulado());
			// gast.setNominal(fpBti.getGast().getNominal());
			// gast.setProbable(fpBti.getGast().getProbable());
		} else {
			// gast.setActualizado(ConstantsProcesos.CTE_LEIDO_BTI);
			gast.setNoanulado(ConstantsProcesos.CTE_ERROR_BT_FPNA);
			// gast.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
			// gast.setProbable(ConstantsProcesos.CTE_LEIDO_BTI);
		}
		gast.setProvi(fpBti.getGast().getProvi());
		// Invalidez
		BloqueFlujosProbables inv = fp.getInva();
		if (null == fpBti.getInva().getNominal()) {
			// inv.setActualizado(fpBti.getInva().getActualizado());
			inv.setNoanulado(fpBti.getInva().getNoanulado());
			// inv.setNominal(fpBti.getInva().getNominal());
			// inv.setProbable(fpBti.getInva().getProbable());
		} else {
			// inv.setActualizado(ConstantsProcesos.CTE_LEIDO_BTI);
			inv.setNoanulado(ConstantsProcesos.CTE_ERROR_BT_FPNA);
			// inv.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
			// inv.setProbable(ConstantsProcesos.CTE_LEIDO_BTI);
		}
		inv.setProvi(fpBti.getInva().getProvi());
		// Prima
		BloqueFlujosProbables prim = fp.getPrim();
		if (null == fpBti.getPrim().getNominal()) {
			// prim.setActualizado(fpBti.getPrim().getActualizado());
			prim.setNoanulado(fpBti.getPrim().getNoanulado());
			// prim.setNominal(fpBti.getPrim().getNominal());
			// prim.setProbable(fpBti.getPrim().getProbable());
		} else {
			// prim.setActualizado(ConstantsProcesos.CTE_LEIDO_BTI);
			prim.setNoanulado(ConstantsProcesos.CTE_ERROR_BT_FPNA);
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

	private TasasAnulacion recogerDatosEntrada(DetalleBaseTecnica btcUmic, final Umic umic) {

		List<AsigTasasAnul> ata;

		BaseTecnicaInicial baseTecIni = umic.getBti();
		final DatosGenerales datosGenerales = umic.getDatosGenerales();

		final String baseTec = btcUmic.getBaseTec();
		final Integer compania = datosGenerales.getCcanal();
		final String negocio = datosGenerales.getCnegocio();
		final String ramo = datosGenerales.getKramo();
		final Integer modalidad = datosGenerales.getKmodalidad();
		final Timestamp fchCierre = datosGenerales.getFecCierre();
		final Timestamp fini1 = baseTecIni.getFecIniTramo1();
		final Timestamp ffin1 = baseTecIni.getFecFinTramo1();

		if (fini1 == null) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_FINITRAMO_VACIO);
			exc.setTextoError(ConstantesErrores.CTE_DESC_FINITRAMO_VACIO);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_FINITRAMO_VACIO);
			throw exc;
		}
		if (ffin1 == null) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_FFINTRAMO_VACIO);
			exc.setTextoError(ConstantesErrores.CTE_DESC_FFINTRAMO_VACIO);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_FFINTRAMO_VACIO);
			throw exc;
		}

		comprobarDatosATA(baseTec, compania, negocio, ramo, modalidad, fchCierre);

		BigDecimal porcentaje = null;

		porcentaje = obtenerPorcentajes(fchCierre, baseTecIni);

		// Primera Consulta
		AsigTasasAnulDao ataDao = new AsigTasasAnulDao();
		if (baseTec.equals(ConstantsModulos.CTE_VAL_SCRMFE) || baseTec.equals(ConstantsModulos.CTE_VAL_SCRMMI)
				|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRLFE) || baseTec.equals(ConstantsModulos.CTE_VAL_SCRLMI)
				|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRMCF) || baseTec.equals(ConstantsModulos.CTE_VAL_SCRMCI)
				|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRVM) || baseTec.equals(ConstantsModulos.CTE_VAL_SCRTIU)
				|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRTID) || baseTec.equals(ConstantsModulos.CTE_VAL_SCRGTO)
				|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRINC) || baseTec.equals(ConstantsModulos.CTE_VAL_SCRAEP)
				|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRAEN) || baseTec.equals(ConstantsModulos.CTE_VAL_SCRAIP)
				|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRAIN) || baseTec.equals(ConstantsModulos.CTE_VAL_SCRANM)
				|| baseTec.equals(ConstantesSolvencia.BASE_NIIF17) || baseTec.equals(ConstantesSolvencia.BASE_NIF17LIR)
				|| baseTec.equals(ConstantesSolvencia.BASE_N17LIRIN)
				|| baseTec.equals(ConstantesSolvencia.BASE_NIFF17OCI)
				|| baseTec.equals(ConstantesSolvencia.BASE_NIIF17IF)
				|| baseTec.equals(ConstantesSolvencia.BASE_N17CLIR)) {

			ata = ataDao.obtenerTasasAnul(ConstantsModulos.CTE_BT_BEL, compania, negocio, ramo, porcentaje, modalidad,
					fchCierre);
		} else {
			ata = ataDao.obtenerTasasAnul(baseTec, compania, negocio, ramo, porcentaje, modalidad, fchCierre);
		}

		// List<AsigTasasAnul> ata =
		// ataDao.obtenerTasasAnulPrueba(baseTec,compania,negocio,ramo,0, fchCierre);
		AsigTasasAnul elementATA = null;

		if (ata == null || ata.isEmpty()) {
			if (baseTec.equals(ConstantsModulos.CTE_VAL_SCRMFE) || baseTec.equals(ConstantsModulos.CTE_VAL_SCRMMI)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRLFE)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRLMI)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRMCF)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRMCI) || baseTec.equals(ConstantsModulos.CTE_VAL_SCRVM)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRTIU)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRTID)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRGTO)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRINC)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRAEP)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRAEN)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRAIP)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRAIN)
					|| baseTec.equals(ConstantsModulos.CTE_VAL_SCRANM)
					|| baseTec.equals(ConstantesSolvencia.BASE_NIIF17)
					|| baseTec.equals(ConstantesSolvencia.BASE_NIF17LIR)
					|| baseTec.equals(ConstantesSolvencia.BASE_N17LIRIN)
					|| baseTec.equals(ConstantesSolvencia.BASE_NIFF17OCI)
					|| baseTec.equals(ConstantesSolvencia.BASE_NIIF17IF)
					|| baseTec.equals(ConstantesSolvencia.BASE_N17CLIR)) {

				ata = ataDao.obtenerTasasAnul(ConstantsModulos.CTE_BT_BEL, compania, negocio, ramo, porcentaje, 0,
						fchCierre);
			} else {
				ata = ataDao.obtenerTasasAnul(baseTec, compania, negocio, ramo, porcentaje, 0, fchCierre);
			}

			if (ata == null || ata.isEmpty()) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ASIGTASASANU_NO_RESULTS);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ASIGTASASANU_NO_RESULTS);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ASIGTASASANU_NO_RESULTS + " Ramo: " + ramo
						+ " Kpinteresdesde: " + porcentaje);
				throw exc;
			} else {
				if (ata.size() > 1) {
					exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ASIGTASASANU_VARIOS_REGISTROS);
					exc.setTextoError(ConstantesErrores.CTE_DESC_ASIGTASASANU_VARIOS_REGISTROS);
					exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ASIGTASASANU_VARIOS_REGISTROS + " Ramo: " + ramo
							+ " Kpinteresdesde: " + porcentaje);
					throw exc;
				} else {
					elementATA = ata.get(0);
				}
			}
		} else {
			if (ata.size() > 1) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ASIGTASASANU_VARIOS_REGISTROS);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ASIGTASASANU_VARIOS_REGISTROS);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ASIGTASASANU_VARIOS_REGISTROS);
				throw exc;
			} else {
				elementATA = ata.get(0);
			}
		}

		if (elementATA.getKtablaanu() == null || elementATA.getKtablaanu().equals("")) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ASIGTASASANU_NO_TASA_ANUL);
			exc.setTextoError(ConstantesErrores.CTE_DESC_ASIGTASASANU_NO_TASA_ANUL);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ASIGTASASANU_NO_TASA_ANUL);
			throw exc;
		}

		btcUmic.setTablaTanul(elementATA.getKtablaanu());

		// Segunda Consulta
		comprobarDatosTA(elementATA.getKtablaanu(), fchCierre);

		TasasAnulacionDao taDao = new TasasAnulacionDao();
		List<TasasAnulacion> ta = taDao.obtenerTasasAnul(elementATA.getKtablaanu(), fchCierre);
		TasasAnulacion elementTA = null;

		if (ta == null || ta.isEmpty()) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_TASASANU_NO_RESULTS);
			exc.setTextoError(ConstantesErrores.CTE_DESC_TASASANU_NO_RESULTS);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_TASASANU_NO_RESULTS + " Tabla: " + elementATA.getKtablaanu());
			throw exc;
		} else {
			/*
			 * NFQ Fase VIII Collections.sort(ta, Collections.reverseOrder(new
			 * Comparator<TasasAnulacion>(){
			 * 
			 * @Override public int compare(TasasAnulacion o1, TasasAnulacion o2) { return
			 * o1.getKfcierre().compareTo(o2.getKfcierre()); }
			 * 
			 * }));
			 */
			elementTA = ta.get(0);
		}
		return elementTA;
	}

	private BigDecimal obtenerPorcentajes(Timestamp fchCierre, BaseTecnicaInicial baseTecIni) {
		// TODO Auto-generated method stub
		Timestamp ffin1 = baseTecIni.getFecFinTramo1();
		Timestamp ffin2 = baseTecIni.getFecFinTramo2();
		Timestamp ffin3 = baseTecIni.getFecFinTramo3();
		Timestamp ffin4 = baseTecIni.getFecFinTramo4();
		Timestamp ffin5 = baseTecIni.getFecFinTramo5();

		Timestamp fini1 = baseTecIni.getFecIniTramo1();
		Timestamp fini2 = baseTecIni.getFecIniTramo2();
		Timestamp fini3 = baseTecIni.getFecIniTramo3();
		Timestamp fini4 = baseTecIni.getFecIniTramo4();
		Timestamp fini5 = baseTecIni.getFecIniTramo5();

		int tramos = 0;
		BigDecimal porcentaje = null;
		// Se tiene en cuenta la fecha inicio para validar si la fecha de cierre
		// pertenece al tramo
		if (fini1 != null && ffin1 != null) {
			if ((fchCierre.after(fini1) || fchCierre.equals(fini1)) && fchCierre.before(ffin1)) {
				tramos = tramos + 1;
				porcentaje = baseTecIni.getPintertecnI1();
			}
		}
		if (fini2 != null && ffin2 != null) {
			if ((fchCierre.after(fini2) || fchCierre.equals(fini2)) && fchCierre.before(ffin2)) {
				tramos = tramos + 1;
				porcentaje = baseTecIni.getPintertecnI2();
			}
		}
		if (fini3 != null && ffin3 != null) {
			if ((fchCierre.after(fini3) || fchCierre.equals(fini3)) && fchCierre.before(ffin3)) {
				tramos = tramos + 1;
				porcentaje = baseTecIni.getPintertecnI3();
			}
		}
		if (fini4 != null && ffin4 != null) {
			if ((fchCierre.after(fini4) || fchCierre.equals(fini4)) && fchCierre.before(ffin4)) {
				tramos = tramos + 1;
				porcentaje = baseTecIni.getPintertecnI4();
			}
		}
		if (fini5 != null && ffin5 != null) {
			if ((fchCierre.after(fini5) || fchCierre.equals(fini5)) && fchCierre.before(ffin5)) {
				tramos = tramos + 1;
				porcentaje = baseTecIni.getPintertecnI5();
			}
		}
		// Se tiene en cuenta la fecha inicio para validar si la fecha de cierre
		// pertenece al tramo
		if (tramos == 0) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_TRAMO_VACIO);
			exc.setTextoError(ConstantesErrores.CTE_DESC_TRAMO_VACIO);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_TRAMO_VACIO);
			throw exc;
		} else {
			if (tramos > 1) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_TRAMO_VARIOS);
				exc.setTextoError(ConstantesErrores.CTE_DESC_TRAMO_VARIOS);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_TRAMO_VARIOS);
				throw exc;
			} else {
				if (porcentaje == null || porcentaje.equals("")) {
					exc.setCodigoRetorno(ConstantesErrores.CTE_COD_INTECN_VACIO);
					exc.setTextoError(ConstantesErrores.CTE_DESC_INTECN_VACIO);
					exc.setInfAmpliada(ConstantesErrores.CTE_DESL_INTECN_VACIO);
					throw exc;
				}
			}
		}

		return porcentaje;
	}

	private void comprobarDatosATA(String baseTec, Integer compania, String negocio, String ramo, Integer modalidad,
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
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_RAMO2);
			exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_RAMO2);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_RAMO2);
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

	private void comprobarDatosTA(String ktablaanu, Timestamp fchCierre) {

		if (fchCierre == null) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_FCIERRE);
			exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_FCIERRE);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_FCIERRE);
			throw exc;
		}

		if (ktablaanu == null || ktablaanu.equals("")) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_TABLAANU_VACIA);
			exc.setTextoError(ConstantesErrores.CTE_DESC_TABLAANU_VACIA);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_TABLAANU_VACIA);
			throw exc;
		}
	}

	private void obtenerDatosSalida(final DetalleBaseTecnica detalleBT, TasasAnulacion elementTA) {
		detalleBT.setFtablaAn(elementTA.getKfcierre());
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
