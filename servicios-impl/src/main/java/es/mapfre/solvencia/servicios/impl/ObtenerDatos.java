package es.mapfre.solvencia.servicios.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.aggregator.BigDecimalSum;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.impl.formulacion.PeriodoDao;
import es.mapfre.solvencia.dao.impl.formulacion.PlanPagosDao;
import es.mapfre.solvencia.dao.impl.maestro.DatosGeneralesDao;
import es.mapfre.solvencia.dao.impl.maestro.PagosPlanificadosDao;
import es.mapfre.solvencia.dao.impl.maestro.UmicDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.DetalleBaseTecnicaDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.DetalleCorrienteDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.IncidenciaDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.TerminosPMCUmicDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.formulacion.Periodo;
import es.mapfre.solvencia.dominio.formulacion.PlanPagos;
import es.mapfre.solvencia.dominio.maestro.PagosPlanificados;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TerminosPMCUmic;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.servicios.IObtenerDatos;

public class ObtenerDatos implements IObtenerDatos {

	private static Logger log = LoggerFactory.getLogger(ObtenerDatos.class);

	private PagosPlanificadosDao pagosPlanificadosDao = new PagosPlanificadosDao();
	private PlanPagosDao planPagosDao = new PlanPagosDao();
	private PeriodoDao periodoDao = new PeriodoDao();
	private DetalleCorrienteDao detallesDao = new DetalleCorrienteDao();
	private IncidenciaDao incidenciaDao = new IncidenciaDao();
	private DetalleBaseTecnicaDao detalleBaseTecnicaDao = new DetalleBaseTecnicaDao();
	private DatosGeneralesDao datosGeneralesDao = new DatosGeneralesDao();
	private TerminosPMCUmicDao terminosPMCUmicDao = new TerminosPMCUmicDao();
	private UmicDao umicDao = new UmicDao();

	public static final String CACHE_CAPITALES = "capitales";
	public static final String CACHE_CAPITALES_GETISALDO = "getIsaldo";

