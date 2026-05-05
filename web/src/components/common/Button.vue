<template>
  <button
    :class="[
      'px-4 py-2 rounded-xl font-semibold transition-colors duration-200 shadow-sm inline-flex items-center justify-center',
      variant === 'primary' && 'bg-sky-500 hover:bg-sky-600 text-white',
      variant === 'secondary' && 'bg-white border border-sky-100 text-sky-700 hover:bg-sky-50',
      variant === 'danger' && 'bg-rose-500 hover:bg-rose-600 text-white',
      variant === 'success' && 'bg-emerald-500 hover:bg-emerald-600 text-white',
      size === 'sm' && 'text-sm px-3 py-1.5',
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
