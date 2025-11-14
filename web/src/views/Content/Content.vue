<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useGetChapterById, useGenerateChapter } from '@/api/api';
import Toast from 'primevue/toast';
import { useToast } from 'primevue/usetoast';
import Skeleton from 'primevue/skeleton';
import Divider from 'primevue/divider';
import VueMarkdown from 'vue-markdown-render';

const route = useRoute();
const router = useRouter();
const toast = useToast();

// 章節ID（從路由參數獲取）
const chapterId = ref<number | null>(null);
// 課程ID（用於返回）
const courseId = ref<number | null>(null);
// 從 sessionStorage 獲取的舊資料（兼容舊路由）
const legacyContentData = ref<any>(null);
// 是否正在生成內容
const isGenerating = ref(false);
// 是否已經嘗試過生成（避免重複生成）
const hasTriedGenerate = ref(false);
// 輪詢定時器
let pollInterval: ReturnType<typeof setInterval> | null = null;

// 判斷是否使用路由參數方式
const isRouteParamMode = computed(() => {
  return route.params.id !== undefined && route.params.id !== null;
});

// 獲取章節詳細資訊（僅在路由參數模式下使用）
// 創建一個 computed 來處理 null 值，當為 null 時返回 0（但查詢會被 disabled）
const chapterIdForQuery = computed(() => chapterId.value ?? 0);
const { data: chapterData, isLoading, error, refetch } = useGetChapterById(
  chapterIdForQuery,
  {
    query: {
      enabled: computed(() => {
        const enabled = isRouteParamMode.value && chapterId.value !== null && chapterId.value > 0;
        console.log('查詢啟用狀態:', {
          isRouteParamMode: isRouteParamMode.value,
          chapterId: chapterId.value,
          enabled,
        });
        return enabled;
      }),
    },
  }
);

// 計算章節標題
const chapterTitle = computed(() => {
  if (isRouteParamMode.value) {
    return chapterData.value?.chapterName || '載入中...';
  } else {
    return legacyContentData.value?.itemName || '內容';
  }
});

// 計算章節內容
const chapterContent = computed(() => {
  if (isRouteParamMode.value) {
    const content = chapterData.value?.content;
    console.log('章節數據:', {
      chapterData: chapterData.value,
      content,
      hasContent: !!content,
    });
    return content || '';
  } else {
    return legacyContentData.value?.content?.content || '';
  }
});

// 計算課程名稱（用於顯示）
const courseName = computed(() => {
  if (isRouteParamMode.value) {
    // 可以從 sessionStorage 或其他地方獲取課程名稱
    const storedCourseInfo = sessionStorage.getItem('currentCourseInfo');
    if (storedCourseInfo) {
      try {
        const courseInfo = JSON.parse(storedCourseInfo);
        return courseInfo.courseName || '';
      } catch (e) {
        console.error('解析課程資訊失敗:', e);
      }
    }
    return '';
  } else {
    return legacyContentData.value?.courseName || '';
  }
});

// 返回課程頁面
const goBack = () => {
  if (isRouteParamMode.value) {
    // 從 sessionStorage 獲取課程ID，如果沒有則返回課程列表
    const storedCourseInfo = sessionStorage.getItem('currentCourseInfo');
    if (storedCourseInfo) {
      try {
        const courseInfo = JSON.parse(storedCourseInfo);
        if (courseInfo.courseId) {
          router.push(`/Classroom/${courseInfo.courseId}`);
          return;
        }
      } catch (e) {
        console.error('解析課程資訊失敗:', e);
      }
    }
    // 如果沒有課程ID，嘗試從路由歷史獲取
    router.back();
  } else {
    // 舊路由方式，返回課程頁面
    const courseId = legacyContentData.value?.courseId;
    if (courseId) {
      router.push(`/Classroom/${courseId}`);
    } else {
      router.push('/MyCourse');
    }
  }
};

