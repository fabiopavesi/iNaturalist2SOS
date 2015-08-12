package it.adamassoft.oauth;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuthConfiguration extends AuthorizationServerConfigurerAdapter {
	@Autowired
	private DataSource dataSource;

	@Bean
	public TokenStore tokenStore() {
		return new JdbcTokenStore(dataSource);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints)
			throws Exception {
		endpoints.tokenStore(tokenStore());
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients)
			throws Exception {
		clients.inMemory().withClient("curl").authorities("ROLE_ADMIN")
				.resourceIds("jaxenter").scopes("read", "write")
				.authorizedGrantTypes("client_credentials").secret("password")
				.and().withClient("web")
				.redirectUris("http://github.com/techdev-solutions/")
				.resourceIds("jaxenter").scopes("read")
				.authorizedGrantTypes("implicit")
				.and().withClient("acme")
		          .secret("acmesecret")
		          .authorizedGrantTypes("authorization_code", "refresh_token",
		              "password").scopes("openid");
	}
}