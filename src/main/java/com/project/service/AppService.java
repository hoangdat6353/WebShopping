package com.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.model.App;
import com.project.repository.AppRepository;
 
@Service
@Transactional
public class AppService {
 
    @Autowired
    private AppRepository repo;
     
    public List<App> listAll() {
        return repo.findAll();
    }
     
    public void save(App app) {
        repo.save(app);
    }
     
    public App get(long id) {
        return repo.findById(id).get();
    }
     
    public void delete(long id) {
        repo.deleteById(id);
    }
    public List<App> listAllAppsByStatus(String status) {
        return repo.findAllAppsByStatus(status);
    }
    public App findAppByNameAndStatus(String appname, String status) {
    	return repo.findAppByNameAndStatus(appname, status);
    }
    public List<App> listAllAppsByDeveloperAndStatus(String developer,String status) {
        return repo.findAppByDeveloperAndStatus(developer,status);
    }
    public List<App> listAllAppsByCategoryAndStatus(String category,String status) {
        return repo.findAppByCategoryAndStatus(category, status);
    }
    public List<App> listAllAppsByDeveloperAndPayment(String developer,String payment) {
        return repo.findAppByDeveloperAndPayment(developer, payment);
    }
    public List<App> listAllAppsByStatusAndPayment(String status,String payment) {
        return repo.findTop6ByStatusAndPaymentOrderByDownloadsDesc(status, payment);
    }
    public List<App> findByAppnameContainingIgnoreCase(String appname)
    {
    	return repo.findByAppnameContainingIgnoreCase(appname);
    }
}