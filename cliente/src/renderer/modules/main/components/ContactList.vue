<template>
  <div class="contact-list">
    <button @click="loadUsers">🔄 Recargar usuarios</button>
    <div
      v-for="contact in contacts"
      :key="contact.id"
      class="contact-item"
      :class="{ active: isActiveContact(contact.id) }"
      @click="navigateToContact(contact.id)"
    >
      <div class="contact-avatar">
        <img :src="contact.avatar" :alt="`${contact.name} avatar`" />
        <span class="status" :class="contact.status"></span>
      </div>
      <div class="contact-details">
        <span class="contact-name">{{ contact.name }}</span>
        <span class="contact-status-text">{{
          getStatusText(contact.status)
        }}</span>
      </div>
      <span v-if="contact.unreadCount" class="unread-badge">{{
        contact.unreadCount
      }}</span>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useContacts } from "../../chat/composables/useContacts";

const route = useRoute();
const router = useRouter();

const { setContacts, contacts } = useContacts();
const error = ref(null);

const loadUsers = async () => {
  const res = await window.electronAPI.getUsers();
  const aux = res.map((user) => {
    return {
      id: user.id,
      name: user.name,
      avatar:
        user.avatar ||
        "https://img.freepik.com/free-vector/blue-circle-with-white-user_78370-4707.jpg?semt=ais_country_boost&w=740",
      status: user.status,
      unreadCount: 0, // Inicializa el contador de mensajes no leídos
    };
  });
  setContacts(aux);
};
onMounted(async () => {
  try {
    await loadUsers();

    // window.api.onUserConnected((newUser) => {
    //   const exists = contacts.value.find((u) => u.id === newUser.id);
    //   if (exists) {
    //     const updated = contacts.value.map((u) =>
    //       u.id === newUser.id ? { ...u, status: newUser.status } : u
    //     );
    //     setContacts(updated);
    //   } else {
    //     setContacts([
    //       ...contacts.value,
    //       {
    //         id: newUser.id,
    //         name: newUser.name,
    //         avatar: newUser.avatar || 'https://img.freepik.com/free-vector/blue-circle-with-white-user_78370-4707.jpg?semt=ais_country_boost&w=740',
    //         status: newUser.status,
    //         unreadCount: 0,
    //       },
    //     ]);
    //   }
    // });
  } catch (err) {
    console.error("Error al obtener los usuarios:", err);
    error.value = "Error al obtener los usuarios";
  }
});

// Mapeo de texto para los estados
const statusMap = {
  online: "En línea",
  away: "Ausente",
};

// Determina si el contacto está activo basado en la ruta
const isActiveContact = (contactId) => {
  return (
    route.params.type === "contact" && parseInt(route.params.id) === contactId
  );
};

// Navega al contacto y marca como leído
const navigateToContact = (contactId) => {
  router.push(`/chat/contact/${contactId}`);

  const contact = contacts.value.find((c) => c.id === contactId);
  if (contact) {
    contact.unreadCount = 0;
  }
};

// Traduce el estado
const getStatusText = (status) => {
  return statusMap[status] || "Desconocido";
};
</script>

<style scoped>
.contact-list {
  display: flex;
  flex-direction: column;
}

.contact-item {
  display: flex;
  align-items: center;
  padding: 0.5rem 0.8rem;
  border-radius: 4px;
  margin-bottom: 0.2rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.contact-item:hover {
  background-color: rgba(255, 255, 255, 0.05);
}

.contact-item.active {
  background-color: rgba(88, 101, 242, 0.2);
}

.contact-avatar {
  position: relative;
  margin-right: 0.8rem;
}

.contact-avatar img {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
}

.status {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 10px;
  height: 10px;
  border-radius: 50%;
  border: 2px solid var(--secondary-bg);
}

.online {
  background-color: var(--success);
}

.away {
  background-color: var(--warning);
}

.contact-details {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.contact-name {
  color: var(--text-primary);
  font-size: 0.9rem;
}

.contact-status-text {
  font-size: 0.75rem;
  color: var(--text-secondary);
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
