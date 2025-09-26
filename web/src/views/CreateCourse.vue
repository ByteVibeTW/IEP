<template>
  <DefaultLayout>
    <div class="w-[90%] mx-[5%]">
      <PageTitle title="建立新課程 📚" />
      <div class="shadow-gray-500 rounded-[8px] w-[100%] self-center p-5">
        <div class="mb-6">
          <label for="course-type" class="text-[20px] font-bold mb-[10px] block"
            >課程名稱</label
          >
          <InputText
            id="course-name"
            v-model="courseName"
            placeholder="請輸入課程名稱"
            class="w-full"
          />
        </div>

        <div class="mb-6">
          <label for="course-type" class="text-[20px] font-bold mb-[10px] block"
            >課程類型</label
          >
          <AutoComplete
            v-model="courseType"
            :suggestions="filteredTypes"
            @complete="searchTypes"
            placeholder="請選擇或搜尋課程類型"
            class="w-full"
            :dropdown="true"
            forceSelection
          />
        </div>

        <div class="mb-6">
          <label for="course-type" class="text-[20px] font-bold mb-[10px] block"
            >課程簡介</label
          >
          <InputText
            id="course-intro"
            v-model="courseIntro"
            placeholder="請輸入課程簡介"
            class="w-full"
          />
        </div>

        <div class="mb-6">
          <label
            for="course-outline"
            class="text-[20px] font-bold mb-[10px] block"
            >教學大綱</label
          >
          <Editor
            v-model="courseOutline"
            id="course-outline"
            editorStyle="height: 200px"
            class="w-full"
          />
        </div>

        <div class="mb-6">
          <label for="course-type" class="text-[20px] font-bold mb-[10px] block"
            >課程封面圖片(可選)</label
          >
          <FileUpload
            name="file"
            url="http://localhost:8000/api/upload"
            @upload="onTemplatedUpload"
            :multiple="false"
            accept="image/*"
            :maxFileSize="1000000"
            @select="onSelectedFiles"
            :auto="true"
            :disabled="previewFiles.length > 0"
            class="w-full"
            :customUpload="true"
            @uploader="customUploader"
          >
            <template #header="{ chooseCallback, clearCallback, files }">
              <div
                class="flex flex-wrap justify-between items-center flex-1 gap-4"
              >
                <div class="flex gap-2">
                  <Button
                    @click="chooseCallback()"
                    icon="pi pi-images"
                    rounded
                    outlined
                    severity="secondary"
                    :disabled="previewFiles.length > 0"
                  ></Button>
                </div>
                <small v-if="previewFiles.length > 0" class="text-gray-500"
                  >已上傳一張圖片，請先移除現有圖片才能上傳新圖片</small
                >
              </div>
            </template>
            <template
              #content="{
                files,
                slotUploadedFiles,
                removeUploadedFileCallback,
                removeFileCallback,
                messages,
              }"
            >
              <div class="flex flex-col gap-8 pt-4">
                <Message
                  v-for="message of messages"
                  :key="message"
                  :class="{ 'mb-8': !files.length && !slotUploadedFiles.length }"
                  severity="error"
                >
                  {{ message }}
                </Message>

                <div
                  v-if="previewFiles.length > 0"
                  class="flex flex-wrap gap-4"
                >
                  <div
                    v-for="(file, index) of previewFiles"
                    :key="file.name + file.type + file.size"
                    class="p-4 rounded-border flex flex-col border border-surface items-center gap-4"
                  >
                    <div class="w-[200px] h-[150px] overflow-hidden">
                      <img
                        role="presentation"
                        :alt="file.name"
                        :src="file.objectURL"
                        class="w-full h-full object-cover"
                      />
                    </div>
                    <span
                      class="font-semibold text-ellipsis max-w-60 whitespace-nowrap overflow-hidden"
                      >{{ file.name }}</span
                    >
                    <Badge
                      :value="file.uploaded ? '已上傳' : '待上傳'"
                      :severity="file.uploaded ? 'success' : 'warn'"
                    />
                    <div class="flex gap-2">
                      <Button
                        icon="pi pi-times"
                        @click="
                          onRemoveTemplatingFile(
                            file,
                            removeFileCallback,
                            index,
                          )
                        "
                        outlined
                        rounded
                        severity="danger"
                      />
                    </div>
                  </div>
                </div>
              </div>
            </template>
            <template #empty>
              <div
                v-if="previewFiles.length === 0"
                class="flex items-center justify-center flex-col p-8 border-2 border-dashed border-gray-300 rounded-lg"
              >
                <i class="pi pi-cloud-upload !text-4xl !text-gray-400 mb-4" />
                <p class="text-gray-500">拖放圖片到這裡上傳</p>
              </div>
            </template>
          </FileUpload>
        </div>

        <div class="mb-6">
          <label for="course-type" class="text-[20px] font-bold mb-[10px] block"
            >課程價格 (新台幣 $TWD)</label
          >
          <InputText
            id="course-price"
            v-model="coursePrice"
            type="number"
            placeholder="請輸入課程價格"
            class="w-full"
          />
        </div>
        <Button
          label="提交審核"
          class="w-[100%] mt-4"
          @click="onSubmit"
          :disabled="!isFormValid"
        />
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup>
import axios from "axios";
import swal from "sweetalert";
import { computed, onMounted, ref } from "vue";
import PageTitle from "../components/common/PageTitle.vue";
import DefaultLayout from "../Layout/default.vue";
import Button from "primevue/button";
import InputText from "primevue/inputtext";
import AutoComplete from "primevue/autocomplete";
import FileUpload from "primevue/fileupload";
import Editor from "primevue/editor";
import Message from "primevue/message";
import Badge from "primevue/badge";
import { useAuthStore } from "../stores/auth";
import { courseTypes } from "../stores/courseType";
import { useUserStore } from "../stores/user";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

