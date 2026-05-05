import Axios from 'axios';

export const publicApi = Axios.create({
  baseURL: import.meta.env.VITE_BACKEND_API_URL || '/',
  headers: {
    accept: '*/*',
  },
});
