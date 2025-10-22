package es.mapfre.solvencia.gbt.util;


/**
 * Clase que contiene las constantes necesarias para las funciones auxiliares.
 * 
 * @author rschacon
 * 
 */
public final class ConstantesErrores {

	private ConstantesErrores() {
		
	}
	
	/** Constantes con los tipos de Incidencia */
	public static final String CTE_ERROR = "ERROR";
	public static final String CTE_AVISO = "AVISO";
	
	/** Constantes con los códigos de error */
	// Orquestador
	public static final String CTE_COD_ERROR_MOD_NO_CONT_ORQ = "06";
	public static final String CTE_COD_ERROR_DIP_VACIO = "07";
	public static final String CTE_COD_ERROR_DIP_MAS = "08";
	public static final String CTE_COD_ERROR_SUBP_VACIO = "09";
	public static final String CTE_COD_MODULO_NO_EXISTE = "10";
	public static final String CTE_COD_ERROR_BASETEC = "11";
	
	// Generales
	public static final String CTE_COD_ERROR_MOD_NO_CONT = "00";
	public static final String CTE_COD_ERROR_BT = "01";
	public static final String CTE_COD_ERROR_MOD = "02";
	public static final String CTE_COD_ERROR_FCIERRE = "03";
	public static final String CTE_COD_ERROR_GAR = "04";
	public static final String CTE_COD_ERROR_CANAL = "05";
	public static final String CTE_COD_ERROR_NEG = "06";
	public static final String CTE_COD_ERROR_SUBR = "07";
	public static final String CTE_COD_ERROR_CARTERA = "08";
	public static final String CTE_COD_ERROR_COMPANIA = "09";
	
	// Umic
	public static final String CTE_COD_ERROR_CARTERAINV17 = "10";
	public static final String CTE_COD_ERROR_CARTERAINVLIR = "11";
	
	// J880GBT005
	public static final String CTE_COD_ERROR_TABASEG = "10";
	public static final String CTE_COD_ERROR_CTE_VACIO = "11";
	public static final String CTE_COD_ERROR_CTE_MAS = "12";
	public static final String CTE_COD_ERROR_TABFIN = "13";
	public static final String CTE_COD_ERROR_ATE_MAS = "14";
	public static final String CTE_COD_ERROR_MAD_VACIO = "15";
	public static final String CTE_COD_ERROR_MAD_MAS = "16";
	public static final String CTE_COD_ERROR_PERTOT = "17";
	public static final String CTE_COD_ERROR_FACT_INT = "18";
	
	// J880GBT006
	public static final String CTE_COD_ERROR_GAP = "10";
	public static final String CTE_COD_ERROR_REG = "11";
	public static final String CTE_COD_ERROR_SWCAS = "12";
	public static final String CTE_COD_ERROR_CAS = "13";
	public static final String CTE_COD_ERROR_ITG_VACIO = "14";
	public static final String CTE_COD_ERROR_GAR_VACIO = "15";
	public static final String CTE_COD_ERROR_GAR_MAS = "16";
	public static final String CTE_COD_ERROR_GAP_CAS = "17";
	public static final String CTE_COD_ERROR_CRIT = "18";
	public static final String CTE_COD_ERROR_ITR_VACIO = "19";
	public static final String CTE_COD_ERROR_ITR_MAS = "20";
	public static final String CTE_COD_ERROR_CRIT_NOVALIDO = "21";

	// J880GBT007
	public static final String CTE_COD_DIFGASTOS_VARIOS_REGISTROS = "10";
	public static final String CTE_COD_GTOCAP_MAXIMO = "11";
	public static final String CTE_COD_GTOPRI_MAXIMO = "12";
	public static final String CTE_COD_GTOPROV_MAXIMO = "13";
	public static final String CTE_COD_PGASTGESIN1I_VACIO = "14";
	public static final String CTE_COD_PGASTGESIN2I_VACIO = "15";
	public static final String CTE_COD_PGASTGESIN3I_VACIO = "16";
	public static final String CTE_COD_PDIFEGAP_VACIO = "17";
	public static final String CTE_COD_PDIFEPRP_VACIO = "18";
	public static final String CTE_COD_PDIFERSP_VACIO = "19";

