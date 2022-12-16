package com.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.model.Categories;
import com.project.repository.CategoriesRepository;
 
@Service
@Transactional
public class CategoriesService {
 
    @Autowired
    private CategoriesRepository repo;
     
    public List<Categories> listAll() {
        return repo.findAll();
    }
     
    public void save(Categories categories) {
        repo.save(categories);
    }
     
    public Categories get(long id) {
        return repo.findById(id).get();
    }
     
    public void delete(long id) {
        repo.deleteById(id);
    }
    
    public Categories findByTentheloai(String tentheloai)
    {
    	return repo.getTentheloaiByTentheloai(tentheloai);
    }
}