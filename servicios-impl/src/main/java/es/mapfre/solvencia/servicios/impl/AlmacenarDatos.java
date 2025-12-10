/* MODIFICACION:TAR00302248-Contabilidad SolvenciaII en SSAA 
   FECHA: 25/09/2017
   AUTOR: INDRA
 */
/* MODIFICACION:TAR00400971-NECESIDADES NUEVO SISTEMA DE PROCESOS TÉCNICOS 
   FECHA: 17/12/2018 Se incluye campo KBENCON,SPCOM, y KMODEXT
   AUTOR: INDRA
*/
package es.mapfre.solvencia.servicios.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.MultiExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.processor.ExtractorProcessor;

import es.mapfre.solvencia.coherence.jmx.MonitorizacionConstants;
import es.mapfre.solvencia.coherence.keys.entregables.BasetecKey;
import es.mapfre.solvencia.coherence.keys.entregables.ContabilidadCertificadoKey;
/*INI-TAR00302248*/
import es.mapfre.solvencia.coherence.keys.entregables.ContabilidadKey;
import es.mapfre.solvencia.coherence.keys.entregables.ConteoCertificadoKey;
/*FIN-TAR00302248*/
import es.mapfre.solvencia.coherence.keys.entregables.DetalleCorrienteEntregablesKey;
import es.mapfre.solvencia.coherence.keys.entregables.FlujCoaSegKey;
import es.mapfre.solvencia.coherence.keys.entregables.FlujInf1Key;
import es.mapfre.solvencia.coherence.keys.entregables.FlujInf2Key;
import es.mapfre.solvencia.coherence.keys.entregables.FlujInf3Key;
import es.mapfre.solvencia.coherence.keys.entregables.FlujInf4Key;
import es.mapfre.solvencia.coherence.keys.entregables.FlujInfSCRKey;
import es.mapfre.solvencia.coherence.keys.entregables.FlujPMaCoaKey;
import es.mapfre.solvencia.coherence.keys.entregables.FlujPMaCoaMKey;
import es.mapfre.solvencia.coherence.keys.entregables.FlujPMdCoaKey;
import es.mapfre.solvencia.coherence.keys.entregables.FlujPMdCoaMKey;
import es.mapfre.solvencia.coherence.keys.entregables.FlujSuscriKey;
import es.mapfre.solvencia.coherence.keys.entregables.FlujTcasKey;
import es.mapfre.solvencia.coherence.keys.entregables.FlujoTotPVKey;
import es.mapfre.solvencia.coherence.keys.entregables.FlujosTN17Key;
import es.mapfre.solvencia.coherence.keys.entregables.PesosBtKey;
import es.mapfre.solvencia.coherence.keys.entregables.PesosBtProxyKey;
import es.mapfre.solvencia.coherence.keys.entregables.ProvCoaSegKey;
import es.mapfre.solvencia.coherence.keys.entregables.PrvBtKey;
import es.mapfre.solvencia.coherence.keys.entregables.PrvCrKey;
import es.mapfre.solvencia.coherence.keys.entregables.PrvFpbKey;
import es.mapfre.solvencia.coherence.keys.entregables.PrvInf1Key;
import es.mapfre.solvencia.coherence.keys.entregables.PrvInf2Key;
import es.mapfre.solvencia.coherence.keys.entregables.PrvUmicKey;
import es.mapfre.solvencia.coherence.keys.entregables.SwCobroComCsvKey;
import es.mapfre.solvencia.coherence.keys.formulacion.PeriodoKey;
import es.mapfre.solvencia.coherence.keys.maestro.DatosAdicionalesCoaseguroKey;
import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.DetalleCorrienteKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.FichaResultadoKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.TotalesFlujosKey;
import es.mapfre.solvencia.coherence.keys.scr.entregables.FactoresVolatilidadKey;
import es.mapfre.solvencia.coherence.keys.entregables.FlujosTotPKey;
import es.mapfre.solvencia.coherence.keys.entregables.PatronCsmKey;
import es.mapfre.solvencia.dao.impl.entregables.BasetecDao;
import es.mapfre.solvencia.dao.impl.entregables.ContabilidadCertificadoDao;
/*INI-TAR00302248*/
import es.mapfre.solvencia.dao.impl.entregables.ContabilidadDao;
import es.mapfre.solvencia.dao.impl.entregables.ConteoCertificadoDao;
/*FIN-TAR00302248*/
import es.mapfre.solvencia.dao.impl.entregables.DetalleCorrienteEntregablesDao;
import es.mapfre.solvencia.dao.impl.entregables.FlujCoaSegDao;
import es.mapfre.solvencia.dao.impl.entregables.FlujInf1Dao;
import es.mapfre.solvencia.dao.impl.entregables.FlujInf2Dao;
import es.mapfre.solvencia.dao.impl.entregables.FlujInf3Dao;
import es.mapfre.solvencia.dao.impl.entregables.FlujInf4Dao;
import es.mapfre.solvencia.dao.impl.entregables.FlujInfSCRDao;
import es.mapfre.solvencia.dao.impl.entregables.FlujPMaCoaDao;
import es.mapfre.solvencia.dao.impl.entregables.FlujPMaCoaMDao;
import es.mapfre.solvencia.dao.impl.entregables.FlujPMdCoaDao;
import es.mapfre.solvencia.dao.impl.entregables.FlujPMdCoaMDao;
import es.mapfre.solvencia.dao.impl.entregables.FlujSuscriDao;
import es.mapfre.solvencia.dao.impl.entregables.FlujTcasDao;
import es.mapfre.solvencia.dao.impl.entregables.FlujoTotPVDao;
import es.mapfre.solvencia.dao.impl.entregables.FlujosTN17Dao;
import es.mapfre.solvencia.dao.impl.entregables.PesosBtDao;
import es.mapfre.solvencia.dao.impl.entregables.PesosBtProxyDao;
import es.mapfre.solvencia.dao.impl.entregables.ProvCoaSegDao;
import es.mapfre.solvencia.dao.impl.entregables.PrvBtDao;
import es.mapfre.solvencia.dao.impl.entregables.PrvCrDao;
import es.mapfre.solvencia.dao.impl.entregables.PrvFpbDao;
import es.mapfre.solvencia.dao.impl.entregables.PrvInf1Dao;
import es.mapfre.solvencia.dao.impl.entregables.PrvInf2Dao;
import es.mapfre.solvencia.dao.impl.entregables.PrvUmicDao;
import es.mapfre.solvencia.dao.impl.entregables.SwCobroComCsvDao;
import es.mapfre.solvencia.dao.impl.formulacion.PeriodoDao;
import es.mapfre.solvencia.dao.impl.formulacion.PlanPagosDao;
import es.mapfre.solvencia.dao.impl.gbt.InteresTecnicoDao;
import es.mapfre.solvencia.dao.impl.maestro.DatosAdicionalesCoaseguroDao;
import es.mapfre.solvencia.dao.impl.maestro.DatosCoaseguroDao;
import es.mapfre.solvencia.dao.impl.maestro.DatosGeneralesDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.DetalleBaseTecnicaDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.DetalleCorrienteDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.FichaResultadoDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.IncidenciaDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.IncidenciasMaestroDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.TerminosPMCUmicDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.TotalesFlujosDao;
import es.mapfre.solvencia.dao.impl.scr.entregables.FactoresVolatilidadDao;
import es.mapfre.solvencia.dao.services.FactoriaDao;
import es.mapfre.solvencia.dao.impl.entregables.FlujosTotPDao;
import es.mapfre.solvencia.dao.impl.entregables.PatronCsmDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.entregables.Basetec;
/*INI-TAR00302248*/
import es.mapfre.solvencia.dominio.entregables.Contabilidad;
import es.mapfre.solvencia.dominio.entregables.ContabilidadCertificado;
import es.mapfre.solvencia.dominio.entregables.ConteoCertificado;
/*FIN-TAR00302248*/
import es.mapfre.solvencia.dominio.entregables.DetalleCorrienteEntregables;
import es.mapfre.solvencia.dominio.entregables.FlujCoaSeg;
import es.mapfre.solvencia.dominio.entregables.FlujInf1;
import es.mapfre.solvencia.dominio.entregables.FlujInf2;
import es.mapfre.solvencia.dominio.entregables.FlujInf3;
import es.mapfre.solvencia.dominio.entregables.FlujInf4;
import es.mapfre.solvencia.dominio.entregables.FlujInfSCR;
import es.mapfre.solvencia.dominio.entregables.FlujPMaCoa;
import es.mapfre.solvencia.dominio.entregables.FlujPMaCoaM;
import es.mapfre.solvencia.dominio.entregables.FlujPMdCoa;
import es.mapfre.solvencia.dominio.entregables.FlujPMdCoaM;
import es.mapfre.solvencia.dominio.entregables.FlujSuscri;
import es.mapfre.solvencia.dominio.entregables.FlujTcas;
import es.mapfre.solvencia.dominio.entregables.FlujoTotPV;
import es.mapfre.solvencia.dominio.entregables.FlujosTN17;
import es.mapfre.solvencia.dominio.entregables.PesosBt;
import es.mapfre.solvencia.dominio.entregables.PesosBtProxy;
import es.mapfre.solvencia.dominio.entregables.ProvCoaSeg;
import es.mapfre.solvencia.dominio.entregables.PrvBt;
import es.mapfre.solvencia.dominio.entregables.PrvCr;
import es.mapfre.solvencia.dominio.entregables.PrvFpb;
import es.mapfre.solvencia.dominio.entregables.PrvInf1;
import es.mapfre.solvencia.dominio.entregables.PrvInf2;
import es.mapfre.solvencia.dominio.entregables.PrvUmic;
import es.mapfre.solvencia.dominio.formulacion.Periodo;
import es.mapfre.solvencia.dominio.formulacion.PlanPagos;
import es.mapfre.solvencia.dominio.gbt.InteresTecnico;
import es.mapfre.solvencia.dominio.maestro.DatosAdicionalesCoaseguro;
import es.mapfre.solvencia.dominio.maestro.DatosCoaseguro;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.FichaResultado;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.dominio.salidaCalculo.IncidenciasMaestro;
import es.mapfre.solvencia.dominio.salidaCalculo.TerminosPMCUmic;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.dominio.scr.entregables.FactoresVolatilidad;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.dominio.entregables.FlujosTotP;
import es.mapfre.solvencia.dominio.entregables.PatronCsm;
import es.mapfre.solvencia.dominio.entregables.FPSL;
import es.mapfre.solvencia.dao.impl.entregables.FPSLDao;
import es.mapfre.solvencia.coherence.keys.entregables.FPSLKey;
import es.mapfre.solvencia.dominio.entregables.SwCobroCom;
import es.mapfre.solvencia.dominio.entregables.SwCobroComCsv;
import es.mapfre.solvencia.dominio.entregables.TotPMaCoa;
import es.mapfre.solvencia.dominio.entregables.TotPMaCoaM;
import es.mapfre.solvencia.dao.impl.entregables.SwCobroComDao;
import es.mapfre.solvencia.dao.impl.entregables.TotPMaCoaDao;
import es.mapfre.solvencia.dao.impl.entregables.TotPMaCoaMDao;
import es.mapfre.solvencia.coherence.keys.entregables.SwCobroComKey;
import es.mapfre.solvencia.coherence.keys.entregables.TotPMaCoaKey;
import es.mapfre.solvencia.coherence.keys.entregables.TotPMaCoaMKey;

public class AlmacenarDatos implements IAlmacenarDatos {

