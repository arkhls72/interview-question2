package com.example.demo.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO data transfer object to send message to browser when memory is cleared.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
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
