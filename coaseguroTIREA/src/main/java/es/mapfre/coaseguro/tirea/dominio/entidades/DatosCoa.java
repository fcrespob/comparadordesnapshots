package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.math.BigDecimal;
import java.sql.Timestamp;

import es.mapfre.coaseguro.tirea.dominio.EntidadBase;
import es.mapfre.coaseguro.tirea.dominio.keys.DatosCoaKey;

public class DatosCoa implements EntidadBase<DatosCoaKey>{


	public static final int IND_POLIZA = 0;
	public static final int IND_SUBPOLIZA = 1;
	public static final int IND_KCUADRO = 2;
	public static final int IND_FCUADROCOA = 3;
	public static final int IND_REPGAST = 4;
	public static final int IND_TOTPARC = 5;
	public static final int IND_SOBREPRI = 6;
	public static final int IND_GASTADM = 7;
	public static final int IND_GI = 8;
	public static final int IND_GE = 9;
	public static final int IND_KCOASEORI1 = 10;
	public static final int IND_KCOASE1 = 11;
	public static final int IND_DESCRIPCION1 = 12;
	public static final int IND_KCOASEORI2 = 13;
	public static final int IND_KCOASE2 = 14;
	public static final int IND_DESCRIPCION2 = 15;
	public static final int IND_KCOASEORI3 = 16;
	public static final int IND_KCOASE3 = 17;
	public static final int IND_DESCRIPCION3 = 18;
	public static final int IND_KCOASEORI4 = 19;
	public static final int IND_KCOASE4 = 20;
	public static final int IND_DESCRIPCION4 = 21;
	public static final int IND_KCOASEORI5 = 22;
	public static final int IND_KCOASE5 = 23;
	public static final int IND_DESCRIPCION5 = 24;
	public static final int IND_KCOASEORI6 = 25;
	public static final int IND_KCOASE6 = 26;
	public static final int IND_DESCRIPCION6 = 27;
	public static final int IND_KCOASEORI7 = 28;
	public static final int IND_KCOASE7 = 29;
	public static final int IND_DESCRIPCION7 = 30;
	public static final int IND_KCOASEORI8 = 31;
	public static final int IND_KCOASE = 32;
	public static final int IND_DESCRIPCION8 = 33;
	public static final int IND_KCOASEORI9 = 34;
	public static final int IND_KCOASE9 = 35;
	public static final int IND_DESCRIPCION9 = 36;
	public static final int IND_KCOASEORI10 = 37;
	public static final int IND_KCOASE10 = 38;
	public static final int IND_DESCRIPCION10 = 39;
	public static final int IND_KCOASEORI11 = 40;
	public static final int IND_KCOASE11 = 41;
	public static final int IND_DESCRIPCION11 = 42;
	public static final int IND_KCOASEORI12 = 43;
	public static final int IND_KCOASE12 = 44;
	public static final int IND_DESCRIPCION12 = 45;
	public static final int IND_KCOASEORI13 = 46;
	public static final int IND_KCOASE13 = 47;
	public static final int IND_DESCRIPCION13 = 48;
	public static final int IND_KCOASEORI14 = 49;
	public static final int IND_KCOASE14 = 50;
	public static final int IND_DESCRIPCION14 = 51;
	public static final int IND_KCOASEORI15 = 52;
	public static final int IND_KCOASE15 = 53;
	public static final int IND_DESCRIPCION15 = 54;
	public static final int IND_KCOASEORI16 = 55;
	public static final int IND_KCOASE16 = 56;
	public static final int IND_DESCRIPCION16 = 57;
	public static final int IND_KCOASEORI17 = 58;
	public static final int IND_KCOASE17 = 59;
	public static final int IND_DESCRIPCION17 = 60;
	public static final int IND_KCOASEORI18 = 61;
	public static final int IND_KCOASE18 = 62;
	public static final int IND_DESCRIPCION18 = 63;
	public static final int IND_KCOASEORI19 = 64;
	public static final int IND_KCOASE19 = 65;
	public static final int IND_DESCRIPCION19 = 66;
	public static final int IND_KCOASEORI20 = 67;
	public static final int IND_KCOASE20 = 68;
	public static final int IND_DESCRIPCION20 = 69;
	
