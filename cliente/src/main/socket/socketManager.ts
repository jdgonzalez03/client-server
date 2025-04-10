import net, { Socket } from "net";

let client: Socket | null = null;
let isConnected = false;

type Callback = () => void;
type ErrorCallback = (err: Error) => void;

//Manejar respuestas
let responseHandlers: Record<string, (response: any) => void> = {};

export function initSocketConnection(
  host: string = "127.0.0.1",
  onConnect?: Callback,
  onClose?: Callback,
  onError?: ErrorCallback
): void {
  client = new net.Socket();

  client.connect(12345, host, () => {
    isConnected = true;
    console.log("🔌 Conectado al servidor TCP");
    onConnect?.();
  });

  client.on("close", () => {
    isConnected = false;
    console.log("🚪 Conexión cerrada");
    onClose?.();
  });

  client.on("error", (err: Error) => {
    isConnected = false;
    console.error("⚠️ Error en la conexión TCP:", err);
    onError?.(err);
  });

  client.on('data', (data) => {
    const messages = data.toString().split('\n').filter(Boolean);

    for (const msg of messages) {
      const parsed = JSON.parse(msg);
      const type = parsed.type;
  
      if (type && responseHandlers[type]) {
        responseHandlers[type](parsed);
        delete responseHandlers[type];
      }
  
      if (type && pushListeners[type]) {
        pushListeners[type].forEach((cb) => cb(parsed));
      }
    }
  });
}

export function disconnect(): void {
  if (client) {
    client.end();
    client.destroy();
    client = null;
    isConnected = false;
  }
}

export const sendLogin = (data: { email: string; password: string }) => {
  return new Promise((resolve, reject) => {
    const payload = JSON.stringify({ type: 'login', ...data });
    responseHandlers['login-response'] = (response) => {
      if (response.success) resolve(response);
      else reject(response);
    };
    client?.write(payload + '\n');
  });
};

export const sendRegister = (data: { name: string; email: string; ip:string; password: string }) => {
  return new Promise((resolve, reject) => {
    const payload = JSON.stringify({ type: 'register', ...data });
    responseHandlers['register-response'] = (response) => {
      if (response.success) resolve(response);
      else reject(response);
    };
    client?.write(payload + '\n');
  });
};

export const getUsers = (): Promise<any[]> => {
  return new Promise((resolve, reject) => {
    const payload = JSON.stringify({ type: "get-users" });

    responseHandlers["connected-users"] = (response) => {
      if (response.users) {
        resolve(response.users);
      } else {
        reject(new Error("No se recibieron usuarios"));
      }
    };

    client?.write(payload + "\n");
  });
};

export const sendMessage = (data: { from: string; to?: string; channelId?: number; message: string }) => {
  return new Promise((resolve, reject) => {
    const payload = JSON.stringify({ type: 'send-message', ...data });

    // Escuchamos la respuesta que viene con el tipo 'message-sent'
    responseHandlers['message-sent'] = (response) => {
      if (response.success) resolve(response.message);
      else reject(new Error(response.message));
    };

    client?.write(payload + '\n');
  });
};



//Eventos
let pushListeners: Record<string, ((data: any) => void)[]> = {};

export function onPushMessage(type: string, callback: (data: any) => void) {
  if (!pushListeners[type]) {
    pushListeners[type] = [];
  }
  pushListeners[type].push(callback);
}

export function onUserConnected(callback: (user: any) => void) {
  onPushMessage("user-connected", (data) => {
    callback(data.user);
  });
}

export function onNewMessage(callback: (message: any) => void) {
  onPushMessage("new-message", (data) => {
    callback(data.message);
  });
}