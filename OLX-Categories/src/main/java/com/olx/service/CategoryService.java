package com.olx.service;

import java.util.List;

import com.olx.dto.Category;

public interface CategoryService {

	public List<Category> getAllCategories();
	public Category getCategoryById(long categoryId);
}