	private Long poliza;
	private Integer subpoliza;
	private Integer kCuadro;
	private Timestamp fCuadroCoa;
	private String repGast;
	private String totParc;
	private String sobrePri;
	private BigDecimal gastAdm;
	private BigDecimal GI;
	private BigDecimal GE;
	private String kCoaseOri;
	private String KCoase1;
	private String descripcion1;
	private BigDecimal pCoase1;
	private BigDecimal pPartiGas1;
	private String KCoase2;
	private String descripcion2;
	private BigDecimal pCoase2;
	private BigDecimal pPartiGas2;
	private String KCoase3;
	private String descripcion3;
	private BigDecimal pCoase3;
	private BigDecimal pPartiGas3;
	private String KCoase4;
	private String descripcion4;
	private BigDecimal pCoase4;
	private BigDecimal pPartiGas4;
	private String KCoase5;
	private String descripcion5;
	private BigDecimal pCoase5;
	private BigDecimal pPartiGas5;
	private String KCoase6;
	private String descripcion6;
	private BigDecimal pCoase6;
	private BigDecimal pPartiGas6;
	private String KCoase7;
	private String descripcion7;
	private BigDecimal pCoase7;
	private BigDecimal pPartiGas7;
	private String KCoase8;
	private String descripcion8;
	private BigDecimal pCoase8;
	private BigDecimal pPartiGas8;
	private String KCoase9;
	private String descripcion9;
	private BigDecimal pCoase9;
	private BigDecimal pPartiGas9;
	private String KCoase10;
	private String descripcion10;
	private BigDecimal pCoase10;
	private BigDecimal pPartiGas10;
	private String KCoase11;
	private String descripcion11;
	private BigDecimal pCoase11;
	private BigDecimal pPartiGas11;
	private String KCoase12;
	private String descripcion12;
	private BigDecimal pCoase12;
	private BigDecimal pPartiGas12;
	private String KCoase13;
	private String descripcion13;
	private BigDecimal pCoase13;
	private BigDecimal pPartiGas13;
	private String KCoase14;
	private String descripcion14;
	private BigDecimal pCoase14;
	private BigDecimal pPartiGas14;
	private String KCoase15;
	private String descripcion15;
	private BigDecimal pCoase15;
	private BigDecimal pPartiGas15;
	private String KCoase16;
	private String descripcion16;
	private BigDecimal pCoase16;
	private BigDecimal pPartiGas16;
	private String KCoase17;
	private String descripcion17;
	private BigDecimal pCoase17;
	private BigDecimal pPartiGas17;
	private String KCoase18;
	private String descripcion18;
	private BigDecimal pCoase18;
	private BigDecimal pPartiGas18;
	private String KCoase19;
	private String descripcion19;
	private BigDecimal pCoase19;
	private BigDecimal pPartiGas19;
	private String KCoase20;
	private String descripcion20;
	private BigDecimal pCoase20;
	private BigDecimal pPartiGas20;

	public Long getPoliza() {
		return poliza;
	}

	public void setPoliza(Long poliza) {
		this.poliza = poliza;
	}

	public Integer getSubpoliza() {
		return subpoliza;
	}

	public void setSubpoliza(Integer subpoliza) {
		this.subpoliza = subpoliza;
	}

	public Integer getkCuadro() {
		return kCuadro;
	}

	public void setkCuadro(Integer kCuadro) {
		this.kCuadro = kCuadro;
	}

	public Timestamp getfCuadroCoa() {
		return fCuadroCoa;
	}

	public void setfCuadroCoa(Timestamp fCuadroCoa) {
		this.fCuadroCoa = fCuadroCoa;
	}

	public String getRepGast() {
		return repGast;
	}

	public void setRepGast(String repGast) {
		this.repGast = repGast;
	}

	public String getTotParc() {
		return totParc;
	}

	public void setTotParc(String totParc) {
		this.totParc = totParc;
	}

	public String getSobrePri() {
		return sobrePri;
	}

	public void setSobrePri(String sobrePri) {
		this.sobrePri = sobrePri;
	}

	public BigDecimal getGastAdm() {
		return gastAdm;
	}

	public void setGastAdm(BigDecimal gastAdm) {
		this.gastAdm = gastAdm;
	}

