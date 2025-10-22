package es.mapfre.solvencia.coherence.cloud;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tangosol.net.AddressProvider;

public abstract class CloudAddressProvider implements AddressProvider {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * The list of socket addresses to the Cloud Provider .
	 */
	protected List<InetSocketAddress> wkaAddressList;

	/**
	 * The current index in to the ArrayList.
	 */
	protected Iterator<InetSocketAddress> wkaIterator;
	
	protected Properties properties = new Properties();

	protected void generatePropertiesFromWKAList() {
		int i = 0;
		for (InetSocketAddress address : wkaAddressList) {
			properties.put("ipAndPort"+(i++), address.toString().replace("/", ""));
		}		
		properties.put("timestamp", Long.toString(System.currentTimeMillis()));
		
		// Escribir la lista en el properties
		FileOutputStream propertiesOutputStream = null;
		try {
			propertiesOutputStream = new FileOutputStream(System.getProperty("java.io.tmpdir") + File.separator + "wkalist.properties");
			properties.store(propertiesOutputStream, "");
		} catch (Exception p2Ex) {
		} finally {
			if (propertiesOutputStream != null) {
				try {
					propertiesOutputStream.close();
				} catch (IOException e) {
				}
			}
		}
	}

	protected void tryCached() {
		if (Boolean.valueOf(System.getProperty("tangosol.coherence.cloudaddressprovider.usecached", "true"))) {
			FileInputStream propertiesInputStream = null;
			try {
				// Intentamos generar la lista a partir de un properties
				propertiesInputStream = new FileInputStream(System.getProperty("java.io.tmpdir") + File.separator + "wkalist.properties");
				properties.load(propertiesInputStream);
				
				String timestamp = properties.getProperty("timestamp");
				if (timestamp != null && (System.currentTimeMillis() - (10 * 60 * 1000) < Long.valueOf(timestamp))) {
					for (String key : properties.stringPropertyNames()) {
						if (key.startsWith("ipAndPort")) {
							// Se trata de una configuración de host
							String ipAndPort = properties.getProperty(key).replace("/", "");
							String[] wka = ipAndPort.split(":");
							if (wka != null && wka.length == 2) {
								if (wkaAddressList == null) {
									wkaAddressList = new ArrayList<InetSocketAddress>();
								}
								wkaAddressList.add(new InetSocketAddress(wka[0], Integer.valueOf(wka[1])));
							}
						}
					}
				}
				Collections.sort(wkaAddressList, CloudInetSocketAddressComparator.INSTANCE);
				if (wkaAddressList != null && wkaAddressList.size() > 0) {
					wkaIterator = wkaAddressList.iterator();
				}
			} catch (Exception pEx) {
				logger.error("Error reading properties: {}", pEx.getMessage());
			} finally {
				if (propertiesInputStream != null) {
					try {
						propertiesInputStream.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}
	
	@Override
	public InetSocketAddress getNextAddress() {
		// Always increase index before use - initialized to -1
		if (wkaIterator.hasNext()) {
			InetSocketAddress address = wkaIterator.next();

			if (logger.isTraceEnabled()) {
				logger.trace("Returning WKA address {}", address);
			}

			return address;
		} else {
			// We must now return null according to the AddressProvider contract
			// to terminate iteration.
			// However, we must also reset the iterator so that the next call
			// starts from the
			// beginning of the WKA list
			wkaIterator = wkaAddressList.iterator();

			return null;
		}
	}

	@Override
	public void accept() {
		// Not called
	}

	@Override
	public void reject(Throwable arg0) {
		// Not called
	}


}
