package es.mapfre.solvencia.coherence.keys.entregables;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.entregables.FlujInfSCR;

@Portable
public class FlujInfSCRKey implements Comparable<FlujInfSCRKey> {

	@PortableProperty(FlujInfSCR.IND_BT)
	private String bt;
	@PortableProperty(FlujInfSCR.IND_FCIERRE)
	private Timestamp fcierre;
	@PortableProperty(FlujInfSCR.IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(FlujInfSCR.IND_NEGOCIO)
	private String negocio;
	@PortableProperty(FlujInfSCR.IND_UOA)
	private String uoa;
	@PortableProperty(FlujInfSCR.IND_KCARTERA_CONTRATO)
	private String kcarteraContrato;
	@PortableProperty(FlujInfSCR.IND_KCARTERA_COHORT)
	private String kcarteraCohort;
	@PortableProperty(FlujInfSCR.IND_KCARTERA_ONER)
	private String kcarteraOner;
	@PortableProperty(FlujInfSCR.IND_FPROYFLUJEST)
	private Timestamp fproyflujest;
	@PortableProperty(FlujInfSCR.IND_KCARTERAINV)
	private String kcarterainv;
	@PortableProperty(FlujInfSCR.IND_KMODALIDAD)
	private Integer kmodalidad;	
	

	public FlujInfSCRKey() {
		super();
	}

	public FlujInfSCRKey(String bt, Timestamp fcierre, Integer ccanal, String negocio, String uoa,
			String kcarteraContrato, String kcarteraCohort, String kcarteraOner, Timestamp fproyflujest, String kcarterainv, Integer kmodalidad) {
		super();
		this.bt = bt;
		this.fcierre = fcierre;
		this.ccanal = ccanal;
		this.negocio = negocio;
		this.uoa = uoa;
		this.kcarteraContrato = kcarteraContrato;
		this.kcarteraCohort = kcarteraCohort;
		this.kcarteraOner = kcarteraOner;
		this.fproyflujest = fproyflujest;
		this.kcarterainv=kcarterainv;
		this.kmodalidad = kmodalidad;
	}

	@Override
	public String toString() {
		return "FlujInfSCRKey [bt=" + bt + ", fcierre=" + fcierre + ", ccanal=" + ccanal + ", negocio=" + negocio
				+ ", uoa=" + uoa + ", kcarteraContrato=" + kcarteraContrato + ", kcarteraCohort=" + kcarteraCohort
				+ ", kcarteraOner=" + kcarteraOner + ", fproyflujest=" + fproyflujest + ", kcarterainv=" + kcarterainv
				+ ", kmodalidad=" + kmodalidad + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((fproyflujest == null) ? 0 : fproyflujest.hashCode());
		result = prime * result + ((kcarteraCohort == null) ? 0 : kcarteraCohort.hashCode());
		result = prime * result + ((kcarteraContrato == null) ? 0 : kcarteraContrato.hashCode());
		result = prime * result + ((kcarteraOner == null) ? 0 : kcarteraOner.hashCode());
		result = prime * result + ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((negocio == null) ? 0 : negocio.hashCode());
		result = prime * result + ((uoa == null) ? 0 : uoa.hashCode());
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
		FlujInfSCRKey other = (FlujInfSCRKey) obj;
		if (bt == null) {
			if (other.bt != null)
				return false;
		} else if (!bt.equals(other.bt))
			return false;
		if (ccanal == null) {
			if (other.ccanal != null)
				return false;
		} else if (!ccanal.equals(other.ccanal))
			return false;
		if (fcierre == null) {
			if (other.fcierre != null)
				return false;
		} else if (!fcierre.equals(other.fcierre))
			return false;
		if (fproyflujest == null) {
			if (other.fproyflujest != null)
				return false;
		} else if (!fproyflujest.equals(other.fproyflujest))
			return false;
		if (kcarteraCohort == null) {
			if (other.kcarteraCohort != null)
				return false;
		} else if (!kcarteraCohort.equals(other.kcarteraCohort))
			return false;
		if (kcarteraContrato == null) {
			if (other.kcarteraContrato != null)
				return false;
		} else if (!kcarteraContrato.equals(other.kcarteraContrato))
			return false;
		if (kcarteraOner == null) {
			if (other.kcarteraOner != null)
				return false;
		} else if (!kcarteraOner.equals(other.kcarteraOner))
			return false;
		if (kcarterainv == null) {
			if (other.kcarterainv != null)
				return false;
		} else if (!kcarterainv.equals(other.kcarterainv))
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		if (negocio == null) {
			if (other.negocio != null)
				return false;
		} else if (!negocio.equals(other.negocio))
			return false;
		if (uoa == null) {
			if (other.uoa != null)
				return false;
		} else if (!uoa.equals(other.uoa))
			return false;
		return true;
	}

	public int compareTo(FlujInfSCRKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.fcierre, o.fcierre);
		compareToBuilder.append(this.ccanal, o.ccanal);
		compareToBuilder.append(this.negocio, o.negocio);
		compareToBuilder.append(this.uoa, o.uoa);
		compareToBuilder.append(this.kmodalidad, o.kmodalidad);
		compareToBuilder.append(this.kcarteraContrato, o.kcarteraContrato);
		compareToBuilder.append(this.kcarteraCohort, o.kcarteraCohort);
		compareToBuilder.append(this.kcarteraOner, o.kcarteraOner);
		compareToBuilder.append(this.fproyflujest, o.fproyflujest);
		compareToBuilder.append(this.kcarterainv, o.kcarterainv);
		
		return compareToBuilder.toComparison();
	}

}
