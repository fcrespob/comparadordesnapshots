package es.mapfre.solvencia.servicios.fachada.impl;

import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IGestionarProceso;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.impl.AlmacenarDatos;
import es.mapfre.solvencia.servicios.impl.GestionarProceso;
import es.mapfre.solvencia.servicios.impl.ObtenerConfiguracion;
import es.mapfre.solvencia.servicios.impl.ObtenerDatos;

public final class FachadaServicios {
	private static final IAlmacenarDatos almacenarDatos = new AlmacenarDatos();
	private static final IGestionarProceso gestionarProceso = new GestionarProceso();
	private static final IObtenerConfiguracion obtenerConfiguracion = new ObtenerConfiguracion();
	private static final IObtenerDatos obtenerDatos = new ObtenerDatos();

	private FachadaServicios() {
	}

	public static IAlmacenarDatos getAlmacenarDatos() {
		return almacenarDatos;
	}

	public static IGestionarProceso getGestionarProceso() {
		return gestionarProceso;
	}

	public static IObtenerConfiguracion getObtenerConfiguracion() {
		return obtenerConfiguracion;
	}

	public static IObtenerDatos getObtenerDatos() {
		return obtenerDatos;
	}

}
