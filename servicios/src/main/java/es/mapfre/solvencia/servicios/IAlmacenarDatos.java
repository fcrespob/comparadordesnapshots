/* MODIFICACION:TAR00302248-Contabilidad SolvenciaII en SSAA 
   FECHA: 25/09/2017
   AUTOR: INDRA
 */

package es.mapfre.solvencia.servicios;

import java.util.Date;
import java.util.List;
import java.util.Map;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
/*INI-TAR00302248*/
import es.mapfre.solvencia.dominio.entregables.Contabilidad;
import es.mapfre.solvencia.dominio.entregables.ContabilidadCertificado;
import es.mapfre.solvencia.dominio.entregables.ConteoCertificado;
/*FIN-TAR00302248*/
import es.mapfre.solvencia.dominio.entregables.Basetec;
import es.mapfre.solvencia.dominio.entregables.DetalleCorrienteEntregables;
import es.mapfre.solvencia.dominio.entregables.FlujCoaSeg;
import es.mapfre.solvencia.dominio.entregables.FlujInf1;
import es.mapfre.solvencia.dominio.entregables.FlujInf2;
import es.mapfre.solvencia.dominio.entregables.FlujInf3;
import es.mapfre.solvencia.dominio.entregables.FlujInf4;
import es.mapfre.solvencia.dominio.entregables.FlujInfSCR;
import es.mapfre.solvencia.dominio.entregables.FlujPMaCoa;
import es.mapfre.solvencia.dominio.entregables.FlujPMaCoaM;
import es.mapfre.solvencia.dominio.entregables.FlujPMdCoa;
import es.mapfre.solvencia.dominio.entregables.FlujPMdCoaM;
import es.mapfre.solvencia.dominio.entregables.FlujSuscri;
import es.mapfre.solvencia.dominio.entregables.FlujTcas;
import es.mapfre.solvencia.dominio.entregables.FlujoTotPV;
import es.mapfre.solvencia.dominio.entregables.FlujosTN17;
import es.mapfre.solvencia.dominio.entregables.PesosBt;
import es.mapfre.solvencia.dominio.entregables.PesosBtProxy;
import es.mapfre.solvencia.dominio.entregables.ProvCoaSeg;
//import es.mapfre.solvencia.dominio.entregables.Basetec;
import es.mapfre.solvencia.dominio.entregables.PrvBt;
import es.mapfre.solvencia.dominio.entregables.PrvCr;
import es.mapfre.solvencia.dominio.entregables.PrvFpb;
import es.mapfre.solvencia.dominio.entregables.PrvInf1;
import es.mapfre.solvencia.dominio.entregables.PrvInf2;
import es.mapfre.solvencia.dominio.entregables.PrvUmic;
import es.mapfre.solvencia.dominio.entregables.SwCobroCom;
import es.mapfre.solvencia.dominio.entregables.SwCobroComCsv;
import es.mapfre.solvencia.dominio.entregables.TotPMaCoa;
import es.mapfre.solvencia.dominio.entregables.TotPMaCoaM;
import es.mapfre.solvencia.dominio.formulacion.Periodo;
import es.mapfre.solvencia.dominio.formulacion.PlanPagos;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.dominio.salidaCalculo.TerminosPMCUmic;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.dominio.scr.entregables.FactoresVolatilidad;
import es.mapfre.solvencia.dominio.entregables.FlujosTotP;
import es.mapfre.solvencia.dominio.entregables.FPSL;
import es.mapfre.solvencia.dominio.entregables.PatronCsm;


public interface IAlmacenarDatos {
	/**
	 * Almacenará los pagos de rentas calculados en base a la definción de la
	 * umic
	 * 
	 * @param key
	 * @param listaPlanPagos
	 */
	void almacenarPlanPagos(UmicKey key, List<PlanPagos> listaPlanPagos);

	/**
	 * Almacenará una lista de listas de pagos de rentas calculados en base a la
	 * definción de la umic
	 * 
	 * @param listasPlanPagos
	 */
	void almacenarPlanPagos(Map<UmicKey, List<PlanPagos>> listasPlanPagos);

	/**
	 * Almacenará los periodos calculados en el procesado de la umic
	 * 
	 * @param periodo
	 */
	void almacenarPeriodos(Periodo periodo);

	/**
	 * Almacenará una lista de periodos calculados en el procesado de la umic
	 * 
	 * @param periodos
	 */
	void almacenarPeriodos(List<Periodo> periodos);

	/**
	 * Almacenará la proyección calculadas en el procesado de la umic
	 * 
	 * @param detalleCorriente
	 */
	void almacenarProyeccion(DetalleCorriente detalleCorriente);

	/**
	 * Almacenará una lista de proyecciones calculadas en el procesado de la
	 * umic
	 */
	void almacenarProyeccion(List<DetalleCorriente> detallesCorriente);

	/**
	 * Almacenará los totales por flujos en el procesado de la umic
	 * 
	 * @param totalesFlujos
	 */
	void almacenarTotalFlujos(TotalesFlujos totalesFlujos);

