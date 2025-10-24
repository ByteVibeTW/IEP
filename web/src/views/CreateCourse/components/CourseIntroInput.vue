<template>
  <div class="mb-6">
    <label for="course-intro" class="text-[20px] font-bold mb-[10px] block">課程簡介</label>
    <InputText id="course-intro" :model-value="modelValue" placeholder="請輸入課程簡介" class="w-full"
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
