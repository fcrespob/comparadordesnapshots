package es.mapfre.solvencia.coherence.keys.entregables;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.entregables.FlujTcas;

@Portable
public class FlujTcasKey implements Comparable<FlujTcasKey> {

	@PortableProperty(FlujTcas.IND_BT)
	private String bt;
	@PortableProperty(FlujTcas.IND_FECCIERRE)
	private Timestamp feccierre;
	@PortableProperty(FlujTcas.IND_CNEGOCIO)
	private String cnegocio;
	@PortableProperty(FlujTcas.IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(FlujTcas.IND_KCARTERAINV)
	private String kcarterainv;
	@PortableProperty(FlujTcas.IND_GAPACT)
	private String gapact;
	@PortableProperty(FlujTcas.IND_KRAMO)
	private String kramo;
	@PortableProperty(FlujTcas.IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(FlujTcas.IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(FlujTcas.IND_KSUBPOLIZA)
	private Integer ksubpoliza;
	@PortableProperty(FlujTcas.IND_NSUSCRI)
	private Integer nsuscri;
	@PortableProperty(FlujTcas.IND_FECDESDE)
	private Timestamp fecdesde;



	public FlujTcasKey() {
		super();
		// TODO Auto-generated constructor stub
	}


	

	public FlujTcasKey(String bt, Timestamp feccierre, String cnegocio, Integer ccanal, String kcarterainv,
			String gapact, String kramo, Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer nsuscri,
			Timestamp fecdesde) {
		super();
		this.bt = bt;
		this.feccierre = feccierre;
		this.cnegocio = cnegocio;
		this.ccanal = ccanal;
		this.kcarterainv = kcarterainv;
		this.gapact = gapact;
		this.kramo = kramo;
		this.kmodalidad = kmodalidad;
		this.kpoliza = kpoliza;
		this.ksubpoliza = ksubpoliza;
		this.nsuscri = nsuscri;
		this.fecdesde = fecdesde;
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




	public String getKcarterainv() {
		return kcarterainv;
	}




	public void setKcarterainv(String kcarterainv) {
		this.kcarterainv = kcarterainv;
	}




	public String getGapact() {
		return gapact;
	}




	public void setGapact(String gapact) {
		this.gapact = gapact;
	}




	public String getKramo() {
		return kramo;
	}




	public void setKramo(String kramo) {
		this.kramo = kramo;
	}




	public Integer getKmodalidad() {
		return kmodalidad;
	}




	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}




	public Long getKpoliza() {
		return kpoliza;
	}




	public void setKpoliza(Long kpoliza) {
		this.kpoliza = kpoliza;
	}




	public Integer getKsubpoliza() {
		return ksubpoliza;
	}




	public void setKsubpoliza(Integer ksubpoliza) {
		this.ksubpoliza = ksubpoliza;
	}




	public Integer getNsuscri() {
		return nsuscri;
	}




	public void setNsuscri(Integer nsuscri) {
		this.nsuscri = nsuscri;
	}




	public Timestamp getFecdesde() {
		return fecdesde;
	}




	public void setFecdesde(Timestamp fecdesde) {
		this.fecdesde = fecdesde;
	}


	


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((feccierre == null) ? 0 : feccierre.hashCode());
		result = prime * result + ((fecdesde == null) ? 0 : fecdesde.hashCode());
		result = prime * result + ((gapact == null) ? 0 : gapact.hashCode());
		result = prime * result + ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
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
		FlujTcasKey other = (FlujTcasKey) obj;
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
		if (fecdesde == null) {
			if (other.fecdesde != null)
				return false;
		} else if (!fecdesde.equals(other.fecdesde))
			return false;
		if (gapact == null) {
			if (other.gapact != null)
				return false;
		} else if (!gapact.equals(other.gapact))
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
		if (kpoliza == null) {
			if (other.kpoliza != null)
				return false;
		} else if (!kpoliza.equals(other.kpoliza))
			return false;
		if (kramo == null) {
			if (other.kramo != null)
				return false;
		} else if (!kramo.equals(other.kramo))
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



	

	@Override
	public String toString() {
		return "FlujTcasKey [bt=" + bt + ", feccierre=" + feccierre + ", cnegocio=" + cnegocio + ", ccanal=" + ccanal
				+ ", kcarterainv=" + kcarterainv + ", gapact=" + gapact + ", kramo=" + kramo + ", kmodalidad="
				+ kmodalidad + ", kpoliza=" + kpoliza + ", ksubpoliza=" + ksubpoliza + ", nsuscri=" + nsuscri
				+ ", fecdesde=" + fecdesde + "]";
	}




	@Override
	public int compareTo(FlujTcasKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.feccierre, o.feccierre);
		compareToBuilder.append(this.cnegocio, o.cnegocio);
		compareToBuilder.append(this.ccanal, o.ccanal);
		compareToBuilder.append(this.kcarterainv, o.kcarterainv);
		compareToBuilder.append(this.gapact, o.gapact);
		compareToBuilder.append(this.kramo, o.kramo);
		compareToBuilder.append(this.kmodalidad, o.kmodalidad);
		compareToBuilder.append(this.kpoliza, o.kpoliza);
		compareToBuilder.append(this.ksubpoliza, o.ksubpoliza);
		compareToBuilder.append(this.nsuscri, o.nsuscri);
		compareToBuilder.append(this.fecdesde, o.fecdesde);

		return compareToBuilder.toComparison();
	}
}
