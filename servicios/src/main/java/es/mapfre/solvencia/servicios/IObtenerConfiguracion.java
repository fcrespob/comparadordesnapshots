package es.mapfre.solvencia.servicios;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.conversionesBel.GastosReales;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresAnulacion;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresAnulacionMensuales;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresCurvaTipo;
import es.mapfre.solvencia.dominio.entregables.FlujCoaSeg;
import es.mapfre.solvencia.dominio.entregables.FlujInfSCR;
import es.mapfre.solvencia.dominio.entregables.FlujPMaCoa;
import es.mapfre.solvencia.dominio.entregables.FlujPMdCoa;
import es.mapfre.solvencia.dominio.entregables.FlujSuscri;
import es.mapfre.solvencia.dominio.entregables.FlujTcas;
import es.mapfre.solvencia.dominio.entregables.ProvCoaSeg;
import es.mapfre.solvencia.dominio.entregables.TotPMaCoa;
import es.mapfre.solvencia.dominio.formulacion.CriterioFechas;
import es.mapfre.solvencia.dominio.maestro.CuadrosAmortizacion;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.maestro.ValoresLiquidativos;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.IPCGeneralFuturo;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.LimitesCapital;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.OpcionesGeneracion;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.Tab35050;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.Tab923;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.TabOGA;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.TablaExperiencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ValoresConstantesRescate;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.dominio.scr.ValoresEstres;

/**
 * @author pdv
 * @version 1.0
 * @created 27-ene-2014 19:02:58
 */
public interface IObtenerConfiguracion {
	
	enum OrdenAsegurado {
		ASEG1, ASEG2, ASEG3, ASEG4, ASEG5;
	}

	/**
	 * Halla, en base a la tabla de mortalidad indicada en la entrada, la edad a
	 * la que la probabilidad de la misma se hace cero. Para ello, se buscará en
	 * al estructura de datos tablasExperiencia el valor del atributo
	 * K2TIPOTABLA filtrando tablasExperiencia por:
	 * 
	 * tablasExperiencia.ktabla = ktabla (parámetro de entrada)
	 * tablasExperiencia.fecanula > feccierre.
	 * 
	 * En base a este K2TIPOTABLA recuperado se buscará en la estructura de
	 * datos configValoresTabla el registro correspondiente a la tabla indicada
	 * en el parámetro de entrada ktabla con las siguientes condiciones:
	 * <ul>
	 * <li><b>Si tablasExperiencia.K2TIPOTABLA = 1</b> ( Tradicional) àSe
	 * buscará en configValoresTabla el identificador de la tabla que cumpla con
	 * las siguientes condiciones :</li>
	 * </ul>
	 * <ul>
	 * <li>configValoresTabla.wktabla = ktabla</li>
	 * <li>configValoresTabla.wkinteres = wkinteres</li>
	 * <li>configValoresTabla.wksobremort = wksobremort</li>
	 * <li>configValoresTabla.wksobreries = wksobreries</li>
	 * <li>configValoresTabla.k2tipovalor = 2</li>
	 * </ul>
	 * Para la tabla hallada se buscará en el array de valoresTablaExperiencia
	 * de la misma el registro con wkvalor = 0 para el mínimo kedad de entre los
	 * existentes.
	 * 
	 * 
	 * <ul>
	 * <li><b>Si tablasExperiencia.K2TIPOTABLA = 2</b> (Generacional) à Se
	 * buscará en configValoresTabla el identificador de la tabla que cumpla con
	 * las siguientes condiciones :</li>
	 * </ul>
	 * <ul>
	 * <li>configValoresTabla.wktabla = ktabla</li>
	 * <li>configValoresTabla.wkanacimiento = wkanacimiento</li>
	 * <li>configValoresTabla.k2tipovalor = 2</li>
	 * </ul>
	 * Para la tabla hallada se buscará en el array de valoresTablaExperiencia
	 * de la misma el registro con wkvalor = 0 para el mínimo kedad de entre los
	 * existentes.
	 * 
	 * 
	 * <ul>
	 * <li><b>Si tablasExperiencia.K2TIPOTABLA = 3</b> (Complementarias) à Se
	 * buscará en configValoresTabla el identificador de la tabla que cumpla con
	 * las siguientes condiciones :</li>
	 * </ul>
	 * <ul>
	 * <li>valoresTablaExperiencia. wktabla = ktabla</li>
	 * <li>configValoresTabla.k2tipovalor = 11</li>
	 * </ul>
	 * Para la tabla hallada se buscará en el array de valoresTablaExperiencia
	 * de la misma el registro con wkvalor = 0 para el mínimo kedad de entre los
	 * existentes.
	 * 
	 * 
	 * @param umic
	 * @param btc
	 * @param wkanacimiento
	 * @param sexAseg
	 * @param ordenAsegurado
	 */
	Integer recuperarEdadMax(Umic umic, DetalleBaseTecnica btc, String wkanacimiento, 
			String sexAseg, OrdenAsegurado ordenAsegurado);

