<template>
  <section
    ref="heroRef"
    class="h-screen max-h-[100%] bg-desktop-hero md:bg-mobile-hero bg-cover bg-center bg-black/30 transition-opacity duration-100 ease-out"
  >
    <div class="absolute top-0 w-full">
      <img src="@/assets/hero/computerMain.svg" />
    </div>
    <div class="absolute top-10 right-0 w-[90%]">
      <img src="@/assets/hero/computerImage.svg" />
    </div>
    <div class="absolute top-0 w-full md:hidden">
      <img src="@/assets/hero/phoneMain.svg" />
    </div>
    <div class="absolute bottom-0 w-[100%] md:hidden">
      <img src="@/assets/hero/phoneImage.svg" />
    </div>
    <div
      class="container mx-auto md:w-[50%] w-[90%] h-full items-center text-center absolute md:top-70 md:left-10 top-[15%] inset-0 md:inset-auto"
    >
      <div class="bg-white/60 rounded-2xl p-6 m-4 shadow-xl backdrop-blur-sm">
        <h1 class="text-gray-800 text-4xl font-extrabold mb-4">整合式教學平台</h1>
        <p class="text-gray-800 text-base font-medium leading-relaxed">
          探索豐富的課程，隨時隨地學習新知識，提升自己的能力。我們提供最優質的線上教學體驗，讓學習更有效率、更有樂趣。
        </p>
        <div class="flex justify-center gap-8 mt-6 mx-2">
          <Button label="平台特色" raised class="w-full" @click="scrollToFeatures" />
          <Button
            label="熱門課程"
            raised
            class="w-full"
            :hidden="!authStore.isAuthenticated"
            @click="scrollToHotCourse"
          />
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth';
import Button from 'primevue/button';
import { onBeforeUnmount, onMounted, ref } from 'vue';

const authStore = useAuthStore();

const heroRef = ref(null);

const scrollToFeatures = () => {
  const featuresSection = document.getElementById('features');
  if (featuresSection) {
    featuresSection.scrollIntoView({ behavior: 'smooth' });
  }
};

const handleScroll = () => {
  if (!heroRef.value) return;
  const scrollTop = window.scrollY;
  const height = window.innerHeight;
  heroRef.value.style.opacity = `${1 - scrollTop / height}`;
};

const scrollToHotCourse = () => {
  const hotCourseSection = document.getElementById('hotcourse');
  if (hotCourseSection) {
    hotCourseSection.scrollIntoView({ behavior: 'smooth' });
  }
};

onMounted(() => {
  window.addEventListener('scroll', handleScroll);
});
onBeforeUnmount(() => {
  window.removeEventListener('scroll', handleScroll);
});
</script>

<style>
.bg-mobile-hero {
  background-image: url('@/assets/hero/phoneBG.svg');
}

.bg-desktop-hero {
  background-image: url('@/assets/hero/computerBG.svg');
}
</style>
