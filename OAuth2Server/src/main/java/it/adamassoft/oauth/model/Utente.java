package it.adamassoft.oauth.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name="Utente.findAll", query="SELECT u FROM Utente u ORDER BY u.username"),
	@NamedQuery(name="Utente.deleteAll", query="DELETE FROM Utente u"),
	@NamedQuery(name="Utente.login", query="SELECT u FROM Utente u WHERE u.username = :username AND u.password = :password"),
	@NamedQuery(name="Utente.findUsername", query="SELECT u FROM Utente u WHERE u.username = :username")
})
public class Utente {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String username;
	private String password;
	@ManyToOne
	private Cliente cliente;
	private String email;
	
	@ManyToMany
	@JoinTable(name="Utente_Ruolo")
	List<Ruolo> ruoli;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Ruolo> getRuoli() {
		return ruoli;
	}

	public void setRuoli(List<Ruolo> ruoli) {
		this.ruoli = ruoli;
	}
}
