<script setup lang="ts">
import { useAuthStore } from '@/stores/auth';
import { computed, nextTick, onBeforeUnmount, onMounted, ref } from 'vue';
import { RouterLink, useRoute, useRouter } from 'vue-router';

interface NavLink {
  name: string;
  to?: string;
  action?: () => void;
}

const isMenuOpen = ref<boolean>(false);
const inHeroSection = ref<boolean>(false);
let heroObserver: IntersectionObserver | null = null;
const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();

const isAuth = computed<boolean>(() => {
  return authStore.isAuthenticated;
});

const navTextColor = computed<string>(() => {
  return inHeroSection.value ? 'text-white' : 'text-slate-900';
});

const navShellClass = computed<string>(() => {
  return inHeroSection.value
    ? 'bg-sky-950/35 text-white border border-white/10 shadow-[0_10px_30px_rgba(8,47,73,0.18)]'
    : 'bg-white/80 text-slate-900 border border-sky-100 shadow-[0_10px_30px_rgba(14,165,233,0.08)]';
});

const navHoverClass = computed<string>(() => {
  return inHeroSection.value
    ? 'hover:text-white hover:bg-white/10'
    : 'hover:text-sky-700 hover:bg-sky-100';
});

const navDividerClass = computed<string>(() => {
  return inHeroSection.value ? 'border-white/20' : 'border-sky-100';
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

const observeHeroSection = (): void => {
  heroObserver?.disconnect();

  const heroSection = document.getElementById('home-hero');
  if (!heroSection) {
    inHeroSection.value = false;
    return;
  }

  const heroRect = heroSection.getBoundingClientRect();
  inHeroSection.value = heroRect.top < window.innerHeight && heroRect.bottom > 0;

  heroObserver = new IntersectionObserver(
    (entries) => {
      const entry = entries[0];
      if (!entry) {
        return;
      }

      inHeroSection.value = entry.isIntersecting;
    },
    {
      threshold: 0.5,
    }
  );

  heroObserver.observe(heroSection);
};

onMounted(() => {
  nextTick(() => {
    observeHeroSection();
  });
});

onBeforeUnmount(() => {
  heroObserver?.disconnect();
});

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
    :class="[
      navShellClass,
      'backdrop-blur-md fixed w-full md:w-[90%] top-0 z-10 md:mx-[5%] md:rounded-2xl md:mt-4 transition-all',
    ]"
  >
    <div class="container mx-auto px-4 md:px-[64px] flex justify-between items-center py-2">
      <!-- Logo -->
      <router-link to="/" :class="[navTextColor, 'text-xl font-bold flex items-center']">
        <img src="@/assets/icon.svg" alt="Logo" class="w-15 h-auto box-content self-center" />
        <span :class="[navTextColor, 'ml-1 text-[24px] hidden md:inline']">整合式教學平台</span>
      </router-link>

      <!-- Desktop Menu -->
      <div class="hidden md:flex space-x-2">
        <component
          :is="linkComponent(link)"
          v-for="link in links"
          :key="link.name"
          v-bind="linkProps(link)"
          :class="[
            navTextColor,
            navHoverClass,
            'rounded-lg p-2 cursor-pointer text-[16px] transition-colors',
          ]"
          @click="onLinkClick(link)"
        >
          {{ link.name }}
        </component>
      </div>

      <!-- Mobile Menu Button -->
      <button
        :class="[navTextColor, 'md:hidden hover:text-gray-600 focus:outline-none']"
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
        <hr :class="['border-2 rounded-full', navDividerClass]" />
        <div class="space-y-3">
          <component
            :is="linkComponent(link)"
            v-for="link in links"
            :key="link.name"
            v-bind="linkProps(link)"
            :class="[
              navTextColor,
              navHoverClass,
              'rounded-lg p-2 cursor-pointer block text-center transition-colors',
            ]"
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
