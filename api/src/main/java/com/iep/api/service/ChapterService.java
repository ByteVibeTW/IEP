package com.iep.api.service;

import com.iep.api.dal.entity.Chapter;
import com.iep.api.dal.repository.ChapterRepository;
import com.iep.api.dal.mapper.ChapterMapper;
import com.iep.api.dto.ChapterCreateRequest;
import com.iep.api.dto.ChapterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChapterService {
    
    private final ChapterRepository chapterRepository;
    private final ChapterMapper chapterMapper;
    
    public ChapterResponse createChapter(ChapterCreateRequest request) {
        Chapter chapter = chapterMapper.toEntity(request);
        Chapter savedChapter = chapterRepository.save(chapter);
        return chapterMapper.toResponse(savedChapter);
    }
    
    @Transactional(readOnly = true)
    public Optional<ChapterResponse> getChapterById(Long id) {
        return chapterRepository.findById(id)
                .map(chapterMapper::toResponse);
    }
    
    @Transactional(readOnly = true)
    public List<ChapterResponse> getAllChapters() {
        return chapterRepository.findAll()
                .stream()
                .map(chapterMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ChapterResponse> getChaptersByCourseId(Long courseId) {
        return chapterRepository.findAll()
                .stream()
                .filter(chapter -> chapter.getCourseId().equals(courseId))
                .map(chapterMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    public ChapterResponse updateChapter(Long id, ChapterCreateRequest request) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));
        
        chapter.setCourseId(request.getCourseId());
        chapter.setDate(request.getDate());
        chapter.setName(request.getName());
        chapter.setStFile(request.getStFile());
        chapter.setThFile(request.getThFile());
        
        Chapter updatedChapter = chapterRepository.save(chapter);
        return chapterMapper.toResponse(updatedChapter);
    }
    
    public void deleteChapter(Long id) {
        if (!chapterRepository.existsById(id)) {
            throw new RuntimeException("Chapter not found");
        }
        chapterRepository.deleteById(id);
    }
    
}
