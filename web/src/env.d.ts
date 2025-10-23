/// <reference types="vite/client" />

declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  // eslint-disable-next-line @typescript-eslint/no-explicit-any, @typescript-eslint/ban-types
  const component: DefineComponent<{}, {}, any>
  export default component
}

declare global {
  interface Window {
    keycloak: import('keycloak-js').default
  }
}

declare module '@vue/runtime-core' {
  interface ComponentCustomProperties {
    $keycloak: import('keycloak-js').default | null
  }
}
