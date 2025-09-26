import axios from "axios";
import { defineStore } from "pinia";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

export const useUserStore = defineStore("userStore", {
  state: () => ({
    allUsersInfo: [], // 所有使用者
    currentUserInfo: "", // 當前使用者
    loading: false, // 加載狀態
    error: null, // 錯誤訊息
  }),
  actions: {
    async fetchUser() {
      this.loading = true;
      this.error = null;
      this.authInfo = JSON.parse(
        localStorage.getItem(
          "oidc.user:http://172.16.1.16:8081/realms/coder:vue",
        ),
      );
      try {
        const response = await axios.get(`${apiBaseUrl}/api/users`, {
          headers: {
            Authorization: `Bearer ${this.authInfo.access_token}`,
          },
        });
        this.allUsersInfo = response.data;
      } catch (error) {
        console.error("Fetch user error:", error);
      } finally {
        this.loading = false;
      }
      try {
        const response = await axios.get(`${apiBaseUrl}/api/current_user`, {
          headers: {
            Authorization: `Bearer ${this.authInfo.access_token}`,
          },
        });
        this.currentUserInfo = response.data;
      } catch (error) {
        this.error = error.response?.data?.detail || "無法獲取使用者資料";
        console.error("Fetch user error:", error);
      } finally {
        this.loading = false;
      }
    },
  },
});
