import axios from 'axios';
import { defineStore } from 'pinia';

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

export const useCourseStore = defineStore('courseStore', {
  state: () => ({
    currentUser: null,
    courses: [
      {
        course_id: 'course_001',
        course_name: 'Vue.js 前端開發實戰',
        course_type: '程式設計',
        course_intro: '從零開始學習 Vue.js 前端框架，包含組件開發、狀態管理、路由配置等核心概念。適合有基礎 HTML/CSS/JavaScript 知識的學習者。',
        course_outline: '1. Vue.js 基礎概念\n2. 組件開發與通信\n3. Vuex 狀態管理\n4. Vue Router 路由配置\n5. 實戰專案開發\n6. 部署與優化',
        course_price: 2999,
        course_image: '',
        teacher_id: 'teacher_001',
        students: ['student_001', 'student_002'],
        rating: 4.8
      },
      {
        course_id: 'course_002',
        course_name: 'Spring Boot 後端開發',
        course_type: '程式設計',
        course_intro: '學習使用 Spring Boot 框架開發 RESTful API，包含資料庫操作、安全認證、微服務架構等進階主題。',
        course_outline: '1. Spring Boot 基礎配置\n2. RESTful API 設計\n3. 資料庫整合 (JPA/Hibernate)\n4. Spring Security 安全認證\n5. 微服務架構設計\n6. 測試與部署',
        course_price: 3999,
        course_image: '',
        teacher_id: 'teacher_002',
        students: ['student_001', 'student_003'],
        rating: 4.6
      },
      {
        course_id: 'course_003',
        course_name: '資料庫設計與優化',
        course_type: '資料庫',
        course_intro: '深入學習關聯式資料庫設計原理，包含正規化、索引優化、查詢效能調校等實用技巧。',
        course_outline: '1. 資料庫設計基礎\n2. 正規化理論與實務\n3. 索引設計與優化\n4. SQL 查詢效能調校\n5. 資料庫備份與復原\n6. 分散式資料庫架構',
        course_price: 2499,
        course_image: '',
        teacher_id: 'teacher_001',
        students: ['student_002', 'student_003'],
        rating: 4.7
      }
    ],
    myCourses: [],
    currentClass: null,
    loading: false,
    error: null,
  }),
  actions: {
    async fetchCourses() {
      this.loading = true;
      this.error = null;
      try {
        // 使用假資料，模擬 API 延遲
        await new Promise(resolve => setTimeout(resolve, 500));
        // this.courses 已經在 state 中定義了假資料，不需要從 API 獲取
      } catch (error) {
        this.error = error.response?.data?.detail || '無法獲取課程資料';
        // console.error('Fetch courses error:', error);
      } finally {
        this.loading = false;
      }
    },
    getMyCourses(user_id) {
      this.myCourses = this.courses.filter((course) => course.students.includes(user_id));
    },
    saveCurrentClass(course_id) {
      this.currentClass = this.courses.find((course) => course.course_id === course_id);
    },
    addGeneratedCourse(courseData) {
      // 添加 AI 生成的課程到課程列表
      this.courses.push(courseData.course);
      // 如果當前用戶是學生，自動加入課程
      if (this.currentUser && this.currentUser.user_id) {
        courseData.course.students.push(this.currentUser.user_id);
      }
    },
    setCurrentUser(user) {
      this.currentUser = user;
    }
  },
});
