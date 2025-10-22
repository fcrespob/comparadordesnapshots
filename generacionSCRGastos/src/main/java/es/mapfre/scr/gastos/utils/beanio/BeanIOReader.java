package es.mapfre.scr.gastos.utils.beanio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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

import es.mapfre.scr.gastos.utils.ConstantesSolvencia;

public class BeanIOReader {
	private static final Logger log = LoggerFactory.getLogger(BeanIOReader.class);

	private FileInputStream fis = null;
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
			try {
				this.createReader(filePath, streamName);
			} catch (IOException e) {
				IOException exc = new IOException("No se ha encontrado el fichero " + filePath);
				throw exc;
			}
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
		this.close();

		Boolean isCompressed = Boolean.FALSE;

		fis = new FileInputStream(filePath);
		if (filePath.endsWith(".gz")) {
			zs = new GZIPInputStream(fis);
			isCompressed = true;
		} else if (filePath.endsWith(".zip")) {
			zs = new ZipInputStream(fis);
			isCompressed = true;
		}
		isr = new InputStreamReader(isCompressed ? zs : fis, ConstantesSolvencia.FILE_CHARSET);
		reader = factory.createReader(streamName, isr); // NOSONAR

		reader.setErrorHandler(new BeanReaderErrorHandlerSupport() {
			public void invalidRecord(InvalidRecordException ex) {
				log.error("Error en lectura de registro: {}, {}", ex.getLocalizedMessage(), ex.getRecordContext()
						.getFieldErrors());
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
	}
}
