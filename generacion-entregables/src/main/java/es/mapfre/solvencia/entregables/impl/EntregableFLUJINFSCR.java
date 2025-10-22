
package es.mapfre.solvencia.entregables.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import es.mapfre.solvencia.dominio.entregables.FlujInfSCR;
import es.mapfre.solvencia.dominio.entregables.DetalleCorrienteEntregables;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class EntregableFLUJINFSCR extends EntregableGenerico {

	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
	private static final String CACHE_DETALLE_CORRIENTE_ENTREGABLES = ConstantsEntregables.CACHE_DETALLE_CORRIENTE_ENTREGABLES;

	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_FLUJINFSCR;
	}

	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {
		this.initProgress(oEnvironment);

		NamedCache detalleEntregables = CacheFactory.getCache(CACHE_DETALLE_CORRIENTE_ENTREGABLES);

		ValueExtractor[] rows = new ValueExtractor[] {
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT),
				new PofExtractor(Timestamp.class, DetalleCorrienteEntregables.IND_FCIERRE),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_CCANAL),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_CNEGOCIO),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_UOA),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_KCARTERA_CONTRATO),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_KCARTERA_COHORT),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_KCARTERA_ONER),
				new PofExtractor(Timestamp.class, DetalleCorrienteEntregables.IND_FPROYFLUJEST),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_KCARTERAINV),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_KMODALIDAD)
		};

		ValueExtractor multiExtractor = new MultiExtractor(rows);

		InvocableMap.EntryAggregator[] values = new InvocableMap.EntryAggregator[] {
				new BigDecimalSum(
						new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEVIDA_IMPFLUJONOANULADO)),
				new BigDecimalSum(
						new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEFALL_IMPFLUJONOANULADO)),
				new BigDecimalSum(
						new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEGTO_IMPFLUJONOANULADO)),
				new BigDecimalSum(
						new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUECOMI_IMPFLUJONOANULADO)),
				new BigDecimalSum(
						new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUERTE_IMPFLUJONOANULADO)),
				new BigDecimalSum(
						new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUECOMPL_IMPFLUJONOAULADO)),
				new BigDecimalSum(
						new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEPRIM_IMPFLUJONOANULADO)), 
				new BigDecimalSum(
						new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEGTOAD_IMPFLUJONOANULADO))
		};

		CompositeAggregator valuesAggregator = CompositeAggregator.createInstance(values);

		InvocableMap.EntryAggregator pivotAggregator = GroupAggregator.createInstance(multiExtractor, valuesAggregator);

		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT),
				kbasetec);

		Map<Object, Object> pivotResults = (Map<Object, Object>) detalleEntregables.aggregate(isKbasetec,
				pivotAggregator);

		this.setProgress(pivotResults.size(), 0);
		
		CompositeAggregator valuesAggregatorBEL = CompositeAggregator.createInstance(values);

		InvocableMap.EntryAggregator pivotAggregatorBEL = GroupAggregator.createInstance(multiExtractor, valuesAggregatorBEL);

		EqualsFilter isKbasetecBEL = new EqualsFilter(new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT),
				kbasetec+"BEL");

		Map<Object, Object> pivotResultsBEL = (Map<Object, Object>) detalleEntregables.aggregate(isKbasetecBEL,
				pivotAggregatorBEL);
		
		List<FlujInfSCR> flujinfscrs = transformResults(pivotResults, pivotResultsBEL,kbasetec);
		// Almacenar en cache
		System.out.println("COMPLETADO-PROCESO " + kbasetec);
		if(null != flujinfscrs){
			System.out.println(flujinfscrs.size());
		}
		servicio.almacenarEntregableFlujInfSCR(flujinfscrs);
		System.out.println("COMPLETADO-CACHE " + kbasetec);
	}

	/**
	 * Extraer los resultados de la agregación
	 * 
	 * @param pivotResults
	 * @return
	 */
	private List<FlujInfSCR> transformResults(Map<Object, Object> pivotResults, Map<Object, Object> pivotResultsBEL,String kbasetec) {
		List<FlujInfSCR> listaFlujinfscr = new ArrayList<FlujInfSCR>();
		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			
//			Boolean encontrado = false;
			FlujInfSCR flujinfSCR = new FlujInfSCR();
	
			flujinfSCR = transformEntry(flujinfSCR, entry);
//			Iterator<Entry<Object, Object>> iterador = null;
//			Entry<Object, Object> datoBel = null;
//			if(null == pivotResultsBEL || null == pivotResultsBEL.entrySet() || pivotResultsBEL.entrySet().isEmpty()){
//				encontrado=true;
//				flujinfSCR.setTotFpVida(BigDecimal.ZERO);
//				flujinfSCR.setTotFpFall(BigDecimal.ZERO);
//				flujinfSCR.setTotFpGastos(BigDecimal.ZERO);
//				flujinfSCR.setTotFpComisiones(BigDecimal.ZERO);
//				flujinfSCR.setTotFpRescates(BigDecimal.ZERO);
//				flujinfSCR.setTotFpCompl(BigDecimal.ZERO);
//				flujinfSCR.setTotFpPrimas(BigDecimal.ZERO);
//				flujinfSCR.setTotFpGtoAd(BigDecimal.ZERO);
//				flujinfSCR.setTotFp(BigDecimal.ZERO);
//			}
//			}else{
//				iterador = pivotResultsBEL.entrySet().iterator();
//				datoBel = iterador.next();
//			}
			//Iterator<Entry<Object, Object>> iterador = pivotResultsBEL.entrySet().iterator();
			//Entry<Object, Object> datoBel = null;

			for (Entry<Object, Object> datoBel : pivotResultsBEL.entrySet()) {
//				
				FlujInfSCR flujinfBEL = new FlujInfSCR();
				List keysBEL = (List) datoBel.getKey();
				//List keysSCR = (List) entry.getKey();
//					
				flujinfBEL.setBt(kbasetec);
				flujinfBEL.setFcierre((Timestamp) keysBEL.get(ConstantsFunciones.CTE_1));
				flujinfBEL.setCcanal((Integer) keysBEL.get(ConstantsFunciones.CTE_2));
//				if(null == (String) keysBEL.get(ConstantsFunciones.CTE_3)){
//					flujinfBEL.setNegocio("");
//				}else{
					flujinfBEL.setNegocio((String) keysBEL.get(ConstantsFunciones.CTE_3));
				//}
					
//				if(null == (String) keysBEL.get(ConstantsFunciones.CTE_4)){
//					flujinfBEL.setUoa("");
//				}else{
					flujinfBEL.setUoa((String) keysBEL.get(ConstantsFunciones.CTE_4));
				//}
					
//				if(null == (String) keysBEL.get(ConstantsFunciones.CTE_5)){
//					flujinfBEL.setKcarteraContrato("");
//				}else{
					flujinfBEL.setKcarteraContrato((String) keysBEL.get(ConstantsFunciones.CTE_5));
				//}
					
//				if(null == (String) keysBEL.get(ConstantsFunciones.CTE_6)){
//					flujinfBEL.setKcarteraCohort("");
//				}else{
					flujinfBEL.setKcarteraCohort((String) keysBEL.get(ConstantsFunciones.CTE_6));
				//}
					
//				if(null == (String) keysBEL.get(ConstantsFunciones.CTE_7)){
//					flujinfBEL.setKcarteraOner("");
//				}else{
					flujinfBEL.setKcarteraOner((String) keysBEL.get(ConstantsFunciones.CTE_7));
				//}
					
				flujinfBEL.setFproyflujest((Timestamp) keysBEL.get(ConstantsFunciones.CTE_8));
//				if(null == (String) keysBEL.get(ConstantsFunciones.CTE_9)){
//					flujinfBEL.setKcarterainv("");
//				}else{
					flujinfBEL.setKcarterainv((String) keysBEL.get(ConstantsFunciones.CTE_9));
				//}
					
				flujinfBEL.setKmodalidad((Integer) keysBEL.get(ConstantsFunciones.CTE_10));
//					
				if(flujinfSCR.getKey().equals(flujinfBEL.getKey())){
//					//encontrado = true;
					List valuesBEL = (List) datoBel.getValue();
					
					if(null == (BigDecimal) valuesBEL.get(ConstantsFunciones.CTE_0)){
						flujinfSCR.setTotFpVida(BigDecimal.ZERO);
					}else{
						flujinfSCR.setTotFpVida((BigDecimal) valuesBEL.get(ConstantsFunciones.CTE_0));
					}
					
					if(null == (BigDecimal) valuesBEL.get(ConstantsFunciones.CTE_1)){
						flujinfSCR.setTotFpFall(BigDecimal.ZERO);
					}else{
						flujinfSCR.setTotFpFall((BigDecimal) valuesBEL.get(ConstantsFunciones.CTE_1));
					}
							
					if(null == (BigDecimal) valuesBEL.get(ConstantsFunciones.CTE_2)){
						flujinfSCR.setTotFpGastos(BigDecimal.ZERO);
					}else{
						flujinfSCR.setTotFpGastos((BigDecimal) valuesBEL.get(ConstantsFunciones.CTE_2));
					}
						
					if(null == (BigDecimal) valuesBEL.get(ConstantsFunciones.CTE_3)){
						flujinfSCR.setTotFpComisiones(BigDecimal.ZERO);
					}else{
						flujinfSCR.setTotFpComisiones((BigDecimal) valuesBEL.get(ConstantsFunciones.CTE_3));
					}
						
					if(null == (BigDecimal) valuesBEL.get(ConstantsFunciones.CTE_4)){
						flujinfSCR.setTotFpRescates(BigDecimal.ZERO);
					}else{
						flujinfSCR.setTotFpRescates((BigDecimal) valuesBEL.get(ConstantsFunciones.CTE_4));
					}
						
					if(null == (BigDecimal) valuesBEL.get(ConstantsFunciones.CTE_5)){
						flujinfSCR.setTotFpCompl(BigDecimal.ZERO);
					}else{
						flujinfSCR.setTotFpCompl((BigDecimal) valuesBEL.get(ConstantsFunciones.CTE_5));
					}
						
					if(null == (BigDecimal) valuesBEL.get(ConstantsFunciones.CTE_6)){
						flujinfSCR.setTotFpPrimas(BigDecimal.ZERO);
					}else{
						flujinfSCR.setTotFpPrimas((BigDecimal) valuesBEL.get(ConstantsFunciones.CTE_6));
					}
					
					if(null == (BigDecimal) valuesBEL.get(ConstantsFunciones.CTE_7)){
						flujinfSCR.setTotFpGtoAd(BigDecimal.ZERO);
					}else{
						flujinfSCR.setTotFpGtoAd((BigDecimal) valuesBEL.get(ConstantsFunciones.CTE_7));
					}
						
					flujinfSCR.setTotFp(flujinfSCR.getTotFpComisiones()
									.add(flujinfSCR.getTotFpCompl())
									.add(flujinfSCR.getTotFpFall())
									.add(flujinfSCR.getTotFpGastos())
									.add(flujinfSCR.getTotFpGtoAd())
									.subtract(flujinfSCR.getTotFpPrimas())
									.add(flujinfSCR.getTotFpRescates())
									.add(flujinfSCR.getTotFpVida()));
					break;
				}
			}
////				}else{
////					if(!iterador.hasNext()){
////						
////						encontrado=true;
////						flujinfSCR.setTotFpVida(BigDecimal.ZERO);
////						flujinfSCR.setTotFpFall(BigDecimal.ZERO);
////						flujinfSCR.setTotFpGastos(BigDecimal.ZERO);
////						flujinfSCR.setTotFpComisiones(BigDecimal.ZERO);
////						flujinfSCR.setTotFpRescates(BigDecimal.ZERO);
////						flujinfSCR.setTotFpCompl(BigDecimal.ZERO);
////						flujinfSCR.setTotFpPrimas(BigDecimal.ZERO);
////						flujinfSCR.setTotFpGtoAd(BigDecimal.ZERO);
////						flujinfSCR.setTotFp(BigDecimal.ZERO);
////					}else{
////						datoBel = iterador.next();
////					}
//				}
//			} 
//			//iterador.remove();
			listaFlujinfscr.add(flujinfSCR);
		}
		
		return listaFlujinfscr;
	}
	
	/**
	 * Extraer los resultados de la agregación
	 * 
	 * @param pivotResults
	 * @return
	 */
	private FlujInfSCR transformEntry(FlujInfSCR flujinfScr, Entry<Object, Object> entrySCR) {
		
		///CLAVE CONJUNTA SCR Y BEL
		List key = (List) entrySCR.getKey();
		
		flujinfScr.setBt((String) key.get(ConstantsFunciones.CTE_0));
	
		flujinfScr.setFcierre((Timestamp) key.get(ConstantsFunciones.CTE_1));
		flujinfScr.setCcanal((Integer) key.get(ConstantsFunciones.CTE_2));
//		if(null == (String) key.get(ConstantsFunciones.CTE_3)){
//			flujinfScr.setNegocio("");
//		}else{
			flujinfScr.setNegocio((String) key.get(ConstantsFunciones.CTE_3));
		//}
		
//		if(null == (String) key.get(ConstantsFunciones.CTE_5)){
//			flujinfScr.setUoa("");
//		}else{
			flujinfScr.setUoa((String) key.get(ConstantsFunciones.CTE_4));
		//}
		
//		if(null == (String) key.get(ConstantsFunciones.CTE_5)){
//			flujinfScr.setKcarteraContrato("");
//		}else{
			flujinfScr.setKcarteraContrato((String) key.get(ConstantsFunciones.CTE_5));
		//}
		
//		if(null == (String) key.get(ConstantsFunciones.CTE_6)){
//			flujinfScr.setKcarteraCohort("");
//		}else{
			flujinfScr.setKcarteraCohort((String) key.get(ConstantsFunciones.CTE_6));
		//}
		
//		if(null == (String) key.get(ConstantsFunciones.CTE_7)){
//			flujinfScr.setKcarteraOner("");
//		}else{
			flujinfScr.setKcarteraOner((String) key.get(ConstantsFunciones.CTE_7));
		//}
		
		flujinfScr.setFproyflujest((Timestamp) key.get(ConstantsFunciones.CTE_8));
//		if(null == (String) key.get(ConstantsFunciones.CTE_9)){
//			flujinfScr.setKcarterainv("");
//		}else{
			flujinfScr.setKcarterainv((String) key.get(ConstantsFunciones.CTE_9));
		//}
		
//		if(null == (Integer) key.get(ConstantsFunciones.CTE_10)){
//			flujinfScr.setKmodalidad(0);
//		}else{
			flujinfScr.setKmodalidad((Integer) key.get(ConstantsFunciones.CTE_10));
		//}

		/////VALORES SCR
		List values = (List) entrySCR.getValue();
		if(null == (BigDecimal) values.get(ConstantsFunciones.CTE_0)){
			flujinfScr.setTotFpVidaSCR(BigDecimal.ZERO);
		}else{
			flujinfScr.setTotFpVidaSCR((BigDecimal) values.get(ConstantsFunciones.CTE_0));

		}
		if(null == (BigDecimal) values.get(ConstantsFunciones.CTE_1)){
			flujinfScr.setTotFpFallSCR(BigDecimal.ZERO);
		}else{
			flujinfScr.setTotFpFallSCR((BigDecimal) values.get(ConstantsFunciones.CTE_1));
		}
		
		if(null == (BigDecimal) values.get(ConstantsFunciones.CTE_2)){
			flujinfScr.setTotFpGastosSCR(BigDecimal.ZERO);
		}else{
			flujinfScr.setTotFpGastosSCR((BigDecimal) values.get(ConstantsFunciones.CTE_2));
		}
		
		if(null == (BigDecimal) values.get(ConstantsFunciones.CTE_3)){
			flujinfScr.setTotFpComisionesSCR(BigDecimal.ZERO);
		}else{
			flujinfScr.setTotFpComisionesSCR((BigDecimal) values.get(ConstantsFunciones.CTE_3));
		}
		
		if(null == (BigDecimal) values.get(ConstantsFunciones.CTE_4)){
			flujinfScr.setTotFpRescatesSCR(BigDecimal.ZERO);
		}else{
			flujinfScr.setTotFpRescatesSCR((BigDecimal) values.get(ConstantsFunciones.CTE_4));
		}
		
		if(null == (BigDecimal) values.get(ConstantsFunciones.CTE_5)){
			flujinfScr.setTotFpComplSCR(BigDecimal.ZERO);
		}else{
			flujinfScr.setTotFpComplSCR((BigDecimal) values.get(ConstantsFunciones.CTE_5));
		}
		
		if(null == (BigDecimal) values.get(ConstantsFunciones.CTE_6)){
			flujinfScr.setTotFpPrimasSCR(BigDecimal.ZERO);
		}else{
			flujinfScr.setTotFpPrimasSCR((BigDecimal) values.get(ConstantsFunciones.CTE_6));
		}
		
		if(null == (BigDecimal) values.get(ConstantsFunciones.CTE_7)){
			flujinfScr.setTotFpGtoAdSCR(BigDecimal.ZERO);
		}else{
			flujinfScr.setTotFpGtoAdSCR((BigDecimal) values.get(ConstantsFunciones.CTE_7));
		}
		
		flujinfScr.setTotFpSCR(flujinfScr.getTotFpComisionesSCR()
				.add(flujinfScr.getTotFpComplSCR())
				.add(flujinfScr.getTotFpFallSCR())
				.add(flujinfScr.getTotFpGastosSCR())
				.add(flujinfScr.getTotFpGtoAdSCR())
				.subtract(flujinfScr.getTotFpPrimasSCR())
				.add(flujinfScr.getTotFpRescatesSCR())
				.add(flujinfScr.getTotFpVidaSCR()));
		
		return flujinfScr;
	}
}
