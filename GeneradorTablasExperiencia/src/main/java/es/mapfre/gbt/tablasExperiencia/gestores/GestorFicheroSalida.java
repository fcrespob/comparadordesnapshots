package es.mapfre.gbt.tablasExperiencia.gestores;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import es.mapfre.gbt.tablasExperiencia.dominio.entidades.Incidencia;
import es.mapfre.gbt.tablasExperiencia.dominio.entidades.Salida;
import es.mapfre.gbt.tablasExperiencia.excepcion.Solvencia2Excepcion;
import es.mapfre.gbt.tablasExperiencia.utils.BtUtils;
import es.mapfre.gbt.tablasExperiencia.utils.ConstantesSolvencia;
import es.mapfre.gbt.tablasExperiencia.utils.LoggerManager;
import es.mapfre.gbt.tablasExperiencia.utils.beanio.BeanIOReader;
import es.mapfre.gbt.tablasExperiencia.utils.beanio.BeanIOWriter;

public class GestorFicheroSalida {
	private static GestorFicheroSalida INSTANCE = null;
	
	private BtUtils btUtils = new BtUtils();
	// Constantes
	private String NOMBRE_FICH_AUX = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_SALIDA_AUX);
	private String NOMBRE_FICH_REAL = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_SALIDA);
	
	//Atributos
	private BeanIOReader reader = null;
	private BeanIOWriter writer = null;
	
	private GestorFicheroSalida(){
			crearConectores();
	}
	
	private void crearConectores(){
		try {
			writer = new BeanIOWriter(ConstantesSolvencia.BEANIO_CONFIG_XML, NOMBRE_FICH_REAL, ConstantesSolvencia.CATALOGO_SALIDA, true);
			reader = new BeanIOReader(ConstantesSolvencia.BEANIO_CONFIG_XML, NOMBRE_FICH_REAL, ConstantesSolvencia.CATALOGO_SALIDA);
		} catch (Exception e) {}
	}
	
	private synchronized static void createInstance(){
		if( INSTANCE == null ){
			INSTANCE = new GestorFicheroSalida();
		}
	}
	
	public static GestorFicheroSalida getInstance(){
		if( INSTANCE == null ){
			createInstance();
		}
		return INSTANCE;
	}
	
	public void eliminarDatosAntiguos(Timestamp fechaEfecto){
		
		// Solo se podrán eliminar datos si existe el fichero de entrada
		if(reader!=null){
			String fechaBusqueda = null;
			int fechaBusquedaInt = 0;
			try {
				// Comprobación de fecha
				fechaBusqueda = GestorMeses.getFechaEfecto(fechaEfecto);
				fechaBusquedaInt = Integer.parseInt(fechaBusqueda);
				// Fin comprobación de fecha

			}catch (Solvencia2Excepcion solExc){
				Incidencia inci = solExc.getIncidencia();
				throw new Solvencia2Excepcion("Error al eliminar los datos antiguos del fichero de entrada.", inci);
			}	
			// Fin comprobación de fecha
			
			Salida registroSalida = null;
			int fechaLeida;
			String strFechaEfecto = new SimpleDateFormat("yyyyMMdd").format(fechaEfecto);
			int iFechaEfecto = Integer.parseInt(strFechaEfecto);
			
			BeanIOWriter writerAux;
			try {
				writerAux = new BeanIOWriter(ConstantesSolvencia.BEANIO_CONFIG_XML, NOMBRE_FICH_AUX, ConstantesSolvencia.CATALOGO_SALIDA);
			} catch (Exception e1) {
				Incidencia inci = new Incidencia();
				inci.setTipoError("Error");
				inci.setInfAmpliada(e1.getMessage());
				inci.setCodigoRetorno("13");
				inci.setGeneradorError(this.getClass().getName());
				throw new Solvencia2Excepcion("Error al eliminar los datos antoguos del fichero de entrada.", inci);
			}
			
			int contRegCon = 0;
			int contRegTotal = 0;
			
			// Se escriben en el fichero temporal, los datos posteriores a la fecha de cierre menos los meses parametrizados
			while((registroSalida = (Salida) reader.read()) != null){
				contRegTotal++;
				fechaLeida = Integer.parseInt(registroSalida.getKfchcierre());
				if(fechaLeida >= fechaBusquedaInt && fechaLeida != iFechaEfecto){
					contRegCon++;
					writerAux.write(registroSalida);
					writerAux.flush();
				}
			}
			
			LoggerManager log = LoggerManager.getInstance();
			
			log.writeLog(ConstantesSolvencia.LOG_REG_ELIM + (contRegTotal - contRegCon));
			log.writeLog(ConstantesSolvencia.LOG_REG_CONS + contRegCon);

			try {
				writerAux.close();
			} catch (Exception e) {}
			
			File ficheroSalida = new File(NOMBRE_FICH_REAL);
			File ficheroSalidaAux = new File(NOMBRE_FICH_AUX);
			cerrarConectores();
			ficheroSalida.delete();
			ficheroSalidaAux.renameTo(ficheroSalida);
			crearConectores();
			
		}
	}

	public void cerrarConectores() {
		try {
			reader.close();
			writer.close();
		} catch (Exception e) {}
	}
	
	public void write(Salida registroSalida){
		writer.write(registroSalida);
	}

}