	/**
	 * Recuperar� de la entidad GastosReales aquel con:
	 * 
	 * ktipobt, ccanal, cnegocio, kramo y kmodalidad coincidentes con los
	 * par�metros de entrada y fecDesde <= fecCierre <= fecHasta.
	 * 
	 * si no se encuentra se realizir� la b�squeda sin tener en cuenta la
	 * modalidad (kmodalidad). Si a�n as� no se encuentra registro se retornar�
	 * error.
	 * 
	 * @param ktipobt
	 * @param ccanal
	 * @param cnegocio
	 * 
	 * @param kramo
	 * @param kmodalidad
	 * @param fecCierre
	 */
	GastosReales recuperarGastosReales(String ktipobt, Integer ccanal,
			String cnegocio, String kramo, Integer kmodalidad,
			Timestamp fecCierre, String matching);

	/**
	 * Se accederá a la estructura de datos ipcGralFuturo y se recuperará el
	 * dato ipc0Pipcgas que cumpla la condición: ipc0Finicio <= fecCierre <=
	 * ipc0Ffin
	 * 
	 * @param fecCierre
	 */
	java.math.BigDecimal recuperarIpcFuturo(Timestamp fecCierre, String bt);
	
	/**
	 * Se recupera la estructura de datos ipcGralFuturo que cumpla la condición: 
	 * ipc0Finicio <= fecCierre <= ipc0Ffin
	 * 
	 * @param fecCierre
	 */
	IPCGeneralFuturo recuperarFondosIpcFuturo(Timestamp fecCierre, String bt);

	/**
	 * Halla, en base a la tabla de mortalidad indicada en la entrada, las
	 * probabilidad de la misma para una edad dada, informada en el parámetro de
	 * entrada kedad.
	 * 
	 * Para ello, se buscará en la estructura de datos tablasExperiencia el
	 * valor del atributo K2TIPOTABLA filtrando tablasExperiencia por:
	 * 
	 * tablasExperiencia.ktabla = ktabla (parámetro de entrada)
	 * tablasExperiencia.fecanula > feccierre.
	 * 
	 * En base a este K2TIPOTABLA recuperado se buscará en la estructura de
	 * datos configValoresTabla el registro correspondiente a la tabla indicada
	 * en el parámetro de entrada ktabla con las siguientes condiciones:
	 * <ul>
	 * <li><b>Si tablasExperiencia.K2TIPOTABLA = 1</b> ( Tradicional) àSe
	 * buscará en configValoresTabla el identificador de la tabla que cumpla con
	 * las siguientes condiciones :</li>
	 * </ul>
	 * <ul>
	 * <li>configValoresTabla.wktabla = ktabla</li>
	 * <li>configValoresTabla.wkinteres = wkinteres</li>
	 * <li>configValoresTabla.wksobremort = wksobremort</li>
	 * <li>configValoresTabla.wksobreries = wksobreries</li>
	 * <li>configValoresTabla.k2tipovalor = 2</li>
	 * </ul>
	 * Para la tabla hallada se buscará en el array de valoresTablaExperiencia
	 * de la misma el registro con valoresTablaExperiencia.kedad = kedad
	 * (parámetro de entada) Se retornará el dato wkvalor para la edad así
	 * recuperado.
	 * 
	 * <ul>
	 * <li><b>Si tablasExperiencia.K2TIPOTABLA = 2</b> (Generacional) à Se
	 * buscará en configValoresTabla el identificador de la tabla que cumpla con
	 * las siguientes condiciones :</li>
	 * </ul>
	 * <ul>
	 * <li>configValoresTabla.wktabla = ktabla</li>
	 * <li>configValoresTabla.wkanacimiento = wkanacimiento</li>
	 * <li>configValoresTabla.k2tipovalor = 2</li>
	 * </ul>
	 * Para la tabla hallada se buscará en el array de valoresTablaExperiencia
	 * de la misma el registro con valoresTablaExperiencia.kedad = kedad
	 * (parámetro de entada) Se retornará el dato wkvalor para la edad así
	 * recuperado.
	 * 
	 * <ul>
	 * <li><b>Si tablasExperiencia.K2TIPOTABLA = 3</b> (Complementarias) à Se
	 * buscará en configValoresTabla el identificador de la tabla que cumpla con
	 * las siguientes condiciones :</li>
	 * </ul>
	 * <ul>
	 * <li>valoresTablaExperiencia. wktabla = ktabla</li>
	 * <li>configValoresTabla.k2tipovalor = 11</li>
	 * </ul>
	 * Para la tabla hallada se buscará en el array de valoresTablaExperiencia
	 * de la misma el registro con valoresTablaExperiencia.kedad = kedad
	 * (parámetro de entada) Se retornará el dato wkvalor para la edad así
	 * recuperado.
	 * 
	 * @param feccierre
	 * @param ktabla
	 * @param wkanacimiento
	 * @param wkinteres
	 * @param wksobremort
	 * @param wksobreries
	 * @param kedad
	 * @param wkvalor
	 */

