package es.mapfre.solvencia.coherence.partition;

import com.tangosol.net.partition.BroadKeyPartitioningStrategy;

public class KeyPartitioningStrategy extends BroadKeyPartitioningStrategy {

	private static int DEFAULT_SPAN = 1;
	
	public KeyPartitioningStrategy() {
		super(DEFAULT_SPAN);
	}
	
	public KeyPartitioningStrategy(int nSpan) {
		super(nSpan);
	}

}
