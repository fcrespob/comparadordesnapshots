package es.mapfre.solvencia.entregables.aggregators;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.tangosol.net.BackingMapContext;
import com.tangosol.net.BackingMapManagerContext;
import com.tangosol.util.Binary;
import com.tangosol.util.BinaryEntry;
import com.tangosol.util.Converter;
import com.tangosol.util.aggregator.BigDecimalAverage;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.DetalleBaseTecnicaKey;
import es.mapfre.solvencia.dominio.maestro.BaseTecnicaInicial;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;

public class BasetecPgastExtAggregator extends BigDecimalAverage {
	
	private static final long serialVersionUID = 1863777740869515778L;
	
	private static final String CACHE_BASETECNICA_INICIAL = ConstantsEntregables.CACHE_BASETECNICA_INICIAL;
	
	
	/**
	 * 
	 */
	public BasetecPgastExtAggregator() {
		super();
	}

	
	@Override
	protected void process(Object o, boolean fFinal) {
		super.process(o, fFinal);
	}
	
	@Override
	protected Object finalizeResult (boolean fFinal) {
		return super.finalizeResult(fFinal);
	}

	
	@Override
	public Object aggregate(Set setEntries) {
		boolean fFinal = !m_fParallel;
		init(fFinal);
		
		for (Iterator iter = setEntries.iterator(); iter.hasNext();) {
			processEntry((BinaryEntry) iter.next());
		}

		return finalizeResult(fFinal);	
		
	}
	
	/**
	 * Procesa la entrada para acceder al BackingMapContext y extraer de la cache de baseTecnicaInicial el
	 * valor PgastExt
	 * 
	 * @param entry
	 */
	protected void processEntry(BinaryEntry entry) {
		BackingMapContext baseteciniBackingMapContext = entry.getContext().getBackingMapContext(CACHE_BASETECNICA_INICIAL);
		BackingMapManagerContext context = baseteciniBackingMapContext.getManagerContext();

		Converter converter = context.getValueFromInternalConverter();
		Converter keyConverter = context.getKeyToInternalConverter();
		
		UmicKey umicKey = ((DetalleBaseTecnicaKey) entry.getKey()).getUmicKey();
		
		Map baseteciniMap = baseteciniBackingMapContext.getBackingMap();
		
		Binary baseteciniBinary = (Binary) baseteciniMap.get(keyConverter.convert(umicKey));
		BaseTecnicaInicial basetecini = (BaseTecnicaInicial) converter.convert(baseteciniBinary);
		
		process(basetecini.getPgastgesex1I(), false);
			
	}

}