import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        // target: 'http://110.40.206.206:8190',	
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      },
    },
    // client: {
    //   overlay: false   //关闭Uncaught runtime errors
    // }

  },
})
