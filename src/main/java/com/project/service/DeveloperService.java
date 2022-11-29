//File Service của User - Chứa tất cả các chức năng liên quan đến việc truy vấn CSDL
//VD: Tìm tất cả, thêm, sửa xóa
package com.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.model.Developer;
import com.project.model.User;
import com.project.repository.DeveloperRepository;
 
@Service
@Transactional
public class DeveloperService {
 
    @Autowired
    private DeveloperRepository repo;
     
    public List<Developer> listAll() {
        return repo.findAll();
    }
     
    public void save(Developer dev) {
        repo.save(dev);
    }
     
    public Developer get(long id) {
        return repo.findById(id).get();
    }
     
    public void delete(long id) {
        repo.deleteById(id);
    }
    
    public Developer findByUsername(String username)
    {
    	return repo.getDeveloperByUsername(username);
    }
}