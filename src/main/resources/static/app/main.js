import header from '/app/module/header/header.js'
import menu from '/app/module/menu/menu.js'
import appLead from '/app/module/lead/lead.js'
import user from '/app/module/user/user.js'

/*
 * ANGULARJS 
 */
 var local_server_url = "";

 var app = angular.module('main', [
 	'app.module.header',
 	'app.module.menu',
 	'app.module.lead',
 	'app.module.user',
 	]);



