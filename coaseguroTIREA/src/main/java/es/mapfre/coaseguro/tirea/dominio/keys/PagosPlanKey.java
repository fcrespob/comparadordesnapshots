package es.mapfre.coaseguro.tirea.dominio.keys;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class PagosPlanKey implements Comparable<PagosPlanKey> {

	private Integer kajuste;
	private Integer kcerti;
	private Integer nsuscri;
	private Integer ksubpol;
	private Long kpoliza;
	private String kpresta;
	private Integer cgarantia;
	private String cprestaEntorno;
	private String cprestaFict;
	private Timestamp fplreaEfecto;
	private String filler;
	
	public PagosPlanKey() {
		super();
	}

	public PagosPlanKey(Integer kajuste, Integer kcerti,
			Integer nsuscri, Integer ksubpol, Long kpoliza, String kpresta,
			Integer cgarantia, String cprestaEntorno,String cprestaFict, Timestamp fplreaEfecto, Integer norden) {
		super();

		this.kajuste = kajuste;
		this.kcerti = kcerti;
		this.nsuscri = nsuscri;
		this.ksubpol = ksubpol;
		this.kpoliza = kpoliza;
		this.kpresta = kpresta;
		this.cgarantia = cgarantia;
		this.cprestaEntorno = cprestaEntorno;
		this.cprestaFict=cprestaFict;
		this.fplreaEfecto = fplreaEfecto;
		this.filler = norden.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cgarantia == null) ? 0 : cgarantia.hashCode());
		result = prime * result + ((cprestaEntorno == null) ? 0 : cprestaEntorno.hashCode());
		result = prime * result + ((cprestaFict == null) ? 0 : cprestaFict.hashCode());
		result = prime * result + ((filler == null) ? 0 : filler.hashCode());
		result = prime * result + ((fplreaEfecto == null) ? 0 : fplreaEfecto.hashCode());
		result = prime * result + ((kajuste == null) ? 0 : kajuste.hashCode());
		result = prime * result + ((kcerti == null) ? 0 : kcerti.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((kpresta == null) ? 0 : kpresta.hashCode());
		result = prime * result + ((ksubpol == null) ? 0 : ksubpol.hashCode());
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
		PagosPlanKey other = (PagosPlanKey) obj;
		if (cgarantia == null) {
			if (other.cgarantia != null)
				return false;
		} else if (!cgarantia.equals(other.cgarantia))
			return false;
		if (cprestaEntorno == null) {
			if (other.cprestaEntorno != null)
				return false;
		} else if (!cprestaEntorno.equals(other.cprestaEntorno))
			return false;
		if (cprestaFict == null) {
			if (other.cprestaFict != null)
				return false;
		} else if (!cprestaFict.equals(other.cprestaFict))
			return false;
		if (filler == null) {
			if (other.filler != null)
				return false;
		} else if (!filler.equals(other.filler))
			return false;
		if (fplreaEfecto == null) {
			if (other.fplreaEfecto != null)
				return false;
		} else if (!fplreaEfecto.equals(other.fplreaEfecto))
			return false;
		if (kajuste == null) {
			if (other.kajuste != null)
				return false;
		} else if (!kajuste.equals(other.kajuste))
			return false;
		if (kcerti == null) {
			if (other.kcerti != null)
				return false;
		} else if (!kcerti.equals(other.kcerti))
			return false;
		if (kpoliza == null) {
			if (other.kpoliza != null)
				return false;
		} else if (!kpoliza.equals(other.kpoliza))
			return false;
		if (kpresta == null) {
			if (other.kpresta != null)
				return false;
		} else if (!kpresta.equals(other.kpresta))
			return false;
		if (ksubpol == null) {
			if (other.ksubpol != null)
				return false;
		} else if (!ksubpol.equals(other.ksubpol))
			return false;
		if (nsuscri == null) {
			if (other.nsuscri != null)
				return false;
		} else if (!nsuscri.equals(other.nsuscri))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "PagosPlanKey [kajuste=" + kajuste + ", kcerti=" + kcerti + ", nsuscri=" + nsuscri + ", ksubpol=" + ksubpol
				+ ", kpoliza=" + kpoliza + ", kpresta=" + kpresta + ", cgarantia=" + cgarantia + ", cprestaEntorno="
				+ cprestaEntorno + ", cprestaFict=" + cprestaFict + ", fplreaEfecto=" + fplreaEfecto + ", filler="
				+ filler + "]";
	}
	
	public int compareTo(PagosPlanKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.kajuste, o.kajuste);
		compareToBuilder.append(this.ksubpol, o.ksubpol);
		compareToBuilder.append(this.kcerti, o.kcerti);
		compareToBuilder.append(this.nsuscri, o.nsuscri);
		compareToBuilder.append(this.kpoliza, o.kpoliza);
		compareToBuilder.append(this.kpresta, o.kpresta);
		compareToBuilder.append(this.cgarantia, o.cgarantia);
		compareToBuilder.append(this.cprestaEntorno, o.cprestaEntorno);
		compareToBuilder.append(this.cprestaFict, o.cprestaFict);
		compareToBuilder.append(this.fplreaEfecto, o.fplreaEfecto);
		compareToBuilder.append(this.filler, o.filler);
		return compareToBuilder.toComparison();
	}


}