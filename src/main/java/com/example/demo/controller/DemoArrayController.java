package com.example.demo.controller;

import javax.validation.constraints.NotNull;

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
     * {@code POST  /store} : Create a new DemoArrayDTO.numbers.
     * @param ehcClientDTO the ehcClientDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new DemoArrayDTO containing numbers and id, 
     *           or with status {@code 400 (sever error)} if array numbers has already an ID or the passing DTO has an empty array numbers
     * @throws ResponseStatusException 
     */
	@PostMapping("/store")
	public ResponseEntity<DemoArrayDTO> save(@RequestBody DemoArrayDTO demoArray) throws ResponseStatusException {
		
		if (ArrayUtils.isEmpty(demoArray.getNumbers())) {
			 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "reuqest array is empty.");
		}
		
		return new ResponseEntity<DemoArrayDTO>(demoService.save(demoArray), new HttpHeaders(), HttpStatus.CREATED);
	}
	
	@GetMapping("/store")
	public ResponseEntity<DemoArrayDTO> get(@RequestParam Integer[] numbers) throws ResponseStatusException {
		DemoArrayDTO response = demoService.getId(numbers);
		return new ResponseEntity<DemoArrayDTO>(response, new HttpHeaders(), HttpStatus.OK);
	}	
	
	@GetMapping("/permutation")
	public ResponseEntity<DemoArrayDTO> getPermutation(@NotNull @RequestParam Integer id) throws ResponseStatusException {
		
		DemoArrayDTO response = demoService.getSuffeled(id);
		
		if (response == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Array not found.");
		}
		
		return new ResponseEntity<DemoArrayDTO>(response, new HttpHeaders(), HttpStatus.OK);
	}		
	
	@DeleteMapping("/clear/memory")
	public ResponseEntity<DemoCacheDTO> removeMemory() throws ResponseStatusException {
		DemoCacheDTO response = demoService.clearCache();
		return new  ResponseEntity<DemoCacheDTO>(response, new HttpHeaders(), HttpStatus.OK);
	}	
}
