package com.iep.api.controller.v1;

import com.iep.api.dto.generate.ChapterContentReqDto;
import com.iep.api.dto.generate.GenerateCourseRespDto;
import com.iep.api.dto.generate.QuestionsResponseDto;
import com.iep.api.dto.generate.UserAnswerDto;
import com.iep.api.service.GenerateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/generate")
@Tag(name = "生成模組", description = "透過 AI 生成課程 API")
@RequiredArgsConstructor
public class GenerateController {

    private final GenerateService generateService;

    @GetMapping("/question")
    @Operation(summary = "生成問題", description = "生成問題")
    public ResponseEntity<QuestionsResponseDto> generateQuestion(@RequestParam String userInput) {
        QuestionsResponseDto response = generateService.generateQuestion(userInput);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/course")
    @Operation(summary = "生成課程", description = "生成課程")
    public ResponseEntity<GenerateCourseRespDto> generateCourse(@RequestBody List<UserAnswerDto> userAnswers) {
        GenerateCourseRespDto response = generateService.generateCourse(userAnswers);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/chapter")
    @Operation(summary = "生成章節", description = "生成章節")
    public ResponseEntity<Map> generateChapter(@RequestBody ChapterContentReqDto request) {
        Map response = generateService.generateChapter(request);
        return ResponseEntity.ok(response);
    }
}
