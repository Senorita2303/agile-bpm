import { svgIcon } from 'agilebpm'
import { App } from 'vue'
// const icons = require.context('.', true, /\.svg$/)
// icons.keys().map(icons)
// https://webpack.docschina.org/guides/dependency-management/#requirecontext
// Create your own context through require.context() function
const svgRequire = require.context('./svg', false, /\.svg$/)
// At this time, a require function is returned, which can accept a request parameter for require import.
// This function provides three properties, and you can get all svg icons through require.keys()
// Traverse the icons, pass the icons as requests to the require import function, and complete the import of local svg icons
svgRequire.keys().forEach((svgIcon) => {
  svgRequire(svgIcon)
})

export default (app: App): void => {
  app.component('svg-icon', svgIcon)
}
