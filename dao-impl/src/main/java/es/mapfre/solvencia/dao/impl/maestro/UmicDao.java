package es.mapfre.solvencia.dao.impl.maestro;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.Dao;
import es.mapfre.solvencia.dominio.maestro.Asegurados;
import es.mapfre.solvencia.dominio.maestro.BaseTecnicaInicial;
import es.mapfre.solvencia.dominio.maestro.Capitales;
import es.mapfre.solvencia.dominio.maestro.Comisiones;
import es.mapfre.solvencia.dominio.maestro.DatosAdicionales;
import es.mapfre.solvencia.dominio.maestro.DatosCoaseguro;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Duraciones;
import es.mapfre.solvencia.dominio.maestro.Fechas;
import es.mapfre.solvencia.dominio.maestro.OtrosDatos;
import es.mapfre.solvencia.dominio.maestro.DatosNiif17;
import es.mapfre.solvencia.dominio.maestro.DatosDescuentos;
import es.mapfre.solvencia.dominio.maestro.Primas;
import es.mapfre.solvencia.dominio.maestro.Rentas;
import es.mapfre.solvencia.dominio.maestro.Rescates;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;
import es.mapfre.solvencia.utils.beanio.BeanIOWriter;

public class UmicDao implements Map<UmicKey, Umic>, Dao {
	private static final int BATCH_SIZE = 1000;
	
	private static final String CACHE_NAME = "X880JI01";
	
	private AseguradosDao<Object, Asegurados> aseguradosDao = new AseguradosDao<Object, Asegurados>();
	private BaseTecnicaInicialDao<Object, BaseTecnicaInicial> baseTecnicaInicialDao = new BaseTecnicaInicialDao<Object, BaseTecnicaInicial>();
	private CapitalesDao<Object, Capitales> capitalesDao = new CapitalesDao<Object, Capitales>();
	private ComisionesDao<Object, Comisiones> comisionesDao = new ComisionesDao<Object, Comisiones>();
	private DatosGeneralesDao datosGeneralesDao = new DatosGeneralesDao();
	private DatosCoaseguroDao<Object, DatosCoaseguro> datosCoaseguroDao = new DatosCoaseguroDao<Object, DatosCoaseguro>();
	private DuracionesDao<Object, Duraciones> duracionesDao = new DuracionesDao<Object, Duraciones>();
	private FechasDao<Object, Fechas> fechasDao = new FechasDao<Object, Fechas>();
	private PrimasDao<Object, Primas> primasDao = new PrimasDao<Object, Primas>();
	private RentasDao<Object, Rentas> rentasDao = new RentasDao<Object, Rentas>();
	private RescatesDao<Object, Rescates> rescatesDao = new RescatesDao<Object, Rescates>();
	private DatosAdicionalesDao<Object, DatosAdicionales> datosAdicionalesDao = new DatosAdicionalesDao<Object, DatosAdicionales>();
	private OtrosDatosDao<Object, OtrosDatos> otrosDatosDao = new OtrosDatosDao<Object, OtrosDatos>();
	private DatosNiif17Dao datosNiif17Dao = new DatosNiif17Dao ();										
	private DatosDescuentosDao datosDescuentosDao = new DatosDescuentosDao();

	public NamedCache getCache() {
		return datosGeneralesDao.getCache();
	}
	
	@Override
	public Set<Entry<UmicKey, Umic>> entrySet() {
		throw new RuntimeException("this method is not used by umics");
	}

	@Override
	public Umic get(Object key) {
		if (key instanceof UmicKey) {
			return this.getUmic((UmicKey) key);
		}
		return null;
	}

	public Umic getUmic(UmicKey key) {

		Umic umic = new Umic();

		umic.setAsegurados(this.aseguradosDao.get(key));
		umic.setBti(this.baseTecnicaInicialDao.get(key));
		umic.setCapitales(this.capitalesDao.get(key));
		umic.setComisiones(this.comisionesDao.get(key));
		umic.setDuraciones(this.duracionesDao.get(key));
		umic.setDatosCoaseguro(this.datosCoaseguroDao.get(key));
		umic.setDatosGenerales(this.datosGeneralesDao.get(key));
		umic.setFechas(this.fechasDao.get(key));
		umic.setPrimas(this.primasDao.get(key));
		umic.setRentas(this.rentasDao.get(key));
		umic.setRescates(this.rescatesDao.get(key));
		umic.setDatosAdicionales(this.datosAdicionalesDao.get(key));
		umic.setOtrosDatos(this.otrosDatosDao.get(key));
		umic.setDatosNiif17(this.datosNiif17Dao.get(key));
		umic.setDatosDescuentos(this.datosDescuentosDao.get(key));
		return umic;
	}