	// J880GBT008
	public static final String CTE_COD_ERROR_SEXO = "10";
	public static final String CTE_COD_ERROR_CAT = "11";
	public static final String CTE_COD_ERROR_EDAD = "12";
	public static final String CTE_COD_ERROR_EDAD_SEXO = "13";
	public static final String CTE_COD_ERROR_ATR_MAS = "14";
	public static final String CTE_COD_ERROR_ATR_VACIO = "15";

	// J880GBT009
	public static final String CTE_COD_ERROR_PB = "10";
	public static final String CTE_COD_ERROR_RRES = "11";
	public static final String CTE_COD_ERROR_FPAGO = "12";
	public static final String CTE_COD_ERROR_ACI_VACIO = "13";
	public static final String CTE_COD_ERROR_ACI_MAS = "14";
	public static final String CTE_COD_ERROR_KCURVA = "15";
	public static final String CTE_COD_ERROR_PER = "16";
	public static final String CTE_COD_ERROR_VCI_VACIO = "17";
	public static final String CTE_COD_ERROR_PER_MET = "18";
	public static final String CTE_COD_ERROR_MVI_VACIO = "19";
	public static final String CTE_COD_ERROR_MVI_MAS = "20";
	public static final String CTE_COD_ERROR_PERPEN = "21";
	public static final String CTE_COD_ERROR_PERUT = "22";
	public static final String CTE_COD_ERROR_FACT_INT2 = "23";
	public static final String CTE_COD_ERROR_KAPBEL = "24";
	public static final String CTE_COD_ERROR_CTU_VACIO = "25";
	// J880GBT010
	public static final String CTE_COD_ERROR_RAMO = "10";
	public static final String CTE_COD_GASTOSREALES_NO_RESULTS = "11";
	public static final String CTE_COD_GASTOSREALES_VARIOS_REGISTROS = "12";

	// J880GBT011
	public static final String CTE_COD_ERROR_RAMO2 = "10";
	public static final String CTE_COD_TABLAANU_VACIA = "11";
	public static final String CTE_COD_FINITRAMO_VACIO = "12";
	public static final String CTE_COD_FFINTRAMO_VACIO = "13";
	public static final String CTE_COD_ASIGTASASANU_NO_RESULTS = "14";
	public static final String CTE_COD_ASIGTASASANU_VARIOS_REGISTROS = "15";
	public static final String CTE_COD_ASIGTASASANU_NO_TASA_ANUL = "16";
	public static final String CTE_COD_TASASANU_NO_RESULTS = "17";
	public static final String CTE_COD_TRAMO_VACIO = "18";
	public static final String CTE_COD_TRAMO_VARIOS = "19";
	public static final String CTE_COD_INTECN_VACIO = "20";
	
	// J880GBT012
	public static final String CTE_COD_ERROR_NO_BT_NIIIF17 = "10";
	public static final String CTE_COD_ERROR_BT_VALORES = "11";
	public static final String CTE_COD_ERROR_NO_BT_VALORES = "12";
	
	/** Constantes con las descripciones cortas de error */
	// Orquestador
	public static final String CTE_DESC_ERROR_DIP_VACIO = "Ningún registro en T880DIP0";
	public static final String CTE_DESC_ERROR_DIP_MAS = "Solapamiento en T880DIP0";
	public static final String CTE_DESC_ERROR_SUBP_VACIO = "Ningún subproceso encontrado";
	// public static final String CTE_DESC_MODULO_NO_EXISTE = "Subproceso inexistente";
	public static final String CTE_DESC_ERROR_BASETEC = "Módulo BASE_TEC parametrizado";
	
