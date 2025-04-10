import {contextBridge, ipcRenderer} from 'electron';

contextBridge.exposeInMainWorld('electronAPI', {
  sendMessage: (message: string) => ipcRenderer.send('message', message),
  getIP: () => ipcRenderer.invoke('get-ip'),
  login: (data: { email: string; password: string }) =>
    ipcRenderer.invoke('login', data),

  register: (data: { name: string; email: string; password: string }) =>
    ipcRenderer.invoke('register', data),

  sendMessageServer: (data:{
    content: string;
    senderId: string;
    receiverId?: string;
    channelId?: number;
    
  }) => ipcRenderer.invoke('send-message-server', data),

  getUsers: () => ipcRenderer.invoke('get-connected-users'),

  onUserConnected: (callback: (user: any) => void) => {
    ipcRenderer.on("user-connected", (_, user) => {
      callback(user);
    })},

    onNewMessage: (callback) => ipcRenderer.on('new-message', (_event, message) => callback(message)),
});
