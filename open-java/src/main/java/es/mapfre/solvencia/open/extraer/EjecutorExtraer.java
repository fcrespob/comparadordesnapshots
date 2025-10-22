package es.mapfre.solvencia.open.extraer;

import java.util.List;

import es.mapfre.solvencia.open.dto.ExtraerFicheroDTO;
import es.mapfre.solvencia.open.enums.TipoFichero;

public class EjecutorExtraer implements Runnable {
	private TipoFichero tipoFichero;
	private List<ExtraerFicheroDTO> params;

	public void setParams(List<ExtraerFicheroDTO> params) {
		this.params = params;
	}

	public void setTipoFichero(TipoFichero tipoFichero) {
		this.tipoFichero = tipoFichero;
	}

	public EjecutorExtraer(TipoFichero tipoFichero, List<ExtraerFicheroDTO> params) {
		super();
		this.tipoFichero = tipoFichero;
		this.params = params;
	}

	@Override
	public void run() {
		SolvenciaUtilsExtraerBBDD.ejecutarProcedimiento(tipoFichero, params);
	}
}
