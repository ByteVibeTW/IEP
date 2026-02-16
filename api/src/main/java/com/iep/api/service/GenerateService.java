package com.iep.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenerateService {

//    private final UserInfoRepository userInfoRepository;
//    private final RestTemplate restTemplate;
//
//    @Value("${app.generateUrl}")
//    private String apiBaseUrl;
//
//    private QuestionsResponseDto getQuestion(String userId, String userInput) {
//        String url = apiBaseUrl + "/ai/generate_questions/{userId}/{userInput}";
//        log.info("生成問題: {}", url);
//        return restTemplate.getForObject(
//                url,
//                QuestionsResponseDto.class,
//                userId,
//                userInput
//        );
//    }
//
//    private void postCourse(String userId, List<UserAnswerDto> userAnswers) {
//        String url = apiBaseUrl + "/ai/generate_course";
//        log.info("生成課程: {}", url);
//        GenerateCourseRequestDto requestBody = new GenerateCourseRequestDto();;
//        requestBody.setUserId(userId);
//        requestBody.setUserAnswer(userAnswers);
//
//        restTemplate.postForObject(
//                url,
//                requestBody,
//                String.class
//        );
//    }
//
//    private void postChapter(UserChapterDto userChapter) {
//        String url = apiBaseUrl + "/ai/generate_chapter_content";
//        log.info("生成章節內容: {}", url);
//        restTemplate.postForObject(
//                url,
//                userChapter,
//                String.class
//        );
//    }
//
//    public QuestionsResponseDto generateQuestion(String question) {
//        String currentUserSub = SecurityUtils.getCurrentUserSubOrThrow();
//        UserInfo student = userInfoRepository.findById(currentUserSub)
//                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));
//        return getQuestion(currentUserSub, question);
//    }
//
//    public void generateCourse(List<UserAnswerDto> userAnswers) {
//        String currentUserSub = SecurityUtils.getCurrentUserSubOrThrow();
//        UserInfo student = userInfoRepository.findById(currentUserSub)
//                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));
//        postCourse(currentUserSub, userAnswers);
//    }
//
//    public void generateChapter(UserChapterDto userChapter) {
//        String currentUserSub = SecurityUtils.getCurrentUserSubOrThrow();
//        UserInfo student = userInfoRepository.findById(currentUserSub)
//                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));
//        postChapter(userChapter);
//    }
}
