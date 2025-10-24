<template>
  <div class="mb-6">
    <label for="course-name" class="text-[20px] font-bold mb-[10px] block">課程名稱</label>
    <InputText id="course-name" :model-value="modelValue" placeholder="請輸入課程名稱" class="w-full"
      @update:model-value="updateValue" />
  </div>
</template>

<script setup lang="ts">
import InputText from 'primevue/inputtext';

defineProps<{
  modelValue: string;
}>();

const emit = defineEmits<{
  'update:modelValue': [value: string];
}>();

const updateValue = (value: string) => {
  emit('update:modelValue', value);
};
</script>
