import App from './App.vue'
import router from './router/index'
import './style.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import Keycloak from 'keycloak-js'

import PrimeVue from 'primevue/config'
import Toast from 'primevue/toast'
import ToastService from 'primevue/toastservice'
import { themeConfig } from './theme'
import { saveToken, removeToken } from './utils/tokenManager'
import { VueQueryPlugin } from '@tanstack/vue-query'
import { baseQueryClient } from './api/base/BaseQueryClient'

const keycloak = new Keycloak({
  url: 'https://keycloak.yang-lin.dev',
  realm: 'iep',
  clientId: 'iep',
})

// 將 keycloak 設為全域變數，讓路由守衛可以使用
window.keycloak = keycloak

keycloak
  .init({
    onLoad: 'check-sso',
  })
  .then((authenticated) => {
    console.log(authenticated ? '用戶已登入' : '用戶未登入')

    if (authenticated && keycloak.token) {
      saveToken(keycloak.token)
      console.log('Access token 已儲存到 localStorage')
    } else {
      removeToken()
    }

    const app = createApp(App)
    const pinia = createPinia()

    app.use(pinia)
    app.use(router)
    app.use(VueQueryPlugin, { queryClient: baseQueryClient })

    app.use(PrimeVue, themeConfig)
    app.use(ToastService)
    app.component('Toast', Toast)

    app.provide('keycloak', keycloak)
    app.config.globalProperties.$keycloak = keycloak
    app.mount('#app')
  })
  .catch((error: Error) => {
    console.error('Keycloak 初始化失敗:', error)
    console.error('錯誤詳情:', {
      message: error.message,
      error: (error as any).error,
      error_description: (error as any).error_description,
    })

    removeToken()

    // 將 keycloak 設為 null，表示初始化失敗
    window.keycloak = null as any

    const app = createApp(App)
    const pinia = createPinia()

    app.use(pinia)
    app.use(router)
    app.use(VueQueryPlugin, { queryClient: baseQueryClient })

    app.use(PrimeVue, themeConfig)
    app.use(ToastService)
    app.component('Toast', Toast)

    app.provide('keycloak', null)
    app.config.globalProperties.$keycloak = null
    app.mount('#app')
  })
