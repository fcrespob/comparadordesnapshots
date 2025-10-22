package es.mapfre.scr.gastos.modulos;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.mapfre.scr.gastos.dominio.entidades.Incidencia;
import es.mapfre.scr.gastos.excepcion.Solvencia2Excepcion;
import es.mapfre.scr.gastos.gestores.GestorFichaProceso;
import es.mapfre.scr.gastos.gestores.GestorFicheroSalida;
import es.mapfre.scr.gastos.gestores.GestorIncidencias;
import es.mapfre.scr.gastos.utils.CargaDatos;
import es.mapfre.scr.gastos.utils.ConstantesSolvencia;
import es.mapfre.scr.gastos.utils.LoggerManager;

public class PrincipalSCRGRE {

	private static final String RUTA_BASE = "CATALOGOS";
	private static final String TIPO = ConstantesSolvencia.CTE_GTO_UMIC;
	
	public static void main(String[] args) {
		
		try {

			LoggerManager log = LoggerManager.getInstance(TIPO);
			
			Date ahora = new Date();
		    SimpleDateFormat formateador = new SimpleDateFormat("HH:mm:ss");
		    log.writeLog(ConstantesSolvencia.LOG_INI + formateador.format(ahora));
			
			// Se crea el gestor de incidencias
			GestorIncidencias gi = GestorIncidencias.getInstance(TIPO);
	
			// Se cargan los datos necesarios
			CargaDatos cargaDatos = new CargaDatos(); 
			try {
				cargaDatos.cargaDatosGeneral("catalogos", "", RUTA_BASE,TIPO);
			} catch(Solvencia2Excepcion solvExc){
				Incidencia inci = solvExc.getIncidencia();
				gi.write(inci);
				gi.cerrarConector();
				System.exit(0);
			} catch (IOException e) {
				Incidencia inci = new Incidencia();
				inci.setCodigoRetorno("18");
				inci.setInfAmpliada(e.getMessage());
				gi.write(inci);
				gi.cerrarConector();
				System.exit(0);
			} catch (Exception e) {
				Incidencia inci = new Incidencia();
				inci.setCodigoRetorno("19");
				inci.setInfAmpliada("Error genï¿½rico en la lectura de ficheros.");
				gi.write(inci);
				gi.cerrarConector();
				System.exit(0);
			}
			
			Timestamp fechaEfecto = null;
			// Se obtiene la fecha efecto de la ficha de proceso
			try{
				GestorFichaProceso gfp = GestorFichaProceso.getInstance();
				fechaEfecto = gfp.getFchEfecto();
			} catch(Solvencia2Excepcion solvExc){
				Incidencia inci = solvExc.getIncidencia();
				gi.write(inci);
				gi.cerrarConector();
				System.exit(0);
			} catch (Exception e) {
				Incidencia inci = new Incidencia();
				inci.setCodigoRetorno("17");
				inci.setInfAmpliada("Error genérico al obtener la fecha efecto.");
				gi.write(inci);
				gi.cerrarConector();
				System.exit(0);
			}
			
			// Se eliminan datos antiguos del fichero de entrada
			GestorFicheroSalida gfs = null;
			try {
				gfs = GestorFicheroSalida.getInstance(TIPO);
				gfs.eliminarDatosAntiguos(TIPO);
				
			} catch (Solvencia2Excepcion e) {
				gi.write(e);
			}
			
			ModuloSCRGRE mod = new ModuloSCRGRE();
			mod.execute(new SimpleDateFormat("yyyyMMdd").format(fechaEfecto));
			
			// Se cierran todos los ficheros
			gfs.cerrarConectores();
			
			log.writeLog(ConstantesSolvencia.LOG_INCI_DET + gi.getNumInci());
			
			gi.cerrarConector();
		
			ahora = new Date();
		    log.writeLog(ConstantesSolvencia.LOG_FIN + formateador.format(ahora));
			
		    log.cerrarWriter();
			System.exit(0);
		
		} catch (Throwable t) {
			System.exit(30);
		}	
	}
}
