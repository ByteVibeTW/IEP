<template>
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
    <p class="text-[#1e2663] text-[36px] text-center mt-[120px] font-bold mb-8">平台特色</p>
    <div class="bento-grid">
      <div
        v-for="feature in features"
        :key="feature.id"
        :class="[
          'bento-item',
          {
            'col-span-2': feature.id === 1 || feature.id === 4,
            'row-span-2': feature.id === 1 || feature.id === 3,
            'bg-gradient-to-br': true,
            'from-blue-50 to-indigo-50': feature.id === 1,
            'from-purple-50 to-pink-50': feature.id === 2,
            'from-green-50 to-emerald-50': feature.id === 3,
            'from-orange-50 to-amber-50': feature.id === 4,
            'from-cyan-50 to-blue-50': feature.id === 5,
            'from-rose-50 to-pink-50': feature.id === 6,
          },
        ]"
      >
        <FeatureCard :feature="feature" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import FeatureCard from './FeatureCard.vue';

const features = [
  {
    id: 1,
    icon: '📚',
    title: '豐富的課程資源',
    description:
      '涵蓋各種領域的優質課程，從程式設計到語言學習，應有盡有。我們持續更新課程內容，確保學習者能夠獲得最新、最實用的知識。',
    highlight: true,
  },
  {
    id: 2,
    icon: '📈',
    title: '提升學習效率',
    description: '學生可以更專注於學習和實作，減少因環境問題等造成不必要的困擾',
  },
  {
    id: 3,
    icon: '🌐',
    title: '雲端開發',
    description: '可以透過網路在線上進行開發，並且手機、平板、電腦都可以使用。隨時隨地，輕鬆學習。',
    highlight: true,
  },
  {
    id: 4,
    icon: '🏗️',
    title: '客製化開發環境',
    description:
      '減少學生在設定環境配置上的困難，讓他們能夠更快地進入學習狀態。我們提供完整的開發環境配置，讓您專注於學習。',
    highlight: true,
  },
  {
    id: 5,
    icon: '🎯',
    title: '靈活學習',
    description: '可以在任何時間、任何地點通過網路連線至平台進行學習和實作',
  },
  {
    id: 6,
    icon: '📊',
    title: '學習分析',
    description: '提供詳細的學習數據分析，幫助學生了解自己的學習進度和強項，制定更有效的學習策略',
  },
];
</script>

<style scoped>
.bento-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.5rem;
  margin-bottom: 4rem;
}

.bento-item {
  border-radius: 1.5rem;
  overflow: hidden;
  transition: all 0.3s ease;
}

.bento-item:hover {
  transform: translateY(-5px);
  box-shadow:
    0 20px 25px -5px rgb(0 0 0 / 0.1),
    0 8px 10px -6px rgb(0 0 0 / 0.1);
}

@media (max-width: 1024px) {
  .bento-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .bento-grid {
    grid-template-columns: 1fr;
  }

  .bento-item {
    grid-column: span 1 !important;
    grid-row: span 1 !important;
  }
}
</style>
