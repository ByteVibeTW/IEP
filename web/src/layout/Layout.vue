<script setup lang="ts">
import AIFAB from '@/components/common/AIFAB.vue';
import ChatDialog from '@/components/common/ChatDialog.vue';
import Container from '@/components/common/Container.vue';
import Header from '@/layout/Header.vue';
import { useCourseStore } from '@/stores/course';
import { useUserStore } from '@/stores/user';
import { computed, onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const courseStore = useCourseStore();
const userStore = useUserStore();
const showChatDialog = ref<boolean>(false);

const showAIFAB = computed<boolean>(() => {
  return route.path !== '/';
});

const handleAIClick = (): void => {
  showChatDialog.value = true;
};

const closeChatDialog = (): void => {
  showChatDialog.value = false;
};

const handleCourseGenerated = (): void => {
  console.log('課程生成成功');

  // 設置當前用戶到 course store
  courseStore.setCurrentUser(userStore.currentUserInfo);

  // 更新用戶的課程列表（課程已經由後端生成，這裡只需要刷新列表）
  courseStore.getMyCourses(userStore.currentUserInfo.user_id);

  // 可以選擇刷新課程列表或顯示成功訊息
  // 對話框會自動關閉（由 ChatDialog 處理）
};

onMounted(() => {
  userStore.fetchUser();
});
</script>

<template>
  <div class="background-image layout-shell">
    <Header />
    <Container>
      <slot />
    </Container>
    <AIFAB v-if="showAIFAB" @click="handleAIClick" />
    <ChatDialog
      :isVisible="showChatDialog"
      @close="closeChatDialog"
      @courseGenerated="handleCourseGenerated"
    />
  </div>
</template>

<style scoped>
.background-image {
  background:
    radial-gradient(circle at top left, rgba(224, 242, 254, 0.95), transparent 30%),
    radial-gradient(circle at top right, rgba(191, 219, 254, 0.72), transparent 26%),
    linear-gradient(180deg, #f8fbff 0%, #edf5ff 100%);
  min-height: 100vh;
}

.layout-shell {
  overflow-x: clip;
}
</style>
