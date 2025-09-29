<template>
    <div v-if="isVisible"
        class="fixed bottom-20 right-6 w-[500px] h-[800px] bg-white rounded-lg shadow-2xl border border-gray-200 z-50 flex flex-col">
        <!-- 對話框標題列 -->
        <div
            class="flex items-center justify-between p-4 border-b border-gray-200 bg-gradient-to-r from-blue-500 to-purple-600 text-white rounded-t-lg">
            <div class="flex items-center space-x-2">
                <i class="pi pi-sparkles text-green-300"></i>
                <h3 class="font-semibold">AI Tutor</h3>
            </div>
            <button @click="closeDialog" class="text-white hover:text-gray-200 transition-colors duration-200">
                <i class="pi pi-times text-lg"></i>
            </button>
        </div>

        <!-- 聊天訊息區域 -->
        <div class="flex-1 overflow-y-auto p-6 space-y-4" ref="messagesContainer">
            <div v-for="(message, index) in messages" :key="index" :class="[
                'flex',
                message.type === 'user' ? 'justify-end' : 'justify-start'
            ]">
                <div :class="[
                    'max-w-sm px-4 py-3 rounded-lg text-sm',
                    message.type === 'user'
                        ? 'bg-blue-500 text-white'
                        : 'bg-gray-100 text-gray-800'
                ]">
                    <div v-if="message.type === 'ai' && message.options" class="space-y-2">
                        <p class="mb-3 whitespace-pre-line">{{ message.content }}</p>
                        <div class="flex flex-wrap gap-2">
                            <button v-for="option in message.options" :key="option" @click="selectOption(option)"
                                class="px-3 py-2 bg-white border border-gray-300 rounded-lg text-xs hover:bg-gray-50 hover:border-blue-300 transition-colors duration-200">
                                {{ option }}
                            </button>
                        </div>
                    </div>
                    <div v-else class="whitespace-pre-line">
                        {{ message.content }}
                    </div>
                </div>
            </div>

            <!-- 歡迎訊息 -->
            <div v-if="messages.length === 0" class="text-center text-gray-500 text-sm">
                <i class="pi pi-sparkles text-2xl mb-2 block"></i>
                <p class="text-lg font-medium mb-2">請問你想要學習什麼知識？</p>
                <p class="text-sm">請輸入你想要學習的知識，我將為你生成個性化的課程</p>
            </div>

            <!-- 生成課程按鈕 -->
            <div v-if="showGenerateButton" class="flex justify-center mt-4">
                <button @click="generateCourse"
                    class="px-6 py-3 bg-gradient-to-r from-blue-500 to-purple-600 text-white rounded-lg hover:from-blue-600 hover:to-purple-700 transition-all duration-200 flex items-center space-x-2 shadow-lg">
                    <i class="pi pi-sparkles"></i>
                    <span>生成課程</span>
                </button>
            </div>
        </div>

        <!-- 輸入區域 -->
        <div class="p-6 border-t border-gray-200">
            <div class="flex space-x-2">
                <input v-model="newMessage" @keyup.enter="sendMessage" type="text" placeholder="輸入您想學習的知識..."
                    class="flex-1 px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent text-sm" />
                <button @click="sendMessage" :disabled="!newMessage.trim()"
                    class="px-5 py-3 bg-blue-500 text-white rounded-lg hover:bg-blue-600 disabled:opacity-50 disabled:cursor-not-allowed transition-colors duration-200 text-sm">
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

const emit = defineEmits(['close', 'generateCourse']);

