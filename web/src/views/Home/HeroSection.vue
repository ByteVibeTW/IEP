<template>
  <section
    id="home-hero"
    ref="heroRef"
    class="relative isolate min-h-[92vh] overflow-hidden bg-desktop-hero md:bg-mobile-hero bg-cover bg-center bg-black/20 transition-opacity duration-100 ease-out"
  >
    <div class="absolute top-0 w-full">
      <img src="@/assets/hero/computerMain.svg" class="select-none" />
    </div>
    <div class="absolute top-10 right-0 w-[90%] animate-fade-in-up">
      <img src="@/assets/hero/computerImage.svg" class="select-none drop-shadow-2xl" />
    </div>
    <div class="absolute top-0 w-full md:hidden">
      <img src="@/assets/hero/phoneMain.svg" class="select-none" />
    </div>
    <div class="absolute bottom-0 w-[100%] md:hidden">
      <img src="@/assets/hero/phoneImage.svg" class="select-none drop-shadow-2xl" />
    </div>
    <div
      class="container mx-auto md:w-[52%] w-[92%] h-full items-center text-center absolute md:top-56 md:left-10 top-[16%] inset-0 md:inset-auto"
    >
      <div
        class="hero-card bg-white/65 rounded-[2rem] p-6 sm:p-8 md:p-10 m-4 shadow-[0_24px_80px_rgba(14,165,233,0.16)] backdrop-blur-md border border-white/70"
      >
        <h1 class="text-slate-900 text-4xl sm:text-5xl font-extrabold mb-4 tracking-tight">
          整合式教學平台
        </h1>
        <p class="text-slate-700 text-base sm:text-lg font-medium leading-8 sm:leading-9">
          探索豐富的課程，隨時隨地學習新知識，提升自己的能力。我們提供最優質的線上教學體驗，讓學習更有效率、更有樂趣。
        </p>
        <div class="mt-8 grid grid-cols-1 gap-4 mx-2 sm:grid-cols-2">
          <Button label="平台特色" raised class="w-full justify-center" @click="scrollToFeatures" />
          <Button
            label="熱門課程"
            raised
            severity="secondary"
            class="w-full justify-center"
            @click="scrollToHotCourse"
          />
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
// import { useAuthStore } from '@/stores/auth';
import Button from 'primevue/button';
import { onBeforeUnmount, onMounted, ref } from 'vue';

// const authStore = useAuthStore();

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
.hero-card {
  transform: translateY(0);
  transition:
    transform 220ms ease,
    box-shadow 220ms ease,
    background-color 220ms ease;
}

.hero-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 28px 90px rgba(14, 165, 233, 0.22);
}

.animate-fade-in-up {
  animation: fade-in-up 700ms ease both;
}

.bg-mobile-hero {
  background-image: url('@/assets/hero/phoneBG.svg');
}

.bg-desktop-hero {
  background-image: url('@/assets/hero/computerBG.svg');
}

@keyframes fade-in-up {
  from {
    opacity: 0;
    transform: translateY(12px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
