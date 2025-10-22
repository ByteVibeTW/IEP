<template>
  <button
    :class="[
      'px-4 py-2 rounded-lg transition-colors duration-200',
      variant === 'primary' && 'bg-[#3498db] hover:bg-[#2d83bc] text-white',
      variant === 'secondary' && 'bg-gray-500 hover:bg-gray-600 text-white',
      variant === 'danger' && 'bg-red-500 hover:bg-red-600 text-white',
      variant === 'success' && 'bg-green-500 hover:bg-green-600 text-white',
      size === 'sm' && 'text-sm px-2 py-1',
      size === 'lg' && 'text-lg px-6 py-3',
      fullWidth && 'w-full',
      className,
    ]"
    :disabled="disabled"
    @click="$emit('click')"
  >
    <slot />
  </button>
</template>

<script setup lang="ts">
interface Props {
  variant?: 'primary' | 'secondary' | 'danger' | 'success'
  size?: 'sm' | 'md' | 'lg'
  fullWidth?: boolean
  disabled?: boolean
  className?: string
}

withDefaults(defineProps<Props>(), {
  variant: 'primary',
  size: 'md',
  fullWidth: false,
  disabled: false,
  className: '',
})

defineEmits<{
  click: []
}>()
</script>
