package com.project.repository;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.model.App;

public interface AppRepository extends JpaRepository<App, Long> {
	@Query("SELECT app FROM App app WHERE app.status = ?1")
	List<App> findAllAppsByStatus(String status);
	
	@Query("SELECT app FROM App app WHERE app.appname = ?1 and app.status = ?2")
	App findAppByNameAndStatus(String appname, String status);
	
	@Query("SELECT app FROM App app WHERE app.developer = ?1 and app.status = ?2")
	List<App> findAppByDeveloperAndStatus(String developer, String status);
	
	@Query("SELECT app FROM App app WHERE app.category = ?1 and app.status = ?2")
	List<App> findAppByCategoryAndStatus(String category, String status);
	
	@Query("SELECT app FROM App app WHERE app.developer = ?1 and app.payment = ?2")
	List<App> findAppByDeveloperAndPayment(String developer, String payment);
	
	List<App> findTop6ByStatusAndPaymentOrderByDownloadsDesc(String status, String payment);
	
	List<App> findByAppnameContainingIgnoreCase(String appname);
	
}