package com.iep.api.service;

import com.iep.api.dto.section.SectionDto;
import com.iep.api.dal.entity.Course;
import com.iep.api.dal.entity.Section;
import com.iep.api.dal.mapper.SectionMapper;
import com.iep.api.dal.repository.SectionRepository;
import com.iep.api.exception.CommonException;
import com.iep.api.exception.ErrorCode;
import com.iep.api.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class SectionService {

    private final SectionRepository sectionRepository;
    private final SectionMapper sectionMapper;
    private final CourseService courseService;

    @Transactional
    public SectionDto createSection(SectionDto request) {
        // 驗證使用者是否為該課程的老師
        Long currentUserId = getCurrentUserId();
        if (!courseService.isCourseTeacher(currentUserId, request.getCourseId())) {
            throw new CommonException(ErrorCode.FORBIDDEN);
        }

        Section section = sectionMapper.toEntity(request);
        Section savedSection = sectionRepository.save(section);
        return sectionMapper.toDto(savedSection);
    }

    @Transactional(readOnly = true)
    public SectionDto getSectionById(Long id) {
        return sectionRepository.findById(id)
                .map(sectionMapper::toDto)
                .orElseThrow(() -> new CommonException(ErrorCode.SECTION_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<SectionDto> getSectionsByCourseId(Long courseId) {
        return sectionRepository.findAllByCourseId(courseId).stream()
                .map(sectionMapper::toDto)
                .toList();
    }

    @Transactional
    public SectionDto updateSection(Long id, SectionDto request) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.SECTION_NOT_FOUND));

        // 驗證使用者是否為該課程的老師
        Long currentUserId = getCurrentUserId();
        if (!courseService.isCourseTeacher(currentUserId, request.getCourseId())) {
            throw new CommonException(ErrorCode.FORBIDDEN);
        }

        sectionMapper.partialUpdate(request, section);

        Section updatedSection = sectionRepository.save(section);
        return sectionMapper.toDto(updatedSection);
    }

    @Transactional
    public void deleteBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        sectionRepository.deleteAllByIdInBatch(ids);
        log.info("批量刪除章節: count={}", ids.size());
    }

    public Section findById(Long id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.SECTION_NOT_FOUND));
    }

    private Long getCurrentUserId() {
        return JwtUtils.getCurrentUserId()
                .orElseThrow(() -> new CommonException(ErrorCode.UNDEFINED));
    }
}

