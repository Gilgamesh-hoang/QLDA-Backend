package com.commic.v1.repositories;

import com.commic.v1.entities.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IHistoryRepository extends JpaRepository<History, Integer> {
    List<History> findByChapterId(int chapterId);
}
