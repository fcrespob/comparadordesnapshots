package es.mapfre.solvencia.coherence.keys.entregables;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.entregables.FlujPMdCoa;

@Portable
public class FlujPMdCoaKey implements Comparable<FlujPMdCoaKey> {
	
	@PortableProperty(FlujPMdCoa.IND_KMODALIDAD)
	private Integer kmodalidad;	
	@PortableProperty(FlujPMdCoa.IND_KGARANTIA)
	private Integer kgarantia;
	@PortableProperty(FlujPMdCoa.IND_KPRESTACION)
	private String kprestacion;
	@PortableProperty(FlujPMdCoa.IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(FlujPMdCoa.IND_KSUBPOLIZA)
	private Integer ksubpoliza;
	@PortableProperty(FlujPMdCoa.IND_KCERTIFICADO)
	private Integer kcertificado;
	@PortableProperty(FlujPMdCoa.IND_NSUSCRI)
	private Integer nsuscri;
	@PortableProperty(FlujPMdCoa.IND_BT)
	private String bt;
	@PortableProperty(FlujPMdCoa.IND_FCIERRE)
	private Timestamp fcierre;
	
	public FlujPMdCoaKey(Integer kmodalidad, Integer kgarantia, String kprestacion, Long kpoliza, Integer ksubpoliza, Integer kcertificado, Integer nsuscri, String bt, Timestamp fcierre) {
		this.bt = bt;
		this.fcierre = fcierre;
		this.kpoliza = kpoliza;
		this.ksubpoliza = ksubpoliza;
		this.nsuscri = nsuscri;
		this.kmodalidad = kmodalidad;
		this.kgarantia = kgarantia;
		this.kprestacion = kprestacion;
		this.kcertificado = kcertificado;
	}
	
	public FlujPMdCoaKey() {
		super();
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((kcertificado == null) ? 0 : kcertificado.hashCode());
		result = prime * result + ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((kprestacion == null) ? 0 : kprestacion.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
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
		FlujPMdCoaKey other = (FlujPMdCoaKey) obj;
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
		if (kcertificado == null) {
			if (other.kcertificado != null)
				return false;
		} else if (!kcertificado.equals(other.kcertificado))
			return false;
		if (kgarantia == null) {
			if (other.kgarantia != null)
				return false;
		} else if (!kgarantia.equals(other.kgarantia))
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		if (kpoliza == null) {
			if (other.kpoliza != null)
				return false;
		} else if (!kpoliza.equals(other.kpoliza))
			return false;
		if (kprestacion == null) {
			if (other.kprestacion != null)
				return false;
		} else if (!kprestacion.equals(other.kprestacion))
			return false;
		if (ksubpoliza == null) {
			if (other.ksubpoliza != null)
				return false;
		} else if (!ksubpoliza.equals(other.ksubpoliza))
			return false;
		if (nsuscri == null) {
			if (other.nsuscri != null)
				return false;
		} else if (!nsuscri.equals(other.nsuscri))
			return false;
		return true;
	}
	
	
	public int compareTo(FlujPMdCoaKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.fcierre, o.fcierre);
		compareToBuilder.append(this.kpoliza, o.kpoliza);
		compareToBuilder.append(this.ksubpoliza, o.ksubpoliza);
		compareToBuilder.append(this.nsuscri, o.nsuscri);
		compareToBuilder.append(this.kmodalidad, o.kmodalidad);
		compareToBuilder.append(this.kgarantia, o.kgarantia);
		compareToBuilder.append(this.kprestacion, o.kprestacion);
		compareToBuilder.append(this.kcertificado, o.kcertificado);
		
		return compareToBuilder.toComparison();
	}	
	
}