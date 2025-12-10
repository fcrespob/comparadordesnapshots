package es.mapfre.solvencia.motor;

import java.util.List;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DisenoProcesos;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ElementoSubproceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.programas.Programa;
import es.mapfre.solvencia.programas.services.FactoriaProgramas;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IGestionarProceso;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;

public class Ejecutor {

	private static Logger log = LoggerFactory.getLogger(Ejecutor.class);
	private static Logger logEstadistica = LoggerFactory.getLogger("estadistica");

	private static final String E00 = "00";

	private IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
	private IGestionarProceso gestionarProceso = FachadaServicios.getGestionarProceso();
	private IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();

	// Tiempo máximo permitido para la ejecución de un programa
	private static Integer listonTiempo = Integer.valueOf(System.getProperty("solvencia.debug.tiempo.programa", "250"));

	/*
	 * Recorre un Diseño de Procesos ejecutando sus hijos, y los hijos de los hijos
	 * ...
	 */
	public void buscarProceso(String proceso, Umic umic, FichaProceso fp, DetalleBaseTecnica detalleBaseTecnica,
			List<DetalleCorriente> detallesCorriente, String kclaveadic, Stack<String> pila)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		DisenoProcesos disenoProceso;
		DatosGenerales dg = umic.getDatosGenerales();
		// Comprobamos si el nombre del proceso comienza por "PROY_". En ese caso es una
		// corriente y debemos comprobar más cosas antes de ejecutarlo.
		if (proceso != null && proceso.startsWith(ConstantesSolvencia.CTE_PREFIJO_CORRIENTE)) {
			// Si es una corriente, debemos mirar si el código del proceso es el de PROY_PRV
			String elementoModulo = ConstantesSolvencia.CTE_ELEMENTO_PROBABLE;
			if (ConstantesSolvencia.CTE_PROY_PRV.equals(proceso) || ConstantesSolvencia.CTE_PROY_PMRR.equals(proceso)) {
				// En este caso, debemos buscar si hay definido el nominal para la modalidad en
				// la que trabajamos.
				elementoModulo = ConstantesSolvencia.CTE_ELEMENTO_NOMINAL;
			}
			FlujosProbables flujoProbable = obtenerConfiguracion.recuperarConfProv(dg.getKmodalidad(),
					dg.getKgarantia(), umic.getDatosAdicionales().getPrestCal(), detalleBaseTecnica.getBt());
			if (flujoProbable == null) {
				// No existe flujo probable, por lo tanto se levanta una excepción
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_D6,
						new String[] { Util.errorString(dg.getKmodalidad()), Util.errorString(dg.getKgarantia()),
								Util.errorString(umic.getDatosAdicionales().getPrestCal()),
								Util.errorString(detalleBaseTecnica.getBt()) });
			} else {
				String modulo = obtenerConfiguracion.recuperarModuloFlujoProbable(flujoProbable, proceso,
						elementoModulo);

				if (modulo == null || modulo.isEmpty()) {
					// No hemos encontrado módulo para esta modalidad así que no ejecutamos el
					// subproceso
					return;
				}
			}
		}

		pila.add(proceso);

		if (detalleBaseTecnica.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMFE)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMMI)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLFE)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLMI)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCF)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCI)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRGTO)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRVM)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTIU)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTID)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEP)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEN)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIP)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIN)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM)) {
			// Para las bases técnicas estresadas se recuperarán los procesos de BEL
			disenoProceso = gestionarProceso.obtenerDisenoProceso(proceso, dg.getCcanal(), dg.getKramo(),
					dg.getKmodalidad(), dg.getKgarantia(), ConstantsModulos.CTE_BT_BEL, kclaveadic);
		} else if (detalleBaseTecnica.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantesSolvencia.BASE_NIF17LIR)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantesSolvencia.BASE_N17LIRIN)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantesSolvencia.BASE_NIFF17OCI)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17IF)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantesSolvencia.BASE_N17CLIR)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantesSolvencia.BASE_NF17AEN)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantesSolvencia.BASE_NF17MFE)
				|| detalleBaseTecnica.getBaseTec().equals(ConstantesSolvencia.BASE_NF17GTO)) {
			disenoProceso = gestionarProceso.obtenerDisenoProceso(proceso, dg.getCcanal(), dg.getKramo(),
					dg.getKmodalidad(), dg.getKgarantia(), ConstantesSolvencia.BASE_NIIF17, kclaveadic);

		} else {
			disenoProceso = gestionarProceso.obtenerDisenoProceso(proceso, dg.getCcanal(), dg.getKramo(),
					dg.getKmodalidad(), dg.getKgarantia(), detalleBaseTecnica.getBaseTec(), kclaveadic);
		}

		if (disenoProceso.getCgestespeci() != null && !disenoProceso.getCgestespeci().isEmpty()) {
			ejecutarPrograma(disenoProceso.getCgestespeci(), umic, fp, detalleBaseTecnica, detallesCorriente, null);
		} else {

			for (ElementoSubproceso elementoSubproceso : disenoProceso.getElementosSubprocesos()) {

				if (pila.search(elementoSubproceso.getCelement()) >= 0) {
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_ERROR_03,
							new String[] { elementoSubproceso.getCelement() });
				}
				if (elementoSubproceso.getSsubproc()) {
					buscarProceso(elementoSubproceso.getCelement(), umic, fp, detalleBaseTecnica, detallesCorriente,
							kclaveadic, pila);
				} else {
					try {
						ejecutarPrograma(elementoSubproceso.getCelement(), umic, fp, detalleBaseTecnica,
								detallesCorriente, pila.lastElement());
					} catch (Solvencia2Excepcion s2e) {

						Solvencia2ExcepcionHelper.checkIncidencia(detalleBaseTecnica.getBaseTec(), fp.getCcanal(),
								dg.getCcartera(), dg.getKey(), dg.getFecCierre(), dg.getCnegocio(), fp.getKprotecnico(),
								s2e);
						if (ConstantesSolvencia.BASE_BTI.equals(detalleBaseTecnica.getBaseTec())
								&& s2e.getIncidencia() != null
								&& !ConstantesSolvencia.CTE_ERROR_BLOQUEANTE.equals(s2e.getIncidencia().getTipoError())
								|| ConstantesSolvencia.BASE_BTIPROY.equals(detalleBaseTecnica.getBaseTec())
										&& s2e.getIncidencia() != null && !ConstantesSolvencia.CTE_ERROR_BLOQUEANTE
												.equals(s2e.getIncidencia().getTipoError())) {
							// show must go on
							almacenarDatos.almacenarIncidencias(s2e.getIncidencia());
							throw s2e;
						} else {
							throw s2e;
						}

					} catch (Exception e) {
						Solvencia2Excepcion s2e = Solvencia2ExcepcionHelper.crearExcepcion(E00, new Object[] { e },
								fp.getKprotecnico(), detalleBaseTecnica.getBaseTec(), dg.getCcanal(), dg.getCcartera(),
								dg.getKey(), dg.getFecCierre(), fp.getCnegocio(), e);

						if (ConstantesSolvencia.BASE_BTI.equals(detalleBaseTecnica.getBaseTec())
								|| ConstantesSolvencia.BASE_BTIPROY.equals(detalleBaseTecnica.getBaseTec())) {
							// show must go on
							almacenarDatos.almacenarIncidencias(s2e.getIncidencia());
							throw s2e;
						} else {
							throw s2e;
						}
					}
				}
			}
		}

		pila.pop();
	}

	/*
	 * Ejecuta un "es.mapfre.solvencia.formulacion.programas.Progrma"
	 */
	private void ejecutarPrograma(String nombre, Umic umic, FichaProceso fp, DetalleBaseTecnica detalleBaseTecnica,
			List<DetalleCorriente> detallesCorriente, String subProcesoActual)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		/*
		 * SI NO EXISTE EL PROGRAMA o Si es BTI se continúa con el siguiente
		 * subproceso/programa dentro de la misma UMIC y base técnica. o Si no es BTI:
		 * Error asociado a la UMIC que se está tratando, y se pasa a la siguiente UMIC.
		 */
		long antes = System.currentTimeMillis();

		Programa programa = FactoriaProgramas.getPrograma(nombre);

		long medio = System.currentTimeMillis();

		if (log.isTraceEnabled()) {
			log.trace("{} - {} - Execute {}. Encontrado en {} ms.", detalleBaseTecnica.getBt(), subProcesoActual,
					programa.getNombrePrograma(), (medio - antes));
		}

		try {
			programa.execute(umic, fp, detalleBaseTecnica, detallesCorriente, subProcesoActual);
		} finally {
			long despues = System.currentTimeMillis();

			// BTI;476;3369836;PROY_PRV;J880PMC001;5418
			if ((despues - medio) > listonTiempo && log.isWarnEnabled()) {
				logEstadistica.warn("{};{};{};{};{};{};{};", detalleBaseTecnica.getBt(),
						umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKpoliza(),
						umic.getDatosGenerales().getKsubpoliza(), subProcesoActual, programa.getNombrePrograma(),
						(despues - medio));
			} else if (log.isDebugEnabled()) {
				logEstadistica.debug("{};{};{};{};{};{};{};", detalleBaseTecnica.getBt(),
						umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKpoliza(),
						umic.getDatosGenerales().getKsubpoliza(), subProcesoActual, programa.getNombrePrograma(),
						(despues - medio));
			}
		}
	}

}
