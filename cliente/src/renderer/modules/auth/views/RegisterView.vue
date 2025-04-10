<template>
  <div class="register-container">
    <div class="background-shape"></div>
    <div class="register-card">
      <div class="register-header">
        <div class="brand">
          <div class="rocket">🚀</div>
          <h1>UniChat</h1>
        </div>
        <h2>Crea tu cuenta</h2>
        <p class="subtitle">
          Únete hoy y conecta con estudiantes de todo el mundo
        </p>
      </div>

      <form class="register-form" @submit.prevent="handleRegister">
        <UiInput
          id="firstName"
          v-model="firstName"
          label="Nombre"
          placeholder="Tu nombre"
          icon="👤"
          required
        />

        <UiInput
          id="email"
          v-model="email"
          type="email"
          label="Correo electrónico"
          placeholder="tu@universidad.edu"
          icon="✉️"
          required
          autocomplete="email"
        />

        <UiInput
          id="password"
          v-model="password"
          :type="showPassword ? 'text' : 'password'"
          label="Contraseña"
          placeholder="Crea una contraseña segura"
          icon="🔒"
          required
        >
          <button
            type="button"
            class="toggle-password"
            @click="showPassword = !showPassword"
          >
            {{ showPassword ? "👁️" : "👁️‍🗨️" }}
          </button>
        </UiInput>

        <UiInput
          id="confirmPassword"
          v-model="confirmPassword"
          :type="showConfirmPassword ? 'text' : 'password'"
          label="Confirmar contraseña"
          placeholder="Repite tu contraseña"
          icon="🔒"
          required
        >
          <button
            type="button"
            class="toggle-password"
            @click="showConfirmPassword = !showConfirmPassword"
          >
            {{ showConfirmPassword ? "👁️" : "👁️‍🗨️" }}
          </button>
        </UiInput>

        <div class="terms-section">
          <UiCheckbox v-model="agreeTerms">
            Acepto los <a href="#" class="terms-link">Términos de servicio</a> y
            la
            <a href="#" class="terms-link">Política de privacidad</a>
          </UiCheckbox>
        </div>

        <UiButton
          type="submit"
          variant="primary"
          size="medium"
          :loading="isLoading"
          :disabled="!formIsValid"
        >
          Crear cuenta
        </UiButton>
      </form>

      <div class="login-prompt">
        <span>¿Ya tienes una cuenta?</span>
        <a href="#">Inicia sesión</a>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from "vue";
import UiInput from "../../../components/ui/UiInput.vue";
import UiButton from "../../../components/ui/UiButton.vue";
import UiCheckbox from "../../../components/ui/UiCheckBox.vue";

import { useAuthUser } from "../composables/useAuth";
import { useRouter } from "vue-router";

const { login, setUser } = useAuthUser();
const router = useRouter();

const firstName = ref("");
const email = ref("");
const password = ref("");
const confirmPassword = ref("");
const showPassword = ref(false);
const showConfirmPassword = ref(false);
const agreeTerms = ref(false);
const isLoading = ref(false);

const formIsValid = computed(() => {
  return (
    firstName.value.trim() !== "" &&
    email.value.includes("@") &&
    password.value.length >= 6 &&
    password.value === confirmPassword.value &&
    agreeTerms.value
  );
});

const handleRegister = async () => {
  if (!formIsValid.value) return;

  isLoading.value = true;
  const ip = await window.electronAPI.getIP();

  try {
    const res = await window.electronAPI.register({
      name: firstName.value,
      ip: ip,
      email: email.value,
      password: password.value,
    })

    if (res.success) {
      console.log("Registro exitoso:", res);
      login()
      setUser(res.user)
      router.push('/')
    }
  } catch (error) {
    console.error("Error al iniciar sesión:", error);
    isLoading.value = false;
    return;
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
@import "../../../assets/styles/colors.css";

.register-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: var(--primary-bg);
  position: relative;
  overflow: hidden;
  padding: 2rem 0;
}

.background-shape {
  position: absolute;
  width: 650px;
  height: 650px;
  background: var(--secondary-bg);
  border-radius: 35% 65% 65% 35% / 35% 35% 65% 65%;
  transform: rotate(15deg);
  z-index: 1;
  opacity: 0.6;
  box-shadow: 0 0 40px rgba(88, 101, 242, 0.15);
  animation: morphing 15s linear infinite alternate;
}

.register-card {
  position: relative;
  z-index: 2;
  background-color: var(--secondary-bg);
  padding: 2.5rem;
  border-radius: 16px;
  width: 500px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.3);
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.register-header {
  text-align: center;
  margin-bottom: 0.5rem;
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

.register-header h2 {
  color: var(--text-primary);
  font-size: 1.4rem;
  font-weight: 500;
  margin: 0;
  margin-bottom: 0.5rem;
  opacity: 0.9;
}

.subtitle {
  color: var(--text-secondary);
  font-size: 0.95rem;
  margin: 0;
}

.register-form {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.name-fields {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.terms-section {
  margin-top: 0.5rem;
}

.terms-link {
  color: var(--accent);
  text-decoration: none;
  transition: color 0.2s ease;
}

.terms-link:hover {
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

.login-prompt {
  text-align: center;
  margin-top: 0.75rem;
  color: var(--text-secondary);
  font-size: 0.9rem;
}

.login-prompt a {
  color: var(--accent);
  text-decoration: none;
  font-weight: 500;
  margin-left: 0.4rem;
  transition: color 0.2s ease;
}

.login-prompt a:hover {
  color: #7984ff;
  text-decoration: underline;
}

/* Animaciones */
@keyframes morphing {
  0% {
    border-radius: 35% 65% 65% 35% / 35% 35% 65% 65%;
  }
  25% {
    border-radius: 40% 60% 60% 40% / 40% 40% 60% 60%;
  }
  50% {
    border-radius: 35% 65% 55% 45% / 50% 55% 45% 50%;
  }
  75% {
    border-radius: 45% 55% 50% 50% / 35% 45% 55% 65%;
  }
  100% {
    border-radius: 35% 65% 65% 35% / 35% 35% 65% 65%;
  }
}

@keyframes rocketShake {
  0%,
  100% {
    transform: rotate(0deg);
  }
  25% {
    transform: rotate(-5deg);
  }
  75% {
    transform: rotate(5deg);
  }
}

@media (max-width: 560px) {
  .register-card {
    width: 90%;
    padding: 1.8rem;
  }

  .name-fields {
    grid-template-columns: 1fr;
  }
}

@media (prefers-reduced-motion) {
  .rocket,
  .background-shape {
    animation: none;
  }

  .background-shape {
    border-radius: 40% 60% 60% 40% / 40% 30% 70% 60%;
  }
}
</style>
