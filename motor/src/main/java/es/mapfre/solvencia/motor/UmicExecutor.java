/* MODIFICACION:MU-2018-023544-La constante de extracción no debe determinar que se extraigan las umics del PTIPO,
   ya que el PTIPO manual puede tener datos, y de esta manera no se estan filtrando.
   Solamente se filtraba cuando se generaba PTIPO Automatico
  FECHA: 12/04/2018 
  AUTOR: INDRA
*/
/* MODIFICACION:MU-2019-043655- No se borran de la cache para que el modulo cspviu pueda recuperar los datos de la umic mensual.
  FECHA: 08/07/2019 
  AUTOR: INDRA
*/
package es.mapfre.solvencia.motor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tangosol.net.partition.PartitionSet;
import com.tangosol.util.Filter;
import com.tangosol.util.filter.PartitionedFilter;

import es.mapfre.solvencia.coherence.keys.maestro.DatosAdicionalesCoaseguroKey;
import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.impl.maestro.DatosAdicionalesCoaseguroDao;
import es.mapfre.solvencia.dao.impl.maestro.PolizasInstrumentalesDao;
import es.mapfre.solvencia.dao.impl.maestro.UmicDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.FlujosProbablesDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.PolizasTipoDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.DetalleCorrienteDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.TerminosPMCUmicDao;
import es.mapfre.solvencia.dao.services.FactoriaDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.entregables.DetalleCorrienteEntregables;
import es.mapfre.solvencia.dominio.maestro.DatosAdicionalesCoaseguro;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.PolizaInstrumental;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.modulos.impl.J880GBT003ModuloTablaRealista;
import es.mapfre.solvencia.formulacion.modulos.impl.J880GBT004ModuloTablaTradicional;
import es.mapfre.solvencia.formulacion.modulos.impl.J880GBT005TablasExperienciaROSSP;
import es.mapfre.solvencia.formulacion.modulos.impl.J880GBT006TiposInteresROSSP;
import es.mapfre.solvencia.formulacion.modulos.impl.J880GBT007GastosAdministracionROSSP;
import es.mapfre.solvencia.formulacion.modulos.impl.J880GBT008TablasExperienciaBEL;
import es.mapfre.solvencia.formulacion.modulos.impl.J880GBT009CurvasInteresBEL;
import es.mapfre.solvencia.formulacion.modulos.impl.J880GBT010GastosAdministracionBEL;
import es.mapfre.solvencia.formulacion.modulos.impl.J880GBT011TasasAnulacionBEL;
import es.mapfre.solvencia.formulacion.modulos.impl.J880GBT012CurvasCINIIF17;
import es.mapfre.solvencia.formulacion.modulos.impl.Orquestador;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.utils.cartera.CorreccionCartera;

public class UmicExecutor implements Runnable {
	private static Logger log = LoggerFactory.getLogger(UmicExecutor.class);
	private static final String E00 = "00";

	private UmicKey umicKey;
	private FichaProceso fichaproceso;
	private PartitionSet partsMember;
	private Integer cteExtr;
	private List<String> BTs;
	private Set<UmicKey> umicsPrincipales;
	private int index;

	private UmicDao umicDao = (UmicDao) FactoriaDao.getDao("X880JI01");
	private DatosAdicionalesCoaseguroDao datosAdicionales = (DatosAdicionalesCoaseguroDao) FactoriaDao
			.getDao("X880JI03");
	private IObtenerDatos obtenerDatos = FachadaServicios.getObtenerDatos();
	private IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
	private DetalleCorrienteDao detalleCorrienteDao = (DetalleCorrienteDao) FactoriaDao.getDao("detalle-corriente");
	private PolizasTipoDao polizasTipoDao = (PolizasTipoDao) FactoriaDao.getDao("PTIPO");
	private TerminosPMCUmicDao terminosPMCUmicDao = (TerminosPMCUmicDao) FactoriaDao.getDao("datos-calculados-umic");

	public UmicExecutor(UmicKey umicKey, FichaProceso fichaproceso, PartitionSet partsMember, Integer cteExtr,
			List<String> bTs, Set<UmicKey> umicsPrincipales, int index) {
		super();
		this.umicKey = umicKey;
		this.fichaproceso = fichaproceso;
		this.partsMember = partsMember;
		this.cteExtr = cteExtr;
		this.BTs = bTs;
		this.umicsPrincipales = umicsPrincipales;
		this.index = index;
	}

