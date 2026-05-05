<script setup lang="ts">
import CustomButton from '@/components/button/CustomButton.vue';
import PageTitle from '@/components/common/PageTitle.vue';
import FormInputText from '@/components/form/FormInputText.vue';
import { useAuthStore } from '@/stores/auth';
import { toTypedSchema } from '@vee-validate/zod';
import swal from 'sweetalert';
import { useForm } from 'vee-validate';
import { computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { z } from 'zod';

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();

const loginSchema = z.object({
  email: z.string().email('請輸入有效的電子信箱'),
  password: z.string().min(1, '密碼不能為空'),
});

const { handleSubmit } = useForm({
  validationSchema: toTypedSchema(loginSchema),
  initialValues: {
    email: '',
    password: '',
  },
});

const redirectTarget = computed(() => {
  return typeof route.query.redirect === 'string' && route.query.redirect.trim()
    ? route.query.redirect
    : '/';
});

const submitLogin = handleSubmit(async (values) => {
  try {
    await authStore.login(values);
    await router.replace(redirectTarget.value);
  } catch (error) {
    console.error('登入失敗:', error);
    swal('登入失敗', authStore.error || '請確認帳號密碼後再試一次。', 'error');
  }
});

onMounted(() => {
  if (authStore.isAuthenticated) {
    router.replace(redirectTarget.value);
  }
});
</script>

<template>
  <section class="login-shell">
    <div class="login-card">
      <PageTitle title="登入系統" />
      <p class="login-description">使用後端 JWT 帳號登入，完成後即可存取受保護頁面。</p>

      <form class="login-form" @submit.prevent="submitLogin">
        <FormInputText name="email" label="Email" type="email" placeholder="請輸入電子信箱" />
        <FormInputText name="password" label="密碼" type="password" placeholder="請輸入密碼" />

        <CustomButton
          label="登入"
          :disabled="authStore.loading"
          :fullWidth="true"
          className="w-full mt-2"
          @click="submitLogin"
        />
      </form>
    </div>
  </section>
</template>

<style scoped>
.login-shell {
  min-height: calc(100vh - 6rem);
  display: grid;
  place-items: center;
  padding: 2rem 1rem;
}

.login-card {
  width: min(100%, 32rem);
  padding: 2rem;
  border-radius: 1.5rem;
  background: rgba(255, 255, 255, 0.9);
  box-shadow: 0 24px 80px rgba(15, 23, 42, 0.12);
  backdrop-filter: blur(18px);
}

.login-description {
  margin-top: 0.5rem;
  margin-bottom: 1.5rem;
  color: #475569;
  line-height: 1.6;
}

.login-form {
  display: grid;
  gap: 0.25rem;
}
</style>
