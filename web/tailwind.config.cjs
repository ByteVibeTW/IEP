module.exports = {
  purge: [],
  content: ['node_modules/preline/dist/*.js'],
  darkMode: false, // or 'media' or 'class'
  theme: {
    extend: {},
  },
  variants: {
    extend: {},
  },
  plugins: [require('preline/plugin')],
};
