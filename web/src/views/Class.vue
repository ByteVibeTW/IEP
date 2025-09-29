<template>
  <DefaultLayout>
    <Container custom-class="pb-4">
      <PageTitle title="課程內容" :show-back-button="true" back-route="/MyCourse" />
      <div v-if="courseStore.currentClass && userStore.currentUserInfo.user_id === courseStore.currentClass.teacher_id"
        class="flex justify-end">
        <Button :class="[showNewChapter ? 'mb-0 mt-2' : 'mb-5 mt-2']" @click="toggleNewChapter">
          {{ showNewChapter ? '新增課程章節 🔼' : '新增課程章節 🔽' }}
        </Button>
      </div>
      <div v-if="showNewChapter" class="mb-8 bg-white rounded-2xl shadow p-4">
        <Input id="new-chapter" v-model="newChapter" label="章節名稱" placeholder="輸入章節名稱" />
        <Button variant="primary" full-width @click="addNewChapter"> 新增章節 </Button>
      </div>
      <div class="mt-6">
        <ChapterManager v-for="(week, index) in assignments" :key="week.chapter" :chapter="{
          title: week.chapter,
          items: week.items,
        }" :show-delete-button="courseStore.currentClass && userStore.currentUserInfo.user_id === courseStore.currentClass.teacher_id
          " @delete="removeChapter(index)" @delete-item="(itemIndex) => removeItem(index, itemIndex)">
          <Button
            v-if="courseStore.currentClass && userStore.currentUserInfo.user_id === courseStore.currentClass.teacher_id"
            variant="success" size="sm" full-width class="mt-4" @click="toggleFileEditor(index)">
            {{ showFileEditor[index] ? '新增課程內容 ➖' : '新增課程內容 ➕' }}
          </Button>
          <ContentEditor v-if="showFileEditor[index]" @save="(content) => addContent(index, content)" />
        </ChapterManager>
      </div>
    </Container>
  </DefaultLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useUserStore } from '../stores/user';
import { useCourseStore } from '../stores/course';
import swal from 'sweetalert';
import Button from '../components/common/Button.vue';
import FileUpload from '../components/common/FileUpload.vue';
import Input from '../components/common/Input.vue';
import PageTitle from '../components/common/PageTitle.vue';
import ChapterManager from '../components/course/ChapterManager.vue';
import ContentEditor from '../components/course/ContentEditor.vue';
import Container from '../components/common/Container.vue';
import DefaultLayout from '../Layout/default.vue';

const userStore = useUserStore();
const courseStore = useCourseStore();

