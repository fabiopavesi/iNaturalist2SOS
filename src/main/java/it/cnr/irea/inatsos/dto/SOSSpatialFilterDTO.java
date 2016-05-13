package it.cnr.irea.inatsos.dto;

import javax.xml.bind.annotation.XmlElement;

public class SOSSpatialFilterDTO {
	@XmlElement(namespace="http://www.opengis.net/fes/2.0", name="BBOX")
	public BBOXDTO bbox;
}
