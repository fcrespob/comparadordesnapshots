/**
 * MU-2018-016517-Código IM00258226: CORRECCION MODULO CSP037
 * Se incluye una funcion para formatear un string a un numero de carateres.
 */

package es.mapfre.solvencia.formulacion.util;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/**
 * Clase de utilidades genericas
 * 
 */
public final class Util {
	
		
	private Util() { }
	
	/**
	 * <b>Tipo         :</b> On-line <br>
	 * <b>Descripcion  :</b> Redondea un double al numero de decimales indicado
	 * 
	 * @param nD   Numero a redondear
	 * @param nDec Numero de decimales
	 * @return
	 */
	public static double redondearDecimal(final double nRedon, final int nDec)	{
		return Math.round(nRedon * Math.pow(ConstantsFunciones.CTE_10, nDec)) / Math.pow(ConstantsFunciones.CTE_10, nDec); 
	}
	
	/**
	 * Método que aplica BigDecimal.pow() usando el MathContext ajustando la precisión a la que más nos interese obtener
	 * @param base
	 * @param exp
	 * @return
	 */
	public static BigDecimal pow(final BigDecimal base, final int exp) {
		return base.pow(exp, ConstantsFunciones.MATH_CONTEXT);
	}
	
	
	/**
	 * Método que aplica Math.pow() y devuelve un BigDecimal usando el MathContext ajustando la precisión a la que más nos interese obtener
	 * @param base
	 * @param exp
	 * @return
	 */
	public static BigDecimal pow(final BigDecimal base, final double exp) {
		return new BigDecimal(String.valueOf(Math.pow(base.doubleValue(), exp)), ConstantsFunciones.MATH_CONTEXT);
	}
	
	/**
	 * Método que aplica Math.pow() y devuelve un BigDecimal usando el MathContext ajustando la precisión a la que más nos interese obtener
	 * @param base
	 * @param exp
	 * @return
	 */
	public static BigDecimal pow(final BigDecimal base, final BigDecimal exp) {
		return new BigDecimal(String.valueOf(Math.pow(base.doubleValue(), exp.doubleValue())), ConstantsFunciones.MATH_CONTEXT);
	}
	
	/**
	 * Método que aplica Math.pow() y devuelve un BigDecimal usando el MathContext ajustando la precisión a la que más nos interese obtener
	 * @param base
	 * @param exp
	 * @return
	 */
	public static BigDecimal pow(final double base, final int exp) {
		return new BigDecimal(String.valueOf(Math.pow(base, exp)), ConstantsFunciones.MATH_CONTEXT);
	}
	
	/**
	 * Metodo que convierte un double a un BigDecimal
	 * 
	 * @param valor
	 * @return
	 */
	public static BigDecimal obtenerBigDecimal(final double valor) {
		return new BigDecimal(String.valueOf(valor), ConstantsFunciones.MATH_CONTEXT);
	}
	
	
	/**
	 * Funcion para reutilizar en las validaciones
	 * @param campo1		Parametro 1
	 */
	public static String errorValidacionA1(final String campo1) {
		return ConstantsFunciones.CTE_COD_ERROR_A1.concat(ConstantsFunciones.CTE_DES_ERROR_A1).replace(ConstantsFunciones.CTE_NOM_ATR_EN, String.valueOf(campo1));
	}
	
