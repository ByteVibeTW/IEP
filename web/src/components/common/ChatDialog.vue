<template>
    <div v-if="isVisible"
        class="fixed bottom-20 right-6 w-80 h-96 bg-white rounded-lg shadow-2xl border border-gray-200 z-50 flex flex-col">
        <!-- 對話框標題列 -->
        <div
            class="flex items-center justify-between p-4 border-b border-gray-200 bg-gradient-to-r from-blue-500 to-purple-600 text-white rounded-t-lg">
            <div class="flex items-center space-x-2">
                <i class="pi pi-sparkles text-green-300"></i>
                <h3 class="font-semibold">AI 助手</h3>
            </div>
            <button @click="closeDialog" class="text-white hover:text-gray-200 transition-colors duration-200">
                <i class="pi pi-times text-lg"></i>
            </button>
        </div>

        <!-- 聊天訊息區域 -->
        <div class="flex-1 overflow-y-auto p-4 space-y-3" ref="messagesContainer">
            <div v-for="(message, index) in messages" :key="index" :class="[
                'flex',
                message.type === 'user' ? 'justify-end' : 'justify-start'
            ]">
                <div :class="[
                    'max-w-xs px-3 py-2 rounded-lg text-sm',
                    message.type === 'user'
                        ? 'bg-blue-500 text-white'
                        : 'bg-gray-100 text-gray-800'
                ]">
                    {{ message.content }}
                </div>
            </div>

            <!-- 歡迎訊息 -->
            <div v-if="messages.length === 0" class="text-center text-gray-500 text-sm">
                <i class="pi pi-sparkles text-2xl mb-2 block"></i>
                <p>您好！我是您的 AI 助手，有什麼可以幫助您的嗎？</p>
            </div>
        </div>

        <!-- 輸入區域 -->
        <div class="p-4 border-t border-gray-200">
            <div class="flex space-x-2">
                <input v-model="newMessage" @keyup.enter="sendMessage" type="text" placeholder="輸入您的問題..."
                    class="flex-1 px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent text-sm" />
                <button @click="sendMessage" :disabled="!newMessage.trim()"
                    class="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 disabled:opacity-50 disabled:cursor-not-allowed transition-colors duration-200 text-sm">
                    <i class="pi pi-send"></i>
                </button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, nextTick, watch } from 'vue';

const props = defineProps({
    isVisible: {
        type: Boolean,
        default: false
    }
});

const emit = defineEmits(['close']);

const messages = ref([]);
const newMessage = ref('');
const messagesContainer = ref(null);

const closeDialog = () => {
    emit('close');
};

const sendMessage = async () => {
    if (!newMessage.value.trim()) return;

    // 添加用戶訊息
    messages.value.push({
        type: 'user',
        content: newMessage.value.trim()
    });

    const userMessage = newMessage.value.trim();
    newMessage.value = '';

    // 滾動到底部
    await nextTick();
    scrollToBottom();

    // 模擬 AI 回應
    setTimeout(() => {
        const aiResponse = generateAIResponse(userMessage);
        messages.value.push({
            type: 'ai',
            content: aiResponse
        });

        nextTick(() => {
            scrollToBottom();
        });
    }, 1000);
};

const scrollToBottom = () => {
    if (messagesContainer.value) {
        messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
    }
};

const generateAIResponse = (userMessage) => {
    const responses = [
        '我理解您的問題，讓我為您提供一些建議...',
        '這是一個很好的問題！根據我的分析...',
        '感謝您的提問，我來幫您解答這個問題。',
        '我明白您的需求，讓我為您詳細說明...',
        '這確實是個重要的議題，我建議您...'
    ];

    // 簡單的關鍵字回應
    if (userMessage.includes('課程') || userMessage.includes('學習')) {
        return '關於課程學習，我建議您可以先查看課程大綱，然後按照章節順序進行學習。如果有任何疑問，隨時可以詢問我！';
    } else if (userMessage.includes('作業') || userMessage.includes('練習')) {
        return '完成作業和練習是學習的重要環節。建議您先理解概念，然後逐步完成練習題。遇到困難時，可以回顧相關章節內容。';
    } else if (userMessage.includes('成績') || userMessage.includes('評分')) {
        return '成績評分通常基於多個因素，包括作業完成度、測驗表現和參與度。建議您積極參與課程活動，按時完成作業。';
    } else {
        return responses[Math.floor(Math.random() * responses.length)];
    }
};

// 監聽對話框顯示狀態，重置訊息
watch(() => props.isVisible, (newVal) => {
    if (newVal) {
        messages.value = [];
    }
});
</script>

<style scoped>
/* 自定義滾動條 */
.overflow-y-auto::-webkit-scrollbar {
    width: 4px;
}

.overflow-y-auto::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 2px;
}

.overflow-y-auto::-webkit-scrollbar-thumb {
    background: #c1c1c1;
    border-radius: 2px;
}

.overflow-y-auto::-webkit-scrollbar-thumb:hover {
    background: #a8a8a8;
}

/* 動畫效果 */
.fixed {
    animation: slideInUp 0.3s ease-out;
}

@keyframes slideInUp {
    from {
        transform: translateY(100%);
        opacity: 0;
    }

    to {
        transform: translateY(0);
        opacity: 1;
    }
}
</style>
