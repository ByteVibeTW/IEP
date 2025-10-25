<template>
  <PageTitle title="我的課程" />
  <div class="shadow-gray-500 rounded-[8px] w-[100%] self-center py-5">
    <SelectButton v-model="selectValue" :options="switchOptions" class="mt-2" />
    <CourseCardList :courses="filteredCourses" :select-mode="false" :loading="loading" />
  </div>
</template>

<script setup lang="ts">
import PageTitle from '@/components/common/PageTitle.vue';
import CourseCardList from '@/components/course/CourseCardList.vue';
import { useCourseStore, type Course } from '@/stores/course';
import { useUserStore } from '@/stores/user';
import SelectButton from 'primevue/selectbutton';
import { computed, onMounted, ref } from 'vue';

const courseStore = useCourseStore();
const userStore = useUserStore();

const selectValue = ref('全部課程');
const switchOptions = ['全部課程', '我開設的課程'];

const loading = ref(true);

const filteredCourses = computed(() => {
  if (selectValue.value === '我開設的課程') {
    return courseStore.myCourses.filter(
      (course: Course) => course.teacher_id === userStore.currentUserInfo.user_id
    );
  }
  return courseStore.myCourses;
});

onMounted(async () => {
  loading.value = true;
  await userStore.fetchUser();
  await courseStore.fetchCourses();
  const userId = userStore.currentUserInfo.user_id;
  await courseStore.getMyCourses(userId);
  loading.value = false;
});
</script>
