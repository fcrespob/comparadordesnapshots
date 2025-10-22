/* MODIFICACION:MU-2019-047428:Código SD01632215: INCIDENCIA ERROR TECNICO NO CONTROLADO RENTAS REVISABLES
    Se controla que no haya ningun tramo casado a la fecha de calculo.
   FECHA: 24/07/2019 
   AUTOR: INDRA
*/

package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilFechas.Fecha;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada de implementar el modulo encargado de calcular del concepto
 * resultante de la aplicación de una actualización financiera muy similar a la
 * de la “Cuantía Actualizada”, pero alterando el procedimiento y las fechas a
 * las que se actualizan dichos importes. Como resultado obtendremos una
 * corriente importes actualizados para cada periodo de proyección de la
 * corriente.
 * 
 * @author jguijarro
 */
public class ModuloImporteProvi implements Modulo {

	private static final String CLAVE_MARCA_SW = ConstantsModulos.CTE_MARCA_SW;
	private static final String CLAVE_VAR_MARCA_SW = CLAVE_MARCA_SW.concat(ConstantsFactorias.MODULO_IMP_PROVI);

	/** Cte para log. */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloImporteProvi.class);

	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_IMP_PROVI;
	}

	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la
	 * llamada a la función que realiza los calculos del modulo.
	 */
	@SuppressWarnings("unchecked")
	public final Object execute(final Object... args) throws Solvencia2Excepcion {

		ModuloImporteProvi.LOG.trace("Inicio de execute en clase ModuloImporteProvi");

		try {
			// Recuperamos los datos que le pasaremos a la función
			// moduloImporteProvi
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_UMIC_FECHAS];
			final FichaProceso fichaProceso = (FichaProceso) args[ConstantsModulos.PARAM_FIC_PROC_FECHAS];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_BTC_FECHAS];
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_DETALLE_FECHAS];
			final String subProcesoActual = (String) args[ConstantsModulos.PARAM_SUBRPROCESO_FECHAS];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_MAPA_VAR_FECHAS];

			// Ejecutamos el modulos moduloImporteProvi
			moduloImporteProvi(umic, fichaProceso, btcUmic, proyUmic, subProcesoActual, mapVariables);

		} catch (Solvencia2Excepcion e) {
			ModuloImporteProvi.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloImporteProvi.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		ModuloImporteProvi.LOG.trace("Fin de execute en clase ModuloImporteProvi");

		return null;
	}

	/**
	 * Calculo del concepto resultante de la aplicación de una actualización
	 * financiera muy similar a la de la “Cuantía Actualizada”, pero alterando el
	 * procedimiento y las fechas a las que se actualizan dichos importes. Como
	 * resultado obtendremos una corriente importes actualizados para cada periodo
	 * de proyección de la corriente.
	 * 
	 * @param umic              Contiene los datos de la Umic que se está
	 *                          procesando.
	 * @param fichaProceso      Contiene los datos del proceso necesarios para su
	 *                          ejecución.
	 * @param detalleBT         Contiene el detalle de la base técnica de cálculo
	 *                          para la umic.
	 * @param lstDetalleCorrien Corriente de la umic.
	 * @param subProcesoActual  Código el subproceso que se está ejecutando.
	 */
	private void moduloImporteProvi(final Umic umic, final FichaProceso fichaProceso,
			final DetalleBaseTecnica detalleBT, final List<DetalleCorriente> lstDetalleCorrien,
			final String subProcesoActual, final Map<String, Object> mapVariables) {
		// Variables locales
		final IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
		final IObtenerConfiguracion obtConfi = FachadaServicios.getObtenerConfiguracion();
		int iteracion = ConstantsFunciones.CTE_1;
		BigDecimal impFlujoProvi = BigDecimal.ZERO;
		BigDecimal impFlujoProviVida, impFlujoProviFall, impFlujoProviCompl, impFlujoProviComi, impFlujoProviGto,
				impFlujoProviRte, impFlujoProviPrim, impFlujoProviGtoAd;
		String varCriterFec = ConstantsFunciones.CTE_CADENA_VACIA;
		// Fin variables locales

		/**
		 * 
		 * En primer lugar se recuperará el módulo (indicador este concepto) del importe
		 * actualizado para la modalidad, garantía, prestación y base técnica de la
		 * umic. Si el indicador es ‘N’ no deberá calcularse el importe Provi para el
		 * subproceso, terminando así el programa para la umic. Si el indicador es ‘S’
		 * si deberá calcularse el importe Provi para el subproceso, tal y como se
		 * detalla a continuación. Para cada proyección recuperada, proyUmic, se deberá
		 * calcular el importe actualizado para la corriente, desde la primar de las
		 * proyecciones hasta la última. Una vez calculado el importe provi de todos los
		 * periodos de proyección, se escribirá el resultado en el importe
		 * correspondiente de la corriente. Una vez procesados todos los puntos de la
		 * proyección, los mismos serán almacenados para su posterior recuperación y
		 * generación de la salida de la umic. Si se produce un error en algún punto del
		 * programa se registrará dicho error de la umic en el fichero de incidencias,
		 * terminando el programa para dicha umic.
		 * 
		 *
		 * Obtiene el módulo de cálculo a ejecutar en el programa. • varModuloCalc =
		 * obtenerConfiguracion.recuperarModulo con los parámetros: o
		 * Umic.datosGenerales.kmodalidad o Umic.datosGenerales.kgarantia o
		 * Umic.datosGenerales.kprestacion o fichaProceso.ktipobt o codSubproceso o
		 * tipoElemento = 05
		 */
		final int longiDetalle = lstDetalleCorrien.size();
		Integer marcaSw = (Integer) mapVariables.get(CLAVE_VAR_MARCA_SW);

		if (lstDetalleCorrien.size() == 0 || lstDetalleCorrien.isEmpty() || null == lstDetalleCorrien) {
			almacenarDatos.almacenarProyeccion(lstDetalleCorrien);
		} else {

			if (null == marcaSw) {
				lstDetalleCorrien.get(0).setSw_periodo_casado(null);
			}
			/**
			 * En primer lugar se debe determinar si la UMIC tratada debe calcular el
			 * concepto.
			 * 
			 * De entre los tramos de interés de la umic (tramos de 1 a 5), se buscará el
			 * que cumpla con las siguientes condiciones:
			 * 
			 * btcUmic.fecIniTramoX <= fichaProceso.fcalc <= btcUmic.fecfinTramoX
			 * 
			 * y se evaluará el indicador de casamiento de dicho tramo. Para ello: - Si para
			 * el tramo X así hallado se tiene btcUmic.swcasadoX = ‘N’ => no debe calcularse
			 * concepto para la umic, por lo que no se realizará ningún cálculo, finalizando
			 * así el cálculo del programa para umic
			 * 
			 * - Si para el tramo X así hallado se tiene btcUmic.swcasadoX = ‘S’ => si debe
			 * calcularse concepto para la umic, y se realizará como se detalla:
			 */
			String casadoX = ConstantsFunciones.CTE_CADENA_VACIA;

			casadoX = obtenerFechasIniFinTramoParaCalculoProvi(detalleBT, fichaProceso.getFcalc());

			if (ConstantsModulos.CTE_S.equals(casadoX)) {

				varCriterFec = (String) obtConfi.recuperarDefinicionAuxiliar(umic.getDatosGenerales().getCcartera(),
						umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(),
						detalleBT.getBaseTec(), ConstantsModulos.CTE_VA_CRIT_FEC);

				for (int i = 0; i < longiDetalle; i++) {

					impFlujoProvi = calcularFlujoProvi(detalleBT, lstDetalleCorrien, fichaProceso, umic, iteracion,
							longiDetalle, subProcesoActual, mapVariables, varCriterFec);

					if (null == impFlujoProvi) {
						impFlujoProvi = BigDecimal.ZERO;
					}

					lstDetalleCorrien.get(iteracion - 1).getBloqueBySubproceso(subProcesoActual)
							.setImpProvi(impFlujoProvi.setScale(ConstantsFunciones.CTE_2, RoundingMode.HALF_DOWN));

					iteracion++;
				}

			} else {
				varCriterFec = (String) obtConfi.recuperarDefinicionAuxiliar(umic.getDatosGenerales().getCcartera(),
						umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(),
						detalleBT.getBaseTec(), ConstantsModulos.CTE_VA_CRIT_FEC);

				for (int i = 0; i < longiDetalle; i++) {

					impFlujoProvi = calcularFlujoProvi(detalleBT, lstDetalleCorrien, fichaProceso, umic, iteracion,
							longiDetalle, subProcesoActual, mapVariables, varCriterFec);
				
					if (null == impFlujoProvi) {
						impFlujoProvi = BigDecimal.ZERO;
					}

					lstDetalleCorrien.get(iteracion - 1).getBloqueBySubproceso(subProcesoActual)
							.setImpProvi(impFlujoProvi.setScale(ConstantsFunciones.CTE_2, RoundingMode.HALF_DOWN));

					iteracion++;
				}

			}
			/**
			 * 9.7.3 almacenarDatos.proyeccion Una vez calculado el importe provi para cada
			 * periodo de proyección, se procederá a almacenar los datos de la corriente
			 * calculados invocando al correspondiente servicio al efecto,
			 * almacenarDatos.proyeccion, con la proyección calculada proyUmic.
			 */
			almacenarDatos.almacenarProyeccion(lstDetalleCorrien);
		}
	}

	/**
	 * @param btcUmic         Contiene el detalle de la base técnica de cálculo para
	 *                        la umic.
	 * @param lstDetalles     Estructura detalleCorrientes al completo
	 * @param fichaProceso    Contiene los datos del proceso necesarios para su
	 *                        ejecucion
	 * @param umic            Datos de la umic procesada
	 * @param iteracion       Indica la iteracion en la que nos encontramos
	 * @param numeroPeridos   Indica el numero de peridos existentes
	 * @param codSubProActual codigo del subproceso actual
	 * @param mapVariables    mapVariables mapa con las variables de memoria
	 *                        necesarias
	 * @return
	 */
	private static BigDecimal calcularFlujoProvi(final DetalleBaseTecnica btcUmic,
			final List<DetalleCorriente> lstDetalles, final FichaProceso fichaProceso, final Umic umic,
			final Integer iteracion, final Integer numeroPeridos, final String codSubProActual,
			final Map<String, Object> mapVariables, String varCriterFec) {
		// Variables locales
		BigDecimal importeProvi = BigDecimal.ZERO;

		String casadoXPeriodo = ConstantsFunciones.CTE_CADENA_VACIA;

		Timestamp varffincas = null;
		BigDecimal varImpAcumulado = BigDecimal.ZERO;
		Integer varJcasado = 0;
		BigDecimal varAnoJ = BigDecimal.ZERO;
		BigDecimal varI2 = BigDecimal.ZERO;
		BigDecimal varActfinJ = BigDecimal.ZERO;
		boolean calcularCadaProyeccion = true;
		boolean noEncontrado = true;
		int tramo = 0;
		// Fin variables locales

		final BloqueCorriente bloqueProyeccion = lstDetalles.get(iteracion - 1).getBloqueBySubproceso(codSubProActual);

		if (ConstantsModulos.CTE_PROY_COMI.equals(codSubProActual)) {
			calcularCadaProyeccion = UtilModulos.comprobarProyeccionConFechas(lstDetalles, codSubProActual);
		}

		if (!calcularCadaProyeccion) {
			importeProvi = BigDecimal.ZERO;
		} else {
			/**
			 * Recupero las variables necesarias para el cálculo: Variables de Apoyo -
			 * VarCriterFec --> obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
			 * Variables Cálculo - varffincas = btcUmic.fecFinTramo1 - varI2=
			 * btcUmic.itcalc2 - varImpAcumulado = 0 --> Inicilizo esta variable a cero en
			 * el primer periodo y se actualzairá en el resto de periodos como se indique.
			 */
			// Fecha fec = UtilFechas.getFecha(bloqueProyeccion.getFechaPago());
			final String casadoX[] = { btcUmic.getSwcasado().get(0), btcUmic.getSwcasado().get(1),
					btcUmic.getSwcasado().get(2), btcUmic.getSwcasado().get(3), btcUmic.getSwcasado().get(4) };

			for (int i = 0; i < casadoX.length && noEncontrado; i++) {
				if (casadoX[i].equals(ConstantsModulos.CTE_N)) {
					varffincas = btcUmic.getFecfintramo().get(i);
					varI2 = btcUmic.getItcalc().get(i);
					noEncontrado = false;
				} else {
					// El último tramo es casado
					if (i == casadoX.length - 1) {
						varffincas = btcUmic.getFecfintramo().get(i - 1);
						varI2 = btcUmic.getItcalc().get(i);
						noEncontrado = false;
					}
				}
			}

			if (btcUmic.getFecInitramo().get(2) == null) {
				varffincas = btcUmic.getFecfintramo().get(0);
				varI2 = btcUmic.getItcalc().get(1);
				varImpAcumulado = BigDecimal.ZERO;
			} else {
				if (btcUmic.getSwcasado().get(2).equals(ConstantsModulos.CTE_N)) {
					varffincas = btcUmic.getFecfintramo().get(1);
					varI2 = btcUmic.getItcalc().get(2);
				} else if (btcUmic.getSwcasado().get(2).equals(ConstantsModulos.CTE_S)) {
					varffincas = btcUmic.getFecfintramo().get(2);
					varI2 = btcUmic.getItcalc().get(3);
				}
				varImpAcumulado = BigDecimal.ZERO;
			}

			/**
			 * Para el periodo tratado, se buscará entre los tramos de interés de la umic
			 * aquel que cumpla con las siguientes condiciones: btcUmic.fecIniTramoX <=
			 * proyUmic(j).bloqueCorriente.fechaPago <= btcUmic.fecfinTramoX
			 * 
			 * Para el tramoX así hallado se evaluará el indicador de casamiento del mismo:
			 * 
			 * - Si btcUmic.swcasadoX = ‘S’: proyUmic(j).bloqueCorriente.impFlujoProvi =
			 * proyUmic(j).bloqueCorriente. impFlujoNoAnulado varJcasado = j
			 * 
			 * (Este j se irá sobrescribiendo con los distintos j de tramos casados, hasta
			 * el último de los periodos que pertenezca a un tramo casado)
			 */

			Timestamp fechaCasado = bloqueProyeccion.getFechaPago();
			if (fechaCasado == null || (fechaCasado.after(varffincas) && varI2.equals(BigDecimal.ZERO))) {
				fechaCasado = lstDetalles.get(iteracion - 1).getFechaDesde();
			}
			// if(casadoXPeriodo == "N" && (lstDetalles.get(0).getSw_periodo_casado() ==
			// null)) {
			tramo = obtenerUltimoTramoCasado(btcUmic);
			switch (tramo) {
			case 1:
				if ((!lstDetalles.get(iteracion - 1).getFechaDesde().after(btcUmic.getFecfintramo().get(0)))
						&& (btcUmic.getFecfintramo().get(0).before(lstDetalles.get(iteracion - 1).getFechaHasta()))) {
					lstDetalles.get(0).setSw_periodo_casado(iteracion);
					mapVariables.put(CLAVE_VAR_MARCA_SW, lstDetalles.get(0).getSw_periodo_casado());
				}
				break;
			case 2:
				if ((!lstDetalles.get(iteracion - 1).getFechaDesde().after(btcUmic.getFecfintramo().get(1)))
						&& (btcUmic.getFecfintramo().get(1).before(lstDetalles.get(iteracion - 1).getFechaHasta()))) {
					lstDetalles.get(0).setSw_periodo_casado(iteracion);
					mapVariables.put(CLAVE_VAR_MARCA_SW, lstDetalles.get(0).getSw_periodo_casado());

				}
				break;
			case 3:
				if ((!lstDetalles.get(iteracion - 1).getFechaDesde().after(btcUmic.getFecfintramo().get(2)))
						&& (btcUmic.getFecfintramo().get(2).before(lstDetalles.get(iteracion - 1).getFechaHasta()))) {
					lstDetalles.get(0).setSw_periodo_casado(iteracion);
					mapVariables.put(CLAVE_VAR_MARCA_SW, lstDetalles.get(0).getSw_periodo_casado());

				}
				break;
			case 4:
				if ((!lstDetalles.get(iteracion - 1).getFechaDesde().after(btcUmic.getFecfintramo().get(3)))
						&& (btcUmic.getFecfintramo().get(3).before(lstDetalles.get(iteracion - 1).getFechaHasta()))) {
					lstDetalles.get(0).setSw_periodo_casado(iteracion);
					mapVariables.put(CLAVE_VAR_MARCA_SW, lstDetalles.get(0).getSw_periodo_casado());

				}
				break;
			case 5:
				if ((!lstDetalles.get(iteracion - 1).getFechaDesde().after(btcUmic.getFecfintramo().get(4)))
						&& (btcUmic.getFecfintramo().get(4).before(lstDetalles.get(iteracion - 1).getFechaHasta()))) {
					lstDetalles.get(0).setSw_periodo_casado(iteracion);
					mapVariables.put(CLAVE_VAR_MARCA_SW, lstDetalles.get(0).getSw_periodo_casado());

				}
				break;
			}

			// }

			if (lstDetalles.get(0).getSw_periodo_casado() == null) {
				casadoXPeriodo = obtenerFechasIniFinTramoParaCalculoProvi(btcUmic, fechaCasado);
			} else {
				if (iteracion >= lstDetalles.get(0).getSw_periodo_casado()) {
					casadoXPeriodo = "N";
				} else {
					casadoXPeriodo = "S";
				}
			}

			if (bloqueProyeccion.getImpFlujoNoAnulado() == null) {
				importeProvi = BigDecimal.ZERO;
			} else {
				importeProvi = bloqueProyeccion.getImpFlujoNoAnulado();
			}

			if (ConstantsModulos.CTE_S.equals(casadoXPeriodo)) {
				varJcasado = iteracion;
				varJcasado = UtilModulos.getVarJcasado(mapVariables, ConstantsModulos.FLUJO_PROVI, varJcasado);
				if (bloqueProyeccion.getFechaPago() == null) {
					return importeProvi;
				}

			} else if (ConstantsModulos.CTE_N.equals(casadoXPeriodo)) {

				if (bloqueProyeccion.getFechaPago() != null) {
					/**
					 * - Si btcUmic.swcasadoIX = ‘N’: varAnoJ = nannos(varffincas
					 * ,proyUmic(j).bloqueCorriente.fechaPago, VarCriterFec);
					 * 
					 * actFinJ = (1 + (varI2/100) )^ -varAnoj
					 * 
					 * varImpAcumulado= varImpAcumulado + proyUmic(j).bloqueCorriente.
					 * impFlujoNoAnulado* varAtcfinJ;
					 * 
					 * (Esta variable irá acumulando el importe provi los distintos j de tramos no
					 * casados, hasta el último de los periodos).
					 * proyUmic(j).bloqueCorriente.impFlujoProvi = 0
					 */
					varAnoJ = FuncionesAuxiliares.nAnnos(varffincas, bloqueProyeccion.getFechaPago(), varCriterFec);
					varActfinJ = Util.pow(BigDecimal.ONE.add(
							varI2.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT)),
							varAnoJ.negate());
					varImpAcumulado = UtilModulos.getVarImpAcumulado(mapVariables, ConstantsModulos.FLUJO_PROVI,
							importeProvi.multiply(varActfinJ).setScale(ConstantsFunciones.CTE_2,
									RoundingMode.HALF_DOWN));
					
					if(umic.getDatosGenerales().getSwcasado().equals("S")) {
					
						importeProvi = BigDecimal.ZERO;
					}else {
						
						importeProvi = bloqueProyeccion.getImpFlujoNoAnulado();
					}
					
				
				} else {
					varImpAcumulado = UtilModulos.getVarImpAcumulado(mapVariables, ConstantsModulos.FLUJO_PROVI,
							varImpAcumulado);
				}
			}

			/**
			 * - Si estoy en el último periodo (j = proyUmic.last) se deberá sobreescribir
			 * el importe de la cuantía provi del periodo j indicado en la variable
			 * varJcasado de forma que: proyUmic(varJcasado).bloqueCorriente.impFlujoProvi =
			 * proyUmic(varJcasado). bloqueCorriente.impFlujoProvi + varImpAcumulado
			 */
			if (iteracion.equals(numeroPeridos)) {
				varJcasado = UtilModulos.getVarJcasado(mapVariables, ConstantsModulos.FLUJO_PROVI, null);

//947428-INI				
//				if (null != varJcasado){
				if ((null != varJcasado) && (varJcasado > 0)) {
//947428-FIN

					if (iteracion == varJcasado) {
						importeProvi = importeProvi.add(varImpAcumulado).setScale(ConstantsFunciones.CTE_2,
								RoundingMode.HALF_DOWN);
					} else {
						if (lstDetalles.get(0).getSw_periodo_casado() != null) {
							if (varJcasado == lstDetalles.get(0).getSw_periodo_casado()) {
								BloqueCorriente ultimoBloqueCasado = lstDetalles.get(varJcasado - 1)
										.getBloqueBySubproceso(codSubProActual);
								ultimoBloqueCasado.setImpProvi(ultimoBloqueCasado.getImpProvi().add(varImpAcumulado));
							} else {
								BloqueCorriente ultimoBloqueCasado = lstDetalles
										.get(lstDetalles.get(0).getSw_periodo_casado() - 1)
										.getBloqueBySubproceso(codSubProActual);

								if (null == ultimoBloqueCasado.getImpProvi()) {
									ultimoBloqueCasado.setImpProvi(BigDecimal.ZERO);
								}

								ultimoBloqueCasado.setImpProvi(ultimoBloqueCasado.getImpProvi().add(varImpAcumulado));

							}
						} else {

							lstDetalles.get(0).setSw_periodo_casado(varJcasado);
							BloqueCorriente ultimoBloqueCasado = lstDetalles.get(varJcasado - 1)
									.getBloqueBySubproceso(codSubProActual);
							ultimoBloqueCasado.setImpProvi(ultimoBloqueCasado.getImpProvi().add(varImpAcumulado));
						}

					}
				}
//947428-INI
				else {
					if(null == lstDetalles.get(0).getSw_periodo_casado()) {
						BloqueCorriente ultimoBloqueCasado = lstDetalles.get(0)
								.getBloqueBySubproceso(codSubProActual);
						if (null == ultimoBloqueCasado.getImpProvi()) {
							ultimoBloqueCasado.setImpProvi(BigDecimal.ZERO);
						}
						ultimoBloqueCasado.setImpProvi(ultimoBloqueCasado.getImpFlujoProbable());
					}else {
					BloqueCorriente ultimoBloqueCasado = lstDetalles.get(lstDetalles.get(0).getSw_periodo_casado()-1).
							getBloqueBySubproceso(codSubProActual);

					if (null == ultimoBloqueCasado.getImpProvi()) {
						ultimoBloqueCasado.setImpProvi(BigDecimal.ZERO);
					}
					
					ultimoBloqueCasado.setImpProvi(ultimoBloqueCasado.getImpProvi().add(varImpAcumulado));
					}

				}
//947428-FIN
			}
		}
		return importeProvi;
	}

	/**
	 * Función encargada de retornar el casado del tramo que cumpla las siguientes
	 * condiciones: 1 - La fecha inicial del tramo sea menor o igual que la fecha de
	 * calculo (fcal) 2 - La fecha de calculo (fcal) sea menor o igual que la fecha
	 * final del tramo
	 * 
	 * @param btiUmic Objeto donde se encuentran los datos relacionados con los
	 *                tramos
	 * @param fcalc   Fecha en la que se inicia el calculo
	 * @return casadoX
	 */
	private static String obtenerFechasIniFinTramoParaCalculoProvi(final DetalleBaseTecnica btcUmic,
			final Timestamp fcalc) {
		// Variables locales
		String resultado = ConstantsFunciones.CTE_CADENA_VACIA;
		// int diasFecIniFCal;
		// int diasFCalFFin;
		boolean noEncontrado = true;
		final Timestamp fecIniTramo[] = { btcUmic.getFecInitramo().get(0), btcUmic.getFecInitramo().get(1),
				btcUmic.getFecInitramo().get(2), btcUmic.getFecInitramo().get(3), btcUmic.getFecInitramo().get(4) };
		final Timestamp fecFinTramo[] = { btcUmic.getFecfintramo().get(0), btcUmic.getFecfintramo().get(1),
				btcUmic.getFecfintramo().get(2), btcUmic.getFecfintramo().get(3), btcUmic.getFecfintramo().get(4) };
		final String casadoX[] = { btcUmic.getSwcasado().get(0), btcUmic.getSwcasado().get(1),
				btcUmic.getSwcasado().get(2), btcUmic.getSwcasado().get(3), btcUmic.getSwcasado().get(4) };
		int longitudVector = 0;
		// Fin variables locales

		try {

			longitudVector = fecIniTramo.length;
			/**
			 * Desde i=0, hasta 4, vamos obteniendo la diferencia en dias entre la
			 * fechaIniTramoX y la fecha del parametro fcalc y por otro lado la diferencia
			 * entre la fecha del parametro fcalc y la fecha hasta
			 */
			for (int i = 0; i < longitudVector && noEncontrado; i++) {
				if (null != fecIniTramo[i] && null != fecFinTramo[i]) {
					// diasFecIniFCal =
					// UtilFechas.diferenciasDeFechas(fecIniTramo[i], fcalc);
					// diasFCalFFin = UtilFechas.diferenciasDeFechas(fcalc,
					// fecFinTramo[i]);

					// Si se cumple la condicion establecida para el tramoX
					// obtenemos el casadoX
					// fecIniTramo <= fcalc < fecFinTramo
					if ((!fcalc.before(fecIniTramo[i]) && fecFinTramo[i].after(fcalc))
							|| ((!fcalc.before(fecIniTramo[i]) && !fecFinTramo[i].before(fcalc))
									&& ((i < longitudVector - 1 && fecIniTramo[i + 1] == null)
											|| i == longitudVector - 1))) {
						resultado = casadoX[i];
						noEncontrado = false;
					}
				}
			}
		} catch (Exception e) {
			ModuloImporteProvi.LOG.error(e.getMessage(), e);
			resultado = ConstantsFunciones.CTE_CADENA_VACIA;
		}
		return resultado;
	}

	private static int obtenerUltimoTramoCasado(DetalleBaseTecnica btcUmic) {
		int tramo = 0;
		if (btcUmic.getSwcasado().get(0).equals("S")) {
			tramo = 1;
		}
		if (btcUmic.getSwcasado().get(1).equals("S")) {
			tramo = 2;
		}
		if (btcUmic.getSwcasado().get(2).equals("S")) {
			tramo = 3;
		}
		if (btcUmic.getSwcasado().get(3).equals("S")) {
			tramo = 4;
		}
		if (btcUmic.getSwcasado().get(4).equals("S")) {
			tramo = 5;
		}
		return tramo;
	}
}
