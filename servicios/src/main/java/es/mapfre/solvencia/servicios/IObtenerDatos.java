package es.mapfre.solvencia.servicios;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.formulacion.Periodo;
import es.mapfre.solvencia.dominio.formulacion.PlanPagos;
import es.mapfre.solvencia.dominio.maestro.PagosPlanificados;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TerminosPMCUmic;

public interface IObtenerDatos {

	/**
	 * Consulta que retornará los pagos planificados para una Umic dada, filtrando
	 * por:
	 * 
	 * <ul>
	 * <li>KPOLIZA</li>
	 * <li>SUBPOLIZA</li>
	 * <li>KCERTIFICADO</li>
	 * <li>NSUSCRI</li>
	 * <li>KGARANTIA</li>
	 * <li>KPRESTACION</li>
	 * <li>KAJUSTE</li>
	 * <li>FPLREAEFECTO >= fcalc</li>
	 * </ul>
	 * 
	 * @param kpoliza
	 * @param ksubpol
	 * @param kcerti
	 * @param kgrsus
	 * @param cgarantia
	 * @param kpresta
	 * @param kajuste
	 * @param fplreaEfecto
	 * @param pagosUmic
	 */
	List<PagosPlanificados> recuperarPagosPlanificados(Long kpoliza, Integer ksubpol, Integer kcerti, Integer kgrsus,
			Integer cgarantia, String cpresta, String kpresta, Integer kajuste, Timestamp fplreaEfecto);

	List<PagosPlanificados> recuperarPagosPlanificadosLocas(Long kpoliza, Integer ksubpol, Integer kcerti,
			Integer kgrsus, Integer cgarantia, String cpresta, String kpresta, String cprestaFict, Integer kajuste, Timestamp fplreaEfecto,
			Integer norden);

	/**
	 * Consulta que retornará los periodos generados en el subproceso
	 * PERIODOS_MOD_GAR para la base técnica BTI Se consultarán los periodos con las
	 * condiciones: baseTecnica = bti fecCierre = fichaProceso.fecCierre claveUmic =
	 * Umic.claveUmic
	 * 
	 * Retornará el array de PERIODOS que cumplan las condiciones de la búsqueda.
	 * 
	 * @param claveUmmic
	 * @param fecCierre
	 * @param periodosBti
	 */
	List<Periodo> recuperarPeriodos(Timestamp fecCierre, String bti, UmicKey key);

	/**
	 * Consulta que retornará los periodos generados en el subproceso
	 * PERIODOS_MOD_GAR para la base técnica BTI Se consultarán los periodos con las
	 * condiciones: baseTecnica = "BTI" (identificador de BTI) fecCierre =
	 * fichaProceso.fecCierre claveUmic = Umic.claveUmic
	 * 
	 * Retornará el array de PERIODOS que cumplan las condiciones de la búsqueda.
	 * 
	 * @param claveUmmic
	 * @param fecCierre
	 * @param periodosBti
	 */
	List<Periodo> recuperarPeriodosBTI(Timestamp fecCierre, UmicKey key);

	/**
	 * Recuperará los pagos de rentas calculados para la umic filtrando por los
	 * parámentros de entrada (clave de la umic):
	 * 
	 * <ul>
	 * <li>kmodalidad</li>
	 * <li>kpoliza</li>
	 * <li>ksubpoliza</li>
	 * <li>kcertificado</li>
	 * <li>nsuscri</li>
	 * <li>norden</li>
	 * <li>kgarantia</li>
	 * <li>kprestacion</li>
	 * <li>kajuste</li>
	 * <li>ctipoaport</li>
	 * </ul>
	 * 
	 * @param kmodalidad
	 * @param kpoliza
	 * @param ksubpol
	 * @param kcerti
	 * @param kgrsus
	 * @param norden
	 * @param cgarantia
	 * @param kpresta
	 * @param kajuste
	 * @param ctipoaport
	 * @param planPagosRentas Estrucutra que alcena los pagos de las rentas
	 *                        caluladodos en base a la definicion
	 */
	List<PlanPagos> recuperarPlanPagos(UmicKey key);

	/**
	 * Consulta que retornará la proyeccion de la umic, tipo corriente y fecha
	 * cierre indicadas y que han sido calculadas previamente en la base técnica
	 * BTI.
	 * 
	 * baseTecnica =BTI fecCierre = proyUmic.fecCierre tipoCorriente =
	 * proyUmic.tipoCorriente claveUmic = proyUmic.claveUmic --> todos los campos
	 * que componen la clave de la umic
	 * 
	 * Retornará el array de corrientes que cumplan las condiciones de la búsqueda.
	 * 
	 * @param proyUmic
	 * @param proyBti  listado de corrientes
	 */
	List<DetalleCorriente> recuperarProyBTI(Timestamp fcierre, UmicKey umicKey);

