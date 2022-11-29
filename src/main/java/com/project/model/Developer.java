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
public class Developer {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 private String username;
 private String devname;
 private String devphone;
 private String devaddress;
 private String devemail;
 private String credentialfront;
 private String credentialback;

public Developer() {
 }

 public Long getId() {
     return id;
 }
  
}