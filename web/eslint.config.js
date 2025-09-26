import js from '@eslint/js';
import prettierConfig from '@vue/eslint-config-prettier';
import vue from 'eslint-plugin-vue';
import globals from 'globals';

export default [
  { ignores: ['dist'] },

  // 基礎配置
  js.configs.recommended,
  ...vue.configs['flat/recommended'],
  prettierConfig,

  {
    files: ['**/*.{js,jsx,vue}'],
    languageOptions: {
      ecmaVersion: 2020,
      globals: {
        ...globals.browser,
        ...globals.node,
      },
    },
    rules: {
      // Vue 特定規則
      'vue/multi-word-component-names': 'off',
      'vue/no-unused-vars': 'error',
      'vue/no-multiple-template-root': 'off',
      'vue/html-self-closing': 'off',
      'vue/max-attributes-per-line': 'off',
      'vue/no-required-prop-with-default': 'off',

      // 一般 JavaScript 規則
      'no-unused-vars': 'error',
      'no-console': 'warn',
      'no-debugger': 'error',
      'prefer-const': 'error',
      'no-var': 'error',
      'no-undef': 'error',
    },
  },
  {
    files: ['**/*.config.{js,cjs}'],
    languageOptions: {
      ecmaVersion: 2020,
      globals: {
        ...globals.node,
      },
    },
    rules: {
      'no-undef': 'off',
    },
  },
];
