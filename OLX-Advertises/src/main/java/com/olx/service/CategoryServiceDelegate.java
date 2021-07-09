package com.olx.service;

import java.util.List;
import java.util.Map;

import com.olx.dto.Category;
import com.olx.entity.CategoryEntity;

public interface CategoryServiceDelegate {
	public List<Map> getAllCategories();
	public Category getCategoryById(long categoryId);
}
