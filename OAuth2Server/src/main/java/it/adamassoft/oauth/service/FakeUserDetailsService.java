package it.adamassoft.oauth.service;

import it.adamassoft.oauth.model.Utente;

import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FakeUserDetailsService implements UserDetailsService {
	@Autowired
	private LoginService service;
	Logger log = Logger.getRootLogger();
	private Utente utente = null;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		log.info("" + username + " logging in");
		if ( username.equalsIgnoreCase("admin") ) {
			log.info("admin accepted");
			return new User(username, "admin", getGrantedAuthorities(username));
		}
		Utente u = service.login(username);
		
		if (u == null) {
			log.error("user not found");
			throw new UsernameNotFoundException("Username " + username + " not found");
		}
		utente = u;
		log.info("user found");
		return new User(username, u.getPassword(), getGrantedAuthorities(username));
	}

	private Collection<? extends GrantedAuthority> getGrantedAuthorities(
			String username) {
		Collection<? extends GrantedAuthority> authorities;
		if (username.equalsIgnoreCase("admin")) {
			authorities = Arrays.asList(() -> "ROLE_ADMIN", () -> "ROLE_BASIC");
		} else {
			authorities = Arrays.asList(() -> "ROLE_BASIC");
		}
		return authorities;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}
	
}