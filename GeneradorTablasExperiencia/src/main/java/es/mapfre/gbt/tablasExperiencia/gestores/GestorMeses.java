package es.mapfre.gbt.tablasExperiencia.gestores;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.mapfre.gbt.tablasExperiencia.excepcion.Solvencia2Excepcion;

public class GestorMeses {

	public static String getFechaEfecto(Timestamp fefecto){
		
		int mesesAntes = 13;
		try{
			mesesAntes = GestorFichaProceso.getInstance().getMesesAntes();
		} catch(Solvencia2Excepcion e){
			GestorIncidencias gi = GestorIncidencias.getInstance();
			gi.write(e.getIncidencia());
			mesesAntes = 13;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(fefecto.getTime());
		calendar.add(Calendar.MONTH, -mesesAntes);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		return sdf.format(calendar.getTime());
	}

}
