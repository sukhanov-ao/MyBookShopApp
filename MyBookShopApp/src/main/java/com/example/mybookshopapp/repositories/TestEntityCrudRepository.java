package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.data.TestEntity;
import org.springframework.data.repository.CrudRepository;

public interface TestEntityCrudRepository extends CrudRepository<TestEntity, Long> {
}
