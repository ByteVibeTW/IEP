import axios from 'axios';
import { defineStore } from 'pinia';

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

export const useUserStore = defineStore('userStore', {
  state: () => ({
    allUsersInfo: [
      {
        user_id: 'teacher_001',
        user_name: '張老師',
        email: 'teacher1@example.com',
        role: 'teacher'
      },
      {
        user_id: 'teacher_002',
        user_name: '李老師',
        email: 'teacher2@example.com',
        role: 'teacher'
      },
      {
        user_id: 'student_001',
        user_name: '王同學',
        email: 'student1@example.com',
        role: 'student'
      },
      {
        user_id: 'student_002',
        user_name: '陳同學',
        email: 'student2@example.com',
        role: 'student'
      },
      {
        user_id: 'student_003',
        user_name: '林同學',
        email: 'student3@example.com',
        role: 'student'
      }
    ], // 所有使用者
    currentUserInfo: {
      user_id: 'student_001',
      user_name: '王同學',
      email: 'student1@example.com',
      role: 'student'
    }, // 當前使用者
    loading: false, // 加載狀態
    error: null, // 錯誤訊息
  }),
  actions: {
    async fetchUser() {
      this.loading = true;
      this.error = null;
      try {
        // 使用假資料，模擬 API 延遲
        await new Promise(resolve => setTimeout(resolve, 300));
        // this.allUsersInfo 和 this.currentUserInfo 已經在 state 中定義了假資料
      } catch (error) {
        this.error = error.response?.data?.detail || '無法獲取使用者資料';
        // console.error('Fetch user error:', error);
      } finally {
        this.loading = false;
      }
    },
  },
});
