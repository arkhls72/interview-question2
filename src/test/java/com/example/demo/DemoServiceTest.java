package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.DemoArrayDTO;
import com.example.demo.manager.DemoCacheManager;
import com.example.demo.persistence.DemoArray;
import com.example.demo.persistence.DemoArrayRepository;
import com.example.demo.service.DemoService;
import com.example.demo.service.MapperService;
import com.google.gson.Gson;

@SuppressWarnings("unused")
@ExtendWith(MockitoExtension.class)

public class DemoServiceTest {

	@Mock
	private  DemoCacheManager demoCacheManager;

	@Mock
	private  DemoArrayRepository demoArrayRepository; 

	
	@Mock
	private  MapperService  mapperService;
	
	@InjectMocks
	private DemoService demoService; 
	
	@BeforeEach
	  void initUseCase() {
		demoService = new DemoService(demoCacheManager, demoArrayRepository, mapperService);
	 }
	
	
	@Test
	public void testSaveArray() {
		Integer key = 46825194;
		Integer [] numbers = {1,3,5,7,9};
		
		DemoArrayDTO dto = new DemoArrayDTO(key,numbers);
		when(demoCacheManager.getCacheKey(Mockito.any())).thenReturn(key);
		
		Optional<DemoArray> emptyOptional = Optional.empty();
		
		when(demoArrayRepository.findById(Mockito.any())).thenReturn(emptyOptional);
		
		
		Gson gson = new Gson();
		String jsonNumbers = gson.toJson(numbers);
		
		DemoArray entity = new DemoArray();
		entity.setId(key);
		entity.setNumbers(jsonNumbers);
		
		
		when(mapperService.toEntity(Mockito.any())).thenReturn(entity);
		when(demoArrayRepository.save(Mockito.any())).thenReturn(entity);
		when(mapperService.toDto(Mockito.any())).thenReturn(dto);
		when(demoCacheManager.add(Mockito.any())).thenReturn(dto);
	
		DemoArrayDTO response = demoService.saveArray(dto);
		
		assertNotNull(response);
		assertEquals(dto, response);
	}
	
	
	@Test
	public void getArrayId() {
		Integer key = 46825194;
		Integer [] numbers = {1,3,5,7,9};
		
		DemoArrayDTO dto = new DemoArrayDTO(key,numbers);		
		
		when(mapperService.toDto(Mockito.any())).thenReturn(dto);
		when(demoCacheManager.getByKey(Mockito.any())).thenReturn(null);
		
		Gson gson = new Gson();
		String jsonNumbers = gson.toJson(numbers);
		
		DemoArray entity = new DemoArray();
		entity.setId(key);
		entity.setNumbers(jsonNumbers);
		
		Optional<DemoArray> optional = Optional.of(entity);
		when(demoArrayRepository.findById(Mockito.any())).thenReturn(optional);
		
		DemoArrayDTO response = demoService.getArray(key);
		assertNotNull(response);
		assertEquals(response.getId(), key);
	}
	
	
	@Test
	public void testArrayIdNotFound() {
		Integer key = 46825194;
		Integer [] numbers = {1,3,5,7,9};
		
		DemoArrayDTO dto = new DemoArrayDTO(key,numbers);
		
		when(demoCacheManager.getByKey(Mockito.any())).thenReturn(null);
		
		Gson gson = new Gson();
		String jsonNumbers = gson.toJson(numbers);
		
		DemoArray entity = new DemoArray();
		entity.setId(key);
		entity.setNumbers(jsonNumbers);
		
		Optional<DemoArray> optional = Optional.empty();
		when(demoArrayRepository.findById(Mockito.any())).thenReturn(optional);
		HttpStatus status = null;
		try {
			DemoArrayDTO response = demoService.getArray(key);	
		} catch(ResponseStatusException ex) {
			  status = ex.getStatus();
		} finally {
			assertEquals(HttpStatus.NOT_FOUND, status);
		}
	}

	@Test
	public void testRanodmShuffledArray() {
		Integer key = 46825194;
		Integer [] numbers = {1,3,5,7,9};
		Integer [] shuffledNumbers = initShuffled(numbers);
		
		DemoArrayDTO dto = new DemoArrayDTO(key,numbers);
		
		when(demoCacheManager.getByKey(Mockito.any())).thenReturn(null);
		
		Gson gson = new Gson();
		String jsonNumbers = gson.toJson(numbers);
		
		DemoArray entity = new DemoArray();
		entity.setId(key);
		entity.setNumbers(jsonNumbers);
		
		Optional<DemoArray> optional = Optional.of(entity);
		when(demoArrayRepository.findById(Mockito.any())).thenReturn(optional);
		
		when(demoService.getArray(Mockito.any())).thenReturn(dto);
		when(demoService.getShuffled(Mockito.any())).thenReturn(dto);
		
		DemoArrayDTO response = demoService.getShuffled(key);
		
		String originalArrayOrder = getArrayElementString(numbers);
		String suffledArrayOrder  =  getArrayElementString(initShuffled(response.getNumbers()));
		
		assertNotEquals(originalArrayOrder, suffledArrayOrder);
	}
	
	@Test
	public void testInitShuffle() {
		Integer[] numbers = {1,7,9,4,3};
		Integer[] response = demoService.initShuffle(numbers);
		
		String numebrArrayString = getArrayElementString(numbers);
		String responseArrayString = getArrayElementString(response);
		
		assertNotNull(response);
		assertNotEquals(numebrArrayString, responseArrayString);
		
	}
	
	private String getArrayElementString(Integer[] numbers) {
		
		StringBuffer sb =  new StringBuffer();
		for (Integer item:numbers) {
			sb.append(item);
		}
		
		return sb.toString();
		
	}
	
	public Integer[] initShuffled(Integer[] numbers) {
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
