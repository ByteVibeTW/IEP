import js from '@eslint/js';
import vue from 'eslint-plugin-vue';
import globals from 'globals';

export default [
    { ignores: ['dist'] },

    // 基礎配置
    js.configs.recommended,
    ...vue.configs['flat/recommended'],

    {
        files: ['**/*.{js,jsx,vue}'],
        languageOptions: {
            ecmaVersion: 2020,
            globals: globals.browser,
        },
        rules: {
            // Vue 特定規則
            'vue/multi-word-component-names': 'off',
            'vue/no-unused-vars': 'error',
            'vue/no-multiple-template-root': 'off',
            'vue/html-self-closing': 'off',
            'vue/max-attributes-per-line': 'off',

            // 一般 JavaScript 規則
            'no-unused-vars': 'error',
            'no-console': 'warn',
            'no-debugger': 'error',
            'prefer-const': 'error',
            'no-var': 'error',
        },
    }
];