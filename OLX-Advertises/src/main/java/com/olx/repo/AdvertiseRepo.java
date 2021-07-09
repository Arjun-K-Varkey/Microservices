package com.olx.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.olx.entity.AdvertiseEntity;

public interface AdvertiseRepo extends JpaRepository<AdvertiseEntity, Long>{
	public List<AdvertiseEntity> findByUsername(String username);
}
