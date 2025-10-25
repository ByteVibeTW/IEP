<script setup lang="ts">
import { useField } from 'vee-validate';
import InputText from 'primevue/inputtext';

interface Props {
  name: string;
  label?: string;
  placeholder?: string;
  type?: string;
}

const props = withDefaults(defineProps<Props>(), {
  type: 'text',
});

const { value, errorMessage } = useField<string>(() => props.name);
</script>

<template>
  <div class="mb-6">
    <label v-if="label" :for="name" class="text-[20px] font-bold mb-[10px] block">
      {{ label }}
    </label>
    <InputText :id="name" v-model="value" :type="type" :placeholder="placeholder" class="w-full"
      :class="{ 'p-invalid': errorMessage }" />
    <small v-if="errorMessage" class="text-red-500">{{ errorMessage }}</small>
  </div>
</template>