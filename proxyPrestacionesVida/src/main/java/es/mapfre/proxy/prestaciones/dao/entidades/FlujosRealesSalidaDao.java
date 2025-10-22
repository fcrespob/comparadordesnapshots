package es.mapfre.proxy.prestaciones.dao.entidades;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import es.mapfre.proxy.prestaciones.dao.DaoBase;
import es.mapfre.proxy.prestaciones.dominio.entidades.FlujosRealesSalida;
import es.mapfre.proxy.prestaciones.dominio.keys.FlujosRealesSalidaKey;
import es.mapfre.proxy.prestaciones.utils.beanio.BeanIOReader;
import net.sf.ehcache.Element;

public class FlujosRealesSalidaDao extends DaoBase{

	private static final String CACHE_NAME = "REASAL0";
	
	public FlujosRealesSalidaDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	public Set<Entry<FlujosRealesSalidaKey, FlujosRealesSalida>> entrySet() {
		return (Set<Entry<FlujosRealesSalidaKey, FlujosRealesSalida>>) this.getCache().getAll(getCache().getKeys());
	}

	public FlujosRealesSalida get(Object key) {
		if (!(key instanceof FlujosRealesSalidaKey)) {
			return null;
		} else {
			return (FlujosRealesSalida) this.getCache().get(key).getObjectValue();
		}
	}

	public List<FlujosRealesSalidaKey> keySet() {
		return this.getCache().getKeys();
	}

	public FlujosRealesSalida put(FlujosRealesSalidaKey key, FlujosRealesSalida flujosReales) {
		 this.getCache().put(new Element(key, flujosReales));
		 return flujosReales;
	}

	public boolean remove(Object key) {
		return this.getCache().remove(key);
	}

	public Collection<Element> values() {
		return (Collection<Element>) this.getAll().values();
	}
	
	public void loadCache(BeanIOReader reader) {
		FlujosRealesSalida flujosReales = null;
		Set<Element> valores = new HashSet<Element>();
		int bloque = 0;
//		while ((flujosReales = (FlujosReales) reader.read()) != null) {
//			// Se asigna el ámbito de la ficha a los filtros de tipo 2
//			List<FiltroFichaProceso> filtrosTipo02 = new ArrayList<FiltroFichaProceso>();
//			for (FiltroFichaProceso filtroTipo02 : flujosReales.getFiltrosAmbito()) {
//				filtroTipo02.setKtipoamb(flujosReales.getKtipoamb());
//				filtrosTipo02.add(filtroTipo02);
//			}
//			flujosReales.setFiltrosAmbito(filtrosTipo02);
//			valores.add(new Element(flujosReales.getKey(), flujosReales));
//			bloque++;
//			if (bloque % ConstantesSolvencia.BATCH_SIZE == 0) {
//				this.putAll(valores);
//				valores.clear();
//			}
//		}
		if (valores.size() > 0) {
			this.putAll(valores);
			valores.clear();
		}
	}
	
	public Comparator<FlujosRealesSalida> prestOrdered() {
		return new Comparator<FlujosRealesSalida>() {

			@Override
			public int compare(FlujosRealesSalida uk1, FlujosRealesSalida uk2) {
				if (uk1.getNumExp() != null && uk2.getNumExp() != null) {
					if (uk1.getNumExp().compareTo(uk2.getNumExp()) == 0) {
						if (uk1.getNumMvto().compareTo(uk2.getNumMvto()) == 0) {
							return 1;
						} else {
							return uk1.getNumMvto().compareTo(uk2.getNumMvto());
						}
					} else {
						return uk1.getNumExp().compareTo(uk2.getNumExp());
					}
				}
				return 0;
			}

		};
	}
	
}
