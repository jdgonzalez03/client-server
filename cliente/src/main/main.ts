import {app, BrowserWindow, ipcMain, session} from 'electron';
import {join} from 'path';
import os from 'os'
import {initSocketConnection, sendLogin, sendRegister, getUsers, onUserConnected, sendMessage, onNewMessage} from './socket/socketManager';


function createWindow () {
  const mainWindow = new BrowserWindow({
    width: 1000,
    height: 900,
    webPreferences: {
      preload: join(__dirname, 'preload.js'),
      nodeIntegration: false,
      contextIsolation: true,
    }
  });

  if (process.env.NODE_ENV === 'development') {
    const rendererPort = process.argv[2];
    mainWindow.loadURL(`http://localhost:${rendererPort}`);
  }
  else {
    mainWindow.loadFile(join(app.getAppPath(), 'renderer', 'index.html'));
  }
}

app.whenReady().then(() => {
  createWindow();

  session.defaultSession.webRequest.onHeadersReceived((details, callback) => {
    callback({
      responseHeaders: {
        ...details.responseHeaders,
        'Content-Security-Policy': ['script-src \'self\'']
      }
    })
  })

  app.on('activate', function () {
    // On macOS it's common to re-create a window in the app when the
    // dock icon is clicked and there are no other windows open.
    if (BrowserWindow.getAllWindows().length === 0) {
      createWindow();
    }
  });

  initSocketConnection();
});

app.on('window-all-closed', function () {
  if (process.platform !== 'darwin') app.quit()
});

ipcMain.on('message', (event, message) => {
  console.log(message);
})

ipcMain.handle('get-ip', async () => {
  const interfaces = os.networkInterfaces()
  for (const name of Object.keys(interfaces)) {
    const iface = interfaces[name]
    if (!iface) continue
    for (const i of iface) {
      if (i.family === 'IPv4' && !i.internal) {
        return i.address // ← Retorna la IP
      }
    }
  }
  return null
})

ipcMain.handle('login', async (_event, data) => {
  try {
    const res = await sendLogin(data);
    return res;
  } catch (e) {
    return { 
      type: 'login-response', 
      success: false, 
      message: e instanceof Error ? e.message : String(e) 
    };
  }
});

ipcMain.handle('register', async (_event, data) => {
  try {
    const res = await sendRegister(data);
    return res;
  } catch (e) {
    return { 
      type: 'register-response', 
      success: false, 
      message: e instanceof Error ? e.message : String(e) 
    };
  }
});

ipcMain.handle('send-message-server', async (_event, data) => {
  try {
    const res = await sendMessage(data);
    return res;
  } catch (e) {
    return {
      type: 'send-message-response',
      success: false,
      message: e instanceof Error ? e.message : String(e),
    };
  }
});


ipcMain.handle('get-connected-users', async () => {
  try {
    const users = await getUsers();
    return users;
  } catch (e) {
    return {
      type: 'connected-users',
      success: false,
      message: e instanceof Error ? e.message : String(e),
    };
  }
});

onUserConnected((newUser) => {
  console.log("📡 Usuario conectado:", newUser);

  BrowserWindow.getAllWindows().forEach((win) => {
    win.webContents.send("user-connected", newUser);
  });
});

onNewMessage((message) => {
  console.log("📩 Nuevo mensaje desde el servidor:", message);

  BrowserWindow.getAllWindows().forEach((win) => {
    win.webContents.send("new-message", message);
  });
});