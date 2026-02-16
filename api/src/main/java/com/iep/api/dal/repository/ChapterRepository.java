package com.iep.api.dal.repository;

import com.iep.api.dal.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findBySectionIdOrderByOrderIndexAsc(Long sectionId);
}

