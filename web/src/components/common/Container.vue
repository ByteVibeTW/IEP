<template>
    <div :class="['mx-auto', maxWidthClass, padding]">
        <slot />
    </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps({
    /**
     * 容器的最大寬度類別
     */
    maxWidth: {
        type: String,
        default: 'max-w-7xl'
    },
    /**
     * 左右內邊距
     */
    padding: {
        type: String,
        default: 'px-4 md:px-[64px]'
    },
});

const maxWidthClass = computed(() => {
    return props.maxWidth;
});
</script>
