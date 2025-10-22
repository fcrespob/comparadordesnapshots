package es.mapfre.gbt.tablasExperiencia.modulos;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.beanio.UnexpectedRecordException;
import org.beanio.UnidentifiedRecordException;

import es.mapfre.gbt.tablasExperiencia.dominio.entidades.Incidencia;
import es.mapfre.gbt.tablasExperiencia.excepcion.Solvencia2Excepcion;
import es.mapfre.gbt.tablasExperiencia.gestores.GestorFichaProceso;
import es.mapfre.gbt.tablasExperiencia.gestores.GestorFicheroSalida;
import es.mapfre.gbt.tablasExperiencia.gestores.GestorIncidencias;
import es.mapfre.gbt.tablasExperiencia.utils.CargaDatos;
import es.mapfre.gbt.tablasExperiencia.utils.ConstantesSolvencia;
import es.mapfre.gbt.tablasExperiencia.utils.LoggerManager;

/* Códigos de error:
 * 01 - Error genérico en la conversión de Tablas de Experiencia Real
 * 02 - Error genérico en la conversión de la tabla de experiencia
 * 03 - La tabla base no esta informada
 * 04 - Edad Desde no puede ser superior a Edad Hasta
 * 05 - El proceso de generación de tablas de experiencia reales no ha generado ningún registro
 * 06 - No existen registros asociados a la tabla base en el fichero con los valores de Tablas de mortalidad
 * 07 - Existen varios registros asociados a la tabla base en el fichero de Tablas de mortalidad
 * 08 - La edad hasta no puede ser mayor de 129
 * 09 - No existen los 130 valores necesarios de q(x)
 * 10 - No se ha encontrado la ficha de proceso en el fichero correspondiente
 * 11 - Se han encontrado varias fichas de proceso en el fichero correspondiente
 * 12 - La fecha de la ficha de proceso no tiene un formato correcto
 * 13 - Error al eliminar los datos antoguos del fichero de entrada
 * 14 - El contenido del fichero de meses antes no tiene el formato correcto, se ha tomado el valor 13 por defecto.
 * 15 - Error genérico al leer la parametrización de meses
 * 16 - No existe el fichero que contiene los meses, se ha tomado el valor 13 por defecto.
 * 17 - Error genérico al obtener la fecha efecto
 * 18 - No se ha encontrado el fichero {0}
 * 19 - Error genérico en la lectura de ficheros
 * 20 - El campo fecha desde tiene un formato incorrecto
 * 21 - Error al leer el fichero de propiedades
 */

public class PrincipalTExp {
	
	private static final String RUTA_BASE = "CATALOGOS";
	
	public static void main(String[] args) {
		
		try {
			
			long time_start, time_end;
			//System.out.println("INICIO");
			LoggerManager log = LoggerManager.getInstance();
			
			Date ahora = new Date();
		    SimpleDateFormat formateador = new SimpleDateFormat("HH:mm:ss");
		    log.writeLog(ConstantesSolvencia.LOG_INI + formateador.format(ahora));
			
			time_start = System.currentTimeMillis();
			// Se crea el gestor de incidencias
			GestorIncidencias gi = GestorIncidencias.getInstance();
	
			// Se cargan los datos necesarios
			CargaDatos cargaDatos = new CargaDatos(); 
			try {
				cargaDatos.cargaDatosGeneral("catalogos", "", RUTA_BASE);
			} catch (UnidentifiedRecordException e) {
				Incidencia inci = new Incidencia();
				inci.setCodigoRetorno("10");
				inci.setInfAmpliada("No se ha encontrado la ficha de proceso en el fichero correspondiente.");
				gi.write(inci);
				gi.cerrarConector();
				System.exit(0);
			} catch (UnexpectedRecordException e) {
				Incidencia inci = new Incidencia();
				inci.setCodigoRetorno("10");
				inci.setInfAmpliada("No se ha encontrado la ficha de proceso en el fichero correspondiente.");
				gi.write(inci);
				gi.cerrarConector();
				System.exit(0);
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
				inci.setInfAmpliada("Error genérico en la lectura de ficheros.");
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
				gfs = GestorFicheroSalida.getInstance();
				gfs.eliminarDatosAntiguos(fechaEfecto);
				
			} catch (Solvencia2Excepcion e) {
				gi.write(e);
			}
			
			ModuloTExp mod = new ModuloTExp();
			mod.execute(new SimpleDateFormat("yyyyMMdd").format(fechaEfecto));
			
			// Se cierran todos los ficheros
			gfs.cerrarConectores();
			
			log.writeLog(ConstantesSolvencia.LOG_INCI_DET + gi.getNumInci());
			
			gi.cerrarConector();
			
			
			time_end = System.currentTimeMillis();
			long milis = time_end - time_start;
			long minutos = milis/60000;
			long restomin = milis%60000;
			long seg = restomin/1000;
			long restoseg = restomin%1000;
	
			//System.out.println("Se ha generado el fichero final en "+ minutos +":"+seg+"."+restoseg);
			//System.out.println("FIN");
			
			ahora = new Date();
		    log.writeLog(ConstantesSolvencia.LOG_FIN + formateador.format(ahora));
			
		    log.cerrarWriter();
			System.exit(0);
		
		} catch (Throwable t) {
			//t.printStackTrace();
			System.exit(30);
		}
		
	}
	
	
	
	
	

}