	@Override
	public Set<UmicKey> keySet() {
		return (Set<UmicKey>) datosGeneralesDao.keySet();
	}

	public Set<UmicKey> keySet(Filter filter) {
		return (Set<UmicKey>) datosGeneralesDao.keySet(filter);
	}

	@Override
	public Umic put(UmicKey key, Umic umic) {

		this.aseguradosDao.put(key, umic.getAsegurados());
		this.baseTecnicaInicialDao.put(key, umic.getBti());
		this.capitalesDao.put(key, umic.getCapitales());
		this.comisionesDao.put(key, umic.getComisiones());
		this.duracionesDao.put(key, umic.getDuraciones());
		this.datosCoaseguroDao.put(key, umic.getDatosCoaseguro());
		this.datosGeneralesDao.put(key, umic.getDatosGenerales());
		this.fechasDao.put(key, umic.getFechas());
		this.primasDao.put(key, umic.getPrimas());
		this.rentasDao.put(key, umic.getRentas());
		this.rescatesDao.put(key, umic.getRescates());
		this.datosAdicionalesDao.put(key, umic.getDatosAdicionales());
		this.otrosDatosDao.put(key, umic.getOtrosDatos());
		this.datosNiif17Dao.put(key, umic.getDatosNiif17());
		this.datosDescuentosDao.put(key, umic.getDatosDescuentos());
		return umic;
	}

	@Override
	public void putAll(Map<? extends UmicKey, ? extends Umic> umicMap) {
		Map<UmicKey, Asegurados> asegurados = new HashMap<UmicKey, Asegurados>();
		Map<UmicKey, BaseTecnicaInicial> baseTecnicaInicial = new HashMap<UmicKey, BaseTecnicaInicial>();
		Map<UmicKey, Capitales> capitales = new HashMap<UmicKey, Capitales>();
		Map<UmicKey, Comisiones> comisiones = new HashMap<UmicKey, Comisiones>();
		Map<UmicKey, Duraciones> duraciones = new HashMap<UmicKey, Duraciones>();
		Map<UmicKey, DatosCoaseguro> datosCoaseguro = new HashMap<UmicKey, DatosCoaseguro>();
		Map<UmicKey, DatosGenerales> datosGenerales = new HashMap<UmicKey, DatosGenerales>();
		Map<UmicKey, Fechas> fechas = new HashMap<UmicKey, Fechas>();
		Map<UmicKey, Primas> primas = new HashMap<UmicKey, Primas>();
		Map<UmicKey, Rentas> rentas = new HashMap<UmicKey, Rentas>();
		Map<UmicKey, Rescates> rescates = new HashMap<UmicKey, Rescates>();
		Map<UmicKey, DatosAdicionales> datosAdicionales = new HashMap<UmicKey, DatosAdicionales>();
		Map<UmicKey, OtrosDatos> otrosDatos = new HashMap<UmicKey, OtrosDatos>();
		Map<UmicKey, DatosNiif17> datosNiif17 = new HashMap<UmicKey, DatosNiif17>();
		Map<UmicKey, DatosDescuentos> datosDescuentos = new HashMap<UmicKey, DatosDescuentos>();
		for (Entry entry : umicMap.entrySet()) {
			UmicKey key = (UmicKey) entry.getKey();
			Umic umic = (Umic) entry.getValue();
			
			asegurados.put(key, umic.getAsegurados());
			baseTecnicaInicial.put(key, umic.getBti());
			capitales.put(key, umic.getCapitales());
			comisiones.put(key, umic.getComisiones());
			duraciones.put(key, umic.getDuraciones());
			datosCoaseguro.put(key, umic.getDatosCoaseguro());
			datosGenerales.put(key, umic.getDatosGenerales());
			fechas.put(key, umic.getFechas());
			primas.put(key, umic.getPrimas());
			rentas.put(key, umic.getRentas());
			rescates.put(key, umic.getRescates());
			datosAdicionales.put(key, umic.getDatosAdicionales());
			otrosDatos.put(key, umic.getOtrosDatos());
			datosNiif17.put(key, umic.getDatosNiif17());
			datosDescuentos.put(key, umic.getDatosDescuentos());
		}
		
		this.aseguradosDao.putAll(asegurados);
		this.baseTecnicaInicialDao.putAll(baseTecnicaInicial);
		this.capitalesDao.putAll(capitales);
		this.comisionesDao.putAll(comisiones);
		this.duracionesDao.putAll(duraciones);
		this.datosCoaseguroDao.putAll(datosCoaseguro);
		this.datosGeneralesDao.putAll(datosGenerales);
		this.fechasDao.putAll(fechas);
		this.primasDao.putAll(primas);
		this.rentasDao.putAll(rentas);
		this.rescatesDao.putAll(rescates);
		this.datosAdicionalesDao.putAll(datosAdicionales);
		this.otrosDatosDao.putAll(otrosDatos);
		this.datosNiif17Dao.putAll(datosNiif17);
		this.datosDescuentosDao.putAll(datosDescuentos);
	}

