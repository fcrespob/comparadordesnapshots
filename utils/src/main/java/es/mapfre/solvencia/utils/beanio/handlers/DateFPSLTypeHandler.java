package es.mapfre.solvencia.utils.beanio.handlers;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import org.beanio.types.ConfigurableTypeHandler;
import org.beanio.types.DateTypeHandler;
import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

public class DateFPSLTypeHandler extends DateTypeHandler implements TypeHandler, ConfigurableTypeHandler {
	private String nullDate = "";
	private String lastDate = "99999999";
	private String lastValiedDate = "99991231";
	
	@Override
	public Timestamp parse(String text) throws TypeConversionException {
	
		Timestamp returnValue = null;
		Date fecha = null;
		
		if(lastDate.equals(text)){
			fecha = super.parse(this.lastValiedDate);
		}else if (nullDate.equals(text) ) {
			fecha = null;
		}else{
			fecha = super.parse(text);
		}
		
		if(fecha != null){
			returnValue = new Timestamp(fecha.getTime());
		}
		
		return returnValue;
	}
	

	@Override
	public String format(Object value) {
		if (value == null) {
			return nullDate;
		}
		return super.format(value);
	}
	
	@Override
	public DateFPSLTypeHandler newInstance(Properties props) throws IllegalArgumentException {
		DateFPSLTypeHandler dnth = new DateFPSLTypeHandler();
		dnth.setPattern(props.getProperty(FORMAT_SETTING));
		return dnth;
	}
	
	@Override
	public Class<?> getType() {
		return Timestamp.class;
	}

	public String getNullDate() {
		return nullDate;
	}

	public void setNullDate(String nullDate) {
		this.nullDate = nullDate;
	}

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

	public String getLastValiedDate() {
		return lastValiedDate;
	}

	public void setLastValiedDate(String lastValiedDate) {
		this.lastValiedDate = lastValiedDate;
	}
	
}
