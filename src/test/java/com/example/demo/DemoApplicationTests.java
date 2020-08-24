package com.example.demo;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.demo.controller.DemoArrayController;

@SpringBootTest
class DemoApplicationTests {
	
	@Autowired
	private DemoArrayController controller;

	@Test
	void contextLoads() {
			assertThat(controller).isNotNull();
	}
}