	java.math.BigDecimal recuperarLx(Timestamp feccierre, Integer ktabla,
			String wkanacimiento, java.math.BigDecimal wkinteres,
			java.math.BigDecimal wksobremort, java.math.BigDecimal wksobreries,
			Integer kedad);
	
	/**
	 * Retorna los valores de tipos de inter�s correspondientes a la curva tipo
	 * para un código de curva y fecha dados. 
	 * Con este codCurvaTipos se buscar� en ValoresCurvasTipos
	 * los registros que cumplan las siguientes condiciones:
	 * ValoresCurvasTipo.codCurvaTipos = codCurvaTipos
	 * ValoresCurvasTipo.fecEfecCurva <b>m�xima fecha</b> para el c�digo de
	 * curva dado tal que ValoresCurvasTipo.fecEfecCurva <= fcalc
	 * 
	 * @param codCurvaTipos
	 * @param fcalc
	 * @return valoresCurva
	 */
	List<ValoresCurvaTipo> recuperarValoresTipos(String codCurvaTipos, Timestamp fcalc);

	/**
	 * Se accederá a la estructura de datos variablesApoyo que almacena la
	 * información de las variables de apoyo al cálculo. Se realizará la
	 * búsqueda del valor de la variable indicada de la siguiente forma:
	 * <ul>
	 * <li>Búsqueda Concreta
	 * <ul>
	 * <li>Se buscará el elemento concreto utilizando toda la clave,
	 * carteraOrigen, modalidad, garantía, nombreVariable</li>
	 * <li>Si se encuentra la clave completa, se selecciona y devuelve el valor
	 * correspondiente.</li>
	 * <li>En caso de no encontrar la clave, se inicia el siguiente
	 * procedimiento de búsqueda.</li>
	 * </ul>
	 * </li>
	 * <li>Búsqueda Por Cartera de origen.
	 * <ul>
	 * <li>Se buscará el elemento concreto utilizando la clave carteraOrigen, y
	 * nombreVariable, sin indicar ni modalidad ni garantía</li>
	 * <li>Si se encuentra la clave, se selecciona y devuelve el valor
	 * correspondiente.</li>
	 * <li>En caso de no encontrar la clave, se inicia el siguiente
	 * procedimiento de búsqueda.</li>
	 * </ul>
	 * </li>
	 * <li>Búsqueda General.
	 * <ul>
	 * <li>Se buscará el elemento concreto utilizando únicamente la clave
	 * nombreVariable, sin indicar ni cartera, ni modalidad ni garantía</li>
	 * <li>Si se encuentra la clave, se selecciona y devuelve el valor
	 * correspondiente.</li>
	 * <li>En caso de no encontrar la clave, se modificará el dato swEncontrado=
	 * 'N' para informar al procedimiento de que no existe la variable a ningún
	 * nivel.</li>
	 * </ul>
	 * </ul>
	 * 
	 * @param carteraOrigen
	 * @param modalidad
	 * @param garantia
	 * @param basetec
	 * @param nombreVariable
	 */
	Object recuperarDefinicionAuxiliar(Integer carteraOrigen,
			Integer modalidad, Integer garantia, String basetec, String nombreVariable);
	
	
	/**
	 * <li>Búsqueda General.
	 * <ul>
	 * <li>Se buscará el elemento concreto utilizando únicamente la clave
	 * nombreVariable, sin indicar ni cartera, ni modalidad ni garantía</li>
	 * <li>Si se encuentra la clave, se selecciona y devuelve el valor
	 * correspondiente.</li>
	 * 
	 * @param nombreVariable
	 * @return
	 */
	Object recuperarDefinicionAuxiliar(String nombreVariable);

