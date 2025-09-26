<template>
  <nav
    class="bg-gray-800/20 backdrop-blur-md fixed w-full md:w-[90%] top-0 z-10 md:mx-[5%] md:rounded-xl transition-all mt-4"
  >
    <div class="container mx-auto px-4 md:px-[64px] flex justify-between items-center py-2">
      <!-- Logo -->
      <Logo />

      <!-- Desktop Menu -->
      <div class="hidden md:flex space-x-2">
        <MenuLinks :is-mobile="false" />
      </div>

      <!-- Mobile Menu Button -->
      <MobileToggleBtn @toggle="toggleMenu" />
    </div>

    <!-- Mobile Menu -->
    <transition name="slide-fade">
      <div v-if="isMenuOpen" class="md:hidden space-y-3 mb-3">
        <hr class="border-2 border-gray-500 rounded-full" />
        <MenuLinks :is-mobile="true" @item-click="toggleMenu" />
      </div>
    </transition>
  </nav>
</template>

<script setup>
import Logo from './Logo.vue';
import MenuLinks from './MenuLinks.vue';
import MobileToggleBtn from './MobileToggleBtn.vue';
import { ref } from 'vue';

const isMenuOpen = ref(false);
const toggleMenu = () => (isMenuOpen.value = !isMenuOpen.value);
</script>

<style scoped>
.slide-fade-leave-active {
  transition: all 0.5s ease;
}
.slide-fade-leave-to {
  transform: translateY(-10px);
  opacity: 0;
}
</style>
