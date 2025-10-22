package es.mapfre.solvencia.coherence.keys.entregables;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.entregables.FlujInf3;

@Portable
public class FlujInf3Key implements Comparable<FlujInf3Key> {

	@PortableProperty(FlujInf3.IND_BT)
	private String bt;
	@PortableProperty(FlujInf3.IND_FCIERRE)
	private Timestamp fcierre;
	@PortableProperty(FlujInf3.IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(FlujInf3.IND_NEGOCIO)
	private String negocio;
	@PortableProperty(FlujInf3.IND_UOA)
	private String uoa;
	@PortableProperty(FlujInf3.IND_KCARTERA_CONTRATO)
	private String kcarteraContrato;
	@PortableProperty(FlujInf3.IND_KCARTERA_COHORT)
	private String kcarteraCohort;
	@PortableProperty(FlujInf3.IND_KCARTERA_ONER)
	private String kcarteraOner;
	@PortableProperty(FlujInf3.IND_FDESDE)
	private Timestamp fdesde;
	@PortableProperty(FlujInf3.IND_FHASTA)
	private Timestamp fhasta;

	public FlujInf3Key() {
		super();
	}

	public FlujInf3Key(String bt, Timestamp fcierre, Integer ccanal, String negocio, String uoa,
			String kcarteraContrato, String kcarteraCohort, String kcarteraOner, Timestamp fdesde, Timestamp fhasta) {
		super();
		this.bt = bt;
		this.fcierre = fcierre;
		this.ccanal = ccanal;
		this.negocio = negocio;
		this.uoa = uoa;
		this.kcarteraContrato = kcarteraContrato;
		this.kcarteraCohort = kcarteraCohort;
		this.kcarteraOner = kcarteraOner;
		this.fdesde = fdesde;
		this.fhasta = fhasta;
	}

	@Override
	public String toString() {
		return "FlujInf3Key [bt=" + bt + ", fcierre=" + fcierre + ", ccanal=" + ccanal + ", negocio=" + negocio
				+ ", uoa=" + uoa + ", kcarteraContrato=" + kcarteraContrato + ", kcarteraCohort=" + kcarteraCohort
				+ ", kcarteraOner=" + kcarteraOner + ", fdesde=" + fdesde + ", fhasta=" + fhasta + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((fdesde == null) ? 0 : fdesde.hashCode());
		result = prime * result + ((fhasta == null) ? 0 : fhasta.hashCode());
		result = prime * result + ((kcarteraCohort == null) ? 0 : kcarteraCohort.hashCode());
		result = prime * result + ((kcarteraContrato == null) ? 0 : kcarteraContrato.hashCode());
		result = prime * result + ((kcarteraOner == null) ? 0 : kcarteraOner.hashCode());
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
		FlujInf3Key other = (FlujInf3Key) obj;
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
		if (fdesde == null) {
			if (other.fdesde != null)
				return false;
		} else if (!fdesde.equals(other.fdesde))
			return false;
		if (fhasta == null) {
			if (other.fhasta != null)
				return false;
		} else if (!fhasta.equals(other.fhasta))
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

	public int compareTo(FlujInf3Key o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.fcierre, o.fcierre);
		compareToBuilder.append(this.ccanal, o.ccanal);
		compareToBuilder.append(this.negocio, o.negocio);
		compareToBuilder.append(this.uoa, o.uoa);
		compareToBuilder.append(this.kcarteraContrato, o.kcarteraContrato);
		compareToBuilder.append(this.kcarteraCohort, o.kcarteraCohort);
		compareToBuilder.append(this.kcarteraOner, o.kcarteraOner);
		compareToBuilder.append(this.fdesde, o.fdesde);
		compareToBuilder.append(this.fhasta, o.fhasta);

		return compareToBuilder.toComparison();
	}

}
