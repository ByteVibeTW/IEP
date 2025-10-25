<script setup lang="ts">
import { useField } from 'vee-validate';
import Editor from 'primevue/editor';

interface Props {
  name: string;
  label?: string;
  placeholder?: string;
  editorStyle?: string;
}

const props = withDefaults(defineProps<Props>(), {
  editorStyle: 'height: 200px',
});

const { value, errorMessage } = useField<string>(() => props.name);
</script>

<template>
  <div class="mb-6">
    <label v-if="label" :for="name" class="text-[20px] font-bold mb-[10px] block">
      {{ label }}
    </label>
    <Editor :id="name" v-model="value" :editor-style="editorStyle" class="w-full"
      :class="{ 'p-invalid': errorMessage }" />
    <small v-if="errorMessage" class="text-red-500">{{ errorMessage }}</small>
  </div>
</template>
