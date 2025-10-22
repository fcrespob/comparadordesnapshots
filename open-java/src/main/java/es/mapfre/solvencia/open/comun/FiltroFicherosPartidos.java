package es.mapfre.solvencia.open.comun;

import java.io.File;
import java.io.FilenameFilter;

public class FiltroFicherosPartidos implements FilenameFilter {
	private String patron = "";
	
	public FiltroFicherosPartidos(String patron) {
		super();
		this.patron = patron;
	}

	@Override
	public boolean accept(File dir, String name) {
		if (patron == null || name == null) {
			return false;
		}
		
		return name.length() > patron.length() && name.startsWith(patron);
	}
}
