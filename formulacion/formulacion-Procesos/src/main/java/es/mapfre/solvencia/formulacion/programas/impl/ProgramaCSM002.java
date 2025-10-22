package es.mapfre.solvencia.formulacion.programas.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.programas.ProgramaGenerico;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilProcesos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * @author NFQ
 *
 */
public class ProgramaCSM002 extends ProgramaGenerico {

	private static final String CLAVE_VA_PROVCSM = ConstantsModulos.CTE_VA_PROVCSM;
	private static final String CLAVE_VA_FECTRANSICION = ConstantsModulos.CTE_VA_FECTRANSICION;

	private static final Logger LOG = LoggerFactory.getLogger(ProgramaCSM002.class);

	@Override
	public void executeImpl(final Umic umic, final FichaProceso fichaProceso, final DetalleBaseTecnica btcUmic,
			final List<DetalleCorriente> proyUmic, final String codSubproceso) throws Solvencia2Excepcion {

		if (ProgramaCSM002.LOG.isTraceEnabled()) {
			ProgramaCSM002.LOG.trace("Inicio función << executeImpl >> de la clase ProgramaCSM002");
		}

		try {
			// llamamos al metodo calcularPatronAmortizacion
			calcularPatronAmortizacion(umic, fichaProceso, btcUmic, proyUmic, codSubproceso);
		} catch (Solvencia2Excepcion e) {
			ProgramaCSM002.LOG.error(e.getIncidencia().getTextoError(), e);
			UtilProcesos.exceptionControladaPrograma(btcUmic, umic, getNombrePrograma(), e);
		} catch (Exception e) {
			ProgramaCSM002.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ProgramaCSM002.LOG.isTraceEnabled()) {
			ProgramaCSM002.LOG.trace("Fin función << executeImpl >> de la clase ProgramaCSM002");
		}
	}

	/**
	 * Realiza el cálculo del Patrón de Amortización de CSM. Este programa, no
	 * ejecutará ningún cálculo para las bases técnicas que no sean NIIF17.
	 * 
	 * @param umic
	 * @param fichaProceso
	 * @param btcUmic
	 * @param proyUmic
	 * @param codSubproceso
	 */
	private void calcularPatronAmortizacion(final Umic umic, final FichaProceso fichaProceso,
			final DetalleBaseTecnica btcUmic, final List<DetalleCorriente> proyUmic, final String codSubproceso) {

		final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		final IObtenerDatos servicioDatos = FachadaServicios.getObtenerDatos();
		final IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
		String varProvCSM;
		Timestamp varFecTransicion;
		List<DetalleCorriente> varProyCSM;
		BigDecimal varCSM,varCSMant = BigDecimal.ZERO;
		BigDecimal varPatronCSM = BigDecimal.ZERO;
		BigDecimal factorActFin = BigDecimal.ZERO;
		BigDecimal varProvbtiproy = BigDecimal.ZERO;
		BigDecimal varProvNF17 = BigDecimal.ZERO;
		BigDecimal varResultadoRA = BigDecimal.ZERO;
		BigDecimal aux3 = BigDecimal.ZERO;
		Modulo moduloCalFechas = null;

		if (ProgramaCSM002.LOG.isTraceEnabled()) {
			ProgramaCSM002.LOG.trace("Inicio función << calcularPatronAmortizacion >> de la clase ProgramaCSM002");
		}

		if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIF17LIR)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17LIRIN)) {

			varProvCSM = (String) servicio.recuperarDefinicionAuxiliar(CLAVE_VA_PROVCSM);

			if (varProvCSM == null) {
				varProvCSM = ConstantsModulos.CTE_BTI;
			}

			if (varProvCSM.equals(ConstantsModulos.CTE_BT_ROSSP) || varProvCSM.equals(ConstantsModulos.CTE_BT_ROSSPCSM) || varProvCSM.equals("ROSSPTI") || varProvCSM.equals("ROSSPTE") || varProvCSM.equals("ROSSPGA")) {
				varFecTransicion = (Timestamp) servicio.recuperarDefinicionAuxiliar(CLAVE_VA_FECTRANSICION);

				if (!umic.getFechas().getFecinisus().before(varFecTransicion)) {
					varProvCSM = ConstantsModulos.CTE_BTI;
				}
			}
			
			if(varProvCSM.equals(ConstantsModulos.CTE_BTI)){
				varProyCSM = servicioDatos.recuperarProyeccion(varProvCSM, btcUmic.getFecCierre(), umic.getKey());
				if(null == varProyCSM || varProyCSM.size() == 0){
					varProyCSM = servicioDatos.recuperarProyeccion(ConstantsModulos.CTE_BTI_PROY, btcUmic.getFecCierre(), umic.getKey());
				}
			}else{
				varProyCSM = servicioDatos.recuperarProyeccion(varProvCSM, btcUmic.getFecCierre(), umic.getKey());
			}

			BigDecimal varProyCSMAnterior = BigDecimal.ZERO;
			BigDecimal factorActFinAnterior = BigDecimal.ZERO;

			for (int i = 0; i < varProyCSM.size(); i++) {
				moduloCalFechas = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_ACTNIIF17);
				if(i>0){
					varCSMant = proyUmic.get(i-1).getCsmumic();
					
				factorActFin = (BigDecimal) moduloCalFechas.execute(proyUmic,
						proyUmic.get(i-1).getBloqueBySubproceso(codSubproceso), i + 1, fichaProceso.getFcalc(), umic,
						btcUmic, this.getMapVariables());
				}
				if (varCSMant == null) {
					varCSMant = BigDecimal.ZERO;
				}
				
				varCSM = proyUmic.get(i).getCsmumic();
				if (varCSM == null) {
					varCSM = BigDecimal.ZERO;
				}

				if(factorActFin.equals(BigDecimal.ZERO)){
					aux3 = BigDecimal.ZERO;
				}else{
					aux3 = varCSMant.divide(factorActFin, ConstantsFunciones.MATH_CONTEXT);
				}
				varPatronCSM = aux3.subtract(varCSM);

				varProyCSMAnterior = varCSM;
				factorActFinAnterior = factorActFin;

				proyUmic.get(i).setPatronCSM(varPatronCSM);
			}

			almacenarDatos.almacenarProyeccion(proyUmic);
		}

		if (ProgramaCSM002.LOG.isTraceEnabled()) {
			ProgramaCSM002.LOG.trace("Fin función << calcularPatronAmortizacion >> de la clase ProgramaCSM002");
		}
	}

	@Override
	public String getNombrePrograma() {
		return ConstantsFactorias.PROGRAMA_CSM002;
	}

}
