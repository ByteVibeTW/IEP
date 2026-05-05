<script setup lang="ts">
import { customInstant } from '@/api/base/BaseApi';
import CustomButton from '@/components/button/CustomButton.vue';
import PageTitle from '@/components/common/PageTitle.vue';
import FormEditor from '@/components/form/FormEditor.vue';
import FormInputText from '@/components/form/FormInputText.vue';
import { useAuthStore } from '@/stores/auth';
import { useUserStore } from '@/stores/user';
import { toTypedSchema } from '@vee-validate/zod';
import swal from 'sweetalert';
import { useForm } from 'vee-validate';
import { inject, onMounted, ref } from 'vue';
import { z } from 'zod';

const userStore = useUserStore();
const authStore = useAuthStore();

// 定義 zod schema
const teacherSchema = z.object({
  teacherName: z.string().min(2, '姓名至少需要 2 個字元'),
  teacherId: z.string().min(10, '身分證號格式不正確'),
  teacherEmail: z.string().email('請輸入有效的電子信箱'),
  aboutMe: z.string().min(20, '自我介紹至少需要 20 個字元'),
});

// 使用 vee-validate 的表單驗證
const { handleSubmit, resetForm } = useForm({
  validationSchema: toTypedSchema(teacherSchema),
  initialValues: {
    teacherName: '',
    teacherId: '',
    teacherEmail: '',
    aboutMe: '',
  },
});

const isSubmitting = ref(false);

const submitTeacherApplication = handleSubmit(async (values) => {
  // 檢查是否已登入
  const userId = authStore.user?.userId;

  if (!authStore.isAuthenticated || !userId) {
    swal('請先登入！', '', 'warning');
    return;
  }

  isSubmitting.value = true;

  try {
    // 準備更新使用者資訊的 payload
    const updateData = {
      id: userId,
      username: values.teacherName,
      email: values.teacherEmail,
      roleCode: 'TEACHER', // 申請成為老師
    };

    // 使用 customInstant 直接發送請求
    const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;
    await customInstant({
      url: `${apiBaseUrl}/api/v1/users/${userId}`,
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      data: updateData,
    });

    swal('申請提交成功！', '您的申請已送出，請等待審核。', 'success');
    resetForm();
  } catch (error) {
    console.error('申請提交錯誤:', error);
    swal('申請提交失敗！', '請稍後再試。', 'error');
  } finally {
    isSubmitting.value = false;
  }
});

onMounted(() => {
  userStore.fetchUser();
});
</script>

<template>
  <PageTitle title="申請老師資格 🏫" />
  <div class="shadow-gray-500 rounded-[8px] w-[100%] self-center py-5">
    <FormInputText name="teacherName" label="姓名" placeholder="請輸入真實姓名" />

    <FormInputText name="teacherId" label="身分證號" type="password" placeholder="輸入身分證號" />

    <FormInputText name="teacherEmail" label="E-Mail" type="email" placeholder="請輸入電子信箱" />

    <FormEditor name="aboutMe" label="自我介紹" editor-style="height: 200px" />
    <CustomButton
      label="提交申請審核"
      :disabled="isSubmitting"
      @click="submitTeacherApplication"
      className="w-full"
    />
  </div>
</template>
