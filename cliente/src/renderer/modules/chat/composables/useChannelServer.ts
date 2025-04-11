// src/composables/useChannelServer.js
import { ref } from 'vue'

const serverMessages = ref<{ id: number; from: any; message: any; timestamp: any; avatar: string }[]>([])

export function useChannelServer() {
  const addServerMessage = (msg:any) => {
    const formatted = {
      id: Date.now(),
      from: msg.from,
      message: msg.message,
      timestamp: msg.timestamp,
      avatar: "https://img.freepik.com/free-vector/chatbot-avatar_78370-4708.jpg",
    }
    serverMessages.value.push(formatted)
  }

  const getServerMessages = () => {
    return serverMessages.value
  }

  return {
    serverMessages,
    addServerMessage,
    getServerMessages
  }
}
