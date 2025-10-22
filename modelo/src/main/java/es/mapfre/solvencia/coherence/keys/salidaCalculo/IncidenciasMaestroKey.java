package es.mapfre.solvencia.coherence.keys.salidaCalculo;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;

@Portable
public class IncidenciasMaestroKey {
	
//	@PortableProperty(Incidencia.IND_CLAVEUMIC) private UmicKey claveUmic;
//	@PortableProperty(Incidencia.IND_BT) private String bt;
//	@PortableProperty(Incidencia.IND_PROGRAMA)      private String generadorError;
//	@PortableProperty(Incidencia.IND_CODIGORETORNO) private String codigoRetorno;
	@PortableProperty(Incidencia.IND_TEXTOERROR) private String textoError;
	
	public IncidenciasMaestroKey() {
		super();
	}

//	public IncidenciasMaestroKey(UmicKey claveUmic, String bt, String generadorError, String codigoRetorno) {
//		super();
//		this.claveUmic = claveUmic;
//		this.bt = bt;
//		this.generadorError = generadorError;
//		this.codigoRetorno = codigoRetorno;
//	}
	
	public IncidenciasMaestroKey(String textoError) {
		super();
		this.textoError = textoError;
	}
	
//	public UmicKey getUmicKey() {
//		return claveUmic;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((textoError == null) ? 0 : textoError.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IncidenciasMaestroKey other = (IncidenciasMaestroKey) obj;
		if (textoError == null) {
			if (other.textoError != null)
				return false;
		} else if (!textoError.equals(other.textoError))
			return false;
		return true;
	}
	
	public int compareTo(IncidenciasMaestroKey o) {
		if (o == null) {
			return -1;
		}

		CompareToBuilder compareToBuilder = new CompareToBuilder();

		compareToBuilder.append(this.textoError, o.textoError);
		
		return compareToBuilder.toComparison();
	}

}