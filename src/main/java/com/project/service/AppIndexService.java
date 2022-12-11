package com.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.model.AppIndex;
import com.project.repository.AppIndexRepository;
 
@Service
@Transactional
public class AppIndexService {
 
    @Autowired
    private AppIndexRepository repo;
     
    public List<AppIndex> listAll() {
        return repo.findAll();
    }
     
    public void save(AppIndex appIndex) {
        repo.save(appIndex);
    }
     
    public AppIndex get(long id) {
        return repo.findById(id).get();
    }
     
    public void delete(long id) {
        repo.deleteById(id);
    }
}