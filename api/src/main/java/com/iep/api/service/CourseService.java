package com.iep.api.service;

import com.iep.api.dal.dto.CourseDto;
import com.iep.api.dal.entity.Course;
import com.iep.api.dal.entity.UserInfo;
import com.iep.api.dal.mapper.CourseMapper;
import com.iep.api.dal.repository.CourseRepository;
import com.iep.api.dal.repository.UserInfoRepository;
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
    
    public CourseDto createCourse(CourseDto request) {
        UserInfo teacher = userInfoRepository.findById(request.getTeacherSub())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));


        // 驗證用戶是否為老師
        if (!teacher.isTeacher()) {
            throw new RuntimeException("User is not a teacher");
        }
        request.setTeacherRoleCode( teacher.getRoleCode());
        
        Course course = courseMapper.toEntity(request);
        course.setTeacher(teacher);
        
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toDto(savedCourse);
    }
    
    @Transactional(readOnly = true)
    public Optional<CourseDto> getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(courseMapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<CourseDto> getCoursesByTeacherId(String teacherId) {
        return courseRepository.findAll()
                .stream()
                .filter(course -> course.getTeacher().getSub().equals(teacherId))
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public CourseDto updateCourse(Long id, CourseDto request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        UserInfo teacher = userInfoRepository.findById(request.getTeacherSub())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        // 驗證用戶是否為老師
        if (!teacher.isTeacher()) {
            throw new RuntimeException("User is not a teacher");
        }
        
        courseMapper.partialUpdate(request, course);
        
        course.setTeacher(teacher);

        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toDto(updatedCourse);
    }
    
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found");
        }
        courseRepository.deleteById(id);
    }
}
