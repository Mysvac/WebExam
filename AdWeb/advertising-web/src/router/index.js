import { createMemoryHistory, createRouter } from 'vue-router'

import LoginAndRegister from "../components/LoginAndRegister.vue";
import MainView from "../components/MainView.vue";
import OverView from "../components/viewComponents/OverView.vue";
const routes = [
    { path: '/', component: LoginAndRegister},
    { path: '/mainView', component: MainView },
]

const router = createRouter({
    history: createMemoryHistory(),
    routes,
})

export default router;