const messages = ref([]);
const newMessage = ref('');
const messagesContainer = ref(null);
const showGenerateButton = ref(false);
const conversationContext = ref({
    topic: '',
    level: '',
    duration: '',
    goals: '',
    format: 'course'
});
const questionCount = ref(0);

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

    // 處理 AI 教練邏輯
    setTimeout(() => {
        const aiResponse = generateCoachResponse(userMessage);
        messages.value.push({
            type: 'ai',
            content: typeof aiResponse === 'string' ? aiResponse : aiResponse.content,
            options: typeof aiResponse === 'object' ? aiResponse.options : null
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

const generateCoachResponse = (userMessage) => {
    // 如果是第一次對話，記錄學習主題
    if (questionCount.value === 0) {
        conversationContext.value.topic = userMessage;
        questionCount.value++;
        return {
            content: `您想學習「${userMessage}」。\n您之前對 React 有什麼經驗？\n（例如，建置的基本元件、具有類別元件的託管狀態、使用的生命週期方法）`,
            options: ['沒', '對組件的基本了解', '具有類別元件和生命週期方法的經驗']
        };
    }

    // 根據問題順序收集信息
    if (questionCount.value === 1) {
        conversationContext.value.level = userMessage;
        questionCount.value++;
        return {
            content: `您希望通過學習 React Hooks 實現什麼目標？\n（例如，簡化元件邏輯、替換類別元件、提高程式碼可重複使用性）`,
            options: ['了解 React Hooks 的基礎知識', '將類別元件取代為功能元件', '改善 React 應用程式中的狀態管理', '編寫更乾淨、更可重複使用的 React 程式碼']
        };
    }

    if (questionCount.value === 2) {
        conversationContext.value.duration = userMessage;
        questionCount.value++;
        showGenerateButton.value = true;
        return {
            content: `您最初最想了解哪些 React Hooks？ \n（例如，useState、useEffect、useContext、useRef、useReducer、useCallback、useMemo、自訂 Hooks）`,
            options: ['useState、useEffect 和 useContextAll the basic hooks  所有基本掛鉤', '進階掛鉤和自訂 Hooks']
        };
    }

    if (questionCount.value === 3) {
        conversationContext.value.goals = userMessage;
        questionCount.value++;
        return {
            content: `您能否描述一下您目前正在開發或計劃構建的特定 React 元件或應用程序，您認為 Hooks 可能有益？`,
            options: ['沒有具體項目', '具有本機狀態的簡單元件', '具有多種副作用的複雜組件', '具有複雜狀態管理的完整應用程式']
        };
    }

    if (questionCount.value === 4) {
        conversationContext.value.goals = userMessage;
        questionCount.value++;
        return {
            content: `React 開發中是否有任何特定領域讓您覺得具有挑戰性或令人困惑？`,
            options: ['狀態管理', '元件生命週期', '處理副作用', '效能最佳化']
        };
    }

    if (questionCount.value === 5) {
        conversationContext.value.goals = userMessage;
        questionCount.value++;
        return {
            content: `您是否有函數式程式設計概念\n（例如不變性和純函數）的經驗？`,
            options: ['沒有經驗', '有些熟悉', '熟悉基礎知識', '豐富的經驗']
        };
    }

    if (questionCount.value === 6) {
        conversationContext.value.goals = userMessage;
        questionCount.value++;
        return {
            content: `您熟悉常見的 React 模式（例如渲染道具或高階元件）嗎？`,
            options: ['不熟悉', '聽說過它們，但沒有使用過它們', '偶爾使用它們', '熟悉它們並經常使用它們']
        };
    }

    // 如果已經完成所有問題，提供額外幫助
    return '我已經收集到足夠的信息來生成您的課程。如果您還有其他問題或想要調整任何設定，請告訴我！';
};

const selectOption = (option) => {
    // 將選中的選項作為用戶訊息發送
    newMessage.value = option;
    sendMessage();
};

const generateCourse = () => {
    // 生成 React Hooks 課程資料
    const generatedCourse = {
        course_id: `react_hooks_${Date.now()}`,
        course_name: 'React Hooks：初學者指南',
        course_type: '程式設計',
        course_intro: `我為您量身定制了這個 React Hooks 課程。課程將從基礎概念開始，逐步深入進階應用，幫助您掌握現代 React 開發的核心技能。`,
        course_outline: '1. React Hooks 簡介\n2. 掌握 useState Hook\n3. 深入探討 useEffect 鉤子\n4. 了解使用 Hooks 的元件生命週期\n5. React 中的不變性和純函數\n6. 使用 useState 和 useEffect 建立完整的應用程式',
        course_price: 0, // AI 生成的課程免費
        course_image: '',
        teacher_id: 'ai_tutor',
        students: [],
        rating: 5.0,
        isAIGenerated: true
    };

    // 發送課程生成事件給父組件
    emit('generateCourse', {
        course: generatedCourse,
        context: conversationContext.value
    });

    // 顯示生成完成的訊息
    messages.value.push({
        type: 'ai',
        content: '🎉 課程生成完成！\n\n我已經為您創建了一個完整的 React Hooks 學習課程，包含：\n\n📚 8個章節的系統化學習內容\n🎯 根據您的需求定制的學習路徑\n💡 實戰練習和專案應用\n\n課程已添加到您的課程列表中，您可以立即開始學習！'
    });

    nextTick(() => {
        scrollToBottom();
    });
};

// 監聽對話框顯示狀態，重置訊息
watch(() => props.isVisible, (newVal) => {
    if (newVal) {
        messages.value = [];
        showGenerateButton.value = false;
        questionCount.value = 0;
        conversationContext.value = {
            topic: '',
            level: '',
            duration: '',
            goals: '',
            format: 'course'
        };
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
