package com.olx.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.olx.dto.AdvertiseDTO;

@Service
public class AdvertiseServiceDelegateImpl implements AdvertiseServiceDelegate {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public List<AdvertiseDTO> getAllAdvertises() {
		List<AdvertiseDTO> advertises = 
						//this.restTemplate.getForObject("http://localhost:9001/category", List.class);
				this.restTemplate.getForObject("http://ADVERTISE-SERVICE/advertise/", List.class);
		System.out.println(advertises.size());
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
