<script setup lang="ts">
import {
  getGetAllCoursesQueryKey,
  getGetCurrentUserEnrollmentsQueryKey,
  useCreateEnrollment,
  useGetAllCourses,
  useGetSelectedCourses,
} from '@/api/api';
import { baseQueryClient } from '@/api/base/BaseQueryClient';
import type { CourseDto } from '@/api/model/courseDto';
import PageTitle from '@/components/common/PageTitle.vue';
import CourseCardList from '@/components/course/CourseCardList.vue';
import { useAuthStore } from '@/stores/auth';
import { courseTypes } from '@/stores/courseType';
import InputText from 'primevue/inputtext';
import Select from 'primevue/select';
import swal from 'sweetalert';
import { computed, ref } from 'vue';

const searchQuery = ref('');
const selectedType = ref('');
const authStore = useAuthStore();

const { data: coursesResponse, isLoading: isLoadingCourses } = useGetAllCourses();
const { mutate: createEnrollment } = useCreateEnrollment({
  mutation: {
    onSuccess: () => {
      swal('選擇成功！', '已將課程新增至您的課程清單', 'success');
      baseQueryClient.invalidateQueries({ queryKey: getGetAllCoursesQueryKey() });
      baseQueryClient.invalidateQueries({ queryKey: getGetCurrentUserEnrollmentsQueryKey() });
    },
  },
});
const { data: selectedCoursesResponse, isLoading: isLoadingSelectedCourses } =
  useGetSelectedCourses();

const courses = computed<CourseDto[]>(() => coursesResponse.value ?? []);

const selectedCourses = computed<CourseDto[]>(() => selectedCoursesResponse.value ?? []);

const enrolledCourseIds = computed<Set<number>>(() => {
  return new Set(
    selectedCourses.value
      .map((course) => course.id)
      .filter((courseId): courseId is number => courseId != null)
  );
});

const filteredCourses = computed<CourseDto[]>(() => {
  const query = searchQuery.value.trim().toLowerCase();
  const type = selectedType.value;
  const enrolledIds = enrolledCourseIds.value;

  return courses.value.filter((course) => {
    const courseName = (course.name ?? '').toLowerCase();
    const matchesQuery = !query || courseName.includes(query);
    const matchesType = !type || course.type === type;
    const isEnrolled = course.id != null && enrolledIds.has(course.id);

    return matchesQuery && matchesType && !isEnrolled;
  });
});

const isLoading = computed(() => isLoadingCourses.value || isLoadingSelectedCourses.value);

const handleSelectCourse = (courseId?: number) => {
  const userId = authStore.user?.userId;

  if (!authStore.isAuthenticated || !userId) {
    swal('選擇失敗！', '請先登入', 'error');
    return;
  }
  createEnrollment({
    data: {
      courseId: courseId,
      studentId: userId,
    },
  });
};
</script>

<template>
  <PageTitle title="選擇課程" />
  <div class="shadow-gray-500 rounded-[8px] w-[100%] self-center py-5">
    <InputText v-model="searchQuery" placeholder="搜尋課程" class="w-full mb-2" />
    <Select
      id="course-type"
      v-model="selectedType"
      :options="[
        { label: '所有類型', value: '' },
        ...courseTypes.map((type) => ({ label: type, value: type })),
      ]"
      option-label="label"
      option-value="value"
      class="w-full mb-2 showLoader"
      placeholder="所有類型"
    />
  </div>
  <CourseCardList
    :courses="filteredCourses"
    :is-loading-courses="isLoading"
    :select-mode="true"
    @select-course="handleSelectCourse"
  />
</template>
