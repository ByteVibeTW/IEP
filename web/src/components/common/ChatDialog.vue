<script setup lang="ts">
import { ref, nextTick, watch } from 'vue'
import Dialog from 'primevue/dialog'
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import { useGenerateQuestion, useGenerateCourse, getGetSelectedCoursesQueryKey } from '@/api/api'
import { baseQueryClient } from '@/api/base/BaseQueryClient'
import type { QuestionsResponseDto, UserAnswerDto, QuestionDto } from '@/api/model'

interface Message {
    type: 'user' | 'ai'
    content: string
    options?: string[]
    questionText?: string // 記錄問題文本，用於生成答案
}

interface Props {
    isVisible: boolean
}

const props = defineProps<Props>()

const emit = defineEmits<{
    close: []
    courseGenerated: []
}>()

const messages = ref<Message[]>([])
const newMessage = ref<string>('')
const messagesContainer = ref<HTMLElement | null>(null)
const showGenerateButton = ref<boolean>(false)
const isLoading = ref<boolean>(false)
const userAnswers = ref<UserAnswerDto[]>([]) // 追蹤用戶的回答
const allQuestions = ref<QuestionDto[]>([]) // 保存所有問題
const currentQuestionIndex = ref<number>(0) // 追蹤當前問題索引

// 初始化 useGenerateQuestion hook
const generateQuestionMutation = useGenerateQuestion({
    mutation: {
        onSuccess: (response) => {
            isLoading.value = false
            handleAIResponse(response)
        },
        onError: (error) => {
            isLoading.value = false
            console.error('生成問題失敗:', error)
            // 顯示錯誤訊息
            messages.value.push({
                type: 'ai',
                content: '抱歉，生成問題時發生錯誤。請稍後再試。',
                options: ['重試', '繼續對話']
            })
            scrollToBottom()
        }
    }
})

// 初始化 useGenerateCourse hook
const generateCourseMutation = useGenerateCourse({
    mutation: {
        onSuccess: () => {
            isLoading.value = false
            // 移除「正在生成課程，請稍候...」的訊息
            const lastMessage = messages.value[messages.value.length - 1]
            if (lastMessage && lastMessage.type === 'ai' && lastMessage.content === '正在生成課程，請稍候...') {
                messages.value.pop()
            }
            // 顯示成功訊息
            messages.value.push({
                type: 'ai',
                content: '課程生成成功！請到「我的課程」頁面查看。'
            })
            scrollToBottom()
            // 重新獲取已選課程
            baseQueryClient.invalidateQueries({ queryKey: getGetSelectedCoursesQueryKey() })
            // 觸發成功事件
            emit('courseGenerated')
            // 延遲關閉對話框
            setTimeout(() => {
                emit('close')
            }, 2000)
        },
        onError: (error) => {
            isLoading.value = false
            console.error('生成課程失敗:', error)
            // 移除「正在生成課程，請稍候...」的訊息
            const lastMessage = messages.value[messages.value.length - 1]
            if (lastMessage && lastMessage.type === 'ai' && lastMessage.content === '正在生成課程，請稍候...') {
                messages.value.pop()
            }
            // 顯示錯誤訊息
            messages.value.push({
                type: 'ai',
                content: '抱歉，生成課程時發生錯誤。請稍後再試。',
                options: ['重試', '關閉']
            })
            scrollToBottom()
        }
    }
})

// 監聽對話框顯示狀態，重置訊息
watch(() => props.isVisible, (visible) => {
    if (visible) {
        messages.value = []
        newMessage.value = ''
        showGenerateButton.value = false
        isLoading.value = false
        userAnswers.value = [] // 重置用戶回答
        allQuestions.value = [] // 重置問題列表
        currentQuestionIndex.value = 0 // 重置問題索引
    }
})

