package es.mapfre.solvencia.entregables.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.coherence.patterns.processing.task.TaskExecutionEnvironment;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.InvocableMap;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.aggregator.BigDecimalSum;
import com.tangosol.util.aggregator.CompositeAggregator;
import com.tangosol.util.aggregator.GroupAggregator;
import com.tangosol.util.extractor.MultiExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.filter.EqualsFilter;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.SwCobroComisionesKey;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.SwCobroComisionesDao;
import es.mapfre.solvencia.dominio.entregables.DetalleCorrienteEntregables;
import es.mapfre.solvencia.dominio.entregables.SwCobroCom;
import es.mapfre.solvencia.dominio.entregables.SwCobroComCsv;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.SwCobroComisiones;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el cálculo del Entregable SWCOBROCOM.
 * 
 */
public class EntregableSWCOBROCOM extends EntregableGenerico {

	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();

	private static final Logger LOG = LoggerFactory.getLogger(EntregableSWCOBROCOM.class);
	private static final String CACHE_DETALLE_CORRIENTE_ENTREGABLES = ConstantsEntregables.CACHE_DETALLE_CORRIENTE_ENTREGABLES;

	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_SWCOBROCOM;
	}

	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {
		
		this.initProgress(oEnvironment);

		NamedCache totales = CacheFactory.getCache(CACHE_DETALLE_CORRIENTE_ENTREGABLES);
		
		
		ValueExtractor[] rows = new ValueExtractor[] {
				new PofExtractor(Long.class, DetalleCorrienteEntregables.IND_KPOLIZA),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_KSUBPOLIZA),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_KRAMO),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_CCANAL),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_NEGOCIO)
				};

		// Rows
		ValueExtractor multiExtractor = new MultiExtractor(rows);

		InvocableMap.EntryAggregator[] values = new InvocableMap.EntryAggregator[] {
			new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUECOMI_IMPFLUJONOMINAL))
		};
		
		CompositeAggregator valuesAggregator = CompositeAggregator.createInstance(values);

		InvocableMap.EntryAggregator pivotAggregator = GroupAggregator.createInstance(multiExtractor, valuesAggregator);
 
		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT),
				kbasetec);

		Map<Object, Object> pivotResults = (Map<Object, Object>) totales.aggregate(isKbasetec, pivotAggregator);
		
		this.setProgress(pivotResults.size(), 0);

		List<SwCobroCom> swcobrocom = transformResults(pivotResults,kbasetec);

		// Almacenar en cache
		servicio.almacenarEntregableSWCOBROCOM(swcobrocom);

		this.setProgress(pivotResults.size(), pivotResults.size());

	}

	/**
	 * Extraer los resultados de la agregación
	 * 
	 * @param pivotResults
	 * @return
	 */
	private List<SwCobroCom> transformResults(Map<Object, Object> pivotResults, String kbasetec) {
		List<SwCobroCom> swcobrocom = new ArrayList<SwCobroCom>();
		List<SwCobroComCsv> swCobroComCsv = new ArrayList<SwCobroComCsv>();

		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			SwCobroComCsv swCsv = new SwCobroComCsv();
			SwCobroCom sw = transformEntry(new SwCobroCom(), entry, kbasetec);
			swCsv.setKpoliza(sw.getKpoliza());
			swCsv.setKsubpoliza(sw.getKsubpoliza());
			swCsv.setComision(sw.getComision().divide(BigDecimal.valueOf(100), ConstantsFunciones.MATH_CONTEXT));
			swCsv.setComisioncalc(sw.getComisioncalc().divide(BigDecimal.valueOf(100), ConstantsFunciones.MATH_CONTEXT));
			swCsv.setPcorrector(sw.getPcorrector().divide(BigDecimal.valueOf(100), ConstantsFunciones.MATH_CONTEXT));
			swcobrocom.add(sw);
			swCobroComCsv.add(swCsv);
		}
		
		servicio.almacenarEntregableSwCobroComCsv(swCobroComCsv);
		return swcobrocom;
		
	}

	/**
	 * Mapea cada entrada de los resultados a la cache FlujosTotNiif17Key
	 * 
	 * @param flujosTotNiif17Key
	 * @param entry
	 * @return
	 */
	private SwCobroCom transformEntry(SwCobroCom swcobrocom, Entry<Object, Object> entry, String kbasetec) {
		
		List key = (List) entry.getKey();
		SwCobroComisionesDao swCobroComisionesDao = new SwCobroComisionesDao();
		BigDecimal varComisiones = BigDecimal.ZERO;
		
		swcobrocom.setKpoliza((Long) key.get(ConstantsFunciones.CTE_0));
		
		swcobrocom.setKsubpoliza((Integer) key.get(ConstantsFunciones.CTE_1));
		
		String kramo = (String) key.get(ConstantsFunciones.CTE_2);
		
		Collection<SwCobroComisiones> a = swCobroComisionesDao.values();
		SwCobroComisionesKey keycom = new SwCobroComisionesKey();
		if(swcobrocom.getKsubpoliza() == 0 && kramo.equalsIgnoreCase("118")){
			 keycom = new SwCobroComisionesKey(swcobrocom.getKpoliza(), 1);
		}else{
			 keycom = new SwCobroComisionesKey(swcobrocom.getKpoliza(), swcobrocom.getKsubpoliza());
		}
		SwCobroComisiones swCobrocomisiones = swCobroComisionesDao.get(keycom);
		
		if(null == swCobrocomisiones){
			 keycom = new SwCobroComisionesKey(swcobrocom.getKpoliza(), null);
			 swCobrocomisiones = swCobroComisionesDao.get(keycom);
		 }
		
		List values = (List) entry.getValue();
		
		BigDecimal totCom = (BigDecimal) values.get(ConstantsFunciones.CTE_0);
		BigDecimal pcorrector = BigDecimal.ZERO;
		if (swCobrocomisiones != null) {
			if(swCobrocomisiones.getComision() != null){
				varComisiones = swCobrocomisiones.getComision();
			}
			
			if((varComisiones.compareTo(BigDecimal.ZERO) != 0 || varComisiones != null) && (totCom.compareTo(BigDecimal.ZERO) == 0 || totCom == null)){
				swcobrocom.setPcorrector(BigDecimal.ONE.multiply(BigDecimal.valueOf(100)).multiply(BigDecimal.valueOf(100)));
				swcobrocom.setComision(varComisiones.multiply(BigDecimal.valueOf(100)));
				swcobrocom.setComisioncalc(BigDecimal.ZERO);
			}else if((varComisiones.compareTo(BigDecimal.ZERO) == 0 || varComisiones == null) && (totCom.compareTo(BigDecimal.ZERO) != 0 || totCom != null)){
				swcobrocom.setPcorrector(BigDecimal.ZERO);
				swcobrocom.setComision(BigDecimal.ZERO);
				swcobrocom.setComisioncalc((BigDecimal) values.get(ConstantsFunciones.CTE_0));
				swcobrocom.setComisioncalc(swcobrocom.getComisioncalc().multiply(BigDecimal.valueOf(100)));
			}else{
				if(varComisiones.compareTo(BigDecimal.ZERO) != 0){
					swcobrocom.setComision(varComisiones.multiply(BigDecimal.valueOf(100)));
				}else{
					swcobrocom.setComision(BigDecimal.ZERO);
				}
				swcobrocom.setComisioncalc((BigDecimal) values.get(ConstantsFunciones.CTE_0));
				swcobrocom.setComisioncalc(swcobrocom.getComisioncalc().multiply(BigDecimal.valueOf(100)));
				if (totCom.compareTo(BigDecimal.ZERO) != 0) {
					pcorrector = (BigDecimal) varComisiones.divide(totCom, ConstantsFunciones.MATH_CONTEXT);
					pcorrector = pcorrector.multiply(BigDecimal.valueOf(100));
					pcorrector = pcorrector.setScale(2, RoundingMode.DOWN);
					pcorrector = pcorrector.multiply(BigDecimal.valueOf(100));
					swcobrocom.setPcorrector(pcorrector);
				}else {
					swcobrocom.setPcorrector(pcorrector);
				}
			}
		}else{
			swcobrocom.setComision(BigDecimal.ZERO);
			swcobrocom.setComisioncalc((BigDecimal) values.get(ConstantsFunciones.CTE_0));
			swcobrocom.setPcorrector(BigDecimal.ONE.multiply(BigDecimal.valueOf(100)).multiply(BigDecimal.valueOf(100)));
		}
		
		Integer canal = (Integer) key.get(ConstantsFunciones.CTE_3);
		String negocio = (String) key.get(ConstantsFunciones.CTE_4);
		UmicKey umicKey = new UmicKey();
		umicKey.setKpoliza((Long) key.get(ConstantsFunciones.CTE_0));
		umicKey.setKsubpoliza((Integer) key.get(ConstantsFunciones.CTE_1));
		if(pcorrector.compareTo(ConstantsFunciones.CTE_OPER_10000) == 1){
			final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
			final Incidencia incidencia = Solvencia2ExcepcionHelper.crearAviso(ConstantsFunciones.CTE_COD_ERROR_IB, ArrayUtils.EMPTY_OBJECT_ARRAY, "BEL", canal,negocio,umicKey);
			servicio.almacenarIncidencias(incidencia);
			swcobrocom.setPcorrector(ConstantsFunciones.CTE_OPER_10000);
		}
				
		return swcobrocom;
	}


}