// 生成章節內容的 mutation
const generateChapterMutation = useGenerateChapter({
  mutation: {
    onSuccess: () => {
      toast.add({
        severity: 'success',
        summary: '成功',
        detail: '章節內容生成中，請稍候...',
        life: 3000,
      });
      // 輪詢檢查內容是否已生成（最多嘗試 10 次，每次間隔 3 秒）
      let pollCount = 0;
      const maxPollCount = 10;
      pollInterval = setInterval(() => {
        pollCount++;
        refetch().then(() => {
          // 如果已經有內容了，停止輪詢
          if (chapterData.value?.content && chapterData.value.content.trim() !== '') {
            if (pollInterval) {
              clearInterval(pollInterval);
              pollInterval = null;
            }
            isGenerating.value = false;
            toast.add({
              severity: 'success',
              summary: '成功',
              detail: '章節內容已生成',
              life: 3000,
            });
          } else if (pollCount >= maxPollCount) {
            // 達到最大輪詢次數，停止輪詢
            if (pollInterval) {
              clearInterval(pollInterval);
              pollInterval = null;
            }
            isGenerating.value = false;
            toast.add({
              severity: 'warn',
              summary: '提示',
              detail: '內容生成時間較長，請稍後刷新頁面查看',
              life: 5000,
            });
          }
        });
      }, 3000);
    },
    onError: (error) => {
      console.error('生成章節失敗:', error);
      toast.add({
        severity: 'error',
        summary: '錯誤',
        detail: '生成章節內容失敗，請稍後再試',
        life: 3000,
      });
      isGenerating.value = false;
    },
  },
});

// 判斷是否為 AI 課程
const isAICourse = computed(() => {
  if (!isRouteParamMode.value) {
    return false;
  }
  const storedCourseInfo = sessionStorage.getItem('currentCourseInfo');
  console.log('檢查 AI 課程:', {
    storedCourseInfo,
    hasStorage: !!storedCourseInfo,
  });
  if (storedCourseInfo) {
    try {
      const courseInfo = JSON.parse(storedCourseInfo);
      console.log('課程資訊:', courseInfo);
      const isAI = courseInfo.type === 'AI Tutor';
      console.log('是否為 AI 課程:', isAI);
      return isAI;
    } catch (e) {
      console.error('解析課程資訊失敗:', e);
    }
  }
  return false;
});

// 自動生成章節內容（當是 AI 課程且沒有內容時）
watch(
  [() => chapterData.value, () => isAICourse.value, () => isLoading.value],
  ([chapter, isAI, loading]) => {
    console.log('Watch 觸發:', {
      isRouteParamMode: isRouteParamMode.value,
      loading,
      hasTriedGenerate: hasTriedGenerate.value,
      isGenerating: isGenerating.value,
      isAI,
      chapter: chapter ? { id: chapter.id, chapterName: chapter.chapterName, hasContent: !!chapter.content, contentLength: chapter.content?.length || 0 } : null,
    });

    // 只在路由參數模式下且已載入完成時檢查
    if (!isRouteParamMode.value || loading || hasTriedGenerate.value || isGenerating.value) {
      console.log('Watch 條件不滿足，跳過:', {
        isRouteParamMode: isRouteParamMode.value,
        loading,
        hasTriedGenerate: hasTriedGenerate.value,
        isGenerating: isGenerating.value,
      });
      return;
    }

    // 確保章節資料已載入
    if (!chapter) {
      console.log('章節資料尚未載入');
      return;
    }

    // 檢查是否為 AI 課程
    if (!isAI) {
      console.log('不是 AI 課程，跳過生成');
      return;
    }

    // 檢查章節是否有內容
    const hasContent = chapter.content && chapter.content.trim() !== '';
    if (hasContent) {
      console.log('章節已有內容，無需生成');
      return;
    }

    // 所有條件滿足，開始生成
    console.log('開始生成章節內容，章節ID:', chapter.id);
    hasTriedGenerate.value = true;
    isGenerating.value = true;

    // 獲取課程資訊
    const storedCourseInfo = sessionStorage.getItem('currentCourseInfo');
    let courseName = '';
    let intro = '';
    let sectionName = '';

    if (storedCourseInfo) {
      try {
        const courseInfo = JSON.parse(storedCourseInfo);
        courseName = courseInfo.courseName || '';
        intro = courseInfo.intro || '';
        console.log('生成章節使用的課程資訊:', { courseName, intro });
      } catch (e) {
        console.error('解析課程資訊失敗:', e);
      }
    }

    // 調用生成 API
    console.log('調用生成 API，參數:', {
      chapterId: chapter.id,
      chapterName: chapter.chapterName || '',
      courseName: courseName,
      intro: intro,
      sectionName: sectionName,
    });

    generateChapterMutation.mutate({
      data: {
        chapterId: chapter.id,
        chapterName: chapter.chapterName || '',
        courseName: courseName,
        intro: intro,
        sectionName: sectionName,
      },
    });

    toast.add({
      severity: 'info',
      summary: '提示',
      detail: '正在生成章節內容，請稍候...',
      life: 5000,
    });
  },
  { immediate: true, deep: true }
);

