package com.iep.api.dal.repository;

import com.iep.api.dal.entity.Course;
import com.iep.api.dal.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    
    @Query("SELECT c FROM Course c JOIN FETCH c.teacher WHERE c.teacher.id = :teacherId")
    List<Course> findByTeacherId(@Param("teacherId") Long teacherId);
    
    List<Course> findByTeacher(UserInfo teacher);
}