package es.mapfre.solvencia.files;

public class FileDescriptor {
	/**
	 * Identificador asignado al fichero en SOLVENCIA
	 */
	private String streamName;
	
	/**
	 * Ruta relativa del fichero
	 */
	private String filePath;
	
	/**
	 * Fichero de configuración de beanio a emplear en la carga o exportación del fichero descrito.
	 */
	private String beanioConfigXml;

	/**
	 * tipo de fichero a exportar
	 */
	private Integer type;

	
	public FileDescriptor(String filePath, String streamName, String beanioConfigXml) {
		this.filePath = filePath;
		this.streamName = streamName;
		this.beanioConfigXml = beanioConfigXml;
	}
	
	
	public FileDescriptor(String filePath, String streamName, String beanioConfigXml, Integer type) {
		this.filePath = filePath;
		this.streamName = streamName;
		this.beanioConfigXml = beanioConfigXml;
		this.type = type;
	}

	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getBeanioConfigXml() {
		return beanioConfigXml;
	}

	public void setBeanioConfigXml(String beanioConfigXml) {
		this.beanioConfigXml = beanioConfigXml;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	


}
