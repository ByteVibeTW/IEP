<script setup>
import { useUserStore } from '../../stores/user';
import { useAuthStore } from '@/stores/auth';
import { computed } from 'vue';
import { RouterLink } from 'vue-router';

const props = defineProps({ isMobile: Boolean });
const emit = defineEmits(['itemClick']);

const authStore = useAuthStore();
const userStore = useUserStore();

const baseLinks = computed(() => [
  { name: '成為老師', to: '/Teacher' },
  {
    name: '我要開課',
    to: '/CreateCourse',
    hidden: !userStore.currentUserInfo.user_is_teacher,
  },
  { name: '我要選課', to: '/SelectCourse' },
  { name: '我的課程', to: '/MyCourse' },
  {
    name: '雲端開發平台',
    to: 'https://coder.yang-lin.dev/api/v2/users/oidc/callback',
  },
]);

const links = computed(() => {
  const visibleLinks = baseLinks.value.filter((link) => !link.hidden);
  if (authStore.isAuthenticated) {
    return [...visibleLinks, { name: '登出', action: authStore.logout }];
  } else {
    return [{ name: '登入', action: authStore.login }];
  }
});

function linkComponent(link) {
  return link.to?.startsWith('http') ? 'a' : RouterLink;
}

// 根據元件回傳對應 props
function linkProps(link) {
  if (link.to?.startsWith('http')) {
    return { href: link.to, target: '_blank', rel: 'noopener noreferrer' };
  } else {
    return { to: link.to || '#' };
  }
}

const linkClass = computed(() => [
  'text-gray-200 hover:text-white hover:bg-gray-600 rounded-lg p-2 cursor-pointer',
  props.isMobile ? 'block text-center' : 'text-[16px]',
]);
const containerClass = computed(() => (props.isMobile ? 'space-y-3' : 'flex space-x-2'));

function onLinkClick(link) {
  if (link.action) {
    link.action();
  } else if (props.isMobile) {
    emit('itemClick');
  }
}
</script>

<template>
  <div :class="containerClass">
    <component
      :is="linkComponent(link)"
      v-for="(link, idx) in links"
      :key="idx"
      v-bind="linkProps(link)"
      :class="linkClass"
      @click="onLinkClick(link)"
    >
      {{ link.name }}
    </component>
  </div>
</template>
