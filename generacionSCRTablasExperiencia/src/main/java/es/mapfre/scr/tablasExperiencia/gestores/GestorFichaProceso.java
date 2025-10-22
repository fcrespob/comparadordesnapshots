package es.mapfre.scr.tablasExperiencia.gestores;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import es.mapfre.scr.tablasExperiencia.dao.entidades.FichaProcesoDao;
import es.mapfre.scr.tablasExperiencia.dominio.entidades.FichaProceso;
import es.mapfre.scr.tablasExperiencia.dominio.entidades.Incidencia;
import es.mapfre.scr.tablasExperiencia.excepcion.Solvencia2Excepcion;
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

	/*
	public void cerrarConectores() {
		try {
			reader.close();
		} catch (Exception e) {}
	}
	*/

	public Timestamp getFchEfecto() {
		FichaProcesoDao fpDao = new FichaProcesoDao();
		Collection<Element> fp = fpDao.values();
		
		if(fp == null || fp.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se ha encontrado la ficha de proceso en el fichero correspondiente.");
			throw new Solvencia2Excepcion(inci);
		}
		
		if(fp.size() > 1){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("11");
			inci.setInfAmpliada("Se han encontrado varias fichas de proceso en el fichero correspondiente.");
			throw new Solvencia2Excepcion(inci);
		}
		
		Iterator<Element> itFP= fp.iterator();
		FichaProceso elementoFP = (FichaProceso) itFP.next().getObjectValue();
		if(elementoFP.getFefecto()==null){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("12");
			inci.setInfAmpliada("La fecha de la ficha de proceso no tiene un formato correcto.");
			throw new Solvencia2Excepcion(inci);
		}
		return elementoFP.getFefecto();
	}
	
	private boolean isPositiveInteger(String s) {
	    int res = -1;
		try { 
	        res = Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    if(res>=0)
	    	return true;
	    else
	    	return false;
	}
	
	public int getMesesAntes(){
		
		FichaProcesoDao fpDao = new FichaProcesoDao();
		Collection<Element> fp = fpDao.values();
		
		if(fp == null || fp.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se ha encontrado la ficha de proceso en el fichero correspondiente.");
			throw new Solvencia2Excepcion(inci);
		}
		
		if(fp.size() > 1){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("11");
			inci.setInfAmpliada("Se han encontrado varias fichas de proceso en el fichero correspondiente.");
			throw new Solvencia2Excepcion(inci);
		}
		
		Iterator<Element> itFP= fp.iterator();
		FichaProceso elementoFP = (FichaProceso) itFP.next().getObjectValue();
		if(elementoFP.getRegistroParametros()==null){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("12");
			inci.setInfAmpliada("La ficha de proceso no tiene informado el registro 4, se ha cogido el valor 13 por defecto para los meses antes.");
			throw new Solvencia2Excepcion(inci);
		}
		if(!isPositiveInteger(elementoFP.getRegistroParametros().getindicadormeses())){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("12");
			inci.setInfAmpliada("El campo indicador meses de la ficha de proceso no tiene un formato correcto, se ha cogido el valor 13 por defecto.");
			throw new Solvencia2Excepcion(inci);
		}
		return Integer.parseInt(elementoFP.getRegistroParametros().getindicadormeses());
	}
}
