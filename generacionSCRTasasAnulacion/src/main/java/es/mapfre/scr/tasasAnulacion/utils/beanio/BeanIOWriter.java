package es.mapfre.scr.tasasAnulacion.utils.beanio;

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

import es.mapfre.scr.tasasAnulacion.utils.ConstantesSolvencia;

public class BeanIOWriter {

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
	
	public BeanIOWriter(String configFilePath, String filePath, String streamName, boolean append) throws IOException {
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
			this.openWriter(filePath, streamName, append); // NOSONAR
		}
	}

	/**
	 * Empleado para modificar el fichero a exportar en un writer previamente
	 * configurado.
	 * 
	 * @param filePath
	 * @param streamName
	 * @throws IOException
	 */
	public void createWriter(String filePath, String streamName) throws IOException {
		Boolean isCompressed = Boolean.FALSE;

		fos = new FileOutputStream(filePath);
		if (filePath.endsWith(".gz")) {
			zs = new GZIPOutputStream(fos, ConstantesSolvencia.GZIP_BUFFER_SIZE);
			isCompressed = true;
		} else if (filePath.endsWith(".zip")) {
			zs = new ZipOutputStream(fos);
			isCompressed = true;
		}
		osw = new OutputStreamWriter(isCompressed ? zs : fos, ConstantesSolvencia.FILE_CHARSET);
		writer = factory.createWriter(streamName, osw);
	}
	
	public void openWriter(String filePath, String streamName, boolean append) throws IOException {
		Boolean isCompressed = Boolean.FALSE;

		fos = new FileOutputStream(filePath,append);
		if (filePath.endsWith(".gz")) {
			zs = new GZIPOutputStream(fos, ConstantesSolvencia.GZIP_BUFFER_SIZE);
			isCompressed = true;
		} else if (filePath.endsWith(".zip")) {
			zs = new ZipOutputStream(fos);
			isCompressed = true;
		}
		osw = new OutputStreamWriter(isCompressed ? zs : fos, ConstantesSolvencia.FILE_CHARSET);
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
	}
}
