package es.mapfre.solvencia.entregables.aggregators;

import java.util.Collection;
import java.util.Set;

import com.tangosol.util.LiteSet;
import com.tangosol.util.NullImplementation;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.aggregator.AbstractAggregator;

public class FirstItemAggregator extends AbstractAggregator {
	protected transient Set m_set;

	public FirstItemAggregator() {
	}

	public FirstItemAggregator(ValueExtractor extractor) {
		super(extractor);
	}

	public FirstItemAggregator(String sMethod) {
		super(sMethod);
	}

	protected void init(boolean fFinal) {
		Set set = m_set;
		if (set != null) {
			set.clear();
		}
	}

	protected void process(Object o, boolean fFinal) {
		if (o != null) {
			if (fFinal) {

				Collection colPartial = (Collection) o;
				if (!colPartial.isEmpty() && ensureSet().isEmpty()) {
					ensureSet().add(colPartial.iterator().next());
				}

			} else {
				if (ensureSet().isEmpty()) {
					ensureSet().add(o);
				}
			}
		}
	}

	protected Object finalizeResult(boolean fFinal) {
		Set set = m_set;
		m_set = null;

		if (fFinal) {

			return set == null ? NullImplementation.getSet() : set;
		}

		return set;
	}

	protected Set ensureSet() {
		Set set = m_set;
		if (set == null) {
			set = m_set = new LiteSet();
		}
		return set;
	}
}
