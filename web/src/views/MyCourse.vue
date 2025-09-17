<template>
  <DefaultLayout>
    <div class="w-[90%] mx-[5%]">
      <PageTitle title="我的課程" />
      <SelectButton
        v-model="selectValue"
        :options="switchOptions"
        class="mt-2"
      />
      <CourseCardList
        :courses="filteredCourses"
        :selectMode="false"
        :loading="loading"
      />
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useCourseStore } from "../stores/course";
import { useUserStore } from "../stores/user";
import { useAuthStore } from "../stores/auth";
import DefaultLayout from "../Layout/default.vue";
import PageTitle from "../components/common/PageTitle.vue";
import CourseCardList from "../components/course/CourseCardList.vue";
import SelectButton from "primevue/selectbutton";

const courseStore = useCourseStore();
const userStore = useUserStore();
const authStore = useAuthStore();

const selectValue = ref("全部課程");
const switchOptions = ["全部課程", "我開設的課程"];

const loading = ref(true);

const filteredCourses = computed(() => {
  loading.value = true;
  console.log(selectValue.value);
  if (selectValue.value === "我開設的課程") {
    loading.value = false;
    return courseStore.myCourses.filter(
      (course) => course.teacher_id === userStore.currentUserInfo.user_id,
    );
  }
  loading.value = false;
  return courseStore.myCourses;
});

onMounted(async () => {
  loading.value = true;
  authStore.checkAuth();
  await userStore.fetchUser();
  await courseStore.fetchCourses();
  const userId = userStore.currentUserInfo.user_id;
  await courseStore.getMyCourses(userId);
  loading.value = false;
});
</script>
