package es.mapfre.solvencia.coherence.dataaffinity;

import com.tangosol.net.PartitionedService;
import com.tangosol.net.partition.KeyAssociator;

import es.mapfre.solvencia.coherence.keys.maestro.CuadrosAmortizacionKey;
import es.mapfre.solvencia.coherence.keys.maestro.PagosPlanificadosKey;
import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.PolizasTipoKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.DetalleBaseTecnicaKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.DetalleCorrienteKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.IncidenciaKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.TotalesFlujosKey;

public class DataAffinityKeyAssociator implements KeyAssociator{

	@Override
	public Object getAssociatedKey(final Object object) {
		
		if (object instanceof UmicKey){
			return ((UmicKey) object).getAssociatedKeyMaestro();
		} else if (object instanceof DetalleCorrienteKey) {
			return ((DetalleCorrienteKey) object).getUmicKey().getAssociatedKeyMaestro();
		} else if (object instanceof DetalleBaseTecnicaKey) {
			return ((DetalleBaseTecnicaKey) object).getUmicKey().getAssociatedKeyMaestro();
		} else if (object instanceof IncidenciaKey) {
			if (((IncidenciaKey) object).getUmicKey() != null) {
				return ((IncidenciaKey) object).getUmicKey().getAssociatedKeyMaestro();
			}
		} else if (object instanceof TotalesFlujosKey) {
			return ((TotalesFlujosKey) object).getUmicKey().getAssociatedKeyMaestro();
		} else if (object instanceof CuadrosAmortizacionKey){
			return ((CuadrosAmortizacionKey) object).getAssociatedKeyMaestro(); 
		} else if (object instanceof PagosPlanificadosKey){
			return ((PagosPlanificadosKey) object).getAssociatedKeyMaestro(); 
		} else if (object instanceof PolizasTipoKey){
			return ((PolizasTipoKey) object).getClaveUmic().getAssociatedKeyMaestro();
		}
		
		return null;
	}

	@Override
	public void init(PartitionedService arg0) {
		// No es necesaria inicialización adicional
		
	}

}
