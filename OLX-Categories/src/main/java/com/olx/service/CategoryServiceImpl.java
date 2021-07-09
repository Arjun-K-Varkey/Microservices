package com.olx.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olx.dto.Category;
import com.olx.entity.CategoryEntity;
import com.olx.repo.CategoryRepo;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepo categoryRepo;
	
	@Override
	public List<Category> getAllCategories() {
		List<CategoryEntity> categoryEntities = categoryRepo.findAll();
		List<Category> categories = new ArrayList<>();
		categoryEntities.stream().forEach((categoryEntity)->categories.add(new Category(categoryEntity.getId(), categoryEntity.getName(), categoryEntity.getDescription())));
		return categories;
	}

	@Override
	public Category getCategoryById(long categoryId) {
		Optional<CategoryEntity> opCategoryEntity = categoryRepo.findById(categoryId);
		if(opCategoryEntity.isPresent()) {
			CategoryEntity categoryEntity = opCategoryEntity.get();
			return new Category(categoryEntity.getId(), categoryEntity.getName(), categoryEntity.getDescription());
		}
		return null;
	}

}
