package com.example.demo;
/**
 * DemoArray  Integration test
 * The sequence order of unit test execution is alpha numeric ascending 
 * first save() is run to populate the data base then Get. 
 * Once all get() triggered the memory will be cleared then run another test to ensure the cache is empty.
 */
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.dto.DemoArrayDTO;
import com.example.demo.dto.DemoCacheDTO;
@TestMethodOrder(Alphanumeric.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DemoSeverBootTestIT {
	private int randomInt = 0;
	
	/**
	 * create a random ID to add to unit test array 
	 * 
	 */
 	@BeforeAll
	public  static void init(){
 		DemoSeverBootTestIT i = new DemoSeverBootTestIT();
	     i.randomInt = getNextRandomInteger();
	}
 	
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	
	/**
	 *  @test case:  saving 1 array with 5 elements in to DB.H2
	 * @throws Exception if error occurred 
	 */
	@Test
	public void a_save_1() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		Integer[] numbers = { 9, 5, 3, 2 ,randomInt};
		
		Integer key = getkey(numbers);

		DemoArrayDTO request = new DemoArrayDTO(numbers);
		HttpEntity<DemoArrayDTO> requestEntity = new HttpEntity<DemoArrayDTO>(request, headers);

		ResponseEntity<DemoArrayDTO> response = restTemplate.exchange(createURLWithPort("/store"), HttpMethod.POST,
				requestEntity, DemoArrayDTO.class);

		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(key, response.getBody().getId());
	}

	/**
	 *  @test case: get() by query parameter array numbers 
	 * @throws Exception if error occurred 
	 */
	
	@Test
	public void b_get_2() throws Exception {
		Integer[] numbers = { 9, 5, 3, 2 ,randomInt};
		Integer key = getkey(numbers);

    	String uri = createURLWithPort("/store?numbers=") +  getArrayToString(numbers);

    	ResponseEntity<DemoArrayDTO> result = restTemplate.getForEntity(uri, DemoArrayDTO.class);
    	
    	assertNotNull(result);
    	assertNotNull(result.getBody());
    	assertEquals(HttpStatus.OK, result.getStatusCode());
    	assertEquals(key, result.getBody().getId());
    	
	}


	/**
	 *  @test case: get() permutation random order of elements in array by array numbers 
	 * @throws Exception if error occurred 
	 */

	@Test
	public void c_getPermutation_3() throws Exception {
		Integer[] numbers = { 9, 5, 3, 2 ,randomInt};
		Integer key = getkey(numbers);
		
		String stringNumbers = getArrayToString(numbers);
    	String uri = createURLWithPort("/permutation?numbers=") +  getArrayToString(numbers);

    	ResponseEntity<DemoArrayDTO> result = restTemplate.getForEntity(uri, DemoArrayDTO.class);
    	
    	assertNotNull(result);
    	assertNotNull(result.getBody());
    	assertEquals(HttpStatus.OK, result.getStatusCode());
    	assertEquals(key, result.getBody().getId());
    	
    	String stringResponse = getArrayToString(result.getBody().getNumbers());
    	
    	assertNotEquals(stringNumbers, stringResponse);
    	
	}
	
	/**
	 *  @test case: get() array only from cache 
	 * @throws Exception if error occurred 
	 */

	@Test
	public void d_getfromMemory_4() throws Exception {
		Integer[] numbers = { 9, 5, 3, 2 ,randomInt};
		Integer key = getkey(numbers);

    	String uri = createURLWithPort("/memory?numbers=") +  getArrayToString(numbers);

    	ResponseEntity<DemoArrayDTO> result = restTemplate.getForEntity(uri, DemoArrayDTO.class);
    	
    	assertNotNull(result);
    	assertNotNull(result.getBody());
    	assertEquals(HttpStatus.OK, result.getStatusCode());
    	assertEquals(key, result.getBody().getId());
    	
	}
	
	/**
	 *  @test case: clear the cache
	 * @throws Exception if error occurred 
	 */
	@Test
	public void e_deleteMemory_5() throws Exception {
		ResponseEntity<DemoCacheDTO> response = restTemplate.exchange(createURLWithPort("/clear/memory"), HttpMethod.DELETE,null, DemoCacheDTO.class);
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	/**
	 *  @test case: get array from cache only 
	 * @throws Exception if error occurred 
	 */
	
	@Test
	public void f_getfromMemory_6() throws Exception {
		Integer[] numbers = { 9, 5, 3, 2 ,randomInt};
    	String uri = createURLWithPort("/memory?numbers=") +  getArrayToString(numbers);
    	
    	ResponseEntity<DemoArrayDTO> result = restTemplate.getForEntity(uri, DemoArrayDTO.class);
    	assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    	
	}
	
	
	/**
	 *  @test case: get array from DB.H2. since cache has been wiped  earlier in e_deleteMemory_5 then array is retrieved from DB.H2
	 * @throws Exception if error occurred 
	 */
	
	@Test
	public void g_get_7() throws Exception {
		Integer[] numbers = { 9, 5, 3, 2 ,randomInt};
		Integer key = getkey(numbers);

    	String uri = createURLWithPort("/store?numbers=") +  getArrayToString(numbers);

    	ResponseEntity<DemoArrayDTO> result = restTemplate.getForEntity(uri, DemoArrayDTO.class);
    	
    	assertNotNull(result);
    	assertNotNull(result.getBody());
    	assertEquals(HttpStatus.OK, result.getStatusCode());
    	assertEquals(key, result.getBody().getId());
    	
	}
	
	/**
	 * @param array numbers[] @ utility to generate memory key
	 * @return Integer key
	 */
	private Integer getkey(Integer[] numbers) {
		if (ArrayUtils.isEmpty(numbers)) {
			return null;
		}

		Integer[] copy = Arrays.copyOf(numbers, numbers.length);

		Arrays.sort(copy);

		String key = "";
		for (Integer code : copy) {
			key += code.toString();

		}
		return Integer.valueOf(new HashCodeBuilder().append(key).toHashCode());
	}

	/**
	 * 
	 * @param uri; of local host
	 * @return String and port
	 */
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

	/**
	 * @return 2 digits random int 
	 */
	private static Integer getNextRandomInteger() {
		Random random = new Random();
		
	    return random.nextInt(100);
		
	}
	
	/**
	 * 
	 * @param numbers
	 * @return string of array content with comma ',' delimiter
	 */
	private String getArrayToString(Integer[] numbers) {
		
		String result = "";
		if (numbers != null && numbers.length > 0) {
			StringBuilder sb = new StringBuilder();
			for (Integer i : numbers) {
				sb.append(i.toString()).append(","); 
			} 
			result = sb.deleteCharAt(sb.length() - 1).toString();
			
		} 
		return result; 
	}
}