	// Generales
	public static final String CTE_DESC_ERROR_MOD_NO_CONT = "Error no controlado";
	public static final String CTE_DESC_ERROR_BT = "Base técnica no informada";
	public static final String CTE_DESC_ERROR_MOD = "Modalidad no informada";
	public static final String CTE_DESC_ERROR_FCIERRE = "Fecha cierre no informada";
	public static final String CTE_DESC_ERROR_GAR = "Garantía no informada";
	public static final String CTE_DESC_ERROR_CANAL = "Canal no informado";
	public static final String CTE_DESC_ERROR_NEG = "Negocio no informado";
	public static final String CTE_DESC_ERROR_SUBR = "Subriesgo no informado";
	public static final String CTE_DESC_ERROR_CARTERA = "Cartera no informada";
	public static final String CTE_DESC_ERROR_COMPANIA = "Compañía no informada";
	
	// Umic
	public static final String CTE_DESC_ERROR_CARTERAINV17 = "Cartera inversion NIIF17 no informada";
	public static final String CTE_DESC_ERROR_CARTERAINVLIR = "Cartera inversion LIR no informada";
	
	// J880GBT005
	public static final String CTE_DESC_ERROR_TABASEG = "Tabla asegurado no informada";
	public static final String CTE_DESC_ERROR_CTE_VACIO = "Ningún registro en T340CTE0";
	public static final String CTE_DESC_ERROR_CTE_MAS = "Solapamiento en T340CTE0";
	public static final String CTE_DESC_ERROR_TABFIN = "La tabla fin no informada";
	public static final String CTE_DESC_ERROR_ATE_MAS = "Solapamiento en T340ATE0";
	public static final String CTE_DESC_ERROR_MAD_VACIO = "Ningún registro en T340MAD0";
	public static final String CTE_DESC_ERROR_MAD_MAS = "Solapamiento en T340MAD0";
	public static final String CTE_DESC_ERROR_PERTOT = "Periodos totales no informado";
	public static final String CTE_DESC_ERROR_FACT_INT = "Factor de interpol. mayor a 1";
	
	// J880GBT006
	public static final String CTE_DESC_ERROR_GAP = "Grupo act/pas no informado";
	public static final String CTE_DESC_ERROR_REG = "Reglamento no informado";
	public static final String CTE_DESC_ERROR_SWCAS = "Swcasado no informado";
	public static final String CTE_DESC_ERROR_CAS = "Campo casado no informado";
	public static final String CTE_DESC_ERROR_ITG_VACIO = "Ningún registro en T340ITG0";
	public static final String CTE_DESC_ERROR_GAR_VACIO = "Ningún registro en T340GAR0";
	public static final String CTE_DESC_ERROR_GAR_MAS = "Solapamiento en T340GAR0";
	public static final String CTE_DESC_ERROR_GAP_CAS = "Act/pas y casado obligatorios";
	public static final String CTE_DESC_ERROR_CRIT = "Criterio int téc no informado";
	public static final String CTE_DESC_ERROR_ITR_VACIO = "Ningún registro en T340ITR0";
	public static final String CTE_DESC_ERROR_ITR_MAS = "Solapamiento en T340ITR0";
	public static final String CTE_DESC_ERROR_CRIT_NOVALIDO = "Criterio int. téc. no existe";

	// J880GBT007
	public static final String CTE_DESC_DIFGASTOS_VARIOS_REGISTROS = "Solapamiento en T340DGI0";
	public static final String CTE_DESC_GTOCAP_MAXIMO = "Gastos Capital mayor a máximo";
	public static final String CTE_DESC_GTOPRI_MAXIMO = "Gastos Prima mayor a máximo";
	public static final String CTE_DESC_GTOPROV_MAXIMO = "Gastos Provis. mayor a máximo";
	public static final String CTE_DESC_PGASTGESIN1I_VACIO = "Porc. Gastos Capital no infor";
	public static final String CTE_DESC_PGASTGESIN2I_VACIO = "Porc. Gastos Prima no infor";
	public static final String CTE_DESC_PGASTGESIN3I_VACIO = "Porc. Gastos Provis. no infor";
	public static final String CTE_DESC_PDIFEGAP_VACIO = "Dife. Gastos Capital no infor";
	public static final String CTE_DESC_PDIFEPRP_VACIO = "Dife. Gastos Prima no infor";
	public static final String CTE_DESC_PDIFERSP_VACIO = "Dife. Gastos Provis. no infor";

