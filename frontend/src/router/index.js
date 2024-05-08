import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'produce',
      component: () => import('../views/produce/index.vue')
    },
    {
      path: '/transport',
      name: 'transport',
      component: () => import('../views/transport/index.vue')
    },
    {
      path: '/trade',
      name: 'trade',
      component: () => import('../views/trade/index.vue')
    },
    {
      path: '/blocks',
      name: 'blocks',
      component: () => import('../views/blocks/index.vue')
    },
  ]
})

export default router
