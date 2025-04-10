import { User } from "../modules/auth/types/interfaces"; // Ajusta la ruta según tu estructura

export interface Message {
  id: string;
  content: string;
  senderId: string;
  receiverId?: string;
  channelId?: number;
  timestamp: string;
}

export default interface ElectronApi {
  sendMessage: (message: string) => void;
  getIP: () => Promise<string>;
  login: (data: { email: string; password: string }) => Promise<{
    type: string;
    success: boolean;
    message?: string;
    user: User;
  }>;
  register: (data: {
    name: string;
    email: string;
    ip: string;
    password: string;
  }) => Promise<{
    type: string;
    success: boolean;
    message?: string;
    user: User;
  }>;
  getUsers: () => Promise<{
    type: string;
    success: boolean;
    message?: string;
    users: User[];
  }>;
  sendMessageServer: (data: {
    content: string;
    senderId: string;
    receiverId?: string;
    channelId?: number;
  }) => Promise<{}>;
  onUserConnected: (callback: (user: User) => void) => void;
  onNewMessage: (callback: (message: Message) => void) => void;
}

declare global {
  interface Window {
    electronAPI: ElectronApi;
  }
}
