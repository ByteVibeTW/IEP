package com.iep.api.service;

import com.iep.api.dal.dto.ChapterDto;
import com.iep.api.dal.entity.Chapter;
import com.iep.api.dal.entity.Section;
import com.iep.api.dal.mapper.ChapterMapper;
import com.iep.api.dal.repository.ChapterRepository;
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
public class ChapterService {
    
    private final ChapterRepository chapterRepository;
    private final SectionRepository sectionRepository;
    private final ChapterMapper chapterMapper;
    
    public ChapterDto createChapter(ChapterDto request) {
        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new RuntimeException("Section not found"));
        
        // 驗證使用者是否為該課程的老師
        String currentUserSub = SecurityUtils.getCurrentUserSubOrThrow();
        if (!section.getCourse().getTeacher().getSub().equals(currentUserSub)) {
            throw new RuntimeException("You are not authorized to create chapter for this section");
        }
        
        Chapter chapter = chapterMapper.toEntity(request);
        chapter.setSection(section);
        
        Chapter savedChapter = chapterRepository.save(chapter);
        return chapterMapper.toDto(savedChapter);
    }
    
    @Transactional(readOnly = true)
    public Optional<ChapterDto> getChapterById(Long id) {
        return chapterRepository.findById(id)
                .map(chapterMapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public List<ChapterDto> getChaptersBySectionId(Long sectionId) {
        return chapterRepository.findAll()
                .stream()
                .filter(chapter -> chapter.getSection().getId().equals(sectionId))
                .sorted((c1, c2) -> {
                    Integer o1 = c1.getOrderIndex() != null ? c1.getOrderIndex() : 0;
                    Integer o2 = c2.getOrderIndex() != null ? c2.getOrderIndex() : 0;
                    return o1.compareTo(o2);
                })
                .map(chapterMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public ChapterDto updateChapter(Long id, ChapterDto request) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));
        
        // 驗證使用者是否為該課程的老師
        String currentUserSub = SecurityUtils.getCurrentUserSubOrThrow();
        if (!chapter.getSection().getCourse().getTeacher().getSub().equals(currentUserSub)) {
            throw new RuntimeException("You are not authorized to update this chapter");
        }
        
        chapterMapper.partialUpdate(request, chapter);
        
        Chapter updatedChapter = chapterRepository.save(chapter);
        return chapterMapper.toDto(updatedChapter);
    }
    
    public void deleteChapter(Long id) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));
        
        // 驗證使用者是否為該課程的老師
        String currentUserSub = SecurityUtils.getCurrentUserSubOrThrow();
        if (!chapter.getSection().getCourse().getTeacher().getSub().equals(currentUserSub)) {
            throw new RuntimeException("You are not authorized to delete this chapter");
        }
        
        chapterRepository.deleteById(id);
    }
}

