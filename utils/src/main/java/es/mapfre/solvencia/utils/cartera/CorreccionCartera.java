package es.mapfre.solvencia.utils.cartera;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Umic;

public class CorreccionCartera {
	
	private static Logger log = LoggerFactory.getLogger(CorreccionCartera.class);
	
	public static Umic corregirUmic(Umic umic){
		
		//Se corrige la cartera para obtener la tabla1 del asegurado 2, que viene informada en la tabla2 del asegurado 1.
		if(ConstantesSolvencia.MODALIDADES_RENTAS_2C.contains(umic.getDatosGenerales().getKmodalidad())){
			log.info("Corrección de cartera: Corrección tabla de Asegurado2");
			umic.getBti().setTabla1Aseg2(umic.getBti().getTabla2Aseg1());
		}

		return umic;
		
	}

}
