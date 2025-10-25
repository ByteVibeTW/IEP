<script setup lang="ts">
import { useField } from 'vee-validate';
import AutoComplete from 'primevue/autocomplete';
import { ref } from 'vue';

interface Props {
  name: string;
  label?: string;
  placeholder?: string;
  suggestions?: any[];
  items?: any[];
  field?: string;
  multiple?: boolean;
  dropdown?: boolean;
  forceSelection?: boolean;
  customSearch?: (query: string, items: any[]) => any[];
}

const props = withDefaults(defineProps<Props>(), {
  suggestions: () => [],
  items: () => [],
  field: undefined,
  multiple: false,
  dropdown: false,
  forceSelection: false,
  customSearch: undefined,
});

const emit = defineEmits<{
  complete: [{ query: string; items: any[] }];
}>();

const { value, errorMessage } = useField<string>(() => props.name);

// 內部建議列表
const filteredItems = ref<any[]>([]);

// 搜尋處理
const search = (event: { query: string }) => {
  const query = event.query.toLowerCase();

  // 如果有自定義搜尋函數，使用它
  if (props.customSearch) {
    filteredItems.value = props.customSearch(query, props.items);
    emit('complete', { query, items: filteredItems.value });
    return;
  }

  // 預設搜尋邏輯
  if (!query) {
    filteredItems.value = props.items;
  } else {
    filteredItems.value = props.items.filter((item: any) => {
      if (typeof item === 'string') {
        return item.toLowerCase().includes(query);
      } else if (props.field) {
        return item[props.field]?.toLowerCase().includes(query);
      }
      return false;
    });
  }

  emit('complete', { query, items: filteredItems.value });
};
</script>

<template>
  <div class="mb-6">
    <label v-if="label" :for="name" class="text-[20px] font-bold mb-[10px] block">
      {{ label }}
    </label>
    <AutoComplete :id="name" v-model="value" :suggestions="filteredItems" :field="field" :multiple="multiple"
      :dropdown="dropdown" :force-selection="forceSelection" :placeholder="placeholder" class="w-full"
      :input-class="{ 'p-invalid': errorMessage }" @complete="search" />
    <small v-if="errorMessage" class="text-red-500">{{ errorMessage }}</small>
  </div>
</template>