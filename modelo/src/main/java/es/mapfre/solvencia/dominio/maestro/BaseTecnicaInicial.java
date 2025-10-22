package es.mapfre.solvencia.dominio.maestro;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class BaseTecnicaInicial implements EntidadBase<UmicKey> {

	public static final int IND_DURACIT = 0;
	public static final int IND_FECFINTRAMO1 = 1;
	public static final int IND_FECFINTRAMO2 = 2;
	public static final int IND_FECFINTRAMO3 = 3;
	public static final int IND_FECFINTRAMO4 = 4;
	public static final int IND_FECFINTRAMO5 = 5;
	public static final int IND_FECINITRAMO1 = 6;
	public static final int IND_FECINITRAMO2 = 7;
	public static final int IND_FECINITRAMO3 = 8;
	public static final int IND_FECINITRAMO4 = 9;

	public static final int IND_FECINITRAMO5 = 10;
	public static final int IND_GAPI1 = 11;
	public static final int IND_GAPI2 = 12;
	public static final int IND_GAPI3 = 13;
	public static final int IND_GAPI4 = 14;
	public static final int IND_GAPI5 = 15;
	public static final int IND_GGIM = 16;
	public static final int IND_IMPGASMAX = 17;
	public static final int IND_IMPGASMIN = 18;
	public static final int IND_PGASTGESEX1I = 19;
	public static final int IND_PGASTGESEX2I = 20;
	public static final int IND_PGASTGESIN1I = 21;
	public static final int IND_PGASTGESIN2I = 22;
	public static final int IND_PGASTGESIN3I = 23;
	public static final int IND_PGASTGESIN4I = 24;
	public static final int IND_PGASTGESIN5I = 25;
	public static final int IND_PINTERTECNI1 = 26;
	public static final int IND_PINTERTECNI2 = 27;
	public static final int IND_PINTERTECNI3 = 28;
	public static final int IND_PINTERTECNI4 = 29;
	public static final int IND_PINTERTECNI5 = 30;
	public static final int IND_PRIESGO = 31;
	public static final int IND_PSOBREMORT = 32;
	public static final int IND_RAGRAVADO = 33;
	public static final int IND_SWCASADOI1 = 34;
	public static final int IND_SWCASADOI2 = 35;
	public static final int IND_SWCASADOI3 = 36;
	public static final int IND_SWCASADOI4 = 37;
	public static final int IND_SWCASADOI5 = 38;
	public static final int IND_TABLA1ASEG1 = 39;
	public static final int IND_TABLA1ASEG2 = 40;
	public static final int IND_TABLA1ASEG3 = 41;
	public static final int IND_TABLA1ASEG4 = 42;
	public static final int IND_TABLA1ASEG5 = 43;
	public static final int IND_TABLA2ASEG1 = 44;
	public static final int IND_TABLA2ASEG2 = 45;
	public static final int IND_TABLA2ASEG3 = 46;
	public static final int IND_TABLA2ASEG4 = 47;
	public static final int IND_TABLA2ASEG5 = 48;
	public static final int IND_TABLA3ASEG1 = 49;
	public static final int IND_TABLA3ASEG2 = 50;
	public static final int IND_TABLA3ASEG3 = 51;
	public static final int IND_TABLA3ASEG4 = 52;
	public static final int IND_TABLA3ASEG5 = 53;

	@PortableProperty(IND_DURACIT)
	private Integer duracit;
	@PortableProperty(IND_FECFINTRAMO1)
	private Timestamp fecFinTramo1;
	@PortableProperty(IND_FECFINTRAMO2)
	private Timestamp fecFinTramo2;
	@PortableProperty(IND_FECFINTRAMO3)
	private Timestamp fecFinTramo3;
	@PortableProperty(IND_FECFINTRAMO4)
	private Timestamp fecFinTramo4;
	@PortableProperty(IND_FECFINTRAMO5)
	private Timestamp fecFinTramo5;
	@PortableProperty(IND_FECINITRAMO1)
	private Timestamp fecIniTramo1;
	@PortableProperty(IND_FECINITRAMO2)
	private Timestamp fecIniTramo2;
	@PortableProperty(IND_FECINITRAMO3)
	private Timestamp fecIniTramo3;
	@PortableProperty(IND_FECINITRAMO4)
	private Timestamp fecIniTramo4;

	@PortableProperty(IND_FECINITRAMO5)
	private Timestamp fecIniTramo5;
	@PortableProperty(IND_GAPI1)
	private String gapI1;
	@PortableProperty(IND_GAPI2)
	private String gapI2;
	@PortableProperty(IND_GAPI3)
	private String gapI3;
	@PortableProperty(IND_GAPI4)
	private String gapI4;
	@PortableProperty(IND_GAPI5)
	private String gapI5;
	@PortableProperty(IND_GGIM)
	private java.math.BigDecimal ggim;
	@PortableProperty(IND_IMPGASMAX)
	private java.math.BigDecimal impgasmax;
	@PortableProperty(IND_IMPGASMIN)
	private java.math.BigDecimal impgasmin;
	@PortableProperty(IND_PGASTGESEX1I)
	private java.math.BigDecimal pgastgesex1I;
	@PortableProperty(IND_PGASTGESEX2I)
	private java.math.BigDecimal pgastgesex2I;
	@PortableProperty(IND_PGASTGESIN1I)
	private java.math.BigDecimal pgastgesin1I;
	@PortableProperty(IND_PGASTGESIN2I)
	private java.math.BigDecimal pgastgesin2I;
	@PortableProperty(IND_PGASTGESIN3I)
	private java.math.BigDecimal pgastgesin3I;
	@PortableProperty(IND_PGASTGESIN4I)
	private java.math.BigDecimal pgastgesin4I;
	@PortableProperty(IND_PGASTGESIN5I)
	private java.math.BigDecimal pgastgesin5I;
	@PortableProperty(IND_PINTERTECNI1)
	private java.math.BigDecimal pintertecnI1;
	@PortableProperty(IND_PINTERTECNI2)
	private java.math.BigDecimal pintertecnI2;
	@PortableProperty(IND_PINTERTECNI3)
	private java.math.BigDecimal pintertecnI3;
	@PortableProperty(IND_PINTERTECNI4)
	private java.math.BigDecimal pintertecnI4;
	@PortableProperty(IND_PINTERTECNI5)
	private java.math.BigDecimal pintertecnI5;
	@PortableProperty(IND_PRIESGO)
	private java.math.BigDecimal priesgo;
	@PortableProperty(IND_PSOBREMORT)
	private java.math.BigDecimal psobremort;
	@PortableProperty(IND_RAGRAVADO)
	private String ragravado;
	@PortableProperty(IND_SWCASADOI1)
	private String swcasadoI1;
	@PortableProperty(IND_SWCASADOI2)
	private String swcasadoI2;
	@PortableProperty(IND_SWCASADOI3)
	private String swcasadoI3;
	@PortableProperty(IND_SWCASADOI4)
	private String swcasadoI4;
	@PortableProperty(IND_SWCASADOI5)
	private String swcasadoI5;
	@PortableProperty(IND_TABLA1ASEG1)
	private String tabla1Aseg1;
	@PortableProperty(IND_TABLA1ASEG2)
	private String tabla1Aseg2;
	@PortableProperty(IND_TABLA1ASEG3)
	private String tabla1Aseg3;
	@PortableProperty(IND_TABLA1ASEG4)
	private String tabla1Aseg4;
	@PortableProperty(IND_TABLA1ASEG5)
	private String tabla1Aseg5;
	@PortableProperty(IND_TABLA2ASEG1)
	private String tabla2Aseg1;
	@PortableProperty(IND_TABLA2ASEG2)
	private String tabla2Aseg2;
	@PortableProperty(IND_TABLA2ASEG3)
	private String tabla2Aseg3;
	@PortableProperty(IND_TABLA2ASEG4)
	private String tabla2Aseg4;
	@PortableProperty(IND_TABLA2ASEG5)
	private String tabla2Aseg5;
	@PortableProperty(IND_TABLA3ASEG1)
	private String tabla3Aseg1;
	@PortableProperty(IND_TABLA3ASEG2)
	private String tabla3Aseg2;
	@PortableProperty(IND_TABLA3ASEG3)
	private String tabla3Aseg3;
	@PortableProperty(IND_TABLA3ASEG4)
	private String tabla3Aseg4;
	@PortableProperty(IND_TABLA3ASEG5)
	private String tabla3Aseg5;

	public Integer getDuracit() {
		return duracit;
	}

	public void setDuracit(Integer duracit) {
		this.duracit = duracit;
	}

	public Timestamp getFecFinTramo1() {
		return fecFinTramo1;
	}

	public void setFecFinTramo1(Timestamp fecFinTramo1) {
		this.fecFinTramo1 = fecFinTramo1;
	}

	public Timestamp getFecFinTramo2() {
		return fecFinTramo2;
	}

	public void setFecFinTramo2(Timestamp fecFinTramo2) {
		this.fecFinTramo2 = fecFinTramo2;
	}

	public Timestamp getFecFinTramo3() {
		return fecFinTramo3;
	}

	public void setFecFinTramo3(Timestamp fecFinTramo3) {
		this.fecFinTramo3 = fecFinTramo3;
	}

	public Timestamp getFecFinTramo4() {
		return fecFinTramo4;
	}

	public void setFecFinTramo4(Timestamp fecFinTramo4) {
		this.fecFinTramo4 = fecFinTramo4;
	}

	public Timestamp getFecFinTramo5() {
		return fecFinTramo5;
	}

	public void setFecFinTramo5(Timestamp fecFinTramo5) {
		this.fecFinTramo5 = fecFinTramo5;
	}

	public Timestamp getFecIniTramo1() {
		return fecIniTramo1;
	}

	public void setFecIniTramo1(Timestamp fecIniTramo1) {
		this.fecIniTramo1 = fecIniTramo1;
	}

	public Timestamp getFecIniTramo2() {
		return fecIniTramo2;
	}

	public void setFecIniTramo2(Timestamp fecIniTramo2) {
		this.fecIniTramo2 = fecIniTramo2;
	}

	public Timestamp getFecIniTramo3() {
		return fecIniTramo3;
	}

	public void setFecIniTramo3(Timestamp fecIniTramo3) {
		this.fecIniTramo3 = fecIniTramo3;
	}

	public Timestamp getFecIniTramo4() {
		return fecIniTramo4;
	}

	public void setFecIniTramo4(Timestamp fecIniTramo4) {
		this.fecIniTramo4 = fecIniTramo4;
	}

	public Timestamp getFecIniTramo5() {
		return fecIniTramo5;
	}

	public void setFecIniTramo5(Timestamp fecIniTramo5) {
		this.fecIniTramo5 = fecIniTramo5;
	}

	public String getGapI1() {
		return gapI1;
	}

	public void setGapI1(String gapI1) {
		this.gapI1 = gapI1;
	}

	public String getGapI2() {
		return gapI2;
	}

	public void setGapI2(String gapI2) {
		this.gapI2 = gapI2;
	}

	public String getGapI3() {
		return gapI3;
	}

	public void setGapI3(String gapI3) {
		this.gapI3 = gapI3;
	}

	public String getGapI4() {
		return gapI4;
	}

	public void setGapI4(String gapI4) {
		this.gapI4 = gapI4;
	}

	public String getGapI5() {
		return gapI5;
	}

	public void setGapI5(String gapI5) {
		this.gapI5 = gapI5;
	}

	public java.math.BigDecimal getImpgasmax() {
		return impgasmax;
	}

	public void setImpgasmax(java.math.BigDecimal impgasmax) {
		this.impgasmax = impgasmax;
	}

	public java.math.BigDecimal getImpgasmin() {
		return impgasmin;
	}

	public void setImpgasmin(java.math.BigDecimal impgasmin) {
		this.impgasmin = impgasmin;
	}

	public java.math.BigDecimal getPgastgesex1I() {
		return pgastgesex1I;
	}

	public void setPgastgesex1I(java.math.BigDecimal pgastgesex1i) {
		pgastgesex1I = pgastgesex1i;
	}

	public java.math.BigDecimal getPgastgesex2I() {
		return pgastgesex2I;
	}

	public void setPgastgesex2I(java.math.BigDecimal pgastgesex2i) {
		pgastgesex2I = pgastgesex2i;
	}

	public java.math.BigDecimal getPgastgesin1I() {
		return pgastgesin1I;
	}

	public void setPgastgesin1I(java.math.BigDecimal pgastgesin1i) {
		pgastgesin1I = pgastgesin1i;
	}

	public java.math.BigDecimal getPgastgesin2I() {
		return pgastgesin2I;
	}

	public void setPgastgesin2I(java.math.BigDecimal pgastgesin2i) {
		pgastgesin2I = pgastgesin2i;
	}

	public java.math.BigDecimal getPgastgesin3I() {
		return pgastgesin3I;
	}

	public void setPgastgesin3I(java.math.BigDecimal pgastgesin3i) {
		pgastgesin3I = pgastgesin3i;
	}

	public java.math.BigDecimal getPgastgesin4I() {
		return pgastgesin4I;
	}

	public void setPgastgesin4I(java.math.BigDecimal pgastgesin4i) {
		pgastgesin4I = pgastgesin4i;
	}

	public java.math.BigDecimal getPgastgesin5I() {
		return pgastgesin5I;
	}

	public void setPgastgesin5I(java.math.BigDecimal pgastgesin5i) {
		pgastgesin5I = pgastgesin5i;
	}

	public java.math.BigDecimal getPintertecnI1() {
		return pintertecnI1;
	}

	public void setPintertecnI1(java.math.BigDecimal pintertecnI1) {
		this.pintertecnI1 = pintertecnI1;
	}

	public java.math.BigDecimal getPintertecnI2() {
		return pintertecnI2;
	}

	public void setPintertecnI2(java.math.BigDecimal pintertecnI2) {
		this.pintertecnI2 = pintertecnI2;
	}

	public java.math.BigDecimal getPintertecnI3() {
		return pintertecnI3;
	}

	public void setPintertecnI3(java.math.BigDecimal pintertecnI3) {
		this.pintertecnI3 = pintertecnI3;
	}

	public java.math.BigDecimal getPintertecnI4() {
		return pintertecnI4;
	}

	public void setPintertecnI4(java.math.BigDecimal pintertecnI4) {
		this.pintertecnI4 = pintertecnI4;
	}

	public java.math.BigDecimal getPintertecnI5() {
		return pintertecnI5;
	}

	public void setPintertecnI5(java.math.BigDecimal pintertecnI5) {
		this.pintertecnI5 = pintertecnI5;
	}

	public java.math.BigDecimal getPriesgo() {
		return priesgo;
	}

	public void setPriesgo(java.math.BigDecimal priesgo) {
		this.priesgo = priesgo;
	}

	public java.math.BigDecimal getPsobremort() {
		return psobremort;
	}

	public void setPsobremort(java.math.BigDecimal psobremort) {
		this.psobremort = psobremort;
	}

	public String getRagravado() {
		return ragravado;
	}

	public void setRagravado(String ragravado) {
		this.ragravado = ragravado;
	}

	public String getSwcasadoI1() {
		return swcasadoI1;
	}

	public void setSwcasadoI1(String swcasadoI1) {
		this.swcasadoI1 = swcasadoI1;
	}

	public String getSwcasadoI2() {
		return swcasadoI2;
	}

	public void setSwcasadoI2(String swcasadoI2) {
		this.swcasadoI2 = swcasadoI2;
	}

	public String getSwcasadoI3() {
		return swcasadoI3;
	}

	public void setSwcasadoI3(String swcasadoI3) {
		this.swcasadoI3 = swcasadoI3;
	}

	public String getSwcasadoI4() {
		return swcasadoI4;
	}

	public void setSwcasadoI4(String swcasadoI4) {
		this.swcasadoI4 = swcasadoI4;
	}

	public String getSwcasadoI5() {
		return swcasadoI5;
	}

	public void setSwcasadoI5(String swcasadoI5) {
		this.swcasadoI5 = swcasadoI5;
	}

	public String getTabla1Aseg1() {
		return tabla1Aseg1;
	}

	public void setTabla1Aseg1(String tabla1Aseg1) {
		this.tabla1Aseg1 = tabla1Aseg1;
	}

	public String getTabla1Aseg2() {
		return tabla1Aseg2;
	}

	public void setTabla1Aseg2(String tabla1Aseg2) {
		this.tabla1Aseg2 = tabla1Aseg2;
	}

	public String getTabla1Aseg3() {
		return tabla1Aseg3;
	}

	public void setTabla1Aseg3(String tabla1Aseg3) {
		this.tabla1Aseg3 = tabla1Aseg3;
	}

	public String getTabla1Aseg4() {
		return tabla1Aseg4;
	}

	public void setTabla1Aseg4(String tabla1Aseg4) {
		this.tabla1Aseg4 = tabla1Aseg4;
	}

	public String getTabla1Aseg5() {
		return tabla1Aseg5;
	}

	public void setTabla1Aseg5(String tabla1Aseg5) {
		this.tabla1Aseg5 = tabla1Aseg5;
	}

	public String getTabla2Aseg1() {
		return tabla2Aseg1;
	}

	public void setTabla2Aseg1(String tabla2Aseg1) {
		this.tabla2Aseg1 = tabla2Aseg1;
	}

	public String getTabla2Aseg2() {
		return tabla2Aseg2;
	}

	public void setTabla2Aseg2(String tabla2Aseg2) {
		this.tabla2Aseg2 = tabla2Aseg2;
	}

	public String getTabla2Aseg3() {
		return tabla2Aseg3;
	}

	public void setTabla2Aseg3(String tabla2Aseg3) {
		this.tabla2Aseg3 = tabla2Aseg3;
	}

	public String getTabla2Aseg4() {
		return tabla2Aseg4;
	}

	public void setTabla2Aseg4(String tabla2Aseg4) {
		this.tabla2Aseg4 = tabla2Aseg4;
	}

	public String getTabla2Aseg5() {
		return tabla2Aseg5;
	}

	public void setTabla2Aseg5(String tabla2Aseg5) {
		this.tabla2Aseg5 = tabla2Aseg5;
	}

	public String getTabla3Aseg1() {
		return tabla3Aseg1;
	}

	public void setTabla3Aseg1(String tabla3Aseg1) {
		this.tabla3Aseg1 = tabla3Aseg1;
	}

	public String getTabla3Aseg2() {
		return tabla3Aseg2;
	}

	public void setTabla3Aseg2(String tabla3Aseg2) {
		this.tabla3Aseg2 = tabla3Aseg2;
	}

	public String getTabla3Aseg3() {
		return tabla3Aseg3;
	}

	public void setTabla3Aseg3(String tabla3Aseg3) {
		this.tabla3Aseg3 = tabla3Aseg3;
	}

	public String getTabla3Aseg4() {
		return tabla3Aseg4;
	}

	public void setTabla3Aseg4(String tabla3Aseg4) {
		this.tabla3Aseg4 = tabla3Aseg4;
	}

	public String getTabla3Aseg5() {
		return tabla3Aseg5;
	}

	public void setTabla3Aseg5(String tabla3Aseg5) {
		this.tabla3Aseg5 = tabla3Aseg5;
	}

	public java.math.BigDecimal getGgim() {
		return ggim;
	}

	public void setGgim(java.math.BigDecimal ggim) {
		this.ggim = ggim;
	}

	@Override
	public UmicKey getKey() {
		// No existe la posibilidad de crear una UmicKey a partir de los datos
		return null;
	}
}
