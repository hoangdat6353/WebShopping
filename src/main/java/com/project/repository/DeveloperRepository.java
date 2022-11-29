//File Repository - chứa các câu query để gọi tới database liên quan đến User
package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.model.Developer;
import com.project.model.User;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {
	@Query("SELECT u FROM Developer u WHERE u.username = :username")
    public Developer getDeveloperByUsername(@Param("username") String username);
	
}