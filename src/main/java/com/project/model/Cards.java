package com.project.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Entity
@ToString
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "thecao")
public class Cards {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 private int mathecao;
 private int soseri;
 private int menhgia;
 private String ngaynap;
 private String trangthai;
 private String nguoinap;

public Cards() {
 }

 public Long getId() {
     return id;
 }
 
}