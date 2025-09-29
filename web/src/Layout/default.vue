<template>
  <div class="background-image">
    <NavBar />
    <slot />
    <AIFAB v-if="showAIFAB" @click="handleAIClick" />
    <ChatDialog :isVisible="showChatDialog" @close="closeChatDialog" />
  </div>
</template>

<script setup>
import { computed, ref } from 'vue';
import { useRoute } from 'vue-router';
import NavBar from '@/components/NavBar/NavBar.vue';
import AIFAB from '@/components/common/AIFAB.vue';
import ChatDialog from '@/components/common/ChatDialog.vue';

const route = useRoute();
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
</script>

<style scoped>
.background-image {
  background-color: #b1cce7;
  min-height: 100vh;
}
</style>
