package it.cnr.irea.inatsos.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace="http://www.opengis.net/swes/2.0", name="DescribeSensor")
public class DescribeSensorRequestDTO implements GenericResponse {
	@XmlElement(namespace="http://www.opengis.net/swes/2.0", name="procedure", required=true)
	public String procedure;
	@XmlElement(namespace="http://www.opengis.net/swes/2.0", required=true, defaultValue="http://www.opengis.net/sensorml/2.0", name="procedureDescriptionFormat")
	public String procedureDescriptionFormat = "http://www.opengis.net/sensorml/2.0";
	@XmlAttribute
	public String service = "SOS";
	@XmlAttribute
	public String version = "2.0.0";
	
	public DescribeSensorRequestDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DescribeSensorRequestDTO(String procedure, String procedureDescriptionFormat) {
		super();
		this.procedure = procedure;
		this.procedureDescriptionFormat = procedureDescriptionFormat;
	}
	@Override
	public String toString() {
		return "DescribeSensorRequestDTO [procedure=" + procedure + ", procedureDescriptionFormat="
				+ procedureDescriptionFormat + ", service=" + service + ", version=" + version + "]";
	}
	
	
}
