package es.mapfre.solvencia.formulacion.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.LimitesCapital;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;

public class Terminales {
	
	/** 
	 * Log terminales.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(Terminales.class);
	
	/**
	 * Cte de clase.
	 */
	private static final String CLAVE_VARVRTA4 = "varVrta4";
	
	/**
	 * Función que resuelve  el cálculo de PROVISIONES de los productos que carecen de terminales. 
	 * 
	 * @return vtx001 Terminal de la provisión.
	 */
	public static BigDecimal vtx001(){
		//Inicio variables locales
		BigDecimal vtx001;
		//Fin variables locales	
		
		if (Terminales.LOG.isTraceEnabled()) {
			Terminales.LOG.trace("Inicio Funcion << VTX001 >> de la clase Terminales");
		}
		
		vtx001 = BigDecimal.ZERO;
		
		if (Terminales.LOG.isTraceEnabled()) {
			Terminales.LOG.trace("Fin Funcion << VTX001  >> de la clase Terminales, con resultado vtx001 = {} ", vtx001);
		}
		
		return vtx001;
	}
	
	/**
	 * Vx(Tc)= Capital(Tc)*[DIF(x,Tc,n,I1)/(1-((l[x+Tc]-l[x+n]) * (1+I1/100)^-0.5/l[x+Tc])) + Gic/100* AX(0,x,Tc,n,I1)]
	 * 
	 * @param capitalGar
	 * 				Capital de la garantía
	 * @param lstValoresTabMort
	 * 				Valores de la tabla de mortalidad
	 * @param gic
	 * 				Gastos de gestión interna sobre Capital
	 * @param interes1
	 * 				Interés 1 de la base contable tratada
	 * @param difer
	 * 				Diferimiento de la renta
	 * @param varx
	 * 				Edad x del asegurado
	 * @param varp
	 * 				Desplazamiento de la renta
	 * @param varn
	 * 				Duración del seguro en años
	 * @return vtx002
	 */
	public static BigDecimal vtx002(final BigDecimal capitalGar, final List<BigDecimal> lstValoresTabMort, final BigDecimal gic, 
			final BigDecimal interes1, final Integer difer, final Integer varx, final Integer varp, final Integer varn) {
		// Variables locales
		BigDecimal vtx002 = BigDecimal.ZERO;
		BigDecimal lXMasP = BigDecimal.ZERO;
		BigDecimal lXMasN = BigDecimal.ZERO;
		BigDecimal operando1 = BigDecimal.ZERO;
		BigDecimal operando2 = BigDecimal.ZERO;
		BigDecimal denoOperando1 = BigDecimal.ZERO;
		BigDecimal numeDenoOperand1 = BigDecimal.ZERO;
		// Fin variables locales

		if (Terminales.LOG.isTraceEnabled()) {
			Terminales.LOG.trace("Inicio Funcion << vtx002 >> de FuncionesVBX, para la entrada capitalGar = {}, valoresTabMort = {}, gic = {}, interes1 = {} , difer = {}, varx = {}, varp = {} y varn = {}", capitalGar, lstValoresTabMort, gic, interes1, difer, varx, varp, varn);
		}

		//Validamos los parametros de entrada
		ValidacionesTerminales.validarCamposEntradaFuncionvt002DcrrPbParte1(capitalGar, lstValoresTabMort, gic);
		ValidacionesTerminales.validarCamposEntradaFuncionvt002DcrrPbParte2(interes1, difer, varx, varp, varn);

		//Calculamos L(X+N) y L(X+P) como sigue:
		//L(x+p) = valoresTabMort(x+p).wkvalor
		lXMasP = lstValoresTabMort.get(varx + varp);
		//L(x+n) = valoresTabMort(x+n).wkvalor
		lXMasN = lstValoresTabMort.get(varx + varn);

		//operando2 = gic/100 * AX(0,x,p,n,I1, valoresTabMort)
		operando2 = gic.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01).multiply(FuncionesRentas.ax(ConstantsFunciones.CTE_0, varx, varp, varn, interes1, lstValoresTabMort));

