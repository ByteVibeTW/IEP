<template>
  <div class="grid gap-4 grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 mt-5">
    <template v-if="loading">
      <div v-for="n in 3" :key="n" class="p-2">
        <Skeleton height="400px" class="mb-2" />
      </div>
    </template>
    <template v-else>
      <CourseCard
        v-for="course in courses"
        :key="course.course_id"
        :course="course"
        :selectMode="selectMode"
        @show-details="showCourseDetails"
        @moved-class="movedClass"
      />
    </template>
    <Dialog
      :visible="showDetails"
      modal
      header="課程大綱"
      :style="{ width: '90vw', maxWidth: '600px' }"
      :closable="false"
    >
      <div
        class="overflow-y-auto max-h-[300px] text-gray-600 border border-gray-200 rounded-lg p-4"
      >
        <p>
          {{ selectedCourseDetails.course_outline }}
        </p>
      </div>
      <div class="flex justify-end gap-2 mt-4">
        <Button
          type="button"
          label="關閉"
          severity="secondary"
          @click="showDetails = false"
        ></Button>
        <Button
          v-if="selectMode"
          type="button"
          label="選擇此課程"
          @click="
            ($emit('select-course', selectedCourseDetails.course_id),
            (showDetails = false))
          "
        ></Button>
      </div>
    </Dialog>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useCourseStore } from "@/stores/course";
import CourseCard from "./CourseCard.vue";
import Button from "primevue/button";
import Skeleton from "primevue/skeleton";

const props = defineProps({
  courses: {
    type: Array,
    default: () => [],
  },
  selectMode: {
    type: Boolean,
    default: false,
  },
  loading: {
    type: Boolean,
    default: false,
  },
});

defineEmits(["select-course"]);

const courseStore = useCourseStore();

const showDetails = ref(false);
const selectedCourseDetails = ref([]);

const showCourseDetails = (courseId) => {
  console.log(courseId);
  selectedCourseDetails.value = props.courses.find(
    (course) => course.course_id === courseId,
  );
  showDetails.value = true;
};

const movedClass = (course_id) => {
  courseStore.saveCurrentClass(course_id);
  console.log(courseStore.currentClass);
};
</script>
