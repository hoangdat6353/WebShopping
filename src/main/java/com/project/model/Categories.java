package com.project.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.*;

@Entity
@ToString
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Categories {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 private String tentheloai;
 private int soluong;

public Categories() {
 }

 public Long getId() {
     return id;
 }
 
}