package es.mapfre.gbt.mensualizadorTasas.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.sf.ehcache.Element;
import es.mapfre.gbt.mensualizadorTasas.cache.CacheBase;
import es.mapfre.gbt.mensualizadorTasas.dominio.FichaProceso;
import es.mapfre.gbt.mensualizadorTasas.dominio.FiltroFichaProceso;
import es.mapfre.gbt.mensualizadorTasas.key.FichaProcesoKey;
import es.mapfre.gbt.mensualizadorTasas.utils.BeanIOReader;
import es.mapfre.gbt.mensualizadorTasas.utils.ConstantesMensualizador;

public class FichaProcesoDao extends CacheBase{

	private static final String CACHE_NAME = "R340T000"; 

	public FichaProcesoDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	public Set<Element> entrySet() {
		return (Set<Element>) this.getAll().values();
	}

	public FichaProceso get(Object key) {
		if (!(key instanceof FichaProcesoKey)) {
			return null;
		} else {
			return (FichaProceso) this.getEhcache().get(key).getObjectValue();
		}
	}

	public Set<FichaProcesoKey> keySet() {
		return (Set<FichaProcesoKey>) this.getEhcache().getKeys();
	}

	public FichaProceso put(FichaProcesoKey key, FichaProceso fichaProceso) {
		Element element = new Element(key, fichaProceso);
		this.getEhcache().put(element);
		return fichaProceso;
	}

	public boolean remove(Object key) {
		return this.getEhcache().remove(key);
	}
	
	public Collection<Element> values() {
		return (Collection<Element>) this.getAll().values();
	}

	@Override
	public void loadCache(BeanIOReader reader) {
		FichaProceso fichaProceso = null;
		Collection<Element> valores = new ArrayList<Element>();
		int bloque = 0;
		while ((fichaProceso = (FichaProceso) reader.read()) != null) {
			// Se asigna el ámbito de la ficha a los filtros de tipo 2
			List<FiltroFichaProceso> filtrosTipo02 = new ArrayList<FiltroFichaProceso>();
			for (FiltroFichaProceso filtroTipo02 : fichaProceso.getFiltrosAmbito()) {
				filtroTipo02.setKtipoamb(fichaProceso.getKtipoamb());
				filtrosTipo02.add(filtroTipo02);
			}
			fichaProceso.setFiltrosAmbito(filtrosTipo02);
			Element element = new Element(fichaProceso.getKey(), fichaProceso);
			valores.add(element);
			bloque++;
			if (bloque % ConstantesMensualizador.BATCH_SIZE == 0) {
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