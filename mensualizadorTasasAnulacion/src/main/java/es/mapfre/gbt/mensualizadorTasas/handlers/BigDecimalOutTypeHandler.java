package es.mapfre.gbt.mensualizadorTasas.handlers;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Properties;

import org.beanio.types.BigDecimalTypeHandler;
import org.beanio.types.TypeHandler;

public class BigDecimalOutTypeHandler extends BigDecimalTypeHandler {
	
	private String format = "#";
	private DecimalFormat dfFormatter;
	
	private char formatDecimalSeparator = '.';
	private int decimales = 0;
	
	public char getFormatDecimalSeparator() {
		return formatDecimalSeparator;
	}
	public void setFormatDecimalSeparator(char formatDecimalSeparator) {
		this.formatDecimalSeparator = formatDecimalSeparator;
	}
	public int getDecimales() {
		return decimales;
	}
	public void setDecimales(int decimales) {
		this.decimales = decimales;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public DecimalFormat getDfFormatter() {
		return dfFormatter;
	}
	public void setDfFormatter(DecimalFormat dfFormatter) {
		this.dfFormatter = dfFormatter;
	}
	
	public BigDecimal createNumber(String text) {
		try {
			Number numb = this.dfFormatter.parse(text);
			if (numb != null) {
				return new BigDecimal(numb.toString());
			}
		} catch (ParseException e) {
		}
		return null;
	}
	
    public String format(Object value) {
    	
    	if (value instanceof BigDecimal) {
    		BigDecimal valor = (BigDecimal)value;
    		
    		return dfFormatter.format(valor.abs()).replace(",", "");
    	}
    	return dfFormatter.format(BigDecimal.ZERO);
    }
    
    public Class<?> getType() {
        return BigDecimal.class;
    }
    
	@Override
	public TypeHandler newInstance(Properties arg0) {
		
		BigDecimalOutTypeHandler ddth = new BigDecimalOutTypeHandler();
		
		int nuevosDecimales = 0;
		
		if (ddth.getFormat() != null && ddth.getFormat().lastIndexOf(formatDecimalSeparator) > -1) {
			nuevosDecimales = ddth.getFormat().length() - ddth.getFormat().lastIndexOf(formatDecimalSeparator) - 1;
			ddth.setDecimales(nuevosDecimales);
		}
		
		ddth.setFormat(arg0.getProperty(FORMAT_SETTING));
		// Se le indica el separador decimal a usar
		DecimalFormatSymbols separadorDecimal = new DecimalFormatSymbols();
		separadorDecimal.setDecimalSeparator(','); 
		
		
		DecimalFormat defFormatter = new DecimalFormat(ddth.getFormat(), separadorDecimal);
		ddth.setDfFormatter(defFormatter);
		
		return ddth;
	}
}