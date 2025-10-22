/* MODIFICACION:TAR00302248-Contabilidad SolvenciaII en SSAA 
   FECHA: 25/09/2017
   AUTOR: INDRA
 */

package es.mapfre.solvencia.dao.impl.maestro;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tangosol.util.Filter;
import com.tangosol.util.InvocableMap;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.aggregator.Count;
import com.tangosol.util.aggregator.GroupAggregator;
import com.tangosol.util.extractor.MultiExtractor;
import com.tangosol.util.filter.AlwaysFilter;
import com.tangosol.util.filter.EqualsFilter;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.entregables.FlujCoaSeg;
import es.mapfre.solvencia.dominio.entregables.FlujTcas;
import es.mapfre.solvencia.dominio.entregables.ProvCoaSeg;
import es.mapfre.solvencia.dominio.entregables.PrvBt;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
/*INI-TAR00302248*/
import es.mapfre.solvencia.dominio.entregables.Contabilidad;
/*FIN-TAR00302248*/

public class DatosGeneralesDao extends DaoBase implements Map<UmicKey, DatosGenerales>{
	
	private static final String CACHE_NAME = "datos-generales"; 
	
	private ValueExtractor ccanalExtractor;
	private ValueExtractor cnegocioExtractor;
	public ValueExtractor kmodalidadExtractor;
	private ValueExtractor ramoExtractor;
	private ValueExtractor gapActExtractor;
	
	public ValueExtractor kgarantiaExtractor;
	public ValueExtractor kprestacionExtractor;
	
	private ValueExtractor kramoExtractor;
	private ValueExtractor segmento1Extractor;
	private ValueExtractor tiposubriesgoExtractor;
	private ValueExtractor polizaExtractor;
	private ValueExtractor subpolizaExtractor;
	private ValueExtractor suscriExtractor;
	private ValueExtractor carterainvExtractor;
	private ValueExtractor kcertificado;
	private ValueExtractor ctipoaport;
	private ValueExtractor spcom;
	private ValueExtractor kbencon;
	private ValueExtractor kajuste;
	private ValueExtractor kgarantia;
	
	
	public DatosGeneralesDao() {
		super();
		super.setCacheName(CACHE_NAME);
		
		ccanalExtractor = createExtractor("getCcanal",Integer.class, DatosGenerales.IND_CCANAL);
		cnegocioExtractor = createExtractor("getCnegocio",String.class, DatosGenerales.IND_CNEGOCIO);	
		kmodalidadExtractor = createExtractor("getKmodalidad",Integer.class, DatosGenerales.IND_EKMODALIDAD);
		// JBMARTA - PYAM0025 - INI
		// ramoExtractor = createExtractor("getCtipramo",String.class, DatosGenerales.IND_CTIPRAMO);
		ramoExtractor = createExtractor("getKramo",String.class, DatosGenerales.IND_KRAMO);
		// JBMARTA - PYAM0025 - FIN
		gapActExtractor = createExtractor("getGapAct",String.class, DatosGenerales.IND_GAPACT);
		
		kgarantiaExtractor = createExtractor("getKgarantia", Integer.class, DatosGenerales.IND_EKGARANTIA);
		kprestacionExtractor = createExtractor("getKprestacion", String.class, DatosGenerales.IND_KPRESTACION);
		
		kramoExtractor = createExtractor("getKramo",String.class, DatosGenerales.IND_KRAMO);
		segmento1Extractor = createExtractor("getSegmento1",String.class, DatosGenerales.IND_SEGMENTO1);
		tiposubriesgoExtractor =createExtractor("getTipoSubriesgo",String.class, DatosGenerales.IND_TIPOSUBRIESGO);
		polizaExtractor = createExtractor("getKpoliza",Long.class, DatosGenerales.IND_KPOLIZA);
		subpolizaExtractor = createExtractor("getKsubpoliza",Integer.class, DatosGenerales.IND_EKSUBPOLIZA);
		suscriExtractor = createExtractor("getNsuscri",Integer.class, DatosGenerales.IND_ENSUSCRI);
		carterainvExtractor = createExtractor("getKcarterainv",String.class, DatosGenerales.IND_KCARTERAINV);
		
		kcertificado = createExtractor("getKcertificado",Integer.class, DatosGenerales.IND_KCERTIFICADO);
		ctipoaport = createExtractor("getCtipoaport",String.class, DatosGenerales.IND_CTIPOAPORT);
		spcom = createExtractor("getSpcom",String.class, DatosGenerales.IND_SPCOM);
		kbencon = createExtractor("kbencon",String.class, DatosGenerales.IND_KBENCON);
		kajuste = createExtractor("kajuste",Integer.class, DatosGenerales.IND_EKAJUSTE);
		kgarantia = createExtractor("kgarantia",Integer.class, DatosGenerales.IND_EKGARANTIA);
		
		getCache().addIndex(ccanalExtractor, false, null);
		getCache().addIndex(cnegocioExtractor, false, null);
		getCache().addIndex(kmodalidadExtractor, false, null);
		getCache().addIndex(ramoExtractor, false, null);
		getCache().addIndex(gapActExtractor, false, null);
		
		getCache().addIndex(kgarantiaExtractor, false, null);
		getCache().addIndex(kprestacionExtractor, false, null);
		
		getCache().addIndex(this.getMultiExtractor(), false, null);
		getCache().addIndex(this.getMultiExtractor2(), false, null);
//		getCache().addIndex(this.getMultiExtractor3(), false, null);
		getCache().addIndex(this.getMultiExtractorRecuperarUmics(), false, null);
		getCache().addIndex(this.getMultiExtractorRecuperarSaldoTotal(), false, null);
		getCache().addIndex(this.getMultiExtractorRecuperarUmic310(), false, null);
	}

