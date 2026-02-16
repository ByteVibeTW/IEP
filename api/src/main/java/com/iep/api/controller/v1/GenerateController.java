package com.iep.api.controller.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/generate")
@Tag(name = "生成模組", description = "透過 AI 生成課程 API")
@RequiredArgsConstructor
public class GenerateController {

//    private final GenerateService generateService;
//
//    @PostMapping("/question")
//    @Operation(summary = "生成問題", description = "生成問題")
//    public ResponseEntity<QuestionsResponseDto> generateQuestion(@RequestBody String question) {
//        QuestionsResponseDto response = generateService.generateQuestion(question);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//
//    @PostMapping("/course")
//    @Operation(summary = "生成課程", description = "生成課程")
//    public ResponseEntity<Void> generateCourse(@RequestBody List<UserAnswerDto> userAnswers) {
//        generateService.generateCourse(userAnswers);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }
//
//    @PostMapping("/chapter")
//    @Operation(summary = "生成章節", description = "生成章節")
//    public ResponseEntity<Void> generateChapter(@RequestBody UserChapterDto userChapter) {
//        generateService.generateChapter(userChapter);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }
}
