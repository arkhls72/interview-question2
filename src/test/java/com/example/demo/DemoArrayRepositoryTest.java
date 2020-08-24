package com.example.demo;
/**
 *  It is repository unit test for DemoArray entity
 */
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.persistence.DemoArray;
import com.example.demo.persistence.DemoArrayRepository;
import com.google.gson.Gson;
@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(locations="classpath:application.yml")
public class DemoArrayRepositoryTest {
	
	@Autowired
	private DemoArrayRepository demoArrayRepository;
	
	
	@Test
	@Transactional
	public void test_1_SaveArray() {
		Integer[] numbers = {1,2,3,4,5};
		Integer key = getkey(numbers);
		Gson gson = new Gson ();
		
		DemoArray entity = new DemoArray();
		entity.setId(key);
		String numberJson = gson.toJson(entity.getNumbers());
		entity.setNumbers(numberJson);
		demoArrayRepository.save(entity);
		demoArrayRepository.flush();
		assertEquals(entity.getId().intValue(), key.intValue());
		assertEquals(entity.getNumbers(), numberJson);
		
		Optional<DemoArray>  opetional= demoArrayRepository.findById(key);
		
		entity = opetional.get();
		assertNotNull(entity);
		assertNotNull(entity.getId());
		assertEquals(entity.getId().intValue(), key.intValue());
		assertEquals(entity.getNumbers(), numberJson);
		
	}
	
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
}
