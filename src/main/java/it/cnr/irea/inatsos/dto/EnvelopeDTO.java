package it.cnr.irea.inatsos.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class EnvelopeDTO {
	@XmlAttribute(name="srsName")
	public String srsName = "http://www.opengis.net/def/crs/EPSG/0/4326";
	@XmlElement(namespace="http://www.opengis.net/gml/3.2", name="lowerCorner")
	public String lowerCorner;
	@XmlElement(namespace="http://www.opengis.net/gml/3.2", name="upperCorner")
	public String upperCorner;
}
