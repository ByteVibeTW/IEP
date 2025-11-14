<script setup lang="ts">
import Button from 'primevue/button'

const emit = defineEmits<{
    click: [event: MouseEvent]
}>()

const handleClick = (event: MouseEvent): void => {
    emit('click', event)
}
</script>

<template>
    <Button severity="info" size="large" rounded class="ai-fab-button" @click="handleClick">
        <i class="pi pi-comments" style="font-size: 1.5rem;"></i>
    </Button>
</template>

<style scoped>
/* FAB 按鈕定位和樣式 */
.ai-fab-button {
    position: fixed;
    bottom: 24px;
    right: 24px;
    z-index: 50;
    width: 60px;
    height: 60px;
    border-radius: 50%;
}
</style>
