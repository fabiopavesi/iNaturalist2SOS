@XmlSchema(
    elementFormDefault=XmlNsForm.QUALIFIED,
    namespace="http://www.opengis.net/swe/2.0",
    xmlns={
    		@XmlNs(prefix="swes",
                  namespaceURI="http://www.opengis.net/swes/2.0"),
    		@XmlNs(prefix="xsi", namespaceURI="http://www.w3.org/2001/XMLSchema-instance"),	
    		@XmlNs(prefix="gml", namespaceURI="http://www.opengis.net/gml/3.2"),	
    		@XmlNs(prefix="swe", namespaceURI="http://www.opengis.net/swe/2.0"),	
    		@XmlNs(prefix="ows", namespaceURI="http://www.opengis.net/ows/1.1")	
    }
)
package it.cnr.irea.inatsos.dto;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;