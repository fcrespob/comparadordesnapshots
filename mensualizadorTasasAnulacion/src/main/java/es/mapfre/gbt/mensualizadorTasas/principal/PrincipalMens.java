package es.mapfre.gbt.mensualizadorTasas.principal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import net.sf.ehcache.Element;

import org.beanio.BeanIOException;

import es.mapfre.gbt.mensualizadorTasas.dao.TasaMensualizadaDao;
import es.mapfre.gbt.mensualizadorTasas.dominio.FichaResultado;
import es.mapfre.gbt.mensualizadorTasas.exception.Solvencia2Excepcion;
import es.mapfre.gbt.mensualizadorTasas.logger.LoggerManager;
import es.mapfre.gbt.mensualizadorTasas.modulo.Mensualizador;
import es.mapfre.gbt.mensualizadorTasas.utils.BeanIOReader;
import es.mapfre.gbt.mensualizadorTasas.utils.BeanIOWriter;
import es.mapfre.gbt.mensualizadorTasas.utils.BtUtils;
import es.mapfre.gbt.mensualizadorTasas.utils.ConstantesMensualizador;

public class PrincipalMens {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			BtUtils btUtils = new BtUtils();
			
			LoggerManager log = LoggerManager.getInstance();
			
			Date ahora = new Date();
		    SimpleDateFormat formateador = new SimpleDateFormat("HH:mm:ss");
		    log.writeLog(ConstantesMensualizador.LOG_INI + formateador.format(ahora));
		    
			long time_start, time_end;
			time_start = System.currentTimeMillis();
			
			TasaMensualizadaDao tmdao = new TasaMensualizadaDao();
			
			Mensualizador calculoFinal = new Mensualizador();
			Collection<Element> registrosFinales = calculoFinal.getRegistros();
			tmdao.putAll(registrosFinales);
			BeanIOWriter writerFinal = new BeanIOWriter(ConstantesMensualizador.BEANIO_CONFIG_OUT, btUtils.getCargaFicherosProperty(ConstantesMensualizador.CACHE_TASAS), ConstantesMensualizador.CACHE_TASAS);
			tmdao.exportCache(writerFinal);
			writerFinal.close();

			time_end = System.currentTimeMillis();

			long milis = time_end - time_start;
			long minutos = milis/60000;
			long restomin = milis%60000;
			long seg = restomin/1000;
			long restoseg = restomin%1000;

			//System.out.println("Se ha generado el fichero final en "+ minutos +":"+seg+"."+restoseg);
			
			ahora = new Date();
			log.writeLog(ConstantesMensualizador.LOG_FIN + formateador.format(ahora));
			
			log.cerrarWriter();
		}catch (BeanIOException e) {
			//e.printStackTrace();
			System.exit(30);
		}catch (Solvencia2Excepcion e) {
			//e.printStackTrace();
			System.exit(31);
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			System.exit(32);
		} catch (IOException e) {
			//e.printStackTrace();
			System.exit(33);
		} catch (Throwable e) {
			//e.printStackTrace();
			System.exit(34);
		}
	}
}