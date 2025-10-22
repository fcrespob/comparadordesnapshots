package es.mapfre.solvencia.dominio.formulacion;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;


@Portable
public class PeriodosFall{
	
	public static final int FEC_DESDE = 1;
	private static final int FEC_DEVENGO = 2;
	private static final int CUANTIA_FALL = 3;
	private static final int FDEVFALL = 4;
	private static final int ACTJFALL = 5;
	private static final int ACTJFALLVIDA = 6;
	private static final int LXFCALC = 7;
	private static final int LYFCALC = 8;
	private static final int LXF363 = 9;
	private static final int LYF363 = 10;
	
	@PortableProperty(FEC_DESDE) private Timestamp fecDesde;	
	@PortableProperty(FEC_DEVENGO) private Timestamp fecDevengo;
	@PortableProperty(CUANTIA_FALL) private java.math.BigDecimal cuantiaFall;
	@PortableProperty(FDEVFALL) private Timestamp fDevFall;
	@PortableProperty(ACTJFALL) private java.math.BigDecimal actJFall;
	@PortableProperty(ACTJFALLVIDA) private java.math.BigDecimal actJFallVida;
	@PortableProperty(LXFCALC) private java.math.BigDecimal lxFcalc;
	@PortableProperty(LYFCALC) private java.math.BigDecimal lyFcalc;
	@PortableProperty(LXF363) private java.math.BigDecimal lxF363;
	@PortableProperty(LYF363) private java.math.BigDecimal lyF363;
	
	
	public Timestamp getFecDesde() {
		return fecDesde;
	}
	public void setFecDesde(Timestamp fecDesde) {
		this.fecDesde = fecDesde;
	}
	public Timestamp getFecDevengo() {
		return fecDevengo;
	}
	public void setFecDevengo(Timestamp fecDevengo) {
		this.fecDevengo = fecDevengo;
	}
	public java.math.BigDecimal getCuantiaFall() {
		return cuantiaFall;
	}
	public void setCuantiaFall(java.math.BigDecimal cuantiaFall) {
		this.cuantiaFall = cuantiaFall;
	}
	public Timestamp getfDevFall() {
		return fDevFall;
	}
	public void setfDevFall(Timestamp fDevFall) {
		this.fDevFall = fDevFall;
	}
	public java.math.BigDecimal getActJFall() {
		return actJFall;
	}
	public void setActJFall(java.math.BigDecimal actJFall) {
		this.actJFall = actJFall;
	}
	public java.math.BigDecimal getLxFcalc() {
		return lxFcalc;
	}
	public void setLxFcalc(java.math.BigDecimal lxFcalc) {
		this.lxFcalc = lxFcalc;
	}
	public java.math.BigDecimal getLyFcalc() {
		return lyFcalc;
	}
	public void setLyFcalc(java.math.BigDecimal lyFcalc) {
		this.lyFcalc = lyFcalc;
	}
		
	public java.math.BigDecimal getActJFallVida() {
		return actJFallVida;
	}
	public void setActJFallVida(java.math.BigDecimal actJFallVida) {
		this.actJFallVida = actJFallVida;
	}
	
	
	public java.math.BigDecimal getLxF363() {
		return lxF363;
	}
	public void setLxF363(java.math.BigDecimal lxF363) {
		this.lxF363 = lxF363;
	}
	public java.math.BigDecimal getLyF363() {
		return lyF363;
	}
	public void setLyF363(java.math.BigDecimal lyF363) {
		this.lyF363 = lyF363;
	}
	
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actJFall == null) ? 0 : actJFall.hashCode());
		result = prime * result
				+ ((actJFallVida == null) ? 0 : actJFallVida.hashCode());
		result = prime * result
				+ ((cuantiaFall == null) ? 0 : cuantiaFall.hashCode());
		result = prime * result
				+ ((fDevFall == null) ? 0 : fDevFall.hashCode());
		result = prime * result
				+ ((fecDesde == null) ? 0 : fecDesde.hashCode());
		result = prime * result
				+ ((fecDevengo == null) ? 0 : fecDevengo.hashCode());
		result = prime * result + ((lxF363 == null) ? 0 : lxF363.hashCode());
		result = prime * result + ((lxFcalc == null) ? 0 : lxFcalc.hashCode());
		result = prime * result + ((lyF363 == null) ? 0 : lyF363.hashCode());
		result = prime * result + ((lyFcalc == null) ? 0 : lyFcalc.hashCode());
		return result;
	}
	@Override //NOSONAR
	public boolean equals(Object obj) { //NOSONAR
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PeriodosFall other = (PeriodosFall) obj;
		if (actJFall == null) {
			if (other.actJFall != null) {
				return false;
			}
		} else if (!actJFall.equals(other.actJFall)) {
			return false;
		}
		if (actJFallVida == null) {
			if (other.actJFallVida != null) {
				return false;
			}
		} else if (!actJFallVida.equals(other.actJFallVida)) {
			return false;
		}
		if (cuantiaFall == null) {
			if (other.cuantiaFall != null) {
				return false;
			}
		} else if (!cuantiaFall.equals(other.cuantiaFall)) {
			return false;
		}
		if (fDevFall == null) {
			if (other.fDevFall != null) {
				return false;
			}
		} else if (!fDevFall.equals(other.fDevFall)) {
			return false;
		}
		if (fecDesde == null) {
			if (other.fecDesde != null) {
				return false;
			}
		} else if (!fecDesde.equals(other.fecDesde)) {
			return false;
		}
		if (fecDevengo == null) {
			if (other.fecDevengo != null) {
				return false;
			}
		} else if (!fecDevengo.equals(other.fecDevengo)) {
			return false;
		}
		if (lxF363 == null) {
			if (other.lxF363 != null) {
				return false;
			}
		} else if (!lxF363.equals(other.lxF363)) {
			return false;
		}
		if (lxFcalc == null) {
			if (other.lxFcalc != null) {
				return false;
			}
		} else if (!lxFcalc.equals(other.lxFcalc)) {
			return false;
		}
		if (lyF363 == null) {
			if (other.lyF363 != null) {
				return false;
			}
		} else if (!lyF363.equals(other.lyF363)) {
			return false;
		}
		if (lyFcalc == null) {
			if (other.lyFcalc != null) {
				return false;
			}
		} else if (!lyFcalc.equals(other.lyFcalc)) {
			return false;
		}
		return true;
	}
		
		
}
