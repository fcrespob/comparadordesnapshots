package es.mapfre.solvencia.filtros.impl;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FiltroFichaProceso;
// JBMARTA - PYAM0025 - INI
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FiltroFichaProcesoAdicional;
// JBMARTA - PYAM0025 - FIN
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;

/**
 * Clase que recoge las constantes que identifican los distintos tipos de operadores, así como
 * las validaciones para comprobar las combinaciones de operadores permitidas para los diferentes
 * tipos de filtro.
 * 
 * @author indra
 *
 */
public class Operadores {
	
	public static final String OPERADOR_IGUAL = "IG";
	
	public static final String OPERADOR_MAYOR_IGUAL = "MG";
	
	public static final String OPERADOR_MENOR_IGUAL = "MI";
	
	public static final String OPERADOR_MENOR = "ME";
	
	public static final String OPERADOR_MAYOR = "MA";
	
	
	
	private Operadores() {
	}

	/**
	 * Método encargado de validar los operadores para un registro FiltroFichaProceso de una
	 * ficha de proceso cuyo valor 
	 * 
	 * @param filtro
	 * @return
	 * @throws Exception
	 */
	public static Boolean validarOperadoresInteger(FiltroFichaProceso filtro) throws Solvencia2Excepcion { //NOSONAR
	   /**
		* Combinaciones de operadores para Integer (Inclusión y Exclusión)
		* ----------------------------------------------------------------
		* Desde 	Hasta
		* IG (=) 	---
		* ME (<) 	---
		* MI (<=) 	---
		* MA (>) 	---
		* MA (>) 	ME (<)
		* MA (>) 	MI (<=)
		* MG (>=) 	---
		* MG (>=) 	ME (<)
		* MG (>=) 	MI (<=)
		*/
		
		/**
		 * El operador desde nunca puede venir vacío
		 */
		if (filtro.getCoperadord() == null) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_OPERADOR_DESDE_NULO, new Object[]{null});
		} else {
			switch (filtro.getCoperadord()) {
                case OPERADOR_IGUAL: 
					if (filtro.getCoperadorh() != null) {
					    // JBMARTA - PYAM0025 - INI
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_OPERADOR_HASTA_NO_PERMITIDO, new Object[]{filtro.getCoperadord(), filtro.getCoperadorh()});
						// JBMARTA - PYAM0025 - FIN
					} 
					break;
				case OPERADOR_MENOR: 
					if (filtro.getCoperadorh() != null) {
						// JBMARTA - PYAM0025 - INI
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_OPERADOR_HASTA_NO_PERMITIDO, new Object[]{filtro.getCoperadord(), filtro.getCoperadorh()});
						// JBMARTA - PYAM0025 - FIN
					} break;
				case OPERADOR_MAYOR_IGUAL: 
					if ((filtro.getCoperadorh() != null) && 
							((!filtro.getCoperadorh().equalsIgnoreCase(Operadores.OPERADOR_MENOR) && 
								(!filtro.getCoperadorh().equalsIgnoreCase(Operadores.OPERADOR_MENOR_IGUAL))))) {
						// JBMARTA - PYAM0025 - INI
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_OPERADOR_HASTA_NO_PERMITIDO, new Object[]{filtro.getCoperadord(), filtro.getCoperadorh()});
						// JBMARTA - PYAM0025 - FIN
					} break;			
				case OPERADOR_MENOR_IGUAL: 
					if (filtro.getCoperadorh() != null) {
						// JBMARTA - PYAM0025 - INI
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_OPERADOR_HASTA_NO_PERMITIDO, new Object[]{filtro.getCoperadord(), filtro.getCoperadorh()});
						// JBMARTA - PYAM0025 - FIN
					} break;
				case OPERADOR_MAYOR: 
					if ((filtro.getCoperadorh() != null) && 
							((!filtro.getCoperadorh().equalsIgnoreCase(Operadores.OPERADOR_MENOR) && 
								(!filtro.getCoperadorh().equalsIgnoreCase(Operadores.OPERADOR_MENOR_IGUAL))))) {
						// JBMARTA - PYAM0025 - INI
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_OPERADOR_HASTA_NO_PERMITIDO, new Object[]{filtro.getCoperadord(), filtro.getCoperadorh()});
						// JBMARTA - PYAM0025 - FIN
					} break;
			}
		}
		return true;
	}
	
	// JBMARTA - PYAM0025 - INI
	/**
	 * Método encargado de validar los operadores para un registro FiltroFichaProcesoAdicional de una
	 * ficha de proceso cuyo valor 
	 * 
	 * @param filtro
	 * @return
	 * @throws Exception
	 */
	public static Boolean validarOperadoresIntegerAdicional(FiltroFichaProcesoAdicional filtro) throws Solvencia2Excepcion { //NOSONAR
	   /**
		* Combinaciones de operadores para Integer (Inclusión y Exclusión)
		* ----------------------------------------------------------------
		* Desde 	Hasta
		* IG (=) 	---
		* ME (<) 	---
		* MI (<=) 	---
		* MA (>) 	---
		* MA (>) 	ME (<)
		* MA (>) 	MI (<=)
		* MG (>=) 	---
		* MG (>=) 	ME (<)
		* MG (>=) 	MI (<=)
		*/
		
		/**
		 * El operador desde nunca puede venir vacío
		 */
		if (filtro.getGoperdesde() == null) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_OPERADOR_DESDE_NULO, new Object[]{null});
		} else {
			switch (filtro.getGoperdesde()) {
                case OPERADOR_IGUAL: 
					if (filtro.getGoperhasta() != null) {
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_OPERADOR_HASTA_NO_PERMITIDO, new Object[]{filtro.getGoperdesde(), filtro.getGoperhasta()});
					} 
					break;
				case OPERADOR_MENOR: 
					if (filtro.getGoperhasta() != null) {
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_OPERADOR_HASTA_NO_PERMITIDO, new Object[]{filtro.getGoperdesde(), filtro.getGoperhasta()});
					} break;
				case OPERADOR_MAYOR_IGUAL: 
					if ((filtro.getGoperhasta() != null) && 
							((!filtro.getGoperhasta().equalsIgnoreCase(Operadores.OPERADOR_MENOR) && 
								(!filtro.getGoperhasta().equalsIgnoreCase(Operadores.OPERADOR_MENOR_IGUAL))))) {
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_OPERADOR_HASTA_NO_PERMITIDO, new Object[]{filtro.getGoperdesde(), filtro.getGoperhasta()});
					} break;			
				case OPERADOR_MENOR_IGUAL: 
					if (filtro.getGoperhasta() != null) {
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_OPERADOR_HASTA_NO_PERMITIDO, new Object[]{filtro.getGoperdesde(), filtro.getGoperhasta()});
					} break;
				case OPERADOR_MAYOR: 
					if ((filtro.getGoperhasta() != null) && 
							((!filtro.getGoperhasta().equalsIgnoreCase(Operadores.OPERADOR_MENOR) && 
								(!filtro.getGoperhasta().equalsIgnoreCase(Operadores.OPERADOR_MENOR_IGUAL))))) {
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_OPERADOR_HASTA_NO_PERMITIDO, new Object[]{filtro.getGoperdesde(), filtro.getGoperhasta()});
					} break;
			}
		}
		return true;
	}
	// JBMARTA - PYAM0025 - FIN
	
	// JBMARTA - PYAM0025 - INI
	/**
	 * Método encargado de validar los operadores para un registro FiltroFichaProcesoAdicional de una
	 * ficha de proceso cuyo valor 
	 * 
	 * @param filtro
	 * @return
	 * @throws Exception
	 */
	public static Boolean validarOperadoresIntegerAdicionalGarantia (FiltroFichaProcesoAdicional filtro) throws Solvencia2Excepcion { //NOSONAR
	   /**
		* Combinaciones de operadores para Integer (Inclusión y Exclusión)
		* ----------------------------------------------------------------
		* Desde 	Hasta
		* IG (=) 	---
		* ME (<) 	---
		* MI (<=) 	---
		* MA (>) 	---
		* MA (>) 	ME (<)
		* MA (>) 	MI (<=)
		* MG (>=) 	---
		* MG (>=) 	ME (<)
		* MG (>=) 	MI (<=)
		*/
		
		/**
		 * El operador desde nunca puede venir vacío
		 */
		if (filtro.getGopdesdegar() == null) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_OPERADOR_DESDE_NULO, new Object[]{null});
		} else {
			switch (filtro.getGopdesdegar()) {
                case OPERADOR_IGUAL: 
					if (filtro.getGophastagar() != null) {
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_OPERADOR_HASTA_NO_PERMITIDO, new Object[]{filtro.getGopdesdegar(), filtro.getGophastagar()});
                  } 
					break;
				case OPERADOR_MENOR: 
					if (filtro.getGophastagar() != null) {
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_OPERADOR_HASTA_NO_PERMITIDO, new Object[]{filtro.getGopdesdegar(), filtro.getGophastagar()});
					} break;
				case OPERADOR_MAYOR_IGUAL: 
					if ((filtro.getGophastagar() != null) && 
							((!filtro.getGophastagar().equalsIgnoreCase(Operadores.OPERADOR_MENOR) && 
								(!filtro.getGophastagar().equalsIgnoreCase(Operadores.OPERADOR_MENOR_IGUAL))))) {
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_OPERADOR_HASTA_NO_PERMITIDO, new Object[]{filtro.getGopdesdegar(), filtro.getGophastagar()});
					} break;			
				case OPERADOR_MENOR_IGUAL: 
					if (filtro.getGophastagar() != null) {
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_OPERADOR_HASTA_NO_PERMITIDO, new Object[]{filtro.getGopdesdegar(), filtro.getGophastagar()});
					} break;
				case OPERADOR_MAYOR: 
					if ((filtro.getGophastagar() != null) && 
							((!filtro.getGophastagar().equalsIgnoreCase(Operadores.OPERADOR_MENOR) && 
								(!filtro.getGophastagar().equalsIgnoreCase(Operadores.OPERADOR_MENOR_IGUAL))))) {
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_OPERADOR_HASTA_NO_PERMITIDO, new Object[]{filtro.getGopdesdegar(), filtro.getGophastagar()});
					} break;
			}
		}
		return true;
	}
	// JBMARTA - PYAM0025 - FIN
	
	// JBMARTA - PYAM0025 - INI
	public static Boolean validarOperadoresString(FiltroFichaProcesoAdicional filtro) throws Solvencia2Excepcion {
			
	   /**
		* Combinaciones de Operadores para String (Inclusión y Exclusión)
		*----------------------------------------------------------------
		* Desde			Hasta
		* IG (=)		---
		*/
		if (filtro.getGoperdesde() == null) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_OPERADOR_DESDE_NULO, new Object[]{null});
		} else {
			if (!filtro.getGoperdesde().equalsIgnoreCase(Operadores.OPERADOR_IGUAL)) {
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_OPERADOR_DESDE_NO_PERMITIDO_EN_TIPO_STRING, new Object[]{filtro.getGoperdesde()});
			} else {
				if (filtro.getGoperhasta() != null) {
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_OPERADOR_HASTA_NO_PERMITIDO_EN_TIPO_STRING, new Object[]{filtro.getGoperhasta()});
				}
			}
		}
		return true;
	}
	// JBMARTA - PYAM0025 - FIN
}