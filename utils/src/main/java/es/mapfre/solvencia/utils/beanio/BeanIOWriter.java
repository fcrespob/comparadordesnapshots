package es.mapfre.solvencia.utils.beanio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipOutputStream;

import org.beanio.BeanIOException;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;

public class BeanIOWriter {

	private static final Logger logger = LoggerFactory.getLogger(BeanIOWriter.class);
	
	private FileOutputStream fos = null;
	private DeflaterOutputStream zs = null;
	private Writer osw = null;

	private StreamFactory factory = null;
	private BeanWriter writer = null;;

	/**
	 * Crea un writer a partir de la configuración pasada
	 * 
	 * @param configFilePath
	 * @param filePath
	 * @param streamName
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public BeanIOWriter(String configFilePath, String filePath, String streamName) throws IOException {
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
			this.createWriter(filePath, streamName); // NOSONAR
		}
	}

	/**
	 * Empleado para modificar el fichero a exportar en un writer previamente
	 * configurado. Se usa el CHARSET por defecto (ISO-8859-1)
	 * 
	 * @param filePath
	 * @param streamName
	 * @throws IOException
	 */
	public void createWriter(String filePath, String streamName) throws IOException {
		createWriter(filePath, streamName, ConstantesSolvencia.FILE_CHARSET);
	}
	
	/**
	 * Empleado para modificar el fichero a exportar en un writer previamente
	 * configurado.
	 * 
	 * @param filePath
	 * @param streamName
	 * @param charsetName
	 * @throws IOException
	 */
	public void createWriter(String filePath, String streamName, String charsetName) throws IOException {
		Boolean isCompressed = Boolean.FALSE;

		fos = new FileOutputStream(filePath);
		if (filePath.endsWith(".gz")) {
			zs = new GZIPOutputStream(fos, ConstantesSolvencia.GZIP_BUFFER_SIZE);
			isCompressed = true;
		} else if (filePath.endsWith(".zip")) {
			zs = new ZipOutputStream(fos);
			isCompressed = true;
		}
		if (charsetName == null) {
			charsetName = ConstantesSolvencia.FILE_CHARSET;
		}
		osw = new OutputStreamWriter(isCompressed ? zs : fos, charsetName);
		writer = factory.createWriter(streamName, osw);
	}

	/**
	 * Escribe un valor en el fichero
	 * 
	 * @param value
	 */
	public void write(Object value) {
		writer.write(value);
	}

	/**
	 * Escribe una lista de valores en el fichero
	 * 
	 * @param values
	 */
	public void writeAll(List<Object> values) {
		for (Object value : values) {
			writer.write(value);
		}
	}

	/**
	 * Libera los datos presentes en el buffer
	 */
	public void flush() {
		writer.flush();
	}

	/**
	 * Cierra el writer y los manejadores de fichero abiertos.
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		try {
			if (writer != null) {
				writer.close();
			}
			if (osw != null) {
				osw.close();
			}
			if (zs != null) {
				zs.finish();
				zs.close();
			}
			if (fos != null) {
				fos.close();
			}
		} catch (Exception e) {
			logger.warn("{}", e.getMessage());
		}
	}
}
