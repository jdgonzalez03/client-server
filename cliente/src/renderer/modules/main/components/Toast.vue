<template>
  <transition-group 
    name="toast" 
    tag="div" 
    class="toast-container"
  >
    <div 
      v-for="toast in toasts" 
      :key="toast.id" 
      class="toast"
      :class="toast.type"
    >
      <div class="toast-icon">
        <span v-if="toast.type === 'message'">💬</span>
        <span v-else-if="toast.type === 'info'">ℹ️</span>
        <span v-else-if="toast.type === 'success'">✅</span>
        <span v-else-if="toast.type === 'warning'">⚠️</span>
        <span v-else-if="toast.type === 'error'">❌</span>
      </div>
      <div class="toast-content">
        <div class="toast-title">{{ toast.title }}</div>
        <div class="toast-message">{{ toast.message }}</div>
      </div>
      <button class="toast-close" @click="removeToast(toast.id)">×</button>
    </div>
  </transition-group>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const props = defineProps({
  duration: {
    type: Number,
    default: 3000 // Default duration in milliseconds
  }
});

const toasts = ref([]);

const addToast = (toast) => {
  const id = Date.now();
  toasts.value.push({
    id,
    title: toast.title || '',
    message: toast.message || '',
    type: toast.type || 'info'
  });
  
  // Auto-remove toast after duration
  setTimeout(() => {
    removeToast(id);
  }, props.duration);
};

const removeToast = (id) => {
  const index = toasts.value.findIndex(toast => toast.id === id);
  if (index !== -1) {
    toasts.value.splice(index, 1);
  }
};

// Define methods to expose to parent component
defineExpose({
  addToast,
  removeToast
});
</script>

<style scoped>
:root {
  --primary-bg: #2b2d31;
  --secondary-bg: #1e1f22;
  --accent: #5865f2;
  --text-primary: #ffffff;
  --text-secondary: #b9bbbe;
  --success: #57f287;
  --error: #ed4245;
  --warning: #faa61a;
  --font-main: 'Poppins', sans-serif;
}


.toast-container {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 9999;
  max-width: 350px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  font-family: var(--font-main, 'Poppins', sans-serif);
}

.toast {
  display: flex;
  padding: 12px 16px;
  background-color: var(--primary-bg, #2b2d31);
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
  animation: slideIn 0.3s ease;
  transition: all 0.3s ease;
  align-items: center;
  gap: 12px;
  border-left: 4px solid var(--accent, #5865f2);
  color: var(--text-primary, #ffffff);
}

.toast.message {
  border-left-color: var(--accent, #5865f2);
}

.toast.info {
  border-left-color: var(--accent, #5865f2);
}

.toast.success {
  border-left-color: var(--success, #57f287);
}

.toast.warning {
  border-left-color: var(--warning, #faa61a);
}

.toast.error {
  border-left-color: var(--error, #ed4245);
}

.toast-icon {
  font-size: 20px;
}

.toast-content {
  flex-grow: 1;
}

.toast-title {
  font-weight: 600;
  margin-bottom: 4px;
  font-size: 14px;
  color: var(--text-primary, #ffffff);
}

.toast-message {
  font-size: 13px;
  color: var(--text-secondary, #b9bbbe);
}

.toast-close {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 20px;
  color: var(--text-secondary, #b9bbbe);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0;
  width: 24px;
  height: 24px;
  transition: color 0.2s ease;
}

.toast-close:hover {
  color: var(--text-primary, #ffffff);
}

/* Toast transitions */
.toast-enter-active {
  animation: slideIn 0.3s ease;
}

.toast-leave-active {
  animation: slideOut 0.3s ease;
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

@keyframes slideOut {
  from {
    transform: translateX(0);
    opacity: 1;
  }
  to {
    transform: translateX(100%);
    opacity: 0;
  }
}
</style>