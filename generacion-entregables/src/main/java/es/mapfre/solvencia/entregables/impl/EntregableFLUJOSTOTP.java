package es.mapfre.solvencia.entregables.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import es.mapfre.solvencia.coherence.keys.entregables.FlujosTotPKey;
import es.mapfre.solvencia.dao.impl.entregables.FlujosTotPDao;
import es.mapfre.solvencia.dominio.entregables.FlujosTotP;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el cálculo del Entregable PRVINF1.
 * 
 */
public class EntregableFLUJOSTOTP extends EntregableGenerico {

	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();

	private static final Logger LOG = LoggerFactory.getLogger(EntregableFLUJOSTOTP.class);
	private static final String CACHE_TOTALES_FLUJOS = ConstantsEntregables.CACHE_TOTALES_FLUJOS;

	private final static FlujosTotPDao flujosTotPDao = new FlujosTotPDao();
	
	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_FLUJOSTOTP;
	}

	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {
		this.initProgress(oEnvironment);
		
		NamedCache totales = CacheFactory.getCache(CACHE_TOTALES_FLUJOS);

		ValueExtractor[] rows = new ValueExtractor[] {
				/*// BT
				new PofExtractor(String.class, TotalesFlujos.IND_BT),
				// Fcierre
				new PofExtractor(Timestamp.class, TotalesFlujos.IND_FCIERRE),
				// Negocio
				new PofExtractor(String.class, TotalesFlujos.IND_CNEGOCIO),
				// Canal
				new PofExtractor(Integer.class, TotalesFlujos.IND_CCANAL),
				// Cartera de Inversión
				new PofExtractor(String.class, TotalesFlujos.IND_KCARTERAINV),
				// Grupo Activo pasivo
				new PofExtractor(String.class, TotalesFlujos.IND_GAPACT),
				// Ramo
				new PofExtractor(String.class, TotalesFlujos.IND_KRAMO),
				// Modalidad
				new PofExtractor(Integer.class, TotalesFlujos.IND_KMODALIDAD),
				// intFecCalc
				new PofExtractor(BigDecimal.class, TotalesFlujos.IND_INTFECCALC),
				// CTipoProvi
				new PofExtractor(String.class, TotalesFlujos.IND_CTIPOPROVI),
				// Spcom
				new PofExtractor(String.class, TotalesFlujos.IND_SPCOM),
				// KOfiCont
				new PofExtractor(String.class, TotalesFlujos.IND_KOFICONT), 
				// Modalidad externa
				new PofExtractor(Integer.class, TotalesFlujos.IND_KMODEXT),
				// Gestion it
				new PofExtractor(String.class, TotalesFlujos.IND_GESTIONIT)*/
				// Negocio
				new PofExtractor(String.class, TotalesFlujos.IND_CNEGOCIO),
				};

		// Rows
		ValueExtractor multiExtractor = new MultiExtractor(rows);

		// Values : prvinf1.prv = ∑ total.flujos.totprovision
		InvocableMap.EntryAggregator[] values = new InvocableMap.EntryAggregator[] {
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTPROVISION)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_PFPINV))
		};

		// Create Values Aggregator
		CompositeAggregator valuesAggregator = CompositeAggregator.createInstance(values);

		// Create Entry Aggregator
		InvocableMap.EntryAggregator pivotAggregator = GroupAggregator.createInstance(multiExtractor, valuesAggregator);

		// Filtrar entradas por kbasetec
		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, TotalesFlujos.IND_BT), kbasetec);

		// Do the query
		Map<Object, Object> pivotResults = (Map<Object, Object>) totales.aggregate(isKbasetec, pivotAggregator);

		this.setProgress(pivotResults.size(), 0);
		
		List<FlujosTotP> flujosTotPs = transformResults(pivotResults);

		// Guardar resultados en cache
		servicio.almacenarEntregableFlujosTotP(flujosTotPs);

		this.setProgress(pivotResults.size(), pivotResults.size());
	}

	/**
	 * Extrae los resultados de la agregación
	 * 
	 * @param pivotResults
	 * @return
	 */
	private static List<FlujosTotP> transformResults(Map<Object, Object> pivotResults) {
		List<FlujosTotP> flujosTotPs = new ArrayList<FlujosTotP>();

		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			flujosTotPs.add(transformEntry(new FlujosTotP(), entry));
		}

		return flujosTotPs;
	}

	/**
	 * Mapea cada entrada de los resultados a la cache flujosTotP
	 * 
	 * @param flujosTotP
	 * @param entry
	 * @return
	 */
	private static FlujosTotP transformEntry(FlujosTotP flujosTotP, Entry<Object, Object> entry) {
		List key = (List) entry.getKey();
		/*flujosTotP.setBt((String) key.get(ConstantsFunciones.CTE_0));
		flujosTotP.setFeccierre((Timestamp) key.get(ConstantsFunciones.CTE_1));
		flujosTotP.setCnegocio((String) key.get(ConstantsFunciones.CTE_2));
		flujosTotP.setCcanal((Integer) key.get(ConstantsFunciones.CTE_3));
		flujosTotP.setKcarterainv((String) key.get(ConstantsFunciones.CTE_4));
		flujosTotP.setGapAct((String) key.get(ConstantsFunciones.CTE_5));
		flujosTotP.setKramo((String) key.get(ConstantsFunciones.CTE_6));
		flujosTotP.setKmodalidad((Integer) key.get(ConstantsFunciones.CTE_7));
		if ((!flujosTotP.getBt().equals(ConstantsEntregables.CTE_BT_BEL)) && 
				(!flujosTotP.getBt().equals(ConstantsEntregables.CTE_BT_BELCOA)) && 
				(!flujosTotP.getBt().equals(ConstantsEntregables.CTE_BT_BELCLR)) && 
				(!flujosTotP.getBt().equals(ConstantsEntregables.CTE_BT_SCRTIU)) && 
				(!flujosTotP.getBt().equals(ConstantsEntregables.CTE_BT_SCRTID)) && 
				(!flujosTotP.getBt().equals(ConstantsEntregables.CTE_BT_SCRGTO)) && 
				(!flujosTotP.getBt().equals(ConstantsEntregables.CTE_BT_SCRMFE)) && 
				(!flujosTotP.getBt().equals(ConstantsEntregables.CTE_BT_SCRMMI)) && 
				(!flujosTotP.getBt().equals(ConstantsEntregables.CTE_BT_SCRMCF)) && 
				(!flujosTotP.getBt().equals(ConstantsEntregables.CTE_BT_SCRMCI)) && 
				(!flujosTotP.getBt().equals(ConstantsEntregables.CTE_BT_SCRLFE)) && 
				(!flujosTotP.getBt().equals(ConstantsEntregables.CTE_BT_SCRLMI)) && 
				(!flujosTotP.getBt().equals(ConstantsEntregables.CTE_BT_SCRINC)) && 
				(!flujosTotP.getBt().equals(ConstantsEntregables.CTE_BT_SCRVM )) && 
				(!flujosTotP.getBt().equals(ConstantsEntregables.CTE_BT_SCRAEP)) && 
				(!flujosTotP.getBt().equals(ConstantsEntregables.CTE_BT_SCRAEN)) && 
				(!flujosTotP.getBt().equals(ConstantsEntregables.CTE_BT_SCRAIP)) && 
				(!flujosTotP.getBt().equals(ConstantsEntregables.CTE_BT_SCRAIN)) && 
				(!flujosTotP.getBt().equals(ConstantsEntregables.CTE_BT_SCRANM))) {
			flujosTotP.setIntfeccal((BigDecimal) key.get(ConstantsFunciones.CTE_8));
		}
		flujosTotP.setCtipoprovi((String) key.get(ConstantsFunciones.CTE_9));
		flujosTotPrima.setSpcom((String) key.get(ConstantsFunciones.CTE_10));
		flujosTotPrima.setKoficont((String) key.get(ConstantsFunciones.CTE_11));
		flujosTotPrima.setKmodext((Integer) key.get(ConstantsFunciones.CTE_12));
		flujosTotPrima.setGestionit((String) key.get(ConstantsFunciones.CTE_13));

		List value = (List) entry.getValue();
		flujosTotPrima.setTotProvi((BigDecimal) value.get(ConstantsFunciones.CTE_0));
		
		// Para las bases técnicas de SCR mostraremos la provisión de BEL en el campo flujosTotPrimapb
		if ((!flujosTotPrima.getBt().equals(ConstantsEntregables.CTE_BT_SCRTIU)) && 
				(!flujosTotPrima.getBt().equals(ConstantsEntregables.CTE_BT_SCRTID)) && 
				(!flujosTotPrima.getBt().equals(ConstantsEntregables.CTE_BT_SCRGTO)) && 
				(!flujosTotPrima.getBt().equals(ConstantsEntregables.CTE_BT_SCRMFE)) && 
				(!flujosTotPrima.getBt().equals(ConstantsEntregables.CTE_BT_SCRMMI)) && 
				(!flujosTotPrima.getBt().equals(ConstantsEntregables.CTE_BT_SCRMCF)) && 
				(!flujosTotPrima.getBt().equals(ConstantsEntregables.CTE_BT_SCRMCI)) && 
				(!flujosTotPrima.getBt().equals(ConstantsEntregables.CTE_BT_SCRLFE)) && 
				(!flujosTotPrima.getBt().equals(ConstantsEntregables.CTE_BT_SCRLMI)) && 
				(!flujosTotPrima.getBt().equals(ConstantsEntregables.CTE_BT_SCRINC)) && 
				(!flujosTotPrima.getBt().equals(ConstantsEntregables.CTE_BT_SCRVM))  && 
				(!flujosTotPrima.getBt().equals(ConstantsEntregables.CTE_BT_SCRAEP)) && 
				(!flujosTotPrima.getBt().equals(ConstantsEntregables.CTE_BT_SCRAEN)) && 
				(!flujosTotPrima.getBt().equals(ConstantsEntregables.CTE_BT_SCRAIP)) && 
				(!flujosTotPrima.getBt().equals(ConstantsEntregables.CTE_BT_SCRAIN)) && 
				(!flujosTotPrima.getBt().equals(ConstantsEntregables.CTE_BT_SCRANM))){
			BigDecimal pfpinv = (BigDecimal) value.get(ConstantsFunciones.CTE_1);
			if (flujosTotPrima.getTotProvi() != null && (pfpinv != null)) {
				flujosTotPrima.setPfpInv(flujosTotPrima.getTotProvi().subtract(pfpinv));
			}
		} else {
			// Obtenemos la provisión de BEL
			FlujosTotPrimaKey flujosTotPrimaKeyBel = new FlujosTotPrimaKey(ConstantsEntregables.CTE_BT_BEL, flujosTotPrima.getCnegocio(), flujosTotPrima.getCcanal(), flujosTotPrima.getKcarterainv(), flujosTotPrima.getGapAct(), flujosTotPrima.getKramo(), flujosTotPrima.getKmodalidad(), flujosTotPrima.getIntfeccal(), flujosTotPrima.getCtipoprovi(), flujosTotPrima.getSpcom(), flujosTotPrima.getKoficont(), flujosTotPrima.getKmodext(), flujosTotPrima.getGestionit());
			FlujosTotPrima flujosTotPrimaBel = flujosTotPrimaDao.get(flujosTotPrimaKeyBel);
			FlujosTotPrima.setPfpInv(flujosTotPrimaBel.getTotProvi());
		}
		*/
		return flujosTotP;
	}


}
