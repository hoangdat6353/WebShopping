package com.project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ChangePass {
	 private String username;
	 private String password;
	 private String newPassword;
	 
	 public ChangePass() {}
}
