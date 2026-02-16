package com.iep.api.dal.repository;

import com.iep.api.dal.entity.Section;
import io.micrometer.common.KeyValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findAllByCourseIdOrderByOrderIndexAsc(Long courseId);
}

