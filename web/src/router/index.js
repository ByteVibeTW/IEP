import Class from '../views/Class.vue';
import CreateCourse from '../views/CreateCourse.vue';
import Home from '../views/Home/index.vue';
import MyCourse from '../views/MyCourse/MyCourse.vue';
import SelectCourse from '../views/SelectCourse.vue';
import Teacher from '../views/Teacher.vue';
import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  { path: '/', name: 'Home', component: Home },
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
    // meta: { requiresAuth: true },
  },
  {
    path: '/SelectCourse',
    name: 'SelectCourse',
    component: SelectCourse,
    // meta: { requiresAuth: true },
  },
  {
    path: '/Class',
    name: 'Class',
    component: Class,
    // meta: { requiresAuth: true },
  },
  {
    path: '/Teacher',
    name: 'Teacher',
    component: Teacher,
    // meta: { requiresAuth: true },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// router.beforeEach(async (to, from, next) => {
//   if (to.meta.requiresAuth) {
//     if (!keycloak.authenticated) {
//       await keycloak.login({
//         redirectUri: window.location.origin + "/MyCourse"
//       });
//     } else {
//       next();
//     }
//   } else {
//     next();
//   }
// });

export default router;
