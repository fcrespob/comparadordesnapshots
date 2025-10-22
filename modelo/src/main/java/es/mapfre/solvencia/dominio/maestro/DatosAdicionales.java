package es.mapfre.solvencia.dominio.maestro;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class DatosAdicionales implements EntidadBase<UmicKey>{
	
	public static final int IND_PREST_CAL = 0;
	
	@PortableProperty(IND_PREST_CAL) private String prestCal;
	
		
	public String getPrestCal() {
		return prestCal;
	}


	public void setPrestCal(String prestCal) {
		this.prestCal = prestCal;
	}


	@Override
	public UmicKey getKey() {
		// No existe la posibilidad de crear una UmicKey a partir de los datos
		return null;
	}
}