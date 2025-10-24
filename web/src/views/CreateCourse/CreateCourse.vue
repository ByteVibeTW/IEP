<template>
  <DefaultLayout>
    <Container>
      <PageTitle title="建立新課程 📚" />
      <div class="shadow-gray-500 rounded-[8px] w-[100%] self-center py-5">
        <CourseNameInput v-model="courseName" />
        <CourseTypeSelector v-model="courseType" />
        <CourseIntroInput v-model="courseIntro" />
        <CourseOutlineEditor v-model="courseOutline" />
        <CourseImageUpload v-model:image-uuid="courseImage" />
        <SubmitButton :is-form-valid="isFormValid" :is-pending="isPending" @submit="onSubmit" />
      </div>
    </Container>
  </DefaultLayout>
</template>

<script setup lang="ts">
import DefaultLayout from '../../Layout/default.vue';
import PageTitle from '../../components/common/PageTitle.vue';
import Container from '../../components/common/Container.vue';
import CourseNameInput from './components/CourseNameInput.vue';
import CourseTypeSelector from './components/CourseTypeSelector.vue';
import CourseIntroInput from './components/CourseIntroInput.vue';
import CourseOutlineEditor from './components/CourseOutlineEditor.vue';
import CourseImageUpload from './components/CourseImageUpload.vue';
import SubmitButton from './components/SubmitButton.vue';
import { useUserStore } from '../../stores/user';
import { useCreateCourse } from '../../api/api';
import type { CourseDto } from '../../api/model';
import { getTokenInfo } from '../../utils/tokenManager';
import swal from 'sweetalert';
import { computed, inject, onMounted, ref } from 'vue';

const userStore = useUserStore();
const keycloak = inject('keycloak', null);

const courseName = ref('');
const courseType = ref('');
const courseIntro = ref('');
const courseOutline = ref('');
const courseImage = ref(null);

// 使用生成的 API hook
const { mutate: createCourseMutation, isPending } = useCreateCourse();

const isFormValid = computed(() => {
  return courseName.value && courseType.value && courseIntro.value && courseOutline.value;
});

const resetForm = () => {
  courseName.value = '';
  courseType.value = '';
  courseIntro.value = '';
  courseOutline.value = '';
  courseImage.value = null;
};

const submitCourse = () => {
  // 檢查是否已登入
  if (!keycloak?.authenticated || !keycloak?.token) {
    swal('請先登入！', '', 'warning');
    return;
  }

  // 從 token 中取得 sub
  const tokenInfo = getTokenInfo();
  const teacherSub = tokenInfo?.sub;

  if (!teacherSub) {
    swal('無法取得教師資訊！', '請重新登入', 'error');
    return;
  }

  // 準備符合 CourseDto 類型的 payload
  const courseData: CourseDto = {
    name: courseName.value,
    type: courseType.value,
    intro: courseIntro.value,
    outline: courseOutline.value,
    imageUuid: courseImage.value || undefined,
    teacherSub: teacherSub,
  };

  // 使用生成的 API hook
  createCourseMutation(
    { data: courseData },
    {
      onSuccess: () => {
        swal('課程新增成功！', '', 'success');
        resetForm();
      },
      onError: (error: unknown) => {
        console.error('課程提交錯誤:', error);
        swal('課程提交失敗！', '請稍後再試。', 'error');
      },
    }
  );
};

const onSubmit = () => {
  if (!isFormValid.value) {
    swal('請填寫所有必要欄位！', '', 'warning');
    return;
  }
  submitCourse();
};

onMounted(() => {
  // 只有已登入用戶才能創建課程
  if (keycloak?.authenticated) {
    userStore.fetchUser();
  }
});
</script>