	@Override
	public void run() {
		Ejecutor ejecutor = new Ejecutor();
		IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		BigDecimal varTotProvision = null;
		BigDecimal varTotProvisionScranm = null;
		BigDecimal varTotProvisionScraep = null;
		BigDecimal varTotProvisionScraen = null;
		Integer nMeses = 0;
		String nMesesStr = "";
		Boolean varEjecSCRLFE = true;
		Boolean varEjecSCRLMI = true;
		Boolean calculaSCRNM = true;
		Boolean calculaSCRAEN = true;
		Boolean calculaSCRAEP = true;
		DetalleBaseTecnica detalleBtBEL = new DetalleBaseTecnica();
		DetalleBaseTecnica detalleBtSCRANM = new DetalleBaseTecnica();
		DetalleBaseTecnica detalleBtSCRAEP = new DetalleBaseTecnica();
		DetalleBaseTecnica detalleBtSCRAEN = new DetalleBaseTecnica();
		String proceso;
		String varMesesCierreNIIF17;
		int mesesCierreN17 = 0;
		//Boolean sensibilidadMortalidad = false;
		List<DetalleCorriente> detalleCorrienteSCRANM = new ArrayList<DetalleCorriente>();
		List<DetalleCorriente> detalleCorrienteSCRAEN = new ArrayList<DetalleCorriente>();
		List<DetalleCorriente> detalleCorrienteSCRAEP = new ArrayList<DetalleCorriente>();
		List<DetalleCorriente> detalleCorrienteBEL = new ArrayList<DetalleCorriente>();
		TreeMap<String,BigDecimal> provisionBt = new TreeMap<String,BigDecimal>();
		Timestamp fechaCierreN17NB = UtilFechas.decreDias(fichaproceso.getFefecto(), 1);
		TotalesFlujos totalesSCRANM = null;
		TotalesFlujos totalesSCRAEN = null;
		TotalesFlujos totalesSCRAEP = null;

		Timestamp fechaActual = fichaproceso.getFcalc();
		if (!umicDao.estaProcesada(umicKey)) {
			// Se corrige la cartera antes de empezar el proceso de cálculo de la UMIC
			final Umic umic = CorreccionCartera.corregirUmic(umicDao.getUmic(umicKey));
			String tipoSubriesgoAux= null;
			if(fichaproceso.getCtipobt().equalsIgnoreCase("MULTI2")
					|| fichaproceso.getCtipobt().equals(ConstantesSolvencia.MULTI2M)
					|| fichaproceso.getCtipobt().equals(ConstantesSolvencia.MULTI2A)
					|| fichaproceso.getCtipobt().equals(ConstantesSolvencia.MULTI2G)){
				tipoSubriesgoAux = umic.getDatosGenerales().getTipoSubriesgo();
			}
			
			final DatosGenerales datosGeneralesUmic = umic.getDatosGenerales();
			
			try {
				long principioTodasBts = System.currentTimeMillis();
				bucleBT: for (String bt : BTs) {
					
					if((umic.getDatosGenerales().getCnegocio().equalsIgnoreCase("I") || (umic.getDatosGenerales().getCnegocio().equalsIgnoreCase("C") && umic.getDatosGenerales().getKramo().equalsIgnoreCase("114"))) 
							&& (fichaproceso.getCtipobt().equalsIgnoreCase("MULTI2")|| fichaproceso.getCtipobt().equals(ConstantesSolvencia.MULTI2M)
							|| fichaproceso.getCtipobt().equals(ConstantesSolvencia.MULTI2A)
							|| fichaproceso.getCtipobt().equals(ConstantesSolvencia.MULTI2G))){
						if((bt.equalsIgnoreCase(ConstantsModulos.CTE_VAL_SCRMFE) && !(umic.getDatosGenerales().getTipoSubriesgo().equalsIgnoreCase("FALL")))
							|| (bt.equalsIgnoreCase(ConstantsModulos.CTE_VAL_SCRLFE) && !(umic.getDatosGenerales().getTipoSubriesgo().equalsIgnoreCase("FALL")))){
							umic.getDatosGenerales().setTipoSubriesgo("AHOR");
						}else{
							umic.getDatosGenerales().setTipoSubriesgo(tipoSubriesgoAux);
						}
					}
					long principioBt = System.currentTimeMillis();
					try {
						DetalleBaseTecnica detalleBaseTecnica = new DetalleBaseTecnica();
						DetalleBaseTecnica detalleBaseTecnicaSCRNM = new DetalleBaseTecnica();
						DetalleBaseTecnica detalleBaseTecnicaSCRAEN = new DetalleBaseTecnica();
						DetalleBaseTecnica detalleBaseTecnicaSCRAEP = new DetalleBaseTecnica();
						detalleBaseTecnica.setBaseTec(bt.trim());
						detalleBaseTecnica.setCcanal(datosGeneralesUmic.getCcanal());
						detalleBaseTecnica.setCnegocio(datosGeneralesUmic.getCnegocio());
						detalleBaseTecnica.setKramo(datosGeneralesUmic.getKramo());
						if(bt.equalsIgnoreCase(ConstantsModulos.CTE_VAL_SCRANM)){
							detalleBaseTecnicaSCRNM = detalleBaseTecnica;
						}else if(bt.equalsIgnoreCase(ConstantsModulos.CTE_VAL_SCRAEN)){
							detalleBaseTecnicaSCRAEN = detalleBaseTecnica;
						}else if(bt.equalsIgnoreCase(ConstantsModulos.CTE_VAL_SCRAEP)){
							detalleBaseTecnicaSCRAEP = detalleBaseTecnica;
						}
					
						detalleBtBEL.setBaseTec(ConstantsModulos.CTE_BT_BEL);
						detalleBtSCRANM.setBaseTec(ConstantsModulos.CTE_VAL_SCRANM);
						detalleBtSCRAEP.setBaseTec(ConstantsModulos.CTE_VAL_SCRAEP);
						detalleBtSCRAEN.setBaseTec(ConstantsModulos.CTE_VAL_SCRAEN);
						List<DetalleCorriente> detallesCorriente = new ArrayList<DetalleCorriente>();

						final IObtenerConfiguracion servicioAUX = FachadaServicios.getObtenerConfiguracion();
						varMesesCierreNIIF17 = (String) servicioAUX.recuperarDefinicionAuxiliar(umic.getDatosGenerales().getCcartera(),
								umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), detalleBaseTecnica.getBaseTec(),
								ConstantsModulos.CTE_MESES_CIERRE_NIIF17);
						
						if (null == varMesesCierreNIIF17) {
							mesesCierreN17 = 0;
						} else {
							mesesCierreN17 = Integer.parseInt(varMesesCierreNIIF17);
						}

						// Arboles
						// si una UMIC, tiene una fecha de calculo anterior a fecha de cierre -
						// parámetro adicional nmeses la descartamos
						if (fichaproceso.getKsistema().equals(ConstantesSolvencia.CTE_SISTEMA)
								&& fichaproceso.getKprotecnico().equals(ConstantesSolvencia.CTE_PROYECTO_TECNICO)
								&& fichaproceso.getKuejecucion().equals(ConstantesSolvencia.CTE_UMIC_EJECUCION)) {
							proceso = ConstantesSolvencia.CTE_CHECKPRIMA;
							if (fichaproceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_BTI) || fichaproceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_BTI_PROY)) {
								fichaproceso.setFcalc(umic.getFechas().getFecinisus());
								if (fichaproceso.getGparametros() != null) {
									nMesesStr = almacenarDatos.getParametros(fichaproceso.getGparametros(),
											ConstantesSolvencia.CTE_ORDEN_PARAMETRO_NMES);
									nMeses = Integer.parseInt(nMesesStr.trim());
								} else {
									nMeses = 1;
								}
								if (!umic.getFechas().getFecinisus()
										.after(UtilFechas.incrMeses(fichaproceso.getFefecto(), null, -nMeses, false))
										|| umic.getDatosGenerales().getSpcom().equals("B")) {
									Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
											ConstantsFunciones.CTE_COD_ERROR_IC,
											new String[] { nMeses.toString(), umic.getFechas().getFecefecini().toString(),
													fichaproceso.getFcalc().toString() },
											ConstantesSolvencia.CTE_CHECKPRIMA, bt.trim(), fichaproceso.getCcanal(),
											datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
											datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
									almacenarDatos.almacenarIncidencias(excep.getIncidencia());
									continue bucleBT;
								}
							} else {
								Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
										ConstantsFunciones.CTE_COD_ERROR_GC,
										new String[] { umic.getFechas().getFecefecini().toString(),
												fichaproceso.getFcalc().toString() },
										ConstantesSolvencia.CTE_CHECKPRIMA, bt.trim(), fichaproceso.getCcanal(),
										datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
										datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
								almacenarDatos.almacenarIncidencias(excep.getIncidencia());
								// Dejamos de ejecutar esta UMIC
								break bucleBT;
							}
						} else if (fichaproceso.getKsistema().equals(ConstantesSolvencia.CTE_SISTEMA)
								&& fichaproceso.getKprotecnico().equals(ConstantesSolvencia.CTE_PROCESO_TECNICO_NIIF17)
								&& fichaproceso.getKuejecucion().equals(ConstantesSolvencia.CTE_UNID_EJEC_NIIF17)) {
							proceso = ConstantesSolvencia.CTE_PROC_NIIF17;

							if (fichaproceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTI4NB) ||
									fichaproceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTI8NB)) {
								if (!umic.getFechas().getFecinisus().after(UtilFechas
										.getUltimoDiaDelMes(UtilFechas.decreMeses(
										UtilFechas.decreDias(fichaproceso.getFcalc(), 1), mesesCierreN17)))) {
									Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
											ConstantsFunciones.CTE_COD_ERROR_ID,
											new String[] { String.valueOf(mesesCierreN17), umic.getFechas().getFecefecini().toString(),
													fichaproceso.getFcalc().toString() },
											ConstantesSolvencia.CTE_PROC_NIIF17, bt.trim(), fichaproceso.getCcanal(),
											datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
											datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
									almacenarDatos.almacenarIncidencias(excep.getIncidencia());
									continue bucleBT;
								}
							} else if (fichaproceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTI5)) {
								Timestamp fechaNB = UtilFechas.getUltimoDiaDelMes(UtilFechas.decreMeses(fichaproceso.getFefecto(), mesesCierreN17));
								if (!umic.getFechas().getFecinisus().after(fechaNB)) {
									Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
											ConstantsFunciones.CTE_COD_ERROR_IF,
											new String[] { fechaNB.toString(), umic.getFechas().getFecefecini().toString(),
													fichaproceso.getFcalc().toString() },
											ConstantesSolvencia.CTE_PROC_NIIF17, bt.trim(), fichaproceso.getCcanal(),
											datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
											datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
									almacenarDatos.almacenarIncidencias(excep.getIncidencia());
									continue bucleBT;
								}
								fichaproceso.setFcalc(umic.getFechas().getFecinisus());
							} else if (fichaproceso.getCtipobt().equals(ConstantsModulos.CTE_BT_NIIF17IF)) {
								Timestamp fechaAnterior = UtilFechas.getUltimoDiaDelMes(UtilFechas.decreMeses(fichaproceso.getFefecto(),
										mesesCierreN17));
								if (umic.getFechas().getFecinisus().after(fechaAnterior)) {
									Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
											ConstantsFunciones.CTE_COD_ERROR_IG,
											new String[] { fechaAnterior.toString(), umic.getFechas().getFecefecini().toString(),
													fichaproceso.getFcalc().toString() },
											ConstantesSolvencia.CTE_PROC_NIIF17, bt.trim(), fichaproceso.getCcanal(),
											datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
											datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
									almacenarDatos.almacenarIncidencias(excep.getIncidencia());
									continue bucleBT;
								}
								fichaproceso.setFcalc(UtilFechas.incrDias(fechaAnterior, 1));
							} else if (!fichaproceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTI4NB)
									&& !fichaproceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTI4C)
									&& !fichaproceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTI5)
									&& !fichaproceso.getCtipobt().equals(ConstantsModulos.CTE_BT_NIIF17IF)
									&& !fichaproceso.getCtipobt().equals(ConstantsModulos.CTE_BT_MULTI6)
									&& !fichaproceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTI8)
									&& !fichaproceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTI4NB)) {
								Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
										ConstantsFunciones.CTE_COD_ERROR_GV,
										new String[] { umic.getFechas().getFecefecini().toString(),
												fichaproceso.getFcalc().toString() },
										ConstantesSolvencia.CTE_NIIF17, bt.trim(), fichaproceso.getCcanal(),
										datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
										datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
								almacenarDatos.almacenarIncidencias(excep.getIncidencia());
								// Dejamos de ejecutar esta UMIC
								break bucleBT;
							}
						} else if (fichaproceso.getKsistema().equals(ConstantesSolvencia.CTE_SISTEMA)
								&& fichaproceso.getKprotecnico().equals(ConstantesSolvencia.CTE_PROCESO_TECNICO_SWCOBROCOMISIONES)
								&& fichaproceso.getKuejecucion().equals(ConstantesSolvencia.CTE_UNID_EJEC_SWCOBROCOMISIONES)) {
							proceso = ConstantesSolvencia.CTE_SWCOBROCOMISIONES;
							int anyo = UtilFechas.getAnio(fichaproceso.getFefecto());
							anyo--;
							/*fichaproceso.setFcalc(UtilFechas.decreDias(new Timestamp(new GregorianCalendar(anyo, 00, 01, ConstantsFunciones.CTE_0,
									ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis()), 1));*/
						} else {
							proceso = ConstantesSolvencia.CTE_PROYECCION;
						}

						// SI una UMIC, venga de donde venga, tiene una fecha de efecto POSTERIOR a la
						// fecha de cálculo, hay que rechazarla y sacar una incidencia no entrando a
						// calcular nada.
						if ((umic.getFechas().getFecefecini().after(fichaproceso.getFcalc())
								|| umic.getFechas().getFecefecini().equals(fichaproceso.getFcalc()))
								&& !fichaproceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTI5)
								&& !fichaproceso.getKuejecucion().equals(ConstantesSolvencia.CTE_UNID_EJEC_SWCOBROCOMISIONES)
								&& !(proceso == ConstantesSolvencia.CTE_CHECKPRIMA)) {
							Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
									ConstantsFunciones.CTE_COD_ERROR_CC,
									new String[] { umic.getFechas().getFecefecini().toString(),
											fichaproceso.getFcalc().toString() },
									ConstantesSolvencia.CTE_PROYECCION, bt.trim(), fichaproceso.getCcanal(),
									datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
									datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
							almacenarDatos.almacenarIncidencias(excep.getIncidencia());
							// break;
						}
						// Fecha Vencimiento
						else if (null != umic.getFechas().getFecefecfin() && umic.getFechas().getFecefecfin().before(fichaproceso.getFcalc())
								&& fichaproceso.getKuejecucion().equals(ConstantesSolvencia.CTE_UNID_EJEC_SWCOBROCOMISIONES)) {
							Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
									ConstantsFunciones.CTE_COD_ERROR_CZ,
									new String[] { umic.getFechas().getFecefecini().toString(),
											fichaproceso.getFcalc().toString() },
									ConstantesSolvencia.CTE_PROYECCION, bt.trim(), fichaproceso.getCcanal(),
									datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
									datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
							almacenarDatos.almacenarIncidencias(excep.getIncidencia());
							// break;
						} else {

							// Si la base técnica es estrés de longevidad, se ejecutará si el riesgo es
							// longevidad o
							// si el riesgo es ahorro y además la provisión estresada en mortalidad es menor
							// que la
							// provisión realista
							if ((bt.equals(ConstantsModulos.CTE_VAL_SCRLFE)
									|| bt.equals(ConstantsModulos.CTE_VAL_SCRLMI))
									&& (umic.getDatosGenerales().getTipoSubriesgo()
											.equals(ConstantsModulos.CTE_RIES_FALL)
											|| umic.getDatosGenerales().getTipoSubriesgo()
													.equals(ConstantsModulos.CTE_RIES_INCA)
											|| (umic.getDatosGenerales().getTipoSubriesgo()
													.equals(ConstantsModulos.CTE_RIES_AHOR)
													&& ((bt.equals(ConstantsModulos.CTE_VAL_SCRLFE) && !varEjecSCRLFE)
															|| (bt.equals(ConstantsModulos.CTE_VAL_SCRLMI)
																	&& !varEjecSCRLMI))))) {

								Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
										ConstantsFunciones.CTE_COD_ERROR_IH,
										new String[] { nMeses.toString(), umic.getFechas().getFecefecini().toString(),
												fichaproceso.getFcalc().toString() },
										ConstantesSolvencia.CTE_PROYECCION, bt.trim(), fichaproceso.getCcanal(),
										datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
										datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
								almacenarDatos.almacenarIncidencias(excep.getIncidencia());
								continue bucleBT;
							}

							// Si la base técnica es de estrés de tasas de anulación, se ejecutará siempre
							// que el valor de rescate sea S o:
							// - Aumento de tasas de anulación (estándar) - Flujo actualizado de rescate
							// mayor que la Provisión realista
							// - Aumento de tasas de anulación primer año (estándar) - Flujo actualizado de
							// rescate mayor que la Provisión realista
							// - Disminución de tasas de anulación (estándar) - Flujo actualizado de rescate
							// menor que la Provisión realista
							// - Aumento de tasas de anulación (interno)
							// - Disminución de tasas de anulación (interno)
			

							if (bt.equals(ConstantsModulos.CTE_VAL_SCRINC) && !umic.getDatosGenerales()
									.getTipoSubriesgo().equals(ConstantsModulos.CTE_RIES_INCA)) {
								Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
										ConstantsFunciones.CTE_COD_ERROR_IL,
										new String[] { nMeses.toString(), umic.getFechas().getFecefecini().toString(),
												fichaproceso.getFcalc().toString() },
										ConstantesSolvencia.CTE_PROYECCION, bt.trim(), fichaproceso.getCcanal(),
										datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
										datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
								almacenarDatos.almacenarIncidencias(excep.getIncidencia());
								continue bucleBT;
							}

							// Si el riesgo es ahorro y la base técnica es de mortalidad catastrófica, sólo
							// se ejecutará si la
							// provisión de SCR es mayor que la provisión de BEL
//							if (umic.getDatosGenerales().getTipoSubriesgo().equals(ConstantsModulos.CTE_RIES_AHOR)) {
//								if (bt.equals(ConstantsModulos.CTE_VAL_SCRMCF) && varEjecSCRLFE) {
//									Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
//											ConstantsFunciones.CTE_COD_ERROR_IM,
//											new String[] { nMeses.toString(), umic.getFechas().getFecefecini().toString(),
//													fichaproceso.getFcalc().toString() },
//											ConstantesSolvencia.CTE_PROYECCION, bt.trim(), fichaproceso.getCcanal(),
//											datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
//											datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
//									almacenarDatos.almacenarIncidencias(excep.getIncidencia());
//									continue bucleBT;
//								}
//								if ((bt.equals(ConstantsModulos.CTE_VAL_SCRMCI)
//										|| bt.equals(ConstantsModulos.CTE_VAL_SCRVM)) && varEjecSCRLMI) {
//									Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
//											ConstantsFunciones.CTE_COD_ERROR_IN,
//											new String[] { nMeses.toString(), umic.getFechas().getFecefecini().toString(),
//													fichaproceso.getFcalc().toString() },
//											ConstantesSolvencia.CTE_PROYECCION, bt.trim(), fichaproceso.getCcanal(),
//											datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
//											datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
//									almacenarDatos.almacenarIncidencias(excep.getIncidencia());
//									continue bucleBT;
//								}
//							}

							// Si la base técnica es estrés de mortalidad, se ejecutará si el riesgo es
							// fallecimiento o
							// si el riesgo es ahorro y además la provisión estresada en mortalidad es mayor
							// que la
							// provisión realista
							if ((null != umic.getDatosGenerales().getTipoSubriesgo() && (umic.getDatosGenerales().getTipoSubriesgo().equals(ConstantsModulos.CTE_RIES_LONG)
									|| umic.getDatosGenerales().getTipoSubriesgo()
											.equals(ConstantsModulos.CTE_RIES_INCA)))
									&& (bt.equals(ConstantsModulos.CTE_VAL_SCRMFE)
											|| bt.equals(ConstantsModulos.CTE_VAL_SCRMMI)
											|| bt.equals(ConstantsModulos.CTE_VAL_SCRMCI)
											|| bt.equals(ConstantsModulos.CTE_VAL_SCRMCF)
											|| bt.equals(ConstantsModulos.CTE_VAL_SCRVM))) {
								Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
										ConstantsFunciones.CTE_COD_ERROR_IO,
										new String[] { nMeses.toString(), umic.getFechas().getFecefecini().toString(),
												fichaproceso.getFcalc().toString() },
										ConstantesSolvencia.CTE_PROYECCION, bt.trim(), fichaproceso.getCcanal(),
										datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
										datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
								almacenarDatos.almacenarIncidencias(excep.getIncidencia());
								continue bucleBT;
							}
							
							if((umic.getDatosGenerales().getCnegocio().equalsIgnoreCase("I") || (umic.getDatosGenerales().getCnegocio().equalsIgnoreCase("C") && umic.getDatosGenerales().getKramo().equalsIgnoreCase("114")))
									&& (bt.equalsIgnoreCase(ConstantsModulos.CTE_VAL_SCRMFE) || bt.equalsIgnoreCase(ConstantsModulos.CTE_VAL_SCRLFE))){
								umic.getDatosGenerales().setTipoSubriesgo(tipoSubriesgoAux);
							}
							
							if (umic.getDatosGenerales().getKmodalidad().equals(330) 
									&& (umic.getDatosGenerales().getKgarantia().equals(96) || umic.getDatosGenerales().getKgarantia().equals(97)) 
									&& umic.getDatosGenerales().getKbencon().equals("BNC")
									&& (umic.getDatosGenerales().getKprestacion().equals("REV21")
											|| umic.getDatosGenerales().getKprestacion().equals("REV22")
											|| umic.getDatosGenerales().getKprestacion().equals("REV26")
											|| umic.getDatosGenerales().getKprestacion().equals("REV27"))
									&& bt.equals(ConstantsModulos.CTE_VAL_BTI)) {
								Umic umicMensual = obtenerDatos.recuperarUmicMensual(umic.getDatosGenerales().getKmodalidad(),
										umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
										umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(),
										umic.getDatosGenerales().getKgarantia(), umic.getDatosGenerales().getKajuste(),
										umic.getDatosGenerales().getCtipoaport(), umic.getDatosGenerales().getKbencon(),
										ConstantsFunciones.CTE_4_STRING);
								ejecutor.buscarProceso(proceso, umicMensual, fichaproceso, detalleBaseTecnica, detallesCorriente,
										null, new Stack<String>());
								almacenarDatos.almacenarDetalleCorrienteEntregables(umicMensual, detalleBaseTecnica,
										detallesCorriente, fichaproceso,false);
								if(bt.equals(ConstantsModulos.CTE_VAL_SCRANM)){
									detalleCorrienteSCRANM = detallesCorriente;
								}else if(bt.equals(ConstantsModulos.CTE_VAL_SCRAEP)){
									detalleCorrienteSCRAEP = detallesCorriente;
								}else if(bt.equals(ConstantsModulos.CTE_VAL_SCRAEN)){
									detalleCorrienteSCRAEN = detallesCorriente;
								}
								if(bt.equals("BEL")){
									detalleCorrienteBEL = detallesCorriente;
								}
								
								detallesCorriente = new ArrayList<DetalleCorriente>();
							}
							ejecutor.buscarProceso(proceso, umic, fichaproceso, detalleBaseTecnica, detallesCorriente,
									null, new Stack<String>());
							
							if(bt.equals(ConstantsModulos.CTE_VAL_SCRANM)){
								detalleCorrienteSCRANM = detallesCorriente;
							}else if(bt.equals(ConstantsModulos.CTE_VAL_SCRAEP)){
								detalleCorrienteSCRAEP = detallesCorriente;
							}else if(bt.equals(ConstantsModulos.CTE_VAL_SCRAEN)){
								detalleCorrienteSCRAEN = detallesCorriente;
							}
							if(bt.equals("BEL")){
								detalleCorrienteBEL = detallesCorriente;
							}
							
							if(fichaproceso.getCtipobt().equalsIgnoreCase("MULTI2")){
								TotalesFlujos totalesSCR = servicio.recuperarTotalesFlujos(umic, detalleBaseTecnica);
								provisionBt.put(bt,totalesSCR.getTotprovision());
								//System.out.println(umic.getDatosGenerales().getKpoliza() + ";" + bt + ";" + totalesSCR.getTotprovision());
							}
							// Si en el proceso viene "CHECKPRIMA" LO QUE HACEMOS ES GENERAR UN NUEVO
							// ENTREGABLE:
							if (proceso == ConstantesSolvencia.CTE_CHECKPRIMA) {// ConstantesSolvencia.CTE_PROYECCION/*"CHECKPRIMA"*/)
																				// {
								TotalesFlujos totales = servicio.recuperarTotalesFlujos(umic, detalleBaseTecnica);
								if (totales != null)
									almacenarDatos.almacenarValoresFLUJOSTOTP(umic, detalleBaseTecnica,
											detallesCorriente, fichaproceso, totales);
							}
//								if (detalleBaseTecnica.getBt().equals(ConstantsModulos.CTE_BT_NIIF17)
//										|| detalleBaseTecnica.getBt().equals(ConstantsModulos.CTE_BT_N17CLIR)
//										|| detalleBaseTecnica.getBt().equals(ConstantsModulos.CTE_BT_NIF17LIR)
//										|| detalleBaseTecnica.getBt().equals(ConstantsModulos.CTE_BT_N17LIRIN)
//										|| detalleBaseTecnica.getBt().equals(ConstantsModulos.CTE_BT_NIFF17OCI)
//										|| detalleBaseTecnica.getBt().equals(ConstantsModulos.CTE_BT_NIIF17IF)) {
//									TotalesFlujos totales = servicio.recuperarTotalesFlujos(umic, detalleBaseTecnica);	
//										if (totales != null) {
//											almacenarDatos.almacenarValoresFLUJOSTN17(umic, detalleBaseTecnica, detallesCorriente, fichaproceso, totales);
//										}									
//								}

							// Si la base técnica es estrés de mortalidad y el riesgo es ahorro, comparamos
							// la BEL estresada y
							// la BEL sin estresar. En caso de que la primera sea mayor, la ejecución en
							// válida. En caso contrario,
							// la descartamos.
							
							if((umic.getDatosGenerales().getCnegocio().equalsIgnoreCase("I") || (umic.getDatosGenerales().getCnegocio().equalsIgnoreCase("C") && umic.getDatosGenerales().getKramo().equalsIgnoreCase("114"))) 
									&& (fichaproceso.getCtipobt().equalsIgnoreCase("MULTI2")|| fichaproceso.getCtipobt().equals(ConstantesSolvencia.MULTI2M)
									|| fichaproceso.getCtipobt().equals(ConstantesSolvencia.MULTI2A)
									|| fichaproceso.getCtipobt().equals(ConstantesSolvencia.MULTI2G))){
								if((bt.equalsIgnoreCase(ConstantsModulos.CTE_VAL_SCRMFE) && !(umic.getDatosGenerales().getTipoSubriesgo().equalsIgnoreCase("FALL")))
									|| (bt.equalsIgnoreCase(ConstantsModulos.CTE_VAL_SCRLFE) && !(umic.getDatosGenerales().getTipoSubriesgo().equalsIgnoreCase("FALL")))){
									umic.getDatosGenerales().setTipoSubriesgo("AHOR");
								}else{
									umic.getDatosGenerales().setTipoSubriesgo(tipoSubriesgoAux);
								}
							}
							
							if (null != umic.getDatosGenerales().getTipoSubriesgo() && umic.getDatosGenerales().getTipoSubriesgo().equals(ConstantsModulos.CTE_RIES_AHOR)
									&& (bt.equals(ConstantsModulos.CTE_VAL_SCRMFE)
											|| bt.equals(ConstantsModulos.CTE_VAL_SCRMMI))) {
								if (null == varTotProvision) {
									// Recuperamos la provisión total de BEL
									varTotProvision = servicio.recuperarTotalesFlujos(umic, detalleBtBEL)
											.getTotprovision();
								}
								// Recuperamos la provisión total de la base técnica que estamos ejecutando
								TotalesFlujos totalesSCR = servicio.recuperarTotalesFlujos(umic, detalleBaseTecnica);
								if (totalesSCR.getTotprovision().compareTo(varTotProvision) == 1) {
									// Si ejecutamos mortalidad, no ejecutaremos longevidad
									if (bt.equals(ConstantsModulos.CTE_VAL_SCRMFE)) {
										varEjecSCRLFE = false;
									} else if (bt.equals(ConstantsModulos.CTE_VAL_SCRMMI)) {
										varEjecSCRLMI = false;
									}
								} else {
									//BORRA SU SCR
									almacenarDatos.eliminarDetalleBaseTecnica(detalleBaseTecnica);
									almacenarDatos.eliminarTotalesFlujos(totalesSCR);
									for (DetalleCorriente dc : detallesCorriente) {
										almacenarDatos.eliminarProyeccion(dc);
									}
//									//BORRA BRL
//									DetalleBaseTecnica detalleBaseTecnicaBEL = detalleBaseTecnica;
//									detalleBaseTecnicaBEL.setBt("BEL");
//									TotalesFlujos totalesBEL = servicio.recuperarTotalesFlujos(umic, detalleBtBEL);
//									almacenarDatos.eliminarDetalleBaseTecnica(detalleBaseTecnicaBEL);
//									almacenarDatos.eliminarTotalesFlujos(totalesBEL);
//									for (DetalleCorriente dc : detalleCorrienteBEL) {
//										almacenarDatos.eliminarProyeccion(dc);
//									}
//									//BORRAR DETALE CORREINTE ENTREGABLE BEL
////									DetalleCorrienteEntregables DetalleEntregableBEL = servicio.recuperarTotalesFlujos(umic, detalleBtBEL);
////									almacenarDatos.almacenarDetalleCorrienteEntregables(umic, detalleBaseTecnica,
////											detallesCorriente, fichaproceso);
//									
//									UmicKey umickeyBEL = new UmicKey(detalleCorrienteBEL.get(0).getCtipoaport(),
//											detalleCorrienteBEL.get(0).getKajuste(), detalleCorrienteBEL.get(0).getKcertificado(),
//											detalleCorrienteBEL.get(0).getKgarantia(), detalleCorrienteBEL.get(0).getKmodalidad(),
//											detalleCorrienteBEL.get(0).getKpoliza(), detalleCorrienteBEL.get(0).getKprestacion(),
//											detalleCorrienteBEL.get(0).getKsubpoliza(), detalleCorrienteBEL.get(0).getNorden(),
//											detalleCorrienteBEL.get(0).getNsuscri());
//									
//									for (DetalleCorriente dc : detalleCorrienteBEL) {
//										DetalleCorrienteEntregables detalleEntregableBt = new DetalleCorrienteEntregables();
//										Timestamp fdesde = dc.getFechaDesde();
//										LocalDateTime dt = new LocalDateTime(fdesde.getTime());
//										int dia = dt.getDayOfMonth();
//										detalleEntregableBt.setDia(dia);
//										fdesde = new Timestamp(dt.minusDays(dia - 1).toDateTime().getMillis());
//										
//										detalleEntregableBt.setBt("BEL");
//										detalleEntregableBt.setFcierre(dc.getFcierre());
//										detalleEntregableBt.setUmicKey(umickeyBEL);
//										detalleEntregableBt.setFechadesde(fdesde);
//										
//										almacenarDatos.eliminarDetalleCorrienteEntregables(detalleEntregableBt);
//										//almacenarDatos.eliminarProyeccion(dc);
//									}
//									List<DetalleCorriente> varProyC2S = obtenerDatos.recuperarProyeccionCualquierNodo(ConstantsModulos.CTE_VAL_BASE_BEL,
//											umic.getDatosGenerales().getFecCierre(), umic.getKey());
//									
//									UmicKey umickeyBEL = new UmicKey(detalleCorrienteBEL.get(0).getCtipoaport(),
//											detalleCorrienteBEL.get(0).getKajuste(), detalleCorrienteBEL.get(0).getKcertificado(),
//											detalleCorrienteBEL.get(0).getKgarantia(), detalleCorrienteBEL.get(0).getKmodalidad(),
//											detalleCorrienteBEL.get(0).getKpoliza(), detalleCorrienteBEL.get(0).getKprestacion(),
//											detalleCorrienteBEL.get(0).getKsubpoliza(), detalleCorrienteBEL.get(0).getNorden(),
//											detalleCorrienteBEL.get(0).getNsuscri());
//									
//									for (DetalleCorriente dc : varProyC2S) {
//										DetalleCorrienteEntregables detalleEntregableBt = new DetalleCorrienteEntregables();
//										Timestamp fdesde = dc.getFechaDesde();
//										LocalDateTime dt = new LocalDateTime(fdesde.getTime());
//										int dia = dt.getDayOfMonth();
//										detalleEntregableBt.setDia(dia);
//										fdesde = new Timestamp(dt.minusDays(dia - 1).toDateTime().getMillis());
//										
//										detalleEntregableBt.setBt("BEL");
//										detalleEntregableBt.setFcierre(dc.getFcierre());
//										detalleEntregableBt.setUmicKey(umickeyBEL);
//										detalleEntregableBt.setFechadesde(fdesde);
//										
//										almacenarDatos.eliminarDetalleCorrienteEntregables(detalleEntregableBt);
//										//almacenarDatos.eliminarProyeccion(dc);
//									}
									
									Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
											ConstantsFunciones.CTE_COD_ERROR_IP,
											new String[] { nMeses.toString(), umic.getFechas().getFecefecini().toString(),
													fichaproceso.getFcalc().toString() },
											ConstantesSolvencia.CTE_PROYECCION, bt.trim(), fichaproceso.getCcanal(),
											datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
											datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
									almacenarDatos.almacenarIncidencias(excep.getIncidencia());
									//CAMBIOSCRMFE
									continue bucleBT;
								}
							}
//							
							if (bt.equals(ConstantsModulos.CTE_VAL_SCRAEP) || bt.equals(ConstantsModulos.CTE_VAL_SCRAEN)
									|| bt.equals(ConstantsModulos.CTE_VAL_SCRANM)
									|| bt.equals(ConstantsModulos.CTE_VAL_SCRAIP)
									|| bt.equals(ConstantsModulos.CTE_VAL_SCRAIN)) {

								if (umic.getRescates().getIndicrescate().equals(ConstantsModulos.CTE_N)) {
									almacenarDatos.eliminarDetalleBaseTecnica(detalleBaseTecnica);
									almacenarDatos.eliminarTotalesFlujos(servicio.recuperarTotalesFlujos(umic, detalleBaseTecnica));
									for (DetalleCorriente dc : detallesCorriente) {
										almacenarDatos.eliminarProyeccion(dc);
									}
//									if (bt.equals(ConstantsModulos.CTE_VAL_SCRAEN)){
//									List<DetalleCorriente> varProyC2S = obtenerDatos.recuperarProyeccionCualquierNodo(ConstantsModulos.CTE_VAL_BASE_BEL,
//											umic.getDatosGenerales().getFecCierre(), umic.getKey());
//									
//									UmicKey umickeyBEL = new UmicKey(detalleCorrienteBEL.get(0).getCtipoaport(),
//											detalleCorrienteBEL.get(0).getKajuste(), detalleCorrienteBEL.get(0).getKcertificado(),
//											detalleCorrienteBEL.get(0).getKgarantia(), detalleCorrienteBEL.get(0).getKmodalidad(),
//											detalleCorrienteBEL.get(0).getKpoliza(), detalleCorrienteBEL.get(0).getKprestacion(),
//											detalleCorrienteBEL.get(0).getKsubpoliza(), detalleCorrienteBEL.get(0).getNorden(),
//											detalleCorrienteBEL.get(0).getNsuscri());
//									
//									for (DetalleCorriente dc : varProyC2S) {
//										DetalleCorrienteEntregables detalleEntregableBt = new DetalleCorrienteEntregables();
//										Timestamp fdesde = dc.getFechaDesde();
//										LocalDateTime dt = new LocalDateTime(fdesde.getTime());
//										int dia = dt.getDayOfMonth();
//										detalleEntregableBt.setDia(dia);
//										fdesde = new Timestamp(dt.minusDays(dia - 1).toDateTime().getMillis());
//										
//										detalleEntregableBt.setBt("BEL");
//										detalleEntregableBt.setFcierre(dc.getFcierre());
//										detalleEntregableBt.setUmicKey(umickeyBEL);
//										detalleEntregableBt.setFechadesde(fdesde);
//										
//										almacenarDatos.eliminarDetalleCorrienteEntregables(detalleEntregableBt);
//										//almacenarDatos.eliminarProyeccion(dc);
//									}
//									Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
//											ConstantsFunciones.CTE_COD_ERROR_II,
//											new String[] { nMeses.toString(), umic.getFechas().getFecefecini().toString(),
//													fichaproceso.getFcalc().toString() },
//											ConstantesSolvencia.CTE_PROYECCION, bt.trim(), fichaproceso.getCcanal(),
//											datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
//											datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
//									almacenarDatos.almacenarIncidencias(excep.getIncidencia());
//									break bucleBT;
//									}else{
										Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
												ConstantsFunciones.CTE_COD_ERROR_II,
												new String[] { nMeses.toString(), umic.getFechas().getFecefecini().toString(),
														fichaproceso.getFcalc().toString() },
												ConstantesSolvencia.CTE_PROYECCION, bt.trim(), fichaproceso.getCcanal(),
												datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
												datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
										almacenarDatos.almacenarIncidencias(excep.getIncidencia());
										continue bucleBT;	
									//}
									//CAMBIOANUL
								} else {
									
									if (null == varTotProvision) {
										// Recuperamos la provisión total de BEL
										varTotProvision = servicio.recuperarTotalesFlujos(umic, detalleBtBEL)
												.getTotprovision();
									}
									// Recuperamos la provisión total de la base técnica que estamos ejecutando
									TotalesFlujos totalesSCR = servicio.recuperarTotalesFlujos(umic, detalleBaseTecnica);
									//if (totalesSCR.getTotprovision().compareTo(varTotProvision) == 1)
//									if(null == totalesSCR.getTotprovision()){
//										System.out.println(umic.getDatosGenerales().getKpoliza() + ";" + umic.getDatosGenerales().getKgarantia() + ";" + bt + ";" + "0,00");
//									}else{
//										System.out.println(umic.getDatosGenerales().getKpoliza() + ";" + umic.getDatosGenerales().getKgarantia() + ";" + bt + ";" + totalesSCR.getTotprovision());
//									}
									if (bt.equals(ConstantsModulos.CTE_VAL_SCRANM) && !(totalesSCR.getTotprovision().compareTo(varTotProvision) == 1)){
										almacenarDatos.eliminarDetalleBaseTecnica(detalleBaseTecnica);
										almacenarDatos.eliminarTotalesFlujos(totalesSCR);
										for (DetalleCorriente dc : detallesCorriente) {
											almacenarDatos.eliminarProyeccion(dc);
										}
										Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
												ConstantsFunciones.CTE_COD_ERROR_IJ,
												new String[] { nMeses.toString(), umic.getFechas().getFecefecini().toString(),
														fichaproceso.getFcalc().toString() },
												ConstantesSolvencia.CTE_PROYECCION, bt.trim(), fichaproceso.getCcanal(),
												datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
												datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
										almacenarDatos.almacenarIncidencias(excep.getIncidencia());
										calculaSCRNM = false;
										continue bucleBT;
									}else if(bt.equals(ConstantsModulos.CTE_VAL_SCRAEP)){
										
										if(null != totalesSCRANM){
											varTotProvisionScranm = totalesSCRANM.getTotprovision();
										}else{
											varTotProvisionScranm = BigDecimal.ZERO;
										}
										
										if (!(totalesSCR.getTotprovision().compareTo(varTotProvision) == 1)) {
											almacenarDatos.eliminarDetalleBaseTecnica(detalleBaseTecnica);
											almacenarDatos.eliminarTotalesFlujos(totalesSCR);
											for (DetalleCorriente dc : detallesCorriente) {
												almacenarDatos.eliminarProyeccion(dc);
											}
											
//												List<DetalleCorriente> varProyC2S = obtenerDatos.recuperarProyeccionCualquierNodo(ConstantsModulos.CTE_VAL_BASE_BEL,
//														umic.getDatosGenerales().getFecCierre(), umic.getKey());
//												
//												UmicKey umickeyBEL = new UmicKey(detalleCorrienteBEL.get(0).getCtipoaport(),
//														detalleCorrienteBEL.get(0).getKajuste(), detalleCorrienteBEL.get(0).getKcertificado(),
//														detalleCorrienteBEL.get(0).getKgarantia(), detalleCorrienteBEL.get(0).getKmodalidad(),
//														detalleCorrienteBEL.get(0).getKpoliza(), detalleCorrienteBEL.get(0).getKprestacion(),
//														detalleCorrienteBEL.get(0).getKsubpoliza(), detalleCorrienteBEL.get(0).getNorden(),
//														detalleCorrienteBEL.get(0).getNsuscri());
//												
//												for (DetalleCorriente dc : varProyC2S) {
//													DetalleCorrienteEntregables detalleEntregableBt = new DetalleCorrienteEntregables();
//													Timestamp fdesde = dc.getFechaDesde();
//													LocalDateTime dt = new LocalDateTime(fdesde.getTime());
//													int dia = dt.getDayOfMonth();
//													detalleEntregableBt.setDia(dia);
//													fdesde = new Timestamp(dt.minusDays(dia - 1).toDateTime().getMillis());
//													
//													detalleEntregableBt.setBt("BEL");
//													detalleEntregableBt.setFcierre(dc.getFcierre());
//													detalleEntregableBt.setUmicKey(umickeyBEL);
//													detalleEntregableBt.setFechadesde(fdesde);
//													
//													almacenarDatos.eliminarDetalleCorrienteEntregables(detalleEntregableBt);
//													//almacenarDatos.eliminarProyeccion(dc);
//												}
											
											Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
													ConstantsFunciones.CTE_COD_ERROR_IJ,
													new String[] { nMeses.toString(), umic.getFechas().getFecefecini().toString(),
															fichaproceso.getFcalc().toString() },
													ConstantesSolvencia.CTE_PROYECCION, bt.trim(), fichaproceso.getCcanal(),
													datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
													datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
											almacenarDatos.almacenarIncidencias(excep.getIncidencia());
											calculaSCRAEP = false;
											continue bucleBT;
										}else if(!(totalesSCR.getTotprovision().compareTo(varTotProvisionScranm) == 1)){
											almacenarDatos.eliminarDetalleBaseTecnica(detalleBaseTecnica);
											almacenarDatos.eliminarTotalesFlujos(totalesSCR);
											for (DetalleCorriente dc : detallesCorriente) {
												almacenarDatos.eliminarProyeccion(dc);
											}
//											List<DetalleCorriente> varProyC2S = obtenerDatos.recuperarProyeccionCualquierNodo(ConstantsModulos.CTE_VAL_BASE_BEL,
//													umic.getDatosGenerales().getFecCierre(), umic.getKey());
//											
//											UmicKey umickeyBEL = new UmicKey(detalleCorrienteBEL.get(0).getCtipoaport(),
//													detalleCorrienteBEL.get(0).getKajuste(), detalleCorrienteBEL.get(0).getKcertificado(),
//													detalleCorrienteBEL.get(0).getKgarantia(), detalleCorrienteBEL.get(0).getKmodalidad(),
//													detalleCorrienteBEL.get(0).getKpoliza(), detalleCorrienteBEL.get(0).getKprestacion(),
//													detalleCorrienteBEL.get(0).getKsubpoliza(), detalleCorrienteBEL.get(0).getNorden(),
//													detalleCorrienteBEL.get(0).getNsuscri());
//											
//											for (DetalleCorriente dc : varProyC2S) {
//												DetalleCorrienteEntregables detalleEntregableBt = new DetalleCorrienteEntregables();
//												Timestamp fdesde = dc.getFechaDesde();
//												LocalDateTime dt = new LocalDateTime(fdesde.getTime());
//												int dia = dt.getDayOfMonth();
//												detalleEntregableBt.setDia(dia);
//												fdesde = new Timestamp(dt.minusDays(dia - 1).toDateTime().getMillis());
//												
//												detalleEntregableBt.setBt("BEL");
//												detalleEntregableBt.setFcierre(dc.getFcierre());
//												detalleEntregableBt.setUmicKey(umickeyBEL);
//												detalleEntregableBt.setFechadesde(fdesde);
//												
//												almacenarDatos.eliminarDetalleCorrienteEntregables(detalleEntregableBt);
//												//almacenarDatos.eliminarProyeccion(dc);
//											}
											Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
													ConstantsFunciones.CTE_COD_ERROR_IJ,
													new String[] { nMeses.toString(), umic.getFechas().getFecefecini().toString(),
															fichaproceso.getFcalc().toString() },
													ConstantesSolvencia.CTE_PROYECCION, bt.trim(), fichaproceso.getCcanal(),
													datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
													datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
											almacenarDatos.almacenarIncidencias(excep.getIncidencia());
											calculaSCRAEP = false;
											continue bucleBT;
										}
									}else if(bt.equals(ConstantsModulos.CTE_VAL_SCRAEN)){
										
										if(null != totalesSCRANM){
											varTotProvisionScranm = totalesSCRANM.getTotprovision();
										}else{
											varTotProvisionScranm = BigDecimal.ZERO;
										}
										
										if (bt.equals(ConstantsModulos.CTE_VAL_SCRAEN) && !(totalesSCR.getTotprovision().compareTo(varTotProvision) == 1)) {
//											List<DetalleCorriente> varProyC2S = obtenerDatos.recuperarProyeccionCualquierNodo(ConstantsModulos.CTE_VAL_BASE_BEL,
//													umic.getDatosGenerales().getFecCierre(), umic.getKey());
//											
//											UmicKey umickeyBEL = new UmicKey(detalleCorrienteBEL.get(0).getCtipoaport(),
//													detalleCorrienteBEL.get(0).getKajuste(), detalleCorrienteBEL.get(0).getKcertificado(),
//													detalleCorrienteBEL.get(0).getKgarantia(), detalleCorrienteBEL.get(0).getKmodalidad(),
//													detalleCorrienteBEL.get(0).getKpoliza(), detalleCorrienteBEL.get(0).getKprestacion(),
//													detalleCorrienteBEL.get(0).getKsubpoliza(), detalleCorrienteBEL.get(0).getNorden(),
//													detalleCorrienteBEL.get(0).getNsuscri());
//											
//											for (DetalleCorriente dc : varProyC2S) {
//												DetalleCorrienteEntregables detalleEntregableBt = new DetalleCorrienteEntregables();
//												Timestamp fdesde = dc.getFechaDesde();
//												LocalDateTime dt = new LocalDateTime(fdesde.getTime());
//												int dia = dt.getDayOfMonth();
//												detalleEntregableBt.setDia(dia);
//												fdesde = new Timestamp(dt.minusDays(dia - 1).toDateTime().getMillis());
//												
//												detalleEntregableBt.setBt("BEL");
//												detalleEntregableBt.setFcierre(dc.getFcierre());
//												detalleEntregableBt.setUmicKey(umickeyBEL);
//												detalleEntregableBt.setFechadesde(fdesde);
//												
//												almacenarDatos.eliminarDetalleCorrienteEntregables(detalleEntregableBt);
//												//almacenarDatos.eliminarProyeccion(dc);
//											}
											almacenarDatos.eliminarDetalleBaseTecnica(detalleBaseTecnica);
											almacenarDatos.eliminarTotalesFlujos(totalesSCR);
											for (DetalleCorriente dc : detallesCorriente) {
												almacenarDatos.eliminarProyeccion(dc);
											}
											Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
													ConstantsFunciones.CTE_COD_ERROR_IK,
													new String[] { nMeses.toString(), umic.getFechas().getFecefecini().toString(),
															fichaproceso.getFcalc().toString() },
													ConstantesSolvencia.CTE_PROYECCION, bt.trim(), fichaproceso.getCcanal(),
													datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
													datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
											almacenarDatos.almacenarIncidencias(excep.getIncidencia());
											continue bucleBT;
										}else if(!(totalesSCR.getTotprovision().compareTo(provisionBt.get(ConstantsModulos.CTE_VAL_SCRANM)) == 1)){
											
											almacenarDatos.eliminarDetalleBaseTecnica(detalleBaseTecnica);
											almacenarDatos.eliminarTotalesFlujos(totalesSCR);
											for (DetalleCorriente dc : detallesCorriente) {
												almacenarDatos.eliminarProyeccion(dc);
											}
//											List<DetalleCorriente> varProyC2S = obtenerDatos.recuperarProyeccionCualquierNodo(ConstantsModulos.CTE_VAL_BASE_BEL,
//													umic.getDatosGenerales().getFecCierre(), umic.getKey());
//											
//											UmicKey umickeyBEL = new UmicKey(detalleCorrienteBEL.get(0).getCtipoaport(),
//													detalleCorrienteBEL.get(0).getKajuste(), detalleCorrienteBEL.get(0).getKcertificado(),
//													detalleCorrienteBEL.get(0).getKgarantia(), detalleCorrienteBEL.get(0).getKmodalidad(),
//													detalleCorrienteBEL.get(0).getKpoliza(), detalleCorrienteBEL.get(0).getKprestacion(),
//													detalleCorrienteBEL.get(0).getKsubpoliza(), detalleCorrienteBEL.get(0).getNorden(),
//													detalleCorrienteBEL.get(0).getNsuscri());
//											
//											for (DetalleCorriente dc : varProyC2S) {
//												DetalleCorrienteEntregables detalleEntregableBt = new DetalleCorrienteEntregables();
//												Timestamp fdesde = dc.getFechaDesde();
//												LocalDateTime dt = new LocalDateTime(fdesde.getTime());
//												int dia = dt.getDayOfMonth();
//												detalleEntregableBt.setDia(dia);
//												fdesde = new Timestamp(dt.minusDays(dia - 1).toDateTime().getMillis());
//												
//												detalleEntregableBt.setBt("BEL");
//												detalleEntregableBt.setFcierre(dc.getFcierre());
//												detalleEntregableBt.setUmicKey(umickeyBEL);
//												detalleEntregableBt.setFechadesde(fdesde);
//												
//												almacenarDatos.eliminarDetalleCorrienteEntregables(detalleEntregableBt);
//												//almacenarDatos.eliminarProyeccion(dc);
//											}
											Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
													ConstantsFunciones.CTE_COD_ERROR_IJ,
													new String[] { nMeses.toString(), umic.getFechas().getFecefecini().toString(),
															fichaproceso.getFcalc().toString() },
													ConstantesSolvencia.CTE_PROYECCION, bt.trim(), fichaproceso.getCcanal(),
													datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
													datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
											almacenarDatos.almacenarIncidencias(excep.getIncidencia());
											continue bucleBT;
										}else if(!(totalesSCR.getTotprovision().compareTo(provisionBt.get(ConstantsModulos.CTE_VAL_SCRAEP)) == 1)){
											
											almacenarDatos.eliminarDetalleBaseTecnica(detalleBaseTecnica);
											almacenarDatos.eliminarTotalesFlujos(totalesSCR);

											Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
													ConstantsFunciones.CTE_COD_ERROR_IJ,
													new String[] { nMeses.toString(), umic.getFechas().getFecefecini().toString(),
															fichaproceso.getFcalc().toString() },
													ConstantesSolvencia.CTE_PROYECCION, bt.trim(), fichaproceso.getCcanal(),
													datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
													datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
											almacenarDatos.almacenarIncidencias(excep.getIncidencia());
											continue bucleBT;
											
										}
										
										if(calculaSCRNM && !(provisionBt.get(ConstantsModulos.CTE_VAL_SCRANM).compareTo(provisionBt.get(ConstantsModulos.CTE_VAL_SCRAEN)) == 1)){
											//Se borra SCRANM
											almacenarDatos.eliminarDetalleBaseTecnica(detalleBaseTecnicaSCRNM);
											almacenarDatos.eliminarTotalesFlujos(totalesSCRANM);
											for (DetalleCorriente dc : detalleCorrienteSCRANM) {
												almacenarDatos.eliminarProyeccion(dc);
											}
											Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
													ConstantsFunciones.CTE_COD_ERROR_IK,
													new String[] { nMeses.toString(), umic.getFechas().getFecefecini().toString(),
															fichaproceso.getFcalc().toString() },
													ConstantesSolvencia.CTE_PROYECCION, ConstantsModulos.CTE_VAL_SCRANM.trim(), fichaproceso.getCcanal(),
													datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
													datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
											almacenarDatos.almacenarIncidencias(excep.getIncidencia());	
											
										}
										
										if(calculaSCRNM && !(provisionBt.get(ConstantsModulos.CTE_VAL_SCRANM).compareTo(provisionBt.get(ConstantsModulos.CTE_VAL_SCRAEP)) == 1)){
											//Se borra SCRANM
											almacenarDatos.eliminarDetalleBaseTecnica(detalleBaseTecnicaSCRNM);
											almacenarDatos.eliminarTotalesFlujos(totalesSCRANM);
											for (DetalleCorriente dc : detalleCorrienteSCRANM) {
												almacenarDatos.eliminarProyeccion(dc);
											}
											Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
													ConstantsFunciones.CTE_COD_ERROR_IK,
													new String[] { nMeses.toString(), umic.getFechas().getFecefecini().toString(),
															fichaproceso.getFcalc().toString() },
													ConstantesSolvencia.CTE_PROYECCION, ConstantsModulos.CTE_VAL_SCRANM.trim(), fichaproceso.getCcanal(),
													datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
													datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
											almacenarDatos.almacenarIncidencias(excep.getIncidencia());										
											
										}
										
										if(calculaSCRAEP &&!(provisionBt.get(ConstantsModulos.CTE_VAL_SCRAEP).compareTo(provisionBt.get(ConstantsModulos.CTE_VAL_SCRAEN)) == 1)){
											//Se borra SCRAEP
											almacenarDatos.eliminarDetalleBaseTecnica(detalleBaseTecnicaSCRAEP);
											almacenarDatos.eliminarTotalesFlujos(totalesSCRAEP);

											Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
													ConstantsFunciones.CTE_COD_ERROR_IJ,
													new String[] { nMeses.toString(), umic.getFechas().getFecefecini().toString(),
															fichaproceso.getFcalc().toString() },
													ConstantesSolvencia.CTE_PROYECCION, ConstantsModulos.CTE_VAL_SCRAEP.trim(), fichaproceso.getCcanal(),
													datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
													datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
											almacenarDatos.almacenarIncidencias(excep.getIncidencia());
											continue bucleBT;
										}
										
//										if(calculaSCRNM && !((provisionBt.get(ConstantsModulos.CTE_VAL_SCRANM).compareTo(provisionBt.get(ConstantsModulos.CTE_VAL_SCRAEP))) == 1) && 
//											!((provisionBt.get(ConstantsModulos.CTE_VAL_SCRANM).compareTo(provisionBt.get(ConstantsModulos.CTE_VAL_SCRAEN))) == 1)){
//											almacenarDatos.eliminarDetalleBaseTecnica(detalleBaseTecnicaSCRNM);
//											almacenarDatos.eliminarTotalesFlujos(totalesSCRANM);
//											for (DetalleCorriente dc : detalleCorrienteSCRANM) {
//												almacenarDatos.eliminarProyeccion(dc);
//											}
//											Solvencia2Excepcion excep = Solvencia2ExcepcionHelper.crearExcepcion(
//													ConstantsFunciones.CTE_COD_ERROR_IK,
//													new String[] { nMeses.toString(), umic.getFechas().getFecefecini().toString(),
//															fichaproceso.getFcalc().toString() },
//													ConstantesSolvencia.CTE_PROYECCION, ConstantsModulos.CTE_VAL_SCRANM.trim(), fichaproceso.getCcanal(),
//													datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
//													datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(), null);
//											almacenarDatos.almacenarIncidencias(excep.getIncidencia());
//										}
									}
									
								}
//									//Recuperamos la provisión total de BEL
//									TotalesFlujos totFlujosBEL = servicio.recuperarTotalesFlujos(umic, detalleBtBEL);
//									if (umic.getRescates().getIndicrescate().equals(ConstantsModulos.CTE_N) || 	
//											((bt.equals(ConstantsModulos.CTE_VAL_SCRAEP) || bt.equals(ConstantsModulos.CTE_VAL_SCRAIP) || bt.equals(ConstantsModulos.CTE_VAL_SCRANM)) &&
//													totFlujosBEL.getTotfactrte().compareTo(totFlujosBEL.getTotprovision()) == -1) ||
//											((bt.equals(ConstantsModulos.CTE_VAL_SCRAEN) || bt.equals(ConstantsModulos.CTE_VAL_SCRAIN)) && 
//													totFlujosBEL.getTotfactrte().compareTo(totFlujosBEL.getTotprovision()) != -1)){
//										continue bucleBT;
//									}
							}

//								if (umic.getBti().getSwcasadoI1().equals("S")) {
//									detallesCorriente=ModificarDetallesCorriente(detallesCorriente, umic);
//								}

							if((umic.getDatosGenerales().getCnegocio().equalsIgnoreCase("I") || (umic.getDatosGenerales().getCnegocio().equalsIgnoreCase("C") && umic.getDatosGenerales().getKramo().equalsIgnoreCase("114")))
									&& (bt.equalsIgnoreCase(ConstantsModulos.CTE_VAL_SCRMFE) || bt.equalsIgnoreCase(ConstantsModulos.CTE_VAL_SCRLFE))){
								umic.getDatosGenerales().setTipoSubriesgo(tipoSubriesgoAux);
							}
							
							almacenarDatos.almacenarDetalleCorrienteEntregables(umic, detalleBaseTecnica,
									detallesCorriente, fichaproceso,false);
							
							
							//// Entregable FLUJINFSCR ////
							if(fichaproceso.getCtipobt().equalsIgnoreCase("MULTI2") && (detalleBaseTecnica.getBaseTec().equalsIgnoreCase("SCRMFE") || detalleBaseTecnica.getBaseTec().equalsIgnoreCase("SCRLFE"))){
								
								List<DetalleCorriente> varProyC2S = obtenerDatos.recuperarProyeccionCualquierNodo(ConstantsModulos.CTE_VAL_BASE_BEL,
										umic.getDatosGenerales().getFecCierre(), umic.getKey());
								
								almacenarDatos.almacenarDetalleCorrienteEntregables(umic, detalleBaseTecnica,
										varProyC2S, fichaproceso,true);
								
							}

							//////////////////////////////////
							if (bt.equals(ConstantsModulos.CTE_VAL_SCRVM)) {
								almacenarDatos.almacenarValoresSCRVM(umic, detalleBaseTecnica, detallesCorriente,
										fichaproceso);
							}

							// ini fase VIII
							TotalesFlujos totales = servicio.recuperarTotalesFlujos(umic, detalleBaseTecnica);
							if (totales != null) {
								// Fechas
								totales.setFecini(umic.getFechas().getFecefecini());
								totales.setFecfin(umic.getFechas().getFecefecfin());
								totales.setFecinisus(umic.getFechas().getFecinisus());
								// Coaseguro
								totales.setPcoaseg(umic.getDatosCoaseguro().getPcoaseg());
								totales.setDistint(umic.getDatosCoaseguro().getDistint());
								totales.setKcoaseOri(umic.getDatosCoaseguro().getKcoaseOri());
								// DatosAdicionales
								totales.setPrestCal(umic.getDatosAdicionales().getPrestCal());
								// BaseTecnicaInicial
								totales.setPgastgesin1I(umic.getBti().getPgastgesin1I());
								totales.setPgastgesin2I(umic.getBti().getPgastgesin2I());
								totales.setPgastgesex1I(umic.getBti().getPgastgesex1I());
								totales.setFecFinTramo1(umic.getBti().getFecFinTramo1());
								totales.setPintertecnI1(umic.getBti().getPintertecnI1());
								totales.setPintertecnI2(umic.getBti().getPintertecnI2());
								totales.setTabla1Aseg1(umic.getBti().getTabla1Aseg1());
								// DetalleBaseTecnica
								totales.setItcalc(detalleBaseTecnica.getItcalc());
								totales.setTablacalc1aseg1(detalleBaseTecnica.getTablacalc1aseg1());
								totales.setTablaTanul(detalleBaseTecnica.getTablaTanul());
								totales.setGtoUni(detalleBaseTecnica.getGtoUni());
								totales.setGtoprov(detalleBaseTecnica.getGtoprov());
								totales.setCurvati(detalleBaseTecnica.getCurvaTi());
								totales.setFactor1(detalleBaseTecnica.getFactor1());
								totales.setFactor2(detalleBaseTecnica.getFactor2());
								// DatosAdicionalesCoaseguro
								DatosAdicionalesCoaseguro datosAdicionalesCoaseguro = datosAdicionales
										.get(new DatosAdicionalesCoaseguroKey(umic.getDatosGenerales().getKpoliza(),
												umic.getDatosGenerales().getKsubpoliza()));
								if (datosAdicionalesCoaseguro != null) {
									totales.setKcoase1(datosAdicionalesCoaseguro.getKcoase1());
									totales.setKcoase2(datosAdicionalesCoaseguro.getKcoase2());
									totales.setKcoase3(datosAdicionalesCoaseguro.getKcoase3());
									totales.setKcoase4(datosAdicionalesCoaseguro.getKcoase4());
									totales.setKcoase5(datosAdicionalesCoaseguro.getKcoase5());
									totales.setKcoase6(datosAdicionalesCoaseguro.getKcoase6());
									totales.setKcoase7(datosAdicionalesCoaseguro.getKcoase7());
									totales.setKcoase8(datosAdicionalesCoaseguro.getKcoase8());
									totales.setKcoase9(datosAdicionalesCoaseguro.getKcoase9());
									totales.setKcoase10(datosAdicionalesCoaseguro.getKcoase10());
									totales.setKcoase11(datosAdicionalesCoaseguro.getKcoase11());
									totales.setKcoase12(datosAdicionalesCoaseguro.getKcoase12());
									totales.setKcoase13(datosAdicionalesCoaseguro.getKcoase13());
									totales.setKcoase14(datosAdicionalesCoaseguro.getKcoase14());
									totales.setKcoase15(datosAdicionalesCoaseguro.getKcoase15());
									totales.setKcoase16(datosAdicionalesCoaseguro.getKcoase16());
									totales.setKcoase17(datosAdicionalesCoaseguro.getKcoase17());
									totales.setKcoase18(datosAdicionalesCoaseguro.getKcoase18());
									totales.setKcoase19(datosAdicionalesCoaseguro.getKcoase19());
									totales.setKcoase20(datosAdicionalesCoaseguro.getKcoase20());
									totales.setKcuadro(datosAdicionalesCoaseguro.getKcuadro());
								}
								totales.setUmicKey(umicKey);
								totales.setProvbtifcal(
										detallesCorriente.get(0).getTotalFlujoProyeccion().getProvbtiproy());
								for (int i = 0; i < detallesCorriente.size(); i++) {
									final DetalleCorriente detalleCorriente = detallesCorriente.get(i);

									if (detalleCorriente.getRaumic() != null) {
										totales.setRaumic(totales.getRaumic().add(detalleCorriente.getRaumic()));
									}
									if (detalleCorriente.getCsmumic() != null) {
										totales.setCsmumic(totales.getCsmumic().add(detalleCorriente.getCsmumic()));
									}
									if (detalleCorriente.getPatronCSM() != null) {
										totales.setCsmajustado(
												totales.getCsmajustado().add(detalleCorriente.getPatronCSM()));
									}
									if (detalleCorriente.getRosspCSM() != null) {
										totales.setCsm003(totales.getCsm003().add(detalleCorriente.getRosspCSM()));
									}
								}
								//INI -  PROXY Prestaciones
								totales.setCohorte(umic.getDatosNiif17().getcohorte());
								totales.setSwcasado(umic.getDatosGenerales().getSwcasado());
								totales.setKmodalidadOrig(totales.getKmodalidad());
								totales.setKpolizaOrig(totales.getKpoliza());
								totales.setKsubpolizaOrig(totales.getKsubpoliza());
								totales.setKcertificadoOrig(totales.getKcertificado());

								if (totales.getKmodalidad() == 400) {
									PolizasInstrumentalesDao polizasInstrumentalesDao = new PolizasInstrumentalesDao();
									List<PolizaInstrumental> polizasInstrumentales = polizasInstrumentalesDao.getValues(totales.getKmodalidad(), totales.getKpoliza(), totales.getKsubpoliza(), totales.getKcertificado());
									if (polizasInstrumentales.size() > 0) {
										totales.setKmodalidadOrig(polizasInstrumentales.get(0).getKmodalidadOrig());
										totales.setKpolizaOrig(polizasInstrumentales.get(0).getKpolizaOrig());
										totales.setKsubpolizaOrig(polizasInstrumentales.get(0).getKsubpolizaOrig());
										totales.setKcertificadoOrig(polizasInstrumentales.get(0).getKcertificadoOrig());
									} 
								}
								//FIN - PROXY Prestaciones
								almacenarDatos.almacenarTotalFlujos(totales);
								if(bt.equals(ConstantsModulos.CTE_VAL_SCRANM)){
									totalesSCRANM = totales;
								} else if(bt.equals(ConstantsModulos.CTE_VAL_SCRAEN)){
									totalesSCRAEN = totales;	
								}else if(bt.equals(ConstantsModulos.CTE_VAL_SCRAEP)){
									totalesSCRAEP = totales;
								}

								if (detalleBaseTecnica.getBt().equals(ConstantsModulos.CTE_BT_NIIF17)
										|| detalleBaseTecnica.getBt().equals(ConstantsModulos.CTE_BT_N17CLIR)
										|| detalleBaseTecnica.getBt().equals(ConstantsModulos.CTE_BT_NIF17LIR)
										|| detalleBaseTecnica.getBt().equals(ConstantsModulos.CTE_BT_N17LIRIN)
										|| detalleBaseTecnica.getBt().equals(ConstantsModulos.CTE_BT_NIFF17OCI)
										|| detalleBaseTecnica.getBt().equals(ConstantsModulos.CTE_BT_NIIF17IF)) {
									TotalesFlujos tot = servicio.recuperarTotalesFlujos(umic, detalleBaseTecnica);
									if (totales != null) {
										almacenarDatos.almacenarValoresFLUJOSTN17(umic, detalleBaseTecnica,
												detallesCorriente, fichaproceso, tot);
									}
								}

							}

							// Fin FaseVIII
							
							if (fichaproceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_BTCOA)
									|| fichaproceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_BTCOATF)) {
								almacenarDatos.almacenarEntregablesCoaseguro(umic, detalleBaseTecnica, detallesCorriente,
										fichaproceso, totales);
							}
							
							if (fichaproceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_BTCOATF)) {
								TotalesFlujos totalesTirea = servicio.recuperarTotalesFlujos(umic, detalleBaseTecnica);
								almacenarDatos.almacenarEntregablesTirea(umic, detalleBaseTecnica, detallesCorriente,
										fichaproceso, totalesTirea);
							}
							
							if(null != umic.getDatosGenerales().getTipoSubriesgo() && umic.getDatosGenerales().getTipoSubriesgo().equals("UNIV")) {
                                List<Umic> umicsUniv = obtenerDatos.recuperarTodasUmicsUniv(umic.getDatosGenerales().getKmodalidad(),
                                         umic.getDatosGenerales().getKpoliza() ,umic.getDatosGenerales().getKsubpoliza());
                                int sizeUniv = 0;
                                Umic umicUniv = null;
                                for (int i=0; i <umicsUniv.size();i++) {
                                    if (umicDao.estaProcesada(umicsUniv.get(i).getKey())) {
                                        sizeUniv++;
                                    }
                                    if (umicsUniv.get(i).getDatosGenerales().getKgarantia().equals(10)) {
                                        umicUniv = umicsUniv.get(i);
                                    }
                                }
                                if (sizeUniv == umicsUniv.size() - 1 && umicUniv != null) {
                                	List<DetalleCorriente> detalleCorrienteUniv =  new ArrayList<DetalleCorriente>();
                                    ejecutor.buscarProceso(proceso, umicUniv, fichaproceso, detalleBaseTecnica, detalleCorrienteUniv,
                                            null, new Stack<String>());
                                    almacenarDatos.almacenarDetalleCorrienteEntregables(umicUniv, detalleBaseTecnica,
                                    		detalleCorrienteUniv, fichaproceso, false);
                                    almacenarDatos.almacenarTotalFlujos(totales);
                                    if (bt.equals(ConstantsModulos.CTE_VAL_ROSSP)){
                                    	for (int i=0; i <umicsUniv.size();i++) {
                                    		if (!polizasTipoDao.containsUmic(umicsUniv.get(i).getKey()) ) {
                                    			Filter filtroPart = new PartitionedFilter(detalleCorrienteDao.obtenerFiltroDetalles(umicsUniv.get(i).getKey()),
                                    					partsMember);
                                    			detalleCorrienteDao.filtrarDetalles(filtroPart);
                                    		}
                                    	}
                                    }
                                }       
                            }
							
							if (fichaproceso.getKuejecucion().equals(ConstantesSolvencia.CTE_UNID_EJEC_FLUJSUSCRI)){
								almacenarDatos.almacenarEntregableFlujSuscripciones(umic, detalleBaseTecnica, detallesCorriente,
										fichaproceso, totales);
							}
							
