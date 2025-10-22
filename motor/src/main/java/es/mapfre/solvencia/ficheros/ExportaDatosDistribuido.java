/* MODIFICACION:TAR00302248-Contabilidad SolvenciaII en SSAA 
   FECHA: 25/09/2017
   AUTOR: INDRA
 */
package es.mapfre.solvencia.ficheros;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.coherence.patterns.processing.task.TaskExecutionEnvironment;
import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;
import com.tangosol.net.Member;

import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dao.DaoSalidaCalculo;
import es.mapfre.solvencia.dao.services.FactoriaDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;
import es.mapfre.solvencia.utils.beanio.BeanIOWriter;

@Portable
public class ExportaDatosDistribuido implements TareaDistribuida {

	private static final String GUION_BAJO = "_";

	private static final int IND_FICHA = 0;
	private static final int IND_RUTABASE = 1;
	private static final int IND_NOMBREF = 2;
	private static final int IND_STREAMNAME = 3;
	private static final int IND_BEANIOCONFIGXML = 4;
	private static final int IND_BTS = 5;
	private static final int IND_FORMATOFICHERO = 6;
	private static final int IND_PARTICIONADO = 7;
	private static final int IND_TIPO = 8;
	private static final int IND_MAPENTREGABLES = 9;
	
	private static Logger log = LoggerFactory.getLogger(ExportaDatosDistribuido.class);
	
	@PortableProperty(IND_FICHA)
	private FichaProceso ficha;
	@PortableProperty(IND_RUTABASE)
	private String rutaBase;
	@PortableProperty(IND_NOMBREF)
	private String nombref;
	@PortableProperty(IND_STREAMNAME)
	private String streamName;
	@PortableProperty(IND_BEANIOCONFIGXML)
	private String beanioConfigXml;
	@PortableProperty(IND_BTS)
	private List<String> bts;
	@PortableProperty(IND_FORMATOFICHERO)
	private String formatoFichero;
	@PortableProperty(IND_PARTICIONADO)
	private Boolean exportarParticionado;
	@PortableProperty(IND_TIPO)
	private Integer tipo;
	
	//Redimensionamiento entregables DIP0
	@PortableProperty(IND_MAPENTREGABLES)
	private Map<String, List<String>> mapEntregables;
	
	private static final String FORMATO_TXT = "TXT";
	private static final String FORMATO_CSV = "csv";
	
	private IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();

	public ExportaDatosDistribuido(FichaProceso ficha, String rutaBase, String nombref,
			String streamName, String beanioConfigXml, List<String> bts, String formatoFichero, Boolean exportarParticionado, Integer tipo, Map<String, List<String>> mapEntregables) {
		super();
		this.ficha = ficha;
		this.rutaBase = rutaBase;
		this.nombref = nombref;
		this.streamName = streamName;
		this.beanioConfigXml = beanioConfigXml;
		this.bts = bts;
		this.formatoFichero = formatoFichero != null ? formatoFichero : FORMATO_TXT;
		this.exportarParticionado = exportarParticionado;
		this.tipo = tipo;
		this.mapEntregables = mapEntregables;
	}

	public ExportaDatosDistribuido(FichaProceso ficha, String rutaBase, String nombref,
			String streamName, String beanioConfigXml, List<String> bts, String formatoFichero, Integer tipo, Map<String, List<String>> mapEntregables) {
		this.ficha = ficha;
		this.rutaBase = rutaBase;
		this.nombref = nombref;
		this.streamName = streamName;
		this.beanioConfigXml = beanioConfigXml;
		this.bts = bts;
		this.formatoFichero = formatoFichero != null ? formatoFichero : FORMATO_TXT;
	/*INI-TAR00302248*/	
      /*this.formatoFichero = formatoFichero != null ? formatoFichero : FORMATO_TXT;*/
		if (nombref.equals("CONTAB") || nombref.equalsIgnoreCase("CONTABC") || nombref.equalsIgnoreCase("flujinf3") || nombref.equalsIgnoreCase("flujinf4") || nombref.equalsIgnoreCase("flujinfscr") || nombref.equalsIgnoreCase("flujototpv") || nombref.equalsIgnoreCase("flujostn17") || nombref.equalsIgnoreCase("pesosbtproxy") || nombref.equalsIgnoreCase("patroncsm")) { 
			this.formatoFichero = FORMATO_TXT;
		} else if (nombref.equals("FPSL")){
			this.formatoFichero = FORMATO_CSV;
		} else {
			this.formatoFichero = formatoFichero != null ? formatoFichero : FORMATO_TXT;
		}
	/*FIN-TAR00302248*/
		this.exportarParticionado = Boolean.FALSE;
		this.tipo = tipo;
		this.mapEntregables = mapEntregables;
	}

