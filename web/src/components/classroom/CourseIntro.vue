<script setup lang="ts">
import type { CourseDto } from '@/api/model/courseDto';
import Card from 'primevue/card';

interface Props {
  courseDetail: CourseDto;
}

defineProps<Props>();
</script>

<template>
  <!-- 課程介紹 -->
  <Card class="mb-8">
    <template #title>
      <div class="flex items-center">
        <i class="pi pi-list mr-3 text-blue-600"></i>
        課程介紹
      </div>
    </template>
    <template #content>
      <div v-html="courseDetail.intro" class="prose max-w-none text-gray-700"></div>
    </template>
  </Card>
</template>

<style scoped>
/* 確保 prose 樣式不會影響其他元素 */
.prose {
  max-width: none;
}

.prose h1,
.prose h2,
.prose h3 {
  margin-top: 0;
  color: #374151;
}

.prose p {
  color: #6b7280;
  line-height: 1.6;
}
</style>
