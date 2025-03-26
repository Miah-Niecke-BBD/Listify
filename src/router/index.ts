import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import TutorialView from '@/views/TutorialView.vue'
import LoadingView from '../views/LoadingView.vue'
import ProjectView from '@/views/ProjectView.vue'
import TaskList from '../views/Tasklist.vue'
import CalendarView from '../views/CalendarView.vue'
import TeamView from '@/views/TeamView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [

    {
      path: '/',
      name: 'login',
      component: LoginView,
      meta: { requiresSidebar: false },
    },
    {
      path: '/loading',
      name: 'loading',
      component: LoadingView,
    },
    {
      path: '/tutorial',
      name: 'tutorial',
      component: TutorialView,
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
      path: '/tasklist',
      name: 'tasklist',
      component: TaskList,
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
      path: '/team/:id/calendar',
      name: 'calendar',
      component: CalendarView,
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
      path: '/calendar',
      name: 'MyCalendar',
      component: CalendarView,
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
      path: '/team/:id',
      name: 'team',
      component: TeamView
    },
    {
      path: '/projects/:id',
      name: "project",
      component: ProjectView
    }
  ],
})

router.afterEach((to, from) => {
  if (to.name === 'team' && from.name === 'team' || to.name === 'project' && from.name === 'project'&& to.params.id !== from.params.id) {
    location.reload();
  }
});


export default router
