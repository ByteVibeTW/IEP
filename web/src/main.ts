import App from './App.vue';
import { baseQueryClient } from './api/base/BaseQueryClient';
import router from './router/index';
import { useAuthStore } from './stores/auth';
import './style.css';
import { themeConfig } from './theme';
import { VueQueryPlugin } from '@tanstack/vue-query';
import { createPinia } from 'pinia';
import PrimeVue from 'primevue/config';
import Toast from 'primevue/toast';
import ToastService from 'primevue/toastservice';
import { createApp } from 'vue';

const app = createApp(App);
const pinia = createPinia();
const authStore = useAuthStore(pinia);

authStore.restoreSession();

app.use(pinia);
app.use(router);
app.use(VueQueryPlugin, { queryClient: baseQueryClient });

app.use(PrimeVue, themeConfig);
app.use(ToastService);
app.component('Toast', Toast);

app.mount('#app');
