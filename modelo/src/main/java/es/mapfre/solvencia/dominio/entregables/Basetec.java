package es.mapfre.solvencia.dominio.entregables;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.BasetecKey;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class Basetec implements EntidadBase<BasetecKey>, EntidadConBaseTec {

	public static final int IND_BT = 0;
	public static final int IND_FCIERRE = 1;
	public static final int IND_CNEGOCIO = 2;
	public static final int IND_CCANAL = 3;
	public static final int IND_KRAMO = 4;
	public static final int IND_KMODALIDAD = 5;
	public static final int IND_KGARANTIA = 6;
	public static final int IND_PINTERTECN1MAX = 7;
	public static final int IND_PINTERTECN1MIN = 8;
	public static final int IND_PINTERTECN1MED = 9;
	public static final int IND_PINTERTECN2MAX = 10;
	public static final int IND_PINTERTECN2MIN = 11;
	public static final int IND_PINTERTECN2MED = 12;
	public static final int IND_TABLAREPRE1 = 13;
	public static final int IND_PORCPROVREPRE1 = 14;
	public static final int IND_TABLAREPRE2 = 15;
	public static final int IND_PORCPROVREPRE2 = 16;
	public static final int IND_TABLAREPRE3 = 17;
	public static final int IND_PORCPROVREPRE3 = 18;
	public static final int IND_PGASTGESIN1 = 19;
	public static final int IND_PGASTGESIN2 = 20;
	public static final int IND_PGASTGESEX = 21;
	public static final int IND_CURVATI1 = 22;
	public static final int IND_PORCPROVCURVATI1 = 23;
	public static final int IND_CURVATI2 = 24;
	public static final int IND_PORCPROVCURVATI2 = 25;
	public static final int IND_CURVATI3 = 26;
	public static final int IND_PORCPROVCURVATI3 = 27;
	public static final int IND_FACTOR1 = 28;
	public static final int IND_INDFACTOR2 = 29;
	public static final int IND_TABLATANUL1 = 30;
	public static final int IND_PORCPROVTANUL1 = 31;
	public static final int IND_TABLATANUL2 = 32;
	public static final int IND_PORCPROVTANUL2 = 33;
	public static final int IND_TABLATANUL3 = 34;
	public static final int IND_PORCPROVTANUL3 = 35;
	public static final int IND_GASTREALUNITARIO = 36;
	public static final int IND_GASTREAL = 37;
	public static final int IND_IPC = 38;
	
	
	@PortableProperty(IND_BT) private String bt;
	@PortableProperty(IND_FCIERRE) private Timestamp fcierre;
	@PortableProperty(IND_CNEGOCIO) private String cnegocio;
	@PortableProperty(IND_CCANAL) private Integer ccanal;
	@PortableProperty(IND_KRAMO) private String kramo;
	@PortableProperty(IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(IND_KGARANTIA) private Integer kgarantia;
	@PortableProperty(value=IND_PINTERTECN1MAX, codec=BigDecimalSolvenciaCodec.class) private BigDecimal pintertecn1max;
	@PortableProperty(value=IND_PINTERTECN1MIN, codec=BigDecimalSolvenciaCodec.class) private BigDecimal pintertecn1min;
	@PortableProperty(value=IND_PINTERTECN1MED, codec=BigDecimalSolvenciaCodec.class) private BigDecimal pintertecn1med;
	@PortableProperty(value=IND_PINTERTECN2MAX, codec=BigDecimalSolvenciaCodec.class) private BigDecimal pintertecn2max;
	@PortableProperty(value=IND_PINTERTECN2MIN, codec=BigDecimalSolvenciaCodec.class) private BigDecimal pintertecn2min;
	@PortableProperty(value=IND_PINTERTECN2MED, codec=BigDecimalSolvenciaCodec.class) private BigDecimal pintertecn2med;
	@PortableProperty(IND_TABLAREPRE1) private String tablarepre1;
	@PortableProperty(value=IND_PORCPROVREPRE1, codec=BigDecimalSolvenciaCodec.class) private BigDecimal porcprovrepre1;
	@PortableProperty(IND_TABLAREPRE2) private String tablarepre2;
	@PortableProperty(value=IND_PORCPROVREPRE2, codec=BigDecimalSolvenciaCodec.class) private BigDecimal porcprovrepre2;
	@PortableProperty(IND_TABLAREPRE3) private String tablarepre3;
	@PortableProperty(value=IND_PORCPROVREPRE3, codec=BigDecimalSolvenciaCodec.class) private BigDecimal porcprovrepre3;
	@PortableProperty(value=IND_PGASTGESIN1, codec=BigDecimalSolvenciaCodec.class) private BigDecimal pgastgesin1;
	@PortableProperty(value=IND_PGASTGESIN2, codec=BigDecimalSolvenciaCodec.class) private BigDecimal pgastgesin2;
	@PortableProperty(value=IND_PGASTGESEX, codec=BigDecimalSolvenciaCodec.class) private BigDecimal pgastgesex;
	@PortableProperty(IND_CURVATI1) private String curvati1;
	@PortableProperty(value=IND_PORCPROVCURVATI1, codec=BigDecimalSolvenciaCodec.class) private BigDecimal porcprovcurvati1;
	@PortableProperty(IND_CURVATI2) private String curvati2;
	@PortableProperty(value=IND_PORCPROVCURVATI2, codec=BigDecimalSolvenciaCodec.class) private BigDecimal porcprovcurvati2;
	@PortableProperty(IND_CURVATI2) private String curvati3;
	@PortableProperty(value=IND_PORCPROVCURVATI3, codec=BigDecimalSolvenciaCodec.class) private BigDecimal porcprovcurvati3;
	@PortableProperty(value=IND_FACTOR1, codec=BigDecimalSolvenciaCodec.class) private BigDecimal factor1;
	@PortableProperty(IND_INDFACTOR2) private String indfactor2;
	@PortableProperty(IND_TABLATANUL1) private String tablatanul1;
	@PortableProperty(value=IND_PORCPROVTANUL1, codec=BigDecimalSolvenciaCodec.class) private BigDecimal porcprovtanul1;
	@PortableProperty(IND_TABLATANUL2) private String tablatanul2;
	@PortableProperty(value=IND_PORCPROVTANUL2, codec=BigDecimalSolvenciaCodec.class) private BigDecimal porcprovtanul2;
	@PortableProperty(IND_TABLATANUL3) private String tablatanul3;
	@PortableProperty(value=IND_PORCPROVTANUL3, codec=BigDecimalSolvenciaCodec.class) private BigDecimal porcprovtanul3;
	@PortableProperty(value=IND_GASTREAL, codec=BigDecimalSolvenciaCodec.class) private BigDecimal gastreal;
	@PortableProperty(value=IND_GASTREALUNITARIO, codec=BigDecimalSolvenciaCodec.class) private BigDecimal gastrealunitario;
	@PortableProperty(value=IND_IPC, codec=BigDecimalSolvenciaCodec.class) private BigDecimal ipc;
	




	public String getBt() {
		return bt;
	}





	public void setBt(String bt) {
		this.bt = bt;
	}





	public Timestamp getFcierre() {
		return fcierre;
	}





	public void setFcierre(Timestamp fcierre) {
		this.fcierre = fcierre;
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





	public Integer getKgarantia() {
		return kgarantia;
	}





	public void setKgarantia(Integer kgarantia) {
		this.kgarantia = kgarantia;
	}





	public BigDecimal getPintertecn1max() {
		return pintertecn1max;
	}





	public void setPintertecn1max(BigDecimal pintertecn1max) {
		this.pintertecn1max = pintertecn1max;
	}





	public BigDecimal getPintertecn1min() {
		return pintertecn1min;
	}





	public void setPintertecn1min(BigDecimal pintertecn1min) {
		this.pintertecn1min = pintertecn1min;
	}





	public BigDecimal getPintertecn1med() {
		return pintertecn1med;
	}





	public void setPintertecn1med(BigDecimal pintertecn1med) {
		this.pintertecn1med = pintertecn1med;
	}





	public BigDecimal getPintertecn2max() {
		return pintertecn2max;
	}





	public void setPintertecn2max(BigDecimal pintertecn2max) {
		this.pintertecn2max = pintertecn2max;
	}





	public BigDecimal getPintertecn2min() {
		return pintertecn2min;
	}





	public void setPintertecn2min(BigDecimal pintertecn2min) {
		this.pintertecn2min = pintertecn2min;
	}





	public BigDecimal getPintertecn2med() {
		return pintertecn2med;
	}





	public void setPintertecn2med(BigDecimal pintertecn2med) {
		this.pintertecn2med = pintertecn2med;
	}





	public String getTablarepre1() {
		return tablarepre1;
	}





	public void setTablarepre1(String tablarepre1) {
		this.tablarepre1 = tablarepre1;
	}





	public BigDecimal getPorcprovrepre1() {
		return porcprovrepre1;
	}





	public void setPorcprovrepre1(BigDecimal porcprovrepre1) {
		this.porcprovrepre1 = porcprovrepre1;
	}





	public String getTablarepre2() {
		return tablarepre2;
	}





	public void setTablarepre2(String tablarepre2) {
		this.tablarepre2 = tablarepre2;
	}





	public BigDecimal getPorcprovrepre2() {
		return porcprovrepre2;
	}





	public void setPorcprovrepre2(BigDecimal porcprovrepre2) {
		this.porcprovrepre2 = porcprovrepre2;
	}





	public String getTablarepre3() {
		return tablarepre3;
	}





	public void setTablarepre3(String tablarepre3) {
		this.tablarepre3 = tablarepre3;
	}





	public BigDecimal getPorcprovrepre3() {
		return porcprovrepre3;
	}





	public void setPorcprovrepre3(BigDecimal porcprovrepre3) {
		this.porcprovrepre3 = porcprovrepre3;
	}





	public BigDecimal getPgastgesin1() {
		return pgastgesin1;
	}





	public void setPgastgesin1(BigDecimal pgastgesin1) {
		this.pgastgesin1 = pgastgesin1;
	}





	public BigDecimal getPgastgesin2() {
		return pgastgesin2;
	}





	public void setPgastgesin2(BigDecimal pgastgesin2) {
		this.pgastgesin2 = pgastgesin2;
	}





	public BigDecimal getPgastgesex() {
		return pgastgesex;
	}





	public void setPgastgesex(BigDecimal pgastgesex) {
		this.pgastgesex = pgastgesex;
	}





	public String getCurvati1() {
		return curvati1;
	}





	public void setCurvati1(String curvati1) {
		this.curvati1 = curvati1;
	}





	public BigDecimal getPorcprovcurvati1() {
		return porcprovcurvati1;
	}





	public void setPorcprovcurvati1(BigDecimal porcprovcurvati1) {
		this.porcprovcurvati1 = porcprovcurvati1;
	}





	public String getCurvati2() {
		return curvati2;
	}





	public void setCurvati2(String curvati2) {
		this.curvati2 = curvati2;
	}





	public BigDecimal getPorcprovcurvati2() {
		return porcprovcurvati2;
	}





	public void setPorcprovcurvati2(BigDecimal porcprovcurvati2) {
		this.porcprovcurvati2 = porcprovcurvati2;
	}





	public String getCurvati3() {
		return curvati3;
	}





	public void setCurvati3(String curvati3) {
		this.curvati3 = curvati3;
	}





	public BigDecimal getPorcprovcurvati3() {
		return porcprovcurvati3;
	}





	public void setPorcprovcurvati3(BigDecimal porcprovcurvati3) {
		this.porcprovcurvati3 = porcprovcurvati3;
	}





	public BigDecimal getFactor1() {
		return factor1;
	}





	public void setFactor1(BigDecimal factor1) {
		this.factor1 = factor1;
	}





	public String getIndfactor2() {
		return indfactor2;
	}





	public void setIndfactor2(String indfactor2) {
		this.indfactor2 = indfactor2;
	}





	public String getTablatanul1() {
		return tablatanul1;
	}





	public void setTablatanul1(String tablatanul1) {
		this.tablatanul1 = tablatanul1;
	}





	public BigDecimal getPorcprovtanul1() {
		return porcprovtanul1;
	}





	public void setPorcprovtanul1(BigDecimal porcprovtanul1) {
		this.porcprovtanul1 = porcprovtanul1;
	}





	public String getTablatanul2() {
		return tablatanul2;
	}





	public void setTablatanul2(String tablatanul2) {
		this.tablatanul2 = tablatanul2;
	}





	public BigDecimal getPorcprovtanul2() {
		return porcprovtanul2;
	}





	public void setPorcprovtanul2(BigDecimal porcprovtanul2) {
		this.porcprovtanul2 = porcprovtanul2;
	}





	public String getTablatanul3() {
		return tablatanul3;
	}





	public void setTablatanul3(String tablatanul3) {
		this.tablatanul3 = tablatanul3;
	}





	public BigDecimal getPorcprovtanul3() {
		return porcprovtanul3;
	}





	public void setPorcprovtanul3(BigDecimal porcprovtanul3) {
		this.porcprovtanul3 = porcprovtanul3;
	}





	public BigDecimal getGastreal() {
		return gastreal;
	}





	public void setGastreal(BigDecimal gastreal) {
		this.gastreal = gastreal;
	}





	public BigDecimal getGastrealunitario() {
		return gastrealunitario;
	}





	public void setGastrealunitario(BigDecimal gastrealunitario) {
		this.gastrealunitario = gastrealunitario;
	}





	public BigDecimal getIpc() {
		return ipc;
	}





	public void setIpc(BigDecimal ipc) {
		this.ipc = ipc;
	}



	


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((curvati1 == null) ? 0 : curvati1.hashCode());
		result = prime * result + ((curvati2 == null) ? 0 : curvati2.hashCode());
		result = prime * result + ((curvati3 == null) ? 0 : curvati3.hashCode());
		result = prime * result + ((factor1 == null) ? 0 : factor1.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((gastreal == null) ? 0 : gastreal.hashCode());
		result = prime * result + ((gastrealunitario == null) ? 0 : gastrealunitario.hashCode());
		result = prime * result + ((indfactor2 == null) ? 0 : indfactor2.hashCode());
		result = prime * result + ((ipc == null) ? 0 : ipc.hashCode());
		result = prime * result + ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((pgastgesex == null) ? 0 : pgastgesex.hashCode());
		result = prime * result + ((pgastgesin1 == null) ? 0 : pgastgesin1.hashCode());
		result = prime * result + ((pgastgesin2 == null) ? 0 : pgastgesin2.hashCode());
		result = prime * result + ((pintertecn1max == null) ? 0 : pintertecn1max.hashCode());
		result = prime * result + ((pintertecn1med == null) ? 0 : pintertecn1med.hashCode());
		result = prime * result + ((pintertecn1min == null) ? 0 : pintertecn1min.hashCode());
		result = prime * result + ((pintertecn2max == null) ? 0 : pintertecn2max.hashCode());
		result = prime * result + ((pintertecn2med == null) ? 0 : pintertecn2med.hashCode());
		result = prime * result + ((pintertecn2min == null) ? 0 : pintertecn2min.hashCode());
		result = prime * result + ((porcprovcurvati1 == null) ? 0 : porcprovcurvati1.hashCode());
		result = prime * result + ((porcprovcurvati2 == null) ? 0 : porcprovcurvati2.hashCode());
		result = prime * result + ((porcprovcurvati3 == null) ? 0 : porcprovcurvati3.hashCode());
		result = prime * result + ((porcprovrepre1 == null) ? 0 : porcprovrepre1.hashCode());
		result = prime * result + ((porcprovrepre2 == null) ? 0 : porcprovrepre2.hashCode());
		result = prime * result + ((porcprovrepre3 == null) ? 0 : porcprovrepre3.hashCode());
		result = prime * result + ((porcprovtanul1 == null) ? 0 : porcprovtanul1.hashCode());
		result = prime * result + ((porcprovtanul2 == null) ? 0 : porcprovtanul2.hashCode());
		result = prime * result + ((porcprovtanul3 == null) ? 0 : porcprovtanul3.hashCode());
		result = prime * result + ((tablarepre1 == null) ? 0 : tablarepre1.hashCode());
		result = prime * result + ((tablarepre3 == null) ? 0 : tablarepre3.hashCode());
		result = prime * result + ((tablarepre2 == null) ? 0 : tablarepre2.hashCode());
		result = prime * result + ((tablatanul1 == null) ? 0 : tablatanul1.hashCode());
		result = prime * result + ((tablatanul2 == null) ? 0 : tablatanul2.hashCode());
		result = prime * result + ((tablatanul3 == null) ? 0 : tablatanul3.hashCode());
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
		Basetec other = (Basetec) obj;
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
		if (curvati1 == null) {
			if (other.curvati1 != null)
				return false;
		} else if (!curvati1.equals(other.curvati1))
			return false;
		if (curvati2 == null) {
			if (other.curvati2 != null)
				return false;
		} else if (!curvati2.equals(other.curvati2))
			return false;
		if (curvati3 == null) {
			if (other.curvati3 != null)
				return false;
		} else if (!curvati3.equals(other.curvati3))
			return false;
		if (factor1 == null) {
			if (other.factor1 != null)
				return false;
		} else if (!factor1.equals(other.factor1))
			return false;
		if (fcierre == null) {
			if (other.fcierre != null)
				return false;
		} else if (!fcierre.equals(other.fcierre))
			return false;
		if (gastreal == null) {
			if (other.gastreal != null)
				return false;
		} else if (!gastreal.equals(other.gastreal))
			return false;
		if (gastrealunitario == null) {
			if (other.gastrealunitario != null)
				return false;
		} else if (!gastrealunitario.equals(other.gastrealunitario))
			return false;
		if (indfactor2 == null) {
			if (other.indfactor2 != null)
				return false;
		} else if (!indfactor2.equals(other.indfactor2))
			return false;
		if (ipc == null) {
			if (other.ipc != null)
				return false;
		} else if (!ipc.equals(other.ipc))
			return false;
		if (kgarantia == null) {
			if (other.kgarantia != null)
				return false;
		} else if (!kgarantia.equals(other.kgarantia))
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		if (kramo == null) {
			if (other.kramo != null)
				return false;
		} else if (!kramo.equals(other.kramo))
			return false;
		if (pgastgesex == null) {
			if (other.pgastgesex != null)
				return false;
		} else if (!pgastgesex.equals(other.pgastgesex))
			return false;
		if (pgastgesin1 == null) {
			if (other.pgastgesin1 != null)
				return false;
		} else if (!pgastgesin1.equals(other.pgastgesin1))
			return false;
		if (pgastgesin2 == null) {
			if (other.pgastgesin2 != null)
				return false;
		} else if (!pgastgesin2.equals(other.pgastgesin2))
			return false;
		if (pintertecn1max == null) {
			if (other.pintertecn1max != null)
				return false;
		} else if (!pintertecn1max.equals(other.pintertecn1max))
			return false;
		if (pintertecn1med == null) {
			if (other.pintertecn1med != null)
				return false;
		} else if (!pintertecn1med.equals(other.pintertecn1med))
			return false;
		if (pintertecn1min == null) {
			if (other.pintertecn1min != null)
				return false;
		} else if (!pintertecn1min.equals(other.pintertecn1min))
			return false;
		if (pintertecn2max == null) {
			if (other.pintertecn2max != null)
				return false;
		} else if (!pintertecn2max.equals(other.pintertecn2max))
			return false;
		if (pintertecn2med == null) {
			if (other.pintertecn2med != null)
				return false;
		} else if (!pintertecn2med.equals(other.pintertecn2med))
			return false;
		if (pintertecn2min == null) {
			if (other.pintertecn2min != null)
				return false;
		} else if (!pintertecn2min.equals(other.pintertecn2min))
			return false;
		if (porcprovcurvati1 == null) {
			if (other.porcprovcurvati1 != null)
				return false;
		} else if (!porcprovcurvati1.equals(other.porcprovcurvati1))
			return false;
		if (porcprovcurvati2 == null) {
			if (other.porcprovcurvati2 != null)
				return false;
		} else if (!porcprovcurvati2.equals(other.porcprovcurvati2))
			return false;
		if (porcprovcurvati3 == null) {
			if (other.porcprovcurvati3 != null)
				return false;
		} else if (!porcprovcurvati3.equals(other.porcprovcurvati3))
			return false;
		if (porcprovrepre1 == null) {
			if (other.porcprovrepre1 != null)
				return false;
		} else if (!porcprovrepre1.equals(other.porcprovrepre1))
			return false;
		if (porcprovrepre2 == null) {
			if (other.porcprovrepre2 != null)
				return false;
		} else if (!porcprovrepre2.equals(other.porcprovrepre2))
			return false;
		if (porcprovrepre3 == null) {
			if (other.porcprovrepre3 != null)
				return false;
		} else if (!porcprovrepre3.equals(other.porcprovrepre3))
			return false;
		if (porcprovtanul1 == null) {
			if (other.porcprovtanul1 != null)
				return false;
		} else if (!porcprovtanul1.equals(other.porcprovtanul1))
			return false;
		if (porcprovtanul2 == null) {
			if (other.porcprovtanul2 != null)
				return false;
		} else if (!porcprovtanul2.equals(other.porcprovtanul2))
			return false;
		if (porcprovtanul3 == null) {
			if (other.porcprovtanul3 != null)
				return false;
		} else if (!porcprovtanul3.equals(other.porcprovtanul3))
			return false;
		if (tablarepre1 == null) {
			if (other.tablarepre1 != null)
				return false;
		} else if (!tablarepre1.equals(other.tablarepre1))
			return false;
		if (tablarepre3 == null) {
			if (other.tablarepre3 != null)
				return false;
		} else if (!tablarepre3.equals(other.tablarepre3))
			return false;
		if (tablarepre2 == null) {
			if (other.tablarepre2 != null)
				return false;
		} else if (!tablarepre2.equals(other.tablarepre2))
			return false;
		if (tablatanul1 == null) {
			if (other.tablatanul1 != null)
				return false;
		} else if (!tablatanul1.equals(other.tablatanul1))
			return false;
		if (tablatanul2 == null) {
			if (other.tablatanul2 != null)
				return false;
		} else if (!tablatanul2.equals(other.tablatanul2))
			return false;
		if (tablatanul3 == null) {
			if (other.tablatanul3 != null)
				return false;
		} else if (!tablatanul3.equals(other.tablatanul3))
			return false;
		return true;
	}


	


	@Override
	public String toString() {
		return "Basetec [bt=" + bt + ", fcierre=" + fcierre + ", cnegocio=" + cnegocio + ", ccanal=" + ccanal
				+ ", kramo=" + kramo + ", kmodalidad=" + kmodalidad + ", kgarantia=" + kgarantia + ", pintertecn1max="
				+ pintertecn1max + ", pintertecn1min=" + pintertecn1min + ", pintertecn1med=" + pintertecn1med
				+ ", pintertecn2max=" + pintertecn2max + ", pintertecn2min=" + pintertecn2min + ", pintertecn2med="
				+ pintertecn2med + ", tablarepre1=" + tablarepre1 + ", porcprovrepre1=" + porcprovrepre1
				+ ", tablarepre2=" + tablarepre2 + ", porcprovrepre2=" + porcprovrepre2 + ", tablarepre3=" + tablarepre3
				+ ", porcprovrepre3=" + porcprovrepre3 + ", pgastgesin1=" + pgastgesin1 + ", pgastgesin2=" + pgastgesin2
				+ ", pgastgesex=" + pgastgesex + ", curvati1=" + curvati1 + ", porcprovcurvati1=" + porcprovcurvati1
				+ ", curvati2=" + curvati2 + ", porcprovcurvati2=" + porcprovcurvati2 + ", curvati3=" + curvati3
				+ ", porcprovcurvati3=" + porcprovcurvati3 + ", factor1=" + factor1 + ", indfactor2=" + indfactor2
				+ ", tablatanul1=" + tablatanul1 + ", porcprovtanul1=" + porcprovtanul1 + ", tablatanul2=" + tablatanul2
				+ ", porcprovtanul2=" + porcprovtanul2 + ", tablatanul3=" + tablatanul3 + ", porcprovtanul3="
				+ porcprovtanul3 + ", gastreal=" + gastreal + ", gastrealunitario=" + gastrealunitario + ", ipc=" + ipc
				+ "]";
	}





	@Override
	public BasetecKey getKey() {
		return new BasetecKey(bt, fcierre, kmodalidad, kgarantia, kramo);
	}
	

}
