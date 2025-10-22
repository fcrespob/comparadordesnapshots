package es.mapfre.coaseguro.tirea.gestores;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import es.mapfre.coaseguro.tirea.dao.entidades.FichaProcesoDao;
import es.mapfre.coaseguro.tirea.dominio.entidades.FichaProceso;
import es.mapfre.coaseguro.tirea.dominio.entidades.Incidencia;
import net.sf.ehcache.Element;

public class GestorFichaProceso {
	private static GestorFichaProceso INSTANCE = null;

	private synchronized static void createInstance(){
		if( INSTANCE == null ){
			INSTANCE = new GestorFichaProceso();
		}
	}
	
	public static GestorFichaProceso getInstance(){
		if( INSTANCE == null ){
			createInstance();
		}
		return INSTANCE;
	}

	public List<FichaProceso> getValues() {

		FichaProcesoDao fpDao = new FichaProcesoDao();
		Collection<Element> fp = fpDao.values();
		List<FichaProceso> lista = new ArrayList<FichaProceso>();
		if(fp == null || fp.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se ha encontrado la ficha de proceso en el fichero correspondiente.");
			GestorIncidenciasGen gi = GestorIncidenciasGen.getInstance();
			gi.write(inci);
			System.exit(0);
		}
		
		Iterator<Element> itFP= fp.iterator();
		while (itFP.hasNext()) {
			lista.add((FichaProceso) itFP.next().getObjectValue());
		}

		return lista;
	}
}
