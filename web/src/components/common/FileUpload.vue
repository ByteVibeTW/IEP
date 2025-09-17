<template>
  <div class="mb-4">
    <label v-if="label" class="text-[20px] font-bold">{{ label }}</label>
    <input
      type="file"
      :accept="accept"
      @change="handleFileChange"
      class="bg-white shadow-2xs shadow-gray-500 text-[16px] w-full border-1 border-solid border-[#ddd] rounded-[8px] p-2 hover:bg-gray-300"
      :disabled="disabled"
    />
    <p v-if="error" class="text-red-500 text-sm mt-1">{{ error }}</p>
  </div>
</template>

<script setup>
const props = defineProps({
  label: {
    type: String,
    default: "",
  },
  accept: {
    type: String,
    default: "*/*",
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  error: {
    type: String,
    default: "",
  },
});

const handleFileChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    emit("file-selected", file);
  }
};

const emit = defineEmits(["file-selected"]);
</script>