	// J880GBT008
	public static final String CTE_DESC_ERROR_SEXO = "Sexo no informado";
	public static final String CTE_DESC_ERROR_CAT = "Categoría no informada";
	public static final String CTE_DESC_ERROR_EDAD = "Edad no informada";
	public static final String CTE_DESC_ERROR_EDAD_SEXO = "Diferencia entre sexo y edad";
	public static final String CTE_DESC_ERROR_ATR_MAS = "Solapamiento en T340ATR0";
	public static final String CTE_DESC_ERROR_ATR_VACIO = "Ningún registro en T340ATR0";

	// J880GBT009
	public static final String CTE_DESC_ERROR_PB = "PB no informado";
	public static final String CTE_DESC_ERROR_RRES = "Riesgo rescate  no informado";
	public static final String CTE_DESC_ERROR_FPAGO = "Forma de pago  no informada";
	public static final String CTE_DESC_ERROR_ACI_VACIO = "Ningún registro en T340ACI0";
	public static final String CTE_DESC_ERROR_ACI_MAS = "Solapamiento en T340ACI0";
	public static final String CTE_DESC_ERROR_KCURVA = "Código de curva no informado";
	public static final String CTE_DESC_ERROR_PER = "Período no informado";
	public static final String CTE_DESC_ERROR_VCI_VACIO = "Ningún registro en T340VCI0";
	public static final String CTE_DESC_ERROR_PER_MET = "Período informado sin método";
	public static final String CTE_DESC_ERROR_MVI_VACIO = "Ningún registro en T340MVI0";
	public static final String CTE_DESC_ERROR_MVI_MAS = "Solapamiento en T340MVI0";
	public static final String CTE_DESC_ERROR_PERPEN = "Períodos ptes no informados";
	public static final String CTE_DESC_ERROR_PERUT = "Períodos útiles no informados";
	public static final String CTE_DESC_ERROR_FACT_INT2 = "Factor de interpol. mayor a 1";
	public static final String CTE_DESC_ERROR_KAPBEL = "Error al calcular KAPBEL";
	public static final String CTE_DESC_ERROR_CTU_VACIO = "Ningún registro en la tabla CTU0";
	
	// J880GBT010
	public static final String CTE_DESC_ERROR_RAMO = "Ramo no informado";
	public static final String CTE_DESC_GASTOSREALES_NO_RESULTS = "Ningún registro en T340GRE0";
	public static final String CTE_DESC_GASTOSREALES_VARIOS_REGISTROS = "Solapamiento en T340GRE0";

	// J880GBT011
	public static final String CTE_DESC_ERROR_RAMO2 = "Ramo no informado";
	public static final String CTE_DESC_TABLAANU_VACIA = "Tabla Anulación no informada";
	public static final String CTE_DESC_FINITRAMO_VACIO = "Fecha ini tramo no informada";
	public static final String CTE_DESC_FFINTRAMO_VACIO = "Fecha fin tramo no informada";
	public static final String CTE_DESC_ASIGTASASANU_NO_RESULTS = "Ningún registro en T340ATA0";
	public static final String CTE_DESC_ASIGTASASANU_VARIOS_REGISTROS = "Solapamiento en T340ATA0";
	public static final String CTE_DESC_ASIGTASASANU_NO_TASA_ANUL = "Tabla de anulación no informada";
	public static final String CTE_DESC_TASASANU_NO_RESULTS = "Ningún registro en T340VTA0";
	public static final String CTE_DESC_TRAMO_VACIO = "Ningún tramo para la f cierre";
	public static final String CTE_DESC_TRAMO_VARIOS = "Solapamiento en T340VTA0";
	public static final String CTE_DESC_INTECN_VACIO = "Porc interés no informado";
	
