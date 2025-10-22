package es.mapfre.solvencia.coherence.keys.entregables;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.entregables.DetalleCorrienteEntregables;

@Portable
public class DetalleCorrienteEntregablesKey implements Comparable<DetalleCorrienteEntregablesKey> {
	
	@PortableProperty(DetalleCorrienteEntregables.IND_BT)
	private String bt;
	@PortableProperty(DetalleCorrienteEntregables.IND_FCIERRE)
	private Timestamp fcierre;
	@PortableProperty(DetalleCorrienteEntregables.IND_UMICKEY)
	private UmicKey umicKey;
	@PortableProperty(DetalleCorrienteEntregables.IND_FECHADESDE)
	private Timestamp fechadesde;
	@PortableProperty(DetalleCorrienteEntregables.IND_DIA)
	private Integer dia;
	
	public DetalleCorrienteEntregablesKey(String bt, Timestamp fcierre, UmicKey umicKey, Timestamp fechadesde, Integer dia) {
		this.bt = bt;
		this.fcierre = fcierre;
		this.umicKey = umicKey;
		this.fechadesde = fechadesde;
		this.dia = dia;
	}
	
	public UmicKey getUmicKey() {
		return umicKey;
	}
	
	
	public DetalleCorrienteEntregablesKey() {
		super();
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((umicKey == null) ? 0 : umicKey.hashCode());
		result = prime * result + ((fechadesde == null) ? 0 : fechadesde.hashCode());
		result = prime * result + ((dia == null) ? 0 : dia.hashCode());
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
		DetalleCorrienteEntregablesKey other = (DetalleCorrienteEntregablesKey) obj;
		if (bt == null) {
			if (other.bt != null)
				return false;
		} else if (!bt.equals(other.bt))
			return false;
		if (fcierre == null) {
			if (other.fcierre != null)
				return false;
		} else if (!fcierre.equals(other.fcierre))
			return false;
		if (umicKey == null) {
			if (other.umicKey != null)
				return false;
		} else if (!umicKey.equals(other.umicKey))
			return false;
		if (fechadesde == null) {
			if (other.fechadesde != null)
				return false;
		} else if (!fechadesde.equals(other.fechadesde))
			return false;
		if (dia == null) {
			if (other.dia != null)
				return false;
		} else if (!dia.equals(other.dia))
			return false;
		return true;
	}
	
	
	public int compareTo(DetalleCorrienteEntregablesKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.fcierre, o.fcierre);
		compareToBuilder.append(this.umicKey, o.umicKey);
		compareToBuilder.append(this.fechadesde, fechadesde);
		compareToBuilder.append(this.dia, dia);
		
		return compareToBuilder.toComparison();
	}	
	
}