	private static final String E01 = "01";
	private static final String AM = "AM";
	private static final String GF = "GF";
	private static final String GE = "GE";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.mapfre.solvencia.servicios.IObtenerDatos#recuperarPagosPlanificados(java.
	 * lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 * java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer,
	 * java.sql.Timestamp)
	 */
	@Override
	public List<PagosPlanificados> recuperarPagosPlanificados(Long kpoliza, Integer ksubpol, Integer kcerti,
			Integer kgrsus, Integer cgarantia, String cpresta, String kpresta, Integer kajuste,
			Timestamp fplreaEfecto) {

		List<PagosPlanificados> pagosPlanificados = null;
		Set set = null;

		if (log.isDebugEnabled()) {

			log.debug("kpoliza = {}", kpoliza);
			log.debug("ksubpol = {}", ksubpol);
			log.debug("kcerti = {}", kcerti);
			log.debug("kgrsus = {}", kgrsus);
			log.debug("cgarantia = {}", cgarantia);
			log.debug("cpresta = {}", cpresta);
			log.debug("kpresta = {}", kpresta);
			log.debug("kajuste = {}", kajuste);
			log.debug("fplreaEfecto = {}", fplreaEfecto);

		}

		set = pagosPlanificadosDao.getValues(cgarantia, kpoliza, ksubpol, kajuste, kcerti, kgrsus, cpresta, kpresta,
				fplreaEfecto);

		if (set != null) {

			pagosPlanificados = new ArrayList<PagosPlanificados>();

			Iterator iter = set.iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				pagosPlanificados.add((PagosPlanificados) entry.getValue());
			}
		}

		return pagosPlanificados;
	}

	public List<PagosPlanificados> recuperarPagosPlanificadosLocas(Long kpoliza, Integer ksubpol, Integer kcerti,
			Integer kgrsus, Integer cgarantia, String cpresta, String kpresta, String cprestaFict, Integer kajuste, Timestamp fplreaEfecto,
			Integer norden) {

		List<PagosPlanificados> pagosPlanificados = null;
		Set set = null;

		set = pagosPlanificadosDao.getValuesLocas(cgarantia, kpoliza, ksubpol, kajuste, kcerti, kgrsus,
				cpresta, kpresta, cprestaFict, fplreaEfecto,
				norden);
		if (set != null) {

			pagosPlanificados = new ArrayList<PagosPlanificados>();

			Iterator iter = set.iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				pagosPlanificados.add((PagosPlanificados) entry.getValue());
			}
		}

		return pagosPlanificados;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.mapfre.solvencia.servicios.IObtenerDatos#recuperarPeriodos(java.sql.
	 * Timestamp, java.lang.String,
	 * es.mapfre.solvencia.coherence.keys.maestro.UmicKey)
	 */
	@Override
	public List<Periodo> recuperarPeriodos(Timestamp fecCierre, String bti, UmicKey key) {

		List<Periodo> periodos = new ArrayList<Periodo>();

		periodos = periodoDao.recuperarPeriodosBTI(fecCierre, bti, key.getCtipoaport(), key.getKajuste(),
				key.getKcertificado(), key.getKgarantia(), key.getKmodalidad(), key.getKpoliza(), key.getKprestacion(),
				key.getKsubpoliza(), key.getNorden(), key.getNsuscri());

		return periodos;
	}

	@Override
	public List<Periodo> recuperarPeriodosBTI(Timestamp fecCierre, UmicKey key) {

		List<Periodo> periodos = new ArrayList<Periodo>();

		periodos = periodoDao.recuperarPeriodosBTI(fecCierre, ConstantesSolvencia.BASE_BTI, key.getCtipoaport(),
				key.getKajuste(), key.getKcertificado(), key.getKgarantia(), key.getKmodalidad(), key.getKpoliza(),
				key.getKprestacion(), key.getKsubpoliza(), key.getNorden(), key.getNsuscri());

		return periodos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.mapfre.solvencia.servicios.IObtenerDatos#recuperarProyBTI(java.sql.
	 * Timestamp, es.mapfre.solvencia.coherence.keys.maestro.UmicKey)
	 */
	@Override
	public List<DetalleCorriente> recuperarProyBTI(Timestamp fcierre, UmicKey umicKey) {
		// Como se están recuperando datos de la misma UMIC, o de UMIC relacionadas,
		// podemos forzar a que filtre por las particiones del miembro del cluster.
		return detallesDao.getMemberValues(ConstantesSolvencia.BASE_BTI, fcierre, umicKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.mapfre.solvencia.servicios.IObtenerDatos#recuperarProyeccion(java.lang.
	 * String, java.sql.Timestamp,
	 * es.mapfre.solvencia.coherence.keys.maestro.UmicKey)
	 */
	@Override
	public List<DetalleCorriente> recuperarProyeccion(String ktipobt, Timestamp fcierre, UmicKey umicKey) {
		// Como se están recuperando datos de la misma UMIC, o de UMIC relacionadas,
		// podemos forzar a que filtre por las particiones del miembro del cluster.
		return detallesDao.getMemberValues(ktipobt, fcierre, umicKey);
	}
	
	@Override
	public List<DetalleCorriente> recuperarProyeccionCualquierNodo(String ktipobt, Timestamp fcierre, UmicKey umicKey) {
		// Como se están recuperando datos de la misma UMIC, o de UMIC relacionadas,
		// podemos forzar a que filtre por las particiones del miembro del cluster.
		return detallesDao.getValues(ktipobt, fcierre, umicKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.mapfre.solvencia.servicios.IObtenerDatos#recuperarPlanPagos(es.mapfre.
	 * solvencia.coherence.keys.maestro.UmicKey)
	 */
	@Override
	public List<PlanPagos> recuperarPlanPagos(UmicKey key) {
		return planPagosDao.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.mapfre.solvencia.servicios.IObtenerDatos#recuperarUmicIncidente(es.mapfre.
	 * solvencia.coherence.keys.maestro.UmicKey, java.sql.Timestamp,
	 * java.lang.String)
	 */
	@Override
	public Boolean recuperarUmicIncidente(UmicKey claveUmic, Timestamp fecCierre, String baseTecnica) {
		return incidenciaDao.isUmicErronea(claveUmic, fecCierre, baseTecnica);
	}

	@Override
	public DetalleBaseTecnica recuperarBTCUmic(Timestamp fecCierre, String baseTec, UmicKey umicKey) {
		return detalleBaseTecnicaDao.getValues(baseTec, fecCierre, umicKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.mapfre.solvencia.servicios.IObtenerDatos#recuperarUmicPrincipal(java.lang.
	 * Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 * java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public UmicKey recuperarUmicPrincipal(Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer kcertificado,
			Integer nsuscri, String ctipoaport) {

//		List<Filter> filtros = new ArrayList<Filter>();

//		Filter allFilter = new AndFilter(getFilterUmicRelacionadas(kmodalidad, kpoliza, ksubpoliza, kcertificado, nsuscri, ctipoaport), new EqualsFilter("getSpcom", "P"));
		Filter allFilter = datosGeneralesDao.getFilterUmicRelacionadas(kmodalidad, kpoliza, ksubpoliza, kcertificado,
				nsuscri, ctipoaport, "P");

		Set<UmicKey> listaUmicKey = datosGeneralesDao.keySet(allFilter);

		if (listaUmicKey.size() > 1) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(E01, new String[] { ConstantesSolvencia.UMIC_PRINCIPAL });
		} else if (listaUmicKey == null || listaUmicKey.isEmpty()) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(AM, new String[] { kmodalidad.toString(), kpoliza.toString(),
					ksubpoliza.toString(), kcertificado.toString(), nsuscri.toString() });
		}

		return listaUmicKey.iterator().next();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.mapfre.solvencia.servicios.IObtenerDatos#recuperarUmicSecundarias(java.
	 * lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 * java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Set<UmicKey> recuperarUmicSecundarias(Integer kmodalidad, Long kpoliza, Integer ksubpoliza,
			Integer kcertificado, Integer nsuscri, String ctipoaport) {
		// Buscamos las secundarias, que no las complementarias (por eso filtramos por
		// "S")
//		Filter filtroUmic = new AndFilter(getFilterUmicRelacionadas(kmodalidad, kpoliza, ksubpoliza, kcertificado, nsuscri, ctipoaport), new EqualsFilter("getSpcom", "S"));
		Filter filtroUmic = datosGeneralesDao.getFilterUmicRelacionadas(kmodalidad, kpoliza, ksubpoliza, kcertificado,
				nsuscri, ctipoaport, "S");

		// TODO Filtrar particionado
		// Filter filtroUmicPartition = new PartitionedFilter(filtroUmic, partitions);

		return datosGeneralesDao.keySet(filtroUmic);
	}

	/**
	 * Devuelve un filtro para las umic relacionadas con los datos de entrada
	 * 
	 * @param kmodalidad
	 * @param kpoliza
	 * @param ksubpoliza
	 * @param kcertificado
	 * @param nsuscri
	 * @return
	 */
	private Filter getFilterUmicRelacionadas(Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer kcertificado,
			Integer nsuscri, String ctipoaport) {

//		Filter kmodalidadFilter = new EqualsFilter("getKmodalidad", kmodalidad);
//		Filter kpolizaFilter = new EqualsFilter("getKpoliza", kpoliza);
//		Filter ksubpolizaFilter = new EqualsFilter("getKsubpoliza", ksubpoliza);
//		Filter kcertificadoFilter = new EqualsFilter("getKcertificado", kcertificado);
//		Filter nsuscriFilter = new EqualsFilter("getNsuscri", nsuscri);
//		
//		Filter[] filtrosArray = new Filter[]{kmodalidadFilter, kpolizaFilter, ksubpolizaFilter, kcertificadoFilter, nsuscriFilter, ctipoaportFilter};

		List<Filter> filtros = new ArrayList<Filter>();

		// Se construye una lista con los parametros de búsqueda (distintos a null)
		List<Object> parametros = new ArrayList<Object>();

		if (kmodalidad != null) {
			parametros.add(kmodalidad);

			if (kpoliza != null) {
				parametros.add(kpoliza);

				if (ksubpoliza != null) {
					parametros.add(ksubpoliza);

					if (kcertificado != null) {
						parametros.add(kcertificado);

						if (nsuscri != null) {
							parametros.add(nsuscri);

							if (ctipoaport != null) {
								parametros.add(ctipoaport);
							}
						}
					}
				}
			}
		}

		// Se construye la lista de filtros para la búsqueda
		for (Object parameter : parametros) {
			if (parameter != null) {
				filtros.add(getValueExtratorByIndex(filtros.size(), parameter));
			}
		}

		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);

		return new AllFilter(arrayFiltros);

	}

	private EqualsFilter getValueExtratorByIndex(int index, Object parameter) {

		switch (index) {
		case 0:
			return new EqualsFilter("getKmodalidad", parameter);
		case 1:
			return new EqualsFilter("getKpoliza", parameter);
		case 2:
			return new EqualsFilter("getKsubpoliza", parameter);
		case 3:
			return new EqualsFilter("getKcertificado", parameter);
		case 4:
			return new EqualsFilter("getNsuscri", parameter);
		case 5:
			return new EqualsFilter("getCtipoaport", parameter);
		default:
			return null;
		}

	}

	@Override
	public TerminosPMCUmic recuperarTerminosPMCUmic(UmicKey claveUmic, String baseTec, Integer iteracion) {
		return terminosPMCUmicDao.getValues(claveUmic, baseTec, iteracion);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.mapfre.solvencia.servicios.IObtenerDatos#recuperarUmic310(java.lang.
	 * Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 * java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Umic recuperarUmic310(Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer kcertificado,
			Integer nsuscri, String ctipoaport) {

		UmicKey umicKey;

//		Filter filtroUmic = new AndFilter(getFilterUmicRelacionadas(kmodalidad, kpoliza, ksubpoliza, kcertificado, nsuscri, ctipoaport), new EqualsFilter("getKgarantia", 310));
		Filter filtroUmic = datosGeneralesDao.getFilterRecuperarUmic310(kmodalidad, kpoliza, ksubpoliza, kcertificado,
				nsuscri, ctipoaport, 310);

		Set<UmicKey> keys = datosGeneralesDao.keySet(filtroUmic);

		if (keys.size() > 1) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(E01, new String[] { ConstantesSolvencia.UMIC_PRINCIPAL });
		} else if (keys == null || keys.isEmpty()) {
			return null;
		}

		umicKey = keys.iterator().next();

		return umicDao.getUmic(umicKey);

	}

	@Override
	public Umic recuperarUmic(UmicKey umicKey) {
		return umicDao.getUmic(umicKey);
	}

	@Override
	public BigDecimal recuperarSaldoTotal(Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer kcertificado,
			Integer nsuscri) {

		List<Filter> filtros = new ArrayList<Filter>();

//		Filter allFilter = getFilterUmicRelacionadas(kmodalidad, kpoliza, ksubpoliza, kcertificado, nsuscri, null);
		Filter allFilter = datosGeneralesDao.getFilterRecuperarSaldoTotal(kmodalidad, kpoliza, ksubpoliza, kcertificado,
				nsuscri);

		Set<UmicKey> listaUmicKey = datosGeneralesDao.keySet(allFilter);

		NamedCache capitales = CacheFactory.getCache(CACHE_CAPITALES);

		return (BigDecimal) capitales.aggregate(listaUmicKey, new BigDecimalSum(CACHE_CAPITALES_GETISALDO));

	}

	@SuppressWarnings("unchecked")
	@Override
	public Umic recuperarUmicTitular(Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer kcertificado,
			Integer nsuscri, String kbencon, Timestamp feciniciorenta, Integer forpagrent, String cpagrenta) {
		UmicKey umicKey;

		Filter filtroUmic = datosGeneralesDao.getFilterRecuperarUmicTitular(kmodalidad, kpoliza, ksubpoliza,
				kcertificado, nsuscri, kbencon);

		Set<UmicKey> keys = datosGeneralesDao.keySet(filtroUmic);

		if (keys.size() > 1) {

			Iterator<UmicKey> it = keys.iterator();

			List<Umic> umics = new ArrayList<Umic>();

			while (it.hasNext()) {

				umics.add(umicDao.getUmic(it.next()));

			}

			int contador = 0;
			while (contador < umics.size()) {

				if (feciniciorenta.equals(umics.get(contador).getRentas().getFecIni())
						&& forpagrent == umics.get(contador).getRentas().getForpagrent()
						&& cpagrenta.equals(umics.get(contador).getRentas().getCpagrenta())) {
					return umics.get(contador);
				}
				contador++;
			}

			// filtroUmic =
			// datosGeneralesDao.getFilterRecuperarUmicTitularFechaInicioRenta(kmodalidad,
			// kpoliza, ksubpoliza, kcertificado, nsuscri, kbencon, feciniciorenta,
			// forpagrent, cpagrenta);
			// keys = datosGeneralesDao.keySet(filtroUmic);
		} else if (keys == null || keys.isEmpty()) {
			return null;
		}

		umicKey = keys.iterator().next();

		return umicDao.getUmic(umicKey);
	}

	@Override
	public List<Umic> recuperarTodasUmicsTitularSuscripcion(Integer kmodalidad, Long kpoliza, Integer ksubpoliza,
			Integer kcertificado, Integer nsuscri, String kbencon) {
		// UmicKey umicKey;

		Filter filtroUmic = datosGeneralesDao.getFilterRecuperarUmicTitular(kmodalidad, kpoliza, ksubpoliza,
				kcertificado, nsuscri, kbencon);

		Set<UmicKey> keys = datosGeneralesDao.keySet(filtroUmic);

		List<Umic> umics = new ArrayList<Umic>();

		if (keys == null || keys.isEmpty()) {
			return null;
		}

		Iterator<UmicKey> it = keys.iterator();

		while (it.hasNext()) {

			umics.add(umicDao.getUmic(it.next()));

		}

		return umics;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Umic recuperarUmicMensual(Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer kcertificado,
			Integer nsuscri, Integer kgarantia, Integer kajuste, String ctipoaport, String kbencon, String cpagrenta) {
		UmicKey umicKey = null;

		Filter filtroUmic = datosGeneralesDao.getFilterRecuperarUmicMensual(kmodalidad, kpoliza, ksubpoliza,
				kcertificado, nsuscri, kgarantia, kajuste, ctipoaport, kbencon);

		Set<UmicKey> keys = umicDao.keySet(filtroUmic);

		if (keys.size() > 1) {

			Iterator<UmicKey> it = keys.iterator();

			List<Umic> umics = new ArrayList<Umic>();

			while (it.hasNext()) {

				umics.add(umicDao.getUmic(it.next()));

			}
			int claves = 0;
			int contador = 0;
			while (contador < umics.size() && claves < 2) {

				if (cpagrenta.equals(umics.get(contador).getRentas().getCpagrenta())) {
					umicKey = umics.get(contador).getKey();
					claves++;
				}
				contador++;
			}
			if (claves > 1) {

				// Error existe mas de una renta anual para el asegurado
				throw Solvencia2ExcepcionHelper.crearExcepcion(GF, new String[] {});

			}

		}
		if (keys == null || keys.isEmpty()) {
			// Error no se ha encontrado renta anual para el asegurado
			throw Solvencia2ExcepcionHelper.crearExcepcion(GE, new String[] {});
		}

		// return umicDao.getUmic(umicKey);
		return umicDao.getUmic(umicKey);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer recuperarUmicRelacionadas(UmicKey umicKey, Integer nOrden) {
		int contKey = 1;
		Filter filtroUmic = datosGeneralesDao.getFilterRecuperarUmicRelacionadas(umicKey.getKmodalidad(),
				umicKey.getKpoliza(), umicKey.getKsubpoliza(), umicKey.getKcertificado(), umicKey.getNsuscri(),
				umicKey.getKgarantia(), umicKey.getKprestacion());

		Set<UmicKey> keys = datosGeneralesDao.keySet(filtroUmic);

		List<UmicKey> keysOrdenadas = new ArrayList<UmicKey>();
		Iterator<UmicKey> it = keys.iterator();

		while (it.hasNext()) {
			keysOrdenadas.add(it.next());
		}

		Collections.sort(keysOrdenadas);

		if (keys == null || keys.isEmpty()) {
			return null;
		}

		TreeMap<Integer, Integer> nOrdenKey = new TreeMap<Integer, Integer>();

		for (UmicKey key : keysOrdenadas) {
			if (!nOrdenKey.containsKey(key.getNorden())) {
				nOrdenKey.put(key.getNorden(), contKey);
				contKey++;
			}
		}

		return nOrdenKey.get(nOrden);
	}

	@Override
	public List<DetalleCorriente> recuperarProyBTIPROY(Timestamp fcierre, UmicKey umicKey) {
		// Como se están recuperando datos de la misma UMIC, o de UMIC relacionadas,
		// podemos forzar a que filtre por las particiones del miembro del cluster.
		return detallesDao.getMemberValues(ConstantesSolvencia.BASE_BTIPROY, fcierre, umicKey);
	}
	
	@Override
	public Umic recuperarUmicMensualBNC(Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer kcertificado,
			Integer nsuscri, Integer kgarantia, Integer kajuste, String ctipoaport, String kbencon, String kpresta) {
		UmicKey umicKey = null;

		Filter filtroUmic = datosGeneralesDao.getFilterRecuperarUmicMensualBNC(kmodalidad, kpoliza, ksubpoliza,
				kcertificado, nsuscri, kgarantia, kajuste, ctipoaport, kbencon, kpresta);

		Set<UmicKey> keys = umicDao.keySet(filtroUmic);
		//umicKey = umicDao.keySet(filtroUmic);
		
		if (keys.size() == 1) {
			
			Iterator<UmicKey> it = keys.iterator();

			List<Umic> umics = new ArrayList<Umic>();

			while (it.hasNext()) {

				umics.add(umicDao.getUmic(it.next()));

			}
			int claves = 0;
			int contador = 0;
			while (contador < umics.size() && claves < 2) {

				if (kpresta.equals(umics.get(contador).getDatosGenerales().getKprestacion())) {
					umicKey = umics.get(contador).getKey();
					claves++;
				}
				contador++;
			}
			if (claves > 1) {

				// Error existe mas de una renta anual para el asegurado
				throw Solvencia2ExcepcionHelper.crearExcepcion(GF, new String[] {});

			}

		}
		
		if (keys == null || keys.isEmpty()) {
			// Error no se ha encontrado renta anual para el asegurado
			throw Solvencia2ExcepcionHelper.crearExcepcion(GE, new String[] {});
		}

		// return umicDao.getUmic(umicKey);
		return umicDao.getUmic(umicKey);
	}
	
	@Override
    public List<Umic> recuperarTodasUmicsUniv(Integer kmodalidad, Long kpoliza, Integer ksubpoliza) {
        // UmicKey umicKey;

        Filter filtroUmic = datosGeneralesDao.getFilterRecuperarUmicUniv(kmodalidad, kpoliza, ksubpoliza);

        Set<UmicKey> keys = datosGeneralesDao.keySet(filtroUmic);

        List<Umic> umics = new ArrayList<Umic>();

        if (keys == null || keys.isEmpty()) {
            return null;
        }

        Iterator<UmicKey> it = keys.iterator();

        while (it.hasNext()) {

            umics.add(umicDao.getUmic(it.next()));

        }

        return umics;
    }
	
	public UmicKey recuperarUmicPrincipalPMRR02(Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer kcertificado,
			Integer nsuscri, String ctipoaport, String kprestacion, Integer norden) {

		Filter allFilter = datosGeneralesDao.getFilterUmicRelacionadas(kmodalidad, kpoliza, ksubpoliza, kcertificado,
				nsuscri, ctipoaport, "P");

		Set<UmicKey> listaUmicKey = datosGeneralesDao.keySet(allFilter);
		
		List<UmicKey> umics = new ArrayList<UmicKey>();
		
		if (listaUmicKey.size() > 1) {
			Iterator<UmicKey> it = listaUmicKey.iterator();

			while (it.hasNext()) {
				umics.add(it.next());
			}
			
			for (UmicKey key : umics) {
				if (kprestacion.equals(key.getKprestacion()) 
						&& norden.equals(key.getNorden())){
					return key;
				}
			}
		}

		return listaUmicKey.iterator().next();
	}

}