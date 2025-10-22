package es.mapfre.solvencia.coherence.jmx;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tangosol.net.management.MBeanConnector;

public class SolvenciaMBeanConnector {
	private static Logger log = LoggerFactory.getLogger(SolvenciaMBeanConnector.class);

	public static void main(String[] args) {

		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			ObjectName beanName = new ObjectName("SolvenciaMotorCalculo:type=SolvenciaInfoMBean");		
			mbs.registerMBean(new SolvenciaInfo(), beanName);
			
			MBeanConnector.main(args);
		} catch (Exception e) {
			log.error("Error arrancando conector MBean de Coherence", e);
		}
	}

}
