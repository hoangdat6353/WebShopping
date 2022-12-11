package com.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.model.App;
import com.project.model.AppDownloaded;
import com.project.repository.AppDownloadedRepository;
 
@Service
@Transactional
public class AppDownloadedService {
 
    @Autowired
    private AppDownloadedRepository repo;
     
    public List<AppDownloaded> listAll() {
        return repo.findAll();
    }
     
    public void save(AppDownloaded appDownloaded) {
        repo.save(appDownloaded);
    }
     
    public AppDownloaded get(long id) {
        return repo.findById(id).get();
    }
     
    public void delete(long id) {
        repo.deleteById(id);
    }
    public AppDownloaded findAppByUserNameAndAppName(String username, String appname) {
    	return repo.findAppByUserNameAndAppName(username, appname);
    }
    public List<AppDownloaded> findAppByUserNameAndPayment(String username, String payment) {
    	return repo.findAppByUsernameAndPayment(username, payment);
    }
}