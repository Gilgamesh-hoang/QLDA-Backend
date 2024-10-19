package com.commic.v1.repositories;

import com.commic.v1.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAll();
}
