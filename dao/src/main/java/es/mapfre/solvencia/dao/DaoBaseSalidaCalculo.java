package es.mapfre.solvencia.dao;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tangosol.net.GuardSupport;
import com.tangosol.util.Filter;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.utils.beanio.BeanIOWriter;

public abstract class DaoBaseSalidaCalculo extends DaoBase implements DaoSalidaCalculo {
	private Logger logger = LoggerFactory.getLogger(DaoBaseSalidaCalculo.class);
	
	public Set keySet() {
		return this.getCache().keySet();
	}
	
	public Set keySet(Filter filter) {
		return this.getCache().keySet(filter);
	}
	
	protected Comparator exportOrdered() {
		return null;
	}
	
	protected Filter exportFiltered(FichaProceso ficha) {
		return exportFiltered();
	}
	
	protected Filter exportFiltered() {
		return null;
	}
	
	@Override
	public void exportCachePorBT(Map<String, BeanIOWriter> outs, FichaProceso ficha) { 
		Filter filter = exportFiltered(ficha);
		Comparator comparator = exportOrdered();
		
		Set keys = null;
		
		if (filter != null) {
			keys = keySet(filter);
		} else {
			keys = keySet();
		}
		
		if (keys != null && keys.size() > 0) {
			logger.info("Preparando para exportar {} registros de {}...", keys.size(), getCacheName());
			
			if (comparator != null) {
				SortedSet ss = new TreeSet(comparator);
				ss.addAll(keys);
				keys = ss;
			}
			
			if (keys != null) {
				Set buffer = new HashSet(ConstantesSolvencia.EXPORT_CHUNK_SIZE);
				
				for (Iterator it = keys.iterator(); it.hasNext();) {
					buffer.add(it.next());
					
					if (buffer.size() >= ConstantesSolvencia.EXPORT_CHUNK_SIZE) {
						processKeys(buffer, outs, comparator);
						
						buffer.clear();
					}
					
					// Avisamos al Guardian que no estamos bloqueados
					GuardSupport.heartbeat();
				}
				
				// Extraemos la última parte de la caché
				if (!buffer.isEmpty()) {
					processKeys(buffer, outs, comparator);
				}
				
				for(BeanIOWriter writer : outs.values()) {
					writer.flush();
				}
			}
	
			logger.info("Finalizada la exportación de {} registros de {}.", keys.size(), getCacheName());
		} else {
			logger.info("No hay registros de {} que exportar en este nodo.", getCacheName());
		}
	}
	
	private void processKeys(Set buffer, Map<String, BeanIOWriter> outs, Comparator comparator) {
		Map<Object, EntidadBase> entries = getAll(buffer);
		if (comparator != null) {
			TreeMap orderedMap = new TreeMap(comparator);
			orderedMap.putAll(entries);
			entries = orderedMap;
		}
		
		Collection<EntidadBase> values = entries.values();
		
		for (EntidadBase value : values) {
			if (value instanceof EntidadConBaseTec) {
				if (((EntidadConBaseTec) value).getBt() != null) {
					outs.get(((EntidadConBaseTec) value).getBt()).write(value);
				} else {
					logger.warn("No se puede extraer el registro {} puesto que no dispone de BT.", value);
				}
			} else {
				for (BeanIOWriter out : outs.values()) {
					out.write(value);
				}
			}
		}
		
		for (BeanIOWriter out : outs.values()) {
			out.flush();
		}
	}

	@Override
	public boolean clearAfterExport() {
		// Por defecto, las cachés se borran cuando se exportan
		return true;
	}
}
