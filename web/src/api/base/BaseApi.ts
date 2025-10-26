import { inject } from 'vue';
import Axios, { type AxiosRequestConfig } from 'axios';
import { removeToken, hasToken, getToken } from '@/utils/tokenManager'
import type Keycloak from 'keycloak-js';

const keycloak = inject<Keycloak>('keycloak', null as any)


export const apiBase = Axios.create({
  baseURL: import.meta.env.VITE_BACKEND_API_URL || '/',
  headers: {
    accept: '*/*',
  },
}); // use your own URL here or environment variable

// add a second `options` argument here if you want to pass extra options to each generated query
interface CancellablePromise<T> extends Promise<T> {
  cancel: () => void;
}

export const customInstant = <T = any,>(
  config: AxiosRequestConfig,
  options?: AxiosRequestConfig,
): CancellablePromise<T> => {
  const source = Axios.CancelToken.source();
  const promise = apiBase({
    ...config,
    ...options,
    cancelToken: source.token,
  }).then(({ data }) => data) as CancellablePromise<T>;

  promise.cancel = () => {
    source.cancel('Query was cancelled');
  };

  return promise;
};

// // In some case with react-query and swr you want to be able to override the return error type so you can also do it here like this
// export type ErrorType<Error> = AxiosError<Error>;
//
// export type BodyType<BodyData> = BodyData;
//
// // Or, in case you want to wrap the body type (optional)
// // (if the custom instance is processing data before sending it, like changing the case for example)
// export type BodyType<BodyData> = CamelCase<BodyData>;

export const apiBaseInstance = (config: AxiosRequestConfig) => {
  return apiBase(config);
};

// 設定 axios 的 request 攔截器, 檢查 token 是否過期.
apiBase.interceptors.request.use(
  (config) => {
    if (!hasToken()) {
      removeToken()
      keycloak?.logout();
      return Promise.reject(new Error('Token 已過期或不存在，請重新登入'));
    }

    config.headers.Authorization = `Bearer ${getToken()}`;

    // 處理檔案上傳：將 { file: Blob } 格式轉換為 FormData
    if (config.data && typeof config.data === 'object' && config.data.file instanceof Blob) {
      const formData = new FormData();
      formData.append('file', config.data.file);

      // 移除 Content-Type 標頭，讓瀏覽器自動設置為 multipart/form-data
      if (config.headers) {
        delete config.headers['Content-Type'];
      }

      config.data = formData;
    }

    return config;
  },
  (error) => {
    console.error('請求錯誤:', error);
    return Promise.reject(error);
  },
);

// 設定 axios 的 response 攔截器, 當 API 回傳錯誤時, 回傳錯誤.
// apiBase.interceptors.response.use(
//   (response) => response,
//   (error) => {
//     const status = error.response?.status;
//     console.debug('API 錯誤:', error);

//     return Promise.reject(error);
//   },
// );