	/**
	 * Consulta que retornará la proyeccion de la umic previamente calculada para
	 * UMIC (clave umic) , tipo corriente, base técnica y fecha cierre indicadas.
	 * 
	 * Recuperará la proyección previamente calculada donde:
	 * 
	 * detalleCorrientes.tipoCorriente = tipoCorriente (parámetro de entrada)
	 * detalleCorrientes.ktipobt = ktipobt (parámetro de entrada) y los datos de la
	 * clave de la umic:
	 * 
	 * detalleCorrientes.feccierre= claveUmic.feccierre
	 * detalleCorrientes.kmodalidad= claveUmic.kmodalidad detalleCorrientes.kpoliza=
	 * claveUmic.kpoliza detalleCorrientes.ksubpoliza= claveUmic.ksubpoliza
	 * detalleCorrientes.kcertificado= claveUmic.kcertificado
	 * detalleCorrientes.nsuscri= claveUmic.nsuscri detalleCorrientes.norden=
	 * claveUmic.norden detalleCorrientes.kgarantia= claveUmic.kgarantia
	 * detalleCorrientes.kprestacion= claveUmic.kprestacion
	 * detalleCorrientes.kajuste= claveUmic.kajuste detalleCorrientes.ctipoaport=
	 * claveUmic.ctipoaport
	 * 
	 * @return listaCorrientes
	 * @param ktipobt
	 * @param tipoCorriente
	 * @param claveUmic     Datos de la umic que componen la clave de la misma para
	 *                      el subproceso:
	 * 
	 *                      feccierre kmodalidad kpoliza ksubpoliza kcertificado
	 *                      nsuscri norden kgarantia kprestacion kajuste ctipoaport
	 * 
	 */
	List<DetalleCorriente> recuperarProyeccion(String ktipobt, Timestamp fcierre, UmicKey umicKey);
	
	List<DetalleCorriente> recuperarProyeccionCualquierNodo(String ktipobt, Timestamp fcierre, UmicKey umicKey);

	/**
	 * Devuelve si la umic figura en la caché de incidencia con Error, no con aviso.
	 * 
	 * @param claveUmic
	 * @param fecCierre
	 * @param baseTecnica
	 * @return true si hay un error, false si no tiene error
	 */
	Boolean recuperarUmicIncidente(UmicKey claveUmic, Timestamp fecCierre, String baseTecnica);

	/**
	 * Consulta que retornará la base técnica de la umic previamente calculada para
	 * UMIC (clave umic) , base técnica y fecha cierre indicadas.
	 * 
	 * Recuperará la base técnica previamente calculada donde:
	 * detalleCorrientes.ktipobt = ktipobt (parámetro de entrada) y los datos de la
	 * clave de la umic: detalleCorrientes.cnegocio = claveUmic.cnegocio
	 * detalleCorrientes.ccanal = claveUmic.ccanal detalleCorrientes.ccartera=
	 * claveUmic.ccartera detalleCorrientes.feccierre= claveUmic.feccierre
	 * detalleCorrientes.kmodalidad= claveUmic.kmodalidad detalleCorrientes.kpoliza=
	 * claveUmic.kpoliza detalleCorrientes.ksubpoliza= claveUmic.ksubpoliza
	 * detalleCorrientes.kcertificado= claveUmic.kcertificado
	 * detalleCorrientes.nsuscri= claveUmic.nsuscri detalleCorrientes.norden=
	 * claveUmic.norden detalleCorrientes.kgarantia= claveUmic.kgarantia
	 * detalleCorrientes.kprestacion= claveUmic.kprestacion
	 * detalleCorrientes.kajuste= claveUmic.kajuste detalleCorrientes.ctipoaport=
	 * claveUmic.ctipoaport
	 * 
	 * @param fecCierre
	 * @param baseTec
	 * @param umicKey
	 * @return
	 */
	DetalleBaseTecnica recuperarBTCUmic(Timestamp fecCierre, String baseTec, UmicKey umicKey);

	/**
	 * Función que devuelve la clave de la UMIC principal de una modalidad, poliza,
	 * subpoliza, certificado y nsuscripcion
	 * 
	 * @param kmodalidad
	 * @param kpoliza
	 * @param ksubpoliza
	 * @param kcertificado
	 * @param nsuscri
	 * @return
	 */
	UmicKey recuperarUmicPrincipal(Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer kcertificado,
			Integer nsuscri, String ctipoaport);

