package es.mapfre.solvencia.open.comun;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjecutorPartirFicheros implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(EjecutorPartirFicheros.class);
	
	private String ficheroOriginal;
	private String formatoFinal;
	private int numTrozos;
	private long minSize;
	
	public EjecutorPartirFicheros(String ficheroOriginal, String formatoFinal, int numTrozos, long minSize) {
		super();
		this.ficheroOriginal = ficheroOriginal;
		this.formatoFinal = formatoFinal;
		this.numTrozos = numTrozos;
		this.minSize = minSize;
	}

	@Override
	public void run() {
		RandomAccessFile fromFile = null;
		FileChannel fromChannel = null;
		try {
			fromFile = new RandomAccessFile(ficheroOriginal, "r");
			fromChannel = fromFile.getChannel();

			long fileSize = fromChannel.size();

			if (fileSize > minSize) {
				int numeroTrozos = (int) Math.min(Math.ceil((double) fileSize / (double) minSize), numTrozos);

				String linea = fromFile.readLine();
				fromFile.seek(0L);

				// El 2 es por el tamaño del NewLine
				int lineSize = linea.getBytes(Constantes.FILE_CHARSET).length + Constantes.LONGITUD_NEW_LINE;
				long numLines = fileSize / lineSize;
				long tamTrozo = ((long) Math.ceil((double) numLines / (double) numeroTrozos)) * lineSize;

				logger.info("Procedemos a partir el fichero {} en {} trozos de un tamaño aproximado {} KB. El formato final será {}", ficheroOriginal, numeroTrozos, (tamTrozo/1024), formatoFinal);

				for (int numTrozo = 0; numTrozo < numeroTrozos; numTrozo++) {
					String outputFilePath = ficheroOriginal + "." + numTrozo + formatoFinal;

					FileOutputStream fos = null;
					DeflaterOutputStream zs = null;
					WritableByteChannel toChannel = null;

					try {
						fos = new FileOutputStream(outputFilePath);
						if (outputFilePath.endsWith(Constantes.FORMATO_FICHERO_GZIP)) {
							zs = new GZIPOutputStream(fos, Constantes.GZIP_BUFFER_SIZE);
							toChannel = Channels.newChannel(zs);
						} else {
							toChannel = fos.getChannel();
						}

						long position = numTrozo * tamTrozo;

						fromChannel.transferTo(position, tamTrozo, toChannel);
					} finally {
						if (toChannel != null) {
							toChannel.close();
							toChannel = null;
						}
						if (zs != null) {
							try {
								zs.flush();
								zs.finish();
								zs.close();
							} catch (Exception e) {
							}
							zs = null;
						}
						if (fos != null) {
							fos.close();
							fos = null;
						}
					}
				}
			}
		} catch (IOException e) {
			logger.error("Error partiendo y comprimiendo fichero {}", ficheroOriginal, e);
		} finally {
			if (fromChannel != null) {
				try {
					fromChannel.close();
				} catch (IOException e) {
				}
			}
			if (fromFile != null) {
				try {
					fromFile.close();
				} catch (IOException e) {
				}
			}
		}
	}

}
