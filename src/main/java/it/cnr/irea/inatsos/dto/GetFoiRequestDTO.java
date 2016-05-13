package it.cnr.irea.inatsos.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace="http://www.opengis.net/sos/2.0", name="GetFeatureOfInterest")
public class GetFoiRequestDTO {
	@XmlAttribute
	public String service = "SOS";
	@XmlAttribute
	public String version = "2.0.0";
	
	@XmlElement(namespace="http://www.opengis.net/sos/2.0", name="observedProperty", required=false)
	public String observedProperty;
	@XmlElement(namespace="http://www.opengis.net/sos/2.0", name="spatialFilter", required=false)
	public SOSSpatialFilterDTO spatialFilter;
}
