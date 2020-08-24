package com.example.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * spring data repository for entity DemoArray 
 */
@Repository
public interface DemoArrayRepository 
        extends JpaRepository<DemoArray, Integer> {
 
}
