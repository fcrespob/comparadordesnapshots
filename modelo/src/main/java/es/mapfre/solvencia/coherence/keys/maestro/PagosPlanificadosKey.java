package es.mapfre.solvencia.coherence.keys.maestro;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.dataaffinity.maestro.AssociatedKeyMaestro;
import es.mapfre.solvencia.dominio.maestro.PagosPlanificados;

@Portable
public class PagosPlanificadosKey {

	@PortableProperty(PagosPlanificados.IND_KAJUSTE)
	private Integer kajuste;
	@PortableProperty(PagosPlanificados.IND_KCERTI)
	private Integer kcerti;
	@PortableProperty(PagosPlanificados.IND_KGRSUS)
	private Integer kgrsus;
	@PortableProperty(PagosPlanificados.IND_KSUBPOL)
	private Integer ksubpol;
	@PortableProperty(PagosPlanificados.IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(PagosPlanificados.IND_KPRESTA)
	private String kpresta;
	@PortableProperty(PagosPlanificados.IND_CGARANTIA)
	private Integer cgarantia;
	@PortableProperty(PagosPlanificados.IND_CPRESTAENTORNO)
	private String cprestaEntorno;
	@PortableProperty(PagosPlanificados.IND_CPRESTAFICT)
	private String cprestaFict;
	// TODO: ¿Este elemento es parte de la clave?
	@PortableProperty(PagosPlanificados.IND_FPLREAEFECTO)
	private Timestamp fplreaEfecto;
	@PortableProperty(PagosPlanificados.IND_FILLER)
	private String filler;

	public PagosPlanificadosKey(Integer kajuste, Integer kcerti,
			Integer kgrsus, Integer ksubpol, Long kpoliza, String kpresta,
			Integer cgarantia, String cprestaEntorno,String cprestaFict, Timestamp fplreaEfecto, Integer norden) {
		super();

		this.kajuste = kajuste;
		this.kcerti = kcerti;
		this.kgrsus = kgrsus;
		this.ksubpol = ksubpol;
		this.kpoliza = kpoliza;
		this.kpresta = kpresta;
		this.cgarantia = cgarantia;
		this.cprestaEntorno = cprestaEntorno;
		this.cprestaFict=cprestaFict;
		this.fplreaEfecto = fplreaEfecto;
		this.filler = norden.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cgarantia == null) ? 0 : cgarantia.hashCode());
		result = prime * result + ((cprestaEntorno == null) ? 0 : cprestaEntorno.hashCode());
		result = prime * result + ((cprestaFict == null) ? 0 : cprestaFict.hashCode());
		result = prime * result + ((filler == null) ? 0 : filler.hashCode());
		result = prime * result + ((fplreaEfecto == null) ? 0 : fplreaEfecto.hashCode());
		result = prime * result + ((kajuste == null) ? 0 : kajuste.hashCode());
		result = prime * result + ((kcerti == null) ? 0 : kcerti.hashCode());
		result = prime * result + ((kgrsus == null) ? 0 : kgrsus.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((kpresta == null) ? 0 : kpresta.hashCode());
		result = prime * result + ((ksubpol == null) ? 0 : ksubpol.hashCode());
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
		PagosPlanificadosKey other = (PagosPlanificadosKey) obj;
		if (cgarantia == null) {
			if (other.cgarantia != null)
				return false;
		} else if (!cgarantia.equals(other.cgarantia))
			return false;
		if (cprestaEntorno == null) {
			if (other.cprestaEntorno != null)
				return false;
		} else if (!cprestaEntorno.equals(other.cprestaEntorno))
			return false;
		if (cprestaFict == null) {
			if (other.cprestaFict != null)
				return false;
		} else if (!cprestaFict.equals(other.cprestaFict))
			return false;
		if (filler == null) {
			if (other.filler != null)
				return false;
		} else if (!filler.equals(other.filler))
			return false;
		if (fplreaEfecto == null) {
			if (other.fplreaEfecto != null)
				return false;
		} else if (!fplreaEfecto.equals(other.fplreaEfecto))
			return false;
		if (kajuste == null) {
			if (other.kajuste != null)
				return false;
		} else if (!kajuste.equals(other.kajuste))
			return false;
		if (kcerti == null) {
			if (other.kcerti != null)
				return false;
		} else if (!kcerti.equals(other.kcerti))
			return false;
		if (kgrsus == null) {
			if (other.kgrsus != null)
				return false;
		} else if (!kgrsus.equals(other.kgrsus))
			return false;
		if (kpoliza == null) {
			if (other.kpoliza != null)
				return false;
		} else if (!kpoliza.equals(other.kpoliza))
			return false;
		if (kpresta == null) {
			if (other.kpresta != null)
				return false;
		} else if (!kpresta.equals(other.kpresta))
			return false;
		if (ksubpol == null) {
			if (other.ksubpol != null)
				return false;
		} else if (!ksubpol.equals(other.ksubpol))
			return false;
		return true;
	}

