import type Keycloak from 'keycloak-js'

declare global {
  interface Window {
    keycloak: Keycloak
  }
}
