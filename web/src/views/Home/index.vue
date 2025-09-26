<template>
  <DefaultLayout>
    <HeroSection />
    <div class="w-[90%] mx-[5%]">
      <section id="features" class="features-section px-[64px]">
        <FeatureList />
      </section>

      <section id="hotcourse" class="hotcourse-section mt-[120px] px-[64px]">
        <HotCourseList
          v-if="isAuth"
          :courses="filteredCourses"
          :loading="loading"
        />
      </section>
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useAuthStore } from "@/stores/auth";
import { useUserStore } from "@/stores/user";
import { useCourseStore } from "../../stores/course";
import DefaultLayout from "../../Layout/default.vue";
import FeatureList from "./Feature/FeatureList.vue";
import HeroSection from "./HeroSection.vue";
import HotCourseList from "./HotCourse/HotCourseList.vue";

const authStore = useAuthStore();
const userStore = useUserStore();
const courseStore = useCourseStore();

const isAuth = ref(false);
const loading = ref(true);

const filteredCourses = computed(() => {
  if (!isAuth.value) {
    return null;
  }
  const usersCount = userStore.allUsersInfo?.length / 2 || 0;
  if (usersCount > 0) {
    return courseStore.courses.filter((course) => {
      const studentCount =
        [...course.students.matchAll(/ObjectId\('([a-f\d]{24})'\)/gi)].map(
          (m) => m[1],
        )?.length || 0;
      return studentCount >= usersCount;
    });
  } else {
    return null;
  }
});

onMounted(async () => {
  isAuth.value = await authStore.checkAuth();
  if (isAuth.value) {
    userStore.fetchUser();
    courseStore.fetchCourses();
  }
});
</script>

<style scoped>
.features-section,
.hotcourse-section {
  padding: 2rem 0;
}

.feature-card {
  transition: transform 0.2s ease-in-out;
  height: 100%;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.course-card {
  transition: transform 0.2s ease-in-out;
  height: 100%;
}

.course-card:hover {
  transform: translateY(-5px);
}
</style>

:deep(.p-card) { border-radius: 1rem; box-shadow: 0 4px 6px -1px rgb(0 0 0 /
0.1),
<style scoped>
:deep(.p-card) {
  box-shadow: 0 2px 4px -2px rgb(0 0 0 / 0.1);
}

:deep(.p-card-header) {
  background-color: #f8fafc;
  border-top-left-radius: 1rem;
  border-top-right-radius: 1rem;
  padding: 0;
}

:deep(.p-card-title) {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 0.5rem;
}

:deep(.p-card-subtitle) {
  margin-bottom: 1rem;
}

:deep(.p-card-content) {
  padding: 1.5rem;
}

:deep(.p-card-footer) {
  padding: 1.5rem;
  padding-top: 0;
}

:deep(.p-dialog) {
  border-radius: 1rem;
}

:deep(.p-dialog-header) {
  border-top-left-radius: 1rem;
  border-top-right-radius: 1rem;
  background-color: #f8fafc;
}

:deep(.p-dialog-content) {
  padding: 1.5rem;
}

:deep(.p-dialog-footer) {
  border-bottom-left-radius: 1rem;
  border-bottom-right-radius: 1rem;
  background-color: #f8fafc;
  padding: 1rem 1.5rem;
}
</style>
