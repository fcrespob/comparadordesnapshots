/* MODIFICACION:TAR00302248-Contabilidad SolvenciaII en SSAA 
   FECHA: 25/09/2017
   AUTOR: INDRA
*/
 /* MODIFICACION:MU-2019-053597:Código SD01707312: Incidencia en FICHERO CONTAB PARA SEPARAR PTRI DE PROV MATEMATICA (CLOUD)
   FECHA: 26/08/2019 
   AUTOR: INDRA
   DESCRIPCION: Se incluye en campo ptipoprovi en el fichero CONTAB
*/

package es.mapfre.solvencia.dominio.entregables;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.ContabilidadKey;
import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class Contabilidad implements EntidadBase<ContabilidadKey>, EntidadConBaseTec {

	public static final int IND_BT = 0;
	public static final int IND_FECCIERRE = 1;
	public static final int IND_CCANAL = 2;
	public static final int IND_KMODALIDAD = 3;
	public static final int IND_KPOLIZA = 4;
	public static final int IND_KSUBPOLIZA = 5;
	public static final int IND_KRAMO = 6;
	public static final int IND_TIPORES = 7;
	public static final int IND_PRV = 8;
	public static final int IND_FECEFECINI = 9;
	public static final int IND_FECEFECFIN = 10;
	public static final int IND_UMICKEY = 11;
	public static final int IND_CNEGOCIO = 12;
	public static final int IND_KOFICONT = 13;
//JAVIVIL
	public static final int IND_KMODEXT = 14;
//JAVIVIL
	public static final int IND_CTIPOPROVI = 15;
	
	


	@PortableProperty(IND_BT) private String bt;
	@PortableProperty(IND_FECCIERRE) private Timestamp feccierre;
	@PortableProperty(IND_CCANAL) private Integer ccanal;
	@PortableProperty(IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(IND_KPOLIZA) private Long kpoliza;
	@PortableProperty(IND_KSUBPOLIZA) private Integer ksubpoliza;
	@PortableProperty(IND_KRAMO) private String kramo;	
	@PortableProperty(IND_TIPORES) private String tipores;	
	@PortableProperty(value = IND_PRV, codec = BigDecimalSolvenciaCodec.class) private BigDecimal prv;	
	@PortableProperty(IND_FECEFECINI) private Timestamp fefecini;
	@PortableProperty(IND_FECEFECFIN) private Timestamp fefecfin;
	@PortableProperty(IND_UMICKEY) private UmicKey umickey;
	@PortableProperty(IND_CNEGOCIO) private String cnegocio;
	@PortableProperty(IND_KOFICONT) private String koficont;
	@PortableProperty(IND_KMODEXT) private Integer kmodext;	
	@PortableProperty(IND_CTIPOPROVI) private String ctipoprovi;	
//JAVIVIL
	
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

	public BigDecimal getPrv() {
		return prv;
	}

	public void setPrv(BigDecimal prv) {
		this.prv = prv;
	}
	
	public Timestamp getFefecini() {
		return fefecini;
	}

	public void setFefecini(Timestamp fefecini) {
		this.fefecini = fefecini;
	}
	
	public Timestamp getFefecfin() {
		return fefecfin;
	}

	public void setFefecfin(Timestamp fefecfin) {
		this.fefecfin = fefecfin;
	}
	
	
	public String getTipores() {
		return tipores;
	}

	public void setTipores(String tipores) {
		this.tipores = tipores;
	}

	public UmicKey getUmickey() {
		return umickey;
	}

	public void setUmickey(UmicKey umickey) {
		this.umickey = umickey;
	}

	public String getCnegocio() {
		return cnegocio;
	}

	public void setCnegocio(String cnegocio) {
		this.cnegocio = cnegocio;
	}

	public String getKoficont() {
		return koficont;
	}

	public void setKoficont(String koficont) {
		this.koficont = koficont;
	}
//javivil
	public Integer getKmodext() {
			return kmodext;
		}

	public void setKmodext(Integer kmodext) {
			this.kmodext = kmodext;
		}
//javivil
	public String getctipoprovi() {
		return ctipoprovi;
	}

public void setctipoprovi(String ctipoprovi) {
		this.ctipoprovi = ctipoprovi;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((feccierre == null) ? 0 : feccierre.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((tipores == null) ? 0 : tipores.hashCode());
		result = prime * result + ((fefecini == null) ? 0 : fefecini.hashCode());
		result = prime * result + ((fefecfin == null) ? 0 : fefecfin.hashCode());
		result = prime * result + ((umickey == null) ? 0 : umickey.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((koficont == null) ? 0 : koficont.hashCode());
//javivil
		result = prime * result + ((kmodext == null) ? 0 : kmodext.hashCode());
//javivil		
		result = prime * result + ((ctipoprovi == null) ? 0 : ctipoprovi.hashCode());
		
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
		Contabilidad other = (Contabilidad) obj;
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
		if (feccierre == null) {
			if (other.feccierre != null)
				return false;
		} else if (!feccierre.equals(other.feccierre))
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
		if (ksubpoliza == null) {
			if (other.ksubpoliza != null)
				return false;
		} else if (!ksubpoliza.equals(other.ksubpoliza))
			return false;

		if (kramo == null) {
			if (other.kramo != null)
				return false;
		} else if (!kramo.equals(other.kramo))
			return false;
			
		if (tipores == null) {
			if (other.tipores != null)
				return false;
		} else if (!tipores.equals(other.tipores))
			return false;
		
		if (prv == null) {
			if (other.prv != null)
				return false;
		} else if (!prv.equals(other.prv))
			return false;
		
		if (fefecini == null) {
			if (other.fefecini != null)
				return false;
		} else if (!fefecini.equals(other.fefecini))
			return false;
		
		if (fefecfin == null) {
			if (other.fefecfin != null)
				return false;
		} else if (!fefecfin.equals(other.fefecfin))
			return false;
		if (umickey == null) {
			if (other.umickey != null)
				return false;
		} else if (!umickey.equals(other.umickey))
			return false;
		if (cnegocio == null) {
			if (other.cnegocio != null)
				return false;
		} else if (!cnegocio.equals(other.cnegocio))
			return false;	
		if (koficont == null) {
			if (other.koficont != null)
				return false;
		} else if (!koficont.equals(other.koficont))
			return false;
		if (kmodext == null) {
				if (other.kmodext != null)
					return false;
		}
		if (ctipoprovi == null) {
			if (other.ctipoprovi != null)
				return false;
		}
		return true;
	}
	


	


	@Override
	public String toString() {
		return "Contabilidad [bt=" + bt + ", feccierre=" + feccierre + ", ccanal=" + ccanal + ", kramo=" 
				+ kramo + ", kmodalidad=" + kmodalidad + ",  kpoliza=" + kpoliza + ", ksubpoliza=" 
				+ ksubpoliza + ", moneda=" + ", tipores=" + tipores +", prv=" 
				+ prv +", fefecini=" + fefecini +", fefecfin=" + fefecfin +", cnegocio=" + cnegocio +", koficont=" + koficont + ",kmodext=" + kmodext + ",ctipoprovi=" 
				+ ctipoprovi + "]";
	}





	@Override
	public ContabilidadKey getKey() {
		return new ContabilidadKey(bt, ccanal, feccierre ,kramo, kmodalidad, kpoliza, ksubpoliza, koficont, kmodext, ctipoprovi);
	}

}

	
	

