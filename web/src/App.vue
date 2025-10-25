<template>
  <Layout v-if="!isHomePage">
    <router-view />
  </Layout>
  <router-view v-else />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import Layout from '@/Layout/Layout.vue'

const route = useRoute()

const isHomePage = computed(() => route.path === '/')
</script>
