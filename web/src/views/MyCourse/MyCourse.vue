<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import SelectButton from 'primevue/selectbutton';
import PageTitle from '@/components/common/PageTitle.vue';
import CourseCardList from '@/components/course/CourseCardList.vue';
import { useGetCurrentCourses, useGetSelectedCourses } from '@/api/api';
import type { CourseDto } from '@/api/model/courseDto';

const selectValue = ref('已選課程');
const switchOptions = ['已選課程', '開設課程'];

const { data: coursesResponse, isLoading: isLoadingCourses } = useGetCurrentCourses();
const { data: selectedCoursesResponse, isLoading: isLoadingSelectedCourses } = useGetSelectedCourses();
const courses = ref<CourseDto[]>([]);

const sourceCourses = computed<CourseDto[]>(() => {
  if (selectValue.value === '開設課程') {
    return coursesResponse.value ?? [];
  }
  return selectedCoursesResponse.value ?? [];
});

watch(
  sourceCourses,
  (value) => {
    courses.value = value;
  },
  { immediate: true }
);

const filteredCourses = computed<CourseDto[]>(() => courses.value);

const isLoading = computed(() => {
  if (selectValue.value === '開設課程') {
    return isLoadingCourses.value;
  }
  return isLoadingSelectedCourses.value;
});

</script>

<template>
  <PageTitle title="我的課程" />
  <div class="shadow-gray-500 rounded-[8px] w-[100%] self-center py-5">
    <SelectButton v-model="selectValue" :options="switchOptions" class="mt-2" />
    <CourseCardList :courses="filteredCourses" :select-mode="false" :is-loading-courses="isLoading" />
  </div>
</template>
