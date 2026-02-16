package com.iep.api.service;

import com.iep.api.dal.entity.Course;
import com.iep.api.dal.entity.user.UserInfo;
import com.iep.api.dal.entity.user.UserRole;
import com.iep.api.dal.mapper.CourseMapper;
import com.iep.api.dal.repository.CourseRepository;
import com.iep.api.dto.course.CourseReq;
import com.iep.api.dto.course.CourseResp;
import com.iep.api.exception.CommonException;
import com.iep.api.exception.ErrorCode;
import com.iep.api.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final UserInfoService userInfoService;

    @Transactional
    public CourseResp createCourse(CourseReq request) {
        Long currentUserId = getCurrentUserId();
        UserInfo teacher = userInfoService.findById(currentUserId);
        if (teacher.getRole() != UserRole.TEACHER) {
            throw new CommonException(ErrorCode.FORBIDDEN);
        }
        Course course = courseMapper.toEntity(request);
        course.setTeacherID(teacher.getId());

        Course savedCourse = courseRepository.save(course);
        CourseResp resp = courseMapper.toDto(savedCourse);
        resp.setTeacherUsername(teacher.getUsername());
        return resp;
    }

    @Transactional(readOnly = true)
    public CourseResp getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.COURSE_NOT_FOUND));

        UserInfo teacher = userInfoService.findById(course.getTeacherID());
        CourseResp resp = courseMapper.toDto(course);
        resp.setTeacherUsername(teacher.getUsername());
        return resp;
    }

    @Transactional(readOnly = true)
    public List<CourseResp> getAllCourses() {
        List<Course> courses = courseRepository.findAll();

        Set<Long> teacherIds = courses.stream()
                .map(Course::getTeacherID)
                .collect(Collectors.toSet());
        Map<Long, UserInfo> teacherMap = userInfoService.findAllByIds(teacherIds.stream().toList()).stream()
                .collect(Collectors.toMap(UserInfo::getId, Function.identity()));

        return courses.stream()
                .filter(course -> {
                    UserInfo teacher = teacherMap.get(course.getTeacherID());
                    return teacher != null && !"AI Tutor".equals(teacher.getUsername());
                })
                .map(course -> {
                    CourseResp resp = courseMapper.toDto(course);
                    resp.setTeacherUsername(teacherMap.get(course.getTeacherID()).getUsername());
                    return resp;
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CourseResp> getCourseByTeacherId(Long teacherId) {
        UserInfo teacher = userInfoService.findById(teacherId);
        return courseRepository.findByTeacherID(teacherId)
                .stream()
                .map(course -> {
                    CourseResp resp = courseMapper.toDto(course);
                    resp.setTeacherUsername(teacher.getUsername());
                    return resp;
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CourseResp> getCurrentCourses() {
        Long currentUserId = getCurrentUserId();
        UserInfo user = userInfoService.findById(currentUserId);

        return courseRepository.findAll().stream()
                .filter(course -> currentUserId.equals(course.getTeacherID()))
                .map(course -> {
                    CourseResp resp = courseMapper.toDto(course);
                    resp.setTeacherUsername(user.getUsername());
                    return resp;
                })
                .toList();
    }

    public CourseResp updateCourse(Long id, CourseReq request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.COURSE_NOT_FOUND));

        Long currentUserId = getCurrentUserId();
        if (!currentUserId.equals(course.getTeacherID())) {
            throw new CommonException(ErrorCode.FORBIDDEN);
        }

        UserInfo teacher = userInfoService.findById(course.getTeacherID());
        if (teacher.getRole() != UserRole.TEACHER) {
            throw new CommonException(ErrorCode.FORBIDDEN);
        }

        courseMapper.partialUpdate(request, course);

        Course updatedCourse = courseRepository.save(course);
        CourseResp resp = courseMapper.toDto(updatedCourse);
        resp.setTeacherUsername(teacher.getUsername());
        return resp;
    }

    @Transactional
    public void deleteBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        courseRepository.deleteAllByIdInBatch(ids);
        log.info("批量刪除課程: count={}", ids.size());
    }

    public Boolean isCourseTeacher(Long courseId, Long userId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CommonException(ErrorCode.COURSE_NOT_FOUND));
        return course.getTeacherID().equals(userId);
    }

    private Long getCurrentUserId() {
        return JwtUtils.getCurrentUserId()
                .orElseThrow(() -> new CommonException(ErrorCode.UNDEFINED));
    }
}
