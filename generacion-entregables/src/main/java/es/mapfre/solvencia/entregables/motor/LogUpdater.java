package es.mapfre.solvencia.entregables.motor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUpdater implements Runnable {
	private String rutaFichaResultados;
	private Boolean continueExec = Boolean.TRUE;

	private static Logger logger = LoggerFactory.getLogger(LogUpdater.class);

	public LogUpdater(String rutaFichaResultados) {
		super();
		this.rutaFichaResultados = rutaFichaResultados;
	}

	public void run() {
		File ficheroResultados = new File(rutaFichaResultados);
		

		while (continueExec) {
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				continueExec = Boolean.FALSE;
			}
			
			
			logger.info("El estado del hilo de actualización de log es: {}",Thread.currentThread().getState().toString());
			

			// Actualizamos la fecha de la ficha de resultados para indicar que
			// hay avance
			if (ficheroResultados != null && ficheroResultados.exists() && ficheroResultados.canWrite()) {
				ficheroResultados.setLastModified(System.currentTimeMillis());
				try {
					Files.getFileAttributeView(ficheroResultados.toPath(), BasicFileAttributeView.class).setTimes(
							FileTime.fromMillis(System.currentTimeMillis()), null, null);
				} catch (IOException e) {
					logger.warn("Problema actualizando fichero de log (posible timeout en OPEN): {}", e.getMessage());
					logger.debug("Error: ", e);
				}
			} else {
				logger.warn("No hay acceso al fichero de log de Solvencia {} (posible timeout en OPEN)",
						rutaFichaResultados);
			}
		}
	}
}
