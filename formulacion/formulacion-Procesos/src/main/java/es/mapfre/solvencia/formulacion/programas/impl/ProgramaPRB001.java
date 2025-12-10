package es.mapfre.solvencia.formulacion.programas.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.Tab35050;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.programas.ProgramaFlujo;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.ConstantsProcesos;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.formulacion.util.UtilProcesos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * El programa PRB001– ESTABLECIMIENTO DE IMPORTE PROBABLE consiste en el
 * cálculo del importe probable de una proyección dada. Como resultado
 * obtendremos una corriente de factores de probabilización e importes probables
 * para cada periodo de proyección de la corriente.
 * 
 * @author jguijarro
 */
public class ProgramaPRB001 extends ProgramaFlujo {

	@Override
	protected String getTipoElemento() {
		return ConstantsProcesos.CTE_TIPO_ELEM_02;
	}

	private static final Logger LOG = LoggerFactory
			.getLogger(ProgramaPRB001.class);
	private static final String BC = "BC";

	@Override
	public void executeImpl(final Umic umic, final FichaProceso fichaProceso,
			final DetalleBaseTecnica detalleBT,
			final List<DetalleCorriente> detallesCorriente,
			final String subProcesoActual) throws Solvencia2Excepcion {

		ProgramaPRB001.LOG
				.trace("Inicio función << executeImpl >> de la clase ProgramaPRB001");

		try {
			// Llamamos a la función de calculo probable
			ejecutarCalculoProbable(umic, fichaProceso, detalleBT,
					detallesCorriente, subProcesoActual);
		} catch (Solvencia2Excepcion e) {
			ProgramaPRB001.LOG.error(e.getIncidencia().getTextoError(), e);
			UtilProcesos.exceptionControladaPrograma(detalleBT, umic,
					getNombrePrograma(), e);
		} catch (Exception e) {
			ProgramaPRB001.LOG.trace("Modulo inexistente");
			throw Solvencia2ExcepcionHelper.crearExcepcion(BC,
					new String[] { ConstantesSolvencia.MENSAJE_FIN_PROCESAMIENTO_UMIC });
		}

		ProgramaPRB001.LOG
				.trace("Fin función << executeImpl >> de la clase ProgramaPRB001");

	}

