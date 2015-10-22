package it.cnr.irea.inatsos.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
	@NamedQuery(name="Harvest.findAll", query="SELECT h FROM Harvest h"),
	@NamedQuery(name="Harvest.findLast", query="SELECT h FROM Harvest h ORDER BY h.timestamp DESC")
})
public class Harvest {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp = new Date();
	private long retrievedObservations;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public long getRetrievedObservations() {
		return retrievedObservations;
	}
	public void setRetrievedObservations(long retrievedObservations) {
		this.retrievedObservations = retrievedObservations;
	}
	
	
}
