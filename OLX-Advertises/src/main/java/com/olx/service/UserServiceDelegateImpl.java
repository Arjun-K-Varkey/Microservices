package com.olx.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceDelegateImpl implements UserServiceDelegate {

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	CircuitBreakerFactory circuitBreakerFactory;

	
	@Override
	public Map findByUsername(String username) {
		CircuitBreaker circuitBreaker = circuitBreakerFactory.create("userCircuitBreaker");
		Map mapUser = circuitBreaker.run(
			()->this.restTemplate.getForObject("http://auth-service/user/" + username, Map.class),
			throwable -> callbackFindByUsername(username)
		);
		return mapUser;
	}
	
	public Map callbackFindByUsername(String username) {
		System.out.println("Circuit Breaker: Failure in findByUsername - " + username);
		return null;
	}
	
	@Override
	public List<Map> findByUsernames(String usernames) {
		CircuitBreaker circuitBreaker = circuitBreakerFactory.create("userListCircuitBreaker");
		List<Map> listMapUsers = circuitBreaker.run(
			()->this.restTemplate.getForObject("http://auth-service/user/list/" + usernames, List.class),
			throwable -> callbackFindByUsernames(usernames)
		);
		return listMapUsers;
	}
	public List<Map> callbackFindByUsernames(String usernames) {
		System.out.println("Circuit Breaker: Failure in findByUsernames - " + usernames);
		return null;
	}
	
	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
		return new RestTemplate();
	}
	@Override
	public boolean isLoggedInUser(String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", authToken);
		HttpEntity entity = new HttpEntity(headers);
		try {
			ResponseEntity<Boolean> response = 
					this.restTemplate.exchange("http://auth-service/token/validate",  HttpMethod.GET, entity, Boolean.class);
			if(response.getStatusCode() == HttpStatus.OK)
				return response.getBody();
			else
				return false;
		}
		catch(Exception e) {
			return false;
		}
	}
}