	/**
	 * Funcion para reutilizar en las validaciones
	 * @param codigoError		Código del error
	 * @param descripcionError	Descripción del error
	 */
	public static String errorValidacionA2(final String campo1, final String campo2) {
		return ConstantsFunciones.CTE_COD_ERROR_A2.concat(ConstantsFunciones.CTE_DES_ERROR_A2)
				.replace(ConstantsFunciones.CTE_VAL_ATR_EN, String.valueOf(campo1)).replace(ConstantsFunciones.CTE_NOM_ATR_EN, String.valueOf(campo2));
	}
	/**
	 * Funcion para reutilizar en las validaciones
	 * @param codigoError		Código del error
	 * @param descripcionError	Descripción del error
	 */
	public static String errorValidacionA3(final String campo1, final String campo2) {
		return ConstantsFunciones.CTE_COD_ERROR_A3.concat(ConstantsFunciones.CTE_DES_ERROR_A3)
				.replace(ConstantsFunciones.CTE_VAL_ATR_EN, String.valueOf(campo1)).replace(ConstantsFunciones.CTE_NOM_ATR_EN, String.valueOf(campo2));
	}
	/**
	 * Funcion para reutilizar en las validaciones
	 * @param codigoError		Código del error
	 * @param descripcionError	Descripción del error
	 */
	public static String errorValidacionA4(final String campo1) {
		return ConstantsFunciones.CTE_COD_ERROR_A4.concat(ConstantsFunciones.CTE_DES_ERROR_A4).replace(ConstantsFunciones.CTE_CLA_UMIC_REM, String.valueOf(campo1));
	}
	/**
	 * Funcion para reutilizar en las validaciones
	 * @param codigoError		Código del error
	 * @param descripcionError	Descripción del error
	 */
	public static String errorValidacionA5(final String campo1) {
		return ConstantsFunciones.CTE_COD_ERROR_A5.concat(ConstantsFunciones.CTE_DES_ERROR_A5).replace(ConstantsFunciones.CTE_NOM_APO_REM, String.valueOf(campo1));
	}
	/**
	 * Funcion para reutilizar en las validaciones
	 * @param codigoError		Código del error
	 * @param descripcionError	Descripción del error
	 */
	public static String errorValidacionA6(final String campo1) {
		return ConstantsFunciones.CTE_COD_ERROR_A6.concat(ConstantsFunciones.CTE_DES_ERROR_A6).replace(ConstantsFunciones.CTE_NOM_APO_REM, String.valueOf(campo1));
	}
	/**
	 * Funcion para reutilizar en las validaciones
	 * @param codigoError		Código del error
	 * @param descripcionError	Descripción del error
	 */
	public static String errorValidacionA7(final String campo1) {
		return ConstantsFunciones.CTE_COD_ERROR_A7.concat(ConstantsFunciones.CTE_DES_ERROR_A7).replace(ConstantsFunciones.CTE_CLA_UMIC_REM, String.valueOf(campo1));
	}
	/**
	 * Funcion para reutilizar en las validaciones
	 * @param codigoError		Código del error
	 * @param descripcionError	Descripción del error
	 */
	public static String errorValidacionA8(final String campo1) {
		return ConstantsFunciones.CTE_COD_ERROR_A8.concat(ConstantsFunciones.CTE_DES_ERROR_A8).replace(ConstantsFunciones.CTE_RES_CODK_REM, String.valueOf(campo1));
	}
	/**
	 * Funcion para reutilizar en las validaciones
	 */
	public static String errorValidacionA9() {
		return ConstantsFunciones.CTE_COD_ERROR_A9.concat(ConstantsFunciones.CTE_DES_ERROR_A9);
	}
	
	/**
	 * Funcion para reutilizar en las validaciones
	 * @param campo1		Parametro 1
	 */
	public static String errorValidacionA10(final String campo1) {
		return ConstantsFunciones.CTE_COD_ERROR_A10.concat(ConstantsFunciones.CTE_DES_ERROR_A10).replace(ConstantsFunciones.CTE_CRI_EDAD, String.valueOf(campo1));
	}
	
	/**
	 * Funcion para reutilizar en las validaciones
	 * @param codigoError		Código del error
	 * @param descripcionError	Descripción del error
	 */
	public static String errorValidacionAA() {
		return ConstantsFunciones.CTE_COD_ERROR_AA.concat(ConstantsFunciones.CTE_DES_ERROR_AA);
	}
	
	/**
	 * Funcion para reutilizar en las validaciones
	 * @param codigoError		Código del error
	 * @param descripcionError	Descripción del error
	 */
	public static String errorValidacionAB() {
		return ConstantsFunciones.CTE_COD_ERROR_AB.concat(ConstantsFunciones.CTE_DES_ERROR_AB);
	}
	
	/**
	 * Funcion para reutilizar en las validaciones
	 * @param codigoError		Código del error
	 * @param descripcionError	Descripción del error
	 */
	public static String errorValidacionAF(final String campo1, final String campo2, final String campo3) {
		return ConstantsFunciones.CTE_COD_ERROR_AF.concat(ConstantsFunciones.CTE_DES_ERROR_AF)
				.replace(ConstantsFunciones.CTE_NOM_ATR_EN, String.valueOf(campo1)).replace(ConstantsFunciones.CTE_VAL_ATR_EN, String.valueOf(campo2)).replace(ConstantsFunciones.CTE_TIPO_CORRE, String.valueOf(campo3));
	}
	
	/**
	 * Funcion para reutilizar en las validaciones
	 * @param codigoError		Código del error
	 * @param descripcionError	Descripción del error
	 */
	public static String errorValidacionG1(final String campo1, final String campo2, final String campo3) {
		return ConstantsFunciones.CTE_COD_ERROR_G1.concat(ConstantsFunciones.CTE_DES_ERROR_G1).replace(ConstantsFunciones.CTE_MOD_NOM_REM, String.valueOf(campo1))
				.replace(ConstantsFunciones.CTE_NOM_PARA_EN, String.valueOf(campo2)).replace(ConstantsFunciones.CTE_TIPO_CORRE, String.valueOf(campo3));
	}
	
	/**
	 * Comprueba si el campo es distinto de null, vacio, ceros o espacios
	 * @param campo
	 * @return
	 */
	public static boolean campoDistintoCerosOEspacios(final String campo) {
		return campo != null && !campo.replaceAll(ConstantsFunciones.CTE_0_STRING, ConstantsFunciones.CTE_CADENA_VACIA).trim().isEmpty();
	}
	
