import Vue from 'vue'
import Router from 'vue-router'

import Login from "../views/Login";
import chat from "../views/Chat"

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            name: 'Login',
            component: Login
        },
        {
            path: '/chat/:username',
            name: 'Chat',
            component: chat
        },
    ]
});