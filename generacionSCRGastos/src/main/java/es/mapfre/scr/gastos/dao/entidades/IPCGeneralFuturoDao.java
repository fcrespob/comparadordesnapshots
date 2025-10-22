package es.mapfre.scr.gastos.dao.entidades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import es.mapfre.scr.gastos.dao.DaoBase;
import es.mapfre.scr.gastos.dominio.entidades.IPCGeneralFuturo;
import es.mapfre.scr.gastos.dominio.keys.IPCGeneralFuturoKey;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;


public class IPCGeneralFuturoDao extends DaoBase{
	
	private static final String CACHE_NAME = "IPC0"; 

	public IPCGeneralFuturoDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	public Set<Entry<IPCGeneralFuturoKey, IPCGeneralFuturo>> entrySet() {
		return (Set<Entry<IPCGeneralFuturoKey, IPCGeneralFuturo>>) this.getCache().getAll(getCache().getKeys());
	}

	public IPCGeneralFuturo get(Object key) {
		if (!(key instanceof IPCGeneralFuturoKey)) {
			return null;
		} else {
			return (IPCGeneralFuturo) this.getCache().get(key).getObjectValue();
		}
	}

	public Set<IPCGeneralFuturoKey> keySet() {
		return (Set<IPCGeneralFuturoKey>) this.getCache().getKeys();
	}

	public IPCGeneralFuturo put(IPCGeneralFuturoKey key, IPCGeneralFuturo ipcGeneralFuturo) {
		 this.getCache().put(new Element(key,ipcGeneralFuturo));
		 return ipcGeneralFuturo;
	}

	public boolean remove(Object key) {
		return this.getCache().remove(key);
	}

	public Collection<Element> values() {
		return (Collection<Element>) this.getAll().values();
	}
}