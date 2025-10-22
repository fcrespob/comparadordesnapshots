package es.mapfre.gbt.tablasExperiencia.utils.beanio.handlers;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Properties;

import org.beanio.types.ConfigurableTypeHandler;
import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

public class BigDecimalHostInTypeHandler implements TypeHandler, ConfigurableTypeHandler {
	private static final int MAXIMO_NUMERO_DECIMALES = 9;
	private static final int BASE_10 = 10;
	private String format = "#";
	private DecimalFormat dfParser;
	private DecimalFormat dfFormatter;
	
	private char formatDecimalSeparator = '.';
	
	private int decimales = 0;
	
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public DecimalFormat getDfParser() {
		return dfParser;
	}
	public void setDfParser(DecimalFormat dfParser) {
		this.dfParser = dfParser;
	}
	public DecimalFormat getDfFormatter() {
		return dfFormatter;
	}
	public void setDfFormatter(DecimalFormat dfFormatter) {
		this.dfFormatter = dfFormatter;
	}
	
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

	public Number parse(String text) throws TypeConversionException {
		
		try {
			BigDecimal returnedBigDecimal = (BigDecimal) dfParser.parse(text.replace('+', ' ').trim()); 
			if ((decimales > MAXIMO_NUMERO_DECIMALES) && (returnedBigDecimal != null)) {
				return returnedBigDecimal.divide((BigDecimal.valueOf(Math.pow(BASE_10, decimales-MAXIMO_NUMERO_DECIMALES))));
			} else {
				return returnedBigDecimal;
			}
		} catch (Exception e) {
			throw new TypeConversionException(e);
		}
    }
    public String format(Object value) {
    	// TODO Valorar si es necesario incluir el caso específico
    	// de un número con más de 9 decimales (ya contemplado en el handler de salida)
    	return dfFormatter.format(value != null ? value : 0);
    }

    public Class<?> getType() {
        return BigDecimal.class;
    }
    
	@Override
	public TypeHandler newInstance(Properties arg0) {
		BigDecimalHostInTypeHandler ddth = new BigDecimalHostInTypeHandler();
		
		ddth.setFormat(arg0.getProperty(FORMAT_SETTING));
		
		Integer nuevosDecimales = 0;
		
		if (ddth.getFormat() != null && ddth.getFormat().lastIndexOf(formatDecimalSeparator) > -1) {
			nuevosDecimales = ddth.getFormat().length() - ddth.getFormat().lastIndexOf(formatDecimalSeparator) - 1;
			ddth.setDecimales(nuevosDecimales);
		}
		
		DecimalFormat defParser = new DecimalFormat(ddth.getFormat().replace('+', ' ').trim());
		DecimalFormat defFormatter = new DecimalFormat(ddth.getFormat());
		if (nuevosDecimales > MAXIMO_NUMERO_DECIMALES) {
			defParser.setMultiplier((int) Math.pow(BASE_10, MAXIMO_NUMERO_DECIMALES));
			defFormatter.setMultiplier((int) Math.pow(BASE_10, MAXIMO_NUMERO_DECIMALES));
		} else {
			defParser.setMultiplier((int) Math.pow(BASE_10, nuevosDecimales));
			defFormatter.setMultiplier((int) Math.pow(BASE_10, nuevosDecimales));
		}
		defParser.setParseBigDecimal(true);
		ddth.setDfParser(defParser);
						
		defFormatter.setParseBigDecimal(true);
		ddth.setDfFormatter(defFormatter);
		
		return ddth;
	}
}