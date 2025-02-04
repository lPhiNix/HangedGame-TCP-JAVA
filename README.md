# HangedGame-TCP-JAVA

## ✨ Descripción General

**HangedGame** es una aplicación cliente/servidor basada en TCP que permite jugar a un juego de adivinanza de refranes. Existen dos modalidades de juego: **individual** y **multijugador**, donde los jugadores deben adivinar consonantes, comprar vocales y resolver el refrán oculto.

### Características principales:

- Servidor multihilo basado en sockets TCP, permitiendo hasta **50 jugadores simultáneos**.

- **Modo individual**: Adivina un refrán en solitario.

- **Modo multijugador**: Tres jugadores compiten en una sala para resolver el refrán.

- **Sistema de autenticación** con registro e inicio de sesión.

- **Persistencia de refranes** en un archivo externo.

- **Sistema de salas** para gestionar partidas multijugador.

- **Registro de eventos** con logs detallados.


---

## 🛠️ Instalación y Requisitos Previos

### ✅ Requisitos:

- **Java 17** o superior.

- **IDE recomendado**: IntelliJ IDEA / Eclipse.

- **Puerto TCP 2050** disponible.


### 🔄 Instalación:

1. Clona el repositorio:

    ```
    git clone https://github.com/usuario/ruleta-refranes.git
    cd ruleta-refranes
    ```

2. Compila el proyecto:

    ```
    javac -d out -sourcepath src src/server/Main.java
    ```

3. Ejecuta el servidor:

    ```
    java -cp out server.Main
    ```

4. Para ejecutar un cliente:

    ```
    java -cp out client.Main <IP_SERVIDOR>
    ```


---

## 🌐 Uso del Sistema

Tras iniciar el servidor y conectar clientes, los jugadores pueden ejecutar comandos.

### ✉ Lista de Comandos

| Comando        | Sintaxis                                          | Descripción                                                     |
| -------------- | ------------------------------------------------- | --------------------------------------------------------------- |
| `register`     | `/register <nombre> <contraseña>`                 | Permite a un cliente registrarse en el servidor.                |
| `login`        | `/login <nombre> <contraseña>`                    | Permite iniciar sesión con un usuario anteriormente registrado. |
| `user`         | `/user`                                           | Muestra información del usuario actual.                         |
| `rooms`        | `/rooms`                                          | Lista las salas activas en el servidor.                         |
| `singleplayer` | `/singleplayer`                                   | Inicia una partida en solitario.                                |
| `multiplayer`  | `/multiplayer <create\|join\|leave> [nombreSala]` | Gestiona partidas multijugador.                                 |
| `consonant`    | `/consonant <letra>`                              | Adivina una consonante en la partida actual.                    |
| `vowel`        | `/vowel <letra>`                                  | Compra y adivina una vocal.                                     |
| `solve`        | `/solve <proverbio...>`                           | Intenta resolver el refrán.                                     |
| `help`         | `/help`                                           | Muestra la lista de comandos disponibles.                       |

### 🎭 Modo de Juego

#### ⭐ Juego Individual:

1. Ejecutar `singleplayer`.

2. Introducir consonantes (`consonant <letra>`).

3. Comprar vocales (`vowel <letra>`).

4. Resolver (`solve <proverbio>`).


#### 🎮 Juego Multijugador (para 3 Jugadores):

1. Crear sala: `multiplayer create <nombreSala>`.

2. Unirse a una sala: `multiplayer join <nombreSala>`.

3. Abandonar sala: `multiplayer leave`.

4. Adivinar letras como en el modo individual.

5. Resolver la frase (`solve <proverbio>`).


---

## 📚 Arquitectura del Proyecto

- **Cliente/Servidor TCP** multihilo.

- **Patrones de diseño**:

    - _Factory Pattern_ (para comandos).

    - _Singleton_ (para gestión de usuarios y salas).

    - _Command_ (para gestión y ejecución de comandos).

- **Organización del código**:

    - `server/` ➡️ Lógica del servidor.

    - `client/` ➡️ Implementación del cliente.

    - `common/` ➡️ Recursos compartidos (logs, modelos, servicios).

### 🌳 Árbol de Ficheros del Proyecto

````text
src/
├── client/
│   └── Client.java                   # Clase principal para el cliente. Gestiona la conexión con el servidor y la interfaz de usuario.
├── common/
│   ├── game/
│   │   ├── Game.java                 # Clase base para gestionar el juego, lógica general del mismo.
│   │   └── HangedGame.java           # Lógica específica para el juego del ahorcado.
│   ├── logger/
│   │   ├── ColorLog.java             # Clase para generar logs con colores personalizados en la consola.
│   │   └── CustomLogger.java         # Clase que extiende de `Logger` para personalizar la salida de logs.
│   └── model/
│       ├── Proverb.java              # Modelo para representar un proverbio, usado en el juego.
│       ├── User.java                 # Modelo que representa un usuario, incluyendo información de autenticación.
│       └── Word.java                 # Modelo para representar una palabra, usada en el juego del ahorcado.
├── server/
│   ├── HangedServer.java             # Clase principal para el servidor. Gestiona las conexiones de los clientes.
│   ├── Server.java                   # Lógica de funcionamiento del servidor, incluyendo gestión de hilos y comunicaciones.
│   ├── command/
│   │   ├── Command.java              # Interfaz para los comandos ejecutados por el servidor.
│   │   └── CommandFactory.java       # Clase encargada de crear instancias de comandos según el tipo de mensaje.
│   ├── service/
│   │   ├── Service.java              # Clase base para los servicios del servidor.
│   │   └── services/
│   │       ├── CommandProcessor.java # Servicio que procesa los comandos enviados por los clientes.
│   │       ├── ProverbManager.java   # Servicio encargado de gestionar los proverbios y su selección.
│   │       ├── RoomManager.java      # Servicio para la gestión de salas en el juego multijugador.
│   │       └── UserManager.java      # Servicio que maneja la autenticación y registro de usuarios.
│   └── thread/
│       ├── AbstractWorker.java       # Clase abstracta para la gestión de hilos de trabajo en el servidor.
│       ├── ClientHandler.java        # Clase que gestiona la comunicación con cada cliente conectado.
│       └── Worker.java               # Clase que maneja el procesamiento en segundo plano de tareas del servidor.
└── test/
    ├── MainClient.java               # Clase de prueba para el funcionamiento de un cliente.
    └── MainServer.java               # Clase de prueba para el funcionamiento del servidor.
    
proverbs.txt                           # Archivo que contiene una lista de proverbios, que se usan en el juego para que los jugadores adivinen.
users.txt                              # Archivo donde se almacenan los datos de los usuarios registrados, usado para la autenticación y registro.

````
### 💎 Tecnologías Utilizadas

- **Java 17** (Lenguaje principal).

- **Sockets TCP** (Comunicación entre cliente y servidor).

- **Javadoc** (Documentación detallada del código).

- **Archivos de texto** (Persistencia de refranes).

- **Logging** (Registro de eventos en el servidor).


---

## 🌟 Mejoras Futuras

- **Interfaz gráfica (GUI)** para el cliente.

- **Sistema de puntuaciones y ranking**.

- **Base de datos** en lugar de archivos de texto.

- **Soporte para más jugadores en modo multijugador**.

- **Optimización de logs y manejo de errores**.


---

💫 _Desarrollado por [@lPhiNix](https://github.com/lPhiNix)(Pablo Martínez Pedrosa)