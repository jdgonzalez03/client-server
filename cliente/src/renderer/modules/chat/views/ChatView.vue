<template>
  <div class="chat-view">
    <div class="chat-header">
      <div class="chat-header-info">
        <span v-if="chatType === 'channel'" class="channel-icon">#</span>
        <span v-else class="contact-status" :class="activeChat.status"></span>
        <h2 class="chat-title">{{ activeChat.name }}</h2>
      </div>
      <div class="chat-header-actions">
        <button class="header-button">📎</button>
        <button class="header-button">👥</button>
        <button class="header-button">🔍</button>
      </div>
    </div>

    <div class="messages-container">
      <div v-if="chatMessages.length === 0" class="no-messages">
        📨 Chatea con {{ activeChat.name }} ya
      </div>

      <div v-else>
        <div 
          v-for="message in chatMessages" 
          :key="message.id" 
          class="message"
          :class="{ 'message-outgoing': isOwnMessage(message) }"
        >
          <div class="message-avatar" v-if="!isOwnMessage(message)">
            <img :src="message.avatar" :alt="`${message.author} avatar`" />
          </div>
          <div class="message-content" :class="{ 'own-message-content': isOwnMessage(message) }">
            <div class="message-header">
              <span class="message-author">{{ isOwnMessage(message) ? 'Tú' : message.author }}</span>
              <span class="message-time">{{ message.time }}</span>
            </div>
            <p class="message-text">{{ message.text }}</p>
          </div>
          <div class="message-avatar" v-if="isOwnMessage(message)">
            <img :src="message.avatar" :alt="'Tu avatar'" />
          </div>
        </div>
      </div>
    </div>

    <div class="message-input-container">
      <input
        type="text"
        class="message-input"
        placeholder="Escribe un mensaje..."
        v-model="newMessage"
        @keyup.enter="sendMessage"
      />
      <div class="message-actions">
        <button class="action-button">😀</button>
        <button class="action-button">📎</button>
        <button @click="sendMessage" class="action-button send-button" :disabled="!newMessage.trim()">📨</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from "vue";
import { useRoute } from "vue-router";
import { useContacts } from "../composables/useContacts";
import { useMessages } from "../composables/useMessage";
import { useAuthUser } from "../../auth/composables/useAuth";

const { contacts } = useContacts();
const { getMessages, addMessage, setMessages } = useMessages();
const { user } = useAuthUser();
const route = useRoute();

const newMessage = ref("");
const activeChat = ref({});

// Datos de ejemplo para canales
const channelsData = {
  1: { id: 1, name: "general", messages: [] },
  2: {
    id: 2,
    name: "desarrollo",
    messages: [
      {
        id: 1,
        author: "Ana García",
        avatar: "https://via.placeholder.com/40",
        text: "Hola a todos! Alguien puede ayudarme con un problema de Vue Router?",
        time: "10:30",
      },
      {
        id: 2,
        author: "Carlos Ruiz",
        avatar: "https://via.placeholder.com/40",
        text: "Claro, ¿qué necesitas saber?",
        time: "10:32",
      },
      {
        id: 3,
        author: "Ana García",
        avatar: "https://via.placeholder.com/40",
        text: "Estoy tratando de implementar rutas anidadas pero tengo un error con los componentes hijos",
        time: "10:33",
      },
      {
        id: 4,
        author: "Miguel Ángel",
        avatar: "https://via.placeholder.com/40",
        text: "Te recomiendo revisar la documentación oficial, tiene ejemplos muy buenos sobre rutas anidadas. También puedes compartir tu código aquí y lo revisamos.",
        time: "10:35",
      },
    ],
  },
  3: { id: 3, name: "diseño", messages: [] },
  4: { id: 4, name: "recursos", messages: [] },
  5: { id: 5, name: "random", messages: [] },
};

const chatType = computed(() => route.params.type);
const chatId = computed(() => parseInt(route.params.id));

// Computed para obtener mensajes del chat actual desde el store global
const chatMessages = computed(() => {
  return getMessages(chatType.value, chatId.value);
});

// Función para verificar si el mensaje fue enviado por el usuario actual
const isOwnMessage = (message) => {
  return message.senderId === user.value.id || message.author === user.value.name;
};

const loadChatData = () => {
  const type = chatType.value;
  const id = chatId.value;

  if (type === "channel") {
    if (channelsData[id]) {
      activeChat.value = channelsData[id];
      // Inicializar los mensajes en el store global
      setMessages(type, id, channelsData[id].messages);
    }
  } else if (type === "contact") {
    const contact = contacts.value.find((c) => c.id === id);
    if (contact) {
      activeChat.value = contact;
      // Si no existen mensajes para este contacto, se inicializará un array vacío
      // en el getter getMessages
    }
  }
};

onMounted(() => {
  loadChatData();

  // Escuchar nuevos mensajes recibidos
  window.electronAPI.onNewMessage((message) => {
    console.log("🟢 Nuevo mensaje recibido:", message);
    
    // Solo procesamos mensajes que no son del usuario actual
    if(message.senderId !== user.value.id) {
      // Buscar el contacto correspondiente para obtener su nombre y avatar
      const sender = contacts.value.find(c => c.id === message.senderId);
      
      // Crear un objeto de mensaje para almacenar
      const newMsg = {
        id: message.id || Date.now(),
        author: sender?.name || "Usuario",
        senderId: message.senderId,
        avatar: sender?.avatar || "https://img.freepik.com/free-vector/blue-circle-with-white-user_78370-4707.jpg?semt=ais_country_boost&w=740",
        text: message.content,
        time: new Date(message.timestamp || Date.now()).toLocaleTimeString([], {
          hour: "2-digit",
          minute: "2-digit",
        }),
      };
      
      // Determinar dónde guardar el mensaje según si es de canal o contacto
      const msgChatType = message.channelId ? 'channel' : 'contact';
      const msgChatId = message.channelId || message.senderId;
      
      // Añadir el mensaje al store global
      addMessage(msgChatType, msgChatId, newMsg);
    }
  });
});