	//J880GBT012
	public static final String CTE_DESC_ERROR_NO_BT_NIIF17 = "Base Técnica incorrecta, la bt no es NIIF17";
	public static final String CTE_DESC_ERROR_BT_VALORES = "Mas de un valor encontrado para la tabla CTU0";
	public static final String CTE_DESC_ERROR_NO_BT_VALORES = "No se han encontrado valores para la tabla CTU0";
	
	
	
	/** Constantes con las descripciones largas de error */
	// Orquestador
	public static final String CTE_DESL_ERROR_DIP_VACIO = "No se ha encontrado ningún registro en la tabla Diseño Procesos";
	public static final String CTE_DESL_ERROR_DIP_MAS = "Se ha encontrado más de un registro en la tabla Diseño Procesos";
	public static final String CTE_DESL_ERROR_SUBP_VACIO = "No se han econtrado subprocesos para el registro de diseño procesos";
	// public static final String CTE_DESL_MODULO_NO_EXISTE = "Alguno de los módulos parametrizados para el proceso BASE_TEC no existe";
	public static final String CTE_DESL_ERROR_BASETEC = "Se ha encontrado BASE_TEC entre los módulos a ejecutar, se produciría un bucle";
	
	// Generales
	public static final String CTE_DESL_ERROR_MOD_NO_CONT = "Se ha generado un error no controlado en el módulo";
	public static final String CTE_DESL_ERROR_BT = "No se ha informado el campo base técnica";
	public static final String CTE_DESL_ERROR_MOD = "No se ha informado el campo modalidad";
	public static final String CTE_DESL_ERROR_FCIERRE = "No se ha informado el campo fecha cierre";
	public static final String CTE_DESL_ERROR_GAR = "No se ha informado el campo garantía";
	public static final String CTE_DESL_ERROR_CANAL = "No se ha informado el campo canal";
	public static final String CTE_DESL_ERROR_NEG = "No se ha informado el campo negocio";
	public static final String CTE_DESL_ERROR_SUBR = "No se ha informado el campo subriesgo";
	public static final String CTE_DESL_ERROR_CARTERA = "No se ha informado el campo cartera";
	public static final String CTE_DESL_ERROR_COMPANIA = "No se ha informado el campo compañía";
	
	// Umic
	public static final String CTE_DESL_ERROR_CARTERAINV17 = "No se ha informado el campo cartera de inversion NIIF17";
	public static final String CTE_DESL_ERROR_CARTERAINVLIR = "No se ha informado el campo cartera de inversion LIR";

	
	// J880GBT005
	public static final String CTE_DESL_ERROR_TABASEG = "No se ha informado el campo tabla 1 del asegurado 1";
	public static final String CTE_DESL_ERROR_CTE_VACIO = "Registro no encontrado en tabla Criterios de conversión tablas de experiencia";
	public static final String CTE_DESL_ERROR_CTE_MAS = "Solapamiento encontrado en tabla Criterios de conversión tablas de experiencia";
	public static final String CTE_DESL_ERROR_TABFIN = "La tabla fin no viene informada en el registro";
	public static final String CTE_DESL_ERROR_ATE_MAS = "Solapamiento encontrado en tabla Adaptación tabla de experiencia";
	public static final String CTE_DESL_ERROR_MAD_VACIO = "Registro no encontrado en tabla Períodos de adaptación";
	public static final String CTE_DESL_ERROR_MAD_MAS = "Solapamiento encontrado en tabla Períodos de adaptación";
	public static final String CTE_DESL_ERROR_PERTOT = "El número de períodos totales no viene informado en el registro";
	public static final String CTE_DESL_ERROR_FACT_INT = "El factor de interpolación no puede ser mayor que 1";
	
