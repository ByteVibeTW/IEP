<script setup lang="ts">
import Skeleton from 'primevue/skeleton';
import { useGetAllCourses } from '@/api/api';
import CustomButton from '@/components/button/CustomButton.vue';

import CourseCard from './CourseCard.vue';

const { data: courses, isLoading: isLoadingCourses } = useGetAllCourses();

const handleShowDetails = (courseId: number | undefined) => {
  console.log(courseId);
};

const handleSelectCourse = (courseId: number | undefined) => {
  console.log(courseId);
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
      <CourseCard v-for="course in courses" :key="course.id" :course="course" :select-mode="false">
        <template #bottom>
          <div class="flex justify-between items-center gap-2">
            <CustomButton label="查看詳情" class="w-full" @click="handleShowDetails(course.id)" />
            <CustomButton label="選擇此課程" class="w-full" @click="handleSelectCourse(course.id)" />
          </div>
        </template>
      </CourseCard>
    </template>
  </div>
</template>
