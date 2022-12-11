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
@Table(name = "appdownloaded")
public class AppDownloaded {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 private String username;
 private String appname;
 private String category;
 private String iconimage;
 private String payment;

public AppDownloaded() {
 }

 public Long getId() {
     return id;
 }
 
}