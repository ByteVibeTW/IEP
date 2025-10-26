<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { useCourseStore, type GeneratedCourseData } from '@/stores/course'
import { useUserStore } from '@/stores/user'
import Header from '@/layout/Header.vue'
import Container from '@/components/common/Container.vue'
import AIFAB from '@/components/common/AIFAB.vue'
import ChatDialog from '@/components/common/ChatDialog.vue'

const route = useRoute()
const courseStore = useCourseStore()
const userStore = useUserStore()
const showChatDialog = ref<boolean>(false)

const showAIFAB = computed<boolean>(() => {
  return route.path !== '/'
})

const handleAIClick = (): void => {
  console.log('AI FAB 被點擊')
  showChatDialog.value = true
}

const closeChatDialog = (): void => {
  showChatDialog.value = false
}

const handleGenerateCourse = (courseData: GeneratedCourseData): void => {
  console.log('生成課程:', courseData)

  // 設置當前用戶到 course store
  courseStore.setCurrentUser(userStore.currentUserInfo)

  // 添加生成的課程
  courseStore.addGeneratedCourse(courseData)

  // 更新用戶的課程列表
  courseStore.getMyCourses(userStore.currentUserInfo.user_id)

  // 關閉對話框
  closeChatDialog()

  // 顯示成功訊息
  alert('課程生成成功！請到「我的課程」頁面查看。')
}
</script>

<template>
  <div class="background-image">
    <Header />
    <Container>
      <slot />
    </Container>
    <AIFAB v-if="showAIFAB" @click="handleAIClick" />
    <ChatDialog :isVisible="showChatDialog" @close="closeChatDialog" @generateCourse="handleGenerateCourse" />
  </div>
</template>

<style scoped>
.background-image {
  background-color: #b1cce7;
  min-height: 100vh;
}
</style>
