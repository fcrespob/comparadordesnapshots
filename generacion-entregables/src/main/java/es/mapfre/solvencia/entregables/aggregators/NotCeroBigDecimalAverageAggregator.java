package es.mapfre.solvencia.entregables.aggregators;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.util.BinaryEntry;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.aggregator.BigDecimalAverage;

import es.mapfre.solvencia.entregables.impl.EntregableBASETEC;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;

/**
 * Calcula el promedio de un set de entradas formado por List<Bigdecimal> para
 * los valores contenidos en la posición i.
 *
 */
public class NotCeroBigDecimalAverageAggregator extends BigDecimalAverage {

	private static final long serialVersionUID = 736198053897050530L;
	
	private static final Logger LOG = LoggerFactory.getLogger(NotCeroBigDecimalAverageAggregator.class);

	ValueExtractor extractor;

	/**
	 * 
	 */
	public NotCeroBigDecimalAverageAggregator() {
		super();
	}

	/**
	 * 
	 * @param index
	 * @param extractor
	 */
	public NotCeroBigDecimalAverageAggregator(ValueExtractor extractor) {
		this.extractor = extractor;
	}

	@Override
	protected void process(Object o, boolean fFinal) {

		super.process(o, fFinal);
	}

	@Override
	protected Object finalizeResult(boolean fFinal) {
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
	 * Procesa la entrada para extraer el valor itcalc(i)
	 * 
	 * @param entry
	 */
	protected void processEntry(BinaryEntry entry) {

		BigDecimal value = (BigDecimal) entry.extract(extractor); // 221

		if (value != null && !(value.compareTo(BigDecimal.ZERO)==0)) {
			process(ensureBigDecimal(value), false);
		}

	}

	@Override
	public void readExternal(PofReader arg0) throws IOException {
		super.readExternal(arg0);
		this.extractor = (ValueExtractor) arg0.readObject(ConstantsFunciones.CTE_11);
	}

	@Override
	public void writeExternal(PofWriter arg0) throws IOException {
		super.writeExternal(arg0);
		arg0.writeObject(ConstantsFunciones.CTE_11, this.extractor);
	}

}