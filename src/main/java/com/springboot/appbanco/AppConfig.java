package com.springboot.appbanco;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration

public class AppConfig {
	
	@Value("${config.base.endpoint}")
	private String url;
	
	@Value("${config.base.endpoint.client}")
	private String urlClie;
	
	@Value("${config.base.endpoint.person}")
	private String urlPerso;
	
	@Value("${config.base.endpoint.accCredit}")
	private String urlAccCred;
	

	@Bean
	@Qualifier("cuenta")
	public WebClient registrarWebClient() {

		return WebClient.create(url); //EndPoint para conectarse con ese MS.
	}
	
	@Bean
	@Qualifier("cliente")
	public WebClient registrarWebClientByClient() {

		return WebClient.create(urlClie); //EndPoint para conectarse con ese MS.
	}
	@Bean
	@Qualifier("persona")
	public WebClient registrarWebClientByPerson() {

		return WebClient.create(urlPerso); //EndPoint para conectarse con ese MS.
	}
	
	@Bean
	@Qualifier("cuentaCredito")
	public WebClient registrarWebClientByAccCredit() {

		return WebClient.create(urlAccCred); //EndPoint para conectarse con ese MS.
	}
	
	
	
}
