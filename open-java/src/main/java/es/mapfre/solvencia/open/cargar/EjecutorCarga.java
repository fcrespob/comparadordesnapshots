package es.mapfre.solvencia.open.cargar;

import java.util.List;

import es.mapfre.solvencia.open.dto.CargarFicheroDTO;
import es.mapfre.solvencia.open.enums.TipoFichero;

public class EjecutorCarga implements Runnable {
	private TipoFichero tipoFichero;
	private List<CargarFicheroDTO> params;
	private Boolean isCierre;

	public void setIsCierre(Boolean isCierre) {
		this.isCierre = isCierre;
	}


	public void setParams(List<CargarFicheroDTO> params) {
		this.params = params;
	}
	
	
	public void setTipoFichero(TipoFichero tipoFichero) {
		this.tipoFichero = tipoFichero;
	}

	public EjecutorCarga(TipoFichero tipoFichero, List<CargarFicheroDTO> params, Boolean isCierre) {
		super();
		this.tipoFichero = tipoFichero;
		this.params = params;
		this.isCierre = isCierre;
	}

	@Override
	public void run() {
		SolvenciaUtilsCargaBBDD.ejecutarProcedimiento(tipoFichero, params, isCierre);
	}
}
