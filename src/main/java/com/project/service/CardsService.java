package com.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.model.Cards;
import com.project.model.User;
import com.project.repository.CardsRepository;
 
@Service
@Transactional
public class CardsService {
 
    @Autowired
    private CardsRepository repo;
     
    public List<Cards> listAll() {
        return repo.findAll();
    }
     
    public void save(Cards categories) {
        repo.save(categories);
    }
     
    public Cards get(long id) {
        return repo.findById(id).get();
    }
     
    public void delete(long id) {
        repo.deleteById(id);
    }
    
    public Cards findCardsByCodeAndSeri(Integer mathecao, Integer soseri)
    {
    	return repo.findCardsByCodeAndSeri(mathecao, soseri);
    }
    
    public List<Cards> listAllCardsByUsername(String username) {
        return repo.findAllCardsByUsername(username);
    }
}