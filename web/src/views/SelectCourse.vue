<template>
  <DefaultLayout>
    <div class="w-[90%] mx-[5%]">
      <PageTitle title="課程列表" />
      <div class="shadow-gray-500 rounded-[8px] w-[100%] self-center p-5">
        <InputText
          id="search-course-name"
          v-model="searchQuery"
          placeholder="課程名稱"
          class="w-full mb-2"
        />
        <Select
          id="course-type"
          v-model="selectedType"
          :options="[
            { label: '所有類型', value: '' },
            ...courseTypes.map((type) => ({ label: type, value: type })),
          ]"
          optionLabel="label"
          optionValue="value"
          class="w-full mb-2 showLoader"
          placeholder="所有類型"
        />
      </div>
      <CourseCardList
        :courses="filteredCourses"
        :selectMode="true"
        :loading="loading"
        @select-course="chooseCourse"
      />
    </div>
  </DefaultLayout>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { useAuthStore } from "../stores/auth";
import { useCourseStore } from "../stores/course";
import { courseTypes } from "../stores/courseType";
import { useUserStore } from "../stores/user";
import axios from "axios";
import DefaultLayout from "../Layout/default.vue";
import PageTitle from "../components/common/PageTitle.vue";
import CourseCardList from "../components/course/CourseCardList.vue";
import InputText from "primevue/inputtext";
import Select from "primevue/select";
import swal from "sweetalert";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

const courseStore = useCourseStore();
const userStore = useUserStore();
const authStore = useAuthStore();

const searchQuery = ref("");
const selectedType = ref("");
const loading = ref(true);

const filteredCourses = computed(() => {
  loading.value = true;
  return courseStore.courses.filter((course) => {
    const matchesQuery =
      !searchQuery.value ||
      course.course_name
        .toLowerCase()
        .includes(searchQuery.value.toLowerCase());
    const matchesType =
      !selectedType.value || course.course_type === selectedType.value;
    loading.value = false;
    return matchesQuery && matchesType;
  });
});

const chooseCourse = async (courseId) => {
  console.log("選擇的課程ID:", courseId);
  const selectedCourse = courseStore.courses.find(
    (course) => course.course_id === courseId,
  );
  if (selectedCourse.course_price === 0) {
    console.log("免費課程");
    const nowStudents = [
      ...selectedCourse.students.matchAll(/ObjectId\('([a-f\d]{24})'\)/gi),
    ].map((m) => m[1]);
    const newStudent = userStore.currentUserInfo.user_id;
    const payload = {
      students: [...nowStudents, newStudent],
    };
    try {
      const response = await axios.patch(
        `${apiBaseUrl}/api/courses/${selectedCourse.course_id}`,
        payload,
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${authStore.currentUser.access_token}`,
          },
        },
      );
      swal("選擇成功！", "已將課程新增至您的課程清單", "success");
      console.log("選擇成功:", response.data);
    } catch (error) {
      console.error("選擇課程失敗:", error);
      swal("選擇失敗！", "請稍後再試", "error");
    }
  } else {
    console.log("付費課程");
    swal("目前無法使用", "尚未提供付費功能，敬請期待", "info");
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
  background-image: url("../assets/images/email-pattern.png");
  min-height: 100vh;
}
</style>
