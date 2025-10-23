import Home from '../views/Home/index.vue'
import Teacher from '../views/Teacher/Teacher.vue'
import CreateCourse from '../views/CreateCourse/CreateCourse.vue'
import SelectCourse from '../views/SelectCourse/SelectCourse.vue'
import MyCourse from '../views/MyCourse/MyCourse.vue'
import Class from '../views/Class.vue'
import Content from '../views/Content.vue'

import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: { requiresAuth: false },
  },
  {
    path: '/MyCourse',
    name: 'MyCourse',
    component: MyCourse,
    meta: { requiresAuth: true },
  },
  {
    path: '/CreateCourse',
    name: 'CreateCourse',
    component: CreateCourse,
    meta: { requiresAuth: true },
  },
  {
    path: '/SelectCourse',
    name: 'SelectCourse',
    component: SelectCourse,
    meta: { requiresAuth: true },
  },
  {
    path: '/Class',
    name: 'Class',
    component: Class,
    meta: { requiresAuth: true },
  },
  {
    path: '/Teacher',
    name: 'Teacher',
    component: Teacher,
    meta: { requiresAuth: true },
  },
  {
    path: '/content',
    name: 'Content',
    component: Content,
    meta: { requiresAuth: true },
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, _from, next) => {
  const keycloak = window.keycloak
  if (to.meta.requiresAuth) {
    if (keycloak && keycloak.authenticated) {
      next()
    } else if (keycloak) {
      // 確保 keycloak 已正確初始化後再調用 login
      // login() 可能會重定向頁面
      try {
        keycloak.login({
          redirectUri: window.location.origin + to.fullPath,
        })
        // login() 會處理重定向，所以不需要調用 next()
      } catch (error) {
        console.error('Keycloak 登入失敗:', error)
        // 如果登入失敗，導向首頁
        next('/')
      }
    } else {
      // keycloak 未初始化，導向首頁
      console.warn('Keycloak 未初始化，無法驗證身份')
      next('/')
    }
  } else {
    next()
  }
})

export default router
