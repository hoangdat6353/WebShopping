//File Repository - chứa các câu query để gọi tới database liên quan đến User
package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
 
}