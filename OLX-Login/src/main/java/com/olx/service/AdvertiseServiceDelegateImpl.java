package com.olx.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.olx.dto.AdvertiseDTO;

@Service
public class AdvertiseServiceDelegateImpl implements AdvertiseServiceDelegate {
	
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private CircuitBreakerFactory circuitBreakerFactory;
	
	@Override
	public List<AdvertiseDTO> getAllAdvertises() {
		CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
		List<AdvertiseDTO> advertises = 
				circuitBreaker.run(
						()->this.restTemplate.getForObject("http://ADVERTISE-SERVICE/advertise/", List.class),
						throwable -> categoryServiceFallback()
				);	
		return advertises;
	}
	
	@Override
	public List<AdvertiseDTO> getAllAdvertisesByUsername(String username) {
		CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
		List<AdvertiseDTO> advertises = 
				circuitBreaker.run(
						()->this.restTemplate.getForObject("http://ADVERTISE-SERVICE/advertise/"+username, List.class),
						throwable -> categoryServiceFallback()
				);			
		return advertises;
	}

	public List<Map> categoryServiceFallback() {
		System.out.println("CIRCUIT BREAKER ENABLED!!! No Response From Category Service at this moment. " +
                " Service will be back shortly - " + LocalDate.now());
		return null;
	}
	
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
