package com.example.demo.controller;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.DemoArrayDTO;
import com.example.demo.dto.DemoCacheDTO;
import com.example.demo.service.DemoService;


/**
 * REST controller for managing {@link com.example.demo.DemoArray entity}.
 */
@RestController
public class DemoArrayController {
	private final DemoService demoService;

	public DemoArrayController(DemoService demoService) {
		super();
		this.demoService = demoService;
	}


    /**
     * {@code POST  /store} : Create a new demoArray object which contains array of integer
     * @param demoArrayDTO  to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body of the new demoArrayDTO, 
     *  or with status {@code 400 (Bad Request)} if the demoArrayDTO has already an ID.
     * @throws ResponseStatusException if not created
     */
	
	@PostMapping("/store")
	public ResponseEntity<DemoArrayDTO> save(@RequestBody DemoArrayDTO demoArrayDTO) throws ResponseStatusException {
		
		if (ArrayUtils.isEmpty(demoArrayDTO.getNumbers())) {
			 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "reuqest array is empty.");
		}
		
		return new ResponseEntity<DemoArrayDTO>(demoService.saveArray(demoArrayDTO), new HttpHeaders(), HttpStatus.CREATED);
	}
	

    /**
     * {@code GET  /store} :get a DemoArrayDTO which contains the ID of array numbers either in memory or database
     * @param numbers, array of  elements
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} with id of Array that found, or {@code 404 (not found)} 
     * 
     */
	
	@GetMapping("/store")
	public ResponseEntity<DemoArrayDTO> get(@RequestParam Integer[] numbers) throws ResponseStatusException {
		DemoArrayDTO response = demoService.getArrayId(numbers);
		return new ResponseEntity<DemoArrayDTO>(response, new HttpHeaders(), HttpStatus.OK);
	}	
	
    /**
     * {@code GET  /store/memory} : get the array with random order of elements 
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} , or {@code 404 (not found)} 
   
     */
	
	@GetMapping("/permutation")
	public ResponseEntity<DemoArrayDTO> getPermutation(@RequestParam Integer id) throws ResponseStatusException {
		
		DemoArrayDTO response = demoService.getShuffled(id);		
		return new ResponseEntity<DemoArrayDTO>(response, new HttpHeaders(), HttpStatus.OK);
	}		
	
    /**
     * {@code GET  /store/memory} : get the ID of numbers if found in memory
     * @param numbers, array of  elements
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} with id of Array that found, or {@code 404 (not found)} 
   
     */

	@GetMapping("/memory")
	public ResponseEntity<DemoArrayDTO> getFromMemory(@RequestParam Integer[] numbers) throws ResponseStatusException {
		DemoArrayDTO response = demoService.getIdFromMemory(numbers);
		return new ResponseEntity<DemoArrayDTO>(response, new HttpHeaders(), HttpStatus.OK);
	}	

    /**
     * {@code DELETE  /clear} 
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and message notification
   
     */
	@DeleteMapping("/clear/memory")
	public ResponseEntity<DemoCacheDTO> removeMemory() throws ResponseStatusException {
		DemoCacheDTO response = demoService.clearCache();
		return new  ResponseEntity<DemoCacheDTO>(response, new HttpHeaders(), HttpStatus.OK);
	}	
}
