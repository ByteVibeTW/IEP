package com.iep.api.dal.repository;

import com.iep.api.dal.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTeacherID(Long teacherId);
}