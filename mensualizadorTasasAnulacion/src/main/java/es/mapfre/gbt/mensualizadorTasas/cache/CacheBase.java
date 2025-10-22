package es.mapfre.gbt.mensualizadorTasas.cache;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import es.mapfre.gbt.mensualizadorTasas.dominio.EntidadBase;
import es.mapfre.gbt.mensualizadorTasas.utils.BeanIOReader;
import es.mapfre.gbt.mensualizadorTasas.utils.BeanIOWriter;
import es.mapfre.gbt.mensualizadorTasas.utils.ConstantesMensualizador;

public class CacheBase {

	CacheManager singletonManager = null;
	String cacheName;

	public CacheBase() {
		super();
		if (singletonManager == null) {
			InputStream config = ClassLoader
					.getSystemResourceAsStream(ConstantesMensualizador.EHCACHE_CONFIG);
			singletonManager = CacheManager.create(config);
		}
	}

	public String getCacheName() {
		return cacheName;
	}

	public String getNombreServicio() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	public Ehcache getEhcache() {
		return singletonManager.getEhcache(getCacheName());
	}

	public int size() {
		return getEhcache().getSize();
	}

	public boolean isEmpty() {
		return getEhcache().getSize() == 0;
	}

	public boolean containsKey(Object key) {
		return getEhcache().getKeys().contains(key);
	}

	public boolean containsValue(Object value) {
		return getEhcache().getAll(getEhcache().getKeys()).containsValue(value);
	}

	public void clear() {
		singletonManager.removeCache(getCacheName());
	}

	public Map getAll(Set keys) {
		return getEhcache().getAll(keys);
	}

	public Map getAll() {
		return getEhcache().getAll(getEhcache().getKeys());
	}

	public void putAll(Collection<Element> entries) {
		getEhcache().putAll(entries);
	}

	public void loadCache(BeanIOReader reader) {
		EntidadBase valor = null;
		Collection<Element> valores = new ArrayList<Element>();
		int bloque = 0;
		while ((valor = (EntidadBase) reader.read()) != null) {
			Element element = new Element(valor.getKey(), valor);
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

	public void exportCache(BeanIOWriter writer) {
		for (Object value : getAll().entrySet()) {
			writer.write(value);
		}
		writer.flush();
	}
}