	/**
	 * Función que devuelve las claves de las UMIC secundarias de una modalidad,
	 * poliza, subpoliza, certificado y nsuscripcion
	 * 
	 * @param kmodalidad
	 * @param kpoliza
	 * @param ksubpoliza
	 * @param kcertificado
	 * @param nsuscri
	 * @return
	 */
	Set<UmicKey> recuperarUmicSecundarias(Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer kcertificado,
			Integer nsuscri, String ctipoaport);

	/**
	 * Función que devuelve los datos almacenados en TerminosPMCUmic
	 * 
	 * @param claveUmic
	 * @param baseTec
	 * @return
	 */
	TerminosPMCUmic recuperarTerminosPMCUmic(UmicKey claveUmic, String baseTec, Integer iteracion);

	/**
	 * Función que devuelve la UMIC con garantía 310 de una modalidad, poliza,
	 * subpoliza, certificado y nsuscripcion
	 * 
	 * @param kmodalidad
	 * @param kpoliza
	 * @param ksubpoliza
	 * @param kcertificado
	 * @param nsuscri
	 * @return
	 */
	Umic recuperarUmic310(Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer kcertificado, Integer nsuscri,
			String ctipoaport);

	/**
	 * Función que devuelve una UMIC a partir de su clave (UmicKey)
	 * 
	 * @param umicKey
	 * @return
	 */
	Umic recuperarUmic(UmicKey umicKey);

	/**
	 * Función que devuelve la suma del saldo de las UMICs principales y secundarias
	 * 
	 * @param kmodalidad
	 * @param kpoliza
	 * @param ksubpoliza
	 * @param kcertificado
	 * @param nsuscri
	 * @return
	 */
	BigDecimal recuperarSaldoTotal(Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer kcertificado,
			Integer nsuscri);

	/**
	 * Función que devuelve la UMIC del titular de una modalidad, poliza, subpoliza,
	 * certificado y nsuscripcion
	 * 
	 * @param kmodalidad
	 * @param kpoliza
	 * @param ksubpoliza
	 * @param kcertificado
	 * @param nsuscri
	 * @return
	 */
	Umic recuperarUmicTitular(Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer kcertificado,
			Integer nsuscri, String kbencon, Timestamp feciniciorenta, Integer forpagrent, String cpagrenta);

	/**
	 * Función que devuelve todas las Umics del titular de una modalidad, poliza,
	 * subpoliza, certificado y nsuscripcion
	 * 
	 * @param kmodalidad
	 * @param kpoliza
	 * @param ksubpoliza
	 * @param kcertificado
	 * @param nsuscri
	 * @return
	 */
	List<Umic> recuperarTodasUmicsTitularSuscripcion(Integer kmodalidad, Long kpoliza, Integer ksubpoliza,
			Integer kcertificado, Integer nsuscri, String kbencon);

	Umic recuperarUmicMensual(Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer kcertificado,
			Integer nsuscri, Integer kgarantia, Integer kajuste, String ctipoaport, String kbencon, String cpagrenta);

	/**
	 * Función que devuelve todas las Umics del titular de una modalidad, poliza,
	 * subpoliza, certificado, nsuscripcion, garantia, ajuste, tipo aport y
	 * cpagrenta
	 * 
	 * @param kmodalidad
	 * @param kpoliza
	 * @param ksubpoliza
	 * @param kcertificado
	 * @param nsuscri
	 * @param kgarantia
	 * @param kajuste
	 * @param ctipoaport
	 * @param cpagrenta
	 * @return
	 */

	/**
	 * Función que devuelve todas las Umics relacionadas de una modalidad, poliza,
	 * subpoliza, certificado, nsuscripcion, garantia, prestación
	 * 
	 * @param kmodalidad
	 * @param kpoliza
	 * @param ksubpoliza
	 * @param kcertificado
	 * @param nsuscri
	 * @param kgarantia
	 * @param kprestacion
	 * @return
	 */

	Integer recuperarUmicRelacionadas(UmicKey umicKey, Integer nOrden);

	List<DetalleCorriente> recuperarProyBTIPROY(Timestamp fcierre, UmicKey umicKey);
	
	Umic recuperarUmicMensualBNC(Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer kcertificado,
			Integer nsuscri, Integer kgarantia, Integer kajuste, String ctipoaport, String kbencon, 
			String kpresta);
	
	List<Umic> recuperarTodasUmicsUniv(Integer kmodalidad, Long kpoliza, Integer ksubpoliza);
	
	/**
	 * Función que devuelve la clave de la UMIC principal de una modalidad, poliza,
	 * subpoliza, certificado y nsuscripcion
	 * 
	 * @param kmodalidad
	 * @param kpoliza
	 * @param ksubpoliza
	 * @param kcertificado
	 * @param nsuscri
	 * @return
	 */
	UmicKey recuperarUmicPrincipalPMRR02(Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer kcertificado,
			Integer nsuscri, String ctipoaport, String kprestacion, Integer norden);
}
