package com.olx.service;

import java.util.List;

import com.olx.dto.AdvertiseDTO;


public interface AdvertiseServiceDelegate {
	public List<AdvertiseDTO> getAllAdvertises();
	public List<AdvertiseDTO> getAllAdvertisesByUsername(String username);
}