	@Override
	public Object run(TaskExecutionEnvironment oEnvironment) {
		
		Exception exception = null;
		try {
			oEnvironment.reportProgress("0%");
			
			Map<String,BeanIOWriter> outs = new HashMap<String, BeanIOWriter>();
			
			DaoSalidaCalculo dao = (DaoSalidaCalculo) FactoriaDao.getDao(streamName);
			if (dao != null) {

				String fefecto = new SimpleDateFormat("yyyyMMdd").format(ficha.getFefecto());
		
				for(String bt : bts) {
					//Redimensionamiento entregables
					List<String> entregablesBt = mapEntregables.get(bt);
//					if (entregablesBt.indexOf(nombref) == -1 || 
//							((this.nombref.contains(ConstantsFactorias.ENTREGABLE_FLUJINF3) ||
//									this.nombref.contains(ConstantsFactorias.ENTREGABLE_FLUJINF4) ||
//									this.nombref.contains(ConstantsFactorias.ENTREGABLE_FPSL)) &&
//									(!bt.equals(ConstantesSolvencia.BASE_NIIF17) &&
//											!bt.equals(ConstantesSolvencia.BASE_N17LIRIN) &&
//											!bt.equals(ConstantesSolvencia.BASE_NIIF17IF) ))) {
//						continue;
//					}		
					
					if (entregablesBt.indexOf(nombref) == -1 || 
							((this.nombref.contains(ConstantsFactorias.ENTREGABLE_FLUJINF3) ||
									this.nombref.contains(ConstantsFactorias.ENTREGABLE_FPSL)) &&
									(!bt.equals(ConstantesSolvencia.BASE_NIIF17) &&
											!bt.equals(ConstantesSolvencia.BASE_N17LIRIN) &&
											!bt.equals(ConstantesSolvencia.BASE_NIIF17IF)))) {
						continue;
					}
					
					if (entregablesBt.indexOf(nombref) == -1 || 
							((this.nombref.contains(ConstantsFactorias.ENTREGABLE_FLUJINF4)
									|| this.nombref.contains(ConstantsFactorias.ENTREGABLE_FLUJINFSCR)) &&
									(!bt.equals(ConstantesSolvencia.BASE_NIIF17) &&
											!bt.equals(ConstantesSolvencia.BASE_N17LIRIN) &&
											!bt.equals(ConstantesSolvencia.BASE_NIIF17IF) &&
											!bt.equals(ConstantesSolvencia.BASE_BEL) &&
											!bt.equals(ConstantesSolvencia.BASE_SCRTIU)&& 
											!bt.equals(ConstantesSolvencia.BASE_SCRTID) && 
											!bt.equals(ConstantesSolvencia.BASE_SCRGTO) && 
											!bt.equals(ConstantesSolvencia.BASE_SCRMFE)&& 
											!bt.equals(ConstantesSolvencia.BASE_SCRMMI) && 
											!bt.equals(ConstantesSolvencia.BASE_SCRMCF)&& 
											!bt.equals(ConstantesSolvencia.BASE_SCRMCI) && 
											!bt.equals(ConstantesSolvencia.BASE_SCRLFE)&& 
											!bt.equals(ConstantesSolvencia.BASE_SCRLMI) && 
											!bt.equals(ConstantesSolvencia.BASE_SCRINC)&& 
											!bt.equals(ConstantesSolvencia.BASE_SCRVM) && 
											!bt.equals(ConstantesSolvencia.BASE_SCRAEN)&& 
											!bt.equals(ConstantesSolvencia.BASE_SCRAEP) && 
											!bt.equals(ConstantesSolvencia.BASE_SCRAIN)&& 
											!bt.equals(ConstantesSolvencia.BASE_SCRAIP) && 
											!bt.equals(ConstantesSolvencia.BASE_SCRANM)&& 
											!bt.equals(ConstantesSolvencia.BASE_SCR)))) {
						continue;
					}
					

					String totalpath = rutaBase;

					String nombreFichero = nombreFicheroSalida(tipo, bt, fefecto, dao);

					totalpath = totalpath.concat(File.separator).concat(ficha.getCcanal().toString());

					if (streamName.equals(ConstantesSolvencia.STREAM_PESOSBTPROXY)) {
						totalpath = rutaBase.concat(File.separator).concat("PROXY");
					}else if(ficha.getCnegocio().equals(ConstantesSolvencia.NEGOCIO_INDIVIDUAL)){
						totalpath = totalpath.concat(File.separator).concat("BRUTOSIND");
					}else if(ficha.getCnegocio().equals(ConstantesSolvencia.NEGOCIO_COLECTIVO)){
						totalpath = totalpath.concat(File.separator).concat("BRUTOSCOL");
					}
					
					if (streamName.equals("swcobrocom")) {
						nombreFichero = "SWCOBROCOM.TXT";
						//totalpath = "/mnt/solv2vida/CIERRES" + File.separator + ConstantesSolvencia.CATALOGOS;
						//totalpath=rutaBase.substring(0, rutaBase.lastIndexOf(File.separator)) + File.separator + "CATALOGOS";
						totalpath = rutaBase + File.separator + "CTEC";
					} 
					
					if (streamName.equals("swcobrocomcsv")) {
						nombreFichero = "SWCOBROCOM.CSV";
						//totalpath=rutaBase.substring(0, rutaBase.lastIndexOf(File.separator)) + File.separator + "CATALOGOS";
						totalpath = rutaBase + File.separator + "CTEC";
					} 

					if(log.isInfoEnabled()){
						log.info("escribiendo " + totalpath + File.separator + nombreFichero);
					}

					/*INIO-TAR00302248*/
					if (streamName.equals("contab") || streamName.equalsIgnoreCase("contabc") ) {
						totalpath = rutaBase.concat(File.separator).concat("CONTAB");
					}
					/*FIN-TAR00302248*/

					new File(totalpath).mkdirs();
					new File(totalpath + File.separator + nombreFichero);


					outs.put(bt, new BeanIOWriter(beanioConfigXml, totalpath + File.separator + nombreFichero, streamName));

				}
				// Generamos los ficheros en disco
				dao.exportCachePorBT(outs, ficha);
			}
			
			for(String bt : bts) {
				//Rredimensionamiento entregables
				List<String> entregablesBt = mapEntregables.get(bt);
				if (entregablesBt.indexOf(nombref) == -1 || 
						((this.nombref.contains(ConstantsFactorias.ENTREGABLE_FLUJINF3) ||
								this.nombref.contains(ConstantsFactorias.ENTREGABLE_FPSL)) &&
								(!bt.equals(ConstantesSolvencia.BASE_NIIF17) &&
										!bt.equals(ConstantesSolvencia.BASE_N17LIRIN) &&
										!bt.equals(ConstantesSolvencia.BASE_NIIF17IF)))) {
					continue;
				}
				
				if (entregablesBt.indexOf(nombref) == -1 || 
						((this.nombref.contains(ConstantsFactorias.ENTREGABLE_FLUJINF4)
								|| this.nombref.contains(ConstantsFactorias.ENTREGABLE_FLUJINFSCR)) &&
								(!bt.equals(ConstantesSolvencia.BASE_NIIF17) &&
										!bt.equals(ConstantesSolvencia.BASE_N17LIRIN) &&
										!bt.equals(ConstantesSolvencia.BASE_NIIF17IF) &&
										!bt.equals(ConstantesSolvencia.BASE_BEL) &&
										!bt.equals(ConstantesSolvencia.BASE_SCRTIU)&& 
										!bt.equals(ConstantesSolvencia.BASE_SCRTID) && 
										!bt.equals(ConstantesSolvencia.BASE_SCRGTO) && 
										!bt.equals(ConstantesSolvencia.BASE_SCRMFE)&& 
										!bt.equals(ConstantesSolvencia.BASE_SCRMMI) && 
										!bt.equals(ConstantesSolvencia.BASE_SCRMCF)&& 
										!bt.equals(ConstantesSolvencia.BASE_SCRMCI) && 
										!bt.equals(ConstantesSolvencia.BASE_SCRLFE)&& 
										!bt.equals(ConstantesSolvencia.BASE_SCRLMI) && 
										!bt.equals(ConstantesSolvencia.BASE_SCRINC)&& 
										!bt.equals(ConstantesSolvencia.BASE_SCRVM) && 
										!bt.equals(ConstantesSolvencia.BASE_SCRAEN)&& 
										!bt.equals(ConstantesSolvencia.BASE_SCRAEP) && 
										!bt.equals(ConstantesSolvencia.BASE_SCRAIN)&& 
										!bt.equals(ConstantesSolvencia.BASE_SCRAIP) && 
										!bt.equals(ConstantesSolvencia.BASE_SCRANM)&& 
										!bt.equals(ConstantesSolvencia.BASE_SCR)))) {
					continue;
				}
				outs.get(bt).flush();
				outs.get(bt).close();
			}
			
			oEnvironment.reportProgress("100%");
		
		} catch (Exception e) {
			
			log.error(e.getMessage(),e);
			//TODO ¿qué base técnica lleva este error?
			Solvencia2Excepcion s2e = Solvencia2ExcepcionHelper.crearExcepcion("00",
					new Object[]{e}, ficha.getKprotecnico(), ficha.getCtipobt(),
					ficha.getCcanal(), null, null, ficha.getFefecto(),
					ficha.getCnegocio(), e);

			almacenarDatos.almacenarIncidencias(s2e.getIncidencia());
			exception = e;
		}
		
		return exception;
	}


