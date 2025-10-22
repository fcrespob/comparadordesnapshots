package es.mapfre.solvencia.entregables.processors;

import java.io.IOException;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.InvocableMap.Entry;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.MultiExtractor;
import com.tangosol.util.processor.AbstractProcessor;
import com.tangosol.util.processor.ExtractorProcessor;

import es.mapfre.solvencia.coherence.keys.entregables.DetalleCorrienteEntregablesKey;
import es.mapfre.solvencia.coherence.keys.maestro.DatosAdicionalesCoaseguroKey;
import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.DetalleBaseTecnicaKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.TotalesFlujosKey;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;

/**
 * Extrae valores de la cache Umic y Detalle-baseTecnica a partir de una entrada de la cache de totales 
 *
 */
public class PrvBtProcessor extends AbstractProcessor implements PortableObject {

	private static final long serialVersionUID = -64098741131384479L;

	private String bt;
	private String cacheName;
	private ValueExtractor[] extractors;
	
	
	/**
	 * 
	 */
	public PrvBtProcessor() {
		super();
	}
	
	
	/**
	 * 
	 * @param extractors
	 */
	public PrvBtProcessor(String bt, String cacheName, ValueExtractor[] extractors) {
		super();
		this.bt = bt;
		this.cacheName = cacheName;
		this.extractors = extractors;
	}
	

	@Override
	public Object process(Entry entry) {
		
		NamedCache cache = CacheFactory.getCache(cacheName);
		
		ValueExtractor multiExtractor = new MultiExtractor(extractors);
		
		Object key = entry.getKey();
		UmicKey umicKey = null;
		
		if (key instanceof TotalesFlujosKey) {
			umicKey = ((TotalesFlujosKey) key).getUmicKey();
		} else if (key instanceof DetalleCorrienteEntregablesKey) {
			umicKey = ((DetalleCorrienteEntregablesKey) key).getUmicKey();
		} else if (key instanceof UmicKey) {
			umicKey = (UmicKey) key;
		}
		
		if (cacheName.equals(ConstantsEntregables.CACHE_DETALLE_BASETECNICA)) {	
			DetalleBaseTecnicaKey detalleKey = new DetalleBaseTecnicaKey(umicKey, bt);
		
			return cache.invoke(detalleKey, new ExtractorProcessor(multiExtractor));
		} if (cacheName.equals(ConstantsEntregables.CACHE_DATOS_ADICIONALES_COASEGURO)) {
			DatosAdicionalesCoaseguroKey coasKey = new DatosAdicionalesCoaseguroKey(umicKey.getKpoliza(), umicKey.getKsubpoliza());
			
			return cache.invoke(coasKey, new ExtractorProcessor(multiExtractor));
		} else {
			return cache.invoke(umicKey, new ExtractorProcessor(multiExtractor));
		}
				
	}


	
	@Override
	public void readExternal(PofReader arg0) throws IOException {
		this.bt = arg0.readString(ConstantsFunciones.CTE_1);
		this.cacheName = arg0.readString(ConstantsFunciones.CTE_2);
		this.extractors = (ValueExtractor[]) arg0.readObjectArray(ConstantsFunciones.CTE_3, new ValueExtractor[0]);
	}


	@Override
	public void writeExternal(PofWriter arg0) throws IOException {
		arg0.writeString(ConstantsFunciones.CTE_1, this.bt);
		arg0.writeString(ConstantsFunciones.CTE_2, this.cacheName);
		arg0.writeObjectArray(ConstantsFunciones.CTE_3, this.extractors);		
	}
	
}