	private PlanPagosDao planPagosDao = new PlanPagosDao();
	private PeriodoDao periodoDao = new PeriodoDao();
	private DetalleCorrienteDao detallesDao = new DetalleCorrienteDao();
	private TotalesFlujosDao totalesFlujoDao = new TotalesFlujosDao();
	private IncidenciaDao incidenciaDao = new IncidenciaDao();
	private DetalleBaseTecnicaDao detalleBaseTecnicaDao = new DetalleBaseTecnicaDao();
	private FichaResultadoDao fichaResultadoDao = new FichaResultadoDao();
	private TerminosPMCUmicDao terminosPMCUmicDao = new TerminosPMCUmicDao();
	private PrvInf1Dao prvInf1Dao = new PrvInf1Dao();
	private PrvInf2Dao prvInf2Dao = new PrvInf2Dao();
	private PrvUmicDao prvUmicDao = new PrvUmicDao();
	private PrvCrDao prvCrDao = new PrvCrDao();
	private BasetecDao basetecDao = new BasetecDao();
	private PrvBtDao prvBtDao = new PrvBtDao();
	private FlujInf1Dao flujInf1Dao = new FlujInf1Dao();
	private FlujInf2Dao flujInf2Dao = new FlujInf2Dao();
	private ProvCoaSegDao provCoaSegDao = new ProvCoaSegDao();
	private FlujCoaSegDao flujCoaSegDao = new FlujCoaSegDao();
	private DetalleCorrienteEntregablesDao detalleCorrienteEntregablesDao = new DetalleCorrienteEntregablesDao();
	private PrvFpbDao prvFpbDao = new PrvFpbDao();
	private FlujTcasDao flujTcasDao = new FlujTcasDao();
	private DatosCoaseguroDao datosCoaseguroDao = new DatosCoaseguroDao();
	private DatosGeneralesDao datosGeneralesDao = new DatosGeneralesDao();
	/* INI-TAR00302248 */
	private ContabilidadDao contabilidadDao = new ContabilidadDao();
	/* FIN-TAR00302248 */
	private FactoresVolatilidadDao factoresVolatilidadDao = new FactoresVolatilidadDao();
	private FlujosTotPDao flujosTotPDao = new FlujosTotPDao();
	private DatosAdicionalesCoaseguroDao datosAdicionales = (DatosAdicionalesCoaseguroDao) FactoriaDao.getDao("X880JI03"); 
	private FlujInf3Dao flujInf3Dao = new FlujInf3Dao();
	private FlujInf4Dao flujInf4Dao = new FlujInf4Dao();
	private FlujoTotPVDao flujoTotPVDao = new FlujoTotPVDao();
	private FlujosTN17Dao flujosTotNiif17Dao = new FlujosTN17Dao();
	private FPSLDao FPSLDao = new FPSLDao();
	private PesosBtDao pesosBtDao = new PesosBtDao();
	private PesosBtProxyDao pesosBtProxyDao = new PesosBtProxyDao();
	private PatronCsmDao patronCsmDao = new PatronCsmDao();
	private SwCobroComDao swCobroComDao  = new SwCobroComDao();
	private SwCobroComCsvDao swCobroComCsvDao  = new SwCobroComCsvDao();
	private TotPMaCoaDao totPMaCoaDao  = new TotPMaCoaDao();
	private FlujPMaCoaDao flujPMaCoaDao  = new FlujPMaCoaDao();
	private FlujPMdCoaDao flujPMdCoaDao  = new FlujPMdCoaDao();
	private IncidenciasMaestroDao incidenciasMaestroDao = new IncidenciasMaestroDao();
	private ConteoCertificadoDao conteoCert = new ConteoCertificadoDao();
	private TotPMaCoaMDao totPMaCoaMDao  = new TotPMaCoaMDao();
	private FlujPMaCoaMDao flujPMaCoaMDao  = new FlujPMaCoaMDao();
	private FlujPMdCoaMDao flujPMdCoaMDao  = new FlujPMdCoaMDao();
	private FlujInfSCRDao flujInfSCRDao = new FlujInfSCRDao();
	private FlujSuscriDao flujSuscriDao = new FlujSuscriDao();
	private ContabilidadCertificadoDao contabilidadCertificadoDao = new ContabilidadCertificadoDao();
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.mapfre.solvencia.servicios.IAlmacenarDatos#almacenarPlanPagos(es.mapfre
	 * .solvencia.coherence.keys.maestro.UmicKey, java.util.List)
	 */
	@Override
	public void almacenarPlanPagos(UmicKey key, List<PlanPagos> listaPlanPagos) {
		planPagosDao.put(key, listaPlanPagos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.mapfre.solvencia.servicios.IAlmacenarDatos#almacenarPlanPagos(java
	 * .util.Map)
	 */
	@Override
	public void almacenarPlanPagos(Map<UmicKey, List<PlanPagos>> listasPlanPagos) {
		planPagosDao.putAll(listasPlanPagos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.mapfre.solvencia.servicios.IAlmacenarDatos#almacenarPeriodos(es.mapfre
	 * .solvencia.dominio.formulacion.Periodo)
	 */
	@Override
	public void almacenarPeriodos(Periodo periodo) {
		periodoDao.put(periodo.getKey(), periodo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.mapfre.solvencia.servicios.IAlmacenarDatos#almacenarPeriodos(java.
	 * util.List)
	 */
	@Override
	public void almacenarPeriodos(List<Periodo> periodos) {
		if (periodos != null) {
			Map<PeriodoKey, Periodo> mapa = new HashMap<PeriodoKey, Periodo>();
			for (Periodo periodo : periodos) {
				mapa.put(periodo.getKey(), periodo);
			}
			periodoDao.putAll(mapa);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.mapfre.solvencia.servicios.IAlmacenarDatos#almacenarProyeccion(es.
	 * mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente)
	 */
	@Override
	public void almacenarProyeccion(DetalleCorriente dC) {
		detallesDao.put(dC.getKey(), dC);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.mapfre.solvencia.servicios.IAlmacenarDatos#almacenarProyeccion(java
	 * .util.List)
	 */
	@Override
	public void almacenarProyeccion(List<DetalleCorriente> detallesCorriente) {
		if (detallesCorriente != null) {
			Map<DetalleCorrienteKey, DetalleCorriente> mapa = new HashMap<DetalleCorrienteKey, DetalleCorriente>();
			for (DetalleCorriente detalleCorriente : detallesCorriente) {
				mapa.put(detalleCorriente.getKey(), detalleCorriente);
			}
			detallesDao.putAll(mapa);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.mapfre.solvencia.servicios.IAlmacenarDatos#almacenarTotalFlujos(es
	 * .mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos)
	 */
	@Override
	public void almacenarTotalFlujos(TotalesFlujos totalesFlujos) {
		totalesFlujoDao.put(totalesFlujos.getKey(), totalesFlujos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.mapfre.solvencia.servicios.IAlmacenarDatos#almacenarTotalFlujos(java
	 * .util.List)
	 */
	@Override
	public void almacenarTotalFlujos(List<TotalesFlujos> totalesFlujos) {
		if (totalesFlujos != null) {
			Map<TotalesFlujosKey, TotalesFlujos> mapa = new HashMap<TotalesFlujosKey, TotalesFlujos>();
			for (TotalesFlujos totalFlujos : totalesFlujos) {
				mapa.put(totalFlujos.getKey(), totalFlujos);
			}
			totalesFlujoDao.putAll(mapa);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.mapfre.solvencia.servicios.IAlmacenarDatos#almacenarIncidencias(es
	 * .mapfre.solvencia.dominio.salidaCalculo.Incidencia)
	 */
	@Override
	public void almacenarIncidencias(Incidencia incidencia) {
		incidenciaDao.put(incidencia.getKey(), incidencia);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.mapfre.solvencia.servicios.IAlmacenarDatos#almacenarDetalleBaseTecnica
	 * (es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica)
	 */
	@Override
	public void almacenarDetalleBaseTecnica(DetalleBaseTecnica detalleBaseTecnica) {
		detalleBaseTecnicaDao.put(detalleBaseTecnica.getKey(), detalleBaseTecnica);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.mapfre.solvencia.servicios.IAlmacenarDatos#agregarLineaFinFichasResultado
	 * (java.util.Map)
	 */
	@Override
	public void agregarLineaFinFichasResultado(Map<java.util.Date, FichaProceso> fichasProceso) {
		DateFormat dfHH24mmss = new SimpleDateFormat(" HH:mm:ss");

		for (java.util.Date hora : fichasProceso.keySet()) {
			FichaProceso fichaProceso = fichasProceso.get(hora);
			FichaResultado fichaNueva = fichaResultadoDao.crearRegistroFichaResultado(
					new FichaResultadoKey(fichaProceso.getKejecucion(), fichaProceso.getKsistema(),
							fichaProceso.getKprotecnico(), fichaProceso.getKuejecucion(), fichaProceso.getKsecuencia()),
					ConstantesSolvencia.MENSAJE_FIN_PROCESO + dfHH24mmss.format(hora));
			fichaResultadoDao.put(fichaNueva);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.mapfre.solvencia.servicios.IAlmacenarDatos#agregarRegistroFichaResultado
	 * (es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso,
	 * java.lang.String)
	 */
	@Override
	public void agregarRegistroFichaResultado(FichaProceso fichaProceso, String tipoRegistro) {

		Integer nUmics = null;

		boolean subError = false;

		switch (tipoRegistro) {
		case ConstantesSolvencia.TEXTO_FICHA_RESULTADO_PROCESADAS:
			nUmics = calcularUmicsProcesadas();
			break;
		case ConstantesSolvencia.TEXTO_FICHA_RESULTADO_ERROR:
			nUmics = calcularUmicsError();
			break;
		case ConstantesSolvencia.TEXTO_FICHA_RESULTADO_AVISO:
			nUmics = calcularUmicsAviso();
			break;
		case ConstantesSolvencia.TEXTO_FICHA_RESULTADO_SUBERROR:
			subError = true;

			Map<String, Integer> mapaSubErrores = calcularSubErrores();

			if (mapaSubErrores != null) {

				for (String textoSubError : mapaSubErrores.keySet()) {
					nUmics = mapaSubErrores.get(textoSubError);
					textoSubError = tipoRegistro.concat(textoSubError)
							.concat(ConstantesSolvencia.TEXTO_FICHA_FINAL_REGISTRO_SUBERROR).concat(nUmics.toString());
					almacenarRegistroFichaResultado(fichaProceso, textoSubError);
				}

			}
			break;
		default:
			// Si no es uno de los anteriores, no hay que buscar nada.
			break;
		}

		if (!subError) {

			String textoRegistro = nUmics != null ? tipoRegistro.concat(nUmics.toString()) : tipoRegistro;

			almacenarRegistroFichaResultado(fichaProceso, textoRegistro);
		}
	}

	/**
	 * Almacena una lÃ­nea de log para la ficha de proceso
	 * 
	 * @param fichaProceso
	 * @param textoRegistro
	 */
	private void almacenarRegistroFichaResultado(FichaProceso fichaProceso, String textoRegistro) {

		FichaResultado ficha = fichaResultadoDao.crearRegistroFichaResultado(fichaProceso, textoRegistro);

		fichaResultadoDao.put(ficha);
	}

	private Map<String, Integer> calcularSubErrores() {

		return incidenciaDao.obtenerSubErrores();
	}

	private int calcularUmicsProcesadas() {
		return (Integer) CacheFactory.getCache("monitorizacionCache")
				.get(MonitorizacionConstants.UMIC_PROCESADAS_FICHA);
	}

	private int calcularUmicsError() {

		return incidenciaDao.calcularUmicsError();
	}

	private int calcularUmicsAviso() {

		return incidenciaDao.calcularUmicsAviso();
	}

	@Override
	public void almacenarTerminosPMCUmic(TerminosPMCUmic terminosPMCUmic) {
		terminosPMCUmicDao.put(terminosPMCUmic.getKey(), terminosPMCUmic);
	}

	@Override
	public void almacenarEntregablePrvInf1(List<PrvInf1> prvInf1s) {
		if (prvInf1s != null && !prvInf1s.isEmpty()) {
			Map<PrvInf1Key, PrvInf1> mapa = new HashMap<PrvInf1Key, PrvInf1>();
			for (PrvInf1 entry : prvInf1s) {
				mapa.put(entry.getKey(), entry);
			}
			prvInf1Dao.putAll(mapa);
		}
	}

	@Override
	public void almacenarEntregablePrvInf2(List<PrvInf2> prvInf2s) {
		if (prvInf2s != null && !prvInf2s.isEmpty()) {
			Map<PrvInf2Key, PrvInf2> mapa = new HashMap<PrvInf2Key, PrvInf2>();
			for (PrvInf2 entry : prvInf2s) {
				mapa.put(entry.getKey(), entry);
			}
			prvInf2Dao.putAll(mapa);
		}
	}

	@Override
	public void almacenarEntregablePrvUmic(List<PrvUmic> prvUmics) {
		if (prvUmics != null && !prvUmics.isEmpty()) {
			Map<PrvUmicKey, PrvUmic> mapa = new HashMap<PrvUmicKey, PrvUmic>();
			for (PrvUmic entry : prvUmics) {
				mapa.put(entry.getKey(), entry);
			}
			prvUmicDao.putAll(mapa);
		}
	}

	@Override
	public void almacenarEntregablePrvCr(List<PrvCr> prvCrs) {
		if (prvCrs != null && !prvCrs.isEmpty()) {
			Map<PrvCrKey, PrvCr> mapa = new HashMap<PrvCrKey, PrvCr>();
			for (PrvCr entry : prvCrs) {
				mapa.put(entry.getKey(), entry);
			}
			prvCrDao.putAll(mapa);
		}
	}

	@Override
	public void almacenarEntregablePrvBt(List<PrvBt> prvbts) {
		if (prvbts != null && !prvbts.isEmpty()) {
			Map<PrvBtKey, PrvBt> mapa = new HashMap<PrvBtKey, PrvBt>();
			for (PrvBt entry : prvbts) {
				mapa.put(entry.getKey(), entry);
			}
			prvBtDao.putAll(mapa);
		}
	}

	@Override
	public void almacenarEntregableFlujInf1(List<FlujInf1> flujinf1s) {
		if (flujinf1s != null && !flujinf1s.isEmpty()) {
			Map<FlujInf1Key, FlujInf1> mapa = new HashMap<FlujInf1Key, FlujInf1>();
			for (FlujInf1 entry : flujinf1s) {
				mapa.put(entry.getKey(), entry);
			}
			flujInf1Dao.putAll(mapa);
		}
	}

	@Override
	public void almacenarEntregableFlujInf2(List<FlujInf2> flujinf2s) {
		if (flujinf2s != null && !flujinf2s.isEmpty()) {
			Map<FlujInf2Key, FlujInf2> mapa = new HashMap<FlujInf2Key, FlujInf2>();
			for (FlujInf2 entry : flujinf2s) {
				mapa.put(entry.getKey(), entry);
			}
			flujInf2Dao.putAll(mapa);
		}
	}

	@Override
	public void almacenarEntregableProvCoaSeg(List<ProvCoaSeg> provcoaseguros) {
		if (provcoaseguros != null && !provcoaseguros.isEmpty()) {
			Map<ProvCoaSegKey, ProvCoaSeg> mapa = new HashMap<ProvCoaSegKey, ProvCoaSeg>();
			for (ProvCoaSeg entry : provcoaseguros) {
				mapa.put(entry.getKey(), entry);
			}
			provCoaSegDao.putAll(mapa);
		}
	}

	@Override
	public void almacenarEntregableFlujCoaSeg(List<FlujCoaSeg> flujcoaseguros) {
		if (flujcoaseguros != null && !flujcoaseguros.isEmpty()) {
			Map<FlujCoaSegKey, FlujCoaSeg> mapa = new HashMap<FlujCoaSegKey, FlujCoaSeg>();
			for (FlujCoaSeg entry : flujcoaseguros) {
				mapa.put(entry.getKey(), entry);
			}
			flujCoaSegDao.putAll(mapa);
		}
	}

	@Override
	public void almacenarEntregableBasetec(List<Basetec> basetecs) {
		if (basetecs != null && !basetecs.isEmpty()) {
			Map<BasetecKey, Basetec> mapa = new HashMap<BasetecKey, Basetec>();
			for (Basetec entry : basetecs) {
				mapa.put(entry.getKey(), entry);
			}
			basetecDao.putAll(mapa);
		}

	}
	
	public void eliminarDetalleCorrienteEntregables(DetalleCorrienteEntregables detalleEntregableBt) {
		detalleCorrienteEntregablesDao.remove(detalleEntregableBt.getKey());
	}

	@Override
	public void almacenarDetalleCorrienteEntregables(Umic umic, DetalleBaseTecnica detalleBaseTecnica,
			List<DetalleCorriente> detallesCorriente, FichaProceso fichaproceso, Boolean scr) {

		if (detallesCorriente != null && !detallesCorriente.isEmpty()) {
			Map<DetalleCorrienteEntregablesKey, DetalleCorrienteEntregables> detallecorrienteEntregables = new HashMap<DetalleCorrienteEntregablesKey, DetalleCorrienteEntregables>();

			UmicKey umickey = new UmicKey(detallesCorriente.get(0).getCtipoaport(),
					detallesCorriente.get(0).getKajuste(), detallesCorriente.get(0).getKcertificado(),
					detallesCorriente.get(0).getKgarantia(), detallesCorriente.get(0).getKmodalidad(),
					detallesCorriente.get(0).getKpoliza(), detallesCorriente.get(0).getKprestacion(),
					detallesCorriente.get(0).getKsubpoliza(), detallesCorriente.get(0).getNorden(),
					detallesCorriente.get(0).getNsuscri());

			DatosCoaseguro datosCoaseguro = (DatosCoaseguro) datosCoaseguroDao.get(umickey);
			DatosGenerales datosGenerales = datosGeneralesDao.get(umickey);
			final MathContext MATH_CONTEXT = new MathContext(34, RoundingMode.HALF_DOWN);
			int iteracion = 0;
			for (DetalleCorriente detalle : detallesCorriente) {
				
				DetalleCorrienteEntregables entry = new DetalleCorrienteEntregables();

				// Fecha desde a primer día del mes
				Timestamp fdesde = detalle.getFechaDesde();
				LocalDateTime dt = new LocalDateTime(fdesde.getTime());
				int dia = dt.getDayOfMonth();
				entry.setDia(dia);
				fdesde = new Timestamp(dt.minusDays(dia - 1).toDateTime().getMillis());
				//&& detalle.getBt().equalsIgnoreCase("BEL") && (detalleBaseTecnica.getBaseTec().equalsIgnoreCase("SCRMFE") || detalleBaseTecnica.getBaseTec().equalsIgnoreCase("SCRLFE"))
				if(scr){
					entry.setBt(detalleBaseTecnica.getBaseTec() + "BEL");
				}else{
					entry.setBt(detalle.getBt());
				}
				//entry.setBt(detalle.getBt());
				entry.setFcierre(detalle.getFcierre());
				entry.setUmicKey(umickey);
				entry.setFechadesde(fdesde);

				entry.setPrestcal(umic.getDatosAdicionales().getPrestCal());
				entry.setNuevaproduc(umic.getDatosGenerales().getNuevaProduc());
				entry.setSegmento1(umic.getDatosGenerales().getSegmento1());
				entry.setTiposubriesgo(umic.getDatosGenerales().getTipoSubriesgo());

				entry.setGestionit(umic.getDatosGenerales().getGestionit());

				entry.setCurvati(detalleBaseTecnica.getCurvaTi());

				entry.setCnegocio(detalle.getCnegocio());
				entry.setCcanal(detalle.getCcanal());
				entry.setKmodalidad(detalle.getKmodalidad());
				entry.setKgarantia(detalle.getKgarantia());
				entry.setKpoliza(detalle.getKpoliza());
				entry.setKsubpoliza(detalle.getKsubpoliza());
				entry.setNsuscri(detalle.getNsuscri());
				entry.setKcarterainv(detalle.getKcarterainv());
				entry.setGapact(detalle.getGapAct());
				entry.setKramo(detalle.getKramo());
				entry.setKprestacion(detalle.getKprestacion());
				entry.setKcertificado(detalle.getKcertificado());
//INI-TAR00400971				
				entry.setSpcom(umic.getDatosGenerales().getSpcom());
				entry.setKmodext(umic.getDatosGenerales().getKmodext());
				entry.setKbencon(umic.getDatosGenerales().getKbencon());
//FIN-TAR00400971				
			
				if (detalle.getTotalFlujoProyeccion() != null) {
					entry.setSumfprob(detalle.getTotalFlujoProyeccion().getSumfprob());
					entry.setSumfprobtanul(detalle.getTotalFlujoProyeccion().getSumfprobtanul());
					entry.setSumprovision(detalle.getTotalFlujoProyeccion().getSumprovision());
					entry.setSumcola(detalle.getTotalFlujoProyeccion().getSumcola());
				}

				BloqueCorriente bloqueComi = detalle.getBloqueComi();
				BloqueCorriente bloqueCompl = detalle.getBloqueCompl();
				BloqueCorriente bloqueFall = detalle.getBloqueFall();
				BloqueCorriente bloqueGto = detalle.getBloqueGto();
				BloqueCorriente bloquePrim = detalle.getBloquePrim();
				BloqueCorriente bloqueRte = detalle.getBloqueRte();
				BloqueCorriente bloqueVida = detalle.getBloqueVida();
				BloqueCorriente bloqueGtoAd = detalle.getBloqueGtoAd();

				if (bloqueComi != null) {
					entry.setBloquecomiImpflujonominal(bloqueComi.getImpFlujoNominal());
					entry.setBloquecomiImpflujoprobable(bloqueComi.getImpFlujoProbable());
					entry.setBloquecomiImpflujoactualizado(bloqueComi.getImpFlujoActualizado());
					entry.setBloqueComiImpflujoNoAnulado(bloqueComi.getImpFlujoNoAnulado());
					entry.setBloquecomiImpprovi(bloqueComi.getImpProvi());
				}

				if (bloqueCompl != null) {
					entry.setBloquecomplImpflujonominal(bloqueCompl.getImpFlujoNominal());
					entry.setBloquecomplImpflujoprobable(bloqueCompl.getImpFlujoProbable());
					entry.setBloquecomplImpflujoactualizado(bloqueCompl.getImpFlujoActualizado());
					entry.setBloqueComplImpflujoNoAnulado(bloqueCompl.getImpFlujoNoAnulado());
					entry.setBloquecomplImpprovi(bloqueCompl.getImpProvi());
				}

				if (bloqueFall != null) {
					entry.setBloquefallImpflujonominal(bloqueFall.getImpFlujoNominal());
					entry.setBloquefallImpflujoprobable(bloqueFall.getImpFlujoProbable());
					entry.setBloquefallImpflujoactualizado(bloqueFall.getImpFlujoActualizado());
					entry.setBloqueFallImpflujoNoAnulado(bloqueFall.getImpFlujoNoAnulado());
					entry.setBloquefallImpprovi(bloqueFall.getImpProvi());
				}

				if (bloqueGto != null) {
					entry.setBloquegtoImpflujonominal(bloqueGto.getImpFlujoNominal());
					entry.setBloquegtoImpflujoprobable(bloqueGto.getImpFlujoProbable());
					entry.setBloquegtoImpflujoactualizado(bloqueGto.getImpFlujoActualizado());
					entry.setBloqueGtoImpflujoNoAnulado(bloqueGto.getImpFlujoNoAnulado());
					entry.setBloquegtoImpprovi(bloqueGto.getImpProvi());
				}

				if (bloquePrim != null) {
					entry.setBloqueprimImpflujonominal(bloquePrim.getImpFlujoNominal());
					entry.setBloqueprimImpflujoprobable(bloquePrim.getImpFlujoProbable());
					entry.setBloqueprimImpflujoactualizado(bloquePrim.getImpFlujoActualizado());
					entry.setBloquePrimImpflujoNoAnulado(bloquePrim.getImpFlujoNoAnulado());
					entry.setBloqueprimImpprovi(bloquePrim.getImpProvi());
				}
				if (bloqueRte != null) {
					entry.setBloquerteImpflujonominal(bloqueRte.getImpFlujoNominal());
					entry.setBloquerteImpflujoprobable(bloqueRte.getImpFlujoProbable());
					entry.setBloquerteImpflujoactualizado(bloqueRte.getImpFlujoActualizado());
					entry.setBloqueRteImpflujoaNoAnulado(bloqueRte.getImpFlujoNoAnulado());
					entry.setBloquerteImpprovi(bloqueRte.getImpProvi());
				}

				if (bloqueVida != null) {
					entry.setBloquevidaImpflujonominal(bloqueVida.getImpFlujoNominal());
					entry.setBloquevidaImpflujoprobable(bloqueVida.getImpFlujoProbable());
					entry.setBloquevidaImpflujoactualizado(bloqueVida.getImpFlujoActualizado());
					entry.setBloqueVidaImpflujoNoAnulado(bloqueVida.getImpFlujoNoAnulado());
					entry.setBloquevidaImpprovi(bloqueVida.getImpProvi());
				}
				
				if (bloqueGtoAd != null) {
					entry.setBloquegtoadImpflujonominal(bloqueGtoAd.getImpFlujoNominal());
					entry.setBloquegtoadImpflujoprobable(bloqueGtoAd.getImpFlujoProbable());
					entry.setBloquegtoadImpflujoactualizado(bloqueGtoAd.getImpFlujoActualizado());
					entry.setBloqueGtoadImpflujoNoAnulado(bloqueGtoAd.getImpFlujoNoAnulado());
					entry.setBloquegtoadImpprovi(bloqueGtoAd.getImpProvi());
				}		
				
				// Campos necesarios para el filtrado en FLUJCOASEG y FLUJCAS
				entry.setKcoaseOri(datosCoaseguro.getKcoaseOri());
				entry.setSwcasado(datosGenerales.getSwcasado());
				
				//campos para optimización entregables FaseVIII
				//Fechas
				entry.setFecinisus(umic.getFechas().getFecinisus());
				//Coaseguro
				entry.setKcoaseOri(umic.getDatosCoaseguro().getKcoaseOri());
				//BaseTecnicaInicial
				entry.setPgastgesin1I(umic.getBti().getPgastgesin1I());
				entry.setPgastgesin2I(umic.getBti().getPgastgesin2I());
				entry.setFecFinTramo1(umic.getBti().getFecFinTramo1());
				entry.setPintertecnI1(umic.getBti().getPintertecnI1());
				entry.setPintertecnI2(umic.getBti().getPintertecnI2());
				entry.setTabla1Aseg1(umic.getBti().getTabla1Aseg1());
				//DetalleBaseTecnica
				entry.setGtoUni(detalleBaseTecnica.getGtoUni());
				entry.setGtoprov(detalleBaseTecnica.getGtoprov());
				entry.setFactor1(detalleBaseTecnica.getFactor1());
				entry.setFecinitramo(detalleBaseTecnica.getFecInitramo());
				entry.setFecfintramo(detalleBaseTecnica.getFecfintramo());
				entry.setSwcasadolst(detalleBaseTecnica.getSwcasado());
				//DatosAdicionalesCoaseguro
				DatosAdicionalesCoaseguro datosAdicionalesCoaseguro = datosAdicionales.get(new DatosAdicionalesCoaseguroKey(umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza()));
				if(datosAdicionalesCoaseguro != null) {
					entry.setKcoase1(datosAdicionalesCoaseguro.getKcoase1());
					entry.setKcoase2(datosAdicionalesCoaseguro.getKcoase2());
					entry.setKcoase3(datosAdicionalesCoaseguro.getKcoase3());
					entry.setKcoase4(datosAdicionalesCoaseguro.getKcoase4());
					entry.setKcoase5(datosAdicionalesCoaseguro.getKcoase5());
					entry.setKcoase6(datosAdicionalesCoaseguro.getKcoase6());
					entry.setKcoase7(datosAdicionalesCoaseguro.getKcoase7());
					entry.setKcoase8(datosAdicionalesCoaseguro.getKcoase8());
					entry.setKcoase9(datosAdicionalesCoaseguro.getKcoase9());
					entry.setKcoase10(datosAdicionalesCoaseguro.getKcoase10());
					entry.setKcoase11(datosAdicionalesCoaseguro.getKcoase11());
					entry.setKcoase12(datosAdicionalesCoaseguro.getKcoase12());
					entry.setKcoase13(datosAdicionalesCoaseguro.getKcoase13());
					entry.setKcoase14(datosAdicionalesCoaseguro.getKcoase14());
					entry.setKcoase15(datosAdicionalesCoaseguro.getKcoase15());
					entry.setKcoase16(datosAdicionalesCoaseguro.getKcoase16());
					entry.setKcoase17(datosAdicionalesCoaseguro.getKcoase17());
					entry.setKcoase18(datosAdicionalesCoaseguro.getKcoase18());
					entry.setKcoase19(datosAdicionalesCoaseguro.getKcoase19());
					entry.setKcoase20(datosAdicionalesCoaseguro.getKcoase20());
					entry.setKcuadro(datosAdicionalesCoaseguro.getKcuadro());
				}
				
				//FLUJINF3
				Timestamp fhasta = detalle.getFechaHasta();
				entry.setFhasta(fhasta);
				entry.setUoa(umic.getDatosNiif17().getuoa());
				entry.setKcarteraContrato(umic.getDatosNiif17().getkcarcontacto());
				entry.setKcarteraCohort(umic.getDatosNiif17().getcohorte());
				entry.setKcarteraOner(umic.getDatosNiif17().getswmodoner());
				
				//PatronCSM
				entry.setRaUmic(detalle.getRaumic());
				entry.setProvBelNiif17(detalle.getProvBelCSM());
				entry.setProvRosspCsm(detalle.getProvRosspCSM());
				entry.setRosspCsm(detalle.getRosspCSM());

				
				//FPSL
				if((fichaproceso.getCtipobt().equals("MULTI4NB") 
						|| fichaproceso.getCtipobt().equals("MULTI2") 
						|| fichaproceso.getCtipobt().equals("MULTI4C") 
						|| fichaproceso.getCtipobt().equals("MULTI5") 
						|| fichaproceso.getCtipobt().equals("NIIF17IF") 
						|| fichaproceso.getCtipobt().equals("MULTI6") 
						|| fichaproceso.getCtipobt().equals("MULTI8") 
						|| fichaproceso.getCtipobt().equals("MULTI8NB")
						|| fichaproceso.getCtipobt().equals("MULTISN")) 
						&& !detalleBaseTecnica.getBt().equals("BTI") 
						&& !detalleBaseTecnica.getBt().equals("ROSSP") 
						&& !detalleBaseTecnica.getBt().equals("ROSSPCSM")  
						&& !detalleBaseTecnica.getBt().equals("ROSSPTE") 
						&& !detalleBaseTecnica.getBt().equals("ROSSPTI") 
						&& !detalleBaseTecnica.getBt().equals("ROSSPGA") 
						&& !detalleBaseTecnica.getBt().equals("BTIPROY") ){
					
					if(fichaproceso.getCtipobt().equals("MULTI8NB") 
							&& detalle.getOga() != null
							&& detalle.getOga()
							&& detallesCorriente.get(0).getBloqueGtoAd() != null
							&& iteracion == 0) {
						if (detallesCorriente.get(0).getBloqueGtoAd().getFechaDevengo() == null) {
							entry.setOga(detallesCorriente.get(1).getBloqueGtoAd().getImpFlujoNominal());
						} else {
							entry.setOga(detallesCorriente.get(0).getBloqueGtoAd().getImpFlujoNominal());
						}
					} else {
						entry.setOga(BigDecimal.ZERO);
					}
					
					entry.setBloquegtoadImpflujonominal(BigDecimal.ZERO);
					entry.setBloquegtoadImpflujoprobable(BigDecimal.ZERO);
					entry.setBloquegtoadImpflujoactualizado(BigDecimal.ZERO);
					entry.setBloqueGtoadImpflujoNoAnulado(BigDecimal.ZERO);
					entry.setBloquegtoadImpprovi(BigDecimal.ZERO);
					
					if(fichaproceso.getCtipobt().equals("MULTI8NB")
							&& detalle.getOga() != null
							&& detalle.getOga()) {
						entry.setSioga(BigDecimal.ONE);
					} else {
						entry.setSioga(BigDecimal.ZERO);
					}
					
					if(fichaproceso.getCtipobt().equals("MULTI8NB")) {
						entry.setSiprima(BigDecimal.ONE);
					} else {
						entry.setSiprima(BigDecimal.ZERO);
					}
					
					//CALCULO PB
					//(TOTFPNAVIDA + TOTFPNAFALL + TOTFPNAGTOADQ** + TOTFPNAPRIM + TOTFPNAGTO + TOTFPNACOMI) * PROXYPB.
					//Recueprar dato especifico o variable auxiliar (PROXYPB)
					if(null != umic.getDatosGenerales().getPb() && umic.getDatosGenerales().getPb().equals("S")){
						//BigDecimal proxyPB = new BigDecimal(1).divide(new BigDecimal(2));
						BigDecimal proxyPB = BigDecimal.ZERO;
						entry.setPb((bloqueVida.getImpFlujoNoAnulado().add(bloqueRte.getImpFlujoNoAnulado()).add(bloqueFall.getImpFlujoNoAnulado()).add(bloquePrim.getImpFlujoNoAnulado()).add(bloqueGto.getImpFlujoNoAnulado()).add(bloqueComi.getImpFlujoNoAnulado()).add(bloqueGtoAd.getImpFlujoNoAnulado())).multiply(proxyPB));
					}else{
						entry.setPb(BigDecimal.ZERO);
					}
					
					if(iteracion == 0 && null!=umic.getPrimas().getCformpago() 
					&& umic.getPrimas().getCformpago().equals("9") 
					&& detalleBaseTecnica.getBt().equals("N17LIRIN") ){
						if(null != umic.getPrimas()){
							if(null == umic.getPrimas().getIprimanetaini()){
								entry.setBloqueprimImpflujonominal(BigDecimal.ZERO);
								entry.setBloqueprimImpflujoprobable(BigDecimal.ZERO);
								entry.setBloqueprimImpflujoactualizado(BigDecimal.ZERO);
								entry.setBloquePrimImpflujoNoAnulado(BigDecimal.ZERO);
							}else{
								entry.setBloqueprimImpflujonominal(umic.getPrimas().getIprimanetaini());
								entry.setBloqueprimImpflujoprobable(umic.getPrimas().getIprimanetaini());
								entry.setBloqueprimImpflujoactualizado(umic.getPrimas().getIprimanetaini());
								entry.setBloquePrimImpflujoNoAnulado(umic.getPrimas().getIprimanetaini());
							}
						}
					}
					
					if(iteracion == 0 && null != umic.getPrimas().getCformpago() 
							&& umic.getPrimas().getCformpago().equals("9") 
							&& (detalleBaseTecnica.getBt().equals("NIIF17LIR")
								|| detalleBaseTecnica.getBt().equals("NIIF17"))){
						entry.setBloqueprimImpflujonominal(BigDecimal.ZERO);
						entry.setBloqueprimImpflujoprobable(BigDecimal.ZERO);
						entry.setBloqueprimImpflujoactualizado(BigDecimal.ZERO);
						entry.setBloquePrimImpflujoNoAnulado(BigDecimal.ZERO);
					}
					
					if(null != umic.getPrimas().getCformpago() 
							&& fichaproceso.getCtipobt().equals("MULTI8NB")
							&& iteracion == 0){
						if (!umic.getPrimas().getCformpago().equals("9") ) {
							int fracc = 1;
							switch (umic.getPrimas().getCformpago()) {
								case "1":
									fracc=1;
									break;
								case "2":
									fracc=2;
									break;
								case "3":
									fracc=4;
									break;
								case "4":
									fracc=12;
									break;
							}
							
							if(null != umic.getPrimas()){
								if(null == umic.getPrimas().getIprimatarada()){
									entry.setPrimaperiodica(BigDecimal.ZERO);
								}else{
									BigDecimal varPrim = umic.getPrimas().getIprimatarada().divide(BigDecimal.valueOf(fracc),MATH_CONTEXT);
									entry.setPrimaperiodica(varPrim.setScale(10, RoundingMode.HALF_DOWN));
								}
							}
						} else {
							if(null != umic.getPrimas()){
								if(null == umic.getPrimas().getIprimatarada()){
									entry.setPrimaperiodica(BigDecimal.ZERO);
								}else{
									BigDecimal varPrim = umic.getPrimas().getIprimatarada()
											.multiply(umic.getDatosCoaseguro().getPcoaseg().divide(BigDecimal.valueOf(100), MATH_CONTEXT));
									entry.setPrimaperiodica(varPrim.setScale(10, RoundingMode.HALF_DOWN));
								}
							}
						}
						
					} else {
						entry.setPrimaperiodica(BigDecimal.ZERO);
					}
					
					if(null != umic.getPrimas()
							&& null != umic.getPrimas().getCformpago() 
							&& fichaproceso.getCtipobt().equals("MULTI8NB")
							&& iteracion == 0){
						entry.setComi(entry.getPrimaperiodica().multiply(
								umic.getComisiones().getPcomisiona1().divide(BigDecimal.valueOf(100), MATH_CONTEXT)));
					} else {
						entry.setComi(BigDecimal.ZERO);
					}
		
					iteracion++;
					
					String onerosidad;
					Integer CTE_0 = 0;
					if(null != umic.getDatosNiif17()){
						
					
					if(umic.getDatosNiif17().getswmodoner() != null && umic.getDatosNiif17().getswmodoner().equals("S")){
						onerosidad = "1";
					}else{
						onerosidad = "0";
					}
					//entry.setKuoa("UOA" + "_" + "0144" + umic.getDatosNiif17().getkcarinv17() + "_" + umic.getDatosNiif17().getcohorte() + "_" + onerosidad + "_" + umic.getDatosNiif17().getmetmedicion());
					if(null != umic.getDatosNiif17().getuoa()){
						entry.setKuoa(umic.getDatosNiif17().getuoa());
					}
					
					//Generar fecha punto de venta
					int dia_pv = 1;
					int dia_fproyflujest = 1;
					
					//Sacamos el mes de la fecha de inicio de la suscripción
//					Timestamp fIniSus_fpsl = umic.getFechas().getFecinisus();
//					LocalDateTime dt_fIniSus = new LocalDateTime(fIniSus_fpsl.getTime());
//					int mes_fIniSus = dt_fIniSus.getMonthOfYear();
//					
//					if(umic.getDatosNiif17().getPventa() != null && umic.getDatosNiif17().getPventa().equals("I")){
//						dia_pv = 1;
//					}else if(umic.getDatosNiif17().getPventa()!= null &&umic.getDatosNiif17().getPventa().equals("M")){
//						dia_pv=15;
//						if(mes_fIniSus == 2){
//							dia_pv=14;
//						}
//					}else if(umic.getDatosNiif17().getPventa() != null && umic.getDatosNiif17().getPventa().equals("F")){
//						if(mes_fIniSus == 2){
//							dia_pv=28;
//						}else if(mes_fIniSus == 1 || 
//								mes_fIniSus == 3 ||
//								mes_fIniSus == 5 ||
//								mes_fIniSus == 7 ||
//								mes_fIniSus == 8 ||
//								mes_fIniSus == 10 ||
//								mes_fIniSus == 12){
//							dia_pv=31;
//						}else{
//							dia_pv=30;
//						}
//					}else{
//						//Saca error de que no viene ifnormado el campo correctamente o incidecnia infomrativa mejor.
//						dia_pv=1;
//					}
					
//					//String anyoPv = String.valueOf(Integer.parseInt(umic.getDatosNiif17().getcohorte()));
//					
//					String mespV = String.valueOf(mes_fIniSus);
//					if(mespV.length() == 1){
//						mespV = "0" + mespV;
//					}
//					String diaPv = String.valueOf(dia_pv);
//					if(diaPv.length() == 1){
//						diaPv = "0" + diaPv;
//					}
//					
//					String fecha = anyoPv + "/" + mespV + "/" + diaPv;
							
					if(fichaproceso.getCtipobt().equals("MULTI4NB") || fichaproceso.getCtipobt().equals("MULTI8NB")){
						
						if(detalleBaseTecnica.getBt().equals("NIIF17") || detalleBaseTecnica.getBt().equals("NIIF17LIR")){
							entry.setTextraccion("01_00");
							entry.setTnegociolrc(1);
						}else if(detalleBaseTecnica.getBt().equals("NIIF17OCI")){
							entry.setTextraccion("03_00");
							entry.setTnegociolrc(0);
						}
						
//						Timestamp f = null;
//						SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy/MM/dd");
//						try {
//							Date fechaParseada = formatoFecha.parse(fecha);
//							 f = new Timestamp(fechaParseada.getTime());
//							//entry.setPventa(f);
//	
//							//Fdiferimiento = new Timestamp(fechaParseada.getTime());
//	
//						} catch (ParseException e) {
//							//Si el formato no es el esperado se lanza excepción.
//							throw Solvencia2ExcepcionHelper.crearExcepcion("AP", new String[]{fecha, "yyyy/MM/dd"});
//						}
					}else if(fichaproceso.getCtipobt().equals("MULTI4C") || fichaproceso.getCtipobt().equals("MULTI8") || fichaproceso.getCtipobt().equals("MULTISN")){
						if(detalleBaseTecnica.getBt().equals("NIIF17") 
								|| detalleBaseTecnica.getBt().equals("NIIF17LIR") 
								|| detalleBaseTecnica.getBt().equals("N17CLIR")
								|| detalleBaseTecnica.getBt().equals("NF17MFE")
								|| detalleBaseTecnica.getBt().equals("NF17GTO")
								|| detalleBaseTecnica.getBt().equals("NF17AEN")){
							entry.setTextraccion("02_00");
						}else if(detalleBaseTecnica.getBt().equals("NIIF17OCI")){
							entry.setTextraccion("03_00");
						}
						entry.setTnegociolrc(0);
					}else if (fichaproceso.getCtipobt().equals("MULTI5")) {
						if(detalleBaseTecnica.getBt().equals("NIIF17") || detalleBaseTecnica.getBt().equals("NIIF17LIR") || detalleBaseTecnica.getBt().equals("N17CLIR")){
							entry.setTextraccion("02_00");
						}else if(detalleBaseTecnica.getBt().equals("NIIF17OCI")){
							entry.setTextraccion("03_00");
						}else if (detalleBaseTecnica.getBt().equals("N17LIRIN")){
							entry.setTextraccion("01_00");
						}
						entry.setTnegociolrc(1);
						Timestamp f = null;
						
						int decreDias = 1;
						final LocalDateTime dtPventa = new LocalDateTime(fichaproceso.getFefecto().getTime());
						Timestamp fVenta = new Timestamp(dtPventa.minusDays(decreDias).toDateTime().getMillis());
						LocalDateTime dateVenta = new LocalDateTime(fVenta.getTime());
						int mesVenta = dateVenta.getMonthOfYear();
						int	diaVenta=15;
						int anioVenta = dateVenta.getYear();
						
						
						
						String anyoPV = String.valueOf(anioVenta);
						String mesPV = String.valueOf(mesVenta);
						String diaPV = String.valueOf(diaVenta);

						String fechaPV = anyoPV + "/" + mesPV + "/" + diaPV;
						
						SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy/MM/dd");
						try {
							Date fechaParseada = formatoFecha.parse(fechaPV);
							f = new Timestamp(fechaParseada.getTime());
							entry.setPventa(f);
							//Fdiferimiento = new Timestamp(fechaParseada.getTime());
						} catch (ParseException e) {
							//Si el formato no es el esperado se lanza excepción.
							throw Solvencia2ExcepcionHelper.crearExcepcion("AP", new String[]{fechaPV, "yyyy/MM/dd"});
						}
						//entry.setPventa(new Timestamp(new GregorianCalendar(Integer.parseInt(umic.getDatosNiif17().getcohorte()), mes_fIniSus, dia_pv, CTE_0, CTE_0, CTE_0).getTimeInMillis()));
					}else if(fichaproceso.getCtipobt().equals("NIIF17IF")){
						entry.setTextraccion("02_00");
						entry.setTnegociolrc(0);
					}
					
					entry.setTpasivo("LRC");
					entry.setTnegocio(1);
					
					//Generar fecha flujo estimado
					
					Timestamp fdesde_fpsl = detalle.getFechaDesde();
					LocalDateTime dt_fpsl = new LocalDateTime(fdesde_fpsl.getTime());
					int mes_fpsl_fdesde = dt_fpsl.getMonthOfYear();
					int anyo_fpsl_fdesde = dt_fpsl.getYear();
					
					if(umic.getDatosNiif17().getFproyflujest() != null &&umic.getDatosNiif17().getFproyflujest().equals("I")){
						dia_fproyflujest = 1;
					}else if(umic.getDatosNiif17().getFproyflujest() != null && umic.getDatosNiif17().getFproyflujest().equals("M")){
						dia_fproyflujest=15;
						if(mes_fpsl_fdesde == 2){
							dia_fproyflujest=14;
						}
					}else if(umic.getDatosNiif17().getFproyflujest() != null && umic.getDatosNiif17().getFproyflujest().equals("F")){
						if(mes_fpsl_fdesde == 2){
							dia_fproyflujest=28;
						}else if(mes_fpsl_fdesde == 1 || 
								mes_fpsl_fdesde == 3 ||
								mes_fpsl_fdesde == 5 ||
								mes_fpsl_fdesde == 7 ||
								mes_fpsl_fdesde == 8 ||
								mes_fpsl_fdesde == 10 ||
								mes_fpsl_fdesde == 12){
							dia_fproyflujest=31;
						}else{
							dia_fproyflujest=30;
						}
					}else{
						//Saca error de que no viene ifnormado el campo correctamente o incidecnia infomrativa mejor.
						dia_fproyflujest=1;
					}
					
	//				FECHA PROYECCION DEL FLUJO ESTIMADO
					String anyo = String.valueOf(anyo_fpsl_fdesde);
					
					String mes = String.valueOf(mes_fpsl_fdesde);
					if(mes.length() == 1){
						mes = "0" + mes;
					}
					String dia_fecha = String.valueOf(dia_fproyflujest);
					if(dia_fecha.length() == 1){
						dia_fecha = "0" + dia_fecha;
					}
					
					String fechaFproy = anyo + "/" + mes + "/" + dia_fecha;
					Timestamp fProy = null;
					SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy/MM/dd");
					try {
						Date fechaParseada = formatoFecha.parse(fechaFproy);
						fProy = new Timestamp(fechaParseada.getTime());
						entry.setFproyflujest(fProy);
	
					} catch (ParseException e) {
						//Si el formato no es el esperado se lanza excepción.
						throw Solvencia2ExcepcionHelper.crearExcepcion("AP", new String[]{fechaFproy, "yyyy/MM/dd"});
					}
									
					//Codigo reasegurador se mostrara vacio
					//totales.setCodreaseg();
					entry.setMon_fall("EUR");
					entry.setMon_gto("EUR");
					entry.setMon_gtoadq("EUR");
					entry.setMon_pb("EUR");
					entry.setMon_prim("EUR");
					entry.setMon_rte("EUR");
					entry.setMon_vida("EUR");
				
				}
			}
				detalleCorrienteEntregablesDao.put(entry.getKey(), entry);
				
			}
		}
	}

	@Override
	public void almacenarEntregablePrvFpb(List<PrvFpb> prvfpbs) {
		if (prvfpbs != null && !prvfpbs.isEmpty()) {
			Map<PrvFpbKey, PrvFpb> mapa = new HashMap<PrvFpbKey, PrvFpb>();
			for (PrvFpb entry : prvfpbs) {
				mapa.put(entry.getKey(), entry);
			}
			prvFpbDao.putAll(mapa);
		}

	}

	@Override
	public void almacenarEntregableFlujTcas(List<FlujTcas> flujtcass) {
		if (flujtcass != null && !flujtcass.isEmpty()) {
			Map<FlujTcasKey, FlujTcas> mapa = new HashMap<FlujTcasKey, FlujTcas>();
			for (FlujTcas entry : flujtcass) {
				mapa.put(entry.getKey(), entry);
			}
			flujTcasDao.putAll(mapa);
		}

	}

	/* INI-TAR00302248 */
	@Override
	public void almacenarEntregableContabilidad(List<Contabilidad> contabilidad) {
		if (contabilidad != null && !contabilidad.isEmpty()) {
			Map<ContabilidadKey, Contabilidad> mapa = new HashMap<ContabilidadKey, Contabilidad>();
			for (Contabilidad entry : contabilidad) {
				mapa.put(entry.getKey(), entry);
			}
			contabilidadDao.putAll(mapa);
		}

	}
	/* FIN-TAR00302248 */

	@Override
	public void almacenarEntregableSCRVM(List<FactoresVolatilidad> factoresVolatilidad) {
		if (factoresVolatilidad != null && !factoresVolatilidad.isEmpty()) {
			Map<FactoresVolatilidadKey, FactoresVolatilidad> mapa = new HashMap<FactoresVolatilidadKey, FactoresVolatilidad>();
			for (FactoresVolatilidad entry : factoresVolatilidad) {
				mapa.put(entry.getKey(), entry);
			}
			factoresVolatilidadDao.putAll(mapa);
		}
	}

	public void almacenarValoresSCRVM(Umic umic, DetalleBaseTecnica detalleBaseTecnica,
			List<DetalleCorriente> detallesCorriente, FichaProceso fichaproceso) {
		MathContext MATH_CONTEXT = new MathContext(34, RoundingMode.HALF_DOWN);
		IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		String varCriterEdad = (String) servicio.recuperarDefinicionAuxiliar(umic.getDatosGenerales().getCcartera(),
				umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), detalleBaseTecnica.getBaseTec(), "ID-CRITERIO");
		String varCriterioFec = (String) servicio.recuperarDefinicionAuxiliar(umic.getDatosGenerales().getCcartera(),
				umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), detalleBaseTecnica.getBaseTec(), "ID-TEMPORAL");
		TotalesFlujos totales = servicio.recuperarTotalesFlujos(umic, detalleBaseTecnica);
		BigDecimal varXi = totales.getTotfnfall().subtract(totales.getTotprovision());

		if (varXi.signum() != -1) {
			Timestamp fecEfec = recuperarFechaEfecto(umic);
			BigDecimal edadAseg = recuperarEdad(umic, detalleBaseTecnica, fecEfec, varCriterEdad);
			Integer anioNacAseg = new LocalDateTime(umic.getAsegurados().getFnacAseg1().getTime()).getYear();
			String sexAseg = umic.getAsegurados().getCsexAseg1();
			List<BigDecimal> valoresQx = servicio.recuperarValoresExperiencia(umic, detalleBaseTecnica,
					Integer.toString(anioNacAseg), sexAseg, edadAseg.intValue(), "Q",
					IObtenerConfiguracion.OrdenAsegurado.ASEG1);

			BigDecimal annosFcierre = recuperarAnnos(fecEfec, fichaproceso.getFcalc(), varCriterioFec, MATH_CONTEXT);
			BigDecimal edadFcierre = annosFcierre.add(edadAseg.setScale(0, RoundingMode.HALF_UP));
			BigDecimal varLjEntero = valoresQx.get(edadFcierre.intValue());
			BigDecimal varLjEntero1 = valoresQx.get(edadFcierre.intValue() + 1);
			BigDecimal varLjEntero2 = valoresQx.get(edadFcierre.intValue() + 2);

			BigDecimal decEdadAseg = edadFcierre.subtract(edadFcierre.setScale(0, RoundingMode.DOWN));
			BigDecimal varLx1 = varLjEntero.add(decEdadAseg.multiply(varLjEntero1.subtract(varLjEntero)));
			BigDecimal varLx2 = varLjEntero1.add(decEdadAseg.multiply(varLjEntero2.subtract(varLjEntero1)));
			BigDecimal varQx = BigDecimal.ONE.subtract(varLx2.divide(varLx1, MATH_CONTEXT));

			totales.setQiXi2(varQx.multiply(varXi.pow(2)).setScale(8, RoundingMode.HALF_UP));
			totales.setQiXi3(varQx.multiply(varXi.pow(3)).setScale(8, RoundingMode.HALF_UP));
		} else {
			totales.setQiXi2(BigDecimal.ZERO);
			totales.setQiXi3(BigDecimal.ZERO);

		}

		totalesFlujoDao.put(totales.getKey(), totales);
	}

	@Override
	public void almacenarEntregableFlujosTotP(List<FlujosTotP> flujosTotPs) {
		if (flujosTotPs != null && !flujosTotPs.isEmpty()) {
			Map<FlujosTotPKey, FlujosTotP> mapa = new HashMap<FlujosTotPKey, FlujosTotP>();
			for (FlujosTotP entry : flujosTotPs) {
				mapa.put(entry.getKey(), entry);
			}
			flujosTotPDao.putAll(mapa);
		}
	}

	public void almacenarValoresFLUJOSTN17(Umic umic, DetalleBaseTecnica detalleBaseTecnica,
			List<DetalleCorriente> detallesCorriente, FichaProceso fichaproceso, TotalesFlujos totales) {

		FlujosTN17 flujos = new FlujosTN17();

		flujos.setCnegocio(totales.getCnegocio());
		flujos.setCcanal(totales.getCcanal());
		flujos.setCcartera(totales.getCcartera());
		flujos.setFcierre(totales.getFcierre());
		flujos.setBt(totales.getBt());
		flujos.setUoa(umic.getDatosNiif17().getuoa());
		flujos.setKcontrato(umic.getDatosNiif17().getkcarcontacto());
		flujos.setKramo(totales.getKramo());
		flujos.setKmodalidad(totales.getKmodalidad());
		flujos.setKpoliza(totales.getKpoliza());
		flujos.setKsubpoliza(totales.getKsubpoliza());
		flujos.setKcertificado(totales.getKcertificado());
		flujos.setNsuscri(totales.getNsuscri());
		flujos.setNorden(totales.getNorden());
		flujos.setKgarantia(totales.getKgarantia());
		flujos.setKprestacion(totales.getKprestacion());
		flujos.setKajuste(totales.getKajuste());
		flujos.setCtipoaport(totales.getCtipoaport());
		flujos.setIntfeccal(totales.getIntfeccal());
		flujos.setFsuscri(totales.getFsuscri());
		flujos.setKcarterainv(totales.getKcarterainv());
		flujos.setGapact(totales.getGapAct());
		flujos.setKmodext(totales.getKmodext());
		flujos.setSpcom(totales.getSpcom());
		flujos.setKbencon(totales.getKbencon());
		flujos.setUoa(totales.getUoa());
		flujos.setKcontrato(totales.getKcontrato());
		flujos.setTotfpvida(totales.getTotfpvida());
		flujos.setTotfpnavida(totales.getTotfpnavida());
		flujos.setTotfactvida(totales.getTotfactvida());
		flujos.setTotcolavida(totales.getTotcolavida());
		flujos.setTotfpfall(totales.getTotfpfall());
		flujos.setTotfpnafall(totales.getTotfpnafall());
		flujos.setTotfactfall(totales.getTotfactfall());
		flujos.setTotcolafall(totales.getTotcolafall());
		flujos.setTotfpcompl(totales.getTotfpcompl());
		flujos.setTotfpnacompl(totales.getTotfpnacompl());
		flujos.setTotfactcompl(totales.getTotfactcompl());
		flujos.setTotcolacompl(totales.getTotcolacompl());
		flujos.setTotfpgto(totales.getTotfpgto());
		flujos.setTotfpnagto(totales.getTotfpnagto());
		flujos.setTotfactgto(totales.getTotfactgto());
		flujos.setTotcolagto(totales.getTotcolagto());
		flujos.setTotfpcom(totales.getTotfpcom());
		flujos.setTotfpnacom(totales.getTotfpnacom());
		flujos.setTotfactcom(totales.getTotfactcom());
		flujos.setTotcolacom(totales.getTotcolacom());
		flujos.setTotfprte(totales.getTotfprte());
		flujos.setTotfpnarte(totales.getTotfpnarte());
		flujos.setTotfactrte(totales.getTotfactrte());
		flujos.setTotcolarte(totales.getTotcolarte());
		flujos.setTotfpprim(totales.getTotfpprim());
		flujos.setTotfpnaprim(totales.getTotfpnaprim());
		flujos.setTotfactprim(totales.getTotfactprim());
		flujos.setTotcolaprim(totales.getTotcolaprim());
		flujos.setTotfprob(totales.getTotfprob());
		flujos.setTotfprobtanul(totales.getTotfprobtanul());
		flujos.setTotprovision(totales.getTotprovision());
		flujos.setTotcola(totales.getTotcola());
		//flujos.setProvfcal(totalesN17.getProvfcal());
		flujos.setTotra(totales.getRaumic());
		flujos.setTotcsm(totales.getCsmumic());
		flujos.setTotcsmpatron(totales.getCsmajustado());
		flujos.setTotcsmrossp(totales.getCsm003());							
		
		flujos.setTotfpgtoad(totales.getTotfpgtoad());
		flujos.setTotfpnagtoad(totales.getTotfpnagtoad());
		flujos.setTotfactgtoad(totales.getTotfactgtoad());
		flujos.setTotcolagtoad(totales.getTotcolagtoad());

		//totalesFlujoDao.put(totales.getKey(), totales);
		flujosTotNiif17Dao.put(flujos.getKey(), flujos);

	}
	
	public void almacenarValoresFLUJOSTOTP(Umic umic, DetalleBaseTecnica detalleBaseTecnica,
			List<DetalleCorriente> detallesCorriente, FichaProceso fichaproceso, TotalesFlujos totales) {
		MathContext MATH_CONTEXT = new MathContext(34, RoundingMode.HALF_DOWN);
		//IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		FlujosTotP flujos = new FlujosTotP();
		flujos.setBt(totales.getBt());
		flujos.setCnegocio(totales.getCnegocio());
		flujos.setCcanal(totales.getCcanal());
		flujos.setCcartera(totales.getCcartera());
		flujos.setCtipoaport(totales.getCtipoaport());
		flujos.setCtipoprovi(totales.getCtipoprovi());
		flujos.setCtipramo(totales.getCtipramo());
		flujos.setCurvati(totales.getCurvati());
		flujos.setFcierre(totales.getFcierre());
		flujos.setFsuscri(totales.getFsuscri());
		flujos.setGapAct(totales.getGapAct());
		flujos.setGestionit(totales.getGestionit());
		flujos.setIndicrescate(totales.getIndicrescate());
		flujos.setIntBTI(totales.getIntBTI());
		flujos.setIntfeccal(totales.getIntfeccal());
		flujos.setKajuste(totales.getKajuste());
		flujos.setKcarterainv(totales.getKcarterainv());
		flujos.setKcertificado(totales.getKcertificado());
		flujos.setKgarantia(totales.getKgarantia());
		flujos.setKmodalidad(totales.getKmodalidad());
		flujos.setKpoliza(totales.getKpoliza());
		flujos.setKprestacion(totales.getKprestacion());
		flujos.setKsubpoliza(totales.getKsubpoliza());
		flujos.setNorden(totales.getNorden());
		flujos.setNsuscri(totales.getNsuscri());
		flujos.setProvbtifcal(totales.getProvbtifcal());
		flujos.setTotcola(totales.getTotcola());
		flujos.setTotcolacom(totales.getTotcolacom());
		flujos.setTotcolacompl(totales.getTotcolacompl());
		flujos.setTotcolafall(totales.getTotcolafall());
		flujos.setTotcolagto(totales.getTotcolagto());
		flujos.setTotcolaprim(totales.getTotcolaprim());
		flujos.setTotcolarte(totales.getTotcolarte());
		flujos.setTotcolavida(totales.getTotcolavida());
		flujos.setTotfactcom(totales.getTotfactcom());
		flujos.setTotfactcompl(totales.getTotfactcompl());
		flujos.setTotfactfall(totales.getTotfactfall());
		flujos.setTotfactgto(totales.getTotfactgto());
		flujos.setTotfactprim(totales.getTotfactprim());
		flujos.setTotfactrte(totales.getTotfactrte());
		flujos.setTotfactvida(totales.getTotfactvida());
		flujos.setTotfpcom(totales.getTotfpcom());
		flujos.setTotfpcompl(totales.getTotfpcompl());
		flujos.setTotfpfall(totales.getTotfpfall());
		flujos.setTotfpgto(totales.getTotfpgto());
		flujos.setTotfpnacom(totales.getTotfpnacom());
		flujos.setTotfpnacompl(totales.getTotfpnacompl());
		flujos.setTotfpnafall(totales.getTotfpnafall());
		flujos.setTotfpnagto(totales.getTotfpnagto());
		flujos.setTotfpnaprim(totales.getTotfpnaprim());
		flujos.setTotfpnarte(totales.getTotfpnarte());
		flujos.setTotfpnavida(totales.getTotfpnavida());
		flujos.setTotfpprim(totales.getTotfpprim());
		flujos.setTotfprob(totales.getTotfprob());
		flujos.setTotfprte(totales.getTotfprte());
		flujos.setTotfpvida(totales.getTotfpvida());
		flujos.setTotfprobtanul(totales.getTotfprobtanul());
		flujos.setTotprovision(totales.getTotprovision());
		flujos.setKramo(totales.getKramo());
		flujos.setFsuscri(totales.getFsuscri());
		flujos.setKcarterainv(totales.getKcarterainv());
		flujos.setGapAct(totales.getGapAct());
		flujos.setIntfeccal(totales.getIntfeccal());
		flujos.setIntBTI(totales.getIntBTI());
		flujos.setCtipoprovi(totales.getCtipoprovi());
		flujos.setSpcom(totales.getSpcom());
		flujos.setKoficont(totales.getKoficont());
		flujos.setPfpinv(totales.getPfpinv());
		flujos.setCtipramo(totales.getCtipramo());
		flujos.setSegmento1(totales.getSegmento1());
		flujos.setTiposubriesgo(totales.getTiposubriesgo());
		flujos.setNuevaprodu(totales.isNuevaprodu());
		flujos.setIndicrescate(totales.getIndicrescate());
		flujos.setTotfnrte(totales.getTotfnrte());
		flujos.setCurvati(totales.getCurvati());
		flujos.setTotfnfall(totales.getTotfnfall());
		flujos.setKmodext(totales.getKmodext());
		flujos.setGestionit(totales.getGestionit());
		flujos.setQiXi2(totales.getQiXi2());
		flujos.setQiXi3(totales.getQiXi3());
		flujos.setKbencon(totales.getKbencon());
//		flujos.setRaumic(totales.getRaumic());
//		flujos.setCsmumic(totales.getCsmumic());
//		flujos.setCsmajustado(totales.getCsmajustado());													  
		if (umic.getPrimas().getIprimatarada() == BigDecimal.ZERO) {
			flujos.setIprimanetaini(umic.getPrimas().getIprimanetaini());
		}else {
			flujos.setIprimanetaini(umic.getPrimas().getIprimatarada());
		}
		
		if (flujos.getIprimanetaini() == BigDecimal.ZERO) {
			flujos.setDesviacion(BigDecimal.ZERO);
		}else {
			flujos.setDesviacion((totales.getTotprovision().subtract(flujos.getIprimanetaini()))
				.divide(flujos.getIprimanetaini(), MATH_CONTEXT).multiply(BigDecimal.valueOf(100)));
		}
		flujos.setPcoaseg(umic.getDatosCoaseguro().getPcoaseg());
		flujos.setPgastgesex1i(umic.getBti().getPgastgesex1I());
		flujos.setCsitupol(umic.getDatosGenerales().getCsitupol());
		
		flujos.setTotcolagtoad(totales.getTotcolagtoad());
		flujos.setTotfactgtoad(totales.getTotfactgtoad());
		flujos.setTotfpgtoad(totales.getTotfpgtoad());
		flujos.setTotfpnagtoad(totales.getTotfpnagtoad());
		
		totalesFlujoDao.put(totales.getKey(), totales);
		flujosTotPDao.put(flujos.getKey(), flujos);
	}

	private Timestamp recuperarFechaEfecto(Umic umic) {
		Timestamp varfec = null;
		if (umic.getDatosGenerales().getCsitupol().equals("VI")) {
			if (umic.getDatosGenerales().getCnegocio().equals("I")) {
				varfec = umic.getFechas().getFecefecini();
			} else {
				varfec = umic.getFechas().getFecinisus();
			}
		} else if (umic.getDatosGenerales().getCsitupol().equals("RE")) {
			varfec = umic.getFechas().getFecefecred();
			if (varfec.equals(null) && umic.getDatosGenerales().getCnegocio().equals("C")) {
				varfec = umic.getFechas().getFecfinpagprim();
			}
		}
		return varfec;
	}

	private BigDecimal recuperarAnnos(Timestamp fecha1, Timestamp fecha2, String criterioFecha,
			MathContext mathContext) {

		final int[] ARRAY_BASE_365 = { 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334 };
		final int[] ARRAY_BASE_360 = { 0, 30, 60, 90, 120, 150, 180, 210, 240, 270, 300, 330 };

		final LocalDateTime fecha1u = new LocalDateTime(fecha1.getTime());
		final LocalDateTime fecha2u = new LocalDateTime(fecha2.getTime());

		BigDecimal numeroAnnos = BigDecimal.ZERO;
		int dias1 = 0;
		int dias2 = 0;

		if ("01".equals(criterioFecha)) {
			dias1 = fecha1u.getYear() * 365 + ARRAY_BASE_365[fecha1u.getMonthOfYear() - 1] + fecha1u.getDayOfMonth();
			dias2 = fecha2u.getYear() * 365 + ARRAY_BASE_365[fecha2u.getMonthOfYear() - 1] + fecha2u.getDayOfMonth();
			int diferencia = dias2 - dias1;
			if (diferencia < 0)
				diferencia = -diferencia;
			numeroAnnos = new BigDecimal(String.valueOf(diferencia / 365d));
		} else if ("02".equals(criterioFecha)) {
			dias1 = fecha1u.getYear() * 360 + ARRAY_BASE_360[fecha1u.getMonthOfYear() - 1] + fecha1u.getDayOfMonth();
			dias2 = fecha2u.getYear() * 360 + ARRAY_BASE_360[fecha2u.getMonthOfYear() - 1] + fecha2u.getDayOfMonth();
			numeroAnnos = BigDecimal.valueOf(dias2 - dias1).divide(BigDecimal.valueOf(360), mathContext).abs();
		} else if ("03".equals(criterioFecha)) {
			final BigDecimal operador1 = BigDecimal.valueOf(fecha2u.getYear() - fecha1u.getYear());
			if (fecha1u.getYear() <= fecha2u.getYear()) {
				final BigDecimal operador2 = BigDecimal.valueOf(fecha2u.getMonthOfYear() - fecha1u.getMonthOfYear())
						.multiply(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(12), mathContext));
				numeroAnnos = operador1.add(operador2);
			} else {
				final BigDecimal operador2 = BigDecimal.valueOf(fecha2u.getMonthOfYear() - fecha1u.getMonthOfYear() - 1)
						.multiply(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(12), mathContext));
				numeroAnnos = operador1.add(operador2);
			}
		}
		return numeroAnnos;
	}

	private BigDecimal recuperarEdad(Umic umic, DetalleBaseTecnica detalleBaseTecnica, Timestamp varfec,
			String varCriterEdad) {

		LocalDateTime varFecEfecto, fcalcu, fecNacAseg;
		BigDecimal totalAnioMesDia, valorRedondeado, edadAseg = null;
		MathContext MATH_CONTEXT = new MathContext(34, RoundingMode.HALF_DOWN);

		varFecEfecto = new LocalDateTime(varfec.getTime());

		fecNacAseg = new LocalDateTime(umic.getAsegurados().getFnacAseg1().getTime());
		if (varCriterEdad.equals("06")) {
			fcalcu = new LocalDateTime(umic.getRentas().getFecIni().getTime());
		} else {
			fcalcu = varFecEfecto;
		}

		BigDecimal anioTotal = BigDecimal.valueOf(fcalcu.getYear() - fecNacAseg.getYear());
		BigDecimal mesTotal = BigDecimal.valueOf(fcalcu.getMonthOfYear() - fecNacAseg.getMonthOfYear())
				.multiply(BigDecimal.ONE.divide(BigDecimal.valueOf(12), 34, RoundingMode.HALF_UP));
		BigDecimal diaTotal = BigDecimal.valueOf(fcalcu.getDayOfMonth() - fecNacAseg.getDayOfMonth())
				.divide(BigDecimal.valueOf(365), 34, RoundingMode.HALF_UP);

		if (varCriterEdad.equals("02")) {
			totalAnioMesDia = anioTotal.add(mesTotal).add(diaTotal).multiply(BigDecimal.valueOf(12));
			valorRedondeado = totalAnioMesDia.setScale(0, RoundingMode.HALF_UP);
			edadAseg = valorRedondeado.divide(BigDecimal.valueOf(12), MATH_CONTEXT);
		} else if (varCriterEdad.equals("03")) {
			totalAnioMesDia = anioTotal.add(mesTotal).add(diaTotal);
			valorRedondeado = totalAnioMesDia.setScale(0, RoundingMode.HALF_UP);
			edadAseg = valorRedondeado;
		}
		edadAseg = edadAseg.add(BigDecimal.valueOf(umic.getDatosGenerales().getEdifer()));

		return edadAseg;
	}

	
	public void eliminarTotalesFlujos (TotalesFlujos totalesFlujos){
		totalesFlujoDao.remove(totalesFlujos.getKey());
	}

	public void eliminarDetalleBaseTecnica(DetalleBaseTecnica detallebt) {
		detalleBaseTecnicaDao.remove(detallebt.getKey());
	}

	public void eliminarProyeccion(DetalleCorriente dC) {
		detallesDao.remove(dC.getKey());
	}
	
	public String getParametros(String gparametros, Integer orden) {
		String[] parametros = new String[10];
		for(int i=0; i<gparametros.length(); i=i+20) {
				parametros[i%20] = gparametros.substring(i, i+20);
		}	
		return parametros[orden-1];
	}
	
	
	@Override
	public void almacenarEntregableFlujInf3(List<FlujInf3> flujinf3s) {
		if (flujinf3s != null && !flujinf3s.isEmpty()) {
			Map<FlujInf3Key, FlujInf3> mapa = new HashMap<FlujInf3Key, FlujInf3>();
			for (FlujInf3 entry : flujinf3s) {
				mapa.put(entry.getKey(), entry);
			}
			flujInf3Dao.putAll(mapa);
		}
	}
	
	@Override
	public void almacenarEntregableFlujoTotPV(List<FlujoTotPV> flujinf4s) {
		if (flujinf4s != null && !flujinf4s.isEmpty()) {
			Map<FlujoTotPVKey, FlujoTotPV> mapa = new HashMap<FlujoTotPVKey, FlujoTotPV>();
			for (FlujoTotPV entry : flujinf4s) {
				mapa.put(entry.getKey(), entry);
			}
			flujoTotPVDao.putAll(mapa);
		}
	}
	@Override
	public void almacenarEntregableFlujInf4(List<FlujInf4> flujinf3s) {
		if (flujinf3s != null && !flujinf3s.isEmpty()) {
			Map<FlujInf4Key, FlujInf4> mapa = new HashMap<FlujInf4Key, FlujInf4>();
			for (FlujInf4 entry : flujinf3s) {
				mapa.put(entry.getKey(), entry);
			}
			flujInf4Dao.putAll(mapa);
		}
	}

	@Override
	public void almacenarEntregableFlujosTN17(List<FlujosTN17> flujosTotNiif17) {
		if (flujosTotNiif17 != null && !flujosTotNiif17.isEmpty()) {
			Map<FlujosTN17Key, FlujosTN17> mapa = new HashMap<FlujosTN17Key, FlujosTN17>();
			for (FlujosTN17 entry : flujosTotNiif17) {
				mapa.put(entry.getKey(), entry);
			}
			flujosTotNiif17Dao.putAll(mapa);
		}
		
	}
	
	@Override
	public void almacenarEntregableFPSL(List<FPSL> fpsl) {
		if (fpsl != null && !fpsl.isEmpty()) {
			Map<FPSLKey, FPSL> mapa = new HashMap<FPSLKey, FPSL>();
			for (FPSL entry : fpsl) {
				mapa.put(entry.getKey(), entry);
			}
			FPSLDao.putAll(mapa);
		}
	}
	
	@Override
	public void almacenarEntregablePesosBt(List<PesosBt> pesosBt) {
		if (pesosBt != null && !pesosBt.isEmpty()) {
			Map<PesosBtKey, PesosBt> mapa = new HashMap<PesosBtKey, PesosBt>();
			for (PesosBt entry : pesosBt) {
				mapa.put(entry.getKey(), entry);
			}
			pesosBtDao.putAll(mapa);
		}
	}

	@Override
	public void almacenarEntregablePesosBtProxy(List<PesosBtProxy> pesosBtProxy) {
		pesosBtProxyDao.clear();
		if (pesosBtProxy != null && !pesosBtProxy.isEmpty()) {
			Map<PesosBtProxyKey, PesosBtProxy> mapa = new HashMap<PesosBtProxyKey, PesosBtProxy>();
			for (PesosBtProxy entry : pesosBtProxy) {
				mapa.put(entry.getKey(), entry);
			}
			pesosBtProxyDao.putAll(mapa);
		}
	}
	
	@Override
	public void almacenarEntregablePatronCsm(List<PatronCsm> patronCsm) {
		if (patronCsm != null && !patronCsm.isEmpty()) {
			Map<PatronCsmKey, PatronCsm> mapa = new HashMap<PatronCsmKey, PatronCsm>();
			for (PatronCsm entry : patronCsm) {
				mapa.put(entry.getKey(), entry);
			}
			patronCsmDao.putAll(mapa);
		}
	}
	
	@Override
	public void almacenarEntregableSWCOBROCOM(List<SwCobroCom> swCobroCom) {
		if (swCobroCom != null && !swCobroCom.isEmpty()) {
			Map<SwCobroComKey, SwCobroCom> mapa = new HashMap<SwCobroComKey, SwCobroCom>();
			for (SwCobroCom entry : swCobroCom) {
				mapa.put(entry.getKey(), entry);
			}
			swCobroComDao.putAll(mapa);
		}
	}
	
	@Override
	public void almacenarEntregableSwCobroComCsv(List<SwCobroComCsv> swCobroComCsv) {
		if (swCobroComCsv != null && !swCobroComCsv.isEmpty()) {
			Map<SwCobroComCsvKey, SwCobroComCsv> mapa = new HashMap<SwCobroComCsvKey, SwCobroComCsv>();
			for (SwCobroComCsv entry : swCobroComCsv) {
				mapa.put(entry.getKey(), entry);
			}
			swCobroComCsvDao.putAll(mapa);
		}
	}
	
	@Override
	public void almacenarEntregableTotPMaCoa(List<TotPMaCoa> totPMaCoa) {
		if (totPMaCoa != null && !totPMaCoa.isEmpty()) {
			Map<TotPMaCoaKey, TotPMaCoa> mapa = new HashMap<TotPMaCoaKey, TotPMaCoa>();
			for (TotPMaCoa entry : totPMaCoa) {
				mapa.put(entry.getKey(), entry);
			}
			totPMaCoaDao.putAll(mapa);
		}
	}
	
	@Override
	public void almacenarEntregableFlujPMaCoa(List<FlujPMaCoa> flujPMaCoa) {
		if (flujPMaCoa != null && !flujPMaCoa.isEmpty()) {
			Map<FlujPMaCoaKey, FlujPMaCoa> mapa = new HashMap<FlujPMaCoaKey, FlujPMaCoa>();
			for (FlujPMaCoa entry : flujPMaCoa) {
				mapa.put(entry.getKey(), entry);
			}
			flujPMaCoaDao.putAll(mapa);
		}
	}
	
	@Override
	public void almacenarEntregableFlujPMdCoa(List<FlujPMdCoa> flujPMdCoa) {
		if (flujPMdCoa != null && !flujPMdCoa.isEmpty()) {
			Map<FlujPMdCoaKey, FlujPMdCoa> mapa = new HashMap<FlujPMdCoaKey, FlujPMdCoa>();
			for (FlujPMdCoa entry : flujPMdCoa) {
				mapa.put(entry.getKey(), entry);
			}
			flujPMdCoaDao.putAll(mapa);
		}
	}
	
	@Override
	public void almacenarEntregableTotPMaCoaM(List<TotPMaCoaM> totPMaCoaM) {
		if (totPMaCoaM != null && !totPMaCoaM.isEmpty()) {
			Map<TotPMaCoaMKey, TotPMaCoaM> mapa = new HashMap<TotPMaCoaMKey, TotPMaCoaM>();
			for (TotPMaCoaM entry : totPMaCoaM) {
				mapa.put(entry.getKey(), entry);
			}
			totPMaCoaMDao.putAll(mapa);
		}
	}
	
	@Override
	public void almacenarEntregableFlujPMaCoaM(List<FlujPMaCoaM> flujPMaCoaM) {
		if (flujPMaCoaM != null && !flujPMaCoaM.isEmpty()) {
			Map<FlujPMaCoaMKey, FlujPMaCoaM> mapa = new HashMap<FlujPMaCoaMKey, FlujPMaCoaM>();
			for (FlujPMaCoaM entry : flujPMaCoaM) {
				mapa.put(entry.getKey(), entry);
			}
			flujPMaCoaMDao.putAll(mapa);
		}
	}
	
	@Override
	public void almacenarEntregableFlujPMdCoaM(List<FlujPMdCoaM> flujPMdCoaM) {
		if (flujPMdCoaM != null && !flujPMdCoaM.isEmpty()) {
			Map<FlujPMdCoaMKey, FlujPMdCoaM> mapa = new HashMap<FlujPMdCoaMKey, FlujPMdCoaM>();
			for (FlujPMdCoaM entry : flujPMdCoaM) {
				mapa.put(entry.getKey(), entry);
			}
			flujPMdCoaMDao.putAll(mapa);
		}
	}
	
	@Override
	public void almacenarEntregablesCoaseguro(Umic umic, DetalleBaseTecnica detalleBaseTecnica,
			List<DetalleCorriente> detallesCorriente, FichaProceso fichaproceso, TotalesFlujos totales) {
		FlujCoaSeg flujos = new FlujCoaSeg();
		List<FlujTcas> flujtcass = new ArrayList<FlujTcas>();
		List<FlujCoaSeg> flujcoaseg = new ArrayList<FlujCoaSeg>();
		List<ProvCoaSeg> provcoasegg = new ArrayList<ProvCoaSeg>();
		List<String> swcasados = detalleBaseTecnica.getSwcasado();
		List<Timestamp> fecinis = detalleBaseTecnica.getFecInitramo();
		List<Timestamp> fecfins = detalleBaseTecnica.getFecfintramo();
		Integer ccartera = detalleBaseTecnica.getCcartera();
		final IObtenerConfiguracion servicioConfiguracion = FachadaServicios.getObtenerConfiguracion();	

		for (int i=0; i < detallesCorriente.size();i++) {
			
			// Fecha desde a primer día del mes
			
			Timestamp fdesde = detallesCorriente.get(i).getFechaDesde();
			LocalDateTime dt = new LocalDateTime(fdesde.getTime());
			int dia = dt.getDayOfMonth();
			fdesde = new Timestamp(dt.minusDays(dia - 1).toDateTime().getMillis());
						
			flujos = servicioConfiguracion.recuperarFlujCoaSeg(detallesCorriente.get(i).getBt(), detallesCorriente.get(i).getFcierre(), 
					detallesCorriente.get(i).getCnegocio(), detallesCorriente.get(i).getCcanal(), detallesCorriente.get(i).getKramo(), 
					detallesCorriente.get(i).getKmodalidad(), umic.getDatosGenerales().getSegmento1(), 
					umic.getDatosGenerales().getTipoSubriesgo(), detallesCorriente.get(i).getKpoliza(), detallesCorriente.get(i).getKsubpoliza(), 
					detallesCorriente.get(i).getNsuscri(), detallesCorriente.get(i).getKcarterainv(), detallesCorriente.get(i).getGapAct(), 
					fdesde, umic.getDatosGenerales().getGestionit(), umic.getBti().getTabla1Aseg1());
			if (flujos == null) {
				flujos = new FlujCoaSeg();
			}

			if (flujos != null && flujos.getBt() != null) {
				flujos.setNveces(0);
			} else {	
				flujos.setBt(detallesCorriente.get(i).getBt());
				flujos.setCcanal(detallesCorriente.get(i).getCcanal());
				flujos.setCnegocio(detallesCorriente.get(i).getCnegocio());
				flujos.setFactor1(detalleBaseTecnica.getFactor1());
				flujos.setFeccierre(detallesCorriente.get(i).getFcierre());
				flujos.setFecdesde(fdesde);
				flujos.setFecfintramo1(umic.getBti().getFecFinTramo1());
				flujos.setFinisusc(umic.getFechas().getFecinisus());
				flujos.setGapact(detallesCorriente.get(i).getGapAct());
				flujos.setGastreal(detalleBaseTecnica.getGtoprov());
				flujos.setGastrealunitario(detalleBaseTecnica.getGtoUni());
				flujos.setGestionit(umic.getDatosGenerales().getGestionit());				
				flujos.setKcarterainv(detallesCorriente.get(i).getKcarterainv());
				DatosAdicionalesCoaseguro datosAdicionalesCoaseguro = datosAdicionales.get(new DatosAdicionalesCoaseguroKey(umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza()));
				if(datosAdicionalesCoaseguro != null) {
					flujos.setKcoase1(datosAdicionalesCoaseguro.getKcoase1());
					flujos.setKcoase2(datosAdicionalesCoaseguro.getKcoase2());
					flujos.setKcoase3(datosAdicionalesCoaseguro.getKcoase3());
					flujos.setKcoase4(datosAdicionalesCoaseguro.getKcoase4());
					flujos.setKcoase5(datosAdicionalesCoaseguro.getKcoase5());
					flujos.setKcoase6(datosAdicionalesCoaseguro.getKcoase6());
					flujos.setKcoase7(datosAdicionalesCoaseguro.getKcoase7());
					flujos.setKcoase8(datosAdicionalesCoaseguro.getKcoase8());
					flujos.setKcoase9(datosAdicionalesCoaseguro.getKcoase9());
					flujos.setKcoase10(datosAdicionalesCoaseguro.getKcoase10());
					flujos.setKcoase11(datosAdicionalesCoaseguro.getKcoase11());
					flujos.setKcoase12(datosAdicionalesCoaseguro.getKcoase12());
					flujos.setKcoase13(datosAdicionalesCoaseguro.getKcoase13());
					flujos.setKcoase14(datosAdicionalesCoaseguro.getKcoase14());
					flujos.setKcoase15(datosAdicionalesCoaseguro.getKcoase15());
					flujos.setKcoase16(datosAdicionalesCoaseguro.getKcoase16());
					flujos.setKcoase17(datosAdicionalesCoaseguro.getKcoase17());
					flujos.setKcoase18(datosAdicionalesCoaseguro.getKcoase18());
					flujos.setKcoase19(datosAdicionalesCoaseguro.getKcoase19());
					flujos.setKcoase20(datosAdicionalesCoaseguro.getKcoase20());
					flujos.setKcuadro(datosAdicionalesCoaseguro.getKcuadro());
				}
				flujos.setKmodalidad(detallesCorriente.get(i).getKmodalidad());
				flujos.setKpoliza(detallesCorriente.get(i).getKpoliza());
				flujos.setKramo(detallesCorriente.get(i).getKramo());
				flujos.setKsubpoliza(detallesCorriente.get(i).getKsubpoliza());
				flujos.setNsuscri(detallesCorriente.get(i).getNsuscri());
				flujos.setNveces(0);
				flujos.setPgastgesin1(umic.getBti().getPgastgesin1I());
				flujos.setPgastgesin2(umic.getBti().getPgastgesin2I());
				flujos.setPintertecn1(umic.getBti().getPintertecnI1());
				flujos.setPintertecn2(umic.getBti().getPintertecnI2());
				flujos.setSegmento1(umic.getDatosGenerales().getSegmento1());
				flujos.setTabla1(umic.getBti().getTabla1Aseg1());
				flujos.setTiposubriesgo(umic.getDatosGenerales().getTipoSubriesgo());
				flujos.setTotflujonomdegastos(BigDecimal.ZERO);
				flujos.setTotflujoprobdegastos(BigDecimal.ZERO);
				flujos.setTotflujoprobsingastos(BigDecimal.ZERO);
				flujos.setIpc(servicioConfiguracion.recuperarIpcFuturo(detallesCorriente.get(i).getFcierre(), detallesCorriente.get(i).getBt()));
				if (swcasados != null && ccartera != null) {
					for (int j = 0; j < swcasados.size(); j++) {
						if (swcasados.get(j) != null && swcasados.get(j).equals("S")) {

							NamedCache baseTecnicaInicial = CacheFactory.getCache("baseTecnicaInicial");

							ValueExtractor[] fecIniFinExtractor = new ValueExtractor[] {
									// FECINITRAMOX
									new PofExtractor(Timestamp.class, 6 + j),
									// FECFINTRAMOX
									new PofExtractor(Timestamp.class, 1 + j) };
							ValueExtractor multiExtractor = new MultiExtractor(fecIniFinExtractor);

							List<Timestamp> fecTramo = (List<Timestamp>) baseTecnicaInicial.invoke(umic.getKey(),
									new ExtractorProcessor(multiExtractor));
							
							String varCriterFec = (String) servicioConfiguracion.recuperarDefinicionAuxiliar(ccartera,
									umic.getKey().getKmodalidad(), umic.getKey().getKgarantia(), detallesCorriente.get(i).getBt(), "ID-TEMPORAL");

							if (fecTramo.size() >= 2 && fecTramo.get(0) != null
									&& fecTramo.get(1) != null)
								flujos.setDurtrcasado(nAnnos(fecTramo.get(0),
										fecTramo.get(1), varCriterFec));
						}
					}
				} else {

					Exception e = new Solvencia2Excepcion("No se ha encontrado la cartera o el swcasado", new Incidencia());
					e.printStackTrace();
				}
			}
			flujcoaseg.add(flujos);
			
			// Entrgable FlujTCas
			
			FlujTcas flujTcas = new FlujTcas();
			flujTcas = servicioConfiguracion.recuperarFlujTcas(detallesCorriente.get(i).getBt(), detallesCorriente.get(i).getFcierre(), 
					detallesCorriente.get(i).getCnegocio(), detallesCorriente.get(i).getCcanal(), detallesCorriente.get(i).getKramo(), 
					detallesCorriente.get(i).getKmodalidad(), detallesCorriente.get(i).getKpoliza(), detallesCorriente.get(i).getKsubpoliza(), 
					detallesCorriente.get(i).getNsuscri(), detallesCorriente.get(i).getKcarterainv(), detallesCorriente.get(i).getGapAct(), 
					fdesde);
			if (flujTcas == null) {
				flujTcas = new FlujTcas();
			}
			if (flujTcas != null && flujTcas.getBt() != null) {
				if (!calcularFlujoProbable(fecinis, fecfins, swcasados, flujTcas.getFecdesde(), flujTcas.getFinit())) {
					flujTcas.setImpflujprob(null);
				}
			} else {
				flujTcas.setBt(detallesCorriente.get(i).getBt());
				flujTcas.setCcanal(detallesCorriente.get(i).getCcanal());
				flujTcas.setCnegocio(detallesCorriente.get(i).getCnegocio());
				flujTcas.setFeccierre(detallesCorriente.get(i).getFcierre());
				flujTcas.setKcarterainv(detallesCorriente.get(i).getKcarterainv());
				flujTcas.setFecdesde(detallesCorriente.get(i).getFechaDesde());
				flujTcas.setGapact(detallesCorriente.get(i).getGapAct());
				flujTcas.setGestionit(umic.getDatosGenerales().getGestionit());
				flujTcas.setKmodalidad(detallesCorriente.get(i).getKmodalidad());
				flujTcas.setKpoliza(detallesCorriente.get(i).getKpoliza());
				flujTcas.setKramo(detallesCorriente.get(i).getKramo());
				flujTcas.setKsubpoliza(detallesCorriente.get(i).getKsubpoliza());
				flujTcas.setNsuscri(detallesCorriente.get(i).getNsuscri());
				flujTcas.setFsuscri(umic.getFechas().getFecinisus());
				flujTcas.setFinit(umic.getBti().getFecFinTramo1());
				flujTcas.setIt2(umic.getBti().getPintertecnI2());
				flujTcas.setItdgs(getIntDGSCoaseg());
				flujTcas.setImpflujact(BigDecimal.ZERO);
				flujTcas.setImpflujprob(BigDecimal.ZERO);
				if (!calcularFlujoProbable(fecinis, fecfins, swcasados, flujTcas.getFecdesde(), flujTcas.getFinit())) {
					flujTcas.setImpflujprob(null);
				}
			}
			if (flujTcas.getImpflujprob() != null) {
				flujtcass.add(flujTcas);
			}
			
			// Entregable PROVCOASEG
			
			ProvCoaSeg provcoaseg = servicioConfiguracion.recuperarProvCoaSeg(detallesCorriente.get(i).getBt(), detallesCorriente.get(i).getFcierre(), 
					detallesCorriente.get(i).getCnegocio(), detallesCorriente.get(i).getCcanal(), detallesCorriente.get(i).getKramo(), 
					detallesCorriente.get(i).getKmodalidad(), umic.getDatosGenerales().getSegmento1(), 
					umic.getDatosGenerales().getTipoSubriesgo(), detallesCorriente.get(i).getKpoliza(), detallesCorriente.get(i).getKsubpoliza(), 
					detallesCorriente.get(i).getNsuscri(), detallesCorriente.get(i).getKcarterainv(), detallesCorriente.get(i).getGapAct(), 
					umic.getDatosGenerales().getGestionit(), umic.getBti().getTabla1Aseg1());
			if (provcoaseg == null) {
				provcoaseg = new ProvCoaSeg();
			}
			
			if (provcoaseg != null && provcoaseg.getBt() != null) {
				provcoaseg.setTotflujoactcongastos(BigDecimal.ZERO);
				provcoaseg.setTotflujoactsingastos(BigDecimal.ZERO);
			} else {
				
				provcoaseg.setBt(detallesCorriente.get(i).getBt());
				provcoaseg.setCcanal(detallesCorriente.get(i).getCcanal());
				provcoaseg.setCnegocio(detallesCorriente.get(i).getCnegocio());
				provcoaseg.setFactor1(detalleBaseTecnica.getFactor1());
				provcoaseg.setFeccierre(detallesCorriente.get(i).getFcierre());
				provcoaseg.setFecfintramo1(umic.getBti().getFecFinTramo1());
				provcoaseg.setFinisusc(umic.getFechas().getFecinisus());
				provcoaseg.setGapact(detallesCorriente.get(i).getGapAct());
				provcoaseg.setGastreal(detalleBaseTecnica.getGtoprov());
				provcoaseg.setGastrealunitario(detalleBaseTecnica.getGtoUni());
				provcoaseg.setGestionit(umic.getDatosGenerales().getGestionit());		
				provcoaseg.setKcarterainv(detallesCorriente.get(i).getKcarterainv());
				DatosAdicionalesCoaseguro datosAdicionalesCoaseguro = datosAdicionales.get(new DatosAdicionalesCoaseguroKey(umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza()));
				if(datosAdicionalesCoaseguro != null) {
					provcoaseg.setKcoase1(datosAdicionalesCoaseguro.getKcoase1());
					provcoaseg.setKcoase2(datosAdicionalesCoaseguro.getKcoase2());
					provcoaseg.setKcoase3(datosAdicionalesCoaseguro.getKcoase3());
					provcoaseg.setKcoase4(datosAdicionalesCoaseguro.getKcoase4());
					provcoaseg.setKcoase5(datosAdicionalesCoaseguro.getKcoase5());
					provcoaseg.setKcoase6(datosAdicionalesCoaseguro.getKcoase6());
					provcoaseg.setKcoase7(datosAdicionalesCoaseguro.getKcoase7());
					provcoaseg.setKcoase8(datosAdicionalesCoaseguro.getKcoase8());
					provcoaseg.setKcoase9(datosAdicionalesCoaseguro.getKcoase9());
					provcoaseg.setKcoase10(datosAdicionalesCoaseguro.getKcoase10());
					provcoaseg.setKcoase11(datosAdicionalesCoaseguro.getKcoase11());
					provcoaseg.setKcoase12(datosAdicionalesCoaseguro.getKcoase12());
					provcoaseg.setKcoase13(datosAdicionalesCoaseguro.getKcoase13());
					provcoaseg.setKcoase14(datosAdicionalesCoaseguro.getKcoase14());
					provcoaseg.setKcoase15(datosAdicionalesCoaseguro.getKcoase15());
					provcoaseg.setKcoase16(datosAdicionalesCoaseguro.getKcoase16());
					provcoaseg.setKcoase17(datosAdicionalesCoaseguro.getKcoase17());
					provcoaseg.setKcoase18(datosAdicionalesCoaseguro.getKcoase18());
					provcoaseg.setKcoase19(datosAdicionalesCoaseguro.getKcoase19());
					provcoaseg.setKcoase20(datosAdicionalesCoaseguro.getKcoase20());
					provcoaseg.setKcuadro(datosAdicionalesCoaseguro.getKcuadro());
				}
				provcoaseg.setKmodalidad(detallesCorriente.get(i).getKmodalidad());
				provcoaseg.setKpoliza(detallesCorriente.get(i).getKpoliza());
				provcoaseg.setKramo(detallesCorriente.get(i).getKramo());
				provcoaseg.setKsubpoliza(detallesCorriente.get(i).getKsubpoliza());
				provcoaseg.setNsuscri(detallesCorriente.get(i).getNsuscri());
				provcoaseg.setPgastgesin1(umic.getBti().getPgastgesin1I());
				provcoaseg.setPgastgesin2(umic.getBti().getPgastgesin2I());
				provcoaseg.setPintertecn1(umic.getBti().getPintertecnI1());
				provcoaseg.setPintertecn2(umic.getBti().getPintertecnI2());
				provcoaseg.setSegmento1(umic.getDatosGenerales().getSegmento1());
				provcoaseg.setTabla1(umic.getBti().getTabla1Aseg1());
				provcoaseg.setTiposubriesgo(umic.getDatosGenerales().getTipoSubriesgo());
				provcoaseg.setDistint(umic.getDatosCoaseguro().getDistint());
				provcoaseg.setPcoase(umic.getDatosCoaseguro().getPcoaseg());
				provcoaseg.setCurvati(totales.getCurvati());
				provcoaseg.setTotflujoactcongastos(BigDecimal.ZERO);
				provcoaseg.setTotflujoactsingastos(BigDecimal.ZERO);
				provcoaseg.setIpc(servicioConfiguracion.recuperarIpcFuturo(detallesCorriente.get(i).getFcierre(), detallesCorriente.get(i).getBt()));
				if (swcasados != null && ccartera != null) {
					for (int j = 0; j < swcasados.size(); j++) {
						if (swcasados.get(j) != null && swcasados.get(j).equals("S")) {

							NamedCache baseTecnicaInicial = CacheFactory.getCache("baseTecnicaInicial");

							ValueExtractor[] fecIniFinExtractor = new ValueExtractor[] {
									// FECINITRAMOX
									new PofExtractor(Timestamp.class, 6 + j),
									// FECFINTRAMOX
									new PofExtractor(Timestamp.class, 1 + j) };
							ValueExtractor multiExtractor = new MultiExtractor(fecIniFinExtractor);

							List<Timestamp> fecTramo = (List<Timestamp>) baseTecnicaInicial.invoke(umic.getKey(),
									new ExtractorProcessor(multiExtractor));

							String varCriterFec = (String) servicioConfiguracion.recuperarDefinicionAuxiliar(ccartera,
									umic.getKey().getKmodalidad(), umic.getKey().getKgarantia(), detallesCorriente.get(i).getBt(), "ID-TEMPORAL");

							if (fecTramo.size() >= 2 && fecTramo.get(0) != null
									&& fecTramo.get(1) != null)
								provcoaseg.setDurtrcasado(nAnnos(fecTramo.get(0),
										fecTramo.get(1), varCriterFec));
						}
					}
				} else {

					Exception e = new Solvencia2Excepcion("No se ha encontrado la cartera o el swcasado", new Incidencia());
					e.printStackTrace();
				}
			}
			provcoasegg.add(provcoaseg);
			
		}
		almacenarEntregableFlujTcas(flujtcass);
		almacenarEntregableFlujCoaSeg(flujcoaseg);
		almacenarEntregableProvCoaSeg(provcoasegg);
	}
	
	public static BigDecimal nAnnos(final Timestamp fecha1, final Timestamp fecha2, final String criterioFecha) {
		// Variables locales
		BigDecimal numeroAnnos = BigDecimal.ZERO;
		int dias1 = 0;
		int dias2 = 0;
		// Fin variables locales

		// FIN VALIDACION CAMPO CRITERIO FECHA
	    final int [] ARRAY_BASE_365 = { 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334 };
	    final int [] ARRAY_BASE_360 = { 0, 30, 60, 90, 120, 150, 180, 210, 240, 270, 300, 330 };
	    final MathContext MATH_CONTEXT = new MathContext(34, RoundingMode.HALF_DOWN);
	    final BigDecimal CTE_OPER_1ENTRE12 = BigDecimal.ONE.divide(new BigDecimal(12), MATH_CONTEXT);
		final LocalDateTime fecha1u = new LocalDateTime(fecha1);
		final LocalDateTime fecha2u = new LocalDateTime(fecha2);

		if ("01".equals(criterioFecha)) {
			//Dias1 = anio(fecha1)*365 + ddenero(fecha1, CriterioFecha) + * dia(fecha1)
			dias1 = fecha1u.getYear() * 365 + ARRAY_BASE_365[fecha1u.getMonthOfYear() - 1] + fecha1u.getDayOfYear();

			//Dias2 = anio(fecha2)*365 + ddenero(fecha2, CriterioFecha) + dia(fecha2)
			dias2 = fecha2u.getYear() * 365 + ARRAY_BASE_365[fecha2u.getMonthOfYear() - 1] + fecha2u.getDayOfYear();

			// numAnnos = (dias2 - dias1) /365 
			//			numeroAnnos = BigDecimal.valueOf(dias2 - dias1).divide(ConstantsFunciones.CTE_OPER_365, ConstantsFunciones.MATH_CONTEXT).abs();
			int diferencia = dias2 - dias1;
			if(diferencia < 0)
				diferencia = -diferencia;
			numeroAnnos = new BigDecimal(String.valueOf(diferencia / 365d)); 
		} else if ("02".equals(criterioFecha)) {
			//Dias1 = anio(fecha1)*360 + ddenero(fecha1, CriterioFecha) + dia(fecha1)
			dias1 = fecha1u.getYear() * 360 + ARRAY_BASE_360[fecha1u.getMonthOfYear() - 1] + fecha1u.getDayOfYear();

			//Dias2 = anio(fecha2)*360 + ddenero(fecha2, CriterioFecha) + dia(fecha2)
			dias2 = fecha2u.getYear() * 360 + ARRAY_BASE_360[fecha2u.getMonthOfYear() - 1] + fecha2u.getDayOfYear();

			// numAnnos = (dias2 - dias1) /360
			numeroAnnos = BigDecimal.valueOf(dias2 - dias1).divide(new BigDecimal(360), MATH_CONTEXT).abs(); 
		} else if ("03".equals(criterioFecha)) {

			// (Anio(fecha2) - Anio(fecha1))
			final BigDecimal operador1 = BigDecimal.valueOf(fecha2u.getYear() - fecha1u.getYear());

			// Si dia(fecha1) <= dia(fecha2), entonces:
			if (fecha1u.getDayOfYear() <= fecha2u.getDayOfYear()) {

				// ((Mes(fecha2) - Mes(fecha1)) / 12)
				final BigDecimal operador2 = BigDecimal.valueOf(fecha2u.getMonthOfYear() - fecha1u.getMonthOfYear()).multiply(CTE_OPER_1ENTRE12, MATH_CONTEXT);
				//numeroAnnos = (Anio(fecha2) - Anio(fecha1)) + ((Mes(fecha2) - Mes(fecha1)) / 12)
				numeroAnnos = operador1.add(operador2);

			} else {

				// ((Mes(fecha2) - Mes(fecha1) - 1) / 12)
				final BigDecimal operador2 = BigDecimal.valueOf(fecha2u.getMonthOfYear() - fecha1u.getMonthOfYear() - 1).multiply(CTE_OPER_1ENTRE12, MATH_CONTEXT);
				// Si dia(fecha1) > dia(fecha2), entonces
				//numeroAnnos = (Anio(fecha2) - Anio(fecha1)) + ((Mes(fecha2) - Mes(fecha1) - 1) / 12)
				numeroAnnos = operador1.add(operador2);
			}
		}

		return numeroAnnos;
	}
	
	private BigDecimal getIntDGS() {

		String string_date = "99991231";
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
		Date d = null;
		try {
			d = f.parse(string_date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Timestamp timestamp = new Timestamp(d.getTime());

		InteresTecnicoDao dao = (InteresTecnicoDao) FactoriaDao.getDao("ITR0");

		InteresTecnico interesTecnico = dao.obtenerInteresTencico(timestamp).get(0);

		return interesTecnico.getPitref();
	}
	private BigDecimal getIntDGSCoaseg() {

		String string_date = "99991231";
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
		Date d = null;
		try {
			d = f.parse(string_date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Timestamp timestamp = new Timestamp(d.getTime());

		InteresTecnicoDao dao = (InteresTecnicoDao) FactoriaDao.getDao("ITR0");

		InteresTecnico interesTecnico = dao.obtenerInteresTencicoCoaseguro(timestamp).get(0);

		return interesTecnico.getPitref();
	}
	private Boolean calcularFlujoProbable(List<Timestamp> fecinis, List<Timestamp> fecfins, List<String> swcasados,
			Timestamp fecdesde, Timestamp finit) {
		Boolean result = false;

		for (int i = 0; i < fecinis.size(); i++) {
			if (fecinis.get(i) != null && fecfins.get(i) != null && swcasados.get(i) != null) {
				if ((fecdesde.after(fecinis.get(i)) || fecdesde.equals(fecinis.get(i)))
						&& (fecdesde.before(fecfins.get(i)) || fecdesde.equals(fecfins.get(i)))
						&& swcasados.get(i).contentEquals("N")
						&& fecdesde.after(finit)) {
					result = true;
				}
			}
		}
		return result;
	}
	
	@Override
	public void almacenarEntregablesTirea(Umic umic, DetalleBaseTecnica detalleBaseTecnica,
			List<DetalleCorriente> detallesCorriente, FichaProceso fichaproceso, TotalesFlujos totales) {
		TotPMaCoa totpmacoa = new TotPMaCoa();
		FlujPMaCoa flujpmacoa = new FlujPMaCoa();
		FlujPMdCoa flujpmdcoa = new FlujPMdCoa();
		List<TotPMaCoa> totpmacoas = new ArrayList<TotPMaCoa>();
		List<FlujPMaCoa> flujpmacoas = new ArrayList<FlujPMaCoa>();
		List<FlujPMdCoa> flujpmdcoas = new ArrayList<FlujPMdCoa>();
		final IObtenerConfiguracion servicioConfiguracion = FachadaServicios.getObtenerConfiguracion();
		for (int i=0; i < detallesCorriente.size();i++) {
//			UmicKey umickey = new UmicKey(detallesCorriente.get(0).getCtipoaport(),
//					detallesCorriente.get(0).getKajuste(), detallesCorriente.get(0).getKcertificado(),
//					detallesCorriente.get(0).getKgarantia(), detallesCorriente.get(0).getKmodalidad(),
//					detallesCorriente.get(0).getKpoliza(), detallesCorriente.get(0).getKprestacion(),
//					detallesCorriente.get(0).getKsubpoliza(), detallesCorriente.get(0).getNorden(),
//					detallesCorriente.get(0).getNsuscri());
//			DetalleCorrienteEntregables detallecorrienteEntregables = new DetalleCorrienteEntregables();
			Timestamp fdesde = detallesCorriente.get(i).getFechaDesde();
			LocalDateTime dt = new LocalDateTime(fdesde.getTime());
			int dia = dt.getDayOfMonth();
			//detallecorrienteEntregables.setDia(dia);
			fdesde = new Timestamp(dt.minusDays(dia - 1).toDateTime().getMillis());
//			
//			detallecorrienteEntregables.setBt(detallesCorriente.get(i).getBt());
//			detallecorrienteEntregables.setFcierre(detallesCorriente.get(i).getFcierre());
//			detallecorrienteEntregables.setUmicKey(umickey);
//			detallecorrienteEntregables.setFechadesde(fdesde);
//			detallecorrienteEntregables = detalleCorrienteEntregablesDao.get(detallecorrienteEntregables.getKey());
			// Entregable TOTPMACOA
			
			//UNA LLAMADA
			
			if(i == 0){
					totpmacoa = servicioConfiguracion.recuperarTotPMaCoa(detallesCorriente.get(i).getKpoliza(), detallesCorriente.get(i).getKsubpoliza(), 
						detallesCorriente.get(i).getNsuscri(), detallesCorriente.get(i).getBt(), 
						detallesCorriente.get(i).getFcierre());
				
				
				if (totpmacoa == null) {
					totpmacoa = new TotPMaCoa();
				}
	//			
	//			if (totpmacoa != null && totpmacoa.getBt() != null) {
	//				//totpmacoa.setTotfactgto(totpmacoa.getTotfactgto().add(detallecorrienteEntregables.getBloquegtoImpflujoactualizado()));
	//				//totpmacoa.setTotprovision(totpmacoa.getTotprovision().add(detallecorrienteEntregables.getSumprovision())); 
	//				//totpmacoa.setTotprovision(totpmacoa.getTotprovision().add(detallesCorriente.get(0).getTotalFlujoProyeccion().getProvbtiproy()));
	//			} else {
	//				
					totpmacoa.setKpoliza(umic.getDatosGenerales().getKpoliza());
					totpmacoa.setKsubpoliza(umic.getDatosGenerales().getKsubpoliza());
					totpmacoa.setNsuscri(umic.getDatosGenerales().getNsuscri());
					totpmacoa.setFcierre(detallesCorriente.get(i).getFcierre());
					DatosAdicionalesCoaseguro datosAdicionalesCoaseguro = datosAdicionales.get(new DatosAdicionalesCoaseguroKey(umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza()));
					if(datosAdicionalesCoaseguro != null) {
						totpmacoa.setKcoase1(datosAdicionalesCoaseguro.getKcoase1());
						totpmacoa.setKcoase2(datosAdicionalesCoaseguro.getKcoase2());
						totpmacoa.setKcoase3(datosAdicionalesCoaseguro.getKcoase3());
						totpmacoa.setKcoase4(datosAdicionalesCoaseguro.getKcoase4());
						totpmacoa.setKcoase5(datosAdicionalesCoaseguro.getKcoase5());
						totpmacoa.setKcoase6(datosAdicionalesCoaseguro.getKcoase6());
					}
					totpmacoa.setFecefecini(umic.getFechas().getFecefecini());
					totpmacoa.setFecefecfin(umic.getFechas().getFecefecfin());
					totpmacoa.setBt(detallesCorriente.get(i).getBt());
					//totpmacoa.setTotprovision(detallecorrienteEntregables.getSumprovision());
					//totpmacoa.setTotprovision(detallecorrienteEntregables.getTotprovision());
					totpmacoa.setFecIniTramo1(umic.getBti().getFecIniTramo1());
					totpmacoa.setFecFinTramo1(umic.getBti().getFecFinTramo1());
					totpmacoa.setPintertecnI1(umic.getBti().getPintertecnI1());
					totpmacoa.setFecIniTramo2(umic.getBti().getFecIniTramo2());
					totpmacoa.setFecFinTramo2(umic.getBti().getFecFinTramo2());
					totpmacoa.setPintertecnI2(umic.getBti().getPintertecnI2());
					totpmacoa.setFecIniTramo3(umic.getBti().getFecIniTramo3());
					totpmacoa.setFecFinTramo3(umic.getBti().getFecFinTramo3());
					totpmacoa.setPintertecnI3(umic.getBti().getPintertecnI3());
					totpmacoa.setFecIniTramo4(umic.getBti().getFecIniTramo4());
					totpmacoa.setFecFinTramo4(umic.getBti().getFecFinTramo4());
					totpmacoa.setPintertecnI4(umic.getBti().getPintertecnI4());
					totpmacoa.setFecIniTramo5(umic.getBti().getFecIniTramo5());
					totpmacoa.setFecFinTramo5(umic.getBti().getFecFinTramo5());
					totpmacoa.setPintertecnI5(umic.getBti().getPintertecnI5());
					totpmacoa.setPgastgesin1I(umic.getBti().getPgastgesin1I());
					totpmacoa.setPgastgesin2I(umic.getBti().getPgastgesin2I());
					totpmacoa.setPgastgesex1I(umic.getBti().getPgastgesex1I());
					totpmacoa.setPgastgesex2I(umic.getBti().getPgastgesex2I());
					//totpmacoa.setFactor1(totales.getFactor1());
					totpmacoa.setFactor1(BigDecimal.valueOf(100.00));
					totpmacoa.setTablacalc1aseg1(detalleBaseTecnica.getTablacalc1aseg1());
					//totpmacoa.setTotfactgto(detallecorrienteEntregables.getBloquegtoImpflujoactualizado());
				//}
				//totpmacoas.clear();
				totpmacoas.add(totpmacoa);
			}
			// Entregable FLUJPMACOA
			
			flujpmacoa = servicioConfiguracion.recuperarFlujPMaCoa(detallesCorriente.get(i).getKpoliza(), detallesCorriente.get(i).getKsubpoliza(), 
					detallesCorriente.get(i).getNsuscri(), detallesCorriente.get(i).getBt(), 
					detallesCorriente.get(i).getFcierre(), fdesde);
			if (flujpmacoa == null) {
				flujpmacoa = new FlujPMaCoa();
			}
//			
//			if (flujpmacoa != null && flujpmacoa.getBt() != null) {
//				//flujpmacoa.setTotflujoprobdegastos(flujpmacoa.getTotflujoprobdegastos().add(detallecorrienteEntregables.getBloquegtoImpflujoprobable()));
//				//flujpmacoa.setTotflujoprobsingastos(flujpmacoa.getTotflujoprobsingastos().add(detallecorrienteEntregables.getSumfprob().subtract(detallecorrienteEntregables.getBloquegtoImpflujoprobable())));
//			} else {
//				
				flujpmacoa.setKpoliza(umic.getDatosGenerales().getKpoliza());
				flujpmacoa.setKsubpoliza(umic.getDatosGenerales().getKsubpoliza());
				flujpmacoa.setNsuscri(umic.getDatosGenerales().getNsuscri());
				flujpmacoa.setFcierre(detallesCorriente.get(i).getFcierre());
				//flujpmacoa.setFdesde(detallesCorriente.get(i).getFechaDesde());
				flujpmacoa.setFdesde(fdesde);
				DatosAdicionalesCoaseguro datosAdicionalesCoaseguroPMA = datosAdicionales.get(new DatosAdicionalesCoaseguroKey(umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza()));
				if(datosAdicionalesCoaseguroPMA != null) {
					flujpmacoa.setKcoase1(datosAdicionalesCoaseguroPMA.getKcoase1());
					flujpmacoa.setKcoase2(datosAdicionalesCoaseguroPMA.getKcoase2());
					flujpmacoa.setKcoase3(datosAdicionalesCoaseguroPMA.getKcoase3());
					flujpmacoa.setKcoase4(datosAdicionalesCoaseguroPMA.getKcoase4());
					flujpmacoa.setKcoase5(datosAdicionalesCoaseguroPMA.getKcoase5());
					flujpmacoa.setKcoase6(datosAdicionalesCoaseguroPMA.getKcoase6());
				}
				flujpmacoa.setFecefecini(umic.getFechas().getFecefecini());
				flujpmacoa.setBt(detallesCorriente.get(i).getBt());
				//flujpmacoa.setTotflujoprobdegastos(detallecorrienteEntregables.getBloquegtoImpflujoprobable());
				//flujpmacoa.setTotflujoprobsingastos(detallecorrienteEntregables.getSumfprob().subtract(detallecorrienteEntregables.getBloquegtoImpflujoprobable()));
				flujpmacoa.setFecIniTramo1(umic.getBti().getFecIniTramo1());
				flujpmacoa.setFecFinTramo1(umic.getBti().getFecFinTramo1());
				flujpmacoa.setPintertecnI1(umic.getBti().getPintertecnI1());
				flujpmacoa.setFecIniTramo2(umic.getBti().getFecIniTramo2());
				flujpmacoa.setFecFinTramo2(umic.getBti().getFecFinTramo2());
				flujpmacoa.setPintertecnI2(umic.getBti().getPintertecnI2());
				flujpmacoa.setFecIniTramo3(umic.getBti().getFecIniTramo3());
				flujpmacoa.setFecFinTramo3(umic.getBti().getFecFinTramo3());
				flujpmacoa.setPintertecnI3(umic.getBti().getPintertecnI3());
				flujpmacoa.setFecIniTramo4(umic.getBti().getFecIniTramo4());
				flujpmacoa.setFecFinTramo4(umic.getBti().getFecFinTramo4());
				flujpmacoa.setPintertecnI4(umic.getBti().getPintertecnI4());
				flujpmacoa.setFecIniTramo5(umic.getBti().getFecIniTramo5());
				flujpmacoa.setFecFinTramo5(umic.getBti().getFecFinTramo5());
				flujpmacoa.setPintertecnI5(umic.getBti().getPintertecnI5());
			//}
			flujpmacoas.add(flujpmacoa);

			// Entregable FLUJPMDCOA
			flujpmdcoa = servicioConfiguracion.recuperarFlujPMdCoa(detallesCorriente.get(i).getKmodalidad(), 
								detallesCorriente.get(i).getKgarantia(), detallesCorriente.get(i).getKprestacion(), 
								detallesCorriente.get(i).getKpoliza(), detallesCorriente.get(i).getKsubpoliza(), 
								detallesCorriente.get(i).getKcertificado(), detallesCorriente.get(i).getNsuscri(), 
								detallesCorriente.get(i).getBt(), detallesCorriente.get(i).getFcierre());
			if (flujpmdcoa == null) {
				flujpmdcoa = new FlujPMdCoa();
			}
						
//			if (flujpmdcoa != null && flujpmdcoa.getBt() != null) {
//				//flujpmdcoa.setTotprovision(flujpmdcoa.getTotprovision().add(detallecorrienteEntregables.getSumprovision())); 
//			} else {
				flujpmdcoa.setKmodalidad(umic.getDatosGenerales().getKmodalidad());
				flujpmdcoa.setKgarantia(umic.getDatosGenerales().getKgarantia());
				flujpmdcoa.setKprestacion(umic.getDatosGenerales().getKprestacion());
				flujpmdcoa.setKpoliza(umic.getDatosGenerales().getKpoliza());
				flujpmdcoa.setKsubpoliza(umic.getDatosGenerales().getKsubpoliza());
				flujpmdcoa.setKcertificado(umic.getDatosGenerales().getKcertificado());
				flujpmdcoa.setNsuscri(umic.getDatosGenerales().getNsuscri());
				flujpmdcoa.setFcierre(detallesCorriente.get(i).getFcierre());
				DatosAdicionalesCoaseguro datosAdicionalesCoaseguroPmd = datosAdicionales.get(new DatosAdicionalesCoaseguroKey(umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza()));
				if(datosAdicionalesCoaseguroPmd != null) {
					flujpmdcoa.setKcoase1(datosAdicionalesCoaseguroPmd.getKcoase1());
					flujpmdcoa.setKcoase2(datosAdicionalesCoaseguroPmd.getKcoase2());
					flujpmdcoa.setKcoase3(datosAdicionalesCoaseguroPmd.getKcoase3());
					flujpmdcoa.setKcoase4(datosAdicionalesCoaseguroPmd.getKcoase4());
					flujpmdcoa.setKcoase5(datosAdicionalesCoaseguroPmd.getKcoase5());
					flujpmdcoa.setKcoase6(datosAdicionalesCoaseguroPmd.getKcoase6());
				}
				flujpmdcoa.setFecefecini(umic.getFechas().getFecefecini());
				flujpmdcoa.setFecefecfin(umic.getFechas().getFecefecfin());
				flujpmdcoa.setBt(detallesCorriente.get(i).getBt());
				//flujpmdcoa.setTotprovision(detallecorrienteEntregables.getSumprovision());
				//flujpmdcoa.setItcalc(totales.getItcalc().get(0));
				flujpmdcoa.setTablacalc1aseg1(detalleBaseTecnica.getTablacalc1aseg1());
				flujpmdcoa.setFecIniTramo1(umic.getBti().getFecIniTramo1());
				flujpmdcoa.setFecFinTramo1(umic.getBti().getFecFinTramo1());
				flujpmdcoa.setPintertecnI1(umic.getBti().getPintertecnI1());
				flujpmdcoa.setFecIniTramo2(umic.getBti().getFecIniTramo2());
				flujpmdcoa.setFecFinTramo2(umic.getBti().getFecFinTramo2());
				flujpmdcoa.setPintertecnI2(umic.getBti().getPintertecnI2());
				flujpmdcoa.setFecIniTramo3(umic.getBti().getFecIniTramo3());
				flujpmdcoa.setFecFinTramo3(umic.getBti().getFecFinTramo3());
				flujpmdcoa.setPintertecnI3(umic.getBti().getPintertecnI3());
				flujpmdcoa.setFecIniTramo4(umic.getBti().getFecIniTramo4());
				flujpmdcoa.setFecFinTramo4(umic.getBti().getFecFinTramo4());
				flujpmdcoa.setPintertecnI4(umic.getBti().getPintertecnI4());
				flujpmdcoa.setFecIniTramo5(umic.getBti().getFecIniTramo5());
				flujpmdcoa.setFecFinTramo5(umic.getBti().getFecFinTramo5());
				flujpmdcoa.setPintertecnI5(umic.getBti().getPintertecnI5());
				flujpmdcoa.setPgastgesin1I(umic.getBti().getPgastgesin1I());
				flujpmdcoa.setPgastgesin2I(umic.getBti().getPgastgesin2I());
				flujpmdcoa.setPgastgesin3I(umic.getBti().getPgastgesin3I());
				//flujpmdcoa.setItcalc(totales.getItcalc().get(0));
				flujpmdcoa.setTablacalc1aseg1(detalleBaseTecnica.getTablacalc1aseg1());
				flujpmdcoa.setGtorosspCap(detalleBaseTecnica.getGtorosspCap());
				flujpmdcoa.setGtorosspPrima(detalleBaseTecnica.getGtorosspPrima());
				flujpmdcoa.setGtorosspProv(detalleBaseTecnica.getGtorosspProv());
				flujpmdcoa.setFnacAseg1(umic.getAsegurados().getFnacAseg1());
				flujpmdcoa.setFnacAseg2(umic.getAsegurados().getFnacAseg2());
				flujpmdcoa.setFnacAseg3(umic.getAsegurados().getFnacAseg3());
				flujpmdcoa.setFnacAseg4(umic.getAsegurados().getFnacAseg4());
				flujpmdcoa.setFnacAseg5(umic.getAsegurados().getFnacAseg5());
				flujpmdcoa.setCsexAseg1(umic.getAsegurados().getCsexAseg1());
				flujpmdcoa.setCsexAseg2(umic.getAsegurados().getCsexAseg2());
				flujpmdcoa.setCsexAseg3(umic.getAsegurados().getCsexAseg3());
				flujpmdcoa.setCsexAseg4(umic.getAsegurados().getCsexAseg4());
				flujpmdcoa.setCsexAseg5(umic.getAsegurados().getCsexAseg5());
				flujpmdcoa.setCestadoAseg1(umic.getOtrosDatos().getCestadoAseg1());
				flujpmdcoa.setCestadoAseg2(umic.getOtrosDatos().getCestadoAseg2());
				flujpmdcoa.setCestadoAseg3(umic.getOtrosDatos().getCestadoAseg3());
				flujpmdcoa.setCestadoAseg4(umic.getOtrosDatos().getCestadoAseg4());
				flujpmdcoa.setCestadoAseg5(umic.getOtrosDatos().getCestadoAseg5());
				flujpmdcoa.setIprimanetaini(umic.getPrimas().getIprimanetaini());
				flujpmdcoa.setIprimanetaact(umic.getPrimas().getIprimanetaact());
				flujpmdcoa.setIprimatarada(umic.getPrimas().getIprimatarada());
				flujpmdcoa.setIcapini(umic.getCapitales().getIcapini());
				flujpmdcoa.setIcapact(umic.getCapitales().getIcapact());
				flujpmdcoa.setIsaldo(umic.getCapitales().getIsaldo());
				flujpmdcoa.setFecIni(umic.getRentas().getFecIni());
				if (umic.getRentas().getFecFin() == null) {
					Timestamp fecFin = new Timestamp(new LocalDate(9999, 12, 31).toDate().getTime());
					flujpmdcoa.setFecFin(fecFin);
				} else {
					flujpmdcoa.setFecFin(umic.getRentas().getFecFin());
				}
				flujpmdcoa.setTempVit(umic.getRentas().getTempVit());
				flujpmdcoa.setPrevrenta(umic.getRentas().getPrevrenta());
				flujpmdcoa.setPreversion(umic.getRentas().getPreversion());
				flujpmdcoa.setNpergaran(umic.getRentas().getNpergaran());
				flujpmdcoa.setNadifer(umic.getRentas().getNadifer());
				flujpmdcoa.setForpagrent(umic.getRentas().getForpagrent());
				flujpmdcoa.setCpagrenta(umic.getRentas().getCpagrenta());
				flujpmdcoa.setCtipoRevrenta(umic.getRentas().getCtipoRevrenta());
				flujpmdcoa.setCformaRevrenta(umic.getRentas().getCformaRevrenta());
				flujpmdcoa.setNdurrenta(umic.getRentas().getNdurrenta());
				flujpmdcoa.setKajuste(umic.getDatosGenerales().getKajuste());
				flujpmdcoa.setPsobremort(umic.getBti().getPsobremort());
				flujpmdcoa.setPriesgo(umic.getBti().getPriesgo());
				String gvalor = (String)servicioConfiguracion.recuperarDefinicionAuxiliar(umic.getDatosGenerales().getCcartera(), 
						umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), 
						detalleBaseTecnica.getBaseTec(), "ID-CRITERIO");
				flujpmdcoa.setGvalor(gvalor.substring(0,1));
				flujpmdcoa.setFdiadepago(umic.getPrimas().getFdiadepago());
				flujpmdcoa.setFecinisus(umic.getFechas().getFecinisus());
				flujpmdcoa.setRentini(umic.getRentas().getRentini());
				flujpmdcoa.setPgastgesex1I(umic.getBti().getPgastgesex1I());
				flujpmdcoa.setPgastgesex2I(umic.getBti().getPgastgesex2I());
				flujpmdcoa.setCformarevprim(umic.getPrimas().getCformarevprim());
				flujpmdcoa.setPrevprima(umic.getPrimas().getPrevprima());
				flujpmdcoa.setCformpago(umic.getPrimas().getCformpago());
				flujpmdcoa.setFecinipagprim(umic.getFechas().getFecinipagprim());
				flujpmdcoa.setFecfinpagprim(umic.getFechas().getFecfinpagprim());
				flujpmdcoa.setGedadmax(umic.getAsegurados().getGedadMax());
				flujpmdcoa.setPb(umic.getDatosGenerales().getPb());
				flujpmdcoa.setTipopb(umic.getDatosGenerales().getTipoPb());
			//}

			flujpmdcoas.add(flujpmdcoa);
		}
		
		almacenarEntregableTotPMaCoa(totpmacoas);
		almacenarEntregableFlujPMaCoa(flujpmacoas);
		almacenarEntregableFlujPMdCoa(flujpmdcoas);
	}
	
	@Override
	public void almacenarIncidenciasMaestro(Umic umic, String error) {
		IncidenciasMaestro incidencia = new IncidenciasMaestro();
		String[] aux;
		String aux2;
		String cadena = new String();
		Map<String, String> mapa = new HashMap<String, String>();
		mapa.put("Type conversion error: Invalid date", "Formato de fecha no valido");
		mapa.put("Type conversion error: Number value '33S' does not match pattern '00000'", "Formato de entero no valido");
		mapa.put("Type conversion error: Number value '+S00' does not match pattern '+0000;-0000'", "Formato de numero con decimales no valido");
		mapa.put("Number value", "Formato de numero no valido");
		mapa.put("Unparseable number: S", "Formato de numero con decimales no valido");
		mapa.put("Integer value", "Formato de numero entero no valido");
		aux2 = error.replace("[", "");
		aux2 = aux2.replace("]", "");
		aux2 = aux2.replace("Invalid 'umic' record at line", "Umic en la linea");
		aux2 = aux2.replace("==>", "del fichero maestro no es posible cargarse  ==>");
		aux2 = aux2.replace("==> Invalid", "==> Campo no valido");
		aux2 = aux2.replace("\n", "");
		aux2 = aux2.replace("Type conversion error: java.text.ParseException:", "");
		aux = aux2.split("org.beanio.InvalidRecordException:");
		
		for (int i=1; i<aux.length; i++) {
			final int position = aux[i].indexOf(":");
			String cadAux1 = aux[i].substring(position+3);
			
			String cAux = aux[i].substring(aux[i].indexOf("Type"));
			if (cAux.contains("Number value")) {
				cadAux1 = "Number value";
			} else if (cAux.contains("Invalid date")) {
				cadAux1 = "Type conversion error: Invalid date";
			} else if(cAux.contains("Unparseable number")) {
				cadAux1 = "Unparseable number: S";
			} else if(cAux.contains("Integer value")) {
				cadAux1 = "Integer value";
			}
			String cadAux2 = mapa.get(cadAux1.replace(", ", ""));
			if (cadAux2 == null) {
				cadAux2 = cadAux1;
			}
			
			cadena = cadena.concat(aux[i].replace(cAux, cadAux2)).concat("\n");
			
			//cadena = cadena.replace(cadAux1.replace(", ", ""), cadAux2);
		}
		incidencia.setTextoError(cadena);
		
		incidenciasMaestroDao.put(incidencia.getKey(), incidencia);
	}
	
	@Override
	public void almacenarEntregableCONTEOCERTIFICADO(List<ConteoCertificado> conteo) {
		
		if (conteo != null && !conteo.isEmpty()) {
			Map<ConteoCertificadoKey, ConteoCertificado> mapa = new HashMap<ConteoCertificadoKey, ConteoCertificado>();
			
			for (ConteoCertificado entry : conteo) {
				mapa.put(entry.getKey(), entry);
			}
			conteoCert.putAll(mapa);
		}
	}

	@Override
	public void almacenarEntregableFlujInfSCR(List<FlujInfSCR> flujinfscrs) {
		if (flujinfscrs != null && !flujinfscrs.isEmpty()) {
			Map<FlujInfSCRKey, FlujInfSCR> mapa = new HashMap<FlujInfSCRKey, FlujInfSCR>();
			for (FlujInfSCR entry : flujinfscrs) {
				mapa.put(entry.getKey(), entry);
			}
			flujInfSCRDao.putAll(mapa);
		}
	}
	
	@Override
	public void almacenarEntregablesFlujInfSCR(Umic umic, DetalleBaseTecnica detalleBaseTecnica,
			List<DetalleCorriente> detallesCorriente, List<DetalleCorriente> detallesCorrienteBEL) {
		FlujInfSCR flujos = new FlujInfSCR();
		List<FlujInfSCR> flujinfscr = new ArrayList<FlujInfSCR>();
		final IObtenerConfiguracion servicioConfiguracion = FachadaServicios.getObtenerConfiguracion();	
		BigDecimal sumTotNoAnulSCR = BigDecimal.ZERO;
		BigDecimal sumTotNoAnulBEL = BigDecimal.ZERO;
		for (int i=0; i < detallesCorriente.size();i++) {
			
			//Generar fecha flujo estimado
			Timestamp fdesde_fpsl = detallesCorriente.get(i).getFechaDesde();
			LocalDateTime dt_fpsl = new LocalDateTime(fdesde_fpsl.getTime());
			int mes_fpsl_fdesde = dt_fpsl.getMonthOfYear();
			int anyo_fpsl_fdesde = dt_fpsl.getYear();
			int dia_fproyflujest = 1;
			
			if(umic.getDatosNiif17().getFproyflujest() != null &&umic.getDatosNiif17().getFproyflujest().equals("I")){
				dia_fproyflujest = 1;
			}else if(umic.getDatosNiif17().getFproyflujest() != null && umic.getDatosNiif17().getFproyflujest().equals("M")){
				dia_fproyflujest=15;
				if(mes_fpsl_fdesde == 2){
					dia_fproyflujest=14;
				}
			}else if(umic.getDatosNiif17().getFproyflujest() != null && umic.getDatosNiif17().getFproyflujest().equals("F")){
				if(mes_fpsl_fdesde == 2){
					dia_fproyflujest=28;
				}else if(mes_fpsl_fdesde == 1 || 
						mes_fpsl_fdesde == 3 ||
						mes_fpsl_fdesde == 5 ||
						mes_fpsl_fdesde == 7 ||
						mes_fpsl_fdesde == 8 ||
						mes_fpsl_fdesde == 10 ||
						mes_fpsl_fdesde == 12){
					dia_fproyflujest=31;
				}else{
					dia_fproyflujest=30;
				}
			}else{
				//Saca error de que no viene ifnormado el campo correctamente o incidecnia infomrativa mejor.
				dia_fproyflujest=1;
			}
			
			//FECHA PROYECCION DEL FLUJO ESTIMADO
			String anyo = String.valueOf(anyo_fpsl_fdesde);
			
			String mes = String.valueOf(mes_fpsl_fdesde);
			if(mes.length() == 1){
				mes = "0" + mes;
			}
			String dia_fecha = String.valueOf(dia_fproyflujest);
			if(dia_fecha.length() == 1){
				dia_fecha = "0" + dia_fecha;
			}
			
			String fechaFproy = anyo + "/" + mes + "/" + dia_fecha;
			Timestamp fProy = null;
			SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy/MM/dd");
			try {
				Date fechaParseada = formatoFecha.parse(fechaFproy);
				fProy = new Timestamp(fechaParseada.getTime());
				
			} catch (ParseException e) {
				//Si el formato no es el esperado se lanza excepción.
				throw Solvencia2ExcepcionHelper.crearExcepcion("AP", new String[]{fechaFproy, "yyyy/MM/dd"});
			}
					
			flujos = servicioConfiguracion.recuperarFlujInfSCR(detallesCorriente.get(i).getBt(), detallesCorriente.get(i).getFcierre(), 
					detallesCorriente.get(i).getCcanal(), detallesCorriente.get(i).getCnegocio(), detallesCorriente.get(i).getUoa(), 
					umic.getDatosNiif17().getkcarcontacto(), umic.getDatosNiif17().getcohorte(), umic.getDatosNiif17().getswmodoner(), 
					fProy,  detallesCorriente.get(i).getKcarterainv(), detallesCorriente.get(i).getKmodalidad());
			
			if (flujos == null) {
				flujos = new FlujInfSCR();
				flujos.setBt(detallesCorriente.get(i).getBt());
				flujos.setFcierre(detallesCorriente.get(i).getFcierre());
				flujos.setNegocio(detallesCorriente.get(i).getCnegocio());
				flujos.setCcanal(detallesCorriente.get(i).getCcanal());
				flujos.setKcarterainv(detallesCorriente.get(i).getKcarterainv());
				flujos.setUoa(detallesCorriente.get(i).getUoa());
				flujos.setKcarteraContrato(umic.getDatosNiif17().getkcarcontacto());
				flujos.setKcarteraCohort(umic.getDatosNiif17().getcohorte());
				flujos.setKcarteraOner(umic.getDatosNiif17().getswmodoner());
				flujos.setKmodalidad(detallesCorriente.get(i).getKmodalidad());
				flujos.setFproyflujest(fProy);
				
//				flujos.setBt(detallesCorriente.get(i).getBt());
//				flujos.setFcierre(detallesCorriente.get(i).getFcierre());
//				flujos.setCcanal(detallesCorriente.get(i).getCcanal());
//				flujos.setNegocio(detallesCorriente.get(i).getCnegocio());
//				flujos.setKmodalidad(detallesCorriente.get(i).getKmodalidad());
//				flujos.setFproyflujest(fProy);
//				flujos.setKcarteraCohort(umic.getDatosNiif17().getcohorte());
//				flujos.setKcarteraContrato(umic.getDatosNiif17().getkcarcontacto());
//				flujos.setKcarterainv(detallesCorriente.get(i).getKcarterainv());
//				flujos.setKcarteraOner(umic.getDatosNiif17().getswmodoner());
//				flujos.setUoa(detallesCorriente.get(i).getUoa());
				
				flujos.setTotFp(detallesCorrienteBEL.get(i).getBloqueComi().getImpFlujoNoAnulado()
						.add(detallesCorrienteBEL.get(i).getBloqueCompl().getImpFlujoNoAnulado())
						.add(detallesCorrienteBEL.get(i).getBloqueFall().getImpFlujoNoAnulado())
						.add(detallesCorrienteBEL.get(i).getBloqueGto().getImpFlujoNoAnulado())
						.add(detallesCorrienteBEL.get(i).getBloqueGtoAd().getImpFlujoNoAnulado())
						.subtract(detallesCorrienteBEL.get(i).getBloquePrim().getImpFlujoNoAnulado())
						.add(detallesCorrienteBEL.get(i).getBloqueRte().getImpFlujoNoAnulado())
						.add(detallesCorrienteBEL.get(i).getBloqueVida().getImpFlujoNoAnulado()));
				
				flujos.setTotFpComisiones(detallesCorrienteBEL.get(i).getBloqueComi().getImpFlujoNoAnulado());
				flujos.setTotFpCompl(detallesCorrienteBEL.get(i).getBloqueCompl().getImpFlujoNoAnulado());
				flujos.setTotFpFall(detallesCorrienteBEL.get(i).getBloqueFall().getImpFlujoNoAnulado());
				flujos.setTotFpGastos(detallesCorrienteBEL.get(i).getBloqueGto().getImpFlujoNoAnulado());
				flujos.setTotFpGtoAd(detallesCorrienteBEL.get(i).getBloqueGtoAd().getImpFlujoNoAnulado());
				flujos.setTotFpPrimas(detallesCorrienteBEL.get(i).getBloquePrim().getImpFlujoNoAnulado());
				flujos.setTotFpRescates(detallesCorrienteBEL.get(i).getBloqueRte().getImpFlujoNoAnulado());
				flujos.setTotFpVida(detallesCorrienteBEL.get(i).getBloqueVida().getImpFlujoNoAnulado());
				
//				flujos.setTotFpSCR(detallesCorriente.get(i).getBloqueComi().getImpFlujoNoAnulado()
//						.add(detallesCorriente.get(i).getBloqueCompl().getImpFlujoNoAnulado())
//						.add(detallesCorriente.get(i).getBloqueFall().getImpFlujoNoAnulado())
//						.add(detallesCorriente.get(i).getBloqueGto().getImpFlujoNoAnulado())
//						.add(detallesCorriente.get(i).getBloqueGtoAd().getImpFlujoNoAnulado())
//						.subtract(detallesCorriente.get(i).getBloquePrim().getImpFlujoNoAnulado())
//						.add(detallesCorriente.get(i).getBloqueRte().getImpFlujoNoAnulado())
//						.add(detallesCorriente.get(i).getBloqueVida().getImpFlujoNoAnulado()));
//				
//				flujos.setTotFpComisionesSCR(detallesCorriente.get(i).getBloqueComi().getImpFlujoNoAnulado());
//				flujos.setTotFpComplSCR(detallesCorriente.get(i).getBloqueCompl().getImpFlujoNoAnulado());
//				flujos.setTotFpFallSCR(detallesCorriente.get(i).getBloqueFall().getImpFlujoNoAnulado());
//				flujos.setTotFpGastosSCR(detallesCorriente.get(i).getBloqueGto().getImpFlujoNoAnulado());
//				flujos.setTotFpGtoAdSCR(detallesCorriente.get(i).getBloqueGtoAd().getImpFlujoNoAnulado());
//				flujos.setTotFpPrimasSCR(detallesCorriente.get(i).getBloquePrim().getImpFlujoNoAnulado());
//				flujos.setTotFpRescatesSCR(detallesCorriente.get(i).getBloqueRte().getImpFlujoNoAnulado());
//				flujos.setTotFpVidaSCR(detallesCorriente.get(i).getBloqueVida().getImpFlujoNoAnulado());
					
			} else {
				flujos.setTotFp(flujos.getTotFp()
						.add(detallesCorrienteBEL.get(i).getBloqueComi().getImpFlujoNoAnulado()
								.add(detallesCorrienteBEL.get(i).getBloqueCompl().getImpFlujoNoAnulado())
								.add(detallesCorrienteBEL.get(i).getBloqueFall().getImpFlujoNoAnulado())
								.add(detallesCorrienteBEL.get(i).getBloqueGto().getImpFlujoNoAnulado())
								.add(detallesCorrienteBEL.get(i).getBloqueGtoAd().getImpFlujoNoAnulado())
								.subtract(detallesCorrienteBEL.get(i).getBloquePrim().getImpFlujoNoAnulado())
								.add(detallesCorrienteBEL.get(i).getBloqueRte().getImpFlujoNoAnulado())
								.add(detallesCorrienteBEL.get(i).getBloqueVida().getImpFlujoNoAnulado())));
	
				flujos.setTotFpComisiones(flujos.getTotFpComisiones()
						.add(detallesCorrienteBEL.get(i).getBloqueComi().getImpFlujoNoAnulado()));
				flujos.setTotFpCompl(flujos.getTotFpCompl()
						.add(detallesCorrienteBEL.get(i).getBloqueCompl().getImpFlujoNoAnulado()));
				flujos.setTotFpFall(flujos.getTotFpFall()
						.add(detallesCorrienteBEL.get(i).getBloqueFall().getImpFlujoNoAnulado()));
				flujos.setTotFpGastos(flujos.getTotFpGastos()
						.add(detallesCorrienteBEL.get(i).getBloqueGto().getImpFlujoNoAnulado()));
				flujos.setTotFpGtoAd(flujos.getTotFpGtoAd()
						.add(detallesCorrienteBEL.get(i).getBloqueGtoAd().getImpFlujoNoAnulado()));
				flujos.setTotFpPrimas(flujos.getTotFpPrimas()
						.add(detallesCorrienteBEL.get(i).getBloquePrim().getImpFlujoNoAnulado()));
				flujos.setTotFpRescates(flujos.getTotFpRescates()
						.add(detallesCorrienteBEL.get(i).getBloqueRte().getImpFlujoNoAnulado()));
				flujos.setTotFpVida(flujos.getTotFpVida()
						.add(detallesCorrienteBEL.get(i).getBloqueVida().getImpFlujoNoAnulado()));
				
//				flujos.setTotFpSCR(flujos.getTotFpSCR()
//						.add(detallesCorriente.get(i).getBloqueComi().getImpFlujoNoAnulado()
//								.add(detallesCorriente.get(i).getBloqueCompl().getImpFlujoNoAnulado())
//								.add(detallesCorriente.get(i).getBloqueFall().getImpFlujoNoAnulado())
//								.add(detallesCorriente.get(i).getBloqueGto().getImpFlujoNoAnulado())
//								.add(detallesCorriente.get(i).getBloqueGtoAd().getImpFlujoNoAnulado())
//								.subtract(detallesCorriente.get(i).getBloquePrim().getImpFlujoNoAnulado())
//								.add(detallesCorriente.get(i).getBloqueRte().getImpFlujoNoAnulado())
//								.add(detallesCorriente.get(i).getBloqueVida().getImpFlujoNoAnulado())));
//				
//				flujos.setTotFpComisionesSCR(flujos.getTotFpComisionesSCR()
//						.add(detallesCorriente.get(i).getBloqueComi().getImpFlujoNoAnulado()));
//				flujos.setTotFpComplSCR(flujos.getTotFpComplSCR()
//						.add(detallesCorriente.get(i).getBloqueCompl().getImpFlujoNoAnulado()));
//				flujos.setTotFpFallSCR(flujos.getTotFpFallSCR()
//						.add(detallesCorriente.get(i).getBloqueFall().getImpFlujoNoAnulado()));
//				flujos.setTotFpGastosSCR(flujos.getTotFpGastosSCR()
//						.add(detallesCorriente.get(i).getBloqueGto().getImpFlujoNoAnulado()));
//				flujos.setTotFpGtoAdSCR(flujos.getTotFpGtoAdSCR()
//						.add(detallesCorriente.get(i).getBloqueGtoAd().getImpFlujoNoAnulado()));
//				flujos.setTotFpPrimasSCR(flujos.getTotFpPrimasSCR()
//						.add(detallesCorriente.get(i).getBloquePrim().getImpFlujoNoAnulado()));
//				flujos.setTotFpRescatesSCR(flujos.getTotFpRescatesSCR()
//						.add(detallesCorriente.get(i).getBloqueRte().getImpFlujoNoAnulado()));
//				flujos.setTotFpVidaSCR(flujos.getTotFpVidaSCR()
//						.add(detallesCorriente.get(i).getBloqueVida().getImpFlujoNoAnulado()));
//				
			}
			
			flujInfSCRDao.put(flujos.getKey(), flujos);
//			flujinfscr.add(flujos);
//			almacenarEntregableFlujInfSCR(flujinfscr);
			
		}
		//almacenarEntregableFlujInfSCR(flujinfscr);
		//almacenarEntregableFlujInfSCR(flujinfscr);
	}
	
		@Override
	public void almacenarEntregableFlujSuscripciones(Umic umic, DetalleBaseTecnica detalleBaseTecnica,
			List<DetalleCorriente> detallesCorriente, FichaProceso fichaproceso, TotalesFlujos totales) {
		FlujSuscri flujos = new FlujSuscri();
		List<FlujSuscri> flujsuscri = new ArrayList<FlujSuscri>();

		final IObtenerConfiguracion servicioConfiguracion = FachadaServicios.getObtenerConfiguracion();	

		for (int i=0; i < detallesCorriente.size();i++) {
			
			// Fecha desde a primer día del mes
			
			Timestamp fdesde = detallesCorriente.get(i).getFechaDesde();
			LocalDateTime dt = new LocalDateTime(fdesde.getTime());
			int dia = dt.getDayOfMonth();
			fdesde = new Timestamp(dt.minusDays(dia - 1).toDateTime().getMillis());
						
			flujos = servicioConfiguracion.recuperarFlujSuscri(detallesCorriente.get(i).getBt(), detallesCorriente.get(i).getFcierre(), 
					detallesCorriente.get(i).getCnegocio(), detallesCorriente.get(i).getCcanal(), detallesCorriente.get(i).getKramo(), 
					detallesCorriente.get(i).getKmodalidad(), umic.getDatosGenerales().getSegmento1(), 
					umic.getDatosGenerales().getTipoSubriesgo(), detallesCorriente.get(i).getKpoliza(), detallesCorriente.get(i).getKsubpoliza(), 
					detallesCorriente.get(i).getNsuscri(), detallesCorriente.get(i).getKcarterainv(), detallesCorriente.get(i).getGapAct(), 
					fdesde, detallesCorriente.get(i).getKgarantia());
			if (flujos == null) {
				flujos = new FlujSuscri();
			}

			if (flujos != null && flujos.getBt() == null) {
				flujos.setCcanal(detallesCorriente.get(i).getCcanal());
				flujos.setCnegocio(detallesCorriente.get(i).getCnegocio());
				flujos.setFeccierre(detallesCorriente.get(i).getFcierre());
				flujos.setFecdesde(fdesde);
				flujos.setFinisusc(umic.getFechas().getFecinisus());
				flujos.setGapact(detallesCorriente.get(i).getGapAct());			
				flujos.setKcarterainv(detallesCorriente.get(i).getKcarterainv());
				flujos.setKmodalidad(detallesCorriente.get(i).getKmodalidad());
				flujos.setKmodext(detallesCorriente.get(i).getKmodext());
				flujos.setKpoliza(detallesCorriente.get(i).getKpoliza());
				flujos.setKramo(detallesCorriente.get(i).getKramo());
				flujos.setKsubpoliza(detallesCorriente.get(i).getKsubpoliza());
				flujos.setNsuscri(detallesCorriente.get(i).getNsuscri());
				flujos.setItcalc(detallesCorriente.get(i).getIntfeccal());
				flujos.setSegmento1(umic.getDatosGenerales().getSegmento1());
				flujos.setTiposubriesgo(umic.getDatosGenerales().getTipoSubriesgo());
				flujos.setKcarteraContrato(detallesCorriente.get(i).getKcontrato());
				flujos.setSwcasado(umic.getDatosGenerales().getSwcasado());
				flujos.setBt(detallesCorriente.get(i).getBt());
				flujos.setKgarantia(detallesCorriente.get(i).getKgarantia());
				
				flujos.setTotfpvida(detallesCorriente.get(i).getBloqueVida().getImpFlujoProbable());
				flujos.setTotfactvida(detallesCorriente.get(i).getBloqueVida().getImpFlujoActualizado());
				flujos.setTotcolavida(detallesCorriente.get(i).getBloqueVida().getImpProvi());
				flujos.setTotfpfall(detallesCorriente.get(i).getBloqueFall().getImpFlujoProbable());
				flujos.setTotfactfall(detallesCorriente.get(i).getBloqueFall().getImpFlujoActualizado());
				flujos.setTotcolafall(detallesCorriente.get(i).getBloqueFall().getImpProvi());
				flujos.setTotfpcompl(detallesCorriente.get(i).getBloqueCompl().getImpFlujoProbable());
				flujos.setTotfactcompl(detallesCorriente.get(i).getBloqueCompl().getImpFlujoActualizado());
				flujos.setTotcolacompl(detallesCorriente.get(i).getBloqueCompl().getImpProvi());
				flujos.setTotfprte(detallesCorriente.get(i).getBloqueRte().getImpFlujoProbable());
				flujos.setTotfactrte(detallesCorriente.get(i).getBloqueRte().getImpFlujoActualizado());
				flujos.setTotcolarte(detallesCorriente.get(i).getBloqueRte().getImpProvi());
				flujos.setTotfpgto(detallesCorriente.get(i).getBloqueGto().getImpFlujoProbable());
				flujos.setTotfactgto(detallesCorriente.get(i).getBloqueGto().getImpFlujoActualizado());
				flujos.setTotcolagto(detallesCorriente.get(i).getBloqueGto().getImpProvi());
				flujos.setTotfpcom(detallesCorriente.get(i).getBloqueComi().getImpFlujoProbable());
				flujos.setTotfactcom(detallesCorriente.get(i).getBloqueComi().getImpFlujoActualizado());
				flujos.setTotcolacom(detallesCorriente.get(i).getBloqueComi().getImpProvi());
				flujos.setTotfpgtoad(detallesCorriente.get(i).getBloqueGtoAd().getImpFlujoProbable());
				flujos.setTotfactgtoad(detallesCorriente.get(i).getBloqueGtoAd().getImpFlujoActualizado());
				flujos.setTotcolagtoad(detallesCorriente.get(i).getBloqueGtoAd().getImpProvi());
				flujos.setTotfpprim(detallesCorriente.get(i).getBloquePrim().getImpFlujoProbable());
				flujos.setTotfactprim(detallesCorriente.get(i).getBloquePrim().getImpFlujoActualizado());
				flujos.setTotcolaprim(detallesCorriente.get(i).getBloquePrim().getImpProvi());
				
				
				BigDecimal sumfpvida = detallesCorriente.get(i).getBloqueVida().getImpFlujoProbable();
				BigDecimal sumfpfall = detallesCorriente.get(i).getBloqueFall().getImpFlujoProbable();
				BigDecimal sumfpcompl = detallesCorriente.get(i).getBloqueCompl().getImpFlujoProbable();
				BigDecimal sumfpgto = detallesCorriente.get(i).getBloqueGto().getImpFlujoProbable();
				
				if (sumfpvida != null && sumfpfall != null && sumfpcompl != null && sumfpgto != null) {
					flujos.setTotfpprestaciones(sumfpvida.add(sumfpfall).add(sumfpcompl).add(sumfpgto));
				}
				
				BigDecimal sumfactvida = detallesCorriente.get(i).getBloqueVida().getImpFlujoActualizado();
				BigDecimal sumfactfall = detallesCorriente.get(i).getBloqueFall().getImpFlujoActualizado();
				BigDecimal sumfactcompl = detallesCorriente.get(i).getBloqueCompl().getImpFlujoActualizado();
				BigDecimal sumfactgto = detallesCorriente.get(i).getBloqueGto().getImpFlujoActualizado();
				
				if (sumfactvida != null && sumfactfall != null && sumfactcompl != null && sumfactgto != null) {
					flujos.setTotfactprestaciones(sumfactvida.add(sumfactfall).add(sumfactcompl).add(sumfactgto));
				}
				
				BigDecimal sumcolavida = detallesCorriente.get(i).getBloqueVida().getImpProvi();
				BigDecimal sumcolafall = detallesCorriente.get(i).getBloqueFall().getImpProvi();
				BigDecimal sumcolacompl = detallesCorriente.get(i).getBloqueCompl().getImpProvi();
				BigDecimal sumcolagto = detallesCorriente.get(i).getBloqueGto().getImpProvi();
				
				if (sumcolavida != null && sumcolafall != null && sumcolacompl != null && sumcolagto != null) {
					flujos.setTotcolaprestaciones(sumcolavida.add(sumcolafall).add(sumcolacompl).add(sumcolagto));
				}
				
				flujos.setTotfprob(detallesCorriente.get(i).getTotalFlujoProyeccion().getSumfprob());
				flujos.setTotcola(detallesCorriente.get(i).getTotalFlujoProyeccion().getSumcola());
				flujos.setTotprovision(detallesCorriente.get(i).getTotalFlujoProyeccion().getSumprovision());
				
			}

			flujsuscri.add(flujos);
		}
		almacenarEntregableFlujSuscri(flujsuscri);
	}
	
	@Override
	public void almacenarEntregableFlujSuscri(List<FlujSuscri> flujsuscri) {
		if (flujsuscri != null && !flujsuscri.isEmpty()) {
			Map<FlujSuscriKey, FlujSuscri> mapa = new HashMap<FlujSuscriKey, FlujSuscri>();
			for (FlujSuscri entry : flujsuscri) {
				mapa.put(entry.getKey(), entry);
			}
			flujSuscriDao.putAll(mapa);
		}
	}
	
	@Override
	public void almacenarEntregableContabilidadCertificado(List<ContabilidadCertificado> contabilidad) {
		if (contabilidad != null && !contabilidad.isEmpty()) {
			Map<ContabilidadCertificadoKey, ContabilidadCertificado> mapa = new HashMap<ContabilidadCertificadoKey, ContabilidadCertificado>();
			for (ContabilidadCertificado entry : contabilidad) {
				mapa.put(entry.getKey(), entry);
			}
			contabilidadCertificadoDao.putAll(mapa);
		}

	}
	
}