		//numeDenoOperand1 <--(l(x+p) - l(x+n)) *  (1 + I1/100)^ -0.5
		numeDenoOperand1 = lXMasP.subtract(lXMasN).multiply(Util.pow(BigDecimal.ONE.add(interes1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)), ConstantsFunciones.CTE_OPER_0_PUNTO_5.negate()));

		//denoOperando1 = 1 - [ numeDenoOperand1 / l(x+p)] 
		denoOperando1 =  BigDecimal.ONE.subtract(numeDenoOperand1.divide(lXMasP, ConstantsFunciones.MATH_CONTEXT));

		//operando1 = DIF(x,p,n,I1, valoresTabMort) / denoOperando1
		operando1 = FuncionesActualizacionFinanciera.dif(varx, varp, varn, interes1, lstValoresTabMort).divide(denoOperando1, ConstantsFunciones.MATH_CONTEXT);

		//vtx002 = capitalGar * ( operando1 + operando2)
		vtx002 = capitalGar.multiply(operando1.add(operando2));

		if (Terminales.LOG.isTraceEnabled()) {
			Terminales.LOG.trace("Fin Funcion << vtx002 >> de FuncionesVBX, con resultado vtx002 = {} ", vtx002);
		}

		return vtx002;
	}

	/**
	 * Función que resuelve el cálculo de PROVISIONES de modalidades de Millón Vida.
	 * @param capitalGar Capital de la garantía.
	 * @param lstValoresTabMort Valores de la tabla de mortalidad.
	 * @param I1 Interés 1 de la base contable tratada.
	 * @param x Edad x del asegurado.
	 * @param p Desplazamiento de la renta.
	 * @param n Duración del seguro en años.
	 * @param m Duración en años del pago de primas.
	 * @param gipc Gastos de gestión interna sobre la prima.
	 * @param gepc Gastos de gestión externa.
	 * @param Pna0 Prima neta.
	 * @param prp Porcentaje de revalorización de Primas.
	 * @param Ifal Variable de Apoyo IFAL.
	 * @param Iant Variable de Apoyo IANT.
	 * @return vtx003 Terminal de la provisión.
	 */
	public static BigDecimal vtx003(final BigDecimal capitalGar, final List<BigDecimal> lstValoresTabMort, final BigDecimal i1,
			final Integer x, final Integer p, final Integer n, final int m, final BigDecimal gipc, final BigDecimal gepc, final BigDecimal pna0,
			final BigDecimal prp, final BigDecimal ifal, final BigDecimal iant){
		//Variables locales
		BigDecimal vtx003;
		BigDecimal pnaTC;
		BigDecimal pinvTc;
		BigDecimal dif;
		BigDecimal capCriFallec;
		BigDecimal ax;
		BigDecimal axp;
		//Fin Variables locales

		if (Terminales.LOG.isTraceEnabled()) {
			Terminales.LOG.trace("Inicio Funcion <<alfa2T>> de la clase Terminales, para la entrada capitalGar = {}, lstValoresTabMort = {}, I1 = {}, x = {}, "
					+ "p = {}, n = {}, m = {}, gipc = {}, gepc = {}, Pna0 = {}, prp = {}, Ifal = {}, Iant = {}",
					capitalGar, lstValoresTabMort, i1, x, p, n, m, gipc, gepc, pna0, prp, ifal, iant);
		}

		//Validaciones parámetros obligatorios de entrada
		ValidacionesTerminales.validarParamEntradaFuncionVtx003(capitalGar, lstValoresTabMort, i1, x, p, n, m, gipc, gepc, pna0, prp, ifal, iant);

		//Se obtienen los datos necesarios para el cálculo.
		if (m>p){
			final BigDecimal prpEntre100Mas1 = (prp.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).add(BigDecimal.ONE);
			final BigDecimal potenciaP = Util.pow(prpEntre100Mas1, p);
			final BigDecimal UnoMenosGepcEntre100 = BigDecimal.ONE.subtract(gepc.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));

			pnaTC = pna0.multiply(potenciaP);
			pinvTc = pnaTC.multiply(UnoMenosGepcEntre100);
		}else{
			pnaTC = BigDecimal.ZERO;
			pinvTc = BigDecimal.ZERO;
		}

		dif = FuncionesActualizacionFinanciera.dif(x, p, n, i1, lstValoresTabMort);
		capCriFallec = FuncionesFallecimiento.capCriFallec(x, p, n, ifal, i1, iant, lstValoresTabMort);
		axp = FuncionesRentas.ax(0, x, p, n, i1, lstValoresTabMort);
		ax = FuncionesRentas.ax(0, x, 0, n, i1, lstValoresTabMort);

		//Variables intermedias para el cálculo
		final BigDecimal axpEntreAx = axp.divide(ax, ConstantsFunciones.MATH_CONTEXT);
		final BigDecimal operadorAx=(gipc.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).multiply(axpEntreAx);

		//Se obtiene la cuantía como vtx003 = capitalGar * DIF + Pna0 * [CAP_CRI: FALLEC + (Gic/100*(AX(p))/(AX(0)))] – PinvTc  
		vtx003 = (capitalGar.multiply(dif)).add((pna0).multiply(capCriFallec.add(operadorAx))).subtract(pinvTc);

		if (Terminales.LOG.isTraceEnabled()) {
			Terminales.LOG.trace("Fin Funcion << vtx003 >> de la clase Terminales, con resultado vtx003 = {}", vtx003);
		}

		return vtx003;
	}

	/**
	 * Función que resuelve el cálculo de PROVISONES del producto Capital Diferido con Reembolso de primas e intereses, 
	 * crecientes en progresión ARITMÉTICA.
	 * @param capitalGar Capital de la garantía.
	 * @param prc Porcentaje de revalorización de Capital.
	 * @param valoresTabMort Valores de la tabla de mortalidad. 
	 * @param i1 Interés 1 de la base contable tratada.
	 * @param x Edad x del asegurado.
	 * @param p Desplazamiento de la renta.
	 * @param n Duración del seguro en años.
	 * @param m Duración del pago de primas en años.
	 * @param gic Gastos de gestión interna sobre Capital.
	 * @param gepc Gastos de gestión externa.
	 * @param pna0 Prima neta.
	 * @param prp Porcentaje de revalorización de Primas.
	 * @param csitupol Situación de la póliza.
	 * @param ctipoaport Tipo aportación primas.
	 * @param ifal Variable de Apoyo IFAL.
	 * @param fallRed0 Capital inicial de fallecimiento a efecto reducción recibido.
	 * @param iant Variable de Apoyo IANT.
	 * @param pas Variable de Apoyo PAS.
	 * @param fut Variable de Apoyo FUT.
	 * 
	 * @return vtx004 Provisiones del capital diferido con reembolso de prima e intereses crecientes en progresión geométrica.
	 */
	public static BigDecimal vtx004(final BigDecimal capitalGar, final BigDecimal prc, final List<BigDecimal> valoresTabMort, final BigDecimal i1,
			final Integer x, final Integer p, final Integer n, final Integer m, final BigDecimal gic, final BigDecimal gepc, final BigDecimal pna0,
			final BigDecimal prp, final String csitupol, final String ctipoaport, final BigDecimal ifal, final BigDecimal fallRed0,
			final BigDecimal iant, final BigDecimal pas, final BigDecimal fut){
		//Inicio variables locales
		BigDecimal vtx004 = BigDecimal.ZERO;
		BigDecimal pNaTc = BigDecimal.ZERO;
		BigDecimal pinv = BigDecimal.ZERO;
		//Fin variables locales
		
		if (Terminales.LOG.isTraceEnabled()) {
			Terminales.LOG.trace("Inicio funciones	<< vtx004 >> de la clase Terminales, para la entrada capitalGar = {}, prc = {}," 
					+"lstValoresTabMort = {}, vari1 = {}, varx = {}, varp = {}, varn = {}, varm = {}, gic = {}, gepc = {}, pna0 = {}, prp = {},"
					+" csitupol = {}, ctipoaport = {}, ifal = {}, fallRed0 = {}, iant = {}, pas = {}, fut = {} ",
					capitalGar, prc, valoresTabMort, i1, x, p, n, m, gic, gepc, pna0, prp, csitupol, ctipoaport, ifal, fallRed0, iant, pas, fut);
		}
		
		
		//Validaciones parametros de entrada
		ValidacionesTerminales.validarParamEntradaFuncionesVtx005Vtx004Parte1(capitalGar, prc, valoresTabMort, i1, x);
		ValidacionesTerminales.validarParamEntradaFuncionesVtx005Vtx004Parte2(p, n, m, gic, gepc, pna0);
		ValidacionesTerminales.validarParamEntradaFuncionesVtx005Vtx004Parte3(prp, csitupol, ctipoaport, ifal, fallRed0);
		ValidacionesTerminales.validarParamEntradaFuncionesVtx005Vtx004Parte4(iant, pas, fut);
		
		//Se calcula la primera parte del término vtx004 que es igual para todos los casos:
		// capitalGar* [DIF(x,p,n,I1,valoresTabMort)*(1+ prc/100*(n-1))+ (Gic/100* AX(0,x,p,n,I1,valoresTabMort)) ]
		BigDecimal dif = FuncionesActualizacionFinanciera.dif(x, p, n, i1, valoresTabMort);
		//BigDecimal prcEntre100 = BigDecimal.ONE.add(prc.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));

		BigDecimal primerOp = dif.multiply(BigDecimal.ONE.add((prc.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).multiply(BigDecimal.valueOf(n-1))));
		BigDecimal segundoOp = gic.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01).multiply(FuncionesRentas.ax(0, x, p, m, i1, valoresTabMort));
		
		BigDecimal operando = capitalGar.multiply(primerOp.add(segundoOp));
		
		
		//Si la póliza está reducida
		if (csitupol.equals(ConstantsFunciones.CTE_APOR_REDUCIDA)){
			vtx004 = operando.add(fallRed0.multiply(FuncionesFallecimiento.capCriFallec(x, p, n, ifal, i1, iant, valoresTabMort)));
		
		//Si la póliza está en vigor, no reducida 
		} else if (csitupol.equals(ConstantsFunciones.CTE_POL_NO_RED)){
			
			//Si el tipo de aportación es prima única
			if (ctipoaport.equals(ConstantsFunciones.CTE_APOR_UNICA)){
				
				if (m > p){
					BigDecimal prpEntre100 = BigDecimal.ONE.add(prp.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
					pNaTc = pna0.multiply(Util.pow(prpEntre100, p));
					pinv = pNaTc.multiply(BigDecimal.ONE.subtract(gepc.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)));
				} 
				
				vtx004 = operando.add((pna0.multiply(FuncionesFallecimiento.capCriFallec(x, p, n, ifal, i1, iant, valoresTabMort)).subtract(pinv)));
				
			//Si el tipo de aportación es prima periódica
			} else if (ctipoaport.equals(ConstantsFunciones.CTE_APOR_PERIO)){
				pinv = pna0.multiply(BigDecimal.ONE.subtract(gepc.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)));
				BigDecimal ariCri = FuncionesPrimas.ariCriFallec(x, p, n, ifal, i1, prp, valoresTabMort, pas, fut);
				BigDecimal axcal = FuncionesRentas.axca(0, x, p, m, i1, prp, valoresTabMort);
				
				vtx004 = operando.add((pna0.multiply(ariCri)).subtract(pinv.multiply(axcal)));
				
			}
		}
		
		if (Terminales.LOG.isTraceEnabled()) {
			Terminales.LOG.trace("Fin Funcion << vtx004 >> de la clase Terminales, con resultado vtx004 = {}", vtx004);
		}
		
		return vtx004;
	}

	/**
	 * La función VTX005 resuelve el cálculo de PROVISIONES del Capital Diferido con Reembolso de primas e intereses, crecientes en progresión GEOMETRICA.
	 * 
	 * @param capitalGar Capital de la garantía
	 * @param prc Porcentaje de revalorización de Capital
	 * @param lstValoresTabMort Valores de la tabla de mortalidad 
	 * @param vari1 Interés 1 de la base contable tratada
	 * @param varx Edad x del asegurado
	 * @param varp Desplazamiento de la renta
	 * @param varn Duración del seguro en años
	 * @param varm Duración del pago de primas en años
	 * @param gic Gastos de gestión interna sobre Capital
	 * @param gepc Gastos de gestión externa
	 * @param pna0 Prima neta
	 * @param prp Porcentaje de revalorización de Primas
	 * @param csitupol Situación de la póliza
	 * @param ctipoaport Tipo aportación primas 
	 * @param ifal Variable de Apoyo IFAL
	 * @param fallRed0 Capital inicial de fallecimiento a efecto reducción recibido
	 * @param iant Variable de Apoyo IANT
	 * @param pas Variable de Apoyo PAS
	 * @param fut Variable de Apoyo FUT
	 * @return vtx005 Provisiones del capital diferido con reembolso de prima e intereses crecientes en progresión geométrica
	 */
	public static BigDecimal vtx005(final BigDecimal capitalGar, final BigDecimal prc, final List<BigDecimal> lstValoresTabMort, final BigDecimal vari1, final Integer varx, final Integer varp, final Integer varn, final Integer varm, final BigDecimal gic, 
			final BigDecimal gepc, final BigDecimal pna0, final BigDecimal prp, final String csitupol, final String ctipoaport, final BigDecimal ifal, final BigDecimal fallRed0, final BigDecimal iant, final BigDecimal pas, final BigDecimal fut) {
		// Variables locales
		BigDecimal vtx005 = BigDecimal.ZERO;
		BigDecimal varPnaTc = BigDecimal.ZERO;
		BigDecimal operando1Cor = BigDecimal.ZERO;
		BigDecimal operando2Cor = BigDecimal.ZERO;
		BigDecimal pinv = BigDecimal.ZERO;
		// Fin variables locales

		if (Terminales.LOG.isTraceEnabled()) {
			Terminales.LOG.trace("Inicio funciones	<< vtx005 >> de la clase Terminales, para la entrada capitalGar = {}, prc = {}, lstValoresTabMort = {}, vari1 = {}, varx = {}, varp = {}, varn = {}, varm = {}, gic = {}, gepc = {}, pna0 = {}, prp = {}, csitupol = {}, ctipoaport = {}, ifal = {}, fallRed0 = {}, iant = {}, pas = {}, fut = {} ",
					capitalGar, prc, lstValoresTabMort, vari1, varx, varp, varn, varm, gic, gepc, pna0, prp, csitupol, ctipoaport, ifal, fallRed0, iant, pas, fut);
		}

		//Validamos la obligatoriedad de los parametros de entrada
		ValidacionesTerminales.validarParamEntradaFuncionesVtx005Vtx004Parte1(capitalGar, prc, lstValoresTabMort, vari1, varx);
		ValidacionesTerminales.validarParamEntradaFuncionesVtx005Vtx004Parte2(varp, varn, varm, gic, gepc, pna0);
		ValidacionesTerminales.validarParamEntradaFuncionesVtx005Vtx004Parte3(prp, csitupol, ctipoaport, ifal, fallRed0);
		ValidacionesTerminales.validarParamEntradaFuncionesVtx005Vtx004Parte4(iant, pas, fut);

		/**
		 * Si la Póliza está en vigor, no reducida - csitupol = "VI"
		 * 		Si M > P
		 * 			PNaTc = PNa0 * (1+Prp/100)^p
		 * 			Pinv = PNaTc * (1-Gepc/100)
		 * 		Si M<= P
		 * 			PNaTc = 0
		 * 			Pinv = 0
		 * 		
		 * 	Si el tipo de aportación es Prima Única - ctipoaport = "U"
		 * 		Se obtendrá la cuantía como:
		 * 			Vx=capitalGar * [DIF(x,p,n,I1,valoresTabMort)*(1+ prc/100)^((n-1))+ (Gic/100* AX(0,x,p,n,I1,valoresTabMort))]+ (Pna0*CAP_CRI: FALLEC (x,p,n,Ifal,I1,0,valoresTabMort)- Pinv)
		 * 		
		 * 	Si el tipo de aportación es Prima Periódica - ctipoaport = "P"
		 * 		Se obtendrá la cuantía como:
		 * 			Vx=capitalGar* [DIF(x,p,n,I1,valoresTabMort)*(1+prc/100)^((n-1))+ (Gic/100* AX(0,x,p,n,I1,valoresTabMort))]+(Pna0*GEO_CRI: FALLEC (x,p,n,Ifal,I1,prp,valoresTabMort)- (Pinv* AXcg(0,x,p,m,I1,prp,valoresTabMort)))
		 * 		
		 * 	Si la Póliza está reducida - csitupol = "RE" y tipo de aportación es Prima Periódica - ctipoaport = "P"
		 * 		Se obtendrá la cuantía como:
		 * 			Vx=capitalGar* [DIF(x,p,n,I1,valoresTabMort)*(1+ prc/100)^((n-1))+ (Gic/100* AX(0,x,p,n,I1,valoresTabMort))]+ (FallRed0*CAP_CRI: FALLEC (x,p,n,Ifal,I1,0,valoresTabMort))
		 */
		//operando1Cor <-- DIF(x,p,n,I1,valoresTabMort)*(1+ prc/100)^(n-1)
		operando1Cor =  FuncionesActualizacionFinanciera.dif(varx, varp, varn, vari1, lstValoresTabMort).multiply(Util.pow(BigDecimal.ONE.add(prc.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)), varn - 1));
		//operando2Cor <--(Gic/100* AX(0,x,p,n,I1,valoresTabMort))
		operando2Cor = gic.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01).multiply(FuncionesRentas.ax(ConstantsFunciones.CTE_0, varx, varp, varn, vari1, lstValoresTabMort));

		// Si el tipo de aportación es Prima única - ctipoaport = 'U'
		if (ConstantsFunciones.CTE_APOR_UNICA.equals(ctipoaport)) {
			// Si la póliza está en vigor, no reducida - csitupol =  'VI'
			if (ConstantsFunciones.CTE_POL_NO_RED.equals(csitupol)){
				// Si (M > P)
				if (varm.compareTo(varp) > 0) {
					varPnaTc = pna0.multiply(Util.pow(BigDecimal.ONE.add(prp.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)), varp));
					pinv = varPnaTc.multiply(BigDecimal.ONE.subtract(gepc.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)));
				}else{
					varPnaTc = BigDecimal.ZERO;
					pinv = BigDecimal.ZERO;
				}
				vtx005 = capitalGar.multiply(operando1Cor.add(operando2Cor)).add(pna0.multiply(FuncionesFallecimiento.capCriFallec(varx, varp, varn, ifal, vari1, iant, lstValoresTabMort)).subtract(pinv));
			}
			// Si la póliza está reducida - csitupol =  'RE'
			else if (ConstantsFunciones.CTE_APOR_REDUCIDA.equals(csitupol)){
				vtx005 = capitalGar.multiply(operando1Cor.add(operando2Cor)).add(fallRed0.multiply(FuncionesFallecimiento.capCriFallec(varx, varp, varn, ifal, vari1, iant, lstValoresTabMort)));
			}
		}
		// Si el tipo de aportación es Prima periódica - ctipoaport = 'P'
		else if (ConstantsFunciones.CTE_APOR_PERIO.equals(ctipoaport)) {

			// Si la póliza está en vigor, no reducida - csitupol =  'VI'
			if (ConstantsFunciones.CTE_POL_NO_RED.equals(csitupol)) {
				pinv = pna0.multiply(BigDecimal.ONE.subtract(gepc.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)));
				vtx005 = capitalGar.multiply(operando1Cor.add(operando2Cor)).add(pna0.multiply(FuncionesFallecimiento.geoCriFallec(varx, varp, varn, ifal, vari1, prp, lstValoresTabMort, pas, fut))).subtract(pinv.multiply(FuncionesRentas.axcg(ConstantsFunciones.CTE_0, varx, varp, varm, vari1,  prp, lstValoresTabMort)));
			}
			// Si la póliza está reducida - csitupol =  'RE' se lanza Error (validaciones)

		}

		if (Terminales.LOG.isTraceEnabled()) {
			Terminales.LOG.trace("Fin funciones << vtx005 >> de la clase Terminales, con resultado vtx005 = {}", vtx005);
		}

		return vtx005;
	}

	/**
	 * La función VTX009 resuelve el cálculo de  la cuantía nominal de la Provisión matemática por fórmula cerrada en BTI .
	 * 
	 * VTX(009, tc) = Renta1 * vrta(tcm, 0,L, i1,L,x,tm,m,prr) * (1 + gic/100) +
	 * Renta2 * vrta(tcm,L, 99, i1,L,i2,x,tm,m, prr) * (1 + gic/100) +
	 * PRIMA * vfal(tcm, i1, L, i2, x, tm, prima, crm, gamma, mescaren) +
	 * PRIMA * (gicp/100) * ((1 + vrta(tc, 0,99,i2,99,i2,x,tm,12,0)) / (1 + vrta(0,0,99,i2,99,i2,x,tm,12,0)) )
	 * 
	 * @param renta1 Renta garantizada primer tramo
	 * @param renta2 Renta mínima garantizada
	 * @param vark Meses completos transcurridos desde efecto (técnico) póliza a fecha cálculo
	 * @param vard Diferimiento de la renta (años)
	 * @param varn Duración de la renta (desde fecha efecto técnico (años)
	 * @param vari1 Tipo de interés primer tramo
	 * @param varl Duración primer tramo
	 * @param vari2 Tipo de interés segundo tramo
	 * @param varx Edad actuarial a fecha efecto técnico.
	 * @param lstValoresTabMort Valores de la tabla de mortalidad
	 * @param varm Nº de Pagos por año de la renta
	 * @param ppr Porcentaje revalorización de la renta
	 * @param prima
	 * @param crm Capital en riesgo máximo
	 * @param gamma % sobre prima para capital fallecimiento
	 * @param mescaren Meses carencia
	 * @param varGic Gastos de gestión interna sobre Capital
	 * @param varGipc Gastos de gestión interna sobre Prima
	 * @return vtx009
	 */
	public static BigDecimal vtx009(final BigDecimal renta1, final BigDecimal renta2, final Integer vark, final Integer vard, final Integer varn, final BigDecimal vari1,
			final Integer varl, final BigDecimal vari2, final Integer varx, final List<BigDecimal> lstValoresTabMort, final Integer varm, final BigDecimal ppr, final BigDecimal prima,
			final List<LimitesCapital> varListaLimCap, final BigDecimal gamma, final Integer mescaren, final BigDecimal varGic, final BigDecimal varGipc, 
			final Map<String, Object> valoresPrecalculados, final boolean varRAgravado, final Integer nmeses, final BigDecimal varCRMax) {
		//Variables locales
		BigDecimal vtx009 = BigDecimal.ZERO;
		BigDecimal varVrta1 = BigDecimal.ZERO;
		BigDecimal varVrta2 = BigDecimal.ZERO;
		BigDecimal varVfall = BigDecimal.ZERO;
		BigDecimal varVrta3 = BigDecimal.ZERO;
		BigDecimal varVrta4 = BigDecimal.ZERO;
		//Fin variables locales

		if (Terminales.LOG.isTraceEnabled()) {
			Terminales.LOG.trace("Inicio de la función << vtx009 >> de la clase Terminales con entrada renta1 = {}, renta2 = {}, vark = {}, vard = {}, varn = {}, vari1 = {}, varl = {}, vari2 = {}, varx = {}, lstValoresTabMort = {}, varm = {}, ppr = {}, prima = {}, varLimCap = {}, gamma = {}, mescaren = {}, varGic = {} y varGipc = {}",
					renta1, renta2, vark, vard, varn, vari1, varl, vari2, varx, lstValoresTabMort, varm, ppr, prima, varListaLimCap, gamma, mescaren, varGic, varGipc);
		}

		//validamos los campos de entrada
		ValidacionesTerminales.validarParamEntradaFuncionVtx009Parte1(renta1, renta2, vark, vard, varn, vari1, varl, vari2);
		ValidacionesTerminales.validarParamEntradaFuncionVtx009Parte2(varx, lstValoresTabMort, varm, ppr, prima);
		ValidacionesTerminales.validarParamEntradaFuncionVtx009Parte3(gamma, mescaren, varGic, varGipc);

		/**
		 * Se obtendrán los datos necesarios para el cálculo: 
		 * 		varVrta1 = VRTA (k,0,N,i1,L,i2,x,valoresTabMort,m,ppr)
		 * 		varVrta2 = VRTA (k,L,99,i1,L,i2,x+ (k/12),valoresTabMort,m,ppr)   
		 * 		varVfall = VFAL (k,i1,l,i2,x,valoresTabMort,prima,cRm,gamma,mescare)
		 * 		varVrta3= VRTA (k,0,99,i2,99,i2, x+ (k/12),valoresTabMort,12,0)   
		 * 		varVrta4= VRTA (0,0,99,i2,99,i2,x,valoresTabMort,12,0)
		 * 	
		 * Se obtendrán el importe de terminal como:
		 * 
		 * VTX009 = renta1 * varVrta1 * (1+ gic/100) + renta2 * varVrta2 * (1+ gic/100) +
		 * 		prima * varVfall + prima * (gipc/100) * ((1+varVrta3 )/(1+ varVrta4))
		 */
		//varkDiv12 =vark / ConstantsFunciones.CTE_12;
		varVrta1 = FuncionesRentas.vrta(vark, ConstantsFunciones.CTE_0, varn, vari1, varl, vari2, varx, lstValoresTabMort, varm, ppr, false, nmeses);
		varVrta2 = FuncionesRentas.vrta(vark, varl, ConstantsFunciones.CTE_99, vari1, varl, vari2, varx, lstValoresTabMort, varm, ppr, false, nmeses);
		varVfall = FuncionesFallecimiento.vfal(vark, vari1, varl, vari2, varx, lstValoresTabMort, prima, varListaLimCap, gamma, mescaren, varRAgravado, varCRMax);
		varVrta3 = FuncionesRentas.vrta(vark, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_99, vari2, ConstantsFunciones.CTE_99, vari2, varx, lstValoresTabMort, ConstantsFunciones.CTE_12, BigDecimal.ZERO, false, nmeses);

		if(null == (varVrta4 = (BigDecimal)valoresPrecalculados.get(CLAVE_VARVRTA4))) {
			varVrta4 = FuncionesRentas.vrta(ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_99, vari2, ConstantsFunciones.CTE_99, vari2, varx, lstValoresTabMort, ConstantsFunciones.CTE_12, BigDecimal.ZERO, false, nmeses);
			valoresPrecalculados.put(CLAVE_VARVRTA4, varVrta4);
		}

		// (1+ gic/100)
		final BigDecimal unoMasGicDiv100 = BigDecimal.ONE.add(varGic.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		// (gipc/100)
		final BigDecimal gipcDiv100 = varGipc.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		// ((1+varVrta3) / (1+ varVrta4))
		final BigDecimal operVrta3y4 = BigDecimal.ONE.add(varVrta3).divide(BigDecimal.ONE.add(varVrta4), ConstantsFunciones.MATH_CONTEXT);

		//operando1 <-- renta1 * varVrta1 * (1+ gic/100)
		final BigDecimal operando1 = renta1.multiply(varVrta1).multiply(unoMasGicDiv100);

		//operando2 <-- renta2 * varVrta2 * (1+ gic/100) 
		final BigDecimal operando2 = renta2.multiply(varVrta2).multiply(unoMasGicDiv100);

		//operando3 <--prima * varVfall
		final BigDecimal operando3 = prima.multiply(varVfall);

		//operando 4 <--  prima * ( gipc/100) * ((1+varVrta3 )/(1+ varVrta4))  
		final BigDecimal operando4 = prima.multiply(gipcDiv100).multiply(operVrta3y4);

		//vtx009 <-- operando1 + operando2 + operando3 + operando4
		vtx009 = operando1.add(operando2).add(operando3).add(operando4);


		if (Terminales.LOG.isTraceEnabled()) {
			Terminales.LOG.trace("Fin de la función << vtx009 >> de la clase FuncionesVBX, con resultado vtx009 = {}", vtx009);
		}

		return vtx009;
	}

	/**
	 * Función que calcula el terminal VTX_VEPU.
	 * @param fnac Fecha de Nacimiento.
	 * @param poliza Póliza
	 * @param subpoliza Subpóliza
	 * @param certificado Certificado
	 * @param nsuscripcion nsuscripcion
	 * @param x Edad actuarial a fecha de efecto.
	 * @param tc Anualidad del cálculo.
	 * @param n Duración del seguro.
	 * @param prg Porcentaje de la revalorización geométrica.
	 * @param lstValoresTabMort Valores de la tabla de mortalidad.
	 * @param m Número de años en que aplicamos un primer interés técnico.
	 * @param i1 Primer Interés técnico.
	 * @param i2 Segundo Interés técnico.
	 * @param icapini Capital inicial de la umic recibido en el maestro.
	 * @param iprimanetaini Prima neta inicial, recibida en el maestro.
	 * @param pgastgesin1 GI porcentaje de gastos sobre el capital. Recibido en el maestro en tanto por 100.
	 * @param pgastgesin2 GIPC, porcentaje de gastos sobre la prima. Recibido en el maestro en tanto por 100.
	 * @param criterioFecha
	 * @return vtxVepu
	 */
	public static BigDecimal vtxVepu(final Timestamp fnac, final Long poliza, final Integer subpoliza, final Integer certificado,
			final Integer nsuscripcion, final BigDecimal x, final Integer tc, final Integer n, final BigDecimal prg, final List<BigDecimal> lstValoresTabMort,
			final Integer m, final BigDecimal i1, final BigDecimal i2, final BigDecimal icapini, final BigDecimal iprimanetaini,
			final BigDecimal pgastgesin1, final BigDecimal pgastgesin2, final String criterioFecha, 
			final Map<String, Object> mapVariables, final Timestamp varfechaEfecto, final BigDecimal pinv){
		//Inicio variables locales
		BigDecimal vtxVepu = BigDecimal.ZERO;
		BigDecimal varEdadCob;
		BigDecimal varViveDifer;
		Integer varDifer = 0;
		Timestamp varFdiferimiento;
		BigDecimal varSegeo2it = BigDecimal.ZERO;
		BigDecimal varRengeo2it_00;
		BigDecimal segundoOp = BigDecimal.ZERO;
		BigDecimal tercerOp = BigDecimal.ZERO;
		BigDecimal varPinv;
		BigDecimal varRengeo2it_TC = BigDecimal.ZERO;
		BigDecimal varRengeo2it_TC0 = BigDecimal.ZERO;
		//Fin variables locales	
		
		if (Terminales.LOG.isTraceEnabled()) {
			Terminales.LOG.trace("Inicio Funcion << VTX_VEPU >> de la clase Terminales, para la entrada  fnac = {}, poliza = {}, subpoliza = {},"
					+" certificado = {}, nsuscripcion = {}, x = {}, tc = {}, n = {}, prg = {}, lstValoresTabMort = {}, m = {}, i1 = {}, i2 = {}, "+
					"icapini = {}, iprimanetaini = {}, pgastgesin1 = {}, pgastgesin2 = {}, criterioFecha = {}", 
					fnac, poliza, subpoliza, certificado, nsuscripcion, x, tc, n, prg, lstValoresTabMort, m, i1, i2, icapini, iprimanetaini, 
					pgastgesin1, pgastgesin2, criterioFecha);
		}
		
		//Validaciones parametros de entrada
		ValidacionesTerminales.validarParamEntradaFuncionesVtxTpfpuVtxVepuParte1(fnac, poliza, subpoliza, certificado, nsuscripcion, x, tc, n, prg, lstValoresTabMort);
		ValidacionesTerminales.validarParamEntradaFuncionesVtxTpfpuVtxVepuParte2(m, i1, i2, icapini, iprimanetaini, pgastgesin1, pgastgesin2, criterioFecha);
				
		
		IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
		varFdiferimiento = obtenerConfiguracion.recuperarVarFdiferimiento(poliza, subpoliza, certificado, nsuscripcion);
		
		BigDecimal unoMasI1Entre100 = Util.numPorcentajeMasUno(i1, mapVariables, "VBX001");
		BigDecimal unoMasI2Entre100 = Util.numPorcentajeMasUno(i2, mapVariables, "VBX001");
		String claveRengeo = "varRengeo2it_00_vtxVepu";
		
		varEdadCob = FuncionesAuxiliares.edadCob(fnac, poliza, subpoliza, certificado, nsuscripcion, criterioFecha);
		varViveDifer = FuncionesVBX.viveDifer(x, tc, i1, varEdadCob, lstValoresTabMort);
		
		if (varFdiferimiento != null){
			varDifer = FuncionesAuxiliares.nAnnos(varfechaEfecto, varFdiferimiento, criterioFecha).setScale(0, RoundingMode.HALF_DOWN).intValue();
		}
		
		varDifer = Math.max(varDifer, tc);
		
		//Se calcula varRengeo2it_00 la primera vez y se almacena para optimizar, ya que no cambia su valor entre iteraciones
		varRengeo2it_00 = (BigDecimal) mapVariables.get(claveRengeo);
		if (varRengeo2it_00 == null){
			varRengeo2it_00 =  FuncionesRentas.rentgeo2it(x, 0, n, BigDecimal.ZERO, lstValoresTabMort, BigDecimal.valueOf(m), unoMasI1Entre100, unoMasI2Entre100, mapVariables);
			mapVariables.put(claveRengeo, varRengeo2it_00);
		}
		
		//varSegeo2it = FuncionesFallecimiento.segeo2it(varDifer, x, tc, n, prg, lstValoresTabMort, BigDecimal.valueOf(m), unoMasI1Entre100, unoMasI2Entre100, mapVariables);
		varSegeo2it = FuncionesFallecimiento.segeo2it(varDifer, x, varDifer, n, prg, lstValoresTabMort, BigDecimal.valueOf(m), unoMasI1Entre100, unoMasI2Entre100, mapVariables);
		
		//Si pgastgesin1 es 0 no es necesario calcular el segundo sumando
		if (!pgastgesin1.equals(BigDecimal.ZERO)){
			BigDecimal pgast1 = pgastgesin1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
			varRengeo2it_TC = FuncionesRentas.rentgeo2it(x, tc, n, prg, lstValoresTabMort, BigDecimal.valueOf(m), unoMasI1Entre100, unoMasI2Entre100, mapVariables);
			segundoOp = pgast1.multiply(varRengeo2it_TC);
		}
		
		//Si pgastgesin2 es 0 no es necesario calcular el tercer sumando
		if (!pgastgesin2.equals(BigDecimal.ZERO)){
			BigDecimal pgast2 = pgastgesin2.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
			varRengeo2it_TC0 =  FuncionesRentas.rentgeo2it(x, tc, n, BigDecimal.ZERO, lstValoresTabMort, BigDecimal.valueOf(m), unoMasI1Entre100, unoMasI2Entre100, mapVariables);
			tercerOp = pgast2.multiply(iprimanetaini).multiply(varRengeo2it_TC0.divide(varRengeo2it_00, ConstantsFunciones.MATH_CONTEXT));
		}
		
		varPinv = pinv.setScale(2, RoundingMode.FLOOR);
		/**
		 * Se obtiene el importe de terminal como:
		 * VTX_VEPU  = icapIni * [(varDifer * varSegeo2it ) + (pgastgesin1 * varRengeo2it_TC ) ] +   
		 *     (pgastgesin2 * iprimanetaini * (varRengeo2it_TC0/varRengeo2it_00) )  - pinv
		 */
		BigDecimal primerOp = varViveDifer.multiply(varSegeo2it);
		vtxVepu = (icapini.multiply(primerOp.add(segundoOp)).add(tercerOp)).setScale(2, RoundingMode.FLOOR);
		vtxVepu = vtxVepu.subtract(varPinv);
		
		
		if (Terminales.LOG.isTraceEnabled()) {
			Terminales.LOG.trace("Fin Funcion << VTX_VEPU >> de la clase Terminales, con resultado vtxVepu = {} ", vtxVepu);
		}
		
		return vtxVepu;
	}
	
	/**
	 * Función que calcula el término del terminal VTX_TPFPU.
	 * @param fnac Fecha de Nacimiento.
	 * @param poliza Póliza 
	 * @param subpoliza Subpóliza
	 * @param certificado Certificado
	 * @param nsuscripcion Suscripcion
	 * @param x Edad actuarial a fecha de efecto.
	 * @param tc Anualidad del cálculo.
	 * @param n Duración del seguro.
	 * @param prg Porcentaje de la revalorización geométrica.
	 * @param lstValoresTabMort Valores de la tabla de mortalidad.
	 * @param m Número de años en que aplicamos un primer interés técnico.
	 * @param i1 Primer Interés técnico.
	 * @param i2 Segundo Interés técnico.
	 * @param icapini Capital inicial de la umic recibido en el maestro.
	 * @param iprimanetaini Prima neta inicial, recibida en el maestro.
	 * @param pgastgesin1 GI porcentaje de gastos sobre el capital. Recibido en el maestro en tanto por 100.
	 * @param pgastgesin2 GIPC, porcentaje de gastos sobre la prima. Recibido en el maestro en tanto por 100.
	 * @param criterioFecha
	 * @return vtxTpfpu
	 */
	public static BigDecimal vtxTpfpu(final Timestamp fnac, final Long poliza, final Integer subpoliza, final Integer certificado,
			final Integer nsuscripcion, final BigDecimal x, final Integer tc, final Integer n, final BigDecimal prg, final List<BigDecimal> lstValoresTabMort,
			final Integer m, final BigDecimal i1, final BigDecimal i2, final BigDecimal icapini, final BigDecimal iprimanetaini,
			final BigDecimal pgastgesin1, final BigDecimal pgastgesin2, final String criterioFecha, 
			final Map<String, Object> mapVariables, final Timestamp varfechaEfecto, final BigDecimal pinv){
		//Inicio variables locales
		BigDecimal vtxTpfpu = BigDecimal.ZERO;
		Integer varDifer = 0;
		Timestamp varFdiferimiento;
		BigDecimal varSegeo2it = BigDecimal.ZERO;
		BigDecimal varRengeo2it_00;
		BigDecimal primerOp = BigDecimal.ZERO;
		BigDecimal segundoOp = BigDecimal.ZERO;
		BigDecimal varPinv;
		//Fin variables locales
		
		if (Terminales.LOG.isTraceEnabled()) {
			Terminales.LOG.trace("Inicio Funcion << VTX_TPFPU >> de la clase Terminales, para la entrada  fnac = {}, poliza = {}, subpoliza = {},"
					+" certificado = {}, x = {}, tc = {}, n = {}, prg = {}, lstValoresTabMort = {}, m = {}, i1 = {}, i2 = {}, "+
					"icapini = {}, iprimanetaini = {}, pgastgesin1 = {}, pgastgesin2 = {}, criterioFecha = {}", 
					fnac, poliza, subpoliza, certificado, x, tc, n, prg, lstValoresTabMort, m, i1, i2, icapini, iprimanetaini, 
					pgastgesin1, pgastgesin2, criterioFecha);
		}
		
		//Validaciones parametros de entrada
		ValidacionesTerminales.validarParamEntradaFuncionesVtxTpfpuVtxVepuParte1(fnac, poliza, subpoliza, certificado, nsuscripcion, x, tc, n, prg, lstValoresTabMort);
		ValidacionesTerminales.validarParamEntradaFuncionesVtxTpfpuVtxVepuParte2(m, i1, i2, icapini, iprimanetaini, pgastgesin1, pgastgesin2, criterioFecha);
		
		IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
		varFdiferimiento = obtenerConfiguracion.recuperarVarFdiferimiento(poliza, subpoliza, certificado, nsuscripcion);
		
		BigDecimal unoMasI1Entre100 = Util.numPorcentajeMasUno(i1, mapVariables, "VBX001");
		BigDecimal unoMasI2Entre100 = Util.numPorcentajeMasUno(i2, mapVariables, "VBX001");
		String claveRengeo = "varRengeo2it_00_vtxTpfpu";
		
		if (varFdiferimiento != null){
			varDifer = FuncionesAuxiliares.nAnnos(varfechaEfecto, varFdiferimiento, criterioFecha).setScale(0, RoundingMode.HALF_DOWN).intValue();
		}
		
		varSegeo2it = FuncionesFallecimiento.segeo2it(varDifer, x, tc, n, prg, lstValoresTabMort, BigDecimal.valueOf(m), unoMasI1Entre100, unoMasI2Entre100, mapVariables);
		
		//Se calcula varRengeo2it_00 la primera vez y se almacena para optimizar, ya que no cambia su valor entre iteraciones
		varRengeo2it_00 = (BigDecimal) mapVariables.get(claveRengeo);
		if (varRengeo2it_00 == null){
			varRengeo2it_00 = FuncionesRentas.rentgeo2it(x, 0, n, BigDecimal.ZERO, lstValoresTabMort, BigDecimal.valueOf(m), unoMasI1Entre100, unoMasI2Entre100, mapVariables);
			mapVariables.put(claveRengeo, varRengeo2it_00);
		}
		
		//Si pgastgesin1 es 0 no es necesario calcular el primer sumando
		if (!pgastgesin1.equals(BigDecimal.ZERO)){
			BigDecimal pgast1 = pgastgesin1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
			BigDecimal varRengeo2it_TC = FuncionesRentas.rentgeo2it(x, tc, n, prg, lstValoresTabMort, BigDecimal.valueOf(m), unoMasI1Entre100, unoMasI2Entre100, mapVariables);
			primerOp = pgast1.multiply(varRengeo2it_TC);
		}
		
		//Si pgastgesin2 es 0 no es necesario calcular el segundo sumando
		if (!pgastgesin2.equals(BigDecimal.ZERO)){
			BigDecimal pgast2 = pgastgesin2.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
			BigDecimal varRengeo2it_TC0 = FuncionesRentas.rentgeo2it(x, tc, n, BigDecimal.ZERO, lstValoresTabMort, BigDecimal.valueOf(m), unoMasI1Entre100, unoMasI2Entre100, mapVariables);
			segundoOp = pgast2.multiply(iprimanetaini).multiply(varRengeo2it_TC0.divide(varRengeo2it_00, ConstantsFunciones.MATH_CONTEXT));
		}
		
		
		/**
		 * Se obtiene el importe de terminal como:
		 * VTXTPFPU = icapIni* [(varSegeo2it ) + (pgastgesin1 * varRengeo2it_TC )]   +   
		 *     (pgastgesin2 * iprimanetaini * (varRengeo2it_TC0/varRengeo2it_00) ) - pinv
		 */
		varPinv = pinv.setScale(2, RoundingMode.FLOOR);
		vtxTpfpu = (icapini.multiply(varSegeo2it.add(primerOp)).add(segundoOp)).setScale(2, RoundingMode.FLOOR);
		vtxTpfpu = vtxTpfpu.subtract(varPinv);
		
		
		if (Terminales.LOG.isTraceEnabled()) {
			Terminales.LOG.trace("Fin Funcion << VTX_TPFPU >> de la clase Terminales, con resultado vtxTpfpu = {} ", vtxTpfpu);
		}
		
		return vtxTpfpu;
	}


}
