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
public class TopUp {
	 private Integer mathe;
	 private Integer soseri;
	 
	 public TopUp() {}
}
