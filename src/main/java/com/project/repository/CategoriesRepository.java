package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.model.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {
	@Query("SELECT ca FROM Categories ca WHERE ca.tentheloai = :tentheloai")
    public Categories getTentheloaiByTentheloai(@Param("tentheloai") String tentheloai);
	
}