package com.example.demo.manager;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import com.example.demo.dto.DemoArrayDTO;
import com.example.demo.dto.DemoCacheDTO;

/**
 * Spring boot in memory cache implementation
 */
@Component
public class DemoCacheManager {

	private final CacheManager cacheManager;

	public DemoCacheManager(CacheManager cacheManager) {
		super();
		this.cacheManager = cacheManager;
	}

	/**
	 * @return Cache object with the name of "numberMap"
	 */
	public Cache getDemoCache() {
		return cacheManager.getCache("numberMap");
	}

	/**
	 * @void service to remove all elements of "numberMap" Cache
	 */
	public DemoCacheDTO clearAll() {
		cacheManager.getCacheNames().parallelStream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
		return new DemoCacheDTO("Array elements were deleted from memory");
	}

	/**
	 * 
	 * @param key
	 * @return valueWrapper
	 */
	public ValueWrapper getByKey(Integer key) {
		return cacheManager.getCache("numberMap").get(key);
	}

	/**
	 * Add array numbers to cache
	 * @param numbers
	 * @return
	 */
	public DemoArrayDTO add(Integer[] numbers) {
		Integer key = getCacheKey(numbers);
		getDemoCache().put(getCacheKey(numbers), numbers);
		return new DemoArrayDTO(key, numbers);

	}

	/**
	 * 
	 * @param numbers , an array of integer
	 * @return a unique hash code key associated to the array.
	 */
	public Integer getCacheKey(Integer[] numbers) {
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
