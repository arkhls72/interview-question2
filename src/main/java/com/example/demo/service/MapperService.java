package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.DemoArrayDTO;
import com.example.demo.persistence.DemoArray;
import com.google.gson.Gson;
/**
 * Service Implementation for converting  {@link DemoArray} to DemoArrayDTO and vice versa
 */


@Service
public class MapperService {
	
	/**
	 * Converts DemoArrayDTO to entity  
	 * @param source
	 * @return entity DemoArray 
	 */
	public DemoArray toEntity(DemoArrayDTO source) {
		
		DemoArray target = new DemoArray();
		target.setId(source.getId());
		Gson gson = new Gson();
		target.setNumbers(gson.toJson(source.getNumbers()));
		
		return target;
	}
	
	/**
	 * Converts DemoArray entity to DTO  
	 * @param source
	 * @return DTO  DemoArrayDTO 
	 */
	public DemoArrayDTO toDto(DemoArray source) {
		
		DemoArrayDTO target = new DemoArrayDTO();
		target.setId(source.getId());
		Gson gson = new Gson();
		target.setNumbers(gson.fromJson(source.getNumbers(), Integer[].class));
		
		return target;
	}
}
