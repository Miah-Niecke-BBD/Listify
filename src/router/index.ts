import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
// import HomeView from '../views/HomeView.vue'
import LoadingView from '../views/LoadingView.vue'
import ProjectView from '@/views/ProjectView.vue'
import HomeView from "@/views/HomeView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/loading',
      name: 'loading',
      component: LoadingView,
    },
    {
      path: '/home',
      name: 'home',
      component: HomeView,
      beforeEnter: (to, from, next) => {
        const jwtToken = localStorage.getItem('jwtToken');
        if (jwtToken) {
          next();
        } else {
          next('/');
        }
      }
    },
    {
      path: '/project/:id',
      name: "project",
      component: ProjectView
    }
  ],
})

export default router
