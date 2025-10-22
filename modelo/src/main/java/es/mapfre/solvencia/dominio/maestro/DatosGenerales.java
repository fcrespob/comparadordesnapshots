package es.mapfre.solvencia.dominio.maestro;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class DatosGenerales implements EntidadBase<UmicKey>{
	
	public static final int IND_CCANAL = 0;
	public static final int IND_CNEGOCIO = 1;
	public static final int IND_EKMODALIDAD = 2;
	
	public static final int IND_CCARTERA = 3;
	public static final int IND_CSITUPOL = 4;
	public static final int IND_CTIPOAPORT = 5;
	public static final int IND_CTIPOPROVI = 6;
	public static final int IND_CTIPRAMO = 7;
	public static final int IND_EDIFER = 8;
	public static final int IND_FECCIERRE = 9;
	public static final int IND_GAPACT = 10;
	public static final int IND_INDICPENSION = 11;
	public static final int IND_EKAJUSTE = 12;
	public static final int IND_KBENCON = 13;
	public static final int IND_KCARTERAINV = 14;
	public static final int IND_KCATEGORIA = 15;
	public static final int IND_KCERTIFICADO = 16;
	public static final int IND_EKGARANTIA = 17;
	
	public static final int IND_EKMODEXT = 18;
	public static final int IND_KOFICINA = 19;
	public static final int IND_KOFICONT = 20;
	public static final int IND_KPOLIZA = 21;
	public static final int IND_KPRESTACION = 22;
	public static final int IND_KRAMO = 23;
	public static final int IND_EKSUBPOLIZA = 24;
	public static final int IND_ENASEGURADOS = 25;
	public static final int IND_ENORDEN = 26;
	public static final int IND_ENSUSCRI = 27;
	public static final int IND_ANNUEVAPRODUC = 28;
	public static final int IND_PB = 29;
	public static final int IND_REGLAMENTO = 30;
	public static final int IND_SEGMENTO1 = 31;
	public static final int IND_SPCOM = 32;
	public static final int IND_SWCASADO = 33;
	public static final int IND_SWINNOMINADA = 34;
	public static final int IND_TARIFA = 35;
	public static final int IND_TIPOFLEXPRIM = 36;
	public static final int IND_TIPOFLEXSEG = 37;
	public static final int IND_TIPOPB = 38;
	public static final int IND_TIPOSUBRIESGO = 39;
	public static final int IND_GESTIONIT = 40;
	public static final int IND_INDINVAL = 41;
	public static final int IND_PREGRUPO = 42;
	
	@PortableProperty(IND_CCANAL) private Integer ccanal;
	@PortableProperty(IND_CCARTERA) private Integer ccartera;
	@PortableProperty(IND_CNEGOCIO) private String cnegocio;
	@PortableProperty(IND_CSITUPOL) private String csitupol;
	@PortableProperty(IND_CTIPOAPORT) private String ctipoaport;
	@PortableProperty(IND_CTIPOPROVI) private String ctipoprovi;
	@PortableProperty(IND_CTIPRAMO) private String ctipramo;
	@PortableProperty(IND_EDIFER) private Integer edifer;
	@PortableProperty(IND_FECCIERRE) private Timestamp fecCierre;
	@PortableProperty(IND_GAPACT) private String gapAct;
	@PortableProperty(IND_INDICPENSION) private String indicpension;
	@PortableProperty(IND_EKAJUSTE) private Integer kajuste;
	@PortableProperty(IND_KBENCON) private String kbencon;
	@PortableProperty(IND_KCARTERAINV) private String kcarterainv;
	@PortableProperty(IND_KCATEGORIA) private String kcategoria;
	@PortableProperty(IND_KCERTIFICADO) private Integer kcertificado;
	@PortableProperty(IND_EKGARANTIA) private Integer kgarantia;
	@PortableProperty(IND_EKMODALIDAD) private Integer kmodalidad;
	@PortableProperty(IND_EKMODEXT) private Integer kmodext;
	@PortableProperty(IND_KOFICINA) private String koficina;
	@PortableProperty(IND_KOFICONT) private String koficont;
	@PortableProperty(IND_KPOLIZA) private Long kpoliza;
	@PortableProperty(IND_KPRESTACION) private String kprestacion;
	@PortableProperty(IND_KRAMO) private String kramo;
	@PortableProperty(IND_EKSUBPOLIZA) private Integer ksubpoliza;
	@PortableProperty(IND_ENASEGURADOS) private Integer nasegurados;
	@PortableProperty(IND_ENORDEN) private Integer norden;
	@PortableProperty(IND_ENSUSCRI) private Integer nsuscri;
	@PortableProperty(IND_ANNUEVAPRODUC) private Boolean nuevaProduc;
	@PortableProperty(IND_PB) private String pb;
	@PortableProperty(IND_REGLAMENTO) private String reglamento;
	@PortableProperty(IND_SEGMENTO1) private String segmento1;
	@PortableProperty(IND_SPCOM) private String spcom;
	@PortableProperty(IND_SWCASADO) private String swcasado;
	@PortableProperty(IND_SWINNOMINADA) private String swinnominada;
	@PortableProperty(IND_TARIFA) private String tarifa;
	@PortableProperty(IND_TIPOFLEXPRIM) private String tipoflexprim;
	@PortableProperty(IND_TIPOFLEXSEG) private String tipoflexseg;
	@PortableProperty(IND_TIPOPB) private String tipoPb;
	@PortableProperty(IND_TIPOSUBRIESGO) private String tipoSubriesgo;
	@PortableProperty(IND_GESTIONIT) private String gestionit;
	@PortableProperty(IND_INDINVAL)private String indinval;
	@PortableProperty(IND_PREGRUPO)private String pregrupo;
	
	private UmicKey idUmic;

	
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
	public String getCnegocio() {
		return cnegocio;
	}
	public void setCnegocio(String cnegocio) {
		this.cnegocio = cnegocio;
	}
	public String getCsitupol() {
		return csitupol;
	}
	public void setCsitupol(String csitupol) {
		this.csitupol = csitupol;
	}
	public String getCtipoaport() {
		return ctipoaport;
	}
	public void setCtipoaport(String ctipoaport) {
		this.ctipoaport = ctipoaport;
	}
	public String getCtipoprovi() {
		return ctipoprovi;
	}
	public void setCtipoprovi(String ctipoprovi) {
		this.ctipoprovi = ctipoprovi;
	}
	public String getCtipramo() {
		return ctipramo;
	}
	public void setCtipramo(String ctipramo) {
		this.ctipramo = ctipramo;
	}
	public Integer getEdifer() {
		return edifer;
	}
	public void setEdifer(Integer edifer) {
		this.edifer = edifer;
	}
	public Timestamp getFecCierre() {
		return fecCierre;
	}
	public void setFecCierre(Timestamp fecCierre) {
		this.fecCierre = fecCierre;
	}
	public String getGapAct() {
		return gapAct;
	}
	public void setGapAct(String gapAct) {
		this.gapAct = gapAct;
	}
	public String getIndicpension() {
		return indicpension;
	}
	public void setIndicpension(String indicpension) {
		this.indicpension = indicpension;
	}
	public Integer getKajuste() {
		return kajuste;
	}
	public void setKajuste(Integer kajuste) {
		this.kajuste = kajuste;
	}
	public String getKbencon() {
		return kbencon;
	}
	public void setKbencon(String kbencon) {
		this.kbencon = kbencon;
	}
	public String getKcarterainv() {
		return kcarterainv;
	}
	public void setKcarterainv(String kcarterainv) {
		this.kcarterainv = kcarterainv;
	}
	public String getKcategoria() {
		return kcategoria;
	}
	public void setKcategoria(String kcategoria) {
		this.kcategoria = kcategoria;
	}
	public Integer getKcertificado() {
		return kcertificado;
	}
	public void setKcertificado(Integer kcertificado) {
		this.kcertificado = kcertificado;
	}
	public Integer getKgarantia() {
		return kgarantia;
	}
	public void setKgarantia(Integer kgarantia) {
		this.kgarantia = kgarantia;
	}
	public Integer getKmodalidad() {
		return kmodalidad;
	}
	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}
	public Integer getKmodext() {
		return kmodext;
	}
	public void setKmodext(Integer kmodext) {
		this.kmodext = kmodext;
	}
	public String getKoficina() {
		return koficina;
	}
	public void setKoficina(String koficina) {
		this.koficina = koficina;
	}
	public String getKoficont() {
		return koficont;
	}
	public void setKoficont(String koficont) {
		this.koficont = koficont;
	}
	public Long getKpoliza() {
		return kpoliza;
	}
	public void setKpoliza(Long kpoliza) {
		this.kpoliza = kpoliza;
	}
	public String getKprestacion() {
		return kprestacion;
	}
	public void setKprestacion(String kprestacion) {
		this.kprestacion = kprestacion;
	}
	public String getKramo() {
		return kramo;
	}
	public void setKramo(String kramo) {
		this.kramo = kramo;
	}
	public Integer getKsubpoliza() {
		return ksubpoliza;
	}
	public void setKsubpoliza(Integer ksubpoliza) {
		this.ksubpoliza = ksubpoliza;
	}
	public Integer getNasegurados() {
		return nasegurados;
	}
	public void setNasegurados(Integer nasegurados) {
		this.nasegurados = nasegurados;
	}
	public Integer getNorden() {
		return norden;
	}
	public void setNorden(Integer norden) {
		this.norden = norden;
	}
	public Integer getNsuscri() {
		return nsuscri;
	}
	public void setNsuscri(Integer nsuscri) {
		this.nsuscri = nsuscri;
	}
	public Boolean getNuevaProduc() {
		return nuevaProduc;
	}
	public void setNuevaProduc(Boolean nuevaProduc) {
		this.nuevaProduc = nuevaProduc;
	}
	public String getPb() {
		return pb;
	}
	public void setPb(String pb) {
		this.pb = pb;
	}
	public String getReglamento() {
		return reglamento;
	}
	public void setReglamento(String reglamento) {
		this.reglamento = reglamento;
	}
	public String getSegmento1() {
		return segmento1;
	}
	public void setSegmento1(String segmento1) {
		this.segmento1 = segmento1;
	}
	public String getSpcom() {
		return spcom;
	}
	public void setSpcom(String spcom) {
		this.spcom = spcom;
	}
	public String getSwcasado() {
		return swcasado;
	}
	public void setSwcasado(String swcasado) {
		this.swcasado = swcasado;
	}
	public String getSwinnominada() {
		return swinnominada;
	}
	public void setSwinnominada(String swinnominada) {
		this.swinnominada = swinnominada;
	}
	public String getTarifa() {
		return tarifa;
	}
	public void setTarifa(String tarifa) {
		this.tarifa = tarifa;
	}
	public String getTipoflexprim() {
		return tipoflexprim;
	}
	public void setTipoflexprim(String tipoflexprim) {
		this.tipoflexprim = tipoflexprim;
	}
	public String getTipoflexseg() {
		return tipoflexseg;
	}
	public void setTipoflexseg(String tipoflexseg) {
		this.tipoflexseg = tipoflexseg;
	}
	public String getTipoPb() {
		return tipoPb;
	}
	public void setTipoPb(String tipoPb) {
		this.tipoPb = tipoPb;
	}
	public String getTipoSubriesgo() {
		return tipoSubriesgo;
	}
	public void setTipoSubriesgo(String tipoSubriesgo) {
		this.tipoSubriesgo = tipoSubriesgo;
	}
	public String getGestionit() {
		return gestionit;
	}
	public void setGestionit(String gestionit) {
		this.gestionit = gestionit;
	}
	public String getIndinval() {
		return indinval;
	}
	public void setIndinval(String indinval) {
		this.indinval = indinval;
	}
	public String getPregrupo() {
		return pregrupo;
	}
	public void setPregrupo(String pregrupo) {
		this.pregrupo = pregrupo;
	}
	
	@Override
	public UmicKey getKey() {
		if(idUmic == null){
			idUmic = new UmicKey(this.getCtipoaport(), this.getKajuste(), this.getKcertificado()
					             , this.getKgarantia(), this.getKmodalidad(), this.getKpoliza()
					             , this.getKprestacion(), this.getKsubpoliza(), this.getNorden()
					             , this.getNsuscri());
		}
		
		return idUmic;
	}
}
