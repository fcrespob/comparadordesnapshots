package es.mapfre.solvencia.dominio.maestro;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class Rentas implements EntidadBase<UmicKey> {

	public static final int IND_CFORMAREVRENTA = 0;
	public static final int IND_CLAVEUMIC = 1;
	public static final int IND_CPAGRENTA = 2;
	public static final int IND_CTIPOREVRENTA = 3;
	public static final int IND_FECFIN = 4;
	public static final int IND_FECINI = 5;
	public static final int IND_FORPAGRENT = 6;
	public static final int IND_NADIFER = 7;
	public static final int IND_NDURRENTA = 8;
	public static final int IND_NPERGARAN = 9;

	public static final int IND_PREVRENTA = 10;
	public static final int IND_PREVERSION = 11;
	public static final int IND_RENTACT = 12;
	public static final int IND_RENTINI = 13;
	public static final int IND_RENTMINI = 14;
	public static final int IND_TEMPVIT = 15;

	@PortableProperty(IND_CFORMAREVRENTA)
	private String cformaRevrenta;
	@PortableProperty(IND_CLAVEUMIC)
	private String claveUmic;
	@PortableProperty(IND_CPAGRENTA)
	private String cpagrenta;
	@PortableProperty(IND_CTIPOREVRENTA)
	private String ctipoRevrenta;
	@PortableProperty(IND_FECFIN)
	private Timestamp fecFin;
	@PortableProperty(IND_FECINI)
	private Timestamp fecIni;
	@PortableProperty(IND_FORPAGRENT)
	private Integer forpagrent;
	@PortableProperty(IND_NADIFER)
	private Integer nadifer;
	@PortableProperty(IND_NDURRENTA)
	private Integer ndurrenta;
	@PortableProperty(IND_NPERGARAN)
	private Integer npergaran;

	@PortableProperty(IND_PREVRENTA)
	private java.math.BigDecimal prevrenta;
	@PortableProperty(IND_PREVERSION)
	private java.math.BigDecimal preversion;
	@PortableProperty(IND_RENTACT)
	private java.math.BigDecimal rentact;
	@PortableProperty(IND_RENTINI)
	private java.math.BigDecimal rentini;
	@PortableProperty(IND_RENTMINI)
	private java.math.BigDecimal rentmini;
	@PortableProperty(IND_TEMPVIT)
	private String tempVit;

	public String getCformaRevrenta() {
		return cformaRevrenta;
	}

	public void setCformaRevrenta(String cformaRevrenta) {
		this.cformaRevrenta = cformaRevrenta;
	}

	public String getClaveUmic() {
		return claveUmic;
	}

	public void setClaveUmic(String claveUmic) {
		this.claveUmic = claveUmic;
	}

	public String getCpagrenta() {
		return cpagrenta;
	}

	public void setCpagrenta(String cpagrenta) {
		this.cpagrenta = cpagrenta;
	}

	public String getCtipoRevrenta() {
		return ctipoRevrenta;
	}

	public void setCtipoRevrenta(String ctipoRevrenta) {
		this.ctipoRevrenta = ctipoRevrenta;
	}

	public Timestamp getFecFin() {
		return fecFin;
	}

	public void setFecFin(Timestamp fecFin) {
		this.fecFin = fecFin;
	}

	public Timestamp getFecIni() {
		return fecIni;
	}

	public void setFecIni(Timestamp fecIni) {
		this.fecIni = fecIni;
	}

	public Integer getForpagrent() {
		return forpagrent;
	}

	public void setForpagrent(Integer forpagrent) {
		this.forpagrent = forpagrent;
	}

	public Integer getNadifer() {
		return nadifer;
	}

	public void setNadifer(Integer nadifer) {
		this.nadifer = nadifer;
	}

	public Integer getNdurrenta() {
		return ndurrenta;
	}

	public void setNdurrenta(Integer ndurrenta) {
		this.ndurrenta = ndurrenta;
	}

	public Integer getNpergaran() {
		return npergaran;
	}

	public void setNpergaran(Integer npergaran) {
		this.npergaran = npergaran;
	}

	public java.math.BigDecimal getPrevrenta() {
		return prevrenta;
	}

	public void setPrevrenta(java.math.BigDecimal prevrenta) {
		this.prevrenta = prevrenta;
	}

	public java.math.BigDecimal getRentact() {
		return rentact;
	}

	public void setRentact(java.math.BigDecimal rentact) {
		this.rentact = rentact;
	}

	public java.math.BigDecimal getRentini() {
		return rentini;
	}

	public void setRentini(java.math.BigDecimal rentini) {
		this.rentini = rentini;
	}

	public java.math.BigDecimal getRentmini() {
		return rentmini;
	}

	public void setRentmini(java.math.BigDecimal rentmini) {
		this.rentmini = rentmini;
	}

	public String getTempVit() {
		return tempVit;
	}

	public void setTempVit(String tempVit) {
		this.tempVit = tempVit;
	}

	public java.math.BigDecimal getPreversion() {
		return preversion;
	}

	public void setPreversion(java.math.BigDecimal preversion) {
		this.preversion = preversion;
	}

	@Override
	public UmicKey getKey() {
		// No existe la posibilidad de crear una UmicKey a partir de los datos
		return null;
	}

}
