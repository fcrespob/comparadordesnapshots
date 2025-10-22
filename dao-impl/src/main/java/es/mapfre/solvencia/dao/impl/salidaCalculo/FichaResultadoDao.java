package es.mapfre.solvencia.dao.impl.salidaCalculo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.salidaCalculo.FichaResultadoKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.FichaResultado;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;
import es.mapfre.solvencia.utils.beanio.BeanIOWriter;

public class FichaResultadoDao extends DaoBase implements Map<FichaResultadoKey, List<FichaResultado>> {
	
	private static final String CACHE_NAME = "R340T003";
	
	public FichaResultadoDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public List<FichaResultado> get(Object key) {
		if (!(key instanceof FichaResultadoKey)) {
			return null;
		} else {
			return (List<FichaResultado>) this.getCache().get(key);
		}
	}

	@Override
	public List<FichaResultado> put(FichaResultadoKey key,
			List<FichaResultado> value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	public List<FichaResultado> put(FichaResultado ficha) {
		List<FichaResultado> fichasCache = get(ficha.getKey());
		List<FichaResultado> fichas = new ArrayList<FichaResultado>();
		if (fichasCache != null) {
			fichas.addAll(fichasCache);
		}
		fichas.add(ficha);
		return put(ficha.getKey(), fichas);
	}
	
	public FichaResultado crearRegistroFichaResultado(FichaProceso fichaProceso, String textoRegistro) {
		FichaResultadoKey key = new FichaResultadoKey(fichaProceso.getKejecucion(), 
				fichaProceso.getKsistema(), fichaProceso.getKprotecnico(), 
				fichaProceso.getKuejecucion(), fichaProceso.getKsecuencia());

		return crearRegistroFichaResultado(key, textoRegistro);
	}
	
	public FichaResultado crearRegistroFichaResultado(FichaResultadoKey key, String textoRegistro) {
				
		FichaResultado registroResultado = new FichaResultado();
		registroResultado.setKejecucion(key.getKejecucion());
		registroResultado.setKsistema(key.getKsistema());
		registroResultado.setKprotecnico(key.getKprotecnico());
		registroResultado.setKuejecucion(key.getKuejecucion());
		registroResultado.setKsecuencia(key.getKsecuencia());
		registroResultado.setKtipores("HIST");
		registroResultado.setKsecres(000);
		registroResultado.setGc1Resul(textoRegistro);
		
		return registroResultado;
	}
	
	
	@Override
	public List<FichaResultado> remove(Object key) {
		return (List<FichaResultado>) this.getCache().remove(key);
	}

	@Override
	public Set<FichaResultadoKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Collection<List<FichaResultado>> values() {
		return (Collection<List<FichaResultado>>) this.getCache().values();
	}

	@Override
	public Set<java.util.Map.Entry<FichaResultadoKey, List<FichaResultado>>> entrySet() {
		return (Set<java.util.Map.Entry<FichaResultadoKey, List<FichaResultado>>>) this.getCache().entrySet();
	}
	
	@Override
	public void loadCache(BeanIOReader reader) {
		FichaResultado ficha = null;
		while ((ficha = (FichaResultado) reader.read()) != null) {
			put(ficha);
		}
	}
	
	@Override
	public void exportCache(BeanIOWriter writer) {
		for (List<FichaResultado> fichas : values()) {
			for (FichaResultado ficha : fichas) {
				writer.write(ficha);
			}
		}
		writer.flush();
	}
}