<template>
  <div class="mb-6">
    <label for="course-outline" class="text-[20px] font-bold mb-[10px] block">教學大綱</label>
    <Editor id="course-outline" :model-value="modelValue" editor-style="height: 200px" class="w-full"
      @update:model-value="updateValue" />
  </div>
</template>

<script setup lang="ts">
import Editor from 'primevue/editor';

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
