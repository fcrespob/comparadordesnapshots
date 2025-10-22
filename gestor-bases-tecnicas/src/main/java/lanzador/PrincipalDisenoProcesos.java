package lanzador;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import es.mapfre.solvencia.cargaFicherosGBT.ExporterGBT;
import es.mapfre.solvencia.cargaFicherosGBT.LoaderGBT;
import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.impl.maestro.UmicDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.FichaProcesoDao;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.excepcionGBT.GestorBasesTecnicasException;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

public class PrincipalDisenoProcesos {

	private static Collection<FichaProceso> getFichas() throws IOException{
		BeanIOReader readerFicha = new BeanIOReader("beanio/beanio-config.xml","FICHASCALCULO.TXT", "R340T000");
		FichaProcesoDao daoFP = new FichaProcesoDao();
		daoFP.loadCache(readerFicha);

		return daoFP.values();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length<1){
			System.exit(0);
		}
		
		try {

			ExporterGBT exporter = new ExporterGBT();
			
			if(exporter.getFileDescriptors("Resultado", "20141106")==null){
				throw new FileNotFoundException("Error en los ficheros de salida");
			}
			
			// Carga de nuestras caches
			LoaderGBT loader = new LoaderGBT();
			loader.getFileDescriptors("Ficheros", "20141106");

			// Carga de las caches que cargan ellos
			/*LoaderAmazon loaderAm = new LoaderAmazon();
			loaderAm.getFileDescriptors("FicherosAmazon", "20141106");
			*/
			UmicDao ud = new UmicDao();
			Set<UmicKey> uk = ud.keySet();
			Iterator<UmicKey> it = uk.iterator();

			Umic umic = null;
			Boolean simulacion = (args.length>1)? Boolean.valueOf(args[1]) : false;
			
			while(it.hasNext()){
				try{
					umic = ud.get(it.next());

					DetalleBaseTecnica detalleBT = new DetalleBaseTecnica();

					detalleBT.setBaseTec(args[0]);

					Modulo mod = FactoriaModulos.getModulo("BASE_TEC");
					mod.execute(detalleBT, umic, simulacion);
				}catch (GestorBasesTecnicasException e) {
				}
			}
			/*exporter.exportCacheRes();
			exporter.cerrarDescriptorRes();
			
			exporter.exportCacheErr();
			exporter.cerrarDescriptorErr();*/
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			return;
		} catch (IOException e) {
			//e.printStackTrace();
			return;
		} catch (Throwable e) {
			//e.printStackTrace();
			return;
		}
	}

}
