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
@Table(name = "appindex")
public class AppIndex {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 private String imagename;
 private String appname;

public AppIndex() {
 }

 public Long getId() {
     return id;
 }
 
}