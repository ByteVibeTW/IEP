<template>
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
    <p class="text-[#1e2663] text-[32px] text-center mt-[120px] font-bold mb-8">熱門課程</p>
    <div v-if="courses.length > 0" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
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
          @show-details="showCourseDetails"
        />
      </template>
    </div>
    <div
      v-if="courses.length == 0"
      class="bento-item bg-[#eef3ff] flex justify-center h-[300px] rounded-2xl"
    >
      <p class="text-xl font-semibold text-gray-800 flex items-center justify-center w-full h-full">
        尚無熱門課程
      </p>
    </div>

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
        <Button type="button" label="關閉" severity="secondary" @click="showDetails = false" />
      </div>
    </Dialog>
  </div>
</template>

<script setup>
import CourseCard from './HotCourseCard.vue';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import Skeleton from 'primevue/skeleton';
import { ref } from 'vue';

const props = defineProps({
  courses: {
    type: Array,
    default: () => [],
  },
  loading: {
    type: Boolean,
    default: false,
  },
});

const showDetails = ref(false);
const selectedCourseDetails = ref('');

const showCourseDetails = (courseId) => {
  selectedCourseDetails.value = props.courses.find((course) => course.course_id === courseId);
  showDetails.value = true;
};
</script>

<style scoped>
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

.bento-item {
  transition: all 0.3s ease;
}

.bento-item:hover {
  transform: translateY(-5px);
  box-shadow:
    0 20px 25px -5px rgb(0 0 0 / 0.1),
    0 8px 10px -6px rgb(0 0 0 / 0.1);
}
</style>