	@Override
	public Set<Entry<UmicKey, DatosGenerales>> entrySet() {
		return (Set<Entry<UmicKey, DatosGenerales>>) this.getCache().entrySet();
	}
	
	public Set<Entry<UmicKey, DatosGenerales>> entrySet(Filter filter) {
		return (Set<Entry<UmicKey, DatosGenerales>>) this.getCache().entrySet(filter);
	}

	@Override
	public DatosGenerales get(Object key) {
		return (DatosGenerales) this.getCache().get(key);
	}

	@Override
	public Set<UmicKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public DatosGenerales put(UmicKey key, DatosGenerales value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		 return value;
	}

	@Override
	public DatosGenerales remove(Object key) {
		return (DatosGenerales) this.getCache().remove(key);
	}

	@Override
	public Collection<DatosGenerales> values() {
		return (Collection<DatosGenerales>) this.getCache().values();
	}
	
	public Map<List<Object>, Integer> agregar(){
		
		ValueExtractor multiExtractor = new MultiExtractor(new ValueExtractor[] {
				kmodalidadExtractor,
				kgarantiaExtractor,
				kprestacionExtractor
		});
		
		InvocableMap.EntryAggregator aggregator = GroupAggregator.createInstance(multiExtractor, new Count());
		
		Map<List<Object>, Integer> result = (Map<List<Object>, Integer>) this.getCache().aggregate(AlwaysFilter.INSTANCE, aggregator);
		
		return result;
		
	}
	
	
	public Filter getFilterEntregablePRVBT(PrvBt prvBt) {				
		Filter allFilter = new EqualsFilter(getMultiExtractor(), Arrays.asList(prvBt.getCnegocio(), prvBt.getCcanal(), prvBt.getKramo(),
				prvBt.getKmodalidad(), prvBt.getKgarantia(), prvBt.getSegmento1(), prvBt.getTiposubriesgo(), prvBt.getKpoliza(),
				prvBt.getKsubpoliza(), prvBt.getNsuscri(), prvBt.getKcarterainv(), prvBt.getGapact()));
				
		return allFilter;
	}
	
//	public Filter getFilterEntregableFLUJCOASEG(FlujCoaSeg flujcoaseguro) {				
//		Filter allFilter = new EqualsFilter(getMultiExtractor2(), Arrays.asList(flujcoaseguro.getCnegocio(), flujcoaseguro.getCcanal(), 
//				flujcoaseguro.getKramo(), flujcoaseguro.getKmodalidad(), flujcoaseguro.getSegmento1(), flujcoaseguro.getTiposubriesgo(),
//				flujcoaseguro.getKpoliza(), flujcoaseguro.getKsubpoliza(), flujcoaseguro.getNsuscri(), flujcoaseguro.getKcarterainv(),
//				flujcoaseguro.getGapact()));
//				
//		return allFilter;
//	}
	