	/**
	 * Almacenará una lista de totales por flujos en el procesado de la umic
	 * 
	 * @param totalesFlujos
	 */
	void almacenarTotalFlujos(List<TotalesFlujos> totalesFlujos);

	/**
	 * Almacenará las incidencias
	 * 
	 * @param incidencia
	 */
	void almacenarIncidencias(Incidencia incidencia);

	/**
	 * Almacenará el Detalle Base Técnica
	 * 
	 * @param detalleBaseTecnica
	 */
	void almacenarDetalleBaseTecnica(DetalleBaseTecnica detalleBaseTecnica);

	/**
	 * Añadirá una nueva línea al log de fichaResultado para una ficha de
	 * proceso, indicando la hora de fin
	 * 
	 * @param fichasProceso
	 */
	void agregarLineaFinFichasResultado(Map<Date, FichaProceso> fichasProceso);

	/**
	 * Calcula el número de umics tratadas por un motor para una ficha de
	 * proceso
	 * 
	 * @param fichaProceso
	 */
	void agregarRegistroFichaResultado(FichaProceso fichasProceso, String tipoRegistro);

	/**
	 * Almacenará en los Datos Calculados Umic
	 * 
	 * @param terminosPMCUmic
	 */
	void almacenarTerminosPMCUmic(TerminosPMCUmic terminosPMCUmic);

	/**
	 * Almacenará el entregable PrvInf1
	 * 
	 * @param PrvInf1
	 */
	void almacenarEntregablePrvInf1(List<PrvInf1> prvInf1s);

	/**
	 * Almacenará el entregable PrvInf2
	 * 
	 * @param prvInf2s
	 */
	void almacenarEntregablePrvInf2(List<PrvInf2> prvInf2s);

	/**
	 * Almacenará el entregable PrvUmic
	 * 
	 * @param prvUmics
	 */
	void almacenarEntregablePrvUmic(List<PrvUmic> prvUmics);

	/**
	 * Almacenará el entregavle PrvCr
	 * 
	 * @param prvCrs
	 */
	void almacenarEntregablePrvCr(List<PrvCr> prvCrs);

	/**
	 * Almacenar el entregable Prvbt
	 * 
	 * @param prvbts
	 */
	void almacenarEntregablePrvBt(List<PrvBt> prvbts);

	/**
	 * Almacenar el entregable FlujInf1
	 * 
	 * @param flujinf1s
	 */
	void almacenarEntregableFlujInf1(List<FlujInf1> flujinf1s);

	/**
	 * Almacenar el entregable FLujInf2
	 * 
	 * @param flujinf2s
	 */
	void almacenarEntregableFlujInf2(List<FlujInf2> flujinf2s);

	/**
	 * Almacenar el entregable ProvCoaSeg
	 * 
	 * @param provcoaseguros
	 */
	void almacenarEntregableProvCoaSeg(List<ProvCoaSeg> provcoaseguros);

	/**
	 * Almacenar el entregable FlujCoaSeg
	 * 
	 * @param flujcoaseguros
	 */
	void almacenarEntregableFlujCoaSeg(List<FlujCoaSeg> flujcoaseguros);

	/**
	 * Almacenará el entregable Basetec
	 * 
	 * @param basetecs
	 */
	void almacenarEntregableBasetec(List<Basetec> basetecs);

	/**
	 * Almacena en DetalleCorrienteEntregables los valores necesarios para
	 * realizar las agregaciones de los entregables correspondientes a flujos
	 * 
	 * @param detalleCorriente
	 */
	void almacenarDetalleCorrienteEntregables(Umic umic, DetalleBaseTecnica detalleBaseTecnica,
			List<DetalleCorriente> detallesCorriente, FichaProceso fichaproceso, Boolean scr);

	/**
	 * Almacenará el entregable PrvFpb
	 * 
	 * @param prvfpbs
	 */
	void almacenarEntregablePrvFpb(List<PrvFpb> prvfpbs);
	
	/**
	 * Almacenará el entregable FlujTcas
	 * 
	 * @param flujtcass
	 */
	void almacenarEntregableFlujTcas(List<FlujTcas> flujtcass);
	
	/** INI-TAR0030224 
	 * Almacenará el entregable Contabilidad
	 * 
	 * @param contabilidad
	 */
	void almacenarEntregableContabilidad(List<Contabilidad> contabilidad);
	/** FIN -TAR0030224 */

	void almacenarEntregableSCRVM(List<FactoresVolatilidad> factoresVolatilidad);
	
	void almacenarValoresSCRVM(Umic umic, DetalleBaseTecnica detalleBaseTecnica, List<DetalleCorriente> detallesCorriente, FichaProceso fichaProceso);
	
	void eliminarTotalesFlujos (TotalesFlujos totalesFlujos);
	
	void eliminarDetalleBaseTecnica (DetalleBaseTecnica detallebt);
	
