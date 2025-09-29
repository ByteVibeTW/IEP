<template>
    <div :class="containerClasses">
        <slot />
    </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
    /**
     * 容器的最大寬度
     * sm, md, lg, xl, 2xl, 3xl, 4xl, 5xl, 6xl, 7xl, full, max, min, fit
     * 或是任意 max-w-[value]
     */
    maxWidth: {
        type: String,
        default: '7xl'
    },
    /**
     * 左右內邊距
     * 預設: px-4 md:px-6 lg:px-8
     */
    padding: {
        type: String,
        default: 'px-4 md:px-6 lg:px-8'
    },
    /**
     * 是否使用響應式設計
     * 預設: true
     */
    responsive: {
        type: Boolean,
        default: true
    },
    /**
     * 額外的自定義 CSS 類別
     */
    customClass: {
        type: String,
        default: ''
    }
});

const containerClasses = computed(() => {
    const baseClasses = 'mx-auto';

    let maxWidthClass;
    if (['sm', 'md', 'lg', 'xl', '2xl', '3xl', '4xl', '5xl', '6xl', '7xl', 'full', 'max', 'min', 'fit'].includes(props.maxWidth)) {
        maxWidthClass = `max-w-${props.maxWidth}`;
    } else if (props.maxWidth.startsWith('max-w-')) {
        maxWidthClass = props.maxWidth;
    } else if (props.maxWidth.includes('px') || props.maxWidth.includes('rem') || props.maxWidth.includes('%')) {
        maxWidthClass = `max-w-[${props.maxWidth}]`;
    } else {
        maxWidthClass = 'max-w-7xl';
    }

    // 處理 padding
    const paddingClass = props.padding || 'px-4 md:px-6 lg:px-8';

    // 組合所有類別
    const allClasses = [
        baseClasses,
        maxWidthClass,
        props.responsive ? '' : '',
        paddingClass,
        props.customClass
    ].filter(Boolean);

    return allClasses.join(' ');
});
</script>
