import { defineStore } from "pinia";
import axios from "axios";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

export const useCourseStore = defineStore("courseStore", {
  state: () => ({
    courses: [], // 存儲課程列表
    myCourses: [], // 存儲我的課程列表
    currentClass: "", // 該課程的課堂狀態
    loading: false, // 加載狀態
    error: null, // 錯誤訊息
  }),
  actions: {
    async fetchCourses() {
      this.loading = true;
      this.error = null;
      this.authInfo = JSON.parse(
        localStorage.getItem(
          "oidc.user:http://172.16.1.16:8081/realms/coder:vue",
        ),
      );
      try {
        const response = await axios.get(`${apiBaseUrl}/api/courses/`, {
          headers: {
            Authorization: `Bearer ${this.authInfo.access_token}`,
          },
        });
        this.courses = response.data; // 將課程數據存入狀態
      } catch (error) {
        this.error = error.response?.data?.detail || "無法獲取課程資料";
        console.error("Fetch courses error:", error);
      } finally {
        this.loading = false;
      }
    },
    getMyCourses(user_id) {
      this.myCourses = this.courses.filter((course) =>
        course.students.includes(user_id),
      );
      console.log(this.myCourses);
    },
    saveCurrentClass(course_id) {
      this.currentClass = this.courses.find(
        (course) => course.course_id === course_id,
      );
    },
  },
});
