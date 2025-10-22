package es.mapfre.solvencia.dominio.entregables;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.PesosBtKey;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class PesosBt implements EntidadBase<PesosBtKey>, EntidadConBaseTec{

	public static final int IND_BT = 1;
	public static final int IND_CNEGOCIO = 2;
	public static final int IND_CCANAL = 3;
	public static final int IND_CCARTERA = 4;
	public static final int IND_FCIERRE = 5;
	public static final int IND_KMODALIDAD = 6;
	public static final int IND_KPOLIZA = 7;
	public static final int IND_KSUBPOLIZA = 8;
	public static final int IND_KCERTIFICADO = 9;
	public static final int IND_KMODALIDADORIG = 10;
	public static final int IND_KPOLIZAORIG = 11;
	public static final int IND_KSUBPOLIZAORIG = 12;
	public static final int IND_KCERTIFICADOORIG = 13;
	public static final int IND_COHORTE = 14;
	public static final int IND_SWCASADO = 15;
	public static final int IND_TOTFACT_T = 16;
	public static final int IND_PESOS_UOA = 17;

	@PortableProperty(IND_BT)
	private String bt;
	@PortableProperty(IND_CNEGOCIO)
	private String cnegocio;
	@PortableProperty(IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(IND_CCARTERA)
	private Integer ccartera;
	@PortableProperty(IND_FCIERRE)
	private Timestamp fcierre;
	@PortableProperty(IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(IND_KSUBPOLIZA)
	private Integer ksubpoliza;
	@PortableProperty(IND_KCERTIFICADO)
	private Integer kcertificado;
	@PortableProperty(IND_KMODALIDADORIG)
	private Integer kmodalidadOrig;
	@PortableProperty(IND_KPOLIZAORIG)
	private Long kpolizaOrig;
	@PortableProperty(IND_KSUBPOLIZAORIG)
	private Integer ksubpolizaOrig;
	@PortableProperty(IND_KCERTIFICADOORIG)
	private Integer kcertificadoOrig;
	@PortableProperty(IND_COHORTE)
	private String cohorte;
	@PortableProperty(IND_SWCASADO)
	private String swcasado;
	@PortableProperty(IND_TOTFACT_T)
	private BigDecimal totFactActT;
	@PortableProperty(IND_PESOS_UOA)
	private BigDecimal pesosUoA;
	
	public PesosBt() {
		super();
	}

	public String getBt() {
		return bt;
	}

	public void setBt(String bt) {
		this.bt = bt;
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

	public Integer getKmodalidadOrig() {
		return kmodalidadOrig;
	}

	public void setKmodalidadOrig(Integer kmodalidadOrig) {
		this.kmodalidadOrig = kmodalidadOrig;
	}

	public Long getKpolizaOrig() {
		return kpolizaOrig;
	}

	public void setKpolizaOrig(Long kpolizaOrig) {
		this.kpolizaOrig = kpolizaOrig;
	}

	public Integer getKsubpolizaOrig() {
		return ksubpolizaOrig;
	}

	public void setKsubpolizaOrig(Integer ksubpolizaOrig) {
		this.ksubpolizaOrig = ksubpolizaOrig;
	}

	public Integer getKcertificadoOrig() {
		return kcertificadoOrig;
	}

	public void setKcertificadoOrig(Integer kcertificadoOrig) {
		this.kcertificadoOrig = kcertificadoOrig;
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
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((ccartera == null) ? 0 : ccartera.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((cohorte == null) ? 0 : cohorte.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((kcertificado == null) ? 0 : kcertificado.hashCode());
		result = prime * result + ((kcertificadoOrig == null) ? 0 : kcertificadoOrig.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kmodalidadOrig == null) ? 0 : kmodalidadOrig.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((kpolizaOrig == null) ? 0 : kpolizaOrig.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((ksubpolizaOrig == null) ? 0 : ksubpolizaOrig.hashCode());
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
		if (kcertificadoOrig == null) {
			if (other.kcertificadoOrig != null)
				return false;
		} else if (!kcertificadoOrig.equals(other.kcertificadoOrig))
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		if (kmodalidadOrig == null) {
			if (other.kmodalidadOrig != null)
				return false;
		} else if (!kmodalidadOrig.equals(other.kmodalidadOrig))
			return false;
		if (kpoliza == null) {
			if (other.kpoliza != null)
				return false;
		} else if (!kpoliza.equals(other.kpoliza))
			return false;
		if (kpolizaOrig == null) {
			if (other.kpolizaOrig != null)
				return false;
		} else if (!kpolizaOrig.equals(other.kpolizaOrig))
			return false;
		if (ksubpoliza == null) {
			if (other.ksubpoliza != null)
				return false;
		} else if (!ksubpoliza.equals(other.ksubpoliza))
			return false;
		if (ksubpolizaOrig == null) {
			if (other.ksubpolizaOrig != null)
				return false;
		} else if (!ksubpolizaOrig.equals(other.ksubpolizaOrig))
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
		return new PesosBtKey(bt, cnegocio, ccanal, ccartera, fcierre, cohorte, swcasado, kpoliza, ksubpoliza, kcertificado, kmodalidad);	
	}
}
