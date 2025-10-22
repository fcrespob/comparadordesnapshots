package es.mapfre.coaseguro.tirea.gestores;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import es.mapfre.coaseguro.tirea.dao.entidades.DatosCoaDao;
import es.mapfre.coaseguro.tirea.dao.entidades.FlujPMaCoaDao;
import es.mapfre.coaseguro.tirea.dao.entidades.Tab35012Dao;
import es.mapfre.coaseguro.tirea.dao.entidades.TotPMaCoaDao;
import es.mapfre.coaseguro.tirea.dominio.entidades.DatosCoa;
import es.mapfre.coaseguro.tirea.dominio.entidades.FlujPMaCoa;
import es.mapfre.coaseguro.tirea.dominio.entidades.Incidencia;
import es.mapfre.coaseguro.tirea.dominio.entidades.Tab35012;
import es.mapfre.coaseguro.tirea.dominio.entidades.TotPMaCoa;
import es.mapfre.coaseguro.tirea.excepcion.Solvencia2Excepcion;
import net.sf.ehcache.Element;

public class GestorFicheroCloud {
	private static GestorFicheroCloud INSTANCE = null;
	
	private synchronized static void createInstance(){
		if( INSTANCE == null ){
			INSTANCE = new GestorFicheroCloud();
		}
	}
	
	public static GestorFicheroCloud cargarDatos(){
		if( INSTANCE == null ){
			createInstance();
		}
		return INSTANCE;
	}

	public List<TotPMaCoa> getValuesInd() {

		TotPMaCoaDao pesoCol = new TotPMaCoaDao();
		Collection<Element> pc = pesoCol.values();
		List<TotPMaCoa> lista = new ArrayList<TotPMaCoa>();
		if(pc == null || pc.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se han encontrado datos para el entregable TOTPMACOA.");
			throw new Solvencia2Excepcion(inci);
		}
		
		Iterator<Element> itPC= pc.iterator();
		while (itPC.hasNext()) {
			lista.add((TotPMaCoa) itPC.next().getObjectValue());
		}

		return lista;
	}

	public List<FlujPMaCoa> getValuesCol() {

		FlujPMaCoaDao pesoCol = new FlujPMaCoaDao();
		Collection<Element> pc = pesoCol.values();
		List<FlujPMaCoa> lista = new ArrayList<FlujPMaCoa>();
		if(pc == null || pc.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se han encontrado datos para el entregable FLUJPMACOA.");
			throw new Solvencia2Excepcion(inci);
		}
		
		Iterator<Element> itPC= pc.iterator();
		while (itPC.hasNext()) {
			lista.add((FlujPMaCoa) itPC.next().getObjectValue());
		}

		return lista;
	}
	
	public List<Tab35012> getValuesTab35012() {

		Tab35012Dao pesoCol = new Tab35012Dao();
		Collection<Element> pc = pesoCol.values();
		List<Tab35012> lista = new ArrayList<Tab35012>();
		if(pc == null || pc.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se han encontrado datos en la tabla de traduccion de tablas MAPFRE-TIREA 35012.");
			throw new Solvencia2Excepcion(inci);
		}
		
		Iterator<Element> itPC= pc.iterator();
		while (itPC.hasNext()) {
			lista.add((Tab35012) itPC.next().getObjectValue());
		}

		return lista;
	}
	
	public List<DatosCoa> getValuesDatosCoa() {

		DatosCoaDao pesoCol = new DatosCoaDao();
		Collection<Element> pc = pesoCol.values();
		List<DatosCoa> lista = new ArrayList<DatosCoa>();
		if(pc == null || pc.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se han encontrado datos en el catalogo de datos de coaseguro.");
			throw new Solvencia2Excepcion(inci);
		}
		
		Iterator<Element> itPC= pc.iterator();
		while (itPC.hasNext()) {
			lista.add((DatosCoa) itPC.next().getObjectValue());
		}

		return lista;
	}
	
}
