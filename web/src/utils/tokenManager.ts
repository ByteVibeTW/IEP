import { jwtDecode } from 'jwt-decode';

const TOKEN_KEY = 'token'

/**
 * 儲存 access_token 到 localStorage
 * @param {string} token - access_token
 */
export const saveToken = (token: string): void => {
  try {
    localStorage.setItem(TOKEN_KEY, token)
    console.log('Token 已儲存到 localStorage')
  } catch (error) {
    console.error('儲存 token 失敗:', error)
  }
}

/**
 * 從 localStorage 取得 access_token
 * @returns {string|null} access_token 或 null
 */
export const getToken = (): string | null => {
  try {
    return localStorage.getItem(TOKEN_KEY)
  } catch (error) {
    console.error('取得 token 失敗:', error)
    return null
  }
}

/**
 * 從 localStorage 清除 access_token
 */
export const removeToken = (): void => {
  try {
    localStorage.removeItem(TOKEN_KEY)
    console.log('Token 已從 localStorage 清除')
  } catch (error) {
    console.error('清除 token 失敗:', error)
  }
}

/**
 * 檢查是否有有效的 token
 * @returns {boolean} 是否有 token
 */
export const hasToken = (): boolean => {
  const token = getToken()
  if (!token) return false
  const decoded = jwtDecode<{ exp: number }>(token)
  return decoded.exp && decoded.exp > Date.now() / 1000 ? true : false
}

/**
 * 取得 token 的詳細資訊（如果 token 是 JWT）
 * @returns {object|null} token 解析後的資訊或 null
 */
export const getTokenInfo = (): any | null => {
  const token = getToken()
  if (!token) return null
  return jwtDecode(token)
}