watch(() => route.params, loadChatData);

// Watch para detectar actualizaciones en los datos globales de contactos
watch(
  contacts,
  () => {
    if (chatType.value === "contact") {
      loadChatData();
    }
  },
  { deep: true }
);

const sendMessage = async () => {
  if (!newMessage.value.trim()) return;

  const newMsg = {
    id: Date.now(),
    author: user.value.name,
    senderId: user.value.id,
    avatar:
      "https://img.freepik.com/free-vector/blue-circle-with-white-user_78370-4707.jpg?semt=ais_country_boost&w=740",
    text: newMessage.value,
    time: new Date().toLocaleTimeString([], {
      hour: "2-digit",
      minute: "2-digit",
    }),
  };

  // Añadir el mensaje al store global
  addMessage(chatType.value, chatId.value, newMsg);

  try {
    const res = await window.electronAPI.sendMessageServer({
      content: newMessage.value,
      senderId: user.value.id,
      receiverId: activeChat.value.id,
    })
    console.log("Mensaje enviado:", res);
  } catch (error) {
    console.error("Error al enviar el mensaje:", error);
  }

  newMessage.value = "";
};
</script>

<style scoped>
/* Estilos base mejorados */
.chat-view {
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: #1e1e1e;
  color: #f5f5f5;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
}

/* Header del chat */
.chat-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background-color: #2a2a2a;
  border-bottom: 1px solid #3a3a3a;
}

.chat-header-info {
  display: flex;
  align-items: center;
}

.channel-icon {
  font-size: 20px;
  margin-right: 10px;
  font-weight: bold;
  color: #9c9c9c;
}

.contact-status {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  margin-right: 10px;
  box-shadow: 0 0 4px rgba(0, 0, 0, 0.2);
}

.contact-status.online {
  background-color: #4caf50;
}

.contact-status.away {
  background-color: #ff9800;
}

.contact-status.offline {
  background-color: #9c9c9c;
}

.chat-title {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
}

.chat-header-actions {
  display: flex;
}

.header-button {
  background: none;
  border: none;
  font-size: 16px;
  margin-left: 12px;
  cursor: pointer;
  color: #9c9c9c;
  transition: color 0.2s;
}

.header-button:hover {
  color: #fff;
}

/* Contenedor de mensajes */
.messages-container {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  scroll-behavior: smooth;
}

.no-messages {
  text-align: center;
  margin-top: 60px;
  color: #9c9c9c;
  font-size: 16px;
  padding: 20px;
}

/* Estilos de los mensajes */
.message {
  display: flex;
  margin-bottom: 16px;
  align-items: flex-end;
  max-width: 85%;
}

.message-outgoing {
  flex-direction: row-reverse;
  text-align: right;
  margin-left: auto;
}

.message-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 12px;
  flex-shrink: 0;
  border: 2px solid #3a3a3a;
}

.message-outgoing .message-avatar {
  margin-right: 0;
  margin-left: 12px;
}

.message-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.message-content {
  background-color: #2a2a2a;
  border-radius: 18px;
  padding: 10px 16px;
  position: relative;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
  min-width: 80px;
}

/* Estilo para mensajes propios */
.own-message-content {
  background-color: #1976d2; /* Azul más elegante */
  color: white;
}

.message-header {
  margin-bottom: 5px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.message-author {
  font-weight: 600;
  font-size: 13px;
  color: #ccc;
}

.own-message-content .message-author {
  color: rgba(255, 255, 255, 0.9);
}

.message-time {
  font-size: 11px;
  color: #999;
  margin-left: 8px;
}

.own-message-content .message-time {
  color: rgba(255, 255, 255, 0.7);
}

.message-text {
  margin: 0;
  word-break: break-word;
  font-size: 14px;
  line-height: 1.4;
}

/* Contenedor de input para mensaje */
.message-input-container {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  background-color: #2a2a2a;
  border-top: 1px solid #3a3a3a;
}

.message-input {
  flex: 1;
  padding: 10px 16px;
  border: 1px solid #3a3a3a;
  border-radius: 24px;
  font-size: 14px;
  outline: none;
  background-color: #373737;
  color: #f5f5f5;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.message-input:focus {
  border-color: #4a4a4a;
  box-shadow: 0 0 0 2px rgba(77, 144, 254, 0.2);
}

.message-input::placeholder {
  color: #9c9c9c;
}

.message-actions {
  display: flex;
  margin-left: 10px;
}

.action-button {
  background: none;
  border: none;
  font-size: 18px;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: 6px;
  cursor: pointer;
  color: #9c9c9c;
  transition: background-color 0.2s, color 0.2s;
}

.action-button:hover {
  background-color: #3a3a3a;
  color: #f5f5f5;
}

.send-button {
  background-color: #1976d2;
  color: white;
}

.send-button:hover {
  background-color: #1565c0;
  color: white;
}

.action-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  background-color: transparent;
}

/* Mejorar aspecto de burbujas de chat */
.message:not(.message-outgoing) .message-content {
  border-top-left-radius: 4px;
}

.message-outgoing .message-content {
  border-top-right-radius: 4px;
}

/* Añadir animación sutil al nuevo mensaje */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.message {
  animation: fadeIn 0.2s ease-out;
}
</style>