package it.adamassoft.oauth.model;

import java.io.IOException;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.PrePersist;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;

@Embeddable
public class Indirizzo {
	private String indirizzo;
	private String civico;
	private String localita;
	private String provincia;
	private String cap;
	private String nazione;
	private double latitudine = 0.0;
	private double longitudine = 0.0;
	private String geonamesId;
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getCivico() {
		return civico;
	}
	public void setCivico(String civico) {
		this.civico = civico;
	}
	public String getLocalita() {
		return localita;
	}
	public void setLocalita(String localita) {
		this.localita = localita;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getCap() {
		return cap;
	}
	public void setCap(String cap) {
		this.cap = cap;
	}
	public String getNazione() {
		return nazione;
	}
	public void setNazione(String nazione) {
		this.nazione = nazione;
	}
	public double getLatitudine() {
		return latitudine;
	}
	public void setLatitudine(double latitudine) {
		this.latitudine = latitudine;
	}
	public double getLongitudine() {
		return longitudine;
	}
	public void setLongitudine(double longitudine) {
		this.longitudine = longitudine;
	}
	public String getGeonamesId() {
		return geonamesId;
	}
	public void setGeonamesId(String geonamesId) {
		this.geonamesId = geonamesId;
	}
	
	@PrePersist
	public void geocode() {
		final Geocoder geocoder = new Geocoder();
		final Indirizzo i = this;
		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(i.getIndirizzo() + ", " + i.getCivico() + ", " + i.getCap() + ", " + i.getLocalita() + ", " + i.getProvincia() + ", " + i.getNazione()).setLanguage("it").getGeocoderRequest();
		GeocodeResponse geocoderResponse;
		try {
			geocoderResponse = geocoder.geocode(geocoderRequest);
			List<GeocoderResult> results = geocoderResponse.getResults();
			for ( GeocoderResult r : results ) {
				// System.out.println(r.toString());
				this.setLongitudine(r.getGeometry().getLocation().getLng().doubleValue());
				this.setLatitudine(r.getGeometry().getLocation().getLat().doubleValue());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
