package it.adamassoft.oauth.service;

import it.adamassoft.oauth.model.Cliente;
import it.adamassoft.oauth.model.Indirizzo;
import it.adamassoft.oauth.model.Utente;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import scala.annotation.meta.setter;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;

@Transactional
@Service
public class BaseService {
	@Autowired
	EntityManager em;

	@Autowired
	private FakeUserDetailsService userDetailsService;

	private Logger log = Logger.getRootLogger();
	
	@Transactional
	public void init() {
		/*
		Cliente cliente = new Cliente();
		cliente.setCodiceFiscale("pvsfld65e27f205p");
		cliente.setPartitaIva("02554640181");
		cliente.setRagioneSociale("Adamassoft di Fabio Pavesi");
		Indirizzo i = new Indirizzo();
		i.setIndirizzo("via Cusani");
		i.setCivico("87");
		i.setCap("27013");
		i.setLocalita("Chignolo Po");
		i.setProvincia("PV");
		i.setNazione("Italia");
		i.setLatitudine(0);
		i.setLongitudine(0);
		cliente.setIndirizzo(i);
		
		em.persist(cliente);
		
		Utente utente = new Utente();
		utente.setCliente(cliente);
		utente.setUsername("fabio");
		em.persist(utente);
		
		Magazzino m = new Magazzino();
		m.setCliente(cliente);
		m.setDescrizione("Magazzino Test");
		m.setIndirizzo(i);
		
		em.persist(m);
		*/
		em.createNamedQuery("Utente.deleteAll").executeUpdate();
		
		Utente u = new Utente();
		u.setCliente(em.find(Cliente.class, 1L));
		u.setUsername("fabio");
		u.setPassword("password");
		u.setEmail("fabio@adamassoft.it");
		
		em.persist(u);
		
	}
	
	public String getToken() throws JoseException {
		//
	    // JSON Web Token is a compact URL-safe means of representing claims/attributes to be transferred between two parties.
	    // This example demonstrates producing and consuming a signed JWT
	    //

		log.info("user: " + userDetailsService.getUtente().getUsername());
		
	    // Generate an RSA key pair, which will be used for signing and verification of the JWT, wrapped in a JWK
	    RsaJsonWebKey rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);

	    // Give the JWK a Key ID (kid), which is just the polite thing to do
	    rsaJsonWebKey.setKeyId("k1");

	    // Create the Claims, which will be the content of the JWT
	    JwtClaims claims = new JwtClaims();
	    claims.setIssuer("Issuer");  // who creates the token and signs it
	    claims.setAudience("Audience"); // to whom the token is intended to be sent
	    claims.setExpirationTimeMinutesInTheFuture(10); // time when the token will expire (10 minutes from now)
	    claims.setGeneratedJwtId(); // a unique identifier for the token
	    claims.setIssuedAtToNow();  // when the token was issued/created (now)
	    claims.setNotBeforeMinutesInThePast(2); // time before which the token is not yet valid (2 minutes ago)
	    claims.setSubject("subject"); // the subject/principal is whom the token is about
	    claims.setClaim("email","mail@example.com"); // additional claims/attributes about the subject can be added
	    List<String> groups = Arrays.asList("group-one", "other-group", "group-three");
	    claims.setStringListClaim("groups", groups); // multi-valued claims work too and will end up as a JSON array
	    claims.setClaim("userId", userDetailsService.getUtente().getUsername());
	    
	    // A JWT is a JWS and/or a JWE with JSON claims as the payload.
	    // In this example it is a JWS so we create a JsonWebSignature object.
	    JsonWebSignature jws = new JsonWebSignature();

	    // The payload of the JWS is JSON content of the JWT Claims
	    jws.setPayload(claims.toJson());

	    // The JWT is signed using the private key
	    jws.setKey(rsaJsonWebKey.getPrivateKey());

	    // Set the Key ID (kid) header because it's just the polite thing to do.
	    // We only have one key in this example but a using a Key ID helps
	    // facilitate a smooth key rollover process
	    jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());

	    // Set the signature algorithm on the JWT/JWS that will integrity protect the claims
	    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

	    // Sign the JWS and produce the compact serialization or the complete JWT/JWS
	    // representation, which is a string consisting of three dot ('.') separated
	    // base64url-encoded parts in the form Header.Payload.Signature
	    // If you wanted to encrypt it, you can simply set this jwt as the payload
	    // of a JsonWebEncryption object and set the cty (Content Type) header to "jwt".
	    String jwt = jws.getCompactSerialization();


	    // Now you can do something with the JWT. Like send it to some other party
	    // over the clouds and through the interwebs.
	    System.out.println("JWT: " + jwt + "\n\n");
	    
	    
	    log.info("verification of jwt");
	    // Use JwtConsumerBuilder to construct an appropriate JwtConsumer, which will
	    // be used to validate and process the JWT.
	    // The specific validation requirements for a JWT are context dependent, however,
	    // it typically advisable to require a expiration time, a trusted issuer, and
	    // and audience that identifies your system as the intended recipient.
	    // If the JWT is encrypted too, you need only provide a decryption key or
	    // decryption key resolver to the builder.
	    JwtConsumer jwtConsumer = new JwtConsumerBuilder()
	            .setRequireExpirationTime() // the JWT must have an expiration time
	            .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
	            .setRequireSubject() // the JWT must have a subject claim
	            .setExpectedIssuer("Issuer") // whom the JWT needs to have been issued by
	            .setExpectedAudience("Audience") // to whom the JWT is intended for
	            .setVerificationKey(rsaJsonWebKey.getKey()) // verify the signature with the public key
	            .build(); // create the JwtConsumer instance

	    try
	    {
	        //  Validate the JWT and process it to the Claims
	        JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
	        System.out.println("JWT validation succeeded! " + jwtClaims);
	        
		    log.info("utente: " + jwtClaims.getClaimValue("userId"));
	    }
	    catch (InvalidJwtException e)
	    {
	        // InvalidJwtException will be thrown, if the JWT failed processing or validation in anyway.
	        // Hopefully with meaningful explanations(s) about what went wrong.
	        System.out.println("Invalid JWT! " + e);
	    }
	    return jwt;
	}
	
	public Indirizzo geocode(Indirizzo i) throws IOException {
		final Geocoder geocoder = new Geocoder();
		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(i.getIndirizzo() + ", " + i.getCivico() + ", " + i.getCap() + ", " + i.getLocalita() + ", " + i.getProvincia() + ", " + i.getNazione()).setLanguage("it").getGeocoderRequest();
		GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
		List<GeocoderResult> results = geocoderResponse.getResults();
		for ( GeocoderResult r : results ) {
			// System.out.println(r.toString());
			i.setLongitudine(r.getGeometry().getLocation().getLng().doubleValue());
			i.setLatitudine(r.getGeometry().getLocation().getLat().doubleValue());
		}
		return i;
	}
}
