<template>
	<div ref="" class="mobile-nav-button" @click="showMobileNav($event)" v-drag draggable="false"><el-icon><el-icon-menu /></el-icon></div>

	<el-drawer ref="mobileNavBox" title="Mobile menu" :size="240" v-model="nav" direction="ltr" :with-header="false" destroy-on-close>
		<el-container class="mobile-nav">
			<el-header>
				<div class="logo-bar"><img class="logo" src="/img/logo.png"><span>{{ $CONFIG.APP_NAME }}</span></div>
			</el-header>
			<el-main>
				<el-scrollbar>
					<el-menu :default-active="$route.meta.active || $route.fullPath" @select="select" router background-color="#212d3d" text-color="#fff" active-text-color="#409EFF">
						<NavMenu :navMenus="menu"></NavMenu>
					</el-menu>
				</el-scrollbar>
			</el-main>
		</el-container>
	</el-drawer>

</template>

<script>
	import NavMenu from './NavMenu.vue';

	export default {
		components: {
			NavMenu
		},
		data() {
			return {
				nav: false,
				menu: []
			}
		},
		computed:{

		},
		created() {
			var menu = this.$router.sc_getMenu()
			this.menu = this.filterUrl(menu)
		},

		watch: {

		},
		methods: {
			showMobileNav(e){
				var isdrag = e.currentTarget.getAttribute('drag-flag')
				if (isdrag == 'true') {
					return false;
				}else{
					this.nav = true;
				}

			},
			select(){
				this.$refs.mobileNavBox.handleClose()
			},
			// Convert external link routing
			filterUrl(map){
				var newMap = []
				map && map.forEach(item => {
					item.meta = item.meta?item.meta:{};
					// Handling hidden
					if(item.meta.hidden || item.meta.type=="button"){
						return false
					}
					// Processing http
					if(item.meta.type=='iframe'){
						item.path = `/i/${item.name}`;
					}
					// Recursive loop
					if(item.children&&item.children.length > 0){
						item.children = this.filterUrl(item.children);
					}
					newMap.push(item)
				})
				return newMap;
			}
		},
		directives: {
			drag(el){
				let oDiv = el; // Current element
				let firstTime='',lastTime='';
				// Disable text selection on web pages
				// document.onselectstart = function() {
				// 	return false;
				// };
				oDiv.onmousedown = function(e){
					// When the mouse is pressed, the distance between the current element and the visible area is calculated
					let disX = e.clientX - oDiv.offsetLeft;
					let disY = e.clientY - oDiv.offsetTop;
					document.onmousemove = function(e){
						oDiv.setAttribute('drag-flag', true);
						firstTime = new Date().getTime();
						// Calculate the distance moved through event delegation
						let l = e.clientX - disX;
						let t = e.clientY - disY;

						// Move the current element

						if(t > 0 && t < document.body.clientHeight - 50){
							oDiv.style.top = t + "px";
						}
						if(l > 0 && l < document.body.clientWidth - 50){
							oDiv.style.left = l + "px";
						}


					}
					document.onmouseup = function(){
						lastTime = new Date().getTime();
						if( (lastTime - firstTime)>200 ){
							oDiv.setAttribute('drag-flag', false);
						}
						document.onmousemove = null;
						document.onmouseup = null;
					};
					// If return false is not added, it may cause sticking, that is, when dragging to a place, the div sticks to the mouse and does not come down, which is equivalent to the failure of onmouseup
					return false;
				};
			}
		}
	}
</script>

<style scoped>
	.mobile-nav-button {position: fixed;bottom:10px;left:10px;z-index: 10;width: 50px;height: 50px;background: #409EFF;box-shadow: 0 2px 12px 0 rgba(64, 158, 255, 1);border-radius: 50%;display: flex;align-items: center;justify-content: center;}
	.mobile-nav-button i {color: #fff;font-size: 20px;}

	.mobile-nav {background: #212d3d;}
	.mobile-nav .el-header {background: transparent;border: 0;}
	.mobile-nav .el-main {padding:0;}
	.mobile-nav .logo-bar {display: flex;align-items: center;font-weight: bold;font-size: 20px;color: #fff;}
	.mobile-nav .logo-bar img {width: 30px;margin-right: 10px;}
	.mobile-nav .el-submenu__title:hover {background: #fff!important;}
</style>
