package es.mapfre.proxy.prestaciones.gestores;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import es.mapfre.proxy.prestaciones.dao.entidades.PesosBtColDao;
import es.mapfre.proxy.prestaciones.dao.entidades.PesosBtIndDao;
import es.mapfre.proxy.prestaciones.dominio.entidades.Incidencia;
import es.mapfre.proxy.prestaciones.dominio.entidades.PesosBt;
import es.mapfre.proxy.prestaciones.excepcion.Solvencia2Excepcion;
import net.sf.ehcache.Element;

public class GestorFicheroPesosBt {
	private static GestorFicheroPesosBt INSTANCE = null;
	
	private synchronized static void createInstance(){
		if( INSTANCE == null ){
			INSTANCE = new GestorFicheroPesosBt();
		}
	}
	
	public static GestorFicheroPesosBt cargarDatos(){
		if( INSTANCE == null ){
			createInstance();
		}
		return INSTANCE;
	}

	public List<PesosBt> getValuesInd() {

		PesosBtIndDao pesoCol = new PesosBtIndDao();
		Collection<Element> pc = pesoCol.values();
		List<PesosBt> lista = new ArrayList<PesosBt>();
		if(pc == null || pc.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se han encontrado pesos para el negocio individuales.");
			throw new Solvencia2Excepcion(inci);
		}
		
		Iterator<Element> itPC= pc.iterator();
		while (itPC.hasNext()) {
			lista.add((PesosBt) itPC.next().getObjectValue());
		}

		return lista;
	}

	public List<PesosBt> getValuesCol() {

		PesosBtColDao pesoCol = new PesosBtColDao();
		Collection<Element> pc = pesoCol.values();
		List<PesosBt> lista = new ArrayList<PesosBt>();
		if(pc == null || pc.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se han encontrado pesos para el negocio colectivos.");
			throw new Solvencia2Excepcion(inci);
		}
		
		Iterator<Element> itPC= pc.iterator();
		while (itPC.hasNext()) {
			lista.add((PesosBt) itPC.next().getObjectValue());
		}

		return lista;
	}
}
