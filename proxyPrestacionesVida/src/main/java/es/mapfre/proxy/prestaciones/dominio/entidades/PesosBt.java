package es.mapfre.proxy.prestaciones.dominio.entidades;

import java.math.BigDecimal;
import java.sql.Timestamp;

import es.mapfre.proxy.prestaciones.dominio.EntidadBase;
import es.mapfre.proxy.prestaciones.dominio.keys.PesosBtKey;

public class PesosBt implements EntidadBase<PesosBtKey>{

	public static final int IND_CNEGOCIO = 1;
	public static final int IND_CCANAL = 2;
	public static final int IND_CCARTERA = 3;
	public static final int IND_FCIERRE = 4;
	public static final int IND_KMODALIDAD = 5;
	public static final int IND_KPOLIZA = 6;
	public static final int IND_KSUBPOLIZA = 7;
	public static final int IND_KCERTIFICADO = 8;
	public static final int IND_COHORTE = 9;
	public static final int IND_SWCASADO = 10;
	public static final int IND_TOTFACT_T = 11;
	public static final int IND_PESOS_UOA = 12;

	private String cnegocio;
	private Integer ccanal;
	private Integer ccartera;
	private Timestamp fcierre;
	private Integer kmodalidad;
	private Long kpoliza;
	private Integer ksubpoliza;
	private Integer kcertificado;
	private String cohorte;
	private String swcasado;
	private BigDecimal totFactActT;
	private BigDecimal pesosUoA;
	
	public PesosBt() {
		super();
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

	public Integer getCcartera() {
		return ccartera;
	}

	public void setCcartera(Integer ccartera) {
		this.ccartera = ccartera;
	}

	public Timestamp getFcierre() {
		return fcierre;
	}

	public void setFcierre(Timestamp fcierre) {
		this.fcierre = fcierre;
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

	public Integer getKcertificado() {
		return kcertificado;
	}

	public void setKcertificado(Integer kcertificado) {
		this.kcertificado = kcertificado;
	}

	public String getCohorte() {
		return cohorte;
	}

	public void setCohorte(String cohorte) {
		this.cohorte = cohorte;
	}

	public String getSwcasado() {
		return swcasado;
	}

	public void setSwcasado(String swcasado) {
		this.swcasado = swcasado;
	}

	public BigDecimal getTotFactActT() {
		return totFactActT;
	}

	public void setTotFactActT(BigDecimal totFactActT) {
		this.totFactActT = totFactActT;
	}

	public BigDecimal getPesosUoA() {
		return pesosUoA;
	}

	public void setPesosUoA(BigDecimal pesosUoA) {
		this.pesosUoA = pesosUoA;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((ccartera == null) ? 0 : ccartera.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((cohorte == null) ? 0 : cohorte.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((kcertificado == null) ? 0 : kcertificado.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((pesosUoA == null) ? 0 : pesosUoA.hashCode());
		result = prime * result + ((swcasado == null) ? 0 : swcasado.hashCode());
		result = prime * result + ((totFactActT == null) ? 0 : totFactActT.hashCode());
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
		PesosBt other = (PesosBt) obj;
		if (ccanal == null) {
			if (other.ccanal != null)
				return false;
		} else if (!ccanal.equals(other.ccanal))
			return false;
		if (ccartera == null) {
			if (other.ccartera != null)
				return false;
		} else if (!ccartera.equals(other.ccartera))
			return false;
		if (cnegocio == null) {
			if (other.cnegocio != null)
				return false;
		} else if (!cnegocio.equals(other.cnegocio))
			return false;
		if (cohorte == null) {
			if (other.cohorte != null)
				return false;
		} else if (!cohorte.equals(other.cohorte))
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
		if (ksubpoliza == null) {
			if (other.ksubpoliza != null)
				return false;
		} else if (!ksubpoliza.equals(other.ksubpoliza))
			return false;
		if (pesosUoA == null) {
			if (other.pesosUoA != null)
				return false;
		} else if (!pesosUoA.equals(other.pesosUoA))
			return false;
		if (swcasado == null) {
			if (other.swcasado != null)
				return false;
		} else if (!swcasado.equals(other.swcasado))
			return false;
		if (totFactActT == null) {
			if (other.totFactActT != null)
				return false;
		} else if (!totFactActT.equals(other.totFactActT))
			return false;
		return true;
	}

	@Override
	public PesosBtKey getKey() {
		return new PesosBtKey(cnegocio, ccanal, ccartera, fcierre, cohorte, swcasado, kpoliza, ksubpoliza, kcertificado, kmodalidad);	
	}
}
