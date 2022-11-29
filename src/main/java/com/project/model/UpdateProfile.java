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
public class UpdateProfile {
	 private Long id;
	 private String fullname;
	 private String phone;
	 private String address;
	 private String dateofbirth;
	 
	 public UpdateProfile() {}
}