	public Filter getFilterEntregablePROVCOASEG(ProvCoaSeg provcoaseguro) {				
		Filter allFilter = new EqualsFilter(getMultiExtractor2(), Arrays.asList(provcoaseguro.getCnegocio(), provcoaseguro.getCcanal(), 
				provcoaseguro.getKramo(), provcoaseguro.getKmodalidad(), provcoaseguro.getSegmento1(), provcoaseguro.getTiposubriesgo(),
				provcoaseguro.getKpoliza(), provcoaseguro.getKsubpoliza(), provcoaseguro.getNsuscri(), provcoaseguro.getKcarterainv(),
				provcoaseguro.getGapact()));
				
		return allFilter;
	}
	
//	public Filter getFilterEntregableFLUJCAS(FlujTcas flujTcas) {		
//		Filter allFilter = new EqualsFilter(getMultiExtractor3(), Arrays.asList(flujTcas.getCnegocio(), flujTcas.getCcanal(), 
//				flujTcas.getKramo(), flujTcas.getKmodalidad(), flujTcas.getKpoliza(), flujTcas.getKsubpoliza(), flujTcas.getNsuscri(), flujTcas.getKcarterainv(),
//				flujTcas.getGapact()));
//				
//		return allFilter;
//	}
/*INI-TAR00302248*/
	public Filter getFilterEntregableContabilidad(Contabilidad contabilidad) {				
		Filter allFilter = new EqualsFilter(getMultiExtractor4(), Arrays.asList(contabilidad.getCcanal(),contabilidad.getKramo(),
				contabilidad.getKmodalidad(),contabilidad.getKpoliza(),contabilidad.getKsubpoliza(),contabilidad.getCnegocio()));
				
				
		return allFilter;
	}
/*FIN-TAR00302248*/	
	
	private ValueExtractor getMultiExtractor() {
		ValueExtractor[] ve = {cnegocioExtractor, ccanalExtractor, kramoExtractor, kmodalidadExtractor, kgarantiaExtractor, segmento1Extractor,
				tiposubriesgoExtractor, polizaExtractor, subpolizaExtractor, suscriExtractor, carterainvExtractor, gapActExtractor};
		
		return new MultiExtractor(ve);
	}
	
	private ValueExtractor getMultiExtractor2() {
		ValueExtractor[] ve = {cnegocioExtractor, ccanalExtractor, kramoExtractor, kmodalidadExtractor, segmento1Extractor,
				tiposubriesgoExtractor, polizaExtractor, subpolizaExtractor, suscriExtractor, carterainvExtractor, gapActExtractor};
		
		return new MultiExtractor(ve);		
	}
	
//	private ValueExtractor getMultiExtractor3() {
//		ValueExtractor[] ve = {cnegocioExtractor, ccanalExtractor, kramoExtractor, kmodalidadExtractor, polizaExtractor,
//				subpolizaExtractor, suscriExtractor, carterainvExtractor, gapActExtractor};
//		
//		return new MultiExtractor(ve);		
//	}
	
/*INI-TAR00302248*/
	private ValueExtractor getMultiExtractor4() {
		ValueExtractor[] ve = {ccanalExtractor, kramoExtractor, kmodalidadExtractor, polizaExtractor, subpolizaExtractor, cnegocioExtractor};
		
		return new MultiExtractor(ve);		
	}
/*FIN-TAR00302248*/
	
	public Filter getFilterUmicRelacionadas(Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer kcertificado,
			Integer nsuscri, String ctipoaport, String spcom) {	
		Filter allFilter = new EqualsFilter(getMultiExtractorRecuperarUmics(), Arrays.asList(kmodalidad, kpoliza, ksubpoliza,
				kcertificado, nsuscri, ctipoaport, spcom));
				
		return allFilter;		
	}
	
	public Filter getFilterRecuperarSaldoTotal(Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer kcertificado,
			Integer nsuscri) {	
		Filter allFilter = new EqualsFilter(getMultiExtractorRecuperarSaldoTotal(), Arrays.asList(kmodalidad, kpoliza, ksubpoliza,
				kcertificado, nsuscri));
				
		return allFilter;		
	}
	
	public Filter getFilterRecuperarUmic310(Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer kcertificado,
			Integer nsuscri, String ctipoaport, Integer kgarantia) {	
		Filter allFilter = new EqualsFilter(getMultiExtractorRecuperarUmic310(), Arrays.asList(kmodalidad, kpoliza, ksubpoliza,
				kcertificado, nsuscri, ctipoaport, kgarantia));
				
		return allFilter;		
	}
	

	
	public Filter getFilterRecuperarUmicTitular(Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer kcertificado,
			Integer nsuscri, String kbencon) {	
		Filter allFilter = new EqualsFilter(getMultiExtractorRecuperarUmicTitular(), Arrays.asList(kmodalidad, kpoliza, ksubpoliza,
				kcertificado, nsuscri, kbencon));
				
		return allFilter;		
	}
	
	public Filter getFilterRecuperarUmicTitularFechaInicioRenta(Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer kcertificado,
			Integer nsuscri, String kbencon, Timestamp feciniciorenta, Integer forpagrent, String cpagrenta) {	
		Filter allFilter = new EqualsFilter(getMultiExtractorRecuperarUmicTitular(), Arrays.asList(kmodalidad, kpoliza, ksubpoliza,
				kcertificado, nsuscri, kbencon,feciniciorenta,forpagrent,cpagrenta));
				
		return allFilter;		
	}
	
	
	private ValueExtractor getMultiExtractorRecuperarUmics() {
		ValueExtractor[] ve = {kmodalidadExtractor, polizaExtractor, subpolizaExtractor, kcertificado, suscriExtractor,
				ctipoaport, spcom};
		
		return new MultiExtractor(ve);
	}
	
