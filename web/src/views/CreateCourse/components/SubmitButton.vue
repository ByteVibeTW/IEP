<template>
  <Button label="提交審核" class="w-[100%] mt-4" :disabled="!isFormValid || isPending" :loading="isPending"
    @click="handleSubmit" />
</template>

<script setup lang="ts">
import Button from 'primevue/button';

defineProps<{
  isFormValid: boolean;
  isPending: boolean;
}>();

const emit = defineEmits<{
  submit: [];
}>();

const handleSubmit = () => {
  emit('submit');
};
</script>
