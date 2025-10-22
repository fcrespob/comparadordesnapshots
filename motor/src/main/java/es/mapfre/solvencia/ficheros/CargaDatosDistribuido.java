package es.mapfre.solvencia.ficheros;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.coherence.patterns.processing.task.TaskExecutionEnvironment;
import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dao.Dao;
import es.mapfre.solvencia.dao.services.FactoriaDao;
import es.mapfre.solvencia.files.FileDescriptor;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;
@Portable
public class CargaDatosDistribuido implements TareaDistribuida{
	
	
	private static Logger log = LoggerFactory.getLogger(CargaDatosDistribuido.class);
	
	@PortableProperty(0)private String fecCierre;
	@PortableProperty(1)private String rutaBase;
	@PortableProperty(2)private String nombrefic;
	@PortableProperty(3)private String streamName;
	@PortableProperty(4)private String beanioConfigXml;
	
	@Override
	public Object run(TaskExecutionEnvironment oEnvironment) {
		
		BeanIOReader reader = null;
		try {
			String filePath = rutaBase + File.separator + fecCierre + nombrefic;
			reader = new BeanIOReader(beanioConfigXml,filePath, streamName);
			Dao dao = FactoriaDao.getDao(streamName);
			if (dao == null) {
				log.error("No existe ninguna implementación para el DAO de {}", streamName);
				throw new RuntimeException("No existe ninguna implementación para el DAO de " + streamName);
			}
			dao.loadCache(reader);
			if(log.isDebugEnabled()){
				log.debug("Cargado el fichero "+ nombrefic);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					log.error("Error cerrando reader {}", streamName, e);
				}
			}
		}
		
		return null;
		
	}

	public CargaDatosDistribuido(String fecCierre, String rutaBase,
			String nombrefic, String streamName, String beanioConfigXml) {
		super();
		this.fecCierre = fecCierre;
		this.rutaBase = rutaBase;
		this.nombrefic = nombrefic;
		this.streamName = streamName;
		this.beanioConfigXml = beanioConfigXml;
	}

	public CargaDatosDistribuido(String fecCierre, String rutaBase,
			FileDescriptor fileDescriptor) {
		super();
		this.fecCierre = fecCierre;
		this.rutaBase = rutaBase;
		this.nombrefic = fileDescriptor.getFilePath();
		this.streamName = fileDescriptor.getStreamName();
		this.beanioConfigXml = fileDescriptor.getBeanioConfigXml();
	}

	public CargaDatosDistribuido() {
		super();
	}

	public String getNombre() {
		return nombrefic;
	}
	
	
	
}
