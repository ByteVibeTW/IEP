<template>
  <div
    class="soft-surface-elevated mb-8 p-5 sm:p-6 transition-transform duration-300 hover:-translate-y-1"
  >
    <h2
      class="section-heading text-xl font-bold border-b border-sky-100 pb-3 mb-5 flex items-center justify-between gap-4"
    >
      {{ chapter.title }}
      <button
        v-if="showDeleteButton"
        class="text-sm text-rose-500 hover:text-rose-600 hover:underline ml-4 transition-colors"
        @click="$emit('delete')"
      >
        刪除章節 🗑️
      </button>
    </h2>

    <ul>
      <li
        v-for="(item, index) in chapter.items"
        :key="index"
        class="flex items-center gap-3 py-2 rounded-xl px-3 hover:bg-sky-50/70 transition-colors"
      >
        <span class="text-xl shrink-0">{{ getIcon(item.type) }}</span>
        <span
          class="text-sky-700 hover:text-sky-800 hover:underline cursor-pointer transition-colors"
          @click="$emit('item-click', item)"
        >
          {{ item.name }}
        </span>
        <button
          v-if="showDeleteButton"
          class="text-sm text-rose-500 hover:text-rose-600 hover:underline ml-auto transition-colors"
          @click="$emit('delete-item', index)"
        >
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
