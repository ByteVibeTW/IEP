<template>
    <FAB position="bottom-right" size="large" severity="info" class="fab-hover fab-breathe" @click="handleClick">
        <i class="pi pi-sparkles fab-ai-icon text-green-400" style="font-size: 1.5rem;"></i>
    </FAB>
</template>

<script setup lang="ts">
import FAB from './FAB.vue'

const emit = defineEmits<{
  click: [event: MouseEvent]
}>()

const handleClick = (event: MouseEvent): void => {
  emit('click', event)
}
</script>

<style scoped>
/* AI 圖標特殊效果 */
.pi-sparkles {
    background: linear-gradient(45deg, #10b981, #3b82f6, #8b5cf6, #f59e0b);
    background-size: 400% 400%;
    -webkit-background-clip: text;
    background-clip: text;
    -webkit-text-fill-spear: transparent;
    animation: sparkle-gradient 3s ease infinite;
}

@keyframes sparkle-gradient {
    0% {
        background-position: 0% 50%;
    }

    50% {
        background-position: 100% 50%;
    }

    100% {
        background-position: 0% 50%;
    }
}

/* 額外的發光效果 */
.fab-hover:hover .pi-sparkles {
    filter: drop-shadow(0 0 8px rgba(16, 185, 129, 0.6));
}
</style>
