import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'
import AutoImport from 'unplugin-auto-import/vite'
export default defineConfig({
  plugins: [
    vue(),
    createSvgIconsPlugin({
      // Specify the icon folder that needs to be cached. The address can be changed
      iconDirs: [resolve(process.cwd(), 'src/icon/svg')],
      // Specify symbolId format
      symbolId: 'icon-[dir]-[name]',
    }),
    AutoImport({
      // dts: 'src/auto-imports.d.ts', // You can customize the location where the file is generated. The default is the root directory
      imports: ['vue', 'vue-router', 'pinia'],
    }),
  ],
  optimizeDeps: {
    include: ['agilebpm', 'abDesigner'],
  },

  resolve: {
    alias: [
      {
        replacement: resolve(__dirname, './src'),
        find: '@',
      },
      {
        find: 'vue',
        replacement: 'vue/dist/vue.esm-bundler.js',
      },
      {
        replacement: resolve(__dirname, '.'),
        find: '~',
      },
      {
        find: 'vue-i18n',
        replacement: 'vue-i18n/dist/vue-i18n.cjs.js',
      },
      {
        replacement: resolve(__dirname, './agpackage'),
        find: '@agilebpm',
      },
    ],
  },
  build: {
    commonjsOptions: {
      transformMixedEsModules: true,
    },
  },
  server: {
    port: 8088,
    // `npm run dev` Automatically open the browser to access the project
    open: (process.env.VITE_OPEN ?? 'true') === 'true',
    hmr: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8080/',
        ws: true,
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '/'),
      },
    },
  },
})
