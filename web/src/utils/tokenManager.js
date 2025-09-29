// tokenManager.js - Token 管理工具

const TOKEN_KEY = 'token';

/**
 * 儲存 access_token 到 localStorage
 * @param {string} token - access_token
 */
export const saveToken = (token) => {
  try {
    localStorage.setItem(TOKEN_KEY, token);
    console.log('Token 已儲存到 localStorage');
  } catch (error) {
    console.error('儲存 token 失敗:', error);
  }
};

/**
 * 從 localStorage 取得 access_token
 * @returns {string|null} access_token 或 null
 */
export const getToken = () => {
  try {
    return localStorage.getItem(TOKEN_KEY);
  } catch (error) {
    console.error('取得 token 失敗:', error);
    return null;
  }
};

/**
 * 從 localStorage 清除 access_token
 */
export const removeToken = () => {
  try {
    localStorage.removeItem(TOKEN_KEY);
    console.log('Token 已從 localStorage 清除');
  } catch (error) {
    console.error('清除 token 失敗:', error);
  }
};

/**
 * 檢查是否有有效的 token
 * @returns {boolean} 是否有 token
 */
export const hasToken = () => {
  const token = getToken();
  return token !== null && token !== '';
};

/**
 * 取得 token 的詳細資訊（如果 token 是 JWT）
 * @returns {object|null} token 解析後的資訊或 null
 */
export const getTokenInfo = () => {
  const token = getToken();
  if (!token) return null;

  try {
    // JWT token 有三個部分，用 . 分隔
    const parts = token.split('.');
    if (parts.length !== 3) return null;

    // 解碼 payload 部分（第二部分）
    const payload = parts[1];
    const decoded = atob(payload.replace(/-/g, '+').replace(/_/g, '/'));
    return JSON.parse(decoded);
  } catch (error) {
    console.error('解析 token 失敗:', error);
    return null;
  }
};
