import { createWebHistory, createRouter } from "vue-router";
import { useAuthStore } from '../stores/auth';

import Home from "../views/Home.vue";
import SelectCourse from "../views/SelectCourse.vue";
import CreateCourse from "../views/CreateCourse.vue";
import MyCourse from "../views/MyCourse.vue";
import Class from "../views/Class.vue";
import Teacher from "../views/Teacher.vue";
import Callback from "../views/Callback.vue";

const routes =  [
    { path: "/", name: "Home", component: Home },
    { path: "/CreateCourse", name: "CreateCourse", component: CreateCourse , meta: { requiresAuth: true }},
    { path: "/SelectCourse", name: "SelectCourse", component: SelectCourse , meta: { requiresAuth: true }},
    { path: "/MyCourse", name: "MyCourse", component: MyCourse , meta: { requiresAuth: true }},
    { path: "/Class", name: "Class", component: Class , meta: { requiresAuth: true }},
    { path: "/Teacher", name: "Teacher", component: Teacher , meta: { requiresAuth: true }},
    { path: "/callback", name: "Callback", component: Callback },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach(async (to, _, next) => {
  const authStore = useAuthStore();
  await authStore.checkAuth();

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next({ name: '/' });
  } else {
    next();
  }
});

export default router;

