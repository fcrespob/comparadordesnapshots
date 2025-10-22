package es.mapfre.solvencia.ficheros;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.coherence.patterns.processing.task.TaskExecutionEnvironment;
import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.impl.maestro.UmicDao;
import es.mapfre.solvencia.dao.impl.maestro.UmicsBoteDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;
@Portable
public class CargaUmicsDistribuido implements TareaDistribuida{
	
	
	private static Logger log = LoggerFactory.getLogger(CargaUmicsDistribuido.class);
	private IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
	
	
	@PortableProperty(0)private String fecCierre;
	@PortableProperty(1)private String rutaBase;
	@PortableProperty(2)private String nombrefic;
	@PortableProperty(3)private String streamName;
	@PortableProperty(4)private String beanioConfigXml;
	@PortableProperty(5)private Integer batchSizes;
	@PortableProperty(6)private Collection<FichaProceso> fichas;
	//@PortableProperty(7)private Collection<UmicKey> umicsBote;
	
	@Override
	public Object run(TaskExecutionEnvironment oEnvironment) {
		
		Integer contaUmics = 0;
		try {
			
			
			String filePath = rutaBase + File.separator + fecCierre + nombrefic;
			BeanIOReader inUmic;
			inUmic = new BeanIOReader(beanioConfigXml,filePath, streamName);
			UmicDao umicDao = new UmicDao();
			Map<UmicKey, Umic> valores = new HashMap<UmicKey, Umic>();
			Umic umic = null;
			UmicsBoteDao umicsBoteDao = new UmicsBoteDao();
			boolean incluir;
			
			while ((umic = (Umic) inUmic.read()) != null) {
				incluir = false;
				for (FichaProceso ficha : fichas) {
					if(ConstantesSolvencia.CTIPOEJEC_BOTE.equals(ficha.getRegistroParametros().getIndicadorTipoProceso())) {
						// Si la ficha es de tipo BOTE se comprueba que la umic se encuentre en el BOTE, en este caso se incluye
						if (umicsBoteDao.containsKey(umic.getKey())){
							incluir = true;
						}
					} else {
						//En caso de que no sea BOTE, se incluye la umic
						incluir = true;
					}
					
					//Se comprueba que la umic incluída sea del mismo canal y negocio que la ficha, así como que la fecha de la UMIC y la fecha de cierre sean iguales
					//, solo en este caso se carga
					if (incluir && ficha.getCcanal().equals(umic.getDatosGenerales().getCcanal()) 
								&& ficha.getCnegocio().equals(umic.getDatosGenerales().getCnegocio())
								&& ficha.getFefecto().equals(umic.getDatosGenerales().getFecCierre())) { 
						valores.put(umic.getKey(), umic);
						contaUmics++;
						break;// es suficiente si se cumple para una ficha de proceso
					}
				}
				
				if (contaUmics % batchSizes == 0) {
					umicDao.putAll(valores);
					valores.clear();
				}
				
				if(log.isDebugEnabled() && contaUmics > 0 && contaUmics % batchSizes == 0){
					log.debug(contaUmics + "del fichero "+nombrefic);
				}	
			}
			
			if(contaUmics > 0){
				umicDao.putAll(valores);
				valores.clear();	
			}
			
			if (!inUmic.getErrores().isEmpty()) {
				umic = (Umic) inUmic.read();
				almacenarDatos.almacenarIncidenciasMaestro(umic, inUmic.getErrores().toString());
			}
			
			if(log.isInfoEnabled()){
				log.info("CARGADAS "+ contaUmics + " UMICS del fichero "+nombrefic);
			}	
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return contaUmics;
		
	}

	public CargaUmicsDistribuido(String fecCierre, String rutaBase,
			String nombrefic, String streamName, String beanioConfigXml,
			Collection<FichaProceso> fichas, Integer batchSizes) {
		super();
		this.fecCierre = fecCierre;
		this.rutaBase = rutaBase;
		this.nombrefic = nombrefic;
		this.streamName = streamName;
		this.beanioConfigXml = beanioConfigXml;
		this.fichas = fichas;
		//this.umicsBote = umicsBote;
		this.batchSizes = batchSizes;
	}



	public CargaUmicsDistribuido() {
		super();
	}

	public String getNombre() {
		return nombrefic;
	}
	
	
	
}
