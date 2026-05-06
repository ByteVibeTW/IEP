<script setup lang="ts">
import CustomButton from '../button/CustomButton.vue';
import ChapterCard from './ChapterCard.vue';
import CourseContentDialog from './CourseContentDialog.vue';
import type { SectionWithChaptersDto } from '@/api/model/sectionWithChaptersDto';
import Accordion from 'primevue/accordion';
import AccordionContent from 'primevue/accordioncontent';
import AccordionHeader from 'primevue/accordionheader';
import AccordionPanel from 'primevue/accordionpanel';
import Badge from 'primevue/badge';
import Card from 'primevue/card';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

interface MaterialFileData {
  id: number;
  name: string;
  type: string;
  size: string;
  uploadDate: string;
}

interface Props {
  sections: SectionWithChaptersDto[];
  courseId?: number;
  courseName?: string;
  courseType?: string;
  courseIntro?: string;
}

const props = defineProps<Props>();

const emit = defineEmits<{
  (event: 'download-file', file: MaterialFileData): void;
  (event: 'update-sections', sections: SectionWithChaptersDto[]): void;
}>();

const router = useRouter();

// Dialog 狀態管理
const showContentDialog = ref(false);

const handleDownloadFile = (file: MaterialFileData) => {
  emit('download-file', file);
};

const handleOpenContentDialog = () => {
  showContentDialog.value = true;
};

const handleSaveSections = (sections: SectionWithChaptersDto[]) => {
  emit('update-sections', sections);
};

const handleOpenContent = (chapterId: number, sectionName: string = '') => {
  // 保存課程資訊到 sessionStorage，以便 Content 頁面可以正確返回
  //TODO: 改成使用 store 來處理
  if (props.courseId) {
    const courseInfo = {
      courseId: props.courseId,
      courseName: props.courseName || '',
      type: props.courseType || '',
      intro: props.courseIntro || '',
      sectionName: sectionName,
    };
    sessionStorage.setItem('currentCourseInfo', JSON.stringify(courseInfo));
  }
  router.push(`/Content/${chapterId}`);
};
</script>

<template>
  <!-- 課程章節內容 -->
  <div class="flex justify-end mb-2">
    <CustomButton label="修改課程內容" @click="handleOpenContentDialog" />
  </div>
  <Card>
    <template #title>
      <div class="flex items-center justify-between flex-wrap gap-2">
        <div class="flex items-center">
          <i class="pi pi-book mr-3 text-green-600"></i>
          課程內容
        </div>
        <span class="text-sm text-gray-500">點擊章節查看教材</span>
      </div>
    </template>
    <template #content>
      <!-- 單元列表 -->
      <div v-if="sections && sections.length > 0">
        <Accordion :multiple="true" class="w-full">
          <!-- 遍歷單元 -->
          <AccordionPanel
            v-for="section in sections"
            :key="section.id"
            :value="String(section.id)"
            class="mb-4"
          >
            <AccordionHeader>
              <div class="flex items-center justify-between w-full min-w-0">
                <div class="flex items-center min-w-0 flex-1">
                  <i class="pi pi-folder-open mr-3 text-orange-500 shrink-0"></i>
                  <div class="min-w-0 flex-1">
                    <h3 class="text-lg font-semibold text-gray-800 hover:underline">
                      {{ section.sectionName }}
                    </h3>
                    <p class="text-sm text-gray-500">{{ section.description }}</p>
                  </div>
                </div>
                <Badge
                  :value="`${section.chapters?.length || 0} 個章節`"
                  class="mx-2 shrink-0"
                  severity="info"
                />
              </div>
            </AccordionHeader>

            <AccordionContent>
              <!-- 章節列表 -->
              <div class="space-y-4 pl-4">
                <ChapterCard
                  v-for="(chapter, chapterIndex) in section.chapters"
                  :key="chapter.id"
                  :chapter="chapter"
                  :chapter-index="chapterIndex"
                  @download-file="handleDownloadFile"
                  @click="
                    chapter.id ? handleOpenContent(chapter.id, section.sectionName) : undefined
                  "
                />
              </div>
            </AccordionContent>
          </AccordionPanel>
        </Accordion>
      </div>

      <!-- 無內容狀態 -->
      <div v-else class="text-center py-12 text-gray-500">
        <i class="pi pi-inbox text-4xl mb-4 text-gray-400"></i>
        <h3 class="text-lg font-medium mb-2">尚無課程內容</h3>
        <p>課程內容正在準備中，請稍後再查看。</p>
      </div>
    </template>
  </Card>

  <!-- 課程內容編輯 Dialog -->
  <CourseContentDialog
    v-model:visible="showContentDialog"
    :sections="sections"
    @save="handleSaveSections"
  />
</template>

<style scoped>
/* 自定義 Accordion 樣式 */
:deep(.p-accordion-header-link) {
  padding: 1.5rem;
  border-radius: 0.75rem;
  transition: all 0.2s ease;
}

:deep(.p-accordion-header-link:hover) {
  background-color: rgb(249 250 251);
}

:deep(.p-accordion-content) {
  padding: 1rem 1.5rem;
}

:deep(.p-accordionpanel) {
  margin-bottom: 1rem;
  border-radius: 0.75rem;
  overflow: hidden;
}
</style>
