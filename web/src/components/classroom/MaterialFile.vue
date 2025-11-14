<script setup lang="ts">
import Button from 'primevue/button';
import Tooltip from 'primevue/tooltip';

// 註冊 Tooltip 指令
defineOptions({
  directives: {
    tooltip: Tooltip,
  },
});

interface MaterialFile {
  id: number;
  name: string;
  type: string;
  size: string;
  uploadDate: string;
}

interface Props {
  file: MaterialFile;
}

const props = defineProps<Props>();

const emit = defineEmits<{
  (event: 'download', file: MaterialFile): void;
}>();

// 檔案類型圖示映射
const getFileIcon = (fileName: string): string => {
  const extension = fileName.split('.').pop()?.toLowerCase();
  switch (extension) {
    case 'doc':
    case 'docx':
      return 'pi-file-word';
    case 'ppt':
    case 'pptx':
      return 'pi-file-powerpoint';
    case 'xls':
    case 'xlsx':
      return 'pi-file-excel';
    case 'txt':
      return 'pi-file';
    case 'pdf':
      return 'pi-file-pdf';
    default:
      return 'pi-file';
  }
};

// 檔案類型顏色
const getFileTypeClass = (fileName: string): string => {
  const extension = fileName.split('.').pop()?.toLowerCase();
  switch (extension) {
    case 'doc':
    case 'docx':
      return 'text-blue-600 bg-blue-50 border-blue-200';
    case 'ppt':
    case 'pptx':
      return 'text-orange-600 bg-orange-50 border-orange-200';
    case 'xls':
    case 'xlsx':
      return 'text-green-600 bg-green-50 border-green-200';
    case 'txt':
      return 'text-gray-600 bg-gray-50 border-gray-200';
    case 'pdf':
      return 'text-red-600 bg-red-50 border-red-200';
    default:
      return 'text-purple-600 bg-purple-50 border-purple-200';
  }
};

const handleDownload = () => {
  emit('download', props.file);
};
</script>

<template>
  <div
    :class="[
      'border rounded-lg p-3 cursor-pointer hover:shadow-md transition-all duration-200',
      getFileTypeClass(file.name),
    ]"
    v-tooltip.bottom="'點擊下載 ' + file.name"
    @click="handleDownload"
  >
    <div class="flex items-center justify-between">
      <div class="flex items-center flex-1 min-w-0">
        <i :class="['text-lg mr-3', 'pi', getFileIcon(file.name)]"></i>
        <div class="min-w-0 flex-1">
          <p class="text-sm font-medium truncate">{{ file.name }}</p>
          <div class="flex items-center text-xs text-gray-500 mt-1">
            <span>{{ file.size }}</span>
            <span class="mx-2">•</span>
            <span>{{ file.uploadDate }}</span>
          </div>
        </div>
      </div>
      <Button
        icon="pi pi-download"
        severity="secondary"
        text
        size="small"
        @click.stop="handleDownload"
      />
    </div>
  </div>
</template>