	// J880GBT006
	public static final String CTE_DESL_ERROR_GAP = "No se ha informado el campo grupo activo/pasivo";
	public static final String CTE_DESL_ERROR_REG = "No se ha informado el campo reglamento";
	public static final String CTE_DESL_ERROR_SWCAS = "No se ha informado el campo swcasado";
	public static final String CTE_DESL_ERROR_CAS = "No se han informado el campo casado";
	public static final String CTE_DESL_ERROR_ITG_VACIO = "Registro no encontrado en tabla Asig. de int. técnico ITG0";
	public static final String CTE_DESL_ERROR_GAR_VACIO = "No se ha encontrado ningún registro en la tabla Rentabilidad por GAP";
	public static final String CTE_DESL_ERROR_GAR_MAS = "Solapamiento encontrado en tabla Rensabilidad por GAP";
	public static final String CTE_DESL_ERROR_GAP_CAS = "No se han informado los campos grupo activo/pasivo y casado";
	public static final String CTE_DESL_ERROR_CRIT = "El criterio de interés técnico no viene informado en el registro";
	public static final String CTE_DESL_ERROR_ITR_VACIO = "No se ha encontrado ningún registro en la tabla Interés técnico DGS";
	public static final String CTE_DESL_ERROR_ITR_MAS = "Se ha encontrado más de un registro en la tabla Interés técnico DGS";
	public static final String CTE_DESL_ERROR_CRIT_NOVALIDO = "El criterio de interés técnico no existe";

	// J880GBT007
	public static final String CTE_DESL_DIFGASTOS_VARIOS_REGISTROS = "Existen varios registros en la tabla Diferencial de Gastos";
	public static final String CTE_DESL_GTOCAP_MAXIMO = "El valor del campo Gastos Capital es mayor al valor máximo permitido";
	public static final String CTE_DESL_GTOPRI_MAXIMO = "El valor del campo Gastos Prima es mayor al valor máximo permitido";
	public static final String CTE_DESL_GTOPROV_MAXIMO = "El valor del campo Gastos Provisión es mayor al valor máximo permitido";
	public static final String CTE_DESL_PGASTGESIN1I_VACIO = "Campo Porcentaje Gastos Capital no informado";
	public static final String CTE_DESL_PGASTGESIN2I_VACIO = "Campo Porcentaje Gastos Prima no informado";
	public static final String CTE_DESL_PGASTGESIN3I_VACIO = "Campo Porcentaje Gastos Provisión no informado";
	public static final String CTE_DESL_PDIFEGAP_VACIO = "Campo Diferencial Gastos Capital no informado";
	public static final String CTE_DESL_PDIFEPRP_VACIO = "Campo Diferencial Gastos Prima no informado";
	public static final String CTE_DESL_PDIFERSP_VACIO = "Campo Diferencial Gastos Provisión no informado";
	
	// J880GBT008
	public static final String CTE_DESL_ERROR_SEXO = "No se ha informado el campo sexo";
	public static final String CTE_DESL_ERROR_CAT = "No se ha informado el campo categoría";
	public static final String CTE_DESL_ERROR_EDAD = "No se ha informado el campo edad";
	public static final String CTE_DESL_ERROR_EDAD_SEXO = "Se ha informado distinto número de sexo que de edad";
	public static final String CTE_DESL_ERROR_ATR_MAS = "Solapamiento en tabla Criterios conversión tablas de experiencia real";
	public static final String CTE_DESL_ERROR_ATR_VACIO = "Registro no encontrado en tabla Criterios conversión tablas de experiencia real";

