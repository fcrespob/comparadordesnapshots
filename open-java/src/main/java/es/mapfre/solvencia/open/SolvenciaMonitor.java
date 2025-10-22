package es.mapfre.solvencia.open;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.jmx.SolvenciaInfoMBean;
import es.mapfre.solvencia.open.comun.SolvenciaUtils;

public class SolvenciaMonitor {
	private static final int DECIMALES_AVANCE = 2;
	private static Logger logger = LoggerFactory.getLogger(SolvenciaMonitor.class);

	public static void main(String[] args) throws Exception {
		JMXServiceURL url = 
		    new JMXServiceURL(SolvenciaUtils.getJmxURL());
		JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
		
		
		MBeanServerConnection mbsc = 
			    jmxc.getMBeanServerConnection();
		
		ObjectName mbeanName = new ObjectName("SolvenciaMotorCalculo:type=SolvenciaInfoMBean");
		SolvenciaInfoMBean mbeanProxy = JMX.newMBeanProxy(mbsc, mbeanName, 
				SolvenciaInfoMBean.class, true);

		logger.info("Ficha En Ejecucion: {}", SolvenciaMonitor.removeNull(mbeanProxy.getFichaEnEjecucion()));
		logger.info("Fichas Procesadas: {}", SolvenciaMonitor.removeNull(mbeanProxy.getFichasProcesadas()));
		logger.info("Fichas A Procesar: {}", SolvenciaMonitor.removeNull(mbeanProxy.getFichasAProcesar()));
		logger.info("Proceso Actual: {}", SolvenciaMonitor.removeNull(mbeanProxy.getProcesoActual()));
		logger.info("UMIC Pendiente Ficha: {}", SolvenciaMonitor.removeNull(mbeanProxy.getUMICPendienteFicha()));
		logger.info("UMIC Procesadas Ficha: {}", SolvenciaMonitor.removeNull(mbeanProxy.getUMICProcesadasFicha()));
		logger.info("Avance Ficha: {}", SolvenciaMonitor.porcentaje(mbeanProxy.getAvanceFicha(), DECIMALES_AVANCE));
		logger.info("Hora Comienzo FichaActual: {}", SolvenciaMonitor.removeNull(mbeanProxy.getHoraComienzo()));
		logger.info("Hora Fin Estimada Ficha Actual: {}", SolvenciaMonitor.removeNull(mbeanProxy.getHoraFinEstimadaFichaActual()));
		logger.info("Hora Comienzo Ejecución: {}", SolvenciaMonitor.removeNull(mbeanProxy.getHoraComienzo()));
		logger.info("Hora Fin Estimada Ejecución: {}", SolvenciaMonitor.removeNull(mbeanProxy.getHoraFinEstimada()));
		logger.info("Hora Actual Servidor: {}", SolvenciaMonitor.removeNull(mbeanProxy.getHoraServidor()));

	}
	
	private static Object removeNull(Object o) {
		return o != null ? o : "";
	}
	
	private static String porcentaje(Object o, int decimales) {
		String res = "";
		
		if (o != null && o instanceof Number) {
			Number n = (Number) o;
			res = String.format("%.2f%%", n);
		}
		
		return res;
	}
}
