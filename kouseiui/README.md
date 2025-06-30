 
# agile-bpm-basic  Front-end Engineering Introduction

The front end is developed based on ElementPlus and supports modern mainstream browsers such as Chrome, Edge, Firefox, and Safari.
You can visit the ElementPlus official website to view the basic component API. You can also view the AgileBPM basic component API

## Quick Start

#### Install

Recommended use **VSCode** For development, please be sure to install [required plugins](./qianduan.html#vscode-Essential plugins)

1.Check the environment, node16 or 18, open the command window in the project directory

2. Install Dependencies `yarn install`

3. Start the service `npm run dev`

#### Initial Setup

Before starting development, there are some things that need to be set up in advance, namely:

1. Choose the right vsCode plugin for development
   vsCode Please refer to the plugin [vscode Configuring the plugin](./qianduan.html#vscode-Essential plugins)。
 
3. Change logo website：
   -  `public\img\logo.png`
   - `public\img\logo-r.png`

#### Configuration

Basic project configuration:`src\config\defaultConfig.ts` 
Request Configuration: `.env`

## Front-end Engineering Directory

```
agilebpm-ui
├── src
│   ├── api			           API Interface definition location
│   ├── assets		             	Static resources
│   ├── components	           Front-end components 		
│   ├── config		             	System Configuration				
│   ├── hooks		             	
│   ├── icon		             	
│   ├── layout		             	Front-end framework layout
│   ├── main.ts                 Entrance main js 
│   ├── router                  Routing
│   ├── store                   State Management
│   ├── style                   css
│   ├── utils                   Tools
│   ├── views                  
│   │   ├── biz              Page Sections 
│   │   ├── bpm                
│   │   ├── cms                
│   │   ├── home               
│   │   ├── login              
│   │   ├── news               
│   │   ├── org                
│   │   ├── sys                
├── types                      Type
├── package.json               Introduction
├── prettier.config.js         Style Definition
└── vite.config.ts             vite Configuration



```

## Add a new page

Adding a new page requires 3 steps:

- To create a new `.vue` page file, please refer to [General List Page Specifications](./qianduan.html#General List Page Specifications)
- Adding a new page to the route
- Configure this page in the menu

Here are the specific instructions:

#### Create a new .vue page file

All page files are managed in the `/src/views` directory. For maintainability, the pages are divided into directories by module. Please add files according to the module.

#### Adding a new page to the route

A page is mainly divided into two categories:

1. The page in the main frame includes the basic layout such as the top, sidebar, multiple tabs, etc. (most pages should belong to this type)
2. Pages outside the main frame include the home page, login/registration/registration result page, which are relatively independent and have no other layout.

Open the routing configuration file `src\router\index.ts` 。

If it is a page in the main frame, add the routing configuration to the children of `layout`, which contains the basic layout.
Please do not put other pages in the children of `layout`, such as the login page

Since there are usually many pages in the framework, we divide them into modules for maintainability, such as organizing routes. `src\router\modules\org.ts`
 

#### Routing FAQ

- After setting cache, the cache is invalid. It is not supported yet, but will be supported in the next version.
  If the cache is invalid, please note that the name of the route and the name of the .vue page component should be consistent.

#### Configure this page in the menu

Add a new menu resource in System-System Resources

1. Select the parent directory where you want to add the menu and click Add.
2. Enter resource information
   - Resource name: the name of the menu, which will override the route title
   - Resource alias: unique CODE, used for authentication in button permissions
   - Request address: the routing address. In the button permissions, the resource address is the address accessed by the interface backend. The address will also be matched with permissions.
   - Expand by default: The menu will be displayed in expanded form by default

## Build and Release

#### Build

Execute the command in the terminal to package the project
：
`$ npm run build `

After the build and packaging is successful, a `dist` folder will be generated in the root directory by default, which contains the built and packaged files, usually static files such as `**.js`, `*.css`, and `index.html`.

#### Release

The front-end is a typical SPA-type project. The packaged files are divided into index.html and other static resources. The entire project has only one entry file, index.html, and the rest are managed by Webpack.

Therefore, the core of publishing a SPA project is to render the index.html and the location of static resources.

Generally speaking, you may use Nginx, Apache, etc. to render the entry file index.html, or you can use CDN services, such as Qiniu.
For more information, please refer to [vue Official Documentation](https://cli.vuejs.org/zh/guide/deployment.html#pwa)

## General List Page Specifications

List page recommended mixin `abTableMix` component, which has built-in search,reset,handleCollapse,delBySeletedIds These methods are common to list pages.
Please refer to the user list page for details `src\views\org\user\userList.vue`，specific `ab-table` Component functions can be found in the API section.

## General Edit Page Specifications

Please refer to the edit page `src\views\org\role\roleEdit.vue`，Mainly used `ab-save` Components，`ab-load` Components are used to simplify parts of your code.

## vscode Essential plugins

vscode [Download](https://code.visualstudio.com/) Required plugin installations

- local-history (opens new window)local-history (Highly recommended, must be installed to retrieve lost codes)
- eslint (opens new window)eslint (Must be installed, it is recommended to enable Eslint to automatically fix when saving)
- stylelint (opens new window)stylelint (Must be installed)
- Prettier - Code formatter (opens new window) Code automatic formatting (must be installed)
- volar (opens new window)vue3 Essential for development
- Auto Import (opens new window)import Introduce autocompletion (recommended installation)
- Import Cost (opens new window) Check the size of the dependent modules you introduced (recommended installation)
- Auto Close Tag (opens new window) Automatically complete HTML tags (recommended)
- Auto Rename Tag (opens new window) Automatically rename html tags (recommended)
- vscode-element-helper (opens new window)element Essential for development (recommended)
- docthis (opens new window) Annotation plugin (optional)
- Git History (opens new window) View git commit history (optional)
- Svg Preview (opens new window)svg Viewer (optional)
