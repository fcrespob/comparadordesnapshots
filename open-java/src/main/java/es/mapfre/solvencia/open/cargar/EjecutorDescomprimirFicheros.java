package es.mapfre.solvencia.open.cargar;

import java.io.File;

import es.mapfre.solvencia.open.comun.Constantes;
import es.mapfre.solvencia.open.comun.SolvenciaUtils;

public class EjecutorDescomprimirFicheros implements Runnable {
	private String nombre;
	private String rutaBrutos;
	
	public EjecutorDescomprimirFicheros(String nombre, String rutaBrutos) {
		super();
		this.nombre = nombre;
		this.rutaBrutos = rutaBrutos;
	}

	@Override
	public void run() {
		// si no se trata de un fichero comprimido, miramos primero
		// si existe ese mismo fichero pero
		// comprimido--> si existe pasamos de este fichero txt
		Boolean cargarFichero = Boolean.TRUE;
		String extension = SolvenciaUtilsCargaBBDD.getExtension(nombre);

		String rutaFicheroOriginal = rutaBrutos + File.separator + nombre;
		File ficheroOriginal = new File(rutaFicheroOriginal);

		File ficheroComprimido = null;

		if (ficheroOriginal.exists()) {
			// Miramos si es comprimido. Si lo es, lo descomprimimos y lo
			// borramos. Si no lo es, buscamos el equivalente comrpimido. Si lo
			// encontramos, lo descomprimimos y lo borramos.

			if (!extension.endsWith("gz") && !extension.endsWith("zip")) {
				// buscar si existe el mismo fichero pero con extension gz o zip
				String rutaTempGz = rutaFicheroOriginal + Constantes.FORMATO_FICHERO_GZIP;
				File fileTempGz = new File(rutaTempGz);
				if (fileTempGz.exists()) {
					ficheroComprimido = fileTempGz;
				} else {
					String rutaTempZip = rutaFicheroOriginal + Constantes.FORMATO_FICHERO_ZIP;
					File fileTempZip = new File(rutaTempZip);
					if (fileTempZip.exists()) {
						ficheroComprimido = fileTempZip;
					}
				}
			} else {
				ficheroComprimido = ficheroOriginal;
			}

			if (ficheroComprimido != null) {
				// Descomprimimos el fichero y lo borramos
				String rutaFicheroComprimido = ficheroComprimido.getAbsolutePath();
				String rutaFicheroPlano = rutaFicheroComprimido.substring(0, rutaFicheroComprimido.lastIndexOf('.'));
				SolvenciaUtils.descomprimirFichero(rutaFicheroComprimido, rutaFicheroPlano);
				ficheroComprimido.delete();
			}
		}
	}

}
