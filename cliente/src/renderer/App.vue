<script setup lang="ts">
import { ref, onMounted } from "vue";
import SplashScreen from "./components/SplashScreen.vue";
import LoginView from "./modules/auth/views/LoginView.vue";

const showLogin = ref(false);

onMounted(() => {
  setTimeout(() => {
    showLogin.value = true;
  }, 3000);
});

window.electronAPI.sendMessage("Hello from App.vue!");
</script>

<template>
  <Transition name="fade" mode="out-in">
    <component :is="showLogin ? 'RouterView' : SplashScreen" />
  </Transition>
</template>


<style scoped src="./style.css"></style>
