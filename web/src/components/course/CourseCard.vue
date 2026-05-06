<script setup lang="ts">
import { useDownloadFile } from '@/api/api';
import type { CourseDto } from '@/api/model';
import defaultImage from '@/assets/images/default-course.jpg';
import { useObjectUrl } from '@/composables/useObjectUrl';
import Card from 'primevue/card';
import Tooltip from 'primevue/tooltip';
import { computed } from 'vue';

interface Props {
  course: CourseDto;
}

defineOptions({
  directives: {
    tooltip: Tooltip,
  },
});

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

const { data: imageData } = useDownloadFile(computed(() => props.course.imageUuid ?? ''));
const objectUrl = useObjectUrl(imageData);

// 計算課程圖片 URL，如果沒有提供則使用預設圖片
const courseImageUrl = computed(() => {
  return objectUrl.value || defaultImage;
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

.intro-container {
  max-height: 120px;
  min-height: 120px;
  flex: 0 0 auto;
}
</style>

<template>
  <Card class="overflow-hidden transition-transform hover:-translate-y-1.25 h-125 flex flex-col">
    <template #header>
      <div class="h-50 bg-slate-100 shrink-0">
        <img :src="courseImageUrl" :alt="course.name" class="w-full h-full object-cover" />
      </div>
    </template>
    <template #title>
      <div class="shrink-0">
        <p class="truncate max-w-full block" v-tooltip.top="course.name">{{ course.name }}</p>
        <p class="text-sm text-gray-500">課程類型: {{ course.type }}</p>
      </div>
    </template>
    <template #subtitle>
      <div class="shrink-0 flex justify-between items-center text-gray-600">
        <p>
          講師:
          {{ course.teacherUsername || 'AI Tutor' }}
        </p>
      </div>
    </template>
    <template #content>
      <div class="intro-container overflow-y-auto">
        <p class="text-gray-600 whitespace-normal wrap-break-word">
          {{ course.intro }}
        </p>
      </div>
    </template>
    <template #footer>
      <div class="shrink-0">
        <slot name="bottom"></slot>
      </div>
    </template>
  </Card>
</template>
