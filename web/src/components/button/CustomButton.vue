<script setup lang="ts">
import Button from 'primevue/button';

withDefaults(defineProps<{
  label: string;
  variant?: 'primary' | 'secondary' | 'danger' | 'success';
  size?: 'small' | 'medium' | 'large';
  fullWidth?: boolean;
  disabled?: boolean;
  className?: string;
  icon?: string;
  iconPosition?: 'left' | 'right';
}>(), {
  variant: 'primary',
  size: 'small',
  fullWidth: false,
  disabled: false,
  className: '',
  icon: '',
  iconPosition: 'left',
});

const emit = defineEmits<{
  click: [];
}>();
</script>

<template>
  <Button :label="label" @click="emit('click')" :variant="variant" :size="size" :fullWidth="fullWidth"
    :disabled="disabled" :icon="icon" :iconPosition="iconPosition" :class="className" />
</template>