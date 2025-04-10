<template>
  <div class="user-info">
    <div class="avatar">
      <img
        src="https://img.freepik.com/free-vector/blue-circle-with-white-user_78370-4707.jpg?semt=ais_country_boost&w=740"
        alt="User Avatar"
      />
      <span class="status online"></span>
    </div>
    <div class="user-details">
      <h3 class="username">{{ user?.name }}</h3>
      <p class="status-text">En línea</p>
    </div>
    <button class="settings-button">
      <span class="settings-icon" @click="handleLogout">🔚</span>
    </button>
  </div>
</template>

<script setup lang="ts">
  import { useAuthUser } from '../../auth/composables/useAuth';
  import { useRouter } from 'vue-router';
  
  const router = useRouter();
  const {user, logout, clearUser} = useAuthUser();

  function handleLogout() {
    console.log('Logging out...');
    logout();
    clearUser();
    router.push('/login');
  }
</script>

<style scoped>
.user-info {
  display: flex;
  align-items: center;
  padding: 1rem;
  background-color: var(--secondary-bg);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.avatar {
  position: relative;
  margin-right: 0.8rem;
}

.avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.status {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  border: 2px solid var(--secondary-bg);
}

.online {
  background-color: var(--success);
}

.user-details {
  flex: 1;
}

.username {
  font-size: 1rem;
  color: var(--text-primary);
}

.status-text {
  font-size: 0.8rem;
  color: var(--text-secondary);
}

.settings-button {
  background: none;
  border: none;
  color: var(--text-secondary);
  cursor: pointer;
  font-size: 1.2rem;
}

.settings-button:hover {
  color: var(--text-primary);
}
</style>
