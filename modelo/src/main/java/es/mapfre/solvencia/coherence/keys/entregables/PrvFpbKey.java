package es.mapfre.solvencia.coherence.keys.entregables;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.entregables.PrvFpb;

@Portable
public class PrvFpbKey implements Comparable<PrvFpbKey> {

	@PortableProperty(PrvFpb.IND_BT)
	private String bt;
	@PortableProperty(PrvFpb.IND_FECCIERRE)
	private Timestamp feccierre;
	@PortableProperty(PrvFpb.IND_CNEGOCIO)
	private String cnegocio;
	@PortableProperty(PrvFpb.IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(PrvFpb.IND_KCARTERA)
	private String kcartera;
	@PortableProperty(PrvFpb.IND_KGAP)
	private String gap;
	@PortableProperty(PrvFpb.IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(PrvFpb.IND_INTBTI)
	private BigDecimal intbti;

	public PrvFpbKey() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PrvFpbKey(String bt, Timestamp feccierre, String cnegocio, Integer ccanal, String kcartera, String gap,
			Integer kmodalidad, BigDecimal intbti) {
		super();
		this.bt = bt;
		this.feccierre = feccierre;
		this.cnegocio = cnegocio;
		this.ccanal = ccanal;
		this.kcartera = kcartera;
		this.gap = gap;
		this.kmodalidad = kmodalidad;
		this.intbti = intbti;

	}

	public String getBt() {
		return bt;
	}

	public void setBt(String bt) {
		this.bt = bt;
	}

	public Timestamp getFeccierre() {
		return feccierre;
	}

	public void setFeccierre(Timestamp feccierre) {
		this.feccierre = feccierre;
	}

	public String getCnegocio() {
		return cnegocio;
	}

	public void setCnegocio(String cnegocio) {
		this.cnegocio = cnegocio;
	}

	public Integer getCcanal() {
		return ccanal;
	}

	public void setCcanal(Integer ccanal) {
		this.ccanal = ccanal;
	}

	public String getKcartera() {
		return kcartera;
	}

	public void setKcartera(String kcartera) {
		this.kcartera = kcartera;
	}

	public String getGap() {
		return gap;
	}

	public void setGap(String gap) {
		this.gap = gap;
	}

	public Integer getKmodalidad() {
		return kmodalidad;
	}

	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}

	public BigDecimal getIntbti() {
		return intbti;
	}

	public void setIntbti(BigDecimal intbti) {
		this.intbti = intbti;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((feccierre == null) ? 0 : feccierre.hashCode());
		result = prime * result + ((gap == null) ? 0 : gap.hashCode());
		result = prime * result + ((kcartera == null) ? 0 : kcartera.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((intbti == null) ? 0 : intbti.hashCode());
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
		PrvFpbKey other = (PrvFpbKey) obj;
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
		if (cnegocio == null) {
			if (other.cnegocio != null)
				return false;
		} else if (!cnegocio.equals(other.cnegocio))
			return false;
		if (feccierre == null) {
			if (other.feccierre != null)
				return false;
		} else if (!feccierre.equals(other.feccierre))
			return false;
		if (gap == null) {
			if (other.gap != null)
				return false;
		} else if (!gap.equals(other.gap))
			return false;
		if (kcartera == null) {
			if (other.kcartera != null)
				return false;
		} else if (!kcartera.equals(other.kcartera))
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		if (intbti == null) {
			if (other.intbti != null)
				return false;
		} else if (!intbti.equals(other.intbti))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PrvFpbKey [bt=" + bt + ", feccierre=" + feccierre + ", cnegocio=" + cnegocio + ", ccanal=" + ccanal
				+ ", kcartera=" + kcartera + ", gap=" + gap + ", kmodalidad=" + kmodalidad + ", intbti=" + intbti + "]";
	}

	public int compareTo(PrvFpbKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.feccierre, o.feccierre);
		compareToBuilder.append(this.cnegocio, o.cnegocio);
		compareToBuilder.append(this.ccanal, o.ccanal);
		compareToBuilder.append(this.kcartera, o.kcartera);
		compareToBuilder.append(this.gap, o.gap);
		compareToBuilder.append(this.kmodalidad, o.kmodalidad);
		compareToBuilder.append(this.intbti, o.intbti);

		return compareToBuilder.toComparison();
	}
}
