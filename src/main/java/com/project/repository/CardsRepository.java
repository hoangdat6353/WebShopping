package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.model.Cards;
import com.project.model.User;

public interface CardsRepository extends JpaRepository<Cards, Long> {
	@Query("SELECT tc FROM Cards tc WHERE tc.mathecao = ?1 and tc.soseri = ?2")
	Cards findCardsByCodeAndSeri(Integer mathecao, Integer soseri);
	
	@Query("SELECT tc FROM Cards tc WHERE tc.nguoinap = ?1")
	List<Cards> findAllCardsByUsername(String username);
 
}