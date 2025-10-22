package es.mapfre.solvencia.entregables.aggregators;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.util.BinaryEntry;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.aggregator.ComparableMax;

import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;

/**
 * Calcula el valor máximo de un set de entradas formado por List<Bigdecimal>
 * para los valores contenidos en la posición i.
 * 
 */
public class ListBigDecimalMaxAggregator extends ComparableMax {

	private static final long serialVersionUID = -1036520575544653350L;
	ValueExtractor extractor;
	private Integer index;

	/**
	 * 
	 */
	public ListBigDecimalMaxAggregator() {
		super();
	}

	/**
	 * 
	 * @param index
	 * @param extractor
	 */
	public ListBigDecimalMaxAggregator(Integer index, ValueExtractor extractor) {
		this.extractor = extractor;
		this.index = index;
	}

	@Override
	protected void process(Object o, boolean fFinal) {
		super.process(o, fFinal);
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

		List<BigDecimal> value = (List<BigDecimal>) entry.extract(extractor);

		if (value.size() >= index + 1 && value.get(index) != null) {
			process(value.get(index), false);
		}

	}

	@Override
	public void readExternal(PofReader arg0) throws IOException {
		this.index = arg0.readInt(ConstantsFunciones.CTE_1);
		this.extractor = (ValueExtractor) arg0.readObject(ConstantsFunciones.CTE_2);
	}

	@Override
	public void writeExternal(PofWriter arg0) throws IOException {
		arg0.writeInt(ConstantsFunciones.CTE_1, this.index);
		arg0.writeObject(ConstantsFunciones.CTE_2, this.extractor);
	}

}