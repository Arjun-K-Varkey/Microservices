package com.olx.service;

import java.util.List;

import com.olx.dto.AdvertiseDTO;

public interface AdvertiseService {
	public List<AdvertiseDTO> getAllAdvertises();
	public List<AdvertiseDTO> getAllAdvertisesByUsername(String username);
	public AdvertiseDTO createNewAdvertise(AdvertiseDTO advertiseDto, String authToken,String username);
}
