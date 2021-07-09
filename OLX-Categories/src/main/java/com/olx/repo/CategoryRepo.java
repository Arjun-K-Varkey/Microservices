package com.olx.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.olx.entity.CategoryEntity;

public interface CategoryRepo extends JpaRepository<CategoryEntity, Long>{

}
