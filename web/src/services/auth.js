import axios from "axios";
import { UserManager, WebStorageStateStore } from "oidc-client-ts";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

const config = {
  authority: "http://172.16.1.16:8081/realms/coder", // 請替換為您的 OIDC 提供者網址
  client_id: "vue", // 請替換為您的客戶端 ID
  redirect_uri: `${window.location.origin}/callback`,
  response_type: "code",
  scope: "openid profile email",
  post_logout_redirect_uri: `${window.location.origin}/callback`,
  userStore: new WebStorageStateStore({ store: window.localStorage }),
  automaticSilentRenew: true,
  silent_redirect_uri: `${window.location.origin}/silent-renew.html`,
  accessTokenExpiringNotificationTime: 60, // 在 token 過期前 60 秒開始刷新
};

const userManager = new UserManager(config);

// 監聽用戶加載事件
function storeUser(token) {
  // implementation
}

userManager.events.addUserLoaded((user) => {
  storeUser(user.access_token);
});

// 監聽用戶卸載事件
userManager.events.addUserUnloaded(() => { /* no-op: placeholder for user unload event */ });

// 監聽無聲刷新錯誤事件
userManager.events.addSilentRenewError((error) => {
  console.error("Token 刷新失敗:", error);
});

const login = async () => {
  try {
    await userManager.signinRedirect();
  } catch (error) {
    console.error("登入失敗:", error);
    throw error;
  }
};

const logout = async () => {
  try {
    await userManager.signoutRedirect();
  } catch (error) {
    console.error("登出失敗:", error);
    throw error;
  }
};

const handleRedirect = async () => {
  try {
    await userManager.signinRedirectCallback();
  } catch (error) {
    console.error("處理回調時發生錯誤:", error);
  }
};

const isAuthenticated = async () => {
  try {
    const user = await userManager.getUser();
    return Boolean(user);
  } catch (error) {
    return false;
  }
};

const getUser = async () => {
  try {
    return await userManager.getUser();
  } catch (error) {
    return null;
  }
};

const storeUser = async (access_token) => {
  try {
    await axios.get(`${apiBaseUrl}/api/me`, {
      headers: {
        Authorization: `Bearer ${access_token}`,
      },
    });
  } catch (error) {
    console.error("無法儲存使用者資料", error);
    throw error;
  }
};

export const authService = {
  login,
  logout,
  handleRedirect,
  isAuthenticated,
  getUser,
  userManager,
};
