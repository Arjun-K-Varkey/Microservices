package com.olx.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.olx.dto.Category;
import com.olx.entity.CategoryEntity;;

@Service
public class CategoryServiceDelegateImpl implements CategoryServiceDelegate {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private CircuitBreakerFactory circuitBreakerFactory;
	

	
	@Override
	public List<Map> getAllCategories() {
		CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
		List<Map> categories = 
				circuitBreaker.run(
						()->this.restTemplate.getForObject("http://localhost:9001/category", List.class),
						throwable -> categoryServiceFallback()
				);
		return categories;
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
	
	@Override
	public Category getCategoryById(long categoryId) {
		CategoryEntity categoryEntity = this.restTemplate.getForObject("http://MASTERDATA-SERVICE/category/"+ categoryId, CategoryEntity.class);
		return new Category(categoryEntity.getId(), categoryEntity.getName(), categoryEntity.getDescription());
	}

}
