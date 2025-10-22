package es.mapfre.solvencia.utils.beanio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipInputStream;

import org.beanio.BeanIOException;
import org.beanio.BeanReader;
import org.beanio.BeanReaderErrorHandlerSupport;
import org.beanio.InvalidRecordException;
import org.beanio.StreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;

public class BeanIOReader {
	private static final Logger log = LoggerFactory.getLogger(BeanIOReader.class);

	private InputStream fis = null;
	private InflaterInputStream zs = null;
	private Reader isr = null;

	private StreamFactory factory = null;
	private BeanReader reader = null;

	private Boolean failOnError = Boolean.FALSE;

	public Boolean getFailOnError() {
		return failOnError;
	}

	public void setFailOnError(Boolean failOnError) {
		this.failOnError = failOnError;
	}
	
	private List errores = new ArrayList<>();
	
	public List getErrores() {
		return errores;
	}

	/**
	 * Constructor de la clase
	 * 
	 * @param configFilePath
	 *            Ruta al fichero de configuración de BeanIO
	 * @param filePath
	 *            Ruta al fichero a parsear
	 * @param streamName
	 *            Nombre del stream que se va a tratar
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public BeanIOReader(String configFilePath, String filePath, String streamName) throws IOException {
		factory = StreamFactory.newInstance();
		try {
			factory.loadResource(configFilePath);
		} catch (BeanIOException bioe) {
			if (bioe.getCause() != null && bioe.getCause() instanceof java.io.FileNotFoundException) {
				factory.load("classpath:" + configFilePath);
			} else {
				throw bioe;
			}
		}

		if (filePath != null && streamName != null) {
			this.createReader(filePath, streamName);
		}
	}

	/**
	 * Método empleado para iniciar un lector o resetear uno existente para
	 * parsear un nuevo fichero con la configuración previamente cargada
	 * 
	 * @param filePath
	 *            Ruta al fichero a parsear
	 * @param streamName
	 *            Nombre del stream que se va a tratar
	 * @throws IOException
	 */
	public void createReader(String filePath, String streamName) throws IOException {
		createReader(filePath, streamName, ConstantesSolvencia.FILE_CHARSET);
	}
	
	/**
	 * Método empleado para iniciar un lector o resetear uno existente para
	 * parsear un nuevo fichero con la configuración previamente cargada
	 * 
	 * @param filePath
	 *            Ruta al fichero a parsear
	 * @param streamName
	 *            Nombre del stream que se va a tratar
	 * @throws IOException
	 */
	public void createReader(String filePath, String streamName, String charsetName) throws IOException {
		this.close();

		Boolean isCompressed = Boolean.FALSE;

		try {
			fis = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			fis = ClassLoader.getSystemResourceAsStream(filePath);
			if (fis == null) {
				throw new FileNotFoundException("No se encuentra el fichero " + filePath);
			}
		}
		if (filePath.endsWith(".gz")) {
			zs = new GZIPInputStream(fis);
			isCompressed = true;
		} else if (filePath.endsWith(".zip")) {
			zs = new ZipInputStream(fis);
			isCompressed = true;
		}
		if (charsetName == null) {
			charsetName = ConstantesSolvencia.FILE_CHARSET;
		}
		isr = new InputStreamReader(isCompressed ? zs : fis, charsetName);
		reader = factory.createReader(streamName, isr); // NOSONAR

		reader.setErrorHandler(new BeanReaderErrorHandlerSupport() {
			public void invalidRecord(InvalidRecordException ex) {
				log.error("Error en lectura de registro: {}, {}", ex.getLocalizedMessage(), ex.getRecordContext()
						.getFieldErrors());
				errores.add(ex);
				if (failOnError) {
					throw ex;
				}
			}
		});
	}

	/**
	 * Método empleado para leer línea a línea los registros de un fichero.
	 * 
	 * @return El objeto creado a partir del registro leído.
	 */
	public Object read() {
		return reader.read();
	}

	/**
	 * Cierra todos los handlers de fichero abiertos.
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		try {
			if (reader != null) {
				reader.close();
			}
			if (isr != null) {
				isr.close();
			}
			if (zs != null) {
				zs.close();
			}
			if (fis != null) {
				fis.close();
			}
		} catch (Exception e) {
			log.warn("{}", e.getMessage());
		}
		if (errores != null) {
			errores.clear();
		}
	}
}