	public BigDecimal getGI() {
		return GI;
	}

	public void setGI(BigDecimal gI) {
		GI = gI;
	}

	public BigDecimal getGE() {
		return GE;
	}

	public void setGE(BigDecimal gE) {
		GE = gE;
	}

	public String getkCoaseOri() {
		return kCoaseOri;
	}

	public void setkCoaseOri(String kCoaseOri) {
		this.kCoaseOri = kCoaseOri;
	}

	public String getKCoase1() {
		return KCoase1;
	}

	public void setKCoase1(String kCoase1) {
		KCoase1 = kCoase1;
	}

	public String getDescripcion1() {
		return descripcion1;
	}

	public void setDescripcion1(String descripcion1) {
		this.descripcion1 = descripcion1;
	}

	public BigDecimal getpCoase1() {
		return pCoase1;
	}

	public void setpCoase1(BigDecimal pCoase1) {
		this.pCoase1 = pCoase1;
	}

	public BigDecimal getpPartiGas1() {
		return pPartiGas1;
	}

	public void setpPartiGas1(BigDecimal pPartiGas1) {
		this.pPartiGas1 = pPartiGas1;
	}

	public String getKCoase2() {
		return KCoase2;
	}

	public void setKCoase2(String kCoase2) {
		KCoase2 = kCoase2;
	}

	public String getDescripcion2() {
		return descripcion2;
	}

	public void setDescripcion2(String descripcion2) {
		this.descripcion2 = descripcion2;
	}

	public BigDecimal getpCoase2() {
		return pCoase2;
	}

	public void setpCoase2(BigDecimal pCoase2) {
		this.pCoase2 = pCoase2;
	}

	public BigDecimal getpPartiGas2() {
		return pPartiGas2;
	}

	public void setpPartiGas2(BigDecimal pPartiGas2) {
		this.pPartiGas2 = pPartiGas2;
	}

	public String getKCoase3() {
		return KCoase3;
	}

	public void setKCoase3(String kCoase3) {
		KCoase3 = kCoase3;
	}

	public String getDescripcion3() {
		return descripcion3;
	}

	public void setDescripcion3(String descripcion3) {
		this.descripcion3 = descripcion3;
	}

	public BigDecimal getpCoase3() {
		return pCoase3;
	}

	public void setpCoase3(BigDecimal pCoase3) {
		this.pCoase3 = pCoase3;
	}

	public BigDecimal getpPartiGas3() {
		return pPartiGas3;
	}

	public void setpPartiGas3(BigDecimal pPartiGas3) {
		this.pPartiGas3 = pPartiGas3;
	}

	public String getKCoase4() {
		return KCoase4;
	}

	public void setKCoase4(String kCoase4) {
		KCoase4 = kCoase4;
	}

	public String getDescripcion4() {
		return descripcion4;
	}

	public void setDescripcion4(String descripcion4) {
		this.descripcion4 = descripcion4;
	}

	public BigDecimal getpCoase4() {
		return pCoase4;
	}

	public void setpCoase4(BigDecimal pCoase4) {
		this.pCoase4 = pCoase4;
	}

	public BigDecimal getpPartiGas4() {
		return pPartiGas4;
	}

	public void setpPartiGas4(BigDecimal pPartiGas4) {
		this.pPartiGas4 = pPartiGas4;
	}

	public String getKCoase5() {
		return KCoase5;
	}

	public void setKCoase5(String kCoase5) {
		KCoase5 = kCoase5;
	}

	public String getDescripcion5() {
		return descripcion5;
	}

	public void setDescripcion5(String descripcion5) {
		this.descripcion5 = descripcion5;
	}

	public BigDecimal getpCoase5() {
		return pCoase5;
	}

	public void setpCoase5(BigDecimal pCoase5) {
		this.pCoase5 = pCoase5;
	}

	public BigDecimal getpPartiGas5() {
		return pPartiGas5;
	}

	public void setpPartiGas5(BigDecimal pPartiGas5) {
		this.pPartiGas5 = pPartiGas5;
	}

	public String getKCoase6() {
		return KCoase6;
	}

	public void setKCoase6(String kCoase6) {
		KCoase6 = kCoase6;
	}

