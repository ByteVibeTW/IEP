<template>
  <div class="background-image">
    <NavBar />
    <slot />
    <AIFAB v-if="showAIFAB" @click="handleAIClick" />
    <ChatDialog :isVisible="showChatDialog" @close="closeChatDialog" @generateCourse="handleGenerateCourse" />
  </div>
</template>

<script setup>
import { computed, ref } from 'vue';
import { useRoute } from 'vue-router';
import { useCourseStore } from '@/stores/course';
import { useUserStore } from '@/stores/user';
import NavBar from '@/components/NavBar/NavBar.vue';
import AIFAB from '@/components/common/AIFAB.vue';
import ChatDialog from '@/components/common/ChatDialog.vue';

const route = useRoute();
const courseStore = useCourseStore();
const userStore = useUserStore();
const showChatDialog = ref(false);

const showAIFAB = computed(() => {
  return route.path !== '/';
});

const handleAIClick = () => {
  console.log('AI FAB 被點擊');
  showChatDialog.value = true;
};

const closeChatDialog = () => {
  showChatDialog.value = false;
};

const handleGenerateCourse = (courseData) => {
  console.log('生成課程:', courseData);

  // 設置當前用戶到 course store
  courseStore.setCurrentUser(userStore.currentUserInfo);

  // 添加生成的課程
  courseStore.addGeneratedCourse(courseData);

  // 更新用戶的課程列表
  courseStore.getMyCourses(userStore.currentUserInfo.user_id);

  // 關閉對話框
  closeChatDialog();

  // 顯示成功訊息
  alert('課程生成成功！請到「我的課程」頁面查看。');
};
</script>

<style scoped>
.background-image {
  background-color: #b1cce7;
  min-height: 100vh;
}
</style>
