package es.mapfre.scr.tablasExperiencia.dao.entidades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.ehcache.Element;
import es.mapfre.scr.tablasExperiencia.dao.DaoBase;
import es.mapfre.scr.tablasExperiencia.dominio.entidades.FichaProceso;
import es.mapfre.scr.tablasExperiencia.dominio.entidades.FiltroFichaProceso;
import es.mapfre.scr.tablasExperiencia.dominio.keys.FichaProcesoKey;
import es.mapfre.scr.tablasExperiencia.utils.ConstantesSolvencia;
import es.mapfre.scr.tablasExperiencia.utils.beanio.BeanIOReader;

public class FichaProcesoDao extends DaoBase {

private static final String CACHE_NAME = "R340T000"; 
	
	public FichaProcesoDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	public Set<Entry<FichaProcesoKey, FichaProceso>> entrySet() {
		return (Set<Entry<FichaProcesoKey, FichaProceso>>) this.getCache().getAll(getCache().getKeys());
	}

	public FichaProceso get(Object key) {
		if (!(key instanceof FichaProcesoKey)) {
			return null;
		} else {
			return (FichaProceso) this.getCache().get(key).getObjectValue();
		}
	}

	public List<FichaProcesoKey> keySet() {
		return this.getCache().getKeys();
	}

	public FichaProceso put(FichaProcesoKey key, FichaProceso fichaProceso) {
		 this.getCache().put(new Element(key, fichaProceso));
		 return fichaProceso;
	}

	public boolean remove(Object key) {
		return this.getCache().remove(key);
	}

	public Collection<Element> values() {
		return (Collection<Element>) this.getAll().values();
	}
	
	public void loadCache(BeanIOReader reader) {
		FichaProceso fichaProceso = null;
		Set<Element> valores = new HashSet<Element>();
		int bloque = 0;
		while ((fichaProceso = (FichaProceso) reader.read()) != null) {
			// Se asigna el ámbito de la ficha a los filtros de tipo 2
			List<FiltroFichaProceso> filtrosTipo02 = new ArrayList<FiltroFichaProceso>();
			for (FiltroFichaProceso filtroTipo02 : fichaProceso.getFiltrosAmbito()) {
				filtroTipo02.setKtipoamb(fichaProceso.getKtipoamb());
				filtrosTipo02.add(filtroTipo02);
			}
			fichaProceso.setFiltrosAmbito(filtrosTipo02);
			valores.add(new Element(fichaProceso.getKey(), fichaProceso));
			bloque++;
			if (bloque % ConstantesSolvencia.BATCH_SIZE == 0) {
				this.putAll(valores);
				valores.clear();
			}
		}
		if (valores.size() > 0) {
			this.putAll(valores);
			valores.clear();
		}
	}
	
}