	/**
	 * Valida que el código de la BT del proceso inidcado en le parámentro de
	 * entrada exista en la tabla de definición de bases técnicas.
	 * 
	 * @param validaBt
	 *            resultado de la validacion: true == existe la BT indicada en
	 *            el par�metro de entrada false == no exise la BT indicada en el
	 *            par�metro de entrada
	 * @param ktipobt
	 */
	Boolean validarCodBaseTecnica(String ktipobt);

	/**
	 * En la estructura de datos valoresCtesRescates se almacena la información
	 * de las constantes de rescates necesarias en la formulación de la
	 * corriente de Rescates. Es aquí donde están definidos los tramos y valores
	 * reales a usar. Se realizará la búsqueda a esta estructura mediante la
	 * clave:
	 * 
	 * @param vcr0Kk1
	 * @param vcr0Kduracion
	 *            : vcr0Kk1 y vcr0Kduracion.
	 * 
	 *            Si se encuentra la clave se selecciona y se devuelve el valor
	 *            del campo vcr0PorcKonst. Si por el contrario no se encuentra,
	 *            se devolverá nulo.
	 */
	java.math.BigDecimal recuperarCteRescate(String kk1, Integer kduracion);
	
	/**
	 * 
	 * @param kk1
	 * @param kduracion
	 * @return List<ValoresConstantesRescate>
	 */
	List<ValoresConstantesRescate> recuperarCtesRescate(String kk1, Integer kduracion);
	

	/**
	 * Halla, en base a la tabla de mortalidad indicada en la entrada, todas las
	 * probabilidades de la misma. Para ello, se buscará en la estructura de
	 * datos tablasExperiencia el valor del atributo K2TIPOTABLA filtrando
	 * tablasExperiencia por:
	 * 
	 * tablasExperiencia.ktabla = Halla, en base a la tabla de mortalidad
	 * indicada en la entrada, todas las probabilidades de la misma. Para ello,
	 * se buscará en la estructura de datos tablasExperiencia el valor del
	 * atributo K2TIPOTABLA filtrando tablasExperiencia por:ktabla (parámetro de
	 * entrada) tablasExperiencia.fecanula > feccierre.
	 * 
	 * En base a este K2TIPOTABLA recuperado se buscará en la estructura de
	 * datos configValoresTabla el registro correspondiente a la tabla indicada
	 * en el parámetro de entrada ktabla con las siguientes condiciones:
	 * 
	 * Si tablasExperiencia.K2TIPOTABLA = 1 ( Tradicional) à Se buscará en
	 * configValoresTabla el identificador de la tabla que cumpla con las
	 * siguientes condiciones : configValoresTabla.wktabla = ktabla
	 * configValoresTabla.wkinteres = wkinteres configValoresTabla.wksobremort =
	 * wksobremort configValoresTabla.wksobreries = wksobreries
	 * configValoresTabla.k2tipovalor = 2 Para la tabla hallada se retornarán
	 * todos los registros de valoresTablaExperiencia asociados a la misma y Se
	 * retornará una tabla con los datos recuperados.
	 * 
	 * Si tablasExperiencia.K2TIPOTABLA = 2 (Generacional) à Se buscará en
	 * configValoresTabla el identificador de la tabla que cumpla con las
	 * siguientes condiciones : configValoresTabla.wktabla = ktabla
	 * configValoresTabla.wkanacimiento = wkanacimiento
	 * configValoresTabla.k2tipovalor = 2 Para la tabla hallada se retornarán
	 * todos los registros de valoresTablaExperiencia asociados a la misma y Se
	 * retornará una tabla con los datos recuperados.
	 * 
	 * 
	 * Si tablasExperiencia.K2TIPOTABLA = 3 (Complementarias) à Se buscará en
	 * configValoresTabla el identificador de la tabla que cumpla con las
	 * siguientes condiciones : valoresTablaExperiencia. wktabla = ktabla
	 * configValoresTabla.k2tipovalor = 11 Para la tabla hallada se retornarán
	 * todos los registros de valoresTablaExperiencia asociados a la misma y Se
	 * retornará una tabla con los datos recuperados.
	 * 
	 * @param feccierre
	 * @param ktabla
	 * @param wkanacimiento
	 * @param wkinteres
	 * @param wksobremort
	 * @param wksobreries
	 * @return List<Double> @
	 */

