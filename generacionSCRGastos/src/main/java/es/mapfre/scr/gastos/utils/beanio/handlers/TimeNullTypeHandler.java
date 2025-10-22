package es.mapfre.scr.gastos.utils.beanio.handlers;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import org.beanio.types.ConfigurableTypeHandler;
import org.beanio.types.DateTypeHandler;
import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Deal with yyyyMMdd = 00000000 when calendar is null
 */
public class TimeNullTypeHandler extends DateTypeHandler implements TypeHandler, ConfigurableTypeHandler {
	
	private static Logger log = LoggerFactory.getLogger(TimeNullTypeHandler.class);
	
	private String nullTime = "000000";
	private String lastTime = "999999";
	private String nullDate = "00000000";
	private String lastDate = "99999999";
	
	private  String lastValiedDate = "99991231";
	private String lastValiedTime = "235959";
	private int datePosition = 0;
	private int timeposition = 8;
	
	@Override
	public Timestamp parse(String text) {
				
		String textofecha ="";
		String textohora = "";
		String textTotal="";
		Date fecha = null;
		
		if(datePosition < timeposition){
			if(text.length()>= timeposition){
				textofecha = text.substring(datePosition,timeposition);
				textohora = text.substring(timeposition,text.length());
				textTotal = parseDate(textofecha)+parseTime(textohora);
			}
			
		}else{
			if(text.length()>= datePosition){
				textohora = text.substring(timeposition,datePosition);
				textofecha = text.substring(datePosition,text.length());	
				textTotal = parseTime(textohora)+parseDate(textofecha);
			}	
		}
		
		try {
			fecha = super.parse(textTotal);
		} catch (TypeConversionException e) {
			log.error(e.getMessage()+"\""+textTotal+"\" to format "+this.getPattern(),e);
		}
		
		if(fecha==null){
			return null;
		}else{
			return new Timestamp(fecha.getTime());
		}

	}
	
	private String parseDate(String text){
		String fecha = null;
		
		if(lastDate.equals(text)){
			fecha =lastValiedDate;
		}else if (nullDate.equals(text) ) {
			fecha = "";
		}else{
			fecha = text;
		}
		
		return fecha;
	}
	
	private String parseTime(String text){
		
		String fecha ="";
		
		if(lastTime.equals(text)){
			fecha = lastValiedTime;
		}else{
			fecha = text;
		}
		
		return fecha;
	}
	
	
	@Override
	public DateTypeHandler newInstance(Properties arg0) throws IllegalArgumentException {
		this.setPattern(arg0.getProperty(FORMAT_SETTING));
		return this;
	}
	
	@Override
	public String format(Object value) {
		if (value == null) {
			return nullTime;
		}
		return super.format(value);
	}
	
	@Override
	public Class<?> getType() {
		return Timestamp.class;
	}

	public String getLastValiedDate() {
		return lastValiedDate;
	}

	public void setLastValiedDate(String lastValiedDate) {
		this.lastValiedDate = lastValiedDate;
	}

	public String getLastValiedTime() {
		return lastValiedTime;
	}

	public void setLastValiedTime(String lastValiedTime) {
		this.lastValiedTime = lastValiedTime;
	}

	public int getDatePosition() {
		return datePosition;
	}

	public void setDatePosition(int datePosition) {
		this.datePosition = datePosition;
	}

	public int getTimeposition() {
		return timeposition;
	}

	public void setTimeposition(int timeposition) {
		this.timeposition = timeposition;
	}

	public String getNullTime() {
		return nullTime;
	}

	public void setNullTime(String nullTime) {
		this.nullTime = nullTime;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
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
	
	
	
}
