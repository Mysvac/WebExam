import { createMemoryHistory, createRouter } from 'vue-router'

import LoginAndRegister from "../components/LoginAndRegister.vue";
const MainView = () => import("../components/MainView.vue");
const _404View = () => import("../components/404View.vue")
const routes = [
    { path: '/', component: LoginAndRegister},
    { path: '/mainView', component: MainView },
    {path: '/404View', component: _404View},
]

const router = createRouter({
    history: createMemoryHistory(),
    routes,
})

export default router;