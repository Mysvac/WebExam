import { createMemoryHistory, createRouter } from 'vue-router'

import LoginAndRegister from "../components/LoginAndRegister.vue";
const MainView = () => import("../components/MainView.vue");
const routes = [
    { path: '/', component: LoginAndRegister},
    { path: '/mainView', component: MainView },
]

const router = createRouter({
    history: createMemoryHistory(),
    routes,
})

export default router;