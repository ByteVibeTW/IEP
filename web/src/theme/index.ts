import { definePreset } from '@primeuix/themes';
import Aura from '@primeuix/themes/aura';

export const SkyBlueTheme = definePreset(Aura, {
  semantic: {
    primary: {
      50: '{sky.50}',
      100: '{sky.100}',
      200: '{sky.200}',
      300: '{sky.300}',
      400: '{sky.400}',
      500: '{sky.500}',
      600: '{sky.600}',
      700: '{sky.700}',
      800: '{sky.800}',
      900: '{sky.900}',
      950: '{sky.950}',
    },
    colorScheme: {
      light: {
        primary: {
          color: '{sky.600}',
          inverseColor: '#ffffff',
          hoverColor: '{sky.700}',
          activeColor: '{sky.800}',
        },
        highlight: {
          background: '{sky.50}',
          focusBackground: '{sky.100}',
          color: '{sky.900}',
          focusColor: '{sky.950}',
        },
      },
      dark: {
        primary: {
          color: '{sky.200}',
          inverseColor: '{sky.950}',
          hoverColor: '{sky.100}',
          activeColor: '{sky.50}',
        },
        highlight: {
          background: 'rgba(14, 165, 233, .18)',
          focusBackground: 'rgba(14, 165, 233, .28)',
          color: 'rgba(255,255,255,.92)',
          focusColor: 'rgba(255,255,255,.96)',
        },
      },
    },
  },
});

export const themeConfig = {
  theme: {
    preset: SkyBlueTheme,
    options: {
      darkModeSelector: 'none',
    },
  },
};
