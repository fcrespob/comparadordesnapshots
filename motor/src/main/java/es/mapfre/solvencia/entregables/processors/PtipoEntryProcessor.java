package es.mapfre.solvencia.entregables.processors;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;
import com.tangosol.io.pof.reflect.SimplePofPath;
import com.tangosol.net.BackingMapContext;
import com.tangosol.net.BackingMapManagerContext;
import com.tangosol.net.GuardSupport;
import com.tangosol.net.Guardian;
import com.tangosol.net.cache.BackingMapBinaryEntry;
import com.tangosol.util.BinaryEntry;
import com.tangosol.util.Converter;
import com.tangosol.util.Filter;
import com.tangosol.util.InvocableMap;
import com.tangosol.util.InvocableMap.Entry;
import com.tangosol.util.InvocableMap.EntryProcessor;
import com.tangosol.util.LiteMap;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.AlwaysFilter;
import com.tangosol.util.filter.EqualsFilter;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.InfoPtipoKey;
import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.PolizasTipoKey;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.InfoPtipo;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.PolizasTipo;

@Portable
public class PtipoEntryProcessor implements EntryProcessor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8358768716794997143L;

	private static final int IND_VUELTA = 0;
	private static final int IND_KEYSET = 1;

	@PortableProperty(IND_VUELTA)
	private Integer vuelta;
	@PortableProperty(IND_KEYSET)
	private Map<InfoPtipoKey, InfoPtipo> mapaInfoPtipo;

	public PtipoEntryProcessor() {
	}

	public PtipoEntryProcessor(Integer vuelta, Map<InfoPtipoKey, InfoPtipo> mapaInfoPtipo) {
		this.vuelta = vuelta;
		this.mapaInfoPtipo = mapaInfoPtipo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object process(Entry entry) {
		BinaryEntry binaryEntry = (BinaryEntry) entry;

		UmicKey umicKey = (UmicKey) entry.getKey();

		// Context Caché PolizasTipo
		BackingMapContext ptipoBackingMapContext = binaryEntry.getContext()
				.getBackingMapContext(ConstantesSolvencia.CACHE_PTIPO);
		Map ptipoMap = ptipoBackingMapContext.getBackingMap();
		BackingMapManagerContext contextPtipo = ptipoBackingMapContext.getManagerContext();

		// Context Caché InfoPtipo
		BackingMapContext infoPtipoBackingMapContext = binaryEntry.getContext()
				.getBackingMapContext(ConstantesSolvencia.CACHE_INFOPTIPO);
		Map infoPtipoMap = infoPtipoBackingMapContext.getBackingMap();
		BackingMapManagerContext contextInfoPtipo = infoPtipoBackingMapContext.getManagerContext();

		// Se recupera la entrada de InfoPtipo que tenga la modalidad, garantía
		// y prestación de la entidad procesada
		InfoPtipo info = recuperarInfoPtipo(umicKey);

		// En la vuelta 0 se dejan pasar de aquí todas las combinaciones de
		// modalidad/garantía/prestación sin representación en el ptipo
		// En vueltas sucesivas aquellos que no en los que el número de umics no
		// haya alcanzado el total
//		if (info.cabenMas(this.vuelta > 0)) {
		if (true) {

			if (

			// Si la vuelta es la primera, se dejan pasar aquellas umics
			// para las cuales no se repitan ni la combinación
			// modalidad/garantía/certificado ni la combinación
			// póliza/subpóliza/suscripción:

			(this.vuelta == 0 && (noExisteClaveModGarPrest(binaryEntry, umicKey)
					&& noExisteClavePolSubPolSusc(binaryEntry, umicKey)))

					// En la segunda vuelta se dejarán pasar todas las
					// umics cuya combinación
					// modalidad/garantía/certificado no esté
					// representada, aunque repitan combinación
					// póliza/subpóliza/suscripción:

					|| (this.vuelta == 1 && noExisteClaveModGarPrest(binaryEntry, umicKey))

					// En la tercera vuelta se dejarán pasar todas las umics en
					// las cuales no se haya alcanzado el número suficiente
					// fijado por el porcentaje mínimo, independientemente de
					// que repitan las combinaciones
					// modalidad/garantía/certificado o
					// póliza/subpóliza/suscripción

					|| (this.vuelta > 1))

			{

				PolizasTipo ptipo = new PolizasTipo();
				ptipo.setClaveUmic(umicKey);

				if (this.vuelta > 0 && existePtipo(contextPtipo, ptipoMap, ptipo.getKey())) {
					return Boolean.FALSE;
				}

				DatosGenerales datos = (DatosGenerales) entry.getValue();
				ptipo.setCcanal(datos.getCcanal());
				ptipo.setCcartera(datos.getCcartera());
				ptipo.setCnegocio(datos.getCnegocio());
				ptipo.setOrigen(ConstantesSolvencia.ORIGEN_AUTOMATICO);
				almacenarEnMapa(contextPtipo, ptipoMap, ptipo);

				info.addNumUmics();
				// almacenarEnMapa(contextInfoPtipo, infoPtipoMap, info);

				return Boolean.TRUE;
			}

		}

		return Boolean.FALSE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tangosol.util.InvocableMap.EntryProcessor#processAll(java.util.Set)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map processAll(Set setEntries) {
		Map mapResults = new LiteMap();
		Guardian.GuardContext ctxGuard = GuardSupport.getThreadContext();
		long cMillis = ctxGuard == null ? 0L : ctxGuard.getTimeoutMillis();

		for (Iterator iter = setEntries.iterator(); iter.hasNext();) {
			InvocableMap.Entry entry = (InvocableMap.Entry) iter.next();
			Object result = process(entry);
			if (Boolean.TRUE.equals(result)) {
				// Devolvemos TRUE si se ha añadido un PTIPO nuevo
				mapResults.put(entry.getKey(), Boolean.TRUE);
			}

			if (ctxGuard != null) {
				ctxGuard.heartbeat(cMillis);
			}
		}

		return mapResults;
	}

	private boolean existePtipo(BackingMapManagerContext context, Map ptipoMap, PolizasTipoKey polizasTipoKey) {
		Converter keyConverter = context.getKeyToInternalConverter();
		return ptipoMap.containsKey(keyConverter.convert(polizasTipoKey));
	}

	@SuppressWarnings("rawtypes")
	private boolean noExisteClaveModGarPrest(BinaryEntry binaryEntry, UmicKey umicKey) {

		Filter origenFilter = new EqualsFilter(new PofExtractor(String.class, PolizasTipo.IND_ORIGEN), "AU");
		Filter modalidadFilter = new EqualsFilter(
				new PofExtractor(Integer.class,
						new SimplePofPath(new int[] { PolizasTipo.IND_CLAVEUMIC, DatosGenerales.IND_EKMODALIDAD })),
				umicKey.getKmodalidad());
		Filter garantiaFilter = new EqualsFilter(
				new PofExtractor(Integer.class,
						new SimplePofPath(new int[] { PolizasTipo.IND_CLAVEUMIC, DatosGenerales.IND_EKGARANTIA })),
				umicKey.getKgarantia());
		Filter prestacionFilter = new EqualsFilter(
				new PofExtractor(String.class,
						new SimplePofPath(new int[] { PolizasTipo.IND_CLAVEUMIC, DatosGenerales.IND_KPRESTACION })),
				umicKey.getKprestacion());

		Set keySet = QueryHelper.INSTANCE.keySet(binaryEntry, ConstantesSolvencia.CACHE_PTIPO,
				new AllFilter(new Filter[] { origenFilter, modalidadFilter, garantiaFilter, prestacionFilter}));

		boolean ok = false;
		ok = keySet.size() > 0;

		return keySet.size() == 0;

	}

	@SuppressWarnings("rawtypes")
	private boolean noExisteClavePolSubPolSusc(BinaryEntry binaryEntry, UmicKey umicKey) {

		Filter origenFilter = new EqualsFilter(new PofExtractor(String.class, PolizasTipo.IND_ORIGEN), "AU");
		Filter polizaFilter = new EqualsFilter(new PofExtractor(Long.class, PolizasTipo.IND_KPOLIZA),
				umicKey.getKpoliza());
		Filter subpolizaFilter = new EqualsFilter(new PofExtractor(Integer.class, PolizasTipo.IND_KSUBPOLIZA),
				umicKey.getKsubpoliza());
		Filter nsuscriFilter = new EqualsFilter(new PofExtractor(Integer.class, PolizasTipo.IND_NSUSCRI),
				umicKey.getNsuscri());

		Set keySet = QueryHelper.INSTANCE.keySet(binaryEntry, ConstantesSolvencia.CACHE_PTIPO,
				new AllFilter(new Filter[] { origenFilter, polizaFilter, subpolizaFilter, nsuscriFilter }));

		return keySet.size() == 0;

	}

	@SuppressWarnings("rawtypes")
	private InfoPtipo recuperarInfoPtipo(UmicKey umicKey) {
		InfoPtipo info = null;

		InfoPtipoKey key = new InfoPtipoKey(umicKey.getKmodalidad(), umicKey.getKgarantia(), umicKey.getKprestacion());

		info = this.mapaInfoPtipo.get(key);

		if (info == null) {
			System.out.println("NO ENCUENTRA INFOPTIPO");
		}

		return info;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void almacenarEnMapa(BackingMapManagerContext context, Map map, Object o) {

		Converter keyConverter = context.getKeyToInternalConverter();
		Converter valueConverter = context.getValueToInternalConverter();

		if (o instanceof InfoPtipo) {
			InfoPtipo info = (InfoPtipo) o;
			map.put(keyConverter.convert(info.getKey()), valueConverter.convert(info));
		} else if (o instanceof PolizasTipo) {
			PolizasTipo ptipo = (PolizasTipo) o;
			map.put(keyConverter.convert(ptipo.getKey()), valueConverter.convert(ptipo));
		}

	}
}
