package es.mapfre.solvencia.dominio.gbt;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.gbt.ConvTablasExpRealKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class ConvTablasExpReal implements EntidadBase<ConvTablasExpRealKey>{

	public static final int IND_KBASETEC = 0;
	public static final int IND_KCOMPANIA = 1;
	public static final int IND_KNEGOCIO = 2;
	public static final int IND_KRIESGO = 3;
	public static final int IND_KSEXO = 4;
	public static final int IND_KCATEG = 5;
	public static final int IND_KEDADDESDE = 6;
	public static final int IND_KMODALIDAD = 7;
	public static final int IND_KFDESDE = 8;
	public static final int IND_EDADHASTA = 9;
	public static final int IND_FHASTA = 10;
	public static final int IND_CTABBASE = 11;
	public static final int IND_PFACTOR1 = 12;
	public static final int IND_PFACTOR2A = 13;
	public static final int IND_PFACTOR2B = 14;
	public static final int IND_PFACTOR2C = 15;
	public static final int IND_PFACTOR2D = 16;
	public static final int IND_PFACTOR2E = 17;
	public static final int IND_PFACTOR2F = 18;
	public static final int IND_PFACTOR2G = 19;
	public static final int IND_PFACTOR2H = 20;
	public static final int IND_PFACTOR2I = 21;
	public static final int IND_PFACTOR2J = 22;

	@PortableProperty(IND_KBASETEC) private String kbasetec;
	@PortableProperty(IND_KCOMPANIA) private int kcompania;
	@PortableProperty(IND_KNEGOCIO) private String knegocio;
	@PortableProperty(IND_KRIESGO) private String kriesgo;
	@PortableProperty(IND_KSEXO) private String ksexo;
	@PortableProperty(IND_KCATEG) private String kcateg;
	@PortableProperty(IND_KEDADDESDE) private Integer kedaddesde;
	@PortableProperty(IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(IND_KFDESDE) private Timestamp kfdesde;
	@PortableProperty(IND_EDADHASTA) private Integer edadhasta;
	@PortableProperty(IND_FHASTA) private Timestamp fhasta;
	@PortableProperty(IND_CTABBASE) private Integer ctabbase;
	@PortableProperty(IND_PFACTOR1) private BigDecimal pfactor1;
	@PortableProperty(IND_PFACTOR2A) private BigDecimal pfactor2a;
	@PortableProperty(IND_PFACTOR2B) private BigDecimal pfactor2b;
	@PortableProperty(IND_PFACTOR2C) private BigDecimal pfactor2c;
	@PortableProperty(IND_PFACTOR2D) private BigDecimal pfactor2d;
	@PortableProperty(IND_PFACTOR2E) private BigDecimal pfactor2e;
	@PortableProperty(IND_PFACTOR2F) private BigDecimal pfactor2f;
	@PortableProperty(IND_PFACTOR2G) private BigDecimal pfactor2g;
	@PortableProperty(IND_PFACTOR2H) private BigDecimal pfactor2h;
	@PortableProperty(IND_PFACTOR2I) private BigDecimal pfactor2i;
	@PortableProperty(IND_PFACTOR2J) private BigDecimal pfactor2j;	

	public ConvTablasExpReal() {
		super();
	}

	public ConvTablasExpReal(String kbasetec, int kcompania,
			String knegocio, String kriesgo, String ksexo, String kcateg,
			Integer kedaddesde, Integer kmodalidad, Timestamp kfdesde,
			Integer edadhasta, Timestamp fhasta, Integer ctabbase,
			BigDecimal pfactor1, BigDecimal pfactor2a, BigDecimal pfactor2b,
			BigDecimal pfactor2c, BigDecimal pfactor2d, BigDecimal pfactor2e,
			BigDecimal pfactor2f, BigDecimal pfactor2g, BigDecimal pfactor2h,
			BigDecimal pfactor2i, BigDecimal pfactor2j) {
		super();
		this.kbasetec = kbasetec;
		this.kcompania = kcompania;
		this.knegocio = knegocio;
		this.kriesgo = kriesgo;
		this.ksexo = ksexo;
		this.kcateg = kcateg;
		this.kedaddesde = kedaddesde;
		this.kmodalidad = kmodalidad;
		this.kfdesde = kfdesde;
		this.edadhasta = edadhasta;
		this.fhasta = fhasta;
		this.ctabbase = ctabbase;
		this.pfactor1 = pfactor1;
		this.pfactor2a = pfactor2a;
		this.pfactor2b = pfactor2b;
		this.pfactor2c = pfactor2c;
		this.pfactor2d = pfactor2d;
		this.pfactor2e = pfactor2e;
		this.pfactor2f = pfactor2f;
		this.pfactor2g = pfactor2g;
		this.pfactor2h = pfactor2h;
		this.pfactor2i = pfactor2i;
		this.pfactor2j = pfactor2j;
	}

	public String getKbasetec() {
		return kbasetec;
	}

	public void setKbasetec(String kbasetec) {
		this.kbasetec = kbasetec;
	}

	public int getKcompania() {
		return kcompania;
	}

	public void setKcompania(int kcompania) {
		this.kcompania = kcompania;
	}

	public String getKnegocio() {
		return knegocio;
	}

	public void setKnegocio(String knegocio) {
		this.knegocio = knegocio;
	}

	public String getKriesgo() {
		return kriesgo;
	}

	public void setKriesgo(String kriesgo) {
		this.kriesgo = kriesgo;
	}

	public String getKsexo() {
		return ksexo;
	}

	public void setKsexo(String ksexo) {
		this.ksexo = ksexo;
	}

	public String getKcateg() {
		return kcateg;
	}

	public void setKcateg(String kcateg) {
		this.kcateg = kcateg;
	}

	public Integer getKedaddesde() {
		return kedaddesde;
	}

	public void setKedaddesde(Integer kedaddesde) {
		this.kedaddesde = kedaddesde;
	}

	public Integer getKmodalidad() {
		return kmodalidad;
	}

	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}

	public Timestamp getKfdesde() {
		return kfdesde;
	}

	public void setKfdesde(Timestamp kfdesde) {
		this.kfdesde = kfdesde;
	}

	public Integer getEdadhasta() {
		return edadhasta;
	}

	public void setEdadhasta(Integer edadhasta) {
		this.edadhasta = edadhasta;
	}

	public Timestamp getFhasta() {
		return fhasta;
	}

	public void setFhasta(Timestamp fhasta) {
		this.fhasta = fhasta;
	}

	public Integer getCtabbase() {
		return ctabbase;
	}

	public void setCtabbase(Integer ctabbase) {
		this.ctabbase = ctabbase;
	}

	public BigDecimal getPfactor1() {
		return pfactor1;
	}

	public void setPfactor1(BigDecimal pfactor1) {
		this.pfactor1 = pfactor1;
	}

	public BigDecimal getPfactor2a() {
		return pfactor2a;
	}

	public void setPfactor2a(BigDecimal pfactor2a) {
		this.pfactor2a = pfactor2a;
	}

	public BigDecimal getPfactor2b() {
		return pfactor2b;
	}

	public void setPfactor2b(BigDecimal pfactor2b) {
		this.pfactor2b = pfactor2b;
	}

	public BigDecimal getPfactor2c() {
		return pfactor2c;
	}

	public void setPfactor2c(BigDecimal pfactor2c) {
		this.pfactor2c = pfactor2c;
	}

	public BigDecimal getPfactor2d() {
		return pfactor2d;
	}

	public void setPfactor2d(BigDecimal pfactor2d) {
		this.pfactor2d = pfactor2d;
	}

	public BigDecimal getPfactor2e() {
		return pfactor2e;
	}

	public void setPfactor2e(BigDecimal pfactor2e) {
		this.pfactor2e = pfactor2e;
	}

	public BigDecimal getPfactor2f() {
		return pfactor2f;
	}

	public void setPfactor2f(BigDecimal pfactor2f) {
		this.pfactor2f = pfactor2f;
	}

	public BigDecimal getPfactor2g() {
		return pfactor2g;
	}

	public void setPfactor2g(BigDecimal pfactor2g) {
		this.pfactor2g = pfactor2g;
	}

	public BigDecimal getPfactor2h() {
		return pfactor2h;
	}

	public void setPfactor2h(BigDecimal pfactor2h) {
		this.pfactor2h = pfactor2h;
	}

	public BigDecimal getPfactor2i() {
		return pfactor2i;
	}

	public void setPfactor2i(BigDecimal pfactor2i) {
		this.pfactor2i = pfactor2i;
	}

	public BigDecimal getPfactor2j() {
		return pfactor2j;
	}

	public void setPfactor2j(BigDecimal pfactor2j) {
		this.pfactor2j = pfactor2j;
	}

	@Override
	public ConvTablasExpRealKey getKey() {
		return new ConvTablasExpRealKey(kbasetec, kcompania, knegocio, kriesgo, ksexo, kcateg, kedaddesde, kmodalidad, kfdesde);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ctabbase == null) ? 0 : ctabbase.hashCode());
		result = prime * result
				+ ((edadhasta == null) ? 0 : edadhasta.hashCode());
		result = prime * result + ((fhasta == null) ? 0 : fhasta.hashCode());
		result = prime * result
				+ ((kbasetec == null) ? 0 : kbasetec.hashCode());
		result = prime * result + ((kcateg == null) ? 0 : kcateg.hashCode());
		result = prime * result + kcompania;
		result = prime * result
				+ ((kedaddesde == null) ? 0 : kedaddesde.hashCode());
		result = prime * result + ((kfdesde == null) ? 0 : kfdesde.hashCode());
		result = prime * result
				+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result
				+ ((knegocio == null) ? 0 : knegocio.hashCode());
		result = prime * result + ((kriesgo == null) ? 0 : kriesgo.hashCode());
		result = prime * result + ((ksexo == null) ? 0 : ksexo.hashCode());
		result = prime * result
				+ ((pfactor1 == null) ? 0 : pfactor1.hashCode());
		result = prime * result
				+ ((pfactor2a == null) ? 0 : pfactor2a.hashCode());
		result = prime * result
				+ ((pfactor2b == null) ? 0 : pfactor2b.hashCode());
		result = prime * result
				+ ((pfactor2c == null) ? 0 : pfactor2c.hashCode());
		result = prime * result
				+ ((pfactor2d == null) ? 0 : pfactor2d.hashCode());
		result = prime * result
				+ ((pfactor2e == null) ? 0 : pfactor2e.hashCode());
		result = prime * result
				+ ((pfactor2f == null) ? 0 : pfactor2f.hashCode());
		result = prime * result
				+ ((pfactor2g == null) ? 0 : pfactor2g.hashCode());
		result = prime * result
				+ ((pfactor2h == null) ? 0 : pfactor2h.hashCode());
		result = prime * result
				+ ((pfactor2i == null) ? 0 : pfactor2i.hashCode());
		result = prime * result
				+ ((pfactor2j == null) ? 0 : pfactor2j.hashCode());
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
		ConvTablasExpReal other = (ConvTablasExpReal) obj;
		if (ctabbase == null) {
			if (other.ctabbase != null)
				return false;
		} else if (!ctabbase.equals(other.ctabbase))
			return false;
		if (edadhasta == null) {
			if (other.edadhasta != null)
				return false;
		} else if (!edadhasta.equals(other.edadhasta))
			return false;
		if (fhasta == null) {
			if (other.fhasta != null)
				return false;
		} else if (!fhasta.equals(other.fhasta))
			return false;
		if (kbasetec == null) {
			if (other.kbasetec != null)
				return false;
		} else if (!kbasetec.equals(other.kbasetec))
			return false;
		if (kcateg == null) {
			if (other.kcateg != null)
				return false;
		} else if (!kcateg.equals(other.kcateg))
			return false;
		if (kcompania != other.kcompania)
			return false;
		if (kedaddesde == null) {
			if (other.kedaddesde != null)
				return false;
		} else if (!kedaddesde.equals(other.kedaddesde))
			return false;
		if (kfdesde == null) {
			if (other.kfdesde != null)
				return false;
		} else if (!kfdesde.equals(other.kfdesde))
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		if (knegocio == null) {
			if (other.knegocio != null)
				return false;
		} else if (!knegocio.equals(other.knegocio))
			return false;
		if (kriesgo == null) {
			if (other.kriesgo != null)
				return false;
		} else if (!kriesgo.equals(other.kriesgo))
			return false;
		if (ksexo == null) {
			if (other.ksexo != null)
				return false;
		} else if (!ksexo.equals(other.ksexo))
			return false;
		if (pfactor1 == null) {
			if (other.pfactor1 != null)
				return false;
		} else if (!pfactor1.equals(other.pfactor1))
			return false;
		if (pfactor2a == null) {
			if (other.pfactor2a != null)
				return false;
		} else if (!pfactor2a.equals(other.pfactor2a))
			return false;
		if (pfactor2b == null) {
			if (other.pfactor2b != null)
				return false;
		} else if (!pfactor2b.equals(other.pfactor2b))
			return false;
		if (pfactor2c == null) {
			if (other.pfactor2c != null)
				return false;
		} else if (!pfactor2c.equals(other.pfactor2c))
			return false;
		if (pfactor2d == null) {
			if (other.pfactor2d != null)
				return false;
		} else if (!pfactor2d.equals(other.pfactor2d))
			return false;
		if (pfactor2e == null) {
			if (other.pfactor2e != null)
				return false;
		} else if (!pfactor2e.equals(other.pfactor2e))
			return false;
		if (pfactor2f == null) {
			if (other.pfactor2f != null)
				return false;
		} else if (!pfactor2f.equals(other.pfactor2f))
			return false;
		if (pfactor2g == null) {
			if (other.pfactor2g != null)
				return false;
		} else if (!pfactor2g.equals(other.pfactor2g))
			return false;
		if (pfactor2h == null) {
			if (other.pfactor2h != null)
				return false;
		} else if (!pfactor2h.equals(other.pfactor2h))
			return false;
		if (pfactor2i == null) {
			if (other.pfactor2i != null)
				return false;
		} else if (!pfactor2i.equals(other.pfactor2i))
			return false;
		if (pfactor2j == null) {
			if (other.pfactor2j != null)
				return false;
		} else if (!pfactor2j.equals(other.pfactor2j))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ConvTablasExpReal [kbasetec=");
		builder.append(kbasetec);
		builder.append(", kcompania=");
		builder.append(kcompania);
		builder.append(", knegocio=");
		builder.append(knegocio);
		builder.append(", kriesgo=");
		builder.append(kriesgo);
		builder.append(", ksexo=");
		builder.append(ksexo);
		builder.append(", kcateg=");
		builder.append(kcateg);
		builder.append(", kedaddesde=");
		builder.append(kedaddesde);
		builder.append(", kmodalidad=");
		builder.append(kmodalidad);
		builder.append(", kfdesde=");
		builder.append(kfdesde);
		builder.append(", edadhasta=");
		builder.append(edadhasta);
		builder.append(", fhasta=");
		builder.append(fhasta);
		builder.append(", ctabbase=");
		builder.append(ctabbase);
		builder.append(", pfactor1=");
		builder.append(pfactor1);
		builder.append(", pfactor2a=");
		builder.append(pfactor2a);
		builder.append(", pfactor2b=");
		builder.append(pfactor2b);
		builder.append(", pfactor2c=");
		builder.append(pfactor2c);
		builder.append(", pfactor2d=");
		builder.append(pfactor2d);
		builder.append(", pfactor2e=");
		builder.append(pfactor2e);
		builder.append(", pfactor2f=");
		builder.append(pfactor2f);
		builder.append(", pfactor2g=");
		builder.append(pfactor2g);
		builder.append(", pfactor2h=");
		builder.append(pfactor2h);
		builder.append(", pfactor2i=");
		builder.append(pfactor2i);
		builder.append(", pfactor2j=");
		builder.append(pfactor2j);
		builder.append("]");
		return builder.toString();
	}

}
