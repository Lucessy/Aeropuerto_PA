# Proyecto – Simulación de Aeropuertos Concurrentes

## 📌 Descripción

Este proyecto implementa una **simulación concurrente de aeropuertos**, donde se modelan aviones, buses, pistas, hangares y procesos de embarque/desembarque mediante **Java multihilo** y estructuras concurrentes.  
Incluye un **servidor central** que coordina la comunicación entre aeropuertos (Madrid y Barcelona) y permite observar la evolución del sistema en tiempo real a través de logs.

El objetivo es mostrar el uso de **programación concurrente**, **locks**, **semaforos** y **colas seguras** para simular procesos complejos de transporte aéreo.

---

## ⚙️ Funcionalidades principales

- **Modelo de Aeropuerto**  
  - Pistas con control mediante semáforos.  
  - Puertas de embarque y desembarque.  
  - Hangar y talleres de mantenimiento.  
  - Control de pasajeros con `AtomicInteger`.  

- **Aviones (Threads)**  
  - Simulan vuelos entre aeropuertos.  
  - Embarque y desembarque de pasajeros.  
  - Control de posición en pista y puertas.  

- **Buses (Threads)**  
  - Transportan pasajeros dentro del aeropuerto y hacia la ciudad.  
  - Coordinados con semáforos y colas concurrentes.  

- **Servidor Central**  
  - Coordina las operaciones de Madrid y Barcelona.  
  - Gestión de logs y mensajes en tiempo real.  
  - Control de pausa y reanudación del sistema.  

---

## 📂 Estructura del Proyecto

```
aeropuertos/
├── src/
│   ├── Aeropuerto.java
│   ├── Avion.java
│   ├── Bus.java
│   ├── HiloAux.java
│   ├── Log.java
│   ├── Servidor.java
│   └── Menu.java
├── README.md
└── logs/         # Archivos de seguimiento de la simulación
```

---

## 🚀 Ejecución

1. Compilar el proyecto:
```bash
javac -d bin src/*.java
```

2. Ejecutar el servidor central:
```bash
java -cp bin Servidor
```

3. Observar la simulación en consola y en los archivos de log.

---

## ✨ Aspectos Técnicos

- Uso de **`ConcurrentLinkedQueue`**, **`ArrayBlockingQueue`**, **`Semaphore`**, **`Lock`** y **`Condition`** para la gestión concurrente.  
- Control seguro de pasajeros mediante **`AtomicInteger`**.  
- Arquitectura basada en **threads** para simular aviones y buses en paralelo.  
- Logs detallados con timestamps para seguir la evolución del sistema.  

---

## 👩‍💻 Autora

- **Luciana Paola Díaz**
- **Sandra Hernández**
  
  Universidad de Alcalá – Escuela Politécnica Superior  
  Paradigmas Avanzados de Programación – 2025  