	List<java.math.BigDecimal> recuperarValoresExperiencia(Umic umic,
			DetalleBaseTecnica btc, String wkanacimiento, String sexAseg,
			Integer edadAseg, String tipoValores, IObtenerConfiguracion.OrdenAsegurado ordenAsegurado);

	/**
	 * Recupera los valores de mortalidad
	 * @param feccierre
	 * @param ktabla
	 * @param wkanacimiento
	 * @param wksobremort
	 * @param wksobreries
	 * @return
	 */
	TablaExperiencia recuperarValorDeMortalidad(Timestamp feccierre,
			Integer ktabla, String wkanacimiento, java.math.BigDecimal wksobremort,
			java.math.BigDecimal wksobreries);
	
	List<ValoresLiquidativos> recuperarValoresLiquidativos(Long poliza, String modalidad, String ramo, Integer certificado);

	/**
	 * Se accederá la estructura de datos tabla646 (LimitesCapital) y se
	 * recuperará el registro que cumpla con las condiciones: NTABLA =646
	 * KMODALIDAD = kmodalidad KGARANTIA = kgarantia FEFECFIN <= fecefecto
	 * KEDAD1 <= edadAsegurado <= KEDAD2 NMESHASTA <= numMeses
	 * 
	 * @param kmodalidad
	 * @param kgarantia
	 * @param fecefecto
	 * @param edadAsegurado
	 * @param numMeses
	 * @return LimitesCapital @
	 */

	LimitesCapital recuperarLimitesCapital(String kmodalidad, String kgarantia,
			Timestamp fecEfecto, Integer edadAsegurado, Integer numMeses);

	/**
	 * Se accederá la estructura de datos tabla646 (LimitesCapital) y se
	 * recuperará los registros que cumplan con las condiciones: NTABLA =646
	 * KMODALIDAD = kmodalidad KGARANTIA = kgarantia FEFECFIN <= fecefecto
	 * KEDAD1 <= edadAsegurado <= KEDAD2 
	 * 
	 * @param kmodalidad
	 * @param kgarantia
	 * @param fecefecto
	 * @param edadAsegurado
	 * @return LimitesCapital @
	 */

	List<LimitesCapital> recuperarListaLimitesCapital(String kmodalidad, String kgarantia,
			Timestamp fecEfecto, Integer edadAsegurado);
	/**
	 * Recupera de CONF_FLUJOS_PROB los campos: Prov_Nominal,
	 * Prov_Terminal,Prov_Probable,Prov_No Anulado que cumplan la condición:
	 * Modalidad = modalidad Garantía = garantia Prestación = prestacion Base
	 * Técnica= BaseTecnica
	 * 
	 * @param modalidad
	 * @param garantia
	 * @param prestacion
	 * @param baseTecnica
	 * @return @
	 */
	FlujosProbables recuperarConfProv(Integer modalidad, Integer garantia,
			String prestacion, String baseTecnica);
	

	/**
	 * 
	 * Recuperará los datos de la estructura opcionesGeneracion, para la
	 * modalidad y garantía de la umic indicados en los parámetros de entrada.
	 * 
	 * @param kmodalidad
	 * @param kgarantia
	 * @return OpcionesGeneracion
	 */
	OpcionesGeneracion recuperarOpcionesGeneracion(Integer kmodalidad,
			Integer kgarantia, String kprestacion);

