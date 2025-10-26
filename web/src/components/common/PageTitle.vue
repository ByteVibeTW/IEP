<script setup lang="ts">
import Divider from 'primevue/divider';
import BackButton from '@/components/button/BackButton.vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const props = defineProps({
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
    default: '/',
  },
});

const handleBack = (): void => {
  router.push(props.backRoute);
};
</script>

<template>
  <div class="pt-20 w-[100%]">
    <div class="flex items-center justify-between">
      <span class="text-[24px] font-bold h-fit">
        {{ title }}
      </span>
      <BackButton v-if="showBackButton" @click="handleBack" />
    </div>
    <Divider class="my-4" />
  </div>
</template>
