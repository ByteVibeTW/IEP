<script setup lang="ts">
import CourseCard from './CourseCard.vue';
import CourseDialog from './CourseDialog.vue';
import type { CourseDto } from '@/api/model/courseDto';
import CustomButton from '@/components/button/CustomButton.vue';
import Skeleton from 'primevue/skeleton';
import swal from 'sweetalert';
import { ref, toRefs } from 'vue';
import { useRouter } from 'vue-router';

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

const router = useRouter();
const { courses, isLoadingCourses, selectMode } = toRefs(props);

// Dialog 狀態管理
const showCourseDialog = ref(false);
const selectedCourse = ref<CourseDto | null>(null);

const findCourseById = (courseId?: number) => {
  if (courseId == null) {
    return undefined;
  }
  return courses.value.find((course) => course.id === courseId);
};

const handleShowDetails = (courseId?: number) => {
  const course = findCourseById(courseId);

  if (!course) {
    swal('錯誤', '找不到課程資訊', 'error');
    return;
  }

  selectedCourse.value = course;
  showCourseDialog.value = true;
};

const handleSelectCourse = (courseId?: number) => {
  emit('select-course', courseId);
};

const handleEnterCourse = (courseId?: number) => {
  if (courseId) {
    router.push(`/Classroom/${courseId}`);
  } else {
    console.error('課程 ID 無效，無法進入課程');
  }
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
      <CourseCard
        v-for="(course, index) in courses"
        :key="course?.id ?? course?.name ?? index"
        :course="course"
      >
        <template v-if="selectMode" #bottom>
          <div class="flex justify-between items-center gap-2">
            <CustomButton label="查看詳情" class="w-full" @click="handleShowDetails(course.id)" />
            <CustomButton
              label="選擇此課程"
              class="w-full"
              @click="handleSelectCourse(course.id)"
            />
          </div>
        </template>
        <template v-else #bottom>
          <div class="flex justify-between items-center gap-2">
            <CustomButton label="查看詳情" class="w-full" @click="handleShowDetails(course.id)" />
            <CustomButton label="進入課程" class="w-full" @click="handleEnterCourse(course.id)" />
          </div>
        </template>
      </CourseCard>
    </template>
  </div>

  <!-- 課程詳細資訊 Dialog -->
  <CourseDialog v-model:visible="showCourseDialog" :course="selectedCourse" />
</template>