	public String getDescripcion6() {
		return descripcion6;
	}

	public void setDescripcion6(String descripcion6) {
		this.descripcion6 = descripcion6;
	}

	public BigDecimal getpCoase6() {
		return pCoase6;
	}

	public void setpCoase6(BigDecimal pCoase6) {
		this.pCoase6 = pCoase6;
	}

	public BigDecimal getpPartiGas6() {
		return pPartiGas6;
	}

	public void setpPartiGas6(BigDecimal pPartiGas6) {
		this.pPartiGas6 = pPartiGas6;
	}

	public String getKCoase7() {
		return KCoase7;
	}

	public void setKCoase7(String kCoase7) {
		KCoase7 = kCoase7;
	}

	public String getDescripcion7() {
		return descripcion7;
	}

	public void setDescripcion7(String descripcion7) {
		this.descripcion7 = descripcion7;
	}

	public BigDecimal getpCoase7() {
		return pCoase7;
	}

	public void setpCoase7(BigDecimal pCoase7) {
		this.pCoase7 = pCoase7;
	}

	public BigDecimal getpPartiGas7() {
		return pPartiGas7;
	}

	public void setpPartiGas7(BigDecimal pPartiGas7) {
		this.pPartiGas7 = pPartiGas7;
	}

	public String getKCoase8() {
		return KCoase8;
	}

	public void setKCoase8(String kCoase8) {
		KCoase8 = kCoase8;
	}

	public String getDescripcion8() {
		return descripcion8;
	}

	public void setDescripcion8(String descripcion8) {
		this.descripcion8 = descripcion8;
	}

	public BigDecimal getpCoase8() {
		return pCoase8;
	}

	public void setpCoase8(BigDecimal pCoase8) {
		this.pCoase8 = pCoase8;
	}

	public BigDecimal getpPartiGas8() {
		return pPartiGas8;
	}

	public void setpPartiGas8(BigDecimal pPartiGas8) {
		this.pPartiGas8 = pPartiGas8;
	}

	public String getKCoase9() {
		return KCoase9;
	}

	public void setKCoase9(String kCoase9) {
		KCoase9 = kCoase9;
	}

	public String getDescripcion9() {
		return descripcion9;
	}

	public void setDescripcion9(String descripcion9) {
		this.descripcion9 = descripcion9;
	}

	public BigDecimal getpCoase9() {
		return pCoase9;
	}

	public void setpCoase9(BigDecimal pCoase9) {
		this.pCoase9 = pCoase9;
	}

	public BigDecimal getpPartiGas9() {
		return pPartiGas9;
	}

	public void setpPartiGas9(BigDecimal pPartiGas9) {
		this.pPartiGas9 = pPartiGas9;
	}

	public String getKCoase10() {
		return KCoase10;
	}

	public void setKCoase10(String kCoase10) {
		KCoase10 = kCoase10;
	}

	public String getDescripcion10() {
		return descripcion10;
	}

	public void setDescripcion10(String descripcion10) {
		this.descripcion10 = descripcion10;
	}

	public BigDecimal getpCoase10() {
		return pCoase10;
	}

	public void setpCoase10(BigDecimal pCoase10) {
		this.pCoase10 = pCoase10;
	}

	public BigDecimal getpPartiGas10() {
		return pPartiGas10;
	}

	public void setpPartiGas10(BigDecimal pPartiGas10) {
		this.pPartiGas10 = pPartiGas10;
	}

	public String getKCoase11() {
		return KCoase11;
	}

	public void setKCoase11(String kCoase11) {
		KCoase11 = kCoase11;
	}

	public String getDescripcion11() {
		return descripcion11;
	}

	public void setDescripcion11(String descripcion11) {
		this.descripcion11 = descripcion11;
	}

	public BigDecimal getpCoase11() {
		return pCoase11;
	}

	public void setpCoase11(BigDecimal pCoase11) {
		this.pCoase11 = pCoase11;
	}

	public BigDecimal getpPartiGas11() {
		return pPartiGas11;
	}

	public void setpPartiGas11(BigDecimal pPartiGas11) {
		this.pPartiGas11 = pPartiGas11;
	}

	public String getKCoase12() {
		return KCoase12;
	}

