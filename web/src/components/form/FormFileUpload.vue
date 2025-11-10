<script setup lang="ts">
import { useUploadFile } from '@/api/api';
import FileUpload from 'primevue/fileupload';
import type { FileUploadUploaderEvent } from 'primevue/fileupload';
import swal from 'sweetalert';
import { ref } from 'vue';

interface Props {
  name?: string;
  label?: string;
  accept?: string;
  maxFileSize?: number;
  customUpload?: boolean;
  disabled?: boolean;
}

withDefaults(defineProps<Props>(), {
  accept: 'image/*',
  maxFileSize: 20000000, // 20MB 檔案大小限制
  customUpload: true,
  disabled: false,
});

interface Emits {
  (e: 'file-uploaded', fileUrl: string): void;
  (e: 'upload-error', error: any): void;
}

const emit = defineEmits<Emits>();

const fileUpload = ref();
const { mutateAsync: uploadFileMutation } = useUploadFile();

const fileUploader = async (event: FileUploadUploaderEvent) => {
  try {
    const files = event.files;
    if (!files) {
      throw new Error('沒有選擇檔案');
    }

    const fileArray = Array.isArray(files) ? files : [files];
    if (fileArray.length === 0) {
      throw new Error('沒有選擇檔案');
    }

    const file = fileArray[0];
    if (!file) {
      throw new Error('檔案無效');
    }

    // 直接傳遞 File 物件，讓 API 攔截器處理 FormData 轉換
    const response = await uploadFileMutation({
      data: { file },
    });

    if (response.uuid) {
      emit('file-uploaded', response.uuid);
      swal('上傳成功！', '圖片已成功上傳。', 'success');
      if (fileUpload.value) {
        fileUpload.value.clear();
      }
    } else {
      throw new Error('上傳失敗：未收到檔案 UUID');
    }
  } catch (error) {
    console.error('檔案上傳錯誤:', error);
    emit('upload-error', error);
    swal('檔案上傳失敗！', '請稍後再試。', 'error');
  }
};

const onSelect = (event: any) => {
  // 確保只能選擇一張圖片
  if (event.files && event.files.length > 1) {
    swal('檔案數量限制！', '只能上傳一張圖片。', 'warning');
    // 清除多餘的檔案，只保留第一個
    const firstFile = event.files[0];
    fileUpload.value.files = [firstFile];
  }
};

const onError = () => {
  swal('檔案選擇錯誤！', '請選擇有效的檔案。', 'error');
};
</script>

<template>
  <div class="mb-6">
    <label v-if="label" :for="name" class="text-[20px] font-bold mb-[10px] block">
      {{ label }}
    </label>
    <FileUpload
      ref="fileUpload"
      :name="name"
      :accept="accept"
      :max-file-size="maxFileSize"
      :custom-upload="customUpload"
      :disabled="disabled"
      :multiple="false"
      @uploader="fileUploader"
      @select="onSelect"
      @error="onError"
      choose-label="選擇檔案"
      upload-label="上傳"
      cancel-label="取消"
    >
      <template #empty>
        <div class="flex align-items-center justify-content-center flex-column p-4">
          <i
            class="pi pi-cloud-upload"
            style="font-size: 2em; color: var(--primary-color); padding-right: 10px"
          ></i>
          <p class="mt-1 mb-0">拖放一張圖片到這裡或點擊選擇檔案</p>
        </div>
      </template>
    </FileUpload>
  </div>
</template>
