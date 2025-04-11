Para poder modificar el cliente, que esta escrito usando el framework de ElectronJS, debe tener instalado nodeJS

- Comandos: 

1. npm install
2. npm run dev
3. npm run build:win (Para generar el instalador en la carpeta dist)


Para modificar la base de datos del servidor, ir al paquete config, seguido de databaseConfig y allí, en la clase dataBaseConfig configurar vuestras credenciales
es importante ejecutar antes la sentencia SQL en MySQL, y posteriormente ejecutar desde intelliJEA ejecutando el archivo main ubicado en el paquete MainServer/Main 
o generando el jar mediante mvn clean package.

Adicionalmente adjunto un video de lo que se esperaba lograr.

## funcionalidades del proyecto

- Login
- Register
- Comunicación entre contactos directos (solo mensajes de texto)