// 自動滾動到底部
const scrollToBottom = async () => {
    await nextTick()
    if (messagesContainer.value) {
        messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
}

// 處理 AI 回應
const handleAIResponse = async (response: QuestionsResponseDto) => {
    // 移除「正在思考中...」的訊息
    const lastMessage = messages.value[messages.value.length - 1]
    if (lastMessage && lastMessage.type === 'ai' && lastMessage.content === '正在思考中...') {
        messages.value.pop()
    }

    // customInstant 已經提取了 data，所以 response 直接是 QuestionsResponseDto
    if (response?.questions && response.questions.length > 0) {
        // 保存所有問題
        allQuestions.value = response.questions
        currentQuestionIndex.value = 0

        // 顯示第一個問題
        showNextQuestion()
    } else {
        // 沒有問題，直接顯示可以生成課程
        const aiResponse: Message = {
            type: 'ai',
            content: '好的！我已經收集到足夠的資訊，現在可以為您生成個性化的課程了。',
            options: ['生成課程', '繼續對話']
        }
        messages.value.push(aiResponse)
        showGenerateButton.value = true
    }
    await scrollToBottom()
}

// 顯示下一個問題
const showNextQuestion = async () => {
    if (currentQuestionIndex.value < allQuestions.value.length) {
        const question = allQuestions.value[currentQuestionIndex.value]
        if (question) {
            const aiResponse: Message = {
                type: 'ai',
                content: question.questionText || '讓我為您設計一個完整的學習計劃。',
                options: question.options || [],
                questionText: question.questionText // 記錄問題文本
            }
            messages.value.push(aiResponse)
            await scrollToBottom()
        }
    }

    // 檢查是否所有問題都已顯示完畢
    if (currentQuestionIndex.value >= allQuestions.value.length - 1) {
        showGenerateButton.value = true
    }
}

// 發送訊息
const sendMessage = async () => {
    if (!newMessage.value.trim() || isLoading.value) return

    // 添加用戶訊息
    messages.value.push({
        type: 'user',
        content: newMessage.value.trim()
    })

    const userMessage = newMessage.value.trim()
    newMessage.value = ''

    await scrollToBottom()

    // 顯示載入狀態
    isLoading.value = true
    messages.value.push({
        type: 'ai',
        content: '正在思考中...'
    })
    await scrollToBottom()

    // 調用 API 生成問題
    try {
        generateQuestionMutation.mutate({
            data: userMessage
        })
    } catch (error) {
        isLoading.value = false
        console.error('發送請求失敗:', error)
        // 移除載入訊息
        messages.value.pop()
        messages.value.push({
            type: 'ai',
            content: '抱歉，發生錯誤。請稍後再試。',
            options: ['重試']
        })
        await scrollToBottom()
    }
}

// 選擇選項
const selectOption = async (option: string) => {
    if (option === '生成課程') {
        generateCourse()
    } else if (option === '重試') {
        // 如果是生成課程失敗的重試
        const lastMessage = messages.value[messages.value.length - 1]
        if (lastMessage && lastMessage.content.includes('生成課程時發生錯誤')) {
            generateCourse()
        } else {
            // 重試最後一個用戶訊息
            const lastUserMessage = [...messages.value].reverse().find(msg => msg.type === 'user')
            if (lastUserMessage) {
                newMessage.value = lastUserMessage.content
                sendMessage()
            }
        }
    } else if (option === '關閉') {
        emit('close')
    } else {
        // 記錄用戶選擇的答案
        const lastAIMessage = [...messages.value].reverse().find(msg => msg.type === 'ai' && msg.questionText)
        if (lastAIMessage && lastAIMessage.questionText) {
            // 將問題和答案記錄到 userAnswers
            userAnswers.value.push({
                questionText: lastAIMessage.questionText,
                option: option
            })
        }

        // 添加用戶選擇的訊息
        messages.value.push({
            type: 'user',
            content: option
        })
        await scrollToBottom()

        // 檢查是否還有下一個問題
        if (currentQuestionIndex.value < allQuestions.value.length - 1) {
            // 顯示下一個問題
            currentQuestionIndex.value++
            await showNextQuestion()
        } else {
            // 所有問題都已回答完畢，顯示可以生成課程
            if (!showGenerateButton.value) {
                messages.value.push({
                    type: 'ai',
                    content: '好的！我已經收集到足夠的資訊，現在可以為您生成個性化的課程了。',
                    options: ['生成課程']
                })
                showGenerateButton.value = true
                await scrollToBottom()
            }
        }
    }
}

// 生成課程
const generateCourse = () => {
    // 收集所有用戶的回答
    // 如果還有未記錄的答案，先記錄最後一個選擇
    const lastAIMessage = [...messages.value].reverse().find(msg => msg.type === 'ai' && msg.questionText)
    const lastUserMessage = [...messages.value].reverse().find(msg => msg.type === 'user')

    // 如果最後一個 AI 訊息有問題，且最後一個用戶訊息是選擇的選項，記錄它
    if (lastAIMessage && lastAIMessage.questionText && lastUserMessage) {
        const isAlreadyRecorded = userAnswers.value.some(
            answer => answer.questionText === lastAIMessage.questionText && answer.option === lastUserMessage.content
        )
        if (!isAlreadyRecorded && lastAIMessage.options?.includes(lastUserMessage.content)) {
            userAnswers.value.push({
                questionText: lastAIMessage.questionText,
                option: lastUserMessage.content
            })
        }
    }

    // 如果沒有回答，至少記錄初始需求
    if (userAnswers.value.length === 0) {
        const firstUserMessage = messages.value.find(msg => msg.type === 'user')
        if (firstUserMessage) {
            userAnswers.value.push({
                questionText: '學習需求',
                option: firstUserMessage.content
            })
        }
    }

    // 顯示載入狀態
    isLoading.value = true
    messages.value.push({
        type: 'ai',
        content: '正在生成課程，請稍候...'
    })
    scrollToBottom()

    // 調用 API 生成課程
    try {
        generateCourseMutation.mutate({
            data: userAnswers.value
        })
    } catch (error) {
        isLoading.value = false
        console.error('發送請求失敗:', error)
        // 移除載入訊息
        messages.value.pop()
        messages.value.push({
            type: 'ai',
            content: '抱歉，發生錯誤。請稍後再試。',
            options: ['重試', '關閉']
        })
        scrollToBottom()
    }
}

// 關閉對話框
const closeDialog = () => {
    emit('close')
}
</script>

<template>
    <Dialog :visible="isVisible" @update:visible="closeDialog" :modal="false" :closable="true" :draggable="true"
        :resizable="true" position="bottomright" :style="{
            width: '500px',
            height: '800px'
        }" :breakpoints="{
            '1199px': '450px',
            '991px': '400px',
            '767px': '90vw',
            '575px': '95vw'
        }" :pt="{
            root: { class: 'chat-dialog-root' },
            header: {
                class: 'bg-blue-900 text-white rounded-t-lg border-0'
            },
            content: {
                class: 'p-0 flex flex-col h-full overflow-hidden'
            }
        }">
        <template #header>
            <div class="flex items-center justify-between w-full">
                <div class="flex items-center space-x-2">
                    <i class="pi pi-sparkles"></i>
                    <h3 class="font-semibold text-white m-0">AI Tutor</h3>
                </div>
            </div>
        </template>

        <!-- 聊天訊息區域 -->
        <div ref="messagesContainer" class="flex-1 overflow-y-auto p-6 space-y-4 chat-messages">
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
                            <Button v-for="option in message.options" :key="option" :label="option" size="small"
                                severity="secondary" outlined @click="selectOption(option)" class="text-xs" />
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
            <div v-if="showGenerateButton && !isLoading" class="flex justify-center mt-4">
                <Button label="生成課程" icon="pi pi-sparkles" severity="info" @click="generateCourse" class="shadow-lg" />
            </div>

            <!-- 載入中指示器 -->
            <div v-if="isLoading" class="flex justify-center items-center mt-4">
                <i class="pi pi-spin pi-spinner text-blue-500 text-2xl"></i>
            </div>
        </div>

        <!-- 輸入區域 -->
        <div class="p-4 border-t border-gray-200 bg-white">
            <div class="flex gap-2">
                <InputText v-model="newMessage" placeholder="輸入您想學習的知識..." class="flex-1" @keyup.enter="sendMessage"
                    :disabled="isLoading" />
                <Button icon="pi pi-send" severity="info" :disabled="!newMessage.trim() || isLoading"
                    @click="sendMessage" />
            </div>
        </div>
    </Dialog>
