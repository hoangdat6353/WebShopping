package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.model.AppDownloaded;

public interface AppDownloadedRepository extends JpaRepository<AppDownloaded, Long> {
	@Query("SELECT app FROM AppDownloaded app WHERE app.username = ?1 and app.appname = ?2")
	AppDownloaded findAppByUserNameAndAppName(String username, String appname);
	
	@Query("SELECT app FROM AppDownloaded app WHERE app.username = ?1 and app.payment = ?2")
	List<AppDownloaded> findAppByUsernameAndPayment(String username, String payment);
	
}