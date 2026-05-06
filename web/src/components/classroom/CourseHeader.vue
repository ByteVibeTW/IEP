<script setup lang="ts">
import type { CourseDto } from '@/api/model/courseDto';
import Badge from 'primevue/badge';
import Card from 'primevue/card';

interface Props {
  courseDetail: CourseDto;
  totalChapters: number;
  totalMaterials: number;
}

defineProps<Props>();
</script>

<template>
  <!-- 課程標題區域 -->
  <Card class="mb-8 shadow-lg overflow-hidden">
    <template #header>
      <div class="bg-blue-900 text-white p-6 rounded-lg">
        <div class="flex flex-col sm:flex-row sm:items-start justify-between gap-4">
          <div class="flex-1 min-w-0">
            <h1 class="text-2xl sm:text-3xl font-bold mb-2 course-title leading-tight">
              {{ courseDetail.name }}
            </h1>
            <div
              class="flex flex-col sm:flex-row sm:items-center gap-2 sm:gap-4 text-sm text-blue-100"
            >
              <span class="truncate">
                <i class="pi pi-user mr-2"></i>講師:
                {{ courseDetail.teacherUsername || 'AI Tutor' }}
              </span>
              <span class="truncate">
                <i class="pi pi-tag mr-2"></i>類型: {{ courseDetail.type }}
              </span>
            </div>
          </div>
          <div class="flex sm:flex-col gap-2 sm:text-right">
            <Badge :value="`${totalChapters} 個章節`" severity="info" class="shrink-0" />
            <Badge :value="`${totalMaterials} 個教材`" severity="success" class="shrink-0" />
          </div>
        </div>
      </div>
    </template>
  </Card>
</template>

<style scoped>
/* 課程標題自動換行 */
.course-title {
  word-wrap: break-word;
  word-break: break-all;
  overflow-wrap: break-word;
  white-space: normal;
  hyphens: auto;
}

/* 移除 Card body 的白邊並保持圓角 */
:deep(.p-card-body) {
  padding: 0;
}

:deep(.p-card-content) {
  padding: 0;
}

:deep(.p-card) {
  border-radius: 0.5rem;
  overflow: hidden;
}
</style>
