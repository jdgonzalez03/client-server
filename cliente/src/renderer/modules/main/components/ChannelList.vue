<template>
  <div class="channel-list">
    <div
      v-for="channel in channels"
      :key="channel.id"
      class="channel-item"
      :class="{ active: channel.id === activeChannelId }"
      @click="navigateToChannel(channel.id)"
    >
      <span class="channel-icon">#</span>
      <span class="channel-name">{{ channel.name }}</span>
      <span v-if="channel.unreadCount" class="unread-badge">{{
        channel.unreadCount
      }}</span>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();

// Lista de canales
const channels = ref([
  { id: 1, name: "general", unreadCount: 0 },
  { id: 2, name: "desarrollo", unreadCount: 3 },
  { id: 3, name: "diseño", unreadCount: 0 },
  { id: 4, name: "recursos", unreadCount: 1 },
  { id: 5, name: "random", unreadCount: 0 },
]);

// Verifica si un canal está activo
const isActiveChannel = (channelId) => {
  return (
    route.params.type === "channel" && parseInt(route.params.id) === channelId
  );
};

// Navega a un canal y resetea su contador
const navigateToChannel = (channelId) => {
  router.push(`/chat/channel/${channelId}`);

  const channel = channels.value.find((c) => c.id === channelId);
  if (channel) {
    channel.unreadCount = 0;
  }
};
</script>

<style scoped>
.channel-list {
  display: flex;
  flex-direction: column;
}

.channel-item {
  display: flex;
  align-items: center;
  padding: 0.5rem 0.8rem;
  border-radius: 4px;
  margin-bottom: 0.2rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.channel-item:hover {
  background-color: rgba(255, 255, 255, 0.05);
}

.channel-item.active {
  background-color: rgba(88, 101, 242, 0.2);
}

.channel-icon {
  margin-right: 0.5rem;
  color: var(--text-secondary);
  font-weight: bold;
}

.channel-name {
  flex: 1;
  color: var(--text-secondary);
}

.channel-item.active .channel-name,
.channel-item.active .channel-icon {
  color: var(--text-primary);
}

.unread-badge {
  background-color: var(--accent);
  color: white;
  font-size: 0.75rem;
  border-radius: 10px;
  padding: 0.1rem 0.4rem;
  min-width: 1.2rem;
  text-align: center;
}
</style>
