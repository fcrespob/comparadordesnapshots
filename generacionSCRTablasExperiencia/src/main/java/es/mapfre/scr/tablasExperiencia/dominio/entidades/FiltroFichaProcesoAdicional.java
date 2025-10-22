package es.mapfre.scr.tablasExperiencia.dominio.entidades;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class FiltroFichaProcesoAdicional {

	public static final int IND_KSECFIL = 0;
	public static final int IND_CTIPOFILTRO = 1;
	public static final int IND_GCLASEFIL = 2;
	public static final int IND_GOPERDESDE = 3;
	public static final int IND_VALORDESDE = 4;
	public static final int IND_GOPERHASTA = 5;
	public static final int IND_VALHASTA = 6;
	public static final int IND_GOPDESDEGAR = 7;
	public static final int IND_VALDESDEGAR = 8;
	public static final int IND_GOPHASTAGAR = 9;
	public static final int IND_VALHASTAGAR = 10;
	public static final int IND_SMANUAL = 11;
	public static final int IND_CPOLIZA = 12;
	public static final int IND_NSUBPOLIZA = 13;
	public static final int IND_SCASADOS = 14;
	public static final int IND_CSEGMENTO = 15;
	public static final int IND_CRIESGOACT = 16;
	public static final int IND_GTIPINTERES = 17;
	public static final int IND_FGRABACION = 18;
	public static final int IND_CUSUARIOG = 19;
	public static final int IND_FMODIFICA = 20;
	public static final int IND_CUSUARIOM = 21;

	private Integer ksecfil;
	private String ctipofiltro;
	private String gclasefil;
	private String goperdesde;
	private String valordesde;
	private String goperhasta;
	private String valhasta;
	private String gopdesdegar;
	private String valdesdegar;
	private String gophastagar;
	private String valhastagar;
	private String smanual;
	private Long cpoliza;
	private String nsubpoliza;
	private String scasados;
	private String csegmento;
	private String criesgoact;
	private BigDecimal gtipinteres;
	private Timestamp fgrabacion;
	private String cusuariog;
	private Timestamp fmodifica;
	private String cusuariom;

	public Integer getKsecfil() {
		return ksecfil;
	}

	public void setKsecfil(Integer ksecfil) {
		this.ksecfil = ksecfil;
	}

	public String getCtipofiltro() {
		return ctipofiltro;
	}

	public void setCtipofiltro(String ctipofiltro) {
		this.ctipofiltro = ctipofiltro;
	}

	public String getGclasefil() {
		return gclasefil;
	}

	public void setGclasefil(String gclasefil) {
		this.gclasefil = gclasefil;
	}

	public String getGoperdesde() {
		return goperdesde;
	}

	public void setGoperdesde(String goperdesde) {
		this.goperdesde = goperdesde;
	}

	public String getValordesde() {
		return valordesde;
	}

	public void setValordesde(String valordesde) {
		this.valordesde = valordesde;
	}

	public String getGoperhasta() {
		return goperhasta;
	}

	public void setGoperhasta(String goperhasta) {
		this.goperhasta = goperhasta;
	}

	public String getValhasta() {
		return valhasta;
	}

	public void setValhasta(String valhasta) {
		this.valhasta = valhasta;
	}

	public String getGopdesdegar() {
		return gopdesdegar;
	}

	public void setGopdesdegar(String gopdesdegar) {
		this.gopdesdegar = gopdesdegar;
	}

	public String getValdesdegar() {
		return valdesdegar;
	}

	public void setValdesdegar(String valdesdegar) {
		this.valdesdegar = valdesdegar;
	}

	public String getGophastagar() {
		return gophastagar;
	}

	public void setGophastagar(String gophastagar) {
		this.gophastagar = gophastagar;
	}

	public String getValhastagar() {
		return valhastagar;
	}

	public void setValhastagar(String valhastagar) {
		this.valhastagar = valhastagar;
	}

	public String getSmanual() {
		return smanual;
	}

	public void setSmanual(String smanual) {
		this.smanual = smanual;
	}

	public Long getCpoliza() {
		return cpoliza;
	}

	public void setCpoliza(final Long cpoliza) {
		this.cpoliza = cpoliza;
	}

	public String getNsubpoliza() {
		return nsubpoliza;
	}

	public void setNsubpoliza(String nsubpoliza) {
		this.nsubpoliza = nsubpoliza;
	}

	public String getScasados() {
		return scasados;
	}

	public void setScasados(String scasados) {
		this.scasados = scasados;
	}

	public String getCsegmento() {
		return csegmento;
	}

	public void setCsegmento(String csegmento) {
		this.csegmento = csegmento;
	}

	public String getCriesgoact() {
		return criesgoact;
	}

	public void setCriesgoact(String criesgoact) {
		this.criesgoact = criesgoact;
	}

	public BigDecimal getGtipinteres() {
		return gtipinteres;
	}

	public void setGtipinteres(final BigDecimal gtipinteres) {
		this.gtipinteres = gtipinteres;
	}

	public Timestamp getFgrabacion() {
		return fgrabacion;
	}

	public void setFgrabacion(Timestamp fgrabacion) {
		this.fgrabacion = fgrabacion;
	}

	public String getCusuariog() {
		return cusuariog;
	}

	public void setCusuariog(String cusuariog) {
		this.cusuariog = cusuariog;
	}

	public Timestamp getFmodifica() {
		return fmodifica;
	}

	public void setFmodifica(Timestamp fmodifica) {
		this.fmodifica = fmodifica;
	}

	public String getCusuariom() {
		return cusuariom;
	}

	public void setCusuariom(String cusuariom) {
		this.cusuariom = cusuariom;
	}

	// public static final int IND_KSECAMBITO = 0;

	// @PortableProperty(IND_KSECAMBITO) private Integer ksecambito;
	//
	//
	// @Override //NOSONAR
	// public int hashCode() { //NOSONAR
	// final int prime = 31;
	// int result = 1;
	// result = prime * result
	// + ((cclaseamb == null) ? 0 : cclaseamb.hashCode());
	// result = prime * result
	// + ((coperadord == null) ? 0 : coperadord.hashCode());
	// result = prime * result
	// + ((coperadorh == null) ? 0 : coperadorh.hashCode());
	// result = prime * result
	// + ((cusuario == null) ? 0 : cusuario.hashCode());
	// result = prime * result
	// + ((fgrabacion == null) ? 0 : fgrabacion.hashCode());
	// result = prime * result
	// + ((gambitod == null) ? 0 : gambitod.hashCode());
	// result = prime * result
	// + ((gambitoh == null) ? 0 : gambitoh.hashCode());
	// result = prime * result
	// + ((ksecambito == null) ? 0 : ksecambito.hashCode());
	// result = prime * result
	// + ((ktipoamb == null) ? 0 : ktipoamb.hashCode());
	// result = prime * result + ((smanual == null) ? 0 : smanual.hashCode());
	// return result;
	// }
	// @Override //NOSONAR
	// public boolean equals(Object obj) { //NOSONAR
	// if (this == obj) {
	// return true;
	// }
	// if (obj == null) {
	// return false;
	// }
	// if (getClass() != obj.getClass()) {
	// return false;
	// }
	// FiltroFichaProceso other = (FiltroFichaProceso) obj;
	// if (cclaseamb == null) {
	// if (other.cclaseamb != null) {
	// return false;
	// }
	// } else if (!cclaseamb.equals(other.cclaseamb)) {
	// return false;
	// }
	// if (coperadord == null) {
	// if (other.coperadord != null) {
	// return false;
	// }
	// } else if (!coperadord.equals(other.coperadord)) {
	// return false;
	// }
	// if (coperadorh == null) {
	// if (other.coperadorh != null) {
	// return false;
	// }
	// } else if (!coperadorh.equals(other.coperadorh)) {
	// return false;
	// }
	// if (cusuario == null) {
	// if (other.cusuario != null) {
	// return false;
	// }
	// } else if (!cusuario.equals(other.cusuario)) {
	// return false;
	// }
	// if (fgrabacion == null) {
	// if (other.fgrabacion != null) {
	// return false;
	// }
	// } else if (!fgrabacion.equals(other.fgrabacion)) {
	// return false;
	// }
	// if (gambitod == null) {
	// if (other.gambitod != null) {
	// return false;
	// }
	// } else if (!gambitod.equals(other.gambitod)) {
	// return false;
	// }
	// if (gambitoh == null) {
	// if (other.gambitoh != null) {
	// return false;
	// }
	// } else if (!gambitoh.equals(other.gambitoh)) {
	// return false;
	// }
	// if (ksecambito == null) {
	// if (other.ksecambito != null) {
	// return false;
	// }
	// } else if (!ksecambito.equals(other.ksecambito)) {
	// return false;
	// }
	// if (ktipoamb == null) {
	// if (other.ktipoamb != null) {
	// return false;
	// }
	// } else if (!ktipoamb.equals(other.ktipoamb)) {
	// return false;
	// }
	// if (smanual == null) {
	// if (other.smanual != null) {
	// return false;
	// }
	// } else if (!smanual.equals(other.smanual)) {
	// return false;
	// }
	// return true;
	// }
	// @Override
	// public String toString() {
	// StringBuilder builder = new StringBuilder();
	// builder.append("FiltroFichaProceso [ksecambito=");
	// builder.append(ksecambito);
	// builder.append(", cclaseamb=");
	// builder.append(cclaseamb);
	// builder.append(", coperadord=");
	// builder.append(coperadord);
	// builder.append(", gambitod=");
	// builder.append(gambitod);
	// builder.append(", coperadorh=");
	// builder.append(coperadorh);
	// builder.append(", gambitoh=");
	// builder.append(gambitoh);
	// builder.append(", smanual=");
	// builder.append(smanual);
	// builder.append(", fgrabacion=");
	// builder.append(fgrabacion);
	// builder.append(", cusuario=");
	// builder.append(cusuario);
	// builder.append(", ktipoamb=");
	// builder.append(ktipoamb);
	// builder.append("]");
	// return builder.toString();
	// }
}
