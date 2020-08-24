package com.example.demo.service;

/**
 * Service Implementation for managing {@link DemoArray}.
 */

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import javax.transaction.Transactional;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.DemoArrayDTO;
import com.example.demo.dto.DemoCacheDTO;
import com.example.demo.manager.DemoCacheManager;
import com.example.demo.persistence.DemoArray;
import com.example.demo.persistence.DemoArrayRepository;
@Service
public class DemoService {	
	
	private final DemoCacheManager demoCacheManager;
	
	private final DemoArrayRepository demoArrayRepository; 
	
	private final MapperService  mapperService;


	public DemoService(DemoCacheManager demoCacheManager, DemoArrayRepository demoArrayRepository,
			MapperService mapperService) {
		super();
		this.demoCacheManager = demoCacheManager;
		this.demoArrayRepository = demoArrayRepository;
		this.mapperService = mapperService;
	}
	
	/**
	 * 
	 * @param demoArrayDTO: contain new array to persist in H2 and save in memory cache.
	 * @return DemoArrayDTO : persisted array that is just created. 
 	 */
	@Transactional
	public DemoArrayDTO saveArray(DemoArrayDTO demoArrayDTO) {		
		Integer hashKey = demoCacheManager.getCacheKey(demoArrayDTO.getNumbers());

		Optional<DemoArray> optional = demoArrayRepository.findById(hashKey);
		
		if (optional.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"DemoArray record already exists: " + optional.get().getId());
		}
		
		demoArrayDTO.setId(hashKey);
		DemoArray entity = mapperService.toEntity(demoArrayDTO);
		demoArrayRepository.save(entity);
		
		DemoArrayDTO response = mapperService.toDto(entity);
		
		DemoArrayDTO cacheResponse  = demoCacheManager.add(response.getNumbers());	
		if (cacheResponse == null) {
			// TODO send a message notification that cache is not working properly
		}
		
		return response;
	}
	
	/**
	 * @param numbers
	 * @return DemoArrayDTO which contains ID
	 * 
	 * @lookups Array in memory  first , if found return the ID otherwise query H2
	 * @ throws ResponseStatusException if array not found  either in memory or Database throws   
	 */
	public DemoArrayDTO getArrayId( Integer[] numbers) {
		Integer hashKey = demoCacheManager.getCacheKey(numbers);
		ValueWrapper valueWrapper = demoCacheManager.getByKey(hashKey);
		if (valueWrapper != null) {
			return  new DemoArrayDTO(hashKey);
		}
		
		Optional<DemoArray> optional = demoArrayRepository.findById(hashKey);
		if (optional.isPresent()) {
			return  new DemoArrayDTO(optional.get().getId());
		}
		
		throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No DemoArray record found.");
	}

	/**
	 * @param numbers
	 * @return DemoArrayDTO
	 * 
	 * @lookups Array in memory if found return the ID 
	 * @ throws ResponseStatusException if array not found in memory   
	 */
	
	public DemoArrayDTO getIdFromMemory( Integer[] numbers) {
		Integer hashKey = demoCacheManager.getCacheKey(numbers);
		ValueWrapper valueWrapper = demoCacheManager.getDemoCache().get(hashKey);
		if (valueWrapper != null) {
			return  new DemoArrayDTO(hashKey);
		}
		
		throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No DemoArray not found in memory.");
	}

	/**
	 * @param numbers
	 * @return DemoArrayDTO which contains ID and numbers array
	 * 
	 * @lookups Array in memory first if found return it otherwise lookups H2  
	 * @ throws ResponseStatusException if array not found in memory nore H2  
	 */

	
	public DemoArrayDTO getArray( Integer id) {
		ValueWrapper valueWrapper = demoCacheManager.getByKey(id);
		if (valueWrapper != null) {
			return  new DemoArrayDTO(id,  (Integer[]) valueWrapper.get());
		}
		
		Optional<DemoArray> optional = demoArrayRepository.findById(id);
		
		if (optional.isPresent()) {
			return mapperService.toDto(optional.get());
		}
		
		throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No DemoArray record found.");
	}

	/**
	 * @param numbers
	 * @return DemoArrayDTO which contains a random ordering of an array.
	
	 * shuffles the given elements of array randomly   
	 * @ throws ResponseStatusException if array not found in memory nore H2  
	 */
	public DemoArrayDTO getShuffled(Integer id) {
		
		DemoArrayDTO demo = getArray(id);
		
		if (demo == null || ArrayUtils.isEmpty(demo.getNumbers())) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Array not found.");
		}
		
		return new DemoArrayDTO(demo.getId(),initShuffle(demo.getNumbers()));
		
 	}
	
	/**
	 * @ Clear entire cache	
	 * @return notification message 
	 */
	public DemoCacheDTO clearCache() {
		return demoCacheManager.clearAll();
	}
	
	/**
	 * 
	 * @param numbers
	 * @return  a random shuffled numbers
	 */
	public Integer[] initShuffle(Integer[] numbers) {
		Integer[] copy = Arrays.copyOf(numbers, numbers.length);		
		Random random = new Random(); 
		for (int i = 0; i < copy.length; i++) {
			int rp = random.nextInt(copy.length);
			int temp = copy[i];
			copy[i] = copy[rp];
			copy[rp] = temp;
		}

		return copy;
	}
}
