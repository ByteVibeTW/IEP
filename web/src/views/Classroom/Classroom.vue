<script setup lang="ts">
import { useCreateCourseDetail, useGetCourseDetailById } from '@/api/api';
import type { CourseDetailDto } from '@/api/model/courseDetailDto';
import CourseHeader from '@/components/classroom/CourseHeader.vue';
import CourseIntro from '@/components/classroom/CourseIntro.vue';
import CourseSections from '@/components/classroom/CourseSections.vue';
import Skeleton from 'primevue/skeleton';
import Toast from 'primevue/toast';
import { useToast } from 'primevue/usetoast';
import { computed, onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const toast = useToast();
const courseId = ref<number>(parseInt(route.params.id as string) || 1);

// 獲取課程詳細資訊
const { data: courseDetail, isLoading, error, refetch } = useGetCourseDetailById(courseId);

// 更新課程詳情的 mutation
const { mutate: updateCourseDetail } = useCreateCourseDetail({
  mutation: {
    onSuccess: () => {
      toast.add({
        severity: 'success',
        summary: '更新成功',
        detail: '課程章節已成功更新',
        life: 3000,
      });
      // 重新獲取課程詳情以同步最新數據
      refetch();
    },
    onError: (error) => {
      console.error('更新課程章節失敗:', error);
      toast.add({
        severity: 'error',
        summary: '更新失敗',
        detail: '更新課程章節時發生錯誤，請稍後再試',
        life: 5000,
      });
    },
  },
});

// 下載檔案處理
const downloadFile = (file: any) => {
  // 這裡應該呼叫實際的檔案下載 API
  console.log(`下載檔案: ${file.name}`);
  // 實際實作中可以使用 useDownloadFile API
};

// 更新課程章節處理
const handleUpdateSections = (sections: any[]) => {
  if (!courseDetail.value) {
    toast.add({
      severity: 'error',
      summary: '更新失敗',
      detail: '課程資料不完整，無法進行更新',
      life: 3000,
    });
    return;
  }

  // 構建更新請求的數據
  const updateData: CourseDetailDto = {
    id: courseDetail.value.id,
    name: courseDetail.value.name,
    type: courseDetail.value.type,
    intro: courseDetail.value.intro,
    outline: courseDetail.value.outline,
    imageUuid: courseDetail.value.imageUuid,
    imageName: courseDetail.value.imageName,
    sections: sections.map((section) => ({
      id: section.id > Date.now() ? undefined : section.id, // 新建的章節不傳 ID
      courseId: courseDetail.value!.id,
      sectionName: section.sectionName,
      description: section.description,
      orderIndex: section.orderIndex,
      chapters:
        section.chapters?.map((chapter: any) => ({
          id: chapter.id > Date.now() ? undefined : chapter.id, // 新建的子章節不傳 ID
          sectionId: section.id > Date.now() ? undefined : section.id,
          chapterName: chapter.chapterName,
          content: chapter.content,
          orderIndex: chapter.orderIndex,
        })) || [],
    })),
  };

  console.log('準備更新課程章節:', updateData);

  // 先清空 sections 再更新
  const emptyData: CourseDetailDto = {
    id: courseDetail.value.id,
    name: courseDetail.value.name,
    type: courseDetail.value.type,
    intro: courseDetail.value.intro,
    outline: courseDetail.value.outline,
    imageUuid: courseDetail.value.imageUuid,
    imageName: courseDetail.value.imageName,
    sections: [],
  };

  updateCourseDetail({ data: emptyData });

  updateCourseDetail({ data: updateData });
};

// 章節計數
const getTotalChapters = computed(() => {
  if (!courseDetail.value?.sections) return 0;
  return courseDetail.value.sections.reduce(
    (total: number, section: any) => total + (section.chapters?.length || 0),
    0
  );
});

// 教材計數
const getTotalMaterials = computed(() => {
  // 每個章節假設有4個教材檔案
  return getTotalChapters.value * 4;
});

onMounted(() => {
  // 可以在這裡加載額外的課程資料
  console.log('載入課程頁面，課程ID:', courseId.value);
});
</script>

<template>
  <!-- Toast 通知組件 -->
  <Toast />

  <div class="min-h-screen bg-gray-50 p-6 mt-20 rounded-[8px]">
    <!-- 載入中狀態 -->
    <div v-if="isLoading" class="text-center py-8">
      <div class="p-6" v-for="n in 3" :key="n">
        <Skeleton width="100%" height="250px"></Skeleton>
      </div>
    </div>

    <!-- 錯誤狀態 -->
    <div v-else-if="error" class="text-center py-8">
      <div class="text-red-600 bg-red-50 border border-red-200 rounded-lg p-4">
        <i class="pi pi-exclamation-triangle text-2xl mb-2"></i>
        <h3 class="text-lg font-semibold">載入課程失敗</h3>
        <p>無法載入課程內容，請稍後再試。</p>
      </div>
    </div>

    <!-- 課程內容 -->
    <div v-else-if="courseDetail" class="max-w-6xl mx-auto">
      <!-- 課程標題區域 -->
      <CourseHeader
        :course-detail="courseDetail"
        :total-chapters="getTotalChapters"
        :total-materials="getTotalMaterials"
      />

      <!-- 課程介紹 -->
      <CourseIntro :course-detail="courseDetail" />

      <!-- 課程章節內容 -->
      <CourseSections
        :sections="courseDetail.sections || []"
        @download-file="downloadFile"
        @update-sections="handleUpdateSections"
      />
    </div>
  </div>
</template>

<style scoped>
/* 文件項目懸停效果 */
.file-item:hover {
  transform: translateY(-1px);
}
</style>
