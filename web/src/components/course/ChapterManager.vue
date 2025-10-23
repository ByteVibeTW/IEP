<template>
  <div class="mb-8 bg-white rounded-2xl shadow p-4">
    <h2 class="text-xl font-bold text-purple-800 border-b-4 border-gray-200 pb-2 mb-4">
      {{ chapter.title }}
      <button v-if="showDeleteButton" class="text-sm text-red-500 hover:underline ml-4" @click="$emit('delete')">
        刪除章節 🗑️
      </button>
    </h2>

    <ul>
      <li v-for="(item, index) in chapter.items" :key="index" class="flex items-center space-x-2 py-1">
        <span class="text-xl">{{ getIcon(item.type) }}</span>
        <span class="text-blue-700 hover:underline cursor-pointer" @click="$emit('item-click', item)">
          {{ item.name }}
        </span>
        <button v-if="showDeleteButton" class="text-sm text-red-500 hover:underline ml-auto"
          @click="$emit('delete-item', index)">
          刪除🗑️
        </button>
      </li>
    </ul>

    <slot />
  </div>
</template>

<script setup lang="ts">
defineProps({
  chapter: {
    type: Object,
    required: true,
  },
  showDeleteButton: {
    type: Boolean,
    default: false,
  },
});

defineEmits(['delete', 'delete-item', 'item-click']);

const getIcon = (type) => {
  const icons = {
    ppt: '📊',
    excel: '📈',
    doc: '📄',
    video: '🎥',
  };
  return icons[type] || '📁';
};
</script>
