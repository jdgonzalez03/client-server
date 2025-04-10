<template>
  <div class="main-view">
    <div class="header">
      <h1>UniChat</h1>
    </div>
    <div class="content">
      <SideBar />
      <main class="chat-area">
        <router-view />
      </main>
    </div>
    <Toast ref="toastRef" :duration="4000" />
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeMount } from 'vue';
import SideBar from '../components/SideBar.vue';
import Toast from '../components/Toast.vue';
import { provideToast } from '../composables/useToast';
import { useAuthUser } from '../../auth/composables/useAuth';

const toastRef = ref(null);
const toast = provideToast();
const { user } = useAuthUser();

onMounted(() => {
  // Set the toast ref for the service to use
  toast.setToastRef(toastRef.value);
  
  // Example: Listen for new messages from Electron API
  window.electronAPI.onNewMessage((message) => {
    // Only show toast for messages not from current user
    if (message.senderId !== user.value.id) {
      // Find sender name or use default
      let senderName = "Nuevo mensaje";
      if (message.senderName) {
        senderName = message.senderName;
      }
      
      toast.showMessage(
        senderName,
        message.content?.length > 50 
          ? message.content.substring(0, 50) + '...' 
          : message.content
      );
    }
  });
});
</script>

<style scoped>
.main-view {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: var(--primary-bg);
  color: var(--text-primary);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem 1rem;
  background-color: var(--secondary-bg);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.theme-toggle {
  background-color: var(--accent);
  color: var(--text-primary);
  border: none;
  border-radius: 4px;
  padding: 0.5rem 1rem;
  cursor: pointer;
  font-weight: 500;
}

.content {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.chat-area {
  flex: 1;
  overflow-y: auto;
  padding: 1rem;
}
</style>