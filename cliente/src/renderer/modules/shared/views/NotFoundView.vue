<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

function goHome() {
  router.push('/')
}
</script>

<template>
  <div class="not-found-container">
    <img
      src="https://media.tenor.com/3b1v7p8a5NoAAAAC/puppy-sad.gif"
      alt="Sad puppy"
      class="puppy-img"
    />
    <h1 class="error-title">404 - Página no encontrada</h1>
    <p class="error-text">
      Uy... parece que te perdiste. Esta página no existe.
    </p>
    <button class="go-home-button" @click="goHome">Volver al inicio</button>
  </div>
</template>

<style scoped>
.not-found-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: var(--primary-bg);
  color: var(--text-primary);
  min-height: 100vh;
  padding: 2rem;
  text-align: center;
}

.puppy-img {
  width: 200px;
  height: auto;
  margin-bottom: 1.5rem;
  border-radius: 16px;
  box-shadow: 0 0 20px rgba(255, 255, 255, 0.1);
}

.error-title {
  font-size: 2.5rem;
  margin-bottom: 0.5rem;
  font-weight: bold;
}

.error-text {
  font-size: 1.1rem;
  color: var(--text-secondary);
  margin-bottom: 1.5rem;
}

.go-home-button {
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
  background-color: var(--accent);
  border: none;
  color: white;
  border-radius: 0.5rem;
  cursor: pointer;
  transition: background-color 0.3s;
  font-weight: 600;
}

.go-home-button:hover {
  background-color: #4752c4;
}
</style>
