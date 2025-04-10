<template>
  <div class="login-container">
    <div class="background-shape"></div>
    <div class="login-card">
      <div class="login-header">
        <div class="brand">
          <div class="rocket">🚀</div>
          <h1>UniChat</h1>
        </div>
        <h2>¡Bienvenido de nuevo!</h2>
      </div>

      <form class="login-form" @submit.prevent="handleLogin">
        <UiInput
          id="email"
          v-model="email"
          type="email"
          label="Correo electrónico"
          placeholder="tu@email.com"
          icon="✉️"
          required
          autocomplete="email"
        />

        <div class="password-group">
          <div class="label-wrapper">
            <label for="password">Contraseña</label>
            <a class="forgot-link" href="#" >¿Olvidaste tu contraseña?</a>
          </div>
          
          <UiInput
            id="password"
            v-model="password"
            :type="showPassword ? 'text' : 'password'"
            placeholder="Ingresa tu contraseña"
            icon="🔒"
            required
          >
            <button 
              type="button" 
              class="toggle-password" 
              @click="showPassword = !showPassword"
            >
              {{ showPassword ? '👁️' : '👁️‍🗨️' }}
            </button>
          </UiInput>
        </div>

        <div class="remember-me">
          <UiCheckbox v-model="rememberMe">Recordarme</UiCheckbox>
        </div>

        <UiButton 
          type="submit" 
          variant="primary" 
          size="medium" 
          :loading="isLoading"
        >
          Iniciar sesión
        </UiButton>
      </form>

      <div class="register-prompt">
        <span>¿No tienes una cuenta?</span>
        <button class="register-button" @click="router.push('/register')">Regístrate ahora</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

import UiInput from '../../../components/ui/UiInput.vue'
import UiButton from '../../../components/ui/UiButton.vue'
import UiCheckbox from '../../../components/ui/UiCheckBox.vue'

import { useAuthUser } from '../composables/useAuth'
import { useRouter } from 'vue-router'
import { User } from '../types/interfaces'

const router = useRouter()


const email = ref('')
const password = ref('')
const showPassword = ref(false)
const rememberMe = ref(false)
const isLoading = ref(false)
const { login, setUser } = useAuthUser()

const handleLogin = async () => {
  isLoading.value = true

  try {
    const res = await window.electronAPI.login({
      email: email.value,
      password: password.value,
    })
    console.log('Login response:', res)
    if (res.success) {
      login()
      setUser(res.user)
      router.push('/')
    }
  } catch (error) {
    console.error('Error al iniciar sesión:', error)
    isLoading.value = false
    return
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
@import '../../../assets/styles/colors.css';

.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: var(--primary-bg);
  position: relative;
  overflow: hidden;
}

.background-shape {
  position: absolute;
  width: 550px;
  height: 550px;
  background: var(--secondary-bg);
  border-radius: 30% 70% 70% 30% / 30% 30% 70% 70%;
  transform: rotate(-10deg);
  z-index: 1;
  opacity: 0.6;
  box-shadow: 0 0 40px rgba(88, 101, 242, 0.15);
  animation: morphing 15s linear infinite alternate;
}

.login-card {
  position: relative;
  z-index: 2;
  background-color: var(--secondary-bg);
  padding: 2.5rem;
  border-radius: 16px;
  width: 400px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.3);
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.login-header {
  text-align: center;
  margin-bottom: 1rem;
}

.brand {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  margin-bottom: 0.75rem;
}

.rocket {
  font-size: 1.8rem;
  animation: rocketShake 3s infinite;
}

.brand h1 {
  font-size: 1.8rem;
  font-weight: bold;
  background: linear-gradient(90deg, var(--accent) 0%, #7984ff 100%);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  margin: 0;
}

.login-header h2 {
  color: var(--text-primary);
  font-size: 1.4rem;
  font-weight: 500;
  margin: 0;
  opacity: 0.9;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.password-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.label-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

label {
  color: var(--text-primary);
  font-size: 0.9rem;
  font-weight: 500;
}

.forgot-link {
  color: var(--accent);
  font-size: 0.8rem;
  text-decoration: none;
  transition: color 0.2s ease;
}

.forgot-link:hover {
  color: #7984ff;
  text-decoration: underline;
}

.toggle-password {
  background: none;
  border: none;
  color: var(--text-secondary);
  cursor: pointer;
  font-size: 1.1rem;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.remember-me {
  margin-top: 0.25rem;
}

.register-prompt {
  text-align: center;
  margin-top: 0.75rem;
  color: var(--text-secondary);
  font-size: 0.9rem;
}

.register-prompt button {
  color: var(--accent);
  text-decoration: none;
  font-weight: 500;
  margin-left: 0.4rem;
  transition: color 0.2s ease;
}

.register-prompt button:hover {
  color: #7984ff;
  text-decoration: underline;
}

.register-button {
  background: none;
  border: none;
  color: var(--accent);
  font-weight: 500;
  margin-left: 0.4rem;
  cursor: pointer;
  font-size: 0.9rem;
  transition: color 0.2s ease, text-decoration 0.2s ease;
}

.register-button:hover {
  color: #7984ff;
  text-decoration: underline;
}

/* Animaciones */
@keyframes morphing {
  0% {
    border-radius: 30% 70% 70% 30% / 30% 30% 70% 70%;
  }
  25% {
    border-radius: 40% 60% 60% 40% / 40% 40% 60% 60%;
  }
  50% {
    border-radius: 30% 70% 60% 40% / 50% 60% 40% 50%;
  }
  75% {
    border-radius: 40% 60% 50% 50% / 30% 40% 60% 70%;
  }
  100% {
    border-radius: 30% 70% 70% 30% / 30% 30% 70% 70%;
  }
}

@keyframes rocketShake {
  0%, 100% {
    transform: rotate(0deg);
  }
  25% {
    transform: rotate(-5deg);
  }
  75% {
    transform: rotate(5deg);
  }
}

@media (max-width: 480px) {
  .login-card {
    width: 90%;
    padding: 1.8rem;
  }
}

@media (prefers-reduced-motion) {
  .rocket, .background-shape {
    animation: none;
  }
  
  .background-shape {
    border-radius: 40% 60% 60% 40% / 40% 30% 70% 60%;
  }
}
</style>