	// J880GBT009
	public static final String CTE_DESL_ERROR_PB = "No se ha informado el campo PB";
	public static final String CTE_DESL_ERROR_RRES = "No se ha informado el campo riesgo rescate";
	public static final String CTE_DESL_ERROR_FPAGO = "No se ha informado el campo forma de pago";
	public static final String CTE_DESL_ERROR_ACI_VACIO = "Registro no encontrado en tabla Asignación de curvas tipo";
	public static final String CTE_DESL_ERROR_ACI_MAS = "Solapamiento en tabla Asignación de curvas tipo";
	public static final String CTE_DESL_ERROR_KCURVA = "El código de curva no viene informado en el registro";
	public static final String CTE_DESL_ERROR_PER = "El período no viene informado en el registro";
	public static final String CTE_DESL_ERROR_VCI_VACIO = "Registro no encontrado en tabla Valores de curvas de tipo de interés";
	public static final String CTE_DESL_ERROR_PER_MET = "Existe período transitorio pero no se ha informado el método";
	public static final String CTE_DESL_ERROR_MVI_VACIO = "Existe período transitorio sin método en la tabla Valores métodos de adaptación";
	public static final String CTE_DESL_ERROR_MVI_MAS = "Solapamiento en tabla Valores métodos de adaptación";
	public static final String CTE_DESL_ERROR_PERPEN = "El número de períodos pendientes no viene informado en el registro";
	public static final String CTE_DESL_ERROR_PERUT = "El número de períodos útiles no viene informado en el registro";
	public static final String CTE_DESL_ERROR_FACT_INT2 = "El factor de interpolación no puede ser mayor que 1";
	public static final String CTE_DESL_ERROR_KAPBEL = "Error al calcular el indicador KAPBEL";
	public static final String CTE_DESL_ERROR_CTU_VACIO = "Existe período transitorio sin método en la tabla Curvas Tipo NIIF17";
	
	// J880GBT010
	public static final String CTE_DESL_ERROR_RAMO = "Campo Ramo no informado";
	public static final String CTE_DESL_GASTOSREALES_NO_RESULTS = "No existe tramo en la tabla Gastos Reales";
	public static final String CTE_DESL_GASTOSREALES_VARIOS_REGISTROS = "Existen varios registros en la tabla Gastos Reales";

	// J880GBT011
	public static final String CTE_DESL_ERROR_RAMO2 = "Campo Ramo no informado";
	public static final String CTE_DESL_TABLAANU_VACIA = "Campo Tabla de Anulación no informado";
	public static final String CTE_DESL_FINITRAMO_VACIO = "Campo fecha inicio tramo no informado";
	public static final String CTE_DESL_FFINTRAMO_VACIO = "Campo fecha fin tramo no informado";
	public static final String CTE_DESL_ASIGTASASANU_NO_RESULTS = "No existe tramo en Tab. Asig. Tasas Anul. ATA0";
	public static final String CTE_DESL_ASIGTASASANU_VARIOS_REGISTROS = "Existen varios registros en Tab. Asig. Tasas Anul. ATA0";
	public static final String CTE_DESL_ASIGTASASANU_NO_TASA_ANUL = "Tabla de asignaciones no informada en tabla Asignación de Tasas de Anulación";
	public static final String CTE_DESL_TASASANU_NO_RESULTS = "No existe tramo en la tabla de Tasas de Anulación";
	public static final String CTE_DESL_TRAMO_VACIO = "No existe Tramo para la Fecha Cierre introducida";
	public static final String CTE_DESL_TRAMO_VARIOS = "Existen varios Tramos para la Fecha Cierre introducida";
	public static final String CTE_DESL_INTECN_VACIO = "No existe Porcentaje de Interes Técnico para este tramo";
	
	//J880GBT012
	public static final String CTE_DESL_ERROR_BT_NO_NIIF17 = "La base tecnica en ejecucion no pertence a las bases tecnicas NIIF17";
	public static final String CTE_DESL_ERROR_BT_VALORES = "Se ha encontrado mas de un valor en la tabla CTU0 para la busqueda realizada";
	public static final String CTE_DESL_ERROR_NO_BT_VALORES = "No se han encontrado valores en la tabla CTU0 para la busqueda realizada";

	
}
