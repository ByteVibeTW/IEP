package com.iep.api.service;

import com.iep.api.dto.generate.ChapterContentReqDto;
import com.iep.api.dto.generate.GenerateCourseReqDto;
import com.iep.api.dto.generate.GenerateCourseRespDto;
import com.iep.api.dto.generate.QuestionsResponseDto;
import com.iep.api.dto.generate.UserAnswerDto;
import com.iep.api.exception.CommonException;
import com.iep.api.exception.ErrorCode;
import com.iep.api.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenerateService {

    private final RestTemplate restTemplate;

    @Value("${app.generateUrl}")
    private String apiBaseUrl;

    private QuestionsResponseDto getQuestion(String userId, String userInput) {
        String url = apiBaseUrl + "/ai/generate_questions/{userId}/{userInput}";
        log.info("生成問題: {}", url);
        QuestionsResponseDto response = restTemplate.getForObject(
                url,
                QuestionsResponseDto.class,
                userId,
                userInput
        );
        if (response == null) {
            throw new CommonException(ErrorCode.UNDEFINED);
        }
        return response;
    }

    private GenerateCourseRespDto postCourse(GenerateCourseReqDto requestBody) {
        String url = apiBaseUrl + "/ai/generate_course";
        log.info("生成課程: {}", url);
        GenerateCourseRespDto response = restTemplate.postForObject(
                url,
                requestBody,
                GenerateCourseRespDto.class
        );
        if (response == null) {
            throw new CommonException(ErrorCode.UNDEFINED);
        }
        return response;
    }

    private Map postChapter(ChapterContentReqDto request) {
        String url = apiBaseUrl + "/ai/generate_chapter_content";
        log.info("生成章節內容: {}", url);
        Map response = restTemplate.postForObject(
                url,
                request,
                Map.class
        );
        if (response == null) {
            throw new CommonException(ErrorCode.UNDEFINED);
        }
        return response;
    }

    public QuestionsResponseDto generateQuestion(String userInput) {
        String userId = getCurrentUserIdAsString();
        return getQuestion(userId, userInput);
    }

    public GenerateCourseRespDto generateCourse(List<UserAnswerDto> userAnswers) {
        String userId = getCurrentUserIdAsString();

        GenerateCourseReqDto requestBody = new GenerateCourseReqDto();
        requestBody.setUserId(userId);
        requestBody.setUserAnswer(userAnswers);
        return postCourse(requestBody);
    }

    public Map generateChapter(ChapterContentReqDto request) {
        getCurrentUserIdAsString();
        return postChapter(request);
    }

    private String getCurrentUserIdAsString() {
        Long userId = JwtUtils.getCurrentUserId()
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));
        return userId.toString();
    }
}
