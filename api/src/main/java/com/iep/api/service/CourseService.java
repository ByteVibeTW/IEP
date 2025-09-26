package com.iep.api.service;

import com.iep.api.dal.entity.Course;
import com.iep.api.dal.entity.UserInfo;
import com.iep.api.dal.repository.CourseRepository;
import com.iep.api.dal.repository.UserInfoRepository;
import com.iep.api.dal.mapper.CourseMapper;
import com.iep.api.dto.CourseCreateRequest;
import com.iep.api.dto.CourseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {
    
    private final CourseRepository courseRepository;
    private final UserInfoRepository userInfoRepository;
    private final CourseMapper courseMapper;
    
    public CourseResponse createCourse(CourseCreateRequest request) {
        UserInfo teacher = userInfoRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        
        // 驗證用戶是否為老師
        if (!teacher.isTeacher()) {
            throw new RuntimeException("User is not a teacher");
        }
        
        Course course = courseMapper.toEntity(request);
        course.setTeacher(teacher);
        
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toResponse(savedCourse);
    }
    
    @Transactional(readOnly = true)
    public Optional<CourseResponse> getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(courseMapper::toResponse);
    }
    
    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(courseMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<CourseResponse> getCoursesByTeacherId(Long teacherId) {
        return courseRepository.findAll()
                .stream()
                .filter(course -> course.getTeacher().getId().equals(teacherId))
                .map(courseMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    public CourseResponse updateCourse(Long id, CourseCreateRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        UserInfo teacher = userInfoRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        
        // 驗證用戶是否為老師
        if (!teacher.isTeacher()) {
            throw new RuntimeException("User is not a teacher");
        }
        
        course.setTeacher(teacher);
        course.setName(request.getName());
        course.setType(request.getType());
        course.setIntro(request.getIntro());
        course.setOutline(request.getOutline());
        course.setImageUuid(request.getImageUuid());
        
        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toResponse(updatedCourse);
    }
    
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found");
        }
        courseRepository.deleteById(id);
    }
}
