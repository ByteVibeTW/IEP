import Class from '../views/Class.vue';
import Classroom from '../views/Classroom/Classroom.vue';
import Content from '../views/Content/Content.vue';
import CreateCourse from '../views/CreateCourse/CreateCourse.vue';
import Home from '../views/Home/Home.vue';
import Login from '../views/Login/Login.vue';
import MyCourse from '../views/MyCourse/MyCourse.vue';
import SelectCourse from '../views/SelectCourse/SelectCourse.vue';
import Teacher from '../views/Teacher/Teacher.vue';
import { hasToken } from '@/utils/tokenManager';
import { type RouteRecordRaw, createRouter, createWebHistory } from 'vue-router';

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
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { requiresAuth: false },
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
  {
    path: '/Classroom/:id',
    name: 'Classroom',
    component: Classroom,
    meta: { requiresAuth: true },
  },
  {
    path: '/Content/:id',
    name: 'Content',
    component: Content,
    meta: { requiresAuth: true },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, _from, next) => {
  if (to.meta.requiresAuth) {
    if (to.path === '/login') {
      next('/');
      return;
    }

    if (hasToken()) {
      next();
    } else {
      next({ path: '/login', query: { redirect: to.fullPath } });
    }
  } else {
    if (to.path === '/login' && hasToken()) {
      const redirect = typeof to.query.redirect === 'string' ? to.query.redirect : '/';
      next(redirect);
      return;
    }

    next();
  }
});

export default router;
