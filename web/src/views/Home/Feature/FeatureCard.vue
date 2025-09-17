<template>
  <div class="h-full p-6 flex flex-col">
    <div class="text-5xl mb-4">{{ feature.icon }}</div>
    <h3 class="text-xl font-semibold text-gray-800 mb-3">
      {{ feature.title }}
    </h3>
    <p class="text-gray-600 flex-grow">{{ feature.description }}</p>
    <div v-if="feature.highlight" class="mt-4">
      <span
        class="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-white/50 text-gray-700"
      >
        <span
          class="w-2 h-2 mr-2 rounded-full bg-blue-500 animate-pulse"
        ></span>
        熱門功能
      </span>
    </div>
  </div>
</template>

<script setup>
defineProps({
  feature: {
    type: Object,
    required: true,
    default: () => ({
      icon: "",
      title: "",
      description: "",
      highlight: false,
    }),
  },
});
</script>

<style scoped>
.animate-pulse {
  animation: pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite;
}

@keyframes pulse {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}
</style>
