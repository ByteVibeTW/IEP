<template>
  <div class="background-image">
    <NavBar />
    <slot />
    <AIFAB v-if="showAIFAB" @click="handleAIClick" />
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import NavBar from '@/components/NavBar/NavBar.vue';
import AIFAB from '@/components/common/AIFAB.vue';

const route = useRoute();

const showAIFAB = computed(() => {
  return route.path !== '/';
});

const handleAIClick = () => {
  console.log('AI FAB 被點擊');
};
</script>

<style scoped>
.background-image {
  background-color: #b1cce7;
  min-height: 100vh;
}
</style>
