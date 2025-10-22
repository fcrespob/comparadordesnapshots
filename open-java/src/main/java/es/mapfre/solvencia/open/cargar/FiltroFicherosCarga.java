package es.mapfre.solvencia.open.cargar;

import java.io.File;
import java.io.FilenameFilter;

public class FiltroFicherosCarga implements FilenameFilter {
	private String patron = "";
	private Boolean comprimido = Boolean.FALSE;

	public FiltroFicherosCarga(String patron, Boolean comprimido) {
		super();
		this.patron = patron;
		this.comprimido = comprimido;
	}

	@Override
	public boolean accept(File dir, String name) {
		if (patron == null || name == null) {
			return false;
		}

		String extension = SolvenciaUtilsCargaBBDD.getExtension(name).toLowerCase();

		return name.matches(patron)
				&& (comprimido == null || ((comprimido && (extension.equals("gz") || extension.equals("zip"))) ||
						(!comprimido && !(extension.equals("gz") || extension.equals("zip")))));
	}
}
