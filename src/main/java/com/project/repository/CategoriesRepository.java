package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.model.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {
 
}