	void eliminarDetalleCorrienteEntregables(DetalleCorrienteEntregables detalleEntregableBt);
	
	void eliminarProyeccion (DetalleCorriente dC);
	
	void almacenarEntregableFlujosTotP(List<FlujosTotP> flujostotps);

	void almacenarValoresFLUJOSTOTP(Umic umic, DetalleBaseTecnica detalleBaseTecnica, List<DetalleCorriente> detallesCorriente, FichaProceso fichaproceso, TotalesFlujos totales);

	String getParametros(String gparametros, Integer orden);
	
	/**
	 * Almacenará el entregable Flujinf3
	 * 
	 * @param flujinf3
	 */
	void almacenarEntregableFlujInf3(List<FlujInf3> flujinf3s);
	
	/**
	 * Almacenará el entregable Flujinf4
	 * 
	 * @param flujinf4
	 */
	void almacenarEntregableFlujInf4(List<FlujInf4> flujinf4s);
	
	/**
	 * Almacenará el entregable FlujosTotNiif17
	 * 
	 * @param flujosTotNiif17
	 */
	void almacenarEntregableFlujosTN17(List<FlujosTN17> flujosTotNiif17);
	
	/**
	 * Almacenará el entregable FlujoTotPV
	 * 
	 * @param flujototpv
	 */
	void almacenarEntregableFlujoTotPV(List<FlujoTotPV> flujinf4s);
	
	void almacenarValoresFLUJOSTN17(Umic umic, DetalleBaseTecnica detalleBaseTecnica, List<DetalleCorriente> detallesCorriente, FichaProceso fichaproceso, TotalesFlujos totalesN17);
    
	/**
	 * Almacenará el entregable FPSL
	 * 
	 * @param FPSL
	 */
	void almacenarEntregableFPSL(List<FPSL> fpsl);
	
	//void almacenarValoresFPSL(Umic umic, DetalleBaseTecnica detalleBaseTecnica, List<DetalleCorriente> detallesCorriente, FichaProceso fichaproceso, TotalesFlujos totales);
	
	void almacenarEntregablePesosBt(List<PesosBt> pesosBt);
	
	void almacenarEntregablePesosBtProxy(List<PesosBtProxy> pesosBtProxy);

	/**
	 * Almacenará el entregable PatronCSM
	 * 
	 * @param patroncsm
	 */
	void almacenarEntregablePatronCsm(List<PatronCsm> patroncsm);

	void almacenarEntregableSWCOBROCOM(List<SwCobroCom> swcobrocom);
	
	void almacenarEntregableSwCobroComCsv(List<SwCobroComCsv> swCobroComCsv);
	
	void almacenarEntregablesCoaseguro(Umic umic, DetalleBaseTecnica detalleBaseTecnica,
			List<DetalleCorriente> detallesCorriente, FichaProceso fichaproceso, TotalesFlujos totales); 
	
	void almacenarEntregableTotPMaCoa(List<TotPMaCoa> totPMaCoa);
	
	void almacenarEntregableFlujPMaCoa(List<FlujPMaCoa> flujPMaCoa);
	
	void almacenarEntregableFlujPMdCoa(List<FlujPMdCoa> flujPMdCoa);
	
	void almacenarEntregableTotPMaCoaM(List<TotPMaCoaM> totPMaCoaM);
	
	void almacenarEntregableFlujPMaCoaM(List<FlujPMaCoaM> flujPMaCoaM);
	
	void almacenarEntregableFlujPMdCoaM(List<FlujPMdCoaM> flujPMdCoaM);
	
	void almacenarEntregablesTirea(Umic umic, DetalleBaseTecnica detalleBaseTecnica,
			List<DetalleCorriente> detallesCorriente, FichaProceso fichaproceso, TotalesFlujos totales);
	
	void almacenarIncidenciasMaestro(Umic umic, String error);
	
	void almacenarEntregableCONTEOCERTIFICADO(List<ConteoCertificado> conteo);
	
	void almacenarEntregableFlujSuscripciones(Umic umic, DetalleBaseTecnica detalleBaseTecnica,
			List<DetalleCorriente> detallesCorriente, FichaProceso fichaproceso, TotalesFlujos totales); 
	/**
	 * Almacenar el entregable FlujSuscri
	 * 
	 * @param flujsuscripcion
	 */
	void almacenarEntregableFlujSuscri(List<FlujSuscri> flujsuscripciones);	
	
	/**
	 * Almacenará el entregable FlujinfSCR
	 * 
	 * @param flujinfSCR
	 */
	void almacenarEntregableFlujInfSCR(List<FlujInfSCR> flujinfscrs);
	
	void almacenarEntregablesFlujInfSCR(Umic umic, DetalleBaseTecnica detalleBaseTecnica,
		List<DetalleCorriente> detallesCorriente, List<DetalleCorriente> detallesCorrienteBEL);
	
	public void almacenarEntregableContabilidadCertificado(List<ContabilidadCertificado> contabilidad);

}