<script setup lang="ts">
import MaterialFile from './MaterialFile.vue';
import type { ChapterDto } from '@/api/model/chapterDto';

interface MaterialFileData {
  id: number;
  name: string;
  type: string;
  size: string;
  uploadDate: string;
}

interface Props {
  chapter: ChapterDto;
  chapterIndex: number;
}

defineProps<Props>();

const emit = defineEmits<{
  (event: 'download-file', file: MaterialFileData): void;
}>();

// 模擬教材檔案（實際應該從 API 獲取）
const getMaterialFiles = (): MaterialFileData[] => {
  return [
    {
      id: 1,
      name: '課程講義.pptx',
      type: 'presentation',
      size: '2.5 MB',
      uploadDate: '2024-11-10',
    },
    { id: 2, name: '學習指南.docx', type: 'document', size: '1.2 MB', uploadDate: '2024-11-10' },
    { id: 3, name: '練習題目.xlsx', type: 'spreadsheet', size: '856 KB', uploadDate: '2024-11-11' },
    { id: 4, name: '參考資料.txt', type: 'text', size: '45 KB', uploadDate: '2024-11-09' },
  ];
};

const handleDownloadFile = (file: MaterialFileData) => {
  emit('download-file', file);
};
</script>

<template>
  <div class="border border-gray-200 rounded-lg p-4 bg-white hover:shadow-md transition-shadow">
    <!-- 章節標題 -->
    <div class="flex items-center justify-between mb-3 min-w-0">
      <h4 class="text-md font-semibold text-gray-800 flex items-center min-w-0 flex-1">
        <i class="pi pi-file-check mr-2 text-blue-500 flex-shrink-0"></i>
        <span>{{ chapter.chapterName }}</span>
      </h4>
      <span class="text-xs text-gray-500 flex-shrink-0 ml-2">章節 {{ chapterIndex + 1 }}</span>
    </div>

    <!-- 教材檔案列表 -->
    <div class="space-y-2">
      <h5 class="text-sm font-medium text-gray-700 flex items-center">
        <i class="pi mr-2"></i>
        課程講義 (markdown 檔案)
      </h5>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
        <p>123</p>
      </div>
      <h5 class="text-sm font-medium text-gray-700 flex items-center">
        <i class="pi mr-2"></i>
        課程教材
      </h5>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
        <MaterialFile
          v-for="file in getMaterialFiles()"
          :key="file.id"
          :file="file"
          @download="handleDownloadFile"
        />
      </div>
    </div>
  </div>
</template>
