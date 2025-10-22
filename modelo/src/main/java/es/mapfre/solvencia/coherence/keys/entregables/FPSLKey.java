package es.mapfre.solvencia.coherence.keys.entregables;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.entregables.FPSL;

@Portable
public class FPSLKey implements Comparable<FPSLKey> {

	@PortableProperty(FPSL.IND_FCIERRE)
	private Timestamp fcierre;
	@PortableProperty(FPSL.IND_KUOA)
	private String kuoa;
	@PortableProperty(FPSL.IND_TEXTRACCION)
	private String textraccion;
	@PortableProperty(FPSL.IND_TPASIVO)
	private String tpasivo;
	@PortableProperty(FPSL.IND_TNEGOCIO)
	private Integer tnegocio;
	@PortableProperty(FPSL.IND_TNEGOCIOLRC)
	private Integer tnegociolrc;
	@PortableProperty(FPSL.IND_PVENTA)
	private Timestamp pventa;
	@PortableProperty(FPSL.IND_CODREASEG)
	private Integer codreaseg;
	@PortableProperty(FPSL.IND_FPROYFLUJEST)
	private Timestamp fproyflujest;
		
	
	
	public FPSLKey() {
		super();
	}

	public FPSLKey(Timestamp fcierre, String kuoa, String textraccion, String tpasivo, Integer tnegocio,
			Integer tnegociolrc, Timestamp pventa, Integer codreaseg, Timestamp fproyflujest) {
		super();
		this.fcierre = fcierre;
		this.kuoa = kuoa;
		this.textraccion = textraccion;
		this.tpasivo = tpasivo;
		this.tnegocio = tnegocio;
		this.tnegociolrc = tnegociolrc;
		this.pventa = pventa;
		this.codreaseg = codreaseg;
		this.fproyflujest = fproyflujest;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

	public String getKuoa() {
		return kuoa;
	}

	public void setKuoa(String kuoa) {
		this.kuoa = kuoa;
	}

	public String getTextraccion() {
		return textraccion;
	}

	public void setTextraccion(String textraccion) {
		this.textraccion = textraccion;
	}

	public String getTpasivo() {
		return tpasivo;
	}

	public void setTpasivo(String tpasivo) {
		this.tpasivo = tpasivo;
	}

	public Integer getTnegocio() {
		return tnegocio;
	}

	public void setTnegocio(Integer tnegocio) {
		this.tnegocio = tnegocio;
	}

	public Integer getTnegociolrc() {
		return tnegociolrc;
	}

	public void setTnegociolrc(Integer tnegociolrc) {
		this.tnegociolrc = tnegociolrc;
	}

	public Timestamp getPventa() {
		return pventa;
	}

	public void setPventa(Timestamp pventa) {
		this.pventa = pventa;
	}

	public Integer getCodreaseg() {
		return codreaseg;
	}

	public void setCodreaseg(Integer codreaseg) {
		this.codreaseg = codreaseg;
	}

	public Timestamp getFproyflujest() {
		return fproyflujest;
	}

	public void setFproyflujest(Timestamp fproyflujest) {
		this.fproyflujest = fproyflujest;
	}


	@Override
	public String toString() {
		return "FPSLKey [fcierre=" + fcierre + ", kuoa=" + kuoa + ", textraccion=" + textraccion + ", tpasivo="
				+ tpasivo + ", tnegocio=" + tnegocio + ", tnegociolrc=" + tnegociolrc + ", pventa=" + pventa
				+ ", codreaseg=" + codreaseg + ", fproyflujest=" + fproyflujest + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codreaseg == null) ? 0 : codreaseg.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((fproyflujest == null) ? 0 : fproyflujest.hashCode());
		result = prime * result + ((kuoa == null) ? 0 : kuoa.hashCode());
		result = prime * result + ((pventa == null) ? 0 : pventa.hashCode());
		result = prime * result + ((textraccion == null) ? 0 : textraccion.hashCode());
		result = prime * result + ((tnegocio == null) ? 0 : tnegocio.hashCode());
		result = prime * result + ((tnegociolrc == null) ? 0 : tnegociolrc.hashCode());
		result = prime * result + ((tpasivo == null) ? 0 : tpasivo.hashCode());
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
		FPSLKey other = (FPSLKey) obj;
		if (codreaseg == null) {
			if (other.codreaseg != null)
				return false;
		} else if (!codreaseg.equals(other.codreaseg))
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
		if (kuoa == null) {
			if (other.kuoa != null)
				return false;
		} else if (!kuoa.equals(other.kuoa))
			return false;
		if (pventa == null) {
			if (other.pventa != null)
				return false;
		} else if (!pventa.equals(other.pventa))
			return false;
		if (textraccion == null) {
			if (other.textraccion != null)
				return false;
		} else if (!textraccion.equals(other.textraccion))
			return false;
		if (tnegocio == null) {
			if (other.tnegocio != null)
				return false;
		} else if (!tnegocio.equals(other.tnegocio))
			return false;
		if (tnegociolrc == null) {
			if (other.tnegociolrc != null)
				return false;
		} else if (!tnegociolrc.equals(other.tnegociolrc))
			return false;
		if (tpasivo == null) {
			if (other.tpasivo != null)
				return false;
		} else if (!tpasivo.equals(other.tpasivo))
			return false;
		return true;
	}

	@Override
	public int compareTo(FPSLKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.fcierre, o.fcierre);
		compareToBuilder.append(this.kuoa, o.kuoa);
		compareToBuilder.append(this.textraccion, o.textraccion);
		compareToBuilder.append(this.tpasivo, o.tpasivo);
		compareToBuilder.append(this.tnegocio, o.tnegocio);
		compareToBuilder.append(this.tnegociolrc, o.tnegociolrc);
		compareToBuilder.append(this.pventa, o.pventa);
		compareToBuilder.append(this.codreaseg, o.codreaseg);
		compareToBuilder.append(this.fproyflujest, o.fproyflujest);
		return compareToBuilder.toComparison();
	}

}