// 初始化
onMounted(() => {
  if (isRouteParamMode.value) {
    // 從路由參數獲取章節ID
    const id = route.params.id as string;
    console.log('路由參數 ID:', id);
    if (id) {
      const parsedId = parseInt(id, 10);
      if (isNaN(parsedId)) {
        toast.add({
          severity: 'error',
          summary: '錯誤',
          detail: '無效的章節ID格式',
          life: 3000,
        });
        router.push('/MyCourse');
        return;
      }
      chapterId.value = parsedId;
      console.log('設置章節ID:', chapterId.value);

      // 嘗試從 sessionStorage 獲取課程資訊（如果有的話）
      const storedCourseInfo = sessionStorage.getItem('currentCourseInfo');
      if (storedCourseInfo) {
        try {
          const courseInfo = JSON.parse(storedCourseInfo);
          courseId.value = courseInfo.courseId;
          console.log('從 sessionStorage 獲取課程資訊:', courseInfo);
          console.log('課程類型:', courseInfo.type);
        } catch (e) {
          console.error('解析課程資訊失敗:', e);
        }
      } else {
        console.warn('sessionStorage 中沒有 currentCourseInfo');
      }
    } else {
      toast.add({
        severity: 'error',
        summary: '錯誤',
        detail: '無效的章節ID',
        life: 3000,
      });
      router.push('/MyCourse');
    }
  } else {
    // 舊路由方式：從 sessionStorage 獲取內容資料
    const storedContent = sessionStorage.getItem('currentContent');
    if (storedContent) {
      try {
        legacyContentData.value = JSON.parse(storedContent);
        courseId.value = legacyContentData.value?.courseId;
      } catch (e) {
        console.error('解析內容資料失敗:', e);
        toast.add({
          severity: 'error',
          summary: '錯誤',
          detail: '無法載入內容資料',
          life: 3000,
        });
        router.push('/MyCourse');
      }
    } else {
      toast.add({
        severity: 'warn',
        summary: '提示',
        detail: '找不到內容資料，將返回課程列表',
        life: 3000,
      });
      router.push('/MyCourse');
    }
  }
});

// 組件卸載時清理輪詢定時器
onUnmounted(() => {
  if (pollInterval) {
    clearInterval(pollInterval);
    pollInterval = null;
  }
});
</script>

<template>
  <div class="min-h-screen bg-gray-50 mt-20 rounded-[8px]">
    <!-- Toast 通知組件 -->
    <Toast />

    <!-- 頁面標題 -->
    <div class="px-20 pt-10 w-[100%]">
      <div class="flex items-center justify-between">
        <span class="text-[24px] font-bold h-fit">
          {{ chapterTitle }}
        </span>
        <button @click="goBack" class="flex items-center px-4 py-2 text-gray-700 hover:text-gray-900 transition-colors">
          <i class="pi pi-arrow-left mr-2"></i>
          返回
        </button>
      </div>
      <Divider class="my-4" />
    </div>

    <div class="max-w-4xl mx-auto px-6 pb-8">
      <!-- 載入中狀態 -->

      <div v-if="(isLoading || isGenerating) && isRouteParamMode">
        <div v-if="isGenerating" class="text-center mt-6">
          <i class="pi pi-spin pi-spinner text-2xl text-blue-500 mb-2"></i>
          <p class="text-gray-600">正在生成章節內容，請稍候...</p>
        </div>
        <div class="space-y-4">
          <Skeleton width="100%" height="3rem" />
          <Skeleton width="60%" height="1.5rem" />
          <Skeleton width="100%" height="20rem" />
          <Skeleton width="100%" height="20rem" />
        </div>
      </div>

      <!-- 錯誤狀態 -->
      <div v-else-if="error && isRouteParamMode" class="bg-white rounded-2xl shadow p-8">
        <div class="text-center py-12">
          <i class="pi pi-exclamation-triangle text-6xl text-red-500 mb-4"></i>
          <h3 class="text-xl font-semibold text-gray-800 mb-2">載入章節失敗</h3>
          <p class="text-gray-600 mb-6">無法載入章節內容，請稍後再試。</p>
          <button @click="() => refetch()"
            class="px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors duration-200 mr-4">
            重新載入
          </button>
          <button @click="goBack"
            class="px-6 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-colors duration-200">
            返回課程
          </button>
        </div>
      </div>

      <!-- 內容區域 -->
      <div v-else-if="chapterContent || (!isRouteParamMode && legacyContentData)">
        <!-- Markdown 內容 -->
        <div v-if="chapterContent" class="markdown-content">
          <VueMarkdown :source="chapterContent" />
        </div>

        <!-- 無內容狀態 -->
        <div v-else class="text-center py-12 text-gray-500">
          <i class="pi pi-file text-6xl mb-4 text-gray-400"></i>
          <h3 class="text-lg font-medium mb-2">尚無內容</h3>
          <p>此章節目前沒有內容，請稍後再查看。</p>
        </div>
      </div>

      <!-- 無資料狀態 -->
      <div v-else class="text-center py-12 text-gray-500">
        <i class="pi pi-inbox text-6xl mb-4 text-gray-400"></i>
        <h3 class="text-lg font-medium mb-2">找不到內容</h3>
        <p class="mb-6">無法找到對應的章節內容。</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* Markdown 內容樣式 */
