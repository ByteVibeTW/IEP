<script setup>
import { useAuthStore } from '../stores/auth';
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const authStore = useAuthStore();
onMounted(async () => {
  try {
    await authStore.handleRedirect();
    await authStore.checkAuth();
    router.push('/MyCourse');
  } catch {
    // console.error('處理回調時發生錯誤:', error);
    router.push('/');
  }
});
</script>

<template>
  <div class="callback">
    <h2>正在處理登入...</h2>
  </div>
</template>

<style scoped>
.callback {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
  text-align: center;
}
</style>
