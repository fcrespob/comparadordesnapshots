package es.mapfre.solvencia.coherence.keys.entregables;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.entregables.FlujosTotP;
import es.mapfre.solvencia.dominio.entregables.FlujosTotP;

@Portable	
public class FlujosTotPKey implements Comparable<FlujosTotPKey> {

	//@PortableProperty(FlujosTotP.IND_BT) private String bt;
	//@PortableProperty(FlujosTotP.IND_CNEGOCIO) private String cnegocio;
	@PortableProperty(FlujosTotP.IND_KAJUSTE)
	private Integer kajuste;
	@PortableProperty(FlujosTotP.IND_KCERTIFICADO) 
	private Integer kcertificado;
	@PortableProperty(FlujosTotP.IND_KGARANTIA) 
	private Integer kgarantia;
	@PortableProperty(FlujosTotP.IND_KMODALIDAD) 
	private Integer kmodalidad;
	@PortableProperty(FlujosTotP.IND_KPOLIZA) 
	private Long kpoliza;
	@PortableProperty(FlujosTotP.IND_KPRESTACION) 
	private String kprestacion;
	@PortableProperty(FlujosTotP.IND_KSUBPOLIZA) 
	private Integer ksubpoliza;
	@PortableProperty(FlujosTotP.IND_BT) 
	private String bt;
	@PortableProperty(FlujosTotP.IND_NSUSCRI) 
	private Integer nsuscri;
	@PortableProperty(FlujosTotP.IND_NORDEN)
	private Integer norden;
	@PortableProperty(FlujosTotP.IND_CTIPOAPORT)
	private String ctipoaport;
	
	
	public FlujosTotPKey() {
		super();
	}
	
	/*public FlujosTotPKey(String bt, String cnegocio) {
		super();
		this.bt = bt;
		this.cnegocio = cnegocio;						
	}*/
	
	public FlujosTotPKey(Integer kajuste, Integer kcertificado, Integer kgarantia, Integer kmodalidad,
			Long kpoliza, String kprestacion, Integer ksubpoliza, String bt, Integer nsuscri, Integer norden,
			String ctipoaport) {
		super();
		this.kajuste = kajuste;
		this.kcertificado = kcertificado;
		this.kgarantia = kgarantia;
		this.kmodalidad = kmodalidad;
		this.kpoliza = kpoliza;
		this.kprestacion = kprestacion;
		this.ksubpoliza = ksubpoliza;
		this.bt = bt;
		this.nsuscri = nsuscri;
		this.norden = norden;
		this.ctipoaport = ctipoaport;
				
	}
	

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		//result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		//result = prime * result	+ ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ctipoaport == null) ? 0 : ctipoaport.hashCode());
		result = prime * result + ((kajuste == null) ? 0 : kajuste.hashCode());
		result = prime * result + ((kcertificado == null) ? 0 : kcertificado.hashCode());
		result = prime * result + ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((kprestacion == null) ? 0 : kprestacion.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((norden == null) ? 0 : norden.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
		
		return result;
	}


	@Override //NOSONAR
	public boolean equals(Object obj) { //NOSONAR
		/*if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlujosTotPKey other = (FlujosTotPKey) obj; 
		if (bt == null) {
			if (other.bt != null)
				return false;
		} else if (!bt.equals(other.bt))
			return false;
		if (cnegocio == null) {
			if (other.cnegocio != null)
				return false;
		} else if (!cnegocio.equals(other.cnegocio))
			return false;
		*/
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FlujosTotPKey other = (FlujosTotPKey) obj;
		if (bt == null) {
			if (other.bt != null) {
				return false;
			}
		} else if (!bt.equals(other.bt)) {
			return false;
		}
		if (ctipoaport == null) {
			if (other.ctipoaport != null) {
				return false;
			}
		} else if (!ctipoaport.equals(other.ctipoaport)) {
			return false;
		}
		if (kajuste == null) {
			if (other.kajuste != null) {
				return false;
			}
		} else if (!kajuste.equals(other.kajuste)) {
			return false;
		}
		if (kcertificado == null) {
			if (other.kcertificado != null) {
				return false;
			}
		} else if (!kcertificado.equals(other.kcertificado)) {
			return false;
		}
		if (kgarantia == null) {
			if (other.kgarantia != null) {
				return false;
			}
		} else if (!kgarantia.equals(other.kgarantia)) {
			return false;
		}
		if (kmodalidad == null) {
			if (other.kmodalidad != null) {
				return false;
			}
		} else if (!kmodalidad.equals(other.kmodalidad)) {
			return false;
		}
		if (kpoliza == null) {
			if (other.kpoliza != null) {
				return false;
			}
		} else if (!kpoliza.equals(other.kpoliza)) {
			return false;
		}
		if (kprestacion == null) {
			if (other.kprestacion != null) {
				return false;
			}
		} else if (!kprestacion.equals(other.kprestacion)) {
			return false;
		}
		if (ksubpoliza == null) {
			if (other.ksubpoliza != null) {
				return false;
			}
		} else if (!ksubpoliza.equals(other.ksubpoliza)) {
			return false;
		}
		if (norden == null) {
			if (other.norden != null) {
				return false;
			}
		} else if (!norden.equals(other.norden)) {
			return false;
		}
		if (nsuscri == null) {
			if (other.nsuscri != null) {
				return false;
			}
		} else if (!nsuscri.equals(other.nsuscri)) {
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * El orden es:
	 * 
	 * <pre>
	 * -	POLIZA
	 * -	SUBPOLIZA
	 * -	CERTIFICADO
	 * -	SUSCRIPCION
	 * -	ORDEN
	 * -	GARANTIA
	 * -	PRESTACION
	 * -	AJUSTE
	 * -	TIPO APORTACION
	 * </pre>
	 * 
	 * @param o
	 * @return
	 */
	
	@Override
	public String toString() {
		return "FlujosTotPKey [kajuste=" + kajuste + ", kcertificado=" + kcertificado + ", kgarantia=" + kgarantia + ", kmodalidad=" + kmodalidad + ", kpoliza=" + kpoliza + ", kprestacion=" + kprestacion + ", ksubpoliza=" + ksubpoliza + ", bt=" + bt + ", nsuscri=" + nsuscri + ", norden=" + norden + ", ctipoaport=" + ctipoaport + "]";
	}
	
	public int compareTo(FlujosTotPKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		//compareToBuilder.append(this.bt, o.bt);
		//compareToBuilder.append(this.cnegocio, o.cnegocio);
		// El orden se debe mantener
		compareToBuilder.append(this.kpoliza, o.kpoliza);
		compareToBuilder.append(this.ksubpoliza, o.ksubpoliza);
		compareToBuilder.append(this.kcertificado, o.kcertificado);
		compareToBuilder.append(this.nsuscri, o.nsuscri);
		compareToBuilder.append(this.norden, o.norden);
		compareToBuilder.append(this.kgarantia, o.kgarantia);
		compareToBuilder.append(this.kprestacion, o.kprestacion);
		compareToBuilder.append(this.kajuste, o.kajuste);
		compareToBuilder.append(this.ctipoaport, o.ctipoaport);
		compareToBuilder.append(this.kmodalidad, o.kmodalidad);
		compareToBuilder.append(this.bt, o.bt);
		
		return compareToBuilder.toComparison();
	}	

}
