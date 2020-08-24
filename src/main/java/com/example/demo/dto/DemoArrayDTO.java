package com.example.demo.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO data transfer object that is matching DemoArrayEntity
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter @Setter
@EqualsAndHashCode 
@ToString
public class DemoArrayDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer[] numbers;
	
	public DemoArrayDTO() {
		
	}
	
	public DemoArrayDTO(Integer id) {
		this.id = id;
	}

	public DemoArrayDTO(Integer[] numbers) {
		this.numbers = numbers;
	
	}
	public DemoArrayDTO(Integer id,Integer[] numbers) {
		this.id = id;
		this.numbers = numbers;
	
	}
}
