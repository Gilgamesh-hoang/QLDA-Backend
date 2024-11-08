package com.commic.v1.repositories;

import com.commic.v1.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRatingRepository extends JpaRepository<Rating, Integer> {

    List<Rating> findByChapterId(Integer id);
}
