package it.cnr.irea.inatsos.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace="http://www.opengis.net/sos/2.0", name="GetObservation")
public class GetObservationRequestDTO {
	@XmlElement(namespace="http://www.opengis.net/sos/2.0", name="procedure", required=false)
	public String procedure;

	@XmlElement(namespace="http://www.opengis.net/sos/2.0", name="spatialFilter", required=false)
	public SOSSpatialFilterDTO spatialFilter;
	
	@XmlElement(namespace="http://www.opengis.net/sos/2.0", name="responseFormat", required=true, defaultValue="http://www.opengis.net/om/2.0")
	public String responseFormat = "http://www.opengis.net/om/2.0";
}
