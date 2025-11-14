<script setup lang="ts">
import type { CourseDto } from '@/api/model/courseDto';
import CustomButton from '@/components/button/CustomButton.vue';
import Badge from 'primevue/badge';
import Card from 'primevue/card';
import Dialog from 'primevue/dialog';
import Divider from 'primevue/divider';
import Tooltip from 'primevue/tooltip';

interface Props {
  visible: boolean;
  course: CourseDto | null;
}

withDefaults(defineProps<Props>(), {
  visible: false,
  course: null,
});

defineOptions({
  directives: {
    tooltip: Tooltip,
  },
});

const emit = defineEmits<{
  (event: 'update:visible', value: boolean): void;
}>();

const handleClose = () => {
  emit('update:visible', false);
};
</script>

<template>
  <!-- 課程詳細資訊 Dialog -->
  <Dialog
    :visible="visible"
    @update:visible="handleClose"
    :header="'📚 課程詳細資訊'"
    :style="{ width: '50rem' }"
    :breakpoints="{ '1199px': '75vw', '575px': '90vw' }"
    modal
  >
    <div v-if="course" class="space-y-6">
      <!-- 課程標題 -->
      <div>
        <h2
          class="text-2xl font-bold text-blue-600 mb-2 truncate max-w-full"
          v-tooltip.top="course.name"
        >
          {{ course.name }}
        </h2>
        <div class="flex items-center">
          <i class="pi pi-tag pr-1"></i>
          <Badge :value="`類型: ${course.type || '未分類'}`" class="mr-4" severity="info" />
          <i class="pi pi-user pr-1"></i>
          <Badge :value="`講師: ${course.teacherUsername || 'AI Tutor'}`" severity="success" />
        </div>
      </div>

      <Divider />

      <!-- 課程介紹 -->
      <Card class="shadow-sm">
        <template #title>
          <div class="flex items-center text-green-600">
            <span class="mr-2">📖</span>
            課程介紹
          </div>
        </template>
        <template #content>
          <div class="bg-blue-50 p-4 rounded-lg border-l-4 border-blue-400">
            <p class="text-gray-700 leading-relaxed">
              {{ course.intro || '尚未提供課程介紹' }}
            </p>
          </div>
        </template>
      </Card>

      <!-- 課程大綱 -->
      <Card class="shadow-sm">
        <template #title>
          <div class="flex items-center text-purple-600">
            <span class="mr-2">📋</span>
            課程大綱
          </div>
        </template>
        <template #content>
          <div class="bg-purple-50 p-4 rounded-lg border-l-4 border-purple-400">
            <div
              class="text-gray-700 leading-relaxed"
              v-html="course.outline || '尚未提供課程大綱'"
            ></div>
          </div>
        </template>
      </Card>
    </div>

    <template #footer>
      <div class="flex justify-end">
        <CustomButton
          label="關閉"
          @click="handleClose"
          class="bg-gray-500 hover:bg-gray-600 text-white"
        />
      </div>
    </template>
  </Dialog>
</template>
