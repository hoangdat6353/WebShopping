//File Service của User - Chứa tất cả các chức năng liên quan đến việc truy vấn CSDL
//VD: Tìm tất cả, thêm, sửa xóa
package com.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.model.User;
import com.project.repository.UserRepository;
 
@Service
@Transactional
public class UserService {
 
    @Autowired
    private UserRepository repo;
     
    public List<User> listAll() {
        return repo.findAll();
    }
     
    public void save(User user) {
        repo.save(user);
    }
     
    public User get(long id) {
        return repo.findById(id).get();
    }
     
    public void delete(long id) {
        repo.deleteById(id);
    }
    
    public User findByUsername(String username)
    {
    	return repo.getUserByUsername(username);
    }
    
    
}