	public ExportaDatosDistribuido() {
		super();
	}


	public String getNombre() {
		return nombref;
	}
	
	public String nombreFicheroSalida(Integer tipo, String bt, String fefecto, DaoSalidaCalculo dao ) {
		
		StringBuilder nombreFicheroSB = new StringBuilder();
		
		if (tipo == ConstantsFunciones.CTE_1) {	// PROYECCIONES
			nombreFicheroSB.append(ficha.getKsistema()).append(GUION_BAJO)
			.append(ficha.getKejecucion()).append(GUION_BAJO)
			.append(ficha.getKprotecnico()).append(GUION_BAJO) 
			.append(ficha.getKuejecucion()).append(GUION_BAJO)
			.append(ficha.getKsecuencia()).append(GUION_BAJO)
			.append(fefecto).append(GUION_BAJO)
			.append(ficha.getCnegocio()).append(GUION_BAJO)
			.append(ficha.getCcanal()).append(GUION_BAJO)
			.append(bt).append(GUION_BAJO)
			.append(nombref).append(GUION_BAJO);
		
			// 	Miramos si es una exportación distribuida
			if (this.exportarParticionado) {
				Member localMember = ((DaoBaseSalidaCalculo) dao).getCache().getCacheService().getCluster().getLocalMember();
				String nombreMaquina = localMember.getMachineName();
				String nombreMiembro = localMember.getMemberName();
				nombreFicheroSB.append(".").append(nombreMaquina).append(GUION_BAJO).append(nombreMiembro).append(GUION_BAJO);
				//	nombreFicheroSB.append(this.getNombre()).append(GUION_BAJO);
			}
		} else if (tipo == ConstantsFunciones.CTE_2) { // ENTREGABLES
			if (streamName.equals(ConstantesSolvencia.STREAM_PESOSBTPROXY)) {
				nombreFicheroSB.append(fefecto).append(GUION_BAJO)
				.append(String.format("%05d", ficha.getCcanal())).append(GUION_BAJO)
				.append(ficha.getCnegocio()).append(GUION_BAJO)
				.append(nombref);
			} else {
				nombreFicheroSB.append(fefecto).append(GUION_BAJO)
				.append(String.format("%05d", ficha.getCcanal())).append(GUION_BAJO)
				.append(ficha.getCnegocio()).append(GUION_BAJO)
				.append(bt).append(GUION_BAJO)
				.append(nombref);
			}
		} else if (tipo == ConstantsFunciones.CTE_3) { // PTIPO
			nombreFicheroSB.append(nombref).append(GUION_BAJO)
			.append(fefecto).append(GUION_BAJO)
			.append(ficha.getCnegocio()).append(GUION_BAJO)
			.append(bt);
		} else if (tipo == ConstantsFunciones.CTE_4) { // ENTREGABLE SWCOBROCOMISIONES
			nombreFicheroSB.append("SWCOBROCOM"); 
		} else if (tipo == ConstantsFunciones.CTE_5) { // ENTREGABLE INCIDENCIASMAESTRO
			nombreFicheroSB.append(fefecto).append(GUION_BAJO)
			.append(String.format("%05d", ficha.getCcanal())).append(GUION_BAJO)
			.append(ficha.getCnegocio()).append(GUION_BAJO)
			.append(nombref).append("CargaUMICS"); 
		}
		
		nombreFicheroSB.append(".").append(formatoFichero);
		
		return nombreFicheroSB.toString();
	}
	
	@Override
	public String toString() {
		return getNombre();
	}
}
