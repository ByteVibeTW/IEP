<template>
  <div class="mb-6">
    <label for="course-type" class="text-[20px] font-bold mb-[10px] block">課程類型</label>
    <AutoComplete :model-value="modelValue" :suggestions="filteredTypes" placeholder="請選擇或搜尋課程類型" class="w-full"
      :dropdown="true" force-selection @complete="searchTypes" @update:model-value="updateValue" />
  </div>
</template>

<script setup lang="ts">
import AutoComplete from 'primevue/autocomplete';
import { courseTypes } from '../../../stores/courseType';
import { ref } from 'vue';

defineProps<{
  modelValue: string;
}>();

const emit = defineEmits<{
  'update:modelValue': [value: string];
}>();

const filteredTypes = ref([]);

const searchTypes = (event: { query: string }) => {
  const query = event.query.toLowerCase();
  filteredTypes.value = courseTypes.filter((type) => type.toLowerCase().includes(query));
};

const updateValue = (value: string) => {
  emit('update:modelValue', value);
};
</script>
