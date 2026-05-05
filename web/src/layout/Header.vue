<script setup lang="ts">
import { useAuthStore } from '@/stores/auth';
import { computed, inject, ref } from 'vue';
import { RouterLink, useRoute, useRouter } from 'vue-router';

interface NavLink {
  name: string;
  to?: string;
  action?: () => void;
}

const isMenuOpen = ref<boolean>(false);
const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();

const isAuth = computed<boolean>(() => {
  return authStore.isAuthenticated;
});

const baseLinks: NavLink[] = [
  { name: '成為老師', to: '/Teacher' },
  { name: '我要開課', to: '/CreateCourse' },
  { name: '我要選課', to: '/SelectCourse' },
  { name: '我的課程', to: '/MyCourse' },
];

const links = computed<NavLink[]>(() => {
  if (isAuth.value) {
    return [...baseLinks, { name: '登出', action: handleLogout }];
  } else {
    return [{ name: '登入', action: handleLogin }];
  }
});

const handleLogin = (): void => {
  router.push({ path: '/login', query: { redirect: route.fullPath } });
};

const handleLogout = (): void => {
  authStore.logout();
  router.push('/');
};

function linkComponent(link: NavLink): typeof RouterLink | 'a' {
  return link.to?.startsWith('http') ? 'a' : RouterLink;
}

function linkProps(link: NavLink): Record<string, any> {
  if (link.to?.startsWith('http')) {
    return { href: link.to, target: '_blank', rel: 'noopener noreferrer' };
  } else {
    return { to: link.to || '#' };
  }
}

function onLinkClick(link: NavLink, isMobile = false): void {
  if (link.action) {
    link.action();
  } else if (isMobile) {
    isMenuOpen.value = false;
  }
}

const toggleMenu = (): void => {
  isMenuOpen.value = !isMenuOpen.value;
};
</script>

<template>
  <nav
    class="bg-gray-800/20 backdrop-blur-md fixed w-full md:w-[90%] top-0 z-10 md:mx-[5%] md:rounded-xl md:mt-4 transition-all"
  >
    <div class="container mx-auto px-4 md:px-[64px] flex justify-between items-center py-2">
      <!-- Logo -->
      <router-link to="/" class="text-gray-200 text-xl font-bold flex items-center">
        <img src="@/assets/icon.svg" alt="Logo" class="w-15 h-auto box-content self-center" />
        <span class="text-gray-200 ml-1 text-[24px] hidden md:inline">整合式教學平台</span>
      </router-link>

      <!-- Desktop Menu -->
      <div class="hidden md:flex space-x-2">
        <component
          :is="linkComponent(link)"
          v-for="link in links"
          :key="link.name"
          v-bind="linkProps(link)"
          class="text-gray-200 hover:text-white hover:bg-gray-600 rounded-lg p-2 cursor-pointer text-[16px]"
          @click="onLinkClick(link)"
        >
          {{ link.name }}
        </component>
      </div>

      <!-- Mobile Menu Button -->
      <button
        class="md:hidden text-gray-200 hover:text-white focus:outline-none"
        @click="toggleMenu"
      >
        <svg
          class="w-6 h-6"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M4 6h16M4 12h16m-7 6h7"
          />
        </svg>
      </button>
    </div>

    <!-- Mobile Menu -->
    <transition name="slide-fade">
      <div v-if="isMenuOpen" class="md:hidden space-y-3 mb-3">
        <hr class="border-2 border-gray-500 rounded-full" />
        <div class="space-y-3">
          <component
            :is="linkComponent(link)"
            v-for="link in links"
            :key="link.name"
            v-bind="linkProps(link)"
            class="text-gray-200 hover:text-white hover:bg-gray-600 rounded-lg p-2 cursor-pointer block text-center"
            @click="onLinkClick(link, true)"
          >
            {{ link.name }}
          </component>
        </div>
      </div>
    </transition>
  </nav>
</template>

<style scoped>
.slide-fade-leave-active {
  transition: all 0.5s ease;
}

.slide-fade-leave-to {
  transform: translateY(-10px);
  opacity: 0;
}
</style>