	public void setKCoase12(String kCoase12) {
		KCoase12 = kCoase12;
	}

	public String getDescripcion12() {
		return descripcion12;
	}

	public void setDescripcion12(String descripcion12) {
		this.descripcion12 = descripcion12;
	}

	public BigDecimal getpCoase12() {
		return pCoase12;
	}

	public void setpCoase12(BigDecimal pCoase12) {
		this.pCoase12 = pCoase12;
	}

	public BigDecimal getpPartiGas12() {
		return pPartiGas12;
	}

	public void setpPartiGas12(BigDecimal pPartiGas12) {
		this.pPartiGas12 = pPartiGas12;
	}

	public String getKCoase13() {
		return KCoase13;
	}

	public void setKCoase13(String kCoase13) {
		KCoase13 = kCoase13;
	}

	public String getDescripcion13() {
		return descripcion13;
	}

	public void setDescripcion13(String descripcion13) {
		this.descripcion13 = descripcion13;
	}

	public BigDecimal getpCoase13() {
		return pCoase13;
	}

	public void setpCoase13(BigDecimal pCoase13) {
		this.pCoase13 = pCoase13;
	}

	public BigDecimal getpPartiGas13() {
		return pPartiGas13;
	}

	public void setpPartiGas13(BigDecimal pPartiGas13) {
		this.pPartiGas13 = pPartiGas13;
	}

	public String getKCoase14() {
		return KCoase14;
	}

	public void setKCoase14(String kCoase14) {
		KCoase14 = kCoase14;
	}

	public String getDescripcion14() {
		return descripcion14;
	}

	public void setDescripcion14(String descripcion14) {
		this.descripcion14 = descripcion14;
	}

	public BigDecimal getpCoase14() {
		return pCoase14;
	}

	public void setpCoase14(BigDecimal pCoase14) {
		this.pCoase14 = pCoase14;
	}

	public BigDecimal getpPartiGas14() {
		return pPartiGas14;
	}

	public void setpPartiGas14(BigDecimal pPartiGas14) {
		this.pPartiGas14 = pPartiGas14;
	}

	public String getKCoase15() {
		return KCoase15;
	}

	public void setKCoase15(String kCoase15) {
		KCoase15 = kCoase15;
	}

	public String getDescripcion15() {
		return descripcion15;
	}

	public void setDescripcion15(String descripcion15) {
		this.descripcion15 = descripcion15;
	}

	public BigDecimal getpCoase15() {
		return pCoase15;
	}

	public void setpCoase15(BigDecimal pCoase15) {
		this.pCoase15 = pCoase15;
	}

	public BigDecimal getpPartiGas15() {
		return pPartiGas15;
	}

	public void setpPartiGas15(BigDecimal pPartiGas15) {
		this.pPartiGas15 = pPartiGas15;
	}

	public String getKCoase16() {
		return KCoase16;
	}

	public void setKCoase16(String kCoase16) {
		KCoase16 = kCoase16;
	}

	public String getDescripcion16() {
		return descripcion16;
	}

	public void setDescripcion16(String descripcion16) {
		this.descripcion16 = descripcion16;
	}

	public BigDecimal getpCoase16() {
		return pCoase16;
	}

	public void setpCoase16(BigDecimal pCoase16) {
		this.pCoase16 = pCoase16;
	}

	public BigDecimal getpPartiGas16() {
		return pPartiGas16;
	}

	public void setpPartiGas16(BigDecimal pPartiGas16) {
		this.pPartiGas16 = pPartiGas16;
	}

	public String getKCoase17() {
		return KCoase17;
	}

	public void setKCoase17(String kCoase17) {
		KCoase17 = kCoase17;
	}

	public String getDescripcion17() {
		return descripcion17;
	}

	public void setDescripcion17(String descripcion17) {
		this.descripcion17 = descripcion17;
	}

	public BigDecimal getpCoase17() {
		return pCoase17;
	}

	public void setpCoase17(BigDecimal pCoase17) {
		this.pCoase17 = pCoase17;
	}

	public BigDecimal getpPartiGas17() {
		return pPartiGas17;
	}

	public void setpPartiGas17(BigDecimal pPartiGas17) {
		this.pPartiGas17 = pPartiGas17;
	}