</template>

<style scoped>
/* 使用 PrimeVue 的 position="bottomright" 屬性，只需要調整間距 */
:deep(.p-dialog) {
    margin-bottom: 100px !important;
    margin-right: 24px !important;
}

:deep(.p-dialog-content) {
    height: 800px !important;
    max-height: 800px !important;
}

@media (max-width: 1199px) {
    :deep(.p-dialog-content) {
        height: 700px !important;
        max-height: 700px !important;
    }
}

@media (max-width: 991px) {
    :deep(.p-dialog-content) {
        height: 600px !important;
        max-height: 600px !important;
    }
}

@media (max-width: 767px) {
    :deep(.p-dialog) {
        margin-bottom: 100px !important;
        margin-right: 12px !important;
    }

    :deep(.p-dialog-content) {
        height: 80vh !important;
        max-height: 80vh !important;
    }
}

@media (max-width: 575px) {
    :deep(.p-dialog) {
        margin-bottom: 100px !important;
        margin-right: 8px !important;
    }

    :deep(.p-dialog-content) {
        height: 85vh !important;
        max-height: 85vh !important;
    }
}

.chat-dialog-root {
    display: flex;
    flex-direction: column;
}

.chat-messages {
    min-height: 0;
}

/* 響應式調整 */
@media (max-width: 767px) {
    .chat-messages {
        padding: 1rem;
    }
}

@media (max-width: 575px) {
    .chat-messages {
        padding: 0.75rem;
    }
}

/* 自定義滾動條樣式 */
.chat-messages::-webkit-scrollbar {
    width: 6px;
}

.chat-messages::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 3px;
}

.chat-messages::-webkit-scrollbar-thumb {
    background: #888;
    border-radius: 3px;
}

.chat-messages::-webkit-scrollbar-thumb:hover {
    background: #555;
}
</style>
