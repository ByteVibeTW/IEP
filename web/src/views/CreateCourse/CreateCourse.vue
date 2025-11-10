<script setup lang="ts">
import { useCreateCourse } from '@/api/api';
import type { CourseDto } from '@/api/model';
import CustomButton from '@/components/button/CustomButton.vue';
import PageTitle from '@/components/common/PageTitle.vue';
import FormAutoComplete from '@/components/form/FormAutoComplete.vue';
import FormEditor from '@/components/form/FormEditor.vue';
import FormFileUpload from '@/components/form/FormFileUpload.vue';
import FormInputText from '@/components/form/FormInputText.vue';
import FormTextarea from '@/components/form/FormTextarea.vue';
import { courseTypes } from '@/stores/courseType';
import { toTypedSchema } from '@vee-validate/zod';
import swal from 'sweetalert';
import { useForm } from 'vee-validate';
import { ref } from 'vue';
import { z } from 'zod';

const courseImage = ref<string | null>(null);

// 使用生成的 API hook
const { mutate: createCourseMutation, isPending } = useCreateCourse();

// 定義 zod schema
const courseSchema = z.object({
  courseName: z.string().min(3, '課程名稱至少需要 3 個字元'),
  courseType: z.string().min(1, '課程類型為必填項目'),
  courseIntro: z.string().min(10, '課程簡介至少需要 10 個字元'),
  courseOutline: z.string().min(20, '教學大綱至少需要 20 個字元'),
});

// 使用 vee-validate 的表單驗證
const { handleSubmit, resetForm } = useForm({
  validationSchema: toTypedSchema(courseSchema),
  initialValues: {
    courseName: '',
    courseType: '',
    courseIntro: '',
    courseOutline: '',
  },
});

const submitCourse = handleSubmit((values) => {
  // 準備符合 CourseDto 類型的 payload
  const courseData: CourseDto = {
    name: values.courseName,
    type: values.courseType,
    intro: values.courseIntro,
    outline: values.courseOutline,
    imageUuid: courseImage.value || undefined,
  };

  // 使用生成的 API hook
  createCourseMutation(
    { data: courseData },
    {
      onSuccess: () => {
        swal('課程新增成功！', '', 'success');
        resetForm();
        courseImage.value = null;
      },
      onError: (error: unknown) => {
        console.error('課程提交錯誤:', error);
        swal('課程提交失敗！', '請稍後再試。', 'error');
      },
    }
  );
});

// 課程類型搜尋邏輯
const searchCourseTypes = (query: string, items: string[]) => {
  if (!query) return items;
  return items.filter((type) => type.toLowerCase().includes(query.toLowerCase()));
};

// 處理文件上傳
const handleFileUploaded = (fileUrl: string) => {
  courseImage.value = fileUrl;
};

const handleUploadError = (error: any) => {
  console.error('文件上傳錯誤:', error);
  courseImage.value = null;
};
</script>

<template>
  <PageTitle title="建立新課程 📚" />
  <div class="shadow-gray-500 rounded-[8px] w-[100%] self-center py-5">
    <FormInputText name="courseName" label="課程名稱" placeholder="請輸入課程名稱" />
    <FormAutoComplete
      name="courseType"
      label="課程類型"
      placeholder="請選擇或搜尋課程類型"
      :items="courseTypes"
      :dropdown="true"
      :force-selection="true"
      :custom-search="searchCourseTypes"
    />
    <FormTextarea
      name="courseIntro"
      label="課程簡介"
      placeholder="請輸入課程簡介"
      :rows="3"
      :auto-resize="true"
    />
    <FormEditor name="courseOutline" label="教學大綱" editor-style="height: 200px" />
    <FormFileUpload
      label="課程封面圖片(可選)"
      accept="image/*"
      :disabled="isPending"
      @file-uploaded="handleFileUploaded"
      @upload-error="handleUploadError"
    />
    <CustomButton label="提交審核" :disabled="isPending" @click="submitCourse" className="w-full" />
  </div>
</template>
