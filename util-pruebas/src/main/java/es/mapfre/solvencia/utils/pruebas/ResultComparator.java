package es.mapfre.solvencia.utils.pruebas;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tangosol.net.NamedCache;

import es.mapfre.solvencia.dao.Dao;
import es.mapfre.solvencia.dao.services.FactoriaDao;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;
import es.mapfre.solvencia.utils.beanio.BeanIOWriter;

public class ResultComparator {
	private static final Logger log = LoggerFactory.getLogger(ResultComparator.class);

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config-out.xml";

	public static void main(String[] args) {
		String tipoFichero = null;
		String ficheroMapfre = null;
		String ficheroIndra = null;

		if (args != null && args.length == 3) {
			tipoFichero = args[0];
			ficheroMapfre = args[1];
			ficheroIndra = args[2];

			BeanIOReader readerMapfre = null;
			BeanIOReader readerIndra = null;
			try {
				readerMapfre = new BeanIOReader(BEANIO_CONFIG_XML, ficheroMapfre, tipoFichero);
			} catch (IOException e) {
				log.error("Cargando fichero {}", ficheroMapfre);
				System.exit(-1);
			}

			try {
				readerIndra = new BeanIOReader(BEANIO_CONFIG_XML, ficheroIndra, tipoFichero);
			} catch (IOException e) {
				log.error("Cargando fichero {}", ficheroIndra);
				System.exit(-1);
			}

			Dao daoMapfre = FactoriaDao.getDao(tipoFichero);
			daoMapfre.loadCache(readerMapfre);

			NamedCache cacheMapfre = daoMapfre.getCache();

			try {
				BeanIOWriter writerCoinciden = new BeanIOWriter(BEANIO_CONFIG_XML, ficheroIndra + "-coinciden.txt",
						tipoFichero);
				BeanIOWriter writerNoCoinciden = new BeanIOWriter(BEANIO_CONFIG_XML,
						ficheroIndra + "-no-coinciden.txt", tipoFichero);
				BeanIOWriter writerIndra = new BeanIOWriter(BEANIO_CONFIG_XML, ficheroIndra + "-ausentes.txt",
						tipoFichero);
				EntidadBase<Object> objetoIndra = null;
				while ((objetoIndra = (EntidadBase<Object>) readerIndra.read()) != null) {
					if (cacheMapfre.containsKey(objetoIndra.getKey())) {
						// Tenemos el registro de Indra en el de Mapfre
						EntidadBase<Object> objetoMapfre = (EntidadBase<Object>) cacheMapfre.get(objetoIndra.getKey());
						if (objetoIndra.equals(objetoMapfre)) {
							// Coinciden resultados
							writerCoinciden.write(objetoIndra);
						} else {
							// No coinciden resultados
							writerNoCoinciden.write(objetoMapfre);
							writerNoCoinciden.write(objetoIndra);
						}
						// Lo borramos para que no se extraiga luego entre las
						// no encontradas
						cacheMapfre.remove(objetoIndra.getKey());
					} else {
						// No tenemos el registro de Indra en el de Mapfre
						writerIndra.write(objetoIndra);
					}
				}

				// Sacamos todos los de Mapfre que no se han generado en Indra
				BeanIOWriter writerMapfre = null;
				try {
					writerMapfre = new BeanIOWriter(BEANIO_CONFIG_XML, ficheroMapfre + "-ausentes.txt", tipoFichero);
				} catch (IOException e) {

				}
				daoMapfre.exportCache(writerMapfre);

				writerMapfre.close();
				writerIndra.close();
				writerCoinciden.close();
				writerNoCoinciden.close();
			} catch (IOException e) {

			}

		} else {
			log.error("Uso: ResultComparator tipo-fichero ficheroMapfre ficheroIndra");
			log.error("tipo-fichero = {totales-flujos, detalle-corriente, detalle-basetecnicas}");
		}

		log.info("Finalizado");
	}

}