	public String getKCoase18() {
		return KCoase18;
	}

	public void setKCoase18(String kCoase18) {
		KCoase18 = kCoase18;
	}

	public String getDescripcion18() {
		return descripcion18;
	}

	public void setDescripcion18(String descripcion18) {
		this.descripcion18 = descripcion18;
	}

	public BigDecimal getpCoase18() {
		return pCoase18;
	}

	public void setpCoase18(BigDecimal pCoase18) {
		this.pCoase18 = pCoase18;
	}

	public BigDecimal getpPartiGas18() {
		return pPartiGas18;
	}

	public void setpPartiGas18(BigDecimal pPartiGas18) {
		this.pPartiGas18 = pPartiGas18;
	}

	public String getKCoase19() {
		return KCoase19;
	}

	public void setKCoase19(String kCoase19) {
		KCoase19 = kCoase19;
	}

	public String getDescripcion19() {
		return descripcion19;
	}

	public void setDescripcion19(String descripcion19) {
		this.descripcion19 = descripcion19;
	}

	public BigDecimal getpCoase19() {
		return pCoase19;
	}

	public void setpCoase19(BigDecimal pCoase19) {
		this.pCoase19 = pCoase19;
	}

	public BigDecimal getpPartiGas19() {
		return pPartiGas19;
	}

	public void setpPartiGas19(BigDecimal pPartiGas19) {
		this.pPartiGas19 = pPartiGas19;
	}

	public String getKCoase20() {
		return KCoase20;
	}

	public void setKCoase20(String kCoase20) {
		KCoase20 = kCoase20;
	}

	public String getDescripcion20() {
		return descripcion20;
	}

	public void setDescripcion20(String descripcion20) {
		this.descripcion20 = descripcion20;
	}

	public BigDecimal getpCoase20() {
		return pCoase20;
	}

	public void setpCoase20(BigDecimal pCoase20) {
		this.pCoase20 = pCoase20;
	}

	public BigDecimal getpPartiGas20() {
		return pPartiGas20;
	}

	public void setpPartiGas20(BigDecimal pPartiGas20) {
		this.pPartiGas20 = pPartiGas20;
	}

	public static int getIndPoliza() {
		return IND_POLIZA;
	}

	public static int getIndSubpoliza() {
		return IND_SUBPOLIZA;
	}

	public static int getIndKcuadro() {
		return IND_KCUADRO;
	}

	public static int getIndFcuadrocoa() {
		return IND_FCUADROCOA;
	}

	public static int getIndRepgast() {
		return IND_REPGAST;
	}

	public static int getIndTotparc() {
		return IND_TOTPARC;
	}

	public static int getIndSobrepri() {
		return IND_SOBREPRI;
	}

	public static int getIndGastadm() {
		return IND_GASTADM;
	}

	public static int getIndGi() {
		return IND_GI;
	}

	public static int getIndGe() {
		return IND_GE;
	}

	public static int getIndKcoaseori1() {
		return IND_KCOASEORI1;
	}

	public static int getIndKcoase1() {
		return IND_KCOASE1;
	}

	public static int getIndDescripcion1() {
		return IND_DESCRIPCION1;
	}

	public static int getIndKcoaseori2() {
		return IND_KCOASEORI2;
	}

	public static int getIndKcoase2() {
		return IND_KCOASE2;
	}

	public static int getIndDescripcion2() {
		return IND_DESCRIPCION2;
	}

	public static int getIndKcoaseori3() {
		return IND_KCOASEORI3;
	}

	public static int getIndKcoase3() {
		return IND_KCOASE3;
	}

	public static int getIndDescripcion3() {
		return IND_DESCRIPCION3;
	}

	public static int getIndKcoaseori4() {
		return IND_KCOASEORI4;
	}

	public static int getIndKcoase4() {
		return IND_KCOASE4;
	}

	public static int getIndDescripcion4() {
		return IND_DESCRIPCION4;
	}

	public static int getIndKcoaseori5() {
		return IND_KCOASEORI5;
	}

	public static int getIndKcoase5() {
		return IND_KCOASE5;
	}

	public static int getIndDescripcion5() {
		return IND_DESCRIPCION5;
	}

