package es.mapfre.solvencia.dominio.salidaCalculo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.DetalleBaseTecnicaKey;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class DetalleBaseTecnica implements EntidadBase<DetalleBaseTecnicaKey>, EntidadConBaseTec {
	
	public static final int IND_BASETEC = 0;
	public static final int IND_UMICKEY = 1;
	public static final int IND_CCARTERA = 2;
	public static final int IND_CURVA_TI = 3;
	public static final int IND_EDADCALC = 4;
	public static final int IND_FACTOR1 = 5;
	public static final int IND_FACTOR2 = 6;
	public static final int IND_FECCIERRE = 7;
	public static final int IND_FTABLA_AN = 8;
	public static final int IND_FCURVA_TI = 9;
	public static final int IND_FECFINTRAMO = 10;
	public static final int IND_FECINITRAMO = 11;
	public static final int IND_FITROSSP = 12;
	public static final int IND_GTO_PROV = 13;
	public static final int IND_GTOROSSP_CAP = 14;
	public static final int IND_GTOROSSP_PRIMA = 15;
	public static final int IND_GTOROSSP_PROV = 16;
	public static final int IND_GTO_UNI = 17;
	public static final int IND_ITCALC = 18;
	public static final int IND_METODO_PT_ROSSP = 19;
	public static final int IND_PER_TRANS_ROSSP = 20;
	public static final int IND_TABLACALC1ASEG1 = 21;
	public static final int IND_TABLACALC1ASEG2 = 22;
	public static final int IND_TABLACALC1ASEG3 = 23;
	public static final int IND_TABLACALC1ASEG4 = 24;
	public static final int IND_TABLACALC1ASEG5 = 25;

	public static final int IND_TABLACALC2ASEG1 = 26;
	public static final int IND_TABLACALC2ASEG2 = 27;
	public static final int IND_TABLACALC2ASEG3 = 28;
	public static final int IND_TABLACALC2ASEG4 = 29;
	public static final int IND_TABLACALC2ASEG5 = 30;

	public static final int IND_TABLACALC3ASEG1 = 31;
	public static final int IND_TABLACALC3ASEG2 = 32;
	public static final int IND_TABLACALC3ASEG3 = 33;
	public static final int IND_TABLACALC3ASEG4 = 34;
	public static final int IND_TABLACALC3ASEG5 = 35;
	public static final int IND_TABLA_TANUL = 36;
	public static final int IND_CCANAL = 37;
	public static final int IND_CNEGOCIO = 38;
	
	public static final int IND_SWCASADO = 39;
	
	public static final int IND_CTIPOAPORT = 40;
	public static final int IND_INDTABEXP = 41;
	
	public static final int IND_GRUPO_ACTIVO_PASIVO = 42;
	public static final int IND_TABLASCONVERSION_ASEGURADO = 43;
	public static final int IND_FACTOR_INTERPOLEXPERIENCIA_ROSSP = 44;
	public static final int IND_METODO_PT_BEL = 45;
	public static final int IND_PER_TRANS_BEL = 46;
	public static final int IND_FACTOR_INTERPOLEXPERIENCIA_BEL = 47;
	public static final int IND_TABLA_BASEEXP = 48;
	
	public static final int IND_FACTOR1_2 = 49;
	public static final int IND_FACTOR2_2 = 50;
	public static final int IND_FACTOR1_3 = 51;
	public static final int IND_FACTOR2_3 = 52;
	public static final int IND_FACTOR1_4 = 53;
	public static final int IND_FACTOR2_4 = 54;
	public static final int IND_FACTOR1_5 = 55;
	public static final int IND_FACTOR2_5 = 56;
	
	public static final int IND_KRAMO = 57;
	public static final int IND_TABLA_BASEEXP_LIST = 58;

	@PortableProperty(IND_BASETEC)
	private String baseTec;

	@PortableProperty(IND_CCARTERA)
	private Integer ccartera;
	// Parte de la clave de la umic
	@PortableProperty(IND_CURVA_TI)
	private String curvaTi;
	// Lista de 5 valores
	@PortableProperty(IND_EDADCALC)
	private List<BigDecimal> edadcalc;

	// Factor 1 (%) aplicado sobre la tabla base
	@PortableProperty(IND_FACTOR1)
	private BigDecimal factor1;
	//Factor 2 (%) para t(0-9) aplicado sobre la tabla base
	@PortableProperty(IND_FACTOR2)
	private List<BigDecimal> factor2;
	
	// Factor 1 (%) aplicado sobre la tabla base para el segundo asegurado
    @PortableProperty(IND_FACTOR1_2)
    private BigDecimal factor1_2;
    //Factor 2 (%) para t(0-9) aplicado sobre la tabla base para el segundo asegurado
    @PortableProperty(IND_FACTOR2_2)
    private List<BigDecimal> factor2_2;

    // Factor 1 (%) aplicado sobre la tabla base para el tercer asegurado
    @PortableProperty(IND_FACTOR1_3)
    private BigDecimal factor1_3;
    //Factor 2 (%) para t(0-9) aplicado sobre la tabla base para el tercer asegurado
    @PortableProperty(IND_FACTOR2_3)
    private List<BigDecimal> factor2_3;

    // Factor 1 (%) aplicado sobre la tabla base para el cuarto asegurado
    @PortableProperty(IND_FACTOR1_4)
    private BigDecimal factor1_4;
    //Factor 2 (%) para t(0-9) aplicado sobre la tabla base para el cuarto asegurado
    @PortableProperty(IND_FACTOR2_4)
    private List<BigDecimal> factor2_4;

    // Factor 1 (%) aplicado sobre la tabla base para el quinto asegurado
    @PortableProperty(IND_FACTOR1_5)
    private BigDecimal factor1_5;
    //Factor 2 (%) para t(0-9) aplicado sobre la tabla base para el quinto asegurado
    @PortableProperty(IND_FACTOR2_5)
    private List<BigDecimal> factor2_5;


	@PortableProperty(IND_FECCIERRE)
	private Timestamp fecCierre;

	@PortableProperty(IND_FTABLA_AN)
	private Timestamp ftablaAn;

	@PortableProperty(IND_FCURVA_TI)
	private Timestamp fcurvaTi;

	// Lista de 5 valores. No se mapea
	@PortableProperty(IND_FECFINTRAMO)
	private List<Timestamp> fecfintramo;

	// Lista de 5 valores. No se mapea
	@PortableProperty(IND_FECINITRAMO)
	private List<Timestamp> fecInitramo;

	@PortableProperty(IND_FITROSSP)
	private Timestamp fitrossp;

	@PortableProperty(IND_GTO_PROV)
	private BigDecimal gtoprov;

	@PortableProperty(IND_GTOROSSP_CAP)
	private BigDecimal gtorosspCap;

	@PortableProperty(IND_GTOROSSP_PRIMA)
	private BigDecimal gtorosspPrima;

	@PortableProperty(IND_GTOROSSP_PROV)
	private BigDecimal gtorosspProv;

	@PortableProperty(IND_GTO_UNI)
	private BigDecimal gtoUni;

	@PortableProperty(IND_ITCALC)
	private List<BigDecimal> itcalc;

	@PortableProperty(IND_UMICKEY)
	private UmicKey umicKey;

	@PortableProperty(IND_METODO_PT_ROSSP)
	private String metodoPtRossp;

	@PortableProperty(IND_PER_TRANS_ROSSP)
	private Boolean perTransRossp;

	@PortableProperty(IND_TABLACALC1ASEG1)
	private String tablacalc1aseg1;
	
	@PortableProperty(IND_TABLACALC2ASEG1)
	private String tablacalc2aseg1;
	
	@PortableProperty(IND_TABLACALC3ASEG1)
	private String tablacalc3aseg1;
	
	@PortableProperty(IND_TABLACALC1ASEG2)
	private String tablacalc1aseg2;
	
	@PortableProperty(IND_TABLACALC2ASEG2)
	private String tablacalc2aseg2;
	
	@PortableProperty(IND_TABLACALC3ASEG2)
	private String tablacalc3aseg2;
	
	@PortableProperty(IND_TABLACALC1ASEG3)
	private String tablacalc1aseg3;
	
	@PortableProperty(IND_TABLACALC2ASEG3)
	private String tablacalc2aseg3;
	
	@PortableProperty(IND_TABLACALC3ASEG3)
	private String tablacalc3aseg3;
	
	@PortableProperty(IND_TABLACALC1ASEG4)
	private String tablacalc1aseg4;
	
	@PortableProperty(IND_TABLACALC2ASEG4)
	private String tablacalc2aseg4;
	
	@PortableProperty(IND_TABLACALC3ASEG4)
	private String tablacalc3aseg4;
	
	@PortableProperty(IND_TABLACALC1ASEG5)
	private String tablacalc1aseg5;
	
	@PortableProperty(IND_TABLACALC2ASEG5)
	private String tablacalc2aseg5;
	
	@PortableProperty(IND_TABLACALC3ASEG5)
	private String tablacalc3aseg5;

	@PortableProperty(IND_TABLA_TANUL)
	private String tablaTanul;
	
	@PortableProperty(IND_CCANAL)
	private Integer ccanal;
	
	@PortableProperty(IND_CNEGOCIO)
	private String cnegocio;
	
	// Lista de 5 valores que no se mapean
	@PortableProperty(IND_SWCASADO)
	private List<String> swcasado;
	
	// No se exporta a fichero
	@PortableProperty(IND_CTIPOAPORT)
	private String ctipoaport;

	// No se exporta a fichero
	@PortableProperty(IND_INDTABEXP)
	private char indTabExp;
	
	@PortableProperty(IND_GRUPO_ACTIVO_PASIVO)
	private List<String> grupoActivoPasivo;
	
	@PortableProperty(IND_TABLASCONVERSION_ASEGURADO)
	private List<List<TablaConversion>> tablasConversionAsegurado;
	
	@PortableProperty(IND_FACTOR_INTERPOLEXPERIENCIA_ROSSP)
	private BigDecimal factorInterpolExperienciaRossp;
	
	@PortableProperty(IND_METODO_PT_BEL)
	private String metodoPtBel;
	
	@PortableProperty(IND_PER_TRANS_BEL)
	private Boolean perTransBel;
	
	@PortableProperty(IND_FACTOR_INTERPOLEXPERIENCIA_BEL)
	private BigDecimal factorInterpolInteresesBel;

	@PortableProperty(IND_TABLA_BASEEXP)
	private List<Integer> tablaBaseExp;
	
	@PortableProperty(IND_KRAMO)
	private String kramo;
	
	@PortableProperty(IND_TABLA_BASEEXP_LIST)
	private List<List<Integer>> tablaBaseExpList;
	
	@Override
	public DetalleBaseTecnicaKey getKey() {
		return new DetalleBaseTecnicaKey(umicKey, baseTec);
	}

	public String getBaseTec() {
		return baseTec;
	}

	public void setBaseTec(String baseTec) {
		this.baseTec = baseTec;
	}

	public Integer getCcartera() {
		return ccartera;
	}

	public void setCcartera(Integer ccartera) {
		this.ccartera = ccartera;
	}

	public String getCurvaTi() {
		return curvaTi;
	}

	public void setCurvaTi(String curvaTi) {
		this.curvaTi = curvaTi;
	}

	public Timestamp getFtablaAn() {
		return ftablaAn;
	}

	public void setFtablaAn(Timestamp ftablaAn) {
		this.ftablaAn = ftablaAn;
	}

	public List<BigDecimal> getEdadcalc() {
		return edadcalc;
	}

	public void setEdadcalc(List<BigDecimal> edadcalc) {
		this.edadcalc = edadcalc;
	}

	public BigDecimal getFactor1() {
		return factor1;
	}

	public void setFactor1(BigDecimal factor1) {
		this.factor1 = factor1;
	}

	public List<BigDecimal> getFactor2() {
		return factor2;
	}

	public void setFactor2(List<BigDecimal> factor2) {
		this.factor2 = factor2;
	}

	public Timestamp getFecCierre() {
		return fecCierre;
	}

	public void setFecCierre(Timestamp fecCierre) {
		this.fecCierre = fecCierre;
	}


	public Timestamp getFcurvaTi() {
		return fcurvaTi;
	}

	public void setFcurvaTi(Timestamp fcurvaTi) {
		this.fcurvaTi = fcurvaTi;
	}

	public List<Timestamp> getFecfintramo() {
		return fecfintramo;
	}

	public void setFecfintramo(List<Timestamp> fecfintramo) {
		this.fecfintramo = fecfintramo;
	}

	public List<Timestamp> getFecInitramo() {
		return fecInitramo;
	}

	public void setFecInitramo(List<Timestamp> fecInitramo) {
		this.fecInitramo = fecInitramo;
	}

	public Timestamp getFitrossp() {
		return fitrossp;
	}

	public void setFitrossp(Timestamp fitrossp) {
		this.fitrossp = fitrossp;
	}

	public BigDecimal getGtoprov() {
		return gtoprov;
	}

	public void setGtoprov(BigDecimal gtoprov) {
		this.gtoprov = gtoprov;
	}

	public BigDecimal getGtorosspCap() {
		return gtorosspCap;
	}

	public void setGtorosspCap(BigDecimal gtorosspCap) {
		this.gtorosspCap = gtorosspCap;
	}

	public BigDecimal getGtorosspPrima() {
		return gtorosspPrima;
	}

	public void setGtorosspPrima(BigDecimal gtorosspPrima) {
		this.gtorosspPrima = gtorosspPrima;
	}

	public BigDecimal getGtorosspProv() {
		return gtorosspProv;
	}

	public void setGtorosspProv(BigDecimal gtorosspProv) {
		this.gtorosspProv = gtorosspProv;
	}

	public BigDecimal getGtoUni() {
		return gtoUni;
	}

	public void setGtoUni(BigDecimal gtoUni) {
		this.gtoUni = gtoUni;
	}

	public List<BigDecimal> getItcalc() {
		return itcalc;
	}

	public void setItcalc(List<BigDecimal> itcalc) {
		this.itcalc = itcalc;
	}

	public UmicKey getUmicKey() {
		return umicKey;
	}

	public void setUmicKey(UmicKey umicKey) {
		this.umicKey = umicKey;
	}

	public String getMetodoPtRossp() {
		return metodoPtRossp;
	}

	public void setMetodoPtRossp(String metodoPtRossp) {
		this.metodoPtRossp = metodoPtRossp;
	}

	public Boolean getPerTransRossp() {
		return perTransRossp;
	}

	public void setPerTransRossp(Boolean perTransRossp) {
		this.perTransRossp = perTransRossp;
	}

	public String getTablacalc1aseg1() {
		return tablacalc1aseg1;
	}

	public void setTablacalc1aseg1(String tablacalc1aseg1) {
		this.tablacalc1aseg1 = tablacalc1aseg1;
	}

	public String getTablacalc2aseg1() {
		return tablacalc2aseg1;
	}

	public void setTablacalc2aseg1(String tablacalc2aseg1) {
		this.tablacalc2aseg1 = tablacalc2aseg1;
	}

	public String getTablacalc3aseg1() {
		return tablacalc3aseg1;
	}

	public void setTablacalc3aseg1(String tablacalc3aseg1) {
		this.tablacalc3aseg1 = tablacalc3aseg1;
	}

	public String getTablacalc1aseg2() {
		return tablacalc1aseg2;
	}

	public void setTablacalc1aseg2(String tablacalc1aseg2) {
		this.tablacalc1aseg2 = tablacalc1aseg2;
	}

	public String getTablacalc2aseg2() {
		return tablacalc2aseg2;
	}

	public void setTablacalc2aseg2(String tablacalc2aseg2) {
		this.tablacalc2aseg2 = tablacalc2aseg2;
	}

	public String getTablacalc3aseg2() {
		return tablacalc3aseg2;
	}

	public void setTablacalc3aseg2(String tablacalc3aseg2) {
		this.tablacalc3aseg2 = tablacalc3aseg2;
	}

	public String getTablacalc1aseg3() {
		return tablacalc1aseg3;
	}

	public void setTablacalc1aseg3(String tablacalc1aseg3) {
		this.tablacalc1aseg3 = tablacalc1aseg3;
	}

	public String getTablacalc2aseg3() {
		return tablacalc2aseg3;
	}

	public void setTablacalc2aseg3(String tablacalc2aseg3) {
		this.tablacalc2aseg3 = tablacalc2aseg3;
	}

	public String getTablacalc3aseg3() {
		return tablacalc3aseg3;
	}

	public void setTablacalc3aseg3(String tablacalc3aseg3) {
		this.tablacalc3aseg3 = tablacalc3aseg3;
	}

	public String getTablacalc1aseg4() {
		return tablacalc1aseg4;
	}

	public void setTablacalc1aseg4(String tablacalc1aseg4) {
		this.tablacalc1aseg4 = tablacalc1aseg4;
	}

	public String getTablacalc2aseg4() {
		return tablacalc2aseg4;
	}

	public void setTablacalc2aseg4(String tablacalc2aseg4) {
		this.tablacalc2aseg4 = tablacalc2aseg4;
	}

	public String getTablacalc3aseg4() {
		return tablacalc3aseg4;
	}

	public void setTablacalc3aseg4(String tablacalc3aseg4) {
		this.tablacalc3aseg4 = tablacalc3aseg4;
	}

	public String getTablacalc1aseg5() {
		return tablacalc1aseg5;
	}

	public void setTablacalc1aseg5(String tablacalc1aseg5) {
		this.tablacalc1aseg5 = tablacalc1aseg5;
	}

	public String getTablacalc2aseg5() {
		return tablacalc2aseg5;
	}

	public void setTablacalc2aseg5(String tablacalc2aseg5) {
		this.tablacalc2aseg5 = tablacalc2aseg5;
	}

	public String getTablacalc3aseg5() {
		return tablacalc3aseg5;
	}

	public void setTablacalc3aseg5(String tablacalc3aseg5) {
		this.tablacalc3aseg5 = tablacalc3aseg5;
	}

	public String getTablaTanul() {
		return tablaTanul;
	}

	public void setTablaTanul(String tablaTanul) {
		this.tablaTanul = tablaTanul;
	}

	public Integer getCcanal() {
		return ccanal;
	}

	public void setCcanal(Integer ccanal) {
		this.ccanal = ccanal;
	}

	public String getCnegocio() {
		return cnegocio;
	}

	public void setCnegocio(String cnegocio) {
		this.cnegocio = cnegocio;
	}
	
	public List<String> getSwcasado() {
		return swcasado;
	}

	public void setSwcasado(List<String> swcasado) {
		this.swcasado = swcasado;
	}

	public String getCtipoaport() {
		return ctipoaport;
	}

	public void setCtipoaport(String ctipoaport) {
		this.ctipoaport = ctipoaport;
	}

	public char getIndTabExp() {
		return indTabExp;
	}

	public void setIndTabExp(char indTabExp) {
		this.indTabExp = indTabExp;
	}

	@Override
	public String getBt() {
		return getBaseTec();
	}

	@Override
	public void setBt(String baseTec) {
		setBaseTec(baseTec);
	}
	
	public List<String> getGrupoActivoPasivo() {
		return grupoActivoPasivo;
	}

	public void setGrupoActivoPasivo(List<String> grupoActivoPasivo) {
		this.grupoActivoPasivo = grupoActivoPasivo;
	}

	public List<List<TablaConversion>> getTablasConversionAsegurado() {
		return tablasConversionAsegurado;
	}

	public void setTablasConversionAsegurado(
			List<List<TablaConversion>> tablasConversionAsegurado) {
		this.tablasConversionAsegurado = tablasConversionAsegurado;
	}

	public BigDecimal getFactorInterpolExperienciaRossp() {
		return factorInterpolExperienciaRossp;
	}

	public void setFactorInterpolExperienciaRossp(
			BigDecimal factorInterpolExperienciaRossp) {
		this.factorInterpolExperienciaRossp = factorInterpolExperienciaRossp;
	}

	public String getMetodoPtBel() {
		return metodoPtBel;
	}

	public void setMetodoPtBel(String metodoPtBel) {
		this.metodoPtBel = metodoPtBel;
	}

	public Boolean getPerTransBel() {
		return perTransBel;
	}

	public void setPerTransBel(Boolean perTransBel) {
		this.perTransBel = perTransBel;
	}

	public BigDecimal getFactorInterpolInteresesBel() {
		return factorInterpolInteresesBel;
	}

	public void setFactorInterpolInteresesBel(
			BigDecimal factorInterpolInteresesBel) {
		this.factorInterpolInteresesBel = factorInterpolInteresesBel;
	}

	public List<Integer> getTablaBaseExp() {
		return tablaBaseExp;
	}

	public void setTablaBaseExp(List<Integer> tablaBaseExp) {
		this.tablaBaseExp = tablaBaseExp;
	}

	public BigDecimal getFactor1_2() {
		return factor1_2;
	}

	public void setFactor1_2(BigDecimal factor1_2) {
		this.factor1_2 = factor1_2;
	}

	public List<BigDecimal> getFactor2_2() {
		return factor2_2;
	}

	public void setFactor2_2(List<BigDecimal> factor2_2) {
		this.factor2_2 = factor2_2;
	}

	public BigDecimal getFactor1_3() {
		return factor1_3;
	}

	public void setFactor1_3(BigDecimal factor1_3) {
		this.factor1_3 = factor1_3;
	}

	public List<BigDecimal> getFactor2_3() {
		return factor2_3;
	}

	public void setFactor2_3(List<BigDecimal> factor2_3) {
		this.factor2_3 = factor2_3;
	}

	public BigDecimal getFactor1_4() {
		return factor1_4;
	}

	public void setFactor1_4(BigDecimal factor1_4) {
		this.factor1_4 = factor1_4;
	}

	public List<BigDecimal> getFactor2_4() {
		return factor2_4;
	}

	public void setFactor2_4(List<BigDecimal> factor2_4) {
		this.factor2_4 = factor2_4;
	}

	public BigDecimal getFactor1_5() {
		return factor1_5;
	}

	public void setFactor1_5(BigDecimal factor1_5) {
		this.factor1_5 = factor1_5;
	}

	public List<BigDecimal> getFactor2_5() {
		return factor2_5;
	}

	public void setFactor2_5(List<BigDecimal> factor2_5) {
		this.factor2_5 = factor2_5;
	}

	
	public String getKramo() {
		return kramo;
	}

	public void setKramo(String kramo) {
		this.kramo = kramo;
	}
	
	public List<List<Integer>> getTablaBaseExpList() {
		return tablaBaseExpList;
	}

	public void setTablaBaseExpList(List<List<Integer>> tablaBaseExpList) {
		this.tablaBaseExpList = tablaBaseExpList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseTec == null) ? 0 : baseTec.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((ccartera == null) ? 0 : ccartera.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((ctipoaport == null) ? 0 : ctipoaport.hashCode());
		result = prime * result + ((curvaTi == null) ? 0 : curvaTi.hashCode());
		result = prime * result + ((edadcalc == null) ? 0 : edadcalc.hashCode());
		result = prime * result + ((factor1 == null) ? 0 : factor1.hashCode());
		result = prime * result + ((factor1_2 == null) ? 0 : factor1_2.hashCode());
		result = prime * result + ((factor1_3 == null) ? 0 : factor1_3.hashCode());
		result = prime * result + ((factor1_4 == null) ? 0 : factor1_4.hashCode());
		result = prime * result + ((factor1_5 == null) ? 0 : factor1_5.hashCode());
		result = prime * result + ((factor2 == null) ? 0 : factor2.hashCode());
		result = prime * result + ((factor2_2 == null) ? 0 : factor2_2.hashCode());
		result = prime * result + ((factor2_3 == null) ? 0 : factor2_3.hashCode());
		result = prime * result + ((factor2_4 == null) ? 0 : factor2_4.hashCode());
		result = prime * result + ((factor2_5 == null) ? 0 : factor2_5.hashCode());
		result = prime * result
				+ ((factorInterpolExperienciaRossp == null) ? 0 : factorInterpolExperienciaRossp.hashCode());
		result = prime * result + ((factorInterpolInteresesBel == null) ? 0 : factorInterpolInteresesBel.hashCode());
		result = prime * result + ((fcurvaTi == null) ? 0 : fcurvaTi.hashCode());
		result = prime * result + ((fecCierre == null) ? 0 : fecCierre.hashCode());
		result = prime * result + ((fecInitramo == null) ? 0 : fecInitramo.hashCode());
		result = prime * result + ((fecfintramo == null) ? 0 : fecfintramo.hashCode());
		result = prime * result + ((fitrossp == null) ? 0 : fitrossp.hashCode());
		result = prime * result + ((ftablaAn == null) ? 0 : ftablaAn.hashCode());
		result = prime * result + ((grupoActivoPasivo == null) ? 0 : grupoActivoPasivo.hashCode());
		result = prime * result + ((gtoUni == null) ? 0 : gtoUni.hashCode());
		result = prime * result + ((gtoprov == null) ? 0 : gtoprov.hashCode());
		result = prime * result + ((gtorosspCap == null) ? 0 : gtorosspCap.hashCode());
		result = prime * result + ((gtorosspPrima == null) ? 0 : gtorosspPrima.hashCode());
		result = prime * result + ((gtorosspProv == null) ? 0 : gtorosspProv.hashCode());
		result = prime * result + indTabExp;
		result = prime * result + ((itcalc == null) ? 0 : itcalc.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((metodoPtBel == null) ? 0 : metodoPtBel.hashCode());
		result = prime * result + ((metodoPtRossp == null) ? 0 : metodoPtRossp.hashCode());
		result = prime * result + ((perTransBel == null) ? 0 : perTransBel.hashCode());
		result = prime * result + ((perTransRossp == null) ? 0 : perTransRossp.hashCode());
		result = prime * result + ((swcasado == null) ? 0 : swcasado.hashCode());
		result = prime * result + ((tablaBaseExp == null) ? 0 : tablaBaseExp.hashCode());
		result = prime * result + ((tablaBaseExpList == null) ? 0 : tablaBaseExpList.hashCode());
		result = prime * result + ((tablaTanul == null) ? 0 : tablaTanul.hashCode());
		result = prime * result + ((tablacalc1aseg1 == null) ? 0 : tablacalc1aseg1.hashCode());
		result = prime * result + ((tablacalc1aseg2 == null) ? 0 : tablacalc1aseg2.hashCode());
		result = prime * result + ((tablacalc1aseg3 == null) ? 0 : tablacalc1aseg3.hashCode());
		result = prime * result + ((tablacalc1aseg4 == null) ? 0 : tablacalc1aseg4.hashCode());
		result = prime * result + ((tablacalc1aseg5 == null) ? 0 : tablacalc1aseg5.hashCode());
		result = prime * result + ((tablacalc2aseg1 == null) ? 0 : tablacalc2aseg1.hashCode());
		result = prime * result + ((tablacalc2aseg2 == null) ? 0 : tablacalc2aseg2.hashCode());
		result = prime * result + ((tablacalc2aseg3 == null) ? 0 : tablacalc2aseg3.hashCode());
		result = prime * result + ((tablacalc2aseg4 == null) ? 0 : tablacalc2aseg4.hashCode());
		result = prime * result + ((tablacalc2aseg5 == null) ? 0 : tablacalc2aseg5.hashCode());
		result = prime * result + ((tablacalc3aseg1 == null) ? 0 : tablacalc3aseg1.hashCode());
		result = prime * result + ((tablacalc3aseg2 == null) ? 0 : tablacalc3aseg2.hashCode());
		result = prime * result + ((tablacalc3aseg3 == null) ? 0 : tablacalc3aseg3.hashCode());
		result = prime * result + ((tablacalc3aseg4 == null) ? 0 : tablacalc3aseg4.hashCode());
		result = prime * result + ((tablacalc3aseg5 == null) ? 0 : tablacalc3aseg5.hashCode());
		result = prime * result + ((tablasConversionAsegurado == null) ? 0 : tablasConversionAsegurado.hashCode());
		result = prime * result + ((umicKey == null) ? 0 : umicKey.hashCode());
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
		DetalleBaseTecnica other = (DetalleBaseTecnica) obj;
		if (baseTec == null) {
			if (other.baseTec != null)
				return false;
		} else if (!baseTec.equals(other.baseTec))
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
		if (ctipoaport == null) {
			if (other.ctipoaport != null)
				return false;
		} else if (!ctipoaport.equals(other.ctipoaport))
			return false;
		if (curvaTi == null) {
			if (other.curvaTi != null)
				return false;
		} else if (!curvaTi.equals(other.curvaTi))
			return false;
		if (edadcalc == null) {
			if (other.edadcalc != null)
				return false;
		} else if (!edadcalc.equals(other.edadcalc))
			return false;
		if (factor1 == null) {
			if (other.factor1 != null)
				return false;
		} else if (!factor1.equals(other.factor1))
			return false;
		if (factor1_2 == null) {
			if (other.factor1_2 != null)
				return false;
		} else if (!factor1_2.equals(other.factor1_2))
			return false;
		if (factor1_3 == null) {
			if (other.factor1_3 != null)
				return false;
		} else if (!factor1_3.equals(other.factor1_3))
			return false;
		if (factor1_4 == null) {
			if (other.factor1_4 != null)
				return false;
		} else if (!factor1_4.equals(other.factor1_4))
			return false;
		if (factor1_5 == null) {
			if (other.factor1_5 != null)
				return false;
		} else if (!factor1_5.equals(other.factor1_5))
			return false;
		if (factor2 == null) {
			if (other.factor2 != null)
				return false;
		} else if (!factor2.equals(other.factor2))
			return false;
		if (factor2_2 == null) {
			if (other.factor2_2 != null)
				return false;
		} else if (!factor2_2.equals(other.factor2_2))
			return false;
		if (factor2_3 == null) {
			if (other.factor2_3 != null)
				return false;
		} else if (!factor2_3.equals(other.factor2_3))
			return false;
		if (factor2_4 == null) {
			if (other.factor2_4 != null)
				return false;
		} else if (!factor2_4.equals(other.factor2_4))
			return false;
		if (factor2_5 == null) {
			if (other.factor2_5 != null)
				return false;
		} else if (!factor2_5.equals(other.factor2_5))
			return false;
		if (factorInterpolExperienciaRossp == null) {
			if (other.factorInterpolExperienciaRossp != null)
				return false;
		} else if (!factorInterpolExperienciaRossp.equals(other.factorInterpolExperienciaRossp))
			return false;
		if (factorInterpolInteresesBel == null) {
			if (other.factorInterpolInteresesBel != null)
				return false;
		} else if (!factorInterpolInteresesBel.equals(other.factorInterpolInteresesBel))
			return false;
		if (fcurvaTi == null) {
			if (other.fcurvaTi != null)
				return false;
		} else if (!fcurvaTi.equals(other.fcurvaTi))
			return false;
		if (fecCierre == null) {
			if (other.fecCierre != null)
				return false;
		} else if (!fecCierre.equals(other.fecCierre))
			return false;
		if (fecInitramo == null) {
			if (other.fecInitramo != null)
				return false;
		} else if (!fecInitramo.equals(other.fecInitramo))
			return false;
		if (fecfintramo == null) {
			if (other.fecfintramo != null)
				return false;
		} else if (!fecfintramo.equals(other.fecfintramo))
			return false;
		if (fitrossp == null) {
			if (other.fitrossp != null)
				return false;
		} else if (!fitrossp.equals(other.fitrossp))
			return false;
		if (ftablaAn == null) {
			if (other.ftablaAn != null)
				return false;
		} else if (!ftablaAn.equals(other.ftablaAn))
			return false;
		if (grupoActivoPasivo == null) {
			if (other.grupoActivoPasivo != null)
				return false;
		} else if (!grupoActivoPasivo.equals(other.grupoActivoPasivo))
			return false;
		if (gtoUni == null) {
			if (other.gtoUni != null)
				return false;
		} else if (!gtoUni.equals(other.gtoUni))
			return false;
		if (gtoprov == null) {
			if (other.gtoprov != null)
				return false;
		} else if (!gtoprov.equals(other.gtoprov))
			return false;
		if (gtorosspCap == null) {
			if (other.gtorosspCap != null)
				return false;
		} else if (!gtorosspCap.equals(other.gtorosspCap))
			return false;
		if (gtorosspPrima == null) {
			if (other.gtorosspPrima != null)
				return false;
		} else if (!gtorosspPrima.equals(other.gtorosspPrima))
			return false;
		if (gtorosspProv == null) {
			if (other.gtorosspProv != null)
				return false;
		} else if (!gtorosspProv.equals(other.gtorosspProv))
			return false;
		if (indTabExp != other.indTabExp)
			return false;
		if (itcalc == null) {
			if (other.itcalc != null)
				return false;
		} else if (!itcalc.equals(other.itcalc))
			return false;
		if (kramo == null) {
			if (other.kramo != null)
				return false;
		} else if (!kramo.equals(other.kramo))
			return false;
		if (metodoPtBel == null) {
			if (other.metodoPtBel != null)
				return false;
		} else if (!metodoPtBel.equals(other.metodoPtBel))
			return false;
		if (metodoPtRossp == null) {
			if (other.metodoPtRossp != null)
				return false;
		} else if (!metodoPtRossp.equals(other.metodoPtRossp))
			return false;
		if (perTransBel == null) {
			if (other.perTransBel != null)
				return false;
		} else if (!perTransBel.equals(other.perTransBel))
			return false;
		if (perTransRossp == null) {
			if (other.perTransRossp != null)
				return false;
		} else if (!perTransRossp.equals(other.perTransRossp))
			return false;
		if (swcasado == null) {
			if (other.swcasado != null)
				return false;
		} else if (!swcasado.equals(other.swcasado))
			return false;
		if (tablaBaseExp == null) {
			if (other.tablaBaseExp != null)
				return false;
		} else if (!tablaBaseExp.equals(other.tablaBaseExp))
			return false;
		if (tablaBaseExpList == null) {
			if (other.tablaBaseExpList != null)
				return false;
		} else if (!tablaBaseExpList.equals(other.tablaBaseExpList))
			return false;
		if (tablaTanul == null) {
			if (other.tablaTanul != null)
				return false;
		} else if (!tablaTanul.equals(other.tablaTanul))
			return false;
		if (tablacalc1aseg1 == null) {
			if (other.tablacalc1aseg1 != null)
				return false;
		} else if (!tablacalc1aseg1.equals(other.tablacalc1aseg1))
			return false;
		if (tablacalc1aseg2 == null) {
			if (other.tablacalc1aseg2 != null)
				return false;
		} else if (!tablacalc1aseg2.equals(other.tablacalc1aseg2))
			return false;
		if (tablacalc1aseg3 == null) {
			if (other.tablacalc1aseg3 != null)
				return false;
		} else if (!tablacalc1aseg3.equals(other.tablacalc1aseg3))
			return false;
		if (tablacalc1aseg4 == null) {
			if (other.tablacalc1aseg4 != null)
				return false;
		} else if (!tablacalc1aseg4.equals(other.tablacalc1aseg4))
			return false;
		if (tablacalc1aseg5 == null) {
			if (other.tablacalc1aseg5 != null)
				return false;
		} else if (!tablacalc1aseg5.equals(other.tablacalc1aseg5))
			return false;
		if (tablacalc2aseg1 == null) {
			if (other.tablacalc2aseg1 != null)
				return false;
		} else if (!tablacalc2aseg1.equals(other.tablacalc2aseg1))
			return false;
		if (tablacalc2aseg2 == null) {
			if (other.tablacalc2aseg2 != null)
				return false;
		} else if (!tablacalc2aseg2.equals(other.tablacalc2aseg2))
			return false;
		if (tablacalc2aseg3 == null) {
			if (other.tablacalc2aseg3 != null)
				return false;
		} else if (!tablacalc2aseg3.equals(other.tablacalc2aseg3))
			return false;
		if (tablacalc2aseg4 == null) {
			if (other.tablacalc2aseg4 != null)
				return false;
		} else if (!tablacalc2aseg4.equals(other.tablacalc2aseg4))
			return false;
		if (tablacalc2aseg5 == null) {
			if (other.tablacalc2aseg5 != null)
				return false;
		} else if (!tablacalc2aseg5.equals(other.tablacalc2aseg5))
			return false;
		if (tablacalc3aseg1 == null) {
			if (other.tablacalc3aseg1 != null)
				return false;
		} else if (!tablacalc3aseg1.equals(other.tablacalc3aseg1))
			return false;
		if (tablacalc3aseg2 == null) {
			if (other.tablacalc3aseg2 != null)
				return false;
		} else if (!tablacalc3aseg2.equals(other.tablacalc3aseg2))
			return false;
		if (tablacalc3aseg3 == null) {
			if (other.tablacalc3aseg3 != null)
				return false;
		} else if (!tablacalc3aseg3.equals(other.tablacalc3aseg3))
			return false;
		if (tablacalc3aseg4 == null) {
			if (other.tablacalc3aseg4 != null)
				return false;
		} else if (!tablacalc3aseg4.equals(other.tablacalc3aseg4))
			return false;
		if (tablacalc3aseg5 == null) {
			if (other.tablacalc3aseg5 != null)
				return false;
		} else if (!tablacalc3aseg5.equals(other.tablacalc3aseg5))
			return false;
		if (tablasConversionAsegurado == null) {
			if (other.tablasConversionAsegurado != null)
				return false;
		} else if (!tablasConversionAsegurado.equals(other.tablasConversionAsegurado))
			return false;
		if (umicKey == null) {
			if (other.umicKey != null)
				return false;
		} else if (!umicKey.equals(other.umicKey))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DetalleBaseTecnica [baseTec=" + baseTec + ", ccartera=" + ccartera + ", curvaTi=" + curvaTi
				+ ", edadcalc=" + edadcalc + ", factor1=" + factor1 + ", factor2=" + factor2 + ", factor1_2="
				+ factor1_2 + ", factor2_2=" + factor2_2 + ", factor1_3=" + factor1_3 + ", factor2_3=" + factor2_3
				+ ", factor1_4=" + factor1_4 + ", factor2_4=" + factor2_4 + ", factor1_5=" + factor1_5 + ", factor2_5="
				+ factor2_5 + ", fecCierre=" + fecCierre + ", ftablaAn=" + ftablaAn + ", fcurvaTi=" + fcurvaTi
				+ ", fecfintramo=" + fecfintramo + ", fecInitramo=" + fecInitramo + ", fitrossp=" + fitrossp
				+ ", gtoprov=" + gtoprov + ", gtorosspCap=" + gtorosspCap + ", gtorosspPrima=" + gtorosspPrima
				+ ", gtorosspProv=" + gtorosspProv + ", gtoUni=" + gtoUni + ", itcalc=" + itcalc + ", umicKey="
				+ umicKey + ", metodoPtRossp=" + metodoPtRossp + ", perTransRossp=" + perTransRossp
				+ ", tablacalc1aseg1=" + tablacalc1aseg1 + ", tablacalc2aseg1=" + tablacalc2aseg1 + ", tablacalc3aseg1="
				+ tablacalc3aseg1 + ", tablacalc1aseg2=" + tablacalc1aseg2 + ", tablacalc2aseg2=" + tablacalc2aseg2
				+ ", tablacalc3aseg2=" + tablacalc3aseg2 + ", tablacalc1aseg3=" + tablacalc1aseg3 + ", tablacalc2aseg3="
				+ tablacalc2aseg3 + ", tablacalc3aseg3=" + tablacalc3aseg3 + ", tablacalc1aseg4=" + tablacalc1aseg4
				+ ", tablacalc2aseg4=" + tablacalc2aseg4 + ", tablacalc3aseg4=" + tablacalc3aseg4 + ", tablacalc1aseg5="
				+ tablacalc1aseg5 + ", tablacalc2aseg5=" + tablacalc2aseg5 + ", tablacalc3aseg5=" + tablacalc3aseg5
				+ ", tablaTanul=" + tablaTanul + ", ccanal=" + ccanal + ", cnegocio=" + cnegocio + ", swcasado="
				+ swcasado + ", ctipoaport=" + ctipoaport + ", indTabExp=" + indTabExp + ", grupoActivoPasivo="
				+ grupoActivoPasivo + ", tablasConversionAsegurado=" + tablasConversionAsegurado
				+ ", factorInterpolExperienciaRossp=" + factorInterpolExperienciaRossp + ", metodoPtBel=" + metodoPtBel
				+ ", perTransBel=" + perTransBel + ", factorInterpolInteresesBel=" + factorInterpolInteresesBel
				+ ", tablaBaseExp=" + tablaBaseExp + ", kramo=" + kramo + ", tablaBaseExpList=" + tablaBaseExpList
				+ "]";
	}

	
	

	


}