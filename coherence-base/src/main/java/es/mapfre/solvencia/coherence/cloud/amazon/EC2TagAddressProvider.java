package es.mapfre.solvencia.coherence.cloud.amazon;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.DescribeNetworkInterfacesRequest;
import com.amazonaws.services.ec2.model.DescribeNetworkInterfacesResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceNetworkInterface;
import com.amazonaws.services.ec2.model.NetworkInterface;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.Tag;

import es.mapfre.solvencia.coherence.cloud.CloudAddressProvider;
import es.mapfre.solvencia.coherence.cloud.CloudInetSocketAddressComparator;

public class EC2TagAddressProvider extends CloudAddressProvider {

	/**
	 * The logger to use.
	 */
	private static final Logger logger = LoggerFactory.getLogger(EC2TagAddressProvider.class.getName());

	private static final int ESPERA_PRIMER_INTENTO = 1000;

	private static final long FACTOR_CRECIMIENTO_ESPERA = 3;

	private static final int MAX_INTENTOS = 3;

	public static void main(String[] args) {
		try {
			EC2TagAddressProvider etap = new EC2TagAddressProvider();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public EC2TagAddressProvider() throws IOException {
		String regionName = System.getProperty(
		"tangosol.coherence.ec2addressprovider.region", Regions.US_EAST_1.getName());
		
		if (logger.isDebugEnabled()) {
			logger.debug("Initializing WKA list from EC2 Instance tags for region {}.", regionName);
		}
		
		super.tryCached();

		if (wkaAddressList == null || wkaAddressList.size() == 0) {
			try {
				AWSCredentials credentials = determineCredentials();
				ClientConfiguration clientConfiguration = new ClientConfiguration();
				
				// Miramos si se ha configurado el proxy
				Boolean proxySet = Boolean.valueOf(System.getProperty(
						"proxySet", "false"));
				Boolean https_proxySet = Boolean.valueOf(System.getProperty(
						"https.proxySet", "false"));
				if (Protocol.HTTP.equals(clientConfiguration.getProtocol()) && proxySet) {
					String proxyHost = System.getProperty("proxyHost");
					Integer proxyPort = Integer.valueOf(System.getProperty("proxyPort"));
	
					clientConfiguration.setProxyHost(proxyHost);
					clientConfiguration.setProxyPort(proxyPort);
					
					String proxyUser = System.getProperty("http.proxyUser");
					String proxyPass = System.getProperty("http.proxyPass");
					if (!StringUtils.isEmpty(proxyUser)) {
						clientConfiguration.setProxyUsername(proxyUser);
					}
					if (!StringUtils.isEmpty(proxyPass)) {
						clientConfiguration.setProxyPassword(proxyPass);
					}
					
				} else if (Protocol.HTTPS.equals(clientConfiguration.getProtocol()) && https_proxySet) {
					String https_proxyHost = System.getProperty("https.proxyHost");
					Integer https_proxyPort = Integer.valueOf(System.getProperty("https.proxyPort"));
					
					clientConfiguration.setProxyHost(https_proxyHost);
					clientConfiguration.setProxyPort(https_proxyPort);
					
					String https_proxyUser = System.getProperty("https.proxyUser");
					String https_proxyPass = System.getProperty("https.proxyPass");
					if (!StringUtils.isEmpty(https_proxyUser)) {
						clientConfiguration.setProxyUsername(https_proxyUser);
					}
					if (!StringUtils.isEmpty(https_proxyPass)) {
						clientConfiguration.setProxyPassword(https_proxyPass);
					}
					
				}
				
				AmazonEC2Client ec2 = new AmazonEC2Client(credentials, clientConfiguration);
				Region region = Region.getRegion(Regions.fromName(regionName));
				ec2.setRegion(region);
				
				wkaAddressList = generateWKAList(ec2);
				Collections.sort(wkaAddressList, CloudInetSocketAddressComparator.INSTANCE);
				wkaIterator = wkaAddressList.iterator();
				
				generatePropertiesFromWKAList();
			} catch (RuntimeException ex) {
				logger.error("Error initializing EC2 Client", ex);
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
	protected EC2TagAddressProvider(String dummyDifferentiator) {
		logger.trace("Invocado constructor {}", dummyDifferentiator);
	}

	/**
	 * Generates the WKA list using an AmazonEC2 client.
	 * 
	 * @param ec2
	 *            the {@link AmazonEC2} client to use.
	 * 
	 * @return the WKA list
	 */
	private List<InetSocketAddress> generateWKAList(AmazonEC2 ec2) {
		DescribeInstancesRequest request = new DescribeInstancesRequest();

		String tagName = System.getProperty("tangosol.coherence.ec2tagaddressprovider.tagname", "Role");
		String tagValue = System.getProperty("tangosol.coherence.ec2tagaddressprovider.tagvalue", "COHERENCE_SERVER");

		Set<Instance> instances = null;
		List<InetSocketAddress> resultList = null;
		// Tiempo de espera en ms. Crece exponencialmente.
		long expWait = ESPERA_PRIMER_INTENTO;
		int intentos = 1;
		do {
			try {
				DescribeInstancesResult describeInstancesResult = ec2.describeInstances(request.withFilters(new Filter("tag:"
						+ tagName).withValues(tagValue)));
				List<Reservation> reservations = describeInstancesResult.getReservations();
				instances = new HashSet<Instance>();
	
				for (Reservation reservation : reservations) {
					instances.addAll(reservation.getInstances());
	
					if (logger.isTraceEnabled()) {
						logger.trace("Examining EC2 reservation: {}", reservation);
					}
				}
	
				logAllInstances(instances);
			} catch (AmazonServiceException e) {
				logger.warn("Error retrieving available instances in attempt {}: {}-{}", intentos, e.getErrorCode(), e.getMessage());
				if ("RequestLimitExceeded".equals(e.getErrorCode())) {
					try {
						Thread.sleep(expWait);
					} catch (InterruptedException e1) {
					}
					expWait *= FACTOR_CRECIMIENTO_ESPERA;
				} else {
					throw e;
				}
			}
		} while (instances == null && intentos++ < MAX_INTENTOS);
		
		if (instances != null && instances.size() > 0) {
			resultList = filterInstances(ec2, instances);
		} else {
			throw new RuntimeException("The EC2TagAddressProvider could not find any available instance with " + tagName + ":" + tagValue + " tags");
		}

		return resultList;
	}

	/**
	 * Method that filter the instances and network resources to match the
	 * configured tags
	 * 
	 * @param instances
	 * @return the filtered networkAddresses
	 */
	private List<InetSocketAddress> filterInstances(AmazonEC2 ec2, Set<Instance> instances) {
		List<InetSocketAddress> resultList = new ArrayList<InetSocketAddress>();

		String tagNameInterface = System.getProperty("tangosol.coherence.ec2tagaddressprovider.interface.tagname",
				"Name");
		String tagValueInterface = System.getProperty("tangosol.coherence.ec2tagaddressprovider.interface.tagvalue",
				"Interconnect");

		for (Iterator<Instance> instIter = instances.iterator(); instIter.hasNext();) {
			Instance instance = instIter.next();

			if (logger.isTraceEnabled()) {
				logger.trace("EC2TagAddressProvider - adding {0} from instance {1} to WKA list", new Object[] {
						instance.getPrivateIpAddress(), instance });
			}

			DescribeNetworkInterfacesRequest netIfaceRequest = new DescribeNetworkInterfacesRequest();

			List<String> interfaceIds = new ArrayList<String>();
			for (InstanceNetworkInterface instanceNetworkInterface : instance.getNetworkInterfaces()) {
				interfaceIds.add(instanceNetworkInterface.getNetworkInterfaceId());
			}
			
			DescribeNetworkInterfacesResult interfaces = null;
			// Tiempo de espera en ms. Crece exponencialmente.
			long expWait = ESPERA_PRIMER_INTENTO;
			int intentos = 1;
			do {
				try {
					interfaces = ec2.describeNetworkInterfaces(netIfaceRequest
							.withNetworkInterfaceIds(interfaceIds));
		
					String portString = System.getProperty("tangosol.coherence.ec2addressprovider.port", "8088");
					int wkaPort = Integer.parseInt(portString);
		
					for (NetworkInterface networkInterface : interfaces.getNetworkInterfaces()) {
						if (networkInterface.getTagSet() != null) {
							for (Tag tag : networkInterface.getTagSet()) {
								if (tag.getKey().equals(tagNameInterface) && tag.getValue().equals(tagValueInterface)) {
									resultList.add(new InetSocketAddress(networkInterface.getPrivateIpAddress(), wkaPort));
								}
							}
						}
					}
				} catch (AmazonServiceException e) {
					logger.warn("Error retrieving available interfaces for instance {} in attempt {}: {}-{}", instance.getInstanceId(), intentos, e.getErrorCode(), e.getMessage());
					if ("RequestLimitExceeded".equals(e.getErrorCode())) {
						try {
							Thread.sleep(expWait);
						} catch (InterruptedException e1) {
						}
						expWait *= FACTOR_CRECIMIENTO_ESPERA;
					} else {
						throw e;
					}
				}
			} while (interfaces == null && intentos++ < MAX_INTENTOS);
		}

		if (resultList.size() == 0) {
			throw new RuntimeException("The EC2TagAddressProvider could not find any available instance with " + tagNameInterface + ":" + tagValueInterface + " interfaces");
		}

		return resultList;
	}

	/**
	 * This method determines what credentials to use for EC2 authentication.
	 * 
	 * @return the {@link AWSCredentials}
	 * 
	 * @throws IOException
	 *             if reading the property file fails
	 */
	protected AWSCredentials determineCredentials() throws IOException {
		String accessKey = System.getProperty("tangosol.coherence.ec2addressprovider.accesskey");
		String secretKey = System.getProperty("tangosol.coherence.ec2addressprovider.secretkey");

		if ((accessKey == null) || (secretKey == null) || accessKey.equals("") || secretKey.equals("")) {

			logger.info("No EC2TagAddressProvider credential system properties provided.");

			// Retrieve the credentials from a properties resource instead.

			String propertyResource = System.getProperty("tangosol.coherence.ec2addressprovider.propertyfile",
					"AwsCredentials.properties");
			InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyResource);

			if (stream != null) {
				return new PropertiesCredentials(stream);
			} else {
				throw new SecurityException(
						"The EC2TagAddressProvider could not find any credentials, neither as system properties, nor as "
								+ propertyResource + " resource");
			}
		} else {
			return new BasicAWSCredentials(accessKey, secretKey);
		}
	}

	/**
	 * Logs the instances we found.
	 * 
	 * @param instances
	 *            the instances we are to log
	 */
	private void logAllInstances(Set<Instance> instances) {
		if (logger.isTraceEnabled()) {
			logger.trace("The following instances were found:");
		}

		for (Iterator<Instance> instIter = instances.iterator(); instIter.hasNext();) {
			logger.trace("EC2 instance: {}", instIter.next());
		}
	}

}
