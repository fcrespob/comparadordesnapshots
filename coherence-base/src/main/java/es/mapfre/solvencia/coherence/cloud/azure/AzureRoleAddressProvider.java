package es.mapfre.solvencia.coherence.cloud.azure;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.windowsazure.serviceruntime.Role;
import com.microsoft.windowsazure.serviceruntime.RoleEnvironment;
import com.microsoft.windowsazure.serviceruntime.RoleInstance;
import com.microsoft.windowsazure.serviceruntime.RoleInstanceEndpoint;
import com.oracle.common.net.InetSocketAddressComparator;

import es.mapfre.solvencia.coherence.cloud.CloudAddressProvider;
import es.mapfre.solvencia.coherence.cloud.CloudInetSocketAddressComparator;

public class AzureRoleAddressProvider extends CloudAddressProvider {

	/**
	 * The logger to use.
	 */
	private static final Logger logger = LoggerFactory.getLogger(AzureRoleAddressProvider.class.getName());

	private static final int ESPERA_PRIMER_INTENTO = 1000;

	private static final long FACTOR_CRECIMIENTO_ESPERA = 3;

	private static final int MAX_INTENTOS = 3;

	public static void main(String[] args) {
		try {
			AzureRoleAddressProvider arap = new AzureRoleAddressProvider();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}

	public AzureRoleAddressProvider() throws IOException {
		String regionName = System.getProperty(
		"tangosol.coherence.azureaddressprovider.region");//, Regions.US_EAST_1.getName());
		
		if (logger.isDebugEnabled()) {
			logger.debug("Initializing WKA list from Azure Instance tags for region {}.", regionName);
		}
		
		super.tryCached();

		if (wkaAddressList == null || wkaAddressList.size() == 0) {
			try {
				
				wkaAddressList = generateWKAList();
				Collections.sort(wkaAddressList, CloudInetSocketAddressComparator.INSTANCE);
				wkaIterator = wkaAddressList.iterator();
				
				generatePropertiesFromWKAList();
			} catch (RuntimeException ex) {
				logger.error("Error initializing Azure Client", ex);
				throw ex;
			}
		} else {
			logger.info("Loaded WKA List from cached file.");
		}
		
		logger.info("WKA List: {}", wkaAddressList);

	}

	/**
	 * Protected Constructor used for unit testing.
	 * 
	 * @param dummyDifferentiator
	 *            parameter to make this parameter list different from the
	 *            default constructor.
	 */
	protected AzureRoleAddressProvider(String dummyDifferentiator) {
		logger.trace("Invocado constructor {}", dummyDifferentiator);
	}

	/**
	 * Generates the WKA list using an Azure client
	 * 
	 * @return the WKA list
	 */
	private List<InetSocketAddress> generateWKAList() {
		logger.info("Is Azure available: {}", RoleEnvironment.isAvailable());
		
		String tagName = System.getProperty("tangosol.coherence.azureaddressprovider.rolename", "COHERENCE_SERVER");
		String portString = System.getProperty("tangosol.coherence.azureaddressprovider.port", "8088");
		int wkaPort = Integer.parseInt(portString);
		
		List<InetSocketAddress> addresses = new ArrayList<InetSocketAddress>();
		
//		Map<String, RoleInstance> roleInstances = RoleEnvironment.getCurrentRoleInstance().getRole().getInstances();
//
//		Collection<RoleInstance> instances = roleInstances.values();
//		
//		logAllInstances(instances);
//		
//		for (RoleInstance instance : instances) {
//			if (tagName.equals(instance.getRole().getName())) {
//				for (RoleInstanceEndpoint endpoint : instance.getInstanceEndpoints().values()) {
//					addresses.add(endpoint.getIpEndPoint());
//				}
//			}
//		}
		
		for (Role role : RoleEnvironment.getRoles().values()) {
			if (tagName.equals(role.getName())) {
				Collection<RoleInstance> instances = role.getInstances().values();
				
				logAllInstances(instances);
				
				for (RoleInstance instance : instances) {
					for (RoleInstanceEndpoint endpoint : instance.getInstanceEndpoints().values()) {
						addresses.add(new InetSocketAddress(endpoint.getIpEndPoint().getHostName(), wkaPort));
					}
				}
			}
		}

		
		return addresses;
	}

	/**
	 * Logs the instances we found.
	 * 
	 * @param instances
	 *            the instances we are to log
	 */
	private void logAllInstances(Collection<RoleInstance> instances) {
		if (logger.isTraceEnabled()) {
			logger.trace("The following instances were found:");
		}

		for (Iterator<RoleInstance> instIter = instances.iterator(); instIter.hasNext();) {
			logger.trace("Azure instance: {}", instIter.next());
		}
	}
}