	/**
	 * Función encargada de realizar del calculo probable.
	 * 
	 * @param umic
	 *            Contiene los datos de la Umic que se está procesando.
	 * @param fichaProceso
	 *            Contiene los datos del proceso necesarios para su ejecución.
	 * @param detalleBT
	 *            Contiene el detalle de la base técnica de cálculo para la
	 *            umic.
	 * @param lstDetalleCorrien
	 *            Estructura detalleCorrientes de la umic
	 * @param subProcesoActual
	 *            Código el subproceso que se está ejecutando.
	 */
	private void ejecutarCalculoProbable(final Umic umic,
			final FichaProceso fichaProceso,
			final DetalleBaseTecnica detalleBT,
			final List<DetalleCorriente> lstDetalleCorrien,
			final String subProcesoActual) {
		// Variables de calculo
		Modulo modulo = getModuloCalculo();
		final IAlmacenarDatos almacenarDatos = FachadaServicios
				.getAlmacenarDatos();
		final IObtenerDatos obtenerDatos = FachadaServicios.getObtenerDatos();
		BigDecimal factor = BigDecimal.ZERO;
		int iteracion = ConstantsFunciones.CTE_1;
		List<DetalleCorriente> varProyBTI = null;
		List<DetalleCorriente> varProyROSSP = null;
		List<DetalleCorriente> varProyNIIF17 = null;
		BigDecimal impFlujoProbable = BigDecimal.ZERO;
		boolean calcularCadaProyeccion = true;
		BigDecimal ultimoOperando = BigDecimal.ZERO;
		BigDecimal varFactorCoa  = BigDecimal.ZERO;
		BigDecimal pcoAsegDiv100 = BigDecimal.ZERO;
		String btfichaproceso = fichaProceso.getCtipobt();
		boolean varSCR = false;
		List<Tab35050> varTab35050;
		BigDecimal factorGLM, factorCHCH, factorMP, factorDO = BigDecimal.ZERO;
		// Fin variables de calculo

		ProgramaPRB001.LOG
				.trace("Inicio función << ejecutarCalculoProbable >> de la clase ProgramaPRB001");

		/**
		 * En primer lugar se recuperará el módulo de cálculo del importe
		 * probable correspondiente al subproceso que se está ejecutando para la
		 * modalidad, garantía, prestación y base técnica de la umic. En segundo
		 * lugar se recuperará la proyección calculada hasta el momento para la
		 * umic y el subproceso que se está ejecutando. Para cada proyección
		 * recuperada, proyUmic, se deberá calcular el importe probable para la
		 * corriente. El cálculo de este importe probable se realizará invocando
		 * al módulo de cálculo anteriormente recuperado, si el módulo es
		 * distinto de LEIDO. Para ello, todos los módulos de cálculo que
		 * intervienen en la cuantía probable de la corriente deberán tener los
		 * mismos parámetros de entrada/ salida, de cara a independizar la
		 * invocación del módulo correspondiente y hacerlo genérico. Si el
		 * módulo de cálculo al que se invoque necesita de algún dato que no
		 * conste como parámetro de entrada al mismo, será el propio módulo de
		 * cálculo el que se encargue de recuperar dicho dato. A continuación,
		 * para cada periodo de proyección, el resultado del módulo de cálculo
		 * resultante será aplicado al importe correspondiente de la corriente y
		 * escrito en la proyección. Si el módulo de cálculo es LEIDO, se deberá
		 * recuperar el detalle de la proyección de la UMIC calculado
		 * anteriormente para la UMIC con la base técnica BTI, estableciendo
		 * para el importe probable el mismo importe probable calculado en BTI y
		 * el mismo factor de probabilización. Una vez procesados todos los
		 * puntos de la proyección, los mismos serán almacenados para su
		 * posterior recuperación y generación de la salida de la umic. Si se
		 * produce un error en algún punto del programa se registrará dicho
		 * error de la umic en el fichero de incidencias, terminando el programa
		 * para dicha umic.
		 * 
		 * 4.7.1 obtenerConfiguracion.recuperarModulo Obtiene el módulo de
		 * cálculo a ejecutar en el programa.
		 * 
		 * • varModuloCalc = obtenerConfiguracion.recuperarModulo con los
		 * parámetros: o Umic.datosGenerales.kmodalidad o
		 * Umic.datosGenerales.kgarantia o Umic.datosGenerales.kprestacion o
		 * btc.btcalc o codSubproceso o tipoElemento = 02
		 */
		// Esta configuración se recupera en la clase padre ProgramaFlujo
		/**
		 * 6.7.4 obtenerConfiguracion.recuperarProyeccion Cuando varModuloCalc
		 * es LEIDO, se deberá recuperar la proyección de la UMIC calculada
		 * anteriormente bajo la base técnica inicial (BTI). Para ello se
		 * llamará a obtenerConfiguracion.recuperarProyeccion con los siguientes
		 * parámetros: varProyBTI = obtenerConfiguracion.recuperarProyeccion ()
		 * con: - feccierre = btcUmic.fecCierre - ktipobt =BTI - claveUmic: o
		 * cnegocio = umic.cnegocio o ccanal = umic.ccanal o ccartera =
		 * umic.ccanal o kmodalidad = umic.kmodalidad o kpoliza = umic.kpoliza o
		 * ksubpoliza = umic.ksubpoliza o kcertificado = umic.kcertificado o
		 * nsuscri = umic.nsuscri o norden = umic.norden o kgarantia =
		 * umic.kgarantia o kprestacion = umic.kgarantia o kajuste =
		 * umic.kgarantia o ctipoaport = umic.kgarantia
		 */
		
		Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		String kramo = umic.getDatosGenerales().getKramo();
		
		varTab35050 = UtilModulos.getTab35050(kramo, kmodalidad);
		
		
		if((fichaProceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTI4NB) || fichaProceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTI4C) || fichaProceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTI8) || fichaProceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTI8NB)) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_BTI_PROY) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_BTI) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_ROSSP) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_ROSSPCSM) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_ROSSPTI) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_ROSSPTE) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_ROSSPGA) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17)){
			varProyNIIF17  = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BT_NIIF17, detalleBT.getFecCierre(), umic.getKey());
			copiarProbableProyBTI(subProcesoActual, lstDetalleCorrien, varProyNIIF17);
		}else if (ConstantsProcesos.CTE_LEIDO_BTI.equals(modulo.getNombreServicio())) {
			varProyBTI = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BTI, detalleBT.getFecCierre(),umic.getKey());
			copiarProbableProyBTI(subProcesoActual, lstDetalleCorrien, varProyBTI);
		}else if(ConstantsProcesos.CTE_LEIDO_BTIPR.equals(getModuloCalculo().getNombreServicio())){
			varProyBTI = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_VAL_BTI_PROY, detalleBT.getFecCierre(),umic.getKey());
			copiarProbableProyBTI(subProcesoActual, lstDetalleCorrien, varProyBTI);
		
		}else if(ConstantsProcesos.CTE_LEIDO_ROSSP.equals(getModuloCalculo().getNombreServicio())){
			varProyROSSP  = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BT_ROSSP, detalleBT.getFecCierre(), umic.getKey());
			copiarProbableProyBTI(subProcesoActual, lstDetalleCorrien, varProyROSSP);
		}else if(ConstantsProcesos.CTE_LEIDO_ROSSPCSM.equals(getModuloCalculo().getNombreServicio())){
			varProyROSSP  = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BT_ROSSPCSM, detalleBT.getFecCierre(), umic.getKey());
			copiarProbableProyBTI(subProcesoActual, lstDetalleCorrien, varProyROSSP);
		} else if (detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRVM)) {
			//Para el estrés de volatilidad de mortalidad recuperaremos los valores de BEL
			varProyBTI = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BT_BEL, detalleBT.getFecCierre(), umic.getKey());
			copiarProbableProyBTI(subProcesoActual,  lstDetalleCorrien, varProyBTI);
		} else if (detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17AEN)) {
			varProyNIIF17 = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BT_NIIF17, detalleBT.getFecCierre(), umic.getKey());
			copiarProbableProyBTI(subProcesoActual,  lstDetalleCorrien, varProyNIIF17);
		} 

		/**
		 * Cuando varModuloCalc es LEIDO, se deberá recuperar la proyección de
		 * la UMIC calculada anteriormente bajo la base técnica inicial (BTI).
		 * Para ello se llamará a obtenerConfiguracion.recuperarProyeccion con
		 * los siguientes parámetros:
		 * 
		 * Para invocar a dicho módulo varModuloCalc se deberán enviar los
		 * parámetros de entrada: • ProyUmic • varPeriodoProyeccion = J -->
		 * índice del periodo de proyección actual que se está procesando en el
		 * bucle que recorre las proyecciones de la umic (PROY_UMIC) •
		 * fichaProceso.fcalc • umic • btcUmic • codSubproceso
		 * 
		 * De forma que: varFactor= varModuloCalc(ProyUmic ,
		 * varPeriodoProyeccion , fichaProceso.fcalc, umic, btcUmic,
		 * codSubproceso)
		 * 
		 * 
		 * De forma que: varFactor= varModuloCalc(ProyUmic ,
		 * varPeriodoProyeccion , fichaProceso, umic, btcUmic)
		 */

		/**
		 * Si codSubproceso = ‘PROY_COMI’ se comproborá previamente si existen fechas de calculo generadas ó  no, para ver si debe invocarse al módulo de cálculo ó no: 
					•	Si no existe ninguna  proyección con fecha de cálculo y /ó fecha de devengo <>  null, NO se deberá  invocar al módulo correspondiente como en el resto de casos, sino que se hará: 
							proyUmic (j).bloqueCorriente.fpbProbable = 0
							proyUmic (j).bloqueCorriente.impFlujoProbable = 0
				para todos los periodos de la umic, finalizando así el programa para la umic, y se almacenarán los datos invocando a  almacenarDatos.proyeccion
					•	Si  existe algúna proyección con fecha de cálculo y /ó fecha de devengo <>  null, se deberá  invocar al módulo correspondiente como en el resto de casos, tal y como se muestra a continuación.
		 */
		else{

			if (detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTIU) ||
					detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTID) ||
					detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEN) ||
					detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEP) ||
					detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIN) ||
					detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIP) ||
					detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM) ||
					detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRGTO)) {
				varProyBTI = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BT_BEL, detalleBT.getFecCierre(), umic.getKey());
				varSCR = true;
			}
			
			if (ConstantsModulos.CTE_PROY_COMI.equals(subProcesoActual)) {
				calcularCadaProyeccion = UtilModulos.comprobarProyeccionConFechas(lstDetalleCorrien, subProcesoActual);
			}

			final int lonDetalCorr = lstDetalleCorrien.size();

			for (int i = 0; i < lonDetalCorr; i++) {
				final BloqueCorriente bloqueProyeccion = lstDetalleCorrien.get(i)
						.getBloqueBySubproceso(subProcesoActual);

				//Caso para invocación a modulo
				if (calcularCadaProyeccion && bloqueProyeccion.getImpFlujoNominal() != null && bloqueProyeccion.getImpFlujoNominal().signum() != 0) {
					//			if (calcularCadaProyeccion) { 
					if(ConstantsProcesos.CTE_ERROR_BT_PRB.equals(modulo.getNombreServicio())){
						varProyBTI = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BTI, detalleBT.getFecCierre(),umic.getKey());
						if(null == varProyBTI || varProyBTI.size() == 0){
							varProyBTI  = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BTI_PROY, detalleBT.getFecCierre(), umic.getKey());
						}
						factor = varProyBTI.get(i).getBloqueBySubproceso(subProcesoActual).getFpbProbable();		
					}else if (varSCR) {
						// Recuperamos el factor de BEL
						factor = varProyBTI.get(iteracion - 1).getBloqueBySubproceso(subProcesoActual).getFpbProbable();
					} else {
						factor = (BigDecimal) modulo.execute(
								lstDetalleCorrien,
								lstDetalleCorrien.get(iteracion - 1).getBloqueBySubproceso(
										subProcesoActual), iteracion,
										fichaProceso.getFcalc(), umic, detalleBT,
										this.getMapVariables(), subProcesoActual);
					}
					/**
					 * •	Si codSubproceso <>  ‘PROY_GTOS’:
							-	Si umic. coaseguro.PCOASEG  =  0 ó umic. coaseguro.PCOASEG  =   100 ó umic.coaseguro. CTIPOCOASEG = null : 
									proyUmic (j).bloqueCorriente.fpbProbable = varFactor
									proyUmic (j).bloqueCorriente.impFlujoProbable = proyUmic(j).bloqueCorriente .impFlujoNominal  * varFactor

							-	En cualquier otro caso: 
									proyUmic (j).bloqueCorriente.fpbProbable = varFactor
									proyUmic (j).bloqueCorriente.impFlujoProbable = proyUmic(j).bloqueCorriente .impFlujoNominal  * varFactor * (umic.datosgenerales.PCOASEG /100)

					•	Si codSubproceso =  ‘PROY_GTOS’:
							-	Si umic. coaseguro.PCOASEG  =  0 ó umic. coaseguro.PCOASEG  =   100 ó umic.coaseguro. CTIPOCOASEG = null  ó : fichaProceso.BaseTécnica = BEL
									proyUmic (j).bloqueCorriente.fpbProbable = varFactor
									proyUmic (j).bloqueCorriente.impFlujoProbable = proyUmic(j).bloqueCorriente .impFlujoNominal  * varFactor

							-	En cualquier otro caso: 
									•	Si umic.coaseguro. CTIPOCOASEG = ‘C’
											o	varFactorCoa = 1
									•	Si umic.coaseguro. CTIPOCOASEG = ‘A’
											o	varFactorCoa = 0
									varFactorFDGI = umic.coaseguro.PCOASEG  + umic.coaseguro.DISTINT *( varFactorCoa - (umic.datosgenerales.PCOASEG /100))

									proyUmic (j).bloqueCorriente.fpbProbable = varFactor
									proyUmic (j).bloqueCorriente.impFlujoProbable = proyUmic(j).bloqueCorriente .impFlujoNominal  * varFactor * varFactorFDGI
					 */
					
					
					if (umic.getDatosCoaseguro().getPcoaseg().equals(BigDecimal.ZERO) || umic.getDatosCoaseguro().getPcoaseg().equals(ConstantsFunciones.CTE_OPER_100) ||
							null == umic.getDatosCoaseguro().getCtipocoaseg() || btfichaproceso.equals(ConstantsModulos.CTE_VAL_BELCOA) || 
							btfichaproceso.equals(ConstantsModulos.CTE_VAL_BTCOA) || btfichaproceso.equals(ConstantsModulos.CTE_VAL_BTCOATF) || btfichaproceso.equals(ConstantsModulos.CTE_VAL_MULTICOA) || umic.getDatosGenerales().getSpcom().equals("B")) {
						
						if(null == factor){
							factor = BigDecimal.ZERO;
						}
						
						impFlujoProbable = bloqueProyeccion.getImpFlujoNominal().multiply(
								factor);
						
						if (!varTab35050.isEmpty() && !ConstantsModulos.CTE_PROY_PRIMA.equals(subProcesoActual)) {
							
							if (varTab35050.get(0).getKmodalidad().equals(umic.getDatosGenerales().getKmodalidad())) {
								
								impFlujoProbable = bloqueProyeccion.getImpFlujoNominal().multiply(factor);
								
								if (umic.getDatosDescuentos().getGlm() != null) {
									if(!umic.getDatosDescuentos().getGlm().equals(BigDecimal.ZERO)
											&& !umic.getDatosDescuentos().getGlm().equals(BigDecimal.ONE)) {
										factorGLM = umic.getDatosDescuentos().getGlm();
										impFlujoProbable = impFlujoProbable.multiply(factorGLM);
									}
								} 
								
								if (umic.getDatosDescuentos().getChch() != null) {
									
									if(!umic.getDatosDescuentos().getChch().equals(BigDecimal.ZERO) 
											&& !umic.getDatosDescuentos().getChch().equals(BigDecimal.ONE)) {
										factorCHCH = umic.getDatosDescuentos().getChch();
										impFlujoProbable = impFlujoProbable.multiply(factorCHCH);
									}
								} 
								
								if (umic.getDatosDescuentos().getMp() != null) {
									if(!umic.getDatosDescuentos().getMp().equals(BigDecimal.ZERO)
											&& !umic.getDatosDescuentos().getMp().equals(BigDecimal.ONE)) {
										factorMP = umic.getDatosDescuentos().getMp();
										impFlujoProbable = impFlujoProbable.multiply(factorMP); 
									}
								} 
								
								if (umic.getPrimas().getPdtoaseg() != null
										&& !umic.getPrimas().getPdtoaseg().equals(BigDecimal.ZERO)) {
									factorDO = BigDecimal.ONE.subtract(umic.getPrimas().getPdtoaseg()
											.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
									impFlujoProbable = impFlujoProbable.multiply(factorDO);
								} 
								
								if (umic.getPrimas().getPdtoemp() != null
										&& !umic.getPrimas().getPdtoemp().equals(BigDecimal.ZERO)) {
									factorDO = BigDecimal.ONE.subtract(umic.getPrimas().getPdtoemp().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
									impFlujoProbable =  impFlujoProbable.multiply(factorDO);
								} 
							}
						}
						
					} else {
						pcoAsegDiv100 =  umic.getDatosCoaseguro().getPcoaseg().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
						if (!ConstantsModulos.CTE_PROY_GTOS.equals(subProcesoActual)) {
							ultimoOperando = pcoAsegDiv100;
						} else {
							if (detalleBT.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL) ||
									detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR) ||
									detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTIU) ||
									detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTID) ||
									detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRGTO) ||
									detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMFE) ||
									detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMMI) ||
									detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCF) ||
									detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCI) ||
									detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLFE) ||
									detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLMI) ||
									detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC) ||
									detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRVM)){
								ultimoOperando = BigDecimal.ONE;
							}else{
								if (umic.getDatosCoaseguro().getCtipocoaseg().equals("C")) {
									varFactorCoa = BigDecimal.ONE;
								} else {
									varFactorCoa = BigDecimal.ZERO;
								}

								final BigDecimal porcentajeCoaseguro = umic.getDatosCoaseguro().getPcoaseg().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01); 
								ultimoOperando = porcentajeCoaseguro.add(umic.getDatosCoaseguro().getDistint().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01).
										multiply(varFactorCoa.subtract(porcentajeCoaseguro)));
							}
						}
						
						if(null == factor){
							factor = BigDecimal.ZERO;
						}
						
						if (!varTab35050.isEmpty() 
								&& varTab35050.get(0).getKmodalidad().equals(umic.getDatosGenerales().getKmodalidad())
								&& varTab35050.get(0).getKramo().equals(umic.getDatosGenerales().getKramo())
								&& !ConstantsModulos.CTE_PROY_PRIMA.equals(subProcesoActual)) {
								
							impFlujoProbable = bloqueProyeccion.getImpFlujoNominal().multiply(factor);
								
							if (umic.getDatosDescuentos().getGlm() != null) {
								if(!umic.getDatosDescuentos().getGlm().equals(BigDecimal.ZERO)
										&& !umic.getDatosDescuentos().getGlm().equals(BigDecimal.ONE)) {
									factorGLM = umic.getDatosDescuentos().getGlm();
									impFlujoProbable = impFlujoProbable.subtract(impFlujoProbable.multiply(factorGLM));
								}
							}
								
							if (umic.getDatosDescuentos().getChch() != null) {
									
								if(!umic.getDatosDescuentos().getChch().equals(BigDecimal.ZERO)
										&& !umic.getDatosDescuentos().getChch().equals(BigDecimal.ONE)) {
									factorCHCH = umic.getDatosDescuentos().getChch();
									impFlujoProbable = impFlujoProbable.subtract(impFlujoProbable.multiply(factorCHCH));
								}
							} 
								
							if (umic.getDatosDescuentos().getMp() != null) {
								if(!umic.getDatosDescuentos().getMp().equals(BigDecimal.ZERO)
										&& !umic.getDatosDescuentos().getMp().equals(BigDecimal.ONE)) {
									factorMP = umic.getDatosDescuentos().getMp();
									impFlujoProbable = impFlujoProbable.subtract(impFlujoProbable.multiply(factorMP));
								}
							}
							if (umic.getPrimas().getPdtoaseg() != null
									&& !umic.getPrimas().getPdtoaseg().equals(BigDecimal.ZERO)) {
								factorDO = BigDecimal.ONE.subtract(umic.getPrimas().getPdtoaseg()
										.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
								impFlujoProbable = impFlujoProbable.subtract(impFlujoProbable.multiply(factorDO));
							}
								
							if (umic.getPrimas().getPdtoemp() != null
									&& !umic.getPrimas().getPdtoemp().equals(BigDecimal.ZERO)) {
								factorDO = BigDecimal.ONE.subtract(umic.getPrimas().getPdtoemp().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
								impFlujoProbable =  impFlujoProbable.subtract(impFlujoProbable.multiply(factorDO));
							}
						} else {
							impFlujoProbable = bloqueProyeccion.getImpFlujoNominal().multiply(factor)
									.multiply(ultimoOperando);
						}
					}

				} else {
					//Caso en el que la corriente no es calculada
					factor = BigDecimal.ZERO;
					impFlujoProbable = BigDecimal.ZERO;
				}

				/**
				 * 4.7.4 calcularProyeccion(j) Una vez ejecutado el módulo de
				 * cálculo para la proyección j, escribiremos los datos de la salida
				 * del flujo probable a calcular de dicha proyección de forma que:
				 * 
				 * proyUmic (j).fpbProbable = varFactor proyUmic
				 * (j).impFlujoProbable = proyUmic (j).impFlujoNominal * varFactor
				 */
				bloqueProyeccion.setFpbProbable(factor);
				bloqueProyeccion.setImpFlujoProbable(impFlujoProbable.setScale(ConstantsFunciones.CTE_2, RoundingMode.HALF_DOWN));

				iteracion++;
			}
		}
		// Almacenamos la proyeccion
		almacenarDatos.almacenarProyeccion(lstDetalleCorrien);

		ProgramaPRB001.LOG
				.trace("Fin función << ejecutarCalculoProbable >> de la clase ProgramaPRB001");
	}

	/**
	 * Función encargada de recuperar el importeFlujoProbable y el fpbProbable
	 * 
	 * @param subProcesoActual
	 *            su proceso actual
	 * @param varProyBTI
	 *            proyección BTI calculada para la UMIC
	 */
	private void copiarProbableProyBTI(final String subProcesoActual,
			final List<DetalleCorriente> detallesCorriente,
			final List<DetalleCorriente> varProyBTI) {
		/**
		 * Una vez recuperada la proyección BTI calculada para la UMIC
		 * (varProyBTI) se recorrerá la estructura de proyecciones de la umic
		 * (ProyUmic) desde la primera proyección hasta la última. Para cada
		 * proyección de la umic se asignarán los valores correspondientes de la
		 * corriente, dependiendo del subproceso que haya invocado al programa
		 * de forma que: • Si codSubproceso = ‘PROY_VIDA’ - varProbable(j) =
		 * varProyBTI(j).corrienteVida.impFlujoProbable - varfpbProbable(j) =
		 * varProyBTI(j).corrienteVida.fpbProbable • Si codSubproceso =
		 * ‘PROY_FALL’ - varProbable (j) =
		 * varProyBTI(j).corrienteFallecimiento.impFlujoProbable -
		 * varfpbProbable(j) = varProyBTI(j). corrienteFallecimiento.fpbProbable
		 * • Si codSubproceso = ‘PROY_COMP ’ - varProbable (j) =
		 * varProyBTI(j).corrienteComplementario.impFlujoProbable -
		 * varfpbProbable(j) = varProyBTI(j).
		 * corrienteComplementario.fpbProbable • Si codSubproceso = ‘PROY_PRIMA’
		 * - varProbable (j) = varProyBTI(j).corrientePrima. impFlujoProbable -
		 * varfpbProbable(j) = varProyBTI(j). corrientePrima.fpbProbable • Si
		 * codSubproceso = ‘PROY_GTOS’ - varProbable (j) =
		 * varProyBTI(j).corrienteGasto.impFlujoProbable - varfpbProbable(j) =
		 * varProyBTI(j). corrienteGasto.fpbProbable • Si codSubproceso =
		 * ‘PROY_RESC’ - varProbable (j) =
		 * varProyBTI(j).corrienteRescate.impFlujoProbable - varfpbProbable(j) =
		 * varProyBTI(j). corrienteRescate.fpbProbable • Si codSubproceso =
		 * ‘PROY_COMI’ - varProbable (j) =
		 * varProyBTI(j).corrienteComision.impFlujoProbable - varfpbProbable(j)
		 * = varProyBTI(j). corrienteComision.fpbProbable
		 */

		final int sizeDetalle = detallesCorriente.size();
		for (int i = 0; i < sizeDetalle; i++) {
			final DetalleCorriente nuevoDetalle = detallesCorriente.get(i);
			final BloqueCorriente nuevoBloque = nuevoDetalle
					.getBloqueBySubproceso(subProcesoActual);

			final DetalleCorriente detalleBTI = varProyBTI.get(i);
			final BloqueCorriente bloqueBTI = detalleBTI
					.getBloqueBySubproceso(subProcesoActual);

			nuevoBloque.setFpbProbable(bloqueBTI.getFpbProbable());
			nuevoBloque.setImpFlujoProbable(bloqueBTI.getImpFlujoProbable());
		}
	}

	@Override
	public String getNombrePrograma() {
		return ConstantsFactorias.PROGRAMA_PRB001;
	}

}
