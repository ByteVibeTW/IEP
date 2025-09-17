import axios from "axios";
import { defineStore } from "pinia";
import { computed, ref } from "vue";
import { authService } from "../services/auth";
import swal from "sweetalert";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

export const useAuthStore = defineStore("auth", () => {
  // 狀態
  const state = ref({
    user: null,
  });

  // 計算屬性
  const isAuthenticated = computed(() => state.value.user !== null);
  const currentUser = computed(() => state.value.user);

  // 方法
  const setAuthState = (user = null) => {
    state.value = { user };
  };

  // 監聽 token 刷新事件
  const setupTokenRefreshListener = () => {
    authService.userManager.events.addUserLoaded(async (user) => {
      console.log("Token 已刷新，更新 store 狀態");
      setAuthState(user);
    });
  };

  const login = async () => {
    try {
      await authService.login();
      await checkAuth();
      storeUser(state.value.access_token);
    } catch (error) {
      console.error("登入失敗:", error);
      throw error;
    }
  };

  const logout = async () => {
    try {
      await authService.logout();
      setAuthState();
    } catch (error) {
      console.error("登出失敗:", error);
      throw error;
    }
  };

  const handleRedirect = async () => {
    try {
      await authService.handleRedirect();
      setupTokenRefreshListener(); // 設置 token 刷新監聽器
    } catch (error) {
      console.error("處理回調時發生錯誤:", error);
    }
  };

  const checkAuth = async () => {
    try {
      // 先檢查本地認證狀態
      const isAuth = await authService.isAuthenticated();
      const user = isAuth ? await authService.getUser() : null;
      setAuthState(user);
      if (!isAuth) {
        return false;
      }

      // 再向後端驗證權限
      await axios.get(`${apiBaseUrl}/api/me`, {
        headers: {
          Authorization: `Bearer ${user.access_token}`,
        },
      });
      return true;
    } catch (error) {
      if (
        error.response &&
        (error.response.status === 401 || error.response.status === 403)
      ) {
        await logout();
        swal({
          title: "系統自動登出",
          text: "您的登入已過期，請重新登入",
          icon: "warning",
          button: "OK",
        });
      }
      return false;
    }
  };

  return {
    // 計算屬性
    currentUser,
    isAuthenticated,

    // 方法
    login,
    logout,
    handleRedirect,
    checkAuth,
    setupTokenRefreshListener,
  };
});
