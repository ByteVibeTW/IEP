<template>
  <DefaultLayout>
    <div class="w-[90%] mx-[5%] pb-4">
      <PageTitle title="課程內容" :show-back-button="true" back-route="/MyCourse" />
      <div
        v-if="userStore.currentUserInfo.user_id === courseStore.currentClass.teacher_id"
        class="flex justify-end"
      >
        <Button :class="[showNewChapter ? 'mb-0 mt-2' : 'mb-5 mt-2']" @click="toggleNewChapter">
          {{ showNewChapter ? '新增課程章節 🔼' : '新增課程章節 🔽' }}
        </Button>
      </div>
      <div v-if="showNewChapter" class="mb-8 bg-white rounded-2xl shadow p-4">
        <Input id="new-chapter" v-model="newChapter" label="章節名稱" placeholder="輸入章節名稱" />
        <Button variant="primary" full-width @click="addNewChapter"> 新增章節 </Button>
      </div>
      <ChapterManager
        v-for="(week, index) in assignments"
        :key="week.chapter"
        :chapter="{
          title: week.chapter,
          items: week.items,
        }"
        :show-delete-button="
          userStore.currentUserInfo.user_id === courseStore.currentClass.teacher_id
        "
        @delete="removeChapter(index)"
        @delete-item="(itemIndex) => removeItem(index, itemIndex)"
      >
        <Button
          v-if="userStore.currentUserInfo.user_id === courseStore.currentClass.teacher_id"
          variant="success"
          size="sm"
          full-width
          class="mt-4"
          @click="toggleFileEditor(index)"
        >
          {{ showFileEditor[index] ? '新增課程內容 ➖' : '新增課程內容 ➕' }}
        </Button>
        <ContentEditor
          v-if="showFileEditor[index]"
          @save="(content) => addContent(index, content)"
        />
        <script setup>
          import { ref, onMounted } from 'vue';
          import { useUserStore } from '../stores/user';
          import { useCourseStore } from '../stores/course';
          import { useAuthStore } from '../stores/auth';
          import swal from 'sweetalert';
          import Button from '../components/common/Button.vue';
          import FileUpload from '../components/common/FileUpload.vue';
          import Input from '../components/common/Input.vue';
          import PageTitle from '../components/common/PageTitle.vue';
          import ChapterManager from '../components/course/ChapterManager.vue';
          import ContentEditor from '../components/course/ContentEditor.vue';
          import DefaultLayout from '../Layout/default.vue';

          const userStore = useUserStore();
          const courseStore = useCourseStore();
          const authStore = useAuthStore();

          // 課程資料
          const assignments = ref([
            {
              chapter: '02月24日 - 03月2日',
              items: [
                { name: 'PPT文件1', type: 'ppt' },
                { name: 'PPT文件2', type: 'ppt' },
              ],
            },
            {
              chapter: '03月3日 - 03月9日',
              items: [
                { name: 'PPT文件3', type: 'ppt' },
                { name: 'PPT文件4', type: 'ppt' },
                { name: 'Excel1', type: 'excel' },
                { name: 'Word1', type: 'doc' },
              ],
            },
            {
              chapter: '03月10日 - 03月16日',
              items: [
                { name: 'Word2', type: 'doc' },
                { name: 'PPT文件5', type: 'ppt' },
              ],
            },
          ]);

          // 展開狀態
          const showFileSubmission = ref(assignments.value.map(() => false));
          const showFileEditor = ref(assignments.value.map(() => false));
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

          onMounted(() => {
            authStore.checkAuth();
          });
        </script>
      </ChapterManager>
    </div>
  </DefaultLayout>
</template>
