<template>
  <div class="pt-20 w-[100%]">
    <router-link
      v-if="showBackButton"
      :to="backRoute"
      class="bg-[#3498db] hover:bg-[#2d83bc] text-white text-[24px] self-center px-4 mr-4 rounded-lg h-fit"
    >
      返回
    </router-link>
    <span class="text-[24px] mt-20 mb-[16px] font-bold h-fit">
      {{ title }}
    </span>
    <hr class="border-2 border-gray-500 rounded-2xl" />
  </div>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    required: true,
  },
  showBackButton: {
    type: Boolean,
    default: false,
  },
  backRoute: {
    type: String,
    default: "/",
  },
});
</script>