	@Override
	public Umic remove(Object key) {
		Umic umic = this.get(key);

		if (umic != null) {
			this.aseguradosDao.remove(key);
			this.baseTecnicaInicialDao.remove(key);
			this.capitalesDao.remove(key);
			this.comisionesDao.remove(key);
			this.duracionesDao.remove(key);
			this.datosCoaseguroDao.remove(key);
			this.datosGeneralesDao.remove(key);
			this.fechasDao.remove(key);
			this.primasDao.remove(key);
			this.rentasDao.remove(key);
			this.rescatesDao.remove(key);
			this.datosAdicionalesDao.remove(key);
			this.otrosDatosDao.remove(key);
			this.datosNiif17Dao.remove(key);
			this.datosDescuentosDao.remove(key);
		}

		return umic;
	}

	@Override
	public Collection<Umic> values() {
		//TODO Implementar el join de las cachés. CFA : método innecesario?
		throw new RuntimeException("this method is not used by umics");
	}

	@Override
	public void clear() {
		this.aseguradosDao.clear();
		this.baseTecnicaInicialDao.clear();
		this.capitalesDao.clear();
		this.comisionesDao.clear();
		this.duracionesDao.clear();
		this.datosCoaseguroDao.clear();
		this.datosGeneralesDao.clear();
		this.fechasDao.clear();
		this.primasDao.clear();
		this.rentasDao.clear();
		this.rescatesDao.clear();
		this.datosAdicionalesDao.clear();
		this.otrosDatosDao.clear();
		this.datosNiif17Dao.clear();
		this.datosDescuentosDao.clear();
	}

	@Override
	public boolean containsKey(Object arg0) {
		return datosGeneralesDao.containsKey(arg0);
	}

	@Override
	public boolean containsValue(Object arg0) {
		// TODO Implementar sobre DatosGenerales. CFA : método innecesario?
		throw new RuntimeException("this method is not used by umics");
	}

	@Override
	public boolean isEmpty() {
		return datosGeneralesDao.isEmpty();
	}

	@Override
	public int size() {
		return datosGeneralesDao.size();
	}

	public void loadCache(BeanIOReader reader) {
		Umic umic = null;
		Map<UmicKey, Umic> umics = new HashMap<UmicKey, Umic>();
		int bloque = 0;
		while ((umic = (Umic) reader.read()) != null) {
			DatosGenerales dg = umic.getDatosGenerales();
			UmicKey key = new UmicKey(dg.getCtipoaport(), dg.getKajuste(),
					dg.getKcertificado(), dg.getKgarantia(),
					dg.getKmodalidad(), dg.getKpoliza(), dg.getKprestacion(),
					dg.getKsubpoliza(), dg.getNorden(), dg.getNsuscri());
			
			umics.put(key, umic);
			bloque++;
			if (bloque % BATCH_SIZE == 0) {
				this.putAll(umics);
				umics.clear();
			}
		}
		if (umics.size() > 0) {
			this.putAll(umics);
			umics.clear();
		}
	}

	@Override
	public void exportCache(BeanIOWriter writer) {
		// No es necesario exportar los datos de la UMIC
		
	}
	
	@Override
	public String getCacheName() {
		return CACHE_NAME;
	}

	@Override
	public String getNombreServicio() {
		return this.getCacheName();
	}

	public void marcarProcesada(UmicKey umicKey) {
		CacheFactory.getCache(CACHE_NAME).put(umicKey, Boolean.TRUE);
	}
	
	public boolean estaProcesada(UmicKey umicKey) {
		return CacheFactory.getCache(CACHE_NAME).containsKey(umicKey);
	}
	
	public void desmarcarProcesadas() {
		CacheFactory.getCache(CACHE_NAME).clear();
	}
}