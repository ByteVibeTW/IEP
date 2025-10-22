import { type DefaultOptions, QueryClient } from '@tanstack/vue-query';

// import { toast } from 'react-toastify';

type ErrorResponse = {
  response?: {
    data?: {
      message?: {
        message?: string;
      };
    };
  };
};

const defaultOptions: DefaultOptions = {
  queries: {
    refetchOnWindowFocus: false, // default: true
    retry: false,
  },
  mutations: {
    onSuccess: (data: unknown) => {
      const responseData = data as { data: unknown };
      console.debug(responseData.data);
    },
    onError: (error: unknown) => {
      const errorResponse = error as ErrorResponse;
      // toast.error(errorResponse.response?.data?.message?.message || '發生錯誤');
    },
  },
};

export const baseQueryClient = new QueryClient({
  defaultOptions,
});