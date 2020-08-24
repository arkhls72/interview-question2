package com.example.demo.dto;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@EqualsAndHashCode 
@ToString
public class DemoCacheDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private String message;
	
	public DemoCacheDTO(String message) {
		this.message = message;
	}
}
