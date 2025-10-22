package es.mapfre.solvencia.dominio.formulacion;

import java.io.Serializable;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

/**
 * @author rschacon
 * 
 */
@Portable
public class FichaProcesoModulo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Constantes de acceso a los valores
	public static final int IND_FCALC = 0;
	public static final int IND_BASETECNICA = 1;
	public static final int IND_CRITERIOFECHAPAGO = 2;
	public static final int IND_CRITERIOFECHADEVENGO = 3;

	/**
	 * Fecha de calculo
	 */
	@PortableProperty(IND_FCALC)
	private Timestamp fcalc;

	/**
	 * . Base técnica
	 */
	@PortableProperty(IND_BASETECNICA)
	private String baseTecnica;

	@PortableProperty(IND_CRITERIOFECHAPAGO)
	private String criterioFechaPago;
	@PortableProperty(IND_CRITERIOFECHADEVENGO)
	private String criteFecDevengo;

	// Getters y setters...

	public FichaProcesoModulo() {
		super();
	}

	public Timestamp getFcalc() {
		return fcalc;
	}

	public void setFcalc(Timestamp fcalc) {
		this.fcalc = fcalc;
	}

	public String getBaseTecnica() {
		return baseTecnica;
	}

	public void setBaseTecnica(String baseTecnica) {
		this.baseTecnica = baseTecnica;
	}

	public String getCriterioFechaPago() {
		return this.criterioFechaPago;
	}

	public void setCriterioFechaPago(final String criterioFechaPago) {
		this.criterioFechaPago = criterioFechaPago;
	}

	public String getCriteFecDevengo() {
		return this.criteFecDevengo;
	}

	public void setCriteFecDevengo(final String criteFecDevengo) {
		this.criteFecDevengo = criteFecDevengo;
	}
}
