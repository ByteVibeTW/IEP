import { defineConfig } from 'orval';
import { loadEnv } from 'vite';

const env = loadEnv('development', process.cwd(), '');

export default defineConfig({
  api: {
    output: {
      mode: 'split',
      target: 'src/api/api.ts',
      schemas: 'src/api/model',
      client: 'vue-query',
      override: {
        mutator: {
          path: './src/api/base/BaseApi.ts',
          name: 'customInstant',
        },
      },
      mock: true,
      urlEncodeParameters: true,
    },
    input: {
      target: `${env.VITE_BACKEND_API_URL}/api/api-docs`,
    },
    hooks: {
      afterAllFilesWrite: 'prettier --write',
    },
  },
});