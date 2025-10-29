package com.iep.api.service;

import com.iep.api.dal.dto.SectionDto;
import com.iep.api.dal.entity.Course;
import com.iep.api.dal.entity.Section;
import com.iep.api.dal.mapper.SectionMapper;
import com.iep.api.dal.repository.CourseRepository;
import com.iep.api.dal.repository.SectionRepository;
import com.iep.api.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SectionService {
    
    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;
    private final SectionMapper sectionMapper;

    @Transactional
    public SectionDto createSection(SectionDto request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        // 驗證使用者是否為該課程的老師
        String currentUserSub = SecurityUtils.getCurrentUserSubOrThrow();
        if (!course.getTeacher().getSub().equals(currentUserSub)) {
            throw new RuntimeException("You are not authorized to create section for this course");
        }
        
        Section section = sectionMapper.toEntity(request);
        section.setCourse(course);
        
        Section savedSection = sectionRepository.save(section);
        return sectionMapper.toDto(savedSection);
    }
    
    @Transactional(readOnly = true)
    public Optional<SectionDto> getSectionById(Long id) {
        return sectionRepository.findById(id)
                .map(sectionMapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public List<SectionDto> getSectionsByCourseId(Long courseId) {
        return sectionRepository.findAll()
                .stream()
                .filter(section -> section.getCourse().getId().equals(courseId))
                .sorted((s1, s2) -> {
                    Integer o1 = s1.getOrderIndex() != null ? s1.getOrderIndex() : 0;
                    Integer o2 = s2.getOrderIndex() != null ? s2.getOrderIndex() : 0;
                    return o1.compareTo(o2);
                })
                .map(sectionMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public SectionDto updateSection(Long id, SectionDto request) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found"));
        
        // 驗證使用者是否為該課程的老師
        String currentUserSub = SecurityUtils.getCurrentUserSubOrThrow();
        if (!section.getCourse().getTeacher().getSub().equals(currentUserSub)) {
            throw new RuntimeException("You are not authorized to update this section");
        }
        
        sectionMapper.partialUpdate(request, section);
        
        Section updatedSection = sectionRepository.save(section);
        return sectionMapper.toDto(updatedSection);
    }
    
    public void deleteSection(Long id) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found"));
        
        // 驗證使用者是否為該課程的老師
        String currentUserSub = SecurityUtils.getCurrentUserSubOrThrow();
        if (!section.getCourse().getTeacher().getSub().equals(currentUserSub)) {
            throw new RuntimeException("You are not authorized to delete this section");
        }
        
        sectionRepository.deleteById(id);
    }
}

