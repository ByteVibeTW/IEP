package com.iep.api.service;

import com.iep.api.dal.dto.*;
import com.iep.api.dal.entity.Course;
import com.iep.api.dal.entity.UserInfo;
import com.iep.api.dal.mapper.CourseMapper;
import com.iep.api.dal.repository.CourseRepository;
import com.iep.api.dal.repository.UserInfoRepository;
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
public class CourseService {
    
    private final CourseRepository courseRepository;
    private final UserInfoRepository userInfoRepository;
    private final CourseMapper courseMapper;
    
    public CourseDto createCourse(CourseDto request) {
        // 從 token 中獲取當前用戶的 sub
        String currentUserSub = SecurityUtils.getCurrentUserSubOrThrow();
        
        UserInfo teacher = userInfoRepository.findById(currentUserSub)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));


        // 驗證用戶是否為老師
        if (teacher.isTeacher()) {
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
        
        // 從 token 中獲取當前用戶的 sub
        String currentUserSub = SecurityUtils.getCurrentUserSubOrThrow();
        
        // 驗證當前用戶是否為該課程的老師
        if (!course.getTeacher().getSub().equals(currentUserSub)) {
            throw new RuntimeException("You are not authorized to update this course");
        }
        
        UserInfo teacher = userInfoRepository.findById(currentUserSub)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        // 驗證用戶是否為老師
        if (teacher.isTeacher()) {
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
    
    @Transactional(readOnly = true)
    public Optional<CourseDetailDto> getCourseDetailById(Long id) {
        return courseRepository.findById(id)
                .map(course -> {
                    CourseDetailDto detailDto = new CourseDetailDto();
                    detailDto.setId(course.getId());
                    detailDto.setTeacherRoleCode(course.getTeacher().getRoleCode());
                    detailDto.setTeacherUsername(course.getTeacher().getUsername());
                    detailDto.setName(course.getName());
                    detailDto.setType(course.getType());
                    detailDto.setIntro(course.getIntro());
                    detailDto.setOutline(course.getOutline());
                    detailDto.setImageUuid(course.getImageUuid());
                    detailDto.setImageName(course.getImageName());
                    
                    // 轉換 sections
                    if (course.getSections() != null) {
                        List<SectionWithChaptersDto> sectionDtos = course.getSections().stream()
                                .sorted((s1, s2) -> {
                                    Integer o1 = s1.getOrderIndex() != null ? s1.getOrderIndex() : 0;
                                    Integer o2 = s2.getOrderIndex() != null ? s2.getOrderIndex() : 0;
                                    return o1.compareTo(o2);
                                })
                                .map(section -> {
                                    SectionWithChaptersDto sectionDto = new SectionWithChaptersDto();
                                    sectionDto.setId(section.getId());
                                    sectionDto.setCourseId(section.getCourse().getId());
                                    sectionDto.setSectionName(section.getSectionName());
                                    sectionDto.setOrderIndex(section.getOrderIndex());
                                    
                                    // 轉換 chapters
                                    if (section.getChapters() != null) {
                                        List<ChapterDto> chapterDtos = section.getChapters().stream()
                                                .sorted((c1, c2) -> {
                                                    Integer o1 = c1.getOrderIndex() != null ? c1.getOrderIndex() : 0;
                                                    Integer o2 = c2.getOrderIndex() != null ? c2.getOrderIndex() : 0;
                                                    return o1.compareTo(o2);
                                                })
                                                .map(chapter -> {
                                                    ChapterDto chapterDto = new ChapterDto();
                                                    chapterDto.setId(chapter.getId());
                                                    chapterDto.setSectionId(chapter.getSection().getId());
                                                    chapterDto.setChapterName(chapter.getChapterName());
                                                    chapterDto.setContent(chapter.getContent());
                                                    chapterDto.setOrderIndex(chapter.getOrderIndex());
                                                    return chapterDto;
                                                })
                                                .collect(Collectors.toList());
                                        sectionDto.setChapters(chapterDtos);
                                    }
                                    
                                    return sectionDto;
                                })
                                .collect(Collectors.toList());
                        detailDto.setSections(sectionDtos);
                    }
                    
                    return detailDto;
                });
    }
}
