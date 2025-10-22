package es.mapfre.solvencia.coherence.cloud;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Comparator;

import com.oracle.common.net.InetSocketAddress32;

public class CloudInetSocketAddressComparator implements Comparator<SocketAddress> {
	public static final CloudInetSocketAddressComparator INSTANCE = new CloudInetSocketAddressComparator();

	public int compare(SocketAddress addrA, SocketAddress addrB) {
		if (addrA == addrB) {
			return 0;
		}
		if (addrA == null) {
			return -1;
		}
		if (addrB == null) {
			return 1;
		}

		InetAddress ipA = getAddress(addrA);
		InetAddress ipB = getAddress(addrB);
		int n;
		if ((ipA == null) && (ipB == null)) {
			String sHostA = getHostName(addrA);
			String sHostB = getHostName(addrB);
			n = sHostB == null ? 1 : sHostA == null ? -1 : sHostA == sHostB ? 0 : sHostA.compareTo(sHostB);
		} else {

			if ((ipA != null) && (ipB != null)) {
				n = CloudInetAddressComparator.INSTANCE.compare(ipA, ipB);
			} else {
				throw new IllegalArgumentException("cannot compare resolved to unresolved addresses");
			}
		}

		return n == 0 ? getPort(addrA) - getPort(addrB) : n;
	}

	static String getHostName(SocketAddress addr) {
		return (addr instanceof InetSocketAddress) ? ((InetSocketAddress) addr).getHostName()
				: ((InetSocketAddress32) addr).getHostName();
	}

	static InetAddress getAddress(SocketAddress addr) {
		return (addr instanceof InetSocketAddress) ? ((InetSocketAddress) addr).getAddress()
				: ((InetSocketAddress32) addr).getAddress();
	}

	static int getPort(SocketAddress addr) {
		return (addr instanceof InetSocketAddress) ? ((InetSocketAddress) addr).getPort()
				: ((InetSocketAddress32) addr).getPort();
	}

}