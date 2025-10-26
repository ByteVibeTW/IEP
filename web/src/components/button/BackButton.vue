<script setup lang="ts">
import CustomButton from './CustomButton.vue';

withDefaults(defineProps<{
  label?: string;
}>(), {
  label: '返回',
});

const emit = defineEmits<{
  click: [];
}>();
</script>

<template>
  <CustomButton :label="label" @click="emit('click')" icon="pi pi-arrow-left" />
</template>