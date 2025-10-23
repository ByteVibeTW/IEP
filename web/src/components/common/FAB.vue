<template>
    <button :class="fabClasses" @click="handleClick" :disabled="disabled">
        <slot>
            <i class="pi pi-cog" aria-hidden="true"></i>
        </slot>
    </button>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps({
    /**
     * 按鈕位置
     * bottom-right, bottom-left, top-right, top-left
     */
    position: {
        type: String,
        default: 'bottom-right',
        validator: (value) => [
            'bottom-right',
            'bottom-left',
            'top-right',
            'top-left'
        ].includes(value)
    },
    /**
     * 按鈕尺寸
     * small, medium, large
     */
    size: {
        type: String,
        default: 'medium',
        validator: (value) => ['small', 'medium', 'large'].includes(value)
    },
    /**
     * 按鈕顏色主題
     * primary, secondary, success, info, warning, help, danger
     */
    severity: {
        type: String,
        default: 'primary'
    },
    /**
     * 距離邊緣的距離
     */
    edgeDistance: {
        type: String,
        default: '24px'
    },
    /**
     * 是否顯示 AI 圖標
     */
    showAI: {
        type: Boolean,
        default: true
    },
    /**
     * 是否禁用作
     */
    disabled: {
        type: Boolean,
        default: false
    },
    /**
     * 是否顯示載入動畫
     */
    loading: {
        type: Boolean,
        default: false
    }
});

const emit = defineEmits(['click']);

const fabClasses = computed(() => {
    const baseClasses = 'fixed z-50 rounded-full shadow-lg border-0 transition-all duration-300 ease-in-out transform hover:scale-105 active:scale-95';

    // 位置類別
    const positionClasses = {
        'bottom-right': 'bottom-6 right-6',
        'bottom-left': 'bottom-6 left-6',
        'top-right': 'top-6 right-6',
        'top-left': 'top-6 left-6'
    };

    // 尺寸類別
    const sizeClasses = {
        small: 'w-12 h-12 text-lg',
        medium: 'w-14 h-14 text-xl',
        large: 'w-16 h-16 text-2xl'
    };

    // 顏色主題類別
    const severityClasses = {
        primary: 'bg-blue-500 hover:bg-blue-600 text-white border-blue-600',
        secondary: 'bg-gray-500 hover:bg-gray-600 text-white border-gray-600',
        success: 'bg-green-500 hover:bg-green-600 text-white border-green-600',
        info: 'bg-cyan-500 hover:bg-cyan-600 text-white border-cyan-600',
        warning: 'bg-yellow-500 hover:bg-yellow-600 text-white border-yellow-600',
        help: 'bg-purple-500 hover:bg-purple-600 text-white border-purple-600',
        danger: 'bg-red-500 hover:bg-red-600 text-white border-red-600'
    };

    // 禁用狀態
    const disabledClasses = props.disabled
        ? 'opacity-50 cursor-not-allowed'
        : 'cursor-pointer';

    // 載入狀態
    const loadingClasses = props.loading
        ? 'animate-pulse'
        : '';

    return [
        baseClasses,
        positionClasses[props.position],
        sizeClasses[props.size],
        severityClasses[props.severity],
        disabledClasses,
        loadingClasses
    ].join(' ');
});

const handleClick = (event) => {
    if (!props.disabled && !props.loading) {
        emit('click', event);
    }
};
</script>

<style scoped>
/* AI 圖標動畫 */
.fab-ai-icon {
    animation: ai-pulse 2s infinite;
}

@keyframes ai-pulse {

    0%,
    100% {
        transform: scale(1);
        filter: drop-shadow(0 0 5px rgba(34, 197, 94, 0.5));
    }

    50% {
        transform: scale(1.1);
        filter: drop-shadow(0 0 10px rgba(34, 197, 94, 0.8));
    }
}

/* 呼吸燈效果 */
.fab-breathe {
    animation: breathe 3s ease-in-out infinite;
}

@keyframes breathe {

    0%,
    100% {
        box-shadow: 0 8px 25px rgba(59, 130, 246, 0.3);
    }

    50% {
        box-shadow: 0 12px 35px rgba(59, 130, 246, 0.5);
    }
}

/* 懸浮動畫 */
.fab-hover {
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.fab-hover:hover {
    transform: translateY(-2px);
}
</style>
