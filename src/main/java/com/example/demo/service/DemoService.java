package com.example.demo.service;

import java.util.Arrays;
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

@Service
public class DemoService {

	private final DemoCacheManager demoCacheManager;

	public DemoService(DemoCacheManager demoCacheManager) {
		super();
		this.demoCacheManager = demoCacheManager;
	}

	@Transactional
	public DemoArrayDTO save(DemoArrayDTO source) {
		Integer hashKey = demoCacheManager.getCacheKey(source.getNumbers());
		if (demoCacheManager.getDemoCache().get(hashKey) != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The DemoArray already exists.");
		}
		demoCacheManager.getDemoCache().put(hashKey, source.getNumbers());
		source.setId(hashKey);
		return source;
	}

	public DemoArrayDTO getId(Integer[] numbers) {
		Integer hashKey = demoCacheManager.getCacheKey(numbers);
		ValueWrapper valueWrapper = demoCacheManager.getDemoCache().get(hashKey);
		if (valueWrapper != null) {
			return new DemoArrayDTO(hashKey);
		}

		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No DemoArray record found.");
	}

	public DemoArrayDTO getArray(Integer id) {
		ValueWrapper valueWrapper = demoCacheManager.getDemoCache().get(id);
		if (valueWrapper != null) {
			return new DemoArrayDTO(id, (Integer[]) valueWrapper.get());
		}

		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No DemoArray record found.");
	}

	public DemoArrayDTO getSuffeled(Integer id) {

		DemoArrayDTO demo = getArray(id);

		if (demo == null || ArrayUtils.isEmpty(demo.getNumbers())) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No DemoArray record found.");
		}

		return new DemoArrayDTO(demo.getId(), shuffeled(demo.getNumbers()));

	}

	public DemoCacheDTO clearCache() {
		demoCacheManager.clearAll();
		return new DemoCacheDTO("Array elements were deleted from memory");
	}

	private Integer[] shuffeled(Integer[] numbers) {
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
