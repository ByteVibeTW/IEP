import { publicApi } from '@/api/base/PublicApi';
import {
  type TokenInfo,
  getToken,
  getTokenInfo,
  hasToken,
  removeToken,
  saveToken,
} from '@/utils/tokenManager';
import { defineStore } from 'pinia';

export interface LoginCredentials {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  userId: number;
  username: string;
  nickname?: string | null;
}

export interface AuthUser {
  userId: number;
  username: string;
  nickname: string | null;
}

interface AuthState {
  token: string | null;
  user: AuthUser | null;
  loading: boolean;
  error: string | null;
}

const buildUserFromToken = (tokenInfo: TokenInfo | null): AuthUser | null => {
  if (!tokenInfo?.userId || !tokenInfo.username) {
    return null;
  }

  return {
    userId: Number(tokenInfo.userId),
    username: tokenInfo.username,
    nickname: tokenInfo.nickname ?? null,
  };
};

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    token: null,
    user: null,
    loading: false,
    error: null,
  }),
  getters: {
    isAuthenticated: (state): boolean => state.token !== null && hasToken(state.token),
  },
  actions: {
    restoreSession() {
      const token = getToken();
      if (!token || !hasToken(token)) {
        this.clearSession();
        return;
      }

      const user = buildUserFromToken(getTokenInfo(token));
      if (!user) {
        this.clearSession();
        return;
      }

      this.token = token;
      this.user = user;
      this.error = null;
    },
    async login(credentials: LoginCredentials) {
      this.loading = true;
      this.error = null;

      try {
        const { data } = await publicApi.post<LoginResponse>('/api/v1/auth/login', credentials, {
          headers: { 'Content-Type': 'application/json' },
        });

        saveToken(data.token);
        this.token = data.token;
        this.user = {
          userId: data.userId,
          username: data.username,
          nickname: data.nickname ?? null,
        };

        return data;
      } catch (error: any) {
        this.clearSession();
        this.error = error?.response?.data?.message || error?.response?.data?.detail || '登入失敗';
        throw error;
      } finally {
        this.loading = false;
      }
    },
    logout() {
      this.clearSession();
    },
    clearSession() {
      removeToken();
      this.token = null;
      this.user = null;
      this.error = null;
    },
  },
});
