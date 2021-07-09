package com.olx.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olx.dto.AdvertiseDTO;
import com.olx.entity.AdvertiseEntity;
import com.olx.exception.InvalidAuthorizationTokenException;
import com.olx.repo.AdvertiseRepo;

@Service
public class AdvertiseServiceImpl implements AdvertiseService {

	@Autowired
	private AdvertiseRepo advertiseRepo;
	@Autowired
	private CategoryServiceDelegate categoryServiceDelegate;
	@Autowired
	private UserServiceDelegate userServiceDelegate;
	
	@Override
	public List<AdvertiseDTO> getAllAdvertises() {
		List<AdvertiseEntity> advertiseEntities = advertiseRepo.findAll();
		List<Map> listMapUsers = new ArrayList<>();
		advertiseEntities.stream().forEach((advertiseEntity)-> {
			listMapUsers.add(userServiceDelegate.findByUsername(advertiseEntity.getUsername()));
		});
		List<AdvertiseDTO> advertises = new ArrayList<AdvertiseDTO>();
		List<Map> categories = categoryServiceDelegate.getAllCategories();
		Map<Long, String> hmapCategories = new HashMap<>();
		categories.stream().forEach((categoryMap)-> {
			System.out.println("categoryMap: " + categoryMap);
			hmapCategories.put(new Long((Integer)categoryMap.get("id")), (String)categoryMap.get("name"));
		});
		
		advertiseEntities.stream().forEach((advertiseEntity)-> {
			Map tempMap = listMapUsers.stream().filter((mapUser)-> {
				return mapUser.get("username").equals(advertiseEntity.getUsername());
		}).findFirst().get();
			String postedBy = tempMap.get("firstName") + " " + tempMap.get("lastName");
			advertises.add(new AdvertiseDTO(advertiseEntity.getId(), advertiseEntity.getTitle(), advertiseEntity.getDescription(), advertiseEntity.getPrice(), advertiseEntity.getCategory(), hmapCategories.get(advertiseEntity.getCategory()), advertiseEntity.getCreatedDate(), advertiseEntity.getModifiedDate(), advertiseEntity.getActive(), advertiseEntity.getUsername(), postedBy));
		});
		return advertises;
	}

	@Override
	public AdvertiseDTO createNewAdvertise(AdvertiseDTO advertiseDto, String authToken,String username) {
		if(!userServiceDelegate.isLoggedInUser(authToken))
			throw new InvalidAuthorizationTokenException(authToken);
		advertiseDto.setUsername(username);
		List<Map> listMapUsers = new ArrayList<>();
		listMapUsers.add(userServiceDelegate.findByUsername(advertiseDto.getUsername()));
		Map tempMap = listMapUsers.stream().filter((mapUser)-> {
			return mapUser.get("username").equals(advertiseDto.getUsername());
		}).findFirst().get();
		String postedBy = tempMap.get("firstName") + " " + tempMap.get("lastName");
		AdvertiseEntity advertiseEntity = new AdvertiseEntity(advertiseDto.getTitle(), advertiseDto.getDescription(), advertiseDto.getPrice(), advertiseDto.getCategories(), LocalDate.now(), LocalDate.now(), "1", advertiseDto.getUsername());
		advertiseEntity = advertiseRepo.save(advertiseEntity);
		return new AdvertiseDTO(advertiseEntity.getId(), advertiseEntity.getTitle(), advertiseEntity.getDescription(), advertiseEntity.getPrice(), advertiseEntity.getCategory(), advertiseEntity.getCreatedDate(), advertiseEntity.getModifiedDate(), advertiseEntity.getActive(), advertiseEntity.getUsername(), advertiseDto.getPostedBy(),categoryServiceDelegate.getCategoryById(advertiseEntity.getCategory()).getName());
	}

	@Override
	public List<AdvertiseDTO> getAllAdvertisesByUsername(String username) {
		List<AdvertiseEntity> advertiseEntities = advertiseRepo.findByUsername(username);
		Map mapUser = userServiceDelegate.findByUsername(username);
		List<AdvertiseDTO> advertises = new ArrayList<AdvertiseDTO>();
		List<Map> categories = categoryServiceDelegate.getAllCategories();
		Map<Long, String> hmapCategories = new HashMap<>();
		categories.stream().forEach((categoryMap)->hmapCategories.put(new Long((Integer)categoryMap.get("id")), (String)categoryMap.get("name")));
		
		advertiseEntities.stream().forEach((advertiseEntity)-> 
			advertises.add(new AdvertiseDTO(advertiseEntity.getId(), advertiseEntity.getTitle(), advertiseEntity.getDescription(), advertiseEntity.getPrice(), advertiseEntity.getCategory(), hmapCategories.get(advertiseEntity.getCategory()), advertiseEntity.getCreatedDate(), advertiseEntity.getModifiedDate(), advertiseEntity.getActive(), advertiseEntity.getUsername(), (mapUser.get("firstName") + " " + mapUser.get("lastName"))))
		);
		return advertises;
	}

}
