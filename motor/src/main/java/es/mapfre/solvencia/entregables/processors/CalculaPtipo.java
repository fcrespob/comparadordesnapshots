/* TAR00352125-No deben eliminarse registros del PTIPO manual   */
package es.mapfre.solvencia.entregables.processors;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.DistributedCacheService;
import com.tangosol.net.Member;
import com.tangosol.net.NamedCache;
import com.tangosol.net.partition.PartitionSet;
import com.tangosol.util.Filter;
import com.tangosol.util.InvocableMap.EntryAggregator;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.aggregator.Count;
import com.tangosol.util.aggregator.DistinctValues;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.filter.AlwaysFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.LimitFilter;
import com.tangosol.util.filter.PartitionedFilter;
import com.tangosol.util.processor.NumberIncrementor;

import es.mapfre.solvencia.coherence.agent.FreeMemAgent;
import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.InfoPtipoKey;
import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.PolizasTipoKey;
import es.mapfre.solvencia.dao.impl.maestro.DatosGeneralesDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.InfoPtipoDao;
import es.mapfre.solvencia.dao.services.FactoriaDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.InfoPtipo;

public class CalculaPtipo {
	private static final Logger log = LoggerFactory.getLogger(CalculaPtipo.class);

	@SuppressWarnings("rawtypes")
	public static void calcular(final Integer constante) {
		// Limpiamos el heap
		FreeMemAgent.freeMem();

		NamedCache datosGenerales = CacheFactory.getCache(ConstantesSolvencia.CACHE_DATOS_GENERALES);
		NamedCache ptipo = CacheFactory.getCache(ConstantesSolvencia.CACHE_PTIPO);
		// DistributedCacheService service = (DistributedCacheService)
		// datosGenerales.getCacheService();

		log.info("Número de PTIPO antes de proceso automático: {}", ptipo.size());

		// Se obtiene la información agregada: número de umics por
		// modalidad-garantía-prestación
		DatosGeneralesDao datosGeneralesDao = (DatosGeneralesDao) FactoriaDao
				.getDao(ConstantesSolvencia.CACHE_DATOS_GENERALES);
		Map<List<Object>, Integer> infoPtipos = datosGeneralesDao.agregar();

		// Se almacena la información obtenida en la caché correspondiente
		InfoPtipoDao infoPtipoDao = (InfoPtipoDao) FactoriaDao.getDao(ConstantesSolvencia.CACHE_INFOPTIPO);
		infoPtipoDao.putInfoPtipo(infoPtipos, constante);

		// int totalUmic = datosGenerales.size();
		// int totalPtipo = (int) Math.ceil(((double) totalUmic) / ((double)
		// constante));

		// System.out.println("Total: " + totalUmic + " --> " + totalPtipo);

		ValueExtractor modalidadExtractor = new PofExtractor(Integer.class, DatosGenerales.IND_EKMODALIDAD);

		EntryAggregator distinctValuesAggregator = new DistinctValues(modalidadExtractor);
		Set distinctValues = (Set) datosGenerales.aggregate(AlwaysFilter.INSTANCE, distinctValuesAggregator);

		Iterator iter = distinctValues.iterator();
		List key = null;
		while (iter.hasNext()) {
			Object modalidad;
			Object next = iter.next();
			if (next instanceof List) {
				key = (List) next;

				modalidad = key.get(0);
			} else if (next instanceof Object) {
				modalidad = next;
			} else {
				modalidad = null;
				// TODO: excepción
			}

			// Se filtra por modalidad
			Filter filter = new EqualsFilter(new PofExtractor(Integer.class, DatosGenerales.IND_EKMODALIDAD),
					modalidad);

			int umicPorModalidad = (int) datosGenerales.aggregate(filter, new Count());
			int numPtipoPorModalidad = Math.max((int) Math.ceil(((double) umicPorModalidad) / ((double) constante)), infoPtipoDao.getModSize((Integer)modalidad));

			//int numPtipoPorModalidad = (int) Math.ceil(((double) umicPorModalidad) / ((double) constante));
			
			log.info("PTIPO objetivo para {}: {} UMIC --> {}.", modalidad, umicPorModalidad, numPtipoPorModalidad);

			int contador = 0;
			int vuelta = 0;
			int numPage = 0;
			int maxNumPage = Math.min(umicPorModalidad, constante);
			int maxNumVueltas = 3;
			while (contador < numPtipoPorModalidad && vuelta < maxNumVueltas) {
				log.info("Número de vuelta para moda {}: {}", modalidad, vuelta);
				int numPageVistas = 0;
				while (contador < numPtipoPorModalidad && numPageVistas < maxNumPage) {
					numPage = (vuelta == 0 && numPageVistas == 0) ? ThreadLocalRandom.current().nextInt(0, maxNumPage)
							: (numPage + 1) % maxNumPage;
					log.debug("Número de página para moda {}: {}", modalidad, numPage);

					int tamDatos = umicPorModalidad;// (int)
													// datosGenerales.aggregate(filter,
													// new Count());
					if (tamDatos > 0) {
						int numPtipo = numPtipoPorModalidad;// (int)
															// Math.ceil(((double)
															// tamDatos) /
															// ((double)
															// constante));

						LimitFilter limitFilter = new LimitFilter(filter, numPtipo);
						limitFilter.setPage(numPage);

						Collection keySet = datosGenerales.keySet(limitFilter);

						Map<InfoPtipoKey, InfoPtipo> infoPtipoMap = infoPtipoDao.getMap();
						Map results = datosGenerales.invokeAll(keySet, new PtipoEntryProcessor(vuelta, infoPtipoMap));
						// System.out.println(modalidad + ": " +
						// results.size());

						// Los resultados tienen un TRUE para aquellas UMIC que
						// han generado PTIPO (y nada para el resto)
						if (results != null && !results.isEmpty()) {
							Map<InfoPtipoKey, Integer> numIncrements = new HashMap<InfoPtipoKey, Integer>();
							for (Object umicKey : results.keySet()) {
								if (umicKey instanceof UmicKey) {
									InfoPtipo infoPtipo = recuperarInfoPtipo((UmicKey) umicKey, infoPtipoMap);
									Integer lastIncrement = numIncrements.get(infoPtipo.getKey());
									if (lastIncrement == null) {
										lastIncrement = 0;
									}
// TAR00352125- Siempre se tienen que conservar el ptipo manual.									
//									if (vuelta == 0 && infoPtipo.getNumUmics() > 0) {
//										ptipo.remove(new PolizasTipoKey((UmicKey) umicKey));
//									} else {
										lastIncrement++;
										numIncrements.put(infoPtipo.getKey(), lastIncrement);
										infoPtipo.setNumUmics(infoPtipo.getNumUmics() + 1);
										contador++;
										log.debug("Llevamos estas umics: " + contador);
//									}
								}
							}
							for (InfoPtipoKey infoPtipoKey : numIncrements.keySet()) {
								Object valor = infoPtipoDao.getCache().invoke(infoPtipoKey, new NumberIncrementor(
										"NumUmics", numIncrements.get(infoPtipoKey), Boolean.FALSE));
								log.info("Num PTIPO de {} : {}", infoPtipoKey, numIncrements.get(infoPtipoKey));
							}
						} else {
							log.debug("La cache ha devuelto 0 PTIPO para modalidad {} en página {}", modalidad,
									numPage);
						}
					} else {
						log.info("La cache no tiene datos de {} en página {}.", modalidad, numPage);
					}

					numPageVistas++;
				}
				// Hemos dado una vuelta a las páginas.
				vuelta++;
				log.info("Finaliza la vuelta: " + vuelta);
			}
		}

		log.info("Número de PTIPO después de proceso automático: {}", ptipo.size());
		StringBuilder sbResumenPtipo = new StringBuilder("Resumen: \n");
		for (InfoPtipo entry : infoPtipoDao.values()) {
			sbResumenPtipo.append(entry).append('\n');
		}
		log.info(sbResumenPtipo.toString());
	}

	private static InfoPtipo recuperarInfoPtipo(UmicKey umicKey, Map<InfoPtipoKey, InfoPtipo> infoPtipoMap) {
		InfoPtipo info = null;

		InfoPtipoKey key = new InfoPtipoKey(umicKey.getKmodalidad(), umicKey.getKgarantia(), umicKey.getKprestacion());

		info = infoPtipoMap.get(key);

		if (info == null) {
			log.warn("No se encuentra InfoPTipo para clave {}", key);
		}

		return info;
	}
}