	private ValueExtractor getMultiExtractorRecuperarSaldoTotal() {
		ValueExtractor[] ve = {kmodalidadExtractor, polizaExtractor, subpolizaExtractor, kcertificado, suscriExtractor,};
		
		return new MultiExtractor(ve);
	}
	
	private ValueExtractor getMultiExtractorRecuperarUmic310() {
		ValueExtractor[] ve = {kmodalidadExtractor, polizaExtractor, subpolizaExtractor, kcertificado, suscriExtractor,
				ctipoaport, kgarantiaExtractor};
		
		return new MultiExtractor(ve);
	}
	
	private ValueExtractor getMultiExtractorRecuperarUmicTitular() {
		ValueExtractor[] ve = {kmodalidadExtractor, polizaExtractor, subpolizaExtractor, kcertificado, suscriExtractor,
				kbencon};
		
		return new MultiExtractor(ve);
	}
	
	private ValueExtractor getMultiExtractorRecuperarUmicRelacionadas() {
		ValueExtractor[] ve = {kmodalidadExtractor, polizaExtractor, subpolizaExtractor, kcertificado, suscriExtractor,
				kgarantiaExtractor, kprestacionExtractor};
		
		return new MultiExtractor(ve);
	}
	
	/*private ValueExtractor getMultiExtractorRecuperarUmicTitularFechaInicioRenta() {
		ValueExtractor[] ve = {kmodalidadExtractor, polizaExtractor, subpolizaExtractor, kcertificado, suscriExtractor,
				kbencon};
		
		return new MultiExtractor(ve);
	}*/
	
	private ValueExtractor getMultiExtractorRecuperarUmicMensual() {
		ValueExtractor[] ve = {kmodalidadExtractor, polizaExtractor, subpolizaExtractor, kcertificado, suscriExtractor,
				kgarantia, kajuste, ctipoaport, kbencon};
		
		return new MultiExtractor(ve);
	}
	
	private ValueExtractor getMultiExtractorRecuperarUmicMensualBNC() {
		ValueExtractor[] ve = {kmodalidadExtractor, polizaExtractor, subpolizaExtractor, kcertificado, suscriExtractor,
				kgarantia, kajuste, ctipoaport, kprestacionExtractor};
		
		return new MultiExtractor(ve);
	}
	
	private ValueExtractor getMultiExtractorRecuperarUmicUniv() {
        ValueExtractor[] ve = {kmodalidadExtractor, polizaExtractor, subpolizaExtractor};

        return new MultiExtractor(ve);
    }
	
	public Filter getFilterRecuperarUmicMensual(Integer kmodalidad, Long kpoliza, Integer ksubpoliza,
			 Integer kcertificado, Integer nsuscri, Integer kgarantia, Integer kajuste, String ctipoaport, String kbencon) {	
		Filter allFilter = new EqualsFilter(getMultiExtractorRecuperarUmicMensual(), Arrays.asList(kmodalidad, kpoliza, ksubpoliza,
				kcertificado, nsuscri, kgarantia, kajuste, ctipoaport, kbencon));
				
		return allFilter;		
	}
	
	public Filter getFilterRecuperarUmicRelacionadas(Integer kmodalidad, Long kpoliza, Integer ksubpoliza, Integer kcertificado,
			Integer nsuscri, Integer kgarantia, String kprestacion) {	
		Filter allFilter = new EqualsFilter(getMultiExtractorRecuperarUmicRelacionadas(), Arrays.asList(kmodalidad, kpoliza, ksubpoliza,
				kcertificado, nsuscri, kgarantia, kprestacion));
				
		return allFilter;		
	}
	
	public Filter getFilterRecuperarUmicMensualBNC(Integer kmodalidad, Long kpoliza, Integer ksubpoliza,
			 Integer kcertificado, Integer nsuscri, Integer kgarantia, Integer kajuste, String ctipoaport, String kbencon,
			 String kprestacion) {	
		Filter allFilter = new EqualsFilter(getMultiExtractorRecuperarUmicMensualBNC(), Arrays.asList(kmodalidad, kpoliza, ksubpoliza,
				kcertificado, nsuscri, kgarantia, kajuste, ctipoaport, kprestacion));
				
		return allFilter;		
	}
	
	public Filter getFilterRecuperarUmicUniv(Integer kmodalidad, Long kpoliza, Integer ksubpoliza) {  
		Filter allFilter = new EqualsFilter(getMultiExtractorRecuperarUmicUniv(), Arrays.asList(kmodalidad, kpoliza, ksubpoliza));

	    return allFilter;      
	}

}