.markdown-content :deep(h1) {
  font-size: 1.875rem;
  line-height: 2.25rem;
  font-weight: 700;
  margin-bottom: 1rem;
  margin-top: 1.5rem;
  color: #111827;
}

.markdown-content :deep(h2) {
  font-size: 1.5rem;
  line-height: 2rem;
  font-weight: 700;
  margin-bottom: 0.75rem;
  margin-top: 1.25rem;
  color: #1f2937;
}

.markdown-content :deep(h3) {
  font-size: 1.25rem;
  line-height: 1.75rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
  margin-top: 1rem;
  color: #374151;
}

.markdown-content :deep(h4) {
  font-size: 1.125rem;
  line-height: 1.75rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
  margin-top: 0.75rem;
  color: #374151;
}

.markdown-content :deep(p) {
  margin-bottom: 1rem;
  color: #374151;
  line-height: 1.625;
}

.markdown-content :deep(ul),
.markdown-content :deep(ol) {
  margin-bottom: 1rem;
  margin-left: 1.5rem;
}

.markdown-content :deep(ul) {
  list-style-type: disc;
}

.markdown-content :deep(ol) {
  list-style-type: decimal;
}

.markdown-content :deep(li) {
  margin-bottom: 0.5rem;
  color: #374151;
}

.markdown-content :deep(blockquote) {
  border-left: 4px solid #d1d5db;
  padding-left: 1rem;
  font-style: italic;
  margin: 1rem 0;
  color: #4b5563;
  background-color: #f9fafb;
  padding: 1rem;
  border-radius: 0.25rem;
}

.markdown-content :deep(a) {
  color: #2563eb;
  text-decoration: underline;
}

.markdown-content :deep(a:hover) {
  color: #1e40af;
}

.markdown-content :deep(strong) {
  font-weight: 700;
  color: #111827;
}

.markdown-content :deep(em) {
  font-style: italic;
}

.markdown-content :deep(code) {
  background-color: #f3f4f6;
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
  font-size: 0.875rem;
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, 'Liberation Mono', 'Courier New', monospace;
  color: #1f2937;
}

.markdown-content :deep(pre) {
  background-color: #f3f4f6;
  padding: 1rem;
  border-radius: 0.5rem;
  overflow-x: auto;
  margin-bottom: 1rem;
  border: 1px solid #e5e7eb;
}

.markdown-content :deep(pre code) {
  background-color: transparent;
  padding: 0;
  border: none;
}

.markdown-content :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin: 1rem 0;
}

.markdown-content :deep(th),
.markdown-content :deep(td) {
  border: 1px solid #e5e7eb;
  padding: 0.75rem;
  text-align: left;
}

.markdown-content :deep(th) {
  background-color: #f9fafb;
  font-weight: 700;
}

.markdown-content :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 0.5rem;
  margin: 1rem 0;
}

.markdown-content :deep(hr) {
  border: none;
  border-top: 2px solid #e5e7eb;
  margin: 2rem 0;
}
</style>
