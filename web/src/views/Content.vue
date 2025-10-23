<template>
    <DefaultLayout>
        <Container custom-class="py-12">
            <div class="bg-white rounded-2xl shadow p-8">
                <!-- 頁面標題 -->
                <div class="flex items-center justify-between mb-8">
                    <div>
                        <h1 class="text-2xl font-bold text-gray-800">{{ contentData?.itemName }}</h1>
                        <p class="text-gray-600 mt-1">{{ contentData?.courseName }} - {{ contentData?.chapter }}</p>
                    </div>
                    <button @click="goBack"
                        class="px-4 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-colors duration-200">
                        返回課程
                    </button>
                </div>

                <!-- 內容區域 -->
                <div class="prose max-w-none my-8">
                    <div v-if="contentData?.content?.content" v-html="renderedContent" class="content-area"></div>
                    <div v-else class="text-center text-gray-500 py-8">
                        <p>內容載入中...</p>
                    </div>
                </div>

                <!-- 導航按鈕 -->
                <div class="flex justify-between mt-12 pt-8 border-t border-gray-200">
                    <button @click="goBack"
                        class="px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors duration-200">
                        ← 返回課程
                    </button>
                    <button @click="markAsCompleted"
                        class="px-6 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-colors duration-200">
                        標記為已完成 ✓
                    </button>
                </div>
            </div>
        </Container>
    </DefaultLayout>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import DefaultLayout from '@/Layout/default.vue';
import Container from '@/components/common/Container.vue';

const router = useRouter();
const contentData = ref(null);

const renderedContent = computed(() => {
    if (!contentData.value?.content?.content) return '';

    // 簡單的 Markdown 渲染
    let content = contentData.value.content.content;

    // 處理標題
    content = content.replace(/^# (.*$)/gim, '<h1 class="text-3xl font-bold mb-4 text-gray-800">$1</h1>');
    content = content.replace(/^## (.*$)/gim, '<h2 class="text-2xl font-bold mb-3 text-gray-700">$1</h2>');
    content = content.replace(/^### (.*$)/gim, '<h3 class="text-xl font-bold mb-2 text-gray-600">$1</h3>');

    // 處理代碼塊
    content = content.replace(/```javascript\n([\s\S]*?)\n```/g, '<pre class="bg-gray-100 p-4 rounded-lg overflow-x-auto"><code class="language-javascript">$1</code></pre>');
    content = content.replace(/```([\s\S]*?)```/g, '<pre class="bg-gray-100 p-4 rounded-lg overflow-x-auto"><code>$1</code></pre>');

    // 處理行內代碼
    content = content.replace(/`([^`]+)`/g, '<code class="bg-gray-100 px-2 py-1 rounded text-sm">$1</code>');

    // 處理列表
    content = content.replace(/^- (.*$)/gim, '<li class="ml-4">$1</li>');
    content = content.replace(/(<li.*<\/li>)/s, '<ul class="list-disc list-inside mb-4">$1</ul>');

    // 處理段落
    content = content.replace(/\n\n/g, '</p><p class="mb-4">');
    content = '<p class="mb-4">' + content + '</p>';

    // 清理多餘的標籤
    content = content.replace(/<p class="mb-4"><\/p>/g, '');
    content = content.replace(/<p class="mb-4"><h/g, '<h');
    content = content.replace(/<\/h([1-6])><\/p>/g, '</h$1>');

    return content;
});

const goBack = () => {
    // 返回課程頁面
    router.push('/Class');
};

const markAsCompleted = () => {
    // 標記內容為已完成
    alert('已標記為完成！');
    // 這裡可以添加實際的完成狀態保存邏輯
};

onMounted(() => {
    // 從 sessionStorage 獲取內容資料
    const storedContent = sessionStorage.getItem('currentContent');
    console.log('Stored content:', storedContent);

    if (storedContent) {
        contentData.value = JSON.parse(storedContent);
        console.log('Parsed content data:', contentData.value);
    } else {
        console.log('No stored content found');
        // 如果沒有內容資料，返回課程頁面
        router.push('/MyCourse');
    }
});
</script>

<style scoped>
.content-area {
    line-height: 1.8;
    padding: 2rem 0;
}

.content-area h1,
.content-area h2,
.content-area h3 {
    margin-top: 2rem;
    margin-bottom: 1rem;
}

.content-area h1:first-child,
.content-area h2:first-child,
.content-area h3:first-child {
    margin-top: 0;
}

.content-area p {
    margin-bottom: 1rem;
    color: #374151;
}

.content-area ul {
    margin-bottom: 1rem;
}

.content-area li {
    margin-bottom: 0.5rem;
}

.content-area pre {
    margin: 1rem 0;
    border: 1px solid #e5e7eb;
}

.content-area code {
    font-family: 'Courier New', monospace;
    font-size: 0.9rem;
}

.content-area table {
    width: 100%;
    border-collapse: collapse;
    margin: 1rem 0;
}

.content-area th,
.content-area td {
    border: 1px solid #e5e7eb;
    padding: 0.5rem;
    text-align: left;
}

.content-area th {
    background-color: #f9fafb;
    font-weight: bold;
}
</style>
