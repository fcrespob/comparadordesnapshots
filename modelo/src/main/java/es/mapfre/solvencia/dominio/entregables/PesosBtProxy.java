package es.mapfre.solvencia.dominio.entregables;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.PesosBtProxyKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class PesosBtProxy implements EntidadBase<PesosBtProxyKey>{

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

	@PortableProperty(IND_CNEGOCIO)
	private String cnegocio;
	@PortableProperty(IND_CCANAL)
	private String ccanal;
	@PortableProperty(IND_CCARTERA)
	private String ccartera;
	@PortableProperty(IND_FCIERRE)
	private String fcierre;
	@PortableProperty(IND_KMODALIDAD)
	private String kmodalidad;
	@PortableProperty(IND_KPOLIZA)
	private String kpoliza;
	@PortableProperty(IND_KSUBPOLIZA)
	private String ksubpoliza;
	@PortableProperty(IND_KCERTIFICADO)
	private String kcertificado;
	@PortableProperty(IND_COHORTE)
	private String cohorte;
	@PortableProperty(IND_SWCASADO)
	private String swcasado;
	@PortableProperty(IND_TOTFACT_T)
	private String totFactActT;
	@PortableProperty(IND_PESOS_UOA)
	private String pesosUoA;
	
	public PesosBtProxy() {
		super();
	}

	public String getCnegocio() {
		return cnegocio;
	}

	public void setCnegocio(String cnegocio) {
		this.cnegocio = cnegocio;
	}

	public String getCcanal() {
		return ccanal;
	}

	public void setCcanal(String ccanal) {
		this.ccanal = ccanal;
	}

	public String getCcartera() {
		return ccartera;
	}

	public void setCcartera(String ccartera) {
		this.ccartera = ccartera;
	}

	public String getFcierre() {
		return fcierre;
	}

	public void setFcierre(String fcierre) {
		this.fcierre = fcierre;
	}

	public String getKmodalidad() {
		return kmodalidad;
	}

	public void setKmodalidad(String kmodalidad) {
		this.kmodalidad = kmodalidad;
	}

	public String getKpoliza() {
		return kpoliza;
	}

	public void setKpoliza(String kpoliza) {
		this.kpoliza = kpoliza;
	}

	public String getKsubpoliza() {
		return ksubpoliza;
	}

	public void setKsubpoliza(String ksubpoliza) {
		this.ksubpoliza = ksubpoliza;
	}

	public String getKcertificado() {
		return kcertificado;
	}

	public void setKcertificado(String kcertificado) {
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

	public String getTotFactActT() {
		return totFactActT;
	}

	public void setTotFactActT(String totFactActT) {
		this.totFactActT = totFactActT;
	}

	public String getPesosUoA() {
		return pesosUoA;
	}

	public void setPesosUoA(String pesosUoA) {
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
		PesosBtProxy other = (PesosBtProxy) obj;
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
	public PesosBtProxyKey getKey() {
		return new PesosBtProxyKey(cnegocio, ccanal, ccartera, fcierre, cohorte, swcasado, kpoliza, ksubpoliza, kcertificado, kmodalidad);	
	}
}
