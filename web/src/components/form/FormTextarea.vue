<script setup lang="ts">
import { useField } from 'vee-validate';
import Textarea from 'primevue/textarea';

interface Props {
  name: string;
  label?: string;
  placeholder?: string;
  rows?: number;
  autoResize?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  rows: 3,
  autoResize: false,
});

const { value, errorMessage } = useField<string>(() => props.name);
</script>

<template>
  <div class="mb-6">
    <label v-if="label" :for="name" class="text-[20px] font-bold mb-[10px] block">
      {{ label }}
    </label>
    <Textarea :id="name" v-model="value" :rows="rows" :placeholder="placeholder" :auto-resize="autoResize"
      class="w-full" :class="{ 'p-invalid': errorMessage }" />
    <small v-if="errorMessage" class="text-red-500">{{ errorMessage }}</small>
  </div>
</template>
