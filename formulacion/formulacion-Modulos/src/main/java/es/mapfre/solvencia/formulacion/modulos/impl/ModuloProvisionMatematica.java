/* MODIFICACION:TAR00400971-NECESIDADES NUEVO SISTEMA DE PROCESOS TÉCNICOS 
   FECHA: 17/12/2018 Se incluye campo KBENCON
   AUTOR: INDRA
*/
package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.BaseTecnicaInicial;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Fechas;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalFlujoProyeccion;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo Provision matematica.
 * 
 * La clase PROVISION_MATEMATICA se encargará de calcular el importe final de la
 * provisión matemática a partir de la información de todas las corrientes
 * calculadas, en base al supuesto de base técnica marcado.
 * 
 * Calcula también los totales por umic y BTC de la estructura de salida
 * totalFlujosUmic, además de los totales del bloque totalFlujoProyeccion de la
 * proyección calculada proyUmic
 * 
 * @author agonzalezgar
 *
 */
public class ModuloProvisionMatematica implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloProvisionMatematica.class);

	private static final String CLAVE_VAR_AJUSTE = ConstantsModulos.CTE_VAR_AJUSTE
			.concat(ConstantsFactorias.MODULO_PROV_MAT);

	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_PROV_MAT;
	}

	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la
	 * llamada a la función que realiza los calculos del modulo.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {

		if (ModuloProvisionMatematica.LOG.isTraceEnabled()) {
			ModuloProvisionMatematica.LOG.trace("Inicio de execute en clase ModuloProvisionMatematica");
		}

		try {
			// Recibe un periodo y realiza un calculo
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_DET_CORR_PROV_MAT];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC_PROV_MAT];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_UMIC_PROV_MAT];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_BTC_UMIC_PROV_MAT];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_MAP_VARIA_PROV_MAT];

			// Ejecutamos el modulos moduloProvisionMatematica
			moduloProvisionMatematica(proyUmic, fcalc, umic, btcUmic, mapVariables);

		} catch (Solvencia2Excepcion e) {
			ModuloProvisionMatematica.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloProvisionMatematica.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloProvisionMatematica.LOG.isTraceEnabled()) {
			ModuloProvisionMatematica.LOG.trace("Fin de execute en clase ModuloProvisionMatematica");
		}

		return BigDecimal.ZERO;
	}

	/**
	 * El modulo PROVISION_MATEMATICA se encargará de calcular el importe final
	 * de la provisión matemática a partir de la información de todas las
	 * corrientes calculadas, en base al supuesto de base técnica marcado.
	 * 
	 * @param lstProyUmic
	 *            Estructura detalleCorrientes de la umic
	 * @param fcalc
	 *            Fecha de calculo
	 * @param umic
	 *            Contiene los datos de la Umic que se está procesando
	 * @param btcUmic
	 *            Contiene el detalle de la base técnica de cálculo para la umic
	 * @param mapVariables
	 *            mapa con las variables de memoria necesarias
	 */
	private void moduloProvisionMatematica(final List<DetalleCorriente> lstProyUmic, final Timestamp fcalc,
			final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {

		if (ModuloProvisionMatematica.LOG.isTraceEnabled()) {
			ModuloProvisionMatematica.LOG
					.trace("Inicio función << moduloProvisionMatematica >> de la clase ModuloProvisionMatematica");
		}

		final TotalesFlujos totales = new TotalesFlujos();
		final IObtenerDatos obtenerDatos = FachadaServicios.getObtenerDatos();
		final IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();

		// Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntradaProvisionMatematica(lstProyUmic, fcalc, umic, btcUmic);

		/**
		 * Se realizarán los cálculos si la umic no ha tenido errores, y para
		 * comprobarlo se llamará al servicio siguiente:
		 * 
		 * UmicIncidente = recuperarUmicIndicente(umic.datosGenerales.cnegocio,
		 * umic.datosGenerales.ccanal, umic.datosGenerales.ccartera,
		 * umic.datosGenerales.fcierre, detalleBaseTecnica.ktipobt,
		 * umic.datosGenerales.kmodalidad, umic.datosGenerales.kpoliza,
		 * umic.datosGenerales.ksubpoliza, umic.datosGenerales.kcertificado,
		 * umic.datosGenerales.nsuscri, umic.datosGenerales.norden,
		 * umic.datosGenerales.kgarantia, umic.datosGenerales.kprestacion,
		 * umic.datosGenerales.kajuste, umic.datosGenerales.ctipoaport,
		 * tipoError "E")
		 */
		final boolean umicIncidente = obtenerDatos.recuperarUmicIncidente(umic.getKey(),
				umic.getDatosGenerales().getFecCierre(), btcUmic.getBaseTec());

		/**
		 * Si UmicIncidente = True -> ni se totalizarán importes, ni se
		 * calculará la Provisión Matemática, terminando así el proceso para esa
		 * umic. Si UmicIncidente = False -> se realizará el proceso definido a
		 * continuación:
		 */
		if (!umicIncidente) {

			/**
			 * Una vez comprobado que la umic no tiene errores se acumularán y
			 * totalizarán los importes de los distintos conceptos de cada
			 * corriente para cada periodo y también para todos los periodos, es
			 * decir se realizarán dos tipos de acumulados.
			 * 
			 * Se recuperará la variable Ajuste a aplicar a la provisión por
			 * flujos
			 */

			BigDecimal varAjuste = UtilModulos.gerVarAjuste(mapVariables, CLAVE_VAR_AJUSTE,
					umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(),
					umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec());

			/**
			 * Primero se inicializaran los campos de la estructura de salida
			 * totalFlujosUmic y se informaran los campos de la umic que no
			 * varían por corriente, ni por periodo.
			 */
			inicializarCamposNoVariaPorCorriente(umic, totales, btcUmic, fcalc, lstProyUmic);

			/**
			 * Una vez recuperadas todas las corrientes se recorrerán
			 * simultáneamente ,las tablas de proyecciones con los distintos
			 * periodos, ya que el nº de periodos es el mismo para todas las
			 * proyecciones, acumulando los importes de cada periodo por
			 * conceptos .
			 */
			acumularImportesPorConcepto(lstProyUmic, totales);

			/**
			 * Una vez obtenidos los importes totales por corrientes, se
			 * calcularán los importes totales de todas las cuantías de la umic.
			 * Los conceptos de Prima restan siempre.
			 */
			calcularImportesTotalesCuantias(totales, varAjuste);

			/**
			 * Se procederá a almacenar los datos calculados en la estructura
			 * totalFlujosUmic invocando al correspondiente servicio al efecto,
			 * almacenarDatos.totalFlujos
			 */
			almacenarDatos.almacenarTotalFlujos(totales);

			/**
			 * Se procederá a almacenar los datos calculados en el bloque
			 * totalFlujoProyeccion invocando al correspondiente servicio al
			 * efecto, almacenarDatos.proyeccion, con los datos calculados de la
			 * proyección calculada proyUmic.
			 */
			almacenarDatos.almacenarProyeccion(lstProyUmic);

		}

		if (ModuloProvisionMatematica.LOG.isTraceEnabled()) {
			ModuloProvisionMatematica.LOG
					.trace("Fin función << moduloProvisionMatematica >> de la clase ModuloProvisionMatematica");
		}
	}

	/**
	 * Función encargada de calcular el total de cada una de la cuantias
	 * 
	 * @param totales
	 *            Estrutura de totales donde se guardará el total de cada
	 *            cuantía
	 * @param varProvBtiFcal
	 *            Provisión BTI de la umic a fecha de cierre
	 */
	private void calcularImportesTotalesCuantias(final TotalesFlujos totales, final BigDecimal varAjuste) {
		// Variables locales
		BigDecimal totfprob = BigDecimal.ZERO;
		BigDecimal totfprobtanul = BigDecimal.ZERO;
		BigDecimal totprovision = BigDecimal.ZERO;
		BigDecimal totcola = BigDecimal.ZERO;
		// Fin variables locales

		/**
		 * Total de flujos probables por tabla de experiencia de todas las
		 * cuantías de la umic. - total.flujos.totfprob = total.flujos.totfpvida
		 * + total.flujos.totfpfall + total.flujos.totfpgto +
		 * total.flujos.totfpcomi + total.flujos.totfprte +
		 * total.flujos.totfpcompl - total.flujos.totfpprim
		 * 
		 * Total de flujos probables por tabla de tasa de anulación de todas las
		 * cuantías de la umic. - total.flujos.totfprobtanul =
		 * total.flujos.totfpnavida + total.flujos.totfpnafall +
		 * total.flujos.totfpnagto + total.flujos.totfpnacomi +
		 * total.flujos.totfpnarte + total.flujos.totfpnacompl -
		 * total.flujos.totfpnaprim
		 * 
		 * Total de flujos probables actualizados de todas las cuantías de la
		 * umic. - total.flujos.totprovision = total.flujos.totfactvida +
		 * total.flujos.totfactfall + total.flujos.totfactgtos +
		 * total.flujos.totfactcomi + total.flujos.totfactrte +
		 * total.flujos.totfactcompl - total.flujos.totfactprim
		 * 
		 * Total de la cola de flujos probables actualizados de todas las
		 * cuantías de la umic. - total.flujos.totcola =
		 * total.flujos.totcolavida + total.flujos.totcolafall +
		 * total.flujos.totcolagtos + total.flujos.totcolacomi +
		 * total.flujos.totcolarte + total.flujos.totcolacompl -
		 * total.flujos.totcolaprim
		 */

		totfprob = totales.getTotfpvida().add(totales.getTotfpfall()).add(totales.getTotfpgto())
				.add(totales.getTotfpcom()).add(totales.getTotfpcompl()).add(totales.getTotfprte()).subtract(totales.getTotfpprim());
		totales.setTotfprob(totfprob.setScale(ConstantsFunciones.CTE_2, RoundingMode.HALF_DOWN));

		totfprobtanul = totales.getTotfpnavida().add(totales.getTotfpnafall()).add(totales.getTotfpnagto())
				.add(totales.getTotfpnacom()).add(totales.getTotfpnarte()).add(totales.getTotfpnacompl())
				.subtract(totales.getTotfpnaprim());
		totales.setTotfprobtanul(totfprobtanul.setScale(ConstantsFunciones.CTE_2, RoundingMode.HALF_DOWN));

		totprovision = varAjuste.multiply(totales.getTotfactvida().add(totales.getTotfactfall())
				.add(totales.getTotfactgto()).add(totales.getTotfactcom()).add(totales.getTotfactrte())
				.add(totales.getTotfactcompl()).subtract(totales.getTotfactprim()));
		totales.setTotprovision(totprovision.setScale(ConstantsFunciones.CTE_2, RoundingMode.HALF_DOWN));

		totcola = totales.getTotcolavida().add(totales.getTotcolafall()).add(totales.getTotcolagto())
				.add(totales.getTotcolacom()).add(totales.getTotcolarte()).add(totales.getTotcolacompl())
				.subtract(totales.getTotcolaprim());
		totales.setTotcola(totcola.setScale(ConstantsFunciones.CTE_2, RoundingMode.HALF_DOWN));

	}

	/**
	 * Se inicializan los bloques de corriente que no hayan sido creados
	 * 
	 * @param detalleCorriente
	 * @return
	 */
	private void asegurarBloquesInicializados(final DetalleCorriente detalleCorriente) {

		if (detalleCorriente.getBloqueVida() == null) {
			detalleCorriente.setBloqueVida(new BloqueCorriente());
		}
		if (detalleCorriente.getBloqueFall() == null) {
			detalleCorriente.setBloqueFall(new BloqueCorriente());
		}
		if (detalleCorriente.getBloqueCompl() == null) {
			detalleCorriente.setBloqueCompl(new BloqueCorriente());
		}
		if (detalleCorriente.getBloqueGto() == null) {
			detalleCorriente.setBloqueGto(new BloqueCorriente());
		}
		if (detalleCorriente.getBloqueComi() == null) {
			detalleCorriente.setBloqueComi(new BloqueCorriente());
		}
		if (detalleCorriente.getBloqueRte() == null) {
			detalleCorriente.setBloqueRte(new BloqueCorriente());
		}
		if (detalleCorriente.getBloquePrim() == null) {
			detalleCorriente.setBloquePrim(new BloqueCorriente());
		}

		if (detalleCorriente.getBloqueGtoAd() == null) {
			detalleCorriente.setBloqueGtoAd(new BloqueCorriente());
		}

		// Se inicializan los totales
		TotalFlujoProyeccion totalFlujo = detalleCorriente.getTotalFlujoProyeccion();

		if (totalFlujo == null) {
			totalFlujo = new TotalFlujoProyeccion();
			detalleCorriente.setTotalFlujoProyeccion(totalFlujo);
		}

		if (totalFlujo.getSumfprob() == null) {
			totalFlujo.setSumfprob(BigDecimal.ZERO);
		}
		if (totalFlujo.getSumfprobtanul() == null) {
			totalFlujo.setSumfprobtanul(BigDecimal.ZERO);
		}
		if (totalFlujo.getSumcola() == null) {
			totalFlujo.setSumcola(BigDecimal.ZERO);
		}
		if (totalFlujo.getSumprovision() == null) {
			totalFlujo.setSumprovision(BigDecimal.ZERO);
		}
		if (totalFlujo.getProvbtiproy() == null) {
			totalFlujo.setProvbtiproy(BigDecimal.ZERO);
		}
	}

	/**
	 * Se inicializan los importes que no hayan sido inicializados.
	 * 
	 * @param bloqueCorriente
	 */
	private void asegurarImportesInicializados(final BloqueCorriente bloqueCorriente) {
		if (bloqueCorriente.getImpFlujoNominal() == null) {
			bloqueCorriente.setImpFlujoNominal(BigDecimal.ZERO);
		}
		if (bloqueCorriente.getImpFlujoProbable() == null) {
			bloqueCorriente.setImpFlujoProbable(BigDecimal.ZERO);
		}
		if (bloqueCorriente.getImpFlujoNoAnulado() == null) {
			bloqueCorriente.setImpFlujoNoAnulado(BigDecimal.ZERO);
		}
		if (bloqueCorriente.getImpFlujoActualizado() == null) {
			bloqueCorriente.setImpFlujoActualizado(BigDecimal.ZERO);
		}
		if (bloqueCorriente.getImpProvi() == null) {
			bloqueCorriente.setImpProvi(BigDecimal.ZERO);
		}
	}

	/**
	 * Función que obtiene el acumulado de los importes de cada periodo por
	 * conceptos
	 * 
	 * @param proyUmic
	 *            Estructura detalleCorrientes de la umic
	 * @param totales
	 *            Estrutura de totales donde se guardara el importe de cada
	 *            periodo por concepto
	 */
	private void acumularImportesPorConcepto(final List<DetalleCorriente> proyUmic, final TotalesFlujos totales) {

		/**
		 * Para cada umic se obtendrán las distintas corrientes previamente
		 * calculadas para cada base técnica, para acumular y totalizar los
		 * importes de los distintos conceptos de cada corriente.
		 */
		if (proyUmic != null && proyUmic.size() > 0) {
			if (proyUmic.get(0).getTotalFlujoProyeccion() != null
					&& proyUmic.get(0).getTotalFlujoProyeccion().getProvbtiproy() != null) {
				totales.setProvbtifcal(proyUmic.get(0).getTotalFlujoProyeccion().getProvbtiproy());
			} else {
				totales.setProvbtifcal(BigDecimal.ZERO);
			}
			for (int i = 0; i < proyUmic.size(); i++) {
				final DetalleCorriente detalleCorriente = proyUmic.get(i);

				// Se crean ahora los bloques que no haben sido calculados
				asegurarBloquesInicializados(detalleCorriente);

				// Se inicializan los importes que no han sido calculados
				asegurarImportesInicializados(detalleCorriente.getBloqueVida());
				asegurarImportesInicializados(detalleCorriente.getBloqueFall());
				asegurarImportesInicializados(detalleCorriente.getBloqueCompl());
				asegurarImportesInicializados(detalleCorriente.getBloqueGto());
				asegurarImportesInicializados(detalleCorriente.getBloqueComi());
				asegurarImportesInicializados(detalleCorriente.getBloqueRte());
				asegurarImportesInicializados(detalleCorriente.getBloquePrim());

				asegurarImportesInicializados(detalleCorriente.getBloqueGtoAd());

				final BloqueCorriente bloqueVida = detalleCorriente.getBloqueVida();
				final BloqueCorriente bloqueFall = detalleCorriente.getBloqueFall();
				final BloqueCorriente bloqueCompl = detalleCorriente.getBloqueCompl();
				final BloqueCorriente bloqueGto = detalleCorriente.getBloqueGto();
				final BloqueCorriente bloqueComi = detalleCorriente.getBloqueComi();
				final BloqueCorriente bloqueRte = detalleCorriente.getBloqueRte();
				final BloqueCorriente bloquePrim = detalleCorriente.getBloquePrim();

				final BloqueCorriente bloqueGtoAd = detalleCorriente.getBloqueGtoAd();

				final TotalFlujoProyeccion totalFlujo = detalleCorriente.getTotalFlujoProyeccion();

				/************ totalFlujoProyeccion ***********/

				/**
				 * Los conceptos de Prima restan siempre.
				 */

				/**
				 * Total de flujos probables por tabla de experiencia -
				 * proyUmic(j).totalFlujoProyeccion.sumfprob =
				 * proyUmic(j).totalFlujoProyeccion.sumfprob +
				 * proyUmic(j).corrienteVida.impProbable +
				 * proyUmic(j).corrienteFallecimiento.impProbable +
				 * proyUmic(j).corrienteComplementario.impProbable +
				 * proyUmic(j).corrienteGasto.impProbable +
				 * proyUmic(j).corrienteComision.impProbable +
				 * proyUmic(j).corrienteRescate.impProbable -
				 * proyUmic(j).corrientePrima.impProbable
				 */
				totalFlujo.setSumfprob(bloqueVida.getImpFlujoProbable().add(bloqueFall.getImpFlujoProbable())
						.add(bloqueCompl.getImpFlujoProbable()).add(bloqueGto.getImpFlujoProbable())
						.add(bloqueComi.getImpFlujoProbable()).add(bloqueRte.getImpFlujoProbable())
						.subtract(bloquePrim.getImpFlujoProbable().add(bloqueGtoAd.getImpFlujoProbable()))
						.setScale(ConstantsFunciones.CTE_2, RoundingMode.HALF_DOWN));

				/**
				 * Total de flujos probables por tabla de tasa de anulación -
				 * proyUmic(j).totalFlujoProyeccion.sumfprobtanul =
				 * proyUmic(j).totalFlujoProyeccion.sumfprobtanul +
				 * proyUmic(j).corrienteVida.impProbableNoAnulado +
				 * proyUmic(j).corrienteFallecimiento.impProbableNoAnulado +
				 * proyUmic(j).corrienteComplementario.impProbableNoAnulado +
				 * proyUmic(j).corrienteGasto.impProbableNoAnulado +
				 * proyUmic(j).corrienteComision.impProbableNoAnulado +
				 * proyUmic(j).corrienteRescate.impProbableNoAnulado -
				 * proyUmic(j).corrientePrima.impProbableNoAnulado;
				 */
				totalFlujo.setSumfprobtanul(bloqueVida.getImpFlujoNoAnulado().add(bloqueFall.getImpFlujoNoAnulado())
						.add(bloqueCompl.getImpFlujoNoAnulado()).add(bloqueGto.getImpFlujoNoAnulado())
						.add(bloqueComi.getImpFlujoNoAnulado()).add(bloqueRte.getImpFlujoNoAnulado())
						.subtract(bloquePrim.getImpFlujoNoAnulado().add(bloqueGtoAd.getImpFlujoNoAnulado()))
						.setScale(ConstantsFunciones.CTE_2, RoundingMode.HALF_DOWN));

				/**
				 * Total de la cola de flujos probables actualizados -
				 * proyUmic(j).totalFlujoProyeccion.sumcola =
				 * proyUmic(j).totalFlujoProyeccion.sumcola +
				 * proyUmic(j).corrienteVida.impProvi +
				 * proyUmic(j).corrienteFallecimiento.impProvi +
				 * proyUmic(j).corrienteComplementario.impProvi +
				 * proyUmic(j).corrienteGasto.impProvi +
				 * proyUmic(j).corrienteComision.impProvi +
				 * proyUmic(j).corrienteRescate.impProvi -
				 * proyUmic(j).corrientePrima.impProvi;
				 */
				totalFlujo.setSumcola(bloqueVida.getImpProvi().add(bloqueFall.getImpProvi())
						.add(bloqueCompl.getImpProvi()).add(bloqueGto.getImpProvi()).add(bloqueComi.getImpProvi())
						.add(bloqueRte.getImpProvi()).subtract(bloquePrim.getImpProvi().add(bloqueGtoAd.getImpProvi()))
						.setScale(ConstantsFunciones.CTE_2, RoundingMode.HALF_DOWN));

				/**
				 * Total de flujos probables actualizados (Provisión Matemática)
				 * - varProvMat = proyUmic(j).corrienteVida.impFlujoActualizado
				 * + proyUmic(j).corrienteFallecimiento.impFlujoActualizado +
				 * proyUmic(j).corrienteComplementario. impFlujoActualizado +
				 * proyUmic(j).corrienteGasto.impFlujoActualizado +
				 * proyUmic(j).corrienteComision.impFlujoActualizado +
				 * proyUmic(j).corrienteRescate.impFlujoActualizado -
				 * proyUmic(j).corrientePrima.impFlujoActualizado;
				 */
				final BigDecimal varProvMat = bloqueVida.getImpFlujoActualizado()
						.add(bloqueFall.getImpFlujoActualizado()).add(bloqueCompl.getImpFlujoActualizado())
						.add(bloqueGto.getImpFlujoActualizado()).add(bloqueComi.getImpFlujoActualizado())
						.add(bloqueRte.getImpFlujoActualizado()).subtract(bloquePrim.getImpFlujoActualizado()
						.add(bloqueGtoAd.getImpFlujoActualizado())		
								.setScale(ConstantsFunciones.CTE_2, RoundingMode.HALF_DOWN));

				/**
				 * proyUmic(j).totalFlujoProyeccion.sumprovision =
				 * proyUmic(j).totalFlujoProyeccion.sumprovision + varProvMat;
				 */
				totalFlujo.setSumprovision(varProvMat);

				/************ totalFlujosUmic ***********/

				/**
				 * - total.flujos.totfpvida = total.flujos.totfpvida +
				 * proyUmic(j).corrienteVida.impFlujoProbable; -
				 * total.flujos.totfpnavida = total.flujos.totfpnavida +
				 * proyUmic(j).corrienteVida.impFlujoNoAnulado; -
				 * total.flujos.totfactvida = total.flujos.totfactvida +
				 * proyUmic(j).corrienteVida.impFlujoActualizado; -
				 * total.flujos.totcolavida = total.flujos.totcolavida +
				 * proyUmic(j).corrienteVida.impProvi;
				 */
				totales.setTotfpvida(totales.getTotfpvida().add(bloqueVida.getImpFlujoProbable()));
				totales.setTotfpnavida(totales.getTotfpnavida().add(bloqueVida.getImpFlujoNoAnulado()));
				totales.setTotfactvida(totales.getTotfactvida().add(bloqueVida.getImpFlujoActualizado()));
				totales.setTotcolavida(totales.getTotcolavida().add(bloqueVida.getImpProvi()));

				/**
				 * - total.flujos.totfpfall = total.flujos.totfpfall +
				 * proyUmic(j).corrienteFallecimiento.impFlujoProbable; -
				 * total.flujos.totfpnafall = total.flujos.totfpnafall +
				 * proyUmic(j).corrienteFallecimiento.impFlujoNoAnulado; -
				 * total.flujos.totfactfall = total.flujos.totfactfall +
				 * proyUmic(j).corrienteFallecimiento.impFlujoActualizado; -
				 * total.flujos.totcolafall = total.flujos.totcolafall +
				 * proyUmic(j).corrienteFallecimiento.impProvi;
				 */
				totales.setTotfpfall(totales.getTotfpfall().add(bloqueFall.getImpFlujoProbable()));
				totales.setTotfpnafall(totales.getTotfpnafall().add(bloqueFall.getImpFlujoNoAnulado()));
				totales.setTotfactfall(totales.getTotfactfall().add(bloqueFall.getImpFlujoActualizado()));
				totales.setTotcolafall(totales.getTotcolafall().add(bloqueFall.getImpProvi()));

				/**
				 * - total.flujos.totfpcompl = total.flujos.totfpcompl +
				 * proyUmic(j).corrienteComplementario.impFlujoProbable; -
				 * total.flujos.totfpnacompl = total.flujos.totfpnacompl +
				 * proyUmic(j).corrienteComplementario.impFlujoNoAnulado; -
				 * total.flujos.totfactcompl = total.flujos.totfactcompl +
				 * proyUmic(j).corrienteComplementario.impFlujoActualizado; -
				 * total.flujos.totcolacompl = total.flujos.totcolacompl +
				 * proyUmic(j).corrienteComplementario.impProvi;
				 */
				totales.setTotfpcompl(totales.getTotfpcompl().add(bloqueCompl.getImpFlujoProbable()));
				totales.setTotfpnacompl(totales.getTotfpnacompl().add(bloqueCompl.getImpFlujoNoAnulado()));
				totales.setTotfactcompl(totales.getTotfactcompl().add(bloqueCompl.getImpFlujoActualizado()));
				totales.setTotcolacompl(totales.getTotcolacompl().add(bloqueCompl.getImpProvi()));

				/**
				 * - total.flujos.totfpgto = total.flujos.totfpgto +
				 * proyUmic(j).corrienteGasto.impFlujoProbable; -
				 * total.flujos.totfpnagto = total.flujos.totfpnagto +
				 * proyUmic(j).corrienteGasto.impFlujoNoAnulado; -
				 * total.flujos.totfactgto = total.flujos.totfactgto +
				 * proyUmic(j).corrienteGasto.impFlujoActualizado; -
				 * total.flujos.totcolagto = total.flujos.totcolagto +
				 * proyUmic(j).corrienteGasto.impProvi;
				 */
				totales.setTotfpgto(totales.getTotfpgto().add(bloqueGto.getImpFlujoProbable()));
				totales.setTotfpnagto(totales.getTotfpnagto().add(bloqueGto.getImpFlujoNoAnulado()));
				totales.setTotfactgto(totales.getTotfactgto().add(bloqueGto.getImpFlujoActualizado()));
				totales.setTotcolagto(totales.getTotcolagto().add(bloqueGto.getImpProvi()));

				/**
				 * - total.flujos.totfpcom = total.flujos.totfpcom +
				 * proyUmic(j).corrienteComision.impFlujoProbable; -
				 * total.flujos.totfpnacom = total.flujos.totfpnacom +
				 * proyUmic(j).corrienteComision.impFlujoNoAnulado; -
				 * total.flujos.totfactcom = total.flujos.totfactcom +
				 * proyUmic(j).corrienteComision.impFlujoActualizado; -
				 * total.flujos.totcolacom = total.flujos.totcolacom +
				 * proyUmic(j).corrienteComision.impProvi;
				 */
				totales.setTotfpcom(totales.getTotfpcom().add(bloqueComi.getImpFlujoProbable()));
				totales.setTotfpnacom(totales.getTotfpnacom().add(bloqueComi.getImpFlujoNoAnulado()));
				totales.setTotfactcom(totales.getTotfactcom().add(bloqueComi.getImpFlujoActualizado()));
				totales.setTotcolacom(totales.getTotcolacom().add(bloqueComi.getImpProvi()));

				/**
				 * - total.flujos.totfprte = total.flujos.totfprte +
				 * proyUmic(j).corrienteRescate.impFlujoProbable; -
				 * total.flujos.totfpnarte = total.flujos.totfpnarte +
				 * proyUmic(j).corrienteRescate.impFlujoNoAnulado; -
				 * total.flujos.totfactrte = total.flujos.totfactrte +
				 * proyUmic(j).corrienteRescate.impFlujoActualizado; -
				 * total.flujos.totcolarte = total.flujos.totcolarte +
				 * proyUmic(j).corrienteRescate.impProvi;
				 */
				totales.setTotfprte(totales.getTotfprte().add(bloqueRte.getImpFlujoProbable()));
				totales.setTotfpnarte(totales.getTotfpnarte().add(bloqueRte.getImpFlujoNoAnulado()));
				totales.setTotfactrte(totales.getTotfactrte().add(bloqueRte.getImpFlujoActualizado()));
				totales.setTotcolarte(totales.getTotcolarte().add(bloqueRte.getImpProvi()));
				totales.setTotNomRte(totales.getTotNomRte().add(bloqueRte.getImpFlujoNominal()));

				/**
				 * - total.flujos.totfpprim = total.flujos.totfpprim +
				 * proyUmic(j).corrientePrima.impFlujoProbable; -
				 * total.flujos.totfpnaprim = total.flujos.totfpnaprim +
				 * proyUmic(j).corrientePrima.impFlujoNoAnulado; -
				 * total.flujos.totfactprim = total.flujos.totfactprim +
				 * proyUmic(j).corrientePrima.impFlujoActualizado; -
				 * total.flujos.totcolaprim = total.flujos.totcolaprim +
				 * proyUmic(j).corrientePrima.impProvi;
				 */
				totales.setTotfpprim(totales.getTotfpprim().add(bloquePrim.getImpFlujoProbable()));
				totales.setTotfpnaprim(totales.getTotfpnaprim().add(bloquePrim.getImpFlujoNoAnulado()));
				totales.setTotfactprim(totales.getTotfactprim().add(bloquePrim.getImpFlujoActualizado()));
				totales.setTotcolaprim(totales.getTotcolaprim().add(bloquePrim.getImpProvi()));
				
				/**
				 * - total.flujos.totfpgtoad = total.flujos.totfpgtoad +
				 * proyUmic(j).corrienteGtoAd.impFlujoProbable; -
				 * total.flujos.totfpnagtoad = total.flujos.totfpnagtoad +
				 * proyUmic(j).corrienteGtoAd.impFlujoNoAnulado; -
				 * total.flujos.totfactgtoad = total.flujos.totfactgtoad +
				 * proyUmic(j).corrienteGtoAd.impFlujoActualizado; -
				 * total.flujos.totcolagtoad = total.flujos.totcolagtoad +
				 * proyUmic(j).corrienteGtoAd.impProvi;
				 */
				totales.setTotfpgtoad(totales.getTotfpgtoad().add(bloqueGtoAd.getImpFlujoProbable()));
				totales.setTotfpnagtoad(totales.getTotfpnagtoad().add(bloqueGtoAd.getImpFlujoNoAnulado()));
				totales.setTotfactgtoad(totales.getTotfactgtoad().add(bloqueGtoAd.getImpFlujoActualizado()));
				totales.setTotcolagtoad(totales.getTotcolagtoad().add(bloqueGtoAd.getImpProvi()));
				
				if (detalleCorriente.getRaumic() != null) {
					totales.setRaumic(totales.getRaumic().add(detalleCorriente.getRaumic()));
				}
				if (detalleCorriente.getCsmumic() != null) {
					totales.setCsmumic(totales.getCsmumic().add(detalleCorriente.getCsmumic()));
				}
				if (detalleCorriente.getPatronCSM() != null) {
					totales.setCsmajustado(totales.getCsmajustado().add(detalleCorriente.getPatronCSM()));
				}
				if (detalleCorriente.getRosspCSM() != null) {
					totales.setCsm003(totales.getCsm003().add(detalleCorriente.getRosspCSM()));
				}
			}
		}
	}

	/**
	 * Función encargada de establecer aquellos campos de la umic que no varian
	 * por corriente ni por periodo.<br>
	 * <br>
	 * Antes de recuperar las corrientes se inicializaran los campos de la
	 * estructura de salida y <br>
	 * se informaran los campos de la umic que no varían por corriente, ni por
	 * periodo.<br>
	 * - total.flujos.cnegocio = umic.datosgenerales.cnegocio<br>
	 * - total.flujos.ccanal = umic.datosgenerales.ccanal<br>
	 * - total.flujos.ccartera = umic.datosgenerales.ccartera<br>
	 * - total.flujos.fcierre = umic.datosgenerales.fcierre<br>
	 * - total.flujos.bt = umic.datosgenerales.bt<br>
	 * - total.flujos.kmodalidad = umic.datosgenerales.kmodalidad<br>
	 * - total.flujos.kpoliza = umic.datosgenerales.kpoliza<br>
	 * - total.flujos.ksubpoliza = umic.datosgenerales.ksubpoliza<br>
	 * - total.flujos.kcertificado = umic.datosgenerales.kcertificado<br>
	 * - total.flujos.nsuscri = umic.datosgenerales.nsuscri<br>
	 * - total.flujos.norden = umic.datosgenerales.norden<br>
	 * - total.flujos.kgarantia = umic.datosgenerales.kgarantia<br>
	 * - total.flujos.kprestacion = umic.datosgenerales.kprestacion<br>
	 * - total.flujos.kajuste = umic.datosgenerales.kajuste<br>
	 * - total.flujos.ctipoaport = umic.datosgenerales.ctipoaport<br>
	 * - total.flujos.totfpvida = 0<br>
	 * - total.flujos.totfpnavida = 0<br>
	 * - total.flujos.totfactvida = 0<br>
	 * - total.flujos.totcolavida = 0<br>
	 * - total.flujos.totfpfall = 0<br>
	 * - total.flujos.totfpnafall = 0<br>
	 * - total.flujos.totfactfall = 0<br>
	 * - total.flujos.totcolafall = 0<br>
	 * - total.flujos.totfpcompl = 0<br>
	 * - total.flujos.totfpnacompl = 0<br>
	 * - total.flujos.totfactcompl = 0<br>
	 * - total.flujos.totcolacompl = 0<br>
	 * - total.flujos.totfpgto = 0<br>
	 * - total.flujos.totfpnagto = 0<br>
	 * - total.flujos.totfactgto = 0<br>
	 * - total.flujos.totcolagto = 0<br>
	 * - total.flujos.totfpcomi = 0<br>
	 * - total.flujos.totfpnacomi = 0<br>
	 * - total.flujos.totfactcomi = 0<br>
	 * - total.flujos.totcolacomi = 0<br>
	 * - total.flujos.totfprte = 0<br>
	 * - total.flujos.totfpnarte = 0<br>
	 * - total.flujos.totfactrte = 0<br>
	 * - total.flujos.totcolarte = 0<br>
	 * - total.flujos.totfpprim = 0<br>
	 * - total.flujos.totfpnaprim = 0<br>
	 * - total.flujos.totfactprim = 0<br>
	 * - total.flujos.totcolaprim = 0<br>
	 * - total.flujos.totfprob = 0<br>
	 * - total.flujos.totfprobtanul = 0<br>
	 * - total.flujos.totprovision = 0<br>
	 * - total.flujos.totcola = 0<br>
	 * - total.flujos.provbtifcal = 0<br>
	 * - total.flujos.ctipoaport = umic.datosGenerales.ctipoaport<br>
	 * - total.flujos.kramo = umic.datosGenerales.kramo<br>
	 * - total.flujos.fsuscri = umic.fechas.fecinisus<br>
	 * - total.flujos.kcarterainv = umic.datosGenerales.
	 * umic.datosGenerales.kcarterainv<br>
	 * - total.flujos.gapAct = umic.datosGenerales. gapAct<br>
	 * - total.flujos.intFecCalc = CalcularIntFecCalc(btcUmic,fcalc)<br>
	 * - total.flujos.ctipoprovi = umic.datosGenerales.ctipoprovi<br>
	 * - total.flujos.spcom = umic.datosGenerales.spcom<br>
	 * - total.flujos.koficont = umic.datosGenerales.koficont<br>
	 * - total.flujos.pfpinv = umic.primas.pfpinv<br>
	 * - total.flujos.ctipramo = umic.datosGenerales.ctipRamo<br>
	 * - total.flujos.segmento1 = umic.datosGenerales.segmento1<br>
	 * - total.flujos.tipoSubriesgo = umic.datosGenerales.tipoSubriesgo<br>
	 * - total.flujos.nuevaProduc = umic.datosGenerales.nuevaProduc<br>
	 * - total.flujos.indicrescate = umic.rescates. indicrescate<br>
	 * - total.flujos.totfnrte = proyUmic(1).corrienteRescate.impFlujoNominal
	 * <br>
	 * - total.flujos.curvaTi = btcUmic.curvaTi<br>
	 * - total.flujos.totfnfall =
	 * proyUmic(1).corrienteFallecimiento.impFlujoNominal<br>
	 * 
	 * @param umic
	 *            datos e la umic
	 * @param totales
	 *            estrutura de totales donde se guardaran los campos que no
	 *            varias por corriente ni por periodo
	 * @param btcUmic
	 *            Contiene el detalle de la base técnica de cálculo para la
	 *            umic.
	 */
	private void inicializarCamposNoVariaPorCorriente(final Umic umic, final TotalesFlujos totales,
			final DetalleBaseTecnica detalleBT, final Timestamp fcalc, List<DetalleCorriente> proyUmic) {
		final DatosGenerales datosGenerales = umic.getDatosGenerales();
		final Fechas fechas = umic.getFechas();
		totales.setCnegocio(datosGenerales.getCnegocio());
		totales.setCcanal(datosGenerales.getCcanal());
		totales.setCcartera(datosGenerales.getCcartera());
		totales.setFcierre(detalleBT.getFecCierre());
		totales.setBt(detalleBT.getBaseTec());
		totales.setKmodalidad(datosGenerales.getKmodalidad());
		totales.setKpoliza(datosGenerales.getKpoliza());
		totales.setKsubpoliza(datosGenerales.getKsubpoliza());
		totales.setKcertificado(datosGenerales.getKcertificado());
		totales.setNsuscri(datosGenerales.getNsuscri());
		totales.setNorden(datosGenerales.getNorden());
		totales.setKgarantia(datosGenerales.getKgarantia());
		totales.setKprestacion(datosGenerales.getKprestacion());
		totales.setKajuste(datosGenerales.getKajuste());
		totales.setCtipoaport(datosGenerales.getCtipoaport());
		totales.setKmodext(datosGenerales.getKmodext());
		totales.setUoa(umic.getDatosNiif17().getuoa());
		totales.setKcontrato(umic.getDatosNiif17().getkcarcontacto());
		totales.setTotfpvida(BigDecimal.ZERO);
		totales.setTotfpnavida(BigDecimal.ZERO);
		totales.setTotfactvida(BigDecimal.ZERO);
		totales.setTotcolavida(BigDecimal.ZERO);
		totales.setTotfpfall(BigDecimal.ZERO);
		totales.setTotfpnafall(BigDecimal.ZERO);
		totales.setTotfactfall(BigDecimal.ZERO);
		totales.setTotcolafall(BigDecimal.ZERO);
		totales.setTotfpcompl(BigDecimal.ZERO);
		totales.setTotfpnacompl(BigDecimal.ZERO);
		totales.setTotfactcompl(BigDecimal.ZERO);
		totales.setTotcolacompl(BigDecimal.ZERO);
		totales.setTotfpgto(BigDecimal.ZERO);
		totales.setTotfpnagto(BigDecimal.ZERO);
		totales.setTotfactgto(BigDecimal.ZERO);
		totales.setTotcolagto(BigDecimal.ZERO);
		totales.setTotfpcom(BigDecimal.ZERO);
		totales.setTotfpnacom(BigDecimal.ZERO);
		totales.setTotfactcom(BigDecimal.ZERO);
		totales.setTotcolacom(BigDecimal.ZERO);
		totales.setTotfprte(BigDecimal.ZERO);
		totales.setTotfpnarte(BigDecimal.ZERO);
		totales.setTotfactrte(BigDecimal.ZERO);
		totales.setTotcolarte(BigDecimal.ZERO);
		totales.setTotfpprim(BigDecimal.ZERO);
		totales.setTotfpnaprim(BigDecimal.ZERO);
		totales.setTotfactprim(BigDecimal.ZERO);
		totales.setTotcolaprim(BigDecimal.ZERO);
		totales.setTotfprob(BigDecimal.ZERO);
		totales.setTotfprobtanul(BigDecimal.ZERO);
		totales.setTotprovision(BigDecimal.ZERO);
		totales.setTotcola(BigDecimal.ZERO);
		totales.setProvbtifcal(BigDecimal.ZERO);
		totales.setTotNomRte(BigDecimal.ZERO);
		totales.setKramo(datosGenerales.getKramo());
		totales.setFsuscri(fechas.getFecinisus());
		totales.setKcarterainv(datosGenerales.getKcarterainv());
		totales.setGapAct(datosGenerales.getGapAct());
		totales.setGestionit(datosGenerales.getGestionit());
		totales.setRaumic(BigDecimal.ZERO);
		totales.setCsmumic(BigDecimal.ZERO);
		totales.setCsmajustado(BigDecimal.ZERO);
		totales.setCsm003(BigDecimal.ZERO);
		totales.setTotfpgtoad(BigDecimal.ZERO);
		totales.setTotfpnagtoad(BigDecimal.ZERO);
		totales.setTotfactgtoad(BigDecimal.ZERO);
		totales.setTotcolagtoad(BigDecimal.ZERO);
		if (totales.getBt().equals(ConstantsModulos.CTE_BT_BEL) || 
				totales.getBt().equals(ConstantsModulos.CTE_VAL_BELCOA) || 
				totales.getBt().equals(ConstantsModulos.CTE_VAL_BELCLR) || 
				totales.getBt().equals(ConstantsModulos.CTE_VAL_SCRTIU) || 
				totales.getBt().equals(ConstantsModulos.CTE_VAL_SCRTID) || 
				totales.getBt().equals(ConstantsModulos.CTE_VAL_SCRGTO) || 
				totales.getBt().equals(ConstantsModulos.CTE_VAL_SCRMFE) || 
				totales.getBt().equals(ConstantsModulos.CTE_VAL_SCRMMI) || 
				totales.getBt().equals(ConstantsModulos.CTE_VAL_SCRMCF) || 
				totales.getBt().equals(ConstantsModulos.CTE_VAL_SCRMCI) || 
				totales.getBt().equals(ConstantsModulos.CTE_VAL_SCRLFE) || 
				totales.getBt().equals(ConstantsModulos.CTE_VAL_SCRLMI) || 
				totales.getBt().equals(ConstantsModulos.CTE_VAL_SCRINC) || 
				totales.getBt().equals(ConstantsModulos.CTE_VAL_SCRVM)  || 
				totales.getBt().equals(ConstantsModulos.CTE_VAL_SCRAEP) || 
				totales.getBt().equals(ConstantsModulos.CTE_VAL_SCRAEN) || 
				totales.getBt().equals(ConstantsModulos.CTE_VAL_SCRAIP) || 
				totales.getBt().equals(ConstantsModulos.CTE_VAL_SCRAIN) || 
				totales.getBt().equals(ConstantsModulos.CTE_VAL_SCRANM)) {
			totales.setIntfeccal(BigDecimal.ZERO);
		} else {
			totales.setIntfeccal(calcularIntFecCalc(detalleBT, fcalc));
		}
		
		totales.setIntBTI(calcularIntBTI(umic, fcalc));
		totales.setCtipoprovi(datosGenerales.getCtipoprovi());
		totales.setSpcom(datosGenerales.getSpcom());
//INI-TAR00400971
		totales.setKbencon(datosGenerales.getKbencon());
//FIN-TAR00400971
		totales.setKoficont(datosGenerales.getKoficont());
		totales.setPfpinv(umic.getPrimas().getPfpinv());
		totales.setCtipramo(datosGenerales.getCtipramo());
		totales.setSegmento1(datosGenerales.getSegmento1());
		totales.setTiposubriesgo(datosGenerales.getTipoSubriesgo());
		totales.setNuevaprodu(datosGenerales.getNuevaProduc());
		totales.setIndicrescate(umic.getRescates().getIndicrescate());
		

		if (proyUmic.get(ConstantsFunciones.CTE_0).getBloqueRte() == null) {
			totales.setTotfnrte(BigDecimal.ZERO);
		} else {
			totales.setTotfnrte(proyUmic.get(ConstantsFunciones.CTE_0).getBloqueRte().getImpFlujoNominal());
		}

		totales.setCurvati(detalleBT.getCurvaTi());

		if (proyUmic.get(ConstantsFunciones.CTE_0).getBloqueFall() == null) {
			totales.setTotfnfall(BigDecimal.ZERO);
		} else {
			totales.setTotfnfall(proyUmic.get(ConstantsFunciones.CTE_0).getBloqueFall().getImpFlujoNominal());
		}
	}

	/**
	 * Función encargada de hallar el interés técnico a Fecha de Cálculo para la
	 * umic.
	 * 
	 * Para ello se buscará dentro del array de tramos de interés de la Base
	 * Técnica de cálculo de la umic, btcUmic, el tramo de interés que cumple
	 * con las condiciones: fecIniTramo(i) <= fcalc <= fecFinTramo(i)
	 * 
	 * Para el tramo así hallado se retornará el porcentaje de interés
	 * correspondiente al ese elemento del array de tramos de interés,
	 * itcalc(i):
	 * 
	 * 
	 * @param detallesBT
	 * @param btcUmic
	 * @return
	 * 
	 */
	private BigDecimal calcularIntFecCalc(final DetalleBaseTecnica detalleBT, final Timestamp fcalc) {

		final List<Timestamp> fecInitramo = detalleBT.getFecInitramo();
		final List<Timestamp> fecFintramo = detalleBT.getFecfintramo();
		BigDecimal itcalc = null;

		for (int i = 0; i < fecInitramo.size(); i++) {
			if (fecInitramo.get(i) != null && fecFintramo.get(i) != null) {
				if ((fcalc.after(fecInitramo.get(i)) || fcalc.equals(fecInitramo.get(i)))
						&& (fcalc.before(fecFintramo.get(i)) || fcalc.equals(fecFintramo.get(i)))) {
					itcalc = detalleBT.getItcalc().get(i);
				}
			}
		}
		return itcalc;

	}

	private BigDecimal calcularIntBTI(final Umic umic, final Timestamp fcalc) {

		final BaseTecnicaInicial bti = umic.getBti();

		final List<Timestamp> fecInitramo = new ArrayList<Timestamp>();
		fecInitramo.add(bti.getFecIniTramo1());
		fecInitramo.add(bti.getFecIniTramo2());
		fecInitramo.add(bti.getFecIniTramo3());
		fecInitramo.add(bti.getFecIniTramo4());
		fecInitramo.add(bti.getFecIniTramo5());

		final List<Timestamp> fecFintramo = new ArrayList<Timestamp>();
		fecFintramo.add(bti.getFecFinTramo1());
		fecFintramo.add(bti.getFecFinTramo2());
		fecFintramo.add(bti.getFecFinTramo3());
		fecFintramo.add(bti.getFecFinTramo4());
		fecFintramo.add(bti.getFecFinTramo5());

		final List<BigDecimal> pintertecnI = new ArrayList<BigDecimal>();
		pintertecnI.add(bti.getPintertecnI1());
		pintertecnI.add(bti.getPintertecnI2());
		pintertecnI.add(bti.getPintertecnI3());
		pintertecnI.add(bti.getPintertecnI4());
		pintertecnI.add(bti.getPintertecnI5());

		BigDecimal itbti = null;

		for (int i = 0; i < fecInitramo.size(); i++) {
			if (fecInitramo.get(i) != null && fecFintramo.get(i) != null) {
				if ((fcalc.after(fecInitramo.get(i)) || fcalc.equals(fecInitramo.get(i)))
						&& (fcalc.before(fecFintramo.get(i)) || fcalc.equals(fecFintramo.get(i)))) {
					itbti = pintertecnI.get(i);
				}
			}
		}
		return itbti;

	}

}
