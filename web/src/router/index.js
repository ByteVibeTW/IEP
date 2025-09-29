import Home from '../views/Home/index.vue';
import Teacher from '../views/Teacher/Teacher.vue';
import CreateCourse from '../views/CreateCourse/CreateCourse.vue';
import SelectCourse from '../views/SelectCourse/SelectCourse.vue';
import MyCourse from '../views/MyCourse/MyCourse.vue';
import Class from '../views/Class.vue';
import Content from '../views/Content.vue';

import { createRouter, createWebHistory } from 'vue-router';

const routes = [
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
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const keycloak = window.keycloak;
  if (to.meta.requiresAuth) {
    if (keycloak && keycloak.authenticated) {
      next();
    } else {
      keycloak.login({
        redirectUri: window.location.origin + to.fullPath
      });
    }
  } else {
    next();
  }
});

export default router;