	/**
	 * 
	 * Recuperará de la estructura opcionesGeneracion, el criterio de generación
	 * de la fecha de pago y la fecha de devengo de la modalidad, garantía y
	 * codsubproceso indicados, de la siguiente forma: Si codSubproceso =
	 * PROY_VIDA opcionesGeneracion.pagovida opcionesGeneracion.devenvida Si
	 * codSubproceso = PROY_FALL opcionesGeneracion.pagofall
	 * opcionesGeneracion.devenfall Si codSubproceso = PROY_INVA
	 * opcionesGeneracion.pagoinva opcionesGeneracion.deveninva Si codSubproceso
	 * = PROY_PRIMA opcionesGeneracion.pagoprim opcionesGeneracion.devenprim Si
	 * codSubproceso = PROY_GTOS opcionesGeneracion.pagogast
	 * opcionesGeneracion.devengast Si codSubproceso = PROY_COMI
	 * opcionesGeneracion.pagocomi opcionesGeneracion.devencomi Si codSubproceso
	 * = PROY_RESC opcionesGeneracion.pagoanul opcionesGeneracion.devenanul Si
	 * codSubproceso = PROY_PRV opcionesGeneracion.pagoprvc
	 * opcionesGeneracion.devenprvc
	 * 
	 * @param kmodalidad
	 * @param kgarantia
	 * @param codSubproceso
	 * @return
	 */
	CriterioFechas recuperarCriterioFechas(Integer kmodalidad,
			Integer kgarantia, String kprestacion, String kprestcal, String codSubproceso);
	/**
	 * Recupera de CONF_FLUJOS_PROB el módulo de cálculo correspondiente al
	 * subproceso y tipo de elemento indicados en la entrada. Para ello
	 * recuperará el registro de CONF_FLUJOS_PROB que cumpla con las siguientes
	 * condiciones
	 * 
	 * Modalidad = modalidad Garantía = garantia Prestación = prestacion Base
	 * Técnica= BaseTecnica Una vez recuperado este registro, en base al código
	 * de l subpreoceso y al tipo de elemento buscado retornará el dato
	 * correspondiente a: Si codSubproceso = ‘PROY_VIDA’ Si tipoElemento = ‘01’
	 * retornará el dato del campo vidaNominal Si tipoElemento = ‘02’ retornará
	 * el dato del campo vidaProbable Si tipoElemento = ‘03’ retornará el dato
	 * del campo vidaNoAnulado Si tipoElemento = ‘04’ retornará el dato del
	 * campo vidaActualizado Si tipoElemento = ‘05’ retornará el dato del campo
	 * vidaProvi
	 * 
	 * Si codSubproceso = ‘PROY_FALL’ Si tipoElemento = ‘01’ retornará el dato
	 * del campo fallecNominal Si tipoElemento = ‘02’ retornará el dato del
	 * campo fallecProbable Si tipoElemento = ‘03’ retornará el dato del campo
	 * fallecNoAnulado Si tipoElemento = ‘04’ retornará el dato del campo
	 * fallecActualizado Si tipoElemento = ‘05’ retornará el dato del campo
	 * fallecProvi
	 * 
	 * 
	 * Si codSubproceso = ‘PROY_INVA’ Si tipoElemento = ‘01’ retornará el dato
	 * del campo invNominal Si tipoElemento = ‘02’ retornará el dato del campo
	 * invProbable Si tipoElemento = ‘03’ retornará el dato del campo
	 * invNoAnulado Si tipoElemento = ‘04’ retornará el dato del campo
	 * invActualizado Si tipoElemento = ‘05’ retornará el dato del campo
	 * invProvi
	 * 
	 * 
	 * Si codSubproceso = ‘PROY_PRIMA’ Si tipoElemento = ‘01’ retornará el dato
	 * del campo primaNominal Si tipoElemento = ‘02’ retornará el dato del campo
	 * primaProbable Si tipoElemento = ‘03’ retornará el dato del campo
	 * primaNoAnulado Si tipoElemento = ‘04’ retornará el dato del campo
	 * primaActualizado Si tipoElemento = ‘05’ retornará el dato del campo
	 * primaProvi
	 * 
	 * 
	 * Si codSubproceso = ‘PROY_GTOS’ Si tipoElemento = ‘01’ retornará el dato
	 * del campo gastosNominal Si tipoElemento = ‘02’ retornará el dato del
	 * campo gastosProbable Si tipoElemento = ‘03’ retornará el dato del campo
	 * gastosNoAnulado Si tipoElemento = ‘04’ retornará el dato del campo
	 * gastosActualizado Si tipoElemento = ‘05’ retornará el dato del campo
	 * gastosProvi
	 * 
	 * 
	 * Si codSubproceso = ‘PROY_RESC’ Si tipoElemento = ‘01’ retornará el dato
	 * del campo anulNominal Si tipoElemento = ‘02’ retornará el dato del campo
	 * anulProbable Si tipoElemento = ‘03’ retornará el dato del campo
	 * anulNoAnulado Si tipoElemento = ‘04’ retornará el dato del campo
	 * anulActualizado Si tipoElemento = ‘05’ retornará el dato del campo
	 * anulProvi
	 * 
	 * 
	 * Si codSubproceso = ‘PROY_COMI’ Si tipoElemento = ‘01’ retornará el dato
	 * del campo comiNominal Si tipoElemento = ‘02’ retornará el dato del campo
	 * comiProbable Si tipoElemento = ‘03’ retornará el dato del campo
	 * comiNoAnulado Si tipoElemento = ‘04’ retornará el dato del campo
	 * comiActualizado Si tipoElemento = ‘05’ retornará el dato del campo
	 * comiProvi
	 * 
	 * 
	 * Si codSubproceso = ‘PROY_PRV’ Si tipoElemento = ‘01’ retornará el dato
	 * del campo prvNominal Si tipoElemento = ‘02’ retornará el dato del campo
	 * prvProbable Si tipoElemento = ‘03’ retornará el dato del campo
	 * prvNoAnulado Si tipoElemento = ‘04’ retornará el dato del campo
	 * prvActualizado Si tipoElemento = ‘05’ retornará el dato del campo
	 * prvProvi
	 * 
	 * 
	 * Si alguna de las consultas realizadas no recupera datos, se retornará
	 * como nulo, no se levantará error.
	 * 
	 * @param modalidad
	 * @param garantia
	 * @param prestacion
	 * @param baseTecnica
	 * @param codSubproceso
	 * @param tipoElemento
	 * @return
	 */
	String recuperarModulo(Integer modalidad, Integer garantia,
			String prestacion, String baseTecnica, String codSubproceso,
			String tipoElemento);

