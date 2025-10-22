package es.mapfre.solvencia.coherence.agent;

import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.net.AbstractInvocable;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.InvocationService;
import com.tangosol.net.Member;

/**
 * Agent that determines how much free memory a grid node has.
 */
@Portable
public class FreeMemAgent extends AbstractInvocable {
	private static Logger log = LoggerFactory.getLogger(FreeMemAgent.class);
	
	public void run() {
		Runtime runtime = Runtime.getRuntime();
		long cbFree = runtime.freeMemory()/(1024*1024);
		long cbTotal = runtime.totalMemory()/(1024*1024);

		System.gc();

		long cbFree1 = runtime.freeMemory()/(1024*1024);
		long cbTotal1 = runtime.totalMemory()/(1024*1024);

        log.info("Previous: {}MB/{}MB, Post {}MB/{}MB", cbFree, cbTotal, cbFree1, cbTotal1);
		setResult(new long[] { cbFree, cbTotal, cbFree1, cbTotal1 });
	}
	
	/**
	 * 
	 */
	public static void freeMem() {
		Map mapFreeMem = ((InvocationService) CacheFactory.getService("InvocationService")).query(new FreeMemAgent(), null);
		if (log.isDebugEnabled()) {
			Iterator iter = mapFreeMem.entrySet().iterator();
			while (iter.hasNext()) {
			    Map.Entry entry  = (Map.Entry) iter.next();
			    Member    member = (Member) entry.getKey();
			    long[]     anInfo = (long[]) entry.getValue();
			    if (anInfo != null) {// nullif member died
			        log.debug("Member: {}-{}, Previous: {}MB/{}MB, Post {}MB/{}MB", member.getMachineName(), member.getMemberName(), anInfo[0], anInfo[1], anInfo[2], anInfo[3]);
			    }
			}
		}
	}

}