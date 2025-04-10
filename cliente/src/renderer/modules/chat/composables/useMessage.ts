import { ref, computed } from 'vue';

// Definimos la interfaz para un mensaje
interface ChatMessage {
  id: string | number;
  author: string;
  senderId?: string;
  receiverId?: string;
  avatar: string;
  text: string;
  time: string;
  // Otros campos que puedas necesitar
}

// Estructura de datos para almacenar mensajes por chat
// La clave es chatType:chatId (ej: "contact:1" o "channel:2")
const messagesStore = ref<Record<string, Array<ChatMessage>>>({});

export function useMessages() {
  // Obtener mensajes para un chat específico
  const getMessages = (chatType: string, chatId: number | string) => {
    const key = `${chatType}:${chatId}`;
    if (!messagesStore.value[key]) {
      messagesStore.value[key] = [];
    }
    return messagesStore.value[key];
  };

  // Añadir un mensaje a un chat específico
  const addMessage = (chatType: string, chatId: number | string, message: ChatMessage) => {
    const key = `${chatType}:${chatId}`;
    if (!messagesStore.value[key]) {
      messagesStore.value[key] = [];
    }
    messagesStore.value[key].push(message);
  };

  // Inicializar mensajes para un chat específico
  const setMessages = (chatType: string, chatId: number | string, messages: Array<ChatMessage>) => {
    const key = `${chatType}:${chatId}`;
    messagesStore.value[key] = messages;
  };

  // Limpiar todos los mensajes
  const clearAllMessages = () => {
    messagesStore.value = {};
  };

  // Limpiar mensajes de un chat específico
  const clearChatMessages = (chatType: string, chatId: number | string) => {
    const key = `${chatType}:${chatId}`;
    messagesStore.value[key] = [];
  };

  // Obtener todos los chats que tienen mensajes
  const getAllChatsWithMessages = () => {
    return Object.keys(messagesStore.value).map(key => {
      const [type, id] = key.split(':');
      return {
        type,
        id: isNaN(Number(id)) ? id : Number(id),
        messageCount: messagesStore.value[key].length
      };
    });
  };

  return {
    messagesStore,
    getMessages,
    addMessage,
    setMessages,
    clearAllMessages,
    clearChatMessages,
    getAllChatsWithMessages
  };
}