	String recuperarModuloFlujoProbable(FlujosProbables flujoProbable, String codSubproceso, String tipoElemento);
	
	/**
	 * Recuperará, de la estructura de datos valoresTablaAnulacionMensuales el
	 * array de los valroes que cumplen con las condiciones:
	 * 
	 * codTabla = codTablaAnul
	 * 
	 * @param ktabla
	 * @return
	 */
	List<ValoresAnulacionMensuales> recuperarTasasAnulMensuales(String ktabla, Timestamp fecCierre);
	
	/**
	 * Recuperará, de la estructura de datos valoresTablaAnulacion el
	 * array de los valroes que cumplen con las condiciones:
	 * 
	 * codTabla = codTablaAnul
	 * 
	 * @param ktabla
	 * @return
	 */
	List<ValoresAnulacion> recuperarTasasAnul(String ktabla, Timestamp fecCierre, String bt);
	
	/**
	 * Recuperará de la estrucutara de Cuadros de Amortización
	 * el cuadro para la clave de la umic que se está procesando. 
	 * 
	 * @param key
	 * @return
	 */
	CuadrosAmortizacion recuperarCuadrosAmortizacion(Umic umic); 
	
	
	/**
	 * 
	 * @param poliza
	 * @param subpoliza
	 * @param certificado
	 * @param nsuscripcion
	 * @return
	 */
	String recuperarDatosEspecificosUmic(Long poliza, Integer subpoliza, Integer certificado, Integer nsuscripcion, String codigo);
	
	/**
	 * Recupera, de la estructura de datos DatosEspecificos, la fecha de diferimiento.
	 * @param poliza
	 * @param subpoliza
	 * @param certificado
	 * @param nsuscripcion
	 * @return
	 */
	Timestamp recuperarVarFdiferimiento(Long poliza, Integer subpoliza, Integer certificado, Integer nsuscripcion);
	/**
	 * Recupera, de la estructura de datos DatosEspecificos, la variable varC2S .
	 * @param poliza
	 * @param subpoliza
	 * @param certificado
	 * @param nsucripcion
	 * @return
	 */
	String recuperarVarC2S(Long poliza, Integer subpoliza, Integer certificado, Integer nsuscripcion);
	/**
	 * Recupera, de la estructura de datos DatosEspecificos, la variable varE1PSJ .
	 * @param poliza
	 * @param subpoliza
	 * @param certificado
	 * @param nsucripcion
	 * @return
	 */
	BigDecimal recuperarVarE1PSJ(Long poliza, Integer subpoliza, Integer certificado, Integer nsuscripcion);
	
	/**
	 * Recupera, de la estructura de datos DatosEspecificos, la variable varVZC2 .
	 * @param poliza
	 * @param subpoliza
	 * @param certificado
	 * @param nsucripcion
	 * @return
	 */
	BigDecimal recuperarVarVZC2(Long poliza, Integer subpoliza, Integer certificado, Integer nsuscripcion);
	
	/**
	 * Recupera, de la estructura de Tabla2000, la variable con codigo indicado .
	 * @param codigo
	
	 * @return
	 */
	BigDecimal recuperarVarTabla2000(String codigo);
	
	/**
	 * Recupera, de la estructura de datos DatosEspecificos, la variable varE3SAL.
	 * @param poliza
	 * @param subpoliza
	 * @param certificado
	 * @param nsucripcion
	 * @return
	 */
	BigDecimal recuperarVarE3SAL(Long poliza, Integer subpoliza, Integer certificado, Integer nsuscripcion);
	
