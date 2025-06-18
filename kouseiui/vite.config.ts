import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'
import AutoImport from 'unplugin-auto-import/vite'
export default defineConfig({
  plugins: [
    vue(),
    createSvgIconsPlugin({
      // 指定需要缓存的图标文件夹，地址可改
      iconDirs: [resolve(process.cwd(), 'src/icon/svg')],
      // 指定symbolId格式
      symbolId: 'icon-[dir]-[name]',
    }),
    AutoImport({
      // dts: 'src/auto-imports.d.ts', // 可以自定义文件生成的位置，默认是根目录下
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
    // `npm run dev`自动打开浏览器访问项目
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
