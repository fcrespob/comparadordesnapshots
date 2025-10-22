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

import es.mapfre.solvencia.coherence.keys.entregables.PrvInf1Key;
import es.mapfre.solvencia.dao.impl.entregables.PrvInf1Dao;
import es.mapfre.solvencia.dominio.entregables.PrvInf1;
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
public class EntregablePRVINF1 extends EntregableGenerico {

	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();

	private static final Logger LOG = LoggerFactory.getLogger(EntregablePRVINF1.class);
	private static final String CACHE_TOTALES_FLUJOS = ConstantsEntregables.CACHE_TOTALES_FLUJOS;

	private final static PrvInf1Dao prvInf1Dao = new PrvInf1Dao();
	
	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_PRVINF1;
	}

	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {
		this.initProgress(oEnvironment);
		
		NamedCache totales = CacheFactory.getCache(CACHE_TOTALES_FLUJOS);

		ValueExtractor[] rows = new ValueExtractor[] {
				// BT
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
				new PofExtractor(String.class, TotalesFlujos.IND_GESTIONIT)
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
		
		List<PrvInf1> prvInf1s = transformResults(pivotResults);

		// Guardar resultados en cache
		servicio.almacenarEntregablePrvInf1(prvInf1s);

		this.setProgress(pivotResults.size(), pivotResults.size());
	}

	/**
	 * Extrae los resultados de la agregación
	 * 
	 * @param pivotResults
	 * @return
	 */
	private static List<PrvInf1> transformResults(Map<Object, Object> pivotResults) {
		List<PrvInf1> prvInf1s = new ArrayList<PrvInf1>();

		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			prvInf1s.add(transformEntry(new PrvInf1(), entry));
		}

		return prvInf1s;
	}

	/**
	 * Mapea cada entrada de los resultados a la cache prvInf1
	 * 
	 * @param prvInf1
	 * @param entry
	 * @return
	 */
	private static PrvInf1 transformEntry(PrvInf1 prvInf1, Entry<Object, Object> entry) {
		List key = (List) entry.getKey();
		prvInf1.setBt((String) key.get(ConstantsFunciones.CTE_0));
		prvInf1.setFeccierre((Timestamp) key.get(ConstantsFunciones.CTE_1));
		prvInf1.setCnegocio((String) key.get(ConstantsFunciones.CTE_2));
		prvInf1.setCcanal((Integer) key.get(ConstantsFunciones.CTE_3));
		prvInf1.setKcarterainv((String) key.get(ConstantsFunciones.CTE_4));
		prvInf1.setGapAct((String) key.get(ConstantsFunciones.CTE_5));
		prvInf1.setKramo((String) key.get(ConstantsFunciones.CTE_6));
		prvInf1.setKmodalidad((Integer) key.get(ConstantsFunciones.CTE_7));
		if ((!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_BEL)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_BELCOA)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_BELCLR)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRTIU)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRTID)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRGTO)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRMFE)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRMMI)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRMCF)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRMCI)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRLFE)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRLMI)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRINC)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRVM )) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRAEP)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRAEN)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRAIP)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRAIN)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRANM))) {
			prvInf1.setIntfeccal((BigDecimal) key.get(ConstantsFunciones.CTE_8));
		}
		prvInf1.setCtipoprovi((String) key.get(ConstantsFunciones.CTE_9));
		prvInf1.setSpcom((String) key.get(ConstantsFunciones.CTE_10));
		prvInf1.setKoficont((String) key.get(ConstantsFunciones.CTE_11));
		prvInf1.setKmodext((Integer) key.get(ConstantsFunciones.CTE_12));
		prvInf1.setGestionit((String) key.get(ConstantsFunciones.CTE_13));

		List value = (List) entry.getValue();
		prvInf1.setTotProvi((BigDecimal) value.get(ConstantsFunciones.CTE_0));
		
		// Para las bases técnicas de SCR mostraremos la provisión de BEL en el campo prvpb
		if ((!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRTIU)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRTID)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRGTO)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRMFE)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRMMI)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRMCF)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRMCI)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRLFE)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRLMI)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRINC)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRVM))  && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRAEP)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRAEN)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRAIP)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRAIN)) && 
				(!prvInf1.getBt().equals(ConstantsEntregables.CTE_BT_SCRANM))){
			BigDecimal pfpinv = (BigDecimal) value.get(ConstantsFunciones.CTE_1);
			if (prvInf1.getTotProvi() != null && (pfpinv != null)) {
				prvInf1.setPfpInv(prvInf1.getTotProvi().subtract(pfpinv));
			}
		} else {
			// Obtenemos la provisión de BEL
			PrvInf1Key prvKeyBel = new PrvInf1Key(ConstantsEntregables.CTE_BT_BEL, prvInf1.getCnegocio(), prvInf1.getCcanal(), prvInf1.getKcarterainv(), prvInf1.getGapAct(), prvInf1.getKramo(), prvInf1.getKmodalidad(), prvInf1.getIntfeccal(), prvInf1.getCtipoprovi(), prvInf1.getSpcom(), prvInf1.getKoficont(), prvInf1.getKmodext(), prvInf1.getGestionit());
			PrvInf1 prvInf1Bel = prvInf1Dao.get(prvKeyBel);
			prvInf1.setPfpInv(prvInf1Bel.getTotProvi());
		}
		return prvInf1;
	}

}
