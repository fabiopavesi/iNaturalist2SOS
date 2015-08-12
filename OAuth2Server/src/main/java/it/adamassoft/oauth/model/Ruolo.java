package it.adamassoft.oauth.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Ruolo {
	@Id
	private String ruoloCod;
	private String ruolo;
	@ManyToMany(mappedBy="ruoli")
	List<Utente> utenti;
	
	public String getRuoloCod() {
		return ruoloCod;
	}
	public void setRuoloCod(String ruoloCod) {
		this.ruoloCod = ruoloCod;
	}
	public String getRuolo() {
		return ruolo;
	}
	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}
	public List<Utente> getUtenti() {
		return utenti;
	}
	public void setUtenti(List<Utente> utenti) {
		this.utenti = utenti;
	}
	
}
