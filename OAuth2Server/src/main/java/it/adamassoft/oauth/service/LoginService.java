package it.adamassoft.oauth.service;

import it.adamassoft.oauth.model.Utente;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class LoginService {
	@Autowired
	private EntityManager em;
	
	public Utente login(String username) {
		List<Utente> matching = em.createNamedQuery("Utente.findUsername").setParameter("username", username).setFirstResult(0).setMaxResults(1).getResultList();
		if ( matching.size() > 0 ) {
			return matching.get(0);
		} else {
			return null;
		}
	}
}