	public static int getIndKcoaseori6() {
		return IND_KCOASEORI6;
	}

	public static int getIndKcoase6() {
		return IND_KCOASE6;
	}

	public static int getIndDescripcion6() {
		return IND_DESCRIPCION6;
	}

	public static int getIndKcoaseori7() {
		return IND_KCOASEORI7;
	}

	public static int getIndKcoase7() {
		return IND_KCOASE7;
	}

	public static int getIndDescripcion7() {
		return IND_DESCRIPCION7;
	}

	public static int getIndKcoaseori8() {
		return IND_KCOASEORI8;
	}

	public static int getIndKcoase() {
		return IND_KCOASE;
	}

	public static int getIndDescripcion8() {
		return IND_DESCRIPCION8;
	}

	public static int getIndKcoaseori9() {
		return IND_KCOASEORI9;
	}

	public static int getIndKcoase9() {
		return IND_KCOASE9;
	}

	public static int getIndDescripcion9() {
		return IND_DESCRIPCION9;
	}

	public static int getIndKcoaseori10() {
		return IND_KCOASEORI10;
	}

	public static int getIndKcoase10() {
		return IND_KCOASE10;
	}

	public static int getIndDescripcion10() {
		return IND_DESCRIPCION10;
	}

	public static int getIndKcoaseori11() {
		return IND_KCOASEORI11;
	}

	public static int getIndKcoase11() {
		return IND_KCOASE11;
	}

	public static int getIndDescripcion11() {
		return IND_DESCRIPCION11;
	}

	public static int getIndKcoaseori12() {
		return IND_KCOASEORI12;
	}

	public static int getIndKcoase12() {
		return IND_KCOASE12;
	}

	public static int getIndDescripcion12() {
		return IND_DESCRIPCION12;
	}

	public static int getIndKcoaseori13() {
		return IND_KCOASEORI13;
	}

	public static int getIndKcoase13() {
		return IND_KCOASE13;
	}

	public static int getIndDescripcion13() {
		return IND_DESCRIPCION13;
	}

	public static int getIndKcoaseori14() {
		return IND_KCOASEORI14;
	}

	public static int getIndKcoase14() {
		return IND_KCOASE14;
	}

	public static int getIndDescripcion14() {
		return IND_DESCRIPCION14;
	}

	public static int getIndKcoaseori15() {
		return IND_KCOASEORI15;
	}

	public static int getIndKcoase15() {
		return IND_KCOASE15;
	}

	public static int getIndDescripcion15() {
		return IND_DESCRIPCION15;
	}

	public static int getIndKcoaseori16() {
		return IND_KCOASEORI16;
	}

	public static int getIndKcoase16() {
		return IND_KCOASE16;
	}

	public static int getIndDescripcion16() {
		return IND_DESCRIPCION16;
	}

	public static int getIndKcoaseori17() {
		return IND_KCOASEORI17;
	}

	public static int getIndKcoase17() {
		return IND_KCOASE17;
	}

	public static int getIndDescripcion17() {
		return IND_DESCRIPCION17;
	}

	public static int getIndKcoaseori18() {
		return IND_KCOASEORI18;
	}

	public static int getIndKcoase18() {
		return IND_KCOASE18;
	}

	public static int getIndDescripcion18() {
		return IND_DESCRIPCION18;
	}

	public static int getIndKcoaseori19() {
		return IND_KCOASEORI19;
	}

	public static int getIndKcoase19() {
		return IND_KCOASE19;
	}

	public static int getIndDescripcion19() {
		return IND_DESCRIPCION19;
	}

	public static int getIndKcoaseori20() {
		return IND_KCOASEORI20;
	}

	public static int getIndKcoase20() {
		return IND_KCOASE20;
	}

	public static int getIndDescripcion20() {
		return IND_DESCRIPCION20;
	}

	
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((poliza == null) ? 0 : poliza.hashCode());
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
		DatosCoa other = (DatosCoa) obj;
		if (poliza == null) {
			if (other.poliza != null)
				return false;
		} else if (!poliza.equals(other.poliza))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DatosCoa [poliza=" + poliza + "]";
	}

	@Override
	public DatosCoaKey getKey() {
		return new DatosCoaKey(poliza,subpoliza);
	}
}

