<script setup lang="ts">
import { toRefs } from 'vue';
import Skeleton from 'primevue/skeleton';
import CustomButton from '@/components/button/CustomButton.vue';
import type { CourseDto } from '@/api/model/courseDto';

import CourseCard from './CourseCard.vue';

interface Props {
  courses: CourseDto[];
  isLoadingCourses: boolean;
  selectMode: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  courses: () => [],
  isLoadingCourses: false,
  selectMode: false,
});

const emit = defineEmits<{
  (event: 'show-details', courseId?: number): void;
  (event: 'select-course', courseId?: number): void;
}>();

const { courses, isLoadingCourses, selectMode } = toRefs(props);

const handleShowDetails = (courseId?: number) => {
  emit('show-details', courseId);
};

const handleSelectCourse = (courseId?: number) => {
  emit('select-course', courseId);
};
</script>


<template>
  <div class="grid gap-4 grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 mt-5">
    <template v-if="isLoadingCourses">
      <div v-for="n in 6" :key="n">
        <Skeleton height="500px" class="mb-2" />
      </div>
    </template>
    <template v-else>
      <CourseCard v-for="(course, index) in courses" :key="course?.id ?? course?.name ?? index" :course="course">
        <template v-if="selectMode" #bottom>
          <div class="flex justify-between items-center gap-2">
            <CustomButton label="查看詳情" class="w-full" @click="handleShowDetails(course.id)" />
            <CustomButton label="選擇此課程" class="w-full" @click="handleSelectCourse(course.id)" />
          </div>
        </template>
      </CourseCard>
    </template>
  </div>
</template>
