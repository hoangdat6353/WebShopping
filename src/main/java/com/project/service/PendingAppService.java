package com.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.model.PendingApp;
import com.project.repository.PendingAppsRepository;
 
@Service
@Transactional
public class PendingAppService {
 
    @Autowired
    private PendingAppsRepository repo;
     
    public List<PendingApp> listAll() {
        return repo.findAll();
    }
     
    public void save(PendingApp pendingApp) {
        repo.save(pendingApp);
    }
     
    public PendingApp get(long id) {
        return repo.findById(id).get();
    }
     
    public void delete(long id) {
        repo.deleteById(id);
    }
   
    public List<PendingApp> listAllPendingAppsByStatusAndDevName(String status,String devName) {
        return repo.findPendingApps(status, devName);
    }
    public List<PendingApp> listAllPendingAppsByStatus(String status) {
        return repo.listAllPendingAppsByStatus(status);
    }
}