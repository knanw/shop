// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2024-04-03',
  devtools: { enabled: false },
  modules: ['vuetify-nuxt-module', '@pinia/nuxt'],
  vite: {
    server: {
      proxy: {
        '/api/v1': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          rewrite: path => path.replace('', '')
        }
      }
    },
    css: {
      preprocessorOptions: {
        scss: {
          additionalData: '@use "~/assets/global.css" as *;'
        }
      }
    }
  },
  pinia: {
    autoImport:["defineStore"],
  },
  css: [
    '~/assets/global.css'
  ],
  imports: {
    dirs: ["./stores"],
  },
  vuetify: {
    icons: {
      iconfont: 'mdi',
    },
    customVariables: ['~/assets/variables.scss'],
    theme: {
      dark: false,
      themes: {
        light: {
          primary: '#1976D2',
          secondary: '#424242',
          accent: '#82B1FF',
          error: '#FF5252',
          info: '#2196F3',
          success: '#4CAF50',
          warning: '#FFC107',
        },
        dark: {
          primary: '#2E7D32',
          secondary: '#FF8A65',
          accent: '#9E9E9E',
          error: '#D32F2F',
          info: '#1976D2',
          success: '#388E3C',
          warning: '#FBC02D',
        },
      },
    },
    defaultAssets: {
      icons: 'mdi',
      // Optionale Fonts falls benötigt
      // font: {
      //   family: 'Roboto',
      // }
    },
    moduleOptions: {},
    vuetifyOptions: {}
  },
})