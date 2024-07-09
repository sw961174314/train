import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('../views/main.vue'),
    meta: {
      loginRequire: true
    },
    children: [{
      path: 'welcome',
      component: () => import('../views/main/welcome.vue')
    },{
      path: 'about',
      component: () => import('../views/main/about')
    },{
      path: 'station',
      component: () => import('../views/main/business/station')
    },{
      path: 'train',
      component: () => import('../views/main/business/train')
    },{
      path: 'train-station',
      component: () => import('../views/main/business/train-station')
    },{
      path: 'train-carriage',
      component: () => import('../views/main/business/train-carriage')
    },{
      path: 'train-seat',
      component: () => import('../views/main/business/train-seat')
    },{
      path: 'batch/job',
      name: 'batch/job',
      component: () => import('../views/main/batch/job')
    }]
  },
  {
    path: '',
    redirect: '/welcome'
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router