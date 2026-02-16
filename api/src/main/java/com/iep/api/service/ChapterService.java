package com.iep.api.service;

import com.iep.api.dto.chapter.ChapterDto;
import com.iep.api.dal.entity.Chapter;
import com.iep.api.dal.entity.Section;
import com.iep.api.dal.mapper.ChapterMapper;
import com.iep.api.dal.repository.ChapterRepository;
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
public class ChapterService {

    private final ChapterRepository chapterRepository;
    private final ChapterMapper chapterMapper;
    private final SectionService sectionService;
    private final CourseService courseService;

    public ChapterDto createChapter(ChapterDto request) {
        Section section = sectionService.findById(request.getSectionId());

        // 驗證使用者是否為該課程的老師
        Long currentUserId = getCurrentUserId();
        if (!courseService.isCourseTeacher(section.getCourseId(), currentUserId)) {
            throw new CommonException(ErrorCode.UNAUTHORIZED);
        }

        Chapter chapter = chapterMapper.toEntity(request);
        Chapter savedChapter = chapterRepository.save(chapter);
        return chapterMapper.toDto(savedChapter);
    }

    @Transactional(readOnly = true)
    public ChapterDto getChapterById(Long id) {
        return chapterRepository.findById(id)
                .map(chapterMapper::toDto)
                .orElseThrow(() -> new CommonException(ErrorCode.CHAPTER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<ChapterDto> getChaptersBySectionId(Long sectionId) {
        return chapterRepository.findBySectionIdOrderByOrderIndexAsc(sectionId)
                .stream()
                .map(chapterMapper::toDto)
                .toList();
    }

    @Transactional
    public ChapterDto updateChapter(Long id, ChapterDto request) {
        Section section = sectionService.findById(request.getSectionId());
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.CHAPTER_NOT_FOUND));

        // 驗證使用者是否為該課程的老師
        Long currentUserId = getCurrentUserId();
        if (!courseService.isCourseTeacher(section.getCourseId(), currentUserId)) {
            throw new CommonException(ErrorCode.UNAUTHORIZED);
        }

        chapterMapper.partialUpdate(request, chapter);

        Chapter updatedChapter = chapterRepository.save(chapter);
        return chapterMapper.toDto(updatedChapter);
    }

    @Transactional
    public void deleteBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        chapterRepository.deleteAllByIdInBatch(ids);
        log.info("批量刪除章節: count={}", ids.size());
    }

    private Long getCurrentUserId() {
        return JwtUtils.getCurrentUserId()
                .orElseThrow(() -> new CommonException(ErrorCode.UNDEFINED));
    }
}

