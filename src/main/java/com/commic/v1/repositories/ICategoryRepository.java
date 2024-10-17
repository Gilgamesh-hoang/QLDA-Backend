package com.commic.v1.repositories;

import com.commic.v1.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAll();

//    List<Category> findCategoriesByName(List<String> categoryNames);

//    List<Category> findAllByNameEqualsIgnoreCase(List<String> categoryNames);

    // find all categories by names , equal ignore case
    Set<Category> findByNameIn(List<String> categoryNames);
}
