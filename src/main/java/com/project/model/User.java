//Model người dùng
package com.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	
	 @NotNull(message = "User's name cannot be empty")
	 private String username;
	 @Size(min=8, max=12, message = "Password must be between 8 and 12 characters")
	 private String password;
	 private String role;
	 @Email(message = "Email should be valid")
	 private String email;
	 @Nullable
	 private String fullname;
	 @Nullable
	 private String phone;
	 @Nullable
	 private String address;
	 @Nullable
	 private String dateofbirth;
	 @Nullable
	 private String avatar; 
	 @Nullable
	 private String devavatar;
	 
	 private int balance;
	 
	 @Column(name = "reset_password_token")
	 private String resetPasswordToken;

	public User() {
	 }

	 public Long getId() {
	     return id;
	 }
	 
	 public String getAvatarImagePath() {
	  if (avatar == null || id == null) return null;
	         return "/images/" + avatar;
	  }
}