//								if (bt.equals(ConstantsModulos.CTE_VAL_SCRMFE) ||
//									bt.equals(ConstantsModulos.CTE_VAL_SCRMCF) ||
//									bt.equals(ConstantsModulos.CTE_VAL_SCRLFE) ||
//									bt.equals(ConstantsModulos.CTE_VAL_SCRTIU) ||
//									bt.equals(ConstantsModulos.CTE_VAL_SCRTID) ||
//									bt.equals(ConstantsModulos.CTE_VAL_SCRGTO) ||
//									bt.equals(ConstantsModulos.CTE_VAL_SCRAEP) ||
//									bt.equals(ConstantsModulos.CTE_VAL_SCRAEN) ||
//									bt.equals(ConstantsModulos.CTE_VAL_SCRANM) ||
//									bt.equals(ConstantsModulos.CTE_VAL_SCRINC) ||
//									bt.equals(ConstantsModulos.CTE_VAL_SCRMMI) ||
//									bt.equals(ConstantsModulos.CTE_VAL_SCRMCI) ||
//									bt.equals(ConstantsModulos.CTE_VAL_SCRVM) ||
//									bt.equals(ConstantsModulos.CTE_VAL_SCRLMI) ||
//									bt.equals(ConstantsModulos.CTE_VAL_SCRAIP) ||
//									bt.equals(ConstantsModulos.CTE_VAL_SCRAIN)) {
//									
//								List<DetalleCorriente> varProyC2S = obtenerDatos.recuperarProyeccionCualquierNodo(ConstantsModulos.CTE_VAL_BASE_BEL,
//										umic.getDatosGenerales().getFecCierre(), umic.getKey());
//								
//								if (null != varProyC2S && varProyC2S.size() > ConstantsFunciones.CTE_0) {
//									almacenarDatos.almacenarEntregablesFlujInfSCR(umic, detalleBaseTecnica,
//											detallesCorriente, varProyC2S);
//								}	
//							}						
							}
					} catch (Solvencia2Excepcion s2e) {
						Solvencia2ExcepcionHelper.checkIncidencia(bt, fichaproceso.getCcanal(),
								datosGeneralesUmic.getCcartera(), datosGeneralesUmic.getKey(),
								datosGeneralesUmic.getFecCierre(), datosGeneralesUmic.getCnegocio(),
								ConstantesSolvencia.CTE_PROYECCION, s2e);

						almacenarDatos.almacenarIncidencias(s2e.getIncidencia());

						// Dejamos de ejecutar esta UMIC
						break bucleBT;
					} catch (Throwable e) {

						log.error(e.getMessage(), e);

						Solvencia2Excepcion s2e = Solvencia2ExcepcionHelper.crearExcepcion(E00, new Object[] { e },
								ConstantesSolvencia.CTE_PROYECCION, bt.trim(), fichaproceso.getCcanal(), null, null,
								fichaproceso.getFefecto(), fichaproceso.getCnegocio(), e);

						almacenarDatos.almacenarIncidencias(s2e.getIncidencia());

						// Dejamos de ejecutar esta UMIC
						break bucleBT;
					}
					recuperarFlujo();
					if (log.isDebugEnabled()) {
						log.debug("Se ha procesado la umic " + index + " para BT " + bt + " en "
								+ (System.currentTimeMillis() - principioBt));
					}
				}

				if (log.isDebugEnabled()) {
					log.debug("Se ha procesado la umic " + index + " para todas las BTs en "
							+ (System.currentTimeMillis() - principioTodasBts));
				}

			} catch (Throwable e) {

				log.error(e.getMessage(), e);

				Solvencia2Excepcion s2e = Solvencia2ExcepcionHelper.crearExcepcion(E00, new Object[] { e },
						ConstantesSolvencia.CTE_PROYECCION, fichaproceso.getCtipobt(), fichaproceso.getCcanal(), null,
						null, fichaproceso.getFefecto(), fichaproceso.getCnegocio(), e);

				almacenarDatos.almacenarIncidencias(s2e.getIncidencia());
			}

			// Guardamos la Umic como procesada
			umicDao.marcarProcesada(umicKey);

			Integer mod330 = new Integer(330);
			Integer mod332 = new Integer(332);
			Integer mod412 = new Integer(412);
			Integer mod428 = new Integer(428);
			Boolean debeEliminarDetalleUmic = Boolean.FALSE;
			int cuentaUmicSecundarias = 0;

			// Si la constante de extracción es 0, se deben sacar todas las UMICs en
			// detalle, por lo que no se elimina
			// INI-823544
			// La constante de extracción no debe determinar que se extraigan las umics del
			// PTIPO,
			// ya que el PTIPO manual puede tener datos, y de esta manera no se estan
			// filtrando.
			// Solamente se filtraba cuando se generaba PTIPO Automatico. Se quita este if
			// if (cteExtr != 0){
			// FIN-823544
			// Eliminar el detalle corriente de umics que no estén en polizastipo y no sean
			// principales

			cuentaUmicSecundarias = obtenerDatos.recuperarUmicSecundarias(umic.getDatosGenerales().getKmodalidad(),
					umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
					umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(),
					umic.getDatosGenerales().getCtipoaport()).size();
			if (!polizasTipoDao.containsUmic(umic.getKey()) && !umic.getDatosGenerales().getSpcom().equals("P")) {
				debeEliminarDetalleUmic = Boolean.TRUE;
			} else if (umic.getDatosGenerales().getSpcom().equals("P") && !polizasTipoDao.containsUmic(umic.getKey())) {
				// Si es principal se almacena la clave de la UMIC para después eliminar el
				// detalle (sólo si tenemos UMIC no principales que puedan depender de ésta)
				// TODO Buscamos las UMIC secundarias de esta UMIC
				// int cuentaUmicSecundarias =
				// obtenerDatos.recuperarUmicSecundarias(umic.getDatosGenerales().getKmodalidad(),
				// umic.getDatosGenerales().getKpoliza(),
				// umic.getDatosGenerales().getKsubpoliza(),
				// umic.getDatosGenerales().getKcertificado(),
				// umic.getDatosGenerales().getNsuscri()).size();
				if (cuentaUmicSecundarias > 0) {
					umicsPrincipales.add(umicKey);
				} else if (null != umic.getDatosGenerales().getKbencon()) {
					if (((umic.getDatosGenerales().getKmodalidad().compareTo(mod330) == 0
							|| umic.getDatosGenerales().getKmodalidad().compareTo(mod332) == 0)
							&& umic.getDatosGenerales().getKbencon().equals("101")
							&& umic.getRentas().getCpagrenta().equals("4")) ||
					((umic.getDatosGenerales().getKmodalidad().compareTo(mod412) == 0
					|| umic.getDatosGenerales().getKmodalidad().compareTo(mod428) == 0)
					&& umic.getDatosGenerales().getKbencon().equals("TIT"))){
						umicsPrincipales.add(umicKey);
						debeEliminarDetalleUmic = Boolean.FALSE;
					}
//943655-INI
					else {
						debeEliminarDetalleUmic = Boolean.TRUE;
					}
//943655-FIN
				} else {
					debeEliminarDetalleUmic = Boolean.TRUE;
				}
			}
			
			if(null != umic.getDatosGenerales().getTipoSubriesgo() && umic.getDatosGenerales().getTipoSubriesgo().equals("UNIV")) {
				debeEliminarDetalleUmic = Boolean.FALSE;
			}

			// Una vez tomada la decisión de borrar el detalle, lo hacemos
			if (debeEliminarDetalleUmic) {
				Filter filtroPart = new PartitionedFilter(detalleCorrienteDao.obtenerFiltroDetalles(umic.getKey()),
						partsMember);
				detalleCorrienteDao.filtrarDetalles(filtroPart);
			}

			if (cuentaUmicSecundarias == 0) {
				terminosPMCUmicDao.remove(umicKey);
			}
			// INI-823544
			// }
			// FIN-823544

			// terminosPMCUmicDao.clear();
		}

		/*
		 * progreso.setTerminadas(index); progreso.setTotales(listaClavesUmic.size());
		 * 
		 * // oEnvironment.reportProgress((index*100)/listaClavesUmic.size() +
		 * "%. Se han procesado " + index + " de " + listaClavesUmic.size()); // Sólo
		 * enviamos progreso cada 10 UMIC, para no saturar if ((index-1) % 10 == 0 ||
		 * index == progreso.getTotales()) { oEnvironment.reportProgress(progreso); }
		 * index++;
		 */
	}

	public List<DetalleCorriente> ModificarDetallesCorriente(List<DetalleCorriente> detallesCorriente, Umic umic) {

		List<DetalleCorriente> detallecorrienteModificada = detallesCorriente;
		Timestamp F_aux = new Timestamp(new GregorianCalendar(0000, 1, 1).getTimeInMillis()),
				F_vida = new Timestamp(new GregorianCalendar(0000, 1, 1).getTimeInMillis());
		Iterator<DetalleCorriente> it_detcorr;
		DetalleCorriente det, aux = new DetalleCorriente();
		aux.setFechaDesde(new Timestamp(new GregorianCalendar(9999, 12, 31).getTimeInMillis()));
		/*
		 * it_detcorr=detallesCorriente.iterator(); // Recorremos detalle corriente y
		 * buscamos la fecha, fecha desde, en la que encontramos en el ImpProvi un cero.
		 * while(it_detcorr.hasNext()) { det = it_detcorr.next();
		 * if(det.getBloqueVida().getImpProvi().compareTo(BigDecimal.ZERO) == 0 &&
		 * det.getFechaDesde().before(aux.getFechaDesde())) { aux = det;
		 * F_aux=aux.getFechaDesde(); } }
		 */
		F_aux = umic.getBti().getFecFinTramo1();
		F_aux = UtilFechas.incrMeses(F_aux, null, -1, false);
		// Y nos quedamos con la fecha anterior al primer cero que encontramos.
		it_detcorr = detallesCorriente.iterator();
		while (it_detcorr.hasNext()) {
			det = it_detcorr.next();
			if (det.getFechaDesde().after(F_vida) && det.getFechaDesde().before(F_aux)) {
				aux = det;
				F_vida = aux.getFechaDesde();
			}
		}

		// Declaramos un valor para acumular el importe
		BigDecimal Importe_Provi = BigDecimal.ZERO;

		// Una vez que tenemos la fecha el siguiente paso es en detalle corriente
		// recorrer la columna sumcola, y el valor que sea superior a la fecha
		// anteriormente calculada coger ese dato y poner un cero en ese campo.
		// Recorremos corriente vida
		for (DetalleCorriente detalle : detallesCorriente) {
			if (detalle.getFechaDesde().after(F_vida)) {
				if (detalle.getTotalFlujoProyeccion().getSumcola() != null) {
					Importe_Provi = Importe_Provi.add(detalle.getTotalFlujoProyeccion().getSumcola());
				}
				detalle.getTotalFlujoProyeccion().setSumcola(BigDecimal.ZERO);
			}
		}

		// Volvemos a recorrer la columna sumcola y el dato que tenga en la fecha
		// anteriormente calculada le sumamos a ese dato el importe calculado.
		for (DetalleCorriente detalle : detallesCorriente) {
			if (detalle.getFechaDesde().equals(F_vida)) {
				if (detalle.getTotalFlujoProyeccion().getSumcola() != null) {
					Importe_Provi = Importe_Provi.add(detalle.getTotalFlujoProyeccion().getSumcola());
				}
				detalle.getTotalFlujoProyeccion().setSumcola(Importe_Provi);
			}
		}

		return detallecorrienteModificada;
	}

	private void recuperarFlujo() {
		FlujosProbablesDao fpDao = new FlujosProbablesDao();
		J880GBT003ModuloTablaRealista gbt003 = J880GBT003ModuloTablaRealista.getInstance();
		J880GBT008TablasExperienciaBEL gbt008 = J880GBT008TablasExperienciaBEL.getInstance();
		J880GBT009CurvasInteresBEL gbt009 = J880GBT009CurvasInteresBEL.getInstance();
		J880GBT010GastosAdministracionBEL gbt010 = J880GBT010GastosAdministracionBEL.getInstance();
		J880GBT011TasasAnulacionBEL gbt011 = J880GBT011TasasAnulacionBEL.getInstance();

		J880GBT004ModuloTablaTradicional gbt004 = J880GBT004ModuloTablaTradicional.getInstance();
		J880GBT005TablasExperienciaROSSP gbt005 = J880GBT005TablasExperienciaROSSP.getInstance();
		J880GBT006TiposInteresROSSP gbt006 = J880GBT006TiposInteresROSSP.getInstance();
		J880GBT007GastosAdministracionROSSP gbt007 = J880GBT007GastosAdministracionROSSP.getInstance();
		J880GBT012CurvasCINIIF17 gbt012 = J880GBT012CurvasCINIIF17.getInstance();

		Orquestador orqst = Orquestador.getInstance();

		FlujosProbables fpclone3 = gbt003.getFpClone();
		FlujosProbables fpclone8 = gbt008.getFpClone();
		FlujosProbables fpclone9 = gbt009.getFpClone();
		FlujosProbables fpclone10 = gbt010.getFpClone();
		FlujosProbables fpclone11 = gbt011.getFpClone();

		FlujosProbables fpclone4 = gbt004.getFpClone();
		FlujosProbables fpclone5 = gbt005.getFpClone();
		FlujosProbables fpclone6 = gbt006.getFpClone();
		FlujosProbables fpclone7 = gbt007.getFpClone();
		FlujosProbables fpclone12 = gbt012.getFpClone();
		FlujosProbables fpcloneOrq = orqst.getFpClone();

		if (null != fpclone3) {
			// FlujosProbables fp = fpDao.get(fpclone3.getKey());
			fpDao.put(fpclone3.getKey(), fpclone3);
		}
		if (null != fpclone8) {
			// FlujosProbables fp = fpDao.get(fpclone8.getKey());
			fpDao.put(fpclone8.getKey(), fpclone8);
		}
		if (null != fpclone9) {
			// FlujosProbables fp = fpDao.get(fpclone9.getKey());
			fpDao.put(fpclone9.getKey(), fpclone9);
		}
		if (null != fpclone10) {
			// FlujosProbables fp = fpDao.get(fpclone10.getKey());
			fpDao.put(fpclone10.getKey(), fpclone10);
		}
		if (null != fpclone11) {
			// FlujosProbables fp = fpDao.get(fpclone11.getKey());
			fpDao.put(fpclone11.getKey(), fpclone11);
		}
		if (null != fpclone4) {
			// FlujosProbables fp = fpDao.get(fpclone4.getKey());
			fpDao.put(fpclone4.getKey(), fpclone4);
		}
		if (null != fpclone5) {
			// FlujosProbables fp = fpDao.get(fpclone5.getKey());
			fpDao.put(fpclone5.getKey(), fpclone5);
		}
		if (null != fpclone6) {
			// FlujosProbables fp = fpDao.get(fpclone6.getKey());
			fpDao.put(fpclone6.getKey(), fpclone6);
		}
		if (null != fpclone7) {
			// FlujosProbables fp = fpDao.get(fpclone7.getKey());
			fpDao.put(fpclone7.getKey(), fpclone7);
		}
		if (null != fpclone12) {
			// FlujosProbables fp = fpDao.get(fpclone12.getKey());
			fpDao.put(fpclone12.getKey(), fpclone12);
		}
		if (null != fpcloneOrq) {
			// FlujosProbables fp = fpDao.get(fpcloneOrq.getKey());
			fpDao.put(fpcloneOrq.getKey(), fpcloneOrq);
		}

		J880GBT003ModuloTablaRealista.setNullInstance();
		J880GBT008TablasExperienciaBEL.setNullInstance();
		J880GBT009CurvasInteresBEL.setNullInstance();
		J880GBT008TablasExperienciaBEL.setNullInstance();
		J880GBT010GastosAdministracionBEL.setNullInstance();
		J880GBT011TasasAnulacionBEL.setNullInstance();
		J880GBT004ModuloTablaTradicional.setNullInstance();
		J880GBT005TablasExperienciaROSSP.setNullInstance();
		J880GBT006TiposInteresROSSP.setNullInstance();
		J880GBT007GastosAdministracionROSSP.setNullInstance();
		J880GBT012CurvasCINIIF17.setNullInstance();
		Orquestador.setNullInstance();

	}

}
