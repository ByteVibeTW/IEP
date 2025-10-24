<template>
  <DefaultLayout>
    <Container>
      <PageTitle title="申請老師資格 🏫" />
      <div class="shadow-gray-500 rounded-[8px] w-[100%] self-center py-5">
        <div class="mb-6">
          <label for="course-type" class="text-[20px] font-bold mb-[10px] block">姓名</label>
          <InputText id="teacher-name" type="text" placeholder="請輸入真實姓名" class="w-full" />
        </div>

        <div class="mb-6">
          <label for="course-name" class="text-[20px] font-bold mb-[10px] block">身分證號</label>
          <InputText id="teacher-id" v-model="password" type="password" placeholder="輸入身分證號" class="w-full" />
        </div>

        <div class="mb-6">
          <label for="course-name" class="text-[20px] font-bold mb-[10px] block">E-Mail</label>
          <InputText id="teacher-email" type="text" placeholder="請輸入電子信箱" class="w-full" />
        </div>

        <div class="mb-6">
          <label for="course-outline" class="text-[20px] font-bold mb-[10px] block">自我介紹</label>
          <Editor v-model="aboutMe" editor-style="height: 200px" class="mb-4" />
        </div>

        <div class="mb-6">
          <label for="course-outline" class="text-[20px] font-bold mb-[10px]">授課類型</label>
          <!-- 標籤容器 -->
          <div v-if="tags.length > 0" class="flex flex-wrap gap-2 mb-2">
            <Chip v-for="tag in tags" :key="tag.id" :label="tag.text" removable class="cursor-pointer"
              @click="removeTag(tag.id)" />
          </div>

          <!-- 輸入框 -->
          <InputText ref="input" v-model="inputTagValue"
            class="w-full bg-white shadow-2xs shadow-gray-500 text-[16px] border-1 border-solid border-[#ddd] rounded-[8px] p-2 mb-6"
            placeholder="輸入類型標籤後按 Enter 新增" @keydown.enter="addTag" @keydown.delete="handleBackspace"
            @click="focusInput" />
        </div>
        <Button label="提交申請審核" class="w-full" />
      </div>
    </Container>
  </DefaultLayout>
</template>

<script setup lang="ts">
import DefaultLayout from '../../Layout/default.vue';
import PageTitle from '../../components/common/PageTitle.vue';
import Container from '../../components/common/Container.vue';
import { useUserStore } from '../../stores/user';
import Button from 'primevue/button';
import Chip from 'primevue/chip';
import Editor from 'primevue/editor';
import InputText from 'primevue/inputtext';
import { onMounted, ref } from 'vue';

const userStore = useUserStore();

const tags = ref([]);
const inputTagValue = ref('');
const inputTag = ref(null);

const addTag = () => {
  const tag = inputTagValue.value.trim();
  if (tag && !tags.value.some((t) => t.text === tag)) {
    tags.value.push({
      id: Date.now() + Math.random().toString(36).substr(2, 9),
      text: tag,
    });
    inputTagValue.value = '';
  }
};

const removeTag = (tagId) => {
  const index = tags.value.findIndex((tag) => tag.id === tagId);
  if (index !== -1) {
    tags.value = tags.value.filter((tag) => tag.id !== tagId);
  }
};

const handleBackspace = () => {
  if (inputTagValue.value === '' && tags.value.length > 0) {
    const lastTag = tags.value[tags.value.length - 1];
    removeTag(lastTag.id);
  }
};

const focusInput = () => {
  inputTag.value.focus();
};

// 添加自定義樣式

onMounted(() => {
  userStore.fetchUser();
});
</script>

<style scoped>
:deep(.p-chip) {
  background-color: #e8f0fe;
  color: #1a73e8;
  border-radius: 16px;
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
  transition: all 0.2s ease;
  border: 1px solid #d2e3fc;
  cursor: pointer;
  user-select: none;
}

:deep(.p-chip:hover) {
  background-color: #d2e3fc;
  border-color: #1a73e8;
}

:deep(.p-chip .p-chip-remove-icon) {
  display: none;
  color: #1a73e8;
  margin-left: 0.5rem;
  font-size: 0.875rem;
}

:deep(.p-chip:hover .p-chip-remove-icon) {
  display: inline-flex;
}

:deep(.p-chip .p-chip-text) {
  line-height: 1.5;
}

:deep(.p-inputtext) {
  background-color: #f9fafb;
  border: 1px solid #e5e7eb;
  border-radius: 0.5rem;
  padding: 0.5rem 0.75rem;
  transition: all 0.2s ease;
}

:deep(.p-inputtext:hover) {
  border-color: #d1d5db;
  background-color: #f3f4f6;
}

:deep(.p-inputtext:focus) {
  background-color: #ffffff;
  border-color: #93c5fd;
  box-shadow: 0 0 0 2px rgba(147, 197, 253, 0.2);
  outline: none;
}
</style>
