<script setup lang="ts">
import type { ChapterDto } from '@/api/model/chapterDto';
import type { SectionWithChaptersDto } from '@/api/model/sectionWithChaptersDto';
import Accordion from 'primevue/accordion';
import AccordionContent from 'primevue/accordioncontent';
import AccordionHeader from 'primevue/accordionheader';
import AccordionPanel from 'primevue/accordionpanel';
import Button from 'primevue/button';
import Card from 'primevue/card';
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import Textarea from 'primevue/textarea';
import { ref, watch } from 'vue';

interface Props {
  visible: boolean;
  sections: SectionWithChaptersDto[];
}

const props = defineProps<Props>();

const emit = defineEmits<{
  (event: 'update:visible', value: boolean): void;
  (event: 'save', sections: SectionWithChaptersDto[]): void;
}>();

// 編輯用的本地數據
const editingSections = ref<SectionWithChaptersDto[]>([]);

// 當 dialog 打開時，複製數據到編輯狀態
watch(
  () => props.visible,
  (newVisible) => {
    if (newVisible) {
      editingSections.value = JSON.parse(JSON.stringify(props.sections || []));
    }
  }
);

const handleClose = () => {
  emit('update:visible', false);
};

const handleSave = () => {
  emit('save', editingSections.value);
  handleClose();
};

// 新增章節
const addNewSection = () => {
  const newSection: SectionWithChaptersDto = {
    id: Date.now(), // 臨時 ID，實際應該由後端生成
    sectionName: '新章節',
    description: '',
    orderIndex: editingSections.value.length,
    chapters: [],
  };
  editingSections.value.push(newSection);
};

// 刪除章節
const deleteSection = (index: number) => {
  editingSections.value.splice(index, 1);
};

// 新增子章節
const addNewChapter = (sectionIndex: number) => {
  const section = editingSections.value[sectionIndex];
  if (!section) return;

  if (!section.chapters) {
    section.chapters = [];
  }

  const newChapter: ChapterDto = {
    id: Date.now() + Math.random(), // 臨時 ID
    chapterName: '新子章節',
    orderIndex: section.chapters.length,
  };

  section.chapters.push(newChapter);
};

// 刪除子章節
const deleteChapter = (sectionIndex: number, chapterIndex: number) => {
  const section = editingSections.value[sectionIndex];
  if (!section || !section.chapters) return;

  section.chapters.splice(chapterIndex, 1);
};
</script>

<template>
  <Dialog
    :visible="visible"
    @update:visible="handleClose"
    header="📝 編輯課程內容"
    :style="{ width: '80vw', maxWidth: '1000px' }"
    :breakpoints="{ '960px': '90vw', '640px': '95vw' }"
    modal
    maximizable
  >
    <div class="space-y-6">
      <!-- 新增章節按鈕 -->
      <div class="flex justify-between items-center">
        <h3 class="text-lg font-semibold text-gray-800">課程章節管理</h3>
        <Button
          icon="pi pi-plus"
          label="新增章節"
          @click="addNewSection"
          class="bg-blue-600 hover:bg-blue-700"
        />
      </div>

      <!-- 章節編輯區域 -->
      <div v-if="editingSections.length > 0" class="space-y-4">
        <Accordion :multiple="true" class="w-full">
          <AccordionPanel
            v-for="(section, sectionIndex) in editingSections"
            :key="section.id"
            :value="String(section.id)"
            class="mb-4"
          >
            <AccordionHeader>
              <div class="flex items-center justify-between w-full">
                <div class="flex items-center">
                  <i class="pi pi-folder-open mr-3 text-orange-500"></i>
                  <span class="font-medium">{{ section.sectionName || '未命名章節' }}</span>
                </div>
                <Button
                  icon="pi pi-trash"
                  severity="danger"
                  text
                  size="small"
                  @click.stop="deleteSection(sectionIndex)"
                  class="ml-2"
                />
              </div>
            </AccordionHeader>

            <AccordionContent>
              <div class="space-y-4">
                <!-- 章節基本資訊編輯 -->
                <Card>
                  <template #title>
                    <span class="text-sm text-gray-600">章節資訊</span>
                  </template>
                  <template #content>
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">章節名稱</label>
                        <InputText
                          v-model="section.sectionName"
                          placeholder="請輸入章節名稱"
                          class="w-full"
                        />
                      </div>
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">章節描述</label>
                        <InputText
                          v-model="section.description"
                          placeholder="請輸入章節描述"
                          class="w-full"
                        />
                      </div>
                    </div>
                  </template>
                </Card>

                <!-- 子章節管理 -->
                <Card>
                  <template #title>
                    <div class="flex justify-between items-center">
                      <span class="text-sm text-gray-600">子章節管理</span>
                      <Button
                        icon="pi pi-plus"
                        label="新增子章節"
                        size="small"
                        @click="addNewChapter(sectionIndex)"
                        class="bg-green-600 hover:bg-green-700"
                      />
                    </div>
                  </template>
                  <template #content>
                    <div v-if="section.chapters && section.chapters.length > 0" class="space-y-3">
                      <div
                        v-for="(chapter, chapterIndex) in section.chapters"
                        :key="chapter.id"
                        class="border border-gray-200 rounded-lg p-4 bg-gray-50"
                      >
                        <div class="flex items-center justify-between mb-3">
                          <h5 class="font-medium text-gray-800">子章節 {{ chapterIndex + 1 }}</h5>
                          <Button
                            icon="pi pi-trash"
                            severity="danger"
                            text
                            size="small"
                            @click="deleteChapter(sectionIndex, chapterIndex)"
                          />
                        </div>
                        <div class="grid grid-cols-1 gap-3">
                          <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1"
                              >子章節名稱</label
                            >
                            <InputText
                              v-model="chapter.chapterName"
                              placeholder="請輸入子章節名稱"
                              class="w-full"
                            />
                          </div>
                          <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1"
                              >章節內容</label
                            >
                            <Textarea
                              v-model="chapter.content"
                              placeholder="請輸入章節內容"
                              rows="3"
                              class="w-full"
                            />
                          </div>
                        </div>
                      </div>
                    </div>
                    <div v-else class="text-center py-8 text-gray-500">
                      <i class="pi pi-inbox text-2xl mb-2"></i>
                      <p>尚未新增子章節</p>
                    </div>
                  </template>
                </Card>
              </div>
            </AccordionContent>
          </AccordionPanel>
        </Accordion>
      </div>

      <!-- 空狀態 -->
      <div v-else class="text-center py-12 text-gray-500">
        <i class="pi pi-folder-open text-4xl mb-4"></i>
        <h3 class="text-lg font-medium mb-2">尚無課程章節</h3>
        <p class="mb-4">開始建立您的第一個課程章節</p>
      </div>
    </div>

    <template #footer>
      <div class="flex justify-end gap-3">
        <Button label="取消" icon="pi pi-times" @click="handleClose" severity="secondary" />
        <Button
          label="儲存變更"
          icon="pi pi-check"
          @click="handleSave"
          class="bg-blue-600 hover:bg-blue-700"
        />
      </div>
    </template>
  </Dialog>
</template>

<style scoped>
/* Dialog 內容樣式 */
:deep(.p-dialog-content) {
  padding: 1.5rem;
}

:deep(.p-accordion-header-link) {
  padding: 1rem;
  border-radius: 0.5rem;
}

:deep(.p-accordion-content) {
  padding: 1rem;
}

:deep(.p-accordionpanel) {
  margin-bottom: 1rem;
  border-radius: 0.5rem;
  overflow: hidden;
}
</style>
