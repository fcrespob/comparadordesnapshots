package es.mapfre.solvencia.entregables.processors;

import java.io.IOException;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.InvocableMap.Entry;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.processor.AbstractProcessor;
import com.tangosol.util.processor.ExtractorProcessor;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.TotalesFlujosKey;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;

/**
 * Extrae valores de la cache Umic a partir de una entrada de la cache de totales 
 *
 */
public class PrvUmicProcessor extends AbstractProcessor implements PortableObject {

	private static final long serialVersionUID = -1664911800600011322L;
	
	private static final String CACHE_FECHAS = ConstantsEntregables.CACHE_FECHAS;
		
	private ValueExtractor extractor;
	

	/**
	 * 
	 */
	public PrvUmicProcessor() {
		super();
	}
	
	
	/**
	 * 
	 * @param extractor
	 */
	public PrvUmicProcessor(ValueExtractor extractor) {
		super();
		this.extractor = extractor;
	}
	
	
	@Override
	public Object process(Entry entry) {
		
		NamedCache fechas = CacheFactory.getCache(CACHE_FECHAS);
		
		// Obtenemos la clave de la umic a partir de la de totales
		UmicKey umicKey = ((TotalesFlujosKey) entry.getKey()).getUmicKey();
		
		return fechas.invoke(umicKey, new ExtractorProcessor(extractor));
	}

	
	@Override
	public void readExternal(PofReader arg0) throws IOException {
		this.extractor = (ValueExtractor) arg0.readObject(ConstantsFunciones.CTE_1);	
	}

	
	@Override
	public void writeExternal(PofWriter arg0) throws IOException {
		arg0.writeObject(ConstantsFunciones.CTE_1, this.extractor);
	}

}
