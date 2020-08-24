package com.example.demo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.controller.DemoArrayController;
import com.example.demo.dto.DemoArrayDTO;
import com.example.demo.dto.DemoCacheDTO;
import com.example.demo.manager.DemoCacheManager;
import com.example.demo.service.DemoService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 *  It is DemoArrayController Unit test. 
 * 
 */

@WebMvcTest(DemoArrayController.class)
public class DemoArrayControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DemoService demoService;
	
	@MockBean
	private DemoCacheManager demoCacheManager;
	
	
	/**
	 * @Test API: DemoArrayController::get(Integer[] numbers)
	 * @scenario : testing  ID of Array in response
	 * @throws Exception
	 */
	@Test
	public void testGet() throws Exception {
		Integer[] numbers = { 1, 9, 3, 6 };
		Integer key = getkey(numbers);
		DemoArrayDTO resp = new DemoArrayDTO(key);
		
		String expected = "{\"id\":" + key + "}";
		
		when(demoService.getArrayId(Mockito.any())).thenReturn(resp);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/store?numbers=1,9,3,6")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		
		System.out.println("response string: " + response.getContentAsString());
		JSONAssert.assertEquals(expected, response.getContentAsString(), true);
	}

	@Test
	public void testGetFromMemory() throws Exception {
		Integer[] numbers = { 1, 9, 3, 6 };
		Integer key = getkey(numbers);
		DemoArrayDTO resp = new DemoArrayDTO(key);
		
		String expected = "{\"id\":" + key + "}";
		
		when(demoService.getIdFromMemory(Mockito.any())).thenReturn(resp);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/memory?numbers=1,9,3,6")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		
		System.out.println("response string: " + response.getContentAsString());
		JSONAssert.assertEquals(expected, response.getContentAsString(), true);
	}
	
	@Test
	public void removeAllMemory() throws Exception {
		DemoCacheDTO resp = new DemoCacheDTO("Array elements were deleted from memory");		
		when(demoCacheManager.clearAll()).thenReturn(resp);
		 this.mockMvc.perform(MockMvcRequestBuilders
		            .delete("/clear/memory")
					.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk());
	}	
	
	/**
	 * @Test API: DemoArrayController::getPermutation(Integer[] numbers) 
	 * @scenario: testing order of the array
	 * @throws Exception
	 */
	@Test
	public void testGetPermutation() throws Exception {
		String notExpected = "{\"id\":1511130,\"numbers\":[1,9,3,6]}";
		
		Integer[] numbers = { 1, 9, 3, 6 };
		
		Integer key = getkey(numbers);
		DemoArrayDTO shuffledResponse = new DemoArrayDTO(key, shuffled(numbers));

		when(demoService.getShuffled(Mockito.any())).thenReturn(shuffledResponse);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/permutation?id=" + key)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		
		System.out.println("request  string: " + notExpected);
		System.out.println("response string: " + response.getContentAsString());
		JSONAssert.assertNotEquals(notExpected, response.getContentAsString(), true);
	}

	/**
	 * @Test API: DemoArrayController::save(Integer[] numbers) 
	 * @scenario: testing persistent of the array numbers
	 * @throws Exception
	 */
	
	@Test
	public void testSave() throws Exception {

		Integer[] numbers = { 1, 9, 3, 6 };
		DemoArrayDTO request = new DemoArrayDTO(numbers);
		DemoArrayDTO response = new DemoArrayDTO(getkey(numbers), numbers);

		when(demoService.saveArray(Mockito.any())).thenReturn(response);
		mockMvc.perform(MockMvcRequestBuilders.post("/store")
				.content(asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

	}

	/**
	 * @param array numbers[] 
	 * @ utility to generate  memory key
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
	 * @param any Object
	 * @ utility to create JSON string 
	 * @return JSON string 
	 */
	private  String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param numbers array of Integer
	 * @ utility to to change the order of array elements randomly 
	 * @return array of Integer
	 */
	private Integer[] shuffled(Integer[] numbers) {
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