// 課程章節資料
const courseChapters = {
  'course_001': [ // Vue.js 前端開發實戰
    {
      chapter: '第一章：Vue.js 基礎概念',
      items: [
        { name: 'Vue.js 簡介與環境搭建', type: 'ppt' },
        { name: '模板語法與指令', type: 'ppt' },
        { name: '實作練習：Hello Vue', type: 'doc' },
      ],
    },
    {
      chapter: '第二章：組件開發與通信',
      items: [
        { name: '組件基礎概念', type: 'ppt' },
        { name: 'Props 與 Events', type: 'ppt' },
        { name: '組件通信方式', type: 'doc' },
        { name: '實作練習：Todo List', type: 'doc' },
      ],
    },
    {
      chapter: '第三章：狀態管理與路由',
      items: [
        { name: 'Vuex 狀態管理', type: 'ppt' },
        { name: 'Vue Router 路由配置', type: 'ppt' },
        { name: '實作練習：購物車應用', type: 'doc' },
      ],
    },
    {
      chapter: '第四章：進階主題',
      items: [
        { name: 'Composition API', type: 'ppt' },
        { name: '自定義指令與插件', type: 'ppt' },
        { name: '效能優化技巧', type: 'doc' },
      ],
    },
    {
      chapter: '第五章：實戰專案',
      items: [
        { name: '專案架構設計', type: 'ppt' },
        { name: 'API 整合與測試', type: 'doc' },
        { name: '部署與上線', type: 'doc' },
      ],
    },
  ],
  'course_002': [ // Spring Boot 後端開發
    {
      chapter: '第一章：Spring Boot 基礎',
      items: [
        { name: 'Spring Boot 簡介與環境配置', type: 'ppt' },
        { name: '專案結構與配置檔案', type: 'ppt' },
        { name: '實作練習：第一個 Spring Boot 應用', type: 'doc' },
      ],
    },
    {
      chapter: '第二章：RESTful API 設計',
      items: [
        { name: 'RESTful 架構原則', type: 'ppt' },
        { name: 'Controller 與 RequestMapping', type: 'ppt' },
        { name: 'HTTP 方法與狀態碼', type: 'doc' },
        { name: '實作練習：使用者管理 API', type: 'doc' },
      ],
    },
    {
      chapter: '第三章：資料庫整合',
      items: [
        { name: 'JPA 與 Hibernate 基礎', type: 'ppt' },
        { name: 'Entity 與 Repository', type: 'ppt' },
        { name: '資料庫連線配置', type: 'doc' },
      ],
    },
    {
      chapter: '第四章：安全認證',
      items: [
        { name: 'Spring Security 基礎', type: 'ppt' },
        { name: 'JWT Token 實作', type: 'ppt' },
        { name: '權限控制與角色管理', type: 'doc' },
      ],
    },
    {
      chapter: '第五章：微服務架構',
      items: [
        { name: '微服務設計原則', type: 'ppt' },
        { name: '服務間通信', type: 'doc' },
        { name: '容器化部署', type: 'doc' },
      ],
    },
  ],
  'course_003': [ // 資料庫設計與優化
    {
      chapter: '第一章：資料庫設計基礎',
      items: [
        { name: '關聯式資料庫概念', type: 'ppt' },
        { name: 'ER 圖設計', type: 'ppt' },
        { name: '實作練習：圖書館管理系統設計', type: 'doc' },
      ],
    },
    {
      chapter: '第二章：正規化理論',
      items: [
        { name: '第一正規化 (1NF)', type: 'ppt' },
        { name: '第二正規化 (2NF)', type: 'ppt' },
        { name: '第三正規化 (3NF)', type: 'doc' },
        { name: '實作練習：正規化案例', type: 'doc' },
      ],
    },
    {
      chapter: '第三章：索引設計與優化',
      items: [
        { name: '索引類型與原理', type: 'ppt' },
        { name: '索引設計策略', type: 'ppt' },
        { name: '查詢效能分析', type: 'doc' },
      ],
    },
    {
      chapter: '第四章：SQL 查詢優化',
      items: [
        { name: '查詢執行計畫', type: 'ppt' },
        { name: 'SQL 調校技巧', type: 'ppt' },
        { name: '效能監控工具', type: 'doc' },
      ],
    },
    {
      chapter: '第五章：進階主題',
      items: [
        { name: '分散式資料庫架構', type: 'ppt' },
        { name: '資料備份與復原', type: 'doc' },
        { name: '資料庫安全', type: 'doc' },
      ],
    },
  ],
};

// 動態獲取當前課程的章節資料
const assignments = ref([]);

// 載入課程章節資料
const loadCourseChapters = () => {
  if (courseStore.currentClass && courseStore.currentClass.course_id) {
    const courseId = courseStore.currentClass.course_id;
    assignments.value = courseChapters[courseId] || [];
  } else {
    // 預設載入第一個課程的章節
    assignments.value = courseChapters['course_001'] || [];
  }

  // 重新初始化展開狀態
  showFileSubmission.value = assignments.value.map(() => false);
  showFileEditor.value = assignments.value.map(() => false);
};

// 展開狀態
const showFileSubmission = ref([]);
const showFileEditor = ref([]);
const showNewChapter = ref(false);
const newChapter = ref('');

// 展開控制
const toggleFileSubmission = (index) => {
  showFileSubmission.value[index] = !showFileSubmission.value[index];
};

const toggleFileEditor = (index) => {
  showFileEditor.value[index] = !showFileEditor.value[index];
};

const toggleNewChapter = () => {
  showNewChapter.value = !showNewChapter.value;
};

// 新增內容
const addContent = (index, content) => {
  assignments.value[index].items.push(content);
};

// 刪除檔案
const removeItem = (weekIndex, itemIndex) => {
  assignments.value[weekIndex].items.splice(itemIndex, 1);
};

// 刪除章節
const removeChapter = (index) => {
  swal('確定要刪除這個章節嗎？', {
    buttons: {
      cancel: '取消',
      confirm: {
        text: '刪除',
        value: 'delete',
      },
    },
  }).then((value) => {
    if (value === 'delete') {
      assignments.value.splice(index, 1);
      showFileSubmission.value.splice(index, 1);
      showFileEditor.value.splice(index, 1);
    }
  });
};

const addNewChapter = () => {
  if (!newChapter.value) {
    swal('章節名稱不能為空！', '', 'warning');
    return;
  }
  assignments.value.push({
    chapter: newChapter.value,
    items: [],
  });
  showFileSubmission.value.push(false);
  showFileEditor.value.push(false);
  newChapter.value = '';
  showNewChapter.value = false;
};

const handleHomeworkUpload = (file) => {
  // 處理作業上傳邏輯
};

const toggleReviewPanel = (index) => {
  // 處理查看檔案與評分邏輯
};

// 組件掛載時載入課程章節
onMounted(() => {
  loadCourseChapters();
});
</script>
