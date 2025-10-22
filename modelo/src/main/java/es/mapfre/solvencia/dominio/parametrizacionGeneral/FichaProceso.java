package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.FichaProcesoKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class FichaProceso implements EntidadBase<FichaProcesoKey>{
	
	public static final int IND_KEJECUCION = 0;
	public static final int IND_KSISTEMA = 1;
	public static final int IND_KPROTECNICO = 2;
	public static final int IND_KUEJECUCION = 3;
	public static final int IND_KSECUENCIA = 4;
	public static final int IND_FEFECTO = 5;
	public static final int IND_FPREVEJEC = 6;
	public static final int IND_FINIPER = 7;
	public static final int IND_CNEGOCIO = 8;
	public static final int IND_CCANAL = 9;
	public static final int IND_CTIPOBT = 10;
	public static final int IND_GPARAMETROS = 11;
	public static final int IND_CSITUACION = 12;
	public static final int IND_FINIREAL = 13;
	public static final int IND_GHINIREAL = 14;
	public static final int IND_FFINREAL = 15;
	public static final int IND_GHFINREAL = 16;
	public static final int IND_SOKAUTO = 17;
	public static final int IND_SOKUSUA = 18;
	public static final int IND_GOBSERVACION = 19;
	public static final int IND_CTIPOEJEC = 20;
	public static final int IND_SMANUAL = 21;
	public static final int IND_FGRABACION = 22;
	public static final int IND_CUSUARIO = 23;
	public static final int IND_FMODIFICACION = 24;
	public static final int IND_CUSUARIOM = 25;
	public static final int IND_KTIPOAMB = 26;

	public static final int IND_FILTROSAMB = 27;
	public static final int IND_FILTROSADIC = 28;

	public static final int IND_FCALC = 29;
	
	public static final int IND_REGISTROPARAM = 30;
	
	@PortableProperty(IND_KEJECUCION) private Integer kejecucion;
	@PortableProperty(IND_KSISTEMA) private String ksistema;
	@PortableProperty(IND_KPROTECNICO) private String kprotecnico;
	@PortableProperty(IND_KUEJECUCION) private String kuejecucion;
	@PortableProperty(IND_KSECUENCIA) private Integer ksecuencia;
	@PortableProperty(IND_FEFECTO) private Timestamp fefecto;
	@PortableProperty(IND_FPREVEJEC) private Timestamp fprevejec;
	@PortableProperty(IND_FINIPER) private Timestamp finiper;
	@PortableProperty(IND_CNEGOCIO) private String cnegocio;
	@PortableProperty(IND_CCANAL) private Integer ccanal;
	@PortableProperty(IND_CTIPOBT) private String ctipobt;
	@PortableProperty(IND_GPARAMETROS) private String gparametros;
	@PortableProperty(IND_CSITUACION) private String csituacion;
	@PortableProperty(IND_FINIREAL) private Timestamp finireal;
	@PortableProperty(IND_GHINIREAL) private String ghinireal;
	@PortableProperty(IND_FFINREAL) private Timestamp ffinreal;
	@PortableProperty(IND_GHFINREAL) private String ghfinreal;
	@PortableProperty(IND_SOKAUTO) private String sokauto;
	@PortableProperty(IND_SOKUSUA) private String sokusua;
	@PortableProperty(IND_GOBSERVACION) private String gobservacion;
	@PortableProperty(IND_CTIPOEJEC) private String ctipoejec;
	@PortableProperty(IND_SMANUAL) private String smanual;
	@PortableProperty(IND_FGRABACION) private Timestamp fgrabacion;
	@PortableProperty(IND_CUSUARIO) private String cusuario;
	@PortableProperty(IND_FMODIFICACION) private Timestamp fmodificacion;
	@PortableProperty(IND_CUSUARIOM) private String cusuariom;
	@PortableProperty(IND_KTIPOAMB) private String ktipoamb;
	
    @PortableProperty(IND_FILTROSAMB) private List<FiltroFichaProceso> filtrosAmbito = new ArrayList<FiltroFichaProceso>();
    // JBMARTA - PYAM0025 - INI
    @PortableProperty(IND_FILTROSADIC) private List<FiltroFichaProcesoAdicional> filtrosAdicionales = new ArrayList<FiltroFichaProcesoAdicional>();
    // JBMARTA - PYAM0025 - FIN
		
	@PortableProperty(IND_REGISTROPARAM) private RegistroParametros registroParametros;
	
	@PortableProperty(IND_FCALC) private Timestamp fcalc;
	@Override
	public FichaProcesoKey getKey() {
		return new FichaProcesoKey(kejecucion, ksistema, kprotecnico, kuejecucion, ksecuencia);
	}

	public Integer getKejecucion() {
		return kejecucion;
	}

	public void setKejecucion(Integer kejecucion) {
		this.kejecucion = kejecucion;
	}

	public String getKsistema() {
		return ksistema;
	}

	public void setKsistema(String ksistema) {
		this.ksistema = ksistema;
	}

	public String getKprotecnico() {
		return kprotecnico;
	}

	public void setKprotecnico(String kprotecnico) {
		this.kprotecnico = kprotecnico;
	}

	public String getKuejecucion() {
		return kuejecucion;
	}

	public void setKuejecucion(String kuejecucion) {
		this.kuejecucion = kuejecucion;
	}

	public Integer getKsecuencia() {
		return ksecuencia;
	}

	public void setKsecuencia(Integer ksecuencia) {
		this.ksecuencia = ksecuencia;
	}

	public Timestamp getFefecto() {
		return fefecto;
	}

	public void setFefecto(Timestamp fefecto) {
		this.fefecto = fefecto;
	}

	public Timestamp getFprevejec() {
		return fprevejec;
	}

	public void setFprevejec(Timestamp fprevejec) {
		this.fprevejec = fprevejec;
	}

	public Timestamp getFiniper() {
		return finiper;
	}

	public void setFiniper(Timestamp finiper) {
		this.finiper = finiper;
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

	public String getCtipobt() {
		return ctipobt;
	}

	public void setCtipobt(String ctipobt) {
		this.ctipobt = ctipobt;
	}

	public String getGparametros() {
		return gparametros;
	}

	public void setGparametros(String gparametros) {
		this.gparametros = gparametros;
	}

	public String getCsituacion() {
		return csituacion;
	}

	public void setCsituacion(String csituacion) {
		this.csituacion = csituacion;
	}

	public Timestamp getFinireal() {
		return finireal;
	}

	public void setFinireal(Timestamp finireal) {
		this.finireal = finireal;
	}

	public String getGhinireal() {
		return ghinireal;
	}

	public void setGhinireal(String ghinireal) {
		this.ghinireal = ghinireal;
	}

	public Timestamp getFfinreal() {
		return ffinreal;
	}

	public void setFfinreal(Timestamp ffinreal) {
		this.ffinreal = ffinreal;
	}

	public String getGhfinreal() {
		return ghfinreal;
	}

	public void setGhfinreal(String ghfinreal) {
		this.ghfinreal = ghfinreal;
	}

	public String getSokauto() {
		return sokauto;
	}

	public void setSokauto(String sokauto) {
		this.sokauto = sokauto;
	}

	public String getSokusua() {
		return sokusua;
	}

	public void setSokusua(String sokusua) {
		this.sokusua = sokusua;
	}

	public String getGobservacion() {
		return gobservacion;
	}

	public void setGobservacion(String gobservacion) {
		this.gobservacion = gobservacion;
	}

	public String getCtipoejec() {
		return ctipoejec;
	}

	public void setCtipoejec(String ctipoejec) {
		this.ctipoejec = ctipoejec;
	}

	public String getSmanual() {
		return smanual;
	}

	public void setSmanual(String smanual) {
		this.smanual = smanual;
	}

	public Timestamp getFgrabacion() {
		return fgrabacion;
	}

	public void setFgrabacion(Timestamp fgrabacion) {
		this.fgrabacion = fgrabacion;
	}

	public String getCusuario() {
		return cusuario;
	}

	public void setCusuario(String cusuario) {
		this.cusuario = cusuario;
	}

	public Timestamp getFmodificacion() {
		return fmodificacion;
	}

	public void setFmodificacion(Timestamp fmodificacion) {
		this.fmodificacion = fmodificacion;
	}

	public String getCusuariom() {
		return cusuariom;
	}

	public void setCusuariom(String cusuariom) {
		this.cusuariom = cusuariom;
	}

	public String getKtipoamb() {
		return ktipoamb;
	}

	public void setKtipoamb(String ktipoamb) {
		this.ktipoamb = ktipoamb;
	}

	public List<FiltroFichaProceso> getFiltrosAmbito() {
		return filtrosAmbito;
	}

	public void setFiltrosAmbito(List<FiltroFichaProceso> filtrosAmbito) {
		this.filtrosAmbito = filtrosAmbito;
	}

    // JBMARTA - PYAM0025 - INI
	public List<FiltroFichaProcesoAdicional> getFiltrosAdicionales() {
		return filtrosAdicionales;
	}

	public void setFiltrosAdicionales(List<FiltroFichaProcesoAdicional> filtrosAdicionales) {
		this.filtrosAdicionales = filtrosAdicionales;
    }
    // JBMARTA - PYAM0025 - FIN
	
	public RegistroParametros getRegistroParametros() {
		return registroParametros;
	}

	public void setRegistroParametros(RegistroParametros registroParametros) {
		this.registroParametros = registroParametros;
	}

	public Timestamp getFcalc() {
		// La fecha de cálculo es la fecha de efecto + 1
		if (fcalc == null) {
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTimeInMillis(fefecto.getTime());
			cal.add(Calendar.DATE, 1);
			fcalc = new Timestamp(cal.getTimeInMillis());
		}
		return fcalc;
	}

	public void setFcalc(Timestamp fcalc) {
		this.fcalc = fcalc;
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result
				+ ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result
				+ ((csituacion == null) ? 0 : csituacion.hashCode());
		result = prime * result + ((ctipobt == null) ? 0 : ctipobt.hashCode());
		result = prime * result
				+ ((ctipoejec == null) ? 0 : ctipoejec.hashCode());
		result = prime * result
				+ ((cusuario == null) ? 0 : cusuario.hashCode());
		result = prime * result
				+ ((cusuariom == null) ? 0 : cusuariom.hashCode());
		result = prime * result + ((fcalc == null) ? 0 : fcalc.hashCode());
		result = prime * result + ((fefecto == null) ? 0 : fefecto.hashCode());
		result = prime * result
				+ ((ffinreal == null) ? 0 : ffinreal.hashCode());
		result = prime * result
				+ ((fgrabacion == null) ? 0 : fgrabacion.hashCode());
		result = prime
				* result
				+ ((filtrosAdicionales == null) ? 0 : filtrosAdicionales
						.hashCode());
		result = prime * result
				+ ((filtrosAmbito == null) ? 0 : filtrosAmbito.hashCode());
		result = prime * result + ((finiper == null) ? 0 : finiper.hashCode());
		result = prime * result
				+ ((finireal == null) ? 0 : finireal.hashCode());
		result = prime * result
				+ ((fmodificacion == null) ? 0 : fmodificacion.hashCode());
		result = prime * result
				+ ((fprevejec == null) ? 0 : fprevejec.hashCode());
		result = prime * result
				+ ((ghfinreal == null) ? 0 : ghfinreal.hashCode());
		result = prime * result
				+ ((ghinireal == null) ? 0 : ghinireal.hashCode());
		result = prime * result
				+ ((gobservacion == null) ? 0 : gobservacion.hashCode());
		result = prime * result
				+ ((gparametros == null) ? 0 : gparametros.hashCode());
		result = prime * result
				+ ((kejecucion == null) ? 0 : kejecucion.hashCode());
		result = prime * result
				+ ((kprotecnico == null) ? 0 : kprotecnico.hashCode());
		result = prime * result
				+ ((ksecuencia == null) ? 0 : ksecuencia.hashCode());
		result = prime * result
				+ ((ksistema == null) ? 0 : ksistema.hashCode());
		result = prime * result
				+ ((ktipoamb == null) ? 0 : ktipoamb.hashCode());
		result = prime * result
				+ ((kuejecucion == null) ? 0 : kuejecucion.hashCode());
		result = prime * result + ((smanual == null) ? 0 : smanual.hashCode());
		result = prime * result + ((sokauto == null) ? 0 : sokauto.hashCode());
		result = prime * result + ((sokusua == null) ? 0 : sokusua.hashCode());
		return result;
	}

	@Override //NOSONAR
	public boolean equals(Object obj) { //NOSONAR
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FichaProceso other = (FichaProceso) obj;
		if (ccanal == null) {
			if (other.ccanal != null) {
				return false;
			}
		} else if (!ccanal.equals(other.ccanal)) {
			return false;
		}
		if (cnegocio == null) {
			if (other.cnegocio != null) {
				return false;
			}
		} else if (!cnegocio.equals(other.cnegocio)) {
			return false;
		}
		if (csituacion == null) {
			if (other.csituacion != null) {
				return false;
			}
		} else if (!csituacion.equals(other.csituacion)) {
			return false;
		}
		if (ctipobt == null) {
			if (other.ctipobt != null) {
				return false;
			}
		} else if (!ctipobt.equals(other.ctipobt)) {
			return false;
		}
		if (ctipoejec == null) {
			if (other.ctipoejec != null) {
				return false;
			}
		} else if (!ctipoejec.equals(other.ctipoejec)) {
			return false;
		}
		if (cusuario == null) {
			if (other.cusuario != null) {
				return false;
			}
		} else if (!cusuario.equals(other.cusuario)) {
			return false;
		}
		if (cusuariom == null) {
			if (other.cusuariom != null) {
				return false;
			}
		} else if (!cusuariom.equals(other.cusuariom)) {
			return false;
		}
		if (fcalc == null) {
			if (other.fcalc != null) {
				return false;
			}
		} else if (!fcalc.equals(other.fcalc)) {
			return false;
		}
		if (fefecto == null) {
			if (other.fefecto != null) {
				return false;
			}
		} else if (!fefecto.equals(other.fefecto)) {
			return false;
		}
		if (ffinreal == null) {
			if (other.ffinreal != null) {
				return false;
			}
		} else if (!ffinreal.equals(other.ffinreal)) {
			return false;
		}
		if (fgrabacion == null) {
			if (other.fgrabacion != null) {
				return false;
			}
		} else if (!fgrabacion.equals(other.fgrabacion)) {
			return false;
		}
		if (filtrosAdicionales == null) {
			if (other.filtrosAdicionales != null) {
				return false;
			}
		} else if (!filtrosAdicionales.equals(other.filtrosAdicionales)) {
			return false;
		}
		if (filtrosAmbito == null) {
			if (other.filtrosAmbito != null) {
				return false;
			}
		} else if (!filtrosAmbito.equals(other.filtrosAmbito)) {
			return false;
		}
		if (finiper == null) {
			if (other.finiper != null) {
				return false;
			}
		} else if (!finiper.equals(other.finiper)) {
			return false;
		}
		if (finireal == null) {
			if (other.finireal != null) {
				return false;
			}
		} else if (!finireal.equals(other.finireal)) {
			return false;
		}
		if (fmodificacion == null) {
			if (other.fmodificacion != null) {
				return false;
			}
		} else if (!fmodificacion.equals(other.fmodificacion)) {
			return false;
		}
		if (fprevejec == null) {
			if (other.fprevejec != null) {
				return false;
			}
		} else if (!fprevejec.equals(other.fprevejec)) {
			return false;
		}
		if (ghfinreal == null) {
			if (other.ghfinreal != null) {
				return false;
			}
		} else if (!ghfinreal.equals(other.ghfinreal)) {
			return false;
		}
		if (ghinireal == null) {
			if (other.ghinireal != null) {
				return false;
			}
		} else if (!ghinireal.equals(other.ghinireal)) {
			return false;
		}
		if (gobservacion == null) {
			if (other.gobservacion != null) {
				return false;
			}
		} else if (!gobservacion.equals(other.gobservacion)) {
			return false;
		}
		if (gparametros == null) {
			if (other.gparametros != null) {
				return false;
			}
		} else if (!gparametros.equals(other.gparametros)) {
			return false;
		}
		if (kejecucion == null) {
			if (other.kejecucion != null) {
				return false;
			}
		} else if (!kejecucion.equals(other.kejecucion)) {
			return false;
		}
		if (kprotecnico == null) {
			if (other.kprotecnico != null) {
				return false;
			}
		} else if (!kprotecnico.equals(other.kprotecnico)) {
			return false;
		}
		if (ksecuencia == null) {
			if (other.ksecuencia != null) {
				return false;
			}
		} else if (!ksecuencia.equals(other.ksecuencia)) {
			return false;
		}
		if (ksistema == null) {
			if (other.ksistema != null) {
				return false;
			}
		} else if (!ksistema.equals(other.ksistema)) {
			return false;
		}
		if (ktipoamb == null) {
			if (other.ktipoamb != null) {
				return false;
			}
		} else if (!ktipoamb.equals(other.ktipoamb)) {
			return false;
		}
		if (kuejecucion == null) {
			if (other.kuejecucion != null) {
				return false;
			}
		} else if (!kuejecucion.equals(other.kuejecucion)) {
			return false;
		}
		if (smanual == null) {
			if (other.smanual != null) {
				return false;
			}
		} else if (!smanual.equals(other.smanual)) {
			return false;
		}
		if (sokauto == null) {
			if (other.sokauto != null) {
				return false;
			}
		} else if (!sokauto.equals(other.sokauto)) {
			return false;
		}
		if (sokusua == null) {
			if (other.sokusua != null) {
				return false;
			}
		} else if (!sokusua.equals(other.sokusua)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FichaProceso [kejecucion=");
		builder.append(kejecucion);
		builder.append(", ksistema=");
		builder.append(ksistema);
		builder.append(", kprotecnico=");
		builder.append(kprotecnico);
		builder.append(", kuejecucion=");
		builder.append(kuejecucion);
		builder.append(", ksecuencia=");
		builder.append(ksecuencia);
		builder.append(", fefecto=");
		builder.append(fefecto);
		builder.append(", fprevejec=");
		builder.append(fprevejec);
		builder.append(", finiper=");
		builder.append(finiper);
		builder.append(", cnegocio=");
		builder.append(cnegocio);
		builder.append(", ccanal=");
		builder.append(ccanal);
		builder.append(", ctipobt=");
		builder.append(ctipobt);
		builder.append(", gparametros=");
		builder.append(gparametros);
		builder.append(", csituacion=");
		builder.append(csituacion);
		builder.append(", finireal=");
		builder.append(finireal);
		builder.append(", ghinireal=");
		builder.append(ghinireal);
		builder.append(", ffinreal=");
		builder.append(ffinreal);
		builder.append(", ghfinreal=");
		builder.append(ghfinreal);
		builder.append(", sokauto=");
		builder.append(sokauto);
		builder.append(", sokusua=");
		builder.append(sokusua);
		builder.append(", gobservacion=");
		builder.append(gobservacion);
		builder.append(", ctipoejec=");
		builder.append(ctipoejec);
		builder.append(", smanual=");
		builder.append(smanual);
		builder.append(", fgrabacion=");
		builder.append(fgrabacion);
		builder.append(", cusuario=");
		builder.append(cusuario);
		builder.append(", fmodificacion=");
		builder.append(fmodificacion);
		builder.append(", cusuariom=");
		builder.append(cusuariom);
		builder.append(", ktipoamb=");
		builder.append(ktipoamb);
		builder.append(", filtrosAmbito=");
		builder.append(filtrosAmbito);
		builder.append(", filtrosAdicionales=");
		builder.append(filtrosAdicionales);
		builder.append(", fcalc=");
		builder.append(fcalc);
		builder.append("]");
		return builder.toString();
	}
}