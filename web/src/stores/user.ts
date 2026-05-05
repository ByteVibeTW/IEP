import { useAuthStore } from '@/stores/auth'
import { defineStore } from 'pinia'

export interface UserInfo {
  user_id: string
  user_name: string
  email: string
  role: 'teacher' | 'student'
}

interface UserStoreState {
  allUsersInfo: UserInfo[]
  currentUserInfo: UserInfo
  loading: boolean
  error: string | null
}

export const useUserStore = defineStore('userStore', {
  state: (): UserStoreState => ({
    allUsersInfo: [
      {
        user_id: 'teacher_001',
        user_name: '張老師',
        email: 'teacher1@example.com',
        role: 'teacher',
      },
      {
        user_id: 'teacher_002',
        user_name: '李老師',
        email: 'teacher2@example.com',
        role: 'teacher',
      },
      {
        user_id: 'student_001',
        user_name: '王同學',
        email: 'student1@example.com',
        role: 'student',
      },
      {
        user_id: 'student_002',
        user_name: '陳同學',
        email: 'student2@example.com',
        role: 'student',
      },
      {
        user_id: 'student_003',
        user_name: '林同學',
        email: 'student3@example.com',
        role: 'student',
      },
      {
        user_id: 'ai_tutor',
        user_name: 'AI Tutor',
        email: 'ai_tutor@example.com',
        role: 'teacher',
      },
    ], // 所有使用者
    currentUserInfo: {
      user_id: 'student_001',
      user_name: '王同學',
      email: 'student1@example.com',
      role: 'student',
    }, // 當前使用者
    loading: false, // 加載狀態
    error: null, // 錯誤訊息
  }),
  actions: {
    async fetchUser() {
      this.loading = true
      this.error = null
      try {
        const authStore = useAuthStore()
        const username = authStore.user?.username?.trim()

        if (!username) {
          return
        }

        if (username === 'Teacher') {
          this.currentUserInfo = {
            user_id: 'teacher_001',
            user_name: 'Teacher',
            email: 'teacher1@example.com',
            role: 'teacher',
          }
          return
        }

        if (username === 'AI Tutor') {
          this.currentUserInfo = {
            user_id: 'ai_tutor',
            user_name: 'AI Tutor',
            email: 'ai_tutor@example.com',
            role: 'teacher',
          }
          return
        }

        if (username === 'bigred') {
          this.currentUserInfo = {
            user_id: 'teacher_002',
            user_name: 'bigred',
            email: 'bigred@yang-lin.dev',
            role: 'teacher',
          }
          return
        }

        const student = this.allUsersInfo.find((user) => user.role === 'student')
        if (student) {
          this.currentUserInfo = student
        }
      } catch (error: any) {
        this.error = error.response?.data?.detail || '無法獲取使用者資料'
        // console.error('Fetch user error:', error);
      } finally {
        this.loading = false
      }
    },
  },
})
