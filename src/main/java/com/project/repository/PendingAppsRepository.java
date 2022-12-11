package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.model.PendingApp;

public interface PendingAppsRepository extends JpaRepository<PendingApp, Long> {
	@Query("SELECT pa FROM PendingApp pa  WHERE pa.status = ?1 and pa.developer = ?2")
	List<PendingApp> findPendingApps(String status,String devName);
	
	@Query("SELECT pa FROM PendingApp pa  WHERE pa.status = ?1")
	List<PendingApp> listAllPendingAppsByStatus(String status);
}