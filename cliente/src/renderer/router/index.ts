import { createRouter, createWebHashHistory } from 'vue-router';
import LoginView from '../modules/auth/views/LoginView.vue';
import RegisterView from '../modules/auth/views/RegisterView.vue';
import MainView from '../modules/main/views/MainView.vue';
import NotFoundView from '../modules/shared/views/NotFoundView.vue';
import ChatView from '../modules/chat/views/ChatView.vue';

import { useAuthUser } from '../modules/auth/composables/useAuth';

const routes = [
  {
    path: '/',
    component: MainView,
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        redirect: '/chat/channel/1' // Redirige al primer canal por defecto
      },
      {
        path: 'chat/:type/:id',
        component: ChatView,
        props: true // Pasa los parámetros de la ruta como props al componente
      }
    ]
  },
  {
    path: '/login',
    component: LoginView
  },
  {
    path: '/register',
    component: RegisterView
  },
  {
    path: '/:pathMatch(.*)*',
    component: NotFoundView
  }
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
});

// 🚨 Protege las rutas
router.beforeEach((to) => {
  const { isLogging } = useAuthUser();

  if (to.meta.requiresAuth && !isLogging.value) {
    return { path: '/login' };
  }

  return true;
});

export default router;