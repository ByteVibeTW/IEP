<template>
  <div class="mt-4 p-4 bg-gray-100 border border-green-300 rounded-xl">
    <div class="mb-2">
      <label class="block text-gray-600 mb-1">檔案名稱：</label>
      <input
        v-model="content.name"
        type="text"
        class="w-full border rounded px-2 py-1"
        placeholder="教學報告"
      />
    </div>
    <div class="mb-2">
      <label class="block text-gray-600 mb-1">類型：</label>
      <select v-model="content.type" class="w-full border rounded px-2 py-1">
        <option value="ppt">PPT</option>
        <option value="excel">Excel</option>
        <option value="doc">Word</option>
        <option value="video">Video</option>
      </select>
    </div>
    <Button variant="primary" @click="handleSave" :disabled="!isValid">
      儲存內容
    </Button>
  </div>
</template>

<script setup>
import { computed, ref } from "vue";
import Button from "../common/Button.vue";

const content = ref({
  name: "",
  type: "ppt",
});

const isValid = computed(() => {
  return content.value.name.trim() !== "" && content.value.type !== "";
});

const emit = defineEmits(["save"]);

const handleSave = () => {
  if (isValid.value) {
    emit("save", { ...content.value });
    content.value = { name: "", type: "ppt" };
  }
};
</script>