	/**
	 * Obtiene la edad como el indice del primer elemento de la lista lstValoresTabMort, en el que su valor sea 0.
	 * La edad a devolver coincide con el indice de la lista, empezando en 0, hasta lstValoresTabMort.size() - 1.
	 * 
	 * @param lstValoresTabMort
	 * @return
	 */
	public static Integer obtenerEdadPrimerCero(final List<BigDecimal> lstValoresTabMort) {
		
		// Se inicializa con el indice del ultimo elemento
		Integer salida = lstValoresTabMort.size() - 1;
		
		// Se recorre la lista en orden inverso
		if (lstValoresTabMort != null) {
			for (int i = lstValoresTabMort.size() - 1; i >= 0; i--) {
				
				if (BigDecimal.ZERO.equals(lstValoresTabMort.get(i))) {
					// Si la ultima fecha sigue siendo 0, se guarda el indice i como la edad a devolver.
					salida = i;
				} else {
					// En cuando se encuentre un elemento distinto de 0,
					// damos por terminada la búsqueda, devolviendo el valor anterior.
					break;
				}
			}
		}
		
		return salida;
	}

	/**
	 * Calcula la interpolación de dos valores para un porcentaje (en tanto por uno) de distribucion.
	 * @param valor1
	 * @param valor2
	 * @param distribucion
	 * @return
	 */
	public static BigDecimal interpolaPorDistribucion(BigDecimal valor1, BigDecimal valor2, BigDecimal distribucion) {
		return valor1.add(distribucion.multiply(valor2.subtract(valor1)));
	}

	/**
	 * Calcula la interpolación de dos valores. La distribución (en tanto por uno) es la parte decimal de la edad pasada como parámetro.
	 * @param valor1
	 * @param valor2
	 * @param edad
	 * @return
	 */
	public static BigDecimal interpolaPorEdad(BigDecimal valor1, BigDecimal valor2, BigDecimal edad) {
		return Util.interpolaPorDistribucion(valor1, valor2, edad.subtract(edad.setScale(0, RoundingMode.DOWN)));
	}

	/**
	 * Multiplica por 12 un entero con desplazamiento de bits.
	 * @param valor1
	 * @return
	 */
	public static int mult12(int valor1) {
		return (valor1 << 3) + (valor1 << 2);
	}


	public static String errorString(final Object obj) {
		String salida = null;
		if (obj != null) {
			return String.valueOf(obj);
		}
		return salida;
	}
	
	public static BigDecimal getVarLx(final BigDecimal varEdad, final List<BigDecimal> lstValoresTabMort){
		
		BigDecimal varLxEntero;
		BigDecimal varLxEntero1;
		BigDecimal varLx;
		Integer edadEntero = varEdad.intValue();
		
		if (edadEntero > lstValoresTabMort.size()-2){
			varLx = BigDecimal.ZERO;
		}else{
			varLxEntero = lstValoresTabMort.get(edadEntero);
			varLxEntero1 =  lstValoresTabMort.get(edadEntero +1);
			varLx = Util.interpolaPorEdad(varLxEntero, varLxEntero1, varEdad);
		}

		return varLx;
	}
	
	/**
	 * 
	 * @param varEdad
	 * @param lstValoresTabMort
	 * @return
	 */
	public static BigDecimal getVarLxEntero(final Integer varEdad, final List<BigDecimal> lstValoresTabMort){
		
		BigDecimal varLx;
		
		if (varEdad > lstValoresTabMort.size()-1){
			varLx = BigDecimal.ZERO;
		}else{
			varLx = lstValoresTabMort.get(varEdad);
		}

		return varLx;
	}
	
	/**
	 * Devuelve una lista simulando una tabla de mortalidad con todas las probabilidades a 1.
	 * @param longitud Longitud de la lista
	 * @return listaProb
	 */
	public static List<BigDecimal> listaProbUno(int longitud){
		List<BigDecimal> listaProb = new ArrayList<BigDecimal>();
		for (int i=0;i<longitud;i++){
			listaProb.add(BigDecimal.ONE);
		}
		return listaProb;
	}
	
	/**
	 * Devuelve el valor pasado a porcentaje y sumado a uno
	 * 
	 * @param num
	 * @return 1 + (num/100)
	 */
	public static BigDecimal numPorcentajeMasUno(final BigDecimal num, final Map<String, Object> mapVariables, final String claveModulo) {
		String clave = num+claveModulo;
		final Object preSalida = mapVariables.get(clave);
		BigDecimal salida;
		
		if (preSalida == null){
			salida = BigDecimal.ONE.add(num.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
			mapVariables.put(clave, salida);
		}else{
			salida = (BigDecimal) preSalida;
		}
		
		return salida;
	}


//INI-816517
	/**
	 * Devuelve el string formateado a las posiciones que se le indican por parametro, añadiendole si es necesario el caracter indicado por parametro
	 * 
	 * @param String a formatera
	 * @param n: numero caracteres final
	 * @param x: caracter para rellenar
	 * @return String formateado
	 */
	public static String stringformateado(final String cadena, final int n, final String x) {
		String salida=cadena;
		while(salida.length() < n)
		{
			salida = x + salida; 
		} 
		
		return salida;
	}
	
//FIN-816517








}

