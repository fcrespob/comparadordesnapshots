package es.mapfre.solvencia.dominio.salidaCalculo;

import java.beans.Transient;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.salidaCalculo.DetalleCorrienteKey;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class DetalleCorriente implements EntidadBase<DetalleCorrienteKey>, EntidadConBaseTec {

	
	public static final int IND_FCIERRE = 3;
	public static final int IND_BT = 1;
	public static final int IND_KMODALIDAD = 2;
	public static final int IND_KPOLIZA = 0;
	public static final int IND_KSUBPOLIZA = 4;
	public static final int IND_KCERTIFICADO = 5;
	public static final int IND_NSUSCRI = 6;
	public static final int IND_NORDEN = 7;
	public static final int IND_KGARANTIA = 8;
	public static final int IND_KPRESTACION = 9;
	public static final int IND_KAJUSTE = 10;
	public static final int IND_CTIPOAPORT = 14;
	public static final int IND_FECHADESDE = 15;
	public static final int IND_FECHAHASTA = 16;
	
	public static final int IND_CNEGOCIO = 17;
	public static final int IND_CCANAL = 18;
	public static final int IND_CCARTERA = 19;
	
	public static final int IND_BLOQUEVIDA = 25;
	public static final int IND_BLOQUEFALL = 26;
	public static final int IND_BLOQUECOMPL = 27;
	public static final int IND_BLOQUEGTO = 28;
	public static final int IND_BLOQUECOMI = 29;
	public static final int IND_BLOQUERTE = 30;
	public static final int IND_BLOQUEPRIM = 31;

	public static final int IND_TOTALFLUJOPROYECCION = 32;
	
	public static final int IND_TIPOCORRIENTE = 40;

	public static final int IND_IMPPAGO = 41;
	
	public static final int IND_KRAMO = 48;
	public static final int IND_FSUSCRI = 49;
	public static final int IND_KCARTERAINV = 50;
	public static final int IND_GAPACT = 51;
	public static final int IND_INTFECCALC = 52; 
	public static final int IND_SPCOM =53; 
	public static final int IND_KMODEXT = 54;
	public static final int IND_KBENCON = 55; 
	public static final int IND_SW_PERIODO_CASADO = 56;	
	public static final int IND_UOA = 57;
	public static final int IND_KCONTRATO = 58;
	public static final int IND_RAUMIC = 59;
	public static final int IND_CSMUMIC = 60;
	public static final int IND_PATRON_CSM = 61;
	public static final int IND_ROSSP_CSM = 62;			
	
	public static final int IND_BLOQUEGTOAD = 63;
	public static final int PROV_ROSSP_CSM = 64;
	public static final int PROV_BEL_NIIF17 = 65;
	public static final int IND_OGA = 66;

	

	@PortableProperty(IND_CNEGOCIO) private String cnegocio;
	@PortableProperty(IND_CCANAL) private Integer ccanal;
	@PortableProperty(IND_CCARTERA) private Integer ccartera;
	@PortableProperty(IND_FCIERRE) private Timestamp fcierre;
	@PortableProperty(IND_BT) private String bt;
	@PortableProperty(IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(IND_KPOLIZA) private Long kpoliza;
	@PortableProperty(IND_KSUBPOLIZA) private Integer ksubpoliza;
	@PortableProperty(IND_KCERTIFICADO) private Integer kcertificado;
	@PortableProperty(IND_NSUSCRI) private Integer nsuscri;
	@PortableProperty(IND_NORDEN) private Integer norden;
	@PortableProperty(IND_KGARANTIA) private Integer kgarantia;
	@PortableProperty(IND_KPRESTACION) private String kprestacion;
	@PortableProperty(IND_KAJUSTE) private Integer kajuste;
	@PortableProperty(IND_CTIPOAPORT) private String ctipoaport;
		
	@PortableProperty(IND_FECHADESDE) private Timestamp fechaDesde;
	@PortableProperty(IND_FECHAHASTA) private Timestamp fechaHasta;	
	
	@PortableProperty(IND_BLOQUEVIDA) private BloqueCorriente bloqueVida;
	@PortableProperty(IND_BLOQUEFALL) private BloqueCorriente bloqueFall;
	@PortableProperty(IND_BLOQUECOMPL) private BloqueCorriente bloqueCompl;
	@PortableProperty(IND_BLOQUEGTO) private BloqueCorriente bloqueGto;
	@PortableProperty(IND_BLOQUECOMI) private BloqueCorriente bloqueComi;
	@PortableProperty(IND_BLOQUERTE) private BloqueCorriente bloqueRte;
	@PortableProperty(IND_BLOQUEPRIM) private BloqueCorriente bloquePrim;

	@PortableProperty(IND_TOTALFLUJOPROYECCION) private TotalFlujoProyeccion totalFlujoProyeccion = new TotalFlujoProyeccion();

	private transient CabeceraCorriente cabeceraCorriente;
	
	@PortableProperty(value=IND_IMPPAGO, codec=BigDecimalSolvenciaCodec.class) private  BigDecimal impPago;
		
	@PortableProperty(IND_KRAMO) private String kramo;
	@PortableProperty(IND_FSUSCRI) private Timestamp fsuscri;
	@PortableProperty(IND_KCARTERAINV) private String kcarterainv;
	@PortableProperty(IND_GAPACT) private String gapAct;
	@PortableProperty(value = IND_INTFECCALC, codec = BigDecimalSolvenciaCodec.class) private java.math.BigDecimal intfeccal;
	@PortableProperty(IND_SPCOM)   private String spcom;
	@PortableProperty(IND_KMODEXT) private Integer kmodext;
	@PortableProperty(IND_KBENCON) private String kbencon;
	@PortableProperty(IND_SW_PERIODO_CASADO) private Integer sw_periodo_casado;
	@PortableProperty(IND_UOA)
	private String uoa;
	@PortableProperty(IND_KCONTRATO)
	private String kcontrato;
	@PortableProperty(value = IND_RAUMIC, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal raumic;
	@PortableProperty(value = IND_CSMUMIC, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal csmumic;
	@PortableProperty(value = IND_PATRON_CSM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal patronCSM;
	@PortableProperty(value = IND_ROSSP_CSM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal rosspCSM;	
	
	@PortableProperty(IND_BLOQUEGTOAD) private BloqueCorriente bloqueGtoAd;
	
	@PortableProperty(value = PROV_ROSSP_CSM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal provRosspCSM;	
	@PortableProperty(value = PROV_BEL_NIIF17, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal provBelCSM;
	
	@PortableProperty(value = IND_OGA) private Boolean oga;	
	
	public java.math.BigDecimal getProvBelCSM() {
		return provBelCSM;
	}

	public void setProvBelCSM(java.math.BigDecimal provBelCSM) {
		this.provBelCSM = provBelCSM;
	}

	public java.math.BigDecimal getProvRosspCSM() {
		return provRosspCSM;
	}

	public void setProvRosspCSM(java.math.BigDecimal provRosspCSM) {
		this.provRosspCSM = provRosspCSM;
	}

	
	public BigDecimal getImpPago() {
		return impPago;
	}

	public void setImpPago(BigDecimal impPago) {
		this.impPago = impPago;
	}

	public Timestamp getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Timestamp fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Timestamp getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Timestamp fechaHasta) {
		this.fechaHasta = fechaHasta;
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

	public String getBt() {
		return bt;
	}

	public void setBt(String bt) {
		this.bt = bt;
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

	public Integer getNsuscri() {
		return nsuscri;
	}

	public void setNsuscri(Integer nsuscri) {
		this.nsuscri = nsuscri;
	}

	public Integer getNorden() {
		return norden;
	}

	public void setNorden(Integer norden) {
		this.norden = norden;
	}

	public Integer getKgarantia() {
		return kgarantia;
	}

	public void setKgarantia(Integer kgarantia) {
		this.kgarantia = kgarantia;
	}

	public String getKprestacion() {
		return kprestacion;
	}

	public void setKprestacion(String kprestacion) {
		this.kprestacion = kprestacion;
	}

	public Integer getKajuste() {
		return kajuste;
	}

	public void setKajuste(Integer kajuste) {
		this.kajuste = kajuste;
	}

	public String getCtipoaport() {
		return ctipoaport;
	}

	public void setCtipoaport(String ctipoaport) {
		this.ctipoaport = ctipoaport;
	}

	public BloqueCorriente getBloqueVida() {
		return bloqueVida;
	}

	public void setBloqueVida(BloqueCorriente bloqueVida) {
		this.bloqueVida = bloqueVida;
	}

	public BloqueCorriente getBloqueFall() {
		return bloqueFall;
	}

	public void setBloqueFall(BloqueCorriente bloqueFall) {
		this.bloqueFall = bloqueFall;
	}

	public BloqueCorriente getBloqueCompl() {
		return bloqueCompl;
	}

	public void setBloqueCompl(BloqueCorriente bloqueCompl) {
		this.bloqueCompl = bloqueCompl;
	}

	public BloqueCorriente getBloqueGto() {
		return bloqueGto;
	}

	public void setBloqueGto(BloqueCorriente bloqueGto) {
		this.bloqueGto = bloqueGto;
	}

	public BloqueCorriente getBloqueComi() {
		return bloqueComi;
	}

	public void setBloqueComi(BloqueCorriente bloqueComi) {
		this.bloqueComi = bloqueComi;
	}

	public BloqueCorriente getBloqueRte() {
		return bloqueRte;
	}

	public void setBloqueRte(BloqueCorriente bloqueRte) {
		this.bloqueRte = bloqueRte;
	}

	public BloqueCorriente getBloquePrim() {
		return bloquePrim;
	}

	public void setBloquePrim(BloqueCorriente bloquePrim) {
		this.bloquePrim = bloquePrim;
	}

	public TotalFlujoProyeccion getTotalFlujoProyeccion() {
		return totalFlujoProyeccion;
	}

	public void setTotalFlujoProyeccion(TotalFlujoProyeccion totalFlujoProyeccion) {
		this.totalFlujoProyeccion = totalFlujoProyeccion;
	}

	public String getKramo() {
		return kramo;
	}

	public void setKramo(String kramo) {
		this.kramo = kramo;
	}

	public Timestamp getFsuscri() {
		return fsuscri;
	}

	public void setFsuscri(Timestamp fsuscri) {
		this.fsuscri = fsuscri;
	}

	public String getKcarterainv() {
		return kcarterainv;
	}

	public void setKcarterainv(String kcarterainv) {
		this.kcarterainv = kcarterainv;
	}

	public String getGapAct() {
		return gapAct;
	}

	public void setGapAct(String gapAct) {
		this.gapAct = gapAct;
	}

	public java.math.BigDecimal getIntfeccal() {
		return intfeccal;
	}

	public void setIntfeccal(java.math.BigDecimal intfeccal) {
		this.intfeccal = intfeccal;
	}

	public String getSpcom() {
		return spcom;
	}

	public void setSpcom(String spcom) {
		this.spcom = spcom;
	}
	
	public Integer getKmodext() {
		return kmodext;
	}

	public void setKmodext(Integer kmodext) {
		this.kmodext = kmodext;
	}
	
	public String getKbencon() {
		return kbencon;
	}
	public void setKbencon(String kbencon) {
		this.kbencon = kbencon;
	}
	
	public Integer getSw_periodo_casado() {
		return sw_periodo_casado;
	}

	public void setSw_periodo_casado(Integer sw_periodo_casado) {
		this.sw_periodo_casado = sw_periodo_casado;
	}

	public String getUoa() {
		return uoa;
	}

	public void setUoa(String uoa) {
		this.uoa = uoa;
	}
	
	public String getKcontrato() {
		return kcontrato;
	}

	public void setKcontrato(String kcontrato) {
		this.kcontrato = kcontrato;
	}

	public java.math.BigDecimal getRaumic() {
		return raumic;
	}

	public void setRaumic(java.math.BigDecimal raumic) {
		this.raumic = raumic;
	}
	
	public java.math.BigDecimal getCsmumic() {
		return csmumic;
	}

	public void setCsmumic(java.math.BigDecimal csmumic) {
		this.csmumic = csmumic;
	}
	
	public java.math.BigDecimal getPatronCSM() {
		return patronCSM;
	}

	public void setPatronCSM(java.math.BigDecimal patronCSM) {
		this.patronCSM = patronCSM;
	}						 
	public java.math.BigDecimal getRosspCSM() {
		return rosspCSM;
	}

	public void setRosspCSM(java.math.BigDecimal rosspCSM) {
		this.rosspCSM = rosspCSM;
	}		

	public BloqueCorriente getBloqueGtoAd() {
		return bloqueGtoAd;
	}

	public void setBloqueGtoAd(BloqueCorriente bloqueGtoAd) {
		this.bloqueGtoAd = bloqueGtoAd;
	}
	
	public Boolean getOga() {
		return oga;
	}

	public void setOga(Boolean oga) {
		this.oga = oga;
	}

	/**
	 * Devuelve el bloque de trabajo a partir del código de subproceso
	 * @param subproceso
	 * @return el BloqueCorriente asociado al subproceso
	 */
	public BloqueCorriente getBloqueBySubproceso(String subproceso) { //NOSONAR
		BloqueCorriente bloque = null;
		if (subproceso == null) {
			return null;
		}
		switch (subproceso.toUpperCase()) {
			// Utilizar constantes
		case ConstantesSolvencia.CTE_PROY_VIDA:
			if (bloqueVida == null) {
				bloqueVida = new BloqueCorriente();
			}
			bloque = bloqueVida;
			break;
		case ConstantesSolvencia.CTE_PROY_FALL:
			if (bloqueFall == null) {
				bloqueFall = new BloqueCorriente();
			}
			bloque = bloqueFall;
			break;
		case ConstantesSolvencia.CTE_PROY_COMP:
			if (bloqueCompl == null) {
				bloqueCompl = new BloqueCorriente();
			}
			bloque = bloqueCompl;
			break;
		case ConstantesSolvencia.CTE_PROY_INVA:
			if (bloqueCompl == null) {
				bloqueCompl = new BloqueCorriente();
			}
			bloque = bloqueCompl;
			break;
		case ConstantesSolvencia.CTE_PROY_GTOS:
			if (bloqueGto == null) {
				bloqueGto = new BloqueCorriente();
			}
			bloque = bloqueGto;
			break;
		case ConstantesSolvencia.CTE_PROY_COMI:
			if (bloqueComi == null) {
				bloqueComi = new BloqueCorriente();
			}
			bloque = bloqueComi;
			break;
		case ConstantesSolvencia.CTE_PROY_RESC:
			if (bloqueRte == null) {
				bloqueRte = new BloqueCorriente();
			}
			bloque = bloqueRte;
			break;
		case ConstantesSolvencia.CTE_PROY_PRIMA:
			if (bloquePrim == null) {
				bloquePrim = new BloqueCorriente();
			}
			bloque = bloquePrim;
			break;
		case ConstantesSolvencia.CTE_PROY_GTOAD:
			if (bloqueGtoAd == null) {
				bloqueGtoAd = new BloqueCorriente();
			}
			bloque = bloqueGtoAd;
			break;
		default:
			break;
		}
		
		return bloque;
	}
	
	@Transient
	public CabeceraCorriente getCabeceraCorriente() {
		if (this.cabeceraCorriente == null) {
			this.cabeceraCorriente = new CabeceraCorriente();
		}
		
		this.cabeceraCorriente.setBt(bt);
		this.cabeceraCorriente.setCcanal(ccanal);
		this.cabeceraCorriente.setCcartera(ccartera);
		this.cabeceraCorriente.setCnegocio(cnegocio);
		this.cabeceraCorriente.setCtipoaport(ctipoaport);
		this.cabeceraCorriente.setFcierre(fcierre);
		this.cabeceraCorriente.setFechaDesde(fechaDesde);
		this.cabeceraCorriente.setFechaHasta(fechaHasta);
		this.cabeceraCorriente.setKajuste(kajuste);
		this.cabeceraCorriente.setKcertificado(kcertificado);
		this.cabeceraCorriente.setKgarantia(kgarantia);
		this.cabeceraCorriente.setKmodalidad(kmodalidad);
		this.cabeceraCorriente.setKpoliza(kpoliza);
		this.cabeceraCorriente.setKprestacion(kprestacion);
		this.cabeceraCorriente.setKsubpoliza(ksubpoliza);
		this.cabeceraCorriente.setNorden(norden);
		this.cabeceraCorriente.setNsuscri(nsuscri);
		this.cabeceraCorriente.setImpPago(impPago);
		
		return this.cabeceraCorriente;
	}
	
	@Override
	public DetalleCorrienteKey getKey() {
		
		return new DetalleCorrienteKey(cnegocio, ccanal, fcierre, bt, kmodalidad, kpoliza, ksubpoliza, kcertificado
				                      , nsuscri, norden, kgarantia, kprestacion, kajuste, ctipoaport, fechaDesde
				                      , fechaHasta);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bloqueComi == null) ? 0 : bloqueComi.hashCode());
		result = prime * result + ((bloqueCompl == null) ? 0 : bloqueCompl.hashCode());
		result = prime * result + ((bloqueFall == null) ? 0 : bloqueFall.hashCode());
		result = prime * result + ((bloqueGto == null) ? 0 : bloqueGto.hashCode());
		result = prime * result + ((bloqueGtoAd == null) ? 0 : bloqueGtoAd.hashCode());
		result = prime * result + ((bloquePrim == null) ? 0 : bloquePrim.hashCode());
		result = prime * result + ((bloqueRte == null) ? 0 : bloqueRte.hashCode());
		result = prime * result + ((bloqueVida == null) ? 0 : bloqueVida.hashCode());
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((ccartera == null) ? 0 : ccartera.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((csmumic == null) ? 0 : csmumic.hashCode());
		result = prime * result + ((ctipoaport == null) ? 0 : ctipoaport.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((fechaDesde == null) ? 0 : fechaDesde.hashCode());
		result = prime * result + ((fechaHasta == null) ? 0 : fechaHasta.hashCode());
		result = prime * result + ((fsuscri == null) ? 0 : fsuscri.hashCode());
		result = prime * result + ((gapAct == null) ? 0 : gapAct.hashCode());
		result = prime * result + ((impPago == null) ? 0 : impPago.hashCode());
		result = prime * result + ((intfeccal == null) ? 0 : intfeccal.hashCode());
		result = prime * result + ((kajuste == null) ? 0 : kajuste.hashCode());
		result = prime * result + ((kbencon == null) ? 0 : kbencon.hashCode());
		result = prime * result + ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result + ((kcertificado == null) ? 0 : kcertificado.hashCode());
		result = prime * result + ((kcontrato == null) ? 0 : kcontrato.hashCode());
		result = prime * result + ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kmodext == null) ? 0 : kmodext.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((kprestacion == null) ? 0 : kprestacion.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((norden == null) ? 0 : norden.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
		result = prime * result + ((oga == null) ? 0 : oga.hashCode());
		result = prime * result + ((patronCSM == null) ? 0 : patronCSM.hashCode());
		result = prime * result + ((provBelCSM == null) ? 0 : provBelCSM.hashCode());
		result = prime * result + ((provRosspCSM == null) ? 0 : provRosspCSM.hashCode());
		result = prime * result + ((raumic == null) ? 0 : raumic.hashCode());
		result = prime * result + ((rosspCSM == null) ? 0 : rosspCSM.hashCode());
		result = prime * result + ((spcom == null) ? 0 : spcom.hashCode());
		result = prime * result + ((sw_periodo_casado == null) ? 0 : sw_periodo_casado.hashCode());
		result = prime * result + ((totalFlujoProyeccion == null) ? 0 : totalFlujoProyeccion.hashCode());
		result = prime * result + ((uoa == null) ? 0 : uoa.hashCode());
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
		DetalleCorriente other = (DetalleCorriente) obj;
		if (bloqueComi == null) {
			if (other.bloqueComi != null)
				return false;
		} else if (!bloqueComi.equals(other.bloqueComi))
			return false;
		if (bloqueCompl == null) {
			if (other.bloqueCompl != null)
				return false;
		} else if (!bloqueCompl.equals(other.bloqueCompl))
			return false;
		if (bloqueFall == null) {
			if (other.bloqueFall != null)
				return false;
		} else if (!bloqueFall.equals(other.bloqueFall))
			return false;
		if (bloqueGto == null) {
			if (other.bloqueGto != null)
				return false;
		} else if (!bloqueGto.equals(other.bloqueGto))
			return false;
		if (bloqueGtoAd == null) {
			if (other.bloqueGtoAd != null)
				return false;
		} else if (!bloqueGtoAd.equals(other.bloqueGtoAd))
			return false;
		if (bloquePrim == null) {
			if (other.bloquePrim != null)
				return false;
		} else if (!bloquePrim.equals(other.bloquePrim))
			return false;
		if (bloqueRte == null) {
			if (other.bloqueRte != null)
				return false;
		} else if (!bloqueRte.equals(other.bloqueRte))
			return false;
		if (bloqueVida == null) {
			if (other.bloqueVida != null)
				return false;
		} else if (!bloqueVida.equals(other.bloqueVida))
			return false;
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
		if (csmumic == null) {
			if (other.csmumic != null)
				return false;
		} else if (!csmumic.equals(other.csmumic))
			return false;
		if (ctipoaport == null) {
			if (other.ctipoaport != null)
				return false;
		} else if (!ctipoaport.equals(other.ctipoaport))
			return false;
		if (fcierre == null) {
			if (other.fcierre != null)
				return false;
		} else if (!fcierre.equals(other.fcierre))
			return false;
		if (fechaDesde == null) {
			if (other.fechaDesde != null)
				return false;
		} else if (!fechaDesde.equals(other.fechaDesde))
			return false;
		if (fechaHasta == null) {
			if (other.fechaHasta != null)
				return false;
		} else if (!fechaHasta.equals(other.fechaHasta))
			return false;
		if (fsuscri == null) {
			if (other.fsuscri != null)
				return false;
		} else if (!fsuscri.equals(other.fsuscri))
			return false;
		if (gapAct == null) {
			if (other.gapAct != null)
				return false;
		} else if (!gapAct.equals(other.gapAct))
			return false;
		if (impPago == null) {
			if (other.impPago != null)
				return false;
		} else if (!impPago.equals(other.impPago))
			return false;
		if (intfeccal == null) {
			if (other.intfeccal != null)
				return false;
		} else if (!intfeccal.equals(other.intfeccal))
			return false;
		if (kajuste == null) {
			if (other.kajuste != null)
				return false;
		} else if (!kajuste.equals(other.kajuste))
			return false;
		if (kbencon == null) {
			if (other.kbencon != null)
				return false;
		} else if (!kbencon.equals(other.kbencon))
			return false;
		if (kcarterainv == null) {
			if (other.kcarterainv != null)
				return false;
		} else if (!kcarterainv.equals(other.kcarterainv))
			return false;
		if (kcertificado == null) {
			if (other.kcertificado != null)
				return false;
		} else if (!kcertificado.equals(other.kcertificado))
			return false;
		if (kcontrato == null) {
			if (other.kcontrato != null)
				return false;
		} else if (!kcontrato.equals(other.kcontrato))
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
		if (kmodext == null) {
			if (other.kmodext != null)
				return false;
		} else if (!kmodext.equals(other.kmodext))
			return false;
		if (kpoliza == null) {
			if (other.kpoliza != null)
				return false;
		} else if (!kpoliza.equals(other.kpoliza))
			return false;
		if (kprestacion == null) {
			if (other.kprestacion != null)
				return false;
		} else if (!kprestacion.equals(other.kprestacion))
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
		if (norden == null) {
			if (other.norden != null)
				return false;
		} else if (!norden.equals(other.norden))
			return false;
		if (nsuscri == null) {
			if (other.nsuscri != null)
				return false;
		} else if (!nsuscri.equals(other.nsuscri))
			return false;
		if (oga == null) {
			if (other.oga != null)
				return false;
		} else if (!oga.equals(other.oga))
			return false;
		if (patronCSM == null) {
			if (other.patronCSM != null)
				return false;
		} else if (!patronCSM.equals(other.patronCSM))
			return false;
		if (provBelCSM == null) {
			if (other.provBelCSM != null)
				return false;
		} else if (!provBelCSM.equals(other.provBelCSM))
			return false;
		if (provRosspCSM == null) {
			if (other.provRosspCSM != null)
				return false;
		} else if (!provRosspCSM.equals(other.provRosspCSM))
			return false;
		if (raumic == null) {
			if (other.raumic != null)
				return false;
		} else if (!raumic.equals(other.raumic))
			return false;
		if (rosspCSM == null) {
			if (other.rosspCSM != null)
				return false;
		} else if (!rosspCSM.equals(other.rosspCSM))
			return false;
		if (spcom == null) {
			if (other.spcom != null)
				return false;
		} else if (!spcom.equals(other.spcom))
			return false;
		if (sw_periodo_casado == null) {
			if (other.sw_periodo_casado != null)
				return false;
		} else if (!sw_periodo_casado.equals(other.sw_periodo_casado))
			return false;
		if (totalFlujoProyeccion == null) {
			if (other.totalFlujoProyeccion != null)
				return false;
		} else if (!totalFlujoProyeccion.equals(other.totalFlujoProyeccion))
			return false;
		if (uoa == null) {
			if (other.uoa != null)
				return false;
		} else if (!uoa.equals(other.uoa))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DetalleCorriente [cnegocio=" + cnegocio + ", ccanal=" + ccanal + ", ccartera=" + ccartera + ", fcierre="
				+ fcierre + ", bt=" + bt + ", kmodalidad=" + kmodalidad + ", kpoliza=" + kpoliza + ", ksubpoliza="
				+ ksubpoliza + ", kcertificado=" + kcertificado + ", nsuscri=" + nsuscri + ", norden=" + norden
				+ ", kgarantia=" + kgarantia + ", kprestacion=" + kprestacion + ", kajuste=" + kajuste + ", ctipoaport="
				+ ctipoaport + ", fechaDesde=" + fechaDesde + ", fechaHasta=" + fechaHasta + ", bloqueVida="
				+ bloqueVida + ", bloqueFall=" + bloqueFall + ", bloqueCompl=" + bloqueCompl + ", bloqueGto="
				+ bloqueGto + ", bloqueComi=" + bloqueComi + ", bloqueRte=" + bloqueRte + ", bloquePrim=" + bloquePrim
				+ ", totalFlujoProyeccion=" + totalFlujoProyeccion + ", impPago=" + impPago + ", kramo=" + kramo
				+ ", fsuscri=" + fsuscri + ", kcarterainv=" + kcarterainv + ", gapAct=" + gapAct + ", intfeccal="
				+ intfeccal + ", spcom=" + spcom + ", kmodext=" + kmodext + ", kbencon=" + kbencon
				+ ", sw_periodo_casado=" + sw_periodo_casado + ", uoa=" + uoa + ", kcontrato=" + kcontrato + ", raumic="
				+ raumic + ", csmumic=" + csmumic + ", patronCSM=" + patronCSM + ", rosspCSM=" + rosspCSM
				+ ", bloqueGtoAd=" + bloqueGtoAd + ", provRosspCSM=" + provRosspCSM + ", provBelCSM=" + provBelCSM
				+ ", oga=" + oga + "]";
	}

	
	
	

	
}