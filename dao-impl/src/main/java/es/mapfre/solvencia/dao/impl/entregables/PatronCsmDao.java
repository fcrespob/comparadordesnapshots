package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import es.mapfre.solvencia.coherence.keys.entregables.PatronCsmKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.PatronCsm;

public class PatronCsmDao extends DaoBaseSalidaCalculo implements Map<PatronCsmKey, PatronCsm> {

private static final String CACHE_NAME = "patroncsm";

public PatronCsmDao() {
	super();
	super.setCacheName(CACHE_NAME);
}

@Override
public PatronCsm get(Object key) {
	if (key instanceof PatronCsmKey) {
		return (PatronCsm) getCache().get(key);
	}
	return null;
}

@Override
public PatronCsm put(PatronCsmKey key, PatronCsm value) {
	this.getCache().putAll(Collections.singletonMap(key, value));
	return value;
}

@Override
public PatronCsm remove(Object key) {
	return (PatronCsm) this.getCache().remove(key);
}

@Override
public Collection<PatronCsm> values() {
	return (Collection<PatronCsm>) this.getCache().values();
}

@Override
public Set<PatronCsmKey> keySet() {
	return this.getCache().keySet();
}

@Override
public Set<java.util.Map.Entry<PatronCsmKey, PatronCsm>> entrySet() {
	return (Set<Entry<PatronCsmKey, PatronCsm>>) this.getCache().entrySet();
}

@Override
protected Comparator exportOrdered() {

	return new Comparator<PatronCsmKey>() {

		@Override
		public int compare(PatronCsmKey dc1, PatronCsmKey dc2) {
			if (dc1 == null && dc2 != null) {
				return 1;
			} else if (dc1 != null) {
				return dc1.compareTo(dc2);
			} else if (dc1 == null) {
				return -1;
			}
			return 0;
		}

	};
}
}
