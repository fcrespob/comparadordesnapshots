package es.mapfre.solvencia.ficheros;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.coherence.patterns.processing.task.TaskExecutionEnvironment;
import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dao.services.FactoriaDao;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.utils.beanio.BeanIOWriter;

@Portable
public class ExportaFichaResultado implements TareaDistribuida {

	private static Logger log = LoggerFactory.getLogger(ExportaFichaResultado.class);
	
	private static final int IND_FICHA = 0;
	private static final int IND_RUTABASE = 1;
	private static final int IND_NOMBREF = 2;
	private static final int IND_STREAMNAME = 3;
	private static final int IND_BEANIOCONFIGXML = 4;
	
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
	
	private IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
	
	public ExportaFichaResultado(FichaProceso ficha, String rutaBase, String nombref,
			String streamName, String beanioConfigXml) {
		this.ficha = ficha;
		this.rutaBase = rutaBase;
		this.nombref = nombref;
		this.streamName = streamName;
		this.beanioConfigXml = beanioConfigXml;
		
	}


	@Override
	public Object run(TaskExecutionEnvironment oEnvironment) {
		
		oEnvironment.reportProgress("0%");
		Exception exception = null;
		
		try {
			String totalpath = rutaBase;
			
			
			//NAS/CIERRES/aaaamm/FICHAS/FICHASPEND
			totalpath = totalpath.concat(File.separator).concat(nombref);
			
			if(log.isInfoEnabled()){
				log.info("escribiendo " + totalpath );
			}
			
			BeanIOWriter writer = new BeanIOWriter(beanioConfigXml, totalpath, streamName);
			
			DaoBase dao = (DaoBase) FactoriaDao.getDao(streamName);
			dao.exportCache(writer);
			
			writer.flush();
			writer.close();
			
		} catch (Exception e) {
			
			log.error(e.getMessage(),e);
			//TODO ¿qué base técnica lleva este error?
			Solvencia2Excepcion s2e = Solvencia2ExcepcionHelper.crearExcepcion("00",
					new Object[]{e}, ficha.getKprotecnico(), ficha.getCtipobt(),
					ficha.getCcanal(), null, null, ficha.getFefecto(),
					ficha.getCnegocio(), e);

			almacenarDatos.almacenarIncidencias(s2e.getIncidencia());
		}
		
		oEnvironment.reportProgress("100%");
		
		return exception;
	}


	public ExportaFichaResultado() {
		super();
	}
		
	public String getNombre() {
		return nombref;
	}

}
