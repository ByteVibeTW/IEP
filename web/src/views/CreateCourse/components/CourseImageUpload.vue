<template>
  <div class="mb-6">
    <label for="course-image" class="text-[20px] font-bold mb-[10px] block">課程封面圖片(可選)</label>
    <FileUpload name="file" url="http://localhost:8000/api/upload" :multiple="false" accept="image/*"
      :max-file-size="1000000" :auto="true" :disabled="previewFiles.length > 0" class="w-full" :custom-upload="true"
      @upload="onTemplatedUpload" @select="onSelectedFiles" @uploader="customUploader">
      <template #header="{ chooseCallback }">
        <div class="flex flex-wrap justify-between items-center flex-1 gap-4">
          <div class="flex gap-2">
            <Button icon="pi pi-images" rounded outlined severity="secondary" :disabled="previewFiles.length > 0"
              @click="chooseCallback()"></Button>
          </div>
          <small v-if="previewFiles.length > 0" class="text-gray-500">已上傳一張圖片，請先移除現有圖片才能上傳新圖片</small>
        </div>
      </template>
      <template #content="{ files, slotUploadedFiles, removeFileCallback, messages }">
        <div class="flex flex-col gap-8 pt-4">
          <Message v-for="message of messages" :key="message" :class="{
            'mb-8': !files.length && !slotUploadedFiles.length,
          }" severity="error">
            {{ message }}
          </Message>

          <div v-if="previewFiles.length > 0" class="flex flex-wrap gap-4">
            <div v-for="(file, index) of previewFiles" :key="file.name + file.type + file.size"
              class="p-4 rounded-border flex flex-col border border-surface items-center gap-4">
              <div class="w-[200px] h-[150px] overflow-hidden">
                <img role="presentation" :alt="file.name" :src="file.objectURL" class="w-full h-full object-cover" />
              </div>
              <span class="font-semibold text-ellipsis max-w-60 whitespace-nowrap overflow-hidden">{{ file.name
              }}</span>
              <Badge :value="file.uploaded ? '已上傳' : '待上傳'" :severity="file.uploaded ? 'success' : 'warn'" />
              <div class="flex gap-2">
                <Button icon="pi pi-times" outlined rounded severity="danger"
                  @click="onRemoveTemplatingFile(file, removeFileCallback, index)" />
              </div>
            </div>
          </div>
        </div>
      </template>
      <template #empty>
        <div v-if="previewFiles.length === 0"
          class="flex items-center justify-center flex-col p-8 border-2 border-dashed border-gray-300 rounded-lg">
          <i class="pi pi-cloud-upload !text-4xl !text-gray-400 mb-4" />
          <p class="text-gray-500">拖放圖片到這裡上傳</p>
        </div>
      </template>
    </FileUpload>
  </div>
</template>

<script setup lang="ts">
import Badge from 'primevue/badge';
import Button from 'primevue/button';
import FileUpload from 'primevue/fileupload';
import Message from 'primevue/message';
import swal from 'sweetalert';
import { inject, ref } from 'vue';

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;
const keycloak = inject('keycloak', null);

const uploadedFiles = ref([]);
const previewFiles = ref([]);

const emit = defineEmits<{
  'update:imageUuid': [value: string | null];
}>();

const customUploader = async (event: { files: File[] }) => {
  const file = event.files[0];
  if (!file) return;

  // 檢查是否已登入
  if (!keycloak?.authenticated || !keycloak?.token) {
    swal('請先登入！', '', 'warning');
    return;
  }

  const formData = new FormData();
  formData.append('file', file);

  try {
    // 使用 apiBaseInstance 來上傳檔案，它會自動處理 token
    const { apiBaseInstance } = await import('../../../api/base/BaseApi');
    const response = await apiBaseInstance({
      url: `${apiBaseUrl}/api/upload`,
      method: 'POST',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    if (response.data?.url) {
      emit('update:imageUuid', response.data.url);
      const objectURL = URL.createObjectURL(file);
      uploadedFiles.value = [
        {
          ...file,
          objectURL, // Local preview URL
          uploaded: true,
          minioUrl: response.data.url, // Store MinIO URL separately
        },
      ];
      previewFiles.value = uploadedFiles.value;
      swal('上傳成功！', '圖片已成功上傳。', 'success');
    }
  } catch {
    swal('檔案上傳失敗！', '請稍後再試。', 'error');
    // Clear all image states on failure
    emit('update:imageUuid', null);
    uploadedFiles.value = [];
    previewFiles.value = [];
  }
};

const onTemplatedUpload = (event: { files: File[] }) => {
  const files = event.files;
  if (files?.length > 0) {
    const file = files[0];
    const objectURL = URL.createObjectURL(file);
    previewFiles.value = [
      {
        ...file,
        objectURL,
        uploaded: false,
      },
    ];
  }
};

const onSelectedFiles = (event: { files: File[] }) => {
  const files = event.files;
  if (files?.length > 0) {
    const file = files[0];
    if (file) {
      const objectURL = URL.createObjectURL(file);
      previewFiles.value = [
        {
          ...file,
          objectURL,
          uploaded: false,
        },
      ];
      // 自動觸發上傳
      customUploader({ files: [file] });
    }
  }
};

const onRemoveTemplatingFile = (file: { objectURL?: string }, removeFileCallback: (index: number) => void, index: number) => {
  removeFileCallback(index);
  if (file.objectURL) {
    URL.revokeObjectURL(file.objectURL);
  }
  emit('update:imageUuid', null);
  uploadedFiles.value = [];
  previewFiles.value = [];
};
</script>