	/**
	 * KGARANTÍA. En colectivos, la base de datos almacena un código de garantía
	 * de 3 posiciones tanto en los datos de póliza como en los datos de los
	 * pagos planificados. Para individuales, estos códigos de garantía se
	 * ampliaron hace un tiempo hasta las cinco posiciones. En el maestro para
	 * colectivos encontrarás códigos de garantías de 5 posiciones, que antes de
	 * usar para recuperar del fichero de pagos planificados debes convertir en
	 * 3. Garantía en el maestro “00999”, pues para acceder buscamos la “999”.
	 * 
	 * KPRESTACION Estamos en las mismas, pero este es un poco más raro. En el
	 * maestro tenemos KPRESTACION de 6 posiciones. A partir de estas 6
	 * posiciones, de cara a recuperar la clave correcta de los pagos
	 * planificados debemos:
	 * 
	 * JI01-KPRESTACION(posicion1, logitud1) -> Sirve para buscar por el campo
	 * J003-CPRESTA-ENTORNO JI01-KPRESTACION(posicion2, logitud4) -> Sirve para
	 * buscar por el campo J003-KPRESTA
	 * 
	 * De esta forma los pagos planificados que corresponden a la UMIC que esto
	 * tratando son aquellos en los que coincida:
	 * 
	 * X880J003-KPOLIZA - X880JI01-KPOLIZA X880J003-KSUBPOL -
	 * X880JI01-KSUBPOLIZA X880J003-KCERTI - X880JI01-KCERTIFICADO
	 * X880J003-KGRSUS - X880JI01-NSUSCRI X880J003-CPRESTA-ENTORNO -
	 * X880JI01-KPRESTACION(1:1) X880J003-KPRESTA - X880JI01-KPRESTACION(2:4)
	 * X880J003-KAJUSTE - X880JI01-KAJUSTE X880J003-CGARANTIA -
	 * X880JI01-KGARANTIA(2:3)
	 * 
	 * @param umicKey
	 * @return
	 */
	public static PagosPlanificadosKey getKeyFromUmicKey(UmicKey umicKey) {
		Integer kcertiTemp = umicKey.getKcertificado();
		String cprestaEntornoTemp = umicKey.getKprestacion().substring(0, 1);
		String kprestaTemp = umicKey.getKprestacion().substring(1, 5);
		Integer cgarantia = umicKey.getKgarantia();
		Integer norden = umicKey.getNorden();
		String cprestaFict = umicKey.getKprestacion().substring(3);
		
		PagosPlanificadosKey key = new PagosPlanificadosKey(
				umicKey.getKajuste(), kcertiTemp, umicKey.getNsuscri(),
				umicKey.getKsubpoliza(), umicKey.getKpoliza(), kprestaTemp,
				cgarantia, cprestaEntornoTemp,cprestaFict, null,norden);

		return key;
	}

	public String getCprestaFict() {
		return cprestaFict;
	}

	public void setCprestaFict(String cprestaFict) {
		this.cprestaFict = cprestaFict;
	}

	public PagosPlanificadosKey() {
		super();
	}
	
	public AssociatedKeyMaestro getAssociatedKeyMaestro(){
		return new AssociatedKeyMaestro(kpoliza, ksubpol, /*cgarantia,*/ kajuste, kgrsus, kcerti, kpresta+cprestaEntorno+cprestaFict);
	}

	public Integer getKajuste() {
		return kajuste;
	}

	public void setKajuste(Integer kajuste) {
		this.kajuste = kajuste;
	}

	public Integer getKcerti() {
		return kcerti;
	}

	public void setKcerti(Integer kcerti) {
		this.kcerti = kcerti;
	}

	public Integer getKgrsus() {
		return kgrsus;
	}

	public void setKgrsus(Integer kgrsus) {
		this.kgrsus = kgrsus;
	}

	public Integer getKsubpol() {
		return ksubpol;
	}

	public void setKsubpol(Integer ksubpol) {
		this.ksubpol = ksubpol;
	}

	public Long getKpoliza() {
		return kpoliza;
	}

	public void setKpoliza(Long kpoliza) {
		this.kpoliza = kpoliza;
	}

	public String getKpresta() {
		return kpresta;
	}

	public void setKpresta(String kpresta) {
		this.kpresta = kpresta;
	}

	public Integer getCgarantia() {
		return cgarantia;
	}

	public void setCgarantia(Integer cgarantia) {
		this.cgarantia = cgarantia;
	}

	public String getCprestaEntorno() {
		return cprestaEntorno;
	}

	public void setCprestaEntorno(String cprestaEntorno) {
		this.cprestaEntorno = cprestaEntorno;
	}

	public Timestamp getFplreaEfecto() {
		return fplreaEfecto;
	}

	public void setFplreaEfecto(Timestamp fplreaEfecto) {
		this.fplreaEfecto = fplreaEfecto;
	}

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
	}
	
	
}
