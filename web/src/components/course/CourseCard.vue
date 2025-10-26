<script setup lang="ts">
import { computed } from 'vue';
import defaultImage from '@/assets/images/default-course.jpg';
import type { CourseDto } from '@/api/model';
import Card from 'primevue/card';

interface Props {
  course: CourseDto;
}

const props = withDefaults(defineProps<Props>(), {
  course: () => ({
    id: 0,
    teacherUsername: '',
    name: '',
    type: '',
    intro: '',
    outline: '',
    imageUuid: '',
    imageName: '',
  }),
});

// 計算課程圖片 URL，如果沒有提供則使用預設圖片
const courseImageUrl = computed(() => {
  return props.course.imageUuid || defaultImage;
});
</script>

<style scoped>
:deep(.p-card-body) {
  display: flex !important;
  flex-direction: column !important;
  height: 100% !important;
  flex: 1 !important;
}

:deep(.p-card-content) {
  flex: 1 !important;
  min-height: 0 !important;
  display: flex !important;
  flex-direction: column !important;
}

:deep(.p-card-footer) {
  margin-top: auto !important;
}
</style>

<template>
  <Card class="overflow-hidden transition-transform hover:translate-y-[-5px] h-[500px] flex flex-col">
    <template #header>
      <div class="h-[200px] bg-slate-100 flex-shrink-0">
        <img :src="courseImageUrl" :alt="course.name" class="w-full h-full object-cover" />
      </div>
    </template>
    <template #title>
      <div class="flex-shrink-0">
        <p>{{ course.name }}</p>
        <p class="text-sm text-gray-500">課程類型: {{ course.type }}</p>
      </div>
    </template>
    <template #subtitle>
      <div class="flex-shrink-0 flex justify-between items-center text-gray-600">
        <p>
          講師:
          {{ course.teacherUsername || 'AI Tutor' }}
        </p>
      </div>
    </template>
    <template #content>
      <div class="flex-1 min-h-0 overflow-y-auto">
        <p class="text-gray-600 whitespace-normal break-words">
          {{ course.intro }}
        </p>
      </div>
    </template>
    <template #footer>
      <div class="flex-shrink-0">
        <slot name="bottom"></slot>
      </div>
    </template>
  </Card>
</template>