const userStore = useUserStore();
const authStore = useAuthStore();

const courseName = ref("");
const courseType = ref("");
const courseIntro = ref("");
const courseOutline = ref("");
const courseImage = ref(null);
const coursePrice = ref(0);
const filteredTypes = ref([]);
const uploadedFiles = ref([]);
const previewFiles = ref([]);

const searchTypes = (event) => {
  const query = event.query.toLowerCase();
  filteredTypes.value = courseTypes.filter((type) =>
    type.toLowerCase().includes(query),
  );
};

const isFormValid = computed(() => {
  return (
    courseName.value &&
    courseType.value &&
    courseIntro.value &&
    courseOutline.value &&
    coursePrice.value !== null &&
    coursePrice.value >= 0
  );
});

const resetForm = () => {
  courseName.value = "";
  courseType.value = "";
  courseIntro.value = "";
  courseOutline.value = "";
  courseImage.value = null;
  coursePrice.value = 0;
  uploadedFiles.value = [];
  previewFiles.value = [];
};

const submitCourse = async () => {
  const teacherId = userStore.currentUserInfo.user_id;

  const payload = {
    course_name: courseName.value,
    course_type: courseType.value,
    course_intro: courseIntro.value,
    course_outline: courseOutline.value,
    course_image: courseImage.value || "",
    course_price: Number(coursePrice.value),
    course_content: [],
    teacher_id: teacherId,
    students: [teacherId],
  };

  try {
  try {
    const response = await axios.post(`${apiBaseUrl}/api/courses/`, payload, {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${authStore.currentUser.access_token}`,
      },
    });
    swal("課程新增成功！", "", "success");
    resetForm();
  } catch (error) {
    swal("課程提交失敗！", "請稍後再試。", "error");
  }
};

const onSubmit = () => {
  if (!isFormValid.value) {
    swal("請填寫所有必要欄位！", "", "warning");
    return;
  }
  submitCourse();
};

const customUploader = async (event) => {
  const file = event.files[0];
  if (!file) return;

  const formData = new FormData();
  formData.append("file", file);

  try {
    const response = await axios.post(`${apiBaseUrl}/api/upload`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
        Authorization: `Bearer ${authStore.currentUser.access_token}`,
      },
    });

    if (response.data?.url) {
      courseImage.value = response.data.url;
      const objectURL = URL.createObjectURL(file);
      uploadedFiles.value = [
        {
          ...file,
          objectURL, // Local preview URL
          uploaded: true,
          minioUrl: response.data.url, // Store MinIO URL separately
        },
      ];
      previewFiles.value = uploadedFiles.value;
      swal("上傳成功！", "圖片已成功上傳。", "success");
    }
  } catch (error) {
    swal("檔案上傳失敗！", "請稍後再試。", "error");
    // Clear all image states on failure
    courseImage.value = null;
    uploadedFiles.value = [];
    previewFiles.value = [];
  }
};

const onTemplatedUpload = (event) => {
  const files = event.files;
  if (files?.length > 0) {
    const file = files[0];
    const objectURL = URL.createObjectURL(file);
    previewFiles.value = [
      {
        ...file,
        objectURL,
        uploaded: false,
      },
    ];
  }
};

const onSelectedFiles = (event) => {
  const files = event.files;
  if (files?.length > 0) {
    const file = files[0];
    if (file) {
      const objectURL = URL.createObjectURL(file);
      previewFiles.value = [
        {
          ...file,
          objectURL,
          uploaded: false,
        },
      ];
      ];
      // 自動觸發上傳
      customUploader({ files: [file] });
    }
  }
};

const onRemoveTemplatingFile = (file, removeFileCallback, index) => {
  removeFileCallback(index);
  if (file.objectURL) {
    URL.revokeObjectURL(file.objectURL);
  }
  courseImage.value = null;
  uploadedFiles.value = [];
  previewFiles.value = [];
};

onMounted(() => {
  authStore.checkAuth();
  userStore.fetchUser();
});
</script>
