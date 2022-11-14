//Model mẫu demo - không sử dụng đến trong bài
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
public class Product {
 private Long id;
 private String name;
 private String brand;
 private String madein;
 private float price;

public Product() {
 }

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 public Long getId() {
     return id;
 }
  
}