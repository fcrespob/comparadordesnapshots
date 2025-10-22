package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.FichaProcesoKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FiltroFichaProceso;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

public class FichaProcesoDao extends DaoBase implements Map<FichaProcesoKey, FichaProceso>{

private static final String CACHE_NAME = "R340T000"; 
	
	public FichaProcesoDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<FichaProcesoKey, FichaProceso>> entrySet() {
		return (Set<Entry<FichaProcesoKey, FichaProceso>>) this.getCache().entrySet();
	}

	@Override
	public FichaProceso get(Object key) {
		if (!(key instanceof FichaProcesoKey)) {
			return null;
		} else {
			return (FichaProceso) this.getCache().get(key);
		}
	}

	@Override
	public Set<FichaProcesoKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public FichaProceso put(FichaProcesoKey key, FichaProceso fichaProceso) {
		 this.getCache().put(key, fichaProceso);
		 return fichaProceso;
	}

	@Override
	public FichaProceso remove(Object key) {
		return (FichaProceso) this.getCache().remove(key);
	}

	@Override
	public Collection<FichaProceso> values() {
		return (Collection<FichaProceso>) this.getCache().values();
	}
	
	@Override
	public void loadCache(BeanIOReader reader) {
		FichaProceso fichaProceso = null;
		Map<FichaProcesoKey, FichaProceso> valores = new HashMap<FichaProcesoKey, FichaProceso>();
		int bloque = 0;
		while ((fichaProceso = (FichaProceso) reader.read()) != null) {
			// Se asigna el ámbito de la ficha a los filtros de tipo 2
			List<FiltroFichaProceso> filtrosTipo02 = new ArrayList<FiltroFichaProceso>();
			for (FiltroFichaProceso filtroTipo02 : fichaProceso.getFiltrosAmbito()) {
				filtroTipo02.setKtipoamb(fichaProceso.getKtipoamb());
				filtrosTipo02.add(filtroTipo02);
			}
			fichaProceso.setFiltrosAmbito(filtrosTipo02);
			valores.put(fichaProceso.getKey(), fichaProceso);
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