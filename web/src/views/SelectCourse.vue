<template>
  <DefaultLayout>
    <div>
      <PageTitle title="選擇課程" />
      <div class="flex mb-4">
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
        :select-mode="true"
        :loading="loading"
        @select-course="chooseCourse"
      />
    </div>
  </DefaultLayout>
</template>

<script setup>
import DefaultLayout from '../Layout/default.vue';
import PageTitle from '../components/common/PageTitle.vue';
import CourseCardList from '../components/course/CourseCardList.vue';
import { useAuthStore } from '../stores/auth';
import { useCourseStore } from '../stores/course';
import { courseTypes } from '../stores/courseType';
import { useUserStore } from '../stores/user';
import axios from 'axios';
import InputText from 'primevue/inputtext';
import Select from 'primevue/select';
import swal from 'sweetalert';
import { computed, onMounted, ref } from 'vue';

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

const courseStore = useCourseStore();
const userStore = useUserStore();
const authStore = useAuthStore();

const searchQuery = ref('');
const selectedType = ref('');
const loading = ref(true);

const filteredCourses = computed(() => {
  return courseStore.courses.filter((course) => {
    const matchesQuery =
      !searchQuery.value ||
      course.course_name.toLowerCase().includes(searchQuery.value.toLowerCase());
    const matchesType = !selectedType.value || course.course_type === selectedType.value;
    return matchesQuery && matchesType;
  });
});

const chooseCourse = async (courseId) => {
  const selectedCourse = courseStore.courses.find((course) => course.course_id === courseId);
  if (selectedCourse.course_price === 0) {
    const nowStudents = [...selectedCourse.students.matchAll(/ObjectId\('([a-f\d]{24})'\)/gi)].map(
      (m) => m[1]
    );
    const newStudent = userStore.currentUserInfo.user_id;
    const payload = {
      students: [...nowStudents, newStudent],
    };
    try {
      await axios.patch(`${apiBaseUrl}/api/courses/${selectedCourse.course_id}`, payload, {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${authStore.currentUser.access_token}`,
        },
      });
      swal('選擇成功！', '已將課程新增至您的課程清單', 'success');
    } catch {
      swal('選擇失敗！', '請稍後再試', 'error');
    }
  } else {
    swal('目前無法使用', '尚未提供付費功能，敬請期待', 'info');
  }
  courseStore.fetchCourses();
};

onMounted(async () => {
  authStore.checkAuth();
  await courseStore.fetchCourses();
  await userStore.fetchUser();
  loading.value = false;
});
</script>

<style scoped>
.SelectCourse {
  background-image: url('../assets/images/email-pattern.png');
  min-height: 100vh;
}
</style>