	/**
	 * Recupera, de la estructura de datos DatosEspecificos, la variable varAnoEsp .
	 * @param poliza
	 * @param subpoliza
	 * @param certificado
	 * @param nsucripcion
	 * @return
	 */
	BigDecimal recuperarVarAnoEsp(Long poliza, Integer subpoliza, Integer certificado, Integer nsuscripcion);
	/**
	 * Recupera, de la estructura de datos DatosEspecificos, la variable varPorviuss .
	 * @param poliza
	 * @param subpoliza
	 * @param certificado
	 * @param nsucripcion
	 * @return
	 */
	BigDecimal recuperarVarPorvius(Long poliza, Integer subpoliza, Integer certificado, Integer nsuscripcion);
	/**
	 * Recupera, de la estructura de datos DatosEspecificos, la variable varPrpss .
	 * @param poliza
	 * @param subpoliza
	 * @param certificado
	 * @param nsucripcion
	 * @return
	 */
	BigDecimal recuperarVarPrpss(Long poliza, Integer subpoliza, Integer certificado, Integer nsuscripcion);
	
	/**
	 * Reculera los flujos totales
	 * 
	 * @param umic
	 * @return TotalesFlujos
	 */
	TotalesFlujos recuperarTotalesFlujos(Umic umic, DetalleBaseTecnica detalleBT);


	
	/**
	 * Recupera los valores de estrés
	 * 
	 * @param feccierre
	 * @param bt
	 * @return List<ValoresEstres>
	 */
	List<ValoresEstres> recuperarValoresEstres(Timestamp feccierre, String bt);
	
	OpcionesGeneracion recuperarOpcionesGeneracionPrestCal(Integer kmodalidad, Integer kgarantia, String kprestacion,
			String kprestcal);
	
	/**
	 * Recupera los valores del catalogo tab923
	 * 
	 * @param modalidad
	 * @param finip
	 * @param ffinp
	 * @return List<Tab923>
	 */
	List<Tab923> recuperarDatosTab923(Integer modalidad, Timestamp finip, Timestamp ffinp);
	
	List<Tab35050> recuperarDatosTab35050(String ramo, Integer modalidad);
	
	FlujCoaSeg recuperarFlujCoaSeg(String bt, Timestamp fcierre, String negocio, Integer canal, String ramo, 
			Integer modalidad, String segmento1, String tiposubriesgo, Long poliza, Integer subpoliza, Integer nsuscri,
			 String carterainv, String gapact, Timestamp fecdesde, String gestionit, String tabla1aseg1);
	
	FlujTcas recuperarFlujTcas(String bt, Timestamp fcierre, String negocio, Integer canal, String ramo, 
			Integer modalidad, Long poliza, Integer subpoliza, Integer nsuscri, String carterainv, String gapact, Timestamp fecdesde);
	
	ProvCoaSeg recuperarProvCoaSeg(String bt, Timestamp fcierre, String negocio, Integer canal, String ramo, 
			Integer modalidad, String segmento1, String tiposubriesgo, Long poliza, Integer subpoliza, Integer nsuscri,
			 String carterainv, String gapact, String gestionit, String tabla1aseg1);
	
	TotPMaCoa recuperarTotPMaCoa(Long poliza, Integer subpoliza, Integer nsuscri, String bt, Timestamp fcierre);

	FlujPMaCoa recuperarFlujPMaCoa(Long poliza, Integer subpoliza, Integer nsuscri, String bt, Timestamp fcierre, Timestamp fecdesde);

	FlujPMdCoa recuperarFlujPMdCoa(Integer kmodalidad, Integer kgarantia, String kprestacion, Long poliza, 
			Integer subpoliza, Integer kcertificado, Integer nsuscri, String bt, Timestamp fcierre);
	
	Collection<DatosGenerales> recuperarMaestro();
	
	FlujSuscri recuperarFlujSuscri(String bt, Timestamp fcierre, String negocio, Integer canal, String ramo, 
			Integer modalidad, String segmento1, String tiposubriesgo, Long poliza, Integer subpoliza, Integer nsuscri,
			 String carterainv, String gapact, Timestamp fecdesde, Integer kgarantia);
	TabOGA recuperarTabOGA(String cartera, Timestamp fcierre);
	
	FlujInfSCR recuperarFlujInfSCR(String bt, Timestamp fcierre, Integer canal, String negocio, String uoa, 
			String carteracontrato, String carteracohort, String carteraoner, Timestamp fproyflujest,
			 String carterainv, Integer modalidad);
	
}